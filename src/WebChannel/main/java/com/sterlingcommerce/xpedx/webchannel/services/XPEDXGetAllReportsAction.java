package com.sterlingcommerce.xpedx.webchannel.services;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.reports.service.Report;
import com.reports.service.ReportList;
import com.reports.service.ReportService;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfs.core.YFSSystem;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@SuppressWarnings("serial")
public class XPEDXGetAllReportsAction extends WCMashupAction {
	List<Report> customReportList;
	List<Report> standardReportList;
	List<Report> validCustomReportList;
	List<Report> dataExchangeReportList;
	
	public String getCustomerNo(String customerID) {
		String[] custDetails = customerID.split("-");
		String suffix = custDetails[1];
		return suffix;
	}
	
	public String execute() {
		XPEDXReportService reportService = new XPEDXReportService();
		ReportService intReportService = reportService.getReportService();
		ReportList reportList = intReportService.getReports();
		customReportList = reportList.getCustReportList();
		standardReportList = reportList.getStdReportList();
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

