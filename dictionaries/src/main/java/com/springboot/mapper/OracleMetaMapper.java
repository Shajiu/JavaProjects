package com.springboot.mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import javax.annotation.Resource;

/**
 * @author shajiu
 * @date 2021/11/10 10:28
 */
@Resource
public interface OracleMetaMapper extends MetaMapper {

    @Select("SELECT COUNT(*) FROM USER_TABLES WHERE TABLE_NAME=UPPER('${tabName}')")
    void addColumnComment(@Param("table") String table);
}