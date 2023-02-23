package com.zhf.webfont.controller;

import com.zhf.common.bo.BasePageParam;
import com.zhf.common.returnType.CommonResult;
import com.zhf.webfont.bo.ArticleLikeNotificationParam;
import com.zhf.webfont.config.NeedLogin;
import com.zhf.webfont.service.ArticleClickRelationService;
import com.zhf.webfont.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author 10276
 * @Date 2023/2/23 14:13
 */
@RestController
@Api("文章点赞控制类")
@RequestMapping("articleLike")
public class ArticleLikeController {

    @Resource
    private ArticleClickRelationService articleClickRelationService;

    @GetMapping("getArticleLikeNotifications")
    @ApiOperation("获得消息通知中的点赞消息")
    @NeedLogin
    public CommonResult getArticleLikeNotifications(BasePageParam pageParam){
        Map<String,Object> map = articleClickRelationService.getArticleLikeNotifications(pageParam);
        return CommonResult.success(map);
    }

}
