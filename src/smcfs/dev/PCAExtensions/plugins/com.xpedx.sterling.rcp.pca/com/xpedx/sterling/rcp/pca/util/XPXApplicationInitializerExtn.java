package com.xpedx.sterling.rcp.pca.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.xpedx.sterling.rcp.pca.util.XPXUtils;
import com.yantra.yfc.rcp.IYRCApiCallbackhandler;
import com.yantra.yfc.rcp.IYRCApplicationInitializer;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXApplicationInitializerExtn implements IYRCApplicationInitializer  {

	@Override
	public void initalize() {
		String[] apinames = null;
		Document[] docInput = null;
		Document docLDAPSearchInputQry=YRCXmlUtils.createFromString("<Organization />");
		docLDAPSearchInputQry.getDocumentElement().setAttribute("IsNode", "Y");
		
		Document inXML = XPXUtils.getOrganizationListInput(YRCPlatformUI.getUserId()).getOwnerDocument();
		
		apinames = new String[]{"getOrganizationList","getAllOrganizationsForUser"};
    	docInput = new Document[]{docLDAPSearchInputQry, inXML};
		YRCApiContext ctx = new YRCApiContext();
		ctx.setFormId(this.getFormId());
		ctx.setApiNames(apinames);
		ctx.setInputXmls(docInput);
		YRCPlatformUI.callApi(ctx, new IYRCApiCallbackhandler() {

			// --handleApiCompletion function used to call API & fetch the
			// response from API
			public void handleApiCompletion(YRCApiContext ctx) {
				if (ctx.getInvokeAPIStatus() > 0) {
					String[] apinames= ctx.getApiNames();
		        	for(int i=0; i< apinames.length;i++){
		        		String apiname = apinames[i];
		        		if (apiname.equals("getOrganizationList")) {
							Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
							XPXUtils.createOrganizationList(outXml);
							// setModel("XPXCustomerAssignmentList",outXml);
						}
						else if (apiname.equals("getAllOrganizationsForUser")) {
							Element outXml = ctx.getOutputXmls()[i].getDocumentElement();
							XPXUtils.createOrganizationListElemForUser(outXml);
							// setModel("XPXCustomerAssignmentList",outXml);
						}
		        	}//api loop ends
				}

				// In case of Invoke API failure
				else if (ctx.getInvokeAPIStatus() == -1) {
					Element outXml = ctx.getOutputXml().getDocumentElement();
					if ("Errors".equals(outXml.getNodeName())) {
						YRCPlatformUI.trace( "Failed while getting Customer Child List: ", outXml);
						//YRCPlatformUI.showError("Failed!", "Unable to get Customer Child List.");
					}

				}

			}
		});

	}


	 
	private String getFormId() {
		return "com.xpedx.sterling.rcp.pca.util.XPXApplicationInitializerExtn";
	}





}
