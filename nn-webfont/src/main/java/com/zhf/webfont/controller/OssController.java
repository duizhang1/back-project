package com.zhf.webfont.controller;

import com.zhf.common.returnType.CommonResult;
import com.zhf.webfont.config.NeedLogin;
import com.zhf.webfont.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author 10276
 * @Date 2023/1/13 21:31
 */
@RestController
@RequestMapping("oss")
@Api("七牛云oss控制类")
public class OssController {

    @Resource
    private OssService ossService;

    @ApiOperation("获得七牛云token以及文件访问头")
    @GetMapping("getConfig")
    @NeedLogin
    public CommonResult getConfig(){
        Map<String,Object> map = ossService.getConfig();
        return CommonResult.success(map);
    }


}
