package com.zhf.webfont.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author 10276
 * @Date 2023/1/3 13:46
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public CheckLoginInterceptor checkLoginInterceptor(){
        return new CheckLoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkLoginInterceptor())
                .addPathPatterns("/**");
    }
}
