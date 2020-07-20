package com.atguigu.eduservice.client;

import com.atguigu.commonutils.ordervo.UcenterMemberOrder;
import com.atguigu.eduservice.entity.vo.UcenterMemberPay;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author liukun
 * @description
 * @since 2020/7/8
 */
@Component
@FeignClient(name="service-ucenter",fallback = UcenterClientImpl.class)
public interface  UcenterClient {
    //根据用户id获取用户信息
    @PostMapping("/ucenterservice/ucenter-member/getUserInfoOrder/{id}")
    public UcenterMemberOrder getUcenterPay(@PathVariable("id") String id);
}
