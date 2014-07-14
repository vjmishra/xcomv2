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
<%--  <s:set name="currentShipTo" value="#wcUtil.getShipToAdress(getWCContext().getCustomerId(),getWCContext().getStorefrontId())" /> --%>
<s:set name="currentShipTo" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("shipToCustomer")'/>
<s:set name='_action' value='[0]' />
<s:url id='targetURL' namespace='/common'
	action='setCurrentCustomerIntoContext' />
<s:url id='searchURL' namespace='/common' action='xpedxSearchAssignedCustomersForOrderList' />
<s:url id='setAsDefaultURL' namespace='/common'
	action='setSelectedShipToAsDefault' />
<%-- 
<s:set name="assgnCustomers"
	value="#wcUtil.getAssignedCustomers(#loggedInUser)" />
	--%>
	<s:set name="assgnCustomers"
	value="#wcUtil.getObjectFromCache('XPEDX_Customer_Contact_Info_Bean')" />
<s:if test="#_action.isSearch()">
	<s:url id="assignedCustomersPaginated" action="xpedxSearchAssignedCustomersForOrderList" namespace="/common">
		<s:param name="orderByAttribute" value="%{orderByAttribute}"/>
		<s:param name="orderByDesc" value="orderByDesc"/>
		<s:param name="pageNumber" value="'{0}'"/>
		<s:param name="searchTerm" value="%{searchTerm}" />
		<s:param name="pageSetToken" value="%{pageSetToken}"/>
	</s:url>
</s:if>
<s:else>
	<s:url id="assignedCustomersPaginated" action="xpedxGetAssignedCustomersForOrderList" namespace="/common">
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
<title><s:property value="wCContext.storefrontId" /> - <s:text name="MSG.SWC.SHIPTO.SELECTSHIPTO.GENERIC.DLGTITLE"/> </title>

</head>
<body>


 <!-- modal window container -->
    <div class="xpedx-light-box" id="select-ship-to">    
    <br />
	<!-- START modal 'header' -->
	<div class="ship-to-header">
		<!-- <h2 class="no-border"  style="float:left;" >Select Ship-To</h2> -->
		<h2 class="no-border"  style="float:left;" > <s:text name="MSG.SWC.SHIPTO.SELECTSHIPTO.GENERIC.DLGTITLE" /></h2>
		
		<!-- <img id="magGlass" class="searchButton" src="../../images/icons/22x22_white_search.png" onclick="javascript:searchShipToAddress();">		 -->
		<span id="magGlass" class="searchButton"  onclick="javascript:searchShipToAddress('shipToOrderSearchDiv');">&nbsp;&nbsp;</span>
		<s:textfield cssClass="input-details x-input"  name='searchTerm' id='Text1'  onclick="javascript:clearText();"  title="searchBox" value="%{searchTerm}" theme="simple" onkeypress="javascript:shipToSearchSubmit(event,'shipToOrderSearchDiv','%{#searchURL}');" />
		<s:hidden id="magGlass" name="searchButton"></s:hidden>
</div>
<div class="clearall">&nbsp;</div>
<div class="clearall">&nbsp;</div>

    <!-- START main body (with scroll bar) -->
   <div class="paginationContainer" style="float: right;"><!-- pagination control -->
       <s:if test="%{totalNumberOfPages>1}">Page</s:if>&nbsp;&nbsp;
       <xpedx:pagectl currentPage="%{pageNumber}" lastPage="%{totalNumberOfPages}" 
       urlSpec="%{#assignedCustomersPaginated}" isAjax="true" divId="shipToOrderSearchDiv" 
       showFirstAndLast="False" showMyUserFormat="true"/>
    </div> 
	
	<div class="ship-to-body">
		<div id="address-list" class="x-corners ship-to-address-list">

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
					onclick="setSelectedValue(this)"
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


<!-- <div class="right">
	<input type='checkbox' name="setAsDefault" id="setAsDefault" class="change-preferred-ship-to" />
	bb2Change Preferred Ship-To to Selected
</div>  -->


<div class="float-right">
<ul id="tool-bar" class="tool-bar-bottom">
		<li>
		   <a class="green-ui-btn"  href="#" onclick = "setVariable('<s:property value="#defaultShipTo"/>' , '<s:property value="#assgnCustomers.getNumberOfAssignedShioTos()"/>');$.fancybox.close();"><span>Select</span></a> 		
	</li>
	<s:if test="#defaultShipTo!='' || (#assgnCustomers != null && #assgnCustomers.getNumberOfAssignedShioTos()==0)">
		<li>
			<a class="grey-ui-btn" href="#" style="" onclick="clearShipToField();$.fancybox.close();"><span>Cancel</span></a>
		</li>
	</s:if>

</ul>
	
</div>
</div>

</div>

<div>



</div>
</body>
</html>