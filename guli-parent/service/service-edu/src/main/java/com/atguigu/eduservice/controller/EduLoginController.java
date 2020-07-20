package com.atguigu.eduservice.controller;

import com.atguigu.commonutils.response.R;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

/**
 * @author liukun
 * @description 后台登录
 * @date 2020/6/11
 */
@RestController
@RequestMapping("/eduservice/user")
@CrossOrigin
@Api(description = "登录")
public class EduLoginController {

    /**
     * login
     * @return
     */
    @PostMapping("/login")
    public R login() {
        // 这个key是根据前端定义的
        return R.ok().data("token","admin");
    }

    /**
     * info
     * @return
     */
    @GetMapping("/info")
    public R info() {
        return R.ok().data("roles","[admin]")
                .data("name","admin")
                .data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }
}
