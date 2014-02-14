package com.sterlingcommerce.xpedx.webchannel.punchout.order;

import java.io.CharArrayReader;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Timestamp;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.framework.extensions.ISCUITransactionContext;
import com.sterlingcommerce.ui.web.platform.transaction.SCUITransactionContextFactory;
import com.sterlingcommerce.webchannel.common.eprocurement.AribaContextImpl;
import com.sterlingcommerce.webchannel.common.eprocurement.IAribaContext;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.core.context.WCContextHelper;
import com.sterlingcommerce.webchannel.utilities.SWCProperties;
import com.sterlingcommerce.webchannel.utilities.WCIntegrationXMLUtils;
import com.sterlingcommerce.webchannel.utilities.WCUtils;
import com.sterlingcommerce.xpedx.webchannel.common.eprocurement.XPEDXCXMLMessageFields;
import com.sterlingcommerce.xpedx.webchannel.punchout.PunchoutRequest;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.common.util.XPXTranslationUtilsAPI;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;

/**
 * This class is the main action handler for dealing with punch-out (i.e. return
 * to) an e-procurement system. The initial version only supports Ariba
 * e-procurement systems.
 */
public class CustomPunchoutOrderAction extends WCMashupAction {
	public static final String MODE_SAVE = "save";
	public static final String MODE_CANCEL = "cancel";

	public static final String DRAFT_ORDER_DELETE_MASHUP = "draftOrderDelete";
	public static final String GET_SOURCE_ORDER_FOR_ARIBA_PUNCHOUT_MASHUP = "getSourceOrderForAribaPunchOut";
	public static final String GET_PUNCH_OUT_ORDER_MESSAGE_CXML_MASHUP = "XPEDXGetPunchOutOrderMessageCXML";
	private static final String customerExtnInfoMashUp = "xpedx-customer-getCustomExtnInformation";

	public static final String USER_AGENT_PROPERTY_KEY = "swc.ariba.userAgent";

	public String mashUpId = null;

	protected Element sourceOrderForAribaPunchout = null;
	protected String msap = null;
	XPEDXCXMLMessageFields cXMLFields = null;
	private PunchoutRequest punchoutRequest = null;

	private static final Logger LOG = Logger
			.getLogger(XPEDXProcurementPunchOutAction.class);

	public String execute() {
		String toReturn = SUCCESS;
		try {

			punchoutRequest = (PunchoutRequest) XPEDXWCUtils.getObjectFromCache("PunchoutRequest");
			
			AribaContextImpl aribaContext = AribaContextImpl.getInstance();

			if (punchoutRequest == null) throw new Exception("No procurement punch in context information found.");
			
			String cicOrderHeaderKey = (String) XPEDXWCUtils.getObjectFromCache("OrderHeaderInContext");

			String cxml = populatePunchOutOrderMessage(cicOrderHeaderKey, aribaContext);
			
			request.setAttribute("requestUrl", punchoutRequest.getReturnURL());
			
			request.setAttribute("cxml", cxml);

			deleteCart(aribaContext.getOrderHeaderKey());

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			WCUtils.setErrorInContext(getWCContext(), e);

			return ERROR;
		}

		return toReturn;
	}

	/**
	 * Deletes the order matching the given deleteOrderHeaderKey.
	 * 
	 * @param deleteOrderHeaderKey
	 *            the order header key of the order to delete.
	 * @throws Exception
	 */
	private void deleteCart(String deleteOrderHeaderKey) throws Exception {
		prepareAndInvokeMashup(DRAFT_ORDER_DELETE_MASHUP);
	}

	/*
	 * 
	 */
	private String populatePunchOutOrderMessage(String orderHeaderKey,
			IAribaContext aribaContext) throws Exception {

		if (orderHeaderKey != null) {
			sourceOrderForAribaPunchout = prepareAndInvokeMashup(GET_SOURCE_ORDER_FOR_ARIBA_PUNCHOUT_MASHUP);
		}

		String userAgent = SWCProperties.getProperty(USER_AGENT_PROPERTY_KEY);

		IWCContext context = WCContextHelper.getWCContext(ServletActionContext
				.getRequest());
		SCUIContext wSCUIContext = context.getSCUIContext();
		ISCUITransactionContext scuiTransactionContext = wSCUIContext
				.getTransactionContext(true);
		YFSEnvironment env = (YFSEnvironment) scuiTransactionContext
				.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);

		String customerID = context.getCustomerId();
		String storeFrontId = context.getStorefrontId();
		msap = XPXTranslationUtilsAPI.getMsap(env, customerID, storeFrontId);

		Document CustDetails = XPEDXWCUtils.getCustomerDetails(msap, storeFrontId);
		String xsltVersion = SCXmlUtil.getXpathAttribute(CustDetails.getDocumentElement(),"/Customer/Extn/@ExtnXSLTVer");
		String xsltFileName = SCXmlUtil.getXpathAttribute(CustDetails.getDocumentElement(), "/Customer/Extn/@ExtnXSLTFileName");
		String replaceChars = SCXmlUtil.getXpathAttribute( CustDetails.getDocumentElement(), "/Customer/Extn/@ExtnReplaceCharacter");

		YIFApi api = YIFClientFactory.getInstance().getApi();

		Document inputOrderDoc = YFCDocument.createDocument("Order").getDocument();

		Element inputOrderElement = inputOrderDoc.getDocumentElement();
		inputOrderElement.setAttribute(XPXLiterals.A_ORDER_HEADER_KEY, orderHeaderKey);

		Document orderOutputTemplate = SCXmlUtil.createFromString(" <Order OrderHeaderKey='' OrderNo='' OrderName='' CustomerPONo='' EntryType='' EnterpriseCode='' "
						+ " OptimizationType='' CarrierServiceCode='' Modifyts='' Createts='' "
						+ " DraftOrderFlag='' HasPendingChanges='' AuthorizedClient='' "
						+ " BuyerOrganizationCode='' ReqDeliveryDate='' OrderDate='' OrderType='' ShipToID='' Createuserid='' Modifyuserid=''> "
						+ " <PriceInfo Currency='' /> "
						+ "	<Extn ExtnDeliveryHoldTime='' ExtnDeliveryHoldDate='' ExtnOrderDivision='' ExtnGenerationNo='' "
						+ "       ExtnDeliveryHoldFlag='' ExtnRushOrderComments='' ExtnShipComplete='' "
						+ "       ExtnWillCall='' ExtnAttentionName='' ExtnHeaderComments='' ExtnCustomerNo='' ExtnBillToSuffix='' "
						+ "       ExtnAddnlEmailAddr='' ExtnCustomerDivision='' ExtnOrderedByName=''  "
						+ "       ExtnSourceType='' ExtnWebConfNum='' ExtnLegacyOrderNo='' ExtnBillToCustomerID='' "
						+ "       ExtnRushOrderFlag='' ExtnWebHoldFlag='' ExtnOrderSubTotal='' ExtnOrderCouponDiscount='' "
						+ "       ExtnOrderDiscount='' ExtnTotOrderAdjustments='' ExtnTotalOrderValue='' ExtnTotOrdValWithoutTaxes='' ExtnLegTotOrderAdjustments=''> "
						+ "	</Extn>	 "
						+ "   <OrderLines TotalNumberOfRecords=''> "
						+ "       <OrderLine OrderedQty='' ItemGroupCode='' "
						+ "           ConfigurationKey='' DeliveryMethod='' OrderLineKey='' KitQty='' "
						+ "           MaxLineStatus='' CarrierServiceCode='' ReqShipDate='' "
						+ "           IsBundleParent='' OpenQty='' Status='' PrimeLineNo='' SubLineNo='' "
						+ "           CustomerPONo='' CustomerLinePONo='' LineType=''> "
						+ "           <Item ItemID='' ItemShortDesc='' ItemDesc='' UnitOfMeasure='' /> "
						+ "           <LineOverallTotals UnitPrice='' "
						+ "               DisplayUnitPrice='' KitUnitPrice='' LineAdjustments='' "
						+ "               DisplayLineAdjustments='' LineTotalWithoutTaxes='' "
						+ "               DisplayLineTotalWithoutTaxes='' />       "
						+ "           <ItemDetails ItemID=''><Extn ExtnUNSPSC=''></Extn></ItemDetails>"
						+ "        </OrderLine> "
						+ "   </OrderLines> "
						+ " </Order> ");

		env.setApiTemplate("getCompleteOrderDetails", orderOutputTemplate);

		Document orderOutputDoc = api.invoke(env, "getCompleteOrderDetails", inputOrderDoc);

		String xmlString = invokePunchOut(orderOutputDoc, xsltFileName);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		orderOutputDoc = builder.parse(new InputSource(new StringReader(xmlString)));
		updatetimeStampValue(orderOutputDoc);
		updateheaderElementValue(orderOutputDoc, "From", "Identity", punchoutRequest.getFromIdentity());
		updateheaderElementValue(orderOutputDoc, "To", "Identity", punchoutRequest.getToIdentity());
		updateheaderElementValue(orderOutputDoc, "Sender", "Identity", punchoutRequest.getToIdentity());
		updateheaderElementValue(orderOutputDoc, "PunchOutOrderMessage", "BuyerCookie", punchoutRequest.getBuyerCookie());
		updateheaderElementValue(orderOutputDoc, "BrowserFormPost", "URL", punchoutRequest.getBuyerCookie());

		return WCIntegrationXMLUtils.AttachDocType(orderOutputDoc);
	}

	private String invokePunchOut(Document inXML, String xsltFileName)
			throws Exception {
		File xslStream = new File(
				(new StringBuilder()
						.append("D:\\GitWorkspace\\xcomv2\\src\\smcfs\\dev\\Foundation\\extensions\\global\\template\\xsl\\punchout\\")
						.append(xsltFileName).toString()));
		// XSL Conversion Starts here
		TransformerFactory tranFactory = TransformerFactory.newInstance();
		javax.xml.transform.URIResolver resolver = YFSSystem
				.getXSLURIResolver();
		if (resolver != null) {
			tranFactory.setURIResolver(resolver);
		}

		Transformer transformer = tranFactory.newTransformer(new StreamSource(
				xslStream));
		StringWriter stw = new StringWriter();
		StreamResult result = new StreamResult(stw);
		StreamSource source = new StreamSource(new CharArrayReader(SCXmlUtil
				.getString(inXML).toCharArray()));
		transformer.transform(source, result);
		StringWriter out = (StringWriter) result.getWriter();
		StringBuffer sb = out.getBuffer();
		String finalstring = sb.toString();
		return finalstring;

	}

	private Document updatetimeStampValue(Document doc) {
		java.util.Date date = new java.util.Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		NodeList cXML = doc.getElementsByTagName("cXML");
		Element timestampelement = null;
		for (int i = 0; i < cXML.getLength(); i++) {
			timestampelement = (Element) cXML.item(i);
			timestampelement.setAttribute("timestamp", timestamp.toString());
		}

		return doc;
	}

	private Document updateheaderElementValue(Document doc, String parentTag,
			String ChildTag, String headerValue) {
		NodeList headerParentTag = doc.getElementsByTagName(parentTag);
		Element childTagname = null;
		// loop for each employee
		for (int i = 0; i < headerParentTag.getLength(); i++) {
			try {
				childTagname = (Element) headerParentTag.item(i);
				Node name = childTagname.getElementsByTagName(ChildTag).item(0)
						.getFirstChild();
				name.setTextContent(headerValue);
			} catch (Exception Ex) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Exception while updateheaderElementValue: "
							+ Ex.getLocalizedMessage());
				}
			}
		}
		return doc;
	}

}
