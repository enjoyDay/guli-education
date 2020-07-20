package com.atguigu.statisticservice.client;

import com.atguigu.commonutils.response.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author liukun
 * @description
 * @since 2020/7/12
 */
@Component
@FeignClient("service-ucenter")
public interface UcenterClient {
    @GetMapping(value = "/ucenterservice/ucenter-member/countregister/{day}")
    public R registerCount(@PathVariable("day") String day);
}
