package com.xpedx.nextgen.dashboard;

import java.util.Properties;

import org.w3c.dom.Document;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXSetOrderEditFlag implements YIFCustomApi {

	private static YFCLogCategory log;
	Properties props;
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");		
	}	

	public Document setOrderEditFlag(YFSEnvironment env, Document changeOrderXml) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("XPXSetOrderEditFlag_InputXML: "+ SCXmlUtil.getString(changeOrderXml));
			}
			
			if(changeOrderXml!=null) {
				changeOrderXml.getDocumentElement().setAttribute("IsOrderEdit", "true");
			}
			
			if (log.isDebugEnabled()) {
				log.debug("XPXSetOrderEditFlag_OutputXML: "+ SCXmlUtil.getString(changeOrderXml));
			}			

		} catch (Exception e) {
			log.error("Error while executing setOrderEditFlag method in XPXSetOrderEditFlag class: "+ e.getMessage());
			
		}

		return changeOrderXml;
	}	

	@Override
	public void setProperties(Properties arg0) throws Exception {
		this.props = props;

	}
}