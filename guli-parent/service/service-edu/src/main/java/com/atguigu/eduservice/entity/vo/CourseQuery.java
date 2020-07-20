package com.atguigu.eduservice.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liukun
 * @description
 * @since 2020/6/22
 */
@Data
@ApiModel(value = "CourseQuery对象", description = "课程查询接受类")
public class CourseQuery {
    @ApiModelProperty(value = "课程名")
    private String title;
    @ApiModelProperty(value = "讲师ID")
    private String teacherId;
    @ApiModelProperty(value = "一级类别")
    private String subjectParentId;
    @ApiModelProperty(value = "二级类别")
    private String subjectId;
}
