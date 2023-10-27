package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wxx
 * @version 1.0
 */
@Mapper
public interface MessageMapper {
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

    //添加私信
    int insertLetter(Message message);

    //更新私信的状态
    int updateLetterStatus(List<Integer> ids,int status);
    //查询指定类型的系统通知的数量
    int selectAllNoticeCount(int userId, String topic);
    //查询指定类型的未读的系统通知的数量
    //如果conversationId 为null，则查询所有未读的系统通知
    int selectUnreadNoticeCount(int userId, String topic);
    //返回最近的一条通知
    Message selectLatestNotice(int userId,String topic);

    //返回指定类型的通知
    List<Message> selectNotices(int userId,String topic,int offset,int limit);
}
