package com.xpedx.nextgen.customermanagement.api;        

import java.util.Properties;

import org.w3c.dom.Document;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXReportParentSAPChanges implements YIFCustomApi {
	
	private static YIFApi api = null;
	private Properties prop;
	private static YFCLogCategory log;
	
	static {
		
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try {
			api = YIFClientFactory.getInstance().getApi();
		}
		catch (YIFClientCreationException yifEx) {
			yifEx.printStackTrace();
		}
	}
	
	/**
	 * This method gets invoked on hit of  XPXReportParentSAPChanges service.
	 * This method forms the complete input xml before sending an email (reporting Parent SAP changes) to ebusiness@xpedx.com
	 * @param env
	 * @param inXML
	 * @return
	 * @throws Exception
	 */
	public Document reportParentSAPChanges(YFSEnvironment env, Document reportParentSAPChangeDoc) throws Exception
	{
		
		log.info("Entering XPXReportParentSAPChanges - reportParentSAPChanges : "
				+ SCXmlUtil.getString(reportParentSAPChangeDoc));
		api = YIFClientFactory.getInstance().getApi();
		/*Begin - Changes made by Mitesh Parikh for JIRA 3283*/
		String emailBrand = reportParentSAPChangeDoc.getDocumentElement().getAttribute(XPXLiterals.A_SELLER_ORGANIZATION_CODE) + ".com";
		StringBuffer subjectLine = new StringBuffer(emailBrand); 
		subjectLine.append(" Parent SAP # Changed Notification in ").append(YFSSystem.getProperty("environment")).append(" environment");
		reportParentSAPChangeDoc.getDocumentElement().setAttribute("Subject", subjectLine.toString());
		
		StringBuffer reportParentSAPChangeEmailID = new StringBuffer();
		reportParentSAPChangeEmailID.append(YFSSystem.getProperty("fromAddress.username")).append("@").append(emailBrand);
		reportParentSAPChangeDoc.getDocumentElement().setAttribute("FromEmailID", reportParentSAPChangeEmailID.toString());
		reportParentSAPChangeDoc.getDocumentElement().setAttribute("ToEmailID", YFSSystem.getProperty("reportParentSAPChangeToEmailID")) ;
		/*End - Changes made by Mitesh Parikh for JIRA 3283*/
		log.info("Exiting XPXReportParentSAPChanges - reportParentSAPChanges : "
				+ SCXmlUtil.getString(reportParentSAPChangeDoc));
		
		return reportParentSAPChangeDoc;
		
	}	
	
	public void setProperties(Properties prop) throws Exception {
		this.prop = prop;
	}

}
