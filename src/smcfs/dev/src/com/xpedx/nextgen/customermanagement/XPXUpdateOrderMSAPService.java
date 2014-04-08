package com.xpedx.nextgen.customermanagement;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.om.api.XPXPriceChangeOrderOnSuccessAPI;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.shared.dbclasses.XPX_Item_ExtnDBHome;
import com.yantra.shared.dbi.YFS_Order_Header;
import com.yantra.shared.ycp.YFSContext;
import com.yantra.yfc.dblayer.PLTQueryBuilder;
import com.yantra.yfc.dblayer.PLTQueryBuilderHelper;
import com.yantra.yfc.dblayer.YFCDBContext;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXUpdateOrderMSAPService  implements YIFCustomApi {

	
	private static YIFApi api = null;
	private static final Logger log = Logger
			.getLogger(XPXUpdateOrderMSAPService.class);
	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public Document updateOrder(YFSEnvironment env, Document inputDoc)
	{
		Element rootElement=inputDoc.getDocumentElement();
		Element orderElement=(Element)rootElement.getElementsByTagName("Order").item(0);
		if(orderElement != null)
		{
			String oldMsapCustomerId="";
			try
			{
				oldMsapCustomerId=getMSAPCustomerElement(env,orderElement.getAttribute("OldMSAPCustomerKey"));
			}
			catch(Exception e)
			{
				log.error("Error while getting msap");
			}
			String newMsapCustomerId=orderElement.getAttribute("NewMSAPCustomerID");
			if(!YFCCommon.isVoid(newMsapCustomerId) && !YFCCommon.isVoid(oldMsapCustomerId)  &&  newMsapCustomerId.equals(oldMsapCustomerId))
			{
				return inputDoc;
			}
			ArrayList<Element> customerList=SCXmlUtil.getElements(orderElement, "Customer");
			YFCDBContext dbContext=(YFCDBContext)getContext(env);
			Connection con=dbContext.getConnectionForTable("YFS_ORDER_HEADER");
			try
			{
				
				for(Element customer:customerList)
				{
					/*YFS_Order_Header orderHeader=new YFS_Order_Header
					PLTQueryBuilder pltQryBuilder = PLTQueryBuilderHelper.createPLTQueryBuilder();
					pltQryBuilder.setCurrentTable("XPX_ITEM_EXTN");
					YFS_Order_Header.getInstance().updateWithWhere(dbContext, aDBObject, pltQryBuilder)*/
					PreparedStatement ps=null;
					try
					{
						String buyerOrganizationCode=customer.getAttribute("BuyerOrganizationCode");
						if(!YFCCommon.isVoid(newMsapCustomerId) && !YFCCommon.isVoid(oldMsapCustomerId) && !YFCCommon.isVoid(buyerOrganizationCode) )
						{
						 String query="UPDATE YFS_ORDER_HEADER SET bill_to_id ='"+newMsapCustomerId+"' WHERE bill_to_id ='"+oldMsapCustomerId+"' and buyer_organization_code = '"+buyerOrganizationCode+"' ";
						 ps=con.prepareStatement(query);
						 ps.executeUpdate();
						 con.commit();	
						}
					}
					catch(Exception e)
					{
						log.error("Connection is already close");
					}
					finally
					{
						try
						{
							if(ps != null)
								ps.close();
							
						}
						catch(SQLException e1)
						{
							log.error("Connection is already close");
						}
						
					}
				}
			}
			catch(Exception e)
			{
				log.error("Connection is already close");
			}
			finally
			{
				try
				{
					if(con != null)
						con.close();
					
				}
				catch(SQLException e1)
				{
					log.error("Connection is already close");
				}
			}
		}
		
		return inputDoc;
	}
	
	private String getMSAPCustomerElement(YFSEnvironment env, String customerKey) throws Exception{
		Document getCustomerListInputDoc =SCXmlUtil.createDocument("Customer");
		getCustomerListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_KEY, customerKey);

		Document templateListDoc = SCXmlUtil.createDocument(XPXLiterals.E_CUSTOMER_LIST);
		Element templateListElement = templateListDoc.getDocumentElement();
		Element templateCustElement = templateListDoc.createElement(XPXLiterals.E_CUSTOMER);
		templateCustElement.setAttribute(XPXLiterals.A_CUSTOMER_ID, "");
		templateListElement.appendChild(templateCustElement);

		env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API, templateListDoc);
		api = YIFClientFactory.getInstance().getApi();
		Document getCustomerListOutputDoc = api.invoke(env, XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListInputDoc);
		env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);

		return ((Element)getCustomerListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_CUSTOMER).item(0)).getAttribute("CustomerID");
	}
	
	private YFSContext  getContext(YFSEnvironment env) {
		if ( env instanceof YFSContext )
			return (YFSContext)env;
		else
			return null;
	}
}
