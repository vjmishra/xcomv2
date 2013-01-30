<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="webapp-context" content="/swc" />
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<link media="all" type="text/css" rel="stylesheet"  href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/theme-xpedx_v1.2<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/ORDERS<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="print" />

<s:set name='_action' value='[0]'/>
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXPriceandAvailabilityUtil' id='priceUtil' />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean' id='xpedxutil' />
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean" id="xpedxUtilBean" />
<s:bean name='com.yantra.yfc.util.YFCCommon' id='yfcCommon' />
<s:bean name='com.sterlingcommerce.webchannel.utilities.OrderHelper' id='orderHelper' />
<s:set name='orderShippingAndTotalStartTabIndex' value='980'/>
<s:set name="xutil" value="XmlUtils"/>
<s:set name='wcContext' value="wCContext" />
<s:set name='orderDetail' value='elementOrder'/>
<s:set name="inventoryMap" value="inventoryMap"/>
<s:set name='organizationMap' value='#wcContext.getWCAttribute("organizationKeyNameMap")' />
<s:set name='soldTo' value='#xutil.getChildElement(#orderDetail, "PersonInfoSoldTo")'/>
<s:set name='shipTo' value='#xutil.getChildElement(#orderDetail, "PersonInfoShipTo")'/>
<s:set name='billTo' value='#xutil.getChildElement(#orderDetail, "PersonInfoBillTo")'/>
<s:set name='shipToAddress1' value='#xutil.getAttribute(#shipTo, "AddressLine1")'/>
<s:set name='shipToAddress2' value='#xutil.getAttribute(#shipTo, "AddressLine2")'/>
<s:set name='shipToCity' value='#xutil.getAttribute(#shipTo, "City")'/>
<s:set name='shipToState' value='#xutil.getAttribute(#shipTo, "State")'/>
<s:set name='shipToZipCode' value='#xutil.getAttribute(#shipTo, "ZipCode")'/>
<s:set name='shipToCountry' value='#xutil.getAttribute(#shipTo, "Country")'/>
<s:set name='billToAddress1' value='#xutil.getAttribute(#billTo, "AddressLine1")'/>
<s:set name='billToAddress2' value='#xutil.getAttribute(#billTo, "AddressLine2")'/>
<s:set name='billToCity' value='#xutil.getAttribute(#billTo, "City")'/>
<s:set name='billToState' value='#xutil.getAttribute(#billTo, "State")'/>
<s:set name='billToZipCode' value='#xutil.getAttribute(#billTo, "ZipCode")'/>
<s:set name='billToCountry' value='#xutil.getAttribute(#billTo, "Country")'/>
<s:set name='orderHoldTypes' value='#xutil.getChildElement(#orderDetail, "OrderHoldTypes")'/>
<s:set name='orderHoldType' value='#xutil.getChildElement(#orderHoldTypes, "OrderHoldType")'/>
<s:set name='approverID2' value='#xutil.getAttribute(#orderHoldType, "ResolverUserId")'/>
<s:set name='modifyuserid' value='#xutil.getAttribute(#orderHoldType, "Modifyuserid")'/>
<s:set name='approvedDate' value='#xutil.getAttribute(#orderHoldType, "Modifyts")'/>
<s:set name='xpedxApprovedDate' value="#xpedxutil.formatDate(#approvedDate, #wcContext, null,'MM/dd/yyyy')" />
<s:set name='xpedxReqDeliveryDate' value='#xutil.getAttribute(#orderDetail,"ReqDeliveryDate")'/>
<s:set name='xpedxReqDeliveryDate' value="#util.formatDate(#xpedxOrderDate, #wcContext, null,'MM/dd/yyyy')" />



<s:if test="#_action.isWillCall() == true">
<!-- End - Changes made by Mitesh Parikh for JIRA 3581 -->
	<s:set name='shipFromDoc'
		value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getShipFromAddress()' />
	<s:set name='shipFrom'
		value='#util.getElement(#shipFromDoc, "OrganizationList/Organization/ContactPersonInfo ")' />
</s:if>
	
<s:set name='dorderHeaderKey' value='#xutil.getAttribute(#orderDetail,"OrderHeaderKey")'/>
<s:set name='dorderNo' value='#xutil.getAttribute(#orderDetail,"OrderNo")'/>
<s:set name='orderHoldFlag' value='#xutil.getAttribute(#orderDetail,"ExtnDeliveryHoldFlag")'/>
<s:set name='denterpriseCode' value='#xutil.getAttribute(#orderDetail,"EnterpriseCode")'/>
<s:set name='xpedxHeaderInstructions' value='#xutil.getChildElement(#orderDetail, "Instructions")'/>
<s:set name='xpedxHeaderInstruction' value='#xutil.getLastChildElement(#xpedxHeaderInstructions)'/>
<s:set name='xpedxHeaderInstructionType' value='#xpedxHeaderInstruction.getAttribute("InstructionType")' />
<s:set name='xpedxExtnHeaderComments' value='#xpedxHeaderInstruction.getAttribute("InstructionText")' />
<s:set name='draftOrderFlag' value='%{"N"}'/>
<s:set name='dorderName' value='#xutil.getAttribute(#orderDetail,"OrderName")'/>
<s:set name='status' value='#xutil.getAttribute(#orderDetail,"Status")'/>
<s:set name='showCurrencySymbol' value='true' />
<s:set name='priceInfo' value='#xutil.getChildElement(#orderDetail,"PriceInfo")'/>
<s:set name='currencyCode' value='#priceInfo.getAttribute("Currency")' />
<s:set name="isOrderOnApprovalHold" value="%{#_action.isOrderOnApprovalHold()}"/>
<s:set name="isOrderOnRejectionHold" value="%{#_action.isOrderOnRejctionHold()}"/>
<s:set name="isPendingOrderCheckReq" value='%{true}' /> 
			<s:if test='chainedFOMap.size() > 0'>
                <s:set name="isPendingOrderCheckReq" value='%{false}' />
            </s:if>

<s:set name="isOrderOnCSRReviewHold" value="%{#_action.isOrderOnCSRReviewHold()}"/>
<s:set name="isCSRReview" value="%{#_action.isCSRReview()}"/>
<s:set name="isFOCSRReview" value="%{#_action.isFOCSRReview()}"/>
<s:set name='OrderExtn' value='#xutil.getChildElement(#orderDetail,"Extn")'/>
<s:set name='webConfirmationNumber' value='#OrderExtn.getAttribute("ExtnWebConfNum")' /> 
<s:set name='xpedxLegacyOrderNumber' value='#OrderExtn.getAttribute("ExtnLegacyOrderNo")' />
<s:set name="shipToId" value='#orderDetail.getAttribute("ShipToID")' />
<s:set name='billToAttnTxt' value='#OrderExtn.getAttribute("ExtnAttnName")'/>
<s:set name="xpedxCustomerContactInfoBean" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("XPEDX_Customer_Contact_Info_Bean")' />
<s:set name="createuserkey" value='userKey' />
<s:set name='extnInvoiceNo' value='#OrderExtn.getAttribute("ExtnInvoiceNo")' />
<s:set name='extnInvoiceNo' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getInvoiceNoWithoutDate(#extnInvoiceNo)' />
<s:set name='xpedxAttentionName' value='#OrderExtn.getAttribute("ExtnAttentionName")' />
<s:set name='xpedxRushOrderComments' value='#OrderExtn.getAttribute("ExtnRushOrderComments")' />
<s:set name='xpedxExtnDeliveryHoldDate' value='#OrderExtn.getAttribute("ExtnDeliveryHoldDate")' />
<s:set name='xpedxExtnRushOrderDelDate' value='#OrderExtn.getAttribute("ExtnRushOrderDeliveryDate")' />
<s:set name='xpedxExtnRushOrderFlag' value='#OrderExtn.getAttribute("ExtnRushOrderFlag")' />
<s:set name='xpedxEmail' value='#OrderExtn.getAttribute("ExtnAddnlEmailAddr")' />
<s:set name='xpedxShipToName' value='#OrderExtn.getAttribute("ExtnShipToName")' />
<s:set name='xpedxBillToName' value='#OrderExtn.getAttribute("ExtnBillToName")' />
<s:set name='xpedxOrderedByName' value='#OrderExtn.getAttribute("ExtnOrderedByName")' />
<s:set name='extnTotalOrderValue'	value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#orderExtn.getAttribute("ExtnTotalOrderValue"))'/>
<s:set name='extnWillCall' value='#OrderExtn.getAttribute("ExtnWillCall")' />
<s:set name='extnShipComplete' value='#OrderExtn.getAttribute("ExtnShipComplete")' />
<s:set name='extnWebHoldFlag' value='#OrderExtn.getAttribute("ExtnWebHoldFlag")' />
<s:set name='extnOrderDivision' value='#OrderExtn.getAttribute("ExtnOrderDivision")' />
<s:set name='extnOrderDivisionName' value='%{#organizationMap.get(#extnOrderDivision)}'/>
<s:set name='extnOrderDivisionName11' value='%{#organizationMap.get(#extnOrderDivision)}'/>
<s:set name='instructions' value='#xutil.getChildElement(#orderDetail, "Instructions")'/>
<s:set name='instruction' value='#xutil.getChildElement(#instructions, "Instruction")'/>
<s:set name='instructionType' value='#instruction.getAttribute("InstructionType")' />
<s:set name='instructionText' value='#instruction.getAttribute("InstructionText")' />
<s:set name='xpedxOrderDate' value='#xutil.getAttribute(#orderDetail,"OrderDate")' />
<s:set name='xpedxOrderDate' value="#util.formatDate(#xpedxOrderDate, #wcContext, null,'MM/dd/yyyy')" /> <!-- If order is places from Call Center then format this way -->

<s:set name='xpedxOrderDateTimezone' value=''/>
<s:if test='#xpedxOrderDate.contains("-05")'>
	<s:set name='xpedxOrderDateTimezone' value="%{'CT'}"/>
</s:if>
<s:if test='#xpedxOrderDate.contains("-04")'>
	<s:set name='xpedxOrderDateTimezone' value="%{'ET'}"/>
</s:if>

<s:set name='orderDetails' value='#orderDetail'/>
<s:set name='overallTotals' value='#xutil.getChildElement(#orderDetail, "OverallTotals")'/>
<s:set name='orderDetailExtn' value='#xutil.getChildElement(#orderDetail, "Extn")'/>
<s:set name='shipping' value='#xutil.getChildElement(#orderDetail, "Shipping")'/>
<s:set name='hasPendingChanges' value='#xutil.getAttribute(#orderDetail,"HasPendingChanges")'/>
<s:set name='grandTotal' value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#overallTotals.getAttribute("GrandTotal"))'/>
<s:set name="status" value='#xutil.getAttribute(#orderDetail,"Status")'/>
<s:set name="orderType" value='%{#xutil.getAttribute(#orderDetail, "OrderType")}' />
<s:set name="ShipNode" value='%{#xutil.getAttribute(#orderDetail, "ShipNode")}' />
<s:set name='xpedxOrderDateTime' value='#xutil.getAttribute(#orderDetail,"OrderDate")' />
<s:set name='xpedxOrderTime' value="#xpedxutil.formatDate(#xpedxOrderDateTime, #wcContext,'yyyy-MM-dd\'T\'HH:mm:ss', 'HH:mm:ss')"/>
<s:set name='xpedxReqDeliveryDate1' value='#xutil.getAttribute(#orderDetail,"ReqDeliveryDate")'/>
<s:set name='xpedxReqDeliveryDate' value="#xpedxutil.formatDate(#xpedxReqDeliveryDate1, #wcContext,'yyyy-MM-dd\'T\'HH:mm:ss', 'MM/dd/yyyy')"/>
<s:set name='countItem' value='#_action.getLineItemCount()' />
<s:set name='countOthers' value='#_action.getLineOtherCount()' />

<title><s:property value="wCContext.storefrontId" /> - Print Preview for Order <s:property value='%{webConfirmationNumber}'/></title>
</head>

<body class="  ext-gecko ext-gecko3" >
<div id="main-container">
<div id="main" class=" order-pages">
<s:url id ='homeLink' action='home' namespace='/home' /> 

<%-- <div class="t1-header commonHeader signOnHeader" id="headerContainer" >
	<div class="logo">
		<s:a href="%{homeLink}">&nbsp;</s:a>
	</div> 
</div>  --%>  	
<div class="rounded-border top-section">

<table border="0px solid black" cellpadding="0" cellspacing="0" style="width:100%;background-color:#FFFFFF; padding-top: 5px; padding-left: 5px; padding-right: 5px;" id="print-Preview-section">
	<!-- <tr><td colspan="6" style="border-bottom:1px solid black"></td> </tr>	 -->
	<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr>
	<td colspan="6" style="border-bottom:1px solid black">
	<s:a href="%{homeLink}">&nbsp;<img border="0" alt="" src="../swc/images/logo/xpedx/logo.gif"></s:a>
	</td>
	</tr>
	<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr>
	<s:if test='#orderType == "STOCK_ORDER"'>
		<td class="printField"><strong><span style="float:right;">Order Type:&nbsp;</span></strong></td><td class="printValue"><strong>Fulfillment</strong></td>
	</s:if>
	<s:else>
		<td class="printField"><strong><span style="float:right;">Order Type:&nbsp;</span></strong></td><td class="printValue"><strong><s:property value='#orderType'/></strong></td>
	</s:else>
		
		<td class="printField"><span style="float:right;">Order Create Date:&nbsp;</span></td><td class="printValue"><s:property value='#xpedxOrderDate' /></td>
		<td class="printField"><span style="float:right; width:68%;">Total Order Value:&nbsp;</span></td><td class="printValue"><s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#OrderExtn.getAttribute("ExtnTotalOrderValue"))' /></td>
	</tr>
	<tr>
		<td class="printField"><span style="float:right;">&nbsp;</span></td><td class="printValue"> </td>
		<td class="printField"><span style="float:right;">Order Create Time:&nbsp;</span></td><td class="printValue"><s:property value='%{#xpedxOrderTime}' /></td>
		<td class="printField"><span style="float:right;">Line Count:&nbsp;</span></td><td class="printValue">Item:&nbsp;<s:property value='%{#countItem}' />&nbsp;Other:&nbsp;<s:property value='%{#countOthers}' /></td>
	</tr>
	<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr>
		<td class="printField"><span style="float:right;">Web Confirmation #:&nbsp;</span></td><td class="printValue"><s:property value="%{webConfirmationNumber}"/></td>
		<td class="printField"><span style="float:right;">Ship Date:&nbsp;</span></td><td class="printValue"><s:property value='#xpedxReqDeliveryDate' /> </td>
		<td class="printField"><span style="float:right;">Ordered By:&nbsp;</span></td><td class="printValue"><s:if test='#xpedxOrderedByName!=""' ><s:property value='#xpedxOrderedByName' /></s:if></td>
	</tr>
	<tr>
		<td class="printField"><span style="float:right;">Order #:&nbsp;</span></td>
		<td class="printValue"><s:property value='@com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils@getFormattedOrderNumber(#OrderExtn)'/></td>
		<td class="printField"><span style="float:right;">Ship Complete:&nbsp;</span></td>
		<td class="printValue">
		<s:if test='%{#extnShipComplete == "C"}'>
				<input name="shipComplete" type=checkbox checked="checked" onclick='return false;'>
		</s:if>
		<s:else>
			<input name="shipComplete" type=checkbox onclick='return false;'>
		</s:else></td>
		<td class="printField">&nbsp;</td><td class="printField">&nbsp;</td>
	</tr>
	<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr>
		<td class="printField"><span style="float:right;">Order Status:&nbsp;</span></td>
		<td class="printValue">
		<s:if test='%{#xutil.getAttribute(#orderDetail,"Status") == "Awaiting FO Creation" || (#orderType == "Customer" && #isCSRReview)}'>
                        				Submitted (CSR Reviewing) 
                        			</s:if>
                        			<s:else>
                        			  <s:property value='#xutil.getAttribute(#orderDetail,"Status")'/>
                        			  <s:if test='%{#status != "Cancelled"}'>
                        				<s:if test='%{#isOrderOnApprovalHold && !#isOrderOnRejectionHold && #isPendingOrderCheckReq}'>
                        					(Pending Approval)
                        				</s:if>
                        				<s:elseif test="%{#isOrderOnRejectionHold && #isPendingOrderCheckReq}">
                        					(Rejected)
                        				</s:elseif>  
                        				<s:elseif test='%{(#isOrderOnCSRReviewHold && #isPendingOrderCheckReq) || (#orderType != "Customer"  && #isFOCSRReview) }'>
                        					(CSR Reviewing)
                        				</s:elseif>                        				                        				
                        			  </s:if>
                        			  <s:elseif test='%{(#orderType != "Customer"  && #isFOCSRReview) }'>
                        			  	(CSR Reviewing)
                        			  </s:elseif>
                        			</s:else> 
		
		 </td>
		<td class="printField"><span style="float:right;">Will Call:&nbsp;</span></td>
		<td class="printValue">
			<s:if test='%{#extnWillCall == "Y"}'>
				<input name="extnWillCall"  type=checkbox checked="checked"  onclick='return false;'>
			</s:if>
		<s:else>
			<input name="extnWillCall"  type=checkbox onclick='return false;'>
		</s:else></td>
		<td class="printField"><span style="float:right;">Approved By:&nbsp;</span></td>
		<td class="printValue">
		<s:if test='%{#approverID2 !="" && #approverID2 == #modifyuserid}'>
			<s:set name="approverID" value='#_action.getApproversUserName(#approverID2)' />
			<s:property value='#approverID'/>
		</s:if></td>
	</tr>
	<tr>
		<td class="printField"><span style="float:right;">Order Hold Code:&nbsp;</span></td>
		
		<td class="printValue">
		<s:if test='%{#orderHoldFlag == "Y"}'>
				<input name="OrderHoldCode"  type=checkbox checked="checked" onclick='return false;'>
			</s:if>
		<s:else>
			<input name="OrderHoldCode"  type=checkbox  onclick='return false;'>
		</s:else>		
		</td>
		<td class="printField"><span style="float:right;">Ship From Division:&nbsp;</span></td>
	
		<td class="printValue">
		<s:iterator value="orgsKeyNameMap" id="orgMap" status="status" >
			<s:set name="orgName" value="value" />
			<s:set name="orgKey" value="key" />										
			<s:hidden name="hdn_test" value="%{#orgKey}"  />
			<s:if test='%{#orgKey == #extnOrderDivision}'>
				<s:property value='%{#orgName}'/>
			</s:if>
		</s:iterator>
		</td>
	
		<td class="printField"><span style="float:right;">Approved Date:&nbsp;</span></td>
		<td class="printValue">
		<s:if test='%{#approverID2 !="" && #approverID2 == #modifyuserid}'>
		<s:property value='xpedxApprovedDate'/> 
		</s:if></td>
	</tr>
	<tr>
		<td class="printField"><span style="float:right;">Web Hold Flag:&nbsp;</span></td>
		<td class="printValue">
			<s:if test='%{#extnWebHoldFlag == "Y"}'>
				<input name="webHoldFlag"  type=checkbox checked="checked" onclick='return false;'  >
			</s:if>
		<s:else>
			<input name="webHoldFlag"  type=checkbox onclick='return false;' >
		</s:else></td>
	</tr>
	<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr>
		<td class="printField"><span style="float:right;">Customer PO #:&nbsp;</span></td>
		<td class="printValue"><s:property value='%{#xutil.getAttribute(#orderDetail,"CustomerPONo")}'/></td>
	</tr>
	<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr>
        <td class="printField"><span style="float:right;">Bill To Acct#:&nbsp;</span></td><td class="printValue"><s:property value='orderBillToID'/></td>
		<td class="printField"><span style="float:right;">Ship To Acct#:&nbsp;</span></td><td class="printValue"><s:property value='orderShipToID'/></td>
     </tr>
                        <tr>
							<td class="printField"><span style="float:right;">Bill To Name:&nbsp;</span></td><td class="printField"><s:property value='#xpedxBillToName'/></td>
							<td class="printField"><span style="float:right;">Ship To Name:&nbsp;</span></td><td class="printValue"><s:property value='#xpedxShipToName'/></td>
                        </tr>   
                        <tr>
                        	<td class="printField"><span style="float:right;">Address 1:&nbsp;</span></td>
                        	<td class="printValue" style="width:20%"><s:property value='#billToAddress1'/></td>
                         	<td class="printField"><span style="float:right;">Address 1:&nbsp;</span></td>
                         	<td class="printValue" style="width:20%"><s:property value='#shipToAddress1'/></td>
                         </tr>
                         <tr>
                        	<td class="printField"><span style="float:right;">Address 2:&nbsp;</span></td>
                        	<td class="printValue"><s:property value='#billToAddress2'/></td>
                        	<td class="printField"><span style="float:right;">Address 2:&nbsp;</span></td><td class="printValue"><s:property value='#shipToAddress2'/></td>
                        </tr>
                        <tr>
                        	<td class="printField"><span style="float:right;">City, ST Zip Cntry:&nbsp;</span></td><td class="printValue"><s:property value='#billToCity'/>,&nbsp;<s:property value='#billToState'/>&nbsp; <s:property value='#billToZipCode'/>&nbsp; <s:property value='#billToCountry'/></td>
                        	<td class="printField"><span style="float:right;">City, ST Zip Cntry:&nbsp;</span></td><td class="printValue"><s:property value='#shipToCity'/>,&nbsp;<s:property value='#shipToState'/>&nbsp;<s:property value='#shipToZipCode'/>&nbsp;<s:property value='#shipToCountry'/></td>
                        </tr>
                        <tr>
                        	<td class="printField"></td><td class="printValue"></td>
                        	<td class="printField"><span style="float:right;">Attention:&nbsp;</span></td>
                        	<td class="printValue"><s:property value='%{xpedxAttentionName}'/></td></tr>
						<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
					
	<tr>
		<td valign="top"><span style="float:right;">Header Comment:&nbsp;</span></td>
		<td colspan="5" rowspan="2" valign="top"><s:property value='#xpedxExtnHeaderComments' /></td>
	</tr>
		<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr>
		<td valign="top"><span style="float:right;">Internal Comment:&nbsp;</span></td>
		<td colspan="5" rowspan="2" valign="top">
		<s:if test='%{#instructionType == "INTERNAL"}'>
			<s:property value='#instructionText' />
		</s:if>
		
		</td>
	</tr>
			<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>

	<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr><td colspan="6" style="border-bottom:1px solid black"></td> </tr>
	<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	</table>
	
	<s:form action="orderAgain" namespace="/order" method="post"  id= "mainOrderForm" name='mainOrderForm'>
			<s:hidden name="orderHeaderKey" value="%{dorderHeaderKey}" />
			<s:hidden name="draft" value="%{#draftOrderFlag}"/>
			<s:hidden name="copyFromOrderHeaderKey" value="%{dorderHeaderKey}" />
			<s:hidden name="orderNo" value="%{@com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils@getFormattedOrderNumber(#OrderExtn)}" />
			<s:hidden name="orderName" value="%{dorderName}" />
			
			<s:iterator value="#_action.getMajorLineElements()" id='orderLine' status='orderLineStatIndex'>
				<s:set name='priceInfo' value='#xutil.getChildElement(#orderLine, "LinePriceInfo")'/>
				<s:set name='lineTotal' value='#priceInfo.getAttribute("LineTotal")'/>
				<s:set name='unitPrice' value='#priceInfo.getAttribute("UnitPrice")'/>			
				<s:set name='lineTotals' value='#xutil.getChildElement(#orderLine, "LineOverallTotals")'/>
				<s:set name='item' value='#xutil.getChildElement(#orderLine, "Item")'/>
				<s:set name='itemShortDesc' value='#item.getAttribute("ItemShortDesc")'/>
				<s:set name='itemDetails' value='#xutil.getChildElement(#orderLine, "ItemDetails")'/>
				<s:set name='uomFromItem' value='#itemDetails.getAttribute("UnitOfMeasure")'/>		
				<s:set name='baseUOM'  value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#uomFromItem)'/>
				<s:set name='primaryInfo' value='#xutil.getChildElement(#itemDetails, "PrimaryInformation")'/>
				<s:set name='extendedDesc' value='#primaryInfo.getAttribute("ExtendedDescription")'/>
				<s:set name='itemExtnEle' value='#xutil.getChildElement(#itemDetails, "Extn")' />
				<s:set name='certFlag' value='#itemExtnEle.getAttribute("ExtnCert")' />
				<s:set name='kitLines' value='#xutil.getChildElement(#orderLine, "KitLines")'/>
				<s:set name='lineShipTo' value='#xutil.getChildElement(#orderLine,"PersonInfoShipTo")'/>
				<s:set name='reqShipDate' value='#xutil.getAttribute(#xutil.getElements(#orderLine,"OrderStatuses/OrderStatus").get(0),"ReqShipDate")'/>
				<s:set name='orderDates' value='#xutil.getElementByAttribute(#orderLine,"OrderDates/OrderDate","DateTypeId","YCD_COMPLETELY_SHIPPED_OR_CANCELLED")'/>
				<s:set name='orderLineKey' value='#xutil.getAttribute(#orderLine,"OrderLineKey")'/>
				<s:set name='deliveryMethod' value='#xutil.getAttribute(#orderLine,"DeliveryMethod")'/>
				<s:set name='lineCarrierServiceCode' value='#orderLine.getAttribute("CarrierServiceCode")'/>
				<s:set name='overallStatus' value='#orderLine.getAttribute("OverallStatus")'/>
				<s:set name='lineNotes' value='#util.getElement(#orderLine, "Instructions/Instruction")'/>
				<s:set name='orderLineSpecialInstruction' value='#lineNotes.getAttribute("InstructionText")'/>
				<s:set name='subLineNumber' value='#orderLine.getAttribute("SubLineNo")'/>
				<s:set name='primeLineNumber' value='#orderLine.getAttribute("PrimeLineNo")'/>
				<s:set name='xpedxOrderLineExtn' value='#xutil.getChildElement(#orderLine,"Extn")'/>
				<s:set name='xpedxBckQtyExtn' value='#xutil.getAttribute(#xpedxOrderLineExtn,"ExtnReqBackOrdQty")'/>
				<s:set name='xpedxShippedQtyExtn' value='#xutil.getAttribute(#xpedxOrderLineExtn,"ExtnReqShipOrdQty")'/>
				<s:set name='xpedxJobId' value='#xpedxOrderLineExtn.getAttribute("ExtnCustLineAccNo")'/>
				<s:set name='originalOrderedQty' value='#xutil.getAttribute(#orderLine,"OriginalOrderedQty")'/>
				<s:set name='shipNode' value='#xutil.getAttribute(#orderLine,"ShipNode")'/>
				<s:hidden name="hdn_ship" value="%{#shipNode}"  />

				
				<s:set name="orderLineStatIndexSize" value='#_action.getMajorLineElements().size()'/>
				<s:set name="myPriceValue" value="%{'false'}" />
				<s:if test='#_action.getShortDescriptionForOrderLine(#orderLine) == #item.getAttribute("ItemID")'>
					<s:set name='showDesc' value='#_action.getDescriptionForOrderLine(#orderLine)'/>
					<s:set name='showDescLength' value='#_action.getDescriptionForOrderLine(#orderLine).length()'/>
				</s:if>
				<s:else>
					<s:set name='showDesc' value='#_action.getShortDescriptionForOrderLine(#orderLine)'/>
					<s:set name='showDescLength' value='#_action.getShortDescriptionForOrderLine(#orderLine).length()'/>
				</s:else>
				
				<s:set name='unformatteddesc' value='#_action.getDescriptionForOrderLine(#orderLine)'/>
				
				<s:if test='%{#overallStatus == ""}'>
					<s:set name='overallStatus' value='#orderLine.getAttribute("Status")'/>
				</s:if>
				<input type="hidden" value="<s:property value='#orderLineKey'/>" id="editOrderOrderLineKeyList_<s:property value='#orderLineKey'/>" name="editOrderOrderLineKeyList" /> 
			    <!-- begin middle section -->
			<s:set name="xpedxItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_ITEM_LABEL"/>
			<s:set name="customerItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CUSTOMER_ITEM_LABEL"/>
			<s:set name="manufacturerItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MANUFACTURER_ITEM_LABEL"/>
			<s:set name="mpcItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MPC_ITEM_LABEL"/>
			
			<s:set name='orderLineTranQuantity' value='#xutil.getChildElement(#orderLine, "OrderLineTranQuantity")'/>
            <s:set name='orderLineExtnElem' value='#xutil.getChildElement(#orderLine, "Extn")'/>
            <s:set name="orderType" value='%{#xutil.getAttribute(#orderDetail, "OrderType")}' />
            <s:set name="uom" value='#orderLineTranQuantity.getAttribute("TransactionalUOM")'/>
            <s:set name="extendedPrice" value="#priceUtil.getLineTotal(#orderLineExtnElem.getAttribute('ExtnExtendedPrice'),'1','0')" />
            
            
            <s:set name='overallStatus' value='#orderLine.getAttribute("OverallStatus")'/>
			<s:set name='lineNotes' value='#util.getElement(#orderLine, "Instructions/Instruction")'/>
			<s:set name='orderLineSpecialInstruction' value='#lineNotes.getAttribute("InstructionText")'/>
			<s:set name='adjustment' value='#xutil.getDoubleAttribute(#orderLineExtnElem,"ExtnAdjDollarAmt")' />
			<s:set name='extnPriceOverrideFlag' value='#xutil.getDoubleAttribute(#orderLineExtnElem,"ExtnPriceOverrideFlag")' />
			<s:set name='extnLineCouponDiscount' value='#xutil.getDoubleAttribute(#orderLineExtnElem,"ExtnLineCouponDiscount")' />
			<s:set name='extnLineOrderedTotal' value='#xutil.getDoubleAttribute(#orderLineExtnElem,"ExtnLineOrderedTotal")' />
			<s:set name='extnCustLineAccNo' value='#xutil.getDoubleAttribute(#orderLineExtnElem,"ExtnCustLineAccNo")' />
			<s:set name='xpedxJobId' value='#xpedxOrderLineExtn.getAttribute("ExtnCustLineAccNo")'/>
			
			<s:set name='customerPONo' value='#xutil.getAttribute(#orderLine,"CustomerPONo")'/>
			<s:set name='primaryInformation' value='#xutil.getChildElement(#itemDetails, "PrimaryInformation")'/>
			<s:set name='manufacturerItem' value='#xutil.getAttribute(#primaryInformation,"ManufacturerItem")'/>
			<s:set name='extnWebLineNumber' value='#orderLineExtnElem.getAttribute("ExtnWebLineNumber")' />
			<s:set name='extnLegacyLineNumber' value='#orderLineExtnElem.getAttribute("ExtnLegacyLineNumber")' />
			<s:set name='lineType' value='#xutil.getAttribute(#orderLine,"LineType")'/>			
			
								
	<table border="0px solid black" cellpadding="0" cellspacing="0" style="width:100%;background-color:#FFFFFF; padding-top: 5px; padding-left: 5px; padding-right: 5px;" id="print-Preview-section">
	<tr>
		<td class="printField"><span style="float:right;"> MAX Line Seq #:&nbsp;</span></td><td class="printValue"><s:property value='#extnLegacyLineNumber'/></td>
		<td class="printField"><span style="float:right;"> Legacy Item #:&nbsp;</span></td><td class="printValue"><s:property value='#item.getAttribute("ItemID")'/></td>
		<td class="printField"><span style="float:right;"> Ship From:&nbsp;</span></td><td class="printValue">
		<s:iterator value="orgsKeyNameMap" id="orgMap" status="status" >
			<s:set name="orgName" value="value" />
			<s:set name="orgKey" value="key" />										
			<s:hidden name="hdn_test" value="%{#orgKey}"  />
			<s:hidden name="hdn_kk" value="%{#orgKey}"  />
			<s:if test='%{#orgKey == #shipNode}'>
				<s:property value='%{#orgName}'/>
			</s:if>
			
		</s:iterator>
		</td>
	</tr>
	<tr>
		<td class="printField"><span style="float:right;"> Web Line #:&nbsp;</span></td><td class="printValue"><s:property value='#extnWebLineNumber'/></td>
		<td class="printField"> <span style="float:right;">Cust Item #:&nbsp;</span></td><td class="printValue"><s:property value='#item.getAttribute("CustomerItem")'/></td>
		<td class="printField">&nbsp;</td>
	</tr>
	<tr>
		<td class="printField"> &nbsp;</td><td class="printValue"></td>
		<td class="printField"><span style="float:right;"> Mfg Item #:&nbsp;</span></td><td class="printValue"><s:property value='#manufacturerItem'/></td>
		<td class="printField"> &nbsp; </td><td class="printValue"></td>
	</tr>
	<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr> 

	<tr>
		<td valign="top" rowspan="2"><span style="float:right;">Description:&nbsp;</span></td>
		<s:if test='(#orderLine.getAttribute("LineType") == "C") || (#orderLine.getAttribute("LineType") == "M")'>
			<td rowspan="3" colspan="3" valign="top"><s:property value='#itemShortDesc'/></td>
			
		</s:if>
		<s:else>
			<td rowspan="3" colspan="3" valign="top"><s:property value='#extendedDesc'/></td>
		</s:else>
		
				<td><span style="float:right;">Unit Price:&nbsp;</span></td>
				<td>
				<s:set name="theMyPrice" value='#xpedxutil.formatPriceWithCurrencySymbolWithPrecisionFive(#wcContext,#currencyCode,#orderLineExtnElem.getAttribute("ExtnUnitPrice"))'/>
				 <s:if test='(#orderLine.getAttribute("LineType") != "C") && (#orderLine.getAttribute("LineType") != "M")'> 
				  <s:property value='#theMyPrice'/>&nbsp;per&nbsp;<s:property value='#wcUtil.getUOMDescription(#orderLineExtnElem.getAttribute("ExtnPricingUOM"))'/>
				</s:if>
				</td></tr>
				<tr><td colspan="1"><span style="float:right;">Price Override:&nbsp;</span></td>
				<td>
				
				<s:if test='%{#extnPriceOverrideFlag == "Y"}'>
					<input name="priceOverrideFlag"  type=checkbox checked="checked" onclick='return false;'>
			   </s:if>
			   <s:else>
					<input name="priceOverrideFlag"  type=checkbox  onclick='return false;'>
			</s:else></td></tr>
				
				

	 <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>

	<tr>
		<td class="printField"><span style="float:right;"> Order Qty:&nbsp;</span></td>
		<td class="printValue">
		<s:set name='orderqty' value='#_action.getCalculatedOrderedQuantityWithoutDecimal(#orderLine)' />
		<s:set name='orderdqty' value="#xpedxUtilBean.formatQuantityForCommas(#orderdqty)"/>
		<s:if test='(#orderLine.getAttribute("LineType") != "C") && (#orderLine.getAttribute("LineType") != "M")'>				    			
		<s:property value='#xpedxUtilBean.formatQuantityForCommas(#orderqty)'/>&nbsp;<s:property value='#wcUtil.getUOMDescription(#uom)'/> 
		</s:if></td>
		<td class="printField"><span style="float:right;"> Backorder Qty:&nbsp;</span></td>
		<td class="printValue">
		<s:set name='backqty' value='#orderLineExtnElem.getAttribute("ExtnReqBackOrdQty")' />
		<s:set name='backqty' value='%{#strUtil.replace(#backqty, ".00", "")}' />
		<s:set name='backqty' value="#xpedxUtilBean.formatQuantityForCommas(#backqty)"/>
		<s:property value='%{#backqty}'/>&nbsp;<s:property value='#wcUtil.getUOMDescription(#uom)'/> 
		</td>
		<td class="printField"> <span style="float:right;">Line Total:&nbsp;</span></td>
		<td class="printValue">
		<s:property value='#util.formatPriceWithCurrencySymbol(wCContext,#currencyCode,#extnLineOrderedTotal)'/>		
		</td>
	</tr>	
	

	<tr>
		<td class="printField"><span style="float:right;">ExtendedOrderQty:&nbsp;</span></td>
		<td class="printValue"><s:property value='#originalOrderedQty'/></td>
		<td class="printField">&nbsp;</td><td class="printValue"></td>
		<td class="printField"><span style="float:right;">Adjustment:&nbsp;</span></td>
		<td class="printValue">
		<s:property value='#util.formatPriceWithCurrencySymbol(wCContext,#currencyCode,#adjustment)'/>					
		</td>
	</tr>
	<tr>
		<td class="printField"><span style="float:right;">Base UOM:&nbsp;</span></td><td class="printValue"><s:property value='#baseUOM'/></td>
		<td class="printField">&nbsp;</td><td class="printValue"></td>
		<td class="printField"><span style="float:right;">Extended:&nbsp;</span></td>
		<td class="printValue">
		 	<s:set name="theExtendedPrice" value='#util.formatPriceWithCurrencySymbol(wCContext, #currencyCode, #extendedPrice)'/>
		 	<s:property value='#theExtendedPrice'/>
		</td>
	</tr>
	<tr>
		<td class="printField">&nbsp;</td><td class="printValue"></td>
		<td class="printField">&nbsp;</td><td class="printValue"></td>
		<td class="printField"><span style="float:right;">Coupon Code:&nbsp;</span></td><td class="printValue"></td>
	</tr>
	<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>

	<tr>
		<td valign="top"><span style="float:right;">Notes:&nbsp;</span></td>
		<td rowspan="2" colspan="3" valign="top">
		<s:property value="#orderLineSpecialInstruction"/>
		 </td></tr>
	<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr>
		<td class="printField"><span style="float:right;"> Cust Line Acct #:&nbsp;</span></td>
		<td class="printValue"> 
		<s:property value="#xpedxJobId"/>
		</td>
		<td class="printField"><span style="float:right;"> Cust PO #:&nbsp;</span></td>
		<td class="printValue">
				<s:property value="#customerPONo"/>
		
		</td>
	</tr>
		<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
		<tr><td colspan="6" style="border-bottom:1px solid black"></td> </tr>
		<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	</table>
	<div class="clearall">&nbsp;</div>
	</s:iterator>
</s:form>
<table border="0px solid black" cellpadding="0" cellspacing="0" style="width:100%;background-color:#FFFFFF; padding-top: 5px; padding-left: 5px; padding-right: 5px;" id="print-Preview-section">
<tr>
        <td class="printField">&nbsp;</td><td class="printValue"></td>
        <td class="printField">&nbsp;</td><td class="printValue"></td>
           
	
	<td class="printValue" colspan="2">
              	<ul id="cart-actions" class="float-right">
		            <li><s:a href="javascript:window.print()" cssClass="orange-ui-btn"><span>Print</span></s:a></li>
		        </ul>
        </td>
      </tr>     
</table>			    	    
			    	    
			    	    
			    
</div>
</div>
</div>
</body>
</html>