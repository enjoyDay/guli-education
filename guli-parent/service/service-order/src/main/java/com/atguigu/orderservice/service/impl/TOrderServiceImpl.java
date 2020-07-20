package com.atguigu.orderservice.service.impl;

import com.atguigu.commonutils.ordervo.CourseWebVoOrder;
import com.atguigu.commonutils.ordervo.UcenterMemberOrder;
import com.atguigu.orderservice.client.EduClient;
import com.atguigu.orderservice.client.UcenterClient;
import com.atguigu.orderservice.entity.TOrder;
import com.atguigu.orderservice.mapper.TOrderMapper;
import com.atguigu.orderservice.service.TOrderService;
import com.atguigu.orderservice.util.OrderNoUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author liukun
 * @since 2020-07-09
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {

    /**
     * 远程获取课程信息
     */
    @Autowired
    private EduClient eduClient;

    /**
     * 远程获取用户信息
     */
    @Autowired
    private UcenterClient ucenterClient;

    @Autowired
    private TOrderService orderService;


    @Override
    public String createOrders(String courseId, String memberId) {
        //通过远程调用根据用户id获取用户信息
        UcenterMemberOrder userInfoOrder = ucenterClient.getUserInfoOrder(memberId);

        //通过远程调用根据课程id获取课信息
        CourseWebVoOrder courseInfoOrder = eduClient.getCourseInfoOrder(courseId);

        // 先查询是否存在已经生成的订单并且是没支付的
        // 如果有则直接返回订单号
        QueryWrapper<TOrder> orderWrapper = new QueryWrapper();
        orderWrapper.eq("course_id", courseId);
        orderWrapper.eq("member_id", memberId);
        // 未支付状态
        orderWrapper.eq("status", 0);
        TOrder one = orderService.getOne(orderWrapper);
        if (one != null) {
            return one.getOrderNo();
        }

        //创建Order对象，向order对象里面设置需要数据
        TOrder order = new TOrder();
        //订单号
        order.setOrderNo(OrderNoUtil.getOrderNo());
        //课程id
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfoOrder.getTitle());
        order.setCourseCover(courseInfoOrder.getCover());
        order.setTeacherName(courseInfoOrder.getTeacherName());
        order.setTotalFee(courseInfoOrder.getPrice());
        order.setMemberId(memberId);
        order.setMobile(userInfoOrder.getMobile());
        order.setEmail(userInfoOrder.getEmail());
        order.setNickname(userInfoOrder.getNickname());
        //订单状态（0：未支付 1：已支付）
        order.setStatus(0);
        //支付类型 ，微信1
        order.setPayType(1);
        baseMapper.insert(order);
        //返回订单号
        return order.getOrderNo();
    }
}
