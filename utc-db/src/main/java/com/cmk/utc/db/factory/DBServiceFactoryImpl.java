package com.cmk.utc.db.factory;


import com.cmk.utc.db.IDBServiceFactory;
import com.cmk.utc.db.datasource.DataSourceFactory;
import com.cmk.utc.db.datasource.model.DataSourceMeta;
import com.cmk.utc.db.executor.ISqlExecutor;
import com.cmk.utc.db.executor.impl.SqlExecutor;


public class DBServiceFactoryImpl implements IDBServiceFactory {

    public ISqlExecutor getISqlExecutor() {
        return new SqlExecutor();
    }

    public DataSourceMeta getDataSourceMeta(String ds) {
        return DataSourceFactory.getInstance().getDataSourceMeta(ds);
    }

    public DataSourceMeta getDefaultDataSourceMeta() {
        return DataSourceFactory.getInstance().getDefaultDataSourceMeta();
    }

}
