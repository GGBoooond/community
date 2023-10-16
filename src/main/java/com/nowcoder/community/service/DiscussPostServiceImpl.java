package com.nowcoder.community.service;

import com.mysql.cj.util.StringUtils;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.mapper.DiscussPostMapper;
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
public class DiscussPostServiceImpl implements DiscussPostService{
    @Resource
    private DiscussPostMapper discussPostMapper;
    @Resource
    private SensitiveFilter sensitiveFilter;
    @Override
    public List<DiscussPost> selectDiscussPosts(int userID, int offset, int limit) {
        return discussPostMapper.selectDiscussPosts(userID, offset, limit);
    }

    @Override
    public int selectDiscussPostRows(int userID) {
        return discussPostMapper.selectDiscussPostRows(userID);
    }

    @Override
    public int addDiscussPost(DiscussPost post) {
        if(post==null)
            throw new IllegalArgumentException("参数不能为空");
        //转义HTML标记
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        //过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));
        return discussPostMapper.insertDiscussPost(post);
    }
    @Override
    public DiscussPost selectDiscussPostByID(int id){
        return discussPostMapper.selectDiscussPostByID(id);
    }

    @Override
    public int updateComment_Count(int id, int count) {
        return discussPostMapper.updateComment_Count(id,count);
    }
}
