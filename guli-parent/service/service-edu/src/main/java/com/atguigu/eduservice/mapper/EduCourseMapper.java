package com.atguigu.eduservice.mapper;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author liukun
 * @since 2020-06-20
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    /**
     * 根据课程id，编写sql语句查询课程信息
     * @param courseId 课程id
     * @return 课程信息
     */
    CourseWebVo getBaseCourseInfo(String courseId);
}
