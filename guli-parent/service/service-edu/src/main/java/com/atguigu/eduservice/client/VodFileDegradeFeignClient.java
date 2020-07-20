package com.atguigu.eduservice.client;

import com.atguigu.commonutils.response.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author liukun
 * @description
 * @since 2020/6/26
 */
@Component
@Slf4j
public class VodFileDegradeFeignClient implements VodClient {
    @Override
    public R removeFile(String filePath) {
      log.error("视频vod服务，删除视频文件服务请求超时");
        return R.error().message("time out");
    }
}
