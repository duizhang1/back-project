package com.zhf.webfont.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.zhf.common.exception.ApiException;
import com.zhf.common.exception.Asserts;
import com.zhf.webfont.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author 10276
 * @Date 2023/1/4 20:30
 */
@Service
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String sendMail;
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendVerifyCode(String emailAddress) {
        Asserts.failIsTrue(StrUtil.isEmpty(emailAddress),"请输入邮箱");

        String verifyCode = RandomUtil.randomString(6);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(emailAddress);
        mailMessage.setFrom(sendMail);
        mailMessage.setSentDate(new Date());
        mailMessage.setSubject("NotNull社区注册验证码");
        mailMessage.setText("亲爱的用户：" +
                "你正在注册NotNull社区，您的邮箱验证码为: " + verifyCode + "，有效时间为5分钟，请勿转发给其他人");
        // todo 设置将verifycode存入redis中
        try {
            javaMailSender.send(mailMessage);
        }catch (MailException e){
            throw new ApiException("请确认邮箱是否正确");
        }
    }
}
