package com.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
/**
 * @author shajiu
 * @date 2021/11/9 13:55
 */
@Mapper
@Resource
public interface BaseMapper {
    public static final String name = "baseDmlMapper";

    // 查询表名描述  DEV_TAB_H
    @Select("SELECT TAB_DES FROM DEV_TAB_H WHERE TAB_NAME='${tabName}'")
    List<Map<String,String>> getTableFormat(@Param("tabName") String tabName);

    // 查询表中字段属性  DEV_TAB_D
    @Select("SELECT FIE_NAME,FIE_DES,FIE_TYPE,FIE_LEN,IND_FLAG FROM DEV_TAB_D WHERE TAB_NAME ='${tabName}'")
    List<Map<String,Object>> getTableField(@Param("tabName") String tabName);

    // 获取Oracle中的数据信息  USER_TAB_COLS
    @Select("SELECT COLUMN_NAME FROM USER_TAB_COLS WHERE TABLE_NAME='${tabName}'")
    List<Map<String,Object>> getOracleTableField(@Param("tabName") String tabName);


    // 获取Mysql中的数据信息  information_schema.columns
    @Select("SELECT COLUMN_NAME FROM information_schema.columns  WHERE TABLE_NAME='${tabName}'")
    List<Map<String,Object>> getMysqlTableField(@Param("tabName") String tabName);


    // 删除表
    @Update("DROP TABLE ${tabName}")
    void delete(@Param("tabName") String tabName);


    // 查询接口2 Mysql和Oracle的公共部分
    @Select("SELECT\n" +
            "\tdev_tab_d.uu_id,\n" +
            "\tdev_tab_d.fie_des,\n" +
            "\tdev_tab_d.ind_flag,\n" +
            "\tdev_tab_d.fie,\n" +
            "\tdev_tab_d.upd_code,\n" +
            "\tdev_tab_d.upd_name,\n" +
            "\tdev_tab_d.upd_time,\n" +
            "\tdev_tab_d.dec_t,\n" +
            "\tdev_tab_d.dec_value,\n" +
            "\tdev_tab_d.huu_id,\n" +
            "\tdev_tab_d.fie_lab,\n" +
            "\tuser_tab_cols.COLUMN_NAME,\n" +
            "\tuser_tab_cols.DATA_TYPE,\n" +
            "\tuser_tab_cols.DATA_LENGTH\n" +
            "FROM\n" +
            "\tuser_tab_cols,\n" +
            "\tdev_tab_d \n" +
            "WHERE\n" +
            "\tuser_tab_cols.TABLE_NAME = dev_tab_d.TAB_NAME\n" +
            "\tAND user_tab_cols.COLUMN_NAME = dev_tab_d.fie_name\n" +
            "\tAND user_tab_cols.TABLE_NAME = '${tabName}'")
    List<Map<String,Object>> inquireD(@Param("tabName") String tabName);
}
