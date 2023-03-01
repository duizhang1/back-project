package com.zhf.webfont.controller;

import com.zhf.common.bo.BasePageParam;
import com.zhf.common.returnType.CommonResult;
import com.zhf.webfont.config.NeedLogin;
import com.zhf.webfont.service.UserSubscribeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author 10276
 * @Date 2023/2/26 0:34
 */
@RestController
@RequestMapping("userSubscribe")
@Api("用户关注控制类")
public class UserSubscribeController {

    @Resource
    private UserSubscribeService userSubscribeService;

    @GetMapping("getUserSubscribeNotification")
    @ApiOperation("获得关注的消息通知")
    @NeedLogin
    public CommonResult getUserSubscribeNotification(BasePageParam pageParam){
        Map<String,Object> map = userSubscribeService.getUserSubscribeNotification(pageParam);
        return CommonResult.success(map);
    }

}
