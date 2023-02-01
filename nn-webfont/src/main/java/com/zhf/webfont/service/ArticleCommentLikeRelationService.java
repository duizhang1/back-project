package com.zhf.webfont.service;

import com.zhf.webfont.po.ArticleCommentLikeRelation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 10276
* @description 针对表【wf_article_comment_like_relation】的数据库操作Service
* @createDate 2023-02-01 22:48:22
*/
public interface ArticleCommentLikeRelationService extends IService<ArticleCommentLikeRelation> {

    /**
     * 点赞文章评论
     * @param commentId
     */
    void likeArticleComment(String commentId);

    /**
     * 取消点赞
     * @param commentId
     */
    void dislikeArticleComment(String commentId);

    /**
     * 获得点赞记录
     * @param commentId
     * @return
     */
    ArticleCommentLikeRelation getLikeArticleComment(String commentId);
}
