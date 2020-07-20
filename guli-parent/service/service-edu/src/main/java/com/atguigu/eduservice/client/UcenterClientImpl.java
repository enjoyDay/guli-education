package com.atguigu.eduservice.client;

import com.atguigu.commonutils.ordervo.UcenterMemberOrder;
import com.atguigu.eduservice.entity.vo.UcenterMemberPay;
import org.springframework.stereotype.Component;

/**
 * @author liukun
 * @description
 * @since 2020/7/8
 */
@Component
public class UcenterClientImpl implements UcenterClient{

    /**
     * 远程调用失败会调用这个
     * @param id 用户id
     * @return 返回null表示失败
     */
    @Override
    public UcenterMemberOrder getUcenterPay(String id) {
        return null;
    }
}
