package com.cmk.utc.db;


import com.cmk.utc.db.datasource.model.DataSourceMeta;
import com.cmk.utc.db.executor.ISqlExecutor;


/**
 * @Title IDBServiceFactory.java
 * @author zhangliang
 * @Description: DB服务工具类
 * @version $Rev$
 * @LastModify: $Id$
 */
public interface IDBServiceFactory {

    /**
     * @param ds
     * @return
     */
    public DataSourceMeta getDataSourceMeta(String ds);

    /**
     * @return
     */
    public DataSourceMeta getDefaultDataSourceMeta();

    /**
     * @return
     */
    public ISqlExecutor getISqlExecutor();
}
