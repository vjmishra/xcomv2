<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<meta http-equiv="X-UA-Compatible" content="IE=8" /> 
<s:bean	name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils'	id='wcUtil' />


<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<%
  		request.setAttribute("isMergedCSSJS","true");
  	  %>
<s:url id='uomDescriptionURL' namespace="/common" action='getUOMDescription' />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.common.XPEDXSCXmlUtils' id='xpedxSCXmlUtil' />
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean" id="xpedxUtilBean" />
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />


<!-- begin styles. These should be the only three styles. -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/ORDERS<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<![endif]-->
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
<!--  End Styles -->

<!-- javascript -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- carousel scripts js   -->
 <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-jquery-headder<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.numeric<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- STUFF YOU NEED FOR BEAUTYTIPS -->


<!--[if IE]>
<script src="../other_libs/excanvas_r3/excanvas.js" type="text/javascript" charset="utf-8"></script>
<![endif]-->


<s:hidden name="uomDescriptionURL" value='%{#uomDescriptionURL}'/>
<!-- /STUFF -->

<!-- Lightbox/Modal Window -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- Page Calls -->

<script type="text/javascript">
$(document).ready(function () {
    $('#Availability_Hover').bt( {
        ajaxPath : '../tool-tips/cart-availability-hover.html div#tool-tip-content', ajaxError : "There was a problem getting this content. Here's what we know: <em>%error</em>.", fill : '#ebebeb', cssStyles :  {
            color : 'black'
        },
        padding : 20, spikeLength : 10, spikeGirth : 15, cornerRadius : 6, shadow : true, shadowOffsetX : 0, shadowOffsetY : 3, shadowBlur : 3, shadowColor : 'rgba(0,0,0,.4)', shadowOverlap : false, strokeWidth : 1, strokeStyle : '#FFFFFF', noShadowOpts :  {
            strokeStyle : '#969696'
        },
        positions : ['top']
    });

    $("#xpedxDeleteCartDialog1").fancybox({
    	'titleShow'			: false,
    	'transitionIn'		: 'fade',
    	'transitionOut'		: 'fade',
    	'titlePosition' : 'inside',
    	'transitionIn' : 'none',
    	'transitionOut' : 'none',
    	//added for clearing the copycart name and copycartdescription fields
    	'onClosed'	: function(){$("#copyCartName").val('');$("#copyCartDescription").val(''); $("#otherCartActions").val('None'); }
    						
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
});


//Clicking on Redx ChecksHiddenCheckBox then Delete Function wil be called.
function checkHiddenCheckboxAndDeleteItem (oImg, sChkId){
	
	  var oChk = document.getElementById(sChkId);
	  oChk.checked = !oChk.checked;
	  oImg.src = (oChk.checked) ? "<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_red_x.png" : "<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_charcoal_x.png";
	
	  //This piggibacks on old delete function. But called after each checkbox selection. So only one item will be deleted on each click.
	  //call remove function after checking the checkbox (which going to be hidden)
	 javascript:removeItems();
	}
	
function processDetail(itemid, uom) {
	<s:url id='detailURL' namespace='/catalog' action='itemDetails.action'>
	</s:url>
	// Begin - Changes made by Mitesh Parikh for 2422 JIRA
	<s:set name="itemDtlBackPageURL" value="%{itemDtlBackPageURL}" scope="session"/>
	// End - Changes made by Mitesh Parikh for 2422 JIRA
	window.location.href="<s:property value='%{detailURL}' escape='false'/>" + "&itemID=" + itemid + "&unitOfMeasure=" + uom;
}
	
function hideSharedListForm(){
	document.getElementById("dynamiccontent").style.display = "none";
   	}
    	
function showSharedListForm(){
	var dlgForm 		= document.getElementById("dynamiccontent");
	if (dlgForm){
 				dlgForm.style.display = "block";
 			}
 		}

function copyNewCart() {
	if( $.trim($("#copyCartName").val()).length > 0) {
		$("#cp-cart-err-msg").hide();
		document.copyOrder.submit();
	/*	if( $.trim($("#orderDescription").val()).length > 0) 
			{			
			$("#cp-cart-err-msg1").hide();
			 document.copyOrder.submit();
			}
		else
			{			
			$("#cp-cart-err-msg1").show();
			} commented for 2919 */
	} else {		
		$("#cp-cart-err-msg").show();
	}
}
var selReplacementId = "";

function setUId(uId)
{
	selReplacementId = uId;
}
</script>

<script type="text/javascript">

function deleteCart(formObj){
		<s:url id="cartActionURL" value="/order/draftOrderDelete.action">
		</s:url>
			formObj.action = '<s:property value="cartActionURL"/>';
           formObj.submit();
}
function maxNewLength(field,maxlimit) {
	if (field.value.length > maxlimit) // if too long...trim it!
	     // commented for jira 2923 alert(field.title + ' should not exceed '+maxlimit+ ' characters');
		field.value = field.value.substring(0, maxlimit);
		return false;
	}

function resetDeleteCart(formObj)
{
	document.getElementById("otherCartActions").value = "None";
}
function quickAddCopyAndPaste(data){
	//Clean up the data
	//Ext.get('dlgCopyAndPasteText').dom.value = '';
	//$.fancybox.close();
	
	//console.debug("data: ", data);
	var itemLineFlag = "false";
	var itemsString = data;
	var char = '\n';
	var itemLines = itemsString.split(char);
	var checklength=itemLines.length;//jira 4128
	if(itemsString == "")
	{
		document.getElementById("errorMsgCopyBottom").innerHTML = "Valid string is required. See instructions above." ;
        document.getElementById("errorMsgCopyBottom").style.display = "inline";
	}
	else 
	{
		document.getElementById("errorMsgCopyBottom").innerHTML = "" ;
        document.getElementById("errorMsgCopyBottom").style.display = "none";
	}
				
	for(var i=0;i < itemLines.length; i++)
	{
		var itemQty = null;
		var itemSku = null;
		var jobId = "";
		var itemLine = itemLines[i].split('\t');
		if(i == itemLines.length-1){
			if(itemLine == ""){
				break;
			}
		}//jira4128 start
		if(checklength > 20)
		{
			
			itemLineFlag = "true";
			alert( "<s:text name='MSG.SWC.CART.ADDTOCART.ERROR.QTYGT20' />");
			break;
			//document.getElementById("errorMsgCopyBottom").innerHTML = "You can only add 20 items per copy/paste." ;
	        //document.getElementById("errorMsgCopyBottom").style.display = "inline";
		}
		//jira4128 stop
		if(itemLine.length > 1 )
		{
			itemQty = itemLine[0];
			itemSku = itemLine[1];
		}
		itemLine = itemLines[i].split(',');
		if(itemLine.length > 1 )
		{
			itemQty = itemLine[0];
			itemSku = itemLine[1];
			if(itemSku == "" && itemQty == ""){
				itemLineFlag = "true";
				document.getElementById("errorMsgCopyBottom").innerHTML = "Valid string is required. See instructions above." ;
		        document.getElementById("errorMsgCopyBottom").style.display = "inline"; 
			}
		}
		else
		{
			itemLineFlag = "true";
			document.getElementById("errorMsgCopyBottom").innerHTML = "Valid string is required. See instructions above." ;
	        document.getElementById("errorMsgCopyBottom").style.display = "inline"; 
		}
		
		/*
		itemSku = Ext.util.Format.trim(itemSku);
		itemQty = Ext.util.Format.trim(itemQty);
		
		document.getElementById("qaProductID").value= itemSku;
		document.getElementById("qaQuantity").value= itemQty;
		//call metods for quick add 3349 by balkhi
		  addProductToQuickAddList(document.getElementById('quickAddButton'));
		//qaAddItem(jobId, itemQty, itemSku, '','', 'xpedx #' );  */
	}
	if(itemLineFlag == "false")
	{
		for(var i=0;i < itemLines.length; i++)
		{
			var itemQty = null;
			var itemSku = null;
			var jobId = "";
			var itemLine = itemLines[i].split('\t');
			
			if(itemLine.length > 1 )
			{
				itemQty = itemLine[0];
				itemSku = itemLine[1];
			}
			itemLine = itemLines[i].split(',');
		
			if(itemLine.length > 1 )
			{
				itemQty = itemLine[0];
				itemSku = itemLine[1];
			}					
			if((i+1) == itemLines.length && itemLineFlag == "false")
			{
				$.fancybox.close();
				Ext.get('dlgCopyAndPasteText').dom.value = '';
			}
			itemSku = Ext.util.Format.trim(itemSku);
			itemQty = Ext.util.Format.trim(itemQty);
			if(itemSku != null && itemSku != "null"){
				
			document.getElementById("qaProductID").value= itemSku;
			document.getElementById("qaQuantity").value= itemQty;
			//call metods for quick add 3349 by balkhi
		  	addProductToQuickAddList(document.getElementById('quickAddButton'));
			//qaAddItem(jobId, itemQty, itemSku, '','', 'xpedx #' ); 
			}
		}
	}
	
	//var w = Ext.WindowMgr.get("dlgCopyAndPaste");
	//w.hide();
}
</script>

<script type="text/javascript">
$(document).ready(function(){
		$('#quick-add-button').click(function(){
				var offset = $(this).offset();
				$('#tq-quick-add-overlay').css({ top: offset.top+35 });         
				$('#tq-quick-add-overlay').toggle();
				return false;
		});
		$('#quick-add-close').click(function(){
				$('#tq-quick-add-overlay').toggle();
				return false;
		});
/* 		$('ul.mil-desc-attribute-list li').each(function(){
            $(this).shorten({noblock: true, width:($(this).width() - 20)});
		}); */
		

		/* FIX FOR : CART, WC, EDIT ORDER PAGES */
		/* To ensure that the long/short desc. gets shortened each time the view changes.
		 * Added per Jira 3318. (Looks like substring is ignoring the spaces.)
		 */
			/* Begin Short desc. shortener */
			//mil-wrap-condensed-desc item-short-desc
			// $('.mil-desc-wrap mil-wrap-condensed-desc item-short-desc short-description').each(function() { 
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
			 /* End Short desc. shortener */
			 
				/* Begin Long desc. shortener */
				$('.mil-desc-attribute-list ul li').each(function() {
					var html = $(this).html();
					var shortHTML = html.substring(0, 40);
					if( html.length > shortHTML.length )
					{
						$(this).html(shortHTML);
						$(this).append('...');	
						$(this).attr('title', html );
					}
				});
				
				$('.prodlist ul li, #prodlist ul li').each(function() {
					var html = $(this).html();
					var shortHTML = html.substring(0, 30);
					if( html.length > shortHTML.length )
					{
						$(this).html(shortHTML);
						$(this).append('...');	
						$(this).attr('title', html );
					}
				});
				/* end Long desc. shortener */
			/* End Jira 3318 changes */
});
</script>

<%-- <title><s:text name="draftorder.details.title" /></title> --%>
<title><s:text name="MSG.SWC.ORDR.DRAFTORDRDETAIL.GENERIC.TABTITLE" /></title>

				<!-- Web Trends tag start -->
						<s:if test='%{#session.isUpdateCartMetaTag != null}'>						
							<meta name ="DCSext.w_x_cart_save" content="1" />
							<s:set name="isUpdateCartMetaTag" value="<s:property value=null />" scope="session"/> 							
						</s:if>
						<s:if test='%{#session.reorderMetaTag != null}'>						
							<meta name ="DCSext.w_x_reorder" content="1" />
							<s:set name="reorderMetaTag" value="<s:property value=null />" scope="session"/> 							
						</s:if>
						
					<!-- Web Trends tag end -->		
</head>

<div style="display:none;">
<div id="dlgCopyAndPaste" class="xpedx-light-box" style="width: 400px; height: 300px;">
<h2>Copy and Paste &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  <small>Limit is 20 items per copy/paste</small> </h2>
<%-- <p>Copy and Paste the quantities and <s:property value="wCContext.storefrontId" /> item #'s from your file. --%>
<!-- Enter one item per line:<br /> -->
<!-- Qty. [Tab or Comma] Item#</p> -->
<p>Copy and paste or type the quantities and <s:property value="wCContext.storefrontId" />  item numbers from your file in the following format: quantity,item number (no spaces). <br/>Example:12,5002121 </p>
<p>To enter items without quantities, copy and paste or type a comma followed by the item number (no spaces).<br/> Example: ,5002121  <br />
</p>
<br />
<form id="form1" name="form1" method="post" action=""><textarea
	name="dlgCopyAndPasteText" id="dlgCopyAndPasteText" cols="48" rows="5"></textarea>
<ul id="tool-bar" class="tool-bar-bottom" style="float:right";>
	<li><a class="grey-ui-btn" href="javascript:$.fancybox.close();"
		onclick="Ext.get('dlgCopyAndPasteText').dom.value = '';Ext.get('errorMsgCopyBottom').dom.innerHTML='';Ext.get('errorMsgCopyBottom').dom.style.display='none'"><span>Cancel</span></a></li>
	<li style="float: right;"><a href="javascript: quickAddCopyAndPaste( document.form1.dlgCopyAndPasteText.value);" class="green-ui-btn" style="margin-left:5px;"><span>Add to Quick List</span></a></li>
	
	
</ul>
</form>
</br></br></br><div class="error" id="errorMsgCopyBottom" style="display:none;position:relative;left:100px" ></div>
</div>
</div>

<s:set name='_action' value='[0]' />
<s:bean
	name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils'
	id='XPEDXWCUtils' />
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean'
	id='util' />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean' id='xpedxutil' />	
<s:bean name='com.sterlingcommerce.webchannel.utilities.CommonCodeUtil'
	id='ccUtil' />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXPriceandAvailabilityUtil'
	id='priceUtil' />
<s:set name='orderShippingAndTotalStartTabIndex' value='900' />
<s:set name='sdoc' value="outputDocument" />
<s:set name='orderDetails' value='#util.getElement(#sdoc, "Order")' />
<s:set name='extnOrderDetails'
	value='#util.getElement(#sdoc, "Order/Extn")' />
<s:set name='orderHeaderKey'
	value='#orderDetails.getAttribute("OrderHeaderKey")' />
<s:set name='draftOrderFlag'
	value='#orderDetails.getAttribute("DraftOrderFlag")' />
<s:set name='priceInfo'
	value='#util.getElement(#sdoc, "Order/PriceInfo")' />
<s:set name='currencyCode' value='#priceInfo.getAttribute("Currency")' />
<s:set name='overallTotals'
	value='#util.getElement(#sdoc, "Order/OverallTotals")' />
<s:set name='orderExtn' value='#util.getElement(#sdoc, "Order/Extn")' />
<s:set name='orderLines'
	value='#util.getElement(#sdoc, "Order/OrderLines")' />
<s:set name='shipping' value='#util.getElement(#sdoc, "Order/Shipping")' />
<s:set name='wcContext' value="wCContext" />
<s:set name="xpedxCustomerContactInfoBean" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("XPEDX_Customer_Contact_Info_Bean")' />
<s:set name="isEstimator" value='%{#xpedxCustomerContactInfoBean.isEstimator()}' />
<s:set name="msapExtnUseOrderMulUOMFlag" value='%{#xpedxCustomerContactInfoBean.getMsapExtnUseOrderMulUOMFlag()}' />

<%--	Using CustomerContactBean object from session
<s:set name="isEstimator" value="#wCContext.getWCAttribute('isEstimator')" />
 --%>
<s:set name='chargeDescriptionMap'
	value='#util.getChargeDescriptionMap(#wcContext)' />
<s:set name='isOwnerOfNonCartInContextDraftOrder'
	value='#_action.isOwnerOfNonCartInContextDraftOrder()' />
<s:set name='isProcurementInspectMode'
	value='#util.isProcurementInspectMode(wCContext)' />
<s:set name='isReadOnly'
	value='#isOwnerOfNonCartInContextDraftOrder || #isProcurementInspectMode' />
<s:set name='tempIsReadOnly'
	value='#isOwnerOfNonCartInContextDraftOrder || #isProcurementInspectMode' />
<s:set name='isProcurementUser' value='wCContext.isProcurementUser()' />
<s:set name='hasPendingChanges'
	value='#orderDetails.getAttribute("HasPendingChanges")' />
	
<s:if test='majorLineElements.size() == 0'>
	<s:set name='checkoutDisabled' value='"disabled"' />
</s:if>
<s:set name="editOrderFlag" value='%{#_action.getIsEditOrder()}' />
<s:set name="resetDescFlag" value='%{#_action.getResetDesc()}' />
<s:else>
	<s:set name='checkoutDisabled' value='' />
</s:else>

<s:set name='appFlowContext' value='#session.FlowContext' />
<s:set name='isGuest' value='wCContext.isGuestUser()' />

<s:set name="isEditOrderHeaderKey" value ="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute(@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@EDITED_ORDER_HEADER_KEY)}"/>

<s:set name='numberOfInitialComplementaryItemsToDisplay' value='3' />
<%-- commented for performance jira# 3594
<s:set name='canChangeOrderName'
	value='#_action.isOrderModificationAllowed("ORDER_NAME")' />
<s:set name='canChangeCurrency'
	value='#_action.isOrderModificationAllowed("CHANGE_CURRENCY", false)' />
<s:set name='canAddLine'
	value='#_action.isOrderModificationAllowed("ADD_LINE")' />
<s:set name='canChangeOrderDate'
	value='#_action.isOrderModificationAllowed("CHANGE_ORDER_DATE")' />
 --%>
<s:set name='canChangeOrderName'
	value='true' />
<s:set name='canChangeCurrency'
	value='true' />
<s:set name='canAddLine'
	value='true' />
<s:set name='canChangeOrderDate'
	value='true' />	

<s:set name="emailDialogTitle" scope="page"
	value="#_action.getText('Email_Title')" />
	<%--jira 2885 --%>
<s:set name="pnALineErrorMessage" value="#_action.getPnALineErrorMessage()" />
<s:set name="pnaErrorStatusMsg" value="#_action.getAjaxLineStatusCodeMsg()"/>
<s:url includeParams="none" id="orderNotesListURL"
	action="orderNotesList.action">
	<s:param name="OrderHeaderKey" value='#orderHeaderKey' />
	<s:param name="draft" value="#draftOrderFlag" />
</s:url>

<s:url id="returnURL" action="draftOrderDetails">
	<s:param name="OrderHeaderKey" value='#orderHeaderKey' />
	<s:param name="draft" value="#draftOrderFlag" />
</s:url>

<s:if test='#_action.isDraftOrder() || !#canAddLine'>
	<s:url id="continueShoppingURL" action="navigate" namespace="/catalog" />
</s:if>
<s:else>
	<s:url id="continueShoppingURL" action="navigate" namespace="/catalog"
		escapeAmp="false">
		<s:param name='orderHeaderKey'>
			<s:property value='#orderHeaderKey' />
		</s:param>
		<s:param name='currency'>
			<s:property value='#currencyCode' />
		</s:param>
		<s:param name='flowID'>
			<s:property value='flowType' />
		</s:param>
		<s:param name='_r_url_' value='%{returnURL}' />
		<s:param name='draft'>
			<s:property value='#draftOrderFlag' />
		</s:param>
		<s:param name='editedOrderHeaderKey'>
			<s:property value='#orderHeaderKey' />
		</s:param>
	</s:url>
</s:else>

<s:url id="urlEmail" includeParams="none" escapeAmp="false"
	action='emailOrder' namespace='/order'>
	<s:param name="messageType" value='%{"ComposeMail"}' />
	<s:param name="orderHeaderKey" value='%{#orderHeaderKey}' />
	<s:param name="draft" value="#draftOrderFlag" />
</s:url>
<s:url id="urlPrint" includeParams="none" escapeAmp="false"
	action='PrintCartDetail.action' namespace='/order' />

<s:url id="discardPendingChangesURL" includeParams="none"
	action='XPEDXResetPendingOrder' namespace='/order' escapeAmp="false">
	<s:param name="orderHeaderKey" value='%{#orderHeaderKey}' />
</s:url>

<s:url id='checkoutURLid' namespace='/order' action='xpedxsaveCartDetails' />
<s:if test='#isProcurementUser'>
	<s:if test='#isProcurementInspectMode'>
		<s:set name='checkoutButtonText'
			value='%{#_action.getText("ProcurementCancelAndReturn")}' />
		<s:url id='procurementInspectModeCheckoutURLid' namespace='/order'
			action='procurementPunchOut' />
		<s:set name='mode' value='"cancel"' />
		<s:url id='procurementImmediatePunchOutURL' namespace='/order'
			action='procurementPunchOut' escapeAmp='false'>
			<s:param name='mode' value='"cancel"' />
		</s:url>
		<s:set name='procurementCheckoutDisabled' value='' />
	</s:if>
	<s:else>
		<s:set name='checkoutButtonText'
			value='%{#_action.getText("ProcurementSaveAndReturn")}' />
		<s:url id='procurementCheckoutURLid' namespace='/order'
			action='procurementSaveAndReturn' />
		<s:set name='mode' value='"save"' />
		<s:url id='procurementImmediatePunchOutURL' namespace='/order'
			action='procurementPunchOut' escapeAmp='false'>
			<s:param name='mode' value='"save"' />
		</s:url>
		<s:if test='invalidOrderLinesMap.size() == 0'>
			<s:set name='procurementCheckoutDisabled' value='' />
		</s:if>
		<s:else>
			<s:set name='procurementCheckoutDisabled' value='"disabled"' />
		</s:else>
	</s:else>
</s:if>
<s:bean name='org.apache.commons.lang.StringUtils' id='strUtil' />

<body class="  ext-gecko ext-gecko3">

<div id="tq-quick-add-overlay" class="quick-add float-right" style="display: none;">
	<div class="tq-quick-add-form">
		<span class="page-title">Quick Add</span>
		<p class="quick-add-aux-links" style="margin-top:5px; margin-right:5px;"> 
			<a class="modal underlink" href="#dlgCopyAndPaste" onclick="javascript: writeMetaTag('DCSext.w_x_ord_quickadd_cp', '1');" id="copyPaste" >Copy and Paste</a>
			<img class="pointers" alt="[close]" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_charcoal_x.png" id="quick-add-close" title="Close">
		</p>
		<div class="clear">&nbsp;</div>
			<form name="QuickAddForm" class="form selector quick-add-to-cart-form" id="QuickAddForm">
				<s:hidden name='#action.name' id='validationActionNameQA' value='draftOrderDetails' />
				<s:hidden name='#action.namespace' value='/order' />
				<s:hidden name="orderHeaderKey"	value='%{#orderHeaderKey}' />
				<s:hidden name="draft" value="%{#draftOrderFlag}" />
				<s:hidden name='Currency' value='%{#currencyCode}' />
				<s:hidden id="isPNACallOnLoad" name="isPNACallOnLoad" value='false' />	
				<%--<s:if test='%{#editOrderFlag == "true" || #editOrderFlag.contains("true")}'>
					<s:hidden name="isEditNewline" value="%{'Y'}"/>
				</s:if>
				<s:else>
					<s:hidden name="isEditNewline" value="%{'N'}"/>
				</s:else>	--%>	
				<input type="hidden" name="isEditOrder" value="<s:property	value='%{(#_action.getIsEditOrder())}' escape="false" />"/>
				<ul class="hvv">
					<li>
						<label>Item Type:</label>
						<s:select id="qaItemType"  name="qaItemType" cssStyle="width:135px;"
										headerKey="1"
										list="skuTypeList" listKey="key" listValue="value"/>
						
						<s:hidden name="#qaItemType.type" value="ItemID" />
					</li>
					<li>
						<label>Item #:</label>
						<input maxlength="27" style="width:70px;" type="text" id="qaProductID" name="qaProductID" class="text x-input" />
						<s:hidden name="#qaProductID.type" value="ItemID" />
						<s:hidden name='localizedMissingProductIDMessage'value='%{#_action.getText("QAMissingProductID")}' />
					</li>
					<li>
						<label>Qty:</label>						
						<input maxlength="7" style="width:70px;" type="text" id="qaQuantity" name="qaQuantity" class="qty-field text x-input" onKeyUp="return isValidQuantityRemoveAlpha(this,event)"/>
						<s:hidden name="#qaQuantity.type" value="OrderedQty" />
					</li>
					<s:set name="jobIdFlag" value='%{customerFieldsMap.get("CustLineAccNo")}'></s:set>
					<s:set name="chargeAmount" value='%{chargeAmount}'></s:set>
					<%--JIRA 3547 start--%>
					<s:set name="fmtdchargeAmount" value='#util.formatPriceWithCurrencySymbol(wCContext,#currencyCode,#chargeAmount)'/>
					<s:set name="minOrderAmount" value='%{minOrderAmount}'></s:set>
					<s:set name="fmtdMinOrderAmount" value='#util.formatPriceWithCurrencySymbol(wCContext,#currencyCode,#minOrderAmount)'/>
					<s:set name="erroMsg" value='%{erroMsg}'></s:set>
					<%--JIRA 3547 end--%>
					<%--JIRA 3488 start--%>
					<s:set name="maxOrderAmount" value='%{maxOrderAmount}'></s:set>
					<s:set name="fmtdMaxOrderAmount" value='#util.formatPriceWithCurrencySymbol(wCContext,#currencyCode,#maxOrderAmount)'/>
					<%--JIRA 3488 end--%>
					<%--JIRA 3853 start--%>
					<s:if test='%{#jobIdFlag != null && !#jobIdFlag.equals("")}'>
					<li>
						<label><s:property value='#jobIdFlag' />:</label>
						 <s:hidden name='jobIdValue' value='%{#jobIdFlag}' />
						<input maxlength="24" style="width:154px;" type="text" id="qaJobID" name="qaJobID" class="text x-input" />
						<s:hidden name="#qaJobID.type" value="" />
					</li>
					</s:if>
					<s:else>
						<s:hidden id="qaJobID" name="qaJobID" value="" />
						<s:hidden name="#qaJobID.type" value="" />
					</s:else>
						<%--JIRA 3853 end--%>
					<s:set name="customerPONoFlag" value='%{customerFieldsMap.get("CustomerPONo")}'></s:set>
					<s:if test='%{#customerPONoFlag != null && !#customerPONoFlag.equals("")}'>
					<li>
						<label><s:property value='#customerPONoFlag' />:</label>
						<s:textfield maxlength="22" name="purchaseOrder" value=""></s:textfield>
					</li>
					</s:if>
					<li>
						<label>&nbsp;</label>
						<input id="quickAddButton" type="hidden"/>
						<a class="grey-ui-btn" onclick="javascript:addProductToQuickAddList(document.getElementById('quickAddButton')); return false;" href="#" class="noborder">
							<%-- <img src="<s:url value='/xpedx/images/theme/theme-1/quick-add/addtoquicklist.png'/>" /> --%>
							<span><p>+</p>Add to Quick List</span>
							</a>
						<s:hidden name='localizedDeleteLabel' value='%{#_action.getText("localizedDeleteLabel")}' />
						<s:hidden name='localizedAddToCartLabel' value='%{#_action.getText("AddQAListToCart")}' />
					</li>
				</ul>
				<s:url id='productValidateURLid' namespace='/order' action='validateProduct' />
				<s:a id='productValidateURL' href='%{#productValidateURLid}' />
				<div id="QuickAddList" style="display: block;"></div>
				<div class="error" id="errorMsgItemBottom" style="display:none;position:relative;left:340px" ></div>
				
			</form>
			<!-- Start 2964 -->
			<s:hidden name="msapOrderMulUOMFlag" id="msapOrderMulUOMFlag" value="%{#msapExtnUseOrderMulUOMFlag}" />
			
			<div class="clear">&nbsp;</div>
			<div id="addProdsToOrder" class="close-btn" style="display: none;">
				<%-- <a href="#" id="quick-add-close" class="grey-ui-btn"><span>Close</span></a> --%>
				<a href="#" class="orange-ui-btn" onclick="javascript:addProductsToOrder(); return false;" tabindex="210"  name="addProdsToOrder">
					<s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
						<span>Add to Cart</span>
					</s:if>
					<s:else>
						<span>Add to Order</span>
					</s:else>
				</a>
			</div>
	</div>
</div><!-- id="tq-quick-add-overlay" -->

<div id="main-container">

<div id="main">
	<s:action name="xpedxHeader" executeResult="true" namespace="/common" >
		<s:param name='shipToBanner' value="%{'true'}" />
	</s:action> 
	<!-- // t1-header end --> <!-- begin t1-navigate -->



<div class="container shopping-cart">

<s:if test='ajaxLineStatusCodeMsg!=null'>
	<div id="errorMsgDiv">
	<s:if test='#pnaErrorStatusMsg !=null && pnaErrorStatusMsg != "" '>
	<h5 align="center"><b><font color="red"><s:property value="pnaErrorStatusMsg" /></font></b></h5><br/>
	</s:if>
<%--	<h5 align="center"><b><font color="red"><s:property
		value="ajaxLineStatusCodeMsg" /></font></b></h5>  --%>
	</div>
	<h5 align="center"><b><font color="red"><div id="minOrderErrorMessage"></div></font></b></h5><br/>
	<h5 align="center"><b><font color="red"><div id="maxOrderErrorMessage"></div></font></b></h5><br/>
	<h5 align="center"><b><font color="red"><div id="entileErrorMessade"></div></font></b></h5><br/>
</s:if>

<!-- breadcrumb / 'print page' button -->
<div class="breadcrumb-title" id="breadcumbs-list-name">
	<span class="page-title">
	<s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
		My Cart:&nbsp;
		<s:if test='%{#editOrderFlag == "true" || #editOrderFlag.contains("true")}'>
			<s:if test='#orderDetails.getAttribute("OrderType") != "Customer" ' > 
        	 Order #: <s:property value='@com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils@getFormattedOrderNumber(#orderExtn)'/>
        </s:if>
        <s:else>
        	Web Confirmation: <s:property value='#orderExtn.getAttribute("ExtnWebConfNum")'/>
        </s:else>
	</s:if>
	<s:else>
			 	<s:property value='#orderDetails.getAttribute("OrderName")' />
		</s:else>
	</s:if>
	<s:else>
			<s:if test='#orderDetails.getAttribute("OrderType") != "Customer" ' > 
        		<b>Order #: <s:property value='@com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils@getFormattedOrderNumber(#orderExtn)'/></b>
        	</s:if>
        	<s:else>
        		<b>Web Confirmation: <s:property value='#orderExtn.getAttribute("ExtnWebConfNum")'/></b>
        	</s:else>
	</s:else>
	
	</span>
	

<br/><br/>
<a href="javascript:window.print()"><span class="print-ico-xpedx"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/print-icon<s:property value='#wcUtil.xpedxBuildKey' />.gif" width="16" height="15" alt="Print Page" /><span class="underlink">Print Page</span></span></a>
</div>

<div id="mid-col-mil">

<div class="mil-edit">

<div class="float-right">
	<!-- promotion -->
	<div class="ad-margin">
		<!-- ad placeholder, per the mockup. Ad Juggler Starts -->
		<div class="float-none ad-float smallBody"><img height="4" width="7" class="ad-img" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/mil/ad-arrow<s:property value='#wcUtil.xpedxBuildKey' />.gif" alt="advertisement" />advertisement</div>

		<s:set name='ad_keyword' value='%{#_action.getAdjCatTwoShortDesc()}' />
				
		<!-- aj_server : https://rotator.hadj7.adjuggler.net:443/servlet/ajrotator/  -->
		
		<s:if test="#ad_keyword != null" >
			<s:if test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT}' >
				<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118169'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<s:property value="%{#ad_keyword}" />';
				aj_pv = true; aj_click = '';
				</script>
			</s:if>
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CANADA_STORE_FRONT}' >
								<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118204'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<s:property value="%{#ad_keyword}" />';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif>
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@BULKLEY_DUNTON_STORE_FRONT}' >
									<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118191'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<s:property value="%{#ad_keyword}" />';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif >
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_STORE_FRONT}' >
				<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '115718'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<s:property value="%{#ad_keyword}" />';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif>
			<s:else >
				<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '115718'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<s:property value="%{#ad_keyword}" />';
				aj_pv = true; aj_click = '';
				</script>
			</s:else>
		</s:if>	
		<s:else>
			<s:if test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT}' >
				<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118169'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<%=session.getAttribute("CUST_PREF_CATEGORY_DESC")%>';
				aj_pv = true; aj_click = '';
				</script>
			</s:if>
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CANADA_STORE_FRONT}' >
								<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118204'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<%=session.getAttribute("CUST_PREF_CATEGORY_DESC")%>';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif>
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@BULKLEY_DUNTON_STORE_FRONT}' >
									<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118191'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<%=session.getAttribute("CUST_PREF_CATEGORY_DESC")%>';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif >
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_STORE_FRONT}' >
				<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '115718'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<%=session.getAttribute("CUST_PREF_CATEGORY_DESC")%>';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif>
			<s:else>
				<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '115718'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<%=session.getAttribute("CUST_PREF_CATEGORY_DESC")%>';
				aj_pv = true; aj_click = '';
				</script>
			</s:else>			
		</s:else>
		<script type="text/javascript" language="JavaScript" src="https://img.hadj7.adjuggler.net/banners/ajtg.js"></script>
		<!-- Ad Juggler Tag Ends -->
				
			
		<div class="clear">&nbsp;</div>
	</div>
</div>
<!-- end promotion space -->
<!-- List Item Description -->
<div class="mil-edit-forms">

	<s:if test="#canChangeOrderName">
		Name
		<s:textfield name='cartName_new' id="cartName_new" size="35"
			cssClass="x-input" onkeyup="javascript:maxNewLength(this,'35');"
			value='%{#orderDetails.getAttribute("OrderName")}' tabindex="3400" />
	</s:if> 
	<s:else>
		<input type="hidden" name="cartName_new" id="cartName_new" onkeyup="javascript:maxNewLength(this,'35');" value="<s:property value='%{#orderDetails.getAttribute("OrderName")}' />" />
	</s:else>
	<br />
	
	
	
<s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">	
	<s:if test="#canChangeOrderName">
	<%-- <s:textfield id="cartDesc_new" name='cartDesc_new' id="cartDesc_new" size="50"
			cssClass="x-input" onkeyup="javascript:maxNewLength(this,'220');"
			value='%{#extnOrderDetails.getAttribute("ExtnOrderDesc")}'
			tabindex="3400" /> --%>
		
			Description
			<br/>
			<s:if test='%{#resetDescFlag == "true" || #resetDescFlag.contains("true")}'>
				<textarea  id="cartDesc_new" name="cartDesc_new" onkeyup="javascript:maxNewLength(this,'255');"></textarea>
			</s:if>
			<s:else>
				<textarea  id="cartDesc_new" name="cartDesc_new" onkeyup="javascript:maxNewLength(this,'255');"><s:property value='%{#extnOrderDetails.getAttribute("ExtnOrderDesc")}' /></textarea>
			</s:else>
								
	</s:if> 
	<s:else>	
		<s:if test='%{#resetDescFlag == "true" || #resetDescFlag.contains("true")}'>
			<textarea  id="cartDesc_new" name="cartDesc_new" onkeyup="javascript:maxNewLength(this,'255');"></textarea>
		</s:if>
		<s:else>
			<textarea  id="cartDesc_new" name="cartDesc_new" onkeyup="javascript:maxNewLength(this,'255');"><s:property value='%{#extnOrderDetails.getAttribute("ExtnOrderDesc")}' /></textarea>
		</s:else>
	</s:else>
</s:if>
<s:else>
<%-- <span class="red bold">Donâ€™t forget to â€˜Checkoutâ€™ to apply these changes to your order</span> --%>
<span class="red bold"> <s:text name='MSG.SWC.ORDR.DRAFTORDRDETAIL.GENERIC.CHKOUTMUST.FORCHANGES' /> </span>
	<s:if test="#canChangeOrderName">
	<%-- <s:textfield id="cartDesc_new" name='cartDesc_new' id="cartDesc_new" size="50"
			cssClass="x-input" onkeyup="javascript:maxNewLength(this,'220');"
			value='%{#extnOrderDetails.getAttribute("ExtnOrderDesc")}'
			tabindex="3400" /> --%>
		
			<s:if test='%{#resetDescFlag == "true" || #resetDescFlag.contains("true")}'>
				<s:hidden  id="cartDesc_new" name="cartDesc_new"></s:hidden>
			</s:if>
			<s:else>
			<s:set name='extOrderDesc' value='#extnOrderDetails.getAttribute("ExtnOrderDesc")' /> 
				<s:hidden  id="cartDesc_new" name="cartDesc_new" value='<s:property value="%{#extOrderDesc}" />' />
			</s:else>
								
	</s:if> 
	<s:else>	
		<s:if test='%{#resetDescFlag == "true" || #resetDescFlag.contains("true")}'>
			<s:hidden  id="cartDesc_new" name="cartDesc_new"/>
		</s:if>
		<s:else>
			<s:set name='extOrderDesc' value='#extnOrderDetails.getAttribute("ExtnOrderDesc")' />
			<s:hidden  id="cartDesc_new" name="cartDesc_new" value='<s:property value="%{#extOrderDesc}" />' />
		</s:else>
	</s:else>

</s:else>	
	<div class="clearall">&nbsp;</div>
	
	<s:if test='%{#editOrderFlag == "true" || #editOrderFlag.contains("true")}'>	
		<ul class="float-right tool-bar-bottom sc-btn-list margin-top-15">
	</s:if>
	<s:else>
		<ul class="float-right tool-bar-bottom sc-btn-list">
	</s:else>
	
	<li class="float-right"><a id="quick-add-button" class="grey-ui-btn" href="#"><span>Quick Add</span></a></li>
    <s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
    	<li><a href="#" name="otherCartActions" id="otherCartActions" class="grey-ui-btn" onclick="javascript:actionOnList('Copy');" /><span>Copy Cart</span></a></li>
    </s:if>	
<s:if test='majorLineElements.size() > 0'>
	<s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
   	 <li><a class="grey-ui-btn sc-update-cart" href="javascript:update();"><span>Update Cart</span></a></li>
   	 </s:if>
   	 <s:else>
   	 	<li><a class="grey-ui-btn sc-update-cart" href="javascript:update();"><span>Update Order</span></a></li>
   	 </s:else>
 </s:if>

</ul>
	<br />
	
</div>
<br/><div class="error" id="errorMsgTop" style="display:none;position:relative;margin-left:445px;" ></div>
<div class="clear">&nbsp;</div>
<!-- end item description -->

	<br />
	
	<div class="mil-wrap-condensed-container">

	<s:include value="XPEDXCoreDraftOrderDetail.jsp" />

<div id="order-btm-left">
<s:if test='majorLineElements.size() > 0'>
<div id="selected-items-manager">

<s:set name="isUserAdmin" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@isCurrentUserAdmin(wCContext)" />
<!-- START of new MIL - PN -->
<div style="display: none;">
	<a id="dlgShareListLink" href="#dlgShareList"> show new list</a>
	<div id="dlgShareList" class="share-modal xpedx-light-box">
	<h2 id="smilTitle">Share My Items List</h2>
	<br>
	
	<!-- CODE_START MIL - PN --> 
	<s:form 
		id="XPEDXMyItemsDetailsChangeShareList" 
		name="XPEDXMyItemsDetailsChangeShareList"  
		action="XPEDXMyItemsDetailsChangeShareList" 
		namespace="/xpedx/myItems" method="post">
	
		<p><strong>List Name:</strong>&nbsp;&nbsp;<input type="text" name="listName" value="" maxlength="255"/></p>
		<p><strong>Description:</strong>&nbsp;&nbsp;<input type="text" name="listDesc" value="" maxlength="255"/></p>
		<p><strong>Share With:</strong></p>
	
		<s:hidden name="listKey" value="new"></s:hidden>
		<s:hidden name="editMode" value="%{true}"></s:hidden>
		<s:hidden name="itemCount" value="%{0}"></s:hidden>
		<s:hidden id="clFromListId" name="clFromListId" value=""></s:hidden>
		
		<s:set name="rbPermissionShared" value="%{''}" />
		<s:set name="rbPermissionPrivate" value="%{''}" />
		<s:if test="%{#isUserAdmin}">
			<s:set name="rbPermissionShared" value="%{' checked '}" />
		</s:if>
		<s:else>
			<s:set name="rbPermissionPrivate" value="%{' checked '}" />
		</s:else>
		
		<s:set name="saCV" value="%{''}" />
		<s:if test='%{shareAdminOnly == "Y"}'>
			<s:set name="saCV" value="%{' checked '}" />
		</s:if>
	
		<!-- START - Saved hidden data Fields -->
		<s:iterator id="item" value='savedSharedList'>
			<s:set name='customerID' value='#item.getAttribute("CustomerID")' />
			<s:set name='customerPath' value='#item.getAttribute("CustomerPath")' />
	
			<s:hidden name="sslNames" value="%{#customerID}"></s:hidden>
			<s:hidden name="sslValues" value="%{#customerPath}"></s:hidden>
		</s:iterator>
		<!-- END - Saved hidden data Fields -->
	
		<!-- Private and Shared are missing from the HTMLs -->
		<p><strong> This list is:
		<input onclick="hideSharedListForm()" 
			id="rbPermissionPrivate" <s:property value="#rbPermissionPrivate"/> 
			type="radio" name="sharePermissionLevel"
			value="<s:property value="wCContext.loggedInUserId"/>" />
			Private &nbsp;&nbsp; 
		<s:if test="%{!#isUserAdmin}">
			<div style="display: none;">
		</s:if>
				<input
					onclick="showSharedListForm()" 
					id="rbPermissionShared" 
					<s:property value="#rbPermissionShared"/>
					type="radio"
					name="sharePermissionLevel" 
					value=" " 
				/> 
				Shared 
		<s:if test="%{!#isUserAdmin}">
			</div>
		</s:if>
		
		</strong></p>
		<br />
		
	<s:set name="displayStyle" value="%{''}" />
		<s:if test="%{!#isUserAdmin}">
			<s:set name="displayStyle" value="%{'display: none;'}"/>
		</s:if>
		
		<div style="<s:property value="#displayStyle"/>" id="dynamiccontent">
		<!-- Placeholder for the dynamic content -->
		<s:div id="dlgShareListShared">
			<input type="checkbox" <s:property value="#saCV"/> name="shareAdminOnly" value="Y" /> Edit by Admin users only<br /><!-- changed with 1.82 version -->
			<br />
	<script type="text/javascript">
		
		function shareSelectAll(checked_status){
			//var checked_status = this.checked;
			var checkboxes = Ext.query('input[name*=customerPaths]');
			Ext.each(checkboxes, function(obj_item){
				obj_item.checked = !checked_status;
				obj_item.click();
				//obj_item.fireEvent('click');
			});
		}
		
	</script>

		<a href="javascript:shareSelectAll(true)" >Select All</a>
		<a href="javascript:shareSelectAll(false)" >Deselect All</a>
			
			<!-- START - BODY OF SHARE FORM -->
			<s:div id="divMainShareList" cssClass="grey-msg x-corners">
				<!-- CONTENT WILL GO HERE -->
			</s:div>
			<!-- END - BODY OF SHARE FORM -->
		</s:div>
	</div>
		<SCRIPT type="text/javascript">
						/*if ("<s:property value="rbPermissionPrivate"/>" != ""){
							hideForm('ShareListShared');
						}*/
						
						function submitSL(){
							submitNewlist();
						} 
					</SCRIPT>
	
		</br>
		</br>
		<ul id="tool-bar" class="tool-bar-bottom" style="border-top: 1px solid #CCCCCC; padding-top: 20px;">
			<li><a class="grey-ui-btn" href="javascript:$.fancybox.close()"><span>Cancel</span></a></li>
			<li style="float: right;"><a href="javascript:submitSL();"> <img
				src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/ui-buttons/ui-btn-save<s:property value='#wcUtil.xpedxBuildKey' />.gif" width="49" height="23" alt="Save" title="Save" /> </a></li>
		</ul>
	
	</s:form> 
	<!-- CODE_END MIL - PN -->
	</div>
</div>
<!-- END of new MIL - PN -->

<!-- START - Adding the MIL dropdown - PN -->	
<%-- <ul id="cart-actions"><s:if test="!(#isGuest == true)">		
<li><s:url id='addItemsToListURLid' namespace='/order' action='xpedxAddItemsToList' includeParams="none" /> 
<s:a id='addItemsToListURL' href='%{#addItemsToListURLid}' /> 

<s:action name="XPEDXMyItemsList" executeResult="true" namespace="/xpedx/myItems">
	<s:param name="filterByAccChk" value="%{true}" />
	<s:param name="filterByMyListChk" value="%{true}" />
	<s:param name="filterByShipToChk" value="%{true}" /><!-- changed with 1.82 version -->
	<s:param name="displayAsDropdownList" value="%{true}" />
</s:action></li>				
</s:if>	
</ul> --%>
<!-- END - Adding the MIL dropdown - PN -->
<script type="text/javascript">
function addItemsToList(idx, itemId, name, desc, qty, uom){
	try{ console.debug("List Key: " + currentAadd2ItemList.value); }catch(e){}
	try{ console.debug("idx: " + idx + ", name: " + name  + ", desc: " + desc  + ", qty: " + qty + ", uom: " + uom + ", ItemId: " + itemId); }catch(e){}
	//return;
	
	if (idx == 1){
		$("#dlgShareListLink").trigger('click');
    } else if (idx > 1){
		Ext.Msg.wait("Adding item to list... Please wait.");
		
		document.OrderDetailsForm.listKey.value 			= currentAadd2ItemList.value;
        
        <s:url id='AddItemURL' includeParams='none' escapeAmp="false" namespace="/order" action="xpedxAddItemsToList" />
        
        var url = "<s:property value='#AddItemURL'/>";
        url = ReplaceAll(url,"&amp;",'&');

        if(idx > itemCountList.length){
			setArrayValue(currentAadd2ItemList.value,0);
		}

        var reqIndex;
        for(i=0 ; i < itemCountList.length ; i++)
        {
	        if(itemCountList[i].listKeyId==currentAadd2ItemList.value){
		        reqIndex = i;
		        break;
	        }
        }
        
		var selectedLineItem = document.getElementsByName("selectedLineItem");
		var selectedItemCount = 0;
        for(var i=0 ; i < selectedLineItem.length ; i++)
        {
        	if(selectedLineItem[i].checked==true)
        	{
        		selectedItemCount++;
        	}    
        }

        document.OrderDetailsForm.orderLineItemOrders.value = Number(itemCountList[reqIndex].itemCount)+1;
        if((Number(itemCountList[reqIndex].itemCount)+Number(selectedItemCount))<=200){
			//Execute the call
        	document.body.style.cursor = 'wait';
        	Ext.Ajax.request({
          		url: url,
          		form: 'OrderDetailsForm',
          		method: 'POST',
          		success: function (response, request){
              		document.body.style.cursor = 'default';
              		Ext.Msg.hide();
			  		//reloadMenu();
			  		// Removal of MIL dropdown list from header for performance improvement
			  		itemCountList[reqIndex].itemCount = Number(itemCountList[reqIndex].itemCount)+Number(selectedItemCount) ;
          		},
          		failure: function (response, request){
              		document.body.style.cursor = 'default';
              		Ext.Msg.hide();
              		alert("Error adding item to the list. Please try again later.");
          		}
       		});
        }
        else{
            		alert("Maximum number of element in a list can only be 200..\n Please try again with removing some items or create a new list.");
            		Ext.Msg.hide();
        }
      document.body.style.cursor = 'default';
	  currentAadd2ItemList.selectedIndex = 0;   
	    
    }
}

function prepareDiv(data, itemId, name, desc, qty, uom){
	
	try{
		data = data.replace("[itemId]", itemId);
		data = data.replace("[name]", 	name); 
		data = data.replace("[desc]", 	desc);
		data = data.replace("[qty]",	qty);
		data = data.replace("[uom]", 	uom);
	}catch(e){
		data = "";
	}
	
	return data;
}

var currentAadd2ItemList = new Object();

</script>
 
</div>
</s:if>
</div>

<div class="clearall">&nbsp;</div>
<br />

<div>

</div>

<s:set name = "setTBD" value="%{false}"/>
	<s:if test="%{#_action.getMajorLineElements().size() == #isOrderTBD}">
		<s:set name = "setTBD" value="%{true}"/>
	</s:if>

<s:set name="xutil" value="XMLUtils" /> <s:set
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
	name="allAdjCounter" value="false" />
<!-- Pricing -->
<%--	Using CustomerContactBean object from session
<s:if test='%{#session.viewPricesFlag == "Y"}'>
--%>
<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
<div class="cart-sum-right">
	<table cellspacing="0" align="right" style="font-size:12px">
	<s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
		<tr>
			<th>Subtotal:</th>
			<td>
				<%--<s:if test='%{#editOrderFlag == "true"}'>
					<s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#editOrderOrderExtn.getAttribute("ExtnOrderSubTotal"))' />
				</s:if>
				<s:else>
					--%>	
					<s:set name='extnOrderSubTotal' value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#orderExtn.getAttribute("ExtnOrderSubTotal"))' />
 			  		 <s:if test="%{(#extnOrderSubTotal == #priceWithCurrencyTemp) && #setTBD == true && #_action.getMajorLineElements().size() > 0}">
						<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>  
			  		</s:if>						  
				  	<s:else>
				    	&nbsp;<s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#orderExtn.getAttribute("ExtnOrderSubTotal"))' />
				  	</s:else>					
						  
				<%--</s:else>
			
			--%></td>
		</tr>
		<tr>
			<th>Order Total Adjustments:</th>
			<td><%--<s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#headerAdjustmentWithoutShipping)' /> --%>
				
				<%--<s:if test='%{#editOrderFlag == "true"}'>
					<s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#editOrderOrderExtn.getAttribute("ExtnTotOrderAdjustments"))' />
				</s:if>
				<s:else>
					--%>
				&nbsp;
			  	    <a
						href="javascript:displayLightbox('orderTotalAdjustmentLightBox')" id='tip_<s:property value="#orderHeaderKey"/>'
						tabindex="<s:property value='%{#tabIndex}'/>"> <span
						class="nowrap underlink"><s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#orderExtn.getAttribute("ExtnLegTotOrderAdjustments"))' /></span>
						</a>
						 												
				<%--</s:else>
			--%></td>
		</tr>
		<tr>
			<th>Adjusted Subtotal:</th>
			<td><%-- <s:property value='#adjustedSubtotalWithoutTaxes' /> --%>
				<%--<s:if test='%{#editOrderFlag == "true"}'>
					<s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#editOrderOrderExtn.getAttribute("ExtnTotOrdValWithoutTaxes"))' />
				</s:if>
				<s:else>
					--%>
					<s:set name='extnTotOrdValWithoutTaxes' value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#orderExtn.getAttribute("ExtnTotOrdValWithoutTaxes"))' />
	 			  <s:if test="%{(#extnTotOrdValWithoutTaxes == #priceWithCurrencyTemp) && #setTBD == true && #_action.getMajorLineElements().size() > 0}">
						<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>  
				  </s:if>					
					<s:else>
							&nbsp;<s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#orderExtn.getAttribute("ExtnTotOrdValWithoutTaxes"))' />																			
					</s:else>					
			<%-- </s:else>
				
			--%></td>
		</tr>
		<tr>
			<th>Tax:</th>
			<td class="red bold">
					<s:text name='MSG.SWC.ORDR.OM.INFO.TBD' />
			</td>
		</tr>
		<tr class="bottom-padding">
			<th>Shipping &amp; Handling:</th>
			<td class="red bold">
					<s:text name='MSG.SWC.ORDR.OM.INFO.TBD' />
			</td>
		</tr>
		<%-- <s:set name="subTotAdjusted" value='%{#priceUtil.getLineTotal(#subTotal,"1",#headerAdjustmentWithoutShipping)}' /> --%>
		<tr class="order-total">
			<th>Order Total (<s:property value='#currencyCode'/>):</th>
			<td>
				<%--<s:if test='%{#editOrderFlag == "true"}'>
					<s:property value='%{#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#editOrderOrderExtn.getAttribute("ExtnTotalOrderValue"))}' />
				</s:if>
				<s:else>
					--%>
				<s:set name='extnTotalOrderValue'	value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#orderExtn.getAttribute("ExtnTotalOrderValue"))'/>
		 			  		 <s:if test="%{(#extnTotalOrderValue == #priceWithCurrencyTemp)  && #setTBD == true && #_action.getMajorLineElements().size() > 0}">
									<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>  
					  		</s:if>						  
						  <s:else>
							<s:set name='adjustedSubtotalWithoutTaxes'  value='#orderExtn.getAttribute("ExtnTotalOrderValue")' />
								<s:property value='#util.formatPriceWithCurrencySymbol(#wcContext,#currencyCode,#orderExtn.getAttribute("ExtnTotalOrderValue"))'/>
					     </s:else>					
					

				<%--</s:else>
			
			--%></td>
		</tr>
	</table>
</div>
</s:if>
<div class="clearall">&nbsp;</div>

<!--bottom button 'bar' -->
<div class="bottom-btn-bar scp">
<s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
	<a id="otherCartActions"  class="grey-ui-btn pointers" onclick="javascript:actionOnList('Delete');"><span>Delete Cart</span></a>
</s:if>
<s:else>
	<s:url id="cancelEditOrderChanges" includeParams="none"
							action='XPEDXResetPendingOrder' namespace='/order' escapeAmp="false">
							<s:param name="orderHeaderKey" value='%{#isEditOrderHeaderKey}' />
	</s:url>
	<a id="cancel-btn" class="grey-ui-btn" href="<s:property value="#cancelEditOrderChanges"/>"><span>Cancel Changes</span></a>
	
	
</s:else>	
	<s:set name="ohk" value='%{#orderHeaderKey}' />
	<s:set name="isEstimator" value='%{#xpedxCustomerContactInfoBean.isEstimator()}' />
	<%--	Using CustomerContactBean object from session
	<s:set name="isEstimator" value="%{#wcContext.getWCAttribute('isEstimator')}" />
	--%>
	<s:if test="!#isEstimator">
	<s:if test='majorLineElements.size() > 0'>
	    <s:if test="%{#_action.getCustStatus() != '30'}">
	    <a id="checkout-btn" class="orange-ui-btn" href="javascript:checkOut();"><span>Checkout</span></a>
		</s:if> 
	     <s:if test='#hasPendingChanges == "Y"'>
                   <a id="reset-btn" class="grey-ui-btn" href="<s:property value="#discardPendingChangesURL"/>"><span>Reset Changes</span></a> 
          </s:if>
	    <s:if test='#canAddLine'>
			<a id="cont-shopping" class="grey-ui-btn"  href="<s:property value="#continueShoppingURL"/>"><span>Continue Shopping</span></a>
		</s:if>
		   				   
	</s:if>
	<s:elseif test='#isProcurementUser'>
		<a id="checkout-btn" class="orange-ui-btn" href="javascript:checkOut();"><span>SaveAndReturn</span></a>
		<s:if test='#canAddLine'>
			<a id="cont-shopping" class="grey-ui-btn cont-shopping-margin"  href="<s:property value="#continueShoppingURL"/>"><span>Continue Shopping</span></a>
		</s:if>
	</s:elseif>
	<s:else>
		<s:if test='#canAddLine'>
			<a id="cont-shopping" class="grey-ui-btn"  href="<s:property value="#continueShoppingURL"/>"><span>Continue Shopping</span></a>
		</s:if>
	</s:else>
	</s:if>
<s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
	<a class="grey-ui-btn sc-update-cart" href="javascript:update();"><span>Update Cart</span></a>
</s:if>
<s:else>
	<a class="grey-ui-btn sc-update-cart" href="javascript:update();"><span>Update Order</span></a>
</s:else>
</div>
<!--Added for 3098  -->
<br/><br/><b><div  id="maxOrderErrorMessageBottom" style="position:relative;left:600px;color:red;display:inline" ></div></b>
<br/><br/><b><div  id="entitleErrorMessageBottom" style="position:relative;left:150px;color:red;display:inline" ></div></b>
<br/><br/><h5 align="center"><b><font color="red"><div 	id="minOrderErrorMessageBottom"></div></font></b></h5>
<br/><br/><div  class="error" id="errorMsgBottom" style="display:none;position:relative;left:800px;" ></div> 

<div id="errorDiv_orderHeader" style="color:red;" ></div>
<!--bottom button 'bar' -->
</div>
<s:set name='lastModifiedDateString' value="getLastModifiedDateToDisplay()" />
<s:set name='lastModifiedUserId' value="lastModifiedUserId" />
<s:set name='modifiedBy' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getLoginUserName(#lastModifiedUserId)' />
<div class="clearall">&nbsp;</div>
<div class="last-modified-div sc">
    Last modified by <s:property value="#modifiedBy"/> on <s:property value="#lastModifiedDateString"/> 
</div>
</div>

<!-- START Carousel -->	
<!-- add clearall for proper positioning -->
<div class="clearall">&nbsp;</div>
	
<s:if test='xpedxYouMightConsiderItems.size() > 0'>
	<div class="cart-bg carousel-div" style="margin-top:0px;">
		<div id="cross-sell" class="float-left">
            <span class="promotxt"> You might also consider...</span>
			<ul id="youMightConsiderCarousel" class="jcarousel-skin-xpedx">
			<!-- Begin - Changes made by Mitesh for JIRA 3186 -->
		    <s:if test='xpedxYouMightConsiderItems.size() < 4'>
			    <div disabled="disabled" class="jcarousel-prev jcarousel-prev-hide-horizontal"></div> 
			    <div disabled="disabled" class="jcarousel-next jcarousel-next-hide-horizontal"></div>
		    </s:if>
		    <s:if test='xpedxYouMightConsiderItems.size() > 0'>
				<s:iterator value='xpedxYouMightConsiderItems' id='reltItem' status='iStatus'>
					<s:set name="itemAssetList"
							value='#xpedxSCXmlUtil.getElementsByAttribute(#reltItem,"AssetList/Asset","Type","ITEM_IMAGE_1" )' />
						<s:if test='#itemAssetList != null && #itemAssetList.size() > 0 '>
							<s:set name="itemAsset" value='#itemAssetList[0]' />
							<s:set name='imageLocation'
								value="#xpedxSCXmlUtil.getAttribute(#itemAsset, 'ContentLocation')" />
							<s:set name='imageId'
								value="#xpedxSCXmlUtil.getAttribute(#itemAsset, 'ContentID')" />
							<s:set name='imageLabel'
								value="#xpedxSCXmlUtil.getAttribute(#itemAsset, 'Label')" />
							<!--Removed "/" -->
							<s:set name='imageURL' value="#imageLocation + '/' + #imageId " />
							<s:if test='%{#imageURL=="/"}'>
								<s:set name='imageURL' value='%{"/xpedx/images/INF_150x150.jpg"}' />
							</s:if>
							
							<s:set name='info' value='XMLUtils.getChildElement(#reltItem, "PrimaryInformation")'/>
							<s:set name='shortDesc' value='#info.getAttribute("ShortDescription")'/>
							<!--Jira 2918 - Modified For Image Path -->
							<li><s:a href="javascript:processDetail('%{#reltItem.getAttribute('ItemID')}', '%{#reltItem.getAttribute('UnitOfMeasure')}')"> 
								<img src="<s:url value='%{#imageURL}'/>" title='<s:property value="%{#reltItem.getAttribute('ItemID')}"/>' width="91" height="94" alt="<s:text name='%{#imageMainLabel}'/>" /> <!-- <b><s:property value="%{#reltItem.getAttribute('ItemID')}"/></b> --> <br />
								<s:property value="%{#shortDesc}"/>
								<br />
								<br />
								<br />
								</s:a>  
							</li>
						</s:if> 
						<s:else>								
							<s:set name='imageIdBlank' value='%{"/xpedx/images/INF_150x150.jpg"}' />									
							<s:set name='info' value='XMLUtils.getChildElement(#reltItem, "PrimaryInformation")'/>
							<s:set name='shortDesc' value='#info.getAttribute("ShortDescription")'/>
							<li> 
							    <s:a href="javascript:processDetail('%{#reltItem.getAttribute('ItemID')}', '%{#reltItem.getAttribute('UnitOfMeasure')}')"> <img src="<s:url value='%{#imageIdBlank}'/>" title='<s:property value="%{#reltItem.getAttribute('ItemID')}"/>' width="91" height="94" alt="" /> <!-- <b><s:property value="%{#reltItem.getAttribute('ItemID')}"/></b> --><br />
							    	<span class="short-description"><s:property value="%{#shortDesc}"/></span>
									<br />
									<br />
									<br />
								</s:a> 
							</li>
						</s:else>
						<!-- End - Changes made by Mitesh for JIRA 3186 -->					
				</s:iterator>
			</s:if>		    
	        </ul>
		</div>
	</div>
</s:if>
<!-- END carousel -->

</div>
</div>
</div>
<!-- end main  -->

<!-- end container  -->

<s:include value="XPEDXOrderTotalAdjustments.jsp" />


<div style="display: none;">

<!-- Light Box -->
<div style=" height:202px; width:600px; overflow:auto;">
<!-- START of Hidden Layer -PN --> <!-- CODE_START Replacement items - PN -->
<!-- START: XPEDX Panel for Replacement items --> <s:set name='tabIndex'
	value='3001' /> 
	<s:iterator value='xpedxItemIDUOMToReplacementListMap'>
	<s:set name='altItemList' value='value' />
	
	<div id="replacement_<s:property value='key'/>" class="xpedx-light-box" >
	  <%-- <h2>Replacement Item(s) for <s:property value="wCContext.storefrontId" /> Item #: <s:property value='key'/> </h2> --%> <%-- key contains the original itemId --%>
	  <h2><s:text name='MSG.SWC.ITEM.REPLACEMENT.GENERIC.PGTITLE' /> for <s:property value="wCContext.storefrontId" /> Item #: <s:property value='key'/> </h2><%-- key contains the original itemId --%>
	         <!-- Light Box --><div style=" height:202px; width:580px; overflow:auto;  border:1px solid #CCCCCC;">
		<!--Adding below if condn for Jira 1601 - Error Msg For Replacement Item  -->
		<s:if test="#altItemList.size() == 0">
				<div class="error" style="margin-top:90px; margin-left:80px;">The replacement item is not available, please contact customer service.</div>
		</s:if> 
		<!--Fix End for Jira 1601 - Error Msg For Replacement Item  -->
		<s:iterator value='#altItemList' id='altItem' status='iStatus'>
		<div class="mil-wrap-condensed-container" style="width:100%;">
		<!--  hide in case of one item  -->
            <s:if test="!#iStatus.last" >
				<div class="mil-wrap-condensed-container"  onmouseover="$(this).addClass('green-background');" onmouseout="$(this).removeClass('green-background');" >
			</s:if>
			<s:else>
				<div class="mil-wrap-condensed-container last"  onmouseover="$(this).addClass('green-background');" onmouseout="$(this).removeClass('green-background');" >
			</s:else>
			<div class="mil-wrap-condensed" style="min-height:200px;">
				<s:set name='uId' value='%{key + "_" +#altItem.getAttribute("ItemID")}' />
				
	<!-- begin image / checkbox   -->
                <div class="mil-checkbox-wrap">
                <s:set name='altItemIDUOM'
						value='#_action.getIDUOM(#altItem.getAttribute("ItemID"), #altItem.getAttribute("UnitOfMeasure"))' />
					<s:set name='altItemPrimaryInfo' value='#util.getElement(#altItem, "PrimaryInformation")' /> 
						<s:set name='name' value='%{#altItemPrimaryInfo.getAttribute("ShortDescription")}' />
					<s:set name='extnTag' value='#util.getElement(#altItem, "Extn")' />	
						 <s:set name='extnVendorNo' value='%{#extnTag.getAttribute("ExtnVendorNo")}' />
						 <s:set name='certFlagVal' value='%{#extnTag.getAttribute("ExtnCert")}' />
						<s:set name='rItemID' value='%{#altItem.getAttribute("ItemID")}' /> 
						<s:set name='rdesc' value='%{#altItemPrimaryInfo.getAttribute("Description")}' />
						<s:url id='pImg' value='%{#_action.getImagePath(#altItemPrimaryInfo)}' />						
						
						<s:set name='ritemUomId' value='#altItem.getAttribute("UnitOfMeasure")' />
						<s:set name='ritemType' value='#altItem.getAttribute("ItemType")' />
						
						<s:url id='ritemDetailsLink' namespace="/catalog"
							action='itemDetails.action' includeParams='none' escapeAmp="false">
							<s:param name="itemID" value="#rItemID" />
							<s:param name="sfId" value="#parameters.sfId" />
							<s:param name="unitOfMeasure" value="#ritemUomId" />
						</s:url>
                   <input name="relatedItems"
						onclick="javascript:setUId('<s:property value="#uId" />');"	type="radio" />
                    <div class="mil-question-mark"> 
                               <s:a href="javascript:processDetail('%{#altItem.getAttribute('ItemID')}', '%{#altItem.getAttribute('UnitOfMeasure')}')" >
                                     <img src="<s:property value='%{#pImg}'/>" width="150" height="150" alt="" />
                                 </s:a>
                    </div>
                    <!--  image hardcoded  -->
                </div>
                <!-- end image / checkbox   -->
					<s:hidden name="replacement_%{uId}_itemid" value='%{#rItemID}' /> 
					<s:hidden name="replacement_%{uId}_name" value='%{#name}' /> 
					<s:hidden name="replacement_%{uId}_desc" value='%{#rdesc}' />
					
					<s:set name='altItemUomList' value='itemIdsUOMsDescMap.get(#rItemID)' />
					<s:set name="repItemUOM" value="" />
					<s:iterator value="altItemUomList" id="itemUOM" status="repItemUOMStatus">
						<s:if test="%{#repItemUOMStatus.first}" >
							<s:set name="repItemUOM" value="key" />
						</s:if>
					</s:iterator>
					<s:hidden id="replacement_%{uId}_uom" name="replacement_%{uId}_uom" value="%{#repItemUOM}" />

                <!-- begin description  -->
                <div class="mil-desc-wrap">
                    <div class="mil-wrap-condensed-desc item-short-desc" ><s:if test="%{#ritemType != 99}">
								<a href='<s:property value="%{ritemDetailsLink}" />'>
									<span class="short-description"><s:property value="#name" /></span>
								</a>
							</s:if> 
							<%--<s:if test="%{#itemType != 99}"><a/></s:if> --%>
					</div>
                    <div class="mil-attr-wrap">
                          <ul class="prodlist"><a href='<s:property value="%{ritemDetailsLink}" />'><s:property escape='false'  value='#rdesc'/></a></ul>
					    <%-- key contains the original itemId --%>
					    <!--  <s:text name='MSG.SWC.ITEM.REPLACEMENT.GENERIC.PGTITLE' /> -->
					    <%-- 
                          <p><s:property value="wCContext.storefrontId" /> Item #: <s:property value='key'/></p>
                        <p>Replacement Item #: <s:property value='rItemID' /></p>  --%>
                        
							 <p>xpedx Item #: <s:property value="#rItemID" /> </p> <!--  Since this is replacement Screen replacement Item is nothig but 'Xpedx Item#' -->
								 <s:if test='#certFlagVal=="Y"'>
								 	<img border="none"  src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/catalog/green-e-logo_small.png" alt="" style="margin-left:0px; display: inline;" />
								 </s:if>
								 
                             <p style="margin-top:0px;">Mfg. Item #: <s:property value="#extnVendorNo" /> </p>
						
                    </div>
                    
                </div>
                </div>
                <%--<div class="clearall"> &nbsp; </div>
				 Qty, UOM, PNA added for Cart details removed as per 1357--%>
				</div>
				<%-- --%>                
  			</div>
	 </s:iterator></div></div>
  </s:iterator>   </div>
  

</div>
<s:form action="addComplementaryItemToCart"
	name="addReplacementItemToCartForm" id="addReplacementItemToCartForm"
	namespace="/order" method="POST">
	<div id="replacementItems" style="height: 380px; display: none;">
	
		<s:hidden name='#action.name' id='validationActionName'
			value='addReplacementItemToCart' />
		<s:hidden name='#action.namespace' value='/order' />
	
		<div id="replacementItemBody"  class="xpedx-light-box"/> 
	</div>
	<ul class="tool-bar-bottom" id="tool-bar" style="margin-right:30px;float:right;">
		<li style="float: right;"><a href="javascript:replacementReplaceInList(selReplacementId);" class="orange-ui-btn modal"><span>Replace</span></a></li>
		<li style="float: right; margin-right:5px;"><a href="javascript:replacementAddToList(selReplacementId);" class="grey-ui-btn"><span>Add</span></a></li>
		<li style="float: right;"><a href="javascript:$.fancybox.close();" class="grey-ui-btn"><span>Cancel</span></a></li>
	</ul>
</s:form> 
<s:form id="formRIAddToList" action="draftOrderAddReplacementOrderLines"
	method="post">
	<s:hidden name="orderHeaderKey" value='%{#orderHeaderKey}' />
	<s:hidden name="draft" value='%{#draftOrderFlag}' />

	<s:hidden name="itemId" value="" />
	<s:hidden name="name" value="" />
	<s:hidden name="desc" value="" />
	<s:hidden name="qty" value="" />
	<s:hidden name="jobId" value=" " />
	<s:hidden name="itemType" value="1" />
	<s:hidden name="uomId" value="" />
	<s:hidden name="order" value="" />
	<s:hidden name="addToList" value="true" />
	<s:hidden name="originalQuantity" value="%{#qty}" />
</s:form> 
<s:form id="formRIReplaceInList" action="draftOrderAddReplacementOrderLines"
	method="post">
	<s:hidden name="orderHeaderKey" value='%{#orderHeaderKey}' />
	<s:hidden name="draft" value='%{#draftOrderFlag}' />
	
	<s:hidden name="key" value="" />
	<s:hidden name="itemId" value="" />
	<s:hidden name="name" value="" />
	<s:hidden name="desc" value="" />
	<s:hidden name="qty" value="" />
	<s:hidden name="jobId" value=" " />
	<s:hidden name="itemType" value="1" />
	<s:hidden name="uomId" value="" />
	<s:hidden name="order" value="" />
	<s:hidden name="addToList" value="false" />
	
</s:form>
</div>
<div class="hp-ad"></div>
<!-- END: XPEDX Panel for Replacement items -->

<!-- CODE_END Replacement items - PN-->



<s:set name="editHeaderDialog" scope="page"
	value="#_action.getText('editHeaderDialog')" />
<s:if test="!(#canChangeOrderName || #canChangeCurrency)">
	<s:set name="editHeaderDialog" scope="page"
		value="#_action.getText('viewHeaderDialog')" />
</s:if>
<swc:dialogPanel title="Cart Details"
	isModal="true" id="editHeaderDialog" cssClass="my-class"
	contentID="editCartDetailContent">

	<s:form action="changeOrderHeader" name="OrderHeaderForm"
		id="OrderHeaderForm" namespace="/order" method="POST" validate="true">
		<s:hidden id="action_namespace" name="#action.namespace"
			value="/order" />
		<s:hidden id="actionName" name="#action.name"
			value="changeOrderHeader" />
		<div id="editHeaderDiv" class="xpedx-light-box"
			style="width: 340px; height: 300px;">
		<h2>New Cart</h2>
		<p class="mil-edit-forms-label">Cart Name:</p>
		<s:if test="#canChangeOrderName">
			<s:textfield name='cartName' id="cartName" size="35"
				cssClass="x-input"
				value='%{#orderDetails.getAttribute("OrderName")}' tabindex="3400" />
		</s:if> <s:else>
			<s:property value='%{#orderDetails.getAttribute("OrderName")}' />
		</s:else>
		<p class="mil-edit-forms-label">Description</p>
		<s:if test="#canChangeOrderName">
			<s:textarea name='cartDesc' id="cartDesc" cols="45" rows="5"
				cssClass="x-input"
				value='%{#extnOrderDetails.getAttribute("ExtnOrderDesc")}'
				tabindex="3400" />
		</s:if> <s:else>
			<s:property
				value='%{#extnOrderDetails.getAttribute("ExtnOrderDesc")}' />
		</s:else> <s:if test="#canChangeOrderName">
			<ul id="tool-bar" class="tool-bar-bottom">
				<s:hidden name="OrderHeaderKey" value='%{#orderHeaderKey}' />
				<s:hidden name="draft" value="%{#draftOrderFlag}" />
				<li style="float: left;"><a class="green-ui-btn" href="#"
					onclick="document.forms.OrderHeaderForm.submit();"><span>Save</span></a>
				</li>
			</ul>
		</s:if></div>
	</s:form>
</swc:dialogPanel>
<s:include value="modals/XPEDXDeleteCartModal.jsp" />

<%-- <swc:dialogPanel title="Copy And Paste Quick Add" isModal="true"
	id="copyPasteDialog" cssClass="xpedx-light-box"
	contentID="copyPasteContent">
</swc:dialogPanel> --%>

<%-- 	<div id="dlgCopyAndPaste" class="xpedx-light-box" style="width: 400px; height: 300px; display:none;">
	<h2>Copy and Paste Quick Add</h2>
	<p>Copy and Paste the quantities and <s:property value="wCContext.storefrontId" /> item #'s from your file.
		Or enter manually with quantity and item #, separated by a comma, per line. Example:12,5002121 -bb2-<br />
	</p>
	<br />
	<form id="form1" name="form1" method="post" action=""><textarea
		name="items" id="items" cols="69" rows="5"></textarea></form>
	<ul id="tool-bar" class="tool-bar-bottom">
		<li><a class="grey-ui-btn"
			href="javascript:closeCopyPasteDialog()"><span>Cancel</span></a></li>
			<li style="float: right;"><a href="#" onclick="javascript:addItemsToQuickAddList(); return false;" class="green-ui-btn" style="margin-left:5px;"><span>bb1Add to Quick List</span></a></li>
	</ul>
	</div> --%>
<s:include value="modals/XPEDXCopyCartModal.jsp" />

<div style="display: none;">
<div id="adjustmentsLightBox1"  style="width:600px;height:200px;overflow:auto;">
	<div class="adjustment-body" id='adjustment-body'></div>
</div>
</div>
<a href="#adjustmentsLightBox1" id="adjustmentsLightBox" style="display:none"></a> 

<div class="hidden-data"><s:if test='!#isProcurementUser'>
	<s:a id='checkoutURL' href='%{#checkoutURLid}' />
</s:if> <s:else>
	<s:if test='#isProcurementInspectMode'>
		<s:a id='checkoutURL' href='%{#procurementInspectModeCheckoutURLid}' />
	</s:if>
	<s:else>
		<s:a id='checkoutURL' href='%{#procurementCheckoutURLid}' />
	</s:else>
</s:else> 
<s:url id='addProductsToOrderURLid' namespace='/order' action='draftOrderAddOrderLines' /> 
<s:a id='addProductsToOrderURL'	href='%{#addProductsToOrderURLid}' /> 

<s:url id='updateURLid'	namespace='/order' action='draftOrderModifyLineItems' /> 
<s:a id='updateURL' href='%{#updateURLid}' /> 

<s:url id='updateNoLinesURLid'	namespace='/order' action='draftOrderModifyEmptyLineItems' /> 
<s:a id='updateNoLinesURL' href='%{#updateNoLinesURLid}' /> 

<s:url id='updateNotesURLid' namespace='/order' action='xpedxDraftOrderModifyLineNotes' /> 
<s:a id='updateNotesURL' href='%{#updateNotesURLid}' /> 

<s:url id='removeItemsURLid' namespace='/order'	action='draftOrderDeleteLineItems' /> 
<s:a id='removeItemsURL' href='%{#removeItemsURLid}' /> 

<s:url id='addItemsToListURLid'	namespace='/order' action='xpedxAddItemsToList' /> 
<s:a id='addItemsToListURL' href='%{#addItemsToListURLid}' />

</div>

<script type="text/javascript">
function validateOrder()
{
	var minAmount='<s:property value="#minOrderAmount"/>';
	var chargeAmount='<s:property value="#chargeAmount"/>';
	var totalAmount='<s:property value='#adjustedSubtotalWithoutTaxes' />';
	var totalAmountNum=Number(totalAmount);
	var maxAmount='<s:property value="#maxOrderAmount"/>';//JIRA 3488
	var fmtdMaxOrderAmount='<s:property value='#fmtdMaxOrderAmount' />';//JIRA 3488
	var fmtdMinOrderAmount='<s:property value='#fmtdMinOrderAmount' />';//JIRA 3547
	var fmtdchargeAmount='<s:property value='#fmtdchargeAmount' />';//JIRA 3547
	var erroMsg = '<s:property value='#erroMsg' />';//Added for JIRA 3523
	
	//Added for JIRA 3523
	if(erroMsg != null && erroMsg != ""){
		var divId=document.getElementById("entileErrorMessade");
		var divId1 = document.getElementById("entitleErrorMessageBottom");
		if(divId != null)
		{		
			
			divId.innerHTML="This account is no longer entitled to xpedx item # "+erroMsg+". Please delete it from your cart.";
		}
		if(divId1 != null)
		{
			divId1.innerHTML="This account is no longer entitled to xpedx item # "+erroMsg+". Please delete it from your cart.";
		}
	}
	
	//JIRA 3488 start
	if(maxAmount > 0 && totalAmountNum>maxAmount)
	{
		var divId=document.getElementById("maxOrderErrorMessage");
		var divId1 = document.getElementById("maxOrderErrorMessageBottom");
		if(divId != null)
		{		
			
			divId.innerHTML="Order exceeds allowable maximum of "+fmtdMaxOrderAmount;
		}
		if(divId1 != null)
		{
			divId1.innerHTML="Order exceeds allowable maximum of "+fmtdMaxOrderAmount;
		}
			
	}
	//JIRA 3488 end
	if(minAmount >totalAmountNum)
	{
		var divId=document.getElementById("minOrderErrorMessage");
		var divId1=document.getElementById("minOrderErrorMessageBottom");
		if(divId != null)
		{		
			//Start fix for 3547
			//divId.innerHTML="Order minimum is "+minAmount+". A Penalty of "+chargeAmount+" will be charged.";
			divId.innerHTML="To avoid a Minimum Order Charge of "+fmtdchargeAmount+" at the time of order placement, this order must meet the minimum order amount of " + fmtdMinOrderAmount + "."
		}
		if(divId1 != null)
		{
			divId1.innerHTML="To avoid a Minimum Order Charge of "+fmtdchargeAmount+" at the time of order placement, this order must meet the minimum order amount of " + fmtdMinOrderAmount + ".";
			//divId1.innerHTML="Order minimum is "+minAmount+". A Penalty of "+chargeAmount+" will be charged.";
			//End fix for 3547
		}
	}
}
	function updateValidation(){
		$(".numeric").numeric();	
	}
	updateValidation();
	var isProcurementUser = <s:property value="#wcContext.isProcurementUser()"/>;
	if(!isProcurementUser) {
		validateOrder();
	}

	function actionOnList(oKey){
		if(oKey=="Copy"){
			document.getElementById("copyCartName").value="";
			document.getElementById("copyCartDescription").value="";
			<s:url id="cartActionURL" value="/order/draftOrderCopy.action" />
			document.copyOrder.OrderHeaderKey.value='<s:property value='#ohk' />';
			Ext.MessageBox.hide();
			$('#various2').trigger('click');
		} else if(oKey=="Delete"){
			document.delOrder.OrderHeaderKey.value='<s:property value='#ohk' />';
			$('#xpedxDeleteCartDialog1').trigger('click');
		}
	}
	function openQuickAdd() {
		var isQuickAdd = <s:property value="isQuickAdd()" />;
		if(isQuickAdd) {
				 var offset = $(document.getElementById('quick-add-button')).offset();
				 $('#tq-quick-add-overlay').css({ top: offset.top+35 });
				 $('#tq-quick-add-overlay').toggle();
				 return false;
				}
		}
	openQuickAdd();
</script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx.swc.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery-ui.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/common/XPEDXUtils<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified<s:property value='#wcUtil.xpedxBuildKey' />.js"
	type="text/javascript" charset="utf-8"></script>
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bgiframe.min<s:property value='#wcUtil.xpedxBuildKey' />.js"
	type="text/javascript" charset="utf-8"></script>
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/order/XPEDXDraftOrderDetails<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/order/XPEDXOrder<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/order/XPEDXItemAssociation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/orderAdjustment<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jqdialog/jqdialog<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
</body>
</html>