package com.xpedx.nextgen.customermanagement.api;

import java.rmi.RemoteException;
import java.util.Properties;

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
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

/*
 * Input xml
 * <CustomerXRefList>
       <CustomerXRef/>
   <\CustomerXRefList>
   
   output
   <XPXItemcustXref CapsId=" " ConvFactor=" "  
    CustItemKey=" " CustomerBranch=" "
    CustomerDecription=" " CustomerNumber=" " CustomerPartNumber=" "
    CustomerUnit=" " ItemcustRefKey=" " LegacyBase=" "
    LegacyItemNumber=" "  MPC=" " 
     >
 */

/**
 * This class is used to manage the customer item cross reference.
 *
 */
public class XPXCustomerXReferenceFeed implements YIFCustomApi  {
	
	private static YIFApi api = null;
	private static YFCLogCategory log;
	Element inputElement = null;
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try 
		{
			api = YIFClientFactory.getInstance().getApi();
		}
		catch (YIFClientCreationException e1) {

			e1.printStackTrace();
		}
	}
	
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}
	
	/**
	 * This method is used to manage the item customer cross reference
	 * creation,updation and deletion
	 * @param env
	 * @param inXML
	 * @throws Exception
	 */
	public void invokeCustomerXReferenceFeedProcess(YFSEnvironment env,Document inXML) throws Exception
	{
		/** Modified by Arun Sekhar on 20-Jan-2011 for CENT tool logging **/
		try{
			
			//log.debug("In invokeCustomerXReferenceFeedProcess");
			String xRefKey = "";
			//api = YIFClientFactory.getInstance().getApi();
			inputElement = inXML.getDocumentElement();
			NodeList customerxRefNodeList = inXML.getElementsByTagName("CustomerXRef");
			int customerLength = customerxRefNodeList.getLength();
			if(customerLength>0)
			{
				Element customerElement = (Element)customerxRefNodeList.item(0);
				String processCode = customerElement.getAttribute("ProcessCode");
				
				/** Test block by Arun Sekhar */
				// processCode = "A";
				/** Test block ends here */

				if(processCode.equalsIgnoreCase("A"))
				{
					/**Start fix for CR 2176 by Prasanth Kumar M.****/
					//log.debug("In invokeCustomerXReferenceFeedProcess create");
					//Document inputCustomerItemDoc = createCustomerXreference(env, customerElement);
					//create a item customer cross reference
				    //api.executeFlow(env, "createItemCustXrefService", inputCustomerItemDoc);
					
					modifyItemCustomerXReference(env, xRefKey, customerElement,inXML);	
					/**End fix for CR 2176 by Prasanth Kumar M.****/
				}
				if(processCode.equalsIgnoreCase("D"))
				{ //log.debug("In invokeCustomerXReferenceFeedProcess delete");
					deleteItemCustomerReference(env, customerElement);
					
				}
				
				if(processCode.equalsIgnoreCase("C")){
					modifyItemCustomerXReference(env, xRefKey, customerElement,inXML);				
				}								
			}
		}catch (NullPointerException ne) {
			log.error("------------Failed XML Needs to Catch for Re-Processing XML START ----------");
			log.error(SCXmlUtil.getString(inputElement));
			log.error("------------Failed XML Needs to Catch for Re-Processing XML END ----------");
			log.error("NullPointerException: " + ne.getStackTrace());
			prepareErrorObject(ne, XPXLiterals.CUST_XREF_B_TRANS_TYPE,
					XPXLiterals.NE_ERROR_CLASS, env, inXML);
			throw ne;
		} catch (YFSException yfe) {
			log.error("------------Failed XML Needs to Catch for Re-Processing XML START ----------");
			log.error(SCXmlUtil.getString(inputElement));
			log.error("------------Failed XML Needs to Catch for Re-Processing XML END ----------");
			log.error("YFSException: " + yfe.getStackTrace());
			prepareErrorObject(yfe, XPXLiterals.CUST_XREF_B_TRANS_TYPE,
					XPXLiterals.NE_ERROR_CLASS, env, inXML);
			throw yfe;
		} catch (Exception e) {
			log.error("------------Failed XML Needs to Catch for Re-Processing XML START ----------");
			log.error(SCXmlUtil.getString(inputElement));
			log.error("------------Failed XML Needs to Catch for Re-Processing XML END ----------");
			log.error("Exception: " + e.getStackTrace());
			prepareErrorObject(e, XPXLiterals.CUST_XREF_B_TRANS_TYPE,
					XPXLiterals.NE_ERROR_CLASS, env, inXML);
			throw e;
		}
		log.debug("<--- invokeCustomerXReferenceFeedProcess()");
	}

	/**
	 * @author asekhar-tw on 20-Jan-2011
	 * 
	 * This method prepares the error object with the exception details which in
	 * turn will be used to log into CENT
	 * 
	 * @param e
	 * @param transType
	 * @param errorClass
	 * @param env
	 * @param inXML
	 * @return void
	 * @throws
	 */
	private void prepareErrorObject(Exception e, String transType,
			String errorClass, YFSEnvironment env, Document inXML) {
		com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
		errorObject.setTransType(transType);
		errorObject.setErrorClass(errorClass);
		errorObject.setInputDoc(inXML);
		errorObject.setException(e);

		log.debug("e.getMessage() :" + e.getMessage());
		log.debug("prepareErrorObject() :: StackTrace :");
		e.printStackTrace();

		ErrorLogger.log(errorObject, env);
	}
	private void modifyItemCustomerXReference(YFSEnvironment env,
			String xRefKey, Element customerElement, Document inXML) throws RemoteException {
		
		Element xRefElement = null;
		//log.debug("In invokeCustomerXReferenceFeedProcess Modify");
		//get the key of the existing record
		//form the input doc
		Document inputModifyDoc = YFCDocument.createDocument("XPXItemcustXref").getDocument();
		Element inputModifyElement = inputModifyDoc.getDocumentElement();
		inputModifyElement.setAttribute("EnvironmentCode", customerElement.getAttribute("EnvironmentId"));
		inputModifyElement.setAttribute("CompanyCode", customerElement.getAttribute("CompanyCode"));
		
		/*********Start fix for JIRA # 2579 by Prasanth Kumar M.******************/
		//inputModifyElement.setAttribute("MPC", customerElement.getAttribute("MasterProductCode"));
		inputModifyElement.setAttribute("CustomerDivision", customerElement.getAttribute("CustomerDivision"));
		/**********End fix for JIRA #2579*****************************************/
		
		inputModifyElement.setAttribute("CustomerNumber", customerElement.getAttribute("LegacyCustomerNumber"));
		/**Start fix for CR 2176 by Prasanth Kumar M.****/
		inputModifyElement.setAttribute("CustomerItemNumber", customerElement.getAttribute("CustomerItemNumber"));
		/**End fix for CR 2176 by Prasanth Kumar M.****/
		//get the list of itemXref
		log.debug("inputModifyDoc"+SCXmlUtil.getString(inputModifyDoc));
		Document outputListDoc = api.executeFlow(env, "getItemCustXrefListService", inputModifyDoc);
		log.debug("outputListDoc"+SCXmlUtil.getString(outputListDoc));
		NodeList xRefNodeList = outputListDoc.getElementsByTagName("XPXItemcustXref");
		int xRefLength = xRefNodeList.getLength();
		if(xRefLength == 0)
		{
			
				
			log.debug("record does not exist");
			
			/*********Start fix for JIRA # 2579 by Prasanth Kumar M.******************/
			if(customerElement.getAttribute("ProcessCode").equalsIgnoreCase("A"))
			{
			   Document inputCustomerItemDoc = createCustomerXreference(env,customerElement);
			
			
			   log.debug("The input to create the record is: "+SCXmlUtil.getString(inputCustomerItemDoc));
			   api.executeFlow(env, "createItemCustXrefService", inputCustomerItemDoc);
			}
			else if (customerElement.getAttribute("ProcessCode").equalsIgnoreCase("C"))
			{
				//If record does not exist create an exeption and update CENT.
				YFSException exceptionMessage = new YFSException();
				exceptionMessage.setErrorDescription("Record does not exist in Sterling for an update");	
				prepareErrorObject(exceptionMessage, XPXLiterals.CUST_XREF_B_TRANS_TYPE,
						XPXLiterals.NE_ERROR_CLASS, env, inXML);
			}
			/**********End fix for JIRA #2579*****************************************/
			
					
		}
		else
		{
			xRefElement = (Element)xRefNodeList.item(0);
			//log.debug("xRefElement"+SCXmlUtil.getString(xRefElement));
			xRefKey = xRefElement.getAttribute("ItemcustRefKey");
			log.debug("xRefKey"+xRefKey);
					
			Document newDoc = YFCDocument.createDocument("XPXItemcustXref").getDocument();
			/*newDoc.appendChild(newDoc.importNode(customerElement, true));
			newDoc.renameNode(newDoc.getDocumentElement(), newDoc.getNamespaceURI(), "XPXItemcustXref");*/
			
			newDoc.getDocumentElement().setAttribute("ItemcustRefKey", xRefKey);
			newDoc.getDocumentElement().setAttribute("CompanyCode", customerElement.getAttribute("CompanyCode"));
			newDoc.getDocumentElement().setAttribute("ConvFactor", customerElement.getAttribute("CustomerUoMConversionFactor"));
			newDoc.getDocumentElement().setAttribute("CustomerDecription", customerElement.getAttribute("CustomerSpecificDescription"));
			newDoc.getDocumentElement().setAttribute("CustomerDivision", customerElement.getAttribute("CustomerDivision"));
			newDoc.getDocumentElement().setAttribute("CustomerItemNumber", customerElement.getAttribute("CustomerItemNumber"));
			newDoc.getDocumentElement().setAttribute("CustomerNumber", customerElement.getAttribute("LegacyCustomerNumber"));
			newDoc.getDocumentElement().setAttribute("CustomerUom", customerElement.getAttribute("CustomerUoM"));
			newDoc.getDocumentElement().setAttribute("EnvironmentCode", customerElement.getAttribute("EnvironmentId"));
			newDoc.getDocumentElement().setAttribute("IsCustUOMExcl", customerElement.getAttribute("CustomerExclusiveItem"));
			newDoc.getDocumentElement().setAttribute("LegacyItemNumber", customerElement.getAttribute("LegacyItemNumber"));
			newDoc.getDocumentElement().setAttribute("LegacyUom", customerElement.getAttribute("LegacyUoM"));
			newDoc.getDocumentElement().setAttribute("MPC", customerElement.getAttribute("MasterProductCode"));
					
			//modify 
			log.debug("newDoc"+SCXmlUtil.getString(newDoc));
			log.debug("The input xml to changeIteMCustXref is: "+SCXmlUtil.getString(newDoc));
			api.executeFlow(env, "changeItemCustXrefService", newDoc);
		}
		
		
	}

	/**
	 * This method deletes the item customer cross reference
	 * @param env
	 * @param inputElement
	 * @throws RemoteException
	 */
	private void deleteItemCustomerReference(YFSEnvironment env,
			Element inputElement) throws RemoteException {
		//Create an input doc
		
		YFCDocument inputDelDocument = YFCDocument.createDocument("XPXItemcustXref");
		YFCElement inputDelElement = inputDelDocument.getDocumentElement();
		inputDelElement.setAttribute("CustomerDivision", inputElement.getAttribute("CustomerDivision"));
		inputDelElement.setAttribute("CompanyCode", inputElement.getAttribute("CompanyCode"));
		inputDelElement.setAttribute("CustomerNumber", inputElement.getAttribute("LegacyCustomerNumber"));
		//inputDelElement.setAttribute("MPC", inputElement.getAttribute("MasterProductCode"));
		/**Start fix for CR 2176 by Prasanth Kumar M.****/
		//inputDelElement.setAttribute("LegacyUom", inputElement.getAttribute("LegacyUoM"));
		//inputDelElement.setAttribute("CustomerUom", inputElement.getAttribute("CustomerUoM"));
		inputDelElement.setAttribute("CustomerItemNumber", inputElement.getAttribute("CustomerItemNumber"));
		inputDelElement.setAttribute("EnvironmentCode", inputElement.getAttribute("EnvironmentId"));
		/**End fix for CR 2176 by Prasanth Kumar M.****/
		//get the record using list api
		Document crossRefListDoc = api.executeFlow(env, "getItemCustXrefListService", inputDelDocument.getDocument());
		Element crossRefListElement = crossRefListDoc.getDocumentElement();
		//String crossLength = crossRefListElement.getAttribute("TotalNumberOfRecords");
		NodeList crossNodeList = crossRefListElement.getElementsByTagName("XPXItemcustXref");
		int crossLength = crossNodeList.getLength();
		if(crossLength == 0)
		{
			log.error("the record does not exist");
		}
		else
		{
			Element crossRefElement = (Element)crossNodeList.item(0);
			String crossRefKey = crossRefElement.getAttribute("ItemcustRefKey");
			
			Document inputDeleteDoc= YFCDocument.createDocument("XPXItemcustXref").getDocument();
			inputDeleteDoc.getDocumentElement().setAttribute("ItemcustRefKey", crossRefKey);
			//inputDeleteDoc.appendChild(inputDeleteDoc.importNode(crossNodeList.item(0), true));
			//invoke the delete api
			api.executeFlow(env, "deleteItemCustXrefService", inputDeleteDoc);
		}
	}

	/**
	 * This method creates the item customer cross reference
	 * @param env
	 * @param custElement
	 * @return
	 * @throws RemoteException
	 */
	private Document createCustomerXreference(YFSEnvironment env, Element custElement)
			throws RemoteException {
		Document outputCustomerItemDoc;
		//form the input document
		YFCDocument inputCustomerRefDoc = YFCDocument.createDocument("XPXItemcustXref");
		YFCElement inputCustomerRefElement = inputCustomerRefDoc.getDocumentElement();
		
		inputCustomerRefElement.setAttribute("EnvironmentCode", custElement.getAttribute("EnvironmentId"));
		inputCustomerRefElement.setAttribute("CompanyCode", custElement.getAttribute("CompanyCode"));
		inputCustomerRefElement.setAttribute("CustomerDivision", custElement.getAttribute("CustomerDivision"));
		inputCustomerRefElement.setAttribute("CustomerNumber", custElement.getAttribute("LegacyCustomerNumber"));
		inputCustomerRefElement.setAttribute("MPC", custElement.getAttribute("MasterProductCode"));
		inputCustomerRefElement.setAttribute("LegacyItemNumber", custElement.getAttribute("LegacyItemNumber"));
		inputCustomerRefElement.setAttribute("CustomerItemNumber", custElement.getAttribute("CustomerItemNumber"));
		inputCustomerRefElement.setAttribute("CustomerDecription", custElement.getAttribute("CustomerSpecificDescription"));
		inputCustomerRefElement.setAttribute("LegacyUom", custElement.getAttribute("LegacyUoM"));
		inputCustomerRefElement.setAttribute("CustomerUom", custElement.getAttribute("CustomerUoM"));
		inputCustomerRefElement.setAttribute("ConvFactor", custElement.getAttribute("CustomerUoMConversionFactor"));
		inputCustomerRefElement.setAttribute("IsCustUOMExcl", custElement.getAttribute("CustomerExclusiveItem"));
		
				
		
		
		/*************Removed as fix for bug # 11660*************************************************/
        //inputCustomerRefElement.setAttribute("CapsId", custElement.getAttribute("CapsId"));
        //inputCustomerRefElement.setAttribute("CustomerPartNumber", custElement.getAttribute("LegacyItemNumber"));
		//inputCustomerRefElement.setAttribute("CustomerUnit", custElement.getAttribute("CustomerUnit"));
		//inputCustomerRefElement.setAttribute("ItemcustRefKey", custElement.getAttribute("ItemcustRefKey"));
		
		return inputCustomerRefDoc.getDocument();
	}
}
