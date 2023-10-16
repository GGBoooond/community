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
public class LoginTicket {
    private int id;
    private int user_id;
    // ticket 登录凭证
    private String ticket;
    //status  0 表示过期  1 表示未过期
    private int status;
    //expired 过期时间
    private Date expired;
}
