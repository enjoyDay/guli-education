package com.atguigu.vod.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author liukun
 * @description
 * @since 2020/6/18
 */
@Configuration
public class FileMvcConfig implements WebMvcConfigurer {

    @Value("${video.path}")
    private String videoPath;

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //文件磁盘图片url 映射
        //配置server虚拟路径，handler为前台访问的目录，locations为files相对应的本地路径

        registry.addResourceHandler(videoPath + "/**").addResourceLocations("file:" + uploadPath + "/");
    }
}
