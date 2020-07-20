package com.atguigu.orderservice.client;

import com.atguigu.commonutils.ordervo.CourseWebVoOrder;
import com.atguigu.commonutils.ordervo.EduCourseVo;
import com.atguigu.commonutils.response.R;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * @author liukun
 * @description
 * @since 2020/7/9
 */
@Component
@FeignClient(value = "service-edu", fallback = EduClientImpl.class)
public interface EduClient {
    //根据课程id查询课程信息
    @GetMapping("/eduservice/edu-course/front/getCourseInfoOrder/{id}")
    public CourseWebVoOrder getCourseInfoOrder(@PathVariable("id") String id);

    // 更新原始课程信息
    @PutMapping("/eduservice/edu-course/front/course-info")
    public R updateCourse(@RequestBody EduCourseVo eduCourse);

    // 根据课程id获取原始课程
    @GetMapping("/eduservice/edu-course/front/course-info/{id}")
    public R getCourseById(@PathVariable("id") String id);
}
