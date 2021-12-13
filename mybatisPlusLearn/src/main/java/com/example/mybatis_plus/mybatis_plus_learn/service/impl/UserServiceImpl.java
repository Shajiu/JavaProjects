package com.example.mybatis_plus.mybatis_plus_learn.service.impl;

import com.example.mybatis_plus.mybatis_plus_learn.service.UserService;
import com.example.mybatis_plus.mybatis_plus_learn.mapper.SimpleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shajiu
 * @date 2021/12/9 15:45
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    SimpleMapper simpleMapper;

    @Override
    public String getName(String name) {
        return simpleMapper.getName(name);
    }
}
