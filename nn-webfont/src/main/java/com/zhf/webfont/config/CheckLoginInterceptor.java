package com.zhf.webfont.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhf.common.exception.Asserts;
import com.zhf.common.returnType.CommonResult;
import com.zhf.common.returnType.ResultCode;
import com.zhf.webfont.bo.UserLoginParam;
import com.zhf.webfont.mapper.UserMapper;
import com.zhf.webfont.po.User;
import com.zhf.webfont.service.UserService;
import com.zhf.webfont.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

/**
 * 用来检查用来拦截登陆的拦截器
 * @Author 10276
 * @Date 2023/1/3 1:06
 */
public class CheckLoginInterceptor implements AsyncHandlerInterceptor {
    public static final Logger LOGGER = LoggerFactory.getLogger(CheckLoginInterceptor.class);
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Resource
    private UserService userService;
    /**
     * 在请求执行前校验是否执行器方法上是否有需要校验的注解
     * 有NeedLogin注解的话，校验请求的header中token是否包含注册信息
     * 没有该注解的话，直接放行
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            // 获取类和方法上的是否需要登录注解
            NeedLogin loginController = method.getClass().getAnnotation(NeedLogin.class);
            NeedLogin needLoginMehtod = method.getMethodAnnotation(NeedLogin.class);
            // 类和方法上是否需要登录注解，不需要放行
            if (!(loginController != null && loginController.value()) && !(needLoginMehtod != null && needLoginMehtod.value())){
                return true;
            }
            //该方法需要校验是否登录
            String account = jwtTokenUtil.getAccountFromHeader();
            if (account == null){
                setResponseMsg(response);
                return false;
            }
            // 查询用户姓名是否有相关账号
            User user = userService.getUserFromEmailAddress(account);
            // 如果不存在就直接返回错误
            if (user == null){
                setResponseMsg(response);
                return false;
            }
            if (!jwtTokenUtil.isTokenExpired()){
                setResponseMsg(response);
                return false;
            }
        }
        return true;
    }

    private void setResponseMsg(HttpServletResponse response) throws Exception{
        // 请求接口，直接返回响应数据
        response.reset();
        //设置编码格式
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter pw = response.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        pw.write(mapper.writeValueAsString(CommonResult.failed(ResultCode.UNAUTHORIZED)));
        pw.flush();
        pw.close();
    }
}
