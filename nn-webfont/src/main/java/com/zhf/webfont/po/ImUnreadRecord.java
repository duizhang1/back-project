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
 * @TableName wf_im_unread_record
 */
@TableName(value ="wf_im_unread_record")
@Data
public class ImUnreadRecord implements Serializable {
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
     * 私聊者ID
     */
    private String toUserId;

    /**
     * 未读消息数量
     */
    private Integer count;

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