/**
 * 
 */
package com.sterlingcommerce.xpedx.webchannel.common.eprocurement;

import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.webchannel.common.eprocurement.CXMLMessageFields;
import com.sterlingcommerce.webchannel.core.WCException;
import com.yantra.yfc.util.YFCCommon;

import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;

/**
 * @author adsouza
 *
 */
public class XPEDXCXMLMessageFields extends CXMLMessageFields  {

	public XPEDXCXMLMessageFields(Document doc) {
		super(doc);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Return the user name from the CXML document
	 * @return String
	 * @exception WCException
	 */
     public String getAuthUser(String AuthUserXPath, String CustIdentity) throws WCException
     {
    	 //String AuthUserXPath = XPEDXWCUtils.getAuthUserXPathForCustomerIdentity(getCustomerIdentity());
    	 if(_loginID == null)
    	 {
	    	 Document doc = this.getRequestDoc();
	    	 if(!YFCCommon.isVoid(doc))
	    	 {
	    		 Element docElement = doc.getDocumentElement();
	    		 //List attrElement = SCXmlUtils.getElements(docElement,"/Header/From/Credential/Identity");
	    		 List attrElement = null;
	    		 
	    		 
	    		 if((AuthUserXPath != null) && AuthUserXPath.contains("[")){
	    			 String elementName = AuthUserXPath.substring(AuthUserXPath.indexOf("[") + 2, AuthUserXPath.indexOf("="));
	    			 String elementVal = AuthUserXPath.substring(AuthUserXPath.indexOf("=") + 2, AuthUserXPath.indexOf("]")-1);
	    			 AuthUserXPath = AuthUserXPath.substring(0,AuthUserXPath.indexOf("["));
	    			 
	    			 attrElement = SCXmlUtils.getElementsByAttribute(docElement,AuthUserXPath,elementName,elementVal);
	    		 }
	    		 else if((AuthUserXPath != null) && !AuthUserXPath.contains("[")){
	    			 attrElement = SCXmlUtils.getElements(docElement,AuthUserXPath);
	    		 }
	    		 
	    		 if(!YFCCommon.isVoid(attrElement) && attrElement.size() > 0)
	        	 {
	    			 Element element = (Element)attrElement.get(0);
	    			 _loginID = element.getTextContent();
	        	 }
	    		 if(_loginID!=null && _loginID.trim().length() == 0){
	    			 _loginID = null;
	    		 }
	    		 if(_loginID == null){
	    			 _loginID = CustIdentity;
	    		 }
	    	 }
    	 }
    	return _loginID;
     }
     
     
 	/**
 	 * Return the Customer Identity from the CXML document
 	 * @return String
 	 * @exception WCException
 	 */
      public String getCustomerIdentity() throws WCException
      {
     	 if(_customerID == null)
     	 {
 	    	 Document doc = this.getRequestDoc();
 	    	 if(!YFCCommon.isVoid(doc))
 	    	 {
 	    		 Element docElement = doc.getDocumentElement();
 	    		 List attrElement = SCXmlUtils.getElements(docElement,"/Header/From/Credential/Identity");
 	    		 if(!YFCCommon.isVoid(attrElement) && attrElement.size() > 0)
 	        	 {
 	    			 Element element = (Element)attrElement.get(0);
 	    			_customerID = element.getTextContent();
 	        	 }    		 
 	    	 }
     	 }
     	return _customerID;
      }     

	private static final Logger log = Logger.getLogger(XPEDXCXMLMessageFields.class);
	private String _loginID = null;
	private String _customerID = null;
}
