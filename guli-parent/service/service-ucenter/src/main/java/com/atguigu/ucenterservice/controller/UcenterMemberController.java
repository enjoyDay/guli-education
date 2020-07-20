package com.atguigu.ucenterservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.ordervo.UcenterMemberOrder;
import com.atguigu.commonutils.response.R;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.entity.vo.LoginInfoVo;
import com.atguigu.ucenterservice.entity.vo.LoginVo;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author liukun
 * @since 2020-06-29
 */
@RestController
@RequestMapping("/ucenterservice/ucenter-member")
@Api(description = "会员登录注册管理")
@CrossOrigin
public class UcenterMemberController {
    @Autowired
    private UcenterMemberService ucenterMemberService;

    // 登录，之所以先返回token是因为采用的jwt技术，这样可以跨域登录
    // 前端每次请求后会携带token,后端在接受请求前会判断是否有该用户，并且判断该用户是否被禁
    @ApiOperation(value = "会员登录")
    @PostMapping("/login")
    public R login(
            @ApiParam(name = "loginVo", value = "登录对象")
            @RequestBody LoginVo loginVo) {
        String token = ucenterMemberService.login(loginVo);
        return R.ok().data("token", token);
    }

    // 注册
    @ApiOperation(value = "会员注册")
    @PostMapping("register")
    public R register(
            @ApiParam(name = "registerVo", value = "注册对象")
            @RequestBody RegisterVo registerVo) {
        ucenterMemberService.register(registerVo);
        return R.ok();
    }

    @ApiOperation(value = "根据token获取登录信息")
    @GetMapping("auth/getLoginInfo")
    public R getLoginInfo(HttpServletRequest request) {
        try {
            String memberId = JwtUtils.getMemberIdByJwtToken(request);
            if ("".equals(memberId)) {
                return R.ok().data("item", null);
            }
            LoginInfoVo loginInfoVo = ucenterMemberService.getLoginInfo(memberId);
            return R.ok().data("item", loginInfoVo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001, "error");
        }
    }

    //根据用户id获取用户信息
    @PostMapping("getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id) {
        System.out.println("获取到的用户id:" + id);
        UcenterMember member = ucenterMemberService.getById(id);
        //把member对象里面值复制给UcenterMemberOrder对象
        UcenterMemberOrder ucenterMemberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(member, ucenterMemberOrder);
        System.out.println("远程调用返回的用户数据：" + ucenterMemberOrder);
        return ucenterMemberOrder;
    }

    @GetMapping(value = "countregister/{day}")
    public R registerCount(@PathVariable String day) {
        Integer count = ucenterMemberService.countRegisterByDay(day);
        return R.ok().data("countRegister", count);
    }
}

