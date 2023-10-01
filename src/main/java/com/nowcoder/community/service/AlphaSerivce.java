package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wxx
 * @version 1.0
 */
@Service
public class AlphaSerivce {
    @Autowired
    private AlphaDao alphaDao;
    public String say(){
        return alphaDao.say();
    }
}
