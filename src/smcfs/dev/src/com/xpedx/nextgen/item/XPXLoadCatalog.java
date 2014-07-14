package com.xpedx.nextgen.item;

//Ajit import java.util.HashMap;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNode;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;
import com.yantra.yfc.log.YFCLogCategory;

public class XPXLoadCatalog implements YIFCustomApi {

	private static YFCLogCategory log = (YFCLogCategory) YFCLogCategory
	.getLogger("com.xpedx.nextgen.log");

	private YIFApi api = null;

	private static String _ATTR_GROUP_ID = "xpedx";

	private static String _ORG_CODE = "xpedx";

	private static String _RESPONSE_MSG_SERVICE = "xpedxSendItemFeedResponse";
	
	Element eItem = null;

	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	public Document invoke(YFSEnvironment env, Document inXML) throws Exception {
		try {
			api = YIFClientFactory.getInstance().getApi();
			int iCat = 0;
			YFCDocument modifyCategoryItemsDoc = YFCDocument
					.createDocument("ModifyCategoryItems");
			YFCElement eModifyCategoryItems = modifyCategoryItemsDoc
					.getDocumentElement();
			eModifyCategoryItems.setAttribute("CallingOrganizationCode",
					_ORG_CODE);
			log.debug("Input XML in the invoke method of XPXLoadCatalog :" + SCXmlUtil.getString(inXML));
			Element eItemList = inXML.getDocumentElement();

			NodeList nlItems = eItemList.getElementsByTagName("Item");
			for (int i = 0; i < nlItems.getLength(); i++) {
				eItem = (Element) nlItems.item(i);

				ArrayList<HashMap<String, String>> alCategoryDtls = getCategoryAssociations(
						env, eItem);

				// update the valid values for attributes
				/*
				 * NodeList nlAddnlAttrList =
				 * eItem.getElementsByTagName("AdditionalAttribute"); for(int
				 * j=0; j<nlAddnlAttrList.getLength(); j++) { Element
				 * eAddnlAttr = (Element)nlAddnlAttrList.item(j); String strName =
				 * eAddnlAttr.getAttribute("Name"); String strValue =
				 * eAddnlAttr.getAttribute("Value");
				 * 
				 * updateValidValueForAttribute(env, strName, strValue); }
				 */
				// update the modifyCategoryItem input and remove CategoryList
				// element from the inXML
				iCat = addItemToCategory(env, modifyCategoryItemsDoc, eItem,
						alCategoryDtls);

				/*
				 * Ajit: Temporary Fix to truncate item shortdescription field
				 * START
				 */

				NodeList primInfoNL = eItem
						.getElementsByTagName("PrimaryInformation");
				Element primInfoEle = (Element) primInfoNL.item(0);
				String shortDescription = primInfoEle
						.getAttribute("ShortDescription");
				String trunShortDescription = "";
				int len = shortDescription.length();
				if (len >= 100) {
					trunShortDescription = shortDescription.substring(0, 98);
				}
				primInfoEle.setAttribute("ShortDescription",
						trunShortDescription);
				primInfoEle.setAttribute("ExtendedDescription",
						shortDescription);

				/*
				 * Ajit: Temporary Fix to truncate item shortdescription field
				 * END
				 */
				if(log.isDebugEnabled()){
					log.debug("Before Manage Item:" + eItem.getAttribute("ItemID"));
				}
			}

			// create the item
			api.invoke(env, "manageItem", inXML);
			if(log.isDebugEnabled()){
				log.debug("Before Manage Item Successfull");
			}
			// attach the item to the category tree

			if(log.isDebugEnabled()){
				log.debug("modifyCategoryItem :" + modifyCategoryItemsDoc.toString());
			}
			if (iCat > 0) {
				api.invoke(env, "modifyCategoryItem", modifyCategoryItemsDoc
						.getDocument());
			}
			if(log.isDebugEnabled()){
				log.debug("Before Manage Item Successfull");
			}
			// post a message to the response queue
			generateResponse(env, inXML, "SUCCESS", null);

		}
		/**
		 * catch block updated by Arun Sekhar on 27-Jan-2011 for CENT tool
		 * integration *
		 */
		/*
		 * catch(Exception ex) { log.debug("Exception while loading Items",ex);
		 * ex.printStackTrace(); generateResponse(env, inXML, "FAIL", ex); }
		 */
		catch (RemoteException re) {
			log.error("------------Failed XML Needs to Catch for Re-Processing XML START ----------");
			log.error(SCXmlUtil.getString(eItem));
			log.error("------------Failed XML Needs to Catch for Re-Processing XML END ----------");
			log.error("NullPointerException: " + re.getStackTrace());
			prepareErrorObject(re, XPXLiterals.CD_ITEM_TRANS_TYPE,
					XPXLiterals.NE_ERROR_CLASS, env, inXML);
			generateResponse(env, inXML, "FAIL", re);
			return inXML;
		} catch (NullPointerException ne) {
			log.error("------------Failed XML Needs to Catch for Re-Processing XML START ----------");
			log.error(SCXmlUtil.getString(eItem));
			log.error("------------Failed XML Needs to Catch for Re-Processing XML END ----------");
			log.error("NullPointerException: " + ne.getStackTrace());
			prepareErrorObject(ne, XPXLiterals.CD_ITEM_TRANS_TYPE,
					XPXLiterals.NE_ERROR_CLASS, env, inXML);
			generateResponse(env, inXML, "FAIL", ne);
			return inXML;
		} catch (YFSException yfe) {
			log.error("------------Failed XML Needs to Catch for Re-Processing XML START ----------");
			log.error(SCXmlUtil.getString(eItem));
			log.error("------------Failed XML Needs to Catch for Re-Processing XML END ----------");
			log.error("YFSException: " + yfe.getStackTrace());
			prepareErrorObject(yfe, XPXLiterals.CD_ITEM_TRANS_TYPE,
					XPXLiterals.YFE_ERROR_CLASS, env, inXML);
			generateResponse(env, inXML, "FAIL", yfe);
			return inXML;
		} catch (Exception e) {
			log.error("------------Failed XML Needs to Catch for Re-Processing XML START ----------");
			log.error(SCXmlUtil.getString(eItem));
			log.error("------------Failed XML Needs to Catch for Re-Processing XML END ----------");
			log.error("Exception: " + e.getStackTrace());
			prepareErrorObject(e, XPXLiterals.CD_ITEM_TRANS_TYPE,
					XPXLiterals.E_ERROR_CLASS, env, inXML);
			generateResponse(env, inXML, "FAIL", e);
			return inXML;
		}
		return inXML;
	}

	/**
	 * Added by Arun Sekhar on 27-Jan-2011 for CENT tool integration
	 * 
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

	private void generateResponse(YFSEnvironment env, Document inXML,
			String strStatus, Exception ex) throws Exception {
		String strCorrelationID = obtainCatalogMsgCorrelationID(inXML);
		YFCDocument responseDoc = YFCDocument
				.createDocument("xcomg2ItemResponse");
		YFCElement eItemResponse = responseDoc.getDocumentElement();
		YFCElement eItemResultList = responseDoc
				.createElement("ItemResultList");
		eItemResponse.appendChild(eItemResultList);

		YFCElement eItemResult = responseDoc.createElement("ItemResult");
		eItemResultList.appendChild(eItemResult);

		YFCElement eErrorList = responseDoc.createElement("ErrorList");
		eItemResult.appendChild(eErrorList);

		YFCElement eError = responseDoc.createElement("Error");
		eErrorList.appendChild(eError);

		if ("FAIL".equalsIgnoreCase(strStatus)) {
			eItemResult.setAttribute("transactionSuccessful", "N");
			String strErrMsg = ex.getMessage();
			YFCNode nErrorText = responseDoc.createTextNode(strErrMsg);
			eError.appendChild(nErrorText);
		} else {
			eItemResult.setAttribute("transactionSuccessful", "Y");
		}

		eItemResult.setAttribute("ItemIdentifier", strCorrelationID);

		if ("".equals(_RESPONSE_MSG_SERVICE)) {
			// log.debug("ResponseDoc:" + responseDoc);
		} else {
			api.executeFlow(env, _RESPONSE_MSG_SERVICE, responseDoc
					.getDocument());
		}
		if(log.isDebugEnabled()){
			log.debug("ResponseMsg_xcomg2ItemResponse" + responseDoc.toString());
		}

	}

	private String obtainCatalogMsgCorrelationID(Document inXML) {
		// TODO Auto-generated method stub
		Element eItemList = inXML.getDocumentElement();
		NodeList nlItem = eItemList.getElementsByTagName("Item");

		Element eItem = (Element) nlItem.item(0);
		String strItemID = eItem.getAttribute("ItemID");

		return strItemID;
	}

	private int addItemToCategory(YFSEnvironment env,
			YFCDocument modifyCategoryItemsDoc, Element eItem,
			ArrayList<HashMap<String, String>> alCategoryAssociations)
			throws Exception {

		int counter = 0;
		YFCElement eModifyCategoryItems = modifyCategoryItemsDoc
				.getDocumentElement();

		Element eCategoryList = (Element) eItem.getElementsByTagName(
				"CategoryList").item(0);

		String strItemID = eItem.getAttribute("ItemID");
		String strUOM = eItem.getAttribute("UnitOfMeasure");
		String strOrganizationCode = eItem.getAttribute("OrganizationCode");

		NodeList nlCategoryList = eItem.getElementsByTagName("Category");
		for (int k = 0; k < nlCategoryList.getLength(); k++) {
			Element eCategory = (Element) nlCategoryList.item(k);
			String strCatPath = eCategory.getAttribute("CategoryPath");
			String strOrgCode = eCategory.getAttribute("OrganizationCode");

			if (!assocatedToCategory(strCatPath, strOrgCode,
					alCategoryAssociations)) {
				counter++;
				YFCElement eCategoryToModify = modifyCategoryItemsDoc
						.createElement("Category");
				eCategoryToModify.setAttribute("CategoryPath", strCatPath);
				eCategoryToModify.setAttribute("OrganizationCode", strOrgCode);

				YFCElement eCategoryItemList = modifyCategoryItemsDoc
						.createElement("CategoryItemList");

				YFCElement eCategoryItem = modifyCategoryItemsDoc
						.createElement("CategoryItem");
				eCategoryItem.setAttribute("Action", "Create");
				eCategoryItem.setAttribute("ItemID", strItemID);
				eCategoryItem.setAttribute("UnitOfMeasure", strUOM);
				eCategoryItem.setAttribute("OrganizationCode",
						strOrganizationCode);

				eModifyCategoryItems.appendChild(eCategoryToModify);
				eCategoryToModify.appendChild(eCategoryItemList);
				eCategoryItemList.appendChild(eCategoryItem);
			}

		}
		
		for (int j = 0; j < alCategoryAssociations.size(); j++) {
			HashMap<String, String> hmCat = alCategoryAssociations.get(j);
			String sMatched = hmCat.get("Matched");
			String strCatPath = hmCat.get("CategoryPath");
			String strOrgCode = hmCat.get("OrganizationCode");

			if ("Y".equals(sMatched)) {
				continue;
			} else {
				// these are the associations which need to be removed
				counter++;
				if(log.isDebugEnabled()){
					log.debug("strCatPath|strOrgCode:" + strCatPath + "|" + strOrgCode);
				}
				YFCElement eCategoryToModify = modifyCategoryItemsDoc
						.createElement("Category");
				eCategoryToModify.setAttribute("CategoryPath", strCatPath);
				eCategoryToModify.setAttribute("OrganizationCode", strOrgCode);

				YFCElement eCategoryItemList = modifyCategoryItemsDoc
						.createElement("CategoryItemList");

				YFCElement eCategoryItem = modifyCategoryItemsDoc
						.createElement("CategoryItem");
				eCategoryItem.setAttribute("Action", "Delete");
				eCategoryItem.setAttribute("ItemID", strItemID);
				eCategoryItem.setAttribute("UnitOfMeasure", strUOM);
				eCategoryItem.setAttribute("OrganizationCode",
						strOrganizationCode);

				eModifyCategoryItems.appendChild(eCategoryToModify);
				eCategoryToModify.appendChild(eCategoryItemList);
				eCategoryItemList.appendChild(eCategoryItem);
			}

		}
		if(log.isDebugEnabled()){
			log.debug("modifyCategoryItemsDoc:" + modifyCategoryItemsDoc.toString());
		}
		eItem.removeChild(eCategoryList);
		return counter;

	}

	private boolean assocatedToCategory(String sCatPath, String sOrgCode,
			ArrayList<HashMap<String, String>> alCategoryAssociations) {
		boolean bAssociated = false;
		for (int i = 0; i < alCategoryAssociations.size(); i++) {
			HashMap<String, String> hmCat = alCategoryAssociations.get(i);
			String sCatPathAssociated = hmCat.get("CategoryPath");
			String sOrgCodeAssociated = hmCat.get("OrganizationCode");

			if (sCatPathAssociated.equals(sCatPath)
					&& sOrgCodeAssociated.equals(sOrgCode)) {
				bAssociated = true;
				hmCat.put("Matched", "Y");
				break;
			}
		}
		if(log.isDebugEnabled()){
			log.debug("Category Path: " + sCatPath + "|" + bAssociated);
		}
		return bAssociated;
	}

	private void updateValidValueForAttribute(YFSEnvironment env,
			String sAttrName, String sItemAttrValue) throws Exception {

		/*
		 * HashMap <String, String>hmValidAttrValue = new HashMap<String,
		 * String>(); log.debug("Setting " + sItemAttrValue + " as
		 * valid value for " + sAttrName);
		 * 
		 * YFCDocument getAttrListInDoc =
		 * YFCDocument.createDocument("Attribute"); YFCElement eAttributeIn =
		 * getAttrListInDoc.getDocumentElement();
		 * eAttributeIn.setAttribute("AttributeDomainID", "ItemAttribute");
		 * eAttributeIn.setAttribute("AttributeGroupID", _ATTR_GROUP_ID);
		 * eAttributeIn.setAttribute("AttributeID", sAttrName);
		 * eAttributeIn.setAttribute("CallingOrganizationCode", _ORG_CODE);
		 * 
		 * //log.debug("getAttrListInDoc:" + getAttrListInDoc);
		 * 
		 * //set template for getAttributeList YFCDocument getAttrListTemp =
		 * YFCDocument.createDocument("AttributeList"); YFCElement eAttrListTemp =
		 * getAttrListTemp.getDocumentElement(); YFCElement eAttrTemp =
		 * getAttrListTemp.createElement("Attribute");
		 * eAttrListTemp.appendChild(eAttrTemp);
		 * 
		 * eAttrTemp.setAttribute("AttributeID", "");
		 * eAttrTemp.setAttribute("AttributeKey", "");
		 * 
		 * AjitYFCElement eAttAllowedValueListTemp =
		 * getAttrListTemp.createElement("AttributeAllowedValueList");
		 * eAttrTemp.appendChild(eAttAllowedValueListTemp);
		 * 
		 * YFCElement eAttAllowedValueTemp =
		 * getAttrListTemp.createElement("AttributeAllowedValue");
		 * eAttAllowedValueListTemp.appendChild(eAttAllowedValueTemp);
		 * 
		 * eAttAllowedValueTemp.setAttribute("Value", "");
		 * eAttAllowedValueTemp.setAttribute("ShortDescription", ""); Ajit
		 * //log.debug("docAttrList:" + getAttrListTemp);
		 * 
		 * env.setApiTemplate("getAttributeList",
		 * getAttrListTemp.getDocument());
		 * 
		 * 
		 * Document docAttrList = api.invoke(env, "getAttributeList",
		 * getAttrListInDoc.getDocument());
		 * 
		 * env.clearApiTemplate("getAttributeList"); log.debug("docAttrList :" +
		 * SCXmlUtils.getString(docAttrList));
		 * 
		 * if("Y".equals(sVerbose)) { //log.debug("docAttrList:" +
		 * YFCDocument.getDocumentFor(docAttrList)); }
		 * 
		 * Element eAttrListOut = docAttrList.getDocumentElement(); Element
		 * eAttrOut =
		 * (Element)eAttrListOut.getElementsByTagName("Attribute").item(0);
		 * String sAttrKey = eAttrOut.getAttribute("AttributeKey");
		 * 
		 * NodeList nlAttrValues =
		 * eAttrListOut.getElementsByTagName("AttributeAllowedValue"); for(int
		 * i=0; i<nlAttrValues.getLength(); i++) { Element eAttrValueOut =
		 * (Element)nlAttrValues.item(i); String sAttrValue =
		 * eAttrValueOut.getAttribute("Value"); String sAttrShortDesc =
		 * eAttrValueOut.getAttribute("ShortDescription");
		 * hmValidAttrValue.put(sAttrValue, sAttrShortDesc); }
		 * 
		 * 
		 * if("Y".equals(sVerbose)) { //log.debug("hmValidAttrValue:" +
		 * hmValidAttrValue); }
		 * 
		 * 
		 * if(!hmValidAttrValue.containsKey(sItemAttrValue)) {
		 * //log.debug("adding " + sItemAttrValue); //update
		 */

		YFCDocument manageAttributeIn = YFCDocument
				.createDocument("AttributeList");
		YFCElement eAttributeListIn = manageAttributeIn.getDocumentElement();
		YFCElement eManageAttributeIn = manageAttributeIn
				.createElement("Attribute");
		eAttributeListIn.appendChild(eManageAttributeIn);
		eManageAttributeIn.setAttribute("AttributeDomainID", "ItemAttribute");
		eManageAttributeIn.setAttribute("AttributeGroupID", _ATTR_GROUP_ID);
		eManageAttributeIn.setAttribute("AttributeID", sAttrName);
		eManageAttributeIn.setAttribute("OrganizationCode", _ORG_CODE);

		YFCElement eAttrAllowedValueList = manageAttributeIn
				.createElement("AttributeAllowedValueList");
		eManageAttributeIn.appendChild(eAttrAllowedValueList);

		YFCElement eAttrAllowedValue = manageAttributeIn
				.createElement("AttributeAllowedValue");
		eAttrAllowedValueList.appendChild(eAttrAllowedValue);

		eAttrAllowedValue.setAttribute("LongDescription", sItemAttrValue);
		eAttrAllowedValue.setAttribute("ShortDescription", sItemAttrValue);
		eAttrAllowedValue.setAttribute("Value", sItemAttrValue);
		eAttrAllowedValue.setAttribute("Operation", "Manage");

		if(log.isDebugEnabled()){
			log.debug("manageAttributeIn:" + manageAttributeIn.toString());
		}
		if(log.isVerboseEnabled()){
			log.verbose("ManageAttribute :" + manageAttributeIn.getDocument());
		}

		YFCDocument manageAttrTemp = YFCDocument
				.createDocument("AttributeList");
		YFCElement eAttrListTemp = manageAttrTemp.getDocumentElement();
		YFCElement eAttrTemp = manageAttrTemp.createElement("Attribute");
		eAttrTemp.setAttribute("AttributeID", "");

		eAttrListTemp.appendChild(eAttrTemp);

		env.setApiTemplate("manageAttribute", manageAttrTemp.getDocument());
		api.invoke(env, "manageAttribute", manageAttributeIn.getDocument());
		env.clearApiTemplate("manageAttribute");

		/* } */

		// Ajit}
	}

	private ArrayList<HashMap<String, String>> getCategoryAssociations(
			YFSEnvironment env, Element eItem) throws Exception {

		String sItemID = eItem.getAttribute("ItemID");
		String sUOM = eItem.getAttribute("UnitOfMeasure");
		String sOrgCode = eItem.getAttribute("OrganizationCode");
		// sayan call getItemList to get the list of associated categories

		YFCDocument getItemListInDoc = YFCDocument.createDocument("Item");
		YFCElement eItemIn = getItemListInDoc.getDocumentElement();

		eItemIn.setAttribute("ItemID", sItemID);
		eItemIn.setAttribute("OrganizationCode", sOrgCode);
		eItemIn.setAttribute("UnitOfMeasure", sUOM);

		YFCDocument getItemListTemplate = YFCDocument
				.createDocument("ItemList");
		YFCElement eItemListTemplate = getItemListTemplate.getDocumentElement();
		YFCElement eItemTemplate = getItemListTemplate.createElement("Item");
		YFCElement eCategoryListTemplate = getItemListTemplate
				.createElement("CategoryList");
		YFCElement eCategoryTemplate = getItemListTemplate
				.createElement("Category");

		eItemListTemplate.appendChild(eItemTemplate);
		eItemTemplate.appendChild(eCategoryListTemplate);
		eCategoryListTemplate.appendChild(eCategoryTemplate);

		eItemTemplate.setAttribute("ItemID", "");
		eItemTemplate.setAttribute("UnitOfMeasure", "");
		eItemTemplate.setAttribute("OrganizationCode", "");

		eCategoryTemplate.setAttribute("CategoryID", "");
		eCategoryTemplate.setAttribute("CategoryPath", "");
		eCategoryTemplate.setAttribute("OrganizationCode", "");

		env.setApiTemplate("getItemList", getItemListTemplate.getDocument());
		Document itemListOutDoc = api.invoke(env, "getItemList",
				getItemListInDoc.getDocument());
		env.clearApiTemplate("getItemList");

		Element eItemListOut = itemListOutDoc.getDocumentElement();
		NodeList nlCategory = eItemListOut.getElementsByTagName("Category");

		ArrayList<HashMap<String, String>> alCategory = new ArrayList();
		for (int i = 0; i < nlCategory.getLength(); i++) {
			Element eCategory = (Element) nlCategory.item(i);
			String sCategoryID = eCategory.getAttribute("CategoryID");
			String sCategoryPath = eCategory.getAttribute("CategoryPath");
			String sOrganizationCode = eCategory
					.getAttribute("OrganizationCode");

			HashMap<String, String> hmCategoryDtl = new HashMap();
			hmCategoryDtl.put("CategoryID", sCategoryID);
			hmCategoryDtl.put("CategoryPath", sCategoryPath);
			hmCategoryDtl.put("OrganizationCode", sOrganizationCode);
			if(log.isDebugEnabled()){
				log.debug("CategoryID|CategoryPath|OrganizationCode:" + sCategoryID + "|" + sCategoryPath + "|" + sOrganizationCode);
			}
			alCategory.add(hmCategoryDtl);

		}
		
		return alCategory;
	}

}
