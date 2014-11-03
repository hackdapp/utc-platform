/*
 * 文件名：DBConfigImplTest.java 版权：Copyright by www.wmccn.com 描述： 修改人：LIFE2014 修改时间：2014-11-3 跟踪单号：
 * 修改单号： 修改内容：
 */

package com.cmk.utc.db.datasource.config.impl;


import static org.junit.Assert.*;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cmk.utc.db.datasource.config.IXmlConfig;


public class DBConfigImplTest {

    @Before
    public void setUp() throws Exception {}

    @After
    public void tearDown() throws Exception {}

    @Test
    public void testParse() {
        IXmlConfig xmlConfig = new DBConfigImpl();
        Map map = xmlConfig.load(getClass().getClassLoader().getResourceAsStream("db.xml"));
        assertEquals(map.size(), 1);
    }

}
