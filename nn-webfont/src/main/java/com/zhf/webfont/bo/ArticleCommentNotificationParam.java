package com.zhf.webfont.bo;

import com.zhf.webfont.po.ArticleComment;
import lombok.Data;

/**
 * @Author 10276
 * @Date 2023/2/25 2:31
 */
@Data
public class ArticleCommentNotificationParam {

    private ArticleComment articleComment;

    private String userId;

    private String username;

    private String avatar;

    private String articleId;

    private String title;

}
