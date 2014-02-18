package com.xpedx.nextgen.customermanagement.api;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.shared.dbclasses.YFS_CustomerBase;
import com.yantra.shared.dbclasses.YFS_CustomerDBHome;
import com.yantra.shared.ycp.YFSContext;
import com.yantra.yfc.dblayer.PLTQueryBuilder;
import com.yantra.yfc.dblayer.PLTQueryBuilderHelper;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCDate;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXManageCustomersAPI implements YIFCustomApi {

	private static YFCLogCategory log;
	private static YIFApi api = null;
	private Properties arg0;
	
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e1) {
			log.error("API initialization error");
		}
	}
	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}
	
	public Document manageCustomers(YFSEnvironment env,Document inputXML) throws Exception
	{
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		Element  customerList=(Element)inputXML.getElementsByTagName("CustomerList").item(0);
		Document customerListDoc=SCXmlUtil.createFromString(SCXmlUtil.getString(customerList));
		if(customerListDoc != null)
		{
			manageCustomerList(env, customerListDoc);
		}
		
		
		return null;
		
	}
	
	public Document manageCustomerList(YFSEnvironment env,Document inputXML) throws Exception
	{
		YFSContext ctx=(YFSContext)env;		

	
		NodeList customerNodeList=inputXML.getElementsByTagName("Customer");
		PLTQueryBuilder pltQryBuilder = PLTQueryBuilderHelper.createPLTQueryBuilder();
			for(int i=0;i<customerNodeList.getLength();i++)
		{
			try
			{
				YFS_CustomerBase ycBase=YFS_CustomerBase.newInstance();
				Element customerElem=(Element)customerNodeList.item(i);
				String operation=customerElem.getAttribute("Operation");
			
				if("Update".equalsIgnoreCase(operation))
				{	
					pltQryBuilder = PLTQueryBuilderHelper.createPLTQueryBuilder();
					pltQryBuilder.setCurrentTable("YFS_CUSTOMER");
					pltQryBuilder.append(" customer_key ='"+customerElem.getAttribute("CustomerKey")+"'");					
					ycBase.setModifyts(new YFCDate());
					
					String customerLevel = customerElem.getAttribute("CustomerLevel");
					if(customerLevel!=null && customerLevel.trim().length() > 0)
						ycBase.setCustomer_Level(customerElem.getAttribute("CustomerLevel"));
					
					String extnECSR = SCXmlUtil.getXpathAttribute(customerElem,"Extn/@ExtnECSR");
					if(extnECSR!=null && extnECSR.trim().length() > 0)
						ycBase.setExtn_Extn_Ecsr(SCXmlUtil.getXpathAttribute(customerElem,"Extn/@ExtnECSR"));
					
					String extnECSR2 = SCXmlUtil.getXpathAttribute(customerElem,"Extn/@ExtnECSR2");
					if(extnECSR2!=null && extnECSR2.trim().length() > 0)
						ycBase.setExtn_Extn_Ecsr2(SCXmlUtil.getXpathAttribute(customerElem,"Extn/@ExtnECSR2"));
					
					String extnECsr1EMailID = SCXmlUtil.getXpathAttribute(customerElem,"Extn/@ExtnECsr1EMailID");
					if(extnECsr1EMailID!=null && extnECsr1EMailID.trim().length() > 0)
						ycBase.setExtn_Extn_Ecsr1_Email_Id(SCXmlUtil.getXpathAttribute(customerElem,"Extn/@ExtnECsr1EMailID"));
					
					String extnECsr2EMailID = SCXmlUtil.getXpathAttribute(customerElem,"Extn/@ExtnECsr2EMailID");
					if(extnECsr2EMailID!=null && extnECsr2EMailID.trim().length() > 0)
						ycBase.setExtn_Extn_Ecsr2_Email_Id(SCXmlUtil.getXpathAttribute(customerElem,"Extn/@ExtnECsr2EMailID"));
					
					String extnECSR1Key = SCXmlUtil.getXpathAttribute(customerElem,"Extn/@ExtnECSR1Key");
					if(extnECSR1Key!=null && extnECSR1Key.trim().length() > 0)
						ycBase.setExtn_Extn_Ecsr1_Key(SCXmlUtil.getXpathAttribute(customerElem,"Extn/@ExtnECSR1Key"));
					
					String extnECSR2Key = SCXmlUtil.getXpathAttribute(customerElem,"Extn/@ExtnECSR2Key");
					if(extnECSR2Key!=null && extnECSR2Key.trim().length() > 0)
						ycBase.setExtn_Extn_Ecsr2_Key(SCXmlUtil.getXpathAttribute(customerElem,"Extn/@ExtnECSR2Key"));					

				
					YFS_CustomerDBHome.getInstance().updateWithWhere(ctx, ycBase, pltQryBuilder);
				}
				
			}
			catch(Exception e)
			{
				log.error(" Error While updating customer  "+e.getMessage());
			}
			
		}
	 return  null;
		
	}

	
}
