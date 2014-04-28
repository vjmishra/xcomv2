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
	<s:set name='orderHeaderKey' value='%{#_action.getOrderHeaderKey()}' />
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
</head>
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
				
				<s:if test='%{#parameters.addedToCart[0] == "true"}'>
					<div id="infoMessage" class="addpadtop15">
						Items successfully added to
						<s:property value='%{#isEditOrderHeaderKey == null || #isEditOrderHeaderKey == "" ? "cart" : "order"}' />.
					</div>
				</s:if>
				
				<div id="breadcumbs-list-name" class="page-title">Quick Add</div>  
				
				<div class="qa-standalone-copy">
					<a id="copyPaste" class="underlink" tabindex="-1" href="#dlgCopyAndPaste" onclick="javascript: writeMetaTag('DCSext.w_x_ord_quickadd_cp', '1');">
						Copy and Paste
					</a>
				</div>
				<div class="clearfix" ></div>
				
				<div class="tq-quick-add-form">
					<form name="QuickAddForm" class="form selector quick-add-to-cart-form qa-standalone-wrap" id="QuickAddForm">
						<s:hidden name='fromQuickAdd' value='true' />
						<s:hidden name='#action.name' id='validationActionNameQA' value='draftOrderDetails' />
						<s:hidden name='#action.namespace' value='/order' />
						<s:hidden name="orderHeaderKey"	value='%{#orderHeaderKey}' />
						<s:hidden name="draft" value="%{#draftOrderFlag}" />
						<s:hidden name='Currency' value='%{#currencyCode}' />
						<s:hidden id="isPNACallOnLoad" name="isPNACallOnLoad" value='false' />	
						<s:hidden id="isEditOrder" value="%{(#isEditOrderHeaderKey != null && #isEditOrderHeaderKey != '')}" />
						<s:hidden name='sfId' id='sfId' value="%{wCContext.storefrontId}" />
						<label>
							<span>Item Type:</span>
							<s:select id="qaItemType" name="qaItemType" cssStyle="width:135px;" headerKey="1"
									list="skuTypeList" listKey="key" listValue="value"/>
							<s:hidden name="#qaItemType.type" value="ItemID" />
						</label>
						
						<label>
							<span>Item #:</span>
							<input type="text" maxlength="27" style="width:80px;" id="qaProductID" name="qaProductID" class="text x-input" />
							<s:hidden name="#qaProductID.type" value="ItemID" />
							<s:hidden name='localizedMissingProductIDMessage'value='%{#_action.getText("QAMissingProductID")}' />
						</label>
						
						<label>
							<span>Qty:</span>
							<input maxlength="7" style="width:80px;" type="text" id="qaQuantity" name="qaQuantity" class="qty-field text x-input" onKeyUp="return isValidQuantityRemoveAlpha(this,event)"/>
							<s:hidden name="#qaQuantity.type" value="OrderedQty" />
						</label>
						
						<s:set name="jobIdFlag" value='%{customerFieldsMap.get("CustLineAccNo")}' />
						<s:set name="chargeAmount" value='%{chargeAmount}' />
						<s:set name="fmtdchargeAmount" value='#util.formatPriceWithCurrencySymbol(wCContext,#currencyCode,#chargeAmount)' />
						<s:set name="minOrderAmount" value='%{minOrderAmount}' />
						<s:set name="fmtdMinOrderAmount" value='#util.formatPriceWithCurrencySymbol(wCContext,#currencyCode,#minOrderAmount)' />
						<s:set name="erroMsg" value='%{erroMsg}' />
						<s:set name="maxOrderAmount" value='%{maxOrderAmount}' />
						<s:set name="fmtdMaxOrderAmount" value='#util.formatPriceWithCurrencySymbol(wCContext,#currencyCode,#maxOrderAmount)' />
						<s:set name="customerPONoFlag" value='%{customerFieldsMap.get("CustomerPONo")}' />
						
						<s:if test='%{#customerPONoFlag != null && !#customerPONoFlag.equals("")}'>
							<label>
								<span><s:property value='#customerPONoFlag' />:</span>
								<s:hidden name='customerPONoValue' value='%{#customerPONoFlag}' />
								<input maxlength="22" style="width:180px;" type="text" name="purchaseOrder" value="" class="text x-input" />
							</label>
						</s:if>
						
						<s:if test='%{#jobIdFlag != null && !#jobIdFlag.equals("")}'>
							<label>
								<span><s:property value='#jobIdFlag' />:</span>
								 <s:hidden name='jobIdValue' value='%{#jobIdFlag}' />
								<input maxlength="24" style="width:180px;" type="text" id="qaJobID" name="qaJobID" class="text x-input" />
								<s:hidden name="#qaJobID.type" value="" />
							</label>
						</s:if>
						<s:else>
							<s:hidden id="qaJobID" name="qaJobID" value="" />
							<s:hidden name="#qaJobID.type" value="" />
						</s:else>
						
						<label>
							<div class="qa-standalone-qabutton">
								<input id="quickAddButton" type="hidden"/>
								<input type="submit" class="btn-gradient" value="+ Add to Quick List" onclick="quickAdd_addProductToQuickAddList(document.getElementById('quickAddButton')); return false;" />
							</div>
						</label>
						<div class="clearfix addpadbottom20"></div>
						
						<s:hidden name='localizedDeleteLabel' value='%{#_action.getText("localizedDeleteLabel")}' />
						<s:hidden name='localizedAddToCartLabel' value='%{#_action.getText("AddQAListToCart")}' />
						
						<s:url id='productValidateURLid' namespace='/order' action='validateProduct' />
						<s:url id='productListValidateURLid' namespace='/order' action='validateProductList' />
						<s:a id='productValidateURL' href='%{#productValidateURLid}' tabindex="-1" />
						<s:a id='productListValidateURL' href='%{#productListValidateURLid}' tabindex="-1" />
						
						<div id="QuickAddList" style="display: block;"></div>
						
						<div id="addProdsToOrder" style="display: none;">
							<div class="qa-standalone-cartbtn">
								<input type="submit" class="btn-gradient floatright addmarginright10" onclick="quickAdd_addProductsToOrder(); return false;"
										value="<s:property value='%{#isEditOrderHeaderKey == null || #isEditOrderHeaderKey == "" ? "Add to Cart" : "Add to Order"}'/>"
										/>
							</div>
						</div>
						
						<span class="error" id="errorMsgItemBottom" style="display:none;position:relative;left:340px"></span>
					</form>
					
					<s:hidden name="msapOrderMulUOMFlag" id="msapOrderMulUOMFlag" value="%{#msapExtnUseOrderMulUOMFlag}" />
					
				</div> <%-- / tq-quick-add-form --%>
			</div> <%-- / container --%>
			
		</div> <%-- main --%>
	</div> <%-- / main-container --%>
	
	<div style="display:none;">
		<div id="dlgCopyAndPaste" class="xpedx-light-box" style="width: 450px; height: 600px;">
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
				<textarea name="dlgCopyAndPasteText" id="dlgCopyAndPasteText" cols="48" rows="20"></textarea>
				
				<div class="button-container addpadtop15">
					<input class="btn-gradient floatright addmarginright10" type="submit" value="Add to Quick List" onclick="quickAddCopyAndPaste($('#dlgCopyAndPasteText').val()); return false;" />
					<input class="btn-neutral floatright addmarginright10" type="submit" value="Cancel" onclick="$.fancybox.close(); return false;" />
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
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/order/QuickAdd<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/order/XPEDXOrder<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/order/XPEDXItemAssociation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/orderAdjustment<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jqdialog/jqdialog<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
</body>
</html>