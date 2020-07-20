package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.atguigu.commonutils.response.R;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author liukun
 * @since 2020-06-20
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private VodClient vodClient;

    @Override
    public boolean removeVideoByVideoPath(String videoOriginalPath) {
        // 这里是用真实视频文件地址作为视频的id
        File videoFile = new File(videoOriginalPath);
        if (videoFile.exists()) {
            videoFile.delete();
        }

        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("video_original_path", videoOriginalPath);
        EduVideo video = this.getOne(queryWrapper);

        if (video == null) {
            return true;
        }

        video.setVideoOriginalName("");
        video.setVideoOriginalPath("");
        video.setVideoSourceId("");
        video.setVideoSourcePath("");
        boolean b = this.updateById(video);
        if (!b) {
            throw new GuliException(20001, "删除视频失败");
        }

        return b;
    }

    @Override
    public R deleteVideoByVideoId(String videoId) {
        EduVideo eduVideo = baseMapper.selectById(videoId);
        if (eduVideo == null) {
            return R.error().message("查询不到该视频数据");
        }

        String videoOriginalPath = eduVideo.getVideoOriginalPath();
        if (!StringUtils.isEmpty(videoOriginalPath)) {
            R r = vodClient.removeFile(videoOriginalPath);
            System.out.println("返回的r数据：" + r);
            if (r.getCode().equals(20000)) {
                return R.ok();
            } else {
                return r;
            }
        }
        return R.error().message("删除失败");
    }
}
