package com.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shajiu
 * @date 2021/11/5 13:48
 */
@RestController
public class HelloWorldController {
    @RequestMapping(value = "/hello")
    public String sayHelloWorld() {
        System.out.println("测试段!!!");
        return "HelloWorld";
    }
}
