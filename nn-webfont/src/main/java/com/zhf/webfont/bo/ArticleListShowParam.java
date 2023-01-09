package com.zhf.webfont.bo;

import com.zhf.webfont.po.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author 10276
 * @Date 2023/1/8 17:13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleListShowParam extends Article {

    private String creatorName;

    private String sortName;

    private String labelName;

}
