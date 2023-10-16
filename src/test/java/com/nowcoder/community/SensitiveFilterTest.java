package com.nowcoder.community;

import com.nowcoder.community.util.SensitiveFilter;
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
public class SensitiveFilterTest {
    @Resource
    public SensitiveFilter sensitiveFilter;
    @Test
    public void SensitiveFiltertest(){
        String text="草，我去这边，尼玛，碰到我操了";
        String res = sensitiveFilter.filter(text);
        System.out.println(res);
    }
}
