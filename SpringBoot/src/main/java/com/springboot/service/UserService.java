package com.springboot.service;

import javax.annotation.Resource;

/**
 * @author shajiu
 * @date 2021/11/5 13:39
 */
@Resource
public interface UserService {
    int getAgeByUsername(String username);
}
