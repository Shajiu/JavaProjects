package com.example.mybatis_plus.mybatis_plus_learn.model;


import java.util.Date;

/**
 * @author shajiu
 * @date 2021/12/9 11:24
 */
public class UserBean {
    public String id;
    public String name;
    public String score;
    public Date tim;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Date getTim() {
        return tim;
    }

    public void setTim(Date tim) {
        this.tim = tim;
    }
}
