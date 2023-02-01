package com.zhf.webfont.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhf.webfont.po.ArticleClickRelation;
import com.zhf.webfont.po.User;
import com.zhf.webfont.service.ArticleClickRelationService;
import com.zhf.webfont.mapper.ArticleClickRelationMapper;
import com.zhf.webfont.util.JwtTokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 10276
* @description 针对表【wf_article_click_relation】的数据库操作Service实现
* @createDate 2023-01-29 13:48:03
*/
@Service
public class ArticleClickRelationServiceImpl extends ServiceImpl<ArticleClickRelationMapper, ArticleClickRelation>
    implements ArticleClickRelationService{

    @Resource
    private ArticleClickRelationMapper articleClickRelationMapper;
    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public ArticleClickRelation getCurUserClick(String articleId) {
        User currentUser = jwtTokenUtil.getCurrentUserFromHeader();

        QueryWrapper<ArticleClickRelation> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id",articleId)
                .eq("user_id",currentUser.getUuid());
        return articleClickRelationMapper.selectOne(wrapper);
    }

    @Override
    public ArticleClickRelation getArticleLike(String articleId) {
        if (jwtTokenUtil.getCurrentUserFromHeader() == null){
            return null;
        }else{
            return getCurUserClick(articleId);
        }
    }
}




