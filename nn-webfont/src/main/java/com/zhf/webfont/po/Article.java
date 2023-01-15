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
 * @Date 2023/1/7 1:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("wf_article")
public class Article {

    @TableId(type = IdType.ASSIGN_UUID)
    private String uuid;

    private String title;

    private String img;

    private String summary;

    private String content;

    private String creatorId;

    private Date createTime;

    private Date updateTime;

    private String sortId;

    private String labelId;

    private Integer readCount;

    private Integer likeCount;

    private Integer storeCount;

    private Integer titleState;

    private Integer score;
}
