package com.xpedx.nextgen.dataload.nontaskq;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.ycp.japi.util.YCPBaseAgent;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;


public class XPXInventoryIndicatorAgent extends YCPBaseAgent{

	private static YFCLogCategory log = YFCLogCategory
	.instance(XPXInventoryIndicatorAgent.class);
    private static boolean updateTriggered=false;
    
	public List getJobs(YFSEnvironment env, Document criteria,
			Document lastMessageCreated) throws Exception {
		log.info("Start of Method getJobs in XPXInventoryAgent");

		List listOfJobs = new ArrayList();
		if(updateTriggered==false){
  		log.info("start if Condition of Method getJobs in XPXInventoryAgent Value of Update Triggered is :: "+updateTriggered);	
		Document docCustContacts = SCXmlUtil.createFromString("<ItemBranch><OrderBy><Attribute Name='CustomerContactKey' Desc='N' /></OrderBy></ItemBranch>");
		log.info("docCustContacts"+SCXmlUtil.getString(docCustContacts));
		this.updateListOfJobs(criteria, listOfJobs, docCustContacts);
		updateTriggered=true;
		}
		else if(updateTriggered==true){
			log.info("start if Condition of Method getJobs in XPXInventoryAgent Value of Update Triggered is :: "+updateTriggered);		
			Document docCustContacts = SCXmlUtil.createFromString("<ItemBranch></ItemBranch>");
			log.info("docCustContacts"+SCXmlUtil.getString(docCustContacts));
			this.updateListOfJobs(criteria, listOfJobs, docCustContacts);
		}
		log.info("End of Method getJobs in XPXInventoryAgent");
		return listOfJobs;

	}




	private void updateListOfJobs(Document criteria, List listOfJobs,
			Document docCustContacts) {

		Element eleContact = null;
		List contacts = SCXmlUtil.getChildrenList(docCustContacts.getDocumentElement());

		for (int counter = 0; counter < contacts.size(); counter++)
		{
			Document docOrderNew = SCXmlUtil.createDocument("ItemBranch");
			eleContact = (Element) contacts.get(counter);


			docOrderNew.getDocumentElement().setAttribute("Type", "DummyXml");
			//Add the Order to listOfJobs to be processed
			listOfJobs.add(docOrderNew);

		}
		return ;
	}

	@Override
	public void executeJob(YFSEnvironment env, Document inputDoc) throws Exception {


		// TODO Auto-generated method stub
		YIFApi api = YIFClientFactory.getInstance().getLocalApi();

		try {  
			log.info("Start of Method executeJob in XPXInventoryAgent");
			api.executeFlow(env,"XPXInventoryIndicatorUpdate",inputDoc);
			log.info("End of Method executeJob in XPXInventoryAgent");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}





}
