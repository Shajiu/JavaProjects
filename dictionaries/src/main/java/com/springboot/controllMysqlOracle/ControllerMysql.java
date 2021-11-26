package com.springboot.controllMysqlOracle;

/**
 * @author shajiu
 * @date 2021/11/10 14:15
 * 连接Mysql数据库
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ControllerMysql {
    public static final String MYSQL_URL = "jdbc:mysql://%s:%d/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    public static void main(String[] args) {
        //MySql8.0以下版本-jdbc驱动名及数据库url
        final String jdbc_driver = "com.mysql.jdbc.Driver";
        String ip="192.168.2.186";
        String port="3306";
        String name="test";
        System.out.println();
        final String db_url=String.format(MYSQL_URL, ip, Integer.parseInt(port), name);
        final String user = "root";
        final String password = "root";
        Connection conn = null;
        try {
            //注册JDBC驱动
            Class.forName(jdbc_driver);
            //打开链接
            conn = DriverManager.getConnection(db_url, user, password);
            /**
             * 调用增删改除操作接口如下
             * 分别传入conn和tableName
             */
            System.out.println("Mysql连接成功"+conn);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
