package com.springboot.entity;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import com.springboot.config.SqlDBConfig;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.springboot.util.MD5Util;
import org.springframework.beans.factory.annotation.Value;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author shajiu
 * @date 2021/11/9 11:06
 * Druid数据库连接池配置文件
 */
@Slf4j
public class JdbcDataSource extends DruidDataSource {
    @Value("${spring.datasource.type}")
    private String type;

    @Value("${spring.datasource.druid.max-active:10}")
    private int maxActive=10;

    @Value("${spring.datasource.druid.initial-size:5}")
    private int initialSize;

    @Value("${spring.datasource.druid.min-idle:3}")
    private int minIdle;

    @Value("${spring.datasource.druid.max-wait:30000}")
    private int maxWait;

    // 指定空闲连接检查、废弃连接清理、空闲连接池大小调整之间的操作时间间隔
    @Value("${spring.datasource.druid.time-between-eviction-runs-millis:600000}")
    private int timeBetweenEvictionRunsMillis=600000;

    //指定一个空闲连接最少空闲多久后可被清除
    @Value("${spring.datasource.druid.min-evictable-idle-time-millis:1800000}")
    private int minEvictableIdleTimeMillis=1800000;

    @Value("${spring.datasource.druid.test-while-idle:true}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.druid.test-on-borrow:false}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.druid.test-on-return:false}")
    private boolean testOnReturn;

    @Value("${spring.datasource.druid.filters:}")
    private String filters;


    @Value("${spring.datasource.druid.break-after-acquire-failure:true}")
    private boolean breakAfterAcquireFailure;

    @Value("${spring.datasource.druid.connection-error-retry-attempts:0}")
    private int connectionErrorRetryAttempts;

    public static volatile Map<String, DruidDataSource> dataSources = new ConcurrentHashMap<>();

    //  针对数据关闭以及查询状态的函数
    public DruidDataSource getDataSource(String username, String password) {
        String key = getKey(username, password);
        return dataSources.get(key);
    }

    // Druid 连接池配置 主数据源:前端埋点数据相关
    public DruidDataSource getDataSource(SqlDBConfig sqlDBConfig) {
        String jdbcUrl = sqlDBConfig.getUrl();
        String username = sqlDBConfig.getUsrName();
        String password = sqlDBConfig.getPassword();
        //构建相应格式并对用户名和密钥进行加密
        String key = getKey(username, password);
        if (!dataSources.containsKey(key) || null == dataSources.get(key)) {
            DruidDataSource instance = new JdbcDataSource();
            String className = null;
            try {
                // 获取数据库源的driverName值
                className = DriverManager.getDriver(jdbcUrl.trim()).getClass().getName();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (StringUtils.isEmpty(className)) {
                throw new RuntimeException("数据库源的驱动为空: DbId=" + sqlDBConfig.getUuId());
            } else {
                // 加载数据源驱动
                instance.setDriverClassName(className);
            }

            // 启动加载
            instance.setUrl(jdbcUrl.trim());
            // 用户名
            instance.setUsername(username);
            // 密钥
            instance.setPassword(password);
            // 初始尺寸
            instance.setInitialSize(initialSize);
            // 最小空闲
            instance.setMinIdle(minIdle);
            // 自启动以来活动的进程数最大值
            instance.setMaxActive(maxActive);
            // 最长等待时间
            instance.setMaxWait(maxWait);
            // 在驱逐之间运行时间
            instance.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
            instance.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
            instance.setTestWhileIdle(false);
            // 测试点阶段
            instance.setTestOnBorrow(testOnBorrow);
            // 测试点返回
            instance.setTestOnReturn(testOnReturn);
            if (!isBlank(filters)) {
                try {
                    instance.setFilters(filters);
                } catch (SQLException e) {
                    log.error("setFilters error", e);
                }
            }
            // 设置连接错误重试次数
            instance.setConnectionErrorRetryAttempts(connectionErrorRetryAttempts);
            // 在获取失败后设置Break
            instance.setBreakAfterAcquireFailure(breakAfterAcquireFailure);
            try {
                instance.init();
            } catch (Exception e) {
                log.error("数据池初始化失败!", e);
                return null;
            }
            dataSources.put(key, instance);

        }
        return dataSources.get(key);

    }

    private String getKey(String username, String password) {
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.isEmpty(username)) {
            sb.append(username);

        }
        if (!StringUtils.isEmpty(password)) {
            sb.append(":").append(password);
        }
        return MD5Util.MD5Encode(sb.toString(), "");
    }
}
