package com.nowcoder.community.util;

/**
 * @author wxx
 * @version 1.0
 */
public class RedisKeyUtil {
    private final static String SPLIT =":";
    private final static String PREFIX_ENITITY_LIKE="like:enitity";
    private final static String PREFIX_USER_LIKE="like:user";
    private final static String FOLLOWER="follower";
    private final static String FOLLOWEE="followee";
    private final static String KAPTCHA="kaptcha";
    private final static String LOGINTIKET="loginTicket";
    private final static String USER="user";
    private final static String CACHE="cache";


    public static String getEnitityLikeKey(int enitity_type,int enitity_id){
        return PREFIX_ENITITY_LIKE + SPLIT +enitity_type + SPLIT +enitity_id;
    }
    public static String getUserLikeKey(int enitity_user){
        return PREFIX_USER_LIKE + SPLIT +enitity_user;
    }

    //某用户关注的实体
    //followee:id:type ->zset(entityId,curentTime)
    public static String getFolloweeKey(int userId,int entityType){
        return FOLLOWEE+SPLIT+userId+SPLIT+entityType;
    }
    //某用户拥有的粉丝
    //followee:type:id ->zset(entityId,curentTime)
    public static String getFollowerKey(int userId,int entityType){
        return FOLLOWER+SPLIT+entityType+SPLIT+userId;
    }

    public static String getkaptchaKey(String owner){
        return KAPTCHA+SPLIT+owner;
    }
    public static String loginTicketKey(String ticket){
        return LOGINTIKET+SPLIT+ticket;
    }

    public static String getUserCacheKey(int userId){
        return CACHE+SPLIT+USER+SPLIT+userId;
    }
}
