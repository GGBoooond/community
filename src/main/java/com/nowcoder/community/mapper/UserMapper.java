package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wxx
 * @version 1.0
 */
@Mapper
public interface UserMapper {
    User findUserByID(int id);
}
