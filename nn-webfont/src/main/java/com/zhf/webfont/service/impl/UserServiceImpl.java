package com.zhf.webfont.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.Digester;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhf.common.exception.Asserts;
import com.zhf.common.returnType.CommonResult;
import com.zhf.common.service.RedisService;
import com.zhf.common.util.TransactionUtil;
import com.zhf.webfont.bo.UserLoginParam;
import com.zhf.webfont.bo.UserRegisterParam;
import com.zhf.webfont.mapper.NotificationUnreadMapper;
import com.zhf.webfont.mapper.UserMapper;
import com.zhf.webfont.po.NotificationUnread;
import com.zhf.webfont.po.User;
import com.zhf.webfont.service.MailCacheService;
import com.zhf.webfont.service.NotificationUnreadService;
import com.zhf.webfont.service.UserService;
import com.zhf.webfont.util.JwtTokenUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
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
    @Resource
    private MailCacheService mailCacheService;
    @Resource
    private NotificationUnreadMapper notificationUnreadMapper;
    @Resource
    private NotificationUnreadService notificationUnreadService;

    @Override
    public String login(UserLoginParam userLoginParam) {
        checkUserLoginParam(userLoginParam);
        User user = getUserFromEmailAddress(userLoginParam.getAccount());
        Asserts.failIsTrue(user == null, "账号或者密码错误");
        String password = user.getPassword();
        Asserts.failIsTrue(!passwordEncoder.matches(userLoginParam.getPassword(), password), "账号或者密码错误");
        return jwtTokenUtil.generateToken(userLoginParam.getAccount());
    }

    @Override
    public void register(UserRegisterParam userRegisterParam) {
        checkUserRegisterParam(userRegisterParam);

        User user = new User();
        BeanUtil.copyProperties(userRegisterParam,user);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setAvatar("https://p3-passport.byteimg.com/img/mosaic-legacy/3795/3033762272~180x180.awebp");
        user.setPassword(passwordEncoder.encode(userRegisterParam.getPassword()));

        // 创建消息记录的初始记录
        NotificationUnread notificationUnread = new NotificationUnread();
        notificationUnread.setLikeCount(0);
        notificationUnread.setCommentCount(0);
        notificationUnread.setFocusCount(0);
        notificationUnread.setCreateTime(new Date());
        notificationUnread.setUpdateTime(new Date());

        TransactionUtil.transaction(()->{
            int count = userMapper.insert(user);
            Asserts.failIsTrue(count <= 0,"出错了，注册失败");

            notificationUnreadService.InsertNotificationUnreadRecord(user.getUuid());
        });
    }

    @Override
    public User getUserFromEmailAddress(String emailAddress) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email_address", emailAddress);
        List<User> users = userMapper.selectList(wrapper);
        if (CollUtil.isEmpty(users)) {
            return null;
        }
        return users.get(0);
    }

    @Override
    public User getCurrentUser() {
        User currentUser = jwtTokenUtil.getCurrentUserFromHeader();
        Asserts.failIsTrue(currentUser == null,"暂未登陆或者登陆已过期");
        return currentUser;
    }

    private void checkUserRegisterParam(UserRegisterParam registerParam) {
        Asserts.failIsTrue(StrUtil.isEmpty(registerParam.getUsername()), "用户名不为空");
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", registerParam.getUsername());
        List<User> users = userMapper.selectList(wrapper);
        Asserts.failIsTrue(!CollUtil.isEmpty(users), "用户名已存在");

        Asserts.failIsTrue(StrUtil.isEmpty(registerParam.getPassword()), "密码不为空");
        Asserts.failIsTrue(StrUtil.isEmpty(registerParam.getRepeatPassword()), "重复密码不为空");
        Asserts.failIsTrue(!registerParam.getPassword().equals(registerParam.getRepeatPassword()), "两次输入密码不相等");

        Asserts.failIsTrue(StrUtil.isEmpty(registerParam.getEmailAddress()), "邮箱不为空");
        User userFromEmailAddress = getUserFromEmailAddress(registerParam.getEmailAddress());
        Asserts.failIsTrue(userFromEmailAddress != null, "邮箱已注册");

        Asserts.failIsTrue(StrUtil.isEmpty(registerParam.getVerifyCode()), "验证码不为空");
        String verifyCode = mailCacheService.getVerifyCode(registerParam.getEmailAddress());
        Asserts.failIsTrue(StrUtil.isEmpty(verifyCode) || !verifyCode.equalsIgnoreCase(registerParam.getVerifyCode()), "验证码错误");
    }

    private void checkUserLoginParam(UserLoginParam userLoginParam) {
        String username = userLoginParam.getAccount();
        Asserts.failIsTrue(StrUtil.isEmpty(username), "请输入账号");

        String inputPassword = userLoginParam.getPassword();
        Asserts.failIsTrue(StrUtil.isEmpty(inputPassword), "请输入密码");
    }
}
