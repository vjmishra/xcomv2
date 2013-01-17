	<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>

<s:bean  name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs?. 
    This is to avoid a defect in Struts that?s creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts? OGNL statements. --%>
<s:set name='_action' value='[0]' />

<html>
<head>
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<!-- Webtrends Tag starts -->
<meta name="WT.si_n" content="ShoppingCart" />
<meta name="WT.si_x" content="3" />
<%
  		request.setAttribute("isMergedCSSJS","true");
  	  %>
<!-- Webtrends Tag ends -->

<s:set name="shipComplY" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SHIP_COMPLETE_Y"/>
<s:set name="shipComplC" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SHIP_COMPLETE_C"/>
<s:set name="shipComplN" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SHIP_COMPLETE_N"/>
<%--
<s:set name="currentShipTo" value="#wcUtil.getShipToAdress(getWCContext().getCustomerId(),getWCContext().getStorefrontId())" />
<%--for jira 3438 - sales rep emailID display --%>
<s:set name="isSalesRep" value ="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute('IS_SALES_REP')}"/>

<!-- begin styles. These should be the only three styles. -->

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/ORDERS<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<![endif]-->
<!--  End Styles -->
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-jquery-headder<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>


<script type="text/javascript">

function processDetail(itemid, uom) {
	<s:url id='detailURL' namespace='/catalog' action='itemDetails.action'>	
     <s:param name='_r_url_' value='%{orderDetailsURL}'/>
	</s:url>
	//alert("<s:property value='%{detailURL}'/>");
	// Begin - Changes made by Mitesh Parikh for 2422 JIRA
	<s:set name="itemDtlBackPageURL" value="%{itemDtlBackPageURL}" scope="session"/>
	// End - Changes made by Mitesh Parikh for 2422 JIRA
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

function setShipComplete(){
	var shipComplete = document.getElementById("ShipComplete");
	if(shipComplete!=null)
	{
		if(shipComplete.checked ==  true)
			document.getElementById("draftShipComplete").value = '<s:property value="#shipComplC"/>';
		else
			document.getElementById("draftShipComplete").value = '<s:property value="#shipComplY"/>';
	}
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

	function setCustomerPONumber()
	{
		var po_comboObj=document.getElementById("po_combo_input");
		if(po_comboObj != null )
		{
			var newPoNumberObj=document.getElementById("newPoNumber");
			newPoNumberObj.value = po_comboObj.value;
			
		}
		
	}
	

	
	function validateCustomerPO()
	{
		
		var errordiv=document.getElementById("requiredCustomerPOErrorDiv");
		errordiv.style.display="none";
		var po_comboObj=document.getElementById("po_combo_input");
		if(po_comboObj.value.trim().length == 0)
		{			
			errordiv.style.display="block";
			po_comboObj.style.borderColor="#FF0000";
			return false;
		}
		else
		{
			//setCustomerPONumber();validateForm_OrderSummaryForm;submitOrder();
			validateFormSubmit();
			return false; //changed by bb6
		}
	}
	
	
	function validateFormSubmit(){
		//Added For Jira 3232
	    //Commented for 3475
		//Ext.Msg.wait("Processing...");
		
		setCustomerPONumber();
	var deliveryHoldCheck = '<s:property value="%{#_action.isDeliveryHold()}"/>';
	var deliveryHoldFlag = document.getElementById("DeliveryHoldFlag");
    var OrderSummaryForm_rushOrdrDateFlagField =   document.getElementById("OrderSummaryForm_rushOrdrDateFlag");
	//Special Instructions field validation
	var OrderSummaryForm_rushOrdrFlagField =   document.getElementById("OrderSummaryForm_rushOrdrFlag"); 	
	var splInstructionsField = document.getElementById("OrderSummaryForm_SpecialInstructions");
		
    //var usernameField = document.forgotPwdForm.UserId;
    var errorDiv = document.getElementById("errorMsg");
    var returnval = false;

    if(deliveryHoldCheck == "true" && (deliveryHoldFlag.checked == false) ){
    	document.getElementById("customerHoldCheck").value = "true";
    }
    errorDiv.innerHTML = "";
    splInstructionsField.style.borderColor="";
    errorDiv.style.display = "none";

    
    
    if(splInstructionsField.value.trim().length == 0 && (OrderSummaryForm_rushOrdrFlagField.checked == true) )
    {
    	errorDiv.innerHTML = "Rush Order delivery information is required. Please enter in the Comments field.";
        splInstructionsField.style.borderColor="#FF0000";
        errorDiv.style.display = 'inline';
        return returnval;
    }
    else if (splInstructionsField.value.trim().length == 0 && (OrderSummaryForm_rushOrdrDateFlagField.checked  == true ) )
       {
    	
    	errorDiv.innerHTML = "Requested delivery date information is required. Please enter in the Comments field.";
        splInstructionsField.style.borderColor="#FF0000";
        errorDiv.style.display = 'inline';
        return returnval;	
       }
    else{
    	writewebtrendTagForQty();
    	//Added for 3475
    	Ext.Msg.wait("Processing...");
    	validateForm_OrderSummaryForm(),submitOrder()
   // document.OrderSummaryForm.submit();
    returnval = true;
    }
    return returnval;
}

	function writewebtrendTagForQty(){
		var val = document.getElementById('webtrend_allqty').value;
		writeMetaTag("WT.tx_u",val);

	}
</script>
<title><s:property value="wCContext.storefrontId" /> - <s:text name="MSG.SWC.ORDR.ORDRSUMMARY.GENERIC.TABTITLE" /></title>



<!-- BEGIN head-calls.php -->


<!-- sterling 9.0 base  do not edit  javascript move all functions to js/global-xpedx-functions.js -->
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel.js"></script>
-->
<link type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/FlexBox/css/jquery.flexbox<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<!-- carousel scripts js   -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/FlexBox/js/jquery.flexbox<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.tabs.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.shorten.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/modals/checkboxtree/jquery.checkboxtree.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/DD_roundies_0.0.2a-min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pseudofocus.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global-xpedx-functions.js"></script>
<!-- Facy Box (Lightbox/Modal Window -->
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>-->

<!-- Page Calls -->

<!-- END head-calls.php -->
<!-- <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.datepicker.js"></script> -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/email<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/order<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/orderSummary<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/orderAdjustment<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/addCoupon<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript">
// See http://www.fairwaytech.com/flexbox

	
	<s:if test="addnlPoNumberList">
	$().ready(function(){
	$('#po_combo').flexbox({  
					"results": [
					            
				         	<s:iterator value="addnlPoNumberList.values()" status="POValue">
				         	<s:if test='#POValue.last'>
								{ "id": "<s:property/>", "name": "<s:property/>" }
							</s:if>
							<s:else>
								{ "id": "<s:property/>", "name": "<s:property/>" },
							</s:else>
								
							</s:iterator>
						], "total": <s:property value="addnlPoNumberList.size()"/>
			}, {
				paging: { pageSize: 10 }
});
	$("#adjustmentsLightBox").fancybox({
		'titleShow'			: false,
		'transitionIn'		: 'fade',
		'transitionOut'		: 'fade',
		'titlePosition' : 'inside',
		'transitionIn' : 'none',
		'transitionOut' : 'none',
		//added for clearing the copycart name and copycartdescription fields							
	});
	
	
	/* Begin Short desc. shortener */
	  $('.short-description chkout, #short-description chkout').each(function() { 
		var html = $(this).html();
		var shortHTML = html.substring(0, 70); 
		if( html.length > shortHTML.length )
		{
			$(this).html(shortHTML);
			$(this).append('...');	
			$(this).attr('title', html );
		}
	});
	
	/* Begin Long desc. shortener */
	$('.prodlist ul li, #prodlist ul li').each(function() {
		var html = $(this).html();
		var shortHTML = html.substring(0, 40);
		if( html.length > shortHTML.length )
		{
			$(this).html(shortHTML);
			$(this).append('...');	
			$(this).attr('title', html );
		}
	});
	

$('#po_combo_input').attr("maxlength","22");
$('#po_combo_input').attr("name","po_combo_input");
<%--Fix For Jira 4083. Added escape= false --%>
$('#po_combo_input').attr("value","<s:property value='custmerPONumber' escape='false' />");
	});
	</s:if>

		

</script>

<swc:extDateFieldComponentSetup />
</head>
<!-- END swc:head -->

<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean'
	id='util' />
	<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean' id='xpedxutil' />
<s:bean name='com.sterlingcommerce.webchannel.utilities.CommonCodeUtil'
	id='ccUtil' />
<s:bean name='com.yantra.yfc.util.YFCCommon' id='yfcCommon' />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXPriceandAvailabilityUtil'
	id='priceUtil' />
<s:set name='wcContext' value="wCContext" />
<s:set name="shipToCustomer" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("shipToCustomer")' />
<s:set name="currentShipTo" value='#shipToCustomer' />
<s:set name='shipFromDivision' value='%{#shipToCustomer.getExtnShipFromBranch()}' />
<%--
 I don't see this ie getting used in this page . removing for performance issue.
<s:set name='chargeDescriptionMap'
	value='#util.getChargeDescriptionMap(#wcContext)' />
 --%>
<s:set name="xpedxCustomerContactInfoBean" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("XPEDX_Customer_Contact_Info_Bean")' />	
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
	<%--Retrieving Default Currency from context - for JIRA 4183 --%>
<s:set name='currencyCode' value='%{#_action.getWCContext().getDefaultCurrency()}' />
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
<s:set name="orderType" value='%{#xutil.getAttribute(#orderDetails, "OrderType")}' />
<s:set name="myPriceValue" value="%{'false'}" />
<s:set name='storeFrontId'
	value='%{#_action.getWCContext().getStorefrontId()}' />	
<s:set name='shipFromDoc'
	value='#_action.getShipFromDoc()' />	
<s:if test='%{#shipFromDoc == null }'>
<s:set name='shipFromDoc'
	value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getShipFromAddress()' />
</s:if>	

<s:set name='shipFrom'
	value='#util.getElement(#shipFromDoc, "OrganizationList/Organization/ContactPersonInfo ")' />
	
<s:set name='contactId'
	value='%{#_action.getWCContext().getCustomerContactId()}' />
	
<s:set name="isEditOrderHeaderKey" value ="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute(@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@EDITED_ORDER_HEADER_KEY)}"/>
<%--
I don't see UserInfoExtn variable used in this page . So commenting this. If we want to use customer contact extn fields , Please use 
from session . We have customer Contact Object in session .
<s:set name='userInfo1'
	value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUserInfo(#contactId, #storeFrontId)' />
<s:set name='CustomerContact1'
	value='#xutil.getChildElement(#userInfo1,"CustomerContact")' />
<s:set name='UserInfoExtn'
	value='#xutil.getChildElement(#CustomerContact1,"Extn")' />
--%>
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
 <s:set name="editOrderFlag" value='%{#_action.getIsEditOrder()}' />
<s:url includeParams="none" id="orderNotesListURL"
	action="orderNotesList.action">
	<s:param name="OrderHeaderKey" value='#orderHeaderKey' />
	<s:param name="draft" value="#draftOrderFlag" />
</s:url>
<s:if test='%{#editOrderFlag == "true"}'>
		<s:set name="isEditOrder" value="%{'true'}" />
</s:if>
<s:else>
	<s:set name="isEditOrder" value="%{'false'}" />
</s:else>
<s:url id="draftOrderDetailsURL" action="draftOrderDetails"
	escapeAmp="false">
	<s:param name="OrderHeaderKey" value='#orderHeaderKey' />
	<s:param name="draft" value="#draftOrderFlag" />
	<s:param name="isEditOrder" value="#isEditOrder" />
	
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
<%--jira 2885 --%>
<s:set name="pnALineErrorMessage" value="#_action.getPnALineErrorMessage()" />
<s:set name="pnaErrorStatusMsg" value="#_action.getAjaxLineStatusCodeMsg()"/>
<body class="ext-gecko ext-gecko3">
<div id="main-container">
<div id="main">
<s:action name="xpedxHeader" executeResult="true"
	namespace="/common" /> 
	<s:form action="draftOrderSummaryUpdate"
	validate="true" name="OrderSummaryForm" id="OrderSummaryForm"
	namespace="/order" method="POST">
	<s:hidden name='fullBackURL' value='%{#returnURL}' />
	<s:hidden name='customerHoldCheck' value='' id='customerHoldCheck'/>
	<div class="container checkout"><!-- breadcrumb -->
		<div id="breadcrumbs-list-name" class="breadcrumbs-no-float">
		<span class="page-title">Checkout</span>
	<a href="javascript:window.print();"> <span class="print-ico-xpedx underlink" style="margin-top:0px;">
		<img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/print-icon<s:property value='#wcUtil.xpedxBuildKey' />.gif" width="16" height="15" alt="Print Page" />Print Page</span></a>
		</div>
		<!--Commented for 3098  -->
		<!-- <div class="error"  style="float:right; margin-right: 12px;display:none;" id="errorMsg" ></div> -->
		<%--jira 2885 --%>
		<div id="errorMessageDiv"><s:if test='#pnaErrorStatusMsg !=null || pnaErrorStatusMsg != "" '>
				<h5 align="center"><b><font color="red"><s:property value="pnaErrorStatusMsg" /></font></b></h5>
			</s:if>
			<s:if test='%{draftOrderFlagOrderSummary !=null || draftOrderFlagOrderSummary == "true" }'>
				<h5 align="center"><b><font color="red">This cart has already been submitted, please refer to the Order Management page to review the order.</font></b></h5>
			</s:if>
			</div>
		
		<div class="clearall">&nbsp;</div>
				<s:set name='storefrontId' value="wCContext.storefrontId" />
		 
		<s:hidden name="OrderHeaderKey" value='%{#orderHeaderKey}' /> <s:hidden
		name="draft" value="%{#draftOrderFlag}" /> 
		<%-- 
			I don't see this variable used in this jsp so removing it . In case if any one wants to use getUOMDescriptions method please use
			XPEDXWCUtils.getUOMDescription .
			<s:set name="uomMap" value='#util.getUOMDescriptions(#wcContext,true)' />
	    --%>
<!-- 	new ui	 -->
	<s:set name='renderPersonInfo' value='#billTo' />
	<div class="checkout-body" id="checkout-container">
		<fieldset>
			<legend>

				<!-- This is Checkout Page  -->
				<s:if test='%{#editOrderFlag == "true" || #editOrderFlag.contains("true")}'>
					<s:if test='#orderDetails.getAttribute("OrderType") != "Customer" ' > 
        				Order #: <s:property value='@com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils@getFormattedOrderNumber(#extnElem)'/>
        			</s:if>
        			<s:else>
        				Web Confirmation: <s:property value='#extnElem.getAttribute("ExtnWebConfNum")'/>
        			</s:else>
				</s:if>
				<s:else>
					<s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
						My Cart: <s:property value='#orderDetails.getAttribute("OrderName")' />
					</s:if>
					<s:else>
						<s:if test='#orderDetails.getAttribute("OrderType") != "Customer" ' > 
        					<b>Order #: <s:property value='@com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils@getFormattedOrderNumber(#extnElem)'/> </b>
        				</s:if>
        				<s:else>
        					<b>Web Confirmation: <s:property value='#extnElem.getAttribute("ExtnWebConfNum")'/> </b>
        				</s:else>
					 </s:else>
				</s:else>
			</legend>
	            <!--[if IE]>
					<br/>
				<![endif]-->
			<table class="address-block">
				<tr><td><span class="bold">Bill-To: </span> <s:property value='orderBillToID'/></td> </tr>	
				<s:if test="%{#currentShipTo.getBillTo().getOrganizationName()!='' && #currentShipTo.getBillTo().getOrganizationName()!= null}">
				<tr>
					<td width="100%"><s:property value='%{#currentShipTo.getBillTo().getOrganizationName()}' /></td>
				</tr>
				</s:if>	    
				<tr><td><s:property value='%{#wcContext.getWCAttribute("BillToForOrderSummaryPage")}'/></td> </tr>
				<tr>
				<td><s:set name='renderPersonInfo' value='#billTo' /> 
				
						<s:include value="/xpedx/jsp/order/XpedxShipToAddress.jsp" />
						<!-- includes html line breaks have to be changed to td -->
					</td> 
				</tr>
			
			</table>
			<table class="address-block">
			<!-- Webtrends Tag starts -->
     			<script type="text/javascript">
     				var tag = "WT.ti,DCSext.w_x_ord_shov_edit";
					var content = "Change ShipTo,1";
     			</script>
     			<!-- Webtrends Tag ends -->
				<tr>
					<td><span class="bold">Ship-To:</span> <s:property value='orderShipToID'/> 
					<s:if test='#canChangeShipTo && #_action.isDraftOrder()'>
					<!-- Webtrends Tag starts -->
					<a onclick="javascript: writeMetaTag(tag,content,2);" href="#ajax-assignedShipToCustomers" id="shipToSelect2" class="underlink" tabindex="201">[Change]</a></s:if></td></tr>	
					<s:if test="%{#currentShipTo.getOrganizationName()!='' && #currentShipTo.getOrganizationName()!= null}">
					<tr>
						<td width="100%"><s:property value='%{#currentShipTo.getOrganizationName()}' /></td>
					</tr>
					</s:if>
					<s:if test="%{#currentShipTo.LocationID!='' && #currentShipTo.LocationID!= null}">
						<tr><td>Local ID: <s:property value='%{#currentShipTo.locationID}' /></td></tr>
					</s:if>
				<!-- Webtrends Tag Stops -->
				<tr>
					<td><s:property value='%{#wcContext.getWCAttribute("ShipToForOrderSummaryPage")}'/></td> 
				</tr>				    
				<tr>
					<td><s:set name='renderPersonInfo' value='#shipTo' />
					<s:include
						value="/xpedx/jsp/order/XpedxShipToAddress.jsp" />
					</td> 
				</tr>
			</table>
			<table class="shiping-methods">

				<tr><th>Shipping Options</th></tr>
				<s:set name="delHDate"
					value='#extnElem.getAttribute("ExtnDeliveryHoldDate")' />
				<tr><td><s:checkbox cssClass="checkbox"
						name='DeliveryHoldFlag' fieldValue="true" id="DeliveryHoldFlag"
						value="%{#_action.isDeliveryHold()}" /> 
						<%--Changes for JIRA 3413 --%>
						<s:if test="%{deliveryCutOffTime!= null && deliveryCutOffTime!=''}">
						Place Order on Hold, CSR will release at
					&nbsp;<s:property value="deliveryCutOffTime"/>
					</s:if>
					<s:else>
					Place Order on Hold, will not deliver until released.
					</s:else>
					<%--End of Changes for JIRA 3413 --%>
					<s:hidden name='DeliveryHoldTime'
						value="%{#extnElem.getAttribute('ExtnDeliveryHoldTime')}" />
				</td></tr>	   
				
				<s:if test='%{#_action.getShipCompleteOption()== #shipComplY}'>
					<tr><td><s:checkbox cssClass="checkbox" name='ShipComplete' id="ShipComplete" onclick="javascript:setShipComplete()" 
								disabled='%{false}' fieldValue="true" value="%{false}" /> 
								<s:text name="Ship Order Complete (will not ship until all items are available)" />
								<s:hidden name="draftShipComplete" id="draftShipComplete" value="%{#shipComplY}" />
							</td></tr>	
				</s:if>
				<s:elseif test='%{#_action.getShipCompleteOption()== #shipComplC}'>
					<tr><td><s:checkbox name='ShipComplete'  id="ShipComplete" cssClass="checkbox"
								disabled='%{true}' fieldValue="true" value="%{true}" /> 
								<s:text	name="Ship Order Complete (will not ship until all items are available)" />
								<s:hidden name="draftShipComplete" id="draftShipComplete" value="%{#shipComplC}" />
							</td></tr>
				</s:elseif>
				<s:elseif test='%{#_action.getShipCompleteOption()== #shipComplN}'>
					<tr><td>
							</td></tr>
				</s:elseif>
				<s:else>
						<s:if test='%{#_action.getShipComplete()== #shipComplC}'>
						<tr><td><s:checkbox name='ShipComplete'  id="ShipComplete" cssClass="checkbox"
								disabled='%{true}' fieldValue="true" value="%{true}" /> 
								<s:text	name="Ship Order Complete (will not ship until all items are available)" />
								<s:hidden name="draftShipComplete" id="draftShipComplete" value="%{#shipComplC}" />
							</td></tr>
						</s:if>
						<s:elseif test='%{#_action.getShipComplete()== #shipComplY}'>
						<tr><td><s:checkbox cssClass="checkbox" name='ShipComplete' id="ShipComplete" onclick="javascript:setShipComplete()" 
								disabled='%{false}' fieldValue="true" value="%{false}" /> 
								<s:text name="Ship Order Complete (will not ship until all items are available)" />
								<s:hidden name="draftShipComplete" id="draftShipComplete" value="%{#shipComplY}" />
							</td></tr>
						</s:elseif>
						<s:elseif
							test='%{(#_action.getShipCompleteOption()!= #shipComplY && #_action.getShipCompleteOption()!= #shipComplC && #_action.getShipCompleteOption()!= #shipComplN) || #_action.getShipCompleteOption()=="" || #_action.getShipCompleteOption()==null}'>
							<tr><td><s:checkbox name='ShipComplete'  id="ShipComplete" cssClass="checkbox"
									disabled='%{true}' fieldValue="true" value="%{false}" /> <span style="color:#cccccc;"><s:text
									name="Only available quantity will be shipped" /></span>
									<s:hidden name="draftShipComplete" id="draftShipComplete" value="%{#shipComplN}" />
							</td></tr>	
						</s:elseif> 
				</s:else>
				<tr><td><s:checkbox name='rushOrdrFlag' cssClass="checkbox"
						disabled="%{! #_action.isDraftOrder()}" fieldValue="true"
						value="%{#_action.isRushOrderFlag()}" /><s:if
						test='%{#_action.isDraftOrder()}'>
						  Rush Order: Charges may apply. <span class="bold">MUST</span> add delivery info in Comments.						
					</s:if> <s:else>
						  Rush Order: Charges may apply. <span class="bold">MUST</span> add delivery info in Comments.
					</s:else>	  
<!-- 						  Have to confirm and remove the logic for draft order , see if the wording has to be changed -->
				</td></tr>
			
				<tr><td><s:checkbox name='rushOrdrDateFlag' cssClass="checkbox" onclick="if (this.checked) this.form.SpecialInstructions.focus()"
						disabled="%{! #_action.isDraftOrder()}" fieldValue="true"
						value="" /><s:if
						test='%{#_action.isDraftOrder()}'>
						 Requested Delivery Date. <span class="bold">MUST</span> add delivery date in Comments for deliveries outside your normal schedule.						
					</s:if> <s:else>
						 Requested Delivery Date. <span class="bold">MUST</span> add delivery date in Comments for deliveries outside your normal schedule.
					</s:else>	  
<!-- 						  Have to confirm and remove the logic for draft order , see if the wording has to be changed -->
				</td></tr>			
				<tr>
					<td>
						<s:set name='renderPersonInfo' value='#shipFrom' />
						<s:checkbox name='WillCallFlag' cssClass="checkbox"
							disabled="%{! #_action.isDraftOrder()}" fieldValue="true"
							value="%{#_action.isWillCall()}" /> Will Call - Pick up at : <div class="pick-up-location"><s:include
						value="/xpedx/jsp/order/XpedxReadOnlyAddress.jsp" /></div>
					</td>
				</tr>
						</table>
						
						<!-- bb1 -->
						<table id="checkout-po-tbl">
					<td class="first-cell-first-row" valign="middle">
					<%-- 	
						<s:if test="#_action.getIsCustomerPOMandatory() =='true'" >
							<div class="mandatory float-left">*</div>
						</s:if> 
						--%>
						<div id="po_combo">	
							<label>PO #:&nbsp;</label>												
								<input type="hidden" name="newPoNumber" id="newPoNumber" value="<s:property value='newPoNumber' />" />							
						</div>
					</td>
					<td valign="middle" class="second-cell-first-row">
						<span id="attn-text"><s:text name='Attention' />:</span> <s:textfield cssClass="x-input" id='ShipToAttn' name='ShipToAttn' size="25" tabindex="" maxlength="30" value='%{#extnElem.getAttribute("ExtnAttentionName")}' />
					</td>
				</tr>
				<tr>
					<td  class="first-cell-second-row">
						<s:if test='%{#session.PoNumberSaveNeeded != null}'>
						<input id="PoNumberSaveNeeded" type="checkbox" name="PoNumberSaveNeeded" checked="checked"/>
						<s:set name="PoNumberSaveNeeded" value="<s:property value=null />" scope="session"/>	
						</s:if>
						<s:else><input id="PoNumberSaveNeeded" type="checkbox" name="PoNumberSaveNeeded" /></s:else> 
						<span>Add to my PO list</span>
					</td>
				</tr>
						</table>
						<!-- end bb1 -->
			<div class="clearall">&nbsp;</div>

			<s:if test="!#_action.isDraftOrder()">
				<s:set name="orderDate" value='%{#dateUtilBean.formatDate(#orderDetails.getAttribute("OrderDate"),wCContext)}'/>
				<s:set name="sourceType" value='%{#extnElem.getAttribute("ExtnSourceType")}'/>
				<s:set name="sourceValue" value="%{'COM'}" />
				<s:if test="#sourceType = 1">
					<s:set name="sourceValue" value="%{'Web'}" />
				</s:if>
			 		<div class="order-placed-by full-width">Order placed by <s:property value='%{#extnElem.getAttribute("ExtnOrderedByName")}' /> on date <s:property value="orderDate" /> via <s:property value='#sourceValue'/>.</div>
<%-- 			 		Order placed by <s:property value='#orderDetails.getAttribute("Modifyuserid")'/> on <s:property value='#orderDetails.getAttribute("Modifyts")'/> via <s:property value='#orderDetails.getAttribute("EntryType")'/> --%>
			</s:if>
			
		</fieldset>
		
		<!--  second fieldset -->
					<div class="checkout-right">
				<fieldset id="bottom-fieldset">
				<table id="textboxes">
				<tr>
				<td width="245px" class="border-right-gray" valign="top">
						<label class="block-label bold " for="comments " >Comments</label>
						<s:if test="%{#_action.isCouponApplied()}">
							<s:if test="%{#orderType != 'DIRECT_ORDER'}">
								<s:textarea cssClass="x-input" 
									readonly="%{! #_action.isDraftOrder()}" name="SpecialInstructions"
									cols="25" rows="3" onkeyup="restrictTextareaMaxLength(this,'250');" 
									value='%{#_action.getInstructionText()}'></s:textarea>
							</s:if>
							<s:else>
								<s:textarea cssClass="x-input" 
									readonly="%{true}" name="SpecialInstructions"
									cols="25" rows="3" onkeyup="restrictTextareaMaxLength(this,'250');" 
									value='%{#_action.getInstructionText()}'></s:textarea>
							</s:else>
						</s:if>
						<s:else>
							<s:if test="%{#orderType != 'DIRECT_ORDER'}">
								<s:textarea cssClass="x-input" 
									readonly="%{! #_action.isDraftOrder()}" name="SpecialInstructions"
									cols="25" rows="3" onkeyup="restrictTextareaMaxLength(this,'250');" 
									value='%{#instrElem.getAttribute("InstructionText")}'></s:textarea>
							</s:if>
							<s:else>
								<s:textarea cssClass="x-input" 
									readonly="%{true}" name="SpecialInstructions"
									cols="25" rows="3" onkeyup="restrictTextareaMaxLength(this,'250');" 
									value='%{#instrElem.getAttribute("InstructionText")}'></s:textarea>
							</s:else>
						</s:else>
						<s:if test="#_action.getIsCustomerPOMandatory() =='true'" >
						<table>
							<tr>
									<td colspan="1">
										<div class="mandatory" id="requiredCustomerPOErrorDiv-old" style="display:none;">PO #: is required field</div>
									</td>
								</tr>
							</table>
						</s:if>
			</td>
			
			<td width="260px" valign="top" class="second-cell">
						<label class="block-label bold " for="comments " >Email Confirmation</label>
							<s:iterator value="addnlEmailAddrList" id="addtnEmailAddrs">
								<s:set name="emailAddrs" value="key" />
								<div class="float-left margin-top-five">
									<input id="input-prop" type="checkbox" name="AddnlEmailAddrList" value="<s:property value='#emailAddrs'/>"></input> 
									<p class="email-list-prop"><s:property value="#emailAddrs"/></p>
								</div>								
							</s:iterator>
						
			</td>
			<td valign="top">
							<div class="email-confirm-right-legend text-left"><label class="bold " for="comments " >Additional Email Addresses</label> (Comma Separated Values)</div>
							<s:textarea cssClass="font email-textarea" cols="16" rows="3"
									cssStyle="margin-bottom: 5px;" name="newEmailAddr" id="newEmailAddr" readonly="%{! #_action.isDraftOrder()}" onkeyup="restrictTextareaMaxLength(this,'500');" /> 
							<input type="checkbox" class="checkbox" name="EmailAddrSaveNeeded"
									id="emailAddrSaveNeeded"> </input>Add to my email address list
			</td>
			</tr>
			</table>
		</fieldset>

			</div><!-- class="checkout-right" -->
			<div class="clearall"></div>
			
			<!-- end second fieldset  -->
		
		<!-- BEGIN ITEM LIST -->
			<s:set name='tabIndex' value='300' />
			<s:set name="divIdx" value='%{1}'/>
			<%-- <s:set name="subTotal" value='%{0.00}' /> --%>
			<s:hidden name="orderLineKeyForNote" id="orderLineKeyForNote" value="" />
			<!-- <div class="mil-wrap-condensed-container"> -->
			
			<table id="checkout-table-header">

			<td class="center white text-right table-header-bar-left"> My Price (<s:property value='#currencyCode'/>) </td>
			<td class="center white text-right pricing-border table-header-bar-right" style="width:143px;"> Extended Price (<s:property value='#currencyCode'/>)&nbsp;</td>

			</tr>
			</table>  
			<%--jira 3788 --%>  
				<s:set name="isOrderTBD" value="%{0}" />   
				<s:set name='webtrend_orderqty' value=''/>                   
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
				<s:set name='itemExtnEle' value='#util.getElement(#itemDetails, "Extn")' />
				<s:set name='certFlag' value='#itemExtnEle.getAttribute("ExtnCert")' />
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
				<s:set name="jsonKey" value='%{#item.getAttribute("ItemID")+"_"+#orderLine.getAttribute("PrimeLineNo")}' />
				<s:set name="json" value='pnaHoverMap.get(#jsonKey)' />
				
				<s:set name="displayPriceForUoms" value='%{""}' />
				<s:set name="priceInfo" value='priceHoverMap.get(#jsonKey)' />
				<s:if test="%{#priceInfo!=null}" >
					<s:set name="displayPriceForUoms" value='%{#priceInfo.getDisplayPriceForUoms()}' />
				</s:if>
		
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
				<s:set name="isMyPriceZero" value="%{'false'}" />									 			    			
				
				<s:set name="editOrderOrderExtn" value='%{""}' />
				<s:set name="editOrderOrderLineExtn" value='%{""}' />
				<%--<s:if test='%{#editOrderFlag == "true"}'>
					<s:set name="editOrderOrder" value='%{#_action.getEditOrderOrderMap().get(#orderHeaderKey)}' />
					<s:set name="editOrderOrderExtn" value='#util.getElement(#editOrderOrder, "Extn")' />
					<s:set name="editOrderOrderLine" value='%{#_action.getEditOrderOrderLineMap().get(#orderLineKey)}' />
					<s:set name="editOrderOrderLineExtn" value='#util.getElement(#editOrderOrderLine, "Extn")' />
				</s:if>
						--%><!-- begin iterator -->   
		<s:if test='#orderLine_1.last'>    
	   	 	<div class="mil-wrap last">
	    </s:if>
	    <s:else>
	    	<div class="mil-wrap">	    
	    </s:else>
         	<div class="mil-container">               
                <!-- begin description  -->
               <s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
                <div class="mil-desc-wrap">
                     <div id="description-text" class="short-description chkout" > 
	  	  
                    
                        <s:a href="javascript:processDetail('%{#item.getAttribute('ItemID')}', '%{#item.getAttribute('UnitOfMeasure')}')" >
						<s:if test='#item.getAttribute("ItemShortDesc") == ""'>
							<s:property	value='%{#item.getAttribute("ItemDesc")}' />
						</s:if>
						<s:else>
							<s:property	value='%{#item.getAttribute("ItemShortDesc")}' />
						</s:else>
						</s:a>
					</div>
	                <div class="mil-attr-wrap">
	                    <s:a href="javascript:processDetail('%{#item.getAttribute('ItemID')}', '%{#item.getAttribute('UnitOfMeasure')}')" >
							<s:if test='#item.getAttribute("ItemShortDesc") != ""'>
								<ul class="prodlist">
									<s:property	value='%{#item.getAttribute("ItemDesc")}' escape="false" />
								</ul>
							</s:if>						
						</s:a>
	                </div>
				</div>
				</s:if>
				<s:else>
				<div class="mil-desc-wrap">
					<div id="description-text" class="mil-wrap-condensed-desc item-short-desc">
                   	 <span class="short-description_M" >
						<s:if test='#item.getAttribute("ItemShortDesc") == ""'>
							<s:property	value='%{#item.getAttribute("ItemDesc")}' />
						</s:if>
						<s:else>
							<s:property	value='%{#item.getAttribute("ItemShortDesc")}' />
						</s:else>
					</span>
					</div>
	                <div class="mil-attr-wrap">
	                	<s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
							<s:if test='#item.getAttribute("ItemShortDesc") != ""'>
								<ul class="prodlist">
									<s:property	value='%{#item.getAttribute("ItemDesc")}' escape="false" />
								</ul>
							</s:if>
						</s:if>
	                </div>
				</div>
				</s:else>
				<!-- end description -->
                        
				<div class="mil-action-list-wrap no-border-left" style="float:right;">
	            	<%-- 
	            	<table	 width="630" cellspacing="0" cellpadding="0" border="0px solid red" class="mil-config">
	                	<tbody>
	                    	<tr>
								<td class="text-right">
									Qty: 
								</td>
								<td width="157">	
									&nbsp;<s:property value='%{#lineTran.getAttribute("OrderedQty")}' />&nbsp;<s:set name="uom" value='%{#lineTran.getAttribute("TransactionalUOM")}' /> <s:property
										value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#uom)' />
                         		</td>
						 		<td class="text-right" width="147">
				  	  			<s:if test='#orderLine.getAttribute("LineType") =="C"'>
									<s:if test='%{#session.viewPricesFlag == "Y"}'>
						 				TBD
						 			</s:if>
					 			</s:if>
					 			<s:else>
									<s:if test='%{#session.viewPricesFlag == "Y"}'>
									<s:property value='#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, #json.get("UnitPricePerRequestedUOM"))' />
									</s:if>
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
								<s:if test='%{#session.viewPricesFlag == "Y"}'>
									TBD
								</s:if>
								</s:if>
								<s:else>
									<s:if test='%{#session.viewPricesFlag == "Y"}'>
									<s:property value='#util.formatPriceWithCurrencySymbol(wCContext, #currencyCode,#priceUtil.getLineTotal(#lineExtn.getAttribute("ExtnExtendedPrice"),"1","0"))' />
									</s:if>
								</s:else>
								</span></td>
                        	</tr>
                        	<s:if test='#orderLine.getAttribute("LineType") !="C"'>
                        		<tr>
					 				<td class="center">&nbsp;</td>
					 				<td class="center">&nbsp;</td>
		                        	<td class="text-right">
		                        		<s:if test='%{#session.viewPricesFlag == "Y"}'>
										<s:property	value='#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, #json.get("PricingUOMUnitPrice"))' />
										<br/>
										/<s:property	value='#json.get("PricingUOM")' />
										</s:if>
									</td>
									<td class="center">&nbsp;</td>
	    	                    </tr>
					 		</s:if>
					 		<s:else>
					 			<tr><td colspan=4 class="center">&nbsp;</td></tr>
					 		</s:else>
						</tbody>
				 	</table>
				 	--%>
				 	<table	 width="600" cellspacing="0" cellpadding="0" border="0px solid red" class="mil-config">
	                	<tbody>
	                		<s:set name="uom" value='%{#lineTran.getAttribute("TransactionalUOM")}' /> 
	                		<s:if test="#displayPriceForUoms!=null && #displayPriceForUoms.size()>0" >
					 			<s:iterator value='#displayPriceForUoms' id='disUOM' status='disUOMStatus'>
					 				<s:set name="bracketPriceForUOM" value="bracketPrice" />
									<s:set name="bracketUOMDesc" value="bracketUOM" />
									
					 				<s:if test='#webtrend_orderqty == null' >
										<s:set name='webtrend_orderqty' value=''/>
										<s:set name='webtrend_orderqty' value='%{#lineTran.getAttribute("OrderedQty")}' />
									</s:if>
									<s:else>
										<s:set name='webtrend_orderqty' value='%{#webtrend_orderqty + ";" + #lineTran.getAttribute("OrderedQty")}' />
									</s:else>
									
									<s:if test='%{!#disUOMStatus.last}' >
									
									<s:if test='%{#disUOMStatus.first}' >
				                    	<tr>
											<td class="text-right padding-right-0">
												<s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
												Qty: 
												</s:if>
											</td>
											<td width="157">
											<s:set name='orderqty' value='%{#lineTran.getAttribute("OrderedQty")}' />
											<s:set name='orderdqty' value='%{#_action.replaceString(#orderqty, ".00", "")}' />	
											<s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
												&nbsp;<s:property value='#xpedxUtilBean.formatQuantityForCommas( #orderdqty )' />&nbsp;
												<s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#uom)' />
											</s:if>
											</td>
			                         		
									 		<td class="text-right" width="147">
								  	  			<s:if test='#orderLine.getAttribute("LineType") =="C" || #orderLine.getAttribute("LineType") =="M" '>
								  	  				<%--	Using CustomerContactBean object from session
								  	  				<s:if test='%{#session.viewPricesFlag == "Y"}'>
													--%>													
									 			</s:if>
									 			<s:else>
								  	  				<%--	Using CustomerContactBean object from session
								  	  				<s:if test='%{#session.viewPricesFlag == "Y"}'>
													--%>
													<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
													<s:set name="priceWithCurrencyTemp1" value='%{#xpedxutil.formatPriceWithCurrencySymbolWithPrecisionFive(wCContext, #currencyCode, "0")}' />
									 			  		<s:if test="%{#bracketPriceForUOM==#priceWithCurrencyTemp1}">
									 			  			<s:set name="isMyPriceZero" value="%{'true'}" />
									 			    		<s:set name="myPriceValue" value="%{'true'}" />
									 			  			 <span class="red bold"> <s:text name='MSG.SWC.ORDR.ORDR.GENERIC.CALLFORPRICE' /></span>  
												 		</s:if>
												  		<s:else>
															<s:property	value='#bracketPriceForUOM' />
															<br/>
															per&nbsp;<s:property value="#bracketUOMDesc" />
														</s:else>
													</s:if>
													
													<s:hidden name="unitPricePerRequestedUOM" id="unitPricePerRequestedUOM_%{#orderLineKey}" value="%{#json.get('UnitPricePerRequestedUOM')}" /> 
											 		<s:hidden name="unitPrice" id="unitPrice_%{#orderLineKey}" value="%{#json.get('UnitPricePerRequestedUOM')+1}" />
													<s:hidden name="pricingUOMUnitPrice" id="pricingUOMUnitPrice_%{#orderLineKey}" value="%{#json.get('PricingUOMUnitPrice')}" /> 
													<s:hidden name="pricingUOM" id="pricingUOM_%{#orderLineKey}" value="%{#json.get('PricingUOM')}" />
													<s:hidden name="extnPricingUOM" id="extnPricingUOM_%{#orderLineKey}" value="%{#json.get('PricingUOM')}" />
												</s:else>
												
											</td>
			                            	<td class="text-right" width="153"><span class="mil-action-list-wrap-num-span">
			                            		<%--	Using CustomerContactBean object from session
			                            		<s:if test='%{#session.viewPricesFlag == "Y"}'>
			                            		--%>
			                            		<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
				                            		<s:if test='#orderLine.getAttribute("LineType")=="C"'>
				                            			
													</s:if>
													<s:else>
														<%--<s:if test='%{#editOrderFlag == "true"}'>
															<s:property value='#util.formatPriceWithCurrencySymbol(wCContext, #currencyCode,#priceUtil.getLineTotal(#editOrderOrderLineExtn.getAttribute("ExtnExtendedPrice"),"1","0"))' />
														</s:if>
														<s:else>
															--%>	
														<s:set name= 'extendedPrice'  value='#util.formatPriceWithCurrencySymbol(wCContext, #currencyCode,#priceUtil.getLineTotal(#lineExtn.getAttribute("ExtnExtendedPrice"),"1","0"))' />
														<s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
														<s:if test="%{#extendedPrice == #priceWithCurrencyTemp}">	
																<span class="red bold"><s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>
																<s:set name="isOrderTBD" value="%{#isOrderTBD+1}" /> 
														  </s:if>
														  <s:else>
																<s:property value='#util.formatPriceWithCurrencySymbol(wCContext, #currencyCode,#priceUtil.getLineTotal(#lineExtn.getAttribute("ExtnExtendedPrice"),"1","0"))' />
														  </s:else>
													</s:else>
															
												</s:if>
											</span></td>
			                        	</tr>
			                        	<tr>
			                        		<td colspan="5" class="text-left"> 
			                        			<s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>											 	
											 		<s:set name="itemID3" value='#item.getAttribute("ItemID")' />
											    	<s:iterator value="inventoryIndicatorMap" id="inventoryIndicatorMap" status="status" >
											    		<s:property value='#key'/>
														<s:set name="inventoryChk" value="value" />
														<s:set name="itemID1" value="key" />
													 <s:if test='#itemID1 == #itemID3'>
															<%-- <s:if test='%{#inventoryChk !="Y"}'>
																<p id="milltext">Mill / Mfg. Item - Additional charges may apply</p>
															</s:if> --%>
														</s:if>	 
													</s:iterator>
												</s:if>
			                        		</td> <!--  end test -->
			                        	</tr>
			                        </s:if> 
                        	
		                        	<s:else> <!--  not first item -->
		                        			
	                        			<s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
	                        			<tr><td colspan=4 class="center">&nbsp;</td></tr>
			                    	 	<tr>
			                    	 		<td class="center">&nbsp;</td>
						 					<td class="center">&nbsp;</td>
				                        	<td class="text-right">
				                        	<%--	Using CustomerContactBean object from session
				                        	<s:if test='%{#session.viewPricesFlag == "Y"}'>
				                        	--%>
				                        	<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
				                        		<s:if test="%{#isMyPriceZero == 'false'}">
													<s:property	value='#bracketPriceForUOM' />
													<br/>
														per&nbsp;<s:property value="#bracketUOMDesc" />
												</s:if>
											</s:if><%-- View Prices Flag is Y --%>
											</td>
											<td class="center">&nbsp;</td>
		    	                    	</tr>
		    	                    	</s:if>
								 			
						           </s:else>
				              
					 			</s:if>
					 			</s:iterator>
					 		</s:if>
					 		<s:else>
					 			<!-- PnA is not available -->	
					 			
					 				<s:if test='#webtrend_orderqty == null' >
										<s:set name='webtrend_orderqty' value=''/>
										<s:set name='webtrend_orderqty' value='%{#lineTran.getAttribute("OrderedQty")}' />
									</s:if>
									<s:else>
										<s:set name='webtrend_orderqty' value='%{#webtrend_orderqty + ";" + #lineTran.getAttribute("OrderedQty")}' />
									</s:else>
					 			<tr>
											<td class="text-right padding-right-0">
											<s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
												Qty: 
											</s:if>
											</td>
											<td width="157">
											<s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>	
												&nbsp;<s:property value='#xpedxUtilBean.formatQuantityForCommas(#lineTran.getAttribute("OrderedQty"))' />&nbsp;
												<s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#uom)' />
											</s:if>
											</td>
											<td class="text-right" width="147">
											</td>
											<td class="text-right" width="153">
											</td>
								</tr>
					 		</s:else>
					 	<s:hidden name="transactionalQty" value='%{#lineTran.getAttribute("OrderedQty")}' />
						<s:hidden name="transactionalUOM" value='%{#lineTran.getAttribute("TransactionalUOM")}' />
						
						<s:hidden name="uom" id="uom_%{#orderLineKey}" value="%{#uom}" /> 
						<s:hidden name="itemId" id="itemId_%{#orderLineKey}" value='%{#item.getAttribute("ItemID")}' />
						<s:hidden name="orderLineKeyLists" id="orderLineKeyLists_%{#orderLineKey}" value="%{#orderLineKey}" /> 
						
						
							<tr>
								<td>&nbsp;</td> <td>&nbsp;</td> <td>&nbsp;</td> <td>&nbsp;</td>
							</tr>
							<tr>
								<td colspan="2" style="text-align:right;"> 
								<s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>									
									<s:iterator value="inventoryIndicatorMap" id="inventoryIndicatorMap" status="status" >										
										<s:set name="inventoryChk" value="value" />
										<s:set name="itemId" value="key" />
										<s:if test='#item.getAttribute("ItemID") == #itemId'>	
											<s:if test='%{#inventoryChk !="Y"}'>
												<p id="milltext">Mill / Mfg. Item - Additional charges may apply</p>
												<s:hidden name="inventoryInds" id="inventoryInd_%{#orderLineKey}" value='%{#orderLineKey}' />
											</s:if>
										</s:if>	 
									</s:iterator>
								</s:if>
								</td>
								<td></td>
								<td></td>
							</tr>
						
						
						</tbody>
				 	</table>
				 	<div class="mill-discount-text">
				 <%-- 	<s:if test='#orderLine.getAttribute("LineType") != "M"'>
				 	<s:set name="itemID3" value='#item.getAttribute("ItemID")' />
				    	<s:iterator value="inventoryIndicatorMap" id="inventoryIndicatorMap" status="status" >
				    		<s:property value='#key'/>
							<s:set name="inventoryChk" value="value" />
							<s:set name="itemID1" value="key" />
						 <s:if test='#itemID1 == #itemID3'>
								<s:if test='%{#inventoryChk !="Y"}'>
									<p id="milltext">Mill / Mfg. Item - Additional charges may apply</p>
								</s:if>
							</s:if>	 
						</s:iterator>
					</s:if>
					--%>
						<s:include value='XPEDXOrderLineTotalAdjustments.jsp' />
						<script type="text/javascript">
								 Ext.onReady(function(){
				                        	          		 
																			
										new Ext.ToolTip({        
				                    			  	 target: 'tip_${orderLineKey}',
													 anchor: 'left',
													 html:	Ext.DomQuery.selectNode('.lineAdj_${orderLineKey}').innerHTML,													
													 autoHide: true,
													 closable: true
												});
				                    		 Ext.QuickTips.init(); 
				                    		 
				                    		 
				                    	 });
                    	</script>
                    	<%--
						<s:set name='linelineoverallTotals' value='#util.getElement(#orderLine, "LineOverallTotals")'/>
						<s:set name='adjustment' value='#xutil.getDoubleAttribute(#linelineoverallTotals,"DisplayLineAdjustments")' />
						--%>
						
						<s:set name='adjustment' value='%{0.00}' />
						<s:if test='%{#editOrderFlag == "true"}'>
								<s:set name='adjustment' value='#xutil.getDoubleAttribute(#editOrderOrderLineExtn,"ExtnAdjDollarAmt")' />
						</s:if>
						<s:else>
						    <s:set name='adjustment' value='#xutil.getDoubleAttribute(#lineExtn,"ExtnLegOrderLineAdjustments")' />
						</s:else>
						<s:if test='%{#adjustment != 0.00}'>
							<p>A Discount of <a id='tip_<s:property value="#orderLineKey"/>' href="javascript:displayLineAdjustments('adjustmentsLightBox','<s:property value='#orderLineKey'/>')">
							<s:property value='#util.formatPriceWithCurrencySymbol(wCContext,#currencyCode,#adjustment)'/></a> has applied to this line.</p>
						</s:if>
				    	<br/>
			    	</div> 
			    	</div>
			    
				<div class="clearall">&nbsp;</div>
			    
			    <div class="bottom-mil-info">
			    	<div class="float-left brand-info">
			    		<p><s:property value="wCContext.storefrontId" /> <s:property value="#xpedxItemLabel" />: <s:property value='#item.getAttribute("ItemID")' />
			    			<s:if test='#certFlag=="Y"'>
							 	<img border="none"  src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/catalog/green-e-logo_small.png" alt="" style="margin-left:0px; display: inline;"/>
							 </s:if>
			    		</p>			    		
			    		<s:set name="itemID" value='#item.getAttribute("ItemID")' />
			    		<s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M"'>	
				    		<s:if test='skuMap!=null && skuMap.size()>0 && customerSku!=null && customerSku!=""'>
								<s:set name='itemSkuMap' value='%{skuMap.get(#itemID)}'/>
								<s:set name='itemSkuVal' value='%{#itemSkuMap.get(customerSku)}'/>
								
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
									<s:property value='#itemSkuVal' />
								</p>
								
							</s:if>
						</s:if>
			    	</div>			    	
			    	<div class="text-left od-customer-defined-fields">
			    		<table style="FONT-SIZE: 12px; MARGIN-LEFT: 500px;">
			    			<tbody>
				    			<s:set name='tabIndex' value='%{#tabIndex + 1}' />
								<s:iterator value='customerFieldsMap'>
								<s:set name='FieldLabel' value='key' />
								<s:set name='FieldValue' value='value' />
								<s:set name='customLbl' value='%{"Extn" + #FieldLabel}' />
								<tr>
									
                                	<s:if test='(#orderLine.getAttribute("LineType") =="P" || #orderLine.getAttribute("LineType") =="S")'>
		                                <s:if test=' (#FieldLabel == "CustomerPONo") || (#FieldLabel == "CustomerLinePONo") '>
											<s:if test="%{#orderLine.getAttribute(#FieldLabel) != null && #orderLine.getAttribute(#FieldLabel) != ''}">
												<td class=float-right>
												<s:property value="%{#FieldValue}" />:					
												<s:label id="orderLine%{#FieldLabel}_%{#orderLineKey}" name='orderLine%{#FieldLabel}' value="%{#orderLine.getAttribute(#FieldLabel)}"/>
												</td>
												
											</s:if>
										</s:if>
										<s:else>
											<s:if test="%{#lineExtn.getAttribute(#customLbl) != null && #lineExtn.getAttribute(#customLbl) != ''}">
												<td class=float-right>
													<s:property value="%{#FieldValue}" />:												
												<s:label id="orderLine%{#FieldLabel}_%{#orderLineKey}" name='orderLine%{#FieldLabel}' value="%{#lineExtn.getAttribute(#customLbl)}"/>
												</td>
												</s:if>
										</s:else>
									</s:if>
<!-- 									</td> -->
								</tr>
                                </s:iterator>
                            </tbody>
                        </table>
                        </div>
                        

                        <div class="clearall">&nbsp; </div>
			    		<s:if test='#lineNotes.getAttribute("InstructionText") != ""'>
				    		<p style='MARGIN-LEFT: 15px;' class="line-spacing">Special Instructions:</p>
				    		<div style="width:550px; word-wrap: break-word; MARGIN-LEFT: 130px; MARGIN-TOP: -23px; line-height: 130%" class="checkout-special-instructions">
				    			<s:property value='%{#lineNotes.getAttribute("InstructionText")}'/>
				    		</div>				    		
			    		</s:if>						
                        
				</div>

		    	
                        <%--jira 2885 --%>
    				<s:set name="lineStatusCodeMsg" value="#pnALineErrorMessage.get(#itemID)"></s:set>
    				<s:if test='%{#lineStatusCodeMsg != null || #lineStatusCodeMsg != ""}'>
						<div id="mid-col-mil">
							<h5 align="center"><b><font color="red"><s:property value="%{#lineStatusCodeMsg}" /></font></b></h5>
						</div>
					</s:if>
    				<%-- end of it 2885 --%>
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
							
					 </s:else>	
					<%-- 
					 <s:if test='( #json.get("UnitPricePerRequestedUOM") != null && #json.get("UnitPricePerRequestedUOM") != "")'>
										<s:set name="subTotal" value='%{#subTotal+#calculatedLineTotal}' />
					</s:if>
					--%>
			</s:iterator>
			<s:hidden name="webtrend_allqty" id="webtrend_allqty" value="%{#webtrend_orderqty}" />
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
			 <!--Commenting for BR1. Will be required for BR2. SO do not delete
			 <div class="cart-sum-left" style="width:300px">
				<fieldset id="coupon-field" style="width:0em;"	> <legend>Coupon Code</legend>-->
			<!-- 	<s:if test='isCouponEntryAllowed && draftOrder'>
					<s:set name='addCouponStartTabIndex' value='800' />
					<div><s:hidden name='currency'
						value='%{#currencyCode}' /> <s:set name='renderAddCouponError'
						value='couponOperationError' /> <s:set name='orderDetailsElem'
						value='%{#orderDetails}' /> <s:set name='showAddButton'
						value='%{true}' /> <s:url id='deleteUrlId' action="deleteCoupon" />
					<s:url id='addCouponUrlId' action="xpedxaddCoupon" />
					<%-- <b><font color="red"><s:property value="couponOperationError" /></font></b> --%>
					 <div id="msgForCouponCode" style="display: none;  float: left; margin-right: 28px; margin-top: 5px;" class="error" > <s:property value="couponOperationError" /> </div>
			
					</div>
				</s:if><!--
					 <input id='couponID' name='couponID' type="text" /> <a class="grey-ui-btn" style="float: right;" href="javascript:setCustomerPONumber();addCoupon('<s:property value="%{#addCouponUrlId}"/>')"><span>Apply</span></a>
			 
				</fieldset>
			</div>-->
			<!--  Added div for Jira 3465 - Delivery Information on Checkout Screen -->
			<s:set id="deliveryinformation" name="deliveryinformation" value="deliveryInfo" />
			<s:if test='#deliveryinformation != null && #deliveryinformation!="" '>
			<div align="center" >
			<table align="left">
  				<tr>
    			<td class="second-cell" width="400px" style="text-align:left;display:block">
    			<label  class="block-label bold " for="comments ">Delivery Information:</label>
				<s:property value="deliveryInfo"/></td>
   				</tr>
			</table> 
			</div>
			</s:if>
			<!--  Fix end for Jira 3465 - Delivery Information on Checkout Screen -->
			<%-- 
			 <div id="msgForCouponCode" style="display: block;  float: left; margin-right: 28px; margin-top: 5px;" class="error" > <s:property value="couponOperationError" /> </div>
			 --%>
			<%-- <div id="msgForCouponCode" style="display: none;" class="error" > <s:property value="couponOperationError" /> </div> --%>
			
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
			<%--	Using CustomerContactBean object from session
			<s:if test='%{#session.viewPricesFlag == "Y"}'>
			--%>
			<%--jira 3788 --%>
			<s:set name = "setTBD" value="%{false}"/>
			<s:if test="%{#_action.getMajorLineElements().size() == #isOrderTBD}">
				<s:set name = "setTBD" value="%{true}"/>
			</s:if>
			<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
			<div class="cart-sum-right">
			<s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
				<table cellspacing="0"  align="right">
					<s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
					<tr>
						<th>Subtotal:</th>
						<td><%-- <s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#subTotal.toString())' />--%> 
							<%--<s:if test='%{#editOrderFlag == "true"}'>
								<s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#editOrderOrderExtn.getAttribute("ExtnOrderSubTotal"))' />
							</s:if>
							<s:else>
								--%>								
								<s:set name='extnOrderSubTotal' value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#orderExtn.getAttribute("ExtnOrderSubTotal"))' />
 			  		 			<s:if test="%{#extnOrderSubTotal == #priceWithCurrencyTemp || #setTBD == true}">							    			       
									<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>  
                                 </s:if>
                                 <s:else>
											<s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#extnElem.getAttribute("ExtnOrderSubTotal"))' />
								</s:else>
							<%-- </s:else> --%>
							
						</td>
					</tr>

					<tr>
						<th>Order Total Adjustments:</th>
						<td>
						<a class="underlink" href="javascript:displayLightbox('orderTotalAdjustmentLightBox')" id='tip_<s:property value="#orderHeaderKey"/>'>
							<%-- <s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#headerAdjustmentWithoutShipping)' /> --%>
							
							<%--<s:if test='%{#editOrderFlag == "true"}'>
								<s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#editOrderOrderExtn.getAttribute("ExtnTotOrderAdjustments"))' />
							</s:if>
							<s:else>
								--%>
								
							<s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#extnElem.getAttribute("ExtnLegTotOrderAdjustments"))' />
								
							<%-- </s:else> --%>
						</a>
						</td>
					</tr>
					<tr>
						<th>Adjusted Subtotal:</th>
						<td>
							<%-- <s:property	value='#adjustedSubtotalWithoutTaxes' /> --%>
							<%--<s:if test='%{#editOrderFlag == "true"}'>
								<s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#editOrderOrderExtn.getAttribute("ExtnTotOrdValWithoutTaxes"))' />
							</s:if>
							<s:else>
								--%>
								<s:set name='extnTotOrdValWithoutTaxes' value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#orderExtn.getAttribute("ExtnTotOrdValWithoutTaxes"))' />
	 			  				<s:if test="%{#extnTotOrdValWithoutTaxes == #priceWithCurrencyTemp || #setTBD == true}">
									<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>  
                                 </s:if>
                                 <s:else>
												<s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#extnElem.getAttribute("ExtnTotOrdValWithoutTaxes"))' />
								</s:else>													            
							<%-- </s:else> --%>
							
						</td>

					</tr>
					<tr>
						<th>Tax:</th>
						<td class="red bold">
									<s:text name='MSG.SWC.ORDR.OM.INFO.TBD' />
						</td>
					</tr>
					<tr>
						<th>Shipping &amp; Handling:</th>

						<td class="red bold">
									<s:text name='MSG.SWC.ORDR.OM.INFO.TBD' />
						</td>
					</tr>
					<tr class="order-total">
						<th>Order Total (<s:property value='#currencyCode'/>):</th>
						<td><%-- <s:set name="subTotAdjusted" value='%{#priceUtil.getLineTotal(#subTotal,"1",#headerAdjustmentWithoutShipping)}' />
							<s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#subTotAdjusted)' />			
							<s:set name="totalPrice" value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#subTotAdjusted)' /> --%>
							<%--<s:if test='%{#editOrderFlag == "true"}'>
								<s:property value='%{#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#editOrderOrderExtn.getAttribute("ExtnTotalOrderValue"))}' />
							</s:if>
							<s:else>
								--%>
								 <s:set name='extnTotalOrderValue'	value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#orderExtn.getAttribute("ExtnTotalOrderValue"))'/>
		 			  			 <s:if test="%{#extnTotalOrderValue == #priceWithCurrencyTemp || #setTBD == true}">
									<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>  
                                 </s:if>
                                 <s:else>
									<s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#extnElem.getAttribute("ExtnTotalOrderValue"))' />
								</s:else>
							<%-- </s:else> --%>
						</td>
					</tr>
				</table>

			</div>
			</s:if>
			<div class="clearall">&nbsp;</div>
		</div><!-- class="cart-summary" -->
		
		<!--bottom button bar -->
		<div class="bottom-btn-bar">
		<s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
			<a class="grey-ui-btn" id="left" href="#" onclick='window.location="<s:property value="#draftOrderDetailsURL"/>"'><span>Edit Cart</span></a>
		</s:if>
		<s:else><a class="grey-ui-btn" id="left" href="#" onclick='window.location="<s:property value="#draftOrderDetailsURL"/>"'><span>Edit Order</span></a></s:else>	
			<s:if test="#_action.getIsCustomerPOMandatory() =='true'" >
				<%-- <a class="orange-ui-btn" id="right" href="#" onclick='javascript:return validateCustomerPO();'><span>Submit Order</span></a> --%>
				<a class="orange-ui-btn" id="right" href="" onclick='javascript:return validateCustomerPO();'><span>Submit Order</span></a>		
			</s:if>
			<s:else>
<%-- 				 <a class="orange-ui-btn" id="right" href="#" onclick='javascript:validateRushOrderCommentSubmit(),setCustomerPONumber(),validateForm_OrderSummaryForm(),submitOrder()'><span>Submit Order</span></a>  --%>
				     <a class="orange-ui-btn" id="right" href="#" onclick='javascript:validateFormSubmit();'><span>Submit Order</span></a>
			</s:else>	
			
		</div><!--bottom button bar -->
		</div>
		<!-- class="checkout-body" -->
		</div>
		
		<!-- <div class="mandatory" id="requiredCustomerPOErrorDiv" style="display:none;">PO #: is required field</div> -->
		<div id="requiredCustomerPOErrorDiv" style="display: none;  float: right; margin-right: 28px; margin-top: 5px;" class="error" >PO #: is required field</div>
		<!--Added for 3098  -->
		<div class="error"  style="float:right; margin-right: 12px;display:none;" id="errorMsg" ></div><br/>
		<s:if test="%{#isSalesRep}">
			<div class="salesProNotice" style="width:65%; float: right;"> Please ensure the correct Ship-To has been selected for this order prior to submitting the order. To change the Ship-To click on the 'Change' link next to the Ship-To address.</div>
		</s:if>
  
<!-- class="container" -->
		
		
<s:set name='lastModifiedDateString' value="getLastModifiedDateToDisplay()" />
<s:set name='lastModifiedUserId' value="#_action.getLastModifiedUserId()" />
<s:set name='modifiedBy' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getLoginUserName(#lastModifiedUserId)' />
<div class="clearall">&nbsp;</div>
<div class="last-modified-div sc">
     <!--   Last modified by <s:property value="#modifiedBy"/> on <s:property value="#lastModifiedDateString"/>  -->
    
    Last modified by 
    <s:if test="%{#isSalesRep}">
    	<s:property value="#_action.getSalesreploggedInUserName()"/>
    </s:if>
    <s:else>
        <s:property value="#modifiedBy"/> 
    </s:else> on <s:property value="#_action.getLastModifiedDateToDisplay()"/> 
    
</div>

</s:form>

</div>
<!-- id="main" -->
</div>
<!-- id="main-container" -->
<s:form namespace="/order" method="post" name="downloadForm"
	id="downloadForm" target="">
	<s:hidden id="orderHeaderKey" name="orderHeaderKey"
		value='%{#orderHeaderKey}' />
	<s:hidden id="draft" name="draft" value="%{#draftOrderFlag}" />
</s:form>
<s:form action="draftOrderDetails" cssClass="" name="OrderDetailsForm"
	id="OrderDetailsForm" namespace="/order" method="POST" validate="true">
</s:form>

<s:url id='updateNotesURLid' namespace='/order'
	action='xpedxOrderSummaryModifyLineNotes' />
<s:a id='updateNotesURL' href='%{#updateNotesURLid}' />
<div style="display: none;">
<div id="adjustmentsLightBox1"  style="width:600px;height:200px;overflow:auto;">
	<div class="adjustment-body" id='adjustment-body'></div>
</div>
</div>
<a href="#adjustmentsLightBox1" id="adjustmentsLightBox" style="display:none"></a>

<div class="hidden-data"><s:url id='submitOrderURLid'
	namespace='/order' action='saveOrderSummary' /> <s:a
	id='submitOrderURL' href='%{#submitOrderURLid}' /> <s:url
	id='updateURLid' namespace='/order' action='draftOrderSummaryUpdate' />
<s:a id='updateURL' href='%{#updateURLid}' /></div>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all.js"></script>-->

<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/swc.js"></script>-->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/XPEDXOrderSummaryCommon<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- Web Trends tag start -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/webtrends/displayWebTag<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- Web Trends tag end  -->
<script>
//setTotalPrice('<s:property value="#totalPrice" />');
</script>
<!-- Footer Start --> 
<s:action name="xpedxFooter" executeResult="true" namespace="/common" /> 
<!-- Footer End --> 
</body>
</html>