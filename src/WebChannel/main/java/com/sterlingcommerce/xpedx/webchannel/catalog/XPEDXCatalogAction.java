
package com.sterlingcommerce.xpedx.webchannel.catalog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.xml.xpath.XPathExpressionException;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.comergent.appservices.configuredItem.XMLUtils;
import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.framework.extensions.ISCUITransactionContext;
import com.sterlingcommerce.ui.web.framework.helpers.SCUITransactionContextHelper;
import com.sterlingcommerce.ui.web.platform.transaction.SCUITransactionContextFactory;
import com.sterlingcommerce.ui.web.platform.utils.SCUIPlatformUtils;
import com.sterlingcommerce.webchannel.catalog.CatalogAction;
import com.sterlingcommerce.webchannel.common.Breadcrumb;
import com.sterlingcommerce.webchannel.common.BreadcrumbHelper;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXCustomerContactInfoBean;
import com.sterlingcommerce.xpedx.webchannel.common.XpedxSortUOMListByConvFactor;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXItem;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXPriceAndAvailability;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXPriceandAvailabilityUtil;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXWarehouseLocation;
import com.xpedx.nextgen.common.util.XPXCatalogDataProcessor;
import com.yantra.util.YFCUtils;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNode;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;

@SuppressWarnings("deprecation")
public class XPEDXCatalogAction extends CatalogAction {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(XPEDXCatalogAction.class);

	private String customerNumber = null;
	private boolean isStockedItem = false;
	private boolean CategoryC3 = false;
	private String priceCurrencyCode;
	private static final String CUSTOMER_PART_NUMBER_FLAG = "1";
	private boolean stockedCheckeboxSelected;
	private Map<String, Map<String, String>> itemUomHashMap = new HashMap<String, Map<String, String>>();
	private Map<String, String> uomConvFactorMap = new HashMap<String, String>();
	ArrayList<String> itemIDList = new ArrayList<String>();
	private String catalogLandingMashupID = null;
	private XPEDXShipToCustomer shipToCustomer;
	private String msapOrderMultipleFlag = "";
	private String tryAgain = "";
	private String errorCode = "";
	private Map<String, List<Element>> PLLineMap;
	private String firstItem = "";
	private String indexField = "";
	private String remSearchTerms = "";
	private LinkedHashMap<String, Map<String, String>> itemUomIsCustomerUomHashMap = new LinkedHashMap<String, Map<String, String>>();

	protected String isCustomerPO="N";
	protected String isCustomerLinAcc="N";
	protected String customerPOLabel="";
	protected String custLineAccNoLabel="";

	public String getIsCustomerPO() {
		return isCustomerPO;
	}

	public void setIsCustomerPO(String isCustomerPO) {
		this.isCustomerPO = isCustomerPO;
	}

	public String getIsCustomerLinAcc() {
		return isCustomerLinAcc;
	}

	public void setIsCustomerLinAcc(String isCustomerLinAcc) {
		this.isCustomerLinAcc = isCustomerLinAcc;
	}

	public String getCustomerPOLabel() {
		return customerPOLabel;
	}

	public void setCustomerPOLabel(String customerPOLabel) {
		this.customerPOLabel = customerPOLabel;
	}

	public String getCustLineAccNoLabel() {
		return custLineAccNoLabel;
	}

	public void setCustLineAccNoLabel(String custLineAccNoLabel) {
		this.custLineAccNoLabel = custLineAccNoLabel;
	}

	//Added class variable for JIRA #4195 - OOB variable searchTerm doesn't have a getter method exposed
	private String searchString = null;
	private String searchIndexInputXML;
	private String qtyTextBox = null;
	private String luceneEscapeWords[]={"a", "an", "and", "are", "as", "at", "be", "but", "by",
			 "for", "if", "in", "into", "is", "it",
			 "no", "not", "of", "on", "or", "such",
			  "that", "the", "their", "then", "there", "these",
			 "they", "this", "to", "was", "will", "with"
	};

	public String getQtyTextBox() {
		return qtyTextBox;
	}

	public void setQtyTextBox(String qtyTextBox) {
		this.qtyTextBox = qtyTextBox;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public String getRemSearchTerms() {
		return remSearchTerms;
	}

	public void setRemSearchTerms(String remSearchTerms) {
		this.remSearchTerms = remSearchTerms;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getTryAgain() {
		return tryAgain;
	}

	public void setTryAgain(String tryAgain) {
		this.tryAgain = tryAgain;
	}

	public String getIndexField() {
		return indexField;
	}

	public void setIndexField(String indexField) {
		this.indexField = indexField;
	}

	private String extnMfgItemFlag;
	private String extnCustomerItemFlag;

	public String getExtnMfgItemFlag() {
		return extnMfgItemFlag;
	}

	public void setExtnMfgItemFlag(String extnMfgItemFlag) {
		this.extnMfgItemFlag = extnMfgItemFlag;
	}

	public String getExtnCustomerItemFlag() {
		return extnCustomerItemFlag;
	}

	public void setExtnCustomerItemFlag(String extnCustomerItemFlag) {
		this.extnCustomerItemFlag = extnCustomerItemFlag;
	}

	private String theSpanNameValue;
	private String sortDirection;

	public String getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	public String getTheSpanNameValue() {
		return theSpanNameValue;
	}

	public void setTheSpanNameValue(String theSpanNameValue) {
		this.theSpanNameValue = theSpanNameValue;
	}

	private String categoryPath = "";

	public String getCategoryPath() {
		return categoryPath;
	}

	public void setCategoryPath(String categoryPath) {
		this.categoryPath = categoryPath;
	}

	// Added for performance Fix
	private String categoryShortDescription = "";

	public String getCategoryShortDescription() {
		return categoryShortDescription;
	}

	public void setCategoryShortDescription(String categoryShortDescription) {
		this.categoryShortDescription = categoryShortDescription;
	}

	private Map<String, String> defaultShowUOMMap;

	private LinkedHashMap<String, String> itemCustomerUomMap = new LinkedHashMap<String, String>();

	public LinkedHashMap<String, String> getItemCustomerUomMap() {
		return itemCustomerUomMap;
	}

	public void setItemCustomerUomMap(
			LinkedHashMap<String, String> itemCustomerUomMap) {
		this.itemCustomerUomMap = itemCustomerUomMap;
	}

	private Map<String, String> orderMultipleMap;
	private String itemDtlBackPageURL = "";
	private String productCompareBackPageURL;

	public String getProductCompareBackPageURL() {
		return productCompareBackPageURL;
	}

	public void setProductCompareBackPageURL(String productCompareBackPageURL) {
		this.productCompareBackPageURL = productCompareBackPageURL;
	}

	public String getItemDtlBackPageURL() {
		return itemDtlBackPageURL;
	}

	public void setItemDtlBackPageURL(String itemDtlBackPageURL) {
		this.itemDtlBackPageURL = itemDtlBackPageURL;
	}

	public Map<String, String> getOrderMultipleMap() {
		return orderMultipleMap;
	}

	public void setOrderMultipleMap(Map<String, String> orderMultipleMap) {
		this.orderMultipleMap = orderMultipleMap;
	}

	public Map<String, String> getDefaultShowUOMMap() {
		return defaultShowUOMMap;
	}

	public void setDefaultShowUOMMap(Map<String, String> defaultShowUOMMap) {
		this.defaultShowUOMMap = defaultShowUOMMap;
	}

	public String getFirstItem() {
		return firstItem;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public Map<String, List<Element>> getPLLineMap() {
		return PLLineMap;
	}

	public void setPLLineMap(Map<String, List<Element>> pLLineMap) {
		PLLineMap = pLLineMap;
		wcContext.setWCAttribute("PLLineMap", PLLineMap, WCAttributeScope.REQUEST);
	}

	public String getCatalogLandingMashupID() {
		return catalogLandingMashupID;
	}

	public void setCatalogLandingMashupID(String catalogLandingMashupID) {
		this.catalogLandingMashupID = catalogLandingMashupID;
	}

	// Webtrends tag start
	private boolean searchMetaTag = false;

	public boolean issearchMetaTag() {
		return searchMetaTag;
	}

	public void setsearchMetaTag(boolean searchMetaTag) {
		this.searchMetaTag = searchMetaTag;
	}

	// Webtrends tag end

	public boolean isCategoryC3() {
		return CategoryC3;
	}

	public void setCategoryC3(boolean categoryC3) {
		CategoryC3 = categoryC3;
	}

	public boolean isStockedCheckeboxSelected() {

		return stockedCheckeboxSelected;
	}

	public void setStockedCheckeboxSelected(boolean stockedCheckeboxSelected) {
		this.stockedCheckeboxSelected = stockedCheckeboxSelected;
	}

	public boolean isStockedItem() {
		return isStockedItem;
	}

	public void setStockedItem(boolean isStockedItem) {

		this.isStockedItem = isStockedItem;
	}

	private void setStockedItemFromSession() {
		if (getWCContext().getWCAttribute("StockedCheckbox", WCAttributeScope.SESSION) == null) {
			// init session value from bill-to setting
			shipToCustomer = (XPEDXShipToCustomer) XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
			if (shipToCustomer != null) {
				String defaultStockedItemView = shipToCustomer.getBillTo().getExtnDefaultStockedItemView();
				setStockedCheckeboxSelected(defaultStockedItemView.equals(XPEDXConstants.DEFAULT_STOCKED_ITEM_VIEW_STOCKED)
						|| defaultStockedItemView.equals(XPEDXConstants.DEFAULT_STOCKED_ITEM_VIEW_ONLY_STOCKED));
				getWCContext().setWCAttribute("StockedCheckbox", isStockedCheckeboxSelected(), WCAttributeScope.SESSION);
			}
		}
		Object sessionStockedCheckbox = getWCContext().getWCAttribute("StockedCheckbox", WCAttributeScope.SESSION);
		if (sessionStockedCheckbox != null) {
			isStockedItem = sessionStockedCheckbox.toString().equalsIgnoreCase("true");
		}
	}

	private Map<String,List<Element>> replacmentItemsMap;

	public Map<String, List<Element>> getReplacmentItemsMap() {
		return replacmentItemsMap;
	}

	public void setReplacmentItemsMap(Map<String, List<Element>> replacmentItemsMap) {
		this.replacmentItemsMap = replacmentItemsMap;
	}

	/**
	 * Using the constructor to set the default pageSize to 15. Xpedx
	 * requirement- RUgrani
	 */
	public XPEDXCatalogAction() {
		super();
		setPageSize("20");
		//Setting default selected view with empty string, so that the b2bview can be loaded by default
		this.selectedView = "";
	}

	@Override
	public String execute() {
		try {
			setCustomerNumber();
			String retVal = super.execute();
			getSortFieldDocument();
			setItemsUomsMap();
			init();
			return retVal;
		} catch (Exception e) {
			XPEDXWCUtils.logExceptionIntoCent(e); // JIRA 4289
			log.error("", e);
		}
		return "";
	}

	private void init() {
		try {
			req.setAttribute("Tag_WCContext", wcContext);
			req.setAttribute("Tag_orderMultipleString", getText("MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES"));
			req.setAttribute("Tag_qtyString", getText("MSG.SWC.CART.ADDTOCART.ERROR.QTYGTZERO"));

			if (searchString != null && !searchString.trim().equals("")) {
				searchTerm = searchString;
			}

			if (searchTerm != null && !searchTerm.trim().equals("")) {
				// Changes made for XBT 251 special characters replace by Space while Search
				// searchTerm=searchTerm.replaceAll("[\\[\\]\\-\\+\\^\\)\\;{!(}:,~\\\\]"," ");
				searchTerm = processSpecialCharacters(searchTerm);

				searchTerm = XPXCatalogDataProcessor.preprocessSearchQuery(searchTerm);
				setSearchString(searchTerm);

				String appendStr = "&searchTerm=" + searchTerm;
				XPEDXWCUtils.setItemDetailBackPageURLinSession(appendStr);
				if (searchTerm.trim().length() == 1 && (searchTerm.indexOf("*") == 0 || searchTerm.indexOf("?") == 0)) {
					searchTerm = "";
				}
				if (searchTerm.indexOf("*") == 0 || searchTerm.indexOf("?") == 0)
					searchTerm = searchTerm.substring(1, searchTerm.length());
			} else {
				XPEDXWCUtils.setItemDetailBackPageURLinSession();
			}
			setItemDtlBackPageURL(wcContext.getSCUIContext().getSession().getAttribute("itemDtlBackPageURL").toString());
			setProductCompareBackPageURL(wcContext.getSCUIContext().getSession().getAttribute("itemDtlBackPageURL").toString());
			getCustomerLineDetails();
		} catch (Exception exception) {
			log.error("Error in Init Method", exception);
		}
	}

	protected void getCustomerLineDetails() throws Exception {
		// get the map from the session. if null query the DB
		LinkedHashMap<String, String> customerFieldsSessionMap = getCustomerFieldsMapfromSession();
		if (null != customerFieldsSessionMap && customerFieldsSessionMap.size() >= 0) {
			LOG.debug("Found customerFieldsMap in the session");
		}
		if (customerFieldsSessionMap != null) {
			for (String field : customerFieldsSessionMap.keySet()) {
				if ("CustomerPONo".equals(field)) {
					isCustomerPO = "Y";
					customerPOLabel = customerFieldsSessionMap.get(field);
				}
				if ("CustLineAccNo".equals(field)) {
					isCustomerLinAcc = "Y";
					custLineAccNoLabel = customerFieldsSessionMap.get(field);
				}
			}
		}
	}

	protected LinkedHashMap getCustomerFieldsMapfromSession() {
		XPEDXWCUtils.setSAPCustomerExtnFieldsInCache();
		LinkedHashMap customerFieldsSessionMap = (LinkedHashMap) XPEDXWCUtils.getObjectFromCache("customerFieldsSessionMap");
		return customerFieldsSessionMap;
	}

	private void getSortFieldDocument() {
		Map<String, String> fl = this.getSortFieldList();
		Map<String, String> fieldMap = new LinkedHashMap<String, String>();
		List<String> keyValues = new ArrayList(fl.keySet());
		fieldMap.put("relevancy", getText("relevancy"));
		for (String keyValue : keyValues) {
			String field = fl.get(keyValue);
			String dispText = "";
			for (String value : columnList) {
				if (field.contains(value)) {
					fieldMap.put(keyValue, field);
					break;
				} else {
					if (value.equals("Sku")) {
						value = "SKU";
					}
					if (value.equals("Desc")) {
						value = "Name";
					}
					if (field.contains(value) || field.contains("Env") || field.contains("Stock")) {
						if (field.contains("Env") && field.contains("ascending")) {
							field = field.replaceAll("ascending", "descending");
						} else if (field.contains("Env") && field.contains("descending")) {
							field = field.replaceAll("descending", "ascending");
						}
						fieldMap.put(keyValue, field);
						break;
					}
				}
			}
		}

		getSortFieldList().clear();
		getSortFieldList().putAll(fieldMap);
	}

	private void setCustomerNumber() {
		try {
			String customerId = getWCContext().getCustomerId();
			if (customerId == null) {
				return;
			}
			String[] customerIDTokens = customerId.split("\\-");
			if (customerIDTokens != null && customerIDTokens.length > 1) {
				customerNumber = customerIDTokens[1];
			}
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sterlingcommerce.webchannel.catalog.CatalogAction#filter()
	 */
	@Override
	public String filter() {
		init();

		if(("").equals(super.sortField) && (super.session.get("sortField") == null || ("").equals(super.session.get("sortField")))) {
			orderByAttribute = "Item.ExtnBestMatch";
			sortField = "Item.ExtnBestMatch--A";
		}

		String returnString = super.filter();
		changeBasis();
		//getting the customer bean object from session.
		/***** Start of  Code changed for Promotions Jira 2599 ********/
		List<Breadcrumb> bcl = BreadcrumbHelper.preprocessBreadcrumb(this
				.get_bcs_());
		log.debug("CatalogAction : filter(): start");

		Breadcrumb lastBc = bcl.get(bcl.size() - 1);
		Map<String, String> params = lastBc.getParams();
		String[] pathDepth = StringUtils.split(path, "/");
		path = params.get("path");

		if (log.isDebugEnabled()) {
			for (int i = 0; i < bcl.size(); i++) {
				Breadcrumb bc = bcl.get(i);
				Map<String, String> bcParams = bc.getParams();
				String cnameValue = bcParams.get("cname");
				log.debug("CatalogAction : filter(): Breadcrumb : cnameValue=" + cnameValue);
			}
		}

		shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);

		if (ERROR.equals(returnString)) {
			return returnString;
		} else {
			try {
				getAllAPIOutput();
			} catch (Exception e) {
				log.error("", e);
			}
			setItemsUomsMap();
			setAttributeListForUI();
			prepareItemBranchInfoBean();
			setColumnListForUI();

			if ((path == null || path.equals("/")) && getFirstItem().trim() != "") {
				YFCNode yfcNode = YFCDocument.getDocumentFor(getOutDoc()).getDocumentElement().getChildElement("ItemList").getFirstChild();
				YFCIterable<? extends YFCNode> YFCIterables = yfcNode.getChildren();
				while (YFCIterables.hasNext()) {
					YFCNode node = YFCIterables.next();
					if (node != null && node.getNodeName().equalsIgnoreCase("CategoryList")) {
						path = node.getFirstChild().getAttributes().get("CategoryPath");
						categoryPath = path;
					}
				}
			}
			getCatTwoDescFromItemIdForpath(getOutDoc().getDocumentElement(), categoryPath);

		}
		return SUCCESS;
	}

	public void getCatTwoDescFromItemIdForpath(Element categoryList, String path) {
		categoryPath = path;
		StringBuilder cat = new StringBuilder();
		String adjugglerKeywordPrefix = XPEDXWCUtils.getAdJugglerKeywordPrefix();
		String categoryID = "";
		// getting exact cat2 path and category id from the actual path
		if (path != null && path.trim().length() > 0 && categoryList != null) {
			StringTokenizer st = new StringTokenizer(path, "/");
			if (st.hasMoreTokens()) {
				cat.append("/").append(st.nextToken());
				if (st.hasMoreTokens()) {
					cat.append("/").append(st.nextToken());
					if (st.hasMoreTokens()) {
						categoryID = st.nextToken();
						cat.append("/").append(categoryID);
					}
				}
			}
			try {
				// checking if the cat2 path is already there in cache if yes then get it from there
				ArrayList<Element> categoryElements = SCXmlUtil.getElements(categoryList, "/CategoryList/Category");
				if (categoryElements != null)
					categoryElements.addAll(SCXmlUtil.getElements(categoryList, "/CategoryList/Category/ChildCategoryList/Category"));
				else
					categoryElements = SCXmlUtil.getElements(categoryList, "/CategoryList/Category/ChildCategoryList/Category");
				for (Element catgegory : categoryElements) {
					String shortDescription = catgegory.getAttribute("ShortDescription");
					String categoryPath = catgegory.getAttribute("CategoryPath");
					if (categoryPath != null && categoryPath.equals(cat.toString())) {
						categoryShortDescription = shortDescription;
						break;
					}
				}
			} catch (Exception e) {
				log.error("Error while getting ShortDescreption for Adjuggler " + e.getMessage());
			}
		}
		// Added For XBT-253
		// if category id is not there in caceh then check whether the item is there on cat2 level if yes then get it from there
		if (categoryShortDescription == null || categoryShortDescription.equals("")) {
			if (firstItemCategoryShortDescription != null && firstItemCategoryShortDescription.length() > 0) {
				categoryShortDescription = firstItemCategoryShortDescription;
			} else {
				// if category id is not there in cacche and first item is also not at cat2 level , do the api call for getCategoryList which will give shortdescription
				categoryShortDescription = getCategoryShortDescription(categoryID);
				if (categoryShortDescription == null)
					categoryShortDescription = (String) XPEDXWCUtils.getObjectFromCache("defaultCategoryDesc");
			}
		}
		categoryShortDescription = XPEDXWCUtils.sanitizeAJKeywords(adjugglerKeywordPrefix + categoryShortDescription);
	}

	/**
	 * This method will do a API call getCategoryList based on given category ID
	 *
	 * @param categoryID return shortDescriotion
	 */
	private String getCategoryShortDescription(String categoryID) {
		YFCDocument getCategoryListInXML = YFCDocument.createDocument("Category");

		YFCElement categoryLstEle = getCategoryListInXML.getDocumentElement();
		categoryLstEle.setAttribute("CategoryID", categoryID);
		categoryLstEle.setAttribute("OrganizationCode", wcContext.getStorefrontId());

		/*
		 * Input : <Category CategoryID="300131" OrganizationCode="xpedx"></Category>
		 *
		 * Output : <CategoryList ><Category CategoryID="" CategoryKey="" CategoryPath="" Description="" ShortDescription="" ></Category></CategoryList>
		 */
		YFCDocument template2 = YFCDocument.getDocumentFor("<CategoryList ><Category /></CategoryList>");
		ISCUITransactionContext scuiTransactionContext = null;
		SCUIContext wSCUIContext = null;
		YFCElement categoryListElement = null;

		try {
			IWCContext context = WCContextHelper.getWCContext(ServletActionContext.getRequest());
			wSCUIContext = context.getSCUIContext();
			scuiTransactionContext = wSCUIContext.getTransactionContext(true);

			categoryListElement = SCUIPlatformUtils.invokeXAPI("getCategoryList", categoryLstEle, template2.getDocumentElement(), wcContext.getSCUIContext());

		} catch (Exception ex) {
			log.error(ex.getMessage());
			scuiTransactionContext.rollback();
		} finally {
			if (scuiTransactionContext != null) {
				SCUITransactionContextHelper.releaseTransactionContext(scuiTransactionContext, wSCUIContext);
			}
		}

		if (categoryListElement == null) {
			return null;
		}

		YFCElement catEle = categoryListElement.getFirstChildElement();
		if (catEle != null) {
			return catEle.getAttribute("ShortDescription");
		}

		return null;
	}

	/**
	 * This method sets the instance varaible "columList" by getting all the
	 * Column Names from the DB. This is determined by the FilterType and the
	 * Value. If the FilterType is "ProductType", it fethches the DB for all the
	 * ColumnNames for the FilterType value; If the Filter(NarrowBy) is not
	 * applied OR if the Filter Type is not "ProductType" then the default set
	 * of ColumnNames are returned.
	 */
	public void setColumnListForUI() {
		List<Breadcrumb> bcl = BreadcrumbHelper.preprocessBreadcrumb(this.get_bcs_());
		log.debug("CatalogAction : setColumnListForUI(): start");

		boolean isLayoutDefined = false;
		try {
			Iterator<Breadcrumb> iter = bcl.iterator();
			while (iter.hasNext()) {
				Breadcrumb bc = iter.next();
				if (bc.getAction() != null && bc.getAction().equals("filter")) {
				//EB 4803 For a filter action indexField should not be null.
				if(YFCCommon.isVoid(indexField))
						indexField = (String) bc.getParams().get("indexField");
					// String indexField = (String) bc.getParams().get(
					// "indexField");
					String filterDesc = (String) bc.getParams().get("filterDesc");
					if (log.isDebugEnabled()) {
						log.debug("CatalogAction : setColumnListForUI(): Breadcrumb : indexField=" + indexField);
						log.debug("CatalogAction : setColumnListForUI(): Breadcrumb : filterDesc=" + filterDesc);
					}
					if (!YFCCommon.isVoid(indexField) && indexField.contains(XPEDXCatalogAction.XPEDX_PRODUCT_LINE_INDEX_FIELD)) {
						String productType = (String) bc.getParams().get("cname");
						columnList = getListOfColumns(productType);
						if (null == columnList || columnList.size() <= 0) {
							isLayoutDefined = false;
						} else {
							isLayoutDefined = true;
						}
						break;
					}
				}
			}

			if (!isLayoutDefined) {
				columnList = getListOfColumns(XPEDX_DEFAULT_TYPE_COLUMN);
			}
			if (log.isDebugEnabled()) {
				log.debug("setColumnListForUI: " + columnList.toString());
			}
		} catch (CannotBuildInputException e) {
			LOG.error("Error Getting Column Names from the DB for product type.... " + e);
			columnList = new ArrayList<String>();
		} catch (Exception e) {
			LOG.error("Error Getting Column Names from the DB for product type.... " + e);
			columnList = new ArrayList<String>();
		}
	}

	@Override
	protected void populateMashupInput(String mashupId, Map<String, String> valueMap, Element mashupInput) throws WCMashupHelper.CannotBuildInputException {
		int TERMS_NODE = 0;

		Set<String> keySet = valueMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		String searchStringValue = "";
		while (iterator.hasNext()) {
			String key = iterator.next();
			if (key.contains("Terms/Term") && key.contains("Value"))
				searchStringValue = searchStringValue + " " + valueMap.get(key);
		}

		/*
		 * Following line of code replaces the search term from leading astrix by blank It also splits the search term by space and add as a must attribute in search term
		 */

		int termIndex = 1;
		if (null != searchStringValue && !"".equals(searchStringValue.trim())) {
			searchStringValue = searchStringValue.trim();

			if (searchStringValue.indexOf("*") == 0 || searchStringValue.indexOf("?") == 0) {
				searchStringValue = searchStringValue.substring(1, searchStringValue.length());
			}

			searchStringValue = XPXCatalogDataProcessor.preprocessSearchQuery(searchStringValue);

			searchStringValue = processSpecialCharacters(searchStringValue);

			// Changes made for XBT 251 special characters replace by Space while Search
			// searchStringValue=searchStringValue.replaceAll("[\\[\\]\\-\\+\\^\\)\\;{!(}:,~\\\\]"," ");
			String searchStringTokenList[] = searchStringValue.split(" ");

			setStockedItemFromSession();
			List<String> specialWords = Arrays.asList(luceneEscapeWords);
			for (String searchStringToken : searchStringTokenList) {
				if (!specialWords.contains(searchStringToken.trim().toLowerCase())) {
					if (!"".equals(searchStringToken.trim())) {
						valueMap.put("/SearchCatalogIndex/Terms/Term[" + termIndex + "]/@Value", searchStringToken.trim());
						// eb-3685: marketing group search 'search within results' cannot use SHOULD
						if (searchStringTokenList.length == 1 && getMarketingGroupId() == null && !isStockedItem) {
							valueMap.put("/SearchCatalogIndex/Terms/Term[" + termIndex + "]/@Condition", "SHOULD");
						} else {
							valueMap.put("/SearchCatalogIndex/Terms/Term[" + termIndex + "]/@Condition", "MUST");
						}
						termIndex++;
					}
				}
			}
		}

		if (marketingGroupId != null) {
			valueMap.put("/SearchCatalogIndex/Terms/Term[" + termIndex + "]/@IndexFieldName", "Item.ExtnMarketingGroupId");
			valueMap.put("/SearchCatalogIndex/Terms/Term[" + termIndex + "]/@Value", marketingGroupId);
			valueMap.put("/SearchCatalogIndex/Terms/Term[" + termIndex + "]/@Condition", "MUST");
			termIndex++;
		}

		super.populateMashupInput(mashupId, valueMap, mashupInput);
		ArrayList<Element> elements = SCXmlUtil.getElements(mashupInput, "Terms");
		boolean flag = false;
		List<Element> termListEle = null;
		if (elements != null && elements.size() > 0) {
			Element terms = elements.get(TERMS_NODE);
			termListEle = SCXmlUtil.getChildrenList(terms);
			for (Element termEle : termListEle) {
				String termValue = termEle.getAttribute("Value");
				/*
				 * Following if block checks if there is only one search term in terms and if this search term is * then it sets the flag this flag is lateron checked in code and
				 * it removes the complete Terms from mashupinput
				 */
				if (termValue.trim().length() == 1 && (termValue.indexOf("*") == 0 || termValue.indexOf("?") == 0) && termListEle != null && termListEle.size() == 1) {
					flag = true;
				}
				/*
				 * Following line of code checks if there are multiple terms and one of the search term is only * then it removes only term which contains * attribute
				 */
				if (termValue.trim().length() == 1 && (termValue.indexOf("*") == 0 || termValue.indexOf("?") == 0) && termListEle != null && termListEle.size() > 1) {
					termListEle.remove(termEle);
				}
				/*
				 * Following code replaces all the * in leading to blank
				 */
				if (termValue.indexOf("*") == 0 || termValue.indexOf("?") == 0) {
					termValue = termValue.substring(1, termValue.length());
					termEle.setAttribute("Value", termValue);
				}

				termValue = XPXCatalogDataProcessor.preprocessSearchQuery(termValue);

				termEle.setAttribute("Value", termValue);
			}

			if (customerNumber != null && customerNumber.trim().length() > 0) {
				Element term = SCXmlUtil.createChild(terms, "Term");
				term.setAttribute("Condition", "SHOULD");
				term.setAttribute("IndexFieldName", "customerNumberPlusPartNumber");
				if(null != searchTerm && !("").equals(searchTerm)) {
					searchTerm = searchTerm.trim();
					if(searchTerm.indexOf("*") == 0 || searchTerm.indexOf("?") == 0) {
						searchTerm = searchTerm.substring(1, searchTerm.length());
					}
				}

				searchTerm = XPXCatalogDataProcessor.preprocessSearchQuery(searchTerm);

				term.setAttribute("Value", customerNumber + searchTerm);
			}
		}

		if (flag) {
			SCXmlUtil.removeNode(elements.get(0));
		}

		setStockedItemFromSession();
		shipToCustomer = (XPEDXShipToCustomer) XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		if (isStockedItem) {
			if (shipToCustomer == null) {
				XPEDXWCUtils.setCustomerObjectInCache(XPEDXWCUtils.getCustomerDetails(getWCContext().getCustomerId(), getWCContext().getStorefrontId()).getDocumentElement());
				setShipToCustomer((XPEDXShipToCustomer) XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER));
			}
			String shipFromDivision = shipToCustomer.getExtnShipFromBranch();

			if (shipFromDivision != null && shipFromDivision.trim().length() > 0) {
				Element terms = null;
				if (elements != null && elements.size() > 0) {
					terms = elements.get(TERMS_NODE);
				} else {
					terms = SCXmlUtil.createChild(mashupInput, "Terms");
				}

				Element term = SCXmlUtil.createChild(terms, "Term");
				term.setAttribute("Condition", "MUST");
				term.setAttribute("IndexFieldName", "showStockedItems");
				term.setAttribute("Value", shipFromDivision);
			}
		}
		// EB-1731 Adding api inputXML in request so that it should not do all calculation on View all link
		//         the size of this xml will be around 100 bytes so there will not be any latency.
		searchIndexInputXML = SCXmlUtil.getString(mashupInput).replace("\n", "");
	}

	private void changeBasis() {
		if (getOutDoc() != null) {
			Element itemEementList = (Element) getOutDoc().getElementsByTagName("ItemList").item(0);
			if (itemEementList != null) {
				NodeList extnNodeList = itemEementList.getElementsByTagName("Extn");
				if (extnNodeList != null) {
					for (int i = 0; i < extnNodeList.getLength(); i++) {
						Element itemElement = (Element) extnNodeList.item(i);
						if (itemElement != null) {
							if (itemElement.getAttribute("ExtnBasis") != null) {
								String extnBasis = itemElement.getAttribute("ExtnBasis").replaceFirst("^0+(?!$)", "");
								if (extnBasis != null && extnBasis.trim().length() > 0 && !"0".equals(extnBasis.trim())) {
									itemElement.setAttribute("ExtnBasis", (Double.valueOf(extnBasis) + "").replaceFirst("\\.0+$", ""));
								}
								/*
								 * EB-829 To implement a Permanent Fix for Numeric Sort on item list page for the 'Basis' Column
								 */
								else {
									itemElement.setAttribute("ExtnBasis", "");
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public String newSearch() {
		try {
			init();
			setCustomerNumber();
			StringBuffer sb = new StringBuffer();
			long startTime = System.currentTimeMillis();

			if (("").equals(super.sortField) && (super.session.get("sortField") == null || ("").equals(super.session.get("sortField")))) {
				orderByAttribute = "Item.ExtnBestMatch";
				sortField = "Item.ExtnBestMatch--A";
			}

			String returnString = super.newSearch();

			// getting the customer bean object from session.
			changeBasis();
			long endTime = System.currentTimeMillis();
			long timespent = (endTime - startTime);
			sb.append("OOTB execution time on catlaog newSearch() = " + timespent);
			shipToCustomer = (XPEDXShipToCustomer) XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);

			List<Breadcrumb> bcl = BreadcrumbHelper.preprocessBreadcrumb(this.get_bcs_());
			log.debug("CatalogAction : newSearch(): start");
			Breadcrumb lastBc = bcl.get(bcl.size() - 1);
			Map<String, String> params = lastBc.getParams();
			String[] pathDepth = StringUtils.split(path, "/");
			path = params.get("path");

			for (int i = 0; i < bcl.size(); i++) {
				Breadcrumb bc = bcl.get(i);
				Map<String, String> bcParams = bc.getParams();
				String cnameValue = bcParams.get("cname");
				log.debug("CatalogAction : newSearch(): Breadcrumb : cnameValue=" + cnameValue);
			}

			if (bcl.size() > 1 || (!("true".equals(displayAllCategories)))) {
				if (!YFCCommon.isVoid(pathDepth) && pathDepth.length == 2) {
					// for c1 categories-> show all the sub categories of C1 in
					// the
					// landing page
					setCategoryDepth("1");// default value is 2; coming from
											// struts
					// file
				}
			}

			// Webrtends tag start
			setsearchMetaTag(true);
			// Webrtends tag start

			if (ERROR.equals(returnString)) {
				if ("true".equals(getTryAgain())) {
					return returnString;
				}
				if (searchTerm != null && searchTerm.indexOf("*") > -1) {
					String searchTermList[] = searchTerm.split(" ");
					String strSearchTerm = "";
					String removedSearchTerms = "";
					for (String searchTermToken : searchTermList) {
						if (searchTermToken.indexOf("*") > -1 && searchTermToken.length() > 5) {
							strSearchTerm = strSearchTerm + searchTermToken + " ";
							continue;
						} else if (searchTermToken.indexOf("*") == -1) {
							strSearchTerm = strSearchTerm + searchTermToken + " ";
							continue;
						}
						removedSearchTerms = removedSearchTerms + searchTermToken + " ";
					}

					setRemSearchTerms(removedSearchTerms);
					strSearchTerm = strSearchTerm.trim();
					if (!("").equals(strSearchTerm)) {
						searchTerm = strSearchTerm;
						return "retrySearch";
					}
				}
			} else if (isSingleItem()) {
				// First checking if its a single item and doing the redirection with out making any other DB calls
				YFCNode yfcNode = YFCDocument.getDocumentFor(getOutDoc()).getDocumentElement().getChildElement("ItemList").getFirstChild();
				setItemID(yfcNode.getAttributes().get("ItemID"));
				setUnitOfMeasure(yfcNode.getAttributes().get("UnitOfMeasure"));
				return "singleItem";
			}
			long startTimeCustomerService = System.currentTimeMillis();

			getAllAPIOutput();
			long endTimeCustomerService = System.currentTimeMillis();
			timespent = (endTimeCustomerService - startTimeCustomerService);
			sb.append("\nCustom Service Execution time on catlaog newSearch() = " + timespent);
			setItemsUomsMap();
			setAttributeListForUI();
			prepareItemBranchInfoBean();
			setColumnListForUI();

			path = tempCategoryPath;
			categoryPath = path;

			setStockedItemFromSession();
			getCatTwoDescFromItemIdForpath(getOutDoc().getDocumentElement(), categoryPath);
			endTime = System.currentTimeMillis();
			timespent = (endTime - startTime);
			sb.append("\nTotal time of Action execution on catlaog newSearch() = " + timespent);
			log.info(sb.toString());
		} catch (Exception exception) {
			// Not throwing any exception as it gives exception for JIRA 3705
			log.error("Error while refeshing catalog cache in method newSearch", exception);
			XPEDXWCUtils.logExceptionIntoCent(exception); // JIRA 4289
		}

		return SUCCESS;
	}

	private void getAllAPIOutput() throws Exception {
		Document catDoc = getOutDoc();

		if (catDoc == null) {
			Document rootDoc = SCXmlUtil.createDocument("CatalogSearch");
			Element rootEle = rootDoc.getDocumentElement();
			rootEle.setAttribute("CallingOrganizationCode", wcContext.getStorefrontId());
			rootEle.setAttribute("CategoryDepth", "2");
			rootEle.setAttribute("DisplayLocalizedFieldInLocale", "en_US_EST");
			rootEle.setAttribute("PageNumber", "1");
			rootEle.setAttribute("PageSize", "20");
			rootEle.setAttribute("SortDescending", "");
			rootEle.setAttribute("SortField", "Item.ExtnBestMatch");
			rootEle.setAttribute("TotalHits", "0");
			rootEle.setAttribute("TotalPages", "0");
			Element sortFieldListEle = SCXmlUtil.createChild(rootEle, "SortFieldList");
			Element sortFieldEle1 = SCXmlUtil.createChild(sortFieldListEle, "SortField");
			sortFieldEle1.setAttribute("IndexFieldName", "Item.ItemID");
			Element sortFieldEle2 = SCXmlUtil.createChild(sortFieldListEle, "SortField");
			sortFieldEle2.setAttribute("IndexFieldName", "Item.SortableShortDescription");
			Element sortFieldEle3 = SCXmlUtil.createChild(sortFieldListEle, "SortField");
			sortFieldEle3.setAttribute("IndexFieldName", "Item.ExtnMpc");
			Element sortFieldEle4 = SCXmlUtil.createChild(sortFieldListEle, "SortField");
			sortFieldEle4.setAttribute("IndexFieldName", "Item.ExtnSize");
			Element sortFieldEle5 = SCXmlUtil.createChild(sortFieldListEle, "SortField");
			sortFieldEle5.setAttribute("IndexFieldName", "Item.ExtnColor");
			Element sortFieldEle6 = SCXmlUtil.createChild(sortFieldListEle, "SortField");
			sortFieldEle6.setAttribute("IndexFieldName", "Item.ExtnBestMatch");
			Element sortFieldEle7 = SCXmlUtil.createChild(sortFieldListEle, "SortField");
			sortFieldEle7.setAttribute("IndexFieldName", "Item.ExtnBasis");
			Element sortFieldEle8 = SCXmlUtil.createChild(sortFieldListEle, "SortField");
			sortFieldEle8.setAttribute("IndexFieldName", "Item.ExtnPackMethod");
			Element sortFieldEle9 = SCXmlUtil.createChild(sortFieldListEle, "SortField");
			sortFieldEle9.setAttribute("IndexFieldName", "Item.ExtnMwt");
			Element sortFieldEle10 = SCXmlUtil.createChild(sortFieldListEle, "SortField");
			sortFieldEle10.setAttribute("IndexFieldName", "Item.ExtnMaterial");
			Element sortFieldEle11 = SCXmlUtil.createChild(sortFieldListEle, "SortField");
			sortFieldEle11.setAttribute("IndexFieldName", "Item.ExtnForm");
			Element sortFieldEle12 = SCXmlUtil.createChild(sortFieldListEle, "SortField");
			sortFieldEle12.setAttribute("IndexFieldName", "Item.ExtnCapacity");
			Element sortFieldEle13 = SCXmlUtil.createChild(sortFieldListEle, "SortField");
			sortFieldEle13.setAttribute("IndexFieldName", "Item.ExtnModel");
			Element sortFieldEle14 = SCXmlUtil.createChild(sortFieldListEle, "SortField");
			sortFieldEle14.setAttribute("IndexFieldName", "Item.ExtnGauge");
			Element sortFieldEle15 = SCXmlUtil.createChild(sortFieldListEle, "SortField");
			sortFieldEle15.setAttribute("IndexFieldName", "Item.ExtnThickness");
			Element sortFieldEle16 = SCXmlUtil.createChild(sortFieldListEle, "SortField");
			sortFieldEle16.setAttribute("IndexFieldName", "Item.ExtnPly");
			Element sortFieldEle17 = SCXmlUtil.createChild(sortFieldListEle, "SortField");
			sortFieldEle17.setAttribute("IndexFieldName", "Item.ExtnVendorNo");
			Element sortFieldEle18 = SCXmlUtil.createChild(sortFieldListEle, "SortField");
			sortFieldEle18.setAttribute("IndexFieldName", "Item.ExtnCert");
			Element categoryDomainEle = SCXmlUtil.createChild(rootEle, "CategoryDomain");
			categoryDomainEle.setAttribute("AttributeName", "");
			categoryDomainEle.setAttribute("CategoryDomain", YFSSystem.getProperty("xpedx.searhindex.categoryDomain"));
			categoryDomainEle.setAttribute("Description", "Master Catalog for " + wcContext.getStorefrontId());
			categoryDomainEle.setAttribute("IsClassification", "N");
			categoryDomainEle.setAttribute("ShortDescription", "Master Catalog");
			Element itemListEle = SCXmlUtil.createChild(rootEle, "ItemList");
			itemListEle.setAttribute("Currency", "USD");
			catDoc = rootDoc;
			setOutDoc(rootDoc);
			setErrorCode("Your search returned too many items. Please refine your search and try again.");
		}

		if ("".equals(errorCode) && ("0").equals(getOutDoc().getDocumentElement().getAttribute("TotalHits"))) {
			setErrorCode("Your search did not yield any results. Please try again.");
		} else if ("".equals(errorCode) && !"".equals(tryAgain)) {
			setErrorCode("Your search returned too many items. Therefore \"" + getRemSearchTerms().trim() + "\" was removed from the search term.");
		}

		// get the CatalogSearch/ItemList
		Element itemListElement = SCXmlUtil.getChildElement(catDoc.getDocumentElement(), "ItemList");
		NodeList itemNodeList = itemListElement.getElementsByTagName("Item");
		ArrayList<String> itemIds = new ArrayList<String>();

		for (int i = 0; i < itemNodeList.getLength(); i++) {
			itemIds.add(SCXmlUtil.getAttribute((Element) itemNodeList.item(i), "ItemID"));
		}
		XPEDXShipToCustomer shipToCustomer = (XPEDXShipToCustomer) XPEDXWCUtils
				.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);

		try {
			if (shipToCustomer == null) {
				XPEDXWCUtils.setCustomerObjectInCache(XPEDXWCUtils.getCustomerDetails(wcContext.getCustomerId(), wcContext.getStorefrontId()).getDocumentElement());
				shipToCustomer = (XPEDXShipToCustomer) XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
			}
		} catch (Exception e) {
			log.error("Error fetching or creating the shipToCustomer object in to the session in getXpxItemCustXRefDoc() So returning back null");
		}

		if (!(itemIds != null && itemIds.size() > 0)) {
			return;
		}

		if (shipToCustomer != null) {
			String envCode = shipToCustomer.getExtnEnvironmentCode();
			String legacyCustomerNumber = shipToCustomer
					.getExtnLegacyCustNumber();
			//String custDivision = shipToCustomer.getExtnShipFromBranch();
			String custDivision = shipToCustomer.getExtnCustomerDivision();
			HashMap<String, String> valueMap = new HashMap<String, String>();
			valueMap.put("/XPXItemcustXref/@EnvironmentCode", envCode);
			valueMap.put("/XPXItemcustXref/@CustomerNumber", legacyCustomerNumber);
			// valueMap.put("/XPXItemcustXref/@LegacyItemNumber", itemId);
			// Filled using Complex Query
			valueMap.put("/XPXItemcustXref/@CustomerDivision", custDivision);

			Element xrefInput = null;
			Element res = null;
			try {
				xrefInput = WCMashupHelper.getMashupInput("XPEDXMyItemsListGetCustomersPart", valueMap, wcContext.getSCUIContext());
				Element complexQuery = xrefInput.getOwnerDocument().createElement("ComplexQuery");
				xrefInput.appendChild(complexQuery);
				Element Or = xrefInput.getOwnerDocument().createElement("Or");
				complexQuery.appendChild(Or);
				Iterator<String> itemIdIter = itemIds.iterator();

				String customerId = wcContext.getCustomerId();
				Map<String, String> valueMaps = new HashMap<String, String>();
				valueMaps.put("/PricelistAssignment//@CustomerID", customerId);
				valueMaps.put("/PricelistAssignment//@ExtnPriceWareHouse", shipToCustomer.getExtnPriceWareHouse());
				valueMaps.put("/PricelistAssignment/PricelistLine/Item/@OrganizationCode", wcContext.getStorefrontId());
				Element pricLlistAssignmentInput = WCMashupHelper.getMashupInput("xpedxYpmPriceLinelistAssignmentList", valueMaps, getWCContext().getSCUIContext());
				Document pricLlistAssignmentInputDoc = pricLlistAssignmentInput.getOwnerDocument();
				NodeList pricLlistAssignmentInputNodeList = pricLlistAssignmentInput.getElementsByTagName("Or");
				Element pricLlistAssignmentInputElemt = (Element) pricLlistAssignmentInputNodeList.item(0);
				Element input = WCMashupHelper.getMashupInput("xpedxgetAllAPI", wcContext.getSCUIContext());

				YFCDocument inputDocument = YFCDocument.createDocument("UOMList");
				YFCElement documentElement = inputDocument.getDocumentElement();

				documentElement.setAttribute("CustomerID", customerId);

				// workaround for eb-2035: hard-code 'xpedx' storefrontId here due to brand-specific records in the yfs_item_uom_master table
				// this avoids duplicating UOMs per brand in the Sterling configuration, which is not desired as of August 2013
				documentElement.setAttribute("OrganizationCode", "xpedx");

				YFCElement complexQueryElement = documentElement.createChild("ComplexQuery");
				YFCElement complexQueryOrElement = documentElement.createChild("Or");

				complexQueryElement.setAttribute("Operator", "AND");
				complexQueryElement.appendChild(complexQueryOrElement);
				Element customerDetails = SCXmlUtil.createChild(inputDocument.getDocument().getDocumentElement(), "CustomerDetails");
				customerDetails.setAttribute("ExtnEnvironmentCode", shipToCustomer.getExtnEnvironmentCode());
				customerDetails.setAttribute("ExtnShipFromBranch", shipToCustomer.getExtnShipFromBranch());
				customerDetails.setAttribute("ExtnCustomerDivision", shipToCustomer.getExtnCustomerDivision());
				customerDetails.setAttribute("ExtnUseOrderMulUOMFlag", shipToCustomer.getExtnUseOrderMulUOMFlag());

				valueMap = new HashMap<String, String>();
				valueMap.put("/XPXItemExtn/@XPXDivision", shipToCustomer.getExtnShipFromBranch());
				valueMap.put("/XPXItemExtn/@EnvironmentID", envCode);

				Element xpxItemExtninputElem = WCMashupHelper.getMashupInput("xpedxXPXItemExtnList", valueMap, wcContext.getSCUIContext());

				Document inputDoc = xpxItemExtninputElem.getOwnerDocument();
				NodeList inputNodeList = inputDoc.getElementsByTagName("Or");
				Element inputNodeListElemt = (Element) inputNodeList.item(0);
				itemIdIter = itemIds.iterator();
				orderMultipleMap = new HashMap<String, String>();
				while (itemIdIter.hasNext()) {
					// Complex query for XPXItemExtn input xml
					Element expElement = SCXmlUtil.createChild(inputNodeListElemt, "Exp");
					String itemid = itemIdIter.next();
					expElement.setAttribute("Name", "ItemID");
					expElement.setAttribute("Value", itemid);

					// Complex query for itemXef input xml
					Element exp1Element = xrefInput.getOwnerDocument().createElement("Exp");
					exp1Element.setAttribute("Name", "LegacyItemNumber");
					exp1Element.setAttribute("QryType", "EQ");
					exp1Element.setAttribute("Value", itemid);
					SCXmlUtil.importElement(Or, exp1Element);

					// Complex query for PriceLineList input xml
					Document expDoc = YFCDocument.createDocument("Exp").getDocument();
					Element exp2Element = expDoc.getDocumentElement();
					exp2Element.setAttribute("Name", "ItemID");
					exp2Element.setAttribute("Value", itemid);
					pricLlistAssignmentInputElemt.appendChild(pricLlistAssignmentInputDoc.importNode(exp2Element, true));

					// UOMList input XML
					YFCElement exp3Element = documentElement.createChild("Exp");
					exp3Element.setAttribute("Name", "ItemID");
					exp3Element.setAttribute("Value", itemid);
					complexQueryOrElement.appendChild((YFCNode) exp3Element);
					orderMultipleMap.put(itemid, "1");
				}

				input.appendChild(input.getOwnerDocument().importNode(xrefInput, true));
				input.appendChild(input.getOwnerDocument().importNode(pricLlistAssignmentInput, true));
				input.appendChild(input.getOwnerDocument().importNode(xpxItemExtninputElem, true));
				input.appendChild(input.getOwnerDocument().importNode(inputDocument.getDocument().getDocumentElement(), true));

				allAPIOutputDoc = (Element) WCMashupHelper.invokeMashup("xpedxgetAllAPI", input, wcContext.getSCUIContext());

				getOrderMultipleMapForItems();
				String shipFromBranch = shipToCustomer.getExtnShipFromBranch();
				getReplacmentItemsMapForItems(envCode, shipFromBranch);
				wcContext.setWCAttribute("replacmentItemsMap", replacmentItemsMap, WCAttributeScope.REQUEST);
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

	private boolean isSingleItem() {
		YFCDocument yfcDocument = YFCDocument.getDocumentFor(getOutDoc());
		if (yfcDocument != null && yfcDocument.getDocumentElement() != null) {
			YFCElement yfcElement = yfcDocument.getDocumentElement().getChildElement("ItemList");
			if (yfcElement != null && yfcElement.getChildNodes() != null) {
				int length = yfcElement.getChildNodes().getLength();
				if (length == 1) {
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * Added for JIRA EB-1731
	 */
	private void setAttributeListForUIForNarrowBy() {
		NodeList FacetList = SCXmlUtil.getXpathNodes(getOutDoc().getDocumentElement(), "/CatalogSearch/FacetList/ItemAttribute");
		attributeMap = new HashMap();
		for (int i = 0; i < FacetList.getLength(); i++) {
			// Added for JIRA 3821
			Element facetEle = (Element) FacetList.item(i);
			Element itemattr = SCXmlUtil.getChildElement(facetEle, "Attribute");
			String shortDesc = itemattr.getAttribute("ShortDescription");
			if (facetDivShortDescription != null && !facetDivShortDescription.equalsIgnoreCase(shortDesc)) {
				continue;
			}
			List<Element> itemAttribute = SCXmlUtil.getElements(facetEle, "AssignedValueList/AssignedValue");
			Collections.sort(itemAttribute, new Comparator<Element>() {
				@Override
				public int compare(Element elem, Element elem1) {
					String attrValue = elem.getAttribute("Value");
					String attrValue1 = elem1.getAttribute("Value");
					if (isDouble(attrValue) && isDouble(attrValue1)) {
						return Double.valueOf(attrValue).compareTo(Double.valueOf(attrValue1));
					}
					return attrValue.compareTo(attrValue1);
				}
			});

			facetListMap.put(shortDesc, itemAttribute);
			// narrowByDivCount.put(shortDesc, 1);
			// End of JIRA 3821
			String attrName = facetEle.getAttribute("ItemAttributeName");
			String isFiltered = facetEle.getAttribute("IsProvidedFilter");
			if (!(isFiltered != null && isFiltered.trim().length() > 0 && "Y".equals(isFiltered))) {
				attributeMap.put(attrName, attrName);
			}
		}
	}

	private void setAttributeListForUI() {
		NodeList FacetList = SCXmlUtil.getXpathNodes(getOutDoc().getDocumentElement(), "/CatalogSearch/FacetList/ItemAttribute");
		attributeMap = new HashMap();
		for (int i = 0; i < FacetList.getLength(); i++) {
			// Added for JIRA 3821
			Element facetEle = (Element) FacetList.item(i);
			Element itemattr = SCXmlUtil.getChildElement(facetEle, "Attribute");
			String shortDesc = itemattr.getAttribute("ShortDescription");
			List<Element> itemAttribute = SCXmlUtil.getElements(facetEle, "AssignedValueList/AssignedValue");
			Collections.sort(itemAttribute, new Comparator<Element>() {
				@Override
				public int compare(Element elem, Element elem1) {
					String attrValue = elem.getAttribute("Value");
					String attrValue1 = elem1.getAttribute("Value");
					if (isDouble(attrValue) && isDouble(attrValue1)) {
						return Double.valueOf(attrValue).compareTo(Double.valueOf(attrValue1));
					}
					return attrValue.compareTo(attrValue1);
				}
			});
			facetListMap.put(shortDesc, itemAttribute);
			// End of JIRA 3821
			String attrName = facetEle.getAttribute("ItemAttributeName");
			String isFiltered = facetEle.getAttribute("IsProvidedFilter");
			if (!(isFiltered != null && isFiltered.trim().length() > 0 && "Y".equals(isFiltered))) {
				attributeMap.put(attrName, attrName);
			}
		}
	}

	/*
	 * @see com.sterlingcommerce.webchannel.catalog.CatalogAction#navigate()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String navigate() {
		try {
			long catalogLoadStartTime = System.currentTimeMillis();

			/* Start of webtrend tags */
			setsearchMetaTag(false);
			/* End of webtrend tags */

			init();
			if (draft != null && !YFCCommon.isVoid(draft) && "N".equals(draft)) {
				XPEDXWCUtils.setEditedOrderHeaderKeyInSession(wcContext, editedOrderHeaderKey);
			}

			List<Breadcrumb> bcl = BreadcrumbHelper.preprocessBreadcrumb(this.get_bcs_());
			log.debug("CatalogAction : navigate(): start");
			Breadcrumb lastBc = bcl.get(bcl.size() - 1);
			Map<String, String> params = lastBc.getParams();
			String[] pathDepth = StringUtils.split(path, "/");
			path = params.get("path");

			if (log.isDebugEnabled()) {
				// Added for debugging Breadcrumb parameters
				for (int i = 0; i < bcl.size(); i++) {
					Breadcrumb bc = bcl.get(i);
					Map<String, String> bcParams = bc.getParams();
					String cnameValue = bcParams.get("cname");
					log.debug("CatalogAction : navigate(): Breadcrumb : cnameValue=" + cnameValue);
				}
			}

			if (("").equals(super.sortField) && (super.session.get("sortField") == null || ("").equals(super.session.get("sortField")))) {
				orderByAttribute = "Item.ExtnBestMatch";
				sortField = "Item.ExtnBestMatch--A";
			}
			// Added below condn for XBT-269
			if ("2".equals(categoryDepthNarrowBy)) {
				setCategoryDepth("2");
			}

			else if (bcl.size() > 1 || (!("true".equals(displayAllCategories)))) {
				if (!YFCCommon.isVoid(pathDepth) && pathDepth.length == 2) {
					// for c1 categories-> show all the sub categories of C1 in the landing page
					setCategoryDepth("1"); // default value is 2; coming from struts file
				}
			}
			// getting the customer bean object from session.
			shipToCustomer = (XPEDXShipToCustomer) XPEDXWCUtils
					.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);

			// determine if the request is catalog landing or catalog page
			boolean isCategoryLanding = determineCatalogLandingRedirection(bcl, pathDepth);
			// Added below condn for XBT-269
			if (!"2".equals(categoryDepthNarrowBy) && isCategoryLanding) {
				getWCContext().removeWCAttribute("StockedCheckbox", WCAttributeScope.SESSION);
				// call the catalog landing mashup
				setMashupID(getCatalogLandingMashupID());
			}

			String returnString = super.navigate();
			// XBT-260

			if (ERROR.equals(returnString)) {
				return returnString;
			} else {
				setAttributeListForUI();
				// Added below condn for XBT-269
				if (!"2".equals(categoryDepthNarrowBy) && (isCategoryLanding || displayAllCategories.equalsIgnoreCase("true"))) {
					log.debug("Search for Category Domain. Need to show the asset widget for Category Images");
					return setCategoryDomainAssetList();
				}

				if (wcContext.isGuestUser()) {
					isGuestUser = "Y";
				}
				changeBasis();
				getAllAPIOutput();
				setItemsUomsMap();
				prepareItemBranchInfoBean();
				setColumnListForUI();
				// prepareMyItemListList();
				getSortFieldDocument();
			}

			getCatTwoDescFromItemIdForpath(getOutDoc().getDocumentElement(), path);

			long catalogLoadEndTime = System.currentTimeMillis();
			log.info("Time taken in milliseconds to load the catalog with Narrow By : " + (catalogLoadEndTime - catalogLoadStartTime));
		} catch (Exception exception) {
			// Not throwing any exception as it gives exception for JIRA 3705
			log.error("Error while refeshing catalog cache in method navigate", exception);
		}

		return SUCCESS;
	}

	private boolean determineCatalogLandingRedirection(List<Breadcrumb> bcl, String[] pathDepth) {
		boolean isCategoryDomain = false;
		if (null == bcl || bcl.size() == 1) {
			isCategoryDomain = true;
		} else if (bcl.size() > 1) {
			if (YFCCommon.isVoid(pathDepth) || pathDepth.length <= 2) {
				isCategoryDomain = true;
			}
		}
		if (isCategoryC3() == true) {
			isCategoryDomain = false;
		}
		return isCategoryDomain;
	}

	protected void setItemsUomsMap() {
		try {
			ArrayList<Element> itemList = getXMLUtils().getElements(getOutDoc().getDocumentElement(), "//ItemList/Item");
			if (itemList != null) {
				for (int i = 0; i < itemList.size(); i++) {
					String itemId = itemList.get(i).getAttribute("ItemID");
					if (i == 0) {
						// Added For XBT-253
						firstItem = itemId;
						Element catagoryElement = (Element) itemList.get(i).getElementsByTagName("Category").item(0);
						if (catagoryElement != null) {
							String catDescription = catagoryElement.getAttribute("ShortDescription");

							tempCategoryPath = catagoryElement.getAttribute("CategoryPath");

							if (tempCategoryPath != null) {
								String[] descriptions = tempCategoryPath.split("/");
								if (descriptions != null) {
									if (descriptions.length == 4) {
										firstItemCategoryShortDescription = catDescription;
									}
								}
							}
						}
					}

					if (itemIDList.contains(itemId)) {
						continue;
					}
					itemIDList.add(itemId);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			// TODO: handle exception
		}

		if (itemIDList.size() > 0) {
			XPEDXCustomerContactInfoBean xpedxCustomerContactInfoBean = (XPEDXCustomerContactInfoBean) XPEDXWCUtils
					.getObjectFromCache(XPEDXConstants.XPEDX_Customer_Contact_Info_Bean);
			if (xpedxCustomerContactInfoBean != null && xpedxCustomerContactInfoBean.getMsapExtnUseOrderMulUOMFlag() != null
					&& xpedxCustomerContactInfoBean.getMsapExtnUseOrderMulUOMFlag() != "") {
				msapOrderMultipleFlag = xpedxCustomerContactInfoBean.getMsapExtnUseOrderMulUOMFlag();
			}

			wcContext.setWCAttribute("orderMultipleMap", orderMultipleMap, WCAttributeScope.REQUEST);
			itemUomHashMap = getXpedxUOMList();

			wcContext.setWCAttribute("itemUomHashMap", itemUomHashMap, WCAttributeScope.REQUEST);
			wcContext.setWCAttribute("defaultShowUOMMap", defaultShowUOMMap, WCAttributeScope.REQUEST);
			wcContext.setWCAttribute("itemCustomerUomMap", itemCustomerUomMap, WCAttributeScope.REQUEST);

			double minFractUOM = 0.00;
			double maxFractUOM = 0.00;
			String lowestUOM = "";
			String highestUOM = "";
			String minUOMsDesc = "";
			String maxUOMsDesc = "";
			String defaultConvUOM = "";
			String defaultUOM = "";
			String orderMultiple = "";
			String customUom = "";

			defaultShowUOMMap = new HashMap<String, String>();
			if (itemUomHashMap != null && itemUomHashMap.size() > 0) {
				for (int i = 0; i < itemIDList.size(); i++) {
					Map displayUomMap = new HashMap<String, String>();
					String strItemID = itemIDList.get(i);
					displayUomMap = itemUomHashMap.get(strItemID);
					defaultUOM = "";
					defaultConvUOM = "";
					minFractUOM = 0.00;
					maxFractUOM = 0.00;
					lowestUOM = "";
					highestUOM = "";
					minUOMsDesc = "";
					maxUOMsDesc = "";
					Map<String, String> uomIsCustomermap = itemUomIsCustomerUomHashMap
							.get(strItemID);
					customUom = "";
					for (Iterator it = displayUomMap.keySet().iterator(); it.hasNext();) {
						try {
							String uom = (String) it.next();
							Object objConversionFactor = displayUomMap.get(uom);
							String isCustomerUom = "N";
							if (uomIsCustomermap != null && uomIsCustomermap.get(uom) != null) {
								isCustomerUom = (String) uomIsCustomermap.get(uom);
								customUom = uom;
							}
							// Start- Code added to fix XNGTP 2964
							orderMultiple = orderMultipleMap.get(strItemID);
							if ("Y".equals(msapOrderMultipleFlag) && Integer.valueOf(orderMultiple) > 1 && !"1".equals(objConversionFactor)) {
								// orderMultiple = "12";
								if (objConversionFactor.toString() == orderMultiple) {
									minFractUOM = 1;
									lowestUOM = uom;
									minUOMsDesc = XPEDXWCUtils.getUOMDescription(lowestUOM) + " (" + Math.round(Double.parseDouble((String) objConversionFactor)) + ")";

								} else {
									double conversion = getConversion(objConversionFactor, orderMultiple);
									if (conversion != -1 && uom != null && uom.length() > 0) {
										if (conversion <= 1 && conversion >= minFractUOM) {
											minFractUOM = conversion;
											lowestUOM = uom;
											minUOMsDesc = XPEDXWCUtils.getUOMDescription(lowestUOM) + " (" + Math.round(Double.parseDouble((String) objConversionFactor)) + ")";

										} else if (conversion > 1 && (conversion < maxFractUOM || maxFractUOM == 0)) {
											maxFractUOM = conversion;
											highestUOM = uom;
											maxUOMsDesc = XPEDXWCUtils.getUOMDescription(highestUOM) + " (" + Math.round(Double.parseDouble((String) objConversionFactor)) + ")";

										}
									}
								}
								// End - Code added to fix XNGTP 2964

							}

							if (objConversionFactor != null) {
								uomConvFactorMap.put(uom, objConversionFactor.toString());
							}
							if (isCustomerUom.equalsIgnoreCase("Y")) { // Show only UOM code without M_
								if ("0".equals(objConversionFactor) || "1".equals(objConversionFactor)) {
									displayUomMap.put(uom, uom.substring(2, uom.length()));
								} else {
									if (null != objConversionFactor && !"".equals(objConversionFactor)) {// JIRA  1391 - Displaying an Integer instead of a decimal.
										displayUomMap.put(uom, uom.substring(2, uom.length()) + " (" + Math.round(Double.parseDouble((String) objConversionFactor)) + ")");
									}
								}
							} else {
								if ("0".equals(objConversionFactor)
										|| "1".equals(objConversionFactor)) {
									displayUomMap
											.put(uom, XPEDXWCUtils
													.getUOMDescription(uom));
								} else {
									if (null != objConversionFactor && !"".equals(objConversionFactor)) {// JIRA 1391 - Displaying an Integer instead of a decimal.
										displayUomMap.put(uom, XPEDXWCUtils.getUOMDescription(uom) + " (" + Math.round(Double.parseDouble((String) objConversionFactor)) + ")");
									}
								}
							}

						} catch (Exception e) {
							log.error("Error while getting the UOM Description.....");
							log.error("", e);
						}
					}
					if (uomIsCustomermap != null && customUom.length() > 0) {
						defaultUOM = customUom.substring(2, customUom.length());
					} else {
						// Start- Code added to fix XNGTP 2964
						if (minFractUOM == 1.0 && minFractUOM != 0.0) {
							defaultConvUOM = lowestUOM;
							defaultUOM = minUOMsDesc;

						} else if (maxFractUOM > 1.0) {
							defaultConvUOM = highestUOM;
							defaultUOM = maxUOMsDesc;

						} else {
							defaultConvUOM = lowestUOM;
							defaultUOM = minUOMsDesc;
						}
					}

					if (!SCUtil.isVoid(defaultUOM)) {
						defaultShowUOMMap.put(strItemID, defaultUOM);
					}

					itemUomHashMap.put(strItemID, displayUomMap);
				}
			}
		}
		wcContext.setWCAttribute("itemUomHashMap", itemUomHashMap, WCAttributeScope.REQUEST);
		wcContext.setWCAttribute("uomConvFactorMap", uomConvFactorMap, WCAttributeScope.REQUEST);
		wcContext.setWCAttribute("defaultShowUOMMap", defaultShowUOMMap, WCAttributeScope.REQUEST);
	}

	private void getOrderMultipleMapForItems() {
		ArrayList<Element> xpxItemExtnList = SCXmlUtil.getElements(allAPIOutputDoc, "XPXItemExtnList/XPXItemExtn");
		if (xpxItemExtnList != null) {
			for (Element xpxItemExtn : xpxItemExtnList) {
				if (xpxItemExtn != null) {
					orderMultipleMap.put(xpxItemExtn.getAttribute("ItemID"), xpxItemExtn.getAttribute("OrderMultiple"));
				}
			}
		}
	}

	private void getReplacmentItemsMapForItems(String envCode,
			String shipFromDivision) {
		String custID = wcContext.getCustomerId();
		replacmentItemsMap = new LinkedHashMap<String, List<Element>>();
		if (allAPIOutputDoc != null) {
			Element wElement = (Element) allAPIOutputDoc.getElementsByTagName("ItemList").item(0);
			NodeList wNodeList = wElement.getChildNodes();
			if (wNodeList != null) {
				int length = wNodeList.getLength();
				for (int i = 0; i < length; i++) {
					Node wNode = wNodeList.item(i);
					if (wNode != null) {
						NamedNodeMap nodeAttributes = wNode.getAttributes();
						if (nodeAttributes != null) {
							Node itemId = nodeAttributes.getNamedItem("ItemID");
							if (itemId != null) {
								try {
									ArrayList<Element> replacementAssociatedItems = new ArrayList<Element>();
									ArrayList<String> itemIDListForGetCompleteItemList = new ArrayList<String>();
									Document XPXItemExtnListElement = null;

									XPXItemExtnListElement = XPEDXOrderUtils.getXPEDXItemAssociation(custID, shipFromDivision, itemId.getTextContent(), getWCContext());

									List<Element> xPXItemExtn = XMLUtilities.getElements(XPXItemExtnListElement.getDocumentElement(),
											"XPXItemExtn[@ItemID='" + itemId.getNodeValue() + "']");
									if (xPXItemExtn != null && xPXItemExtn.size() > 0) {
										Iterator iterxPXItemExtn = xPXItemExtn.iterator();
										List<Element> replacementList = new ArrayList<Element>();
										while (iterxPXItemExtn.hasNext()) {
											Element xPXItemExtnElement = (Element) iterxPXItemExtn.next();
											// Check the ItemExtn for the
											// current customer
											String companyCode = xPXItemExtnElement.getAttribute("CompanyCode");
											String environmentCode = xPXItemExtnElement.getAttribute("EnvironmentID");
											String division = xPXItemExtnElement.getAttribute("XPXDivision");
											if (shipFromDivision != null && envCode != null
													&& (!(shipFromDivision.equalsIgnoreCase(division) && envCode.equalsIgnoreCase(environmentCode)))) {
												continue;
											}
											try {
												Element xPXItemAssociationsListElement = SCXmlUtil.getChildElement(xPXItemExtnElement, "XPXItemAssociationsList");
												// Add the Replacement Items
												List<Element> replacementItemList = XMLUtilities.getElements(xPXItemAssociationsListElement,
														"XPXItemAssociations[@AssociationType='R']");
												if (replacementItemList != null && replacementItemList.size() > 0) {
													replacementList.addAll(replacementItemList);
												}
											}// try block ends
											catch (XPathExpressionException e) {

											}
										}

										ArrayList<String> repItemIds = new ArrayList<String>();
										// Get the replacement items
										if (null != replacementList && replacementList.size() >= 0) {
											for (Element repItem : replacementList) {
												String associatedItemID = SCXmlUtil.getAttribute(repItem, "AssociatedItemID");
												if (!YFCCommon.isVoid(associatedItemID) && !associatedItemID.equals("")) {
													if (!itemIDListForGetCompleteItemList.contains(associatedItemID)) {
														itemIDListForGetCompleteItemList.add(associatedItemID);
													}
													repItemIds.add(associatedItemID);
												}
											}
										}

										// call getCompleteItemList
										Document itemDetailsListDoc = null;
										try {
											// invoking a different function which will give onyl the entitiled items - 734
											itemDetailsListDoc = XPEDXOrderUtils.getXpedxEntitledItemDetails(itemIDListForGetCompleteItemList, custID, wcContext.getStorefrontId(), wcContext);
										} catch (Exception e) {
											LOG.error("Exception while getting item details for associated items", e);
											return;
										}
										if (itemDetailsListDoc != null) {
											NodeList itemDetailsList = itemDetailsListDoc.getElementsByTagName("Item");
											for (int j = 0; j < itemDetailsList.getLength(); j++) {
												Element curritem = (Element) itemDetailsList.item(j);
												String curritemID = XMLUtils.getAttributeValue(curritem, "ItemID");
												if (repItemIds.contains(curritemID)) {
													replacementAssociatedItems.add(curritem);
												}
											}
										}
									}
									replacmentItemsMap.put(itemId.getTextContent(), replacementAssociatedItems);
								} catch (Exception e1) {
									LOG.error("Error getting the Item Branch Information for Item id " + itemID);
									e1.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	}

	private Map<String, Map<String, String>> getXpedxUOMList() {

		LinkedHashMap<String, Map<String, String>> itemUomHashMap = new LinkedHashMap<String, Map<String, String>>();

		IWCContext context = WCContextHelper.getWCContext(ServletActionContext.getRequest());
		SCUIContext wSCUIContext = context.getSCUIContext();
		ISCUITransactionContext scuiTransactionContext = wSCUIContext.getTransactionContext(true);
		YFSEnvironment env = (YFSEnvironment) scuiTransactionContext.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);
		try {

			if (allAPIOutputDoc != null) {
				String isCustomerUOMFlg = "";
				Element wElement = (Element) allAPIOutputDoc.getElementsByTagName("ItemList").item(0);
				NodeList wNodeList = wElement.getChildNodes();
				if (wNodeList != null) {
					int length = wNodeList.getLength();
					String conversion;
					for (int i = 0; i < length; i++) {
						Node wNode = wNodeList.item(i);
						if (wNode != null) {
							NamedNodeMap nodeAttributes = wNode.getAttributes();
							if (nodeAttributes != null) {
								Node itemId = nodeAttributes.getNamedItem("ItemID");
								if (itemId != null) {
									LinkedHashMap<String, String> wUOMsAndConFactors = new LinkedHashMap<String, String>();
									LinkedHashMap<String, String> wUOMsAndCustomerUOMFlag = new LinkedHashMap<String, String>();
									NodeList uomListNodeList = wNode.getChildNodes();
									Node uomListNode = uomListNodeList.item(0);

									if (uomListNode != null) {
										List<Element> listOfUOMElements = SCXmlUtil.getChildrenList((Element) uomListNode);
										Collections.sort(listOfUOMElements, new XpedxSortUOMListByConvFactor());

										for (Element uomNode : listOfUOMElements) {

											if (uomNode != null) {
												NamedNodeMap uomAttributes = uomNode.getAttributes();
												if (uomAttributes != null) {
													Node UnitOfMeasure = uomAttributes.getNamedItem("UnitOfMeasure");
													Node Conversion = uomAttributes.getNamedItem("Conversion");
													Node CustomerUOmFlag = uomAttributes.getNamedItem("IsCustUOMFlag");

													isCustomerUOMFlg = "";
													if (UnitOfMeasure != null && Conversion != null) {
														conversion = Conversion.getTextContent();
														if (CustomerUOmFlag != null) {
															isCustomerUOMFlg = CustomerUOmFlag.getTextContent();
															Map<String, String> uomIsCustomermap = new LinkedHashMap<String, String>();
															uomIsCustomermap.put(UnitOfMeasure.getTextContent(), isCustomerUOMFlg);
															itemUomIsCustomerUomHashMap.put(itemId.getTextContent(), uomIsCustomermap);
														}
														if (!YFCUtils.isVoid(conversion)) {
															long convFactor = Math.round(Double.parseDouble(conversion));
															wUOMsAndConFactors.put(UnitOfMeasure.getTextContent(), Long.toString(convFactor));

														}
														if (!YFCUtils.isVoid(isCustomerUOMFlg)) {
															wUOMsAndCustomerUOMFlag.put(UnitOfMeasure.getTextContent(), isCustomerUOMFlg);
															itemCustomerUomMap.put(itemId.getTextContent(), UnitOfMeasure.getTextContent());
														} else {
															wUOMsAndCustomerUOMFlag.put(UnitOfMeasure.getTextContent(), "N");
														}
													}
												}
											}
										}
									}

									itemUomHashMap.put(itemId.getTextContent(), wUOMsAndConFactors);
								}
							}
						}
					}
				}
			}

		} catch (Exception ex) {
			log.error(ex.getMessage());
		} finally {
			scuiTransactionContext.end();
			env.clearApiTemplate("XPXUOMListAPI");
			SCUITransactionContextHelper.releaseTransactionContext(
					scuiTransactionContext, wSCUIContext);
			env = null;
		}
		return itemUomHashMap;

	}

	protected void prepareMyItemListList() {
		itemListMap = new HashMap();
		String customerId = wcContext.getCustomerId();
		Document myItemListOutputDoc;
		try {
			myItemListOutputDoc = XPEDXWCUtils.getAllItemList(customerId);
			Element outputEl = myItemListOutputDoc.getDocumentElement();
			ArrayList<Element> listofWishLists = getXMLUtils().getElements(outputEl, "XPEDXMyItemsList");
			if (listofWishLists != null) {
				for (Element list : listofWishLists) {
					itemListMap.put(list.getAttribute("MyItemsListKey"), list.getAttribute("Name"));
				}
			}
		} catch (Exception e) {
			log.error("Unable to get MyItemList list from DB. The list will be empty. " + e);
		}
	}

	public String setCategoryDomainAssetList() {
		String categoryDomainDisplay = "CatalogLanding";
		categoryAssetMap = new HashMap<String, ArrayList<XPEDXCatalogCategoryImageBean>>();
		Document catDoc = getOutDoc();
		String imageURL = "";
		String childCategoryName = "";
		String childCategoryPath = "";
		Element catListElement = SCXmlUtil.getChildElement(catDoc.getDocumentElement(), "CategoryList");
		ArrayList<Element> categoryList = SCXmlUtil.getChildren(catListElement, "Category");
		Iterator<Element> catIter = categoryList.iterator();
		while (catIter.hasNext()) {

			Element topLevelCategory = catIter.next();
			String topCategoryShortDesc = SCXmlUtil.getAttribute(topLevelCategory, "ShortDescription");

			if (getCategoryDepth().equals("1")) {
				// for c1 categories-> show all the sub categories of C1 in the landing page
				String topCategoryPath = topLevelCategory.getAttribute("CategoryPath");
				Element assetElem = SCXmlUtils.getElementByAttribute(topLevelCategory, "/AssetList/Asset", "Type", CATEGORY_IMAGE_ASSET_TYPE);
				if (null != assetElem) {
					String contentLocation = assetElem.getAttribute("ContentLocation");
					String contentId = assetElem.getAttribute("ContentID");
					if (contentLocation != null && contentId != null && contentLocation != "" && contentId != "") {
						imageURL = contentLocation + "/" + contentId;
					} else {
						imageURL = XPEDXWCUtils.getStaticFileLocation() + "/xpedx/images/INF_150x150.jpg";
					}
				} else {
					// no category images defined
					log.error("no category image defined for " + topCategoryShortDesc);
					imageURL = XPEDXWCUtils.getStaticFileLocation() + "/xpedx/images/INF_150x150.jpg";
				}
				XPEDXCatalogCategoryImageBean bean = new XPEDXCatalogCategoryImageBean(topCategoryShortDesc, topCategoryPath, imageURL);
				ArrayList<XPEDXCatalogCategoryImageBean> topBeanList = new ArrayList<XPEDXCatalogCategoryImageBean>();
				topBeanList.add(bean);
				categoryAssetMap.put(topCategoryShortDesc, topBeanList);

			} else {
				// for Home Catalog Landing
				Element childCatListElement = SCXmlUtil.getChildElement(topLevelCategory, "ChildCategoryList");
				ArrayList<Element> childCategoryList = SCXmlUtil.getChildren(childCatListElement, "Category");
				ArrayList<XPEDXCatalogCategoryImageBean> childBeanList = new ArrayList<XPEDXCatalogCategoryImageBean>();
				Iterator<Element> childCatIter = childCategoryList.iterator();
				while (childCatIter.hasNext()) {
					Element childCategory = childCatIter.next();
					childCategoryName = childCategory.getAttribute("ShortDescription");
					childCategoryPath = childCategory.getAttribute("CategoryPath");
					Element assetElem = SCXmlUtils.getElementByAttribute(childCategory, "/AssetList/Asset", "Type", CATEGORY_IMAGE_ASSET_TYPE);
					if (null != assetElem) {
						String contentLocation = assetElem.getAttribute("ContentLocation");
						String contentId = assetElem.getAttribute("ContentID");
						if (contentLocation != null && contentId != null && contentLocation != "" && contentId != "") {
							imageURL = contentLocation + "/" + contentId;
						} else {
							imageURL = XPEDXWCUtils.getStaticFileLocation() + "/xpedx/images/INF_150x150.jpg";
						}
					} else {
						// no category images defined
						log.error("no category image defined for " + childCategoryName);
						imageURL = XPEDXWCUtils.getStaticFileLocation() + "/xpedx/images/INF_150x150.jpg";
					}
					XPEDXCatalogCategoryImageBean bean = new XPEDXCatalogCategoryImageBean(childCategoryName, childCategoryPath, imageURL);
					childBeanList.add(bean);
				}
				categoryAssetMap.put(topCategoryShortDesc, childBeanList);
			}
		}
		return categoryDomainDisplay;
	}

	@Override
	public String search() {
		init();
		setCustomerNumber();
		long startTime = System.currentTimeMillis();
		StringBuffer sb = new StringBuffer();

		if (("").equals(super.sortField) && (super.session.get("sortField") == null || ("").equals(super.session.get("sortField")))) {
			orderByAttribute = "Item.ExtnBestMatch";
			sortField = "Item.ExtnBestMatch--A";
		}

		String returnString = super.search();
		long endTime = System.currentTimeMillis();
		long timespent = (endTime - startTime);
		if (log.isInfoEnabled()) {
			sb.append("OOTB execution time on catlaog Search() = " + timespent);
		}
		// getting the customer bean object from session. XBT-260
		changeBasis();
		shipToCustomer = (XPEDXShipToCustomer) XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);

		/* Start of webtrend tags */
		setsearchMetaTag(true);
		/* End of webtrend tags */

		if (ERROR.equals(returnString)) {
			if ("true".equals(getTryAgain())) {
				return returnString;
			}
			if (searchTerm != null && searchTerm.indexOf("*") > -1) {
				String searchTermList[] = searchTerm.split(" ");
				String strSearchTerm = "";
				String removedSearchTerms = "";
				for (String searchTermToken : searchTermList) {
					if (searchTermToken.indexOf("*") > -1 && searchTermToken.length() > 5) {
						strSearchTerm = strSearchTerm + searchTermToken + " ";
						continue;
					} else if (searchTermToken.indexOf("*") == -1) {
						strSearchTerm = strSearchTerm + searchTermToken + " ";
						continue;
					}
					removedSearchTerms = removedSearchTerms + searchTermToken + " ";
				}

				setRemSearchTerms(removedSearchTerms);
				strSearchTerm = strSearchTerm.trim();
				if (!("").equals(strSearchTerm)) {
					searchTerm = strSearchTerm;
					return "retrySearch";
				}
			}
		} else if (isSingleItem()) {
			YFCNode yfcNode = YFCDocument.getDocumentFor(getOutDoc()).getDocumentElement().getChildElement("ItemList").getFirstChild();
			setItemID(yfcNode.getAttributes().get("ItemID"));
			setUnitOfMeasure(yfcNode.getAttributes().get("UnitOfMeasure"));
			return "singleItem";
		}

		try {
			long startTimeCustomerService = System.currentTimeMillis();
			getAllAPIOutput();
			endTime = System.currentTimeMillis();
			timespent = (endTime - startTimeCustomerService);
			if (log.isInfoEnabled()) {
				sb.append("\nCustom Service execution time on catlaog Search() = " + timespent);
			}
		} catch (Exception e) {
			log.error("", e);
		}

		setItemsUomsMap();
		setAttributeListForUI();
		prepareItemBranchInfoBean();
		setColumnListForUI();

		if ((path == null || path.equals("/")) && getFirstItem().trim() != "") {
			YFCNode yfcNode = YFCDocument.getDocumentFor(getOutDoc()).getDocumentElement().getChildElement("ItemList").getFirstChild();
			path = yfcNode.getAttributes().get("CategoryPath");
		}

		setStockedItemFromSession();
		if (isStockedItem) {
			setsearchMetaTag(true);
		}

		getCatTwoDescFromItemIdForpath(getOutDoc().getDocumentElement(), path);

		if (log.isInfoEnabled()) {
			timespent = (endTime - startTime);
			sb.append("\nTotal Action time  execution on catlaog Search() = " + timespent);
			log.info(sb.toString());
		}

		return SUCCESS;
	}

	@Override
	public String sortResultBy() {
		StringBuffer sb = new StringBuffer();
		long startTime = System.currentTimeMillis();
		init();

		if (("").equals(super.sortField) && (super.session.get("sortField") == null || ("").equals(super.session.get("sortField")))) {
			orderByAttribute = "Item.ExtnBestMatch";
			sortField = "Item.ExtnBestMatch--A";
		}

		String returnString = super.sortResultBy();
		long endTime = System.currentTimeMillis();
		long timespent = (endTime - startTime);

		if (log.isInfoEnabled()) {
			sb.append("OOTB execution time on catlaog sortResultBy() = " + timespent);
		}

		// XBT-260
		changeBasis();
		if (ERROR.equals(returnString)) {
			return returnString;
		} else {

			// Added for performance of sortResultByAction
			Document catDoc = getOutDoc();
			if (catDoc != null) {
				NodeList itemList = catDoc.getElementsByTagName("ItemList");
				if (itemList != null) {
					for (int i = 0; i < itemList.getLength(); i++) {
						Element _categoryElem = (Element) itemList.item(i);
						path = SCXmlUtil.getXpathAttribute(_categoryElem, "//ItemList/Item/CategoryList/Category/@CategoryPath");
					}
				}
			}
			// end of performance sortResultByAction
			try {
				long startTimeCustomerService = System.currentTimeMillis();
				getAllAPIOutput();
				endTime = System.currentTimeMillis();
				timespent = (endTime - startTimeCustomerService);
				if (log.isInfoEnabled()) {
					sb.append("\nCustom Service execution time on catlaog sortResultBy() = " + timespent);
				}
			} catch (Exception e) {
				log.error("", e);
			}

			setItemsUomsMap();
			setAttributeListForUI();
			prepareItemBranchInfoBean();
			setColumnListForUI();
			getSortFieldDocument();
		}

		if (log.isInfoEnabled()) {
			timespent = (endTime - startTime);
			sb.append("\nTotal Action time  execution on catlaog sortResultBy() = " + timespent);
			log.info(sb.toString());
		}
		return SUCCESS;
	}

	@Override
	public String goToPage() {
		StringBuffer sb = new StringBuffer(1024);
		long startTime = System.currentTimeMillis();
		init();

		if (("").equals(super.sortField) && (super.session.get("sortField") == null || ("").equals(super.session.get("sortField")))) {
			orderByAttribute = "Item.ExtnBestMatch";
			sortField = "Item.ExtnBestMatch--A";
		}

		String returnString = super.goToPage();
		long endTime = System.currentTimeMillis();
		long timespent = (endTime - startTime);

		if (log.isInfoEnabled()) {
			sb.append("OOTB execution time on catlaog goToPage() = " + timespent);
		}
		if (ERROR.equals(returnString)) {
			return returnString;
		} else {
			// Added for performance of filterAction
			Document catDoc = getOutDoc();
			if (catDoc != null) {
				NodeList itemList = catDoc.getElementsByTagName("ItemList");
				if (itemList != null) {
					for (int i = 0; i < itemList.getLength(); i++) {
						Element _categoryElem = (Element) itemList.item(i);
						path = SCXmlUtil.getXpathAttribute(_categoryElem, "//ItemList/Item/CategoryList/Category/@CategoryPath");
					}
				}
			}

			// end of performance filterAction XBT-260
			changeBasis();
			try {
				long startTimeCustomerService = System.currentTimeMillis();
				getAllAPIOutput();
				endTime = System.currentTimeMillis();
				timespent = (endTime - startTimeCustomerService);
				if (log.isInfoEnabled()) {
					sb.append("\nCustom Service execution time on catlaog goToPage() = " + timespent);
				}

			} catch (Exception e) {
				log.error("", e);
			}

			setItemsUomsMap();
			setAttributeListForUI();
			prepareItemBranchInfoBean();
			setColumnListForUI();
		}

		if (log.isInfoEnabled()) {
			timespent = (endTime - startTime);
			sb.append("\nTotal Action time  execution on catlaog goToPage() = " + timespent);
			log.info(sb.toString());
		}

		return SUCCESS;
	}

	@Override
	public String selectPageSize() {
		StringBuffer sb = new StringBuffer(1024);
		long startTime = System.currentTimeMillis();
		init();

		if (("").equals(super.sortField) && (super.session.get("sortField") == null || ("").equals(super.session.get("sortField")))) {
			orderByAttribute = "Item.ExtnBestMatch";
			sortField = "Item.ExtnBestMatch--A";
		}

		String returnString = super.selectPageSize();
		long endTime = System.currentTimeMillis();
		long timespent = (endTime - startTime);
		if (log.isInfoEnabled()) {
			sb.append("OOTB execution time on catlaog selectPageSize() = " + timespent);
		}
		changeBasis();
		wcContext.getSCUIContext().getSession().setAttribute("selectedPageSize", pageSize);
		if (ERROR.equals(returnString)) {
			return returnString;
		} else {
			// Added for performance of filterAction
			Document catDoc = getOutDoc();
			if (catDoc != null) {
				NodeList itemList = catDoc.getElementsByTagName("ItemList");
				if (itemList != null) {
					for (int i = 0; i < itemList.getLength(); i++) {
						Element _categoryElem = (Element) itemList.item(i);
						path = SCXmlUtil.getXpathAttribute(_categoryElem, "//ItemList/Item/CategoryList/Category/@CategoryPath");
					}
				}
			}
			try {
				long startTimeCustomerService = System.currentTimeMillis();
				getAllAPIOutput();
				endTime = System.currentTimeMillis();
				timespent = (endTime - startTimeCustomerService);
				if (log.isInfoEnabled()) {
					sb.append("\nCustom Service execution time on catlaog selectPageSize() = " + timespent);
				}
			} catch (Exception e) {
				log.error("", e);
			}
			// end of performance sortResultByAction
			setItemsUomsMap();
			setAttributeListForUI();
			prepareItemBranchInfoBean();
			setColumnListForUI();
		}

		if (log.isInfoEnabled()) {
			timespent = (endTime - startTime);
			sb.append("\nTotal Action time  execution on catlaog selectPageSize() = " + timespent);
			log.info(sb.toString());
		}

		return SUCCESS;
	}

	public ArrayList<String> getListOfColumns(String layOutType) throws CannotBuildInputException {
		ArrayList<String> columnList = new ArrayList<String>();
		Document outputDoc = null;

		if (YFCCommon.isVoid(layOutType)) {
			LOG.error("getListOfColumns: layOutType is a required field. Returning a empty list...");
			return columnList;
		}

		templateName = "tmp_" + layOutType.replaceAll("[^a-zA-Z]", "");

		// Checking if the layout is set into cache. If its set then getting the
		// details from the cache and returning the ColumnList
		String ccLongDescriptionFromCache = (String) XPEDXWCUtils.getObjectFromCache(layOutType);
		if (!(YFCCommon.isStringVoid(ccLongDescriptionFromCache))) {
			String[] columnNamesArrayFromCache = ccLongDescriptionFromCache.split(",");
			columnList = new ArrayList<String>(Arrays.asList(columnNamesArrayFromCache));
			return columnList;
		}
		// If it is not in cache then call the common code mashup,fetch the
		// column list for the layout type, put it in session and return it.

		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/CommonCode/@CodeValue", layOutType);

		Element input = WCMashupHelper.getMashupInput("xpedxGetCustomCommonCodesForCatBrowse", valueMap, wcContext.getSCUIContext());
		String inputXml = SCXmlUtil.getString(input);
		LOG.debug("getListOfColumns: Input XML: " + inputXml);
		Object obj = WCMashupHelper.invokeMashup("xpedxGetCustomCommonCodesForCatBrowse", input, wcContext.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		if (null != outputDoc) {
			LOG.debug("getListOfColumns: Output XML: " + SCXmlUtil.getString((Element) obj));
		}
		// get the common code node which has the CodeValue=layOutType. It
		// should return only one Element
		ArrayList<Element> commonCodeList = SCXmlUtils.getElementsByAttribute(outputDoc.getDocumentElement(), "CommonCode", "CodeValue", layOutType);
		if (null != commonCodeList && commonCodeList.size() == 1) {
			Element commonCodeElement = commonCodeList.get(0);
			// Column names are stored in the long description of a CommonCode
			String ccLongDescription = commonCodeElement.getAttribute("CodeLongDescription");
			if (YFCCommon.isVoid(ccLongDescription)) {
				LOG.error("getListOfColumns: Not data in the CommonCodes for the given layoutType.Returning a empty columnList...LayoutType: " + layOutType);
				return columnList;
			}
			// setting the layout into the context for future use.
			XPEDXWCUtils.setObectInCache(layOutType, ccLongDescription);
			// Column names are stored seperated by Commas(,)
			String[] columnNamesArray = ccLongDescription.split(",");
			columnList = new ArrayList<String>(Arrays.asList(columnNamesArray));
		} else {
			// Either zero rows or more than one rows. Return a empty columnList
			LOG.error("getListOfColumns: Not able to determine the Layout code. Returning a empty columnList");
			return columnList;
		}
		return columnList;
	}

	/**
	 * @return the columList
	 */
	public ArrayList<String> getColumnList() {
		return columnList;
	}

	public String praparePnAjaxData() {
		init();
		String pnaItemId = getPnaItemId();
		Document itemDoc;
		String requestedUOM = "";
		setIsBracketPricing("false");
		try {
			itemDoc = XPEDXOrderUtils.getItemDetails(pnaItemId, wcContext.getCustomerId(), wcContext.getStorefrontId(), wcContext);
			log.debug("Item Details complete");
			// prepare the MyItemList list
			prepareMyItemListList();
			log.debug("MyItemList list prepared");
			Element itemEle = SCXmlUtil.getChildElement(itemDoc.getDocumentElement(), "Item");
			requestedUOM = SCXmlUtil.getAttribute(itemEle, "UnitOfMeasure");
			log.debug("Requested UOM: " + requestedUOM);
			ArrayList<XPEDXItem> inputItems = getPnAInputDoc(getPnaItemId(), requestedUOM);
			XPEDXPriceAndAvailability pna = XPEDXPriceandAvailabilityUtil.getPriceAndAvailability(inputItems);

			// This takes care of displaying message to Users based on
			// ServiceDown, Transmission Error, HeaderLevelError, LineItemError
			ajaxDisplayStatusCodeMsg = XPEDXPriceandAvailabilityUtil.getAjaxDisplayStatusCodeMsg(pna);
			setAjaxLineStatusCodeMsg(ajaxDisplayStatusCodeMsg);

			// Check for Success/Failure and Error Conditions
			if (null == pna || pna.getTransactionStatus().equalsIgnoreCase("F")) {
				/*
				 * If the PnA is failure, set the error msg and send success back to the UI.No matter what the reply is, we are going to display the modal with the error
				 * messages*incase of PnA failure, the pricing and Availability information will not be available to the user,*but they can still add the item to the cart.
				 */
				log.error(ajaxDisplayStatusCodeMsg + "PnA failed(TransactionStatus Error) for ItemID: " + pnaItemId);
				// setAjaxLineStatusCodeMsg(getAjaxLineStatusCodeMsg()
				// + "Error getting pricing detail: Transaction Failed\n");
			} else if (!pna.getHeaderStatusCode().equalsIgnoreCase("00")) {
				log.error(ajaxDisplayStatusCodeMsg + "-P7-PnA failed(HeaderStatusCode Error) for ItemID: " + pnaItemId);
				// setAjaxLineStatusCodeMsg(getAjaxLineStatusCodeMsg()
				// + "Error getting pricing detail: HeaderStatusCode Error.\n");
			}

			if (null == pna || null == pna.getItems()) {
				preparePnAPricingInfo(itemDoc, new Vector<XPEDXItem>(), pnaItemId, requestedUOM);
			} else {
				Vector<XPEDXItem> items = pna.getItems();
				// prepare the information for JSP
				setAjaxPnAJson(items, pnaItemId);
				preparePnAPricingInfo(itemDoc, items, pnaItemId, requestedUOM);

				for (XPEDXItem pandAItem : items) {
					if (pandAItem.getLegacyProductCode().equals(pnaItemId)) {
						// set the line status erros mesages if any
						String lineStatusErrorMsg = XPEDXPriceandAvailabilityUtil.getPnALineErrorMessage(pandAItem);

						ajaxDisplayStatusCodeMsg = ajaxDisplayStatusCodeMsg + "  " + lineStatusErrorMsg;

						if (!YFCCommon.isVoid(lineStatusErrorMsg)) {
							setAjaxLineStatusCodeMsg(ajaxDisplayStatusCodeMsg);
						}
					}
				}
			}

		} catch (Exception e) {
			log.error("Error Getting Item Details and PnA Details for" + pnaItemId, e);
			return ERROR;
		}
		return SUCCESS;
	}

	private void preparePnAPricingInfo(Document itemDoc, Vector<XPEDXItem> items, String itemId, String requestedUOM) throws Exception {
		Element itemEle = SCXmlUtil.getChildElement(itemDoc.getDocumentElement(), "Item");
		Element primaryInfoEle = SCXmlUtil.getChildElement(itemEle, "PrimaryInformation");
		ItemDescription = SCXmlUtils.getAttribute(primaryInfoEle, "Description").toString();
		ItemExtendedDescription = SCXmlUtils.getAttribute(itemEle, "ItemID").toString();
		ItemShortDescription = SCXmlUtils.getAttribute(primaryInfoEle, "ShortDescription").toString();
		SCXmlUtil.getString(itemEle);
		Element itemExtnEle = SCXmlUtil.getChildElement(itemEle, "Extn");
		String minOrderQty = SCXmlUtils.getAttribute(primaryInfoEle, "MinOrderQuantity");
		String pricingUOM = SCXmlUtils.getAttribute(primaryInfoEle, "PricingUOM");
		String pricingUOMConvFactor = SCXmlUtils.getAttribute(primaryInfoEle, "PricingQuantityConvFactor");
		String baseUOM = SCXmlUtils.getAttribute(itemEle, "UnitOfMeasure");
		String prodMweight = SCXmlUtils.getAttribute(itemExtnEle, "ExtnMwt");
		List<String> displayUOMs = new ArrayList();
		getItemUOMs(itemId);
		for (XPEDXItem pandAItem : items) {
			if (pandAItem.getLegacyProductCode().equals(itemId)) {
				if (pandAItem.getPriceCurrencyCode() != null && pandAItem.getPriceCurrencyCode().trim().length() > 0) {
					setPriceCurrencyCode(pandAItem.getPriceCurrencyCode());
				}
				String pricingUOMUnitPrice = pandAItem.getUnitPricePerPricingUOM();
				BigDecimal pricingUOMPrice = new BigDecimal(pricingUOMUnitPrice);
				BigDecimal prodWeight = null;
				BigDecimal priceForCWTUom = null;
				BigDecimal priceForTHUom = null;
				// displayUOMs.add(baseUOM); //removed as specified in the bug
				// 185 comments on 03/Jan/11 3:58 PM by Barb Widmer
				displayUOMs.add(pricingUOM);
				if ("TH".equalsIgnoreCase(pricingUOM)) {
					// hardcode for now.
					displayUOMs.add("CWT");
					if (prodMweight != null && prodMweight.trim().length() > 0)
						prodWeight = new BigDecimal(prodMweight);
					else
						prodWeight = new BigDecimal(100); // this will make
					// pricing for TH
					// and CWT same.
					priceForCWTUom = pricingUOMPrice.divide(prodWeight.divide(new BigDecimal(100)));
				}
				if ("CWT".equalsIgnoreCase(pricingUOM)) {
					displayUOMs.add("TH");
					if (prodMweight != null && prodMweight.trim().length() > 0)
						prodWeight = new BigDecimal(prodMweight);
					else
						prodWeight = new BigDecimal(100); // this will make
					// pricing for CW
					priceForTHUom = pricingUOMPrice.multiply(prodWeight.divide(new BigDecimal(100)));
				}
				displayUOMs.add(requestedUOM);
				if (YFCCommon.isVoid(pricingUOMConvFactor)) {
					pricingUOMConvFactor = "1";
				}

				if (pricingUOMConvFactor != null && ((new BigDecimal(0)).compareTo(new BigDecimal(pricingUOMConvFactor)) <= 0)) {
					pricingUOMConvFactor = "1";
				}
				BigDecimal basePrice = pricingUOMPrice.divide(new BigDecimal(pricingUOMConvFactor));

				String BaseUomDesc = null;
				String RequestedQtyUOMDesc = null;
				String PricingUOMDesc = null;
				try {
					BaseUomDesc = XPEDXWCUtils.getUOMDescription(baseUOM);
					RequestedQtyUOMDesc = XPEDXWCUtils.getUOMDescription(pandAItem.getRequestedQtyUOM());
					PricingUOMDesc = XPEDXWCUtils.getUOMDescription(pandAItem.getPricingUOM());
				} catch (Exception e) {

				}

				displayPriceForUoms.add(pricingUOMUnitPrice + "/" + PricingUOMDesc);
				if (priceForCWTUom != null) {
					displayPriceForUoms.add(priceForCWTUom.toString() + "/" + "CWT");
				}
				if (priceForTHUom != null) {
					displayPriceForUoms.add(priceForTHUom.toString() + "/" + "TH");
				}
				displayPriceForUoms.add(pandAItem.getUnitPricePerRequestedUOM() + "/" + RequestedQtyUOMDesc);
				displayPriceForUoms.add("Extended Price" + "-" + pandAItem.getExtendedPrice());
				// Vector bracketsPricingList = null;
				bracketsPricingList = pandAItem.getBrackets();
				setIsBracketPricing(XPEDXPriceandAvailabilityUtil.isBracketPricingAvailable(bracketsPricingList));
				DataList = displayUOMs;
			}
		}
	}

	protected void getItemUOMs(String itemId) throws Exception {
		String customerId = wcContext.getCustomerId();
		String organizationCode = wcContext.getStorefrontId();

		Map<String, String> uoms = null;

		List items = new ArrayList();
		items.add(itemId);

		itemUOMsMap = getXpedxUOMList();
		displayItemUOMsMap = new HashMap();
		for (Iterator it = itemUOMsMap.keySet().iterator(); it.hasNext();) {
			String uomDesc = (String) it.next();
			Object o = itemUOMsMap.get(uomDesc);
			if ("1".equals(o)) {
				displayItemUOMsMap.put(XPEDXWCUtils.getUOMDescription(uomDesc), uomDesc);
			} else {
				displayItemUOMsMap.put(XPEDXWCUtils.getUOMDescription(uomDesc) + " (" + o + ")", uomDesc);
			}
		}
	}

	public Map getDisplayItemUOMsMap() {
		return displayItemUOMsMap;
	}

	public void setDisplayItemUOMsMap(Map displayItemUOMsMap) {
		this.displayItemUOMsMap = displayItemUOMsMap;
	}

	private void setAjaxPnAJson(Vector<XPEDXItem> items, String itemId) {
		pnaJson = new JSONObject();
		for (XPEDXItem item : items) {
			if (item.getLegacyProductCode().equals(itemId)) {
				Vector wareHouseList = item.getWarehouseLocationList();
				XPEDXWarehouseLocation wareHouseItem = null;
				Float totalForImmediate = new Float("0.0");
				Float totalForNextDay = new Float("0.0");
				Float totalForTwoPlus = new Float("0.0");
				Float toalAvailable = new Float("0.0");
				for (Object wareHouse : wareHouseList) {
					wareHouseItem = (XPEDXWarehouseLocation) wareHouse;
					String availQtyStr = wareHouseItem.getAvailableQty();
					if (YFCCommon.isVoid(availQtyStr)) {
						log.error("Empty available quantity for " + item.getLegacyProductCode() + " in warehouse " + wareHouseItem.getWarehouse());
						continue;
					}
					String noOfDaysString = wareHouseItem.getNumberOfDays();
					if (YFCCommon.isVoid(noOfDaysString) || !isANumber(noOfDaysString)) {
						log.error("Empty or Corrupt NumberOfDays for " + item.getLegacyProductCode() + " in warehouse " + wareHouseItem.getWarehouse());
						continue;
					}
					Float availQtyFloat = Float.valueOf(availQtyStr.trim()).floatValue();
					int noOfDays = Integer.parseInt(wareHouseItem.getNumberOfDays());
					if (noOfDays == 0) {
						// Immediate
						totalForImmediate += availQtyFloat;
					} else if (noOfDays == 1) {
						// Next day
						totalForNextDay += availQtyFloat;
					} else {
						// 2+ days
						totalForTwoPlus += availQtyFloat;
					}
				}
				// Prepare the JSON Object
				toalAvailable = totalForImmediate + totalForNextDay + totalForTwoPlus;

				JSONObject jsonAvail = new JSONObject();
				JSONObject jsonImmediate = new JSONObject();
				JSONObject jsonNext = new JSONObject();
				JSONObject jsonTwoPlus = new JSONObject();
				JSONObject jsonTotal = new JSONObject();
				String ItemUOMDesc = null;
				try {
					ItemUOMDesc = XPEDXWCUtils.getUOMDescription(item.getRequestedQtyUOM());
				} catch (Exception e) {
					log.error("Exception while getting the UOM description for item " + item.getLegacyProductCode() + " and UOM " + item.getRequestedQtyUOM(), e);
				}
				jsonAvail.put("availability", "Availability");
				jsonAvail.put("availValue", ItemUOMDesc);
				jsonImmediate.put("availability", "Immediate");
				jsonImmediate.put("availValue", totalForImmediate.toString());
				jsonNext.put("availability", "Next Day");
				jsonNext.put("availValue", totalForNextDay.toString());
				jsonTwoPlus.put("availability", "2+ days");
				jsonTwoPlus.put("availValue", totalForTwoPlus.toString());
				jsonTotal.put("availability", "Total Qty Available");
				jsonTotal.put("availValue", toalAvailable.toString());
				JSONObject[] outputList = { jsonAvail, jsonImmediate, jsonNext, jsonTwoPlus, jsonTotal };
				pnaJson.put("availList", outputList);
			}
		}
	}

	protected boolean isANumber(String value) {
		try {
			Integer.parseInt(value);
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	private ArrayList<XPEDXItem> getPnAInputDoc(String pnaItemId, String requestedUOM) {
		ArrayList<XPEDXItem> pnaList = new ArrayList<XPEDXItem>();
		XPEDXItem item = new XPEDXItem();
		item.setLegacyProductCode(pnaItemId);
		item.setLineNumber("1");
		if (Qty == null) {
			item.setRequestedQty("1");
		} else {
			item.setRequestedQty(Qty);
		}
		if (UOM != null) {
			requestedUOM = UOM;
		}
		item.setRequestedQtyUOM(requestedUOM);
		pnaList.add(item);
		return pnaList;
	}

	/**
	 * Author: Rajendra Ugrani prepareItemBranchInfoBean() takes the output doc
	 * from the getOutDoc() method which is prepared by the process() method. It
	 * gets the customerID from the context, and retrieves the Item branch
	 * information about each product, for customers ship from division. Once it
	 * has the information, it prepares a HashMap with ItemID as the Key, and
	 * XPEDXItemBranchInfoBean as the value which will hold the Item branch info
	 * about a Item for the Customer Ship From Division(Primary Warehouse)
	 */
	protected void prepareItemBranchInfoBean() {
		// initialize the itemToItemBranchBeanMap
		itemToItemBranchBeanMap = new HashMap<String, XPEDXItemBranchInfoBean>();
		itemToCustPartNoMap = new HashMap<String, String>();
		// get the customer id from the context
		String customerID = wcContext.getCustomerId();
		String storeFrontID = wcContext.getCustomerId();
		String customerShipFromBranch = null;
		if (YFCCommon.isVoid(customerID)) {
			log.error("Unable to retrieve customerID from the context.Cannot prepare Item Branch Info. Returning back to the caller.");
			return;
		}
		// get the customers ship from division from the table
		try {
			// customerShipFromBranch =
			// (String)wcContext.getWCAttribute(XPEDXConstants.SHIP_FROM_BRANCH);
			// Added for performance - SortResultBy action
			shipToCustomer = (XPEDXShipToCustomer) XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
			if (shipToCustomer == null) {
				log.error("shipToCustomer object from session is null... Creating the Object and Putting it in the session");

				XPEDXWCUtils.setCustomerObjectInCache(XPEDXWCUtils.getCustomerDetails(getWCContext().getCustomerId(), getWCContext().getStorefrontId()).getDocumentElement());
				setShipToCustomer((XPEDXShipToCustomer) XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER));
			}
			// customerShipFromBranch = shipToCustomer.getExtnShipFromBranch();
			customerShipFromBranch = shipToCustomer.getExtnCustomerDivision();
			if (YFCCommon.isVoid(customerShipFromBranch)) {
				// oops... DB is messed up.
				log.error("customer ship from branch from the DB for Customer " + customerID + " is NULL." + " Cannot evaluate Item Branch Info. Returning back to the caller");
				return;
			}
		} catch (CannotBuildInputException e) {
			log.error("Unable to get customer ship from branch from the DB for Customer: " + customerID, e);
		}
		// get the prepared Catalog Document
		Document catDoc = getOutDoc();

		// get the CatalogSearch/ItemList
		Element itemListElement = SCXmlUtil.getChildElement(catDoc.getDocumentElement(), "ItemList");
		NodeList itemNodeList = itemListElement.getElementsByTagName("Item");

		// JIRA-901: SKU Changes Begin
		String customerPartNumber = "";
		String manufacturerPartNo = "";
		String extnMpc = "";
		// Commented for EB 47 String customerUseSKU = (String)
		// wcContext.getWCAttribute("customerUseSKU");
		// Added for EB 47
		extnMfgItemFlag = (String) wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.BILL_TO_CUST_MFG_ITEM_FLAG);
		extnCustomerItemFlag = (String) wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.BILL_TO_CUST_PART_ITEM_FLAG);
		// End of EB 47

		String legacyCustNumber = (String) wcContext.getWCAttribute(XPEDXConstants.LEGACY_CUST_NUMBER, WCAttributeScope.LOCAL_SESSION);
		// JIRA-901: SKU Changes End
		String environmentCode = (String) wcContext.getWCAttribute(XPEDXConstants.ENVIRONMENT_CODE, WCAttributeScope.LOCAL_SESSION);

		ArrayList<String> itemIds = new ArrayList<String>();

		for (int i = 0; i < itemNodeList.getLength(); i++) {
			itemIds.add(SCXmlUtil.getAttribute((Element) itemNodeList.item(i), "ItemID"));
		}

		if (!SCUtil.isVoid(extnCustomerItemFlag) && extnCustomerItemFlag.equalsIgnoreCase("Y")) {// Added for Eb
																									// 47
			if (itemIds.size() > 0) {
				// Document custPartNoDoc =
				// XPEDXWCUtils.getXpxItemCustXRefDoc(itemIds, wcContext);
				// if(custPartNoDoc!=null){
				Element itemcustXrefListElemet = (Element) allAPIOutputDoc.getElementsByTagName("XPXItemcustXrefList").item(0);
				if (itemcustXrefListElemet != null) {
					NodeList itemCustXrefList = itemcustXrefListElemet.getElementsByTagName("XPXItemcustXref");
					if (itemCustXrefList != null) {
						for (int i = 0; i < itemCustXrefList.getLength(); i++) {
							Element itemcustXrefElement = (Element) itemCustXrefList.item(i);
							String itemId = SCXmlUtil.getAttribute(itemcustXrefElement, "LegacyItemNumber");
							String customerItemNumber = SCXmlUtil.getAttribute(itemcustXrefElement, "CustomerItemNumber");
							itemToCustPartNoMap.put(itemId, customerItemNumber);
						}
					}
				}
			}
		}

		Element xPXItemExtnListElement = null;
		try {
			xPXItemExtnListElement = (Element) allAPIOutputDoc.getElementsByTagName("XPXItemExtnList").item(0);// XPEDXWCUtils.getXPXItemExtnList(itemIds,
																												// wcContext);
		} catch (Exception e) {
			log.error("", e);
		}

		try {
			getYPMPricelistLineList(itemIds, wcContext);
		} catch (Exception e) {
			log.error("", e);
		}

		for (int i = 0; i < itemNodeList.getLength(); i++) {
			// get the item node
			Node itemNode = itemNodeList.item(i);
			Element itemElement = (Element) itemNode;
			String itemID = SCXmlUtil.getAttribute(itemElement, "ItemID");
			// get the item extn element
			Element itemExtnElement = SCXmlUtil.getChildElement(itemElement, "Extn");

			if (itemExtnElement != null) {
				extnMpc = itemExtnElement.getAttribute("ExtnMpc");
				Element primaryInformation = SCXmlUtil.getChildElement(itemElement, "PrimaryInformation");
				manufacturerPartNo = primaryInformation.getAttribute("ManufacturerItem");
				customerPartNumber = (String) itemToCustPartNoMap.get(itemID);
				HashMap<String, String> skuMap = new HashMap<String, String>();
				skuMap.put("MPN", manufacturerPartNo);
				skuMap.put("MPC", extnMpc);
				skuMap.put("CPN", customerPartNumber);

				itemMap.put(itemID, skuMap);

				List<Element> itemBranchElementList = null;
				if (xPXItemExtnListElement != null) {
					try {
						itemBranchElementList = XMLUtilities.getElements(xPXItemExtnListElement, "XPXItemExtn[@ItemID='" + itemID + "']");
					} catch (XPathExpressionException e) {
						log.error("", e);
					}
					if (itemBranchElementList != null && itemBranchElementList.size() > 0) {
						Element itemBranchElement = itemBranchElementList.get(0);

						// get the field information
						String itemNumber = SCXmlUtil.getAttribute(itemBranchElement, "ItemID");
						String environmentID = SCXmlUtil.getAttribute(itemBranchElement, "EnvironmentID");
						String companyCode = SCXmlUtil.getAttribute(itemBranchElement, "CompanyCode");
						String legacyPartNo = SCXmlUtil.getAttribute(itemBranchElement, "MasterProductCode");
						String division = SCXmlUtil.getAttribute(itemBranchElement, "XPXDivision");
						String itemStockStatus = SCXmlUtil.getAttribute(itemBranchElement, "ItemStockStatus");
						String orderMultiple = SCXmlUtil.getAttribute(itemBranchElement, "OrderMultiple");
						String inventoryIndicator = SCXmlUtil.getAttribute(itemBranchElement, "InventoryIndicator");
						// prepare the PoJo
						XPEDXItemBranchInfoBean itemBranchInfoBean = new XPEDXItemBranchInfoBean(itemNumber, environmentID, companyCode, legacyPartNo, division, itemStockStatus,
								orderMultiple, inventoryIndicator);

						// inject it in HashMap
						itemToItemBranchBeanMap.put(itemNumber, itemBranchInfoBean);
					}
				}

			}
		}
		wcContext.setWCAttribute("itemMap", itemMap, WCAttributeScope.REQUEST);
		wcContext.setWCAttribute("itemToItemBranchBeanMap", itemToItemBranchBeanMap, WCAttributeScope.SESSION);
	}

	/**
	 * Method to get the customer part number for an item
	 *
	 * @param itemID2
	 * @param environmentID
	 * @param customerShipFromBranch
	 * @param customerNumber
	 * @return
	 */
	private String getCustomerPartNumber(String itemID2, String environmentID, String customerShipFromBranch, String customerNumber) {

		Element outputEl = null;
		Element custXrefEle = null;

		IWCContext wcContext = getWCContext();
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/XPXItemcustXref/@EnvironmentCode", environmentID);
		valueMap.put("/XPXItemcustXref/@CustomerBranch", customerShipFromBranch);
		valueMap.put("/XPXItemcustXref/@LegacyItemNumber", itemID2);
		valueMap.put("/XPXItemcustXref/@CustomerNumber", customerNumber);
		Element input;
		try {
			input = WCMashupHelper.getMashupInput("xpedxItemCustXRef", valueMap, wcContext.getSCUIContext());
			outputEl = (Element) WCMashupHelper.invokeMashup("xpedxItemCustXRef", input, wcContext.getSCUIContext());
			custXrefEle = XMLUtilities.getElement(outputEl, "XPXItemcustXref");
		} catch (Exception e) {
			LOG.error("Error while retrieving the customer part number:getCustomerPartNumber " + e.getMessage(), e);
		}
		SCXmlUtil.getString(outputEl);
		return SCXmlUtil.getAttribute(custXrefEle, "CustomerItemNumber");
	}

	public Document getYPMPricelistLineList(ArrayList<String> itemIdList, IWCContext wcContext) throws Exception {
		Document outputDoc = null;
		Document outputDocument = null;

		NodeList itemNodeList1 = allAPIOutputDoc.getElementsByTagName("PricelistLine");
		Map<String, List<Element>> PricelistLineMap = new HashMap<String, List<Element>>();
		List<Element> PricelistLineList = null;
		if (itemNodeList1 != null) {
			for (int i = 0; i < itemNodeList1.getLength(); i++) {
				Node itemNode = itemNodeList1.item(i);
				Element itemElement = (Element) itemNode;
				String itemId = itemElement.getAttribute("ItemID");
				String quantity = itemElement.getAttribute("FromQuantity");
				if (quantity == "" || quantity == null || quantity.equals("") || quantity.equals("null")) {
					itemElement.setAttribute("FromQuantity", "1");
				}
				if (PricelistLineMap.containsKey(itemId)) {
					PricelistLineList = PricelistLineMap.get(itemId);
					PricelistLineList.add(itemElement);
					PricelistLineMap.put(itemId, PricelistLineList);
				} else {
					PricelistLineList = new ArrayList<Element>();
					PricelistLineList.add(itemElement);
					PricelistLineMap.put(itemId, PricelistLineList);
				}
			}
		}
		setPLLineMap(sortPriceListLine(PricelistLineMap));
		/*
		 * if (null != outputDoc) { log.debug("Output XML for xpedxYpmPricelistLine: " + SCXmlUtil.getString((Element) obj)); }
		 */
		// }
		return outputDoc;
	}

	private Map<String, List<Element>> sortPriceListLine(Map<String, List<Element>> PricelistLineMap) {
		Set<String> itemSet = PricelistLineMap.keySet();
		Map<String, List<Element>> _pricelistLineMap = new HashMap<String, List<Element>>();
		for (String itemID : itemSet) {
			List priceList = PricelistLineMap.get(itemID);
			Collections.sort(priceList, new XPEDXSortingListPrice());
			_pricelistLineMap.put(itemID, priceList);
		}
		return _pricelistLineMap;
	}

	public String setNormallyStockedCheckbox() {
		init();
		getWCContext().setWCAttribute("StockedCheckbox", isStockedCheckeboxSelected(), WCAttributeScope.SESSION);
		return SUCCESS;
	}

	private double getConversion(Object convFactor, String orderMultiple) {
		if (convFactor != null && orderMultiple != null && orderMultiple.length() > 0) {
			String parseConvFactor = convFactor.toString();
			double convFactorD = Double.valueOf(parseConvFactor).doubleValue();
			double orderMultipleD = Double.parseDouble(orderMultiple);
			double factor = (convFactorD / orderMultipleD);
			return factor;
		}
		return -1;
	}

	private String processSpecialCharacters(String searchText) {
		if (searchText == null) {
			return null;
		}
		String specialCharacterReg = "[\\[\\]^);{!(}:,~\\\\]";
		searchText = searchText.replaceAll(specialCharacterReg, " ");
		searchText = processPlusCharacter(searchText);
		searchText = processEiphenCharacter(searchText);
		return searchText;
	}

	/**
	 * Replace "+" with whitespace when it a) appears as a word b) appears as
	 * first character in a word c) appears as last character in a word
	 */
	private String processPlusCharacter(String searchText) {
		String whiteSpaceReg = "[ ]";
		String[] searchTexts = searchText.split(whiteSpaceReg);
		StringBuilder searchTerm = new StringBuilder();
		for (String textSegment : searchTexts) {
			textSegment = textSegment.trim();
			if (textSegment.equals("+")) {
				continue;
			}

			if (textSegment.startsWith("+")) {
				textSegment = " " + textSegment.substring(1, textSegment.length());
			}

			if (textSegment.endsWith("+")) {
				textSegment = textSegment.substring(0, textSegment.length() - 1) + " ";
			}

			searchTerm.append(textSegment);
			searchTerm.append(" ");
		}
		return searchTerm.toString();
	}

	/**
	 * Replace "-" with whitespace when it a) appears as a word b) appears as
	 * last character in a word
	 */
	private String processEiphenCharacter(String searchText) {
		String whiteSpaceReg = "[ ]";
		String[] searchTexts = searchText.split(whiteSpaceReg);
		StringBuilder searchTerm = new StringBuilder();
		for (String textSegment : searchTexts) {
			textSegment = textSegment.trim();
			if (textSegment.equals("-")) {
				continue;
			}

			if (textSegment.endsWith("-")) {
				textSegment = textSegment.substring(0, textSegment.length() - 1) + " ";
			}

			searchTerm.append(textSegment);
			searchTerm.append(" ");
		}
		return searchTerm.toString();
	}

	public String getFacetList() {
		String retVal = SUCCESS;
		if (searchIndexInputXML != null) {
			Element inputDocElemen = SCXmlUtil.createFromString(searchIndexInputXML).getDocumentElement();
			if (!YFCCommon.isVoid(facetListItemAttributeKey)) {
				Element allAssignedListElem = SCXmlUtil.createChild(inputDocElemen, "ShowAllAssignedValues");

				// Added this attribute after Hot fix HF80 for EB-2810
				allAssignedListElem.setAttribute("ConsiderOnlyAllAssignedValueAttributes", "Y");

				Element itemAttributeElem = SCXmlUtil.createChild(allAssignedListElem, "ItemAttribute");
				itemAttributeElem.setAttribute("ItemAttributeKey", facetListItemAttributeKey);
			}

			// EB-3738 Should not display system error message. To catch the exception 'TooManyClauses'.
			try {
				Object outputObj = WCMashupHelper.invokeMashup("xpedxNarrowByCatalogSearch", inputDocElemen, wcContext.getSCUIContext());
				Document outputDoc = ((Element) outputObj).getOwnerDocument();
				setOutDoc(outputDoc);
				setAttributeListForUIForNarrowBy();
			} catch (Exception e) {
				log.error("Exception in XPEDXCatalogAction - getFacetList method while retrieving the search results", e);
				return retVal;
			}
		}
		return retVal;
	}

	/**
	 * @return the pnaItemId
	 */
	public String getPnaItemId() {
		return pnaItemId;
	}

	/**
	 * @param pnaItemId
	 *            the pnaItemId to set
	 */
	public void setPnaItemId(String pnaItemId) {
		this.pnaItemId = pnaItemId;
	}

	/**
	 * @return the pnaJson
	 */
	public JSONObject getPnaJson() {
		return pnaJson;
	}

	public String getItemDescription() {
		return ItemDescription;
	}

	public void setItemDescription(String ItemDescription) {
		this.ItemDescription = ItemDescription;
	}

	public String getItemExtendedDescription() {
		return ItemExtendedDescription;
	}

	public void setItemExtendedDescription(String ItemExtendedDescription) {
		this.ItemExtendedDescription = ItemExtendedDescription;
	}

	public String getItemShortDescription() {
		return ItemShortDescription;
	}

	public void setItemShortDescription(String ItemShortDescription) {
		this.ItemShortDescription = ItemShortDescription;
	}

	public String getQty() {
		return Qty;
	}

	public void setQty(String Qty) {
		this.Qty = Qty;
	}

	public String getUOM() {
		return UOM;
	}

	public void setUOM(String uOM) {
		UOM = uOM;
	}

	public String getJob() {
		return Job;
	}

	public void setJob(String Job) {
		this.Job = Job;
	}

	public String getcustomer() {
		return customer;
	}

	public void setcustomer(String customer) {
		this.customer = customer;
	}

	public String getDetails() {
		return Details;
	}

	public void setDetails(String Details) {
		this.Details = Details;
	}

	public List<String> getDataList() {
		return DataList;
	}

	public void setDataList(List<String> DataList) {
		this.DataList = DataList;
	}

	public List<String> getdisplayPriceForUoms() {
		return displayPriceForUoms;
	}

	public void setdisplayPriceForUoms(List<String> displayPriceForUoms) {
		this.displayPriceForUoms = displayPriceForUoms;
	}

	public String getIsGuestUser() {
		return isGuestUser;
	}

	public void setIsGuestUser(String isGuestUser) {
		this.isGuestUser = isGuestUser;
	}

	public List getBracketsPricingList() {
		return bracketsPricingList;
	}

	public void setBracketsPricingList(List bracketsPricingList) {
		this.bracketsPricingList = bracketsPricingList;
	}

	public HashMap getAttributeMap() {
		return attributeMap;
	}

	public void setAttributeMap(HashMap attributeMap) {
		this.attributeMap = attributeMap;
	}

	public Map getItemListMap() {
		return itemListMap;
	}

	public void setItemListMap(Map itemListMap) {
		this.itemListMap = itemListMap;
	}

	/**
	 * @return the categoryAssetMap
	 */
	public HashMap<String, ArrayList<XPEDXCatalogCategoryImageBean>> getCategoryAssetMap() {
		return categoryAssetMap;
	}

	public String getDisplayAllCategories() {
		return displayAllCategories;
	}

	public void setDisplayAllCategories(String displayAllCategories) {
		this.displayAllCategories = displayAllCategories;
	}

	/**
	 * @return the ajaxLineStatusCodeMsg
	 */
	public String getAjaxLineStatusCodeMsg() {
		return ajaxLineStatusCodeMsg;
	}

	/**
	 * @param ajaxLineStatusCodeMsg
	 *            the ajaxLineStatusCodeMsg to set
	 */
	public void setAjaxLineStatusCodeMsg(String ajaxLineStatusCodeMsg) {
		this.ajaxLineStatusCodeMsg = ajaxLineStatusCodeMsg;
	}

	/**
	 * @return the itemToItemBranchBeanMap
	 */
	public HashMap<String, XPEDXItemBranchInfoBean> getItemToItemBranchBeanMap() {
		return itemToItemBranchBeanMap;
	}

	public String getPriceCurrencyCode() {
		return priceCurrencyCode;
	}

	public void setPriceCurrencyCode(String priceCurrencyCode) {
		this.priceCurrencyCode = priceCurrencyCode;
	}

	public String getIsBracketPricing() {
		return isBracketPricing;
	}

	public void setIsBracketPricing(String isBracketPricing) {
		this.isBracketPricing = isBracketPricing;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	@Override
	public void setSelectedView(String sSelectedView) {
		this.selectedView = sSelectedView;
	}

	@Override
	public String getSelectedView() {

		return selectedView;
	}

	protected static final String XPEDX_PRODUCT_LINE_INDEX_FIELD = "ItemAttribute.xpedx.FF_1101";
	protected static final String XPEDX_DEFAULT_TYPE_COLUMN = "XPXDefault";
	protected ArrayList<String> columnList;
	protected String pnaItemId = "";
	protected JSONObject pnaJson;

	String ItemDescription = null;
	String ItemExtendedDescription = null;
	String ItemShortDescription = null;
	String Qty = null;
	String UOM = null;

	String Job = "";
	String templateName = "";
	String customer = "";
	String Details = "Mill/Manufacture item-Additional charges may apply";
	List<String> DataList = new ArrayList();
	List bracketsPricingList = null;
	List<String> displayPriceForUoms = new ArrayList();
	protected String isGuestUser = "N";
	protected HashMap attributeMap;
	protected Map itemListMap;
	protected HashMap<String, ArrayList<XPEDXCatalogCategoryImageBean>> categoryAssetMap;
	protected static final String CATEGORY_IMAGE_ASSET_TYPE = "CATEGORY_IMAGE";
	private String displayAllCategories = "false";
	protected String ajaxLineStatusCodeMsg = "";
	protected String ajaxDisplayStatusCodeMsg = "";
	protected HashMap<String, XPEDXItemBranchInfoBean> itemToItemBranchBeanMap;
	protected HashMap<String, String> itemToCustPartNoMap;
	protected Map itemUOMsMap;
	protected Map displayItemUOMsMap;
	protected String isBracketPricing;
	protected String itemID;
	protected String unitOfMeasure;
	private HashMap<String, HashMap<String, String>> itemMap = new HashMap<String, HashMap<String, String>>();
	protected String editedOrderHeaderKey = "";
	protected String draft;
	protected String path;
	public Map<String, List<Element>> facetListMap = new HashMap<String, List<Element>>();
	private String firstItemCategoryShortDescription;
	private String tempCategoryPath;
	private Element allAPIOutputDoc;
	private String categoryDepthNarrowBy;
	private Map<String, String> sortListMap = new LinkedHashMap<String, String>();
	// Added for JIRA 1731 to show only 8 facet list woth ajax
	private String facetDivShortDescription;
	private String facetListItemAttributeKey;

	private String rememberNewSearchText;

	public Map<String, String> getSortListMap() {
		sortListMap.put("Item.ExtnBestMatch--A", "Best Match");
		sortListMap.put("relevancy", "Relevancy");
		sortListMap.put("Item.ItemID--A", "Item # (Low to High)");
		sortListMap.put("Item.ItemID--D", "Item # (High to Low)");
		sortListMap.put("Item.SortableShortDescription--A", "Description (A to Z)");
		sortListMap.put("Item.SortableShortDescription--D", "Description (Z to A)");

		ArrayList<String> allowedColumns = getColumnList();

		if (allowedColumns != null && allowedColumns.contains("Size")) {
			sortListMap.put("Item.ExtnSize--A", "Size (Low to High)");
			sortListMap.put("Item.ExtnSize--D", "Size (High to Low)");
		}
		if (allowedColumns != null && allowedColumns.contains("Color")) {
			sortListMap.put("Item.ExtnColor--A", "Color (A to Z)");
			sortListMap.put("Item.ExtnColor--D", "Color (Z to A)");
		}
		if (allowedColumns != null && allowedColumns.contains("Basis")) {
			sortListMap.put("Item.ExtnBasis--A", "Basis (Low to High)");
			sortListMap.put("Item.ExtnBasis--D", "Basis (High to Low)");
		}
		if (allowedColumns != null && allowedColumns.contains("Mwt")) {
			sortListMap.put("Item.ExtnMwt--A", "Mwt (Low to High)");
			sortListMap.put("Item.ExtnMwt--D", "Mwt (High to Low)");
		}
		if (allowedColumns != null && allowedColumns.contains("Thickness")) {
			sortListMap.put("Item.ExtnThickness--A", "Thickness (A to Z)");
			sortListMap.put("Item.ExtnThickness--D", "Thickness (Z to A)");
		}
		if (allowedColumns != null && allowedColumns.contains("Package")) {
			sortListMap.put("Item.ExtnPackMethod--A", "Pack (A to Z)");
			sortListMap.put("Item.ExtnPackMethod--D", "Pack (Z to A)");
		}

		return sortListMap;
	}

	public void setSortListMap(Map<String, String> sortListMap) {
		this.sortListMap = sortListMap;
	}

	public String getCategoryDepthNarrowBy() {
		return categoryDepthNarrowBy;
	}

	public void setCategoryDepthNarrowBy(String categoryDepthNarrowBy) {
		this.categoryDepthNarrowBy = categoryDepthNarrowBy;
	}

	public Map<String, List<Element>> getFacetListMap() {
		return facetListMap;
	}

	public void setFacetListMap(Map<String, List<Element>> facetListMap) {
		this.facetListMap = facetListMap;
	}

	/**
	 * @return the itemMap
	 */
	public HashMap<String, HashMap<String, String>> getItemMap() {
		return itemMap;
	}

	/**
	 * @param itemMap
	 *            the itemMap to set
	 */
	public void setItemMap(HashMap<String, HashMap<String, String>> itemMap) {
		this.itemMap = itemMap;
	}

	public Map<String, Map<String, String>> getItemUomHashMap() {
		return itemUomHashMap;
	}

	public void setItemUomHashMap(
			Map<String, Map<String, String>> itemUomHashMap) {
		this.itemUomHashMap = itemUomHashMap;
	}

	public Map<String, String> getUomConvFactorMap() {
		return uomConvFactorMap;
	}

	public void setUomConvFactorMap(Map<String, String> uomConvFactorMap) {
		this.uomConvFactorMap = uomConvFactorMap;
	}

	public String getTemplateName() {
		return templateName;
	}

	public String getEditedOrderHeaderKey() {
		return editedOrderHeaderKey;
	}

	public void setEditedOrderHeaderKey(String editedOrderHeaderKey) {
		this.editedOrderHeaderKey = editedOrderHeaderKey;
	}

	public String getDraft() {
		return draft;
	}

	public void setDraft(String draft) {
		this.draft = draft;
	}

	public HashMap<String, String> getItemToCustPartNoMap() {
		return itemToCustPartNoMap;
	}

	public void setItemToCustPartNoMap(
			HashMap<String, String> itemToCustPartNoMap) {
		this.itemToCustPartNoMap = itemToCustPartNoMap;
	}

	public XPEDXShipToCustomer getShipToCustomer() {
		return shipToCustomer;
	}

	public void setShipToCustomer(XPEDXShipToCustomer shipToCustomer) {
		this.shipToCustomer = shipToCustomer;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public void setPath(String path) {
		this.path = path;
	}

	public String getFacetDivShortDescription() {
		return facetDivShortDescription;
	}

	public void setFacetDivShortDescription(String facetDivShortDescription) {
		this.facetDivShortDescription = facetDivShortDescription;
	}

	/**
	 * This operation will verfiy if Value is Integer isInteger
	 *
	 * @param i
	 * @return
	 */
	public static boolean isDouble(String i) {
		try {
			Double.parseDouble(i);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	public String getSearchIndexInputXML() {
		return searchIndexInputXML;
	}

	public void setSearchIndexInputXML(String searchIndexInputXML) {
		this.searchIndexInputXML = searchIndexInputXML;
	}

	public String getFacetListItemAttributeKey() {
		return facetListItemAttributeKey;
	}

	public void setFacetListItemAttributeKey(String facetListItemAttributeKey) {
		this.facetListItemAttributeKey = facetListItemAttributeKey;
	}

	private String marketingGroupId; // eb-2772: if non-null, then searches on
										// items in this marketing group

	public String getMarketingGroupId() {
		return marketingGroupId;
	}

	public void setMarketingGroupId(String marketingGroupId) {
		this.marketingGroupId = marketingGroupId;
	}

	public String getRememberNewSearchText() {
		return rememberNewSearchText;
	}

	public void setRememberNewSearchText(String rememberNewSearchText) {
		this.rememberNewSearchText = rememberNewSearchText;
	}

}

