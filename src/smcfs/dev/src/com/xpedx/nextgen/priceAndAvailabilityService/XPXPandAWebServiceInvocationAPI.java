package com.xpedx.nextgen.priceAndAvailabilityService;



import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.w3c.dom.Document;

import zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.WsIpaperAvailability;
import zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.WsIpaperAvailabilityPortType;
import zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.WsIpaperAvailabilityStub;
/*import zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.WsIpaperAvailabilityStub.AItems;
import zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.WsIpaperAvailabilityStub.ArrayOfaItems;
import zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.WsIpaperAvailabilityStub.AvailabilityInput;
import zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.WsIpaperAvailabilityStub.AvailabilityOutput;*/
import zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.WsIpaperAvailabilityStub.FGetAvailability;
import zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.WsIpaperAvailabilityStub.FGetAvailabilityE;
import zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.WsIpaperAvailabilityStub.FGetAvailabilityResponseE;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sun.xml.internal.ws.client.BindingProviderProperties;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.client.ClientVersionSupport;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXPandAWebServiceInvocationAPI implements YIFCustomApi {
	private static Properties props;
	private static YFCLogCategory log;
	private static YIFApi api = null;
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
			
			//setting the timeout for the web service
			int maxItemNumber=10;
			try
			{
				maxItemNumber=Integer.parseInt(YFSSystem.getProperty("MaxItemNumber"));
			}
			catch(Exception e)
			{
				log.error("Exception while tring to cast MaxItemNumber: "+ e.getStackTrace());
			}
			try {
				int minTimeoutInSecs= Integer.parseInt(YFSSystem.getProperty("PandAMinNumberofItemsForTimeout"));
				int maxTimeoutInSecs=Integer.parseInt(YFSSystem.getProperty("PandAMaxNumberofItemsForTimeout"));
				int itemCount = inputXML.getElementsByTagName("aItem").getLength();
				if(itemCount <= maxItemNumber){
					timeoutInSecs = minTimeoutInSecs;
					
				}else{
					timeoutInSecs = maxTimeoutInSecs;
				}
			}
			catch (NumberFormatException e) {
				log.error("Exception while tring to cast PandANumberofRetries: "+ e.getStackTrace());
				timeoutInSecs = 30;
			}
			
			final Integer timeoutInMilliSecs = timeoutInSecs *1000;
			
			//Initializing the instance of the stub class generated
			//WsIpaperAvailabilityStub testStub = new WsIpaperAvailabilityStub(endPointURL);
			QName qname = new QName("http://zwm1/com/ipaper/xpedx/wm/web/priceavailability/wsIpaperAvailability", "wsIpaperAvailability");
			 URL url =new URL(null,
					 endPointURL+"?WSDL",
	                    new URLStreamHandler() {
	                    @Override
	                    protected URLConnection openConnection(URL url) throws IOException {
	                    URL clone_url = new URL(url.toString());
	                    HttpURLConnection clone_urlconnection = (HttpURLConnection) clone_url.openConnection();
	                    // TimeOut settings
	                    clone_urlconnection.setConnectTimeout(timeoutInMilliSecs);
	                    clone_urlconnection.setReadTimeout(timeoutInMilliSecs);
	                    return(clone_urlconnection);
	                    }
	                });

	            WsIpaperAvailability iPaperAvailablityService = new WsIpaperAvailability(url, qname);
			WsIpaperAvailabilityPortType iPaperAvailablityPortType = iPaperAvailablityService
							.getComIpaperXpedxWmWebPriceavailabilityWsIpaperAvailabilityPort();
		
			BindingProvider bp =(BindingProvider)iPaperAvailablityPortType;
			bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPointURL);
			//bp.getRequestContext().put(com.sun.xml.internal.ws.developer.JAXWSProperties.CONNECT_TIMEOUT, timeoutInMilliSecs);
			/*bp.getRequestContext().put("com.sun.xml.ws.connect.timeout",timeoutInMilliSecs);
			bp.getRequestContext().put("com.sun.xml.ws.request.timeout",timeoutInMilliSecs);*/
			//testStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(timeoutInMilliSecs);
			int maxretry=1;
			try
			{
				maxretry = Integer.parseInt(YFSSystem.getProperty("PandANumberofRetries"));
			}
			catch(Exception e)
			{
				log.error("Exception while tring to cast PandANumberofRetries: " + e.getStackTrace());
			}
			int retry=0;
			for(;retry<maxretry;retry++)
			{
				try
				{
				
					//Initializing the instance of the stub class generated
					
					//FGetAvailabilityE input = new FGetAvailabilityE();
					//FGetAvailability fAvailability = new FGetAvailability();
					
					//Converting the input XML Document to a String format
					String inputXMLString = SCXmlUtil.getString(inputXML);
					//inputXMLString="<PriceAndAvailabilityRequest>    <sSourceIndicator>1</sSourceIndicator>    <sEnvironmentId>M</sEnvironmentId>    <sCustomerEnvironmentId>T3</sCustomerEnvironmentId>    <sCompany>XX</sCompany>    <sCustomerBranch>60</sCustomerBranch>    <sCustomerNumber>0006101695</sCustomerNumber>    <sShipToSuffix>000003</sShipToSuffix>    <sOrderBranch>6Y</sOrderBranch>    <aItems>        <aItem>            <sLineNumber>1</sLineNumber>            <sLegacyProductCode>2376041</sLegacyProductCode>            <sRequestedQtyUOM>M_BAG</sRequestedQtyUOM>            <sRequestedQty>19200.00</sRequestedQty>        </aItem>    </aItems></PriceAndAvailabilityRequest>>";
					//Creating the SOAP request input
					//fAvailability.setWsIpaperAvailabilityInput(inputXMLString);
					//input.setFGetAvailability(fAvailability);
					if(log.isDebugEnabled()){
						log.debug("Availability of webservice input : " + inputXMLString);						
					}        
			        //Invoking the WSDL and receiving the response simultaneously
					log.beginTimer("Calling-the-webmethod-PnA-webservice");
					String outputString=iPaperAvailablityPortType
								.fGetAvailability(inputXMLString);
					outputXML = YFCDocument.getDocumentFor(outputString).getDocument();
			       // FGetAvailabilityResponseE response = testStub.fGetAvailability(input) ; 
			        log.endTimer("Calling-the-webmethod-PnA-webservice");
			        if(log.isDebugEnabled()){
						log.debug("The response of availability of the webservice is: "+outputString);
			        }
			        log.error("The response of availability of the webservice is: "+outputString);
					//Converting the string reponse to XML Document format to return it to the calling app		
					//outputXML = YFCDocument.getDocumentFor(response.getFGetAvailabilityResponse().getWsIpaperAvailabilityOutput()).getDocument();
					break;
				}
				/*catch (SocketException ne) {
					String stackTrace=getStackTrace(ne);
					log.error("REmoteException: " + stackTrace);
					Element inputXMLEelm=(Element)inputXML.getDocumentElement().getFirstChild();
					inputXMLEelm.setAttribute("RetryCount", ""+(retry+1));
					logExceptionIntoCent(env,stackTrace,SCXmlUtil.getString(inputXMLEelm));
					//prepareErrorObject(ne, XPXLiterals.PA_TRANS_TYPE, XPXLiterals.NE_ERROR_CLASS, env, inputXML);
					
					invokeException(env,ne,inputXML,retry);
				}*/
				catch (NullPointerException ne) {
					/*String stackTrace=getStackTrace(ne);
					log.error("NullPointerException: " + stackTrace);
					Element inputXMLEelm=(Element)inputXML.getDocumentElement().getFirstChild();
					inputXMLEelm.setAttribute("RetryCount", ""+(retry+1));
					logExceptionIntoCent(env,stackTrace,SCXmlUtil.getString(inputXMLEelm));*/
					//prepareErrorObject(ne, XPXLiterals.PA_TRANS_TYPE, XPXLiterals.NE_ERROR_CLASS, env, inputXML);	
					invokeException(env,ne,inputXML,retry);
				} catch (YFSException yfe) {
					invokeException(env,yfe,inputXML,retry);
					//prepareErrorObject(yfe, XPXLiterals.PA_TRANS_TYPE, XPXLiterals.YFE_ERROR_CLASS, env, inputXML);			
				} catch (Exception e) {
					/*String stackTrace=getStackTrace(e);
					log.error("Exception: " + stackTrace);
					Element inputXMLEelm=(Element)inputXML.getDocumentElement().getFirstChild();
					inputXMLEelm.setAttribute("RetryCount", ""+(retry+1));
					logExceptionIntoCent(env,stackTrace,SCXmlUtil.getString(inputXMLEelm));*/
					//prepareErrorObject(e, XPXLiterals.PA_TRANS_TYPE, XPXLiterals.E_ERROR_CLASS, env, inputXML);
					invokeException(env,e,inputXML,retry);
				}
			}

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
	
	private void invokeException(YFSEnvironment env,Throwable aThrowable,Document inputXML,int retry)
	{
		String stackTrace=getStackTrace(aThrowable);
		log.error("YFSException: " + stackTrace);
		String exceptionStr=SCXmlUtil.getString(inputXML.getDocumentElement());
		exceptionStr=exceptionStr.replace("<PriceAndAvailabilityRequest", "<PriceAndAvailabilityRequest RetryCount=\""+(retry+1)+"\"");
		log.error("YFSException: " + stackTrace);
		try
		{
			logExceptionIntoCent(env,stackTrace,exceptionStr);
		}
		catch(Exception e)
		{
			log.error("Excpetion ");
		}
		try
		{
			int retryInterval=Integer.parseInt(YFSSystem.getProperty("PandAWaitBetweenRetries"));
			int retryIntervalinMili=retryInterval*1000;
			Thread.sleep(retryIntervalinMili);
		}
		catch(Exception e)
		{
			log.error("Exception while seting retry interval");
		}
	}
	private String getStackTrace(Throwable aThrowable) {
	    final Writer result = new java.io.StringWriter();
	    final PrintWriter printWriter = new PrintWriter(result);
	    aThrowable.printStackTrace(printWriter);
	    return result.toString();
	}
	 private  void logExceptionIntoCent(YFSEnvironment env,String exceptionMsg,String inputXML) throws Exception
	 {
		
		try{
				throw new Exception(exceptionMsg);
			
	        } catch (Exception e) {
			log.error("Exception: " +exceptionMsg);
			prepareErrorObject(e, XPXLiterals.SWC_ORDER_TRANS_TYPE, XPXLiterals.E_ERROR_CLASS, env, SCXmlUtil.createFromString(inputXML));
			
		}
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
