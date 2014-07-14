package com.xpedx.nextgen.customermanagement.api;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.client.ClientVersionSupport;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfs.japi.YFSEnvironment;

import edu.emory.mathcs.backport.java.util.Collections;

public class XPXGetAvailableLocationsService  implements YIFCustomApi{

	ArrayList<Element> availableLocatins=new ArrayList<Element>();
	ArrayList<Element> authorizedLocations=new ArrayList<Element>();
	
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public Document getAvailableLocations(YFSEnvironment p_env,Document p_dInXML) throws Exception{
		Document doc=SCXmlUtil.createDocument("XPXCustViewList");
		getRulesDocFromEnv (p_env);
		Collections.sort(availableLocatins,new XpedxAvailableLocationComparator());// {
		/*	@Override
			public int compare(Element elem, Element elem1) {		
				//sort on PrimeLineNo
				String customerPath1 = elem.getAttribute("CustomerPath"); 
				String customerPath2 = elem1.getAttribute("CustomerPath");
				
				return 		customerPath1.compareTo(customerPath2);
				
			}
		}); */
		for(Element availableLocatin:availableLocatins)
		{
			Element custViewElement=SCXmlUtil.createChild(doc.getDocumentElement(), "XPXCustView");
			custViewElement.setAttribute("CustomerID", availableLocatin.getAttribute("CustomerID"));
			custViewElement.setAttribute("CustomerPath", availableLocatin.getAttribute("CustomerPath"));
			custViewElement.setAttribute("CustomerAddress", availableLocatin.getAttribute("CustomerAddress"));
		}
		return doc;
	}
	
private void getRulesDocFromEnv (YFSEnvironment env) {
		
	
		if (env instanceof ClientVersionSupport) {
			ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
			HashMap envVariablesmap = clientVersionSupport.getClientProperties();
			if (envVariablesmap != null) {
				// Order Placed From Web. Ship To Customer Profile Has Been Set In Environment By WC Application.
				authorizedLocations = (ArrayList<Element>) envVariablesmap.get("assignedCustomers");
				availableLocatins = (ArrayList<Element>) envVariablesmap.get("availableCustomers");
			} 
		}
	}
}
