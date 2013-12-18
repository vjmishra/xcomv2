package com.xpedx.sterling.rcp.pca.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXPathUtils;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXUtils {
	

	public static String PRICE_AND_AVAILABILITY_SRVC= "XPXPnAService";
	public static String CHARGE_TYPE= "M";
	public static String LEGACY_PRODUCT_TYPE= "P";
	public static  HashMap<String, String> divisionMap = new HashMap<String, String>();
	public static  HashMap<String, String> SorteddivisionMap = new HashMap<String, String>();
	public static  Element orgList = YRCXmlUtils.createDocument("OrganizationList").getDocumentElement();
	public static String masterCustomerID ;
	public static String CustomerName;
	public static String customerKey;
	public static Element elemModel;
	// XB-519
	public static String polbl;
	public static String lineAcc;
	public static ArrayList refDiv;
	public static HashMap<String, String> webConfNumMap = new HashMap<String, String>(); // User from Order Search Page
	/**
	 * Add's a Paint Listener to the given composite with background as 'TaskComposite'.
	 */
	public static void paintPanel(Composite composite) {
		final Composite tmpComp = composite;
		tmpComp.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {
				GC gc = new GC(tmpComp);
				Rectangle r = tmpComp.getClientArea();
				gc.setForeground(YRCPlatformUI.getBackGroundColor("TaskComposite"));
				gc.setForeground(Display.getCurrent().getSystemColor(15));
				gc.drawLine((r.x + r.width) - 1, r.y, (r.x + r.width) - 1, (r.y + r.height) - 1);
				gc.drawLine(r.x, (r.y + r.height) - 1, (r.x + r.width) - 1, (r.y + r.height) - 1);
				gc.drawLine(r.x, r.y, r.x + r.width, r.y);
				gc.drawLine(r.x, r.y, r.x, (r.y + r.height) - 1);
				gc.dispose();
			}

		});
	}
	
	public static void addGradientPanelHeader(Composite composite, String sPanelHeader, boolean getFromBundle)
    {
		final Composite composite2 =composite;
        GridData gridData182 = new GridData();
        gridData182.heightHint = 17;
        gridData182.horizontalAlignment = 4;
        gridData182.horizontalSpan = 1;
        gridData182.grabExcessHorizontalSpace = true;
        final CLabel cLabel = new CLabel(composite2, 0);
        if(getFromBundle)
            sPanelHeader = YRCPlatformUI.getString(sPanelHeader);
        cLabel.setText(sPanelHeader);
        cLabel.setFont(YRCPlatformUI.getFont("PanelHeader"));
        cLabel.setForeground(YRCPlatformUI.getForeGroundColor("HeaderPanel.0"));
        cLabel.setBackground(
        		new Color[] { YRCPlatformUI.getBackGroundColor("HeaderPanel"), YRCPlatformUI.getBackGroundColor("HeaderPanel.3")}, 
        		new int[] { 100 }, 
        		true);
        cLabel.setData("name", "PanelHeaderLabel");
        cLabel.setSize(YRCPlatformUI.getWidth("PanelHeader"), YRCPlatformUI.getHeight("PanelHeader"));
        GridData gridData10 = new GridData();
        gridData10.horizontalAlignment = 4;
        gridData10.grabExcessHorizontalSpace = true;
        gridData10.heightHint = 20;
        gridData10.horizontalSpan = ((GridLayout)composite.getLayout()).numColumns;
        gridData10.verticalAlignment = 2;
        cLabel.setLayoutData(gridData10);
        composite2.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {
				GC gc = new GC(composite2);
				Rectangle r = composite2.getClientArea();
				gc.setForeground(YRCPlatformUI.getBackGroundColor("PanelHeader"));
				gc.drawRectangle(r.x, r.y, r.width - 1, r.height - 1);
				gc.setForeground(Display.getCurrent().getSystemColor(15));
				Rectangle r1 = cLabel.getBounds();
				gc.drawLine(r.x, r1.y + r1.height, (r.x + r.width) - 2, r1.y + r1.height);
				gc.setForeground(YRCPlatformUI.getBackGroundColor("PanelHeader"));
				gc.setBackground(YRCPlatformUI.getBackGroundColor("PanelHeader"));
				gc.fillRectangle(r.x, r.y, r.width, r1.height + 1);
				gc.setForeground(Display.getCurrent().getSystemColor(15));
				gc.drawLine((r.x + r.width) - 1, r.y, (r.x + r.width) - 1, (r.y + r.height) - 1);
				gc.drawLine(r.x, (r.y + r.height) - 1, (r.x + r.width) - 1, (r.y + r.height) - 1);
				gc.drawLine(r.x, r.y, r.x + r.width, r.y);
				gc.drawLine(r.x, r.y, r.x, (r.y + r.height) - 1);
				gc.dispose();
			}
		});
	}


	public static void openItemSearch(Object inputObject, String ItemidDescription, String org, String sUserID, boolean lookupMode) {
		Element apiElement = YRCXmlUtils.createDocument("Item").getDocumentElement();
		apiElement.setAttribute("ItemID", ItemidDescription);
		apiElement.setAttribute("ItemIDQryType", "LIKE");
		apiElement.setAttribute("EnterpriseCode", org);
		apiElement.setAttribute("CallingOrganizationCode", org);
		apiElement.setAttribute("HideDropDown", "Y");
		if (!YRCPlatformUI.isVoid(sUserID)) {
			Element customerInformation = YRCXmlUtils.createChild(apiElement, "CustomerInformation");
			customerInformation.setAttribute("CustomerID", sUserID);
		}
		Element itemElem = YRCXmlUtils.createChild(YRCXmlUtils.createChild(apiElement, "Input"), "Item");
		itemElem.setAttribute("EnterpriseCode", org);
		itemElem.setAttribute("CallingOrganizationCode", org);
		itemElem.setAttribute("ItemID", ItemidDescription);
		Element itemSearchConfigElem = YRCXmlUtils.createDocument("Data").getDocumentElement();
		if (lookupMode)
			itemSearchConfigElem.setAttribute("LookupMode", "Y");
		else
			itemSearchConfigElem.setAttribute("LookupMode", "N");
		HashMap itemSearchData = new HashMap();
		itemSearchData.put("ConfigInputData", itemSearchConfigElem);
		YRCPlatformUI.closeEditor("com.yantra.pca.ycd.rcp.editors.YCDItemSearchEditor", false);
		YRCPlatformUI.openEditor("com.yantra.pca.ycd.rcp.editors.YCDItemSearchEditor", new YRCEditorInput(apiElement, apiElement, new String[] { "ItemID" }, "YCD_TASK_ADVANCED_ITEM_SEARCH"));
		// YCDEditorInputUtils.openEditor("com.yantra.pca.ycd.rcp.editors.YCDItemSearchEditor",
		// new YCDEditorInput(apiElement, apiElement, new String[] { "ItemID"},
		// "YCD_TASK_ADVANCED_ITEM_SEARCH", itemSearchData));
	}

	public static Element createNewDraftOrderElement(Element element, String enterpriseCode) {
		if (YRCPlatformUI.isVoid(element))
			return createNewDraftOrderElement(enterpriseCode);
		
		Element orderDetails = YRCXmlUtils.createDocument("Order").getDocumentElement();
		if (!YRCPlatformUI.isVoid(element.getAttribute("EnterpriseCode")))
			enterpriseCode = element.getAttribute("EnterpriseCode");
		
		String ohk = element.getAttribute("OrderHeaderKey");
		if (!YRCPlatformUI.isVoid(ohk))
			orderDetails.setAttribute("OrderHeaderKey", ohk);
		
		if (!YRCPlatformUI.isVoid(element.getAttribute("SellerOrganizationCode")))
			orderDetails.setAttribute("SellerOrganizationCode", element.getAttribute("SellerOrganizationCode"));
		
		orderDetails.setAttribute("EnterpriseCode", enterpriseCode);
		orderDetails.setAttribute("DocumentType", "0001");
		return orderDetails;
	}

	public static Element createNewDraftOrderElement(String enterpriseCode) {
		Element orderDetails = YRCXmlUtils.createDocument("Order").getDocumentElement();
		orderDetails.setAttribute("EnterpriseCode", enterpriseCode);
		orderDetails.setAttribute("DocumentType", "0001");
		orderDetails.setAttribute("SellerOrganizationCode", YRCPlatformUI.getUserElement().getAttribute("Node"));
		return orderDetails;
	}
	
	public static void addCommentElement(Element ele, String comments,
			String instructionType, String strInstructionKey) {
		Element instructionsListElem = YRCXmlUtils.getChildElement(ele,
				"Instructions", true);
		Element instructionElem = null;
//		int sequenceNo = 0;
		if(!YRCPlatformUI.isVoid(comments)){
			if ("HEADER".equals(instructionType)) {
				instructionElem = (Element) YRCXPathUtils
						.evaluate(
								instructionsListElem,
								"/Instructions/Instruction[@InstructionType='HEADER']",
								XPathConstants.NODE);
				if (YRCPlatformUI.isVoid(instructionElem)) {
					instructionElem = YRCXmlUtils.createChild(instructionsListElem, "Instruction");
				}
				instructionElem.setAttribute("InstructionType", "HEADER");
				instructionElem.setAttribute("InstructionDetailKey",strInstructionKey );
			} else if ("LINE".equals(instructionType)) {
				instructionElem = (Element) YRCXPathUtils
						.evaluate(
								instructionsListElem,
								"/Instructions/Instruction[@InstructionType='LINE']",
								XPathConstants.NODE);
				if (YRCPlatformUI.isVoid(instructionElem)) {
					instructionElem = YRCXmlUtils.createChild(instructionsListElem, "Instruction");
				}
				instructionElem.setAttribute("InstructionType", "LINE");
				instructionElem.setAttribute("InstructionDetailKey",strInstructionKey );
			} else if ("INTERNAL".equals(instructionType)) {
				instructionElem = (Element) YRCXPathUtils
						.evaluate(
								instructionsListElem,
								"/Instructions/Instruction[@InstructionType='INTERNAL']",
								XPathConstants.NODE);
				if (YRCPlatformUI.isVoid(instructionElem)) {
					instructionElem = YRCXmlUtils.createChild(instructionsListElem, "Instruction");
				}
				instructionElem.setAttribute("InstructionType", "INTERNAL");
				instructionElem.setAttribute("InstructionDetailKey",strInstructionKey );
			}
			// getFieldValue("txtComment")
			instructionElem.setAttribute("InstructionText", comments);
		}
		//START- Added for JIRA 3910
		else if(YRCPlatformUI.isVoid(comments))
		{
			if ("INTERNAL".equals(instructionType)) {
				instructionElem = (Element) YRCXPathUtils
						.evaluate(
								instructionsListElem,
								"/Instructions/Instruction[@InstructionType='INTERNAL']",
								XPathConstants.NODE);
				if (YRCPlatformUI.isVoid(instructionElem)) {
					instructionElem = YRCXmlUtils.createChild(instructionsListElem, "Instruction");
				}
				instructionElem.setAttribute("InstructionType", "INTERNAL");
				instructionElem.setAttribute("InstructionDetailKey",strInstructionKey );
				instructionElem.setAttribute("InstructionText", "DummyText");
				instructionElem.setAttribute("Action", "REMOVE");
			}
		}
		//End- JIRA 3910
	}
	public static Document preparePnAInputDocForOrderLines(Element orderDetailsElement, Element custDetailsElement) {
		Document returnDoc = null;
		Element orderLinesElement = YRCXmlUtils.getChildElement(orderDetailsElement, "OrderLines");
		ArrayList listOrderLine = YRCXmlUtils.getChildren(orderLinesElement, "OrderLine");
		if(listOrderLine.size() ==0){
			return null;
		}
		Element custDetailsExtnElement = YRCXmlUtils.getChildElement(custDetailsElement, "Extn");
		
		returnDoc = YRCXmlUtils.createFromString("<PriceAndAvailability><Items></Items></PriceAndAvailability>");
		Element eleReturn = returnDoc.getDocumentElement();
//		String customerID = orderDetailsElement.getAttribute("BuyerOrganizationCode");
		String customerID = custDetailsExtnElement.getAttribute("ExtnLegacyCustNumber");
//		Element extnElement = YRCXmlUtils.getChildElement(orderDetailsElement, "Extn");
		String envId = custDetailsExtnElement.getAttribute("ExtnEnvironmentCode");
		String custEnvId = custDetailsExtnElement.getAttribute("ExtnOrigEnvironmentCode");
		String Company = custDetailsExtnElement.getAttribute("ExtnCompanyCode");
		//String CustomerBranch = extnElement.getAttribute("ExtnOrderDivision");
		String CustomerBranch = custDetailsExtnElement.getAttribute("ExtnCustomerDivision");
		String ShipToSuffix = custDetailsExtnElement.getAttribute("ExtnShipToSuffix");//??
		//??
//		String OrderBranch = !YRCPlatformUI.isVoid(extnElement.getAttribute("ExtnCustOrderBranch"))?
//				extnElement.getAttribute("ExtnCustOrderBranch"):
//					extnElement.getAttribute("ExtnShipFromBranch");
		String OrderBranch = !YRCPlatformUI.isVoid(custDetailsExtnElement.getAttribute("ExtnCustOrderBranch"))?
				custDetailsExtnElement.getAttribute("ExtnCustOrderBranch"):
					custDetailsExtnElement.getAttribute("ExtnShipFromBranch");		
		addXMLTag(returnDoc,eleReturn,"SourceIndicator","1");//1 for WebChannel//??
		addXMLTag(returnDoc,eleReturn,"EnvironmentId",envId);
		addXMLTag(returnDoc, eleReturn, "CustomerEnvironmentId", custEnvId);
		addXMLTag(returnDoc,eleReturn,"Company",Company);
		addXMLTag(returnDoc,eleReturn,"CustomerBranch",CustomerBranch);
		addXMLTag(returnDoc,eleReturn,"CustomerNumber",customerID);
		addXMLTag(returnDoc,eleReturn,"ShipToSuffix",ShipToSuffix);
		addXMLTag(returnDoc,eleReturn,"OrderBranch",OrderBranch);
		NodeList inputNodeList = returnDoc.getElementsByTagName("Items");
		Element inputNodeListElemt = (Element)inputNodeList.item(0);	
		
		boolean isAnyOrderLineExists = false;
		for (int i = 0; i < listOrderLine.size(); i++){
			Element eleItem = YRCXmlUtils.createChild(inputNodeListElemt, "Item");
			Element orderLine =(Element) listOrderLine.get(i);
			if(XPXConstants.CHARGE_TYPE.equals(orderLine.getAttribute("LineType"))){
				continue;
			}
			//addXMLTag(returnDoc,eleItem,"LineNumber",YRCXmlUtils.getAttribute(orderLine, "OrderLineKey"));
			String itemID = YRCXmlUtils.getAttributeValue(orderLine, "/OrderLine/Item/@ItemID");
			if(isInteger(itemID)){
			addXMLTag(returnDoc,eleItem,"LineNumber",YRCXmlUtils.getAttribute(orderLine, "PrimeLineNo"));
			addXMLTag(returnDoc,eleItem,"LegacyProductCode",YRCXmlUtils.getAttributeValue(orderLine, "/OrderLine/Item/@ItemID"));
			addXMLTag(returnDoc,eleItem,"RequestedQtyUOM",YRCXmlUtils.getAttributeValue(orderLine, "/OrderLine/OrderLineTranQuantity/@TransactionalUOM"));
			addXMLTag(returnDoc,eleItem,"RequestedQty",YRCXmlUtils.getAttributeValue(orderLine, "/OrderLine/OrderLineTranQuantity/@OrderedQty"));
			}
		}
		
		return returnDoc;
	}
	
	public static boolean isInteger( String input )  
	{  
	   try  
	   {  
	      Integer.parseInt( input );  
	      return true;  
	   }  
	   catch( Exception e)  
	   {  
	      return false;  
	   }  
	}  
	public static void addXMLTag(Document returnDoc, Element element, String tagName, String tagValue){
		Element textNode = YRCXmlUtils.createChild(element, tagName);
		Text txt = returnDoc.createTextNode(tagName);
		txt.setTextContent(tagValue);
		textNode.appendChild(txt);
	}
	
	/**
	 * Compares OrderType for 'Customer' Order.
	 * <br>
	 * <b>Required:</b> OrderType Attribute should be passes in Order element.
	 * 
	 * @param eleOrder
	 * @return
	 */
	public static boolean isCustomerOrder(Element eleOrder) {
		return "Customer".equals(YRCXmlUtils.getAttribute(eleOrder, "OrderType"));
	}
	/**
	 * Compares OrderType for FullFillment Order.
	 * <br>
	 * <b>Required:</b> OrderType Attribute should be passes in Order element.
	 * 
	 * @param eleOrder
	 * @return
	 */
	public static boolean isFullFillmentOrder(Element eleOrder) {
		String strOrderType = YRCXmlUtils.getAttribute(eleOrder, "OrderType");
		return isFulFillmentOrder(strOrderType);
	}
	
	/**
	 * Compares OrderType for FullFillment Order.
	 * <br>
	 * <b>Required:</b> strOrderType - Order Type String value.
	 * 
	 * @param strOrderType
	 * @return
	 */
	public static boolean isFulFillmentOrder(String strOrderType) {
		
		if(YRCPlatformUI.isVoid(strOrderType))
			return false;
		return ARRAY_OF_FULFILLMENT_ORDER_TYPES.contains(strOrderType);
	}
	
	/**
	 * Returns true if Order is in Placed Status. 
	 * <br>
	 * <b>Required:</b> MinOrderStatus and MaxOrderStatus Attributes should be passes in Order element.
	 * 
	 * @param eleOrder
	 * @return
	 */
	public static boolean isPlaced(Element eleOrder) {
		return XPXConstants.ORD_STTS_PLACED.equals(eleOrder.getAttribute("MaxOrderStatus")) 
				|| XPXConstants.ORD_STTS_PLACED.equals(eleOrder.getAttribute("MinOrderStatus"));
	}
	
	/**
	 * Returns true if Order beyond Placed Status. 
	 * <br>
	 * <b>Required:</b> MinOrderStatus and MaxOrderStatus Attributes should be passes in Order element.
	 * 
	 * @param eleOrder
	 * @return
	 */
	public static boolean isBeyondPlaced(Element eleOrder) {
		if(XPXConstants.ORD_STTS_PLACED.compareTo(eleOrder.getAttribute("MaxOrderStatus"))<0 
				|| XPXConstants.ORD_STTS_PLACED.compareTo(eleOrder.getAttribute("MinOrderStatus"))<0){
			return true;
		}
		return false;
	}

	/**
	 * Returns true if order is a Reference order. 
	 * <br>
	 * <b>Required:</b> DocumentType Attribute should be passes in Order element.
	 * 
	 * @param orderDetailsElem
	 * @return
	 */
	public static boolean isReferenceOrder(Element orderDetailsElem) {
		return XPXConstants.DOC_TYPE_REFERENCE_ORDER.equals(orderDetailsElem.getAttribute("DocumentType"));
	}
	
	public static void paintControl(PaintEvent e)
    {
        Composite caller = (Composite)e.widget;
        caller.setBackgroundImage(YRCPlatformUI.getImage("PanelHeaderImage"));
        Composite child = getFirstChildComposite(caller);	
        if(child == null)
            return;
        child.setData("yrc:customType", "PanelHeader");
        child.setBackgroundMode(2);
        child.setBackgroundImage(YRCPlatformUI.getImage("PanelHeaderImage"));
        child.setFont(YRCPlatformUI.getFont("PanelHeader"));
        child.addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent e)
            {
                GC gc = new GC((Composite)e.widget);
                Rectangle r = ((Composite)e.widget).getBounds();
                gc.setForeground(Display.getCurrent().getSystemColor(1));
                gc.dispose();
            }

        }
        );
        for(int i = 0; i < child.getChildren().length; i++)
        {
            Control c = child.getChildren()[i];
            if(c instanceof Link)
            {
                Link link = (Link)c;
                link.setData("yrc:customType", "PanelHeader");
                link.setForeground(YRCPlatformUI.getForeGroundColor("Link"));
                link.setFont(YRCPlatformUI.getFont("PanelHeader"));
                caller.layout(true, true);
            } else
            {
                c.setData("yrc:customType", "NoTheme");
                c.setForeground(YRCPlatformUI.getForeGroundColor("NoTheme"));
                c.setBackground(YRCPlatformUI.getBackGroundColor("NoTheme"));
                c.setFont(YRCPlatformUI.getFont("PanelHeader"));
            }
        }

        GC gc = new GC(caller);
        Rectangle r = caller.getClientArea();
        gc.setForeground(YRCPlatformUI.getBackGroundColor("PanelHeader"));
        gc.drawRectangle(r.x, r.y, r.width - 1, r.height - 1);
        gc.setForeground(Display.getCurrent().getSystemColor(15));
        Rectangle r1 = getFirstChildComposite(caller).getBounds();
        gc.drawLine(r.x, r1.y + r1.height, (r.x + r.width) - 2, r1.y + r1.height);
        gc.setForeground(YRCPlatformUI.getBackGroundColor("PanelHeader"));
        gc.setBackground(YRCPlatformUI.getBackGroundColor("PanelHeader"));
        gc.fillGradientRectangle(r.x, r.y, r.width, r1.height + 1, true);
        gc.setForeground(Display.getCurrent().getSystemColor(15));
        gc.drawLine((r.x + r.width) - 1, r.y, (r.x + r.width) - 1, (r.y + r.height) - 1);
        gc.drawLine(r.x, (r.y + r.height) - 1, (r.x + r.width) - 1, (r.y + r.height) - 1);
        gc.drawLine(r.x, r.y, r.x + r.width, r.y);
        gc.drawLine(r.x, r.y, r.x, (r.y + r.height) - 1);
        gc.dispose();
    }
	private static Composite getFirstChildComposite(Composite caller)
    {
        for(int i = 0; i < caller.getChildren().length; i++)
            if(caller.getChildren()[i] instanceof Composite)
                return (Composite)caller.getChildren()[i];

        return null;
    }

	public static Element getOrganizationListInput(String userID)
    {
        Element elemOrg = YRCXmlUtils.createDocument("Organization").getDocumentElement();
        Element elemOrgRoleList = YRCXmlUtils.createChild(elemOrg, "OrgRoleList");
        Element elemOrgRole = YRCXmlUtils.createChild(elemOrgRoleList, "OrgRole");
        elemOrgRole.setAttribute("RoleKey", "ENTERPRISE");
        Element elemDataAccess = YRCXmlUtils.createChild(elemOrg, "DataAccessFilter");
        elemDataAccess.setAttribute("UserId", userID);
        return elemOrg;
    }
	
	/**
	 * Appends EnvtId and Node in this format[if both are not null]:NODE_ENVTID 
	 * @param strEnvtId
	 * @param strNode
	 * @return
	 */
	public static String updateNodeSyntax(String strEnvtId, String strNode) {
		if(YRCPlatformUI.isVoid(strNode) || YRCPlatformUI.isVoid(strEnvtId)){
			strNode = "";
		} else {
			strNode = strNode+"_"+strEnvtId;
		}
		return strNode;
	}
	

	public static boolean validateEmail(String enteredEmail){
		String patternString = "[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?";
        Pattern VALID_PATTERN = Pattern.compile(patternString);
        String emailRegEx = VALID_PATTERN.pattern();
        return enteredEmail.matches(emailRegEx); 
	}
	
	public static void createOrganizationList(Element outXml) {
		List nodesList=YRCXmlUtils.getChildren(outXml, "Organization");
		
		for (Object object : nodesList) {
			Element ele=(Element) object;
			String orgCode=ele.getAttribute("OrganizationCode");
			String OrganizationName=ele.getAttribute("OrganizationName");
			divisionMap.put(orgCode, OrganizationName);
			SorteddivisionMap.put(OrganizationName, orgCode);
			
		}
	}
	
	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static String getFormattedOrderNumber(Element obj) {
		
		String fmtLegacyOrderNumber  = "";
		
		try{
		Element orderElement = (Element)obj;
		String divisionNo = YRCXmlUtils.getXPathElement(orderElement, "/Order/Extn").getAttribute("ExtnOrderDivision");
		String legacyNo = YRCXmlUtils.getXPathElement(orderElement, "/Order/Extn").getAttribute("ExtnLegacyOrderNo");
		String generationNo = YRCXmlUtils.getXPathElement(orderElement, "/Order/Extn").getAttribute("ExtnGenerationNo");
		
		fmtLegacyOrderNumber = XPXUtils.getFormattedOrderNumber(divisionNo, legacyNo, generationNo) ;
		
		return  fmtLegacyOrderNumber;
		}
		catch (Exception e) {
			System.out.println( "XPXUtils : getFormattedOrderNumber has exception : " + e.getMessage() );
		}
		
		return fmtLegacyOrderNumber;
		
	}
	
	/**
	 * 
	 * @param orderBranch
	 * @param legacyOrderNum
	 * @param generationNum
	 * @return
	 */
	public static String getFormattedOrderNumber(String orderBranch, String  legacyOrderNum, String generationNum )
	{
		StringBuffer sb = new StringBuffer();
		
		if( legacyOrderNum != null  )
		{
			
			if( (generationNum == null ) || (generationNum.trim().length() == 0) )
				generationNum = "0";
			
			if( (orderBranch == null ) || (orderBranch.trim().length() == 0) )
				orderBranch = "";
	
			
			if(orderBranch!=null && orderBranch.length()>0)
			{
				sb.append(orderBranch);
				sb.append("-");
			}
			if(legacyOrderNum!=null && legacyOrderNum.length()>0)
			{				
				sb.append(legacyOrderNum);				
				sb.append("-");
			}
			if(generationNum!=null && generationNum.length()>0)
			{
				if(generationNum.trim().length()==1)
				{
					generationNum="0"+generationNum;
				}				
				sb.append(generationNum);
				
			}
		}
		
		//Strip off additional character in formatted legacyNumber.
		return sb.toString().replaceAll("_M","");
	}

	
	public static void createOrganizationListElemForUser(Element outXml) {
		orgList = outXml;
	}
	
	public static void getOrganizationListElemForUser(Element outXml) {
		orgList = outXml;
	}
	
	/**
	 * Used to open a specified URL (with Query Scring)in the System Browser.
	 * 
	 * @param url
	 * @param queryString
	 */
	public static void accessURL(String url,String queryString) {
		
		Program program = Program.findProgram("html");
		if (program == null)
			program = Program.findProgram("htm");
		if (program != null) {
			if(YRCPlatformUI.isVoid(queryString))
				program.execute(url);
			else
				program.execute(url+"?"+queryString);
		}
		else
			// show an error that web browser cannot be launched.
			// TODO externalize this.
			YRCPlatformUI.showInformation("Error","Unable_to_Launch_Browser");
	}
	
	public static ArrayList<String> ARRAY_OF_FULFILLMENT_ORDER_TYPES = new ArrayList<String>(4);
	
	static {
		ARRAY_OF_FULFILLMENT_ORDER_TYPES.add("SPECIAL_ORDER");
		ARRAY_OF_FULFILLMENT_ORDER_TYPES.add("STOCK_ORDER");
		ARRAY_OF_FULFILLMENT_ORDER_TYPES.add("THIRD_PARTY");
		ARRAY_OF_FULFILLMENT_ORDER_TYPES.add("DIRECT_ORDER");
	}
	
	/**
	 * Compares OrderStatus for 'Cancelled' Order.
	 * <br>
	 * <b>Required:</b> OrderType Attribute should be passes in Order element.
	 * 
	 * @param eleOrder
	 * @return
	 */
	public static boolean isCancelOrder(Element eleOrder) {
		String status=YRCXmlUtils.getAttributeValue(eleOrder,"/Order/@Status");
		return "Cancelled".equals(status);
	}

	public static String getMasterCustomerID() {
		return masterCustomerID;
	}

	public static void setMasterCustomerID(String masterCustomerID) {
		XPXUtils.masterCustomerID = masterCustomerID;
	}

	public static String getCustomerName() {
		return CustomerName;
	}

	public static void setCustomerName(String customerName) {
		XPXUtils.CustomerName = customerName;
	}

	public static Element getElemModel() {
		return elemModel;
	}

	public static void setElemModel(Element elemModel) {
		XPXUtils.elemModel = elemModel;
	}

	public static String getCustomerKey() {
		return customerKey;
	}

	public static void setCustomerKey(String customerKey) {
		XPXUtils.customerKey = customerKey;
	}
	//XB-519
	public static void setPoLbl(String polbl){
		XPXUtils.polbl = polbl;
	}
	public static String getPoLbl() {
		return polbl;
	}
	
	public static void setLineAcc(String polbl){
		XPXUtils.lineAcc = polbl;
	}
	public static String getLineAcc() {
		return lineAcc;
	}

	public static ArrayList getRefDiv() {
		return refDiv;
	}

	public static void setRefDiv(ArrayList refDiv) {
		XPXUtils.refDiv = refDiv;
	}

	public static HashMap<String, String> getWebConfNumMap() {
		return webConfNumMap;
	}

	public static void setWebConfNumMap(HashMap<String, String> webConfNumMap) {
		XPXUtils.webConfNumMap = webConfNumMap;
	}
	
	
}
