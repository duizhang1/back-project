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
 * @TableName wf_store_list_user_realation
 */
@TableName(value ="wf_store_list_user_realation")
@Data
public class StoreListUserRealation implements Serializable {
    /**
     * uuid主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String uuid;

    /**
     * 收藏夹ID
     */
    private String sortListId;

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