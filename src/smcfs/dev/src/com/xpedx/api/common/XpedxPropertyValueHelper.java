package com.xpedx.api.common;


import java.util.HashMap;

import org.apache.log4j.*;
import com.yantra.ycp.propmgmt.helper.PLTPropertyHelper;
import com.yantra.yfs.core.YFSSystem;

/**
 * @author smrao
 *
 */
public class XpedxPropertyValueHelper {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(XpedxPropertyValueHelper.class); 
		
	
	/**
	 * Method to get the property value from property table. In Case
	 * The value is not found in the table, then, searches in
	 * customeroverrides.properties. if it is not found in the prop file, then
	 * the default value sent is returned.  
	 * @param propertyName
	 * @return
	 */
	public static String getPropertyValue(String propertyName){
		return getPropertyValue(propertyName, null);
	}
	
	private static HashMap<String, String> oldProperties = new HashMap<String, String>();
	
	public static String getPropertyValue(String propertyName, String defaultValue){

		String propertySource = "DataBase Table PLT_PROPERTY";
		String propertyNameStr = null;
		String propertyValue = null;
		if(propertyName!=null){
			
			propertyNameStr = propertyName.trim();
			
			PLTPropertyHelper pHelper = new PLTPropertyHelper();

			// property from the table
			propertyValue = pHelper.getPropertyFromDB(null, propertyNameStr);
			if(propertyValue != null){
				propertySource = "DataBase Table PLT_PROPERTY";
			}else{
				// property from the custom overrides
				propertyValue = YFSSystem.getProperty(propertyNameStr);
				if(propertyValue!=null){
					propertySource = "customer_overrides.properties";
				}else{
					// default value
					propertyValue = defaultValue;
					propertySource = "Defalult Value";
				}
			} 
		
		
			if (oldProperties.containsKey(propertyNameStr) == false
					|| oldProperties.get(propertyNameStr).toString().equalsIgnoreCase(propertyValue) == false) {
				
				LOG.debug("Fetched Property {" + propertyNameStr + "} Value {" + propertyValue + "} from " + propertySource);
				
				if (propertyValue != null) {
					oldProperties.put(propertyNameStr, propertyValue);
		}
			} 
		}

		return propertyValue;
	}
}

