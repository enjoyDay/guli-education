package com.atguigu.statisticservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author liukun
 * @description
 * @since 2020/7/12
 */
@SpringBootApplication
@MapperScan("com.atguigu.statisticservice.mapper")
@ComponentScan("com.atguigu")
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
public class StatisticApplication {
    public static void main(String[] args) {
        SpringApplication.run(StatisticApplication.class, args);
    }
}
