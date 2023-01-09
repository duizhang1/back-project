package com.zhf.webfont.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @Author 10276
 * @Date 2023/1/9 22:40
 */
@TableName(value ="wf_storelist")
@Data
public class StoreList implements Serializable {
    /**
     * uuid主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String uuid;

    /**
     * 收藏夹名称
     */
    private String name;

    /**
     * 收藏夹文章描述
     */
    private String summary;

    /**
     * 收藏夹状态(1:公开,2:隐私)
     */
    private String state;

    /**
     * 用户ID
     */
    private String userId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}