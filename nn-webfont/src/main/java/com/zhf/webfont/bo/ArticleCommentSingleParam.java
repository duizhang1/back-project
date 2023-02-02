package com.zhf.webfont.bo;

import com.zhf.webfont.po.ArticleComment;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author 10276
 * @Date 2023/2/1 4:24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleCommentSingleParam extends ArticleComment {

    private String replyUserName;

    private String userName;

    private String avatarHref;

    private Integer likeState;

}
