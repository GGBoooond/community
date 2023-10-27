package com.nowcoder.community.config;

import com.nowcoder.community.controller.Interceptor.LoginRequiredInterceptor;
import com.nowcoder.community.controller.Interceptor.LoginTicketInterceptor;
import com.nowcoder.community.controller.Interceptor.MessageInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author wxx
 * @version 1.0
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer{
    @Resource
    private LoginTicketInterceptor loginTicketInterceptor;
    @Resource
    private LoginRequiredInterceptor loginRequiredInterceptor;
    @Resource
    private MessageInterceptor messageInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加拦截器
        registry.addInterceptor(loginTicketInterceptor)
                //设置拦截路径
                //出现了一个小错误 /**代表所有目录;/*代表当前目录
                //一开始 设置为 /*，结果在访问/user/upload时候出错了，原因就是因为设置路径不正确拦截器没起作用
                //改成/** 就好了
                .addPathPatterns("/**")
                //设置不拦截的路径
                .excludePathPatterns("/**/*.js","/**/*.css","/**/*.png","/**/*.jpg","/**/*.jpeg");
        registry.addInterceptor(loginRequiredInterceptor)
                .addPathPatterns("/**")
                .addPathPatterns("/**/*.js","/**/*.css","/**/*.png","/**/*.jpg","/**/*.jpeg");
        registry.addInterceptor(messageInterceptor)
                .addPathPatterns("/**")
                .addPathPatterns("/**/*.js","/**/*.css","/**/*.png","/**/*.jpg","/**/*.jpeg");
    }
}


