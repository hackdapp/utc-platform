package com.cmk.utc.db.datasource.pool.impl;



import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.cmk.utc.db.datasource.model.DBConfig;
import com.cmk.utc.db.datasource.pool.IPool;
import com.cmk.utc.util.CommonUtil;


/**
 * 功能简述:〈一句话描述〉 详细描述:〈功能详细描述〉
 * 
 * @author LIFE2014
 * @version 2014-5-9
 * @see DruidImpl
 * @since
 */
public class DruidImpl implements IPool {

    public DataSource loadSource(DBConfig config) {
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(config.getDriver());
        ds.setUrl(config.getUrl());
        ds.setUsername(config.getUserName());
        ds.setPassword(config.getPassword());

        if (checkConfigExist(config, "maxActive")) {
            ds.setMaxActive(config.getProperties("maxActive", 0));
        }
        if (checkConfigExist(config, "initialSize")) {
            ds.setInitialSize(config.getProperties("initialSize", 0));
        }
        if (checkConfigExist(config, "maxWait")) {
            ds.setMaxWait(config.getProperties("maxWait", 0));
        }
        if (checkConfigExist(config, "maxIdle")) {
            ds.setMaxIdle(config.getProperties("maxIdle", 0));
        }
        if (checkConfigExist(config, "minIdle")) {
            ds.setMinIdle(config.getProperties("minIdle", 0));
        }
        if (checkConfigExist(config, "removeAbandoned")) {
            ds.setRemoveAbandoned(config.getProperties("removeAbandoned", false));
        }
        if (checkConfigExist(config, "removeAbandonedTimeout")) {
            ds.setRemoveAbandonedTimeout(config.getProperties("removeAbandonedTimeout", 0));
        }
        if (checkConfigExist(config, "testOnReturn")) {
            ds.setTestOnReturn(config.getProperties("testOnReturn", false));
        }
        if (checkConfigExist(config, "testOnBorrow")) {
            ds.setTestOnBorrow(config.getProperties("testOnBorrow", false));
        }
        if (checkConfigExist(config, "testWhileIdle")) {
            ds.setTestWhileIdle(config.getProperties("testWhileIdle", false));
        }
        if (checkConfigExist(config, "validationQuery")) {
            ds.setValidationQuery(config.getProperties("validationQuery", ""));
        }
        if (checkConfigExist(config, "validationQueryTimeout")) {
            ds.setValidationQueryTimeout(config.getProperties("validationQueryTimeout", 0));
        }
        if (checkConfigExist(config, "timeBetweenEvictionRunsMillis")) {
            ds.setTimeBetweenEvictionRunsMillis(config.getProperties("timeBetweenEvictionRunsMillis", 0));
        }
        if (checkConfigExist(config, "numTestsPerEvictionRun")) {
            ds.setNumTestsPerEvictionRun(config.getProperties("numTestsPerEvictionRun", 0));
        }
        return ds;
    }

    /**
     * check datasource'config
     * 
     * @param config DBConfig
     * @param itemName itemCode
     * @return boolean
     * @see
     */
    public boolean checkConfigExist(DBConfig config, String itemName) {
        return !CommonUtil.isNullOrEmpty(config.getProperties(itemName, ""));
    }
}
