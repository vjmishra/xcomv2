package com.xpedx.nextgen.common.util;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.woodstock.util.frame.log.base.ISCILogger;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfs.japi.YFSConnectionHolder;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;
import com.yantra.yfc.log.YFCLogCategoryFactory;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class XPXUtilsCacheEnableDisable implements YIFCustomApi {

	private Properties _properties = null;
	private static YIFApi api = null;
	private static ISCILogger log = null;	
	String feedName="";

	static {
		log = new YFCLogCategoryFactory().getLogger(XPXUtilsCacheEnableDisable.class
				.getCanonicalName());
		try {

			api = YIFClientFactory.getInstance().getApi();

		} catch (YIFClientCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	@Override
	public void setProperties(Properties properties) throws Exception {
		// TODO Auto-generated method stub
		_properties = properties;
	}



	public Document CacheEnableDisable(YFSEnvironment env, Document inXML) throws  RemoteException{

		try{


			log.debug("inXML"+SCXmlUtil.getString(inXML));
			

			//String feedName = inXML.getDocumentElement().getAttribute("FeedName");
			//	String cacheType=_properties.getProperty("CacheType");
			String strRoot = inXML.getDocumentElement().getNodeName();
			if(strRoot.equals("SOF")){
				feedName =_properties.getProperty("FeedName");
				log.debug("feedName::"+feedName);
				
			}


			Document modifyCacheInput = YFCDocument.createDocument(
			"CachedGroups").getDocument();
			Element cachedGroup = modifyCacheInput
			.createElement("CachedGroup");
			cachedGroup.setAttribute("Name", "Database");
			modifyCacheInput.getDocumentElement().appendChild(cachedGroup);

		


			//if(strRoot!=null && (strRoot.equals("SOF")||strRoot.equals("EOFCustomer")||strRoot.equals("EOFDivision")|| strRoot.equals("EOFEntitlement")||strRoot.equals("EOFPriceBook")||strRoot.equals("EOFUom"))){
			/***** Modified code for Jira 4143 *********/
			if(strRoot!=null && (strRoot.equals("SOF")||strRoot.equals("EOFDivision")|| strRoot.equals("EOFEntitlement")||strRoot.equals("EOFPriceBook")|| strRoot.equals("EOFUom"))){	
				
				/***commented code for JIra 4143   ******/
			
/*				if ((feedName!=null &&feedName.equalsIgnoreCase("Customer"))||strRoot.equals("EOFCustomer")) {
					
					String customerFeedCachedObjects = _properties   
					.getProperty("CustomerFeedCachedObjectsList");
					String[] cachedObjectsList = customerFeedCachedObjects
					.split(",");
					for (String cachedObjectProperty : cachedObjectsList) {
						Element cachedObject = modifyCacheInput
						.createElement("CachedObject");
						cachedObject.setAttribute("Action", "MODIFY");
						if(strRoot.equals("SOF")){
							
							cachedObject.setAttribute("Enabled", "N");
						}else{
							
							cachedObject.setAttribute("Enabled", "Y");

						}
						cachedObject.setAttribute("Class", _properties
								.getProperty(cachedObjectProperty));
						cachedGroup.appendChild(cachedObject);
					}

				}*/

				if ((feedName!=null &&feedName.equalsIgnoreCase("Division"))||strRoot.equals("EOFDivision") ) {
				
					String DivisionFeedCachedObjects = _properties
					.getProperty("DivisionFeedCachedObjectsList");
					String[] cachedObjectsList = DivisionFeedCachedObjects
					.split(",");
					for (String cachedObjectProperty : cachedObjectsList) {
						Element cachedObject = modifyCacheInput
						.createElement("CachedObject");
						cachedObject.setAttribute("Action", "MODIFY");
						if(strRoot.equals("SOF")){
							cachedObject.setAttribute("Enabled", "N");
						}else{
							cachedObject.setAttribute("Enabled", "Y");

						}
						cachedObject.setAttribute("Class", _properties
								.getProperty(cachedObjectProperty).trim());
						cachedGroup.appendChild(cachedObject);
					}
				}
				else if ((feedName!=null && feedName.equalsIgnoreCase("Entitlement"))||strRoot.equals("EOFEntitlement")) {
					String EntitlementFeedCachedObjects = _properties
					.getProperty("EntitlementFeedCachedObjectsList");
					String[] cachedObjectsList = EntitlementFeedCachedObjects
					.split(",");
					log.debug("Printing EntitlementFeedCachedObjects "+EntitlementFeedCachedObjects);
					for (String cachedObjectProperty : cachedObjectsList) {
						Element cachedObject = modifyCacheInput
						.createElement("CachedObject");
						cachedObject.setAttribute("Action", "MODIFY");
						if(strRoot.equals("SOF")){
							cachedObject.setAttribute("Enabled", "N");
						}else{
							cachedObject.setAttribute("Enabled", "Y");

						}
						//	cachedObject.setAttribute("Enabled", "N");
						cachedObject.setAttribute("Class", _properties
								.getProperty(cachedObjectProperty).trim());
						cachedGroup.appendChild(cachedObject);
					}
				}

				//else if ((feedName!=null &&feedName.equalsIgnoreCase("PriceBook"))||strRoot.equals("EOFPriceBook")) {
					
					/****Modified code for Jira 4143 ***********/
					else if ((feedName!=null &&feedName.equalsIgnoreCase("PriceBook"))&& strRoot.equals("EOFPriceBook")) {
						
						deletePriceListLine(env);
						
						
						/*******Commented code for Jira 4143 *********/
					/*String PriceBookFeedCachedObjects = _properties
					.getProperty("PriceBookFeedCachedObjectsList");
					String[] cachedObjectsList = PriceBookFeedCachedObjects
					.split(",");
					for (String cachedObjectProperty : cachedObjectsList) {
						Element cachedObject = modifyCacheInput
						.createElement("CachedObject");
						cachedObject.setAttribute("Action", "MODIFY");
						if(strRoot.equals("SOF")){
							cachedObject.setAttribute("Enabled", "N");
						}else{
							cachedObject.setAttribute("Enabled", "Y");

						}
						//cachedObject.setAttribute("Enabled", "N");
						cachedObject.setAttribute("Class", _properties
								.getProperty(cachedObjectProperty).trim());
						cachedGroup.appendChild(cachedObject);
					}
					if(strRoot.equals("EOFPriceBook")){

						deletePriceListLine(env);

					}*/

				}	

				else if ((feedName!=null &&feedName.equalsIgnoreCase("Uom"))||strRoot.equals("EOFUom") ) {
					String PriceBookFeedCachedObjects = _properties
					.getProperty("UomFeedCachedObjectsList");
					String[] cachedObjectsList = PriceBookFeedCachedObjects
					.split(",");
					log.debug("Printing PriceBookFeedCachedObjects "+PriceBookFeedCachedObjects);
					for (String cachedObjectProperty : cachedObjectsList) {
						Element cachedObject = modifyCacheInput
						.createElement("CachedObject");
						cachedObject.setAttribute("Action", "MODIFY");
						if(strRoot.equals("SOF")){
							cachedObject.setAttribute("Enabled", "N");
						}else{
							cachedObject.setAttribute("Enabled", "Y");

						}
						//cachedObject.setAttribute("Enabled", "N");
						cachedObject.setAttribute("Class", _properties
								.getProperty(cachedObjectProperty).trim());
						cachedGroup.appendChild(cachedObject);
					}
				}

								log.debug("The modifyCache input is: "
						+ SCXmlUtil.getString(modifyCacheInput));
				api.invoke(env,"modifyCache",modifyCacheInput);


			//} commented if condition



			}
		}
		catch(Exception e ){

			log.error("Exception "+e);
			prepareErrorObject(e, feedName, XPXLiterals.NE_ERROR_CLASS, env,
					inXML);

		}
		return inXML;

	}//end of method


	private void prepareErrorObject(Exception e, String transType,
			String errorClass, YFSEnvironment env, Document inXML) {
		com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
		errorObject.setTransType(transType);
		errorObject.setErrorClass(errorClass);
		errorObject.setInputDoc(inXML);
		errorObject.setException(e);
		ErrorLogger.log(errorObject, env);
	}



	public  void deletePriceListLine(YFSEnvironment env)throws Exception{

		try{

			String sDel ="Delete from ypm_pricelist_line where modifyts <current_date-1";
			YFSConnectionHolder connHolder  = (YFSConnectionHolder)env;
			Connection m_Conn= connHolder.getDBConnection();
			Statement stmt =m_Conn.createStatement();
			stmt.execute(sDel);
			m_Conn.commit();

		}
		catch(Exception e){
			log.error("Exception: " + e.getStackTrace());

		}


	}



}
