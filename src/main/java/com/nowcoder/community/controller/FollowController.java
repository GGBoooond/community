package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Event;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.event.EventProducter;
import com.nowcoder.community.service.FollowService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wxx
 * @version 1.0
 */
@Controller
public class FollowController implements CommunityConstant {
    @Resource
    private FollowService followService;
    @Resource
    private UserService userService;
    @Resource
    private HostHolder hostHolder;
    @Resource
    private EventProducter eventProducter;

    @RequestMapping(value = "/follow",method = RequestMethod.POST)
    @ResponseBody
    public String follow(int entityType,int entityId){
        User user = hostHolder.getUser();
        if(user==null)
            throw new RuntimeException("未登录");
        followService.follow(user.getId(),entityId,entityType);

        //触发关注事件（系统通知）
        Event event=new Event()
                .setTopic(TOPIC_FOLLOW)
                .setEntityId(entityId)
                .setEntityType(entityType)
                .setUserId(user.getId())
                .setEntityUserId(entityId);

        eventProducter.sendEvent(event);

        return CommunityUtil.getJsonString(0,"已关注");
    }

    @RequestMapping(value = "/unfollow",method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(int entityType,int entityId){
        User user = hostHolder.getUser();
        if(user==null)
            throw new RuntimeException("未登录");
        followService.unfollow(user.getId(),entityId,entityType);
        return CommunityUtil.getJsonString(0,"已取消关注");
    }

    @RequestMapping(value = "/getFollowees/{userId}",method = RequestMethod.GET)
    public String getFollowee(Model model, Page page, @PathVariable("userId") int userId){
        page.setPath("/getFollowee/"+userId);
        page.setRows((int) followService.followCount(userId,ENTITY_TYPE_USER));
        page.setLimit(5);

        Map<String,Object> map=new HashMap<>();

        List<Map<String, Object>> followeeList =
                followService.getFollowee(userId, ENTITY_TYPE_USER, page.getOffset(), page.getLimit());
        map.put("followeeList",followeeList);
        map.put("user",userService.findUserByID(userId));

        model.addAttribute("maps",map);
        model.addAttribute("loginUser",hostHolder.getUser());
        return "site/followee";
    }

    @RequestMapping(value = "/getFollowers/{userId}",method = RequestMethod.GET)
    public String getFollower(Model model, Page page, @PathVariable("userId") int userId){
        page.setPath("/getFollower/"+userId);
        page.setRows((int) followService.followerCount(userId,ENTITY_TYPE_USER));
        page.setLimit(5);

        Map<String,Object> map=new HashMap<>();

        List<Map<String, Object>> followerList =
                followService.getFollower(userId, ENTITY_TYPE_USER, page.getOffset(), page.getLimit());
        map.put("followerList",followerList);
        map.put("user",userService.findUserByID(userId));

        model.addAttribute("maps",map);
        model.addAttribute("loginUser",hostHolder.getUser());
        return "site/follower";
    }
}
