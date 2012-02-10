package com.xpedx.nextgen.customermanagement.api;

import java.rmi.RemoteException;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

/*
 * Input
 * 
 * <Customer CustomerID="BUYER1" OrganizationCode="xpedx"/>
 * 
 * Output
 * 
 * 	<BillTo BillToID="BUYER1" OrganizationCode="xpedx">
 		<ShipToList>
  			<ShipTo OrganizationCode="xpedx" ShipToID="BUYER2" /> 
  			<ShipTo OrganizationCode="xpedx" ShipToID="BUYER3" /> 
  		</ShipToList>
  </BillTo>
 */

/**
 * This class gets the immediate Ship To for a given Bill To
 *
 */
public class XPXGetShipToListAPI implements YIFCustomApi
{
	
	private static YIFApi api = null;
	private static YFCLogCategory log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");

	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}
	
	/**
	 * @param env
	 * @param inXML
	 * @return
	 * @throws Exception
	 * This is the method invoked for getting the immediate Ship To for a BIll To
	 */
	public Document invokeGetChildOrganization(YFSEnvironment env,Document inXML) throws Exception
	
	{
		Document formattedBillToDoc = null;
		
		api = YIFClientFactory.getInstance().getApi();
		Element inputElement = inXML.getDocumentElement();
		
		Document BillToDoucument = YFCDocument.createDocument("BillTo").getDocument();
		Element orgListElement = BillToDoucument.getDocumentElement();
		orgListElement.setAttribute("BillToID", inputElement.getAttribute("CustomerID"));
		orgListElement.setAttribute("OrganizationCode", inputElement.getAttribute("OrganizationCode"));
		Element shipToListElement = BillToDoucument.createElement("ShipToList");
		orgListElement.appendChild(shipToListElement);
		//get the customer key 
		
		String customerKey = getCustomerKey(env, inXML);
		
		if(customerKey != null || customerKey.trim().length() > 0)
		{
		//get the list of child orgs under this customer
			BillToDoucument = getChildOrgList(env, BillToDoucument, shipToListElement, customerKey);
			if(log.isDebugEnabled()){
				log.debug("The Un-formatted BillTo Document is: "+ SCXmlUtil.getString(BillToDoucument));
			}
			formattedBillToDoc = formatBillToDocument(BillToDoucument);
			if(log.isDebugEnabled()){
				log.debug("The Formatted BillTo Document is: "+ SCXmlUtil.getString(formattedBillToDoc));
			}
		}
		return formattedBillToDoc;
	}
	
	/**
	 * This method changes the format of the output document from 
	 * <BillTo BillToID="BUYER1" OrganizationCode="xpedx">
     *       <ShipToList>
     *             <ShipTo OrganizationCode="xpedx" ShipToID="BUYER2" /> 
     *             <ShipTo OrganizationCode="xpedx" ShipToID="BUYER3" /> 
     *       </ShipToList>
     * </BillTo>
     * 
     * To
     * 
     * <CustomerList>
     *<Customer Type=”BillTo” BillToID="BUYER1" OrganizationCode="xpedx">
     *       <CustomerList >
     *             <Customer Type=”ShipTo” OrganizationCode="xpedx" ShipToID="BUYER2" /> 
     *             <Customer Type=”ShipTo” OrganizationCode="xpedx" ShipToID="BUYER3" /> 
     *       </CustomerList>
     * </BillTo>
     * </CustomerList>
     * 
     *
	 * 
	 * @param billToDoucument
	 * @return
	 */
	
	private Document formatBillToDocument(Document billToDoucument) {

            Document formattedBillToDocument = null;
            
            formattedBillToDocument = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER_LIST).getDocument();
            
		    Element customerElement = formattedBillToDocument.createElement(XPXLiterals.E_CUSTOMER);
		    customerElement.setAttribute("Type", "BillTo");
		    customerElement.setAttribute(XPXLiterals.A_CUSTOMER_ID,billToDoucument.getDocumentElement().getAttribute(XPXLiterals.A_BILL_TO_ID));
		    customerElement.setAttribute(XPXLiterals.A_ORGANIZATION_CODE,billToDoucument.getDocumentElement().getAttribute(XPXLiterals.A_ORGANIZATION_CODE));
		    
		    Element customerListElement = formattedBillToDocument.createElement(XPXLiterals.E_CUSTOMER_LIST);
		    
		    
		    Element billToDoucumentShipToListElement = (Element)billToDoucument.getElementsByTagName("ShipToList").item(0);
		    String totalNoOfRecords = billToDoucumentShipToListElement.getAttribute("TotalNumberOfRecords");
		    NodeList billToDoucumentShipToNodeList = billToDoucumentShipToListElement.getElementsByTagName("ShipTo");
		    
		    if(billToDoucumentShipToNodeList.getLength() > 0)
		    {
		    	for(int i=0; i<billToDoucumentShipToNodeList.getLength(); i++)
		    	{
		    	     Element  billToDoucumentShipToElement = (Element)billToDoucumentShipToNodeList.item(i);
		    	     
		    	     Element iterativeCustomerElement = formattedBillToDocument.createElement(XPXLiterals.E_CUSTOMER);
		    	     iterativeCustomerElement.setAttribute("Type", "ShipTo");
		    	     iterativeCustomerElement.setAttribute(XPXLiterals.A_ORGANIZATION_CODE,billToDoucumentShipToElement.getAttribute(XPXLiterals.A_ORGANIZATION_CODE));
		    	     iterativeCustomerElement.setAttribute(XPXLiterals.A_CUSTOMER_ID,billToDoucumentShipToElement.getAttribute(XPXLiterals.A_SHIP_TO_ID));
		    	     customerListElement.appendChild(iterativeCustomerElement);
		    	}
		    }
		    customerListElement.setAttribute("TotalNumberOfRecords", totalNoOfRecords);
		    customerElement.appendChild(customerListElement);
		    formattedBillToDocument.getDocumentElement().appendChild(customerElement);
		    
		    return formattedBillToDocument;
		
	}

	/**
	 * @param env
	 * @param inXML
	 * @return
	 * @throws RemoteException
	 * This method is used to fetch the key to get all the Ship To under this BillTo
	 */
	private String getCustomerKey(YFSEnvironment env, Document inXML) throws RemoteException
	{
		Element inputElement = inXML.getDocumentElement();
		//form the input xml
		//pass customerid and organization code
		String customerKey = "";
		YFCDocument inputCustomerDocument = YFCDocument.createDocument("Customer");
		YFCElement inputCustomerElement = inputCustomerDocument.getDocumentElement();
		inputCustomerElement.setAttribute("CustomerID", inputElement.getAttribute("CustomerID"));
		inputCustomerElement.setAttribute("OrganizationCode", inputElement.getAttribute("OrganizationCode"));
		//create a output template
		YFCDocument outputCustomerListTemplateDoc = YFCDocument.createDocument("CustomerList");
		YFCElement outputCustomerListTemplateElement = outputCustomerListTemplateDoc.getDocumentElement();
		outputCustomerListTemplateElement.setAttribute("TotalNumberOfRecords", "");
		YFCElement outputCustomerTemplateElement = outputCustomerListTemplateDoc.createElement("Customer");
		outputCustomerTemplateElement.setAttribute("CustomerKey", "");
		outputCustomerListTemplateElement.appendChild(outputCustomerTemplateElement);
		env.setApiTemplate("getCustomerList", outputCustomerListTemplateDoc.getDocument());
		Document outputCustomerDocument = api.invoke(env, "getCustomerList", inputCustomerDocument.getDocument());
		env.clearApiTemplate("getCustomerList");
		if(!outputCustomerDocument.getDocumentElement().getAttribute("TotalNumberOfRecords").equals("0"))
		{
			Element outputCustomerElement = (Element)outputCustomerDocument.getElementsByTagName("Customer").item(0);
			customerKey = outputCustomerElement.getAttribute("CustomerKey");
		}
		return customerKey;
	}
	
	/**
	 * @param env
	 * @param orgListDoucment
	 * @param orgListElement
	 * @param customerKey
	 * @throws RemoteException
	 * This method is used to get the ShipTo list and form a output document as mentioned in the above format.
	 */
	private Document getChildOrgList(YFSEnvironment env,
			Document BillToDoucument, Element shipToListElement,
			String customerKey) throws RemoteException {
		//Form the input to fetch the child org list
		YFCDocument inputCustomerDocument = YFCDocument.createDocument("Customer");
		YFCElement inputCustomerElement = inputCustomerDocument.getDocumentElement();
		inputCustomerElement.setAttribute("ParentCustomerKey", customerKey);
		//create a output template
		YFCDocument outputCustomerListTemplateDoc = YFCDocument.createDocument("CustomerList");
		YFCElement outputCustomerListTemplateElement = outputCustomerListTemplateDoc.getDocumentElement();
		outputCustomerListTemplateElement.setAttribute("TotalNumberOfRecords", "");
		YFCElement outputCustomerTemplateElement  = outputCustomerListTemplateDoc.createElement("Customer");
		outputCustomerTemplateElement.setAttribute("OrganizationCode", "");
		outputCustomerTemplateElement.setAttribute("CustomerID", "");
		outputCustomerTemplateElement.setAttribute("CustomerKey", "");
		outputCustomerListTemplateElement.appendChild(outputCustomerTemplateElement);
		env.setApiTemplate("getCustomerList", outputCustomerListTemplateDoc.getDocument());
		Document outputCustomerDocument = api.invoke(env, "getCustomerList", inputCustomerDocument.getDocument());
		env.clearApiTemplate("getCustomerList");
		Element outputCustomerElement = outputCustomerDocument.getDocumentElement();
		if(!outputCustomerElement.getAttribute("TotalNumberOfRecords").equals("0"))
		{
			NodeList customerList = outputCustomerDocument.getElementsByTagName("Customer");
			int customerListLength = customerList.getLength();
			shipToListElement.setAttribute("TotalNumberOfRecords", outputCustomerElement.getAttribute("TotalNumberOfRecords"));
			for(int orgCounter = 0;orgCounter<customerListLength;orgCounter++)
			{
				Element customerElement = (Element)customerList.item(orgCounter);
				Element shipToElement = BillToDoucument.createElement("ShipTo");
				shipToElement.setAttribute("OrganizationCode", customerElement.getAttribute("OrganizationCode"));
				shipToElement.setAttribute("ShipToID", customerElement.getAttribute("CustomerID"));
				shipToListElement.appendChild(shipToElement);
							
			}
		}
		
		return BillToDoucument;
	} 

}
