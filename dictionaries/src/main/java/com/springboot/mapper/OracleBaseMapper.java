package com.springboot.mapper;

import com.springboot.po.UserPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author shajiu
 * @date 2021/11/9 17:28
 */
@Repository
@Mapper
public interface OracleBaseMapper extends BaseMapper {

    // 判断是否存在数据表
    @Select("SELECT COUNT(*) FROM USER_TABLES WHERE TABLE_NAME=UPPER('${tabName}')")
    int decision(@Param("tabName") String tabName);

    // 获取数据表描述
    @Select("SELECT TAB_DES FROM DEV_TAB_H WHERE TAB_NAME='${tabName}'")
    List<Map<String, String>> getTableFormat(@Param("tabName") String table);

    // 获取数据表字段属性值
    @Select("SELECT FIE_NAME,FIE_DES,FIE_TYPE,FIE_LEN,IND_FLAG FROM DEV_TAB_D WHERE TAB_NAME ='${table}'")
    List<Map<String,Object>> getTableField(@Param("table") String table);

    // 创建表
    @Update("${sqlStatement}")
    void execute(@Param("sqlStatement") String sqlStatement);

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
            "\tuser_tables.TABLE_NAME \n" +
            "FROM\n" +
            "\tuser_tables,\n" +
            "\tdev_tab_h \n" +
            "WHERE\n" +
            "\tuser_tables.TABLE_NAME = dev_tab_h.TAB_NAME\n" +
            "\tAND dev_tab_h.SYS_CODE = '${tableName}'\n" +
            "\tAND user_tables.TABLE_NAME like '%${tableName}%'")
    List<Map<String, Object>>inquireHParameter(@Param("tableName")String tableName);

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
            "\tuser_tables.TABLE_NAME \n" +
            "FROM\n" +
            "\tuser_tables,\n" +
            "\tdev_tab_h \n" +
            "WHERE\n" +
            "\tuser_tables.TABLE_NAME = dev_tab_h.TAB_NAME")
    List<Map<String, Object>> inquireHNotParameter();

    // 删除表中的字段
    // 删除表
    @Update("Alter TABLE ${table} DROP (${field})")
    void deleteField(@Param("table") String table,@Param("field") String field);

    // 更改字段属性 类型、长度
    @Update("Alter table ${table} modify (${fieName} ${fieType}(${fieLen}))")
    void updateField(@Param("table") String table,@Param("fieName")String fieName,@Param("fieType")String fieType ,@Param("fieLen")int fieLen);


    // 更新字段索引
    @Update("Alter table ${table} modify (${fieName} ${fieType}('${fieLen}'))")
    void updateFieldIndex(@Param("table") String table,@Param("fieName")String fieName,@Param("fieType")String fieType ,@Param("fieLen")int fieLen);

    // 判断某字段是否在于此表
    @Select("SELECT COUNT(*) FROM USER_TAB_COLUMNS WHERE TABLE_NAME = '${table}' AND COLUMN_NAME = '${fieName}'")
    int decisionField(@Param("table") String table,@Param("fieName")String fieName);

    // 增加字段
    @Update("Alter table ${table} ADD (${fieName} ${fieType} ( ${fieLen} ))")
    void addField(@Param("table") String table,@Param("fieName")String fieName,@Param("fieType")String fieType ,@Param("fieLen")int fieLen);

    // 增加字段注解
    @Update("COMMENT ON COLUMN ${table}.${fieName} IS '       ${fieDes}'")
    void addFieldAnnotation(@Param("table") String table,@Param("fieName")String fieName,@Param("fieDes")String fieDes);
    // 增加索引
    @Update("CREATE UNIQUE INDEX UI_${table}_${fieName} ON ${table} ('${fieName}')")
    void addFieldIndex(@Param("table") String table,@Param("fieName")String fieName);



    UserPO selectAllFrom(@Param("table")String table);
}

