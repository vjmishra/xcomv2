<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/yfc.tld" prefix="yfc"%>
<s:if
	test="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getLoggedInCustomerFromSession(wCContext)!=null">
	<s:set name='customerId' value="wCContext.customerId" />
	<s:set name='storeFrontId' value="WCContext.storefrontId" />
	<s:set name="defualtShipTAddress"
		value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getShipToAdress(#customerId, #storeFrontId)" />
	<ul class="readOnlyAddress">
					<li>
                      <div>
                        <s:property value='%{#defualtShipTAddress.FirstName}' />
                        <s:property value='%{#defualtShipTAddress.MiddleName}' />
                        <s:property value='%{#defualtShipTAddress.LastName}' />
                      </div>
                    </li> 
                    <li>
                        <s:property value='%{#defualtShipTAddress.Company}' />
                    </li>
                    <s:iterator value='%{#defualtShipTAddress.addressList}' id='adressLine'>
					<li>
						<s:property value='adressLine' />
					</li>
					</s:iterator>
					 <li>
                        <s:property value='%{#defualtShipTAddress.City}' />
                    </li>
					<li>
                        <s:property value='%{#defualtShipTAddress.State}' />&nbsp; <s:property value='%{#defualtShipTAddress.ZipCode}' />
                    </li>
                    <li>
                        <s:property value='%{#defualtShipTAddress.Country}' />
                    </li>
                </ul>
</s:if>
