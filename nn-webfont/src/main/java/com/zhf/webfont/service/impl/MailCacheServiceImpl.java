package com.zhf.webfont.service.impl;

import com.zhf.common.annotation.CacheException;
import com.zhf.common.service.RedisService;
import com.zhf.webfont.service.MailCacheService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author 10276
 * @Date 2023/1/5 12:57
 */
@Service
public class MailCacheServiceImpl implements MailCacheService {

    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.key.emailVerifyCode}")
    private String REDIS_KEY_EMAIL_VERIFY_CODE;
    @Value("${redis.expire.fiveMin}")
    private Long REDIS_EXPIRE_FIVE_MIN;
    @Resource
    private RedisService redisService;

    @Override
    @CacheException
    public void setVerifyCode(String emailAddress, String verifyCode) {
        redisService.set(REDIS_DATABASE + ":" + REDIS_KEY_EMAIL_VERIFY_CODE + ":" + emailAddress,verifyCode,REDIS_EXPIRE_FIVE_MIN);
    }

    @Override
    @CacheException
    public String getVerifyCode(String emailAddress) {
        return (String) redisService.get(REDIS_DATABASE + ":" + REDIS_KEY_EMAIL_VERIFY_CODE + ":" + emailAddress);
    }
}
