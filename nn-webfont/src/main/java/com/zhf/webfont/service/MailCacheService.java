package com.zhf.webfont.service;

/**
 * @Author 10276
 * @Date 2023/1/5 12:57
 */
public interface MailCacheService {
    /**
     * 将邮箱验证码存入redis中
     * @param emailAddress
     * @param verifyCode
     */
    void setVerifyCode(String emailAddress, String verifyCode);

    /**
     * 获得邮箱验证码
     * @param emailAddress
     * @return
     */
    String getVerifyCode(String emailAddress);
}
