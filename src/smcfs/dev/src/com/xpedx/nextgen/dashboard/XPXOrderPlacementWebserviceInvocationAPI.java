package com.xpedx.nextgen.dashboard;

import java.util.Properties;

import javax.xml.ws.BindingProvider;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder.FPlaceOrder;
import zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder.WsIpaperPlaceOrderPortType;
import zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder.WsIpaperPlaceOrder;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sun.xml.internal.ws.client.BindingProviderProperties;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXOrderPlacementWebserviceInvocationAPI implements YIFCustomApi {
		private static Properties props;
		//private static final Logger log = Logger.getLogger(XPXOrderPlacementWebserviceInvocationAPI.class);
		private static YFCLogCategory log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		
		public void setProperties(Properties arg0) throws Exception {
			// TODO Auto-generated method stub
			this.props = arg0;
		}

		public Document invokeWebService(YFSEnvironment env, Document inputXML)
				throws Exception {
			Document orderPlaceResponseDoc = null;
			String endPointURL = null;
			
			endPointURL = YFSSystem.getProperty("OrderPlaceWSDL");
			if(endPointURL ==  null || endPointURL.trim().length()<=0)
			{//No customer overrides entry so property value is retrieved from the SDF
			   endPointURL = props.getProperty("ENDPOINT_URL");
			}
			//WsIpaperPlaceOrderStub testStub = new WsIpaperPlaceOrderStub(endPointURL);
			//Setting the time out for the Order placement to legacy also based on the email from pawan dated July 15th 2011
			Integer timeoutInMilliSecs = getTimeoutInMilliSecondsForWebService();
			//testStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(timeoutInMilliSecs);
			//End of Setting the time out
			//FPlaceOrderE inputOrderXml = new FPlaceOrderE();
			//FPlaceOrder inputPlaceOrder = new FPlaceOrder();
			if(inputXML != null){
				log.debug("Input xml for OrderPlace to Legacy: "+SCXmlUtil.getString(inputXML));
			}
			
			String inputXMLString = SCXmlUtil.getString(inputXML);
			//inputXMLString="<Order>     <TransactionStatus/>     <TransactionMessage/>     <SourceIndicator>1</SourceIndicator>     <EnvironmentId>M</EnvironmentId>     <CustomerEnvironmentId>D3</CustomerEnvironmentId>     <Company>XX</Company>     <WebConfirmationNumber>230107E9063321</WebConfirmationNumber>     <OrderingDivision>6R</OrderingDivision>     <LegacyOrderNumber/>     <GenerationNumber/>     <LegacyOrderType>S</LegacyOrderType>     <WebHoldFlag>N</WebHoldFlag>     <ShipFromDivision>6R</ShipFromDivision>     <CustomerDivision>60</CustomerDivision>     <CustomerNumber>0006096461</CustomerNumber>     <ShipToSuffix>000003</ShipToSuffix>     <ShipToName>CINCINNATI ZOO OP R ULRICH</ShipToName>     <AttentionName/>     <ShipToAddress1>3400 VINE ST</ShipToAddress1>     <ShipToAddress2/>     <ShipToAddress3/>     <ShipToCity>CINCINNATI</ShipToCity>     <ShipToState>OH</ShipToState>     <ShipToZIP>452201399</ShipToZIP>     <ShipToCountryCode>US</ShipToCountryCode>     <BillToSuffix>000</BillToSuffix>     <BillToName>CINCINNATI ZOO</BillToName>     <BillToAddress1>3400 VINE ST</BillToAddress1>     <BillToAddress2>ATTN COMMISSARY</BillToAddress2>     <BillToAddress3/>     <BillToCity>CINCINNATI</BillToCity>     <BillToState>OH</BillToState>     <BillToZIP>452201399</BillToZIP>     <BillToCountryCode>US</BillToCountryCode>     <CustomerHeaderPONumber>kirtesh456</CustomerHeaderPONumber>     <OrderCode>O</OrderCode>     <ShipComplete>Y</ShipComplete>     <WillCall/>     <ShipDate/>     <HeaderComments/>     <OrderedByName>Dave Young</OrderedByName>     <OrderCreateDate>2013-01-07T06:03:00-05:00</OrderCreateDate>     <OrderSource>3</OrderSource>     <HeaderProcessCode>A</HeaderProcessCode>     <OrderStatus>100</OrderStatus>     <OrderStatusComment/>     <CurrencyCode>USD</CurrencyCode>     <TotalShippableValue>0.00</TotalShippableValue>     <TotalOrderValue>1264.00</TotalOrderValue>     <OrderSpecialCharges>0.00</OrderSpecialCharges>     <TotalOrderFreight>0.00</TotalOrderFreight>     <OrderTax>0.00</OrderTax>     <SystemIdentifier/>     <WebHoldReason/>     <HeaderStatusCode/>     <AdjustmentAmount/>     <AdjustmentAmountProcessCode/>     <AdjustmentAmountStatusCode/>     <OrderHeaderKey>20130107060301199063355</OrderHeaderKey>     <OrderLockFlag>Y</OrderLockFlag>     <NoBoSplit/>     <LineItems>         <LineItem>             <WebLineNumber>E99063341</WebLineNumber>             <LegacyLineNumber/>             <LineType>P</LineType>             <LineProcessCode>A</LineProcessCode>             <LegacyProductCode>4602441</LegacyProductCode>             <CustomerProductCode/>             <BaseUnitOfMeasure>M_HDL</BaseUnitOfMeasure>             <OrderedQtyInBase>200.00</OrderedQtyInBase>             <PriceUnitOfMeasure>M_CTN</PriceUnitOfMeasure>             <UnitPrice>75.84</UnitPrice>             <LineDescription>Wet Mop Handle</LineDescription>             <PriceOverrideFlag/>             <RequestedUnitOfMeasure>M_HDL</RequestedUnitOfMeasure>             <RequestedOrderQuantity>200.00</RequestedOrderQuantity>             <ShippedQty>0.00</ShippedQty>             <BackOrderQty>0.00</BackOrderQty>             <CustomerLineNumber/>             <CustomerLinePONumber>anv</CustomerLinePONumber>             <CustomerUserField1/>             <CustomerUserField2/>             <CustomerUserField3/>             <ShipFromBranch>6R</ShipFromBranch>             <LineNotes/>             <ShippableLineTotal>0.00</ShippableLineTotal>             <OrderedLineTotal>1264.00</OrderedLineTotal>             <ShippableQtyInBase>0.00</ShippableQtyInBase>             <BackOrderQtyInBase>0.00</BackOrderQtyInBase>             <CustLineAccNumber>aaa</CustLineAccNumber>             <AdjustDollarAmount>0.00</AdjustDollarAmount>             <CouponCode/>             <LineStatusCode/>         </LineItem>     </LineItems> </Order>";
			//inputPlaceOrder.setWsIpaperPlaceOrderInput(inputXMLString);
			//inputOrderXml.setFPlaceOrder(inputPlaceOrder);
			WsIpaperPlaceOrder orderPlaceService=new WsIpaperPlaceOrder();
			WsIpaperPlaceOrderPortType orderPlacePort= orderPlaceService.getComIpaperXpedxWmWebOrderplacementWsIpaperPlaceOrderPort();
			log.beginTimer("Calling-the-webmethod-OrderPlace-webservice");
			//FPlaceOrderResponseE orderResponse = testStub.fPlaceOrder(inputOrderXml);
			BindingProvider bp =(BindingProvider)orderPlacePort;
			bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPointURL);
			bp.getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, timeoutInMilliSecs);
			bp.getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT,timeoutInMilliSecs);
			String outputString=orderPlacePort.fPlaceOrder(inputXMLString);
			System.out.println("Out put xml in Edit Order = "+outputString);
			log.endTimer("Calling-the-webmethod-OrderPlace-webservice");
			//Document outputXml = YFCDocument.getDocumentFor(orderResponse.getFPlaceOrderResponse().getWsIpaperPlaceOrderOutput()).getDocument();
			//YFCDocument outDoc = YFCDocument.getDocumentFor(orderResponse.getFPlaceOrderResponse().getWsIpaperPlaceOrderOutput());
			
			orderPlaceResponseDoc = YFCDocument.getDocumentFor(outputString).getDocument();
			
			if(null!=orderPlaceResponseDoc){
				String outputXMLString = SCXmlUtil.getString(orderPlaceResponseDoc);
				log.debug("Output xml for OrderPlace from Legacy: "+outputXMLString);
			}else{
				log.error("Output from Legcy for Order placement is empty");
			}
			/*****************************************************************************/
			/** Added by Arun Sekhar on 02-March-2011
			 * This attribute will be validated by the XPXPerformLegacyOrderUpdateAPI
			 * to distinguish between OrderPlace and OrderUpdate calls **/
			orderPlaceResponseDoc.getDocumentElement().setAttribute("IsOrderPlace", "Y");
			/*****************************************************************************/
			return orderPlaceResponseDoc;
			
			}
		
		private Integer getTimeoutInMilliSecondsForWebService(){
			//setting the time out for the order placement
			String timeout = null;
			Integer timeoutInSecs;
			
			timeout = YFSSystem.getProperty("OrderPlacementWebServiceTimeOut");
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
