package com.zhf.webfont.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName wf_article_comment_like_relation
 */
@TableName(value ="wf_article_comment_like_relation")
@Data
public class ArticleCommentLikeRelation implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String uuid;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 评论ID
     */
    private String commentId;

    /**
     * 点赞状态:(1:点击，2:取消)
     */
    private Integer state;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}