package com.zhf.common.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 基础分页参数
 * @Author 10276
 * @Date 2023/2/23 14:25
 */
@Data
public class BasePageParam {

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
     * 数据数量
     */
    @ApiModelProperty(value = "全部数量")
    private Long total;

}
