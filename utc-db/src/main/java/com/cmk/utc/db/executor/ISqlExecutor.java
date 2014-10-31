package com.cmk.utc.db.executor;



import java.util.List;
import java.util.Map;

/**
 * Sql执行器
 * 
 * @author LIFE2014
 * @version 2014-5-12
 * @see ISqlExecutor
 * @since
 */
public interface ISqlExecutor {
    /**
     * 意义，目的和功能，以及被用到的地方<br>
     */
    String MODULE = ISqlExecutor.class.getName();

    /**
     * 预编译SQL执行
     * 
     * @param ds 数据源 数据源
     * @param preSql preSql
     * @see
     */
    void execute(String ds, PreStatementSQL preSql);

    /**
     * 批量执行SQL
     * 
     * @param ds 数据源
     * @param sql sql
     * @see
     */
    void execute(String ds, List<String> sql);

    /**
     * 执行单条SQL
     * 
     * @param ds 数据源
     * @param sql sql
     * @see
     */
    void execute(String ds, String sql);

    /**
     * 查询多条数据
     * 
     * @param ds 数据源
     * @param sql sql
     * @return List<Map>
     * @see
     */
    List<Map> query(String ds, String sql);

    /**
     * 查询单条数据
     * 
     * @param ds 数据源
     * @param sql sql
     * @return Map
     * @see
     */
    Map queryOne(String ds, String sql);

    /**
     * 统计记录条数
     * 
     * @param ds 数据源
     * @param sql sql
     * @return int
     * @see
     */
    int count(String ds, String sql);
}