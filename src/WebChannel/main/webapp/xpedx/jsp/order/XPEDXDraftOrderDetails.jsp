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
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/ORDERS.css" />
<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE.css" />
<![endif]-->
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
<!--  End Styles -->

<!-- javascript -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header.js"></script>
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/ajaxValidation.js"></script>


<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/swc.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel.js"></script>
-->

<!-- carousel scripts js   -->
 <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-jquery-headder.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-header.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.numeric.js"></script>

<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add.js"></script>

-->
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.tabs.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.shorten.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/modals/checkboxtree/jquery.checkboxtree.js"></script>
-->

<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/DD_roundies_0.0.2a-min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pseudofocus.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global-xpedx-functions.js"></script>
-->
<!-- STUFF YOU NEED FOR BEAUTYTIPS -->


<!--[if IE]>
<script src="../other_libs/excanvas_r3/excanvas.js" type="text/javascript" charset="utf-8"></script>
<![endif]-->


<s:hidden name="uomDescriptionURL" value='%{#uomDescriptionURL}'/>
<!-- /STUFF -->

<!-- Lightbox/Modal Window -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4.js"></script>

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
		}
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
					var shortHTML = html.substring(0, 40);
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
<h2>Copy and Paste</h2>
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
<a href="javascript:window.print()"><span class="print-ico-xpedx"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/print-icon.gif" width="16" height="15" alt="Print Page" /><span class="underlink">Print Page</span></span></a>
</div>

<div id="mid-col-mil">

<div class="mil-edit">

<div class="float-right">
	<!-- promotion -->
	<div class="ad-margin">
		<!-- ad placeholder, per the mockup. Ad Juggler Starts -->
		<div class="float-none ad-float smallBody"><img height="4" width="7" class="ad-img" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/mil/ad-arrow.gif" alt="advertisement" />advertisement</div>
				<s:iterator value='majorLineElements' id='orderLine' status="rowStatus">
				<s:set name='item' value='#util.getElement(#orderLine, "Item")' />
				<s:if test="%{#rowStatus.index == 0}">				
				 <s:set name="ItemID1" value='#item.getAttribute("ItemID")' />
					 <s:if test="#ItemID1 != null" >
							<s:set name="cat2Val" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getCatTwoDescFromItemId(#ItemID1,wCContext.storefrontId)" />
								<s:if test="#cat2Val != null" >
								<s:set name='ad_keyword' value='#cat2Val' />
					 </s:if>
					</s:if>	
				</s:if>
				</s:iterator>
				
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

<!--for selected items fieldset 
<fieldset class="mil-edit-field">
    <legend>For Selected Items:</legend>
    <input class="forselected-input-edit" type="checkbox" id="selAll2" />
    <a class="grey-ui-btn float-left" href="javascript:removeItems();"><span>Remove Items</span></a>
</fieldset>
 end fieldset-->



	<br />
	
	<div class="mil-wrap-condensed-container">

	<s:form action="draftOrderDetails" cssClass="" name="OrderDetailsForm"
		id="OrderDetailsForm" namespace="/order" method="POST" validate="true">

	<s:hidden name='#action.name' id='validationActionName'
		value='draftOrderDetails' />
	<s:hidden name='#action.namespace' value='/order' />
	<s:hidden name="orderHeaderKey" value='%{#orderHeaderKey}' />
	<s:hidden name="draft" value="%{#draftOrderFlag}" />
	<s:hidden name='Currency' value='%{#currencyCode}' />
	<s:hidden name='mode' value='%{#mode}' />
	<s:hidden name='fullBackURL' value='%{#returnURL}' />
	<s:hidden name="orderLineKeyForNote" id="orderLineKeyForNote" value="" />
	<s:hidden name="listKey" id="listKey" value="" />
	<s:hidden name="orderLineItemOrders" value="" />
	<s:hidden id="orderName" name="orderName" value="" />
	<s:hidden id="orderDesc" name="orderDesc" value="" />
	<s:hidden id="OrderLinesCount" name="OrderLinesCount" value='%{majorLineElements.size()}' />
	<s:hidden id="zeroOrderLines" name="zeroOrderLines" value='false' />
	<s:hidden id="isPNACallOnLoad" name="isPNACallOnLoad" value='false' />
	<%-- Removing changeOrder call for Performance improvement, while checkout --%>
	<s:hidden id="isComingFromCheckout" name="isComingFromCheckout" value='false' />
	<s:hidden id="modifyOrderLines" name="modifyOrderLines" value='false' />
	<input type="hidden" value='<s:property value="%{chargeAmount}" />' name="chargeAmount" />
	<input type="hidden" value='<s:property value="%{minOrderAmount}" />' name="minOrderAmount" />
	<%-- 
	I don't see this variable used in this jsp so removing it . In case if any one wants to use getUOMDescriptions method please use
	XPEDXWCUtils.getUOMDescription .
	<s:set name="uomMap" value='#util.getUOMDescriptions(#wcContext,true)' />
	 --%>
	
	<s:set name="xpedxItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_ITEM_LABEL"/>
	<s:set name="customerItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CUSTOMER_ITEM_LABEL"/>
	<s:set name="manufacturerItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MANUFACTURER_ITEM_LABEL"/>
	<s:set name="mpcItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MPC_ITEM_LABEL"/>
	<s:set name="myPriceValue" value="%{'false'}" />
	
    <table class="mil-top-border" border="0px solid red" class="float-right">
	   <tr  class="table-header-bar">
		<td class="text-right white table-header-bar-left" > My Price (<s:property value='#currencyCode'/>) </td>
		<td class="text-right white pricing-border mill-container-extended-pricing table-header-bar-right" > Extended Price (<s:property value='#currencyCode'/>) &nbsp;</td>
	   </tr>
	</table>
	
	<%--<s:set name="subTotal" value='%{0.00}' /> --%>
	<input type="hidden" name="isEditOrder" value="<s:property	value='%{(#_action.getIsEditOrder())}' escape="false" />"/>
	<s:iterator value='majorLineElements' id='orderLine' status="rowStatus"  >
		<s:set name='lineTotals' value='#util.getElement(#orderLine, "LineOverallTotals")' />
		<s:set name='item' value='#util.getElement(#orderLine, "Item")' />
		<s:set name='lineTran' value='#util.getElement(#orderLine, "OrderLineTranQuantity")' />
		<s:set name='itemDetails' value='#util.getElement(#orderLine, "ItemDetails")' />
		<s:set name='lineNotes' value='#util.getElement(#orderLine, "Instructions/Instruction")' />
		<s:set name='primaryInfo' value='#util.getElement(#itemDetails, "PrimaryInformation")' />
		<s:set name='itemExtnEle' value='#util.getElement(#itemDetails, "Extn")' />
		<s:set name="imageLocation" value="#xpedxSCXmlUtil.getAttribute(#primaryInfo,'ImageLocation')" />
		<s:set name='imageID' value='#primaryInfo.getAttribute("ImageID")' />
		<s:set name='certFlag' value='#itemExtnEle.getAttribute("ExtnCert")' />
		<s:set name='orderLineKey' value='#orderLine.getAttribute("OrderLineKey")' />
		<s:set name='orderLineType' value='#orderLine.getAttribute("LineType")' />
		<s:set name='kitLines' value='#util.getElement(#orderLine, "KitLines")' />
		<s:set name='lineExtn' value='#util.getElement(#orderLine, "Extn")' />
		<s:set name='itemIDUOM' value='#_action.getIDUOM(#item.getAttribute("ItemID"), #item.getAttribute("UnitOfMeasure"))' />
		<s:set name='canCancel' value='#_action.isOrderLineModificationAllowed(#orderLine, "CANCEL")' />
		<s:set name='canChangeShipNode' value='#_action.isOrderLineModificationAllowed(#orderLine, "SHIP_NODE")' />
		<s:set name='canChangeDeliveryCode' value='#_action.isOrderLineModificationAllowed(#orderLine, "DELIVERY_CODE")' />
		<s:set name='canChangeLineOrderDate' value='#_action.isOrderLineModificationAllowed(#orderLine, "CHANGE_ORDER_DATE")' />
		<s:set name='canChangeLineReqShipDate' value='#_action.isOrderLineModificationAllowed(#orderLine, "REQ_SHIP_DATE")' />
		<s:set name='canChangeBundleDefinition' value='#_action.isOrderLineModificationAllowed(#orderLine, "CHANGE_BUNDLE_DEFINITION")' />
		<s:set name='isSpecialLine' value='#_action.isSpecialLine(#orderLine)' />
		<s:set name="jsonKey" value='%{#item.getAttribute("ItemID")+"_"+#orderLine.getAttribute("PrimeLineNo")}' />
		<s:set name="json" value='pnaHoverMap.get(#jsonKey)' />
		<s:set name="displayPriceForUoms" value='%{""}' />
		<s:set name="priceInfo" value='priceHoverMap.get(#jsonKey)' />
		<s:if test="%{#priceInfo!=null}" >
			<s:set name="displayPriceForUoms" value='%{#priceInfo.getDisplayPriceForUoms()}' />
		</s:if>
		<s:include value='XPEDXOrderLineTotalAdjustments.jsp' />
		<s:hidden name="orderLineKeys" id="orderLineKeys_%{#orderLineKey}" value="%{#orderLineKey}" />
		<s:hidden name="orderLineItemIDs" id="orderLineItemIDs_%{#orderLineKey}" value='%{#item.getAttribute("ItemID")}' />
		<s:hidden name="orderLineItemNames" id="orderLineItemNames_%{#orderLineKey}" value='%{#item.getAttribute("ItemShortDesc")}' />
		<s:hidden name="orderLineItemDesc" id="orderLineItemDesc_%{#orderLineKey}" value='%{#item.getAttribute("ItemDesc")}' />
		<s:set name='catalogItem' value='#item' />
		<s:include value="XPEDXCatalogDetailURL.jsp" />
		<s:set name='itemIDVal' value='%{#item.getAttribute("ItemID")}' />
		<s:set name="mulVal" value='itemOrderMultipleMap[#itemIDVal]' />
		<s:hidden name="orderLineOrderMultiple" id="orderLineOrderMultiple_%{#orderLineKey}" value="%{#mulVal}" />
		<s:hidden name="minLineQuantity" id="minLineQuantity_%{#orderLineKey}" value="%{#_action.getMinimumLineQuantity(#orderLine)}" />
		<s:hidden name="maxLineQuantity" id="maxLineQuantity_%{#orderLineKey}" value="%{#_action.getMaximumLineQuantity(#orderLine)}" />
		<s:set name="uom" value='%{#lineTran.getAttribute("TransactionalUOM")}' /> 
				<s:set name="MultiUom" value='%{#item.getAttribute("UnitOfMeasure")}' />
				<s:set name="BaseUOMs" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#MultiUom)' /> 
				<s:hidden name="BaseUOMs" id="BaseUOMs_%{#orderLineKey}" value='%{#BaseUOMs}' /> 
				
		<s:hidden name="selectedCustomerContactId" id="selectedCustomerContactId" value="" />
		
		<%-- 
		<s:set name="itemuomMap" value='itemUOMsMap[#itemIDUOM]' /> 
		<s:set name="displayUomsMap" value='#_action.getDisplayItemUOMsMap()' />
		<s:set name="displayUomMap" value='#displayUomsMap[#itemIDUOM]' />
		<s:hidden name="itemUOMs" id="itemUOMs_%{#orderLineKey}" value='%{#uom}' /> 
		<s:set name="convF" value='#itemuomMap[#uom]' />
		<s:hidden name="UOMconversion" id="UOMconversion_%{#orderLineKey}" value="%{#convF}" />
		--%>
		
		<s:set name="editOrderOrderExtn" value='%{""}' />
		<s:set name="editOrderOrderLineExtn" value='%{""}' />
		<s:if test='%{#editOrderFlag == "true" || #editOrderFlag.contains("true")}'>
			<s:set name="editOrderOrder" value='%{#_action.getEditOrderOrderMap().get(#orderHeaderKey)}' />
			<s:set name="editOrderOrderExtn" value='#util.getElement(#editOrderOrder, "Extn")' />
			<s:set name="editOrderOrderLine" value='%{#_action.getEditOrderOrderLineMap().get(#orderLineKey)}' />
			<s:set name="editOrderOrderLineExtn" value='#util.getElement(#editOrderOrderLine, "Extn")' />
		</s:if>
		 
		<s:set name="itemuomMap" value='itemIdConVUOMMap[#itemIDVal]' />
		<s:set name="displayUomMap" value='itemIdsUOMsDescMap[#itemIDVal]' />
		<s:hidden name="itemUOMs" id="itemUOMs_%{#orderLineKey}" value='%{#uom}' /> 
		<s:set name="convF" value='#itemuomMap[#uom]' />
		<s:hidden name="UOMconversion" id="UOMconversion_%{#orderLineKey}" value="%{#convF}" />
		<s:if test='%{#editOrderFlag == "true" || #editOrderFlag.contains("true")}'>
			<s:if test='%{#lineExtn.getAttribute("ExtnEditOrderFlag") == "Y" || #lineExtn.getAttribute("ExtnEditOrderFlag") =="true"}'>
				<s:set name="isUOMAndInstructions" value="%{false}" />
			</s:if>
			<s:else>
				<s:set name="isUOMAndInstructions" value="%{true}" />
			</s:else>
		</s:if>
		<s:else>
			<s:set name="isUOMAndInstructions" value="%{#isReadOnly}" />
		</s:else>

		
		<!-- begin iterator -->       
		<s:if test="#rowStatus.last == true ">
	    	<div class="mil-wrap last" onmouseout="$(this).removeClass('green-background');" onmouseover="$(this).addClass('green-background');">
	    </s:if>
	    <s:else>
	    	 <div class="mil-wrap" onmouseout="$(this).removeClass('green-background');" onmouseover="$(this).addClass('green-background');">
	    </s:else>
         	<div class="mil-container" >
                <!--checkbox   -->
                <div class="mil-checkbox-wrap">
                     <%-- <s:checkbox name='selectedLineItem' id='selectedLineItem_%{#orderLineKey}' fieldValue='%{#orderLineKey}' disabled='%{!#canCancel}' tabindex="%{#tabIndex}" /> --%>                    
                   <s:checkbox name='selectedLineItem' id='selectedLineItem_%{#orderLineKey}' cssStyle="display:none" onclick="checkHiddenCheckboxAndDeleteItem(this, 'selectedLineItem_%{#orderLineKey}')" fieldValue='%{#orderLineKey}' disabled='%{!#canCancel}' tabindex="%{#tabIndex}" />
                   <s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
                   	<img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_red_x.png" onclick="javascript:checkHiddenCheckboxAndDeleteItem(this,&#39;<s:text name='selectedLineItem_%{#orderLineKey}'/>&#39; );" title="Remove" alt="RemoveIcon" />
                   </s:if> 
                  </div>
                <!-- end checkbox   -->
                        
                <!-- begin description  -->
                <s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
                <s:a href="javascript:processDetail('%{#item.getAttribute('ItemID')}', '%{#item.getAttribute('UnitOfMeasure')}')" >
	                <div class="mil-desc-wrap">
	                    <div class="mil-wrap-condensed-desc item-short-desc" style="max-height:59px; height: auto;"> 
	
							<span class="short-description">
							<s:if test='#item.getAttribute("ItemShortDesc") == ""'>
								<s:property escape='false'	value='%{#item.getAttribute("ItemDesc")}' />
							</s:if>
							<s:else>
								<s:property escape='false'	value='%{#item.getAttribute("ItemShortDesc")}' />
							</s:else></span>
						</div>
		                <div class="mil-attr-wrap">
							<s:if test='#item.getAttribute("ItemDesc") != ""'>
								<ul class="prodlist">
									<s:property escape='false'	value='%{#item.getAttribute("ItemDesc")}' />
								</ul>
							</s:if>							
		                </div>
					</div>
				 </s:a>
				</s:if>
				<s:else>
	                <div class="mil-desc-wrap">
	                    <div class="mil-wrap-condensed-desc item-short-desc" style="max-height:59px; height: auto;"> 
	
							<span class="short-description_M">
							<s:if test='#item.getAttribute("ItemShortDesc") == ""'>
								<s:property escape='false'	value='%{#item.getAttribute("ItemDesc")}' />
							</s:if>
							<s:else>
								<s:property escape='false'	value='%{#item.getAttribute("ItemShortDesc")}' />
							</s:else></span>
						</div>
		                <div class="mil-attr-wrap">
		                    <s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
								<s:if test='#item.getAttribute("ItemDesc") != ""'>
									<ul class="prodlist">
										<s:property escape='false'	value='%{#item.getAttribute("ItemDesc")}' />
									</ul>
								</s:if>
							</s:if>
		                </div>
					</div>
				</s:else>
				
				<!-- Disable the fields for line type C -->
				<s:if test='(#orderLine.getAttribute("LineType") =="C" || #orderLine.getAttribute("LineType") =="M" 
						<%-- || (!#_action.isDraftOrder() && (!#_action.getIsEditOrder().contains("true") ))--%>
					)'>
					<s:set name="disblForLnTypCOrNonDrftOdr" value="%{true}"></s:set>
				</s:if>
				<s:else>
					<s:set name="disblForLnTypCOrNonDrftOdr" value="%{false}"></s:set>
				</s:else>
				<!-- end description -->
                <s:if test="!#isReadOnly">
                	<s:set name="isReadOnly" value="#disblForLnTypCOrNonDrftOdr"></s:set>
                </s:if>
                <!-- Disable the fields for line type C -->
                
                <s:set name='qty' value='#lineTran.getAttribute("OrderedQty")' />
				<s:set name='qty' value='%{#strUtil.replace(#qty, ".00", "")}' />
				<div class="cart-availability-section">
	            	<table width="100%" cellspacing="0" cellpadding="0" border="0px solid red" class="mil-config availability-table">
	                	<tbody>
	                    	<tr>
								<td class="text-right" style="padding:0px;">
								<s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
									<label style="font-size:12px">Qty: </label>
								</s:if>
								<s:if test='#isReadOnly'>
								  <s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
									<s:textfield name='tempOrderLineQuantities'
									theme="simple" id="tempOrderLineQuantities_%{#orderLineKey}" size='1'
									cssClass="mil-action-list-wrap-qty-label" value='%{#qty}'
									disabled='%{#isReadOnly}' tabindex="%{#tabIndex}" onkeyup="javascript:isValidQuantityRemoveAlpha(this,event);" maxlength="7"/>
								 </s:if>
									<s:hidden name="orderLineQuantities" id="orderLineQuantities_%{#orderLineKey}" value='%{#qty}' />
								</s:if>
								<s:else>
								  <s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
									<s:textfield name='orderLineQuantities'
									theme="simple" id="orderLineQuantities_%{#orderLineKey}" size='1'
									cssClass="mil-action-list-wrap-qty-label" value='%{#qty}'
									disabled='%{#isReadOnly}' tabindex="%{#tabIndex}" onkeyup="javascript:isValidQuantityRemoveAlpha(this,event);" maxlength="7"/>
								  </s:if>
								  <s:else>
								  		<s:hidden name="orderLineQuantities" id="orderLineQuantities_%{#orderLineKey}" value='%{#qty}' />
								  </s:else>
								</s:else>
									<s:set name='tabIndex' value='%{#tabIndex + 1}' />
									<s:hidden name="#qaQuantity.type" value="OrderedQty" />
									<s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
										  <s:select name="itemUOMsSelect" id="itemUOMsSelect_%{#orderLineKey}"
											cssClass="xpedx_select_sm mil-action-list-wrap-select" onchange="javascript:setUOMValue(this.id,'%{#_action.getJsonStringForMap(#itemuomMap)}')" 
											list="#displayUomMap" listKey="key" listValue='value'
											disabled="#isUOMAndInstructions" value='%{#uom}' tabindex="%{#tabIndex}" theme="simple"/>
									</s:if>
									<s:if test='#isUOMAndInstructions'>
										<s:hidden name="itemUOMsSelect" id="itemUOMsSelect_%{#orderLineKey}" value='%{#uom}' />
									</s:if>
                         		</td>
						 	</tr>
						 	<tr>
						 		<td>
						 		<br/>
						 		<s:if test='%{#mulVal >"1" && #mulVal !=null}'>
						 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<div class="notice" id="errorDiv_orderLineQuantities_<s:property value='%{#orderLineKey}' />" style="display : inline;position:absolute;font-size:12px;"><s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> <s:property value="%{#xpedxutil.formatQuantityForCommas(#mulVal)}"></s:property>&nbsp;<s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#MultiUom)'></s:property><br/></div>
						 		</s:if>
						 		</td>
						 	</tr>
					 	</tbody>
				 	</table>
				 <%--	Using CustomerContactBean object from session
				 <s:if test='%{#session.viewPricesFlag == "Y"}'>
				 --%>
				 <s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
				 	<s:if test="#rowStatus.first == true ">
				 		<table class="float-right pricing-table" style="font-size:12px">
				 	</s:if>
				 	<s:else>
				 		<table class="float-right pricing-table" style="font-size:12px">
				 	</s:else>
				 	<s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
				 	
						<tbody>
							<%-- 
				  	  		<tr>
				  	  			<td class="text-right" width="147">
				  	  			<s:if test='#orderLine.getAttribute("LineType") =="C" || #orderLine.getAttribute("LineType") =="M" '>
					 				TBD
					 			</s:if>
					 			<s:else>
									<s:property value='#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, #json.get("UnitPricePerRequestedUOM"))' />
								</s:else>
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
                        	<tr>
            	            	<td class="text-right">
            	            	<s:if test='#orderLine.getAttribute("LineType") =="C" || #orderLine.getAttribute("LineType") =="M" '>
					 				&nbsp;
					 			</s:if>
					 			<s:else>
									/&nbsp;<s:property value="#XPEDXWCUtils.getUOMDescription(#json.get('UOM'))" />
								</s:else>
								</td>
                    	    </tr>
                        	<s:if test='#orderLine.getAttribute("LineType") !="C"'>
					 			<s:if test="#displayPriceForUoms.size()>0" >
					 			<s:set name="pricingUOM" value="#json.get('PricingUOM')" />
					 			
					 			<s:set name="displayIndex" value="1" />
					 			<s:if test="#pricingUOM == 'A_CWT' || #pricingUOM == 'M_CWT' || #pricingUOM == 'M_M' || #pricingUOM == 'A_M' ">
					 				<s:set name="displayIndex" value="2" />
					 			</s:if>
					 			<s:iterator value='displayPriceForUoms' id='disUOM' status='disUOMStatus'>
					 				<s:set name="bracketPriceForUOM" value="bracketPrice" />
									<s:set name="bracketUOMDesc" value="bracketUOM" />
									
					 				<s:if test='%{#disUOMStatus.index < #displayIndex && (#XPEDXWCUtils.getUOMDescription(#json.get("UOM")) != #bracketUOMDesc) }' >
									
										<tr>
				                        	<td class="text-right">
												<s:property	value='#bracketPriceForUOM' />
											</td>
		    	                    	</tr>
			        	                <tr>
			            	            	<td class="text-right">
												/&nbsp;<s:property value="#bracketUOMDesc" />
											</td>
			                	            <td></td>
			                    	    </tr>
			                    	    </s:if>
								</s:iterator>
								
	                    	    </s:if>
					 		</s:if> 
					 		--%>
					 		
					 		<s:set name="isMyPriceZero" value="%{'false'}" />
					 	
					 		<s:set name="break" value="false"></s:set>
					 		<s:if test="#displayPriceForUoms!=null && #displayPriceForUoms.size()>0" >
					 			<s:iterator value='#displayPriceForUoms' id='disUOM' status='disUOMStatus'>
					 				<s:set name="bracketPriceForUOM" value="bracketPrice" />
									<s:set name="bracketUOMDesc" value="bracketUOM" />
									
									<s:if test='%{!#disUOMStatus.last}' >
										<s:if test='%{#disUOMStatus.first}' >
											<tr>
											<s:if test="%{#break == false}">
								  	  			<td class="text-right" width="130">
								  	  			<s:if test='#orderLine.getAttribute("LineType") =="C" || #orderLine.getAttribute("LineType") =="M" '>
									 				
									 			</s:if>
									 			<s:else>
									 			
									 			  <s:set name="priceWithCurrencyTemp1" value='%{#xpedxutil.formatPriceWithCurrencySymbolWithPrecisionFive(wCContext, #currencyCode, "0")}' />
									 			  <s:if test="%{#bracketPriceForUOM == #priceWithCurrencyTemp1}">
									 			    	<s:set name="myPriceValue" value="%{'true'}" />
									 			    	
														<span class="red bold"> <s:text name='MSG.SWC.ORDR.ORDR.GENERIC.CALLFORPRICE' /></span>  
														<s:set name="break" value="true"></s:set>
												  </s:if>
												  <s:else>
												  <s:if test="%{#bracketPriceForUOM != #priceWithCurrencyTemp1}">
												    <s:property value="#bracketPriceForUOM" /><br/>
													per&nbsp;<s:property value="#bracketUOMDesc" /></s:if>
												  </s:else>
												</s:else>
												</td></s:if>
				                            	<td class="text-right" width="147" valign="top">
					                            	<span class="mil-action-list-wrap-num-span">
					                            	<s:set name= 'extendedPrice'  value='#util.formatPriceWithCurrencySymbol(wCContext, #currencyCode,#priceUtil.getLineTotal(#lineExtn.getAttribute("ExtnExtendedPrice"),"1","0"))' />
					                            	<s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
					                            	<s:if test='#orderLine.getAttribute("LineType")=="C"'>
														
													</s:if>
													<s:else>
														<%-- <s:if test='%{#editOrderFlag == "true"}'>
															<s:property value='#util.formatPriceWithCurrencySymbol(wCContext, #currencyCode,#priceUtil.getLineTotal(#editOrderOrderLineExtn.getAttribute("ExtnExtendedPrice"),"1","0"))' />
														</s:if>
														<s:else> --%>														  
											 			  <s:if test="%{#extendedPrice == #priceWithCurrencyTemp}">											 			  
																<span class="red bold"><s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>
														  </s:if>
														  <s:else>
														   		<s:property value='#util.formatPriceWithCurrencySymbol(wCContext, #currencyCode,#priceUtil.getLineTotal(#lineExtn.getAttribute("ExtnExtendedPrice"),"1","0"))' />
														  </s:else>
														<%-- 	
														</s:else>
														--%>
													</s:else>
													</span>
												</td>
				                        	</tr>
				                        	<tr><td>&nbsp;</td></tr>											
				                    	 </s:if>
				                    	 <s:else>										
				                    	 	<tr>
					                        	<td class="text-right">
					                        		<s:if test='#orderLine.getAttribute("LineType") =="C" || #orderLine.getAttribute("LineType") =="M" '>
										 				&nbsp;
										 			</s:if>
										 			<s:else>
										 				<s:if test="%{#break == false}">
										 			    	<s:property	value='#bracketPriceForUOM' />
													    </s:if>
										 			</s:else>					                        														  
												</td>
			    	                    	</tr>
				                    	 <tr>
			            	            	<td class="text-right">
			            	            	<s:if test='#orderLine.getAttribute("LineType") =="C" || #orderLine.getAttribute("LineType") =="M" '>
								 				&nbsp;
								 			</s:if>
								 			<s:else>									 			
												<s:if test="%{#break == false}">
													per&nbsp;<s:property value="#bracketUOMDesc" />
												</s:if>											
											</s:else>
											</td>											 
			                    	    </tr>
			                    	    <!--  Add empty space between each price / UOM  -->
			                    	    <tr><td>&nbsp;</td></tr>										
				                    	</s:else>
		                    	    </s:if>
			                    	   
					 			</s:iterator>
					 		</s:if>
						</tbody>
				 	</table>
				 </s:if>
				 	<s:if test='#orderLine.getAttribute("LineType") =="C" || #orderLine.getAttribute("LineType") =="M" '>
						<s:set name="calculatedLineTotal" value='{0}' />
					</s:if>
					<s:else>
							<s:set name="calculatedLineTotal" value='#priceUtil.getLineTotal(#json.get("UnitPricePerRequestedUOM"),#qty,#lineTotals.getAttribute("DisplayLineAdjustments"))' />
							<s:set name='deliveryMethod' value='#orderLine.getAttribute("DeliveryMethod")' />
							<s:set name='deliveryMethods' value='deliveryMethodHelper.getDeliveryMethodsForLine(#orderLine)' />
							
							<!-- commenting delivery methods code - to decide where to put this on the screen -->
							<%-- <br />
							<s:if test='!#isProcurementUser'>
								<s:if test='#deliveryMethods.size() > 0'>
									<s:select name="selectedDeliveryMethods"
										id="selectedDeliveryMethods_%{#orderLineKey}"
										list="deliveryMethods"
										disabled="%{!(#canChangeOrderDate && #canChangeShipNode && #canChangeDeliveryCode && #canChangeLineOrderDate && #canChangeLineReqShipDate)}"
										value='%{#deliveryMethod}'
										tabindex="%{#tabIndexForDeliveryMethod}" />
								</s:if>
								<s:else>
									<s:property value='deliveryMethodHelper.allDeliveryMethods[#deliveryMethod]' />
								</s:else>
							</s:if>--%>
							<s:if test="(!(#canChangeOrderDate && #canChangeShipNode && #canChangeDeliveryCode && #canChangeLineOrderDate && #canChangeLineReqShipDate)) || (#deliveryMethods.size() == 0) || (#isProcurementUser)">
								<s:hidden name="selectedDeliveryMethods" id="selectedDeliveryMethods_%{#orderLineKey}" value="%{#deliveryMethod}" />
							</s:if> 
							<s:hidden name="originalDeliveryMethods" id="originalDeliveryMethods_%{#orderLineKey}" value="%{#deliveryMethod}" />
							<s:hidden name="canChangeStore" id="canChangeStore_%{#orderLineKey}" value="%{(#canChangeOrderDate && #canChangeShipNode && #canChangeLineOrderDate && #canChangeLineReqShipDate)}" />
					</s:else>
				 	<%-- 
				 	<s:if test='( #json.get("UnitPricePerRequestedUOM") != null && #json.get("UnitPricePerRequestedUOM") != "")'> 
                           <s:set name="subTotal" value='%{#subTotal+#calculatedLineTotal}' />
                 	</s:if>
				 	--%>
				 	<s:if test="(#json.get('Immediate') == null)">
						<s:set name="jsonImmediate" value="'0'" />
					</s:if>
					<s:else>
						<s:set name="jsonImmediate" value="#json.get('Immediate')" />
					</s:else>
					
					<s:set name="jsonImmediate" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonImmediate)"/>
					<s:set name="jsonCommaFmtImmediate" value='#xpedxUtilBean.formatQuantityForCommas( #jsonImmediate )' />
 
					
					<s:if test="(#json.get('NextDay') == null)">
						<s:set name="jsonNextDay" value="'0'" />
					</s:if>
					<s:else>
						<s:set name="jsonNextDay" value="#json.get('NextDay')" />
					</s:else>
					
					<s:set name="jsonNextDay" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonNextDay)"/>
					<s:set name="jsonFmtNextDay" value='#xpedxUtilBean.formatQuantityForCommas( #jsonNextDay )' />
					
					<s:if test="(#json.get('TwoPlusDays') == null)">
						<s:set name="jsonTwoPlus" value="'0'" />
					</s:if>
					<s:else>
						<s:set name="jsonTwoPlus" value="#json.get('TwoPlusDays')" />
					</s:else>
					
					<s:set name="jsonTwoPlus" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonTwoPlus)"/>
					<s:set name="jsonFmtTwoPlus" value='#xpedxUtilBean.formatQuantityForCommas( #jsonTwoPlus )' />
					
					<s:set name="jsonUOM" value="#json.get('UOM')" />
					<s:set name="jsonUOMDesc" value="#XPEDXWCUtils.getUOMDescription(#jsonUOM)"/>
					<s:set name="jsonAvailability" value="#json.get('Availability')" />
					<s:set name="jsonTotal" value="#json.get('Total')" />
					<s:set name="jsonImage1" value="#XPEDXWCUtils.getImage('Immediate')" />
					<s:set name="jsonImage3" value="#XPEDXWCUtils.getImage('TwoPlusDays')" />
					<s:set name="jsonImage2" value="#XPEDXWCUtils.getImage('NextDay')" />
					<s:set name="divName" value="#_action.getDivisionName()" />
					<s:set name="stateCode" value="#_action.getState()" />
					<s:if test="(#stateCode == '')">
						<s:set name='StateCode' value="'&nbsp;'" />
					</s:if>
					<s:else>
						<s:set name='StateCode' value='#stateCode' />
					</s:else>
					<s:if test="(#divName == '')">
						<s:set name='DivisionName' value="'&nbsp;'" />
					</s:if>
					<s:else>
						<s:set name='DivisionName' value='#divName' />
					</s:else>
					<s:set name="jsonTotal" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDecimalQty(#jsonTotal)"/>
					<s:set name="jsonFmtTotal" value='#xpedxUtilBean.formatQuantityForCommas( #jsonTotal )' />
					
					
					<%-- <s:if test="(#jsonTotal != null)"> --%>
					
				     	<div class="cart-availability text-left">
				     	 <div id="errorDiv_orderLineQuantities_<s:property value='%{#orderLineKey}' />"></div> 
				     	 <s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
					 		<table  cellspacing="0" cellpadding="0" border="0px solid red" class="mil-config" style="font-size:12px">
						    	<tbody>
						    		<tr>
										<td><strong><p class="bold left" style="width:110px">Total Available: </p></strong></td>
										<td class="text-right"><strong>${jsonFmtTotal} </strong></td>
										<td class="text-left"><strong>&nbsp;${jsonUOMDesc}</strong></td>
						    		</tr>
						    		<tr>
										<td><p class="availability-indent">Next Day: </p></td>
										<td class="text-right"><p> ${jsonFmtNextDay} </p></td>
										<td class="text-left">&nbsp;<%-- ${jsonUOMDesc} --%></td>									
						    		</tr>
						    		<tr>
										<td><p class="availability-indent">2+ Days: </p></td>
										<td class="text-right"><p> ${jsonFmtTwoPlus} </p></td>
										<td class="text-left">&nbsp;<%-- ${jsonUOMDesc} --%></td>
						    		</tr>
						    		<%-- <s:if test="(#divName != null)"> --%>
						    		<tr>
										<%-- <td colspan="3"><p class="italic">${jsonImmediate} available today at ${DivisionName}</p></td> --%>
										<td colspan="3"><p class="italic">${jsonCommaFmtImmediate} &nbsp;${jsonUOMDesc}&nbsp;available today at ${DivisionName}</p></td> 
								    </tr>
								    <%-- </s:if> --%>
							    </tbody>
					    	</table>
					    	</s:if>
				    	</div>
			    	
			    	
			    	<s:if test='(xpedxItemIDUOMToComplementaryListMap.containsKey(#itemIDUOM))'>
						<a href='javascript:showXPEDXComplimentaryItems("<s:property value="#itemIDUOM"/>", "<s:property value="#orderLineKey"/>", "<s:property value="#orderLine.getAttribute('OrderedQty')"/>");'
							tabindex="100"> Complimentary </a>
					</s:if>
					<s:if test='(xpedxItemIDUOMToAlternativeListMap.containsKey(#itemIDUOM))'>
						<a href='javascript:showXPEDXAlternateItems("<s:property value="#itemIDUOM"/>", "<s:property value="#orderLineKey"/>", "<s:property value="#orderLine.getAttribute('OrderedQty')"/>");'
							tabindex="100"> Alternate </a>
					</s:if>
					<%-- Commenting the Replacement link as 'This Item has been replaced' is the link to replacement items.
					<s:if test='(xpedxItemIDUOMToReplacementListMap.containsKey(#itemID) && xpedxItemIDUOMToReplacementListMap.get(#itemID).size()>0)'>
						<a href='javascript:showXPEDXReplacementItems("<s:property value="#itemID"/>", "<s:property value="#orderLineKey"/>", "<s:property value="#orderLine.getAttribute('OrderedQty')"/>");'
							tabindex="100"> Replacement </a>
					</s:if>
					--%>
				<%-- </s:if> --%>
				<br/> 
				<div class="clearall">&nbsp; </div>
			    	<div class="red float-left">
			    		<s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
				    	<s:iterator value="inventoryMap" id="inventoryMap" status="status" >
							<s:set name="inventoryChk" value="value" />
							<s:set name="itemId" value="key" />
							<s:if test='#item.getAttribute("ItemID") == #itemId'>
								<s:if test='%{#inventoryChk !="Y"}'>								
									<p class="red">Mill / Mfg. Item - Additional charges may apply</p>
								</s:if>
							</s:if>	
						</s:iterator>
						</s:if>
						<s:set name='linelineoverallTotals' value='#util.getElement(#orderLine, "LineOverallTotals")'/>
						<s:set name='adjustment' value='%{0.00}' />
						<%--<s:if test='%{#editOrderFlag == "true"}'>
								<s:set name='adjustment' value='#xutil.getDoubleAttribute(#editOrderOrderLineExtn,"ExtnAdjDollarAmt")' />
						</s:if>
						<s:else>
						     --%><s:set name='adjustment' value='#xutil.getDoubleAttribute(#lineExtn,"ExtnLegOrderLineAdjustments")' />
						<%--</s:else>
				    	
						--%><s:if test='%{#adjustment != 0.00}'>
							<p>A Discount of <a id='tip_<s:property value="#orderLineKey"/>' href="javascript:displayLineAdjustments('adjustmentsLightBox','<s:property value='#orderLineKey'/>')">
							<s:property value='#util.formatPriceWithCurrencySymbol(wCContext,#currencyCode,#adjustment)'/></a> has applied to this line.</p>
						</s:if>
				    	<br/>
			    	</div>
				</div>
			
				<div class="clearall">&nbsp;</div>
			    
			    <div class="bottom-mil-info">
			    	<div class="float-left brand-info">
			    		<s:set name="itemID" value='#item.getAttribute("ItemID")' />
			    		<p><s:property value="wCContext.storefrontId" /> <s:property value="#xpedxItemLabel" />: <s:property value='#item.getAttribute("ItemID")' />
				    		<s:if test='#certFlag=="Y"'>
							 	<img border="none"  src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/catalog/green-e-logo_small.png" alt="" style="margin-left:0px; display: inline;"/>
							 </s:if>
			    		</p>
			    		<s:if test='skuMap!=null && skuMap.size()>0 && customerSku!=null && customerSku!=""'>
			    			<s:set name='itemSkuMap' value='%{skuMap.get(#itemID)}'/>
			    			<s:set name='itemSkuVal' value='%{#itemSkuMap.get(customerSku)}'/>
							
							<p class="line-spacing">
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
						
						<s:if test='(xpedxItemIDUOMToReplacementListMap.containsKey(#itemID) && xpedxItemIDUOMToReplacementListMap.get(#itemID) != null)'>
			    		<a href='javascript:showXPEDXReplacementItems("<s:property value="#itemID"/>", "<s:property value="#orderLineKey"/>", "<s:property value="#orderLine.getAttribute('OrderedQty')"/>");' ><p class="cart-replaced red line-spacing">This Item has been replaced<img class="replacement-img" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_charcoal_i.png" title="View Replacement Item"/></p></a>
			    		</s:if>
			    		
			    	</div>
			    	<div class="special-instructions-div">
			    		<s:if test='#orderLine.getAttribute("LineType") !="C" && #orderLine.getAttribute("LineType") !="M" '>
			    				<p class="special-instructions-padding">Special Instructions:</p>
					    		<s:set name='lineNoteText' value='#lineNotes.getAttribute("InstructionText")' />
								<s:hidden name="lineNotesKey" id="lineNotesKey_%{#orderLineKey}" value='%{#lineNotes.getAttribute("InstructionDetailKey")}' />
		    					<s:textfield name='orderLineNote' maxlength="62"
									id="orderLineNote_%{#orderLineKey}" value='%{#lineNotes.getAttribute("InstructionText")}'
									cssClass="special-instructions-input" tabindex="%{#tabIndex}" theme="simple" disabled='%{#isUOMAndInstructions}'/>
								<s:if test='#isUOMAndInstructions'>
									<s:hidden name="orderLineNote" id="orderLineNote_%{#orderLineKey}" value='%{#lineNotes.getAttribute("InstructionText")}'/>
								</s:if>	
			    		</s:if>
			    		<s:else>
			    			<s:hidden name="orderLineNote" id="orderLineNote_%{#orderLineKey}" value='%{#lineNotes.getAttribute("InstructionText")}'/>
			    		</s:else>			    	
    				</div>
    
			    	<div class="cust-defined-fields">
			    		<table style="font-size:12px">
			    			<tbody>
				    			<s:set name='tabIndex' value='%{#tabIndex + 1}' />
				    			<s:if test="%{requiredCustFieldsErrorMap!=null && requiredCustFieldsErrorMap.size>0}" >
                					<s:set name="requiredFieldsForOLK" value="%{requiredCustFieldsErrorMap.get(#orderLineKey)}" />
                				</s:if>
								<s:iterator value='customerFieldsMap'>
									<s:set name='FieldLabel' value='key' />
									<s:set name='FieldValue' value='value' />
									<s:set name='customLbl' value='%{"Extn" + #FieldLabel}' />
										<tr>
		                                	<td class="float-right" colspan="2">
		                                	<s:if test='(#orderLineType =="P" || #orderLineType =="S")'>
		                                		<label>
		                                			<s:text name="%{#FieldValue}" />:
		                                		</label>
		                                		<s:if test=' (#FieldLabel == "CustomerPONo") || (#FieldLabel == "CustomerLinePONo") '>
													<%--Added if-else condn for giving border-color when CustomerPONo and CustomerLinePONo are blank - Jira 3966 --%>
													<s:if test="%{#requiredFieldsForOLK!=null && #requiredFieldsForOLK.contains(#FieldLabel)}" >
													<s:textfield name='orderLine%{#FieldLabel}' theme="simple"
														cssClass="x-input bottom-mill-info-avail"
														id="orderLine%{#FieldLabel}_%{#orderLineKey}"
														value="%{#orderLine.getAttribute(#FieldLabel)}"
														disabled='%{#isReadOnly}' tabindex="%{#tabIndex}" maxlength="22" cssStyle="border-color:#FF0000"/>
													</s:if>
													<s:else>
													<s:textfield name='orderLine%{#FieldLabel}' theme="simple"
														cssClass="x-input bottom-mill-info-avail"
														id="orderLine%{#FieldLabel}_%{#orderLineKey}"
														value="%{#orderLine.getAttribute(#FieldLabel)}"
														disabled='%{#isReadOnly}' tabindex="%{#tabIndex}" maxlength="22"  />
													</s:else>
												</s:if>
												<s:else>
												<%--Added if-else condn for giving border-color when Line Acc# and PO# are blank - Jira 3966 --%>
													<s:if test="%{#requiredFieldsForOLK!=null && #requiredFieldsForOLK.contains(#FieldLabel)}" >
														<s:textfield name='orderLine%{#FieldLabel}' theme="simple"
														cssClass="x-input bottom-mill-info-avail"
														id="orderLine%{#FieldLabel}_%{#orderLineKey}"
														value="%{#lineExtn.getAttribute(#customLbl)}"
														disabled='%{#isReadOnly}' tabindex="%{#tabIndex}" maxlength="24" cssStyle="border-color:#FF0000"/>
													</s:if>
													<s:else>
														<s:textfield name='orderLine%{#FieldLabel}' theme="simple"
														cssClass="x-input bottom-mill-info-avail"
														id="orderLine%{#FieldLabel}_%{#orderLineKey}"
														value="%{#lineExtn.getAttribute(#customLbl)}"
														disabled='%{#isReadOnly}' tabindex="%{#tabIndex}" maxlength="24" />
													
													</s:else>
													
												</s:else>
												<%-- Show error message against each required customer field --%>
												<s:if test="%{#requiredFieldsForOLK!=null && #requiredFieldsForOLK.contains(#FieldLabel)}" >
													
													<br/><br/><span class="error">Required fields missing. Please review and try again.</span> <br/><br/>
	
												</s:if>
												
												<%-- --%>
												<s:set name='tabIndex' value='%{#tabIndex + 1}' />
											</s:if>
											<s:else> 
												<s:hidden name='orderLine%{#FieldLabel}' id="orderLine%{#FieldLabel}_%{#orderLineKey}"
														value="" />
											
											</s:else>
											</td>
										</tr>
                                </s:iterator>
                            </tbody>
                        </table>
					</div>
				</div>
				<%--jira 2885 --%>
    				<s:set name="lineStatusCodeMsg" value="#pnALineErrorMessage.get(#itemID)"></s:set>
    				<s:if test='%{#lineStatusCodeMsg != null || #lineStatusCodeMsg != ""}'>
						<div id="mid-col-mil">
							<h5 align="center"><b><font color="red"><s:property value="%{#lineStatusCodeMsg}" /></font></b></h5>
						</div>
					</s:if>
    				<%-- end of it 2885 --%>
                <br/>
				<div class="clear"></div>
			</div>
		</div>
		<s:set name='isReadOnly' value='%{#tempIsReadOnly}' />
</s:iterator>     <!-- end iterator -->
</s:form>

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
				src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/ui-buttons/ui-btn-save.gif" width="49" height="23" alt="Save" title="Save" /> </a></li>
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
<!--for selected items fieldset
<fieldset class="mil-edit-field">
    <legend>For Selected Items:</legend>
    <input class="forselected-input-edit" type="checkbox" id="selAll1"" />
    <a class="grey-ui-btn float-left" href="javascript:removeItems();"><span>Remove Items</span></a>
</fieldset>
// end fieldset

<ul id="tool-bar" class="tool-bar-bottom">
    <li><a class="grey-ui-btn" href="javascript:update();"><span>Update Cart</span></a></li>
</ul> -->
</div>

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
 			  		 <s:if test="%{#extnOrderSubTotal == #priceWithCurrencyTemp} && #displayPriceForUoms!=null && #displayPriceForUoms.size()>0 ">	
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
	 			  <s:if test="%{#extnTotOrdValWithoutTaxes == #priceWithCurrencyTemp} && #displayPriceForUoms!=null && #displayPriceForUoms.size()>0">
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
		 			  		 <s:if test="%{#extnTotalOrderValue == #priceWithCurrencyTemp} && #displayPriceForUoms!=null && #displayPriceForUoms.size()>0">
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
							    <s:a cssClass="short-description" href="javascript:processDetail('%{#reltItem.getAttribute('ItemID')}', '%{#reltItem.getAttribute('UnitOfMeasure')}')"> <img src="<s:url value='%{#imageIdBlank}'/>" title='<s:property value="%{#reltItem.getAttribute('ItemID')}"/>' width="91" height="94" alt="" /> <!-- <b><s:property value="%{#reltItem.getAttribute('ItemID')}"/></b> --><br />
							    	<s:property value="%{#shortDesc}"/>
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
<script type="text/javascript">

//Removed to prevent hover

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
		<script type="text/javascript">
		Ext.onReady(function(){		

			/* Begin long desc. shortener */
			$('.prodlist ul li, #prodlist ul li ').each(function() {
				var html = $(this).html();
				var shortHTML = html.substring(0, 25);
				if( html.length > shortHTML.length )
				{
					$(this).html(shortHTML);
					$(this).append('...');	
					$(this).attr('title', html );
				}
			});
		});
		</script> 
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
									<s:property value="#name" />
								</a>
							</s:if> 
							<%--<s:if test="%{#itemType != 99}"><a/></s:if> --%>
					</div>
                    <div class="mil-attr-wrap">
                        <ul class="prodlist">                        
							<s:property value='#rdesc' escape='false'/>
					    </ul>
					    
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
<div class="hp-ad"><!-- iframe width="" height="" noresize
	scrolling=No frameborder=0 marginheight=0 marginwidth=0
	src="https://rotator.hadj7.adjuggler.net:443/servlet/ajrotator/52891/0/vh?z=xpedx&kw=&click="><script
	language=JavaScript
	src="https://rotator.hadj7.adjuggler.net:443/servlet/ajrotator/52891/0/vj?z=xpedx&kw=&click=&abr=$scriptiniframe"></script>
<noscript><a
	href="https://rotator.hadj7.adjuggler.net:443/servlet/ajrotator/52891/0/cc?z=xpedx"><img
	src="https://rotator.hadj7.adjuggler.net:443/servlet/ajrotator/52891/0/vc?z=xpedx&kw=&click=&abr=$imginiframe"
	width="" height="" border="0"></a></noscript>
</iframe --></div>
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
<!-- 		<li style="float: right;"><a -->
<!-- 			href="javascript:addItemsToQuickAddList()"><img -->
<!-- 			src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/ui-buttons/ui-btn-add.gif" -->
<!-- 			width="49" height="23" alt="Save" title="Save" /></a></li>			 -->
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

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx.swc.min.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery-ui.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/common/XPEDXUtils.js"></script>

<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified.js"
	type="text/javascript" charset="utf-8"></script>
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bgiframe.min.js"
	type="text/javascript" charset="utf-8"></script>
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/order/XPEDXDraftOrderDetails.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/order/XPEDXOrder.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/order/XPEDXItemAssociation.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/orderAdjustment.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jqdialog/jqdialog.js"></script>
<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
</body>
</html>