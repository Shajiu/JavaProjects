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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author shajiu
 * @date 2021/11/5 13:48
 */

@RestController
@Slf4j
public class UserController {
    // 控制是否存在表的判断依据
    public static final int DECISION = 1;
    // 添加字段标识符
    public static final String addField = "add_field";
    // 修改字段标识符
    public static final String updateField = "update_field";
    // 删除字段标识符
    public static final String deleteField = "delete_field";
    // 获取Mysql中表的字段值
    public static List<Map<String, Object>> fieldMysqlUserCols = new ArrayList<>();
    // 获取Oracle中表的字段值
    public static List<Map<String, Object>> fieldOracleUserCols = new ArrayList<>();

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

    // 实例化函数
    @Autowired
    GenerativeGrammar generativeGrammar;

    /**
     * 查询原始数据表信息(SELECT * FROM DEV_DBS)
     *
     * @param userpo
     * @return
     */
    @RequestMapping("/getAll")
    public List<UserPO> getNameByUsername(UserPO userpo) {
        /**
         * 第一步、入口访问连接接口
         */
        return userService.getNameByUsername(userpo);
    }

    @GetMapping(path = "/test")
    public JSONObject test(@RequestBody JSONObject person){
        System.out.println(person);
        return person;
    }

    @PostMapping(path = "/link")
    public Map<String, List<Map<String, Object>>> establish(@RequestBody JSONObject person) {
        String dbsName = "";   //数据源名
        String password = "";  //数据密码
        Map<String, List<Map<String, Object>>> resultInquireMap = new HashMap<>();   //查询返回的结果
        System.out.println("person:" + person);
        List<Map<String, List<String>>> establish = (List<Map<String, List<String>>>) person.get("establish");
        List<Map<String, List<String>>> maintain = (List<Map<String, List<String>>>) person.get("maintain");
        List<Map<String, List<String>>> remove = (List<Map<String, List<String>>>) person.get("remove");
        List<Map<String, List<String>>> inquire1 = (List<Map<String, List<String>>>) person.get("inquire1");
        List<Map<String, List<String>>> inquire2 = (List<Map<String, List<String>>>) person.get("inquire2");

        /**
         * 创建部分
         */
        if (establish.get(2).size() > 0) {
            dbsName = establish.get(0).get("dsname").get(0);
            password = establish.get(1).get("dspassword").get(0);
            for (String table : establish.get(2).get("TAB_NAME")) {
                // 调用创建表函数
                tableCreate(dbsName, password, table);
                System.out.println("dbsName:" + dbsName + "\tpassword:" + password + "\t表名" + table);
            }
        }

        /**
         * 维护部分
         */
        if (maintain.get(2).size() > 0 & maintain.get(3).size() > 0) {
            // System.out.println("维护部分:"+maintain);
            dbsName = maintain.get(0).get("dsname").get(0);
            password = maintain.get(1).get("dspassword").get(0);
            for (String table : maintain.get(2).get("TAB_NAME")) {
                for (String signboard : maintain.get(3).get("signboard")) {
                    // 调用维护表函数
                    tableMaintain(dbsName, password, table, signboard);
                    System.out.println("dbsName:" + dbsName + "\tpassword:" + password + "\t表名:" + table + "\t维护:" + signboard);

                }

            }
        }


        /**
         * 删除部分
         */
        if (remove.get(2).size() > 0) {
            // System.out.println("remove:\n" + remove);
            dbsName = remove.get(0).get("dsname").get(0);
            password = remove.get(1).get("dspassword").get(0);
            for (String table : remove.get(2).get("TAB_NAME")) {
                // 调用删除部分函数
                tableDelete(dbsName, password, table);
                System.out.println("dbsName:" + dbsName + "\tpassword:" + password + "\t表名:" + table);
            }

        }

        /**
         * 查询1
         */
        if (inquire1.size() > 0) {
            //System.out.println("查询1:"+inquire1);
            dbsName = inquire1.get(0).get("dsname").get(0);
            password = inquire1.get(1).get("dspassword").get(0);
            // 有参数的情况下执行
            if (inquire1.get(2).size() > 0) {
                for (String table : inquire1.get(2).get("TAB_NAME")) {
                    List<Map<String, Object>> result = tabaleInquireH(dbsName, password, table);
                    resultInquireMap.put(table, result);
                    System.out.println("dbsName:" + dbsName + "\tpassword:" + password + "\t表名:" + table);
                }
            }
            // 无参数的情况下执行
            else {
                List<Map<String, Object>> result = tabaleInquireH(dbsName, password, "");
                resultInquireMap.put("tableName", result);
                System.out.println("dbsName:" + dbsName + "\tpassword:" + password + "\t表名:");
            }
        }

        /**
         * 查询2
         */
        if (inquire2.get(2).size() > 0) {
            dbsName = inquire2.get(0).get("dsname").get(0);
            password = inquire2.get(1).get("dspassword").get(0);
            for (String table : inquire2.get(2).get("TAB_NAME")) {
                List<Map<String, Object>> result = tabaleInquireD(dbsName, password, table);
                resultInquireMap.put(table, result);
                System.out.println("dbsName:" + dbsName + "\tpassword:" + password + "\t表名:" + table);
            }
        }
        return resultInquireMap;
    }

    /**
     * 创建数据表接口
     *
     * @param password
     * @return
     */
    @RequestMapping("/tableCreate")
    public String tableCreate(@RequestParam("dbsName") String dbsName,
                              @RequestParam("password") String password,
                              @RequestParam("table") String table) {
        UserPO Dbs = userService.getDataByUsername(dbsName);
        mgr.getDataSource(Dbs, password);
        //  链接Mysql数据源
        if (Dbs.getDbsType().equals(Manager.MYSQL)) {
            MySQLBaseMapper mapper = mgr.getMapper(Dbs.getUsrName(), "baseDmlMapper");
            List<Map<String, String>> tableNameFormat = mapper.getTableFormat(table);
            System.out.println("表名描述" + tableNameFormat.get(0).get("TAB_DES"));
            List<Map<String, Object>> field = mapper.getTableField(table);
            System.out.println("表属性:" + field);
            return "创建数据表完成!!!";
        }
        // 链接Oracle数据源
        else if (Dbs.getDbsType().equals(Manager.ORACLE)) {
            OracleBaseMapper mapper = mgr.getMapper(Dbs.getUsrName(), "baseDmlMapper");
            List<Map<String, String>> tableNameFormat = mapper.getTableFormat(table);
            //System.out.println("表名描述:\n" + tableNameFormat.get(0).get("TAB_DES"));
            List<Map<String, Object>> fieldProperty = mapper.getTableField(table);
            // 判断此表是否存在  若为1则存在此表，若非1则不存在
            if ((mapper.decision(table) != 1) & fieldProperty.size() > 0) {
                // 创建数据表
                System.out.println("字段:" + fieldProperty);
                String establishSql = ""; // 待执行的语句
                for (Map<String, Object> field : fieldProperty) {
                    establishSql = establishSql + (String) field.get("FIE_NAME") + " " + (String) field.get("FIE_TYPE") + "(" + Integer.parseInt((String) field.get("FIE_LEN")) + "),";
                }
                establishSql = "CREATE TABLE " + table + "(" + establishSql.substring(0, establishSql.length() - 1) + ")"; //创建表的语句
                mapper.execute(establishSql); // 创建数据表、直接执行即可 可能判断列重复的
                establishSql = "COMMENT ON TABLE " + table + " IS " + "'" + tableNameFormat.get(0).get("TAB_DES") + "'";
                mapper.execute(establishSql); // 向表添加注解、直接执行即可

                // 向字段添加注解
                for (Map<String, Object> field : fieldProperty) {
                    mapper.addFieldAnnotation(table, (String) field.get("FIE_NAME"), (String) field.get("FIE_DES"));
                }

                // 向表设置唯一索引
                String indexs = "";
                for (Map<String, Object> field : fieldProperty) {
                    if (field.get("IND_FLAG").equals("Y")) {
                        indexs = indexs + field.get("FIE_NAME") + ",";
                    }
                }
                // 判断是否含有索引
                if (indexs.length() > 0) {
                    indexs = indexs.substring(0, indexs.length() - 1);
                    indexs = "CREATE UNIQUE INDEX " + "UI_" + table + " ON " + table + "(" + indexs + ")";
                    mapper.execute(indexs); // 向表添加唯一索引、直接执行即可
                } else {
                    System.out.println("无设置唯一索引!!!");
                }

                return "创建表已成功!";
            } else return "此表存在或未找到建表对应的属性值!";
        }
        return null;
    }

    /**
     * 维护数据表接口
     *
     * @param dbsName
     * @param password
     * @param table
     */
    @GetMapping("/tableMaintain")
    public String tableMaintain(@RequestParam("dbsName") String dbsName,
                                @RequestParam("password") String password,
                                @RequestParam("table") String table,
                                @RequestParam("signboard") String signboard) {
        UserPO Dbs = userService.getDataByUsername(dbsName);
        log.debug("dbs:{}", Dbs);
        mgr.getDataSource(Dbs, password);
        BaseMapper mapper = mgr.getMapper(Dbs.getUsrName(), "baseDmlMapper");
        // 获取DEV_TAB_D中的值
        List<Map<String, Object>> fieldMap = mapper.getTableField(table);
        System.out.println("DEV_TAB_D:\n\n" + fieldMap + "\n\n");
        // 获取information_schema.columns中的值---Mysql
        if (Dbs.getDbsType().equals(Manager.MYSQL)) {
            // 获取公共部分
            fieldMysqlUserCols = mapper.getMysqlTableField(table);
            System.out.println("从Mysql中查询的值:\n" + fieldMysqlUserCols + "\n");
        }
        // 获取USER_TAB_COLS中的值---Oracle
        else if (Dbs.getDbsType().equals(Manager.ORACLE)) {
            fieldOracleUserCols = mapper.getOracleTableField(table);
            System.out.println("从Oracle中查询的值:\n" + fieldOracleUserCols + "\n");
        }
        /**
         * 调用添加字段函数+更新字段函数
         */
        else if (signboard.equals(updateField) || signboard.equals(addField)) {
            // 调用mysql接口
            if (Dbs.getDbsType().equals(Manager.MYSQL)) {
                System.out.println("基于Mysql的字段更新");
            }
            // 调用Oracle接口
            else if (Dbs.getDbsType().equals(Manager.ORACLE)) {
                OracleBaseMapper mapperOracle = mgr.getMapper(Dbs.getUsrName(), "baseDmlMapper");
                for (Map<String, Object> fieldD : fieldMap) {
                    for (Map<String, Object> fieldU : fieldOracleUserCols) {
                        // 更新
                        if (fieldD.get("FIE_NAME").equals(fieldU.get("COLUMN_NAME"))) {
                            // 更新表字段:字段类型、字段大小
                            mapperOracle.updateField(table, (String) fieldD.get("FIE_NAME"), (String) fieldD.get("FIE_TYPE"), (int) fieldD.get("FIE_LEN"));
                            // 更新注释
                            mapperOracle.addFieldAnnotation(table, (String) fieldD.get("FIE_NAME"), (String) fieldD.get("FIE_DES"));
                        }
                        // 增加
                        else {
                            //  添加字段
                            mapperOracle.addField(table, (String) fieldD.get("FIE_NAME"), (String) fieldD.get("FIE_TYPE"), Integer.parseInt((String) fieldD.get("FIE_LEN")));
                            // 添加注释
                            mapperOracle.addFieldAnnotation(table, (String) fieldD.get("FIE_NAME"), (String) fieldD.get("FIE_DES"));
                            //添加唯一索引
                            if (fieldD.get("IND_FLAG").equals("Y")) {
                                mapperOracle.addFieldIndex(table, (String) fieldD.get("FIE_NAME"));
                            }
                        }
                    }
                }
            }

        }

        /**
         * 调用删除字段
         */
        else if (signboard.equals(deleteField)) {
            // 调用mysql删除字段接口
            if (Dbs.getDbsType().equals(Manager.MYSQL)) {
                // Mysql的未添加
            }
            // 调用Oracle删除字段接口
            else if (Dbs.getDbsType().equals(Manager.ORACLE)) {
                OracleBaseMapper mapperOracle = mgr.getMapper(Dbs.getUsrName(), "baseDmlMapper");
                for (Map<String, Object> fieldU : fieldOracleUserCols) {
                    for (Map<String, Object> fieldD : fieldMap) {
                        if (fieldU.get("COLUMN_NAME").equals(fieldD.get("FIE_NAME"))) {
                            //  在当前创建的数据表中此删除字段
                            mapperOracle.deleteField(table, (String) fieldU.get("COLUMN_NAME"));
                            // 删除数据字典定义中相应表记录/删除明细表记录
                            userService.deleteField(table, (String) fieldU.get("COLUMN_NAME"));
                        }
                    }
                }
            }
        }
        // 未找到合适的操作符
        else {
            return "请您输入正确的标识符!";
        }
        return null;
    }

    /**
     * 功能: 删除数据表接口
     *
     * @param dbsName
     * @param password
     * @param table
     */
    @GetMapping("/tableDelete")
    public String tableDelete(@RequestParam("dbsName") String dbsName,
                              @RequestParam("password") String password,
                              @RequestParam("table") String table) {
        UserPO Dbs = userService.getDataByUsername(dbsName);

        log.debug("dbs:{}", Dbs);
        mgr.getDataSource(Dbs, password);
        // 链接Mysql数据源
        if (Dbs.getDbsType().equals(Manager.MYSQL)) {
            MySQLBaseMapper mapper = mgr.getMapperMySQLBaseMapper(Dbs.getUsrName(), "baseDmlMapper");
            if (mapper.decision(table) == DECISION) {
                //  删除当前所创建的数据表
                mapper.delete(table);
                // 删除头表记录
                userService.deleteTableData("dev_tab_h", table);
                // 删除明细表记录
                userService.deleteTableData("dev_tab_d", table);
                System.out.println("Mysql删除部分:" + table);
            } else return "表" + table + "未存在!";
        }

        // 链接Oracle数据源
        if (Dbs.getDbsType().equals(Manager.ORACLE)) {
            OracleBaseMapper mapper = mgr.getMapperOracleBaseMapper(Dbs.getUsrName(), "baseDmlMapper");
            if (mapper.decision(table) == DECISION) {
                mapper.delete(table);
                // 删除头表记录
                userService.deleteTableData("dev_tab_h", table);
                // 删除明细表记录
                userService.deleteTableData("dev_tab_d", table);
            } else return "表" + table + "未存在!";
        }
        return "表" + table + "删除成功!";
    }

    /**
     * 查询数据表接口1
     */
    //@GetMapping("/tabaleInquireH")
    public List<Map<String, Object>> tabaleInquireH(String dbsName, String password, String table) {
        System.out.println("参数:" + table.length());
        UserPO Dbs = userService.getDataByUsername(dbsName);
        log.debug("dbs:{}", Dbs);
        mgr.getDataSource(Dbs, password);
        System.out.println("查询1执行中" + Dbs.getDbsType());
        //  链接Mysql数据源
        if (Dbs.getDbsType().equals(Manager.MYSQL)) {
            MySQLBaseMapper mapper = mgr.getMapperOracleBaseMapper(Dbs.getUsrName(), "baseDmlMapper");
            if (mapper.decision(table) == DECISION) {
                // 有参数查询
                if (table.length() > 0) {
                    System.out.println("有参数的情况下！！！");
                    return mapper.inquireHParameter(table);
                }
                // 无参数查询
                else if (table.length() == 0) {
                    System.out.println("无参数情况下！！！");
                    return mapper.inquireHNotParameter();
                }
            }
        }
        // 链接Oracle数据源  自测没问题
        else if (Dbs.getDbsType().equals(Manager.ORACLE)) {
            OracleBaseMapper mapper = mgr.getMapperOracleBaseMapper(Dbs.getUsrName(), "baseDmlMapper");

            // 有参数查询
            if (table.length() > 0) {
                System.out.println("有参数的情况下！！！");
                return mapper.inquireHParameter(table);
            }
            // 无参数查询
            else if (table.length() == 0) {
                System.out.println("无参数的情况下！！！");
                return mapper.inquireHNotParameter();
            }
        }
        return null;
    }

    /**
     * 查询数据表接口2  Mysql和Oracle公用方法
     * 自测没问题
     *
     * @param dbsName
     * @param password
     * @param table
     */
    @GetMapping("/tabaleInquireD")
    public List<Map<String, Object>> tabaleInquireD(@RequestParam("dbsName") String dbsName,
                                                    @RequestParam("password") String password,
                                                    @RequestParam("table") String table) {
        UserPO Dbs = userService.getDataByUsername(dbsName);
        log.debug("dbs:{}", Dbs);
        mgr.getDataSource(Dbs, password);
        BaseMapper mapper = mgr.getMapperOracleBaseMapper(Dbs.getUsrName(), "baseDmlMapper");
        return mapper.inquireD(table);
    }

    /**
     * 关闭数据源
     * 自测没问题
     *
     * @param password
     */
    //@GetMapping("/closeDataSource")
    public Object closeDataSource(String dbsName, String password) {
        UserPO Dbs = userService.getDataByUsername(dbsName);
        log.debug("dbs:{}", Dbs);
        mgr.getDataSource(Dbs, password);
        mgr.closeDataSource(Dbs.getUsrName(), password);
        return "数据源已关闭";
    }
}
