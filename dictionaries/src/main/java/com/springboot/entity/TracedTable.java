package com.springboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shajiu
 * @date 2021/11/9 14:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TracedTable {

    private String TAB_NAME;
    private String TAB_DES;
    private String SYS_CODE;
    private String TAB_REC;
    private String UPD_CODE;
    private String UPD_NAME;
    private String UPD_TIME;
    private String UU_ID;
}
