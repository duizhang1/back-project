package com.zhf.webfont.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhf.common.enumType.ArticleOrderEnum;
import com.zhf.common.enumType.TitleStateEnum;
import com.zhf.common.exception.Asserts;
import com.zhf.common.util.TransactionUtil;
import com.zhf.webfont.bo.ArticleListParam;
import com.zhf.webfont.bo.ArticleListShowParam;
import com.zhf.webfont.bo.StoreArticleParam;
import com.zhf.webfont.mapper.ArticleMapper;
import com.zhf.webfont.po.Article;
import com.zhf.webfont.po.StoreList;
import com.zhf.webfont.po.StoreListUserRealation;
import com.zhf.webfont.po.User;
import com.zhf.webfont.service.*;
import com.zhf.webfont.util.JwtTokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author 10276
 * @Date 2023/1/7 14:23
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private UserService userService;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private ArticleCacheService articleCacheService;
    @Resource
    private StoreListUserRealationService storeListUserRealationService;

    @Override
    public void insertArticle(Article article) {
        checkArticleParams(article);
        
        User user = jwtTokenUtil.getCurrentUserFromHeader();

        article.setCreateTime(new Date());
        article.setCreatorId(user.getUuid());
        article.setLikeCount(0);
        article.setReadCount(0);
        article.setStoreCount(0);
        article.setTitleState(TitleStateEnum.REVIEWING.getValue());

        int insert = articleMapper.insert(article);
        Asserts.failIsTrue(insert < 1,"添加失败");
    }

    @Override
    public void deleteArticle(String articleId) {
        checkCanDeleteArticle(articleId);

        // 开启事务
        TransactionUtil.transaction(() ->{
            // todo 删除文章评论
            int del = articleMapper.deleteById(articleId);
            Asserts.failIsTrue(del < 1,"删除失败");
        });
    }

    @Override
    public void updateArticle(Article article) {
        checkCanUpdateArticle(article);

        article.setUpdateTime(new Date());
        int update = articleMapper.updateById(article);
        Asserts.failIsTrue(update < 1,"文章更新失败");
    }

    @Override
    public List<ArticleListShowParam> getArticleList(ArticleListParam articleListParam) {

        // 按最新排序
        if (ArticleOrderEnum.NEW.getValue().equals(articleListParam.getOrderBy())){
            Page<ArticleListShowParam> page = new Page<>(articleListParam.getCurrent(), articleListParam.getSize());
            OrderItem orderItem = new OrderItem();
            orderItem.setColumn("create_time");
            orderItem.setAsc(false);
            page.addOrder(orderItem);
            IPage<ArticleListShowParam> articleListShowParamList = articleMapper.getArticleListShowParamList(page, articleListParam);
            return articleListShowParamList.getRecords();
        }else if (ArticleOrderEnum.HOT.getValue().equals(articleListParam.getOrderBy())){

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

        //向redis中添加一条点赞记录，以及更新点赞数
        articleCacheService.likeArticlePip(articleId);
        //todo 添加一条消息通知，即xxx点赞了作者的文章
    }

    @Override
    public void dislikeArticle(String articleId) {
        checkCanLikeOrStoreArticle(articleId);

        //向redis中取消点赞记录，没有记录就为false，说明已经存入数据库中
        if (!articleCacheService.dislikeArticle(articleId)){

        }
    }

    @Override
    public void storeArticle(StoreArticleParam storeArticleParam) {
        checkCanLikeOrStoreArticle(storeArticleParam.getArticleId());

        StoreListUserRealation storeListUserRealation = new StoreListUserRealation();
        storeListUserRealation.setSortListId(storeArticleParam.getStoreListId());
        storeListUserRealation.setArticleId(storeArticleParam.getArticleId());
        storeListUserRealation.setCreateTime(new Date());
        storeListUserRealation.setUpdateTime(new Date());

        TransactionUtil.transaction(() ->{
            boolean save = storeListUserRealationService.save(storeListUserRealation);
            Asserts.failIsTrue(!save,"收藏文章失败");
        });
        articleCacheService.incrArticleStore(storeArticleParam.getArticleId());
    }

    @Override
    public Article isCanUpdateArticle(String id) {
        User currentUser = jwtTokenUtil.getCurrentUserFromHeader();
        Article articleInfo = checkArticleExist(id);
        Asserts.failIsTrue(!currentUser.getUuid().equals(articleInfo.getCreatorId()),"当前用户不能修改文章");
        return articleInfo;
    }

    private void checkCanLikeOrStoreArticle(String articleId) {
        Article articleByUuid = checkArticleExist(articleId);

        // 校验当前登陆用户是不是文章的创建人，不是则通过
        Asserts.failIsTrue(checkCurrentUserIsArticleCreator(articleByUuid.getCreatorId()),"不能操作自己的文章");
    }

    private void checkCanUpdateArticle(Article article) {
        Article articleByUuid = checkArticleExist(article.getUuid());

        Asserts.failIsTrue(!checkCurrentUserIsArticleCreator(articleByUuid.getCreatorId()),"当前用户没有更新该文章的权限");

        checkArticleParams(article);
    }

    private void checkCanDeleteArticle(String articleId) {
        Article article = checkArticleExist(articleId);

        // 校验当前登陆用户是不是文章的创建人
        Asserts.failIsTrue(!checkCurrentUserIsArticleCreator(article.getCreatorId()),"当前用户没有删除该文章的权限");
    }

    /**
     * 校验当前文章是否存在
     * @param articleId
     * @return
     */
    private Article checkArticleExist(String articleId){
        Asserts.failIsTrue(StrUtil.isEmpty(articleId),"文章不存在");
        Article article = articleMapper.selectById(articleId);
        Asserts.failIsTrue(article == null,"文章不存在");
        return article;
    }

    /**
     * 校验当前登陆用户是不是文章的创建人
     * @param articleCreatorId
     * @return
     */
    private boolean checkCurrentUserIsArticleCreator(String articleCreatorId){
        User user = jwtTokenUtil.getCurrentUserFromHeader();
        return user.getUuid().equals(articleCreatorId);
    }

    private void checkArticleParams(Article article) {
        Asserts.failIsTrue(StrUtil.isEmpty(article.getTitle()),"文章标题不为空");
        Asserts.failIsTrue(StrUtil.isEmpty(article.getSortId()),"文章分类不为空");
        Asserts.failIsTrue(StrUtil.isEmpty(article.getContent()),"文章正文不为空");
        Asserts.failIsTrue(StrUtil.isEmpty(article.getLabelId()),"文章标签不为空");
        Asserts.failIsTrue(StrUtil.isEmpty(article.getSummary()),"文章摘要不为空");
    }

}
