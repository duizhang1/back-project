package com.zhf.webfont.config;

import com.zhf.common.config.BaseSwaggerConfig;
import com.zhf.common.domain.SwaggerProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author 10276
 * @Date 2023/1/5 11:22
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.zhf.webfont.controller")
                .title("NotNull前台API")
                .description("NotNull前台相关接口文档")
                .contactName("zhf")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }
}
