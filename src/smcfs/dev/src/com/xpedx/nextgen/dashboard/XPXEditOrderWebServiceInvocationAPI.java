package com.xpedx.nextgen.dashboard;

import java.util.Properties;

import javax.xml.ws.BindingProvider;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder.FPlaceOrder;
import zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder.WsIpaperPlaceOrder;
import zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder.WsIpaperPlaceOrderPortType;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXEditOrderWebServiceInvocationAPI implements YIFCustomApi{

	private static Properties props;
	private static final YFCLogCategory log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	
	public Document invokeWebService(YFSEnvironment env, Document inputXML) throws Exception
	{
			
			log.debug("OrderEditInterface_InputXML:" + SCXmlUtil.getString(inputXML));	
            Document orderEditResponseDoc = null;
            String endPointURL = null;
            
            endPointURL = YFSSystem.getProperty("OrderPlaceWSDL");
            if(endPointURL ==  null || endPointURL.trim().length()<=0)
            {//No customer overrides entry so property value is retrieved from the SDF
                     endPointURL = props.getProperty("ENDPOINT_URL");
            }
            
            //WsIpaperPlaceOrderStub testStub = new WsIpaperPlaceOrderStub(endPointURL);
            //Setting the time out for the Order placement and Edit order based on the email from pawan dated July 15th 2011
			Integer timeoutInMilliSecs = getTimeoutInMilliSecondsForWebService();
			//testStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(timeoutInMilliSecs);
			//End of Setting the time out
            //FPlaceOrderE inputOrderXml = new FPlaceOrderE();
            FPlaceOrder inputPlaceOrder = new FPlaceOrder();
            String inputXMLString = SCXmlUtil.getString(inputXML);
            //inputXMLString="<Order>     <TransactionStatus/>     <TransactionMessage/>     <SourceIndicator>1</SourceIndicator>     <EnvironmentId>M</EnvironmentId>     <CustomerEnvironmentId>D3</CustomerEnvironmentId>     <Company>XX</Company>     <WebConfirmationNumber>230107E9063321</WebConfirmationNumber>     <OrderingDivision>6R</OrderingDivision>      <LegacyOrderNumber>87619</LegacyOrderNumber>    <GenerationNumber/>     <LegacyOrderType>S</LegacyOrderType>     <WebHoldFlag>N</WebHoldFlag>     <ShipFromDivision>6R</ShipFromDivision>     <CustomerDivision>60</CustomerDivision>     <CustomerNumber>0006096461</CustomerNumber>     <ShipToSuffix>000003</ShipToSuffix>     <ShipToName>CINCINNATI ZOO OP R ULRICH</ShipToName>     <AttentionName/>     <ShipToAddress1>3400 VINE ST</ShipToAddress1>     <ShipToAddress2/>     <ShipToAddress3/>     <ShipToCity>CINCINNATI</ShipToCity>     <ShipToState>OH</ShipToState>     <ShipToZIP>452201399</ShipToZIP>     <ShipToCountryCode>US</ShipToCountryCode>     <BillToSuffix>000</BillToSuffix>     <BillToName>CINCINNATI ZOO</BillToName>     <BillToAddress1>3400 VINE ST</BillToAddress1>     <BillToAddress2>ATTN COMMISSARY</BillToAddress2>     <BillToAddress3/>     <BillToCity>CINCINNATI</BillToCity>     <BillToState>OH</BillToState>     <BillToZIP>452201399</BillToZIP>     <BillToCountryCode>US</BillToCountryCode>     <CustomerHeaderPONumber>kirtesh456</CustomerHeaderPONumber>     <OrderCode>O</OrderCode>     <ShipComplete>Y</ShipComplete>     <WillCall/>     <ShipDate/>     <HeaderComments/>     <OrderedByName>Dave Young</OrderedByName>     <OrderCreateDate>2013-01-07T06:03:00-05:00</OrderCreateDate>     <OrderSource>3</OrderSource>     <HeaderProcessCode>C</HeaderProcessCode>     <OrderStatus>100</OrderStatus>     <OrderStatusComment/>     <CurrencyCode>USD</CurrencyCode>     <TotalShippableValue>0.00</TotalShippableValue>     <TotalOrderValue>1264.00</TotalOrderValue>     <OrderSpecialCharges>0.00</OrderSpecialCharges>     <TotalOrderFreight>0.00</TotalOrderFreight>     <OrderTax>0.00</OrderTax>     <SystemIdentifier/>     <WebHoldReason/>     <HeaderStatusCode/>     <AdjustmentAmount/>     <AdjustmentAmountProcessCode/>     <AdjustmentAmountStatusCode/>     <OrderHeaderKey>20130107060301199063355</OrderHeaderKey>     <OrderLockFlag>Y</OrderLockFlag>     <NoBoSplit/>     <LineItems>         <LineItem>             <WebLineNumber>E99063341</WebLineNumber>             <LegacyLineNumber/>             <LineType>P</LineType>             <LineProcessCode>C</LineProcessCode>             <LegacyProductCode>4602441</LegacyProductCode>             <CustomerProductCode/>             <BaseUnitOfMeasure>M_HDL</BaseUnitOfMeasure>             <OrderedQtyInBase>200.00</OrderedQtyInBase>             <PriceUnitOfMeasure>M_CTN</PriceUnitOfMeasure>             <UnitPrice>75.84</UnitPrice>             <LineDescription>Wet Mop Handle</LineDescription>             <PriceOverrideFlag/>             <RequestedUnitOfMeasure>M_HDL</RequestedUnitOfMeasure>             <RequestedOrderQuantity>200.00</RequestedOrderQuantity>             <ShippedQty>0.00</ShippedQty>             <BackOrderQty>0.00</BackOrderQty>             <CustomerLineNumber/>             <CustomerLinePONumber>anv</CustomerLinePONumber>             <CustomerUserField1/>             <CustomerUserField2/>             <CustomerUserField3/>             <ShipFromBranch>6R</ShipFromBranch>             <LineNotes/>             <ShippableLineTotal>0.00</ShippableLineTotal>             <OrderedLineTotal>1264.00</OrderedLineTotal>             <ShippableQtyInBase>0.00</ShippableQtyInBase>             <BackOrderQtyInBase>0.00</BackOrderQtyInBase>             <CustLineAccNumber>aaa</CustLineAccNumber>             <AdjustDollarAmount>0.00</AdjustDollarAmount>             <CouponCode/>             <LineStatusCode/>         </LineItem>     </LineItems> </Order>";
            log.debug("Input xml for OrderPlace to Legacy: "+inputXMLString);
           //inputPlaceOrder.setWsIpaperPlaceOrderInput(inputXMLString);
           // inputOrderXml.setFPlaceOrder(inputPlaceOrder);
            //FPlaceOrderResponseE orderResponse=null;
			try {
				//orderResponse = testStub.fPlaceOrder(inputOrderXml);
				WsIpaperPlaceOrder orderPlaceService=new WsIpaperPlaceOrder();
				WsIpaperPlaceOrderPortType orderPlacePort= orderPlaceService.getComIpaperXpedxWmWebOrderplacementWsIpaperPlaceOrderPort();
				log.beginTimer("Calling-the-webmethod-OrderPlace-webservice");
				//FPlaceOrderResponseE orderResponse = testStub.fPlaceOrder(inputOrderXml);
				BindingProvider bp =(BindingProvider)orderPlacePort;
				bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPointURL);
				String outputString=orderPlacePort.fPlaceOrder(inputXMLString);
				orderEditResponseDoc = YFCDocument.getDocumentFor(outputString).getDocument();
				System.out.println("Out put xml in Edit Order = "+outputString);
			}catch (Exception oeException){
				oeException = new Exception("Transaction has failed when trying to invoke Edit Order webservice."); 				
 				prepareErrorObject(oeException, "Order Edit", XPXLiterals.YFE_ERROR_CLASS, env, inputXML);
 				/*Begin - Changes made by Mitesh Parikh for JIRA 3045*/
 				env.setTxnObject("OrderEditTransactionFailure", "System Error - Please try after sometime.");
 				/*End - Changes made by Mitesh Parikh for JIRA 3045*/
				throw oeException;
				
			}

            //orderEditResponseDoc = YFCDocument.getDocumentFor(orderResponse.getFPlaceOrderResponse().getWsIpaperPlaceOrderOutput()).getDocument();
             
            log.debug("OrderEditInterface_LegacyResponse:" + SCXmlUtil.getString(orderEditResponseDoc));
            // Throw error to revert the transaction if there a transaction failure message from legacy.
            Element orderElement = orderEditResponseDoc.getDocumentElement(); 
            NodeList tranStatusList = (NodeList)orderElement.getElementsByTagName("TransactionStatus");
     		if(tranStatusList.getLength() > 0){
     			Node tranStatusNode = (Node)tranStatusList.item(0);
     			String tranStatus = tranStatusNode.getTextContent();
     			// Transaction status has been checked for Failure.
     			if(tranStatus != null && tranStatus.equalsIgnoreCase("F")){
     				// Error logged in CENT and thrown to revert the Order changes made in sterling database.
     				log.debug("Order Edit - Transaction has failed when trying to modify order in Legacy.");	
     				Exception oeException = new Exception("Transaction has failed when trying to modify order in Legacy.");
     				// setErrorDescription("Transaction has failed when trying to modify order in Legacy.");
     				prepareErrorObject(oeException, "Order Edit", XPXLiterals.YFE_ERROR_CLASS, env, inputXML);
     				/*Begin - Changes made by Mitesh Parikh for JIRA 3045*/
     				env.setTxnObject("OrderEditTransactionFailure", "System Error - Please try after sometime.");
     				/*End - Changes made by Mitesh Parikh for JIRA 3045*/
     				throw oeException;
     			}
     		}
               
            if(null!=orderEditResponseDoc){
            	  // Flag set to identify order edit interface when doing order update.
            	  orderEditResponseDoc.getDocumentElement().setAttribute("IsOrderEdit", "Y");
	              String outputXMLString = SCXmlUtil.getString(orderEditResponseDoc);
	              log.debug("Output xml for OrderPlace from Legacy: "+outputXMLString);
             } else {
	              log.error("Output from Legacy for Order placement is empty");
             }
             
             return orderEditResponseDoc;

}
	
	private void prepareErrorObject(Exception e, String transType, String errorClass, YFSEnvironment env, Document inXML){
		com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();	
		errorObject.setTransType(transType);
		errorObject.setErrorClass(errorClass);
		errorObject.setInputDoc(inXML);
		errorObject.setException(e);
		ErrorLogger.log(errorObject, env);
	}
	
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		this.props = arg0;
	}
	
	private Integer getTimeoutInMilliSecondsForWebService(){
		//setting the time out for the order placement
		String timeout = null;
		Integer timeoutInSecs;
		
		timeout = YFSSystem.getProperty("EditOrderWebServiceTimeOut");
		if(timeout == null || timeout.trim().length() <= 0) 
		{// setting the default to 300 sec
			timeout = "300";
		}
		try {
			timeoutInSecs= Integer.parseInt(timeout);
		}
		catch (NumberFormatException e) {
			log.error("Exception: " + e.getStackTrace());
			timeoutInSecs = 300;
		}
		Integer timeoutInMilliSecs = timeoutInSecs*1000;
		return timeoutInMilliSecs;
	}

	

}
