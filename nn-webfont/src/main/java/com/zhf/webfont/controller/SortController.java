package com.zhf.webfont.controller;

import com.zhf.common.returnType.CommonResult;
import com.zhf.webfont.po.Sort;
import com.zhf.webfont.service.SortService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author 10276
 * @Date 2023/1/14 19:57
 */
@RestController
@RequestMapping("sort")
@Api("分类控制类")
public class SortController {

    @Resource
    private SortService sortService;

    @GetMapping("getSortList")
    @ApiOperation("获得sort数据")
    public CommonResult getSortList(){
        List<Sort> list = sortService.list();
        return CommonResult.success(list);
    }

}
