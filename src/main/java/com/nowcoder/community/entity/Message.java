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
public class Message {
    private int id;
    private int from_id;
    private int to_id;
    private String conversation_id;
    private String content;
    private int status;
    private Date create_time;
}
