package com.springboot.mapper;

import com.springboot.po.UserPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

/**
 * @author shajiu
 * @date 2021/11/5 13:35
 * @Repository注解是把这个接口的一个实现类交给spring管理。
 */
@Repository
@Mapper
public interface UserMapper {
    // 获取初始链接的数据源信息
    UserPO getDataByUsername(@Param("dbsName")String dbsName);
    // 删除数据字典定义中相应表记录
    void  deleteField(@Param("tabname")String tabname,@Param("field")String field);
    // 删除头表记录和删除明细表记录
    void  deleteTableData(@Param("DevTab")String DevTab,@Param("tabname")String tabname);

    List<UserPO> getNameByUsername(@Param("UserPO")UserPO userpo);
}
