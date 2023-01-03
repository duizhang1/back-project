package com.zhf.webfont.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Author 10276
 * @Date 2023/1/3 0:46
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedLogin {
    /**
     * 是否需要登陆
     * @return
     */
    boolean value() default true;
}
