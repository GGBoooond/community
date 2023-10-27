package com.nowcoder.community.service;

import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
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

    /**
     *
     * @param userId
     * @param entity_type  点赞的类型：1 给帖子点赞  2  给用户评论点赞
     * @param entity_id
     * @param entity_user
     */

    //编程式事务
    //点赞
    public void like(int userId,int entity_type,int entity_id,int entity_user){
        //String likeKey= RedisKeyUtil.getEnitityLikeKey(entity_type,entity_id);
        //if(redisTemplate.opsForSet().isMember(likeKey,userId)){
        //    redisTemplate.opsForSet().remove(likeKey,userId);
        //}else {
        //    redisTemplate.opsForSet().add(likeKey,userId);
        //}
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String likeKey= RedisKeyUtil.getEnitityLikeKey(entity_type,entity_id);
                String userLikeKey=RedisKeyUtil.getUserLikeKey(entity_user);
                boolean isMember=redisTemplate.opsForSet().isMember(likeKey,userId);
                //注意：查询要在事务前处理，不然不能及时返回结果（事务的处理机制）
                //开始事务
                operations.multi();
                if(isMember){
                    operations.opsForSet().remove(likeKey,userId);
                    operations.opsForValue().decrement(userLikeKey);
                }else {
                    operations.opsForSet().add(likeKey,userId);
                    operations.opsForValue().increment(userLikeKey);
                }
                //执行事务
                return operations.exec();
            }
        });
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
    //查询某个用户收到的点赞数量
    public int UserlikeCountSum(int userId){
        String userLikeKey=RedisKeyUtil.getUserLikeKey(userId);
        Integer count=(Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count==null ? 0:count;
    }
}
