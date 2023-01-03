package com.zhf.webfont.controller;

import com.zhf.common.returnType.CommonResult;
import com.zhf.webfont.bo.UserLoginParam;
import com.zhf.webfont.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
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

    @ApiOperation("登陆接口返回token")
    @PostMapping("login")
    public CommonResult login(@RequestBody UserLoginParam userLoginParam){
        String token = userService.login(userLoginParam);
        Map<String,String> tokenMap = new HashMap<>(2);
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",tokenHead);
        return CommonResult.success(tokenMap);
    }

}
