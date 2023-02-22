package com.zhf.webfont.controller;

import com.zhf.common.returnType.CommonResult;
import com.zhf.webfont.bo.ImRecordItemParam;
import com.zhf.webfont.config.NeedLogin;
import com.zhf.webfont.service.ImRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author 10276
 * @Date 2023/2/19 15:40
 */
@RestController
@RequestMapping("imRecord")
@Api("聊天室控制类")
public class ImRecordController {

    @Resource
    private ImRecordService imRecordService;

    @GetMapping("getChatList")
    @ApiOperation("获得聊天列表")
    @NeedLogin
    public CommonResult getChatList(){
        List<ImRecordItemParam> list = imRecordService.getChatList();
        return CommonResult.success(list);
    }

    @GetMapping("getOneChat")
    @ApiOperation("获得某个的chat列表的对象")
    @NeedLogin
    public CommonResult getOneChat(String toUserId){
        ImRecordItemParam imRecordItemParam = imRecordService.getOneChat(toUserId);
        return CommonResult.success(imRecordItemParam);
    }

    @GetMapping("getChatContent")
    @ApiOperation("获得与某人的聊天记录")
    @NeedLogin
    public CommonResult getChatContent(String toUserId){
        List<ImRecordItemParam> list = imRecordService.getChatContent(toUserId);
        return CommonResult.success(list);
    }
}
