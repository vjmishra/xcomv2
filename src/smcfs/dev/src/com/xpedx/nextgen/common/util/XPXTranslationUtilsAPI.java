package com.xpedx.nextgen.common.util;

import java.rmi.RemoteException;
import java.util.Properties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXTranslationUtilsAPI implements YIFCustomApi{

	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
	private static YIFApi api = null;
	private static YFCLogCategory log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	
	static String getCustomerListTemplate = "global/template/api/getCustomerList.XPXTranslationUtilsAPI.xml";
	
	String getItemListTemplate = "global/template/api/getItemList.XPXTranslationUtilsAPI";
	
	//this method is to convert customer specific uom to Sterling specific uom for an incoming order
	public Document translateUomCustomerToSterlingForOrder(YFSEnvironment env, Document inputXML) throws YIFClientCreationException, YFSException, RemoteException
	{
		String itemID = "";
		String xpedxUnspsc = "";
		api = YIFClientFactory.getInstance().getApi();
		if(inputXML != null){
			log.debug("The inputXML to translateUomCustomerToSterlingForOrder in XPXTranslationUtilsAPI is : "+ SCXmlUtil.getString(inputXML) );	
		}
		//get the customer id for the b2b order place
		Element inputElement = inputXML.getDocumentElement();
		String orgCode = inputElement.getAttribute("EnterpriseCode");
		String customerID = inputElement.getAttribute("CustomerID");
		String masterCustomer = getMsap(env,customerID,orgCode);
		//loop thru orderlines to get itemID
		//does not apply for order
		//unspscConversion(env, inputXML, masterCustomer);
		uomConversion(env, inputXML, masterCustomer);
		
		return inputXML;
	}
	
	//this method is to convert customer specific unspsc to Sterling specific unspsc for an incoming order
	public Document translateUnspscSterlingToCustomerForOrder(YFSEnvironment env, Document inputXML) throws YIFClientCreationException, YFSException, RemoteException
	{
		String itemID = "";
		String xpedxUnspsc = "";
		api = YIFClientFactory.getInstance().getApi();
		String orgCode = "";		
		//get the customer id for the b2b order place
		if(inputXML != null){
			log.debug("The inputXML to translateUnspscSterlingToCustomerForOrder in XPXTranslationUtilsAPI is : "+ SCXmlUtil.getString(inputXML) );	
		}
		Element inputElement = inputXML.getDocumentElement();
		String customerID = inputElement.getAttribute("CustomerID");
		orgCode = inputElement.getAttribute("EnterpriseCode");
		String masterCustomer = getMsap(env,customerID,orgCode);
		unspscConversion(env, inputXML, masterCustomer);
		return inputXML;
	}
	
	//this method is to convert Sterling specific uom to Customer specific uom for an outgoing order
	public void translateUomSterlingToCustomer(YFSEnvironment env,Element customerOrderElement) throws YFSException, RemoteException
	{
		String orgCode = "";
		String customerID = "";
		customerID = customerOrderElement.getAttribute("BillToID");
		orgCode = customerOrderElement.getAttribute("EnterpriseCode");
		String masterCustomer = getMsap(env,customerID,orgCode);
		String uom = "";
		NodeList orderLinesList = customerOrderElement.getElementsByTagName("OrderLines");
		int orderLinesLength = orderLinesList.getLength();
		if(orderLinesLength != 0)
		{
			NodeList orderLineList = customerOrderElement.getElementsByTagName("OrderLine");
			int orderLineLength = orderLineList.getLength();
			if(orderLineLength != 0)
			{
				for(int counter=0;counter<orderLineLength;counter++)
				{
					Element orderLineElement = (Element)orderLineList.item(counter);
					checkUomconversionType(env,orderLineElement,masterCustomer);
				}
			}
		}
	}
	
	//this method is used to change Sterling specific unspsc to customer unspsc for invoice ack
	public String translateUnspscSterlingToCustomerInvoice(YFSEnvironment env, String customerID,String itemID,String orgCode) throws YFSException, RemoteException
	{
		String masterCustomer = "";
		masterCustomer = getMsap(env, customerID, orgCode);
		String customerUnspsc = "";
		String unspsc = "";
		String xpedxUnspsc = getUnspscCode(env,itemID);
		//form the input
		Document inputUnspscDoc = SCXmlUtil.createDocument("XPXB2bLegacyUnspscXref");
		Element inputUnspscElement = inputUnspscDoc.getDocumentElement();
		inputUnspscElement.setAttribute("MasterCustomerID", masterCustomer);
		inputUnspscElement.setAttribute("LegacyItemID", itemID);
		inputUnspscElement.setAttribute("XpedxUNSPSC", xpedxUnspsc);
		Document outputUnspsc = api.executeFlow(env, "getXPXB2bLegacyUnspscXrefList", inputUnspscDoc);
		NodeList unspscNodeList = outputUnspsc.getElementsByTagName("XPXB2bLegacyUnspscXref");
		int unspscLength = unspscNodeList.getLength();
		if(unspscLength != 0)
		{
			Element unspscElement = (Element)unspscNodeList.item(0);
			customerUnspsc = unspscElement.getAttribute("CustomerUNSPSC");
		}
		
		return customerUnspsc;
	}
	
	private void checkUomconversionType(YFSEnvironment env, Element orderLineElement, String masterCustomer) throws YFSException, RemoteException
	{
		String uom = SCXmlUtil.getXpathAttribute(orderLineElement, "./OrderLineTranQuantity/@TransactionalUOM");
		String legacyUOM = "";
		//form the input xml
		Document inputUomDoc = SCXmlUtil.createDocument("XPXB2bLegacyUomXref");
		Element inputUomElement = inputUomDoc.getDocumentElement();
		inputUomElement.setAttribute("LegacyUOM", uom);
		inputUomElement.setAttribute("MasterCustomerID", masterCustomer);
		Document outputUomDoc = api.executeFlow(env, "getXPXB2bLegacyUomXrefList", inputUomDoc);
		NodeList uomNodeList = outputUomDoc.getElementsByTagName("XPXB2bLegacyUomXref");
		int uomLength = uomNodeList.getLength();
		if(uomLength != 0)
		{
			Element uomElement = (Element)uomNodeList.item(0);
			legacyUOM = uomElement.getAttribute("CustomerUOM");
			NodeList extnNodeList = orderLineElement.getElementsByTagName("Extn");
			int extnLength = extnNodeList.getLength();
			if(extnLength != 0)
			{
				Element extnElement = (Element)extnNodeList.item(0);
				extnElement.setAttribute("ExtnPricingUOM", legacyUOM);
			}
			NodeList tranNodeList = orderLineElement.getElementsByTagName("OrderLineTranQuantity");
			int tranLength = tranNodeList.getLength();
			if(tranLength != 0)
			{
				Element tranElement = (Element)tranNodeList.item(0);
				tranElement.setAttribute("TransactionalUOM", legacyUOM);
			}
			NodeList itemNodeList = orderLineElement.getElementsByTagName("Item");
			int itemLength = itemNodeList.getLength();
			if(itemLength != 0)
			{
				Element itemElement = (Element)itemNodeList.item(0);
				itemElement.setAttribute("UnitOfMeasure", legacyUOM);
			}
			
		}
	}
	
	
	private void uomConversion(YFSEnvironment env, Document inputXML, String masterCustomer) throws YFSException, RemoteException
	{
		String uom;
		NodeList orderLinesList = inputXML.getElementsByTagName("OrderLines");
		int linesLength = orderLinesList.getLength();
		if(linesLength != 0)
		{
			NodeList orderLineList = inputXML.getElementsByTagName("OrderLine");
			int lineLength = orderLineList.getLength();
			if(lineLength != 0)
			{
				for(int counter=0;counter<lineLength;counter++)
				{
					Element orderLineElement = (Element)orderLineList.item(counter);
					uom = SCXmlUtil.getXpathAttribute(orderLineElement, "./OrderLineTranQuantity/@TransactionalUOM");
					checkUOMConversionType(env,inputXML,masterCustomer,uom,orderLineElement);
				}
				
			}
		}
	}
	
	private void checkUOMConversionType(YFSEnvironment env, Document inputXML, String masterCustomer, String uom, Element orderLineElement) throws YFSException, RemoteException
	{
		String legacyUOM = "";
		//form the input xml
		Document inputUomDoc = SCXmlUtil.createDocument("XPXB2bLegacyUomXref");
		Element inputUomElement = inputUomDoc.getDocumentElement();
		inputUomElement.setAttribute("CustomerUOM", uom);
		inputUomElement.setAttribute("MasterCustomerID", masterCustomer);
		Document outputUomDoc = api.executeFlow(env, "getXPXB2bLegacyUomXrefList", inputUomDoc);
		NodeList uomNodeList = outputUomDoc.getElementsByTagName("XPXB2bLegacyUomXref");
		int uomLength = uomNodeList.getLength();
		if(uomLength != 0)
		{
			Element uomElement = (Element)uomNodeList.item(0);
			legacyUOM = uomElement.getAttribute("LegacyUOM");
			NodeList extnNodeList = orderLineElement.getElementsByTagName("Extn");
			int extnLength = extnNodeList.getLength();
			if(extnLength != 0)
			{
				Element extnElement = (Element)extnNodeList.item(0);
				extnElement.setAttribute("ExtnPricingUOM", legacyUOM);
			}
			NodeList tranNodeList = orderLineElement.getElementsByTagName("OrderLineTranQuantity");
			int tranLength = tranNodeList.getLength();
			if(tranLength != 0)
			{
				Element tranElement = (Element)tranNodeList.item(0);
				tranElement.setAttribute("TransactionalUOM", legacyUOM);
			}
			
			
		}
	}

	private void unspscConversion(YFSEnvironment env, Document inputXML,
			String masterCustomer) throws RemoteException {
		String itemID;
		String xpedxUnspsc;
		NodeList orderLinesList = inputXML.getElementsByTagName("OrderLines");
		int linesLength = orderLinesList.getLength();
		if(linesLength != 0)
		{
			NodeList orderLineList = inputXML.getElementsByTagName("OrderLine");
			int lineLength = orderLineList.getLength();
			if(lineLength != 0)
			{
				for(int counter=0;counter<lineLength;counter++)
				{
					Element orderLineElement = (Element)orderLineList.item(counter);
					itemID = SCXmlUtil.getXpathAttribute(orderLineElement, "./Item/@ItemID");
					xpedxUnspsc = getUnspscCode(env,itemID);
					checkUNSPSCConversionType(env,itemID,masterCustomer,xpedxUnspsc,orderLineElement,inputXML);
				}
				
			}
		}
	}
	
	private String getUnspscCode(YFSEnvironment env, String itemID) throws YFSException, RemoteException
	{
		String xpedxUnspsc = "";
		Document inputItemDoc = SCXmlUtil.createDocument("Item");
		Element inputItemElement = inputItemDoc.getDocumentElement();
		inputItemElement.setAttribute("ItemID", itemID);
		env.setApiTemplate("getItemList", getItemListTemplate);
		Document itemListDoc = api.invoke(env, "getItemList", inputItemDoc);
		env.clearApiTemplate("getItemList");
		NodeList itemNodeList = itemListDoc.getElementsByTagName("Item");
		int itemLength = itemNodeList.getLength();
		if(itemLength != 0)
		{
			Element itemElement = (Element)itemNodeList.item(0);
			xpedxUnspsc = itemElement.getAttribute("UNSPSC");
			
		}
		return xpedxUnspsc;
	}
	
	private void checkUNSPSCConversionType(YFSEnvironment env,String itemID,String masterCustomer,String xpedxUnspsc,Element orderLineElement,Document inputXML) throws YFSException, RemoteException
	{
		String customerUNSPSC = "";
		//form the input
		Document inputUnspscDoc = SCXmlUtil.createDocument("XPXB2bLegacyUnspscXref");
		Element inputUnspscElement = inputUnspscDoc.getDocumentElement();
		inputUnspscElement.setAttribute("MasterCustomerID", masterCustomer);
		inputUnspscElement.setAttribute("LegacyItemID", itemID);
		inputUnspscElement.setAttribute("XpedxUNSPSC", xpedxUnspsc);
		Document unspscListDoc = api.executeFlow(env, "getXPXB2bLegacyUnspscXrefList", inputUnspscDoc);
		NodeList unspscNodeList = unspscListDoc.getElementsByTagName("XPXB2bLegacyUnspscXref");
		int unspscLength = unspscNodeList.getLength();
		if(unspscLength != 0)
		{
			//then i need to stamp the customer specific UNSPSC on the order line.
			Element unspscElement = (Element)unspscNodeList.item(0);
			customerUNSPSC = unspscElement.getAttribute("CustomerUNSPSC");
			NodeList itemNodeList = orderLineElement.getElementsByTagName("Item");
			int itemLength = itemNodeList.getLength();
			if(itemLength != 0)
			{
				Element itemElement = (Element)itemNodeList.item(0);
				//NodeList classificationNodeList = itemElement.getElementsByTagName("ClassificationCodes");
				//int classLength = classificationNodeList.getLength();
				//if(classLength != 0)
				//{
				//	Element classElement = (Element)classificationNodeList.item(0);
				//	classElement.setAttribute("UNSPSC", customerUNSPSC);
				//}
				Element classificationElement = inputXML.createElement("ClassificationCodes");
				classificationElement.setAttribute("UNSPSC", customerUNSPSC);
				itemElement.appendChild(classificationElement);
				
			}
			
		}
	}
	public static String getMsap(YFSEnvironment env,String customerID,String orgCode) throws YFSException, RemoteException
	{
		//api = YIFClientFactory.getInstance().getApi();
		String masterCustomer = "";
		try{
			YIFApi api1 = YIFClientFactory.getInstance().getApi();
		
		String customerKey = "";
		Document inputCustomerDoc = SCXmlUtil.createDocument("Customer");
		Element inputCustomerElement = inputCustomerDoc.getDocumentElement();
		inputCustomerElement.setAttribute("CustomerID", customerID);
		inputCustomerElement.setAttribute("OrganizationCode", orgCode);
		env.setApiTemplate("getCustomerList", getCustomerListTemplate);
		Document customerListDoc = api1.invoke(env, "getCustomerList", inputCustomerDoc);
		env.clearApiTemplate("getCustomerList");
		NodeList customerNodeList = customerListDoc.getElementsByTagName("Customer");
		int customerLength = customerNodeList.getLength();
		if(customerLength != 0)
		{
			Element customerElement = (Element)customerNodeList.item(0);
			customerKey = customerElement.getAttribute("RootCustomerKey");
			
		}
		masterCustomer = getCustomer(env,customerKey);

		
		} catch( Exception e){
			e.printStackTrace();
		}
		return masterCustomer;
	}
	
	public static String getCustomer(YFSEnvironment env,String customerKey) throws YFSException, RemoteException
	{
		String customerID = "";
		try{
			YIFApi api2 = YIFClientFactory.getInstance().getApi();

		
		Document customerDoc = SCXmlUtil.createDocument("Customer");
		Element customerElement = customerDoc.getDocumentElement();
		customerElement.setAttribute("CustomerKey", customerKey);
		Document customerListDoc = api2.invoke(env, "getCustomerList", customerDoc);
		NodeList customerNodeList = customerListDoc.getElementsByTagName("Customer");
		int customerLength = customerNodeList.getLength();
		if(customerLength != 0)
		{
			Element msapElement = (Element)customerNodeList.item(0);
			customerID = msapElement.getAttribute("CustomerID");
		}
		} catch( Exception e){
			e.printStackTrace();
		}
		return customerID;
	}
	
	public String getSterlingUom(YFSEnvironment env,String legacyUom, String envID) throws YFSException, RemoteException
	{
		String uom = "";
		//form the input doc
		Document inputLegacyUomDoc = SCXmlUtil.createDocument("XPEDXLegacyUomXref");
		Element inputLegacyUomElement = inputLegacyUomDoc.getDocumentElement();
		inputLegacyUomElement.setAttribute("LegacyUOM", legacyUom);
		inputLegacyUomElement.setAttribute("LegacyType", envID);
		Document outputLegacyUomDoc = api.executeFlow(env, "getXPXLegacyUomXrefList", inputLegacyUomDoc);
		NodeList uomNodeList = outputLegacyUomDoc.getElementsByTagName("XPEDXLegacyUomXref");
		int uomLength = uomNodeList.getLength();
		if(uomLength != 0){
			Element uomElement = (Element)uomNodeList.item(0);
			uom = uomElement.getAttribute("UOM");
		}
		return uom;
		
	}
	
	public String getLegacyUom(YFSEnvironment env,String masterCustomer, String customerUom) throws YFSException, RemoteException
	{
		String legacyUom = "";
		//form the input
		Document inputCustomerUomDoc = SCXmlUtil.createDocument("XPXB2bLegacyUomXref");
		Element inputCustomerUomElement = inputCustomerUomDoc.getDocumentElement();
		inputCustomerUomElement.setAttribute("MasterCustomerID", masterCustomer);
		inputCustomerUomElement.setAttribute("CustomerUOM", customerUom);
		Document outputLegacyUomDoc = api.executeFlow(env, "getXPXB2bLegacyUomXrefList", inputCustomerUomDoc);
		NodeList uomNodeList = outputLegacyUomDoc.getElementsByTagName("XPXB2bLegacyUomXref");
		int uomLength = uomNodeList.getLength();
		if(uomLength != 0)
		{
			Element uomElement = (Element)uomNodeList.item(0);
			legacyUom = uomElement.getAttribute("LegacyUOM");
		}
		return legacyUom;
	}
	
	public static String getCustomerUom(YFSEnvironment env,String masterCustomer, String legacyUom) throws YFSException
	{
		String customerUom = "";
		try{
			YIFApi api3 = YIFClientFactory.getInstance().getApi();

		
		//form the input
		Document inputCustomerUomDoc = SCXmlUtil.createDocument("XPXB2bLegacyUomXref");
		Element inputCustomerUomElement = inputCustomerUomDoc.getDocumentElement();
		inputCustomerUomElement.setAttribute("MasterCustomerID", masterCustomer);
		inputCustomerUomElement.setAttribute("LegacyUOM", customerUom);
		Document outputLegacyUomDoc = api3.executeFlow(env, "getXPXB2bLegacyUomXrefList", inputCustomerUomDoc);
		NodeList uomNodeList = outputLegacyUomDoc.getElementsByTagName("XPXB2bLegacyUomXref");
		int uomLength = uomNodeList.getLength();
		if(uomLength != 0)
		{
			Element uomElement = (Element)uomNodeList.item(0);
			customerUom = uomElement.getAttribute("CustomerUOM");
		}
		} catch( Exception e){
			e.printStackTrace();
		}
		return customerUom;
	}
	
	public static String getCustomerUnspsc(YFSEnvironment env, String masterCustomer,String itemID,String sterlingUnspsc) throws YFSException
	{
		String customerUnspsc = "";
		try{
			YIFApi api4 = YIFClientFactory.getInstance().getApi();

		Document inputSterlingUnspscDoc = SCXmlUtil.createDocument("XPXB2bLegacyUnspscXref");
		Element inputsterlingUnspscElement = inputSterlingUnspscDoc.getDocumentElement();
		inputsterlingUnspscElement.setAttribute("LegacyItemID", itemID);
		inputsterlingUnspscElement.setAttribute("MasterCustomerID", masterCustomer);
		inputsterlingUnspscElement.setAttribute("XpedxUNSPSC", sterlingUnspsc);
		Document outputCustomerUnspscDoc = api4.executeFlow(env, "getXPXB2bLegacyUnspscXrefList", inputSterlingUnspscDoc);
		NodeList unspscNodeList = outputCustomerUnspscDoc.getElementsByTagName("XPXB2bLegacyUnspscXref");
		int unspscLength = unspscNodeList.getLength();
		if(unspscLength != 0){
			Element unspscElement = (Element)unspscNodeList.item(0);
			customerUnspsc = unspscElement.getAttribute("CustomerUNSPSC");
		}
		}catch( Exception e){
			e.printStackTrace();
		}
		
		return customerUnspsc;
		
	}
	
	/**
	 * Gets the Buyer ID stored at SAP level customer.
	 * 
	 * @param env
	 * @param customerID - Ship To ID of the customer.
	 * @param orgCode - Organization code of the customer.
	 * 
	 * @return buyerID - buyer ID of the SAP level customer.
	 * @throws YFSException
	 * @throws RemoteException
	 */
	
	public static String getBuyerID(YFSEnvironment env,String customerID,String orgCode) throws YFSException, RemoteException
	{		
		String billToID = "";
		String SAPCustomerID = "";
		String buyerID = "";
		try{
			YIFApi api1 = YIFClientFactory.getInstance().getApi();
		
		// To get the billToID customer.
		Document inputCustomerDoc = SCXmlUtil.createDocument("Customer");
		Element inputCustomerElement = inputCustomerDoc.getDocumentElement();
		inputCustomerElement.setAttribute("CustomerID", customerID);
		inputCustomerElement.setAttribute("OrganizationCode", orgCode);
		env.setApiTemplate("getCustomerList", getCustomerListTemplate);
		Document shipToCustomerListDoc = api1.invoke(env, "getCustomerList", inputCustomerDoc);		
		billToID = getParentCustomerID(env, shipToCustomerListDoc);	
		
		// To get the SAP level customer ID from BillToID.
		inputCustomerElement.setAttribute("CustomerID", billToID);		
		Document billToCustomerListDoc = api1.invoke(env, "getCustomerList", inputCustomerDoc);	
		SAPCustomerID = getParentCustomerID(env, billToCustomerListDoc);
		
		// To get the buyer id of the SAP customer.
		inputCustomerElement.setAttribute("CustomerID", SAPCustomerID);
		Document SAPCustomerListDoc = api1.invoke(env, "getCustomerList", inputCustomerDoc);
		
		if(SAPCustomerListDoc != null && SAPCustomerListDoc.getDocumentElement().hasChildNodes()){
			Element customerElem = (Element) SAPCustomerListDoc.getDocumentElement().getElementsByTagName("Customer").item(0);
			Element extnElement = (Element) customerElem.getElementsByTagName("Extn").item(0);
			if(extnElement.hasAttribute("ExtnBuyerID")){
			buyerID = extnElement.getAttribute("ExtnBuyerID");
			}			
		}
		
		env.clearApiTemplate("getCustomerList");
		
		} catch( Exception e){
			e.printStackTrace();
		}
		
		if(log.isDebugEnabled()){
	//		log.debug("");
			log.debug("XPXTranslationUtilsAPI : buyerID = " + buyerID);
		}
		if(!YFCObject.isNull(buyerID) && !YFCObject.isVoid(buyerID)) {
		//	log.debug("");
			log.debug("XPXTranslationUtilsAPI : buyerID = " + buyerID);
		}
		
		return buyerID;
	}
	
	/**
	 * Gets the parent customer ID of the customer id passes as input.
	 * 
	 * @param env
	 * @param customerListDoc - Document which holds the customer list.
	 * 
	 * @return parentCustID - parent customer ID of the customer passed.
	 * @throws YFSException
	 * @throws RemoteException
	 */
	
	public static String getParentCustomerID(YFSEnvironment env,Document customerListDoc) throws YFSException, RemoteException
	{
		String parentCustID = "";
		
		if(customerListDoc != null && customerListDoc.hasChildNodes()){
			NodeList customerNodeList = customerListDoc.getElementsByTagName("Customer");			
			if(customerNodeList.getLength() > 0){
				Element customerElement = (Element)customerNodeList.item(0);	
				NodeList parentCustomerList = customerElement.getElementsByTagName("ParentCustomer");
				if(parentCustomerList.getLength() > 0){
				Element parentCustomerElem = (Element) parentCustomerList.item(0);				
				parentCustID = parentCustomerElem.getAttribute("CustomerID");
				}
			}		
		}
		if(log.isDebugEnabled()){
			//log.debug("");
			log.debug("XPXTranslationUtilsAPI : parentCustID = " + parentCustID);
		}
		if(!YFCObject.isNull(parentCustID) && !YFCObject.isVoid(parentCustID)) {
		//	log.debug("");
			log.debug("XPXTranslationUtilsAPI : parentCustID = " + parentCustID);
		}
		
		return parentCustID;
	}

}
