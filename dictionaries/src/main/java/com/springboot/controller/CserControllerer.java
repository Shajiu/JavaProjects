package com.springboot.controller;

import com.springboot.mapper.BaseMapper;
import com.springboot.mapper.OracleBaseMapper;
import com.springboot.mapper.UserMapper;
import com.springboot.po.UserPO;
import com.springboot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.springboot.entity.GenerativeGrammar;
import com.springboot.service.Manager;
import com.springboot.mapper.MySQLBaseMapper;
import java.util.*;


/**
 * @author shajiu
 * @date 2021/11/5 13:48
 */

@RestController
@Slf4j
public class CserControllerer {
    @Autowired
    UserService userService;
    @Autowired
    private UserMapper usermapper;
    @Autowired
    private Manager mgr;
    @Autowired
    private OracleBaseMapper oracleBaseMapper;
    @Autowired
    private MySQLBaseMapper mySQLBaseMapper;
    @Autowired
    GenerativeGrammar generativeGrammar;

    /**
     * 查询原始数据表信息(SELECT * FROM DEV_DBS)
     *
     * @param userpo
     * @return
     */
    @RequestMapping("/getAller")
    public List<UserPO> getNameByUsername(UserPO userpo) {
        return userService.getNameByUsername(userpo);
    }

    @GetMapping(path = "/linker")
    public LinkedHashMap<String, LinkedHashMap<String, Object>> test(@RequestBody JSONObject person) {
        String dbsName = (String) person.get("dsname");                          //  数据源名
        String passWord = (String) person.get("dspassword");                     //  数据密码
        String comCode = (String) person.get("comCode");                         //  数据编码
        List<LinkedHashMap<String, Object>> establish = new ArrayList<>();       //  创建数据表
        List<LinkedHashMap<String, Object>> maintain = new ArrayList<>();        //  编辑数据表
        List<String> remove = new ArrayList<>();                                 //  删除数据表
        LinkedHashMap<String, List<String>> inquire1 = new LinkedHashMap<String, List<String>>();    //查询1
        LinkedHashMap<String, List<String>> inquire2 = new LinkedHashMap<String, List<String>>();    //查询1
        LinkedHashMap<String, LinkedHashMap<String, Object>> inquireResultMap = new LinkedHashMap<>();   //最终查询的返回结果集
        /**
         * 创建数据表信息
         */
        establish = (List<LinkedHashMap<String, Object>>) person.get("establish");
        tableCreate(dbsName, passWord, establish);

        /**
         * 维护数据表信息
         */
        maintain = (List<LinkedHashMap<String, Object>>) person.get("maintain");
        tableMaintain(dbsName, passWord, maintain);

        /**
         * 删除数据表信息
         *
         */
        remove = (List<String>) person.get("remove");
        tableDelete(dbsName, passWord, remove);

        /**
         * 查询1
         */
        inquire1 = (LinkedHashMap<String, List<String>>) person.get("inquire1");
        inquireResultMap.put("inquire1", tabaleInquireH(dbsName, passWord, inquire1));

        /**
         * 查询2
         */
        inquire2 = (LinkedHashMap<String, List<String>>) person.get("inquire2");
        inquireResultMap.put("inquire2", tabaleInquireD(dbsName, passWord, inquire2));

        return inquireResultMap;
    }

    /**
     * 创建数据表接口
     *
     * @param passWord
     * @return
     */
    public String tableCreate(String dbsName, String passWord, List<LinkedHashMap<String, Object>> establish) {
        UserPO Dbs = userService.getDataByUsername(dbsName);
        mgr.getDataSource(Dbs, passWord);
        /**
         * 链接Mysql数据源
         */
        if (Dbs.getDbsType().equals(Manager.MYSQL)) {
            MySQLBaseMapper mapper = mgr.getMapper(Dbs.getUsrName(), "baseDmlMapper");
            //  表结构循环
            for (LinkedHashMap property : establish) {
                List<LinkedHashMap<String, String>> fieldProperty = (List<LinkedHashMap<String, String>>) property.get("fields");
                String establishSql = "";       // 待执行的语句
                String indexs = "";             // 唯一索引
                if (mapper.decision((String) property.get("tabName")) != 1) {
                    for (LinkedHashMap<String, String> field : fieldProperty) {
                        establishSql = establishSql + field.get("fieldName") + " " + field.get("fieldType") + "(" + field.get("fieldLength") + ") COMMENT " + "'" + field.get("fieldDes") + "',";
                        if (field.get("indFlag").equals("Y")) {
                            indexs = indexs + field.get("fieldName") + ",";
                        }
                    }
                    if (indexs.length() != 0) {
                        establishSql = "CREATE TABLE IF NOT EXISTS " + property.get("tabName") + "(" + establishSql + "UNIQUE INDEX (" + indexs.substring(0, indexs.length() - 1) + "))" + "COMMENT ='" + property.get("tabDes") + "'";
                    } else {
                        establishSql = "CREATE TABLE IF NOT EXISTS " + property.get("tabName") + "(" + establishSql.substring(0, establishSql.length() - 1) + ")" + "COMMENT ='" + property.get("tabDes") + "'";
                    }
                    mapper.execute(establishSql); // 向表添加注解、直接执行即可
                }
            }
            return "创建数据表完成!!!";
        }

        /**
         * 链接Oracle数据源
         */
        else if (Dbs.getDbsType().equals(Manager.ORACLE)) {
            OracleBaseMapper mapper = mgr.getMapper(Dbs.getUsrName(), "baseDmlMapper");
            //  表结构循环
            for (LinkedHashMap property : establish) {
                List<LinkedHashMap<String, String>> fieldProperty = (List<LinkedHashMap<String, String>>) property.get("fields");
                String establishSql = "";     // 待执行的语句
                String indexs = "";           // 向表设置唯一索引
                //  判断此表是否存在
                if (mapper.decision((String) property.get("tabName")) != 1) {
                    for (LinkedHashMap<String, String> field : fieldProperty) {
                        establishSql = establishSql + field.get("fieldName") + " " + field.get("fieldType") + "(" + Integer.parseInt(field.get("fieldLength")) + "),";
                        if (field.get("indFlag").equals("Y")) {
                            indexs = indexs + field.get("fieldName") + ",";
                        }
                    }
                    establishSql = "CREATE TABLE " + property.get("tabName") + "(" + establishSql.substring(0, establishSql.length() - 1) + ")"; //创建表的语句
                    mapper.execute(establishSql); // 创建数据表、直接执行即可
                    establishSql = "COMMENT ON TABLE " + property.get("tabName") + " IS " + "'" + property.get("tabDes") + "'";
                    mapper.execute(establishSql); // 向表添加注解、直接执行即可
                    //   向字段添加注解
                    for (LinkedHashMap<String, String> field : fieldProperty) {
                        mapper.addFieldAnnotation((String) property.get("tabName"), field.get("fieldName"), field.get("fieldDes"));
                    }
                    //   判断是否含有索引
                    if (indexs.length() > 0) {
                        indexs = indexs.substring(0, indexs.length() - 1);
                        indexs = "CREATE UNIQUE INDEX " + "UI_" + property.get("tabName") + " ON " + property.get("tabName") + "(" + indexs + ")";
                        mapper.execute(indexs); // 向表添加唯一索引、直接执行即可
                    } else {
                        System.out.println("无设置唯一索引!!!");
                    }
                }
            }
        }
        return null;
    }

    /**
     * 维护数据表接口
     *
     * @param dbsName
     * @param passWord
     * @param maintain
     */
    public String tableMaintain(String dbsName, String passWord, List<LinkedHashMap<String, Object>> maintain) {
        UserPO Dbs = userService.getDataByUsername(dbsName);
        mgr.getDataSource(Dbs, passWord);

        /**
         * 链接Mysql数据源
         */
        if (Dbs.getDbsType().equals(Manager.MYSQL)) {
            MySQLBaseMapper mySQLBaseMapper = mgr.getMapper(Dbs.getUsrName(), "baseDmlMapper");
            //  遍历每个待维护的数据信息
            for (LinkedHashMap<String, Object> fieldProperty : maintain) {
                // 判断数据表是否存在
                if (mySQLBaseMapper.decision((String) fieldProperty.get("tabName")) == 1) {
                    // 添加部分
                    List<LinkedHashMap<String, String>> addFields = (List<LinkedHashMap<String, String>>) fieldProperty.get("addFields");
                    for (LinkedHashMap<String, String> fields : addFields) {
                        // 判断表中是否存在此字段
                        if (mySQLBaseMapper.decisionField((String) fieldProperty.get("tabName"), fields.get("fieldName")) != 1) {
                            mySQLBaseMapper.addFieles((String) fieldProperty.get("tabName"),
                                    fields.get("fieldName"), fields.get("fieldDes"),
                                    fields.get("fieldType"), fields.get("fieldLength"));
                            // 有唯一索引
                            if (fields.get("indFlag").equals("Y")) {
                                 mySQLBaseMapper.addIndex((String) fieldProperty.get("tabName"), fields.get("fieldName"));
                            }
                        }
                    }
                    // 更新部分
                    List<LinkedHashMap<String, String>> altFields = (List<LinkedHashMap<String, String>>) fieldProperty.get("altFields");
                    for (LinkedHashMap<String, String> fields : altFields) {
                        // 更新字段属性(类型、长度)
                        mySQLBaseMapper.altFields((String) fieldProperty.get("tabName"), fields.get("fieldName"),
                                fields.get("fieldType"), fields.get("fieldLength"));
                        // 更新注解
                        mySQLBaseMapper.altFieldsComment((String) fieldProperty.get("tabName"), fields.get("fieldName"),
                                fields.get("fieldType"), fields.get("fieldDes"));
                        // 修改唯一索引
                        if (fields.get("indFlag").equals("Y")) {
                            mySQLBaseMapper.addIndex((String) fieldProperty.get("tabName"), fields.get("fieldName"));
                        }
                    }
                    // 删除部分
                    List<LinkedHashMap<String, String>> delFields = (List<LinkedHashMap<String, String>>) fieldProperty.get("delFields");
                    for (LinkedHashMap<String, String> fields : delFields) {
                        // 删除此表中的字段
                        mySQLBaseMapper.delFields((String) fieldProperty.get("tabName"), fields.get("fieldName"));
                        // 删除数据字典定义中相应表记录/删除明细表记录
                        userService.deleteField((String) fieldProperty.get("tabName"), fields.get("fieldName"));

                    }
                }
            }
        }
        /**
         * 链接Oracle数据源
         */
        else if (Dbs.getDbsType().equals(Manager.ORACLE)) {
            OracleBaseMapper mapperOracle = mgr.getMapper(Dbs.getUsrName(), "baseDmlMapper");
            for (LinkedHashMap<String, Object> fieldProperty : maintain) {
                if (mapperOracle.decision((String) fieldProperty.get("tabName")) == 1) {
                    // 添加部分
                    List<LinkedHashMap<String, String>> addFields = (List<LinkedHashMap<String, String>>) fieldProperty.get("addFields");
                    for (LinkedHashMap<String, String> fields : addFields) {
                        // 判断此字段是否在于此表中
                        if (mapperOracle.decisionField((String) fieldProperty.get("tabName"), fields.get("fieldName")) != 1) {
                            //  添加字段
                            mapperOracle.addField((String) fieldProperty.get("tabName"), fields.get("fieldName"), fields.get("fieldType"), Integer.parseInt(fields.get("fieldLength")));
                            //  添加注释
                            mapperOracle.addFieldAnnotation((String) fieldProperty.get("tabName"), fields.get("fieldName"), fields.get("fieldDes"));
                            //  添加唯一索引
                            if (fields.get("indFlag").equals("Y")) {
                                mapperOracle.addFieldIndex((String) fieldProperty.get("tabName"), fields.get("fieldName"));
                            }
                        }
                    }
                    // 修改部分
                    List<LinkedHashMap<String, String>> altFields = (List<LinkedHashMap<String, String>>) fieldProperty.get("altFields");
                    for (LinkedHashMap<String, String> fields : altFields) {
                        // 判断此字段是否在于此表中
                        if (mapperOracle.decisionField((String) fieldProperty.get("tabName"), fields.get("fieldName")) == 1) {
                            // 更新表字段:字段类型、字段大小
                            mapperOracle.updateField((String) fieldProperty.get("tabName"), fields.get("fieldName"), fields.get("fieldType"), Integer.parseInt(fields.get("fieldLength")));
                            // 更新注释
                            mapperOracle.addFieldAnnotation((String) fieldProperty.get("tabName"), fields.get("fieldName"), fields.get("fieldDes"));
                        }
                    }
                    // 删除部分
                    List<LinkedHashMap<String, String>> delFields = (List<LinkedHashMap<String, String>>) fieldProperty.get("delFields");
                    for (LinkedHashMap<String, String> fields : delFields) {
                        // 判断此字段是否在于此表中
                        if (mapperOracle.decisionField((String) fieldProperty.get("tabName"), fields.get("fieldName")) == 1) {                           // 在当前创建的数据表中此删除字段
                            mapperOracle.deleteField((String) fieldProperty.get("tabName"), fields.get("fieldName"));
                            // 删除数据字典定义中相应表记录/删除明细表记录
                            userService.deleteField((String) fieldProperty.get("tabName"), fields.get("fieldName"));
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 功能: 删除数据表接口
     *
     * @param dbsName
     * @param passWord
     * @param remove
     * @return
     */
    public String tableDelete(String dbsName, String passWord, List<String> remove) {
        UserPO Dbs = userService.getDataByUsername(dbsName);
        mgr.getDataSource(Dbs, passWord);
        // 链接Mysql数据源
        if (Dbs.getDbsType().equals(Manager.MYSQL)) {
            MySQLBaseMapper mapper = mgr.getMapperMySQLBaseMapper(Dbs.getUsrName(), "baseDmlMapper");
            for (String table : remove) {
                //  删除当前所创建的数据表
                if (mapper.decision(table) == 1) {
                    mapper.delete(table);
                    // 删除头表记录
                    userService.deleteTableData("dev_tab_h", table);
                    // 删除明细表记录
                    userService.deleteTableData("dev_tab_d", table);
                }
            }
        }

        // 链接Oracle数据源
        if (Dbs.getDbsType().equals(Manager.ORACLE)) {
            OracleBaseMapper mapper = mgr.getMapperOracleBaseMapper(Dbs.getUsrName(), "baseDmlMapper");
            for (String table : remove) {
                if (mapper.decision(table) == 1) {
                    mapper.delete(table);
                    // 删除头表记录
                    userService.deleteTableData("dev_tab_h", table);
                    // 删除明细表记录
                    userService.deleteTableData("dev_tab_d", table);
                }
            }
        }
        return "删除成功!";
    }

    /**
     * 查询数据表接口1
     */
    public LinkedHashMap<String, Object> tabaleInquireH(String dbsName, String password, LinkedHashMap<String, List<String>> inquire1) {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();  // 查询返回的结果
        UserPO Dbs = userService.getDataByUsername(dbsName);
        log.debug("dbs:{}", Dbs);
        mgr.getDataSource(Dbs, password);
        System.out.println("查询1执行中" + Dbs.getDbsType());
        //  链接Mysql数据源
        if (Dbs.getDbsType().equals(Manager.MYSQL)) {
            MySQLBaseMapper mapper = mgr.getMapperOracleBaseMapper(Dbs.getUsrName(), "baseDmlMapper");
            //  无参数查询
            if (inquire1.get("TAB_NAME").size() == 0) {
                resultMap.put("NotTable", mapper.inquireHNotParameter());
            }
            for (String tableName : inquire1.get("TAB_NAME")) {
                if (mapper.decision(tableName) == 1) {
                    // 有参数查询
                    resultMap.put(tableName, mapper.inquireHParameter(tableName));
                }
            }
            return resultMap;
        }
        // 链接Oracle数据源
        else if (Dbs.getDbsType().equals(Manager.ORACLE)) {
            OracleBaseMapper mapper = mgr.getMapperOracleBaseMapper(Dbs.getUsrName(), "baseDmlMapper");
            // 无参数查询
            if (inquire1.get("TAB_NAME").size() == 0) {
                resultMap.put("NotTable", mapper.inquireHNotParameter());
            } else {
                // 查询遍历
                for (String tableName : inquire1.get("TAB_NAME")) {
                    if (mapper.decision(tableName) == 1) {
                        resultMap.put(tableName, mapper.inquireHParameter(tableName));
                    }
                }
            }
        }
        return resultMap;
    }

    /**
     * 查询数据表接口2  Mysql和Oracle公用方法
     * 自测没问题
     *
     * @param dbsName
     * @param password
     * @param inquire2
     */
    public LinkedHashMap<String, Object> tabaleInquireD(String dbsName, String password,
                                                        LinkedHashMap<String, List<String>> inquire2) {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();  // 查询返回的结果
        UserPO Dbs = userService.getDataByUsername(dbsName);
        mgr.getDataSource(Dbs, password);
        BaseMapper mapper = mgr.getMapperOracleBaseMapper(Dbs.getUsrName(), "baseDmlMapper");
        for (String tableName : inquire2.get("TAB_NAME")) {
            if (Dbs.getDbsType().equals(Manager.MYSQL)) {

                MySQLBaseMapper mySQLBaseMapper = mgr.getMapperOracleBaseMapper(Dbs.getUsrName(), "baseDmlMapper");
                if (mySQLBaseMapper.decision(tableName) == 1) {
                    resultMap.put(tableName, mapper.inquireD(tableName));
                }
            } else if (Dbs.getDbsType().equals(Manager.ORACLE)) {
                OracleBaseMapper oracleBaseMapper = mgr.getMapperOracleBaseMapper(Dbs.getUsrName(), "baseDmlMapper");
                if (oracleBaseMapper.decision(tableName) == 1) {
                    resultMap.put(tableName, mapper.inquireD(tableName));
                }
            }
        }
        return resultMap;
    }

    /**
     * 关闭数据源
     * 自测没问题
     *
     * @param password
     */
    public Object closeDataSource(String dbsName, String password) {
        UserPO Dbs = userService.getDataByUsername(dbsName);
        log.debug("dbs:{}", Dbs);
        mgr.getDataSource(Dbs, password);
        mgr.closeDataSource(Dbs.getUsrName(), password);
        return "数据源已关闭";
    }
}
