package com.zhf.webfont.controller;

import com.zhf.common.returnType.CommonResult;
import com.zhf.webfont.po.Label;
import com.zhf.webfont.service.LabelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author 10276
 * @Date 2023/1/14 23:01
 */
@RestController
@RequestMapping("label")
@Api("标签控制类")
public class LabelController {

    @Resource
    private LabelService labelService;

    @GetMapping("getLabelList")
    @ApiOperation("获得标签列表")
    public CommonResult getLabelList(){
        List<Label> list = labelService.list();
        return CommonResult.success(list);
    }

}
