package com.nowcoder.community.dao;

import org.springframework.stereotype.Component;

/**
 * @author wxx
 * @version 1.0
 */
@Component
public class AlphaDaoImpl implements AlphaDao{
    @Override
    public String say() {
        return "hello,alpha";
    }
}
