package com.example.mybatis_plus.mybatis_plus_learn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.mybatis_plus.mybatis_plus_learn.mapper")
public class MybatisPlusLearnApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusLearnApplication.class, args);
    }
}
