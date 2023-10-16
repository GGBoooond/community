package com.nowcoder.community;

import com.nowcoder.community.entity.*;
import com.nowcoder.community.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author wxx
 * @version 1.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes=CommunityApplication.class)
public class MapperTest {
    @Resource
    private DiscussPostMapper discussPostMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private LoginTicketMapper loginTicketMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private MessageMapper messageMapper;
    @Test
    public void discussPostTest(){
        discussPostMapper.selectDiscussPosts(0,0,10);
    }
    @Test
    public void discussPostTest1(){
        int i=discussPostMapper.selectDiscussPostRows(0);
        log.info("数量为"+i);
    }
    @Test
    public void UserMapperTest1(){
        User user= userMapper.findUserByID(0);
        if(user==null) System.out.println("================为空");
        log.info("数量为"+user);
    }
    @Test
    public void insertLoginTicketTest(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUser_id(1001);
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date());
        loginTicket.setTicket("wxxxx");
        loginTicketMapper.insertLoginTicket(loginTicket);
    }
    @Test
    public void selectByTicketTest(){
        LoginTicket res = loginTicketMapper.selectByTicket("wxxxx");
        log.info("res===="+res);
    }
    @Test
    public void updataStatusTest(){
        int res = loginTicketMapper.updataStatus("wxxxx", 1);
        log.info("res==="+res);
    }
    @Test
    public void discussPostTest2(){
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUser_id(149);
        discussPost.setTitle("hhhh");
        discussPost.setContent("ssssss");
        discussPost.setCreate_time(new Date());
        System.out.println(discussPost);
        int i=discussPostMapper.insertDiscussPost(discussPost);
        log.info("数量为"+i);
    }
    @Test
    public void discussPostTest3() {
        System.out.println(discussPostMapper.selectDiscussPostByID(287));
    }
    @Test
    public void commentTest() {
        System.out.println(commentMapper.findCommentsByEntity(1,234,0,5).toString());
    }
    @Test
    public void commentTest1(){
        Comment comment = new Comment();
        comment.setUser_id(149);
        comment.setCreate_time(new Date());
        commentMapper.insertComment(comment);
    }
    @Test
    public void discussPostTest4(){
        discussPostMapper.updateComment_Count(281,1);
    }
    @Test
    public void messageMapperTest(){
        System.out.println(messageMapper.selectLetterCount("111_112"));
        System.out.println(messageMapper.selectLetters("111_112",0,5));
        System.out.println(messageMapper.selectConversationCount(111));
        System.out.println(messageMapper.selectConversations(111,0,5));
        System.out.println(messageMapper.selectLetterUnreadCount(111,null));
    }

    @Test
    public void messageMapperTest1(){
        Message message = new Message();
        message.setContent("test");
        message.setConversation_id("test");
        System.out.println(messageMapper.insertLetter(message));
        ArrayList<Integer> list = new ArrayList<>();
        list.add(355);
        System.out.println(messageMapper.updateLetterStatus(list,1));
    }
}
