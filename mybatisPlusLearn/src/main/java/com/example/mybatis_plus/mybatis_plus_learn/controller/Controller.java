package com.example.mybatis_plus.mybatis_plus_learn.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.mybatis_plus.mybatis_plus_learn.service.UserService;

import com.example.mybatis_plus.mybatis_plus_learn.mapper.SimpleMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shajiu
 * @date 2021/12/9 15:34
 */
@RestController
public class Controller {
    private static Logger logger=LoggerFactory.getLogger(Controller.class);

    @Autowired
    UserService userService;

    @GetMapping("/hello")
    public String HelloWorld(){
        // 这里添加打印的不会有sql语句的打印结果，要在yaml里添加配置
        logger.info("test");
        return userService.getName("test");
    }
}
