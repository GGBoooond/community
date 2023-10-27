package com.nowcoder.community.event;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.entity.Event;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.util.CommunityConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wxx
 * @version 1.0
 */
@Slf4j
@Component
public class EventComsumer implements CommunityConstant {

    @Resource
    private MessageService messageService;

    @KafkaListener(topics = {TOPIC_COMMENT,TOPIC_LIKE,TOPIC_FOLLOW})
    public void handleEvent(ConsumerRecord record){
        if(record==null || record.value()==null){
            log.error("消息不能为空");
            return;
        }
        Event event= JSONObject.parseObject(record.value().toString(),Event.class);
        if(event==null){
            log.error("消息格式错误");
        }

        Message message = new Message();
        message.setFrom_id(SYSTEM_USER);
        message.setTo_id(event.getEntityUserId());
        message.setStatus(0);
        message.setConversation_id(event.getTopic());
        message.setCreate_time(new Date());
        //内容
        Map<String,Object> content=new HashMap<>();
        content.put("userId",event.getUserId());
        content.put("entityType",event.getEntityType());
        content.put("entityId",event.getEntityId());
        if(!event.getData().isEmpty()){
            for(Map.Entry<String,Object> entry:event.getData().entrySet()){
                content.put(entry.getKey(),entry.getValue());
            }
        }
        message.setContent(JSONObject.toJSONString(content));
        messageService.insertLetter(message);
    }
}
