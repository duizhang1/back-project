package com.zhf.webfont.controller;

import com.zhf.common.returnType.CommonResult;
import com.zhf.webfont.bo.StoreListWithIsStoreParam;
import com.zhf.webfont.config.NeedLogin;
import com.zhf.webfont.po.StoreList;
import com.zhf.webfont.service.StoreListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author 10276
 * @Date 2023/1/31 0:02
 */
@RequestMapping("storeList")
@RestController
@Api("收藏夹控制类")
public class StoreListController {

    @Resource
    private StoreListService storeListService;

    @ApiOperation("获得当前收藏夹并且附上改文章是否已被收藏")
    @GetMapping("getStoreListWithIsStore")
    @NeedLogin
    public CommonResult getStoreListWithIsStore(String articleId){
        List<StoreListWithIsStoreParam> list = storeListService.getStoreListWithIsStore(articleId);
        return CommonResult.success(list);
    }

    @ApiOperation("创建收藏夹")
    @PostMapping("createStoreList")
    @NeedLogin
    public CommonResult createStoreList(@RequestBody StoreList storeList){
        storeListService.createStoreList(storeList);
        return CommonResult.successWithMsg("添加成功");
    }

    @ApiOperation("收藏文章更新收藏记录")
    @PostMapping("updateArticleStore")
    @NeedLogin
    public CommonResult updateArticleStore(@RequestBody Map<String,Object> param){
        storeListService.updateArticleStore(param);
        return CommonResult.successWithMsg("收藏成功");
    }

}
