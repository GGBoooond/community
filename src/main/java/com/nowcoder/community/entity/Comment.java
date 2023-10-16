package com.nowcoder.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author wxx
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private int id;
    private int user_id;
    //类型（1 回帖 2  回复）
    private int entity_type;
    //评论对象的id
    private int entity_id;
    //评论对象的用户的id（用于标记回复）
    private int target_id;
    private String content;
    private int status;
    private Date create_time;
}
