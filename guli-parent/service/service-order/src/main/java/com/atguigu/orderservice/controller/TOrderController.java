package com.atguigu.orderservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.response.R;
import com.atguigu.orderservice.entity.TOrder;
import com.atguigu.orderservice.service.TOrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author liukun
 * @since 2020-07-09
 */
@RestController
@RequestMapping("/orderservice/t-order")
@CrossOrigin
@Api(description = "订单管理")
public class TOrderController {
    @Autowired
    private TOrderService orderService;

    //1 根据课程ID生成订单的方法
    @ApiOperation(value = "根据课程ID生成订单的方法")
    @PostMapping("createOrder/{courseId}")
    public R saveOrder(
            @ApiParam(name = "courseId", value = "课程ID")
            @PathVariable String courseId, HttpServletRequest request) {
        //创建订单，返回订单号
        String orderNo =
                orderService.createOrders(courseId, JwtUtils.getMemberIdByJwtToken(request));
        return R.ok().data("orderId", orderNo);
    }

    //2 根据订单id查询订单信息
    @ApiOperation(value = "根据订单id查询订单信息")
    @GetMapping("getOrderInfo/{orderId}")
    public R getOrderInfo(@PathVariable String orderId) {
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderId);
        TOrder order = orderService.getOne(wrapper);
        return R.ok().data("item", order);
    }

    /**
     * 根据客户ID和课程ID查询订单信息
     *
     * @param memberid
     * @param id
     * @return
     */
    @GetMapping("isBuyCourse/{memberid}/{id}")
    public boolean isBuyCourse(@PathVariable String memberid,
                               @PathVariable String id) {
        //订单状态是1表示支付成功
        int count = orderService.count(new QueryWrapper<TOrder>().eq("member_id", memberid).eq("course_id", id).eq("status", 1));
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    @GetMapping("test")
    public R test() {
        return R.ok().data("success", "测试成功");
    }
}

