/*
 * 文件名：DBServiceFactoryImplTest.java 版权：Copyright by www.wmccn.com 描述： 修改人：LIFE2014 修改时间：2014-11-3
 * 跟踪单号： 修改单号： 修改内容：
 */

package com.cmk.utc.db.factory;


import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cmk.utc.db.DBServiceFactory;
import com.cmk.utc.db.datasource.DataSourceFactory;
import com.cmk.utc.db.executor.ISqlExecutor;


public class DBServiceFactoryImplTest {
    @Before
    public void setUp() throws Exception {
        DataSourceFactory.getInstance().load(getClass().getClassLoader().getResource("db.xml").getPath());
    }

    @After
    public void tearDown() throws Exception {}

    @Test
    public void test() {
        ISqlExecutor sqlExecutor = DBServiceFactory.getISqlExecutor();
        assertEquals(sqlExecutor.query("platform", "select * from jzusers").size()>0, true);
    }

}
