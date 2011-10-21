package com.sterlingcommerce.xpedx.webchannel.security;

import static com.sterlingcommerce.webchannel.utilities.WCConstants.SF_INFO_MASHUP_ID;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.ui.web.framework.SCUIConstants;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.framework.extensions.ISCUITransactionContext;
import com.sterlingcommerce.ui.web.framework.helpers.SCUITransactionContextHelper;
import com.sterlingcommerce.ui.web.framework.security.SCUISecurityResponse;
import com.sterlingcommerce.ui.web.platform.utils.SCUIPlatformUtils;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.IWCContextBuilder;
import com.sterlingcommerce.webchannel.core.binding.IWCXmlBindingSource;
import com.sterlingcommerce.webchannel.core.binding.WCXmlBindingFactory;
import com.sterlingcommerce.webchannel.core.binding.WCXmlBindingHelper;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.core.wcaas.WCContextBuilder_PostAuthenticationProvider;
import com.sterlingcommerce.webchannel.utilities.WCConstants;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.yantra.yfc.ui.backend.util.APIManager;
import com.yantra.yfc.util.YFCCommon;

public class XPEDXContextBuilder_PostAuthenticationProvider extends
		WCContextBuilder_PostAuthenticationProvider	{
	
	private static final Logger log = Logger.getLogger(XPEDXContextBuilder_PostAuthenticationProvider.class);  
	
	  private static final SCUISecurityResponse ERROR_RESPONSE = new SCUISecurityResponse(SCUISecurityResponse.FAILURE, "");
	    
	    private static final String CUSTOMER_INFO_MASHUP_ID = "getCustomerInfoForUser";            
	    
	    //private static final String XML_BINDING_FILE = "com/sterlingcommerce/webchannel/core/wcaas/WCContextBuilder_binding.xml";
	    private static final String XML_BINDING_FILE = "WCContextBuilder_binding.xml";
	    
	    private static final String OUTPUT_CATALOG_ORG = "//Organization/@OrganizationCode";
	    private static final String OUTPUT_CUSTOMER_ID = "//CustomerContactList/CustomerContact/Customer/@CustomerID";
	    private static final String OUTPUT_CUSTOMER_MSTR_ORG_CODE = "//CustomerContactList/CustomerContact/Customer/@OrganizationCode";
	    private static final String OUTPUT_BUYER_ORG = "//CustomerContactList/CustomerContact/Customer/@BuyerOrganizationCode";
	    private static final String OUTPUT_CUSTOMER_CONTACT_ID = "//CustomerContactList/CustomerContact/@CustomerContactID"; 
	    
	    private static final String OUTPUT_ORG_THEME_ID = "//Organization/OrgThemeList/OrgTheme[@IsDefault=\"Y\"]/@ThemeID";
	    
	    private static final String CUSTOMER_CURRENCIES = "//CustomerContactList/CustomerContact/Customer/CustomerCurrencyList";
	    
	    //initialize the Cache key to the class name
	    private static final String CACHE_KEY = "WCContextBuilder_PostAuthenticationProvider";
	    
	    private static final String NOT_AUTHORIZED_FOR_STOREFRONT = "NOT_AUTHORIZED_FOR_STOREFRONT";
	    private static final String STOREFRONT_NOT_CONFIGURED = "STOREFRONT_NOT_CONFIGURED";
	    private static final String SYSTEM_ERROR = "SYSTEM_ERROR";
	    private String lastLoginDate;
	 /***
     * This method does the work of obtaining the Customer/Customer Contact information of the logged in user.
     * Appropriate mashups are invoked to obtain the information from the backend. The fetched information is then
     * set on the Builder object.
     * @param SCUIContext 
     * @return SCUISecurityResponse 
     */
	    @Override
    public SCUISecurityResponse postAuthenticate(SCUIContext scuiCtx) {
        if (log.isDebugEnabled()) {
            log.debug(" > postAuthentiate");
        }
        try {
            SCUISecurityResponse response = (SCUISecurityResponse) scuiCtx.getRequest().getAttribute(SCUIConstants.SECURITY_RESPONSE_ATTR_NAME);
                        
            //get the builder object            
            IWCContextBuilder builder = WCContextHelper.getBuilder(scuiCtx.getRequest(), scuiCtx.getResponse());                                     
            
            //check to see if the binding file has already been loaded
            if(! WCXmlBindingHelper.INSTANCE.isBindingInformationLoaded(CACHE_KEY)) {                            
                boolean isFileLoaded = WCXmlBindingHelper.INSTANCE.loadBindingInformation(CACHE_KEY, XML_BINDING_FILE,WCContextBuilder_PostAuthenticationProvider.class);
                if(!isFileLoaded) {
                    log.error("Failed to load Xml binding file: " + XML_BINDING_FILE + " for cache key: " + CACHE_KEY);
                    return response;
                }
            }    
            
            //obtain the context stack
            Stack<IWCXmlBindingSource> ctxStack = WCXmlBindingFactory.getWCContextStack(builder);
            
            //invoke mashup to fetch catalog org
            Set<String> mashupIds = new HashSet<String> ();
            mashupIds.add(SF_INFO_MASHUP_ID);
                        
            Map<String, Element> mashupOutput = WCMashupHelper.prepareAndInvokeMashups(CACHE_KEY, mashupIds, ctxStack, builder);                                                                   
            
            if (!validateAndSetStoreFrontInfoInBuilder(builder, mashupOutput.get(SF_INFO_MASHUP_ID))) {
                return prepareErrorResponse(scuiCtx, getLoginRedirectorPage(scuiCtx), STOREFRONT_NOT_CONFIGURED);
            }
            
            //set the builder
            WCContextHelper.setBuilder(scuiCtx.getRequest(), builder);
            
            if (scuiCtx.getSecurityContext().isGuestUser())
                return response;//No Customer/CustomerContact for the guest user
            
            //invoke mashup to get customer info
            mashupIds.clear();
            mashupIds.add(CUSTOMER_INFO_MASHUP_ID);
            
            /*Jira 2632 - For sales Rep ,Setting the selected enterprise code as Store front id*/
            if(scuiCtx.getRequest().getParameter("EnterpriseCode")!=null && (!YFCCommon.isVoid(scuiCtx.getRequest().getParameter("EnterpriseCode")))){
            	builder.setStorefrontId(scuiCtx.getRequest().getParameter("EnterpriseCode"));
            }
            /**/
            mashupOutput = WCMashupHelper.prepareAndInvokeMashups(CACHE_KEY, mashupIds, ctxStack, builder);  
            
            if(log.isDebugEnabled())
                log.debug("Trying to set Customer/CustomerContact information in the Builder");
 
            // if there is no customer or contact information - use is not authorized for the current storefront 
            //set the appropriate attribute values in the builder based on the response
            if (!Boolean.TRUE.equals(scuiCtx.getAttribute("IS_LDAP_AUTHENTICATED")) && !validateAndsetCustomerInfoInBuilder(builder, mashupOutput.get(CUSTOMER_INFO_MASHUP_ID))) {
                return prepareErrorResponse(scuiCtx, getLoginRedirectorPage(scuiCtx), NOT_AUTHORIZED_FOR_STOREFRONT);
            }
            
            //set the builder in session
            WCContextHelper.setBuilder(scuiCtx.getRequest(), builder);
            
            //set status to SUCCESS
            response.setReturnStatus(SCUISecurityResponse.SUCCESS);
            //Following code of removing the transactionContext should be removed once Bug 193105 is fixed from platform.
            // get the existing transaction from request.
            
            ISCUITransactionContext transactionContext = builder.getSCUIContext().getTransactionContext(false);
            if (transactionContext != null) {
                // Roll back any uncommitted changes
                transactionContext.rollback();
                // End the transactionR
                transactionContext.end();
                // Return the transaction context back to factory.
                SCUITransactionContextHelper.releaseTransactionContext(transactionContext, builder.getSCUIContext());
                // Remove the transaction context reference from request.
                builder.getSCUIContext().removeTransactionContext();
            }
            //OrderHelper.transferCartAndSetInContextOnLogin(builder);
            //Stamping the last login time stamp on the customer contact field stamped is ExtnLastLoginDate
        	/* Removed as this is causing the Customer cache reload. @jkotha
            try {
        		YFCDate loginDate = new YFCDate();
            	lastLoginDate = loginDate.getString();
            	IWCContext wcContext = WCContextHelper.getWCContext(scuiCtx.getRequest());
            	Map<String, String> valueMap = new HashMap<String, String>();
            	String mashupId = "XPEDXUpdateLastLoginDate";
            	valueMap.put("/Customer/@CustomerID", wcContext.getCustomerId());
            	valueMap.put("/Customer/@OrganizationCode", wcContext.getStorefrontId());
            	valueMap.put("/Customer/CustomerContactList/CustomerContact/@CustomerContactID", wcContext.getCustomerContactId());
            	valueMap.put("/Customer/CustomerContactList/CustomerContact/Extn/@ExtnLastLoginDate",lastLoginDate);

            	Element input = WCMashupHelper.getMashupInput(mashupId, valueMap, wcContext
    					.getSCUIContext());
            	String inputXml = SCXmlUtil.getString(input);
    	
            	log.debug("Input XML: " + inputXml);
    		
            	//Object obj = WCMashupHelper.invokeMashup(mashupId, input, wcContext.getSCUIContext());				
			} catch (Exception e) {
				 if (log.isDebugEnabled()) {
		                log.debug("< postAuthenticate Unable to stamp the last login date ");
		            }
				 return response;
			}
			*/
            return response;
        }        
        catch(WCMashupHelper.CannotBuildInputException cbex) {
            log.warn("Exception while trying to build the input for obtaining Catalog org and Customer information for the logged in user", cbex);
            return prepareErrorResponse(scuiCtx, getLoginRedirectorPage(scuiCtx), SYSTEM_ERROR);
        }
        catch(APIManager.XMLExceptionWrapper ex) {
            log.warn("Mashup invocation returned an error response", ex.getCause());
            return prepareErrorResponse(scuiCtx, getLoginRedirectorPage(scuiCtx), SYSTEM_ERROR);
        }
        catch(XPathExpressionException xex) {
            log.warn("XPATH expression exception while trying to glean Catalog org and Customer information", xex);
            return prepareErrorResponse(scuiCtx, getLoginRedirectorPage(scuiCtx), SYSTEM_ERROR);
        }
        finally {
            if (log.isDebugEnabled()) {
                log.debug(" < postAuthentiate");
            }
        }
                
    }
    private boolean validateAndSetStoreFrontInfoInBuilder(IWCContextBuilder builder, Node response) throws XPathExpressionException
    {                
        if(log.isDebugEnabled())
            log.debug("Trying to set Catalog org in the Builder");
        
        //get Catalog org
        Node n = XMLUtilities.evaluatePath(response, OUTPUT_CATALOG_ORG);        
        
        // set storefront default theme in the context
        n = XMLUtilities.evaluatePath(response, OUTPUT_ORG_THEME_ID);        
        if(n != null) {
            String sfThemeId = n.getNodeValue();
            builder.setCurrentStorefrontThemeId(sfThemeId);            
        }                     
        
        return true;
    }
    
    private String getLoginPage(SCUIContext scuiCtx) {
        return SCUIPlatformUtils.getLoginPage(scuiCtx);
    }
    
    private String getLoginRedirectorPage(SCUIContext scuiCtx) {        
        //get the configured login redirector page
        String loginPage = scuiCtx.getServletContext().getInitParameter(WCConstants.WC_LOGIN_REDIRECOR_PAGE_PARAM_NAME);
        if(log.isDebugEnabled())
            log.debug("Login redirector page is: " + loginPage);
        return loginPage;
    }

    private SCUISecurityResponse prepareErrorResponse(SCUIContext scuiCtx, String forwardPage, String errorMessage) {
        SCUISecurityResponse error = new SCUISecurityResponse(SCUISecurityResponse.FAILURE, forwardPage);
        error.setErrorMessage(errorMessage);
        scuiCtx.getSession().invalidate();
        return error;
    }
    
    private boolean validateAndsetCustomerInfoInBuilder(IWCContextBuilder builder, Node response) throws XPathExpressionException
    {                
        //get Customer id
        Node n = XMLUtilities.evaluatePath(response, OUTPUT_CUSTOMER_ID);        
        if(n != null) {
            String custId = n.getNodeValue();
            builder.setCustomerId(custId);            
        } else {
            log.error("CustomerID not be found");
            return false;
        }
        // get Customer master organization code
         n = XMLUtilities.evaluatePath(response,OUTPUT_CUSTOMER_MSTR_ORG_CODE);
        if(n != null) {
            String custMstrOrg = n.getNodeValue();
            builder.setCustomerMstrOrg(custMstrOrg);            
        } else {
            log.error("Customer master organization code not be found");
            return false;
        }
        
        //get Buyer Org
        n = XMLUtilities.evaluatePath(response, OUTPUT_BUYER_ORG);
        if(n != null) {
            String buyerOrg = n.getNodeValue();
            builder.setBuyerOrgCode(buyerOrg);            
        } else {
            log.error("BuyerOrg not be found");
            return false;
        }
                                        
        //get Customer Contact id
        n = XMLUtilities.evaluatePath(response, OUTPUT_CUSTOMER_CONTACT_ID);
        if ( n != null) {
            String contactId = n.getNodeValue();
            builder.setCustomerContactId(contactId);            
        } else {
            log.error("CustomerContact not be found");
            return false;
        }
        
        n = XMLUtilities.evaluatePath(response, CUSTOMER_CURRENCIES);
        if (n != null) {
            Set<String> currencies = new LinkedHashSet<String>();
            Iterator eleCurrencyList = SCXmlUtils.getChildren((Element)n);
            while(eleCurrencyList.hasNext())
            {
                Element eleCurrency= (Element)eleCurrencyList.next();
                String cur = SCXmlUtils.getAttribute(eleCurrency, "Currency");
                String isDefault = SCXmlUtils.getAttribute(eleCurrency, "IsDefaultCurrency");
                if ((isDefault != null) && (isDefault.equalsIgnoreCase("Y"))) {
                    builder.setDefaultCurrency(cur);
                }
                currencies.add(cur);
            }
            builder.setCustomerCurrencies(currencies);
        }

        return true;
    }

}
