package com.xpedx.sterling.rcp.pca.tasks.myitems.screen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.xpedx.sterling.rcp.pca.myitems.editor.XPXManageMyItemsListEditor;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXmlUtils;


public class XPXAddItemPopupPanelBehavior extends YRCBehavior {

	private String formId;
	private Element eleXpath;
	private XPXAddItemPopupPanel page;
	private boolean searchSKUPerformed = false;
	public static final HashMap masterUOMList = new HashMap<String, String>();
	private Element inputEle;
	private XPXMyItemsListDetailsPanel myItemListref1;

	public XPXAddItemPopupPanelBehavior(Composite ownerComposite, String formId,Element inputObject, XPXMyItemsListDetailsPanel myItemListref) {
		super(ownerComposite, formId);
		this.formId = formId;
		this.page = (XPXAddItemPopupPanel) ownerComposite;
		this.inputEle=inputObject;
		myItemListref1=myItemListref;
		initiliaze();
	}

	private void initiliaze() {
		String[] apinames = {"XPXGetLineTypeList","XPXGetItemUOMMasterList"};
		Document[] docInput = {
				YRCXmlUtils.createFromString("<CommonCode CallingOrganizationCode='" + YRCXmlUtils.getAttribute(inputEle, "EnterpriseCode")+ "' CodeType='LineType'/>"),
				YRCXmlUtils.createFromString("<ItemUOMMaster CallingOrganizationCode='" +YRCXmlUtils.getAttribute(inputEle, "EnterpriseCode")+ "'/>"),
				};
		callApis(apinames, docInput);
	}

	 private void callApis(String apinames[], Document inputXmls[]) {
			YRCApiContext ctx = new YRCApiContext();
			ctx.setFormId(formId);
			ctx.setApiNames(apinames);
			ctx.setInputXmls(inputXmls);
			if (!page.isDisposed())
				callApi(ctx, page);
		}
	 
	 public void handleApiCompletion(YRCApiContext ctx) {
			if (ctx.getInvokeAPIStatus() > 0){
				if (page.isDisposed()) {
					YRCPlatformUI.trace("Page is Disposed");
				}
				else{
					String[] apinames = ctx.getApiNames();
					for (int i = 0; i < apinames.length; i++) {
						String apiname = apinames[i];
						if ("XPXGetLineTypeList".equals(apiname)) {
							Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
							YRCXmlUtils.getString(outXml);
							outXml=updateLineTypesModel(outXml);
							setModel("LineTypes",outXml);
						}
						if("XPXGetSKUDetailsService".equals(apinames[i])){
							Document docItemInfo = ctx.getOutputXmls()[i];
							Element eleItemInfo = docItemInfo.getDocumentElement();
							setModel("itemInfo", eleItemInfo);
							
							if(YRCPlatformUI.isVoid(eleItemInfo.getAttribute("ErrorMessage"))){
								
								getControl("txtItemId").setFocus();
							//	setFieldValue("txtItemId", eleItemInfo.getAttribute("ItemID"));
								setFieldValue("txtUOM", eleItemInfo.getAttribute("UnitOfMeasure"));
								
								if(masterUOMList.containsKey(eleItemInfo.getAttribute("UnitOfMeasure"))){
									setFieldValue( "txtUOM",(String) masterUOMList.get(eleItemInfo.getAttribute("UnitOfMeasure")));
								}
								else{
									setFieldValue( "txtUOM",eleItemInfo.getAttribute("UnitOfMeasure"));
								}
								if(!YRCPlatformUI.isVoid(YRCXmlUtils.getXPathElement(eleItemInfo, "/Order/PrimaryInformation")))
									setFieldValue("txtItemDesc",YRCXmlUtils.getXPathElement(eleItemInfo, "/Order/PrimaryInformation").getAttribute("ExtendedDescription") );
								
							}
							else{
								setFieldValue("txtItemId", "");
								YRCPlatformUI.showError("Error", "Invalid SKU:"+eleItemInfo.getAttribute("ErrorMessage"));
							}
//							private Text txtDescription;
//							private Text txtUOM;
						}
						
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
						} 
						
						 else if ("updateXPEDXMyItemsList".equals(apiname)) {
								Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
								Element eleMyItemsList = YRCXmlUtils.getXPathElement(outXml, "/XPEDXMyItemsList");
								setModel("getXPEDXMyItemsListDetail",eleMyItemsList);
								//this.page.showResultMessage("Update Successful!!");
								page.getShell().close();
								//page.getParent().redraw();
								myItemListref1.getMyBehavior().loadItemsList();
								
							}
					}
				}
			}
	 }

	 private Element updateLineTypesModel(Element outXml) {
		 
		 Document root = YRCXmlUtils.createDocument("CommonCodeList");
		ArrayList list = YRCXmlUtils.getChildren(outXml, "CommonCode");
		if (list.size() > 0) {
			Iterator<Element> iterator = list.iterator();
			while (iterator.hasNext()) {
				Element ele = iterator.next();
				if ("S".equals(ele.getAttribute("CodeValue"))
						|| "P".equals(ele.getAttribute("CodeValue"))) {
					Element temp = YRCXmlUtils.createChild(root
							.getDocumentElement(), "CommonCode");
					temp.setAttribute("CodeValue", ele
							.getAttribute("CodeValue"));
					temp.setAttribute("CodeShortDescription", ele
							.getAttribute("CodeShortDescription"));
					temp.setAttribute("CodeLongDescription", ele
							.getAttribute("CodeLongDescription"));
				}

			}
		}
		Element temp = YRCXmlUtils.createChild(root.getDocumentElement(),
				"CommonCode");
		/*temp.setAttribute("CodeValue", "NC");
		temp.setAttribute("CodeShortDescription", "Non-Catalog");
		temp.setAttribute("CodeLongDescription", "Non-Catalog");*/
		Element select = YRCXmlUtils.createChild(root.getDocumentElement(),
				"SelectItem");
		select.setAttribute("CodeValue", "P");
		;
		return root.getDocumentElement(); 		
	}

	public void getItemInfo(String strItemID, String strLineType) {
		 String[] apinames = {"XPXGetSKUDetailsService"};
			if(!YRCPlatformUI.isVoid(strItemID) && !searchSKUPerformed){
//				setFieldValue("txtSKU", "");
//				setFieldValue("comboSKU", "");
				Element eleSelectedSKU  = YRCXmlUtils.createDocument("Item").getDocumentElement();;
				eleSelectedSKU.setAttribute("SKU", strItemID);
				if("S".equals(strLineType))
					eleSelectedSKU.setAttribute("SKUType", "CPN");
				else
					eleSelectedSKU.setAttribute("SKUType", "LPC");
				
				eleSelectedSKU.setAttribute("OrganizationCode", YRCXmlUtils.getAttribute(this.inputEle, "EnterpriseCode"));
				Document doc=eleSelectedSKU.getOwnerDocument();
				Document[] docInput={doc};
				callApis(apinames, docInput);

			} else {
				searchSKUPerformed = false;
			}
		}

	public void addItemToList() {
		 String[] apinames = {"updateXPEDXMyItemsList"};
		
		Document doc=null;
		String unitOfMeasure="";
		String name="";
		
		
		Document[] docInput = {YRCXmlUtils.createDocument("XPEDXMyItemsList")};
		Element eleMyItemList = docInput[0].getDocumentElement();
		eleMyItemList.setAttribute("MyItemsListKey", inputEle.getAttribute("MyItemsListKey"));
		String strCreatedByUsername = YRCPlatformUI.getUserElement().getAttribute("Username");
		String strModifyuserid = YRCPlatformUI.getUserElement().getAttribute("Loginid");
		if(!YRCPlatformUI.isVoid(strCreatedByUsername))
		{
			eleMyItemList.setAttribute("ModifyUserName", strCreatedByUsername);
			eleMyItemList.setAttribute("Modifyuserid", strModifyuserid);
		}
		Element ItemInfoEle=getModel("itemInfo");
		if(!YRCPlatformUI.isVoid(ItemInfoEle)){
			YRCXmlUtils.getString(ItemInfoEle);
			unitOfMeasure=ItemInfoEle.getAttribute("UnitOfMeasure");
			name=YRCXmlUtils.getXPathElement(ItemInfoEle, "/Order/PrimaryInformation").getAttribute("ExtendedDescription");
		}
		else{
			unitOfMeasure=getFieldValue("txtUOM");
			
		}
		
		Element eleMyItemsList1 = YRCXmlUtils.createChild(eleMyItemList, "XPEDXMyItemsItemsList");
		//Populate the object
		Element eleMyItemsList = YRCXmlUtils.createChild(eleMyItemsList1, "XPEDXMyItemsItems");	
		eleMyItemsList.setAttribute("Name",name);
		eleMyItemsList.setAttribute("ItemId",getFieldValue("txtItemId"));
		System.out.println(getFieldValue("comboLineType"));
		if("NC".equalsIgnoreCase(getFieldValue("comboLineType"))){
			eleMyItemsList.setAttribute("ItemType","99");
		}
		if("S".equalsIgnoreCase(getFieldValue("comboLineType"))){
			eleMyItemsList.setAttribute("ItemType","3");
		}
		if("P".equalsIgnoreCase(getFieldValue("comboLineType"))){
			eleMyItemsList.setAttribute("ItemType","1");
		}
		if(YRCPlatformUI.isVoid(getFieldValue("txtQuantity")))
		{
			eleMyItemsList.setAttribute("Qty","0.00");
		}
		else
		{
			eleMyItemsList.setAttribute("Qty",getFieldValue("txtQuantity"));			
		}
		eleMyItemsList.setAttribute("UomId",unitOfMeasure);
		eleMyItemsList.setAttribute("Desc",getFieldValue("txtItemDesc"));
		eleMyItemsList.setAttribute("JobId",getFieldValue("txtCustLineAccount"));
		/*eleMyItemsList.setAttribute("ItemCustomField1",getFieldValue("txtCustLineNoOne"));
		eleMyItemsList.setAttribute("ItemCustomField2",getFieldValue("txtCustLineNoTwo"));*/
		eleMyItemsList.setAttribute("ItemPoNumber",getFieldValue("txtLinePO"));
		int maxValue = getMaxVaue();
		maxValue = maxValue + 1;
		String aString = Integer.toString(maxValue);
		eleMyItemsList.setAttribute("ItemOrder", aString);
		doc=eleMyItemsList.getOwnerDocument();
		callApis(apinames, docInput);
		((XPXManageMyItemsListEditor)YRCDesktopUI.getCurrentPart()).showBusy(true);
	}
	
	
	private int getMaxVaue() {
		int[] nums = new int[1000000];
		NodeList nlItems = inputEle.getElementsByTagName("XPEDXMyItemsItems");
		for( int i=0; i<nlItems.getLength(); i++){
	    	eleXpath = (Element)nlItems.item(i);
	    	int aInt = Integer.parseInt(eleXpath.getAttribute("ItemOrder"));
	    	nums[i] = aInt;
		}
		Arrays.sort(nums); 
		return nums[nums.length-1];
	}
}
