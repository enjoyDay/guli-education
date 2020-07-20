package com.atguigu.orderservice.client;

import com.atguigu.commonutils.ordervo.UcenterMemberOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author liukun
 * @description
 * @since 2020/7/9
 */
@Component
@FeignClient(value = "service-ucenter", fallback = UcenterClientImpl.class)
public interface UcenterClient {
    @PostMapping("/ucenterservice/ucenter-member/getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable("id") String id);
}
