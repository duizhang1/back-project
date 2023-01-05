package com.zhf.webfont.controller;

import com.zhf.common.returnType.CommonResult;
import com.zhf.webfont.service.MailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author 10276
 * @Date 2023/1/5 18:04
 */
@RestController
@Api(value = "邮件发送控制类")
@RequestMapping("mail")
public class MailController {

    @Resource
    private MailService mailService;

    @ApiOperation("获得邮箱验证码")
    @GetMapping("getVerifyCode")
    public CommonResult getVerifyCode(String emailAddress){
        mailService.sendVerifyCode(emailAddress);
        return CommonResult.successWithMsg("成功发送验证码，请前往邮箱查收");
    }
}
