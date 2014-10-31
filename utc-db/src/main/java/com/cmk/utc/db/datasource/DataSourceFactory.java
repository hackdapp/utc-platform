package com.cmk.utc.db.datasource;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.rmi.activation.Activator;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.cmk.utc.db.datasource.config.IXmlConfig;
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
     * 模块<br>
     */
    private static final String MODULE = DataSourceFactory.class.getName();

    /**
     * 数据源缓存<br>
     */
    private static Map<String, DataSourceMeta> dsMap = new ConcurrentHashMap<String, DataSourceMeta>();

    /**
     * 意义，目的和功能，以及被用到的地方<br>
     */
    private static DataSourceFactory dataSourceFactory = null;

    /**
     * 缺少数据源名称<br>
     */
    private static String defaultDsName;

    /**
     * 数据库配置文件<br>
     */
    private static String confxml = "db.xml";

    private static IPool ipool;

    private DataSourceFactory() {
        InputStream dbStream = load(bundleContext);
        IXmlConfig xmlConfig = new PoolConfigImpl();
        Map xmlMap = xmlConfig.load(dbStream);
        String className = (String)xmlMap.get("provider");
        if (CommonUtil.isNullOrEmpty(className)) {
            className = C3p0Impl.class.getName();
        }
        try {
            ipool = (IPool)ClassUtil.createObject(className);
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

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

    
    public void load(String xml){
        
    }
    
    public void registerDBDataSouce(){
        
    }
    /**
     * 获取数据源实例
     * 
     * @param ds 数据源
     * @return DataSourceMeta
     * @see
     */
    public DataSourceMeta getDataSourceMeta(String ds) {
        if (!dsMap.containsKey(ds)) {
            registerDBDataSouce(ds);
        }
        return dsMap.get(ds);
    }

    public DataSourceMeta getDefaultDataSourceMeta() {
        return getDataSourceMeta(defaultDsName);
    }

    /**
     * @see
     */
    public void start() {
        IXmlConfig xmlConfig = new DBConfigImpl();
        InputStream dbStream = load(bundleContext);
        Map<String, DBConfig> mapConfig = xmlConfig.load(dbStream);
        for (Entry<String, DBConfig> tmpDbConfig : mapConfig.entrySet()) {
            String name = tmpDbConfig.getKey();
            DBConfig tmpConfig = tmpDbConfig.getValue();
            if (tmpConfig.isDefault()) {
                defaultDsName = name;
            }
            install(defaultDsName, tmpConfig);
        }
    }

    /**
     * @see
     */
    public void stop() {
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
                System.out.println("Datasource:" + ds + "-> Connection is failed, check the network or configuration information.");
            } else {
                dsMap.put(ds, new DataSourceMeta(ds, ipool.loadSource(dbConfig)));
                DataSourceMeta tmpMeta = dsMap.get(ds);
                System.out.println("dynamic load datasource: " + tmpMeta.getDbType() + " > " + tmpMeta.getName());
            }
        } else {
            System.out.println("The datasource has been loaded.");
        }
    }

    /**
     * @param dbConfig DBConfig
     * @return boolean
     * @see
     */
    private boolean testConnection(DBConfig dbConfig) {
        try {
            Class.forName(dbConfig.getDriver());
            Connection connection = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUserName(), dbConfig.getPassword());
            if (connection != null) {
                connection.close();
                return true;
            }
        } catch (SQLException e) {
            System.out.println( "The database connection Failed. url:" + dbConfig.getUrl() + " user:" + dbConfig.getUserName());
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("the driver class[" + dbConfig.getDriver() + "] is not exist.");
            e.printStackTrace();
        }
        return false;
    }
}
