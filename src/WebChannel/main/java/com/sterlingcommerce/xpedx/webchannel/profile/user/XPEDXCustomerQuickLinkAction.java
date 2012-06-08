package com.sterlingcommerce.xpedx.webchannel.profile.user;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.Arrays;
import java.util.Map;

@SuppressWarnings("serial")
public class XPEDXCustomerQuickLinkAction extends WCMashupAction {

	private QuickLinkBean quickLinkBeanArray[];
	private String createSelected = "false";
	private String customerContactId;

	public String getCreateSelected() {
		return createSelected;
	}
	public void setCreateSelected(String createSelected) {
		this.createSelected = createSelected;
	}
	public QuickLinkBean[] getQuickLinkBeanArray() {
		return quickLinkBeanArray;
	}
	public void setQuickLinkBeanArray(QuickLinkBean[] quickLinkBeanArray) {
		this.quickLinkBeanArray = quickLinkBeanArray;
	}
	
	public String execute() {
		Element outputElement = null;
		try {
			outputElement = prepareAndInvokeMashup("getCustomerQuickLink");
			String inputXml = SCXmlUtil.getString(outputElement);
			LOG.debug("Input XML: " + inputXml);
		} catch (XMLExceptionWrapper e) {			
			LOG.debug("Not able to retrieve Customer Quick Link:->"
					+ e.getMessage());
			return ERROR;
		} catch (CannotBuildInputException e) {
			LOG.debug("Not able to build input xml for Customer Quick Link:->"
					+ e.getMessage());
			return ERROR;
		}
		
		QuickLinkBean quickLinkBean[] = null;
		
		try {
			quickLinkBean = getQuickLink(outputElement);
		} catch (RuntimeException re) {
			LOG.debug("Not able to parse output xml for Customer Quick Link:->"
					+ re.getMessage());
			if (getCreateSelected().equalsIgnoreCase("true")) 
				return "createsuccess";
			
				return ERROR;			
		}
		
		setQuickLinkBeanArray(quickLinkBean);
		
		if (getCreateSelected().equalsIgnoreCase("true"))
			return "createsuccess";
		
		return SUCCESS;
	}
	private QuickLinkBean[] getQuickLink(Element outputElement)throws RuntimeException {
		  
		
		Node quickLinkNodeList = outputElement.getFirstChild().getFirstChild().getFirstChild();
		if(!quickLinkNodeList.hasChildNodes())
			throw new RuntimeException();
		
		NodeList quickLinkNode = quickLinkNodeList.getChildNodes();
		QuickLinkBean quickLinkBean[] = new QuickLinkBean[quickLinkNode.getLength()];
		
		for (int i=0; i<quickLinkNode.getLength(); i++) {
			Element quickLinkElement = (Element)quickLinkNode.item(i);
			quickLinkBean[i] = new QuickLinkBean();
			quickLinkBean[i].setUrlName(quickLinkElement.getAttribute("QuickLinkName"));
			quickLinkBean[i].setQuickLinkURL(quickLinkElement.getAttribute("QuickLinkUrl"));
			quickLinkBean[i].setUrlOrder(Integer.parseInt(quickLinkElement.getAttribute("URLOrder")));
			quickLinkBean[i].setShowQuickLink(quickLinkElement.getAttribute("ShowQuickLink"));
		}
		
		Arrays.sort(quickLinkBean, new QuickLinkComparator());

		return quickLinkBean;
	}
	public String getCustomerContactId() {
		return customerContactId;
	}
	public void setCustomerContactId(String customerContactId) {
		this.customerContactId = customerContactId;
	}
	@Override
	protected void manipulateMashupInputs(Map<String, Element> mashupInputs)
			throws CannotBuildInputException {
		Element input = mashupInputs.get("getCustomerQuickLink");
		if(input !=null) {
			String customerId = XPEDXWCUtils.getLoggedInCustomerFromSession(wcContext);
			if(!(customerId!=null && customerId.trim().length()>0))
				customerId = getWCContext().getCustomerId();
			if(customerContactId!=null && customerContactId.trim().length()>0) {
				String contactId = input.getAttribute("CustomerContactID");
				if(!customerContactId.equalsIgnoreCase(contactId)) {
					input.setAttribute("CustomerContactID", customerContactId);
				}				
			}
			if(customerId!=null && customerId.trim().length()>0) {
				Element cusotmerElem = SCXmlUtil.getChildElement(input, "Customer");
				String passedCustomerId = cusotmerElem.getAttribute("CustomerID");
				if(!customerId.equalsIgnoreCase(passedCustomerId)) {
					cusotmerElem.setAttribute("CustomerID", customerId);
				}				
			}
		}
		// TODO Auto-generated method stub
		super.manipulateMashupInputs(mashupInputs);
	}
}
