package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.common.CoursePublishStatus;
import com.atguigu.eduservice.controller.EduVideoController;
import com.atguigu.eduservice.entity.*;
import com.atguigu.eduservice.entity.chapter.CoursePublishVo;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.*;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author liukun
 * @since 2020-06-20
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {
    //课程描述注入
    @Autowired
    private EduCourseDescriptionService courseDescriptionService;
    @Autowired
    private EduSubjectService eduSubjectService;
    @Autowired
    private EduTeacherService eduTeacherService;

    @Autowired
    private EduChapterService eduChapterService;
    @Autowired
    private EduVideoService eduVideoService;
    @Autowired
    private EduVideoController eduVideoController;

    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //1 向课程表添加课程基本信息
        //CourseInfoVo对象转换eduCourse对象
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if (insert == 0) {
            throw new GuliException(20001, "添加课程失败");
        }

        String id = eduCourse.getId();

        //2 向课程简介表添加课程简介
        //edu_course_description
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        eduCourseDescription.setId(id);
        courseDescriptionService.save(eduCourseDescription);
        return id;
    }

    @Override
    public CourseInfoVo getCourseInfoFormById(String id) {
        // 获取course课程信息
        EduCourse eduCourse = this.getById(id);
        if (eduCourse == null) {
            throw new GuliException(20001, "数据不存在");
        }
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse, courseInfoVo);
        // 获取course_description信息
        EduCourseDescription eduCourseDescription = courseDescriptionService.getById(eduCourse.getId());
        if (eduCourseDescription != null) {
            BeanUtils.copyProperties(eduCourseDescription, courseInfoVo);
        }
        return courseInfoVo;
    }

    /**
     * 修改课程
     *
     * @param courseInfoVo 前端传递过来的课程信息
     */
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int update = baseMapper.updateById(eduCourse);
        if (update == 0) {
            throw new GuliException(20001, "修改课程信息失败");
        }

        //2 修改描述表
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(courseInfoVo.getId());
        BeanUtils.copyProperties(courseInfoVo, eduCourseDescription);
        courseDescriptionService.updateById(eduCourseDescription);
    }

    /**
     * 根据课程ID获得课程发布信息
     *
     * @param courseId 课程id
     * @return 课程发布信息
     */
    @Override
    public CoursePublishVo publishCourseInfo(String courseId) {
        // 需要获取图像，共用课时，所属分类，课程讲师，价格
        CoursePublishVo coursePublishVo = new CoursePublishVo();

        EduCourse eduCourse = baseMapper.selectById(courseId);

        // 从教师管理中获取教师名
        String teacherId = eduCourse.getTeacherId();
        String teacherName = eduTeacherService.getById(teacherId).getName();

        // 从subject课程分类中获取一级分类和二级分类
        String subjectId = eduCourse.getSubjectId();
        String subjectParentId = eduCourse.getSubjectParentId();
        String subjectLevelTwo = eduSubjectService.getById(subjectId).getTitle();
        String subjectLevelOne = eduSubjectService.getById(subjectParentId).getTitle();

        // 从教程course中获取价格，课时数，图片，课程名
        BigDecimal price = eduCourse.getPrice();
        Integer lessonNum = eduCourse.getLessonNum();
        String cover = eduCourse.getCover();
        String title = eduCourse.getTitle();

        coursePublishVo.setTitle(title);
        coursePublishVo.setTeacherName(teacherName);
        coursePublishVo.setPrice(price);
        coursePublishVo.setLessonNum(lessonNum);
        coursePublishVo.setCover(cover);
        coursePublishVo.setSubjectLevelOne(subjectLevelOne);
        coursePublishVo.setSubjectLevelTwo(subjectLevelTwo);
        return coursePublishVo;
    }

    /**
     * 发布课程
     *
     * @param id 课程id
     */
    @Override
    public void publishCourseById(String id) {
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus(CoursePublishStatus.NORMAL.getValue());
        int updateNums = baseMapper.updateById(eduCourse);
        if (updateNums < 0) {
            throw new GuliException(20001, "课程发布失败");
        }
    }

    /**
     * 分页带条件查询
     *
     * @param pageParam   分页参数
     * @param courseQuery 条件
     */
    @Override
    public void pageQuery(Page pageParam, CourseQuery courseQuery) {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        // 根据sort进行排序
        queryWrapper.orderByDesc("gmt_create");

        if (courseQuery == null) {
            baseMapper.selectPage(pageParam, queryWrapper);
            return;
        }

        String title = courseQuery.getTitle();
        String teacherId = courseQuery.getTeacherId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String subjectId = courseQuery.getSubjectId();

        if (!StringUtils.isEmpty(title)) {
            queryWrapper.like("title", title);
        }

        if (!StringUtils.isEmpty(teacherId)) {
            queryWrapper.eq("teacher_id", teacherId);
        }

        if (!StringUtils.isEmpty(subjectParentId)) {
            queryWrapper.eq("subject_parent_id", subjectParentId);
        }

        if (!StringUtils.isEmpty(subjectId)) {
            queryWrapper.eq("subject_id", subjectId);
        }

        baseMapper.selectPage(pageParam, queryWrapper);
    }

    /**
     * 删除课程时，需要把课程描述、章节，小节信息都要删除
     *
     * @param courseId 课程id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeCourseById(String courseId) {
//        1 删除课程描述
        courseDescriptionService.removeById(courseId);

//        2 删除小节
        QueryWrapper<EduVideo> videoWrapper = new QueryWrapper();
        videoWrapper.eq("course_id", courseId);
        List<EduVideo> eduVideoList = eduVideoService.list(videoWrapper);
        if (eduVideoList != null) {
            for (int i = 0; i < eduVideoList.size(); i++) {
                String videoId = eduVideoList.get(i).getId();
                eduVideoController.deleteVideo(videoId);

            }
        }

//        2 删除章节信息
        QueryWrapper<EduChapter> chapterWrapper = new QueryWrapper();
        chapterWrapper.eq("course_id", courseId);
        eduChapterService.remove(chapterWrapper);

//        3 删除课程信息
        int b = baseMapper.deleteById(courseId);

        return b > 0;
    }

    @Override
    public List<EduCourse> selectByTeacherId(String teacherId) {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<EduCourse>();
        queryWrapper.eq("teacher_id", teacherId);
        //按照最后更新时间倒序排列
        queryWrapper.orderByDesc("gmt_modified");
        List<EduCourse> courses = baseMapper.selectList(queryWrapper);
        return courses;
    }

//    @Override
//    public Map<String, Object> pageListWeb(Page<EduCourse> pageParam) {
//        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper();
//        // 升序
//        queryWrapper.orderByAsc("sort");
//        IPage iPage = baseMapper.selectPage(pageParam, queryWrapper);
//
//        long current = iPage.getCurrent();
//        long pages = iPage.getPages();
//        long size = iPage.getSize();
//        long total = iPage.getTotal();
//        List records = iPage.getRecords();
//        boolean hasNext = pageParam.hasNext();
//        boolean hasPrevious = pageParam.hasPrevious();
//
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("items", records);
//        map.put("current", current);
//        map.put("pages", pages);
//        map.put("size", size);
//        map.put("total", total);
//        map.put("hasNext", hasNext);
//        map.put("hasPrevious", hasPrevious);
//
//        return map;
//    }

    /**
     * 条件查询带分页查询课程
     * @param pageParam 分页参数
     * @param courseFrontVo
     * @return
     */
    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> pageParam, CourseFrontVo courseFrontVo) {
        //2 根据讲师id查询所讲课程
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        //判断条件值是否为空，不为空拼接
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())) { //一级分类
            wrapper.eq("subject_parent_id",courseFrontVo.getSubjectParentId());
        }
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectId())) { //二级分类
            wrapper.eq("subject_id",courseFrontVo.getSubjectId());
        }
        if(!StringUtils.isEmpty(courseFrontVo.getBuyCountSort())) { //关注度
            wrapper.orderByDesc("buy_count");
        }
        if (!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort())) { //最新
            wrapper.orderByDesc("gmt_create");
        }

        if (!StringUtils.isEmpty(courseFrontVo.getPriceSort())) {//价格
            wrapper.orderByDesc("price");
        }

        baseMapper.selectPage(pageParam,wrapper);

        List<EduCourse> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();//下一页
        boolean hasPrevious = pageParam.hasPrevious();//上一页

        //把分页数据获取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        //map返回
        return map;
    }

    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {
        return baseMapper.getBaseCourseInfo(courseId);
    }
}
