package com.zhf.webfont.service;

import com.zhf.webfont.bo.ArticleCommentListParam;
import com.zhf.webfont.bo.ArticleCommentSingleParam;
import com.zhf.webfont.po.ArticleComment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 10276
* @description 针对表【wf_article_comment】的数据库操作Service
* @createDate 2023-02-01 00:13:52
*/
public interface ArticleCommentService extends IService<ArticleComment> {

    /**
     * 创建文章评论
     * @param articleComment
     */
    void createArticleComment(ArticleComment articleComment);

    /**
     * 获得文章的评论
     * @param articleId
     * @param childSize
     * @return
     */
    List<ArticleCommentListParam> getArticleComment(String articleId, Integer childSize);

    /**
     * 加载更多评论
     * @param articleCommentId
     * @param childSize
     * @return
     */
    List<ArticleCommentSingleParam> loadMoreReply(String articleCommentId, Integer childSize);

    /**
     * 删除文章评论
     * @param commentId
     */
    void deleteArticleComment(String commentId);
}
