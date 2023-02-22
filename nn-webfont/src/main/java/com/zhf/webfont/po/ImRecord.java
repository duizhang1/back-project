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
 * @TableName wf_im_record
 */
@TableName(value ="wf_im_record")
@Data
public class ImRecord implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String uuid;

    /**
     * 发送者ID
     */
    private String userId;

    /**
     * 被发送者ID
     */
    private String toUserId;

    /**
     * 发送内容
     */
    private String content;

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