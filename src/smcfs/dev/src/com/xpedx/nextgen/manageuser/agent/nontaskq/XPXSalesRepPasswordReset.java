package com.xpedx.nextgen.manageuser.agent.nontaskq;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.map.MultiValueMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.ycp.japi.util.YCPBaseAgent;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCException;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXSalesRepPasswordReset extends YCPBaseAgent {

	private static YFCLogCategory log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	private static Set<String> customerContactKeys = new HashSet<String>();
	
	private static HashMap customerContactUserContactKeys = new HashMap();
	private static MultiValueMap customerContactDetailsForAlertCreation = new MultiValueMap();
	private static String saltKey = null;

	
	@Override
	public List getJobs(YFSEnvironment env, Document criteria, Document lastMessageCreated) throws Exception {
		List listOfJobs = new ArrayList();
		YIFApi api = YIFClientFactory.getInstance().getLocalApi();
		
		int waitTimeInMins = -2440;
		DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.MINUTE, waitTimeInMins);
		String sysDateTime=dateFormat.format(cal.getTime());
		Document inputCustomerContactDoc = SCXmlUtil.createFromString("<CustomerContact><Extn ExtnIsSalesRep='Y' /><OrderBy><Attribute Name='CustomerContactKey' Desc='N' /></OrderBy></CustomerContact>");
		Element inputCustomerContactElement = inputCustomerContactDoc.getDocumentElement();

		//String totalNumberOfRecords = getCriteriaParamValue(criteria,"NumRecordsToBuffer");
			inputCustomerContactElement.setAttribute("MaximumRecords","10");
		
		log.debug("lastMessageCreated doc is: " + SCXmlUtil.getString(lastMessageCreated));
		if (lastMessageCreated != null) 
		{
			// get the CustomerContact which have failed
			//this.formInputToContactListWithLastMessageCreated(lastMessageCreated, inputCustomerContactDoc, inputCustomerContactElement);
			
			String customerContactKeyInLastCreatedMessage = "";
			Element lastCCElement = null;
			NodeList ccNodeList = lastMessageCreated.getElementsByTagName("ChangePassword");
			int ccLength = ccNodeList.getLength();
			if(ccLength != 0)
			{
				lastCCElement = (Element)ccNodeList.item(ccLength-1);
			
			String userKey = lastCCElement.getAttribute("UserKey");
			
			customerContactKeyInLastCreatedMessage=(String) customerContactUserContactKeys.get(userKey);
			inputCustomerContactDoc.getDocumentElement().setAttribute("CustomerContactKey", customerContactKeyInLastCreatedMessage);
			inputCustomerContactDoc.getDocumentElement().setAttribute("CustomerContactKeyQryType", "GT");
			}
		}
		log.debug("inputCustomerDoc"+ SCXmlUtil.getString(inputCustomerContactDoc));
		// template for customer contact list
		Document customerContactListTemplateDoc = SCXmlUtil.createFromString("<CustomerContactList><CustomerContact><User><Extn/></User></CustomerContact></CustomerContactList>");
		env.setApiTemplate("getCustomerContactList",customerContactListTemplateDoc);
		Document docCustContacts = api.invoke(env, "getCustomerContactList", inputCustomerContactDoc);
		env.clearApiTemplate("getCustomerContactList");
		
		if(docCustContacts.getDocumentElement().getElementsByTagName("CustomerContact").getLength()>0)
		{
			NodeList custContactList = docCustContacts.getDocumentElement().getElementsByTagName("CustomerContact");
			
			for(int i=0; i<custContactList.getLength(); i++)
			{
				Element customerContactElement = (Element) custContactList.item(i);
				
				Element userElement = (Element) customerContactElement.getElementsByTagName("User").item(0);
				
				Element extn = (Element) customerContactElement.getElementsByTagName("User").item(0);
				saltKey =  extn.getAttribute("ExtnSaltKey");
				customerContactUserContactKeys.put(userElement.getAttribute("UserKey"), customerContactElement.getAttribute("CustomerContactKey"));
				
				
				customerContactDetailsForAlertCreation.put(userElement.getAttribute("UserKey"), customerContactElement.getAttribute("CustomerContactID"));
				customerContactDetailsForAlertCreation.put(userElement.getAttribute("UserKey"), userElement.getAttribute("EnterpriseCode"));
				customerContactDetailsForAlertCreation.put(userElement.getAttribute("UserKey"), userElement.getAttribute("OrganizationKey"));
				
			}
		}
		
		this.updateListOfJobs(criteria, listOfJobs, docCustContacts);
		
		return listOfJobs;
	}

	/**
	 * @param criteria
	 * @param parameterName 
	 * @return
	 */
	private String getCriteriaParamValue(Document criteria, String parameterName) {
		String parameterValue = criteria.getDocumentElement().getAttribute(parameterName);
		if("NumRecordsToBuffer".equals(parameterName)){
			try{
				Integer.parseInt(parameterValue);
			} catch (Exception e) {
				YFCException ex = new YFCException(e);
				ex.setErrorDescription("Invalid Number! NumRecordsToBuffer");
				ex.setAttribute(parameterName, parameterValue);
			}
		}
		return parameterValue;
	}
	private void formInputToContactListWithLastMessageCreated(Document lastMessageCreated, Document inputCustomerContactDoc, Element inputCustomerElement) {
		Element complexQueryElement = inputCustomerContactDoc.createElement("ComplexQuery");
		/*inputCustomerElement.appendChild(complexQueryElement);
		Element orElement = inputCustomerContactDoc.createElement("or");*/
		/*for (String customerContactKey : customerContactKeys)
		{
			complexQueryElement.appendChild(orElement);
			Element expElement = inputCustomerContactDoc.createElement("Exp");
			expElement.setAttribute("Name", "CustomerContactKey");
			expElement.setAttribute("Value", customerContactKey);

			orElement.appendChild(expElement);
		}*/

		String customerContactKeyInLastCreatedMessage = "";
		Element lastCCElement = null;
		NodeList ccNodeList = lastMessageCreated.getElementsByTagName("ChangePassword");
		int ccLength = ccNodeList.getLength();
		if(ccLength != 0)
		{
			lastCCElement = (Element)ccNodeList.item(ccLength-1);
		
		String userKey = lastCCElement.getAttribute("UserKey");
		
		customerContactKeyInLastCreatedMessage=(String) customerContactUserContactKeys.get(userKey);
		
		log.debug("customerKeyInLastCreatedMessage"+ customerContactKeyInLastCreatedMessage);
		Element expNextElement = inputCustomerContactDoc.createElement("Exp");
		expNextElement.setAttribute("Name", "CustomerContactKey");
		expNextElement.setAttribute("Value",customerContactKeyInLastCreatedMessage);
		expNextElement.setAttribute("QryType", "GT");
		complexQueryElement.appendChild(expNextElement);
		}
	}
	
	private void updateListOfJobs(Document criteria, List listOfJobs,
			Document docCustContacts) {
		
		Element eleContact = null;
		List contacts = SCXmlUtil.getChildrenList(docCustContacts.getDocumentElement());
		List listOfSalesRep = new ArrayList();
		
		if(docCustContacts.getDocumentElement().getElementsByTagName("CustomerContact").getLength()>0)
        {
               NodeList custContactList = docCustContacts.getDocumentElement().getElementsByTagName("CustomerContact");
               
               for(int i=0; i<custContactList.getLength(); i++)
               {
                      Element customerContactElement = (Element) custContactList.item(i);
                            
                      listOfSalesRep.add(customerContactElement.getAttribute("CustomerContactID"));
                      
               }
        }
         
       // Element eleContact = null;
		Document docOrderNew = null;
        //List contacts = SCXmlUtil.getChildrenList(docCustContacts.getDocumentElement());
        log.debug("in get Jobs -----"+ listOfSalesRep );
        
        for (int salesRep = 0; salesRep < listOfSalesRep.size(); salesRep++)
        {
               docOrderNew = SCXmlUtil.createDocument("User");
               //eleContact = (Element) contacts.get(salesRep);
               
               //Element userElement = (Element) eleContact.getElementsByTagName(XPXLiterals.E_USER).item(0);
               
               log.debug("Customer Contact List  " + listOfSalesRep.get(salesRep).toString());
               docOrderNew.getDocumentElement().setAttribute("ExistingPassword", listOfSalesRep.get(salesRep).toString());
               docOrderNew.getDocumentElement().setAttribute("Loginid", listOfSalesRep.get(salesRep).toString());
   			   docOrderNew.getDocumentElement().setAttribute("Password", applySaltPattern(listOfSalesRep.get(salesRep).toString(), saltKey));
               docOrderNew.getDocumentElement().setAttribute("GeneratePassword", "YES");
               log.debug("Before Change Password ---  " + docOrderNew);
               log.debug("new change Oassword is ----" + docOrderNew);
               //api.invoke(env, "ChangePassword", docOrderNew);
               //log.debug("Change Password ---  " + docOrderNew);
               listOfJobs.add(docOrderNew);
        }
        log.debug("The input document to requestResetPassword is: "+SCXmlUtil.getString(docOrderNew));


		return ;
	}

	public static  String applySaltPattern(String word,String salt) { 
		ArrayList<Character> one = new ArrayList<Character>();    
		String [] saltpattern = salt.split("@") ; 
		ArrayList swapArrayList = new ArrayList();
	
		int l=0;
		for(int j =0; j < saltpattern.length ;j++){
			
			int k = Integer.parseInt(saltpattern[j]);
		for (int i = 0; i < word.length()-1; i++) {
			
			if(i==k){			
			    swapArrayList.add(word.substring(l,k));
               l = i;				
			}else{
				continue;
			}
			
			break;
		}
		}
	    
	    swapArrayList.add(word.substring(l,word.length()));		
			
		return swapListValues(swapArrayList).toString().replace("[", "").trim().replace(","," ").trim().replace("]", "").trim().replace("-", "").trim().replace(".", "").trim().replace(" ", "");
		    
		}   
	
	public static List swapListValues(ArrayList swapArrayList){
		List<String> list = new ArrayList<String>();
		//Change Position in List with 1-3, 2-4,3-5
		for(int i=0;i< swapArrayList.size();i++){
			if(i < swapArrayList.size() - 1){
				list.add((String) swapArrayList.get(i+1).toString().replace("@", "C").trim().replace("-", " "));
				list.add((String) swapArrayList.get(i).toString().replace("@", "C").trim().replace("-", " "));
				i++;
			}
			
		}
		if(list.size() < swapArrayList.size()){
			list.addAll(swapArrayList.subList(list.size(), swapArrayList.size()));
		}
		
		return list;
	}

	@Override
	public void executeJob(YFSEnvironment env, Document inputDoc) throws Exception {
		YIFApi api = YIFClientFactory.getInstance().getLocalApi();
		try {
			log.debug("The input document to requestResetPassword is: "+SCXmlUtil.getString(inputDoc));
			api.invoke(env, "changePassword", inputDoc);
		} catch (Exception e) {
			e.printStackTrace();
			//customerContactKeys.add(customerContactKey);
		}

	}

}
