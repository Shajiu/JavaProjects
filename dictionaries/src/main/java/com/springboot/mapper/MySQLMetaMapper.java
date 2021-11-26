package com.springboot.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author shajiu
 * @date 2021/11/10 10:27
 */
@Resource
public interface MySQLMetaMapper extends MetaMapper {
    public static final String name = "MySQLMetaDmlMapper";
    @Select("SELECT * FROM websites")
    List<Map<String, Object>> getAllTracedTableByLike(@Param("name") String name);
}
