package com.nowcoder.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.MessageService;
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
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author wxx
 * @version 1.0
 */
@Controller
public class MessageController implements CommunityConstant {
    @Resource
    private UserService userService;
    @Resource
    private MessageService messageService;
    @Resource
    private HostHolder hostHolder;

    //获得私信列表 --每个会话只展示最新的一条消息
    @LoginRequired
    @RequestMapping(value = "/letter/list",method = RequestMethod.GET)
    public String getLetterList(Model model, Page page){
        User user = hostHolder.getUser();
        //if(user==null)
        //    return "redirect:/login";
        //设置page
        page.setPath("/letter/list");
        page.setRows(messageService.selectConversationCount(user.getId()));
        page.setLimit(5);

        Map<String,Object> map=new HashMap<>();
        //未读总数量
        map.put("letterUnreadCount",messageService.selectLetterUnreadCount(user.getId(), null));
        //查询当前用户的会话列表，针对每个会话只返回最新的一条私信
        List<Message> conversations = messageService.selectConversations(user.getId(), page.getOffset(), page.getLimit());
        List<Map<String,Object>> conversationMap=new ArrayList<>();
        for(Message message:conversations){
            Map<String,Object> messageVO=new HashMap<>();
            messageVO.put("message",message);
            //此会话里的未读消息数量
            messageVO.put("unreadCount",
                    messageService.selectLetterUnreadCount(user.getId(), message.getConversation_id()));
            //私聊的对方id
            //因为不知道显示的消息的发送方是我方还是对方，所以进行判断
            int target_id=message.getFrom_id()== user.getId()? message.getTo_id():message.getFrom_id();
            User targetUser = userService.findUserByID(target_id);
            messageVO.put("targetUser",targetUser);
            messageVO.put("messageCount",messageService.selectLetterCount(message.getConversation_id()));
            conversationMap.add(messageVO);
        }
        map.put("conversationMap",conversationMap);
        model.addAttribute("map",map);
        model.addAttribute("allNoticeUnread",
                messageService.selectUnreadNoticeCount(user.getId(),null));
        return "/site/letter";
    }

    //获取这个会话的消息列表
    @RequestMapping(value = "/letter/detail/{conversationId}",method = RequestMethod.GET)
    public String getConversationDetail(@PathVariable("conversationId") String conversation_id
                            ,Model model,Page page){
        User user = hostHolder.getUser();

        page.setPath("/letter/detail/"+conversation_id);
        page.setRows(messageService.selectLetterCount(conversation_id));
        page.setLimit(5);

        Map<String,Object> letterMap=new HashMap<>();
        List<Message> messageList =
                messageService.selectLetters(conversation_id, page.getOffset(), page.getLimit());
        List<Map<String,Object>> letterVOmap=new ArrayList<>();
        for(Message message:messageList){
            Map<String,Object> letterVO=new HashMap<>();
            letterVO.put("fromUser",userService.findUserByID(message.getFrom_id()));
            letterVO.put("messageVO",message);
            letterVOmap.add(letterVO);
        }
        letterMap.put("target",userService.findUserByID(getTargetId(conversation_id,user.getId())));
        letterMap.put("letterVOmap",letterVOmap);
        model.addAttribute("map",letterMap);

        //对消息状态进行处理：如果有未读状态的改为已读
        updataMessgeStatus(messageList,1);

        return "/site/letter-detail";
    }
    private void updataMessgeStatus(List<Message> messageList,int status){
        User user = hostHolder.getUser();
        ArrayList<Integer> ids= new ArrayList<>();
        for(Message message:messageList){
            if(message.getStatus()==0 && message.getTo_id()==user.getId()){
                ids.add(message.getId());
            }
        }
        if(!ids.isEmpty()) {
            messageService.updateLetterStatus(ids, status);
        }
    }


    //发送私信
    @RequestMapping(value = "/letter/send",method = RequestMethod.POST)
    @ResponseBody
    public String sendLetter(String toName,String content){
        User target = userService.findUserByUsername(toName);
        if(target ==null)
            return CommunityUtil.getJsonString(1,"目标用户不存在");

        User from = hostHolder.getUser();
        Message message = new Message();
        message.setContent(content);
        message.setFrom_id(from.getId());
        message.setTo_id(target.getId());
        if(from.getId() <target.getId()) {
            message.setConversation_id(from.getId()+"_"+target.getId());
        }else {
            message.setConversation_id(target.getId()+"_"+ from.getId());
        }
        message.setCreate_time(new Date());
        messageService.insertLetter(message);

        return CommunityUtil.getJsonString(0);
    }

    private int getTargetId(String conversation_id,int userId){
        String[] s = conversation_id.split("_");
        int id_1=Integer.parseInt(s[0]);
        int id_2=Integer.parseInt(s[1]);
        return id_1==userId ? id_2:id_1;
    }
    @RequestMapping(path = "/notice/list",method = RequestMethod.GET)
    public String  getNoticeList(Model model){
        User user = hostHolder.getUser();

        //显示评论信息
        Map<String,Object> commentVO=new HashMap<>();
        Message commentNotice = messageService.selectLatestNotice(user.getId(), TOPIC_COMMENT);
        if(commentNotice!=null){
            commentVO.put("notice",commentNotice);

            String content= HtmlUtils.htmlUnescape(commentNotice.getContent());
            HashMap<String,Object> data = JSONObject.parseObject(content, HashMap.class);
            commentVO.put("user",userService.findUserByID((int)data.get("userId")));
            commentVO.put("entityType",data.get("entityType"));
            commentVO.put("entityId",data.get("entityId"));
            commentVO.put("postId",data.get("postId"));
            //未读数量和总共数量
            int allCount = messageService.selectAllNoticeCount(user.getId(), TOPIC_COMMENT);
            commentVO.put("allCount",allCount);
            int unreadCount = messageService.selectUnreadNoticeCount(user.getId(), TOPIC_COMMENT);
            commentVO.put("unreadCount",unreadCount);
        }else {
            commentVO.put("notice",null);
        }
        model.addAttribute("commentNotice",commentVO);

        //显示点赞信息
        Map<String,Object> likeVO=new HashMap<>();
        Message likeNotice = messageService.selectLatestNotice(user.getId(), TOPIC_LIKE);
        if(likeNotice!=null){
            likeVO.put("notice",likeNotice);

            String content= HtmlUtils.htmlUnescape(likeNotice.getContent());
            HashMap<String,Object> data = JSONObject.parseObject(content, HashMap.class);
            likeVO.put("user",userService.findUserByID((int)data.get("userId")));
            likeVO.put("entityType",data.get("entityType"));
            likeVO.put("entityId",data.get("entityId"));
            likeVO.put("postId",data.get("postId"));
            //未读数量和总共数量
            int allCount = messageService.selectAllNoticeCount(user.getId(), TOPIC_LIKE);
            likeVO.put("allCount",allCount);
            int unreadCount = messageService.selectUnreadNoticeCount(user.getId(), TOPIC_LIKE);
            likeVO.put("unreadCount",unreadCount);
        }else {
            likeVO.put("notice",null);
        }
        model.addAttribute("likeNotice",likeVO);

        //显示关注信息
        Map<String,Object> followVO=new HashMap<>();
        Message followNotice = messageService.selectLatestNotice(user.getId(), TOPIC_FOLLOW);
        if(followNotice!=null){
            followVO.put("notice",followNotice);

            String content= HtmlUtils.htmlUnescape(followNotice.getContent());
            HashMap<String,Object> data = JSONObject.parseObject(content, HashMap.class);
            followVO.put("user",userService.findUserByID((int)data.get("userId")));
            followVO.put("entityType",data.get("entityType"));
            followVO.put("entityId",data.get("entityId"));
            followVO.put("postId",data.get("postId"));
            //未读数量和总共数量
            int allCount = messageService.selectAllNoticeCount(user.getId(), TOPIC_FOLLOW);
            followVO.put("allCount",allCount);
            int unreadCount = messageService.selectUnreadNoticeCount(user.getId(), TOPIC_FOLLOW);
            followVO.put("unreadCount",unreadCount);
        }else {
            followVO.put("notice",null);
        }
        model.addAttribute("followNotice",followVO);
        //私信未读总数量
        model.addAttribute("letterUnreadCount",
                messageService.selectLetterUnreadCount(user.getId(), null));
        //通知未读总数量
        model.addAttribute("noticeUnreadCount",
                messageService.selectUnreadNoticeCount(user.getId(),null));
        return "/site/notice";
    }
    @RequestMapping(path = "/notice/detail/{topic}",method = RequestMethod.GET)
    public String getNoticeDetail(@PathVariable("topic")String topic, Model model,Page page){
        User user = hostHolder.getUser();

        page.setPath("/notice/detail/"+topic);
        page.setRows(messageService.selectAllNoticeCount(user.getId(),topic));
        page.setLimit(5);
        List<Map<String,Object>> noticeList=new ArrayList<>();
        List<Message> noticesList =
                messageService.selectNotices(user.getId(), topic, page.getOffset(), page.getLimit());
        for(Message notice:noticesList){
            Map<String,Object> noticeVO=new HashMap<>();
            noticeVO.put("fromUser",userService.findUserByID(notice.getFrom_id()));
            noticeVO.put("notice",notice);
            String content=HtmlUtils.htmlUnescape(notice.getContent());
            HashMap<String,Object> map = JSONObject.parseObject(content, HashMap.class);
            noticeVO.put("user",userService.findUserByID((int)map.get("userId")));
            noticeVO.put("entityType",(int)map.get("entityType"));
            noticeVO.put("postId",(int)map.get("postId"));
            noticeList.add(noticeVO);
        }
        model.addAttribute("notices",noticeList);
        model.addAttribute("topic",topic);

        //对通知的消息状态进行处理：如果有未读状态的改为已读
        updataMessgeStatus(noticesList,1);
        return "/site/notice-detail";
    }

}
