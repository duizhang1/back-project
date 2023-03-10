package com.zhf.webfont.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhf.webfont.po.ArticleLabelRelation;
import com.zhf.webfont.po.Label;
import com.zhf.webfont.service.ArticleLabelRelationService;
import com.zhf.webfont.mapper.ArticleLabelRelationMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 10276
* @description 针对表【wf_article_label_relation】的数据库操作Service实现
* @createDate 2023-01-28 14:07:02
*/
@Service
public class ArticleLabelRelationServiceImpl extends ServiceImpl<ArticleLabelRelationMapper, ArticleLabelRelation>
    implements ArticleLabelRelationService{

    @Resource
    private ArticleLabelRelationMapper articleLabelRelationMapper;

    @Override
    public List<Label> getArticleLabel(String articleId) {
         return articleLabelRelationMapper.getArticleLabel(articleId);
    }
}




