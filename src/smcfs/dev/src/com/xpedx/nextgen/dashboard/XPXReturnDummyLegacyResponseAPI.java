package com.xpedx.nextgen.dashboard;

import java.io.File;
import java.util.Enumeration;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXReturnDummyLegacyResponseAPI implements YIFCustomApi{
	
	Properties props;
	
	private static YFCLogCategory log;

	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	}
	public Document dummyResponse(YFSEnvironment env, Document inputXML) throws Exception
	{
		String filePath = null;
		
		log.debug("The input doc is: "+SCXmlUtil.getString(inputXML));
		
		
		if(props != null){
            Enumeration e = props.propertyNames();
            while(e.hasMoreElements()){
                String name = (String)e.nextElement();
                
                if(name.equalsIgnoreCase("FilePath"))
                {
                filePath = props.getProperty(name);
                }
                log.debug("Property Name is: " + name + " Value is: " + props.getProperty(name));
            }
		}
		  File file = new File(filePath);
		  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		  DocumentBuilder db = dbf.newDocumentBuilder();
		  Document outputDoc = db.parse(file);
		  
		  log.debug("The output doc is: "+SCXmlUtil.getString(outputDoc));
		  
		  return outputDoc;
	}

	public void setProperties(Properties props) throws Exception {
		this.props = props;
		
	}

}
