package com.xpedx.nextgen.customermanagement.api;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.woodstock.util.frame.log.base.ISCILogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.common.util.XPXUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.log.YFCLogCategoryFactory;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

/**
 * @author Sunith Dodda
 * 
 * Used to get the Ship-To Customer list in user requested Template 
 * [Template should be formed according to getCustomerList API output format]
 * 
 */
public class XPXGetListOfShipTosAPI implements YIFCustomApi {

	private static YIFApi api = null;
	private static ISCILogger log = null;
	static {
		log = new YFCLogCategoryFactory().getLogger(XPXUtils.class.getCanonicalName());
	}
	
	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}
	/**
	 * Service: XPXGetListOfShipTosByTemplateService <br/> <b>Input XML format:<b>
	 * 
	 * <pre>
	 * &lt;Customer CustomerID='required' OrganizationCode='required'&gt;
	 * &lt;Template&gt;
	 * &lt;CustomerList&gt;
	 *    &lt;Customer CustomerKey='' CustomerID='' OrganizationCode=''&gt;
	 *       &lt;Extn ExtnSuffixType=''/&gt;
	 *       &lt;CustomerAdditionalAddressList TotalNumberOfRecords=''&gt;
	 *          &lt;CustomerAdditionalAddress&gt;
	 *             &lt;PersonInfo/&gt;
	 *          &lt;/CustomerAdditionalAddress&gt;
	 *       &lt;/CustomerAdditionalAddressList&gt;
	 *    &lt;/Customer&gt;
	 * &lt;/CustomerList&gt;
	 * &lt;Template&gt;
	 * &lt;/Customer&gt;
	 * </pre>
	 * 
	 * <br/> <b>Default output template:</b>
	 * 
	 * <pre>
	 * &lt;CustomerList&gt;
	 * &lt;Customer CustomerKey='' CustomerID='' OrganizationCode=''&gt;
	 * &lt;Extn ExtnSuffixType=''/&gt;
	 * &lt;/Customer&gt;
	 * &lt;/CustomerList&gt;
	 * </pre>
	 * 
	 * <i>Note:</i> <b>Template</b> element is optional, if provided it will be merged with the default output template of this API.
	 * 
	 * @param env
	 * @param docInput
	 * @return Document
	 * @throws Exception
	 */
	public Document getListOfShipTos(YFSEnvironment env, Document docInput) throws Exception{
		
		if (env == null) {
			// Throw new YFSException with the description
			throw new YFSException("YFSEnvironment cannot be null or invalid to XPXGetListOfShipTosAPI.getListOfShipTos()");
		}
		if (docInput == null) {
			// Throw new YFSException with the description
			throw new YFSException("Input XML document cannot be null or invalid to XPXGetListOfShipTosAPI.getListOfShipTos()");
		}
		if (SCUtil.isVoid(docInput.getDocumentElement().getAttribute("CustomerID")) || SCUtil.isVoid(docInput.getDocumentElement().getAttribute("OrganizationCode"))) {
			throw new YFSException("Invalid Input XPXGetListOfShipTosAPI.getListOfShipTos(): CustomerID and OrganizationCode are mandatory");
		}
		log.debug("getListOfShipTos() Input XML:" + SCXmlUtil.getString(docInput));
		api = YIFClientFactory.getInstance().getApi();
		Document docDefaultTemplate = this.mergeInputReqTemplateWithDefault(docInput);
		env.setApiTemplate("getCustomerList", docDefaultTemplate);
		docDefaultTemplate = null;
		Document docShipToCustomerList = SCXmlUtil.createDocument("CustomerList");
		Element eleShipToCustomerList = docShipToCustomerList.getDocumentElement();
		Set<String> setOfCustomers = new HashSet<String>();
		
		// Get the Queried Customer Info
		Document docQrydCustomerList = api.invoke(env, XPXLiterals.GET_CUSTOMER_LIST_API, docInput);
		Element eleQrydCustomerList = docQrydCustomerList.getDocumentElement();
		Element eleQrydCustomer = SCXmlUtil.getChildElement(eleQrydCustomerList, XPXLiterals.E_CUSTOMER);
		if(!YFCCommon.isVoid(eleQrydCustomer)){
			if(this.isAShipToCustomer(eleQrydCustomer)){
				SCXmlUtil.importElement(eleShipToCustomerList, eleQrydCustomer);
			} else {
				setOfCustomers.add(eleQrydCustomer.getAttribute(XPXLiterals.A_CUSTOMER_KEY));
				while(setOfCustomers.size()>0){
					
					// 1. prepare getCustomerList API input
					Document docQryCustomers = SCXmlUtil.createDocument(XPXLiterals.E_CUSTOMER);
					Element eleQryCustomers = docQryCustomers.getDocumentElement();
					Element eleComplexQuery = SCXmlUtil.createChild(eleQryCustomers, XPXLiterals.E_COMPLEX_QUERY);
					Element eleOr = SCXmlUtil.createChild(eleComplexQuery, XPXLiterals.E_Or);
					for (String qryCustomerKey : setOfCustomers) {
						Element eleExp = SCXmlUtil.createChild(eleOr, XPXLiterals.E_EXP);
						eleExp.setAttribute(XPXLiterals.A_NAME, XPXLiterals.A_PARENT_CUSTOMER_KEY);
						eleExp.setAttribute(XPXLiterals.A_VALUE, qryCustomerKey);
					}
					
					// 2. invoke getCustomerList API
					docQrydCustomerList = api.invoke(env, XPXLiterals.GET_CUSTOMER_LIST_API, docQryCustomers);

					// 3. [mungeOutput(docQrydCustomerList)]munge Output, update eleShipToCustomerList in case of Ship-To(s) 
					//	is/are present in o/p and re-update mapOfCustomers in case of not a ship-to
					setOfCustomers = this.mungeOutput(docQrydCustomerList, docShipToCustomerList);
				}
			}
		}
		
		log.debug("getListOfShipTos() Output XML:" + SCXmlUtil.getString(docShipToCustomerList));
		return docShipToCustomerList;
		
	}

	private Document mergeInputReqTemplateWithDefault(Document docInput) {
		String strDefaultTemplate = new StringBuffer().append("<CustomerList TotalNumberOfRecords=''>")
		.append("<Customer CustomerKey='' CustomerID='' OrganizationCode=''>")
		.append("<Extn ExtnSuffixType=''/>")
//		.append("<CustomerAdditionalAddressList TotalNumberOfRecords=''><CustomerAdditionalAddress><PersonInfo/></CustomerAdditionalAddress></CustomerAdditionalAddressList>")
		.append("</Customer></CustomerList>").toString();
		Document docDefaultTemplate = SCXmlUtil.createFromString(strDefaultTemplate);
		Element eleInputReqTemplate = SCXmlUtil.getChildElement(docInput.getDocumentElement(), "Template");
		if(null != eleInputReqTemplate){
			Element eleCustomerList = SCXmlUtil.getChildElement(eleInputReqTemplate, "CustomerList");
			if(null != eleCustomerList){
				SCXmlUtil.mergeElement(eleCustomerList, docDefaultTemplate.getDocumentElement(), true);
			}
			//Remove the Template element from the input.
			SCXmlUtil.removeNode(eleInputReqTemplate);
		}
		return docDefaultTemplate;
	}

	private boolean isAShipToCustomer(Element eleQrydCustomer) {
		Element eleExtn = SCXmlUtil.getChildElement(eleQrydCustomer, XPXLiterals.E_EXTN);
		String strSuffixType = null;
		if(!YFCCommon.isVoid(eleExtn))
			strSuffixType = eleExtn.getAttribute(XPXLiterals.A_EXTN_SUFFIX_TYPE);
		
		if(!YFCCommon.isVoid(strSuffixType)){
			return XPXLiterals.CHAR_S.equals(strSuffixType);
		}
		return false;
	}

	private Set<String> mungeOutput(Document docQrydCustomerList, Document docShipToCustomerList) {
		Set<String> setOfCustomers = new HashSet<String>();
		List<Element> listCustomers = SCXmlUtil.getChildrenList(docQrydCustomerList.getDocumentElement());
		for (Element eleQrydCustomer : listCustomers) {
			if(this.isAShipToCustomer(eleQrydCustomer)){
				SCXmlUtil.importElement(docShipToCustomerList.getDocumentElement(), eleQrydCustomer);
			} else {
				setOfCustomers.add(eleQrydCustomer.getAttribute(XPXLiterals.A_CUSTOMER_KEY));
			}
		}
		
		// LOOP each Customer from docQrydCustomerList[eleQrydCustomer]
		//		IF eleQrydCustomer is a Ship-to
		//			THEN Import eleQrydCustomer and append it to CustomerList element of docShipToCustomerList
		//		else //it means it is not a Ship-to, it is a MSAP/SAP/Bill-to
		//			THEN Update setOfCustomers<Set>
		// LOOP END
		// return setOfCustomers
		return setOfCustomers;
	}

}
