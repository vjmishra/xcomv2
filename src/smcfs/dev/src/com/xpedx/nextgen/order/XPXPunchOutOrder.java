package com.xpedx.nextgen.order;

import java.io.CharArrayReader;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Properties;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXPunchOutOrder implements YIFCustomApi {
	
	private static YIFApi api = null;
	private static YFCLogCategory log;
	private String xsltFileName = null;
	private String xsltVersion = null;

	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}
	
	public Document invokePunchOut(YFSEnvironment env,Document inXML) throws Exception {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		Element punchout = inXML.getDocumentElement();
		Element  headerInfo = SCXmlUtil.getChildElement(punchout, "HeaderInfo");
		if(!SCUtil.isVoid(headerInfo)) {
			xsltVersion = headerInfo.getAttribute("XSLTVer");
			xsltFileName = headerInfo.getAttribute("XSLTFileName");
			//xsltFileName = "ABCart_HTML.xsl";
			if(!SCUtil.isVoid(xsltFileName)) {
				//String xsltPath = "/global/template/xsl/punchout/"+xsltFileName;
				InputStream xslStream = XPXPunchOutOrder.class.getResourceAsStream(new StringBuilder().append("/global/template/xsl/punchout/").append(xsltFileName).toString());
				//XSL Conversion Starts here
				TransformerFactory tranFactory = TransformerFactory.newInstance();
				javax.xml.transform.URIResolver resolver = YFSSystem.getXSLURIResolver();
			    if(resolver != null)
			            tranFactory.setURIResolver(resolver);
				Transformer transformer =  tranFactory.newTransformer(new StreamSource(xslStream));
				StringWriter stw = new StringWriter();
		        StreamResult result = new StreamResult(stw);
		        StreamSource source = new StreamSource(new CharArrayReader(SCXmlUtil.getString(inXML).toCharArray()));;
				transformer.transform(source, result);
				StringWriter out = (StringWriter) result.getWriter();
				StringBuffer sb = out.getBuffer();
				String finalstring = sb.toString();
				YFCDocument oDoc = YFCDocument.parse(finalstring);
//				YFCDocument newDoc = YFCDocument.createDocument("PunchOutOutput");
//				YFCElement punchOutElement = newDoc.createElement("PunchOutOrderMessage");
//				String xml = SCXmlUtil.getString(oDoc.getDocument());
//				punchOutElement.setAttribute("OrderMessage", finalstring);
				return oDoc.getDocument();
			}
			else{
				log.error("The Punchout Xml does not have HeaderInfo.. So returning null document-- In the service XPXPunchOutOrderToAriba-- Class " +
						"XPXPunchOutOrder.class");
			}
		}
		return null;
	}

}
