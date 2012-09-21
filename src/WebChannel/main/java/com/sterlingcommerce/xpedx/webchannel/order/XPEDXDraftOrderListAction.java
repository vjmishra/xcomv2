/*
 * Action Class to load the Draft Order List Page..
 * 
 * @author: adsouza 	@date: 6-Apr-2010
 */

package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.order.utilities.XPEDXCommerceContextHelper;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.sterlingcommerce.webchannel.utilities.UtilBean;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.core.YFSSystem;

public class XPEDXDraftOrderListAction extends WCMashupAction  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9013184679884854420L;
	public XPEDXDraftOrderListAction() {
		productIdValue = null;
		productIdSearchValue = null;
		productIdQueryType = null;
		ownerFirstNameValue = null;
		ownerFirstNameSearchValue = null;
		ownerFirstNameQueryType = null;
		ownerLastNameValue = null;
		ownerLastNameSearchValue = null;
		ownerLastNameQueryType = null;
		orderNameValue = null;
		orderNameSearchValue = null;
		orderNameQueryType = null;
		orderNumberValue = null;
		orderNumberSearchValue = null;
		orderNumberQueryType = null;
		pageNumber = Integer.valueOf(1);
		orderByAttribute = "Modifyts";
		orderDesc = "Y";
		outputDoc = null;
		searchFieldName = "";
		searchFieldValue = "";
		createTSFrom = "";
		createTSTo = "";
		modifyTSFrom = "";
		modifyTSTo = "";
		createTSQryType = "";
		modifyTSQryType = "";
		isFirstPage = Boolean.FALSE;
		isLastPage = Boolean.FALSE;
		isValidPage = Boolean.FALSE;
		totalNumberOfPages = new Integer(1);
		searchList = new LinkedHashMap();
		recordPerPage = null;
		messageType = "";
	}
	
	public String execute() {
		try {
			String val = YFSSystem.getProperty(CART_WIDGET_RECORD_PER_PAGE_PARAM);
			if (!YFCCommon.isVoid(val)){
				CART_WIDGET_RECORD_PER_PAGE = new Integer(val.trim());
			}
			val = YFSSystem.getProperty(CART_LIST_RECORD_PER_PAGE_PARAM);
			if (!YFCCommon.isVoid(val)){
				CART_LIST_RECORD_PER_PAGE = new Integer(val.trim());
			}		
			
			String messageType = getMessageType();
			
			if(messageType != null && messageType.equals("XPEDXCartListWidget"))
	        {
				setOrderByAttribute("Modifyts");
	            setPageNumber(CART_WIDGET_PAGE_NUMBER);
	            setRecordPerPage(CART_WIDGET_RECORD_PER_PAGE);
	        } else
	        {
	            setRecordPerPage(CART_LIST_RECORD_PER_PAGE);
	        }
			outputDoc = prepareAndInvokeMashup("xpedxDraftOrderList");				
			if(copyFlag!=null && copyFlag.trim().length()>0)
			{
				NodeList OrderList = outputDoc.getElementsByTagName("XPEDXOrderListView");
				for(int OrderCounter = 0;OrderCounter<OrderList.getLength();OrderCounter++)
				{
					Element OrderElement = (Element)OrderList.item(OrderCounter);
					String OrderName = OrderElement.getAttribute("OrderName");
					String OrderNamewithparenthases="'"+OrderName+"'";	
					if(OrderName.equals(copyFlag)|| OrderNamewithparenthases.equals(copyFlag))
					{
						copyFlag=OrderElement.getAttribute("OrderHeaderKey");
						return "copyCart";
					}						
				}
			}
			UtilBean utilBean=new UtilBean();
			ArrayList<Element> orderList =SCXmlUtil.getElements(outputDoc, "Output/XPXOrderListViewList/XPXOrderListView");
			
			//Commented since the call is not required.
			/*Map<String,String> userNameMap=createModifyUserNameMap(outputDoc);
			for(Element order : orderList) {
				order.setAttribute("ModifyUserName", userNameMap.get(order.getAttribute("OrderedByName")));
			}*/
			if(orderList != null && orderList.size()==0)
			{
				XPEDXOrderUtils.refreshMiniCart(wcContext, null, true, XPEDXConstants.MAX_ELEMENTS_IN_MINICART);
				XPEDXWCUtils.setObectInCache("OrderHeaderInContext","_CREATE_NEW_");
			}
			else if("true".equals(deleteOrder))
			{
				//Remove itemMap from Session, when cart change in context,  For Minicart Jira 3481
				XPEDXWCUtils.removeObectFromCache("itemMap");
				
				Element orderNewList = orderList.get(0);
				String orderHeaderKey = orderNewList.getAttribute("OrderHeaderKey");
				Map<String, String> valueMap = new HashMap<String, String>();
			    valueMap.put("/Order/@OrderHeaderKey", orderHeaderKey);
			    Element input = WCMashupHelper.getMashupInput("XPEDXgetOrderList", valueMap, getWCContext());
			    Object obj = WCMashupHelper.invokeMashup("XPEDXgetOrderList", input,wcContext.getSCUIContext());
			    Document outputDoc = ((Element) obj).getOwnerDocument();
			    ArrayList<Element> orderListNew= SCXmlUtil.getElements(outputDoc.getDocumentElement(),"Order");
					if(orderListNew != null && orderListNew.size()>0 ){
						XPEDXOrderUtils.refreshMiniCart(wcContext, orderListNew.get(0), true, XPEDXConstants.MAX_ELEMENTS_IN_MINICART);
				}
			}
			processOutput(outputDoc);
		} catch (Exception ex) {
			XPEDXWCUtils.logExceptionIntoCent(ex);  //JIRA 4289
			log.error(ex);
		}				
		return "success";
	}
	//Commented since this method is not called.
	/*private Map<String,String> createModifyUserNameMap(Element ordersElement)
	{
		Map<String,String> modifyUserMap=new HashMap<String,String>();
		try
		{
			UtilBean utilBean=new UtilBean();
			List<Element> orderList =utilBean.getElements(ordersElement, "//Page/Output/OrderList/Order");
			Element input = WCMashupHelper.getMashupInput("xpedxGetContactUserName", getWCContext());
			input.setAttribute("OrganizationCode", getWCContext().getStorefrontId());
			Element complexQuery = SCXmlUtil.getChildElement(input, "ComplexQuery");
			Element OrElem = SCXmlUtil.getChildElement(complexQuery, "Or");
			for(Element order : orderList) {
				Element exp = input.getOwnerDocument().createElement("Exp");
				exp.setAttribute("Name", "CustomerContactID");
				exp.setAttribute("Value", order.getAttribute("Modifyuserid"));
				SCXmlUtil.importElement(OrElem, exp);
			}
			Element output =(Element) WCMashupHelper.invokeMashup("xpedxGetContactUserName", input, getWCContext().getSCUIContext());
			
			
			List<Element> customerContactList =utilBean.getElements(output, "//CustomerContactList/CustomerContact");
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
	}*/
	public void setProductIdValue(String productID) {
		if (!productID.equals("")) {
			setProductIdSearchValue(productID);
			if (productID.endsWith("*")) {
				productIdValue = productID.substring(0, productID.length() - 1);
				setProductIdQueryType(queryTypeFlike);
			} else {
				productIdValue = productID;
				setProductIdQueryType(queryTypeEq);
			}
		}
	}

	public String getProductIdValue() {
		return productIdValue;
	}

	/**
	 * @return the messageType
	 */
	public String getMessageType() {
		return messageType;
	}

	/**
	 * @param messageType the messageType to set
	 */
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	/**
	 * @return the recordPerPage
	 */
	public Integer getRecordPerPage() {
		return recordPerPage;
	}

	/**
	 * @param recordPerPage the recordPerPage to set
	 */
	public void setRecordPerPage(Integer recordPerPage) {
		this.recordPerPage = recordPerPage;
	}

	public void setProductIdQueryType(String query) {
		productIdQueryType = query;
	}

	public String getProductIdSearchValue() {
		return productIdSearchValue;
	}

	public void setProductIdSearchValue(String value) {
		productIdSearchValue = value;
	}

	public String getProductIdQueryType() {
		return productIdQueryType;
	}

	public void setOwnerFirstNameValue(String firstName) {
		if (!firstName.equals("")) {
			setOwnerFirstNameSearchValue(firstName);
			if (firstName.endsWith("*")) {
				ownerFirstNameValue = firstName.substring(0,
						firstName.length() - 1);
				setOwnerFirstNameQueryType(queryTypeFlike);
			} else {
				ownerFirstNameValue = firstName;
				setOwnerFirstNameQueryType(queryTypeEq);
			}
		}
	}

	public String getOwnerFirstNameValue() {
		return ownerFirstNameValue;
	}

	public void setOwnerFirstNameQueryType(String query) {
		ownerFirstNameQueryType = query;
	}

	public String getOwnerFirstNameSearchValue() {
		return ownerFirstNameSearchValue;
	}

	public void setOwnerFirstNameSearchValue(String value) {
		ownerFirstNameSearchValue = value;
	}

	public String getOwnerFirstNameQueryType() {
		return ownerFirstNameQueryType;
	}

	public void setOwnerLastNameValue(String lastName) {
		if (!lastName.equals("")) {
			setOwnerLastNameSearchValue(lastName);
			if (lastName.endsWith("*")) {
				ownerLastNameValue = lastName.substring(0,
						lastName.length() - 1);
				setOwnerLastNameQueryType(queryTypeFlike);
			} else {
				ownerLastNameValue = lastName;
				setOwnerLastNameQueryType(queryTypeEq);
			}
		}
	}

	public String getOwnerLastNameValue() {
		return ownerLastNameValue;
	}

	public void setOwnerLastNameQueryType(String query) {
		ownerLastNameQueryType = query;
	}

	public String getOwnerLastNameSearchValue() {
		return ownerLastNameSearchValue;
	}

	public void setOwnerLastNameSearchValue(String value) {
		ownerLastNameSearchValue = value;
	}

	public String getOwnerLastNameQueryType() {
		return ownerLastNameQueryType;
	}

	public void setOrderNameValue(String orderName) {
		if (!orderName.equals("")) {
			setOrderNameSearchValue(orderName);
			if (orderName.endsWith("*")) {
				orderNameValue = orderName.substring(0, orderName.length() - 1);
				setOrderNameQueryType(queryTypeFlike);
			} else {
				orderNameValue = orderName;
				setOrderNameQueryType(queryTypeEq);
			}
		}
	}

	public String getOrderNameSearchValue() {
		return orderNameSearchValue;
	}

	public void setOrderNameSearchValue(String value) {
		orderNameSearchValue = value;
	}

	public String getSearchFieldValue() {
		return searchFieldValue;
	}

	public void setSearchFieldValue(String value) {
		if ("ProductIdValue".equals(getSearchFieldName()))
			setProductIdValue(value);
		else if ("OrderNameValue".equals(getSearchFieldName()))
			setOrderNameValue(value);
		else if ("OrderNumberValue".equals(getSearchFieldName()))
			setOrderNumberValue(value);
		searchFieldValue = value;
	}

	public String getOrderNameValue() {
		return orderNameValue;
	}

	public void setOrderNameQueryType(String query) {
		orderNameQueryType = query;
	}

	public String getOrderNameQueryType() {
		return orderNameQueryType;
	}

	public String getSearchFieldName() {
		return searchFieldName;
	}

	public void setSearchFieldName(String name) {
		searchFieldName = name;
	}

	public void setOrderNumberValue(String orderNo) {
		if (!orderNo.equals("")) {
			setOrderNumberSearchValue(orderNo);
			if (orderNo.endsWith("*")) {
				orderNumberValue = orderNo.substring(0, orderNo.length() - 1);
				setOrderNumberQueryType(queryTypeFlike);
			} else {
				orderNumberValue = orderNo;
				setOrderNumberQueryType(queryTypeEq);
			}
		}
	}

	public String getOrderNumberValue() {
		return orderNumberValue;
	}

	public String getOrderNumberSearchValue() {
		return orderNumberSearchValue;
	}

	public void setOrderNumberSearchValue(String value) {
		orderNumberSearchValue = value;
	}

	public void setOrderNumberQueryType(String query) {
		orderNumberQueryType = query;
	}

	public String getOrderNumberQueryType() {
		return orderNumberQueryType;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getOrderByAttribute() {
		return orderByAttribute;
	}

	public void setOrderByAttribute(String orderByAttribute) {
		this.orderByAttribute = orderByAttribute;
	}

	public String getOrderDesc() {
		return orderDesc;
	}

	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}

	protected void processOutput(Element outputEl) {
		try {
			Node retVal = outputEl;
			if (null != retVal) {
				Document doc = retVal.getOwnerDocument();
				
				parsePageInfo(doc, true);
				if (LOG.isDebugEnabled())
					LOG.debug((new StringBuilder()).append("Output Doc is: ")
							.append(XMLUtilities.getXMLDocString(doc))
							.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Element getOutputDoc() {
		return outputDoc;
	}

	public void setOutputDoc(Element outputDocument) {
		outputDoc = outputDocument;
	}

	private void parsePageInfo(Document outDoc, boolean paginated)
			throws XPathExpressionException {
		XPath xpath = XPathFactory.newInstance().newXPath();
		Element page = (Element) xpath.evaluate("//Page", outDoc,
				XPathConstants.NODE);
		isLastPage = Boolean.valueOf(false);
		if (paginated && page != null)
			setIsLastPage(getBooleanAttribute(page, "IsLastPage", isLastPage));
		isFirstPage = Boolean.valueOf(false);
		if (paginated && page != null)
			setIsFirstPage(getBooleanAttribute(page, "IsFirstPage", isFirstPage));
		isValidPage = Boolean.valueOf(false);
		if (paginated && page != null)
			setIsValidPage(getBooleanAttribute(page, "IsValidPage", isValidPage));
		if (paginated && page != null)
			setPageNumber(getIntegerAttribute(page, "PageNumber", pageNumber));
		if (paginated && page != null)
			setPageSetToken(page.getAttribute("PageSetToken"));
		
		totalNumberOfPages = new Integer(0);
		if (paginated && page != null)
			setTotalNumberOfPages(getIntegerAttribute(page,
					"TotalNumberOfPages", totalNumberOfPages));
	}

	private Integer getIntegerAttribute(Element page, String name,
			Integer defaultValue) {
		Integer value = defaultValue;
		String str = page.getAttribute(name);
		try {
			value = Integer.valueOf(str);
		} catch (NumberFormatException e) {
			value = defaultValue;
		}
		return value;
	}

	private static Boolean getBooleanAttribute(Element page, String name,
			Boolean defaultValue) {
		Boolean value = defaultValue;
		String str = page.getAttribute(name);
		if (str != null)
			if ("Y".equals(str))
				value = Boolean.valueOf(true);
			else
				value = Boolean.valueOf(false);
		return value;
	}

	public Boolean getIsLastPage() {
		return isLastPage;
	}

	public void setIsLastPage(Boolean isLastPage) {
		this.isLastPage = isLastPage;
	}

	public Boolean getIsFirstPage() {
		return isFirstPage;
	}

	public void setIsFirstPage(Boolean isFirstPage) {
		this.isFirstPage = isFirstPage;
	}

	public Boolean getIsValidPage() {
		return isValidPage;
	}

	public void setIsValidPage(Boolean isValidPage) {
		this.isValidPage = isValidPage;
	}

	public Integer getTotalNumberOfPages() {
		return totalNumberOfPages;
	}

	public void setTotalNumberOfPages(Integer totalNumberOfPages) {
		this.totalNumberOfPages = totalNumberOfPages;
	}

	public Map getSearchList() {
		searchList.put("OrderNumberValue", getText("OrderNo"));
		searchList.put("OrderNameValue", getText("New_Cart_Name"));
		searchList.put("ProductIdValue", getText("ProductID"));
		return searchList;
	}

	public String getCreateTSFrom() {
		return createTSFrom;
	}

	public void setCreateTSFrom(String value) {
		if (value != null && !value.equals("")) {
			createTSFrom = value;
			setCreateTSQryType(dateRange);
		}
	}

	public String getCreateTSTo() {
		return createTSTo;
	}

	public void setCreateTSTo(String value) {
		if (value != null && !value.equals("")) {
			createTSTo = value;
			setCreateTSQryType(dateRange);
		}
	}

	public String getModifyTSFrom() {
		return modifyTSFrom;
	}

	public void setModifyTSFrom(String value) {
		if (value != null) {
			modifyTSFrom = value;
			setModifyTSQryType(dateRange);
		}
	}

	public String getModifyTSTo() {
		return modifyTSTo;
	}

	public void setModifyTSTo(String value) {
		if (value != null) {
			modifyTSTo = value;
			setModifyTSQryType(dateRange);
		}
	}

	public String getModifyTSQryType() {
		return modifyTSQryType;
	}

	public void setModifyTSQryType(String value) {
		modifyTSQryType = value;
	}

	public void setCreateTSQryType(String value) {
		createTSQryType = value;
	}

	public String getCreateTSQryType() {
		return createTSQryType;
	}

	public String getCartInContextOrderHeaderKey() {
		return (String)XPEDXWCUtils.getObjectFromCache("OrderHeaderInContext")
		/*XPEDXCommerceContextHelper.getCartInContextOrderHeaderKey(
				getWCContext(), false)*/;
	}
    public boolean isAdminUser(){
      return true;
    }
    
    public String getCopyFlag() {
		return copyFlag;
	}

	public void setCopyFlag(String copyFlag) {
		this.copyFlag = copyFlag;
	}   
	
	public String getDeleteOrder() {
		return deleteOrder;
	}

	public void setDeleteOrder(String deleteOrder) {
		this.deleteOrder = deleteOrder;
	}

	public String getPageSetToken() {
		return pageSetToken;
	}

	public void setPageSetToken(String pageSetToken) {
		this.pageSetToken = pageSetToken;
	}

	private static final Logger log = Logger.getLogger(XPEDXDraftOrderListAction.class);
	protected String productIdValue;
	protected String productIdSearchValue;
	protected String productIdQueryType;
	protected String ownerFirstNameValue;
	protected String ownerFirstNameSearchValue;
	protected String ownerFirstNameQueryType;
	protected String ownerLastNameValue;
	protected String ownerLastNameSearchValue;
	protected String ownerLastNameQueryType;
	protected String orderNameValue;
	protected String orderNameSearchValue;
	protected String orderNameQueryType;
	protected String orderNumberValue;
	protected String orderNumberSearchValue;
	protected String orderNumberQueryType;
	protected Integer pageNumber;
	protected String orderByAttribute;
	protected String orderDesc;
	protected Element outputDoc;
	private String searchFieldName;
	private String searchFieldValue;
	private String createTSFrom;
	private String createTSTo;
	private String modifyTSFrom;
	private String modifyTSTo;
	private String createTSQryType;
	private String modifyTSQryType;
	private String copyFlag;
	private Boolean isFirstPage;
	private Boolean isLastPage;
	private Boolean isValidPage;
	private Integer totalNumberOfPages;
	protected static String queryTypeFlike = "FLIKE";
	protected static String queryTypeEq = "EQ";
	protected static String dateRange = "DATERANGE";
	protected Map searchList;
	
	//RUgrani: Adding page size
	private Integer recordPerPage;
	private String messageType;
	private String deleteOrder;

	private static final String CART_WIDGET_RECORD_PER_PAGE_PARAM = "cart_widget_record_per_page";
    private static final String CART_LIST_RECORD_PER_PAGE_PARAM = "cart_list_record_per_page";

	private static Integer CART_WIDGET_RECORD_PER_PAGE = Integer.valueOf(3);
    private static Integer CART_LIST_RECORD_PER_PAGE = Integer.valueOf(25);
 
    private static final Integer CART_WIDGET_PAGE_NUMBER = Integer.valueOf(1);
    private String pageSetToken;	

}


