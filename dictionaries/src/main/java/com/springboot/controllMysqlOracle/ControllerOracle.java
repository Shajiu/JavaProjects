package com.springboot.controllMysqlOracle;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
/**
 * @author shajiu
 * @date 2021/11/10 14:33
 */
/**
 * @author shajiu
 * @date 2021/10/25 16:13
 * 原始连接Orcle数据库
 */
@Slf4j
public class ControllerOracle {
    public static void main(String[] args) {
        try {
            Class.forName("oracle.jdbc.OracleDriver");//加载数据库驱动
            System.out.println("加载数据库驱动成功");
            String url = "jdbc:oracle:thin:@192.168.168.105:1521:ZLORANEW";//声明数据库test的url
            String user = "devnew";//数据库的用户名
            String password = "devpass";//数据库的密码
            //建立数据库连接，获得连接对象conn(抛出异常即可)
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("连接数据库成功"+conn);
            /**
             * 调用增删改除操作接口如下
             * 分别传入conn和tableName
             */
            String sql = "SELECT FIE_NAME,FIE_DES,FIE_TYPE,FIE_LEN,IND_FLAG FROM DEV_TAB_D WHERE TAB_NAME ='EDM_COM'";
            //创建该连接下的PreparedStatement对象
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rset = pstmt.executeQuery();
            Statement stmt=conn.createStatement();
            log.debug("\n测试段:{}", rset);
            stmt.executeUpdate(sql);
            conn.close();
            System.out.println("关闭数据库成功");

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
