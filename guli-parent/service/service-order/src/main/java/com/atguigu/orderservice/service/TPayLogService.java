package com.atguigu.orderservice.service;

import com.atguigu.orderservice.entity.TPayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author liukun
 * @since 2020-07-09
 */
public interface TPayLogService extends IService<TPayLog> {

   // 更新订单
    void updateOrdersStatus(Map<String, String> map);

    // 订单状态查询
    Map<String, String> queryPayStatus(String orderNo);

    // 包含二维码地址，还有其他需要的信息
    Map createNatvie(String orderNo);
}
