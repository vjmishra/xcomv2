<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs’. 
    This is to avoid a defect in Struts that’s creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts’ OGNL statements. --%>
<s:set name='_action' value='[0]' />
<html class="ext-strict" xml:lang="en"
	xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>

<script type="text/javascript">

function processDetail(itemid, uom) {
	<s:url id='detailURL' namespace='/catalog' action='itemDetails.action'>
	</s:url>
	window.location.href="<s:property value='%{detailURL}' escape='false'/>" + "&itemID=" + itemid + "&unitOfMeasure=" + uom;
}

function maxLength(field,maxlimit) {
	if (field.value.length > maxlimit) // if too long...trim it!
	field.value = field.value.substring(0, maxlimit);
	}

String.prototype.trim = function() {
	return this.replace(/^\s+|\s+$/g,"");
}
function clearSelectBox()
{
	document.getElementById("AddnlPoNumberList").selectedIndex = 0;
}

function clearPONumber()
{
	document.getElementById("newPoNumber").value = "";
}
function editNotes(element)
{
	var note = document.getElementById("orderLineNote_" + element);
	var edit = document.getElementById("editNote_" + element);
	var save = document.getElementById("saveNote_" + element);
	note.disabled = false;
	save.style.visibility = "visible";
	edit.style.visibility = "hidden";
}

function addNotes(element)
{	
	var note = document.getElementById("orderLineNote_" + element);
	var add = document.getElementById("addNote_" + element);
	var save = document.getElementById("saveNote_" + element);
	note.style.visibility = "visible";
	save.style.visibility = "visible";
	add.style.visibility = "hidden";
}

function saveNotes(element)
{
	document.getElementById("orderLineKeyForNote").value = element;
	document.OrderSummaryForm.action = document.getElementById('updateNotesURL');
	var arrNotes = new Array();        
	arrNotes = document.getElementsByName("orderLineNote");
	for(var i = 0; i < arrNotes.length; i++)
	{
		arrNotes[i].disabled = false;
		
	}
    document.OrderSummaryForm.submit();
}

function setTotalPrice(val){
// var html='<p class="float-right"><b><label><s:text	name="TotalPrice"> <s:param> '+
// 			val+'</s:param></s:text></label></b> &nbsp;&nbsp;<a href="#" class="p-orange-lnk">View Details</a></p>';
// 	document.getElementById("topTotalPrice").innerHTML=html;
}

</script>
<title><s:property value="wCContext.storefrontId" /> - <s:text name="draftorder.ordersummary.title" /></title>


<!-- BEGIN head-calls.php -->
<!-- styles --> 
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/swc<s:property value='#wcUtil.xpedxBuildKey' />.css" />


<link type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/themes/base/jquery.ui.all<s:property value='#wcUtil.xpedxBuildKey' />.css" rel="stylesheet" />

<s:include value="../common/XPEDXStaticInclude.jsp"/>

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/banner<s:property value='#wcUtil.xpedxBuildKey' />.css"/>
<!--[if lt IE 8]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/ie7.css" />
<![endif]-->

<!--[if IE 8]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/ie.css" />
<![endif]-->

<!-- sterling 9.0 base  do not edit  javascript move all functions to js/global-xpedx-functions.js -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/swc<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- sterling 9.0 base  do not edit  javascript move all functions to js/global-xpedx-functions.js -->
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/shipping-option<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/draft-order-list<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/shopping-cart-detail<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/order-adjustment<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/common/email/email<s:property value='#wcUtil.xpedxBuildKey' />.css" />

<!-- carousel scripts css  -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/skins/xpedx/theme<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/skins/xpedx/skin<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- carousel scripts js   -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.core<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.tabs<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.shorten<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/modals/checkboxtree/demo.css"/> -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/modals/checkboxtree/jquery.checkboxtree<s:property value='#wcUtil.xpedxBuildKey' />.css"/>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/modals/checkboxtree/jquery.checkboxtree<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery-ui.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/DD_roundies_0.0.2a-min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pseudofocus<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global-xpedx-functions<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>


<!-- Facy Box (Lightbox/Modal Window -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
<!-- Page Calls -->

<!-- END head-calls.php -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.datepicker<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/FlexBox/js/jquery.flexbox<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<link type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/FlexBox/css/jquery.flexbox<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<!-- new ui -->

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/my-items<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/shopping-cart<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/checkout<s:property value='#wcUtil.xpedxBuildKey' />.css"/>
<!-- new ui -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/email<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/order<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/orderSummary<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/orderAdjustment<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/addCoupon<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript">
// See http://www.fairwaytech.com/flexbox

	
	<s:if test="addnlPoNumberList">
	$().ready(function(){
	$('#po_combo').flexbox({  
					"results": [
					            
					         <s:iterator value="addnlPoNumberList">
								{ "id": "<s:property/>", "name": "<s:property/>" },  
							</s:iterator>
						], "total": <s:property value="addnlPoNumberList.size()"/>
			}, {
				paging: { pageSize: 10 }
});
	});
	</s:if>

		

</script>
<script type="text/javascript">
	$(document).ready(function() {
		$(document).pngFix();
	});
	$(function() {
		$(".datepicker").datepicker({
			showOn: 'button',
						numberOfMonths: 1,
			buttonImage: '<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/calendar-icon.png',
			buttonImageOnly: true
		});
	});
</script>
<style type="text/css">
table{
font-size:11.7px;
}
.ui-datepicker-trigger {
/* 	margin-left:397px; */
	margin-left:350px;
	margin-top:-26px;
}
</style>
<swc:extDateFieldComponentSetup />
</head>
<!-- END swc:head -->
<body class="ext-gecko ext-gecko3">
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean'
	id='util' />
	<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean' id='xpedxutil' />
<s:bean name='com.sterlingcommerce.webchannel.utilities.CommonCodeUtil'
	id='ccUtil' />
<s:bean name='com.yantra.yfc.util.YFCCommon' id='yfcCommon' />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXPriceandAvailabilityUtil'
	id='priceUtil' />
<s:set name='wcContext' value="wCContext" />
<s:set name='shipFromDivision' value='%{#wcContext.getWCAttribute("SHIP_FROM_BRANCH")}' />
<s:set name='chargeDescriptionMap'
	value='#util.getChargeDescriptionMap(#wcContext)' />
<s:set name='sdoc' value="outputDocument" />
<s:set name="emailDialogTitle" scope="page"
	value="#_action.getText('Email_Title')" />
<s:set name='xutil' value="#_action.getXMLUtils()" />
<s:url id='targetURL' namespace='/common'
	action='xpedxGetAssignedCustomers' />
<s:set name='orderDetails' value='#util.getElement(#sdoc, "Order")' />
<s:set name='orderHeaderKey'
	value='#orderDetails.getAttribute("OrderHeaderKey")' />
<s:set name='draftOrderFlag'
	value='#orderDetails.getAttribute("DraftOrderFlag")' />
<s:set name='soldTo'
	value='#util.getElement(#sdoc, "Order/PersonInfoSoldTo")' />
<s:set name='shipTo'
	value='#util.getElement(#sdoc, "Order/PersonInfoShipTo")' />
<s:set name='billTo'
	value='#util.getElement(#sdoc, "Order/PersonInfoBillTo")' />
<s:set name='priceInfo'
	value='#util.getElement(#sdoc, "Order/PriceInfo")' />
<s:set name='currencyCode' value='#priceInfo.getAttribute("Currency")' />
<s:set name='overallTotals'
	value='#util.getElement(#sdoc, "Order/OverallTotals")' />
<s:set name='orderLines'
	value='#util.getElement(#sdoc, "Order/OrderLines")' />
<s:set name='shipping' value='#util.getElement(#sdoc, "Order/Shipping")' />
<s:set name='hasPendingChanges'
	value='#orderDetails.getAttribute("HasPendingChanges")' />
<s:set name='extnElem' value='#util.getElement(#sdoc, "Order/Extn")' />
<s:set name='canChangeOrderDateAtAllLevel'
	value='#_action.isChangeAllowedAtAllLevels("CHANGE_ORDER_DATE")' />

<s:set name='storeFrontId'
	value='%{#_action.getWCContext().getStorefrontId()}' />	
<s:set name='shipFromDoc'
	value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getShipFromAddress()' />
<s:set name='shipFrom'
	value='#util.getElement(#shipFromDoc, "OrganizationList/Organization/ContactPersonInfo ")' />
	
<s:set name='contactId'
	value='%{#_action.getWCContext().getCustomerContactId()}' />
<s:set name='userInfo1'
	value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUserInfo(#contactId, #storeFrontId)' />
<s:set name='CustomerContact1'
	value='#xutil.getChildElement(#userInfo1,"CustomerContact")' />
<s:set name='UserInfoExtn'
	value='#xutil.getChildElement(#CustomerContact1,"Extn")' />
<s:set name='instrElem'
	value='#util.getElement(#sdoc, "Order/Instructions/Instruction")' />
<s:set name='isOwnerOfNonCartInContextDraftOrder'
	value='#_action.isOwnerOfNonCartInContextDraftOrder()' />
<s:set name='isProcurementInspectMode'
	value='#util.isProcurementInspectMode(wCContext)' />
<s:set name='isReadOnly'
	value='#isOwnerOfNonCartInContextDraftOrder || #isProcurementInspectMode' />

<!-- from draft order  -->
<s:set name="xpedxItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_ITEM_LABEL"/>
	<s:set name="customerItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CUSTOMER_ITEM_LABEL"/>
	<s:set name="manufacturerItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MANUFACTURER_ITEM_LABEL"/>
	<s:set name="mpcItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MPC_ITEM_LABEL"/>

<s:url includeParams="none" id="orderNotesListURL"
	action="orderNotesList.action">
	<s:param name="OrderHeaderKey" value='#orderHeaderKey' />
	<s:param name="draft" value="#draftOrderFlag" />
</s:url>

<s:url id="draftOrderDetailsURL" action="draftOrderDetails"
	escapeAmp="false">
	<s:param name="OrderHeaderKey" value='#orderHeaderKey' />
	<s:param name="draft" value="#draftOrderFlag" />
</s:url>

<s:url id="returnURL" escapeAmp="false" action="OrderSummary">
	<s:param name="orderHeaderKey" value='#orderHeaderKey' />
	<s:param name="draft" value="#draftOrderFlag" />
</s:url>

<s:url id="discardPendingChangesURL" includeParams="none"
	action='ResetPendingOrder' namespace='/order' escapeAmp="false">
	<s:param name="orderHeaderKey" value='%{#orderHeaderKey}' />
</s:url>

<s:url id="soldToURL" action="getOrderSoldToAddress">
	<s:param name="OrderHeaderKey" value='#orderHeaderKey' />
	<s:param name="draft" value="#draftOrderFlag" />
	<s:param name="returnURL" value="#returnURL" />
</s:url>

<s:url id="shipToURL" action="getOrderShipToAddress">
	<s:param name="OrderHeaderKey" value='#orderHeaderKey' />
	<s:param name="draft" value="#draftOrderFlag" />
	<s:param name="returnURL" value="#returnURL" />
</s:url>

<s:url id="shippingOptionURL" action="shippingOption">
	<s:param name="OrderHeaderKey" value='#orderHeaderKey' />
	<s:param name="draft" value="#draftOrderFlag" />
	<s:param name="returnURL" value="#returnURL" />
</s:url>

<s:url id="billToURL" action="getOrderBillToAddress">
	<s:param name="OrderHeaderKey" value='#orderHeaderKey' />
	<s:param name="draft" value="#draftOrderFlag" />
	<s:param name="returnURL" value="#returnURL" />
</s:url>

<s:url id="paymentURL" action="OrderPayment">
	<s:param name="orderHeaderKey" value='#orderHeaderKey' />
	<s:param name="draft" value="#draftOrderFlag" />
	<s:param name="returnURL" value="#returnURL" />
</s:url>

<s:url id="lineLevelShippingURL" action="lineLevelShippingInfo">
	<s:param name="OrderHeaderKey" value='#orderHeaderKey' />
	<s:param name="draft" value="#draftOrderFlag" />
	<s:param name="returnURL" value="#returnURL" />
</s:url>

<s:url id="urlEmail" includeParams="none" escapeAmp="false"
	action='emailOrder' namespace='/order'>
	<s:param name="messageType" value='%{"ComposeMail"}' />
	<s:param name="orderHeaderKey" value='%{#orderHeaderKey}' />
	<s:param name="draft" value="#draftOrderFlag" />
</s:url>
<s:url id="urlPrint" includeParams="none" escapeAmp="false"
	action='PrintCartDetail.action' namespace='/order' />
<!--s:include value = 'orderTotalAdjustments.jsp'/-->

<s:set name='canAddLine'
	value='#_action.isOrderModificationAllowed("ADD_LINE")' />
<s:set name='canChangeSoldTo'
	value='#_action.isOrderModificationAllowed("SOLDTO")' />
<s:set name='canChangeOthers'
	value='#_action.isOrderModificationAllowed("OTHERS")' />
<s:set name='canChangeShipTo'
	value='#_action.isOrderModificationAllowed("SHIPTO")' />
<s:set name='canChangeCarrierServiceCode'
	value='#_action.isOrderModificationAllowed("CARRIER_SERVICE_CODE")' />
<s:set name='canChangeShipDate'
	value='#_action.isOrderModificationAllowed("REQ_SHIP_DATE")' />
<s:set name='canChangeOrderDate'
	value='#_action.isOrderModificationAllowed("CHANGE_ORDER_DATE")' />
<s:set name='canChangeBillTo'
	value='#_action.isOrderModificationAllowed("BILLTO")' />
<s:set name='canChangePaymentMethod'
	value='#_action.isOrderModificationAllowed("PAYMENT_METHOD")' />
<s:set name='canAnyLineChangeCarrierServiceCode'
	value='#_action.isOrderLineModificationAllowedOnAnyLine("CARRIER_SERVICE_CODE")' />
<s:set name='canAnyLineChangeShipDate'
	value='#_action.isOrderLineModificationAllowedOnAnyLine("REQ_SHIP_DATE")' />
<s:set name='canAnyLineChangeOthers'
	value='#_action.isOrderLineModificationAllowedOnAnyLine("OTHERS")' />
<s:set name='canEditShippingOption'
	value='#_action.isShippingOptionEditPossible()' />
<s:if test='!#canChangeOthers'>
	<s:set name='poNumberEditability' value='"disabled"' />
</s:if>
<div id="main-container">
<div id="main"><s:action name="xpedxHeader" executeResult="true"
	namespace="/common" /> <s:form action="draftOrderSummaryUpdate"
	validate="true" name="OrderSummaryForm" id="OrderSummaryForm"
	namespace="/order" method="POST">
	<s:hidden name='fullBackURL' value='%{#returnURL}' />
	<div class="container"><!-- breadcrumb -->
	<div id="breadcrumbs-list-name" class="breadcrumbs-no-float">
		<span class="page-title">Checkout</span>
		</div>
	<a href="javascript:window.print();"> <span class="print-ico-xpedx">
		<img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/print-icon<s:property value='#wcUtil.xpedxBuildKey' />.gif" width="16" height="15" alt="Print Page" />Print page</span></a>
		
		<div class="clearall">&nbsp;</div>
		 
		<s:hidden name="OrderHeaderKey" value='%{#orderHeaderKey}' /> <s:hidden
		name="draft" value="%{#draftOrderFlag}" /> <s:set name="uomMap"
		value='#util.getUOMDescriptions(#wcContext,true)' />
<!-- 	new ui	 -->
	<s:set name='renderPersonInfo' value='#billTo' />
	<div class="checkout-body" id="checkout-container">
		<fieldset>
			<legend><s:property value='#orderDetails.getAttribute("OrderName")'/></legend>
			<table class="address-block">
				<tr><td><span class="bold">Bill-To:</span> <s:property value='orderBillToID'/></td> </tr>		    
				<tr><td><s:property value='%{#wcContext.getWCAttribute("BillToForOrderSummaryPage")}'/></td> </tr>

				<tr><td>
				<s:include value="/xpedx/jsp/order/XpedxShipToAddress.jsp" />
<!-- 				includes html line breaks have to be changed to td -->
				</td> 
				<td>
					<br/>
					<br/>
					<table>
						<tr>
							<td><label>PO #:&nbsp;</label></td>
							<td><div id="po_combo"></div></td>
	
						</tr>
						<tr>
							<td></td>
							<td><input type="checkbox" name="chk_po" /> Add to my PO list</td>
						</tr>
					</table>
				</td>
				
				</tr>
			
			</table>
			<table class="address-block">
				<tr><td><span class="bold">Ship-To:</span> <s:property value='orderShipToID'/> <s:if
					test='#canChangeShipTo'>
					<a href="#ajax-assignedShipToCustomers" style="text-decoration:underline;" id="shipToAnchor" tabindex="201">[Change]</a></s:if></td></tr>	
							    
				<tr><td><s:set name='renderPersonInfo' value='#shipTo' />
				<s:include
					value="/xpedx/jsp/order/XpedxShipToAddress.jsp" /></td> </tr>
				<tr><td><s:text name='Attention' />: <s:textfield cssClass="x-input"
					id='ShipToAttn' name='ShipToAttn' size="25" tabindex="" maxlength="30"
					value='%{#extnElem.getAttribute("ExtnAttentionName")}' /></td></tr>
			</table>
			<table class="shiping-methods">

				<tr><th>Shipping Options</th></tr>
				<s:set name="delHDate"
					value='#extnElem.getAttribute("ExtnDeliveryHoldDate")' />
				<tr><td><s:checkbox cssClass="checkbox"
						name='DeliveryHoldFlag' fieldValue="true"
						value="%{#_action.isDeliveryHold()}" /> Place Order on Hold
					until:&nbsp; <s:property value="deliveryCutOffTime"/>
					<s:hidden name='DeliveryHoldTime'
						value="%{#extnElem.getAttribute('ExtnDeliveryHoldTime')}" />
				</td></tr>		
				<s:if
					test='%{(#_action.getShipCompleteOption()!="Y" && #_action.getShipCompleteOption()!="C") || #_action.getShipCompleteOption()=="" || #_action.getShipCompleteOption()==null}'>
					<tr><td><s:checkbox name='ShipComplete' cssClass="checkbox"
							disabled='%{true}' fieldValue="true" value="%{false}" /> <s:text
							name="Only available quantity will be shipped" />
					</td></tr>	
				</s:if> <s:else>
					<tr>
						<s:if test='%{#_action.getShipCompleteOption()=="C"}'>
							<td><s:checkbox name='ShipComplete' cssClass="checkbox"
								disabled='%{true}' fieldValue="true" value="%{true}" /> <s:text
								name="Ship Order Complete (will not ship until all items are available)" />
							</td>
						</s:if>
						<s:elseif test='%{#_action.getShipCompleteOption()=="Y"}'>
							<td><s:checkbox cssClass="checkbox" name='ShipComplete'
								disabled='%{false}' fieldValue="true"
								value="%{#_action.isShipComplete()}" /> <s:text
								name="Ship Order Complete (will not ship until all items are available)" />
							</td>
						</s:elseif>
					</tr>
				</s:else>
				<tr><td><input
						type="checkbox" class="checkbox"> <b>Rush Order:</b>
					Requested Delivery Date: &nbsp; <s:set name="reqDate"
						value='#orderDetails.getAttribute("ReqDeliveryDate")' /> <s:if
						test='%{#_action.isDraftOrder()}'>
						 <s:textfield name='orderReqDeliveryDate' id='orderReqDeliveryDate'
						cssStyle="margin-left: 0px;" theme="simple" size="15"
						cssClass='x-input datepicker'
						value="%{#util.formatDate(#reqDate, #wcContext)}" />						
					</s:if> <s:else>
					
						 <s:textfield readonly="true" name='orderReqDeliveryDate' id='orderReqDeliveryDate'
						cssStyle="margin-left: 0px;" theme="simple" size="15"
						cssClass='x-input datepicker'
						value="%{#util.formatDate(#reqDate, #wcContext)}" />						
					</s:else> * Additional Charges May Apply
				</td></tr>
			
					<tr><td>
					<s:set name='renderPersonInfo' value='#shipFrom' />
					<s:checkbox name='WillCallFlag' cssClass="checkbox"
						disabled="%{! #_action.isDraftOrder()}" fieldValue="true"
						value="%{#_action.isWillCall()}" /> Will Call – Pick up at: <div class="pick-up-location"><s:include
					value="/xpedx/jsp/order/XpedxReadOnlyAddress.jsp" />  (Allow <s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getLeadTimeOfDivision(#shipFromDivision)' />  hours for processing. A Customer
					Service Representative will contact you.)</div>
				</td></tr>
						</table>
			<div class="clearall">&nbsp;</div>

			<div class="checkout-left">
				<label class="block-label bold " for="comments " >Comments</label>
				<s:textarea cssClass="x-input" 
					readonly="%{! #_action.isDraftOrder()}" name="SpecialInstructions"
					cols="60" rows="3" onkeyup="javascript:maxLength(this,'250');" 
					value='%{#instrElem.getAttribute("InstructionText")}' /></textarea>
	<!-- 			<table>
					<tr>
						<td><label>PO #:&nbsp;</label></td>
						<td><div id="po_combo"></div></td>

					</tr>
					<tr>
						<td></td>
						<td><input type="checkbox" name="chk_po" /> Add to my PO list</td>
					</tr>
				</table> -->
				<div class="clearall">&nbsp;</div>
			</div><!-- class="checkout-left" -->

			<div class="checkout-right">
				<fieldset>
					<legend>Email Confirmation</legend>
					<div class="email-confirm-left">
							<s:iterator value="addnlEmailAddrList" id="addtnEmailAddrs">
								<s:set name="emailAddrs" value="key" />
								<div><input type="checkbox" name="AddnlEmailAddrList" value="<s:property value='#emailAddrs'/>"></input><s:property value="#emailAddrs"/></div>
																	
							</s:iterator>
						
						<br/>
						<div class="email-confirm-right-legend text-left">Additional Email Addresses:</div>
						<s:textarea cssClass="font email-textarea" cols="16" rows="3"
								cssStyle="margin-bottom: 5px;" name="newEmailAddr"
								id="newEmailAddr" readonly="%{! #_action.isDraftOrder()}"
								/> 
						<input type="checkbox" class="checkbox" name="EmailAddrSaveNeeded"
								id="emailAddrSaveNeeded"> Add to my email address list
					</div>
				</fieldset>

			</div><!-- class="checkout-right" -->
			<div class="clearall"></div>
			<div class="placed-by full-width">Order placed by &lt;user name&gt; on &lt;date&gt; via &lt;source&gt;.</div>
		</fieldset>
		<!-- BEGIN ITEM LIST -->
			<s:set name='tabIndex' value='300' />
			<s:set name="divIdx" value='%{1}'/>
			<s:set name="subTotal" value='%{0.00}' />
			<s:hidden name="orderLineKeyForNote" id="orderLineKeyForNote" value="" />
			<!-- <div class="mil-wrap-condensed-container"> -->
			<div class="mil-top-border">
			<table style="float:right; margin-right:1px;">
			<tr>
			<td class="center white text-right" width="153"> My Price (<s:property value='%{#wcContext.getWCAttribute("CUSTOMER_CURRENCY_CODE")}'/>) </td>
			<td class="center white text-right pricing-border" width="161"> Extended Price (<s:property value='%{#wcContext.getWCAttribute("CUSTOMER_CURRENCY_CODE")}'/>)</td>

			</tr>
			</table>
			</div>                            
				<s:iterator value='majorLineElements' id='orderLine'
				status="orderLine_1">
				<s:set name='lineTotals'
					value='#util.getElement(#orderLine, "LineOverallTotals")' />
				<s:set name='item' value='#util.getElement(#orderLine, "Item")' />
				<s:set name='itemDetails'
					value='#util.getElement(#orderLine, "ItemDetails")' />
				<s:set name='lineTran'
					value='#util.getElement(#orderLine, "OrderLineTranQuantity")' />
				<s:set name='primaryInfo'
					value='#util.getElement(#itemDetails, "PrimaryInformation")' />
				<s:set name='orderLineKey'
					value='#orderLine.getAttribute("OrderLineKey")' />
				<s:set name='kitLines'
					value='#util.getElement(#orderLine, "KitLines")' />
				<s:set name='itemIDUOM'
					value='#_action.getIDUOM(#item.getAttribute("ItemID"), #item.getAttribute("UnitOfMeasure"))' />
				<s:set name='lineNotes'
					value='#util.getElement(#orderLine, "Instructions/Instruction")' />
				<s:set name='imageLocation'
					value='#primaryInfo.getAttribute("ImageLocation")' />
				<s:set name='imageID' value='#primaryInfo.getAttribute("ImageID")' />
				<s:set name='lineExtn' value='#util.getElement(#orderLine, "Extn")' />
				<s:set name="json"
					value='pnaHoverMap.get(#item.getAttribute("ItemID"))' />
				 
				<s:if test='#item.getAttribute("ItemShortDesc") == ""'>
					<s:set name='showDesc' value='#item.getAttribute("ItemDesc")'/>
				</s:if>
				<s:else>
					<s:set name='showDesc' value='#item.getAttribute("ItemShortDesc")'/>
				</s:else>
			
				<s:set name='lineShipTo'
					value='#util.getElement(#orderLine, "PersonInfoShipTo")' />
				<s:set name='deliveryMethod'
					value='#orderLine.getAttribute("DeliveryMethod")' />
				<s:set name='lineCarrierServiceCode'
					value='#orderLine.getAttribute("CarrierServiceCode")' />
				<s:set name='canChangeLineShipTo'
					value='#_action.isOrderLineModificationAllowed(#orderLine, "SHIPTO")' />
				<s:set name='lineExtn' value='#util.getElement(#orderLine, "Extn")' />
						
						<!-- begin iterator -->       
	    <div class="mil-wrap">
         	<div class="mil-container">
                                       
                <!-- begin description  -->
               
                <div class="mil-desc-wrap">
                    <div id="description-text" class="mil-wrap-condensed-desc">
                        <s:a href="javascript:processDetail('%{#item.getAttribute('ItemID')}', '%{#item.getAttribute('UnitOfMeasure')}')" >
						<s:if test='#item.getAttribute("ItemShortDesc") == ""'>
							<s:property	value='%{#item.getAttribute("ItemDesc")}' />
						</s:if>
						<s:else>
							<s:property	value='%{#item.getAttribute("ItemShortDesc")}' />
						</s:else>
						</s:a>
					</div>
					<br />
					<br />
	                <div class="mil-attr-wrap">
						<s:if test='#item.getAttribute("ItemShortDesc") != ""'>
							<ul class="mil-desc-attribute-list">
								<!--expecting description with li tags -->
								<s:property	value='%{#item.getAttribute("ItemDesc")}' />
							</ul>
						</s:if>
	                </div>
				</div>
				
				<!-- end description -->
                        
				<div class="mil-action-list-wrap" style="float:right;">
	            	<table	 width="630" cellspacing="0" cellpadding="0" border="0px solid red" class="mil-config availability-table">
	                	<tbody>
	                    	<tr>
								<td class="text-right" style="padding:0px;">
									Qty: 
								</td>
								<td width="157">	
									<s:property
											value='%{#lineTran.getAttribute("OrderedQty")}' />&nbsp;<s:set name="uom"		value='%{#lineTran.getAttribute("TransactionalUOM")}' /> <s:property
										value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#uom)' />
                         		</td>
						 		<td class="text-right" width="147">
				  	  			<s:if test='#orderLine.getAttribute("LineType") =="C"'>
					 				TBD
					 			</s:if>
					 			<s:else>
									<s:property value='#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, #json.get("UnitPricePerRequestedUOM"))' />
									<s:hidden name="unitPricePerRequestedUOM" id="unitPricePerRequestedUOM_%{#orderLineKey}" value="%{#json.get('UnitPricePerRequestedUOM')}" /> 
							 		<s:hidden name="unitPrice" id="unitPrice_%{#orderLineKey}" value="%{#json.get('UnitPricePerRequestedUOM')+1}" />
									<s:hidden name="pricingUOMUnitPrice" id="pricingUOMUnitPrice_%{#orderLineKey}" value="%{#json.get('PricingUOMUnitPrice')}" /> 
									<s:hidden name="pricingUOM" id="pricingUOM_%{#orderLineKey}" value="%{#json.get('PricingUOM')}" />
									<s:hidden name="extnPricingUOM" id="extnPricingUOM_%{#orderLineKey}" value="%{#json.get('PricingUOM')}" />
								</s:else>
									<s:hidden name="uom" id="uom_%{#orderLineKey}" value="%{#uom}" /> 
									<s:hidden name="itemId" id="itemId_%{#orderLineKey}" value='%{#item.getAttribute("ItemID")}' />
									<s:hidden name="orderLineKeyLists" id="orderLineKeyLists_%{#orderLineKey}" value="%{#orderLineKey}" /> 
								</td>
                            	<td class="text-right" width="147"><span class="mil-action-list-wrap-num-span">
                            	<s:if test='#orderLine.getAttribute("LineType")=="C"'>
									TBD
								</s:if>
								<s:else>
									<s:property value='#util.formatPriceWithCurrencySymbol(wCContext, #currencyCode,#priceUtil.getLineTotal(#json.get("ExtendedPrice"),"1","0"))' />
								</s:else>
								</span></td>
                        	</tr>
                        	<s:if test='#orderLine.getAttribute("LineType") !="C"'>
                        		<tr>
					 				<td class="center">&nbsp;</td>
					 				<td class="center">&nbsp;</td>
		                        	<td class="text-right">
										<s:property	value='#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, #json.get("PricingUOMUnitPrice"))' />
										<br/>
										/<s:property	value='#json.get("PricingUOM")' />
									</td>
									<td class="center">&nbsp;</td>
	    	                    </tr>
					 		</s:if>
					 		<s:else>
					 			<tr><td colspan=4 class="center">&nbsp;</td></tr>
					 		</s:else>
						</tbody>
				 	</table>
				 	<div class="mill-discount-text">
				    	<s:iterator value="inventoryMap" id="inventoryMap" status="status" >
							<s:set name="inventoryChk" value="value" />
							<s:set name="itemId" value="key" />
							<s:if test='#itemID == #itemId'>
								<s:if test='%{#inventoryChk !="Y"}'>
									<p id="milltext">Mill / Mfg. Item - Additional charges may apply</p>
								</s:if>
							</s:if>	
						</s:iterator>
				    	<br/>
			    	</div>
			    	</div>
			    
				<div class="clearall">&nbsp;</div>
			    
			    <div class="bottom-mil-info">
			    	<div class="float-left brand-info">
			    		<p><s:property value="#xpedxItemLabel" /> : <s:property value='#item.getAttribute("ItemID")' /></p>
			    		<s:set name="itemID" value='#item.getAttribute("ItemID")' />
			    		<s:if test='skuMap!=null && customerSku!= ""'>
							<s:set name='itemSkuMap' value='%{skuMap.get(#itemID)}'/>
							<p>
								<s:if test='%{customerSku == "1"}' >
									<s:property value="#customerItemLabel" />:
								</s:if>
								<s:elseif test='%{customerSku == "2"}'>
									<s:property value="#manufacturerItemLabel" />:
								</s:elseif>
								<s:else>
									<s:property value="#mpcItemLabel" />:
								</s:else>
								<s:property value='#itemSkuMap.get(customerSku)' />
							</p>
						</s:if>
			    	</div>
			    	<div class="special-instructions-div">
			    		<s:if test='#lineNotes.getAttribute("InstructionText") != ""'>
			    		<p class="special-instructions-padding">Special Instructions:</p>
			    		<div class="checkout-special-instructions">
			    			<s:property value='%{#lineNotes.getAttribute("InstructionText")}'/>
			    		</div>
			    		</s:if>
					</div>
			    	<div class="cust-defined-fields">
			    		<table>
			    			<tbody>
				    			<s:set name='tabIndex' value='%{#tabIndex + 1}' />
								<s:iterator value='customerFieldsMap'>
								<s:set name='FieldLabel' value='key' />
								<s:set name='FieldValue' value='value' />
								<s:set name='customLbl' value='%{"Extn" + #FieldLabel}' />
								<tr>
                                	<td class="right" colspan="2">
                                		<s:if test=' (#FieldValue == "Line Account#")'>
											<p class="p-black txt-left form-label-top">Job Number:</p>
										</s:if>
										<s:else>
											<p class="p-black txt-left form-label-top"><s:text
													name="%{#FieldValue}" />:</p>
										</s:else>
										<s:property  value="%{#lineExtn.getAttribute(#customLbl)}"/>
										<s:set name='tabIndex' value='%{#tabIndex + 1}' />
									</td>
								</tr>
                                </s:iterator>
                            </tbody>
                        </table>
					</div>
				</div>
                <br/>
		    	<div class="clear"></div>
			</div>
		</div>
	    <!-- end iterator -->
<!-- 	     Following for calculating the subtotal -->
  					  <s:if test='#orderLine.getAttribute("LineType") =="C"'>
							<s:set name="calculatedLineTotal" value='{0}' />
					  </s:if>
					  <s:else>
					  		<s:set name="qty" value='%{#lineTran.getAttribute("OrderedQty")}'/>
							<s:set name="calculatedLineTotal" value='#priceUtil.getLineTotal(#json.get("UnitPricePerRequestedUOM"),#qty,#lineTotals.getAttribute("DisplayLineAdjustments"))' />
							<s:set name="subTotal" value='%{#subTotal+#calculatedLineTotal}' />
					 </s:else>	
					 <s:if test='( #json.get("UnitPricePerRequestedUOM") != null && #json.get("UnitPricePerRequestedUOM") != "")'>
										<s:set name="subTotal" value='%{#subTotal+#calculatedLineTotal}' />
					</s:if>
			</s:iterator>
			<!-- majorLineElements -->

<!-- 	end new ui	 -->
<s:set name="xutil" value="xMLUtils" /> <s:set
		name='subtotalWithoutTaxes'
		value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#overallTotals.getAttribute("SubtotalWithoutTaxes"))' />
	 <s:set name='hdrShippingTotal'
		value='#xutil.getDoubleAttribute(#overallTotals,"HdrShippingTotal")' />
	<s:set name='hdrShippingBaseCharge'
		value='#xutil.getDoubleAttribute(#overallTotals,"HdrShippingBaseCharge")' />
	<s:set name='hdrAdjustmentWithoutShipping'
		value='#xutil.getDoubleAttribute(#overallTotals,"HeaderAdjustmentWithoutShipping")' />

	<s:set name='headerAdjustmentWithoutShipping'
		value='%{#hdrAdjustmentWithoutShipping - #hdrShippingTotal + #hdrShippingBaseCharge}' />
	<s:set name='adjustedSubtotalWithoutTaxes'
		value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,(#overallTotals.getAttribute("AdjustedSubtotalWithoutTaxes") - #hdrShippingTotal + #hdrShippingBaseCharge))' />
	<s:set name='grandTax'
		value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#overallTotals.getAttribute("GrandTax"))' />
	<s:set name='grandTotal'
		value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#overallTotals.getAttribute("GrandTotal"))' />
	<s:set name='shippingCharges'
		value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#overallTotals.getAttribute("HdrShippingTotal"))' />
	<s:set name="shippingAdjCounter" value="false" /> <s:set
		name="allAdjCounter" value="false" /> <s:include
		value="XPEDXOrderTotalAdjustments.jsp" /> <s:include
		value="XPEDXOrderShippingAdjustments.jsp" /> <s:include
		value="XPEDXAllAdjustments.jsp" />
		
	<div class="cart-summary">
			<div class="cart-sum-left">
				<fieldset id="coupon-field"><legend>Coupon Code</legend>
				<s:if test='isCouponEntryAllowed && draftOrder'>
					<s:set name='addCouponStartTabIndex' value='800' />
					<s:hidden name='currency'
						value='%{#currencyCode}' /> <s:set name='renderAddCouponError'
						value='couponOperationError' /> <s:set name='orderDetailsElem'
						value='%{#orderDetails}' /> <s:set name='showAddButton'
						value='%{true}' /> <s:url id='deleteUrlId' action="deleteCoupon" />
					<s:url id='addCouponUrlId' action="addCoupon" />
				</s:if>
					<input name="coupon" type="text" /> <a class="grey-ui-btn" style="float: right;" href="javascript:addCoupon('<s:property value="%{#addCouponUrlId}"/>')"><span>Apply</span></a>
			
				</fieldset>
			</div>
			<div class="cart-sum-right">
				<table cellspacing="0"  align="right">
					<tr>
						<th>Subtotal:</th>
						<td><s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#subTotal.toString())' /></td>
					</tr>

					<tr>
						<th>Order Total Adjustments:</th>
						<td><s:property
			value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#headerAdjustmentWithoutShipping)' /></td>
					</tr>
					<tr>
						<th>Adjusted Subtotal:</th>
						<td><s:property
			value='#adjustedSubtotalWithoutTaxes' /></td>

					</tr>
					<tr>
						<th>Tax:</th>
						<td class="gray"><s:property value='#grandTax' /></td>
					</tr>
					<tr>
						<th>Shipping &amp; Handling:</th>

						<td class="gray"><s:property value='#shippingCharges' /></td>
					</tr>
					<tr class="order-total">
						<th>Order Total (USD):</th>
						<td><s:set name="subTotAdjusted" value='%{#priceUtil.getLineTotal(#subTotal,"1",#headerAdjustmentWithoutShipping)}' />
					<s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#subTotAdjusted)' />			
					<s:set name="totalPrice" value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#subTotAdjusted)' />
		</td>
					</tr>
				</table>

			</div>
			<div class="clearall">&nbsp;</div>
		</div><!-- class="cart-summary" -->
		
		<!--bottom button bar -->
		<div class="bottom-btn-bar">
			<a class="grey-ui-btn" id="left" href="#" onclick='window.location="<s:property value="#draftOrderDetailsURL"/>"'><span>Edit Cart</span></a>
			<a class="orange-ui-btn" id="right" href="#" onclick='javascript:validateForm_OrderSummaryForm(),submitOrder()'><span>Submit Order</span></a>
		</div>

		
	
<!-- 	end of tofix -->
		
	
<!-- 		</div> -->
	</div>
</s:form></div>

</div>
<!-- end main  -->
<div></div>

<!-- end container  -->
<s:form namespace="/order" method="post" name="downloadForm"
	id="downloadForm" target="">
	<s:hidden id="orderHeaderKey" name="orderHeaderKey"
		value='%{#orderHeaderKey}' />
	<s:hidden id="draft" name="draft" value="%{#draftOrderFlag}" />
</s:form>
<s:form action="draftOrderDetails" cssClass="" name="OrderDetailsForm"
	id="OrderDetailsForm" namespace="/order" method="POST" validate="true">
</s:form>

<!-- EB-519 -->
<!--<s:action name="xpedxFooter" executeResult="true" namespace="/common" />-->
<s:include value='payment/OrderPaymentCommon.jsp' />
<s:url id='updateNotesURLid' namespace='/order'
	action='xpedxOrderSummaryModifyLineNotes' />
<s:a id='updateNotesURL' href='%{#updateNotesURLid}' />

<swc:dialogPanel title='' isModal="true" id="adjustmentsLightBox">
	<div class="adjustment-body"></div>
</swc:dialogPanel>

<div class="hidden-data"><s:url id='submitOrderURLid'
	namespace='/order' action='saveOrderSummary' /> <s:a
	id='submitOrderURL' href='%{#submitOrderURLid}' /> <s:url
	id='updateURLid' namespace='/order' action='draftOrderSummaryUpdate' />
<s:a id='updateURL' href='%{#updateURLid}' /></div>
<!-- EB-519 -->
<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
</body>
<script>
setTotalPrice('<s:property value="#totalPrice" />');
</script>
</html>