package com.cmk.utc.db.datasource.model;


import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.cmk.utc.util.CommonUtil;


/**
 * 数据源对象
 * 
 * @author LIFE2014
 * @version 2014-6-18
 * @see DataSourceMeta
 * @since
 */
public class DataSourceMeta {

    /**
     * DataSource<br>
     */
    private DataSource sqlds;

    /**
     * 数据源名称<br>
     */
    private String name;

    /**
     * 数据库类型<br>
     */
    private String dbType;

    public DataSourceMeta(String name, DataSource sqlds) {
        this.name = name;
        this.sqlds = sqlds;
    }

    /**
     * 获取新的连接
     * 
     * @return Connection
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return this.sqlds.getConnection();
    }

    /**
     * 获取数据库类型
     * 
     * @return String
     */
    public String getDbType() {
        if (CommonUtil.isNullOrEmpty(this.dbType)) {
            Connection conn = null;
            try {
                conn = sqlds.getConnection();
                this.dbType = conn.getMetaData().getDatabaseProductName();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return this.dbType;
    }

    /**
     * 获取数据源名称
     * 
     * @return String
     */
    public String getName() {
        return this.name;
    }
}
