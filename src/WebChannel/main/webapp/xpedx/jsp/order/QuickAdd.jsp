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
	<!--[if IE]> 
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-ie-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" /> 
	<![endif]--> 
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/ORDERS<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<!--[if IE]>
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/ie-hacks<s:property value='#wcUtil.xpedxBuildKey' />.css" />
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
	<s:set name='isImported' value="%{#_action.isImported()}"/>	
	<s:hidden id="isImportedValue"	value='%{#isImported}' />
	<s:set name='csvVar' value="%{#_action.isCsvVar()}"/>	
	<s:hidden id="csvVarValue"	value='%{#csvVar}' />
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
    
 <div >
     <div class="loading-icon" style="display:none;"></div>
</div>
	
	<s:url id="addProductsToOrderURLid" namespace="/order" action="draftOrderAddOrderLines" /> 
	<s:a cssClass="display:none;" id="addProductsToOrderURL" href="%{#addProductsToOrderURLid}" />
	
	<s:url id="ajaxAddItemsToCartURLid" namespace="/order" action="ajaxAddItemsToCart" /> 
	<s:a cssClass="display:none;" id="ajaxAddItemsToCartURL" href="%{#ajaxAddItemsToCartURLid}" />
	
	<s:url id="quickAddURLid" namespace="/order" action="quickAdd">
		<s:param name="addedToCart" value="true" />
	</s:url> 
	<s:a cssClass="display:none;" id="quickAddURL" href="%{#quickAddURLid}" />
	<a style="display: none;" id="dlgImportFormLink" href="#dlgImportForm" />
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

				<div class="content-container">
					<h1>Quick Add</h1>
					<form name="QuickAddForm" id="QuickAddForm"
						onsubmit="validateItems(); return false;">
						<label></label>
						 <div class="floatright"> 
							<input id="importItemList" class="btn-neutral addmarginright120 addmargintop-5" type="button" onclick="" value="Import"/>
						</div> 
							<div class="qa-rightcol">
								<p>Select Item Type</p>
							</div> <!-- START wctheme.select.ftl --> <!-- START wctheme.controlheader.ftl -->
							<div id="wwgrp_qaItemType" class="wwgrp">
								<div id="wwctrl_qaItemType" class="wwctrl">
									<!-- END controlheader.ftl -->
									<s:select id="qaItemType" name="qaItemType"
										cssStyle="width:135px;" headerKey="1" list="skuTypeList"
										listKey="key" listValue="value" />
							</div>
						</div> <!-- END select.ftl --> <s:hidden name="#qaItemType.type"
								value="ItemID" id="#qaItemType_type" />
						<div class="clearfix"></div>
						<div class="center">
								<s:if test='%{!#csvVar}'>
									<p id="csvCheck" class="error">The import file must be
										in .csv format.</p>
								</s:if>
								<s:set name='dataSize' value='xpedxQuickAddCsvVOMap.size()' />
								<s:if test='%{#dataSize>"200"}'>
									<p id="dataSize" class="error">Quick Add has exceeded
										the maximum of 200 items. Please remove some items and try
										again.</p>
								</s:if>
							</div>
						<div class="qa-add-wrap">
							<div class="qa-add-divider"></div>
							<s:set name="jobIdFlag"
								value='%{customerFieldsMap.get("CustLineAccNo")}' />
							<s:set name="customerPONoFlag"
								value='%{customerFieldsMap.get("CustomerPONo")}' />
							
							<s:if test='%{#customerPONoFlag != null && !#customerPONoFlag.equals("")}'>
							<div class="qa-button-wrap">
								<input type="submit"
									class="btn-gradient floatright "
									value="<s:property value='%{#isEditOrderHeaderKey == null || #isEditOrderHeaderKey == "" ? "Add to Cart" : "Add to Order"}'/>" />
							<input
									id="btn-clear-all"
									class="btn-neutral floatright addmarginright10" value="Clear"
									type="button" onclick="clearAll()"/>
							</div>
							</s:if>
							<s:else>
							<div class="qa-button-wrap-alt ">
							
								<input type="submit" class="btn-gradient floatright "
									value="<s:property value='%{#isEditOrderHeaderKey == null || #isEditOrderHeaderKey == "" ? "Add to Cart" : "Add to Order"}'/>" />
							<input
									id="btn-clear-all"
									class="btn-neutral floatright addmarginright10" value="Clear"
									type="button" onclick="clearAll()"/>
							</div>
							</s:else>
							<s:hidden id="orderHeaderKey" value='%{#orderHeaderKey}' />
							<s:hidden id="currencyCode" value='%{#currencyCode}' />
							<s:hidden id="isEditOrder" name="isEditOrder"
								value="%{(#isEditOrderHeaderKey != null && #isEditOrderHeaderKey != '')}" />
							<h3 class="qa-subhead">Add Item List</h3>
							<%-- <s:set name="jobIdFlag"
								value='%{customerFieldsMap.get("CustLineAccNo")}' />
							<s:set name="customerPONoFlag"
								value='%{customerFieldsMap.get("CustomerPONo")}' /> --%>

							<div class="qa-listheader ">
								<div class="label-item">Item</div>
								<div class="label-qty">Qty</div>
								<s:if
									test='%{#customerPONoFlag != null && !#customerPONoFlag.equals("")}'>
									
									<div class="label-po">
										<s:property value='#customerPONoFlag' />
									</div>
									
								</s:if>
								<s:if test='%{#jobIdFlag != null && !#jobIdFlag.equals("")}'>
								
									<div class="label-account">
										<s:property value='#jobIdFlag' />
									</div>
									
								</s:if>
							</div>
							 <s:if test='%{#isImported}'>
								<s:iterator value='#_action.getQuickaddItemLines()'
									status="itemline">
									<s:set name='index' value='%{#itemline.count}' />
									<s:set name='XPEDXQuickAddCsvVO' value='%{xpedxQuickAddCsvVOMap.get(#index)}'/>
									<s:div id='%{"qa-listrow_" + #itemline.count}'
										cssStyle='%{#itemline.count > #_action.getDisplayItemLines() ? "display:none;" : ""}'>
										<div class="qa-listrow">
											<div class="qa-error-icon" style="visibility: hidden">
												<input type="image"
													id="errorIcon_<s:property value='#itemline.count'/>"
													src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_red_x<s:property 
											value='#wcUtil.xpedxBuildKey' />.png" />
											</div>
											<div class="label-item">
													<input type="text" maxlength="27" size="15"
														id="enteredProductIDs_<s:property value='#itemline.count'/>"
														name="enteredProductIDs" class="inputfloat input-item"
														value="<s:property value='%{#XPEDXQuickAddCsvVO.getItemId()}'/>"
														onfocus="javascript:showQuickAddRow(<s:property value='%{#itemline.count + 1}'/>)" />
											</div>
											<div class="label-qty">
													<input maxlength="7" size="8" type="text"
														id="enteredQuantities_<s:property value='#itemline.count'/>"
														name="enteredQuantities" class="inputfloat input-qty" 
														value="<s:property value='%{#XPEDXQuickAddCsvVO.getQty()}'/>"
														onKeyUp="return isValidQuantityRemoveAlpha(this,event)" />
											</div>
											<s:if
												test='%{#customerPONoFlag != null && !#customerPONoFlag.equals("")}'>
												<div class="label-po">
													<input maxlength="22" size="15" type="text"
														name="enteredPONos" class="inputfloat input-po"
														value="<s:property value='%{#XPEDXQuickAddCsvVO.getItemPONumber()}'/>"
														id="enteredPONos_<s:property value='#itemline.count'/>" />
												</div>
											</s:if>
											<s:if test='%{#jobIdFlag != null && !#jobIdFlag.equals("")}'>
												<div class="label-account">
													<input maxlength="24" size="20" type="text"
														id="enteredJobIDs_<s:property value='#itemline.count'/>"
														name="enteredJobIDs" class="inputfloat input-account" value="<s:property value='%{#XPEDXQuickAddCsvVO.getItemLineNumber()}'/>" />
												</div>
											</s:if>
											<div class="error producterrorLine" style="display: none;"
												id="producterrorLine_<s:property value='#itemline.count'/>"></div>

											<%-- These inputs are required for the backend to process, not visible in UI --%>
											<s:hidden cssClass="input-uom" name="enteredUOMs" value="" />
											<%-- populated by javascript before submission --%>
											<s:hidden cssClass="input-itemType" name="enteredItemTypes"
												value="" />
											<%-- populated by javascript before submission --%>
											<s:hidden name="enteredProductDescs" value="" />
											<%-- always empty --%>
											<s:hidden name="quickAddOrderMultiple" value="1" />
											<%-- always 1 (quick add ignores order multiple) --%>
										</div>
									</s:div>
								</s:iterator>
								</s:if>
						<s:else>
							<s:iterator value='#_action.getQuickaddItemLines()'
								status="itemline">
								<s:div id='%{"qa-listrow_" + #itemline.count}'
									cssStyle='%{#itemline.count > #_action.getDisplayItemLines() ? "display:none;" : ""}'>
									<div class="qa-listrow">
										<div class="qa-error-icon" style="visibility: hidden">
											<input type="image" id="errorIcon_<s:property value='#itemline.count'/>"  src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_red_x<s:property 
											value='#wcUtil.xpedxBuildKey' />.png"  />
										</div>
										<div class="label-item">
											<input type="text" maxlength="27" size="15"
												id="enteredProductIDs_<s:property value='#itemline.count'/>"
												name="enteredProductIDs" class="inputfloat input-item"
												onfocus="javascript:showQuickAddRow(<s:property value='%{#itemline.count + 1}'/>)" />											
										</div>
										<div class="label-qty">
											<input maxlength="7" size="8" type="text"
												id="enteredQuantities_<s:property value='#itemline.count'/>"
												name="enteredQuantities" class="inputfloat input-qty"
												onKeyUp="return isValidQuantityRemoveAlpha(this,event)" />
										</div>
										<s:if
											test='%{#customerPONoFlag != null && !#customerPONoFlag.equals("")}'>
											<div class="label-po">
												<input maxlength="22" size="15" type="text"
													name="enteredPONos" value="" class="inputfloat input-po"
													id="enteredPONos_<s:property value='#itemline.count'/>" />
											</div>
								
										</s:if>
										<s:if test='%{#jobIdFlag != null && !#jobIdFlag.equals("")}'>
											<div class="label-account">
												<input maxlength="24" size="20" type="text"
													id="enteredJobIDs_<s:property value='#itemline.count'/>"
													name="enteredJobIDs" class="inputfloat input-account" />
											</div>
										</s:if>
										<div class="error producterrorLine" style="display: none;"
											id="producterrorLine_<s:property value='#itemline.count'/>"></div>

										<%-- These inputs are required for the backend to process, not visible in UI --%>
										<s:hidden cssClass="input-uom" name="enteredUOMs" value="" />
										<%-- populated by javascript before submission --%>
										<s:hidden cssClass="input-itemType" name="enteredItemTypes"
											value="" />
										<%-- populated by javascript before submission --%>
										<s:hidden name="enteredProductDescs" value="" />
										<%-- always empty --%>
										<s:hidden name="quickAddOrderMultiple" value="1" />
										<%-- always 1 (quick add ignores order multiple) --%>
									</div>
								</s:div>
								<%-- / qa-listrow --%>
							</s:iterator>
						</s:else>
							<%-- quickaddItemLines --%>
							<s:if
							test='%{#customerPONoFlag != null && !#customerPONoFlag.equals("")}'>
							<div class="qa-button-wrap addpadtop20">
								<input type="submit"
									class="btn-gradient floatright "
									value="<s:property value='%{#isEditOrderHeaderKey == null || #isEditOrderHeaderKey == "" ? "Add to Cart" : "Add to Order"}'/>" />
							<input
									id="btn-clear-all"
									class="btn-neutral floatright addmarginright10" value="Clear"
									type="button" onclick="clearAll()"/>
							</div>
							</s:if>
							<s:else>
							<div class="qa-button-wrap-alt addpadtop20">
								<input type="submit"
									class="btn-gradient floatright "
									value="<s:property value='%{#isEditOrderHeaderKey == null || #isEditOrderHeaderKey == "" ? "Add to Cart" : "Add to Order"}'/>" />
							<input
									id="btn-clear-all"
									class="btn-neutral floatright addmarginright10" value="Clear"
									type="button" onclick="clearAll()"/>
							</div>
							</s:else>
						</div>
						<!------close qa-add-wrap------->
					</form>
					<%-- / QuickAddForm --%>

					<div class="clearfix"></div>


					<%-- Copy/Paste --%>
					<div class="qa-copy-wrap">
						<div class="qa-divider"></div>
						<div class="qa-marketing-copy">
							<span>Looking for a fast way to add items?</span><br /> It's as
							easy as using copy and paste from your file with the format
							below.
						</div>
						<h3 class="qa-subhead">
							Copy and Paste <span>(replaces content above.)</span>
						</h3>
						<form>



							<div class="qa-copywrap">
								<textarea name="items" id="copypaste-text" class="qa-copypaste"
									rows="10"></textarea>
								<div class="qa-itemlimit">Item Limit 200</div>

							</div>
							<div class="qa-copy-instructions">
								<p class="qa-psmall">
									Paste or type the
									<s:property value="wCContext.storefrontId" />
									item numbers or customer item numbers in the following format:
								</p>
								<p class="qa-plarge addmargintop12">
									<strong>Item Number,Quantity</strong>
								</p>
								<p class="qa-psmall addmargintop20">Examples:</p>
								<p class="qa-plarge">
									<strong>50052121,12</strong> (item with quantity)
								</p>
								<p class="qa-plarge">
									<strong>50052121</strong> (item without quantity)
								</p>
							</div>

							<div class="clearfix"></div>
							<div class="qa-button-wrap-copy">
								<input id="btn-add-to-list" class="btn-gradient floatright"
									value="Add to List" type="button" /> <input
									id="btn-reset-copy-paste"
									class="btn-neutral floatright addmarginright10" value="Clear"
									type="button" />
							</div>
							<div id="copypaste-error" class="error floatleft"
								style="display: none;"></div>
						</form>
						<div class="clearfix"></div>
						<div class="qa-divider"></div>
					</div>
					<!--------close qa-copywrap------->
				</div>
				<!------close content-container------>
			</div> <%-- / container --%>
		</div> <%-- main --%>
	</div> <%-- / main-container --%>
	<s:include value="../modals/XPEDXQuickAddItemImportModal.jsp" />
	<div class="loading-wrap"  style="display:none;">
         <div class="load-modal" ></div>
    </div>
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
