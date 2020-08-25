package com.pw.controller;

import com.pw.pojo.Account;
import com.pw.pojo.utils.VerificationAns;
import com.pw.service.AccountService;
import com.pw.utils.SendEmail;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Api(value = "忘记密码接口层次", tags = "忘记密码接口")
@Controller
@Slf4j
@RequestMapping(value = "/account", produces = "text/html;charset=utf-8")
public class ForgetPasswordController {
    @Autowired
    private AccountService accountService;
    @Autowired
    JavaMailSenderImpl javaMailSender;

    @PostMapping("/queryProblemPassword")
    @ApiOperation("查询密保")
    @ResponseBody
    public String queryProblemPassword(String userName, HttpSession session) {
        if (accountService.login(userName, null) != null) {
            session.setAttribute("PN", accountService.login(userName, null).getId());
        }
        return accountService.queryProblemAnswer(userName);
    }

    @PostMapping("/forgetPassword")
    @ApiOperation("忘记密码")
    public String forgetPassword(VerificationAns verificationAns, Model model) {
        if (accountService.login(verificationAns.getUserName(), null).getProblemAnswer().equals(verificationAns.getProblemAnswer())) {
            return "forget2";
        } else {
            model.addAttribute("answerPrompt", "答案错误!!!");
            return "forget1";
        }
    }

    @PostMapping("/queryProblem")
    @ApiOperation("查询问题")
    @ResponseBody
    public String queryProblem(String userName, HttpSession session) {
        Account account1 = accountService.login(userName, null);
        if (account1 == null) {
            return "未查询到密保问题！(可能该用户不存在)";
        }
        session.setAttribute("PN", account1.getId());
        return account1.getProblemPassword();
    }

    @PostMapping("/setPassword")
    @ApiOperation("/设置新密码")
    public String setPassword(String newPassword, String confirmPassword, HttpSession session, Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "两次输入的密码不一致！");
            return "forget2";
        }
        Account account = new Account();
        account.setId((Integer) session.getAttribute("PN"));
        account.setPassword(newPassword);
        accountService.updateAccount(account);
        return "forget3";
    }

    @GetMapping("/verificationEmail")
    public Boolean verificationEmail(String userName){
        Account account1 = accountService.login(userName, null);
        return account1.getEmail()!=null;
    }

    @GetMapping("/getCode")
    public void getCode(String email, HttpSession session) {
        String code = SendEmail.achieveCode();
        session.setAttribute("code", code);
        //TimerTask实现2分钟后从session中删除checkCode
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
               session.removeAttribute("code");
                System.out.println("code删除成功");
                timer.cancel();
            }
        }, 2 * 60 * 1000);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("火图网提醒您：您的验证码如下，二分钟内有效");
        simpleMailMessage.setText(code);
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom("2117940371@qq.com");
        javaMailSender.send(simpleMailMessage);
    }
    @GetMapping("verificationCode")
    public String verificationCode(HttpSession session,String code){
        String s=(String)session.getAttribute("code");
        if(s!=null){
            if(s.equals(code)){
                return "true";
            }
           return "false";
        }else {
            return "验证码格式不对！";
        }
    }
}
