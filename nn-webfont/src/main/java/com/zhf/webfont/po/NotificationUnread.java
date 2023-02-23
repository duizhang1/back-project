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
 * @TableName wf_notification_unread
 */
@TableName(value ="wf_notification_unread")
@Data
public class NotificationUnread implements Serializable {
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
     * 点赞未读消息数
     */
    private Integer likeCount;

    /**
     * 评论未读消息数
     */
    private Integer commentCount;

    /**
     * 关注未读消息数
     */
    private Integer focusCount;

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