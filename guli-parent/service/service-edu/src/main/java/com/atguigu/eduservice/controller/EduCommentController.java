package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.ordervo.UcenterMemberOrder;
import com.atguigu.commonutils.response.R;
import com.atguigu.eduservice.client.UcenterClient;
import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.entity.vo.UcenterMemberPay;
import com.atguigu.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author liukun
 * @since 2020-06-20
 */
@RestController
@RequestMapping("/eduservice/edu-comment")
@CrossOrigin
@Api(description = "课程评论")
public class EduCommentController {
    @Autowired
    private EduCommentService commentService;
    @Autowired
    private UcenterClient ucenterClient;

    /**
     * 根据课程id分页查询评论列表
     * @param page 当前页
     * @param limit 每页数量
     * @param courseId 课程id
     * @return
     */
    @ApiOperation(value = "评论分页列表")
    @GetMapping("{page}/{limit}")
    public R getCommentList(@ApiParam(name = "page", value = "当前页码", required = true)
                            @PathVariable Long page,
                            @ApiParam(name = "limit", value = "每页记录数", required = true)
                            @PathVariable Long limit,
                            @ApiParam(name = "courseQuery", value = "查询对象", required = false)
                            @RequestParam("courseId")  String courseId) {
        Page<EduComment> pageParam = new Page<>(page, limit);

        QueryWrapper<EduComment> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        wrapper.orderByDesc("gmt_modified");
        commentService.page(pageParam,wrapper);

        List<EduComment> commentList = pageParam.getRecords();

        Map<String, Object> map = new HashMap<>();
        map.put("items", commentList);
        map.put("current", pageParam.getCurrent());
        map.put("pages", pageParam.getPages());
        map.put("size", pageParam.getSize());
        map.put("total", pageParam.getTotal());
        map.put("hasNext", pageParam.hasNext());
        map.put("hasPrevious", pageParam.hasPrevious());
        return R.ok().data(map);
    }

    /**
     * 保存评论
     * @param comment 评论
     * @param request 请求携带的token
     * @return
     */
    @ApiOperation(value = "添加评论")
    @PostMapping("auth/save")
    public R save(@RequestBody EduComment comment, HttpServletRequest request) {
        // 根据token获取会员id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)) {
            // 发送这个状态的原因是因为前端商量好的，
            // 前端接受响应的拦截器会查看状态码是不是28004，如果是直接拦截并跳转到登录界面上
            return R.error().code(28004).message("请登录");
        }

        comment.setMemberId(memberId);
        // 根据会员id获取会员信息
        UcenterMemberOrder ucenterInfo = ucenterClient.getUcenterPay(memberId);

        comment.setNickname(ucenterInfo.getNickname());
        comment.setAvatar(ucenterInfo.getAvatar());
        commentService.save(comment);

        return R.ok();
    }
}

