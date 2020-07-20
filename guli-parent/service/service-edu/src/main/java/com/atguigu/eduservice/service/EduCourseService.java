package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.chapter.CoursePublishVo;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author liukun
 * @since 2020-06-20
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoVo courseInfoVo);

    /**
     * 根据课程ID获取相关信息
     * @param id 课程ID
     * @return 表单信息
     */
    CourseInfoVo getCourseInfoFormById(String id);

    void updateCourseInfo(CourseInfoVo courseInfoVo);

    CoursePublishVo publishCourseInfo(String id);

    void publishCourseById(String id);

    void pageQuery(Page pageParam, CourseQuery courseQuery);

    boolean removeCourseById(String courseId);

    List<EduCourse> selectByTeacherId(String teacherId);

    Map<String, Object> getCourseFrontList(Page<EduCourse> pageCourse, CourseFrontVo courseFrontVo);

    /**
     * 根据课程id，编写sql语句查询课程信息
     * @param courseId 课程id
     * @return 课程信息
     */
    CourseWebVo getBaseCourseInfo(String courseId);
}
