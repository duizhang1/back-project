package com.zhf.webfont.service;

import java.util.List;

/**
 * @Author 10276
 * @Date 2023/1/8 22:33
 */
public interface ArticleCacheService {
    /**
     * 增加文章阅读量
     * @param articleId
     */
    void incrArticleRead(String articleId);

    /**
     * 向redis中添加一条点赞记录，以及更新点赞数
     * @param articleId
     */
    void likeArticlePip(String articleId);

    /**
     * 向redis中加1的收藏数
     * @param articleId
     */
    void incrArticleStore(String articleId);

    /**
     * 更新redis中的数据
     * @param articleId
     * @return
     */
    boolean dislikeArticle(String articleId);

    /**
     * 获得需要更新的文章的西悉尼
     * @return
     */
    List<String> getUpdateArticle();

    /**
     * 获得文章的阅读量
     * @param articleId
     */
    Integer getArticleRead(String articleId);
}
