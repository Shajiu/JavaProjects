package com.springboot.entity;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import com.springboot.po.UserPO;
import com.springboot.mapper.BaseMapper;
import com.springboot.mapper.MetaMapper;
/**
 * @author shajiu
 * @date 2021/11/9 13:45
 */
@Data
public class Def {

    public UserPO devDbs;
    public BaseMapper base = null;
    public MetaMapper meta = null;
    public DruidDataSource ds = null;

    public Def(UserPO devDbs) {
        this.devDbs = devDbs;
    }
}
