package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.response.R;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author liukun
 * @since 2020-06-20
 */
@RestController
@RequestMapping("/eduservice/edu-video")
@CrossOrigin
@Api(description = "课时管理")
@Slf4j
public class EduVideoController {
    @Autowired
    private EduVideoService eduVideoService;

    /**
     * 添加小节
     *
     * @param eduVideo
     * @return
     */
    @PostMapping("addVideo")
    @ApiOperation(value = "添加小节")
    public R addVideo(@RequestBody EduVideo eduVideo) {
        eduVideoService.save(eduVideo);
        return R.ok();
    }

    /**
     * 删除小节
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    @ApiOperation(value = "删除小节")
    public R deleteVideo(@PathVariable String id) {
        // 先删除小节中视频
        R result = eduVideoService.deleteVideoByVideoId(id);
        if (result.getCode().equals(20001)) {
            return R.error().message(result.getMessage());
        }

        eduVideoService.removeById(id);
        return R.ok();
    }

    @PutMapping("updateVideo")
    @ApiOperation(value = "修改小节")
    public R updateVideo(@RequestBody EduVideo eduVideo) {
        boolean update = eduVideoService.updateById(eduVideo);
        if (update) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @GetMapping("{videoId}")
    @ApiOperation(value = "根据ID获取小节")
    public R getVideoByVideoId(@PathVariable String videoId) {
        EduVideo eduVideo = eduVideoService.getById(videoId);
        return R.ok().data("item", eduVideo);
    }

    @ApiOperation(value = "刪除视频文件")
    @PostMapping("/video-source/")
    public R removeVideo(@ApiParam(name = "videoOriginalPath", value = "视频真实路径", required = true)
                         @RequestBody String videoOriginalPath) {
        videoOriginalPath = URLDecoder.decode(videoOriginalPath);
        String test = videoOriginalPath.replace("=", "");
        log.info("传送的删除视频路径：" + test);
        boolean b = eduVideoService.removeVideoByVideoPath(test);
        return b ? R.ok().message("删除视频文件成功") : R.error().message("删除视频文件失败");
    }
}


