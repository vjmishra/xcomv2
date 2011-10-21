package com.xpedx.sterling.rcp.pca.usermanagement.extn;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.yantra.yfc.rcp.IYRCComposite;
import com.yantra.yfc.rcp.YRCApiContext;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCWizardExtensionBehavior;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXEnterpriseUserEntryWizardExtnBehavior extends
YRCWizardExtensionBehavior {

	
	private Element getUserGroupListOutXml;
	private boolean isModelSet = false;

	/**
	 * This method initializes the behavior class.
	 */
	public void init() {

	}

	public void handleApiCompletion(YRCApiContext ctx) {
		if (ctx.getInvokeAPIStatus() > 0){
				if (ctx.getApiName().equals("getUserGroupList")) {
					getUserGroupListOutXml = ctx.getOutputXml().getDocumentElement();
					if(!isModelSet)
					{
						populateModel();						
					}
				}
			}
		//In case of Invoke API failure
		else if(ctx.getInvokeAPIStatus()==-1){
				Element outXml = ctx.getOutputXml().getDocumentElement();
				if("Errors".equals(outXml.getNodeName())){
					YRCPlatformUI.trace("Failed while getting Customer Child List: ", outXml);
					YRCPlatformUI.showError("Failed!", "Unable to get Customer Child List.");
				}
		}
		super.handleApiCompletion(ctx);
	}
    /*
     * creates model with all the user groups from the API output and repopulates.
     *
     */	
	private void populateModel() {
		Element eleUserGroupLists = getModel("getUserGroupLists_output");
		if(!YRCPlatformUI.isVoid(eleUserGroupLists)&& getUserGroupListOutXml!= null)
		{
			eleUserGroupLists =createNewModel(getUserGroupListOutXml,eleUserGroupLists);
			setDirty(false);
			repopulateModel("getUserGroupLists_output");
		}
	}
    /*
     * Method to create model with all the user groups from the API output.
     *
     */	
	private Element createNewModel(Element outXml, Element eleUserGroupLists) {
		Element tempUserGroupLists = eleUserGroupLists;

		NodeList userGroupNodeList = outXml.getElementsByTagName("UserGroup");
		for (int i = 0; i < userGroupNodeList.getLength(); i++) {

			Element userGroupElement = (Element) userGroupNodeList.item(i);
			boolean isAssignedGroup = false;
			NodeList tempUserGroupNodeList = tempUserGroupLists
					.getElementsByTagName("UserGroupList");
			for (int j = 0; j < tempUserGroupNodeList.getLength(); j++) {
				Element tempElement = (Element) tempUserGroupNodeList.item(j);

				if (tempElement.getAttribute("UsergroupKey").equals(
						userGroupElement.getAttribute("UsergroupKey"))) {
					isAssignedGroup = true;
				}
			}
			if (!isAssignedGroup) {
				Element eleUserGroupList = YRCXmlUtils.createChild(
						eleUserGroupLists, "UserGroupList");
				eleUserGroupList.setAttribute("UsergroupKey", userGroupElement
						.getAttribute("UsergroupKey"));
				Element eleUserGroup = YRCXmlUtils.createChild(
						eleUserGroupList, "UserGroup");
				eleUserGroup.setAttribute("UsergroupKey", userGroupElement
						.getAttribute("UsergroupKey"));
				eleUserGroup.setAttribute("UsergroupId", userGroupElement
						.getAttribute("UsergroupId"));
				eleUserGroup.setAttribute("UsergroupName", userGroupElement
						.getAttribute("UsergroupName"));
				eleUserGroup.setAttribute("UsergroupDescription", userGroupElement
						.getAttribute("UsergroupDescription"));
			}
		}
		return eleUserGroupLists;
	}
    /*
     * API is Called when a wizard page is about to be shown for the first time.
     *
     */
    public void initPage(String pageBeingShown) {
		if (pageBeingShown
				.equals("com.yantra.pca.ycd.rcp.tasks.enterpriseUserEntry.wizardpages.YCDEnterpriseUserEntryWizardPage")) {
			invokeAPIToGetUserGroups();
		}
	}
    

	public void postSetModel(String model) {
		if("getUserGroupLists_output".equalsIgnoreCase(model)){
			Element eleUserGroupLists = getModel("getUserGroupLists_output");
			if(!YRCPlatformUI.isVoid(eleUserGroupLists)&& getUserGroupListOutXml!= null)
			{
				populateModel();
				isModelSet = true;
			}
		}
		super.postSetModel(model);
	}

	private void invokeAPIToGetUserGroups() {
		YRCApiContext context = new YRCApiContext();
		context.setApiName("getUserGroupList");
		context
				.setFormId("com.yantra.pca.ycd.rcp.tasks.enterpriseUserEntry.wizards.YCDEnterpriseUserEntryWizard");
		context.setInputXml(YRCXmlUtils.createFromString("<UserGroup/>"));
		callApi(context);
	}

	@Override
	public IYRCComposite createPage(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pageBeingDisposed(String arg0) {
		// TODO Auto-generated method stub
		
	}
}
