package com.zhf.webfont.bo;

import com.zhf.common.bo.BasePageParam;
import com.zhf.webfont.po.Article;
import com.zhf.webfont.po.ArticleClickRelation;
import com.zhf.webfont.po.User;
import lombok.Data;

/**
 * @Author 10276
 * @Date 2023/2/23 14:35
 */
@Data
public class ArticleLikeNotificationParam{

    private ArticleClickRelation relation;

    private User user;

    private Article article;
}
