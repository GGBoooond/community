package com.nowcoder.community.service;

import com.nowcoder.community.entity.Message;
import com.nowcoder.community.mapper.MessageMapper;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wxx
 * @version 1.0
 */
@Service
public class MessageServiceImpl implements  MessageService{
    @Resource
    private MessageMapper messageMapper;

    @Resource
    private SensitiveFilter sensitiveFilter;
    @Override
    public List<Message> selectConversations(int userId, int offset, int limit) {
        return messageMapper.selectConversations(userId,offset,limit);
    }

    @Override
    public int selectConversationCount(int userId) {
        return messageMapper.selectConversationCount(userId);
    }

    @Override
    public List<Message> selectLetters(String conversationId, int offset, int limit) {
        return messageMapper.selectLetters(conversationId,offset,limit);
    }

    @Override
    public int selectLetterCount(String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }

    @Override
    public int selectLetterUnreadCount(int userId, String conversationId) {
        return messageMapper.selectLetterUnreadCount(userId,conversationId);
    }

    @Override
    public int insertLetter(Message message) {
        if(message==null)
            throw new IllegalArgumentException("评论不能为空！！");
        //过滤
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insertLetter(message);
    }

    @Override
    public int updateLetterStatus(List<Integer> ids, int status) {
        return messageMapper.updateLetterStatus(ids,status);
    }

    @Override
    public int selectAllNoticeCount(int userId, String topic) {
        return messageMapper.selectAllNoticeCount(userId,topic);
    }

    @Override
    public int selectUnreadNoticeCount(int userId, String topic) {
        return messageMapper.selectUnreadNoticeCount(userId,topic);
    }

    @Override
    public Message selectLatestNotice(int userId, String topic) {
        return messageMapper.selectLatestNotice(userId,topic);
    }

    @Override
    public List<Message> selectNotices(int userId, String topic, int offset, int limit) {
        return messageMapper.selectNotices(userId,topic,offset,limit);
    }

}
