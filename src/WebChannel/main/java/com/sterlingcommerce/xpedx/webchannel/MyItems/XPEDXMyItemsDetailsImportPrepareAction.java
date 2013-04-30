package com.sterlingcommerce.xpedx.webchannel.MyItems;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import au.com.bytecode.opencsv.CSVReader;

import com.sterlingcommerce.webchannel.common.BreadcrumbHelper;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.xpedx.webchannel.MyItems.vo.XPEDXCsvVO;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXShipToCustomer;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

@SuppressWarnings("serial")
public class XPEDXMyItemsDetailsImportPrepareAction extends WCMashupAction {
	
	private static final Logger LOG = Logger.getLogger(XPEDXMyItemsDetailsImportPrepareAction.class);
	
	private String listKey		= ""; //For the listing page
	private String listName		= ""; //For the listing page
	private String listDesc		= "";
	private String errorMsg		= "";
	
    private File file;
    private String contentType;
    private String filename;
    
	public String getFilename() {
		return filename;
	}
	boolean csvVar;
	public boolean isCsvVar() {
		return csvVar;
	}

	public void setCsvVar(boolean csvVar) {
		this.csvVar = csvVar;
	}

	private List<XPEDXCsvVO> dataList;
    
    private String[] itemsIds;
    private String[] itemsName;
	private String[] itemsDesc;
	private String[] itemsQty;
	//XB - 56 Changes - Start
	private String[] MfgItemsNumber;
	//XB - 56 Changes - End
	private String[] itemsUOM;
	private String[] itemsJobId;
	private String[] itemsOrder;
	private HashMap<String, HashMap<String, String>> itemsCustomFields;
	
	private HashMap customerFieldsMap;
	private HashMap customerFieldsDBMap;
	
	private String currentEnvCode = "";
	
	private String itemCount;
    private int itemCountInFile;
    private boolean editMode = false;
    private String sharePermissionLevel = "";
    private String shareAdminOnly = "";
    private String listOwner = "";
    private String listCustomerId = "";
	
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
	
	public String getItemCount() {
		return itemCount;
	}

	public void setItemCount(String itemCount) {
		this.itemCount = itemCount;
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
	public boolean isEditMode() {
		return editMode;
	}
	
	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

    public String getUniqueId(){
    	return System.currentTimeMillis() + "";
    }
    
    public void setUpload(File file) {
       this.file = file;
    }

	public void setUploadContentType(String contentType) {
       this.contentType = contentType;
    }

    public void setUploadFileName(String filename) {
       this.filename = filename;
    }


	public String getListKey() {
		return listKey;
	}


	public void setListKey(String listKey) {
		this.listKey = listKey;
	}


	public String getListName() {
		return listName;
	}


	public void setListName(String listName) {
		this.listName = listName;
	}
	
	protected Document getsapCustExtnFieldsFromSession(){
		/*HttpServletRequest httpRequest = wcContext.getSCUIContext().getRequest();
        HttpSession localSession = httpRequest.getSession();
        Document sapCustExtnFields = (Document)localSession.getAttribute("sapCustExtnFields");*/
		XPEDXWCUtils.setSAPCustomerExtnFieldsInCache();
		Document sapCustExtnFields = (Document)XPEDXWCUtils.getObjectFromCache("sapCustExtnFields");
        return sapCustExtnFields;
	}

	private void getCustomerDisplayFields(){
		try {
			setCustomerFieldsMap(new LinkedHashMap()) ;
			setCustomerFieldsDBMap(new LinkedHashMap()) ;
			//Element result = prepareAndInvokeMashup("draftOrderGetCustomerLineFields");
			Element result = getsapCustExtnFieldsFromSession().getDocumentElement();//Getting from session attr Extn Attributes of sap customer
			Element customerOrganizationExtnEle = XMLUtilities.getChildElementByName(result, "Extn");
			
			String custLineNoFlag 	= getXMLUtils().getAttribute(customerOrganizationExtnEle, "ExtnCustLineAccNoFlag");
			String custPONoFlag 	= getXMLUtils().getAttribute(customerOrganizationExtnEle, "ExtnCustLinePONoFlag");
			String custSeqNoFlag 	= getXMLUtils().getAttribute(customerOrganizationExtnEle, "ExtnCustLineSeqNoFlag");
			String custField1Flag 	= getXMLUtils().getAttribute(customerOrganizationExtnEle, "ExtnCustLineField1Flag");
			String custField2Flag 	= getXMLUtils().getAttribute(customerOrganizationExtnEle, "ExtnCustLineField2Flag");
			String custField3Flag 	= getXMLUtils().getAttribute(customerOrganizationExtnEle, "ExtnCustLineField3Flag");
			
			
			
			XPEDXShipToCustomer shipToCustomer = (XPEDXShipToCustomer) XPEDXWCUtils.getObjectFromCache(XPEDXConstants.SHIP_TO_CUSTOMER);
			
			String shipFromDivision = shipToCustomer.getExtnShipFromBranch();
			
			String envCode =  shipToCustomer.getExtnEnvironmentCode();
			try {
				setCurrentEnvCode(envCode);
			} catch (Exception e) {
				setCurrentEnvCode("");
			}
			
			if ("Y".equals(custLineNoFlag)) {
				String custLineNoLbl = getXMLUtils().getAttribute(customerOrganizationExtnEle, "ExtnCustLineAccLbl");
				getCustomerFieldsDBMap().put("CustLineAccNo", "JobId");
				//If no label found, Line Account# is used
				if (custLineNoLbl != null && custLineNoLbl.trim().length() > 0){
					getCustomerFieldsMap().put("CustLineAccNo", custLineNoLbl);
				}else {
					getCustomerFieldsMap().put("CustLineAccNo", "Line Account#");
				}
			}
			
			if ("Y".equals(custPONoFlag)) {
				getCustomerFieldsMap().put("CustomerPONo", "Line PO #");
				getCustomerFieldsDBMap().put("CustomerPONo", "ItemPoNumber");
			}
			//Fix for not showing Seq Number as per Pawan's mail dated 17/3/2011
			/*if ("Y".equals(custSeqNoFlag)) {
				getCustomerFieldsMap().put("CustomerLinePONo", "Customer Seq No");
				getCustomerFieldsDBMap().put("CustomerLinePONo", "ItemSeqNumber");
			}*/
			
			if ("Y".equals(custField1Flag)) {
				String custField1Lbl = getXMLUtils().getAttribute(customerOrganizationExtnEle, "ExtnCustLineField1Label");
				getCustomerFieldsDBMap().put("CustLineField1", "ItemCustomField1");
				if (custField1Lbl != null && custField1Lbl.trim().length() > 0)
					getCustomerFieldsMap().put("CustLineField1", custField1Lbl);
				else
					getCustomerFieldsMap().put("CustLineField1", "Customer Field 1");
			}
			
			if ("Y".equals(custField2Flag)) {
				String custField2Lbl = getXMLUtils().getAttribute(customerOrganizationExtnEle, "ExtnCustLineField2Label");
				getCustomerFieldsDBMap().put("CustLineField2", "ItemCustomField2");
				if (custField2Lbl != null && custField2Lbl.trim().length() > 0)
					getCustomerFieldsMap().put("CustLineField2", custField2Lbl);
				else
					getCustomerFieldsMap().put("CustLineField2", "Customer Field 2");
			}
			
			if ("Y".equals(custField3Flag)) {
				String custField3Lbl = getXMLUtils().getAttribute(customerOrganizationExtnEle, "ExtnCustLineField3Label");
				getCustomerFieldsDBMap().put("CustLineField3", "ItemCustomField3");
				if (custField3Lbl != null && custField3Lbl.trim().length() > 0)
					getCustomerFieldsMap().put("CustLineField3", custField3Lbl);
				else
					getCustomerFieldsMap().put("CustLineField3", "Customer Field 3");
			}
			
			
			
		} catch (Exception e) {
			LOG.error(e.toString());
		}
	}
	
	private ArrayList<String> getItemsFromFile()throws Exception{
		ArrayList<String> itemStr=new ArrayList<String>();
		CSVReader reader = new CSVReader(new FileReader(this.file));
		int i = -1;
		String [] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			i++;
			if (i > 0){
				itemStr.add(nextLine[1]);
			}
		}
		return itemStr;
	}
	private void parseFile() throws Exception{
		CSVReader reader = new CSVReader(new FileReader(this.file));
		String [] nextLine;
		int i = -1;
		
		ArrayList<XPEDXCsvVO> tmp = new ArrayList<XPEDXCsvVO>();
		int RegularFieldIndex = 0; // always equal to the last index that will be used
		RegularFieldIndex = (getCustomerFieldsMap().size()) + 3;
		
		/* Let us call the getCompleteItemList in the import create action. 
		 * So that we can avoid extra calls
		 * Document entitledItemsList=XPEDXMyItemsUtils.getEntitledItemsDocument(getWCContext(),getItemsFromFile()); 
		ArrayList<Element> itemList=com.sterlingcommerce.framework.utils.SCXmlUtils.getElements(entitledItemsList.getDocumentElement(), "/Item");
		ArrayList<String> itemListEntitled = new ArrayList<String>();
		for(Element elem : itemList){
			itemListEntitled.add(elem.getAttribute("ItemID"));
		}*/
		while ((nextLine = reader.readNext()) != null) {
			i++;
			if (i > 0){
				//Populate the object
				XPEDXCsvVO vo = new XPEDXCsvVO();
				/*
				•	Customer Part # - customer specific sku
				•	Supplier part # aka xpedx Part #
				•	Quantity
				•	Unit Of Measure
				•	Customer defined fields [ 1 to 5] - They show up based on the customer profile selection. The labels for the fields are going to be shown. 
				•	Description – Product description, this will be used for special items or the items which we did not find in the catalog. For the ones we found in the catalog, the desc will be used from the catalog and will ignore this description.
				 */
				
				vo.setCustomerPartNumber(nextLine[1]);
				vo.setSupplierPartNumber(nextLine[0]);
				// XB - 56 
				vo.setMfgItemNumber(nextLine[2]);
				//XB - End
				vo.setQty(nextLine[3]);
				/* Append the Uom Id in the import create action.
				 * This way we can avoid the getComplteteItemList call in this class
				 * if(itemListEntitled.contains(nextLine[1])){
					vo.setUOM(getCurrentEnvCode() + "_" + nextLine[3]);
				}
				else{
					vo.setUOM(nextLine[3]);
				}*/
				vo.setUOM(nextLine[4]);
				//vo.setLineLevelCode(nextLine[4]);
				
				int counter = 0;
				vo.setDescription(nextLine[counter+4+1]);
				
				for (Iterator iterator = getCustomerFieldsDBMap().values().iterator(); iterator.hasNext();) {
					counter++;
					String currentField = (String)iterator.next();
					String currentValue	= nextLine[counter+3+3];
					vo.getCustomFields().put(currentField, currentValue);
				}
				//vo.setDescription(nextLine[counter+3+1]);
				
				//validate against the rules
				boolean addVo = true;
					//Discard if no customer part number
					if (
							vo.getSupplierPartNumber().trim().length() == 0 &&
							vo.getCustomerPartNumber().trim().length() == 0
						) { addVo = false; }
					//Aggregate all the data in the description for future reference
					if (addVo){
						//vo.setDescription(vo.getDescription() + "\n\n" + vo.toString());
					}
				
				//Add the item to the list
				if (addVo){
					tmp.add(vo);
				}
			}
			LOG.debug("Record: " + nextLine);
			}
		
		itemCountInFile = tmp.size();
		
		if((itemCountInFile+Integer.parseInt(itemCount))<=200){
			//Add the items collected to the main object
			dataList = tmp;
			
			//Execute the import here
			try {
				//Prepare the data
				itemsIds 	= new String[dataList.size()];
				itemsName 	= new String[dataList.size()];
				itemsDesc 	= new String[dataList.size()];
				//XB- 56
				MfgItemsNumber = new String[dataList.size()];
				//XB- 56 End
				itemsQty 	= new String[dataList.size()];
				itemsUOM 	= new String[dataList.size()];
				itemsJobId 	= new String[dataList.size()];
				itemsOrder = new String[dataList.size()];
				itemsCustomFields 	= new HashMap<String, HashMap<String,String>>();
				
				for (int j = 0; j < getDataList().size(); j++) {
					XPEDXCsvVO vo 	= (XPEDXCsvVO)getDataList().get(j);
					itemsIds[j] 	= vo.getSupplierPartNumber();
					itemsName[j] 	= vo.getCustomerPartNumber();
					//XB- 56
					MfgItemsNumber[j] = vo.getMfgItemNumber();
					//XB-56 End
					itemsDesc[j] 	= vo.getDescription();
					itemsQty[j] 	= vo.getQty();
					itemsUOM[j] 	= vo.getUOM();
					itemsJobId[j] 	= " ";
					itemsCustomFields.put(j+"", vo.getCustomFields());
					itemsOrder[j] = Integer.parseInt(itemCount)+j+1+"";
				}
				
			} catch (Exception e) {
			}
			
			//Add the data to the request
			request.getSession().setAttribute("itemsId", 	getItemsIds());
			request.getSession().setAttribute("itemsName", 	getItemsName());
			request.getSession().setAttribute("itemsDesc", 	getItemsDesc());
			request.getSession().setAttribute("itemsQty", 	getItemsQty());
			//XB-56 - Start
			request.getSession().setAttribute("MfgItemsNumber", getMfgItemsNumber());
			//XB-56 - End
			request.getSession().setAttribute("itemsUOM", 	getItemsUOM());
			request.getSession().setAttribute("itemsJobId", getItemsJobId());
			request.getSession().setAttribute("itemsOrder", getItemsOrder());
			request.getSession().setAttribute("itemsCustomFields", itemsCustomFields);
		}
	}
		
	@Override
	@SuppressWarnings("unused")
	public String execute() {
		try {
			//Added For Jira 3197 - Invaid Import condition
			if(!(getFilename().endsWith(".csv"))){
				if(LOG.isDebugEnabled()){
				LOG.debug("ERROR...");
				}
				//csvVar= true;
				setErrorMsg("InvalidImport");
				editMode = true;
				return "failure";
			}
			getCustomerDisplayFields();
			
			//Read the file
			parseFile();
			
		} catch (Exception e) {
			LOG.error(e.toString());
			setErrorMsg("Level 1: " + e.toString());
		}
		
		if((itemCountInFile+Integer.parseInt(itemCount))<=200){
			BreadcrumbHelper.paramExclusionList.add("upload");
			request.getSession().setAttribute("errorMsg", 	getErrorMsg());
			return SUCCESS;
		}
		else{
			setErrorMsg("ItemsOverLoad");
			editMode = true;
			return "failure";
		}
	}

	public List<XPEDXCsvVO> getDataList() {
		return dataList;
	}

	public void setDataList(List<XPEDXCsvVO> dataList) {
		this.dataList = dataList;
	}

	public String[] getItemsName() {
		return itemsName;
	}

	public void setItemsName(String[] itemsName) {
		this.itemsName = itemsName;
	}

	public String[] getItemsDesc() {
		return itemsDesc;
	}

	public void setItemsDesc(String[] itemsDesc) {
		this.itemsDesc = itemsDesc;
	}

	public String[] getItemsQty() {
		return itemsQty;
	}

	public void setItemsQty(String[] itemsQty) {
		this.itemsQty = itemsQty;
	}
  //XB - 56 - Start
	public String[] getMfgItemsNumber() {
		return MfgItemsNumber;
	}

	public void setMfgItemsNumber(String[] mfgItemsNumber) {
		MfgItemsNumber = mfgItemsNumber;
	}
//XB - 56 - End
	public String[] getItemsUOM() {
		return itemsUOM;
	}

	public void setItemsUOM(String[] itemsUOM) {
		this.itemsUOM = itemsUOM;
	}

	public String[] getItemsJobId() {
		return itemsJobId;
	}

	public void setItemsJobId(String[] itemsJobId) {
		this.itemsJobId = itemsJobId;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
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

	public HashMap<String, HashMap<String, String>> getItemsCustomFields() {
		return itemsCustomFields;
	}

	public void setItemsCustomFields(
			HashMap<String, HashMap<String, String>> itemsCustomFields) {
		this.itemsCustomFields = itemsCustomFields;
	}

	public String[] getItemsIds() {
		return itemsIds;
	}

	public void setItemsIds(String[] itemsIds) {
		this.itemsIds = itemsIds;
	}

	public String getCurrentEnvCode() {
		return currentEnvCode;
	}

	public void setCurrentEnvCode(String currentEnvCode) {
		this.currentEnvCode = currentEnvCode;
	}

	public String[] getItemsOrder() {
		return itemsOrder;
	}

	public void setItemsOrder(String[] itemsOrder) {
		this.itemsOrder = itemsOrder;
	}

	

	
	
}
