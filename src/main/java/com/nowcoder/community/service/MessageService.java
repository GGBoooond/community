package com.nowcoder.community.service;

import com.nowcoder.community.entity.Message;

import java.util.List;

/**
 * @author wxx
 * @version 1.0
 */
public interface MessageService {
    //查询当前用户的会话列表，针对每个会话只返回最新的一条私信
    List<Message> selectConversations(int userId, int offset, int limit);

    //查询当前用户的会话数量
    int selectConversationCount(int userId);

    //查询某个会话所包含的私信列表
    List<Message> selectLetters(String conversationId,int offset,int limit);

    //查询某个会话所包含的私信数量
    int selectLetterCount(String conversationId);

    //查询未读私信的数量
    int selectLetterUnreadCount(int userId,String conversationId);
    int insertLetter(Message message);

    int updateLetterStatus(List<Integer> ids,int status);
}
