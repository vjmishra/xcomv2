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

		Element generalInfo= parentObj.getBehavior().getLocalModel("XPXCustomerIn"); // need?
		setModel("XPXCustomerIn",generalInfo);

		customerKey = YRCXmlUtils.getAttribute(this.inputElement, "CustomerKey");
		initPage();
	}

	public void initPage() {
		customerKey = YRCXmlUtils.getAttribute(this.inputElement, "CustomerKey");
		getEntitlementsFromServer(customerKey);
	}

	private void getEntitlementsFromServer(String customerKey) {
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
					//System.out.println("handleApi called on Behavior for " + apiname);

					if ("XPXGetCustomerList".equals(apiname)) {
						Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
						setModel("XPXGetImmediateChildCustomerListService",outXml);
						getChildList();    //--function used to set the values of child nodes in Tree structure

					} else if ("XPXManageCustomersAPIService".equals(apiname)) {
						//TODO update element's OldValue? Currently assuming update will work
						// (new set-based API doesn't return updated cust records - address?)

						//YRCDesktopUI.getCurrentPart().showBusy(false);
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

		// Create outer tag if first Ship-To
		if (multiAPIDocElement == null){
			multiAPIDocElement = YRCXmlUtils.createDocument("CustomerList").getDocumentElement();
		}

		Element customerElem=YRCXmlUtils.createChild(multiAPIDocElement, "Customer");
		customerElem.setAttribute("CustomerKey", custKey);
		customerElem.setAttribute("CustomerLevel", (enabled ? "Y" : "N"));
		customerElem.setAttribute("Operation","Update");
	}

	public void updateAction(){
		page.getTargetModelForUpdateAssignments();
		if (multiAPIDocElement != null && !page.isDisposed()) {
			//(change to use local callApi method?)
			YRCApiContext ctx = new YRCApiContext();
			ctx.setFormId(page.getFormId());
			ctx.setApiName("XPXManageCustomersAPIService");
			ctx.setInputXml(multiAPIDocElement.getOwnerDocument());
			ctx.setShowError(false);
			ctx.setUserData("isRefreshReqd", String.valueOf(false));
			//System.out.println("Input for API XPXManageCustomersAPIService: " + YRCXmlUtils.getString(multiAPIDocElement));
			callApi(ctx, page);
			multiAPIDocElement = null;
			//YRCDesktopUI.getCurrentPart().showBusy(true); // need?
		}
	}

	public boolean isThisEntitled(Element eleCust) {
		return "Y".equalsIgnoreCase(eleCust.getAttribute("CustomerLevel"));
	}
}