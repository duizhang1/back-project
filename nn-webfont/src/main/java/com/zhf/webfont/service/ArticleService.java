package com.zhf.webfont.service;

import com.zhf.webfont.bo.ArticleListParam;
import com.zhf.webfont.bo.ArticleListShowParam;
import com.zhf.webfont.bo.StoreArticleParam;
import com.zhf.webfont.po.Article;

import java.util.List;

/**
 * @Author 10276
 * @Date 2023/1/7 14:22
 */
public interface ArticleService {
    /**
     * 创建文章
     * @param article
     */
    void insertArticle(Article article);

    /**
     * 通过文章ID删除文章
     * @param articleId
     */
    void deleteArticle(String articleId);

    /**
     * 更新文章
     * @param article
     */
    void updateArticle(Article article);

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
     * 收藏文章
     * @param storeArticleParam
     */
    void storeArticle(StoreArticleParam storeArticleParam);

    /**
     * 取消点赞文章
     * @param articleId
     */
    void dislikeArticle(String articleId);

    /**
     * 检查是否能对文章进行更新
     * @param id
     */
    Article isCanUpdateArticle(String id);
}
