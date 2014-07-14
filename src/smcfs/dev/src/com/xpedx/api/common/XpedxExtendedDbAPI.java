package com.xpedx.api.common;


import java.lang.reflect.Method;

import org.w3c.dom.Document;

//import com.xpedx.common.XMLUtil;
//import com.xpedx.common.XMLUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.ycp.ui.backend.YCPBackendUtils;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfs.japi.YFSEnvironment;

public class XpedxExtendedDbAPI extends  XpedxYIFCustomApi {

	/**
	 * XML Extended DB API.
	 * 
	 * The only purpose of this API is to rid the need to create a dummy service for every DB Extension.
	 * 
	 * Input document: 
	 * <XpedxExtendedDb>
	 * 	<API Name="SomeAPIName">
	 * 		<Input>
	 * 			<InputToSomeAPI1/>
	 * 		</Input>
	 * 	</API>
	 * 	<Template>
	 * 		<SomeTemplate>
	 * 	</Template>	
	 * </XpedxExtendedDb>
	 * 
	 * Output template:
	 * Template will be passed to cascading target API/Flow
	 */


	public Document invoke(YFSEnvironment env, Document inputDoc) throws Exception {

		//String apiName = XMLUtil.getString(inputDoc, "/XpedxExtendedDb/API/@Name");
		String apiName =SCXmlUtil.getXpathAttribute(inputDoc.getDocumentElement(), "/XpedxExtendedDb/API/@Name");

		YFCDocument inDoc = YFCDocument.getDocumentFor(inputDoc);

		YFCElement inputElement = inDoc.getDocumentElement().getChildElement("API").getChildElement("Input").getFirstChildElement();
		YFCDocument nextDoc = YFCDocument.createDocument();
		nextDoc.appendChild(nextDoc.importNode(inputElement, true));

		if (inDoc.getDocumentElement().getChildElement("Template")!=null) {
			YFCElement templateElement = inDoc.getDocumentElement().getChildElement("Template").getFirstChildElement();
			if (templateElement!=null) {
				YFCDocument templateDoc = YFCDocument.createDocument();
				templateDoc.appendChild(templateDoc.importNode(templateElement, true));
				env.setApiTemplate(apiName, templateDoc.getDocument());
			}
		}
		
		String apiClassName = YCPBackendUtils.getExtnApiClassName(apiName);
		String apiMethodName = YCPBackendUtils.getExtnApiMethodName(apiName);

		Class c = Class.forName(apiClassName);
		Method m = c.getMethod(apiMethodName, new Class[] {
				com.yantra.yfs.japi.YFSEnvironment.class,
				org.w3c.dom.Document.class });

		Document outputDoc = (Document) m.invoke(c.newInstance(), new Object[] { env, nextDoc.getDocument() });

		return outputDoc;
	}
}
