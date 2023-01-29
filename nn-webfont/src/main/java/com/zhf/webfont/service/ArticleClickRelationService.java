package com.zhf.webfont.service;

import com.zhf.webfont.po.ArticleClickRelation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 10276
* @description 针对表【wf_article_click_relation】的数据库操作Service
* @createDate 2023-01-29 13:48:03
*/
public interface ArticleClickRelationService extends IService<ArticleClickRelation> {

    /**
     * 获得当前用户的点击
     * @param articleId
     * @return
     */
    ArticleClickRelation getCurUserClick(String articleId);

}
