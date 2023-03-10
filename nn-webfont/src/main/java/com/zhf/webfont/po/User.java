package com.zhf.webfont.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author 10276
 * @Date 2023/1/3 18:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("wf_user")
public class User {

    @TableId(type = IdType.ASSIGN_UUID)
    private String uuid;

    private String username;

    private String password;

    private String emailAddress;

    private Integer sex;

    private String avatar;

    private String companyName;

    private String position;

    private String personProfile;

    private Date createTime;

    private Date updateTime;

}
