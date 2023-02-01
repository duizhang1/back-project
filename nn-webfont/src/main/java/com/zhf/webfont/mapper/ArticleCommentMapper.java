package com.zhf.webfont.mapper;

import com.zhf.webfont.bo.ArticleCommentSingleParam;
import com.zhf.webfont.po.ArticleComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 10276
* @description 针对表【wf_article_comment】的数据库操作Mapper
* @createDate 2023-02-01 00:13:52
* @Entity com.zhf.webfont.po.ArticleComment
*/
public interface ArticleCommentMapper extends BaseMapper<ArticleComment> {

    List<ArticleCommentSingleParam> getChildCommentList(@Param("parentCommentId") String uuid,@Param("startIndex") Integer startIndex,@Param("childSize") Integer childSize);
}




