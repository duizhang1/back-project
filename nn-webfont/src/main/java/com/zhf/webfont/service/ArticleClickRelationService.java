package com.zhf.webfont.service;

import com.zhf.common.bo.BasePageParam;
import com.zhf.webfont.bo.ArticleLikeNotificationParam;
import com.zhf.webfont.po.ArticleClickRelation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author 10276
* @description 针对表【wf_article_click_relation】的数据库操作Service
* @createDate 2023-01-29 13:48:03
*/
public interface ArticleClickRelationService extends IService<ArticleClickRelation> {

    /**
     * 获得当前用户的点击
     * @param articleId
     * @return
     */
    ArticleClickRelation getCurUserClick(String articleId);

    /**
     * 获得文章点赞记录
     * @param articleId
     * @return
     */
    ArticleClickRelation getArticleLike(String articleId);

    /**
     * 获得点赞消息列表
     * @param pageParam
     * @return
     */
    Map<String,Object> getArticleLikeNotifications(BasePageParam pageParam);
}
