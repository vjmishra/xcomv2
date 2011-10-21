package com.xpedx.sterling.rcp.pca.customerprofilerule.screen;

import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;

import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class CustomerProfileRuleLineBehavior extends YRCBehavior {
	private Element inputElement;
	private CustomerProfileRuleLinePanel page;
	public CustomerProfileRuleLineBehavior(Composite ownerComposite,
			String formId, Object objInput, Element eleInput) {
		super(ownerComposite, formId, objInput);
		this.page = (CustomerProfileRuleLinePanel) ownerComposite;
		this.inputElement = ((YRCEditorInput) objInput).getXml();
		setModel("Source", eleInput);
	}
	public Element getTargetModelforParent() {
		Element ele = null;
		Element olElem = this.getTargetModel("Target");
		//System.out.println(YRCXmlUtils.getString(olElem));
		if ("Y".equals(olElem.getAttribute("Selected"))) {
			
			ele = YRCXmlUtils.createDocument(
					"XPXCustomerRulesProfile").getDocumentElement();
			ele.setAttribute("RuleKey", olElem.getAttribute("RuleKey"));
			ele.setAttribute("Param1", olElem.getAttribute("Param1"));
			ele.setAttribute("Param2", olElem.getAttribute("Param2"));
			ele.setAttribute("Param3", olElem.getAttribute("Param3"));
			ele.setAttribute("CustomerKey", YRCXmlUtils.getAttribute(this.inputElement, "CustomerKey"));
			ele.setAttribute("CustomerID",  YRCXmlUtils.getAttribute(this.inputElement, "CustomerID"));
			ele.setAttribute("OrganizationCode",  YRCXmlUtils.getAttribute(this.inputElement, "OrganizationCode"));
		}

		return ele;
	}
	public boolean validateForErrors(){
		
		if("RequiredCustomerLinePO".equals(page.getRuleId())){
			if(!page.isCustomerFieldChecked("RequiredCustomerLinePO")){
				page.getPnlRuleLines().errorMessageList.add("Customer Line PO#");
			}
			
		}
		if("RequireCustomerLineField1".equals(page.getRuleId())){
			if( (!page.isCustomerFieldChecked(page.getRuleId())) || YRCPlatformUI.isVoid(getFieldValue("txtParam1")) )
			page.getPnlRuleLines().errorMessageList.add("CustLine Field1");
		}
		if("RequireCustomerLineField2".equals(page.getRuleId())){
			if( (!page.isCustomerFieldChecked(page.getRuleId())) || YRCPlatformUI.isVoid(getFieldValue("txtParam1")) )
				page.getPnlRuleLines().errorMessageList.add("CustLine Field2");
		}
		if("RequireCustomerLineField3".equals(page.getRuleId())){
			if( (!page.isCustomerFieldChecked(page.getRuleId())) || YRCPlatformUI.isVoid(getFieldValue("txtParam1")) )
			page.getPnlRuleLines().errorMessageList.add("CustLine Field3");
		}
		if("GrossTradingMargin".equals(page.getRuleId())){
			if( YRCPlatformUI.isVoid(getFieldValue("txtParam1")) && YRCPlatformUI.isVoid(getFieldValue("txtParam2")) )
			{
			page.getPnlRuleLines().errorMessageList.add("Gross Trade Margin");
			}
			else{
				String minRangeVal=getFieldValue("txtParam1");
				String maxRangeVal=getFieldValue("txtParam2");

				Float param1Float = null;
				Float param2Float = null;
				if (!YRCPlatformUI.isVoid(getFieldValue("txtParam1"))) {
					param1Float = Float.parseFloat(minRangeVal);
				}
				if (!YRCPlatformUI.isVoid(getFieldValue("txtParam2"))) {
					param2Float = Float.parseFloat(maxRangeVal);
				}
				
				if(param1Float != null && param2Float != null && param2Float<param1Float) {
					YRCPlatformUI.showError("Error",
							"Min Range Value can not be More than Max Range Value For Gross Trade Margin " );
					return true;
				}
				
			}
		}

		return false;
	}

}
