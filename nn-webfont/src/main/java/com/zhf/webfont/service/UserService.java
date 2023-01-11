package com.zhf.webfont.service;

import com.zhf.common.returnType.CommonResult;
import com.zhf.webfont.bo.UserLoginParam;
import com.zhf.webfont.bo.UserRegisterParam;
import com.zhf.webfont.po.User;

/**
 * @Author 10276
 * @Date 2023/1/4 0:16
 */
public interface UserService {
    /**
     * 登陆
     * @param userLoginParam
     * @return
     */
    String login(UserLoginParam userLoginParam);

    /**
     * 注册
     * @param userRegisterParam
     */
    void register(UserRegisterParam userRegisterParam);

    /**
     * 从邮箱地址获得用户信息
     * @param emailAddress
     * @return
     */
    User getUserFromEmailAddress(String emailAddress);

    /**
     * 获得当前登陆用户的信息
     * @return
     */
    User getCurrentUser();
}
