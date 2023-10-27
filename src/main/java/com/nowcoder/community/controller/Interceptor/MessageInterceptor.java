package com.nowcoder.community.controller.Interceptor;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.util.HostHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wxx
 * @version 1.0
 */
@Component
public class MessageInterceptor implements HandlerInterceptor {
    @Resource
    private HostHolder hostHolder;

    @Resource
    private MessageService messageService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if(user!=null && modelAndView!=null){
            int letterUnreadCount = messageService.selectLetterUnreadCount(user.getId(), null);
            int unreadNoticeCount = messageService.selectUnreadNoticeCount(user.getId(), null);
            modelAndView.addObject("allUnreadCount",letterUnreadCount+unreadNoticeCount);
        }
    }
}
