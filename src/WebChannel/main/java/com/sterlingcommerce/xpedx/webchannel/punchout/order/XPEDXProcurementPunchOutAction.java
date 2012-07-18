package com.sterlingcommerce.xpedx.webchannel.punchout.order;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.framework.extensions.ISCUITransactionContext;
import com.sterlingcommerce.ui.web.framework.helpers.SCUITransactionContextHelper;
import com.sterlingcommerce.ui.web.platform.transaction.SCUITransactionContextFactory;
import com.sterlingcommerce.webchannel.common.eprocurement.IAribaContext;
import com.sterlingcommerce.webchannel.common.eprocurement.IProcurementContext;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.utilities.SWCProperties;
import com.sterlingcommerce.webchannel.utilities.UtilBean;
import com.sterlingcommerce.webchannel.utilities.WCIntegrationXMLUtils;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCUtils;
import com.sterlingcommerce.xpedx.webchannel.order.utilities.XPEDXCommerceContextHelper;
import com.sterlingcommerce.xpedx.webchannel.punchout.servlet.IOciContext;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.xpedx.nextgen.common.util.XPXTranslationUtilsAPI;
import com.yantra.yfs.japi.YFSEnvironment;

/**
 * This class is the main action handler for dealing with punch-out
 * (i.e. return to) an e-procurement system.  The initial version
 * only supports Ariba e-procurement systems.
 */
public class XPEDXProcurementPunchOutAction extends WCMashupAction
{
    public static final String MODE_SAVE = "save";
    public static final String MODE_CANCEL = "cancel";

    public static final String DRAFT_ORDER_DELETE_MASHUP = "draftOrderDelete";
    public static final String GET_SOURCE_ORDER_FOR_ARIBA_PUNCHOUT_MASHUP = "getSourceOrderForAribaPunchOut";
    public static final String GET_PUNCH_OUT_ORDER_MESSAGE_CXML_MASHUP = "XPEDXGetPunchOutOrderMessageCXML";
    private static final String customerExtnInfoMashUp = "xpedx-customer-getCustomExtnInformation";

    public static final String USER_AGENT_PROPERTY_KEY = "swc.ariba.userAgent";

    protected String mode = null;
    protected String punchOutOrderMessage = null;
    protected String returnURL = null;
    protected String orderHeaderKey = null;
    protected Element sourceOrderForAribaPunchout = null;
    protected String identity = null;
    protected String toIdentity = null;
    protected String userAgent = null;
    protected String buyerCookie = null;
    protected String xsltVersion = null;
    protected String xsltFileName = null;
    protected String replaceChars = null;
    protected String msap = null;

    private static final Logger LOG = Logger.getLogger(XPEDXProcurementPunchOutAction.class);

    public String execute()
    {
    	String toReturn = SUCCESS;
        try
        {
            UtilBean util = new UtilBean();
            IProcurementContext procurementContext = util.getProcurementContext(getWCContext());

            if(procurementContext == null)
            {
                throw new Exception("No procurement punch in context information found.");
            }

            if(procurementContext instanceof IAribaContext)
            {	//Ariba Punchout  or CXml Punchout
            	//toReturn = "ariba_"+toReturn;
                handleAribaPunchout((IAribaContext)procurementContext);
            }
            else if(procurementContext instanceof IOciContext)
            {	// OCI/SAP Punchout 
            	//toReturn = "oci_"+toReturn;
            	handleOciPunchout((IOciContext)procurementContext);
            }
            else
            {
            	// Unknown punch in session in progress.
                throw new Exception("Unknown punch in session type.");
            }

        }
        catch(Exception e)
        {
            LOG.error(e.getMessage(), e);
            WCUtils.setErrorInContext(getWCContext(), e);

            return ERROR;
        }

        return toReturn;
    }

    /**
     * Handles punch out for an Ariba punch in session.  Draft orders are deleted as required,
     * and the PunchOutOrderMessage is populated (using the populatePunchOutOrder method).
     *
     * @param aribaContext the Ariba context object; callers should ensure that this is not null.
     * @throws Exception for unsupported mode/operation combinations, or if an error occurs.
     */
    public void handleAribaPunchout(IAribaContext aribaContext) throws Exception
    {
        int operation = aribaContext.getOperation();
        returnURL = aribaContext.getReturnURL();
        String cicOrderHeaderKey = (String)XPEDXWCUtils.getObjectFromCache("OrderHeaderInContext");//XPEDXCommerceContextHelper.getCartInContextOrderHeaderKey(getWCContext());

        if(MODE_CANCEL.equals(mode))
        {
            switch(operation)
            {
                case IProcurementContext.PROCUREMENT_OPERATION_CREATE:
                    // For create, there may not be a cart in context if no add or edit had been attempted
                    // yet, so check for the possibility of a null order header key.
                    if(cicOrderHeaderKey != null)
                    {
                        deleteCart(cicOrderHeaderKey);
                    }
                    break;

                case IProcurementContext.PROCUREMENT_OPERATION_EDIT:
                    deleteCart(cicOrderHeaderKey);
                    break;

                case IProcurementContext.PROCUREMENT_OPERATION_INSPECT:
                    // Intentionally left blank.
                    break;

                default:
                    throwUnsupportedModeOperationException(mode, operation);
                    break;
            }

            populatePunchOutOrderMessage(null, aribaContext);
        }
        else if(MODE_SAVE.equals(mode))
        {
            switch(operation)
            {
                case IProcurementContext.PROCUREMENT_OPERATION_CREATE:
                    // Intentionally left blank.
                    break;

                case IProcurementContext.PROCUREMENT_OPERATION_EDIT:
                    deleteCart(aribaContext.getOrderHeaderKey());
                    break;

                default:
                    throwUnsupportedModeOperationException(mode, operation);
                    break;
            }

            populatePunchOutOrderMessage(cicOrderHeaderKey, aribaContext);
        }
        else
        {
            throwUnsupportedModeOperationException(mode, operation);
        }
    }
    
    /**
     * Handles punch out for an OCI/SAP punch in session.  Draft orders are deleted as required,
     * and the PunchOutOrderMessage is populated (using the populatePunchOutOrder method).
     *
     * @param ociContext the IOciContext context object; callers should ensure that this is not null.
     * @throws Exception for unsupported mode/operation combinations, or if an error occurs.
     */
    public void handleOciPunchout(IOciContext ociContext) throws Exception
    {
        int operation = ociContext.getOperation();
        returnURL = ociContext.getReturnURL();
        String cicOrderHeaderKey = (String)XPEDXWCUtils.getObjectFromCache("OrderHeaderInContext");//XPEDXCommerceContextHelper.getCartInContextOrderHeaderKey(getWCContext());

        if(MODE_CANCEL.equals(mode))
        {
            switch(operation)
            {
                case IProcurementContext.PROCUREMENT_OPERATION_CREATE:
                    // For create, there may not be a cart in context if no add or edit had been attempted
                    // yet, so check for the possibility of a null order header key.
                    if(cicOrderHeaderKey != null)
                    {
                        deleteCart(cicOrderHeaderKey);
                    }
                    break;

                case IProcurementContext.PROCUREMENT_OPERATION_EDIT:
                    deleteCart(cicOrderHeaderKey);
                    break;

                case IProcurementContext.PROCUREMENT_OPERATION_INSPECT:
                    // Intentionally left blank.
                    break;

                default:
                    throwUnsupportedModeOperationException(mode, operation);
                    break;
            }

            populatePunchOutOrderMessage(null, ociContext);
        }
        else if(MODE_SAVE.equals(mode))
        {
            switch(operation)
            {
                case IProcurementContext.PROCUREMENT_OPERATION_CREATE:
                    // Intentionally left blank.
                    break;

                case IProcurementContext.PROCUREMENT_OPERATION_EDIT:
                    deleteCart(ociContext.getOrderHeaderKey());
                    break;

                default:
                    throwUnsupportedModeOperationException(mode, operation);
                    break;
            }

            populatePunchOutOrderMessage(cicOrderHeaderKey, ociContext);
        }
        else
        {
            throwUnsupportedModeOperationException(mode, operation);
        }
    }

    /**
     * Throws an exception with a message built up from the given mode and operation.
     *
     * @param mode the mode to include in the exception message.
     * @param operation the operation to include in the exception message.
     */
    public void throwUnsupportedModeOperationException(String mode, int operation) throws Exception
    {
        throw new Exception("Unsupported mode (" + mode + ") and operation (" + operation + ") combination encountered");
    }

    /**
     * Deletes the order matching the given deleteOrderHeaderKey.
     *
     * @param deleteOrderHeaderKey the order header key of the order to delete.
     * @throws Exception
     */
    public void deleteCart(String deleteOrderHeaderKey) throws Exception
    {
        orderHeaderKey = deleteOrderHeaderKey;
        prepareAndInvokeMashup(DRAFT_ORDER_DELETE_MASHUP);
    }

    /**
     * Sets the punchOutOrderMessage based on the given orderHeaderKey.
     *
     * @param orderHeaderKey the key of the order to create the PunchOutOrderMessage for;
     *        pass null to get the "empty" message for the cancel mode.
     * @param aribaContext the Ariba context object; callers should ensure that this is not null.
     */
    public void populatePunchOutOrderMessage(String orderHeaderKey, IAribaContext aribaContext) throws Exception
    {
        // If orderHeaderKey is non-null, restore the source order.
        if(orderHeaderKey != null)
        {
            this.orderHeaderKey = orderHeaderKey;
            sourceOrderForAribaPunchout = prepareAndInvokeMashup(GET_SOURCE_ORDER_FOR_ARIBA_PUNCHOUT_MASHUP);
        }

        // Set the context parameter variables
        toIdentity = aribaContext.getFromIdentity();
        identity = aribaContext.getToIdentity();
        buyerCookie = aribaContext.getBuyerCookie();
        userAgent = SWCProperties.getProperty(USER_AGENT_PROPERTY_KEY);
        xsltVersion = "1.0";

        IWCContext context = WCContextHelper.getWCContext(ServletActionContext
				.getRequest());
		SCUIContext wSCUIContext = context.getSCUIContext();	
		ISCUITransactionContext scuiTransactionContext = wSCUIContext
				.getTransactionContext(true);
		YFSEnvironment env = (YFSEnvironment) scuiTransactionContext
				.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);
		
		String customerID = context.getCustomerId();
		String storeFrontId = context.getStorefrontId();
		msap = XPXTranslationUtilsAPI.getMsap(env, customerID,storeFrontId);

		/**
		 * JIRA 243
		 * Modified getCustomerDetails method to consider the mashup to be invoked
		 * so that, we get only the required information - here ExtnXSLTVer.
		 * @param inputItems
		 * @return
		 */
        //Document CustDetails = XPEDXWCUtils.getCustomerDetails(customerID, storeFrontId, customerExtnInfoMashUp);
		Document CustDetails = XPEDXWCUtils.getCustomerDetails(msap, storeFrontId);
        xsltVersion = SCXmlUtils.getXpathAttribute(CustDetails.getDocumentElement(), "/Customer/Extn/@ExtnXSLTVer");
        xsltFileName = SCXmlUtils.getXpathAttribute(CustDetails.getDocumentElement(), "/Customer/Extn/@ExtnXSLTFileName");
        replaceChars = SCXmlUtils.getXpathAttribute(CustDetails.getDocumentElement(), "/Customer/Extn/@ExtnReplaceCharacter");

        Element output = prepareAndInvokeMashup(GET_PUNCH_OUT_ORDER_MESSAGE_CXML_MASHUP);
        if(LOG.isDebugEnabled()){
        LOG.debug("***************************PUNCHOUT CART OUTPUT***************************");
        LOG.debug(SCXmlUtils.getString(output));
        LOG.debug("***************************PUNCHOUT CART OUTPUT***************************");
        }
        punchOutOrderMessage = WCIntegrationXMLUtils.AttachDocType(output.getOwnerDocument());
        
        removeUnacceptableCharacters();
        if(LOG.isDebugEnabled()){
        LOG.debug(SCXmlUtils.getString(output));
        }
        LOG.debug("Punch Out Order Message : " + punchOutOrderMessage);
        

    }
    
    /**
     * Sets the punchOutOrderMessage based on the given orderHeaderKey.
     *
     * @param orderHeaderKey the key of the order to create the PunchOutOrderMessage for;
     *        pass null to get the "empty" message for the cancel mode.
     * @param aribaContext the Ariba context object; callers should ensure that this is not null.
     */
    public void populatePunchOutOrderMessage(String orderHeaderKey, IOciContext ociContext) throws Exception
    {
        // If orderHeaderKey is non-null, restore the source order.
        if(orderHeaderKey != null)
        {
            this.orderHeaderKey = orderHeaderKey;
            sourceOrderForAribaPunchout = prepareAndInvokeMashup(GET_SOURCE_ORDER_FOR_ARIBA_PUNCHOUT_MASHUP);
        }

        // Set the context parameter variables
        //toIdentity = ociContext.getFromIdentity();
        //identity = ociContext.getToIdentity();
        buyerCookie = ociContext.getBuyerCookie();
        userAgent = SWCProperties.getProperty(USER_AGENT_PROPERTY_KEY);
        xsltVersion = "1.0";

		IWCContext context = WCContextHelper.getWCContext(ServletActionContext
				.getRequest());
		SCUIContext wSCUIContext = context.getSCUIContext();	
		ISCUITransactionContext scuiTransactionContext = wSCUIContext
				.getTransactionContext(true);
		YFSEnvironment env = (YFSEnvironment) scuiTransactionContext
				.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);
		
		String customerID = context.getCustomerId();
		String storeFrontId = context.getStorefrontId();
		// Getting the msap as all the fields are from the msap customer.
		msap = XPXTranslationUtilsAPI.getMsap(env, customerID,storeFrontId);
		/**
		 * JIRA 243
		 * Modified getCustomerDetails method to consider the mashup to be invoked
		 * so that, we get only the required information - here ExtnXSLTVer.
		 * @param inputItems
		 * @return
		 */
        //Document CustDetails = XPEDXWCUtils.getCustomerDetails(customerID, storeFrontId, customerExtnInfoMashUp);
		Document CustDetails = XPEDXWCUtils.getCustomerDetails(msap, storeFrontId);
        xsltVersion = SCXmlUtils.getXpathAttribute(CustDetails.getDocumentElement(), "/Customer/Extn/@ExtnXSLTVer");
        xsltFileName = SCXmlUtils.getXpathAttribute(CustDetails.getDocumentElement(), "/Customer/Extn/@ExtnXSLTFileName");
        replaceChars = SCXmlUtils.getXpathAttribute(CustDetails.getDocumentElement(), "/Customer/Extn/@ExtnReplaceCharacter");
        
//        Element input = WCMashupHelper.getMashupInput(GET_PUNCH_OUT_ORDER_MESSAGE_CXML_MASHUP, context);
//
//        Object output = WCMashupHelper.invokeMashup(GET_PUNCH_OUT_ORDER_MESSAGE_CXML_MASHUP, input, wSCUIContext);
//        output.toString();
        Element output = prepareAndInvokeMashup(GET_PUNCH_OUT_ORDER_MESSAGE_CXML_MASHUP);
        if(LOG.isDebugEnabled()){
        LOG.debug("***************************PUNCHOUT CART OUTPUT***************************");
        LOG.debug(SCXmlUtils.getString(output));
        LOG.debug("***************************PUNCHOUT CART OUTPUT***************************");
        }
        //punchOutOrderMessage = WCIntegrationXMLUtils.AttachDocType(output.getOwnerDocument());
        punchOutOrderMessage = SCXmlUtil.getString(output);
        
        removeUnacceptableCharacters();
        if(LOG.isDebugEnabled()){
        LOG.debug(SCXmlUtils.getString(output));
     
        LOG.debug("Punch Out Order Message : " + punchOutOrderMessage);
        }
        

    }
    // replace characters (*,!,@,#,$) Example
    protected void removeUnacceptableCharacters(){
    	char[] replaceCharsArray = replaceChars.toCharArray();
    	for(int i=0; i<replaceCharsArray.length; i++) {
    		char charToReplace = replaceCharsArray[i];
    		punchOutOrderMessage = punchOutOrderMessage.replace(charToReplace, ' ');
    	}
    }

    /**
     * Manipulates the input for the GET_PUNCH_OUT_ORDER_MESSAGE_CXML_MASHUP mashup by inserting sourceOrderForAribaPunchout,
     * if it is non-null.
     *
     * @param mashupInputs the map of inputs to examine and manipulate.
     */
    protected void manipulateMashupInputs(Map<String, Element> mashupInputs) throws WCMashupHelper.CannotBuildInputException
    {
    	YFSEnvironment env = null;
    	ISCUITransactionContext scuiTransactionContext = null;
    	SCUIContext wSCUIContext = null;
        try
        {
        	IWCContext context = WCContextHelper.getWCContext(ServletActionContext
    				.getRequest());
    		String customerID = context.getCustomerId();
    		String orgCode = context.getStorefrontId();

            Element punchOutOrderInput = mashupInputs.get(GET_PUNCH_OUT_ORDER_MESSAGE_CXML_MASHUP);
            if( (punchOutOrderInput != null) && (sourceOrderForAribaPunchout != null) )
            {
            	wSCUIContext = context.getSCUIContext();
        		scuiTransactionContext = wSCUIContext
        				.getTransactionContext(true);
            	env = (YFSEnvironment) scuiTransactionContext
						.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);
            	
            	//XPXTranslationUtilsAPI  tu = new XPXTranslationUtilsAPI();
            	if(SCUtil.isVoid(msap))
            		msap = XPXTranslationUtilsAPI.getMsap (env, customerID,orgCode);
            	
            	
            	 //Find UOMs in the Document and replace with UOMReplacement
            	//& Find UNSPSCs in the Document and replace with UNSPSCReplacement
                NodeList orderLinesNode = sourceOrderForAribaPunchout.getElementsByTagName("OrderLines");
                if(orderLinesNode.getLength() != 0){
                	Element orderLinesElement = (Element)orderLinesNode.item(0);
                	NodeList orderLineNode = orderLinesElement.getElementsByTagName("OrderLine");
                	if(orderLineNode.getLength() !=0){
                		for(int oCounter = 0;oCounter < orderLineNode.getLength();oCounter++)
        				{
                			Element orderLineElement = (Element)orderLineNode.item(oCounter);
                			NodeList itemNode = orderLineElement.getElementsByTagName("Item");
                			Element itemElement = (Element)itemNode.item(0);
                			String itemid = itemElement.getAttribute("ItemID");
                			String UOM = itemElement.getAttribute("UnitOfMeasure");
                			String rUOM = XPEDXWCUtils.getUOMReplacement(env, msap, UOM);
                			itemElement.setAttribute("UnitOfMeasure", rUOM);

                			NodeList itemDetailsNode = orderLineElement.getElementsByTagName("ItemDetails");
                			Element itemDetailsElement = (Element)itemDetailsNode.item(0);
                			NodeList classificationCodesNode = orderLineElement.getElementsByTagName("ClassificationCodes");
                			if(classificationCodesNode.getLength() != 0){
                				for(int ccnCounter = 0;ccnCounter < classificationCodesNode.getLength();ccnCounter++){
                					Element ccElement = (Element)classificationCodesNode.item(ccnCounter);
                					if(ccElement.hasAttribute("UNSPSC")){
                            			String UNSPSC = ccElement.getAttribute("UNSPSC");
                            			String rUNSPSC = XPEDXWCUtils.getUNSPSCReplacement(env, msap, itemid, UNSPSC);
                            			ccElement.setAttribute("UNSPSC", rUNSPSC);
                					}
                				}
                			}

        				}
                	}
                }



                Document punchOutOrderInputDocument = punchOutOrderInput.getOwnerDocument();
                Node nodeToImport = punchOutOrderInputDocument.importNode(sourceOrderForAribaPunchout, true);
                punchOutOrderInput.appendChild(nodeToImport);
                if(LOG.isDebugEnabled()){
                LOG.debug("***************************PUNCHOUT CART INPUT***************************");
                LOG.debug(SCXmlUtils.getString(punchOutOrderInput));
                LOG.debug("***************************PUNCHOUT CART INPUT***************************");
                }
            }
        }
        catch(Exception e)
        {
            throw new WCMashupHelper.CannotBuildInputException("Error encountered manipulating InputXML", e);
        }
        finally{
        	if(scuiTransactionContext!=null){	
        		scuiTransactionContext.end();
        		SCUITransactionContextHelper.releaseTransactionContext(
				scuiTransactionContext, wSCUIContext);
        	}
        	env = null;
        }
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPunchOutOrderMessage() {
        return punchOutOrderMessage;
    }

    public String getReturnURL() {
        return returnURL;
    }

    public String getOrderHeaderKey() {
        return orderHeaderKey;
    }

    public String getIdentity() {
        return identity;
    }

    public String getToIdentity() {
        return toIdentity;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getBuyerCookie() {
        return buyerCookie;
    }

    public String getXsltVersion() {
        return xsltVersion;
    }

	public String getXsltFileName() {
		return xsltFileName;
	}

	public void setXsltFileName(String xsltFileName) {
		this.xsltFileName = xsltFileName;
	}

	public void setXsltVersion(String xsltVersion) {
		this.xsltVersion = xsltVersion;
	}
}

