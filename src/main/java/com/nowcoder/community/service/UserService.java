package com.nowcoder.community.service;

import com.nowcoder.community.entity.User;

/**
 * @author wxx
 * @version 1.0
 */
public interface UserService {
    User findUserByID(int id);
}
