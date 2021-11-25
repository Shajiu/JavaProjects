package com.springboot.service.impl;

import com.springboot.mapper.UserMapper;
import com.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shajiu
 * @date 2021/11/5 13:47
 * 在service接口的实现类中，要加上@Service注解，把实现类交给spring处理
 */
@Service
public class UserServiceImpl implements UserService {
    //通过@Autowired注解获得自动注入的userMapper实现类，在重写的方法中进行调用，获得数据。
    @Autowired
    UserMapper userMapper;

    @Override
    public int getAgeByUsername(String username) {
        return userMapper.getAgeByUsername(username);
    }
}
