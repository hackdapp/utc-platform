package com.cmk.utc.db.datasource.config.impl;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jdom.Element;

import com.cmk.utc.db.datasource.config.IXmlConfig;
import com.cmk.utc.db.datasource.model.DBConfig;


public class DBConfigImpl extends IXmlConfig {
    @Override
    public void parse(Element root, Map results) throws Exception {

        Properties extendConfig = new Properties();
        if (root.getChild("base-config") != null) {
            List<Element> defaultConfigs = (List<Element>)root.getChild("base-config").getChildren();
            for (Element itemConfig : defaultConfigs) {
                extendConfig.setProperty(itemConfig.getName(), itemConfig.getValue());
            }
        }

        List<Element> dataBases = (List<Element>)root.getChildren("database");
        for (Element dbEle : dataBases) {
            if (!"database".equals(dbEle.getName()))
                continue;

            String dbName = dbEle.getAttributeValue("name");
            boolean isDefault = dbEle.getAttributeValue("default") != null
                                && "true".equals(dbEle.getAttributeValue("default"));
            String userName = dbEle.getChildText("username");
            String userPwd = dbEle.getChildText("password");
            String url = dbEle.getChildText("url");
            String driverName = dbEle.getChildText("driver").trim();

            for (Element itemConfig : (List<Element>)dbEle.getChildren()) {
                if (!"driver".equals(itemConfig.getName()) && !"url".equals(itemConfig.getName())
                    && !"username".equals(itemConfig.getName())
                    && !"password".equals(itemConfig.getName())) {
                    extendConfig.setProperty(itemConfig.getName(), itemConfig.getValue());
                }
            }

            results.put(dbName, new DBConfig(driverName, url, userName, userPwd, isDefault,
                extendConfig));
        }
    }

    public static void main(String[] args) {
        DBConfigImpl cb = new DBConfigImpl();
        File file = new File("D:\\OSGI_WORK\\platform-db\\META-INF\\db.xml");
        try {
            Map<String, DBConfig> map = cb.load(new FileInputStream(file));
            System.out.println(map);
            System.out.println(map.get("platform").getConfiguration());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
