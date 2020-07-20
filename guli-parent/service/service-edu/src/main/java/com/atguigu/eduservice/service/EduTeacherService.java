package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author liukun
 * @since 2020-06-05
 */
public interface EduTeacherService extends IService<EduTeacher> {

    /**
     * 分页条件查询讲师
     *
     * @param pageParam    分页
     * @param teacherQuery 条件
     */
    void pageQuery(Page<EduTeacher> pageParam, TeacherQuery teacherQuery);

    /**
     * 分页获取讲师列表
     * @param pageParam 分页
     * @return 讲师列表
     */
    Map<String, Object> pageListWeb(Page<EduTeacher> pageParam);
}
