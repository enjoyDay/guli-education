package com.atguigu.msmservice.service;

import java.util.Map;

public interface MsmService {
    //发送短信的方法
    boolean sendMessage(Map<String, Object> param, String phone);

    // 发送邮箱
    void sendSimpleMail(String to, String param);
}
