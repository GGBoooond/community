package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.service.ElasticSearchService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.management.StandardEmitterMBean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wxx
 * @version 1.0
 */
@Controller
public class ElasticSearchController implements CommunityConstant {
    @Resource
    private ElasticSearchService elasticSearchService;

    @Resource
    private UserService userService;
    @Resource
    private LikeService likeService;

    //search?keyword=xxx
    @RequestMapping(path = "/search",method = RequestMethod.GET)
    public String search(String keyword, Page page, Model model) throws IOException {
        page.setPath("/search?keyword="+keyword);
        page.setRows(elasticSearchService.searchDiscussPost(keyword,0,1000).size());
        List<DiscussPost> list =
                elasticSearchService.searchDiscussPost(keyword, page.getOffset(), page.getLimit());

        List<Map<String,Object>> resList=new ArrayList<>();
        for(DiscussPost discussPost:list){
            Map<String,Object> postVO=new HashMap<>();
            postVO.put("post",discussPost);
            postVO.put("user",userService.findUserByID(discussPost.getUser_id()));
            postVO.put("likeCount",likeService.likeCount(ENTITY_TYPE_POST,discussPost.getId()));

            resList.add(postVO);
        }

        model.addAttribute("discussPosts",resList);

        return "/site/search";
    }
}
