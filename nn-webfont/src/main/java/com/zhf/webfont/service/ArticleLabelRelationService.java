package com.zhf.webfont.service;

import com.zhf.webfont.po.ArticleLabelRelation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhf.webfont.po.Label;

import java.util.List;

/**
* @author 10276
* @description 针对表【wf_article_label_relation】的数据库操作Service
* @createDate 2023-01-28 14:07:02
*/
public interface ArticleLabelRelationService extends IService<ArticleLabelRelation> {

    /**
     * 获得文章的标签
     * @param articleId
     * @return
     */
    List<Label> getArticleLabel(String articleId);

}
