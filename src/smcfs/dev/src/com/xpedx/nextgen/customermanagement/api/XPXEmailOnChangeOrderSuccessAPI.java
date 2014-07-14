package com.xpedx.nextgen.customermanagement.api;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfs.japi.YFSEnvironment;


public class XPXEmailOnChangeOrderSuccessAPI implements YIFCustomApi{

	private static YIFApi api = null;
	
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public Document createEmailOrderList(YFSEnvironment p_env,Document p_dInXML) throws Exception{
			api = YIFClientFactory.getInstance().getApi();	
		
			Element inputElement =  p_dInXML.getDocumentElement();
			String sWebLineNumber=SCXmlUtil.getXpathAttribute(inputElement, "./Extn/@ExtnWebLineNumber");

			Document orderTemplateDoc = YFCDocument.createDocument("Order").getDocument();
			Element orderElement = orderTemplateDoc.getDocumentElement();
		
			String sCustomerID = SCXmlUtil.getXpathAttribute(inputElement,"./@BillToID");
			String sShipNode = SCXmlUtil.getXpathAttribute(inputElement,"./@ShipNode");
			Document templateDoc = setCustomerListTemplate();
			p_env.setApiTemplate("getCustomerList",templateDoc);

			Document custDoc = YFCDocument.createDocument("Customer").getDocument();
			Element custElement =custDoc.getDocumentElement();
			custElement.setAttribute("CustomerID",sCustomerID);
			custElement.setAttribute("OrganizationCode","xpedx");
			Document outListDoc = api.invoke(p_env,"getCustomerList",custDoc);
			//Document outListDoc = api.invoke(p_env,"getCustomerList",custDoc);
						
			p_env.clearApiTemplate("getCustomerList");
			Element emailElement = outListDoc.getDocumentElement();

			String sEmailId = SCXmlUtil.getXpathAttribute(emailElement,"./Customer/BuyerOrganization/ContactPersonInfo/@EMailID");

			orderElement.setAttribute("Emailid",sEmailId);
			orderElement.setAttribute("OrderNo", SCXmlUtil.getXpathAttribute(inputElement,"./@OrderNo"));
			orderElement.setAttribute("OrderType", SCXmlUtil.getXpathAttribute(inputElement,"./@OrderType"));
			orderElement.setAttribute("HeaderStatusCode", SCXmlUtil.getXpathAttribute(inputElement,"./@HeaderStatusCode"));	
			Element orderExtnElement = orderTemplateDoc.createElement("Extn");
			orderExtnElement.setAttribute("ExtnWebConfNumber",SCXmlUtil.getXpathAttribute(inputElement,"./Extn/@ExtnWebConfNum"));
			orderElement.appendChild(orderExtnElement);

			Element orderStatusesElement = orderTemplateDoc.createElement("OrderStatuses");
			Element orderStatus = orderTemplateDoc.createElement("OrderStatus");
			orderStatusesElement.appendChild(orderStatus);

			NodeList linesList = p_dInXML.getElementsByTagName("OrderLines");
			Element orderLinesElement = (Element)linesList.item(0);
			NodeList lineList = orderLinesElement.getElementsByTagName("OrderLine");
			Element oLinesElement = orderTemplateDoc.createElement("OrderLines");
			orderElement.appendChild(oLinesElement);
			Element oLineElement = null;
			Float eQty=0.00f;
			for(int linecounter=0;linecounter<lineList.getLength();linecounter++)
			{  

				oLineElement =	orderTemplateDoc.createElement("OrderLine");
				Element orderLineElement = (Element)lineList.item(linecounter);

				oLineElement.setAttribute("WebLineNumber",SCXmlUtil.getXpathAttribute(orderLineElement,"./Extn/@ExtnWebLineNumber"));
				oLineElement.setAttribute("LineStatus",SCXmlUtil.getXpathAttribute(orderLineElement,"./Extn/@ExtnLineStatusCode"));
				oLineElement.setAttribute("ShipNode",SCXmlUtil.getXpathAttribute(orderLineElement,"./@ShipNode"));
				
				oLineElement.setAttribute("ItemId",SCXmlUtil.getXpathAttribute(orderLineElement,"./Item/@ItemID"));
				oLineElement.setAttribute("ProductClass",SCXmlUtil.getXpathAttribute(orderLineElement,"./Item/@ProductClass"));
				oLineElement.setAttribute("PricingUOM",SCXmlUtil.getXpathAttribute(orderLineElement,"./LinePriceInfo/@PricingUOM"));
				oLineElement.setAttribute("LineTotal",SCXmlUtil.getXpathAttribute(orderLineElement,"./LinePriceInfo/@LineTotal"));
				
				
				oLineElement.setAttribute("OrderedQty", SCXmlUtil.getXpathAttribute(orderLineElement,"@OrderedQty"));
				oLineElement.setAttribute("ShippedQty", SCXmlUtil.getXpathAttribute(orderLineElement,"./OrderLineTranQuantity/@ShippedQuantity"));
				oLineElement.setAttribute("BackOrderQty",SCXmlUtil.getXpathAttribute(orderLineElement,"./OrderLineTranQuantity/@BackOrderQty"));
				oLinesElement.appendChild(oLineElement);
			}

			return orderTemplateDoc;
		}	

	private Document setCustomerListTemplate()throws Exception
		{
			Document templateEmailDoc = YFCDocument.createDocument("CustomerList").getDocument() ;
			Element templateElement = templateEmailDoc.getDocumentElement();
			Element CustElement = templateEmailDoc.createElement("Customer");
			templateElement.appendChild(CustElement);
			CustElement.setAttribute("CustomerID", "");
			Element BuyerOrg = templateEmailDoc.createElement("BuyerOrganization");
			CustElement.appendChild(BuyerOrg);
			
			Element CrpPersnInfo = templateEmailDoc.createElement("CorporatePersonInfo");
			BuyerOrg.appendChild(CrpPersnInfo);
			
			Element ContactPersonInfo= templateEmailDoc.createElement("ContactPersonInfo");
			BuyerOrg.appendChild(ContactPersonInfo);
			return templateEmailDoc;
		}

	
	
	
	
}
