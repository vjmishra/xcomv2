package com.sterlingcommerce.xpedx.webchannel.services;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.reports.service.Report;
import com.reports.service.ReportList;
import com.reports.service.ReportService;
import com.reports.service.webi.ReportUtils;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfs.core.YFSSystem;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.http.HttpHost;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@SuppressWarnings("serial")
public class XPEDXGetAllReportsAction extends WCMashupAction {
	List<Report> customReportList;
	List<Report> standardReportList;
	List<Report> validCustomReportList;
	List<Report> dataExchangeReportList;
	Map<String,List<Report>> mapOfReports;
	
	public String getCustomerNo(String customerID) {
		String[] custDetails = customerID.split("-");
		String suffix = custDetails[1];
		return suffix;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see com.sterlingcommerce.webchannel.core.WCMashupAction#execute()
	 * This action class logs onto BI and retrieves a list of all the standard, then custom reports. 
	 * It adds the standard reports to the standardReportList
	 * public variable to use on the JSP. Then compares the the user's entitled custom 
	 * reports to the list retrieved from BI. If any match, they are added to the ValidCustomReportList public variable to display to the user. 
	 */
	public String execute() {
		String username;
		String password;
		String authentication;
		String CMS;
		String standard_folder_id = "";
		String custom_folder_id = "";
		HttpHost _target = null;
		String wcPropertiesFile = "xpedx_reporting.properties";		
		XPEDXWCUtils.loadXPEDXSpecficPropertiesIntoYFS(wcPropertiesFile);		
		ReportUtils ru = new ReportUtils();
		ArrayList<String> logonTokens = null;
		
		try {
			//ML - changed logic to read CMS Info from property file only once. 
			Map<String,String> CMSLogonDetails = ReportUtils.getCMSLogonDetails();
			username = CMSLogonDetails.get("username").toString();
			password = CMSLogonDetails.get("password").toString();
			authentication = CMSLogonDetails.get("authentication").toString();
			standard_folder_id = CMSLogonDetails.get("standard_folder_id").toString();
			custom_folder_id = CMSLogonDetails.get("custom_folder_id").toString();
			CMS = CMSLogonDetails.get("CMS").toString();
			_target = ru.getHttpHost(CMS);
			
			//ML:Find out if logonTokens is in session. If not, then go get a new one and store it in the session. 
			//this logic should be reused across this "Report List" page, the "Report Details" page, and the ReportDisplay page to avoid
			//creating too many sessions and violate the SAP license agreement. 
			logonTokens = (ArrayList) request.getSession().getAttribute("logonTokens"); 
			if ((logonTokens == null) || (logonTokens.size() != 2)) {				
				logonTokens = ru.logonCMS(username, password, authentication, _target);
				//store the logonTokens in session for future use
				request.getSession().setAttribute("logonTokens", logonTokens);
			}							
			
		
		} catch (Exception e) {
			System.out.println("Could not logon to BI/CMS using the supplied credentials.");
			e.printStackTrace();
		}
		Boolean isOK = true;
		if (logonTokens.size() < 2) {
			System.out.println("No Tokens Found");
			isOK = false;
		}
		if (isOK) {			
			try {
				standardReportList = ru.getAllDocuments(_target, logonTokens.get(0), standard_folder_id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				customReportList = ru.getAllDocuments(_target, logonTokens.get(0), custom_folder_id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			getConnection();
		} catch (SQLException e) {			
				e.printStackTrace();
		}
		compareRpt();

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
			dataExchangeReportList = new ArrayList<Report>();
			if(test==true)
			{				
				rs = stmt.getResultSet();
				while(rs.next())
				{
					Report report = new Report();
					report.setCuid(rs.getString("RPT_CUID"));
					report.setName(rs.getString("RPT_NAME"));
					report.setKind(rs.getString("RPT_KIND"));
					report.setId(rs.getInt("RPT_ID"));
					report.setDescription(rs.getString("RPT_DESC"));

					dataExchangeReportList.add(report);					
				}				
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
	
	
	public void compareRpt() {
		validCustomReportList = new ArrayList<Report>();
		if(customReportList!=null){
		for (int i=0;i<customReportList.size();i++) {
			for (int j=0;j<dataExchangeReportList.size(); j++) {
				if(customReportList.get(i).getCuid().equals(dataExchangeReportList.get(j).getCuid())){
					Report valReport = new Report();
					valReport.setCuid(customReportList.get(i).getCuid());
					valReport.setDescription(customReportList.get(i).getDescription());
					valReport.setId(customReportList.get(i).getId());
					valReport.setKind(customReportList.get(i).getKind());
					valReport.setName(customReportList.get(i).getName());
					validCustomReportList.add(valReport);
				}
				}
			}
		}		
	}

	public List<Report> getCustomReportList() {
		return customReportList;
	}

	public void setCustomReportList(List<Report> customReportList) {
		this.customReportList = customReportList;
	}

	public List<Report> getStandardReportList() {
		return standardReportList;
	}

	public void setStandardReportList(List<Report> standardReportList) {
		this.standardReportList = standardReportList;
	}

	public List<Report> getValidCustomReportList() {
		return validCustomReportList;
	}

	public void setValidCustomReportList(List<Report> validCustomReportList) {
		this.validCustomReportList = validCustomReportList;
	}

	public List<Report> getDataExchangeReportList() {
		return dataExchangeReportList;
	}

	public void setDataExchangeReportList(List<Report> dataExchangeReportList) {
		this.dataExchangeReportList = dataExchangeReportList;
	}

	private static final Logger log = Logger
	.getLogger(XPEDXGetAllReportsAction.class);
}

