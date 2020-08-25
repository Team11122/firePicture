package com.pw;

import com.pw.config.LoginInterceptor;
import com.pw.pojo.Account;
import com.pw.pojo.AccountCollection;
import com.pw.service.AccountService;
import com.pw.utils.SendEmail;
import com.pw.utils.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class FirepictureApplicationTests {

    @Autowired
    JavaMailSenderImpl javaMailSender;
    @Autowired
    AccountService accountService;
    @Test
    void contextLoads() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("火图网提醒您：您的验证码如下，一分钟内有效");
        simpleMailMessage.setText(SendEmail.achieveCode());
        simpleMailMessage.setTo("2117940371@qq.com");
        simpleMailMessage.setFrom("2117940371@qq.com");
        javaMailSender.send(simpleMailMessage);
    }
    @Test
    void contextLoads1() {
        System.out.println(accountService.findAccount(73));
    }
}
