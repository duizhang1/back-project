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
 * @TableName wf_article_label_relation
 */
@TableName(value ="wf_article_label_relation")
@Data
public class ArticleLabelRelation implements Serializable {
    /**
     * 
     */
    @TableId
    private String uuid;

    /**
     * 文章id
     */
    private String articleId;

    /**
     * 类别id
     */
    private String labelId;

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