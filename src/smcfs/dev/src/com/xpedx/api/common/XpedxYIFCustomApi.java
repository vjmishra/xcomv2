package com.xpedx.api.common;

import java.util.Properties;

import com.xpedx.constants.XpedxConstants;

public class XpedxYIFCustomApi implements com.yantra.interop.japi.YIFCustomApi, XpedxConstants {
	private Properties properties = new Properties();
	public void setProperties(Properties props) throws Exception { 
		this.properties = props;
	}
	public Properties getProperties() throws Exception {
		return properties;
	}
	public Object getProperty(Object key) throws Exception {
		return properties.get(key);
	}
	public String getProperty(String key, String defaultValue) throws Exception {
		return properties.getProperty(key, defaultValue);
	}
	public boolean containsKey(String key) throws Exception {
		return properties.containsKey(key);
	}
}

