package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author wxx
 * @version 1.0
 */
@Mapper
public interface DiscussPostMapper {
    List<DiscussPost> selectDiscussPosts(int userID,int offset,int limit);
    //@Param用于给参数取别名
    //如果只有一个参数，并且需要在<if>中使用，则需要
    int selectDiscussPostRows(@Param("userID") int userID);


    @Insert("INSERT INTO discuss_post(user_id,title,content,`type`,`status`,create_time,comment_count,score)" +
            " VALUES(#{user_id},#{title},#{content},#{type}," +
            "#{status},#{create_time},#{comment_count},#{score}) ")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertDiscussPost(DiscussPost discussPost);
    @Select("SELECT *  FROM discuss_post  " +
            "WHERE id= #{id} ")
    DiscussPost selectDiscussPostByID(int id);
    @Update("update discuss_post " +
            "set comment_count=#{count} " +
            "where id=#{id}")
    int updateComment_Count(int id,int count);
}
