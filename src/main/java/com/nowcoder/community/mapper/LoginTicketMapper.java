package com.nowcoder.community.mapper;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * @author wxx
 * @version 1.0
 */
@Mapper
@Deprecated
public interface LoginTicketMapper {
    @Insert("insert into login_ticket(user_id,ticket,`status`,expired) " +
            "VALUES(#{user_id},#{ticket},#{status},#{expired})"
    )
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    @Select("SELECT *  FROM login_ticket  " +
            "WHERE ticket= #{ticket} ")
    LoginTicket selectByTicket(String ticket);

    @Update("UPDATE login_ticket  SET `status`=#{status} " +
            " WHERE ticket=#{ticket} ")
    int updataStatus(String ticket, int status);
}
