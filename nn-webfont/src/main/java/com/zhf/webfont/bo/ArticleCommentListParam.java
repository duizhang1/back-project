package com.zhf.webfont.bo;

import com.zhf.webfont.po.ArticleComment;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author 10276
 * @Date 2023/2/1 3:58
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ArticleCommentListParam extends ArticleComment {

    private List<ArticleCommentSingleParam> childCommentList;

    private Boolean hasMore;

    private String userName;

    private String avatarHref;

    private Integer likeState;
}
