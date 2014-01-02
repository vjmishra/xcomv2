package com.xpedx.sterling.rcp.pca.csrmanagement.screen;


import java.util.Comparator;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.xml.xpath.XPathConstants;

import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCDesktopUI;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXPathUtils;
import com.yantra.yfc.rcp.YRCXmlUtils;


public class XPXCSRMaintenancePanelBehavior extends YRCBehavior {
	
	private XPXCSRMaintenancePanel page ;
	private String defaultOrgCode ;
	private String INTERNAL="INTERNAL";
	private boolean isSecondAPICallRequired=false;
	private Element searchResultsCSR1Element = null;

	public XPXCSRMaintenancePanelBehavior(Composite ownerComposite, String formId, Object inputObject) {
		super(ownerComposite, formId,inputObject);
		this.page = (XPXCSRMaintenancePanel)getOwnerForm();
		init();
		
	}

	public void init() {
		
		loadIntialDataAndSetModel();
		
	}

	/**
	 * Initially reset the Search Criteria to blank
	 */
	public void loadIntialDataAndSetModel() {
		YRCDesktopUI.getCurrentPart().showBusy(true);
		this.defaultOrgCode = "";
		this.setOrganizationList(XPXUtils.orgList);
		initialCSRLoad(); 
		YRCDesktopUI.getCurrentPart().showBusy(false);
		
	}

   
	public void search() {
		searchResultsCSR1Element = null;
		isSecondAPICallRequired=false;
		Element eleInput = getTargetModel("SearchCriteria");
		String enterpriseKey = YRCXmlUtils.getAttribute(eleInput,"OrganizationCode");
		if(YRCPlatformUI.isVoid(enterpriseKey))
		{
			YRCPlatformUI.showError("Search Criteria: Mandatory Parameter", "Please select Enterprise");
			return ;
		}

		String oldCsr = YRCXmlUtils.getAttribute(eleInput,"ExtnECSR1Key");
		if(YRCPlatformUI.isVoid(oldCsr))
		{
			YRCPlatformUI.showError("Search Criteria: Mandatory Parameter", "Please select Old CSR");
			return ;
		}
		YRCDesktopUI.getCurrentPart().showBusy(true);
		prepareInputAndCallAPIToSearch(isSecondAPICallRequired);
		YRCDesktopUI.getCurrentPart().showBusy(false);


	}   
    
    private void prepareInputAndCallAPIToSearch(boolean isSecondAPICallRequired) {
    	Element eleInput = getTargetModel("SearchCriteria");
    	String enterpriseKey = YRCXmlUtils.getAttribute(eleInput,"OrganizationCode");
    	String csr = YRCXmlUtils.getAttribute(eleInput,"ExtnECSR1Key");
    	Document inputXml;
    	if(isSecondAPICallRequired){
    		inputXml=YRCXmlUtils.createFromString("<Customer OrganizationCode='"+ enterpriseKey +"'><Extn ExtnECSR2Key='"+ csr +"'/></Customer>") ;
    	}
    	else{
    		inputXml=YRCXmlUtils.createFromString("<Customer OrganizationCode='"+ enterpriseKey +"'><Extn ExtnECSR1Key='"+ csr +"'/></Customer>") ;
    	}

    	YRCApiContext context = new YRCApiContext();
    	context.setApiName("getCustomerList");
    	context.setFormId(getFormId());
    	context.setInputXml(inputXml);	
    	callApi(context);

    }
    
    public void update() {
    	Element eleSearchInput = getTargetModel("SearchCriteria");
    	Element eleInput = getTargetModel("update");

    	String oldCsr = YRCXmlUtils.getAttribute(eleSearchInput,"ExtnECSR1Key");
    	String newCsr = YRCXmlUtils.getAttribute(eleInput,"ExtnECSR2Key");
    	String csrOptionForUpdate = YRCXmlUtils.getAttribute(eleInput,"CSROption");
    	if(YRCPlatformUI.isVoid(newCsr))
    	{
    		YRCPlatformUI.showError("For update: Mandatory Parameter", "Please select New CSR");
    		return ;
    	}
    	if(!YRCPlatformUI.isVoid(oldCsr) && !YRCPlatformUI.isVoid(newCsr) && oldCsr.equalsIgnoreCase(newCsr))
    	{
    		YRCPlatformUI.showError("For update:"," New CSR not equal to old CSR");	
    		return ;		
    	}
    	//YRCDesktopUI.getCurrentPart().showBusy(true);
    	Element eleReplaceMyItems = getTargetModel("replace_input");
    	NodeList customerList = (NodeList) YRCXPathUtils.evaluate(eleReplaceMyItems, "/CustomerList/Customer[@Checked='Y']", XPathConstants.NODESET);
    	Element eleGetCSRList = getModel("XPXGetUserList");
    	String oldCSRXPath="/UserList/User[@UserKey='"+oldCsr+"']";
    	String newCSRXPath="/UserList/User[@UserKey='"+newCsr+"']";
		NodeList oldCSRUsrList = (NodeList) YRCXPathUtils.evaluate(eleGetCSRList, oldCSRXPath, XPathConstants.NODESET);
		NodeList newCSRUsrList = (NodeList) YRCXPathUtils.evaluate(eleGetCSRList, newCSRXPath, XPathConstants.NODESET);
		Element  eleOldCSR = (Element)oldCSRUsrList.item(0);
		Element  eleNewCSR = (Element)newCSRUsrList.item(0);
		boolean isCustomerUpdateRequired = false;
		Document docCustomerList = YRCXmlUtils.createDocument("CustomerList");
		for(int i=0;i<customerList.getLength();i++){
			isCustomerUpdateRequired = false;
			Element eleCustomer = (Element)customerList.item(i);
			NodeList extnNodeList =  eleCustomer.getElementsByTagName("Extn");
			Element  eleExtn = (Element)extnNodeList.item(0);
			
			Element eleCustomerForUpdate = YRCXmlUtils.createDocument("Customer").getDocumentElement();
			eleCustomerForUpdate.setAttribute("CustomerKey", eleCustomer.getAttribute("CustomerKey"));
			eleCustomerForUpdate.setAttribute("Operation", "Update");
			String userKey = eleNewCSR.getAttribute("UserKey");
			String loginid = eleNewCSR.getAttribute("Loginid");
			String extnCSREmailId = ((Element)eleNewCSR.getElementsByTagName("ContactPersonInfo").item(0)).getAttribute("EMailID");
	
			Element eleExtnorUpdate =  YRCXmlUtils.createChild(eleCustomerForUpdate, "Extn");
			if("CSR1".equalsIgnoreCase(csrOptionForUpdate) && oldCsr.equals(eleExtn.getAttribute("ExtnECSR1Key"))){		
					eleExtnorUpdate.setAttribute("ExtnECSR1Key", userKey);
					eleExtnorUpdate.setAttribute("ExtnECSR", loginid);
					eleExtnorUpdate.setAttribute("ExtnECsr1EMailID", extnCSREmailId);
					isCustomerUpdateRequired =true;
			}
			if("CSR2".equalsIgnoreCase(csrOptionForUpdate) && oldCsr.equals(eleExtn.getAttribute("ExtnECSR2Key"))){	
				eleExtnorUpdate.setAttribute("ExtnECSR2Key", userKey);
				eleExtnorUpdate.setAttribute("ExtnECSR2", loginid);
				eleExtnorUpdate.setAttribute("ExtnECsr2EMailID", extnCSREmailId);
				
				isCustomerUpdateRequired =true;
			}
			if("Both".equalsIgnoreCase(csrOptionForUpdate)){	
				if(oldCsr.equals(eleExtn.getAttribute("ExtnECSR1Key"))){		
					eleExtnorUpdate.setAttribute("ExtnECSR1Key", userKey);
					eleExtnorUpdate.setAttribute("ExtnECSR", loginid);
					eleExtnorUpdate.setAttribute("ExtnECsr1EMailID", extnCSREmailId);
					isCustomerUpdateRequired =true;
			    }
				if(oldCsr.equals(eleExtn.getAttribute("ExtnECSR2Key"))){	
					eleExtnorUpdate.setAttribute("ExtnECSR2Key", userKey);
					eleExtnorUpdate.setAttribute("ExtnECSR2", loginid);
					eleExtnorUpdate.setAttribute("ExtnECsr2EMailID", extnCSREmailId);					
					isCustomerUpdateRequired =true;
				}
			}
			if(isCustomerUpdateRequired){
				YRCXmlUtils.importElement(docCustomerList.getDocumentElement(), eleCustomerForUpdate);
			}
			
		}
		//YRCDesktopUI.getCurrentPart().showBusy(false);
		String[] apinames = {"XPXManageCustomersAPIService"};
		Document[] docInput = {	docCustomerList};
		callApis(apinames, docInput);
		
    }   
    /**
     * Reset the Search Criteria.
     */
    public void reset() {
    	loadIntialDataAndSetModel();
    }

    private void setOrganizationList(Element eOrgList)
    {
    	setModel("OrgList", eOrgList);
    	NodeList nl = eOrgList.getElementsByTagName("Organization");
    	if(null != nl && nl.getLength()>0)
    		defaultOrgCode = YRCPlatformUI.getUserElement().getAttribute("EnterpriseCode");        

    }
	private void initialCSRLoad() {
		String[] apinames = {"XPXGetUserList"};
		Document[] docInput = {	createCSRUSROutputXml().getOwnerDocument()};
		callApis(apinames, docInput);
		createCSROptions();

	}
	private void callApis(String apinames[], Document inputXmls[]) {		
		YRCApiContext ctx = new YRCApiContext();
		ctx.setFormId(page.getFormId());
		ctx.setApiNames(apinames);
		ctx.setInputXmls(inputXmls);
		
		if (!page.isDisposed())
			callApi(ctx, page);
	}
	private Element createCSRUSROutputXml(){
		Document docInput =	YRCXmlUtils.createFromString("<Extn ExtnUserType='" + INTERNAL + "' />");
		Element inputXML = docInput.getDocumentElement();
		Element targetDoc = YRCXmlUtils.createFromString("<User />").getDocumentElement();
		YRCXmlUtils.importElement(targetDoc, inputXML);		
		return targetDoc;
	}
	@Override
	public void handleApiCompletion(YRCApiContext ctx) {
		
		if (ctx.getInvokeAPIStatus() > 0){
			if (page.isDisposed()) {
				YRCPlatformUI.trace("Page is Disposed");
			} else {
				String[] apinames = ctx.getApiNames();
				for (int i = 0; i < apinames.length; i++) {
					String apiname = apinames[i];								
					if ("XPXGetUserList".equals(apiname)) {
						YRCDesktopUI.getCurrentPart().showBusy(true);
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						//setModel("XPXGetUserList",outXml);
						updateXPXGetUserList(outXml);
						YRCDesktopUI.getCurrentPart().showBusy(false);
					}
					if ("getCustomerList".equals(apiname)) {
						YRCDesktopUI.getCurrentPart().showBusy(true);
						if(isSecondAPICallRequired){
							Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
							Element customerListForCSR1AndCSR2 =searchResultsCSR1Element;
							NodeList nodeList	=	outXml.getOwnerDocument().getElementsByTagName("Customer");
					
							for(int j=0;j<nodeList.getLength();j++){	
								Element  eleCustomer = (Element)nodeList.item(j);
								NodeList extnNodeList =  eleCustomer.getElementsByTagName("Extn");
								Element  eleExtn = (Element)extnNodeList.item(0);
								if(eleExtn.getAttribute("ExtnECSR1Key")!=null){
									String[] csr1Info =	getEcsrLoginId(eleExtn.getAttribute("ExtnECSR1Key"));
									if(csr1Info!=null){
										eleExtn.setAttribute("ExtnECSR", csr1Info.length>0?csr1Info[0]:"");
										eleExtn.setAttribute("ExtnECSR1Name", csr1Info.length>2?csr1Info[2]+","+csr1Info[1]:"" );
									}
									
								}
								if(eleExtn.getAttribute("ExtnECSR2Key")!=null){
									String[] csr2Info =	getEcsrLoginId(eleExtn.getAttribute("ExtnECSR2Key"));
									if(csr2Info!=null){
										eleExtn.setAttribute("ExtnECSR2", csr2Info.length>0?csr2Info[0]:"");
										eleExtn.setAttribute("ExtnECSR2Name", csr2Info.length>2?csr2Info[2]+","+csr2Info[1]:"" );
									}
									
								}
								YRCXmlUtils.importElement(customerListForCSR1AndCSR2, eleCustomer);
							}
							setModel("CustomerList",customerListForCSR1AndCSR2);
							isSecondAPICallRequired = false;
							searchResultsCSR1Element = null;
						}else{
							Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
							Document docCustomerList = YRCXmlUtils.createDocument("CustomerList");
							NodeList nodeList	=	outXml.getOwnerDocument().getElementsByTagName("Customer");
							
							for(int j=0;j<nodeList.getLength();j++){	
								Element  eleCustomer = (Element)nodeList.item(j);
								NodeList extnNodeList =  eleCustomer.getElementsByTagName("Extn");
								Element  eleExtn = (Element)extnNodeList.item(0);
								if(eleExtn.getAttribute("ExtnECSR1Key")!=null){
									String[] csr1Info =	getEcsrLoginId(eleExtn.getAttribute("ExtnECSR1Key"));
									if(csr1Info!=null){
										eleExtn.setAttribute("ExtnECSR", csr1Info.length>0?csr1Info[0]:"");
										eleExtn.setAttribute("ExtnECSR1Name", csr1Info.length>2?csr1Info[2]+","+csr1Info[1]:"" );
									}
								
								}
								if(eleExtn.getAttribute("ExtnECSR2Key")!=null){
									String[] csr2Info =	getEcsrLoginId(eleExtn.getAttribute("ExtnECSR2Key"));
									if(csr2Info!=null){
										eleExtn.setAttribute("ExtnECSR2", csr2Info.length>0?csr2Info[0]:"");
										eleExtn.setAttribute("ExtnECSR2Name", csr2Info.length>2?csr2Info[2]+","+csr2Info[1]:"" );
									}
								
								}
								YRCXmlUtils.importElement(docCustomerList.getDocumentElement(), eleCustomer);
							}
							searchResultsCSR1Element = docCustomerList.getDocumentElement();
							isSecondAPICallRequired=true;
							prepareInputAndCallAPIToSearch(isSecondAPICallRequired);
						}
						YRCDesktopUI.getCurrentPart().showBusy(false);
					}
					if ("XPXManageCustomersAPIService".equals(apiname)) {					
						//Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						this.page.getMyBehavior().search();	
					
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
	
	public void createCSROptions(){


		Element elemModel = YRCXmlUtils.createDocument("CSROptions").getDocumentElement();

		Element attrElemComplex1 = YRCXmlUtils.createChild(elemModel, "Code");
		attrElemComplex1.setAttribute("CodeValue", "CSR1");
		attrElemComplex1.setAttribute("CodeShortDescription", "CSR1");

		Element attrElemComplex2 = YRCXmlUtils.createChild(elemModel, "Code");;

		attrElemComplex2.setAttribute("CodeValue", "CSR2");
		attrElemComplex2.setAttribute("CodeShortDescription", "CSR2");

		Element attrElemComplex3 = YRCXmlUtils.createChild(elemModel, "Code");

		attrElemComplex3.setAttribute("CodeValue", "Both");
		attrElemComplex3.setAttribute("CodeShortDescription", "Both (CSR1 & CSR2)");

		setModel("CSROptions",elemModel);

	}
	private String[] getEcsrLoginId(String usrKey){
		Element eleGetCSRList = getModel("XPXGetUserList");
		String csrXPath="/UserList/User[@UserKey='"+usrKey+"']";
		NodeList csrUsrList = (NodeList) YRCXPathUtils.evaluate(eleGetCSRList, csrXPath, XPathConstants.NODESET);
		String[] usrInfo = new String[3];
		if(csrUsrList!=null && csrUsrList.getLength()>0){
		Element  eleOldCSR = (Element)csrUsrList.item(0);
		usrInfo[0]=eleOldCSR.getAttribute("Loginid");
		NodeList contactPersonInfoNodeList =  eleOldCSR.getElementsByTagName("ContactPersonInfo");
		Element  eleContactPersonInfo = (Element)contactPersonInfoNodeList.item(0);
		usrInfo[1]=eleContactPersonInfo.getAttribute("FirstName");
		usrInfo[2]=eleContactPersonInfo.getAttribute("LastName");		
		return usrInfo;
		}
	   return null;
	}
	
	private void updateXPXGetUserList(Element outXml){
		Element elemUsrList = YRCXmlUtils.createDocument("UserList").getDocumentElement();
		Document usrDocument= outXml.getOwnerDocument();
		TreeMap<String,String> usrTreeMap = new TreeMap<String,String>(new Comparator<String>(){
			public int compare(String str1, String str2) {
		        return str1.toLowerCase().compareTo(str2.toLowerCase());
		    }
		});
		NodeList nodeList	=	usrDocument.getElementsByTagName("User");
		for(int j=0;j<nodeList.getLength();j++){	
			Element  eleUser = (Element)nodeList.item(j);
			String loginId=eleUser.getAttribute("Loginid");
			NodeList contactPersonInfoNodeList =  eleUser.getElementsByTagName("ContactPersonInfo");
			Element  eleContactPersonInfo = (Element)contactPersonInfoNodeList.item(0);
			String usrName = eleContactPersonInfo.getAttribute("LastName")+","+eleContactPersonInfo.getAttribute("FirstName");
			String 	usrNameWithLoginId = usrName+" ("+loginId+")";
			eleContactPersonInfo.setAttribute("userName", usrName);
			eleUser.setAttribute("userNameAndLoginId", usrNameWithLoginId);	
			usrTreeMap.put(usrNameWithLoginId, eleUser.getAttribute("UserKey"));
	   }
		for(Entry<String,String> entry:usrTreeMap.entrySet()){
			Element eleUser = YRCXmlUtils.createChild(elemUsrList, "User");
			eleUser.setAttribute("UserKey", entry.getValue());
			eleUser.setAttribute("userNameAndLoginId", entry.getKey());
		}	
		setModel("XPXGetUserList",usrDocument.getDocumentElement());
		setModel("XPXGetUserSortedList",elemUsrList);
	}
	
	public void selectOrUnselect(String  allCheckOrUncheck){
		Element eleCustomers = getModel("CustomerList");
    	NodeList customerList = eleCustomers.getElementsByTagName("Customer");
    	for(int j=0;j<customerList.getLength();j++){	
    		Element  eleCustomer = (Element)customerList.item(j);
    		eleCustomer.setAttribute("Checked", allCheckOrUncheck);
    	}
    	setModel("CustomerList",eleCustomers);
	}
}
