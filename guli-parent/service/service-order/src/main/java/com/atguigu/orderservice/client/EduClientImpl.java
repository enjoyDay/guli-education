package com.atguigu.orderservice.client;

import com.atguigu.commonutils.ordervo.CourseWebVoOrder;
import com.atguigu.commonutils.ordervo.EduCourseVo;
import com.atguigu.commonutils.response.R;
import org.springframework.stereotype.Component;

/**
 * @author liukun
 * @description
 * @since 2020/7/9
 */
@Component
public class EduClientImpl implements EduClient {
    @Override
    public CourseWebVoOrder getCourseInfoOrder(String id) {
        return null;
    }

    @Override
    public R updateCourse(EduCourseVo eduCourse) {
        return null;
    }

    @Override
    public R getCourseById(String id) {
        return null;
    }
}
