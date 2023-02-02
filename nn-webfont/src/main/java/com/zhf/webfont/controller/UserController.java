package com.zhf.webfont.controller;

import com.zhf.common.returnType.CommonResult;
import com.zhf.webfont.bo.UserLoginParam;
import com.zhf.webfont.bo.UserRegisterParam;
import com.zhf.webfont.config.NeedLogin;
import com.zhf.webfont.po.User;
import com.zhf.webfont.po.UserSubscribe;
import com.zhf.webfont.service.MailService;
import com.zhf.webfont.service.UserService;
import com.zhf.webfont.service.UserSubscribeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 10276
 * @Date 2023/1/3 18:45
 */
@RestController
@RequestMapping("user")
@Api(value = "用户操作")
public class UserController {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Resource
    private UserService userService;
    @Resource
    private UserSubscribeService userSubscribeService;

    @ApiOperation("登陆接口返回token")
    @PostMapping("login")
    public CommonResult login(@RequestBody UserLoginParam userLoginParam){
        String token = userService.login(userLoginParam);
        Map<String,String> tokenMap = new HashMap<>(2);
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",tokenHead);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation("注册账号")
    @PostMapping("register")
    public CommonResult register(@RequestBody UserRegisterParam userRegisterParam){
        userService.register(userRegisterParam);
        return CommonResult.successWithMsg("注册成功");
    }

    @ApiOperation("获得登陆用户信息")
    @GetMapping("getCurrentUser")
    public CommonResult getCurrentUser(){
        User user = userService.getCurrentUser();
        return CommonResult.success(user);
    }

    @ApiOperation("关注用户")
    @GetMapping("subscribeUser")
    @NeedLogin
    public CommonResult subscribeUser(String userId){
        userSubscribeService.subscribeUser(userId);
        return CommonResult.successWithMsg("关注成功");
    }

    @ApiOperation("取关用户")
    @GetMapping("unSubscribeUser")
    @NeedLogin
    public CommonResult unSubscribeUser(String userId){
        userSubscribeService.unSubscribeUser(userId);
        return CommonResult.successWithMsg("取关成功");
    }

    @ApiOperation("获得用户关注信息")
    @GetMapping("getNowUserSubscribe")
    public CommonResult getNowUserSubscribe(String userId){
        UserSubscribe userSubscribe = userSubscribeService.getNowUserSubscribe(userId);
        return CommonResult.success(userSubscribe);
    }

}
