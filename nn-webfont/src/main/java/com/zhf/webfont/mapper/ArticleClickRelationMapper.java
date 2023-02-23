package com.zhf.webfont.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhf.webfont.po.ArticleClickRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author 10276
* @description 针对表【wf_article_click_relation】的数据库操作Mapper
* @createDate 2023-01-29 13:48:03
* @Entity com.zhf.webfont.po.ArticleClickRelation
*/
public interface ArticleClickRelationMapper extends BaseMapper<ArticleClickRelation> {

    IPage<ArticleClickRelation> selectLikeNotification(IPage<ArticleClickRelation> iPage,@Param("userId") String userId);
}




