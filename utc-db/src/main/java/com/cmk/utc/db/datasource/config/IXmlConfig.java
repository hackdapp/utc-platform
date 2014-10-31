package com.cmk.utc.db.datasource.config;


import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


public abstract class IXmlConfig {
    /**
     * @param inputStream
     * @return
     */
    public Map load(InputStream inputStream) {
        Map<String, Object> mps = new LinkedHashMap<String, Object>();
        try {
            Document doc = buildDocument(inputStream);
            if (doc == null)
                return mps;
            Element root = doc.getRootElement();
            parse(root, mps);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mps;
    }

    /**
     * @param configFileName
     * @return
     */
    public Map load(String configFileName) {
        Map<String, Object> mps = new LinkedHashMap<String, Object>();
        try {
            if (configFileName == null || configFileName.trim().length() == 0)
                return mps;
            Document doc = buildDocument(getInputStream(configFileName));
            if (doc == null)
                return mps;

            Element root = doc.getRootElement();
            parse(root, mps);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mps;
    }

    /**
     * @param xmlStream
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    protected Document buildDocument(InputStream xmlStream) throws JDOMException, IOException {
        return new SAXBuilder().build(xmlStream);
    }

    /**
     * @param configFileName
     * @return
     * @throws Exception
     */
    protected InputStream getInputStream(String configFileName) throws Exception {
        return this.getClass().getResourceAsStream("/META-INF/" + configFileName);
    }

    /**
     * @param root
     * @param results
     * @throws Exception
     */
    public abstract void parse(Element root, Map results) throws Exception;
}
