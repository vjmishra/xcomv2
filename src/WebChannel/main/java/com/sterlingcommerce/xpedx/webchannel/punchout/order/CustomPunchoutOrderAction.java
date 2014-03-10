package com.sterlingcommerce.xpedx.webchannel.punchout.order;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.framework.extensions.ISCUITransactionContext;
import com.sterlingcommerce.ui.web.platform.transaction.SCUITransactionContextFactory;
import com.sterlingcommerce.webchannel.common.eprocurement.AribaContextImpl;
import com.sterlingcommerce.webchannel.common.eprocurement.IAribaContext;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.order.utilities.CommerceContext;
import com.sterlingcommerce.webchannel.utilities.WCIntegrationXMLUtils;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCUtils;
import com.sterlingcommerce.xpedx.webchannel.punchout.PunchoutRequest;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.common.util.XPXTranslationUtilsAPI;
import com.xpedx.nextgen.order.XPXPunchOutOrder;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

/**
 * Action handler for performing submit order for punchout users
 * (and return to) an e-procurement system.
 */
public class CustomPunchoutOrderAction extends WCMashupAction {
	private static final String GET_PUNCHOUT_ORDER_DETAILS_MASHUP = "XPEDXGetPunchoutOrderDetails";
	private static final String PUNCHOUT_XSL_LOCATION = "/global/template/xsl/punchout/";
	public static final String MODE_SAVE = "save";
	public static final String MODE_CANCEL = "cancel";
	public static final String DRAFT_ORDER_DELETE_MASHUP = "draftOrderDelete";
	public static final String GET_PUNCH_OUT_ORDER_MESSAGE_CXML_MASHUP = "XPEDXGetPunchOutOrderMessageCXML";
	public static final String USER_AGENT_PROPERTY_KEY = "swc.ariba.userAgent";

	public String mashUpId = null;

	protected String orderHeaderKey = null;
	protected String returnUrl = null;
	protected String cxmlData = null;
	protected String ociData = null;

	protected String msap = null;
	private PunchoutRequest punchoutRequest = null;

	private static final Logger LOG = Logger.getLogger(CustomPunchoutOrderAction.class);

	@Override
	public String execute() {
		String toReturn = SUCCESS;
		try {
			punchoutRequest = (PunchoutRequest) XPEDXWCUtils.getObjectFromCache("PunchoutRequest");
			CommerceContext cc = (CommerceContext) getWCContext().getWCAttribute("CommerceContextObject");

			AribaContextImpl aribaContext = AribaContextImpl.getInstance();

			if (punchoutRequest == null)
				throw new Exception("No procurement punch in context information found.");

			setReturnUrlfromRequest();

			String outputData = populatePunchOutOrderMessage(cc.getOrderHeaderKey(), aribaContext);

			if (punchoutRequest.isCXML()) {
				cXmlFormat(outputData);
			}
			else {
				ociFormat(outputData);
			}

			deleteCart(cc.getOrderHeaderKey());

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			WCUtils.setErrorInContext(getWCContext(), e);

			return ERROR;
		}

		return toReturn;
	}

	private String populatePunchOutOrderMessage(String orderHeaderKey,
			IAribaContext aribaContext) throws Exception {

		IWCContext context = WCContextHelper.getWCContext(ServletActionContext.getRequest());
		SCUIContext wSCUIContext = context.getSCUIContext();
		ISCUITransactionContext scuiTransactionContext = wSCUIContext.getTransactionContext(true);
		YFSEnvironment env = (YFSEnvironment) scuiTransactionContext
				.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);

		String customerID = context.getCustomerId();
		String storeFrontId = context.getStorefrontId();
		msap = XPXTranslationUtilsAPI.getMsap(env, customerID, storeFrontId);

		// replaceChars not supported (later?). Is xslt version being set/used??
		Document CustDetails = XPEDXWCUtils.getCustomerDetails(msap, storeFrontId);
		String xsltVersion = SCXmlUtil.getXpathAttribute(CustDetails.getDocumentElement(),"/Customer/Extn/@ExtnXSLTVer");
		String xsltFileName = SCXmlUtil.getXpathAttribute(CustDetails.getDocumentElement(), "/Customer/Extn/@ExtnXSLTFileName");
		String replaceChars = SCXmlUtil.getXpathAttribute( CustDetails.getDocumentElement(), "/Customer/Extn/@ExtnReplaceCharacter");

		Document orderOutputDoc = getOrderDetails(orderHeaderKey, env);

		updateOrderUoms(env, orderOutputDoc);

		return transformUsingXslt(orderOutputDoc, xsltFileName);
	}

	private void cXmlFormat(String outputData)
			throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document orderOutputDoc = builder.parse(new InputSource(new StringReader(outputData)));
		updatetimeStampValue(orderOutputDoc);
		updateheaderElementValue(orderOutputDoc, "From", "Identity", punchoutRequest.getFromIdentity());
		updateheaderElementValue(orderOutputDoc, "To", "Identity", punchoutRequest.getToIdentity());
		updateheaderElementValue(orderOutputDoc, "Sender", "Identity", punchoutRequest.getToIdentity());
		updateheaderElementValue(orderOutputDoc, "PunchOutOrderMessage", "BuyerCookie", punchoutRequest.getBuyerCookie());

		// For cXML, this will get added to form as one param, cxml-urlencoded
		cxmlData = WCIntegrationXMLUtils.AttachDocType(orderOutputDoc);
	}

	private void ociFormat(String outputData)
			throws ParserConfigurationException, SAXException, IOException {

		ociData = outputData;
	}

	private void updateOrderUoms(YFSEnvironment env, Document orderOutputDoc)
			throws RemoteException, YIFClientCreationException {
		NodeList nodeList = orderOutputDoc.getElementsByTagName("OrderLineTranQuantity");

		// replace UOM (MAX format) with EDI UOM
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element) nodeList.item(i);
			String maxUom = element.getAttribute("TransactionalUOM");
			String convertedBaseUom = convertMaxUomToEdi(env, maxUom);
			element.setAttribute("TransactionalUOM", convertedBaseUom);
		}
	}
	//TODO move this method to xpxutils or xpedxwcutils?
	public static String convertMaxUomToEdi(YFSEnvironment env, String maxUom)
			throws YFSException, RemoteException, YIFClientCreationException {
		String ediUom = maxUom;
		Document legacyUomOutputDoc = null;
		Document legacyUomInputDoc = YFCDocument.createDocument("XPEDXLegacyUomXref").getDocument();

		legacyUomInputDoc.getDocumentElement().setAttribute("LegacyType", "M");
		legacyUomInputDoc.getDocumentElement().setAttribute("LegacyUOM", maxUom);

		YIFApi api = YIFClientFactory.getInstance().getApi();
		legacyUomOutputDoc = api.executeFlow(env, "XPXGetLegacyUomXrefService", legacyUomInputDoc); //TODO return fewer fields by using mashup?

		if (legacyUomOutputDoc != null) {
			String mappedUom = SCXmlUtil.getXpathAttribute(legacyUomOutputDoc.getDocumentElement(),"/XPEDXLegacyUomXrefList/XPEDXLegacyUomXref/@UOM");

			if (mappedUom != null && mappedUom.trim().length() > 0) {
				ediUom = stripEnvFromUom(mappedUom); //TODO if strip M_ here or in caller, don't need to do in xslt
				LOG.info("Converted max UOM " +maxUom+ " to EDI UOM " + ediUom);
			}
			else {
				LOG.warn("convertMaxUomToEdi: UOM Doesn't exist in XPEDX_Legacy_Uom_Xref table: " + maxUom);
			}
		} else {
			LOG.error("convertMaxUomToEdi: Problem getting data from XPEDX_Legacy_Uom_Xref table for " + maxUom);
		}

		return ediUom;
	}

	private static String stripEnvFromUom(String mappedUom) {
		String ediUom;
		ediUom = mappedUom;
		if (mappedUom.contains("_")) {
			ediUom = mappedUom.split("_")[1];
		}
		return ediUom;
	}

	private void deleteCart(String deleteOrderHeaderKey) throws Exception {
		IWCContext context = WCContextHelper.getWCContext(ServletActionContext.getRequest());
		SCUIContext wSCUIContext = context.getSCUIContext();
		ISCUITransactionContext scuiTransactionContext = wSCUIContext.getTransactionContext(true);
		YFSEnvironment env = (YFSEnvironment) scuiTransactionContext
				.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);
		YIFApi api = YIFClientFactory.getInstance().getApi();

		Document deleteOrderInputDoc = YFCDocument.createDocument("Order").getDocument();
		deleteOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, deleteOrderHeaderKey);
		deleteOrderInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ACTION,"DELETE");
		api.invoke(env, "deleteOrder", deleteOrderInputDoc);

		//prepareAndInvokeMashup(DRAFT_ORDER_DELETE_MASHUP);
	}

	private Document getOrderDetails(String orderHeaderKey, YFSEnvironment env)
			throws YIFClientCreationException, RemoteException {

		Document inputOrderDoc = YFCDocument.createDocument("Order").getDocument();
		Element inputOrderElement = inputOrderDoc.getDocumentElement();
		inputOrderElement.setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, orderHeaderKey);

		Element orderElement = (Element)WCMashupHelper.invokeMashup(
				GET_PUNCHOUT_ORDER_DETAILS_MASHUP,inputOrderElement, wcContext.getSCUIContext());

		return orderElement.getOwnerDocument();
	}

	private void setReturnUrlfromRequest() throws Exception {
		if (punchoutRequest.getReturnURL() != null) {
			returnUrl = formatUrl(punchoutRequest.getReturnURL()).toString();
		}
	}

	private URL formatUrl(String punchoutUrl) throws Exception
	{
		String decodedURL;
		decodedURL = URLDecoder.decode(punchoutUrl, "UTF-8");
		URL url = new URL(decodedURL);
		URI uri = new URI(url.getProtocol(), url.getHost(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getQuery());
		return uri.toURL();
	}

	private String transformUsingXslt(Document inXML, String xsltFileName)
			throws Exception {
		InputStream xslStream = XPXPunchOutOrder.class.getResourceAsStream(
				new StringBuilder().append(PUNCHOUT_XSL_LOCATION).append(xsltFileName).toString());
		TransformerFactory tranFactory = TransformerFactory.newInstance();
		javax.xml.transform.URIResolver resolver = YFSSystem.getXSLURIResolver();
		if (resolver != null) {
			tranFactory.setURIResolver(resolver);
		}

		StreamSource source = new StreamSource(new CharArrayReader(SCXmlUtil
				.getString(inXML).toCharArray()));
		StreamResult result = new StreamResult(new StringWriter());
		Transformer transformer = tranFactory.newTransformer(new StreamSource(xslStream));
		transformer.transform(source, result);

		StringWriter out = (StringWriter) result.getWriter();
		return out.getBuffer().toString();
	}

	private Document updatetimeStampValue(Document doc) {
		NodeList cXML = doc.getElementsByTagName("cXML");

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SS-z");
		df.setTimeZone(TimeZone.getTimeZone("CST"));

		for (int i = 0; i < cXML.getLength(); i++) {
			Element timestampelement = (Element) cXML.item(i);
			timestampelement.setAttribute("timestamp", df.format(date).toString());
		}

		return doc;
	}

	private Document updateheaderElementValue(Document doc, String parentTag,
			String ChildTag, String headerValue) {
		NodeList headerParentTag = doc.getElementsByTagName(parentTag);

		for (int i = 0; i < headerParentTag.getLength(); i++) {
			try {
				Element childTagname = (Element) headerParentTag.item(i);
				Node name = childTagname.getElementsByTagName(ChildTag).item(0).getFirstChild();
				name.setTextContent(headerValue);
			} catch (Exception Ex) {
				LOG.info("Exception while updateheaderElementValue: "+ Ex.getLocalizedMessage());
			}
		}
		return doc;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public String getCxmlData() {
		return cxmlData;
	}

	public String getOciData() {
		return ociData;
	}

	public String getOrderHeaderKey() {
		return orderHeaderKey;
	}

	public void setOrderHeaderKey(String orderHeaderKey) {
		this.orderHeaderKey = orderHeaderKey;
	}

}
