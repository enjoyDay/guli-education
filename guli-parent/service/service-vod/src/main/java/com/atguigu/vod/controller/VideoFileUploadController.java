package com.atguigu.vod.controller;

import com.atguigu.commonutils.response.R;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.vod.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author liukun
 * @description
 * @since 2020/6/23
 */
@Api(description = "阿里云视频上传")
@RestController
@CrossOrigin
@RequestMapping("/vodservice/video")
public class VideoFileUploadController {
    @Autowired
    private VideoService videoService;

    @ApiOperation(value = "视频文件上传")
    @PostMapping("/upload")
    public R upload(
            @ApiParam(name = "file", value = "文件", required = true)
            @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return R.error().message("文件不能为空");
        }

       return videoService.upload(file);
    }

    @ApiOperation(value = "视频文件删除")
    @DeleteMapping("/delete")
    public R removeFile(@ApiParam(name = "filePath", value = "本地视频文件绝对路径", required = true)
                        @RequestBody String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return R.error().message("文件：" + filePath + "不存在");
        }
        boolean delete = file.delete();
        return R.ok();
    }
}
