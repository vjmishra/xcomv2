package com.xpedx.sterling.rcp.pca.customerprofilerule.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathConstants;

import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXPathUtils;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class CustomerProfileMaintenanceBehavior extends YRCBehavior {
	private String INTERNAL="INTERNAL";
	private Element inputElement;
	private CustomerProfileMaintenance page;
	private Element customerDetails;
	private List<String> existingEmailList;
	private Map <String, String> emailMap;
	public CustomerProfileMaintenanceBehavior(
			CustomerProfileMaintenance customerProfileMaintenance,
			Object inputObject) {
		
		super((Composite) customerProfileMaintenance, customerProfileMaintenance.getFormId(), inputObject);
		this.page = customerProfileMaintenance;
		this.inputElement=((YRCEditorInput) inputObject).getXml();
		initPage();
		// TODO Auto-generated constructor stub
	}
	@Override
	public void init() {
//		System.out.println("init().....");
	}

	public void initPage() {
		String[] apinames = {"XPXGetUserList","XPXGetCustomerListService","XPXGetParentCustomerListService"};
		Document[] docInput = {
				createManageCustomerOutputXml().getOwnerDocument(),
				YRCXmlUtils.createFromString("<Customer  CustomerKey='" + YRCXmlUtils.getAttribute(this.inputElement, "CustomerKey") + "' />"),
				YRCXmlUtils.createFromString("<Customer CustomerID='"+YRCXmlUtils.getAttribute(this.inputElement, "CustomerID")+"' OrganizationCode='"+YRCXmlUtils.getAttribute(this.inputElement, "OrganizationCode")+"'/>")
		};
		callApis(apinames, docInput);
		//callApi("XPXGetCustomerListService", YRCXmlUtils.createFromString("<Customer  CustomerKey='" + YRCXmlUtils.getAttribute(this.inputElement, "CustomerKey") + "' />"));
	}
	private void callApis(String apinames[], Document inputXmls[]) {
		YRCApiContext ctx = new YRCApiContext();
		ctx.setFormId(page.getFormId());
		ctx.setApiNames(apinames);
		ctx.setInputXmls(inputXmls);
		if (!page.isDisposed())
			callApi(ctx, page);
	}
	// Added for JIRA 4267
	private Element createManageCustomerOutputXml(){
		Document docInput =
			YRCXmlUtils.createFromString("<Extn ExtnUserType='" + INTERNAL + "' />");
		Element inputXML = docInput.getDocumentElement();
		Element targetDoc = YRCXmlUtils.createFromString("<User />").getDocumentElement();
		/*Element targetUserList = YRCXmlUtils.createChild(targetDoc, "User");*/
		YRCXmlUtils.importElement(targetDoc, inputXML);
		return targetDoc;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yantra.yfc.rcp.YRCBaseBehavior#handleApiCompletion(com.yantra.yfc.rcp.YRCApiContext)
	 */
	@Override
	public void handleApiCompletion(YRCApiContext ctx) {
		if (ctx.getInvokeAPIStatus() > 0){
			if (page.isDisposed()) {
				YRCPlatformUI.trace("Page is Disposed");
			} else {
				String[] apinames = ctx.getApiNames();
				for (int i = 0; i < apinames.length; i++) {
					String apiname = apinames[i];
					if ("XPXGetCustomerListService".equals(apiname)) {
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						setModel("XPXCustomerIn",outXml);
						prepareSalesRepModel(outXml);
						String suffixType = YRCXmlUtils.getAttribute(YRCXmlUtils.getXPathElement(outXml, "/CustomerList/Customer/Extn"), "ExtnSuffixType");
						if ("B".equals(suffixType)) {
							Element emailListEle=createModelForAdditionalEmails(outXml);
							setModel("XPXBillToLevelEmailList", emailListEle);
						}
						
					}
					if ("XPXGetParentCustomerListService".equals(apiname)) {
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						updateModelWithParentInfo(outXml);
					}						
					if ("XPXGetUserList".equals(apiname)) {
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						setModel("XPXGetUserList",outXml);
					}
				}
				/*if ("XPXPnAService".equals(ctx.getApiName())) {
					
				}*/
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
	/*public void handleApiCompletion(YRCApiContext ctx) {
		if (ctx.getInvokeAPIStatus() > 0)
			if (page.isDisposed()) {
				YRCPlatformUI.trace("Page is Disposed");
			} else {
				if (ctx.getApiName().equals("XPXGetCustomerListService")) {
					Element outXml = ctx.getOutputXml().getDocumentElement();
					setModel("XPXCustomerIn",outXml);
					prepareSalesRepModel(outXml);
					setModel("Vikas",outXml);
					page.createTabFolder(outXml);
				}				
			}
		super.handleApiCompletion(ctx);
	}*/
	
	private void updateModelWithParentInfo(Element outXml) {

		Element generalInfo = getModel("XPXCustomerIn");
		String suffixType = YRCXmlUtils.getAttribute(YRCXmlUtils.getXPathElement(generalInfo, "/CustomerList/Customer/Extn"), "ExtnSuffixType");
		
		ArrayList listCustomers = YRCXmlUtils.getChildren(generalInfo, "Customer");
		ArrayList listParentCustomers = YRCXmlUtils.getChildren(outXml, "Customer");
		if (!YRCPlatformUI.isVoid(listParentCustomers) && !YRCPlatformUI.isVoid(listCustomers)) {
			Element customerEle = (Element)listCustomers.get(0);
			if ("MC".equals(suffixType)) {
				Element eleParentMasterCustomer = YRCXmlUtils.createChild(customerEle, "ParentMasterCustomer");
				YRCXmlUtils.importElement(eleParentMasterCustomer, (Element) listParentCustomers.get(0));
			}			
			if ("C".equals(suffixType)) {
				Element eleParentMasterCustomer = YRCXmlUtils.createChild(customerEle, "ParentMasterCustomer");
				Element eleParentCustomer = YRCXmlUtils.createChild(customerEle, "ParentCustomerCustomer");
				YRCXmlUtils.importElement(eleParentCustomer, (Element) listParentCustomers.get(0));
				YRCXmlUtils.importElement(eleParentMasterCustomer, (Element) listParentCustomers.get(1));
			} else if ("B".equals(suffixType)) {
				Element eleParentCustomer = YRCXmlUtils.createChild(customerEle, "ParentCustomerCustomer");
				Element eleParentMasterCustomer = YRCXmlUtils.createChild(customerEle, "ParentMasterCustomer");
				YRCXmlUtils.importElement(eleParentCustomer, (Element) listParentCustomers.get(1));
				YRCXmlUtils.importElement(eleParentMasterCustomer, (Element) listParentCustomers.get(2));
			} else if ("S".equals(suffixType)) {
				Element eleBillToCustomer = YRCXmlUtils.createChild(customerEle, "ParentBillToCustomer");
				Element eleParentCustomer = YRCXmlUtils.createChild(customerEle, "ParentCustomerCustomer");
				Element eleParentMasterCustomer = YRCXmlUtils.createChild(customerEle, "ParentMasterCustomer");
				YRCXmlUtils.importElement(eleBillToCustomer, (Element) listParentCustomers.get(1));
				YRCXmlUtils.importElement(eleParentCustomer, (Element) listParentCustomers.get(2));
				YRCXmlUtils.importElement(eleParentMasterCustomer, (Element) listParentCustomers.get(3));
			}
		}
		setModel("XPXCustomerIn", generalInfo);
		setCustomerDetails(generalInfo);
		page.createTabFolder(generalInfo);

	}

	
	private void callApi(String apinames, Document inputXml) {
		YRCApiContext ctx = new YRCApiContext();
		ctx.setFormId(page.getFormId());
		ctx.setApiName(apinames);
		ctx.setInputXml(inputXml);
		if (!page.isDisposed())
			callApi(ctx, page);
	}
	public Element getLocalModel(String modelName){
		return getModel(modelName);
	}
	public List empList;
	String primarySalesRepId;
	private void prepareSalesRepModel(Element outXml){
		Element salesRepList = YRCXmlUtils.getXPathElement(outXml, "/CustomerList/Customer/Extn/XPEDXSalesRepList");
		Element salesRepDoc = YRCXmlUtils.createFromString("<SalesRepList/>").getDocumentElement();
		Element PrimarySalesRepDoc = YRCXmlUtils.createFromString("<PrimarySalesRepList/>").getDocumentElement();
		if(null != salesRepList){
			NodeList salesReps = salesRepList.getElementsByTagName("XPEDXSalesRep");
			Element childSalesRepEle = null;
			for (int i = 0; i < salesReps.getLength(); i++) {
				Element salesRep = (Element) salesReps.item(i);
				String SalesRepID = salesRep.getAttribute("SalesRepId");
				if("Y".equals(salesRep.getAttribute("PrimarySalesRepFlag"))){
					primarySalesRepId=SalesRepID;
					childSalesRepEle = YRCXmlUtils.createChild(PrimarySalesRepDoc, "SalesRep");
					
				} else {
					childSalesRepEle = YRCXmlUtils.createChild(salesRepDoc, "SalesRep");
				}
				
					Element userElement = YRCXmlUtils.getXPathElement(
							salesRep, "/XPEDXSalesRep/YFSUser");
					if(null !=userElement)
					childSalesRepEle.setAttribute("SalesRepName", userElement
							.getAttribute("Username"));
				
				
			}
		}
		setModel("SalesRepList",salesRepDoc);
		setModel("PrimarySalesRepList",PrimarySalesRepDoc);
	}
	
	public Element getCustomerDetails() {
		return customerDetails;
	}
	public void setCustomerDetails(Element customerDetails) {
		this.customerDetails = customerDetails;
	}
	
	public Element createModelForAdditionalEmails(Element outXml) {
		
		Element emailListEle = YRCXmlUtils.createFromString("<EmailList/>").getDocumentElement();
		Element extnCustListParentEle = YRCXmlUtils.getXPathElement(outXml, "/CustomerList/Customer/Extn/XPXCustomerExtnListList/");
		List extnCustListChildList  = YRCXmlUtils.getChildren(extnCustListParentEle, "XPXCustomerExtnList");
		if(extnCustListChildList!=null) {
			existingEmailList = new ArrayList<String>();
			emailMap = new HashMap<String, String>();
			for (int i = 0; i < extnCustListChildList.size(); i++) {
				Element extnCustListChildEle = (Element)extnCustListChildList.get(i);
				String emailAddress =(String)YRCXPathUtils.evaluate(extnCustListChildEle, "./@ExtnListValue", XPathConstants.STRING);
				String extnCustExtnListKey=(String)YRCXPathUtils.evaluate(extnCustListChildEle, "./@CustomerExtnListKey", XPathConstants.STRING);
				if(!YRCPlatformUI.isVoid(emailAddress)) {
					existingEmailList.add(emailAddress);
					Element optionEle = YRCXmlUtils.createChild(emailListEle, "Email");
					optionEle.setAttribute("emailAddress", emailAddress);
					emailMap.put(emailAddress, extnCustExtnListKey);
				}
			}
		}
		return emailListEle;		
		
	}
	public List<String> getExistingEmailList() {
		return existingEmailList;
	}
	public void setExistingEmailList(List<String> existingEmailList) {
		this.existingEmailList = existingEmailList;
	}
	public Map<String, String> getEmailMap() {
		return emailMap;
	}
	public void setEmailMap(Map<String, String> emailMap) {
		this.emailMap = emailMap;
	}
	
}
