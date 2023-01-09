package com.zhf.webfont.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 10276
 * @Date 2023/1/9 21:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreArticleParam {

    @ApiModelProperty(value = "收藏夹ID",required = true)
    private String storeListId;

    @ApiModelProperty(value = "文章ID",required = true)
    private String articleId;

}
