package com.xpedx.nextgen.item;

//Ajit import java.util.HashMap;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

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
import com.yantra.yfc.util.YFCCommon;
import com.xpedx.nextgen.common.util.XPXCatalogDataProcessor;

public class XPXLoadCatalog2 implements YIFCustomApi {

	private static YFCLogCategory log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	private YIFApi api = null;
	private static String _ATTR_GROUP_ID = "xpedx";
	private static String _ORG_CODE = "xpedx";
	private static String _RESPONSE_MSG_SERVICE = "xpedxSendItemFeedResponse";

	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	public Document invoke(YFSEnvironment env, Document inXML) throws Exception
	{
		Document outXML = null;
		try
		{
			api = YIFClientFactory.getInstance().getApi();
			int iCat = 0;
			YFCDocument modifyCategoryItemsDoc = YFCDocument.createDocument("ModifyCategoryItems");
			YFCElement eModifyCategoryItems = modifyCategoryItemsDoc.getDocumentElement();
			eModifyCategoryItems.setAttribute("CallingOrganizationCode", _ORG_CODE);
			if(inXML != null){
				log.debug("Input to XPXLoadCatalog : " + SCXmlUtil.getString(inXML));
				System.out.println("Item Load xml"+SCXmlUtil.getString(inXML));
			}
			Element eItemList = inXML.getDocumentElement();

			NodeList nlItems = eItemList.getElementsByTagName("Item");
			for(int i=0; i< nlItems.getLength(); i++)
			{
				Element eItem = (Element)nlItems.item(i);

				ArrayList<HashMap<String, String>> alCategoryDtls = getCategoryAssociations(env, eItem);


				//check if it's a delete operation;change the flow
				String actionName = SCXmlUtil.getAttribute(eItem, "Action");
				if(!YFCCommon.isVoid(actionName) && (actionName.equalsIgnoreCase("Delete"))){

					//delete the category association
					Element eCategoryList = (Element)eItem.getElementsByTagName("CategoryList").item(0);

					eItem.removeChild(eCategoryList);
					Element textNode = SCXmlUtil.createChild(eItem, "CategoryList");
					Text txt = eItem.getOwnerDocument().createTextNode(null);
					textNode.appendChild(txt);

					iCat = addItemToCategory(env, modifyCategoryItemsDoc, eItem, alCategoryDtls);
					if(log.isDebugEnabled()){
						log.debug("modifyCategoryItem for delete operation :" + modifyCategoryItemsDoc.toString());
					}

					api.invoke(env, "modifyCategoryItem", modifyCategoryItemsDoc.getDocument());
					//delete the item
					if(log.isDebugEnabled()){
						log.debug("manageItem for delete operation :" + SCXmlUtil.getString(inXML));
					}

					api.invoke(env, "manageItem", inXML);
					//generate the response
					outXML = generateResponse(env, inXML, "SUCCESS", null);
					return outXML;
				}

				else if(!YFCCommon.isVoid(actionName) && (actionName.equalsIgnoreCase("Manage"))){

					Element eExtnList = (Element)eItem.getElementsByTagName("Extn").item(0);
					
					if(eExtnList !=null){				

					String strShortDesc = eExtnList.getAttribute("ExtnShortDescription");
					String strDesc =eExtnList.getAttribute("ExtnDescription");
					String strExtendedDesc = eExtnList.getAttribute("ExtnExtendedDesc");
					strShortDesc =	XPXCatalogDataProcessor.preprocessCatalogData(strShortDesc);
					strDesc = XPXCatalogDataProcessor.preprocessCatalogData(strDesc);
					strExtendedDesc =XPXCatalogDataProcessor.preprocessCatalogData(strExtendedDesc);
					
					eExtnList.setAttribute("ExtnShortDescription", strShortDesc);
					eExtnList.setAttribute("ExtnDescription", strDesc);
					eExtnList.setAttribute("ExtnExtendedDesc", strExtendedDesc);

					}
					
				//	System.out.println("Item Load xml"+SCXmlUtil.getString(inXML));

				}




				//update the valid values for attributes
				/*
				NodeList nlAddnlAttrList = eItem.getElementsByTagName("AdditionalAttribute");
				for(int j=0; j<nlAddnlAttrList.getLength(); j++)
				{
					Element eAddnlAttr = (Element)nlAddnlAttrList.item(j);
					String strName = eAddnlAttr.getAttribute("Name");
					String strValue = eAddnlAttr.getAttribute("Value");

					updateValidValueForAttribute(env, strName, strValue);
				}
				 */

				//update the modifyCategoryItem input and remove CategoryList element from the inXML
				iCat = addItemToCategory(env, modifyCategoryItemsDoc, eItem, alCategoryDtls);

				/* Ajit: Temporary Fix to truncate item shortdescription field START 

				NodeList primInfoNL = eItem.getElementsByTagName("PrimaryInformation");
				Element primInfoEle =(Element)primInfoNL.item(0);
				String shortDescription = primInfoEle.getAttribute("ShortDescription");
				String trunShortDescription = "";
				int len = shortDescription.length();
				if (len>=100){
					trunShortDescription = shortDescription.substring(0, 98);
				}
				primInfoEle.setAttribute("ShortDescription", trunShortDescription);
				primInfoEle.setAttribute("ExtendedDescription", shortDescription);

				 Ajit: Temporary Fix to truncate item shortdescription field END */
				if(log.isDebugEnabled()){
					log.debug("Before Manage Item:" + eItem.getAttribute("ItemID") );
				}
			}

			//create the item

			api.invoke(env, "manageItem", inXML);
			if(log.isDebugEnabled()){
				log.debug("Before Manage Item Successfull" );
			}
			//attach the item to the category tree

			if(log.isDebugEnabled()){
				log.debug("modifyCategoryItem :" + modifyCategoryItemsDoc.toString());
			}
			if(iCat > 0)
			{
				api.invoke(env, "modifyCategoryItem", modifyCategoryItemsDoc.getDocument());
			}
			if(log.isDebugEnabled()){
				log.debug("Before Manage Item Successfull" );
			}
			//post a message to the response queue
			outXML = generateResponse(env, inXML, "SUCCESS", null);

		}

		/**
		 * catch block updated by Arun Sekhar for CENT tool
		 * integration *
		 */
		/*		catch(Exception ex)
		{
			log.debug("Exception while loading Items",ex);
			ex.printStackTrace();
			outXML = generateResponse(env, inXML, "FAIL", ex);
		}*/

		catch (RemoteException re) {
			log.error("NullPointerException: " + re.getStackTrace());
			prepareErrorObject(re, XPXLiterals.CD_ITEM_TRANS_TYPE,
					XPXLiterals.NE_ERROR_CLASS, env, inXML);
			outXML = generateResponse(env, inXML, "FAIL", re);
			return outXML;
		} catch (NullPointerException ne) {
			log.error("NullPointerException: " + ne.getStackTrace());
			prepareErrorObject(ne, XPXLiterals.CD_ITEM_TRANS_TYPE,
					XPXLiterals.NE_ERROR_CLASS, env, inXML);
			outXML = generateResponse(env, inXML, "FAIL", ne);
			return outXML;
		} catch (YFSException yfe) {
			log.error("YFSException: " + yfe.getStackTrace());
			prepareErrorObject(yfe, XPXLiterals.CD_ITEM_TRANS_TYPE,
					XPXLiterals.YFE_ERROR_CLASS, env, inXML);
			outXML = generateResponse(env, inXML, "FAIL", yfe);
			return outXML;
		} catch (Exception e) {
			log.error("Exception: " + e.getStackTrace());
			prepareErrorObject(e, XPXLiterals.CD_ITEM_TRANS_TYPE,
					XPXLiterals.E_ERROR_CLASS, env, inXML);
			outXML = generateResponse(env, inXML, "FAIL", e);
			return outXML;
		}	
		return outXML;
	}

	/**
	 * Added by Arun Sekhar for CENT tool integration
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

	private Document generateResponse(YFSEnvironment env, Document inXML, String strStatus, Exception ex)
	throws Exception
	{
		String strCorrelationID = obtainCatalogMsgCorrelationID(inXML);
		YFCDocument responseDoc = YFCDocument.createDocument("xcomg2ItemResponse");
		YFCElement eItemResponse = responseDoc.getDocumentElement();
		YFCElement eItemResultList = responseDoc.createElement("ItemResultList");
		eItemResponse.appendChild(eItemResultList);

		YFCElement eItemResult = responseDoc.createElement("ItemResult");
		eItemResultList.appendChild(eItemResult);

		YFCElement eErrorList = responseDoc.createElement("ErrorList");
		eItemResult.appendChild(eErrorList);

		YFCElement eError = responseDoc.createElement("Error");
		eErrorList.appendChild(eError);

		if("FAIL".equalsIgnoreCase(strStatus))
		{
			eItemResult.setAttribute("transactionSuccessful", "N");
			String strErrMsg = ex.getMessage();
			YFCNode nErrorText = responseDoc.createTextNode(strErrMsg);
			eError.appendChild(nErrorText);
		}
		else
		{
			eItemResult.setAttribute("transactionSuccessful", "Y");
		}

		eItemResult.setAttribute("ItemIdentifier", strCorrelationID);


		/*if("".equals(_RESPONSE_MSG_SERVICE))
		{
			//log.debug("responseDoc:" + responseDoc);
		}
		else
		{
			api.executeFlow(env, _RESPONSE_MSG_SERVICE, responseDoc.getDocument());
		}*/
		if(log.isDebugEnabled()){
			log.debug("Response Msg in XPXLoadCatalog:" + responseDoc.toString());
		}
		return responseDoc.getDocument();
	}


	private String obtainCatalogMsgCorrelationID(Document inXML) 
	{
		// TODO Auto-generated method stub
		Element eItemList = inXML.getDocumentElement();
		NodeList nlItem = eItemList.getElementsByTagName("Item");

		Element eItem = (Element)nlItem.item(0);
		String strItemID = eItem.getAttribute("ItemID");

		return strItemID;
	}


	private int addItemToCategory(YFSEnvironment env, YFCDocument modifyCategoryItemsDoc, Element eItem, ArrayList <HashMap <String, String>> alCategoryAssociations)
	throws Exception
	{
		if(log.isDebugEnabled()){
			log.debug("Modified item XML for delete testing: \n"+SCXmlUtil.getString(eItem));
		}
		int counter = 0;
		YFCElement eModifyCategoryItems = modifyCategoryItemsDoc.getDocumentElement();

		Element eCategoryList = (Element)eItem.getElementsByTagName("CategoryList").item(0);

		String strItemID = eItem.getAttribute("ItemID");
		String strUOM = eItem.getAttribute("UnitOfMeasure");
		String strOrganizationCode = eItem.getAttribute("OrganizationCode");

		NodeList nlCategoryList = eItem.getElementsByTagName("Category");
		for(int k=0; k<nlCategoryList.getLength();k++)
		{
			Element eCategory = (Element)nlCategoryList.item(k);
			String strCatPath = eCategory.getAttribute("CategoryPath");
			String strOrgCode = eCategory.getAttribute("OrganizationCode");
			if(log.isDebugEnabled()){
				log.debug("Adding item to category.");
			}
			if(!assocatedToCategory(strCatPath, strOrgCode, alCategoryAssociations))
			{
				counter++;
				YFCElement eCategoryToModify = modifyCategoryItemsDoc.createElement("Category");
				eCategoryToModify.setAttribute("CategoryPath", strCatPath);
				eCategoryToModify.setAttribute("OrganizationCode", strOrgCode);

				YFCElement eCategoryItemList = modifyCategoryItemsDoc.createElement("CategoryItemList");

				YFCElement eCategoryItem = modifyCategoryItemsDoc.createElement("CategoryItem");
				eCategoryItem.setAttribute("Action", "Create");
				eCategoryItem.setAttribute("ItemID", strItemID);
				eCategoryItem.setAttribute("UnitOfMeasure", strUOM);
				eCategoryItem.setAttribute("OrganizationCode", strOrganizationCode);

				eModifyCategoryItems.appendChild(eCategoryToModify);
				eCategoryToModify.appendChild(eCategoryItemList);
				eCategoryItemList.appendChild(eCategoryItem);
			}

		}
		if(log.isDebugEnabled()){
			log.debug("Removing item.");
		}
		for(int j=0; j<alCategoryAssociations.size(); j++)
		{
			HashMap <String, String> hmCat = alCategoryAssociations.get(j);
			String sMatched = hmCat.get("Matched");
			String strCatPath = hmCat.get("CategoryPath");
			String strOrgCode = hmCat.get("OrganizationCode");

			if("Y".equals(sMatched))
			{
				continue;
			}
			else
			{
				counter++;
				YFCElement eCategoryToModify = modifyCategoryItemsDoc.createElement("Category");
				eCategoryToModify.setAttribute("CategoryPath", strCatPath);
				eCategoryToModify.setAttribute("OrganizationCode", strOrgCode);

				YFCElement eCategoryItemList = modifyCategoryItemsDoc.createElement("CategoryItemList");

				YFCElement eCategoryItem = modifyCategoryItemsDoc.createElement("CategoryItem");
				eCategoryItem.setAttribute("Action", "Delete");
				eCategoryItem.setAttribute("ItemID", strItemID);
				eCategoryItem.setAttribute("UnitOfMeasure", strUOM);
				eCategoryItem.setAttribute("OrganizationCode", strOrganizationCode);

				eModifyCategoryItems.appendChild(eCategoryToModify);
				eCategoryToModify.appendChild(eCategoryItemList);
				eCategoryItemList.appendChild(eCategoryItem);
			}

		}

		if(null!=eCategoryList){
			eItem.removeChild(eCategoryList);
		}
		return counter;

	}

	private boolean assocatedToCategory(String sCatPath, String sOrgCode, ArrayList <HashMap<String, String>>alCategoryAssociations)
	{
		boolean bAssociated = false;
		for(int i=0; i<alCategoryAssociations.size(); i++)
		{
			HashMap <String, String> hmCat = alCategoryAssociations.get(i);
			String sCatPathAssociated = hmCat.get("CategoryPath");
			String sOrgCodeAssociated = hmCat.get("OrganizationCode");

			if(sCatPathAssociated.equals(sCatPath) && sOrgCodeAssociated.equals(sOrgCode))
			{
				bAssociated = true;
				hmCat.put("Matched", "Y");
				break;
			}
		}
		if(log.isDebugEnabled()){
			log.debug("Category Path : "+ sCatPath + "|" + bAssociated);
		}
		return bAssociated;
	}

	private void updateValidValueForAttribute(YFSEnvironment env, String sAttrName, String sItemAttrValue) throws Exception
	{



		YFCDocument manageAttributeIn = YFCDocument.createDocument("AttributeList");
		YFCElement eAttributeListIn = manageAttributeIn.getDocumentElement();
		YFCElement eManageAttributeIn = manageAttributeIn.createElement("Attribute");
		eAttributeListIn.appendChild(eManageAttributeIn);
		eManageAttributeIn.setAttribute("AttributeDomainID", "ItemAttribute");
		eManageAttributeIn.setAttribute("AttributeGroupID", _ATTR_GROUP_ID);
		eManageAttributeIn.setAttribute("AttributeID", sAttrName);
		eManageAttributeIn.setAttribute("OrganizationCode", _ORG_CODE);


		YFCElement eAttrAllowedValueList = manageAttributeIn.createElement("AttributeAllowedValueList");
		eManageAttributeIn.appendChild(eAttrAllowedValueList);

		YFCElement eAttrAllowedValue = manageAttributeIn.createElement("AttributeAllowedValue");
		eAttrAllowedValueList.appendChild(eAttrAllowedValue);

		eAttrAllowedValue.setAttribute("LongDescription", sItemAttrValue);
		eAttrAllowedValue.setAttribute("ShortDescription", sItemAttrValue);
		eAttrAllowedValue.setAttribute("Value", sItemAttrValue);
		eAttrAllowedValue.setAttribute("Operation", "Manage");


		log.verbose("ManageAttribute :" + manageAttributeIn.getDocument());	


		YFCDocument manageAttrTemp = YFCDocument.createDocument("AttributeList");
		YFCElement eAttrListTemp = manageAttrTemp.getDocumentElement();
		YFCElement eAttrTemp = manageAttrTemp.createElement("Attribute");
		eAttrTemp.setAttribute("AttributeID", "");

		eAttrListTemp.appendChild(eAttrTemp);

		env.setApiTemplate("manageAttribute", manageAttrTemp.getDocument());
		api.invoke(env, "manageAttribute", manageAttributeIn.getDocument());
		env.clearApiTemplate("manageAttribute");



	}

	private ArrayList <HashMap<String, String>> getCategoryAssociations(YFSEnvironment env, Element eItem)
	throws Exception
	{

		String sItemID = eItem.getAttribute("ItemID");
		String sUOM = eItem.getAttribute("UnitOfMeasure");
		String sOrgCode = eItem.getAttribute("OrganizationCode");

		YFCDocument getItemListInDoc = YFCDocument.createDocument("Item");
		YFCElement eItemIn = getItemListInDoc.getDocumentElement();

		eItemIn.setAttribute("ItemID", sItemID);
		eItemIn.setAttribute("OrganizationCode", sOrgCode);
		eItemIn.setAttribute("UnitOfMeasure", sUOM);
		eItemIn.setAttribute("GetUnpublishedItems", "Y");

		YFCDocument getItemListTemplate = YFCDocument.createDocument("ItemList");
		YFCElement eItemListTemplate = getItemListTemplate.getDocumentElement();
		YFCElement eItemTemplate = getItemListTemplate.createElement("Item");
		YFCElement eCategoryListTemplate = getItemListTemplate.createElement("CategoryList");
		YFCElement eCategoryTemplate = getItemListTemplate.createElement("Category");

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
		Document itemListOutDoc = api.invoke(env, "getItemList", getItemListInDoc.getDocument());
		if(log.isDebugEnabled()){
			log.debug("getItemlist Output: \n"+SCXmlUtil.getString(itemListOutDoc));
		}
		env.clearApiTemplate("getItemList");

		Element eItemListOut = itemListOutDoc.getDocumentElement();
		Element itemElement = SCXmlUtil.getChildElement(eItemListOut, "Item");
		Element eCategoryList = SCXmlUtil.getChildElement(itemElement, "CategoryList");
		ArrayList <HashMap<String, String>> alCategory = new ArrayList();
		if(null!=eCategoryList && eCategoryList.hasChildNodes()){
			NodeList nlCategory = eCategoryList.getElementsByTagName("Category");
			for(int i=0; i<nlCategory.getLength(); i++)
			{
				Element eCategory = (Element)nlCategory.item(i);
				String sCategoryID = eCategory.getAttribute("CategoryID");
				String sCategoryPath = eCategory.getAttribute("CategoryPath");
				String sOrganizationCode = eCategory.getAttribute("OrganizationCode");

				HashMap <String, String> hmCategoryDtl = new HashMap();
				hmCategoryDtl.put("CategoryID", sCategoryID);
				hmCategoryDtl.put("CategoryPath", sCategoryPath);
				hmCategoryDtl.put("OrganizationCode", sOrganizationCode);
				alCategory.add(hmCategoryDtl);

			}
		}
		return alCategory;
	}

}
