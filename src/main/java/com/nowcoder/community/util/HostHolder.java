package com.nowcoder.community.util;

import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * @author wxx
 * @version 1.0
 * 持有用户信息，用于代替session对象
 */
@Component
public class HostHolder {
    private ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public void setUser(User user){
        threadLocal.set(user);
    }
    public User getUser(){
        return  threadLocal.get();
    }
    public void clear(){
        threadLocal.remove();
    }
}
