package com.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
/**
 * @author shajiu
 * @date 2021/11/5 13:35
 * @Repository注解是把这个接口的一个实现类交给spring管理。
 */
@Repository
public interface UserMapper {
    int getAgeByUsername(String username);
}
