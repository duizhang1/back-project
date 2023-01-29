package com.zhf.webfont.service.impl;

import cn.hutool.core.util.StrUtil;
import com.zhf.common.annotation.CacheException;
import com.zhf.common.service.RedisService;
import com.zhf.webfont.po.User;
import com.zhf.webfont.service.ArticleCacheService;
import com.zhf.webfont.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author 10276
 * @Date 2023/1/8 22:34
 */
@Service
public class ArticleCacheServiceImpl implements ArticleCacheService {

    @Resource
    private RedisService redisService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Value("${redis.database}")
    private String DATABASE;
    @Value("${redis.key.articleReadCount}")
    private String ARTICLE_READ_COUNT;
    @Value("${redis.key.articleLikeRecord}")
    private String ARTICLE_LIKE_RECORD;
    @Value("${redis.key.articleLikeNumber}")
    private String ARTICLE_LIKE_NUMBER;
    @Value("${redis.key.articleStoreNumber}")
    private String ARTICLE_STORE_NUMBER;
    @Value("${redis.key.articleUpdateSet}")
    private String ARTICLE_UPDATE_SET;



    @Override
    @CacheException
    public void incrArticleRead(String articleId) {
        // 阅读量+1，并向更新文章队列进行添加
        redisService.incr(DATABASE + ":" + ARTICLE_READ_COUNT + ":" + articleId,
                1);
        redisService.sAdd(DATABASE + ":" + ARTICLE_UPDATE_SET,articleId);
    }

    @Override
    public void likeArticlePip(String articleId) {
        User user = jwtTokenUtil.getCurrentUserFromHeader();
        redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                // 增加点赞记录 设置key：前缀:账号uuid:文章ID 设置value: 当前时间:1
                Boolean set = connection.set((DATABASE + ":" + ARTICLE_LIKE_RECORD + ":" + user.getUuid() + ":" + articleId).getBytes(StandardCharsets.UTF_8), (System.currentTimeMillis() + ":" + "1").getBytes(StandardCharsets.UTF_8));
                // 增加点赞数
                Long incr = connection.incr((DATABASE + ":" + ARTICLE_LIKE_NUMBER + ":" + articleId).getBytes(StandardCharsets.UTF_8));
                connection.closePipeline();
                return null;
            }
        });
    }

    @Override
    @CacheException
    public void incrArticleStore(String articleId) {
        redisService.incr(DATABASE + ":" + ARTICLE_STORE_NUMBER + ":" + articleId,1);
    }

    @Override
    public boolean dislikeArticle(String articleId) {
        User user = jwtTokenUtil.getCurrentUserFromHeader();
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setLocation(new ClassPathResource("com.zhf.webfont.lua.ArticleDisLike.lua"));
        List<String> keys = new ArrayList<>();
        keys.add(DATABASE + ":" + ARTICLE_LIKE_RECORD + ":" + user.getUuid() + ":" + articleId);
        Boolean execute = redisTemplate.execute(redisScript, keys);
        return execute != null && execute;
    }

    @Override
    public List<String> getUpdateArticle() {
        DefaultRedisScript<List> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(List.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("com/zhf/webfont/lua/GetAndClearSet.lua")));
        List updateList = redisTemplate.execute(redisScript, Arrays.asList(DATABASE + ":" + ARTICLE_UPDATE_SET));
        return updateList;
    }

    @Override
    public Integer getArticleRead(String articleId) {
        return (Integer) redisService.get(DATABASE + ":" + ARTICLE_READ_COUNT + ":" + articleId);
    }
}
