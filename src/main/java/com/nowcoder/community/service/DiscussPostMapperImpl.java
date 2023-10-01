package com.nowcoder.community.service;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.mapper.DiscussPostMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wxx
 * @version 1.0
 */
@Service
public class DiscussPostMapperImpl implements DiscussPostService{
    @Resource
    private DiscussPostMapper discussPostMapper;
    @Override
    public List<DiscussPost> selectDiscussPosts(int userID, int offset, int limit) {
        return discussPostMapper.selectDiscussPosts(userID, offset, limit);
    }

    @Override
    public int selectDiscussPostRows(int userID) {
        return discussPostMapper.selectDiscussPostRows(userID);
    }

}
