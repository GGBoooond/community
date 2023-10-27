package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Event;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.event.EventProducter;
import com.nowcoder.community.service.FollowService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wxx
 * @version 1.0
 */
@Controller
public class LikeController implements CommunityConstant {
    @Resource
    private HostHolder hostHolder;
    @Resource
    private LikeService likeService;

    @Resource
    private EventProducter eventProducter;

    @RequestMapping(value = "/like",method = RequestMethod.POST)
    @ResponseBody
    public String like(int entity_id, int entity_type,int entity_user,int postId){
        User user = hostHolder.getUser();
        likeService.like(user.getId(), entity_type,entity_id,entity_user);
        Map<String,Object> map=new HashMap<>();
        int count =(int) likeService.likeCount(entity_type, entity_id);
        map.put("likeCount",count);
        int status = likeService.likeStatus(user.getId(), entity_type, entity_id);
        map.put("likeStatus",status);

        if(status==1){
            //触发点赞事件（系统通知）
            Event event=new Event()
                    .setTopic(TOPIC_LIKE)
                    .setEntityId(entity_id)
                    .setEntityType(entity_type)
                    .setUserId(user.getId())
                    .setData("postId",postId)
                    .setEntityUserId(entity_user);

            eventProducter.sendEvent(event);
        }
        return CommunityUtil.getJsonString(0,null,map);
    }
}
