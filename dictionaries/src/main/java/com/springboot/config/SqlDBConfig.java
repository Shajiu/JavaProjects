package com.springboot.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author shajiu
 * @date 2021/11/8 17:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SqlDBConfig {
    String uuId;
    String url;
    String usrName;
    String password;
}

