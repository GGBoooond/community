package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Event;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.event.EventComsumer;
import com.nowcoder.community.event.EventProducter;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.HostHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author wxx
 * @version 1.0
 */
@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {
    @Resource
    private HostHolder hostHolder;
    @Resource
    private CommentService commentService;
    @Resource
    private DiscussPostService discussPostService;
    @Resource
    private EventProducter eventProducter;

    @LoginRequired
    @RequestMapping(value = "/add/{discussPostID}",method = RequestMethod.POST)
    public String addComments(@PathVariable("discussPostID") int discussPostId,
                              Comment comment, Model model, Page page){
        comment.setUser_id(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreate_time(new Date());
        commentService.addComments(comment);

        //触发评论事件（系统通知）
        Event event=new Event()
                .setTopic(TOPIC_COMMENT)
                .setEntityId(comment.getEntity_id())
                .setEntityType(comment.getEntity_type())
                .setUserId(comment.getUser_id())
                .setData("postId",discussPostId);
        if(event.getEntityType()==ENTITY_TYPE_POST){
            DiscussPost target=discussPostService.selectDiscussPostByID(comment.getEntity_id());
            event.setEntityUserId(target.getUser_id());
        }else if(event.getEntityType()==ENTITY_TYPE_REPLY){
            Comment target=commentService.selectCommentById(comment.getEntity_id());
            event.setEntityUserId(target.getUser_id());
        }
        eventProducter.sendEvent(event);
        return "redirect:/discuss/detail/"+discussPostId;
    }
}
