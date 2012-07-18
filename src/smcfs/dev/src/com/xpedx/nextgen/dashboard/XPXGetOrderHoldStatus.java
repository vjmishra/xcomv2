package com.xpedx.nextgen.dashboard;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;


public class XPXGetOrderHoldStatus  implements YIFCustomApi {

	private static YFCLogCategory log;
	private static YIFApi api = null;

	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");

		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Document getOrderHoldStatus(YFSEnvironment env, Document docConfirmDraftOrderXml)
	{

		try{
			if(log.isDebugEnabled()){
			log.debug("The input to XPXGetOrderHoldStatus is: "+SCXmlUtil.getString(docConfirmDraftOrderXml));
			}
			NodeList nlOrdHoldTypes = docConfirmDraftOrderXml.getDocumentElement().getElementsByTagName(XPXLiterals.E_ORDER_HOLD_TYPES);
			if(nlOrdHoldTypes!=null && nlOrdHoldTypes.getLength()==0){
				String orderHeaderKey = docConfirmDraftOrderXml.getDocumentElement().getAttribute(XPXLiterals.A_ORDER_HEADER_KEY);
				Document inputOrderDoc = YFCDocument.createDocument("Order").getDocument();
				Element inputOrderElement = inputOrderDoc.getDocumentElement();
				inputOrderElement.setAttribute("OrderHeaderKey",orderHeaderKey);
				Document orderListTemplateDoc = setOrderListTemplate(env);
				env.setApiTemplate("getOrderList",orderListTemplateDoc);

				Document orderListDocument = api.invoke(env,"getOrderList",inputOrderDoc);
				if(log.isDebugEnabled()){
					log.debug("The getOrderList doc is: "+SCXmlUtil.getString(orderListDocument));
				}
				env.clearApiTemplate("getOrderList");
				Element eleOrdHoldType =(Element) orderListDocument.getDocumentElement().getElementsByTagName(XPXLiterals.E_ORDER_HOLD_TYPES).item(0);    
				Element eleConfirmDraftOrder = docConfirmDraftOrderXml.getDocumentElement();
				eleConfirmDraftOrder.appendChild(docConfirmDraftOrderXml.importNode(eleOrdHoldType,true));
			}
			if(log.isDebugEnabled()){
				log.debug("getOrderList:"+SCXmlUtil.getString(docConfirmDraftOrderXml));
			}

		}
		catch(Exception e){
			log.error("Error while executing getOrderHoldStatus  "+e.getMessage() );

		}

		return  docConfirmDraftOrderXml; 
	}

	private Document setOrderListTemplate(YFSEnvironment env)
	{
		Document orderTemplateDoc = YFCDocument.createDocument("OrderList").getDocument();
		Element orderTemplateElement = orderTemplateDoc.getDocumentElement();
		Element orderElement = orderTemplateDoc.createElement("Order");
		orderTemplateElement.appendChild(orderElement);
		Element orderHoldTypes=orderTemplateDoc.createElement("OrderHoldTypes");
		orderElement.appendChild(orderHoldTypes);
		Element orderHoldType=orderTemplateDoc.createElement("OrderHoldType");
		orderHoldType.setAttribute("HoldType", "");
		orderHoldType.setAttribute("Status","");
		orderHoldTypes.appendChild(orderHoldType);
		return orderTemplateDoc;
	}

	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}

}
