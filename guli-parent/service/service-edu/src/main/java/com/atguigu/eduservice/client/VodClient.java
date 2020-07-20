package com.atguigu.eduservice.client;

import com.atguigu.commonutils.response.R;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author liukun
 * @description
 * @since 2020/6/26
 */
@FeignClient(value = "service-vod", fallback = VodFileDegradeFeignClient.class)
@Component
public interface  VodClient {
    @DeleteMapping("/vodservice/video/delete")
    public R removeFile(@RequestBody String filePath);
}
