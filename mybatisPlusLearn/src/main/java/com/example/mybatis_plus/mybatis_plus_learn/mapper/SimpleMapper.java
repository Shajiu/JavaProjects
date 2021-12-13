package com.example.mybatis_plus.mybatis_plus_learn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mybatis_plus.mybatis_plus_learn.model.UserBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;


/**
 * @author shajiu
 * @date 2021/12/9 11:23
 */
@Repository
@Mapper
public interface SimpleMapper extends BaseMapper<UserBean> {

    @Select("SELECT FIE_NAME FROM dev_tab_d WHERE FIE_NAME='field1'")
    String getName(@Param("name")String name);



    // 删除表
    @Update("DROP TABLE ${tabName}")
    void delete(@Param("tabName") String tabName);
}
