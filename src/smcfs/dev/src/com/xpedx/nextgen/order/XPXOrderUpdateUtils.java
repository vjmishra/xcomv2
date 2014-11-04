package com.xpedx.nextgen.order;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.tools.datavalidator.XmlUtils;

public class XPXOrderUpdateUtils {


	/**
	 * verifying is there Back order quantity exist in any of the order line. if exist then set value 'Y'.
	 * @param outputDoc
	 */
	public static void setBackorderQtyFlagIfExists(Document outputDoc){
		String reqBackOrdQtyExist = "N";
		if(outputDoc != null && outputDoc.getDocumentElement() != null && !"Y".equalsIgnoreCase(outputDoc.getDocumentElement().getAttribute("DraftOrderFlag"))){
			Element outputDocElement = outputDoc.getDocumentElement();
			ArrayList<Element> orderLineElemList = SCXmlUtil.getElements(outputDocElement,"OrderLines/OrderLine");
			if(orderLineElemList != null && orderLineElemList.size() > 0){
				for (int i = 0; i < orderLineElemList.size(); i++) {
					Element orderLine = orderLineElemList.get(i);
					if (orderLine != null && orderLine.getAttribute("LineType") != "C" && orderLine.getAttribute("LineType") != "M"){
						Element orderLineExtnElem = XmlUtils.getChildElement(orderLine, "Extn");
						if(orderLineExtnElem != null && !SCUtil.isVoid(orderLineExtnElem.getAttribute("ExtnReqBackOrdQty"))){
							double backOrderedQty = 0;
							try {
								backOrderedQty = Double.parseDouble(orderLineExtnElem.getAttribute("ExtnReqBackOrdQty"));
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
							if(backOrderedQty > 0) {
								reqBackOrdQtyExist = "Y";
								break;
							}
						}
					}
				}
			}
			Element orderExtnElem = XmlUtils.getChildElement(outputDocElement, "Extn");
			if(orderExtnElem == null){
				orderExtnElem = XmlUtils.createChild(outputDocElement, "Extn");
			}
			orderExtnElem.setAttribute("ExtnReqBackOrdQtyExist", reqBackOrdQtyExist);
		}
	}
}
