package com.zhf.common.annotation;

import java.lang.annotation.*;

/**
 * 如果方法上有该注解，切面如果检测到方法上有改注解，对应的redis方法就不会抛出redis出现的异常
 * 如果没有该注解，redisService中的方法如果出现redis异常就会直接抛出
 * @Author 10276
 * @Date 2023/1/5 12:15
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheException {
}
