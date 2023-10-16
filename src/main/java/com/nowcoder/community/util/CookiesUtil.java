package com.nowcoder.community.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author wxx
 * @version 1.0
 */
public class CookiesUtil {
    //查询cookie
    public static String getCookie(HttpServletRequest request,String name){
        if(request==null ||name==null){
            throw new  IllegalArgumentException();
        }
        Cookie[] cookies = request.getCookies();
        if(cookies==null) return null;
        else {
            for(Cookie cookie:cookies){
                if(cookie.getName().equals(name)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
