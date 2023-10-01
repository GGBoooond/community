package com.nowcoder.community.controller;

import com.nowcoder.community.service.AlphaSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wxx
 * @version 1.0
 */
@RestController
public class HelloController {
    @Autowired
    private AlphaSerivce alphaSerivce;
    @RequestMapping("/hello")
    public String hello(){
       return  "hello";
    }
    @RequestMapping("/hi")
    public String hi(){
        return alphaSerivce.say();
    }
}
