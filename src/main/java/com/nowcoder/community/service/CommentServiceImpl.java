package com.nowcoder.community.service;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.mapper.CommentMapper;
import com.nowcoder.community.mapper.DiscussPostMapper;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wxx
 * @version 1.0
 */
@Service
public class CommentServiceImpl implements CommentService, CommunityConstant {
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private DiscussPostMapper discussPostMapper;
    @Resource
    private SensitiveFilter sensitiveFilter;
    @Override
    public List<Comment> findCommentsByEntity(int entity_type, int entity_id, int offset, int limit) {
        return commentMapper.findCommentsByEntity(entity_type, entity_id, offset, limit);
    }

    @Override
    public int countCommentReply(int entity_id){
        return commentMapper.countCommentReply(entity_id);
    }

    @Override
    public int insertComment(Comment comment) {
        return commentMapper.insertComment(comment);
    }
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public int addComments(Comment comment){
        if(comment==null)
            throw new IllegalArgumentException("评论不能为空！！");
        //添加评论
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        int rows = commentMapper.insertComment(comment);
        //更新评论数量
        if(comment.getEntity_type()==ENTITY_TYPE_POST) {
            int count=commentMapper.countCommentReply(comment.getEntity_id());
            discussPostMapper.updateComment_Count(comment.getEntity_id(),count);
        }
        return rows;
    }
}
