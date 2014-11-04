package com.xpedx.nextgen.order;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;
import com.yantra.yfs.japi.YFSUserExitException;
import com.yantra.yfs.japi.ue.YFSBeforeCreateOrderUE;

public class XPXBeforeCreateOrderUE implements YFSBeforeCreateOrderUE {

	private static YIFApi api = null;
	@Override
	public String beforeCreateOrder(YFSEnvironment yfsenvironment, String s)
			throws YFSUserExitException {
		if( !YFCCommon.isVoid(s)){
			YFCDocument yfcDoc = null;
			try {
				yfcDoc = YFCDocument.parse(s);
			} catch (SAXException e) {
				e.printStackTrace();
				throw new YFSUserExitException();
			} catch (IOException e) {
				e.printStackTrace();
				throw new YFSUserExitException();
			}
			if(yfcDoc != null && yfcDoc.getDocument() != null){
				Document outDoc =  this.beforeCreateOrder(yfsenvironment,yfcDoc.getDocument());
				return SCXmlUtil.getString(outDoc.getDocumentElement());
			}
		}
		return s;
	}

	@Override
	public Document beforeCreateOrder(YFSEnvironment yfsenvironment,
			Document document) throws YFSUserExitException {

		if(document != null && document.getDocumentElement() != null){
			YFCDocument yfcDoc = YFCDocument.getDocumentFor(document);
			YFCElement createOrderElement = yfcDoc.getDocumentElement();
			Map<String, String> customerDescItemMap = null;
			try {
				customerDescItemMap = getCustomerSpecificDesc(yfsenvironment, createOrderElement);
			} catch (YFSException e) {
				e.printStackTrace();
				throw new YFSUserExitException(e.getMessage());
			} catch (RemoteException e) {
				e.printStackTrace();
				throw new YFSUserExitException(e.getMessage());
			} catch (YIFClientCreationException e) {
				e.printStackTrace();
				throw new YFSUserExitException(e.getMessage());
			}
			if(customerDescItemMap != null && customerDescItemMap.size() > 0){
				YFCElement orderLinesEle = createOrderElement.getChildElement("OrderLines");
				if(orderLinesEle != null){
					YFCIterable<YFCElement> orderLineIteraor = orderLinesEle.getChildren("OrderLine");
					while (orderLineIteraor.hasNext()){
						YFCElement orderLineElement = orderLineIteraor.next();
						YFCElement itemElement = orderLineElement.getChildElement("Item");
						if(itemElement != null && itemElement.getAttribute("ItemID") != null && customerDescItemMap.containsKey(itemElement.getAttribute("ItemID"))){
							itemElement.setAttribute("ItemShortDesc", customerDescItemMap.get(itemElement.getAttribute("ItemID")));
						}
					}
				}
			}
			XPXOrderUpdateUtils.setBackorderQtyFlagIfExists(document);
		}
		return document;
	}

	/**
	 * Getting the Customer specific Item description if available
	 * @param env
	 * @param createOrderElement
	 * @return
	 * @throws YFSException
	 * @throws RemoteException
	 * @throws YIFClientCreationException
	 */
	private Map<String, String> getCustomerSpecificDesc(YFSEnvironment env, YFCElement createOrderElement) throws YFSException, RemoteException, YIFClientCreationException{
		Map<String, String> customerDescItemMap = null;
		boolean isAPIcallRequired = false;
		if(createOrderElement != null){
			YFCElement orderExtnEle = createOrderElement.getChildElement("Extn");
			if(orderExtnEle != null){
				String extnCustomerDivision = orderExtnEle.getAttribute("ExtnCustomerDivision");
				String extnEnvironmentCode = orderExtnEle.getAttribute("ExtnEnvtId");
				String extnCustomerNumber = orderExtnEle.getAttribute("ExtnCustomerNo");
				String extnCompanyCode = orderExtnEle.getAttribute("ExtnCompanyId");
				if( !YFCCommon.isVoid(extnEnvironmentCode) && !YFCCommon.isVoid(extnCustomerNumber)
						&& !YFCCommon.isVoid(extnCompanyCode) && !YFCCommon.isVoid(extnCustomerDivision) && extnCustomerDivision.length() >= 2){
					Document xpedxItemCustXRefInputDoc = YFCDocument.createDocument("XPXItemcustXref").getDocument();
					xpedxItemCustXRefInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_COMPANY_CODE, extnCompanyCode);
					xpedxItemCustXRefInputDoc.getDocumentElement().setAttribute("EnvironmentCode", extnEnvironmentCode);
					xpedxItemCustXRefInputDoc.getDocumentElement().setAttribute("CustomerNumber",extnCustomerNumber);
					xpedxItemCustXRefInputDoc.getDocumentElement().setAttribute("CustomerDivision",extnCustomerDivision.substring(0,2));
					Element complexQueryElement = SCXmlUtil.createChild(xpedxItemCustXRefInputDoc.getDocumentElement(), "ComplexQuery");
					Element orElement = SCXmlUtil.createChild(complexQueryElement, "Or");
					YFCElement orderLinesEle = createOrderElement.getChildElement("OrderLines");
					if(orderLinesEle != null){
						YFCIterable<YFCElement> orderLineIteraor = orderLinesEle.getChildren("OrderLine");
						if(orderLineIteraor != null){
							while (orderLineIteraor.hasNext()){
								YFCElement orderLineElement = orderLineIteraor.next();
								YFCElement itemElement = orderLineElement.getChildElement("Item");
								Element expElement = SCXmlUtil.createChild(orElement, "Exp");
								expElement.setAttribute("Name", "LegacyItemNumber");
								expElement.setAttribute("Value", itemElement.getAttribute("ItemID"));
								isAPIcallRequired = true;
							}
							if(isAPIcallRequired){
								if(api == null){
									api = YIFClientFactory.getInstance().getApi();
								}
								Document xpedxItemCustXRefOutputDoc = api.executeFlow(env, XPXLiterals.GET_XREF_LIST, xpedxItemCustXRefInputDoc);
								if (xpedxItemCustXRefOutputDoc != null && xpedxItemCustXRefOutputDoc.getDocumentElement() != null) {
									Element xpedxItemCustXRefOutputElement = xpedxItemCustXRefOutputDoc.getDocumentElement();
									List<Element> xpedxItemCustXRefElements = SCXmlUtil.getElements(xpedxItemCustXRefOutputElement, XPXLiterals.E_XPX_ITEM_CUST_XREF);
									if(xpedxItemCustXRefElements != null && xpedxItemCustXRefElements.size() > 0){
										customerDescItemMap = new HashMap<String, String>();
										for (Element xpedxItemCustXRefElement : xpedxItemCustXRefElements) {
											if (xpedxItemCustXRefElement != null && !YFCCommon.isVoid(xpedxItemCustXRefElement.getAttribute("CustomerDecription"))) {
												customerDescItemMap.put(xpedxItemCustXRefElement.getAttribute("LegacyItemNumber"), xpedxItemCustXRefElement.getAttribute("CustomerDecription"));
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return customerDescItemMap;
	}
}
