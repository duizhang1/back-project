package com.zhf.webfont.controller;

import com.zhf.common.returnType.CommonResult;
import com.zhf.webfont.bo.ArticleListParam;
import com.zhf.webfont.bo.ArticleListShowParam;
import com.zhf.webfont.bo.StoreArticleParam;
import com.zhf.webfont.config.NeedLogin;
import com.zhf.webfont.po.Article;
import com.zhf.webfont.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author 10276
 * @Date 2023/1/7 13:34
 */
@RestController
@RequestMapping("article")
@Api("文章控制类")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @ApiOperation("插入文章，需要登陆")
    @PostMapping("insertArticle")
    @NeedLogin
    public CommonResult insertArticle(@RequestBody Article article){
        articleService.insertArticle(article);
        return CommonResult.successWithMsg("文章创建成功");
    }

    @ApiOperation("删除文章，需要登陆")
    @PostMapping("deleteArticle")
    @NeedLogin
    public CommonResult deleteArticle(String articleId){
        articleService.deleteArticle(articleId);
        return CommonResult.successWithMsg("文章删除成功");
    }

    @ApiOperation("更新文章")
    @PostMapping("updateArticle")
    @NeedLogin
    public CommonResult updateArticle(@RequestBody Article article){
        articleService.updateArticle(article);
        return CommonResult.successWithMsg("文章更新成功");
    }

    @ApiOperation("获取文章列表")
    @GetMapping("getArticleList")
    public CommonResult getArticleList(ArticleListParam articleListParam){
        List<ArticleListShowParam> articles = articleService.getArticleList(articleListParam);
        return CommonResult.success(articles);
    }

    @ApiOperation("获取文章详情")
    @GetMapping("getArticleInfo")
    public CommonResult getArticleInfo(String articleId){
        Article article = articleService.getArticleInfo(articleId);
        return CommonResult.success(article);
    }

    @ApiOperation("获得文章以及用户信息")
    @GetMapping("getArticleAndUserInfo")
    public CommonResult getArticleAndUserInfo(String articleId){
        Map<String,Object> map = articleService.getArticleAndUserInfo(articleId);
        return CommonResult.success(map);
    }

    @ApiOperation("点赞文章")
    @PostMapping("likeArticle")
    @NeedLogin
    public CommonResult likeArticle(String articleId){
        articleService.likeArticle(articleId);
        return CommonResult.successWithMsg("点赞成功");
    }

    @ApiOperation("取消点赞")
    @PostMapping("dislikeArticle")
    @NeedLogin
    public CommonResult dislikeArticle(String articleId){
        articleService.dislikeArticle(articleId);
        return CommonResult.successWithMsg("取消成功");
    }

    @ApiOperation("收藏文章")
    @PostMapping("storeArticle")
    @NeedLogin
    public CommonResult storeArticle(@RequestBody StoreArticleParam storeArticleParam){
        articleService.storeArticle(storeArticleParam);
        return CommonResult.successWithMsg("收藏成功");
    }

    @ApiOperation("是否能更新该文章")
    @GetMapping("isCanUpdateArticle")
    @NeedLogin
    public CommonResult isCanUpdateArticle(String id){
        Article articleInfo = articleService.isCanUpdateArticle(id);
        return CommonResult.success(articleInfo);
    }

}
