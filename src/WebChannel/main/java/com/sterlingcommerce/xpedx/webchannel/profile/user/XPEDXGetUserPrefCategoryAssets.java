package com.sterlingcommerce.xpedx.webchannel.profile.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXCustomerContactInfoBean;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

@SuppressWarnings("deprecation")
public class XPEDXGetUserPrefCategoryAssets extends WCMashupAction{

	private static final Logger log = Logger.getLogger(XPEDXGetUserPrefCategoryAssets.class);
	private static final String SEARCH_CATALOG_INDEX_MASHUP_ID = "xpedxHomeCatalogPage";
	private static final String customerExtnInfoMashUp = "xpedx-customer-getCustomExtnFieldsInformation";

	public String execute()
	{
		getUserPrefCategory();
		getPrefCategoryAssets();
		return SUCCESS;
	}

	/**
	 * JIRA 243
	 * Modified getCustomerDetails method to consider the mashup to be invoked
	 * so that, we get only the required information - here ExtnCustomerClass.
	 * @param inputItems
	 * @return
	 */
	private void getUserPrefCategory() {
		
		XPEDXCustomerContactInfoBean xpedxCustomerContactInfoBean = (XPEDXCustomerContactInfoBean)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.XPEDX_Customer_Contact_Info_Bean);
		String userPrefCategory = xpedxCustomerContactInfoBean.getExtnPrefCatalog();
//		String userPrefCategory = (String)wcContext.getSCUIContext().getSession().getAttribute(XPEDXConstants.USER_PREF_CATEGORY);
		if(userPrefCategory != null && userPrefCategory.trim().length()>0 && !userPrefCategory.equalsIgnoreCase("None")) {
			this.prefCategory = userPrefCategory;
		}
		else {
			log.error("No preferred Category at User Level for User:"+ customerContactID + " Checking at Customer Level");
			String custPrefCategory = (String)wcContext.getSCUIContext().getSession().getAttribute(XPEDXConstants.CUST_PREF_CATEGORY);
			if(custPrefCategory!=null && custPrefCategory.trim().length()>0 && !custPrefCategory.equalsIgnoreCase("None")) {
				this.prefCategory = custPrefCategory;
			}
			else {
				log.error("No preferred Category at Customer Level for Customer:"+ getWCContext().getCustomerId() + " Setting Preferred Category as PAPER");
				prefCategory = "4000000"; //This the Category ID for Paper Category took it from the staging database
			}
			// all these Database calls are removed and the preferred categories are set to session in the Header Action itself
			/*customerContactID = wcContext.getCustomerContactId();
			try{
				Element OutputDoc1 = prepareAndInvokeMashup("XPEDX-GetUserPreferredCategory");
				Element customerContactElem = XMLUtilities.getChildElementByName(OutputDoc1, "CustomerContact");
				Element ExtnElem = XMLUtilities.getChildElementByName(customerContactElem, "Extn");
				
				userPrefCategory = SCXmlUtils.getAttribute(ExtnElem, "ExtnPrefCatalog");
				if(userPrefCategory.length()<1||userPrefCategory.equalsIgnoreCase("None"))
				{
					String custPrefCategory = (String)wcContext.getSCUIContext().getSession().getAttribute(XPEDXConstants.CUST_PREF_CATEGORY);
					log.error("No preferred Category at User Level for User:"+ customerContactID + " Checking at Customer Level");
					Document outputDoc2 = XPEDXWCUtils.getCustomerDetails(getWCContext().getCustomerId(), getWCContext()
							.getStorefrontId(), customerExtnInfoMashUp);
					Element customerElem = outputDoc2.getDocumentElement();
					Element customerOrganizationExtnEle = XMLUtilities.getElement(customerElem, "Extn");
					
					custPrefCategory = SCXmlUtils.getAttribute(customerOrganizationExtnEle, "ExtnCustomerClass");
					if(custPrefCategory.length()<1)
					{
						log.error("No preferred Category at Customer Level for Customer:"+ getWCContext().getCustomerId() + " Setting Preferred Category as PAPER");
						prefCategory = "Paper";
					}
					else
					{
						this.prefCategory = custPrefCategory;
					}
				}
				else
				{
					this.prefCategory = userPrefCategory;
				}
			}

			catch (Exception e) {
				e.printStackTrace();
				log.error("Error while getting the Preferred Category");
				} */
		}
		
		}
	private void getPrefCategoryAssets()
	{
		getPrefCategoryPathToDisplay();
			Document outDoc = null;//CatalogContextHelper.getCategoryFromCache(wcContext,categoryDepth)
		boolean isPaperEntitled = false;	//Added for JIRA 1969
	        if(outDoc==null){
	            try
	            {	
	            	custId = wcContext.getCustomerId();
	            	outDoc = prepareAndInvokeMashup(SEARCH_CATALOG_INDEX_MASHUP_ID).getOwnerDocument();
				}
	            catch (Exception e)
	            {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}

	        }
	        if(log.isDebugEnabled()){
	        log.debug("Main Categories " + SCXmlUtils.getString(outDoc));
	        }
	        if(outDoc == null){
	            log.error("Exception in reading the Categories  ");
	        }
	        this.mainCatsDoc = outDoc.getDocumentElement();	      
	        
	        Map<String, String> topCategoryMap = (Map<String, String>)XPEDXWCUtils.getObjectFromCache("TopCategoryMap");
	        if (topCategoryMap == null) {
	        	topCategoryMap = new HashMap<String, String>();
		        Element catListRootElement = SCXmlUtil.getChildElement(mainCatsDoc, "CategoryList");
	    	    ArrayList<Element> mainCategoryList = SCXmlUtil.getChildren(catListRootElement,"Category");
	    	    Iterator<Element> catListIter = mainCategoryList.iterator();
	    	    while (catListIter.hasNext()) {
	    	    	Element topLevelCategory = catListIter.next();
	    		    String topCategoryDescription = SCXmlUtil.getAttribute(topLevelCategory, "Description");
	    		    String topCategoryID = SCXmlUtil.getAttribute(topLevelCategory, "CategoryID");
	    		    topCategoryMap.put(topCategoryID, topCategoryDescription);
	    	    }	    	    
	    	    XPEDXWCUtils.setObectInCache("TopCategoryMap", topCategoryMap);	    	    
	        }

	        this.prefCategoryElem = SCXmlUtils.getElementByAttribute(mainCatsDoc, "CategoryList/Category", "CategoryPath",getPrefCategoryPath());
	     //Added For XBT-253
	        if(this.prefCategoryElem != null)
	        {
		        
		        XPEDXWCUtils.setObectInCache("defaultCategoryDesc",this.prefCategoryElem.getAttribute("ShortDescription"));
	        }
	        
	        if(this.prefCategoryElem==null)
		       {
		    	   SortedSet<String> categorySet = new TreeSet<String>();
		    	   Element catListElement = SCXmlUtil.getChildElement(mainCatsDoc, "CategoryList");
		    	   ArrayList<Element> categoryList = SCXmlUtil.getChildren(catListElement,"Category");
		    	   Iterator<Element> catIter = categoryList.iterator();
		    	   while (catIter.hasNext()) {
		    		   Element topLevelCategory = catIter.next();
		    		   String topCategoryDescription = SCXmlUtil.getAttribute(topLevelCategory, "Description");
		    		   //JIRA 1969 : to check if PAper is entitled
		    		   if(topCategoryDescription.equalsIgnoreCase("Paper")){
		    			   isPaperEntitled = true;
		    		   }
		    		   categorySet.add(topCategoryDescription);
		    	   }
		    	   if(isPaperEntitled){
		    		   this.prefCategoryElem = SCXmlUtils.getElementByAttribute(mainCatsDoc, "CategoryList/Category", "Description","Paper"); 
		    		   //Added For XBT-253
		    		  XPEDXWCUtils.setObectInCache("defaultCategoryDesc",this.prefCategoryElem.getAttribute("ShortDescription"));
		    		   wcContext.getSCUIContext().getSession().setAttribute(XPEDXConstants.USER_PREF_CATEGORY,"Paper");
		    	   } else {
		    	    Iterator categoryIter = categorySet.iterator();
			        if(categoryIter.hasNext())
			        {
			        	String prefCategoryDesc = categoryIter.next().toString();
			        	this.prefCategoryElem = SCXmlUtils.getElementByAttribute(mainCatsDoc, "CategoryList/Category", "Description",prefCategoryDesc); 
			        	//Added For XBT-253
			        	XPEDXWCUtils.setObectInCache("defaultCategoryDesc",this.prefCategoryElem.getAttribute("ShortDescription"));  
			        	wcContext.getSCUIContext().getSession().setAttribute(XPEDXConstants.USER_PREF_CATEGORY,prefCategoryDesc);
			        	//String prefCateforyPath =SCXmlUtil.getAttribute(this.prefCategoryElem,"CategoryPath");			        	
			        }
		    	  }
			        
		       }
	     if(log.isDebugEnabled()){
         log.debug(SCXmlUtils.getString(prefCategoryElem));
	     }
	}
	private void getPrefCategoryPathToDisplay()
	{
		try
		{
			Element outputDoc3 = prepareAndInvokeMashup("XPEDX-getPrefCategoryPath");
			 if(log.isDebugEnabled()){
              log.debug(SCXmlUtils.getString(outputDoc3));
			 }
			Element firstChildElem = (Element) outputDoc3.getFirstChild();

			Element categoryElem = firstChildElem;
			prefCategoryPath = SCXmlUtils.getAttribute(categoryElem, "CategoryPath");
			organizationCode = SCXmlUtils.getAttribute(categoryElem, "OrganizationCode");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("Error while getting the Preferred Category Path to get the category Assets");
		}

	}

	public String getCustomerContactID() {
		return customerContactID;
	}

	public void setCustomerContactID(String customerContactID) {
		this.customerContactID = customerContactID;
	}

	public String getPrefCategory() {
		return prefCategory;
	}

	public void setPrefCategory(String prefCategory) {
		this.prefCategory = prefCategory;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}


	public Element getAsset() {
		return asset;
	}

	public void setAsset(Element asset) {
		this.asset = asset;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public String getAssetURL() {
		return assetURL;
	}

	public void setAssetURL(String assetURL) {
		this.assetURL = assetURL;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setPrefCategoryPath(String prefCategoryPath) {
		this.prefCategoryPath = prefCategoryPath;
	}

	public String getPrefCategoryPath() {
		return prefCategoryPath;
	}

	public String getCategoryDepth() {
		return categoryDepth;
	}

	public void setCategoryDepth(String categoryDepth) {
		this.categoryDepth = categoryDepth;
	}

	public Element getMainCatsDoc() {
		return mainCatsDoc;
	}

	public void setMainCatsDoc(Element mainCatsDoc) {
		this.mainCatsDoc = mainCatsDoc;
	}

	public Element getPrefCategoryElem() {
		return prefCategoryElem;
	}

	public void setPrefCategoryElem(Element prefCategoryElem) {
		this.prefCategoryElem = prefCategoryElem;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	private String customerContactID;
	private String prefCategory;
	private String prefCategoryPath;
	private String organizationCode;
	private Element prefCategoryElem;
	private String custId;
	private Element asset;
	private String assetType;
	private String assetURL;
	private String displayName;
	protected Element mainCatsDoc;
	protected String categoryDepth = "2";

}
