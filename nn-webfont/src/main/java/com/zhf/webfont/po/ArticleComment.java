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
 * @author 10276
 * @TableName wf_article_comment
 */
@TableName(value ="wf_article_comment")
@Data
public class ArticleComment implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String uuid;

    /**
     * 评论内容
     */
    private String comment;

    /**
     * 创建者id
     */
    private String userId;

    /**
     * 文章id
     */
    private String articleId;

    /**
     * 父评论Id
     */
    private String parentCommentId;

    /**
     * 子回复的回复的id
     */
    private String replyCommentId;

    /**
     * 点赞数
     */
    private Integer likeNumber;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 删除状态位0为真，1为已删除
     */
    private Integer isDel;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}