package com.cmk.utc.db.datasource.pool.impl;


import java.beans.PropertyVetoException;

import javax.sql.DataSource;

import com.cmk.utc.db.datasource.model.DBConfig;
import com.cmk.utc.db.datasource.pool.IPool;
import com.cmk.utc.util.CommonUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;


public class C3p0Impl implements IPool {

    public DataSource loadSource(DBConfig config) {
        ComboPooledDataSource cpds = new ComboPooledDataSource(true);

        cpds.setJdbcUrl(config.getUrl());
        try {
            cpds.setDriverClass(config.getDriver());
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        cpds.setUser(config.getUserName());
        cpds.setPassword(config.getPassword());
        cpds.setMaxPoolSize(100);
        cpds.setMaxIdleTime(30);
        cpds.setMaxStatements(1000);

        // cpds.setMaxPoolSize(Integer.valueOf(ConstantUtils.getValue("c3p0.maxPoolSize").toString()));
        // cpds.setMinPoolSize(Integer.valueOf(ConstantUtils.getValue("c3p0.minPoolSize").toString()));
        // cpds.setAcquireIncrement(Integer.valueOf(ConstantUtils.getValue("c3p0.acquireIncrement").toString()));
        // cpds.setInitialPoolSize(Integer.valueOf(ConstantUtils.getValue("c3p0.initialPoolSize").toString()));
        // cpds.setMaxIdleTime(Integer.valueOf(ConstantUtils.getValue("c3p0.maxIdleTime").toString()));

        if (checkConfigExist(config, "initialSize")) {
            cpds.setInitialPoolSize(config.getProperties("initialSize", 0));
        }
        if (checkConfigExist(config, "maxIdle")) {
            cpds.setMaxPoolSize(config.getProperties("maxIdle", 0));
        }
        if (checkConfigExist(config, "minIdle")) {
            cpds.setMinPoolSize(config.getProperties("minIdle", 0));
        }
        return cpds;
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
