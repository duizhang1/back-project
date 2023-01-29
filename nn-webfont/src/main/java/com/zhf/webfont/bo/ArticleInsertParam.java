package com.zhf.webfont.bo;

import com.zhf.webfont.po.Article;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author 10276
 * @Date 2023/1/27 18:20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ArticleInsertParam extends Article {

    private List<String> labelIds;

}
