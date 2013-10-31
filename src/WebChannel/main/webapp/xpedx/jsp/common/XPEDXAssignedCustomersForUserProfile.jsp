<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="xpedx" uri="xpedx" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<!-- Web Trends tag start -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/webtrends/displayWebTag<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<meta name="DCSext.w_x_ord_shov_edit" content="1" />
<!-- Web Trends tag end  -->
<s:set name='_action' value='[0]' />
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean'
	id='hUtil' />

<s:bean
	name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils"
	id="wcUtil" />
<s:set name="defaultShipTo" value="defaultShipToCustomerId" />
<%--
<s:property value="#defaultShipTo"/>
 --%>
<s:set name="currentShipTo" value="#wcUtil.getShipToAdress(getWCContext().getCustomerId(),getWCContext().getStorefrontId())" />
<s:set name='_action' value='[0]' />
<s:url id='targetURL' namespace='/common'
	action='setCurrentCustomerIntoContext' />
<s:url id='searchURL' namespace='/common' action='xpedxSearchAssignedCustomers' />
<s:url id='setAsDefaultURL' namespace='/common'
	action='setSelectedShipToAsDefault' />
<s:set name="assgnCustomers"
	value="#wcUtil.getAssignedCustomers(#loggedInUser)" />
<s:if test="#_action.isSearch()">
	<s:url id="assignedCustomersPaginated" action="xpedxSearchAssignedCustomersForUserProfile" namespace="/common">
		<s:param name="orderByAttribute" value="%{orderByAttribute}"/>
		<s:param name="orderByDesc" value="orderByDesc"/>
		<s:param name="pageNumber" value="'{0}'"/>
		<s:param name="searchTerm" value="%{searchTerm}" />
		<s:param name="pageSetToken" value="%{pageSetToken}"/>
	</s:url>
</s:if>
<s:else>
	<s:url id="assignedCustomersPaginated" action="xpedxGetAssignedCustomersForUserProfile">
		<s:param name="orderByAttribute" value="%{orderByAttribute}"/>
		<s:param name="orderByDesc" value="orderByDesc"/>
		<s:param name="pageNumber" value="'{0}'"/>
		<s:param name="pageSetToken" value="%{pageSetToken}"/>
	</s:url>
</s:else>
<style>
.right {
	text-align:right;
}
</style>
<!-- <title>Choose a Ship To Address Modal</title> -->
<title><s:property value="wCContext.storefrontId" /> - <s:text name="MSG.SWC.SHIPTO.SELECT.PREFERREDSHIPTO.GENERIC.DLGTITLE"/> </title>

</head>
<body>
<%--	Performance Fix - Removal of the mashup call of - XPEDXGetPaginatedCustomerAssignments --%>
<s:if test="%{assignedShipToCount == 0 && comingFromSearch == 'false'}">
 <div class="xpedx-light-box" id="select-ship-to">
	<div class="ship-to-header">
	<h2 class="no-border"  style="float:left;" ><s:text name="MSG.SWC.SHIPTO.SELECT.PREFERREDSHIPTO.GENERIC.DLGTITLE"/></h2>
	</div><br /><br /><br /><br />
	<div align="center" style="color:#ff0000;font-weight:normal;font-size:12px;"><s:text name="MSG.SWC.SHIPTO.NOSHIPTO.USER.INFO" /></div><br /><br />
	<div class="float-right" >
		<ul id="tool-bar" class="tool-bar-bottom" >
			<a class="grey-ui-btn" href="#" style="" onclick="javascript:cancelShipToChanges();$.fancybox.close();"><span>Cancel</span></a>
		</ul>	
	</div>
</s:if>
<s:else>
 <!-- modal window container -->
  <div class="xpedx-light-box" id="select-ship-to"> 
    
	<!-- START modal 'header' -->
	<div class="ship-to-header">
		<!-- <h2 class="no-border"  style="float:left;" >Change Ship-To</h2> -->
		<h2 class="no-border"  style="float:left;" ><s:text name="MSG.SWC.SHIPTO.SELECT.PREFERREDSHIPTO.GENERIC.DLGTITLE"/></h2>
		<!-- <img id="magGlass"  class="searchButton" src="../../images/icons/22x22_white_search.png" onclick="javascript:searchShipToAddress();"/> -->
		<span id="magGlass"  class="searchButton" onclick="javascript:searchShipToAddress('shipToUserProfile');">&nbsp;</span>		
	<!-- XBT-343 --><s:textfield cssClass="input-details x-input"  name='searchTerm' id='Text1'  onclick="javascript:clearText();"  title="searchBox" value="SEARCH CRITERIA" theme="simple" onkeypress="javascript:shipToSearchSubmit(event,'shipToUserProfile');" />
			<%-- <s:hidden id="magGlass" name="searchButton"></s:hidden> --%>
</div>
<div class="clearall">&nbsp;</div>
<!-- END modal header -->

    <!-- START static top section with a top border -->
    <!-- hemantha -->
    <!-- Commented for JIRA 2737
    <div class="ship-to-top">
    	<div id="address-list">
    		<div class="ship-to-top-left" style="margin-left:0px;">
    		<div style="font-weight:bold;font-size:12px;" class="black">Currently Shopping For:</div>
			<table valign="top" align="left">
				<tr>
					<td width="33%" valign="top"><s:property
						value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@formatBillToShipToCustomer(#currentShipTo.getCustomerID())" /></td>
				</tr>
				<s:if
					test="%{currentShipTo.firstName!='' || currentShipTo.middleName!='' || currentShipTo.lastName!=''}">
					<tr>
						<td width="100%"><s:if test="%{#currentShipTo.firstName!='' && #currentShipTo.firstName!= null}">
							<s:property value='%{#currentShipTo.firstName}' /> <s:property value=',' />
							</s:if> <s:if test="%{#currentShipTo.middleName!='' && #currentShipTo.middleName!= null}">
							<s:property value='%{#currentShipTo.middleName}' /> <s:property value=',' />
							</s:if> <s:if test="%{#currentShipTo.lastName!='' && #currentShipTo.lastName!= null}">
							<s:property value='%{#currentShipTo.lastName}' />
						</s:if></td>
					</tr>
				</s:if>
				<s:if test="%{#currentShipTo.company!='' && #currentShipTo.company!= null}">
				<tr>
					<td width="100%"><s:property value='%{#currentShipTo.company}' /></td>
				</tr>
				</s:if>
				<s:if test="%{#currentShipTo.LocationID!='' && #currentShipTo.LocationID!= null}">
					<tr>
						<td width="100%">Local ID: <s:property value='%{#currentShipTo.locationID}' /></td>
					</tr>
				</s:if>
				<s:iterator value='%{#currentShipTo.addressList}' id='adressLine'>
					<tr>
						<td width="100%"><s:property value='adressLine' /></td>
					</tr>
				</s:iterator>
				<tr>
					<td width="100%">
						<s:if test="%{#currentShipTo.city!=''}">
							<s:property value='%{#currentShipTo.city}' /><s:property value=',' /> &nbsp;
						</s:if>
						<s:if test="%{#currentShipTo.state!=''}">
							<s:property value="%{#currentShipTo.state}" /><s:property value=',' /> &nbsp; 
						</s:if>
						<s:if test="%{#currentShipTo.zipCode!=''}">
							<s:property value="%{#currentShipTo.zipCode}" />
						</s:if>						
					</td>
				</tr>
			</table>
    		</div>

		<div style="">  -->
		<!-- preferred ship-to start-->
		
	<!-- 	<div style="font-weight:bold;font-size:12px;" class="black">Preferred Ship-To:</div>
		
		<table valign="top" align="left">
			<tr>
				<td width="33%" valign="top">
				<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@formatBillToShipToCustomer(getDefualtShipToAssigned().getCustomerID())"/>
				</td>
			</tr>
	
			<s:if test="%{defualtShipToAssigned.firstName!='' || defualtShipToAssigned.middleName!='' || defualtShipToAssigned.lastName!=''}">
				<tr>
					<td width="100%">
						<s:if test="%{defualtShipToAssigned.firstName!='' && defualtShipToAssigned.firstName!= null}">
							<s:property	value='%{defualtShipToAssigned.firstName}' />
						</s:if>
						<s:if test="%{defualtShipToAssigned.middleName!='' && defualtShipToAssigned.middleName!= null}">
							<s:property	value='%{defualtShipToAssigned.middleName}' />
						</s:if>
						<s:if test="%{defualtShipToAssigned.lastName!='' && defualtShipToAssigned.lastName!= null}">
							<s:property	value='%{defualtShipToAssigned.lastName}' />
						</s:if>
					</td>
				</tr>
			</s:if>
			<s:if test="%{defualtShipToAssigned.company!='' && defualtShipToAssigned.company!= null">
			<tr>
				<td width="100%"><s:property
					value='%{defualtShipToAssigned.company}' /></td>
			</tr>
			</s:if>
			<s:if test="%{defualtShipToAssigned.LocationID!='' && defualtShipToAssigned.LocationID!= null}">
					<tr>
						<td width="100%">Local ID: <s:property value='%{defualtShipToAssigned.LocationID}' /></td>
					</tr>
				</s:if>		
			<s:iterator value='%{defualtShipToAssigned.addressList}'
				id='adressLine'>
				<tr>
					<td width="100%"><s:property value='adressLine' /></td>
				</tr>
			</s:iterator>

			<tr>
				<td width="100%">
				<s:if test="%{defualtShipToAssigned.city!=''}">
					<s:property value='%{defualtShipToAssigned.city}' />&nbsp;
				</s:if>
				<s:if test="%{defualtShipToAssigned.state!=''}"> 
					<s:property value="%{defualtShipToAssigned.state}" /> &nbsp;
				</s:if> 
				<s:if test="%{defualtShipToAssigned.zipCode!=''}">
					<s:property value="%{defualtShipToAssigned.zipCode}" />
				</s:if>	
				</td>
				
			</tr>
			<%-- Removed as Apply Preferred is added and also a checkbox to set the selected as default
			<tr>
				<td><a class="grey-ui-btn"
					href="javascript:saveShipToChanges('<s:property value="%{targetURL}"/>');
						  javascript:setSelectedAddressAsDefault('<s:property value="%{setAsDefaultURL}"/>')">
				<span><u> Set Selected Address as Default</u> </span></a></td>
			</tr>
			 --%>
		</table> -->
		<!-- preferred ship-to end -->
	<!-- 	</div>		
			<s:if test="%{defualtShipToAssigned.customerID.trim() != ''}">
			<a id="apply-btn1"  style="margin-left:0px; margin-top:-19px;" href="javascript:applyPreferred('<s:property value="#defaultShipTo"/>','<s:property value="%{targetURL}"/>')" class="green-ui-btn apply-ship-to-btn">
				<span>Apply Preferred</span></a>
			</s:if>
    	</div> 
    </div> -->
    
<!-- END top static section -->
        
        
        
    <!-- START main body (with scroll bar) -->
    <div class="paginationContainer" style="float: right;"><!-- pagination control -->
     <!--divId Modified For XNGTP-3088  -->
     <s:if test="%{totalNumberOfPages>1}">Page</s:if>&nbsp;&nbsp;
       <xpedx:pagectl currentPage="%{pageNumber}" lastPage="%{totalNumberOfPages}" urlSpec="%{#assignedCustomersPaginated}" 
       isAjax="true" divId="shipToUserProfile" showFirstAndLast="False" showMyUserFormat="true"/>
    </div> 

	
	<div class="ship-to-body">
		<div id="address-list" style="height: 250px;" class="x-corners ship-to-address-list">

			<form>
				<ul>
				
		<s:set name="shipToAddressList" value="%{assignedShipToList}" />
		<s:iterator value='#shipToAddressList' id='shipToAddress'>
		<s:set name='customerID' value='#shipToAddress.customerID' />
		<s:set name='firstName' value='#shipToAddress.firstName' />
		<s:set name='middleName' value='#shipToAddress.middleName' />
		<s:set name='lastName' value='#shipToAddress.lastName' />
		<s:set name='city' value='#shipToAddress.city + ","' />
		<s:set name='company' value='#shipToAddress.company' />
		<s:set name='addressList' value='#shipToAddress.addressList' />
		<s:set name='hTMLValue' value='#shipToAddress.hTMLValue' />
		<s:set name='state' value='#shipToAddress.state' />
		<s:set name='zipCode' value='#shipToAddress.zipCode' />
		<s:set name='zipCode' value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedZipCode(zipCode)"/>
		<s:set name='country' value='#shipToAddress.country' />
		<s:set name='locationID' value="#shipToAddress.LocationID"/>
		<s:set name='orgName' value="#shipToAddress.getOrganizationName()"/>	
		<li class="ship-to-list">	
		<table>
			<tr>
				<td>&nbsp;</td>
				<td><s:radio list="#{customerID:customerID}" 
					name="selectedShipTo" id="selectedShipTo" 
					listKey="key" 
					listValue="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@formatBillToShipToCustomer(key)"
					onclick="updateCurrentCustomer('%{#targetURL}',this)"
					value="#hTMLValue" theme="simple">
				</s:radio></td>

			</tr>
			
			
			<s:if test="#firstName!='' || #middleName!='' || #lastName!=''">
				<tr>
					<td>&nbsp;</td>
					<td style="padding-left: 15px">
						<s:if test="#firstName!=''">
							<s:property value='#firstName' />
						</s:if>
						<s:if test="#middleName!=''">
							<s:property	value='#middleName' />
						</s:if>
						<s:if test="#lastName!=''">
							<s:property value='#lastName' />
						</s:if>
					</td>
				</tr>
			</s:if>
			
			<s:if test="#orgName!=''">
				<tr>
					<td>&nbsp;</td>
					<td style="padding-left: 15px"><s:property value='#orgName' /></td>
				</tr>
			</s:if>
			
			 <s:if test="#locationID!=''">
                 <tr>
                        <td>&nbsp;</td>
                        <td style="padding-left: 15px">Local ID: &nbsp; <s:property value='#locationID' /></td>
                </tr>
              </s:if>
					
			<s:iterator value='#addressList' id='adressLine'>
				<tr>
					<td>&nbsp;</td>
					<td style="padding-left: 15px"><s:property value='adressLine' /></td>
				</tr>
			</s:iterator>			
			<tr>
				<td>&nbsp;</td>
				<td style="padding-left: 15px"><s:property value='#city' />&nbsp;<s:property value='#state' />&nbsp;<s:property value='#zipCode' />&nbsp;<s:property value='#country' /></td>
			</tr>	
		</table>
		</li>
	</s:iterator>
</ul>
</form>

<s:set name="xOverriddenShipToAddress"
	value="#session.ShipToAddressOveridden" />

<div class="clearall"></div>
</div>


<div class="float-right" >
<ul id="tool-bar" class="tool-bar-bottom" >
		<li>
			<%-- eb-1494: when admin sets pref ship-to for user then pass extra request parameter to trigger an update of pref catalog view and pref product category (site preferences tab options) --%>
			<s:if test="adminMode && (#_action.getDefaultShipToCustomerId() == null || #_action.getDefaultShipToCustomerId() == '')">
				<a class="green-ui-btn" href="javascript:saveShipToChanges('<s:property value="%{targetURL}"/>&initPrefs=true')" ><span>Select</span></a>
			</s:if>
			<s:else>
				<a class="green-ui-btn" href="javascript:saveShipToChanges('<s:property value="%{targetURL}"/>')" ><span>Select</span></a>
			</s:else>
	</li>
	<s:if test="#defaultShipTo!='' || #assgnCustomers.size()==0">
		<li>
			<a class="grey-ui-btn" href="#" style="" onclick="javascript:cancelShipToChanges();$.fancybox.close();"><span>Cancel</span></a>
		</li>
	</s:if>
	
</ul>
	
</div>
</div>

</div>

<div>
<form method="post" name="FormToPost" id="FormToPost"><s:hidden
	id="selectedCustomerId" name="selectedCustomerId" value="%{getCurrentCustomer()}" />
<ul>
	<li>
	<div id="EditSelectedAddress" style="display: none;">
	<table>
		<tr style="border: none">
			<td style="border: none">&nbsp;</td>
			<td style="border: none">&nbsp;</td>
		</tr>
		<tr style="border: none">
			<td style="border: none"><s:text name="Name"> Name</s:text></td>
			<td style="border: none"><s:textfield name="xpedxSTName"
				value="%{#xOverriddenShipToAddress.xpedxSTName}" size="20" /></td>
		</tr>
		<tr style="border: none">
			<td style="border: none"><s:text name="Street"> Street</s:text></td>
			<td style="border: none"><s:textfield name="xpedxSTStreet" size="20"
				value="%{#xOverriddenShipToAddress.xpedxSTStreet}" /></td>
		</tr>
		<tr style="border: none">

			<td style="border: none"><s:text name="Address Line2"> Address Line2</s:text></td>
			<td style="border: none"><s:textfield name="xpedxSTAddressLine2"
				value="%{#xOverriddenShipToAddress.xpedxSTAddressLine2}" size="20" /></td>
		</tr>
		<tr style="border: none">

			<td style="border: none"><s:text name="Address Line3"> Address Line3</s:text></td>
			<td style="border: none"><s:textfield name="xpedxSTAddressLine3"
				value="%{#xOverriddenShipToAddress.xpedxSTAddressLine3}" size="20" /></td>
		</tr>

		<tr style="border: none">
			<td style="border: none"><s:text name="City"> City</s:text></td>
			<td style="border: none"><s:textfield name="xpedxSTCity"
				value="%{#xOverriddenShipToAddress.xpedxSTCity}" size="20" /></td>
		</tr>
		<tr style="border: none">
			<td style="border: none"><s:text name="State"> State</s:text></td>
			<td style="border: none"><s:textfield name="xpedxSTState"
				value="%{#xOverriddenShipToAddress.xpedxSTState}" size="20" /></td>
		</tr>
		<tr style="border: none">
			<td style="border: none"><s:text name="zip">zip</s:text></td>
			<td style="border: none"><s:textfield name="xpedxSTZip"
				value="%{#xOverriddenShipToAddress.xpedxSTZip}" size="20" /></td>
		</tr>
	</table>
	</div>
	</li>
</ul>
</form>



</div>
<%--	Performance Fix - Removal of the mashup call of - XPEDXGetPaginatedCustomerAssignments --%>
</s:else>
</body>
</html>