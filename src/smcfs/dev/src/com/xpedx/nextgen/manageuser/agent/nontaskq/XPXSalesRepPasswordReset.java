package com.xpedx.nextgen.manageuser.agent.nontaskq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections.map.MultiValueMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.ycp.japi.util.YCPBaseAgent;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCException;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.interop.japi.YIFCustomApi;

public class XPXSalesRepPasswordReset extends YCPBaseAgent {

	private static YFCLogCategory log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	private static Set<String> customerContactKeys = new HashSet<String>();
	private Properties _properties = null;
	private static YIFApi api = null;
	
	
	public List getJobs(YFSEnvironment env, Document criteria,
			Document lastMessageCreated) throws Exception {
		List listOfSalesRep = new ArrayList();
		List listOfJobs = new ArrayList();
		Document docOrderNew = null;
		YIFApi api = YIFClientFactory.getInstance().getLocalApi();
		Document inputCustomerContactDoc = SCXmlUtil.createFromString("<CustomerContact><OrderBy><Attribute Name='CustomerContactKey' Desc='N' /></OrderBy></CustomerContact>");
		Element inputCustomerContactElement = inputCustomerContactDoc.getDocumentElement();
        log.debug("In Get Job SalesREp");
	//	String totalNumberOfRecords = getCriteriaParamValue(criteria,"NumRecordsToBuffer");
		inputCustomerContactElement.setAttribute("MaximumRecords","10");
		
		Document customerContactListTemplateDoc = SCXmlUtil.createFromString("<CustomerContactList><CustomerContact><User/></CustomerContact></CustomerContactList>");
		env.setApiTemplate("getCustomerContactList",customerContactListTemplateDoc);
		System.out.println("in get Jobs -----"+ customerContactListTemplateDoc );
		
		Document docCustContacts = api.invoke(env, "getCustomerContactList", inputCustomerContactDoc);
		log.debug("Customer Contact List  " + docCustContacts);
		
		env.clearApiTemplate("getCustomerContactList");
		
		System.out.println("in get Jobs -----"+ docCustContacts );
		
		if(docCustContacts.getDocumentElement().getElementsByTagName("CustomerContact").getLength()>0)
		{
			NodeList custContactList = docCustContacts.getDocumentElement().getElementsByTagName("CustomerContact");
			
			for(int i=0; i<custContactList.getLength(); i++)
			{
				Element customerContactElement = (Element) custContactList.item(i);
					
				listOfSalesRep.add(customerContactElement.getAttribute("CustomerContactID"));
				
			}
		}
		
		Element eleContact = null;
		//List contacts = SCXmlUtil.getChildrenList(docCustContacts.getDocumentElement());
		System.out.println("in get Jobs -----"+ listOfSalesRep );
		
		for (int salesRep = 0; salesRep < listOfSalesRep.size(); salesRep++)
		{
			docOrderNew = SCXmlUtil.createDocument("User");
			//eleContact = (Element) contacts.get(salesRep);
			
			//Element userElement = (Element) eleContact.getElementsByTagName(XPXLiterals.E_USER).item(0);
			
			log.debug("Customer Contact List  " + listOfSalesRep.get(salesRep).toString());
			docOrderNew.getDocumentElement().setAttribute("ExistingPassword", listOfSalesRep.get(salesRep).toString());
			docOrderNew.getDocumentElement().setAttribute("Loginid", listOfSalesRep.get(salesRep).toString());
			docOrderNew.getDocumentElement().setAttribute("Password", applySaltPattern(listOfSalesRep.get(salesRep).toString(), "3@4@7@9@10@12@14"));
			docOrderNew.getDocumentElement().setAttribute("GeneratePassword", "YES");
			log.debug("Before Change Password ---  " + docOrderNew);
			System.out.println("new change Oassword is ----" + docOrderNew);
			//api.invoke(env, "ChangePassword", docOrderNew);
			//log.debug("Change Password ---  " + docOrderNew);
			listOfJobs.add(docOrderNew);
		}
		log.debug("The input document to requestResetPassword is: "+SCXmlUtil.getString(docOrderNew));
      return listOfJobs;
	}
	
	@SuppressWarnings("unchecked")
	public void invokeModifyPassword(YFSEnvironment env, Document inXML) throws Exception
	{
  //Code removed since it is Agent ...
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


		// TODO Auto-generated method stub
		YIFApi api = YIFClientFactory.getInstance().getLocalApi();

		try {  
			log.info("Start of Method executeJob in Sales Rep Change Password");
			log.debug("The input document to requestResetPassword is: "+SCXmlUtil.getString(inputDoc));
			api.invoke(env,"ChangePassword",inputDoc);
			log.info("End of Method executeJob in Sales Rep Change Password");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	

}
