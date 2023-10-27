package com.nowcoder.community.service;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.HostHolder;
import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author wxx
 * @version 1.0
 */
@Service
public class FollowService implements CommunityConstant{
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private UserService userService;

    @Resource
    private HostHolder hostHolder;

    //关注
    public void follow(int userId,int entityId,int entityType){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        String followerKey = RedisKeyUtil.getFollowerKey(entityId, entityType);

        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();

                operations.opsForZSet().add(followeeKey,entityId,System.currentTimeMillis());
                operations.opsForZSet().add(followerKey,userId,System.currentTimeMillis());

                return operations.exec();
            }
        });
    }

    //取消关注
    public void unfollow(int userId,int entityId,int entityType){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        String followerKey = RedisKeyUtil.getFollowerKey(entityId, entityType);

        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();

                operations.opsForZSet().remove(followeeKey,entityId);
                operations.opsForZSet().remove(followerKey,userId);

                return operations.exec();
            }
        });

    }

    //某用户关注人数
    public long followCount(int userId,int entityType){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        return  redisTemplate.opsForZSet().zCard(followeeKey);
    }
    //某用户的粉丝数量
    public long followerCount(int userId,int entityType){
        String followerKey = RedisKeyUtil.getFollowerKey(userId, entityType);
        return redisTemplate.opsForZSet().zCard(followerKey);
    }

    //判断是否关注
    public boolean isFollowed(int userId,int entityId,int entityType){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().score(followeeKey,entityId) !=null;
    }

    //获取某用户的关注人群信息
    public List<Map<String,Object>> getFollowee(int userId,int entityType,int offset,int limit){
        String followeeKey=RedisKeyUtil.getFolloweeKey(userId,entityType);
        Set<Integer> targetIds =
                redisTemplate.opsForZSet().reverseRange(followeeKey, offset, offset + limit);
        if(targetIds==null) {
            return null;
        }
        List<Map<String,Object>> targetUsers=new ArrayList<>();
        for(int id:targetIds){
            Map<String,Object> userVO=new HashMap<>();
            userVO.put("targetUser",userService.findUserByID(id));
            Double score = redisTemplate.opsForZSet().score(followeeKey, id);
            userVO.put("followTime",new Date(score.longValue()));

            User loginUser = hostHolder.getUser();
            if(loginUser==null){
                userVO.put("hasFollowed",false);
            }else {
                userVO.put("hasFollowed",isFollowed(loginUser.getId(),id,ENTITY_TYPE_USER));
            }

            targetUsers.add(userVO);
        }
        return targetUsers;
    }

    //获取某用户的粉丝人群信息
    public List<Map<String,Object>> getFollower(int userId,int entityType,int offset,int limit){
        String followerKey=RedisKeyUtil.getFollowerKey(userId,entityType);
        Set<Integer> targetIds =
                redisTemplate.opsForZSet().reverseRange(followerKey, offset, offset + limit);
        if(targetIds==null) {
            return null;
        }
        List<Map<String,Object>> targetUsers=new ArrayList<>();
        for(int id:targetIds){
            Map<String,Object> userVO=new HashMap<>();
            userVO.put("targetUser",userService.findUserByID(id));
            Double score = redisTemplate.opsForZSet().score(followerKey, id);
            userVO.put("followTime",new Date(score.longValue()));

            User loginUser = hostHolder.getUser();
            if(loginUser==null){
                userVO.put("hasFollowed",false);
            }else {
                userVO.put("hasFollowed",isFollowed(loginUser.getId(),id,ENTITY_TYPE_USER));
            }
            targetUsers.add(userVO);
        }
        return targetUsers;
    }
}
