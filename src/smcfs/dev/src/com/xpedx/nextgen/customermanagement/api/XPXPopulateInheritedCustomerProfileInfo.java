package com.xpedx.nextgen.customermanagement.api;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXPopulateInheritedCustomerProfileInfo implements YIFCustomApi {

	YIFApi api = null;
	static String _GET_CUSTOMER_LIST_SERVICE = "getCustomerList";
	public  int apiCounter=0;
	private static Properties propInheritedFields = new Properties();
	YFCLogCategory logger = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	@Override
	
	public void setProperties(Properties arg0) throws Exception 
	{
		
		Properties props = new Properties();
		String path=arg0.getProperty("InheritedAttributesPropertyPath");
		InputStream in=XPXPopulateInheritedCustomerProfileInfo.class.getClassLoader().getResourceAsStream(path);
		props.load(in);
		propInheritedFields = props;
	
	}
	
	public Document invokeGetInheritedCustInfo(YFSEnvironment env, Document inXML)
	throws Exception
	{
		api = YIFClientFactory.getInstance().getApi();

		Document outXML = getCustomerProfileInfo(env, inXML, null);
		if(logger.isDebugEnabled())
		{
			logger.debug("The output of getCustomerProfileInfo is : "+ SCXmlUtil.getString(outXML));
		}
		YFCDocument outDoc = YFCDocument.getDocumentFor(outXML);
		YFCElement eCustomerList = outDoc.getDocumentElement();
		YFCElement eCustomer = (YFCElement)eCustomerList.getElementsByTagName("Customer").item(0);
		
		YFCDocument returnDoc = YFCDocument.createDocument();
		YFCElement eCustomerReturn = returnDoc.importNode(eCustomer, true);
		returnDoc.appendChild(eCustomerReturn);
		return returnDoc.getDocument();
		
	}
	
	private Document getCustomerProfileInfo(YFSEnvironment env, Document searchXML, Document customerProfileXML)
	throws Exception
	{
		ArrayList<String> alMissingProfileInfo = getMissingProfileInfo(propInheritedFields, customerProfileXML);
		SCXmlUtil.getString(customerProfileXML);
		Document docCustomerProfile = null;
		if(alMissingProfileInfo.size() > 0)
		{
			env.setApiTemplate("getCustomerList",createTemplateForCustomerList());
			Document getCustomerList = api.invoke(env, _GET_CUSTOMER_LIST_SERVICE, searchXML);
			if(logger.isDebugEnabled())
			{
				logger.debug("The input to the getCustomerList service is:"+SCXmlUtil.getString(searchXML));
			}
			apiCounter++;
			env.clearApiTemplate("getCustomerList");
			Document docUpdatedCustProfile = updateMissingProfileInfo(alMissingProfileInfo, customerProfileXML, getCustomerList);
			
			Element eCustomerList = docUpdatedCustProfile.getDocumentElement();
			String sFieldsEmpty = eCustomerList.getAttribute("FieldsEmpty");
			
			if("Y".equals(sFieldsEmpty))
			{
				Element eCustomerListOut = getCustomerList.getDocumentElement();
				Element eCustomer = (Element)eCustomerListOut.getElementsByTagName("Customer").item(0);
				String sParentCustomerKey = eCustomer.getAttribute("ParentCustomerKey");
				
				YFCDocument docCustomerListIn = YFCDocument.createDocument("Customer");
				YFCElement eCustomerListIn = docCustomerListIn.getDocumentElement();
				eCustomerListIn.setAttribute("CustomerKey", sParentCustomerKey);
				
				docUpdatedCustProfile = getCustomerProfileInfo(env, docCustomerListIn.getDocument(), docUpdatedCustProfile);
			}

			docCustomerProfile = docUpdatedCustProfile;

		}
		else
		{
			docCustomerProfile = customerProfileXML;
		}
		return docCustomerProfile;
	}
	
	private ArrayList<String> getMissingProfileInfo(Properties propInheritedFields, Document customerProfileXML)
	{
		boolean bFirstCall = false;
		ArrayList<String> alMissingProfileInfo = new ArrayList<String>();
		Element eCustomerList = null;
		Element eCustomerExtn = null;
		
		if(customerProfileXML == null)
		{
			bFirstCall = true;
		}
		else
		{
			eCustomerList = customerProfileXML.getDocumentElement();
			eCustomerExtn = (Element)eCustomerList.getElementsByTagName("Extn").item(0);
		}
		
		Set ksFields = propInheritedFields.keySet();
		Iterator<String> iFields = ksFields.iterator();
		while(iFields.hasNext())
		{
			String sFieldName = iFields.next();
			String sAttrValue = null;

			if(!bFirstCall)
			{
				sAttrValue = eCustomerExtn.getAttribute(sFieldName);
				alMissingProfileInfo.add(sFieldName);
			}
			else
			{
				alMissingProfileInfo.add(sFieldName);
			}
			
		}
		
		
		return alMissingProfileInfo;
				
	}
	
	private Document updateMissingProfileInfo(ArrayList<String> alMissingProfileInfo, Document customerProfileXML, Document getCustomerList)
	{
		Document docUpdatedCustomerProfile = null;
		int counter = 0;
		
		if(customerProfileXML == null)
		{
			docUpdatedCustomerProfile =  getCustomerList;
			Element eCustomerList = getCustomerList.getDocumentElement();
			SCXmlUtil.getString(eCustomerList);
			eCustomerList.setAttribute("FieldsEmpty", "Y");
			if(logger.isDebugEnabled()){
				logger.debug("FieldsEmpty: Y");
			}
		}
		else
		{
			
			Element eCustomerProfileList = customerProfileXML.getDocumentElement();
			Element eCustomerProfile = (Element)eCustomerProfileList.getElementsByTagName("Customer").item(0);
			Element eCustomerProfileExtn = (Element)eCustomerProfile.getElementsByTagName("Extn").item(0);
			Element eCustomerList = getCustomerList.getDocumentElement();
			Element eCustomer = (Element)eCustomerList.getElementsByTagName("Customer").item(0);
			Element eCustomerExtn = (Element)eCustomer.getElementsByTagName("Extn").item(0);

			if(apiCounter>4){
				eCustomerProfileList.setAttribute("FieldsEmpty", "N");
			}
			else
			{
			
				for(int i=0; i < alMissingProfileInfo.size(); i++)
				{
					String attrName = alMissingProfileInfo.get(i);
					String sParentCustomerAttrValue = eCustomerExtn.getAttribute(attrName);
					String suffixType=eCustomerExtn.getAttribute("ExtnSuffixType");

					
					
					if((!YFCCommon.isStringVoid(sParentCustomerAttrValue))&&!("0.00".equals(sParentCustomerAttrValue)) )
					{
						counter++;
						eCustomerProfileExtn.setAttribute(attrName, sParentCustomerAttrValue);
						
					}
					
					if("MC".equals(suffixType)){
						if("ExtnCanOrderFlag".equals(attrName)||"ExtnCanViewInvFlag".equals(attrName)||"ExtnUseCustSKU".equals(attrName)
								||"ExtnUseOrderMulUOMFlag".equals(attrName)||"ExtnPreviewInvoicesFlag".equals(attrName)){
						
							eCustomerProfileExtn.setAttribute(attrName, eCustomerExtn.getAttribute(attrName));
						}
					}

					if("C".equals(suffixType)){
						
						if("ExtnCustLineAccLbl".equals(attrName)||"ExtnCustLineAccNoFlag".equals(attrName)||"ExtnCustLineField1Label".equals(attrName)
								||"ExtnCustLineField1Flag".equals(attrName)||"ExtnCustLineField2Flag".equals(attrName)||"ExtnCustLineField2Label".equals(attrName)
								||"ExtnCustLineField3Flag".equals(attrName)||"ExtnCustLineField3Label".equals(attrName) ||"ExtnCustLineField3Label".equals(attrName)
								||"ExtnInvoiceEDIFlag".equals(attrName)){
						
							eCustomerProfileExtn.setAttribute(attrName, eCustomerExtn.getAttribute(attrName));
						}
					}
					
				}
				
				if(counter == alMissingProfileInfo.size())
				{
					//all fields have values
					eCustomerProfileList.setAttribute("FieldsEmpty", "N");
					if(logger.isDebugEnabled()){
						logger.debug("FieldsEmpty: N");
					}
				}
				else
				{
					eCustomerProfileList.setAttribute("FieldsEmpty", "Y");
					if(logger.isDebugEnabled()){
						logger.debug("FieldsEmpty: Y");
					}
				}
			}
			docUpdatedCustomerProfile = customerProfileXML;
		}
		
		return docUpdatedCustomerProfile;
	}
	
	private Document createTemplateForCustomerList() {
		
		YFCDocument customeListTemplateDoc = YFCDocument
				.createDocument("CustomerList");
		YFCElement customerList = customeListTemplateDoc
		.getDocumentElement();
		YFCElement customer = customerList
		.createChild("Customer");
		
		YFCElement extn = customer
		.createChild("Extn");
		SCXmlUtil.getString(customeListTemplateDoc.getDocument());
		return customeListTemplateDoc.getDocument();
	}

}
