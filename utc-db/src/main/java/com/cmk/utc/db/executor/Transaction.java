package com.cmk.utc.db.executor;



import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cmk.utc.db.datasource.DataSourceFactory;
import com.cmk.utc.db.datasource.model.DataSourceMeta;
import com.cmk.utc.util.ThreadUtil;



/**
 * @Title Transaction.java 事务管理类
 * @author zhangliang
 * @Description:
 * @version $Rev: 2648 $
 * @LastModify: $Id: Transaction.java 2648 2012-11-23 05:54:05Z zhangl $
 */
public class Transaction {
    /** 父事务 */
    private Transaction parent;

    /** 事务名称 */
    private String name = "";

    /** 本事务数据库连接池 */
    private ConcurrentHashMap<String, Connection> concurrentHashMap;

    /** 是否成功提交事务 */
    private boolean commit = false;

    /**
     * 缺少构造函数
     */
    public Transaction() {
        this(new Date().getTime()+"");
    }

    /**
     * 事务构造函数
     * 
     * @param name 事务名称
     */
    public Transaction(String name) {
        this.name = name;
        Transaction transaction = (Transaction)ThreadUtil.threadVar(Transaction.class.getName());
        // 1.判断当前线程中是否存在事务
        if (transaction != null) {
            // 1.1 存在事务,则根据名称判断是否为嵌套事务
            if (!transaction.getName().equals(name)) {
                this.parent = transaction;
                // A. 如果为嵌套事务，则将当前事务赋值给parent事务,并实始化concurrentHashMap
                this.concurrentHashMap = new ConcurrentHashMap<String, Connection>();
            } else {
                // B. 如果不为嵌套事务,则将当前事务初始化到本实例中
                this.concurrentHashMap = transaction.concurrentHashMap;
            }
        } else {
            // 1.2不存在事务则将本实例事务放入线程中
            this.concurrentHashMap = new ConcurrentHashMap<String, Connection>();
        }
        ThreadUtil.setThreadVar(Transaction.class.getName(), this);
    }

    /**
     * 事务提交
     * 
     * @throws SQLException
     */
    public void commit() throws SQLException {
        if (!commit) {
            for (Map.Entry<String, Connection> entry : this.concurrentHashMap.entrySet()) {
                Connection conn = entry.getValue();
                try {
                    conn.commit();
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    throw new SQLException("手动提交事务失败", e);
                }
            }
            commit = true;
        }
    }

    /**
     * 关闭事务
     * 
     * @throws SQLException
     */
    public void end() throws SQLException {
        for (Map.Entry<String, Connection> entry : this.concurrentHashMap.entrySet()) {
            Connection conn = entry.getValue();
            try {
                if (!commit)
                    conn.rollback();
            } catch (SQLException e) {
                throw new SQLException("事务回滚失败", e);
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new SQLException("数据连接关闭失败", e);
                }
            }
        }
        this.concurrentHashMap.clear();
        this.commit = true;
        ThreadUtil.setThreadVar(Transaction.class.getName(), this.parent);
    }

    /**
     * 从事务中获取数据连接
     * 
     * @param ds
     * @return
     * @throws SQLException
     */
    public synchronized Connection getConnection(String ds) throws SQLException {

        Connection conn = this.concurrentHashMap.get(ds);
        if (conn == null) {
            try {
                DataSourceMeta dataSourceMeta = DataSourceFactory.getInstance().getDataSourceMeta(ds);
                conn = dataSourceMeta.getConnection();
                conn.setAutoCommit(false);
            } catch (SQLException e) {
                throw new SQLException("设置手动提交事务 ", e);
            }
            this.concurrentHashMap.put(ds, conn);
        }
        return conn;
    }

    /**
     * 获取事务名称
     * 
     * @return
     */
    public String getName() {
        return name;
    }
}
