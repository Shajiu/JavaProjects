package com.springboot.service;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.springboot.mapper.*;
import com.springboot.po.UserPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.springboot.config.SqlDBConfig;
import com.springboot.entity.Def;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.springboot.entity.JdbcDataSource;

import javax.sql.DataSource;

/**
 * @author shajiu
 * @date 2021/11/9 8:42
 */

@Service
@Slf4j
public class Manager {
    /**
     * 数据源连接接口
     */
    /**
     * 定义不同类型的数据源
     */
    public static final String MYSQL = "1";
    public static final String ORACLE = "2";
    public static final String MYSQL_URL = "jdbc:mysql://%s:%d/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    public static final String ORACLE_URL = "jdbc:oracle:thin:@%s:%d:%s";
    public static volatile Map<String, Def> dsDefPool = new ConcurrentHashMap<>();
    @Autowired(required = false)
    public JdbcDataSource jdbcDataSource;


    public static String getJdbcUrl(String dbType, String ip, String port, String name) {
        if (MYSQL.equalsIgnoreCase(dbType)) {
            return String.format(MYSQL_URL, ip, Integer.parseInt(port), name);
        } else if (ORACLE.equalsIgnoreCase(dbType)) {
            return String.format(ORACLE_URL, ip, Integer.parseInt(port), name);
        } else return "目前还未找到合适的数据源!";
    }

    private void registerMapper(UserPO devDbs, DataSource ds) {
        Def dsDef = new Def(devDbs);
        String dbtype = devDbs.getDbsType();
        SqlSession sqlSession = new SqlSessionTemplate(createSqlSessionFactory(devDbs.getUsrName(), dbtype, ds));
        // 关联该数据源对应的mapper
        BaseMapper base = sqlSession.getMapper(getSpecialBaseMapper(dbtype));
        MetaMapper meta = sqlSession.getMapper(getSpecialMetaMapper(dbtype));
        dsDef.setBase(base);
        dsDef.setMeta(meta);
        dsDefPool.put(devDbs.getUsrName(), dsDef);
    }

    /**
     * <p>获取数据源连接池信息，不存在则创建</p>
     *
     * @param Dbs 数据库配置信息
     * @return
     * @author zhangxm
     * @date 2021-9-10 11:00
     * @since 1.0.0
     */

    public DruidDataSource getDataSource(UserPO Dbs, String password) {
        SqlDBConfig sqlDBConfig = new SqlDBConfig(Dbs.getUuId(), getJdbcUrl(Dbs), Dbs.getUsrName(), password);
        if (jdbcDataSource == null) {
            jdbcDataSource = new JdbcDataSource();
        }
        DruidDataSource dataSource = jdbcDataSource.getDataSource(sqlDBConfig);
        // 此处调用Mapper函数
        if (!dsDefPool.containsKey(Dbs.getUsrName()) || dsDefPool.get(Dbs.getUsrName()) == null) {
            registerMapper(Dbs, dataSource);
            log.debug("注册mapper:{}", Dbs.getUsrName());
        }
        return dataSource;
    }

    public static String getJdbcUrl(UserPO Dbs) {
        return getJdbcUrl(Dbs.getDbsType(), Dbs.getIp(), Dbs.getPort(), Dbs.getName());
    }

    /**
     * <p>创建SqlSessionFactory</p>
     *
     * @param username 数据库用户名
     * @param dbtype   数据库类型 用于获取对应的Mapper
     * @param ds       数据库连接池
     * @return
     * @author zhangxm
     * @date 2021-9-10 14:55
     * @since 1.0.0
     */
    private static SqlSessionFactory createSqlSessionFactory(String username, String dbtype, DataSource ds) {
        Configuration config = new Configuration();
        config.setCallSettersOnNulls(true);
        config.setEnvironment(new Environment(username, new JdbcTransactionFactory(), ds));
        config.addMapper(BaseMapper.class);
        config.addMapper(getSpecialBaseMapper(dbtype));
        config.addMapper(MetaMapper.class);
        config.addMapper(getSpecialMetaMapper(dbtype));
        config.addInterceptor(new PaginationInterceptor());
        return new SqlSessionFactoryBuilder().build(config);
    }

    public static Class<? extends BaseMapper> getSpecialBaseMapper(String dbtype) {
        switch (dbtype) {
            case MYSQL:
                // 1表示mysql
                return MySQLBaseMapper.class;
            case ORACLE:
                // 2表示oracle
                return OracleBaseMapper.class;
            default:
                break;
        }
        return null;
    }

    public static Class<? extends MetaMapper> getSpecialMetaMapper(String dbtype) {
        switch (dbtype) {
            case MYSQL:
                System.out.println("调用mysql数据库对应的Mapper");
                return MySQLMetaMapper.class;
            case ORACLE:
                System.out.println("调用Oracle数据库对应的Mapper");
                return OracleMetaMapper.class;
            default:
                break;
        }
        return null;
    }

    /**
     * 根据数据库用户名获取指定类型的的mapper
     *
     * @param username   数据库用户名
     * @param mapperType mapper类
     * @param <T>
     * @return
     */
    public <T extends BaseMapper> T getMapper(String username, String mapperType) {
        // 判断username非空且数据源有连接
        if (StringUtils.isBlank(username) || !dsDefPool.containsKey(username)) {
            return null;
        }
        if (BaseMapper.name.equals(mapperType)) {
            return (T) dsDefPool.get(username).getBase();
        } else if (mapperType == MetaMapper.name) {
            return (T) dsDefPool.get(username).getMeta();
        } else {
            return null;
        }
    }


    /**
     * 根据数据库用户名获取指定类型的的mapper
     *
     * @param username   数据库用户名
     * @param mapperType mapper类
     * @param <T>
     * @return
     */
    public <T extends BaseMapper> T getMapperMySQLBaseMapper(String username, String mapperType) {
        // 判断username非空且数据源有连接
        if (StringUtils.isBlank(username) || !dsDefPool.containsKey(username)) {
            return null;
        }
        if (MySQLBaseMapper.name.equals(mapperType)) {
            return (T) dsDefPool.get(username).getBase();
        } else if (mapperType == MetaMapper.name) {
            return (T) dsDefPool.get(username).getMeta();
        } else {
            return null;
        }
    }

    public <T extends MetaMapper> T getMapperMySQLMetaMapper(String username, String mapperType) {
        // 判断username非空且数据源有连接
        System.out.println(mapperType);
        System.out.println(MetaMapper.name);
        if (StringUtils.isBlank(username) || !dsDefPool.containsKey(username)) {
            return null;
        }
        if (!MetaMapper.name.equals(mapperType)) {
            return (T) dsDefPool.get(username).getBase();
        } else if (mapperType == MetaMapper.name) {
            return (T) dsDefPool.get(username).getMeta();
        } else {
            return null;
        }
    }


    /**
     * 根据数据库用户名获取指定类型的的mapper
     *
     * @param username   数据库用户名
     * @param mapperType mapper类
     * @param <T>
     * @return
     */
    public <T extends BaseMapper> T getMapperOracleBaseMapper(String username, String mapperType) {
        System.out.println(username);
        System.out.println(mapperType);
        System.out.println(OracleBaseMapper.name);
        // 判断username非空且数据源有连接
        if (StringUtils.isBlank(username) || !dsDefPool.containsKey(username)) {
            return null;
        }
        if (OracleBaseMapper.name.equals(mapperType)) {
            return (T) dsDefPool.get(username).getBase();
        } else if (mapperType == MetaMapper.name) {
            return (T) dsDefPool.get(username).getMeta();
        } else {
            return null;
        }
    }


    /**
     * 返回数据源状态
     * 0-代表已关闭 (stop)  3-代表未知
     *
     * @param username
     * @param password
     * @return
     */
    // 0-代表已关闭 (stop)
    private static final int DBS_STATE_CLOSE = -1;
    // 3-代表未知
    private static final int DBS_STATE_NONE = -2;

    public int readState(String username, String password) {
        DruidDataSource ds = jdbcDataSource.getDataSource(username, password);
        if (ds == null) {
            return DBS_STATE_NONE;
        } else {
            if (ds.isClosed()) {
                return DBS_STATE_CLOSE;
            }
            if (ds.isEnable()) {
                return ds.getActiveCount();
            }
        }
        return -1;
    }

    /**
     * 关闭指定的数据库连接池
     *
     * @param username
     * @param password
     */
    public void closeDataSource(String username, String password) {
        log.debug("关闭数据库连接：{}", username);
        System.out.println("关闭数据库链接!!!");
        DruidDataSource ds = jdbcDataSource.getDataSource(username, password);
        if (ds != null) {
            ds.close();
        }
    }
}
