package com.nowcoder.community.controller;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.LikeService;
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
public class LikeController {
    @Resource
    private HostHolder hostHolder;
    @Resource
    private LikeService likeService;

    @RequestMapping(value = "/like",method = RequestMethod.POST)
    @ResponseBody
    public String like(int entity_id, int entity_type){
        User user = hostHolder.getUser();
        likeService.like(user.getId(), entity_type,entity_id);
        Map<String,Object> map=new HashMap<>();
        int count =(int) likeService.likeCount(entity_type, entity_id);
        map.put("likeCount",count);
        int status = likeService.likeStatus(user.getId(), entity_type, entity_id);
        map.put("likeStatus",status);

        return CommunityUtil.getJsonString(0,null,map);
    }
}
