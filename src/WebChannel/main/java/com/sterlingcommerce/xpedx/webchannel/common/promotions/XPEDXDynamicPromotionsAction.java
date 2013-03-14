package com.sterlingcommerce.xpedx.webchannel.common.promotions;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.ui.web.framework.SCUILocalSession;
import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXFileManager;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfc.util.YFCException;
import com.yantra.yfs.core.YFSSystem;


/**
 * @author reddypur
 */

/** OLD PROMO FLOW (DON'T DELETE)
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


/** NEW PROMO FLOW
 * -------------------------  PRE LOGIN -------------------
 * SignIn Page - Top (rotational image)
 * 		File name: <Storefront>_SPT_Promo.html
 *		There is no default if file is not found (--NO--).
 *
 * SignIn Page - Bottom (non rotational image)
 * 		File name: <Storefront>_SPB_Promo.html
 * 		There is no default if file is not found (--NO--).
 * 
 * 
 * Catalog Page - guest user
 * 		File name: <Storefront>_AP_Promo.html
 * 		There is no default if file is not found (--NO--). 
 * 
 * -------------------------  POST LOGIN -------------------
 * Home Page
 * 		File name: <Storefront>_HP_<ShipFromDivision>_<CustomerSegment>_Promo.html
 * 		If file not found, default to <Storefront>_HP_Promo.html
 * 	
 * Catalog Page - registered user
 * 		File name: <Storefront>_CP_<Cat1 of first item>_Promo.html
 * 		If file not found, default to <Storefront>_CP_Promo.hmtl
 * 
 **/

public class XPEDXDynamicPromotionsAction extends WCAction {
	
	private   String LIZ_MESSAGE = "PROMO FAILED LIZ";
	private  boolean DEBUG_TRACE = false;
	
	//Here Category and callerPage comes as Action Parameters.
	private String callerPage;
	private String category;
	private String categoryPath;
	
	private int imageCount; 
	
	//Initialized for pre-login stage
	private String storeFrontId;
	private boolean isGuestUser;
	
	//Setting Manually
	private String customerId ="";
	private String organizationCode;
	private String industry = "";
	private String division = "";
	
	//
	private String  generatedFileName ;
	private String  generatedFileFullPath ;
	private boolean isDefaultFileUsed =false;
	//Final Response we set to request and then access by the JSP for rendering 
	//private String ultimateRespPromoHtmlFullFileName = "";
		
	private static final long serialVersionUID = 8339664862359079566L;
	private static final String customerExtnInformation = "xpedx-customer-getCustomExtnFieldsInformation";
	private static final Logger log = Logger.getLogger(XPEDXDynamicPromotionsAction.class);
	private static final Pattern IMG_PATTERN = Pattern.compile(
	    		"<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>", 
	                                               Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
	/**
	 * WebLogic Application Server path for Promotions and other conventions.
	 */
	//private static String SRVR_DYN_PROMO_SYSTEM_PATH = "promotions_shared_path";
	private static String SRVR_PATH_SEPARATOR = System.getProperty("file.separator");
	private static String XPEDX_MARKETING_PROMOTIONS_FILES_PATH;
	
	/**
	 * Standard Prefixes and suffixes used for Dynamic Promotions
	 */
	private static String STD_PROMO_HTML_SUFFIX 	= "Promo.html";
	private static String STD_PRE_LOGIN_PREFIX 		= "preLogin";
	private static String STD_POST_LOGIN_PREFIX 	= "postLogin";
	private static String STD_DEFAULT 				= "Default";
	

	/** 
	 * CALLING PAGE (i.e. JSP ) SENDS THESE PARAMETERS FOR STRUTS ACTION CLASS (CALLER_REQ_PROMO_PARAM_MAP)
	 * 
	 * These four are Unique Locations at present where we are allowing promotions. 
	 * Each need to be Unique value to avoid confusion.
	 * Any parameter sent from JSP need to match to one of these 4 values.
	 * We are going to compare with
	 */
	public static final String CALLER_PROMO_REQ_PARAM_SIGNIN_PG_TOP  			=  	"SignInPageTop";
	public static final String CALLER_PROMO_REQ_PARAM_SIGNIN_PG_BOTTOM  		=  	"SignInPageBottom";
	public static final String CALLER_PROMO_REQ_PARAM_HOME_PG  					=  	"HomePage";
	public static final String CALLER_PROMO_REQ_PARAM_CATALOG_PG  				=  	"CatalogPage";
	public static final String CALLER_PROMO_REQ_PARAM_SIGNIN_PG_SIDE			=  	"SignInPageSide";
	
	/**
	 * PRE_LOGIN PROMOTIONS (XPEDX_PROMO_RESP_MAP)
	 * Contains PreLogin, PostLogin and Default constants.
	 */ 
	//PRE LOGIN
	public static final String XPEDX_PROMO_RESP_PRE_LOGIN_SIGNIN_PAGE_TOP			=	//"preLoginSignInPageTop";
		STD_PRE_LOGIN_PREFIX + CALLER_PROMO_REQ_PARAM_SIGNIN_PG_TOP ;
	
	public static final String XPEDX_PROMO_RESP_PRE_LOGIN_SIGNIN_PAGE_BOTTOM		=	//"preLoginSignInPageBottom";
		STD_PRE_LOGIN_PREFIX + CALLER_PROMO_REQ_PARAM_SIGNIN_PG_BOTTOM ;
	
	public static final String XPEDX_PROMO_RESP_PRE_LOGIN_CATALOG_PAGE				=	//"preLoginCatalogPage";
		STD_PRE_LOGIN_PREFIX + CALLER_PROMO_REQ_PARAM_CATALOG_PG ;
	
	public static final String XPEDX_PROMO_RESP_PRE_LOGIN_SIGNIN_PG_SIDE			=	STD_PRE_LOGIN_PREFIX + CALLER_PROMO_REQ_PARAM_SIGNIN_PG_SIDE ;
	
	//POST LOGIN
	public static final String XPEDX_PROMO_RESP_POST_LOGIN_HOME_PAGE				=	//"postLoginHomePage";
		STD_POST_LOGIN_PREFIX  + CALLER_PROMO_REQ_PARAM_HOME_PG;
	
	public static final String XPEDX_PROMO_RESP_POST_LOGIN_CATALOG_PAGE				=	//"postLoginCatalogPage";
		STD_POST_LOGIN_PREFIX  + CALLER_PROMO_REQ_PARAM_CATALOG_PG;

	//POST_LOGIN DEFAULT PROMOTIONS DEFAULTS
	//IN Case File not found (Applicable only for Post Login)
	public static final String XPEDX_PROMO_RESP_POST_LOGIN_HOME_PAGE_DEFAULT		=	//"postLoginHomePageDefault";
		STD_POST_LOGIN_PREFIX  + CALLER_PROMO_REQ_PARAM_HOME_PG + STD_DEFAULT;
	
	public static final String XPEDX_PROMO_RESP_POST_LOGIN_CATALOG_PAGE_DEFAULT		=	//"postLoginCatalogPageDefault";
		STD_POST_LOGIN_PREFIX  + CALLER_PROMO_REQ_PARAM_CATALOG_PG + STD_DEFAULT;
	
	
	
	
	//Just In case
	public static final String XPEDX_RESP_PROMO_PAGE_NOT_APPLICABLE					=	"pageNotApplicable";

	
	
	public static Vector<String> CALLER_REQ_PROMO_PARAM_VECTOR = new Vector<String>();
	
	/**
	 * This is 
	 */
	static {
		//Keep all possible request params in vector
		CALLER_REQ_PROMO_PARAM_VECTOR.addElement( CALLER_PROMO_REQ_PARAM_SIGNIN_PG_TOP );
		CALLER_REQ_PROMO_PARAM_VECTOR.addElement( CALLER_PROMO_REQ_PARAM_SIGNIN_PG_BOTTOM );
		CALLER_REQ_PROMO_PARAM_VECTOR.addElement( CALLER_PROMO_REQ_PARAM_SIGNIN_PG_SIDE );
		CALLER_REQ_PROMO_PARAM_VECTOR.addElement( CALLER_PROMO_REQ_PARAM_HOME_PG );
		CALLER_REQ_PROMO_PARAM_VECTOR.addElement( CALLER_PROMO_REQ_PARAM_CATALOG_PG );
		
		
		String wcPropertiesFile = "xpedx_webchannel.properties";
		XPEDXWCUtils.loadXPEDXSpecficPropertiesIntoYFS(wcPropertiesFile);
		

		XPEDXConstants.logMessage(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		// get the relative path for the html page
		XPEDX_MARKETING_PROMOTIONS_FILES_PATH = YFSSystem.getProperty(XPEDXConstants.DYN_PROMO_SHARED_PATH_PROP);
		XPEDXConstants.logMessage(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	
		if (YFCCommon.isVoid(XPEDX_MARKETING_PROMOTIONS_FILES_PATH)){
			log.error("This " + XPEDXConstants.DYN_PROMO_SHARED_PATH_PROP + " Application server property is missing.");
			XPEDX_MARKETING_PROMOTIONS_FILES_PATH = "";
		}
		if (! XPEDX_MARKETING_PROMOTIONS_FILES_PATH.endsWith(SRVR_PATH_SEPARATOR)){
			XPEDX_MARKETING_PROMOTIONS_FILES_PATH = XPEDX_MARKETING_PROMOTIONS_FILES_PATH + SRVR_PATH_SEPARATOR;
		}
	}
	
	
	public String getCallerPage() {
		return callerPage;
	}

	public void setCallerPage(String callerPage) {
		this.callerPage = callerPage;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String orgCode) {
		this.organizationCode = orgCode;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industryVal) {
		if(industryVal != null && industryVal.trim().length() > 0)
			this.industry = industryVal;
		else
			this.industry = "";
			
	}

	public String getDivision() {
		return division;
		
	}

	public void setDivision(String divisionVal) {
		if(divisionVal != null && divisionVal.trim().length() > 0)
			this.division = divisionVal;
		else
			this.division = "";
	}

	/**
	 * Always return FirstItem in the list
	 */
	public String getFirstItemInCategory() {
		String firstCat = "";
		
		//Added extra validation since it is throwing NPE in rare scenarios.
		if(category != null)
			firstCat = category;
		
		if(category != null && category.length() >  0 ){
			String cats[] = category.split(",");
			
			if(cats.length > 0)
				if(cats[0] != null)
					firstCat =  cats[0].trim();
				else
					firstCat="";
		}
		
			//return firstCat;
		logMessage("Seding null FirstItemInCategory .... 1");
			return firstCat;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String categoryVal) {
		
		if(categoryVal != null && categoryVal.trim().length() > 0)
			this.category = categoryVal;
		else
			this.category = "";
	}
	
	public String getCategoryPath() {
		return categoryPath;
	}
	

	public void setCategoryPath(String catPath) {
		
		if(catPath != null && catPath.trim().length() > 0)
			this.categoryPath = catPath;
		else
			this.categoryPath = "";
	}
	

	public String getStoreFrontId() {
		return storeFrontId;
	}

	public void setStoreFrontId(String storeFrontId) {
		this.storeFrontId = storeFrontId;
	}

	public boolean isGuestUser() {
		return isGuestUser;
	}

	public void setGuestUser(boolean isGuestUser) {
		this.isGuestUser = isGuestUser;
	}
	
	
	public boolean isDefaultFileUsed() {
		return isDefaultFileUsed;
	}

	public void setDefaultFileUsed(boolean isDefaultFileUsed) {
		this.isDefaultFileUsed = isDefaultFileUsed;
	}

	
	public String getGeneratedFileName() {
		return generatedFileName;
	}

	public boolean setGeneratedFileName(String generatedFileName) {
		
		if(generatedFileName != null && generatedFileName.length() > 0 ) {
			this.generatedFileName = generatedFileName;
			
			//Now set fullpath for the file, Massage the path and set to setGeneratedFileFullPath
			String tempFilePath = XPEDX_MARKETING_PROMOTIONS_FILES_PATH.trim() + generatedFileName.trim();
			tempFilePath = massageFileName(tempFilePath);			
			setGeneratedFileFullPath(tempFilePath);			
			return true;
		}
		else
			return false;
	}

	public String getGeneratedFileFullPath() {
		return generatedFileFullPath;
	}

	public void setGeneratedFileFullPath(String generatedFileFullPath) {
		this.generatedFileFullPath = generatedFileFullPath;
	}
	
	

	
	public int getPromoImageCount(String tempFilePath)
	{
		int count=0;	
		String fileContent = getFileContents(tempFilePath);		
		if (null == fileContent)
			return count;
		Matcher m = IMG_PATTERN.matcher(fileContent);
		while (m.find()) { // find next match
		    count=count+1;
		
		}	   
		return count; 
	}
	
	/**
	 * Read  FILE Contents ...
	 * 
	 * @return
	 * @throws Exception
	 */
	
	public String getFileContents(String fileName) {

		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		StringBuffer imageLineBuffer = new StringBuffer();
		try {
			
			ServletContext servletContext = wcContext.getSCUIContext().getServletContext();
			inputStream = servletContext.getResourceAsStream(fileName);
			if (null == inputStream ) {
				log.error("The file "+ fileName + " is missing for dynamic promotions");
				return null;
			}
			
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			String thisLine = "";
			
			while ((thisLine = bufferedReader.readLine()) != null) {
				imageLineBuffer.append (thisLine);
			}
			
			
		} catch (IOException ioe) {
			log.error("Exception in getFileContents  "+ioe.getMessage());
		}

		finally {
			try {
				if (inputStream != null)
					inputStream.close();
				if (inputStreamReader != null)
					inputStreamReader.close();
				if (bufferedReader != null)
					bufferedReader.close();
			} catch (IOException e) {
				log.error("Exception while closing inputStream  "+e.getMessage());
			}			
		}
		return imageLineBuffer.toString();
	}

	/**
	 * extracts catalog current category Name ...
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getCurrentCategoryName() throws Exception {
		String currentCategoryName = "";
		String cpath = getCategoryPath();
		
		
		if (cpath != null && cpath.length() > 0 ){
			
			Map<String,String> mainCats = getMainCategories();
			String pathElements[] = cpath.split("/");
			for(int i = 0; i <pathElements.length ; i++) {
				
				if(mainCats !=null && mainCats.size() > 0 )
					if( mainCats.containsKey(pathElements[i]) )
						currentCategoryName = mainCats.get(pathElements[i]);
				XPEDXConstants.logMessage("currentCategoryName : " + currentCategoryName );
				currentCategoryName = currentCategoryName.replaceAll(" ", "");
				
			}
		}
			
		
		return currentCategoryName;
	}
	
	/**
	 * extracts catalog main categories...
	 * 
	 * @return
	 * @throws Exception
	 */
	
	public Map<String,String> getMainCategories() throws Exception {
		Map<String, String> MainCategories = new LinkedHashMap<String, String>();

		Element outputXML = null;
		//performance issue for filter action
		Document outDoc = null;
		Element catElem;
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
		LOG.debug("Input XML: " + inputXml);
		
		Object obj = WCMashupHelper.invokeMashup(
				"xpedx-PreferredCatalogOptions", input, wcContext
						.getSCUIContext());
		outputXML = ((Element) obj).getOwnerDocument().getDocumentElement();
		if (null != outputXML) {
			LOG.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
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

	
	/*
	 * Setting AdJuggler URL
	 */
	private void setAdJugglerServerURLInSession() {

		String ajServerKey = "";
		ajServerKey = (String)getWCContext().getSCUIContext().getSession().getAttribute(XPEDXConstants.AJ_SERVER_URL_KEY);
		if (ajServerKey == null || ajServerKey.trim().equals(""))
		{
		String adJuglerServerURLForGivenEnv = "";
		String adJugURL = XPEDXConstants.AJ_SERVER_URL ;
		
		//jira 2890 - TEST was appended to url which is wrong, it should be prepended to aj_kw keyword for dev and staging
		/*
		String custOverridePropertiesFile = "customer_overrides.properties";
		XPEDXWCUtils.loadXPEDXSpecficPropertiesIntoYFS(custOverridePropertiesFile);
		String adJugglerSuffix = YFSSystem.getProperty(XPEDXConstants.AD_JUGGLER_SUFFIX_PROP);
		XPEDXConstants.logMessage("adJugglerSuffix : yfs.xpedx.adjuggler.suffix= " + adJugglerSuffix );
		
		if(adJugglerSuffix != null )
			adJuglerServerURLForGivenEnv = adJugURL + adJugglerSuffix.trim();
		else
			adJuglerServerURLForGivenEnv = adJugURL;
			*/
		adJuglerServerURLForGivenEnv = adJugURL;
		
		//Setting Aj Server Key
		XPEDXConstants.logMessage( " Setting AJ Server Key : " );
		getWCContext().getSCUIContext().getSession().setAttribute(XPEDXConstants.AJ_SERVER_URL_KEY, adJuglerServerURLForGivenEnv );
		
		//
		XPEDXConstants.logMessage( " AJ Server Key is: " + getWCContext().getSCUIContext().getSession().getAttribute(XPEDXConstants.AJ_SERVER_URL_KEY )) ;
		}

		XPEDXConstants.logMessage( " Retriving AJ Server Key : " + getWCContext().getSCUIContext().getSession().getAttribute(XPEDXConstants.AJ_SERVER_URL_KEY )) ;
		
	}
	
	public String execute() {
		String massagedPromoHtml = null;
		try {
			
			String callerPage = getCallerPage();
			String newCallerPageTag = "";
			
			log.debug("######################################################################################################### "  );
			XPEDXConstants.logMessage("---- callerPage  --------- " + callerPage );
			XPEDXConstants.logMessage("---- getCategoryPath  --------- " + getCategoryPath() );
			log.debug("######################################################################################################### "  );
			
			
			//Look for input parameters for this action class.
			validateDynPromoActionParameter(callerPage);
		

			// for guest user fetches the default html page.
			if(wcContext.isGuestUser()){
				setAdJugglerServerURLInSession( );
				
				newCallerPageTag = STD_PRE_LOGIN_PREFIX + callerPage;
				massagedPromoHtml = generatePreLoginPromoFile(newCallerPageTag);
				logMessage("EXECUTE METHOD CALLED (Pre Login page) : " + massagedPromoHtml);
				log.debug("Promotions file Pre-Login : " + newCallerPageTag);
				setInRequest(massagedPromoHtml);
				int imageCount = getPromoImageCount(massagedPromoHtml);
				
				wcContext.setWCAttribute("imageCounter", imageCount, WCAttributeScope.REQUEST);
				return SUCCESS;
			}
		else{
			String massagedPostLoginPromoHtml = "";
			
			 newCallerPageTag = STD_POST_LOGIN_PREFIX + callerPage;
			boolean doesFileBuilt =  generatePostLoginPromoPageName (newCallerPageTag);	
		
			if(doesFileBuilt)			
				{
				massagedPostLoginPromoHtml = getGeneratedFileFullPath();
				logMessage("EXECUTE METHOD CALLED (Post Login page) : " + massagedPostLoginPromoHtml);
				String finalFilePath = getGeneratedFileFullPath();
				setInRequest(finalFilePath);
				int imageCount = getPromoImageCount(finalFilePath);
				
				wcContext.setWCAttribute("imageCounter", imageCount, WCAttributeScope.REQUEST);
				return SUCCESS;
				}
			else
				return ERROR;
			
		}
			
		} catch (Exception ex) {
			log.error(ex.getMessage());			
			return ERROR;
		}
		//return SUCCESS;
	}
	


	/**
	 * 
	 * @param callerPage
	 * @throws Exception
	 */
	private void validateDynPromoActionParameter(String callerPage) throws Exception {
		boolean isValidCallerPage = true ;
		String errorMessage = "";
		
		logMessage("validateDynPromoActionParameter FirstLevel of Validation : " + callerPage);
		//Validate Action Parameter sent from JSP.
		if (callerPage == null || callerPage.trim().length() == 0 )
		{
			logMessage("validateDynPromoActionParameter SecondLevel of Validation : " + callerPage);
			isValidCallerPage = false ;
			errorMessage = "CallerPage name sent as parameter, It is either null or empty : "  + callerPage  ;
		}
		else if( !CALLER_REQ_PROMO_PARAM_VECTOR.contains( callerPage) )
		{
			isValidCallerPage = false ;
			errorMessage = " callerPage name sent as parameter is  : " + callerPage + " And could not match with possible page names (given below): \n" 
			 + CALLER_REQ_PROMO_PARAM_VECTOR.toString() ;
		}
		
		if(isValidCallerPage == false )
			throw new Exception ("ERROR-INVALID PROMO PARAM : ( validateDynPromoActionParameter ) --->\n " + errorMessage);
	}

	/**
	 * 
	 * @param callerPage
	 * @return
	 */
	private String generatePromoFileWithPath (String callerPage ) {
		
		String newCallerPageTag  = "";
		boolean doesFileBuilt = false;
		
		
		if ( isGuestUser() == true ) //then validate only for SignInTop, SignInBottom, preLoginCatalogPage
			{
			newCallerPageTag = STD_PRE_LOGIN_PREFIX + callerPage;
			doesFileBuilt = generatePreLoginPromoPageName (newCallerPageTag);	
			}
		else //Validate for HomePage, CatalogPage, if not found look for Default pages
			{
			newCallerPageTag = STD_POST_LOGIN_PREFIX + callerPage;
			doesFileBuilt =  generatePostLoginPromoPageName (newCallerPageTag);	
			}
		
		if(doesFileBuilt)			
			return getGeneratedFileFullPath();
		else
			return "";
	}
	/**
	 * Logs the messages for debug and other purpose.
	 * 
	 * @param source
	 * @param generatedPromoFileName
	 */
	private void logGeneratedFileNames(String source) {
		logMessage(  "\nGenerated-FileNames - (" + source + ") --> File Name : " + getGeneratedFileName() + "\nFileNamePath : " + getGeneratedFileFullPath() );
		
		if( getGeneratedFileName() == null || "".equalsIgnoreCase(getGeneratedFileName() ) )
			logMessage("ERROR- (" + source + ") --> Generated File Name is null or empty " );
	}
	
	

	/**
	 * generatePreLoginPageNames
	 * 
	 * @param newCallerPageTag
	 * @return
	 */
	private boolean generatePreLoginPromoPageName(String newCallerPageTag) {
		
		logMessage("PRE-LOGIN-PROMO - generatePreLoginPromoPageName -- " );
		String buildFileNameForPreLoginPage = "";
		boolean isFileBuild = false;
		
		try {
			/**
			 * PreLogin SignIn Page - Top (rotational image)
			 * 		File name Format: <Storefront>_SPT_Promo.html 
			 * 		There is no default if file is not found (--NO--).
			 */
			if( XPEDX_PROMO_RESP_PRE_LOGIN_SIGNIN_PAGE_TOP.equalsIgnoreCase(newCallerPageTag ) ) {
				buildFileNameForPreLoginPage = getStoreFrontId() + "_SPT_" + STD_PROMO_HTML_SUFFIX ;
				
			}
			
			/**
			 * SignIn Page - Bottom (non rotational image)
			 * 		File name Format : <Storefront>_SPB_Promo.html 
			 * 		There is no default if file is not found (--NO--).
			 */
			if( XPEDX_PROMO_RESP_PRE_LOGIN_SIGNIN_PAGE_BOTTOM.equalsIgnoreCase(newCallerPageTag ) ) {
				buildFileNameForPreLoginPage = getStoreFrontId() + "_SPB_" + STD_PROMO_HTML_SUFFIX ;
				
			}	

			/**
			 * Catalog Page - guest user
			 * 		File name: <Storefront>_AP_Promo.html
			 * 		There is no default if file is not found (--NO--). 
			 */
			if( XPEDX_PROMO_RESP_PRE_LOGIN_CATALOG_PAGE.equalsIgnoreCase(newCallerPageTag ) ) {
				buildFileNameForPreLoginPage = getStoreFrontId() + "_AP_" + STD_PROMO_HTML_SUFFIX ;
				
			 }
			
			//This sets two variables for one with filename, other with fullpath.
			isFileBuild = setGeneratedFileName(buildFileNameForPreLoginPage  );
			logMessage(" PRE-LOGIN-PROMO : " + buildFileNameForPreLoginPage);
			logMessage(" PRE-LOGIN-PROMO : " + getGeneratedFileFullPath());
			logGeneratedFileNames("PRE-LOGIN-PROMO") ;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return isFileBuild;
	}

	
	/**
	 * generatePostLoginPageNames
	 * 
	 * @param newCallerPageTag
	 * @return
	 */
	private boolean generatePostLoginPromoPageName(String newCallerPageTag) {
		String buildFileNameForPostLoginPage = "";
		boolean isPostLoginFileBuild = false;
		boolean isFileExists = false;
		boolean goForDefault = false ;
		boolean goForCurrentCatDefault = false ;
		String msgFileInfo  ="";
		
		try {
			initializeAdditionalPropeties ();
			logMessage("POST-LOGIN-PROMO - generatePostLoginPromoPageName -- : " + getDivision().length() + " , IL : " + getIndustry().length()  );
			
			/**
			* Home Page
			* 		File name: <Storefront>_HP_<ShipFromDivision>_<CustomerSegment>_Promo.html
			* 		If file not found, default to <Storefront>_HP_Promo.html
			*/
			if( XPEDX_PROMO_RESP_POST_LOGIN_HOME_PAGE.equalsIgnoreCase(newCallerPageTag ) ) {
				
				if ( getStoreFrontId().length() > 0 && getDivision().length() > 0 && getIndustry().length() > 0  )
					{
					buildFileNameForPostLoginPage = getStoreFrontId() + "_HP_" + getDivision() + "_" + getIndustry() + "_" + STD_PROMO_HTML_SUFFIX ;
					}
					msgFileInfo = " Initial File Name : " + buildFileNameForPostLoginPage;
					isPostLoginFileBuild = setGeneratedFileName(buildFileNameForPostLoginPage );
					
					if(isPostLoginFileBuild == true )
					{
						String promoHtmlWithMarketingPromoPath = XPEDX_MARKETING_PROMOTIONS_FILES_PATH + isPostLoginFileBuild;
						isFileExists = XPEDXFileManager.checkFile(promoHtmlWithMarketingPromoPath, this.wcContext, false);
					
					}
					
					if(isPostLoginFileBuild == false ||  isFileExists == false)
						goForDefault=true ;
					
					msgFileInfo = msgFileInfo + " , isFileExists : " + isFileExists + " , goForDefault flag : " + goForDefault ;
					
				
				if (goForDefault == true ) {
					setDefaultFileUsed(goForDefault);
					buildFileNameForPostLoginPage = getStoreFrontId() + "_HP_" + STD_PROMO_HTML_SUFFIX ;
					
					msgFileInfo  = msgFileInfo + " , " + " Default File Name  : " + buildFileNameForPostLoginPage ;
					
					//Just to get copy latest file from external folder to WL path
					simpleFileCheck(buildFileNameForPostLoginPage);
					
					//This sets two variables for one with filename, other with fullpath.
					isPostLoginFileBuild = setGeneratedFileName(buildFileNameForPostLoginPage );
				
				}
				log.debug( msgFileInfo );
			}
			
			goForDefault=false ;
			goForCurrentCatDefault = false;
			/**
			* Catalog Page - registered user
			* 		File name: <Storefront>_CP_<Cat1 of first item>_Promo.html
			* 		If file not found, default to <Storefront>_CP_Promo.hmtl
			*/
			
			if( XPEDX_PROMO_RESP_POST_LOGIN_CATALOG_PAGE.equalsIgnoreCase(newCallerPageTag ) ) {
				isPostLoginFileBuild = false;
				buildFileNameForPostLoginPage  = "";
					msgFileInfo = "Catalog Category " + getCategory() + " , FistItemIn Category : "  + getFirstItemInCategory();
				
					/*				
				if (  getFirstItemInCategory() != null && getFirstItemInCategory().length() > 0  )
					{
					buildFileNameForPostLoginPage = getStoreFrontId() + "_CP_"+ getFirstItemInCategory() + "_" + STD_PROMO_HTML_SUFFIX ;
					}
					msgFileInfo = msgFileInfo + ",  Initial File Name : " + buildFileNameForPostLoginPage ;
					isPostLoginFileBuild = setGeneratedFileName( buildFileNameForPostLoginPage );
					
					if(isPostLoginFileBuild == true )
					{	
						String promoHtmlWithMarketingPromoPath = XPEDX_MARKETING_PROMOTIONS_FILES_PATH + buildFileNameForPostLoginPage;
						isFileExists = XPEDXFileManager.checkFile(promoHtmlWithMarketingPromoPath, this.wcContext, false);
					}
					*/
					
					//Go for Catalog Current Category Default page
					//	File name: <Storefront>_CP_<Cat1 of first item>_Promo.html
					if(isPostLoginFileBuild == false ||  isFileExists == false)
						goForCurrentCatDefault=true ;
					
					msgFileInfo = msgFileInfo + " , isFileExists : " + isFileExists + " , goForCurrentCatDefault flag : " + goForCurrentCatDefault ;
				
					if (goForCurrentCatDefault == true ) {
						
						buildFileNameForPostLoginPage = getStoreFrontId() + "_CP_" + getCurrentCategoryName() + "_" +STD_PROMO_HTML_SUFFIX ;
						
						msgFileInfo = msgFileInfo + " , " + " -Catalog Current Category Default- File Name  : " + buildFileNameForPostLoginPage ;
						
						//Just to get copy latest file from external folder to WL path
						simpleFileCheck(buildFileNameForPostLoginPage);
						
						//This sets two variables for one with filename, other with fullpath.
						isPostLoginFileBuild = setGeneratedFileName( buildFileNameForPostLoginPage );
					}
					
					if(isPostLoginFileBuild == true )
					{	
						String promoHtmlWithMarketingPromoPath = XPEDX_MARKETING_PROMOTIONS_FILES_PATH + buildFileNameForPostLoginPage;
						isFileExists = XPEDXFileManager.checkFile(promoHtmlWithMarketingPromoPath, this.wcContext, false);
					}
					
					//Go for Catalog Home page Default
					if(isPostLoginFileBuild == false ||  isFileExists == false)
						goForDefault=true ;
					
					msgFileInfo = msgFileInfo + " , isFileExists : " + isFileExists + " , goForDefault flag : " + goForDefault ;
				
					
					
					
					if (goForDefault == true ) {
					setDefaultFileUsed(goForDefault);
					buildFileNameForPostLoginPage = getStoreFrontId() + "_CP_" + STD_PROMO_HTML_SUFFIX ;
					
					msgFileInfo = msgFileInfo + " , " + " Default File Name  : " + buildFileNameForPostLoginPage ;
					
					//Just to get copy latest file from external folder to WL path
					simpleFileCheck(buildFileNameForPostLoginPage);
					
					//This sets two variables for one with filename, other with fullpath.
					isPostLoginFileBuild = setGeneratedFileName( buildFileNameForPostLoginPage );
					
				}
				log.debug( msgFileInfo );
			}
			msgFileInfo = msgFileInfo + " , " + " getGeneratedFileFullPath ()  : " + getGeneratedFileFullPath() ;
			
			XPEDXConstants.logMessage(" =================== " );
			XPEDXConstants.logMessage(" POST-LOGIN-PROMO : msgFileInfo : " + msgFileInfo );
			XPEDXConstants.logMessage(" =================== " );
			logMessage(" POST-LOGIN-PROMO : " + buildFileNameForPostLoginPage);
			logMessage(" POST-LOGIN-PROMO : " + getGeneratedFileFullPath());
		
			//Log file names
			logGeneratedFileNames("POST-LOGIN-PROMO" ) ;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return isPostLoginFileBuild;
	}

	
	/**
	 * Initialize variables for pre login stage.
	 * 	callerPage, 
	 * isGuestUser , StoreFront set for Pre login stage ;
	 */
	private void initializePropeties() {
		//Set StoreFrontID.
		String sfid =   wcContext.getStorefrontId() ;
		if (! YFCCommon.isVoid(sfid) )
			setStoreFrontId(sfid);
		else
			logMessage("Strore frontID is missing for DYN_PROMOTIONS ");
			
		//Set Login Status
		if(wcContext.isGuestUser())
			setGuestUser(true );
		else
			setGuestUser(false );
			
		logAllVariables(  "initializePropeties" ) ;
		setStoreFrontId(sfid);
	}
	

	
	/**
	 * Dumps all properties values.
	 * 
	 * @param string
	 */
	private void logAllVariables(String source) {
		
		
		
		StringBuffer msgInfo = new StringBuffer();
		
		msgInfo.append("\n ---------------- Variables Initialized/Used ( " + source + "  ) ------------------- " );
		
		msgInfo.append("\n #### Action Params :" );
		msgInfo.append( "\n SRVR-DYN_PROMO_DIR_PATH : " + XPEDX_MARKETING_PROMOTIONS_FILES_PATH ) ; 
		
		msgInfo.append( "\n\n #### ACTION-PARAMS : ");
		msgInfo.append( "\n CallerPage : " 				+ getCallerPage() ) ; 
		msgInfo.append( "\n category : " 				+ getCategory() ) ; 
		
		msgInfo.append( "\n\n #### PRE-LOGIN-STAGE : ");
		msgInfo.append( "\n isGuestUser: " 				+ isGuestUser ()) ; 
		msgInfo.append( "\n storeFrontId: " 			+ getStoreFrontId ()) ; 
		
		msgInfo.append( "\n\n #### POST-LOGIN-STAGE : ");
		msgInfo.append( "\n CustomerId : " 				+ getCustomerId() ) ; 
		msgInfo.append( "\n OrganizationCode : " 		+ getOrganizationCode() ) ; 
		msgInfo.append( "\n Industry : " 				+ getIndustry() ) ; 
		msgInfo.append( "\n division : " 				+ getDivision() ) ; 
		
		msgInfo.append( "\n\n #### GENERATED-FILE-NAMES : ");
		msgInfo.append( "\n Generated File Name : " 	+ getGeneratedFileName() ) ; 
		
		msgInfo.append( "\n\n #### Final File Set for JSP : ");
		msgInfo.append( "\n Generated File FullPath : " + getGeneratedFileFullPath() ) ; 
		
		msgInfo.append( "\n\n #### IS-DEFAULT-FILE-USED :");
		msgInfo.append( "\n isDefaultFileUsed : " 		+ isDefaultFileUsed() ) ; 
		
		msgInfo.append("\n----------------------------- ----  --------------------- ------------------- " );
		
		logMessage( msgInfo.toString() );
	}

	/**
	 * Initialize variables for post login stage.
	 * 	CustomerID,  OrganizationCode, Division , Industry set for Post login stage ;
	 */
	private void initializeAdditionalPropeties() {
		logMessage(" initializeAdditionalPropeties ");
		initializePropeties() ;
		
		String custId = getCustomerId();
		String orgCode = getOrganizationCode();
		
		XPEDXShipToCustomer shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		// if the customerId is not found in parameter fetch from the context.
		if (YFCCommon.isVoid(custId)){
			custId = wcContext.getCustomerId();
			if (YFCCommon.isVoid(custId)){
				throw new YFCException("CustomerId is null.");
			}
			setCustomerId (custId );
		}

		// if the orgCode is not found in parameter fetch from the context.
		if (YFCCommon.isVoid(orgCode)){
			orgCode = wcContext.getStorefrontId();
			if (YFCCommon.isVoid(orgCode)){
				throw new YFCException("Organization Code is null.");
			}
			setOrganizationCode(orgCode);
		}
		
		try {
		// set the instance variable with the value of the ship from division
			if(!YFCCommon.isVoid(shipToCustomer.getExtnShipFromBranch())){
				setDivision(shipToCustomer.getExtnShipFromBranch().trim());
			}
			
		else
			setDivision("");
			
		} catch (Exception e) {
			setDivision("");
		}
		
		try {
			// set the instance variable with the value of the customer's industry
			if(!YFCCommon.isVoid(shipToCustomer.getExtnIndustry())){
				setIndustry(shipToCustomer.getExtnIndustry().trim());
			}
			else
				setIndustry("");
			
		} catch (Exception e) {
			setIndustry("");
		}
		
		logAllVariables(  "initializeAdditionalPropeties" ) ;
	}
	/**
	 * This is to delete unwanted extra chars.
	 * make sure proper file separator is there, doesn't have semicolons, colons, commas...etc., 
	 */

	
	private String massageFileName(String promoFileBeforeProcess ) {
		
		String massagedPromoFile = promoFileBeforeProcess;
		log.debug("promoFileName Before  Massaging ---  " + promoFileBeforeProcess);
		try
		{
		
		if (massagedPromoFile.contains(":")){
			massagedPromoFile = massagedPromoFile.substring(massagedPromoFile.indexOf(SRVR_PATH_SEPARATOR), massagedPromoFile.length());
		}
		if (!massagedPromoFile.startsWith(SRVR_PATH_SEPARATOR)){
			massagedPromoFile = SRVR_PATH_SEPARATOR + massagedPromoFile;
		}
		massagedPromoFile = massagedPromoFile.replace("\\", "/");
		massagedPromoFile = massagedPromoFile.replace("//", "/");
		
		log.debug("promoFileName after Massaging ---  " + massagedPromoFile);
		
		
		} catch (Exception ex) {
			massagedPromoFile = promoFileBeforeProcess;
			log.debug ("Error occured while massaging Promo file" + ex.getMessage() ) ;
		}
		
		return massagedPromoFile ;
	}
	
	

	/**
	 * This method sets as promotion HTML file as Request parameter. 
	 * This is actual URL jsp page refers.
	 * @param promoHtml of type String
	 */
	private void setInRequest(String finalNameWithPath) {
		LIZ_MESSAGE ="PROMO SUCCESS LIZ";
		XPEDXConstants.logMessage("#### " +LIZ_MESSAGE + " ### - FILE-PROMO-SUCCESS - setInRequest + " +
				"\n PROMO HTML FILE-NAME : "  +getGeneratedFileName() + 
				"\n FILE-PATH : " + finalNameWithPath 
				);
		
		wcContext.setWCAttribute(XPEDXConstants.REQUEST_ATTR_PROMO_PAGE_URL, finalNameWithPath, WCAttributeScope.REQUEST);
		log.error("The final name with parameters for dynamic promotions is " + finalNameWithPath);
		logAllVariables("setInRequest");
		logMessage("##################################### "+ LIZ_MESSAGE + " ########################################################### "  );
	}

	
	/**------------------------------------------------------------------------------------------------------------------------------------------------------------
	 *
	 * 
	 * @return
	 * @throws IOException
	 */
	private String generatePreLoginPromoFile(String callerPage) throws IOException {
		String promoHtml="";
		boolean isFileExists;
		promoHtml = constructPreLoginPromoPageName (callerPage );

		//isFileExists = simpleFileCheck(promoHtmlWithMarketingPromoPath);
		String promoHtmlWithMarketingPromoPath = XPEDX_MARKETING_PROMOTIONS_FILES_PATH + promoHtml;
		isFileExists = XPEDXFileManager.checkFile(promoHtmlWithMarketingPromoPath, this.wcContext, false);
		XPEDXConstants.logMessage ( "-DYN-PROMO--  FileName : " + promoHtmlWithMarketingPromoPath + " , isFileExists : " + isFileExists );
		
		String massagedPromoHtml = massagedFileName(promoHtmlWithMarketingPromoPath);
		return massagedPromoHtml;
	}

	/**
	 * 
	 * @param defaultPromoHtml
	 * @return
	 */
	private String massagedFileName(String defaultPromoHtml) {
		if (defaultPromoHtml.contains(":")){
			defaultPromoHtml = defaultPromoHtml.substring(defaultPromoHtml.indexOf(SRVR_PATH_SEPARATOR), defaultPromoHtml.length());
		}		
		if (!defaultPromoHtml.startsWith(SRVR_PATH_SEPARATOR)){
			defaultPromoHtml = SRVR_PATH_SEPARATOR + defaultPromoHtml;
		}
		defaultPromoHtml = defaultPromoHtml.replace("\\", "/");
		defaultPromoHtml = defaultPromoHtml.replace("//", "/");
		return defaultPromoHtml;
	}
	

	/**
	 * 
	 * @param callerPageTag
	 * @return
	 */
	
	private String constructPreLoginPromoPageName(String callerPageTag) {
		
		initializePropeties();
		
		logMessage("PRE-LOGIN-PROMO - generatePreLoginPromoPageName -- " );
		String buildFileNameForPreLoginPage = "";
		
		try {
			/**
			 * PreLogin SignIn Page - Top (rotational image)
			 * 		File name Format: <Storefront>_SPT_Promo.html 
			 * 		There is no default if file is not found (--NO--).
			 */
			if( XPEDX_PROMO_RESP_PRE_LOGIN_SIGNIN_PAGE_TOP.equalsIgnoreCase(callerPageTag ) ) {
				buildFileNameForPreLoginPage = getStoreFrontId() + "_SPT_" + STD_PROMO_HTML_SUFFIX ;
				
			}
			
			/**
			 * SignIn Page - Bottom (non rotational image)
			 * 		File name Format : <Storefront>_SPB_Promo.html 
			 * 		There is no default if file is not found (--NO--).
			 */
			if( XPEDX_PROMO_RESP_PRE_LOGIN_SIGNIN_PAGE_BOTTOM.equalsIgnoreCase(callerPageTag ) ) {
				buildFileNameForPreLoginPage = getStoreFrontId() + "_SPB_" + STD_PROMO_HTML_SUFFIX ;
				
			}	

			/**
			 * Catalog Page - guest user
			 * 		File name: <Storefront>_AP_Promo.html
			 * 		There is no default if file is not found (--NO--). 
			 */
			if( XPEDX_PROMO_RESP_PRE_LOGIN_CATALOG_PAGE.equalsIgnoreCase(callerPageTag ) ) {
				buildFileNameForPreLoginPage = getStoreFrontId() + "_AP_" + STD_PROMO_HTML_SUFFIX ;
				
			 }
			
			/**
			 *  SignIn Page - Side (contact us image)
			 * 		File name Format: <Storefront>_SPS_Promo.html 
			 * 		There is no default if file is not found (--NO--).
			 */
			if( XPEDX_PROMO_RESP_PRE_LOGIN_SIGNIN_PG_SIDE.equalsIgnoreCase(callerPageTag ) ) {
				buildFileNameForPreLoginPage = getStoreFrontId() + "_SPS_" + STD_PROMO_HTML_SUFFIX ;
				
			}
			
			boolean isPreLoginFileBuild = setGeneratedFileName(buildFileNameForPreLoginPage );

			XPEDXConstants.logMessage("buildFileNameForPreLoginPage : " + buildFileNameForPreLoginPage + " , for newCallerPageTag :" + callerPageTag);
			return buildFileNameForPreLoginPage;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * 
	 * @param promoHtml
	 * @return
	 */
	private boolean simpleFileCheck(String promoHtml) {
		
		try {
			String promoHtmlWithMarketingPromoPath = XPEDX_MARKETING_PROMOTIONS_FILES_PATH + promoHtml;
			return XPEDXFileManager.checkFile(promoHtmlWithMarketingPromoPath, this.wcContext, false);
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * ------------------------------------------------------------------------------------------------------------------------------------------------------------
	 * 
	 * @param promoHtml
	 * @return
	 */
	private void logMessage(String message) {
		if(DEBUG_TRACE == true )
			log.debug( message );
		
	}

	

}
