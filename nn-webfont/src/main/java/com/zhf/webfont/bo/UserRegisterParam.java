package com.zhf.webfont.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author 10276
 * @Date 2023/1/4 17:37
 */
@AllArgsConstructor
@Data
public class UserRegisterParam {

    @ApiModelProperty(value = "用户名",required = true)
    private String username;

    @ApiModelProperty(value = "密码",required = true)
    private String password;

    @ApiModelProperty(value = "重复密码",required = true)
    private String repeatPassword;

    @ApiModelProperty(value = "邮箱",required = true)
    private String emailAddress;

    @ApiModelProperty(value = "验证码",required = true)
    private String verifyCode;

}
