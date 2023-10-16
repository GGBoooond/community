package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.service.CommentService;
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
public class CommentController {
    @Resource
    private HostHolder hostHolder;
    @Resource
    private CommentService commentService;
    @LoginRequired
    @RequestMapping(value = "/add/{discussPostID}",method = RequestMethod.POST)
    public String addComments(@PathVariable("discussPostID") int discussPostId,
                              Comment comment, Model model, Page page){
        comment.setUser_id(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreate_time(new Date());
        commentService.addComments(comment);

        return "redirect:/discuss/detail/"+discussPostId;
    }
}
