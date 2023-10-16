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
public class User {
    private int id;
    private String username;
    private String password;

    //salt 给密码加密生成的随机值  储存时 是md5(password+salt)
    private String salt;
    private String email;
    private int type;

    //status 0 为未激活， 1 为 已激活
    private int status;

    //activation_code 验证激活生成的随机数
    private String activation_code;
    //header_url 头像图片的地址
    private String header_url;
    private Date create_time;
}