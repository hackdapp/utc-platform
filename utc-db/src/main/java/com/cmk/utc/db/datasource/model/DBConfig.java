package com.cmk.utc.db.datasource.model;


import java.util.Properties;

import com.cmk.utc.util.Cast;


/**
 * 数据库配置
 * 
 * @author LIFE2014
 * @version 2014-5-9
 * @see DBConfig
 * @since
 */
public class DBConfig {
    /**
     * 驱动类名<br>
     */
    private String driver;

    /**
     * 数据库链接<br>
     */
    private String url;

    /**
     * 用户名<br>
     */
    private String userName;

    /**
     * 用户密码<br>
     */
    private String password;

    /**
     * 是否缺省<br>
     */
    private boolean isDefault;

    /**
     * 其它配置<br>
     */
    private Properties configuration;

    /**
     * @param driver
     * @param url
     * @param userName
     * @param password
     */
    public DBConfig(String driver, String url, String userName, String password) {
        this(driver, url, userName, password, false, null);
    }

    /**
     * @param driver 驱动类名
     * @param url 数据库链接
     * @param userName 用户名
     * @param password 用户密码
     * @param isDefault 是否缺省
     * @param confProperties 其它配置
     * @param configuration
     */
    public DBConfig(String driver, String url, String userName, String password,
                    boolean isDefault, Properties configuration) {
        this.driver = driver;
        this.url = url;
        this.userName = userName;
        this.password = password;
        this.isDefault = isDefault;
        this.configuration = configuration;
    }

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public Properties getConfiguration() {
        return configuration;
    }

    /**
     * @param name
     * @param defaultValue
     * @return
     * @see
     */
    public String getProperties(String name, String defaultValue) {
        if (configuration != null) {
            return configuration.getProperty(name, defaultValue);
        }
        return defaultValue;
    }

    /**
     * @param name
     * @param defaultValue
     * @return
     * @see
     */
    public boolean getProperties(String name, boolean defaultValue) {
        return Cast.to(getProperties(name, ""), defaultValue);
    }

    /**
     * @param name
     * @param defaultValue
     * @return
     * @see
     */
    public int getProperties(String name, int defaultValue) {
        return Cast.to(getProperties(name, ""), defaultValue);
    }

    public boolean isDefault() {
        return isDefault;
    }
}
