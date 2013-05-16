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

import javax.servlet.http.HttpServletRequest;
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
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.util.YFCUtils;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNode;
import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSEnvironment;

@SuppressWarnings("deprecation")
public class XPEDXCatalogAction extends CatalogAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger
			.getLogger(XPEDXCatalogAction.class);
	private String customerNumber = null;
	private boolean isStockedItem = false;
	private boolean CategoryC3= false;
	private String priceCurrencyCode;
	private static final String CUSTOMER_PART_NUMBER_FLAG = "1";
	private boolean stockedCheckeboxSelected ;
	private Map<String, Map<String, String>> itemUomHashMap = new HashMap<String, Map<String,String>>();
	ArrayList<String> itemIDList = new ArrayList<String>();
	private String catalogLandingMashupID = null;
	private XPEDXShipToCustomer shipToCustomer;
	private String msapOrderMultipleFlag = "";
	private Map <String, List<Element>> PLLineMap;	
	private String firstItem = "";
	private String indexField = "";
//Added class variable for JIRA #4195 - OOB variable searchTerm doesn't have a getter method exposed
	private String searchString=null;
//XNGTP-4264 and XB 355 Escaping Below words from search criteria.
	private String luceneEscapeWords[]={"a", "an", "and", "are", "as", "at", "be", "but", "by",
			 "for", "if", "in", "into", "is", "it",
			 "no", "not", "of", "on", "or", "such",
			  "that", "the", "their", "then", "there", "these",
			 "they", "this", "to", "was", "will", "with"
};

	
	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}		
	
	public String getIndexField() {
		return indexField;
	}

	public void setIndexField(String indexField) {
		this.indexField = indexField;
	}

	//added for EB 47
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
	//End of EB 47


	//End of addition for JIRA #4195
	/*Added for Jira 3624*/
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
	/*End of  Jira 3624*/
	private String categoryPath = "";
	
	public String getCategoryPath() {
		return categoryPath;
	}

	public void setCategoryPath(String categoryPath) {
		this.categoryPath = categoryPath;
	}

//Added for performance Fix 
	private String categoryShortDescription = "";
	
	public String getCategoryShortDescription() {
		return categoryShortDescription;
	}

	public void setCategoryShortDescription(String categoryShortDescription) {
		this.categoryShortDescription = categoryShortDescription;
	}

	//Start 2964
	private Map <String,String>defaultShowUOMMap;
	private Map<String,String>orderMultipleMap;
	private String itemDtlBackPageURL="";
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
	/*Start for webtrend XBT-35 */
	public String getSearchTerm() {
		return searchTerm;
	}
/*End of webtrend */
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

	//Webtrends tag start
	private boolean searchMetaTag = false;
	
	public boolean issearchMetaTag() {
		return searchMetaTag;
	}

	public void setsearchMetaTag(boolean searchMetaTag) {
		this.searchMetaTag = searchMetaTag;
	}
	
	//Webtrends tag end
	
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
		if(getWCContext().getWCAttribute("StockedCheckbox",WCAttributeScope.SESSION)!=null) {
			if(getWCContext().getWCAttribute("StockedCheckbox",WCAttributeScope.SESSION).toString().equalsIgnoreCase("true")){
				isStockedItem=true;
			}
			else{
				isStockedItem=false;
			}
		}
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
			String retVal=super.execute();
			getSortFieldDocument();
			setItemsUomsMap();
			init();
			return retVal;
		} catch (Exception e) {			
			XPEDXWCUtils.logExceptionIntoCent(e);  //JIRA 4289
			e.printStackTrace();
		}
		return "";
	}
	
	private void init() {
		try{
		req.setAttribute("Tag_WCContext", wcContext);	
		req.setAttribute("Tag_orderMultipleString", getText("MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES"));
		req.setAttribute("Tag_qtyString", getText("MSG.SWC.CART.ADDTOCART.ERROR.QTYGTZERO"));
		/* Begin - Changes made by Mitesh Parikh for 2422 JIRA */
		
		/*Begin Changes made for Jira 3464 - Replacing double quotes with unicode character*/
		//searchTerm="11\" W x 20\" L x 31\" H";
		//Changes JIRA #4195 
		if(searchString!=null&& !searchString.trim().equals("")){
			searchTerm=	searchString;
		}
		//End of Changes JIRA #4195 
		/*if(searchTerm!=null && searchTerm.contains("\"")){
			searchTerm= searchTerm.replaceAll("\"", "\\\\u0022");
		}*/
		
		/*End of changes made for Jira 3464*/
		if(searchTerm != null && !searchTerm.trim().equals(""))
		{   //Changes made for XBT 251 special characters replace by Space while Search
			//searchTerm=searchTerm.replaceAll("[\\[\\]\\-\\+\\^\\)\\;{!(}:,~\\\\]"," ");
			//Start Jira XBT-319
			searchTerm = processSpecialCharacters(searchTerm);
			//End Jira XBT-319
			
			//Start JIRA XBT-263
			searchTerm = XPXCatalogDataProcessor.preprocessSearchQuery(searchTerm);
			//End JIRA XBT-263
			setSearchString(searchTerm);//Added JIRA #4195 
			//String appendStr="%12%2Fcatalog%12search%12%12searchTerm%3D"+searchTerm+"%12catalog%12search%12"+searchTerm+"%11"+"&searchTerm="+searchTerm;
			String appendStr="&searchTerm="+searchTerm;
			XPEDXWCUtils.setItemDetailBackPageURLinSession(appendStr);
			if(searchTerm.trim().length() == 1 && (searchTerm.indexOf("*") == 0 || searchTerm.indexOf("?") == 0)) {
				searchTerm = "";
			}
			if(searchTerm.indexOf("*") == 0 || searchTerm.indexOf("?") == 0) 
				searchTerm = searchTerm.substring(1, searchTerm.length());
		}
		else
		{
			XPEDXWCUtils.setItemDetailBackPageURLinSession();
		}
		setItemDtlBackPageURL(wcContext.getSCUIContext().getSession().getAttribute("itemDtlBackPageURL").toString());
		//setItemDtlBackPageURL((wcContext.getSCUIContext().getRequest().getRequestURL().append("?").append(wcContext.getSCUIContext().getRequest().getQueryString())).toString());
		//setProductCompareBackPageURL((wcContext.getSCUIContext().getRequest().getRequestURL().append("?").append(wcContext.getSCUIContext().getRequest().getQueryString())).toString());
		setProductCompareBackPageURL(wcContext.getSCUIContext().getSession().getAttribute("itemDtlBackPageURL").toString());
	   /* End - Changes made for 2422 JIRA */
		}catch(Exception exception){
			//Not throwing any exception as it gives exception for JIRA 3705
			
			log.error("Error in Init Method", exception);
		}
	}

	private void getSortFieldDocument(){
		Map<String, String>  fl = this.getSortFieldList();
		Map<String, String> fieldMap=new LinkedHashMap<String, String>();
		List<String> keyValues=new ArrayList(fl.keySet());
		fieldMap.put("relevancy", getText("relevancy"));
        for(String keyValue:keyValues){                   
        	String field = fl.get(keyValue);
        	String dispText="";
        	for(String value:columnList){
        		if(field.contains(value)){
        			fieldMap.put(keyValue,field);
        			 break;
        		}
        		else{
	        		if(value.equals("Sku")){
	        			value="SKU";
	        		}if(value.equals("Desc")){
	        			value="Name";
	        		}
	        		if(field.contains(value) || field.contains("Env") || field.contains("Stock")){
	        			if(field.contains("Env") && field.contains("ascending"))
	        				field = field.replaceAll("ascending", "descending");
	        			else if(field.contains("Env") && field.contains("descending"))
	        				field = field.replaceAll("descending", "ascending");
	        			fieldMap.put(keyValue,field);
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
		
		/*Map<String, String> topCategoryMap = (Map<String, String>)XPEDXWCUtils.getObjectFromCache("TopCategoryMap");
		List<Breadcrumb> bclFilter = BreadcrumbHelper.preprocessBreadcrumb(this
				.get_bcs_());
		
		for (int i = 0; i < bclFilter.size(); i++) {
			Breadcrumb bc = bclFilter.get(i);
			Map<String, String> bcParams = bc.getParams();
			String cnameValue = bcParams.get("cname");
			if (cnameValue != null && cnameValue.equals("Paper") && i <=2) {
				orderByAttribute = "Item.ExtnBasis";
				break;
			}			
		} */						
		
		if(("").equals(super.sortField) && (super.session.get("sortField") == null || ("").equals(super.session.get("sortField")))) {			
			orderByAttribute = "Item.ExtnBestMatch";
			sortField = "Item.ExtnBestMatch--A";
		}
		
		String returnString = super.filter();
		//XBT-260
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

		// Added for debugging Breadcrumb parameters
		for (int i = 0; i < bcl.size(); i++) {
			Breadcrumb bc = bcl.get(i);
			Map<String, String> bcParams = bc.getParams();
			String cnameValue = bcParams.get("cname");
			log.debug("CatalogAction : filter(): Breadcrumb : cnameValue=" + cnameValue); 
		} // end of debugging code

		/****End of Code Changed for Promotions JIra 2599 *******/


		shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);

		if (ERROR.equals(returnString)) {
			return returnString;
		} else {
			try
			{
				getAllAPIOutput();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			setItemsUomsMap();
			setAttributeListForUI();
			prepareItemBranchInfoBean();
			setColumnListForUI();
		
	   		/****Start of Code Changed for Promotions JIra 2599 *******/
			if((path==null||path.equals("/")) && getFirstItem().trim()!=""){
				/* start of performance code */
				YFCNode yfcNode = YFCDocument.getDocumentFor(getOutDoc())
				.getDocumentElement().getChildElement("ItemList")
				.getFirstChild();
				YFCIterable<? extends YFCNode> YFCIterables = yfcNode.getChildren();
				while(YFCIterables.hasNext()){
					YFCNode node = YFCIterables.next();
					if(node!=null && node.getNodeName().equalsIgnoreCase("CategoryList")){
						path = node.getFirstChild().getAttributes().get("CategoryPath");
						categoryPath = path;
					}
				}
				/*end of performance code*/
				//path=XPEDXWCUtils.getCategoryPathPromo(getFirstItem(), wcContext.getStorefrontId());
			}
			/****End of Code Changed for Promotions JIra 2599 *******/
		getCatTwoDescFromItemIdForpath(getOutDoc().getDocumentElement(),categoryPath);

		}
		return SUCCESS;
	}

/*Added for performance issue */
	public  void getCatTwoDescFromItemIdForpath(Element categoryList,String path) {
            categoryPath=path;
            StringBuilder cat = new StringBuilder();
            String adjugglerKeywordPrefix = XPEDXWCUtils.getAdJugglerKeywordPrefix();
            String categoryID="";
            //getting exact cat2 path and category id  from the actual path
            if(path != null && path.trim().length()>0 && categoryList != null  )
            {
                  StringTokenizer st = new StringTokenizer(path, "/");
                  if(st.hasMoreTokens())
                  {
                        cat.append("/").append(st.nextToken());
                        if(st.hasMoreTokens())
                        {
                              cat.append("/").append(st.nextToken());
                              if(st.hasMoreTokens())
                              {
                                    categoryID=st.nextToken();
                                    cat.append("/").append(categoryID);
                              }
                        }
                  }
                  try
                  {
                        //checking if the cat2 path is already there in cache if yes then get it from there 
                        ArrayList<Element> categoryElements=SCXmlUtil.getElements(categoryList, "/CategoryList/Category");
                        if(categoryElements != null)
                        categoryElements.addAll(SCXmlUtil.getElements(categoryList, "/CategoryList/Category/ChildCategoryList/Category"));
                        else
                              categoryElements=SCXmlUtil.getElements(categoryList, "/CategoryList/Category/ChildCategoryList/Category");
                        for(Element catgegory: categoryElements)
                        {
                              String shortDescription=catgegory.getAttribute("ShortDescription");
                              String categoryPath=catgegory.getAttribute("CategoryPath");
                              if(categoryPath != null && categoryPath.equals(cat.toString()))
                              {
                                    categoryShortDescription = shortDescription;
                                    break;
                              }
                        }
                  }
                  catch(Exception e)
                  {
                        log.error("Error while getting ShortDescreption for Adjuggler "+e.getMessage());
                  }
            }
            // Added For XBT-253
            //if category id is not there in caceh then check whether the item is there on cat2 level if yes then get it from there
            if(categoryShortDescription == null || categoryShortDescription.equals("")){
                  if(firstItemCategoryShortDescription !=null && firstItemCategoryShortDescription.length() >0)
                  {
                        categoryShortDescription= firstItemCategoryShortDescription;
                  }
                  else
                  {
                        //if category id is not there in cacche and first item is also not at cat2 level , do the api call for getCategoryList which will give shortdescription
                        categoryShortDescription =getCategoryShortDescription(categoryID);
                        if(categoryShortDescription == null)
                              categoryShortDescription=(String)XPEDXWCUtils.getObjectFromCache("defaultCategoryDesc");
                  }
            }
            categoryShortDescription= XPEDXWCUtils.sanitizeAJKeywords(adjugglerKeywordPrefix+categoryShortDescription);
      }

      /*
      * This method will do a API call getCategoryList based on given category ID
      * @param categoryID
      * return shortDescriotion 
       */
      private String getCategoryShortDescription(String categoryID)
      {
            YFCDocument getCategoryListInXML = YFCDocument
            .createDocument("Category");
            
            YFCElement categoryLstEle = getCategoryListInXML.getDocumentElement();
            categoryLstEle.setAttribute("CategoryID", categoryID);
            categoryLstEle.setAttribute("OrganizationCode", wcContext.getStorefrontId());

            
            /*Input : <Category CategoryID="300131"  OrganizationCode="xpedx"></Category>
            * 
             * Output : <CategoryList ><Category CategoryID="" CategoryKey="" CategoryPath="" Description="" ShortDescription="" ></Category></CategoryList>
            */
            YFCDocument template2 = YFCDocument
            .getDocumentFor("<CategoryList ><Category /></CategoryList>");
            ISCUITransactionContext scuiTransactionContext = null;
            SCUIContext wSCUIContext = null;
            YFCElement categoryListElement = null;
                                  
            try {
            	IWCContext context = WCContextHelper
    					.getWCContext(ServletActionContext.getRequest());
                wSCUIContext = context.getSCUIContext();
    			scuiTransactionContext = wSCUIContext.getTransactionContext(true);
                

                categoryListElement = SCUIPlatformUtils.invokeXAPI("getCategoryList",
                            categoryLstEle, template2.getDocumentElement(), wcContext.getSCUIContext());
            	
            } catch (Exception ex) {
    			log.error(ex.getMessage());
    			scuiTransactionContext.rollback();
    		} finally {
    			if (scuiTransactionContext != null) {
    				SCUITransactionContextHelper.releaseTransactionContext(
    						scuiTransactionContext, wSCUIContext);
    			}
    		}
            
            if(categoryListElement == null )
            {
                  return null;
            }
            
            YFCElement catEle = categoryListElement.getFirstChildElement();
            if(catEle != null )
            {
                  return catEle.getAttribute("ShortDescription");
            }
            
            return null;
      }

	/*
	 * This method sets the instance varaible "columList" by getting all the
	 * Column Names from the DB. This is determined by the FilterType and the
	 * Value. If the FilterType is "ProductType", it fethches the DB for all the
	 * ColumnNames for the FilterType value; If the Filter(NarrowBy) is not
	 * applied OR if the Filter Type is not "ProductType" then the default set
	 * of ColumnNames are returned.
	 */
	public void setColumnListForUI() {
		List<Breadcrumb> bcl = BreadcrumbHelper.preprocessBreadcrumb(this
				.get_bcs_());
		log.debug("CatalogAction : setColumnListForUI(): start");

		boolean isLayoutDefined = false;
		try {
			Iterator<Breadcrumb> iter = bcl.iterator();
			while (iter.hasNext()) {
				Breadcrumb bc = iter.next();
				if (bc.getAction() != null && bc.getAction().equals("filter")) {
					//String indexField = (String) bc.getParams().get(
					//		"indexField");										
					String filterDesc = (String) bc.getParams().get("filterDesc");
					log.debug("CatalogAction : setColumnListForUI(): Breadcrumb : indexField=" + indexField);
					log.debug("CatalogAction : setColumnListForUI(): Breadcrumb : filterDesc=" + filterDesc);
					/*String attributeName = null;
					if (!YFCCommon.isVoid(filterDesc)
							&& filterDesc
									.contains(XPEDXCatalogAction.XPEDX_PRODUCT_LINE_INDEX_FIELD)) {
						attributeName=filterDesc;
					}*/
					// indexField is the Attribute ID. We need to get the
					// Attribute Name(Short Description) to compare to
					// XPEDX_PRODUCT_TYPE_NARROW_BY
					/*Fetching the description from the param itself.
					 * This is set in the jsp while setting the URL
					 * if(YFCCommon.isStringVoid(attributeName)) {
						String[] indexFieldSplit = StringUtils.split(indexField,
								".");
						// String[] indexFieldSplit = indexField.split(".");
						String attDomainID = indexFieldSplit[0];
						String attGrpID = indexFieldSplit[1];
						String attributeID = indexFieldSplit[2];
						attributeName = XPEDXWCUtils.getAttributeName(
								attributeID, wcContext.getStorefrontId(),
								attDomainID, attGrpID);
					}*/
					if (!YFCCommon.isVoid(indexField)
							&& indexField
									.contains(XPEDXCatalogAction.XPEDX_PRODUCT_LINE_INDEX_FIELD)) {						
						String productType = (String) bc.getParams().get(
								"cname");
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
			if(log.isDebugEnabled()){
			log.debug("setColumnListForUI: " + columnList.toString());
			}
		} catch (CannotBuildInputException e) {
			LOG
					.error("Error Getting Column Names from the DB for product type.... "
							+ e);
			columnList = new ArrayList<String>();
		} catch (Exception e) {
			LOG
					.error("Error Getting Column Names from the DB for product type.... "
							+ e);
			columnList = new ArrayList<String>();
		}
	}

	protected void populateMashupInput(String mashupId,
			Map<String, String> valueMap, Element mashupInput)
			throws WCMashupHelper.CannotBuildInputException {
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
		 * Following line of code replaces the search term from leading astrix by blank
		 * It also splits the search term by space and add as a must attribute in search
		 * term
		 * 
		 */

		if (null != searchStringValue && !"".equals(searchStringValue.trim())) {
			searchStringValue = searchStringValue.trim();
				/*Begin Changes made for Jira 3464 - Replacing double quotes with unicode character*/
			/*if(searchStringValue.contains("\"")){
				searchStringValue= searchStringValue.replaceAll("\"", "\\\\u0022");
			}*/
			
			
			
			/*End of changes 3464*/
			if(searchStringValue.indexOf("*") == 0 || searchStringValue.indexOf("?") == 0) 
				searchStringValue = searchStringValue.substring(1, searchStringValue.length());  
			
			//Start JIRA XBT-263
			searchStringValue = XPXCatalogDataProcessor.preprocessSearchQuery(searchStringValue);
			//End JIRA XBT-263
			
			//Start Jira XBT-319
			searchStringValue = processSpecialCharacters(searchStringValue);
			//End Jira XBT-319
			
			//Changes made for XBT 251 special characters replace by Space while Search
			//searchStringValue=searchStringValue.replaceAll("[\\[\\]\\-\\+\\^\\)\\;{!(}:,~\\\\]"," ");
			String searchStringTokenList[] = searchStringValue.split(" ");
			int i = 1;
			//JIRA - 4264 There are few lucene words , which are ignored for search criteria
			List<String> specialWords=Arrays.asList(luceneEscapeWords);
			for (String searchStringToken : searchStringTokenList) {
				if(!specialWords.contains(searchStringToken.trim().toLowerCase()))
				{
					if(!"".equals(searchStringToken.trim())) {
						valueMap.put("/SearchCatalogIndex/Terms/Term[" + i + "]/@Value", searchStringToken.trim());
					valueMap.put("/SearchCatalogIndex/Terms/Term["+ i + "]/@Condition", "MUST");
					i++;
					}
				}
			}	
	
		}					
		super.populateMashupInput(mashupId, valueMap, mashupInput);
		ArrayList<Element> elements = SCXmlUtil.getElements(mashupInput,
				"Terms");
		boolean flag = false;
		List<Element> termListEle = null;
		if (elements != null && elements.size() > 0) {				
			Element terms = elements.get(TERMS_NODE);	
			termListEle = SCXmlUtil.getChildrenList(terms);
			for(Element termEle : termListEle) {
				String termValue = termEle.getAttribute("Value");
				/*
				 * Following if block checks if there is only one search term in terms 
				 * and if this search term is * then it sets the flag
				 * this flag is lateron checked in code and it removes the complete Terms
				 * from mashupinput
				 * 
				 */
				if(termValue.trim().length() == 1 && (termValue.indexOf("*") == 0 || termValue.indexOf("?") == 0) && termListEle != null && termListEle.size() == 1) {
					flag = true;
				}
				/*
				 * Following line of code checks if there are multiple terms
				 * and one of the search term is only * then it removes only term 
				 * which contains * attribute
				 */
				if (termValue.trim().length() == 1 && (termValue.indexOf("*") == 0 || termValue.indexOf("?") == 0) && termListEle != null && termListEle.size() > 1) {
					termListEle.remove(termEle);
				}
				/*
				 * Following code replaces all the * in leading to blank
				 * 
				 * 
				 */
				if(termValue.indexOf("*") == 0 || termValue.indexOf("?") == 0) {
					termValue = termValue.substring(1, termValue.length());
					termEle.setAttribute("Value", termValue);
				}
				/*Begin Changes made for Jira 3464 - Replacing double quotes with unicode character	
				if(termValue!=null && termValue.contains("\"")){
					termValue= termValue.replaceAll("\"", "\\\\u0022");
					termEle.setAttribute("Value", termValue);
				}
				End of changes 3464*/	
				
				//Start JIRA XBT-263
				termValue = XPXCatalogDataProcessor.preprocessSearchQuery(termValue);
				//End JIRA XBT-263
				
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
					/*Begin Changes made for Jira 3464 - Replacing double quotes with unicode character*/
				/*if(searchTerm!=null && searchTerm.contains("\"")){
				searchTerm= searchTerm.replaceAll("\"", "\\\\u0022");
				}*/	
				/*end of 3464*/			
				
				//Start JIRA XBT-263
				searchTerm = XPXCatalogDataProcessor.preprocessSearchQuery(searchTerm);
				//End JIRA XBT-263
				
				term.setAttribute("Value", customerNumber + searchTerm);
			}
		}
		if (flag) {
			SCXmlUtil.removeNode(elements.get(0));
		}
		setStockedItemFromSession();
		shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		if (isStockedItem) {
			if(shipToCustomer== null){
				XPEDXWCUtils.setCustomerObjectInCache(XPEDXWCUtils
						.getCustomerDetails(getWCContext().getCustomerId(),
								getWCContext().getStorefrontId())
						.getDocumentElement());
				setShipToCustomer((XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER));
			}
			String shipFromDivision = shipToCustomer.getExtnShipFromBranch();
			if (shipFromDivision != null
					&& shipFromDivision.trim().length() > 0) {
				Element terms = null;
				if (elements != null && elements.size() > 0) {
					terms = elements.get(TERMS_NODE);
				} else {
					terms = SCXmlUtil.createChild(mashupInput, "Terms");
				}
				/*Element filters = SCXmlUtil.createChild((Element) terms
						.getParentNode(), "Filters");
				Element filter = SCXmlUtil.createChild(filters, "Filter");
				filter.setAttribute("IndexFieldName",
						"showStockedItems");
				filter.setAttribute("Type", "Normal");
				filter.setAttribute("Value", shipFromDivision); */
				
				Element term = SCXmlUtil.createChild(terms, "Term");
				term.setAttribute("Condition", "MUST");
				term.setAttribute("IndexFieldName", "showStockedItems");
				term.setAttribute("Value", shipFromDivision);
			}
		}
	}

	private void changeBasis()
	{
		
		if(getOutDoc() != null)
		{
			Element itemEementList=(Element)getOutDoc().getElementsByTagName("ItemList").item(0);
			if(itemEementList != null)
			{
				NodeList extnNodeList=itemEementList.getElementsByTagName("Extn");
				if(extnNodeList != null)
				{
					for(int i=0;i<extnNodeList.getLength();i++)
					{
						Element itemElement=(Element)extnNodeList.item(i);
						//Commenting not to promote to 12/15
						/*if(itemElement  != null)
						{
							String extnBasisAttr=itemElement.getAttribute("ExtnBasis");
							if(extnBasisAttr != null && extnBasisAttr.length() >0)
							{
								String extnBasis=extnBasisAttr.replaceFirst("^0+(?!$)", "");
								extnBasis=extnBasis.replaceFirst("\\.?0*$","");
								if(extnBasis != null && !"0".equals(extnBasis))
									itemElement.setAttribute("ExtnBasis", extnBasis);
								else
									itemElement.setAttribute("ExtnBasis","");
							}
						}*/
						if(itemElement  != null)
                        {
				if(itemElement.getAttribute("ExtnBasis") != null)
				{		
                              String extnBasis=itemElement.getAttribute("ExtnBasis").replaceFirst("^0+(?!$)", "");
                              if(extnBasis != null && !"0".equals(extnBasis))
                                    itemElement.setAttribute("ExtnBasis", extnBasis);
                              else
                                    itemElement.setAttribute("ExtnBasis","");
				}
                        }

					}
				}
			}
		}
	}
	@Override
	public String newSearch() {
		try{
		init();
		setCustomerNumber();
		StringBuffer sb=new StringBuffer();
		long startTime=System.currentTimeMillis();		
		
		if(("").equals(super.sortField) && (super.session.get("sortField") == null || ("").equals(super.session.get("sortField")))) {			
			orderByAttribute = "Item.ExtnBestMatch";	
			sortField = "Item.ExtnBestMatch--A";
		}
		
		String returnString = super.newSearch();
		//getting the customer bean object from session.
		//XBT-260
		changeBasis();
		long endTime=System.currentTimeMillis();
		long timespent=(endTime-startTime);
		sb.append("OOTB execution time on catlaog newSearch() = "+timespent);
		shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
	
		/***** Start of  Code changed for Promotions ********/ 

		

		List<Breadcrumb> bcl = BreadcrumbHelper.preprocessBreadcrumb(this
				.get_bcs_());
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
		/****End of Code Changed for Promotions *******/

		if (bcl.size() > 1 || (!("true".equals(displayAllCategories)))) {
			if (!YFCCommon.isVoid(pathDepth) && pathDepth.length == 2) {
				// for c1 categories-> show all the sub categories of C1 in the
				// landing page
				setCategoryDepth("1");// default value is 2; coming from struts
				// file
			}
		}
		//Webrtends	tag start
		setsearchMetaTag(true);
		//Webrtends	tag start
		if (ERROR.equals(returnString)) {
			return returnString;
		}
		else if (isSingleItem()) { // First checking if its a single item and doing the redirection with out making any other DB calls
			YFCNode yfcNode = YFCDocument.getDocumentFor(getOutDoc())
					.getDocumentElement().getChildElement("ItemList")
					.getFirstChild();
			setItemID(yfcNode.getAttributes().get("ItemID"));
			setUnitOfMeasure(yfcNode.getAttributes().get("UnitOfMeasure"));
			return "singleItem";
		}
		else {
			long startTimeCustomerService=System.currentTimeMillis();
			
			getAllAPIOutput();
			long endTimeCustomerService=System.currentTimeMillis();
			timespent=(endTimeCustomerService-startTimeCustomerService);
			sb.append("\nCustom Service Execution time on catlaog newSearch() = "+timespent);
			setItemsUomsMap();
			setAttributeListForUI();
			prepareItemBranchInfoBean();
			setColumnListForUI();
	
	/****Start of Code Changed for Promotions JIra 2599 *******/			
			//code changed for performance
			/*if(path.equals("/") && getFirstItem().trim()!=""){
				YFCNode yfcNode = YFCDocument.getDocumentFor(getOutDoc())
				.getDocumentElement().getChildElement("ItemList")
				.getFirstChild();
				YFCIterable<? extends YFCNode> YFCIterables = yfcNode.getChildren();
				while(YFCIterables.hasNext()){
					YFCNode node = YFCIterables.next();
					if(node!=null && node.getNodeName().equalsIgnoreCase("CategoryList")){
						path = node.getFirstChild().getAttributes().get("CategoryPath");
						categoryPath = path;
					}
				}*/
				//path=XPEDXWCUtils.getCategoryPathPromo(getFirstItem(), wcContext.getStorefrontId());
				path=tempCategoryPath;
				categoryPath = path;
			//}
			
	/****End of Code Changed for Promotions JIra 2599 *******/
		

	}
	
		setStockedItemFromSession();				
		getCatTwoDescFromItemIdForpath(getOutDoc().getDocumentElement(),categoryPath);
		endTime=System.currentTimeMillis();
		timespent=(endTime-startTime);
		sb.append("\nTotal time of Action execution on catlaog newSearch() = "+timespent);
		System.out.println(sb.toString());
		}catch(Exception exception){
			//Not throwing any exception as it gives exception for JIRA 3705
			
			log.error("Error while refeshing catalog cache in method newSearch", exception);
			XPEDXWCUtils.logExceptionIntoCent(exception);  //JIRA 4289
		        
			
		}	
		
		return SUCCESS;
	}

	private void getAllAPIOutput() throws Exception
	{
		Document catDoc = getOutDoc();
		
		// get the CatalogSearch/ItemList
		Element itemListElement = SCXmlUtil.getChildElement(catDoc
				.getDocumentElement(), "ItemList");
		NodeList itemNodeList = itemListElement.getElementsByTagName("Item");
		ArrayList<String> itemIds = new ArrayList<String>();
		
		for (int i = 0; i < itemNodeList.getLength(); i++) {
			itemIds.add(SCXmlUtil.getAttribute((Element) itemNodeList.item(i), "ItemID"));
		}
		XPEDXShipToCustomer shipToCustomer = (XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		
		try {
			if(shipToCustomer==null) {
				XPEDXWCUtils.setCustomerObjectInCache(XPEDXWCUtils.getCustomerDetails(wcContext.getCustomerId(),wcContext.getStorefrontId())
						.getDocumentElement());
				shipToCustomer = (XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);	
			}
		}
		catch (Exception e) {
			log.error("Error fetching or creating the shipToCustomer object in to the session in getXpxItemCustXRefDoc() So returning back null");
		}
		
		if(!(itemIds!=null && itemIds.size()>0))
			return;
		
		if(shipToCustomer!=null) {
			String envCode = shipToCustomer.getExtnEnvironmentCode();
			String legacyCustomerNumber=shipToCustomer.getExtnLegacyCustNumber();
			String custDivision = shipToCustomer.getExtnCustomerDivision();
			HashMap<String,String> valueMap =  new HashMap<String, String>();
			valueMap.put("/XPXItemcustXref/@EnvironmentCode", envCode);
			valueMap.put("/XPXItemcustXref/@CustomerNumber", legacyCustomerNumber);
			//valueMap.put("/XPXItemcustXref/@LegacyItemNumber", itemId); Filled using Complex Query
			valueMap.put("/XPXItemcustXref/@CustomerDivision", custDivision);
			
			Element xrefInput =  null;
			Element res =  null;
			try {
				// Commented for Jira 2796. Calling the XPEDXMyItemsListGetCustomersPart to compare customer number and the customerpartnumber.
				//input = WCMashupHelper.getMashupInput("xpedxGetItemCustXRef", valueMap, context.getSCUIContext()); 
				xrefInput = WCMashupHelper.getMashupInput("XPEDXMyItemsListGetCustomersPart", valueMap,  wcContext.getSCUIContext());
				Element complexQuery = xrefInput.getOwnerDocument().createElement("ComplexQuery");
				xrefInput.appendChild(complexQuery);
				Element Or = xrefInput.getOwnerDocument().createElement("Or");
				complexQuery.appendChild(Or);
				Iterator<String> itemIdIter = itemIds.iterator();
				/*while(itemIdIter.hasNext()) {
					Element expElement = xrefInput.getOwnerDocument().createElement("Exp");
					expElement.setAttribute("Name", "LegacyItemNumber");
					expElement.setAttribute("QryType", "EQ");
					expElement.setAttribute("Value", itemIdIter.next());
					SCXmlUtil.importElement(Or, expElement);
				}*/
				// Commented for Jira 2796. Calling the XPEDXMyItemsListGetCustomersPart to compare customer number and the customerpartnumber.
				//res = (Element)WCMashupHelper.invokeMashup("xpedxGetItemCustXRef",input , context.getSCUIContext());
				
				String customerId = wcContext.getCustomerId();
				Map<String, String> valueMaps = new HashMap<String, String>();
				valueMaps.put("/PricelistAssignment//@CustomerID", customerId);
				valueMaps.put("/PricelistAssignment//@ExtnPriceWareHouse", shipToCustomer.getExtnPriceWareHouse());
				valueMaps.put("/PricelistAssignment/PricelistLine/Item/@OrganizationCode", wcContext.getStorefrontId());
				Element pricLlistAssignmentInput = WCMashupHelper.getMashupInput("xpedxYpmPriceLinelistAssignmentList", valueMaps,getWCContext().getSCUIContext());
				Document pricLlistAssignmentInputDoc = pricLlistAssignmentInput.getOwnerDocument();
				NodeList pricLlistAssignmentInputNodeList = pricLlistAssignmentInput.getElementsByTagName("Or");
				Element pricLlistAssignmentInputElemt = (Element) pricLlistAssignmentInputNodeList.item(0);
				/*itemIdIter = itemIds.iterator();
				while(itemIdIter.hasNext()) {
					Document expDoc = YFCDocument.createDocument("Exp").getDocument();
					Element expElement = expDoc.getDocumentElement();
					expElement.setAttribute("Name", "ItemID");
					expElement.setAttribute("Value", itemIdIter.next());
					pricLlistAssignmentInputElemt.appendChild(pricLlistAssignmentInputDoc.importNode(expElement, true));
				}*/
				Element input = WCMashupHelper.getMashupInput("xpedxgetAllAPI",  wcContext
						.getSCUIContext());
				
				YFCDocument inputDocument = YFCDocument.createDocument("UOMList");
				YFCElement documentElement = inputDocument.getDocumentElement();

				documentElement.setAttribute("CustomerID", customerId);
				documentElement.setAttribute("OrganizationCode", wcContext.getStorefrontId());
				
				YFCElement complexQueryElement = documentElement.createChild("ComplexQuery");
				YFCElement complexQueryOrElement = documentElement.createChild("Or");
				
				complexQueryElement.setAttribute("Operator", "AND");
				complexQueryElement.appendChild(complexQueryOrElement);
				Element customerDetails=SCXmlUtil.createChild(inputDocument.getDocument().getDocumentElement(), "CustomerDetails");
				//EB 47 customerDetails.setAttribute("ExtnCompanyCode", shipToCustomer.getExtnCompanyCode());
				customerDetails.setAttribute("ExtnEnvironmentCode", shipToCustomer.getExtnEnvironmentCode());
				customerDetails.setAttribute("ExtnShipFromBranch", shipToCustomer.getExtnShipFromBranch());
				customerDetails.setAttribute("ExtnCustomerDivision", shipToCustomer.getExtnCustomerDivision());
				customerDetails.setAttribute("ExtnUseOrderMulUOMFlag", shipToCustomer.getExtnUseOrderMulUOMFlag());
				
				valueMap = new HashMap<String, String>();
				valueMap.put("/XPXItemExtn/@XPXDivision", shipToCustomer.getExtnShipFromBranch());
				valueMap.put("/XPXItemExtn/@EnvironmentID", envCode);

				Element xpxItemExtninputElem = WCMashupHelper.getMashupInput("xpedxXPXItemExtnList", valueMap,wcContext.getSCUIContext());
				
				Document inputDoc = xpxItemExtninputElem.getOwnerDocument();
				NodeList inputNodeList = inputDoc.getElementsByTagName("Or");
				Element inputNodeListElemt = (Element) inputNodeList.item(0);
				itemIdIter = itemIds.iterator();
				orderMultipleMap = new HashMap<String,String>();
				while(itemIdIter.hasNext()) {
					//Complex query for XPXItemExtn input xml
					Element expElement =SCXmlUtil.createChild(inputNodeListElemt, "Exp");
					String itemid=itemIdIter.next();
					expElement.setAttribute("Name", "ItemID");
					expElement.setAttribute("Value",itemid );
					
					//Complex query for itemXef input xml
					Element exp1Element = xrefInput.getOwnerDocument().createElement("Exp");
					exp1Element.setAttribute("Name", "LegacyItemNumber");
					exp1Element.setAttribute("QryType", "EQ");
					exp1Element.setAttribute("Value", itemid);
					SCXmlUtil.importElement(Or, exp1Element);
					
					//Complex query for PriceLineList input xml
					Document expDoc = YFCDocument.createDocument("Exp").getDocument();
					Element exp2Element = expDoc.getDocumentElement();
					exp2Element.setAttribute("Name", "ItemID");
					exp2Element.setAttribute("Value", itemid);
					pricLlistAssignmentInputElemt.appendChild(pricLlistAssignmentInputDoc.importNode(exp2Element, true));
					
					//UOMList input XML
					YFCElement exp3Element = documentElement.createChild("Exp");
					exp3Element.setAttribute("Name", "ItemID");
					exp3Element.setAttribute("Value", itemid);
					complexQueryOrElement.appendChild((YFCNode)exp3Element);
					orderMultipleMap.put(itemid, "1");
				}
				input.appendChild(input.getOwnerDocument().importNode(xrefInput, true));
				input.appendChild(input.getOwnerDocument().importNode(pricLlistAssignmentInput, true));
				input.appendChild(input.getOwnerDocument().importNode(xpxItemExtninputElem, true));
				input.appendChild(input.getOwnerDocument().importNode(inputDocument.getDocument().getDocumentElement(), true));
				String inputXml = SCXmlUtil.getString(input);
				
				 allAPIOutputDoc=(Element)WCMashupHelper.invokeMashup(
						"xpedxgetAllAPI", input, wcContext
								.getSCUIContext());
				 
				 getOrderMultipleMapForItems();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private boolean isSingleItem() {
		YFCDocument yfcDocument = YFCDocument.getDocumentFor(getOutDoc());
		if (yfcDocument != null && yfcDocument.getDocumentElement() != null) {
			YFCElement yfcElement = yfcDocument.getDocumentElement()
					.getChildElement("ItemList");
			if (yfcElement != null && yfcElement.getChildNodes() != null) {
				int length = yfcElement.getChildNodes().getLength();
				if (length == 1) {
					return true;
				}
			}
		}
		return false;
	}

	private void setAttributeListForUI() {
		NodeList FacetList = SCXmlUtil
				.getXpathNodes(getOutDoc().getDocumentElement(),
						"/CatalogSearch/FacetList/ItemAttribute");
		attributeMap = new HashMap();
		for (int i = 0; i < FacetList.getLength(); i++) {
			//Added for JIRA 3821
			Element facetEle = (Element) FacetList.item(i);
			Element itemattr = SCXmlUtil.getChildElement(facetEle, "Attribute");
			String shortDesc = itemattr.getAttribute("ShortDescription");
			List<Element> itemAttribute = SCXmlUtil.getElements(facetEle, "AssignedValueList/AssignedValue");
			Collections.sort(itemAttribute, new  Comparator<Element>()			
			{
				public int compare(Element elem, Element elem1) {
					String attrValue =elem.getAttribute("Value");
					String attrValue1 =elem1.getAttribute("Value");
					 if(isDouble(attrValue) && isDouble(attrValue1)){
                         return Double.valueOf(attrValue).compareTo(Double.valueOf(attrValue1));
					 }
					return attrValue.compareTo(attrValue1);
				}
			});
			facetListMap.put(shortDesc,itemAttribute);
			//End of JIRA 3821
			String attrName = facetEle.getAttribute("ItemAttributeName");
			String isFiltered = facetEle.getAttribute("IsProvidedFilter");
			if (!(isFiltered != null && isFiltered.trim().length() > 0 && "Y"
					.equals(isFiltered)))
				attributeMap.put(attrName, attrName);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sterlingcommerce.webchannel.catalog.CatalogAction#navigate()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String navigate() {
		try{
		/*Start of webtrend tags*/
			setsearchMetaTag(false);
		/*End of webtrend tags*/
		init();	
		if(draft!=null && !YFCCommon.isVoid(draft) && "N".equals(draft))
		{
			XPEDXWCUtils.setEditedOrderHeaderKeyInSession(wcContext, editedOrderHeaderKey);
		}
		
		List<Breadcrumb> bcl = BreadcrumbHelper.preprocessBreadcrumb(this
				.get_bcs_());
		log.debug("CatalogAction : navigate(): start");
		Breadcrumb lastBc = bcl.get(bcl.size() - 1);
		Map<String, String> params = lastBc.getParams();
		String[] pathDepth = StringUtils.split(path, "/");
		path = params.get("path");		
		
		// Added for debugging Breadcrumb parameters
		for (int i = 0; i < bcl.size(); i++) {
			Breadcrumb bc = bcl.get(i);
			Map<String, String> bcParams = bc.getParams();
			String cnameValue = bcParams.get("cname");
			log.debug("CatalogAction : navigate(): Breadcrumb : cnameValue=" + cnameValue); 
		} // end of debugging code			}			
		
		/*Map<String, String> topCategoryMap = (Map<String, String>)XPEDXWCUtils.getObjectFromCache("TopCategoryMap");
		if (topCategoryMap == null) {			
			for (int i = 0; i < bcl.size(); i++) {
				Breadcrumb bc = bcl.get(i);
				Map<String, String> bcParams = bc.getParams();			
				String cnameValue = bcParams.get("cname");
				if (cnameValue != null && cnameValue.equals("Paper") && i <=2) {
					orderByAttribute = "Item.ExtnBasis";
					break;
				}			
			}
		} else {
			if (pathDepth != null) {
				String categoryID = pathDepth[1];				
				String catSelected = topCategoryMap.get(categoryID);
				if (catSelected !=null && catSelected.equals("Paper")) {
					orderByAttribute = "Item.ExtnBasis";
				}
			}
		} */
		
		if(("").equals(super.sortField) && (super.session.get("sortField") == null || ("").equals(super.session.get("sortField")))) {			
			orderByAttribute = "Item.ExtnBestMatch";
			sortField = "Item.ExtnBestMatch--A";
		}
	//Added below condn for XBT-269
		if("2".equals(categoryDepthNarrowBy))
		{
			setCategoryDepth("2");
		}

	else if (bcl.size() > 1 || (!("true".equals(displayAllCategories)))) {
			if (!YFCCommon.isVoid(pathDepth) && pathDepth.length == 2) {
				// for c1 categories-> show all the sub categories of C1 in the
				// landing page
				setCategoryDepth("1");// default value is 2; coming from struts
				// file
			}
		}
		//getting the customer bean object from session.
		shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);

		//determine if the request is catalog landing or catalog page
		boolean isCategoryLanding = determineCatalogLandingRedirection(bcl,pathDepth);
	//Added below condn for XBT-269
		if(!"2".equals(categoryDepthNarrowBy) && isCategoryLanding){
			getWCContext().removeWCAttribute("StockedCheckbox", WCAttributeScope.SESSION);
			//call the catalog landing mashup
			setMashupID(getCatalogLandingMashupID());
		}

		String returnString = super.navigate();
		//XBT-260
		
		if (ERROR.equals(returnString)) {
			return returnString;
		} else {
			setAttributeListForUI();
			//Added below condn for XBT-269
			if (!"2".equals(categoryDepthNarrowBy) && (isCategoryLanding || displayAllCategories.equalsIgnoreCase("true"))) {
			
				if(log.isDebugEnabled()){
				log.debug("Search for Category Domain. Need to show the asset widget for Category Images");
				}
				return setCategoryDomainAssetList();
			}
			if (wcContext.isGuestUser())
				isGuestUser = "Y";
			changeBasis();
			getAllAPIOutput();
			setItemsUomsMap();
			prepareItemBranchInfoBean();
			setColumnListForUI();
			//prepareMyItemListList();
			getSortFieldDocument();
		}
		getCatTwoDescFromItemIdForpath(getOutDoc().getDocumentElement(),path);
		}catch(Exception exception){
			//Not throwing any exception as it gives exception for JIRA 3705
			
			log.error("Error while refeshing catalog cache in method navigate", exception);
		}
		return SUCCESS;
	}
	
	private boolean determineCatalogLandingRedirection(List<Breadcrumb> bcl,String[] pathDepth) {
		boolean isCategoryDomain = false;	
		if (null == bcl || bcl.size() == 1) {
			isCategoryDomain = true;
		} else if (bcl.size() > 1) {
			if (YFCCommon.isVoid(pathDepth) || pathDepth.length <= 2) {
				isCategoryDomain = true;
			}
		}
		if(isCategoryC3()== true)
		{
			isCategoryDomain=false;
		}
		return isCategoryDomain;
	}

	protected void setItemsUomsMap() {
		try {
			ArrayList<Element> itemList = getXMLUtils().getElements(getOutDoc().getDocumentElement(), "//ItemList/Item");
			if(itemList !=  null) {
				for(int i=0; i<itemList.size();i++) {
					String itemId = itemList.get(i).getAttribute("ItemID");
					if(i == 0) 
					{
						// Added For XBT-253
						firstItem = itemId;
						 Element catagoryElement=(Element)itemList.get(i).getElementsByTagName("Category").item(0);
                        if(catagoryElement != null)
						{
						 String  catDescription=catagoryElement.getAttribute("ShortDescription");

                         tempCategoryPath=catagoryElement.getAttribute("CategoryPath");                         

                         if(tempCategoryPath !=null)
                         {
                          String [] descriptions=tempCategoryPath.split("/");
                          if(descriptions != null)
                         {
                          if(descriptions.length ==4)
                         {
                          firstItemCategoryShortDescription=catDescription;
                         }        
                        }
                       }
			          }

			        }
					if(itemIDList.contains(itemId))
						continue ;
					itemIDList.add(itemId);
				}
			}			
		}
		catch (Exception e) {
			log.error(e.getMessage());
			// TODO: handle exception
		}
		if(itemIDList.size()>0) {
			//get the map from the session. For Minicart Jira - 3481
			HashMap<String,String> itemMapObj = (HashMap<String, String>) XPEDXWCUtils.getObjectFromCache("itemMap");
			//New method for getting order multiple .
			//setInventoryAndOrderMultipleMap();
			//commented for EB-164
			//itemUomHashMap =XPEDXOrderUtils.getXpedxUOMList(wcContext.getCustomerId(), itemIDList, wcContext.getStorefrontId());
			//orderMultipleMap = new HashMap<String,String>();
			
			XPEDXCustomerContactInfoBean xpedxCustomerContactInfoBean = (XPEDXCustomerContactInfoBean)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.XPEDX_Customer_Contact_Info_Bean);
			if(xpedxCustomerContactInfoBean!=null && xpedxCustomerContactInfoBean.getMsapExtnUseOrderMulUOMFlag()!=null && xpedxCustomerContactInfoBean.getMsapExtnUseOrderMulUOMFlag()!=""){
			msapOrderMultipleFlag = xpedxCustomerContactInfoBean.getMsapExtnUseOrderMulUOMFlag();	
			}
			
			wcContext.setWCAttribute("orderMultipleMap",orderMultipleMap, WCAttributeScope.REQUEST);
			itemUomHashMap = XPEDXOrderUtils.getXpedxUOMDescList(
					wcContext.getCustomerId(), itemIDList,
					wcContext.getStorefrontId(),true);
			defaultShowUOMMap = new HashMap<String,String>();
			defaultShowUOMMap = XPEDXOrderUtils.getDefaultShowUOMMap();
			//Set itemMap MAP again in session
			XPEDXWCUtils.setObectInCache("itemMap",itemMapObj);
			//set a itemsUOMMap in Session for ConvFactor
			XPEDXWCUtils.setObectInCache("itemsUOMMap",XPEDXOrderUtils.getItemUomHashMap());	
			
			wcContext.setWCAttribute("itemUomHashMap", itemUomHashMap, WCAttributeScope.REQUEST);
			wcContext.setWCAttribute("defaultShowUOMMap", defaultShowUOMMap, WCAttributeScope.REQUEST);
			
		}	
			
		/*	itemUomHashMap =XPEDXOrderUtils.getXpedxUOMList(wcContext.getCustomerId(), itemIDList, wcContext.getStorefrontId());
			//orderMultipleMap = new HashMap<String,String>();
			
			//Start - Code added to fix XNGTP 2964
			XPEDXCustomerContactInfoBean xpedxCustomerContactInfoBean = (XPEDXCustomerContactInfoBean)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.XPEDX_Customer_Contact_Info_Bean);
			if(xpedxCustomerContactInfoBean!=null && xpedxCustomerContactInfoBean.getMsapExtnUseOrderMulUOMFlag()!=null && xpedxCustomerContactInfoBean.getMsapExtnUseOrderMulUOMFlag()!=""){
			msapOrderMultipleFlag = xpedxCustomerContactInfoBean.getMsapExtnUseOrderMulUOMFlag();	
			}
			wcContext.setWCAttribute("orderMultipleMap",orderMultipleMap, WCAttributeScope.REQUEST);
		
			
			double minFractUOM = 0.00;
	    	double maxFractUOM = 0.00;
	    	String lowestUOM = "";
	    	String highestUOM = "";
	    	String minUOMsDesc = "";
	    	String maxUOMsDesc = "";
	    	String defaultConvUOM = "";
			String defaultUOM = "";
			String orderMultiple = "";
	    	
			//End - Code added to fix XNGTP 2964
	    	defaultShowUOMMap = new HashMap<String,String>();
			if(itemUomHashMap!=null && itemUomHashMap.size()>0) {
				for(int i=0; i<itemIDList.size(); i++) {
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
			    	
					for (Iterator it = displayUomMap.keySet().iterator(); it.hasNext();) {
						try {
							String uom = (String) it.next();
							Object objConversionFactor = displayUomMap.get(uom);
							//Start- Code added to fix XNGTP 2964
							orderMultiple = orderMultipleMap.get(strItemID);
							if("Y".equals(msapOrderMultipleFlag) && Integer.valueOf(orderMultiple) > 1 && !"1".equals(objConversionFactor)){
								//orderMultiple = "12";
								if(objConversionFactor.toString() == orderMultiple){
									minFractUOM = 1;
									lowestUOM = uom;
									minUOMsDesc =  XPEDXWCUtils.getUOMDescription(lowestUOM)+ " (" + Math.round(Double.parseDouble((String)objConversionFactor)) + ")";
									
								}else {
									double conversion = getConversion(objConversionFactor, orderMultiple);
									if (conversion != -1 && uom != null
											&& uom.length() > 0) {
										if(conversion <= 1 && conversion >= minFractUOM){
											minFractUOM = conversion;
											lowestUOM = uom;
											minUOMsDesc =  XPEDXWCUtils.getUOMDescription(lowestUOM)+ " (" + Math.round(Double.parseDouble((String)objConversionFactor)) + ")";
											
											
										}else if(conversion>1 && ( conversion < maxFractUOM || maxFractUOM == 0)){
											maxFractUOM = conversion;
											highestUOM = uom;
											maxUOMsDesc =  XPEDXWCUtils.getUOMDescription(highestUOM)+ " (" + Math.round(Double.parseDouble((String)objConversionFactor)) + ")";
											
										
										}
									}
								}
								//End - Code added to fix XNGTP 2964								
								
							}		
							if("1".equals(objConversionFactor))
							{
								displayUomMap.put(uom,XPEDXWCUtils.getUOMDescription(uom));
							}
							else{
								if(null != objConversionFactor && !"".equals(objConversionFactor)){//JIRA 1391 - Displaying an Integer instead of a decimal.
									displayUomMap.put(uom,XPEDXWCUtils.getUOMDescription(uom)+ " (" + Math.round(Double.parseDouble((String)objConversionFactor)) + ")");
								}
							}
						} catch (Exception e) {
							log.error("Error while getting the UOM Description.....");
							e.printStackTrace();
						}
					}
						//Start- Code added to fix XNGTP 2964
						if(minFractUOM == 1.0 && minFractUOM != 0.0){
							defaultConvUOM = lowestUOM;
							defaultUOM = minUOMsDesc;
							
						}else if(maxFractUOM > 1.0){
							defaultConvUOM = highestUOM;
							defaultUOM = maxUOMsDesc;
							
						}else{
							
							defaultConvUOM = lowestUOM;
							defaultUOM = minUOMsDesc;
						}
						/*if(SCUtil.isVoid(orderMultiple) || Integer.valueOf(orderMultiple) == 0){
							orderMultiple = "1";
						}
						orderMultipleMap.put(strItemID, orderMultiple);
						*/
						/*if(!SCUtil.isVoid(orderMultiple) && Integer.valueOf(orderMultiple)>1){
							orderMultipleMap.put(strItemID, orderMultiple);
						}*/
					/*	if(!SCUtil.isVoid(defaultUOM))
							defaultShowUOMMap.put(strItemID, defaultUOM);
						//End- Code added to fix XNGTP 2964
						
						itemUomHashMap.put(strItemID, displayUomMap);
						if(itemMapObj !=null )
						{
							itemMapObj.put(strItemID, orderMultiple);
						}
							
				}
			}
			//Set itemMap MAP again in session
			XPEDXWCUtils.setObectInCache("itemMap",itemMapObj);
			//set a itemsUOMMap in Session for ConvFactor
			XPEDXWCUtils.setObectInCache("itemsUOMMap",XPEDXOrderUtils.getXpedxUOMList(wcContext.getCustomerId(), itemIDList, wcContext.getStorefrontId()));	
		}
		wcContext.setWCAttribute("itemUomHashMap", itemUomHashMap, WCAttributeScope.REQUEST);
		wcContext.setWCAttribute("defaultShowUOMMap", defaultShowUOMMap, WCAttributeScope.REQUEST);*/
	}
	private void getOrderMultipleMapForItems()
	{
		ArrayList<Element> xpxItemExtnList=SCXmlUtil.getElements(allAPIOutputDoc, "XPXItemExtnList/XPXItemExtn");
		if(xpxItemExtnList != null)
		{
			for(Element xpxItemExtn : xpxItemExtnList)
			{
				if(xpxItemExtn != null)
				{
					orderMultipleMap.put(xpxItemExtn.getAttribute("ItemID"), xpxItemExtn.getAttribute("OrderMultiple"));
				}
			}
		}
	}
	private Map<String, Map<String,String>> getXpedxUOMList() {
		
		LinkedHashMap<String, Map<String,String>> itemUomHashMap = new LinkedHashMap<String, Map<String,String>>();
		
		
		IWCContext context = WCContextHelper.getWCContext(ServletActionContext
				.getRequest());
		SCUIContext wSCUIContext = context.getSCUIContext();
		ISCUITransactionContext scuiTransactionContext = wSCUIContext
				.getTransactionContext(true);
		YFSEnvironment env = (YFSEnvironment) scuiTransactionContext
				.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);
		try {
			
			if(allAPIOutputDoc != null)
			{
			Element wElement = (Element)allAPIOutputDoc.getElementsByTagName("ItemList").item(0);
			NodeList wNodeList = wElement.getChildNodes();
			if (wNodeList != null) {
				int length = wNodeList.getLength();
				String conversion;
				for (int i = 0; i < length; i++) {
					Node wNode = wNodeList.item(i);
					if (wNode != null) {
						NamedNodeMap nodeAttributes = wNode.getAttributes();
						if (nodeAttributes != null) {
							Node itemId = nodeAttributes
									.getNamedItem("ItemID");
							if(itemId!=null) {
								LinkedHashMap<String, String> wUOMsAndConFactors = new LinkedHashMap<String, String>();
								NodeList uomListNodeList =	wNode.getChildNodes();
								Node uomListNode = uomListNodeList.item(0);
								
								//2964 Start
								if (uomListNode != null) {
									List<Element> listOfUOMElements = SCXmlUtil.getChildrenList((Element) uomListNode);
								Collections.sort(listOfUOMElements, new XpedxSortUOMListByConvFactor());
									
									for (Element uomNode : listOfUOMElements) {
									
										if (uomNode != null) {
											NamedNodeMap uomAttributes = uomNode.getAttributes();
											//2964 end
											if (uomAttributes != null) {
												Node UnitOfMeasure = uomAttributes
														.getNamedItem("UnitOfMeasure");
												Node Conversion = uomAttributes
														.getNamedItem("Conversion");
												if (UnitOfMeasure != null && Conversion != null) {
													conversion = Conversion.getTextContent();
													if(!YFCUtils.isVoid(conversion)){
														long convFactor = Math.round(Double.parseDouble(conversion));
															wUOMsAndConFactors.put(UnitOfMeasure
																.getTextContent(), Long.toString(convFactor));
															
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
			ArrayList<Element> listofWishLists = getXMLUtils().getElements(
					outputEl, "XPEDXMyItemsList");
			if (listofWishLists != null) {
				for (Element list : listofWishLists) {
					itemListMap.put(list.getAttribute("MyItemsListKey"), list
							.getAttribute("Name"));
				}
			}
		} catch (Exception e) {
			log
					.error("Unable to get MyItemList list from DB. The list will be empty. "
							+ e);
		}
	}

	public String setCategoryDomainAssetList() {
		String categoryDomainDisplay = "CatalogLanding";
		categoryAssetMap = new HashMap<String, ArrayList<XPEDXCatalogCategoryImageBean>>();
		Document catDoc = getOutDoc();
		String imageURL = "";
		String childCategoryName = "";
		String childCategoryPath = "";
		Element catListElement = SCXmlUtil.getChildElement(catDoc
				.getDocumentElement(), "CategoryList");
		ArrayList<Element> categoryList = SCXmlUtil.getChildren(catListElement,
				"Category");
		Iterator<Element> catIter = categoryList.iterator();
		while (catIter.hasNext()) {

			Element topLevelCategory = catIter.next();
			String topCategoryShortDesc = SCXmlUtil.getAttribute(
					topLevelCategory, "ShortDescription");

			if (getCategoryDepth().equals("1")) {
				// for c1 categories-> show all the sub categories of C1 in the
				// landing page
				String topCategoryPath = topLevelCategory
						.getAttribute("CategoryPath");
				Element assetElem = SCXmlUtils.getElementByAttribute(
						topLevelCategory, "/AssetList/Asset", "Type",
						CATEGORY_IMAGE_ASSET_TYPE);
				if (null != assetElem) {
					String contentLocation = assetElem.getAttribute("ContentLocation");
					String contentId = assetElem.getAttribute("ContentID");
					if(contentLocation!= null && contentId!=null && contentLocation!="" && contentId!="") {
						imageURL = contentLocation + "/" + contentId;
					} else {
						imageURL = "/swc/xpedx/images/INF_150x150.jpg";
					}
				} else {
					// no category images defined
					log.error("no category image defined for "
							+ topCategoryShortDesc);
					imageURL = "/swc/xpedx/images/INF_150x150.jpg";
				}
				XPEDXCatalogCategoryImageBean bean = new XPEDXCatalogCategoryImageBean(
						topCategoryShortDesc, topCategoryPath, imageURL);
				ArrayList<XPEDXCatalogCategoryImageBean> topBeanList = new ArrayList<XPEDXCatalogCategoryImageBean>();
				topBeanList.add(bean);
				categoryAssetMap.put(topCategoryShortDesc, topBeanList);

			} else {
				// for Home Catalog Landing
				Element childCatListElement = SCXmlUtil.getChildElement(
						topLevelCategory, "ChildCategoryList");
				ArrayList<Element> childCategoryList = SCXmlUtil.getChildren(
						childCatListElement, "Category");
				ArrayList<XPEDXCatalogCategoryImageBean> childBeanList = new ArrayList<XPEDXCatalogCategoryImageBean>();
				Iterator<Element> childCatIter = childCategoryList.iterator();
				while (childCatIter.hasNext()) {
					Element childCategory = childCatIter.next();
					childCategoryName = childCategory
							.getAttribute("ShortDescription");
					childCategoryPath = childCategory
							.getAttribute("CategoryPath");
					Element assetElem = SCXmlUtils.getElementByAttribute(
							childCategory, "/AssetList/Asset", "Type",
							CATEGORY_IMAGE_ASSET_TYPE);
					if (null != assetElem) {
						String contentLocation = assetElem.getAttribute("ContentLocation");
						String contentId = assetElem.getAttribute("ContentID");
						if(contentLocation!= null && contentId!=null && contentLocation!="" && contentId!="") {
							imageURL = contentLocation + "/" + contentId;
						} else {
							imageURL = "/swc/xpedx/images/INF_150x150.jpg";
						}
					} else {
						// no category images defined
						log.error("no category image defined for "
								+ childCategoryName);
						imageURL = "/swc/xpedx/images/INF_150x150.jpg";
					}
					XPEDXCatalogCategoryImageBean bean = new XPEDXCatalogCategoryImageBean(
							childCategoryName, childCategoryPath, imageURL);
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
		long startTime=System.currentTimeMillis();
		StringBuffer sb=new StringBuffer();		
		
		if(("").equals(super.sortField) && (super.session.get("sortField") == null || ("").equals(super.session.get("sortField")))) {						
			orderByAttribute = "Item.ExtnBestMatch";
			sortField = "Item.ExtnBestMatch--A";
		}
		
		String returnString = super.search();
		long endTime=System.currentTimeMillis();
		long timespent=(endTime-startTime);
		sb.append("OOTB execution time on catlaog Search() = "+timespent);
		//getting the customer bean object from session.
		//XBT-260
		changeBasis();
		shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		
				/***** Start of  Code changed for Promotions Jira 2599 ********/ 
		/*List<Breadcrumb> bcl = BreadcrumbHelper.preprocessBreadcrumb(this
				.get_bcs_());
		Breadcrumb lastBc = bcl.get(bcl.size() - 1);    
		Map<String, String> params = lastBc.getParams();
		String[] pathDepth = StringUtils.split(path, "/");
		path = params.get("path");       
   
		if (bcl.size() > 1 || (!("true".equals(displayAllCategories)))) {
			if (!YFCCommon.isVoid(pathDepth) && pathDepth.length == 2) {
				// for c1 categories-> show all the sub categories of C1 in the
				// landing page
				setCategoryDepth("1");// default value is 2; coming from struts
				// file
			}
		} */

		/****End of Code Changed for Promotions JIra 2599 *******/
		//Added for performance of filterAction
		/*Document catDoc = getOutDoc();
		if(catDoc!=null){
			NodeList itemList = catDoc.getElementsByTagName("ItemList"); 
			if(itemList != null) {
				for(int i=0;i<itemList.getLength();i++)
				{
					Element _categoryElem=(Element)itemList.item(i);								
					path=SCXmlUtil.getXpathAttribute(_categoryElem, "//ItemList/Item/CategoryList/Category/@CategoryPath");
				}
			}
		}*/
		// end of performance filterAction
		/*Start of webtrend tags*/
		setsearchMetaTag(true);
		/*End of webtrend tags*/

		if (ERROR.equals(returnString)) {
			return returnString;
		} else if (isSingleItem()) {
			YFCNode yfcNode = YFCDocument.getDocumentFor(getOutDoc())
					.getDocumentElement().getChildElement("ItemList")
					.getFirstChild();
			setItemID(yfcNode.getAttributes().get("ItemID"));
			setUnitOfMeasure(yfcNode.getAttributes().get("UnitOfMeasure"));
			return "singleItem";
		}
		else {
			try
			{
				
				long startTimeCustomerService=System.currentTimeMillis(); 
				getAllAPIOutput();
				endTime=System.currentTimeMillis();
				timespent=(endTime-startTimeCustomerService);
				sb.append("\nCustom Service execution time on catlaog Search() = "+timespent);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			setItemsUomsMap();
			setAttributeListForUI();
			prepareItemBranchInfoBean();
			setColumnListForUI();
			
			/**start of code for JIra 2599****/
			if((path==null||path.equals("/")) && getFirstItem().trim()!=""){
				
				YFCNode yfcNode = YFCDocument.getDocumentFor(getOutDoc())
				.getDocumentElement().getChildElement("ItemList")
				.getFirstChild();
				path = yfcNode.getAttributes().get("CategoryPath");
			//	path=XPEDXWCUtils.getCategoryPathPromo(getFirstItem(), wcContext.getStorefrontId());
				
				
			}
			/**End of code for JIra 2599****/
	}
		//Webrtends	tag start
		setStockedItemFromSession();				
		if(isStockedItem){
		 setsearchMetaTag(true);
		}
		getCatTwoDescFromItemIdForpath(getOutDoc().getDocumentElement(),path);
		timespent=(endTime-startTime);
		sb.append("\nTotal Action time  execution on catlaog Search() = "+timespent);
		System.out.println(sb.toString());
		//Webrtends	tag End
		return SUCCESS;
	}

	@Override
	public String sortResultBy() {
		StringBuffer sb=new StringBuffer();
		long startTime=System.currentTimeMillis();
		init();
				
		if(("").equals(super.sortField) && (super.session.get("sortField") == null || ("").equals(super.session.get("sortField")))) {						
			orderByAttribute = "Item.ExtnBestMatch";
			sortField = "Item.ExtnBestMatch--A";
		}
		
		String returnString = super.sortResultBy();
		long endTime=System.currentTimeMillis();
		long timespent=(endTime-startTime);

		sb.append("OOTB execution time on catlaog sortResultBy() = "+timespent);

		//XBT-260
		changeBasis();	
		if (ERROR.equals(returnString)) {
			return returnString;
		} else {

			//Added for performance of sortResultByAction
			Document catDoc = getOutDoc();
			if(catDoc!=null){
				NodeList itemList = catDoc.getElementsByTagName("ItemList"); 
				if(itemList != null) {
					for(int i=0;i<itemList.getLength();i++)
					{
						Element _categoryElem=(Element)itemList.item(i);								
						path=SCXmlUtil.getXpathAttribute(_categoryElem, "//ItemList/Item/CategoryList/Category/@CategoryPath");
					}
				}
			}
			// end of performance sortResultByAction
			try
			{
				long startTimeCustomerService=System.currentTimeMillis(); 
				getAllAPIOutput();
				endTime=System.currentTimeMillis();
				timespent=(endTime-startTimeCustomerService);
				sb.append("\nCustom Service execution time on catlaog sortResultBy() = "+timespent);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			setItemsUomsMap();
			setAttributeListForUI();
			prepareItemBranchInfoBean();
			setColumnListForUI();
			getSortFieldDocument();
		}
		timespent=(endTime-startTime);
		sb.append("\nTotal Action time  execution on catlaog sortResultBy() = "+timespent);
		System.out.println(sb.toString());
		return SUCCESS;

	}

	@Override
	public String goToPage() {
		StringBuffer sb=new StringBuffer();
		long startTime=System.currentTimeMillis();
		init();
		
		/*Map<String, String> topCategoryMap = (Map<String, String>)XPEDXWCUtils.getObjectFromCache("TopCategoryMap");
		List<Breadcrumb> bcl = BreadcrumbHelper.preprocessBreadcrumb(this
				.get_bcs_());
		if (topCategoryMap == null) {						
			for (int i = 0; i < bcl.size(); i++) {
				Breadcrumb bc = bcl.get(i);
				Map<String, String> bcParams = bc.getParams();			
				String cnameValue = bcParams.get("cname");
				if (cnameValue != null && cnameValue.equals("Paper") && i <=2) {
					orderByAttribute = "Item.ExtnBasis";
					break;
				}				
			}
			
		} else {
			Breadcrumb bc = bcl.get(bcl.size()-2);
			Map<String, String> bcParams = bc.getParams();						
			String pathValue = bcParams.get("path");
			String[] pathDepth = StringUtils.split(pathValue, "/");
			if (pathDepth != null) {
				if (pathDepth.length > 1) {
					String categoryId = pathDepth[1];
					String catDescription = topCategoryMap.get(categoryId);
					if (catDescription !=null && catDescription.equals("Paper")) {
						orderByAttribute = "Item.ExtnBasis";
					}
				} else {					
					for (int i = 0; i < bcl.size(); i++) {
						Breadcrumb bcrum = bcl.get(i);
						Map<String, String> bcParameter = bcrum.getParams();
						if(bcParameter != null )
						{
							String cnameVal = bcParameter.get("cname");
							if (cnameVal != null && cnameVal.equals("Paper") && i <=2) {
								orderByAttribute = "Item.ExtnBasis";
								break;
							}
						}			
					}					
				}				
			}
			
		} */
		
		if(("").equals(super.sortField) && (super.session.get("sortField") == null || ("").equals(super.session.get("sortField")))) {						
			orderByAttribute = "Item.ExtnBestMatch";
			sortField = "Item.ExtnBestMatch--A";
		}
		
		String returnString = super.goToPage();
		long endTime=System.currentTimeMillis();
		long timespent=(endTime-startTime);

		sb.append("OOTB execution time on catlaog goToPage() = "+timespent);
		if (ERROR.equals(returnString)) {
			return returnString;
		} else {
			//Added for performance of filterAction
			Document catDoc = getOutDoc();
			if(catDoc!=null){
				NodeList itemList = catDoc.getElementsByTagName("ItemList"); 
				if(itemList != null) {
					for(int i=0;i<itemList.getLength();i++)
					{
						Element _categoryElem=(Element)itemList.item(i);								
						path=SCXmlUtil.getXpathAttribute(_categoryElem, "//ItemList/Item/CategoryList/Category/@CategoryPath");
					}
				}
			}
			// end of performance filterAction
			//XBT-260
			changeBasis();
			try
			{
				long startTimeCustomerService=System.currentTimeMillis(); 
				getAllAPIOutput();
				endTime=System.currentTimeMillis();
				timespent=(endTime-startTimeCustomerService);
				sb.append("\nCustom Service execution time on catlaog goToPage() = "+timespent);
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			setItemsUomsMap();
			setAttributeListForUI();
			prepareItemBranchInfoBean();
			setColumnListForUI();
		}
		timespent=(endTime-startTime);
		sb.append("\nTotal Action time  execution on catlaog goToPage() = "+timespent);
		System.out.println(sb.toString());
		return SUCCESS;
	}

	@Override
	public String selectPageSize() {
		StringBuffer sb=new StringBuffer();
		long startTime=System.currentTimeMillis();
		init();
		
		/*Map<String, String> topCategoryMap = (Map<String, String>)XPEDXWCUtils.getObjectFromCache("TopCategoryMap");
		List<Breadcrumb> bcl = BreadcrumbHelper.preprocessBreadcrumb(this
				.get_bcs_());
		if (topCategoryMap == null) {						
			for (int i = 0; i < bcl.size(); i++) {
				Breadcrumb bc = bcl.get(i);
				Map<String, String> bcParams = bc.getParams();			
				String cnameValue = bcParams.get("cname");
				if (cnameValue != null && cnameValue.equals("Paper") && i <=2) {
					orderByAttribute = "Item.ExtnBasis";
					break;
				}				
			}
			
		} else {
			Breadcrumb bc = bcl.get(bcl.size()-2);
			Map<String, String> bcParams = bc.getParams();						
			String pathValue = bcParams.get("path");
			String[] pathDepth = StringUtils.split(pathValue, "/");
			if (pathDepth != null) {
				if (pathDepth.length > 1) {
					String categoryId = pathDepth[1];
					String catDescription = topCategoryMap.get(categoryId);
					if (catDescription !=null && catDescription.equals("Paper")) {
						orderByAttribute = "Item.ExtnBasis";
					}
				} else {					
					for (int i = 0; i < bcl.size(); i++) {
						Breadcrumb bcrum = bcl.get(i);
						Map<String, String> bcParameter = bcrum.getParams();
						if(bcParameter != null )
						{
							String cnameVal = bcParameter.get("cname");
							if (cnameVal != null && cnameVal.equals("Paper") && i <=2) {
								orderByAttribute = "Item.ExtnBasis";
								break;
							}
						}			
					}					
				}
			}			
		} */
		
		if(("").equals(super.sortField) && (super.session.get("sortField") == null || ("").equals(super.session.get("sortField")))) {						
			orderByAttribute = "Item.ExtnBestMatch";
			sortField = "Item.ExtnBestMatch--A";
		}
		
		String returnString = super.selectPageSize();
		long endTime=System.currentTimeMillis();
		long timespent=(endTime-startTime);
		sb.append("OOTB execution time on catlaog selectPageSize() = "+timespent);
		//XBT-260
		changeBasis();	
		wcContext.getSCUIContext().getSession().setAttribute(
				"selectedPageSize", pageSize);
		if (ERROR.equals(returnString)) {
			return returnString;
		} else {
			//Added for performance of filterAction
			Document catDoc = getOutDoc();
			if(catDoc!=null){
				NodeList itemList = catDoc.getElementsByTagName("ItemList"); 
				if(itemList != null) {
					for(int i=0;i<itemList.getLength();i++)
					{
						Element _categoryElem=(Element)itemList.item(i);								
						path=SCXmlUtil.getXpathAttribute(_categoryElem, "//ItemList/Item/CategoryList/Category/@CategoryPath");
					}
				}
			}
			try
			{
				long startTimeCustomerService=System.currentTimeMillis(); 
				getAllAPIOutput();
				endTime=System.currentTimeMillis();
				timespent=(endTime-startTimeCustomerService);
				sb.append("\nCustom Service execution time on catlaog selectPageSize() = "+timespent);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			// end of performance sortResultByAction
			setItemsUomsMap();
			setAttributeListForUI();
			prepareItemBranchInfoBean();
			setColumnListForUI();
		}
		timespent=(endTime-startTime);
		sb.append("\nTotal Action time  execution on catlaog selectPageSize() = "+timespent);
		System.out.println(sb.toString());
		return SUCCESS;
	}
	
	public ArrayList<String> getListOfColumns(String layOutType)
			throws CannotBuildInputException {
		ArrayList<String> columnList = new ArrayList<String>();
		Document outputDoc = null;

		if (YFCCommon.isVoid(layOutType)) {
			LOG
					.error("getListOfColumns: layOutType is a required field. Returning a empty list...");
			return columnList;
		}
		
		templateName = "tmp_" + layOutType.replaceAll("[^a-zA-Z]", "");
		
		// Checking if the layout is set into cache. If its set then getting the details from the cache and returning the ColumnList
		String ccLongDescriptionFromCache = (String)XPEDXWCUtils.getObjectFromCache(layOutType);
		if(!(YFCCommon.isStringVoid(ccLongDescriptionFromCache))) {
			String[] columnNamesArrayFromCache = ccLongDescriptionFromCache.split(",");
			columnList = new ArrayList<String>(Arrays.asList(columnNamesArrayFromCache));
			return columnList;
		}
		//If it is not in cache then call the common code mashup,fetch the column list for the layout type, put it in session and return it. 
		
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/CommonCode/@CodeValue", layOutType);

		Element input = WCMashupHelper.getMashupInput(
				"xpedxGetCustomCommonCodesForCatBrowse", valueMap, wcContext
						.getSCUIContext());
		String inputXml = SCXmlUtil.getString(input);
		LOG.debug("getListOfColumns: Input XML: " + inputXml);
		Object obj = WCMashupHelper.invokeMashup(
				"xpedxGetCustomCommonCodesForCatBrowse", input, wcContext
						.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		if (null != outputDoc) {
			LOG.debug("getListOfColumns: Output XML: "
					+ SCXmlUtil.getString((Element) obj));
		}
		// get the common code node which has the CodeValue=layOutType. It
		// should return only one Element
		ArrayList<Element> commonCodeList = SCXmlUtils.getElementsByAttribute(
				outputDoc.getDocumentElement(), "CommonCode", "CodeValue",
				layOutType);
		if (null != commonCodeList && commonCodeList.size() == 1) {
			Element commonCodeElement = commonCodeList.get(0);
			// Column names are stored in the long description of a CommonCode
			String ccLongDescription = commonCodeElement
					.getAttribute("CodeLongDescription");
			if (YFCCommon.isVoid(ccLongDescription)) {
				LOG
						.error("getListOfColumns: Not data in the CommonCodes for the given layoutType.Returning a empty columnList...LayoutType: "
								+ layOutType);
				return columnList;
			}
			//setting the layout into the context for future use.
			XPEDXWCUtils.setObectInCache(layOutType, ccLongDescription);
			// Column names are stored seperated by Commas(,)
			String[] columnNamesArray = ccLongDescription.split(",");
			columnList = new ArrayList<String>(Arrays.asList(columnNamesArray));
		} else {
			// Either zero rows or more than one rows. Return a empty columnList
			LOG
					.error("getListOfColumns: Not able to determine the Layout code. Returning a empty columnList");
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
			itemDoc = XPEDXOrderUtils.getItemDetails(pnaItemId, wcContext
					.getCustomerId(), wcContext.getStorefrontId(), wcContext);
			log.debug("Item Details complete");
			// prepare the MyItemList list
			prepareMyItemListList();
			log.debug("MyItemList list prepared");
			Element itemEle = SCXmlUtil.getChildElement(itemDoc
					.getDocumentElement(), "Item");
			requestedUOM = SCXmlUtil.getAttribute(itemEle, "UnitOfMeasure");
			log.debug("Requested UOM: " + requestedUOM);
			ArrayList<XPEDXItem> inputItems = getPnAInputDoc(getPnaItemId(),
					requestedUOM);
			XPEDXPriceAndAvailability pna = XPEDXPriceandAvailabilityUtil
					.getPriceAndAvailability(inputItems);
			
			//This takes care of displaying message to Users based on ServiceDown, Transmission Error, HeaderLevelError, LineItemError 
			ajaxDisplayStatusCodeMsg  =   XPEDXPriceandAvailabilityUtil.getAjaxDisplayStatusCodeMsg(pna) ;
			setAjaxLineStatusCodeMsg(ajaxDisplayStatusCodeMsg);
		
			// Check for Success/Failure and Error Conditions
			if (null == pna || pna.getTransactionStatus().equalsIgnoreCase("F")) {
				/*
				 * If the PnA is failure, set the error msg and send success
				 * back to the UI.No matter what the reply is, we are going to
				 * display the modal with the error messages*incase of PnA
				 * failure, the pricing and Availability information will not be
				 * available to the user,*but they can still add the item to the
				 * cart.
				 */
				log.error(ajaxDisplayStatusCodeMsg + "PnA failed(TransactionStatus Error) for ItemID: "
						+ pnaItemId);
			//	setAjaxLineStatusCodeMsg(getAjaxLineStatusCodeMsg()
			//			+ "Error getting pricing detail: Transaction Failed\n");
			} else if (!pna.getHeaderStatusCode().equalsIgnoreCase("00")) {
				log.error( ajaxDisplayStatusCodeMsg + "-P7-PnA failed(HeaderStatusCode Error) for ItemID: "
						+ pnaItemId);
				//setAjaxLineStatusCodeMsg(getAjaxLineStatusCodeMsg()
					//	+ "Error getting pricing detail: HeaderStatusCode Error.\n");
			}

			if (null == pna || null == pna.getItems()) {
				preparePnAPricingInfo(itemDoc, new Vector<XPEDXItem>(),
						pnaItemId, requestedUOM);
			} else {
				Vector<XPEDXItem> items = pna.getItems();
				// prepare the information for JSP
				setAjaxPnAJson(items, pnaItemId);
				preparePnAPricingInfo(itemDoc, items, pnaItemId, requestedUOM);
				
				for (XPEDXItem pandAItem : items) {
					if (pandAItem.getLegacyProductCode().equals(pnaItemId)) {
						// set the line status erros mesages if any
						String lineStatusErrorMsg = XPEDXPriceandAvailabilityUtil
								.getPnALineErrorMessage(pandAItem);
						
						ajaxDisplayStatusCodeMsg =  ajaxDisplayStatusCodeMsg + "  " + lineStatusErrorMsg;
						
						if (!YFCCommon.isVoid(lineStatusErrorMsg)) {							
							setAjaxLineStatusCodeMsg(ajaxDisplayStatusCodeMsg);
						}
					}
				}
			}

		} catch (Exception e) {
			log.error("Error Getting Item Details and PnA Details for"
					+ pnaItemId, e);
			return this.ERROR;
		}
		return this.SUCCESS;
	}

	private void preparePnAPricingInfo(Document itemDoc,
			Vector<XPEDXItem> items, String itemId, String requestedUOM)
			throws Exception {
		Element itemEle = SCXmlUtil.getChildElement(itemDoc
				.getDocumentElement(), "Item");
		Element primaryInfoEle = SCXmlUtil.getChildElement(itemEle,
				"PrimaryInformation");
		ItemDescription = SCXmlUtils
				.getAttribute(primaryInfoEle, "Description").toString();
		ItemExtendedDescription = SCXmlUtils.getAttribute(itemEle, "ItemID")
				.toString();
		ItemShortDescription = SCXmlUtils.getAttribute(primaryInfoEle,
				"ShortDescription").toString();
		SCXmlUtil.getString(itemEle);
		Element itemExtnEle = SCXmlUtil.getChildElement(itemEle, "Extn");
		String minOrderQty = SCXmlUtils.getAttribute(primaryInfoEle,
				"MinOrderQuantity");
		String pricingUOM = SCXmlUtils.getAttribute(primaryInfoEle,
				"PricingUOM");
		String pricingUOMConvFactor = SCXmlUtils.getAttribute(primaryInfoEle,
				"PricingQuantityConvFactor");
		String baseUOM = SCXmlUtils.getAttribute(itemEle, "UnitOfMeasure");
		String prodMweight = SCXmlUtils.getAttribute(itemExtnEle, "ExtnMwt");
		List<String> displayUOMs = new ArrayList();
		getItemUOMs(itemId);
		// List<String> displayPriceForUoms = new ArrayList();
		// List<String> bracketsPricingList = null;
		for (XPEDXItem pandAItem : items) {
			if (pandAItem.getLegacyProductCode().equals(itemId)) {
				if (pandAItem.getPriceCurrencyCode() != null
						&& pandAItem.getPriceCurrencyCode().trim().length() > 0) {
					setPriceCurrencyCode(pandAItem.getPriceCurrencyCode());
				}
				String pricingUOMUnitPrice = pandAItem
						.getUnitPricePerPricingUOM();
				BigDecimal pricingUOMPrice = new BigDecimal(pricingUOMUnitPrice);
				BigDecimal prodWeight = null;
				BigDecimal priceForCWTUom = null;
				BigDecimal priceForTHUom = null;
//				displayUOMs.add(baseUOM); //removed as specified in the bug 185 comments on 03/Jan/11 3:58 PM by Barb Widmer
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
					priceForCWTUom = pricingUOMPrice.divide(prodWeight
							.divide(new BigDecimal(100)));
				}
				if ("CWT".equalsIgnoreCase(pricingUOM)) {
					displayUOMs.add("TH");
					if (prodMweight != null && prodMweight.trim().length() > 0)
						prodWeight = new BigDecimal(prodMweight);
					else
						prodWeight = new BigDecimal(100); // this will make
					// pricing for CW
					// priceForCWTUom =
					// pricingUOMPrice.divide(prodWeight.divide(new
					// BigDecimal(100)));
					priceForTHUom = pricingUOMPrice.multiply(prodWeight
							.divide(new BigDecimal(100)));
				}
				displayUOMs.add(requestedUOM);
				if (YFCCommon.isVoid(pricingUOMConvFactor)) {
					pricingUOMConvFactor = "1";
				}

				if (pricingUOMConvFactor != null
						&& ((new BigDecimal(0)).compareTo(new BigDecimal(
								pricingUOMConvFactor)) <= 0)) {
					pricingUOMConvFactor = "1";
				}
				BigDecimal basePrice = pricingUOMPrice.divide(new BigDecimal(
						pricingUOMConvFactor));

				String BaseUomDesc = null;
				String RequestedQtyUOMDesc = null;
				String PricingUOMDesc = null;
				try {
					BaseUomDesc = XPEDXWCUtils.getUOMDescription(baseUOM);
					RequestedQtyUOMDesc = XPEDXWCUtils
							.getUOMDescription(pandAItem.getRequestedQtyUOM());
					PricingUOMDesc = XPEDXWCUtils.getUOMDescription(pandAItem
							.getPricingUOM());
				} catch (Exception e) {

				}

//				displayPriceForUoms.add(basePrice.toString() + "/"
//						+ BaseUomDesc);								//removed as specified in the bug 185 comments on 03/Jan/11 3:58 PM by Barb Widmer
				displayPriceForUoms.add(pricingUOMUnitPrice + "/"
						+ PricingUOMDesc);
				if (priceForCWTUom != null)
					displayPriceForUoms.add(priceForCWTUom.toString()+"/"+"CWT");
				if (priceForTHUom != null)
					displayPriceForUoms.add(priceForTHUom.toString()+"/"+"TH");
				displayPriceForUoms.add(pandAItem.getUnitPricePerRequestedUOM()
						+ "/" + RequestedQtyUOMDesc);
				displayPriceForUoms.add("Extended Price"+"-"+pandAItem.getExtendedPrice());
				// Vector bracketsPricingList = null;
				bracketsPricingList = pandAItem.getBrackets();
				setIsBracketPricing(XPEDXPriceandAvailabilityUtil
						.isBracketPricingAvailable(bracketsPricingList));
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
		
		itemUOMsMap = getXpedxUOMList();//XPEDXOrderUtils.getXpedxUOMList(customerId, itemId,
				//organizationCode);*/
		displayItemUOMsMap = new HashMap();
		for (Iterator it = itemUOMsMap.keySet().iterator(); it.hasNext();) {
			String uomDesc = (String) it.next();
			Object o = itemUOMsMap.get(uomDesc);
			if("1".equals(o))
			{
				displayItemUOMsMap.put(XPEDXWCUtils.getUOMDescription(uomDesc), uomDesc);
			}
			else{
				displayItemUOMsMap.put(XPEDXWCUtils.getUOMDescription(uomDesc)
						+ " (" + o + ")", uomDesc);
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
						log.error("Empty available quantity for "
								+ item.getLegacyProductCode()
								+ " in warehouse "
								+ wareHouseItem.getWarehouse());
						continue;
					}
					String noOfDaysString = wareHouseItem.getNumberOfDays();
					if (YFCCommon.isVoid(noOfDaysString)
							|| !isANumber(noOfDaysString)) {
						log.error("Empty or Corrupt NumberOfDays for "
								+ item.getLegacyProductCode()
								+ " in warehouse "
								+ wareHouseItem.getWarehouse());
						continue;
					}
					Float availQtyFloat = Float.valueOf(availQtyStr.trim())
							.floatValue();
					int noOfDays = Integer.parseInt(wareHouseItem
							.getNumberOfDays());
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
				toalAvailable = totalForImmediate + totalForNextDay
						+ totalForTwoPlus;

				JSONObject jsonAvail = new JSONObject();
				JSONObject jsonImmediate = new JSONObject();
				JSONObject jsonNext = new JSONObject();
				JSONObject jsonTwoPlus = new JSONObject();
				JSONObject jsonTotal = new JSONObject();
				String ItemUOMDesc = null;
				try {
					ItemUOMDesc = XPEDXWCUtils.getUOMDescription(item
							.getRequestedQtyUOM());
				} catch (Exception e) {
					log.error(
							"Exception while getting the UOM description for item "
									+ item.getLegacyProductCode() + " and UOM "
									+ item.getRequestedQtyUOM(), e);
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
				JSONObject[] outputList = { jsonAvail, jsonImmediate, jsonNext,
						jsonTwoPlus, jsonTotal };
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

	private ArrayList<XPEDXItem> getPnAInputDoc(String pnaItemId,
			String requestedUOM) {
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

	/*
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
			log
					.error("Unable to retrieve customerID from the context.Cannot prepare Item Branch Info. Returning back to the caller.");
			return;
		}
		// get the customers ship from division from the table
		try {
			//customerShipFromBranch = (String)wcContext.getWCAttribute(XPEDXConstants.SHIP_FROM_BRANCH);
			// Added for performance  - SortResultBy action
			shipToCustomer = (XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
			if(shipToCustomer== null){
				log.error("shipToCustomer object from session is null... Creating the Object and Putting it in the session");
				
				XPEDXWCUtils.setCustomerObjectInCache(XPEDXWCUtils
						.getCustomerDetails(getWCContext().getCustomerId(),
								getWCContext().getStorefrontId())
						.getDocumentElement());
				setShipToCustomer((XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER));
			}
			
			customerShipFromBranch = shipToCustomer.getExtnShipFromBranch();
			if (YFCCommon.isVoid(customerShipFromBranch)) {
				// oops... DB is messed up.
				log
						.error("customer ship from branch from the DB for Customer "
								+ customerID
								+ " is NULL."
								+ " Cannot evaluate Item Branch Info. Returning back to the caller");
				return;
			}
		} catch (CannotBuildInputException e) {
			log.error(
					"Unable to get customer ship from branch from the DB for Customer: "
							+ customerID, e);
		}
		// get the prepared Catalog Document
		Document catDoc = getOutDoc();
		
		// get the CatalogSearch/ItemList
		Element itemListElement = SCXmlUtil.getChildElement(catDoc
				.getDocumentElement(), "ItemList");
		NodeList itemNodeList = itemListElement.getElementsByTagName("Item");
		
		// JIRA-901: SKU Changes Begin
		String customerPartNumber = "";
		String manufacturerPartNo = "";
		String extnMpc = "";
		//Commented for EB 47 String customerUseSKU = (String) wcContext.getWCAttribute("customerUseSKU");
		//Added for EB 47
		extnMfgItemFlag = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.BILL_TO_CUST_MFG_ITEM_FLAG);
		extnCustomerItemFlag = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.BILL_TO_CUST_PART_ITEM_FLAG);
		//End of EB 47
		
		String legacyCustNumber = (String) wcContext.getWCAttribute(XPEDXConstants.LEGACY_CUST_NUMBER,WCAttributeScope.LOCAL_SESSION);
		// JIRA-901: SKU Changes End
		String environmentCode = (String) wcContext.getWCAttribute(XPEDXConstants.ENVIRONMENT_CODE,WCAttributeScope.LOCAL_SESSION);
		
		ArrayList<String> itemIds = new ArrayList<String>();
		
		for (int i = 0; i < itemNodeList.getLength(); i++) {
			itemIds.add(SCXmlUtil.getAttribute((Element) itemNodeList.item(i), "ItemID"));
		}
		
		if(!SCUtil.isVoid(extnCustomerItemFlag) && extnCustomerItemFlag.equalsIgnoreCase("Y")) {//Added for Eb 47
			if(itemIds.size()>0){
				//Document custPartNoDoc = XPEDXWCUtils.getXpxItemCustXRefDoc(itemIds, wcContext);
				//if(custPartNoDoc!=null){
				Element itemcustXrefListElemet = (Element)allAPIOutputDoc.getElementsByTagName("XPXItemcustXrefList").item(0);
				if(itemcustXrefListElemet != null)
				{
					NodeList itemCustXrefList = itemcustXrefListElemet.getElementsByTagName("XPXItemcustXref");
					if(itemCustXrefList != null)
					{
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
			xPXItemExtnListElement = (Element)allAPIOutputDoc.getElementsByTagName("XPXItemExtnList").item(0);//XPEDXWCUtils.getXPXItemExtnList(itemIds, wcContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			 getYPMPricelistLineList(itemIds, wcContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i = 0; i < itemNodeList.getLength(); i++) {
			// get the item node
			Node itemNode = itemNodeList.item(i);
			Element itemElement = (Element) itemNode;
			String itemID = SCXmlUtil.getAttribute(itemElement, "ItemID");
			// get the item extn element
			Element itemExtnElement = SCXmlUtil.getChildElement(itemElement,
					"Extn");
			
			// JIRA-901: SKU Changes Begin
			if(itemExtnElement!=null){
				extnMpc = itemExtnElement.getAttribute("ExtnMpc");
				Element primaryInformation = SCXmlUtil.getChildElement(itemElement, "PrimaryInformation");
				manufacturerPartNo = primaryInformation.getAttribute("ManufacturerItem");
				customerPartNumber = itemToCustPartNoMap.get(itemID) ;
				HashMap<String, String> skuMap = new HashMap<String, String>();
				skuMap.put("MPN", manufacturerPartNo);
				skuMap.put("MPC", extnMpc);
				skuMap.put("CPN", customerPartNumber);
				
				itemMap.put(itemID, skuMap);
				// JIRA-901: SKU Changes End
				
				List<Element> itemBranchElementList = null;
				if(xPXItemExtnListElement!=null) {
					try {
						itemBranchElementList = XMLUtilities.getElements(xPXItemExtnListElement,"XPXItemExtn[@ItemID='"+itemID+"']");
					} catch (XPathExpressionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if( itemBranchElementList!=null && itemBranchElementList.size()>0){
						Element itemBranchElement = itemBranchElementList.get(0);
		
						// get the field information
						String itemNumber = SCXmlUtil.getAttribute(itemBranchElement,
								"ItemID");
						String environmentID = SCXmlUtil.getAttribute(
								itemBranchElement, "EnvironmentID");
						String companyCode = SCXmlUtil.getAttribute(itemBranchElement,
								"CompanyCode");
						String legacyPartNo = SCXmlUtil.getAttribute(itemBranchElement,
								"MasterProductCode");
						String division = SCXmlUtil.getAttribute(itemBranchElement,
								"XPXDivision");
						String itemStockStatus = SCXmlUtil.getAttribute(
								itemBranchElement, "ItemStockStatus");
						String orderMultiple = SCXmlUtil.getAttribute(
								itemBranchElement, "OrderMultiple");
						String inventoryIndicator = SCXmlUtil.getAttribute(
								itemBranchElement, "InventoryIndicator");
						// prepare the PoJo
						XPEDXItemBranchInfoBean itemBranchInfoBean = new XPEDXItemBranchInfoBean(
								itemNumber, environmentID, companyCode, legacyPartNo,
								division, itemStockStatus, orderMultiple,
								inventoryIndicator);
		
						// inject it in HashMap
						itemToItemBranchBeanMap.put(itemNumber, itemBranchInfoBean);					
					}
				}
				
			}
		}
		wcContext.setWCAttribute("itemMap",itemMap, WCAttributeScope.REQUEST);
		wcContext.setWCAttribute("itemToItemBranchBeanMap",itemToItemBranchBeanMap, WCAttributeScope.SESSION);
		
	}

	/**
	 * Method to get the customer part number for an item
	 * @param itemID2
	 * @param environmentID
	 * @param customerShipFromBranch
	 * @param customerNumber
	 * @return
	 */
	private String getCustomerPartNumber(String itemID2, String environmentID,
			String customerShipFromBranch, String customerNumber) {
		
		/*Element inputDoc = SCXmlUtil.createDocument("XPXItemcustXref").getDocumentElement();
		inputDoc.setAttribute("EnvironmentCode", environmentID);
		inputDoc.setAttribute("CustomerBranch", customerShipFromBranch);// division
		inputDoc.setAttribute("LegacyItemNumber", itemID2);
		inputDoc.setAttribute("CustomerNumber", customerNumber);*/
		
		Element outputEl = null;
		Element custXrefEle = null;
		
		
		IWCContext wcContext =getWCContext();
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/XPXItemcustXref/@EnvironmentCode", environmentID);
		valueMap.put("/XPXItemcustXref/@CustomerBranch",customerShipFromBranch);
		valueMap.put("/XPXItemcustXref/@LegacyItemNumber", itemID2);
		valueMap.put("/XPXItemcustXref/@CustomerNumber", customerNumber);
		Element input;
		try {
			input = WCMashupHelper.getMashupInput("xpedxItemCustXRef", valueMap, wcContext.getSCUIContext());		
			outputEl = (Element)WCMashupHelper.invokeMashup("xpedxItemCustXRef", input, wcContext.getSCUIContext());
			custXrefEle = XMLUtilities.getElement(outputEl,
					"XPXItemcustXref");
		} catch (Exception e) {
			LOG.error("Error while retrieving the customer part number:getCustomerPartNumber " + e.getMessage(), e);
		} 
		SCXmlUtil.getString(outputEl);
		return SCXmlUtil.getAttribute(custXrefEle,"CustomerItemNumber");
	}
	
	public Document getYPMPricelistLineList(ArrayList<String> itemIdList, IWCContext wcContext) throws Exception {
		Document outputDoc = null;
		Document outputDocument = null;
		
	/*	String organizationCode = wcContext.getStorefrontId();
		String customerId = wcContext.getCustomerId();
		
		Map<String, String> valueMaps = new HashMap<String, String>();
		valueMaps.put("/PricelistAssignment/@CustomerID", customerId);
		
		Element inputElement = WCMashupHelper.getMashupInput("xpedxYpmPricelistAssignmentList", valueMaps,getWCContext().getSCUIContext());
		Element object = (Element) WCMashupHelper.invokeMashup("xpedxYpmPricelistAssignmentList", inputElement,getWCContext().getSCUIContext());
		
		outputDocument = ((Element) object).getOwnerDocument();
		Element objct		= getXMLUtils().getChildElement(object, "PricelistAssignment");
		if (objct!=null) {
			String priceListHeaderKey = objct.getAttribute("PricelistHeaderKey");
			
			Map<String, String> valueMap = new HashMap<String, String>();
			valueMap.put("/PricelistLine/Item/@OrganizationCode", organizationCode);
			valueMap.put("/PricelistLine/PricelistHeader/@PricelistHeaderKey", priceListHeaderKey);

			Element input = WCMashupHelper.getMashupInput("xpedxYpmPricelistLine", valueMap,getWCContext().getSCUIContext());
			Document inputDoc = input.getOwnerDocument();
			NodeList inputNodeList = input.getElementsByTagName("Or");
			Element inputNodeListElemt = (Element) inputNodeList.item(0);
			for (int i = 0; i < itemIdList.size(); i++) {
				Document expDoc = YFCDocument.createDocument("Exp").getDocument();
				Element expElement = expDoc.getDocumentElement();
				expElement.setAttribute("Name", "ItemID");
				expElement.setAttribute("Value", itemIdList.get(i));
				inputNodeListElemt.appendChild(inputDoc.importNode(expElement, true));
			}
			Element obj = (Element)WCMashupHelper.invokeMashup("xpedxYpmPricelistLine", input,getWCContext().getSCUIContext());
			outputDoc = ((Element) obj).getOwnerDocument();*/
			
			NodeList itemNodeList1 = allAPIOutputDoc.getElementsByTagName("PricelistLine");
			Map <String, List<Element>> PricelistLineMap = new HashMap<String, List<Element>>();
			List<Element> PricelistLineList = null;
			if(itemNodeList1 != null)
			{
				for (int i = 0; i < itemNodeList1.getLength(); i++) {
					Node itemNode = itemNodeList1.item(i);
					Element itemElement = (Element) itemNode;
					String itemId = itemElement.getAttribute("ItemID");
					String quantity = itemElement.getAttribute("FromQuantity");
					if (quantity == "" || quantity == null || quantity.equals("")|| quantity.equals("null"))
					{
						itemElement.setAttribute("FromQuantity", "1");
					}
					if(PricelistLineMap.containsKey(itemId)) {
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
			/*if (null != outputDoc) {
				log.debug("Output XML for xpedxYpmPricelistLine: " + SCXmlUtil.getString((Element) obj));
			}*/
		//}
		return outputDoc;
	}
	
	private Map <String, List<Element>> sortPriceListLine(Map <String, List<Element>> PricelistLineMap)
	{
		Set<String> itemSet=PricelistLineMap.keySet();
		Map <String, List<Element>> _pricelistLineMap = new HashMap<String, List<Element>>();
		for(String itemID:itemSet)
		{
			List priceList=PricelistLineMap.get(itemID);
			Collections.sort(priceList, new XPEDXSortingListPrice());
			_pricelistLineMap.put(itemID, priceList);
		}
		return _pricelistLineMap;
	}
	
	
	public String setNormallyStockedCheckbox() {
		init();
		getWCContext().setWCAttribute("StockedCheckbox",isStockedCheckeboxSelected(),WCAttributeScope.SESSION);
		return SUCCESS;
	}
	/*Start - Code changes for 2964*/
	private double getConversion(Object convFactor, String orderMultiple) {
		if (convFactor != null && orderMultiple != null && orderMultiple.length() > 0) {
			String parseConvFactor = convFactor.toString();
			double convFactorD = Double.valueOf(parseConvFactor).doubleValue();
			double orderMultipleD = Double.parseDouble(orderMultiple);
			double factor = (convFactorD / orderMultipleD);
			return factor;
			/*if (Math.abs(factor) == factor) {
				return (int) Math.abs(factor);
			}*/
		}
		return -1;
	}
	/*End - Code changes for 2964*/

	//Start JIRA  XBT-319
			private String processSpecialCharacters(String searchText){
				if(searchText == null){
					return null;
				}
				String specialCharacterReg = "[\\[\\]^);{!(}:,~\\\\]";
				searchText = searchText.replaceAll(specialCharacterReg," ");
				searchText = processPlusCharacter(searchText);
				searchText = processEiphenCharacter(searchText);
				return searchText;
			}
			
			 /*Replace "+" with whitespace when it 
			 * a) appears as a word 
			 * b) appears as first character in a word 
			 * c) appears as last character in a word
			 */
			private String processPlusCharacter(String searchText){
				String whiteSpaceReg = "[ ]";
				String[] searchTexts = searchText.split(whiteSpaceReg);
				StringBuilder searchTerm = new StringBuilder();
				for(String textSegment : searchTexts){
					textSegment = textSegment.trim();
					if(textSegment.equals("+")){
						continue;
					}
					
					if(textSegment.startsWith("+")){
						textSegment = " " + textSegment.substring(1, textSegment.length());
					}
					
					if(textSegment.endsWith("+")){
						textSegment = textSegment.substring(0, textSegment.length()-1) + " ";
					}
					
					searchTerm.append(textSegment);
					searchTerm.append(" ");
					
				}
				return searchTerm.toString();
			}
			
			/*Replace "-" with whitespace when it 
			 * a) appears as a word 
			 * b) appears as last character in a word
			 */
			private String processEiphenCharacter(String searchText){
				String whiteSpaceReg = "[ ]";
				String[] searchTexts = searchText.split(whiteSpaceReg);
				StringBuilder searchTerm = new StringBuilder();
				for(String textSegment : searchTexts){
					textSegment = textSegment.trim();
					if(textSegment.equals("-")){
						continue;
					}
					
					if(textSegment.endsWith("-")){
						textSegment = textSegment.substring(0, textSegment.length()-1)+" ";
						
					}
					
					searchTerm.append(textSegment);
					searchTerm.append(" ");
					
				}
				return searchTerm.toString();
			}
			//End JIRA XBT-319
	
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


	/**** added for fix     XNGTP-216 ****  used to set the selected view **/
	
	public void setSelectedView(String sSelectedView){
		
		this.selectedView =sSelectedView;
	}
	
	/**** added for fix     XNGTP-216 ****  used to get the selected view **/
	public String getSelectedView(){
		
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
	private HashMap<String, HashMap<String, String>> itemMap = new HashMap<String, HashMap<String,String>>();
	protected String editedOrderHeaderKey="";
	protected String draft;
	protected String path;	
	public Map<String,List<Element>> facetListMap = new HashMap<String , List<Element>>();//Added for JIRA 3821
	private String firstItemCategoryShortDescription;
	private String tempCategoryPath;
	private Element allAPIOutputDoc;
	private String categoryDepthNarrowBy;
	private Map<String, String> sortListMap = new LinkedHashMap<String, String>();	

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
	 * @param itemMap the itemMap to set
	 */
	public void setItemMap(HashMap<String, HashMap<String, String>> itemMap) {
		this.itemMap = itemMap;
	}

	public Map<String, Map<String, String>> getItemUomHashMap() {
		return itemUomHashMap;
	}

	public void setItemUomHashMap(Map<String, Map<String, String>> itemUomHashMap) {
		this.itemUomHashMap = itemUomHashMap;
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

	public void setItemToCustPartNoMap(HashMap<String, String> itemToCustPartNoMap) {
		this.itemToCustPartNoMap = itemToCustPartNoMap;
	}

	public XPEDXShipToCustomer getShipToCustomer() {
		return shipToCustomer;
	}

	public void setShipToCustomer(XPEDXShipToCustomer shipToCustomer) {
		this.shipToCustomer = shipToCustomer;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * This operation will verfiy if Value is Integer
	 * isInteger
	 * @param i
	 * @return
	 */
	public static boolean isDouble(String i)
	{
		try {
			Double.parseDouble(i);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

}
