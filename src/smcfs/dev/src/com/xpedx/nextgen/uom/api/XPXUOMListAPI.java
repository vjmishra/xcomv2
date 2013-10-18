package com.xpedx.nextgen.uom.api;
 
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
 
import javax.xml.xpath.XPathExpressionException;
 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
 
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNode;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;
 
public class XPXUOMListAPI implements YIFCustomApi {
 
    /** API object. */
    private static YIFApi api = null;
    private static YFCLogCategory log;
    private String lowestConvUOM = "";
    private int currentConversion;
    private String ExtnIsCustUOMExcl = "";
    //Added for XB-687    
    public  ArrayList<String> customerUOMList = new ArrayList<String>();
    public  ArrayList<String> getCustomerUOMList() {
        return customerUOMList;
    }
 
    public  void setCustomerUOMList(ArrayList<String> customerUOMList) {
        this.customerUOMList = customerUOMList;
    }
 
    private String baseUOM = "";
    private boolean complexQuery = false;
    private Document itemsXrefDoc;
    private Document itemExtnDoc;
     
 
    public void setProperties(Properties params) throws Exception {
        // this.props = props;
    }
 
    static {
        try {
            log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
            api = YIFClientFactory.getInstance().getApi();
        } catch (YIFClientCreationException e1) {
            log.debug(e1.getMessage());
        }
    }
 
    public Document getXpedxUOMList(YFSEnvironment env, Document inXML)
            throws XPathExpressionException, YFSException, YIFClientCreationException, RemoteException {
        log.beginTimer("XPXUOMListAPI:getUOMList started...");
        LinkedHashMap<String, String> wUOMsToConversionFactors = new LinkedHashMap<String, String>();
        String LegacyCustomerNumber = "";
        String companyCode = "";
        String useOrderMulUOMFlag = "";
        String orderMultiple = "";
        String customerDivision = "";
        String itemID = "";
         
        YFCDocument complexQueryOutDoc = YFCDocument.createDocument("ItemList");
 
        YFCDocument inDoc = YFCDocument.getDocumentFor(inXML);
        YFCElement complexQueryElement = inDoc.getDocumentElement().getChildElement("ComplexQuery");
        if(complexQueryElement != null) {
            complexQuery = true;
        }
         
        Element documentElement = inXML.getDocumentElement();
        String customerID = documentElement.getAttribute("CustomerID");
        itemID = documentElement.getAttribute("ItemID");
        String storeFrontId = documentElement.getAttribute("OrganizationCode");
        //Added to identify this request is from B2B order flow or not
        String entryType=documentElement.getAttribute("EntryType");
 
        String[] customerIDTokens = customerID.split("\\-");
        if (customerIDTokens != null && customerIDTokens.length > 1) {
            LegacyCustomerNumber = customerIDTokens[1];
        }
 
        HashMap<String, String> customerDetails = getCustomerDetails(env, inXML);
 
        companyCode = customerDetails.get("companyCode");
        customerDivision = customerDetails.get("customerDivision");
        useOrderMulUOMFlag = customerDetails.get("useOrderMulUOMFlag");
 
        YFCDocument inputDocument = YFCDocument.createDocument("Item");
        YFCElement inputElement = inputDocument.getDocumentElement();
        if(complexQuery) {
            inputElement.appendChild(inputDocument.importNode(complexQueryElement, true));
        }  
        else {
            inputElement.setAttribute("ItemID", itemID);
        }
        //Begin: Fix for Jira#2007 - RUgrani
        //inputElement.setAttribute("OrganizationCode", storeFrontId);
        inputElement.setAttribute("CallingOrganizationCode", storeFrontId);
        //End: Fix for Jira#2007 - RUgrani
        //Removing the Extn Template as we get all the XPXItemExtn for that item. We are making separate call
        env.setApiTemplate("getItemList", SCXmlUtil.createFromString(""
                + "<ItemList><Item><AlternateUOMList><AlternateUOM />"
                + "</AlternateUOMList></Item></ItemList>"));
 
        api = YIFClientFactory.getInstance().getApi();
        Document outputListDocument = api.invoke(env, "getItemList",
                inputDocument.getDocument());
        Element outputListElement = outputListDocument.getDocumentElement();
        NodeList itemListNodes = outputListElement.getChildNodes();
        int length = itemListNodes.getLength();
        ArrayList<String> itemIds = new ArrayList<String>();
        for(int i=0;i<length;i++) {
            Node itemNode = itemListNodes.item(i);
            String tmpItemId = itemNode.getAttributes().getNamedItem("ItemID").getTextContent();
            itemIds.add(tmpItemId);
        }
        setItemXrefDoc(itemIds,LegacyCustomerNumber,customerDetails,env);
        setItemExtnDoc(itemIds,LegacyCustomerNumber,customerDetails,env);
        if(!complexQuery) {
            for (int i = 0; i < length; i++) {
                Node itemNode = itemListNodes.item(i);
                 
                /*******************Fix done for Bug#11905 by Prasanth Kumar M.********************/
                 
                baseUOM = itemNode.getAttributes().getNamedItem("UnitOfMeasure").getTextContent();
                 
                /***********************************************************************************/    
                 
                NodeList itemNodeChildren = itemNode.getChildNodes();
                int length1 = itemNodeChildren.getLength();
                for (int j = 0; j < length1; j++) {
                    Node itemNodeChild = itemNodeChildren.item(j);
                    if (itemNodeChild != null
                            && itemNodeChild.getNodeName().equals(
                                    "AlternateUOMList")) {
                        handleAternateUOMs(itemNodeChild, wUOMsToConversionFactors);
                    }
                }
                orderMultiple = getOrderMultipleValue(itemID, customerDetails);
            }
            handleXpxItemcustXrefList(itemID,LegacyCustomerNumber, customerDivision,
                    useOrderMulUOMFlag, orderMultiple,
                    wUOMsToConversionFactors,env,entryType);
            if (ExtnIsCustUOMExcl != null
                    && ExtnIsCustUOMExcl.equals("Y")) {
                return getOutputDocument(wUOMsToConversionFactors, "");
            }        
            env.clearApiTemplate("getItemList");
            Document document = getOutputDocument(wUOMsToConversionFactors, lowestConvUOM);
             
            log.endTimer("XPXUOMListAPI:getUOMList ended...");
            return document;
        }
        else {
            for (int i = 0; i < length; i++) {
                Node itemNode = itemListNodes.item(i);
                wUOMsToConversionFactors.clear();
                 
                // including the fix by Prashath in the Complex Query also
                itemID = itemNode.getAttributes().getNamedItem("ItemID").getTextContent();
                baseUOM = itemNode.getAttributes().getNamedItem("UnitOfMeasure").getTextContent();
                 
                NodeList itemNodeChildren = itemNode.getChildNodes();
                int length1 = itemNodeChildren.getLength();
                for (int j = 0; j < length1; j++) {
                    Node itemNodeChild = itemNodeChildren.item(j);
                    if (itemNodeChild != null
                            && itemNodeChild.getNodeName().equals(
                                    "AlternateUOMList")) {
                        handleAternateUOMs(itemNodeChild, wUOMsToConversionFactors);
                    }
                }
                orderMultiple = getOrderMultipleValue(itemID, customerDetails);
                handleXpxItemcustXrefList(itemID,LegacyCustomerNumber, customerDivision,
                        useOrderMulUOMFlag, orderMultiple,
                        wUOMsToConversionFactors,env,entryType);
                if (ExtnIsCustUOMExcl != null
                        && ExtnIsCustUOMExcl.equals("Y")) {
                    getComplexQueryOutputDocument(wUOMsToConversionFactors, "" ,complexQueryOutDoc, itemID);
                }
                getComplexQueryOutputDocument(wUOMsToConversionFactors, lowestConvUOM ,complexQueryOutDoc,itemID);
            }
            env.clearApiTemplate("getItemList");
            log.endTimer("XPXUOMListAPI:getUOMList ended...");
            return complexQueryOutDoc.getDocument();
        }
    }
 
    private void handleXpxItemcustXrefList(String itemID,
            String customerNumber, String customerBranch,
            String useOrderMulUOMFlag, String orderMultiple,
            HashMap<String, String> wUOMsToConversionFactors, YFSEnvironment env, String entryType)
            throws XPathExpressionException,YFSException, RemoteException, YIFClientCreationException {
         
        Node customerUnitNode = null;
        customerUOMList.clear();
        /*NodeList XpxItemcustXrefList = getItemCustomerXDetails(itemID,
                customerNumber, customerBranch, env);*/
        /*Begin - Changes made by Mitesh for JIRA# 3641*/
        //ArrayList<Element> XpxItemcustXrefList = SCXmlUtil.getElements(itemsXrefDoc.getDocumentElement(), "XPXItemcustXref[@LegacyItemNumber="+itemID+"]");
        List<Element> XpxItemcustXrefList = XPXUtils.getElements(itemsXrefDoc.getDocumentElement(), "XPXItemcustXref[@LegacyItemNumber="+itemID+"]");
        /*End - Changes made by Mitesh for JIRA# 3641*/
        int length3 = XpxItemcustXrefList.size();
        for (int m = 0; m < length3; m++) {
            Node XpxItemcustXref = XpxItemcustXrefList.get(m);
            NamedNodeMap XpxItemcustXrefAttributes = XpxItemcustXref
                    .getAttributes();
            Node ExtnIsCustUOMExclNode = XpxItemcustXrefAttributes
                    .getNamedItem("IsCustUOMExcl");
            if (ExtnIsCustUOMExclNode != null) {
                ExtnIsCustUOMExcl = ExtnIsCustUOMExclNode.getTextContent();
            }
             
             
            if(entryType == null || entryType.trim().length()<=0)
            {
               customerUnitNode = XpxItemcustXrefAttributes
                    .getNamedItem("CustomerUom");
            }
            else
            {
                //Its a B2B request
                customerUnitNode = XpxItemcustXrefAttributes
                .getNamedItem("LegacyUom");
            }
            /********Temporarily retained till new entries get added in CustomerXref table as per modified design***/
            if(customerUnitNode==null)
            {
                customerUnitNode = XpxItemcustXrefAttributes
                .getNamedItem("CustomerUnit");
            }
            /*******************************************************************************************************/
             
            String customerUnit = customerUnitNode.getTextContent();
            Node ConvFactorNode = XpxItemcustXrefAttributes
                    .getNamedItem("ConvFactor");
            String ConvFactor = ConvFactorNode.getTextContent();
            /*EB 1851 Start , CUstomer UOM conversion Factor = Alternate UOM Quantity * xpxitemcust_xref.conver factor where alternate uom =xpxitemcust_xref.legacy_uom by Amar*/
            String legacyConvFact=wUOMsToConversionFactors.get(
					XpxItemcustXrefAttributes.getNamedItem("LegacyUom") != null ? XpxItemcustXrefAttributes.getNamedItem("LegacyUom").getTextContent() : "");
			if(!(YFCCommon.isVoid(ConvFactor) && YFCCommon.isVoid(legacyConvFact) ))
				ConvFactor = ""+(Float.parseFloat(ConvFactor) * Float.parseFloat(legacyConvFact));
			/*EB 1851*/
			//XB-687 - Start
            if (ExtnIsCustUOMExcl != null && ExtnIsCustUOMExcl.equals("Y")) {
                wUOMsToConversionFactors.clear();
            }
            if(customerUnit!=null && !customerUnit.equalsIgnoreCase("")){
                customerUOMList.add(customerUnit);                
                wUOMsToConversionFactors.put(customerUnit, ConvFactor);
             
            }            
            //XB-687 - End    
            // Null check added.
            if (useOrderMulUOMFlag != null && useOrderMulUOMFlag.equals("Y")) {
                int conversion = getConversion(ConvFactor, orderMultiple);
                if (conversion != -1 && customerUnit != null
                        && customerUnit.length() > 0) {
                    if (currentConversion == 0
                            || (currentConversion != 0 && conversion < currentConversion)) {
                        lowestConvUOM = customerUnit;
                        currentConversion = conversion;
                    }
                }
            }
            wUOMsToConversionFactors.put(customerUnit, ConvFactor);
        }
    }
 
    /**
     * <XPXItemcustXref CapsId=" " CompanyCode=" " ConvFactor=" "
     * Createprogid=" " Createts=" " Createuserid=" " CustomerBranch=" "
     * CustomerDecription=" " CustomerNumber=" " CustomerPartNumber=" "
     * CustomerUnit=" " EnvironmentCode=" " IsCustUOMExcl=" " ItemcustRefKey=" "
     * LegacyBase=" " LegacyItemNumber=" " Lockid=" " MPC=" " Modifyprogid=" "
     * Modifyts=" " Modifyuserid=" "> <Extn ExtnBillToSuffix=" "
     * ExtnShipToSuffix=" " ExtnSuffixType=" "/> </XPXItemcustXref>     *  
     * @param itemID
     * @param customerNumber
     * @param customerBranch
     * @param env  
     * @return
     * @throws YIFClientCreationException  
     * @throws RemoteException  
     * @throws YFSException  
     */
    private NodeList getItemCustomerXDetails(String itemID,
            String customerNumber, String customerBranch, YFSEnvironment env)
            throws YIFClientCreationException, YFSException, RemoteException {
        YFCDocument inputDocument = YFCDocument.createDocument("XPXItemcustXref");
        YFCElement inputElement = inputDocument.getDocumentElement();
         
        inputElement.setAttribute("LegacyItemNumber", itemID);
        inputElement.setAttribute("CustomerDivision", customerBranch);
        inputElement.setAttribute("CustomerNumber", customerNumber);
         
        env.setApiTemplate("getXPXItemcustXrefList", SCXmlUtil
                .createFromString(""
                        + "<XPXItemcustXrefList><XPXItemcustXref/>"
                        + "</XPXItemcustXrefList>"));
        api = YIFClientFactory.getInstance().getApi();
        Document outputListDocument = api.executeFlow(env,"getXPXItemcustXrefList",inputDocument.getDocument());
        env.clearApiTemplate("getXPXItemcustXrefList");
        Element outputListElement = outputListDocument.getDocumentElement();
        if (outputListElement != null) {
            return outputListElement.getChildNodes();
        }
        return null;
    }
 
    private void handleAternateUOMs(Node itemNodeChild,
            HashMap<String, String> wUOMsToConversionFactors) {
        NodeList AternateUOMList = itemNodeChild.getChildNodes();
        int length2 = AternateUOMList.getLength();
        for (int k = 0; k < length2; k++) {
            Node AlternateUOM = AternateUOMList.item(k);
            NamedNodeMap namedNodeMap = AlternateUOM.getAttributes();
            Node IsOrderingUOMNode = namedNodeMap.getNamedItem("IsOrderingUOM");
            String IsOrderingUOM = IsOrderingUOMNode.getTextContent();
            if (IsOrderingUOM != null && IsOrderingUOM.equals("Y")) {
                Node unitOfMeasureNode = namedNodeMap
                        .getNamedItem("UnitOfMeasure");
                String unitOfMeasure = unitOfMeasureNode.getTextContent();
                Node quantityNode = namedNodeMap.getNamedItem("Quantity");
                String quantity = quantityNode.getTextContent();
                wUOMsToConversionFactors.put(unitOfMeasure, quantity);
            }
        }
    }
    private Document getOutputDocument(HashMap<String, String> wUOMsToConversionFactors,
            String lowestConvUOM) {
         
        YFCDocument inputDocument = YFCDocument.createDocument("UOMList");
        YFCElement documentElement = inputDocument.getDocumentElement();
         
        /**************Added as part of fix for bug #11905 by Prasanth Kumar M.***********************/
        //when there is no customer specific UOM
        if (baseUOM != null
                && baseUOM.trim().length() > 0
                && (ExtnIsCustUOMExcl == null
                        || ExtnIsCustUOMExcl.trim().length() == 0 || !ExtnIsCustUOMExcl
                        .equals("Y")))
        {
            YFCElement uOMElement = documentElement.createChild("UOM");
            uOMElement.setAttribute("UnitOfMeasure", baseUOM);
            uOMElement.setAttribute("Conversion", "1");
        }
        /*********************************************************************************************/
         
        if (lowestConvUOM != null && lowestConvUOM.length() > 0) {
            YFCElement uOMElement = documentElement.createChild("UOM");
            uOMElement.setAttribute("UnitOfMeasure", lowestConvUOM);
            uOMElement.setAttribute("Conversion", wUOMsToConversionFactors
                    .get(lowestConvUOM));
             
        }
        Set<String> set = wUOMsToConversionFactors.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            YFCElement uOMElement = documentElement.createChild("UOM");
            String UnitOfMeasure = (String) iterator.next();
            String Conversion = wUOMsToConversionFactors.get(UnitOfMeasure);
            if (!UnitOfMeasure.equals(lowestConvUOM)) {
                uOMElement.setAttribute("UnitOfMeasure", UnitOfMeasure);
                uOMElement.setAttribute("Conversion", Conversion);
                //Start of XB-687
                if(customerUOMList.contains(UnitOfMeasure))
                {
                    uOMElement.setAttribute("IsCustUOMFlag", "Y");
                }//End of XB-687
            }
        }
        return inputDocument.getDocument();
    }
     
    private void getComplexQueryOutputDocument(HashMap<String, String> wUOMsToConversionFactors,
            String lowestConvUOM, YFCDocument complexQueryOutDoc, String itemID) {
         
        YFCElement documentElement = complexQueryOutDoc.getDocumentElement();
        YFCElement itemElement = complexQueryOutDoc.createElement("Item");
        YFCElement UOMListElement = itemElement.createChild("UOMList");
         
        /**************Added as part of fix for bug #11905 by Prasanth Kumar M.***********************/
        //when there is no customer specific UOM
        if (baseUOM != null
                && baseUOM.trim().length() > 0
                && (ExtnIsCustUOMExcl == null
                        || ExtnIsCustUOMExcl.trim().length() == 0 || !ExtnIsCustUOMExcl
                        .equals("Y")))
        {
            YFCElement uOMElement = UOMListElement.createChild("UOM");
            uOMElement.setAttribute("UnitOfMeasure", baseUOM);
            uOMElement.setAttribute("Conversion", "1");
        }
        /*********************************************************************************************/
         
        if (lowestConvUOM != null && lowestConvUOM.length() > 0) {
            YFCElement uOMElement = UOMListElement.createChild("UOM");
            uOMElement.setAttribute("UnitOfMeasure", lowestConvUOM);
            uOMElement.setAttribute("Conversion", wUOMsToConversionFactors
                    .get(lowestConvUOM));
             
        }
        Set<String> set = wUOMsToConversionFactors.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            YFCElement uOMElement = UOMListElement.createChild("UOM");
            String UnitOfMeasure = (String) iterator.next();
            String Conversion = wUOMsToConversionFactors.get(UnitOfMeasure);
            if (!UnitOfMeasure.equals(lowestConvUOM)) {
                uOMElement.setAttribute("UnitOfMeasure", UnitOfMeasure);
                //Updated for XB-687 - Start
                if(customerUOMList.contains(UnitOfMeasure))
                {
                    uOMElement.setAttribute("IsCustUOMFlag", "Y");
                }//Updated for XB-687 - End
                uOMElement.setAttribute("Conversion", Conversion);
            }
        }
        itemElement.setAttribute("ItemID", itemID);
        documentElement.appendChild((YFCNode)itemElement);
    }
 
    private int getConversion(String convFactor, String orderMultiple) {
        if (convFactor != null && convFactor.length() > 0
                && orderMultiple != null && orderMultiple.length() > 0) {
            double convFactorD = Double.parseDouble(convFactor);
            double orderMultipleD = Double.parseDouble(orderMultiple);
            double factor = (convFactorD / orderMultipleD);
            if (Math.abs(factor) == factor) {
                return (int) Math.abs(factor);
            }
        }
        return -1;
    }
 
    private String getOrderMultipleValue(String itemId, HashMap<String, String> custDetails) {
        String orderMultiple = "";
        String companyCode = custDetails.get("companyCode");
        //String customerDiv = custDetails.get("customerDivision");
        String shipFromBranch = custDetails.get("shipFromBranch");
        //String envCode = custDetails.get("enviromentCode");
        ArrayList<Element> XpxItemExtnList = SCXmlUtil.getChildren(itemExtnDoc.getDocumentElement(), "XPXItemExtn[@ItemID="+itemId+"]");
        int length3 = XpxItemExtnList.size();
        for (int m = 0; m < length3; m++) {
            Element XpxItemExtn = XpxItemExtnList.get(m);
            NamedNodeMap XpxItemExtnAttributes = XpxItemExtn.getAttributes();
            Node companyCodeNode = XpxItemExtnAttributes
                    .getNamedItem("CompanyCode");
            String companyCodeL = companyCodeNode.getTextContent();
            Node XPXDivisionNode = XpxItemExtnAttributes
                    .getNamedItem("XPXDivision");
            String XPXDivision = XPXDivisionNode.getTextContent();
            if (companyCodeL.equals(companyCode)
                    && XPXDivision.equals(shipFromBranch)) {
                Node orderMultipleNode = XpxItemExtnAttributes
                        .getNamedItem("OrderMultiple");
                orderMultiple = orderMultipleNode.getTextContent();
            }
        }
        return orderMultiple;
    }
 
    private HashMap<String, String> getCustomerDetails(YFSEnvironment env,
            Document inXML) throws YIFClientCreationException, YFSException,
            RemoteException {
        HashMap<String, String> customerDetails = new HashMap<String, String>();
        Element documentElement = inXML.getDocumentElement();
        String customerID = documentElement.getAttribute("CustomerID");
        String storeFrontId = documentElement.getAttribute("OrganizationCode");
 
        YFCDocument inputDocument = YFCDocument.createDocument("Customer");
        YFCElement inputElement = inputDocument.getDocumentElement();
        inputElement.setAttribute("CustomerID", customerID);
        inputElement.setAttribute("OrganizationCode", storeFrontId);
 
        env.setApiTemplate("getCustomerList", SCXmlUtil.createFromString(""
                + "<CustomerList><Customer><Extn>"
                + "</Extn></Customer></CustomerList>"));
 
        api = YIFClientFactory.getInstance().getApi();
 
        Document outputListDocument = api.invoke(env, "getCustomerList",
                inputDocument.getDocument());
        Element outputListElement = outputListDocument.getDocumentElement();
        NodeList wNodeList = outputListElement.getChildNodes();
        int length = wNodeList.getLength();
        for (int i = 0; i < length; i++) {
            Node customerNode = wNodeList.item(i);
            NodeList customerChildNodes = customerNode.getChildNodes();
            int length1 = customerChildNodes.getLength();
            for (int j = 0; j < length1; j++) {
                Node customerChildNode = customerChildNodes.item(j);
                String companyCode = "";
                String envCode = "";
                String customerDivision = "";
                String useOrderMulUOMFlag = "";
                String shipFromBranch = "";
                if (customerChildNode.getNodeName().equals("Extn")) {
                    NamedNodeMap extnAttributes = customerChildNode
                            .getAttributes();
                    Node ExtnCompanyCodeNode = extnAttributes
                            .getNamedItem("ExtnCompanyCode");
                    if (ExtnCompanyCodeNode != null) {
                        companyCode = ExtnCompanyCodeNode.getTextContent();
                        if (companyCode != null
                                && companyCode.trim().length() > 0) {
                            customerDetails.put("companyCode", companyCode);
                        }
                    }
                    Node ExtnEnvironmentCode = extnAttributes
                            .getNamedItem("ExtnEnvironmentCode");
                    if (ExtnEnvironmentCode != null) {
                        envCode = ExtnEnvironmentCode.getTextContent();
                        if (envCode != null
                                && envCode.trim().length() > 0) {
                            customerDetails.put("enviromentCode", envCode);
                        }
                    }
                    Node ExtnShipFromBranch = extnAttributes
                        .getNamedItem("ExtnShipFromBranch");
                    if (ExtnShipFromBranch != null) {
                        shipFromBranch = ExtnShipFromBranch.getTextContent();
                        if (shipFromBranch != null
                                && shipFromBranch.trim().length() > 0) {
                            customerDetails.put("shipFromBranch", shipFromBranch);
                        }
                    }
                    Node ExtnCustomerDivisionNode = extnAttributes
                            .getNamedItem("ExtnCustomerDivision");
                    if (ExtnCustomerDivisionNode != null) {
                        customerDivision = ExtnCustomerDivisionNode
                                .getTextContent();
                        if (customerDivision != null
                                && customerDivision.trim().length() > 0) {
                            customerDetails.put("customerDivision",
                                    customerDivision);
                        }
                    }
                    Node ExtnUseOrderMulUOMFlagNode = extnAttributes
                            .getNamedItem("ExtnUseOrderMulUOMFlag");
                    if (ExtnUseOrderMulUOMFlagNode != null) {
                        useOrderMulUOMFlag = ExtnUseOrderMulUOMFlagNode
                                .getTextContent();
                        if (useOrderMulUOMFlag != null
                                && useOrderMulUOMFlag.trim().length() > 0) {
                            customerDetails.put("useOrderMulUOMFlag",
                                    useOrderMulUOMFlag);
                        }
                    }
                }
            }
        }            
        env.clearApiTemplate("getCustomerList");
        return customerDetails;
    }
     
    private void setItemXrefDoc(ArrayList<String> itemIds,String LegacyCustomerNumber,HashMap<String, String> custDetails, YFSEnvironment env)  
                throws YFSException, RemoteException, YIFClientCreationException {
        String custDivision = custDetails.get("customerDivision");
        String envCode = custDetails.get("enviromentCode");
        YFCDocument inputDocument = YFCDocument.createDocument("XPXItemcustXref");
        YFCElement inputElement = inputDocument.getDocumentElement();
         
        //inputElement.setAttribute("LegacyItemNumber", itemID);
        inputElement.setAttribute("CustomerDivision", custDivision);
        inputElement.setAttribute("CustomerNumber", LegacyCustomerNumber);
        inputElement.setAttribute("EnvironmentCode", envCode);
         
        YFCElement complexQueryElement = inputElement.createChild("ComplexQuery");
        YFCElement complexQueryOrElement = inputElement.createChild("Or");
        for (int i = 0; i < itemIds.size(); i++) {
            YFCElement expElement = inputElement.createChild("Exp");
            expElement.setAttribute("Name", "LegacyItemNumber");
            expElement.setAttribute("Value", itemIds.get(i));
            expElement.setAttribute("QryType", "EQ");
            complexQueryOrElement.appendChild((YFCNode) expElement);
        }
        complexQueryElement.setAttribute("Operator", "AND");
        complexQueryElement.appendChild(complexQueryOrElement);
         
        env.setApiTemplate("getItemCustXrefListService", SCXmlUtil
                .createFromString(""
                        + "<XPXItemcustXrefList><XPXItemcustXref/>"
                        + "</XPXItemcustXrefList>"));
        api = YIFClientFactory.getInstance().getApi();
        itemsXrefDoc = api.executeFlow(env,"getItemCustXrefListService",inputDocument.getDocument());
        env.clearApiTemplate("getItemCustXrefListService");
         
    }
     
    private void setItemExtnDoc(ArrayList<String> itemIds,String LegacyCustomerNumber,HashMap<String, String> custDetails, YFSEnvironment env)  
        throws YFSException, RemoteException, YIFClientCreationException {
        //String custDivision = custDetails.get("customerDivision");
        String shipFromBranch = custDetails.get("shipFromBranch");
        String envCode = custDetails.get("enviromentCode");
        YFCDocument inputDocument = YFCDocument.createDocument("XPXItemExtn");
        YFCElement inputElement = inputDocument.getDocumentElement();
         
        //inputElement.setAttribute("LegacyItemNumber", itemID);
        inputElement.setAttribute("XPXDivision", shipFromBranch);
        //inputElement.setAttribute("CustomerNumber", LegacyCustomerNumber);
        inputElement.setAttribute("EnvironmentID", envCode);
         
        YFCElement complexQueryElement = inputElement.createChild("ComplexQuery");
        YFCElement complexQueryOrElement = inputElement.createChild("Or");
        for (int i = 0; i < itemIds.size(); i++) {
            YFCElement expElement = inputElement.createChild("Exp");
            expElement.setAttribute("Name", "ItemID");
            expElement.setAttribute("Value", itemIds.get(i));
            expElement.setAttribute("QryType", "EQ");
            complexQueryOrElement.appendChild((YFCNode) expElement);
        }
        complexQueryElement.setAttribute("Operator", "AND");
        complexQueryElement.appendChild(complexQueryOrElement);
         
        env.setApiTemplate("getXPXItemExtnList", SCXmlUtil
                .createFromString(""
                        + "<XPXItemExtnList><XPXItemExtn/>"
                        + "</XPXItemExtnList>"));
        api = YIFClientFactory.getInstance().getApi();
        itemExtnDoc = api.executeFlow(env,"getXPXItemExtnList",inputDocument.getDocument());
        env.clearApiTemplate("getXPXItemExtnList");
    }
     
    /*public static void main(String [] args) {
        try{
        Document document=MCFDOMDocFromXMLString.createDomDocFromXMLString("C:\\xpedx\\NextGen\\src\\WebChannel\\main\\resources\\NewFile2.xml");
        //Element ele = SCXmlUtil.get
        java.util.List<Element> XpxItemcustXrefList =  XPXUtils.getElements(document.getDocumentElement(), "XPXItemcustXref[@LegacyItemNumber=2001015]");
         
        }catch(Exception e){
            e.printStackTrace();
        }
    }*/
}