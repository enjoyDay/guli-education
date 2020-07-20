package com.atguigu.cmsservice.controller;


import com.atguigu.cmsservice.service.CrmBannerService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 首页banner表 增删改查
 * </p>
 *
 * @author liukun
 * @since 2020-06-27
 */
@RestController
@RequestMapping("/cmsservice/crm-banner")
@CrossOrigin
@Api(description = "首页banner管理")
public class CrmBannerController {
    @Autowired
    private CrmBannerService bannerService;


}

