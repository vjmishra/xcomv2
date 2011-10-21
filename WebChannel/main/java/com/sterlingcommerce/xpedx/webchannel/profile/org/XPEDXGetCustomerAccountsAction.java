/**
 * 
 */
package com.sterlingcommerce.xpedx.webchannel.profile.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;

/**
 * @author Krithika S
 *
 */
public class XPEDXGetCustomerAccountsAction extends WCMashupAction {
	
	private String customerKey;
	
	private HashMap<String, String> accountsMap;
	
	
	public String getCustomerKey() {
		return customerKey;
	}

	public void setCustomerKey(String customerKey) {
		this.customerKey = customerKey;
	}



	public HashMap<String, String> getAccountsMap() {
		return accountsMap;
	}



	public void setAccountsMap(HashMap<String, String> accountsMap) {
		this.accountsMap = accountsMap;
	}



	public String execute() {
		
		String msapCustomerKey = (String)wcContext.getSCUIContext().getLocalSession().getAttribute(XPEDXWCUtils.LOGGED_IN_CUSTOMER_KEY);
		Document outDoc = null;
		
		try {	
			//Call the mashup only when customer key is not null
			if(msapCustomerKey!=null && wcContext.getStorefrontId()!=null){
				setCustomerKey(msapCustomerKey);
				Map<String, Element> output = prepareAndInvokeMashups();
				if (output.values().iterator().next() != null) {
					outDoc = (Document) output.values().iterator().next()
							.getOwnerDocument();
				}
			}
			ArrayList<String> childCustomerList = new ArrayList<String>();
			if(outDoc!=null)
			{
				NodeList customerListElem = outDoc.getElementsByTagName("Customer");
				if(customerListElem!=null && customerListElem.getLength()>0)
				{
					for(int i=0;i<customerListElem.getLength();i++)
					{
						Element customerElem = (Element) customerListElem.item(i);
						if(customerElem!=null)
							childCustomerList.add(SCXmlUtil.getAttribute(customerElem, "CustomerID"));
					}
				}
			}
			
			if(childCustomerList!=null && childCustomerList.size()>0)
			{
				setAccountsMap((HashMap<String, String>) XPEDXWCUtils.custFullAddresses(childCustomerList, wcContext.getStorefrontId()));
			}
		} catch (XMLExceptionWrapper e) {
			e.printStackTrace();
			return ERROR;
		} catch (CannotBuildInputException e) {
			
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}

}
