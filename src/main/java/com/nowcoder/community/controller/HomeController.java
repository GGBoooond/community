package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wxx
 * @version 1.0
 */
@Slf4j
@Controller
public class HomeController {

    @Resource
    private UserService userService;
    @Resource
    private DiscussPostService discussPostService;
    @Resource
    private LikeService likeService;
    @RequestMapping(path= "/index",method = RequestMethod.GET)
    public String getIndexPage(Model model,Page page){
        page.setPath("/index");
        page.setRows(discussPostService.selectDiscussPostRows(0));
        //page.setCurrent(current);
        log.info("left"+page.getLeft()+"    right"+page.getRight());
        ArrayList<Map<String,Object>> discussPosts= new ArrayList<>();
        List<DiscussPost> list =
                discussPostService.selectDiscussPosts(0, page.getOffset(), page.getLimit());
        for(DiscussPost discussPost:list){
            HashMap<String, Object> map = new HashMap<>();
            map.put("post",discussPost);
            User user = userService.findUserByID(discussPost.getUser_id());
            map.put("user",user);
            discussPosts.add(map);
            //获取赞数
            long likeCount=likeService.likeCount(1,discussPost.getId());
            map.put("likeCount",likeCount);
        }
        model.addAttribute("discussPosts",discussPosts);
        return "index";
    }


}
