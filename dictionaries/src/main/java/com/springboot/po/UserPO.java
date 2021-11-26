package com.springboot.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/**
 * @author shajiu
 * @date 2021/11/5 13:34
 * User类implements了一个Serializable类，该类的作用是序列化。
 * 快捷键为：alt+insert
 */
public class UserPO implements Serializable {
    /**
     * 数据库类型
     */
    public String dbsType;

    /**
     * 数据库服务器
     */
    public String ip;

    /**
     * 端口号
     */
    public String port;

    /**
     * 数据库名称
     */
    public String dbsName;

    /**
     * 数据库用户
     */
    public String usrName;

    /**
     * 维护人编码
     */
    public String updCode;

    /**
     * 维护人姓名
     */
    public String updName;

    /**
     * 维护时间
     */
    public String updTime;

    /**
     * 数据标识编码
     */
    public String uuId;

    /**
     * 数据源编码
     */
    public String dbsCode;
    /**
     * 系统编码
     */
    public String sysCode;
    /**
     * 登录密钥
     * @return
     */
    public String password;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 数据源名称
     */
    public String name;

    public String getDbsType() {
        return dbsType;
    }

    public void setDbsType(String dbsType) {
        this.dbsType = dbsType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbsName() {
        return dbsName;
    }

    public void setDbsName(String dbsName) {
        this.dbsName = dbsName;
    }


    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    public String getUpdCode() {
        return updCode;
    }

    public void setUpdCode(String updCode) {
        this.updCode = updCode;
    }

    public String getUpdName() {
        return updName;
    }

    public void setUpdName(String updName) {
        this.updName = updName;
    }

    public String getUpdTime() {
        return updTime;
    }

    public void setUpdTime(String updTime) {
        this.updTime = updTime;
    }

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

    public String getDbsCode() {
        return dbsCode;
    }

    public void setDbsCode(String dbsCode) {
        this.dbsCode = dbsCode;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}