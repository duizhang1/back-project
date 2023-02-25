package com.zhf.webfont.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhf.common.bo.BasePageParam;
import com.zhf.webfont.bo.ArticleLikeNotificationParam;
import com.zhf.webfont.mapper.ArticleMapper;
import com.zhf.webfont.mapper.UserMapper;
import com.zhf.webfont.po.Article;
import com.zhf.webfont.po.ArticleClickRelation;
import com.zhf.webfont.po.User;
import com.zhf.webfont.service.ArticleClickRelationService;
import com.zhf.webfont.mapper.ArticleClickRelationMapper;
import com.zhf.webfont.service.NotificationUnreadService;
import com.zhf.webfont.util.JwtTokenUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private ArticleMapper articleMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private NotificationUnreadService notificationUnreadService;
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

    @Override
    public Map<String,Object> getArticleLikeNotifications(BasePageParam pageParam) {
        User curUser = jwtTokenUtil.getCurrentUserFromHeader();
        IPage<ArticleClickRelation> iPage = new Page<>(pageParam.getCurrent(),pageParam.getSize());
        IPage<ArticleClickRelation> page = articleClickRelationMapper.selectLikeNotification(iPage,curUser.getUuid());
        List<ArticleLikeNotificationParam> result = new ArrayList<>();
        for (ArticleClickRelation record : page.getRecords()) {
            ArticleLikeNotificationParam param = new ArticleLikeNotificationParam();
            Article article = articleMapper.selectById(record.getArticleId());
            param.setArticleId(article.getUuid());
            param.setTitle(article.getTitle());
            User user = userMapper.selectById(record.getUserId());
            param.setUserId(user.getUuid());
            param.setAvatar(user.getAvatar());
            param.setUsername(user.getUsername());
            param.setRelation(record);
            result.add(param);
        }
        Map<String,Object> map = new HashMap<>(4);
        map.put("size",page.getSize());
        map.put("current",page.getCurrent());
        map.put("total",page.getTotal());
        map.put("data",result);

        // 清空未读消息
        notificationUnreadService.clearLikeUnreadCount();

        return map;
    }
}




