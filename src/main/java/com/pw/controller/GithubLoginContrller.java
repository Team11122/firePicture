package com.pw.controller;

import com.pw.dao.AccountMap;
import com.pw.pojo.AccessToken;
import com.pw.pojo.Account;
import com.pw.pojo.GitUser;
import com.pw.service.AccountService;
import com.pw.utils.GithubPro;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author 武祥市
 */
@Controller
@Api(value = "", tags = "Github登录接口")
public class GithubLoginContrller {

    @Autowired
    private GithubPro githubPro;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountMap accountMap;

    @ApiOperation("登录返回的方法")
    @GetMapping("/callbacks")  //首先通过a标签的连接登录成功之后会返回code ,state两个参数并且进入到callbacks请求
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state, HttpSession session) throws IOException {
        AccessToken accessToken = new AccessToken();//根据官方文档，之后进入callbacks请求后，需要再次传递一下五个属性。利用okhttp解析code获取到token
        accessToken.setCode(code);
        accessToken.setState(state);
        accessToken.setRedirect_uri("http://www.wxsaxaxa.icu/callbacks");
        accessToken.setClient_id("d61a16abe0f4be5f8a8c");
        accessToken.setClient_secret("1065d995273d0c2f9b1197b3dcb9936dc1282c54");
        String acess_token = githubPro.getAccressToken(accessToken);
        GitUser user = githubPro.getUsers(acess_token);
        System.err.println("授权登录的user:" + user.toString());
        if (!accountService.verificationGitName(user.getLogin())) {
            System.out.println("非首次Git注册。。。。。。。。");
            session.setAttribute("Account", accountMap.login(user.getLogin(), null));
        }else {
            System.out.println("首次Git注册。。。。。。。。");
            Account account = new Account();
            account.setUserName(user.getLogin());
            accountService.addAccount(account);
            session.setAttribute("Account", accountMap.login(account.getUserName(), null));
        }
        System.out.println("转发到首页");
        return "forward:/main/rotation";
    }

    @GetMapping("/Git")
    public String goLogin() {
        return "GitHubLogin";
    }
}



