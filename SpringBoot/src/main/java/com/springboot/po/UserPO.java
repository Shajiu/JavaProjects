package com.springboot.po;

import java.io.Serializable;

/**
 * @author shajiu
 * @date 2021/11/5 13:34
 * User类implements了一个Serializable类，该类的作用是序列化。
 */


public class UserPO implements Serializable {
    /**
     * 用户ID
     */
    private int id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 年龄
     */
    private int age;
    /**
     * 性别
     */
    private String sex;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
