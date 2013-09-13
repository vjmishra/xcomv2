/**
 * This class contains all the utility methods for XPEDX
 */
package com.sterlingcommerce.xpedx.webchannel.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.ui.web.framework.SCUIConstants;
import com.sterlingcommerce.ui.web.framework.SCUILocalSession;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.framework.extensions.ISCUITransactionContext;
import com.sterlingcommerce.ui.web.framework.helpers.SCUITransactionContextHelper;
import com.sterlingcommerce.ui.web.framework.utils.SCUIUtils;
import com.sterlingcommerce.ui.web.platform.transaction.SCUITransactionContextFactory;
import com.sterlingcommerce.ui.web.platform.utils.SCUIPlatformUtils;
import com.sterlingcommerce.webchannel.core.CommonCodeDescriptionType;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.IWCContextBuilder;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.core.context.WCContext;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer;
import com.sterlingcommerce.webchannel.utilities.CommonCodeUtil;
import com.sterlingcommerce.webchannel.utilities.UserPreferenceUtil;
import com.sterlingcommerce.webchannel.utilities.UtilBean;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.webchannel.utilities.YfsUtils;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXCustomerContactInfoBean;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXShipToComparator;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrgNodeDetailsBean;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.profile.org.XPEDXOverriddenShipToAddress;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.xpedx.nextgen.common.util.XPXTranslationUtilsAPI;
import com.yantra.interop.client.ClientVersionSupport;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.util.YFCUtils;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.date.YDate;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNode;
import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfc.util.YFCConfigurator;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;

/**
 * @author rugrani
 * @author mvummadi
 * 
 */
public class XPEDXWCUtils {
	public static String LOGGED_IN_CUSTOMER_ID = "loggedInCustomerID";
	public static String LOGGED_IN_CUSTOMER_KEY = "loggedInCustomerKey";
	public static String MY_SELF = "MySelf";
	public static final String XPEDX_ASSIGNED_CUSTOMER_BILL_TO = "BILL_TO";
	public static final String XPEDX_ASSIGNED_CUSTOMER_SHIP_TO = "SHIP_TO";
	public static final String XPEDX_ASSIGNED_CUSTOMER_SAP = "SAP";
	public static final String XPEDX_ASSIGNED_CUSTOMER_MSAP = "MSAP";
	public static final String XPEDX_ASSIGNED_CUSTOMER = "ASSIGNED";
	public static final String XPEDX_SHIP_TO_ADDRESS_OVERIDDEN = "ShipToAddressOveridden";
	public static final String Immedate_image = "../swc/images/theme/green/Available.png";
	public static final String Next_image = "../swc/images/theme/green/Shippable.png";
	public static final String Days2_Image = "../swc/images/theme/green/ItemPickUp.png";
	private static final String customerGeneralInformationMashUp = "xpedx-customer-getGeneralInformation";
	private static final String parentCustomerInformationMashUp = "xpedx-customer-getParentCustInformation";
	private static final String getOCIFieldsMashup = "xpedx-customer-getOCIFields";
	private static final String customerShipToInformationMashUp = "xpedx-customer-getCustomerAddressInformation";
	private static final String extnFieldsInformationMashUp = "xpedx-customer-getCustomExtnFieldsInformation";
	public static Map<String, String> uomDescMap = new HashMap<String, String>();
	public static String LOGGED_IN_FORMATTED_CUSTOMER_ID_MAP = "loggedInFormattedCustomerIDMap";
	
	/**
	 * Following parameters are used for encryption / decryption purposes
	 */
	private static String svString = "b2bxp3dx";
	private static String skString = "b2b1nt3rnat1onal";
	private static byte[] sharedvector = svString.getBytes();
	private static byte[] sharedkey = skString.getBytes();	
	private static SecretKey sk = getSecretKey(sharedkey);	
	private static byte[] sharedkeyfinal = sk.getEncoded();
	private static String staticFileLocation = null;
	private static String xpedxBuildKey =null;

	private final static Logger log = Logger.getLogger(XPEDXWCUtils.class);

	static {
		staticFileLocation = YFSSystem.getProperty("remote.static.location");
		staticFileLocation = staticFileLocation != null ? staticFileLocation.trim() : "/swc";
	}
	
	static {
		xpedxBuildKey = YFSSystem.getProperty("xpedxBuildKey");
		xpedxBuildKey = xpedxBuildKey != null ? xpedxBuildKey.trim() : "";
	}

	public static String getImage(String DaysType) {
		if (DaysType.equals("Immediate")) {
			return Immedate_image;
		}
		if (DaysType.equals("NextDay")) {
			return Next_image;
		}
		if (DaysType.equals("TwoPlusDays")) {
			return Days2_Image;
		}
		return "";
	}
	
	/**
	 * This is wrt JIRA 243
	 * The following method has been modified to accommodate mash up information coming in during the call.
	 * If there is no mashup information, the default - xpedx-customer-getGeneralInformation is invoked.
	 * 
	 * @param customerId
	 * @param orgCode
	 * @return
	 * @throws CannotBuildInputException
	 */
	
	public static Document getCustomerDocument(String customerID, String storeFrontID) {
		Document outputDoc1 = null;
		IWCContext wcContext = WCContextHelper.getWCContext(ServletActionContext.getRequest());
		Map<String, String> valueMap1 = new HashMap<String, String>();
		valueMap1.put("/Customer/@CustomerID", customerID);
		valueMap1.put("/Customer/@OrganizationCode",storeFrontID);
		Element input1;
		try {
			input1 = WCMashupHelper.getMashupInput("getMasterCustomerFields",
					valueMap1, wcContext.getSCUIContext());

			Object obj1 = WCMashupHelper.invokeMashup("getMasterCustomerFields",
					input1, wcContext.getSCUIContext());

			outputDoc1 = ((Element) obj1).getOwnerDocument();

		} catch (CannotBuildInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return outputDoc1;
	}
	
	public static Document getCustomerDetails(String customerId, String orgCode)
	throws CannotBuildInputException {
		return getCustomerDetails(customerId, orgCode, null );
	}
	
	/**
	 * This method was written for JIRA 243.
	 * If the mashup information is not coming then we use the
	 * default mash up to get all the customer details
	 * @param customerId
	 * @param orgCode
	 * @param mashupId
	 * @return
	 * @throws CannotBuildInputException
	 */
	public static Document getCustomerDetails(String customerId, String orgCode, String mashupId)
			throws CannotBuildInputException {
		Document outputDoc = null;
		if(YFCCommon.isStringVoid(mashupId)){
			mashupId = customerGeneralInformationMashUp;// this one fetches all the customer details
		}
		if (null == customerId || customerId.length() <= 0) {
			log
					.debug("getCustomerDetails: customerId is a required field. Returning an empty document");
			return outputDoc;
		} else if (null == orgCode || orgCode.length() <= 0) {
			log
					.debug("getCustomerDetails: orgCode is a required field. Returning an empty document");
			return outputDoc;
		}
		
		IWCContext wcContext = WCContextHelper
				.getWCContext(ServletActionContext.getRequest());
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/Customer/@CustomerID", customerId);
		valueMap.put("/Customer/@OrganizationCode", orgCode);

		Element input = WCMashupHelper.getMashupInput(
				mashupId, valueMap, wcContext
						.getSCUIContext());
		String inputXml = SCXmlUtil.getString(input);
		
		log.debug("Input XML: " + inputXml);
		
		Object obj = WCMashupHelper.invokeMashup(
				mashupId, input, wcContext
						.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		if (null != outputDoc) {
			log.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
		}
		return outputDoc;

	}

	public static Document getOrganizationDetails(String orgCode)
			throws CannotBuildInputException {
		Document outputDoc = null;

		if (null == orgCode || orgCode.length() <= 0) {
			log
					.debug("getCustomerDetails: orgCode is a required field. Returning an empty document");
			return outputDoc;
		}
		IWCContext wcContext = WCContextHelper
				.getWCContext(ServletActionContext.getRequest());
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/Organization/@OrganizationCode", orgCode);

		Element input = WCMashupHelper.getMashupInput(
				"XPEDXGetOrganizationDetails", valueMap, wcContext
						.getSCUIContext());
		String inputXml = SCXmlUtil.getString(input);
		log.debug("Input XML: " + inputXml);
		Object obj = WCMashupHelper.invokeMashup("XPEDXGetOrganizationDetails",
				input, wcContext.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		if (null != outputDoc) {
			log.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
		}

		return outputDoc;

	}
	   public static String setCustomerAccess(String currentCustomerID, IWCContext wcContext){
	        String isChildCustomer = "FALSE";
	        Element loggedinCustomer =null;
	        if(log.isDebugEnabled())
	        {
	            log.debug("Class:GetCustomerOrganizationAction Method:setCustomerAccess STARTS");
	        } 
	        
	            String wCCustomerId=getLoggedInCustomerFromSession(wcContext);
	            
	            if(wCCustomerId == null) {
	            	wCCustomerId = wcContext.getCustomerId();
	            }
	        
	            String wCOrganizationCode=wcContext.getCustomerMstrOrg();
	            
	            isChildCustomer = "FALSE";
	        
	        try {
	            if(currentCustomerID.equals(wCCustomerId)){
	                isChildCustomer = "TRUE";
	                return isChildCustomer;
	            }
	            List children = new ArrayList();
	            Map<String,String> valueMapinput = new HashMap<String,String>();
	            valueMapinput.put("/Customer/@CustomerID", wCCustomerId);
	            valueMapinput.put("/Customer/@OrganizationCode", wCOrganizationCode);
	            Element input = null;
	            input = WCMashupHelper.getMashupInput("getLoggedInCustomer" , valueMapinput, wcContext);
	            loggedinCustomer = (Element)WCMashupHelper.invokeMashup("getLoggedInCustomer", input, wcContext.getSCUIContext());
	            Element custList = null;
	            String rootCustomerKey = SCXmlUtils.getAttribute((Element)loggedinCustomer,"RootCustomerKey");
	            String customerKey = SCXmlUtils.getAttribute((Element)loggedinCustomer,"CustomerKey");
	            valueMapinput = new HashMap<String,String>();
	            valueMapinput.put("/Customer/@RootCustomerKey", rootCustomerKey);
	            valueMapinput.put("/Customer/@OrganizationCode", wcContext.getCustomerMstrOrg());
	            input = null;
	            input = WCMashupHelper.getMashupInput("cust-getChildCustomerList" , valueMapinput, wcContext);
	            custList = (Element)WCMashupHelper.invokeMashup("cust-getChildCustomerList", input, wcContext.getSCUIContext());
	            populateChildrenList(custList,children, wcContext, customerKey);
	            if(children.contains(currentCustomerID)){
	                isChildCustomer = "TRUE";
	            }
	        } catch (XMLExceptionWrapper mashupEx) {//TODO handle exception
	            wcContext.getSCUIContext().replaceAttribute(SCUIConstants.EXCEPTION_ATTR_NAME, mashupEx);
	            mashupEx.printStackTrace();
	             
	        } catch (CannotBuildInputException mashupEx) {
	            wcContext.getSCUIContext().replaceAttribute(SCUIConstants.EXCEPTION_ATTR_NAME, mashupEx);
	            mashupEx.printStackTrace();
	             
	        } catch (Exception mashupEx) {
	            wcContext.getSCUIContext().replaceAttribute(SCUIConstants.EXCEPTION_ATTR_NAME, mashupEx);
	            mashupEx.printStackTrace();
	             
	        } 
	        if(log.isDebugEnabled())
	        {
	                log.debug("Class:GetCustomerOrganizationAction Method:setCustomerAccess ENDS");
	        }
	        
	        return isChildCustomer;
	    }  
	   
	   private static void populateChildrenList(Element customerElemList, List chidren, IWCContext wcContext, String parentCustomerKey){
	        try {
	            List<Element> customerList=SCXmlUtils.getElementsByAttribute(customerElemList, "/Customer", "ParentCustomerKey", parentCustomerKey);
	             
	            if( null != customerList && customerList.size() > 0)
	            {
	               Iterator itr = customerList.iterator();
	               while(itr.hasNext()) {
	                
	                   Element childCust=(Element)itr.next();
	                   chidren.add(childCust.getAttribute("CustomerID"));
	                   String parentCustomerKeyForChild = childCust.getAttribute("CustomerKey");
	                   populateChildrenList(customerElemList,chidren, wcContext,parentCustomerKeyForChild );
	               }
	           }
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
	public static Document getAllItemList(String customerId) throws Exception {
		Document outputDoc = null;
		if (null == customerId || customerId.length() <= 0) {
			log
					.debug("getCustomerDetails: customerId is a required field. Returning an empty document");
			return outputDoc;
		}

		IWCContext wcContext = WCContextHelper
				.getWCContext(ServletActionContext.getRequest());
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/XPEDXMyItemsList/@CustomerID", customerId);

		Element input = WCMashupHelper.getMashupInput("XPEDXMyItemsList",
				valueMap, wcContext.getSCUIContext());
		String inputXml = SCXmlUtil.getString(input);
		log.debug("Input XML: " + inputXml);
		Object obj = WCMashupHelper.invokeMashup("XPEDXMyItemsList", input,
				wcContext.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		if (null != outputDoc) {
			log.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
		}

		return outputDoc;
	}
	
	public static Document AddItemsToList(String listKey, List<String> itemIds,
			List<String> itemNames, List<String> itemDescs, List itemQtys,
			List itemUOMs, List itemJobIds, List itemTypes,  List itemOrders,String modifiedDate) throws Exception {
		return AddItemsToList(listKey,itemIds,
				itemNames,itemDescs,itemQtys,
				itemUOMs,itemJobIds,itemTypes,itemOrders, modifiedDate);
		
	}

	public static Document AddItemsToList(String listKey, List<String> itemIds,
			List<String> itemNames, List<String> itemDescs, List itemQtys,
			List itemUOMs, List itemJobIds, List itemTypes,  List itemOrders,List itemPONumbers, String modifiedDate) throws Exception {
		Document outputDoc = null;
		if (null == listKey || listKey.length() <= 0) {
			log
					.debug("AddItemsToList: ListKey is a required field. Returning an empty document");
			return outputDoc;
		}

		IWCContext wcContext = WCContextHelper
				.getWCContext(ServletActionContext.getRequest());

		for (int i = 0; i < itemIds.size(); i++) {
			Map<String, String> valueMap = new HashMap<String, String>();
			valueMap.put("/XPEDXMyItemsItems/@MyItemsListKey", listKey);
			valueMap.put("/XPEDXMyItemsItems/@ItemId", itemIds.get(i));

			String name = itemNames.get(i);
			String desc = itemDescs.get(i);
			String qty = (String) itemQtys.get(i);
			String JobId = (String) itemJobIds.get(i);
			String ItemType = (String) itemTypes.get(i);
			String uom = (String) itemUOMs.get(i);
			String order = (String) itemOrders.get(i);
			String itemPONumber="";
			if(itemPONumbers != null)
			{
				itemPONumber=(String) itemPONumbers.get(i);
			}

			if (name != null && name.trim().length() > 0)
				valueMap.put("/XPEDXMyItemsItems/@Name", name);
			if (desc != null && desc.trim().length() > 0)
				valueMap.put("/XPEDXMyItemsItems/@Desc", desc);
			if (qty != null && qty.trim().length() > 0)
				valueMap.put("/XPEDXMyItemsItems/@Qty", qty);
			if (JobId != null && JobId.trim().length() > 0)
				valueMap.put("/XPEDXMyItemsItems/@JobId", JobId);
			if (ItemType != null && ItemType.trim().length() > 0)
				valueMap.put("/XPEDXMyItemsItems/@ItemType", ItemType);
			if (uom != null && uom.trim().length() > 0)
				valueMap.put("/XPEDXMyItemsItems/@UomId", uom);
			if (order != null && order.trim().length() > 0)
				valueMap.put("/XPEDXMyItemsItems/@ItemOrder", order);
			if (itemPONumber != null && order.trim().length() > 0)
				valueMap.put("/XPEDXMyItemsItems/@ItemPoNumber", itemPONumber);
			if (modifiedDate != null)
				valueMap.put("/XPEDXMyItemsItems/XPEDXMyItemsList/@Modifyts", modifiedDate);
			//added for jira 4134
			String isSalesRep = (String) wcContext.getSCUIContext().getSession().getAttribute("IS_SALES_REP");
			if(isSalesRep!=null && isSalesRep.equalsIgnoreCase("true")){
				String salesreploggedInUserName = (String)wcContext.getSCUIContext().getSession().getAttribute("loggedInUserName");
				//valueMap.put("/XPEDXMyItemsItems/XPEDXMyItemsList/@Createusername", salesreploggedInUserName);
				valueMap.put("/XPEDXMyItemsItems/XPEDXMyItemsList/@ModifyUserName", salesreploggedInUserName);//jira 4221
			}else{
				String loggedInUserName= wcContext.getLoggedInUserName();
				//valueMap.put("/XPEDXMyItemsItems/XPEDXMyItemsList/@Createusername", loggedInUserName);
				valueMap.put("/XPEDXMyItemsItems/XPEDXMyItemsList/@ModifyUserName", loggedInUserName);//jira 4221
			}
			//end of jira 4134
			Element input = WCMashupHelper.getMashupInput("XPEDXMyItemsDetailsCreate", valueMap, wcContext.getSCUIContext());
			String inputXml = SCXmlUtil.getString(input);
			log.debug("Input XML: " + inputXml);
			Object obj = WCMashupHelper.invokeMashup(
					"XPEDXMyItemsDetailsCreate", input, wcContext
							.getSCUIContext());
			outputDoc = ((Element) obj).getOwnerDocument();
			if (null != outputDoc) {
				log.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
			}

		}

		return outputDoc;
	}

	public static List<String> getAssignedCustomers(String customerContactId) {
		ISCUITransactionContext scuiTransactionContext = null;
		List<String> listOfAssignedCustomers = new ArrayList<String>();
		SCUIContext wSCUIContext = null;
		try {
			YFCDocument inputDocument = YFCDocument
					.createDocument("CustomerAssignment");
			YFCElement documentElement = inputDocument.getDocumentElement();

			IWCContext context = WCContextHelper
					.getWCContext(ServletActionContext.getRequest());
			wSCUIContext = context.getSCUIContext();

			if (customerContactId != null
					&& customerContactId.trim().length() > 0) {
				documentElement.setAttribute("UserId", customerContactId);
			} else {
				documentElement.setAttribute("UserId", context
						.getLoggedInUserId());
			}
			documentElement.setAttribute("OrganizationCode", context
					.getBuyerOrgCode());
			YFCDocument template = YFCDocument
					.getDocumentFor("<CustomerAssignmentList>"
							+ "<CustomerAssignment><Customer>"
							+"<ParentCustomer/>"
							+"<Extn/>"
							+"</Customer>"
							+ "</CustomerAssignment>"
							+ "</CustomerAssignmentList>");
			scuiTransactionContext = wSCUIContext.getTransactionContext(true);
			YFCElement yfcElement = SCUIPlatformUtils.invokeXAPI(
					"getCustomerAssignmentList", inputDocument
							.getDocumentElement(), template
							.getDocumentElement(), wSCUIContext);
			YFCIterable<YFCElement> iteartor = yfcElement.getChildren();
			YFCElement wElement = null;
			while (iteartor.hasNext()) {
				wElement = iteartor.next();
				wElement = wElement.getFirstChildElement();
				String customer = wElement.getAttribute("CustomerID");
				if (!listOfAssignedCustomers.contains(customer)) {
					listOfAssignedCustomers.add(customer);
				}
			}
		} catch (Exception ex) {
			log.error(ex.getMessage());
			scuiTransactionContext.rollback();
		} finally {
			if (scuiTransactionContext != null) {
				SCUITransactionContextHelper.releaseTransactionContext(
						scuiTransactionContext, wSCUIContext);
			}
		}
		return listOfAssignedCustomers;
	}
	
	

	//method added: Catalog price to display flag and reports display flag
	public static void setShowAddToCartAvaiablityAndReportsFlag(IWCContext wcContext) {
		XPEDXCustomerContactInfoBean xpedxCustomerContactInfoBean = (XPEDXCustomerContactInfoBean)getObjectFromCache(XPEDXConstants.XPEDX_Customer_Contact_Info_Bean);
		String viewPricesFlag = xpedxCustomerContactInfoBean.getExtnViewPricesFlag();
		String viewReportsFlag = xpedxCustomerContactInfoBean.getExtnViewReportsFlag();
//		String viewPricesFlag =(String) wcContext.getSCUIContext().getSession().getAttribute("viewPricesFlag");
//		String viewReportsFlag = (String) wcContext.getSCUIContext().getSession().getAttribute("viewReportsFlag");
		if((null == viewPricesFlag) || (null == viewReportsFlag))
		{
			Document contactDoc = XPEDXWCUtils.getCustomerContactDetails();
			String loggedInCustomerID = XPEDXWCUtils.getLoggedInCustomerFromSession(wcContext);
			if(!(loggedInCustomerID!=null && loggedInCustomerID.trim().length()>0))
				loggedInCustomerID = wcContext.getCustomerId();
			try { 
				ArrayList<Element> contacts = SCXmlUtil.getElementsByAttribute(contactDoc.getDocumentElement(), "/CustomerContact","CustomerContactID", wcContext.getCustomerContactId());
				for(int i=0;i<contacts.size();i++) {
					Element contactElem = contacts.get(i);
					Element customerElem = SCXmlUtil.getChildElement(contactElem, "Customer");
					String customerId = customerElem.getAttribute("CustomerID");
					if(customerId!=null && customerId.equalsIgnoreCase(loggedInCustomerID)) {
						Element contactExtnElem = SCXmlUtil.getChildElement(contactElem, "Extn");
						viewPricesFlag = SCXmlUtil.getAttribute(contactExtnElem, "ExtnViewPricesFlag");
						viewReportsFlag = SCXmlUtil.getAttribute(contactExtnElem, "ExtnViewReportsFlag");
					}
					if(null != viewPricesFlag && viewPricesFlag.trim().length()>0){
							wcContext.getSCUIContext().getSession().setAttribute(
									"viewPricesFlag", viewPricesFlag);
					}
					if(null != viewReportsFlag && viewReportsFlag.trim().length()>0){
						wcContext.getSCUIContext().getSession().setAttribute(
								"viewReportsFlag", viewReportsFlag);
					}
				}
			} catch (Exception e) {
				log.error("Error in getting Single contact : ", e);
			}
			if(log.isDebugEnabled()){
			log.debug(" loggedInCustomerID : " + loggedInCustomerID + " viewPriceFlag : " + viewPricesFlag + " and viewReportsFlag : " + viewReportsFlag);
			}
		}
	}

	public static void setCurrentCustomerIntoContext(String selectedCustomer,
			IWCContext wcContext) {
		setLoggedInCustomerIntoSession(wcContext, selectedCustomer);
		IWCContextBuilder builder = WCContextHelper.getBuilder(wcContext
				.getSCUIContext().getRequest(), wcContext.getSCUIContext()
				.getResponse());
		builder.setCustomerId(selectedCustomer);
	}

	private static void setLoggedInCustomerIntoSession(IWCContext wcContext,
			String selectedCustomer) {
		if (getLoggedInCustomerFromSession(wcContext) == null) {
			wcContext.getSCUIContext().getSession().setAttribute(
					LOGGED_IN_CUSTOMER_ID, wcContext.getCustomerId());
			HashMap<String, HashMap<String,String>> custInfoMap = getFormattedMasterCustomer(
					wcContext.getCustomerId(), wcContext.getStorefrontId());
			wcContext.getSCUIContext().getSession().setAttribute(
					LOGGED_IN_FORMATTED_CUSTOMER_ID_MAP, custInfoMap);
			//Getting the Master Customer Level Flags and setting them in session
			//Added condition for performance issue
			if (getMSACustomerKeyFromSession(wcContext) == null  && getCustomerSKUFromSession(wcContext) == null)
			{
				HashMap<String, String> valueMap = new HashMap<String, String>();
				
				valueMap.put("/Customer/@CustomerID", wcContext.getCustomerId());
				valueMap.put("/Customer/@OrganizationCode", wcContext.getStorefrontId());
	
				Element input;
				try {
					input = WCMashupHelper.getMashupInput(
							"getMasterCustomerFields", valueMap, wcContext
									.getSCUIContext());
				} catch (CannotBuildInputException e) {
					return;
				}
				Object obj = WCMashupHelper.invokeMashup(
						"getMasterCustomerFields", input, wcContext
								.getSCUIContext());
				Document allMasterCustFields = ((Element) obj).getOwnerDocument();
				
				Element msapCustElem = allMasterCustFields.getDocumentElement();
				String masterCustKey = SCXmlUtil.getAttribute(msapCustElem, "CustomerKey");
				//Setting the MSAP Customer Key in session
				wcContext.setWCAttribute(LOGGED_IN_CUSTOMER_KEY,masterCustKey,WCAttributeScope.LOCAL_SESSION);
			}
		}
	}

	/**
	 * JIRA 243
	 * Modified getCustomerDetails method to consider the mashup to be invoked
	 * so that, we get only the required information - here ParentCustomer fields only.
	 * 
	 * @param selectedCustomer
	 * @param wcContext
	 * @return
	 */
	public static String getParentCustomer(String selectedCustomer,
			IWCContext wcContext) {
		String billToID = null;
		if (selectedCustomer != null && selectedCustomer.trim().length() > 0) {
			try {
				
				Document outputDoc = XPEDXWCUtils.getCustomerDetails(
						selectedCustomer, wcContext.getStorefrontId(), parentCustomerInformationMashUp);
				Element outputElem = outputDoc.getDocumentElement();
				billToID = SCXmlUtil.getXpathAttribute(outputElem,
						"//Customer/ParentCustomer/@CustomerID");
			} catch (Exception ex) {
				log.error(ex.getMessage());
							}
		}
		if(log.isDebugEnabled()){
		log.debug("Returning =" + billToID);
		}
		return billToID;

	}

	public static String getLoggedInCustomerFromSession(IWCContext wcContext) {
		Object loogedInCustomerIDObject = wcContext.getSCUIContext()
				.getSession().getAttribute(LOGGED_IN_CUSTOMER_ID);
		if (loogedInCustomerIDObject != null) {
			return (String) loogedInCustomerIDObject;
		}
		return null;
	}
	
	public static String getMSACustomerKeyFromSession(IWCContext wcContext) {
		Object loogedInCustomerIDObject = wcContext.getWCAttribute(LOGGED_IN_CUSTOMER_KEY);
		if (loogedInCustomerIDObject != null) {
			return (String) loogedInCustomerIDObject;
		}
		return null;
	}

	public static String getCustomerSKUFromSession(IWCContext wcContext) {
		Object loogedInCustomerIDObject = wcContext.getWCAttribute(XPEDXConstants.CUSTOMER_USE_SKU);
		if (loogedInCustomerIDObject != null) {
			return (String) loogedInCustomerIDObject;
		}
		return null;
	}

	public static void resetLoggedInCustomerIntoContext(IWCContext wcContext) {
		IWCContextBuilder builder = WCContextHelper.getBuilder(wcContext
				.getSCUIContext().getRequest(), wcContext.getSCUIContext()
				.getResponse());
		String loggedInCustomer = (String) wcContext.getSCUIContext()
				.getSession().getAttribute(LOGGED_IN_CUSTOMER_ID);
		if (loggedInCustomer != null && loggedInCustomer.trim().length() > 0) {
			builder.setCustomerId(loggedInCustomer);
			wcContext.getSCUIContext().getSession().removeAttribute(
					LOGGED_IN_CUSTOMER_ID);
		}
	}

	public static Document getCustomerContactDetails(){
		return getCustomerContactDetails(null);
	}
	/**
	 * This method fetches the customer contact details.
	 * @return
	 */
	public static Document getCustomerContactDetails(String customerID){
		Document output = null;
		IWCContext wcContext = WCContextHelper.getWCContext(ServletActionContext.getRequest());
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/CustomerContact/Customer/@OrganizationCode",
				wcContext.getStorefrontId());
		valueMap.put("/CustomerContact/@CustomerContactID", wcContext
				.getCustomerContactId());
		if(!YFCCommon.isVoid(customerID))
		{
			valueMap.put("/CustomerContact/Customer/@CustomerID", customerID);
		}
		Element input;
		Object obj;
		try {
			input = WCMashupHelper.getMashupInput(
					"getXpedxCustomerContactDetails", valueMap, wcContext
							.getSCUIContext());
			obj = WCMashupHelper.invokeMashup(
					"getXpedxCustomerContactDetails", input, wcContext
							.getSCUIContext());
			
			if(!YFCCommon.isVoid(obj)){
				output = ((Element) obj).getOwnerDocument();
			}
		} catch (CannotBuildInputException e) {
			log.error(e.getMessage());
		}
		return output;
	}
	
	public static String getDefaultShipTo() {
		String defaultAssignedShipTo = null;
		Document outputDoc = null;
		IWCContext wcContext = WCContextHelper.getWCContext(ServletActionContext.getRequest());
		if(!wcContext.isGuestUser()) {
			if (wcContext.getCustomerContactId() != null
					&& wcContext.getCustomerContactId().trim().length() > 0
					&& wcContext.getStorefrontId() != null
					&& wcContext.getStorefrontId().trim().length() > 0) {
				try {
					Map<String, String> valueMap = new HashMap<String, String>();
					valueMap.put("/CustomerContact/Customer/@OrganizationCode",
							wcContext.getStorefrontId());
					valueMap.put("/CustomerContact/@CustomerContactID", wcContext
							.getCustomerContactId());
					Element input = WCMashupHelper.getMashupInput(
							"getXpedxCustomerContactDetails", valueMap, wcContext
									.getSCUIContext());
					Object obj = WCMashupHelper.invokeMashup(
							"getXpedxCustomerContactDetails", input, wcContext
									.getSCUIContext());
					outputDoc = ((Element) obj).getOwnerDocument();
					Element wElement = outputDoc.getDocumentElement();
					wElement = SCXmlUtil.getChildElement(wElement, "CustomerContact");
					wElement = SCXmlUtil.getChildElement(wElement, "Extn");
					defaultAssignedShipTo = SCXmlUtil.getAttribute(wElement,
							"ExtnDefaultShipTo");
					if (defaultAssignedShipTo != null
							&& defaultAssignedShipTo.trim().length() == 0) {
						defaultAssignedShipTo = null;
					}
					log
							.debug("*********Default shipto assigned for this user***************="
									+ defaultAssignedShipTo);
				} catch (Exception ex) {
					log.error(ex.getMessage());
				}
			}
		}
		return defaultAssignedShipTo;
	}
	
	public static void setSampleRequestFlag(String billToCustomerId, IWCContext wcContext ) throws CannotBuildInputException
	{
		Document output = XPEDXWCUtils.
		getCustomerDetails(billToCustomerId, wcContext.getStorefrontId(), customerGeneralInformationMashUp);
		Element outputDocNew = output.getDocumentElement();
		Element extnElem = SCXmlUtil.getChildElement(outputDocNew, "Extn");
		String custShowSampleRequestFlag = SCXmlUtil.getAttribute(extnElem, "ExtnSampleRequestFlag");
		String ExtnServiceOptCode = SCXmlUtil.getAttribute(extnElem, "ExtnServiceOptCode");
		setSampleRequestFlag(custShowSampleRequestFlag,ExtnServiceOptCode,wcContext);
	}
	public static void setSampleRequestFlag(String custShowSampleRequestFlag, String ExtnServiceOptCode, IWCContext wcContext ) throws CannotBuildInputException
	{

		if (null != custShowSampleRequestFlag && (custShowSampleRequestFlag.equalsIgnoreCase("T") 
				|| custShowSampleRequestFlag.equalsIgnoreCase("Y"))) 
		{
			wcContext.getSCUIContext().getSession().setAttribute("showSampleRequest","Y");
		}
		else if (custShowSampleRequestFlag == null || custShowSampleRequestFlag.trim().length() == 0 || "".equals(custShowSampleRequestFlag.trim())) 
		{
			if (ExtnServiceOptCode != null
					&& !ExtnServiceOptCode.trim().equals("")) {
				if (ExtnServiceOptCode.equals("K")
						|| ExtnServiceOptCode.equals("P")) {
					wcContext.getSCUIContext().getSession().setAttribute("showSampleRequest","Y");
				}else
				{
					wcContext.getSCUIContext().getSession().setAttribute("showSampleRequest","N");
				}
			}
			else
			{
				wcContext.getSCUIContext().getSession().setAttribute("showSampleRequest","N");
			}
		}
		else
		{
			wcContext.getSCUIContext().getSession().setAttribute("showSampleRequest","N");
		}
	}

	
	public static String getDefaultShipTo(String customerContactId) {
		if(customerContactId!= null && customerContactId.trim().length()>0) {
			String defaultAssignedShipTo = null;
			Document outputDoc = null;
			try {
	
				IWCContext wcContext = WCContextHelper
						.getWCContext(ServletActionContext.getRequest());
				Map<String, String> valueMap = new HashMap<String, String>();
				valueMap.put("/CustomerContact/Customer/@OrganizationCode",
						wcContext.getStorefrontId());
				valueMap.put("/CustomerContact/@CustomerContactID",customerContactId);
				Element input = WCMashupHelper.getMashupInput(
						"getXpedxCustomerContactDetails", valueMap, wcContext
								.getSCUIContext());
				Object obj = WCMashupHelper.invokeMashup(
						"getXpedxCustomerContactDetails", input, wcContext
								.getSCUIContext());
				outputDoc = ((Element) obj).getOwnerDocument();
				Element wElement = outputDoc.getDocumentElement();
				wElement = SCXmlUtil.getChildElement(wElement, "CustomerContact");
				wElement = SCXmlUtil.getChildElement(wElement, "Extn");
				defaultAssignedShipTo = SCXmlUtil.getAttribute(wElement,
						"ExtnDefaultShipTo");
				if (defaultAssignedShipTo != null
						&& defaultAssignedShipTo.trim().length() == 0) {
					defaultAssignedShipTo = null;
				}
				log
						.debug("*********Default shipto assigned for this user***************="
								+ defaultAssignedShipTo);
			} catch (Exception ex) {
				log.error(ex.getMessage());
			}
			return defaultAssignedShipTo;
		}
		else {
			return getDefaultShipTo();
		}
	}

	public static String getB2BViewFromDB() {
		String b2bViewFromDB = null;
		Document outputDoc = null;
		try {
			IWCContext wcContext = WCContextHelper
					.getWCContext(ServletActionContext.getRequest());
			if(wcContext.isGuestUser()){				
				return "normal-view";
			}
			Map<String, String> valueMap = new HashMap<String, String>();
			valueMap.put("/CustomerContact/Customer/@OrganizationCode",
					wcContext.getStorefrontId());
			valueMap.put("/CustomerContact/@CustomerContactID", wcContext
					.getCustomerContactId());
			String masterCustomer =  (String)wcContext.getSCUIContext()
			.getSession().getAttribute(LOGGED_IN_CUSTOMER_ID);
			valueMap.put("/CustomerContact/Customer/@CustomerID", masterCustomer);
			
			Element input = WCMashupHelper.getMashupInput(
					"getXpedxCustomerContactDetails", valueMap, wcContext
							.getSCUIContext());
			Object obj = WCMashupHelper.invokeMashup(
					"getXpedxCustomerContactDetails", input, wcContext
							.getSCUIContext());
			outputDoc = ((Element) obj).getOwnerDocument();
			Element wElement = outputDoc.getDocumentElement();
			wElement = SCXmlUtil.getChildElement(wElement, "CustomerContact");
			wElement = SCXmlUtil.getChildElement(wElement, "Extn");
			b2bViewFromDB = SCXmlUtil.getAttribute(wElement,
					"ExtnB2BCatalogView");
			if (b2bViewFromDB != null && b2bViewFromDB.trim().length() > 0) {
				b2bViewFromDB = b2bViewFromDB.trim();
				int _b2bViewFromDB = Integer.parseInt(b2bViewFromDB);
				if (_b2bViewFromDB == XPEDXConstants.XPEDX_B2B_FULL_VIEW) {
					b2bViewFromDB = "normal-view";
				} else if (_b2bViewFromDB == XPEDXConstants.XPEDX_B2B_CONDENCED_VIEW) {
					b2bViewFromDB = "condensed-view";
				} else if (_b2bViewFromDB == XPEDXConstants.XPEDX_B2B_MINI_VIEW) {
					b2bViewFromDB = "mini-view";
				} else {
					b2bViewFromDB = "papergrid-view";
				}
			} else {
				b2bViewFromDB = "papergrid-view";
			}
			log.debug("B2BViewFromDB=" + b2bViewFromDB);
		} catch (Exception ex) {
			b2bViewFromDB = "papergrid-view";
			log.error(ex.getMessage());
		}
		return b2bViewFromDB;
	}

	/**
	 * JIRA: 243
	 * Separate mash up was written to get only the CustomerAdditionalAddressList
	 * of the the customer. 
	 * Prior to the fix, entire customer details was being fetched.
	 *
	 */
	public static XPEDXShipToCustomer getShipToAdress(String customerId,
			String organizationCode) throws CannotBuildInputException {
		XPEDXShipToCustomer defualtShipToAssigned = new XPEDXShipToCustomer();
		if(customerId == null || organizationCode == null || !(customerId.trim().length()>0) || !(organizationCode.trim().length()>0))
			return null;
		Document document = getCustomerDetails(customerId, organizationCode, customerShipToInformationMashUp);
		defualtShipToAssigned.setCustomerID(SCXmlUtil.getAttribute(document
				.getDocumentElement(), "CustomerID"));
		log.debug(SCXmlUtil.getString(document));
		Element buyerOrgElement = SCXmlUtil.getChildElement(document
				.getDocumentElement(), "BuyerOrganization");
		defualtShipToAssigned.setOrganizationName(SCXmlUtil.getAttribute(buyerOrgElement,
				"OrganizationName"));
		Element element = SCXmlUtil.getChildElement(document
				.getDocumentElement(), "CustomerAdditionalAddressList");
		Element extnElem = SCXmlUtil.getChildElement(document
				.getDocumentElement(), "Extn");
		element = SCXmlUtil.getChildElement(element,
				"CustomerAdditionalAddress");
		element = SCXmlUtil.getChildElement(element, "PersonInfo");
		defualtShipToAssigned.setFirstName(SCXmlUtil.getAttribute(element,
				"FirstName"));
		defualtShipToAssigned.setMiddleName(SCXmlUtil.getAttribute(element,
				"MiddleName"));
		log.debug("************MiddleName************="
				+ SCXmlUtil.getAttribute(element, "MiddleName"));
		defualtShipToAssigned.setLastName(SCXmlUtil.getAttribute(element,
				"LastName"));
		log.debug("************LastName************="
				+ SCXmlUtil.getAttribute(element, "LastName"));
		defualtShipToAssigned.setEMailID(SCXmlUtil.getAttribute(element,
				"EMailID"));
		defualtShipToAssigned.setEMailID(SCXmlUtil.getAttribute(element,
				"EMailID"));
		defualtShipToAssigned.setCity(SCXmlUtil.getAttribute(element, "City"));
			defualtShipToAssigned
				.setState(SCXmlUtil.getAttribute(element, "State"));
		defualtShipToAssigned.setCountry(SCXmlUtil.getAttribute(element,
				"Country"));
		defualtShipToAssigned.setZipCode(SCXmlUtil.getAttribute(element,
				"ZipCode"));
		defualtShipToAssigned.setCompany(SCXmlUtil.getAttribute(element,
				"Company"));
		defualtShipToAssigned.setDayPhone(SCXmlUtil.getAttribute(element,
		"DayPhone"));
		defualtShipToAssigned.setLocationID(SCXmlUtil.getAttribute(extnElem,
		"ExtnCustomerStoreNumber"));
		
		List<String> addressList = new ArrayList<String>();
		for (int index = 0; index < 6; index++) {
			if (SCXmlUtil.getAttribute(element, "AddressLine" + index) != null
					&& SCXmlUtil.getAttribute(element, "AddressLine" + index)
							.trim().length() > 0) {
				addressList.add(SCXmlUtil.getAttribute(element, "AddressLine"
						+ index));
			}
		}
		defualtShipToAssigned.setAddressList(addressList);
		return defualtShipToAssigned;
	}

	public static String getUserTOA() {
		String toaFlag = null;

		Document outputDoc = null;
		try {

			IWCContext wcContext = WCContextHelper
					.getWCContext(ServletActionContext.getRequest());
			Map<String, String> valueMap = new HashMap<String, String>();
			valueMap.put("/CustomerContact/Customer/@OrganizationCode",
					wcContext.getStorefrontId());
			valueMap.put("/CustomerContact/@CustomerContactID", wcContext
					.getCustomerContactId());
			Element input = WCMashupHelper.getMashupInput(
					"getXpedxCustomerContactDetails", valueMap, wcContext
							.getSCUIContext());
			Object obj = WCMashupHelper.invokeMashup(
					"getXpedxCustomerContactDetails", input, wcContext
							.getSCUIContext());
			outputDoc = ((Element) obj).getOwnerDocument();
			Element wElement = outputDoc.getDocumentElement();
			wElement = SCXmlUtil.getChildElement(wElement, "CustomerContact");
			wElement = SCXmlUtil.getChildElement(wElement, "Extn");
			toaFlag = SCXmlUtil.getAttribute(wElement, "ExtnAcceptTAndCFlag");
			if (toaFlag != null && toaFlag.trim().length() == 0) {
				toaFlag = null;
			}
			log.debug("********* terms of access for this user***************="
					+ toaFlag);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}

		return toaFlag;
	}

	public static Element getUserInfo(String conactId) {
		IWCContext wcContext = WCContextHelper
				.getWCContext(ServletActionContext.getRequest());
		return getUserInfo(conactId, wcContext.getStorefrontId());
	}

	public static Element getUserInfo(String conactId, String organizationCode) {
		Document outputDoc = null;
		if(!YFCUtils.isVoid(conactId) && !YFCUtils.isVoid(organizationCode)){
			try {
				IWCContext wcContext = WCContextHelper
						.getWCContext(ServletActionContext.getRequest());
				Map<String, String> valueMap = new HashMap<String, String>();
				valueMap.put("/CustomerContact/Customer/@OrganizationCode",
						organizationCode);
				valueMap.put("/CustomerContact/@CustomerContactID", conactId);
				Element input = WCMashupHelper.getMashupInput(
						"getXpedxCustomerContactDetails", valueMap, wcContext
								.getSCUIContext());
				Object obj = WCMashupHelper.invokeMashup(
						"getXpedxCustomerContactDetails", input, wcContext
								.getSCUIContext());
				outputDoc = ((Element) obj).getOwnerDocument();
				Element wElement = outputDoc.getDocumentElement();
				return wElement;
			} catch (Exception ex) {
				log.error(ex.getMessage());
			}
		}
		return null;
	}
//added for jira 3484
	public static Map<String,Element> getUsersInfoMap(String conactIds, String organizationCode) {
		Document outputDoc = null;
		Map<String,Element> customerContactMap=new HashMap<String,Element>();
		String []custConactIds=conactIds.split(",");
		if(!SCUIUtils.isVoid(conactIds) && !YFCUtils.isVoid(organizationCode)){
			try {
				IWCContext wcContext = WCContextHelper
				.getWCContext(ServletActionContext.getRequest());
				Element input = WCMashupHelper.getMashupInput("getXpedxCustomerContactDetailsList", wcContext);
				Element customerContactElem=(Element)input.getElementsByTagName("Customer").item(0);
				customerContactElem.setAttribute("OrganizationCode", wcContext.getStorefrontId());
				Element complexQuery = SCXmlUtil.getChildElement(input, "ComplexQuery");
				Element OrElem = SCXmlUtil.getChildElement(complexQuery, "Or");
				for(String contactId : custConactIds) {
					Element exp = input.getOwnerDocument().createElement("Exp");
					exp.setAttribute("Name", "CustomerContactID");
					exp.setAttribute("Value", contactId);
					SCXmlUtil.importElement(OrElem, exp);
				}
				Element output =(Element) WCMashupHelper.invokeMashup("getXpedxCustomerContactDetailsList", input, wcContext.getSCUIContext());
				NodeList customerContactList=output.getElementsByTagName("CustomerContact");
				addCustomerContactInToMap(output,customerContactMap);
				return customerContactMap;
			} catch (Exception ex) {
				log.error(ex.getMessage());
			}
		}
		return null;
	}

	private static void addCustomerContactInToMap(Element output,Map<String,Element> customerContactMap)
	{
		NodeList customerContactList=output.getElementsByTagName("CustomerContact");
		for(int i=0;i<customerContactList.getLength();i++)
		{
			Element _customerContactElem=(Element)customerContactList.item(i);
			String customerContactId=_customerContactElem.getAttribute("CustomerContactID");	
			Element _customerContactElemTemp=customerContactMap.get(customerContactId);
			if(_customerContactElemTemp==null)
				customerContactMap.put(customerContactId, _customerContactElem);
			else
			{	
				if(_customerContactElem != null && (_customerContactElem.getAttribute("EmailID") !=null && !_customerContactElem.getAttribute("EmailID").equals(""))
						&& (_customerContactElemTemp.getAttribute("EmailID"))==null ||  _customerContactElemTemp.getAttribute("EmailID").equals(""))
					customerContactMap.put(customerContactId, _customerContactElem);
			}	
		}
	}
	//end of jira 3484
	/*
	 * Takes a Customer ID as input Returns a Map which has two Keys "BILL_TO"
	 * and "SHIP_TO". Query the Map using
	 * XPEDXWCUtils.XPEDX_ASSIGNED_CUSTOMER_BILL_TO and
	 * XPEDXWCUtils.XPEDX_ASSIGNED_CUSTOMER_SHIP_TO. BILL_TO contains arraylist
	 * of BillTo Id's(Recursive) and SHIP_TO contains arraylist of all ShipTo
	 * Id's(Recursive)
	 */
	
	public static Map<String,String> createModifyUserNameMap(List<Element> articleLines)
	{
		IWCContext context = WCContextHelper.getWCContext(ServletActionContext.getRequest());
		UtilBean utilBean=new UtilBean();
		Map<String,String> modifyUserMap=new HashMap<String,String>();
		try
		{
			Element input = WCMashupHelper.getMashupInput("xpedxGetContactUserName", context);
			input.setAttribute("OrganizationCode", context.getStorefrontId());
			Element complexQuery = SCXmlUtil.getChildElement(input, "ComplexQuery");
			Element OrElem = SCXmlUtil.getChildElement(complexQuery, "Or");
			for(Element article : articleLines) {
				Element exp = input.getOwnerDocument().createElement("Exp");
				exp.setAttribute("Name", "CustomerContactID");
				exp.setAttribute("Value", article.getAttribute("Modifyuserid"));
				SCXmlUtil.importElement(OrElem, exp);
			}
			Element output =(Element) WCMashupHelper.invokeMashup("xpedxGetContactUserName", input, context.getSCUIContext());
			
			
			List<Element> customerContactList = utilBean.getElements(output, "//CustomerContactList/CustomerContact");
			for(Element customerContactElem : customerContactList) {
				String userId = customerContactElem.getAttribute("UserID");
				String modifyBy = customerContactElem.getAttribute("CustomerContactID");
				if(!modifyBy.equals(userId))
					continue;
				String lastName = customerContactElem.getAttribute("LastName");
				String firstName = customerContactElem.getAttribute("FirstName");
				String name="";
				if (!YFCCommon.isVoid(lastName)){
					name = lastName;
				}
				if (!YFCCommon.isVoid(firstName)){
					if (!YFCCommon.isVoid(lastName)){
//						name =  " "+name;
						name =  name+ ", ";
					}
//					name = firstName + name ;
					name =  name + firstName;
				}
				modifyUserMap.put(modifyBy, name);
			}
			
		}
		catch(Exception e)
		{
			log.error("Error while getting modify UserName for order!");
		}
		return modifyUserMap;
	}

	
	public static HashMap<String, ArrayList<String>> getAssignedCustomersMap(
			String customerID, String userId) throws CannotBuildInputException {
		HashMap<String, ArrayList<String>> childCustomerMap = new HashMap<String, ArrayList<String>>();
		ArrayList<String> billToList = new ArrayList<String>();
		ArrayList<String> shipToList = new ArrayList<String>();
		ArrayList<String> sapList = new ArrayList<String>();
		ArrayList<String> msapList = new ArrayList<String>();
		Document outputDoc = null;
		if (null == customerID) {
			log
					.debug("getAssignedCustomersMap: customerID is a required field. Returning a empty Map");
			return childCustomerMap;
		}
		outputDoc = getAllChildCustomersDoc(customerID);
		if (null == outputDoc) {
			log
					.debug("getAssignedCustomersMap: No data available for customerID. Returning a empty Map");
			return childCustomerMap;
		}
		ArrayList<Element> bList = SCXmlUtil.getElementsByAttribute(outputDoc
				.getDocumentElement(), "Organization", "CustomerSuffixType",
				"B");
		ArrayList<Element> sList = SCXmlUtil.getElementsByAttribute(outputDoc
				.getDocumentElement(), "Organization", "CustomerSuffixType",
				"S");
		ArrayList<Element> cList = SCXmlUtil.getElementsByAttribute(outputDoc
				.getDocumentElement(), "Organization", "CustomerSuffixType",
				"C");

		// Get the assigned Customer List
		ArrayList<String> assignedCustomerList = (ArrayList<String>) getAssignedCustomers(userId);

		Iterator<Element> bIter = bList.iterator();
		while (bIter.hasNext()) {
			Element orgElement = (Element) bIter.next();
			String billToID = orgElement.getAttribute("CustomerID").trim();
			// Check of the BillTo is assigned to the user, and then add
			if (null != assignedCustomerList
					&& assignedCustomerList.contains(billToID)) {
				billToList.add(billToID);
			}
		}
		Iterator<Element> sIter = sList.iterator();
		while (sIter.hasNext()) {
			Element orgElement = (Element) sIter.next();
			String shipToID = orgElement.getAttribute("CustomerID").trim();
			// Check of the ShipTo is assigned to the user, and then add
			if (null != assignedCustomerList
					&& assignedCustomerList.contains(shipToID)) {
				shipToList.add(shipToID);
			}
		}
		Iterator<Element> cIter = cList.iterator();
		while (cIter.hasNext()) {
			Element orgElement = (Element) cIter.next();
			String sapID = orgElement.getAttribute("CustomerID").trim();
			// Check of the ShipTo is assigned to the user, and then add
			if (null != assignedCustomerList
					&& assignedCustomerList.contains(sapID)) {
				sapList.add(sapID);
			}
		}
		if(assignedCustomerList.contains(customerID.trim()))
			msapList.add(customerID);
		log.debug("In getAssignedCustomersMap***********=" + childCustomerMap);
		childCustomerMap.put(XPEDX_ASSIGNED_CUSTOMER_BILL_TO, billToList);
		childCustomerMap.put(XPEDX_ASSIGNED_CUSTOMER_SHIP_TO, shipToList);
		childCustomerMap.put(XPEDX_ASSIGNED_CUSTOMER_SAP, sapList);
		childCustomerMap.put(XPEDX_ASSIGNED_CUSTOMER_MSAP, msapList);
		childCustomerMap.put(XPEDX_ASSIGNED_CUSTOMER, assignedCustomerList);
		return childCustomerMap;
	}
	
	/*
	 * It returns a hashmap with the following format
	 * {billto1=[shipto1,shipto2], billto2=[shipto5,shipto33]}
	 */
	public static HashMap<String, ArrayList<String>> getAssignedCustomersHashMap(
			String customerID, String userId) throws CannotBuildInputException {
		HashMap<String, ArrayList<String>> childCustomerMap = new HashMap<String, ArrayList<String>>();
		ArrayList<String> billToList = new ArrayList<String>();
		ArrayList<String> shipToList = new ArrayList<String>();
		Document outputDoc = null;
		if (null == customerID) {
			log
					.debug("getAssignedCustomersMap: customerID is a required field. Returning a empty Map");
			return childCustomerMap;
		}
		outputDoc = getAllChildCustomersDoc(customerID);
		if (null == outputDoc) {
			log
					.debug("getAssignedCustomersMap: No data available for customerID. Returning a empty Map");
			return childCustomerMap;
		}
		ArrayList<Element> bList = SCXmlUtil.getElementsByAttribute(outputDoc
				.getDocumentElement(), "Organization", "CustomerSuffixType",
				"B");
		ArrayList<Element> sList = SCXmlUtil.getElementsByAttribute(outputDoc
				.getDocumentElement(), "Organization", "CustomerSuffixType",
				"S");

		// Get the assigned Customer List
		ArrayList<String> assignedCustomerList = (ArrayList<String>) getAssignedCustomers(userId);

		Iterator<Element> bIter = bList.iterator();
		while (bIter.hasNext()) {
			Element orgElement = (Element) bIter.next();
			String billToID = orgElement.getAttribute("CustomerID").trim();
			// Check of the BillTo is assigned to the user, and then add
			if (null != assignedCustomerList
					&& assignedCustomerList.contains(billToID)) {
				childCustomerMap.put(billToID, new ArrayList<String>());
			}
		}
		Iterator<Element> sIter = sList.iterator();
		while (sIter.hasNext()) {
			Element orgElement = (Element) sIter.next();
			String shipToID = orgElement.getAttribute("CustomerID").trim();
			String billToID = orgElement.getAttribute("ParentCustomerID")
					.trim();
			// Check of the ShipTo is assigned to the user, and then add
			if (null != assignedCustomerList
					&& assignedCustomerList.contains(shipToID)) {
				if(childCustomerMap.get(billToID)== null){
					ArrayList<String> listShipTos = new ArrayList<String>();
					listShipTos.add(shipToID);
					childCustomerMap.put(billToID, listShipTos);
				}
				else{
					ArrayList<String> listShipTos = childCustomerMap.get(billToID);
					listShipTos.add(shipToID);
				}
				
			}
		}
		return childCustomerMap;
	}
	
	/*
	 * It returns a hashmap with the following format
	 * {billto1=[shipto1OrgElement,shipto2OrgElement], billto2=[shipto4OrgElement,shipto44OrgElement]}
	 */
	public static HashMap<String, ArrayList<Element>> getAssignedCustomerElementHashMap(
			String customerID, String userId) throws CannotBuildInputException {
		HashMap<String, ArrayList<Element>> childCustomerMap = new HashMap<String, ArrayList<Element>>();
		ArrayList<String> billToList = new ArrayList<String>();
		ArrayList<String> shipToList = new ArrayList<String>();
		Document outputDoc = null;
		if (null == customerID) {
			log
					.debug("getAssignedCustomerElementHashMap: customerID is a required field. Returning a empty Map");
			return childCustomerMap;
		}
		outputDoc = getAllChildCustomersDoc(customerID);
		if (null == outputDoc) {
			log
					.debug("getAssignedCustomerElementHashMap: No data available for customerID. Returning a empty Map");
			return childCustomerMap;
		}
		ArrayList<Element> bList = SCXmlUtil.getElementsByAttribute(outputDoc
				.getDocumentElement(), "Organization", "CustomerSuffixType",
				"B");
		ArrayList<Element> sList = SCXmlUtil.getElementsByAttribute(outputDoc
				.getDocumentElement(), "Organization", "CustomerSuffixType",
				"S");

		// Get the assigned Customer List
		ArrayList<String> assignedCustomerList = (ArrayList<String>) getAssignedCustomers(userId);

		Iterator<Element> bIter = bList.iterator();
		while (bIter.hasNext()) {
			Element orgElement = (Element) bIter.next();
			String billToID = orgElement.getAttribute("CustomerID").trim();
			// Check of the BillTo is assigned to the user, and then add
			if (null != assignedCustomerList
					&& assignedCustomerList.contains(billToID)) {
				childCustomerMap.put(billToID, new ArrayList<Element>());
			}
		}
		Iterator<Element> sIter = sList.iterator();
		while (sIter.hasNext()) {
			Element orgElement = (Element) sIter.next();
			String shipToID = orgElement.getAttribute("CustomerID").trim();
			String billToID = orgElement.getAttribute("ParentCustomerID")
					.trim();
			// Check of the ShipTo is assigned to the user, and then add
			if (null != assignedCustomerList
					&& assignedCustomerList.contains(shipToID)) {
				if(childCustomerMap.get(billToID)== null){
					ArrayList<Element> listShipTos = new ArrayList<Element>();
					listShipTos.add(orgElement);
					childCustomerMap.put(billToID, listShipTos);
				}
				else{
					ArrayList<Element> listShipTos = childCustomerMap.get(billToID);
					listShipTos.add(orgElement);
				}
				
			}
		}
		return childCustomerMap;
	}

	/*
	 * Takes a Customer ID as input Returns a xml document which contains all
	 * the Child Customers (Recursive). The allAssignedCustomerDoc has both
	 * ShipTo's and BillTo's identified by CustomerSuffixType OutputDoc:
	 * <OrganizationList> <Organization CustomerID="" CustomerSuffixType=""
	 * OrganizationCode="" ParentCustomerID=""/> </OrganizationList>
	 */
	public static Document getAllChildCustomersDoc(String customerID)
			throws CannotBuildInputException {
		Document allAssignedCustomerDoc = null;

		if (null == customerID) {
			log
					.debug("getAllChildCustomersDoc: customerID is a required field. Returning a empty Document");
			return allAssignedCustomerDoc;
		}

		IWCContext context = WCContextHelper.getWCContext(ServletActionContext
				.getRequest());
		SCUIContext wSCUIContext = context.getSCUIContext();
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/Customer/@CustomerID", customerID);
		valueMap.put("/Customer/@OrganizationCode", context.getStorefrontId());

		Element input = WCMashupHelper.getMashupInput(
				"xpedxCustomerAssignment", valueMap, wSCUIContext);
		String inputXml = SCXmlUtil.getString(input);
		log.debug("getAllChildCustomersDoc: Input XML: " + inputXml);
		Object obj = WCMashupHelper.invokeMashup("xpedxCustomerAssignment",
				input, wSCUIContext);
		allAssignedCustomerDoc = ((Element) obj).getOwnerDocument();
		if (null != allAssignedCustomerDoc) {
			log.debug("getAllChildCustomersDoc: Output XML: "
					+ SCXmlUtil.getString((Element) obj));
		}
		return allAssignedCustomerDoc;
	}
	
	public static Document getAllChildCustomersDocByView(String customerID) throws CannotBuildInputException {
		Document allAssignedCustomerDoc = null;
		if(customerID == null || customerID.trim().length()<=0) {
			log.debug("getAllChildCustomersDocByView: customerID is a required field. Returning a empty Document");
			return allAssignedCustomerDoc;
		}
		IWCContext context = WCContextHelper.getWCContext(ServletActionContext
				.getRequest());
		SCUIContext wSCUIContext = context.getSCUIContext();
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/Customer/@CustomerID", customerID);
		valueMap.put("/Customer/@OrganizationCode", context.getStorefrontId());

		Element input = WCMashupHelper.getMashupInput(
				"xpedxCustomerAssignment", valueMap, wSCUIContext);
		String inputXml = SCXmlUtil.getString(input);
		log.debug("getAllChildCustomersDoc: Input XML: " + inputXml);
		Object obj = WCMashupHelper.invokeMashup("xpedxCustomerAssignment",
				input, wSCUIContext);
		allAssignedCustomerDoc = ((Element) obj).getOwnerDocument();
		if (null != allAssignedCustomerDoc) {
			log.debug("getAllChildCustomersDoc: Output XML: "
					+ SCXmlUtil.getString((Element) obj));
		}
		return allAssignedCustomerDoc;
	}

	public static List<String> getAllShipTosAsList(IWCContext wcContext)
			throws CannotBuildInputException {
		String loggedInCustomerFromSession = XPEDXWCUtils
				.getLoggedInCustomerFromSession(wcContext);
		HashMap<String, ArrayList<String>> allMap = null;
		Set<String> allAssignedShopTos = new HashSet<String>();
		if (loggedInCustomerFromSession != null
				&& loggedInCustomerFromSession.trim().length() > 0) {
			allMap = getAssignedCustomersMap(loggedInCustomerFromSession,
					wcContext.getLoggedInUserId());
		} else {
			allMap = getAssignedCustomersMap(wcContext.getCustomerId(),
					wcContext.getLoggedInUserId());
		}
		log.debug("All shipTos Map***********=" + allMap);
		List<String> allShipTos = allMap.get(XPEDX_ASSIGNED_CUSTOMER_SHIP_TO);
		log.debug("All shipTos***********=" + allShipTos);
		if (allShipTos == null) {
			allShipTos = new ArrayList<String>();
		}

		List<String> allBillTos = allMap.get(XPEDX_ASSIGNED_CUSTOMER_BILL_TO);
		List<String> allSAPs = allMap.get(XPEDX_ASSIGNED_CUSTOMER_SAP);
		List<String> allMSAPs = allMap.get(XPEDX_ASSIGNED_CUSTOMER_MSAP);
		List<String> allAssignedCustomers = allMap.get(XPEDX_ASSIGNED_CUSTOMER);
		if (allBillTos != null) {
			int size = allBillTos.size();
			for (int index = 0; index < size; index++) {
				String billTo = allBillTos.get(index);
				ArrayList<String> allShipTosFromBillTo = getAllShipTos(billTo);
				allShipTos.addAll(allShipTosFromBillTo);
			}
		}
		if (allSAPs != null) {
			int size = allSAPs.size();
			for (int index = 0; index < size; index++) {
				String sap = allSAPs.get(index);
				ArrayList<String> allShipTosFromSAP = getAllShipTos(sap);
				allShipTos.addAll(allShipTosFromSAP);
			}
		}
		if (allMSAPs != null) {
			int size = allMSAPs.size();
			for (int index = 0; index < size; index++) {
				String msap = allMSAPs.get(index);
				ArrayList<String> allShipTosFromMSAP = getAllShipTos(msap);
				allShipTos.addAll(allShipTosFromMSAP);
			}
		}
		allAssignedShopTos.addAll(allShipTos);
		allShipTos.clear();
		allShipTos.addAll(allAssignedShopTos);

		return allShipTos;
	}
	
	public static ArrayList<String> getAllShipTos(String customerID) throws CannotBuildInputException {
		ArrayList<String> arrayList = new ArrayList<String>();
		ArrayList<Element> customerList = new ArrayList<Element>();
		Map<String, String> valueMap1 = new HashMap<String, String>();
		IWCContext context = WCContextHelper.getWCContext(ServletActionContext
				.getRequest());
		valueMap1.put("/Customer/@CustomerID", customerID);
		valueMap1.put("/Customer/@OrganizationCode", context.getStorefrontId());

		Element input1 = WCMashupHelper.getMashupInput(
				"xpedxGetAllShipTos", valueMap1, context
						.getSCUIContext());
		Object obj1 = WCMashupHelper.invokeMashup(
				"xpedxGetAllShipTos", input1, context
						.getSCUIContext());
		Document outputDoc = ((Element) obj1).getOwnerDocument();
		Element customerListElem = outputDoc.getDocumentElement();
		customerList = SCXmlUtil.getElements(customerListElem, "Customer");
		Iterator<Element> iterator = customerList.iterator();
		while(iterator.hasNext()) {
			Element customer = iterator.next();
			String shipToCustomerID = customer.getAttribute("CustomerID");
			arrayList.add(shipToCustomerID);
		}
		return arrayList;
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> getAllAssignedShipToList(String userId, IWCContext context) {
		ArrayList<String> shipTos = new ArrayList<String>();
		ArrayList<XPEDXShipToCustomer> shipTosAddress = new ArrayList<XPEDXShipToCustomer>();
		HashMap<String, XPEDXShipToCustomer> shipToCustIdAndAddressMap = new HashMap<String, XPEDXShipToCustomer>();
		if(YFCUtils.isVoid(userId) || context==null) {
			log.error("User ID is a mandatory field to get the assigned ship to's.... So returning Empty List");
			return shipTos;
		} else {
			ISCUITransactionContext scuiTransactionContext = context.getSCUIContext().getTransactionContext(true);
			YFSEnvironment env = (YFSEnvironment) scuiTransactionContext
			.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);
			try {
				YIFApi api = YIFClientFactory.getInstance().getApi();
				Document inputDoc =	SCXmlUtil.createDocument("User");
				inputDoc.getDocumentElement().setAttribute("UserId", userId);
				Document flowTemplate = SCXmlUtil.createFromString("<CustomerList><Customer CustomerID='' OrganizationCode=''><Extn ExtnCustomerName=''/>" +
						"<CustomerAdditionalAddressList><CustomerAdditionalAddress><PersonInfo FirstName='' LastName='' MiddleName='' />" +
						"</CustomerAdditionalAddress></CustomerAdditionalAddressList>" +
						"</Customer></CustomerList>");
				env.setApiTemplate("XPXGetListOfAssignedShipTosForAUserService", flowTemplate);
				Document outputDoc = api.executeFlow(env, "XPXGetListOfAssignedShipTosForAUserService", inputDoc);
				if(outputDoc!=null) {
					NodeList assignedShipTos = outputDoc.getDocumentElement().getElementsByTagName("Customer");
					for(int i=0; i<assignedShipTos.getLength();i++) {
						Element shipToCustomer = (Element) assignedShipTos.item(i);
						String customerID = shipToCustomer.getAttribute("CustomerID");
						XPEDXShipToCustomer shipToAddress = getShipToAddressOfCustomer(shipToCustomer);
						shipTos.add(customerID);
						shipToCustIdAndAddressMap.put(customerID, shipToAddress);
					}
				}
				Collections.sort(shipTos, new XPEDXShipToComparator());
				for(int i =0;i<shipTos.size();i++) {
					shipTosAddress.add(shipToCustIdAndAddressMap.get(shipTos.get(i)));
				}
				env.clearApiTemplate("XPXGetListOfAssignedShipTosForAUserService");
				return shipTos;
			} catch (Exception e) {
				env.clearApiTemplate("XPXGetListOfAssignedShipTosForAUserService");
				log.error("Error Getting the Assigned Ship to For the User " + userId);
				e.printStackTrace();
				return shipTos;
			}			
		}
	}

	public static String getCategoryDomainAssetContent(String assetId)
			throws CannotBuildInputException {
		Document outputDoc = null;
		StringBuilder imagePath = new StringBuilder();
		IWCContext wcContext = WCContextHelper
				.getWCContext(ServletActionContext.getRequest());
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/SearchCatalogIndex/@CallingOrganizationCode", wcContext
				.getStorefrontId());

		Element input = WCMashupHelper.getMashupInput("xpedxGetExtnCertImage",
				valueMap, wcContext.getSCUIContext());
		String inputXml = SCXmlUtil.getString(input);
		log.debug("Input XML: " + inputXml);
		Object obj = WCMashupHelper.invokeMashup("xpedxGetExtnCertImage",
				input, wcContext.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		if (null != outputDoc) {
			log.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
		}
		Element categoryDomainElement = SCXmlUtil.getChildElement(outputDoc
				.getDocumentElement(), "CategoryDomain");
		Element assetListElement = SCXmlUtil.getChildElement(
				categoryDomainElement, "AssetList");
		ArrayList<Element> assetList = SCXmlUtil.getElementsByAttribute(
				assetListElement, "Asset", "AssetID", assetId);
		if (null == assetList || assetList.size() != 1) {
			log.error("Unable to get the asset location... returning null");
			return null;
		} else {
			Element assetElement = assetList.get(0);
			String contentLocaltion = assetElement
					.getAttribute("ContentLocation");
			String contentFileName = assetElement.getAttribute("ContentID");
			imagePath.append(contentLocaltion).append("/").append(
					contentFileName);
		}
		return imagePath.toString();
	}

	public static boolean isCustomerSelectedIntoConext(IWCContext wcContext) {
		String loggedInCustomerFromSession = getLoggedInCustomerFromSession(wcContext);
		if (loggedInCustomerFromSession == null
				|| loggedInCustomerFromSession.trim().length() == 0) {
			return false;
		}
		return true;
	}
	
	public static LinkedHashMap getCustomerFieldsMapfromSession(IWCContext wcContext){
		/*HttpServletRequest httpRequest = wcContext.getSCUIContext().getRequest();
        HttpSession localSession = httpRequest.getSession();
        HashMap customerFieldsSessionMap = (HashMap)localSession.getAttribute("customerFieldsSessionMap");*/
		setSAPCustomerExtnFieldsInCache();
		LinkedHashMap customerFieldsSessionMap = (LinkedHashMap)getObjectFromCache("customerFieldsSessionMap");
        return customerFieldsSessionMap;
	}

	public static void changeOrderOwnerToSelectedCustomer(IWCContext wcContext,
			String orderHeaderKey) {
		boolean isCustomerSelectedIntoContext = isCustomerSelectedIntoConext(wcContext);
		if (!isCustomerSelectedIntoContext) {
			return;
		}
		YFSEnvironment env = null;
		ISCUITransactionContext scuiTransactionContext = null;
		SCUIContext wSCUIContext = null;
		try {
			YFCDocument inputDocument = YFCDocument.createDocument("Order");
			YFCElement documentElement = inputDocument.getDocumentElement();
			IWCContext context = WCContextHelper
					.getWCContext(ServletActionContext.getRequest());
			wSCUIContext = context.getSCUIContext();
			 String editedOrderHeaderKey = XPEDXWCUtils.getEditedOrderHeaderKeyFromSession(wcContext);
             if(YFCCommon.isVoid(editedOrderHeaderKey)){
            	 documentElement.setAttribute("DraftOrderFlag", "Y");     
             }


			documentElement.setAttribute("Action", "MODIFY");
			documentElement.setAttribute("OrderHeaderKey", orderHeaderKey);
			documentElement.setAttribute("BillToID", XPEDXWCUtils
					.getLoggedInCustomerFromSession(wcContext));
			documentElement.setAttribute("ShipToID", wcContext.getCustomerId());
			documentElement.setAttribute("BuyerOrganizationCode", wcContext
					.getCustomerId());
			documentElement.setAttribute("CustomerContactID", wcContext
					.getEffectiveUserId());
			documentElement.setAttribute("Override", "Y");
			YFCElement extnDocumentElem=documentElement.getOwnerDocument().createElement("Extn");
			Document customerDetails = XPEDXWCUtils.getCustomerDetails(wcContext.getCustomerId(), wcContext.getStorefrontId(),"xpedx-customer-getCustomerAddressInfo");
			Element extnElement = SCXmlUtil.getChildElement(customerDetails.getDocumentElement(), "Extn");
			String custPrefcategory = SCXmlUtil.getXpathAttribute(customerDetails.getDocumentElement(), "//Customer/ParentCustomer/Extn/@ExtnCustomerClass");
			if(custPrefcategory==null){
			 custPrefcategory = extnElement.getAttribute("ExtnCustomerClass");
			}
			String shipFromDivision = extnElement.getAttribute("ExtnShipFromBranch");
			String customerDivision = extnElement.getAttribute("ExtnCustomerDivision");
			String currencyCode = extnElement.getAttribute("ExtnCurrencyCode");
			String industry = extnElement.getAttribute("ExtnIndustry");
			XPEDXShipToCustomer shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
			shipToCustomer.setExtnCustomerClass(custPrefcategory);
			shipToCustomer.setExtnShipFromBranch(shipFromDivision);
			shipToCustomer.setExtnCustomerDivision(customerDivision);
			shipToCustomer.setExtnCurrencyCode(currencyCode);
			shipToCustomer.setExtnIndustry(industry);
			//Added for EB 289 - to set the ShipTo & BillTo Customer status when the ShipTo is changed in the cart page
			String customerStatus = SCXmlUtil.getXpathAttribute(customerDetails.getDocumentElement(), "//Customer/@Status");
			shipToCustomer.setCustomerStatus(customerStatus);
			String billToCustomerStatus = SCXmlUtil.getXpathAttribute(customerDetails.getDocumentElement(), "//Customer/ParentCustomer/@Status");
			shipToCustomer.getBillTo().setCustomerStatus(billToCustomerStatus);
			//End of EB 289
			
			//Added for JIRA 4306
			shipToCustomer.setOrganizationName(SCXmlUtil.getXpathAttribute(customerDetails.getDocumentElement(),"//Customer/BuyerOrganization/@OrganizationName"));
			//Ended for JIRA 4306
			
			/*wcContext.setWCAttribute(XPEDXConstants.SHIP_FROM_BRANCH,shipFromDivision,WCAttributeScope.LOCAL_SESSION);
			wcContext.setWCAttribute(XPEDXConstants.CUSTOMER_DIVISION,customerDivision,WCAttributeScope.LOCAL_SESSION);
			wcContext.setWCAttribute(XPEDXConstants.INDUSTRY,industry,WCAttributeScope.LOCAL_SESSION);
			wcContext.setWCAttribute(XPEDXConstants.CUSTOMER_CURRENCY_CODE,currencyCode,WCAttributeScope.LOCAL_SESSION);*/
			LinkedHashMap customerFieldsSessionMap = getCustomerFieldsMapfromSession(wcContext);
	        if(null == customerFieldsSessionMap || customerFieldsSessionMap.size() == 0){
	        	LinkedHashMap customerFieldsMap = XPEDXOrderUtils.getCustomerLineFieldMap(customerDetails);
	        	setObectInCache("customerFieldsSessionMap", customerFieldsMap);
	        }
			if(custPrefcategory!=null && custPrefcategory.trim().length()>0) {
				wcContext.getSCUIContext().getSession().setAttribute(XPEDXConstants.CUST_PREF_CATEGORY,custPrefcategory);
			}
			String billToCustomerID = SCXmlUtil.getXpathAttribute(customerDetails.getDocumentElement(), "//Customer/ParentCustomer/@CustomerID");
			extnDocumentElem.setAttribute("ExtnBillToCustomerID", billToCustomerID);
			if(billToCustomerID !=null && billToCustomerID.trim().length()>0) {
				String billToKey;
				Document billToCustomerDetails = XPEDXWCUtils.getCustomerDetails(billToCustomerID, wcContext.getStorefrontId(), "xpedx-customer-getCustomerAddressInfo");
				//Additional change - JIRA 4306
				shipToCustomer.getBillTo().setOrganizationName(SCXmlUtil.getXpathAttribute(billToCustomerDetails.getDocumentElement(),"//Customer/BuyerOrganization/@OrganizationName"));
				//Additional change - JIRA 4306
				Element billToAddressElement = SCXmlUtil.getElementByAttribute(billToCustomerDetails.getDocumentElement(),"CustomerAdditionalAddressList/CustomerAdditionalAddress", "IsDefaultBillTo", "Y");
				if(billToAddressElement==null) {
					billToAddressElement = SCXmlUtil.getXpathElement(billToCustomerDetails.getDocumentElement(), "//Customer/BuyerOrganization/BillingPersonInfo");
					billToKey = SCXmlUtil.getAttribute(billToAddressElement, "PersonInfoKey");
				}
				else {
					billToKey = SCXmlUtil.getXpathAttribute(billToAddressElement, "//CustomerAdditionalAddress/PersonInfo/@PersonInfoKey");
				}
				if(billToKey != null && billToKey.trim().length()>0) {
					documentElement.setAttribute("BillToKey", billToKey);
				}
			}
			setObectInCache(XPEDXConstants.SHIP_TO_CUSTOMER, shipToCustomer);
			if(XPEDXWCUtils.isShipToAddressOveridden(wcContext)) {
				setOverridenShipToAddress(documentElement,wcContext,customerDetails);
				wcContext.getSCUIContext().getSession().removeAttribute(
						XPEDXWCUtils.XPEDX_SHIP_TO_ADDRESS_OVERIDDEN);
			}
			else {
//				Element shipToCustAddress = SCXmlUtil.getElementByAttribute(customerDetails.getDocumentElement(), "//Customer/CustomerAdditionalAddressList/CustomerAdditionalAddress", "IsDefaultShipTo", "Y");
				Element shipToCustAddress = SCXmlUtil.getElementByAttribute(customerDetails.getDocumentElement(),"CustomerAdditionalAddressList/CustomerAdditionalAddress", "IsDefaultShipTo", "Y");
				if(shipToCustAddress!=null) {
					String shipToKey = SCXmlUtil.getXpathAttribute(shipToCustAddress, "//PersonInfo/@PersonInfoKey");
					if(shipToKey!=null && shipToKey.trim().length()>0)
						documentElement.setAttribute("ShipToKey", shipToKey);
				}
			}	
			documentElement.appendChild(extnDocumentElem);
			scuiTransactionContext = wSCUIContext.getTransactionContext(true);
			env = (YFSEnvironment) scuiTransactionContext
					.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);
			YIFApi api = YIFClientFactory.getInstance().getApi();
			setYFSEnvironmentVariables(wcContext);
			Document outputListDocument = api.invoke(env, "changeOrder",
					inputDocument.getDocument());
			log.debug(SCXmlUtil.getString(outputListDocument));
		} catch (Exception ex) {
			log.error(ex.getMessage());
		} finally {
			releaseEnv(wcContext);
			scuiTransactionContext = null;
			env = null;
		}
	}
/*This provides input to the change order with the overridden ship to address */
	private static void setOverridenShipToAddress(YFCElement inputDocument, IWCContext wcContext ,Document shipToCustDetails) {
			
			if(XPEDXWCUtils.isShipToAddressOveridden(wcContext)) {
				YFCElement eleElement = inputDocument.getOwnerDocument().createElement("PersonInfoShipTo");
				XPEDXOverriddenShipToAddress shipToAddress = XPEDXWCUtils.getShipToOveriddenAddress(wcContext);
				
				Element element = SCXmlUtil.getChildElement(shipToCustDetails.getDocumentElement(), "CustomerAdditionalAddressList");
				element = SCXmlUtil.getChildElement(element,"CustomerAdditionalAddress");
				element = SCXmlUtil.getChildElement(element, "PersonInfo");
				eleElement.setAttribute("AddressLine1",(shipToAddress.getXpedxSTStreet() != null && shipToAddress.getXpedxSTStreet().trim().length() > 0) ? shipToAddress.getXpedxSTStreet() : SCXmlUtil.getAttribute(element,"AddressLine1"));
				eleElement.setAttribute("AddressLine2",(shipToAddress.getXpedxSTAddressLine2() != null && shipToAddress.getXpedxSTAddressLine2().trim().length() > 0) ? shipToAddress.getXpedxSTAddressLine2() : SCXmlUtil.getAttribute(element,"AddressLine2"));
				eleElement.setAttribute("AddressLine3",(shipToAddress.getXpedxSTAddressLine3() != null && shipToAddress.getXpedxSTAddressLine3().trim().length() > 0) ? shipToAddress.getXpedxSTAddressLine3() : SCXmlUtil.getAttribute(element,"AddressLine3"));
				eleElement.setAttribute("AddressLine4", "");
				eleElement.setAttribute("AddressLine5", "");
				eleElement.setAttribute("AddressLine6", "");

				eleElement.setAttribute("AlternateEmailID", "");
				eleElement.setAttribute("Beeper", "");
				eleElement.setAttribute("City",(shipToAddress.getXpedxSTCity()!= null && shipToAddress.getXpedxSTCity().trim().length() > 0) ? shipToAddress.getXpedxSTCity() : SCXmlUtil.getAttribute(element,"City"));
				eleElement.setAttribute("Company", "");
				eleElement.setAttribute("Country", "");
				eleElement.setAttribute("DayFaxNo", "");
				eleElement.setAttribute("DayPhone", "");
				eleElement.setAttribute("Department", "");
				eleElement.setAttribute("EMailID", "");
				eleElement.setAttribute("EveningFaxNo", "");
				eleElement.setAttribute("EveningPhone", "");
				eleElement
						.setAttribute("FirstName", (shipToAddress.getXpedxSTName()!= null && shipToAddress.getXpedxSTName().trim().length() > 0) ? shipToAddress.getXpedxSTName() : SCXmlUtil.getAttribute(element,"State"));
				eleElement.setAttribute("IsCommercialAddress", "");
				eleElement.setAttribute("JobTitle", "");
				eleElement.setAttribute("LastName", SCXmlUtil.getAttribute(element,"LastName"));
				eleElement.setAttribute("MiddleName", SCXmlUtil.getAttribute(element,"MiddleName"));
				eleElement.setAttribute("MobilePhone", "");
				eleElement.setAttribute("OtherPhone", "");
				eleElement.setAttribute("PersonID", "");
				eleElement.setAttribute("State", (shipToAddress.getXpedxSTState()!= null && shipToAddress.getXpedxSTState().trim().length() > 0) ? shipToAddress.getXpedxSTState() : SCXmlUtil.getAttribute(element,"State"));
				eleElement.setAttribute("Suffix", "");
				eleElement.setAttribute("TaxGeoCode", "");
				eleElement.setAttribute("Title", "");
				eleElement.setAttribute("ZipCode", (shipToAddress.getXpedxSTZip()!= null && shipToAddress.getXpedxSTZip().trim().length() > 0) ? shipToAddress.getXpedxSTZip() : SCXmlUtil.getAttribute(element,"ZipCode"));
				inputDocument.appendChild(eleElement);
			}
	}

	/*
	 * Util method to get the list of promotion items based on the common code
	 * type. The method return the list of promotion item xml element
	 */
	public static List getPromotionsList(IWCContext wcContext,
			String commonCodeType) {
		List promotionItems = new ArrayList();
		try {
			Map commonCodes = CommonCodeUtil.getCommonCodes(commonCodeType,
					CommonCodeDescriptionType.SHORT, wcContext);
			if (!commonCodes.isEmpty()) {
				Iterator ccIter = commonCodes.values().iterator();
				while (ccIter.hasNext()) {
					String itemID = (String) ccIter.next();
					Document promItemDoc = XPEDXOrderUtils.getItemDetails(
							itemID, wcContext.getCustomerId(), wcContext
									.getStorefrontId(), wcContext);
					Element promItemoutputEl = promItemDoc.getDocumentElement();
					Element itemEl = XMLUtilities.getElement(promItemoutputEl,
							"Item");
					String inputXml = SCXmlUtil.getString(itemEl);

					if (itemEl != null) {
						promotionItems.add(itemEl);
					}
				}
			}
		} catch (XPathExpressionException xpe) {
			log.error("Unable to get the Promotions common code...: " + xpe);
			promotionItems = null;
		} catch (Exception e) {
			log.error("Unable to get the Item details for promotion items...: "
					+ e);
			promotionItems = null;
		}

		return promotionItems;
	}

	public static void setYFSEnvironmentVariables(IWCContext wcContext,
			HashMap envVariables) {
		SCUIContext uictx = wcContext.getSCUIContext();
		ISCUITransactionContext iSCUITransactionContext = uictx.getTransactionContext(true);
		Object env = iSCUITransactionContext.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);
		if (env instanceof ClientVersionSupport) {
			ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
			clientVersionSupport.setClientProperties(envVariables);
		}
	}

	public static void releaseEnv(IWCContext wcContext){
		SCUIContext uictx = wcContext.getSCUIContext();
		ISCUITransactionContext iSCUITransactionContext = uictx.getTransactionContext();
		if (iSCUITransactionContext != null)
			SCUITransactionContextHelper.releaseTransactionContext(iSCUITransactionContext, uictx);
	}
	
	public static String getUOMReplacement(YFSEnvironment Env, String masterCust, String UOM) {
		//String replacedUOM = "ReplacedUOM" + UOM;
		String replacedUOM = XPXTranslationUtilsAPI.getCustomerUom(Env, masterCust, UOM);
		return replacedUOM;
	}

	public static String getUNSPSCReplacement(YFSEnvironment Env, String masterCust, String itemID, String UNSPSC) {
		//String replacedUNSPSC = "ReplacedUNSPSC" + UNSPSsC;
		String replacedUNSPSC = XPXTranslationUtilsAPI.getCustomerUnspsc(Env, masterCust, itemID, UNSPSC);
		return replacedUNSPSC;
	}

	public static void setUserTokenToEnvironment(YFSEnvironment env,
			SCUIContext uiContext) {
		String userTokenId = (String) uiContext.getSession().getAttribute(
				"UserToken");
		if (SCUIUtils.isVoid(userTokenId)) {
			YFSEnvironment yfsEnv = (YFSEnvironment) env;
			yfsEnv.setTokenID(userTokenId);
		}
	}

	public static boolean isShipToAddressOveridden(IWCContext wcContext) {
		if (wcContext.getSCUIContext().getSession().getAttribute(
				XPEDXWCUtils.XPEDX_SHIP_TO_ADDRESS_OVERIDDEN) != null) {
			return true;
		}
		return false;
	}

	public static XPEDXOverriddenShipToAddress getShipToOveriddenAddress(
			IWCContext wcContext) {
		if (isShipToAddressOveridden(wcContext)) {
			return (XPEDXOverriddenShipToAddress) wcContext.getSCUIContext()
					.getSession().getAttribute(
							XPEDXWCUtils.XPEDX_SHIP_TO_ADDRESS_OVERIDDEN);
		}
		return null;
	}

	public static String formatNumber(String number) {
		String formattedString = "";
		DecimalFormat dcf = new DecimalFormat();
		formattedString = dcf.format(new Long(number));
		return formattedString;
	}

	public static String getAttributeName(String attributeID, String orgCode,
			String attDomainID, String attGrpID) {
		String attributeName = null;
		Document outputDoc = null;
		IWCContext wcContext = WCContextHelper
				.getWCContext(ServletActionContext.getRequest());
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/Attribute/@AttributeID", attributeID);
		valueMap.put("/Attribute/@OrganizationCode", wcContext
				.getStorefrontId());
		valueMap.put("/Attribute/@AttributeDomainID", attDomainID);
		valueMap.put("/Attribute/@AttributeGroupID", attGrpID);

		Element input;
		try {
			input = WCMashupHelper.getMashupInput("xpedxGetAttributeDetails",
					valueMap, wcContext.getSCUIContext());
			String inputXml = SCXmlUtil.getString(input);
			log.debug("Input XML: " + inputXml);
			Object obj = WCMashupHelper.invokeMashup(
					"xpedxGetAttributeDetails", input, wcContext
							.getSCUIContext());
			outputDoc = ((Element) obj).getOwnerDocument();
			if (null != outputDoc) {
				log.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
				attributeName = SCXmlUtil.getAttribute(outputDoc
						.getDocumentElement(), "ShortDescription");
			}
		} catch (CannotBuildInputException e) {
			log.error("Unable to get the attibute Name for the Attribute ID: "
					+ attributeID + ". \n" + e);
		}

		return attributeName;
	}

	public static void loadXPEDXSpecficPropertiesIntoYFS(String fileName) {
		try {
			File wFile = new File(fileName);
			InputStream inputStream = null;
			if (wFile.exists()) {
				inputStream = new FileInputStream(wFile);
			} else {
				inputStream = YFSSystem.class.getResourceAsStream("/"
						+ fileName);
			}
			Properties properties = new Properties();
			if (inputStream != null) {
				properties.load(inputStream);
				if (properties.size() > 0) {
					YFCConfigurator.getInstance().addProperties(properties);
					inputStream.close();
				}
			}
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	/**
	 * It first checks whether location is there is in session, if yes, It will
	 * try to get it else It will fetch from database.
	 * 
	 * @return image location
	 */
	public static String getServerLocation(String codeDescription) {
		String sessionCommonCode = XPEDXConstants.IMAGE_SERVER_LOCATION;
		if(XPEDXConstants.IMAGE_SERVER.equals(codeDescription))
			sessionCommonCode = XPEDXConstants.IMAGE_SERVER_LOCATION;
		else if(XPEDXConstants.CONTENT_SERVER_MSDS.equals(codeDescription))
			sessionCommonCode = XPEDXConstants.CONTENT_SERVER_MSDS_LOCATION;
		else if(XPEDXConstants.CONTENT_SERVER.equals(codeDescription))
			sessionCommonCode = XPEDXConstants.CONTENT_SERVER_LOCATION;
		else if(XPEDXConstants.CONTENT_SERVER_FSC.equals(codeDescription))
			sessionCommonCode = XPEDXConstants.CONTENT_SERVER_FSC_LOCATION;
		else if(XPEDXConstants.CONTENT_SERVER_PEFC.equals(codeDescription))
			sessionCommonCode = XPEDXConstants.CONTENT_SERVER_PEFC_LOCATION;
		else if(XPEDXConstants.CONTENT_SERVER_SFI.equals(codeDescription))
			sessionCommonCode = XPEDXConstants.CONTENT_SERVER_SFI_LOCATION;
		IWCContext wcContext = WCContextHelper
				.getWCContext(ServletActionContext.getRequest());
		String imageLocation = (String) wcContext.getSCUIContext().getSession()
				.getAttribute(sessionCommonCode);
		if (imageLocation != null && imageLocation.trim().length() > 0) {
			return imageLocation;
		}
		try {
			String codeType = XPEDXConstants.CONTENT_LOCATION_CODE;
			Map<String, String> map = CommonCodeUtil.getCommonCodes(codeType,
					CommonCodeDescriptionType.SHORT, wcContext);
			if (map == null || !map.containsValue(codeDescription)) {
				map = CommonCodeUtil.getCommonCodes(codeType,
						CommonCodeDescriptionType.LONG, wcContext);
			}
			if (map != null) {
				Set<String> keySet = map.keySet();
				Iterator<String> iterator = keySet.iterator();
				while (iterator.hasNext()) {
					String key = iterator.next();
					String value = map.get(key);
					if (value != null && value.equals(codeDescription)) {
						imageLocation = key;
						wcContext.getSCUIContext().getSession().setAttribute(sessionCommonCode,imageLocation);
						break;
					}
				}
			}
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
		return imageLocation;
	}

	/**
	 * @param UOMCode
	 * @return the UOMDescription
	 * @throws XPathExpressionException
	 * @throws XMLExceptionWrapper
	 * @throws CannotBuildInputException
	 */
	public static String getUOMDescription(String UOMCode) throws XPathExpressionException, XMLExceptionWrapper,CannotBuildInputException {
		String UOMDesc;
		IWCContext wcContext = WCContextHelper.getWCContext(ServletActionContext.getRequest());
		Map<String, String> UOMDescMapFromCache = getUOMDescMapFromCache(wcContext);
		// XB-687 - start
	    LinkedHashMap<String, String> IsCustomerUomHashMap = (LinkedHashMap<String,String>)XPEDXWCUtils.getObjectFromCache("UOMsMap");
	    if(IsCustomerUomHashMap == null){
	    	IsCustomerUomHashMap =  new LinkedHashMap<String,String>();//(LinkedHashMap<String, String>) XPEDXWCUtils.getObjectFromCache("UOMsMap");
	    }
		// XB-687 - End
		if (null == UOMDescMapFromCache || UOMDescMapFromCache.size() <=0) {
			log.debug("Did not get the UOM description map in the cache. Calling the DB");
			// true to use the locale from the userpreferences
			uomDescMap = YfsUtils.getUOMDescriptions(wcContext, true);
			//put uomDescMap in the session. When there is a request for uomDescMap, return it from the session
			HttpServletRequest httpRequest = wcContext.getSCUIContext().getRequest();
	        HttpSession localSession = httpRequest.getSession();
			localSession.setAttribute("UOMDescriptionMap", uomDescMap);
		}else{
			log.debug("Got the UOM description map in the cache");
			uomDescMap = UOMDescMapFromCache;
		}
		if(IsCustomerUomHashMap.get(UOMCode)!=null && IsCustomerUomHashMap.get(UOMCode).equalsIgnoreCase("Y")){
			UOMDesc = UOMCode;
		}else{
			UOMDesc = uomDescMap.get(UOMCode);
		}
		if (UOMDesc == null || UOMDesc.trim().length() == 0) {
			if(UOMCode.contains("M_"));
				UOMCode=UOMCode.replaceFirst("M_", "");
			return UOMCode;
		}
		if(UOMDesc.contains("M_"));
			UOMDesc=UOMDesc.replaceFirst("M_", "");
		return UOMDesc;
	}
	
	public static String getFormattedUOMCode(String UOMCode) throws XPathExpressionException, XMLExceptionWrapper,CannotBuildInputException {
		String UOMDesc;
		String [] theCodeParts;

	    if(UOMCode != null){
		  theCodeParts = UOMCode.split("_");
	      UOMCode = theCodeParts[1];
	    }
	    else{
	    	getUOMDescription(UOMCode);
	    }

		return UOMCode;
	}	
	
	public static Map<String,String> getUOMDescMapFromCache(IWCContext wcContext)
    {
        HttpServletRequest httpRequest = wcContext.getSCUIContext().getRequest();
        HttpSession localSession = httpRequest.getSession();
        Map<String, String> UOMDescMap = (Map<String,String>)localSession.getAttribute("UOMDescriptionMap");
        return UOMDescMap;
    }

	public static String getLoginUserName(String UserID)	throws XPathExpressionException, XMLExceptionWrapper,
	CannotBuildInputException
	{
		return  getLoginUserName(UserID,false);
	}
/*	public static String getLoginUserName(String UserID,boolean formatLastfirst)
			throws XPathExpressionException, XMLExceptionWrapper,
			CannotBuildInputException {
		String UserName = null;
		Document outputDoc1 = null;
		if (UserID == null || UserID.equals("")) {
			return "";
		}
		Map<String, String> valueMap1 = new HashMap<String, String>();
		valueMap1.put("/CustomerContact/@CustomerContactID", UserID);
		IWCContext wcContext = WCContextHelper
				.getWCContext(ServletActionContext.getRequest());
		Element input1 = WCMashupHelper.getMashupInput(
				"xpedxItemListUserContactName", valueMap1, wcContext
						.getSCUIContext());
		Object obj1 = WCMashupHelper.invokeMashup(
				"xpedxItemListUserContactName", input1, wcContext
						.getSCUIContext());
		outputDoc1 = ((Element) obj1).getOwnerDocument();
		Element outputE2 = outputDoc1.getDocumentElement();
		Element CustomerContact = XMLUtilities.getElement(outputE2,
				"CustomerContact");
		String customerContactId = SCXmlUtils
		.getAttribute(CustomerContact, "CustomerContactID");
		
		// Added for JIRA 2643
		if(customerContactId == null ){
			return "";
		} 
		String FirstName = SCXmlUtils
				.getAttribute(CustomerContact, "FirstName");
		String LastName = SCXmlUtils.getAttribute(CustomerContact, "LastName");
		try {
			if(FirstName!=null || LastName!=null)
			{
				if(formatLastfirst)
					UserName = LastName + "," + FirstName;
				else
					UserName = FirstName + " " + LastName;
			}
			else
				UserName = UserID;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return UserName;
	}
*/	
	public static String getLoginUserName(String UserID,boolean formatLastfirst)
			throws XPathExpressionException, XMLExceptionWrapper,
			CannotBuildInputException {
		if (UserID == null || UserID.equals("")) {
			return "";
		}
		String UserName = null;
		XPEDXCustomerContactInfoBean xpedxCustomerContactInfoBean = (XPEDXCustomerContactInfoBean)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.XPEDX_Customer_Contact_Info_Bean);
		String FirstName;
		String LastName;
		
		if (UserID.equals(xpedxCustomerContactInfoBean.getCustomerContactID())) {
			FirstName = xpedxCustomerContactInfoBean.getFirstName();
			LastName = xpedxCustomerContactInfoBean.getLastName();
		} else {
			Document outputDoc1 = null;
			Map<String, String> valueMap1 = new HashMap<String, String>();
			valueMap1.put("/CustomerContact/@CustomerContactID", UserID);
			IWCContext wcContext = WCContextHelper
					.getWCContext(ServletActionContext.getRequest());
			Element input1 = WCMashupHelper.getMashupInput(
					"xpedxItemListUserContactName", valueMap1, wcContext
							.getSCUIContext());
			Object obj1 = WCMashupHelper.invokeMashup(
					"xpedxItemListUserContactName", input1, wcContext
							.getSCUIContext());
			outputDoc1 = ((Element) obj1).getOwnerDocument();
			Element outputE2 = outputDoc1.getDocumentElement();
			Element CustomerContact = XMLUtilities.getElement(outputE2,	"CustomerContact");
			String customerContactId = SCXmlUtil.getAttribute(CustomerContact, "CustomerContactID");
			
			// Added for JIRA 2643
			if(customerContactId == null ){
				return "";
			} 
			FirstName = SCXmlUtil.getAttribute(CustomerContact, "FirstName");
			LastName = SCXmlUtil.getAttribute(CustomerContact, "LastName");
		}
		
		try {
			if(FirstName!=null || LastName!=null)
			{
				if(formatLastfirst)
					UserName = LastName + "," + FirstName;
				else
					UserName = FirstName + " " + LastName;
			}
			else
				UserName = UserID;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return UserName;
	}

	/**
	 * 
	 * @param billToId
	 * @return
	 * @throws CannotBuildInputException
	 *             OUTPUT: <CustomerList> <Customer
	 *             CustomerID="30-0000156300-000-N3-39" OrganizationCode="xpedx"
	 *             Type="BillTo"> <CustomerList> <Customer
	 *             CustomerID="30-0000156300-000001-N3-39"
	 *             OrganizationCode="xpedx" Type="ShipTo"/> <Customer
	 *             CustomerID="30-0000156300-000002-N3-39"
	 *             OrganizationCode="xpedx" Type="ShipTo"/> <Customer
	 *             CustomerID="30-0000156300-000003-N3-39"/> <CustomerList>
	 *             </Customer> </<CustomerList>>
	 */
	public static ArrayList<String> getAllShipTosFromBillTo(String billToId)
			throws CannotBuildInputException {
		ArrayList<String> arrayList = new ArrayList<String>();
		Map<String, String> valueMap1 = new HashMap<String, String>();
		IWCContext context = WCContextHelper.getWCContext(ServletActionContext
				.getRequest());
		valueMap1.put("/Customer/@CustomerID", billToId);
		valueMap1.put("/Customer/@OrganizationCode", context.getStorefrontId());

		Element input1 = WCMashupHelper.getMashupInput(
				"XPXGetImmediateChildCustomerListService", valueMap1, context
						.getSCUIContext());
		Object obj1 = WCMashupHelper.invokeMashup(
				"XPXGetImmediateChildCustomerListService", input1, context
						.getSCUIContext());
		Document outputDoc = ((Element) obj1).getOwnerDocument();
		YFCDocument yfcDocument = YFCDocument.getDocumentFor(outputDoc);
		if (yfcDocument.getFirstChild() != null
				&& yfcDocument.getFirstChild().getFirstChild() != null
				&& yfcDocument.getFirstChild().getFirstChild().getFirstChild() != null) {
			YFCIterable yfcIterable = yfcDocument.getFirstChild()
					.getFirstChild().getFirstChild().getChildren();
			while (yfcIterable.hasNext()) {
				YFCNode yfcNode = (YFCNode) yfcIterable.next();
				String wCustomerId = yfcNode.getAttributes().get("CustomerID");
				arrayList.add(wCustomerId);
			}
		}
		return arrayList;
	}
	
	public static Document getItemCustXrefDoc(IWCContext wcContext,String attributeToQuery, ArrayList<String> attributeValues) {
		HashMap<String,String> custPartnItemIdsMap = new LinkedHashMap<String, String>();
		HashMap<String, String> valueMap = new HashMap<String, String>();
		String envCode = null;
		String custDivision = null;
		String custNumber = null;
		Document output = null;
		if(attributeValues!=null && attributeValues.size()>0) {
			XPEDXShipToCustomer shipCustomer = (XPEDXShipToCustomer) XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
			envCode = shipCustomer.getExtnEnvironmentCode();
			custDivision = shipCustomer.getExtnCustomerDivision();
			custNumber = shipCustomer.getExtnLegacyCustNumber();
			
			valueMap.put("/XPXItemcustXref/@CustomerNumber", custNumber);
			valueMap.put("/XPXItemcustXref/@EnvironmentCode", envCode);
			valueMap.put("/XPXItemcustXref/@CustomerDivision", custDivision);
			try {
				Element input = WCMashupHelper.getMashupInput("xpedxItemCustXRef", valueMap, wcContext);
				Element complexQuery = input.getOwnerDocument().createElement("ComplexQuery");
				Element or = input.getOwnerDocument().createElement("Or");
				for(String value : attributeValues) {
					Element exp = input.getOwnerDocument().createElement("Exp");
					exp.setAttribute("Name", attributeToQuery);
					exp.setAttribute("Value", value);
					or.appendChild(exp);
				}
				complexQuery.appendChild(or);
				input.appendChild(complexQuery);
				Element out = (Element)WCMashupHelper.invokeMashup("xpedxItemCustXRef", input, wcContext.getSCUIContext());
				output = out.getOwnerDocument();
			}
			catch (Exception e) {
				log.error("Error invoking the Api to fetch the results + XPEDXWCUtils.getItemIdsMap()");
			}
		}
		return output;
	}

	public static Element getItemCustXrefInfo(Map itemAttributes)
			throws CannotBuildInputException {
		IWCContext wcContext = WCContextHelper
				.getWCContext(ServletActionContext.getRequest());

		Map<String, String> valueMap = new HashMap<String, String>();
		Map<String, String> getExtnValueMap = new HashMap<String, String>();
		String itemId = (String) itemAttributes.get("LegacyItemNumber");
		String MPC = (String) itemAttributes.get("MPC");
		String CustomerPartNumber = (String) itemAttributes
				.get("CustomerItemNumber");
		String customerLegNo = null;
		String customerSuffix = null;
		//String companyCode = null;
		String envCode = null;
		String customerId = wcContext.getCustomerId();
		String custShipFromBranch = null;

        /*envCode = (String) wcContext.getWCAttribute(XPEDXConstants.ENVIRONMENT_CODE,WCAttributeScope.LOCAL_SESSION);
        //companyCode = (String) wcContext.getWCAttribute(XPEDXConstants.COMPANY_CODE,WCAttributeScope.LOCAL_SESSION);
        customerLegNo = (String) wcContext.getWCAttribute(XPEDXConstants.LEGACY_CUST_NUMBER,WCAttributeScope.LOCAL_SESSION);
        custShipFromBranch = (String) wcContext.getWCAttribute(XPEDXConstants.SHIP_FROM_BRANCH,WCAttributeScope.LOCAL_SESSION);
        String custDivision = (String) wcContext.getWCAttribute(XPEDXConstants.CUSTOMER_DIVISION,WCAttributeScope.LOCAL_SESSION);*/
        
        XPEDXShipToCustomer shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		
		/*String envCode = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.ENVIRONMENT_CODE);
		//String companyCode = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.COMPANY_CODE);
		String legacyCustomerNumber = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.LEGACY_CUST_NUMBER);
		String shipFromBranch = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.SHIP_FROM_BRANCH);
		String custDivision = (String) wcContext.getWCAttribute(XPEDXConstants.CUSTOMER_DIVISION,WCAttributeScope.LOCAL_SESSION);*/
		envCode = shipToCustomer.getExtnEnvironmentCode();
		customerLegNo = shipToCustomer.getExtnLegacyCustNumber();
		custShipFromBranch =shipToCustomer.getExtnShipFromBranch();
		String custDivision =shipToCustomer.getExtnCustomerDivision();
        
        if(envCode==null || /*companyCode==null || */customerLegNo ==null || custShipFromBranch==null)
        {
			//Fetch customer extn fields
        	getExtnValueMap.put("/Customer/@CustomerID", wcContext.getCustomerId());
			getExtnValueMap.put("/Customer/@OrganizationCode", wcContext.getStorefrontId());
			Element input = WCMashupHelper.getMashupInput("xpedx-customer-getExtnFieldsForCustomerPart",
					getExtnValueMap, wcContext.getSCUIContext());
			Element customerData = (Element)WCMashupHelper.invokeMashup("xpedx-customer-getExtnFieldsForCustomerPart", input,
					wcContext.getSCUIContext());
			Element extnElement = SCXmlUtil.getChildElement(customerData, "Extn");
			
			if(extnElement!=null)
			{
				customerLegNo = extnElement.getAttribute("ExtnLegacyCustNumber");
				envCode = extnElement.getAttribute("ExtnEnvironmentCode");
				//companyCode = extnElement.getAttribute("ExtnCompanyCode");
				custShipFromBranch = extnElement.getAttribute("ExtnShipFromBranch");
				custDivision = extnElement.getAttribute("ExtnCustomerDivision");
			}
        }
		/*
		StringTokenizer str = new StringTokenizer(customerId, "-");
		if (str.hasMoreTokens())
			customerBranch = str.nextToken();
		if (str.hasMoreTokens())
			customerLegNo = str.nextToken();
		if (str.hasMoreTokens())
			customerSuffix = str.nextToken();
		if (str.hasMoreTokens())
			customerCode = str.nextToken();
		if (str.hasMoreTokens())
			envCode = str.nextToken();
		*/
		if(itemId!=null)
			valueMap.put("/XPXItemcustXref/@LegacyItemNumber", itemId);
		if(MPC!=null)
			valueMap.put("/XPXItemcustXref/@MPC", MPC);
		if(CustomerPartNumber!=null)
			valueMap.put("/XPXItemcustXref/@CustomerItemNumber", CustomerPartNumber);
		valueMap.put("/XPXItemcustXref/@CustomerNumber", customerLegNo);
		//valueMap.put("/XPXItemcustXref/@CompanyCode", companyCode);
		valueMap.put("/XPXItemcustXref/@EnvironmentCode", envCode);
		valueMap.put("/XPXItemcustXref/@CustomerDivision", custDivision);

		Element input1 = WCMashupHelper.getMashupInput("xpedxItemCustXRef",
				valueMap, wcContext.getSCUIContext());
		Object obj1 = WCMashupHelper.invokeMashup("xpedxItemCustXRef", input1,
				wcContext.getSCUIContext());
		return (Element) obj1;
	}

	public static String getDivisionName() throws CannotBuildInputException {
		String OrganizationName = null;
		// Added for performance issue - itemdetail.action - removing extra call to getCustomerDetails
		String envCode;
		String ShipFromBranch ;
		try {
			IWCContext context = WCContextHelper
					.getWCContext(ServletActionContext.getRequest());
			String customerID = context.getCustomerId();
			//Commented for performance issue - itemdetail.action - removing extra call to getCustomerDetails
			//String envCode = getEnvironmentCode(customerID);
			String storeFrontId = context.getStorefrontId();
			
			 XPEDXShipToCustomer shipToCustomer =(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache("shipToCustomer");
			 if(shipToCustomer!=null && shipToCustomer.getExtnEnvironmentCode()!=null && shipToCustomer.getExtnEnvironmentCode().length()>0){
				  envCode = shipToCustomer.getExtnEnvironmentCode();
			 }else{			
				  envCode = getEnvironmentCode(customerID);
			 }
			 if(shipToCustomer!=null && shipToCustomer.getExtnShipFromBranch()!=null && shipToCustomer.getExtnShipFromBranch().length()>0){
				  ShipFromBranch = shipToCustomer.getExtnShipFromBranch();
			 }else{			
				  ShipFromBranch = getCustomerShipFromDivision(customerID,
					storeFrontId);
			 }
			Document organizationDetails = null;
			if (envCode != null && envCode.trim().length() > 0) {
				organizationDetails = getOrganizationDetails(ShipFromBranch
						+ "_" + envCode);
			} else {
				organizationDetails = getOrganizationDetails(ShipFromBranch);
			}
			OrganizationName = SCXmlUtil.getXpathAttribute(organizationDetails
					.getDocumentElement(),
					"/OrganizationList/Organization/@OrganizationName");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return OrganizationName;
	}
	
	//return state
	
	public static String getState() throws CannotBuildInputException {
		String state = null;
		try {
			IWCContext context = WCContextHelper
					.getWCContext(ServletActionContext.getRequest());
			String customerID = context.getCustomerId();
			String envCode = getEnvironmentCode(customerID);
			String storeFrontId = context.getStorefrontId();
			String ShipFromBranch = getCustomerShipFromDivision(customerID,
					storeFrontId);
			Document organizationDetails = null;
			if (envCode != null && envCode.trim().length() > 0) {
				organizationDetails = getOrganizationDetails(ShipFromBranch
						+ "_" + envCode);
			} else {
				organizationDetails = getOrganizationDetails(ShipFromBranch);
			}
			state = SCXmlUtil.getXpathAttribute(organizationDetails
					.getDocumentElement(),
					"/OrganizationList/Organization/CorporatePersonInfo/@State");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return state;
	}
	
	
	/**
	 * JIRA: 566
	 * This is used to fetch the ShipFrom address from the division information
	 * @return
	 * @throws CannotBuildInputException
	 */
	public static Document getShipFromAddress() throws CannotBuildInputException {
		Document organizationDetails = null;
		XPEDXShipToCustomer shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		try {
			/*IWCContext context = WCContextHelper
					.getWCContext(ServletActionContext.getRequest());*/
			//String customerID = context.getCustomerId();
			String envCode = shipToCustomer.getExtnEnvironmentCode();//getEnvironmentCode(customerID);
			//String storeFrontId = context.getStorefrontId();
			String ShipFromBranch = shipToCustomer.getExtnShipFromBranch();//getCustomerShipFromDivision(customerID,
					//storeFrontId);
			
			if (envCode != null && envCode.trim().length() > 0) {
				organizationDetails = getOrganizationList(ShipFromBranch
						+ "_" + envCode);
			} else {
				organizationDetails = getOrganizationList(ShipFromBranch);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return organizationDetails;
	}

	/**
	 * JIRA: 566
	 * ContactInformation of the organization
	 * @param orgCode
	 * @return
	 * @throws CannotBuildInputException
	 */
	private static Document getOrganizationList(String orgCode) throws CannotBuildInputException {
		Document outputDoc = null;

		if (null == orgCode || orgCode.length() <= 0) {
			log
					.debug("getOrganizationList: orgCode is a required field. Returning an empty document");
			return outputDoc;
		}
		IWCContext wcContext = WCContextHelper
				.getWCContext(ServletActionContext.getRequest());
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/Organization/@OrganizationCode", orgCode);

		Element input = WCMashupHelper.getMashupInput(
				"XPEDXGetOrgListForShipFromAdd", valueMap, wcContext
						.getSCUIContext());
		String inputXml = SCXmlUtil.getString(input);
		log.debug("Input XML: " + inputXml);
		Object obj = WCMashupHelper.invokeMashup("XPEDXGetOrgListForShipFromAdd",
				input, wcContext.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		if (null != outputDoc) {
			log.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
		}

		return outputDoc;
	}

	/**
	 * JIRA: 243
	 * Separate mash up was written to get only the Extn field - ExtnShipFromBranch 
	 * of the the customer. 
	 * Prior to the fix, entire customer details was being fetched.
	 * @param customerID
	 * @param storeFrontID
	 * @return
	 * @throws CannotBuildInputException
	 */
	public static String getCustomerShipFromDivision(String customerID,
			String storeFrontID) throws CannotBuildInputException {
		String customerShipFromDivision = null;
		if (YFCCommon.isVoid(customerID)) {
			log
					.error("customerID cannot be null. Returning back to the caller");
			return customerShipFromDivision;
		}
		if (YFCCommon.isVoid(storeFrontID)) {
			log
					.error("storeFrontID cannot be null. Returning back to the caller");
			return customerShipFromDivision;
		}
		Document CustDetails = getCustomerDetails(customerID, storeFrontID, extnFieldsInformationMashUp);
		if (YFCCommon.isVoid(CustDetails)) {
			log
					.error("The customer info is NULL. Response from DB seems to be corrupted. Returning back to the caller");
			return customerShipFromDivision;
		}
		// get the ExtnShipFromBranch
		customerShipFromDivision = SCXmlUtil.getXpathAttribute(CustDetails
				.getDocumentElement(), "/Customer/Extn/@ExtnShipFromBranch");
		if (YFCCommon.isVoid(customerShipFromDivision)) {
			log.error("No Ship from Division defined for customer "
					+ customerID + ". Returning back to the caller.");
			return null;
		}
		log.debug("ExtnShipFromBranch for customer " + customerID + " is: "
				+ customerShipFromDivision);
		return customerShipFromDivision;
	}

	/**
	 * getting whether this customer can request for samples
	 * JIRA: 243
	 * Separate mash up was written to get few of the Extn fields - ExtnSampleRequestFlag 
	 * of the the customer. 
	 * Prior to the fix, entire customer details was being fetched.
	 *
	 */
	public static String canRequestSamples(String customerID,
			String storeFrontID) throws CannotBuildInputException {
		String canRequestSample = "N";
		if (YFCCommon.isVoid(customerID)) {
			if(log.isDebugEnabled()){
			log.debug("customerID cannot be null. "
					+ "Returning back to the caller");
			}
			return canRequestSample;
		}
		if (YFCCommon.isVoid(storeFrontID)) {
			if(log.isDebugEnabled()){
			log.debug("storeFrontID cannot be null. "
					+ "Returning back to the caller");
			}
			return canRequestSample;
		}
		Document CustDetails = getCustomerDetails(customerID, storeFrontID, extnFieldsInformationMashUp);
		if (YFCCommon.isVoid(CustDetails)) {
			if(log.isDebugEnabled()){
			log.debug("The customer info is NULL. Response from "
					+ "DB seems to be corrupted. Returning back to the caller");
			}
			return canRequestSample;
		}
		// get the ExtnSampleRequest
		canRequestSample = SCXmlUtil.getXpathAttribute(CustDetails
				.getDocumentElement(), "/Customer/Extn/@ExtnSampleRequestFlag");
		if (YFCCommon.isVoid(canRequestSample)) {
			if(log.isDebugEnabled()){
			log.debug("No canRequestSample defined for customer " + customerID
					+ ". Returning back to the caller.");
			}
			return canRequestSample;
		}
		log.debug("ExtnSampleRequest for customer " + customerID + " is: "
				+ canRequestSample);
		return canRequestSample;
	}
	
	public static String getCustomerExtnSuffixType(String customerID,
			String storeFrontID) throws CannotBuildInputException {
		String customerSuffixType = "";
		if (YFCCommon.isVoid(customerID)) {
			if(log.isDebugEnabled()){
			log.debug("customerID cannot be null. "
					+ "Returning back to the caller");
			}
			return customerSuffixType;
		}
		if (YFCCommon.isVoid(storeFrontID)) {
			if(log.isDebugEnabled()){
			log.debug("storeFrontID cannot be null. "
					+ "Returning back to the caller");
			}
			return customerSuffixType;
		}
		Document CustDetails = getCustomerDetails(customerID, storeFrontID, extnFieldsInformationMashUp);
		if (YFCCommon.isVoid(CustDetails)) {
			if(log.isDebugEnabled()){
			log.debug("The customer info is NULL. Response from "
					+ "DB seems to be corrupted. Returning back to the caller");
			}
			return customerSuffixType;
		}
		// get the ExtnSampleRequest
		customerSuffixType = SCXmlUtil.getXpathAttribute(CustDetails
				.getDocumentElement(), "/Customer/Extn/@ExtnSuffixType");
		log.debug("ExtnSuffixType for customer " + customerID + " is: "
				+ customerSuffixType);
		return customerSuffixType;
	}

	/* START - PunchOut Changes - adsouza */
	/*
	 * Method returns the XPATH in the CXML. This XPATH points to user-name that
	 * is then used to login
	 */

	public static String getAuthUserXPathForCustomerIdentity(
			HttpServletRequest req, HttpServletResponse res, String custIdentity) {
		String userXPath = null;
		Document outputDoc = null;
		try {
			if (null == custIdentity || custIdentity.length() <= 0) {
				log
						.error("getAuthUserXPathForCustomerIdentity: Customer Identity is null.");
				return null;
			}
			IWCContext wcContext = WCContextHelper.getWCContext(req);
			Map<String, String> valueMap = new HashMap<String, String>();
			valueMap.put("/Customer/Extn/@ExtnCustIdentity", custIdentity);

			Element input = WCMashupHelper.getMashupInput(
					"xpedx-customer-getUserXPath", valueMap, wcContext
							.getSCUIContext());
			String inputXml = SCXmlUtil.getString(input);
			log.debug("Input XML: " + inputXml);

			Object obj = WCMashupHelper.invokeMashup(
					"xpedx-customer-getUserXPath", input, wcContext
							.getSCUIContext());
			outputDoc = ((Element) obj).getOwnerDocument();

			Element wElement = outputDoc.getDocumentElement();
			wElement = SCXmlUtil.getChildElement(wElement, "Customer");
			wElement = SCXmlUtil.getChildElement(wElement, "Extn");
			userXPath = SCXmlUtil.getAttribute(wElement, "ExtnCXmlUserXPath");
			if (userXPath != null && userXPath.trim().length() == 0) {
				userXPath = null;
			}
			if (userXPath == null){
				//userXPath = "/Request/PunchOutSetupRequest/Contact/Email";
				Map commonCodes = CommonCodeUtil.getCommonCodes("CXML_USER_XPATH",CommonCodeDescriptionType.LONG, wcContext);
				if(!commonCodes.isEmpty())
					userXPath = (String)commonCodes.values().iterator().next();
			}

			log.debug("*********User XPath for Customer: ***************="
					+ userXPath);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
		return userXPath;
	}
	
	/*
	 * Method returns the OCI Fields for a given Customer.
	 */

	public static String[] getOCIFieldsForCustomer(
			HttpServletRequest req, HttpServletResponse res, String customerID, String storefrontID) {
		String[] ociFields = new String[4];
		Document outputDoc = null;
		try {
			if (null == customerID || customerID.length() <= 0) {
				log
						.error("getOCIFieldsForCustomer: Customer Identity is null.");
				return null;
			}		
			
			
				//outputDoc = XPEDXWCUtils.getCustomerDetails(
					//	customerID, storefrontID, getOCIFieldsMashup);
			
			IWCContext wcContext = WCContextHelper.getWCContext(req);
			Map<String, String> valueMap = new HashMap<String, String>();
			valueMap.put("/Customer/@CustomerID", customerID);
			valueMap.put("/Customer/@OrganizationCode", storefrontID);

			Element input = WCMashupHelper.getMashupInput(
					"xpedx-customer-getOCIFields", valueMap, wcContext
							.getSCUIContext());
			String inputXml = SCXmlUtil.getString(input);
			log.debug("Input XML: " + inputXml);

			Object obj = WCMashupHelper.invokeMashup(
					"xpedx-customer-getOCIFields", input, wcContext
							.getSCUIContext());
			outputDoc = ((Element) obj).getOwnerDocument();
			
					Element outputElem = outputDoc.getDocumentElement();
					ociFields[0] = SCXmlUtil.getXpathAttribute(outputElem,
							"//Customer/Extn/@ExtnUseOCInSAPParamFlag");
					ociFields[1] = SCXmlUtil.getXpathAttribute(outputElem,
						"//Customer/Extn/@ExtnUsernameParam");
					ociFields[2] = SCXmlUtil.getXpathAttribute(outputElem,
						"//Customer/Extn/@ExtnUserPwdParam");
					ociFields[3] = SCXmlUtil.getXpathAttribute(outputElem,
						"//Customer/Extn/@ExtnUserEmailTemplate");
					
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
		return ociFields;
	}
	
	/* END - PunchOut Changes - adsouza */
	public static XPEDXShipToCustomer getShipToAddressOfCustomer(Element CustomerDetails) {
		
		return getShipToAddressOfCustomer(CustomerDetails,null);
	}
	public static XPEDXShipToCustomer getShipToAddressOfCustomer(Element CustomerDetails,XPEDXShipToCustomer defualtShipToAssigned) {
		if(defualtShipToAssigned == null)
		{
			defualtShipToAssigned = new XPEDXShipToCustomer();
		}
		if(CustomerDetails == null) {
			if(log.isDebugEnabled()){
			log.debug("Customer Element should not be null so returning null ship to customer");
			}
			return defualtShipToAssigned;
		}
		else {
			Element customerDefaultShipToAddressElem = SCXmlUtil.getElementByAttribute(CustomerDetails, "CustomerAdditionalAddressList/CustomerAdditionalAddress", "IsDefaultShipTo", "Y");
			Element element = null;
			if(customerDefaultShipToAddressElem == null) {
				if(log.isDebugEnabled()){
				log.debug("No default ship to address found in the customer additional addresses so retunring the first address in the customer additional address list");
				}
			 	NodeList customerAddtnlAddressList = SCXmlUtil.getXpathNodes(CustomerDetails, "CustomerAdditionalAddressList/CustomerAdditionalAddress");
			 	if(customerAddtnlAddressList.getLength()>0) {				 	
				 	element= (Element) customerAddtnlAddressList.item(0);
			 	}
			 	else {
			 		if(log.isDebugEnabled()){
			 		log.debug("customer does not have any additional addresses so retunring empty address");
			 		}
					return defualtShipToAssigned;
			 	}
			}
			else {
				element = customerDefaultShipToAddressElem;				
			}
			if(element!=null) {
				element = SCXmlUtil.getChildElement(element, "PersonInfo");
				defualtShipToAssigned.setFirstName(SCXmlUtil.getAttribute(element,
						"FirstName"));
				defualtShipToAssigned.setMiddleName(SCXmlUtil.getAttribute(element,
						"MiddleName"));
				log.debug("************MiddleName************="
						+ SCXmlUtil.getAttribute(element, "MiddleName"));
				defualtShipToAssigned.setLastName(SCXmlUtil.getAttribute(element,
						"LastName"));
				log.debug("************LastName************="
						+ SCXmlUtil.getAttribute(element, "LastName"));
				defualtShipToAssigned.setEMailID(SCXmlUtil.getAttribute(element,
						"EMailID"));
				defualtShipToAssigned.setCity(SCXmlUtil.getAttribute(element, "City"));
				defualtShipToAssigned.setCityCode(SCXmlUtil.getAttribute(element, "City"));
				defualtShipToAssigned
						.setState(SCXmlUtil.getAttribute(element, "State"));
				defualtShipToAssigned.setCountry(SCXmlUtil.getAttribute(element,
						"Country"));
				defualtShipToAssigned.setZipCode(SCXmlUtil.getAttribute(element,
						"ZipCode"));
				defualtShipToAssigned.setCompany(SCXmlUtil.getAttribute(element,
						"Company"));
				defualtShipToAssigned.setDayPhone(SCXmlUtil.getAttribute(element,
				"DayPhone"));
				defualtShipToAssigned.setCustomerID(SCXmlUtil.getAttribute(CustomerDetails,
				"CustomerID"));
				Element ExtnElem = SCXmlUtil.getChildElement(CustomerDetails, "Extn");				
				defualtShipToAssigned.setShipToName(SCXmlUtil.getAttribute(ExtnElem, "ExtnCustomerName"));
				defualtShipToAssigned.setLocationID(SCXmlUtil.getAttribute(ExtnElem, "ExtnCustomerStoreNumber"));
				
				List<String> addressList = new ArrayList<String>();
				for (int index = 0; index < 6; index++) {
					if (SCXmlUtil.getAttribute(element, "AddressLine" + index) != null
							&& SCXmlUtil.getAttribute(element, "AddressLine" + index)
									.trim().length() > 0) {
						addressList.add(SCXmlUtil.getAttribute(element, "AddressLine"
								+ index));
					}
				}
				defualtShipToAssigned.setAddressList(addressList);
			}
			return defualtShipToAssigned;
		}
	}

	// customerID =
	// customerDivision+"-"+legacyCustomerNumber+"-"+billToSuffix+"-"+envtId+"-"+companyCode+"-B";
	public static String getEnvironmentCode(String customerId) {
		String custEnvCode = null;
		if(YFCCommon.isVoid(customerId)){
			log.error("Cannot get EnvironmentCode for empty CustomerID.");
			return custEnvCode;
			
		}
		//xpedx-customer-getEnvironmentCode
		IWCContext wcContext = WCContextHelper.getWCContext(ServletActionContext.getRequest());
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/Customer/@CustomerID", customerId);
		valueMap.put("/Customer/@OrganizationCode", wcContext.getStorefrontId());
		Element input;
		try {
			input = WCMashupHelper.getMashupInput("xpedx-customer-getEnvironmentCode", valueMap, wcContext.getSCUIContext());
		} catch (CannotBuildInputException e) {
			log.error("Exception getting the EnvironmentCode for Customer "+customerId+" from DB`. \n",e);
			return custEnvCode;
		}
		Object obj = WCMashupHelper.invokeMashup("xpedx-customer-getEnvironmentCode", input, wcContext.getSCUIContext());
		if(YFCCommon.isVoid(obj)){
			log.error("No data information for Customer "+customerId+" in DB.");
			return custEnvCode;
		}
		Document customerDetailDocument = ((Element) obj).getOwnerDocument();
		Element custExtnElement = SCXmlUtil.getChildElement(customerDetailDocument.getDocumentElement(), "Extn");
		custEnvCode = SCXmlUtil.getAttribute(custExtnElement, "ExtnEnvironmentCode");
		log.debug("EnvironmentCode for customer "+customerId+" is "+custEnvCode);
		return custEnvCode;
	}
	
	public static HashMap<String,String> getHashMapFromList(List<String> list)
	{
		HashMap<String,String> map = new LinkedHashMap<String,String>();
		if(list!=null)
		{
			Iterator<String> listIter = list.iterator();
			while(listIter.hasNext())
			{
				String listElemVal = listIter.next();
				map.put(listElemVal, listElemVal);
			}
		}
		return map;
	}
	
	//customized the method from ship to List From DB for Order List UI Labeled List
	public static HashMap<String,String> getHashMapFromListWithLabel(List<String> list)
	{
		HashMap<String,String> map = new LinkedHashMap<String,String>();
		map.put(XPEDXConstants.DEFAULT_SELECT_SHIP_TO_LABEL, XPEDXConstants.DEFAULT_SELECT_SHIP_TO_LABEL);
		if(list!=null)
		{
			Iterator<String> listIter = list.iterator();
			while(listIter.hasNext())
			{
				String listElemVal = listIter.next();
				map.put(listElemVal, listElemVal);
			}
		}
		return map;
	}

	public static String shareformatBillToShipToCustomer(String customerId,boolean billto){
		String[] custDetails = customerId.split("-");
		if(custDetails!=null)
		{
			String division = custDetails[0];
			String customerNumber = custDetails[1];
			String suffix = custDetails[2];
			StringBuffer sb = new StringBuffer();
			if(billto)
			{
				sb.append(division).append("-").append(customerNumber);
				//billto  don't add suffix
			}
			else
			{
						sb.append(division).append("-").append(suffix).append(" - "); //Modified code for jira 3307
				
			}
			return sb.toString();
		}
		//If the string cannot be properly split, return the customerId
		return customerId;
	}
	//reopen 3244 due to dependency 3307 above created new methods below
	
	public static String shareFormatSuffixShipToCustomer(String custDisplayId){
		//method calling fro Jire 3244 reopen
		String[] custDetails = custDisplayId.split("-");
		int strShipto = custDisplayId.indexOf("Ship-To:");
		int strBillTo = custDisplayId.indexOf("Bill-To:");
		if(custDetails!=null)
		{
					StringBuffer sb = new StringBuffer();
						
			if(custDetails.length==3 && strShipto!=0 && strBillTo!=0)
			{
				String division = custDetails[0];
				String customerNumber = custDetails[1];
				String suffix = custDetails[2];
				//suffix= customerNumber.replace(" ", "");
				sb.append(customerNumber).append(suffix);
				return sb.toString();
			}
			else{
				custDisplayId = custDisplayId;
			}
			
		}
		return custDisplayId;
	}
	//end methods reopen 3244 due to dependency 3307 above created new methods above 
	
	
	public static String formatBillToShipToCustomer(String customerId){
		String[] custDetails = customerId.split("-");
		if(custDetails!=null && custDetails.length>3)
		{
			String division = custDetails[0];
			String customerNumber = custDetails[1];
			String suffix = custDetails[2];
			StringBuffer sb = new StringBuffer();
			sb.append(division).append("-").append(customerNumber).append("-").append(suffix);
			return sb.toString();
		}
		//If the string cannot be properly split, return the customerId
		return customerId;
		}
	
	public static String formatCustomer(String customerId, IWCContext wcContext){
		if(MY_SELF.equals(customerId))
		{
			return customerId;
		}
		String displayCustomerId = customerId;
		String extnSuffixType = "";
		try {
			extnSuffixType = getCustomerExtnSuffixType(customerId, wcContext.getStorefrontId());
		} catch (CannotBuildInputException e) {
			return displayCustomerId;
		}
		String loggedInCustomerID = (String) wcContext.getSCUIContext().getSession().getAttribute(LOGGED_IN_CUSTOMER_ID);
		if(loggedInCustomerID!=null && loggedInCustomerID.trim().length()>0) {
			HashMap<String, HashMap<String, String>> custInfoMap = (HashMap<String, HashMap<String, String>>) wcContext.getSCUIContext().getSession().getAttribute(
					LOGGED_IN_FORMATTED_CUSTOMER_ID_MAP);
			String key = loggedInCustomerID+"_"+wcContext.getStorefrontId();
			if(custInfoMap!=null)
			{
				HashMap<String, String> map = custInfoMap.get(key);
				if (extnSuffixType!=null && (extnSuffixType.equalsIgnoreCase("M")))
				{
					return map.get("SAPParentAccNo");
				}
				else if(extnSuffixType!=null && (extnSuffixType.equalsIgnoreCase("S") || extnSuffixType.equalsIgnoreCase("B")))
				{
					return formatBillToShipToCustomer(customerId);
				}
				else
					return  map.get("SAPNumber");
			}
		}		
			return customerId;
	}

	public static Map<String, String> custFullAddresses(ArrayList<String> CustomerId, String StoreFrontID)
	{
		return custFullAddresses(CustomerId,StoreFrontID,false,false);

	}
	public static Map<String, String> custFullAddresses(ArrayList<String> CustomerId, String StoreFrontID,boolean formatBilltoShipto)
	{
		return custFullAddresses(CustomerId,StoreFrontID,formatBilltoShipto,false);
	}
	public static Map<String, String> custFullAddresses(ArrayList<String> CustomerId, String StoreFrontID,boolean formatBilltoShipto, boolean appendBillToShipTo) {
		ISCUITransactionContext scuiTransactionContext = null;
		LinkedHashMap<String, String> customerHashMap = new LinkedHashMap<String, String>();
		SCUIContext wSCUIContext = null;

		if(CustomerId!=null && CustomerId.size()>0){
			try {
				YFCDocument inputDocument = YFCDocument.createDocument("Customer");
				YFCElement documentElement = inputDocument.getDocumentElement();

				IWCContext context = WCContextHelper.getWCContext(ServletActionContext
						.getRequest());
				wSCUIContext = context.getSCUIContext();
				scuiTransactionContext = wSCUIContext
						.getTransactionContext(true);
				
				documentElement.setAttribute("OrganizationCode", StoreFrontID);
				
				YFCElement complexQueryElement = documentElement.createChild("ComplexQuery");
				YFCElement complexQueryOrElement = documentElement.createChild("Or");
				for (int i = 0; i < CustomerId.size(); i++) {
					String currentCustomerID = CustomerId.get(i);
					if(currentCustomerID== null || currentCustomerID.length()==0)
						continue;
					if(MY_SELF.equals(currentCustomerID))
						customerHashMap.put(currentCustomerID, currentCustomerID);
					else{
						YFCElement expElement = documentElement.createChild("Exp");
						expElement.setAttribute("Name", "CustomerID");
						expElement.setAttribute("Value", currentCustomerID);
						complexQueryOrElement.appendChild((YFCNode)expElement);
					}
				}
				complexQueryElement.setAttribute("Operator", "AND");
				complexQueryElement.appendChild(complexQueryOrElement);

				YFCDocument template = YFCDocument
				.getDocumentFor("<CustomerList>" +
									"<Customer CustomerID=\"\">" +
										"<BuyerOrganization OrganizationName=\"\"/>" +
										"<Extn ExtnSuffixType=\"\" ExtnCustomerStoreNumber=\"\" />" +
										"<CustomerAdditionalAddressList>" +
											"<CustomerAdditionalAddress CustomerAdditionalAddressKey=\"\">" +
												"<PersonInfo AddressLine1=\"\" AddressLine2=\"\" AddressLine3=\"\" AddressLine4=\"\" AddressLine5=\"\" AddressLine6=\"\" City=\"\" State=\"\" Country=\"\" ZipCode=\"\"/>" + 
											"</CustomerAdditionalAddress>" +
										"</CustomerAdditionalAddressList>" +
									"</Customer>" +
								"</CustomerList>");
				
				YFCElement yfcElement = SCUIPlatformUtils.invokeXAPI(
						"getCustomerList", inputDocument
								.getDocumentElement(), template
								.getDocumentElement(), wSCUIContext);
				
				YFCNodeList<YFCElement> nodelist = yfcElement.getElementsByTagName("Customer");
				YFCElement custElement = null;
				for(int i=0; i<CustomerId.size();i++)
				{
				for (int j=0;j<nodelist.getLength();j++) {
					custElement = nodelist.item(j);
					if (custElement != null) {
						String customerID = custElement.getAttribute("CustomerID");
						String testid = CustomerId.get(i);
							if(customerID!=null) {
							if(customerID.equals(testid))
							{
								String custFullAddr = "";
								
								String tempCustDisplayId= null;//for keeping only name and ID jira 3244  
								String custSuffixType = custElement.getChildElement("Extn").getAttribute("ExtnSuffixType");
									String custDisplayId = customerID;
									String loggedInCustomerID = null;
									String MSapCustId = null;
									String SapCustId = null;									
									
									if(isCustomerSelectedIntoConext(context))
										loggedInCustomerID = getLoggedInCustomerFromSession(context);
									else
										loggedInCustomerID = context.getCustomerId();
									if(loggedInCustomerID!=null && loggedInCustomerID.trim().length()>0) {
										HashMap<String, HashMap<String, String>> custInfoMap = (HashMap<String, HashMap<String, String>>) context.getSCUIContext().getSession().getAttribute(
												LOGGED_IN_FORMATTED_CUSTOMER_ID_MAP);
										String key = loggedInCustomerID+"_"+context.getStorefrontId();
										if(custInfoMap!=null)
										{
											HashMap<String, String> map = custInfoMap.get(key);
											if (custSuffixType!=null && (custSuffixType.equalsIgnoreCase(XPEDXConstants.MASTER_CUSTOMER_SUFFIX_TYPE)))
											{
												tempCustDisplayId = map.get("SAPParentAccNo");
												custDisplayId = "Master:"+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+ tempCustDisplayId;
											}
											else if(custSuffixType!=null && (custSuffixType.equalsIgnoreCase(XPEDXConstants.SHIP_TO_CUSTOMER_SUFFIX_TYPE)))
											{
												if(formatBilltoShipto)
												{
													//default behaviour
													custDisplayId = formatBillToShipToCustomer(customerID);
													if(appendBillToShipTo)// This is for Authorize locations
														//changed Ship-To : by balkhi jira 3244
														custDisplayId = "Ship-To:"+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+custDisplayId;
													//custDisplayId = "Ship-To - "+custDisplayId;
												}
												else
												{
													custDisplayId = shareformatBillToShipToCustomer(customerID,false);
													//special display criteria for share modal
													if(appendBillToShipTo)// This is for Authorize locations
														//changed Ship-To : by balkhi 3244
														custDisplayId = "Ship-To:"+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+custDisplayId;
													//custDisplayId = "Ship-To - "+custDisplayId;
												}
											}
											else if (custSuffixType!=null && (custSuffixType.equalsIgnoreCase(XPEDXConstants.BILL_TO_CUSTOMER_SUFFIX_TYPE)))
											{
												if(formatBilltoShipto)
												{
													//default behaviour
													//added by balkhi 27th Feb 2012 3244 reopen
													custDisplayId = shareformatBillToShipToCustomer(customerID,true);
													if(appendBillToShipTo)// This is for Authorize locations
														custDisplayId = "Bill-To:"+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+custDisplayId;
												}
												else
												{
													custDisplayId = shareformatBillToShipToCustomer(customerID,true);
													//special display criteria for share modal
													if(appendBillToShipTo)// This is for Authorize locations
														custDisplayId = "Bill-To:"+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+custDisplayId;
												}
											}
											else {
												custDisplayId = map.get(customerID);
												if(!(custDisplayId!=null && custDisplayId.trim().length()>0)) {
													custDisplayId = customerID;
												}
												if(appendBillToShipTo)// This is for Authorize locations
													custDisplayId = "Customer: "+ "&nbsp;&nbsp;&nbsp;"+ custDisplayId;
													//added by balkhi 27th Feb 2012 3244 reopen
												//Changing the name to Customer - Account - 4146 - Kubra
											}
										}
									}		
									//added so that the condition occurs only when format billtoship
									if(custDisplayId!=null && custDisplayId.indexOf("-")==-1 && !formatBilltoShipto)
									{
										YFCElement element = custElement.getChildElement("BuyerOrganization");
										if (element.getAttribute("OrganizationName") != null 
												&& element.getAttribute("OrganizationName").trim().length() > 0) 
											custFullAddr += element.getAttribute("OrganizationName")+" ";
										custFullAddr += "("+ custDisplayId +")";
									}
									else{
										YFCElement buyerOrgElement = custElement.getChildElement("BuyerOrganization");
										YFCElement addrElement = custElement.getChildElement("CustomerAdditionalAddressList");
										YFCElement extnElement = custElement.getChildElement("Extn");

										if(addrElement!=null)
											addrElement = addrElement.getChildElement("CustomerAdditionalAddress");
										if(addrElement!=null)
											addrElement = addrElement.getChildElement("PersonInfo");

										custFullAddr += custDisplayId+" ";
										
										if (buyerOrgElement.getAttribute("OrganizationName") != null && buyerOrgElement.getAttribute("OrganizationName").trim().length() > 0){
											custFullAddr += buyerOrgElement.getAttribute("OrganizationName");//added by balkhi to change ' ,' to ',' jira 3244
										tempCustDisplayId = custFullAddr;
										//added by balkhi 27th Feb 2012 3244 reopen
									}
										/*added for 2769*/
										if(custSuffixType!=null && (custSuffixType.equalsIgnoreCase(XPEDXConstants.SHIP_TO_CUSTOMER_SUFFIX_TYPE))){
											 custFullAddr = shareFormatSuffixShipToCustomer(custFullAddr);
											  // this above method for Jira 3244 reopen
											
											if (extnElement.getAttribute("ExtnCustomerStoreNumber") != null 
													&& extnElement.getAttribute("ExtnCustomerStoreNumber").trim().length() > 0){ 
												custFullAddr += ""+extnElement.getAttribute("ExtnCustomerStoreNumber");// change ' ,' to ',' jira 3244 and Local ID
											   
											}
										}
										
										if(addrElement!=null) {
											for (int index = 0; index < 6; index++) {
												if (addrElement.getAttribute("AddressLine"+index) != null && addrElement.getAttribute("AddressLine"+index).trim().length() > 0) 
													custFullAddr += ", "+addrElement.getAttribute("AddressLine"+index);
											}
											if (addrElement.getAttribute("City") != null && addrElement.getAttribute("City").trim().length() > 0) 
												custFullAddr += ", "+addrElement.getAttribute("City");
											if (addrElement.getAttribute("State") != null && addrElement.getAttribute("State").trim().length() > 0) 
												custFullAddr += ", "+addrElement.getAttribute("State");
											if (addrElement.getAttribute("ZipCode") != null && addrElement.getAttribute("ZipCode").trim().length() > 0) 
												custFullAddr += " "+getFormattedZipCode(addrElement.getAttribute("ZipCode"));
											if (addrElement.getAttribute("Country") != null && addrElement.getAttribute("Country").trim().length() > 0) 
												custFullAddr += " "+addrElement.getAttribute("Country");//removed , for 3244
											
										}
										if(custFullAddr.indexOf("Account:")!=-1 && tempCustDisplayId!=null){
													custFullAddr = userProfileAuthorizeLocationAccount(tempCustDisplayId);// for 3244 name and ID on user profile authorize location
								
											}
									}
								customerHashMap.put(customerID, custFullAddr);
									break;
							}
								
							}
							}
						}
					}
			} catch (Exception ex) {
				log.error(ex.getMessage());
				scuiTransactionContext.rollback();
			} finally {
				if (scuiTransactionContext != null) {
					SCUITransactionContextHelper.releaseTransactionContext(
							scuiTransactionContext, wSCUIContext);
				}
			}
		}
		return customerHashMap;
	}
	
	public static Map<String, String> custFullAddresses(Document CustomerListDoc) {
		return custFullAddresses(CustomerListDoc,false,false);

	}
	
	public static Map<String, String> custFullAddresses(Document CustomerListDoc,boolean formatBilltoShipto, boolean appendBillToShipTo){
		LinkedHashMap<String, String> customerHashMap = new LinkedHashMap<String, String>();
		IWCContext context = WCContextHelper.getWCContext(ServletActionContext.getRequest());
		if(CustomerListDoc!=null) {
			YFCDocument yfcDoc = YFCDocument.getDocumentFor(CustomerListDoc);
			YFCElement yfcElement = yfcDoc.getDocumentElement();
			
			YFCIterable<YFCElement> iteartor = yfcElement.getChildren();
			YFCElement custElement = null;
			int countCustWithoutNumber = 0;//jira 3244 variable
			while (iteartor.hasNext()) {
				custElement = iteartor.next();
				if (custElement != null) {
					String customerID = custElement.getAttribute("CustomerID");
						if(customerID!=null) {
							
							String custFullAddr = "";
							String custSuffixType = custElement.getChildElement("Extn").getAttribute("ExtnSuffixType");
								String custDisplayId = customerID;
								String loggedInCustomerID = null;
								if(isCustomerSelectedIntoConext(context))
									loggedInCustomerID = getLoggedInCustomerFromSession(context);
								else
									loggedInCustomerID = context.getCustomerId();
								if(loggedInCustomerID!=null && loggedInCustomerID.trim().length()>0) {
									HashMap<String, HashMap<String, String>> custInfoMap = (HashMap<String, HashMap<String, String>>) context.getSCUIContext().getSession().getAttribute(
											LOGGED_IN_FORMATTED_CUSTOMER_ID_MAP);
									String key = loggedInCustomerID+"_"+context.getStorefrontId();
									if(custInfoMap!=null)
									{
										HashMap<String, String> map = custInfoMap.get(key);
										if (custSuffixType!=null && (custSuffixType.equalsIgnoreCase(XPEDXConstants.MASTER_CUSTOMER_SUFFIX_TYPE)))
										{
											custDisplayId = map.get("SAPParentAccNo");
										}
										else if(custSuffixType!=null && (custSuffixType.equalsIgnoreCase(XPEDXConstants.SHIP_TO_CUSTOMER_SUFFIX_TYPE)))
										{
											if(formatBilltoShipto)
											{
												//default behaviour
												custDisplayId = formatBillToShipToCustomer(customerID);
												if(appendBillToShipTo)// This is for Authorize locations
													//changed Ship-To : by balkhi jira 3244
													custDisplayId = "Ship-To: "+custDisplayId;
												//custDisplayId = "Ship-To - "+custDisplayId;
											}
											else
											{
												custDisplayId = shareformatBillToShipToCustomer(customerID,false);
												//special display criteria for share modal
												if(appendBillToShipTo)// This is for Authorize locations
													//changed Ship-To : by balkhi jira 3244
													custDisplayId = "Ship-To: "+custDisplayId;
												//custDisplayId = "Ship-To - "+custDisplayId;
											}
										}
										else if (custSuffixType!=null && (custSuffixType.equalsIgnoreCase(XPEDXConstants.BILL_TO_CUSTOMER_SUFFIX_TYPE)))
										{
											if(formatBilltoShipto)
											{
												//default behaviour
												custDisplayId = shareformatBillToShipToCustomer(customerID, true);//added for 3244 reopen
												if(appendBillToShipTo)// This is for Authorize locations
													custDisplayId = "Bill-To: "+custDisplayId;
											}
											else
											{
												custDisplayId = shareformatBillToShipToCustomer(customerID,true);
												//special display criteria for share modal
												if(appendBillToShipTo)// This is for Authorize locations
													custDisplayId = "Bill-To: "+custDisplayId;
											}
										}
										else {
											custDisplayId = map.get(customerID);
											if(!(custDisplayId!=null && custDisplayId.trim().length()>0)) {
												custDisplayId = customerID;
											}
											if(appendBillToShipTo)// This is for Authorize locations
												custDisplayId = "Account: "+custDisplayId; //SAP removed for Jira 3244 reopen
											}
									}
								}		
								//added so that the condition occurs only when format billtoship
								if(custDisplayId!=null && custDisplayId.indexOf("-")==-1 && !formatBilltoShipto)
								{
									YFCElement element = custElement.getChildElement("BuyerOrganization");
									if (element.getAttribute("OrganizationName") != null && element.getAttribute("OrganizationName").trim().length() > 0) 
										custFullAddr += element.getAttribute("OrganizationName")+" ";
									if(countCustWithoutNumber==0){
										custFullAddr = custFullAddr;
									}else{
										custFullAddr += "("+ custDisplayId +")";
										custFullAddr = "Account: "+ custFullAddr;
									}
								//jira 3244
								}
								else{
									YFCElement buyerOrgElement = custElement.getChildElement("BuyerOrganization");
									YFCElement addrElement = custElement.getChildElement("CustomerAdditionalAddressList");
									YFCElement extnElement = custElement.getChildElement("Extn");
									
									if(addrElement!=null)
										addrElement = addrElement.getChildElement("CustomerAdditionalAddress");
									if(addrElement!=null)
										addrElement = addrElement.getChildElement("PersonInfo");

									custFullAddr += custDisplayId+" ";
									
									if (buyerOrgElement.getAttribute("OrganizationName") != null && buyerOrgElement.getAttribute("OrganizationName").trim().length() > 0) 
										custFullAddr += buyerOrgElement.getAttribute("OrganizationName");//removed by balkhi for 3244 jira
									
									/*added for 2769*/
									if(custSuffixType!=null && (custSuffixType.equalsIgnoreCase(XPEDXConstants.SHIP_TO_CUSTOMER_SUFFIX_TYPE))){
										
										custFullAddr = shareFormatSuffixShipToCustomer(custFullAddr);
										// this above method for Jira 3244 reopen
										
										if (extnElement.getAttribute("ExtnCustomerStoreNumber") != null 
												&& extnElement.getAttribute("ExtnCustomerStoreNumber").trim().length() > 0) {
											custFullAddr += ""+extnElement.getAttribute("ExtnCustomerStoreNumber")+" ";//3244 reopen removed Local ID
										}
									}
									
									if(addrElement!=null) {
										for (int index = 0; index < 6; index++) {
											if (addrElement.getAttribute("AddressLine"+index) != null && addrElement.getAttribute("AddressLine"+index).trim().length() > 0) 
												custFullAddr += ", "+addrElement.getAttribute("AddressLine"+index);
										}
										if (addrElement.getAttribute("City") != null && addrElement.getAttribute("City").trim().length() > 0) 
											custFullAddr += ", "+addrElement.getAttribute("City");
										if (addrElement.getAttribute("State") != null && addrElement.getAttribute("State").trim().length() > 0) 
											custFullAddr += ", "+addrElement.getAttribute("State");
										if (addrElement.getAttribute("ZipCode") != null && addrElement.getAttribute("ZipCode").trim().length() > 0) 
											custFullAddr += " "+getFormattedZipCode(addrElement.getAttribute("ZipCode"));
										if (addrElement.getAttribute("Country") != null && addrElement.getAttribute("Country").trim().length() > 0) 
											custFullAddr += " "+addrElement.getAttribute("Country");//removed by balkhi for 3244 jira
										
									}
								}
							customerHashMap.put(customerID, custFullAddr);
						}
					}
				countCustWithoutNumber++;//jira 3244 variable
				}
		}
		
		return customerHashMap;
	}
	
	public static String getFormattedZipCode(String zipCode){		
		try
		{		
			if (zipCode != null && zipCode.length() > 0) {
				String zipCode1 = "";
				if (zipCode.indexOf("-") > -1) {
					zipCode1 = zipCode.replaceAll("-", "");
				} else {
					zipCode1 = zipCode;
				}
				long zipCode2 = Long.parseLong(zipCode1);
				String zipCode3 = String.valueOf(zipCode2);
				if(zipCode3.length() > 5) {					
					java.text.MessageFormat phoneMsgFmt=new java.text.MessageFormat("{0}-{1}"); 
					String[] numArr = {zipCode3.substring(0, 5), zipCode3.substring(5, zipCode3.length())};
					return phoneMsgFmt.format(numArr);
				} else {
					return zipCode1;
				}
			} else {
				return zipCode;
			}
		}
		catch(Exception e)
		{
			return zipCode;
		}
	}
	
	
	public static HashMap<String, HashMap<String, String>> getFormattedMasterCustomer(String MsapCustomerId, String sfId){
		
		IWCContext wcContext = WCContextHelper.getWCContext(ServletActionContext.getRequest());
		Map<String, String> valueMap = new HashMap<String, String>();
		HashMap<String, HashMap<String, String>> custInfoMap = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> formattedMasterCustomerMap = new HashMap<String, String>();
		if(MsapCustomerId!=null && MsapCustomerId.trim().length()>0 && sfId!=null && sfId.trim().length()>0) {
			
			valueMap.put("/XPXCustomerSAPNumberView/@MSAPCustomerID", MsapCustomerId);
			Element input;
			try {
				input = WCMashupHelper.getMashupInput(
						"xpedxGetSAPDetails", valueMap, wcContext
								.getSCUIContext());
			} catch (CannotBuildInputException e) {
				// Error in invoking mashup
				return custInfoMap;
			}
			Object obj = WCMashupHelper.invokeMashup(
					"xpedxGetSAPDetails", input, wcContext
							.getSCUIContext());
			Document allSAPCustDoc = ((Element) obj).getOwnerDocument();
			
			NodeList sapList = allSAPCustDoc.getElementsByTagName("XPXCustomerSAPNumberView");
			
			if(sapList!=null && sapList.getLength()>0) {
				int size = sapList.getLength();
				for(int i=0;i<size;i++)
				{
					Element sapElement = (Element)sapList.item(i);
					if(sapElement!=null) {
						String sapCustomerID = sapElement.getAttribute("SAPCustomerID");
						String sapNumber = sapElement.getAttribute("SAPNumber");
						String sapParentAccNo = sapElement.getAttribute("SAPParentAccNo");
						if(sapParentAccNo!=null && sapParentAccNo.trim().length()>0)
							formattedMasterCustomerMap.put("SAPParentAccNo", sapParentAccNo);
						if(sapCustomerID==null)
							sapCustomerID = "SAPNumber";
						if(sapNumber!=null && sapNumber.trim().length()>0)
							formattedMasterCustomerMap.put(sapCustomerID, sapNumber);
						
					}
				}
			}
	
			/*Element allChildCustomerList = prepareAndInvokeMashup(MID_GET_CUSTOMER_ASSIGNMENT);
			Document allChildDoc = allChildCustomerList.getOwnerDocument();
			--- Removing this as this is causing a performance issue if the number of ship tos are large
			NodeList childList = allChildDoc.getElementsByTagName("Organization");
			if(childList!=null && childList.getLength()>0)
			{
				int size = childList.getLength();
				for(int i=0;i<size;i++)
				{
					Element orgElement =  (Element)childList.item(i);
					String suffixType = orgElement.getAttribute("CustomerSuffixType");
					if(suffixType!=null && (suffixType.equals("S") || suffixType.equals("B")))
					{
						String sapParNumber = orgElement.getAttribute("SAPParentAccNo");
						String sapNo =  orgElement.getAttribute("SAPNumber");
						if(sapParNumber!=null && sapParNumber.trim().length()>0)
							formattedMasterCustomerMap.put("SAPParentAccNo", sapParNumber);
						if(sapNo!=null && sapNo.trim().length()>0) {
							String sapCustomerId = "SAPNumber";
							if(suffixType.equals("S")) {
								String billToCustomerId = orgElement.getAttribute("ParentCustomerID");
								Element billToCustomerElem = SCXmlUtil.getElementByAttribute(allChildDoc.getDocumentElement(), "Organization", "CustomerID",billToCustomerId );
								if(billToCustomerElem!=null)
									sapCustomerId = billToCustomerElem.getAttribute("ParentCustomerID");
							}
							else if(suffixType.equals("B")) {
								sapCustomerId = orgElement.getAttribute("ParentCustomerID");
							}
							if(sapCustomerId==null)
								sapCustomerId = "SAPNumber";
							formattedMasterCustomerMap.put(sapCustomerId, sapNo);
						}
					}
				}
			}
			*/
			String custInfoKey = MsapCustomerId+"_"+sfId;
			custInfoMap.put(custInfoKey, formattedMasterCustomerMap);
		}
		wcContext.getSCUIContext().getSession().setAttribute(LOGGED_IN_FORMATTED_CUSTOMER_ID_MAP, custInfoMap);
		return custInfoMap;
		
	}
	
	public static Integer getLeadTimeOfDivision(String shipFromDivision) {
		if(!YFCCommon.isStringVoid(shipFromDivision)) {
			Integer leadTime = null;
			IWCContext wcContext = WCContextHelper.getWCContext(ServletActionContext.getRequest());
			Map<String, String> valueMap = new HashMap<String, String>();
			valueMap.put("/Organization/@OrganizationCode", shipFromDivision);
			valueMap.put("/Organization/@OrganizationKey", shipFromDivision);
			Element input;
			try {
				input = WCMashupHelper.getMashupInput(
						"xpedxLeadTimeOfDivision", valueMap, wcContext
								.getSCUIContext());
			} catch (CannotBuildInputException e) {
				// Error in invoking mashup
				return leadTime;
			}
			Object obj = WCMashupHelper.invokeMashup(
					"xpedxLeadTimeOfDivision", input, wcContext
							.getSCUIContext());
			Document orgDoc = ((Element) obj).getOwnerDocument();
			Element ExtnTransferCircleElem = SCXmlUtil
					.getElementByAttribute(
							orgDoc.getDocumentElement(),
							"/Organization/Extn/XPXXferCircleList/XPXXferCircle",
							"OrganizationKey", shipFromDivision);
			Integer noOfdays = SCXmlUtil.getIntAttribute(ExtnTransferCircleElem,"NoOfDays");
			if(noOfdays!= null) {
				leadTime = noOfdays*24;
			}
			return leadTime;
		}
		return 0;
	}
	
	/*
	 * Bulk UI changes: 
	 * Manufacturing Item number is no longer deemed as unique so removed that for search in QuickAdd.
	 * Biz decision taken and modifications done accordingly.
	 * EB-466 Changes. Removed the logic for MPC and Manufacturing Item code.
	 */
	public static HashMap<String, String> getSkuTypesForQuickAdd(IWCContext wcContext)
	{
		HashMap<String, String> skuTypeList = new LinkedHashMap<String, String>();
		String customerItemFlag = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.BILL_TO_CUST_PART_ITEM_FLAG);
		
		skuTypeList.put("1", wcContext.getStorefrontId()+XPEDXConstants.XPEDX_ITEM_LABEL);
		if(!SCUtil.isVoid(customerItemFlag) && customerItemFlag.equalsIgnoreCase("Y")) {
			skuTypeList.put("2", XPEDXConstants.CUSTOMER_ITEM_LABEL);
		}
		return skuTypeList;
	}
	
	//added for jira 3201
	public static String getDecimalQty(String quantity){
		String formattedDecimalQty = "";
		Double doubleQty = Double.parseDouble(quantity);
		NumberFormat formatter = new DecimalFormat("#0.#####");
		formattedDecimalQty=formatter.format(doubleQty).toString();
		return formattedDecimalQty;	
	}
	
	public static String getFormattedQty(String quantity)
	{
		/* TODO: -FXD1-5 change format for Qty- 41 : c8 e.g 1,000,000 */
		String formattedQty = "";
		StringBuffer fmtCommaFormattedQty = new StringBuffer();
		if(quantity!=null){
			int decIndex = quantity.indexOf(".");
			if(decIndex!= -1)
			{
				quantity = quantity.substring(0, decIndex);
			}
			int intQty = Integer.parseInt(quantity);
			formattedQty = Integer.toString(intQty);
		}
		else
		{
			formattedQty = "0";
		}
		return commaSeparatedStringFmt(formattedQty);
	}

	private static String commaSeparatedStringFmt (String strOriginal ) {
		
		
		 char data[] = new char[2*strOriginal.length()]; //Needs some extra spaces for commas
			for(int k = 0; k < data.length; k++ )
				data[k]=' ';
			
	     
		for(int i = strOriginal.length()-1,j=0 ; i >= 0; i-- )
		{
			data[j++] = (strOriginal.charAt(i)) ;
			if( (j+1)%4 == 0 && i>1 )
				data[j++] = ',' ;
		}
		
		String commaFmtString =  new StringBuffer(new String(data)).reverse().toString();
		return commaFmtString.trim() ;
	}
	
	/**
	 * This method fetches all the Countries - Code, Short Description and Long Description for a default organization
	 * @return
	 */
	public static Map<String, String> getCountryCodeList(){
		
		Map<String, String> countryCodesMap = new LinkedHashMap<String, String>(); 
		IWCContext wcContext = WCContextHelper.getWCContext(ServletActionContext.getRequest());
		@SuppressWarnings("unchecked")
		Map<String, String> countryCodesContextMap = (Map<String, String>)wcContext.getSCUIContext().getServletContext().getAttribute("COUNTRY_CODES");
		if(null != countryCodesContextMap && countryCodesContextMap.size() > 0 ) { 			
			return countryCodesContextMap;		
		}
		
		Element input;
		String countryCode = "";
		String countryDesc = "";
		try {
			input = WCMashupHelper.getMashupInput("xpedxCountryCodeList", wcContext.getSCUIContext());
		} catch (CannotBuildInputException e) {
			// Error in invoking mashup
			return countryCodesMap;
		}
		Object obj = WCMashupHelper.invokeMashup("xpedxCountryCodeList", input, wcContext.getSCUIContext());
		Document countriesListDoc = ((Element) obj).getOwnerDocument();
		
		
		NodeList countryCodeElemsList = countriesListDoc.getElementsByTagName("CommonCode");
		Element countryElem;
		for(int i=0; countryCodeElemsList!=null && i<countryCodeElemsList.getLength(); i++){
			countryElem = (Element)countryCodeElemsList.item(i);
			countryCode = countryElem.getAttribute("CodeValue");
			countryDesc = countryElem.getAttribute("CodeShortDescription");
			countryCodesMap.put(countryCode, countryDesc);
		}
		
		wcContext.getSCUIContext().getServletContext().setAttribute("COUNTRY_CODES", countryCodesMap);
		return countryCodesMap;
	}
	
	/**
	 * This method fetches all the SKUs(Customer Part #, Manufacturer #, MPC #) for a particular xpedx itemId
	 * @param wcContext
	 * @param itemId
	 * @return
	 * @throws CannotBuildInputException
	 * @throws XPathExpressionException
	 */
	public static HashMap<String, String> getAllSkusForItem(IWCContext wcContext,String itemId) throws CannotBuildInputException, XPathExpressionException
	{
		HashMap<String, String> itemSkuMap = new LinkedHashMap<String, String>();
		
		if(itemId ==null || itemId.trim().length() ==0 || wcContext.getStorefrontId()==null || wcContext.getCustomerId()==null)
			return itemSkuMap;
		XPEDXShipToCustomer shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		
		/*String envCode = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.ENVIRONMENT_CODE);
		//String companyCode = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.COMPANY_CODE);
		String legacyCustomerNumber = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.LEGACY_CUST_NUMBER);
		String shipFromBranch = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.SHIP_FROM_BRANCH);
		String custDivision = (String) wcContext.getWCAttribute(XPEDXConstants.CUSTOMER_DIVISION,WCAttributeScope.LOCAL_SESSION);*/
		String envCode = shipToCustomer.getExtnEnvironmentCode();
		String companyCode = shipToCustomer.getExtnCompanyCode();
		String legacyCustomerNumber = shipToCustomer.getExtnLegacyCustNumber();
		String shipFromBranch =shipToCustomer.getExtnShipFromBranch();
		String custDivision =shipToCustomer.getExtnCustomerDivision();
		//Fetch the customer Part Number
		HashMap valueMap =  new HashMap<String, String>();
		valueMap.put("/XPXItemcustXref/@EnvironmentCode", envCode);
		//valueMap.put("/XPXItemcustXref/@CompanyCode", companyCode);
		valueMap.put("/XPXItemcustXref/@CustomerNumber", legacyCustomerNumber);
		valueMap.put("/XPXItemcustXref/@LegacyItemNumber", itemId);
		valueMap.put("/XPXItemcustXref/@CustomerDivision", custDivision);
		
		Element input =  null;
		Element res =  null;
		input = WCMashupHelper.getMashupInput("XPEDXMyItemsListGetCustomersPart", valueMap, wcContext.getSCUIContext());
		
		res = (Element)WCMashupHelper.invokeMashup("XPEDXMyItemsListGetCustomersPart",input , wcContext.getSCUIContext());
		Element itemEle 		= SCXmlUtil.getChildElement(res, "XPXItemcustXref");
		
		if (itemEle != null){
			if(itemId.equals((String)itemEle.getAttribute("LegacyItemNumber")))
			{
				String customerPartNumber = itemEle.getAttribute("CustomerItemNumber");
				if(customerPartNumber!=null && customerPartNumber.length()>0)
				{
					itemSkuMap.put(XPEDXConstants.CUST_SKU_FLAG_FOR_CUSTOMER_ITEM, customerPartNumber);
				}
			}
		}
		//Get this data from the product information
		//Populate all the fields
		valueMap = new HashMap<String, String>();
		valueMap.put("/Item/@ItemID", itemId);
		valueMap.put("/Item/@CallingOrganizationCode",wcContext.getStorefrontId());
		valueMap.put("/Item/CustomerInformation/@CustomerID",wcContext.getCustomerId());
		
		input = WCMashupHelper.getMashupInput("XPEDXGetItemSKUs", valueMap, wcContext.getSCUIContext());
	
		res = (Element)WCMashupHelper.invokeMashup("XPEDXGetItemSKUs",input , wcContext.getSCUIContext());
		Element itemEle2 		= SCXmlUtil.getChildElement(res, "Item");
		if(itemEle2!=null)
		{
			Element primeInfoElem = XMLUtilities.getElement(itemEle2, "PrimaryInformation");
			if(primeInfoElem!=null)
			{
				String manufactureItem = primeInfoElem.getAttribute("ManufacturerItem");
				if(manufactureItem!=null && manufactureItem.length()>0)
					itemSkuMap.put(XPEDXConstants.CUST_SKU_FLAG_FOR_MANUFACTURER_ITEM, manufactureItem);
			}
			Element extnElem = XMLUtilities.getElement(itemEle2, "Extn");
			if(extnElem!=null)
			{
				String mpcCode = extnElem.getAttribute("ExtnMpc");
				if(mpcCode!=null && mpcCode.length()>0)
					itemSkuMap.put(XPEDXConstants.CUST_SKU_FLAG_FOR_MPC_ITEM, mpcCode);
			}
		}
		return itemSkuMap;
	}
	@SuppressWarnings("unchecked")
	public static HashMap<String, HashMap<String, String>> getAllSkusForItemsList(IWCContext wcContext,ArrayList<String> itemIds) throws Exception {
		return getAllSkusForItemsList(wcContext,itemIds,true);
	}
	@SuppressWarnings("unchecked")
	public static HashMap<String, HashMap<String, String>> getAllSkusForItemsList(IWCContext wcContext,
			ArrayList<String> itemIds, boolean isItemDetailsCall) throws Exception {
		HashMap<String, String> skuMap = new LinkedHashMap<String, String>();
		HashMap<String, HashMap<String, String>> itemSkuMap = new LinkedHashMap<String, HashMap<String,String>>();
		String useCustSku = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.CUSTOMER_USE_SKU);
		if(!SCUtil.isVoid(useCustSku) && (XPEDXConstants.CUST_SKU_FLAG_FOR_MPC_ITEM.equalsIgnoreCase(useCustSku.trim())
				|| XPEDXConstants.CUST_SKU_FLAG_FOR_MANUFACTURER_ITEM.equalsIgnoreCase(useCustSku.trim()))   && isItemDetailsCall == true) {
			
			Document itemDoc = XPEDXOrderUtils.getXpedxMinimalItemDetails(itemIds, wcContext.getCustomerId(),wcContext.getStorefrontId(), wcContext);
			
			if(itemDoc!=null) {
				Element itemList = itemDoc.getDocumentElement();
				Iterator<Element> itemIter = SCXmlUtil.getChildren(itemList);
				while(itemIter.hasNext()) {
					Element itemElement = itemIter.next();
					if(!SCUtil.isVoid(itemElement)) {
						String itemId = SCXmlUtil.getAttribute(itemElement, "ItemID");
						if(SCUtil.isVoid(itemSkuMap.get(itemId)) && !SCUtil.isVoid(itemId)) {
							Element primeInfoElem = XMLUtilities.getElement(itemElement, "PrimaryInformation");
							if(primeInfoElem!=null)
							{
								String manufactureItem = primeInfoElem.getAttribute("ManufacturerItem");
								if(manufactureItem!=null && manufactureItem.length()>0)
									skuMap.put(XPEDXConstants.CUST_SKU_FLAG_FOR_MANUFACTURER_ITEM, manufactureItem);
							}
							Element extnElem = XMLUtilities.getElement(itemElement, "Extn");
							if(extnElem!=null)
							{
								String mpcCode = extnElem.getAttribute("ExtnMpc");
								if(mpcCode!=null && mpcCode.length()>0)
									skuMap.put(XPEDXConstants.CUST_SKU_FLAG_FOR_MPC_ITEM, mpcCode);
							}
						}
						itemSkuMap.put(itemId, (HashMap<String, String>)skuMap.clone());
						skuMap.clear();//clearing the sku map for the other Item ids
					}
				}
			}
		}
		if(!SCUtil.isVoid(useCustSku) && XPEDXConstants.CUST_SKU_FLAG_FOR_CUSTOMER_ITEM.equalsIgnoreCase(useCustSku.trim()))
		{
			/*String envCode = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.ENVIRONMENT_CODE);
			//String companyCode = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.COMPANY_CODE);
			String legacyCustomerNumber = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.LEGACY_CUST_NUMBER);
			String shipFromBranch = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.SHIP_FROM_BRANCH);
			String custDivision = (String) wcContext.getWCAttribute(XPEDXConstants.CUSTOMER_DIVISION,WCAttributeScope.LOCAL_SESSION);*/
			
			Iterator<String> itemIdIterator = itemIds.iterator();
			Document xpxItemXRefDoc = getXpxItemCustXRefDoc(itemIds, wcContext);
			while(itemIdIterator.hasNext()) {
				String itemId = itemIdIterator.next();
				/*//Fetch the customer Part Number
				HashMap valueMap =  new HashMap<String, String>();
				valueMap.put("/XPXItemcustXref/@EnvironmentCode", envCode);
				//valueMap.put("/XPXItemcustXref/@CompanyCode", companyCode);
				valueMap.put("/XPXItemcustXref/@CustomerNumber", legacyCustomerNumber);
				valueMap.put("/XPXItemcustXref/@LegacyItemNumber", itemId);
				valueMap.put("/XPXItemcustXref/@CustomerDivision", custDivision);
				
				Element input =  null;
				Element res =  null;
				input = WCMashupHelper.getMashupInput("XPEDXMyItemsListGetCustomersPart", valueMap, wcContext.getSCUIContext());
				
				res = (Element)WCMashupHelper.invokeMashup("XPEDXMyItemsListGetCustomersPart",input , wcContext.getSCUIContext());
				Element itemCustEle 		= SCXmlUtil.getChildElement(res, "XPXItemcustXref");*/
				if(xpxItemXRefDoc!=null) {
					Element itemXref = SCXmlUtil.getElementByAttribute(xpxItemXRefDoc.getDocumentElement(), "XPXItemcustXref", "LegacyItemNumber", itemId);
					if (itemXref != null){
						if(itemId.equals(itemXref.getAttribute("LegacyItemNumber")))
						{
							String customerPartNumber = itemXref.getAttribute("CustomerItemNumber");
							if(customerPartNumber!=null && customerPartNumber.length()>0)
							{
								HashMap<String, String> tmpSkuMap = itemSkuMap.get(itemId);
								if(SCUtil.isVoid(tmpSkuMap))
									skuMap = new HashMap<String, String>();
								else
									skuMap = tmpSkuMap;
								skuMap.put(XPEDXConstants.CUST_SKU_FLAG_FOR_CUSTOMER_ITEM, customerPartNumber);
							}
						}
						itemSkuMap.put(itemId, (HashMap<String, String>)skuMap.clone());
						skuMap.clear();//clearing the sku map for the other Item ids
					}
				}
			}
		}
		return itemSkuMap;
	}
	
	public static Document getXpxItemCustXRefDoc(ArrayList<String> itemIds, IWCContext context) {
		/*String envCode = (String)context.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.ENVIRONMENT_CODE);
		String companyCode = (String)context.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.COMPANY_CODE);
		String legacyCustomerNumber = (String)context.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.LEGACY_CUST_NUMBER);
		String custDivision = (String) context.getWCAttribute(XPEDXConstants.CUSTOMER_DIVISION,WCAttributeScope.LOCAL_SESSION);
		Removed this as the session variables are not set now. Instead getting the customer info from the customer bean
		*/
		
		XPEDXShipToCustomer shipToCustomer = (XPEDXShipToCustomer)getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		
		try {
			if(shipToCustomer==null) {
				setCustomerObjectInCache(getCustomerDetails(context.getCustomerId(),context.getStorefrontId())
						.getDocumentElement());
				shipToCustomer = (XPEDXShipToCustomer)getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);	
			}
		}
		catch (Exception e) {
			log.error("Error fetching or creating the shipToCustomer object in to the session in getXpxItemCustXRefDoc() So returning back null");
			return null;
		}
		
		if(!(itemIds!=null && itemIds.size()>0))
			return null;
		
		if(shipToCustomer!=null) {
			String envCode = shipToCustomer.getExtnEnvironmentCode();
			String legacyCustomerNumber=shipToCustomer.getExtnLegacyCustNumber();
			String custDivision = shipToCustomer.getExtnCustomerDivision();
			HashMap<String,String> valueMap =  new HashMap<String, String>();
			valueMap.put("/XPXItemcustXref/@EnvironmentCode", envCode);
			valueMap.put("/XPXItemcustXref/@CustomerNumber", legacyCustomerNumber);
			//valueMap.put("/XPXItemcustXref/@LegacyItemNumber", itemId); Filled using Complex Query
			valueMap.put("/XPXItemcustXref/@CustomerDivision", custDivision);
			
			Element input =  null;
			Element res =  null;
			try {
				// Commented for Jira 2796. Calling the XPEDXMyItemsListGetCustomersPart to compare customer number and the customerpartnumber.
				//input = WCMashupHelper.getMashupInput("xpedxGetItemCustXRef", valueMap, context.getSCUIContext()); 
				 input = WCMashupHelper.getMashupInput("XPEDXMyItemsListGetCustomersPart", valueMap,  context.getSCUIContext());
				Element complexQuery = input.getOwnerDocument().createElement("ComplexQuery");
				input.appendChild(complexQuery);
				Element Or = input.getOwnerDocument().createElement("Or");
				complexQuery.appendChild(Or);
				Iterator<String> itemIdIter = itemIds.iterator();
				while(itemIdIter.hasNext()) {
					Element expElement = input.getOwnerDocument().createElement("Exp");
					expElement.setAttribute("Name", "LegacyItemNumber");
					expElement.setAttribute("QryType", "EQ");
					expElement.setAttribute("Value", itemIdIter.next());
					SCXmlUtil.importElement(Or, expElement);
				}
				// Commented for Jira 2796. Calling the XPEDXMyItemsListGetCustomersPart to compare customer number and the customerpartnumber.
				//res = (Element)WCMashupHelper.invokeMashup("xpedxGetItemCustXRef",input , context.getSCUIContext());
				res = (Element)WCMashupHelper.invokeMashup("XPEDXMyItemsListGetCustomersPart",input , context.getSCUIContext());
			} catch (CannotBuildInputException e) {
				e.printStackTrace();
				return null;
			}
			return res.getOwnerDocument();
		}
		else
			return null;
	}
	
	public static Document getXPXItemExtnList(ArrayList<String> itemIDList, IWCContext wcContext) throws Exception {
		Document outputDoc = null;
		XPEDXShipToCustomer shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		try {
			if(shipToCustomer==null) {
				setCustomerObjectInCache(getCustomerDetails(wcContext.getCustomerId(),wcContext.getStorefrontId())
						.getDocumentElement());
				shipToCustomer = (XPEDXShipToCustomer)getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);	
			}
		}
		catch (Exception e) {
			log.error("Error fetching or creating the shipToCustomer object in to the session in getXPXItemExtnList() So returning back null");
			return outputDoc;
		}
		String envCode = shipToCustomer.getExtnEnvironmentCode();
		String division =shipToCustomer.getExtnShipFromBranch();
		/*String envCode = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.ENVIRONMENT_CODE);
		String division = (String)wcContext.getWCAttribute(XPEDXConstants.SHIP_FROM_BRANCH);*/
		
		if (YFCCommon.isVoid(itemIDList) || itemIDList.size() <=0) {
			log.error("Atleast one ItemID is required to evaluate the xpedxXPXItemExtnList mashup. Return back to the caller");
			return outputDoc;
		}
		if(YFCCommon.isVoid(division)){
			log.error("Division is required to evaluate the xpedxXPXItemExtnList mashup. Return back to the caller");
			return outputDoc;
		}

		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/XPXItemExtn/@XPXDivision", division);
		valueMap.put("/XPXItemExtn/@EnvironmentID", envCode);

		Element input = WCMashupHelper.getMashupInput("xpedxXPXItemExtnList", valueMap,wcContext.getSCUIContext());
		Document inputDoc = input.getOwnerDocument();
		NodeList inputNodeList = input.getElementsByTagName("Or");
		Element inputNodeListElemt = (Element) inputNodeList.item(0);
		for (int i = 0; i < itemIDList.size(); i++) {
			Document expDoc = YFCDocument.createDocument("Exp").getDocument();
			Element expElement = expDoc.getDocumentElement();
			expElement.setAttribute("Name", "ItemID");
			expElement.setAttribute("Value", itemIDList.get(i));
			inputNodeListElemt.appendChild(inputDoc.importNode(expElement, true));
		}
		Object obj = WCMashupHelper.invokeMashup("xpedxXPXItemExtnList", input,wcContext.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		if (null != outputDoc) {
			log.debug("Output XML for xpedxXPXItemExtnList: " + SCXmlUtil.getString((Element) obj));
		}
		return outputDoc;
	}

	public static boolean isCurrentUserAdmin(WCContext cntx){
		boolean res = false;
		try {
			if	(ResourceAccessAuthorizer.getInstance().isAuthorized(
					"/swc/profile/ManageUserList", cntx)
			) {
	              res = true;
	        } else {
	              res = false;
	        }
		} catch (Exception e) {
			log.error(e.toString());
		}
		
		return res;
	}
	
	public HashMap<String, String> checkForInventory(
			Document itemBranchItemAssociationDoc, String customerDivision,
			String envCode, ArrayList<String> ItemIds) {
		HashMap<String, String> inventoryCheckForItemsMap = new HashMap<String, String>();
		String itemID = "";
		String inventoryChk = "";
		Iterator<String> iterator = ItemIds.iterator();
		while (iterator.hasNext()) {
			itemID = iterator.next();
			Element itemElem = SCXmlUtil.getElementByAttribute(
					itemBranchItemAssociationDoc.getDocumentElement(),
					"Item", "ItemID", itemID);
			ArrayList<Element> xpxItemExtnElems = SCXmlUtil
					.getElementsByAttribute(itemElem,
							"Extn/XPXItemExtnList/XPXItemExtn", "XPXDivision",
							customerDivision);
			Iterator<Element> xpxItemExtnIter = xpxItemExtnElems.iterator();
			while (xpxItemExtnIter.hasNext()) {
				Element xpxItemExtnElem = xpxItemExtnIter.next();
				String xpxItemEnvCode = xpxItemExtnElem
						.getAttribute("EnvironmentID");
				if (xpxItemEnvCode.trim().equalsIgnoreCase(envCode.trim())) {
					inventoryChk = xpxItemExtnElem
							.getAttribute("InventoryIndicator");
					if (inventoryChk.equalsIgnoreCase("W") || inventoryChk.equalsIgnoreCase("I"))
						inventoryCheckForItemsMap.put(itemID, "Y");
					else
						inventoryCheckForItemsMap.put(itemID, "N");
				}
				else
					inventoryCheckForItemsMap.put(itemID, "N");
			}
		}
		return inventoryCheckForItemsMap;
	}

	public HashMap<String, String> getInventoryCheckMap(Document outputDoc, String shipFromBranch, IWCContext wcContext) {
		HashMap<String, String> inventoryCheckForItemsMap = new HashMap<String, String>();
		
		if(null == shipFromBranch || shipFromBranch.equals("")) {
			try {
				shipFromBranch = getCustomerShipFromDivision(wcContext.getCustomerId(), wcContext.getStorefrontId());
			} catch (CannotBuildInputException e) {
				log.error("Unable to get Ship From Branch : " + e.getMessage());
			}
		}
		
		
		NodeList orderLineList = outputDoc.getElementsByTagName("OrderLine");
		Element orderLineElem;
		Element itemDetailsElem;
		Element xpxExtnElem;
		for(int i=0; i<orderLineList.getLength(); i++){
			orderLineElem = (Element)orderLineList.item(i);			
			itemDetailsElem = SCXmlUtil.getChildElement(orderLineElem, "ItemDetails");			
			if(itemDetailsElem !=null)
			{
				String itemId = itemDetailsElem.getAttribute("ItemID");
				Element itemExtnEle = SCXmlUtil.getChildElement(itemDetailsElem, "Extn");
				
				Element itemXPXExtnListEle = null;
				NodeList xpxItemExtnList = null;
				try {
					itemXPXExtnListEle = XMLUtilities.getElement(itemExtnEle, "XPXItemExtnList");					
					xpxItemExtnList = itemXPXExtnListEle.getElementsByTagName("XPXItemExtn");
					
				} catch (XPathExpressionException e) {
					log.error("Unable to get XPXItemExtnList : " + e.getMessage());
				}				
				
				boolean divisionFlag = false;
				String inventoryIndicator = "N";
				for (int j = 0; xpxItemExtnList != null && j < xpxItemExtnList.getLength(); j++){
					xpxExtnElem = (Element) xpxItemExtnList.item(j);									
					// map 
					String division = xpxExtnElem.getAttribute("XPXDivision");
					if(shipFromBranch.equalsIgnoreCase(division)) {
						divisionFlag = true;
						inventoryIndicator = xpxExtnElem.getAttribute("InventoryIndicator").equalsIgnoreCase("W") ||
												xpxExtnElem.getAttribute("InventoryIndicator").equalsIgnoreCase("I") ?"Y":"N";	
						break;
					}
				}
				if(divisionFlag) {
					inventoryCheckForItemsMap.put(itemId, inventoryIndicator);
				} else {
					inventoryCheckForItemsMap.put(itemId, "N");
				}
			}
			else
			{
				Element itemElem = SCXmlUtil.getChildElement(orderLineElem, "Item");
				if(itemElem != null )
				{
					inventoryCheckForItemsMap.put(itemElem.getAttribute("ItemID"), "N");
				}
			}
		}
		return inventoryCheckForItemsMap;
	}
	
	public static Element getMSAPCustomerContactFromContacts(Document  custOutDoc,IWCContext wcContext)
	{
		NodeList custContList=custOutDoc.getDocumentElement().getChildNodes();
		Element custContactListEle=null;
		String msapId=wcContext.getCustomerId();
		if(XPEDXWCUtils.isCustomerSelectedIntoConext(wcContext))
		{
			msapId=XPEDXWCUtils.getLoggedInCustomerFromSession(wcContext);
		}
		for(int i=0;i<custContList.getLength();i++)
		{
			Element custContElem=(Element)custContList.item(i);
			NodeList customerNodeList=custContElem.getElementsByTagName("Customer");
			if(customerNodeList !=null)
			{
				Element custElem=(Element)customerNodeList.item(0);
				if(custElem != null)
				{
					String customerId=custElem.getAttribute("CustomerID");
					if(msapId.equals(customerId))
					{
						String msapKey = custElem.getAttribute("CustomerKey");

						if(!(((String)wcContext.getWCAttribute(LOGGED_IN_CUSTOMER_KEY)!=null) && ((String)wcContext.getWCAttribute(LOGGED_IN_CUSTOMER_KEY)).trim().length()>0))
							wcContext.setWCAttribute(LOGGED_IN_CUSTOMER_KEY,msapKey,WCAttributeScope.LOCAL_SESSION);
						custContactListEle=custContElem;
						break;
					}
				}
			}
		}
		return custContactListEle;
	}

	public static void setYFSEnvironmentVariables(IWCContext wcContext) 
	{
		
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("isPnACall", "true");
			map.put("isDiscountCalculate", "true");
			setYFSEnvironmentVariables(wcContext, map);
	}
	
	/*
	 * Creating a new method which set environments variable for discount calculate only
	 */
	public static void setYFSEnvironmentVariablesForDiscounts(IWCContext wcContext) 
	{
		
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("isDiscountCalculate", "true");
			setYFSEnvironmentVariables(wcContext, map);
	}
	public static String  getFormatPhone(String attributeName)
	{
		if(attributeName!=null && !attributeName.equals("") )
		{
		StringBuffer st = new StringBuffer(attributeName);
		st=st.insert(3, " ");
		st=st.insert(7, "-");		
		attributeName=st.toString();
		}
		return attributeName;
	
	}
	
	public static Document getCustomerDetails(List<String> customerIds,
			String orgCode, String mashupId) throws CannotBuildInputException {
			
		Document outputDoc = null;
		
			if (YFCCommon.isStringVoid(mashupId)) {
				mashupId = customerGeneralInformationMashUp;// this one fetches all the customer details
			}
			if (null == customerIds || customerIds.size() <= 0) {
				log.debug("getCustomerDetails: customerId is a required field. Returning an empty document");
				return outputDoc;
			} else if (null == orgCode || orgCode.length() <= 0) {
				log.debug("getCustomerDetails: orgCode is a required field. Returning an empty document");
				return outputDoc;
			}
			YFCDocument inputDocument = YFCDocument.createDocument("Customer");
			YFCElement documentElement = inputDocument.getDocumentElement();

			IWCContext context = WCContextHelper.getWCContext(ServletActionContext.getRequest());
			
			documentElement.setAttribute("OrganizationCode", orgCode);
			
			YFCElement complexQueryElement = documentElement.createChild("ComplexQuery");
			YFCElement complexQueryOrElement = documentElement.createChild("Or");
			for (int i = 0; i < customerIds.size(); i++) {
				YFCElement expElement = documentElement.createChild("Exp");
				expElement.setAttribute("Name", "CustomerID");
				expElement.setAttribute("Value", customerIds.get(i));
				complexQueryOrElement.appendChild((YFCNode) expElement);
			}
			complexQueryElement.setAttribute("Operator", "AND");
			complexQueryElement.appendChild(complexQueryOrElement);
			Object obj = WCMashupHelper.invokeMashup(mashupId, inputDocument.getDocument().getDocumentElement(), context.getSCUIContext());
			outputDoc = ((Element) obj).getOwnerDocument();
			if (null != outputDoc) {
				log.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
			}
			return outputDoc;	
	}
	
	public static List<String> getAllAssignedShiptoForAUser(String UserId, IWCContext context) {

		List<String> assignedShipTos = new ArrayList<String>();
		
		if(YFCUtils.isVoid(UserId)) {
			log.error("User ID is required to get the customer Assignements. Returning Empty list");
			return assignedShipTos;
		}
		
		if(context==null) {
			log.error("Context is required to get the customer Assignements for the user ID" +UserId+". Returning Empty list");
			return assignedShipTos;
		}
		
		Document inputDoc = SCXmlUtil.createDocument("XPXCustomerAssignmentView");
		inputDoc.getDocumentElement().setAttribute("UserId", UserId);
		try {
			Object obj = WCMashupHelper.invokeMashup("XPEDXGetAllShipToList", inputDoc.getDocumentElement(), context.getSCUIContext());
			Element custAssignedEle = (Element) obj;
			ArrayList<Element> assignedCustElems = SCXmlUtil.getElements(custAssignedEle, "XPXCustomerAssignmentView");
			
			if(assignedCustElems.size()>0) {
				for(int i=0;i<assignedCustElems.size();i++) {
					Element customer = assignedCustElems.get(i);					
					assignedShipTos.add(SCXmlUtil.getAttribute(customer, "ShipToCustomerID"));
				}
			}
								
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error getting the Assigned Ship to for the User "+UserId);
			return assignedShipTos;
		}		
		
		return assignedShipTos;
	}
	
	public static List<String> getAllAssignedShiptoCustomerIdsForAUser(String UserId, IWCContext context) {
		
		List<String> assignedShipTos = new ArrayList<String>();
		List<String> BillToCustomers = new ArrayList<String>();
		List<String> SAPCustomers = new ArrayList<String>();
		List<String> MSAPCustomers = new ArrayList<String>();
		Set<String> ShipToCustomers = new HashSet<String>();
		
		if(YFCUtils.isVoid(UserId)) {
			log.error("User ID is required to get the customer Assignements. Returning Empty list");
			return assignedShipTos;
		}
		
		if(context==null) {
			log.error("Context is required to get the customer Assignements for the user ID" +UserId+". Returning Empty list");
			return assignedShipTos;
		}
		
		try {
			Document inputDoc = SCXmlUtil.createDocument("CustomerAssignment");
			inputDoc.getDocumentElement().setAttribute("UserId", UserId);
			
			Object obj = WCMashupHelper.invokeMashup("xpedx-getCustomerAssignments", inputDoc.getDocumentElement(), context.getSCUIContext());
			Element custAssignedDoc = (Element) obj;
			
			NodeList assignedCustomers = custAssignedDoc.getElementsByTagName("Customer");
			//Adding the customers to the appropriate list based on the suffix type.
			for(int i=0;i<assignedCustomers.getLength();i++) {
				Element customerElem = (Element) assignedCustomers.item(i);
				if(customerElem!=null) {
					String CustomerId = customerElem.getAttribute("CustomerID");
					Element extnElem = SCXmlUtil.getChildElement(customerElem, "Extn");
					if(extnElem!=null) {
						String suffixType = extnElem.getAttribute("ExtnSuffixType");
						if(XPEDXConstants.BILL_TO_CUSTOMER_SUFFIX_TYPE.equalsIgnoreCase(suffixType))
							BillToCustomers.add(CustomerId);
						else if(XPEDXConstants.SAP_CUSTOMER_SUFFIX_TYPE.equalsIgnoreCase(suffixType))
							SAPCustomers.add(CustomerId);
						else if(XPEDXConstants.MASTER_CUSTOMER_SUFFIX_TYPE.equalsIgnoreCase(suffixType))
							MSAPCustomers.add(CustomerId);
						else if(XPEDXConstants.SHIP_TO_CUSTOMER_SUFFIX_TYPE.equalsIgnoreCase(suffixType))
							ShipToCustomers.add(CustomerId);
					}
				}
			}
			assignedShipTos.addAll(ShipToCustomers);
			List<String> shipTos1 = getShipTos(BillToCustomers, XPEDXConstants.BILL_TO_CUSTOMER_SUFFIX_TYPE, context);
			List<String> shipTos2 = getShipTos(SAPCustomers, XPEDXConstants.SAP_CUSTOMER_SUFFIX_TYPE, context);
			List<String> shipTos3 = getShipTos(MSAPCustomers, XPEDXConstants.MASTER_CUSTOMER_SUFFIX_TYPE, context);
			
			ShipToCustomers.addAll(shipTos1);
			ShipToCustomers.addAll(shipTos2);
			ShipToCustomers.addAll(shipTos3);
			assignedShipTos.addAll(ShipToCustomers);
		}
		catch (Exception e) {
			log.error("Error getting the Assigned Ship to Customer for the User "+UserId);
			return assignedShipTos;
		}
		return assignedShipTos;
		
	}
	
	public static List<String> getShipTos(List<String> customerIds, String CustomerSuffixType, IWCContext wcContext) {
		List<String> assignedShipTos = new ArrayList<String>();
		Set<String> shipTos = new HashSet<String>();
		if(customerIds == null || customerIds.size()<=0) {
			log.error("Customer Id Cannot be Null or Empty. Returning Empty List");
			return assignedShipTos;
		}
		if(CustomerSuffixType == null) {
			log.error("Customer Suffix Type Cannot be Null or Empty. Returning Empty List");
			return assignedShipTos;
		}
		if(wcContext == null) {
			log.error("Context Cannot be Null or Empty. Returning Empty List");
			return assignedShipTos;
		}
		String AttributeToQry = "SAPCustomerID";
		
		if(XPEDXConstants.BILL_TO_CUSTOMER_SUFFIX_TYPE.equalsIgnoreCase(CustomerSuffixType))
			AttributeToQry = "BillToCustomerID";
		else if(XPEDXConstants.SAP_CUSTOMER_SUFFIX_TYPE.equalsIgnoreCase(CustomerSuffixType))
			AttributeToQry = "SAPCustomerID";
		else if(XPEDXConstants.MASTER_CUSTOMER_SUFFIX_TYPE.equalsIgnoreCase(CustomerSuffixType))
			AttributeToQry = "MSAPCustomerID";
		else if(XPEDXConstants.SHIP_TO_CUSTOMER_SUFFIX_TYPE.equalsIgnoreCase(CustomerSuffixType))
			AttributeToQry = "ShipToCustomerID";
		
		for(int i=0; i<customerIds.size();i++) {
			Document inputDoc = SCXmlUtil.createDocument("XPXCustHierachyView");
			inputDoc.getDocumentElement().setAttribute(AttributeToQry, customerIds.get(i));		
			
			Object obj = WCMashupHelper.invokeMashup("xpedx-getAssignedShipTos-View", inputDoc.getDocumentElement(), wcContext.getSCUIContext());
			
			Element outputElement = ((Element)obj);
			
			NodeList customerHierView = outputElement.getElementsByTagName("XPXCustHierarchyView");
			for(int j=0;j<customerHierView.getLength();j++) {
				Element customerHierViewElem = (Element)customerHierView.item(j);
				String shipToCustomerID = customerHierViewElem.getAttribute("ShipToCustomerID");
				shipTos.add(shipToCustomerID);
			}			
		}
		assignedShipTos.addAll(shipTos);
		return assignedShipTos;
	}
	
	public static String getSAPCustomerId(String shipToCustomerId, IWCContext wcContext) {
		String sapCustomerId = "";
		if(YFCCommon.isVoid(shipToCustomerId)) {
			shipToCustomerId = wcContext.getCustomerId();
		}
		
		if(wcContext == null) {
			log.error("Context Cannot be Null or Empty. Returning Empty String");
			return sapCustomerId;
		}
		
		Document inputDoc = SCXmlUtil.createDocument("XPXCustHierachyView");
		inputDoc.getDocumentElement().setAttribute("ShipToCustomerID", shipToCustomerId);
		
		Object obj = WCMashupHelper.invokeMashup("xpedx-getAssignedShipTos-View", inputDoc.getDocumentElement(), wcContext.getSCUIContext());
		
		Element outputElement = ((Element)obj);
		
		NodeList customerHierView = outputElement.getElementsByTagName("XPXCustHierarchyView");
		for(int j=0;j<customerHierView.getLength();j++) {
			Element customerHierViewElem = (Element)customerHierView.item(j);
			sapCustomerId = customerHierViewElem.getAttribute("SAPCustomerID");			
		}
		
		return sapCustomerId;
	}
	
	public static Document getCustomerExtensions(String customerId, String orgCode) {
		
		Document outputDoc = null;
		
		if (null == customerId || customerId.length() <= 0) {
			log.debug("getCustomerExtensions: customerId is a required field. Returning an empty document");
			return outputDoc;
		} else if (null == orgCode || orgCode.length() <= 0) {
			log.debug("getCustomerExtensions: orgCode is a required field. Returning an empty document");
			return outputDoc;
		}
		
		try {
			outputDoc = getCustomerDetails(customerId, orgCode, extnFieldsInformationMashUp);
		} catch (CannotBuildInputException e) {
			log.error("Unable to get Customer Details. " + e.getMessage());
		}
		
		return outputDoc;
	}
	
	public static Document getPaginatedShipTosForMIL(String CustomerID, String customerSuffixType, String pageNumber, String pageSize, String pageSetToken, IWCContext wcContext) {
		Document outDoc = null;
		//if page number and page size are not passed we take the default, pagenumber=1 and pageSize =25
		Integer pageNumberToCheck,pageSizeToCheck;
		pageNumberToCheck = Integer.parseInt(pageNumber);
		pageSizeToCheck = Integer.parseInt(pageSize);
		if(!(pageNumber!=null && pageNumber.trim().length()>0 && pageNumberToCheck>0))
			pageNumber="1";
		if(!(pageSize!=null && pageSize.trim().length()>0 && pageSizeToCheck>0))
			pageSize="25";
		//if the customer Id is not passed the returning the null document
		if(!(CustomerID!=null && CustomerID.trim().length()>0)) {
			log.error("Customer ID is not Passed for the methos getPaginatedShipTos: So returnig null document");
			return outDoc;
		}
		//if the suffix type is not passed or is empty or null taking the suffix type from the customerId
		if(!(customerSuffixType!=null && customerSuffixType.trim().length()>0)) {
			try {
				customerSuffixType = XPEDXWCUtils.getCustomerExtnSuffixType(CustomerID,wcContext.getStorefrontId());
			}
			catch (Exception e) {
				log.error("Error getting the suffix type of customer" );
				e.printStackTrace();
				//setting the suffix type to bill to
				customerSuffixType = XPEDXConstants.BILL_TO_CUSTOMER_SUFFIX_TYPE;
			}
		}
		String AttributeToQry = "BillToCustomerID";
		
		if(XPEDXConstants.BILL_TO_CUSTOMER_SUFFIX_TYPE.equalsIgnoreCase(customerSuffixType))
			AttributeToQry = "BillToCustomerID";
		else if(XPEDXConstants.SAP_CUSTOMER_SUFFIX_TYPE.equalsIgnoreCase(customerSuffixType))
			AttributeToQry = "SAPCustomerID";
		else if(XPEDXConstants.MASTER_CUSTOMER_SUFFIX_TYPE.equalsIgnoreCase(customerSuffixType))
			AttributeToQry = "MSAPCustomerID";
		else if(XPEDXConstants.SHIP_TO_CUSTOMER_SUFFIX_TYPE.equalsIgnoreCase(customerSuffixType))
			AttributeToQry = "ShipToCustomerID";
		HashMap<String,String> valueMap = new HashMap<String, String>();
		valueMap.put("/Page/@PageNumber", pageNumber);
		valueMap.put("/Page/@PageSize", pageSize);
		valueMap.put("/Page/@PageSetToken", pageSetToken);
		valueMap.put("/Page/API/Input/XPXCustHierarchyView/@"+AttributeToQry, CustomerID);
		valueMap.put("/Page/API/Input/XPXCustHierarchyView/OrderBy/Attribute/@Name", "ShipToCustomerID");
		try {
			Element input = WCMashupHelper.getMashupInput("xpedx-getPaginatedAssignedShipTosView-MIL", valueMap, wcContext);
			Object obj = WCMashupHelper.invokeMashup("xpedx-getPaginatedAssignedShipTosView-MIL", input, wcContext.getSCUIContext());
			if(obj!= null)
				outDoc = ((Element)obj).getOwnerDocument();
		}
		catch (Exception e) {
			e.printStackTrace();
			log.error("Error getting the Ship to");
			return outDoc;
		}
		return outDoc;
		
	}
	
//New function - to be used
	public static void setYFSEnvironmentVariables(IWCContext wcContext,ArrayList<Element> assignedCustomers,ArrayList<Element> availableCustomers) 
	{
		
			HashMap<String, ArrayList<Element>> map = new HashMap<String, ArrayList<Element>>();
			map.put("assignedCustomers", assignedCustomers);
			map.put("availableCustomers", availableCustomers);
			XPEDXWCUtils.setYFSEnvironmentVariables(wcContext, map);
	}
	
	public static Document getPaginatedCustomers(String rootCustomerKey,String userID, String pageNumber, String pageSize, String pageSetToken, IWCContext wcContext,
			ArrayList<Element> assignedCustomers,ArrayList<Element> availableCustomers) {
		Document outDoc = null;
		Object obj = null;
		//if page number and page size are not passed we take the default, pagenumber=1 and pageSize =25
	
		HashMap<String,String> valueMap = new HashMap<String, String>();
		valueMap.put("/Page/@PageNumber", pageNumber);
		valueMap.put("/Page/@PageSize", pageSize);
		valueMap.put("/Page/@PageSetToken", pageSetToken);
		/*//valueMap.put("/Page/API/Input/XPXCustView/@"+AttributeToQry, CustomerID);
		valueMap.put("/Page/API/Input/XPXCustView/@RootCustomerKey", rootCustomerKey);
		valueMap.put("/Page/API/Input/XPXCustView/@UserID", userID);
		valueMap.put("/Page/API/Input/XPXCustView/OrderBy/Attribute/@Name", "CustomerPath");*/
		try {
//			Element input = WCMashupHelper.getMashupInput("xpedx-getPaginatedAssignedShipTosView-MIL", valueMap, wcContext);
//			Object obj = WCMashupHelper.invokeMashup("xpedx-getPaginatedAssignedShipTosView-MIL", input, wcContext.getSCUIContext());
			//Kubra-28Aug - Jira 4146
			/*if(newcustomersListFomSession!=null && newcustomersListFomSession.size()>0 ){
				//System.out.println("newcustomersListFomSession********************"+newcustomersListFomSession.size());
				
				Element custViewElement=(Element)input.getElementsByTagName("XPXCustView").item(0);
				if(custViewElement != null)
				{
					Element complexQuery =SCXmlUtil.createChild(custViewElement, "ComplexQuery") ;
					Element OrElem = SCXmlUtil.createChild(complexQuery, "Or");
					for(int i=0;i <newcustomersListFomSession.size(); i++) {
						Element exp = SCXmlUtil.createChild(OrElem, "Exp");
						exp.setAttribute("Name", "CustomerID");
						exp.setAttribute("QryType", "NE");
						exp.setAttribute("Value", newcustomersListFomSession.get(i));
						OrElem.appendChild(exp);
					}
				}
		        
			}*/
			Element input = WCMashupHelper.getMashupInput("xpedx-getPaginatedAvailableLocations", valueMap, wcContext);
			/*List<String> newcustomersListFomSession= (List<String>)XPEDXWCUtils.getObjectFromCache("newcustomersList");
			List<String> newcustomersListFomSession1= (List<String>)XPEDXWCUtils.getObjectFromCache("newcustomersList1");
			*/
			setYFSEnvironmentVariables(wcContext,assignedCustomers,availableCustomers);
			obj = WCMashupHelper.invokeMashup("xpedx-getPaginatedAvailableLocations", input, wcContext.getSCUIContext());
			//Element ele1=(Element)outDoc.getElementsByTagName("XPXCustView").item(0);
			if(obj!= null)
				outDoc = ((Element)obj).getOwnerDocument();
			
			/*Element ele2= (Element) outDoc.getDocumentElement();
			ArrayList<Element> assignedCustElems = SCXmlUtil.getElements(ele2, "/Output/XPXCustViewList/XPXCustView");
			if(assignedCustElems!= null && assignedCustElems.size()>0) {
				for(int i=0;i<assignedCustElems.size();i++) {
					Element customer = assignedCustElems.get(i);
					String customerID = SCXmlUtil.getAttribute(customer, "CustomerID");
					if(newcustomersListFomSession!= null && newcustomersListFomSession.contains(customerID))
						SCXmlUtil.removeNode(customer);
						//ele2.removeChild(customer);
				}
			}*/
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
			log.error("Error getting the Ship to");
			return outDoc;
		}
		return outDoc;
		
	}
	
	
	
	public static Element getXPXCustomerContactExtn(IWCContext wcContext, String customerContactId)
	{
		String msapCustomerKey = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXWCUtils.LOGGED_IN_CUSTOMER_KEY);
		if(msapCustomerKey == null){
			log.error("MSAP Customer Key is not in session");
			return null;
		}
		
		if(customerContactId == null){
			log.error("Customer Contact ID is null..");
			return null;
		}
			
		Element custContactListEle =  null;
		
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/XPXCustomercontactExtn/@CustomerKey", msapCustomerKey);
		valueMap.put("/XPXCustomercontactExtn/@CustomerContactID", customerContactId);
		Element input1;
		Element custContactExtnEle = null;
		try {
			input1 = WCMashupHelper.getMashupInput("getXPXCustomerContactExtn",
					valueMap, wcContext.getSCUIContext());

			custContactExtnEle = (Element) WCMashupHelper.invokeMashup("getXPXCustomerContactExtn",
					input1, wcContext.getSCUIContext());
			
		} catch (CannotBuildInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return custContactExtnEle;
	}
	
	public static Element updateXPXCustomerContactExtn(IWCContext wcContext,String customerContactId, boolean create, Map<String, String> attributeMap)
	{
		String msapCustomerKey = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXWCUtils.LOGGED_IN_CUSTOMER_KEY);
		if(msapCustomerKey == null){
			log.error("MSAP Customer Key is not in session");
			return null;
		}
		if(attributeMap == null || attributeMap.isEmpty())
			return null;
			
		Element custContactListEle =  null;
		
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/XPXCustomercontactExtn/@CustomerKey", msapCustomerKey);
		valueMap.put("/XPXCustomercontactExtn/@CustomerContactID", customerContactId);
		
		Set keySet =  attributeMap.keySet();
		Iterator<String> attrIter = keySet.iterator();
		while(attrIter.hasNext())
		{
			String key = attrIter.next();
			valueMap.put("/XPXCustomercontactExtn/@"+key, attributeMap.get(key));
		}
		String mashupId="changeXPXCustomerContactExtn";
		if(create)
			mashupId = "createXPXCustomerContactExtn";
		Element input1;
		Element custContactExtnEle = null;
		try {
			input1 = WCMashupHelper.getMashupInput(mashupId,
					valueMap, wcContext.getSCUIContext());

			custContactExtnEle = (Element) WCMashupHelper.invokeMashup(mashupId,
					input1, wcContext.getSCUIContext());

			//EB-475, 1521 started here
			if(attributeMap.get(XPEDXConstants.XPX_CUSTCONTACT_EXTN_TC_ACCEPTED_ON_ATTR) != null && XPEDXWCUtils
					.getObjectFromCache("CustomerContExtnEle") == null && custContactExtnEle != null)
			{
				
				XPEDXWCUtils.setObectInCache("CustomerContExtnEle", custContactExtnEle);
				XPEDXWCUtils.setObectInCache("CustomerContactRefKey",custContactExtnEle.getAttribute("CustContRefKey"));
			}
			//EB-475, 1521 ended here
			
		} catch (CannotBuildInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return custContactExtnEle;
	}
	
	public static Document getPaginatedAssignedCustomersDocument(IWCContext context) {
		
		String customerContactId = context.getLoggedInUserId();
		Map<String,String> valueMap = new HashMap<String,String>();
		valueMap.put("/Page/API/Input/XPXCustomerAssignmentView/@UserId", customerContactId);
		valueMap.put("/Page/@PageNumber", "1");
		valueMap.put("/Page/@PageSize", "30");
		valueMap.put("/Page/API/Input/XPXCustomerAssignmentView/OrderBy/Attribute/@Desc", "N");
		valueMap.put("/Page/API/Input/XPXCustomerAssignmentView/OrderBy/Attribute/@Name", "ShipToCustomerID");
//		List<String> assignedShipToList = new ArrayList<String>();
		Element outputElem;
		try {
			Element input = WCMashupHelper.getMashupInput("XPEDXGetPaginatedCustomerAssignments",
					valueMap, context.getSCUIContext());

			outputElem = (Element) WCMashupHelper.invokeMashup("XPEDXGetPaginatedCustomerAssignments",
					input, context.getSCUIContext());
			Element customerAssignment = SCXmlUtil.getChildElement(outputElem, "Output");
//			assignedShipToList = parseForShipToCustomers(customerAssignment);
			List<String> assignedShipToList = XPEDXWCUtils.parseForShipToCustomers(customerAssignment);
			if(assignedShipToList.size()<=20)
				setObectInCache("XPEDX_20_ASSIFNED_SHIPTOS", assignedShipToList);
		} catch (XMLExceptionWrapper e) {
			log.error("Error getting the Customer Assignments");
			e.printStackTrace();
			return null;
		} catch (CannotBuildInputException e) {
			log.error("Error getting the Customer Assignments");
			e.printStackTrace();
			return null;
		}
		catch (Exception e) {
			log.error("Error getting the Customer Assignments");
			e.printStackTrace();
			return null;
		}
		return outputElem.getOwnerDocument();
		
	}
	
	public static List<String> getPaginatedAssignedCustomers(IWCContext context) {
		
		if(context.isGuestUser()){
			return null;
		}
		
		String customerContactId = context.getLoggedInUserId();
		Map<String,String> valueMap = new HashMap<String,String>();
		valueMap.put("/Page/API/Input/XPXCustomerAssignmentView/@UserId", customerContactId);
		valueMap.put("/Page/@PageNumber", "1");
		valueMap.put("/Page/@PageSize", "25");
		valueMap.put("/Page/API/Input/XPXCustomerAssignmentView/OrderBy/Attribute/@Desc", "N");
		valueMap.put("/Page/API/Input/XPXCustomerAssignmentView/OrderBy/Attribute/@Name", "ShipToCustomerID");
		List<String> assignedShipToList = new ArrayList<String>();
		try {
			Element input = WCMashupHelper.getMashupInput("XPEDXGetPaginatedCustomerAssignments",
					valueMap, context.getSCUIContext());

			Element outputElem = (Element) WCMashupHelper.invokeMashup("XPEDXGetPaginatedCustomerAssignments",
					input, context.getSCUIContext());
			Element customerAssignment = SCXmlUtil.getChildElement(outputElem, "Output");
			assignedShipToList = parseForShipToCustomers(customerAssignment);
		} catch (XMLExceptionWrapper e) {
			log.error("Error getting the Customer Assignments");
			e.printStackTrace();
		} catch (CannotBuildInputException e) {
			log.error("Error getting the Customer Assignments");
			e.printStackTrace();
		}
		catch (Exception e) {
			log.error("Error getting the Customer Assignments");
			e.printStackTrace();
		}
		return assignedShipToList;
		
	}
	
	public static List<String> parseForShipToCustomers(Element customerAssignmentViewElem) {
		Element viewListElem = SCXmlUtil.getChildElement(customerAssignmentViewElem, "XPXCustomerAssignmentViewList");
		ArrayList<Element> assignedCustElems = SCXmlUtil.getElements(viewListElem, "XPXCustomerAssignmentView");
		ArrayList<String> assignedShipToList = new ArrayList<String>();
		if(assignedCustElems.size()>0) {
			for(int i=0;i<assignedCustElems.size();i++) {
				Element customer = assignedCustElems.get(i);
				String shipToCustomerID = SCXmlUtil.getAttribute(customer, "ShipToCustomerID");
				if(!assignedShipToList.contains(shipToCustomerID))
					assignedShipToList.add(shipToCustomerID);
			}
		}
		return assignedShipToList;
		
	}

	public static Document getOrderList() {
		Document outputDoc1 = null;
		IWCContext wcContext = WCContextHelper.getWCContext(ServletActionContext.getRequest());
		Map<String, String> valueMap1 = new HashMap<String, String>();
		valueMap1.put("/Order/@EnterpriseCode", wcContext.getStorefrontId());
		valueMap1.put("/Order/@BuyerOrganizationCode",wcContext.getCustomerId());
		Element input1;
		try {
			input1 = WCMashupHelper.getMashupInput("xpedxDraftOrderDropDown",
					valueMap1, wcContext.getSCUIContext());

			Object obj1 = WCMashupHelper.invokeMashup("xpedxDraftOrderDropDown",
					input1, wcContext.getSCUIContext());

			outputDoc1 = ((Element) obj1).getOwnerDocument();

		} catch (CannotBuildInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return outputDoc1;
	}


	/**
	 * 
	 * HISTORY : ritesh : Method added for ad juggler (aj_server, aj_keyword).. 
	 * 			 preddy : Updated to add prefix for the ad juggler keyword(s).
	 * 
	 * @param itemId
	 * @param org
	 * @return
	 */
	public static String getCatTwoDescFromItemId(String itemId, String org) {
		String categoryValue = null;
		String result = null;
		String cat = null;
		String categoryId = null;
		String adjugglerKeywordPrefix = "";
		ISCUITransactionContext scuiTransactionContext = null;
		IWCContext context = null;
		SCUIContext wSCUIContext = null;
		
    		
    	
		
		
		log.debug(" getCatTwoDescFromItemId  - Item Id " + itemId  + " org " + org );
		try 
		{
			adjugglerKeywordPrefix = getAdJugglerKeywordPrefix();
			
			context = WCContextHelper.getWCContext(ServletActionContext
					.getRequest());
			wSCUIContext = context.getSCUIContext();

		YFCDocument getItemListInXML = YFCDocument
		.createDocument("Item");
		
		YFCElement itemListEle = getItemListInXML.getDocumentElement();
		itemListEle.setAttribute("ItemID", itemId);
		itemListEle.setAttribute("OrganizationCode", org);
		/*
		 * getItemList(); Input --> <Item ItemID="5160134"
		 * OrganizationCode="xpedx"> </Item> out put --> <ItemList> <Item>
		 * <CategoryList> <Category/> </CategoryList> </Item> </ItemList> //
		 */

		YFCDocument template = YFCDocument
				.getDocumentFor("<ItemList>" + "<Item>" + "<CategoryList>"
						+"<Category/>"
						+ "</CategoryList>" + "</Item>" + "</ItemList>");

		scuiTransactionContext = wSCUIContext
	.getTransactionContext(true);
		//itemDoc = api.executeFlow(env, "XPXGetItemList", getItemListInXML.getDocument());
		
		YFCElement yfcElement = SCUIPlatformUtils.invokeXAPI("getItemList",
				getItemListInXML.getDocumentElement(),
				template.getDocumentElement(), wSCUIContext);

		YFCElement itemEle = yfcElement.getFirstChildElement();
		YFCElement categoryListEle = itemEle.getFirstChildElement();
		YFCElement categoryEle = categoryListEle.getFirstChildElement();
		categoryId = categoryEle.getAttribute("CategoryPath");
		}
		finally {
			if (scuiTransactionContext != null) {
				SCUITransactionContextHelper.releaseTransactionContext(
						scuiTransactionContext, wSCUIContext);
			}
		}

		try{
			context = WCContextHelper.getWCContext(ServletActionContext
					.getRequest());
			wSCUIContext = context.getSCUIContext();
			StringTokenizer st = new StringTokenizer(categoryId, "/");
			if(st.hasMoreTokens())
			{
				st.nextToken();
				if(st.hasMoreTokens())
				{
					st.nextToken();
					if(st.hasMoreTokens())
						cat = st.nextToken();
				}
			}
			
			if(null != cat)
			{
				YFCDocument getCategoryListInXML = YFCDocument
				.createDocument("Category");
				
				YFCElement categoryLstEle = getCategoryListInXML.getDocumentElement();
				categoryLstEle.setAttribute("CategoryID", cat);
				categoryLstEle.setAttribute("OrganizationCode", org);
	
				
				/*Input : <Category CategoryID="300131"  OrganizationCode="xpedx"></Category>
				 * 
				 * Output : <CategoryList ><Category CategoryID="" CategoryKey="" CategoryPath="" Description="" ShortDescription="" ></Category></CategoryList>
				 */
				YFCDocument template2 = YFCDocument
				.getDocumentFor("<CategoryList ><Category /></CategoryList>");
	
				YFCElement yfcElement2 = SCUIPlatformUtils.invokeXAPI("getCategoryList",
						categoryLstEle, template2.getDocumentElement(), wSCUIContext);
	
				YFCElement catEle = yfcElement2.getFirstChildElement();
				result = catEle.getAttribute("ShortDescription");
				
				//JIRA-2890 
				result = adjugglerKeywordPrefix + result;
			}
			
			log.debug(" getCatTwoDescFromItemId  - result " + result);
		}catch (Exception ex) {
			log.error(ex.getMessage());
			scuiTransactionContext.rollback();
		} finally {
			if (scuiTransactionContext != null) {
				SCUITransactionContextHelper.releaseTransactionContext(
						scuiTransactionContext, wSCUIContext);
			}
		}	
		
			result = sanitizeAJKeywords(result);
			
			return result;
	}

	/*Added for performance issue for JIRA 3593 - newSearch action*/
	public static String getCatTwoDescFromItemIdForpath(String itemId, String org,String path) {
		String categoryValue = null;
		String result = null;
		String cat = null;
		String categoryId = null;
		String adjugglerKeywordPrefix = "";
		ISCUITransactionContext scuiTransactionContext = null;
		IWCContext context = null;
		SCUIContext wSCUIContext = null;
		
		log.debug(" getCatTwoDescFromItemId  - Item Id " + itemId  + " org " + org );
		try 
		{
			adjugglerKeywordPrefix = getAdJugglerKeywordPrefix();
			
				context = WCContextHelper.getWCContext(ServletActionContext
					.getRequest());
			wSCUIContext = context.getSCUIContext();
	   if(path==null || (path!=null && path.trim().equalsIgnoreCase(""))) {
			 
		   YFCDocument getItemListInXML = YFCDocument
		   .createDocument("Item");
		
		   YFCElement itemListEle = getItemListInXML.getDocumentElement();
		   itemListEle.setAttribute("ItemID", itemId);
		   itemListEle.setAttribute("OrganizationCode", org);
		/*
		 * getItemList(); Input --> <Item ItemID="5160134"
		 * OrganizationCode="xpedx"> </Item> out put --> <ItemList> <Item>
		 * <CategoryList> <Category/> </CategoryList> </Item> </ItemList> //
		 */

		   YFCDocument template = YFCDocument
				.getDocumentFor("<ItemList>" + "<Item>" + "<CategoryList>"
						+"<Category/>"
						+ "</CategoryList>" + "</Item>" + "</ItemList>");

		   scuiTransactionContext = wSCUIContext
		   .getTransactionContext(true);
		   //itemDoc = api.executeFlow(env, "XPXGetItemList", getItemListInXML.getDocument());
		
		   YFCElement yfcElement = SCUIPlatformUtils.invokeXAPI("getItemList",
				getItemListInXML.getDocumentElement(),
				template.getDocumentElement(), wSCUIContext);

		   YFCElement itemEle = yfcElement.getFirstChildElement();
		   YFCElement categoryListEle = itemEle.getFirstChildElement();
		   YFCElement categoryEle = categoryListEle.getFirstChildElement();
		   categoryId = categoryEle.getAttribute("CategoryPath");
		   path=categoryId;
	   }
	   else	
		   categoryId = path;
	}// end of try
	finally {
			if (scuiTransactionContext != null) {
				SCUITransactionContextHelper.releaseTransactionContext(
						scuiTransactionContext, wSCUIContext);
			}
	}
			
	try{
			context = WCContextHelper.getWCContext(ServletActionContext
					.getRequest());
			wSCUIContext = context.getSCUIContext();
			scuiTransactionContext = wSCUIContext
			.getTransactionContext(true);
			StringTokenizer st = new StringTokenizer(categoryId, "/");
			if(st.hasMoreTokens())
			{
				st.nextToken();
				if(st.hasMoreTokens())
				{
					st.nextToken();
					if(st.hasMoreTokens())
						cat = st.nextToken();
				}
			}
			
			if(null != cat)
			{
				/*Added for performance filter.action - getting the category desc on basis of category id*/
				Document catObjFromCache = (Document)context.getSCUIContext().getLocalSession().getAttribute("categoryCache");
				if(catObjFromCache !=null){
					Element outXml = catObjFromCache.getDocumentElement();
					Element categoryList = XMLUtilities
					.getChildElementByName(outXml, "CategoryList");
				
					if(categoryList != null)
					{
						Map<String,String> catMap = getCategoryMap(categoryList);
						result = getCategoryShortDesc(catMap,cat);
						
					}
				} 
				if(result==null || (result!=null && result.trim().equalsIgnoreCase(""))){
				/*End of performance*/
					
					YFCDocument getCategoryListInXML = YFCDocument
					.createDocument("Category");
				
				YFCElement categoryLstEle = getCategoryListInXML.getDocumentElement();
				categoryLstEle.setAttribute("CategoryID", cat);
				categoryLstEle.setAttribute("OrganizationCode", org);
	
				
				/*Input : <Category CategoryID="300131"  OrganizationCode="xpedx"></Category>
				 * 
				 * Output : <CategoryList ><Category CategoryID="" CategoryKey="" CategoryPath="" Description="" ShortDescription="" ></Category></CategoryList>
				 */
				YFCDocument template2 = YFCDocument
				.getDocumentFor("<CategoryList ><Category /></CategoryList>");
	
				YFCElement yfcElement2 = SCUIPlatformUtils.invokeXAPI("getCategoryList",
						categoryLstEle, template2.getDocumentElement(), wSCUIContext);
	
				YFCElement catEle = yfcElement2.getFirstChildElement();
				result = catEle.getAttribute("ShortDescription");
				
				//JIRA-2890 
				}
				result = adjugglerKeywordPrefix + result;
			}
			
			log.debug(" getCatTwoDescFromItemId  - result " + result);
		}catch (Exception ex) {
			log.error(ex.getMessage());
			scuiTransactionContext.rollback();
		} finally {
			if (scuiTransactionContext != null) {
				SCUITransactionContextHelper.releaseTransactionContext(
						scuiTransactionContext, wSCUIContext);
			}
		}	
		
			result = sanitizeAJKeywords(result);
			
			return result;
	}

	/*Performance - filter.action
	 * Getting all the list of categoryIds and short description from input CategoryList element*/
	public static Map<String,String> getCategoryMap(Element outCatListElem){
		Map<String, String> Categories = new LinkedHashMap<String, String>();
		Element catElem;
		NodeList categoryList;
		Element childCatElem;
		Element categoryEle;
		String catID;
		String sDesc;
		//SCXmlUtils.getString(categoryList);
		
		if (outCatListElem == null) {
			return Categories;
		}
		NodeList cat = outCatListElem.getChildNodes();
		int numCats = cat.getLength();
		int numChildCats =0;
		for (int i = 0; i < numCats; i++) {
			catElem = (Element) cat.item(i);
			 catID = catElem.getAttribute("CategoryID");
			 sDesc = catElem.getAttribute("ShortDescription");
			Categories.put(catID, sDesc);
			//Retrieving child categories
			NodeList childCat = catElem.getChildNodes();
			categoryList = childCat.item(0).getChildNodes();
			numChildCats = categoryList.getLength();
			for(int j=0;j<numChildCats; j++){
				childCatElem = (Element) categoryList.item(j);
				 catID = childCatElem.getAttribute("CategoryID");
				 sDesc = childCatElem.getAttribute("ShortDescription");
				 Categories.put(catID, sDesc);
			}
		}
		return Categories;
	}
	
	public static String getCategoryShortDesc(Map<String,String> Categories, String catId){
		String categoryShortDesc="";
		if(Categories != null){
			categoryShortDesc = Categories.get(catId);
		}
		return categoryShortDesc;
		
	}
	/*End of Performance - filter.action*/
	
	
	/**
	 * preddy: This methods sanitized ad_Jugler keywords before sending.
	 * It replaces space, comma, / , & 
	 * This is done for all keywords all environments before sending to AJ_Server.
	 * 
	 * @param ajkeyword
	 * @return
	 */
	public static String sanitizeAJKeywords(String ajkeyword) {
		
		log.debug("Ad_Jugler Keword Before sanitize: " + ajkeyword  );
		
		if(ajkeyword !=null && ajkeyword.length() > 0 ) {
		//Replace Comma, Space,/, &, '
		ajkeyword = ajkeyword.replaceAll(",", "");
		ajkeyword = ajkeyword.replaceAll(" ", "");
		ajkeyword = ajkeyword.replaceAll("&", "");
		ajkeyword = ajkeyword.replaceAll("/", "");
		ajkeyword = ajkeyword.replaceAll("'", "");
		//ajkeyword = ajkeyword.replaceAll("\"", "");
		}
		
		log.debug("Ad_Jugler Keword After sanitize: " + ajkeyword  );
		
		return ajkeyword;
	}

	/**
	 * 
	 *  HISTORY : JIRA - 2890 :  preddy - Updated to add prefix for the adJuggler keyword.
	 *  		  All AdJuggler keywords for PROD should send as is, For all other environments need to prefix with TEST.
	 * jira 2890 - made this method public so that it can return value to XPEDXCalatolLanding.jsp
	 * @return
	 */
	public static String getAdJugglerKeywordPrefix() {
		String keywordPrefix = "";
		try{
			
			keywordPrefix = YFSSystem.getProperty(XPEDXConstants.AD_JUGGLER_KEYWORD_PREFIX_PROP);
	    	if(keywordPrefix == null){
	    		keywordPrefix="";
	    	}
			else
			{
			   	keywordPrefix = keywordPrefix.trim();	    		
			}
	    	
	    	
		}catch(Exception e){
    		log.debug("AD_JUGGLER Failed to get Prefix : (Property : yfs.xpedx.adjuggler.keyword.attribute.prefix) , Message : " + e.getMessage() );
    	}
		
		
		
		return keywordPrefix;
	}

	public static boolean getReportsFlagForLoggedInUser(IWCContext wcContext){
		
		boolean viewReports = false;
		//String viewReportFlag = SCXmlUtil.getAttribute(extnElem, "ExtnViewReportsFlag");
		XPEDXCustomerContactInfoBean xpedxCustomerContactInfoBean = (XPEDXCustomerContactInfoBean)getObjectFromCache(XPEDXConstants.XPEDX_Customer_Contact_Info_Bean);
		String viewReportsFlag = xpedxCustomerContactInfoBean.getExtnViewReportsFlag();
//		String viewReportsFlag = (String) wcContext.getSCUIContext().getSession().getAttribute("viewReportsFlag");
		if("Y".equalsIgnoreCase(viewReportsFlag)) {
			viewReports = Boolean.TRUE;
		}
		
		return viewReports;
	}
	
	public static boolean getInvoiceFlagForLoggedInUser(IWCContext wcContext)
	{
		boolean viewInvoice=false;
		String viewInvoiceFlag=null;
		XPEDXCustomerContactInfoBean custContBean=(XPEDXCustomerContactInfoBean)XPEDXWCUtils.getObjectFromCache("XPEDX_Customer_Contact_Info_Bean");
		viewInvoiceFlag =custContBean.getExtnViewInvoices();
		if(viewInvoiceFlag == null)
		{
			Document doc = XPEDXWCUtils.getCustomerContactDetails();
			Element contactElem = XPEDXWCUtils.getMSAPCustomerContactFromContacts(doc, wcContext);
			if(contactElem!=null) {			
				Element extnElem = SCXmlUtil.getChildElement(contactElem, "Extn");
				viewInvoiceFlag = SCXmlUtil.getAttribute(extnElem, "ExtnViewInvoices");
				custContBean.setExtnViewInvoices(viewInvoiceFlag);
				setObectInCache("XPEDX_Customer_Contact_Info_Bean", custContBean);
			}
		}

		if(viewInvoiceFlag.equals("Y"))
		{
			viewInvoice=true;
		}
		return viewInvoice;
	}
	
	/*public static Map<String,Map<String,Element>> getOrderLineFromChangeOrderXML(IWCContext wcContext,String orderHeaderKey)
	{
		Map<String,Map<String,Element>> orderMap=new HashMap<String,Map<String,Element>>();
		UtilBean util=new UtilBean();
		try
		{
			Map<String,Element> orderLineMap=new HashMap<String,Element>();
			Map<String,Element> orderHeaderMap=new HashMap<String,Element>();
			HashMap<String, String> valueMap = new HashMap<String, String>();
			valueMap.put("/XPXUeAdditionalAttribsXml/@OrderHeaderKey", orderHeaderKey);
			valueMap.put("/XPXUeAdditionalAttribsXml/@XMLType", "ChangeOrder");
			Element input=null;
			try {
				input = WCMashupHelper.getMashupInput(
						"xpedx_get_pna_responsexml", valueMap, wcContext
								.getSCUIContext());
			} catch (CannotBuildInputException e) {
				// Error in invoking mashup
				//return custInfoMap;
			}
			Object obj = WCMashupHelper.invokeMashup(
					"xpedx_get_pna_responsexml", input, wcContext
							.getSCUIContext());
			if(obj != null)
			{
				Element allChildDoc = ((Element) obj);
				String changeOrderXML=allChildDoc.getAttribute("ResponseXML");
				Document changeOrderDoc=SCXmlUtil.createFromString(changeOrderXML);
				Element changeOrderElem=changeOrderDoc.getDocumentElement();
				Element orderExtn=util.getElements(changeOrderElem, "Extn").get(0);
				
				Document orderDoc=SCXmlUtil.createDocument("Order");
				Element orderElement=orderDoc.getDocumentElement();
				orderElement.setAttribute("OrderHeaderKey", changeOrderElem.getAttribute("OrderHeaderKey"));
				Element orderExtnElem=SCXmlUtil.createChild(orderElement, "Extn");
				orderExtnElem.setAttribute("ExtnOrderCouponDiscount", orderExtn.getAttribute("ExtnOrderCouponDiscount"));
				orderExtnElem.setAttribute("ExtnOrderDiscount", orderExtn.getAttribute("ExtnOrderDiscount"));
				orderExtnElem.setAttribute("ExtnOrderSubTotal", orderExtn.getAttribute("ExtnOrderSubTotal"));
				orderExtnElem.setAttribute("ExtnTotOrdValWithoutTaxes", orderExtn.getAttribute("ExtnTotOrdValWithoutTaxes"));
				orderExtnElem.setAttribute("ExtnTotOrderAdjustments", orderExtn.getAttribute("ExtnTotOrderAdjustments"));
				orderExtnElem.setAttribute("ExtnTotalOrderValue", orderExtn.getAttribute("ExtnTotalOrderValue"));
				orderElement.appendChild(orderExtnElem);
				
				orderHeaderMap.put(orderHeaderKey, orderElement);
				List<Element> orderLines=SCXmlUtil.getElements(changeOrderElem, "OrderLines/OrderLine");
				
				if(orderLines.size()>0)
				{
					for(Element orderLineEle : orderLines)
					{
						String orderLineKey=orderLineEle.getAttribute("OrderLineKey");
						orderLineMap.put(orderLineKey, orderLineEle);
					}
				}
			}
			orderMap.put("OrderLines", orderLineMap);
			orderMap.put("OrderHeader", orderHeaderMap);
		}
		catch(Exception e)
		{
			log.error("Error getting Change Order document "+e.getMessage());
		}
		return orderMap;
	}*/

	public static void setEditedOrderHeaderKeyInSession(IWCContext wcContext,String OrderHeaderKey)
	{
		if(!YFCCommon.isVoid(OrderHeaderKey))
		{
			wcContext.getSCUIContext()
				.getSession().setAttribute(XPEDXConstants.EDITED_ORDER_HEADER_KEY, OrderHeaderKey);
		}
		
		
		
	}
	
	public static String getEditedOrderHeaderKeyFromSession(IWCContext wcContext) {
		Object editedOrderheaderKey = wcContext.getSCUIContext()
				.getSession().getAttribute(XPEDXConstants.EDITED_ORDER_HEADER_KEY);
		if (editedOrderheaderKey != null) {
			return (String) editedOrderheaderKey;
		}
		return null;
	}
	
	public static void resetPendingChanges(String orderHeaderKey,IWCContext wcContext)
	{
		
		try
		{
			Map<String,Element> orderLineMap=new HashMap<String,Element>();
			Map<String,Element> orderHeaderMap=new HashMap<String,Element>();
			HashMap<String, String> valueMap = new HashMap<String, String>();
			valueMap.put("/Order/@OrderHeaderKey", orderHeaderKey);
			valueMap.put("/Order/PendingChanges/@ResetPendingChanges", "Y");
			Element input=null;
			try {
				input = WCMashupHelper.getMashupInput(
						"ResetPendingOrder", valueMap, wcContext
								.getSCUIContext());
			} catch (CannotBuildInputException e) {
				// Error in invoking mashup
				//return custInfoMap;
			}
			WCMashupHelper.invokeMashup(
					"ResetPendingOrder", input, wcContext
							.getSCUIContext());
		}
		catch(Exception e)
		{
			log.error("Error while reseting pending changes "+e.getMessage());
		}
	}
	public static String replaceSemiWithcomma(String emailAddress) {
		try{
			emailAddress = emailAddress.replace(";", ",");
			if(emailAddress.charAt(emailAddress.length()-1) == ',')
				emailAddress = emailAddress.substring(0, emailAddress.length()-1);
		}catch(Exception e) {
			log.error("Error while reseting pending changes "+e.getMessage());
		}
		return emailAddress;
	}
	
	public static String getInvoiceNoWithoutDate(String invoiceNo) {
		return invoiceNo.substring(9, invoiceNo.length());
	}
	
	public static String getDateFromInvoiceNo(String invoiceNo) {
		return invoiceNo.substring(0, 8);
	}
	
	public static String getUnformattedDate(String inputFormat, String date) {
		String outputFormat = "yyyyMMdd";
		YDate yDate = new YDate(date, inputFormat, true);
		return yDate.getString(outputFormat);
	}
	
	public static String encrypt(String plaintext) throws Exception {
		Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(sharedkeyfinal, "DESede"),
				new IvParameterSpec(sharedvector));
		byte[] encrypted = c.doFinal(plaintext.getBytes("UTF-8"));
		return Base64.encode(encrypted);
	}

	public static String decrypt(String ciphertext) throws Exception {
		Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(sharedkeyfinal, "DESede"),
				new IvParameterSpec(sharedvector));
		byte[] decrypted = c.doFinal(Base64.decode(ciphertext));
		return new String(decrypted, "UTF-8");
	}

	public static SecretKey getSecretKey(byte[] encryptionKey) {
		SecretKey secretKey = null;
		if (encryptionKey == null)
			return null;

		byte[] keyValue = new byte[24]; // final 3DES key  

		if (encryptionKey.length == 16) {
			// Create the third key from the first 8 bytes  
			System.arraycopy(encryptionKey, 0, keyValue, 0, 16);
			System.arraycopy(encryptionKey, 0, keyValue, 16, 8);

		} else if (encryptionKey.length != 24) {
			throw new IllegalArgumentException(
					"A TripleDES key should be 24 bytes long");

		} else {
			keyValue = encryptionKey;
		}
		DESedeKeySpec keySpec;
		try {
			keySpec = new DESedeKeySpec(keyValue);
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance("DESede");
			secretKey = keyFactory.generateSecret(keySpec);
		} catch (Exception e) {
			throw new RuntimeException("Error in key Generation", e);
		}
		return secretKey;
	}
	
	/*
	 * Small test case for CommaSeparated Quantity Format
	 */
	public static void main(String args[]){
		 //Testing Comma separated qty-LP1-
		/* String originalQty1 = "1234567890" ;
		 String originalQty = "ABCefgHIJklmNOPqrsTUVwxy" ;
		 String commaFmtQty =  commaSeparatedStringFmt( originalQty );
		 
		 */
		

		 
		 
	}	
	
	public static Object getObjectFromCache(String attributeName)
	{
		IWCContext wcContext = WCContextHelper.getWCContext(ServletActionContext.getRequest());
		return wcContext.getSCUIContext().getRequest().getSession().getAttribute(attributeName);
	}
	
	public static Object getObjectFromCache(String attributeName,boolean getUsingMashup,String mashupId,HashMap<String, String> valueMap)
	{
		IWCContext wcContext = WCContextHelper.getWCContext(ServletActionContext.getRequest());
		Object obj=null;
		if(getUsingMashup == true)
		{
			try
			{
			
				Element input=null;
					input = WCMashupHelper.getMashupInput(
							mashupId, valueMap, wcContext
									.getSCUIContext());
				obj=WCMashupHelper.invokeMashup(
						mashupId, input, wcContext
								.getSCUIContext());
			
			}
			catch (CannotBuildInputException e)
			{
				log.error(" Error in building input for mashup :"+mashupId+"  ::: "+e.getMessage());
				//return custInfoMap;
			}
			catch(Exception e)
			{
				log.error(" Error in invoking mashup "+mashupId+"  ::: "+e.getMessage());
			}
		}
		else
		{
			wcContext.getSCUIContext().getRequest().getSession().getAttribute(attributeName);
		}
		return obj;
	}
	
	public static void removeObectFromCache(String attributeName)
	{
		IWCContext wcContext = WCContextHelper.getWCContext(ServletActionContext.getRequest());
		wcContext.getSCUIContext().getRequest().getSession().removeAttribute(attributeName);
	}
	
	public static void setObectInCache(String attributeName,Object value)
	{
		IWCContext wcContext = WCContextHelper.getWCContext(ServletActionContext.getRequest());
		wcContext.getSCUIContext().getRequest().getSession().setAttribute(attributeName, value);
	}
	
	public static void setSAPCustomerExtnFieldsInCache()
	{
		setSAPCustomerExtnFieldsInCache(null);
	}
	
	public static void setSAPCustomerExtnFieldsInCache(Element orderElement)
	{
		IWCContext wcContext = WCContextHelper.getWCContext(ServletActionContext.getRequest());
		
		String defaultShipToChanged = (String)getObjectFromCache(XPEDXConstants.CUSTOM_FIELD_FLAG_CHANGE);
		String isSapStillNeedToChange=(String)getObjectFromCache(XPEDXConstants.IS_SAP_STILL_NEED_TO_CHANGE);
		try
		{
			if(YFCUtils.isVoid(defaultShipToChanged) || "true".endsWith(defaultShipToChanged) || "Y".equalsIgnoreCase(isSapStillNeedToChange)){
				// do this only when the default ship to is changed and when logging in for the first time.
				Document SAPCustomerDoc = XPEDXOrderUtils.getSAPCustomerExtnFlagsDoc(wcContext);
				LinkedHashMap customerFieldsMap = new LinkedHashMap();
				Document rulesDoc = (Document) wcContext.getWCAttribute("rulesDoc");
				Document orderDoc = SCXmlUtil.createDocument("Order");
				Element orderEle = orderDoc.getDocumentElement();
				orderEle.setAttribute("BuyerOrganizationCode", wcContext.getCustomerId());
				orderEle.setAttribute("EnterpriseCode", wcContext.getStorefrontId());
				orderEle.setAttribute("EntryType", "Web");
				Element orderLineEle=SCXmlUtil.createChild(orderEle, "OrderLines");
				SCXmlUtil.createChild(orderLineEle, "OrderLine");
				ArrayList<String> customerFieldsRulesMap =null;
				
					if(rulesDoc == null)
					{
						rulesDoc = XPEDXOrderUtils.getValidationRulesForCustomer(orderEle, wcContext);
						wcContext.setWCAttribute("rulesDoc", rulesDoc, WCAttributeScope.LOCAL_SESSION);	
					}
					if(orderElement == null)
					{
						customerFieldsRulesMap = XPEDXOrderUtils.getRequiredCustomerFieldMap(orderEle,rulesDoc, wcContext);
					}
					else
					{
						customerFieldsRulesMap = XPEDXOrderUtils.getRequiredCustomerFieldMap(orderElement,rulesDoc, wcContext);
					}
					
					if (customerFieldsRulesMap != null && customerFieldsRulesMap.contains("CustomerPONo")) 
					{
						if(!customerFieldsMap.containsKey("CustomerPONo"))
						customerFieldsMap.put("CustomerPONo", "Line PO #");
					}
					if (customerFieldsRulesMap != null && customerFieldsRulesMap.contains("ExtnCustLineAccNo")) 
					{
						if(!customerFieldsMap.containsKey("CustLineAccNo"))
						customerFieldsMap.put("CustLineAccNo", "Line Account #");
					}
					customerFieldsMap.putAll((LinkedHashMap) XPEDXOrderUtils.getSAPCustomerLineFieldMap(SAPCustomerDoc.getDocumentElement()));
					
					setObectInCache("customerFieldsSessionMap", customerFieldsMap);
					
					setObectInCache("sapCustExtnFields", createSAPCustomerDoc(SAPCustomerDoc.getDocumentElement(),"SAP",customerFieldsRulesMap));
					// reset the flag once used
					setObectInCache(XPEDXConstants.CUSTOM_FIELD_FLAG_CHANGE,"false");
					removeObectFromCache(XPEDXConstants.IS_SAP_STILL_NEED_TO_CHANGE);
					//wcContext.setWCAttribute(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED,"false",WCAttributeScope.LOCAL_SESSION);
			}
		}
		catch(Exception e)
		{
			log.error("Error while setting SAP Custom fields in Cache :: "+e.getMessage());
		}
	}
	
	public static Document createSAPCustomerDoc(Element sapCustomerElement,String prefix,ArrayList<String> customerFieldsRulesMap)
	{
		Document sapCustomerDocument=SCXmlUtil.createDocument("Customer");
		try
		{
			
			Element outputsapCustomerElement=sapCustomerDocument.getDocumentElement();
			Element sapCustomerExtnElem=SCXmlUtil.createChild(outputsapCustomerElement, "Extn");
			Element customerOrganizationExtnEle=(Element)(sapCustomerElement.getElementsByTagName("XPXCustHierarchyView")).item(0);
			outputsapCustomerElement.setAttribute("CustomerID", customerOrganizationExtnEle.getAttribute("SAPCustomerID"));
			if (customerFieldsRulesMap != null && customerFieldsRulesMap.contains("ExtnCustLineAccNo")) 
			{
				sapCustomerExtnElem.setAttribute("ExtnCustLineAccNoFlag", "Y");
			}
			else
			{
				sapCustomerExtnElem.setAttribute("ExtnCustLineAccNoFlag", SCXmlUtil.getAttribute(
						customerOrganizationExtnEle, prefix+"ExtnCustLineAccNoFlag"));
			}
			if (customerFieldsRulesMap != null && customerFieldsRulesMap.contains("CustomerPONo")) {
				sapCustomerExtnElem.setAttribute("ExtnCustLinePONoFlag","Y");
			}
			else
			{			
				sapCustomerExtnElem.setAttribute("ExtnCustLinePONoFlag",SCXmlUtil.getAttribute(
						customerOrganizationExtnEle, prefix+"ExtnCustLinePONoFlag"));
			}
			//Fix for not showing Seq Number as per Pawan's mail dated 17/3/2011
			/*String custSeqNoFlag = SCXmlUtil.getAttribute(
					customerOrganizationExtnEle, prefix+"ExtnCustLineSeqNoFlag");*/
			sapCustomerExtnElem.setAttribute("ExtnCustLineField1Flag",SCXmlUtil.getAttribute(
					customerOrganizationExtnEle, prefix+"ExtnCustLineField1Flag"));
			sapCustomerExtnElem.setAttribute("ExtnCustLineField2Flag",SCXmlUtil.getAttribute(
					customerOrganizationExtnEle, prefix+"ExtnCustLineField2Flag"));
			sapCustomerExtnElem.setAttribute("ExtnCustLineField3Flag",SCXmlUtil.getAttribute(
					customerOrganizationExtnEle, prefix+"ExtnCustLineField3Flag"));
			sapCustomerExtnElem.setAttribute("ExtnCustLineAccLbl",SCXmlUtil.getAttribute(
						customerOrganizationExtnEle, prefix+"ExtnCustLineAccLbl"));
			sapCustomerExtnElem.setAttribute("ExtnCustLinePOLbl",SCXmlUtil.getAttribute(customerOrganizationExtnEle, prefix+"ExtnCustLinePOLbl"));//XB 769 \ XB 434
			sapCustomerExtnElem.setAttribute("ExtnCustLineField1Label",SCXmlUtil.getAttribute(
						customerOrganizationExtnEle, prefix+"ExtnCustLineField1Label"));
			sapCustomerExtnElem.setAttribute("ExtnCustLineField2Label",SCXmlUtil.getAttribute(
						customerOrganizationExtnEle, prefix+"ExtnCustLineField2Label"));
			sapCustomerExtnElem.setAttribute("ExtnCustLineField3Label",SCXmlUtil.getAttribute(
						customerOrganizationExtnEle, prefix+"ExtnCustLineField3Label"));
		}
		catch(Exception e)
		{
			log.error("Error while crating SAP Custom fields :: "+e.getMessage());
		}
		return sapCustomerDocument;
	}
	
	public static XPEDXShipToCustomer setCustomerObjectInCache(Element customerOrganizationEle) {
		XPEDXShipToCustomer shipToCustomer=new XPEDXShipToCustomer();
		try {
			IWCContext wcContext = WCContextHelper.getWCContext(ServletActionContext.getRequest());
			//added for jira 4306 - preffered shipto value was vanishing on click of edit order button
			String editedOrderHeaderKey=XPEDXWCUtils.getEditedOrderHeaderKeyFromSession(wcContext);
			//added for jira 4306
			if(!YFCCommon.isVoid(editedOrderHeaderKey) || XPEDXWCUtils.getObjectFromCache("EDIT_ORDER_FINISHED") != null)
			{
				XPEDXShipToCustomer defaultShipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
				shipToCustomer.setDefaultShipToCustomer(defaultShipToCustomer.getDefaultShipToCustomer());
				XPEDXWCUtils.removeObectFromCache("EDIT_ORDER_FINISHED");//added for jira 4306
			}
			setObectInCache("shipToCustomer", shipToCustomer);
			//end of jira 4306
			XPEDXShipToCustomer billToCustomer=new XPEDXShipToCustomer();
			String customerId = wcContext.getCustomerId();
			String organizationCode = wcContext.getStorefrontId();
			if (customerId != null && organizationCode != null) {
				// set the logged in users Customer Name(Buyer Org's). This is
				// displayed in the Header - RUgrani
				Element sBuyerOrg = SCXmlUtil.getChildElement(
						customerOrganizationEle, "BuyerOrganization");
				shipToCustomer.setOrganizationName(sBuyerOrg
						.getAttribute("OrganizationName"));
				Element extnElement = SCXmlUtil.getChildElement(
						customerOrganizationEle, "Extn");
				Element personInfoElement = SCXmlUtil.getXpathElement(
						customerOrganizationEle, "//Customer/CustomerAdditionalAddressList/CustomerAdditionalAddress/PersonInfo");
				Element customerPaymentInfoElement = SCXmlUtil.getXpathElement(
						customerOrganizationEle, "//Customer/CustomerPaymentMethodList"); //CustomerPaymentMethod
				Element parentElem = SCXmlUtil.getChildElement(
						customerOrganizationEle, "ParentCustomer");
				XPEDXWCUtils.getShipToAddressOfCustomer(customerOrganizationEle,shipToCustomer);
				shipToCustomer.setRootCustomerKey(customerOrganizationEle.getAttribute("RootCustomerKey"));
				shipToCustomer.setCustomerKey(customerOrganizationEle.getAttribute("CustomerKey"));
				shipToCustomer.setExtnAllowDirectOrderFlag(customerOrganizationEle.getAttribute("ExtnAllowDirectOrderFlag"));
				shipToCustomer.setCustomerStatus(customerOrganizationEle.getAttribute("Status"));
				shipToCustomer.setExtnCustomerClass(extnElement.getAttribute("ExtnCustomerClass"));
				shipToCustomer.setExtnShipFromBranch(extnElement.getAttribute("ExtnShipFromBranch"));
				shipToCustomer.setExtnUseCustSKU(extnElement.getAttribute("ExtnUseCustSKU"));
				shipToCustomer.setExtnIndustry(extnElement.getAttribute("ExtnIndustry"));
				shipToCustomer.setExtnEnvironmentCode(extnElement.getAttribute("ExtnEnvironmentCode"));
				shipToCustomer.setExtnCompanyCode(extnElement.getAttribute("ExtnCompanyCode"));
				shipToCustomer.setExtnLegacyCustNumber(extnElement.getAttribute("ExtnLegacyCustNumber"));
				shipToCustomer.setExtnMinOrderAmount(extnElement.getAttribute("ExtnMinOrderAmount"));
				shipToCustomer.setExtnMinChargeAmount(extnElement.getAttribute("ExtnMinChargeAmount"));
				shipToCustomer.setExtnShipComplete(extnElement.getAttribute("ExtnShipComplete"));
				shipToCustomer.setExtnOrigEnvironmentCode(extnElement.getAttribute("ExtnOrigEnvironmentCode"));
				shipToCustomer.setExtnCustOrderBranch(extnElement.getAttribute("ExtnCustOrderBranch"));
				shipToCustomer.setExtnCustomerDivision(extnElement.getAttribute("ExtnCustomerDivision"));
				shipToCustomer.setExtnCurrencyCode(extnElement.getAttribute("ExtnCurrencyCode"));
				shipToCustomer.setExtnCustomerName(extnElement.getAttribute("ExtnCustomerName"));
				shipToCustomer.setExtnSampleRequestFlag(extnElement.getAttribute("ExtnSampleRequestFlag"));
				shipToCustomer.setLocationID(extnElement.getAttribute("ExtnCustomerStoreNumber"));
				//For performance issue itemdetail.action
				shipToCustomer.setExtnUseOrderMulUOMFlag(extnElement.getAttribute("ExtnUseOrderMulUOMFlag"));
				shipToCustomer.setExtnPrimarySalesRep(extnElement.getAttribute("ExtnPrimarySalesRep"));
				shipToCustomer.setExtnShipToSuffix("ExtnShipToSuffix");
				shipToCustomer.setFirstName(personInfoElement.getAttribute("FirstName"));
				shipToCustomer.setMiddleName(personInfoElement.getAttribute("MiddleName"));
				shipToCustomer.setLastName(personInfoElement.getAttribute("LastName"));
				shipToCustomer.setEMailID(personInfoElement.getAttribute("EMailID"));
				shipToCustomer.setExtnSampleRoomEmailAddress(personInfoElement.getAttribute("ExtnSampleRoomEmailAddress"));
				shipToCustomer.setDayPhone(personInfoElement.getAttribute("DayPhone"));
				shipToCustomer.setCity(personInfoElement.getAttribute("City"));
				shipToCustomer.setState(personInfoElement.getAttribute("State"));
				shipToCustomer.setCompany(personInfoElement.getAttribute("Company"));
				shipToCustomer.setExtnPriceWareHouse(extnElement.getAttribute("ExtnPriceWarehouse"));
				List<String> arrAddress = new ArrayList<String>();
				String sAddr = "AddressLine";
				
					for (int i = 1; i <= 6; i++) {
						if(personInfoElement.getAttribute(sAddr + i).equalsIgnoreCase(""))
							arrAddress.add(" ");
						else
							arrAddress.add(personInfoElement.getAttribute(sAddr + i));								
					}
				shipToCustomer.setAddressList(arrAddress);				
				shipToCustomer.setZipCode(personInfoElement.getAttribute("ZipCode"));
				
				if(Integer.parseInt(customerPaymentInfoElement.getAttribute("TotalNumberOfRecords")) > 0) {
					NodeList payNodes = customerPaymentInfoElement.getElementsByTagName("CustomerPaymentMethod");				
					if (payNodes != null && payNodes.getLength() > 0) {
						Element payMethod = (Element) payNodes.item(0);					
						shipToCustomer.setAccountNumber(payMethod.getAttribute("DisplayCustomerAccountNo"));
					}	
				}
				
				
				// end of performance for itemdetail.action
				if(!YFCCommon.isVoid(parentElem))
				{
					billToCustomer.setParentCustomerKey(parentElem.getAttribute("ParentCustomerKey")); //Jira 3162 done Changes
					billToCustomer.setBuyerOrganizationCode(parentElem.getAttribute("BuyerOrganizationCode"));
					shipToCustomer.setParentCustomerKey(parentElem.getAttribute("CustomerKey"));
					billToCustomer.setCustomerStatus(parentElem.getAttribute("Status"));//Added for EB 289
					Element parentExtnElem = SCXmlUtil.getChildElement(parentElem, "Extn");
					billToCustomer.setExtnCurrencyCode(parentExtnElem.getAttribute("ExtnCurrencyCode"));
					billToCustomer.setExtnCustomerDivision(parentExtnElem.getAttribute("ExtnCustomerDivision"));
					billToCustomer.setExtnCustomerName(parentExtnElem.getAttribute("ExtnCustomerName"));
					billToCustomer.setExtnSampleRequestFlag(parentExtnElem.getAttribute("ExtnSampleRequestFlag"));
					billToCustomer.setExtnServiceOptCode(parentExtnElem.getAttribute("ExtnServiceOptCode"));
					billToCustomer.setExtnMinOrderAmount(parentExtnElem.getAttribute("ExtnMinOrderAmount"));
					billToCustomer.setExtnMinChargeAmount(parentExtnElem.getAttribute("ExtnMinChargeAmount"));
					billToCustomer.setCustomerID(parentElem.getAttribute("CustomerID"));					
					billToCustomer.setOrganizationName(SCXmlUtil.getChildElement(parentElem, "BuyerOrganization").getAttribute("OrganizationName"));
					billToCustomer.setExtnECsr1EMailID(parentExtnElem.getAttribute("ExtnECsr1EMailID")); //Jira 3162 done Changes
					billToCustomer.setExtnECsr2EMailID(parentExtnElem.getAttribute("ExtnECsr2EMailID")); //Jira 3162 done Changes
					billToCustomer.setExtnMaxOrderAmount(parentExtnElem.getAttribute("ExtnMaxOrderAmount"));//JIRA 3488
					billToCustomer.setExtnCustomerClass(parentExtnElem.getAttribute("ExtnCustomerClass"));//XBT-265
					/* XB-763 Code Changes Start */
					String extnMfgItemFlag = parentExtnElem.getAttribute("ExtnMfgItemFlag");
					String extnCustomerItemFlag = parentExtnElem.getAttribute("ExtnCustomerItemFlag");
					/* xb-758 Code Changes start */
					wcContext.removeWCAttribute(XPEDXConstants.BILL_TO_CUST_MFG_ITEM_FLAG, WCAttributeScope.LOCAL_SESSION);
					wcContext.removeWCAttribute(XPEDXConstants.BILL_TO_CUST_PART_ITEM_FLAG, WCAttributeScope.LOCAL_SESSION);
					/* xb-758 Code Changes end */
					if(!YFCCommon.isVoid(extnMfgItemFlag) && extnMfgItemFlag.equalsIgnoreCase("Y") ){
						wcContext.setWCAttribute(XPEDXConstants.BILL_TO_CUST_MFG_ITEM_FLAG,extnMfgItemFlag,WCAttributeScope.LOCAL_SESSION);
					}
					
					if(!YFCCommon.isVoid(extnCustomerItemFlag) && extnCustomerItemFlag.equalsIgnoreCase("Y")){
						wcContext.setWCAttribute(XPEDXConstants.BILL_TO_CUST_PART_ITEM_FLAG,extnCustomerItemFlag,WCAttributeScope.LOCAL_SESSION);
					} 						
				      /* XB-763 Code Changes End */
				}
				shipToCustomer.setBillTo(billToCustomer);
				setObectInCache("shipToCustomer", shipToCustomer);
				if(XPEDXWCUtils.getObjectFromCache(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED)== null || "true".equals(XPEDXWCUtils.getObjectFromCache(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED)))
				{
					shipToCustomer.setDefaultShipToCustomer((XPEDXShipToCustomer) shipToCustomer.clone()); //added for jira 4306
					XPEDXWCUtils.setObectInCache(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED, "false");
					
				}
				else
				{	
					// Changes started for preferred ship-to location issue JIRA 4306
					if(XPEDXWCUtils.getObjectFromCache("DEFAULT_SHIP_TO_OBJECT") != null)
					{
						shipToCustomer.setDefaultShipToCustomer((XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache("DEFAULT_SHIP_TO_OBJECT"));
						XPEDXWCUtils.removeObectFromCache("DEFAULT_SHIP_TO_OBJECT");
					}
					//End of changes for preferred ship-to location issue JIRA 4306
				}
			}
		} catch (Exception ex) {
			log.error("Unable to get logged in users Customer Profile. "
					+ ex.getMessage());
		}
		return shipToCustomer;
	}
	
		
	public static void checkMultiStepCheckout() {
			
	    	// read the value: SWC_CHECKOUT_TYPE from session
	    	// if null, then fetch it from database else return the same
		    try
		    {
			IWCContext wcContext = WCContextHelper.getWCContext(ServletActionContext.getRequest());
			String definitionFromSession = (String)wcContext.getWCAttribute("SWC_CHECKOUT_TYPE", WCAttributeScope.LOCAL_SESSION);
			
			// if the checkout type in multi_step change it to single_step.
			if (definitionFromSession==null && isMultiStepCheckout(wcContext)){
				// since the value is being set as valueMap.put("/UserUiState/@Definition", "Single_Step");
				wcContext.setWCAttribute("SWC_CHECKOUT_TYPE","Single_Step", WCAttributeScope.LOCAL_SESSION);
				Map<String, String> valueMap = new HashMap<String, String>();
				valueMap.put("/UserUiState/@ComponentName", "SWC_CHECKOUT_TYPE");
				valueMap.put("/UserUiState/@Definition", "Single_Step");
				valueMap.put("/UserUiState/@Loginid", wcContext.getLoggedInUserId());
				valueMap.put("/UserUiState/@ScreenName", "Webchannel");
				valueMap.put("/UserUiState/@ApplicationName", "swc");
				Element input = WCMashupHelper.getMashupInput("updateUserPreference", valueMap, wcContext.getSCUIContext());
				Element outDoc = (Element)WCMashupHelper.invokeMashup("updateUserPreference",input , wcContext.getSCUIContext());
			}
		    }catch(Exception e)
		    {
		    	log.error("error geting checkout step "+e.getMessage());
		    }
		}
	    private static boolean isMultiStepCheckout(IWCContext wcContext)
	    {
	    	   boolean isMultiStepCheckoutObj=false;
	    	    String definitionFromSession = (String)wcContext.getWCAttribute("SWC_CHECKOUT_TYPE", WCAttributeScope.LOCAL_SESSION);
	           if(!"Single_Step".equals(definitionFromSession))
	           {
		    	    String checkoutType = UserPreferenceUtil.getUserPreference("SWC_CHECKOUT_TYPE", wcContext);
		    	    wcContext.setWCAttribute("SWC_CHECKOUT_TYPE",checkoutType, WCAttributeScope.LOCAL_SESSION);
		            if("Multi_Step".equals(checkoutType))
		                isMultiStepCheckoutObj = Boolean.TRUE;
		            else
		                isMultiStepCheckoutObj = Boolean.FALSE;
	           }
	        return isMultiStepCheckoutObj;
	    }

		public static XPEDXCustomerContactInfoBean setXPEDXCustomerContactInfoBean(Document customerContactDocument, IWCContext wcContext){
			XPEDXCustomerContactInfoBean xpedxCustomerContactInfoBean = new XPEDXCustomerContactInfoBean();
			Element contactElem = XPEDXWCUtils.getMSAPCustomerContactFromContacts(customerContactDocument, wcContext);
			
			if(contactElem!=null) {
				String welcomeUserFirstName = SCXmlUtil.getAttribute(contactElem, "FirstName");
				String welcomeUserLastName = SCXmlUtil.getAttribute(contactElem, "LastName");
				String spendingLimit = SCXmlUtil.getAttribute(contactElem, "SpendingLimit");
				String customerContactID = SCXmlUtil.getAttribute(contactElem, "CustomerContactID");
				String msapEmailID = SCXmlUtil.getAttribute(contactElem, "EmailID");
				String welcomeLocaleId = SCXmlUtil.getXpathAttribute(contactElem, "//CustomerContact/User/@Localecode");
				Element extnElem = SCXmlUtil.getChildElement(contactElem, "Extn");
				String viewInvoiceFlag = SCXmlUtil.getAttribute(extnElem, "ExtnViewInvoices");
				String estimatorFlag = SCXmlUtil.getAttribute(extnElem, "ExtnEstimator");
				String viewReportFlag = SCXmlUtil.getAttribute(extnElem, "ExtnViewReportsFlag");
				String viewPricesFlag = SCXmlUtil.getAttribute(extnElem, "ExtnViewPricesFlag");
				String b2bViewFromDB = SCXmlUtil.getAttribute(extnElem, "ExtnB2BCatalogView");				
				String maxOrderAmt=SCXmlUtil.getAttribute(extnElem, "ExtnMaxOrderAmount");//JIRA 3488 start
				String orderApproveFlag = SCXmlUtil.getAttribute(extnElem, "ExtnOrderApprovalFlag");//added for XB 226
				
				if (b2bViewFromDB != null && b2bViewFromDB.trim().length() > 0) {
					b2bViewFromDB = b2bViewFromDB.trim();
					int _b2bViewFromDB = Integer.parseInt(b2bViewFromDB);
					if (_b2bViewFromDB == XPEDXConstants.XPEDX_B2B_FULL_VIEW) {
						b2bViewFromDB = "normal-view";
					} else if (_b2bViewFromDB == XPEDXConstants.XPEDX_B2B_CONDENCED_VIEW) {
						b2bViewFromDB = "condensed-view";
					} else if (_b2bViewFromDB == XPEDXConstants.XPEDX_B2B_MINI_VIEW) {
						b2bViewFromDB = "mini-view";
					} else {
						b2bViewFromDB = "papergrid-view";
					}
				} else {
					b2bViewFromDB = "papergrid-view";
				}
				boolean usergroupKeyListActive = false;
				boolean isEstimator = false;
				
				Element userElem = SCXmlUtil.getChildElement(contactElem, "User");
				Element approverElem = SCXmlUtil.getElementByAttribute(userElem, "UserGroupLists/UserGroupList", "UsergroupKey", "BUYER-APPROVER");
				
				List<Element> userGroupList = SCXmlUtil.getElements(userElem,"/UserGroupLists/UserGroupList");
				ArrayList<String> newusergroupkey = new ArrayList<String>(); 
				if (userGroupList != null) {
					for (Element userGroup : userGroupList) {
						String userGroupKey = userGroup.getAttribute("UsergroupKey");										
						newusergroupkey.add(userGroupKey);
					}		
					usergroupKeyListActive = true;
				}		
				
				String isApprover = "N";
				if(approverElem!=null) {
					isApprover = "Y";
				}
				if(estimatorFlag.equals("Y")) {
					isEstimator = true;
				}
				
				Element customerElem = SCXmlUtil.getChildElement(contactElem, "Customer");
				Element customerExtnElem = SCXmlUtil.getChildElement(customerElem, "Extn");
				String myItemsLink = SCXmlUtil.getAttribute(customerExtnElem, "ExtnMyItemsLink");
				//Start- Code added to fix XNGTP 2964	
				 String extnUseOrderMulUOMFlag = SCXmlUtil.getAttribute(customerExtnElem, "ExtnUseOrderMulUOMFlag");
				//End- Code added to fix XNGTP 2964	
				 String defaultShipTo = SCXmlUtil.getAttribute(extnElem,	"ExtnDefaultShipTo");
				if (defaultShipTo != null
						&& defaultShipTo.trim().length() == 0) {
					defaultShipTo = null;
				}
				String userPrefCategory = SCXmlUtil.getAttribute(extnElem,"ExtnPrefCatalog");
				String orderConfirmationFalg=SCXmlUtil.getAttribute(extnElem,"ExtnOrderConfEmailFlag");
				String emailID=SCXmlUtil.getAttribute(contactElem,"EmailID");
				String personInfoElement="";
				try
				{
					Element customerAdditionalAddressListElem=SCXmlUtils.getChildElement(contactElem, "CustomerAdditionalAddressList");
					Element customerAdditionalAddressElem=SCXmlUtils.getChildElement(customerAdditionalAddressListElem, "CustomerAdditionalAddress");
					Element personInfoElem=SCXmlUtils.getChildElement(customerAdditionalAddressElem, "PersonInfo");
					
					if(personInfoElem !=null)
					{
						personInfoElement=SCXmlUtil.getAttribute(personInfoElem,"EMailID");
					}
				}
				catch(NullPointerException ne)
				{
					log.error("Error while getting Additional address .");
				}
				
				xpedxCustomerContactInfoBean = 
					new XPEDXCustomerContactInfoBean(welcomeUserFirstName, welcomeUserLastName, customerContactID, msapEmailID,
							welcomeLocaleId, viewInvoiceFlag, isEstimator,
							viewReportFlag, viewPricesFlag,
							newusergroupkey, defaultShipTo,
							userPrefCategory, isApprover, usergroupKeyListActive, myItemsLink, 0 , b2bViewFromDB,orderConfirmationFalg,
							emailID,extnUseOrderMulUOMFlag,personInfoElement,maxOrderAmt,spendingLimit,orderApproveFlag);//added maxOrderAmt for JIRA 3488  added orderApproveFlag xb-226
			}
			XPEDXWCUtils.setObectInCache(XPEDXConstants.XPEDX_Customer_Contact_Info_Bean, xpedxCustomerContactInfoBean);
			return xpedxCustomerContactInfoBean;
		}
		
	public static Element setMiniCartDataInToCache(Element orderElement, IWCContext wcContext)
	{
		/*List<String> itemAndTotalList=new ArrayList<String>();
		String OrderTotal= SCXmlUtil.getXpathAttribute(orderElement, "/Order/Extn/@ExtnTotalOrderValue");    
    	 ArrayList<Element> orderLineList= SCXmlUtil.getElements(orderElement,"OrderLines/OrderLine");
    	 itemAndTotalList.add(OrderTotal);
    	 if(!YFCCommon.isVoid(orderLineList))
    	 {
    		 itemAndTotalList.add(""+orderLineList.size());
    	 }
    	 else
    	 {
    		 itemAndTotalList.add("0");
    	 }
    	 XPEDXWCUtils.setObectInCache("CommerceContextHelperOrderTotal", itemAndTotalList);*/
		XPEDXOrderUtils.refreshMiniCart(wcContext,orderElement,true,false,XPEDXConstants.MAX_ELEMENTS_IN_MINICART);
		return orderElement;
	}
	
	public static XPEDXOrgNodeDetailsBean getShipFromNodeDetails(String orgCode,IWCContext wcContext,Document organizationDoc) {
		
		XPEDXOrgNodeDetailsBean orgDetailsBean = new XPEDXOrgNodeDetailsBean();
		XPEDXShipToCustomer shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		Document outputDoc = null;
		try 
		{
			
			Map<String, String> valueMap = new HashMap<String, String>();
			String customerId=wcContext.getCustomerId();
			String envCode=shipToCustomer.getExtnEnvironmentCode();
			orgDetailsBean=shipToCustomer.getOrganization();
			//String envCode=XPEDXWCUtils.getEnvironmentCode(customerId);
			if(orgDetailsBean == null)
			{
				if(envCode!=null && envCode.trim().length()>0){
					valueMap.put("/Organization/@OrganizationCode", orgCode+"_"+envCode);
				}else{
					valueMap.put("/Organization/@OrganizationCode", orgCode);
				}
				if(organizationDoc == null )
				{
					Element input = WCMashupHelper.getMashupInput(
							"XPEDXGetShipOrgNodeDetails", valueMap, wcContext
									.getSCUIContext());
					Object obj = WCMashupHelper.invokeMashup(
							"XPEDXGetShipOrgNodeDetails", input, wcContext
									.getSCUIContext());
					outputDoc=((Element) obj).getOwnerDocument();
				}
				else
				{
					outputDoc=organizationDoc;
				}
				if (null != outputDoc) {
					Element orgElem, corpElem = null;
					NodeList orgNodes = outputDoc
							.getElementsByTagName("Organization");
					if (orgNodes != null && orgNodes.getLength() > 0) {
						orgElem = (Element) orgNodes.item(0);
						orgDetailsBean.setOrgName(orgElem
								.getAttribute("OrganizationName"));
						corpElem = XMLUtilities.getElement(orgElem,
								"CorporatePersonInfo");
						if (corpElem != null) {
							orgDetailsBean.setAddressLineOne(corpElem
									.getAttribute("AddressLine1"));
							orgDetailsBean.setAddressLineTwo(corpElem
									.getAttribute("AddressLine2"));
							orgDetailsBean.setAddressLineThree(corpElem
									.getAttribute("AddressLine3"));
							orgDetailsBean.setAddressLineFour(corpElem
									.getAttribute("AddressLine4"));
							orgDetailsBean.setAddressLineFive(corpElem
									.getAttribute("AddressLine5"));
							orgDetailsBean.setAddressLineSix(corpElem
									.getAttribute("AddressLine6"));
							orgDetailsBean.setFirstName(corpElem
									.getAttribute("FirstName"));
							orgDetailsBean.setLastName(corpElem
									.getAttribute("LastName"));
							orgDetailsBean.setCity(corpElem.getAttribute("City"));
							orgDetailsBean.setCountry(corpElem
									.getAttribute("Country"));
							orgDetailsBean.setEmailId(corpElem
									.getAttribute("EMailID"));
							orgDetailsBean.setState(corpElem.getAttribute("State"));
							orgDetailsBean.setZipCode(corpElem
									.getAttribute("ZipCode"));
						}
						shipToCustomer.setOrganization(orgDetailsBean);
						XPEDXWCUtils.setObectInCache(XPEDXConstants.SHIP_TO_CUSTOMER, shipToCustomer);
					}
				}
			}
		} catch (CannotBuildInputException ex) {
			log.error("Error while invoking mashup", ex);
		} catch (XPathExpressionException xpEx) {
			log.error("Error accessing Organization Document", xpEx);
		}
		return orgDetailsBean;
	}
	
	 public static Element getCustomerListDetalis(List<String> customerIdList ,IWCContext wcContext) throws Exception
	    {
	        String wCCustomerId;
	        String wCOrganizationCode;
	        Element loggedinCustomer = null;
	        Element inputCustomer = null;
	        wCCustomerId = wcContext.getCustomerId();
	        wCOrganizationCode = wcContext.getStorefrontId();
	        String logInCustRootCustomerKey;
	        String inputCustRootCustomerKey;
	       /* Map valueMapinput = new HashMap();
	        valueMapinput.put("/Customer/@CustomerID", wCCustomerId);
	        valueMapinput.put("/Customer/@OrganizationCode", wCOrganizationCode);*/
	        Element input = WCMashupHelper.getMashupInput("xpedxgetLoggedInCustomer",wcContext);
			input.setAttribute("OrganizationCode", wCOrganizationCode);
			Element complexQuery = SCXmlUtil.getChildElement(input, "ComplexQuery");
			Element OrElem = SCXmlUtil.getChildElement(complexQuery, "Or");
			for(int i=0;i <customerIdList.size(); i++) {
				Element exp = input.getOwnerDocument().createElement("Exp");
				exp.setAttribute("Name", "CustomerID");
				exp.setAttribute("Value", customerIdList.get(i));
				SCXmlUtil.importElement(OrElem, exp);
			}
	        
	        loggedinCustomer = (Element)WCMashupHelper.invokeMashup("xpedxgetLoggedInCustomer", input, wcContext.getSCUIContext());
	        return loggedinCustomer;
	        /*valueMapinput = new HashMap();
	        valueMapinput.put("/Customer/@CustomerID", inputCustomerID);
	        valueMapinput.put("/Customer/@OrganizationCode", wCOrganizationCode);
	        input = null;
	        input = WCMashupHelper.getMashupInput("getLoggedInCustomer", valueMapinput, wcContext);
	        inputCustomer = (Element)WCMashupHelper.invokeMashup("getLoggedInCustomer", input, wcContext.getSCUIContext());
	        inputCustRootCustomerKey = SCXmlUtils.getAttribute(inputCustomer, "RootCustomerKey");
	        if(inputCustRootCustomerKey != null && logInCustRootCustomerKey != null && inputCustRootCustomerKey.equals(logInCustRootCustomerKey))
	            return true;
	        break MISSING_BLOCK_LABEL_263;
	        com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper mashupEx;
	        mashupEx;
	        wcContext.getSCUIContext().replaceAttribute("SCUI_EXCEPTION_ATTR", mashupEx);
	        mashupEx.printStackTrace();
	        break MISSING_BLOCK_LABEL_263;
	        mashupEx;
	        wcContext.getSCUIContext().replaceAttribute("SCUI_EXCEPTION_ATTR", mashupEx);
	        mashupEx.printStackTrace();
	        break MISSING_BLOCK_LABEL_263;
	        mashupEx;
	        wcContext.getSCUIContext().replaceAttribute("SCUI_EXCEPTION_ATTR", mashupEx);
	        mashupEx.printStackTrace();
	        return false;*/
	    }
	 
	 /*
	  * Trim the bullet points that are more than five.
	  */
	 public static String trimItemDescription(String desc) {
		 if(desc != null) {
			 int fromIndex = 0;
			 int startIndex = 0;
			 int counter = 0;
			 while ((startIndex = desc.indexOf("<li>", fromIndex)) >= 0) {
				 if (counter == 5) { counter++; break;}
				 fromIndex = desc.indexOf("</li>", startIndex);
				 counter++; 
				
			}
			 if(counter > 5) 
				 return (desc.substring(0, (fromIndex)) + desc.substring(desc.lastIndexOf("</li>"), desc.length()));
			 else 
				 return desc;
		 }
		 return  desc;
	 }
	 /**
	  * JIRA 3160 START
	  * @param strExtnECsr1EMailID
	  * @param strExtnECsr2EMailID
	  * @return
	  */
	 public static String setCSREmails(String strExtnECsr1EMailID,String strExtnECsr2EMailID) 
	    {
			String strExtnECsrEMailID = "";
		   if((strExtnECsr1EMailID != null && !strExtnECsr1EMailID.equalsIgnoreCase("")) && (strExtnECsr2EMailID != null && !strExtnECsr2EMailID.equalsIgnoreCase(""))){
			   strExtnECsrEMailID = strExtnECsr1EMailID + XPEDXConstants.EMAILIDSEPARATOR + strExtnECsr2EMailID;
			}else if(strExtnECsr1EMailID != null && !strExtnECsr1EMailID.equalsIgnoreCase("")){
				strExtnECsrEMailID = strExtnECsr1EMailID;
				}
			else if(strExtnECsr2EMailID != null && !strExtnECsr2EMailID.equalsIgnoreCase("")){
				strExtnECsrEMailID  = strExtnECsr2EMailID;
			}
			
		 
		  return strExtnECsrEMailID;
		 
		}
	 public static String getSalesRepEmail(String sapCustomerKey,IWCContext wcContext) {
	 		
		 String totalSalesRepEmail = "";
		 
	 	 if (sapCustomerKey != null && !sapCustomerKey.equalsIgnoreCase("")) {
	 		 
	 		Element xpedxSalesRep = SCXmlUtil.createDocument("XPEDXSalesRep").getDocumentElement();
	 		xpedxSalesRep.setAttribute("SalesCustomerKey", sapCustomerKey);  
	 		
	 		Document outputDoc;
			
	 		Object obj = WCMashupHelper.invokeMashup("getXpedxSalesRepList", xpedxSalesRep, wcContext.getSCUIContext());
			outputDoc = ((Element) obj).getOwnerDocument();
	        
	 		
	 		NodeList nodeList  = outputDoc.getElementsByTagName("XPEDXSalesRep");  
			int salesRepLength = nodeList.getLength();
			List<String> salesRepUserKeysList = new ArrayList<String>();
			
			for (int counter = 0; counter < salesRepLength ; counter++) {
				Element salesRepElem = (Element) nodeList.item(counter);
				String salesUserKey = "";
				if (salesRepElem.hasAttribute("SalesUserKey")) {
					salesUserKey = salesRepElem.getAttribute("SalesUserKey");
				}
				if(salesUserKey !=null && salesUserKey.trim().length() > 0)
					salesRepUserKeysList.add(salesUserKey);
			}
			
			if(salesRepUserKeysList.size() > 0){
				try {
				Element inputElem = WCMashupHelper.getMashupInput("getUserListWithContactPersonInfo", wcContext);
				Element complexQueryElem = SCXmlUtil.getChildElement(inputElem, "ComplexQuery");
				Element OrElem = SCXmlUtil.getChildElement(complexQueryElem, "Or");
				Iterator<String> itr = salesRepUserKeysList.iterator();
				while(itr.hasNext()) {
					String userKey = (String)itr.next();
					if(userKey!=null && !userKey.equals("")){
						Element exp = inputElem.getOwnerDocument().createElement("Exp");
						exp.setAttribute("Name", "UserKey");
						exp.setAttribute("Value", userKey);
						SCXmlUtil.importElement(OrElem, exp);
					}
				}
				 
				outputDoc =((Element) WCMashupHelper.invokeMashup("getUserListWithContactPersonInfo", inputElem, wcContext.getSCUIContext())).getOwnerDocument();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				 
				NodeList personInfoList = outputDoc.getElementsByTagName("ContactPersonInfo");
		 		int contactPersonInfoLength = personInfoList.getLength();
	 			for (int counter = 0; counter < contactPersonInfoLength ; counter++) {
	 				Element personInfoElem = (Element) personInfoList.item(counter);
	 				if (personInfoElem != null && personInfoElem.hasAttribute("EmailID")) {
	 					String salesRepEmail = personInfoElem.getAttribute("EmailID");
	 					if (salesRepEmail != null && !salesRepEmail.equalsIgnoreCase("")) {
	 						totalSalesRepEmail = totalSalesRepEmail + XPEDXConstants.EMAILIDSEPARATOR + salesRepEmail;
	 					}
	 				}
	 			}
		 		
		    }	
	 	}
	 	return totalSalesRepEmail;
	 	}
	 
	 public static String getLogoName(String sellerOrgCode)
		{
			String _imageName = "";
			if (XPEDXConstants.XPEDX_LOGO.equalsIgnoreCase(sellerOrgCode)) {
				_imageName = "/xpedx_r_rgb_lo.jpg";
			} else if (XPEDXConstants.BULKLEYDUNTON_LOGO.equalsIgnoreCase(sellerOrgCode)) {
				_imageName = "/BulkleyDunton_r_rgb_lo.jpg";
			} else if (XPEDXConstants.CENTRAILEWMAR_LOG0.equalsIgnoreCase(sellerOrgCode)) {
				_imageName = "/CentralLewmar_r_rgb_lo.jpg";
			} else if (XPEDXConstants.CENTRALMARQUARDT_LOGO.equalsIgnoreCase(sellerOrgCode)) {
				_imageName = "/CentralMarquardt_r_rgb_lo.jpg";
			} else if (XPEDXConstants.SAALFELD_LOGO.equalsIgnoreCase(sellerOrgCode)) {
				_imageName = "/Saalfeld_r_rgb_lo.jpg";
			} else if (XPEDXConstants.STRATEGICPAPER_LOG0.equalsIgnoreCase(sellerOrgCode)) {
				_imageName = "/StrategicPaper_r_rgb_lo.jpg";
			} else if (XPEDXConstants.WESTERNPAPER_LOGO.equalsIgnoreCase(sellerOrgCode)) {
				_imageName = "/WesternPaper_r_rgb_lo.jpg";
			} else if (XPEDXConstants.WHITEMANTOWER_LOGO.equalsIgnoreCase(sellerOrgCode)) {
				_imageName = "/WhitemanTower_r_rgb_lo.jpg";
			} else if (XPEDXConstants.ZELLERBACH_LOGO.equalsIgnoreCase(sellerOrgCode)) {
				_imageName = "/Zellerbach_r_rgb_lo.jpg";
			} else if (XPEDXConstants.XPEDXCANADA_LOGO.equalsIgnoreCase(sellerOrgCode)) {
				_imageName = "/xpedx_r_rgb_lo.jpg";
			} 
			return _imageName;
		}
       /**
        JIRA 3160 END
        */






/* Method created for JIra 2599 ****/

	public static String getCategoryPathPromo(String itemId,String org){

		if(itemId == null || itemId.trim().length() == 0)
			return "";
		IWCContext context = null;
		SCUIContext wSCUIContext = null;

		context = WCContextHelper.getWCContext(ServletActionContext.getRequest());
		wSCUIContext = context.getSCUIContext();
		
		ISCUITransactionContext scuiTransactionContext = null;
		String sCategoryPath="";

		try{
		YFCDocument getItemListInXML = YFCDocument.createDocument("Item");

		YFCElement itemListEle = getItemListInXML.getDocumentElement();
		itemListEle.setAttribute("ItemID", itemId);
		itemListEle.setAttribute("OrganizationCode", org);

		
		YFCDocument template = YFCDocument.getDocumentFor("<ItemList>" + "<Item>" + "<CategoryList>"+"<Category/>"+ "</CategoryList>" + "</Item>" + "</ItemList>");

		scuiTransactionContext = wSCUIContext.getTransactionContext(true);
		YFCElement yfcElement = SCUIPlatformUtils.invokeXAPI("getItemList",	getItemListInXML.getDocumentElement(),template.getDocumentElement(), wSCUIContext);

		YFCElement itemEle = yfcElement.getFirstChildElement();
		YFCElement categoryListEle = itemEle.getFirstChildElement();
		YFCElement categoryEle = categoryListEle.getFirstChildElement();
		sCategoryPath = categoryEle.getAttribute("CategoryPath");


		return sCategoryPath;
		}
		finally {
			if (scuiTransactionContext != null) {
				SCUITransactionContextHelper.releaseTransactionContext(
						scuiTransactionContext, wSCUIContext);
			}
		}
	}


	/* Method created for JIra 2599 ****/

	//changes added for jira 2422 for search within the result
	public static void setItemDetailBackPageURLinSession()
	{
		setItemDetailBackPageURLinSession("");
	}
   
	public static void setItemDetailBackPageURLinSession(String append)
	{
		IWCContext wcContext =WCContextHelper.getWCContext(ServletActionContext.getRequest());
		String backPageURL=(wcContext.getSCUIContext().getRequest().getRequestURL().append("?").append(wcContext.getSCUIContext().getRequest().getQueryString())).toString();
		
		if(append!=null && append.contains("#")){
			append= append.replaceAll("#","%23");
		}
		
		if(append != null && !"".equals(append))
		{
			//String splited[]=backPageURL.split("&scFlag=Y");				
			if(backPageURL != null)		{
				String splited=backPageURL.replaceAll("&scFlag=Y", "");	
				wcContext.getSCUIContext().getSession().setAttribute("itemDtlBackPageURL", splited+append+"&scFlag=Y");
			}
		}
		else
		{
			wcContext.getSCUIContext().getSession().setAttribute("itemDtlBackPageURL", backPageURL);
		}
	}
	
	 public static void logExceptionIntoCent(Throwable ex)
	 {
		 String statckTrace=getStackTrace(ex);
		 logExceptionIntoCent(statckTrace);
	 }
	public static String getStackTrace(Throwable aThrowable) {
	    final Writer result = new java.io.StringWriter();
	    final PrintWriter printWriter = new PrintWriter(result);
	    aThrowable.printStackTrace(printWriter);
	    return result.toString();
	}
   //end of jira 2422 changes
   public static void logExceptionIntoCent(String ExceptionMsg)
	{
		IWCContext wcContext = WCContextHelper.getWCContext(ServletActionContext.getRequest());
		Map<String, String> valueMap1 = new HashMap<String, String>();
		valueMap1.put("/Exception/@StackTrace", ExceptionMsg);		
		Element input1;
		try {		
			
				input1 = WCMashupHelper.getMashupInput("XpedxSwcCentLog",valueMap1, wcContext.getSCUIContext());
				Object obj1 = WCMashupHelper.invokeMashup("XpedxSwcCentLog",input1, wcContext.getSCUIContext());
				Document outputDoc1 = ((Element) obj1).getOwnerDocument();
			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.toString();
			e.toString();
			e.printStackTrace();
		} 
		
	}
	//Jira 3244 reopen method
	public static String userProfileAuthorizeLocationAccount(String custDisplayId){
		String customerInfo=null;
		String[] custDetails = custDisplayId.split(" ");
		if(custDetails!=null)
		{
			String customerNumber = custDetails[1];
			customerInfo = custDisplayId.replaceFirst(customerNumber, "");
			custDisplayId= customerInfo +"("+ customerNumber +")";
		}
		return custDisplayId;
	}
	
	/*Begin - Changes made by Mitesh Parikh for JIRA 3581*/
   /**
	 * This method fetches all the SKUs(Customer Part #, Manufacturer #, MPC #) for a particular xpedx itemId
	 * @param wcContext
	 * @param itemIdList
	 * @param tmpItemSkuMap
	 * @return
	 * @throws CannotBuildInputException
	 * @throws XPathExpressionException
	 */
	public static HashMap<String, HashMap<String, String>> getAllSkusForItem(IWCContext wcContext, ArrayList<String> itemIdList, HashMap<String, HashMap<String,String>> tmpItemSkuMap) throws CannotBuildInputException, XPathExpressionException
	{
		HashMap<String, String> skuMap = new LinkedHashMap<String, String>();
		HashMap<String, HashMap<String,String>> itemSkuMap = new HashMap<String, HashMap<String,String>>();
		Document xpxItemXRefDoc = getXpxItemCustXRefDoc(itemIdList, wcContext);
		// EB-64 - Setting the ItemCustXREF doc in cache so that can retrieve the customerUOM flag for item and UOM for order detail page and Web Conf page
		XPEDXWCUtils.setObectInCache("xpxItemXRefDoc",xpxItemXRefDoc);
		Iterator<String> itemIdIterator = itemIdList.iterator();
		while(itemIdIterator.hasNext()) {
			String itemId = itemIdIterator.next();
			if(xpxItemXRefDoc!=null) {
				Element itemXref = SCXmlUtil.getElementByAttribute(xpxItemXRefDoc.getDocumentElement(), "XPXItemcustXref", "LegacyItemNumber", itemId);
				if (itemXref != null){
					if(itemId.equals(itemXref.getAttribute("LegacyItemNumber")))
					{
						String customerPartNumber = itemXref.getAttribute("CustomerItemNumber");
						if(customerPartNumber!=null && customerPartNumber.length()>0)
						{
							HashMap<String, String> tmpSkuMap = tmpItemSkuMap.get(itemId);
							if(SCUtil.isVoid(tmpSkuMap))
								skuMap = new HashMap<String, String>();
							else
								skuMap = tmpSkuMap;
							skuMap.put(XPEDXConstants.CUST_SKU_FLAG_FOR_CUSTOMER_ITEM, customerPartNumber);
						}
					}
					itemSkuMap.put(itemId, (HashMap<String, String>)skuMap.clone());
					skuMap.clear();//clearing the sku map for the other Item ids
				
				} else {
					itemSkuMap=tmpItemSkuMap;
					
				}
			}
			
		}
		return itemSkuMap;
	}
	/*End - Changes made by Mitesh Parikh for JIRA 3581*/

	public static String getStaticFileLocation() {		
		return staticFileLocation;
	}
	
	
	
	public static String getXpedxBuildKey() {
		return xpedxBuildKey;
	}

	public static void setXpedxBuildKey(String xpedxBuildKey) {
		XPEDXWCUtils.xpedxBuildKey = xpedxBuildKey;
	}

	/**
	 * extracts catalog main categories...
	 * 
	 * @return
	 * @throws Exception
	 */
	
	public static Map<String,String> getMainCategories() throws Exception {
		Map<String, String> MainCategories = new LinkedHashMap<String, String>();

		Element outputXML = null;
		//performance issue for filter action
		Document outDoc = null;
		Element catElem;
		Element el1;
		IWCContext wcContext = WCContextHelper.getWCContext(ServletActionContext.getRequest());
		SCUILocalSession localSession = wcContext.getSCUIContext().getLocalSession();
		outDoc  = (Document) localSession.getAttribute("categoryCache");
		//SCXmlUtil.getString(outDoc);
	if(outDoc == null || (outDoc!=null && outDoc.getDocumentElement() ==null)) 
	{	
		Map<String, String> valueMap = new HashMap<String, String>();
		String orgCode = wcContext.getStorefrontId();
		String customerId = wcContext.getCustomerId();
		valueMap.put("/SearchCatalogIndex/@CallingOrganizationCode", orgCode);
		valueMap.put("/SearchCatalogIndex/Item/CustomerInformation/@CustomerID", customerId);

		Element input = WCMashupHelper.getMashupInput("xpedx-PreferredCatalogOptions", valueMap, wcContext
						.getSCUIContext());
		
		String inputXml = SCXmlUtil.getString(input);
		log.debug("Input XML: " + inputXml);
		
		el1 = (Element) WCMashupHelper.invokeMashup(
				"xpedx-PreferredCatalogOptions", input, wcContext
						.getSCUIContext());
		if(el1 != null){
			outDoc = el1.getOwnerDocument();
			outputXML = el1.getOwnerDocument().getDocumentElement();
			localSession.setAttribute("categoryCache", outDoc);
		}
		if (null != outputXML) {
			log.debug("Output XML: " + SCXmlUtil.getString(el1));
		}
		
	}else{
		outputXML= outDoc.getDocumentElement();
	}	
	
		Element categoryList = XMLUtilities
				.getChildElementByName(outputXML, "CategoryList");
		if (categoryList == null) {
			return MainCategories;
		}
		NodeList cat = categoryList.getChildNodes();
		int numCats = cat.getLength();
		for (int i = 0; i < numCats; i++) {
			catElem = (Element) cat.item(i);
			String catID = catElem.getAttribute("CategoryID");
			String sDesc = catElem.getAttribute("ShortDescription");
			MainCategories.put(catID, sDesc);
		}
		
		XPEDXConstants.logMessage("MainCategories List : " + MainCategories );

		return MainCategories;
	}
	//added for jira 4306
	public static void resetEditedOrderShipTo(IWCContext wcContext)
	{
		String shipToBeforeEditOrder=(String)XPEDXWCUtils.getObjectFromCache("SHIPTO_BEFORE_EDIT_ORDER");
		String customerContactId=(String)XPEDXWCUtils.getObjectFromCache("CUSTOMER_CONTACT_ID_BEFORE_EDIT_ORDER");
		XPEDXWCUtils.setObectInCache(XPEDXConstants.CHANGE_SHIP_TO_IN_TO_CONTEXT, "true");
		XPEDXWCUtils.setObectInCache("EDIT_ORDER_FINISHED", "true");
		XPEDXWCUtils.removeObectFromCache("rulesDoc");
		XPEDXWCUtils.removeObectFromCache("SHIPTO_BEFORE_EDIT_ORDER");
		XPEDXWCUtils.removeObectFromCache("CUSTOMER_CONTACT_ID_BEFORE_EDIT_ORDER");
		XPEDXWCUtils.removeObectFromCache(XPEDXConstants.CUSTOM_FIELD_FLAG_CHANGE);
		setCurrentCustomerIntoContext(shipToBeforeEditOrder,wcContext);
	}
	//end for jira 4306
}
