package com.atguigu.vod.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author liukun
 * @description
 * @since 2020/6/18
 */
public class CommonUtil {
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    public static String getDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static String getDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    }
    public static void main(String[] args) {
        System.out.println(CommonUtil.getDate());
    }
}
