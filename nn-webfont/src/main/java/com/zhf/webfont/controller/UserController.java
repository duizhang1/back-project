package com.zhf.webfont.controller;

import com.zhf.common.returnType.CommonResult;
import com.zhf.webfont.bo.UserLoginParam;
import com.zhf.webfont.bo.UserRegisterParam;
import com.zhf.webfont.service.MailService;
import com.zhf.webfont.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 10276
 * @Date 2023/1/3 18:45
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Resource
    private UserService userService;
    @Resource
    private MailService mailService;

    @ApiOperation("登陆接口返回token")
    @RequestMapping("login")
    public CommonResult login(@RequestBody UserLoginParam userLoginParam){
        String token = userService.login(userLoginParam);
        Map<String,String> tokenMap = new HashMap<>(2);
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",tokenHead);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation("注册账号")
    @RequestMapping("register")
    public CommonResult register(@RequestBody UserRegisterParam userRegisterParam){
        userService.register(userRegisterParam);
        return CommonResult.successWithMsg("注册成功");
    }

    @ApiOperation("获得邮箱验证码")
    @RequestMapping("getVerifyCode")
    public CommonResult getVerifyCode(String emailAddress){
        mailService.sendVerifyCode(emailAddress);
        return CommonResult.successWithMsg("成功发送验证码，请前往邮箱查收");
    }
}
