<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:bean
	name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils"
	id="wcUtil" />
<s:set name='customerDetailsElem' value='customerOrganizationEle' />
<!-- <s:set name="defualtShipTAddress" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getShipToAddressOfCustomer(#customerDetailsElem)" /> -->

<%-- <s:set name="currentShipTo" value="#wcUtil.getShipToAdress(getWCContext().getCustomerId(),getWCContext().getStorefrontId())" /> --%>
<s:set name='currentShipTo' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("shipToCustomer")'/>
<s:set name='_action' value='[0]'/>
<s:set name='defualtShipTAddress'  value="%{#_action.getShipToAddress()}" />
<s:set name="isEditOrderHeaderKey" value ="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute(@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@EDITED_ORDER_HEADER_KEY)}"/>
<s:set name='guestUser' value="#_action.getWCContext().isGuestUser()" />


<!-- Hemantha: added condition for guest user-->
<s:if test='!#guestUser'>
	<!-- Start Shipto header -->
	
	<div class="ship-banner-container">
		<div class="ship_banner">
	     <p>
	       <span class="bold">
	       <s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
	         Shopping for : 
	       </s:if>
	       <s:else>
	         Orders for :
	       </s:else>	        
	       </span>
	       <s:property   value='LoggerInUserCustomerName' />, 
	       <s:if test="%{#currentShipTo.LocationID!='' && #currentShipTo.LocationID!= null}">
					Local ID: <s:property value='%{#currentShipTo.locationID}' />,
				</s:if>
	       
	       <s:iterator value="#defualtShipTAddress.getAddressList()" id='addressline' >
					<s:if test='#addressline.length() > 30'>
							<s:set name='addressline' value='%{#addressline.substring(0,30)}'/>
								<s:property value='addressline'/>,
					</s:if>
					<s:else>
								<s:property value='addressline'/>,
					</s:else>
		</s:iterator>
				
					    <s:if test="{#defualtShipTAddress.getCityCode()!=''}">
							<s:property value="#defualtShipTAddress.getCityCode()"/>,
						</s:if>
						<s:if test="{#defualtShipTAddress.getState()!=''}">
							<s:property value="#defualtShipTAddress.getState()"/>
						</s:if>
						
						<s:if test="{#defualtShipTAddress.getZipCode()!=''}">
						   <s:property value="%{@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedZipCode(#defualtShipTAddress.getZipCode())}"/>
						</s:if>
						<s:if test="{#defualtShipTAddress.getCountry()!=''}">
							<s:property value="#defualtShipTAddress.getCountry()"/>
						</s:if>
		       
	      <s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
	       <a href="#ajax-assignedShipToCustomers" id="shipToSelect1">[Change]</a>
	       </s:if>
	    </p>
	    </div>
    </div>
    

	<!-- End Shipto header -->
</s:if>	