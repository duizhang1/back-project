package com.zhf.webfont.bo;

import com.zhf.webfont.po.Article;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * @Author 10276
 * @Date 2023/1/8 0:23
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticleListParam {

    /**
     * 数据大小
     */
    @ApiModelProperty(value = "分页大小",required = true)
    private Long size;

    /**
     * 当前页码
     */
    @ApiModelProperty(value = "当前页码",required = true)
    private Long current;

    /**
     * 分类的ID，如果为空就是全部类别
     */
    @ApiModelProperty(value = "分类ID",required = true)
    private String sortRoute;

    /**
     * 排序种类，1是最新，2是热榜
     */
    @ApiModelProperty(value = "分类ID",required = true)
    private Integer orderBy;

    /**
     * 热榜天数：3 7 30天，获取这几天的最热的文章
     */
    @ApiModelProperty(value = "热榜天数",required = false)
    private Integer hotDay;

}
