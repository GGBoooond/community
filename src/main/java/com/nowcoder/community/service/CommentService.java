package com.nowcoder.community.service;

import com.nowcoder.community.entity.Comment;

import java.util.List;

/**
 * @author wxx
 * @version 1.0
 */
public interface CommentService {
    List<Comment> findCommentsByEntity(int entity_type, int entity_id, int offset, int limit);
    int countCommentReply(int entity_id);

    int insertComment(Comment comment);
    int addComments(Comment comment);

    Comment selectCommentById(int id);
}
