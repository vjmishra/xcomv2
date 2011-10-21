package com.xpedx.nextgen.myitems.api;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

/**
 * 
 * @author Administrator identifies customerpath and division id and itemid from
 *         input xml <XPEDXMyItemsList BillToCustomerID=""
 *         CustomerID="30-0000145087-000-N3-12-SAP" DivisionID=""
 *         LegacyProductCode="MII@ItemId"
 *         MasterCustomerID="30-0000145087-000-N3-12-MSAP" ShipToCustomerID=""/>
 *         and obtains the myitemlist by invoking service
 *         "getXPEDX_MyItemsList_List" and returns the output to calling service
 */

public class XPXGetListOfMyItemsListsForUIAPI implements YIFCustomApi {

	private static YFCLogCategory log;
	private static YIFApi api = null;

	static {
		log = YFCLogCategory.instance(XPXGetListOfMyItemsListsForUIAPI.class);
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e1) {

			e1.printStackTrace();
		}
	}

	/**
	 * 
	 * @param env
	 * @param inputXML
	 *            CustomerPath =
	 *            MasterCustomerID|CustomerID|BillToCustomerID|ShipToCustomerID
	 *            DivisionID=<Div1>,<Div2>,...<DivN> ItemId=LegacyProductCode
	 * @return
	 * @throws FactoryConfigurationError
	 * @throws ParserConfigurationException
	 * @throws RemoteException
	 * @throws YFSException
	 */
	public Document getListOfMyItemsListsForUIAPI(YFSEnvironment env,
			Document inputXML) throws ParserConfigurationException,
			FactoryConfigurationError, YFSException, RemoteException {
		System.out
				.println("XPXGetListOfMyItemsListsForUIAPI :: getListOfMyItemsListsForUIAPI ");
		System.out.println("inputXML ::" + SCXmlUtil.getString(inputXML));

		Document getXPEDX_MyItemsList_ListInputXML = prepareXPEDX_MyItemsList_ListInputXML(inputXML);
		Element elemXPEDXMyItemsList = getXPEDX_MyItemsList_ListInputXML
				.getDocumentElement();
		// isMultiApi
		String strInvokeMultiAPI = elemXPEDXMyItemsList
				.getAttribute("isMultiApi");
		Document outDoc = null;

		if (strInvokeMultiAPI.equalsIgnoreCase("Y")) {
			System.out
					.println("invoke multiapi and format output to equivalent output of getXPEDX_MyItemsList_List service ");
			Element elemMultiApi = (Element) elemXPEDXMyItemsList
					.getElementsByTagName("MultiApi").item(0);
			String strMultiAPI = "multiApi";
			System.out.println("invoke api " + strMultiAPI);
			Document docMultiApi = SCXmlUtil.getDocumentBuilder().newDocument();
			Node tempNode = docMultiApi.importNode(elemMultiApi, true);
			docMultiApi.appendChild(tempNode);
			System.out.println("input document  "
					+ SCXmlUtil.getString(docMultiApi));
			// outDoc = api.invoke(env, strMultiAPI, docMultiApi);

			Document docMultiAPIOutdoc = api.invoke(env, strMultiAPI,
					docMultiApi);
			System.out.println("calling getFormattedOutDoc ::");
			outDoc = getFormattedOutDoc(docMultiAPIOutdoc);

		} else {
			String strgetXPEDX_MyItemsList_List = "getXPEDX_MyItemsList_List";
			System.out.println("invoke  service : "
					+ strgetXPEDX_MyItemsList_List);
			System.out.println("input xml : "
					+ SCXmlUtil.getString(getXPEDX_MyItemsList_ListInputXML));

			outDoc = api.executeFlow(env, strgetXPEDX_MyItemsList_List,
					getXPEDX_MyItemsList_ListInputXML);

		}

		System.out.println("outDoc abcd :: " + SCXmlUtil.getString(outDoc));

		System.out.println("getXPEDX_MyItemsList_ListInputXML ::"
				+ SCXmlUtil.getString(getXPEDX_MyItemsList_ListInputXML));

		return inputXML;
	}

	/**
	 * 
	 * @param docDuplicateRecords
	 * @return docUniqueRecords
	 * @throws FactoryConfigurationError
	 * @throws ParserConfigurationException
	 */
	private Document getFormattedOutDoc(Document docDuplicateRecords)
			throws ParserConfigurationException, FactoryConfigurationError {

		System.out.println("entering getFormattedOutDoc ::");

		Document docUniqueRecords = SCXmlUtil.getDocumentBuilder()
				.newDocument();

		Element XPEDXMyItemsListList = docUniqueRecords
				.createElement("XPEDXMyItemsListList");
		docUniqueRecords.appendChild(XPEDXMyItemsListList);

		NodeList nlXPEDXMyItemsList = docDuplicateRecords
				.getElementsByTagName("XPEDXMyItemsList");

		int nlXPEDXMyItemsListLen = nlXPEDXMyItemsList.getLength();

		System.out
				.println("nlXPEDXMyItemsList length " + nlXPEDXMyItemsListLen);
		Element tempNode = null;
		HashMap hmXPEDXMyItemsList = new HashMap();
		String strMyItemsListKey = null;

		for (int i = 0; i < nlXPEDXMyItemsListLen; i++) {
			tempNode = (Element) nlXPEDXMyItemsList.item(i);
			strMyItemsListKey = tempNode.getAttribute("MyItemsListKey");

			if (hmXPEDXMyItemsList.containsKey(strMyItemsListKey)) {
				System.out.println("duplicate entry ::" + strMyItemsListKey);
			} else {
				hmXPEDXMyItemsList.put(strMyItemsListKey, tempNode);
				System.out.println("inserting ::" + strMyItemsListKey);
				tempNode = (Element) docUniqueRecords
						.importNode(tempNode, true);
				XPEDXMyItemsListList.appendChild(tempNode);

			}

		}
		System.out.println(" returning docUniqueRecords "
				+ SCXmlUtil.getString(docUniqueRecords));

		return docUniqueRecords;
	}

	/**
	 * 
	 * @param inputXML
	 * @return
	 * @throws ParserConfigurationException
	 * @throws FactoryConfigurationError
	 *             if CustomerPath is null prepare input for DivisionID
	 */

	private Document prepareXPEDX_MyItemsList_ListInputXML(Document inputXML)
			throws ParserConfigurationException, FactoryConfigurationError {

		Element XPEDXMyItemsList = inputXML.getDocumentElement();

		String strMasterCustomerID = XPEDXMyItemsList
				.getAttribute("MasterCustomerID");
		String strCustomerID = XPEDXMyItemsList.getAttribute("CustomerID");
		String strBillToCustomerID = XPEDXMyItemsList
				.getAttribute("BillToCustomerID");
		String strShipToCustomerID = XPEDXMyItemsList
				.getAttribute("ShipToCustomerID");

		System.out.println("strMasterCustomerID ::" + strMasterCustomerID);
		System.out.println("strCustomerID ::" + strCustomerID);
		System.out.println("strBillToCustomerID ::" + strBillToCustomerID);
		System.out.println("strShipToCustomerID ::" + strShipToCustomerID);

		String strCustomerPath = null;
		String strSeparator = "|";

		if (strMasterCustomerID.isEmpty()) {
			// CustomerPath is null
			System.out.println("customer path is null");

		} else {
			// CustomerPath is not null ignore DivisionId
			// CustomerPath =
			// MasterCustomerID|CustomerID|BillToCustomerID|ShipToCustomerID

			if (!strShipToCustomerID.isEmpty()
					&& !strBillToCustomerID.isEmpty()
					&& !strCustomerID.isEmpty()
					&& !strMasterCustomerID.isEmpty()) {

				strCustomerPath = strMasterCustomerID + strSeparator
						+ strCustomerID + strSeparator + strBillToCustomerID
						+ strSeparator + strShipToCustomerID;
			} else if (!strBillToCustomerID.isEmpty()
					&& !strCustomerID.isEmpty()
					&& !strMasterCustomerID.isEmpty()) {

				strCustomerPath = strMasterCustomerID + strSeparator
						+ strCustomerID + strSeparator + strBillToCustomerID;

			} else if (!strCustomerID.isEmpty()
					&& !strMasterCustomerID.isEmpty()) {

				strCustomerPath = strMasterCustomerID + strSeparator
						+ strCustomerID;

			} else {
				strCustomerPath = strMasterCustomerID;
			}

			// end if(!strShipToCustomerID.isEmpty() &&
			// !strBillToCustomerID.isEmpty() && !strCustomerID.isEmpty() &&
			// !strMasterCustomerID.isEmpty()){

		}// end if(strMasterCustomerID.isEmpty()){

		System.out.println("strCustomerPath ::" + strCustomerPath);

		Document getXPEDX_MyItemsList_ListInputXML = prepareInputXML(
				XPEDXMyItemsList, strCustomerPath);

		return getXPEDX_MyItemsList_ListInputXML;
	}

	private Document prepareInputXML(Element xPEDXMyItemsList,
			String strCustomerPath) throws ParserConfigurationException,
			FactoryConfigurationError {

		Document getXPEDX_MyItemsList_ListInputXML = SCXmlUtil
				.getDocumentBuilder().newDocument();

		String strItemID = xPEDXMyItemsList.getAttribute("LegacyProductCode");

		Element elemXPEDXMyItemsList = getXPEDX_MyItemsList_ListInputXML
				.createElement("XPEDXMyItemsList");
		getXPEDX_MyItemsList_ListInputXML.appendChild(elemXPEDXMyItemsList);

		if (null != strCustomerPath) {
			// prepare input xml with customer path
			/**
			 * <XPEDXMyItemsList> <XPEDXMyItemsListShareList>
			 * <XPEDXMyItemsListShare
			 * CustomerPath="30-0000145087-000-N3-12-MSAP|30-00001450"
			 * CustomerPathQryType="FLIKE" /> </XPEDXMyItemsListShareList>
			 * <XPEDXMyItemsItemsList> <XPEDXMyItemsItems
			 * ItemId="XPEDXMyItemsList@LegacyProductCode" />
			 * </XPEDXMyItemsItemsList> </XPEDXMyItemsList>
			 **/

			Element elemXPEDXMyItemsListShareList = getXPEDX_MyItemsList_ListInputXML
					.createElement("XPEDXMyItemsListShareList");
			elemXPEDXMyItemsList.appendChild(elemXPEDXMyItemsListShareList);

			Element elemXPEDXMyItemsListShare = getXPEDX_MyItemsList_ListInputXML
					.createElement("XPEDXMyItemsListShare");
			elemXPEDXMyItemsListShareList
					.appendChild(elemXPEDXMyItemsListShare);
			elemXPEDXMyItemsListShare.setAttribute("CustomerPathQryType",
					"FLIKE");
			elemXPEDXMyItemsListShare.setAttribute("CustomerPath",
					strCustomerPath);

			Element elemXPEDXMyItemsItemsList = getXPEDX_MyItemsList_ListInputXML
					.createElement("XPEDXMyItemsItemsList");
			elemXPEDXMyItemsList.appendChild(elemXPEDXMyItemsItemsList);

			Element elemXPEDXMyItemsItems = getXPEDX_MyItemsList_ListInputXML
					.createElement("XPEDXMyItemsItems");
			elemXPEDXMyItemsItemsList.appendChild(elemXPEDXMyItemsItems);
			elemXPEDXMyItemsItems.setAttribute("ItemId", strItemID);

			System.out.println("getXPEDX_MyItemsList_ListInputXML :: "
					+ SCXmlUtil.getString(getXPEDX_MyItemsList_ListInputXML));

		} else {
			// prepare input with division id
			System.out.println("Customer Path is Null");

			// prepare input xml with division id
			String strDivisionID = xPEDXMyItemsList.getAttribute("DivisionID");

			// division id sample DivisionID="50,60,30"
			// tokenize division id separated by comma

			if (strDivisionID.isEmpty()) {
				// if total no of division id s is zero send input xml with just
				// item id
				// prepare input xml with item id
				/**
				 * <XPEDXMyItemsList> <XPEDXMyItemsItemsList> <XPEDXMyItemsItems
				 * ItemId="XPEDXMyItemsList@LegacyProductCode" />
				 * </XPEDXMyItemsItemsList> </XPEDXMyItemsList>
				 **/

				System.out.println("Division  ID is Null ");

				Element elemXPEDXMyItemsItemsList = getXPEDX_MyItemsList_ListInputXML
						.createElement("XPEDXMyItemsItemsList");
				elemXPEDXMyItemsList.appendChild(elemXPEDXMyItemsItemsList);

				Element elemXPEDXMyItemsItems = getXPEDX_MyItemsList_ListInputXML
						.createElement("XPEDXMyItemsItems");
				elemXPEDXMyItemsItemsList.appendChild(elemXPEDXMyItemsItems);
				elemXPEDXMyItemsItems.setAttribute("ItemId", strItemID);

				System.out.println("getXPEDX_MyItemsList_ListInputXML :: "
						+ SCXmlUtil
								.getString(getXPEDX_MyItemsList_ListInputXML));

			}

			else if (!strDivisionID.contains(",")) {
				// if total no of division id is 1 send input xml with division
				// id and item id

				System.out.println("only one division id ");

				// prepare input xml with customer path
				/**
				 * <XPEDXMyItemsList> <XPEDXMyItemsListShareList>
				 * <XPEDXMyItemsListShare
				 * DivisionID="30-0000145087-000-N3-12-MSAP|30-00001450" />
				 * </XPEDXMyItemsListShareList> <XPEDXMyItemsItemsList>
				 * <XPEDXMyItemsItems
				 * ItemId="XPEDXMyItemsList@LegacyProductCode" />
				 * </XPEDXMyItemsItemsList> </XPEDXMyItemsList>
				 **/

				Element elemXPEDXMyItemsListShareList = getXPEDX_MyItemsList_ListInputXML
						.createElement("XPEDXMyItemsListShareList");
				elemXPEDXMyItemsList.appendChild(elemXPEDXMyItemsListShareList);

				Element elemXPEDXMyItemsListShare = getXPEDX_MyItemsList_ListInputXML
						.createElement("XPEDXMyItemsListShare");
				elemXPEDXMyItemsListShareList
						.appendChild(elemXPEDXMyItemsListShare);
				elemXPEDXMyItemsListShare.setAttribute("DivisionID",
						strDivisionID);

				Element elemXPEDXMyItemsItemsList = getXPEDX_MyItemsList_ListInputXML
						.createElement("XPEDXMyItemsItemsList");
				elemXPEDXMyItemsList.appendChild(elemXPEDXMyItemsItemsList);

				Element elemXPEDXMyItemsItems = getXPEDX_MyItemsList_ListInputXML
						.createElement("XPEDXMyItemsItems");
				elemXPEDXMyItemsItemsList.appendChild(elemXPEDXMyItemsItems);
				elemXPEDXMyItemsItems.setAttribute("ItemId", strItemID);

				System.out.println("getXPEDX_MyItemsList_ListInputXML :: "
						+ SCXmlUtil
								.getString(getXPEDX_MyItemsList_ListInputXML));

			} else {
				// if total no of division id s is greater then 1 prepare
				// multiapi
				// input xml and stamp the root attribute isMultiApiInput=Y

				// tokenize division id comma separted

				String strToken = ",";

				String strDivisionIDArr[] = strDivisionID.split(strToken);
				int intNoOfDivIDs = strDivisionIDArr.length;

				System.out.println("Number of Division ID s " + intNoOfDivIDs);

				elemXPEDXMyItemsList.setAttribute("isMultiApi", "Y");

				/**
				 * <MultiApi> <API FlowName="getXPEDX_MyItemsList_List"> <Input>
				 * <XPEDXMyItemsList> <XPEDXMyItemsListShareList>
				 * <XPEDXMyItemsListShare DivisionID="15" />
				 * </XPEDXMyItemsListShareList> </XPEDXMyItemsList> </Input>
				 * </API> <API FlowName="getXPEDX_MyItemsList_List"> <Input>
				 * <XPEDXMyItemsList> <XPEDXMyItemsListShareList>
				 * <XPEDXMyItemsListShare DivisionID="13" />
				 * </XPEDXMyItemsListShareList> </XPEDXMyItemsList> </Input>
				 * </API> </MultiApi>
				 */

				Element elemMultiAPI = getXPEDX_MyItemsList_ListInputXML
						.createElement("MultiApi");
				elemXPEDXMyItemsList.appendChild(elemMultiAPI);
				String strTempDivID = null;
				Node tempNode = null;
				for (int i = 0; i < intNoOfDivIDs; i++) {
					strTempDivID = strDivisionIDArr[i];
					System.out.println("strTempDivID" + strTempDivID);
					if (!strTempDivID.isEmpty()) {
						tempNode = getAPINode(strTempDivID, strItemID);
						tempNode = getXPEDX_MyItemsList_ListInputXML
								.importNode(tempNode, true);
						elemMultiAPI.appendChild(tempNode);
					}// end if(!strTempDivID.isEmpty()){

				}// end for (int i = 0; i < intNoOfDivIDs; i++) {

			}// end else if (!strDivisionID.contains(",")) {

		}// end if(null != strCustomerPath){

		return getXPEDX_MyItemsList_ListInputXML;
	}

	/**
	 * 
	 * @param strTempDivID
	 * @param strItemID
	 * @return
	 * @throws ParserConfigurationException
	 * @throws FactoryConfigurationError
	 */
	private Node getAPINode(String strTempDivID, String strItemID)
			throws ParserConfigurationException, FactoryConfigurationError {

		// prepare Input of the form
		/**
		 * <API FlowName="getXPEDX_MyItemsList_List"> <Input> <XPEDXMyItemsList>
		 * <XPEDXMyItemsListShareList> <XPEDXMyItemsListShare DivisionID="13" />
		 * </XPEDXMyItemsListShareList> <XPEDXMyItemsItemsList>
		 * <XPEDXMyItemsItems ItemId="XPEDXMyItemsList@LegacyProductCode" />
		 * </XPEDXMyItemsItemsList> </XPEDXMyItemsList> </Input> </API>
		 */

		Document docAPI = SCXmlUtil.getDocumentBuilder().newDocument();
		Element elemAPI = docAPI.createElement("API");
		elemAPI.setAttribute("FlowName", "getXPEDX_MyItemsList_List");

		Element elemInput = docAPI.createElement("Input");
		elemAPI.appendChild(elemInput);

		Element XPEDXMyItemsList = docAPI.createElement("XPEDXMyItemsList");
		elemInput.appendChild(XPEDXMyItemsList);

		Element XPEDXMyItemsListShareList = docAPI
				.createElement("XPEDXMyItemsListShareList");
		XPEDXMyItemsList.appendChild(XPEDXMyItemsListShareList);

		Element XPEDXMyItemsListShare = docAPI
				.createElement("XPEDXMyItemsListShare");
		XPEDXMyItemsListShareList.appendChild(XPEDXMyItemsListShare);
		XPEDXMyItemsListShare.setAttribute("DivisionID", strTempDivID);

		if (!strItemID.isEmpty()) {

			Element XPEDXMyItemsItemsList = docAPI
					.createElement("XPEDXMyItemsItemsList");
			XPEDXMyItemsList.appendChild(XPEDXMyItemsItemsList);

			Element XPEDXMyItemsItems = docAPI
					.createElement("XPEDXMyItemsItems");
			XPEDXMyItemsItemsList.appendChild(XPEDXMyItemsItems);
			XPEDXMyItemsItems.setAttribute("ItemId", strItemID);
		}

		return elemAPI;
	}

	/**
	 * @param arg0
	 */
	public void setProperties(Properties arg0) throws Exception {
		// Not Implemented for current scope of requirements

	}

}
