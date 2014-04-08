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
	
	<s:url id="ajaxValidateItemsURLid" namespace="/order" action="ajaxValidateItems" /> 
	<s:a cssClass="display:none;" id="ajaxValidateItemsURL" href="%{#ajaxValidateItemsURLid}" />
	
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
				
				<div id="breadcumbs-list-name" class="page-title addmarginbottom15 addmargintop17">Quick Add</div>
				
				<form name="QuickAddForm" class="addpadleft20 addpadtop10" id="QuickAddForm">
					<label>
						<div class="qa-rightcol">
						 	<p>Select Item Type</p>
						</div>
						<s:select id="qaItemType" name="qaItemType" cssStyle="width:135px;" headerKey="1"
						 		list="skuTypeList" listKey="key" listValue="value"/>
						<s:hidden name="#qaItemType.type" value="ItemID" />
					</label>		
					<div class="clearfix"></div>
					
					<hr size="1" color="#cdcdcd" class=" addmargintop20 addmarginright20" />
					
					<h3 class="addmargintop10 qa-subhead">Add Item List</h3>
					
					<div class="float-right clearboth">
						<input type="button" onclick="validateItems(); return false;" class="btn-gradient floatright addmarginright20"
								value="<s:property value='%{#isEditOrderHeaderKey == null || #isEditOrderHeaderKey == "" ? "Add to Cart" : "Add to Order"}'/>" />
					</div>
					<s:hidden name='fromQuickAdd' value='true' />
					<s:hidden name='#action.name' id='validationActionNameQA' value='draftOrderDetails' />
					<s:hidden name='#action.namespace' value='/order' />
					<s:hidden name="orderHeaderKey"	value='%{#orderHeaderKey}' />
					<s:hidden name="draft" value="%{#draftOrderFlag}" />
					<s:hidden name='Currency' value='%{#currencyCode}' />
					<s:hidden id="isPNACallOnLoad" name="isPNACallOnLoad" value='false' />	
					<input type="hidden" name="isEditOrder" value="<s:property value='%{(#_action.getIsEditOrder())}' escape="false" />" />
					<s:hidden name='sfId' id='sfId' value="%{wCContext.storefrontId}" />	
					<input type="hidden" name="isEditOrder" value="<s:property	value='%{(#_action.getIsEditOrder())}' escape="false" />" />
					<s:set name="jobIdFlag" value='%{customerFieldsMap.get("CustLineAccNo")}' />
					<s:set name="chargeAmount" value='%{chargeAmount}' />
					<s:set name="fmtdchargeAmount" value='#util.formatPriceWithCurrencySymbol(wCContext,#currencyCode,#chargeAmount)' />
					<s:set name="minOrderAmount" value='%{minOrderAmount}' />
					<s:set name="fmtdMinOrderAmount" value='#util.formatPriceWithCurrencySymbol(wCContext,#currencyCode,#minOrderAmount)' />
					<s:set name="erroMsg" value='%{erroMsg}' />
					<s:set name="maxOrderAmount" value='%{maxOrderAmount}' />
					<s:set name="fmtdMaxOrderAmount" value='#util.formatPriceWithCurrencySymbol(wCContext,#currencyCode,#maxOrderAmount)' />
					<s:set name="customerPONoFlag" value='%{customerFieldsMap.get("CustomerPONo")}' />
					
					<div class="qa-row-wrap">
						<div class="qa-listheader pushleft">
							<div class="label-item">Item</div>
							<div class="label-qty">Qty</div>
							<s:if test='%{#customerPONoFlag != null && !#customerPONoFlag.equals("")}'>
							<div class="label-po"><s:property value='#customerPONoFlag' /></div>
							</s:if>
							<s:if test='%{#jobIdFlag != null && !#jobIdFlag.equals("")}'>
								<div class="label-account"><s:property value='#jobIdFlag' /></div>		
							</s:if>
						</div>
						<s:iterator value='#_action.getQuickaddItemLines()' status="itemline" >
							<s:div id='%{"qa-listrow_" + #itemline.count}' cssClass='qa-listrow pushleft'
									cssStyle='%{#itemline.count > #_action.getDisplayItemLines() ? "display:none;" : ""}'>
								<div class="label-item">
									<input type="text" maxlength="27" size="15" id="enteredProductIDs_<s:property value='#itemline.count'/>" name="enteredProductIDs" class="inputfloat input-item"
											onfocus="javascript:showQuickAddRow(<s:property value='%{#itemline.count + 1}'/>)" />
								</div>					
								<div class="label-qty"><input maxlength="7"  size="8" type="text" id="enteredQuantities_<s:property value='#itemline.count'/>" name="enteredQuantities" class="inputfloat input-qty" onKeyUp="return isValidQuantityRemoveAlpha(this,event)"/></div>
								<s:if test='%{#customerPONoFlag != null && !#customerPONoFlag.equals("")}'>	
									<div class="label-po">
										<input maxlength="22" size="15" type="text" name="enteredPONos" value="" class="inputfloat input-po" id="enteredPONos_<s:property value='#itemline.count'/>"/>
									</div>							
								</s:if>
								<s:if test='%{#jobIdFlag != null && !#jobIdFlag.equals("")}'>						
									<div class="label-account">
										<input maxlength="24" size="20" type="text" id="enteredJobIDs_<s:property value='#itemline.count'/>" name="enteredJobIDs" class="inputfloat input-account" />
									</div>	
								</s:if>
								<div class="error producterrorLine" style="display:none;" id="producterrorLine_<s:property value='#itemline.count'/>"></div> 
								
								<%-- These inputs are required for the backend to process, not visible in UI --%>
								<s:hidden cssClass="input-uom" name="enteredUOMs" value="" /> <%-- populated by javascript before submission --%>
								<s:hidden cssClass="input-itemType" name="enteredItemTypes" value="" /> <%-- populated by javascript before submission --%>
								<s:hidden name="enteredProductDescs" value="" /> <%-- always empty --%>
								<s:hidden name="quickAddOrderMultiple" value="1" /> <%-- always 1 (quick add ignores order multiple) --%>
							</s:div> <%-- / qa-listrow --%>
						</s:iterator> <%-- quickaddItemLines --%>
					</div> <%-- / qa-row-wrap --%>
					
					<div class="float-right clearboth">
						<input type="button" onclick="validateItems(); return false;" class="btn-gradient floatright addmarginright20"
								value="<s:property value='%{#isEditOrderHeaderKey == null || #isEditOrderHeaderKey == "" ? "Add to Cart" : "Add to Order"}'/>" />
					</div>
				</form> <%-- / QuickAddForm --%>
			
				<div class="clearfix"></div>
				
				<hr size="1" color="#cdcdcd" class=" addmargintop20" />
				
				<%-- Copy/Paste --%>
				<h3 class="addmargintop10 addpadleft20">Copy and Paste</h3>
				
				<div class="qa-copy-instructions">
					<p class="qa-psmall">
						Paste or type the <s:property value="wCContext.storefrontId" /> item numbers or customer item numbers in the following format:
					</p>
					<p class="qa-plarge addmargintop12">
						<strong>Item Number,Quantity</strong>
					</p>
					<p class="qa-psmall addmargintop20">
						Examples:
					</p>
					<p class="qa-plarge">
						<strong>50052121,12</strong> (item with quantity)
					</p>
					<p class="qa-plarge">
						<strong>50052121</strong> (item without quantity)
					</p>
				</div>
				
        		<form class="pushleft addpadleft20 addpadtop15">
					<div class="qa-copywrap">
						<textarea name="items" id="copypaste-text" class="qa-copypaste" rows="10"></textarea>
						<div class="qa-itemlimit">Item Limit 200</div>
						<div class="floatright">
							<input type="button" id="btn-reset-copy-paste" class="btn-neutral" value="Reset" />
						</div>
					</div>
					<div class="clearfix"></div>
					<div class="bottom-btn">
						<input type="button" id="btn-add-to-list" class="btn-gradient floatright addmarginright20" value="Add to List" />
					</div>
					<div id="copypaste-error" class="error floatleft" style="display:none;"></div>
				</form>
			</div> <%-- / container --%>
		</div> <%-- main --%>
	</div> <%-- / main-container --%>
	
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx.swc.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery-ui.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/common/XPEDXUtils<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	
	<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
	<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bgiframe.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
	<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/order/QuickAdd<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/order/XPEDXOrder<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/order/XPEDXItemAssociation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/orderAdjustment<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jqdialog/jqdialog<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
</body>
</html>
