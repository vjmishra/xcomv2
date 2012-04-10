package com.sterlingcommerce.xpedx.webchannel.MyItems;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.xpath.XPathExpressionException;

import net.sf.json.JSONObject;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer;
import com.sterlingcommerce.xpedx.webchannel.order.utilities.XPEDXCommerceContextHelper;
import com.sterlingcommerce.webchannel.utilities.UtilBean;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXCustomerContactInfoBean;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXSCXmlUtils;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXBracket;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXItem;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXItemPricingInfo;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXPriceAndAvailability;
import com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXPriceandAvailabilityUtil;
import com.yantra.util.YFCUtils;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfc.util.YFCDate;

@SuppressWarnings("serial")
public class XPEDXMyItemsDetailsAction extends WCMashupAction implements
		ServletResponseAware {

	private static final Logger LOG = Logger
			.getLogger(XPEDXMyItemsDetailsAction.class);
	private Document outDoc;

	public static final String COMMAND_EXPORT_LIST = "export_list";
	public static final String COMMAND_STOCK_CHECK_SEL = "stock_check_sel";
	public static final String COMMAND_STOCK_CHECK_ALL = "stock_check_all";
	protected String ajaxLineStatusCodeMsg = "";
	protected String ajaxDisplayStatusCodeMsg = "";
	protected Document allItemsDoc = null;

	public static final String Get_SAVED_SHARED_LIST_MASHUP_ID = "XPEDXMyItemsDetailsGetSavedShareList";
	private static int itemMax = 200;
	private String command = "";
	private String orderHeaderKey = "";
	private HttpServletResponse response;

	private ArrayList<Element> listOfCarts;
	private ArrayList<Element> savedSharedList;
	private ArrayList<String> itemOrderList;
	private ArrayList upgradeAssociatedItems;
	private ArrayList complimentAssociatedItems;
	//protected HashMap xpedxItemIDUOMToRelatedItemsListMap;
	protected ArrayList<Element> xpedxYouMightConsiderItems;
	protected ArrayList<Element> xpedxPopularAccessoriesItems;
	protected ArrayList<String> xpedxYouMightConsiderItemIds;
	protected ArrayList<String> xpedxPopularAccessoriesItemIds;
	protected HashMap<String, String> inventoryCheckForItemsMap;
	protected List displayUOMs = new ArrayList();
	private ArrayList listOfItems;

	private ArrayList listOfItemsFromsession;
	public ArrayList getListOfItemsFromsession() {
		return listOfItemsFromsession;
	}

	public void setListOfItemsFromsession(ArrayList listOfItemsFromsession) {
		this.listOfItemsFromsession = listOfItemsFromsession;
	}
	private HashMap customerFieldsMap;
	private HashMap customerFieldsDBMap;
	protected Map displayItemUOMsMap;
	private HashMap<String, HashMap<String,String>> skuMap;
	private String customerSku;
	private HashMap<String, String> skuTypeList;

	private Integer pageNumber = 1;
	private String itemCount = "-1";
	private boolean canAddItem = true;
	private boolean canEditItem = true;
	private boolean canSaveItem = true;
	private boolean canDeleteItem = true;
	private boolean canShare = false;
	private boolean editMode = false;
	private boolean itemDeleted = false; 
	private String customerId = "";
	private String itemID = "";
	private String uom = "";
	private String sharePermissionLevel = "";
	private String rbPermissionShared = "";
	private String rbPermissionPrivate = "";
	private String shareAdminOnly = "";
	private String listOwner = "";
	private String listCustomerId = "";
	private String[] itemIds;
	private String[] checkItemKeys;
	private ArrayList<String> enteredUOMs;
	private ArrayList<String> enteredQuantities;
	private HashMap<String, JSONObject> pnaHoverMap;
	private HashMap<String, XPEDXItemPricingInfo> priceHoverMap;
	private HashMap<String, JSONObject> replacementPnaHoverMap;
	private HashMap<String, XPEDXItemPricingInfo> replacementPriceHoverMap;
	protected Map itemOrderMultipleMap;
	protected HashMap xpedxItemIDUOMToReplacementListMap;
	protected HashMap xpedxItemIDToItemExtnMap;
	private HashMap imageMap;
	private String uniqueId = "";
	private String listName = "";
	private String listDesc = "";
	private String listKey = "";
	protected Map itemUOMsMap;
	protected List<XPEDXBracket> displayPriceForUoms = new ArrayList();
	private String pnaItemId;
	private String pnaRequestedUOM;
	private String pnaRequestedQty;
	protected String isBracketPricing;
	protected String isPnAAvailable;
	private String myItemsKey;
	private String priceCurrencyCode;
	public String validateOM;
	public String catagory;
	public boolean validateOrderMul = false;
	public boolean pnaCall;
	public ArrayList<String> itemOrder;
	private Map<String,String> itemOrderMap=new HashMap<String,String>();	
	private Map<String,String> catMap=new HashMap<String,String>();
	public Map<String, String> getCatMap() {
		return catMap;
	}

	public void setCatMap(Map<String, String> catMap) {
		this.catMap = catMap;
	}
	private Map<String,Boolean> validateCheck =new HashMap<String,Boolean>();
	
	public Map<String, Boolean> getValidateCheck() {
		return validateCheck;
	}

	public void setValidateCheck(Map<String, Boolean> validateCheck) {
		this.validateCheck = validateCheck;
	}
	public String validateOMForMultipleItems;
	
	public String getValidateOMForMultipleItems() {
		return validateOMForMultipleItems;
	}

	public void setValidateOMForMultipleItems(String validateOMForMultipleItems) {
		this.validateOMForMultipleItems = validateOMForMultipleItems;
	}
	
	public Map<String, String> getItemOrderMap() {
		return itemOrderMap;
	}

	public void setItemOrderMap(Map<String, String> itemOrderMap) {
		this.itemOrderMap = itemOrderMap;
	}

	public ArrayList<String> getItemOrder() {
		return itemOrder;
	}

	public void setItemOrder(ArrayList<String> itemOrder) {
		this.itemOrder = itemOrder;
	}
	

	public boolean isPnaCall() {
		return pnaCall;
	}

	public void setPnaCall(boolean pnaCall) {
		this.pnaCall = pnaCall;
	}

	public boolean isValidateOrderMul() {
		return validateOrderMul;
	}

	public void setValidateOrderMul(boolean validateOrderMul) {
		this.validateOrderMul = validateOrderMul;
	}

	public String getCatagory() {
		return catagory;
	}

	public void setCatagory(String catagory) {
		this.catagory = catagory;
	}

	public String getValidateOM() {
		return validateOM;
	}

	public void setValidateOM(String validateOM) {
		this.validateOM = validateOM;
	}
	//added for jira 2885
	private  Map<String,String> pnALineErrorMessage=new HashMap<String,String>(); 
	
	
	public Map<String, String> getPnALineErrorMessage() {
		return pnALineErrorMessage;
	}

	public void setPnALineErrorMessage(Map<String, String> pnALineErrorMessage) {
		this.pnALineErrorMessage = pnALineErrorMessage;
	}
	public Map<String,String> baseUOMmap =new HashMap<String,String>();
	

	
	public Map<String, String> getBaseUOMmap() {
		return baseUOMmap;
	}

	public void setBaseUOMmap(Map<String, String> baseUOMmap) {
		this.baseUOMmap = baseUOMmap;
	}

	//2964 Start
	public Map<String, Map<String, String>> getItemIdConVUOMMap() {
		return itemIdConVUOMMap;
	}

	public void setItemIdConVUOMMap(
			Map<String, Map<String, String>> itemIdConVUOMMap) {
		this.itemIdConVUOMMap = itemIdConVUOMMap;
	}
   //2964 End
	protected List bracketsPricingList = null;
	List<String> DataList = new ArrayList();
	private boolean filterBySelectedListChk = false;
	private boolean filterByMyListChk 	= false;
	private boolean filterByAllChk 	= false;
	private String sharePrivateField;
	protected Map<String,Map<String,String>> itemIdsUOMsMap=new HashMap<String,Map<String,String>>();
	protected Map<String,Map<String,String>> itemIdsUOMsDescMap=new HashMap<String,Map<String,String>>();
	//Start 2964
	protected Map<String,Map<String,String>> itemIdConVUOMMap=new HashMap<String,Map<String,String>>();
	//End 2964
	
	protected HashMap<String, String> itemImagesMap = new HashMap<String, String>();
	protected HashMap<String, String> itemDescMap = new HashMap<String, String>();
	protected ArrayList<String> allItemIds = new ArrayList<String>();
	//This includes only My items list items and not other alternate items etc.
	protected ArrayList<String> allMyItemsListItemIds = new ArrayList<String>();
	YFCDate lastModifiedDate = new YFCDate();
	String lastModifiedDateString = "";
	String lastModifiedUserId = "";
	private Document itemcustXrefDoc;
	//Added to remember the filter selection in My items List page.

	private String errorMsg = ""; // for the import class
	
	/*Added  for Webtrends :  to check if "Update Price and Availability button is hit */
	private boolean updatePAMetaTag = false;
	private String strItemIds = "";
	
	private Map<String,Element> descriptionMap=new HashMap<String,Element>();
	private Map<String,Element> masterItemExtnMap=new HashMap<String,Element>();
	
	private String itemDtlBackPageURL="";
	
	public String getItemDtlBackPageURL() {
		return itemDtlBackPageURL;
	}

	public void setItemDtlBackPageURL(String itemDtlBackPageURL) {
		this.itemDtlBackPageURL = itemDtlBackPageURL;
	}
	
	public Map<String, Element> getMasterItemExtnMap() {
		return masterItemExtnMap;
	}

	public void setMasterItemExtnMap(Map<String, Element> masterItemExtnMap) {
		this.masterItemExtnMap = masterItemExtnMap;
	}

	public String getStrItemIds() {
		return strItemIds;
	}

	public void setStrItemIds(String strItemIds) {
		this.strItemIds = strItemIds;
	}

	public String getAjaxLineStatusCodeMsg() {
		return ajaxLineStatusCodeMsg;
	}

	public void setAjaxLineStatusCodeMsg(String ajaxLineStatusCodeMsg) {
		this.ajaxLineStatusCodeMsg = ajaxLineStatusCodeMsg;
	}

	public Map getItemOrderMultipleMap() {

		return itemOrderMultipleMap;
	}

	public void setItemOrderMultipleMap(Map itemOrderMultipleMap) {
		this.itemOrderMultipleMap = itemOrderMultipleMap;
	}

	public ArrayList<String> getItemTypes() {
		ArrayList<String> res = new ArrayList<String>();
		try {
			res.add("XPEDX Item #");
			res.add("Manufacture Item #");
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
		}

		return res;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletResponse getServletResponse() {
		return response;
	}

	private void exportList() {
		try {

			// Get custom fields labels & data
			// get the data in csv format
			String exportData = "Supplier Part Number,Customer Part Number,Quantity,Unit of Measure,Line Level Code,Description\nDM560,428072,2,Carton,,abcdefght";
			StringBuilder sbCSV = new StringBuilder();
			@SuppressWarnings("unused")
			ArrayList<Element> items = getXMLUtils().getElements(
					getOutDoc().getDocumentElement(), "XPEDXMyItemsItems");
			ArrayList<String> itemListEntitled=XPEDXMyItemsUtils.getEntitledItem(getWCContext(),items);
			sbCSV.append("Supplier Part Number,Customer Part Number,Quantity,Unit of Measure,");
			
			
			sbCSV.append("Description,Price,");
			sbCSV.append("Price UOM,Line Acct #,Line PO #,Customer Field 1,Customer Field 2,Customer Field 3");
			
			// START - Display the custom fields
			/*for (Iterator iterator = getCustomerFieldsMap().keySet().iterator(); iterator.hasNext();) {
				sbCSV.append(",");
				String customKey = (String) iterator.next();
				sbCSV.append((String) getCustomerFieldsMap().get(customKey));
			}*/
			// END - Display the custom fields
			sbCSV.append("\n");
			for (Element item : items) {
				String id 		 = item.getAttribute("MyItemsKey");
				String tmpItemId = item.getAttribute("ItemId");
				String customerPartNumber = "";
					
				//get the customer part number
				if(itemcustXrefDoc == null)
					itemcustXrefDoc = XPEDXWCUtils.getXpxItemCustXRefDoc(allMyItemsListItemIds, getWCContext());
				if(itemcustXrefDoc!=null) {
					//Added for 3051
					Element itemXref = XMLUtilities.getElement(itemcustXrefDoc.getDocumentElement(), "XPXItemcustXref[@LegacyItemNumber='"+tmpItemId+"']");
					if(itemXref!=null)
						customerPartNumber = itemXref.getAttribute("CustomerItemNumber");
					//customerPartNumber = XPEDXMyItemsUtils.getCustomerPartNumber(tmpItemId);
				}
					
				// XPEDXMyItemsUtils.encodeStringForCSV(data)
				/*
				sbCSV.append("\"").append(
						XPEDXMyItemsUtils.encodeStringForCSV(item
								.getAttribute("Name"))).append("\"")
						.append(",");
				*/
				sbCSV.append("\"").append(
						XPEDXMyItemsUtils.encodeStringForCSV(item
								.getAttribute("ItemId"))).append("\"").append(
						",");
				
				sbCSV.append("\"").append(
						XPEDXMyItemsUtils.encodeStringForCSV(customerPartNumber)
				).append("\"").append(",");
				
				
				sbCSV.append("").append(
						XPEDXMyItemsUtils.encodeStringForCSV(item
								.getAttribute("Qty"))).append("").append(",");
				String uomId = item.getAttribute("UomId");
				if (uomId.equals("0")) {
					uomId = "";
				}
				
				//Remove the UOM code
				try {
					if(itemListEntitled.contains(tmpItemId)){
						uomId = uomId.substring(2, uomId.trim().length());
					}
				} catch (Exception e) {
				}
				
				sbCSV.append("\"").append(
						XPEDXMyItemsUtils.encodeStringForCSV(uomId)).append(
						"\"").append(",");
				
				if(item.getAttribute("Name").trim().length()>0)
				{
				sbCSV.append("\"").append(
						XPEDXMyItemsUtils.encodeStringForCSV(item
								.getAttribute("Name"))).append("\"")
						.append(",");
				}
				else
				{
					sbCSV.append("\"").append(
							XPEDXMyItemsUtils.encodeStringForCSV(item
									.getAttribute("Desc"))).append("\"")
							.append(",");
				}
				//item.getAttribute("Desc");
				// Get the Avail and price
				String avail = "";
				String price = "";
				String pricingUom = "";
				try {
					if (request.getParameter("avail_" + id) != null) {
						avail = request.getParameter("avail_" + id);
					}
					if (avail.trim().equals("null")) {
						avail = "";
					}
				} catch (Exception e) {
					avail = "";
				}
				try {
					price = request.getParameter("price_" + id);
					pricingUom = request.getParameter("pricingUom_"+id);
					if (price.trim().equals("null")) {
						price = "";
					}
					if (pricingUom.trim().equals("null")) {
						pricingUom = "";
					}
				} catch (Exception e) {
					price = "";
					pricingUom = "";
				}
				XPEDXCustomerContactInfoBean xpedxCustomerContactInfoBean = (XPEDXCustomerContactInfoBean)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.XPEDX_Customer_Contact_Info_Bean);
				String viewPricesFlag = xpedxCustomerContactInfoBean.getExtnViewPricesFlag();
//				String viewPricesFlag =(String) wcContext.getSCUIContext().getSession().getAttribute("viewPricesFlag");
				if(viewPricesFlag!=null && viewPricesFlag.length()>0 && !viewPricesFlag.equalsIgnoreCase("N")){
				// sbCSV.append("\"").append(avail).append("\"").append(",");
					sbCSV.append("\"").append(
							XPEDXMyItemsUtils.encodeStringForCSV(price)).append(
							"\"").append(",");
					//Start- Added for 3051
					if(pricingUom != null && pricingUom.trim().length()>0 && pricingUom.indexOf('_') == 1){
						pricingUom = pricingUom.substring(2, pricingUom.trim().length());
						sbCSV.append("\"").append(
								XPEDXMyItemsUtils.encodeStringForCSV(pricingUom)).append(
										"\"").append(",");
					}
					else{
						sbCSV.append("\"").append(
								XPEDXMyItemsUtils.encodeStringForCSV(pricingUom)).append(
										"\"").append(",");
					}
				//End for 3051
				}
				else
				{
					sbCSV.append(",");// for price value
					sbCSV.append(",");//for price uom value
					
				}
				// START - Display the custom fields
				for (Iterator iterator = getCustomerFieldsDBMap().keySet()
						.iterator(); iterator.hasNext();) {
					String customKey = (String) iterator.next();
					
					if(XPEDXMyItemsUtils.encodeStringForCSV(item.getAttribute((String) getCustomerFieldsDBMap().get(customKey))).length()>0)
					{
					sbCSV.append("\"").append("'"+XPEDXMyItemsUtils.encodeStringForCSV(item.getAttribute((String) getCustomerFieldsDBMap().get(customKey)))+"'").append("\"");
					}
					else
					{
						sbCSV.append(" ");
					}
					sbCSV.append(",");
				}
				// END - Display the custom fields
				sbCSV.append("\n");
			}
			exportData = sbCSV.toString();

			String fileName = "Export_of_" + getListName().replace(" ", "_")
					+ "_" + System.currentTimeMillis() + ".csv";
			StringBuffer attachment = new StringBuffer();

			attachment.append("attachment;filename=").append(fileName);
			response.setContentType("text/csv");
			response.setContentLength((int) exportData.length());
			response.setHeader("Content-disposition", attachment.toString());
			PrintWriter os = response.getWriter();

			os.write(exportData);
			os.flush();
			os.close();

		} catch (Exception e) {
			LOG.error(e.getStackTrace());
		}
	}

	private void setItemDescription()
	{
		if(!SCUtil.isVoid(allItemsDoc)) {
			NodeList itemNodeList=allItemsDoc.getDocumentElement().getElementsByTagName("Item");
			if(itemNodeList != null)
			{
				for(int i=0;i<itemNodeList.getLength();i++)
				{
					Element itemElem=(Element)itemNodeList.item(i);
					String itemId=itemElem.getAttribute("ItemID");
					Element primaryInfoElem=(Element)itemElem.getElementsByTagName("PrimaryInformation").item(0);
					Element extnEle = (Element)itemElem.getElementsByTagName("Extn").item(0);
					masterItemExtnMap.put(itemId, extnEle);
					descriptionMap.put(itemId,primaryInfoElem);
				}
			}
		}
		else {
			if(getListOfItems().size()>0) {
				String customerId = wcContext.getCustomerId();
				String organizationCode = wcContext.getStorefrontId();
				
				Map<String, String> valueMap = new HashMap<String, String>();
				valueMap.put("/Item/CustomerInformation/@CustomerID", customerId);
				valueMap.put("/Item/@CallingOrganizationCode", organizationCode);
				try
				{
					Element input = WCMashupHelper.getMashupInput("xpedxMinimalItemDetails",
							valueMap, wcContext.getSCUIContext());
					
					
					Document inputDoc = input.getOwnerDocument();
					NodeList inputNodeList = input.getElementsByTagName("Or");
					Element inputNodeListElemt = (Element) inputNodeList.item(0);
					for (int i = 0; i < getListOfItems().size(); i++) {
						Document expDoc = YFCDocument.createDocument("Exp").getDocument();
						Element expElement = expDoc.getDocumentElement();
						Element item = (Element) getListOfItems().get(i);
						expElement.setAttribute("Name", "ItemID");
						expElement.setAttribute("Value", item.getAttribute("ItemId"));
						inputNodeListElemt.appendChild(inputDoc.importNode(expElement, true));
					}
					
					String inputXml = SCXmlUtil.getString(input);
					LOG.debug("Input XML: " + inputXml);
					Object obj = WCMashupHelper.invokeMashup("xpedxMinimalItemDetails", input,
							wcContext.getSCUIContext());
					if(obj != null)
					{
						Element outputDoc = ((Element) obj);
						NodeList itemNodeList=outputDoc.getElementsByTagName("Item");
						if(itemNodeList != null)
						{
							for(int i=0;i<itemNodeList.getLength();i++)
							{
								Element itemElem=(Element)itemNodeList.item(i);
								String itemId=itemElem.getAttribute("ItemID");
								Element primaryInfoElem=(Element)itemElem.getElementsByTagName("PrimaryInformation").item(0);
								Element extnEle = (Element)itemElem.getElementsByTagName("Extn").item(0);	
								masterItemExtnMap.put(itemId, extnEle);
								descriptionMap.put(itemId,primaryInfoElem);
							}
						}
						allItemsDoc = outputDoc.getOwnerDocument();
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public String execute() {
		try {
			/* Begin - Changes made by Mitesh Parikh for 2422 JIRA */
			if(!isEditMode())
			{
				setItemDtlBackPageURL((wcContext.getSCUIContext().getRequest().getRequestURL().append("?").append(wcContext.getSCUIContext().getRequest().getQueryString())).toString());
			}
			else
			{
				StringBuffer editListURL=new StringBuffer();
				editListURL.append(wcContext.getSCUIContext().getRequest().getRequestURL().append("?").append("listKey="+getListKey()).append("&").
								   append("listName="+getListName()).append("&").append("listDesc="+getListDesc()).append("&").append("itemCount="+getItemCount()).append("&").
								   append("shareAdminOnly="+getShareAdminOnly()).append("&").append("filterBySelectedListChk="+isFilterBySelectedListChk()).append("&").
								   append("filterByMyListChk="+isFilterByMyListChk()).append("&").append("filterByAllChk="+isFilterByAllChk()).append("&").
								   append("sharePrivateField="+getSharePrivateField()).append("&").append("editMode="+isEditMode()));
								   						
				wcContext.getSCUIContext().getSession().setAttribute("itemDtlBackPageURL", editListURL.toString());
				
			}
			/* End - Changes made by Mitesh Parikh for 2422 JIRA */
			Map<String, Element> out;

			// Init vars
			imageMap = new HashMap<String, String>();			

			setRbPermissionShared("");
			setRbPermissionPrivate(" checked=\"checked\" ");
			if (getSharePermissionLevel().trim().length() == 0) {
				setRbPermissionShared(" checked=\"checked\" ");
				setRbPermissionPrivate("");
			}

			// Set current customer Id
			setCustomerId(getWCContext().getCustomerId());

			//This is the Mashup which gets the items
			out = prepareAndInvokeMashups();
			if (out.values().iterator().next() != null) {
				outDoc = (Document) out.values().iterator().next()
						.getOwnerDocument();
				setListOfItems(getXMLUtils().getElements(
						outDoc.getDocumentElement(), "XPEDXMyItemsItems"));
			}
			
			setAllMyItems();

			// Set the current list of custom fields in the session cache
			/*
			 * HashMap<String, HashMap> tmpCF = new HashMap<String, HashMap>();
			 * tmpCF.put("label", getCustomerFieldsMap()); tmpCF.put("field",
			 * getCustomerFieldsDBMap());
			 * request.getSession().setAttribute("customFields", tmpCF);
			 */
			

			// Process the stock check, PnA calls
			/*if (getCommand().equals(COMMAND_STOCK_CHECK_ALL)) {
				processStockCheck(true);
			} else if (getCommand().equals(COMMAND_STOCK_CHECK_SEL)) {
				processStockCheck(false);
			}*/
			
			// Get the customer fields
			getCustomerDisplayFields();
			
			if (getCommand().equals(COMMAND_EXPORT_LIST)) {
				exportList();
				return "export";
			}
			
			// Get related items and their information
			getRelatedItems();
			
			setItemDescription();
			
			String useCustSku = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.CUSTOMER_USE_SKU);
			if(useCustSku!=null && useCustSku.length()>0)
			{
				setCustomerSku(useCustSku);
				getItemSKUMap();
			}
			
			if(editMode ==  true)
			{
				setSkuTypeList(XPEDXWCUtils.getSkuTypesForQuickAdd(getWCContext()));
			}

			// Get the list of carts for this users
			// Issue #1338 : To improve performance for a list of size 200
		/*	Element resListOfCarts = prepareAndInvokeMashup("draftOrderList");
			@SuppressWarnings("unused")
			ArrayList<Element> res1 = getXMLUtils().getElements(resListOfCarts,
					"/Output/OrderList/Order");
			setListOfCarts(res1);
			Element newCart = getXMLUtils().createDocument("NewCart")
					.getDocumentElement();
			newCart.setAttribute("OrderName", "Create a new cart");
			newCart.setAttribute("OrderHeaderKey", "_CREATE_NEW_");
			getListOfCarts().add(0, newCart); */

			// Get the list of saved shared carts
			processSharedList();

			// Prepare itemOrderlist
			processItemOrderList();

			// Process permissions
			processPermissions();
			
			/* Removing for the changes being made for the performance
			Set<String> itemIdList = new HashSet<String>();
			for (int i = 0; i < getListOfItems().size(); i++) {
				Element item = (Element) getListOfItems().get(i);
				String itemId = item.getAttribute("ItemId");
				itemIdList.add(itemId);
			}
			*/
	
			/*
			 * getting all the items UOMs at the same time using a complex query
			 */
			itemIdsUOMsMap = XPEDXOrderUtils.getXpedxUOMList(
					wcContext.getCustomerId(), allItemIds,
					wcContext.getStorefrontId());

			if (itemIdsUOMsMap != null && itemIdsUOMsMap.keySet() != null) {
				//Get The itemMap From Session For Minicart Jira 3481
				HashMap<String,String> itemMapObj = (HashMap<String, String>) XPEDXWCUtils.getObjectFromCache("itemMap");
				
				ArrayList<String> itemIdsList = new ArrayList<String>();
				itemIdsList.addAll(itemIdsUOMsMap.keySet());
				Iterator<String> iterator = itemIdsList.iterator();
				while (iterator.hasNext()) {
					String itemIdForUom = iterator.next();
					//Added orderMultiple to get OrderMutiple Jira 3481
					String orderMultiple = XPEDXOrderUtils.getOrderMultipleForItem(itemIdForUom);
					Map uommap = itemIdsUOMsMap.get(itemIdForUom);
					Set<Entry<String, String>> set = uommap.entrySet();
					Map<String, String> newUomMap = new HashMap(itemIdsUOMsMap.get(itemIdForUom));
					itemIdConVUOMMap.put(itemIdForUom, newUomMap);
					for (Entry<String, String> entry : set) {
						String uom = entry.getKey();
						String convFactor = (String) uommap.get(entry.getKey());
						long convFac = Math.round(Double
								.parseDouble(convFactor));

						if (1 == convFac) {
							uommap.put(uom, XPEDXWCUtils.getUOMDescription(uom));
						} else {
							// -FXD- adding space between UOM & Conversion
							// Factor
							uommap.put(uom, XPEDXWCUtils.getUOMDescription(uom)
									+ " (" + convFac + ")");
						}

					}
					if(itemMapObj !=null )
					{
						itemMapObj.put(itemIdForUom, orderMultiple);
					}
					itemIdsUOMsDescMap.put(itemIdForUom, uommap);

				}
				//Set itemMap MAP again in session
				XPEDXWCUtils.setObectInCache("itemMap",itemMapObj);
				//set a itemsUOMMap in Session for ConvFactor
				XPEDXWCUtils.setObectInCache("itemsUOMMap",XPEDXOrderUtils.getXpedxUOMList(wcContext.getCustomerId(), getListOfItems(), wcContext.getStorefrontId()));
			}

			/*
			 * This is set in the method setItemDocAndInventoryMap() and the item extn elements are set in the Map xpedxItemIDToItemExtnMap
			 * getAlternativeItems();
			 */
			
			//PNA Call for Replacement Items removed as per JIRA#1357
			//checkPnAForReplacementItems();
			
			setMyItemsImages();
			
			setLastModifiedListInfo();
			
			// Debug
			// setItemCount("150");

			// Debug
			// canShare = true;
			// editMode = true;
			// System.out.print("Stop here");
			// canEditItem = false;

			XPEDXWCUtils.setObectInCache("listOfItemsMap", getListOfItems());
			XPEDXWCUtils.setObectInCache("orderMultipleFromSession", getItemOrderMultipleMap());
			XPEDXWCUtils.setObectInCache("itemConUOM", getItemIdConVUOMMap());
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String pricecheck(){
		try{
		if (getCommand().equals(COMMAND_STOCK_CHECK_ALL)) {
			processStockCheck(true);
		} else if (getCommand().equals(COMMAND_STOCK_CHECK_SEL)) {
			processStockCheck(false);
		}
		}
		catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getStackTrace());
			return ERROR;
		}
		return SUCCESS;
	}
	
	private void setAllMyItems() {
		ArrayList<Element> items = getListOfItems();

		for (Iterator iterator = items.iterator(); iterator.hasNext();) {
			Element item = (Element) iterator.next();
			String itemId = item.getAttribute("ItemId");
			setItemID(itemId);
			
			if(allMyItemsListItemIds.contains(itemId.trim())) {
				continue;
			}
			else {
				allMyItemsListItemIds.add(itemId.trim());
			}
			
		}
	}
	
	@SuppressWarnings("deprecation")
	private void setLastModifiedListInfo() throws CannotBuildInputException {
		String createUserIDStr = "";
		String modifyUserIdStr = "";
		String lastModifiedDateStr = "";
		Document outputDoc;
		Element input = WCMashupHelper.getMashupInput(
				"XPEDXMyItemsList_ListInfo", getWCContext()
						.getSCUIContext());
		//input.setAttribute("CustomerID", getCustomerId());
		input.setAttribute("MyItemsListKey", getListKey());
		Document inputDoc = input.getOwnerDocument();
		Element inXML = inputDoc.getDocumentElement();
		
		Object obj = WCMashupHelper.invokeMashup(
				"XPEDXMyItemsList_CreatedInfo", inXML, getWCContext()
						.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		
		Element xpedxMyItemsListElement = SCXmlUtil.getChildElement(outputDoc.getDocumentElement(), "XPEDXMyItemsList");
		if(xpedxMyItemsListElement!=null){
			modifyUserIdStr = xpedxMyItemsListElement.getAttribute("Modifyuserid");
			if(YFCUtils.isVoid(modifyUserIdStr)){
				createUserIDStr = xpedxMyItemsListElement.getAttribute("Createuserid");
				setLastModifiedUserId(createUserIDStr);
			}else{
				setLastModifiedUserId(modifyUserIdStr);
			}
			lastModifiedDateStr = xpedxMyItemsListElement.getAttribute("Modifyts");
			setLastModifiedDateString(lastModifiedDateStr);
			setLastModifiedDate(YFCDate.getYFCDate(lastModifiedDateStr));
		}
		
	}
	public String getJsonStringForMap(HashMap map){
		String jsonString = "";
		JSONObject jsonObject = JSONObject.fromObject( map );
		jsonString = jsonObject.toString();
		return jsonString;
	}


	private void setMyItemsImages() {
		//int count = 0;
		try {
			XPEDXSCXmlUtils xpedxScxmlUtil = new XPEDXSCXmlUtils();
			if(allItemsDoc == null)
				allItemsDoc = XPEDXOrderUtils.getXpedxMinimalItemDetails(allItemIds, wcContext.getCustomerId(), wcContext.getStorefrontId(), wcContext);
			//ArrayList<Element> itemElements =  SCXmlUtil.getElements(allItemsDoc.getDocumentElement(), "Item");
			for(int i =0; i<allItemIds.size();i++) {
				Element itemElem = SCXmlUtil.getElementByAttribute(allItemsDoc.getDocumentElement(), "Item", "ItemID", allItemIds.get(i));
				String Itemid = SCXmlUtil.getAttribute(itemElem,"ItemID");
				baseUOMmap.put(Itemid,itemElem.getAttribute("UnitOfMeasure"));
				if(itemElem!=null) {
					Element primaryInfo = SCXmlUtil.getChildElement(itemElem, "PrimaryInformation");
					String ImageLocation = xpedxScxmlUtil.getAttribute(primaryInfo, "ImageLocation");
					String ImageID = xpedxScxmlUtil.getAttribute(primaryInfo, "ImageID");
					String itemDescription = xpedxScxmlUtil.getAttribute(primaryInfo, "Description");
					String imageUrl = "/";
					if(ImageLocation!= null && ImageID!=null && ImageLocation!="" && ImageID!="") {
						if(ImageLocation.lastIndexOf("/") == ImageLocation.length()-1)
							imageUrl = ImageLocation+ImageID;
						else
							imageUrl = ImageLocation+"/"+ImageID;
					}else{
						imageUrl = "/xpedx/images/INF_150x150.jpg";
					}
					itemImagesMap.put(allItemIds.get(i), imageUrl);
					if(itemDescription!=null)
						itemDescMap.put(allItemIds.get(i), itemDescription);
				}
				else {
					String imageUrl = "/xpedx/images/INF_150x150.jpg";
					itemImagesMap.put(allItemIds.get(i), imageUrl);
				}
			}
		}catch (Exception e) {
			LOG.error(e.getStackTrace());
			LOG.error("Error getting the all items doc, so setting all the images to default - setMyItemsImages()");
			for(int i =0; i<allItemIds.size();i++) {
				String imageUrl = "/xpedx/images/INF_150x150.jpg";
				itemImagesMap.put(allItemIds.get(i), imageUrl);
			}
		}
	}
	
	public String getImagePath(Element primaryInfo) {
		String imageUrl = "/xpedx/images/INF_150x150.jpg";
		XPEDXSCXmlUtils xpedxScxmlUtil = new XPEDXSCXmlUtils();
		if(primaryInfo!=null) {
			String ImageLocation = xpedxScxmlUtil.getAttribute(primaryInfo, "ImageLocation");
			String ImageID = xpedxScxmlUtil.getAttribute(primaryInfo, "ImageID");
			if(ImageLocation!= null && ImageID!=null && ImageLocation!="" && ImageID!="") {
				if(ImageLocation.lastIndexOf("/") == ImageLocation.length()-1)
					imageUrl = ImageLocation+ImageID;
				else
					imageUrl = ImageLocation+"/"+ImageID;
			}
		}
		return imageUrl;
	}
	
	@SuppressWarnings("unchecked")
	// delete this.
	private void getAlternativeItems() {
		try {
			ArrayList<Element> alItems = getXMLUtils().getElements(
					outDoc.getDocumentElement(), "XPEDXMyItemsItems");
			List items = new ArrayList();

			for (Iterator iterator = alItems.iterator(); iterator.hasNext();) {
				Element item = (Element) iterator.next();
				String itemId = item.getAttribute("ItemId");

				if (item.getAttribute("ItemType").trim().equals("99.00")) {
					continue;
				}

				items.add(itemId);
			}

			itemOrderMultipleMap = XPEDXOrderUtils
					.getOrderMultipleForItems(items);
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
		}
	}

	private void processStockCheck(boolean checkAllItems) throws Exception {

			pnaCall = true;
			String customerId = wcContext.getCustomerId();
			String organizationCode = wcContext.getStorefrontId();
			Map<String , String> uoms = null;
			String uomCode = null;
			String convFact = null;
			String conversion;
			int totalQty;
			int OM;
			// Init some vars
			ArrayList<XPEDXItem> inputItems = new ArrayList<XPEDXItem>();
			
			//Added For Webtrends
			int cntSel =0;
			Map orderMulMap=(Map) XPEDXWCUtils.getObjectFromCache("orderMultipleFromSession");
			Map itemCon = (Map) XPEDXWCUtils.getObjectFromCache("itemConUOM");
			// 1 - Construct the input array for the call
			listOfItemsFromsession = (ArrayList) XPEDXWCUtils.getObjectFromCache("listOfItemsMap");
			ArrayList<String> itemIDList=new ArrayList<String>();
			for (int i = 0; i < listOfItemsFromsession.size(); i++) {
				Element item = (Element) listOfItemsFromsession.get(i);

				// Get some vars
				String id = item.getAttribute("MyItemsKey");
				String itemId = item.getAttribute("ItemId");
				itemIDList.add(itemId);
				itemOrderMap.put(itemId, itemOrder.get(i));
				String itemUom = enteredUOMs.get(i);
				String orderMultiple = (String)orderMulMap.get(itemId);
				uoms = (Map) itemCon.get(itemId);
				convFact = (String) uoms.get(itemUom);
				if(itemUom==null || itemUom.equals(""))
					itemUom = item.getAttribute("UomId");
				//String itemSeqNum = item.getAttribute("ItemSeqNumber");
				String itemLineNum = item.getAttribute("ItemOrder");
				//String itemQty = item.getAttribute("Qty");
				String itemQty = enteredQuantities.get(i);
				if(itemQty==null || itemQty.trim().equals("") || itemQty.equals("0") )
					itemQty = "1";

				totalQty = (Integer.parseInt(convFact)) * (Integer.parseInt(itemQty));
				OM = totalQty % (Integer.parseInt(orderMultiple));
				
				if(OM!= 0){
					validateOrderMul = true;
					validateCheck.put(itemId,validateOrderMul);
				}
				
				boolean addThisItem = checkAllItems;
				if (!checkAllItems) {
					if (ArrayUtils.contains(getCheckItemKeys(), id)) {
						addThisItem = true;
					}
				}

				if (addThisItem) {
					XPEDXItem tmpItem = new XPEDXItem();
					tmpItem.setLegacyProductCode(itemId);
					tmpItem.setRequestedQtyUOM(itemUom);
					//tmpItem.setLineNumber(itemSeqNum);
					tmpItem.setLineNumber(itemLineNum);
					tmpItem.setRequestedQty(itemQty);
					inputItems.add(tmpItem);
					/*Added if loop for webtrends : creating a string of selected 
					itemIds separated with semicolon*/
					if(cntSel==0){
						strItemIds = strItemIds + itemId;
					}else{
						strItemIds = strItemIds + ";"+ itemId;
					}
					cntSel =cntSel +1;
				}

			}
			
			/*Added if loop for Webtrends : for setting a flag to check if "Update Preice and Availability button is hit */
			if(strItemIds.isEmpty()==false){
				updatePAMetaTag = true;
			}
				

			// 2 - Make the call to get the stock data
			if (inputItems.size() > 0) {
				XPEDXPriceAndAvailability pna = XPEDXPriceandAvailabilityUtil
						.getPriceAndAvailability(inputItems);
				setIsPnAAvailable("true");
				//This takes care of displaying message to Users based on ServiceDown, Transmission Error, HeaderLevelError, LineItemError 
				ajaxDisplayStatusCodeMsg  =   XPEDXPriceandAvailabilityUtil.getAjaxDisplayStatusCodeMsg(pna) ;
				setAjaxLineStatusCodeMsg(ajaxDisplayStatusCodeMsg);
				
				if (null == pna || pna.getItems() == null || YFCCommon.isVoid(pna.getTransactionStatus()) ||  pna.getTransactionStatus().equalsIgnoreCase("F")) {
					/*
					 * If the PnA is failure, set the error msg and send success
					 * back to the UI.No matter what the reply is, we are going to
					 * display the modal with the error messages*incase of PnA
					 * failure, the pricing and Availability information will not be
					 * available to the user,*but they can still add the item to the
					 * cart.
					 */
					LOG.error(ajaxDisplayStatusCodeMsg + "PnA failed(TransactionStatus Error");
					
					
					setIsPnAAvailable("false");
				} else if (pna.getHeaderStatusCode() != null && !pna.getHeaderStatusCode().equalsIgnoreCase("00")) {
					LOG.error(ajaxDisplayStatusCodeMsg + "  PnA failed(HeaderStatusCode Error) for ItemID");
					setIsPnAAvailable("false");
				}

				if (pna!=null && pna.getItems()!=null) {
					Vector<XPEDXItem> items = pna.getItems();
					// prepare the information for JSP
					pnaHoverMap = XPEDXPriceandAvailabilityUtil.getPnAHoverMap(items,true);
					Document pricingInfoDoc = XPEDXOrderUtils.getItemDetailsForPricingInfo(itemIDList,wcContext.getCustomerId(), wcContext.getStorefrontId(), wcContext);
					NodeList itemsNode=pricingInfoDoc.getDocumentElement().getElementsByTagName("Item");
					for (int i = 0; i < itemsNode.getLength(); i++) {
					
						Element itemElem=(Element)itemsNode.item(i);
						String itemID = itemElem.getAttribute("ItemID");
						ArrayList<Element> catPath = SCXmlUtil.getElements(itemElem, "CategoryList/Category");
						String Description = catPath.get(0).getAttribute("Description");
						String[] desc = Description.split("/");
						catagory = desc[0];
						catMap.put(itemID,catagory);
					}
					priceHoverMap = XPEDXPriceandAvailabilityUtil.getPricingInfoFromItemDetails(items, wcContext, true,null,false,null);
				}
			} else {
				LOG.warn("No items selected for PNA... bypassing the call.");
			}		
	}

	private void processSharedList() {
		try {
			Element resListOfSavedSharedList = prepareAndInvokeMashup(Get_SAVED_SHARED_LIST_MASHUP_ID);
			ArrayList<Element> res2 = getXMLUtils().getElements(
					resListOfSavedSharedList, "/XPEDXMyItemsListShare");
			setSavedSharedList(res2);

		} catch (Exception e) {
			LOG.error(e.getStackTrace());
		}
	}
	
	protected HashMap getCustomerFieldsMapfromSession(){
		/*HttpServletRequest httpRequest = wcContext.getSCUIContext().getRequest();
        HttpSession localSession = httpRequest.getSession();*/
		XPEDXWCUtils.setSAPCustomerExtnFieldsInCache();
        HashMap customerFieldsSessionMap = (HashMap)XPEDXWCUtils.getObjectFromCache("customerFieldsSessionMap");
        return customerFieldsSessionMap;
	}
	
	protected Document getsapCustExtnFieldsFromSession(){
		/*HttpServletRequest httpRequest = wcContext.getSCUIContext().getRequest();
        HttpSession localSession = httpRequest.getSession();
        Document sapCustExtnFields = (Document)localSession.getAttribute("sapCustExtnFields");*/
		XPEDXWCUtils.setSAPCustomerExtnFieldsInCache();
		Document sapCustExtnFields = (Document)XPEDXWCUtils.getObjectFromCache("sapCustExtnFields");
        return sapCustExtnFields;
	}

	private void setCustomerDisplayFields(Element result) throws Exception{
		
		Element customerOrganizationExtnEle = XMLUtilities
		.getChildElementByName(result, "Extn");
		
		
		String custLineNoFlag = getXMLUtils().getAttribute(
				customerOrganizationExtnEle, "ExtnCustLineAccNoFlag");
		String custPONoFlag = getXMLUtils().getAttribute(
				customerOrganizationExtnEle, "ExtnCustLinePONoFlag");
		//Fix for not showing Seq Number as per Pawan's mail dated 17/3/2011
		/*String custSeqNoFlag = getXMLUtils().getAttribute(
				customerOrganizationExtnEle, "ExtnCustLineSeqNoFlag");*/
		String custField1Flag = getXMLUtils().getAttribute(
				customerOrganizationExtnEle, "ExtnCustLineField1Flag");
		String custField2Flag = getXMLUtils().getAttribute(
				customerOrganizationExtnEle, "ExtnCustLineField2Flag");
		String custField3Flag = getXMLUtils().getAttribute(
				customerOrganizationExtnEle, "ExtnCustLineField3Flag");

		String shipFromDivision = getXMLUtils().getAttribute(
				customerOrganizationExtnEle, "ExtnShipFromBranch");

		if ("Y".equals(custLineNoFlag)) {
			//Reverted back to the earlier logic to read the label from customer profile
			//If no label found, Line Account# is used
			String custLineNoLbl = getXMLUtils().getAttribute(
					customerOrganizationExtnEle, "ExtnCustLineAccLbl");
			getCustomerFieldsDBMap().put("CustLineAccNo", "JobId");
			if (custLineNoLbl != null && custLineNoLbl.trim().length() > 0) {
				getCustomerFieldsMap().put("CustLineAccNo", custLineNoLbl);
			} else {
				getCustomerFieldsMap().put("CustLineAccNo",
						"Line Account#");
			}
			//Fix for showing label as Line Account # as per Pawan's mail dated 17/3/2011
			//getCustomerFieldsMap().put("CustLineAccNo", "Line Account#");
		}
		
		if ("Y".equals(custPONoFlag)) {
			//Fix for showing label as Line PO # as per Pawan's mail dated 17/3/2011
			//getCustomerFieldsMap().put("CustomerPONo", "Customer PO No");
			getCustomerFieldsMap().put("CustomerPONo", "Line PO #");
			getCustomerFieldsDBMap().put("CustomerPONo", "ItemPoNumber");
		}
		
		if ("Y".equals(custField1Flag)) {
			String custField1Lbl = getXMLUtils().getAttribute(
					customerOrganizationExtnEle, "ExtnCustLineField1Label");
			getCustomerFieldsDBMap().put("CustLineField1",
					"ItemCustomField1");
			if (custField1Lbl != null && custField1Lbl.trim().length() > 0)
				getCustomerFieldsMap().put("CustLineField1", custField1Lbl);
			else
				getCustomerFieldsMap().put("CustLineField1",
						"Customer Field 1");
		}

		if ("Y".equals(custField2Flag)) {
			String custField2Lbl = getXMLUtils().getAttribute(
					customerOrganizationExtnEle, "ExtnCustLineField2Label");
			getCustomerFieldsDBMap().put("CustLineField2",
					"ItemCustomField2");
			if (custField2Lbl != null && custField2Lbl.trim().length() > 0)
				getCustomerFieldsMap().put("CustLineField2", custField2Lbl);
			else
				getCustomerFieldsMap().put("CustLineField2",
						"Customer Field 2");
		}

		if ("Y".equals(custField3Flag)) {
			String custField3Lbl = getXMLUtils().getAttribute(
					customerOrganizationExtnEle, "ExtnCustLineField3Label");
			getCustomerFieldsDBMap().put("CustLineField3",
					"ItemCustomField3");
			if (custField3Lbl != null && custField3Lbl.trim().length() > 0)
				getCustomerFieldsMap().put("CustLineField3", custField3Lbl);
			else
				getCustomerFieldsMap().put("CustLineField3",
						"Customer Field 3");
		}
		
		
		//Fix for not showing Seq Number as per Pawan's mail dated 17/3/2011
		/*if ("Y".equals(custSeqNoFlag)) {
			getCustomerFieldsMap().put("CustomerLinePONo",
					"Customer Seq No");
			getCustomerFieldsDBMap().put("CustomerLinePONo",
					"ItemSeqNumber");
		}*/
	}
	
	private void getCustomerDisplayFields() {
		try {
			HashMap<String,String> customerFieldsSessionMap = getCustomerFieldsMapfromSession();
			HashMap<String,String> customerFieldsMap =new HashMap<String,String>();
	        if(null != customerFieldsSessionMap && customerFieldsSessionMap.size() >= 0){
	        	LOG.debug("Found customerFieldsMap in the session");

			setCustomerFieldsMap(new LinkedHashMap());
			setCustomerFieldsDBMap(new LinkedHashMap());
			
			//Getting from session attr Extn Attributes of sap customer
			Element result = getsapCustExtnFieldsFromSession().getDocumentElement();
			
			//Sets the CustomerFieldsMap and CustomerFieldsDBMap with Extn Attr from SAP customer profile.
			setCustomerDisplayFields(result);
			}
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
		}
	}
	
	private void getItemSKUMap() {
		//Fetch all the items in MIL and get their respective SKUs
		if(getListOfItems()==null || getListOfItems().size()==0)
			return;
		setSkuMap(new HashMap<String, HashMap<String,String>>());
		//If sku is for Mpc or Manufacturer item then retrieve them from the Global item Doc
		if(!SCUtil.isVoid(customerSku) && (XPEDXConstants.CUST_SKU_FLAG_FOR_MPC_ITEM.equalsIgnoreCase(customerSku.trim())
				|| XPEDXConstants.CUST_SKU_FLAG_FOR_MANUFACTURER_ITEM.equalsIgnoreCase(customerSku.trim()))) {
			if(allItemsDoc!=null) {
				Element itemList = allItemsDoc.getDocumentElement();
				Iterator<Element> itemIter = SCXmlUtil.getChildren(itemList);
				while(itemIter.hasNext()) {
					HashMap<String, String> itemSkuMap = new LinkedHashMap<String, String>();
					Element itemElement = itemIter.next();
					if(!SCUtil.isVoid(itemElement)) {
						String itemId = SCXmlUtil.getAttribute(itemElement, "ItemID");
						if(!SCUtil.isVoid(itemId) && SCUtil.isVoid(skuMap.get(itemId))) {
							Element primeInfoElem = SCXmlUtil.getChildElement(itemElement, "PrimaryInformation");
							if(primeInfoElem!=null)
							{
								String manufactureItem = primeInfoElem.getAttribute("ManufacturerItem");
								if(manufactureItem!=null && manufactureItem.length()>0)
									itemSkuMap.put(XPEDXConstants.CUST_SKU_FLAG_FOR_MANUFACTURER_ITEM, manufactureItem);
							}
							Element extnElem =  SCXmlUtil.getChildElement(itemElement, "Extn");
							if(extnElem!=null)
							{
								String mpcCode = extnElem.getAttribute("ExtnMpc");
								if(mpcCode!=null && mpcCode.length()>0)
									itemSkuMap.put(XPEDXConstants.CUST_SKU_FLAG_FOR_MPC_ITEM, mpcCode);
							}
						}
						skuMap.put(itemId, itemSkuMap);
					}
				}
			}
		}
		else if(XPEDXConstants.CUST_SKU_FLAG_FOR_CUSTOMER_ITEM.equalsIgnoreCase(customerSku.trim()))
		{
			itemcustXrefDoc = XPEDXWCUtils.getXpxItemCustXRefDoc(allItemIds, getWCContext());
			System.out.println(SCXmlUtil.getString(itemcustXrefDoc));
			if(itemcustXrefDoc!=null) {
				Element itemCustXRefList = itemcustXrefDoc.getDocumentElement();
				ArrayList<Element> itemCustXrefElems = SCXmlUtil.getElements(itemCustXRefList, "XPXItemcustXref");
				if(itemCustXrefElems!=null && itemCustXrefElems.size()>0) {
					Iterator<Element> xrefIter = itemCustXrefElems.iterator();
					while(xrefIter.hasNext()) {
						HashMap<String, String> itemSkuMap = new LinkedHashMap<String, String>();
						Element xref = xrefIter.next();
						String legacyItemNum = xref.getAttribute("LegacyItemNumber");
						if(allItemIds.contains(legacyItemNum.trim())) {
							String customerPartNumber = xref.getAttribute("CustomerItemNumber");
							itemSkuMap.put(XPEDXConstants.CUST_SKU_FLAG_FOR_CUSTOMER_ITEM, customerPartNumber);
						}
						skuMap.put(legacyItemNum, itemSkuMap);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void getRelatedItems() {
		try {

			// Init some variables
			xpedxItemIDUOMToReplacementListMap = new HashMap();
			//xpedxItemIDUOMToRelatedItemsListMap = new HashMap();
			xpedxPopularAccessoriesItems =  new ArrayList<Element>();
			xpedxYouMightConsiderItems = new ArrayList<Element>();
			xpedxYouMightConsiderItemIds = new ArrayList<String>();
			xpedxPopularAccessoriesItemIds = new ArrayList<String>();
			xpedxItemIDToItemExtnMap = new HashMap();
			
			//ArrayList<String> itemIDList = new ArrayList<String>();			
			prepareXPEDXItemAssociation(allMyItemsListItemIds);
			
			// Retrieving all item Information in One call
			setItemDocAndInventoryMap();

				// Get related items
			/*	HashMap hmRelatedItems = XPEDXMyItemsUtils.getRelatedItems(
						itemIdList, getCustomerId(), getWCContext()
								.getStorefrontId(), getWCContext());

				xpedxItemIDUOMToRelatedItemsListMap.put(itemId, hmRelatedItems
						.get(XPEDXMyItemsUtils.GRI_KEY_ALL));
				ArrayList tmpRI = (ArrayList) hmRelatedItems
						.get(XPEDXMyItemsUtils.GRI_KEY_ALL);

				if (!tmpRI.isEmpty()) {
					xpedxItemIDUOMToReplacementListMap.put(itemId, tmpRI);
				} */

		} catch (Exception e) {
			LOG.error(e.getStackTrace());
		}
	}
	
	private void setItemDocAndInventoryMap() {
		
		//setting all the item Id lists
		setAllItemIdsOfList();
		/*
		 *
			XPEDXWCUtils xPEDXWCUtils = new XPEDXWCUtils();
			String customerDivision = (String)wcContext.getWCAttribute(XPEDXConstants.SHIP_FROM_BRANCH);
			String envCode = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.ENVIRONMENT_CODE);
			if(envCode==null || envCode.trim().length()==0)
				envCode = XPEDXWCUtils.getEnvironmentCode(wcContext.getCustomerId());
			Document itemXrefDoc = null;
		 */
		
		// getting the primary information Doc for the related Items also here and setting the Inventory also here for those items
		try {
			allItemsDoc = XPEDXOrderUtils.getXpedxMinimalItemDetails(allItemIds, wcContext.getCustomerId(), wcContext.getStorefrontId(), wcContext);
			//itemXrefDoc = XPEDXOrderUtils.getXpedxItemBranchItemAssociationDetails(allItemIds, wcContext.getCustomerId(), customerDivision, envCode, wcContext);
		} catch (Exception e) {
			LOG.error("Error getting item branch item association.",e);
		}
		//setInventoryCheckForItemsMap(xPEDXWCUtils.checkForInventory(itemXrefDoc,customerDivision,envCode,allItemIds));
		setInventoryAndOrderMultipleMap();
	}
	
	private void setInventoryAndOrderMultipleMap() {
		XPEDXShipToCustomer shipToCustomer=(XPEDXShipToCustomer)XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
		//String customerDivision = (String)wcContext.getWCAttribute(XPEDXConstants.SHIP_FROM_BRANCH);
		//String envCode = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.ENVIRONMENT_CODE);
		String customerDivision =shipToCustomer.getExtnShipFromBranch();
		String envCode = shipToCustomer.getExtnEnvironmentCode();
		if(envCode==null || envCode.trim().length()==0)
			envCode = XPEDXWCUtils.getEnvironmentCode(wcContext.getCustomerId());
		HashMap<String,String> inventoryMap = new HashMap<String, String>();
		HashMap<String,String> orderMultipleMap = new HashMap<String, String>();
		if(allItemIds!=null && xpedxItemIDToItemExtnMap!=null) {
			Iterator<String> itemIdIterator = allItemIds.iterator();
			while(itemIdIterator.hasNext()) {
				String currItemId = itemIdIterator.next();
				ArrayList<Element> itemExtnElement = (ArrayList<Element>) xpedxItemIDToItemExtnMap.get(currItemId);
				if(itemExtnElement!=null && itemExtnElement.size()>0) {
					Element itemExtn = itemExtnElement.get(0);
					if(itemExtn!=null) {
						String division = SCXmlUtil.getAttribute(itemExtn, "XPXDivision");
						String environmentId = SCXmlUtil.getAttribute(itemExtn, "EnvironmentID");
						if(environmentId.equalsIgnoreCase(envCode) && division.equalsIgnoreCase(customerDivision)) {
							String inventoryIndicator = SCXmlUtil.getAttribute(itemExtn, "InventoryIndicator");
							String orderMultiple = SCXmlUtil.getAttribute(itemExtn, "OrderMultiple");
							if (inventoryIndicator.equalsIgnoreCase("W"))
								inventoryMap.put(currItemId, "Y");
							else
								inventoryMap.put(currItemId, "N");
							if (orderMultiple == null || orderMultiple.trim().length() == 0) {
								orderMultiple = "1";
							}
							orderMultipleMap.put(currItemId, orderMultiple);
						}
						else {
							inventoryMap.put(currItemId, "N");
							orderMultipleMap.put(currItemId, "1");
						}
					}
				}
				else {
					inventoryMap.put(currItemId, "N");
					orderMultipleMap.put(currItemId, "1");
				}
			}
		}
		setItemOrderMultipleMap(orderMultipleMap);
		setInventoryCheckForItemsMap(inventoryMap);
	}

	private void setAllItemIdsOfList() {
		//// Preparing the set to retrieve all the Items Information at once.
		Set<String> totalItemIdsSet = new HashSet<String>();
		
		totalItemIdsSet.addAll(allMyItemsListItemIds);
		totalItemIdsSet.addAll(xpedxPopularAccessoriesItemIds);
		totalItemIdsSet.addAll(xpedxYouMightConsiderItemIds);
		
		if(xpedxItemIDUOMToReplacementListMap!=null && !xpedxItemIDUOMToReplacementListMap.isEmpty())
		{
			Set<String> itemIdKeys =  xpedxItemIDUOMToReplacementListMap.keySet();
			Iterator<String> keyIter = itemIdKeys.iterator();
			while(keyIter.hasNext())
			{
				String currentItemId = keyIter.next();
				List<Element> replacementItems =  (ArrayList<Element>) xpedxItemIDUOMToReplacementListMap.get(currentItemId);
				if(replacementItems!=null && replacementItems.size()>0)
				{
					for(Element itemElement: replacementItems)
					{
						String replacementItemId = itemElement.getAttribute("ItemID");
						totalItemIdsSet.add(replacementItemId);
					}
				}
			}
		}
		// Adding them to allitems array list
		allItemIds.addAll(totalItemIdsSet);
	}
	
	private void checkPnAForReplacementItems() {
		try {
			// Init some vars
			ArrayList<XPEDXItem> inputItems = new ArrayList<XPEDXItem>();

			// 1 - Construct the input array for the call
			if(xpedxItemIDUOMToReplacementListMap!=null && !xpedxItemIDUOMToReplacementListMap.isEmpty())
			{
				Set<String> itemIdKeys =  xpedxItemIDUOMToReplacementListMap.keySet();
				Iterator<String> keyIter = itemIdKeys.iterator();
				int i = 0;
				while(keyIter.hasNext())
				{
					String currentItemId = keyIter.next();
					List<Element> replacementItems =  (ArrayList<Element>) xpedxItemIDUOMToReplacementListMap.get(currentItemId);
					if(replacementItems!=null && replacementItems.size()>0)
					{
						for(Element itemElement: replacementItems)
						{
							
							String itemId = itemElement.getAttribute("ItemID");
							Map<String, String> uomList = XPEDXOrderUtils.getXpedxUOMList(getWCContext().getCustomerId(), itemId, getWCContext().getStorefrontId());
							Set<String> keySet =  uomList.keySet();
							String itemUOM = null;
							if(keySet!=null){
								Iterator<String> iter =  keySet.iterator();
								if(iter.hasNext())
									itemUOM = iter.next();
							}
							String itemQty = "1";
							int seqNumber = getListOfItems().size() + i+1;
							String lineNumber = Integer.toString(seqNumber);
							
							XPEDXItem tmpItem = new XPEDXItem();
							tmpItem.setLegacyProductCode(itemId);
							tmpItem.setRequestedQtyUOM(itemUOM);
							tmpItem.setLineNumber(lineNumber);
							tmpItem.setRequestedQty(itemQty);
							inputItems.add(tmpItem);
							
							i++;
						}
					}
				}
				
			}
			// 2 - Make the call to get the stock data
			if (inputItems.size() > 0) {
				XPEDXPriceAndAvailability pna = XPEDXPriceandAvailabilityUtil
						.getPriceAndAvailability(inputItems);
				
				//This takes care of displaying message to Users based on ServiceDown, Transmission Error, HeaderLevelError, LineItemError 
				ajaxDisplayStatusCodeMsg  =   XPEDXPriceandAvailabilityUtil.getAjaxDisplayStatusCodeMsg(pna) ;
				setAjaxLineStatusCodeMsg(ajaxDisplayStatusCodeMsg);
				
				/*				
 				if (inputItems != null && inputItems.size() > 0) {
					if (null == pna || pna.getItems() == null
							|| YFCCommon.isVoid(pna.getTransactionStatus())
							|| pna.getTransactionStatus().equals("F")) {
						//setAjaxLineStatusCodeMsg("Error getting pricing detail: Transaction Failure");
					}
					if (pna.getHeaderStatusCode() != null
							&& !pna.getHeaderStatusCode()
									.equalsIgnoreCase("00")) {
						//setAjaxLineStatusCodeMsg("Error getting pricing detail: HeaderStatusCode Error");
					}
				}
				*/
				
				if (pna != null && pna.getItems()!=null) {
					Vector<XPEDXItem> items = pna.getItems();
					setReplacementPnaHoverMap(XPEDXPriceandAvailabilityUtil
							.getPnAHoverMap(items));
					setReplacementPriceHoverMap(XPEDXPriceandAvailabilityUtil.getPricingInfoFromItemDetails(items, wcContext));
				}
			} else {
				LOG.warn("No items selected for PNA... bypassing the call.");
			}
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
		}
	}
	
	protected void prepareXPEDXItemAssociation(ArrayList<String> itemIDList) throws XPathExpressionException, CannotBuildInputException{
		/*prepareXpedxItemAssociationMap(itemIDList);
		prepareXpedxItemBranchItemAssociationMap(itemIDList);*/
		
		HashMap<String, HashMap<String,ArrayList<Element>>> allAssociatedItemsMap = XPEDXOrderUtils.getXpedxAssociationsForItems(itemIDList, wcContext, editMode);
		if(allAssociatedItemsMap!=null && !allAssociatedItemsMap.isEmpty())
		{
			if(allAssociatedItemsMap.containsKey(XPEDXOrderUtils.REPLACEMENT_ITEMS_KEY))
				xpedxItemIDUOMToReplacementListMap = allAssociatedItemsMap.get(XPEDXOrderUtils.REPLACEMENT_ITEMS_KEY);
			if(allAssociatedItemsMap.containsKey(XPEDXOrderUtils.ITEM_EXTN_KEY))
				xpedxItemIDToItemExtnMap = allAssociatedItemsMap.get(XPEDXOrderUtils.ITEM_EXTN_KEY);
			HashMap<String,ArrayList<Element>> alternateListMap = allAssociatedItemsMap.get(XPEDXOrderUtils.ALTERNATE_ITEMS_KEY);
			if(alternateListMap!=null && !alternateListMap.isEmpty())
			{
				Set altItemKeys = alternateListMap.keySet();
				if(altItemKeys!=null){
					Iterator<String> altKeyIter = altItemKeys.iterator();
					while(altKeyIter.hasNext())
					{
						String key = altKeyIter.next();
						ArrayList<Element> currentItemAltList = alternateListMap.get(key);
						for(Element altItem: currentItemAltList)
						{
							if(!xpedxYouMightConsiderItems.contains(altItem))
								xpedxYouMightConsiderItems.add(altItem);
							String currItemId = SCXmlUtil.getAttribute(altItem, "ItemID");
							if(!SCUtil.isVoid(currItemId) && !xpedxYouMightConsiderItemIds.contains(currItemId))
								xpedxYouMightConsiderItemIds.add(currItemId);
						}
					}
				}
			}
			HashMap<String,ArrayList<Element>> upgradeListMap = allAssociatedItemsMap.get(XPEDXOrderUtils.UPGRADE_ITEMS_KEY);
			if(upgradeListMap!=null && !upgradeListMap.isEmpty())
			{
				Set upItemKeys = upgradeListMap.keySet();
				if(upItemKeys!=null){
					Iterator<String> upKeyIter = upItemKeys.iterator();
					while(upKeyIter.hasNext())
					{
						String key = upKeyIter.next();
						ArrayList<Element> currentItemUpgradeList = upgradeListMap.get(key);
						for(Element upgradeItem: currentItemUpgradeList)
						{
							if(!xpedxYouMightConsiderItems.contains(upgradeItem))
								xpedxYouMightConsiderItems.add(upgradeItem);
							String currItemId = SCXmlUtil.getAttribute(upgradeItem, "ItemID");
							if(!SCUtil.isVoid(currItemId) && !xpedxYouMightConsiderItemIds.contains(currItemId))
								xpedxYouMightConsiderItemIds.add(currItemId);
						}
					}
				}
			}
			HashMap<String,ArrayList<Element>> upSellListMap = allAssociatedItemsMap.get(XPEDXOrderUtils.UP_SELL_ITEMS_KEY);
			if(upSellListMap!=null && !upSellListMap.isEmpty())
			{
				Set upSellItemKeys = upSellListMap.keySet();
				if(upSellItemKeys!=null){
					Iterator<String> upSellKeyIter = upSellItemKeys.iterator();
					while(upSellKeyIter.hasNext())
					{
						String key = upSellKeyIter.next();
						ArrayList<Element> currentItemUSList = upSellListMap.get(key);
						for(Element usItem: currentItemUSList)
						{
							if(!xpedxYouMightConsiderItems.contains(usItem))
								xpedxYouMightConsiderItems.add(usItem);
							String currItemId = SCXmlUtil.getAttribute(usItem, "ItemID");
							if(!SCUtil.isVoid(currItemId) && !xpedxYouMightConsiderItemIds.contains(currItemId))
								xpedxYouMightConsiderItemIds.add(currItemId);
						}
					}
				}
			}
			HashMap<String,ArrayList<Element>> compListMap = allAssociatedItemsMap.get(XPEDXOrderUtils.COMPLEMENTARY_ITEMS_KEY);
			if(compListMap!=null && !compListMap.isEmpty())
			{
				Set compItemKeys = compListMap.keySet();
				if(compItemKeys!=null){
					Iterator<String> compKeyIter = compItemKeys.iterator();
					while(compKeyIter.hasNext())
					{
						String key = compKeyIter.next();
						ArrayList<Element> currentItemCompList = compListMap.get(key);
						for(Element compItem: currentItemCompList)
						{
							if(!xpedxPopularAccessoriesItems.contains(compItem))
								xpedxPopularAccessoriesItems.add(compItem);
							String currItemId = SCXmlUtil.getAttribute(compItem, "ItemID");
							if(!SCUtil.isVoid(currItemId) && !xpedxPopularAccessoriesItemIds.contains(currItemId))
								xpedxPopularAccessoriesItemIds.add(currItemId);
						}
					}
				}
			}
			HashMap<String,ArrayList<Element>> crossSellListMap = allAssociatedItemsMap.get(XPEDXOrderUtils.CROSS_SELL_ITEMS_KEY);
			if(crossSellListMap!=null && !crossSellListMap.isEmpty())
			{
				Set crossSellItemKeys = crossSellListMap.keySet();
				if(crossSellItemKeys!=null){
					Iterator<String> crossSellKeyIter = crossSellItemKeys.iterator();
					while(crossSellKeyIter.hasNext())
					{
						String key = crossSellKeyIter.next();
						ArrayList<Element> currentItemCSList = crossSellListMap.get(key);
						for(Element csItem: currentItemCSList)
						{
							if(!xpedxPopularAccessoriesItems.contains(csItem))
								xpedxPopularAccessoriesItems.add(csItem);
							String currItemId = SCXmlUtil.getAttribute(csItem, "ItemID");
							if(!SCUtil.isVoid(currItemId) && !xpedxPopularAccessoriesItemIds.contains(currItemId))
								xpedxPopularAccessoriesItemIds.add(currItemId);
						}
					}
				}
			}
		}
	}
	
	/*@SuppressWarnings("unchecked")
	protected void prepareXpedxItemBranchItemAssociationMap(ArrayList<String> itemIDList) throws CannotBuildInputException, XPathExpressionException{
		XPEDXWCUtils xPEDXWCUtils = new XPEDXWCUtils();
		String custID = wcContext.getCustomerId();
		String customerDivision = (String)wcContext.getWCAttribute(XPEDXConstants.SHIP_FROM_BRANCH);
		String envCode = XPEDXWCUtils.getEnvironmentCode(wcContext.getCustomerId());
		Document itemBranchItemAssociationDoc = null;
		Document itemBranchDoc = null;
		try {
			itemBranchItemAssociationDoc = XPEDXOrderUtils.getXpedxItemBranchItemAssociationDetails(itemIDList, custID, customerDivision, envCode, wcContext);
			itemBranchDoc = XPEDXOrderUtils.getXpedxItemBranchDetails(itemIDList, custID, customerDivision, envCode, wcContext);
		} catch (Exception e) {
			LOG.error("Error getting item branch item association.",e);
			return;
		}
		if(null == itemBranchItemAssociationDoc){
			LOG.error("No national level item association could be found");
			return;
		}
		
		// to check if the items are in stock. Then store it in a Map for display 
		setInventoryCheckForItemsMap(xPEDXWCUtils.checkForInventory(itemBranchDoc,customerDivision,envCode,itemIDList));
		
		HashMap<String, ArrayList<String>> replacementMap =  new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<String>> alternateMap =  new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<String>> complimentaryMap =  new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<String>> upgradeMap =  new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<String>> relatedMap =  new HashMap<String, ArrayList<String>>();
		ArrayList<String> itemIDListForGetCompleteItemList = new ArrayList<String>();
		//loop through the itemIDList
		for(int i=0;i<itemIDList.size();i++){
			String itemID = itemIDList.get(i);
			List<Element> xPXItemExtnList = XMLUtilities.getElements(itemBranchItemAssociationDoc.getDocumentElement(), "XPXItemExtn[@ItemID='"+itemIDList.get(i)+"']");
			if(xPXItemExtnList == null || xPXItemExtnList.size() <= 0){
				continue;
			}
			//but take only one, since we dont want duplicate elements
			Element xPXItemExtnElement = xPXItemExtnList.get(0);
			Element xPXItemAssociationsListElement = SCXmlUtil.getChildElement(xPXItemExtnElement, "XPXItemAssociationsList");
			//only replacement items will go to the xpedxItemIDUOMToReplacementListMap. Other items will go to the xpedxItemIDUOMToRelatedItemsListMap.
			List<Element> replacementList = XMLUtilities.getElements(xPXItemAssociationsListElement,"XPXItemAssociations[@AssociationType='R']");
			List<Element> alternateList = XMLUtilities.getElements(xPXItemAssociationsListElement,"XPXItemAssociations[@AssociationType='A']");
			List<Element> complementaryList = XMLUtilities.getElements(xPXItemAssociationsListElement,"XPXItemAssociations[@AssociationType='C']");
			List<Element> upgradeList = XMLUtilities.getElements(xPXItemAssociationsListElement,"XPXItemAssociations[@AssociationType='U']");
			
			//prepare the map for replacement
			if(null!=replacementList && replacementList.size() >=0){
				ArrayList<String> replacementAssItemIDList = new ArrayList<String>();
				for(Element replacementItem:replacementList){
					String associatedItemID = SCXmlUtil.getAttribute(replacementItem, "AssociatedItemID");
					if(!YFCCommon.isVoid(associatedItemID)){
						replacementAssItemIDList.add(associatedItemID);
						itemIDListForGetCompleteItemList.add(associatedItemID);
					}
				}
				if(replacementAssItemIDList.size()>0){
					replacementMap.put(itemID, replacementAssItemIDList);
				}
			}
			//prepare the map for alternate and complimentary
			ArrayList<String> relatedAssItemIDList = new ArrayList<String>();
			ArrayList<String> alternateAssItemIDList = new ArrayList<String>();
			//get the alternateList
			if(null!=alternateList && alternateList.size() >=0){
				for(Element alterItemItem:alternateList){
					String associatedItemID = SCXmlUtil.getAttribute(alterItemItem, "AssociatedItemID");
					if(!YFCCommon.isVoid(associatedItemID)){
						relatedAssItemIDList.add(associatedItemID);
						alternateAssItemIDList.add(associatedItemID);
						itemIDListForGetCompleteItemList.add(associatedItemID);
					}
				}
				if(alternateAssItemIDList.size()>0){
					alternateMap.put(itemID, alternateAssItemIDList);
				}
			}			
			//get the complementaryList
			ArrayList<String> complementaryAssItemIDList = new ArrayList<String>();
			if(null!=complementaryList && complementaryList.size() >=0){
				for(Element comItem:complementaryList){
					String associatedItemID = SCXmlUtil.getAttribute(comItem, "AssociatedItemID");
					if(!YFCCommon.isVoid(associatedItemID)){
						relatedAssItemIDList.add(associatedItemID);
						complementaryAssItemIDList.add(associatedItemID);
						itemIDListForGetCompleteItemList.add(associatedItemID);
					}
				}
				if(complementaryAssItemIDList.size()>0){
					complimentaryMap.put(itemID, complementaryAssItemIDList);
				}
			}
			
			//get the upgraded items
			ArrayList<String> upgradeAssItemIDList = new ArrayList<String>();
			if(null!=upgradeList && upgradeList.size() >=0){
				for(Element upgradeItem:upgradeList){
					String associatedItemID = SCXmlUtil.getAttribute(upgradeItem, "AssociatedItemID");
					if(!YFCCommon.isVoid(associatedItemID)){
						relatedAssItemIDList.add(associatedItemID);
						upgradeAssItemIDList.add(associatedItemID);
						itemIDListForGetCompleteItemList.add(associatedItemID);
					}
				}
				if(upgradeAssItemIDList.size()>0){
					upgradeMap.put(itemID, upgradeAssItemIDList);
				}
			}
			//add it to the relatedMap
			if(relatedAssItemIDList.size()>0){
				relatedMap.put(itemID, relatedAssItemIDList);
			}
			
		}//End of for loop
		if(null==itemIDListForGetCompleteItemList || itemIDListForGetCompleteItemList.size()<=0){
			LOG.debug("No branch level associated items.");
			return;
		}
		//remove the duplicate items and call getCompleteItemList
		HashSet hs = new HashSet();
		hs.addAll(itemIDListForGetCompleteItemList);
		itemIDListForGetCompleteItemList.clear();
		itemIDListForGetCompleteItemList.addAll(hs);
		//call getCompleteItemList
		Document itemDetailsListDoc = null;
		try {
			// invoking a different function which will give onyl the entitiled items - 734
			//itemDetailsListDoc = XPEDXOrderUtils.getXpedxMinimalItemDetails(itemIDListForGetCompleteItemList, custID, wcContext.getStorefrontId(), wcContext);
			itemDetailsListDoc = XPEDXOrderUtils.getXpedxEntitledItemDetails(itemIDListForGetCompleteItemList, custID, wcContext.getStorefrontId(), wcContext);
		} catch (Exception e) {
			LOG.error("Exception while getting item details for associated items",e);
			return;
		}
		//prepare the xpedxItemIDUOMToReplacementListMap only when editMode is true
		Set replacementMapKeySet = replacementMap.keySet();
		if(editMode ==  true && replacementMapKeySet!=null){
		Iterator<String> replacementIterator = replacementMapKeySet.iterator();
			while(replacementIterator.hasNext()){
				ArrayList replacementItemsElementList = new ArrayList();
				String itemID = replacementIterator.next();
				ArrayList<String> asscociatedItemIDList = replacementMap.get(itemID);
				for(String assID:asscociatedItemIDList){
					List<Element> itemDetailsList = XMLUtilities.getElements(itemDetailsListDoc.getDocumentElement(),"Item[@ItemID='"+assID+"']");
					if(null==itemDetailsList || itemDetailsList.size()<=0)
						continue;
					replacementItemsElementList.add(itemDetailsList.get(0));
				}
				xpedxItemIDUOMToReplacementListMap.put(itemID, replacementItemsElementList);
			}//end while loop
		}
		
		
		
		//add the complementary and alternate items to the xpedxItemIDUOMToRelatedItemsListMap
		Set relatedMapKeySet = relatedMap.keySet();
		Iterator<String> relatedIterator = relatedMapKeySet.iterator();
		while(relatedIterator.hasNext()){
			ArrayList relatedItemsElementlist = new ArrayList();
			String itemID = relatedIterator.next();
			ArrayList relatedItemsValue = (ArrayList)xpedxItemIDUOMToRelatedItemsListMap.get(itemID);
			ArrayList<String> asscociatedItemIDList = relatedMap.get(itemID);
			for(String assID:asscociatedItemIDList){
				List<Element> itemDetailsList = XMLUtilities.getElements(itemDetailsListDoc.getDocumentElement(),"Item[@ItemID='"+assID+"']");
				if(null==itemDetailsList || itemDetailsList.size()<=0)
					continue;
				relatedItemsValue.add(itemDetailsList.get(0));
				//Form the xpedxYouMightConsiderItems and xpedxPopularAccessoriesItems
				Element currentEntitledItem =  itemDetailsList.get(0);
				ArrayList<String> alternateItemIDList = alternateMap.get(itemID);
				if(alternateItemIDList!=null && alternateItemIDList.size()>0)
				{
					Iterator<String> altItemIter = alternateItemIDList.iterator();
					while(altItemIter.hasNext())
					{
						String altItemId = altItemIter.next();
						if(altItemId.equalsIgnoreCase(currentEntitledItem.getAttribute("ItemID"))){
							if(!xpedxYouMightConsiderItems.contains(currentEntitledItem))
								xpedxYouMightConsiderItems.add(currentEntitledItem);
						}
					}
				}
				
				ArrayList<String> complimentaryItemIDList = complimentaryMap.get(itemID);
				if(complimentaryItemIDList!=null && complimentaryItemIDList.size()>0)
				{
					Iterator<String> compItemIter = complimentaryItemIDList.iterator();
					while(compItemIter.hasNext())
					{
						String compItemId = compItemIter.next();
						if(compItemId.equalsIgnoreCase(currentEntitledItem.getAttribute("ItemID"))){
							if(!xpedxPopularAccessoriesItems.contains(currentEntitledItem))
								xpedxPopularAccessoriesItems.add(currentEntitledItem);
						}
					}
				}
			}
			xpedxItemIDUOMToRelatedItemsListMap.remove(itemID);
			xpedxItemIDUOMToRelatedItemsListMap.put(itemID, relatedItemsValue);
		}//end while loop
		
		
	}*/
	
	/*@SuppressWarnings("unchecked")
	protected void prepareXpedxItemAssociationMap(ArrayList<String> itemIDList) throws XPathExpressionException{
		String custID = wcContext.getCustomerId();
		String callingOrgCode = wcContext.getStorefrontId();
		Document itemAssociationDoc = null;
		//get the getXpedxAssociationlItemDetails
		try {
			itemAssociationDoc = XPEDXOrderUtils.getXpedxAssociationlItemDetails(itemIDList, custID, callingOrgCode, wcContext);
		} catch (CannotBuildInputException e) {
			LOG.error("Error getting National Level item association.",e);
			return;
		}
		if(null == itemAssociationDoc){
			LOG.error("No national level item association could be found");
			return;
		}
		NodeList nItemList = itemAssociationDoc.getElementsByTagName("Item");
		int itemNodeListLength = nItemList.getLength();
		for (int i = 0; i < itemNodeListLength; i++) {
			Element itemElement = (Element)nItemList.item(i);
			String itemID = SCXmlUtil.getAttribute(itemElement, "ItemID");
			if(null !=xpedxItemIDUOMToRelatedItemsListMap && xpedxItemIDUOMToRelatedItemsListMap.containsKey(itemID)){
				LOG.debug("xpedxItemIDUOMToRelatedItemsListMap already contains national level item association for "+itemID);
				continue;
			}
			LOG.debug("Preparing national level association for item Id "+itemID);
			ArrayList relatedItems = new ArrayList();
			Element associationTypeListElem = null;
			associationTypeListElem = XMLUtilities.getElement(itemElement, "AssociationTypeList");
			if (associationTypeListElem != null) {
				List<Element> crossSellElements = XMLUtilities.getElements(associationTypeListElem,"AssociationType[@Type='CrossSell']");
				List<Element> upSellElements = XMLUtilities.getElements(associationTypeListElem,"AssociationType[@Type='UpSell']");
				for (int j = 0; j < crossSellElements.size(); j++) {
					Element associationTypeElem = crossSellElements.get(j);
					Element associationListElem = XMLUtilities.getElement(associationTypeElem, "AssociationList");
					List associationList = XMLUtilities.getChildElements(associationListElem, "Association");
					if (associationList != null&& !associationList.isEmpty()) {
						Iterator associationIter = associationList.iterator();
						while (associationIter.hasNext()) {
							Element association = (Element) associationIter.next();
							Element associateditemEl = XMLUtilities.getElement(association, "Item");
							relatedItems.add(associateditemEl);
							if(!xpedxPopularAccessoriesItems.contains(associateditemEl))
								xpedxPopularAccessoriesItems.add(associateditemEl);
						}
					}
				}//for cross sell
				for (int k = 0; k < upSellElements.size(); k++) {
					Element associationTypeElem = upSellElements.get(k);
					Element associationListElem = XMLUtilities.getElement(associationTypeElem, "AssociationList");
					List associationList = XMLUtilities.getChildElements(associationListElem, "Association");
					if (associationList != null && !associationList.isEmpty()) {
						Iterator associationIter = associationList.iterator();
						while (associationIter.hasNext()) {
							Element association = (Element) associationIter.next();
							Element associateditemEl = XMLUtilities.getElement(association, "Item");
							relatedItems.add(associateditemEl);
							if(!xpedxYouMightConsiderItems.contains(associateditemEl))
								xpedxYouMightConsiderItems.add(associateditemEl);
						}
					}
				}//for upsell
			}//if
			//add relatedItems to the map
			xpedxItemIDUOMToRelatedItemsListMap.put(itemID, relatedItems);
		}
	}*/
	
	@SuppressWarnings("unchecked")
	public String preparePnAjaxData() {
		String pnaItemId = getPnaItemId();
		Document itemDoc;
		String requestedUOM = getPnaRequestedUOM();
		setIsBracketPricing("false");
		setIsPnAAvailable("true");
		try {
			itemDoc = XPEDXOrderUtils.getItemDetails(pnaItemId, wcContext
					.getCustomerId(), wcContext.getStorefrontId(), wcContext);
			LOG.debug("Item Details complete");
			// prepare the MyItemList list
			LOG.debug("MyItemList list prepared");
			Element itemEle = SCXmlUtil.getChildElement(itemDoc
					.getDocumentElement(), "Item");
			if(!(requestedUOM!= null && requestedUOM.trim().length()>0))
				requestedUOM = SCXmlUtil.getAttribute(itemEle, "UnitOfMeasure");
			ArrayList<Element> catPath = SCXmlUtil.getElements(itemEle, "CategoryList/Category");
			String Description = catPath.get(0).getAttribute("Description");
			String[] desc = Description.split("/");
			catagory = desc[0];	
			LOG.debug("Requested UOM: " + requestedUOM);
			//modified for jira 3392
			ArrayList<XPEDXItem> inputItems = getPnAInputDoc(getPnaItemId(),requestedUOM,"1");
			XPEDXPriceAndAvailability pna = XPEDXPriceandAvailabilityUtil.getPriceAndAvailability(inputItems);
			
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
				LOG.error(ajaxDisplayStatusCodeMsg + "PnA failed(TransactionStatus Error) for ItemID: "
						+ pnaItemId);
				//setAjaxLineStatusCodeMsg(getAjaxLineStatusCodeMsg()
				//		+ "-P12-Error getting pricing detail: Transaction Failed\n" + ajaxDisplayStatusCodeMsg);
				
				setIsPnAAvailable("false");
			} else if (!pna.getHeaderStatusCode().equalsIgnoreCase("00")) {
				LOG.error(ajaxDisplayStatusCodeMsg + "  PnA failed(HeaderStatusCode Error) for ItemID: "
						+ pnaItemId);
				//setAjaxLineStatusCodeMsg(getAjaxLineStatusCodeMsg()
					//	+ "Error getting pricing detail: HeaderStatusCode Error.\n");
				setIsPnAAvailable("false");
			}

			if (null == pna || null == pna.getItems()) {
				setIsPnAAvailable("false");
				//setAjaxLineStatusCodeMsg(getAjaxLineStatusCodeMsg()
					//	+ "Could Not retrieve  pricing details for the specified item.\n");
			} else {
				Vector<XPEDXItem> items = pna.getItems();
				// prepare the information for JSP
				pnaHoverMap = XPEDXPriceandAvailabilityUtil.getPnAHoverMap(items);
				HashMap<String,XPEDXItemPricingInfo> priceHoverMap = XPEDXPriceandAvailabilityUtil.getPricingInfoFromItemDetails(items, wcContext);
				if(priceHoverMap!=null && priceHoverMap.containsKey(pnaItemId))
				{
					XPEDXItemPricingInfo itemPriceInfo = priceHoverMap.get(pnaItemId);
					displayPriceForUoms = itemPriceInfo.getDisplayPriceForUoms();
					bracketsPricingList = itemPriceInfo.getBracketsPricingList();
					setIsBracketPricing(itemPriceInfo.getIsBracketPricing());
					setPriceCurrencyCode(itemPriceInfo.getPriceCurrencyCode());
				}
				
				for (XPEDXItem pandAItem : items) {
					if (pandAItem.getLegacyProductCode().equals(pnaItemId)) {
						// set the line status erros mesages if any
						String lineStatusErrorMsg = XPEDXPriceandAvailabilityUtil
								.getPnALineErrorMessage(pandAItem);
						//added for jira 2885 
						if (pna.getHeaderStatusCode().equalsIgnoreCase("00")) {
							pnALineErrorMessage=XPEDXPriceandAvailabilityUtil.getLineErrorMessageMap(pna.getItems());
							if((lineStatusErrorMsg != "") && pnALineErrorMessage.size()>0){
								setIsPnAAvailable("false");
							}
						}
						//end of jira 2885			
					}
				}
			}
			
		} catch (Exception e) {
			LOG.error("Error Getting Item Details and PnA Details for"
					+ pnaItemId, e);
			setIsPnAAvailable("false");
			if(getAjaxLineStatusCodeMsg()==null || getAjaxLineStatusCodeMsg().length()==0)
				setAjaxLineStatusCodeMsg(XPEDXPriceandAvailabilityUtil.WS_PRICEANDAVAILABILITY_WITH_SERVICESTATUSDOWN_ERROR);
			return ERROR;
		}
		return SUCCESS;
	}
	
	/*private void preparePnAPricingInfo(Document itemDoc,
			Vector<XPEDXItem> items, String itemId, String requestedUOM)
			throws Exception {
		Element itemEle = SCXmlUtil.getChildElement(itemDoc
				.getDocumentElement(), "Item");
		Element primaryInfoEle = SCXmlUtil.getChildElement(itemEle,
				"PrimaryInformation");
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
		getItemUOMs(itemId);
		// List<String> displayPriceForUoms = new ArrayList();
		// List<String> bracketsPricingList = null;
		for (XPEDXItem pandAItem : items) {
			if (pandAItem.getLegacyProductCode().equals(itemId)) {
				if (pandAItem.getPriceCurrencyCode() != null
						&& pandAItem.getPriceCurrencyCode().trim().length() > 0) {
					setPriceCurrencyCode(pandAItem.getPriceCurrencyCode());
				}
				else{
					//If price currency code is not set,set the currency code from customer profile
					String custCurrencyCode = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.CUSTOMER_CURRENCY_CODE);
					setPriceCurrencyCode(custCurrencyCode);
				}
				String pricingUOMUnitPrice = pandAItem
						.getUnitPricePerPricingUOM();
				BigDecimal pricingUOMPrice = new BigDecimal(pricingUOMUnitPrice);
				BigDecimal prodWeight = null;
				BigDecimal priceForCWTUom = null;
				BigDecimal priceForTHUom = null;
				
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
				utilBean = new UtilBean();
//				displayPriceForUoms.add(basePrice.toString() + "/"
//						+ BaseUomDesc);								//removed as specified in the bug 185 comments on 03/Jan/11 3:58 PM by Barb Widmer
				displayPriceForUoms.add(new XPEDXBracket(null, PricingUOMDesc, utilBean.formatPriceWithCurrencySymbol(wcContext, priceCurrencyCode,pricingUOMUnitPrice)));
				if (priceForCWTUom != null)
					displayPriceForUoms.add(new XPEDXBracket(null, "CWT", utilBean.formatPriceWithCurrencySymbol(wcContext, priceCurrencyCode,priceForCWTUom.toString())));
				if (priceForTHUom != null)
					displayPriceForUoms.add(new XPEDXBracket(null, "TH", utilBean.formatPriceWithCurrencySymbol(wcContext, priceCurrencyCode, priceForTHUom.toString())));
				
				displayPriceForUoms.add(new XPEDXBracket(null, RequestedQtyUOMDesc, utilBean.formatPriceWithCurrencySymbol(wcContext, priceCurrencyCode, pandAItem.getUnitPricePerRequestedUOM())));
				displayPriceForUoms.add(new XPEDXBracket(null, null, utilBean.formatPriceWithCurrencySymbol(wcContext, priceCurrencyCode, pandAItem.getExtendedPrice())));
				
				// Vector bracketsPricingList = null;
				bracketsPricingList = pandAItem.getBrackets();
				setIsBracketPricing(XPEDXPriceandAvailabilityUtil
						.isBracketPricingAvailable(bracketsPricingList));
				DataList = displayUOMs;
			}
		}
	}*/
	
	
	protected void getItemUOMs(String itemId) throws Exception {
		String customerId = wcContext.getCustomerId();
		String organizationCode = wcContext.getStorefrontId();

		Map<String, String> uoms = null;

		List items = new ArrayList();
		items.add(itemId);

		itemUOMsMap = XPEDXOrderUtils.getXpedxUOMList(customerId, itemId,
				organizationCode);
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
	
	private ArrayList<XPEDXItem> getPnAInputDoc(String pnaItemId,
			String requestedUOM, String lineNumber) {
		ArrayList<XPEDXItem> pnaList = new ArrayList<XPEDXItem>();
		XPEDXItem item = new XPEDXItem();
		item.setLegacyProductCode(pnaItemId);
		item.setLineNumber(lineNumber);
		if (pnaRequestedQty == null) {
			item.setRequestedQty("1");
		} else {
			item.setRequestedQty(pnaRequestedQty);
		}
		item.setRequestedQtyUOM(requestedUOM);
		pnaList.add(item);
		return pnaList;
	}

	public Map<String, String> getUOMList(String customerId, String itemID,
			String organizationCode) {
		// getWCContext().getSCUIContext().getRequest().getContextPath()
		// request.g
		Map<String, String> uoms = null;
		try {
			//Map containing UOMCode as key and ConvFactor as value
			uoms = XPEDXOrderUtils.getXpedxUOMList(customerId, itemID,
					organizationCode);
			displayItemUOMsMap = new HashMap();
			/*for (Iterator it = uoms.keySet().iterator(); it.hasNext();) {
				String uomDesc = (String) it.next();
				Object o = uoms.get(uomDesc);
				displayItemUOMsMap.put(XPEDXWCUtils.getUOMDescription(uomDesc)
						+ " (" + o + ")", o);
			}*/
			for (Iterator it = uoms.keySet().iterator(); it.hasNext();) {
				String uomCode = (String) it.next();
				String convFact = (String)uoms.get(uomCode);
				long convFac = Math.round(Double.parseDouble(convFact));
				
				//Map containing UOMCode as key and Desc ( ConvFactor ) as value
				if(convFac == 1)
				{
					displayItemUOMsMap.put(uomCode, XPEDXWCUtils.getUOMDescription(uomCode));
				}
				else{
					displayItemUOMsMap.put(uomCode, XPEDXWCUtils.getUOMDescription(uomCode)
							+ " (" + convFac + ")");
				}
				
			}
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
		}
		//return uoms;
		return displayItemUOMsMap;
	}

	private boolean isCurrentUserAdmin() {
		boolean res = false;
		try {
			if (ResourceAccessAuthorizer.getInstance().isAuthorized(
					"/swc/profile/ManageUserList", getWCContext())) {
				res = true;
			} else {
				res = false;
			}
		} catch (Exception e) {
			LOG.error(e.toString());
		}

		return res;
	}

	private void processPermissions() {
		try {

			/*
			 * //Can the user edit the list if
			 * (!getSharePermissionLevel().equals
			 * (getWCContext().getLoggedInUserId())){ canShare = false; }
			 * 
			 * if (getShareAdminOnly().equals("Y") && isCurrentUserAdmin()){
			 * //canShare = true; canEditItem = true; }
			 * 
			 * try { String createdUserId =
			 * outDoc.getDocumentElement().getAttribute("Createuserid"); if
			 * (createdUserId.equals(getWCContext().getLoggedInUserId())){
			 * canShare = true; canAddItem = true; } } catch (Exception e) { }
			 */

			// New rules
			if (isCurrentUserAdmin()) {
				canShare = true;
			}
			if (getShareAdminOnly().equals("Y")
					&& isCurrentUserAdmin() == false) {
				canEditItem = false;
			}

			try {
				if (Integer.parseInt(itemCount) >= itemMax) {
					canAddItem = false;
				}
			} catch (Exception e) {
			}

		} catch (Exception e) {
			LOG.error(e.getStackTrace());
		}
	}

	private void processItemOrderList() {
		try {
			setItemOrderList(new ArrayList<String>());
			for (int i = 0; i < getListOfItems().size(); i++) {
				//getItemOrderList().add(i + 1 + " "); commented for jira 2804
				getItemOrderList().add(Integer.toString(i+1));


			}
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
		}
	}

	
	
	public Document getOutDoc() {
		return outDoc;
	}

	public void setOutDoc(Document outDoc) {
		this.outDoc = outDoc;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getOrderHeaderKey() {
		String res = null;

		try {
			/*res = XPEDXCommerceContextHelper
					.getCartInContextOrderHeaderKey(getWCContext());*/
			res=(String)XPEDXWCUtils.getObjectFromCache("OrderHeaderInContext");
		} catch (Exception e) {
		}

		if (res == null) {
			res = "_CREATE_NEW_";
		}

		return res;
	}

	public void setOrderHeaderKey(String orderHeaderKey) {
		this.orderHeaderKey = orderHeaderKey;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public ArrayList<Element> getListOfCarts() {
		return listOfCarts;
	}

	public void setListOfCarts(ArrayList<Element> listOfCarts) {
		this.listOfCarts = listOfCarts;
	}

	public String getItemCount() {
		return itemCount;
	}

	public void setItemCount(String itemCount) {
		this.itemCount = itemCount;
	}

	public ArrayList<Element> getSavedSharedList() {
		return savedSharedList;
	}

	public void setSavedSharedList(ArrayList<Element> savedSharedList) {
		this.savedSharedList = savedSharedList;
	}

	public boolean isCanAddItem() {
		return canAddItem;
	}

	public void setCanAddItem(boolean canAddItem) {
		this.canAddItem = canAddItem;
	}

	public boolean isCanEditItem() {
		return canEditItem;
	}

	public void setCanEditItem(boolean canEditItem) {
		this.canEditItem = canEditItem;
	}

	public boolean isCanSaveItem() {
		return canSaveItem;
	}

	public void setCanSaveItem(boolean canSaveItem) {
		this.canSaveItem = canSaveItem;
	}

	public boolean isCanDeleteItem() {
		return canDeleteItem;
	}

	public void setCanDeleteItem(boolean canDeleteItem) {
		this.canDeleteItem = canDeleteItem;
	}

	public ArrayList<String> getItemOrderList() {
		return itemOrderList;
	}

	public void setItemOrderList(ArrayList<String> itemOrderList) {
		this.itemOrderList = itemOrderList;
	}

	public boolean isEditMode() {
		return editMode;
	}

//Added for Webtrends
	public boolean isUpdatePAMetaTag() {
		return updatePAMetaTag;
	}

	public void setUpdatePAMetaTag(boolean updatePAMetaTag) {
		this.updatePAMetaTag = updatePAMetaTag;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public static int getItemMax() {
		return itemMax;
	}

	public static void setItemMax(int itemMax) {
		XPEDXMyItemsDetailsAction.itemMax = itemMax;
	}

	public String getItemID() {
		String res = itemID;
		try {
			if (res == null) {
				res = "INVALID_ITEM";
			} else {
				if (res.trim().length() == 0) {
					res = "INVALID_ITEM";
				}
			}
		} catch (Exception e) {
			res = "ERROR_INVALID_ITEM";
		}
		return res;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public ArrayList getUpgradeAssociatedItems() {
		return upgradeAssociatedItems;
	}

	public void setUpgradeAssociatedItems(ArrayList upgradeAssociatedItems) {
		this.upgradeAssociatedItems = upgradeAssociatedItems;
	}

	public ArrayList getComplimentAssociatedItems() {
		return complimentAssociatedItems;
	}

	public void setComplimentAssociatedItems(ArrayList complimentAssociatedItems) {
		this.complimentAssociatedItems = complimentAssociatedItems;
	}

	public HashMap getCustomerFieldsMap() {
		return customerFieldsMap;
	}

	public void setCustomerFieldsMap(HashMap customerFieldsMap) {
		this.customerFieldsMap = customerFieldsMap;
	}

	public HashMap getCustomerFieldsDBMap() {
		return customerFieldsDBMap;
	}

	public void setCustomerFieldsDBMap(HashMap customerFieldsDBMap) {
		this.customerFieldsDBMap = customerFieldsDBMap;
	}
	
	public String getCustomerSku() {
		return customerSku;
	}

	public void setCustomerSku(String customerSku) {
		this.customerSku = customerSku;
	}
	
	public HashMap<String, String> getSkuTypeList() {
		return skuTypeList;
	}

	public void setSkuTypeList(HashMap<String, String> skuTypeList) {
		this.skuTypeList = skuTypeList;
	}

	public HashMap<String, HashMap<String,String>> getSkuMap() {
		return skuMap;
	}

	public void setSkuMap(HashMap<String, HashMap<String,String>> skuMap) {
		this.skuMap = skuMap;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getRbPermissionShared() {
		return rbPermissionShared;
	}

	public void setRbPermissionShared(String rbPermissionShared) {
		this.rbPermissionShared = rbPermissionShared;
	}

	public String getRbPermissionPrivate() {
		return rbPermissionPrivate;
	}

	public void setRbPermissionPrivate(String rbPermissionPrivate) {
		this.rbPermissionPrivate = rbPermissionPrivate;
	}

	public String getSharePermissionLevel() {
		return sharePermissionLevel;
	}

	public void setSharePermissionLevel(String sharePermissionLevel) {
		this.sharePermissionLevel = sharePermissionLevel;
	}

	public String getShareAdminOnly() {
		return shareAdminOnly;
	}

	public void setShareAdminOnly(String shareAdminOnly) {
		this.shareAdminOnly = shareAdminOnly;
	}

	public boolean isCanShare() {
		return canShare;
	}

	public void setCanShare(boolean canShare) {
		this.canShare = canShare;
	}

	public String getListOwner() {
		return listOwner;
	}

	public void setListOwner(String listOwner) {
		this.listOwner = listOwner;
	}

	public String getListCustomerId() {
		return listCustomerId;
	}

	public void setListCustomerId(String listCustomerId) {
		this.listCustomerId = listCustomerId;
	}

	public ArrayList getListOfItems() {
		return listOfItems;
	}

	public void setListOfItems(ArrayList listOfItems) {
		this.listOfItems = listOfItems;
	}

	public String[] getItemIds() {
		return itemIds;
	}

	public void setItemIds(String[] itemIds) {
		this.itemIds = itemIds;
	}

	public HashMap<String, JSONObject> getPnaHoverMap() {
		return pnaHoverMap;
	}

	public void setPnaHoverMap(HashMap<String, JSONObject> pnaHoverMap) {
		this.pnaHoverMap = pnaHoverMap;
	}
	
	
	
	/**
	 * @return the ajaxDisplayStatusCodeMsg
	 */
	public String getAjaxDisplayStatusCodeMsg() {
		return ajaxDisplayStatusCodeMsg;
	}

	/**
	 * @param ajaxDisplayStatusCodeMsg the ajaxDisplayStatusCodeMsg to set
	 */
	public void setAjaxDisplayStatusCodeMsg(String ajaxDisplayStatusCodeMsg) {
		this.ajaxDisplayStatusCodeMsg = ajaxDisplayStatusCodeMsg;
	}

	/**
	 * @return the priceHoverMap
	 */
	public HashMap<String, XPEDXItemPricingInfo> getPriceHoverMap() {
		return priceHoverMap;
	}

	/**
	 * @param priceHoverMap the priceHoverMap to set
	 */
	public void setPriceHoverMap(HashMap<String, XPEDXItemPricingInfo> priceHoverMap) {
		this.priceHoverMap = priceHoverMap;
	}
	

	/**
	 * @return the checkItemKeys
	 */
	public String[] getCheckItemKeys() {
		return checkItemKeys;
	}

	/**
	 * @param checkItemKeys the checkItemKeys to set
	 */
	public void setCheckItemKeys(String[] checkItemKeys) {
		this.checkItemKeys = checkItemKeys;
	}
	
	

	/**
	 * @return the enteredUOMs
	 */
	public ArrayList<String> getEnteredUOMs() {
		return enteredUOMs;
	}

	/**
	 * @param enteredUOMs the enteredUOMs to set
	 */
	public void setEnteredUOMs(ArrayList<String> enteredUOMs) {
		this.enteredUOMs = enteredUOMs;
	}

	/**
	 * @return the enteredQuantities
	 */
	public ArrayList<String> getEnteredQuantities() {
		return enteredQuantities;
	}

	/**
	 * @param enteredQuantities the enteredQuantities to set
	 */
	public void setEnteredQuantities(ArrayList<String> enteredQuantities) {
		this.enteredQuantities = enteredQuantities;
	}

	public HashMap<String, JSONObject> getReplacementPnaHoverMap() {
		return replacementPnaHoverMap;
	}

	public void setReplacementPnaHoverMap(
			HashMap<String, JSONObject> replacementPnaHoverMap) {
		this.replacementPnaHoverMap = replacementPnaHoverMap;
	}
	
	public HashMap<String, XPEDXItemPricingInfo> getReplacementPriceHoverMap() {
		return replacementPriceHoverMap;
	}

	public void setReplacementPriceHoverMap(
			HashMap<String, XPEDXItemPricingInfo> replacementPriceHoverMap) {
		this.replacementPriceHoverMap = replacementPriceHoverMap;
	}

	public HashMap getXpedxItemIDUOMToReplacementListMap() {
		return xpedxItemIDUOMToReplacementListMap;
	}

	public void setXpedxItemIDUOMToReplacementListMap(
			HashMap xpedxItemIDUOMToReplacementListMap) {
		this.xpedxItemIDUOMToReplacementListMap = xpedxItemIDUOMToReplacementListMap;
	}

	public String getUniqueId() {
		setUniqueId(System.currentTimeMillis() + "");
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public String getListKey() {
		return listKey;
	}

	public void setListKey(String listKey) {
		this.listKey = listKey;
	}

	public String getListDesc() {
		return listDesc;
	}

	public void setListDesc(String listDesc) {
		if (listDesc == null) {
			listDesc = "";
		}
		this.listDesc = listDesc;
	}

	/*public HashMap getXpedxItemIDUOMToRelatedItemsListMap() {
		return xpedxItemIDUOMToRelatedItemsListMap;
	}

	public void setXpedxItemIDUOMToRelatedItemsListMap(
			HashMap xpedxItemIDUOMToRelatedItemsListMap) {
		this.xpedxItemIDUOMToRelatedItemsListMap = xpedxItemIDUOMToRelatedItemsListMap;
	}*/

	public HashMap getImageMap() {
		return imageMap;
	}

	public void setImageMap(HashMap imageMap) {
		this.imageMap = imageMap;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Map getDisplayItemUOMsMap() {
		return displayItemUOMsMap;
	}

	public Map getDisplayItemUOMsMap(String customerId, String itemID,
			String organizationCode) {
		displayItemUOMsMap = getUOMList(customerId, itemID, organizationCode);
		return displayItemUOMsMap;
	}

	public void setDisplayItemUOMsMap(Map displayItemUOMsMap) {
		this.displayItemUOMsMap = displayItemUOMsMap;
	}

	public String getPnaItemId() {
		return pnaItemId;
	}

	public void setPnaItemId(String pnaItemId) {
		this.pnaItemId = pnaItemId;
	}
	
	//Fix for issue 2066

	/**
	 * @return the pnaRequestedQty
	 */
	public String getPnaRequestedQty() {
		return pnaRequestedQty;
	}

	/**
	 * @param pnaRequestedQty the pnaRequestedQty to set
	 */
	public void setPnaRequestedQty(String pnaRequestedQty) {
		this.pnaRequestedQty = pnaRequestedQty;
	}
	
	public String getIsBracketPricing() {
		return isBracketPricing;
	}

	public void setIsBracketPricing(String isBracketPricing) {
		this.isBracketPricing = isBracketPricing;
	}

	public String getPnaRequestedUOM() {
		return pnaRequestedUOM;
	}

	public void setPnaRequestedUOM(String pnaRequestedUOM) {
		this.pnaRequestedUOM = pnaRequestedUOM;
	}

	public String getMyItemsKey() {
		return myItemsKey;
	}
	
		/**
	 * @return the inventoryCheckForItemsMap
	 */
	public HashMap<String, String> getInventoryCheckForItemsMap() {
		return inventoryCheckForItemsMap;
	}

	/**
	 * @param inventoryCheckForItemsMap the inventoryCheckForItemsMap to set
	 */
	public void setInventoryCheckForItemsMap(
			HashMap<String, String> inventoryCheckForItemsMap) {
		this.inventoryCheckForItemsMap = inventoryCheckForItemsMap;
	}
	

	public void setMyItemsKey(String myItemsKey) {
		this.myItemsKey = myItemsKey;
	}

	public Map getItemUOMsMap() {
		return itemUOMsMap;
	}

	public void setItemUOMsMap(Map itemUOMsMap) {
		this.itemUOMsMap = itemUOMsMap;
	}

	public List<XPEDXBracket> getDisplayPriceForUoms() {
		return displayPriceForUoms;
	}

	public void setDisplayPriceForUoms(List<XPEDXBracket> displayPriceForUoms) {
		this.displayPriceForUoms = displayPriceForUoms;
	}

	public String getPriceCurrencyCode() {
		return priceCurrencyCode;
	}

	public void setPriceCurrencyCode(String priceCurrencyCode) {
		this.priceCurrencyCode = priceCurrencyCode;
	}

	public List getBracketsPricingList() {
		return bracketsPricingList;
	}

	public void setBracketsPricingList(List bracketsPricingList) {
		this.bracketsPricingList = bracketsPricingList;
	}

	public List<String> getDataList() {
		return DataList;
	}

	public void setDataList(List<String> dataList) {
		DataList = dataList;
	}

	public List getDisplayUOMs() {
		return displayUOMs;
	}

	public void setDisplayUOMs(List displayUOMs) {
		this.displayUOMs = displayUOMs;
	}

	public String getIsPnAAvailable() {
		return isPnAAvailable;
	}

	public void setIsPnAAvailable(String isPnAAvailable) {
		this.isPnAAvailable = isPnAAvailable;
	}

	public boolean isFilterBySelectedListChk() {
		return filterBySelectedListChk;
	}

	public void setFilterBySelectedListChk(boolean filterBySelectedListChk) {
		this.filterBySelectedListChk = filterBySelectedListChk;
	}

	public boolean isFilterByMyListChk() {
		return filterByMyListChk;
	}

	public void setFilterByMyListChk(boolean filterByMyListChk) {
		this.filterByMyListChk = filterByMyListChk;
	}

	public boolean isFilterByAllChk() {
		return filterByAllChk;
	}

	public void setFilterByAllChk(boolean filterByAllChk) {
		this.filterByAllChk = filterByAllChk;
	}

	public String getSharePrivateField() {
		return sharePrivateField;
	}

	public void setSharePrivateField(String sharePrivateField) {
		this.sharePrivateField = sharePrivateField;
	}

	public Map<String, Map<String, String>> getItemIdsUOMsMap() {
		return itemIdsUOMsMap;
	}

	public void setItemIdsUOMsMap(Map<String, Map<String, String>> itemIdsUOMsMap) {
		this.itemIdsUOMsMap = itemIdsUOMsMap;
	}
	
	public Map<String, Map<String, String>> getItemIdsUOMsDescMap() {
		return itemIdsUOMsDescMap;
	}

	public void setItemIdsUOMsDescMap(
			Map<String, Map<String, String>> itemIdsUOMsDescMap) {
		this.itemIdsUOMsDescMap = itemIdsUOMsDescMap;
	}

	public HashMap<String, String> getItemImagesMap() {
		return itemImagesMap;
	}

	public void setItemImagesMap(HashMap<String, String> itemImagesMap) {
		this.itemImagesMap = itemImagesMap;
	}
	
	public HashMap<String, String> getItemDescMap() {
		return itemDescMap;
	}

	public void setItemDescMap(HashMap<String, String> itemDescMap) {
		this.itemDescMap = itemDescMap;
	}

	public YFCDate getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(YFCDate lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLastModifiedUserId() {
		return lastModifiedUserId;
	}

	public void setLastModifiedUserId(String lastModifiedUserId) {
		this.lastModifiedUserId = lastModifiedUserId;
	}

	public String getLastModifiedDateString() {
		return lastModifiedDateString;
	}

	public void setLastModifiedDateString(String lastModifiedDateString) {
		this.lastModifiedDateString = lastModifiedDateString;
	}
	
	public String getLastModifiedDateToDisplay() {
		UtilBean utilBean = new UtilBean();
		String dateToDisplay = utilBean.formatDate(lastModifiedDateString, wcContext, null, "M/dd/yyyy");			
		return dateToDisplay;
	}

	public boolean isItemDeleted() {
		return itemDeleted;
	}

	public void setItemDeleted(boolean itemDeleted) {
		this.itemDeleted = itemDeleted;
	}

	public ArrayList<Element> getXpedxYouMightConsiderItems() {
		return xpedxYouMightConsiderItems;
	}

	public void setXpedxYouMightConsiderItems(
			ArrayList<Element> xpedxYouMightConsiderItems) {
		this.xpedxYouMightConsiderItems = xpedxYouMightConsiderItems;
	}

	public ArrayList<Element> getXpedxPopularAccessoriesItems() {
		return xpedxPopularAccessoriesItems;
	}

	public void setXpedxPopularAccessoriesItems(
			ArrayList<Element> xpedxPopularAccessoriesItems) {
		this.xpedxPopularAccessoriesItems = xpedxPopularAccessoriesItems;
	}

	public Map<String, Element> getDescriptionMap() {
		return descriptionMap;
	}

	public void setDescriptionMap(Map<String, Element> descriptionMap) {
		this.descriptionMap = descriptionMap;
	}
	
	
	
	
	
}