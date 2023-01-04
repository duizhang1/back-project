package com.zhf.webfont.service;

/**
 * @Author 10276
 * @Date 2023/1/4 20:30
 */
public interface MailService {
    /**
     * 发送注册邮箱验证码
     * @param emailAddress
     */
    void sendVerifyCode(String emailAddress);
}
