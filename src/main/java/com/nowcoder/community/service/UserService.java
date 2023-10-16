package com.nowcoder.community.service;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;

import java.util.Map;

/**
 * @author wxx
 * @version 1.0
 */
public interface UserService {
    User findUserByID(int id);

    User findUserByEmail(String email);
    User findUserByUsername(String username);

    Map<String,Object> register(User user);

    int activation(int id);

    Map<String,Object> login(String username,String password,int expired_time);

    int logout(String ticket);

    LoginTicket selectByTicket(String ticket);

    int  updateHeader(int id, String headerUrl);
}
