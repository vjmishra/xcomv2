/**
 * 
 */
package com.sterlingcommerce.xpedx.webchannel.profile.org;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.crystaldecisions.celib.synchronization.RWLock;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer;
import com.sterlingcommerce.webchannel.profile.ProfileUtility;
import com.sterlingcommerce.webchannel.utilities.WCDataDeFormatHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCUtils;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.webchannel.utilities.YfsUtils;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.date.YDate;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfc.util.YFCDate;
import com.yantra.yfc.util.YFCLocale;
import com.yantra.yfc.util.YFCTimeZone;


@SuppressWarnings("all")
public class XPEDXGetDivisionArticle extends WCMashupAction {

	private String customerId;
	private String customerDivisionToQry;
    private String isChildCustomer;
    private String customerContactId;
    
    //ResourceIds
    private static final String USER_LIST_RESOURCE_ID = "/swc/profile/ManageUserList";
    private static final String MANAGE_USER_PROFILE_RESOURCE_ID = "/swc/profile/ManageUserProfile";
    private static final String customerDivisionMashUp = "xpedx-customer-getCustomExtnFieldsInformation";
    
    protected List<Element> articleElements = null;
    
    public List custArticleSearchName;
    public List custArticleSearchValue;
    protected String startTodayDate=""; 
    protected String endTodayDate="";
    
    protected XPEDXWCUtils xpedxWCUtilsBean;
    
    public XPEDXGetDivisionArticle() {
		super();
		custArticleSearchName = new ArrayList();
		custArticleSearchValue = new ArrayList();
	}

	/*
     * Get the root of the Notes on the object
     * @return string value with the path to the root of the notes element
     */
    protected String getArticleLocation() {
        return "/XPXArticleList/XPXArticle";
    }

    public String execute()
    {
        String inputCustomerID = customerId;
        customerContactId=getWCContext().getCustomerContactId();
        if(YFCCommon.isVoid(inputCustomerID)){
            inputCustomerID = wcContext.getCustomerId();
            customerId = inputCustomerID;
        }
        if(!(ProfileUtility.isCustInCtxCustHierarchy(inputCustomerID, wcContext)))
            return ERROR;
        if(MANAGE_USER_PROFILE_RESOURCE_ID.equals(resourceId)){
            try {
                if(!(ResourceAccessAuthorizer.getInstance().isAuthorized(USER_LIST_RESOURCE_ID, getWCContext()))){
                    if(!isSelfAdmin())
                        return "";
                }
            } catch (Exception e1) {
                return ERROR;
            }
        }
        
        setArticleElements();
		return SUCCESS;
        
    }
    
    public List<Element> getArticleLines(){
    	 if (this.articleElements == null) {
    		 setArticleElements();
         }
         return this.articleElements;
    }
    
	/**
	 * JIRA 243
	 * Modified getCustomerDetails method to consider the mashup to be invoked
	 * so that, we get only the required information - here ExtnCustomerDivision.
	 * @param inputItems
	 * @return
	 */
    public void setArticleElements(){
    	try{
//    		Element customerInfo = XPEDXWCUtils.getCustomerDetails(customerId, wcContext.getStorefrontId(), customerDivisionMashUp).getDocumentElement();
    		String customerID= wcContext.getCustomerId();
    		String[] customerIdParts = customerID.split("-");
    		customerDivisionToQry='|'+customerIdParts[0]+'|';
//    		String customerDivision = SCXmlUtil.getXpathAttribute(customerInfo, "/Customer/Extn/@ExtnCustomerDivision");
//    		customerDivisionToQry = '|'+customerDivision+'|';
    		custArticleSearchName.add("XPXDivision");
    		custArticleSearchValue.add(customerDivisionToQry);
    		custArticleSearchName.add("ArticleType");
    		custArticleSearchValue.add("D");
 			Element e = prepareAndInvokeMashup("XPEDXDivisionArticleList");
 			String xml = SCXmlUtil.getString(e.getOwnerDocument());
 			this.articleElements = getArticleElements(e,getArticleLocation());
         }
 		catch(Exception e){
 			e.printStackTrace();
 		}
    }
    
    public List<Element> getArticleElements(Element xapiOutput, String xpathToNoteElement) {
    	
        List<Element> articleElements = new ArrayList<Element>();
        
        try {
            NodeList articleLines = (NodeList) XMLUtilities.evaluate(xpathToNoteElement, xapiOutput, XPathConstants.NODESET);
            int length = articleLines.getLength();
            
            for (int i = length-1; i >= 0; i--) {
                Element currNode = (Element) articleLines.item(i);
                //TODO: add a filter here to see if this node should be added or not. Only few users will be able to see the artilces.
                articleElements.add(currNode);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return articleElements;
    }
    
    
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
        setIsChildCustomer(ProfileUtility.setCustomerAccess(customerId, wcContext));
    }

    public String getIsChildCustomer() {
        return isChildCustomer;
    }

    public void setIsChildCustomer(String isChildCustomer) {
        this.isChildCustomer = isChildCustomer;
    }
    
    /**
     * @return true: if seeing own user profile
     */
    public boolean isSelfAdmin() {
        String loggedInCustomerContactId = getWCContext()
                .getCustomerContactId();
        if (getCustomerContactId().compareTo(loggedInCustomerContactId) == 0) {
            return true;
        } else {
            return false;
        }
    }

	/**
	 * @return the customerContactId
	 */
	public String getCustomerContactId() {
		return customerContactId;
	}

	/**
	 * @param customerContactId the customerContactId to set
	 */
	public void setCustomerContactId(String customerContactId) {
		this.customerContactId = customerContactId;
	}


	/**
	 * @return the todaysDate
	 */
	public String getTodayDate() {
		YFCDate date = new YFCDate();
        YFCTimeZone oTz = new YFCTimeZone(YFCLocale.getDefaultLocale(), date);
        oTz.adjustToTimeZone(TimeZone.getTimeZone(wcContext.getSCUIContext().getUserPreferences().getLocale().getTimezone()), date);
        String dateString = date.getString(wcContext.getSCUIContext().getUserPreferences().getLocale().getDateFormat());
        return dateString;
	}

	/**
	 * @return the endTodayDate
	 */
	public String getEndTodayDate() {
		return getTodayDate();
	}

	/**
	 * @return the startTodayDate
	 */
	public String getStartTodayDate() {
		return getTodayDate();
	}
	
	public XPEDXWCUtils getXPEDXWCUtils()
    {
        if(xpedxWCUtilsBean == null)
        	xpedxWCUtilsBean = new XPEDXWCUtils();
        return xpedxWCUtilsBean;
    }

	public List getCustArticleSearchName() {
		return custArticleSearchName;
	}

	public void setCustArticleSearchName(List custArticleSearchName) {
		this.custArticleSearchName = custArticleSearchName;
	}

	public List getCustArticleSearchValue() {
		return custArticleSearchValue;
	}

	public void setCustArticleSearchValue(List custArticleSearchValue) {
		this.custArticleSearchValue = custArticleSearchValue;
	}
	
}
