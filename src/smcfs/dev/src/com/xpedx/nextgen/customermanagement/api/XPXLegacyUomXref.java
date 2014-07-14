
package com.xpedx.nextgen.customermanagement.api;
import java.util.Properties;
//import java.rmi.RemoteException;
//import java.util.HashSet;
import java.util.HashMap;
//import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
//import com.yantra.yfc.dom.YFCElement;
//import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfs.japi.YFSEnvironment;
//import com.yantra.yfs.japi.YFSException;
import com.yantra.yfs.japi.YFSException;


public class XPXLegacyUomXref implements YIFCustomApi {
	
	private static YIFApi api = null;
	private static YFCLogCategory log;
	
	static
	{
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		
	}
	
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}
	/**
	 * This is the method which gets invoked on the hit of the service.
	 * This forms the complete input xml
	 * @param env
	 * @param inXML
	 * @return
	 * @throws Exception
	 */
	public Document getXPXLegacyUomXref(YFSEnvironment env,Document inXML) throws Exception
	{
		/** try-catch block added by Arun Sekhar on 24-Jan-2011 for CENT tool logging **/
		try{

			api = YIFClientFactory.getInstance().getApi();
			YFCDocument inputLegacyDoc = YFCDocument.createDocument("XPXLegacyUomXref");	
			Document NewLegacyDoc=api.executeFlow(env,"legecyUom1", inputLegacyDoc.getDocument());		
			HashMap<String,String> hashMap = new HashMap<String, String>();
			NodeList LegacyUomList = NewLegacyDoc.getElementsByTagName("XPXLegacyUomXref");		
				for(int LegacyUomCounter = 0;LegacyUomCounter<LegacyUomList.getLength();LegacyUomCounter++)
				{
					Element LegacyElement = (Element)LegacyUomList.item(LegacyUomCounter);
					String UOM = LegacyElement.getAttribute("UOM");
					String LegecyUom = LegacyElement.getAttribute("LegacyUOM");
					hashMap.put(UOM,LegecyUom);					
				}		
			//Document outputOrderDoc = null;
			
			Element OrderElement = inXML.getDocumentElement();
			NodeList OrderLineList = OrderElement.getElementsByTagName("OrderLine");																	
			String UnitOfMeasure="";
			String LegacyUOM="";
				for(int OrderNo = 0;OrderNo<OrderLineList.getLength();OrderNo++)
				{
					Element OrderLineElement = (Element)OrderLineList.item(OrderNo);
					NodeList ItemList = OrderLineElement.getElementsByTagName("Item");				
					Element ItemElement = (Element)ItemList.item(0);
					UnitOfMeasure = ItemElement.getAttribute("UnitOfMeasure");
					if(hashMap.containsKey(UnitOfMeasure))
						{
							 LegacyUOM = (String) hashMap.get(UnitOfMeasure);
							 ItemElement.setAttribute("UnitOfMeasure",LegacyUOM);
							
						}				
					NodeList OrderLineTranQuantityList = OrderLineElement.getElementsByTagName("OrderLineTranQuantity");
					if(OrderLineTranQuantityList.getLength()>0)
					{
						Element OrderLineTranQuantityElement = (Element)OrderLineTranQuantityList.item(0);
						UnitOfMeasure = OrderLineTranQuantityElement.getAttribute("TransactionalUOM");
						if(hashMap.containsKey(UnitOfMeasure))
						{
							 LegacyUOM = (String) hashMap.get(UnitOfMeasure);
							 OrderLineTranQuantityElement.setAttribute("TransactionalUOM",LegacyUOM);
							
						}					
											
					}		
					
				}						
		
		}catch (NullPointerException ne) {
			log.error("NullPointerException: " + ne.getStackTrace());	
			prepareErrorObject(ne, XPXLiterals.UOM_B_TRANS_TYPE, XPXLiterals.NE_ERROR_CLASS, env, inXML);	
			throw ne;
		} catch (YFSException yfe) {
			log.error("YFSException: " + yfe.getStackTrace());
			prepareErrorObject(yfe, XPXLiterals.UOM_B_TRANS_TYPE, XPXLiterals.YFE_ERROR_CLASS, env, inXML);	
			throw yfe;
		} catch (Exception e) {
			log.error("Exception: " + e.getStackTrace());
			prepareErrorObject(e, XPXLiterals.UOM_B_TRANS_TYPE, XPXLiterals.E_ERROR_CLASS, env, inXML);
			throw e;
		}
		return inXML;
	}
	
	/**@author asekhar-tw on 24-Jan-2011
	 * This method prepares the error object with the exception details which in turn will be used to log into CENT
	 */
	private void prepareErrorObject(Exception e, String transType, String errorClass, YFSEnvironment env, Document inXML){
		com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();	
		errorObject.setTransType(transType);
		errorObject.setErrorClass(errorClass);
		errorObject.setInputDoc(inXML);
		errorObject.setException(e);
		ErrorLogger.log(errorObject, env);
	}
}
