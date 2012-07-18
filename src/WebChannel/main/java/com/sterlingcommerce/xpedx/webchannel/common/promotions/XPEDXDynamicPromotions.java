package com.sterlingcommerce.xpedx.webchannel.common.promotions;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXFileManager;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfc.util.YFCException;
import com.yantra.yfs.core.YFSSystem;

/**
 * XPEDXDynamicPromotions fetches the promotions dynamically in the form of an HTML page
 * based on the caller page and other parameters passed. <br>
 * 
 * For guest users the default pages like HomePage_Promo.html and Catalog_Promo.html is fetched.
 * <br>
 * 
 * For home page if industry is given then <industry>_HomePage_Promo.html is fetched. 
 * If the page does not exists defaults to HomePage_Promo.html.
 * <br>
 * 
 * For catalog page in the landing page if exists <industry>_CatLanding_Promo.html is fetched.
 * If it does not exists defaults to Catalog_Promo.html
 * <br>
 * 
 * For individual catalog page if exists <category>_<division>_Catalog_Promo.html is fetched.
 * If it does not exists defaults to Catalog_Promo.html
 * <br>
 */
public class XPEDXDynamicPromotions extends WCAction {

	private static final long serialVersionUID = 8339665862559979566L;
	private static final String customerExtnInformation = "xpedx-customer-getCustomExtnFieldsInformation";
	private static final Logger log = Logger
			.getLogger(XPEDXDynamicPromotions.class);
	
	private static Map CALLER_PAGE_MAP = new HashMap();
	private static String HTML_PAGE_SUFFIX = "_Promo.html";
	//private static String HTML_PAGE_FILE_PATH_PROP = "promotions_shared_path";
	private static String XPEDX_MARKETING_PROMOTIONS_FILES_PATH;
	
	private static String PATH_SEPARATOR = System.getProperty("file.separator");
	
	static {
		//Html name for caller page specific is added to the map
		CALLER_PAGE_MAP.put("home", XPEDXConstants.XPEDX_CALLER_HOME_PAGE);
		CALLER_PAGE_MAP.put("catalog", XPEDXConstants.XPEDX_CALLER_CATALOG_PAGE);
		CALLER_PAGE_MAP.put("catlanding", XPEDXConstants.XPEDX_CALLER_CATLANDING_PAGE);
		CALLER_PAGE_MAP.put("signinpage", XPEDXConstants.XPEDX_CALLER_SIGNIN_PAGE);
		
		String wcPropertiesFile = "xpedx_webchannel.properties";
		XPEDXWCUtils.loadXPEDXSpecficPropertiesIntoYFS(wcPropertiesFile);
		
		
		// get the relative path for the html page
		XPEDX_MARKETING_PROMOTIONS_FILES_PATH = YFSSystem.getProperty(XPEDXConstants.DYN_PROMO_SHARED_PATH_PROP);
		if(log.isDebugEnabled()){
		log.debug(" XPEDX_MARKETING_PROMOTIONS_FILES_PATH  : " + XPEDX_MARKETING_PROMOTIONS_FILES_PATH );
		log.debug(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		}
		if (YFCCommon.isVoid(XPEDX_MARKETING_PROMOTIONS_FILES_PATH)){
			log.error("This " + XPEDXConstants.DYN_PROMO_SHARED_PATH_PROP + " property is missing.");
			XPEDX_MARKETING_PROMOTIONS_FILES_PATH = "";
		}
		if (! XPEDX_MARKETING_PROMOTIONS_FILES_PATH.endsWith(PATH_SEPARATOR)){
			XPEDX_MARKETING_PROMOTIONS_FILES_PATH = XPEDX_MARKETING_PROMOTIONS_FILES_PATH + PATH_SEPARATOR;
		}
	}
	
	private String callerPage;
	private String customerId;
	private String orgCode;
	private String industry;
	private String division;
	private String category;

	public String execute() {
		String promoHtml = null;
		boolean isFileExists =false ;
		try {
			promoHtml = getPageBasedHtml(); 
			String storeFrontId = wcContext.getStorefrontId();
			if (! YFCCommon.isVoid(storeFrontId) && "signinpage".equals(getCallerPage())){
				//promoHtml = storeFrontId + "_" + promoHtml; 
				
				promoHtml = "test2_" + storeFrontId + "_" + promoHtml; 
				if(log.isDebugEnabled()){
					log.debug(" NEW File Name (Test purpose modified) :" + promoHtml );
				}
			}
			String defaultPromoHtml = XPEDX_MARKETING_PROMOTIONS_FILES_PATH + promoHtml;
			isFileExists = XPEDXFileManager.checkFile(defaultPromoHtml, this.wcContext, false);
			if(log.isDebugEnabled()){
				log.debug ( "-PREV--  FileName : " + defaultPromoHtml + " , isFileExists : " + isFileExists );
			}
			if (defaultPromoHtml.contains(":")){
				defaultPromoHtml = defaultPromoHtml.substring(defaultPromoHtml.indexOf(PATH_SEPARATOR), defaultPromoHtml.length());
			}		
			if (!defaultPromoHtml.startsWith(PATH_SEPARATOR)){
				defaultPromoHtml = PATH_SEPARATOR + defaultPromoHtml;
			}
			defaultPromoHtml = defaultPromoHtml.replace("\\", "/");
			defaultPromoHtml = defaultPromoHtml.replace("//", "/");
			
			// for guest user fetches the default html page.
			if(wcContext.isGuestUser()){
				log.debug("Promotions file " + promoHtml);
				setInRequest(defaultPromoHtml);
				return SUCCESS;
			}
			
			try
			{
			promoHtml = getPromoHtml(promoHtml);
			}
			catch(Exception ex)
			{
				log.error(ex.getMessage());
				
				/*if no default ship to assigned for the promotion it is not able to get the org code that is why jira 2406 raised this catch block applied for the first case mention in the jira*/
			}
			
			// check if the file exists otherwise return the default ones like the guest user's html page.
			if (YFCCommon.isVoid(promoHtml) || !doesFileExists(promoHtml)){
				promoHtml = defaultPromoHtml;
			}
			if (promoHtml.contains(":")){
				promoHtml = promoHtml.substring(promoHtml.indexOf(PATH_SEPARATOR), promoHtml.length());
			}
			if (!promoHtml.startsWith(PATH_SEPARATOR)){
				promoHtml = PATH_SEPARATOR + promoHtml;
			}
			promoHtml = promoHtml.replace("\\", "/");
			promoHtml = promoHtml.replace("//", "/");
			
			log.debug("Promotions file " + promoHtml);
			setInRequest(promoHtml);
			
		} catch (Exception ex) {
			log.error(ex.getMessage());			
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * getPromoHtml method concatenates the filename as given in the class javadocs.
	 * @param promoHtml is the initial html filename.
	 * @return promoHtml with filename prefixed based on the page and customer information.
	 */
	private String getPromoHtml(String promoHtml) throws Exception {
		// fetch the customer's industry and division
		getCustomerInfo();
		
		//If category is given (in case of individual catalog page) add it to the Html file name
		if (! YFCCommon.isVoid(getCategory())&& "catalog".equals(getCallerPage())){
			if(! YFCCommon.isVoid(getDivision().trim())){
				promoHtml = getCategory().trim() + "_" + getDivision().trim() + "_" + promoHtml; 
			}else{
				promoHtml = getCategory().trim() + "_" + promoHtml; 
			}
			promoHtml = XPEDX_MARKETING_PROMOTIONS_FILES_PATH + promoHtml;
			return promoHtml;
		}
		
		// If industry is given (in case of home page and catalog landing page) add it to the Html file name
		if (! YFCCommon.isVoid(getIndustry()) && "home".equals(getCallerPage())){
			promoHtml = getIndustry().trim() + "_" + promoHtml; 
		}
		
		/*String storeFrontId = wcContext.getStorefrontId();
		// If store front is present (in case of signin page) add it to the Html file name
		if (! YFCCommon.isVoid(storeFrontId) && "signinpage".equals(getCallerPage())){
			promoHtml = storeFrontId + "_" + promoHtml; 
		}*/
		promoHtml = XPEDX_MARKETING_PROMOTIONS_FILES_PATH + promoHtml;
		
		return promoHtml;
	}

	/**
	 * doesFileExists method checks if the html file with promotions exists. 
	 * @param fileName specifies the promotion html file's name.
	 * @return true if the file exists; otherwise false.
	 */
	private boolean doesFileExists(String fileName) {
		boolean isFileExists = false;
		try {
			isFileExists =  XPEDXFileManager.checkFile(fileName, this.wcContext, false);
			if(log.isDebugEnabled()){
				log.debug ( "-PREV- FileName (doesFileExists) : " + fileName  + "   - isFileExists : " + isFileExists );
			}
			return isFileExists;
			// get the absolute path of the html file
			//String testFileName = this.wcContext.getSCUIContext().getServletContext().getRealPath(fileName);
			//File wFile = new File(testFileName);
			// check if file exists
			/*if (wFile.exists()) {
				return true;
			}*/
			} catch (Exception ex) {
				log.error("Promotions file " + fileName + "does not exist.");
				return false;
			}
	}

	/**
	 * getCustomerInfo method fetches a customer's industry and division based on the customerId and orgCode.
	 * The fields are in the instance variables with the same name.
	 * 
	 * @throws YFCException if for some reason the customerId or orgCode is null.
	 */
	private void getCustomerInfo() throws Exception{
		// first fetch customerId and orgCodefrom the parameter
		String custId = getCustomerId();
		String orgCode = getOrgCode();
		XPEDXShipToCustomer shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		// if the customerId is not found in parameter fetch from the context.
		if (YFCCommon.isVoid(custId)){
			custId = wcContext.getCustomerId();
			if (YFCCommon.isVoid(custId)){
				throw new YFCException("CustomerId is null.");
			}
		}

		// if the orgCode is not found in parameter fetch from the context.
		if (YFCCommon.isVoid(orgCode)){
			orgCode = wcContext.getStorefrontId();
			if (YFCCommon.isVoid(orgCode)){
				throw new YFCException("Organization Code is null.");
			}
		}
		
		// fetch the customer details using the customerId and orgCode
		/**
		 * JIRA 243
		 * Modified getCustomerDetails method to consider the mashup to be invoked
		 * so that, we get only the required information - Certain Extn fields.
		 * @param inputItems
		 * @return
		 */
		// this Database call is removed and the required below variables are set into the LOCAL SESSION in the Header Action(line no: 275)	
		/*
		Document outDoc = XPEDXWCUtils.getCustomerDetails(custId, orgCode, customerExtnInformation);
		YFCDocument doc = YFCDocument.getDocumentFor(outDoc);
		YFCElement customerEle = doc.getDocumentElement();
		YFCElement custExtnEle = customerEle.getChildElement("Extn");
		String division = custExtnEle.getAttribute("ExtnShipFromBranch");
		String industry = custExtnEle.getAttribute("ExtnIndustry");
		*/
		
		// set the instance variable with the value of the ship from division
		if(!YFCCommon.isVoid(shipToCustomer.getExtnShipFromBranch())){
			setDivision(shipToCustomer.getExtnShipFromBranch().trim());
		}
		// set the instance variable with the value of the customer's industry
		if(!YFCCommon.isVoid(shipToCustomer.getExtnIndustry())){
			setIndustry(shipToCustomer.getExtnIndustry().trim());
		}
		
	}

	/**
	 * setInRequest method sets the promotions html value in the request as attribute.
	 * @param promoHtml of type String
	 */
	private void setInRequest(String promoHtml) {
		if(log.isDebugEnabled()){
			log.debug( "-PREV- HTML_PAGE_FILE_PATH  --> " + XPEDX_MARKETING_PROMOTIONS_FILES_PATH );
			log.debug( "-PREV- setInRequest : promoHtml  --> " + promoHtml );
		}
		wcContext.setWCAttribute(XPEDXConstants.REQUEST_ATTR_PROMO_PAGE_URL, promoHtml, WCAttributeScope.REQUEST);
	}

	/**
	 * This method fetches the Html page name based on the callerPage.
	 * 
	 * @return String containing the THML page name
	 */
	private String getPageBasedHtml() {
		String page = (String)CALLER_PAGE_MAP.get(getCallerPage().trim().toLowerCase());
		if( YFCCommon.isVoid(page)){
			return "";
		}
			return page + HTML_PAGE_SUFFIX;
	}
	
	/**
	 * Getter for industry that the customer belongs to.
	 * @return industry value as String.
	 */
	public String getIndustry() {
		return industry;
	}

	/**
	 * Modifier for industry that the customer belongs to.
	 * @param industry value of type String.
	 */
	public void setIndustry(String industry) {
		this.industry = industry;
	}

	/**
	 * Getter for the category a catalog belongs to.
	 * @return category value as String.
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Modifier for the category a catalog belongs to.
	 * @param category value of type String.
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	
	/**
	 * Getter for the callerPage identifier.
	 * @return callerPage value as String.
	 */
	public String getCallerPage() {
		return callerPage;
	}

	/**
	 * Modifier for the callerPage identifier.
	 * @param callerPage value of type String.
	 */
	public void setCallerPage(String callerPage) {
		this.callerPage = callerPage;
	}
	
	/**
	 * Getter for the customer id .
	 * @return customerId value as String.
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * Modifier for the customer id .
	 * @param customerId value of type String.
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * Getter for the organization code a customer belongs to.
	 * @return orgCode value as String.
	 */
	public String getOrgCode() {
		return orgCode;
	}

	/**
	 * Modifier for the organization code a customer belongs to.
	 * @param orgCode value of type String.
	 */
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	/**
	 * Getter for the customer division variable.
	 * @return division value as String.
	 */
	public String getDivision() {
		return division;
	}

	/**
	 * Modifier for the customer division variable.
	 * @param division of type String.
	 */
	public void setDivision(String division) {
		this.division = division;
	}
}
