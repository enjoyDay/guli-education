package com.atguigu.vod.controller;


import com.atguigu.commonutils.response.R;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.vod.entity.EduVideo;
import com.atguigu.vod.service.EduVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author liukun
 * @since 2020-07-07
 */
@Api(description = "阿里云视频管理")
@RestController
@CrossOrigin
@RequestMapping("/vodservice/edu-video")
public class EduVideoController {
    @Autowired
    private EduVideoService eduVideoService;

    @ApiOperation(value = "根据视频id获取视频播放地址")
    @GetMapping("getPlayVideoUrl/{id}")
    public R getPlayVideoUrl(@PathVariable String id) {
        try {
            //创建初始化对象
            EduVideo eduVideo = eduVideoService.getById(id);
            return R.ok().data("videoUrl", eduVideo.getVideoSourcePath());
        } catch (Exception e) {
            throw new GuliException(20001, "获取视频地址连接失败");
        }
    }
}

