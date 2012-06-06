package com.xpedx.nextgen.priceAndAvailabilityService;



import java.util.HashMap;
import java.util.Properties;
import org.w3c.dom.Document;
import zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.WsIpaperAvailabilityStub;
/*import zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.WsIpaperAvailabilityStub.AItems;
import zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.WsIpaperAvailabilityStub.ArrayOfaItems;
import zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.WsIpaperAvailabilityStub.AvailabilityInput;
import zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.WsIpaperAvailabilityStub.AvailabilityOutput;*/
import zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.WsIpaperAvailabilityStub.FGetAvailability;
import zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.WsIpaperAvailabilityStub.FGetAvailabilityE;
import zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.WsIpaperAvailabilityStub.FGetAvailabilityResponse;
import zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.WsIpaperAvailabilityStub.FGetAvailabilityResponseE;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.dashboard.XPXUpdateOrderWithNewItemsAPI;
import com.yantra.interop.client.ClientVersionSupport;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXPandAWebServiceInvocationAPI implements YIFCustomApi {
	private static Properties props;
	private static YFCLogCategory log;
	
	static
	{
		log =  (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
     
	}
	public Document invokeWebService(YFSEnvironment env, Document inputXML)
			throws Exception {
		
		/** try-catch block added by Arun Sekhar on 24-Jan-2011 for CENT tool logging **/
		Document outputXML = null;
		try{


			/**
			 * The input xml will be in the following format:
			 * 
			 * <PriceAndAvailability> <SourceIndicator/> <EnvironmentId/> <Company/>
			 * <CustomerBranch/> <CustomerNumber/> <ShipToSuffix/> <OrderBranch/>
			 * <Items> <Item> <LineNumber/> <LegacyProductCode/> <RequestedQtyUOM/>
			 * <RequestedQty/> </Item> </Items> </PriceAndAvailability>
			 * 
			 */

            String endPointURL = null;
            //Setting the time out for the webservice
            // if isPriceLocking is true then this is on order placement else normal
            String isPriceLock=null;
            String timeout = null;
			Integer timeoutInSecs;
			if(inputXML != null){
				log.debug("The input XML to the Price and Availability Webservice is " + SCXmlUtil.getString(inputXML));
			}
			endPointURL = YFSSystem.getProperty("PandAWSDL");
			
			if (env instanceof ClientVersionSupport) {
				ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
				HashMap map = clientVersionSupport.getClientProperties();
				if (map != null) {
					isPriceLock =(String)map.get("isPriceLocking");
				}
			}
			
			if( isPriceLock !=null && ("true".equals(isPriceLock.trim())))
			{// If the PnA is called on order placement this will be true
				timeout = YFSSystem.getProperty("PandATimeoutForOrderPlacement");
			}
			else 
			{// if the PnA is called on other pages this will be true
				timeout = YFSSystem.getProperty("PandATimeoutForOthers");
			}
		
			if(endPointURL ==  null || endPointURL.trim().length()<=0)
			{//No customer overrides entry so property value is retrieved from the SDF
			   endPointURL = props.getProperty("ENDPOINT_URL");
			}
			
			if(timeout == null || timeout.trim().length() <= 0) 
			{// setting the default to 30 sec
				timeout = "30";
			}
			try {
				timeoutInSecs= Integer.parseInt(timeout);
			}
			catch (NumberFormatException e) {
				log.error("Exception: " + e.getStackTrace());
				timeoutInSecs = 30;
			}
			Integer timeoutInMilliSecs = timeoutInSecs*1000;

			
			//Initializing the instance of the stub class generated
			WsIpaperAvailabilityStub testStub = new WsIpaperAvailabilityStub(endPointURL);
			//setting the timeout for the web service 
			testStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(timeoutInMilliSecs);
			FGetAvailabilityE input = new FGetAvailabilityE();
			FGetAvailability fAvailability = new FGetAvailability();
			
			//Converting the input XML Document to a String format
			String inputXMLString = SCXmlUtil.getString(inputXML);
			
			//Creating the SOAP request input
			fAvailability.setWsIpaperAvailabilityInput(inputXMLString);
			input.setFGetAvailability(fAvailability);
			if(log.isDebugEnabled()){
				log.debug("Availability of webservice input : " + input.getFGetAvailability().getWsIpaperAvailabilityInput());        
			}        
	        //Invoking the WSDL and receiving the response simultaneously
			log.beginTimer("Calling-the-webmethod-PnA-webservice");
	        FGetAvailabilityResponseE response = testStub.fGetAvailability(input) ; 
	        log.endTimer("Calling-the-webmethod-PnA-webservice");
	        if(log.isDebugEnabled()){
				log.debug("The response of availability of the webservice is: "+response.getFGetAvailabilityResponse().getWsIpaperAvailabilityOutput());
	        }
			
			//Converting the string reponse to XML Document format to return it to the calling app		
			outputXML = YFCDocument.getDocumentFor(response.getFGetAvailabilityResponse().getWsIpaperAvailabilityOutput()).getDocument();

		}catch (NullPointerException ne) {
			log.error("NullPointerException: " + ne.getStackTrace());	
			prepareErrorObject(ne, XPXLiterals.PA_TRANS_TYPE, XPXLiterals.NE_ERROR_CLASS, env, inputXML);	
			throw ne;
		} catch (YFSException yfe) {
			log.error("YFSException: " + yfe.getStackTrace());
			prepareErrorObject(yfe, XPXLiterals.PA_TRANS_TYPE, XPXLiterals.YFE_ERROR_CLASS, env, inputXML);			
		} catch (Exception e) {
			log.error("Exception: " + e.getStackTrace());
			prepareErrorObject(e, XPXLiterals.PA_TRANS_TYPE, XPXLiterals.E_ERROR_CLASS, env, inputXML);
		}
		return outputXML;
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

	public void setProperties(Properties props) throws Exception {
		this.props = props;
	}
	
	/*
	WsIpaperAvailabilityInput wsIpaperAvailabilityInput = new WsIpaperAvailabilityInput();
	AvailabilityInput availabilityInput = new AvailabilityInput();
	availabilityInput.setSSourceIndicator((String) inputAttributes
			.get(XPXLiterals.E_SOURCE_INDICATOR));

	availabilityInput.setSEnvironmentId((String) inputAttributes
			.get(XPXLiterals.E_ENVT_ID));

	availabilityInput.setSCompany((String) inputAttributes
			.get(XPXLiterals.E_COMPANY));

	availabilityInput.setSCustomerBranch((String) inputAttributes
			.get(XPXLiterals.E_CUSTOMER_BRANCH));

	availabilityInput.setSCustomerNumber((String) inputAttributes
			.get(XPXLiterals.E_CUSTOMER_NUMBER));

	availabilityInput.setSShipToSuffix((String) inputAttributes
			.get(XPXLiterals.E_SHIP_TO_SUFFIX));

	availabilityInput.setSOrderBranch((String) inputAttributes
			.get(XPXLiterals.E_ORDER_BRANCH));

	AItems[] itemList = (AItems[]) inputAttributes
			.get(XPXLiterals.ITEMS_ARRAY);
	ArrayOfaItems arrItem = new ArrayOfaItems();
	arrItem.setArrayOfaItemsItem(itemList);
	availabilityInput.setAItems(arrItem);

	wsIpaperAvailabilityInput.setAvailabilityInput(availabilityInput);
	fAvailability.setWsIpaperAvailabilityInput(wsIpaperAvailabilityInput);

	log.debug("The source indicator is: "
			+ (String) inputAttributes.get(XPXLiterals.E_SOURCE_INDICATOR));

	FGetAvailabilityResponseE fGetAvailabilityResponseE = testStub
			.fGetAvailability(input);
	FGetAvailabilityResponse fGetAvailabilityResponse = fGetAvailabilityResponseE
			.getFGetAvailabilityResponse();
	WsIpaperAvailabilityOutput wsIpaperAvailabilityOutput = fGetAvailabilityResponse
			.getWsIpaperAvailabilityOutput();
	AvailabilityOutput availabilityOutput = wsIpaperAvailabilityOutput
			.getAvailabilityOutput();*/
	//return SCXmlBeanUtils.getXml(availabilityOutput);

}
