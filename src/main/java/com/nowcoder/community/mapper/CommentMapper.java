package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author wxx
 * @version 1.0
 */
@Mapper
public interface CommentMapper {
    @Select(" SELECT id,user_id,entity_type,entity_id,target_id,content,`status`,create_time" +
            " from comment " +
            "where entity_type= #{entity_type} AND entity_id=#{entity_id} " +
            "LIMIT #{offset}, #{limit}")
    List<Comment> findCommentsByEntity(int entity_type,int entity_id,int offset,int limit);
    @Select("SELECT COUNT(*) FROM `comment` " +
            "WHERE entity_id=#{entity_id}"
    )
    int countCommentReply(int entity_id);
    @Insert("insert into comment(user_id,entity_type,entity_id,target_id,content,`status`,create_time)" +
            "values (#{user_id},#{entity_type},#{entity_id},#{target_id}," +
            "#{content},#{status},#{create_time})")
    int insertComment(Comment comment);

    @Select(" SELECT id,user_id,entity_type,entity_id,target_id,content,`status`,create_time" +
            " from comment " +
            "where id= #{id} ")
    Comment selectCommentById(int id) ;
}
