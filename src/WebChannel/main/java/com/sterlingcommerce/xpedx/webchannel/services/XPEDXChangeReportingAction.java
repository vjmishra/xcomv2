package com.sterlingcommerce.xpedx.webchannel.services;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.webchannel.core.CommonCodeDescriptionType;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXContactAction;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils; //import com.sun.xml.internal.fastinfoset.sax.Properties;
import com.yantra.yfs.core.YFSSystem;
import com.businessobjects.rebean.wi.ReportEngines;
import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.crystaldecisions.sdk.framework.ISessionMgr;
import com.crystaldecisions.sdk.occa.infostore.IInfoObjects;
import com.crystaldecisions.sdk.occa.infostore.IInfoObject;
import com.crystaldecisions.sdk.occa.infostore.IInfoStore;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XPEDXChangeReportingAction extends WCMashupAction {
	

	XPEDXReportBean rb = null;
	XPEDXReportBean rb1 = null;
	private List<XPEDXReportBean> myList = new ArrayList<XPEDXReportBean>();
	private List<XPEDXReportBean> myCustList = new ArrayList<XPEDXReportBean>();

	//XPEDXReportBean rb = null;
	private List<XPEDXReportBean> stdreportlist =new ArrayList<XPEDXReportBean>();


	private List<XPEDXReportBean> custreportlist =new ArrayList<XPEDXReportBean>();
	private List<XPEDXReportBean> listDW =new ArrayList<XPEDXReportBean>();
	private List vallist=new ArrayList();
	private List listBOE=new ArrayList();
	HttpSession session = null ;
	
	public String getCustomerNo(String customerID) {
		String[] custDetails = customerID.split("-");
		String suffix = custDetails[1];
		return suffix;
	}
	
	public String execute() {
		try {
			String customerId=getWCContext().getCustomerId();
			String wcPropertiesFile = "xpedx_reporting.properties";
			XPEDXWCUtils.loadXPEDXSpecficPropertiesIntoYFS(wcPropertiesFile);
			String standardId= YFSSystem.getProperty("standard_folder_id");
			String customId= YFSSystem.getProperty("custom_folder_id");
			String username = YFSSystem.getProperty("username");
			String password = YFSSystem.getProperty("password");
			String cms = YFSSystem.getProperty("CMS");
			String auth = YFSSystem.getProperty("authentication");
			HttpSession session = request.getSession();
			IEnterpriseSession enterpriseSession = (IEnterpriseSession) session
			.getAttribute("CE_ENTERPRISESESSION");
			ISessionMgr sessionMgr = CrystalEnterprise.getSessionMgr();
			IInfoStore iStore = null;

			try {
				       	// Attempt logon.
			    if(enterpriseSession == null) {
			    	enterpriseSession = sessionMgr.logon(username, password, cms,auth);
			    	LOG.debug("++++++++++++ successfully logged in to BOE +++++++++++++++++++++");
					if (enterpriseSession != null) {
					// Store the IEnterpriseSession object in the session.
						session.setAttribute("CE_ENTERPRISESESSION",
								enterpriseSession);
						// Create the IInfoStore object.
					}
					else {
						return ERROR;
					}
			    }
			    
				iStore = (IInfoStore) enterpriseSession
				.getService("InfoStore");
				
			} catch(IllegalStateException ex) {
				
				return ERROR;
				
			} catch(SDKException ex) {
				return ERROR;
			}
			
			ReportEngines repEngines = (ReportEngines) enterpriseSession
			.getService("ReportEngines");
			session.setAttribute("ReportEngines", repEngines);

			

			List l1 = new ArrayList();
			//Querying Standard Report folder on BOE  
			IInfoObjects result = iStore
			.query("SELECT  SI_CUID,SI_ID,SI_NAME,SI_DESCRIPTION  FROM CI_INFOOBJECTS where SI_PARENTID=" + standardId +"ORDER BY SI_NAME");
						
			/*IInfoObjects result = iStore
			.query("SELECT  SI_CUID,SI_ID,SI_NAME,SI_DESCRIPTION  FROM CI_INFOOBJECTS where SI_PARENTID=86655" );
*/
			IInfoObject obj = null;

			for (int i = 0; i < result.size(); i++) {

				obj = (IInfoObject) result.get(i);

				String cuidBOE = obj.getCUID();
				int idBOE = obj.getID();
				String nameBOE = obj.getTitle();
				LOG.debug("Name From BOE@@@@@@@@@@@@@@@@@@@@@@@@@-----"+ nameBOE);
				String kindBOE = obj.getKind();
				String descBOE = obj.getDescription();

				rb = new XPEDXReportBean();
				rb.setCuid(cuidBOE);
				rb.setId(idBOE);
				rb.setName(nameBOE);
				rb.setKind(kindBOE);
				rb.setDesc(descBOE);

				myList.add(rb);

			}

			session.setAttribute("Reports", myList);


			//Querying Custom Report folder on BOE  
			IInfoObjects result1 = iStore
			.query("SELECT  SI_CUID,SI_ID,SI_NAME,SI_DESCRIPTION  FROM CI_INFOOBJECTS where SI_PARENTID= "+ customId +"ORDER BY SI_NAME");
			IInfoObject obj1 = null;
			for (int i = 0; i < result1.size(); i++) {

				obj1 = (IInfoObject) result1.get(i);

				String cuidBOE = obj1.getCUID();
				log.debug("CUID --->"+ cuidBOE );
				int idBOE = obj1.getID();
				String nameBOE = obj1.getTitle();
				//session.setAttribute("ReportName", nameBOE);
				log.debug("NAME --->"+ nameBOE);
				String kindBOE = obj1.getKind();
				String descBOE = obj1.getDescription();

				rb1 = new XPEDXReportBean();
				rb1.setCuid(cuidBOE);
				rb1.setId(idBOE);
				rb1.setName(nameBOE);
				rb1.setKind(kindBOE);
				rb1.setDesc(descBOE);

				myCustList.add(rb1);

			}

			session.setAttribute("CustomReports", myCustList);

		} catch (Exception e) {
			return ERROR;
		}


		/*Start Code for hit DW Server 
		 * and  Compare Report With DW
		 */		

		DataSource datasource;
		Connection con = null;

		try{
			if(request.getSession() != null ){
				session= request.getSession();
			}

			stdreportlist = (List<XPEDXReportBean>) session.getAttribute("Reports");
			custreportlist= (List<XPEDXReportBean>) session.getAttribute("CustomReports");
			Iterator<XPEDXReportBean> it= custreportlist.listIterator();
			while (it.hasNext()) {
				XPEDXReportBean reportBean = (XPEDXReportBean) it.next();

				//List for BOE CUIDs
				listBOE.add(reportBean.getCuid());
				log.debug("BOE CUID---->" + reportBean.getCuid()); 
			}

			getConnection();
			compareRpt();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return SUCCESS;
	}

	//Connection To DW Oracle Server
	public void getConnection() throws SQLException
	{
		String XCOM_MST_CUST= getCustomerNo(getWCContext().getBuyerOrgCode());
		String DBUrl= YFSSystem.getProperty("datasource_url");
		String DBName= YFSSystem.getProperty("datasource_name");
		//String DBUrl= "t3://localhost:7002";
		//String DBName= "SeptJNDI";
		Connection connection = null;
		Statement stmt= null;
		ResultSet rs= null;
		XPEDXReportBean rpBean = null;
		try {
			Hashtable ht = new Hashtable();
			ht.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
			ht.put("java.naming.provider.url", DBUrl);
			Context env =new InitialContext(ht);

			//InitialContext context = new InitialContext(ht);
			DataSource dataSource = (DataSource) env.lookup(DBName);
			connection = dataSource.getConnection();
			if(log.isDebugEnabled()){
			log.debug("Connection successful..");
			}
			//String schemaName=YFSSystem.getProperty("schemaname");
			//String Query="select distinct RPT_CUID, RPT_NAME,RPT_ID,RPT_KIND, RPT_DESC from " + schemaName + ".xpedx_custom_rpt_dtl where XCOM_MST_CUST=" + "'"+ XCOM_MST_CUST +"'"+"AND CUST_ROLE in (";
			String Query="select distinct RPT_CUID, RPT_NAME,RPT_ID,RPT_KIND, RPT_DESC from DH.xpedx_custom_rpt_dtl where XCOM_MST_CUST=" + "'"+ XCOM_MST_CUST +"'"+"AND CUST_ROLE in (";
			Query=getUserRole(Query);
			stmt = connection.createStatement();
			boolean test=stmt.execute(Query);
			if(test==true)
			{
				LOG.debug("Able To Query...");
				rs = stmt.getResultSet();
				while(rs.next())
				{

					rpBean = new XPEDXReportBean();
					rpBean.setCuid(rs.getString("RPT_CUID"));
					rpBean.setName(rs.getString("RPT_NAME"));
					rpBean.setKind(rs.getString("RPT_KIND"));

					rpBean.setId(rs.getInt("RPT_ID"));
					rpBean.setDesc(rs.getString("RPT_DESC"));
					log.debug("rpBean.getCuid()=="+rpBean.getCuid());
					log.debug("rpBean.getId()=="+rpBean.getId());
					listDW.add(rpBean);					

				}

				session.setAttribute("listFromDW",listDW);
			}
			
		}
		catch (Exception e) {
			LOG.debug("Not able to connect to DEV Datasource:->" + e.getMessage());
			}
		finally{
			stmt.close();
			connection.close();	
		}

	}

	
	//Mashup Start
	public String  getUserRole(String Query) throws Exception {
		String loginId=wcContext.getLoggedInUserId();
		Document outputDoc = null;
		String query=Query;
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("/User/@Loginid", loginId);
		Element input = WCMashupHelper.getMashupInput("XPEDXGetUserRole", valueMap, wcContext.getSCUIContext());
		String inputXml = SCXmlUtil.getString(input);
		log.debug("Input XML For Getting User Role : " + inputXml);
		Object obj = WCMashupHelper.invokeMashup("XPEDXGetUserRole", input, wcContext.getSCUIContext());
		outputDoc = ((Element) obj).getOwnerDocument();
		if (null != outputDoc) {
			log.debug("Output XML For Getting User Role : " + SCXmlUtil.getString((Element) obj));

            NodeList list = SCXmlUtil.getXpathNodes(outputDoc.getDocumentElement(), "/UserList/User/UserGroupLists/UserGroupList");

            Element reqNode;

            for (int i = 0; i < list.getLength(); i++) {

                  reqNode = (Element) list.item(i);
                  String UOM=reqNode.getAttribute("UsergroupKey");
                  log.debug("Roles:++++"+ UOM);
                  if(i==(list.getLength()-1))
      			{								
      				query=query+"'"+UOM+"'";
      			}
      			else
      			{
      				query=query+"'"+UOM+"',";
      			}
                       
            }

            query=query+")";
      }

	return query;
	}
	//Mashup End
	
	
	public void compareRpt() throws Exception{
		rb = new XPEDXReportBean();
		String dwcuid = "";
		String dwName = "";
		String dwkind = "";
		int dwid = 0;

		log.debug("BOE SIze="+listBOE.size());
		log.debug("DW SIze="+listDW.size());
		for (int i=0;i<listBOE.size();i++) {
			for (int j=0;j<listDW.size(); j++) {
				rb = new XPEDXReportBean();
				rb = (XPEDXReportBean)listDW.get(j);
				if(listBOE.get(i).equals(rb.getCuid())){

					
					dwcuid = rb.getCuid();
					log.debug("Equal CUIDs--->"+ dwcuid );
					dwName = rb.getName();
					log.debug("Equal Name--->"+ dwName );
					dwid = rb.getId();
					log.debug("Equal Id--->"+ dwid);
					dwkind= rb.getKind();
					log.debug("Equal Kind--->"+ dwkind);

					vallist.add(rb);
				}
			}
		}
		LOG.debug("Size Of Valid Report List After Comparision ="+vallist.size());
		session.setAttribute("ValidReports", vallist);
	}



	public List<XPEDXReportBean> getMyCustList() {
		return myCustList;
	}

	public void setMyCustList(List<XPEDXReportBean> myCustList) {
		this.myCustList = myCustList;
	}

	public List getMyList() {
		return myList;
	}

	public void setMyList(List myList) {
		this.myList = myList;
	}


	public List<XPEDXReportBean> getStdreportlist() {
		return stdreportlist;
	}

	public void setStdreportlist(List<XPEDXReportBean> stdreportlist) {
		this.stdreportlist = stdreportlist;
	}

	public List<XPEDXReportBean> getCustreportlist() {
		return custreportlist;
	}

	public void setCustreportlist(List<XPEDXReportBean> custreportlist) {
		this.custreportlist = custreportlist;
	}


	public List getVallist() {
		return vallist;
	}

	public void setVallist(List vallist) {
		this.vallist = vallist;
	}


	private static final Logger log = Logger
	.getLogger(XPEDXChangeReportingAction.class);
}

