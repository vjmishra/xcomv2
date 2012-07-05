<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils' id='wcUtil' />


<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en"
	xmlns="http://www.w3.org/1999/xhtml" lang="en"> 
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<%request.setAttribute("isMergedCSSJS","true");%>
<s:bean name='org.apache.commons.lang.StringUtils' id='strUtil' />

<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/ORDERS<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link rel="stylesheet" type="text/css"
               href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<![endif]-->
<!-- javascript -->
<!-- sterling 9.0 base  do not edit  javascript move all functions to js/global-xpedx-functions.js -->

<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/swc.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/ajaxValidation.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.core.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.datepicker.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.tabs.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/modals/checkboxtree/jquery.checkboxtree.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/DD_roundies_0.0.2a-min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pseudofocus.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global-xpedx-functions.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.datepicker.js"></script>
<!-- Facy Box (Lightbox/Modal Window -->
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.1.js"></script>-->

<!-- Page Calls -->
<!-- END head-calls.php -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/jquery-1.4.2<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-jquery-headder<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript">
$(function() {
	$(".datepicker").datepicker({
		showOn: 'button',
					numberOfMonths: 0,

		buttonImage: '<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/calendar-icon.png',
		buttonImageOnly: true
	});
});

function xpedxOrderAgain(){
	document.mainOrderForm.action = "orderAgain.action";
	var form = Ext.get("mainOrderForm");
	addCSRFToken(form.dom, 'form');
	form.dom.submit();
}
	
function selectAllCheckBox(){
	if ($('#selectAll').attr('checked')) {
		$("input[@type='checkbox'][@name='ReOrderSelectItem']").each(function(){
				this.checked = true;
		});
	}else{
		$("input[@type='checkbox'][@name='ReOrderSelectItem']").each(function(){
				this.checked = false;
		});
	}
}

function printPOs() {
	var customerPos = document.getElementById('xpedxCustomerPOs').value;
       var customerPosArray = customerPos.split(";");
       //customerPos = "TEST1;TEST2;TEST3;TEST4"
       var customerPosArray = customerPos.split(";");
       for(i = 0; i <customerPosArray.length; i++)
       {
          // if(i==0)
          // {
          // 	document.write("<ul>");
         //  }
       	document.write(customerPosArray[i] + " ");
       //	if(i==(customerPosArray.length-1)) 
       //    {
       	//	document.write("</ul>");	
       //    } 
       }
}

var urlParams = {};
(function () {
    var e,
        a = /\+/g,  // Regex for replacing addition symbol with a space
        r = /([^&=]+)=?([^&]*)/g,
        d = function (s) { return decodeURIComponent(s.replace(a, " ")); },
        q = window.location.search.substring(1);

    while (e = r.exec(q))
       urlParams[d(e[1])] = d(e[2]);
       
})();

$(document).ready(function(){
	$(document).pngFix();
	$('#split-order').click(function(){
		var offset = $(this).offset();
		/* $('#split-order-overlay').css({ top: offset.top+25 }); */         
		$('#split-order-overlay').toggle();
		return false;
	});
	$('#popup-window-close').click(function(){
		$('#split-order-overlay').toggle();
		return false;
	});
	$('#info-icon').click(function(){
			var offset = $(this).offset();
			$('#info-popup').toggle();
			return false;
	});
	$('#popup-window-close2').click(function(){
			$('#info-popup').toggle();
			return false;
	});
		
	

/* 	$('.short-description').each(function(){
        $(this).shorten({noblock: true, width:($(this).width() - 15)});
	});  */
	
	
	$('.short-description').each(function() {
		var html = $(this).html();
		var shortHTML = html.substring(0, 70);
		if( html.length > shortHTML.length )
		{
			$(this).html(shortHTML);
			$(this).append('...');	
			$(this).attr('title', html );
		}
	});
	
	//We are displaying long description by reducing the size of the container, 
	//It is different from other pages. This is to acoomidate spaces and capitals, special chars.
	
	$('.pin-height  ul li').each(function(){
        $(this).shorten({noblock: true, width:($(this).width() - 21)});
	}); 
	
   	theParentConfNum = urlParams["parentOrderKey"];
	theWebConfNum = urlParams["theWebConfNumber"];
	//alert(theParentConfNum + " " + theWebConfNum);
	
	$('td.webConfTD').html("<span class='boldText'>" + "Web Confirmation:" + "</span>" + "<a href='" + "/swc/order/orderDetail.action?sfId=xpedx&orderHeaderKey=" + theParentConfNum + "&scFlag=Y" + "'>" + " " + theWebConfNum +"</a>");
});

function setOrderLineKeys()
{
	
	document.postOrderForm.orderLineKeyList.values=document.mainOrderForm.editOrderOrderLineKeyList.values;
	alert(document.postOrderForm.orderLineKeyList.length);
	var v=document.postOrderForm.orderLineKeyList;
	alert(v[0].value);
}

function showSplitDiv(divId)
{
	var divD=document.getElementById(divId);
	if(divD != null)
	{
		var styleT=divD.style;
		if(styleT.display == "none")
		{
			divD.style.display="block";
		}
		else
		{
			divD.style.display="none";
		}
	}
}
</script>
<script type="text/javascript">
	function openNotePanel(id, actionValue,orderHeaderKey){
		document.forms["approval"].elements["ReasonText"].value = "";
		DialogPanel.show(id);
		svg_classhandlers_decoratePage();
		 /*if(actionValue == "Approve")
		     document.forms["approval"].elements["ApprovalAction"].value = "1300";
		 if(actionValue == "Reject")
		     document.forms["approval"].elements["ApprovalAction"].value = "1200";*/
		 document.forms["approval"].elements["OrderHeaderKey"].value = orderHeaderKey;
		}
		//modified for jira 3484
	    function openNotePanelSetAction(actionValue,orderHeaderKey){
		 if(actionValue == "Accept"){
		     document.forms["approval"].elements["ApprovalAction"].value = "1300";
		     if(document.getElementById("ReasonText1")!=null && document.getElementById("ReasonText1").value==""){
			 		document.getElementById("ReasonText").value="Accepted";
			 	}
			    else{
			    	 document.getElementById("ReasonText").value=document.getElementById("ReasonText1").value;
				}	
		 }
		 if(actionValue == "Reject"){
		     document.forms["approval"].elements["ApprovalAction"].value = "1200";
		     if(document.getElementById("ReasonText1")!=null && document.getElementById("ReasonText1").value==""){
			 		document.getElementById("ReasonText").value="Rejected";
			 	}
			     else{
			    	 document.getElementById("ReasonText").value=document.getElementById("ReasonText1").value;
				}	
		 }
		 document.forms["approval"].elements["OrderHeaderKey"].value = orderHeaderKey;
			//submit it
		 document.forms["approval"].submit();	
		}
	</script>
<s:set name="isMyPriceZero" value="%{'false'}" />
<s:set name='_action' value='[0]'/>

<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXPriceandAvailabilityUtil'
	id='priceUtil' />

<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean' id='xpedxutil' />
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean" id="xpedxUtilBean" />
<s:bean name='com.yantra.yfc.util.YFCCommon' id='yfcCommon' />
<s:bean name='com.sterlingcommerce.webchannel.utilities.OrderHelper' id='orderHelper' />
<s:set name='orderShippingAndTotalStartTabIndex' value='980'/>

<s:set name='orderDetail' value='elementOrder'/>
<s:set name="inventoryMap" value="inventoryMap"/>
<s:set name="xutil" value="XmlUtils"/>
<s:set name='wcContext' value="wCContext" />
<%-- 
	I don't see this variable getting used in this jsp so commenting
<s:set name='chargeDescriptionMap' value='#util.getChargeDescriptionMap(#wcContext)'/>
--%>
<s:set name='soldTo' value='#xutil.getChildElement(#orderDetail, "PersonInfoSoldTo")'/>
<s:set name='shipTo' value='#xutil.getChildElement(#orderDetail, "PersonInfoShipTo")'/>
<s:set name='billTo' value='#xutil.getChildElement(#orderDetail, "PersonInfoBillTo")'/>
<!-- Begin - Changes made by Mitesh Parikh for JIRA 3581 -->
<s:if test="#_action.isWillCall() == true">
<!-- End - Changes made by Mitesh Parikh for JIRA 3581 -->
	<s:set name='shipFromDoc'
		value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getShipFromAddress()' />
	<s:set name='shipFrom'
		value='#util.getElement(#shipFromDoc, "OrganizationList/Organization/ContactPersonInfo ")' />
</s:if>
	
<s:set name='dorderHeaderKey' value='#xutil.getAttribute(#orderDetail,"OrderHeaderKey")'/>
<s:set name='dorderNo' value='#xutil.getAttribute(#orderDetail,"OrderNo")'/>
<s:set name='denterpriseCode' value='#xutil.getAttribute(#orderDetail,"EnterpriseCode")'/>
<s:set name='xpedxReqDeliveryDate' value='#xutil.getAttribute(#orderDetail,"ReqDeliveryDate")'/>
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

<s:set name='OrderExtn' value='#xutil.getChildElement(#orderDetail,"Extn")'/>
<s:set name='webConfirmationNumber' value='#OrderExtn.getAttribute("ExtnWebConfNum")' />
<s:set name='xpedxLegacyOrderNumber' value='#OrderExtn.getAttribute("ExtnLegacyOrderNo")' />
<s:set name="shipToId" value='#orderDetail.getAttribute("ShipToID")' />
<s:set name='billToAttnTxt' value='#OrderExtn.getAttribute("ExtnAttnName")'/>
<%--
<s:set name='sapCustId'
	value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getSAPCustomerId(#shipToId, #wcContext)' />
<s:set name='custDoc' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getCustomerExtensions(#sapCustId, #wcContext.getStorefrontId())' />
<s:set name='extnElement' value='#util.getElement(#custDoc, "/Customer/Extn")'/>
<s:set name='billToAttnTxt' value='#extnElement.getAttribute("ExtnAttnName")'/>
 --%>
 <s:set name="xpedxCustomerContactInfoBean" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("XPEDX_Customer_Contact_Info_Bean")' />
<s:set name="createuserkey" value='userKey' />
<s:set name='extnInvoicedDate' value='#OrderExtn.getAttribute("ExtnInvoicedDate")' />
<s:set name='extnInvoiceNo' value='#OrderExtn.getAttribute("ExtnInvoiceNo")' />
<s:set name='extnInvoiceNo' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getInvoiceNoWithoutDate(#extnInvoiceNo)' />
<s:set name='extnInvoicedDate' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUnformattedDate("yyyy-MM-dd", #extnInvoicedDate)' />
<s:set name='xpedxAttentionName' value='#OrderExtn.getAttribute("ExtnAttentionName")' />
<s:set name='xpedxRushOrderComments' value='#OrderExtn.getAttribute("ExtnRushOrderComments")' />
<s:set name='xpedxExtnDeliveryHoldDate' value='#OrderExtn.getAttribute("ExtnDeliveryHoldDate")' />
<s:set name='xpedxExtnRushOrderDelDate' value='#OrderExtn.getAttribute("ExtnRushOrderDeliveryDate")' />
<s:set name='xpedxExtnRushOrderFlag' value='#OrderExtn.getAttribute("ExtnRushOrderFlag")' />
<s:set name='xpedxEmail' value='#OrderExtn.getAttribute("ExtnAddnlEmailAddr")' />
<s:set name='xpedxShipToName' value='#OrderExtn.getAttribute("ExtnShipToName")' />
<s:set name='xpedxBillToName' value='#OrderExtn.getAttribute("ExtnBillToName")' />

<s:set name='xpedxOrderedByName' value='#OrderExtn.getAttribute("ExtnOrderedByName")' />

<s:set name='extnOrderSourceType' value='#xutil.getAttribute(#OrderExtn,"ExtnSourceType")' />
<s:set name='xpedxOrderSource' value='' />
<s:if test='%{#extnOrderSourceType == "1"}'> 
	<s:set name='xpedxOrderSource' value="%{'B2B'}" />
</s:if>
<s:elseif test='%{#extnOrderSourceType == "3"}'>
	<s:set name='xpedxOrderSource' value="%{'Web'}" />
</s:elseif>
<s:elseif test='%{#extnOrderSourceType == "4"}'>
	<s:set name='xpedxOrderSource' value="%{'Call Center'}" />
</s:elseif>

<s:set name='xpedxOrderDate' value='#xutil.getAttribute(#orderDetail,"OrderDate")' />

<%-- jira3431 - showing timestamp and timezone on order detail and web confirmation details page for Web orders - begin --%>
<s:set name='xpedxOrderDateTimezone' value=''/>
<s:if test='#xpedxOrderDate.contains("-05")'>
	<s:set name='xpedxOrderDateTimezone' value="%{'CT'}"/>
</s:if>
<s:if test='#xpedxOrderDate.contains("-04")'>
	<s:set name='xpedxOrderDateTimezone' value="%{'ET'}"/>
</s:if>
<s:if test='%{#xpedxOrderSource == "Web"}'>
	<s:set name='xpedxOrderDate' value="#xpedxutil.formatDate(#xpedxOrderDate, #wcContext,'yyyy-MM-dd\'T\'HH:mm:ss', 'yyyy-MM-dd HH:mm:ss')" />
</s:if>
<s:else>
	<s:set name='xpedxOrderDate' value="#util.formatDate(#xpedxOrderDate, #wcContext, null,'MM/dd/yyyy')" />
</s:else>
<%-- jira3431 - showing timestamp and timezone on order detail and web confirmation details page for Web orders - end --%>

<s:set name='optimizationType' value='#xutil.getAttribute(#orderDetail,"OptimizationType")'/>
<s:set name='locale' value="locale" />
<s:set name='orderDetails' value='#orderDetail'/>
<s:set name="emailDialogTitle" scope="page" value="#_action.getText('Email_Title')"/>
<s:set name="ApprovalHistoryTitle" scope="page" value="#_action.getText('Approval_History_Title')"/>
<s:set name="HoldsPopupTitle" scope="page" value="#_action.getText('Holds_Popup_Title')"/>
<s:set name="ApprovalNotesEntryTitle" scope="page" value="#_action.getText('Approval_Notes_Entry_Title')"/>
<%-- variable declartion for order shipping and total block. --%>
<s:set name='overallTotals' value='#xutil.getChildElement(#orderDetail, "OverallTotals")'/>
<s:set name='orderDetailExtn' value='#xutil.getChildElement(#orderDetail, "Extn")'/>
<s:set name='shipping' value='#xutil.getChildElement(#orderDetail, "Shipping")'/>
<s:set name='hasPendingChanges' value='#xutil.getAttribute(#orderDetail,"HasPendingChanges")'/>
<%-- We already have uom map in session so commenting this map
<s:set name="uomMap" value='#wcUtil.getUOMDescription(true)' />
--%>
<s:set name="holdTypeMap" value='%{#_action.getHoldTypeDescription()}' scope="session" />
<%-- I don't see this variable getting called in this JSP so commenting it
<s:set name="shipmentTrackingUserExitAvailable" value='%{#_action.isUserExitImplAvailableForgetTrackingNumberURL()}' />
 --%>
<s:url id="orderDetailsURL" action="orderDetail">
    <s:param name="orderHeaderKey" value='#orderDetail.getAttribute("OrderHeaderKey")'/>
</s:url>
<s:url id="orderDetailsWebConfURL" action="orderDetail">
    <s:param name="orderHeaderKey" value='#orderDetail.getAttribute("OrderHeaderKey")'/>
    <s:param name="returnUrlForOrderList" value="%{orderListReturnUrl}"/>
</s:url>
<s:url includeParams="none" id="orderNotesListURL" action="orderNotesList.action">
	<s:param name="OrderHeaderKey" value='#orderDetail.getAttribute("OrderHeaderKey")'/>
	<s:param name="draft" value="#draftOrderFlag"/>
	<s:param name="returnUrlForOrderList" value="%{orderListReturnUrl}"/>        
        
</s:url>

<s:url  id = "urlOrderAgain" namespace="/order" includeParams="none" escapeAmp="false"   action='orderAgain123.action'>
     <s:param name="copyFromOrderHeaderKey" value="%{dorderHeaderKey}" />
	<s:param name="orderNo" value="%{dorderNo}" />
	<s:param name="orderName" value="%{dorderName}" />
</s:url>
<s:url  id = "urlEmail" includeParams="none" escapeAmp="false" action='emailOrder' namespace = '/order'>
	<s:param name="messageType" value='%{"ComposeMail"}'/>
	<s:param name="orderHeaderKey" value='%{#orderDetail.getAttribute("OrderHeaderKey")}'/>
    <s:param name="draft" value="#draftOrderFlag"/>
</s:url>
<s:url id='returnItemsLink' namespace='/xpedx/services' action='returnItemsRequest'>
	<s:param name="orderHeaderKey" value='%{#orderDetail.getAttribute("OrderHeaderKey")}'/>
 </s:url>
<s:url id="urlPrint"  includeParams="none" escapeAmp="false" action='PrintOrderDetail.action' namespace = '/order' ></s:url>
<s:url id="urlOrderAgainId"  includeParams="none"  action='orderAgain' namespace = '/order' ></s:url>
<s:url id="urlEditOrderId"    action='draftOrderDetails' namespace = '/order' >
	<s:param name='orderHeaderKey' value='#xutil.getAttribute(#orderDetail,"OrderHeaderKey")'/>
	<s:param name="isEditOrder" value="%{'true'}" ></s:param>
	<s:param name="draft" value='%{"N"}'></s:param>
	<s:param name="resetDesc" value="%{'true'}" ></s:param>
</s:url>
<s:url id="urlResetOrderId"  includeParams="none"  action='XPEDXResetPendingOrder' namespace = '/order' ></s:url>
<s:set name="isOrderOnApprovalHold" value="%{#_action.isOrderOnApprovalHold()}"/>
<s:set name="isOrderOnRejectionHold" value="%{#_action.isOrderOnRejctionHold()}"/>

<s:set name="isOrderOnCSRReviewHold" value="%{#_action.isOrderOnCSRReviewHold()}"/>
<s:set name="isCSRReview" value="%{#_action.isCSRReview()}"/>	
<s:set name='grandTotal' value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#overallTotals.getAttribute("GrandTotal"))'/>
<s:set name="status" value='#xutil.getAttribute(#orderDetail,"Status")'/>
<s:set name="orderType" value='%{#xutil.getAttribute(#orderDetail, "OrderType")}' />
<s:set name="isSalesRep" value ="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute('IS_SALES_REP')}"/>
<s:set name="ViewInvoicesFlag" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getInvoiceFlagForLoggedInUser(wCContext)" />

<title><s:property value="wCContext.storefrontId" /> - <s:text name="orderdetails.title" /> <s:property value='%{webConfirmationNumber}'/></title>
<!-- Webtrend tag start -->
<meta name="WT.ti" content='<s:text name="orderdetails.title" /> <s:property value="%{webConfirmationNumber}"/>' />
<!-- Webtrend tag end -->
</head>
<!-- END swc:head -->

<body class="  ext-gecko ext-gecko3" >
<div id="main-container">
    <div id="main" class=" order-pages">
        <s:action name="xpedxHeader" executeResult="true" namespace="/common" />
        <div class="container orders-page">
        
        <!-- breadcrumb -->
                <div id="breadcumbs-list-name" >
                	<span class="page-title">
                	<s:if test='#orderType != "Customer" ' >
                		<!-- Order Detail  -->
                		 <s:text name='MSG.SWC.ORDR.ORDRDETAIL.GENERIC.PGTITLE' />
                	</s:if>
                	<s:else>
                		<!-- Web Confirmation Detail -->
                	   <s:text name='MSG.SWC.ORDR.WEBCONFDETAIL.GENERIC.PGTITLE' />
                	</s:else>
                	</span>
                	<a href="javascript:window.print()"><span style="margin-top: 5px" class="print-ico-xpedx orders underlink"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/print-icon<s:property value='#wcUtil.xpedxBuildKey' />.gif" width="16" height="15" alt="Print Page" />Print Page</span></a>
					<s:if test = "#_action.isOrderInPendingChageState()" >
                	   <div style="margin-right:5px; font-weight: normal;float:right;" class="notice">Click 'Refresh' to cancel changes in progress and return to the original order</div>
                	  
              	  	</s:if>
                </div>
                <!-- end breadcrumb -->
                <h5 align="center"><b><font color="red"><s:property value="#_action.getErrorMsg()"/></font></b></h5>
                <!-- begin top section -->
                <div id="wc-btn-bar" style="width:98.3%;padding-top:5px">
                <s:form namespace="/order" method="post"  id= "postOrderForm" name='postOrderForm'>
		            <s:hidden name="orderHeaderKey" value="%{dorderHeaderKey}" />
		            <s:hidden name="draft" value="%{#draftOrderFlag}"/>
		            <s:hidden name="copyFromOrderHeaderKey" value="%{dorderHeaderKey}" />
		            <s:hidden name="orderNo" value="%{dorderNo}" />
		            <s:hidden name="orderName" value="%{dorderName}" />
		            <%--Adding extnLegOrderType for Jira 3544 --%>
		            <s:set name="extnLegOrderType" value='#OrderExtn.getAttribute("ExtnLegacyOrderType")' />
		            <s:iterator value="#_action.getMajorLineElements()" id='orderLine' status='orderLineStatIndex'>
			            <s:set name='orderLineKey' value='#xutil.getAttribute(#orderLine,"OrderLineKey")'/>
			            <s:hidden name="orderLineKeyList" value='%{#orderLineKey}' id='orderLineKeyList_<s:property value="#orderLineKey"/>'/>
		            </s:iterator>
		            <s:a id='returnOrderListId' href='%{orderListReturnUrl}' cssClass="grey-ui-btn float-left top-gray-btn"><span>Return to Orders</span></s:a>
	                
	                <s:if test ="#_action.approvalAllowed()" >
		                <s:a key="accept" href="javascript:openNotePanel('approvalNotesPanel', 'Approve','%{dorderHeaderKey}'); " cssClass="grey-ui-btn" cssStyle="float:right"><span>Approve /Reject Order</span></s:a>
<%-- 						<s:a key="reject" href="javascript:openNotePanel('approvalNotesPanel', 'Reject','%{dorderHeaderKey}'); " cssClass="grey-ui-btn" cssStyle="float:right"><span>Reject Order</span></s:a> --%>
		            </s:if>
		              
	            <s:set name="isEstimator" value='%{#xpedxCustomerContactInfoBean.isEstimator()}' />
	            <%--	Using CustomerContactBean object from session
	            <s:set name="isEstimator" value="%{#wcContext.getWCAttribute('isEstimator')}" />
	            --%>
	            <s:if test="!#isEstimator">
	               <s:if test='%{#status != "Cancelled"}'>
					<s:if test="#_action.canOrderAgain() && #orderType == 'Customer' ">
						<%--Added the below condition for extnLegOrderType for Jira 3544 --%>
						<s:if test='(#extnLegOrderType != "Q" && #extnLegOrderType != "F" && #extnLegOrderType != "R" && #extnLegOrderType != "I")'>
						<a href="javascript:xpedxOrderAgain();" style="float:right" class="grey-ui-btn orders"><span>Re-Order</span></a>
						</s:if>
					</s:if>
					</s:if>
				</s:if>
				<s:set name="test11" value="%{#_action.isFOCreated()}"/>	
				
				<s:hidden name="test1" value='%{#test11}'/>
				<s:if test="!#isEstimator">
					<s:if test='#_action.isCustomerOrder(#orderDetail)'>					
							<s:if test="#_action.isEditableOrder() && ! #_action.isFOCreated() && ! #_action.isCSRReview()">
								<a href="javascript:editOrder('${urlEditOrderId}');" style="float:right" class="grey-ui-btn"><span>Edit Order</span></a>
							</s:if>
					</s:if>				
					<s:else>					
						<s:if test="#_action.isEditableOrder()">
							<a href="javascript:editOrder('${urlEditOrderId}');"  style="float:right" class="grey-ui-btn"><span>Edit Order</span></a>
						</s:if>					
					</s:else>
				</s:if>
				 <s:if test = "#_action.isOrderInPendingChageState()" >
					 <a href="javascript:refreshOrder('${urlResetOrderId}');" style="float:right" class="grey-ui-btn" id='refreshButtonId'><span>Refresh</span></a>
               		<%--OOTB button for refreshing order -Amar 
               		 	<s:submit type="button"   key='ORDER_REFRESH' id='refreshButtonId'  value='ORDER_REFRESH' onclick="javascript:refreshOrder('%{urlResetOrderId}');return false" cssClass='submitBtnBg1' tabindex="96"/>
              	  	--%>
              	  </s:if>
					<br/> <br/> <br/>
                 </s:form>
                 </div>
                <div class="rounded-border top-section">
                	<!-- begin content w/border -->
			<fieldset class="x-corners mil-col-mil-div">
			<!-- text on border -->
			<%-- Added Condition - isPendingOrderCheckReq For Jira 4109 --%>
                <s:set name="isPendingOrderCheckReq" value='%{true}' />        
                        <s:if test='#orderType != "Customer" ' > 
                        <!-- <div id="OD-on-line" style="width:120px"> -->
                        	<s:if test="#xpedxLegacyOrderNumber != ''">
                     			<legend>Order #: <s:property value='@com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils@getFormattedOrderNumber(#OrderExtn)'/></legend>
                        	<!-- </div> -->
                       		</s:if>
                       		<s:else>
                       			<legend> In Progress</legend>
                       			<s:set name="isPendingOrderCheckReq" value='%{false}' />
                       		</s:else>
                       		
                        </s:if>
                        <s:else>
                        <!-- <div id="OD-on-line" style="width:220px"> -->
                        	<legend>Web Confirmation: <s:property value='%{webConfirmationNumber}'/></legend>
                        	<!-- </div> -->
                        </s:else>
                        <!--[if IE]>
							<br/>
						<![endif]-->
                        <!-- begin content-holding table -->
                        <table border="0px solid red" cellpadding="0" cellspacing="0" class="width-56 no-border float-left" id="top-section">
                        <tr>
                        <s:if test='#orderType != "Customer" ' >
							<td id="theWebConfTD">
							    <span class="boldText">Web Confirmation:</span>
								<span  class="underlink">
									<s:url id="orderDetailsURL" action="orderDetail">
									    <s:param name="orderHeaderKey" value="parentOHK"/>
									    <s:param name="orderListReturnUrl" value="%{orderListReturnUrl}"/>
									</s:url>
									<s:a href="%{orderDetailsURL}">
									  <s:property value='#webConfirmationNumber'/>
									</s:a>
								</span>
							</td>
						</s:if>
						<s:else>
							<td>
							   <span class="boldText">Order #: </span> 
							   		<s:if test='chainedFOMap.size() > 0'>
							   			<s:if test='chainedFOMap.size() > 1'>
							   				<a id="split-order" class="underlink" href="#">Split Orders</a> 
							        		<img class="replacement-img" title="Your order required multiple orders to fulfill." src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_charcoal_help.png">
	              						</s:if>
                        				<s:else>
	              							<s:iterator value="chainedFOMap" >
												<s:set name='chainedFOKey' value='key'/>
												<s:set name='chainedFONo' value="value"/>
												<s:url id="orderDetailsURL" action="orderDetail">
												    <s:param name="orderHeaderKey" value="#chainedFOKey"/>
												    <s:param name="orderListReturnUrl" value="%{orderListReturnUrl}"/>
												</s:url>
												<s:if test='%{#chainedFONo == "In progress"}'>
													<s:property value='#chainedFONo'/>
												</s:if>
												<s:else>
													<s:a href="%{orderDetailsURL}">
												  		<s:property value='#chainedFONo'/>
													</s:a>
												</s:else>															
											</s:iterator> 
                        				</s:else> 
                        			<s:else>          		
									   		<a class="underlink" href="#"> <s:property value='@com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils@getFormattedOrderNumber(#OrderExtn)'/></a>
									      	<s:set name="splitOrderCount" value="chainedOrderCountMap.get(#orderLineKey)"/>
							      	</s:else> 
                                  	</s:if>
                                  	<s:else>
                                  		In Progress
                                  		<s:set name="isPendingOrderCheckReq" value='%{false}' /> 
                                  	</s:else> 
                                  	
                                  	
							</td>
						</s:else>
						<td><span class="boldText">PO #:</span>
							<s:hidden name='xpedxCustomerPOs' id='xpedxCustomerPOs' value='%{#xutil.getAttribute(#orderDetail,"CustomerPONo")}'/>
								<script type="text/javascript">printPOs();</script></td>
                        </tr>
                        <tr>
							<td>&nbsp;</td>
						</tr>
                        <tr>
                        <td><span class="boldText">Bill-To: </span><s:property value='orderBillToID'/></td>
				        <td><span class="boldText">Ship-To: </span><s:property value='orderShipToID'/></td>
                        </tr>
                        <tr>
						<td><s:property value='#xpedxBillToName'/></td>
						<td><s:property value='#xpedxShipToName'/></td>
                        </tr>   
                        <tr>
						<td valign="top"><s:set name='renderPersonInfo' value='#billTo'/>
                            <s:set name='contentOnly' value='"true"'/>
                            <s:if test='(#billToAttnTxt != "")'>
                            	Attention:&nbsp;<s:property value='%{billToAttnTxt}'/> <br/>
							</s:if>
                            <s:include value="/xpedx/jsp/order/XpedxShipToAddress.jsp" />
                            </td>
						<td valign="top"><s:set name='renderPersonInfo' value='#shipTo'/>
                            <s:set name='contentOnly' value='"true"'/>
                            <s:if test='(#OrderExtn.getAttribute("ExtnAttentionName") != "")'>
                            	Attention:&nbsp;<s:property value='%{xpedxAttentionName}'/> <br/>
							</s:if>
                            <s:include value="/xpedx/jsp/order/XpedxShipToAddress.jsp" />
                            </td>
                        </tr>
                        </table>
                        <table class="width-44 float-right" id="OD-top-section-right" >
                        		<tr>
                        			<td colspan="2"><span class="boldText">Order Status: </span> 
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
                        				<s:elseif test="%{#isOrderOnCSRReviewHold && && #isPendingOrderCheckReq}">
                        					(CSR Reviewing)
                        				</s:elseif>                        				                        				
                        			  </s:if>
                        			</s:else>                        				
					 					<s:if test="%{#ViewInvoicesFlag}">                        				
		                        			<s:if test='%{#status == "Invoiced"}'>	                        				 
		                        				<s:if test='#orderType != "Customer" ' >	                        						                        			
			                        				 : Invoice #: <a class="underlink" target="_blank" href="<s:property value='%{invoiceURL}'/>UserID=<s:property value='#createuserkey'/>&InvoiceNumber=<s:property value='%{encInvoiceNo}'/>&shipTo=<s:property value='%{custSuffix}'/>&InvoiceDate=<s:property value='%{encInvoiceDate}'/>"><s:property value='#extnInvoiceNo'/></a>
		                        				</s:if>
		                        				<s:else>
		                        					<s:set name="splitOrderCount" value="chainedOrderCountMap.get(#orderLineKey)"/>
		                        					<s:if test='chainedFOMap.size() == 1'>	                        						
		                        						<s:iterator  value="#splitOrderCount" id='splitOrder' >																
															<s:if test='%{#splitOrder.getAttribute("Status") == "Invoiced"}'>
																<s:set name="extnInvcNo" value='#splitOrder.getAttribute("ExtnInvoiceNo")'/>
																<s:set name="encInvcNo" value='#splitOrder.getAttribute("EncInvoiceNo")'/>
																<s:set name="extnInvcDt" value='#splitOrder.getAttribute("ExtnInvoicedDate")'/>
																<s:set name="splitCustSuff" value='#splitOrder.getAttribute("ShipToID")'/>														  															  										  	
															  	 : Invoice #: <a class="underlink" target="_blank" href="<s:property value='%{invoiceURL}'/>UserID=<s:property value='#createuserkey'/>&InvoiceNumber=<s:property value='#encInvcNo'/>&shipTo=<s:property value='#splitCustSuff'/>&InvoiceDate=<s:property value='extnInvcDt'/>"><s:property value='#extnInvcNo'/></a>														  				
										        			</s:if>									        			
									        			</s:iterator>	                        						
				                        			</s:if>
		                        				</s:else>		                        					                        				
		                        			</s:if>
	                        			</s:if>
                        			</td>
                        		</tr>
			   				<tr>
								<td>&nbsp;</td>
							</tr>
								<s:if test='#_action.isDeliveryHold() == true || #_action.isShipComplete() == true  || #xpedxExtnRushOrderFlag == "Y" || #_action.isWillCall() == true '>
                        		<tr>
						    		<td class="boldText"  colspan="2">Shipping Options</td>
                        		</tr>
                        		</s:if>
                        		<s:if test='#_action.isDeliveryHold() == true '>
	                        		<tr>
										<td colspan="2">-Order placed on hold until end of business day</td>
	                        		</tr>
                        		</s:if>
                        		<s:if test='#_action.isShipComplete() == true '>
	                        		<tr>
	                        			<td colspan="2">-Ship order complete (will not ship until all items are available)</td>
	                        		</tr>
                        		</s:if>
                        		<s:if test='#xpedxExtnRushOrderFlag == "Y"'>
                        		<tr>
									<td colspan="2">-Rush Order: <s:if test='#xpedxExtnRushOrderDelDate!=null &&  #xpedxExtnRushOrderDelDate!="" '><s:property value='%{#util.formatDate(#xpedxExtnRushOrderDelDate, #wcContext,"yyyy-MM-dd")}'/></s:if> Charges may apply </td>
                        		</tr>
                        		</s:if>             		
                        		<!-- Begin - Changes made by Mitesh Parikh for JIRA 3581 -->
                        		<s:if test='#_action.isWillCall() == true'>
                        		<!-- End - Changes made by Mitesh Parikh for JIRA 3581 -->
	                        		<s:set name='renderPersonInfo' value='#shipFrom' />
	                        		<tr>
	                        			<td>-Will Call - Pick up location: </td>
	                        			<td class="text-left"> <div class="pick-up-location"><s:include value="/xpedx/jsp/order/XpedxReadOnlyAddress.jsp" /></div></td>
	                        		</tr>
                        		</s:if>
                        </table>

                        <!-- end content-holding table -->
                        <div class="clearall">&nbsp;</div>
                        <br/><!-- blank line for george -->
                        <div class="order-placed-by">
                        <s:if test='#xpedxReqDeliveryDate != ""'>
                        	Estimated Delivery Date: <s:property value="%{#util.formatDate(#xpedxReqDeliveryDate, #wcContext)}"/> <br/><br/>
                        </s:if>
							<s:if test='#xpedxExtnHeaderComments != "" && #xpedxHeaderInstructionType == "HEADER"'>
	                        	<p>Comments: <s:property value='#xpedxExtnHeaderComments' /> </p> <br/>
	                        </s:if>
                        Order placed<s:if test='#xpedxOrderedByName!=""' > by <s:property value='#xpedxOrderedByName' /></s:if> 
		                    <s:if test='#xpedxOrderDate!=""' > on  <s:property value='#xpedxOrderDate' /></s:if> 
<%-- jira3431 - showing timestamp and timezone on order detail and web confirmation details page for Web orders - begin --%>
		                     <%-- Added CT For Jira 3431 --%>
		                     <s:if test='#xpedxOrderSource=="Web"'>CT</s:if>
<%-- jira3431 - showing timestamp and timezone on order detail and web confirmation details page for Web orders --%>
		                     <s:if test='#xpedxOrderSource=="B2B" || #xpedxOrderSource=="Web" ||#xpedxOrderSource=="Call Center"' > via  <s:property value='#xpedxOrderSource' /></s:if>. <s:if test='#xpedxEmail!=""' >Order Confirmation emailed to 
		                    <s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@replaceSemiWithcomma(#xpedxEmail)' /></s:if> 
						</div>

		<!-- end border content -->
                <!-- </div> -->
                </fieldset>
	    <!-- end top section -->
	    </div>
	    
	    <!-- begin table header -->
	    <div class="wc-table-header">
	    <table class="full-width no-border">
	    		<tr>
	    			<td class="text-right table-header-bar-left white">My Price (<s:property value='%{currencyCode}'/>)</td>
	    			<s:if test="#orderType != 'Customer'">
						<td class="text-right white" width="125">Shippable Price (<s:property value='%{currencyCode}'/>)</td>
					</s:if>
					<s:else>
						<td class="text-right white" width="125">&nbsp;</td>
					</s:else>
	    			<td class="text-right white table-header-bar-right" width="120">Extended Price (<s:property value='%{currencyCode}'/>)</td>
	    		</tr>
	    </table>
	    </div>
	    <!-- end table header -->
	    <s:set name="isOrderTBD" value="%{0}" />
	    <s:form action="orderAgain" namespace="/order" method="post"  id= "mainOrderForm" name='mainOrderForm'>
			<s:hidden name="orderHeaderKey" value="%{dorderHeaderKey}" />
			<s:hidden name="draft" value="%{#draftOrderFlag}"/>
			<s:hidden name="copyFromOrderHeaderKey" value="%{dorderHeaderKey}" />
			<s:hidden name="orderNo" value="%{@com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils@getFormattedOrderNumber(#OrderExtn)}" />
			<s:hidden name="orderName" value="%{dorderName}" />
			
			<s:iterator value="#_action.getMajorLineElements()" id='orderLine' status='orderLineStatIndex'>
				<s:set name='priceInfo' value='#xutil.getChildElement(#orderLine, "LinePriceInfo")'/>
				<s:set name='lineTotals' value='#xutil.getChildElement(#orderLine, "LineOverallTotals")'/>
				<s:set name='item' value='#xutil.getChildElement(#orderLine, "Item")'/>
				<s:set name='itemDetails' value='#xutil.getChildElement(#orderLine, "ItemDetails")'/>
				<s:set name='primaryInfo' value='#xutil.getChildElement(#itemDetails, "PrimaryInformation")'/>
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
				<s:set name="orderLineStatIndexSize" value='#_action.getMajorLineElements().size()'/>
				<s:set name="myPriceValue" value="%{'false'}" />
				<s:if test='#_action.getShortDescriptionForOrderLine(#orderLine) == #item.getAttribute("ItemID")'>
					<s:set name='showDesc' value='#_action.getDescriptionForOrderLine(#orderLine)'/>
				</s:if>
				<s:else>
					<s:set name='showDesc' value='#_action.getShortDescriptionForOrderLine(#orderLine)'/>
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
			
			    <div 
			    <s:if test='%{#orderLineStatIndexSize == 1}'>class="middle-section rounded-border rr-border WC" id="one-item"</s:if>
			    	<s:elseif test='%{#orderLineStatIndex.count == 1}'>class="middle-section rounded-border rr-border WC" id="first-item"</s:elseif>
					<s:elseif test="#orderLineStatIndex.last">class="middle-section rounded-border rr-border WC" id="last-item"</s:elseif>
					<s:else>class="middle-section rounded-border rr-border WC"</s:else> >
			    	<!-- containing div to get the margin  -->
			    	<div class="div-margin">
			    	    <!--begin left section -->
			    	    <table>
			    	    <tr>
			    	    <td rowspan="2" width="310px" valign="top">
			    	    <div class="left-section ODLS" >
			    	    <div class="pin-height">
				    	    <s:url id='detailURL' namespace='/catalog' action='itemDetails.action'>
			                    <s:param name='itemID'><s:property value='#item.getAttribute("ItemID")'/></s:param>
			                    <s:param name='unitOfMeasure'><s:property value='#item.getAttribute("UnitOfMeasure")'/></s:param>
			                    <s:param name='_r_url_' value='%{orderDetailsURL}'/>
			                </s:url>
					    <s:if test='(#orderLine.getAttribute("LineType") != "C") && (#orderLine.getAttribute("LineType") != "M")'>
			                <s:a href="%{#detailURL}" id="detailAnchor_%{#orderLineKey}" tabindex='%{#itemPanelStartTabIndex+#tabIndexCount}'>
						    	<div class="short-description"><s:property value='#showDesc'/></div>
								<s:if test='#_action.getShortDescriptionForOrderLine(#orderLine) != #item.getAttribute("ItemID")'>
									<s:property value='@com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils@getFormattedDescription(#unformatteddesc)' escape="false"/>
								</s:if>
						    </s:a>
						    </s:if>
						    <s:else>
						    	<div class="short-description_M"><s:property value='#showDesc'/></div>
								<s:if test='#_action.getShortDescriptionForOrderLine(#orderLine) != #item.getAttribute("ItemID")'>
									<s:property value='@com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils@getFormattedDescription(#unformatteddesc)' escape="false"/>
								</s:if>
						    </s:else>
			    		</div>
					    <p class="line-spacing"><s:property value="wCContext.storefrontId" /> <s:property value="#xpedxItemLabel" />: <s:property value='#item.getAttribute("ItemID")'/>
					    	<s:if test='#certFlag=="Y"'>
							 	<img border="none"  src="/swc/xpedx/images/catalog/green-e-logo_small.png" alt="" style="margin-left:0px; display: inline;"/>
							 </s:if>	
					    </p>
					    <s:if test='skuMap!=null && skuMap.size()>0 && customerSku!=null && customerSku!=""'>
			    			 
			    			<s:set name='itemSkuMap' value='%{skuMap.get(#item.getAttribute("ItemID"))}'/>
			    			<s:set name='itemSkuVal' value='%{#itemSkuMap.get(customerSku)}'/>
			    			
							<p class="OD-bottom-margin line-spacing">
								<s:if test='%{customerSku == "1"}' >
									<s:property value="#customerItemLabel" />:
								</s:if>
								<s:elseif test='%{customerSku == "2"}'>
									<s:property value="#manufacturerItemLabel" />:
								</s:elseif>
								<s:else>
									<s:property value="#mpcItemLabel" />:
								</s:else>
								<s:property value='#itemSkuVal' />
							</p>
	
						</s:if>

					    </div>
			    	    <!--end left section -->
			    	    </td>
			    	    <td width="638px" valign="top">
			    	    <!--begin right-section -->
			    	    <s:set name='orderLineTranQuantity' value='#xutil.getChildElement(#orderLine, "OrderLineTranQuantity")'/>
                       	<s:set name='orderLineExtnElem' value='#xutil.getChildElement(#orderLine, "Extn")'/>
                       	<s:set name="orderType" value='%{#xutil.getAttribute(#orderDetail, "OrderType")}' />
                       	<%-- <s:set name="uom" value='#xutil.getAttribute(#item, "UnitOfMeasure")'/> --%>
                       	<s:set name="uom" value='#orderLineTranQuantity.getAttribute("TransactionalUOM")'/>
                       	<s:set name="reqTotalQty" value="#util.formatQuantity(wCContext,#priceUtil.getLineTotal(#orderLineTranQuantity.getAttribute('OrderedQty'),'1','0') )" />
				 		<s:set name="priceInReqUOM" value="#priceUtil.getLineTotal(#orderLineExtnElem.getAttribute('ExtnReqUOMUnitPrice'),'1','0')" />
				 		<%-- <s:set name="extendedPrice" value="#reqTotalQty * #priceInReqUOM" /> --%>
				 		<s:set name="extendedPrice" value="#priceUtil.getLineTotal(#orderLineExtnElem.getAttribute('ExtnExtendedPrice'),'1','0')" />
					    <div class="OD-right-section" >
					    <s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
					    <s:set name="priceWithCurrencyTemp1" value='%{#xpedxutil.formatPriceWithCurrencySymbolWithPrecisionFive(wCContext, #currencyCode, "0")}' /><!-- added for 2787jira -->
					    	<table class="full-width"> <!-- my-price-table -->
					    		<tr>
					    		 
					    			<td class="text-right" width="81">
						    			<s:if test='(#orderLine.getAttribute("LineType") != "C") && (#orderLine.getAttribute("LineType") != "M")'>
						    			  Ordered&nbsp;Qty:
						    			</s:if>
					    			</td>
					    			<s:set name='orderqty' value='#_action.getCalculatedOrderedQuantityWithoutDecimal(#orderLine)' />
									<%--<s:set name='orderdqty' value='%{#strUtil.replace(#orderqty, ".0", "")}' /> --%> 
									<s:set name='orderdqty' value="#xpedxUtilBean.formatQuantityForCommas(#orderdqty)"/>
					    			<td class="text-left"  width="175">	
						    			 <s:if test='(#orderLine.getAttribute("LineType") != "C") && (#orderLine.getAttribute("LineType") != "M")'>				    			
						    			  <s:property value='#xpedxUtilBean.formatQuantityForCommas(#orderqty)'/>&nbsp;<s:property value='#wcUtil.getUOMDescription(#uom)'/> 
						    			 </s:if>
					    			</td>
					    			<td class="text-right" width="95">						    		
						    			<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
						    				<s:set name="theMyPrice" value='#xpedxutil.formatPriceWithCurrencySymbolWithPrecisionFive(#wcContext,#currencyCode,#orderLineExtnElem.getAttribute("ExtnUnitPrice"))'/>
						    			    <s:if test="%{#theMyPrice==#priceWithCurrencyTemp1}">						    			     
						    			     <s:if test='(#orderLine.getAttribute("LineType") != "C") && (#orderLine.getAttribute("LineType") != "M")'>	
						    			     	<s:set name="isMyPriceZero" value="%{'true'}" />
						    			     	<s:set name="myPriceValue" value="%{'true'}" />					    			        
											    <span class="red bold"> <s:text name='MSG.SWC.ORDR.ORDR.GENERIC.CALLFORPRICE' /> </span>
											 </s:if> 
                                            </s:if>
                                            <s:else>
                                             <s:if test='(#orderLine.getAttribute("LineType") != "C") && (#orderLine.getAttribute("LineType") != "M")'>   
	                                		      <s:property value='#theMyPrice'/> <%-- theMyPrice is the Unit Price --%>
	                                		 </s:if>  
	                                		</s:else>
	                                	</s:if>
									</td>
							     
									<td class="text-right" width="127">						    			
						    			<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
						    				<s:if test="#orderType != 'Customer'">
						    				  <s:if test='(#orderLine.getAttribute("LineType") != "C") && (#orderLine.getAttribute("LineType") != "M")'>	
						    				  	<s:if test="%{#myPriceValue == 'false'}">	    					
						    						<s:property value='#xpedxutil.formatPriceWithCurrencySymbol(wCContext,#currencyCode, #orderLineExtnElem.getAttribute("ExtnLineShippableTotal"))'/>
						    					</s:if>
						    					<s:else>
						    						<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>
						    					</s:else>
						    				  </s:if>
						    				</s:if>
						    			</s:if>
									</td>
									<td class="text-right" width="128">						    			
						    			<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
						    			    <s:set name="theExtendedPrice" value='#util.formatPriceWithCurrencySymbol(wCContext, #currencyCode, #extendedPrice)'/>
						    			    <s:if test="%{#myPriceValue == 'true'}">
						    			       <s:if test='(#orderLine.getAttribute("LineType") != "C")'>
											    <span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span> 
											    <s:set name="isOrderTBD" value="%{#isOrderTBD+1}" /> 
											   </s:if>
                                            </s:if>
                                            <s:else>
                                              <s:if test='(#orderLine.getAttribute("LineType") != "C")'>
	                                		    <s:property value='#theExtendedPrice'/>
	                                		  </s:if>
	                                		</s:else>																															  
										</s:if>
									</td>
					    		</tr>
					    		<tr>
					    			<s:if test="#orderType != 'Customer'">
					    				<td class="text-right">
					    				 <s:if test='(#orderLine.getAttribute("LineType") != "C") && (#orderLine.getAttribute("LineType") != "M")'>
					    				  Shippable Qty:
					    				 </s:if>
					    				</td>
					    				<s:set name='shipqty' value='#orderLineExtnElem.getAttribute("ExtnReqShipOrdQty")' />
										<s:set name='shipqty' value='%{#strUtil.replace(#shipqty, ".00", "")}' />
										<s:set name='shipqty' value="#xpedxUtilBean.formatQuantityForCommas(#shipqty)"/>
						    			<td class="text-left">
						    			  <s:if test='(#orderLine.getAttribute("LineType") != "C") && (#orderLine.getAttribute("LineType") != "M")'>
						    			    <s:property value='%{#shipqty}'/>&nbsp;<s:property value='#wcUtil.getUOMDescription(#uom)'/> 
						    			  </s:if>
						    			</td>
					    			</s:if>
					    			<s:else>
						    			<td class="text-right" width="81">&nbsp;</td>
						    			<td class="text-left"  width="175">&nbsp;</td>
					    			</s:else>
					    			<td class="text-right">					    			
						    		<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
						    			  <s:if test="%{#myPriceValue == 'false'}">
                                              <s:if test='(#orderLine.getAttribute("LineType") != "C") && (#orderLine.getAttribute("LineType") != "M")'>
					    						per <s:property value='#wcUtil.getUOMDescription(#orderLineExtnElem.getAttribute("ExtnPricingUOM"))'/>
					    					  </s:if>
					    				  </s:if>
					    			</s:if>
					    			</td>
					    			<td class="text-right">&nbsp;</td>
					    		</tr>
					    		<tr>
					    			<s:if test="#orderType != 'Customer'">
						    			<td class="text-right">
						    			  <s:if test='(#orderLine.getAttribute("LineType") != "C") && (#orderLine.getAttribute("LineType") != "M")'>
						    			    Backorder Qty:
						    			  </s:if>
						    			</td>
						    			<s:set name='backqty' value='#orderLineExtnElem.getAttribute("ExtnReqBackOrdQty")' />
										<s:set name='backqty' value='%{#strUtil.replace(#backqty, ".00", "")}' />
										<s:set name='backqty' value="#xpedxUtilBean.formatQuantityForCommas(#backqty)"/>
						    			<td class="text-left">
						    			  <s:if test='(#orderLine.getAttribute("LineType") != "C") && (#orderLine.getAttribute("LineType") != "M")'>
						    			   <s:property value='%{#backqty}'/>&nbsp;<s:property value='#wcUtil.getUOMDescription(#uom)'/> 
						    			  </s:if>
						    		    </td>
					    			</s:if>
					    			<s:else>
					    				<td class="text-right" width="81">&nbsp;</td>
						    			<td class="text-left"  width="175">&nbsp;</td>
					    			</s:else>
					    			<td class="text-right">&nbsp;</td>
					    			<td class="text-right">&nbsp;</td>
					    		</tr>
					    		<s:set name="splitOrderAttributes" value="chainedOrderMap.get(#orderLineKey)"/>
					    		<s:set name="splitOrderCount" value="chainedOrderCountMap.get(#orderLineKey)"/>
								<s:if test='#orderType != "Customer" ' >
									<tr>
						    			<td class="text-right">&nbsp;</td>
						    			<td class="text-left">&nbsp;</td>
						    			<td>&nbsp;</td>
						    			<td class="text-right">&nbsp;</td>
						    			<td class="text-right">&nbsp;</td>
						    		</tr>								
								</s:if>
								<s:else>
									<tr>
						    			<td class="text-right">Line&nbsp;Status:</td>
						    			<td class="text-left">
						    			<s:if test='#splitOrderCount.size() > 1'>
						    				<s:set name="splitedDivVal" value="%{'splitedDiv_'+#orderLineKey}"/>
						    				<%--<a id="split-order" href="#" class="underlink">Split</a> --%>
						    				<a id='split-order_<s:property value="#orderLineKey" />'  href="#" >Split</a>
						    			</s:if>
						    			<s:else>
							    			<s:url id="legacyOrderDetailsURL" action="orderDetail" escapeAmp="false" >
												<s:param name="orderHeaderKey" value='#splitOrderAttributes.get(0)'/>  
												<s:param name="returnUrlForOrderList" value="%{orderListReturnUrl}"/>
											</s:url>
												Order #: 
												<s:if test='%{#splitOrderAttributes.get(4) == "In progress"}'>
													<s:property value="#splitOrderAttributes.get(4)"/>
												</s:if>
												<s:else>
													<s:a href="%{legacyOrderDetailsURL}"><s:property value="#splitOrderAttributes.get(4)"/></s:a>
												</s:else>												 
							    				<!--  <a class="underlink" href="#"> <s:property value='@com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils@getFormattedOrderNumber(#OrderExtn)'/> </a>-->
												</td>
								    			<td>&nbsp;</td>
								    			<td class="text-right">&nbsp;</td>
								    			<td class="text-right">&nbsp;</td>
												</tr><tr>
												<td>&nbsp;</td>
												<td rowspan="1" valign="top" style="text-align:left;">
												<s:set name='splitqty' value='#splitOrderAttributes.get(2)' />
												<s:set name='splitqty' value='%{#strUtil.replace(#splitqty, ".00", "")}' />
												<s:set name='splitqty' value="#xpedxUtilBean.formatQuantityForCommas(#splitqty)"/>
												<s:property value='%{#splitOrderAttributes.get(3)}'/>: <s:property value="#splitqty"/> <s:property value='#wcUtil.getUOMDescription(#uom)'/>						    					
									    		<s:if test="%{#ViewInvoicesFlag}">	
									    			<s:if test='chainedFOMap.size() > 1'>
										    			<s:if test='%{#splitOrderAttributes.get(3) == "Invoiced"}'>		
										    				<s:set name='enptInvcNo' value='#splitOrderAttributes.get(5)' />
										    				<s:set name='splitCustomerSuff' value='#splitOrderAttributes.get(6)' />
										    				<s:set name='extnInvcedDt' value='#splitOrderAttributes.get(7)' />
										    				<s:set name='extnInvocNo' value='#splitOrderAttributes.get(8)' />						    			
											    			</td>
											    			<td>&nbsp;</td>
											    			<td class="text-right">&nbsp;</td>
											    			<td class="text-right">&nbsp;</td>
											    			</tr><tr>
															<td>&nbsp;</td>
															<td rowspan="1" valign="top" style="text-align:left;">
															Invoice #: <a class="underlink" target="_blank" href="<s:property value='%{invoiceURL}'/>UserID=<s:property value='#createuserkey'/>&InvoiceNumber=<s:property value='#enptInvcNo'/>&shipTo=<s:property value='#splitCustomerSuff'/>&InvoiceDate=<s:property value='extnInvcedDt'/>"><s:property value='#extnInvocNo'/></a>
														</s:if>
													</s:if>
												</s:if>						    					
						    			</s:else>
										</td>
						    			<td>&nbsp;</td>
						    			<td class="text-right">&nbsp;</td>
						    			<td class="text-right">&nbsp;</td>
						    		</tr>
								</s:else>													    		
					    		<tr>
					    			<!--<td colspan="2">
					    			
										<s:if test='#splitOrderAttributes != null'>
											<s:if test='#splitOrderCount.size() > 1'>
												<div id='splitedDiv_<s:property value="#orderLineKey" />' style="display :none">
												
												<s:iterator  value="#splitOrderCount" id='splitOrder' >
													<s:url id="legacyOrderDetailsURL" action="orderDetail" escapeAmp="false" >
															<s:param name="orderHeaderKey" value='#splitOrder.getAttribute("OrderHeaderKey")'/>  
													</s:url>
													Order #: <s:a href="%{legacyOrderDetailsURL}"><s:property value='#splitOrder.getAttribute("LegacyOrderNumber")'/></s:a> <s:property value='%{#overallStatus}'/>: <s:property value='#splitOrder.getAttribute("Quantity")'/> <s:property value='#wcUtil.getUOMDescription(#uom)'/>
													<br/>
												</s:iterator>
												</div>
											</s:if>
											<s:else>
												<s:url id="legacyOrderDetailsURL" action="orderDetail" escapeAmp="false" >
															<s:param name="orderHeaderKey" value='#splitOrderAttributes.get(0)'/>  
													</s:url>
													Order #: <s:a href="%{legacyOrderDetailsURL}"><s:property value="#splitOrderAttributes.get(1)"/></s:a> <s:property value='%{#overallStatus}'/>: <s:property value="#splitOrderAttributes.get(2)"/> <s:property value='#wcUtil.getUOMDescription(#uom)'/>
											</s:else>
											<s:if test='%{#status == "Invoiced"}'>
											<br/>Invoice #: <a class="underlink" href="https://distributioninvoicing.com/xpx1000_requestinterception.aspx?UserKey=<s:property value='#createuserkey'/>&InvoiceNumber=<s:property value='#extnInvoiceNo'/>&shipTo=<s:property value='#shipToId'/>&InvoiceDate=<s:property value='extnInvoicedDate'/>"><s:property value='#extnInvoiceNo'/></a>
		                        			</s:if>
										</s:if>
									</td> -->
					    			<td>
					    			<script type="text/javascript">
											$(document).ready(function(){
												$(document).pngFix();
												$('#split-order_<s:property value="#orderLineKey" />').click(function(){
														var offset = $(this).offset();
														/* $('#split-order-overlay_<s:property value="#orderLineKey" />').css({ top: offset.top+25 });        */  
														$('#split-order-overlay_<s:property value="#orderLineKey" />').toggle();
														return false;
												});
												$('#popup-window-close_<s:property value="#orderLineKey" />').click(function(){
														$('#split-order-overlay_<s:property value="#orderLineKey" />').toggle();
														return false;
												});
												
											});
											</script>
											<div id='split-order-overlay_<s:property value="#orderLineKey" />' style="display: none; -moz-border-radius:5px;border-radius: 5px;-webkit-border-radius: 5px;background: #fafafa;	position: absolute;border: 2px solid #CCCCCC;">
											<div>
												<table>
													<tr>
														<td colspan="3"> <span class="page-title"> Split Orders </span></td>
														<td> <img class="float-right pointers padding-5" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_charcoal_x.png" id="popup-window-close_<s:property value="#orderLineKey" />" width="12" height="12" title="Close" alt="[Close Window]" /> </td>
													</tr>
													<tr>
														<td>  
															<s:iterator  value="#splitOrderCount" id='splitOrder' >
																<s:set name='splitqty' value='#splitOrder.getAttribute("Quantity")' />
																<s:set name='splitqty' value='%{#strUtil.replace(#splitqty, ".00", "")}' />
																<s:set name='splitqty' value="#xpedxUtilBean.formatQuantityForCommas(#splitqty)"/>
																<s:url id="legacyOrderDetailsURL" action="orderDetail" escapeAmp="false" >
																		<s:param name="orderHeaderKey" value='#splitOrder.getAttribute("OrderHeaderKey")'/>  
																		<s:param name="orderListReturnUrl" value="%{orderListReturnUrl}"/>
																</s:url>
																Order #:<s:if test='%{#splitOrder.getAttribute("FormattedLegacyOrderNumber") == "In progress"}'><s:property value='#splitOrder.getAttribute("FormattedLegacyOrderNumber")'/></s:if><s:else> <s:a href="%{legacyOrderDetailsURL}"><s:property value='#splitOrder.getAttribute("FormattedLegacyOrderNumber")'/></s:a></s:else> <s:property value='#splitOrder.getAttribute("Status")'/>: <s:property value='%{#splitqty}'/> <s:property value='#wcUtil.getUOMDescription(#uom)'/>
																<br/>
															</s:iterator>				
														</td>
														<td valign="top"> 
															<s:if test="%{#ViewInvoicesFlag}">
																<s:iterator  value="#splitOrderCount" id='splitOrder' >																
																	<s:if test='%{#splitOrder.getAttribute("Status") == "Invoiced"}'>
																		<s:set name="extnInvcNo" value='#splitOrder.getAttribute("ExtnInvoiceNo")'/>
																		<s:set name="encInvcNo" value='#splitOrder.getAttribute("EncInvoiceNo")'/>
																		<s:set name="extnInvcDt" value='#splitOrder.getAttribute("ExtnInvoicedDate")'/>
																		<s:set name="splitCustSuff" value='#splitOrder.getAttribute("ShipToID")'/>
																	  Invoice #: <a class="underlink" target="_blank" href="<s:property value='%{invoiceURL}'/>UserID=<s:property value='#createuserkey'/>&InvoiceNumber=<s:property value='#encInvcNo'/>&shipTo=<s:property value='#splitCustSuff'/>&InvoiceDate=<s:property value='extnInvcDt'/>"><s:property value='#extnInvcNo'/></a>
																	  <br/>
												        			</s:if>
												        			<s:else>
												        				<br/>
												        			</s:else>
											        			</s:iterator>
											        		</s:if>				
												        </td>
													</tr>			
												</table>
											</div>
										</div>
									</td>
					    			<td class="text-right">&nbsp;</td>
					    			<td class="text-right">&nbsp;</td>
					    		</tr>
					    		<tr>
					    			<td>&nbsp;</td>
					    			<td class="text-left">&nbsp;</td>
					    			<td>&nbsp;</td>
					    			<td class="text-right">&nbsp;</td>
					    			<!-- <td class="text-right">&nbsp;</td> -->
					    		</tr>
					    	</table>    	    	
					    </div>
			    	    <!--end right-section -->
			    	    </td>
						</tr>
						<tr>
						<td>
						
		    			<div class="od-special-instructions line-spacing" >
							<s:if test='#orderLineSpecialInstruction != "" && #orderLineSpecialInstruction != null '>
							<p class="special-instructions-padding">Special Instructions:</p>
								<div class="checkout-special-instructions"><s:property value="#orderLineSpecialInstruction"/></div>
							</s:if>
							<s:else>
								<div class="checkout-special-instructions"> <br/> </div>
							</s:else>								
						</div>
						
			    	    <!--begin bottom-right-section -->
					    <div class="wc-bottom-right-section" >
						    <s:set name='linelineoverallTotals' value='#util.getElement(#orderLine, "LineOverallTotals")'/>
					    	<%-- <s:set name='adjustment' value='#xutil.getDoubleAttribute(#linelineoverallTotals,"DisplayLineAdjustments")' />
							<s:if test='%{#adjustment != 0.00}'>
								<p class="text-left">A Discount of <a id='tip_<s:property value="#orderLineKey"/>' href="javascript:displayLineAdjustments('adjustmentsLightBox','<s:property value='#orderLineKey'/>')">
								<s:property value='#util.formatPriceWithCurrencySymbol(wCContext,#currencyCode,#adjustment)'/></a> has applied to this line.</p>
							</s:if>
							--%>
							<s:set name='adjustment' value='#xutil.getDoubleAttribute(#orderLineExtnElem,"ExtnAdjDollarAmt")' />
							<s:if test='%{#adjustment != 0.00}'>
								<p>A Discount of <a id='tip_<s:property value="#orderLineKey"/>' href="javascript:displayLineAdjustments('adjustmentsLightBox','<s:property value='#orderLineKey'/>')">
								<s:property value='#util.formatPriceWithCurrencySymbol(wCContext,#currencyCode,#adjustment)'/></a> has applied to this line.</p>
							</s:if>
					    	<table class="text-left od-customer-defined-fields">
						    	<s:iterator value='customerFieldsMap'>
									<s:set name='FieldLabel' value='key'/>
									<s:set name='FieldValue' value='value'/>
									<s:set name='customLbl' value='%{"Extn" + #FieldLabel}' />
									<s:if test='(#orderLine.getAttribute("LineType") =="P" || #orderLine.getAttribute("LineType") =="S")'>
										<s:if test=' (#FieldLabel == "CustomerPONo") || (#FieldLabel == "CustomerLinePONo") '>
											<s:if test="%{#orderLine.getAttribute(#FieldLabel) != null && #orderLine.getAttribute(#FieldLabel) != ''}">
												<tr>
													<td class="text-right"><s:property value="%{#FieldValue}" />:</td>
													<td><s:label id="orderLine%{#FieldLabel}_%{#orderLineKey}" name='orderLine%{#FieldLabel}' value="%{#orderLine.getAttribute(#FieldLabel)}"/></td>
												</tr>
											</s:if>
										</s:if>
										<s:else>
											<s:if test="%{#xpedxOrderLineExtn.getAttribute(#customLbl) != null && #xpedxOrderLineExtn.getAttribute(#customLbl) != ''}">
												<tr>
													<td class="text-right"><s:property value="%{#FieldValue}" />:</td>
													<td><s:label id="orderLine%{#FieldLabel}_%{#orderLineKey}" name='orderLine%{#FieldLabel}' value="%{#xpedxOrderLineExtn.getAttribute(#customLbl)}"/></td>
												</tr>
											</s:if>
										</s:else>
									</s:if>
								</s:iterator>
					    	</table>	
				    	</div><!--end bottom-right-section -->
		    		    </td>
		    		    </tr>
		    		    </table>
		    		    <div class="clearall">&nbsp;</div>
					</div> <!-- end containing border div -->
				</div><!-- end box section - "div class="middle-section rounded-border rr-border"" -->
			</s:iterator>
		</s:form>
	<s:set name = "setTBD" value="%{false}"/>
	<s:if test="%{#_action.getMajorLineElements().size() == #isOrderTBD}">
		<s:set name = "setTBD" value="%{true}"/>
	</s:if>
	
        <!-- begin bottom section -->
        <s:set name='subtotalWithoutTaxes' value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#overallTotals.getAttribute("SubtotalWithoutTaxes"))'/>
		<s:set name='hdrShippingTotal' value='#xutil.getDoubleAttribute(#overallTotals,"HdrShippingTotal")'/>
		<s:set name='hdrShippingBaseCharge' value='#xutil.getDoubleAttribute(#overallTotals,"HdrShippingBaseCharge")'/>
		<s:set name='hdrAdjustmentWithoutShipping' value='#xutil.getDoubleAttribute(#overallTotals,"HeaderAdjustmentWithoutShipping")'/>
		
		<s:set name='headerAdjustmentWithoutShipping' value ='%{#hdrAdjustmentWithoutShipping - #hdrShippingTotal + #hdrShippingBaseCharge}'/>
		<s:set name='adjustedSubtotalWithoutTaxes' value ='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,(#overallTotals.getAttribute("AdjustedSubtotalWithoutTaxes") - #hdrShippingTotal + #hdrShippingBaseCharge))'/>
		<s:set name='grandTax' value ='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#overallTotals.getAttribute("GrandTax"))'/>
		<s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
		<s:set name='shippingCharges' value='#util.formatPriceWithCurrencySymbol(#wcContext,#,#overallTotals.getAttribute("HdrShippingTotal"))'/>
		<s:set name='shippableOrderPrice' value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#orderDetailExtn.getAttribute("ExtnTotalShipValue"))'/>
		<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
			<div class="cart-sum-right">
				<table cellspacing="0"  align="right">
					<tr>
						<th>Subtotal:</th>
						<td> 	    			    
					    	<s:set name='extnOrderSubTotal' value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#orderExtn.getAttribute("ExtnOrderSubTotal"))' />
 			  		 			<s:if test="%{#extnOrderSubTotal == #priceWithCurrencyTemp || #setTBD == true}">	
								<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>  
	                       	</s:if>
	                       	<s:else>
	                		   		 &nbsp;<s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#OrderExtn.getAttribute("ExtnOrderSubTotal"))' />
	                		</s:else>											
						</td>
					</tr>
					<tr>
						<th>Order Total Adjustments:</th>
						<td>
		                     <a
								href="javascript:displayLightbox('orderTotalAdjustmentLightBox')" id='tip_<s:property value="#orderHeaderKey"/>'
								tabindex="<s:property value='%{#tabIndex}'/>"> <span
								class="nowrap underlink">
		             		    &nbsp;<s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#OrderExtn.getAttribute("ExtnLegTotOrderAdjustments"))' /></span></a>
						</td>
					</tr>
					<tr>
						<th>Adjusted Subtotal:</th>
						<td>
								<s:set name='extnTotOrdValWithoutTaxes' value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#orderExtn.getAttribute("ExtnTotOrdValWithoutTaxes"))' />
	 			  				<s:if test="%{#extnTotOrdValWithoutTaxes == #priceWithCurrencyTemp || #setTBD == true}">
							    	<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>  
	                       		</s:if>
	                      		<s:else>
	                		    		&nbsp;<s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#OrderExtn.getAttribute("ExtnTotOrdValWithoutTaxes"))' />
	                			</s:else>					
							
						</td>
					</tr>
					<s:if test="#orderType == 'DIRECT_ORDER'">
						<tr>
							<th>Shippable Total:</th>
							<td><s:property value='#shippableOrderPrice'/></td>
						</tr>
					</s:if>
					<tr>
						<th>Tax:</th>
						<td class="gray">
							<s:if test='#_action.getDisplayTaxAndShipHandlingAmt()=="Y"'>											    	                        
		                        <s:if test="%{#isMyPriceZero == 'false'}">
		                        	<s:if test='#orderType != "Customer" && #xpedxLegacyOrderNumber != ""'>
		                        		<s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#OrderExtn.getAttribute("ExtnOrderTax"))' />
		                        	</s:if>
		                        	<s:else>
		                        		<s:if test='#_action.getChainedFOMap().size() >= 1'>	
		                        			<s:set name="isInProgress" value="%{'false'}" />
		                        			<s:iterator value="chainedFOMap" >												
												<s:set name='chainedFONo' value="value"/>	
												<s:if test='#chainedFONo == "In progress"'>
													<s:set name="isInProgress" value="%{'true'}" />
												</s:if>												
											</s:iterator>             
											<s:if test="%{#isInProgress == 'false'}">
												<s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#OrderExtn.getAttribute("ExtnOrderTax"))' />
											</s:if>    
											<s:else>										
												<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>
											</s:else>       				                        			
		                        		</s:if>
										<s:else>										
											<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>
										</s:else>
									</s:else>							
								</s:if>
								<s:else>								
									<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>
								</s:else>
							</s:if>
							<s:else>
								<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>
							</s:else>
						</td>
					</tr>
					<tr class="bottom-padding">
						<th>Shipping &amp; Handling:</th>					
						<td class="gray">
							<s:if test='#_action.getDisplayTaxAndShipHandlingAmt()=="Y"'>
								<s:if test="%{#isMyPriceZero == 'false'}">
		                        	<s:if test='#orderType != "Customer" && #xpedxLegacyOrderNumber != ""'>
		                        		<s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#OrderExtn.getAttribute("ExtnTotalOrderFreight"))' />
		                        	</s:if>
		                        	<s:else>
		                        		<s:if test='#_action.getChainedFOMap().size() >= 1'>	
		                        			<s:set name="isInProgress" value="%{'false'}" />
		                        			<s:iterator value="chainedFOMap" >												
												<s:set name='chainedFONo' value="value"/>	
												<s:if test='#chainedFONo == "In progress"'>
													<s:set name="isInProgress" value="%{'true'}" />
												</s:if>												
											</s:iterator>             
											<s:if test="%{#isInProgress == 'false'}">
												<s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#OrderExtn.getAttribute("ExtnTotalOrderFreight"))' />
											</s:if>    
											<s:else>										
												<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>
											</s:else>       				                        			
		                        		</s:if>
										<s:else>										
											<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>
										</s:else>
									</s:else>							
								</s:if>
								<s:else>								
									<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>
								</s:else>
							</s:if>
							<s:else>
								<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>
							</s:else>									
						</td>						
					</tr>
					<tr class="order-total">
						<th>Order Total (<s:property value='%{currencyCode}'/>):</th>
						<td>
							<s:set name='extnTotalOrderValue'	value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#orderExtn.getAttribute("ExtnTotalOrderValue"))'/>
		 			  			 <s:if test="%{#extnTotalOrderValue == #priceWithCurrencyTemp || #setTBD == true}">
							    	<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>  
	                     	</s:if>
	                        <s:else> 
	                        	 <s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#OrderExtn.getAttribute("ExtnTotalOrderValue"))' />
	                		</s:else>			
						</td>
					</tr>
				</table>
				
				<!-- image here -->
			</div>
		</s:if>		
		<div class="clearall">&nbsp;</div>
		
		<div id="wc-btn-bar" class="padding-top" style="width:98.3%">
		<!--Changed returnOrderListId for  Jira 2039  -->
			<s:a id='returnOrderListId' href='%{orderListReturnUrl}' cssClass="grey-ui-btn return-orders float-left"><span>Return to Orders</span></s:a>
			<s:if test="%{'Invoiced' == #xutil.getAttribute(#orderDetail,'Status')}">
			<s:a href="%{returnItemsLink}" cssClass="grey-ui-btn float-left"><span>Return Items</span></s:a>
			</s:if>
			<%-- Commented for bug# 1913
			 <s:if test ="#_action.isCancel() && ! #_action.isCustomerOrder(#orderDetail)" > --%>
			<s:if test="!#isEstimator">
					<s:if test='#_action.isCustomerOrder(#orderDetail)'>					
							<s:if test="#_action.isEditableOrder() && ! #_action.isFOCreated() && ! #_action.isCSRReview()">
			      				<a href="javascript:cancelOrder();" class="grey-ui-btn"><span>Cancel Order</span></a>
			     			</s:if>
					</s:if>
					<s:else>				
						<s:if test="#_action.isEditableOrder()">
							<a href="javascript:cancelOrder();" class="grey-ui-btn"><span>Cancel Order</span></a>
					  	</s:if>				
					</s:else>
			</s:if>
						
			<s:if test ="#_action.approvalAllowed()" >
				<s:a key="accept" href="javascript:openNotePanel('approvalNotesPanel', 'Approve','%{dorderHeaderKey}'); " cssClass="grey-ui-btn" cssStyle="float:right"><span>Approve / Reject Order</span></s:a>
<%-- 				<s:a key="reject" href="javascript:openNotePanel('approvalNotesPanel', 'Reject','%{dorderHeaderKey}'); " cssClass="grey-ui-btn" cssStyle="float:right"><span>Reject Order</span></s:a> --%>
			</s:if>
			
			<s:if test="!#isEstimator">
			<s:if test='%{#status != "Cancelled"}'>
				<s:if test="#_action.canOrderAgain()  && #orderType == 'Customer'">
					<%--Added the below condition for extnLegOrderType for Jira 3544 --%>
					<s:if test='(#extnLegOrderType != "Q" && #extnLegOrderType != "F" && #extnLegOrderType != "R" && #extnLegOrderType != "I")'>
					<a href="javascript:xpedxOrderAgain();" style="float:right" class="grey-ui-btn re-order"><span>Re-Order</span></a>
					</s:if>
				</s:if>
				</s:if>
			</s:if>
			<s:if test="!#isEstimator">
					<s:if test='#_action.isCustomerOrder(#orderDetail)'>					
							<s:if test="#_action.isEditableOrder() && ! #_action.isFOCreated() && ! #_action.isCSRReview()">
								<a href="javascript:editOrder('${urlEditOrderId}');" style="float:right" class="grey-ui-btn edit-order"><span>Edit Order</span></a>
							</s:if>
					</s:if>		
					<s:else>				
						<s:if test="#_action.isEditableOrder()">			     
							<a href="javascript:editOrder('${urlEditOrderId}');"  style="float:right" class="grey-ui-btn"><span>Edit Order</span></a>					
						</s:if>				
					</s:else>
			</s:if>
			<br/> <br/> <br/>
		</div>
		 <!-- end bottom section -->
		</div>
    </div>
<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
</div>

<s:include value="XPEDXOrderTotalAdjustments.jsp" />
<script type="text/javascript">
/* 	Ext.onReady(function(){             	          		 
        		  new Ext.ToolTip({        
        			  	 target: 'tip_${orderHeaderKey}',
					 anchor: 'right',
					 html:	Ext.DomQuery.selectNode('.orderTotalAdjustmentLightBox').innerHTML,													
					 autoHide: true,
					 closable: true
				});
        		 	Ext.QuickTips.init();
        		 }); */
</script>

<div id="split-order-overlay" style="display: none; right:893px; top:215.5px;">
	<div>
		<table>
			<tr>
				<td colspan="3"><span class="page-title"> Split Orders </span></td>
				<td> <img class="float-right" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_charcoal_x.png" id="popup-window-close" width="12" height="12" title="Close" alt="[Close Window]" /> </td>
			</tr>
			<tr>
				<td>  
					<s:iterator value="chainedFOMap" >
						<s:set name='chainedFOKey' value='key'/>
						<s:set name='chainedFONo' value="value"/>
	                    Order #:
							<s:url id="orderDetailsURL" action="orderDetail">
							    <s:param name="orderHeaderKey" value="#chainedFOKey"/>
							    <s:param name="orderListReturnUrl" value="%{orderListReturnUrl}"/>
							</s:url>
							<s:if test='#chainedFONo == "In progress"'>
								<s:property value='#chainedFONo'/>
							</s:if>
							<s:else>
								<s:a href="%{orderDetailsURL}">
								  <s:property value='#chainedFONo'/>
								</s:a>
							</s:else>						
						<br>	
					</s:iterator> 				
				</td>
			</tr>			
		</table>
	</div>
</div>

 <swc:dialogPanel title='${emailDialogTitle}' isModal="true" id="emailDialogPanel">
        <div id="ajax-body-1"></div>
</swc:dialogPanel>
  <s:form namespace="/order" method ="post"  name="downloadForm" id="downloadForm" target="">
       <s:hidden id="orderHeaderKey" name="orderHeaderKey" value='%{#orderDetail.getAttribute("OrderHeaderKey")}'/>
       <s:hidden id="draft" name="draft" value="%{#draftOrderFlag}"/>
  </s:form>
  
<s:if test='#isOrderOnApprovalHold == true'>
<swc:dialogPanel title="${ApprovalHistoryTitle}" isModal="true" id="approvalHistory">
					<div class="padding-left3" id="dialog-body-id">
						<div id="historyTable">
						<s:set name="orderholdtypelog" value="%{holdTypeLogElem}"/>
	<s:set name="iter" value="#xutil.getChildren(#orderholdtypelog, 'OrderHoldTypeLog')"/>
                  <s:action name="buildSimpleTable" executeResult="true" namespace="/common" >
                    <s:param name="id" value="approvalListTable"/>
                    <s:param name="cssClass" value="'approvalHistoryTable'"/>
                    <s:param name="summary" value="'Approval History Table'"/>
                    <s:param name="iterable" value="#iter"/>
                  <s:param name="columnSpecs[0].label" value="'approvalUser'"/>
                  <s:param name="columnSpecs[0].dataField" value="'UserId'"/>
                  <s:param name="columnSpecs[0].sortable" value="'false'"/>
                  <s:param name="columnSpecs[0].fieldCssClass" value="'nameCell'"/>
                  <s:param name="columnSpecs[0].columnId" value="'UserHeader'"/>
                  <s:param name="columnSpecs[1].label" value="'approved/rejected'"/>
                  <s:param name="columnSpecs[1].dataField" value="'StatusDescription'"/>
                  <s:param name="columnSpecs[1].sortable" value="'false'"/>
                  <s:param name="columnSpecs[1].fieldCssClass" value="'nameCell'"/>
                  <s:param name="columnSpecs[1].columnId" value="'statusDescriptionHeader'"/>
                  <s:param name="columnSpecs[2].label" value="'date.Approval'"/>
                  <s:param name="columnSpecs[2].dataField" value="'Createts'"/>
                  <s:param name="columnSpecs[2].sortable" value="'false'"/>
                  <s:param name="columnSpecs[2].fieldCssClass" value="'dateCell'"/>
                  <s:param name="columnSpecs[2].columnId" value="'CreatetsIdHeader'"/>
                  <s:param name="columnSpecs[2].dataCellBuilder" value="'simpleTableDate'"/>
				  <s:param name="columnSpecs[3].label" value="'notes'"/>
				  <s:param name="columnSpecs[3].dataField" value="'ReasonText'"/>
                  <s:param name="columnSpecs[3].sortable" value="'false'"/>
                  <s:param name="columnSpecs[3].fieldCssClass" value="'nameCell'"/>
                  <s:param name="columnSpecs[3].columnId" value="'ReasonTextHeader'"/>
                  </s:action>
						</div>
					</div>
	  </swc:dialogPanel>
	  </s:if>
	  
<swc:dialogPanel title="${HoldsPopupTitle}" isModal="true" id="holdTable">
					<div class="padding-left3" id="dialog-body-id">
						<div id="orderHoldTable">
						<table class="listTableHeader2 panelHeaderInHoldPopup" id="listTableHeader1">
						  <tr>
						  <tr>
						  	<td><span class="listTableHeaderText"><s:text name="Order_holds_header"/></span></td>
						  </tr>
						</table>
						<s:set name="orderholdtypes" value="%{holdTypesElem}"/>
					<s:set name="iter" value="#xutil.getChildren(#orderholdtypes, 'OrderHoldType')"/>
                  <s:action name="buildSimpleTable" executeResult="true" namespace="/common" >
                    <s:param name="id" value="approvalListTable"/>
                    <s:param name="cssClass" value="'approvalHistoryTable'"/>
                    <s:param name="summary" value="'Approval History Table'"/>
                    <s:param name="iterable" value="#iter"/>
                  <s:param name="columnSpecs[0].label" value="'hold_table_Hold_type_label'"/>
                  <s:param name="columnSpecs[0].dataField" value="'HoldType'"/>
                  <s:param name="columnSpecs[0].dataCellBuilder" value="'holdTypeDescAnchor'"/>
                  <s:param name="columnSpecs[0].dataCellBuilderProperties['holdTypeMap']" value="#holdTypeMap"/>
				  <s:param name="columnSpecs[0].dataCellBuilderProperties['namespace']" value="'/order'"/>
                  <s:param name="columnSpecs[0].sortable" value="'false'"/>
                  <s:param name="columnSpecs[0].fieldCssClass" value="'nameCell'"/>
                  <s:param name="columnSpecs[0].columnId" value="'HoldTypeHeader'"/>
                  <s:param name="columnSpecs[1].label" value="'hold_table_Status_label'"/>
                  <s:param name="columnSpecs[1].dataField" value="'StatusDescription'"/>
                  <s:param name="columnSpecs[1].sortable" value="'false'"/>
                  <s:param name="columnSpecs[1].columnId" value="'statusDescriptionHeader'"/>
                  <s:param name="columnSpecs[1].fieldCssClass" value="'nameCell'"/>
                  </s:action>
						</div>
						<div id="orderLineHoldTable">
						<table class="listTableHeader2 panelHeaderInHoldPopup" id="listTableHeader1">
						  <tr>
						  <tr>
						  	<td><span class="listTableHeaderText"><s:text name="Order_Line_holds_header"/></span></td>
						  </tr>
						</table>
						<s:set name="orderLineholdtypes" value="%{#_action.getLineHoldTypesElem()}"/>
						<s:set name="iter1" value="#xutil.getChildren(#orderLineholdtypes, 'OrderHoldType')"/>
		                  <s:action name="buildSimpleTable" executeResult="true" namespace="/common" >
		                    <s:param name="id" value="orderLineHoldListTable"/>
		                    <s:param name="cssClass" value="'approvalHistoryTable'"/>
		                    <s:param name="summary" value="'Approval History Table'"/>
		                    <s:param name="iterable" value="#iter1"/>
		                  <s:param name="columnSpecs[0].label" value="'hold_table_Hold_type_label'"/>
		                  <s:param name="columnSpecs[0].dataField" value="'HoldType'"/>
		                  <s:param name="columnSpecs[0].dataCellBuilder" value="'holdTypeDescAnchor'"/>
                  		  <s:param name="columnSpecs[0].dataCellBuilderProperties['holdTypeMap']" value="#holdTypeMap"/>
						  <s:param name="columnSpecs[0].dataCellBuilderProperties['namespace']" value="'/order'"/>
		                  <s:param name="columnSpecs[0].sortable" value="'false'"/>
		                  <s:param name="columnSpecs[0].columnId" value="'HoldTypeHeader'"/>
		                  <s:param name="columnSpecs[0].fieldCssClass" value="'nameCell'"/>
		                  <s:param name="columnSpecs[1].label" value="'hold_table_Status_label'"/>
		                  <s:param name="columnSpecs[1].dataField" value="'StatusDescription'"/>
		                  <s:param name="columnSpecs[1].sortable" value="'false'"/>
		                  <s:param name="columnSpecs[1].columnId" value="'statusDescriptionHeader'"/>
		                  <s:param name="columnSpecs[1].fieldCssClass" value="'nameCell'"/>
		                  <s:param name="columnSpecs[2].label" value="'hold_table_Item_Desc_label'"/>
		                  <s:param name="columnSpecs[2].dataField" value="'ItemDescription'"/>
		                  <s:param name="columnSpecs[2].sortable" value="'false'"/>
		                  <s:param name="columnSpecs[2].columnId" value="'ItemDescriptionHeader'"/>
		                  <s:param name="columnSpecs[2].fieldCssClass" value="'nameCell'"/>
		                  </s:action>
					</div>
						<br/>
					<div>&nbsp;</div>
					</div>
	  </swc:dialogPanel>

<!-- div for order cancel light box -->
<swc:dialogPanel title='' isModal="true" id="cancelDialog">
         <s:form id="CancelForm" name="CancelForm" action="xpedxOrderCancel">
         <s:hidden name="orderHeaderKey" value="%{dorderHeaderKey}" />
         <!--Changed for  Jira 2039  -->
         <input type="hidden" name="orderListReturnUrl" value="<s:property value='%{orderListReturnUrl}' />" />
		
 		<s:hidden name="orderNo" value='%{#dorderNo}' />   
         <s:hidden name="orderType" value='%{#xutil.getAttribute(#orderDetail,"OrderType")}' />
         <s:hidden name="sellerOrganizationCode" value='%{#denterpriseCode}' />
         <s:hidden name="webConfirmationNumber" value='%{#webConfirmationNumber}' />
         <s:hidden name="xpedxLegacyOrderNumber" value='%{#xpedxLegacyOrderNumber}' />
         
         <s:iterator value="#_action.getMajorLineElements()" id='orderLine'>
         
         <s:set name='item' value='#xutil.getChildElement(#orderLine, "Item")'/>
         <s:set name='xpedxOrderLineExtn' value='#xutil.getChildElement(#orderLine,"Extn")'/>
              
               
         <s:hidden name="enteredItemIDs" value='%{#item.getAttribute("ItemID")}' />	
		 <s:hidden name="enteredOrderLineKeys" value='%{#xutil.getAttribute(#orderLine,"OrderLineKey")}' />
		 <s:hidden name="enteredPrimeLineNumber" value='%{#xutil.getAttribute(#orderLine,"PrimeLineNo")}' />
		 <s:hidden name='enteredSubLineNumber' value='%{#xutil.getAttribute(#orderLine,"SubLineNo")}' />
		 <s:hidden name="enteredLegacyLineNumbers" value='%{#xutil.getAttribute(#xpedxOrderLineExtn,"ExtnLegacyLineNumber")}' />
		 <s:hidden name="enteredWebLineNumbers" value='%{#xutil.getAttribute(#xpedxOrderLineExtn,"ExtnWebLineNumber")}'/>
		 	  
		</s:iterator>
		
        	<span class="padding-left4 textAlignLeft">
         		<s:text name='Order_Cancel_Confirmation_Message' >
			<!--Commented for  Jira 2039  -->         		
				<s:param><s:property value='@com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils@getFormattedOrderNumber(#OrderExtn)'/></s:param>		
				</s:text>
         	</span>
         	<div class="clearBoth"></div>
         	<div id="deleteButtonPanelId" class="padding-all1 textAlignCenter">
                 <s:submit type="button" id="Confirm_Yes" key="Confirm_Yes" cssClass="submitBtnBg1"  onclick="javascript:DialogPanel.hide('cancelDialog')" tabindex="3100"/>
                 <s:submit type="button" id="Confirm_No" key="Confirm_No" cssClass="submitBtnBg1"  onclick="javascript:DialogPanel.hide('cancelDialog');return false" tabindex="3101"/>
         	</div>
        </s:form>
</swc:dialogPanel>
<s:include value='XPEDXOrderLineTotalAdjustments.jsp' />
<swc:dialogPanel title='' isModal="true" id="adjustmentsLightBox">
	<div class="adjustment-body"></div>
</swc:dialogPanel>

<s:if test ="#_action.approvalAllowed()" >
	<swc:dialogPanel title="Approval/Rejection Notes" isModal="true" id="approvalNotesPanel"> 
			
		<div  class="xpedx-light-box" id="" style="width:400px; height:300px;">
			<h2>Approval / Rejection Comments</h2>
<!-- 			<p>Enter comments or instructions for the order owner:</p> -->
			<s:form id="approval" name="approval" action="approvalAction" namespace="/order" validate="true" method="post">
<%-- 				<span><s:text name="Approval/Rejection.Notes"/></span> --%>
				<s:textarea id="ReasonText1" name="ReasonText1" cols="69" rows="5" theme="simple"></s:textarea>
				<s:hidden name="ReasonText" id="ReasonText" value="" />				
				<s:hidden name="OrderHeaderKey" value="" />
				<s:hidden name="ApprovalAction" value=""/>
				<s:hidden name="ApprovalActionRequestUrl" value="orderDetail"/>
				<s:hidden name="#action.namespace" value="/order"/>
				<s:hidden id="actionName" name="#action.name" value="approval"/>
				<s:hidden id="orderListReturnUrl" name="orderListReturnUrl" value="%{orderListReturnUrl}" />
				
				<s:hidden id="returnWebConfUrl" name="returnWebConfUrl" value="true" />
				<ul id="tool-bar" class="tool-bar-bottom">
					<li><a style="float:right;" class="grey-ui-btn" href="#" onclick="javascript:DialogPanel.hide('approvalNotesPanel');"><span>Cancel</span></a></li>
					<li><a style="float:right;" class="grey-ui-btn" href="#" onclick="javascript:openNotePanelSetAction('Reject','<s:property value="%{dorderHeaderKey}" />');"><span>Reject</span></a></li>
					<li><a style="float:right;" class="green-ui-btn" href="#" onclick="javascript:openNotePanelSetAction('Accept','<s:property value="%{dorderHeaderKey}" />');"><span>Approve</span></a></li>									
				</ul>
			</s:form>
		</div>
	 </swc:dialogPanel>   
</s:if>     
<!-- end container  -->
<script>
function submitApproval()
{
	var form = Ext.get("approval");
	addCSRFToken(form.dom, 'form');
	form.dom.submit();
}
function displayLineAdjustments(panelId, lineKey) {
	var d = Ext.DomQuery.selectNode(".lineAdj_"+lineKey);
	var e = Ext.DomQuery.selectNode(".adjustment-body");
	e.innerHTML = d.innerHTML;
	svg_classhandlers_decoratePage();
	DialogPanel.show(panelId);
	}
</script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/order<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/approval<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/email<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/orderAdjustment<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jQuery<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery-ui.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.shorten<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery-ui.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/sorttable<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.cycle.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
</body>
</html>