package com.xpedx.nextgen.common.util;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.dashboard.XPXAddParametersAPI;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXUOMDetailsSwapUtil implements YIFCustomApi
{
	private static YFCLogCategory log;
	private static YIFApi api = null;
	Properties props;
	
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try 
		{
			api = YIFClientFactory.getInstance().getApi();
		}
		catch (YIFClientCreationException e1) {

			e1.printStackTrace();
		}
	}
	

	public Document swapUOMDetails(YFSEnvironment env,Document inputXML)
	{
		String xpathValue="";
		String populateDesc ="";
		
		
		HashMap descriptionToCodeMap = new HashMap();
		HashMap codeToDescriptionMap = new HashMap();
		ArrayList orderLineElementList = new ArrayList();
		ArrayList nonOrderLineElementList = new ArrayList();
		
		if(props != null){
            Enumeration e = props.propertyNames();
            while(e.hasMoreElements()){
                String name = (String)e.nextElement();
                
                if(name.equalsIgnoreCase("XPathValue"))
                {
                xpathValue = props.getProperty(name);
                }
                else if(name.equalsIgnoreCase("populateDesc"))
                {
                	populateDesc = props.getProperty(name);
                }
                	
                
                log.debug("Propery Name is: " + name + " Value is: " + props.getProperty(name));
            }
        }
		
		log.debug("The inputXML is: "+SCXmlUtil.getString(inputXML));
		

		  int indexOfLastElement = xpathValue.lastIndexOf("/");
		  int indexOfAttribute = xpathValue.lastIndexOf("@");
		  
		  String elementXpath = xpathValue.substring(0, indexOfLastElement);
		  String attributeName =  xpathValue.substring(indexOfAttribute+1);	
		  
		  Element inputXMLRoot = inputXML.getDocumentElement();
		 		
		  NodeList elementList = SCXmlUtil.getXpathNodes(inputXMLRoot, elementXpath);
		  
		  for (int i=0; i<elementList.getLength(); i++)
		  {
			  Element attributeElement = (Element)elementList.item(i);
			  
			  log.debug("The attribute is: "+attributeElement.getAttribute(attributeName));
			  
			  String UOMCode = attributeElement.getAttribute(attributeName);
			  
			  String uomValue = "";
			  String uomDescription = "";
			  
			  Document getUOMListInputDoc = createGetUOMListInputDoc(UOMCode,populateDesc);
			  
			  if(populateDesc.equalsIgnoreCase(XPXLiterals.BOOLEAN_FLAG_N))
			  {
				  
				  
				  if(!descriptionToCodeMap.containsKey(UOMCode))
				  {
					  Document getUOMListOutputDoc = invokeGetUomList(env,getUOMListInputDoc);
					  
					  Element uomElement = (Element) getUOMListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_UOM).item(0);
					  
					  if(uomElement!= null)
					  {
						  uomDescription = uomElement.getAttribute(XPXLiterals.A_UOM_DESCRIPTION);
					  }
				  }
				  
				  else
					  
				  {
					  uomDescription = (String)descriptionToCodeMap.get(UOMCode);
				  }	  
				
				  if(uomDescription!=null &&  uomDescription.trim().length()!=0)
				  {
						  
					   /* Document changeOrderELementInputDoc = createChangeOrderElementInputDoc(attributeElement,uomDescription,attributeName);
						  
					    if(attributeElement.getNodeName().equalsIgnoreCase(XPXLiterals.E_ORDER_LINE))
						{
						        	orderLineElementList.add(changeOrderELementInputDoc);
						}
						else
						{
						        	nonOrderLineElementList.add(changeOrderELementInputDoc);
				         }
						  */
					    attributeElement.setAttribute(attributeName,uomDescription);
					    descriptionToCodeMap.put(UOMCode, uomDescription); 
					    
				    }
			  }
			  
			  else if(populateDesc.equalsIgnoreCase(XPXLiterals.BOOLEAN_FLAG_Y))
			  {
				  
				  if(!codeToDescriptionMap.containsKey(UOMCode))
				  {
					  
					  Document getUOMListOutputDoc = invokeGetUomList(env,getUOMListInputDoc);
					  
					  log.debug("The output of getUomList is: "+SCXmlUtil.getString(getUOMListOutputDoc));
					  
					  Element uomElement = (Element) getUOMListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_UOM).item(0);
					  
					  if(uomElement!= null)
					  {
						  uomValue = uomElement.getAttribute(XPXLiterals.E_UOM);
					  }
				  }
				  
				  else
					  
				  {
					  
					   uomValue = (String)codeToDescriptionMap.get(UOMCode);
					  
				  }	  
				
				  if(uomValue!=null &&  uomValue.trim().length()!=0)
				  {
					     
					    /*Document changeOrderELementInputDoc = createChangeOrderElementInputDoc(attributeElement,uomValue,attributeName);
						  
					    if(attributeElement.getNodeName().equalsIgnoreCase(XPXLiterals.E_ORDER_LINE))
						{
						        	orderLineElementList.add(changeOrderELementInputDoc);
						}
						else
						{
						        	nonOrderLineElementList.add(changeOrderELementInputDoc);
				         }*/
						  
					    log.debug("The UOM value used for replacement is: "+uomValue);
					    attributeElement.setAttribute(attributeName,uomValue);
					    codeToDescriptionMap.put(UOMCode, uomValue);  
					    
				    }
			  }
			  
		  }
			
		  log.debug("The outputXML is: "+SCXmlUtil.getString(inputXML));  
		  
		return inputXML;
	}
	
	
	


	private Document createChangeOrderElementInputDoc(Element attributeElement, String attributeToReplace, String attributeReplaced) {
		
		YFCDocument createChangeOrderElementInputDoc = YFCDocument.createDocument(attributeElement.getNodeName());
		
		createChangeOrderElementInputDoc.getDocumentElement().setAttribute(attributeToReplace, attributeReplaced);
		
		return createChangeOrderElementInputDoc.getDocument();
		
	}


	private Document invokeGetUomList(YFSEnvironment env, Document getUOMListInputDoc) {

            Document getUomListOutputDoc = null;
			try {
				getUomListOutputDoc = api.invoke(env, XPXLiterals.GET_UOM_LIST_API,getUOMListInputDoc);
			} catch (YFSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            return getUomListOutputDoc;
		 
	}


	private Document createGetUOMListInputDoc(String code, String populateDesc) {
		
		YFCDocument getUOMListInputDoc = YFCDocument.createDocument(XPXLiterals.E_UOM);
		
		if(populateDesc.equalsIgnoreCase(XPXLiterals.BOOLEAN_FLAG_N))
		{
		getUOMListInputDoc.getDocumentElement().setAttribute(XPXLiterals.E_UOM, code);
		}
		else
		{
			getUOMListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_UOM_DESCRIPTION, code);	
		}
		//getUOMListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_ORGANIZATION_CODE,XPXLiterals.A_ORGANIZATIONCODE_VALUE);
				
		return getUOMListInputDoc.getDocument();
	}


	public void setProperties(Properties props) throws Exception {
		
		this.props = props;
		
	}
	
	
	

}
