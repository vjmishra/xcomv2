package com.xpedx.nextgen.common.util;

import java.rmi.RemoteException;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.woodstock.util.frame.log.base.ISCILogger;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.log.YFCLogCategoryFactory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

/**
 * @author sdodda
 * <br/>
 * COM Order Summary Utility class.
 */
public class XPXGetCustomerListForCOMOrderSummaryAPI implements YIFCustomApi {

	private static YIFApi api = null;
	private static ISCILogger log = null;
	private Properties _properties = null;
	static {
		// Create private static variables for log and api instances

		log = new YFCLogCategoryFactory().getLogger(XPXGetCustomerListForCOMOrderSummaryAPI.class.getCanonicalName());
		try {

			api = YIFClientFactory.getInstance().getApi();

		} catch (YIFClientCreationException e) {
			e.printStackTrace();
		}
	}

	public void setProperties(Properties _properties) throws Exception {
		this._properties = _properties;

	}

	/**
	 * COM Related service: Used for Getting the ShipTo customer Details, by inheriting Customer Level attributes.<br/>
	 * Input XML Format:
	 * 
	 * <pre>
	 * 	
	 * &lt;Customer CuatomerID=&quot;&quot; OrganizationCode=&quot;&quot; CustomerKey=&quot;&quot; /&gt;
	 * 
	 * Either CustomerKey or combination of [OrganizationCode,CuatomerID] are mandatory.<br/>
	 * Used in COM Order Summary Screen.<br/>
	 * 
	 * @param inDoc
	 * @param env
	 * @return
	 * 
	 */
	public Document getCustomerList(YFSEnvironment env, Document docIn) {
		// check if the Environment/Input document passed is null, throw exception
		if (env == null) {
			// Throw new YFSException with the description
			throw new YFSException("YFSEnvironment cannot be null or invalid to XPXGetCustomerListForCOMOrderSummaryAPI.getCustomerList(...)");
		}
		if (docIn == null) {
			// Throw new YFSException with the description
			throw new YFSException("Input XML document cannot be null or invalid to XPXGetCustomerListForCOMOrderSummaryAPI.getCustomerList(...)");
		}

		log.debug("XPXGetCustomerListForCOMOrderSummaryAPI.getCustomerList(...)--> Input XML" + SCXmlUtil.getString(docIn));
		
		// create references with VOID values.
		Document docOut = null;
		String strParentCustomerKey = "";
		Element eleShipTo = null;
		Element eleBillTo = null;
		Element eleSAPCustomer = null;
		String strSuffixType = "";

		// Using the same template as the getCustomerList API on Order Summary screen.
		env.setApiTemplate("getCustomerList", "template/com.xpedx.sterling.rcp.pca/com.xpedx.sterling.rcp.pca.orderlines.wizard.XPXOrderLinesWizard/namespaces/XPXGetCustomerListService.xml");
		// Iterate through each customer type till we get ShipTo, BillTo and SAP Customer Details.
		while (true) {
			if (docIn == null && !SCUtil.isVoid(strParentCustomerKey)) {
				docIn = SCXmlUtil.createDocument("Customer");
				docIn.getDocumentElement().setAttribute("CustomerKey", strParentCustomerKey);
			}

			if (docIn != null) {
				Document getCustomerList = null;
				Element eleCustomer = null;
				try {
					getCustomerList = api.invoke(env, XPXLiterals.GET_CUSTOMER_LIST_API, docIn);

					eleCustomer = SCXmlUtil.getChildElement(getCustomerList.getDocumentElement(), "Customer");
					if (null != eleCustomer) {
						Element eleExtn = SCXmlUtil.getChildElement(eleCustomer, "Extn");
						if (null != eleExtn) {
							strSuffixType = eleExtn.getAttribute("ExtnSuffixType");
							if ("S".equals(strSuffixType)) {
								eleShipTo = eleCustomer;
								docOut = getCustomerList;
							} else if ("B".equals(strSuffixType)) {
								eleBillTo = eleCustomer;
							} else if ("C".equals(strSuffixType)) {
								eleSAPCustomer = eleCustomer;
							}
						}

					}

				} catch (YFSException e) {
					log.error("XPXGetCustomerListForCOMOrderSummaryAPI.getCustomerList(...)--> Error Occured while querying getCustomerList API.");
					e.printStackTrace();
				} catch (RemoteException e) {
					log.error("XPXGetCustomerListForCOMOrderSummaryAPI.getCustomerList(...)--> Error Occured while querying getCustomerList API.");
					e.printStackTrace();
				}

				strParentCustomerKey = eleCustomer.getAttribute("ParentCustomerKey");
				docIn = null;
				if (SCUtil.isVoid(strParentCustomerKey) || "C".equals(strSuffixType))
					break;
			}
		}
		
		// Copy the Inheritable fields from SAP Customer to ShipTo Customer element.
		if (null != eleShipTo && null != eleSAPCustomer) {
			Element eleShipToExtn = SCXmlUtil.getChildElement(eleShipTo, "Extn");
			Element eleSAPExtn = SCXmlUtil.getChildElement(eleSAPCustomer, "Extn");
			if(null != eleShipToExtn && null != eleSAPExtn){
				eleShipToExtn.setAttribute("ExtnCustLineSeqNoFlag", eleSAPExtn.getAttribute("ExtnCustLineSeqNoFlag"));
				eleShipToExtn.setAttribute("ExtnCustLinePONoFlag", eleSAPExtn.getAttribute("ExtnCustLinePONoFlag"));
				eleShipToExtn.setAttribute("ExtnCustLineAccNoFlag", eleSAPExtn.getAttribute("ExtnCustLineAccNoFlag"));
				eleShipToExtn.setAttribute("ExtnCustLineAccLbl", eleSAPExtn.getAttribute("ExtnCustLineAccLbl"));
				eleShipToExtn.setAttribute("ExtnCustLineField1Flag", eleSAPExtn.getAttribute("ExtnCustLineField1Flag"));
				eleShipToExtn.setAttribute("ExtnCustLineField1Label", eleSAPExtn.getAttribute("ExtnCustLineField1Label"));
				eleShipToExtn.setAttribute("ExtnCustLineField2Flag", eleSAPExtn.getAttribute("ExtnCustLineField2Flag"));
				eleShipToExtn.setAttribute("ExtnCustLineField2Label", eleSAPExtn.getAttribute("ExtnCustLineField2Label"));
				eleShipToExtn.setAttribute("ExtnCustLineField3Flag", eleSAPExtn.getAttribute("ExtnCustLineField3Flag"));
				eleShipToExtn.setAttribute("ExtnCustLineField3Label", eleSAPExtn.getAttribute("ExtnCustLineField3Label"));
			}
			// ShipComplete will be set at BT,ST.
			// eleShipToExtn.setAttribute("", eleSAPExtn.getAttribute(""));
		}
		if(null != eleShipTo && null != eleBillTo ){
			Element eleShipToExtn = SCXmlUtil.getChildElement(eleShipTo, "Extn");
			Element eleBillToExtn = SCXmlUtil.getChildElement(eleBillTo, "Extn");
			if(null != eleShipToExtn && null != eleBillToExtn){
				
				if(SCUtil.isVoid(eleShipToExtn.getAttribute("ExtnShipComplete"))){
					eleShipToExtn.setAttribute("ExtnShipComplete", eleBillToExtn.getAttribute("ExtnShipComplete"));
				}
				String shipToMaxOrderAmt = eleShipToExtn.getAttribute("ExtnMaxOrderAmount");
				String shipToMinOrderAmt = eleShipToExtn.getAttribute("ExtnMinOrderAmount");
				String shipToMinChargeAmt = eleShipToExtn.getAttribute("ExtnMinChargeAmount");
				
				if(SCUtil.isVoid(shipToMaxOrderAmt) ||  Double.parseDouble(shipToMaxOrderAmt) == 0){
					eleShipToExtn.setAttribute("ExtnMaxOrderAmount", eleBillToExtn.getAttribute("ExtnMaxOrderAmount"));
				}
				
				if(SCUtil.isVoid(shipToMinOrderAmt) || Double.parseDouble(shipToMinOrderAmt)== 0){
					eleShipToExtn.setAttribute("ExtnMinOrderAmount", eleBillToExtn.getAttribute("ExtnMinOrderAmount"));
				}
				
				if(SCUtil.isVoid(shipToMinChargeAmt) || Double.parseDouble(shipToMinChargeAmt)==0){
					eleShipToExtn.setAttribute("ExtnMinChargeAmount", eleBillToExtn.getAttribute("ExtnMinChargeAmount"));
				}
			}
		}
		//clear the template.
		env.clearApiTemplate("getCustomerList");

		log.debug("XPXGetCustomerListForCOMOrderSummaryAPI.getCustomerList(...)--> Output XML" + SCXmlUtil.getString(docOut));
		return docOut;
	}

}
