package com.zhf.webfont.config;

import com.zhf.common.config.BaseRedisConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * @Author 10276
 * @Date 2023/1/5 12:12
 */
@EnableCaching
@Configuration
public class RedisConfig extends BaseRedisConfig {

}

