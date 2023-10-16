package com.nowcoder.community;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
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
public class RedisTest {
    @Resource
    private RedisTemplate redisTemplate;
    @Test
    public void stringTest(){
        String key="test:name";
        redisTemplate.opsForValue().set(key,"wuxiangxi");
        System.out.println(redisTemplate.opsForValue().get(key));
    }
}
