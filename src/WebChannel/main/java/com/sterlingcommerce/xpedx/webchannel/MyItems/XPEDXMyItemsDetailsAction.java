package com.sterlingcommerce.xpedx.webchannel.MyItems;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPathExpressionException;

import net.sf.json.JSONObject;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.comergent.appservices.configuredItem.XMLUtils;
import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer;
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
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;
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
	//XB-56 - Start
	Map<String, String> manufactureItemMap;
	Map<String, String> itemdescriptionMap;
	//XB-56 - End

	private ArrayList listOfItemsFromsession;
	public ArrayList getListOfItemsFromsession() {
		return listOfItemsFromsession;
	}

	public void setListOfItemsFromsession(ArrayList listOfItemsFromsession) {
		this.listOfItemsFromsession = listOfItemsFromsession;
	}
	private LinkedHashMap customerFieldsMap;
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
	private ArrayList<String> custUOM;
	private ArrayList<String> itemBaseUOM;
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
	//public boolean validateOrderMul = false;
	public boolean pnaCall;
	public ArrayList<String> itemOrder;
	private Map<String,String> itemOrderMap=new HashMap<String,String>();
	private Map<String,String> itemCustomerUomMap=new HashMap<String,String>();
	private Map<String,String> catMap=new HashMap<String,String>();
	Map<String, String> qtyTextBoxMap;
	private String modifyts;
    private String createUserId;
    private String modifyUserid;
// added for XB 214
	private Map<String,String>  sourcingOrderMultipleForItems =new HashMap<String,String>();
    protected String isOMError;
    protected HashMap useOrderMultipleMapFromSourcing;
	private String customerItemFlag;
    private String mfgItemFlag;
    private String qtyTextBox;
    private String[] names;//EB-760 Moved the export functionality after calling the validate UOM
    public boolean updatebuttonClicked;

	public String[] getNames() {
		return names;
	}

	public void setNames(String[] names) {
		this.names = names;
	}

	public String getQtyTextBox() {
		return qtyTextBox;
	}

	public void setQtyTextBox(String qtyTextBox) {
		this.qtyTextBox = qtyTextBox;
	}

	public String getCustomerItemFlag() {
		return customerItemFlag;
	}

	public void setCustomerItemFlag(String customerItemFlag) {
		this.customerItemFlag = customerItemFlag;
	}

	public String getMfgItemFlag() {
		return mfgItemFlag;
	}

	public void setMfgItemFlag(String mfgItemFlag) {
		this.mfgItemFlag = mfgItemFlag;
	}

	public HashMap getUseOrderMultipleMapFromSourcing() {
		return useOrderMultipleMapFromSourcing;
	}

	public void setUseOrderMultipleMapFromSourcing(
			HashMap useOrderMultipleMapFromSourcing) {
		this.useOrderMultipleMapFromSourcing = useOrderMultipleMapFromSourcing;
	}

    public String getIsOMError() {
		return isOMError;
	}

	public void setIsOMError(String isOMError) {
		this.isOMError = isOMError;
	}

	public Map<String, String> getSourcingOrderMultipleForItems() {
		return sourcingOrderMultipleForItems;
	}

	public void setSourcingOrderMultipleForItems(
			Map<String, String> sourcingOrderMultipleForItems) {
		this.sourcingOrderMultipleForItems = sourcingOrderMultipleForItems;
	}

	//End of XB 214
	//Added for JIRA 1402 Starts
	private ArrayList<String> itemValue = new ArrayList<String>();
    //Added for JIRA 1402 Ends

	//Added for webtrends
	protected Map<String,String>itemTypeMap=new HashMap<String,String>();
	//Added	erroMsg for XB-224
	private String 	erroMsg = "";
	public String getErroMsg() {
		return erroMsg;
	}

	public void setErroMsg(String erroMsg) {
		this.erroMsg = erroMsg;
	}

	private String duplicateInfoMsg = "";
	public String getDuplicateInfoMsg() {
		return duplicateInfoMsg;
	}

	public void setItemTypeMap(Map<String, String> itemTypeMap) {
		this.itemTypeMap = itemTypeMap;
	}
	public HashMap<String, String> getItemTypeMap() {
		return (HashMap<String, String>) itemTypeMap;
	}

	public String getModifyts() {
		return modifyts;
	}

	public void setModifyts(String modifyts) {
		this.modifyts = modifyts;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getModifyUserid() {
		return modifyUserid;
	}

	public void setModifyUserid(String modifyUserid) {
		this.modifyUserid = modifyUserid;
	}

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

/*	public boolean isValidateOrderMul() {
		return validateOrderMul;
	}

	public void setValidateOrderMul(boolean validateOrderMul) {
		this.validateOrderMul = validateOrderMul;
	}*/

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
	//This Map will contain item ids and customer UOM for that item if it exist
	public static LinkedHashMap<String, String> itemAndCustomerUomHashMap = new LinkedHashMap<String, String>();
	private String isPnaReqCustomerUOM;
	private String pnaReqCustomerUOM;
	public String getPnaReqCustomerUOM() {
		return pnaReqCustomerUOM;
	}

	public void setPnaReqCustomerUOM(String pnaReqCustomerUOM) {
		this.pnaReqCustomerUOM = pnaReqCustomerUOM;
	}

	public String getIsPnaReqCustomerUOM() {
		return isPnaReqCustomerUOM;
	}

	public void setIsPnaReqCustomerUOM(String isPnaReqCustomerUOM) {
		this.isPnaReqCustomerUOM = isPnaReqCustomerUOM;
	}

	public static LinkedHashMap<String, String> getItemAndCustomerUomHashMap() {
		return itemAndCustomerUomHashMap;
	}

	public static void setItemAndCustomerUomHashMap(
			LinkedHashMap<String, String> itemAndCustomerUomHashMap) {
		XPEDXMyItemsDetailsAction.itemAndCustomerUomHashMap = itemAndCustomerUomHashMap;
	}


	//Start 2964
	protected Map<String,Map<String,String>> itemIdConVUOMMap=new HashMap<String,Map<String,String>>();
	//End 2964

	protected HashMap<String, String> itemImagesMap = new HashMap<String, String>();
	protected HashMap<String, String> itemDescMap = new HashMap<String, String>();
	protected ArrayList<String> allItemIds = new ArrayList<String>();
	//This includes only My items list items and not other alternate items etc.
	protected ArrayList<String> allMyItemsListItemIds = new ArrayList<String>();
	protected List<String> allItemIDsWithDups = new ArrayList<String>();
	protected List<String> dupMILItemIds = new ArrayList<String>();
	//Added	validItemIds for XB-224
	protected ArrayList<String> validItemIds = new ArrayList<String>();
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
	public Map<String, String> getItemCustomerUomMap() {
		return itemCustomerUomMap;
	}

	public void setItemCustomerUomMap(Map<String, String> itemCustomerUomMap) {
		this.itemCustomerUomMap = itemCustomerUomMap;
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

	@Override
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
			String exportData = "Supplier Part Number,Customer Part Number,Manufacturer Item Number,Quantity,Unit of Measure,Line Level Code,Description\nDM560,428072,2,Carton,,abcdefght";
			StringBuilder sbCSV = new StringBuilder();
			HashMap exportMap = new HashMap();
			@SuppressWarnings("unused")
			ArrayList<Element> items = getXMLUtils().getElements(
					getOutDoc().getDocumentElement(), "XPEDXMyItemsItems");
			ArrayList<String> itemListEntitled=new ArrayList<String>();
			if(items.size() > 0){
				itemListEntitled=XPEDXMyItemsUtils.getEntitledItem(getWCContext(),items);
			}
			

			
			
			for (Element item : items) {
				String id 		 = item.getAttribute("MyItemsKey");
				if (request.getParameter("avail_" + id) != null) {
					this.updatebuttonClicked=true;
				}
			}
			if(this.updatebuttonClicked==true){
				processStockCheck(true);
				exportMap = this.pnaHoverMap;
			}
			sbCSV.append("Supplier Part Number,Customer Part Number,Manufacturer Item Number,Quantity,Unit of Measure,");


			sbCSV.append("Description,Price,");
			//EB-2542 - Reversing the sequence of Lineacct# and linePO# in import
			sbCSV.append("Price UOM,Line PO #,Line Acct #,Primary Available,Next Day Available,2+ Days Available,Availability UOM");

			// START - Display the custom fields
			/*for (Iterator iterator = getCustomerFieldsMap().keySet().iterator(); iterator.hasNext();) {
				sbCSV.append(",");
				String customKey = (String) iterator.next();
				sbCSV.append((String) getCustomerFieldsMap().get(customKey));
			}*/
			// END - Display the custom fields
			sbCSV.append("\n");
			int i = 0;
			JSONObject jdondata = new JSONObject();
			for (Element item : items) {
				String id 		 = item.getAttribute("MyItemsKey");
				String tmpItemId = item.getAttribute("ItemId");
				String customerPartNumber = ""; 
				String lineNumber = item.getAttribute("ItemOrder");
				//Added	this XB-56
				String immediate =null;
				String nextDay = null;
				String plus2Days = null;
				String UOM = null;


				String mfgItemNo = manufactureItemMap.get(item.getAttribute("ItemId"));

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

				// XB- 56 - Start
				if(null!=mfgItemNo && !mfgItemNo.isEmpty()){
				sbCSV.append("\"").append(
						XPEDXMyItemsUtils.encodeStringForCSV(mfgItemNo)).append("\"").append(
						",");
				}else{
					sbCSV.append("\"").append(
							XPEDXMyItemsUtils.encodeStringForCSV("")).append("\"").append(
							",");
				}
				//XB- 56 - End

				sbCSV.append("").append(
						XPEDXMyItemsUtils.encodeStringForCSV(item
								.getAttribute("Qty"))).append("").append(",");
				String uomId = item.getAttribute("UomId");
				if (uomId.equals("0")) {
					uomId = "";
				}

				//Remove the UOM code
				try {
					//if(itemListEntitled.contains(tmpItemId)){ // commented for Jira 4162
						uomId = uomId.substring(2, uomId.trim().length());
					//}
				} catch (Exception e) {
				}

				sbCSV.append("\"").append(
						XPEDXMyItemsUtils.encodeStringForCSV(uomId)).append(
						"\"").append(",");

				//XB-56 - Start - Modified Description by appending Long Description
				if(item.getAttribute("Name").trim().length()>0)
				{
				String name = item.getAttribute("Name");
				name = buildDescription(name,item.getAttribute("ItemId"));
				sbCSV.append("\"").append(
						XPEDXMyItemsUtils.encodeStringForCSV(name)).append("\"")
						.append(",");
				}
				else
				{
					String desc;//EB-760 Moved the export functionality after calling the validate UOM
					if(getNames()!=null &&  getNames().length>i){
						desc = getNames()[i];
					 }else{
						 desc = item.getAttribute("Desc");
					 }
					desc = buildDescription(desc,item.getAttribute("ItemId"));
					sbCSV.append("\"").append(
							XPEDXMyItemsUtils.encodeStringForCSV(desc)).append("\"")
							.append(",");
				}
				//XB-56 - Modified Description by appending Long Description - End
				//item.getAttribute("Desc");
				// Get the Avail and price
				String avail = "";
				String price = "";
				String pricingUom = "";
				try {
					if (request.getParameter("avail_" + id) != null) {
						avail = request.getParameter("avail_" + id);
						
						//EB-1065 - Export MIL
						
						//XB-56 Endif (totalForImmediate > 0) {
						if(exportMap !=null){
							if(lineNumber==null  && "".equalsIgnoreCase(lineNumber)){
								jdondata = (JSONObject) exportMap.get(tmpItemId);
							}else{
								jdondata = (JSONObject) exportMap.get(tmpItemId+"_"+lineNumber);
							}
							
							if(jdondata !=null){
								if(jdondata.get("Immediate") !=null && !"".equalsIgnoreCase(jdondata.get("Immediate").toString())){
									immediate = jdondata.get("Immediate").toString();					
								}else{
									immediate="0";
								}
								if(jdondata.get("NextDay") !=null && !"".equalsIgnoreCase(jdondata.get("NextDay").toString())){
									nextDay= jdondata.get("NextDay").toString();				
								}else{
									nextDay="0";
								}
								if(jdondata.get("TwoPlusDays") !=null && !"".equalsIgnoreCase(jdondata.get("TwoPlusDays").toString())){
									plus2Days=jdondata.get("TwoPlusDays").toString();
								}else{
									plus2Days = "0";
								}
								if(jdondata.get("UOM") !=null && !"".equalsIgnoreCase(jdondata.get("UOM").toString())){
									UOM = jdondata.get("UOM").toString();							
									if(UOM.contains("M_")){
										String a[] = UOM.split("_");
										UOM = a[1];								
									}
									
								}
							}
						
						}
						
						//ends
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

					if("CustomerPONo".equalsIgnoreCase(customKey) || "CustLineAccNo".equalsIgnoreCase(customKey)){
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
				}
				// END - Display the custom fields

				if(immediate !=null && !"".equalsIgnoreCase(immediate)){
					sbCSV.append("\"").append(
							XPEDXMyItemsUtils.encodeStringForCSV(immediate)).append(
									"\"").append(",");
				}else{
					sbCSV.append(",");
				}
				if(nextDay !=null && !"".equalsIgnoreCase(nextDay)){
					sbCSV.append("\"").append(
							XPEDXMyItemsUtils.encodeStringForCSV(nextDay)).append(
									"\"").append(",");
				}else{
					sbCSV.append(",");
				}
				if(plus2Days !=null && !"".equalsIgnoreCase(plus2Days)){
					sbCSV.append("\"").append(
							XPEDXMyItemsUtils.encodeStringForCSV(plus2Days)).append(
									"\"").append(",");
				}else{
					sbCSV.append(",");
				}
                if(UOM !=null && ! "".equalsIgnoreCase(UOM)){
                	sbCSV.append("\"").append(
							XPEDXMyItemsUtils.encodeStringForCSV(UOM)).append(
									"\"").append(",");
                }else{
                	sbCSV.append(",");
                }
				
				sbCSV.append("\n");
				i++;
			}
			exportData = sbCSV.toString();

			String fileName = "Export_of_" + getListName().replace(" ", "_")
					+ "_" + System.currentTimeMillis() + ".csv";
			StringBuffer attachment = new StringBuffer();

			attachment.append("attachment;filename=").append(fileName);
			response.setContentType("text/csv");
			response.setContentLength(exportData.length());
			response.setHeader("Content-disposition", attachment.toString());
			PrintWriter os = response.getWriter();

			os.write(exportData);
			os.flush();
			os.close();

		} catch (Exception e) {
			LOG.error(e.getStackTrace());
		}
	}


 	//XB-224 Added to check for item entitlement for the logged in customer

	//XB-56 - Start
	/**
	 * buildDescription - This will build description with combination of short and long description
	 * @param shortDescrip
	 * @param itemId
	 * @return String
	 */
	private String buildDescription(String shortDescrip, String itemId) {
		String longDescription = itemdescriptionMap.get(itemId);
		StringBuffer itemCompleteDescrip = new StringBuffer(shortDescrip);
		String[] longDescript;
		if(longDescription != null) {
		longDescript =  StringUtils.replace(longDescription, "<ul><li>","").replace("</ul>", "<li>").split("</li><li>");
		for(int i=0;i<longDescript.length;i++){
			itemCompleteDescrip.append(",").append(longDescript[i]);

		  }
		}
		return itemCompleteDescrip.toString();
	}

	//XB-56 - End
	private void checkforEntitlement(){

		ArrayList<String> entlErrorList = new ArrayList<String>();
		ArrayList<String> itemlist = new ArrayList<String>();
		try {
			// Removing call to getXpedxEntitledItemDetails for performance fix. JIRA 4020
			//Iterator productIDIter = allItemIds.iterator();
			if(allMyItemsListItemIds  != null) {
				itemlist  = allMyItemsListItemIds;
			}

			if(itemlist.size() == 0){
				for(int i=0; i<allMyItemsListItemIds.size();i++) {
					entlErrorList.add(allMyItemsListItemIds.get(i));
				}
				if(entlErrorList.size() == 0) {
					erroMsg = "";

				} else {
					if(entlErrorList.size()> 1){
						Iterator itr = entlErrorList.iterator();
						while(itr.hasNext())
						{
							erroMsg+= itr.next().toString()+",";

						}
						int lastIndex = erroMsg.lastIndexOf(",");
						erroMsg = erroMsg.substring(0,lastIndex);

					}
					else{
						erroMsg=entlErrorList.get(0);
					}
				}
			}
			else if(itemlist.size() == 1){

				for(int i=0; i<allMyItemsListItemIds.size();i++) {
					if(validItemIds.size() > 0){
						if(!validItemIds.contains(allMyItemsListItemIds .get(i))){
							entlErrorList.add(allMyItemsListItemIds .get(i));
						}
					}
					else
						entlErrorList.add(allMyItemsListItemIds.get(i));
				}
				if(entlErrorList != null && entlErrorList.size() > 0){
					erroMsg=entlErrorList.get(0);
				}
			}
			else{

				for(int i=0; i<allMyItemsListItemIds.size();i++) {
					if(!validItemIds.contains(allMyItemsListItemIds .get(i))){
						entlErrorList.add(allMyItemsListItemIds .get(i));
					}
				}

				if(entlErrorList != null && entlErrorList.size() > 0){
					if(entlErrorList.size()> 1){
						Iterator itr = entlErrorList.iterator();
						while(itr.hasNext())
						{
							erroMsg+= itr.next().toString()+",";
						}
						int lastIndex = erroMsg.lastIndexOf(",");
						erroMsg = erroMsg.substring(0,lastIndex);

					}
					else {
						erroMsg=entlErrorList.get(0);
					}
				}

				//}	End of while loop
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	//End of XB-224

	private void setItemDescription()
	{
		//XB-56 Start
		itemdescriptionMap = new HashMap<String, String>();
		//XB-56 End
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
					//XB-56 Start
					String description = primaryInfoElem.getAttribute("Description");
					itemdescriptionMap.put(itemId, description);
					//XB-56 End
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
						Element item = getListOfItems().get(i);
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
			if(YFCCommon.isVoid(getListKey()))
			{
				return "noListKey";
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
			if(getItemCount() != null && !getItemCount().equals("0"))
			{
				out = prepareAndInvokeMashups();
				if (out.values().iterator().next() != null) {
					outDoc = out.values().iterator().next()
							.getOwnerDocument();
					setListOfItems(getXMLUtils().getElements(
							outDoc.getDocumentElement(), "XPEDXMyItemsItems"));
				}
			}
			else
			{
				outDoc=SCXmlUtil.createDocument("XPEDXMyItemsItemsList");
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

			getCustomerDisplayFields();

			// Get related items and their information
			getRelatedItems();

			checkforEntitlement();//checks if the Items are entitled to the current customer - added for XB 224
			setItemDescription();
			getItemSKUMap();

			checkForDuplicateItemsinList();

			if(editMode ==  true)
			{
				setSkuTypeList(XPEDXWCUtils.getSkuTypesForQuickAdd(getWCContext()));
			}
			/*if (getCommand().equals(COMMAND_EXPORT_LIST)) {
				exportList();
				return "export";
			}*/

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
			 * getting all the items UOMs and description at the same time using a complex query
			 */
			//Changed for XB-687
			/*itemIdsUOMsDescMap = XPEDXOrderUtils.getXpedxUOMDescList(
			wcContext.getCustomerId(), allItemIds,
			wcContext.getStorefrontId());*/ // commented for performance issue as part of EB-1999 by Amar
			itemIdsUOMsDescMap=XPEDXWCUtils.getXpedxUOMListFromCustomService(allItemIds,false);// commented for performance issue as part of EB-1999 by Amar

			itemIdConVUOMMap = (Map<String,Map<String,String>>)ServletActionContext.getRequest().getAttribute("ItemIdConVUOMMap");
			ServletActionContext.getRequest().removeAttribute("ItemIdConVUOMMap");
			if(itemIdConVUOMMap == null)
				itemIdConVUOMMap =  new HashMap<String,Map<String,String>>();

			itemIdsUOMsMap = (LinkedHashMap<String, Map<String,String>>)ServletActionContext.getRequest().getAttribute("ItemUomHashMap");
			ServletActionContext.getRequest().removeAttribute("ItemUomHashMap");
			if(itemIdsUOMsMap == null)
				itemIdsUOMsMap =  new LinkedHashMap<String, Map<String,String>>();

			itemAndCustomerUomHashMap = (LinkedHashMap<String, String>)ServletActionContext.getRequest().getAttribute("itemCustomerUomHashMap");
			ServletActionContext.getRequest().removeAttribute("itemCustomerUomHashMap");
			if(itemAndCustomerUomHashMap == null)
				itemAndCustomerUomHashMap =  new LinkedHashMap<String, String>();

			//itemIdsIsCustomerUOMsMap = XPEDXOrderUtils.getItemUomIsCustomerUomHashMap();
			/*
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
					Map uomIsCustomermap = itemIdsIsCustomerUOMsMap.get(itemIdForUom);
					Set<Entry<String, String>> set = uommap.entrySet();
					Map<String, String> newUomMap = new HashMap(itemIdsUOMsMap.get(itemIdForUom));

					itemIdConVUOMMap.put(itemIdForUom, newUomMap);
					for (Entry<String, String> entry : set) {
						String uom = entry.getKey();
						String convFactor = (String) uommap.get(entry.getKey());
						String isCustomerUom = (String)uomIsCustomermap.get(entry.getKey());
						long convFac = Math.round(Double
								.parseDouble(convFactor));

						if(isCustomerUom.equalsIgnoreCase("Y")){
							if(1 == convFac){
								uommap.put(uom, uom.substring(2, uom.length()));
							}
							else
								uommap.put(uom, uom.substring(2, uom.length())
										+ " (" + convFac + ")");

						}
						else{
							if(1 == convFac){
								uommap.put(uom, XPEDXWCUtils.getUOMDescription(uom));
							}
							else{
								// -FXD- adding space between UOM & Conversion
								// Factor
								uommap.put(uom, XPEDXWCUtils.getUOMDescription(uom)
										+ " (" + convFac + ")");
							}
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
				try
				{
					XPEDXWCUtils.setObectInCache("itemsUOMMap",XPEDXOrderUtils.getXpedxUOMList(wcContext.getCustomerId(), getListOfItems(), wcContext.getStorefrontId()));
				}
				catch(Exception e)
				{
					LOG.error("Exception while setting UOM in to object");
				}
			}*/
			//itemIdsUOMsDescMap = itemIdsUOMsMap;
			validateItemUOM();

			setLastModifiedListInfo();

			XPEDXWCUtils.setObectInCache("listOfItemsMap", getListOfItems());
			XPEDXWCUtils.setObectInCache("orderMultipleFromSession", getItemOrderMultipleMap());
			//Added for JIRA 1402 Starts
			XPEDXWCUtils.setObectInCache("listOfItemsSize", getListOfItems().size());
			//Added for JIRA 1402 End
			XPEDXWCUtils.setObectInCache("itemConUOM", getItemIdConVUOMMap());
			//EB-760 Moved the export functionality after calling the validate UOM
			if (getCommand().equals(COMMAND_EXPORT_LIST)) {
				exportList();
				return "export";
			}
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
			XPEDXWCUtils.logExceptionIntoCent(e);  //JIRA 4289
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * Sets the duplicateInfoMsg to message that includes a list of dup items from the MIL.
	 * If on Edit screen, then each item includes a list of the seq# of where it occurs in list.
	 */
	private void checkForDuplicateItemsinList() {
		if(dupMILItemIds !=null && dupMILItemIds.size()>0){

			// Group all items with their correspond sequence number(s)
			// - need this so can display seq #s for dups on Edit MIL page
			Map<String, ArrayList<String>> indexes = buildMapItemWithSeqs();
			String itemsString = "";
			String wordForItem = "items";
			String wordForHas  = "have";

			if(dupMILItemIds.size() == 1) {
				// If only one item, wording is different
				String itemId = dupMILItemIds.get(0);
				itemsString = itemId + listDupSeqNums(itemId, indexes);
				wordForItem = "item";
				wordForHas = "has";
			}
			else{
				// For each dup item, display where it appears in list (seq#)
				for(int i =0; i<dupMILItemIds.size(); i++){
					String itemId = dupMILItemIds.get(i);

					itemsString += itemId + listDupSeqNums(itemId, indexes);

					if(i != dupMILItemIds.size()-1) {
						itemsString += ", ";
					}
				}
			}

			duplicateInfoMsg = "Please note that " +wordForItem+ " " +itemsString+ " " +wordForHas+
					" been added to this My Items List more than once.";
		}
	}

	private String listDupSeqNums(String itemId, Map<String, ArrayList<String>> indexes) {
		String ret = "";

		// only on MIL edit are sequence #s visible
		if (isEditMode()) {

			List<String> list = indexes.get(itemId);
			ret = "(" +
					StringUtils.join(list.toArray(), ",") +
					")";
		}
		return ret;
	}

	/**
	 * Creates a map where each item has a list of the seq# of where it appears
	 * in list (For dup items, the list would have more than one entry)
	 *
	 * @return map of items with seq# numbers of occurrences
	 */
	private Map<String, ArrayList<String>> buildMapItemWithSeqs() {
		Map<String,ArrayList<String>> itemsIndexes = new HashMap<String, ArrayList<String>>();
		int seq = 0;

		for (String itemId : allItemIDsWithDups) {
			seq++;
			ArrayList<String> bucket = itemsIndexes.get(itemId);
			if (bucket == null) {
				bucket = new ArrayList<String>();
				itemsIndexes.put(itemId, bucket);
			}
			// add the seq# for this dup to list for this item
			bucket.add(String.valueOf(seq));
		}
		return itemsIndexes;
	}

	private void validateItemUOM()
	{
		NodeList items=outDoc.getElementsByTagName("XPEDXMyItemsItems");
		for(int i=0;i<items.getLength();i++)
		{
			Element item=(Element)items.item(i);
			if(item != null)
			{
				if(itemIdsUOMsDescMap != null)
				{
					Map<String,String> actualUOMList=itemIdsUOMsDescMap.get(item.getAttribute("ItemId"));
					if(actualUOMList!=null)
					{
						Set<String> uomiDs=actualUOMList.keySet();
						boolean isUOMAvaliable=false;
						//EB-3991 to select the valid assigned UOM
						int index=0;
						String firstUOM="";
						for(String uomId :uomiDs)
						{
							
							if(index == 0)
								firstUOM=uomId;
							if(uomId != null && uomId.contains(item.getAttribute("UomId")))
							{
								isUOMAvaliable=true;
							}
							index=index+1;
						}
						if(!isUOMAvaliable)
						{
						//existing code will not work for Custom UOM incase of isCusExclFlay is Y
						//	item.setAttribute("UomId", getBaseUOMmap().get(item.getAttribute("ItemId")));
									item.setAttribute("UomId", firstUOM);

						}
					}
				}
			}
		}

	}

	public String pricecheck(){
		try{
			String invalidItems[]=null;
			if(erroMsg != null)
			{
				invalidItems=erroMsg.split(",");
			}
		if (getCommand().equals(COMMAND_STOCK_CHECK_ALL)) {
			processStockCheck(true,invalidItems);
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
	//for webtrends
	public String buildwebtrendTagForAll(){
		String jsonTotal = "";
		for (int i = 0; i < listOfItemsFromsession.size(); i++) {
			Element item = (Element) listOfItemsFromsession.get(i);
			String itemId = item.getAttribute("ItemId");
			String itemOrder = getItemOrderMap().get(itemId+":"+(i+1));
			String jsonKey = itemId + "_" + itemOrder;
			if(pnaHoverMap.containsKey(jsonKey)){
				JSONObject jsonData = new JSONObject();
				jsonData = pnaHoverMap.get(jsonKey);
				if(i==0)
					jsonTotal =  (String)jsonData.get("Total");
				else
					jsonTotal =  jsonTotal + ";"+(String)jsonData.get("Total");
			}
		}
		return jsonTotal;
	}

	private void setAllMyItems() {
		ArrayList<Element> items = getListOfItems();

		for (Iterator<Element> iterator = items.iterator(); iterator.hasNext();) {
			Element item = iterator.next();

			String itemId = item.getAttribute("ItemId");
			setItemID(itemId);
			String id = itemId.trim();

			// To display dup items in MIL including seq#, need a couple lists
			if(allMyItemsListItemIds.contains(id)) {
				if (!dupMILItemIds.contains(id)) {
					dupMILItemIds.add(id);
				}
			}
			else {
				allMyItemsListItemIds.add(id);
			}
			allItemIDsWithDups.add(id);
		}
	}

	@SuppressWarnings("deprecation")
	private void setLastModifiedListInfo() throws CannotBuildInputException, XPathExpressionException, XMLExceptionWrapper {
		String createUserIDStr = "";
		String createUserID = "";
		String modifyUserIdStr = "";
		String lastModifiedDateStr = "";
		Document outputDoc=null;
		/*if(modifyts == null || modifyts.trim().length()==0) commented for jira 4134
		{*/
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
				//modified to display lastModifiedBy
				//modifyUserIdStr = xpedxMyItemsListElement.getAttribute("Modifyuserid");
				modifyUserIdStr = xpedxMyItemsListElement.getAttribute("ModifyUserName");
				if(YFCUtils.isVoid(modifyUserIdStr)){
					createUserID = xpedxMyItemsListElement.getAttribute("Createuserid");
					createUserIDStr = XPEDXWCUtils.getLoginUserName(createUserID);
					setLastModifiedUserId(createUserIDStr);
				}else{
					setLastModifiedUserId(modifyUserIdStr);
				}
				lastModifiedDateStr = xpedxMyItemsListElement.getAttribute("Modifyts");
				setLastModifiedDateString(lastModifiedDateStr);
				setLastModifiedDate(YFCDate.getYFCDate(lastModifiedDateStr));
			}
			/*commented for jira 4134 since it was displaying wrong username and last modified date in the MIL NON edit page
			 }else
				{
			if(YFCUtils.isVoid(modifyUserid)){
				createUserIDStr = createUserId;
				setLastModifiedUserId(createUserIDStr);
			}else{
				setLastModifiedUserId(modifyUserid);
			}
			lastModifiedDateStr = modifyts;
			setLastModifiedDateString(lastModifiedDateStr);
			setLastModifiedDate(YFCDate.getYFCDate(lastModifiedDateStr));
		}
		*/
	}
	public String getJsonStringForMap(HashMap map){
		String jsonString = "";
		JSONObject jsonObject = JSONObject.fromObject( map );
		jsonString = jsonObject.toString();
		return jsonString;
	}

	private void setMyItemsImages(Element itemElem) {
		try {
			XPEDXSCXmlUtils xpedxScxmlUtil = new XPEDXSCXmlUtils();
			if(itemElem != null)
			{
				String itemId =itemElem.getAttribute("ItemID");
				if(allItemIds != null && allItemIds.contains(itemId))
				{
						Element primaryInfo = SCXmlUtil.getChildElement(itemElem, "PrimaryInformation");
						if(primaryInfo != null)
						{
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
							itemImagesMap.put(itemId, imageUrl);
							if(itemDescription!=null)
								itemDescMap.put(itemId, itemDescription);
						}
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
		processStockCheck(checkAllItems,null);
		this.updatebuttonClicked=true;
	}

	private void processStockCheck(boolean checkAllItems,String invalidItems[]) throws Exception {

			pnaCall = true;
			String customerId = wcContext.getCustomerId();
			String organizationCode = wcContext.getStorefrontId();
			Map<String , String> uoms = null;
			String uomCode = null;
			String convFact = null;
			String conversion;
			int totalQty;
			int OM;
			qtyTextBoxMap = new HashMap<String, String>();
			this.updatebuttonClicked=true;
			// Init some vars
			ArrayList<XPEDXItem> inputItems = new ArrayList<XPEDXItem>();

			//Added For Webtrends
			int cntSel =0;
			Map orderMulMap=(Map) XPEDXWCUtils.getObjectFromCache("orderMultipleFromSession");
			Map itemCon = (Map) XPEDXWCUtils.getObjectFromCache("itemConUOM");
			// 1 - Construct the input array for the call
			listOfItemsFromsession = (ArrayList) XPEDXWCUtils.getObjectFromCache("listOfItemsMap");
			ArrayList<String> itemIDList=new ArrayList<String>();
			if(listOfItemsFromsession != null) {
			for (int i = 0; i < listOfItemsFromsession.size(); i++) {
				Element item = (Element) listOfItemsFromsession.get(i);
				boolean isInvalidItem=false;
				String tmpQtyTxtBox=null;
				// Get some vars
				String id = item.getAttribute("MyItemsKey");
				String itemId = item.getAttribute("ItemId");
				itemIDList.add(itemId);
				itemOrderMap.put(itemId+":"+(i+1), itemOrder.get(i));
				String itemQty = enteredQuantities.get(i);
				String itemUom = enteredUOMs.get(i);
				String orderMultiple = (String)orderMulMap.get(itemId);
				String customerUOM = custUOM.get(i);
				itemCustomerUomMap.put(itemId+":"+(i+1), customerUOM);
				if(itemQty.trim().equals("")) {
					tmpQtyTxtBox=itemQty.trim();
					//Added for Jira EB-3649 - If selected UOM is same as Base UOM, then send the order multiple qty as default for PNA, else as 1 as default
					String itemBaseUom = itemBaseUOM.get(i);
					if(itemUom.equalsIgnoreCase(itemBaseUom)){
						itemQty = orderMultiple;	
					}
					else
					{
						itemQty = "1";	
					}
					itemUom =  enteredUOMs.get(i);//itemBaseUOM.get(i); added for EB 2034
				}

				uoms = (Map) itemCon.get(itemId);
				convFact = uoms.get(itemUom);
				if(itemUom==null || itemUom.equals(""))
					itemUom = item.getAttribute("UomId");
				//String itemSeqNum = item.getAttribute("ItemSeqNumber");
				String itemLineNum = item.getAttribute("ItemOrder");
				//String itemQty = item.getAttribute("Qty");
				/*if(itemQty==null || itemQty.trim().equals("") || itemQty.equals("0") )
					itemQty = "1";

				totalQty = (Integer.parseInt(convFact)) * (Integer.parseInt(itemQty));
				OM = totalQty % (Integer.parseInt(orderMultiple));*/

				if(invalidItems != null && invalidItems.length > 0)
				{
					for(int j=0;j<invalidItems.length;j++){
						if(invalidItems[j].equals(itemId))
						{
							isInvalidItem=true;
							break;
						}
					}
				}

				boolean addThisItem = checkAllItems;
				if (!checkAllItems) {
					if (ArrayUtils.contains(getCheckItemKeys(), id)) {
						addThisItem = true;
					}
				}

				if (addThisItem && !isInvalidItem) {
					XPEDXItem tmpItem = new XPEDXItem();
					tmpItem.setLegacyProductCode(itemId);
					tmpItem.setRequestedQtyUOM(itemUom);
					//tmpItem.setLineNumber(itemSeqNum);
					tmpItem.setLineNumber(itemLineNum);
					tmpItem.setRequestedQty(itemQty);
					inputItems.add(tmpItem);

					if("".equals(tmpQtyTxtBox)) {
						qtyTextBoxMap.put(id, tmpQtyTxtBox);
					}
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
					sourcingOrderMultipleForItems = XPEDXPriceandAvailabilityUtil.getOrderMultipleMapFromSourcing(items,true);//Added for XB 214 BR1
					useOrderMultipleMapFromSourcing = XPEDXPriceandAvailabilityUtil.useOrderMultipleErrorMapFromMax(pna.getItems());

					//System.out.println("sourcingOrderMultipleForItems : "+sourcingOrderMultipleForItems);
					Document pricingInfoDoc = XPEDXOrderUtils.getItemDetailsForPricingInfo(itemIDList,wcContext.getCustomerId(), wcContext.getStorefrontId(), wcContext);
					NodeList itemsNode=pricingInfoDoc.getDocumentElement().getElementsByTagName("Item");
					for (int i = 0; i < itemsNode.getLength(); i++) {

						Element itemElem=(Element)itemsNode.item(i);
						String itemID = itemElem.getAttribute("ItemID");
						ArrayList<Element> catPath = SCXmlUtil.getElements(itemElem, "CategoryList/Category");
						String categoryPath = catPath.get(0).getAttribute("CategoryPath");
						Map<String,String> mainCats = XPEDXWCUtils.getMainCategories();
						String pathElements[] = categoryPath.split("/");
						String currentCategoryName="";
						for(int j = 0; j <pathElements.length ; j++) {
							if(mainCats !=null && mainCats.size() > 0 )
								if( mainCats.containsKey(pathElements[j]) ){
									currentCategoryName = mainCats.get(pathElements[j]);
									XPEDXConstants.logMessage("currentCategoryName : " + currentCategoryName );
									currentCategoryName = currentCategoryName.replaceAll(" ", "");
									continue;
								}
						}
						catagory = currentCategoryName;
						catMap.put(itemID+":"+(i+1),catagory);
					}
					priceHoverMap = XPEDXPriceandAvailabilityUtil.getPricingInfoFromItemDetails(items, wcContext, true,null,false,null);
					//added for jira 2885 \ 4094
					items = pna.getItems();
						if (items != null) {
							if (pna.getHeaderStatusCode().equalsIgnoreCase("00")) {
								pnALineErrorMessage=XPEDXPriceandAvailabilityUtil.getLineErrorMessageMap(pna.getItems());
								setIsPnAAvailable("false");
							}
						}
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

	protected LinkedHashMap getCustomerFieldsMapfromSession(){
		/*HttpServletRequest httpRequest = wcContext.getSCUIContext().getRequest();
        HttpSession localSession = httpRequest.getSession();*/
		XPEDXWCUtils.setSAPCustomerExtnFieldsInCache();
		LinkedHashMap customerFieldsSessionMap = (LinkedHashMap)XPEDXWCUtils.getObjectFromCache("customerFieldsSessionMap");
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

	//EB-2542 - Reversing the sequence of Lineacct# and linePO# in import



		if ("Y".equals(custPONoFlag)) {
			//Fix for showing label as Line PO # as per Pawan's mail dated 17/3/2011
			//getCustomerFieldsMap().put("CustomerPONo", "Customer PO No");
			//Fix for showing custom PONo label entered by the Customer for XB 769\ XB 434
			String custLinePONoLbl = getXMLUtils().getAttribute(customerOrganizationExtnEle, "ExtnCustLinePOLbl");
			//getCustomerFieldsMap().put("CustomerPONo", "Line PO #");
			getCustomerFieldsDBMap().put("CustomerPONo", "ItemPoNumber");

			if (custLinePONoLbl != null && custLinePONoLbl.trim().length() > 0) {
				getCustomerFieldsMap().put("CustomerPONo", custLinePONoLbl);
			} else {
				getCustomerFieldsMap().put("CustomerPONo","Line PO #");
			}
			//End of XB 769\ XB 434
		}
		if ("N".equals(custPONoFlag)) {
			getCustomerFieldsDBMap().put("CustomerPONo","");
		}
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
						"Line Account #");
			}
			//Fix for showing label as Line Account # as per Pawan's mail dated 17/3/2011
			//getCustomerFieldsMap().put("CustLineAccNo", "Line Account#");
		}
		if ("N".equals(custLineNoFlag)) {
			getCustomerFieldsDBMap().put("CustLineAccNo","");
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
			LinkedHashMap<String,String> customerFieldsSessionMap = getCustomerFieldsMapfromSession();

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

	@SuppressWarnings("unchecked")
	private void getItemSKUMap() {
		//Fetch all the items in MIL and get their respective SKUs
		//XB-56 - Added Code to fetch Long Description and Manufacturing Item Number - Start
		manufactureItemMap = new HashMap<String, String>();
		if(getListOfItems()==null || getListOfItems().size()==0)
			return;
		setSkuMap(new HashMap<String, HashMap<String,String>>());
		// xb-805 code changes start
		HashMap<String, String> itemSkuMap = new LinkedHashMap<String, String>();
		 mfgItemFlag = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.BILL_TO_CUST_MFG_ITEM_FLAG);
		customerItemFlag = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXConstants.BILL_TO_CUST_PART_ITEM_FLAG);
		if(!SCUtil.isVoid(mfgItemFlag) && mfgItemFlag.equalsIgnoreCase("Y")) {
			if(allItemsDoc!=null) {
				Element itemList = allItemsDoc.getDocumentElement();
				Iterator<Element> itemIter = SCXmlUtil.getChildren(itemList);
				while(itemIter.hasNext()) {
					Element itemElement = itemIter.next();
					if(!SCUtil.isVoid(itemElement)) {
						String itemId = SCXmlUtil.getAttribute(itemElement, "ItemID");
						if(!SCUtil.isVoid(itemId) && SCUtil.isVoid(skuMap.get(itemId))) {
							Element primeInfoElem = SCXmlUtil.getChildElement(itemElement, "PrimaryInformation");
							if(primeInfoElem!=null)
							{
								String manufactureItem = primeInfoElem.getAttribute("ManufacturerItem");
								//XB-56 Start
								manufactureItemMap.put(itemId, manufactureItem);
								//XB-56 End
								if(manufactureItem!=null && manufactureItem.length()>0)
									itemSkuMap.put(XPEDXConstants.MFG_ITEM_NUMBER, manufactureItem);
							}
					    }
						skuMap.put(itemId, (HashMap<String, String>)itemSkuMap.clone());
						itemSkuMap.clear();
					}
				}
			}
		}
		if(!SCUtil.isVoid(customerItemFlag) && customerItemFlag.equalsIgnoreCase("Y")) {
			itemcustXrefDoc = XPEDXWCUtils.getXpxItemCustXRefDoc(allItemIds, getWCContext());
			if(itemcustXrefDoc!=null) {
				Element itemCustXRefList = itemcustXrefDoc.getDocumentElement();
				ArrayList<Element> itemCustXrefElems = SCXmlUtil.getElements(itemCustXRefList, "XPXItemcustXref");
				if(itemCustXrefElems!=null && itemCustXrefElems.size()>0) {
					Iterator<Element> xrefIter = itemCustXrefElems.iterator();
					while(xrefIter.hasNext()) {
						Element xref = xrefIter.next();
						String legacyItemNum = xref.getAttribute("LegacyItemNumber");
						if(allItemIds.contains(legacyItemNum.trim())) {
							String customerPartNumber = xref.getAttribute("CustomerItemNumber");
							if(customerPartNumber!=null && customerPartNumber.length()>0)
							{
								HashMap<String, String> tmpSkuMap = skuMap.get(legacyItemNum);
								if(SCUtil.isVoid(tmpSkuMap))
									itemSkuMap = new HashMap<String, String>();
								else
									itemSkuMap = tmpSkuMap;
							itemSkuMap.put(XPEDXConstants.CUST_PART_NUMBER, customerPartNumber);
							skuMap.put(legacyItemNum, (HashMap<String, String>)itemSkuMap.clone());
							itemSkuMap.clear();
							}
						}
					}
				}
			}
	   }
		// xb-805 code changes end
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
			e.printStackTrace();
		}
	}

	private void setItemDocAndInventoryMap() {

		//setting all the item Id lists
		setAllItemIdsOfList();
		ArrayList<Element> itemsElem = new ArrayList<Element>();
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
			if(allItemsDoc != null){
			//itemXrefDoc = XPEDXOrderUtils.getXpedxItemBranchItemAssociationDetails(allItemIds, wcContext.getCustomerId(), customerDivision, envCode, wcContext);
				itemsElem=SCXmlUtil.getElements(allItemsDoc.getDocumentElement(), "Item");
			}
			if(itemsElem != null)
			{
				for(int i=0;i<itemsElem.size();i++)
				{
					Element itemElem=itemsElem.get(i);
					if(itemElem!=null){
						String Itemid = SCXmlUtil.getAttribute(itemElem,"ItemID");
						//Using validItemIds for XB-224
						validItemIds.add(Itemid.trim());
						baseUOMmap.put(Itemid,itemElem.getAttribute("UnitOfMeasure"));
					}
					setMyItemsImages(itemElem);
				}
			}
			for(int i=0;i<allItemIds.size();i++)
			{
				Set<String> keySet=itemImagesMap.keySet();
				if( keySet!=null && !keySet.contains(allItemIds.get(i)))
				{
					String imageUrl = "/xpedx/images/INF_150x150.jpg";
					itemImagesMap.put(allItemIds.get(i), imageUrl);
				}
			}
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
		// for webtrends start
		HashMap<String,String> itemTypeMap = new HashMap<String, String>();
		//webtrends end
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
							if (inventoryIndicator.equalsIgnoreCase("W")){
								inventoryMap.put(currItemId, "Y");
								itemTypeMap.put(currItemId,"Stocked");
							}else{
								inventoryMap.put(currItemId, "N");
							}
							/*start of webtrends */
							if (inventoryIndicator.equalsIgnoreCase("I")){
								itemTypeMap.put(currItemId,"InDirect");
							}else if (inventoryIndicator.equalsIgnoreCase("") || inventoryIndicator.equalsIgnoreCase("M")){
								itemTypeMap.put(currItemId,"Mill");
							}
							/*End of webtrends */
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
					itemTypeMap.put(currItemId,"Mill");
					orderMultipleMap.put(currItemId, "1");
				}
			}
		}
		setItemOrderMultipleMap(orderMultipleMap);
		setInventoryCheckForItemsMap(inventoryMap);
		//Added for webtrends
		setItemTypeMap(itemTypeMap);
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
							String altItemID = XMLUtils.getAttributeValue(altItem, "ItemID");
							if(!SCUtil.isVoid(altItemID) && !xpedxYouMightConsiderItemIds.contains(altItemID)){
								addToYouMightAlsoConsiderItemList(altItem);
								xpedxYouMightConsiderItemIds.add(altItemID);
							}

							//String currItemId = SCXmlUtil.getAttribute(altItem, "ItemID");
							//if(!SCUtil.isVoid(currItemId) && !xpedxYouMightConsiderItemIds.contains(currItemId))
							//	xpedxYouMightConsiderItemIds.add(currItemId);

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
							String upgradeItemID = XMLUtils.getAttributeValue(upgradeItem, "ItemID");
							if(!SCUtil.isVoid(upgradeItemID) && !xpedxYouMightConsiderItemIds.contains(upgradeItemID)){
								addToYouMightAlsoConsiderItemList(upgradeItem);
								xpedxYouMightConsiderItemIds.add(upgradeItemID);
							}

							//String currItemId = SCXmlUtil.getAttribute(upgradeItem, "ItemID");
							//if(!SCUtil.isVoid(currItemId) && !xpedxYouMightConsiderItemIds.contains(currItemId))
							//	xpedxYouMightConsiderItemIds.add(currItemId);
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
							String upSellItemId = SCXmlUtil.getAttribute(usItem, "ItemID");
							if(!SCUtil.isVoid(upSellItemId) && !xpedxYouMightConsiderItemIds.contains(upSellItemId)){
								addToYouMightAlsoConsiderItemList(usItem);
								xpedxYouMightConsiderItemIds.add(upSellItemId);
							}

							//String currItemId = SCXmlUtil.getAttribute(usItem, "ItemID");
							//if(!SCUtil.isVoid(currItemId) && !xpedxYouMightConsiderItemIds.contains(currItemId))
							//	xpedxYouMightConsiderItemIds.add(currItemId);
						}
					}
				}
			}
			//XB-673 - Changes Start
			/**
			 * XB-673 - Alternative Items added from business center are displayed on MIL screen under yoo might also consider section
			 */
			HashMap<String,ArrayList<Element>> alternateSBCListMap = allAssociatedItemsMap.get(XPEDXOrderUtils.ALTERNATE_SBC_ITEMS_KEY);
			if(alternateSBCListMap!=null && !alternateSBCListMap.isEmpty())
			{
				Set alternateSBCItemsKey = alternateSBCListMap.keySet();
				if(alternateSBCItemsKey!=null){
					Iterator<String> alternateSBCKeyIter = alternateSBCItemsKey.iterator();
					while(alternateSBCKeyIter.hasNext())
					{
						String key = alternateSBCKeyIter.next();
						ArrayList<Element> currentItemUSList = alternateSBCListMap.get(key);
						for(Element alternateSBCItem: currentItemUSList)
						{
							String alternateSBCItemId = SCXmlUtil.getAttribute(alternateSBCItem, "ItemID");
							if(!SCUtil.isVoid(alternateSBCItemId) && !xpedxYouMightConsiderItemIds.contains(alternateSBCItemId)){
								addToYouMightAlsoConsiderItemList(alternateSBCItem);
								xpedxYouMightConsiderItemIds.add(alternateSBCItemId);
							}

							//String currItemId = SCXmlUtil.getAttribute(usItem, "ItemID");
							//if(!SCUtil.isVoid(currItemId) && !xpedxYouMightConsiderItemIds.contains(currItemId))
							//	xpedxYouMightConsiderItemIds.add(currItemId);
						}
					}
				}
			}
			////XB-673 - Changes End
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
							//XB-673 - Changes Start - Changes related Cross-sell Items are not displayed on MIL
							//if(!xpedxPopularAccessoriesItems.contains(csItem))
							//	xpedxPopularAccessoriesItems.add(csItem);
							String currItemId = SCXmlUtil.getAttribute(csItem, "ItemID");
							if(!SCUtil.isVoid(currItemId) && !xpedxYouMightConsiderItemIds.contains(currItemId))
								addToYouMightAlsoConsiderItemList(csItem);
							    xpedxYouMightConsiderItemIds.add(currItemId);
								//xpedxPopularAccessoriesItemIds.add(currItemId);

							  //XB-673 - Changes End
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

				ArrayList<String> complimentaryItemIDList = complim