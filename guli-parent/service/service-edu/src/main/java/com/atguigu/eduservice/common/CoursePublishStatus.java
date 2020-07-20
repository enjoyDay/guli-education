package com.atguigu.eduservice.common;

/**
 * @author liukun
 * @description 课程发布状态
 * @since 2020/6/21
 */
public enum CoursePublishStatus {
    /**
     * 课程发布状态
     */
    NORMAL("Normal"),
    /**
     * 课程未发布状态
     */
    DRAFT("Draft");

    private CoursePublishStatus(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}
