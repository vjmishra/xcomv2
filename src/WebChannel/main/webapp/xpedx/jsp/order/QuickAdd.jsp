<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<s:bean	name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils'	id='wcUtil' />


<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=8" /> 
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	
	<%
		request.setAttribute("isMergedCSSJS","true");
	%>
	<s:url id='uomDescriptionURL' namespace="/common" action='getUOMDescription' />
	<s:bean name='com.sterlingcommerce.xpedx.webchannel.common.XPEDXSCXmlUtils' id='xpedxSCXmlUtil' />
	<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean" id="xpedxUtilBean" />
	
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/ORDERS<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<!--[if IE]>
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<![endif]-->
	<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
	
	<%-- javascript --%>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	
	<%-- carousel scripts js --%>
	 <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-jquery-headder<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.numeric<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<s:include value="../order/XPEDXRefreshMiniCart.jsp"/>
	
	<s:hidden name="uomDescriptionURL" value='%{#uomDescriptionURL}'/>
	
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	
	<script type="text/javascript">
		/*
		 * This method is a copy of XPEDXDraftOrderDetails.js/addProductToQuickAddList and quick hacks for eb-1999, which was a quick hack for performance.
		 * I did NOT remove that function since it may have broken other pages.
		 * When this page is fully rewritten as part of the larger epic (eb-4366) then this should be cleaned up.
		 */
		function quickAdd_addProductToQuickAddList(element) {
			var theForm = element.form;
	
			var sku = trim(theForm.qaProductID.value);
			var quantity = trim(theForm.qaQuantity.value);
			var jobId = "";
			if (theForm.qaJobID != null) {
				jobId = trim(theForm.qaJobID.value);
				jobidFlag = true;
			}
			var itemType = trim(theForm.qaItemType.value);
			var itemTypeText = itemType;
	
			var itemTypeSelElem = theForm.qaItemType;
			if (itemTypeSelElem != null) {
				itemType = itemTypeSelElem.options[itemTypeSelElem.selectedIndex].value;
				itemTypeText = itemTypeSelElem.options[itemTypeSelElem.selectedIndex].text;
			}
	
			var uomArray = new Array();
			var CustomUomArray = new Array();
			var purchaseOrder = "";
			if (theForm.purchaseOrder != null) {
				purchaseOrder = theForm.purchaseOrder.value;
				custPOFlag = true;
			}
			if (sku == "") {
				document.getElementById("errorMsgItemBottom").innerHTML = "Please enter a valid Item #.";
				document.getElementById("errorMsgItemBottom").style.display = "inline";
				return;
			} else {
				document.getElementById("errorMsgItemBottom").innerHTML = "";
				document.getElementById("errorMsgItemBottom").style.display = "none";
			}
			if (quantity == "") {
				quantity = 1;
				theForm.qaQuantity.value = quantity;
			}
	
			document.getElementById("validationActionNameQA").value = "draftOrderAddOrderLines";
			if (swc_validateForm("QuickAddForm") == false) {
				theForm.qaQuantity.focus();
				return;
			}
	
			QuickAddElems[QuickAddElems.length] = {
				sku: sku,
				quantity: quantity,
				jobId: jobId,
				purchaseOrder: purchaseOrder,
				itemType: itemType,
				itemTypeText: itemTypeText,
				itemDesc: "",
				uom: "",
				uomList: uomArray,
				isValidated: "false",
				isEntitled: "true",
				orderMultiple: "",
				itemUomAndConvString: "",
				//Added selectedUOM for Jira 3862
				selectedUOM: "",
				customUOM: CustomUomArray
			}
	
			theForm.qaProductID.value = "";
			theForm.qaQuantity.value = "";
			if (theForm.qaJobID != null) {
				theForm.qaJobID.value = "";
			}
			if (theForm.purchaseOrder != null) {
				theForm.purchaseOrder.value = "";
			}
	
			// Kludge to get localized string from the form for use in the HTML
			// generated by redrawQuickAddList. It counts on the fact that the
			// first time redrawQuickAddList will be called on the page is for
			// the add. If it were to be called beforehand, there would be no
			// deleteStringFromForm set.
			if (deleteStringFromForm == "") {
				deleteStringFromForm = theForm.localizedDeleteLabel.value;
			}
			if (addStringFromForm == "") {
				addStringFromForm = theForm.localizedAddToCartLabel.value;
			}
			redrawQuickAddList(element);
			validateItems();
			theForm.qaProductID.focus();
	
			return false;
		} // end function quickAdd_addProductToQuickAddList
		
		
		/*
		 * This method is a copy of XPEDXDraftOrderDetails.js/addProductsToOrder and quick hacks for eb-1999, which was a quick hack for performance.
		 * I did NOT remove that function since it may have broken other pages.
		 * When this page is fully rewritten as part of the larger epic (eb-4366) then this should be cleaned up.
		 */
		function quickAdd_addProductsToOrder() {
			if (QuickAddElems.length > 0) {
				var enteredQuants;
				var enteredUoms;
				var enteredUomsConFact = new Array();
				var availUomsConFact = new Array();
				var baseUOM = new Array();
				var entereditems;
				var uomConvFac;
				var selectedUomConvFac;
				var selectedUomConvFacFromStr;
				var selectedUomFromStr;
				var orderMultiple; // only one..refine to set it only once.
				var isError = false;
				var noError = true;
				var selectedUOM = new Array();
				selectedUOM = document.getElementsByName("enteredUOMs");
				baseUOM = document.getElementsByName("quickAddBaseUOMs");
				for (var i = 0; i < QuickAddElems.length; i++) {
					noError = true;
					orderMultiple = encodeForHTML(QuickAddElems[i].orderMultiple);
					if (orderMultiple != undefined && orderMultiple > 1 && orderMultiple.replace(/^\s*|\s*$/g, "") != '' && orderMultiple != null && orderMultiple != 0) {
						var enteredUOM = selectedUOM[i].value;
						enteredUOM = enteredUOM.split(" ");
						enteredQuants = encodeForHTML(QuickAddElems[i].quantity);
						entereditems = encodeForHTML(QuickAddElems[i].sku);
						enteredUoms = enteredUOM[0];
						uomConvFac = encodeForHTML(QuickAddElems[i].itemUomAndConvString);
					} else {
						enteredQuants = encodeForHTML(QuickAddElems[i].quantity);
						entereditems = encodeForHTML(QuickAddElems[i].sku);
						enteredUoms = encodeForHTML(QuickAddElems[i].uom);
						uomConvFac = encodeForHTML(QuickAddElems[i].itemUomAndConvString);
					}
					if (enteredUoms) {
						if (uomConvFac != null) enteredUomsConFact = uomConvFac.split("|");
						for (var j = 0; j < enteredUomsConFact.length; j++) {
							availUomsConFact = enteredUomsConFact[j].split("-");
							selectedUomConvFacFromStr = availUomsConFact[1];
							selectedUomFromStr = availUomsConFact[0];
							if (enteredUoms.trim() == selectedUomFromStr.trim()) {
								selectedUomConvFac = selectedUomConvFacFromStr;
								break;
							}
						}
						enteredQuants = ReplaceAll(enteredQuants, ",", "");
						var divId = "errorQty_" + entereditems + i;
						var divIdError = document.getElementById(divId);
						if (selectedUomConvFac != undefined && selectedUomConvFac != null) {
	
							if (orderMultiple == undefined || orderMultiple.replace(/^\s*|\s*$/g, "") == '' || orderMultiple == null || orderMultiple == 0) {
								orderMultiple = 1;
							}
							var totalQty = selectedUomConvFac * enteredQuants;
							var ordMul = totalQty % orderMultiple;
	
							if (enteredQuants == '' || enteredQuants == '0') {
								//3098
								document.getElementById(divId).innerHTML = 'Please enter a valid quantity and try again.';
								//3098
								document.getElementById(divId).style.display = "inline-block";
								document.getElementById(divId).style.marginLeft = "20px";
								document.getElementById(divId).setAttribute("class", "error");
								document.getElementById(divId).setAttribute("align", "center");
								isError = true;
								noError = false;
							}
							if (orderMultiple > 1 && noError == true) {
								document.getElementById(divId).innerHTML = "Must be ordered in units of " + orderMultiple + " " + baseUOM[i].value;
								document.getElementById(divId).style.display = "inline-block";
								document.getElementById(divId).setAttribute("class", "notice");
								document.getElementById(divId).setAttribute("align", "center");
							}
						}
					} else {
						continue;
					}
				}
				if (!isError) { // no error, then submit to add the products to the cart
					for (var i = 0; i < QuickAddElems.length; i++) {
						var orderMultiple1 = encodeForHTML(QuickAddElems[i].orderMultiple);
						if (orderMultiple1 == undefined || orderMultiple1.replace(/^\s*|\s*$/g, "") == '' || orderMultiple1 == null || orderMultiple1 == 0) {
							orderMultiple1 = 1;
						}
	
						createHiddenField("QuickAddForm", "quickAddOrderMultiple", orderMultiple1);
					}
					document.QuickAddForm.action = document.getElementById('addProductsToOrderURL');
					var form = Ext.get("QuickAddForm");
					addCSRFToken(form.dom, 'form');
					form.dom.submit();
				} else {
					return false;
				}
			}
		} // end function quickAdd_addProductsToOrder
		
		
		function quickAddCopyAndPaste(data) {
			var prodcutValidateUrl = document.getElementById('productValidateURL').href;
			document.getElementById('productValidateURL').href = document.getElementById('productListValidateURL').href;
			var itemLineFlag = "false";
			var itemsString = data;
			var char = '\n';
			var itemLines = itemsString.split(char);
			var checklength = itemLines.length; //jira 4128
			if (itemsString == "") {
				document.getElementById("errorMsgCopyBottom").innerHTML = "Valid string is required. See instructions above.";
				document.getElementById("errorMsgCopyBottom").style.display = "inline";
			} else {
				document.getElementById("errorMsgCopyBottom").innerHTML = "";
				document.getElementById("errorMsgCopyBottom").style.display = "none";
			}
	
			for (var i = 0; i < itemLines.length; i++) {
				var itemQty = null;
				var itemSku = null;
				var jobId = "";
				var itemLine = itemLines[i].split('\t');
				if (i == itemLines.length - 1) {
					if (itemLine == "") {
						break;
					}
				}
				if (checklength > 20) {
					itemLineFlag = "true";
					alert("<s:text name='MSG.SWC.CART.ADDTOCART.ERROR.QTYGT20' />");
					break;
				}
	
				if (itemLine.length > 1) {
					itemQty = itemLine[0];
					itemSku = itemLine[1];
				}
				itemLine = itemLines[i].split(',');
				if (itemLine.length > 1) {
					itemQty = itemLine[0];
					itemSku = itemLine[1];
					if (itemSku == "" && itemQty == "") {
						itemLineFlag = "true";
						document.getElementById("errorMsgCopyBottom").innerHTML = "Valid string is required. See instructions above.";
						document.getElementById("errorMsgCopyBottom").style.display = "inline";
					}
				} else {
					itemLineFlag = "true";
					document.getElementById("errorMsgCopyBottom").innerHTML = "Valid string is required. See instructions above.";
					document.getElementById("errorMsgCopyBottom").style.display = "inline";
				}
			}
	
			if (itemLineFlag == "false") {
				for (var i = 0; i < itemLines.length; i++) {
					var itemQty = null;
					var itemSku = null;
					var jobId = "";
					var itemLine = itemLines[i].split('\t');
	
					if (itemLine.length > 1) {
						itemQty = itemLine[0];
						itemSku = itemLine[1];
					}
					itemLine = itemLines[i].split(',');
	
					if (itemLine.length > 1) {
						itemQty = itemLine[0];
						itemSku = itemLine[1];
					}
					if ((i + 1) == itemLines.length && itemLineFlag == "false") {
						$.fancybox.close();
						Ext.get('dlgCopyAndPasteText').dom.value = '';
					}
	
					itemSku = Ext.util.Format.trim(itemSku);
					itemQty = Ext.util.Format.trim(itemQty);
	
					if (itemSku != null && itemSku != "null") {
						document.getElementById("qaProductID").value = itemSku;
						document.getElementById("qaQuantity").value = itemQty;
	
						var theForm = document.getElementById('quickAddButton').form;
	
						var sku = trim(theForm.qaProductID.value);
						var quantity = trim(theForm.qaQuantity.value);
						var jobId = "";
						if (theForm.qaJobID != null) {
							jobId = trim(theForm.qaJobID.value);
							jobidFlag = true;
						}
						var itemType = trim(theForm.qaItemType.value);
						var itemTypeText = itemType;
	
						var itemTypeSelElem = theForm.qaItemType;
						if (itemTypeSelElem != null) {
							itemType = itemTypeSelElem.options[itemTypeSelElem.selectedIndex].value;
							itemTypeText = itemTypeSelElem.options[itemTypeSelElem.selectedIndex].text;
						}
	
						var uomArray = new Array();
						var CustomUomArray = new Array();
						var purchaseOrder = "";
						if (theForm.purchaseOrder != null) {
							purchaseOrder = theForm.purchaseOrder.value;
							custPOFlag = true;
						}
						if (sku == "") {
							document.getElementById("errorMsgItemBottom").innerHTML = "Please enter a valid Item #.";
							document.getElementById("errorMsgItemBottom").style.display = "inline";
							return;
						} else {
							document.getElementById("errorMsgItemBottom").innerHTML = "";
							document.getElementById("errorMsgItemBottom").style.display = "none";
						}
						if (quantity == "") {
							quantity = 1;
							theForm.qaQuantity.value = quantity;
						}
	
						document.getElementById("validationActionNameQA").value = "draftOrderAddOrderLines";
						if (swc_validateForm("QuickAddForm") == false) {
							theForm.qaQuantity.focus();
							return;
						}
	
						QuickAddElems[QuickAddElems.length] = {
							sku: sku,
							quantity: quantity,
							jobId: jobId,
							purchaseOrder: purchaseOrder,
							itemType: itemType,
							itemTypeText: itemTypeText,
							itemDesc: "",
							uom: "",
							uomList: uomArray,
							isValidated: "false",
							isEntitled: "true",
							orderMultiple: "",
							itemUomAndConvString: "",
							//Added selectedUOM for Jira 3862
							selectedUOM: "",
							customUOM: CustomUomArray
						};
	
						theForm.qaProductID.value = "";
						theForm.qaQuantity.value = "";
						if (theForm.qaJobID != null) {
							theForm.qaJobID.value = "";
						}
						if (theForm.purchaseOrder != null) {
							theForm.purchaseOrder.value = "";
						}
	
						// Kludge to get localized string from the form for use in the HTML
						// generated by redrawQuickAddList. It counts on the fact that the
						// first time redrawQuickAddList will be called on the page is for
						// the add. If it were to be called beforehand, there would be no
						// deleteStringFromForm set.
						if (deleteStringFromForm == "") {
							deleteStringFromForm = theForm.localizedDeleteLabel.value;
						}
						if (addStringFromForm == "") {
							addStringFromForm = theForm.localizedAddToCartLabel.value;
						}
					} // end if itemSku not null
				} // end for itemLines
			} // end if itemLineFlag is 'false'
	
			validateItems();
			document.getElementById('productValidateURL').href = prodcutValidateUrl;
		} // end function quickAddCopyAndPaste
		
		function closeCopyAndPaste() {
			$.fancybox.close();
			
			$('#dlgCopyAndPasteText').val('');
			
			// clear copy and paste error message
			$errorMsgCopyBottom = $('#errorMsgCopyBottom');
			$errorMsgCopyBottom.innerHTML = '';
			$errorMsgCopyBottom.hide();
		}
	</script>
	
	<script type="text/javascript">
		$(document).ready(function() {
			$('#quick-add-button').click(function() {
				var offset = $(this).offset();
				$('#tq-quick-add-overlay').css({ top: offset.top+35 });
				$('#tq-quick-add-overlay').toggle();
				return false;
			});
			$('#quick-add-close').click(function() {
				$('#tq-quick-add-overlay').toggle();
				return false;
			});
	
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
			
	
			$('.full-description-replacement-model').each(function() { 
				var html = $(this).html();
				var shortHTML = html.substring(0, 175);
				if( html.length > shortHTML.length )
				{
					$(this).html(shortHTML);
					$(this).append('...');	
					$(this).attr('title', html );
				}
			});
			 
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
		});
	</script>

	<title>Quick Add</title>

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

<s:set name='_action' value='[0]' />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils' id='XPEDXWCUtils' />
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean' id='xpedxutil' />	
<s:bean name='com.sterlingcommerce.webchannel.utilities.CommonCodeUtil' id='ccUtil' />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.priceandavailability.XPEDXPriceandAvailabilityUtil' id='priceUtil' />
<s:set name='orderShippingAndTotalStartTabIndex' value='900' />
<s:set name='sdoc' value="outputDocument" />
<s:set name='orderDetails' value='#util.getElement(#sdoc, "Order")' />
<s:set name='extnOrderDetails' value='#util.getElement(#sdoc, "Order/Extn")' />
<s:set name='orderHeaderKey' value='#orderDetails.getAttribute("OrderHeaderKey")' />
<s:set name='draftOrderFlag' value='#orderDetails.getAttribute("DraftOrderFlag")' />
<s:set name='priceInfo' value='#util.getElement(#sdoc, "Order/PriceInfo")' />
<s:set name='currencyCode' value='#priceInfo.getAttribute("Currency")' />
<s:set name='overallTotals' value='#util.getElement(#sdoc, "Order/OverallTotals")' />
<s:set name='orderExtn' value='#util.getElement(#sdoc, "Order/Extn")' />
<s:set name='orderLines' value='#util.getElement(#sdoc, "Order/OrderLines")' />
<s:set name='shipping' value='#util.getElement(#sdoc, "Order/Shipping")' />
<s:set name='wcContext' value="wCContext" />
<s:set name="xpedxCustomerContactInfoBean" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("XPEDX_Customer_Contact_Info_Bean")' />
<s:set name="isEstimator" value='%{#xpedxCustomerContactInfoBean.isEstimator()}' />
<s:set name="msapExtnUseOrderMulUOMFlag" value='%{#xpedxCustomerContactInfoBean.getMsapExtnUseOrderMulUOMFlag()}' />

<s:set name='chargeDescriptionMap' value='#util.getChargeDescriptionMap(#wcContext)' />
<s:set name='isOwnerOfNonCartInContextDraftOrder' value='#_action.isOwnerOfNonCartInContextDraftOrder()' />
<s:set name='isProcurementInspectMode' value="true" />
<s:set name='isPunchoutUser' value="%{wCContext.getWCAttribute('isPunchoutUser')}"/>
<s:set name='hasPendingChanges' value='#orderDetails.getAttribute("HasPendingChanges")' />
	
<s:if test='majorLineElements.size() == 0'>
	<s:set name='checkoutDisabled' value='"disabled"' />
</s:if>
<s:else>
	<s:set name='checkoutDisabled' value='' />
</s:else>
<s:set name="editOrderFlag" value='%{#_action.getIsEditOrder()}' />
<s:set name="resetDescFlag" value='%{#_action.getResetDesc()}' />

<s:set name='appFlowContext' value='#session.FlowContext' />
<s:set name='isGuest' value='wCContext.isGuestUser()' />

<s:set name="isEditOrderHeaderKey" value ="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute(@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@EDITED_ORDER_HEADER_KEY)}"/>

<s:set name='numberOfInitialComplementaryItemsToDisplay' value='3' />

<s:set name="emailDialogTitle" scope="page" value="#_action.getText('Email_Title')" />
<s:set name="pnALineErrorMessage" value="#_action.getPnALineErrorMessage()" />
<s:set name="pnaErrorStatusMsg" value="#_action.getAjaxLineStatusCodeMsg()"/>
<s:set name="duplicateInfoMsg" value="#_action.getDuplicateInfoMsg()"/>
<s:url includeParams="none" id="orderNotesListURL" action="orderNotesList.action">
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
	<s:url id="continueShoppingURL" action="navigate" namespace="/catalog" escapeAmp="false">
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
	action='MyResetPendingOrder' namespace='/order' escapeAmp="false">
	<s:param name="orderHeaderKey" value='%{#orderHeaderKey}' />
</s:url>

<s:url id='checkoutURLid' namespace='/order' action='xpedxsaveCartDetails' />
<s:bean name='org.apache.commons.lang.StringUtils' id='strUtil' />

<body class="ext-gecko ext-gecko3">
	
	<s:url id="addProductsToOrderURLid" namespace="/order" action="draftOrderAddOrderLines" /> 
	<s:a cssClass="display:none;" id="addProductsToOrderURL" href="%{#addProductsToOrderURLid}" />
	 
	<div id="main-container">
		<div id="main">
			<s:action name="xpedxHeader" executeResult="true" namespace="/common" >
				<s:param name='shipToBanner' value="%{'true'}" />
			</s:action> 
			<s:set name="shipToCustomer" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("shipToCustomer")' />
			<s:set name="billToCustomer" value='#shipToCustomer.getBillTo()' />
		
			<div class="container">
				
				<div id="infoMessage">
					<s:if test='%{#parameters.addedToCart[0] == "true"}'>
						Items successfully added to
						<s:property value='%{#isEditOrderHeaderKey == null || #isEditOrderHeaderKey == "" ? "cart" : "order"}' />.
					</s:if>
				</div>
				
				<div class="tq-quick-add-form">
					<span class="page-title">Quick Add</span>
					<p class="quick-add-aux-links" style="margin-top:5px; margin-right:5px;"> 
						<a class="modal underlink" tabindex="-1" href="#dlgCopyAndPaste" onclick="javascript: writeMetaTag('DCSext.w_x_ord_quickadd_cp', '1');" id="copyPaste" >
							Copy and Paste
						</a>
					</p>
					<div class="clear">&nbsp;</div>
					<form name="QuickAddForm" class="form selector quick-add-to-cart-form" id="QuickAddForm">
						<s:hidden name='fromQuickAdd' value='true' />
						<s:hidden name='#action.name' id='validationActionNameQA' value='draftOrderDetails' />
						<s:hidden name='#action.namespace' value='/order' />
						<s:hidden name="orderHeaderKey"	value='%{#orderHeaderKey}' />
						<s:hidden name="draft" value="%{#draftOrderFlag}" />
						<s:hidden name='Currency' value='%{#currencyCode}' />
						<s:hidden id="isPNACallOnLoad" name="isPNACallOnLoad" value='false' />	
						<input type="hidden" name="isEditOrder" value="<s:property	value='%{(#_action.getIsEditOrder())}' escape="false" />" />
						<ul class="hvv">
							<li>
								<label>Item Type:</label>
								<s:select tabindex="3403" id="qaItemType" name="qaItemType" cssStyle="width:135px;"
										headerKey="1"
										list="skuTypeList" listKey="key" listValue="value"/>
								
								<s:hidden name="#qaItemType.type" value="ItemID" />
							</li>
							<li>
								<label>Item #:</label>
								<input tabindex="3404" maxlength="27" style="width:70px;" type="text" id="qaProductID" name="qaProductID" class="text x-input" />
								<s:hidden name="#qaProductID.type" value="ItemID" />
								<s:hidden name='localizedMissingProductIDMessage'value='%{#_action.getText("QAMissingProductID")}' />
							</li>
							<li>
								<label>Qty:</label>						
								<input tabindex="3405" maxlength="7" style="width:70px;" type="text" id="qaQuantity" name="qaQuantity" class="qty-field text x-input" onKeyUp="return isValidQuantityRemoveAlpha(this,event)"/>
								<s:hidden name="#qaQuantity.type" value="OrderedQty" />
							</li>
							<s:set name="jobIdFlag" value='%{customerFieldsMap.get("CustLineAccNo")}' />
							<s:set name="chargeAmount" value='%{chargeAmount}' />
							<s:set name="fmtdchargeAmount" value='#util.formatPriceWithCurrencySymbol(wCContext,#currencyCode,#chargeAmount)'/>
							<s:set name="minOrderAmount" value='%{minOrderAmount}' />
							<s:set name="fmtdMinOrderAmount" value='#util.formatPriceWithCurrencySymbol(wCContext,#currencyCode,#minOrderAmount)'/>
							<s:set name="erroMsg" value='%{erroMsg}' />
							<s:set name="maxOrderAmount" value='%{maxOrderAmount}' />
							<s:set name="fmtdMaxOrderAmount" value='#util.formatPriceWithCurrencySymbol(wCContext,#currencyCode,#maxOrderAmount)'/>
							<s:set name="customerPONoFlag" value='%{customerFieldsMap.get("CustomerPONo")}' />
							
							<s:if test='%{#customerPONoFlag != null && !#customerPONoFlag.equals("")}'>
								<li>
									<label><s:property value='#customerPONoFlag' />:</label>
									<s:hidden name='customerPONoValue' value='%{#customerPONoFlag}' />
									<input tabindex="3407" maxlength="22" style="width:154px;" type="text" name="purchaseOrder" value="" class="text x-input" />
								</li>
							</s:if>
								<s:if test='%{#jobIdFlag != null && !#jobIdFlag.equals("")}'>
								<li>
									<label><s:property value='#jobIdFlag' />:</label>
									 <s:hidden name='jobIdValue' value='%{#jobIdFlag}' />
									<input tabindex="3408" maxlength="24" style="width:154px;" type="text" id="qaJobID" name="qaJobID" class="text x-input" />
									<s:hidden name="#qaJobID.type" value="" />
								</li>
							</s:if>
							<s:else>
								<s:hidden id="qaJobID" name="qaJobID" value="" />
								<s:hidden name="#qaJobID.type" value="" />
							</s:else>
							
							<li>
								<label>&nbsp;</label>
								<input id="quickAddButton" type="hidden"/>
								<input class="btn-gradient" type="submit" value="+ Add to Quick List" onclick="quickAdd_addProductToQuickAddList(document.getElementById('quickAddButton')); return false;" />
								<s:hidden name='localizedDeleteLabel' value='%{#_action.getText("localizedDeleteLabel")}' />
								<s:hidden name='localizedAddToCartLabel' value='%{#_action.getText("AddQAListToCart")}' />
							</li>
						</ul>
						<s:url id='productValidateURLid' namespace='/order' action='validateProduct' />
						<s:url id='productListValidateURLid' namespace='/order' action='validateProductList' />
						<s:a id='productValidateURL' href='%{#productValidateURLid}' tabindex="-1" />
						<s:a id='productListValidateURL' href='%{#productListValidateURLid}' />
						<div id="QuickAddList" style="display: block;"></div>
						<div class="error" id="errorMsgItemBottom" style="display:none;position:relative;left:340px" ></div>
					</form>
					<s:hidden name="msapOrderMulUOMFlag" id="msapOrderMulUOMFlag" value="%{#msapExtnUseOrderMulUOMFlag}" />
					
					<div class="clear">&nbsp;</div>
					<div id="addProdsToOrder" class="close-btn" style="display: none;">
						<div class="button-container addpadtop15">
							<input class="btn-gradient floatright addmarginright10" type="submit" onclick="quickAdd_addProductsToOrder(); return false;"
									value="<s:property value='%{#isEditOrderHeaderKey == null || #isEditOrderHeaderKey == "" ? "Add to Cart" : "Add to Order"}'/>"
									/>
						</div>
					</div>
					
				</div> <%-- / tq-quick-add-form --%>
			</div> <%-- / container --%>
			
		</div> <%-- main --%>
	</div> <%-- / main-container --%>
	
	<div style="display:none;">
		<div id="dlgCopyAndPaste" class="xpedx-light-box" style="width: 400px; height: 300px;">
			<h2>Copy and Paste &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <small>Limit is 20 items per copy/paste</small></h2>
			<p>
				Copy and paste or type the quantities and <s:property value="wCContext.storefrontId" /> item numbers or customer item numbers from your file in the following format:
				 quantity,item number (no spaces).
				<br/>
				Example: 12,5002121
			</p>
			<p>
				To enter items without quantities, copy and paste or type a comma followed by the item number (no spaces).
				<br/>
				Example: ,5002121
				<br/>
			</p>
			<br/>
			<form id="form1" name="form1" method="post" action="">
				<textarea name="dlgCopyAndPasteText" id="dlgCopyAndPasteText" cols="48" rows="5"></textarea>
				
				<div class="button-container addpadtop15">
					<input class="btn-gradient floatright addmarginright10" type="submit" value="Add to Quick List" onclick="quickAddCopyAndPaste( document.form1.dlgCopyAndPasteText.value); return false;" />
					<input class="btn-neutral floatright addmarginright10" type="submit" value="Cancel" onclick="$.fancybox.close(); closeCopyAndPaste(); return false;" />
				</div>
			</form>
			<br/>
			<br/>
			<br/>
			<div class="error" id="errorMsgCopyBottom" style="display:none;position:relative;left:100px"></div>
		</div> <%-- / dlgCopyAndPaste --%>
	</div> <%-- / hidden div --%>
	
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx.swc.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery-ui.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/common/XPEDXUtils<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	
	<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
	<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bgiframe.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
	<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/order/XPEDXDraftOrderDetails<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/order/XPEDXOrder<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/order/XPEDXItemAssociation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/orderAdjustment<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jqdialog/jqdialog<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
</body>
</html>