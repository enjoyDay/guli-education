package com.atguigu.eduservice.entity.chapter;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liukun
 * @description
 * @since 2020/6/21
 */
@Data
@ApiModel(value = "课程发布信息")
public class CoursePublishVo {
    private String title;
    private String teacherName;
    private BigDecimal price;
    private Integer lessonNum;
    private String cover;
    private String subjectLevelOne;
    private String subjectLevelTwo;
}
