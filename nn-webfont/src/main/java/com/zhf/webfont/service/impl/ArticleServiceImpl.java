package com.zhf.webfont.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhf.common.enumType.ArticleOrderEnum;
import com.zhf.common.enumType.LikeStateEnum;
import com.zhf.common.enumType.TitleStateEnum;
import com.zhf.common.exception.Asserts;
import com.zhf.common.util.TransactionUtil;
import com.zhf.webfont.bo.ArticleInsertParam;
import com.zhf.webfont.bo.ArticleListParam;
import com.zhf.webfont.bo.ArticleListShowParam;
import com.zhf.webfont.bo.StoreArticleParam;
import com.zhf.webfont.mapper.*;
import com.zhf.webfont.po.*;
import com.zhf.webfont.service.*;
import com.zhf.webfont.util.JwtTokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author 10276
 * @Date 2023/1/7 14:23
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserService userService;
    @Resource
    private ArticleLabelRelationMapper articleLabelRelationMapper;
    @Resource
    private ArticleLabelRelationService articleLabelRelationService;
    @Resource
    private NotificationUnreadMapper notificationUnreadMapper;
    @Resource
    private NotificationUnreadService notificationUnreadService;
    @Resource
    private LabelService labelService;
    @Resource
    private ArticleClickRelationService articleClickRelationService;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private ArticleCacheService articleCacheService;

    @Override
    public void insertArticle(ArticleInsertParam articleInsertParam) {
        checkArticleParams(articleInsertParam);

        User user = jwtTokenUtil.getCurrentUserFromHeader();
        Article article = new Article();
        BeanUtil.copyProperties(articleInsertParam, article);

        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());
        article.setCreatorId(user.getUuid());
        article.setLikeCount(0);
        article.setReadCount(0);
        article.setStoreCount(0);
        article.setTitleState(TitleStateEnum.REVIEWING.getValue());

        TransactionUtil.transaction(() -> {
            int insert = articleMapper.insert(article);
            Asserts.failIsTrue(insert < 1, "添加失败");
            for (String labelId : articleInsertParam.getLabelIds()) {
                ArticleLabelRelation articleLabelRelation = new ArticleLabelRelation();
                articleLabelRelation.setLabelId(labelId);
                articleLabelRelation.setArticleId(article.getUuid());
                articleLabelRelation.setCreateTime(new Date());
                articleLabelRelation.setUpdateTime(new Date());
                int insert1 = articleLabelRelationMapper.insert(articleLabelRelation);
                Asserts.failIsTrue(insert1 < 1, "添加失败");
            }
        });
    }

    @Override
    public void deleteArticle(String articleId) {
        checkCanDeleteArticle(articleId);

        // 开启事务
        TransactionUtil.transaction(() -> {
            // todo 删除文章评论
            int del = articleMapper.deleteById(articleId);
            Asserts.failIsTrue(del < 1, "删除失败");
        });
    }

    @Override
    public void updateArticle(ArticleInsertParam articleInsertParam) {
        checkCanUpdateArticle(articleInsertParam);

        Article article = new Article();
        BeanUtil.copyProperties(articleInsertParam, article);
        QueryWrapper<ArticleLabelRelation> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id", articleInsertParam.getUuid());

        article.setUpdateTime(new Date());
        TransactionUtil.transaction(() -> {
            int update = articleMapper.updateById(article);
            Asserts.failIsTrue(update < 1, "文章更新失败");
            articleLabelRelationMapper.delete(wrapper);
            for (String labelId : articleInsertParam.getLabelIds()) {
                ArticleLabelRelation articleLabelRelation = new ArticleLabelRelation();
                articleLabelRelation.setLabelId(labelId);
                articleLabelRelation.setArticleId(article.getUuid());
                articleLabelRelation.setCreateTime(new Date());
                articleLabelRelation.setUpdateTime(new Date());
                int insert1 = articleLabelRelationMapper.insert(articleLabelRelation);
                Asserts.failIsTrue(insert1 < 1, "更新失败");
            }
        });
    }

    @Override
    public List<ArticleListShowParam> getArticleList(ArticleListParam articleListParam) {
        String sortRoute = articleListParam.getSortRoute();
        // 按最新排序
        if (ArticleOrderEnum.NEW.getValue().equals(articleListParam.getOrderBy())) {
            Page<ArticleListShowParam> page = new Page<>(articleListParam.getCurrent(), articleListParam.getSize());
            OrderItem orderItem = new OrderItem();
            orderItem.setColumn("create_time");
            orderItem.setAsc(false);
            page.addOrder(orderItem);
            IPage<ArticleListShowParam> articleListShowParamList = null;
            if ("all".equals(sortRoute)) {
                articleListParam.setSortRoute(null);
            } else if ("focus".equals(sortRoute)) {
                // todo 获得当前用户关注列表，然后按最新的时间排序获得发布的文章
                return null;
            }
            articleListShowParamList = articleMapper.getArticleListShowParamList(page, articleListParam);
            for (ArticleListShowParam record : articleListShowParamList.getRecords()) {
                List<Label> articleLabel = articleLabelRelationService.getArticleLabel(record.getUuid());
                List<String> labelNames = new ArrayList<>();
                for (Label label : articleLabel) {
                    labelNames.add(label.getLabelName());
                }
                record.setLabelName(labelNames);
            }
            return articleListShowParamList.getRecords();
        } else if (ArticleOrderEnum.HOT.getValue().equals(articleListParam.getOrderBy())) {

        }
        return null;
    }

    @Override
    public Article getArticleInfo(String articleId) {
        articleCacheService.incrArticleRead(articleId);
        return articleMapper.selectById(articleId);
    }

    @Override
    public void likeArticle(String articleId) {
        checkCanLikeOrStoreArticle(articleId);
        User currentUser = jwtTokenUtil.getCurrentUserFromHeader();
        ArticleClickRelation articleClickRelation = articleClickRelationService.getCurUserClick(articleId);
        Article article = articleMapper.selectById(articleId);
        article.setLikeCount(article.getLikeCount() + 1);
        article.setUpdateTime(new Date());

        if (articleClickRelation != null) {
            articleClickRelation.setUpdateTime(new Date());
            articleClickRelation.setState(LikeStateEnum.LIKE.getValue());
            TransactionUtil.transaction(() -> {
                // 更新点赞记录的状态
                articleClickRelationService.updateById(articleClickRelation);
                // 更新点赞数
                int i = articleMapper.updateById(article);
                Asserts.failIsTrue(i < 1, "点赞失败");
            });
        } else {
            ArticleClickRelation clickRelation = new ArticleClickRelation();
            clickRelation.setArticleId(articleId);
            clickRelation.setUserId(currentUser.getUuid());
            clickRelation.setCreateTime(new Date());
            clickRelation.setUpdateTime(new Date());
            clickRelation.setState(LikeStateEnum.LIKE.getValue());
            TransactionUtil.transaction(() -> {
                // 添加点赞记录
                articleClickRelationService.save(clickRelation);
                // 更新点赞数
                int i = articleMapper.updateById(article);
                Asserts.failIsTrue(i < 1, "点赞失败");
            });
        }
        // 更新一个未读点赞记录
        notificationUnreadService.addLikeUnreadCount(article.getCreatorId());
        // todo 是否需要点赞信息加入redis
//        articleCacheService.likeArticlePip(articleId);
    }

    @Override
    public void dislikeArticle(String articleId) {
        checkCanLikeOrStoreArticle(articleId);

        User currentUser = jwtTokenUtil.getCurrentUserFromHeader();
        ArticleClickRelation articleClickRelation = articleClickRelationService.getCurUserClick(articleId);

        Article article = articleMapper.selectById(articleId);
        article.setLikeCount(article.getLikeCount() - 1);
        article.setUpdateTime(new Date());

        if (articleClickRelation != null) {
            articleClickRelation.setUpdateTime(new Date());
            articleClickRelation.setState(LikeStateEnum.DISLIKE.getValue());
            TransactionUtil.transaction(() -> {
                articleClickRelationService.updateById(articleClickRelation);
                int i = articleMapper.updateById(article);
                Asserts.failIsTrue(i < 1, "点赞失败");
            });
        }

        // todo 向redis中取消点赞记录
    }


    @Override
    public ArticleInsertParam isCanUpdateArticle(String id) {
        User currentUser = jwtTokenUtil.getCurrentUserFromHeader();
        Article articleInfo = checkArticleExist(id);
        Asserts.failIsTrue(!currentUser.getUuid().equals(articleInfo.getCreatorId()), "当前用户不能修改文章");
        ArticleInsertParam articleInsertParam = new ArticleInsertParam();
        BeanUtil.copyProperties(articleInfo, articleInsertParam);
        List<String> labelIds = new ArrayList<>();
        for (Label articleLabel : labelService.getLabelsFromArticleId(id)) {
            labelIds.add(articleLabel.getUuid());
        }
        articleInsertParam.setLabelIds(labelIds);
        return articleInsertParam;
    }

    @Override
    public Map<String, Object> getArticleAndUserInfo(String articleId) {
        checkArticleExist(articleId);
        Article articleInfo = getArticleInfo(articleId);
        Map<String, Object> map = new HashMap<>(2);
        map.put("article", articleInfo);
        List<Label> labels = labelService.getLabelsFromArticleId(articleId);
        map.put("labels", labels);
        User user = userMapper.selectById(articleInfo.getCreatorId());
        map.put("author", user);
        return map;
    }

    private void checkCanLikeOrStoreArticle(String articleId) {
        Article articleByUuid = checkArticleExist(articleId);

        // 校验当前登陆用户是不是文章的创建人，不是则通过
        Asserts.failIsTrue(checkCurrentUserIsArticleCreator(articleByUuid.getCreatorId()), "不能操作自己的文章");
    }

    private void checkCanUpdateArticle(ArticleInsertParam articleInsertParam) {
        Article articleByUuid = checkArticleExist(articleInsertParam.getUuid());

        Asserts.failIsTrue(!checkCurrentUserIsArticleCreator(articleByUuid.getCreatorId()), "当前用户没有更新该文章的权限");

        checkArticleParams(articleInsertParam);
    }

    private void checkCanDeleteArticle(String articleId) {
        Article article = checkArticleExist(articleId);

        // 校验当前登陆用户是不是文章的创建人
        Asserts.failIsTrue(!checkCurrentUserIsArticleCreator(article.getCreatorId()), "当前用户没有删除该文章的权限");
    }

    /**
     * 校验当前文章是否存在
     *
     * @param articleId
     * @return
     */
    private Article checkArticleExist(String articleId) {
        Asserts.failIsTrue(StrUtil.isEmpty(articleId), "文章不存在");
        Article article = articleMapper.selectById(articleId);
        Asserts.failIsTrue(article == null, "文章不存在");
        return article;
    }

    /**
     * 校验当前登陆用户是不是文章的创建人
     *
     * @param articleCreatorId
     * @return
     */
    private boolean checkCurrentUserIsArticleCreator(String articleCreatorId) {
        User user = jwtTokenUtil.getCurrentUserFromHeader();
        return user.getUuid().equals(articleCreatorId);
    }

    private void checkArticleParams(ArticleInsertParam articleInsertParam) {
        Asserts.failIsTrue(StrUtil.isEmpty(articleInsertParam.getTitle()), "文章标题不为空");
        Asserts.failIsTrue(StrUtil.isEmpty(articleInsertParam.getSortId()), "文章分类不为空");
        Asserts.failIsTrue(StrUtil.isEmpty(articleInsertParam.getContent()), "文章正文不为空");
        Asserts.failIsTrue(CollUtil.isEmpty(articleInsertParam.getLabelIds()), "文章标签不为空");
        Asserts.failIsTrue(StrUtil.isEmpty(articleInsertParam.getSummary()), "文章摘要不为空");
    }

}
