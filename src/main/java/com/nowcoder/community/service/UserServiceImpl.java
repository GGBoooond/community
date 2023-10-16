package com.nowcoder.community.service;

import com.mysql.cj.util.StringUtils;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.LoginTicketMapper;
import com.nowcoder.community.mapper.UserMapper;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author wxx
 * @version 1.0
 */
@Service
public class UserServiceImpl implements UserService, CommunityConstant {
    @Resource
    private UserMapper userMapper;

    @Resource
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;
    //域名
    @Value("${community.path.domain}")
    private String domain;
    //工程路径
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Override
    public User findUserByID(int id) {
        return userMapper.findUserByID(id);
    }

    @Override
    public User findUserByEmail(String email) {
        return userMapper.findUserByEmail(email);
    }
    @Override
    public User findUserByUsername(String username) {
        return userMapper.findUserByUsername(username);
    }

    @Override
    public Map<String, Object> register(User user) {
        HashMap<String, Object> map = new HashMap<>();
        //空值处理
        if(user==null) {
            throw new IllegalArgumentException("参数不能为空！");
        }
        if(StringUtils.isNullOrEmpty(user.getUsername())){
            map.put("usernameMsg","用户名不能为空");
            return map;
        }
        if(StringUtils.isNullOrEmpty(user.getPassword())){
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        if(StringUtils.isNullOrEmpty(user.getEmail())){
            map.put("emailMsg","邮箱不能为空");
            return map;
        }
        //验证账号
        if(userMapper.findUserByUsername(user.getUsername())!=null){
            map.put("usernameMsg","该用户名已经存在");
            return map;
        }
        //验证邮箱
        if(userMapper.findUserByEmail(user.getEmail())!=null){
            map.put("emailMsg","该邮箱已经存在");
            return map;
        }
        //注册用户
        user.setSalt(CommunityUtil.generateUUID());
        user.setPassword(CommunityUtil.MD5(user.getPassword()+user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivation_code(CommunityUtil.generateUUID());
        user.setCreate_time(new Date());
        user.setHeader_url(String.format("http://images.nowcoder.com/head/%dt.png",
                new Random().nextInt(1000)));
        userMapper.insertUser(user);
        //激活邮件
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        String url="http://localhost:8080/community/activation/"+
                userMapper.findUserByUsername(user.getUsername()).getId()
                +"/"+user.getActivation_code();
        context.setVariable("url",url);
        String process = templateEngine.process("mail/activation", context);
        mailClient.sendMail("2563806166@qq.com","激活账号",process);
        return map;
    }
    //激活
    @Override
    public int activation(int id) {
        User user = findUserByID(id);
        if(user.getStatus()==0){
            userMapper.updateStatus(id,1);
            return ACTIVATION_SUCCESS;
        }else if (user.getStatus()==1){
            return ACTIVATION_REPEAT;
        }else {
            return ACTIVATION_FAILURE;
        }
    }
    //登录
    @Override
    public Map<String,Object> login(String username,String password,int expired_second){
        HashMap<String, Object> map = new HashMap<>();
        //空值处理
        if(username==null){
            map.put("usernameMsg","账号不能为空");
            return map;
        }
        if(password==null){
            map.put("passwordMsg","密码不能为空");
            return map;

        }

        //验证用户
        User user = userMapper.findUserByUsername(username);
        if(user==null){
            map.put("usernameMsg","账号不存在");
            return map;
        }else {
            //验证密码
            password=CommunityUtil.MD5(password+user.getSalt());
            if(!password.equals(user.getPassword())){
                map.put("passwordMsg","密码错误");
                return map;
            }
        }

        //验证成功，创建ticket（登录凭证）
        LoginTicket loginTicket = new LoginTicket();
        String ticket=CommunityUtil.generateUUID();
        loginTicket.setTicket(ticket);
        loginTicket.setUser_id(user.getId());
        loginTicket.setStatus(1);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+expired_second*1000));
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket",ticket);
        return map;
    }

    @Override
    public int logout(String ticket) {
        return loginTicketMapper.updataStatus(ticket,0);
    }

    @Override
    public LoginTicket selectByTicket(String ticket) {
        return  loginTicketMapper.selectByTicket(ticket);
    }

    @Override
    public int updateHeader(int id, String headerUrl) {
        return userMapper.updateHeader(id,headerUrl);
    }

}
