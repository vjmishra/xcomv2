package com.xpedx.nextgen.common.adhoc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSConnectionHolder;
import com.yantra.yfs.japi.YFSEnvironment;

public class UpdateInternalUsers implements YIFCustomApi {


	private static YFCLogCategory log;
	private static YIFApi api = null;
	List<String> userKeyList = new ArrayList<String>();
	List<String> loginIDList = new ArrayList<String>();
	int noOfUsers = 0;
	
	
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try 
		{
			api = YIFClientFactory.getInstance().getApi();
			
			//TODO:createXPXCustomerRulesProfile
		}
		catch (YIFClientCreationException e1) {

			e1.printStackTrace();
		}
	}
	
	
	public Document invokeUpdateUserGroupList(YFSEnvironment env,Document inXML) throws Exception
	{
	//Service name to be Created XPXUpdateInternalUsersToInvoiceGroup with a custom api define inside invoking this method
	 Element customerElement = inXML.getDocumentElement();
	 createUserGroupListInput(customerElement,env);	
	 return null;	
	}

	private void createUserGroupListInput(Element customerElement,YFSEnvironment env) {
		
            try {
            	//Get all the Internal Users and Inserting UserKey and Login in in array List
	   	        log.info("Inside Update method to update all internal users to have view invoices group");
            	YFCDocument getUserListInputDoc = YFCDocument.createDocument("User");
	   	    	YFCElement userList = getUserListInputDoc.getDocumentElement();
	   	    	YFCElement extnUserList = getUserListInputDoc.createElement("Extn");
	   	    	extnUserList.setAttribute("ExtnUserType", "INTERNAL");
	   	    	userList.appendChild(extnUserList);
	   	    	
	   	    	Document getUserListOutputDoc = api.invoke(env, "getUserList", getUserListInputDoc.getDocument());
	   	    	
	   	    	
	   	    	if(getUserListOutputDoc != null){

		   	    	Element getUserListElem = getUserListOutputDoc.getDocumentElement();
		   	    	
		   	    	ArrayList<Element> userListNode = SCXmlUtil.getChildren(getUserListElem, "User");
		   	    	
		   	    	if(userListNode != null){
		   	    		for(int i=0; i<userListNode.size(); i++){
			   	    		Element userEle = userListNode.get(i);
			   	    		if(userEle != null){
			   	    			//System.out.println("**************************Im inside arraylist addition loop************************************");
			   	    			//System.out.println("Userkey and loginid is : " + userEle.getAttribute("UserKey") + " " + userEle.getAttribute("Loginid"));
			   	    			log.info("Userkey and loginid is : " + userEle.getAttribute("UserKey") + " " + userEle.getAttribute("Loginid"));
			   	    			userKeyList.add(userEle.getAttribute("UserKey"));
				   	    		loginIDList.add(userEle.getAttribute("Loginid"));
			   	    		}
			   	    		
			   	    	}
		   	    	}
	   	    	}
	   	    //Updating all non-salesrep users to view Invoices Group	
	   	     if(userKeyList.size() > 0 && loginIDList.size() > 0){
	   	    	 for(int i=0 ; i<userKeyList.size();i++){
	   	    		YFCDocument manageUserProfileInputDoc = YFCDocument.createDocument("User");
		   	    	YFCElement user = manageUserProfileInputDoc.getDocumentElement();
	   	    		user.setAttribute("Loginid", loginIDList.get(i));
	   	    		user.setAttribute("UserKey", userKeyList.get(i));
	   	    		YFCElement userGroupListsElement = manageUserProfileInputDoc.createElement("UserGroupLists");
	   	    		YFCElement userGroupListElement = manageUserProfileInputDoc.createElement("UserGroupList");
	   	    		userGroupListElement.setAttribute("UsergroupId", "XPXViewInvoicesGroup");
	   	    		userGroupListElement.setAttribute("UsergroupKey", "20111110065248106667");
	   	    		userGroupListsElement.appendChild(userGroupListElement);
	   	    		user.appendChild(userGroupListsElement);
	   	    		//System.out.println("**************************Im inside createUserGroupListInput for loop************************************");
	   	    		//Update all the Internal Users to view invoices group
	   	    		Document manageUserProfileOutputDoc = api.invoke(env, "modifyUserHierarchy", manageUserProfileInputDoc.getDocument());
	   	    		noOfUsers = noOfUsers+1;
	   	    	 }
	   	    	log.info("No of records updated : " + noOfUsers);
	    	  }
 
            }
            catch (Exception e) {
            	e.printStackTrace();
            	log.error("Exception: " + e.getStackTrace());	
            }
       
    
}
	
	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}


}
