package com.zhf.webfont.controller;

import com.zhf.common.bo.BasePageParam;
import com.zhf.common.returnType.CommonResult;
import com.zhf.webfont.bo.ArticleCommentListParam;
import com.zhf.webfont.bo.ArticleCommentSingleParam;
import com.zhf.webfont.config.NeedLogin;
import com.zhf.webfont.po.ArticleComment;
import com.zhf.webfont.po.ArticleCommentLikeRelation;
import com.zhf.webfont.service.ArticleCommentLikeRelationService;
import com.zhf.webfont.service.ArticleCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author 10276
 * @Date 2023/2/1 0:15
 */
@RestController
@RequestMapping("articleComment")
@Api("文章评论控制类")
public class ArticleCommentController {

    @Resource
    private ArticleCommentService articleCommentService;
    @Resource
    private ArticleCommentLikeRelationService articleCommentLikeRelationService;

    @GetMapping("getArticleComment")
    @ApiOperation("获得文章评论")
    public CommonResult getArticleComment(String articleId,Integer childSize){
        List<ArticleCommentListParam> list = articleCommentService.getArticleComment(articleId,childSize);
        return CommonResult.success(list);
    }

    @PostMapping("createArticleComment")
    @ApiOperation("创建文章评论")
    @NeedLogin
    public CommonResult createArticleComment(@RequestBody ArticleComment articleComment){
        articleCommentService.createArticleComment(articleComment);
        return CommonResult.successWithMsg("评论成功");
    }

    @GetMapping("loadMoreReply")
    @ApiOperation("加载更多回复")
    public CommonResult loadMoreReply(String articleCommentId,Integer childSize){
        List<ArticleCommentSingleParam> list = articleCommentService.loadMoreReply(articleCommentId,childSize);
        return CommonResult.success(list);
    }

    @DeleteMapping("deleteArticleComment")
    @ApiOperation("删除文章评论")
    @NeedLogin
    public CommonResult deleteArticleComment(String commentId){
        articleCommentService.deleteArticleComment(commentId);
        return CommonResult.successWithMsg("删除成功");
    }

    @GetMapping("likeArticleComment")
    @ApiOperation("点赞文章评论")
    @NeedLogin
    public CommonResult likeArticleComment(String commentId){
        articleCommentLikeRelationService.likeArticleComment(commentId);
        return CommonResult.successWithMsg("点赞成功");
    }

    @GetMapping("dislikeArticleComment")
    @ApiOperation("取消点赞文章评论")
    @NeedLogin
    public CommonResult dislikeArticleComment(String commentId){
        articleCommentLikeRelationService.dislikeArticleComment(commentId);
        return CommonResult.successWithMsg("取消点赞成功");
    }

    @GetMapping("getLikeArticleComment")
    @ApiOperation("获得点赞文章评论记录")
    public CommonResult getLikeArticleComment(String commentId){
        ArticleCommentLikeRelation articleCommentLikeRelation = articleCommentLikeRelationService.getLikeArticleComment(commentId);
        return CommonResult.success(articleCommentLikeRelation);
    }

    @GetMapping("getCommentNotification")
    @ApiOperation("获得别人评论消息")
    @NeedLogin
    public CommonResult getCommentNotification(BasePageParam pageParam){
        Map<String,Object> map = articleCommentService.getCommentNotification(pageParam);
        return CommonResult.success(map);
    }
}
