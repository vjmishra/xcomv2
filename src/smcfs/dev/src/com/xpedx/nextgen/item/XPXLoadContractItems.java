package com.xpedx.nextgen.item;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.custom.dbi.XPX_Item_Contract_Extn;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.shared.dbclasses.XPX_Item_Contract_ExtnBase;
import com.yantra.shared.dbclasses.XPX_Item_Contract_ExtnDBHome;
import com.yantra.shared.ycp.YFSContext;
import com.yantra.yfc.dblayer.PLTQueryBuilder;
import com.yantra.yfc.dblayer.PLTQueryBuilderHelper;
import com.yantra.yfc.dblayer.YFCDBException;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXLoadContractItems implements YIFCustomApi {

	private static final String DEFAULT_FILE_PATH = "/xpedx/ContractItems.csv";
	private static final String CONTRACT_FILE_PROP_NAME = "contractItemsCsv.file";

	private static YFCLogCategory log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	private static YIFApi api;
	private YFSEnvironment env;
	private YFSContext ctx;
	private Map<String,String> origCustIds;    // custId and its type
	private Map<String,String> masterCustKeys; // MC's CustId to its key
	private Map<String,String> masterKeysCust; // MC's key to its CustId
	private Map<String,String> customerKeys;   // C's CustId to its key
	private Map<String,String> keysCustomer;   // C's key to its CustId
	private List<Pair> origEntries;

	@Override
	public void setProperties(Properties arg0) throws Exception {
	}

	public Document loadContractData(YFSEnvironment env,Document inputXML) throws Exception {
		this.env = env;
		ctx = (YFSContext)env;
		api = YIFClientFactory.getInstance().getApi();

		File file = getUsersListExcelFile();
		if (file == null) {
			return null;
		}

		loadContractDataFromFile(file);

		return null;
	}

	private void loadContractDataFromFile(File file) throws Exception {

		if (loadEntriesFromFile(file) == 0) {
			log.warn("XPXLoadContractItems: No valid contract entries found in specified file");
			return;
		}

		// Sort customerIds into different types for processing
		if (getKeysAndCustTypes() == 0) {
			log.warn("XPXLoadContractItems: No valid customers found in DB for specified Cust IDs");
			return;
		}

		// for MCs, get their BillTos by calling getCustomerList where rootKey
		Map<String, List<String>> masterBillTos = getBillTosForCust("RootCustomerKey", masterKeysCust.keySet());

		// for Cs,  get their BillTos by calling getCustomerList where parentKey
		Map<String, List<String>> customerBillTos = getBillTosForCust("ParentCustomerKey", keysCustomer.keySet());

		ArrayList<Pair> resultPairList = createExpandedList(masterBillTos, customerBillTos);

		if (resultPairList.size() == 0) {
			log.warn("XPXLoadContractItems: No entries to write to DB");
			return;
		}
		log.info("XPXLoadContractItems: Resulting list: " + resultPairList.size());

		saveEntriesToDB(resultPairList);
	}

	private ArrayList<Pair> createExpandedList(Map<String, List<String>> masterBillTos, Map<String, List<String>> customerBillTos) {
		ArrayList<Pair> resultPairList = new ArrayList<Pair>(1000);

		// loop over original list, copying billTos, adding expanded entries for MC and C
		for (Pair pair : origEntries) {
			final String itemId = pair.itemId;
			final String custId = pair.custId;
			final String type = origCustIds.get(custId);

			if ("MC".equals(type)) {
				for (String billTo : masterBillTos.get(masterCustKeys.get(custId))) {
					resultPairList.add(new Pair(itemId, billTo));
				}
			}
			else if ("C".equals(type)) {
				for (String billTo : customerBillTos.get(customerKeys.get(custId))) {
					resultPairList.add(new Pair(itemId, billTo));
				}
			}
			else if ("B".equals(type)) {
				resultPairList.add(new Pair(itemId, custId));
			}
			else {
				log.warn("XPXLoadContractItems: CustId in file does not exist: " + custId);
			}
		}
		return resultPairList;
	}

	private void saveEntriesToDB(ArrayList<Pair> outputPairList) {

		// Clear all old entries in XPX_ITEM_CONTRACT_EXTN before inserting new
		deleteExistingEntriesFromTable();

		// Insert the new entries into table
		log.warn("XPXLoadContractItems: attempting to write " +outputPairList.size()+ " rows to XPX_ITEM_CONTRACT_EXTN");
		int count = 0;
		for (Pair pair : outputPairList) {
			final String itemId = pair.itemId;
			final String custId = pair.custId;
			log.debug("XPXLoadContractItems:  inserting " + itemId +" : "+custId);
			try {
				XPX_Item_Contract_Extn contractRow = XPX_Item_Contract_ExtnBase.newInstance();
				contractRow.setItem_Id(itemId);
				contractRow.setCustomer_Id(custId);
				XPX_Item_Contract_ExtnDBHome.getInstance().insert(ctx, contractRow);
				count++;
			}
			catch (YFCDBException ye) {
				if (ye.getSqlException() instanceof SQLIntegrityConstraintViolationException) {
				log.error("XPXLoadContractItems: SQLIntegrityConstraintViolationException on " +
						"ItemID: " +itemId+ " CustomerID: " +custId + ye.getSqlException().getMessage());
				}
			}
			catch (Exception e) {
				log.error("XPXLoadContractItems: problem inserting row with " +
						"ItemID: " +itemId+ " CustomerID: " +custId,  e);
			}
		}
		log.warn("XPXLoadContractItems: Rows written to DB: " + count+ " out of " +outputPairList.size());
	}

	// Clear table. Returns # of rows deleted.
	private int deleteExistingEntriesFromTable() {
		int deleteFlag = 0;
		log.warn("XPXLoadContractItems: Deleting all rows from XPX_ITEM_CONTRACT_EXTN...");
		try {
			PLTQueryBuilder pltQryBuilder = PLTQueryBuilderHelper.createPLTQueryBuilder();
			pltQryBuilder.setCurrentTable("XPX_ITEM_CONTRACT_EXTN");
			pltQryBuilder.appendString("ITEM_ID", "<>", ""); //hack to drop all rows
			deleteFlag = XPX_Item_Contract_ExtnDBHome.getInstance().deleteWithWhere((YFSContext)env, pltQryBuilder);
			log.info("XPXLoadContractItems: deleted old rows from XPX_ITEM_CONTRACT_EXTN: " + deleteFlag);
		}
		catch (Exception e) {
			log.error("XPXLoadContractItems: problem deleting old rows from XPX_ITEM_CONTRACT_EXTN - " + e.getMessage());
		}
		return deleteFlag;
	}

	// Specify either "RootCustomerKey" (MC) or "ParentCustomerKey" (C)
	private Map<String, List<String>> getBillTosForCust(final String keyType, Set<String> custKeys) throws RemoteException {
		Map<String,List<String>> mapCustKeyBillTos = new HashMap<String, List<String>>(custKeys.size());
		if (custKeys.size() == 0) {
			return mapCustKeyBillTos;
		}

		// Create input from cust list specifying relationship to its BillTos
		YFCDocument inputCustomerDoc = YFCDocument.createDocument("Customer");
		YFCElement customerExtnElement = inputCustomerDoc.createElement(XPXLiterals.E_EXTN);
		customerExtnElement.setAttribute("ExtnSuffixType", "B");
		inputCustomerDoc.getDocumentElement().appendChild(customerExtnElement);

		YFCElement eleComplexQuery = inputCustomerDoc.getDocumentElement().createChild(XPXLiterals.E_COMPLEX_QUERY);
        YFCElement eleOr = eleComplexQuery.createChild(XPXLiterals.E_Or);
		for (String custKey : custKeys) {
            YFCElement eleExp = eleOr.createChild(XPXLiterals.E_EXP);
            eleExp.setAttribute(XPXLiterals.A_NAME, keyType);
            eleExp.setAttribute(XPXLiterals.A_VALUE, custKey);
        }

		String customerListTemplate = "<Customer CustomerID='' CustomerKey='' " +keyType+ "=''> </Customer>";

		// Call API to get all the BillTos
		env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API, SCXmlUtil.createFromString(customerListTemplate) );
		Document customerListDoc = api.invoke(env, XPXLiterals.GET_CUSTOMER_LIST_API, inputCustomerDoc.getDocument());
		env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);

		NodeList customerNodeList = customerListDoc.getElementsByTagName("Customer");
		final int length = customerNodeList.getLength();
		log.info("XPXLoadContractItems: billTos for " +keyType+ " CustIds: " +length);

		// Group the found billTos by the key for later lookup
		for(int counter=0; counter<length; counter++) {
			Element customerElement = (Element)customerNodeList.item(counter);
			final String billToId = customerElement.getAttribute("CustomerID");
			final String rootKey  = customerElement.getAttribute(keyType);
			if (!mapCustKeyBillTos.containsKey(rootKey)) {
				mapCustKeyBillTos.put(rootKey, new ArrayList<String>());
			}
			mapCustKeyBillTos.get(rootKey).add(billToId);
		}

		return mapCustKeyBillTos;
	}

	// Obtain and organize cust info from DB. Returns number of valid customers
	private int getKeysAndCustTypes() throws YFSException, RemoteException {

		NodeList customerNodeList = getCustInfo();
		final int length = customerNodeList.getLength();
		if (length == 0) {
			return 0;
		}
		log.info("XPXLoadContractItems: obtained data for " +length+ " customers from DB");

		// Save into various collections for further processing
		masterCustKeys = new HashMap<String,String>();
		masterKeysCust = new HashMap<String,String>();
		customerKeys = new HashMap<String,String>();
		keysCustomer = new HashMap<String,String>();

		for(int counter=0; counter<length; counter++) {
			Element customerElement = (Element)customerNodeList.item(counter);
			final String id  =  customerElement.getAttribute("CustomerID");
			final String key  = customerElement.getAttribute("CustomerKey");
			Element eleExtn = SCXmlUtil.getChildElement(customerElement, "Extn");
			String type = "";
			if (null != eleExtn) {
				type  = eleExtn.getAttribute("ExtnSuffixType");
			}

			if ("MC".equals(type)) {
				origCustIds.put(id, "MC");
				masterCustKeys.put(id, key);
				masterKeysCust.put(key, id);
			}
			else if ("C".equals(type)) {
				origCustIds.put(id, "C");
				customerKeys.put(id, key);
				keysCustomer.put(key, id);
			}
			else if ("B".equals(type)) {
				origCustIds.put(id, "B");
			}
		}
		log.info("XPXLoadContractItems: Custs found from file: " + origCustIds.size());
		log.info("XPXLoadContractItems: MCs: " + masterKeysCust.size());
		log.info("XPXLoadContractItems: Cs:  " + keysCustomer.size());
		return length;
	}

	private NodeList getCustInfo() throws RemoteException {
		YFCDocument inputCustomerDoc = YFCDocument.createDocument("Customer");
        YFCElement eleComplexQuery = inputCustomerDoc.getDocumentElement().createChild(XPXLiterals.E_COMPLEX_QUERY);
        YFCElement eleOr = eleComplexQuery.createChild(XPXLiterals.E_Or);

        for (String custId : origCustIds.keySet()) {
            YFCElement eleExp = eleOr.createChild(XPXLiterals.E_EXP);
            eleExp.setAttribute(XPXLiterals.A_NAME, "CustomerID");
            eleExp.setAttribute(XPXLiterals.A_VALUE, custId);
        }

		String customerListTemplate = "<Customer CustomerID='' CustomerKey=''> "
				   + "<Extn ExtnSuffixType='' /> " + "</Customer>";

		env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API, SCXmlUtil.createFromString(customerListTemplate) );
		Document customerListDoc = api.invoke(env, XPXLiterals.GET_CUSTOMER_LIST_API, inputCustomerDoc.getDocument());
		env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);

		NodeList customerNodeList = customerListDoc.getElementsByTagName("Customer");
		return customerNodeList;
	}

	// returns # of valid entries found in file
	private int loadEntriesFromFile(File file) throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		origEntries = new ArrayList<Pair>(1000);
		origCustIds = new HashMap<String,String>(1000);
		Set<String> alreadyRead = new HashSet<String>(1000);
		int num = 0;
		String line;

		while ((line = br.readLine()) != null) {
			num++;
			String[] ids = line.split(",");
			if (ids.length != 2) {
				log.warn("XPXLoadContractItems: Invalid entry on line " + num +": "+ line);
			}
			else {
				final String itemId = ids[0];
				final String custId = ids[1];
				final String combo = ids[0] + ids[1];
				if (alreadyRead.contains(combo)) {
					log.info("XPXLoadContractItems: Dup entry found - " + itemId +" : "+custId);
					continue;
				}
				alreadyRead.add(combo);

				origEntries.add(new Pair(itemId, custId));
				origCustIds.put(custId,""); // ignores dups
			}
			//TODO check for itemIds not in DB (ignore and/or warn) or load regardless?
		}
		br.close();
		log.info("XPXLoadContractItems: file contains " + origEntries.size() + " items");
		return origEntries.size();
	}

	private File getUsersListExcelFile(){

		String pathName = YFSSystem.getProperty(CONTRACT_FILE_PROP_NAME);
		log.info("XPXLoadContractItems: file specified in properties: "+ pathName);

		if (pathName==null || pathName.isEmpty() || !new File(pathName).exists()){
			pathName = DEFAULT_FILE_PATH;
			log.warn("XPXLoadContractItems: can't find file, trying default pathname: "+pathName);
		}

		File file = new File(pathName);
		if (!file.exists()){
			log.error("XPXLoadContractItems couldn't find file to load: " + pathName);
			return null;
		}

		return file;
	}

	private class Pair {
		public String itemId;
		public String custId;
		public Pair(String str1, String str2) {
			this.itemId = str1;
			this.custId = str2;
		}
	}
}
