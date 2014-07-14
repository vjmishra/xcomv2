package com.xpedx.nextgen.customermanagement.api;

import java.rmi.RemoteException;
import java.util.Properties;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXSendBuyerAddedAPI implements YIFCustomApi {

	private static YIFApi api = null;
	public static final String getCustomerContactListTemplate = "global/template/api/getCustomerContactList.XPXUserUpdateEmail.xml";

	/** Added by Arun Sekhar on 26-April-2011 **/
	private static YFCLogCategory yfcLogCatalog;

	static {
		yfcLogCatalog = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	}

	/***************************************************************/
	/**
	 * 
	 * @param env
	 * @param inXML
	 * @return
	 * @throws Exception
	 */
	public Document sendBuyerAdded(YFSEnvironment env, Document inXML)
			throws Exception {
		if(yfcLogCatalog.isDebugEnabled()){
		yfcLogCatalog.debug("entering sendBuyerAdded :: ");
		yfcLogCatalog.debug(" inXML  :: " + SCXmlUtil.getString(inXML));
		}
		Element UserUpdateEmail = (Element) inXML.getElementsByTagName(
				"UserUpdateEmail").item(0);

		String strCustomerContactID = UserUpdateEmail
				.getAttribute("CustomerContactID");
		if(yfcLogCatalog.isDebugEnabled()){
		yfcLogCatalog.debug(" strCustomerContactID :: " + strCustomerContactID);
		}
		Document getCCListOutDoc = getCCList(env, strCustomerContactID);
		if(yfcLogCatalog.isDebugEnabled()){
		yfcLogCatalog.debug(" getCCListOutDoc  :: "
				+ SCXmlUtil.getString(getCCListOutDoc));
		}
		Element hdrCustomerContactElem = (Element) getCCListOutDoc
				.getElementsByTagName("CustomerContact").item(0);

		Element elemCustomerContactList = (Element) getCCListOutDoc
				.getElementsByTagName("CustomerContactList").item(0);
		NodeList nlCustomerContact = elemCustomerContactList
				.getElementsByTagName("CustomerContact");

		int nlCustomerContactLen = nlCustomerContact.getLength();
		if(yfcLogCatalog.isDebugEnabled()){
		yfcLogCatalog.debug(" nlCustomerContactLen :: " + nlCustomerContactLen);
		}
		// attributes to be stamped on inXML
		String strAccountNumber = "";
		String strFirstName = "";
		String strLastName = "";
		String strcEmailAddress = "";
		String strToEmailAddress = "";
		String strBrandName = "";

		strAccountNumber = strCustomerContactID;
		strFirstName = hdrCustomerContactElem.getAttribute("FirstName");
		strLastName = hdrCustomerContactElem.getAttribute("LastName");
		strcEmailAddress = hdrCustomerContactElem.getAttribute("EmailID");
		strBrandName = UserUpdateEmail.getAttribute("BrandName");

		Element tempCustomerContactElem = null;
		NodeList nlUserGroupList = null;
		int nlUserGroupListLen = 0;
		Element tempUserGroupList = null;
		String strUsergroupKey = null;
		String BUYER_ADMIN = "BUYER-ADMIN";
		String bAdminEmailAdr = "";

		for (int i = 0; i < nlCustomerContactLen; i++) {
			tempCustomerContactElem = (Element) nlCustomerContact.item(i);

			nlUserGroupList = tempCustomerContactElem
					.getElementsByTagName("UserGroupList");
			nlUserGroupListLen = nlUserGroupList.getLength();

			for (int j = 0; j < nlUserGroupListLen; j++) {
				tempUserGroupList = (Element) nlUserGroupList.item(j);
				if(yfcLogCatalog.isDebugEnabled()){
				yfcLogCatalog.debug(" tempUserGroupList :: "
						+ SCXmlUtil.getString(tempUserGroupList));
				}
				strUsergroupKey = tempUserGroupList
						.getAttribute("UsergroupKey");

				if (strUsergroupKey.equalsIgnoreCase(BUYER_ADMIN)) {
					bAdminEmailAdr = tempCustomerContactElem
							.getAttribute("EmailID");
					if(yfcLogCatalog.isDebugEnabled()){
					yfcLogCatalog.debug(" bAdminEmailAdr :: " + bAdminEmailAdr);
					}
					// update strcEmailAddress

					if (strToEmailAddress.isEmpty()) {

						strToEmailAddress = bAdminEmailAdr;
					} else {
						strToEmailAddress = strToEmailAddress + ","
								+ bAdminEmailAdr;
					}

				}

			}

		}

		
		// SCXmlUtil.getString(getCCListOutDoc));
		if(yfcLogCatalog.isDebugEnabled()){
		yfcLogCatalog.debug(" strcEmailAddress :: " + strcEmailAddress);
		yfcLogCatalog.debug(" strAccountNumber :: " + strAccountNumber);
		yfcLogCatalog.debug(" strFirstName :: " + strFirstName);
		yfcLogCatalog.debug(" strLastName :: " + strLastName);
		yfcLogCatalog.debug(" strToEmailAddress :: " + strToEmailAddress);
		}
		if (strBrandName.isEmpty()) {
			UserUpdateEmail.setAttribute("BrandName", "xpedx");
		}

		// required output
		/**
		 * 
		 <UserUpdateEmail BrandName="XPEDX" EntryType="BuyerAdded"
		 * AccountNumber="123" FirstName="Vijay" LastName="Kumar"
		 * cEmailAddress="vijay2veda@gmail.com" />
		 * 
		 * missing attributes AccountNumber="123" FirstName="Vijay"
		 * LastName="Kumar" cEmailAddress="vijay2veda@gmail.com"
		 */

		UserUpdateEmail.setAttribute("AccountNumber", strAccountNumber);
		UserUpdateEmail.setAttribute("FirstName", strFirstName);
		UserUpdateEmail.setAttribute("LastName", strLastName);
		UserUpdateEmail.setAttribute("cEmailAddress", strcEmailAddress);
		UserUpdateEmail.setAttribute("strToEmailAddress", strToEmailAddress);

		/******* Added by Arun Sekhar on 14-April-2011 *******/
		String brandName = UserUpdateEmail.getAttribute("BrandName");
		String sellerOrgCode = "";
		if ("xpedx".equalsIgnoreCase(brandName)) {
			sellerOrgCode = "xpedx";
		} else {
			sellerOrgCode = XPXUtils.getSellerOrganizationCode(env, brandName);
		}

		inXML.getDocumentElement().setAttribute("SellerOrganizationCode",
				sellerOrgCode);
		if(yfcLogCatalog.isDebugEnabled()){
		yfcLogCatalog.debug("inXML after stamping SellerOrganizationCode: "
				+ SCXmlUtil.getString(inXML));
		}
		XPXUtils utilObj = new XPXUtils();
		inXML = utilObj.stampBrandLogo(env, inXML);
		if(yfcLogCatalog.isDebugEnabled()){
		yfcLogCatalog.debug("inXML after stamping BrandLogo: "
				+ SCXmlUtil.getString(inXML));
		}
		/*****************************************************/

		return inXML;

	}

	/**
	 * 
	 * @param env
	 * @param strCustomerContactID
	 * @return
	 * @throws FactoryConfigurationError
	 * @throws ParserConfigurationException
	 * @throws YIFClientCreationException
	 * @throws RemoteException
	 * @throws YFSException
	 */
	private Document getCCList(YFSEnvironment env, String strCustomerContactID)
			throws ParserConfigurationException, FactoryConfigurationError,
			YIFClientCreationException, YFSException, RemoteException {

		// prepare input xml for getCustomerContactList api

		// <CustomerContact CustomerContactID="BillToB2BUser"/>

		Document getCCListInDoc = SCXmlUtil.getDocumentBuilder().newDocument();
		Element elemCustomerContact = getCCListInDoc
				.createElement("CustomerContact");
		elemCustomerContact.setAttribute("CustomerContactID",
				strCustomerContactID);
		getCCListInDoc.appendChild(elemCustomerContact);
		if(yfcLogCatalog.isDebugEnabled()){
		yfcLogCatalog.debug(" getCCListInDoc :: "
				+ SCXmlUtil.getString(getCCListInDoc));
		}
		env.setApiTemplate("getCustomerContactList",
				getCustomerContactListTemplate);

		api = YIFClientFactory.getInstance().getApi();
		Document getCCListOutDoc = api.invoke(env, "getCustomerContactList",
				getCCListInDoc);
		env.clearApiTemplate("getCustomerContactList");

		return getCCListOutDoc;
	}

	/**
	 * 
	 */
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}

}
