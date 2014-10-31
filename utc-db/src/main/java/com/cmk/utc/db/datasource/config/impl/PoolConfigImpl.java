package com.cmk.utc.db.datasource.config.impl;


import java.util.Map;

import org.jdom.Element;

import com.cmk.utc.db.datasource.config.IXmlConfig;


public class PoolConfigImpl extends IXmlConfig {
    @Override
    public void parse(Element root, Map results) throws Exception {
        String providerClass = root.getChildText("provider");
        results.put("provider", providerClass.trim());
    }
}
