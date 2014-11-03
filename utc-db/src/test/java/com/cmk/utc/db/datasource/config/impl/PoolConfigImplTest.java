/*
 * 文件名：PoolConfigImplTest.java
 * 版权：Copyright by www.wmccn.com
 * 描述：
 * 修改人：LIFE2014
 * 修改时间：2014-11-3
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.cmk.utc.db.datasource.config.impl;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cmk.utc.db.datasource.config.IXmlConfig;

public class PoolConfigImplTest {

    @Before
    public void setUp() throws Exception {}

    @After
    public void tearDown() throws Exception {}

    @Test
    public void testLoadInputStream() {
        IXmlConfig xmlConfig = new PoolConfigImpl();
        Map map = xmlConfig.load(getClass().getClassLoader().getResourceAsStream("db.xml"));
        assertEquals(map.get("provider"), "com.cmk.utc.db.datasource.pool.impl.DruidImpl");
    }
}
