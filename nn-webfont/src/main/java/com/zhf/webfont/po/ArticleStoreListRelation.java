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
 * @TableName wf_article_store_list_relation
 */
@TableName(value ="wf_article_store_list_relation")
@Data
public class ArticleStoreListRelation implements Serializable {
    /**
     * uuid主键
     */
    @TableId
    private String uuid;

    /**
     * 收藏夹ID
     */
    private String storeListId;

    /**
     * 文章ID
     */
    private String articleId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}