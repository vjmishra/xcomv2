<<<<<<< HEAD
package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
import java.util.Properties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.common.util.XPXTranslationUtilsAPI;
import com.xpedx.nextgen.common.util.XPXUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXUpdateInvoiceDetailsAPI {
	
	private static YIFApi api = null;
	private Properties _prop;
	private static YFCLogCategory log;
	static
	{
	log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	}

	public void setProperties(Properties arg0) throws Exception {
		this._prop = arg0;
		// TODO Auto-generated method stub
	}
	String getOrganizationListTemplate = "global/template/api/getOrganizationList.XPXUpdateInvoiceDetailsAPI.xml";
	String getCustomerListTemplate = "global/template/api/getCustomerList.XPXUpdateInvoiceDetailsAPI.xml";
	String getItemListTemplate = "global/template/api/getItemList.XPXUpdateInvoiceDetailsAPI.xml";
	String getXPXInvoiceHdrListTemplate = "global/template/api/getXPXInvoiceHdrList.XPXUpdateInvoiceDetailsAPI.xml";
	
	public Document updateInvoice(YFSEnvironment env,Document inXML) throws Exception
	{				
		/** try-catch block added by Arun Sekhar on 24-Jan-2011 for CENT tool logging **/
		Document invoiceDetailsDoc = null;
		boolean isThrowingWebConfExcpt = false;
		try{
			log.beginTimer("XPXUpdateInvoiceDetailsAPI.updateInvoice");
			api = YIFClientFactory.getInstance().getApi();
			//get The input
			String orderHeaderKey = "";
			String division = "";
			String customer = "";
			String invoiceNum = "";
			String invoiceDate = "";
			String invoiceTotal = "";
			String webConfNUmber = "";
			String legacyOrderNo = "";
			
			Element inputElement = inXML.getDocumentElement();	
			if(inXML != null){
				log.debug("inXML in updateInvoice(): " + SCXmlUtil.getString(inputElement));
			}
			String isB2BCustomer = inputElement.getAttribute("IsB2BCustomer");
			if(YFCObject.isNull(isB2BCustomer) || YFCObject.isVoid(isB2BCustomer)){
				throw new Exception(" 'isB2BCustomer' flag cannot be empty in Invoice transaction. Flag denotes 'B2B' or 'B2C' "); 	
			}
			
			// Validation for web confirmation number and retrieving it.
			if(inputElement.hasAttribute("WebConfirmationNo")) {
				webConfNUmber = inputElement.getAttribute("WebConfirmationNo");
				if(YFCObject.isNull(webConfNUmber) || YFCObject.isVoid(webConfNUmber)) {
					throw new Exception("Attribute WebConfirmationNumber Cannot be NULL or Void in Incoming Invoice Message!");
				}
			} else {
				throw new Exception("Attribute WebConfirmationNumber Cannot be NULL or Void in Incoming Invoice Message!"); 
			}
			
			// Validation for legacy order number and retrieving it.
			if(inputElement.hasAttribute("LegacyOrderNo")) {
				legacyOrderNo = inputElement.getAttribute("LegacyOrderNo");
				if(YFCObject.isNull(legacyOrderNo) || YFCObject.isVoid(legacyOrderNo)) {
					throw new Exception("Attribute Legacy Order Number Cannot be NULL or Void in Incoming Invoice Message!");
				}
			} else {
				throw new Exception("Attribute Legacy Order Number Cannot be NULL or Void in Incoming Invoice Message!"); 
			}
			
			// To get the invoice details
			if(inputElement.hasAttribute("LDInvoiceNo")){
			invoiceNum = inputElement.getAttribute("LDInvoiceNo");
			}
			if(inputElement.hasAttribute("InvoiceDate")){
			invoiceDate = inputElement.getAttribute("InvoiceDate");
			}
			if(inputElement.hasAttribute("InvoiceTotal")){
			invoiceTotal = inputElement.getAttribute("InvoiceTotal");
			}
				
			// To form input document
			Document inputOrderDoc = YFCDocument.createDocument("Order").getDocument();
			Element inputOrderElement = inputOrderDoc.getDocumentElement();
			Element extnElement = inputOrderDoc.createElement("Extn");
			extnElement.setAttribute("ExtnWebConfNum", webConfNUmber);
			extnElement.setAttribute("ExtnLegacyOrderNo", legacyOrderNo);
			inputOrderElement.appendChild(extnElement);
	  	    Document outputOrderList = api.invoke(env, "getOrderList", inputOrderDoc);
		    NodeList orderList = outputOrderList.getElementsByTagName("Order");
		    int orderLength = orderList.getLength();
			if(orderLength!=0)
				{
					Element orderElement = (Element)orderList.item(0);
					orderHeaderKey = orderElement.getAttribute("OrderHeaderKey");
//					division = orderElement.getAttribute("ShipNode");
//					log.debug("division"+division);
					//customer = orderElement.getAttribute("BillToID");
					customer = orderElement.getAttribute("BuyerOrganizationCode");					
				} else {
					isThrowingWebConfExcpt = true;
					throw new Exception("Webconfirmation number("+webConfNUmber+") and legacy order number("+legacyOrderNo+") combination doesn't exist in Sterling.");
				}
			
			if(isB2BCustomer.equalsIgnoreCase("Y")){
			// Source type B2B
				
			//these attributes come in Invoice feed
			//stampDivisionInfo(env, inputElement, division);
			//log.debug("after division stamp"+SCXmlUtil.getString(inputElement));
			//stampCustomerInfo(env, inputElement, customer);
			//log.debug("after customer stamp"+SCXmlUtil.getString(inputElement));
			//stampInvoiceLineLevelAttribute(env,inputElement);
			
			// form the input for creating invoice elements
			Document inputChangeOrderDoc = YFCDocument.createDocument("Order").getDocument();
			Element inputChangeOrderElement = inputChangeOrderDoc.getDocumentElement();
			inputChangeOrderElement.setAttribute(XPXLiterals.A_OVERRIDE, "Y");			
			inputChangeOrderElement.setAttribute("OrderHeaderKey", orderHeaderKey);
			Element extnOrderElement = inputChangeOrderDoc.createElement("Extn");			
			if(!YFCObject.isNull(invoiceNum) && !YFCObject.isVoid(invoiceNum)){				
			extnOrderElement.setAttribute("ExtnInvoiceNo", invoiceNum);
			}
			if(!YFCObject.isNull(invoiceDate) && !YFCObject.isVoid(invoiceDate)){
			extnOrderElement.setAttribute("ExtnInvoicedDate", invoiceDate);
			}
			if(!YFCObject.isNull(invoiceTotal) && !YFCObject.isVoid(invoiceTotal)){
			extnOrderElement.setAttribute("ExtnInvoiceTotal", invoiceTotal);
			}			
			inputChangeOrderElement.appendChild(extnOrderElement);
			Element invoiceHeaderListElement = inputChangeOrderDoc.createElement("XPXInvoiceHdrList");
			extnOrderElement.appendChild(invoiceHeaderListElement);
			// inputElement.setAttribute("ProcessedFlag", "Y");
			Node orderNode = inputChangeOrderDoc.importNode(inputElement, true);
			Element inputNewElement = (Element)orderNode;
			invoiceHeaderListElement.appendChild(inputNewElement);			
			invoiceDetailsDoc = api.invoke(env, "changeOrder", inputChangeOrderDoc);			
		    // invoiceDetailsDoc = sendInvoiceDetailsToWebMethods(env, inXML, customer);
			if(invoiceDetailsDoc != null){
		    	log.debug("invoiceDetailsDoc for B2B : " + SCXmlUtil.getString(invoiceDetailsDoc));
			}
		    
			} else {
				// Source Type B2C

				// To build change order input doc.
				Document inputChangeOrderDoc = YFCDocument.createDocument("Order").getDocument();
				Element inputChangeOrderElement = inputChangeOrderDoc.getDocumentElement();
				inputChangeOrderElement.setAttribute(XPXLiterals.A_OVERRIDE, "Y");
				inputChangeOrderElement.setAttribute("OrderHeaderKey", orderHeaderKey);
				Element extnOrderElement = inputChangeOrderDoc.createElement("Extn");
				if(!YFCObject.isNull(invoiceNum) && !YFCObject.isVoid(invoiceNum)){				
				extnOrderElement.setAttribute("ExtnInvoiceNo", invoiceNum);
				}
				if(!YFCObject.isNull(invoiceDate) && !YFCObject.isVoid(invoiceDate)){				
				extnOrderElement.setAttribute("ExtnInvoicedDate", invoiceDate);
				}
				if(!YFCObject.isNull(invoiceTotal) && !YFCObject.isVoid(invoiceTotal)){
				extnOrderElement.setAttribute("ExtnInvoiceTotal", invoiceTotal);
				}
				inputChangeOrderElement.appendChild(extnOrderElement);				
				invoiceDetailsDoc = api.invoke(env, "changeOrder", inputChangeOrderDoc);
				if(invoiceDetailsDoc != null){
					log.debug(" invoiceDetailsDoc for B2C = " + SCXmlUtil.getString(invoiceDetailsDoc));
				}
			}
			
			// To change the FO status.
			changeFOStatus(env,orderHeaderKey,"1100.5700");
		    
		}catch (NullPointerException ne) {
			log.error("NullPointerException: " + ne.getStackTrace());	
			prepareErrorObject(ne, XPXLiterals.INV_B_TRANS_TYPE, XPXLiterals.NE_ERROR_CLASS, env, inXML);	
			throw ne;
		} catch (YFSException yfe) {
			log.error("YFSException: " + yfe.getStackTrace());
			prepareErrorObject(yfe, XPXLiterals.INV_B_TRANS_TYPE, XPXLiterals.YFE_ERROR_CLASS, env, inXML);	
			throw yfe;
		} catch (Exception e) {
			log.error("Exception: " + e.getStackTrace());
			if (!isThrowingWebConfExcpt)
				prepareErrorObject(e, XPXLiterals.INV_B_TRANS_TYPE, XPXLiterals.E_ERROR_CLASS, env, inXML);
			throw e;
		}
		return invoiceDetailsDoc;
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
	
	private Document sendInvoiceDetailsToWebMethods(YFSEnvironment env, Document inXML,String customer) throws NullPointerException, Exception
	{
		// This method is used when Invoice message being sent to Customer - XPXSendInvoiceMessage.java
		Document outputInvoiceDoc = null;
		Element inputElement = inXML.getDocumentElement();
		String ldInvoiceNumber = inputElement.getAttribute("LDInvoiceNo");
		//form input doc
		Document inputInvoiceDoc = SCXmlUtil.createDocument("XPXInvoiceHdr");
		Element inputInvoiceElement = inputInvoiceDoc.getDocumentElement();
		inputInvoiceElement.setAttribute("LDInvoiceNo", ldInvoiceNumber);
		env.setApiTemplate("getXPXInvoiceHdrListService", getXPXInvoiceHdrListTemplate);
		Document invoiceHdrListDoc = api.executeFlow(env, "getXPXInvoiceHdrListService", inputInvoiceDoc);
		NodeList invoiceNodeList = invoiceHdrListDoc.getElementsByTagName("XPXInvoiceHdr");
		int invoiceLength = invoiceNodeList.getLength();
		if(invoiceLength != 0)
		{
			Element invoiceElement = (Element)invoiceNodeList.item(0);
			outputInvoiceDoc = YFCDocument.createDocument().getDocument();
			outputInvoiceDoc.appendChild(outputInvoiceDoc.importNode(invoiceElement, true));
			outputInvoiceDoc.renameNode(outputInvoiceDoc.getDocumentElement(), outputInvoiceDoc.getNamespaceURI(), "XPXInvoiceHdr");
			stampCustomerInfo(env,outputInvoiceDoc.getDocumentElement(),customer);
			stampInvoiceLineLevelAttribute(env,outputInvoiceDoc.getDocumentElement());
			
		}
		return outputInvoiceDoc;
	}
	public void stampInvoiceLineLevelAttribute(YFSEnvironment env, Element inputElement) throws NullPointerException, Exception
	{
		String itemID = "";
		String customerID = "";
		String orgCode = "";
		NodeList invoiceLineList = inputElement.getElementsByTagName("XPXInvoiceLine");
		int lineLength = invoiceLineList.getLength();
		if(lineLength != 0)
		{
			for(int lineCounter = 0;lineCounter<lineLength;lineCounter++)
			{
				Element invoiceLineElement = (Element)invoiceLineList.item(lineCounter);
				itemID = invoiceLineElement.getAttribute("LegacyProductCode");
				//these attributes come in Invoice Feed				
				customerID = inputElement.getAttribute("CustomerID");
				orgCode = inputElement.getAttribute("OrganizationCode");
				getItemAttributeDetails(env, itemID, invoiceLineElement,customerID,orgCode);				

				//Code to convert Legacy UOM to Customer UOM
				String baseUom = invoiceLineElement.getAttribute("BaseUom");				
				String convertedBaseUom = XPXUtils.replaceOutgoingUOMFromLegacy(env,baseUom,itemID,inputElement.getAttribute("CustomerBuyerID"),inputElement.getAttribute("ETradingID"));
				invoiceLineElement.setAttribute("BaseUom", convertedBaseUom);
				
				String requestedUom = invoiceLineElement.getAttribute("ReqUom");
				String convertedReqUom = XPXUtils.replaceOutgoingUOMFromLegacy(env,requestedUom,itemID,inputElement.getAttribute("CustomerBuyerID"),inputElement.getAttribute("ETradingID"));
				invoiceLineElement.setAttribute("ReqUom", convertedReqUom);
			}
		}
	}
	
	public void getItemAttributeDetails(YFSEnvironment env, String itemID, Element invoiceLineElement,String customerID,String orgCode) throws YFSException, RemoteException
	{
		//String fscCertCode = "";
		//String sfiCertCode = "";
		//String pefcCertCode = "";
		String basisWeight = "";
		String rollWidth = "";
		String rollDiameter = "";
		String brand = "";
		String name = "";
		String xpedxUNSPSC = "";
		String customerUNSPSC = "";
		String replacedUNSPSC = "";
		//form the input item doc
		Document inputItemDoc = SCXmlUtil.createDocument("Item");
		Element inputItemElement = inputItemDoc.getDocumentElement();
		inputItemElement.setAttribute("ItemID", itemID);
		env.setApiTemplate("getItemList", getItemListTemplate);
		Document itemListDoc = api.invoke(env, "getItemList", inputItemDoc);
		env.clearApiTemplate("getItemList");
		/*NodeList additionalAttrNodeList = itemListDoc.getElementsByTagName("AdditionalAttribute");
		int attributeLength = additionalAttrNodeList.getLength();
		HashMap<String, String> itemMap = new HashMap<String, String>();
		if(attributeLength != 0)
		{
			for(int linecounter = 0;linecounter<attributeLength;linecounter++)
			{
				Element attributeElement = (Element)additionalAttrNodeList.item(linecounter);
				name = attributeElement.getAttribute("Name");
				//if(name.equals("FSCCertCode"))
				//{
				//	fscCertCode = attributeElement.getAttribute("Value");
				//	itemMap.put("FSCCertCode", fscCertCode);
				//}
				//if(name.equals("SFICertCode"))
				//{
				//	sfiCertCode = attributeElement.getAttribute("Value");
				//	itemMap.put("SFICertCode", sfiCertCode);
				//}
				//if(name.equals("PEFCCertCode"))
				//{
				//	pefcCertCode = attributeElement.getAttribute("Value");
				//	itemMap.put("PEFCCertCode", pefcCertCode);
				//}
				if(name.equals("BasisWeight"))
				{
					basisWeight = attributeElement.getAttribute("Value");
					itemMap.put("BasisWeight", basisWeight);
				}
				if(name.equals("RollWidth"))
				{
					rollWidth = attributeElement.getAttribute("Value");
					itemMap.put("RollWidth", rollWidth);
				}
				if(name.equals("RollDiameter"))
				{
					rollDiameter = attributeElement.getAttribute("Value");
					itemMap.put("RollDiameter", rollDiameter);
				}
				if(name.equals("Brand"))
				{
					brand = attributeElement.getAttribute("Value");
					log.debug("brand"+brand);
					itemMap.put("Brand", brand);
				}
			}
	
		}
		log.debug("itemmap size"+itemMap.size());
		log.debug("brand"+itemMap.get("Brand"));
		Collection<String> c = itemMap.values();
		Iterator<String> i = c.iterator();
		while(i.hasNext())
		{
			log.debug("brand"+i.next());
		}
		//invoiceLineElement.setAttribute("FSCCertCode", fscCertCode);
		//invoiceLineElement.setAttribute("SFICertCode", sfiCertCode);
		//invoiceLineElement.setAttribute("PEFCCertCode", pefcCertCode);
		//invoiceLineElement.setAttribute("BasisWeight", basisWeight);
		//invoiceLineElement.setAttribute("RollWidth", rollWidth);
		//invoiceLineElement.setAttribute("RollDiameter", rollDiameter);
		//invoiceLineElement.setAttribute("Brand", brand);
		//invoiceLineElement.setAttribute("FSCCertCode", itemMap.get("FSCCertCode"));
		//invoiceLineElement.setAttribute("SFICertCode", itemMap.get("SFICertCode"));
		//invoiceLineElement.setAttribute("PEFCCertCode", itemMap.get("PEFCCertCode"));*/
		NodeList itemNodeList = itemListDoc.getElementsByTagName("Item");
		int length = itemNodeList.getLength();
		if(length != 0)
		{
			Element itemElement = (Element)itemNodeList.item(0);
			invoiceLineElement.setAttribute("BasisWeight", SCXmlUtil.getXpathAttribute(itemElement, "./Extn/@ExtnBasis"));
			invoiceLineElement.setAttribute("RollWidth", SCXmlUtil.getXpathAttribute(itemElement, "./Extn/@ExtnRollWidth"));
			invoiceLineElement.setAttribute("RollDiameter", SCXmlUtil.getXpathAttribute(itemElement, "./Extn/@ExtnRollDiameter"));
			invoiceLineElement.setAttribute("Brand", SCXmlUtil.getXpathAttribute(itemElement, "./Extn/@ExtnBrand"));						
			invoiceLineElement.setAttribute("PklvOneUPC", SCXmlUtil.getXpathAttribute(itemElement, "./Extn/@ExtnPKLV1UPC"));
			invoiceLineElement.setAttribute("PklvTwoUPC", SCXmlUtil.getXpathAttribute(itemElement, "./Extn/@ExtnPKLV2UPC"));
			invoiceLineElement.setAttribute("PklvThreeUPC", SCXmlUtil.getXpathAttribute(itemElement, "./Extn/@ExtnPKLV3UPC"));
			invoiceLineElement.setAttribute("PklvFourUPC", SCXmlUtil.getXpathAttribute(itemElement, "./Extn/@ExtnPKLV4UPC"));
			invoiceLineElement.setAttribute("PklvFiveUPC", SCXmlUtil.getXpathAttribute(itemElement, "./Extn/@ExtnPKLV5UPC"));
			
			// UNSPSC replacement.			
			xpedxUNSPSC = SCXmlUtil.getXpathAttribute(itemElement, "./Extn/@ExtnUNSPSC");
			if(!YFCObject.isNull(xpedxUNSPSC) && !YFCObject.isVoid(xpedxUNSPSC)) {
				log.debug("xpedxUNSPSC : " + xpedxUNSPSC);
			}
			if(!YFCObject.isNull(xpedxUNSPSC) && !YFCObject.isVoid(xpedxUNSPSC)){
				customerUNSPSC = translateUnspscSterlingToCustomerInvoice(env,customerID,itemID,orgCode,xpedxUNSPSC);
				if(!YFCObject.isNull(customerUNSPSC) && !YFCObject.isVoid(customerUNSPSC)) {
					log.debug("customerUNSPSC : " + customerUNSPSC);
				}
				if( !YFCObject.isNull(customerUNSPSC) && !YFCObject.isVoid(customerUNSPSC)){
					replacedUNSPSC = customerUNSPSC;
				} else {
					replacedUNSPSC = xpedxUNSPSC;
				}
			} 
			if(!YFCObject.isNull(replacedUNSPSC) && !YFCObject.isVoid(replacedUNSPSC)) {
				log.debug("replacedUNSPSC : " + replacedUNSPSC);
			}
			invoiceLineElement.setAttribute("Unspsc", replacedUNSPSC);
		}
		if(invoiceLineElement != null){
			log.debug("invoiceLineElement: "+SCXmlUtil.getString(invoiceLineElement));
		}
	}
	
	/*private void stampDivisionInfo(YFSEnvironment env, Element inputElement, String division) throws YFSException, RemoteException
	{
		String remitToDunsNo = "";
		String remitToName = "";
		String remitAddr1 = "";
		String remitAddr2 = "";
		String remitCity = "";
		String remitState = "";
		String remitZip = "";
		//form the input document
		Document inputOrgDoc = SCXmlUtil.createDocument("Organization");
		Element inputOrgElement = inputOrgDoc.getDocumentElement();
		inputOrgElement.setAttribute("OrganizationCode", division);
		env.setApiTemplate("getOrganizationList", getOrganizationListTemplate);
		Document orgListDoc = api.invoke(env, "getOrganizationList", inputOrgDoc);
		NodeList orgList = orgListDoc.getElementsByTagName("Organization");
		int orgLength = orgList.getLength();
		if(orgLength != 0)
		{
			Element orgElement = (Element)orgList.item(0);
			remitToDunsNo = SCXmlUtil.getXpathAttribute(orgElement, "ExtnRemitToDunsNo");
			remitToName = SCXmlUtil.getXpathAttribute(orgElement, "ExtnRemitToName");
			remitAddr1 = SCXmlUtil.getXpathAttribute(orgElement, "ExtnRemitAddr1");
			remitAddr2 = SCXmlUtil.getXpathAttribute(orgElement, "ExtnRemitAddr2");
			remitCity = SCXmlUtil.getXpathAttribute(orgElement, "ExtnRemitCity");
			remitState = SCXmlUtil.getXpathAttribute(orgElement, "ExtnRemitState");
			remitZip = SCXmlUtil.getXpathAttribute(orgElement, "ExtnRemitZip");
			inputElement.setAttribute("RemitToDunsNo", remitToDunsNo);
			inputElement.setAttribute("RemitToName", remitToName);
			inputElement.setAttribute("RemitAddr1", remitAddr1);
			inputElement.setAttribute("RemitAddr2", remitAddr2);
			inputElement.setAttribute("RemitCity", remitCity);
			inputElement.setAttribute("RemitState", remitState);
			inputElement.setAttribute("RemitZip", remitZip);
			
			
		}
		
		
		
	}*/
	
	public void stampCustomerInfo(YFSEnvironment env, Element inputElement, String customer) throws YFSException, RemoteException, YIFClientCreationException
	{		
		String mSAPCustomerKey = "";
		String billToDunsNo = "";
		String sellerDunsNo = "";
		String shipToStoreNo = "";
		//String naCustomerNo = "";
		String eTradingID = "";
		String customerBuyerID = "";
		//String customerEnvID = "";
		String b2bInvoiceFlag = "";
		//form input customer doc
		Document inputCustomerDoc = SCXmlUtil.createDocument("Customer");
		Element inputCustomerElement = inputCustomerDoc.getDocumentElement();
		inputCustomerElement.setAttribute("CustomerID", customer);
		env.setApiTemplate("getCustomerList", getCustomerListTemplate);		
		api = YIFClientFactory.getInstance().getLocalApi();
		Document customerListDoc = api.invoke(env, "getCustomerList", inputCustomerDoc);
		NodeList customerList = customerListDoc.getElementsByTagName("Customer");
		int customerLength = customerList.getLength();
		if(customerLength != 0)
		{
			Element customerElement = (Element)customerList.item(0);
             //	Commented out to retrieve the flag from the SAP customer profile as per inheritance design
			//b2bInvoiceFlag = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@Extn810InvoiceFlag");
			
			/**** Start -  Added code to get the Buyer ID from MSAP level. ***/
			if(customerElement.hasAttribute("RootCustomerKey")){
				mSAPCustomerKey = customerElement.getAttribute("RootCustomerKey");
				Document inputMSAPCustomerDoc = SCXmlUtil.createDocument("Customer");
				inputMSAPCustomerDoc.getDocumentElement().setAttribute("CustomerKey", mSAPCustomerKey);		
				api = YIFClientFactory.getInstance().getLocalApi();
				Document mSAPCustListOutputDoc = api.invoke(env, "getCustomerList", inputMSAPCustomerDoc);				
				NodeList mSAPCustomerList = mSAPCustListOutputDoc.getElementsByTagName("Customer");
				int mSAPCustomerLength = mSAPCustomerList.getLength();
				if(mSAPCustomerLength != 0){
					Element mSAPCustomerElement = (Element)mSAPCustomerList.item(0);
					customerBuyerID = SCXmlUtil.getXpathAttribute(mSAPCustomerElement, "./Extn/@ExtnBuyerID");

					if(!YFCObject.isNull(customerBuyerID) && !YFCObject.isVoid(customerBuyerID)) {
						log.debug("");
						log.debug("MSAP_customerBuyerID:" + customerBuyerID);
					}
					if(log.isDebugEnabled()){
						log.debug("MSAP_customerBuyerID:" + customerBuyerID);
					}
				}
			}		
			/**** End -  Added code to get the Buyer ID from MSAP level. ***/
				
			/*********Added code to retrieve flag from SAP customer profile*********/
			Element parentCustomerElement = (Element) customerElement.getElementsByTagName("ParentCustomer").item(0);
			String billToCustomerId  = parentCustomerElement.getAttribute("CustomerID");
			
			inputCustomerElement.setAttribute("CustomerID", billToCustomerId);
			Document getBillToCustomerListOutputDoc = api.invoke(env, "getCustomerList", inputCustomerDoc);
			if(log.isDebugEnabled()){
				log.debug("The second getCustomerList is: "+SCXmlUtil.getString(getBillToCustomerListOutputDoc));
			}
			Element billToCustomerElement = (Element) getBillToCustomerListOutputDoc.getDocumentElement().getElementsByTagName("Customer").item(0);
			Element billToParentCustomerElement = (Element) billToCustomerElement.getElementsByTagName("ParentCustomer").item(0);
			String sapCustomerId  = billToParentCustomerElement.getAttribute("CustomerID");
			
			inputCustomerElement.setAttribute("CustomerID", sapCustomerId);
			Document getSAPCustomerListOutputDoc = api.invoke(env, "getCustomerList", inputCustomerDoc);
			if(log.isDebugEnabled()){
				log.debug("The third getCustomerList is: "+SCXmlUtil.getString(getSAPCustomerListOutputDoc));
			}
			Element sapCustomerElement = (Element) getSAPCustomerListOutputDoc.getDocumentElement().getElementsByTagName("Customer").item(0);
			Element sapExtnCustomerElement = (Element) sapCustomerElement.getElementsByTagName("Extn").item(0);
			
			b2bInvoiceFlag  = sapExtnCustomerElement.getAttribute("Extn810InvoiceFlag");
			/*****************************************************************************/
			
			billToDunsNo = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnBillToDunsNo");
			sellerDunsNo = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnSellerDunsNo");
			shipToStoreNo = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnShipToStoreNo");
			//naCustomerNo = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnNACustomerNo");
			eTradingID = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnETradingID");		
			// customerBuyerID = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnBuyerID");
			//customerEnvID = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnEnvironmentCode");
			inputElement.setAttribute("BillToDunsNo", billToDunsNo);
			inputElement.setAttribute("SellerDunsNo", sellerDunsNo);
			inputElement.setAttribute("ShipToStoreNo", shipToStoreNo);
			//inputElement.setAttribute("NACustomerNo", naCustomerNo);
			inputElement.setAttribute("ETradingID", eTradingID);
			inputElement.setAttribute("CustomerBuyerID", customerBuyerID);
			inputElement.setAttribute("B2BInvoiceFlag", b2bInvoiceFlag);
			//inputElement.setAttribute("CustomerEnvironmentId", customerEnvID);
			
			/* The attributes set below are used for UNSPSC replacement.*/
			inputElement.setAttribute("CustomerID", customer);
			inputElement.setAttribute("OrganizationCode", customerElement.getAttribute("OrganizationCode") );			
			
			if(!b2bInvoiceFlag.equals("Y"))
			{				
				// Error logged in CENT tool.
				YFSException exceptionMessage = new YFSException();
				exceptionMessage.setErrorDescription("Customer Profile field for 810 B2B Invoices is set to 'N' ");		
				log.error("Customer Profile field for 810 B2B Invoices is set to 'N' ");				
				throw exceptionMessage;
				
				// To stop sending B2B Invoice incase invoice flag is not 'Y'.				
				// inputElement.setAttribute("ErrorFlag", "Y");
			}
		}
		
	}
	
	/**
	 * 	To set the status of Fulfillment Order.
	 * 
	 *  @param env Environment variable.
	 *  @param fOrderEle Element contains the Fulfillment Order information.
	 *  @param toStatus Drop status.	                         
	 *	@return tempDoc Output document of the Change Order Status API. 	 
	 *                                              
	 */
	
	private YFCDocument changeFOStatus(YFSEnvironment env, String ordHeaderKey, String toStatus) throws Exception {
		
		YFCDocument chngFOStatusOutXML = null;
				
		if(YFCObject.isNull(ordHeaderKey) || YFCObject.isNull(ordHeaderKey)) {
			throw new Exception("OrderHeaderKey Cannot be NULL or Void!");
		}
		// To set the transaction Id.
		String tranId = null;
		if(this._prop != null) {
			tranId = this._prop.getProperty("XPX_CHNG_ORD_STATUS_TXN");
			if(YFCObject.isNull(tranId) || YFCObject.isVoid(tranId)) {
				throw new Exception("TranstionID Not Configured in API Arguments!");
			}
		} else {
			throw new Exception("Arguments Not Configured For API-XPXPerformLegacyOrderUpdateAPI!");
		}
		// To build the input document to call changeOrderStatus API.
		YFCDocument chngOrdStatusInXML = YFCDocument.getDocumentFor("<OrderStatusChange/>");
		YFCElement ordChngEle = chngOrdStatusInXML.getDocumentElement();
		ordChngEle.setAttribute("OrderHeaderKey", ordHeaderKey);
		ordChngEle.setAttribute("IgnoreTransactionDependencies", "Y");
		ordChngEle.setAttribute("ChangeForAllAvailableQty", "Y");
		ordChngEle.setAttribute("TransactionId", tranId);
		ordChngEle.setAttribute("BaseDropStatus", toStatus);
		if(log.isDebugEnabled()){
			log.debug("");
			log.debug("OrderStatus:"+toStatus);
			log.debug("ChangeFOStatus-InXML:"+chngOrdStatusInXML.getString());
		}
		if(log.isVerboseEnabled()){
			log.verbose("");
			log.verbose("OrderStatus:"+toStatus);
			log.verbose("ChangeFOStatus-InXML:"+chngOrdStatusInXML.getString());
		}
		// To call changeOrderStatus API.
		Document tempDoc = this.api.executeFlow(env, "XPXChangeOrderStatus", chngOrdStatusInXML.getDocument());
		if(tempDoc != null) {
			chngFOStatusOutXML = YFCDocument.getDocumentFor(tempDoc);
		} else {
			throw new Exception("Service XPXChangeOrderStatus Failed!");
		}
		return chngFOStatusOutXML;
	}
	
	/* This method is used to change xpedx UNSPSC to customer UNSPSC for Invoice Transaction 810 */
	
	public String translateUnspscSterlingToCustomerInvoice(YFSEnvironment env, String customerID,String itemID,String orgCode,String xpedxUnspsc) throws YFSException, RemoteException
	{
		String masterCustomer = "";		
		String customerUnspsc = "";
		String legacyItemID = "";
		String buyerID = "";
		
		// masterCustomer = XPXTranslationUtilsAPI.getMsap(env, customerID, orgCode);
		
		// To get the Buyer ID from SAP level customer.
		buyerID = XPXTranslationUtilsAPI.getBuyerID(env, customerID, orgCode);		
		if(!YFCObject.isNull(buyerID) && !YFCObject.isVoid(buyerID)){
			// To form the input document to call the API.
			Document inputUnspscDoc = SCXmlUtil.createDocument("XPXB2bLegacyUnspscXref");
			Element inputUnspscElement = inputUnspscDoc.getDocumentElement();
			inputUnspscElement.setAttribute("MasterCustomerID", buyerID);
			inputUnspscElement.setAttribute("XpedxUNSPSC", xpedxUnspsc);
			Document outputUnspsc = api.executeFlow(env, "getXPXB2BLegacyUnspscXrefList", inputUnspscDoc);		
			if(outputUnspsc != null && outputUnspsc.hasChildNodes()){				
			NodeList unspscNodeList = outputUnspsc.getElementsByTagName("XPXB2bLegacyUnspscXref");
			int unspscLength = unspscNodeList.getLength();
				for(int i=0;i<unspscLength;i++){				
					Element unspscElement = (Element)unspscNodeList.item(i);
					legacyItemID = unspscElement.getAttribute("LegacyItemID");					
					if(!YFCObject.isNull(legacyItemID) && legacyItemID.equalsIgnoreCase(itemID)){					
						customerUnspsc = unspscElement.getAttribute("CustomerUNSPSC");
						break;
					} // If there is no Item ID match then the last customerUNSPSC of the MasterCustomerID should be fetched.  
					else if(i == (unspscLength-1)){
						customerUnspsc = unspscElement.getAttribute("CustomerUNSPSC");	
					}
				}
			} 
		}	
		return customerUnspsc;
	}
=======
package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
import java.util.Properties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.common.util.XPXTranslationUtilsAPI;
import com.xpedx.nextgen.common.util.XPXUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXUpdateInvoiceDetailsAPI {
	
	private static YIFApi api = null;
	private Properties _prop;
	private static YFCLogCategory log;
	static
	{
	log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	}

	public void setProperties(Properties arg0) throws Exception {
		this._prop = arg0;
		// TODO Auto-generated method stub
	}
	String getOrganizationListTemplate = "global/template/api/getOrganizationList.XPXUpdateInvoiceDetailsAPI.xml";
	String getCustomerListTemplate = "global/template/api/getCustomerList.XPXUpdateInvoiceDetailsAPI.xml";
	String getItemListTemplate = "global/template/api/getItemList.XPXUpdateInvoiceDetailsAPI.xml";
	String getXPXInvoiceHdrListTemplate = "global/template/api/getXPXInvoiceHdrList.XPXUpdateInvoiceDetailsAPI.xml";
	
	public Document updateInvoice(YFSEnvironment env,Document inXML) throws Exception
	{				
		/** try-catch block added by Arun Sekhar on 24-Jan-2011 for CENT tool logging **/
		Document invoiceDetailsDoc = null;
		boolean isThrowingWebConfExcpt = false;
		try{
			log.beginTimer("XPXUpdateInvoiceDetailsAPI.updateInvoice");
			api = YIFClientFactory.getInstance().getApi();
			//get The input
			String orderHeaderKey = "";
			String division = "";
			String customer = "";
			String invoiceNum = "";
			String invoiceDate = "";
			String invoiceTotal = "";
			String webConfNUmber = "";
			String legacyOrderNo = "";
			
			Element inputElement = inXML.getDocumentElement();	
			if(inXML != null){
				log.debug("inXML in updateInvoice(): " + SCXmlUtil.getString(inputElement));
			}
			String isB2BCustomer = inputElement.getAttribute("IsB2BCustomer");
			if(YFCObject.isNull(isB2BCustomer) || YFCObject.isVoid(isB2BCustomer)){
				throw new Exception(" 'isB2BCustomer' flag cannot be empty in Invoice transaction. Flag denotes 'B2B' or 'B2C' "); 	
			}
			
			// Validation for web confirmation number and retrieving it.
			if(inputElement.hasAttribute("WebConfirmationNo")) {
				webConfNUmber = inputElement.getAttribute("WebConfirmationNo");
				if(YFCObject.isNull(webConfNUmber) || YFCObject.isVoid(webConfNUmber)) {
					throw new Exception("Attribute WebConfirmationNumber Cannot be NULL or Void in Incoming Invoice Message!");
				}
			} else {
				throw new Exception("Attribute WebConfirmationNumber Cannot be NULL or Void in Incoming Invoice Message!"); 
			}
			
			// Validation for legacy order number and retrieving it.
			if(inputElement.hasAttribute("LegacyOrderNo")) {
				legacyOrderNo = inputElement.getAttribute("LegacyOrderNo");
				if(YFCObject.isNull(legacyOrderNo) || YFCObject.isVoid(legacyOrderNo)) {
					throw new Exception("Attribute Legacy Order Number Cannot be NULL or Void in Incoming Invoice Message!");
				}
			} else {
				throw new Exception("Attribute Legacy Order Number Cannot be NULL or Void in Incoming Invoice Message!"); 
			}
			
			// To get the invoice details
			if(inputElement.hasAttribute("LDInvoiceNo")){
			invoiceNum = inputElement.getAttribute("LDInvoiceNo");
			}
			if(inputElement.hasAttribute("InvoiceDate")){
			invoiceDate = inputElement.getAttribute("InvoiceDate");
			}
			if(inputElement.hasAttribute("InvoiceTotal")){
			invoiceTotal = inputElement.getAttribute("InvoiceTotal");
			}
				
			// To form input document
			Document inputOrderDoc = YFCDocument.createDocument("Order").getDocument();
			Element inputOrderElement = inputOrderDoc.getDocumentElement();
			Element extnElement = inputOrderDoc.createElement("Extn");
			extnElement.setAttribute("ExtnWebConfNum", webConfNUmber);
			extnElement.setAttribute("ExtnLegacyOrderNo", legacyOrderNo);
			inputOrderElement.appendChild(extnElement);
	  	    Document outputOrderList = api.invoke(env, "getOrderList", inputOrderDoc);
		    NodeList orderList = outputOrderList.getElementsByTagName("Order");
		    int orderLength = orderList.getLength();
			if(orderLength!=0)
				{
					Element orderElement = (Element)orderList.item(0);
					orderHeaderKey = orderElement.getAttribute("OrderHeaderKey");
//					division = orderElement.getAttribute("ShipNode");
//					log.debug("division"+division);
					//customer = orderElement.getAttribute("BillToID");
					customer = orderElement.getAttribute("BuyerOrganizationCode");					
				} else {
					isThrowingWebConfExcpt = true;
					throw new Exception("Webconfirmation number("+webConfNUmber+") and legacy order number("+legacyOrderNo+") combination doesn't exist in Sterling.");
				}
			
			if(isB2BCustomer.equalsIgnoreCase("Y")){
			// Source type B2B
				
			//these attributes come in Invoice feed
			//stampDivisionInfo(env, inputElement, division);
			//log.debug("after division stamp"+SCXmlUtil.getString(inputElement));
			//stampCustomerInfo(env, inputElement, customer);
			//log.debug("after customer stamp"+SCXmlUtil.getString(inputElement));
			//stampInvoiceLineLevelAttribute(env,inputElement);
			
			// form the input for creating invoice elements
			Document inputChangeOrderDoc = YFCDocument.createDocument("Order").getDocument();
			Element inputChangeOrderElement = inputChangeOrderDoc.getDocumentElement();
			inputChangeOrderElement.setAttribute(XPXLiterals.A_OVERRIDE, "Y");			
			inputChangeOrderElement.setAttribute("OrderHeaderKey", orderHeaderKey);
			Element extnOrderElement = inputChangeOrderDoc.createElement("Extn");			
			if(!YFCObject.isNull(invoiceNum) && !YFCObject.isVoid(invoiceNum)){				
			extnOrderElement.setAttribute("ExtnInvoiceNo", invoiceNum);
			}
			if(!YFCObject.isNull(invoiceDate) && !YFCObject.isVoid(invoiceDate)){
			extnOrderElement.setAttribute("ExtnInvoicedDate", invoiceDate);
			}
			if(!YFCObject.isNull(invoiceTotal) && !YFCObject.isVoid(invoiceTotal)){
			extnOrderElement.setAttribute("ExtnInvoiceTotal", invoiceTotal);
			}			
			inputChangeOrderElement.appendChild(extnOrderElement);
			Element invoiceHeaderListElement = inputChangeOrderDoc.createElement("XPXInvoiceHdrList");
			extnOrderElement.appendChild(invoiceHeaderListElement);
			// inputElement.setAttribute("ProcessedFlag", "Y");
			Node orderNode = inputChangeOrderDoc.importNode(inputElement, true);
			Element inputNewElement = (Element)orderNode;
			invoiceHeaderListElement.appendChild(inputNewElement);			
			invoiceDetailsDoc = api.invoke(env, "changeOrder", inputChangeOrderDoc);			
		    // invoiceDetailsDoc = sendInvoiceDetailsToWebMethods(env, inXML, customer);
			if(invoiceDetailsDoc != null){
		    	log.debug("invoiceDetailsDoc for B2B : " + SCXmlUtil.getString(invoiceDetailsDoc));
			}
		    
			} else {
				// Source Type B2C

				// To build change order input doc.
				Document inputChangeOrderDoc = YFCDocument.createDocument("Order").getDocument();
				Element inputChangeOrderElement = inputChangeOrderDoc.getDocumentElement();
				inputChangeOrderElement.setAttribute(XPXLiterals.A_OVERRIDE, "Y");
				inputChangeOrderElement.setAttribute("OrderHeaderKey", orderHeaderKey);
				Element extnOrderElement = inputChangeOrderDoc.createElement("Extn");
				if(!YFCObject.isNull(invoiceNum) && !YFCObject.isVoid(invoiceNum)){				
				extnOrderElement.setAttribute("ExtnInvoiceNo", invoiceNum);
				}
				if(!YFCObject.isNull(invoiceDate) && !YFCObject.isVoid(invoiceDate)){				
				extnOrderElement.setAttribute("ExtnInvoicedDate", invoiceDate);
				}
				if(!YFCObject.isNull(invoiceTotal) && !YFCObject.isVoid(invoiceTotal)){
				extnOrderElement.setAttribute("ExtnInvoiceTotal", invoiceTotal);
				}
				inputChangeOrderElement.appendChild(extnOrderElement);				
				invoiceDetailsDoc = api.invoke(env, "changeOrder", inputChangeOrderDoc);
				if(invoiceDetailsDoc != null){
					log.debug(" invoiceDetailsDoc for B2C = " + SCXmlUtil.getString(invoiceDetailsDoc));
				}
			}
			
			// To change the FO status.
			changeFOStatus(env,orderHeaderKey,"1100.5700");
		    
		}catch (NullPointerException ne) {
			log.error("NullPointerException: " + ne.getStackTrace());	
			prepareErrorObject(ne, XPXLiterals.INV_B_TRANS_TYPE, XPXLiterals.NE_ERROR_CLASS, env, inXML);	
			throw ne;
		} catch (YFSException yfe) {
			log.error("YFSException: " + yfe.getStackTrace());
			prepareErrorObject(yfe, XPXLiterals.INV_B_TRANS_TYPE, XPXLiterals.YFE_ERROR_CLASS, env, inXML);	
			throw yfe;
		} catch (Exception e) {
			log.error("Exception: " + e.getStackTrace());
			if (!isThrowingWebConfExcpt)
				prepareErrorObject(e, XPXLiterals.INV_B_TRANS_TYPE, XPXLiterals.E_ERROR_CLASS, env, inXML);
			throw e;
		}
		return invoiceDetailsDoc;
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
	
	private Document sendInvoiceDetailsToWebMethods(YFSEnvironment env, Document inXML,String customer) throws NullPointerException, Exception
	{
		// This method is used when Invoice message being sent to Customer - XPXSendInvoiceMessage.java
		Document outputInvoiceDoc = null;
		Element inputElement = inXML.getDocumentElement();
		String ldInvoiceNumber = inputElement.getAttribute("LDInvoiceNo");
		//form input doc
		Document inputInvoiceDoc = SCXmlUtil.createDocument("XPXInvoiceHdr");
		Element inputInvoiceElement = inputInvoiceDoc.getDocumentElement();
		inputInvoiceElement.setAttribute("LDInvoiceNo", ldInvoiceNumber);
		env.setApiTemplate("getXPXInvoiceHdrListService", getXPXInvoiceHdrListTemplate);
		Document invoiceHdrListDoc = api.executeFlow(env, "getXPXInvoiceHdrListService", inputInvoiceDoc);
		NodeList invoiceNodeList = invoiceHdrListDoc.getElementsByTagName("XPXInvoiceHdr");
		int invoiceLength = invoiceNodeList.getLength();
		if(invoiceLength != 0)
		{
			Element invoiceElement = (Element)invoiceNodeList.item(0);
			outputInvoiceDoc = YFCDocument.createDocument().getDocument();
			outputInvoiceDoc.appendChild(outputInvoiceDoc.importNode(invoiceElement, true));
			outputInvoiceDoc.renameNode(outputInvoiceDoc.getDocumentElement(), outputInvoiceDoc.getNamespaceURI(), "XPXInvoiceHdr");
			stampCustomerInfo(env,outputInvoiceDoc.getDocumentElement(),customer);
			stampInvoiceLineLevelAttribute(env,outputInvoiceDoc.getDocumentElement());
			
		}
		return outputInvoiceDoc;
	}
	public void stampInvoiceLineLevelAttribute(YFSEnvironment env, Element inputElement) throws NullPointerException, Exception
	{
		String itemID = "";
		String customerID = "";
		String orgCode = "";
		NodeList invoiceLineList = inputElement.getElementsByTagName("XPXInvoiceLine");
		int lineLength = invoiceLineList.getLength();
		if(lineLength != 0)
		{
			for(int lineCounter = 0;lineCounter<lineLength;lineCounter++)
			{
				Element invoiceLineElement = (Element)invoiceLineList.item(lineCounter);
				itemID = invoiceLineElement.getAttribute("LegacyProductCode");
				//these attributes come in Invoice Feed				
				customerID = inputElement.getAttribute("CustomerID");
				orgCode = inputElement.getAttribute("OrganizationCode");
				getItemAttributeDetails(env, itemID, invoiceLineElement,customerID,orgCode);				

				//Code to convert Legacy UOM to Customer UOM
				String baseUom = invoiceLineElement.getAttribute("BaseUom");				
				String convertedBaseUom = XPXUtils.replaceOutgoingUOMFromLegacy(env,baseUom,itemID,inputElement.getAttribute("CustomerBuyerID"),inputElement.getAttribute("ETradingID"));
				invoiceLineElement.setAttribute("BaseUom", convertedBaseUom);
				
				String requestedUom = invoiceLineElement.getAttribute("ReqUom");
				String convertedReqUom = XPXUtils.replaceOutgoingUOMFromLegacy(env,requestedUom,itemID,inputElement.getAttribute("CustomerBuyerID"),inputElement.getAttribute("ETradingID"));
				invoiceLineElement.setAttribute("ReqUom", convertedReqUom);
			}
		}
	}
	
	public void getItemAttributeDetails(YFSEnvironment env, String itemID, Element invoiceLineElement,String customerID,String orgCode) throws YFSException, RemoteException
	{
		//String fscCertCode = "";
		//String sfiCertCode = "";
		//String pefcCertCode = "";
		String basisWeight = "";
		String rollWidth = "";
		String rollDiameter = "";
		String brand = "";
		String name = "";
		String xpedxUNSPSC = "";
		String customerUNSPSC = "";
		String replacedUNSPSC = "";
		//form the input item doc
		Document inputItemDoc = SCXmlUtil.createDocument("Item");
		Element inputItemElement = inputItemDoc.getDocumentElement();
		inputItemElement.setAttribute("ItemID", itemID);
		env.setApiTemplate("getItemList", getItemListTemplate);
		Document itemListDoc = api.invoke(env, "getItemList", inputItemDoc);
		env.clearApiTemplate("getItemList");
		/*NodeList additionalAttrNodeList = itemListDoc.getElementsByTagName("AdditionalAttribute");
		int attributeLength = additionalAttrNodeList.getLength();
		HashMap<String, String> itemMap = new HashMap<String, String>();
		if(attributeLength != 0)
		{
			for(int linecounter = 0;linecounter<attributeLength;linecounter++)
			{
				Element attributeElement = (Element)additionalAttrNodeList.item(linecounter);
				name = attributeElement.getAttribute("Name");
				//if(name.equals("FSCCertCode"))
				//{
				//	fscCertCode = attributeElement.getAttribute("Value");
				//	itemMap.put("FSCCertCode", fscCertCode);
				//}
				//if(name.equals("SFICertCode"))
				//{
				//	sfiCertCode = attributeElement.getAttribute("Value");
				//	itemMap.put("SFICertCode", sfiCertCode);
				//}
				//if(name.equals("PEFCCertCode"))
				//{
				//	pefcCertCode = attributeElement.getAttribute("Value");
				//	itemMap.put("PEFCCertCode", pefcCertCode);
				//}
				if(name.equals("BasisWeight"))
				{
					basisWeight = attributeElement.getAttribute("Value");
					itemMap.put("BasisWeight", basisWeight);
				}
				if(name.equals("RollWidth"))
				{
					rollWidth = attributeElement.getAttribute("Value");
					itemMap.put("RollWidth", rollWidth);
				}
				if(name.equals("RollDiameter"))
				{
					rollDiameter = attributeElement.getAttribute("Value");
					itemMap.put("RollDiameter", rollDiameter);
				}
				if(name.equals("Brand"))
				{
					brand = attributeElement.getAttribute("Value");
					log.debug("brand"+brand);
					itemMap.put("Brand", brand);
				}
			}
	
		}
		log.debug("itemmap size"+itemMap.size());
		log.debug("brand"+itemMap.get("Brand"));
		Collection<String> c = itemMap.values();
		Iterator<String> i = c.iterator();
		while(i.hasNext())
		{
			log.debug("brand"+i.next());
		}
		//invoiceLineElement.setAttribute("FSCCertCode", fscCertCode);
		//invoiceLineElement.setAttribute("SFICertCode", sfiCertCode);
		//invoiceLineElement.setAttribute("PEFCCertCode", pefcCertCode);
		//invoiceLineElement.setAttribute("BasisWeight", basisWeight);
		//invoiceLineElement.setAttribute("RollWidth", rollWidth);
		//invoiceLineElement.setAttribute("RollDiameter", rollDiameter);
		//invoiceLineElement.setAttribute("Brand", brand);
		//invoiceLineElement.setAttribute("FSCCertCode", itemMap.get("FSCCertCode"));
		//invoiceLineElement.setAttribute("SFICertCode", itemMap.get("SFICertCode"));
		//invoiceLineElement.setAttribute("PEFCCertCode", itemMap.get("PEFCCertCode"));*/
		NodeList itemNodeList = itemListDoc.getElementsByTagName("Item");
		int length = itemNodeList.getLength();
		if(length != 0)
		{
			Element itemElement = (Element)itemNodeList.item(0);
			invoiceLineElement.setAttribute("BasisWeight", SCXmlUtil.getXpathAttribute(itemElement, "./Extn/@ExtnBasis"));
			invoiceLineElement.setAttribute("RollWidth", SCXmlUtil.getXpathAttribute(itemElement, "./Extn/@ExtnRollWidth"));
			invoiceLineElement.setAttribute("RollDiameter", SCXmlUtil.getXpathAttribute(itemElement, "./Extn/@ExtnRollDiameter"));
			invoiceLineElement.setAttribute("Brand", SCXmlUtil.getXpathAttribute(itemElement, "./Extn/@ExtnBrand"));						
			invoiceLineElement.setAttribute("PklvOneUPC", SCXmlUtil.getXpathAttribute(itemElement, "./Extn/@ExtnPKLV1UPC"));
			invoiceLineElement.setAttribute("PklvTwoUPC", SCXmlUtil.getXpathAttribute(itemElement, "./Extn/@ExtnPKLV2UPC"));
			invoiceLineElement.setAttribute("PklvThreeUPC", SCXmlUtil.getXpathAttribute(itemElement, "./Extn/@ExtnPKLV3UPC"));
			invoiceLineElement.setAttribute("PklvFourUPC", SCXmlUtil.getXpathAttribute(itemElement, "./Extn/@ExtnPKLV4UPC"));
			invoiceLineElement.setAttribute("PklvFiveUPC", SCXmlUtil.getXpathAttribute(itemElement, "./Extn/@ExtnPKLV5UPC"));
			
			// UNSPSC replacement.			
			xpedxUNSPSC = SCXmlUtil.getXpathAttribute(itemElement, "./Extn/@ExtnUNSPSC");
			if(!YFCObject.isNull(xpedxUNSPSC) && !YFCObject.isVoid(xpedxUNSPSC)) {
				log.debug("xpedxUNSPSC : " + xpedxUNSPSC);
			}
			if(!YFCObject.isNull(xpedxUNSPSC) && !YFCObject.isVoid(xpedxUNSPSC)){
				customerUNSPSC = translateUnspscSterlingToCustomerInvoice(env,customerID,itemID,orgCode,xpedxUNSPSC);
				if(!YFCObject.isNull(customerUNSPSC) && !YFCObject.isVoid(customerUNSPSC)) {
					log.debug("customerUNSPSC : " + customerUNSPSC);
				}
				if( !YFCObject.isNull(customerUNSPSC) && !YFCObject.isVoid(customerUNSPSC)){
					replacedUNSPSC = customerUNSPSC;
				} else {
					replacedUNSPSC = xpedxUNSPSC;
				}
			} 
			if(!YFCObject.isNull(replacedUNSPSC) && !YFCObject.isVoid(replacedUNSPSC)) {
				log.debug("replacedUNSPSC : " + replacedUNSPSC);
			}
			invoiceLineElement.setAttribute("Unspsc", replacedUNSPSC);
		}
		if(invoiceLineElement != null){
			log.debug("invoiceLineElement: "+SCXmlUtil.getString(invoiceLineElement));
		}
	}
	
	/*private void stampDivisionInfo(YFSEnvironment env, Element inputElement, String division) throws YFSException, RemoteException
	{
		String remitToDunsNo = "";
		String remitToName = "";
		String remitAddr1 = "";
		String remitAddr2 = "";
		String remitCity = "";
		String remitState = "";
		String remitZip = "";
		//form the input document
		Document inputOrgDoc = SCXmlUtil.createDocument("Organization");
		Element inputOrgElement = inputOrgDoc.getDocumentElement();
		inputOrgElement.setAttribute("OrganizationCode", division);
		env.setApiTemplate("getOrganizationList", getOrganizationListTemplate);
		Document orgListDoc = api.invoke(env, "getOrganizationList", inputOrgDoc);
		NodeList orgList = orgListDoc.getElementsByTagName("Organization");
		int orgLength = orgList.getLength();
		if(orgLength != 0)
		{
			Element orgElement = (Element)orgList.item(0);
			remitToDunsNo = SCXmlUtil.getXpathAttribute(orgElement, "ExtnRemitToDunsNo");
			remitToName = SCXmlUtil.getXpathAttribute(orgElement, "ExtnRemitToName");
			remitAddr1 = SCXmlUtil.getXpathAttribute(orgElement, "ExtnRemitAddr1");
			remitAddr2 = SCXmlUtil.getXpathAttribute(orgElement, "ExtnRemitAddr2");
			remitCity = SCXmlUtil.getXpathAttribute(orgElement, "ExtnRemitCity");
			remitState = SCXmlUtil.getXpathAttribute(orgElement, "ExtnRemitState");
			remitZip = SCXmlUtil.getXpathAttribute(orgElement, "ExtnRemitZip");
			inputElement.setAttribute("RemitToDunsNo", remitToDunsNo);
			inputElement.setAttribute("RemitToName", remitToName);
			inputElement.setAttribute("RemitAddr1", remitAddr1);
			inputElement.setAttribute("RemitAddr2", remitAddr2);
			inputElement.setAttribute("RemitCity", remitCity);
			inputElement.setAttribute("RemitState", remitState);
			inputElement.setAttribute("RemitZip", remitZip);
			
			
		}
		
		
		
	}*/
	
	public void stampCustomerInfo(YFSEnvironment env, Element inputElement, String customer) throws YFSException, RemoteException, YIFClientCreationException
	{		
		String mSAPCustomerKey = "";
		String billToDunsNo = "";
		String sellerDunsNo = "";
		String shipToStoreNo = "";
		//String naCustomerNo = "";
		String eTradingID = "";
		String customerBuyerID = "";
		//String customerEnvID = "";
		String b2bInvoiceFlag = "";
		//form input customer doc
		Document inputCustomerDoc = SCXmlUtil.createDocument("Customer");
		Element inputCustomerElement = inputCustomerDoc.getDocumentElement();
		inputCustomerElement.setAttribute("CustomerID", customer);
		env.setApiTemplate("getCustomerList", getCustomerListTemplate);		
		api = YIFClientFactory.getInstance().getLocalApi();
		Document customerListDoc = api.invoke(env, "getCustomerList", inputCustomerDoc);
		NodeList customerList = customerListDoc.getElementsByTagName("Customer");
		int customerLength = customerList.getLength();
		if(customerLength != 0)
		{
			Element customerElement = (Element)customerList.item(0);
             //	Commented out to retrieve the flag from the SAP customer profile as per inheritance design
			//b2bInvoiceFlag = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@Extn810InvoiceFlag");
			
			/**** Start -  Added code to get the Buyer ID from MSAP level. ***/
			if(customerElement.hasAttribute("RootCustomerKey")){
				mSAPCustomerKey = customerElement.getAttribute("RootCustomerKey");
				Document inputMSAPCustomerDoc = SCXmlUtil.createDocument("Customer");
				inputMSAPCustomerDoc.getDocumentElement().setAttribute("CustomerKey", mSAPCustomerKey);		
				api = YIFClientFactory.getInstance().getLocalApi();
				Document mSAPCustListOutputDoc = api.invoke(env, "getCustomerList", inputMSAPCustomerDoc);				
				NodeList mSAPCustomerList = mSAPCustListOutputDoc.getElementsByTagName("Customer");
				int mSAPCustomerLength = mSAPCustomerList.getLength();
				if(mSAPCustomerLength != 0){
					Element mSAPCustomerElement = (Element)mSAPCustomerList.item(0);
					customerBuyerID = SCXmlUtil.getXpathAttribute(mSAPCustomerElement, "./Extn/@ExtnBuyerID");

					if(!YFCObject.isNull(customerBuyerID) && !YFCObject.isVoid(customerBuyerID)) {
						log.debug("");
						log.debug("MSAP_customerBuyerID:" + customerBuyerID);
					}
					if(log.isDebugEnabled()){
						log.debug("MSAP_customerBuyerID:" + customerBuyerID);
					}
				}
			}		
			/**** End -  Added code to get the Buyer ID from MSAP level. ***/
				
			/*********Added code to retrieve flag from SAP customer profile*********/
			Element parentCustomerElement = (Element) customerElement.getElementsByTagName("ParentCustomer").item(0);
			String billToCustomerId  = parentCustomerElement.getAttribute("CustomerID");
			
			inputCustomerElement.setAttribute("CustomerID", billToCustomerId);
			Document getBillToCustomerListOutputDoc = api.invoke(env, "getCustomerList", inputCustomerDoc);
			if(log.isDebugEnabled()){
				log.debug("The second getCustomerList is: "+SCXmlUtil.getString(getBillToCustomerListOutputDoc));
			}
			Element billToCustomerElement = (Element) getBillToCustomerListOutputDoc.getDocumentElement().getElementsByTagName("Customer").item(0);
			Element billToParentCustomerElement = (Element) billToCustomerElement.getElementsByTagName("ParentCustomer").item(0);
			String sapCustomerId  = billToParentCustomerElement.getAttribute("CustomerID");
			
			inputCustomerElement.setAttribute("CustomerID", sapCustomerId);
			Document getSAPCustomerListOutputDoc = api.invoke(env, "getCustomerList", inputCustomerDoc);
			if(log.isDebugEnabled()){
				log.debug("The third getCustomerList is: "+SCXmlUtil.getString(getSAPCustomerListOutputDoc));
			}
			Element sapCustomerElement = (Element) getSAPCustomerListOutputDoc.getDocumentElement().getElementsByTagName("Customer").item(0);
			Element sapExtnCustomerElement = (Element) sapCustomerElement.getElementsByTagName("Extn").item(0);
			
			b2bInvoiceFlag  = sapExtnCustomerElement.getAttribute("Extn810InvoiceFlag");
			/*****************************************************************************/
			
			billToDunsNo = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnBillToDunsNo");
			sellerDunsNo = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnSellerDunsNo");
			shipToStoreNo = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnShipToStoreNo");
			//naCustomerNo = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnNACustomerNo");
			eTradingID = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnETradingID");		
			// customerBuyerID = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnBuyerID");
			//customerEnvID = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnEnvironmentCode");
			inputElement.setAttribute("BillToDunsNo", billToDunsNo);
			inputElement.setAttribute("SellerDunsNo", sellerDunsNo);
			inputElement.setAttribute("ShipToStoreNo", shipToStoreNo);
			//inputElement.setAttribute("NACustomerNo", naCustomerNo);
			inputElement.setAttribute("ETradingID", eTradingID);
			inputElement.setAttribute("CustomerBuyerID", customerBuyerID);
			inputElement.setAttribute("B2BInvoiceFlag", b2bInvoiceFlag);
			//inputElement.setAttribute("CustomerEnvironmentId", customerEnvID);
			
			/* The attributes set below are used for UNSPSC replacement.*/
			inputElement.setAttribute("CustomerID", customer);
			inputElement.setAttribute("OrganizationCode", customerElement.getAttribute("OrganizationCode") );			
			
			if(!b2bInvoiceFlag.equals("Y"))
			{				
				// Error logged in CENT tool.
				YFSException exceptionMessage = new YFSException();
				exceptionMessage.setErrorDescription("Customer Profile field for 810 B2B Invoices is set to 'N' ");		
				log.error("Customer Profile field for 810 B2B Invoices is set to 'N' ");				
				throw exceptionMessage;
				
				// To stop sending B2B Invoice incase invoice flag is not 'Y'.				
				// inputElement.setAttribute("ErrorFlag", "Y");
			}
		}
		
	}
	
	/**
	 * 	To set the status of Fulfillment Order.
	 * 
	 *  @param env Environment variable.
	 *  @param fOrderEle Element contains the Fulfillment Order information.
	 *  @param toStatus Drop status.	                         
	 *	@return tempDoc Output document of the Change Order Status API. 	 
	 *                                              
	 */
	
	private YFCDocument changeFOStatus(YFSEnvironment env, String ordHeaderKey, String toStatus) throws Exception {
		
		YFCDocument chngFOStatusOutXML = null;
				
		if(YFCObject.isNull(ordHeaderKey) || YFCObject.isNull(ordHeaderKey)) {
			throw new Exception("OrderHeaderKey Cannot be NULL or Void!");
		}
		// To set the transaction Id.
		String tranId = null;
		if(this._prop != null) {
			tranId = this._prop.getProperty("XPX_CHNG_ORD_STATUS_TXN");
			if(YFCObject.isNull(tranId) || YFCObject.isVoid(tranId)) {
				throw new Exception("TranstionID Not Configured in API Arguments!");
			}
		} else {
			throw new Exception("Arguments Not Configured For API-XPXPerformLegacyOrderUpdateAPI!");
		}
		// To build the input document to call changeOrderStatus API.
		YFCDocument chngOrdStatusInXML = YFCDocument.getDocumentFor("<OrderStatusChange/>");
		YFCElement ordChngEle = chngOrdStatusInXML.getDocumentElement();
		ordChngEle.setAttribute("OrderHeaderKey", ordHeaderKey);
		ordChngEle.setAttribute("IgnoreTransactionDependencies", "Y");
		ordChngEle.setAttribute("ChangeForAllAvailableQty", "Y");
		ordChngEle.setAttribute("TransactionId", tranId);
		ordChngEle.setAttribute("BaseDropStatus", toStatus);
		if(log.isDebugEnabled()){
			log.debug("");
			log.debug("OrderStatus:"+toStatus);
			log.debug("ChangeFOStatus-InXML:"+chngOrdStatusInXML.getString());
		}
		if(log.isVerboseEnabled()){
			log.verbose("");
			log.verbose("OrderStatus:"+toStatus);
			log.verbose("ChangeFOStatus-InXML:"+chngOrdStatusInXML.getString());
		}
		// To call changeOrderStatus API.
		Document tempDoc = this.api.executeFlow(env, "XPXChangeOrderStatus", chngOrdStatusInXML.getDocument());
		if(tempDoc != null) {
			chngFOStatusOutXML = YFCDocument.getDocumentFor(tempDoc);
		} else {
			throw new Exception("Service XPXChangeOrderStatus Failed!");
		}
		return chngFOStatusOutXML;
	}
	
	/* This method is used to change xpedx UNSPSC to customer UNSPSC for Invoice Transaction 810 */
	
	public String translateUnspscSterlingToCustomerInvoice(YFSEnvironment env, String customerID,String itemID,String orgCode,String xpedxUnspsc) throws YFSException, RemoteException
	{
		String masterCustomer = "";		
		String customerUnspsc = "";
		String legacyItemID = "";
		String buyerID = "";
		
		// masterCustomer = XPXTranslationUtilsAPI.getMsap(env, customerID, orgCode);
		
		// To get the Buyer ID from SAP level customer.
		buyerID = XPXTranslationUtilsAPI.getBuyerID(env, customerID, orgCode);		
		if(!YFCObject.isNull(buyerID) && !YFCObject.isVoid(buyerID)){
			// To form the input document to call the API.
			Document inputUnspscDoc = SCXmlUtil.createDocument("XPXB2bLegacyUnspscXref");
			Element inputUnspscElement = inputUnspscDoc.getDocumentElement();
			inputUnspscElement.setAttribute("MasterCustomerID", buyerID);
			inputUnspscElement.setAttribute("XpedxUNSPSC", xpedxUnspsc);
			Document outputUnspsc = api.executeFlow(env, "getXPXB2BLegacyUnspscXrefList", inputUnspscDoc);		
			if(outputUnspsc != null && outputUnspsc.hasChildNodes()){				
			NodeList unspscNodeList = outputUnspsc.getElementsByTagName("XPXB2bLegacyUnspscXref");
			int unspscLength = unspscNodeList.getLength();
				for(int i=0;i<unspscLength;i++){				
					Element unspscElement = (Element)unspscNodeList.item(i);
					legacyItemID = unspscElement.getAttribute("LegacyItemID");					
					if(!YFCObject.isNull(legacyItemID) && legacyItemID.equalsIgnoreCase(itemID)){					
						customerUnspsc = unspscElement.getAttribute("CustomerUNSPSC");
						break;
					} // If there is no Item ID match then the last customerUNSPSC of the MasterCustomerID should be fetched.  
					else if(i == (unspscLength-1)){
						customerUnspsc = unspscElement.getAttribute("CustomerUNSPSC");	
					}
				}
			} 
		}	
		return customerUnspsc;
	}
>>>>>>> v2.0.6.2
}