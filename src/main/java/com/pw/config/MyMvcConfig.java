package com.pw.config;

import com.pw.service.AccountService;
import com.pw.service.AccountServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 武祥市
 */
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("main");
    }

    @Bean
    public HandlerInterceptor getLoginInterceptor(){
        return new LoginInterceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AccountPersonalInterceptor()).addPathPatterns("/accountPersonal/*");
        registry.addInterceptor(new AdminInterceptor())
        .addPathPatterns("/account/goVip")
        .addPathPatterns("/account/goNoVip")
        .addPathPatterns("/account/goAccountFeedback");
        registry.addInterceptor(new LoginInterceptor())
        .addPathPatterns("/**")
        .excludePathPatterns("/accountPersonal/goOut")
        .excludePathPatterns("/main/rotations")
        ;
    }
}
