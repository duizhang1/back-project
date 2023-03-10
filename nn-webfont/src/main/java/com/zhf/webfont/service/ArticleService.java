package com.zhf.webfont.service;

import com.zhf.webfont.bo.ArticleInsertParam;
import com.zhf.webfont.bo.ArticleListParam;
import com.zhf.webfont.bo.ArticleListShowParam;
import com.zhf.webfont.bo.StoreArticleParam;
import com.zhf.webfont.po.Article;

import java.util.List;
import java.util.Map;

/**
 * @Author 10276
 * @Date 2023/1/7 14:22
 */
public interface ArticleService {
    /**
     * 创建文章
     * @param articleInsertParam
     */
    void insertArticle(ArticleInsertParam articleInsertParam);

    /**
     * 通过文章ID删除文章
     * @param articleId
     */
    void deleteArticle(String articleId);

    /**
     * 更新文章
     * @param articleInsertParam
     */
    void updateArticle(ArticleInsertParam articleInsertParam);

    /**
     * 获得文章列表，根据条件获得
     * @param articleListParam
     * @return
     */
    List<ArticleListShowParam> getArticleList(ArticleListParam articleListParam);

    /**
     * 获得文章详情
     * @param articleId
     * @return
     */
    Article getArticleInfo(String articleId);

    /**
     * 点赞文章
     * @param articleId
     */
    void likeArticle(String articleId);

    /**
     * 取消点赞文章
     * @param articleId
     */
    void dislikeArticle(String articleId);

    /**
     * 检查是否能对文章进行更新
     * @param id
     */
    ArticleInsertParam isCanUpdateArticle(String id);

    /**
     * 获得文章和创建者的信息
     * @param articleId
     * @return
     */
    Map<String, Object> getArticleAndUserInfo(String articleId);
}
