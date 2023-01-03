package com.zhf.webfont;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author 10276
 * @Date 2023/1/3 13:48
 */
@SpringBootApplication(scanBasePackages = "com.zhf")
@MapperScan(basePackages = "com.zhf.webfont.mapper")
public class WebFontApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebFontApplication.class);
    }

}
