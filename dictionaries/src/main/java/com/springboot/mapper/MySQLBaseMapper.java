package com.springboot.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author shajiu
 * @date 2021/11/9 17:12
 */
@Repository
public interface MySQLBaseMapper extends BaseMapper {
    // 查询表是否存在,若存在，则返回1；若不存在，则返回0
    @Select("SELECT COUNT(*) FROM information_schema.TABLES WHERE TABLE_NAME ='${tableName}'")
    int decision(@Param("tableName") String tableName);

    // 查询表名描述
    @Select("SELECT TAB_DES FROM DEV_TAB_H WHERE TAB_NAME='${tabName}'")
    List<Map<String, String>> getTableFormat(@Param("tabName") String table);

    // 查询表中字段属性
    @Select("SELECT FIE_NAME,FIE_DES,FIE_TYPE,FIE_LEN,IND_FLAG FROM DEV_TAB_D WHERE TAB_NAME ='${table}'")
    List<Map<String, Object>> getTableField(@Param("table") String table);

    // 创建表
    @Update("${sqlStatement}")
    void execute(@Param("sqlStatement") String sqlStatement);

    // 添加字段
    @Update("ALTER TABLE ${tableName} ADD ${fieldName} ${fieldType} ( ${fieldLength} ),COMMENT '${fieldDes}'")
    void addFieles(@Param("tableName") String tableName, @Param("fieldName") String fieldName, @Param("fieldDes") String fieldDes, @Param("fieldType") String fieldType, @Param("fieldLength") String fieldLength);

    // 添加唯一索引
    @Update("ALTER TABLE ${tableName} ADD UNIQUE (`${fieldName}`)")
    void addIndex(@Param("tableName") String tableName, @Param("fieldName") String fieldName);

    // 判断数据表中是否存在某字段
    @Select("select count(*) from information_schema.columns where table_name = '${tabName}' and column_name = '${fieldName}'")
    int decisionField(@Param("tabName") String tabName, @Param("fieldName") String fieldName);

    // 修改字段属性(类型、长度)
    @Update("ALTER TABLE ${tabName} MODIFY COLUMN ${fieldName} ${fieldType}(${fieldLength})")
    void altFields(@Param("tabName") String tabName, @Param("fieldName") String fieldName, @Param("fieldType") String fieldType, @Param("fieldLength") String fieldLength);

    // 修改字段描述
    @Update("ALTER TABLE ${tabName} MODIFY COLUMN ${fieldName} ${fieldType} COMMENT '${fieldDes}';")
    void altFieldsComment(@Param("tabName") String tabName, @Param("fieldName") String fieldName, @Param("fieldType") String fieldType, @Param("fieldDes") String fieldDes);

    // 删除表中字段
    @Update("ALTER TABLE ${tabName} DROP ${fieldName}")
    void delFields(@Param("tabName") String tabName, @Param("fieldName") String fieldName);

    // 删除表
    @Update("DROP TABLE ${table}")
    void delete(@Param("table") String table);

    // 有参数的查询数据表H
    @Select("SELECT\n" +
            "\tdev_tab_h.IS_BUS_RECI,\n" +
            "\tdev_tab_h.IS_CONF,\n" +
            "\tdev_tab_h.upd_code,\n" +
            "\tdev_tab_h.upd_name,\n" +
            "\tdev_tab_h.upd_time,\n" +
            "\tdev_tab_h.uu_id,\n" +
            "\tdev_tab_h.sys_code,\n" +
            "\tdev_tab_h.tab_des,\n" +
            "\tinformation_schema.TABLES.TABLE_NAME \n" +
            "FROM\n" +
            "\tinformation_schema.TABLES,\n" +
            "\tdev_tab_h \n" +
            "WHERE\n" +
            "\tinformation_schema.TABLES.TABLE_NAME = dev_tab_h.TAB_NAME\n" +
            "\tAND dev_tab_h.SYS_CODE = '${tableName}'\n" +
            "\tAND information_schema.TABLES.TABLE_NAME like '%${tableName}%'")
    List<Map<String, Object>> inquireHParameter(@Param("tableName") String tableName);

    // 无参数的查询数据表H
    @Select("SELECT\n" +
            "\tdev_tab_h.IS_BUS_RECI,\n" +
            "\tdev_tab_h.IS_CONF,\n" +
            "\tdev_tab_h.upd_code,\n" +
            "\tdev_tab_h.upd_name,\n" +
            "\tdev_tab_h.upd_time,\n" +
            "\tdev_tab_h.uu_id,\n" +
            "\tdev_tab_h.sys_code,\n" +
            "\tdev_tab_h.tab_des,\n" +
            "\tinformation_schema.TABLES.TABLE_NAME \n" +
            "FROM\n" +
            "\tinformation_schema.TABLES,\n" +
            "\tdev_tab_h \n" +
            "WHERE\n" +
            "\tinformation_schema.TABLES.TABLE_NAME = dev_tab_h.TAB_NAME")
    List<Map<String, Object>> inquireHNotParameter();
}