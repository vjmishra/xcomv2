package com.xpedx.nextgen.customermanagement.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.custom.dbi.XPX_Item_Extn;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.shared.dbclasses.XPX_Item_ExtnDBHome;
import com.yantra.shared.dbclasses.YFS_CustomerDBHome;
import com.yantra.shared.dbclasses.YFS_Customer_AssignmentBase;
import com.yantra.shared.dbclasses.YFS_Customer_AssignmentDBHome;
import com.yantra.shared.dbclasses.YPM_Pricelist_AssignmentDBHome;
import com.yantra.shared.dbi.YFS_Customer;
import com.yantra.shared.dbi.YFS_Customer_Assignment;
import com.yantra.shared.ycp.YFSContext;
import com.yantra.yfc.dblayer.PLTQueryBuilder;
import com.yantra.yfc.dblayer.PLTQueryBuilderHelper;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCDate;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXManageCustomerAndAssignmentAPI  implements YIFCustomApi{

private static YIFApi api = null;
private static YFCLogCategory log;	
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public Document manageCustomerAndAssignment(YFSEnvironment env,Document inputXML) throws Exception
	{
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		Element  customerAssignmentList=(Element)inputXML.getElementsByTagName("CustomerAssignmentList").item(0);
		Document customerAssignmentListDoc=SCXmlUtil.createFromString(SCXmlUtil.getString(customerAssignmentList));
		if(customerAssignmentListDoc != null)
		{
			manageCustomerAssignment(env, customerAssignmentListDoc);
		}
		
		
		return null;
		
	}
	private void getCustomer(YFSEnvironment env,NodeList customerAssignmentNodeList,int counter,List<YFS_Customer> yfsCustomerList)
	{	
		//Modified For EB-340
		if(counter == (customerAssignmentNodeList.getLength()) )
			return;
		PLTQueryBuilder pltQryBuilder = PLTQueryBuilderHelper.createPLTQueryBuilder();
		pltQryBuilder.setCurrentTable("YFS_CUSTOMER");
		if(customerAssignmentNodeList != null && customerAssignmentNodeList.getLength()  >0)
			pltQryBuilder.append(" CUSTOMER_ID IN ('"+((Element)customerAssignmentNodeList.item(counter)).getAttribute("CustomerID")+"'");
			counter=counter+1;
		
			for(int i=1;i<1000 && counter <customerAssignmentNodeList.getLength() ;i++ ,counter++)
			{
				Element customerAssignmentElement=(Element)customerAssignmentNodeList.item(counter);
				pltQryBuilder.append(", '"+customerAssignmentElement.getAttribute("CustomerID")+"'");
			}
			
		pltQryBuilder.append(")");
		yfsCustomerList.addAll(YFS_CustomerDBHome.getInstance().listWithWhere((YFSContext)env, pltQryBuilder,5000));
		//Calling getCustomer() in order to split the query EB-340
		getCustomer(env,customerAssignmentNodeList,counter,yfsCustomerList);

	}
	public Document manageCustomerAssignment(YFSEnvironment env,Document inputXML) throws Exception
	{
		YFSContext ctx=(YFSContext)env;		
		
		//ycAssignment.set
		//YFS_Customer_AssignmentDBHome.getInstance().insert(ctx, ycAssignment);
		/*<CustomerAssignment CustomerID="93-761249-000-A-MM" IgnoreOrdering="Y"
	    Operation="Create" OrganizationCode="CD-0000516824-M-XPED-CC" UserId="wedwards3"/>*/
	//pltQryBuilder.appendString("USER_KEY", "=", user_key);
	
	
		NodeList customerAssignmentNodeList=inputXML.getElementsByTagName("CustomerAssignment");
		//Modified For EB-340
		List<YFS_Customer> customerList=new ArrayList<YFS_Customer>();
		getCustomer(env,customerAssignmentNodeList,0,customerList);
		String userId="";
		String organizationCode="";
		Map<String,YFS_Customer> customerIDMap=new HashMap<String,YFS_Customer>();
		for( YFS_Customer customers:customerList)
		{
			customerIDMap.put(customers.getCustomer_ID(), customers);
		}
		Map<String,String>  assignmentKeyMap=new HashMap<String,String>();
		for(int i=0;i<customerAssignmentNodeList.getLength();i++)
		{
			try
			{
				YFS_Customer_AssignmentBase ycAssignment=YFS_Customer_AssignmentBase.newInstance();
				//YFS_Customer_Assignment ycAssignment= YFS_Customer_Assignment.newInstance();
				Element customerAssignmentElem=(Element)customerAssignmentNodeList.item(i);
				String operation=customerAssignmentElem.getAttribute("Operation");
				YFS_Customer customer=customerIDMap.get(customerAssignmentElem.getAttribute("CustomerID"));
				if(customer != null)
				{
					ycAssignment.setCustomer(customer);
					ycAssignment.setCustomer_Key(customer.getCustomer_Key());
				}
				ycAssignment.setOrganization_Code(customerAssignmentElem.getAttribute("OrganizationCode"));
				ycAssignment.setUser_Id(customerAssignmentElem.getAttribute("UserId"));
				if("Create".equals(operation))
				{				
					ycAssignment.setModifyts(new YFCDate());
					ycAssignment.setCreatets(new YFCDate());
					YFS_Customer_AssignmentDBHome.getInstance().insert(ctx, ycAssignment);
				}
				else if("Delete".equals(operation))
				{
					//Modified For EB-340
					int counter=0;
					if(assignmentKeyMap.size() ==0)
						getCustomerAssignment(env, customerAssignmentElem.getAttribute("UserId"), customerAssignmentElem.getAttribute("OrganizationCode"), customerList,counter,assignmentKeyMap);
					String assignmentKey=assignmentKeyMap.get(customer.getCustomer_Key());
					if(assignmentKey != null && assignmentKey.trim().length() >0)
					{ 
						ycAssignment.setCustomer_Assignment_Key(assignmentKey);
						YFS_Customer_AssignmentDBHome.getInstance().delete(ctx, ycAssignment);
					}
				}
			}
			catch(Exception e)
			{
				log.error(" Error While adding or deleting customer Assignment "+e.getMessage());
			}
				
			//YFS_Customer_AssignmentDBHome.getInstance().addToBatch(ctx, ycAssignment);
			
			
		}
		
		//YFS_Customer_AssignmentDBHome.getInstance().executeBatch(ctx, YFS_Customer_Assignment.getEntityObject(YFS_Customer_AssignmentBase.class));
		
		return  null;
		
	}
	private void  getCustomerAssignment(YFSEnvironment env,String userId,String organizationCode,List<YFS_Customer> customerList,int counter,Map<String,String> asignmentKeyMap)
	{
		//EB-340 - Start 
		if(counter == (customerList.size()) )
			return ;
		PLTQueryBuilder pltQryBuilder = PLTQueryBuilderHelper.createPLTQueryBuilder();
		pltQryBuilder.setCurrentTable("YFS_CUSTOMER_ASSIGNMENT");
		if(customerList != null && customerList.size()  >0)
			pltQryBuilder.append(" CUSTOMER_KEY IN ('"+customerList.get(counter).getCustomer_Key()+"'");
		counter=counter+1;
		for(int i=1;i<1000 && counter <customerList.size() ;i++ ,counter++)
		{
				pltQryBuilder.append(", '"+customerList.get(counter).getCustomer_Key()+"'");
		}
			
		pltQryBuilder.append(")");
		pltQryBuilder.appendString("AND USER_ID", "=", userId);
		pltQryBuilder.append(" AND ORGANIZATION_CODE ='"+organizationCode+"'");
		
		List<YFS_Customer_Assignment> yfsCustomerAssinedList=YFS_Customer_AssignmentDBHome.getInstance().listWithWhere((YFSContext)env, pltQryBuilder,5000);		
		for( YFS_Customer_Assignment custAssingment : yfsCustomerAssinedList)
		{
			asignmentKeyMap.put(custAssingment.getCustomer_Key(), custAssingment.getCustomer_Assignment_Key());
		}
		getCustomerAssignment(env,userId,organizationCode,customerList,counter,asignmentKeyMap);
		//EB-340 - End
		
	}
/*	public Document manageCustomerAssignment(YFSEnvironment env,Document inputXML) throws Exception
	{
		
	}*/
	
}
