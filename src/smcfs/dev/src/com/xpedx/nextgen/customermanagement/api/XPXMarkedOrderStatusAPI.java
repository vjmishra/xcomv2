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
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;


public class XPXMarkedOrderStatusAPI implements YIFCustomApi {

	private static String BASE_DROP_STATUS = "3700.0300";
	private static String MODIFICATION_REASON_CODE= "Mark As Complete With Exceptions";
	private static String MARK_COMPLETE ="XPX_MARK_COMPLETE.0001.ex";
	private static YFCLogCategory log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	private YIFApi api = null;
	String sOrderNo="";
	String sEntrCode="";
	String sDocType="";
	String sShpNode="";
	String sBuyrOrgCode ="";
	String sSellrOrgCode="";


	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}




	public void invokeGetOrderDetails(YFSEnvironment env, Document inXML)throws Exception{

		api = YIFClientFactory.getInstance().getApi();
		String sOrderHeaderKey="";
		if(inXML != null)
		{
			if(log.isDebugEnabled()){
			log.debug("The input XML for getOrderDetails is : " + SCXmlUtil.getString(inXML));
			}
		}
		// create and set output template for the customer list API
		String sTemplate ="<Order> <OrderLines><OrderLine></OrderLine></OrderLines><PersonInfoShipTo></PersonInfoShipTo></Order>";
		env.setApiTemplate("getOrderDetails", SCXmlUtil.createFromString(sTemplate));
		sTemplate = null;
		
		// Prepare Input XML
		sOrderHeaderKey = inXML.getDocumentElement().getAttribute("OrderHeaderKey");
		YFCDocument inputOrderDocument =YFCDocument.createDocument("Order");
		YFCElement  inputOrderElement =inputOrderDocument.getDocumentElement();
		inputOrderElement.setAttribute("OrderHeaderKey", sOrderHeaderKey);

		// Invoke getOrderDetails API
		Document OrderDetails = api.invoke(env,"getOrderDetails",inputOrderDocument.getDocument());
		if(log.isDebugEnabled()){
			log.debug("The output XML file of getOrderDetails is : "+YFCDocument.getDocumentFor(OrderDetails).toString());
		}
		getAllOrderDetails(OrderDetails);

		confirmShipment(env,OrderDetails);
		env.clearApiTemplate("getOrderDetails");
	}



	public void getAllOrderDetails(Document p_dOrderDetails){

		NodeList nlOrderLines = null;
		Element eleOrder = p_dOrderDetails.getDocumentElement();
			sOrderNo =	SCXmlUtil.getAttribute(eleOrder,"OrderNo");
			sEntrCode = SCXmlUtil.getAttribute(eleOrder,"EnterpriseCode");
			sDocType = SCXmlUtil.getAttribute(eleOrder,"DocumentType");
			sBuyrOrgCode =SCXmlUtil.getAttribute(eleOrder,"BuyerOrganizationCode");
			sSellrOrgCode =SCXmlUtil.getAttribute(eleOrder, "SellerOrganizationCode");
			sShpNode = eleOrder.getAttribute("ShipNode");
			if(log.isDebugEnabled()){
				log.debug("Order Details : strOrderNo"+sOrderNo+"strEntrCode"+sEntrCode+"strDocType"+sDocType);
			}

	}


	public void confirmShipment(YFSEnvironment env,Document p_dOrderDetails) throws Exception{

		YFCElement eShipmentLine;
		Element eleItem;
		String sItemId ;
		String sUnitOfMeasure;
		String sPrimelineNo;
		String sSublineNo;
		String sOrderedQty;
		YFCDocument confrmShipmntDocument =YFCDocument.createDocument("Shipment");
		YFCElement  confrmShipmntElement  =confrmShipmntDocument.getDocumentElement();



		confrmShipmntElement.setAttribute("Action","Create");
		confrmShipmntElement.setAttribute("DocumentType","0001");
		confrmShipmntElement.setAttribute("SellerOrganizationCode",sSellrOrgCode);
		confrmShipmntElement.setAttribute("BuyerOrganizationCode",sBuyrOrgCode);
		confrmShipmntElement.setAttribute("EnterpriseCode",sEntrCode);
		confrmShipmntElement.setAttribute("ShipNode",sShpNode);

		/*
		 * YFCElement eToAddress=confrmShipmntDocument.createElement("ToAddress");
		eToAddress.setAttribute("Country","US");\
		*/
		YFCElement eShipmentLines = confrmShipmntDocument.createElement("ShipmentLines");

		NodeList nlOrderShipmentLine= p_dOrderDetails.getElementsByTagName("OrderLine");

		if(nlOrderShipmentLine!=null && nlOrderShipmentLine.getLength()>0){
			Element eleOrderShipmentLine;
			for(int i=0 ;i<nlOrderShipmentLine.getLength();i++){

				eleOrderShipmentLine=(Element) nlOrderShipmentLine.item(i);
				sPrimelineNo= SCXmlUtil.getAttribute(eleOrderShipmentLine,"PrimeLineNo");
				sSublineNo=  SCXmlUtil.getAttribute(eleOrderShipmentLine,"SubLineNo");
				sOrderedQty =SCXmlUtil.getAttribute(eleOrderShipmentLine,"OrderedQty");

				eShipmentLine=confrmShipmntDocument.createElement("ShipmentLine");
				eShipmentLine.setAttribute("OrderNo",sOrderNo);
				eShipmentLine.setAttribute("PrimeLineNo", sPrimelineNo);
				eShipmentLine.setAttribute("SubLineNo", sSublineNo);

				NodeList nlItem =eleOrderShipmentLine.getElementsByTagName("Item");
				eleItem = (Element)nlItem.item(0);


					sItemId = SCXmlUtil.getAttribute(eleItem,"ItemID");
					eShipmentLine.setAttribute("ItemID", sItemId);
					sUnitOfMeasure = SCXmlUtil.getAttribute(eleItem,"UnitOfMeasure");
					eShipmentLine.setAttribute("UnitOfMeasure",sUnitOfMeasure);


				eShipmentLine.setAttribute("Quantity",sOrderedQty);
				eShipmentLines.appendChild(eShipmentLine);
			}

		}

		//confrmShipmntDocument.getDocumentElement().appendChild(eToAddress);
		confrmShipmntDocument.getDocumentElement().appendChild(eShipmentLines);
		if(log.isDebugEnabled()){
			log.debug("Shipment input xml file is :"+confrmShipmntDocument.toString());
		}
		api.invoke(env,"confirmShipment",confrmShipmntDocument.getDocument());
		changeOrderStatus( env);

	}



	public void changeOrderStatus(YFSEnvironment env) throws Exception{
		YFCDocument docChangeOrderStatus =YFCDocument.createDocument("OrderStatusChange");
		YFCElement  eleChangeOrderStatus  =docChangeOrderStatus.getDocumentElement();

		eleChangeOrderStatus.setAttribute("AuditTransactionId","");
		eleChangeOrderStatus.setAttribute("BaseDropStatus",BASE_DROP_STATUS);
		eleChangeOrderStatus.setAttribute("ChangeForAllAvailableQty","Y");
		eleChangeOrderStatus.setAttribute("DocumentType",sDocType);
		eleChangeOrderStatus.setAttribute("EnterpriseCode",sEntrCode);
		eleChangeOrderStatus.setAttribute("IgnoreTransactionDependencies","Y");
		eleChangeOrderStatus.setAttribute("ModificationReasonCode",MODIFICATION_REASON_CODE);
		eleChangeOrderStatus.setAttribute("ModificationReasonText","");
		eleChangeOrderStatus.setAttribute("OrderNo",sOrderNo);
		eleChangeOrderStatus.setAttribute("TransactionId",MARK_COMPLETE);

		if(log.isDebugEnabled()){
			log.debug("Input XML  file for ChangeOrderStatus :"+docChangeOrderStatus.toString());
		}
		api.invoke(env,"changeOrderStatus",docChangeOrderStatus.getDocument());

	}
}



