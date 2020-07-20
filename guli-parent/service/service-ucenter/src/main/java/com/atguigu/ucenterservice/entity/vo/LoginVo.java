package com.atguigu.ucenterservice.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liukun
 * @description 用于接收登录数据
 * @since 2020/6/29
 */

@Data
@ApiModel(value="LoginVo", description="登录对象")
public class LoginVo {
    @ApiModelProperty(value = "邮箱账号")
    private String email;

    @ApiModelProperty(value = "密码")
    private String password;
}
