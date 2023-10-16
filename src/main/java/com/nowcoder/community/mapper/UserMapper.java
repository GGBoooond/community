package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author wxx
 * @version 1.0
 */
@Mapper
public interface UserMapper {
    User findUserByID(int id);

    User findUserByEmail(String email);

    User findUserByUsername(String username);

    int insertUser(User user);

    int updateStatus(int id,int status);
    @Update("UPDATE user  SET header_url=#{headerUrl}" +
            " WHERE `id`=#{id} ")
    int  updateHeader(int id, String headerUrl);
}
