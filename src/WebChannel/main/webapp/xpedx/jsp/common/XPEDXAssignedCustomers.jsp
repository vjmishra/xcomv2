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

<%--
<s:property value="#defaultShipTo"/>
 --%>
<%-- <s:set name="currentShipTo" value="#wcUtil.getShipToAdress(getWCContext().getCustomerId(),getWCContext().getStorefrontId())" />  --%>
<s:set name="currentShipTo" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("shipToCustomer")'/>
<s:set name='_action' value='[0]' />
<s:url id='targetURL' namespace='/common'
	action='setCurrentCustomerIntoContext' />
<s:url id='searchURL' namespace='/common' action='xpedxSearchAssignedCustomers' />
<s:url id='setAsDefaultURL' namespace='/common'
	action='setSelectedShipToAsDefault' />
<%--  <s:set name="assgnCustomers"
	value="#wcUtil.getAssignedCustomers(#loggedInUser)" /> --%>
	 <s:set name="assgnCustomers"
	value="#wcUtil.getObjectFromCache('XPEDX_Customer_Contact_Info_Bean')" />
<s:set name="defaultShipTo" value="#assgnCustomers.getExtnDefaultShipTo()" />	
<s:if test="#_action.isSearch()">
	<s:url id="assignedCustomersPaginated" action="xpedxSearchAssignedCustomers" namespace="/common">
		<s:param name="orderByAttribute" value="%{orderByAttribute}"/>
		<s:param name="orderByDesc" value="orderByDesc"/>
		<s:param name="pageNumber" value="'{0}'"/>
		<s:param name="searchTerm" value="%{searchTerm}" />
		<s:param name="pageSetToken" value="%{pageSetToken}"/>
	</s:url>
</s:if>
<s:else>
	<s:url id="assignedCustomersPaginated" action="xpedxGetAssignedCustomers" namespace="/common">
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
<title><s:property value="wCContext.storefrontId" /> - <s:text name="MSG.SWC.SHIPTO.CHANGESHIPTO.GENERIC.DLGTITLE"/> </title>

</head>
<body>
<%--	Performance Fix - Removal of the mashup call of - XPEDXGetPaginatedCustomerAssignments --%>
<s:if test="%{assignedShipToCount == 0 && comingFromSearch == 'false'}">
<div class="xpedx-light-box" id="change-ship-to">
<div class="ship-to-header"> 
<h2 class="no-border"  style="float:left;" ><s:text name='MSG.SWC.SHIPTO.CHANGESHIPTO.GENERIC.DLGTITLE' /></h2>
</div>
<br /><br /><br /><br />
<div align="center" style="color:#ff0000;font-weight:normal;font-size:12px;"> <s:text name="MSG.SWC.SHIPTO.NOSHIPTO.INFO.CONTACTADMIN" /> </div> <br /><br />
<div align="center" style="color:#ff0000;font-weight:normal;font-size:12px;"> <s:text name="MSG.SWC.SHIPTO.NOSHIPTO.INFO.HELPDESK" /> </div> 
<a class="green-ui-btn" style="position:relative;margin-top:295px;margin-right: 15px;float:right;" href="/swc/home/logout.action?sfId=xpedx&amp;scFlag=Y"><span>Sign Out</span></a>
</s:if>
<s:else>
 <!-- modal window container -->
    <div class="xpedx-light-box" id="change-ship-to">    
    
	<!-- START modal 'header' -->
	<br />
	<div class="ship-to-header">
		<!-- <h2 class="no-border"  style="float:left;" >Change Ship-To</h2> -->
		<h2 class="no-border"  style="float:left;" ><s:text name='MSG.SWC.SHIPTO.CHANGESHIPTO.GENERIC.DLGTITLE' /></h2>
		<!-- <img id="magGlass"  class="searchButton" src="../../images/icons/22x22_white_search.png" onclick="javascript:searchShipToAddress();"/> -->
		<span id="magGlass"  class="searchButton" onclick="javascript:searchShipToAddress();errorValidate();">&nbsp;&nbsp;</span>		
		<s:textfield cssClass="input-details x-input"  name='searchTerm' id='Text1'  onclick="javascript:clearText();"  title="searchBox" value="Search Ship-To…" theme="simple" onkeypress="javascript:shipToSearchSubmit(event);" />	
		<%-- <s:hidden id="magGlass" name="searchButton"></s:hidden> --%>
</div>
<div class="clearall">&nbsp;</div>
<!-- END modal header -->

    <!-- START static top section with a top border -->
    <!-- hemantha -->
    <div class="ship-to-top">
    	<div id="address-list">
    	<s:if test="#defaultShipTo != null">
    		<div class="ship-to-top-left" style="margin-left:0px;">
    		<div style="font-weight:bold;font-size:12px;" class="black">Currently Shopping For:</div>
			<table valign="top" align="left" style="font-weight:normal;font-size:12px;" class="black">
				<tr>
					<td width="33%" valign="top"><s:property
						value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@formatBillToShipToCustomer(#currentShipTo.getCustomerID())" /></td>
				</tr>
				
				<s:if test="%{#currentShipTo.firstName!='' || #currentShipTo.middleName!='' || #currentShipTo.lastName!=''}">
				<tr>
					<td width="100%"><s:if test="%{#currentShipTo.firstName!='' && #currentShipTo.firstName!= null}">
							<s:property	value='%{#currentShipTo.firstName}' />
						</s:if>
						<s:if test="%{#currentShipTo.middleName!='' && #currentShipTo.middleName!= null}">
							<s:property value='%{#currentShipTo.middleName}' /> </s:if>
						<s:if test="%{#currentShipTo.lastName!='' && #currentShipTo.lastName!= null}">
							<s:property	value='%{#currentShipTo.lastName}' />
						</s:if></td>
				</tr>
			</s:if>
				<s:if test="%{#currentShipTo.getOrganizationName()!='' && #currentShipTo.getOrganizationName()!= null}">
				<tr>
					<td width="100%"><s:property value='%{#currentShipTo.getOrganizationName()}' /></td>
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
							<s:property value='%{#currentShipTo.city}' />,&nbsp;
						</s:if>
						<s:if test="%{#currentShipTo.state!=''}">
							<s:property value="%{#currentShipTo.state}" />&nbsp; 
						</s:if>
						<s:if test="%{#currentShipTo.zipCode!=''}">
							<s:property value="%{@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedZipCode(#currentShipTo.zipCode)}" />&nbsp;
						</s:if>	
						<s:if test="%{#currentShipTo.country!=''}"> 
							<s:property value="%{#currentShipTo.country}" />
						</s:if> 					
					</td>
				</tr>
			</table>
    		</div>
    	</s:if>
		<div style="">
		<!-- preferred ship-to start-->
		
		<div style="font-weight:bold;font-size:12px;" class="black">Preferred Ship-To:</div>
<s:if test="#defaultShipTo != null">		
		<table valign="top" align="left" style="font-weight:normal;font-size:12px;" class="black">
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
			<s:if test="%{defualtShipToAssigned.getOrganizationName()!='' && defualtShipToAssigned.getOrganizationName()!= null}">
			<tr>
				<td width="100%"><s:property value='%{defualtShipToAssigned.getOrganizationName()}' /></td>
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
					<s:property value='%{defualtShipToAssigned.city}' />,&nbsp;
				</s:if>
				<s:if test="%{defualtShipToAssigned.state!=''}"> 
					<s:property value="%{defualtShipToAssigned.state}" />&nbsp;
				</s:if> 
				<s:if test="%{defualtShipToAssigned.zipCode!=''}">
					<s:property value="%{@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedZipCode(defualtShipToAssigned.zipCode)}"  />
				</s:if>	
				<s:if test="%{defualtShipToAssigned.country!=''}"> 
					<s:property value="%{defualtShipToAssigned.country}" />
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
		</table>
		</s:if> 
		<!-- preferred ship-to end -->
		</div>		
			<s:if test="%{defualtShipToAssigned.customerID.trim() != ''}">
			<a id="apply-btn1" style="position:absolute;margin-top:-19px;" href="javascript:applyPreferred('<s:property value="#defaultShipTo"/>','<s:property value="%{targetURL}"/>')" class="green-ui-btn">
				<span class="apply-ship-to-btn">Apply Preferred</span></a>
			</s:if>
    	</div>
    </div>
    
<!-- END top static section -->
        
        
        
    <!-- START main body (with scroll bar) -->
    <div class="paginationContainer" style="float: right;margin-right:10px;"><!-- pagination control -->
    <span class="bold">
     	<s:if test="%{totalNumberOfPages>1}">Page</s:if>&nbsp;&nbsp;
		<xpedx:pagectl currentPage="%{pageNumber}" lastPage="%{totalNumberOfPages}" 
		urlSpec="%{#assignedCustomersPaginated}" isAjax="true" divId="ajax-assignedShipToCustomers" 
		showFirstAndLast="False" showMyUserFormat="true"/></span>
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
		<s:set name='country' value='#shipToAddress.country'/>
		<s:set name='locationID' value="#shipToAddress.LocationID"/>
		<s:set name='orgName' value="#shipToAddress.getOrganizationName()"/>
		<li class="ship-to-list">	
		<table style="font-weight:normal;font-size:12px;" class="black">
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

<div id="info" class="float-right">
	<%-- Code added to fix XNGTP-3020 --%>
	<s:if test="#defaultShipTo == null">
	<!-- <h5 align="center"><div class="error">Please select a preferred ship-to and click Apply </div></h5> -->
	<input type='checkbox' name="setAsDefault" id="setAsDefault" class="change-preferred-ship-to" checked/>
	</s:if>
	<s:else> <input type='checkbox' name="setAsDefault" id="setAsDefault" class="change-preferred-ship-to"/></s:else>
	<%-- End fix for XNGTP-3020 --%>
	<!-- bb1Change Preferred Ship-To to Selected -->
	 <s:text name="MSG.SWC.SHIPTO.CHANGESHIPTO.INFO.PREFERREDSHIPTO" />
</div>

<div class="tool-bar-bottom">
<ul id="tool-bar" class="tool-bar-bottom-right" style="margin-right:-171px;float:right">
<s:set name="NoShipTo" value="%{#_action.isShipToResult()}"/>
<s:hidden name="NoShipTo" value="%{#_action.isShipToResult()}"/>
	<li>
<%-- <a class="green-ui-btn" href="javascript:saveShipToChanges('<s:property value="%{targetURL}"/>')" onmousedown="cursor_wait()"><span>Apply</span></a> --%>
			<a class="green-ui-btn" href="javascript:saveShipToChanges('<s:property value="%{targetURL}"/>')"><span>Apply</span></a>

	</li>
	<s:if test="#defaultShipTo!='' || (#assgnCustomers != null && #assgnCustomers.getNumberOfAssignedShioTos()==0)">
		<li>
			<a class="grey-ui-btn" href="#" style="" onclick="javascript:cancelShipToChanges();$.fancybox.close();"><span>Cancel</span></a>
		</li>
	</s:if>
	
</ul>


<div class="change-shipto-error">
<s:if test="%{#NoShipTo}" >
<div id="errorText" class="error float-right">No Ship-To locations were found that meet the search criteria. Please enter new search criteria or click the 'Cancel' button.</div>
</s:if>
<s:elseif test="#defaultShipTo == null">
<div id="errorText" class="error float-right">Please select a Ship-To.</div>
</s:elseif>
<s:elseif test="#defaultShipTo != null">
<div id="errorText" class="notice float-right">Changing the Ship-To could impact pricing on orders.</div>
</s:elseif>
<div id="errorText" class="float-right"></div>
</div>
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