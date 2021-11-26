package com.springboot.mapper;

import org.apache.ibatis.annotations.*;

import javax.annotation.Resource;

/**
 * @author shajiu
 * @date 2021/11/9 14:00
 */

@Mapper
@Resource
public interface MetaMapper {
    public static final String name = "ddlMetaMapper";
}
