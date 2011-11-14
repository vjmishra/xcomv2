package com.xpedx.sterling.rcp.pca.customerDetails.Contributor;

import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.yantra.yfc.rcp.IYRCRelatedTasksExtensionContributor;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCRelatedTask;
import com.yantra.yfc.rcp.YRCXmlUtils;
import com.yantra.yfc.rcp.internal.YRCApiCaller;

/**
 * 
 *
 */
public class XPXCustomerEditorContributor implements
		IYRCRelatedTasksExtensionContributor {

	@Override
	public boolean acceptTask(YRCEditorInput arg0, YRCRelatedTask arg1) {
		String inputTaskId = arg1.getId();

		//if the customer is a Master customer only then the Create customer contact link is displayed.
		//We cannot create customer contacts at any other level than MSAP level.
		if(inputTaskId.equals("YCD_TASK_CUSTOMER_CONTACT_ENTRY"))
		{
			String customerKey = arg0.getAttributeValue("Customer/@CustomerKey");
			YRCApiContext context= new YRCApiContext();
			YRCApiCaller syncapiCaller= new YRCApiCaller(context,true);
			String apiInput ="<Customer CustomerKey=\""+customerKey+"\" />";
			context.setFormId("com.yantra.pca.ycd.rcp.tasks.customerDetails.wizards.YCDCustomerWizard");
			context.setApiName("getCustomerList");
			Document inputXml=YRCXmlUtils.createFromString(apiInput);
			context.setInputXml(inputXml);
			syncapiCaller.invokeApi();
			//get the customer details
			Document outputXml =context.getOutputXml();
			String suffixType = "";
			NodeList customerList = outputXml.getElementsByTagName("Customer");
			int customerLength = customerList.getLength();
			Element customerElement = null;
			if(customerLength != 0 ){
				customerElement = (Element)customerList.item(0);
				Element extnElement = YRCXmlUtils.getChildElement(customerElement, "Extn");
				suffixType = extnElement.getAttribute("ExtnSuffixType");
			}
			if(!suffixType.equals("MC"))
				return false;
		}
		return true;
	}

	@Override
	public boolean canExecuteNewTask(YRCEditorInput arg0, YRCRelatedTask arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Composite createPartControl(Composite arg0, YRCEditorInput arg1,
			YRCRelatedTask arg2) {
		// TODO Auto-generated method stub
			
		return null;
	}

}
