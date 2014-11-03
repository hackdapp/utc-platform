package com.cmk.utc.db.datasource;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.cmk.utc.db.datasource.config.impl.DBConfigImpl;
import com.cmk.utc.db.datasource.config.impl.PoolConfigImpl;
import com.cmk.utc.db.datasource.model.DBConfig;
import com.cmk.utc.db.datasource.model.DataSourceMeta;
import com.cmk.utc.db.datasource.pool.IPool;
import com.cmk.utc.db.datasource.pool.impl.C3p0Impl;
import com.cmk.utc.util.ClassUtil;
import com.cmk.utc.util.CommonUtil;


/**
 * 数据源注册服务中心
 * 
 * @author LIFE2014
 * @version 2014-5-9
 * @see DataSourceFactory
 * @since
 */
public final class DataSourceFactory {
    /**
     * 数据源缓存<br>
     */
    private static Map<String, DataSourceMeta> dsMap = new ConcurrentHashMap<String, DataSourceMeta>();

    /**
     * 工厂实例<br>
     */
    private static DataSourceFactory dataSourceFactory = null;

    /**
     * 缺省数据源名称<br>
     */
    private static String defaultDsName;

    /**
     * 连接池<br>
     */
    private static IPool ipool;

    /**
     * 
     */
    private DataSourceFactory() {}

    /**
     * @return DataSourceFactory
     * @see
     */
    public static DataSourceFactory getInstance() {
        if (dataSourceFactory == null) {
            dataSourceFactory = new DataSourceFactory();
        }
        return dataSourceFactory;
    }

    public void load(String xml) throws Exception {
        Map xmlMap = new PoolConfigImpl().load(new FileInputStream(xml));
        String className = (String)xmlMap.get("provider");
        if (CommonUtil.isNullOrEmpty(className)) {
            className = C3p0Impl.class.getName();
        }
        ipool = (IPool)ClassUtil.createObject(className);

        Map<String, DBConfig> mapConfig = new DBConfigImpl().load(new FileInputStream(xml));
        for (Entry<String, DBConfig> tmpDbConfig : mapConfig.entrySet()) {
            DBConfig tmpConfig = tmpDbConfig.getValue();
            if (tmpConfig.isDefault()) {
                defaultDsName = tmpDbConfig.getKey();
            }
            install(tmpDbConfig.getKey(), tmpConfig);
        }
    }

    /**
     * 获取数据源实例
     * 
     * @param ds 数据源
     * @return DataSourceMeta
     * @see
     */
    public DataSourceMeta getDataSourceMeta(String ds) {
        if (ds != null) {
            return dsMap.get(ds);
        }
        return null;
    }

    public DataSourceMeta getDefaultDataSourceMeta() {
        return getDataSourceMeta(defaultDsName);
    }

    /**
     * @see
     */
    public void clear() {
        defaultDsName = null;
        dsMap.clear();
    }

    /**
     * @param ds 数据源
     * @param dbConfig db配置
     * @see
     */
    private void install(String ds, DBConfig dbConfig) {
        if (!dsMap.containsKey(ds) || !testConnection(dbConfig)) {
            boolean isConn = testConnection(dbConfig);

            for (int i = 0; i < 3 && !isConn; i++ ) {
                System.out.println("Connection failed, retry the " + (i + 1) + " connection ....");
                if (!testConnection(dbConfig)) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                isConn = true;
                break;
            }
            if (!isConn) {
                System.out.println("Datasource:"
                                   + ds
                                   + "-> Connection is failed, check the network or configuration information.");
            } else {
                dsMap.put(ds, new DataSourceMeta(ds, ipool.loadSource(dbConfig)));
                DataSourceMeta tmpMeta = dsMap.get(ds);
                System.out.println("dynamic load datasource: " + tmpMeta.getDbType() + " > "
                                   + tmpMeta.getName());
            }
        } else {
            System.out.println("The datasource has been loaded.");
        }
    }

    /**
     * test the Connection.
     * 
     * @param dbConfig DBConfig
     * @return boolean
     * @see
     */
    private boolean testConnection(DBConfig dbConfig) {
        Connection connection = null;
        try {
            Class.forName(dbConfig.getDriver());
            connection = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUserName(),
                dbConfig.getPassword());
            if (connection != null) {
                connection.close();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("The database connection Failed. url:" + dbConfig.getUrl()
                               + " user:" + dbConfig.getUserName());
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("the driver class[" + dbConfig.getDriver() + "] is not exist.");
        }
        return false;
    }
}
