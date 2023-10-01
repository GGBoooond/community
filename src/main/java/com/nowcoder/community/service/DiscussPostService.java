package com.nowcoder.community.service;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wxx
 * @version 1.0
 */
public interface DiscussPostService {
    List<DiscussPost> selectDiscussPosts(int userID, int offset, int limit);

    int selectDiscussPostRows(@Param("userID") int userID);
}
