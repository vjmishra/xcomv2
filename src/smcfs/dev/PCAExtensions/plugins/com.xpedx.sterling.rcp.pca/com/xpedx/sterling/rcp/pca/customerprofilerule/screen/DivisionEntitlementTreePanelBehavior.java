/*
 * Created on Apr 14,2010
 *
 */
package com.xpedx.sterling.rcp.pca.customerprofilerule.screen;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class DivisionEntitlementTreePanelBehavior extends YRCBehavior {

	private Element inputElement;
	private DivisionEntitlementTreePanel page;
	String organizationCode;
	Element multiAPIDocElement=null;
	private String customerKey;

	public DivisionEntitlementTreePanelBehavior( DivisionEntitlementTreePanel divisionEntitlementTreePanel,
			Object inputObject, CustomerProfileMaintenance parentObj) {

		super(divisionEntitlementTreePanel, divisionEntitlementTreePanel.getFormId(), inputObject);
		this.page = divisionEntitlementTreePanel;
		this.inputElement=((YRCEditorInput) inputObject).getXml();

		Element generalInfo= parentObj.getBehavior().getLocalModel("XPXCustomerIn"); //TODO what for? need?
		setModel("XPXCustomerIn",generalInfo);

		customerKey = YRCXmlUtils.getAttribute(this.inputElement, "CustomerKey");
		initPage();
	}

	public void initPage() {
		customerKey = YRCXmlUtils.getAttribute(this.inputElement, "CustomerKey");
		getEntitlementsFromServer(customerKey);
	}

	private void getEntitlementsFromServer(String customerKey) {
		System.out.println("Behavior callApi XPXGetCustomerList from getEntitlementsFromServer for customerKey: " + customerKey);
		if(!YRCPlatformUI.isVoid(customerKey))
		{
			callApi("XPXGetCustomerList", YRCXmlUtils.createFromString("<Customer  CustomerKey='" + YRCXmlUtils.getAttribute(this.inputElement, "CustomerKey") + "' />"),null);
		}
	}

	private void callApi(String apiName, Document inputXml, String formId) {
		YRCApiContext ctx = new YRCApiContext();
		ctx.setFormId((!YRCPlatformUI.isVoid(formId))?formId:page.getFormId());
		ctx.setApiName(apiName);
		ctx.setInputXml(inputXml);
		if (!page.isDisposed())
			System.out.println("Behavior callApi for " +apiName+ "...");
			callApi(ctx, page);
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
					System.out.println("handleApi called on Behavior for " + apiname);

					if ("XPXGetCustomerList".equals(apiname)) {
						System.out.println("API XPXGetCustomerList called, invoke getChildList()...");

						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						setModel("XPXGetImmediateChildCustomerListService",outXml);
						getChildList();    //--function used to set the values of child nodes in Tree structure

					} else if ("manageCustomer".equals(apiname)) {
						//[can't add to tree like when first opened page using  getEntitlementsFromServer(customerKey);
						//TODO reset updated element's OldValue? Currently assuming update will work

						//((XPXUserProfileEditor)YRCDesktopUI.getCurrentPart()).showBusy(false); need? NPE?
					} else {
						YRCPlatformUI.showWarning("DivisionEntitlementTreePanel", "Unexpected API response - API:" + apiname);
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

	public void getChildList(){
		Element childCustomerList = getModel("XPXGetImmediateChildCustomerListService");
		Element customerElement = YRCXmlUtils.getChildElement(childCustomerList, "Customer");
		ArrayList<Element> arrlst=new ArrayList<Element>();
		arrlst.add(customerElement);

		page.setTreeValues(null, arrlst); //--function used to set the values of child nodes in Tree structure
	}

	public void createManageCustomerInput(String custKey, boolean enabled){
		// Create single customer input record
		Document docElement = YRCXmlUtils.createFromString("<Customer CustomerKey='" + custKey +
				"' CustomerLevel=" + (enabled ? "'Y'" : "'N'") + "></Customer>");

		//TODO for now, callApi for each!
		// If/when switch to set-based API, have multiAPIDocElement build up CustomerList here,
		//  remove this callApi and enable callApi on CustList in updateAction() below
		multiAPIDocElement = docElement.getDocumentElement();

//		if(multiAPIDocElement == null){
//			multiAPIDocElement = YRCXmlUtils.createDocument("ManageCustomerAndAssignment").getDocumentElement();
//		}
//		multiAPIDocElement.setAttribute("IgnoreOrdering", "Y");
//
//		Element applyEntitlement=(Element)multiAPIDocElement.getElementsByTagName("CustomerAssignmentList").item(0);
//		if(applyEntitlement == null)
//		{
//			applyEntitlement= YRCXmlUtils.createChild(multiAPIDocElement, "CustomerAssignmentList");
//		}

		System.out.println("XML for this shipTo : " + YRCXmlUtils.getString(multiAPIDocElement)); //TODO remove all printlns
		callApi("manageCustomer", docElement, null);
	}

	public void updateAction(){
		page.getTargetModelForUpdateAssignments();
		if (multiAPIDocElement != null && !page.isDisposed()) {
			//(change to use local callApi method?)
			YRCApiContext ctx = new YRCApiContext();
			ctx.setFormId(page.getFormId());
			ctx.setApiName("manageCustomer");
			ctx.setInputXml(multiAPIDocElement.getOwnerDocument());
			ctx.setShowError(false);
			ctx.setUserData("isRefreshReqd", String.valueOf(false));
			//TODO restore this callApi if calling set-based API rather than one at a time previously
			//System.out.println("Behavior callApi from updateAction for manageCustomer: " + YRCXmlUtils.getString(multiAPIDocElement));
			//callApi(ctx, page);
			//((XPXUserProfileEditor)YRCDesktopUI.getCurrentPart()).showBusy(true); //TODO need? works?
		}
	}

	public boolean isThisEntitled(Element eleCust) {
		return "Y".equalsIgnoreCase(eleCust.getAttribute("CustomerLevel"));
	}
}