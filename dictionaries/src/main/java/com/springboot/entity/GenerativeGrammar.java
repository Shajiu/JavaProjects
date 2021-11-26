package com.springboot.entity;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shajiu
 * @date 2021/11/15 13:31
 */
@Service
public class GenerativeGrammar {
    // 增加字段集
    List<Map<String, Object>> addFields = new ArrayList<>();
    // 修改字段集
    List<Map<String, Object>> updateFields = new ArrayList<>();
    // 删除字段集
    List<Map<String, Object>> deleteFields = new ArrayList<>();
    // 维护字段集
    Map<String, List<Map<String, Object>>> MaintainAuthorizationFields = new HashMap<String, List<Map<String, Object>>>();

    // 遍历字段 Map<String, List<Map<String, Object>>>
    public Map<String, List<Map<String, Object>>> traverseField(List<Map<String, Object>> fieldTableCols, List<Map<String, Object>> fieldUserCols) {
        System.out.println("\n" + fieldTableCols);
        System.out.println("\n" + fieldUserCols);


        // 遍历R1到R2中
        for (int i = 0; i < fieldTableCols.size(); i++) {
            for (int j = 0; j < fieldUserCols.size(); j++) {
                // R1中的每个字段存在于R2中
                if (fieldTableCols.get(i).get("FIE_NAME").equals(fieldUserCols.get(j).get("COLUMN_NAME"))) {
                    //  更新字段部分
                    System.out.println("更新字段:" + fieldTableCols.get(i));
                    updateFields.add(fieldTableCols.get(i));
                }
                // R1中的每个字段不在R2中
                else {
                    //  增加字段部分
                    System.out.println("增加字段:" + fieldTableCols.get(i));
                    addFields.add(fieldTableCols.get(i));
                }
            }
        }

        // 遍历R2到R1中
        for (int j = 0; j < fieldUserCols.size(); j++) {
            for (int i = 0; i < fieldTableCols.size(); i++) {
                if (!fieldTableCols.get(j).get("COLUMN_NAME").equals(fieldTableCols.get(i).get("FIE_NAME"))) {
                    // 删除字段部分
                    System.out.println("增加字段:" + fieldTableCols.get(j));
                    deleteFields.add(fieldTableCols.get(j));
                }
            }
        }

        // 添加维护数据表字段集
        MaintainAuthorizationFields.put("updateFields", updateFields);
        MaintainAuthorizationFields.put("addFields", addFields);
        MaintainAuthorizationFields.put("deleteFields", deleteFields);
        return MaintainAuthorizationFields;
    }
}
