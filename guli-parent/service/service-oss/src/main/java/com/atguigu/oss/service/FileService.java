package com.atguigu.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author liukun
 * @description
 * @since 2020/6/17
 */
public interface FileService {
    /**
     * 文件上传
     * @param file 文件
     * @return 文件上传后路径
     */
    String upload(MultipartFile file);
}
