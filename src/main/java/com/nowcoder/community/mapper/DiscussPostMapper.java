package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

}
