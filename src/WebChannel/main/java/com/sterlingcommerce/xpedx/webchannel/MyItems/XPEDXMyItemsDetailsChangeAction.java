package com.sterlingcommerce.xpedx.webchannel.MyItems;

import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.util.YFCDate;

@SuppressWarnings("serial")
public class XPEDXMyItemsDetailsChangeAction extends WCMashupAction {
	
	private static final Logger LOG = Logger.getLogger(XPEDXMyItemsDetailsChangeAction.class);
	public static final String COMMAND_SAVE_ONE 	= "save_one";
	public static final String COMMAND_SAVE_ALL 	= "save_all";
	
	private Document outDoc;
	
	private String key			= "";
	private String listKey		= "";
	private String listKey2		= "";
	private String listName		= "";
	private String listDesc		= "";
	private String itemId 		= "";
	private String name 		= "";
	private String desc 		= "";
	private String qty			= "";
	private String order		= null;
	private String itemCount	= null;
	private String jobId		= "";
	private String createdBy 	= "";
	private String lastModified = "";
	private String command		= COMMAND_SAVE_ONE;
	private String uom			= "";
	private String editMode		= "";
	private String errorMsg		= "";
	private String shareAdminOnly   = "";
	
	private String[] names;
	private String[] descs;
	private String[] qtys;
	private String[] uoms;
	private String[] orders;
	private String[] itemIds;
	private String[] keys;

	
	private String customFieldCustLineField1;
	private String customFieldCustLineField2;
	private String customFieldCustLineField3;
	private String customFieldCustomerPONo;
	private String customFieldSeqNo;
	private String[] customFieldCustLineField1s;
	private String[] customFieldCustLineField2s;
	private String[] customFieldCustLineField3s;
	private String[] customFieldCustLineAccNos;
	private String[] customFieldCustomerPONos;
	private String[] customFieldCustomerLinePONos;
	private String modifiedDate;
	private String modifyuserid; //ADDED FOR JIRA 4134

	public String getModifyuserid() {
		return modifyuserid;
	}

	public void setModifyuserid(String modifyuserid) {
		this.modifyuserid = modifyuserid;
	}
	//end of jira 4134

	@Override
	public String execute() {
		try {
			YFCDate modifiedYFCDate = new YFCDate();
			modifiedDate = modifiedYFCDate.getString();
			Map<String, Element> out;
			
			//if (true){ return SUCCESS; }
			if (command.equals(COMMAND_SAVE_ONE)){
				out = prepareAndInvokeMashups();
				outDoc = (Document)out.values().iterator().next().getOwnerDocument();
			}
			if (command.equals(COMMAND_SAVE_ALL)){
				boolean anyItems = false;
				// Create the XPEDXMyItemsList document and append the childs and change the list in one call
				Document myitemsList = SCXmlUtil.createDocument("XPEDXMyItemsList");
				Element myItemsItemsList = myitemsList.createElement("XPEDXMyItemsItemsList");
				myitemsList.getDocumentElement().appendChild(myItemsItemsList);
				
				if (keys != null){
					for (int i = 0; i < keys.length; i++) {
						Element myItemItems = myitemsList.createElement("XPEDXMyItemsItems");
						anyItems = true;
						setItemId(getItemIds()[i]);
						setName(getNames()[i]);
						setDesc(getDescs()[i]);
						setKey(getKeys()[i]);
						setQty(getQtys()[i]);
						setOrder(getOrders()[i]);
						setUom(getUoms()[i]);
						
						//Custom fields
						try { setJobId(getCustomFieldCustLineAccNos()[i]); } catch (Exception e) {} 
						try { setCustomFieldCustomerPONo(getCustomFieldCustomerPONos()[i]); } catch (Exception e) {}
						try { setCustomFieldCustLineField1(getCustomFieldCustLineField1s()[i]); } catch (Exception e) {}
						try { setCustomFieldCustLineField2(getCustomFieldCustLineField2s()[i]); } catch (Exception e) {}
						try { setCustomFieldCustLineField3(getCustomFieldCustLineField3s()[i]); } catch (Exception e) {}
						try { setCustomFieldSeqNo(getCustomFieldCustomerLinePONos()[i]); } catch (Exception e) {}
						
						//Start - Set these field to the XPEDXMyItemsItems Element
						myItemItems.setAttribute("ItemId", getItemId());
						myItemItems.setAttribute("Name", getName());
						myItemItems.setAttribute("Desc", getDesc());
						myItemItems.setAttribute("Qty", getQty());
						myItemItems.setAttribute("JobId", getJobId());
						myItemItems.setAttribute("ItemOrder", getOrder());
						myItemItems.setAttribute("MyItemsListKey", getListKey());
						myItemItems.setAttribute("MyItemsKey", getKey());
						myItemItems.setAttribute("UomId", getUom());
						myItemItems.setAttribute("ItemPoNumber", getCustomFieldCustomerPONo());
						myItemItems.setAttribute("ItemSeqNumber", getCustomFieldSeqNo());
						myItemItems.setAttribute("ItemCustomField1", getCustomFieldCustLineField1());
						myItemItems.setAttribute("ItemCustomField2", getCustomFieldCustLineField2());
						myItemItems.setAttribute("ItemCustomField3", getCustomFieldCustLineField3());
						//Start - Set these field to the XPEDXMyItemsItems Element
						
						//Start - Append the created Element to the MyItemsItemsList Element
						myItemsItemsList.appendChild(myItemItems);
						//End - Append the created Element to the MyItemsItemsList Element
						
						/*This will be invoked at the end. All will be saved at once.
						 * out = prepareAndInvokeMashups();
						outDoc = (Document)out.values().iterator().next().getOwnerDocument();*/
					}
				}
				
				//Save the list info
				if (true){
					myitemsList.getDocumentElement().setAttribute("MyItemsListKey", getListKey2());
					myitemsList.getDocumentElement().setAttribute("Name", getListName());
					myitemsList.getDocumentElement().setAttribute("Desc", getListDesc());
					//added for jira 4134
					String isSalesRep = (String) getWCContext().getSCUIContext().getSession().getAttribute("IS_SALES_REP");
					if(isSalesRep!=null && isSalesRep.equalsIgnoreCase("true")){
						String salesreploggedInUserName = (String)getWCContext().getSCUIContext().getSession().getAttribute("loggedInUserName");
						myitemsList.getDocumentElement().setAttribute("ModifyUserName",salesreploggedInUserName);
					} else {
						myitemsList.getDocumentElement().setAttribute("ModifyUserName",getWCContext().getLoggedInUserName());	
					}
					//end of jira 4134
					Element output = (Element) WCMashupHelper.invokeMashup("XPEDXMyItemsListChange", myitemsList.getDocumentElement(), getWCContext().getSCUIContext());
					//Element tmp1 = prepareAndInvokeMashup("XPEDXMyItemsListChange");
					LOG.debug("Saved list destails..."+output.toString());
				}
			}
			
			
			//Update the item count
			try {
				if ( Integer.parseInt(order) > Integer.parseInt(itemCount) ){
					itemCount = order;
				}
			} catch (Exception e) {
				LOG.error(e.getStackTrace());
			}
			
			LOG.debug("RESULTS: " + XMLUtilities.getXMLDocString(outDoc));
			
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
			if(command.equalsIgnoreCase(COMMAND_SAVE_ONE))
				setErrorMsg("ITEM_REPLACE_ERROR");
			else
				setErrorMsg("ITEM_SAVE_ERROR");
			return ERROR;
		}
		return SUCCESS;
	}

	public String getCustomFieldCustLineAccNo(){
		return getJobId();
	}
	
	public Document getOutDoc() {
		return outDoc;
	}

	public void setOutDoc(Document outDoc) {
		this.outDoc = outDoc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public String getListKey() {
		return listKey;
	}

	public void setListKey(String listKey) {
		this.listKey = listKey;
		this.listKey2 = listKey;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getItemCount() {
		return itemCount;
	}

	public void setItemCount(String itemCount) {
		this.itemCount = itemCount;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String[] getQtys() {
		return qtys;
	}

	public void setQtys(String[] qtys) {
		this.qtys = qtys;
	}

	public String[] getUoms() {
		return uoms;
	}

	public void setUoms(String[] uoms) {
		this.uoms = uoms;
	}

	public String[] getOrders() {
		return orders;
	}

	public void setOrders(String[] orders) {
		this.orders = orders;
	}

	public String[] getItemIds() {
		return itemIds;
	}

	public void setItemIds(String[] itemIds) {
		this.itemIds = itemIds;
	}

	public String[] getKeys() {
		return keys;
	}

	public void setKeys(String[] keys) {
		this.keys = keys;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getEditMode() {
		return editMode;
	}

	public void setEditMode(String editMode) {
		this.editMode = editMode;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String[] getNames() {
		return names;
	}

	public void setNames(String[] names) {
		this.names = names;
	}

	public String[] getDescs() {
		return descs;
	}

	public void setDescs(String[] descs) {
		this.descs = descs;
	}

	public String getCustomFieldCustLineField1() {
		return customFieldCustLineField1;
	}

	public void setCustomFieldCustLineField1(String customFieldCustLineField1) {
		this.customFieldCustLineField1 = customFieldCustLineField1;
	}

	public String getCustomFieldCustLineField2() {
		return customFieldCustLineField2;
	}

	public void setCustomFieldCustLineField2(String customFieldCustLineField2) {
		this.customFieldCustLineField2 = customFieldCustLineField2;
	}

	public String getCustomFieldCustLineField3() {
		return customFieldCustLineField3;
	}

	public void setCustomFieldCustLineField3(String customFieldCustLineField3) {
		this.customFieldCustLineField3 = customFieldCustLineField3;
	}

	public String getCustomFieldCustomerPONo() {
		return customFieldCustomerPONo;
	}

	public void setCustomFieldCustomerPONo(String customFieldCustomerPONo) {
		this.customFieldCustomerPONo = customFieldCustomerPONo;
	}

	public String[] getCustomFieldCustLineField1s() {
		return customFieldCustLineField1s;
	}

	public void setCustomFieldCustLineField1s(String[] customFieldCustLineField1s) {
		this.customFieldCustLineField1s = customFieldCustLineField1s;
	}

	public String[] getCustomFieldCustLineField2s() {
		return customFieldCustLineField2s;
	}

	public void setCustomFieldCustLineField2s(String[] customFieldCustLineField2s) {
		this.customFieldCustLineField2s = customFieldCustLineField2s;
	}

	public String[] getCustomFieldCustLineField3s() {
		return customFieldCustLineField3s;
	}

	public void setCustomFieldCustLineField3s(String[] customFieldCustLineField3s) {
		this.customFieldCustLineField3s = customFieldCustLineField3s;
	}

	public String[] getCustomFieldCustLineAccNos() {
		return customFieldCustLineAccNos;
	}

	public void setCustomFieldCustLineAccNos(String[] customFieldCustLineAccNos) {
		this.customFieldCustLineAccNos = customFieldCustLineAccNos;
	}

	public String[] getCustomFieldCustomerPONos() {
		return customFieldCustomerPONos;
	}

	public void setCustomFieldCustomerPONos(String[] customFieldCustomerPONos) {
		this.customFieldCustomerPONos = customFieldCustomerPONos;
	}
	
	public String[] getCustomFieldCustomerLinePONos() {
		return customFieldCustomerLinePONos;
	}

	public void setCustomFieldCustomerLinePONos(
			String[] customFieldCustomerLinePONos) {
		this.customFieldCustomerLinePONos = customFieldCustomerLinePONos;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getCustomFieldSeqNo() {
		return customFieldSeqNo;
	}

	public void setCustomFieldSeqNo(String customFieldSeqNo) {
		this.customFieldSeqNo = customFieldSeqNo;
	}

	public String getShareAdminOnly() {
		return shareAdminOnly;
	}

	public void setShareAdminOnly(String shareAdminOnly) {
		this.shareAdminOnly = shareAdminOnly;
	}

	public String getItemId() {
		String res = itemId;
		try {
			if (res == null){
				res = "INVALID_ITEM";
			} else {
				if (res.trim().length() == 0){
					res = "INVALID_ITEM";
				}
			}
		} catch (Exception e) {
			res = "ERROR_INVALID_ITEM";
		}
		return res;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	@Override
	protected void populateMashupInput(String mashupId,
			Map<String, String> valueMap, Element mashupInput)
			throws CannotBuildInputException {
		// TODO Auto-generated method stub
		super.populateMashupInput(mashupId, valueMap, mashupInput);
	}

	@Override
	protected void populateMashupInputs(
			Map<String, Map<String, String>> valueMaps,
			Map<String, Element> mashupInputs) throws CannotBuildInputException {
		// TODO Auto-generated method stub
		super.populateMashupInputs(valueMaps, mashupInputs);
	}

	public String getListDesc() {
		return listDesc;
	}

	public void setListDesc(String listDesc) {
		this.listDesc = listDesc;
	}

	public String getListKey2() {
		return listKey2;
	}

	public void setListKey2(String listKey2) {
		this.listKey2 = listKey2;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
