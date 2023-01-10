package com.zhf.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author 10276
 * @Date 2023/1/11 1:14
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
                // 拦截所有的请求
        registry.addMapping("/**")
                // 可跨域的域名，可以为 *
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                // 允许跨域的方法，可以单独配置
                .allowedMethods("*")
                // 允许跨域的请求头，可以单独配置
                .allowedHeaders("*");
    }
}

