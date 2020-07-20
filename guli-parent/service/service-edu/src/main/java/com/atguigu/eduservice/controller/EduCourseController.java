package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.ordervo.CourseWebVoOrder;
import com.atguigu.commonutils.response.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.chapter.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author liukun
 * @since 2020-06-20
 */
@Api(description = "课程管理")
@RestController
@RequestMapping("/eduservice/edu-course")
@CrossOrigin
public class EduCourseController {
    @Autowired
    private EduCourseService eduCourseService;

    // 添加课程
    // 添加课程基本信息的方法
    @ApiOperation(value = "添加课程")
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        //返回添加之后课程id，为了后面添加大纲使用
        String id = eduCourseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId", id);
    }

    @ApiOperation(value = "根据ID查询课程")
    @GetMapping("course-info/{id}")
    public R getById(@ApiParam(name = "id", value = "课程ID", required = true) @PathVariable String id) {
        CourseInfoVo courseInfoForm = eduCourseService.getCourseInfoFormById(id);
        return R.ok().data("courseInfoVo", courseInfoForm);
    }

    @ApiOperation(value = "修改课程")
    @PutMapping("/updateCourseInfo")
    public R updateCourceInfo(@RequestBody CourseInfoVo courseInfoVo) {
        eduCourseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    //根据课程id查询课程确认信息
    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id) {
        CoursePublishVo coursePublishVo = eduCourseService.publishCourseInfo(id);
        return R.ok().data("publishCourse", coursePublishVo);
    }

    /**
     * 发布课程就是改变课程course的状态，
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id发布课程")
    @PutMapping("publish-course/{id}")
    public R publishCourseById(
            @ApiParam(name = "id", value = "课程ID", required = true)
            @PathVariable String id) {

        eduCourseService.publishCourseById(id);
        return R.ok();
    }

    @ApiOperation(value = "根据条件分页获取列表")
    @PostMapping("pageCourseCondition/{current}/{limit}")
    public R getCourseListPage(@ApiParam(name = "current", value = "当前页", required = true)
                               @PathVariable long current,

                               @ApiParam(name = "limit", value = "每页记录数", required = true)
                               @PathVariable long limit,

                               @ApiParam(name = "courseQuery", value = "查询条件", required = false)
                               @RequestBody(required = false) CourseQuery courseQuery) {
        Page<EduCourse> pageParam = new Page(current, limit);
        eduCourseService.pageQuery(pageParam, courseQuery);

        long total = pageParam.getTotal();
        List records = pageParam.getRecords();

        return R.ok().data("total", total).data("rows", records);
    }

    /**
     * 删除课程时，需要把课程描述、章节，小节信息都要删除
     * @param courseId
     * @return
     */
    @ApiOperation(value = "根据id删除")
    @DeleteMapping("{courseId}")
    public R deleteById(@PathVariable String courseId) {
        boolean b = eduCourseService.removeCourseById(courseId);

        return b==true? R.ok():R.error().message("删除失败");
    }
}

