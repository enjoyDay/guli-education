package com.atguigu.oss.controller;

import com.atguigu.commonutils.response.R;
import com.atguigu.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author liukun
 * @description 模仿使用阿里云OSS上传文件
 * @since 2020/6/17
 */
@Api(description = "阿里云文件管理")
@RestController
@CrossOrigin
@RequestMapping("/ossservice/file")
public class FileUploadController {
    @Autowired
    private FileService fileService;

    /**
     * 文件上传
     *
     * @param file 文件
     */
    @ApiOperation(value = "文件上传")
    @PostMapping("/upload")
    public R upload(
            @ApiParam(name = "file", value = "文件", required = true)
            @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return R.error().message("文件不能为空");
        }

        String uploadUrl = fileService.upload(file);
        //返回r对象
        return R.ok().message("文件上传成功").data("url", uploadUrl);
    }

    @ApiOperation(value = "图片文件删除")
    @DeleteMapping("delete")
    public R removeFile(@ApiParam(name = "filePath", value = "本地文件绝对路径", required = true)
                        @RequestBody String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return R.error().message("文件：" + filePath + "不存在");
        }
        file.delete();
        return R.ok();
    }
}
