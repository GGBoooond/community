package com.nowcoder.community.util;

import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author wxx
 * @version 1.0
 */
public class CommunityUtil {
    //生成随机字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
    //进行MD5加密
    public static String MD5(String key){
        if(StringUtils.isNullOrEmpty(key)){
            return null;
        }else {
            return DigestUtils.md5DigestAsHex(key.getBytes());
        }
    }

    public static String getJsonString(int code, String msg, Map<String,Object> map){
        JSONObject json= new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        if(map!=null){
            for(String key:map.keySet()){
                json.put(key,map.get(key));
            }
        }
        return json.toJSONString();
    }
    public static String getJsonString(int code, String msg){
        return getJsonString(code,msg, null);
    }
    public static String getJsonString(int code){
        return getJsonString(code,null, null);
    }
}
