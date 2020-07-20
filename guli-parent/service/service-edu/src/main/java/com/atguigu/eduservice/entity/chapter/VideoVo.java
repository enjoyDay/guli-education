package com.atguigu.eduservice.entity.chapter;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author liukun
 * @description 课程小节信息
 * @since 2020/6/21
 */
@Data
@ApiModel(value = "课时信息")
public class VideoVo {

    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private Boolean free;
}
