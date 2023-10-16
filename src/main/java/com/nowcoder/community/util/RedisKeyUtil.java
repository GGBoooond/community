package com.nowcoder.community.util;

/**
 * @author wxx
 * @version 1.0
 */
public class RedisKeyUtil {
    private final static String SPLIT =":";
    private final static String PREFIX_ENITITY_LIKE="like:enitity";

    public static String getEnitityLikeKey(int enitity_type,int enitity_id){
        return PREFIX_ENITITY_LIKE + SPLIT +enitity_type + SPLIT +enitity_id;
    }
}
