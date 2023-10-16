package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author wxx
 * @version 1.0
 */
@Slf4j
@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {
    @Resource
    private DiscussPostService discussPostService;
    @Resource
    private UserService userService;
    @Resource
    private HostHolder hostHolder;
    @Resource
    private CommentService commentService;

    @Resource
    private LikeService likeService;

    @RequestMapping("/add")
    @ResponseBody
    public String addDiscussPost(String title,String content){
        User user = hostHolder.getUser();
        if(user==null)
            return CommunityUtil.getJsonString(403,"你还没有登录哦");

        DiscussPost post = new DiscussPost();
        post.setTitle(title);
        post.setContent(content);
        post.setUser_id(user.getId());
        post.setCreate_time(new Date());
        //其它错误会在后面统一处理
        discussPostService.addDiscussPost(post);
        return  CommunityUtil.getJsonString(0,"成功发送");
    }
    @RequestMapping(path = "/detail/{discussPostID}" ,method = RequestMethod.GET)
    public String discussPostDetail(@PathVariable("discussPostID") int discussPostID,
                                    Model model, Page page){
        //用户
        User user = hostHolder.getUser();
        HashMap<String, Object> map = new HashMap<>();
        //帖子
        DiscussPost discussPost = discussPostService.selectDiscussPostByID(discussPostID);
        map.put("post",discussPost);
        //作者
        User writer= userService.findUserByID(discussPost.getUser_id());
        map.put("writer",writer);
        //获取赞数
        int likeCount=(int) likeService.likeCount(1,discussPost.getId());
        map.put("likeCount",likeCount);
        //获取点赞状态
        int status=
                user==null?0:likeService.likeStatus(user.getId(),1,discussPost.getId());
        map.put("likeStatus",status);

        model.addAttribute("map",map);

        //评论分页信息
        page.setRows(discussPost.getComment_count());
        page.setLimit(5);
        page.setPath("/discuss/detail/"+discussPostID);
        //获取评论
        List<Comment> comments =
                commentService.findCommentsByEntity(ENTITY_TYPE_POST,discussPostID, page.getOffset(), page.getLimit());
        //设置评论VO列表
        List<Map<String,Object>> commentVOList=new ArrayList<>();
        if(comments!=null){
            for(Comment comment:comments){
                //设置评论VO
                Map<String,Object> commentVO=new HashMap<>();
                //评论
                commentVO.put("comment",comment);
                //作者
                commentVO.put("user",userService.findUserByID(comment.getUser_id()));
                //获取赞数
                likeCount=(int)likeService.likeCount(2,comment.getId());
                commentVO.put("likeCount",likeCount);
                //获取点赞状态
                status=
                        user==null? 0 : likeService.likeStatus(user.getId(),2,comment.getId());
                commentVO.put("likeStatus",status);

                //回复列表
                List<Comment> replys=
                        commentService.findCommentsByEntity(ENTITY_TYPE_REPLY,comment.getId(),0,Integer.MAX_VALUE);
                    List<Map<String,Object>> replyList=new ArrayList<>();
                    for(Comment comment_reply:replys){
                        //设置评论VO
                        Map<String,Object> replyCommentVO=new HashMap<>();
                        //回复
                        replyCommentVO.put("reply",comment_reply);
                        //回复对象
                        User target=comment_reply.getTarget_id()==0?null:userService.findUserByID(comment_reply.getTarget_id());
                        replyCommentVO.put("target_id",target);
                        //作者
                        replyCommentVO.put("user",userService.findUserByID(comment_reply.getUser_id()));
                        //获取赞数
                        likeCount=(int)likeService.likeCount(2,comment_reply.getId());
                        replyCommentVO.put("likeCount",likeCount);
                        //获取点赞状态
                        status=
                                user==null? 0 : likeService.likeStatus(user.getId(),2,comment_reply.getId());
                        replyCommentVO.put("likeStatus",status);


                        replyList.add(replyCommentVO);
                    }

                //回复数量
                commentVO.put("replyCount",commentService.countCommentReply(comment.getId()));
                commentVO.put("replyList",replyList);
                //加入
                commentVOList.add(commentVO);
            }
        }
        model.addAttribute("commentVOList",commentVOList);
        return  "/site/discuss-detail";
    }
}
