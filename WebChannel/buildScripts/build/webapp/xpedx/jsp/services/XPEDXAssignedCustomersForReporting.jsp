<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="xpedx" uri="xpedx" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>

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
	<s:url id="assignedCustomersPaginated" action="xpedxSearchAssignedCustomers" namespace="/common">
		<s:param name="orderByAttribute" value="%{orderByAttribute}"/>
		<s:param name="orderByDesc" value="orderByDesc"/>
		<s:param name="pageNumber" value="'{0}'"/>
		<s:param name="searchTerm" value="%{searchTerm}" />
	</s:url>
</s:if>
<s:else>
	<s:url id="assignedCustomersPaginated" action="xpedxGetAssignedCustomersForReporting">
		<s:param name="orderByAttribute" value="%{orderByAttribute}"/>
		<s:param name="orderByDesc" value="orderByDesc"/>
		<s:param name="pageNumber" value="'{0}'"/>
	</s:url>
</s:else>
<style>
.right {
	text-align:right;
}
</style>
<title>Choose Ship-To</title>

</head>
<body>


 <!-- modal window container -->
    <div class="xpedx-light-box" id="change-ship-to">    
    
	<!-- START modal 'header' -->
	<div class="ship-to-header">
		<h2 class="no-border" >Choose Ship-To</h2>
		
</div>
<div class="clearall">&nbsp;</div>
<!-- END modal header -->

    <!-- START static top section with a top border -->
    <!-- hemantha -->
 
    
<!-- END top static section -->
        
        
        
    <!-- START main body (with scroll bar) -->
    <div class="paginationContainer" style="float: right;"><!-- pagination control -->
       <xpedx:pagectl currentPage="%{pageNumber}" lastPage="%{totalNumberOfPages}" urlSpec="%{#assignedCustomersPaginated}" isAjax="true" divId="showShipToLocationsDiv"/>
    </div>
	<div class="ship-to-body">
		<div id="address-list"  style="height: 250px;" class="x-corners ship-to-address-list">

			<form>
				<ul>
				
		<s:set name="shipToAddressList" value="%{assignedShipToList}" />
		<s:iterator value='#shipToAddressList' id='shipToAddress'>
		<s:set name='customerID' value='#shipToAddress.customerID' />
		<s:set name='firstName' value='#shipToAddress.firstName' />
		<s:set name='middleName' value='#shipToAddress.middleName' />
		<s:set name='lastName' value='#shipToAddress.lastName' />
		<s:set name='city' value='#shipToAddress.city' />
		<s:set name='company' value='#shipToAddress.company' />
		<s:set name='addressList' value='#shipToAddress.addressList' />
		<s:set name='hTMLValue' value='#shipToAddress.hTMLValue' />
		<s:set name='state' value='#shipToAddress.state' />
		<s:set name='zipCode' value='#shipToAddress.zipCode' />
		<s:set name='locationID' value="#shipToAddress.LocationID"/>
		<s:set name='organizationName' value="#shipToAddress.organizationName"/>
		<s:set name='country' value="#shipToAddress.country"/>
		
		
		<li class="ship-to-list">
		<table >
			<tr>
				<td>&nbsp;</td>
				<td><s:radio list="#{customerID:customerID}" 
					name="selectedShipTo" id="selectedShipTo" 
					listKey="key" 
					onclick="javascript:updateSelectedAddress('%{#customerID}','%{#organizationName}','%{#company}','%{#addressList}','%{#locationID}','%{#city}','%{#state}','%{#zipCode}','%{#country}' );"
					listValue="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@formatBillToShipToCustomer(key)"
					value="#hTMLValue" theme="simple">
				</s:radio></td>
			<s:hidden id="selectedcustomerid" name='selectedcustomerid' value='%{#customerID}' />
			<s:hidden id="selectedorganization" name='selectedorganization' value='%{#organizationName}' />
		 	 <s:hidden id="selectedcompany" name='selectedcompany' value='%{#company}' />
			 <s:hidden id="selectedaddressList" name='selectedaddressList' value='%{#addressList}' />
			 <s:hidden id="selectedlocationID" name='selectedlocationID' value='%{#locationID}' />
			 <s:hidden id="selectedcity" name='selectedcity' value='%{#city}' />
			 <s:hidden id="selectedstate" name='selectedstate' value='%{#state}' />
			 <s:hidden id="selectedzipCode" name='selectedzipCode' value='%{#zipCode}' />
			 <s:hidden id="selectedcountry" name='selectedcountry' value='%{#country}' />
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
			
			<s:if test="#company!=''">
				<tr>
					<td>&nbsp;</td>
					<td style="padding-left: 15px"><s:property value='#company' /></td>
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
				<td style="padding-left: 15px"><s:property value='#city' />&nbsp;<s:property value='#state' />&nbsp;<s:property value='#zipCode' /></td>
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
 <div class="clearview">&nbsp;</div>
<div class="float-right">
<ul id="tool-bar" class="tool-bar-bottom">
	
	<s:if test="#defaultShipTo!='' || #assgnCustomers.size()==0">
		<li>
			<a class="grey-ui-btn" href="#" style="" onclick="javascript:cancelShipToChanges()"><span>Cancel</span></a>
		</li>
	</s:if>
	<li>
		<a class="green-ui-btn" href="javascript:submitShipToForm();"><span>Apply</span></a>
	</li>
</ul>
	
</div>
</div>

</div>


</body>
</html>