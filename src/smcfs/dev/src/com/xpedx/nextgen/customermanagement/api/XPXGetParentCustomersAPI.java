package com.xpedx.nextgen.customermanagement.api;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXGetParentCustomersAPI implements YIFCustomApi {

	/** API object. */
	private static YIFApi api = null;


	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * Takes the Customer ID and gets the list of parent Customer ID(s) till Root Customer including itself. If input customer id is 'ShipToID', the customer hierarchy displayed in
	 * the list will be in following format: <br>
	 * ShipToID,Parent of ShipToID[say BillToID], Parent of BillToID[say SAPID], Parent of SAPID[say MSAPID]. <br>
	 * In case of any modifications to this API, the above Sequence/Hierarchy should not be altered. <br>
	 * Input XML format:
	 * 
	 * <pre>
	 * &lt;Customer CustomerID=&quot;&quot; OrganizationCode=&quot;&quot; /&gt;
	 * </pre>
	 * 
	 * Output XML format:
	 * 
	 * <pre>
	 * &lt;CustomerList&gt;
	 * 	&lt;Customer CustomerID=&quot;&quot; CustomerKey=&quot;&quot; OrganizationCode=&quot;&quot; OrganizationName=&quot;&quot; /&gt;
	 * &lt;/CustomerList&gt;
	 * </pre>
	 * 
	 * @param env
	 * @param inXML
	 * @return
	 * @throws Exception
	 */
	public Document invokeGetParentCustomerList(YFSEnvironment env, Document inXML)
			throws Exception {
		api = YIFClientFactory.getInstance().getApi();
		// Unified customers includes Parent Customer ID(s) and Requested
		// Customer ID
		Document docUnifiedCustomers = SCXmlUtil.createDocument("CustomerList");

		Element eleCustomer = null;

		String strOrganizationCode = inXML.getDocumentElement().getAttribute("OrganizationCode");
		String strCustomerID = inXML.getDocumentElement().getAttribute("CustomerID");
		String strParentCustomerID = "";
		String strCustomerKey = "";
		String strParentCustomerKey = "";
		String strRootCustomerKey = "";

		String template = "<CustomerList TotalNumberOfRecords=''>" +
				"<Customer CustomerKey='' CustomerID='' OrganizationCode='' ParentCustomerKey='' RootCustomerKey=''>" +
				"<BuyerOrganization OrganizationName=''/>" +
				"<ParentCustomer CustomerID='' OrganizationCode='' CustomerKey=''>" +
				"<BuyerOrganization OrganizationName=''/>" +
				"</ParentCustomer>" +
				"</Customer></CustomerList>";

		// create and set output template for the customer list API.
		env.setApiTemplate("getCustomerList", SCXmlUtil.createFromString(template));
		template = null;

		while (!YFCCommon.isVoid(strCustomerID)) {

			Document outputCustomerList = null;

			// Prepare Input XML
			YFCDocument inputCustomerDocumentQ1 = YFCDocument.createDocument("Customer");
			YFCElement inputCustomerElementQ1 = inputCustomerDocumentQ1.getDocumentElement();
			inputCustomerElementQ1.setAttribute("CustomerID", strCustomerID);
			inputCustomerElementQ1.setAttribute("OrganizationCode", strOrganizationCode);
			strCustomerID = "";

			// Invoke getCustomerList API
			outputCustomerList = api.invoke(env, "getCustomerList", inputCustomerDocumentQ1.getDocument());

			NodeList nlCustomer = outputCustomerList.getElementsByTagName("Customer");
			// Check if the customer exists.
			if (nlCustomer != null && nlCustomer.getLength() > 0) {
				eleCustomer = (Element) nlCustomer.item(0);
				strRootCustomerKey = SCXmlUtil.getAttribute(eleCustomer, "RootCustomerKey");
				strParentCustomerKey = SCXmlUtil.getAttribute(eleCustomer, "ParentCustomerKey");
				strCustomerKey = SCXmlUtil.getAttribute(eleCustomer, "CustomerKey");

				// Update the unified customer list with the customer id in the loop.
				this.updateParentCustomerList(docUnifiedCustomers, eleCustomer, SCXmlUtil.getAttribute(eleCustomer, "CustomerID"));
				strParentCustomerID = SCXmlUtil.getXpathAttribute(eleCustomer, "./ParentCustomer/@CustomerID");

				/*
				 * Check if the Customer Key and its Root Customer Key are same,
				 * if yes it indicates that this customer itself is the root
				 * customer. else it indicates it has at least one more parent
				 * customer.
				 */
				if (!strRootCustomerKey.equals(strCustomerKey)) {

					/*
					 * Check if this customer's Parent Customer Key and Root
					 * Customer Key are same, if yes it indicates that the
					 * parent of this customer itself is root customer else it
					 * indicates that its parent has at least one more parent
					 * customer.
					 */
					if (!strRootCustomerKey.equals(strParentCustomerKey)) {
						// STAMP the parent customer ID into customer id var to
						// proceed fetching its childs iteratively.
						strCustomerID = strParentCustomerID;
					} else {
						// Update the unified customer list with the parent
						// customer id.
						eleCustomer = SCXmlUtil.getChildElement(eleCustomer, "ParentCustomer");
						this.updateParentCustomerList(docUnifiedCustomers, eleCustomer, strParentCustomerID);
					}
				}
			}
		}

		// Create the template set in environment.
		env.clearApiTemplate("getCustomerList");

		return docUnifiedCustomers;
	}

	private void updateParentCustomerList(Document docParentCustomers,
			Element eleO1Cust, String strCustomerID) {
		Element eleCustomers;
		Element eleO1CustBuyerOrg = SCXmlUtil.getChildElement(eleO1Cust, "BuyerOrganization");
		eleCustomers = docParentCustomers.createElement("Customer");
		eleCustomers.setAttribute("CustomerID", strCustomerID);
		eleCustomers.setAttribute("CustomerKey", eleO1Cust.getAttribute("CustomerKey"));
		eleCustomers.setAttribute("OrganizationCode", eleO1Cust.getAttribute("OrganizationCode"));
		if(eleO1CustBuyerOrg!=null)
			eleCustomers.setAttribute("OrganizationName", SCXmlUtil.getAttribute(eleO1CustBuyerOrg, "OrganizationName"));
		
		docParentCustomers.getDocumentElement().appendChild(eleCustomers);
		eleCustomers = null;
	}
}
