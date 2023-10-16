package com.nowcoder.community.controller.advice;

import com.nowcoder.community.util.CommunityUtil;
import jdk.jfr.Experimental;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author wxx
 * @version 1.0
 */
@ControllerAdvice(annotations = Controller.class)
@Slf4j
public class ExceptionAdvice {
    @ExceptionHandler(NullPointerException.class)
    public void handlerException(Exception e ,
                                 HttpServletRequest request,HttpServletResponse response) throws IOException {
        log.error("服务器发生错误"+e.getMessage());
        for(StackTraceElement element:e.getStackTrace()){
            log.error(element.toString());
        }
        //根据是普通请求还是异步请求
        //分别处理
        String xRequestWith= request.getHeader("x-requested-with");
        if("XMLHttpRequest".equals(xRequestWith)){
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(CommunityUtil.getJsonString(1,"服务器发生错误"));
        }else {
            response.sendRedirect(request.getContextPath()+"/error");
        }
    }
}
