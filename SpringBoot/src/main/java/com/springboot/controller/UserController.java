package com.springboot.controller;

import com.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shajiu
 * @date 2021/11/5 13:48
 */
@RestController

public class UserController {
    @Autowired
    UserService userService;
    @RequestMapping("/user/age")
    public int getAgeOfUser(){
        return userService.getAgeByUsername("springbootdemo");
    }
}
