package com.pw.config;

import com.pw.dao.AccountMap;
import com.pw.service.AccountService;
import com.pw.service.AccountServiceImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 未登录拦截器
 *
 * @author 武祥市
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获得cookie
        Cookie[] cookies = request.getCookies();
        // 定义cookie_username，用户的一些登录信息，例如：用户名，密码等
        if(cookies==null){
            return true;
        }
        String cookie_username = null;
        // 获取cookie里面的一些用户信息
        for (Cookie item : cookies) {
            if ("cookie_username".equals(item.getName())) {
                cookie_username = item.getValue();
                break;
            }
        }
        // 如果cookie里面没有包含用户的一些登录信息，则重定向到主页
        if (StringUtils.isEmpty(cookie_username)) {
            return true;
        }
        // 获取HttpSession对象
        HttpSession session = request.getSession();
        // 获取我们登录后存在session中的用户信息，如果为空，表示session已经过期
        Object obj = session.getAttribute("Account");
        WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        AccountService accountService = applicationContext.getBean(AccountService.class);
        if (null == obj) {
            // 根据用户登录账号获取数据库中的用户信息
            System.out.println("用户信息====>>>" + cookie_username);
            session.setAttribute("Account", accountService.login(cookie_username, null));
        }
        System.out.println("登录成功");
        // 已经登录
        return true;
    }
}

