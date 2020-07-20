package com.atguigu.eduservice.service;

import com.atguigu.commonutils.response.R;
import com.atguigu.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author liukun
 * @since 2020-06-20
 */
public interface EduVideoService extends IService<EduVideo> {

    boolean removeVideoByVideoPath(String videoSourcePath);

    R deleteVideoByVideoId(String videoId);
}
