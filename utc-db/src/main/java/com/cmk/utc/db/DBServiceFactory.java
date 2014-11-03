package com.cmk.utc.db;


import com.cmk.utc.db.datasource.DataSourceFactory;
import com.cmk.utc.db.datasource.model.DataSourceMeta;
import com.cmk.utc.db.executor.ISqlExecutor;
import com.cmk.utc.db.executor.impl.SqlExecutor;


/**
 * @Title IDBServiceFactory.java
 * @author zhangliang
 * @Description: DB服务工具类
 * @version $Rev$
 * @LastModify: $Id$
 */
public class DBServiceFactory {

    public static ISqlExecutor getISqlExecutor() {
        return new SqlExecutor();
    }

    public static DataSourceMeta getDataSourceMeta(String ds) {
        return DataSourceFactory.getInstance().getDataSourceMeta(ds);
    }

    public static DataSourceMeta getDefaultDataSourceMeta() {
        return DataSourceFactory.getInstance().getDefaultDataSourceMeta();
    }

}
