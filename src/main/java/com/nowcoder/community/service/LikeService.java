package com.nowcoder.community.service;

import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wxx
 * @version 1.0
 */
@Service
public class LikeService {
    @Resource
    private RedisTemplate redisTemplate;

    //点赞
    public void like(int userId,int entity_type,int entity_id){
        String likeKey= RedisKeyUtil.getEnitityLikeKey(entity_type,entity_id);
        if(redisTemplate.opsForSet().isMember(likeKey,userId)){
            redisTemplate.opsForSet().remove(likeKey,userId);
        }else {
            redisTemplate.opsForSet().add(likeKey,userId);
        }
    }
    //得到点赞数量
    public long likeCount(int entity_type,int entity_id){
        String likeKey= RedisKeyUtil.getEnitityLikeKey(entity_type,entity_id);
        return redisTemplate.opsForSet().size(likeKey);
    }
    //显示点赞状态
    public int likeStatus(int userId,int entity_type,int entity_id){
        String likeKey= RedisKeyUtil.getEnitityLikeKey(entity_type,entity_id);
        return redisTemplate.opsForSet().isMember(likeKey,userId) ? 1:0;
    }
}
