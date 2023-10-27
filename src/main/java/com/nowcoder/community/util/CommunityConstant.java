package com.nowcoder.community.util;

/**
 * @author wxx
 * @version 1.0
 * 设置激活的字段
 */
public interface CommunityConstant {
    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS=0;
    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT=1;
    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE=2;
    /**
     * 默认情况下的登录凭证的超时时间
     */
    int DEFAULT_EXPIRED_SECONDS=3600*12;
    /**
     * 记住密码下的登录凭证的超时时间
     */
    int REMEMBER_EXPIRED_SECONDS=3600*24*7;

    /**
     *帖子评论
     */
    int ENTITY_TYPE_POST=1;

    /**
     * 评论下的回复
     */
    int ENTITY_TYPE_REPLY=2;

    /**
     * 关注 用户
     */
    int ENTITY_TYPE_USER=3;
    /**
     * 主题： 评论
     */
    String TOPIC_COMMENT="comment";
    /**
     * 主题：点赞
     */
    String TOPIC_LIKE="like";
    /**
     * 主题：关注
     */
    String TOPIC_FOLLOW="follow";
    /**
     * 通知id：系统
     */
    int SYSTEM_USER=1;
}
