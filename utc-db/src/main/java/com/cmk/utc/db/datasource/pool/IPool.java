package com.cmk.utc.db.datasource.pool;




import javax.sql.DataSource;

import com.cmk.utc.db.datasource.model.DBConfig;



/**
 * @author LIFE2014
 * @version 2014-5-9
 * @see IPool
 * @since
 */
public interface IPool {
    /**
     * 根据数据库配置加载DataSource
     * 
     * @param config DBConfig
     * @return DataSource
     * @see
     */
    DataSource loadSource(DBConfig config);
}
