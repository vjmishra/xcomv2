package com.xpedx.sterling.rcp.pca.sharedTasks.shiptolookup;

import java.util.HashSet;

import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXShowListOfAuthorizedLocationsPanelBehaviour extends YRCBehavior {
	
	private Element elePageInput;
	private XPXShowListOfAuthorizedLocationsPanel page;
	private static final String COMMAND_GET_LIST_OF_SHIPTO="XPXGetListOfAssignedShipTosForAUserService";
	
	public XPXShowListOfAuthorizedLocationsPanelBehaviour(Composite ownerComposite, String formId, Element elePageInput) {
        super(ownerComposite, formId);  
        this.elePageInput = elePageInput;        
        this.page = (XPXShowListOfAuthorizedLocationsPanel)ownerComposite;
        initPage();
    }
	
	public void initPage() {
		
		if(null != elePageInput){
			Document input = YRCXmlUtils.createFromString("<XPXCustomerAssignmentView UserId='' ShipToAddressString='' ShipToAddressStringQryType='LIKE' StatusQryType='NE'><OrderBy><Attribute Desc='N' Name='ShipToCustomerID' /></OrderBy></XPXCustomerAssignmentView>");		
			input.getDocumentElement().setAttribute("UserId", elePageInput.getAttribute("UserID"));			
			setModel("Input_getAssignedShipToList", input.getDocumentElement());
			getAuthorizedLocations();
		}		
		
	}
	
	private void getAuthorizedLocations() {
		YRCApiContext context = new YRCApiContext();
		context.setFormId("com.xpedx.sterling.rcp.pca.sharedTasks.shiptolookup.XPXShowListOfAuthorizedLocationsPanel");	
		String[] apinames = {COMMAND_GET_LIST_OF_SHIPTO};
		Document[] docInput = {getModel("Input_getAssignedShipToList").getOwnerDocument()};	
		context.setInputXmls(docInput);
		context.setApiNames(apinames);
		callApi(context);
	}
	
	@Override
	public void handleApiCompletion(YRCApiContext apiContext) {
		if (apiContext.getInvokeAPIStatus() > 0){			
				String[] apinames = apiContext.getApiNames();
				for (int i=0; i<apinames.length; i++) {
					String apiname = apinames[i];
					if(YRCPlatformUI.equals(apiname, COMMAND_GET_LIST_OF_SHIPTO)){
		     			Document docOutput = apiContext.getOutputXmls()[i];
						if (docOutput != null) {
							HashSet<String> set = new HashSet<String>();
							Element eleOutput = docOutput.getDocumentElement(); 							
							NodeList customerList = (NodeList) eleOutput.getElementsByTagName("XPXCustomerAssignmentView");
							if (customerList != null && customerList.getLength()>0) {
								for(int j=0;j<customerList.getLength();j++){
									Element eleCustomer = (Element)customerList.item(j);
									set.add(eleCustomer.getAttribute("BillToCustomerID"));								
								}
							}
							eleOutput.setAttribute("TotalNumberOfBillToRecords", String.valueOf(set.size()));
							setModel("XPXCustomerAssignmentViewList",eleOutput);
						}
		     		}
				}			
		}
		super.handleApiCompletion(apiContext);
	}
}
