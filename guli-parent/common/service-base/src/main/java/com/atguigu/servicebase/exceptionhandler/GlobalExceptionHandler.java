package com.atguigu.servicebase.exceptionhandler;

import com.atguigu.commonutils.exception.ExceptionUtil;
import com.atguigu.commonutils.response.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author liukun
 * @description 服务端统一异常处理
 * @date 2020/6/6
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    //指定出现什么异常执行这个方法
    @ExceptionHandler(Exception.class)
    @ResponseBody //为了返回数据
    public R error(Exception e) {
        e.printStackTrace();
        log.error(ExceptionUtil.getMessage(e));
        return R.error().message("执行了全局异常处理..");
    }

    @ExceptionHandler(GuliException.class)
    @ResponseBody //为了返回数据
    public R error(GuliException e) {
        e.printStackTrace();
        log.error(ExceptionUtil.getMessage(e));
        return R.error().message(e.getMsg()).code(e.getCode());
    }
}
