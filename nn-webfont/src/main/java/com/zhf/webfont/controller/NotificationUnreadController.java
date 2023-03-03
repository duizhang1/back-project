package com.zhf.webfont.controller;

import com.zhf.common.returnType.CommonResult;
import com.zhf.webfont.config.NeedLogin;
import com.zhf.webfont.po.NotificationUnread;
import com.zhf.webfont.service.NotificationUnreadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("notificationUnread")
@Api("未读消息数量控制类")
public class NotificationUnreadController {

    @Resource
    private NotificationUnreadService notificationUnreadService;

    @GetMapping("getNotificationUnreadCount")
    @ApiOperation("获得未读消息数量")
    public CommonResult getNotificationUnreadCount(){
        Map<String,Object> map = notificationUnreadService.getNotificationUnreadCount();
        return CommonResult.success(map);
    }

}
