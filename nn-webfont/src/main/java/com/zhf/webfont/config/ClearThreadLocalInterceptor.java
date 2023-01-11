package com.zhf.webfont.config;

import com.zhf.common.util.ThreadLocalUtil;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 清空ThreadLocal的值，防止内存泄露。
 * @Author 10276
 * @Date 2023/1/12 0:08
 */
public class ClearThreadLocalInterceptor  implements AsyncHandlerInterceptor {

    @Resource
    private ThreadLocalUtil threadLocalUtil;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        threadLocalUtil.remove();
    }
}
