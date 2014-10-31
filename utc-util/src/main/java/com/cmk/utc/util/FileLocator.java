package com.cmk.utc.util;


import java.io.InputStream;
import java.net.URL;
import java.io.File;

/**
 * @author zhangliang
 *
 * note:
 */
public class FileLocator {

    /**
     * @param filePathName
     * @return
     */
    public String getConfPathXmlFile(String filePathName) {
        int i = filePathName.lastIndexOf(".xml");
        String name = filePathName.substring(0, i);
        name = name.replace('.', '/');
        name += ".xml";
        return getConfFile(name);
    }

    /**
     * same as getConfPathXmlFile
     * @param filePathName
     * @return the InputStream intance
     */
    public InputStream getConfPathXmlStream(String filePathName) {
        int i = filePathName.lastIndexOf(".xml");
        String name = filePathName.substring(0, i);
        name = name.replace('.', '/');
        name += ".xml";
        return getConfStream(name);
    }

    /**
     * @param fileName
     * @return
     */
    public String getConfFile(String fileName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = getClass().getClassLoader();
        }
        URL confURL = classLoader.getResource(fileName);
        if (confURL == null)
            confURL = classLoader.getResource("WEB-INF/" + fileName);
        if (confURL == null) {
            return null;
        } else {
            File file1 = new File(confURL.getFile());
            if (file1.isFile()) {
                System.out.println(" locate file: " + confURL.getFile());
                return confURL.getFile();
            } else {
                System.err.println(" it is not a file: " + confURL.getFile());
                return null;
            }
        }
    }

    /**
     * @param fileName
     * @return
     */
    public InputStream getConfStream(String fileName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = this.getClass().getClassLoader();
        }
        InputStream stream = classLoader.getResourceAsStream(fileName);
        if (stream == null)
            stream = classLoader.getResourceAsStream("META-INF/" + fileName);

        return stream;
    }

}
