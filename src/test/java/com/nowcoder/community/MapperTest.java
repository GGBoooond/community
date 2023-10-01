package com.nowcoder.community;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

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
        User user= userMapper.findUserByID(25);
        log.info("数量为"+user);
    }
}
