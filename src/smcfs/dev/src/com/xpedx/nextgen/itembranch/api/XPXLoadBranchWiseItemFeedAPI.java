package com.xpedx.nextgen.itembranch.api;

import java.rmi.RemoteException;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXLoadBranchWiseItemFeedAPI implements YIFCustomApi {

	/** API object. */
	private static YIFApi api = null;

	private static YFCLogCategory log;

	// private Properties props;

	public void setProperties(Properties params) throws Exception {
		// this.props = props;
	}

	static {
		try {
			log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e1) {

			e1.printStackTrace();
		}

	}

	/**
	 * Loads Item Branch data. Input XML format:
	 * 
	 * <pre>
	 *  &lt;ItemBranchs&gt;
	 * 	 &lt;ItemBranch EnvironmentId ='PROD' CompanyCode ='xpedx' ProcessCode='A' MasterProductCode='ITEM1' LegacyProductNumber='ITEM1' DivisionNumber='DIV30' CustomerNumber='Buyer1' ProductStatus='' ItemStockStatus='' InventoryIndicator='' OrderMultiple='1'&gt;
	 * 
	 * 	 &lt;AlternateItems&gt;
	 * 	 &lt;AlternateItem MasterProductCode ='ITEM3' /&gt;
	 * 	 &lt;/AlternateItems&gt;
	 * 
	 * 	 &lt;ReplacementItems&gt;
	 * 	 &lt;ReplacementItem MasterProductCode ='ITEM3' /&gt;
	 * 	 &lt;/ReplacementItems&gt;
	 * 
	 * 	 &lt;ComplimentaryItems&gt;
	 * 	 &lt;ComplimentaryItem MasterProductCode ='ITEM3' /&gt;
	 * 	 &lt;/ComplimentaryItems&gt;
	 * 
	 * 	 &lt;/ItemBranch&gt;
	 * 	 &lt;/ItemBranchs&gt;
	 * </pre>
	 * 
	 * @param env
	 * @param inXML
	 * @return Document
	 * @throws YFSException
	 * @throws YIFClientCreationException
	 * @throws RemoteException
	 */
	public Document invokeItemBranchLoad(YFSEnvironment env, Document inXML)
			throws Exception {

		/**
		 * try-catch block added by Arun Sekhar on 28-Jan-2011 for CENT tool
		 * integration *
		 */
		Document docItemExtns = null;
		Element eleWMItemBranchs = null;
		try {
			log.beginTimer("XPXLoadBranchWiseItemFeedAPI.invokeItemBranchLoad");
			eleWMItemBranchs = inXML.getDocumentElement();
			NodeList nlWMItemBranch = eleWMItemBranchs
					.getElementsByTagName("ItemBranch");

			docItemExtns = SCXmlUtil.createDocument("XPXItemExtns");
			Element eleItemExtns = docItemExtns.getDocumentElement();
			for (int i = 0; i < nlWMItemBranch.getLength(); i++) {
				this.mergeItemBranchData(env, nlWMItemBranch, docItemExtns,
						eleItemExtns, i);
			}
			NodeList nl = eleItemExtns.getElementsByTagName("XPXItemExtn");
			for (int i = 0; i < nl.getLength(); i++) {
				Document docItem = YFCDocument.createDocument().getDocument();
				docItem.appendChild(docItem.importNode(nl.item(i), true));
				String strOperation = docItem.getDocumentElement()
						.getAttribute("Operation");
				String invokeService = null;
				if ("Create".equals(strOperation)) {
					invokeService = "createXPXItemBranchService";
				} else if ("Modify".equals(strOperation)) {
					invokeService = "changeXPXItemBranchService";
				} else if ("Delete".equals(strOperation)) {
					invokeService = "deleteXPXItemBranchService";
				}
				// Document returnDoc =
				api.executeFlow(env, invokeService, docItem);

			}

			log.endTimer("XPXLoadBranchWiseItemFeedAPI.invokeItemBranchLoad");
		} catch (NullPointerException ne) {
			log.error("------------Failed XML Needs to Catch for Re-Processing XML START ----------");
			log.error(SCXmlUtil.getString(eleWMItemBranchs));
			log.error("------------Failed XML Needs to Catch for Re-Processing XML END ----------");
			log.error("NullPointerException: " + ne.getStackTrace());
			prepareErrorObject(ne, XPXLiterals.ITEM_DIV_B_TRANS_TYPE,
					XPXLiterals.NE_ERROR_CLASS, env, inXML);
			throw ne;
		} catch (YFSException yfe) {
			log.error("------------Failed XML Needs to Catch for Re-Processing XML START ----------");
			log.error(SCXmlUtil.getString(eleWMItemBranchs));
			log.error("------------Failed XML Needs to Catch for Re-Processing XML END ----------");
			log.error("YFSException: " + yfe.getStackTrace());
			prepareErrorObject(yfe, XPXLiterals.ITEM_DIV_B_TRANS_TYPE,
					XPXLiterals.YFE_ERROR_CLASS, env, inXML);
			throw yfe;
		} catch (Exception e) {
			log.error("------------Failed XML Needs to Catch for Re-Processing XML START ----------");
			log.error(SCXmlUtil.getString(eleWMItemBranchs));
			log.error("------------Failed XML Needs to Catch for Re-Processing XML END ----------");
			log.error("Exception: " + e.getStackTrace());
			prepareErrorObject(e, XPXLiterals.ITEM_DIV_B_TRANS_TYPE,
					XPXLiterals.E_ERROR_CLASS, env, inXML);
			throw e;
		}
		return docItemExtns;
	}

	/**
	 * @This method prepares the error object with the exception details which
	 *       in turn will be used to log into CENT
	 */
	private void prepareErrorObject(Exception e, String transType,
			String errorClass, YFSEnvironment env, Document inXML) {
		com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
		errorObject.setTransType(transType);
		errorObject.setErrorClass(errorClass);
		errorObject.setInputDoc(inXML);
		errorObject.setException(e);
		ErrorLogger.log(errorObject, env);
	}

	private void mergeItemBranchData(YFSEnvironment env,
			NodeList nlWMItemBranch, Document docItemExtns,
			Element eleItemExtns, int i) throws YFSException, RemoteException {
		Element eleWMItemBranch;
		NodeList nlWMAltItem;
		// NodeList nlWMReplaceItem;
		// NodeList nlWMComplItem;
		Element eleItemExtn;
		Element eleItemAssociationsList;
		String ProcessCode = null;
		String strOperation = null;
		// String CompanyCode = null;
		// String MasterProductCode= null;
		// String DivisionNumber= null;
		String ItemExtnKey = null;

		eleWMItemBranch = (Element) nlWMItemBranch.item(i);

		eleItemExtn = docItemExtns.createElement("XPXItemExtn");
		eleItemExtns.appendChild(eleItemExtn);
		// Map and copy attributes from ItemBranch to ItemExtn
		this.copyItemBranchAttrs(env, eleWMItemBranch, eleItemExtn);
		ProcessCode = eleWMItemBranch.getAttribute("ProcessCode");
		
		if (ProcessCode != null) {
			if ("X".equals(ProcessCode)) {
				Element eleNewLegacyDoc = getXPXItemBranchDetails(env,
						eleWMItemBranch);
				// NodeList nlWNewLegacyDoc =
				// eleNewLegacyDoc.getElementsByTagName("XPXItemAssociations");
				// Element eleWNewLegacyDoc=(Element) nlWNewLegacyDoc.item(0);
				if (eleNewLegacyDoc != null) {
					//ItemExtnKey = eleNewLegacyDoc.getAttribute("ItemExtnKey");
					NodeList nlXPXItemExtn = eleNewLegacyDoc.getElementsByTagName("XPXItemExtn");
					if(nlXPXItemExtn.getLength()>0){
						Element eXPXItemExtn = (Element)nlXPXItemExtn.item(0);
						ItemExtnKey = eXPXItemExtn.getAttribute("ItemExtnKey");
					
					}
				}
			}						
			
			eleItemAssociationsList = docItemExtns
					.createElement("XPXItemAssociationsList");
			eleItemExtn.appendChild(eleItemAssociationsList);

			if ("A".equals(ProcessCode)) {
				strOperation = "Create";
			} /*
				 * else if("C".equals(ProcessCode)){ strOperation = "Modify";
				 * eleItemAssociationsList.setAttribute( "Reset", "true"); }
				 */else if ("D".equals(ProcessCode)) {
				strOperation = "Delete";
			} else if ("X".equals(ProcessCode)) {
				strOperation = "CrossReffrence";
			}
			if ("A".equals(ProcessCode) || "X".equals(ProcessCode)
					|| "D".equals(ProcessCode)) {
				/**
				 * *****************Commenting out as per modified design of
				 * Item Branch creation*****************
				 */
				// nlWMAltItem=eleWMItemBranch.getElementsByTagName("RelatedItem");
				/** *********************************************************************************************** */

				Element itemXRefsElement = (Element) eleWMItemBranch
						.getElementsByTagName("ItemXRefs").item(0);

				if (itemXRefsElement != null) {
					/**
					 * To handle the condition when there is ItemXrefs elemnent
					 * in the feed**************
					 */
					nlWMAltItem = itemXRefsElement
							.getElementsByTagName("ItemXRef");

					if (nlWMAltItem != null) {
						/**
						 * To handle the condition when there is ItemXref
						 * elemnent in the feed**************
						 */
						this.copyItemAssociations(docItemExtns, nlWMAltItem,
								eleItemAssociationsList, ProcessCode,
								strOperation, ItemExtnKey, env);
					}
				}
			}
		}

	}

	private Element getXPXItemBranchDetails(YFSEnvironment env,
			Element eleWMItemBranch) throws RemoteException {
		String CompanyCode;
		String legacyProductNumber;
		String DivisionNumber;
		Element eleNewLegacyDoc = null;
		CompanyCode = eleWMItemBranch.getAttribute("CompanyCode");
		legacyProductNumber = eleWMItemBranch
				.getAttribute("LegacyProductNumber");
		DivisionNumber = eleWMItemBranch.getAttribute("DivisionNumber");
        String environmentId = eleWMItemBranch.getAttribute("EnvironmentId"); 
		YFCDocument inputItemBranch = YFCDocument.createDocument("XPXItemExtn");
		YFCElement inputItemBranchElement = inputItemBranch
				.getDocumentElement();
		//inputItemBranchElement.setAttribute("CompanyCode", CompanyCode);
		inputItemBranchElement.setAttribute("EnvironmentID",environmentId);
		inputItemBranchElement.setAttribute("ItemID", legacyProductNumber);
		inputItemBranchElement.setAttribute("XPXDivision", DivisionNumber);

		Document NewLegacyDoc = api
				.executeFlow(env, "getXPXItemBranchListService",
						inputItemBranch.getDocument());

		if (NewLegacyDoc != null) {
			eleNewLegacyDoc = NewLegacyDoc.getDocumentElement();
		} else {
			// invalid Item Branch
			throw new YFSException("Invalid Item Branch.", "ERROR_LOAD_02",
					"Invalid Item Details:["
							+ eleWMItemBranch
									.getAttribute("LegacyProductNumber")
							+ "] passed in Item Branch Header.");
		}
		return eleNewLegacyDoc;
	}

	private void copyItemAssociations(Document docItemExtns,
			NodeList nlWMAssoItem, Element eleItemAssociationsList,
			String AssoType, String strOperation, String ItemExtnKey,
			YFSEnvironment env) throws YFSException, RemoteException {
		Element eleWMAssoItem;
		Element eleItemAssociations;
		int len;

		len = nlWMAssoItem.getLength();
		for (int j = 0; j < len; j++) {
			eleWMAssoItem = (Element) nlWMAssoItem.item(j);
			String chdProcessCode = eleWMAssoItem.getAttribute("ProcessCode");
			/**
			 * ******Commented out as per modified design for Item Branch
			 * load*********************
			 */
			// String
			// chdRelationType=eleWMAssoItem.getAttribute("RelationType");
			/** ************************************************************************************ */
			String chdRelationType = eleWMAssoItem.getAttribute("Type");

			// Map and copy the attributes from eleWMAssoItem to
			// ItemAssociations
			eleItemAssociations = docItemExtns
					.createElement("XPXItemAssociations");
			eleItemAssociationsList.appendChild(eleItemAssociations);

			strOperation = null;
			if ("A".equals(chdProcessCode)) {
				strOperation = "Create";
			} else if ("D".equals(chdProcessCode)) {
				strOperation = "Delete";

			}

			eleItemAssociations.setAttribute("Operation", strOperation);
			eleItemAssociations.setAttribute("AssociatedItemID", eleWMAssoItem
					.getAttribute("ItemNumber"));
			eleItemAssociations
					.setAttribute("AssociationType", chdRelationType);
			if ("D".equals(chdProcessCode)) {
				if (ItemExtnKey != null && ItemExtnKey.trim().length() != 0) {

					eleItemAssociations
							.setAttribute("ItemExtnKey", ItemExtnKey);

					/**
					 * ***Added code to check if there is any existing Item
					 * association and only then should we go ahead and delete
					 * else error out
					 */
					YFCDocument getItemAssociationsInputDoc = YFCDocument
							.createDocument("XPXItemAssociations");
					getItemAssociationsInputDoc.getDocumentElement()
							.setAttribute("AssociatedItemID",
									eleWMAssoItem.getAttribute("ItemNumber"));
					getItemAssociationsInputDoc.getDocumentElement()
							.setAttribute("AssociationType", chdRelationType);
					getItemAssociationsInputDoc.getDocumentElement()
							.setAttribute("ItemExtnKey", ItemExtnKey);

					Document getItemAssociationsDoc = api.executeFlow(env,
							"getXPXItemBranchAssociationListService",
							getItemAssociationsInputDoc.getDocument());

					if (getItemAssociationsDoc.getDocumentElement()
							.getElementsByTagName("XPXItemAssociations")
							.item(0) == null) {
						throw new YFSException("Invalid Item Association.",
								"ERROR_LOAD_03",
								"Invalid Item Association Details passed in Item Branch Header.");
					}
					/** ******************************************************************************************* */
				} else {
					// invalid Item Branch
					throw new YFSException("Invalid Item Branch.",
							"ERROR_LOAD_02",
							"Invalid Item Details passed in Item Branch Header.");
				}
			}

			eleWMAssoItem = null;
		}
		eleItemAssociations = null;
	}

	private void copyItemBranchAttrs(YFSEnvironment env,
			Element eleWMItemBranch, Element eleItemExtn) throws YFSException,
			RemoteException {
		eleItemExtn.setAttribute("EnvironmentID", eleWMItemBranch
				.getAttribute("EnvironmentId"));
		eleItemExtn.setAttribute("CompanyCode", eleWMItemBranch
				.getAttribute("CompanyCode"));
		eleItemExtn.setAttribute("ItemID", eleWMItemBranch
				.getAttribute("LegacyProductNumber"));
		Document docItemDtls = this.getItemDetails(env, eleWMItemBranch
				.getAttribute("LegacyProductNumber"));
		NodeList nlItem = SCXmlUtil.getXpathNodes(docItemDtls
				.getDocumentElement(), "/ItemList/Item");
		if (nlItem.getLength() > 0) {
			eleItemExtn.setAttribute("ItemKey", ((Element) nlItem.item(0))
					.getAttribute("ItemKey"));
		} else {
			// invalid Item
			throw new YFSException("Invalid Legacy Product Number.",
					"ERROR_LOAD_01", "Invalid Legacy Product Number:["
							+ eleWMItemBranch
									.getAttribute("LegacyProductNumber")
							+ "] passed in Item Branch Header.");
		}
		if ("A".equals(eleWMItemBranch.getAttribute("ProcessCode"))) {
			eleItemExtn.setAttribute("Operation", "Create");
		} else if ("C".equals(eleWMItemBranch.getAttribute("ProcessCode"))
				|| "X".equals(eleWMItemBranch.getAttribute("ProcessCode"))) {
			eleItemExtn.setAttribute("Operation", "Modify");
		} else if ("D".equals(eleWMItemBranch.getAttribute("ProcessCode"))) {
			eleItemExtn.setAttribute("Operation", "Delete");
		}

		eleItemExtn.setAttribute("MasterProductCode", eleWMItemBranch
				.getAttribute("MasterProductCode"));
		eleItemExtn.setAttribute("XPXDivision", eleWMItemBranch
				.getAttribute("DivisionNumber"));

		/**
		 * *************Commented out as per modified design of ItemBranch
		 * feed******************************
		 */
		/*
		 * eleItemExtn.setAttribute("CustomerID",
		 * eleWMItemBranch.getAttribute("CustomerNumber"));
		 * if(YFCCommon.isVoid(eleWMItemBranch.getAttribute("ProductStatus"))){
		 * eleItemExtn.setAttribute("ItemStatus", "A"); } else {
		 * eleItemExtn.setAttribute("ItemStatus",
		 * eleWMItemBranch.getAttribute("ProductStatus")); }
		 */
		/** ************************************************************************************************** */
		if (!"X".equals(eleWMItemBranch.getAttribute("ProcessCode"))) {
			eleItemExtn.setAttribute("MasterProductCode", eleWMItemBranch
					.getAttribute("MasterProductCode"));
			eleItemExtn.setAttribute("ItemStockStatus", eleWMItemBranch
					.getAttribute("ItemStockStatus"));
			if (!YFCCommon
					.isVoid(eleWMItemBranch.getAttribute("OrderMultiple"))) {
				eleItemExtn.setAttribute("OrderMultiple", eleWMItemBranch
						.getAttribute("OrderMultiple"));
			}

					

		   /***Start of  Modified code for Change request 3079****/
		eleItemExtn.setAttribute("OriginalIndicator", eleWMItemBranch
					.getAttribute("InventoryIndicator"));
					
		   /***End of  Modified code for Change request 3079****/
			if("A".equals(eleWMItemBranch.getAttribute("ProcessCode"))){
				eleItemExtn.setAttribute("InventoryIndicator", eleWMItemBranch
					.getAttribute("InventoryIndicator"));
			}


			eleItemExtn.setAttribute("CompanyCode", eleWMItemBranch
					.getAttribute("CompanyCode"));
		}

	}

	private Document getItemDetails(YFSEnvironment env, String strItemID)
			throws YFSException, RemoteException {
		Document docInput = SCXmlUtil.createFromString("<Item ItemID='"
				+ strItemID + "' OrganizationCode='"
				+ XPXLiterals.A_ORGANIZATIONCODE_VALUE + "'/>");
		env
				.setApiTemplate(
						"getItemList",
						SCXmlUtil
								.createFromString("<ItemList><Item ItemID='' ItemKey=''/></ItemList>"));
		Document returnDoc = api.invoke(env, "getItemList", docInput);
		env.clearApiTemplate("getItemList");
		return returnDoc;
	}

}