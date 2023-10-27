package com.nowcoder.community;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.junit.runner.OrderWith;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
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
public class KafkaTest {
    @Resource
    private KafkaProducter kafkaProducter;


    @Test
    public void testKafka() throws InterruptedException {
        kafkaProducter.sendMessage("test","你好");
        kafkaProducter.sendMessage("test","hello");
        Thread.sleep(1000*10);
    }
}


@Component
class KafkaProducter{
    @Resource
    private KafkaTemplate kafkaTemplate;

    public void sendMessage(String topic,String content){
        kafkaTemplate.send(topic,content);
    }
}

@Component
class KafkaConsumer{
    @KafkaListener(topics ={"test"})
    public void handleMessage(ConsumerRecord consumerRecord){
        System.out.println(consumerRecord.value());
    }
}