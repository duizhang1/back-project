package com.zhf.webfont.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhf.webfont.bo.ArticleListParam;
import com.zhf.webfont.bo.ArticleListShowParam;
import com.zhf.webfont.po.Article;

import java.util.List;

/**
 * @Author 10276
 * @Date 2023/1/7 14:23
 */
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 获得文章展示列表，并且分页
     * @param page
     * @param articleListParam
     * @return
     */
    IPage<ArticleListShowParam> getArticleListShowParamList(IPage<ArticleListShowParam> page, ArticleListParam articleListParam);

}
