package com.nowcoder.community.controller.Interceptor;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CookiesUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author wxx
 * @version 1.0
 * 进行登录凭证的检测
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    @Resource
    private HostHolder hostHolder;
    @Resource
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取凭证
        String ticket = CookiesUtil.getCookie(request, "ticket");
        if(ticket!=null){
            //根据凭证查询用户
            LoginTicket loginTicket = userService.selectByTicket(ticket);
            //验证凭证有效性
            if(loginTicket!=null && loginTicket.getStatus()==1 &&loginTicket.getExpired().after(new Date())){
                User user = userService.findUserByID(loginTicket.getUser_id());
                hostHolder.setUser(user);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if(user!=null && modelAndView!=null){
            modelAndView.addObject("loginUser",user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
