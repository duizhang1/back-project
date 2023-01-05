package com.zhf.common.aspect;

import com.zhf.common.annotation.CacheException;
import com.zhf.common.exception.ApiException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * redis的切面
 * @Author 10276
 * @Date 2023/1/5 12:14
 */
@Aspect
@Component
@Order(2)
public class RedisCacheAspect {
    private static Logger LOGGER = LoggerFactory.getLogger(RedisCacheAspect.class);

    @Pointcut("execution(public * com.zhf.webfont.service.*CacheService.*(..))")
    public void cacheAspect() {
    }

    @Around("cacheAspect()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws ApiException {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            LOGGER.error(throwable.getMessage());
            //有CacheException注解的方法需要抛出异常
            if (method.isAnnotationPresent(CacheException.class)) {
                throw new ApiException("服务器内部出现异常，请联系管理员修复");
            }
        }
        return result;
    }

}