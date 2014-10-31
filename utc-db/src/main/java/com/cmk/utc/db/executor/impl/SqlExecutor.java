package com.cmk.utc.db.executor.impl;


import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.SimpleFormatter;

import com.cmk.utc.db.datasource.DataSourceFactory;
import com.cmk.utc.db.datasource.model.DataSourceMeta;
import com.cmk.utc.db.executor.ConnectionStatus;
import com.cmk.utc.db.executor.ISqlExecutor;
import com.cmk.utc.db.executor.PreStatementSQL;
import com.cmk.utc.db.executor.Transaction;
import com.cmk.utc.util.Cast;
import com.cmk.utc.util.DateUtil;
import com.cmk.utc.util.ThreadUtil;


/**
 * @author LIFE2014
 * @version 2014-5-12
 * @see SqlExecutor
 * @since
 */
public class SqlExecutor implements ISqlExecutor {

    public int count(String ds, String sql) {
        sql = "select count(*) N from (" + sql + ") A";
        List<Map> list = dbQuery(ds, sql);
        int count = 0;
        if (list != null && list.size() == 1) {
            count = Cast.to(list.get(0).get("N"), 0);
        }
        return count;
    }

    public Map queryOne(String ds, String sql) {
        List<Map> rsList = dbQuery(ds, sql);
        return rsList != null && rsList.size() > 0 ? rsList.get(0) : null;
    }

    public List<Map> query(String ds, String sql) {
        return dbQuery(ds, sql);
    }

    /**
     * @param ds 数据源
     * @param sql sql
     * @return
     * @see
     */
    private List<Map> dbQuery(String ds, String sql) {
        List<Map> rtnList = new ArrayList<Map>();

        Statement stmt = null;
        ResultSet rs = null;
        Connection connection = null;
        try {
            long st = new Date().getTime();
            connection = openNewConnection(ds);
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();

            Map bean = null;
            while (rs.next()) {
                bean = new HashMap();
                for (int i = 1; i <= rsmd.getColumnCount(); i++ ) {
                    if (rsmd.getColumnType(i) == Types.BLOB
                        || rsmd.getColumnType(i) == Types.LONGVARBINARY) {
                        bean.put(rsmd.getColumnLabel(i), rs.getBytes(i));
                    } else if (rsmd.getColumnType(i) == Types.NUMERIC) {
                        String numStr = Cast.to(rs.getString(i), "0");
                        if (numStr.indexOf(".") != -1) {
                            bean.put(rsmd.getColumnLabel(i), rs.getDouble(i));
                        } else {
                            bean.put(rsmd.getColumnLabel(i), rs.getInt(i));
                        }
                    } else if (rsmd.getColumnType(i) == Types.DATE) {
                        bean.put(rsmd.getColumnLabel(i), rs.getDate(i));
                    } else {
                        bean.put(rsmd.getColumnLabel(i), rs.getString(i));
                    }
                }
                rtnList.add(bean);
            }
            long ed = new Date().getTime();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rtnList;
    }

    public void execute(String ds, String sql) {
        Statement statement = null;
        Connection connection = null;
        boolean isTrans = false;
        try {
            ConnectionStatus connStatus = openConnection(ds);
            connection = connStatus.getConnection();
            isTrans = connStatus.isTrans();
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null && !isTrans) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void execute(String ds, List<String> sql) {
        Statement statement = null;
        Connection connection = null;
        boolean isTrans = false;
        try {
            ConnectionStatus connStatus = openConnection(ds);
            connection = connStatus.getConnection();
            isTrans = connStatus.isTrans();

            statement = connection.createStatement();
            for (String tmp : sql) {
                statement.addBatch(tmp);
            }
            statement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null && !isTrans) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void execute(String ds, PreStatementSQL preSql) {
        PreparedStatement stmt = null;
        Connection connection = null;
        boolean isTrans = false;

        try {
            ConnectionStatus connStatus = openConnection(ds);
            connection = connStatus.getConnection();
            isTrans = connStatus.isTrans();
            if (isTrans) {
                Transaction trans = (Transaction)ThreadUtil.threadVar(Transaction.class.getName());
            }

            stmt = connection.prepareStatement(preSql.getSql());
            for (Object[] tmpRow : preSql.getPlaceValue()) {
                int i = 1;
                for (Object tmpPlaceVal : tmpRow) {
                    if (tmpPlaceVal instanceof byte[]) {
                        byte[] value = (byte[])tmpPlaceVal;
                        stmt.setBinaryStream(i, new ByteArrayInputStream(value), value.length);
                    } else if (tmpPlaceVal instanceof String) {
                        stmt.setString(i, (String)tmpPlaceVal);
                    } else if (tmpPlaceVal instanceof Integer) {
                        stmt.setInt(i, (Integer)tmpPlaceVal);
                    } else if (tmpPlaceVal instanceof java.sql.Date) {
                        stmt.setDate(i, (java.sql.Date)tmpPlaceVal);
                    } else if (tmpPlaceVal instanceof java.util.Date) {
                        java.util.Date sdate = (java.util.Date)tmpPlaceVal;
                        stmt.setDate(i, new java.sql.Date(sdate.getTime()));
                    } else if (tmpPlaceVal instanceof Double) {
                        stmt.setDouble(i, (Double)tmpPlaceVal);
                    } else {
                        if (tmpPlaceVal == null) {
                            stmt.setString(i, "");
                        } else {
                            stmt.setString(i, String.valueOf(tmpPlaceVal));
                        }
                    }
                    i++ ;
                }

                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (connection != null && !isTrans) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Open the data source connection
     * 
     * @param dsName ds
     * @return ConnectionStatus
     * @throws SQLException SQLException
     */
    private static ConnectionStatus openConnection(String dsName) throws SQLException {
        Transaction trans = (Transaction)ThreadUtil.threadVar(Transaction.class.getName());
        if (trans != null) {
            return new ConnectionStatus(trans.getConnection(dsName), true);
        } else {
            return new ConnectionStatus(openNewConnection(dsName), false);
        }
    }

    /**
     * Open the data source connection
     * 
     * @param dsName ds
     * @return Connection
     * @throws SQLException SQLException
     */
    private static Connection openNewConnection(String dsName) throws SQLException {
        DataSourceMeta meta = DataSourceFactory.getInstance().getDataSourceMeta(dsName);
        return meta.getConnection();
    }

    /**
     * @param targetStr
     * @param sqlValues
     * @return
     * @see
     */
    private static String getSQL(String targetStr, Object[] sqlValues) {
        StringTokenizer token = new StringTokenizer(targetStr, "?", false);
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < sqlValues.length; i++ ) {
            buf.append(token.nextToken());
            buf.append("'" + sqlValues[i] + "'");
        }
        return buf.toString();
    }

}
