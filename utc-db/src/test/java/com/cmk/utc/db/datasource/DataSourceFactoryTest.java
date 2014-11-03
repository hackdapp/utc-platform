/*
 * 文件名：DataSourceFactoryTest.java 版权：Copyright by www.wmccn.com 描述： 修改人：LIFE2014 修改时间：2014-11-3
 * 跟踪单号： 修改单号： 修改内容：
 */

package com.cmk.utc.db.datasource;


import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class DataSourceFactoryTest {
    private DataSourceFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = factory.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        factory.clear();
    }

    @Test
    public void testLoad() {
        try {
            factory.load(getClass().getClassLoader().getResource("db.xml").getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetDataSourceMeta() {
        try {
            factory.load(getClass().getClassLoader().getResource("db.xml").getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(factory.getDataSourceMeta("platform") != null, true);
    }

    @Test
    public void testGetDefaultDataSourceMeta() {
        try {
            factory.load(getClass().getClassLoader().getResource("db.xml").getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(factory.getDefaultDataSourceMeta().getName(), "platform");
    }

    @Test
    public void testClear() {
        try {
            factory.load(getClass().getClassLoader().getResource("db.xml").getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        factory.clear();
        assertEquals(factory.getDefaultDataSourceMeta()==null, true);
    }
}
