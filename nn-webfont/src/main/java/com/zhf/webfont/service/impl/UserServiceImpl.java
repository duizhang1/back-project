package com.zhf.webfont.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.Digester;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhf.common.exception.Asserts;
import com.zhf.common.returnType.CommonResult;
import com.zhf.webfont.bo.UserLoginParam;
import com.zhf.webfont.bo.UserRegisterParam;
import com.zhf.webfont.mapper.UserMapper;
import com.zhf.webfont.po.User;
import com.zhf.webfont.service.UserService;
import com.zhf.webfont.util.JwtTokenUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author 10276
 * @Date 2023/1/4 0:16
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public String login(UserLoginParam userLoginParam) {
        checkUserLoginParam(userLoginParam);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",userLoginParam.getUsername());
        List<User> users = userMapper.selectList(wrapper);
        Asserts.failIsTrue(CollUtil.isEmpty(users),"账号或者密码错误");
        User user = users.get(0);
        String password = user.getPassword();
        Asserts.failIsTrue(!passwordEncoder.matches(userLoginParam.getPassword(), password),"账号或者密码错误");
        return jwtTokenUtil.generateToken(userLoginParam);
    }

    @Override
    public void register(UserRegisterParam userRegisterParam) {

    }

    private void checkUserLoginParam(UserLoginParam userLoginParam) {
        String username = userLoginParam.getUsername();
        Asserts.failIsTrue(StrUtil.isEmpty(username),"请输入账号");

        String inputPassword = userLoginParam.getPassword();
        Asserts.failIsTrue(StrUtil.isEmpty(inputPassword),"请输入密码");
    }
}
