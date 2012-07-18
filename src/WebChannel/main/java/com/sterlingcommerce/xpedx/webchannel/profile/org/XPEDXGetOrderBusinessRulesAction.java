/*
 * Action Class - Business Rules for Customer.
 * @author - adsouza
 *
 */

package com.sterlingcommerce.xpedx.webchannel.profile.org;

import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXDraftOrderDropDownAction;


public class XPEDXGetOrderBusinessRulesAction extends WCMashupAction {

    public XPEDXGetOrderBusinessRulesAction() {

    	AcceptPriceORFlag = false;
    	PrvntAutoOrdersFlag = false;
    	POAckFlag = false;

    	CustHeaderCommentsFlag = false;
    	DupPOFlag = false;
    	NonStdShipFlag = false;
    	UseCustAmtLimitFlag = false;
    	CustShipCompleteFlag = false;
    	ValSTZipFlag = false;
    	ShipNotNextBusDayFlag = false;

    	CustLineCommentsFlag = false;
    	CustLineAccNoFlag = false;
    	CustLineSeqNoFlag = false;
    	CustLinePONoFlag = false;
    	CustLineField1Flag = false;
    	CustLineField2Flag = false;
    	CustLineField3Flag = false;
    	LineDelDateMatchFlag = false;
    	PrvntBOFlag = false;
    	ItemNotAvlNextDayFlag = false;
    	CustLineLvlCodeFlag = false;
    	PrvntPriceBelowCostFlag = false;
    	PriceDiscrpFlag = false;

	}


	public String execute() {
		try {
			outputDoc = (Element) prepareAndInvokeMashup("xpedx-cust-GetOrderBusinessRules");
			if(log.isDebugEnabled()){
		    log.debug("getCustomerDetails Output" + SCXmlUtils.getString(outputDoc));
			}
		} catch (Exception ex) {
			log.error(ex);
		}
		setBusinessRulesFlags();
		return "success";
	}

	protected void setBusinessRulesFlags() {

		Element extnElem = XMLUtilities.getChildElementByName(outputDoc, "Extn");


		if ("Y".equals(SCXmlUtils.getAttribute(extnElem, "ExtnAcceptPriceORFlag")))  AcceptPriceORFlag = true;
		if ("Y".equals(SCXmlUtils.getAttribute(extnElem, "ExtnPrvntAutoOrdersFlag")))  PrvntAutoOrdersFlag = true;
		if ("Y".equals(SCXmlUtils.getAttribute(extnElem, "ExtnPOAckFlag")))  POAckFlag = true;

		if ("Y".equals(SCXmlUtils.getAttribute(extnElem, "ExtnCustHeaderCommentsFlag")))  CustHeaderCommentsFlag = true;
		if ("Y".equals(SCXmlUtils.getAttribute(extnElem, "ExtnDupPOFlag")))  DupPOFlag = true;
		if ("Y".equals(SCXmlUtils.getAttribute(extnElem, "ExtnNonStdShipFlag")))  NonStdShipFlag = true;
		if ("Y".equals(SCXmlUtils.getAttribute(extnElem, "ExtnUseCustAmtLimitFlag")))  UseCustAmtLimitFlag = true;
		if ("Y".equals(SCXmlUtils.getAttribute(extnElem, "ExtnCustShipCompleteFlag")))  CustShipCompleteFlag = true;
		if ("Y".equals(SCXmlUtils.getAttribute(extnElem, "ExtnValSTZipFlag")))  ValSTZipFlag = true;
		if ("Y".equals(SCXmlUtils.getAttribute(extnElem, "ExtnShipNotNextBusDayFlag")))  ShipNotNextBusDayFlag = true;

		if ("Y".equals(SCXmlUtils.getAttribute(extnElem, "ExtnCustLineCommentsFlag")))  CustLineCommentsFlag = true;
		if ("Y".equals(SCXmlUtils.getAttribute(extnElem, "ExtnCustLineAccNoFlag")))  CustLineAccNoFlag = true;
		if ("Y".equals(SCXmlUtils.getAttribute(extnElem, "ExtnCustLineSeqNoFlag")))  CustLineSeqNoFlag = true;
		if ("Y".equals(SCXmlUtils.getAttribute(extnElem, "ExtnCustLinePONoFlag")))  CustLinePONoFlag = true;
		if ("Y".equals(SCXmlUtils.getAttribute(extnElem, "ExtnCustLineField1Flag")))  CustLineField1Flag = true;
		if ("Y".equals(SCXmlUtils.getAttribute(extnElem, "ExtnCustLineField2Flag")))  CustLineField2Flag = true;
		if ("Y".equals(SCXmlUtils.getAttribute(extnElem, "ExtnCustLineField3Flag")))  CustLineField3Flag = true;
		if ("Y".equals(SCXmlUtils.getAttribute(extnElem, "ExtnLineDelDateMatchFlag")))  LineDelDateMatchFlag = true;
		if ("Y".equals(SCXmlUtils.getAttribute(extnElem, "ExtnPrvntBOFlag")))  PrvntBOFlag = true;
		if ("Y".equals(SCXmlUtils.getAttribute(extnElem, "ExtnItemNotAvlNextDayFlag")))  ItemNotAvlNextDayFlag = true;
		if ("Y".equals(SCXmlUtils.getAttribute(extnElem, "ExtnCustLineLvlCodeFlag")))  CustLineLvlCodeFlag = true;
		if ("Y".equals(SCXmlUtils.getAttribute(extnElem, "ExtnPrvntPriceBelowCostFlag")))  PrvntPriceBelowCostFlag = true;
		if ("Y".equals(SCXmlUtils.getAttribute(extnElem, "ExtnPriceDiscrpFlag")))  PriceDiscrpFlag = true;
	}



	public boolean IsAcceptPriceORFlag() {
		return AcceptPriceORFlag;
	}

	public boolean IsPrvntAutoOrdersFlag() {
		return PrvntAutoOrdersFlag;
	}

	public boolean IsPOAckFlag() {
		return POAckFlag;
	}


	public boolean IsCustHeaderCommentsFlag() {
		return CustHeaderCommentsFlag;
	}

	public boolean IsDupPOFlag() {
		return DupPOFlag;
	}

	public boolean IsNonStdShipFlag() {
		return NonStdShipFlag;
	}

	public boolean IsUseCustAmtLimitFlag() {
		return UseCustAmtLimitFlag;
	}

	public boolean IsCustShipCompleteFlag() {
		return CustShipCompleteFlag;
	}

	public boolean IsValSTZipFlag() {
		return ValSTZipFlag;
	}

	public boolean IsShipNotNextBusDayFlag() {
		return ShipNotNextBusDayFlag;
	}


	public boolean IsCustLineCommentsFlag() {
		return CustLineCommentsFlag;
	}

	public boolean IsCustLineAccNoFlag() {
		return CustLineAccNoFlag;
	}

	public boolean IsCustLineSeqNoFlag() {
		return CustLineSeqNoFlag;
	}

	public boolean IsCustLinePONoFlag() {
		return CustLinePONoFlag;
	}

	public boolean IsCustLineField1Flag() {
		return CustLineField1Flag;
	}

	public boolean IsCustLineField2Flag() {
		return CustLineField2Flag;
	}

	public boolean IsCustLineField3Flag() {
		return CustLineField3Flag;
	}

	public boolean IsLineDelDateMatchFlag() {
		return LineDelDateMatchFlag;
	}

	public boolean IsPrvntBOFlag() {
		return PrvntBOFlag;
	}

	public boolean IsItemNotAvlNextDayFlag() {
		return ItemNotAvlNextDayFlag;
	}

	public boolean IsCustLineLvlCodeFlag() {
		return CustLineLvlCodeFlag;
	}

	public boolean IsPrvntPriceBelowCostFlag() {
		return PrvntPriceBelowCostFlag;
	}

	public boolean IsPriceDiscrpFlag() {
		return PriceDiscrpFlag;
	}

	public Element getOutputDoc() {
		return outputDoc;
	}

	public void setOutputDoc(Element outputDocument) {
		outputDoc = outputDocument;
	}

	private static final Logger log = Logger.getLogger(XPEDXGetOrderBusinessRulesAction.class);
	protected Element outputDoc;

	protected boolean AcceptPriceORFlag;
	protected boolean PrvntAutoOrdersFlag;
	protected boolean POAckFlag;

	protected boolean CustHeaderCommentsFlag;
	protected boolean DupPOFlag;
	protected boolean NonStdShipFlag;
	protected boolean UseCustAmtLimitFlag;
	protected boolean CustShipCompleteFlag;
	protected boolean ValSTZipFlag;
	protected boolean ShipNotNextBusDayFlag;

	protected boolean CustLineCommentsFlag;
	protected boolean CustLineAccNoFlag;
	protected boolean CustLineSeqNoFlag;
	protected boolean CustLinePONoFlag;
	protected boolean CustLineField1Flag;
	protected boolean CustLineField2Flag;
	protected boolean CustLineField3Flag;
	protected boolean LineDelDateMatchFlag;
	protected boolean PrvntBOFlag;
	protected boolean ItemNotAvlNextDayFlag;
	protected boolean CustLineLvlCodeFlag;
	protected boolean PrvntPriceBelowCostFlag;
	protected boolean PriceDiscrpFlag;




}
