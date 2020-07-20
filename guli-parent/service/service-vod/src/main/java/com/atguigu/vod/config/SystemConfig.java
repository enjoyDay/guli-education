package com.atguigu.vod.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author liukun
 * @description
 * @since 2020/6/17
 */
@Data
@Component
public class SystemConfig {
    private final static String HTTP = "http://";

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${video.path}")
    private String videoPath;

    @Value("${server.ip}")
    private String serverIp;

    @Value("${server.port}")
    private String serverPort;

    public String getServerIpPort() {
        return HTTP+serverIp+":"+serverPort;
    }
}
