package com.zhf.webfont.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhf.common.exception.Asserts;
import com.zhf.common.util.TransactionUtil;
import com.zhf.webfont.mapper.ArticleCommentMapper;
import com.zhf.webfont.po.ArticleComment;
import com.zhf.webfont.po.ArticleCommentLikeRelation;
import com.zhf.webfont.po.User;
import com.zhf.webfont.service.ArticleCommentLikeRelationService;
import com.zhf.webfont.mapper.ArticleCommentLikeRelationMapper;
import com.zhf.webfont.util.JwtTokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
* @author 10276
* @description 针对表【wf_article_comment_like_relation】的数据库操作Service实现
* @createDate 2023-02-01 22:48:22
*/
@Service
public class ArticleCommentLikeRelationServiceImpl extends ServiceImpl<ArticleCommentLikeRelationMapper, ArticleCommentLikeRelation>
    implements ArticleCommentLikeRelationService{

    @Resource
    private ArticleCommentLikeRelationMapper articleCommentLikeRelationMapper;
    @Resource
    private ArticleCommentMapper articleCommentMapper;
    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public void likeArticleComment(String commentId) {
        ArticleCommentLikeRelation commentLikeRelation = getArticleLikeComment(commentId);
        ArticleComment articleComment = articleCommentMapper.selectById(commentId);
        articleComment.setLikeNumber(articleComment.getLikeNumber()+1);
        articleComment.setUpdateTime(new Date());
        User user = jwtTokenUtil.getCurrentUserFromHeader();
        if (commentLikeRelation == null){
            ArticleCommentLikeRelation likeRelation = new ArticleCommentLikeRelation();
            likeRelation.setState(1);
            likeRelation.setCreateTime(new Date());
            likeRelation.setUpdateTime(new Date());
            likeRelation.setCommentId(commentId);
            likeRelation.setUserId(user.getUuid());
            TransactionUtil.transaction(()->{
                int insert = articleCommentLikeRelationMapper.insert(likeRelation);
                Asserts.failIsTrue(insert < 1,"点赞失败");
                int i = articleCommentMapper.updateById(articleComment);
                Asserts.failIsTrue(i < 1,"点赞失败");
            });
        }else{
            commentLikeRelation.setState(1);
            commentLikeRelation.setUpdateTime(new Date());
            TransactionUtil.transaction(()->{
                int insert =  articleCommentLikeRelationMapper.updateById(commentLikeRelation);
                Asserts.failIsTrue(insert < 1,"点赞失败");
                int i = articleCommentMapper.updateById(articleComment);
                Asserts.failIsTrue(i < 1,"点赞失败");
            });

        }
    }

    @Override
    public void dislikeArticleComment(String commentId) {
        ArticleCommentLikeRelation commentLikeRelation = getArticleLikeComment(commentId);
        ArticleComment articleComment = articleCommentMapper.selectById(commentId);
        articleComment.setLikeNumber(articleComment.getLikeNumber()-1);
        articleComment.setUpdateTime(new Date());
        if (commentLikeRelation != null){
            commentLikeRelation.setState(0);
            commentLikeRelation.setUpdateTime(new Date());
            TransactionUtil.transaction(()->{
                int insert =  articleCommentLikeRelationMapper.updateById(commentLikeRelation);
                Asserts.failIsTrue(insert < 1,"取消点赞失败");
                int i = articleCommentMapper.updateById(articleComment);
                Asserts.failIsTrue(i < 1,"取消点赞失败");
            });
        }
    }

    @Override
    public ArticleCommentLikeRelation getLikeArticleComment(String commentId) {
        User user = jwtTokenUtil.getCurrentUserFromHeader();
        if (user == null){
            return null;
        }else{
            QueryWrapper<ArticleCommentLikeRelation> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id",user.getUuid())
                    .eq("comment_id",commentId);
            return articleCommentLikeRelationMapper.selectOne(wrapper);
        }
    }

    private ArticleCommentLikeRelation getArticleLikeComment(String commentId) {
        User user = jwtTokenUtil.getCurrentUserFromHeader();
        QueryWrapper<ArticleCommentLikeRelation> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",user.getUuid())
                .eq("comment_id",commentId);
        return articleCommentLikeRelationMapper.selectOne(wrapper);
    }
}




