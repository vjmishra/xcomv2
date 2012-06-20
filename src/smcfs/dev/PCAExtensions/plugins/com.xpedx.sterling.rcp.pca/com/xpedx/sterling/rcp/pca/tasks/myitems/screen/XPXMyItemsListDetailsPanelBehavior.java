package com.xpedx.sterling.rcp.pca.tasks.myitems.screen;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.xpedx.sterling.rcp.pca.myitems.editor.XPXManageMyItemsListEditor;
import com.xpedx.sterling.rcp.pca.myitems.screen.XPXMyItemsSearchListScreen;
import com.xpedx.sterling.rcp.pca.util.XPXCSVReader;
import com.xpedx.sterling.rcp.pca.util.XPXCacheManager;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCSharedTaskOutput;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXMyItemsListDetailsPanelBehavior extends YRCBehavior {
	
	private static final String IS_MODIFIED = "isModified";
	private Element inpuElement;
	private String myItemsListKey;
	private XPXMyItemsListDetailsPanel page;
	private boolean exportRequested = false;
	private boolean cancel;
	private Element multiAPIDocElement;
	public static final HashMap masterUOMList = new HashMap<String, String>();
	public  HashMap itemDescList = new HashMap<String, String>();
	public ArrayList itemList = new ArrayList();
	public ArrayList duplicateitemList = new ArrayList();
	private Element eleMyItemsList;
	private Element outItemDetailXml;
	private String updated = null;
	private String itemShortDescription;
	public static String baseUOM ;
	/*public static final String ACTION_EDIT = "EDIT";
	public static final String ACTION_DELETE = "DELETE";*/

	//Making UOM Column A ComboBox
	
	public XPXMyItemsListDetailsPanelBehavior(Composite ownerComposite, String formId, Object inputObject) {
        super(ownerComposite, formId,inputObject);
        page =(XPXMyItemsListDetailsPanel)ownerComposite;
        inpuElement = (Element) inputObject;
        myItemsListKey = inpuElement.getAttribute("MyItemsListKey");
        loadItemsList();
        String custName = XPXMyItemsSearchListScreen.getMyBehavior().getFieldValue("txtCustomerName");
        setFieldValue("txtCustName", custName);
        setControlEditable("clmCustAccountField", true);
       }
    
	public void loadItemsList() {
		//Load the ItemUOM from cache
		String enterpriseKey = (String)YRCPlatformUI.getUserElement().getAttribute("EnterpriseCode");
		XPXCacheManager.getInstance().getUomList(enterpriseKey, this);
		
		 Document docActions = YRCXmlUtils.createFromString("<Actions></Actions>");
	     setModel(docActions.getDocumentElement());
		String[] apinames = {"XPXGetItemUOMMasterList","getXPEDXMyItemsListDetail"};
		Document[] docInput = {
				YRCXmlUtils.createFromString("<ItemUOMMaster CallingOrganizationCode='xpedx'/>"),
				YRCXmlUtils.createFromString("<XPEDXMyItemsList MyItemsListKey='"+myItemsListKey+"'/>")		
				
				//YRCXmlUtils.createFromString("<Item  CallingOrganizationCode='xpedx' ItemID='"+myItemsListKey+"'  GetAvailabilityFromCache='Y'  />")
				
		};
		callApis(apinames, docInput);		
	}
	
	private void callApis(String apinames[], Document inputXmls[]) {
		YRCApiContext ctx = new YRCApiContext();
		ctx.setFormId(page.getFormId());
		ctx.setApiNames(apinames);
		ctx.setInputXmls(inputXmls);
		if (!page.isDisposed())
			callApi(ctx, page);
	}
	
	public void handleApiCompletion(YRCApiContext ctx) {
		if (ctx.getInvokeAPIStatus() > 0){
			if (page.isDisposed()) {
				YRCPlatformUI.trace("Page is Disposed");
			} else {
				String[] apinames = ctx.getApiNames();
				for (int i = 0; i < apinames.length; i++) {
					String apiname = apinames[i];
					
					//Added for JIRA 1155 - To get UOM Master List
					if ("XPXGetItemUOMMasterList".equals(apiname)) {						
						Element outUOMXml = ctx.getOutputXmls()[i].getDocumentElement();
						NodeList nl = outUOMXml.getElementsByTagName("ItemUOMMaster");
						if(!YRCPlatformUI.isVoid(nl))
						{
							for(int j=0;j<nl.getLength();j++)
							{
								Element temp = (Element) nl.item(j);
								masterUOMList.put(temp.getAttribute("UnitOfMeasure"), temp.getAttribute("Description"));
							}
						}
					} //  JIRA 1155 - Code Ends
					if ("getXPEDXMyItemsListDetail".equals(apiname)) {
						this.outItemDetailXml = ctx.getOutputXmls()[i].getDocumentElement();
						this.eleMyItemsList = YRCXmlUtils.getXPathElement(outItemDetailXml, "/XPEDXMyItemsListList/XPEDXMyItemsList");
						updateModelWithUOMDesc(eleMyItemsList);										
												
					} else if ("updateXPEDXMyItemsList".equals(apiname)) {
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						updateModelWithUOMDesc(outXml);
						Element eleMyItemsList = YRCXmlUtils.getXPathElement(outXml, "/XPEDXMyItemsList");
						setModel("getXPEDXMyItemsListDetail",eleMyItemsList);
						updated = "updated List";
						if(cancel==true){
							this.page.showResultMessage("Update Cancelled!!" , updated);
							cancel = false;
						}
						else{
							this.page.showResultMessage("Update Successful!!" , updated);
						}
						
						loadItemsList();
					}
					else if ("getCompleteItemList".equals(apiname)) {
						//Commented unnecessary code below
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						/*int itemlist = ctx.getOutputXmls().length;*/
						String itemID = null;
						String uomIds =null;
						Element item = ctx.getOutputXmls()[i].getDocumentElement();
						Element eleItemsList1 = YRCXmlUtils.getChildElement(item, "Item");
						ArrayList uomList = new ArrayList(); 

						if (eleItemsList1 != null) {

							itemID = eleItemsList1.getAttribute("ItemID");

							uomIds = eleItemsList1.getAttribute("UomId");

							/*
							 * uomIds =
							 * eleItemsList1.getAttribute("UnitOfMeasure");
							 * String uomDesc = (String)
							 * masterUOMList.get(uomIds);
							 */Element primaryInfoElem = YRCXmlUtils
									.getChildElement(eleItemsList1,
											"PrimaryInformation");
							if (primaryInfoElem != null) {
								itemShortDescription = primaryInfoElem
								.getAttribute("ShortDescription");
								itemDescList.put(itemID, primaryInfoElem
										.getAttribute("ShortDescription"));
							}
						}
						/* Removed unnecessary code STARTS */
						/*NodeList nl = eleItemsList1.getElementsByTagName("PrimaryInformation");
						for(int j=0;j<nl.getLength();j++)
						{
							Element temp = (Element) nl.item(j);
							temp.getAttribute("Description");
							itemDescList.put(ItemID, temp.getAttribute("ShortDescription"));
						}*/
						/*Element listUom = YRCXmlUtils.getChildElement(eleItemsList1, "AlternateUOMList");
						ArrayList<Element> listItemsUOM = YRCXmlUtils.getChildren(listUom, "AlternateUOM");
						for (Element eleItem : listItemsUOM) {
							eleItem.getAttribute("UnitOfMeasure");
							uomList.add(eleItem.getAttribute("UnitOfMeasure"));
							//eleItem.setAttribute("DisplayName",(String) masterUOMList.get(uomIds));
						}
						//getCompleteItemListAPICall(itemList);UnitOfMeasure
						Element actions = getModel("Actions");
						Element uomElement = YRCXmlUtils.createChild(actions, "ActionList");
						for(int uom =0; uom < uomList.size() ; uom++){
							Element uomElement1 = YRCXmlUtils.createChild(actions, "Action");
							uomElement1.setAttribute("DisplayName", (String) masterUOMList.get(uomList.get(uom)));
							
							uomElement1.setAttribute("Id", (String) masterUOMList.get(uomList.get(uom)));
							uomElement.appendChild(uomElement1);
							
						}*/
						/*ArrayList<Element> listItemsActions = YRCXmlUtils.getChildren(actions, "Action");
						for (Element eleItem : listItemsActions) {								
							eleItem.setAttribute("DisplayName",(String) masterUOMList.get(uomIds));
						}*/
						//setModel(actions);
						//if(duplicateitemList.size() == itemList.size()){
						//Element xmls = this.outItemDetailXml;
						/* Removed unnecessary code ENDS */
						this.eleMyItemsList = YRCXmlUtils.getXPathElement(this.outItemDetailXml, "/XPEDXMyItemsListList/XPEDXMyItemsList");
						String sharePrivate = eleMyItemsList.getAttribute("SharePrivate");
						String CreateUserName = eleMyItemsList.getAttribute("Createusername");
						if(sharePrivate != ""){
							String personalListType = CreateUserName.concat("(").concat(sharePrivate).concat(")");
							setFieldValue("txtListType", personalListType);
							
						}
						else{
							setFieldValue("txtListType", "Shared");
						}

							Element eleItemsItemsList1 = YRCXmlUtils.getChildElement(this.eleMyItemsList, "XPEDXMyItemsItemsList");
							ArrayList<Element> listItems1 = YRCXmlUtils.getChildren(eleItemsItemsList1, "XPEDXMyItemsItems");
							for (Element eleItem : listItems1) {								
								String itemsId = eleItem.getAttribute("ItemId");
								if(!eleItem.hasAttribute("ItemId")){
									eleItem.setAttribute("ItemType", "99.00");
								}
								//uomIds = eleItemsList1.getAttribute("UnitOfMeasure");
								//String uomDesc = (String) masterUOMList.get(uomIds);
								if(itemDescList!=null && itemDescList.containsKey(itemsId))
									eleItem.setAttribute("Name", (String) itemDescList.get(itemsId));
							//		eleItem.setAttribute("UnitOfMeasure",uomIds);
									}							
							
						//} - Removed unnecessary code
						
						//moved here as we need to export correct Item details from YFS_Item
						this.duplicateitemList = null;
						this.duplicateitemList = new ArrayList();
						if(exportRequested){
							synchronized (getFormId()) {
								StringBuilder sbCSV = new StringBuilder();
								sbCSV.append("Customer Part Number,Supplier Part Number,Quantity,Unit of Measure,");
								sbCSV.append("Description");
								sbCSV.append("\n");
								Element eleItemsItemsList = YRCXmlUtils.getChildElement(eleMyItemsList, "XPEDXMyItemsItemsList");								
								ArrayList<Element> listItems = YRCXmlUtils.getChildren(eleItemsItemsList, "XPEDXMyItemsItems");
																
								for (Element eleItem : listItems) {
									String uomId = null;
									if(eleItem.getAttribute("UomId").startsWith("M_")){
										uomId = eleItem.getAttribute("UomId").replace("M_" , " ").trim();
									}else
									{
										uomId = eleItem.getAttribute("UomId");
									}
									eleItem.setAttribute("Name", (String) itemDescList.get(eleItem.getAttribute("ItemId")));
									sbCSV.append("\"").append(eleItem.getAttribute("")).append("\"").append(",");
									sbCSV.append("\"").append(eleItem.getAttribute("ItemId")).append("\"").append(",");
									sbCSV.append("").append(eleItem.getAttribute("Qty")).append("").append(",");
									sbCSV.append("\"").append(uomId).append("\"").append(",");
									sbCSV.append("\"").append(eleItem.getAttribute("Name")).append("\"").append(",");
									sbCSV.append("\n");
								}
								
								
								exportRequested = false;
								String fileName = "Export_of_" + eleMyItemsList.getAttribute("Name").replace(" ", "_") + "_" + System.currentTimeMillis()+".csv";
								org.eclipse.swt.widgets.FileDialog fileDialog = new FileDialog(YRCPlatformUI.getShell(), SWT.SAVE);
								fileDialog.setFileName(fileName);
								fileDialog.setText("Save");
								String filePath = fileDialog.open();
								
								// Save file
								if(!YRCPlatformUI.isVoid(filePath))
									saveFile(filePath,sbCSV);
								
							}
						}
						repopulateModel("getXPEDXMyItemsListDetail");
					}
					
					if ("multiApi".equals(apiname)) {
						updated = "removed";
						this.page.showResultMessage("Items Removed !!", updated);
						loadItemsList();
					}	
					
					
				}
			}
		}
		//In case of Invoke API failure
		else if(ctx.getInvokeAPIStatus()==-1){
			Element outXml = ctx.getOutputXml().getDocumentElement();
			if("Errors".equals(outXml.getNodeName())){
					Element errorEle = (Element) outXml.getElementsByTagName(
							"Error").item(0);
					if (!YRCPlatformUI.isVoid(errorEle)) {
						YRCPlatformUI.trace(errorEle
								.getAttribute("ErrorDescription"), outXml);
						YRCPlatformUI.showError("Failed!", errorEle
								.getAttribute("ErrorDescription"));
					}
				}			
		}
		
		super.handleApiCompletion(ctx);
		
	}

	private void updateModelWithUOMDesc(Element outXml) {
//		Element eleMyItemsList = YRCXmlUtils.getXPathElement(outXml, "/XPEDXMyItemsListList/XPEDXMyItemsList");
//		//JIRA 1155 - Code Starts to Add UOMDesc
		Element eleItemsList = YRCXmlUtils.getChildElement(outXml, "XPEDXMyItemsItemsList");
		ArrayList<Element> listItems = YRCXmlUtils.getChildren(eleItemsList, "XPEDXMyItemsItems");
		
		for (Element eleItem : listItems) {
			eleItem.setAttribute("UomDesc", (String) masterUOMList.get(eleItem.getAttribute("UomId")));
			itemList.add(eleItem.getAttribute("ItemId"));
			//Uncheck the element by default
			eleItem.setAttribute("Checked", "N");
		} 
			setModel("getXPEDXMyItemsListDetail",outXml);
		//JIRA 1137 - Code Ends 	
	
		getCompleteItemListAPICall(itemList);
}
	

	
	 private void getCompleteItemListAPICall(ArrayList itemList){
	
		 for(int i = 0 ; i < itemList.size();i++){
			   if(duplicateitemList==null || !duplicateitemList.contains(itemList.get(i))){
				YRCApiContext ctx = new YRCApiContext();
				ctx.setApiNames(new String[]{"getCompleteItemList"});
				
			 	Document doc = YRCXmlUtils.createFromString("<Item  CallingOrganizationCode='xpedx' ItemID='"+itemList.get(i)+"'  IsForOrdering='N'  />");
				//docs = YRCXmlUtils.createFromString("<Item  CallingOrganizationCode='xpedx' ItemID='"+eleItem.getAttribute("ItemId")+"'  GetAvailabilityFromCache='Y'  />");
			 	duplicateitemList.add(itemList.get(i));
				ctx.setInputXml(doc);
				ctx.setFormId(getFormId());
				callApi(ctx, page);
				//The above api call o/p will be handled in handleAPICompletion method.
				//Element item = ctx.getOutputXml().getDocumentElement();
				
			   }
			 }
	 }
	 
	
	public void saveChangesToMyItemsList()
	{
		Element eleUpdateMyItemsListData = getTargetModel("SaveXPEDXMyItemsListDetail");
		eleUpdateMyItemsListData.setAttribute("MyItemsListKey", myItemsListKey);
		NodeList nlItems = eleUpdateMyItemsListData.getElementsByTagName("XPEDXMyItemsItems");
		for(int i=0;i<nlItems.getLength();i++){
			Element eleItemData =(Element)nlItems.item(i);
			if(!(eleItemData.hasAttribute(IS_MODIFIED))){
				eleItemData.getParentNode().removeChild(eleItemData);
				i--;
			} 
		}
		YRCApiContext ctx = new YRCApiContext();
		ctx.setApiNames(new String[]{"updateXPEDXMyItemsList"});
		Document[] docInput = {createUpdateXPEDXMyItemsListInput(eleUpdateMyItemsListData)};
		ctx.setInputXmls(docInput);
		ctx.setFormId(getFormId());

		callApi(ctx, page);
		((XPXManageMyItemsListEditor)YRCDesktopUI.getCurrentPart()).showBusy(true);
		
		
	}

	private Document createUpdateXPEDXMyItemsListInput(Element eleUpdateMyItemsListData) {
		
		Element updateXPEDXMyItemsListInput = YRCXmlUtils.createDocument("XPEDXMyItemsItemsList").getDocumentElement();
		updateXPEDXMyItemsListInput.setAttribute("MyItemsListKey", myItemsListKey);
		updateXPEDXMyItemsListInput.setAttribute("Name", eleUpdateMyItemsListData.getAttribute("Name"));
		updateXPEDXMyItemsListInput.setAttribute("Desc", eleUpdateMyItemsListData.getAttribute("Desc"));
		Element xPEDXMyItemsItemsList = YRCXmlUtils.createChild(updateXPEDXMyItemsListInput, "XPEDXMyItemsItemsList");
		NodeList nlItems = eleUpdateMyItemsListData.getElementsByTagName("XPEDXMyItemsItems");
		
			//System.out.println("The file is not updated");
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter=  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			String dateNow = formatter.format(currentDate.getTime());
			updateXPEDXMyItemsListInput.setAttribute("Modifyts", dateNow);
		
		for(int i=0;i<nlItems.getLength();i++){
			
			Element tempElement =(Element)nlItems.item(i);
			Element xPEDXMyItemsItems= YRCXmlUtils.createChild(xPEDXMyItemsItemsList, "XPEDXMyItemsItems");
			xPEDXMyItemsItems.setAttribute("MyItemsKey", tempElement.getAttribute("MyItemsKey"));
			xPEDXMyItemsItems.setAttribute("MyItemsListKey", myItemsListKey);
			xPEDXMyItemsItems.setAttribute("Qty", tempElement.getAttribute("Qty"));
			xPEDXMyItemsItems.setAttribute("ItemPoNumber",tempElement.getAttribute("ItemPoNumber"));
			xPEDXMyItemsItems.setAttribute("JobId",tempElement.getAttribute("JobId"));
			xPEDXMyItemsItems.setAttribute("UomId",tempElement.getAttribute("UomId"));
			xPEDXMyItemsItems.setAttribute("ItemOrder",tempElement.getAttribute("ItemOrder"));
			
		}
		return updateXPEDXMyItemsListInput.getOwnerDocument();
	}
	
	//Cancel Function
	public void cancelChangesToMyItemsList()
	{
		cancel = true;
		Element eleUpdateMyItemsListData = getTargetModel("SaveXPEDXMyItemsListDetail");
		eleUpdateMyItemsListData.setAttribute("MyItemsListKey", myItemsListKey);
		NodeList nlItems = eleUpdateMyItemsListData.getElementsByTagName("XPEDXMyItemsItems");
		for(int i=0;i<nlItems.getLength();i++){
			Element eleItemData =(Element)nlItems.item(i);
			if(!(eleItemData.hasAttribute(IS_MODIFIED))){
				eleItemData.getParentNode().removeChild(eleItemData);
				i--;
			} 
		}
		YRCApiContext ctx = new YRCApiContext();
		ctx.setApiNames(new String[]{"updateXPEDXMyItemsList"});
		Document[] docInput = {cancelUpdateXPEDXMyItemsListInput(eleUpdateMyItemsListData)};
		ctx.setInputXmls(docInput);
		ctx.setFormId(getFormId());

		callApi(ctx, page);
		((XPXManageMyItemsListEditor)YRCDesktopUI.getCurrentPart()).showBusy(true);
		
		
	}

	private Document cancelUpdateXPEDXMyItemsListInput(Element eleUpdateMyItemsListData) {
		
		Element updateXPEDXMyItemsListInput = YRCXmlUtils.createDocument("XPEDXMyItemsItemsList").getDocumentElement();
		updateXPEDXMyItemsListInput.setAttribute("MyItemsListKey", myItemsListKey);
		updateXPEDXMyItemsListInput.setAttribute("Name", eleUpdateMyItemsListData.getAttribute("Name"));
		updateXPEDXMyItemsListInput.setAttribute("Desc", eleUpdateMyItemsListData.getAttribute("Desc"));
		Element xPEDXMyItemsItemsList = YRCXmlUtils.createChild(updateXPEDXMyItemsListInput, "XPEDXMyItemsItemsList");
		NodeList nlItems = eleUpdateMyItemsListData.getElementsByTagName("XPEDXMyItemsItems");
		for(int i=0;i<nlItems.getLength();i++){
			
			Element tempElement =(Element)nlItems.item(i);
			Element xPEDXMyItemsItems= YRCXmlUtils.createChild(xPEDXMyItemsItemsList, "XPEDXMyItemsItems");
			xPEDXMyItemsItems.setAttribute("MyItemsKey", tempElement.getAttribute("MyItemsKey"));
			xPEDXMyItemsItems.setAttribute("MyItemsListKey", myItemsListKey);
			if(tempElement.getAttribute("QtyOldValue")!= null){
				xPEDXMyItemsItems.setAttribute("Qty", tempElement.getAttribute("QtyOldValue"));
			}
			else{
				xPEDXMyItemsItems.setAttribute("Qty", tempElement.getAttribute("Qty"));
				}
			if(tempElement.getAttribute("linePOOldValue").equalsIgnoreCase("")){
				xPEDXMyItemsItems.setAttribute("ItemPoNumber",tempElement.getAttribute("ItemPoNumber"));
 				
			}
			else{
				xPEDXMyItemsItems.setAttribute("ItemPoNumber",tempElement.getAttribute("linePOOldValue"));
			}
			if(tempElement.getAttribute("JobIDOldValue").equalsIgnoreCase("")){
				xPEDXMyItemsItems.setAttribute("JobId",tempElement.getAttribute("JobId"));
			}
			else{
				xPEDXMyItemsItems.setAttribute("JobId",tempElement.getAttribute("JobIDOldValue"));
			}
			
			
			
			
		}
		return updateXPEDXMyItemsListInput.getOwnerDocument();
	}
	//Cancel Function Ended
	
	public void importItemsList()
	{
		updated = null;
		this.page.showResultMessage("" , updated);
		org.eclipse.swt.widgets.FileDialog fileDialog = new FileDialog(YRCPlatformUI.getShell(), SWT.OPEN);
		fileDialog.setFilterExtensions(new String[] {"*.csv", "*.*"});
		String filePath = fileDialog.open();
		Document docImport = YRCXmlUtils.createDocument("XPEDXMyItemsItemsList");
		String [] nextLine;
		int i = -1;
		int supNum = 0;
		int ItemIds=0;
		int Qty=0;
		int UomDesc=0;
		int name=0;
		ArrayList columnHeader = new ArrayList();
		try {
			XPXCSVReader reader = new XPXCSVReader(new FileReader(filePath));
			
			while ((nextLine = reader.readNext()) != null) {
				i++;
				int len = nextLine.length;
				if(i <=0){
					for(int header =0 ; header < len ;header++){
						columnHeader.add(nextLine[header]);
						if(nextLine[header].equals("Supplier Part Number")){
							supNum = header;
						}
						if(nextLine[header].equals("Quantity")){
							Qty = header;
						}
						if(nextLine[header].equals("Unit of Measure")){
							UomDesc = header;
						}
						if(nextLine[header].equals("Description")){
							name = header;
						}
					}
					
				}
				if (i > 0){
					
/*					Customer Part Number,Supplier Part Number,Quantity,Unit of Measure,test label,Description,Price
					,4440720,,CTN,,"Reliable Folded Towels, White",19.55
					,2228534,,CTN,,"WypALL X60 Reinforced Teri Wipers, White",9.66
					,5150716,,RL,,"Scott JRT, Toilet Tissue",66.52
*/					Element eleMyItemsList = YRCXmlUtils.createChild(docImport.getDocumentElement(), "XPEDXMyItemsItems");
					for(int colHeader = 0 ; colHeader < columnHeader.size(); colHeader++){
						
						
						//Populate the object
						if(columnHeader.get(colHeader).equals("Supplier Part Number")){
							
							eleMyItemsList.setAttribute("ItemId", nextLine[supNum]);
							eleMyItemsList.setAttribute("ItemType", "1.00");
						}
						if(columnHeader.get(colHeader).equals("Quantity")){
							eleMyItemsList.setAttribute("Qty", nextLine[Qty]);
						}
						if(columnHeader.get(colHeader).equals("Unit of Measure")){
							eleMyItemsList.setAttribute("UomDesc", nextLine[UomDesc]);
							String uomId = null;
							if(nextLine[3].startsWith("M_")){
								uomId = nextLine[3].replace("M_" , " ").trim();
							}else
							{
								uomId = nextLine[3];
							}
							eleMyItemsList.setAttribute("UomId", "M_" + uomId);
						}
						
						if(columnHeader.get(colHeader).equals("Description")){
							//Trying to fetch Items Detail from database not from IMPORT List
							YRCApiContext ctx = new YRCApiContext();
							ctx.setApiNames(new String[]{"getCompleteItemList"});
							Document doc = YRCXmlUtils.createFromString("<Item  CallingOrganizationCode='xpedx' ItemID='"+nextLine[supNum]+"'  IsForOrdering='N'  />");
							ctx.setInputXml(doc);
							ctx.setFormId(getFormId());
							callApi(ctx, page);
							eleMyItemsList.setAttribute("Name", itemShortDescription);
						}
						
/*						eleMyItemsList.setAttribute("ItemId", nextLine[1]);
						eleMyItemsList.setAttribute("Qty", nextLine[2]);
						eleMyItemsList.setAttribute("UomDesc", nextLine[3]);
						eleMyItemsList.setAttribute("UomId", "M_"+nextLine[4]);
					//--changed for XIRA ID-1142	
					//	eleMyItemsList.setAttribute("ItemSeqNumber", nextLine[4]);
						eleMyItemsList.setAttribute("Name", nextLine[5]);
						//eleMyItemsList.setAttribute("Name", nextLine[0]);
*/						
						
					}
/*					Element eleMyItemsList = YRCXmlUtils.createChild(docImport.getDocumentElement(), "XPEDXMyItemsItems");
					//Populate the object
					
					eleMyItemsList.setAttribute("ItemId", nextLine[1]);
					eleMyItemsList.setAttribute("Qty", nextLine[2]);
					eleMyItemsList.setAttribute("UomDesc", nextLine[3]);
					eleMyItemsList.setAttribute("UomId", "M_"+nextLine[3]);
				//--changed for XIRA ID-1142	
				//	eleMyItemsList.setAttribute("ItemSeqNumber", nextLine[4]);
					eleMyItemsList.setAttribute("Name", nextLine[5]);
					//eleMyItemsList.setAttribute("Name", nextLine[0]);
*/				}
			}
		} catch (Exception e) {
			YRCPlatformUI.showError("TITLE_KEY_Error", "ERR_MSG_MY_ITEMS_LIST_Import_File_Failed");
			e.printStackTrace();
		}
		ArrayList listOfItemsToImport = YRCXmlUtils.getChildren(docImport.getDocumentElement(), "XPEDXMyItemsItems");
		
		if(listOfItemsToImport.size()>0){
			Element eleMyItemsList = getModel("getXPEDXMyItemsListDetail");
			/*validateItems(...) method not implemented, need to be implemented based on further guidelines*/
			Element eleInvalidItems = this.validateItems(eleMyItemsList, docImport);
			
			String[] apinames = {"updateXPEDXMyItemsList"};
			Document[] docInput = {YRCXmlUtils.createDocument("XPEDXMyItemsList")};
			Element eleMyItemList = docInput[0].getDocumentElement();
			eleMyItemList.setAttribute("MyItemsListKey", eleMyItemsList.getAttribute("MyItemsListKey"));
			YRCXmlUtils.importElement(eleMyItemList, docImport.getDocumentElement());
			
			callApis(apinames, docInput);
			((XPXManageMyItemsListEditor)YRCDesktopUI.getCurrentPart()).showBusy(true);
		}
	}
	
	/**
	 * Validate the Items being added to the My Item List if the Customer is entitled to add the item, and remove all items which are not entitled for the customer, and return
	 * invalid MyItemsItemsList in following hierarchy.
	 * 
	 * <pre>
	 * &lt;XPEDXMyItemsItemsList&gt;
	 * &lt;!-- Child element is a copy of the invalid child element from docImport&lt;Document&gt; --&gt;
	 * &lt;XPEDXMyItemsItems ItemId='' .../&gt; 
	 * &lt;/XPEDXMyItemsItemsList&gt;
	 * 
	 * @param eleMyItemsList
	 * @param docImport
	 * 
	 */
	private Element validateItems(Element eleMyItemsList, Document docImport) {
		Element eleInvalidItems = YRCXmlUtils.createDocument("XPEDXMyItemsItemsList").getDocumentElement();
		// TODO Auto-generated method stub
		return eleInvalidItems;
	}

	public void exportItemsList()
	{
		updated = null;
		this.page.showResultMessage("" , updated);
		String[] apinames = {"getXPEDXMyItemsListDetail"};
		Document[] docInput = {
				YRCXmlUtils.createFromString("<XPEDXMyItemsList MyItemsListKey='"+myItemsListKey+"'/>")										
		};
		callApis(apinames, docInput);
		exportRequested = true;
		
	}
	private void saveFile(String filePath, StringBuilder output) {

		// Handle in case of NULL file path.
		if(YRCPlatformUI.isVoid(filePath))
			return;

		//Save the buffer into file specified in the File Path.
		try {

			BufferedWriter out = new BufferedWriter(new FileWriter(filePath));  
			String outText = output.toString();
			out.write(outText);
			out.close();
			return ;
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	public void removeItems()
	{
		updated = null;
		this.page.showResultMessage("" , updated);
		boolean hasItemsToRemove=false;
		Element eleUpdateMyItemsListData = getTargetModel("SaveXPEDXMyItemsListDetail");
		NodeList nlItems = eleUpdateMyItemsListData.getElementsByTagName("XPEDXMyItemsItems");
		for(int i=0;i<nlItems.getLength();i++){
			Element eleItemData =(Element)nlItems.item(i);
			if(eleItemData.hasAttribute("Checked") && eleItemData.getAttribute("Checked").equals("Y")){
				hasItemsToRemove=true;
				deleteMyItemsDetailsInput(eleItemData);
			} 
		}	
		if(hasItemsToRemove && YRCPlatformUI.getConfirmation("TITLE_KEY_Confirm_Delete", "MSG_KEY_Confirm_Remove_Items_From_List"))
		{
			invokeDeleteMyItemsDetailsAPI();			
		}
	}

	
	public void deleteMyItemsDetailsInput(Element lineEle){
		updated = null;
		this.page.showResultMessage("" , updated);
		if(null ==multiAPIDocElement){
			multiAPIDocElement = YRCXmlUtils.createDocument("MultiApi").getDocumentElement();
		}
		Element APIele= YRCXmlUtils.createChild(multiAPIDocElement, "API");
		APIele.setAttribute("FlowName", "deleteXPEDX_MyItemsDetails");
		Element inputEle= YRCXmlUtils.createChild(APIele, "Input");
		Element temp =  YRCXmlUtils.createChild(inputEle, "XPEDXMyItemsItems");
		temp.setAttribute("MyItemsKey",lineEle.getAttribute("MyItemsKey") );
		
	}
	public void invokeDeleteMyItemsDetailsAPI(){

		if(!YRCPlatformUI.isVoid(multiAPIDocElement))
		{
			YRCApiContext ctx = new YRCApiContext();
			ctx.setApiName("multiApi");
			ctx.setInputXml(multiAPIDocElement.getOwnerDocument());
			ctx.setFormId(page.getFormId());
			ctx.setShowError(false);
			ctx.setUserData("isRefreshReqd", String.valueOf(false));
			callApi(ctx, page);
			multiAPIDocElement = null;			
		}

	}	
	
	public void editShareList()
	{
		Element eleMyItemsList = getModel("getXPEDXMyItemsListDetail");
		YRCPlatformUI.launchSharedTask(this.page,"com.xpedx.sterling.rcp.pca.sharedTasks.XPXCreateMyItemsListSharedTask",eleMyItemsList);
		
	}

	public void addItem() {
		updated = null;
		this.page.showResultMessage("" , updated);
		Element eleMyItemsList = getModel("getXPEDXMyItemsListDetail");
		YRCPlatformUI.launchSharedTask(this.page,"com.xpedx.sterling.rcp.pca.sharedTasks.XPXAddNewItemSharedTask",eleMyItemsList);
		
	}
	
	public	void openMultipleEditors(Element eleTableItem)throws PartInitException {
			
		System.out.println("T0----he Table element is "+YRCXmlUtils.getString(eleTableItem));
		Element uomElement = eleTableItem;
		baseUOM = eleTableItem.getAttribute("UnitOfMeasure");
		String currItemID = eleTableItem.getAttribute("ItemId");
		//String currItemID = "2001020";
	    Document docInput = YRCXmlUtils.createFromString("<Item SKU='"+currItemID+"' SKUType='LPC'/>");
		YRCSharedTaskOutput output = YRCPlatformUI.launchSharedTask("com.xpedx.sterling.rcp.pca.sharedTasks.XPXUOMDropDownSharedTask",docInput.getDocumentElement());
		Element eleUOMInfo = output.getOutput();
	     String value =  eleUOMInfo.getAttribute("UOMID");            
	      //Saving the updated value
		String oldValue = eleTableItem.getAttribute("UomId");
		if (oldValue != value) {
			
			eleTableItem.setAttribute("UOM_OLD_VALUE", oldValue);
			eleTableItem.setAttribute("UomId", value);
			eleTableItem.setAttribute("isModified", "Y");
		} else {
			eleTableItem.setAttribute("UOM_OLD_VALUE", oldValue);
		}
		
		saveChangesToMyItemsList();
	}
	

}