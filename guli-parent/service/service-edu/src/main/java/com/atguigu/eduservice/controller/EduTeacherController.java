package com.atguigu.eduservice.controller;


import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.commonutils.response.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 * <p>
 * 服务端解决跨域问题
 *
 * @author liukun
 * @since 2020-06-05
 */
@RestController
@CrossOrigin
@Api(description = "讲师管理")
@RequestMapping("/eduservice/edu-teacher")
public class EduTeacherController {
    @Autowired
    private EduTeacherService teacherService;

    /**
     * 查询所有讲师列表
     *
     * @return 所有讲师列表
     */
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("/findAll")
    public R list() {
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items", list);
    }

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "逻辑删除讲师")
    @DeleteMapping("/{id}")
    public R removeById(@ApiParam(name = "id", value = "讲师ID", required = true) @PathVariable String id) {
        teacherService.removeById(id);
        return R.ok();
    }

    //3 分页查询讲师的方法
    //current 当前页
    //limit 每页记录数
    @ApiOperation(value = "分页讲师列表")
    @GetMapping("/pageTeacher/{current}/{limit}")
    public R pageListTeacher(@ApiParam(name = "current", value = "当前页", required = true) @PathVariable long current,
                             @ApiParam(name = "limit", value = "每页记录数", required = true) @PathVariable long limit) {
        //创建page对象
//        Page<EduTeacher> pageTeacher = new Page<>(current, limit);
        //调用方法实现分页
        //调用方法时候，底层封装，把分页所有数据封装到pageTeacher对象里面
//        teacherService.page(pageTeacher, null);
//        long total = pageTeacher.getTotal();//总记录数
//        List<EduTeacher> records = pageTeacher.getRecords(); //数据list集合
//        Map map = new HashMap();
//        map.put("total",total);
//        map.put("rows",records);
//        return R.ok().data(map);
//        return R.ok().data("total", total).data("rows", records);

        Page<EduTeacher> pageParam = new Page<EduTeacher>(current, limit);
        Map<String, Object> map = teacherService.pageListWeb(pageParam);

        return  R.ok().data(map);
    }

    //4 条件查询带分页的方法
    @ApiOperation(value = "根据教师名，等级，开始时间，结束时间查询教师")
    @PostMapping("/pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@ApiParam(name = "current", value = "当前页", required = true)
                                  @PathVariable long current,

                                  @ApiParam(name = "limit", value = "每页记录数", required = true)
                                  @PathVariable long limit,

                                  @ApiParam(name = "teacherQuery", value = "查询条件", required = false)
                                  @RequestBody(required = false) TeacherQuery teacherQuery) {
        Page<EduTeacher> pageParam = new Page<>(current, limit);

        teacherService.pageQuery(pageParam, teacherQuery);

        List<EduTeacher> records = pageParam.getRecords();
        long total = pageParam.getTotal();

        return R.ok().data("total", total).data("rows", records);
    }

    @ApiOperation(value = "新增教师")
    @PostMapping("/addTeacher")
    public R addTeacher(@ApiParam(name = "eduTeacher", value = "教师对象", required = true)
                        @RequestBody EduTeacher eduTeacher) {
        boolean save = teacherService.save(eduTeacher);
        return save ? R.ok() : R.error();
    }

    //根据讲师id进行查询
    @ApiOperation(value = "根据讲师id进行查询")
    @GetMapping("/getTeacher/{id}")
    public R getTeacher(@ApiParam(name = "id", value = "教师ID", required = true)
                        @PathVariable String id) {
        EduTeacher teacher = teacherService.getById(id);
        return R.ok().data("items", teacher);
    }

    //讲师修改功能
    @ApiOperation(value = "讲师修改功能")
    @PostMapping("/updateTeacher")
    public R updateTeacher(@ApiParam(name = "eduTeacher", value = "教师对象", required = true)
                           @RequestBody EduTeacher eduTeacher) {
        boolean update = teacherService.updateById(eduTeacher);
        return update ? R.ok() : R.error();
    }

    @ApiOperation(value = "根据ID修改讲师")
    @PutMapping("/{id}")
    public R updateById(@ApiParam(name = "id", value = "讲师ID", required = true)
                        @PathVariable String id,
                        @ApiParam(name = "teacher", value = "讲师对象", required = true)
                        @RequestBody EduTeacher teacher) {
        teacher.setId(id);

        teacherService.updateById(teacher);
        return R.ok();
    }
}

