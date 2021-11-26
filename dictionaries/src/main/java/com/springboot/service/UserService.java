package com.springboot.service;
import com.springboot.po.UserPO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shajiu
 * @date 2021/11/5 13:39
 */
@Service
public interface UserService {
    // 获取初始链接数据源信息
    UserPO getDataByUsername(String dbsName);

    // 删除数据表中的字段
    void deleteField(String tabname,String field);

    // 删除头表记录和删除明细表记录
    void deleteTableData(String DevTab,String tabname);

    List<UserPO> getNameByUsername(UserPO userpo);
}
