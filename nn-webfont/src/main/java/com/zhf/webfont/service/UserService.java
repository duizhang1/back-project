package com.zhf.webfont.service;

import com.zhf.common.returnType.CommonResult;
import com.zhf.webfont.bo.UserLoginParam;
import com.zhf.webfont.bo.UserRegisterParam;

/**
 * @Author 10276
 * @Date 2023/1/4 0:16
 */
public interface UserService {
    String login(UserLoginParam userLoginParam);

    void register(UserRegisterParam userRegisterParam);
}
