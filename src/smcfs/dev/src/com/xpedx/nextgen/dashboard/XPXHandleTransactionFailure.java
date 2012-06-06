package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
import java.util.Properties;
import org.w3c.dom.Document;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;


/**
 * Description: Handles all the Webmethods and Legacy Failures.
 * Based on HeaderStatusCode and LineStatusCodes received from WebMethods the order will be put on Needs Attention hold.
 * @author Ranjeet Singh.
 * 
 */

public class XPXHandleTransactionFailure implements YIFCustomApi {

	private static YIFApi api = null;
	private static YFCLogCategory log;

	static {

		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException yifEx) {
			yifEx.printStackTrace();
		}
	}

	public Document handleFailure(YFSEnvironment env, Document inXML)
			throws YFSException, RemoteException {

		String headerStatusCode = null;
		String lineStatusCode = null;
		String orderHeaderKey = null;
		if (inXML != null && inXML.getDocumentElement() != null) {
			YFCElement rootEle = YFCDocument.getDocumentFor(inXML)
					.getDocumentElement();

			log.debug("XPXHandleTransactionFailure_InXML:" + rootEle.getString());
		
			if (rootEle.hasChildNodes()) {
				YFCElement headerCodeElem = rootEle
						.getChildElement("HeaderStatusCode");
				if (headerCodeElem != null) {
					headerStatusCode = headerCodeElem.getNodeValue();
				}
				YFCElement orderHeaderKeyElem = rootEle
						.getChildElement("OrderHeaderKey");
				if (orderHeaderKeyElem != null) {
					orderHeaderKey = orderHeaderKeyElem.getNodeValue();
				}
				YFCElement lineItemsElem = (YFCElement) rootEle
						.getChildElement("LineItems");

				if (lineItemsElem != null) {
					YFCIterable<YFCElement> yfcItr = (YFCIterable) lineItemsElem
							.getChildren("LineItem");
					while (yfcItr.hasNext()) {
						YFCElement lineElem = (YFCElement) yfcItr.next();
						if (lineElem != null) {
							YFCElement lineStatusCodeElem = lineElem
									.getChildElement("LineStatusCode");
							if (lineStatusCodeElem != null) {
								lineStatusCode = lineStatusCodeElem
										.getNodeValue();
								if (!YFCObject.isNull(lineStatusCode)
										&& !YFCObject.isVoid(lineStatusCode))
								{
									break;
								}
							}
						}
					}
				}
			}

			if (orderHeaderKey != null) {
				if (!YFCObject.isNull(headerStatusCode)
						&& !YFCObject.isVoid(headerStatusCode)
						|| !YFCObject.isNull(lineStatusCode)
						&& !YFCObject.isVoid(lineStatusCode)) {
					// To create a document for changeOrder API
					Document changeOrdDoc = YFCDocument.createDocument("Order")
							.getDocument();
					YFCElement orderEle = YFCDocument.getDocumentFor(
							changeOrdDoc).getDocumentElement();
					orderEle.setAttribute("OrderHeaderKey", orderHeaderKey);
					YFCElement extnElem = orderEle.createChild("Extn");
					extnElem.setAttribute("ExtnOrderLockFlag", "N");
					YFCElement ordHoldTypesEle = orderEle
							.createChild("OrderHoldTypes");
					YFCElement ordHoldTypeEle = ordHoldTypesEle
							.createChild("OrderHoldType");
					ordHoldTypeEle.setAttribute("HoldType", "NEEDS_ATTENTION");
					ordHoldTypeEle
							.setAttribute("ReasonText",
									"Hold applied due to invalid data sent to WebMethods or Legacy");
					ordHoldTypeEle.setAttribute("Status", "1100");
					if(log.isDebugEnabled()){
						log.debug("InputXML to the changeOrder API is :"+ SCXmlUtil.getString(changeOrdDoc));
					}
					Document outputDoc = api.invoke(env, "changeOrder",
							changeOrdDoc);
				}
			}
		}
		return inXML;
	}

	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}

}
