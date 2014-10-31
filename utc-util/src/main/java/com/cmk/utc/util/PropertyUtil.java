package com.cmk.utc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PropertyUtil {
	private Properties props = new Properties();

	public PropertyUtil(InputStream inputStream) {
		try {
			props.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getProperty(String name) {
		return props.getProperty(name);
	}
}
