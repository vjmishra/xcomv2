<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />


<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<script type="text/javascript">
			
			function linkedRowToggle(ID){
				var el = document.getElementById(ID);
				switch(el.style.display){
				case 'none':
					el.style.display = 'block';
					break;
				case 'block':
					el.style.display = 'none';
					break;
				}									
			}
			function showAssignedShipTos(billToID){
				var shipToDiv = document.getElementById(billToID);
				switch(shipToDiv.style.display){
				case 'none':
					shipToDiv.style.display = 'block';
					break;
				case 'block':
					shipToDiv.style.display = 'none';
					break;
				}
			}
</script>

<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery-ui.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<s:set name='_action' value='[0]'/>
<s:set name='wcContext' value="wCContext" />
<s:set name='storeFrontID' value='%{#wcContext.getStorefrontId()}' />
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
<s:set name='ctxt' value="getWCContext()" />
<s:set name='shipTo' value ="shipToAddress" />
<s:set name="defaultShipTo" value="defaultShipTo" />
<s:set name='assignmentDoc' value="#_action.getCustomerHierachyDoc().getDocumentElement()" />
<s:set name="custAssignment" value="customerHierachyDoc.getDocumentElement()" />
<s:set name="xutil" value="XMLUtils"/>
								<s:hidden name='LocationsLoaded' id='LocationsLoaded' value='%{isLocationsLoaded()}'/>
								<s:hidden name='billToListText' id='billToListText' value='%{getBillToListText()}'/>
								<s:if test="#_action.getBillToList().size()>0" >
						   		<s:iterator id='BillTo' value='#_action.getBillToList()' status="status">
						   			<s:set name='billToCust' value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getCustomerDetails(#BillTo,#storeFrontID)"/>
						   			<s:set name='billToCustElement' value='#billToCust.getDocumentElement()'/>
						   			<s:set name='billToCustExtn' value='#xutil.getChildElement(#billToCustElement,"Extn")'/>
						   			<ul class="user-profile">
						   			<li>
						   				<a href='javascript:showAssignedShipTos("<s:property value='#BillTo' />")'>
						   					<s:property value='#billToCustExtn.getAttribute("ExtnCustomerName")' /></a>
						   				<div id='<s:property value='#BillTo'/>' style="display: none;">
						   				<ul>
						   				<s:iterator id='shipTos' value="#_action.getAssignedCustomersMap().get(#BillTo)"  status='shipToStatus'>
						   					<s:set name="shipToName" value="#shipTos.getAttribute('CustomerName')" />
						   					<s:if test="#shipToName != ''">
						   					<li>
						   						<a><s:property value="#shipTos.getAttribute('CustomerName')" /></a>
						   					</li>
						   					</s:if>
						   				</s:iterator>
						   				</ul></div>
						   			</li>
						   			</ul>
						   		</s:iterator>
						   		</s:if>
						   		<s:else>
						   		<ul>
						   			<li>
							   			No Customers Assigned for Customer Contact with user id <span><s:property value="loggedInUserId" /></span>. <br/><br/>
							   			Please Contact Administrator for Details.
						   			</li>
						   		</ul>
						   		</s:else>						   				
       	   		  