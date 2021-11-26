package com.springboot.service.impl;

import com.springboot.po.UserPO;
import com.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.springboot.mapper.UserMapper;
import java.util.List;

/**
 * @author shajiu
 * @date 2021/11/5 13:47
 * 在service接口的实现类中，要加上@Service注解，把实现类交给spring处理
 */
@Service
public class UserServiceImpl implements UserService {
    //通过@Autowired注解获得自动注入的userMapper实现类，在重写的方法中进行调用，获得数据。
    @Autowired
    UserMapper userMapper;

    // 获取初始链接时的数据源信息
    @Override
    public UserPO getDataByUsername(String dbsName) {
        return userMapper.getDataByUsername(dbsName);
    }

    // 删除数据字典定义中相应表记录
    @Override
    public void deleteField(String tabname,String field){
        userMapper.deleteField(tabname,field);
    }

    // 删除头表记录和删除明细表记录
    @Override
    public void deleteTableData(String DevTab,String tabname){
        userMapper.deleteTableData(DevTab,tabname);
    }

     @Override
    public List<UserPO> getNameByUsername(UserPO userpo){
        return userMapper.getNameByUsername(userpo);
     }
}
