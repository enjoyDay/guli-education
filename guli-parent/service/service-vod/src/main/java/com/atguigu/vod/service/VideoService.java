package com.atguigu.vod.service;

import com.atguigu.commonutils.exception.ExceptionUtil;
import com.atguigu.commonutils.response.R;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.vod.config.SystemConfig;
import com.atguigu.vod.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author liukun
 * @description
 * @since 2020/6/23
 */
@Service
@Slf4j
public class VideoService {
    @Autowired
    private SystemConfig systemConfig;

    public R upload(MultipartFile file) {
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        // 文件名，唯一标识符
        String fileName = CommonUtil.getUUID() + suffix;
        // 上传路径，把文件按照日期进行分类
        String uploadPath = systemConfig.getUploadPath() + File.separator + CommonUtil.getDate();
        File fileDirection = new File(uploadPath);
        if (!fileDirection.exists()) {
            fileDirection.mkdirs();
        }

        File newFile = new File(uploadPath + File.separator + fileName);
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            ExceptionUtil.getMessage(e);
            throw new GuliException(20001, "文件上传失败，请再试一次");
        }

        // 返回文件路径，这个路径时浏览器要访问的路径，而不是真实的路径，在资源映射处进行映射
        String videoPath = systemConfig.getVideoPath();
        String videoSourcePath = systemConfig.getServerIpPort() + videoPath + "/" + CommonUtil.getDate() + "/" + fileName;
        int end = videoSourcePath.lastIndexOf(".");
        int start = videoSourcePath.lastIndexOf("/");
        String videoSourceId = videoSourcePath.substring(start+1, end);

        return R.ok().message("文件上传成功")
                .data("videoSourceId", videoSourceId)
                .data("videoSourcePath", videoSourcePath)
                .data("videoOriginalName", file.getOriginalFilename())
                .data("videoOriginalPath",newFile);
    }
}
