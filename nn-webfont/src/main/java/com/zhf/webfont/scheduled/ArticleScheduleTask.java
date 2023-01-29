package com.zhf.webfont.scheduled;

import com.zhf.webfont.mapper.ArticleMapper;
import com.zhf.webfont.po.Article;
import com.zhf.webfont.service.ArticleCacheService;
import com.zhf.webfont.service.ArticleService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Author 10276
 * @Date 2023/1/28 15:04
 */
@Configuration
@EnableScheduling
public class ArticleScheduleTask {

    @Resource
    private ArticleCacheService articleCacheService;
    @Resource
    private ArticleMapper articleMapper;

    @Scheduled(cron = "0 0/30 * * * ?")
    private void saveReadCountTask(){
        List<String> updateArticle = articleCacheService.getUpdateArticle();
        for (String articleId : updateArticle) {
            Integer articleReadNumber = articleCacheService.getArticleRead(articleId);
            Article article = new Article();
            article.setUpdateTime(new Date());
            article.setUuid(articleId);
            article.setReadCount(articleReadNumber);
            int i = articleMapper.updateById(article);
        }
    }


}
