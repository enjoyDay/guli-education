package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.ordervo.CourseWebVoOrder;
import com.atguigu.commonutils.response.R;
import com.atguigu.eduservice.client.OrderClient;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.frontvo.CourseFrontVo;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author liukun
 * @description
 * @since 2020/7/2
 */
@RestController
@RequestMapping("/eduservice/edu-course/front/")
@CrossOrigin
@Api(description = "前端获取课程")
public class EduCourseFrontController {
    @Autowired
    private EduCourseService eduCourseService;
    @Autowired
    private EduTeacherService eduTeacherService;
    @Autowired
    private EduChapterService eduChapterService;
    @Autowired
    private OrderClient orderClient;

    @ApiOperation(value = "根据讲师ID获取课程")
    @GetMapping("teacher-id/{teacherId}")
    public R getCourseByTeacherId(@PathVariable String teacherId) {
        EduTeacher teacher = eduTeacherService.getById(teacherId);

        List<EduCourse> courseList = eduCourseService.selectByTeacherId(teacherId);
        return R.ok().data("teacher", teacher).data("courseList", courseList);
    }

    //1 条件查询带分页查询课程
    @PostMapping("getFrontCourseList/{page}/{limit}")
    public R getFrontCourseList(@PathVariable long page, @PathVariable long limit,
                                @RequestBody(required = false) CourseFrontVo courseFrontVo) {
        Page<EduCourse> pageCourse = new Page<>(page, limit);
        Map<String, Object> map = eduCourseService.getCourseFrontList(pageCourse, courseFrontVo);
        //返回分页所有数据
        return R.ok().data(map);
    }

    //2 课程详情的方法
    @GetMapping("getFrontCourseInfo/{id}")
    public R getFrontCourseInfo(@PathVariable String id, HttpServletRequest request) {
        //根据课程id，编写sql语句查询课程信息
        CourseWebVo courseWebVo = eduCourseService.getBaseCourseInfo(id);

        //根据课程id查询章节和小节
        List<ChapterVo> chapterVideoList = eduChapterService.getChapterVideoByCourseId(id);

        //远程调用，判断课程是否被购买
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        boolean buyCourse = false;
        if (!StringUtils.isEmpty(memberId)){
            buyCourse = orderClient.isBuyCourse(memberId, id);
        }

        return R.ok()
                .data("courseWebVo", courseWebVo)
                .data("chapterVideoList", chapterVideoList)
                .data("isbuyCourse",buyCourse);
    }

    //根据课程id查询课程信息
    @GetMapping("getCourseInfoOrder/{id}")
    public CourseWebVoOrder getCourseInfoOrder(@PathVariable String id) {
        System.out.println("获取到的课程ID：" + id);
        CourseWebVo courseInfo = eduCourseService.getBaseCourseInfo(id);
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
        BeanUtils.copyProperties(courseInfo, courseWebVoOrder);
        return courseWebVoOrder;
    }

    @ApiOperation(value = "根据ID查询课程")
    @GetMapping("course-info/{id}")
    public R getCourseById(@ApiParam(name = "id", value = "课程ID", required = true) @PathVariable String id) {
        EduCourse eduCourse = eduCourseService.getById(id);
        return R.ok().data("eduCourse", eduCourse);
    }

    @ApiOperation(value = "根据ID更新课程")
    @PutMapping("course-info")
    public R updateCourse(@ApiParam(name = "EduCourse", value = "课程", required = true) @RequestBody EduCourse eduCourse) {
        boolean b = eduCourseService.updateById(eduCourse);

        return b ? R.ok() : R.error();
    }

}
