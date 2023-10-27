package com.nowcoder.community.controller;

import com.google.code.kaptcha.Producer;
import com.mysql.cj.util.StringUtils;
import com.nowcoder.community.config.KaptchaConfig;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author wxx
 * @version 1.0
 */
@Slf4j
@Controller
public class LoginController implements CommunityConstant {
    @Autowired
    private UserService userService;
    @Autowired
    private Producer kaptchaProducer;
    @Resource
    private RedisTemplate redisTemplate;

    //工程路径
    @Value("${server.servlet.context-path}")
    private String contextPath;

    //获取注册页面
    @RequestMapping(path = "/register",method = RequestMethod.GET)
    public String getRegisterPage(){
        return "/site/register";
    }

    //获取登录页面
    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String getLoginPage(){
        return "/site/login";
    }
    //注册提交
    @RequestMapping(path = "/register",method = RequestMethod.POST)
    public String register(Model model, User user) {
        Map<String, Object> map =userService.register(user);
        if (map==null||map.isEmpty()) {
            model.addAttribute("msg","邮件已经发送，等待激活中");
            model.addAttribute("url","/index");
            return "site/operate-result";
        }else {
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));
            return "site/register";
        }
    }
    //激活邮件的验证
    @RequestMapping(path = "/activation/{userId}/{code}",method = RequestMethod.GET)
    public String activation(Model model,@PathVariable(name = "userId")int userId,
                             @PathVariable(name= "code")String code){
        int activation_status = userService.activation(userId);
        if(activation_status==ACTIVATION_SUCCESS){

            model.addAttribute("msg","恭喜，激活成功！下面跳转到登录界面");
            model.addAttribute("url","/login");
        }else if (activation_status==ACTIVATION_REPEAT){
            model.addAttribute("msg","该账号已经被激活！");
            model.addAttribute("url","/index");
        }else {
            model.addAttribute("msg","激活失败！");
            model.addAttribute("url","/index");
        }
        return "site/operate-result";
    }
    @RequestMapping(path = "/kaptcha",method = RequestMethod.GET)
    public void getKaptcha(HttpServletRequest request, HttpServletResponse response){
        //生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        ////将验证码放入session域中
        //request.getSession().setAttribute("kaptcha",text);

        //并设置一个临时凭证给cookies用来判别用户
        String tempOwner = CommunityUtil.generateUUID();
        Cookie cookie=new Cookie("kaptchaOwner",tempOwner);
        cookie.setMaxAge(60);
        cookie.setPath(contextPath);
        response.addCookie(cookie);
        //将验证码存入redis中
        String redisKaptchaKey= RedisKeyUtil.getkaptchaKey(tempOwner);
        redisTemplate.opsForValue().set(redisKaptchaKey,text,60, TimeUnit.SECONDS);


        //输出验证码对应的图片
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image,"png",os);
        } catch (IOException e) {
            log.error("响应验证码失败"+e.getMessage());
        }
    }

    @RequestMapping(path = "/login",method = RequestMethod.POST)
    public String login(String username,String password,String code,boolean rememberme,
            Model model,HttpServletRequest request, HttpServletResponse response,
    @CookieValue("kaptchaOwner")String kaptchaOwner){
        //验证码check
        //String kaptcha =(String) request.getSession().getAttribute("kaptcha");
        String kaptcha=null;
        if(!StringUtils.isNullOrEmpty(kaptchaOwner)){
            kaptcha =(String) redisTemplate.opsForValue().
                            get(RedisKeyUtil.getkaptchaKey(kaptchaOwner));
        }
        if(code==null || StringUtils.isNullOrEmpty(kaptcha) || !(code.equalsIgnoreCase(kaptcha))){
            model.addAttribute("kaptchaMsg","验证码错误");
            return "site/login";
        }
        //验证登录凭证
        int expiredSeconds=rememberme?REMEMBER_EXPIRED_SECONDS:DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);

        if(map.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        }else {
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "site/login";
        }
    }

    @RequestMapping(path = "/logout",method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket,
                         HttpServletResponse response){
        userService.logout(ticket);
        return "redirect:/login";
    }


}
