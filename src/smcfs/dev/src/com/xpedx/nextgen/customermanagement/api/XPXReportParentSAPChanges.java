package com.xpedx.nextgen.customermanagement.api;        

import java.util.Properties;

import org.w3c.dom.Document;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.common.util.XPXUtils;
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
		
		log = YFCLogCategory.instance(XPXReportParentSAPChanges.class);
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
		
		XPXUtils utilObj = new XPXUtils();
		reportParentSAPChangeDoc = utilObj.stampBrandLogo(env, reportParentSAPChangeDoc);
		
		String custRecordType = reportParentSAPChangeDoc.getDocumentElement().getAttribute(XPXLiterals.A_CUSTOMER_RECORD_TYPE);		
		String subjectLine = "Notification: Parent SAP # change for a ".concat(custRecordType).concat(" Customer");
		reportParentSAPChangeDoc.getDocumentElement().setAttribute("Subject", subjectLine);
		
		String reportParentSAPChangeToEmailID = YFSSystem.getProperty("reportParentSAPChangeToEmailID");
		reportParentSAPChangeDoc.getDocumentElement().setAttribute("ToEmailID", reportParentSAPChangeToEmailID);
		
		log.info("Exiting XPXReportParentSAPChanges - reportParentSAPChanges : "
				+ SCXmlUtil.getString(reportParentSAPChangeDoc));
		
		return reportParentSAPChangeDoc;
		
	}	
	
	public void setProperties(Properties prop) throws Exception {
		this.prop = prop;
	}

}
