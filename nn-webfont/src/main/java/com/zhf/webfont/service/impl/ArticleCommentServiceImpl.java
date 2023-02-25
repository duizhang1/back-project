package com.zhf.webfont.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhf.common.bo.BasePageParam;
import com.zhf.common.enumType.LikeStateEnum;
import com.zhf.common.exception.Asserts;
import com.zhf.common.util.TransactionUtil;
import com.zhf.webfont.bo.ArticleCommentListParam;
import com.zhf.webfont.bo.ArticleCommentNotificationParam;
import com.zhf.webfont.bo.ArticleCommentSingleParam;
import com.zhf.webfont.mapper.ArticleMapper;
import com.zhf.webfont.mapper.UserMapper;
import com.zhf.webfont.po.Article;
import com.zhf.webfont.po.ArticleComment;
import com.zhf.webfont.po.ArticleCommentLikeRelation;
import com.zhf.webfont.po.User;
import com.zhf.webfont.service.ArticleCommentLikeRelationService;
import com.zhf.webfont.service.ArticleCommentService;
import com.zhf.webfont.mapper.ArticleCommentMapper;
import com.zhf.webfont.util.JwtTokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 10276
 * @description 针对表【wf_article_comment】的数据库操作Service实现
 * @createDate 2023-02-01 00:13:52
 */
@Service
public class ArticleCommentServiceImpl extends ServiceImpl<ArticleCommentMapper, ArticleComment>
        implements ArticleCommentService {

    @Resource
    private ArticleCommentMapper articleCommentMapper;
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private ArticleCommentLikeRelationService articleCommentLikeRelationService;

    @Override
    public void createArticleComment(ArticleComment articleComment) {
        checkArticleCommentParam(articleComment);

        articleComment.setCreateTime(new Date());
        articleComment.setUpdateTime(new Date());
        articleComment.setUserId(jwtTokenUtil.getCurrentUserFromHeader().getUuid());
        articleComment.setLikeNumber(0);
        articleComment.setIsDel(0);

        int insert = articleCommentMapper.insert(articleComment);
        Asserts.failIsTrue(insert < 1, "评论失败");
    }

    @Override
    public List<ArticleCommentListParam> getArticleComment(String articleId, Integer childSize) {
        List<ArticleCommentListParam> data = new ArrayList<>();
        List<ArticleComment> articleFatherComment = getArticleFatherComment(articleId);

        for (ArticleComment articleComment : articleFatherComment) {
            ArticleCommentListParam articleCommentListParam = new ArticleCommentListParam();
            BeanUtil.copyProperties(articleComment, articleCommentListParam);

            // 获取子评论
            List<ArticleCommentSingleParam> articleCommentSingleParams = articleCommentMapper.getChildCommentList(articleComment.getUuid(), 0, childSize);
            for (ArticleCommentSingleParam articleCommentSingleParam : articleCommentSingleParams) {
                ArticleCommentLikeRelation articleCommentLikeRelation = articleCommentLikeRelationService.getLikeArticleComment(articleCommentSingleParam.getUuid());
                articleCommentSingleParam.setLikeState(articleCommentLikeRelation != null ? articleCommentLikeRelation.getState() : LikeStateEnum.DISLIKE.getValue());
            }
            articleCommentListParam.setChildCommentList(articleCommentSingleParams);

            // 获取评论数
            Long childCount = getArticleChildCommentCount(articleComment.getUuid());
            articleCommentListParam.setHasMore(childCount.intValue() > childSize);

            // 获取发布者用户信息
            User user = userMapper.selectById(articleComment.getUserId());
            articleCommentListParam.setAvatarHref(user.getAvatar());
            articleCommentListParam.setUserName(user.getUsername());

            ArticleCommentLikeRelation articleCommentLikeRelation = articleCommentLikeRelationService.getLikeArticleComment(articleComment.getUuid());
            articleCommentListParam.setLikeState(articleCommentLikeRelation != null ? articleCommentLikeRelation.getState() : LikeStateEnum.DISLIKE.getValue());

            data.add(articleCommentListParam);
        }
        return data;
    }

    @Override
    public List<ArticleCommentSingleParam> loadMoreReply(String articleCommentId, Integer childSize) {
        List<ArticleCommentSingleParam> childCommentList = articleCommentMapper.getChildCommentList(articleCommentId, childSize, 9999);
        for (ArticleCommentSingleParam articleCommentSingleParam : childCommentList) {
            ArticleCommentLikeRelation articleCommentLikeRelation = articleCommentLikeRelationService.getLikeArticleComment(articleCommentSingleParam.getUuid());
            articleCommentSingleParam.setLikeState(articleCommentLikeRelation != null ? articleCommentLikeRelation.getState() : LikeStateEnum.DISLIKE.getValue());
        }
        return childCommentList;
    }

    @Override
    public void deleteArticleComment(String commentId) {
        // 更新本条评论记录
        ArticleComment articleComment = articleCommentMapper.selectById(commentId);
        Asserts.failIsTrue(articleComment == null,"删除失败，评论已不存在");
        articleComment.setIsDel(1);
        // 查询子评论
        QueryWrapper<ArticleComment> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_comment_id",commentId)
                .or(e ->{
                    e.eq("reply_comment_id",commentId);
                });
        List<ArticleComment> articleComments = articleCommentMapper.selectList(wrapper);
        // 进行更新
        TransactionUtil.transaction(()->{
            int i = articleCommentMapper.updateById(articleComment);
            Asserts.failIsTrue(i < 1, "删除失败");
            for (ArticleComment comment : articleComments) {
                comment.setIsDel(1);
                int i1 = articleCommentMapper.updateById(comment);
                Asserts.failIsTrue(i1 < 1, "删除失败");
            }
        });
    }

    @Override
    public Map<String, Object> getCommentNotification(BasePageParam pageParam) {
        User curUser = jwtTokenUtil.getCurrentUserFromHeader();

        IPage<ArticleComment> iPage = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        IPage<ArticleComment> page = articleCommentMapper.selectCommentNotification(iPage,curUser.getUuid());
        List<ArticleCommentNotificationParam> result = new ArrayList<>();
        for (ArticleComment record : page.getRecords()) {
            ArticleCommentNotificationParam param = new ArticleCommentNotificationParam();
            param.setArticleComment(record);
            User user = userMapper.selectById(record.getUserId());
            param.setUserId(user.getUuid());
            param.setAvatar(user.getAvatar());
            param.setUsername(user.getUsername());
            Article article = articleMapper.selectById(record.getArticleId());
            param.setArticleId(article.getUuid());
            param.setTitle(article.getTitle());
            result.add(param);
        }
        Map<String,Object> map = new HashMap<>(4);
        map.put("total",page.getTotal());
        map.put("current",page.getCurrent());
        map.put("size",page.getSize());
        map.put("data",result);
        return map;
    }

    private Long getArticleChildCommentCount(String uuid) {
        QueryWrapper<ArticleComment> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_comment_id", uuid);
        return articleCommentMapper.selectCount(wrapper);
    }

    private List<ArticleComment> getArticleFatherComment(String articleId) {
        QueryWrapper<ArticleComment> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id", articleId)
                .eq("is_del", 0)
                .and(e -> e.isNull("parent_comment_id").or().eq("parent_comment_id", ""))
                .orderByDesc("create_time");
        return articleCommentMapper.selectList(wrapper);
    }

    private void checkArticleCommentParam(ArticleComment articleComment) {
        Asserts.failIsTrue(StrUtil.isEmpty(articleComment.getComment()), "评论内容不为空");
        Asserts.failIsTrue(StrUtil.isEmpty(articleComment.getArticleId()), "评论出错");
    }
}




