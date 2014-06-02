<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<%
	request.setAttribute("isMergedCSSJS","true");
%>
<s:set name='_action' value='[0]' />
<s:bean	name='com.sterlingcommerce.xpedx.webchannel.common.XPEDXSCXmlUtils'	id='xutil' />
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />
<s:bean	name='com.sterlingcommerce.webchannel.catalog.utils.CatalogUtilBean' id='catalogUtil' />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean' id='xpedxutil' />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils' id='utilMIL' />
<s:set name='scuicontext' value="uiContext" />
<s:set name="isEditOrderHeaderKey" value ="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute(@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@EDITED_ORDER_HEADER_KEY)}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<swc:html isXhtml="true">
<head>
	<meta content='IE=8' http-equiv='X-UA-Compatible' />
	<meta name="DCSext.w_x_sc_count" content="1"/>
	<meta name="DCSext.w_x_itemtype" content="<s:property value='%{#session.itemType}' />" />
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/ORDERS<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
	<!--[if IE]>
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/ADMIN<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<![endif]-->
	
	<title><s:property value="wCContext.storefrontId" /> - <s:property value="wCContext.storefrontId" /> Product Details</title>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/common/XPEDXUtils<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-jquery-headder<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<s:include value="../order/XPEDXRefreshMiniCart.jsp"/>
	<!-- Web Trends tag start -->
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/webtrends/displayWebTag<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/PriceAndAvailability<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<!-- Web Trends tag end  -->
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/XPEDXItemDetails<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
</head>
<body class="ext-gecko ext-gecko3">
	
	<div>
	     <div class="loading-icon" style="display:none;"></div>
	</div>
	
	<s:url id='addToCartURLid' namespace='/order' action='addToCart' includeParams="none" />
	<s:hidden id="addToCartURL" value="%{#addToCartURLid}" />
	
	<s:hidden id="isGuestUser" value="%{#_action.getWCContext().isGuestUser()}" />
	<s:hidden id="isSalesRep" value="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute('IS_SALES_REP') ? 'true' : 'false'}" />
	<s:hidden id="goBackFlag" value="%{#_action.getGoBackFlag()}" />
	<s:hidden id="backPageUrl" value="%{#session.itemDtlBackPageURL.substring(#session.itemDtlBackPageURL.indexOf('/swc'))}" />
	<s:hidden id="currentCartInContextOHK" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache('OrderHeaderInContext')" />
	<s:hidden id="isEditOrder" value="%{#isEditOrderHeaderKey}" />
	
	<s:url id='getMyItemsListURLid' includeParams="none" namespace="/myItems" action='MyItemsList'/>
	<s:hidden id="getMyItemsListURL" value="%{#getMyItemsListURLid}" />
	
	<s:url id="getPriceAndAvailabilityForItemsURLid" action="getPriceAndAvailabilityForItems" namespace="/catalog" />
	<s:hidden id="getPriceAndAvailabilityForItemsURL" value="%{#getPriceAndAvailabilityForItemsURLid}" />
	
	<s:url id='addItemURLid' includeParams='none' escapeAmp="false" namespace="/order" action="xpedxAddItemsToList" />
	<s:hidden id="addItemURL" value="%{#addItemURLid}" />
	
	<s:url id='myItemsDetailsChangeShareListURL' includeParams='none' action='XPEDXMyItemsDetailsChangeShareListForItemDetail' namespace="/myItems" escapeAmp="false" />
	<s:hidden id="myItemsDetailsChangeShareListURL" value="%{#XPEDXMyItemsDetailsChangeShareListURLid}" />
	
	<s:if test='%{#_action.getCustomerUOM() == #_action.getBaseUOM()}'>
		<s:set name="baseUOMDesc" value="#customerUomWithoutM" />										
	</s:if>
	<s:else>
		<s:set name="baseUOMDesc" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#_action.getBaseUOM())" />
	</s:else>
	<s:hidden id="baseUOMDesc" value="%{#baseUOMDesc}" />
	
	<s:hidden name="webtrendItemType" id="webtrendItemType" value="%{#session.itemType}"/>
	
	<s:url id="xpedxItemDetailsPandAURL" namespace="/catalog" action="xpedxItemDetailsPandA"/>
	<s:hidden name="xpedxItemDetailsPandAURLid" id="xpedxItemDetailsPandAURLid" value="%{#xpedxItemDetailsPandAURL}"/>
	
	<s:hidden name="catagory" id="catagory" value="%{#_action.getCatagory()}" />
	<s:hidden id="custUOM" name="custUOM" value="%{#_action.getCustomerUOM()}" />

	<s:set name="itemListElem" value="itemListElem" />
	<s:if test="%{null != #xutil.getChildElement(#itemListElem, 'Item')}">
		<s:set name="itemElem" value='#xutil.getChildElement(#itemListElem,"Item")' />
		
		<s:set name='itemID' value='#xutil.getAttribute(#itemElem,"ItemID")' />
		<s:set name='unitOfMeasure'	value='#xutil.getAttribute(#itemElem,"UnitOfMeasure")' />
		<s:set name="prodMweight" value="%{#_action.getProdMweight()}"/>
		<s:set name="pricingUOMConvFactor" value="%{#_action.getPricingUOMConvFactor()}"/>
		<s:set name="pricingUOMConvFactor" value="%{#_action.getPricingUOMConvFactor()}"/>
		
		<s:hidden name="itemID" id="itemID" value="%{#itemID}" />
		<s:hidden id="unitOfMeasure" name="unitOfMeasure" value="%{#unitOfMeasure}" />	
		<s:hidden id="prodMweight" name="prodMweight" value="%{#prodMweight}" />
		<s:hidden id="pricingUOMConvFactor" name="pricingUOMConvFactor" value="%{#pricingUOMConvFactor}" />
			
		<s:set name="itemElemExtn"	value='#xutil.getChildElement(#itemElem,"Extn")' />
		<s:set name='certFlag'	value="#xutil.getAttribute(#itemElemExtn, 'ExtnCert')" />
		<s:set name="primaryInfoElem" value='#xutil.getChildElement(#itemElem,"PrimaryInformation")' />
		<s:set name="itemAssets" value='#xutil.getChildElement(#itemElem,"AssetList")' />
		<s:set name="itemMainImages" value='#catalogUtil.getAssetList(#itemAssets,"ITEM_IMAGE_1")' /> 
		<s:set name='pImg' value='%{#imageLocation+"/"+#primaryInfoElem.getAttribute("ImageID")}' />
		<s:if test='%{#pImg=="/"}'>
			<s:set name='pImg' value='%{"/xpedx/images/INF_150x150.jpg"}' />
		</s:if>
		<s:set name="isStocked" value="isStocked" />
		<s:set name="orderMultiple" value="orderMultiple" />
		<s:set name='showCurrencySymbol' value='true' />
		<s:set name='currency' value='#xutil.getAttribute(#itemListElem,"Currency")' />
		<s:if test='%{#kitCode == "BUNDLE" }'>
			<s:set name='price'	value='#xutil.getAttribute(#computedPrice,"BundleTotal")' />
		</s:if>
		<s:else>
			<s:set name='price'	value='#xutil.getAttribute(#computedPrice,"UnitPrice")' />
		</s:else>
		<s:if test="displayPriceForUoms.size() > 0">
			<s:set name='price' value='%{displayPriceForUoms.get(2)}' />
			<s:set name='formattedUnitprice' value='#xpedxutil.formatPriceWithCurrencySymbol(#scuicontext,#currency,#price,#showCurrencySymbol)' />
		</s:if> 
	
		<s:form name="OrderDetailsForm" id="OrderDetailsForm" namespace="/order" action="xpedxAddItemsToList">
			<s:hidden name="orderHeaderKey" value='%{#appFlowContext.key}' />
			<s:hidden name="draft" value="%{#draftOrderFlag}" />
			<s:hidden name='Currency' value='%{#currencyCode}' />
			<s:hidden name='mode' value='%{#mode}' />
			<s:hidden name='fullBackURL' value='%{#appFlowContext.returnURL}' />
			<s:hidden name="orderLineKeyForNote" id="orderLineKeyForNote" value="" />
		
			<s:hidden name="listKey" id="listKey" value="" />
			<s:hidden name="selectedLineItem" value="1" />
			<s:hidden name="orderLineKeys" value="1" />
			<s:hidden name="orderLineItemOrders" value="" />
		
			<s:hidden name="orderLineItemIDs" value="%{#itemID}" />
			
			<s:set name="primaryInfoElem" value='#xutil.getChildElement(#itemElem,"PrimaryInformation")' />
			<s:hidden name="orderLineItemNames" value='%{#utilMIL.formatEscapeCharacters(#xutil.getAttribute(#primaryInfoElem, "ShortDescription"))}' />
			<s:hidden name="orderLineItemDesc" value='%{#utilMIL.formatEscapeCharacters(#xutil.getAttribute(#primaryInfoElem, "Description"))}' />
		
			<s:hidden name="orderLineQuantities" value="%{#xutil.getAttribute(#primaryInfoElem, 'MinOrderQuantity')}" />
			<s:hidden name="orderLineCustLineAccNo" value=" " />
			<s:hidden name="itemUOMs" value=" " />
			<s:hidden name="sendToItemDetails" value="true" />
		
			<s:hidden name="itemID" value="%{#itemID}" />
			<s:hidden name="unitOfMeasure" value="%{#parameters.unitOfMeasure}" />
			<s:hidden name="customerLinePONo" value="" />
		</s:form>
	</s:if>
	
	<div id="main-container">
		<div id="main">
			<s:action name="xpedxHeader" executeResult="true" namespace="/common" >
				<s:param name='shipToBanner' value="%{'true'}" />
			</s:action> 
			<div class="container content-container detail-view" id="containerId">
				
				<p class="addmarginbottom15"><a href="javascript:goBack();">&lsaquo; Back</a></p>
				
				<div id="errorMessageDiv"></div>
				<h1 style="font-weight:bold; "><s:property	value='#xutil.getAttribute(#primaryInfoElem,"ShortDescription")' /></h1>
				<div id="printButton" class="print-ico-xpedx underlink">
					<img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/print-icon.gif" alt="Print Page" height="15" width="16"/>Print Page
				</div>
				<div class="clearfix"></div>
				<div class="specs-wrap">
					<%-- TODO Item specifications --%>
				</div>
				<div class="image-order-container">
					<div class="detail-image-wrap">
						<ul id="prodlist">
							<s:property value='#xutil.getAttribute(#primaryInfoElem,"Description")' escape="false"/>
						</ul>
						<s:if test="#itemMainImages != null && #itemMainImages.size() > 0">
							<s:set name='imageMainLocation'	value="#xutil.getAttribute(#itemMainImages[0], 'ContentLocation')" />
							<s:set name='imageMainId' value="#xutil.getAttribute(#itemMainImages[0], 'ContentID')" />
							<s:hidden name="hdn_imageMainId" value="%{#imageMainId}" />
							<s:set name='imageMainLabel' value="#xutil.getAttribute(#itemMainImages[0], 'Label')" />
							<s:set name='imageMainURL'	value="#imageMainLocation + #imageMainId " />
							<s:if test='%{#imageMainURL=="/"}'>
								<s:set name='imageMainURL' value='%{"/xpedx/images/INF_150x150.jpg"}' />
							</s:if>
							<img src="<s:url value='%{#imageMainURL}' includeParams='none'/>" class="prodImg" id="productImg1" alt="<s:text name='%{#imageMainLabel}'/>" />
						</s:if>
						<s:else>
							<img src="<s:url value='%{#pImg}'/>"  class="prodImg" id="productImg1" alt="<s:text name='%{#pImg}'/>"/>
						</s:else>
						
						<s:set name="xpedxItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_ITEM_LABEL"/>
						<s:set name="customerItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CUSTOMER_ITEM_LABEL"/>
						<s:set name="manufacturerItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MANUFACTURER_ITEM_LABEL"/>
						<s:set name="mpcItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MPC_ITEM_LABEL"/>
						<div class="item-numbers"><b><s:property value="wCContext.storefrontId" /> <s:property value="#xpedxItemLabel" />: <s:property value='%{#itemID}' /></b>											
							<s:if test='certFlag=="Y"'>
								<img border="none" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/catalog/green-e-logo_small.png" alt="" />
							</s:if>
						</div>
						<s:if test= '%{#_action.getExtnMfgItemFlag()== "Y"}'>
							<div class="mfg-numbers"><s:property value="#manufacturerItemLabel" />: <s:property value='ManufacturerPartNumber' /></div>
						</s:if>
						<s:if test= '%{#_action.getExtnCustomerItemFlag()== "Y"}'>
							<div class="cust-numbers"><s:property value="#customerItemLabel" />: <s:property value='custPartNumber' /></div>
						</s:if>
						<s:if test='%{#isStocked !="Y"}'>
							<div class="mill-mfg-message">Mill / Mfg. Item - Additional charges may apply</div>
						</s:if>
						<s:if test="msdsLinkMap != null && msdsLinkMap.size() > 0">
							<div class="detail-msds-button">
								<s:iterator value="msdsLinkMap" id="msdsMap" status="status" >
										<s:set name="link" value="value" />
										<s:set name="desc" value="key" />
										<input name="" type="button"  class="btn-neutral" value="MSDS" onclick="window.open('<s:property value='#link'/>');"/>
								</s:iterator>									
							</div>
						</s:if>
					</div> <%-- / detail-image-wrap --%>
					
					<div class="order-wrap">
						<p><s:property value='#xutil.getAttribute(#itemElemExtn,"ExtnSellText")' escape="false"/></p>
						<s:if test='%{#_action.getWCContext().isGuestUser()}'>
							<div class="addpadtop20 addpadright20">
								<h4>Price and availability are available to registered customers only. Please contact us at 1-888-973-3976 to learn more.</h4>
							</div>
						</s:if>
						<s:else>
							<div class="order-input-wrap">													
								<s:set name="addToCartDisabled"	value="%{''}" />
								<s:if test='%{(#catalogUtil.hasAccessToAddtoCart(#primaryInfoElem,#formattedUnitprice))=="Y"}'>
									<s:set name="addToCartDisabled" value="%{''}" />
								</s:if> 
								<s:if test="%{!#isReadOnly && !(#_action.getWCContext().isGuestUser() == true) && #addToCartDisabled == ''}">
									<div class="order-row">
										<s:hidden id="Qty_Check_Flag" name="Qty_Check_Flag" value="false"/>
										<div class="order-label">Qty:</div>
										<div class="order-input">
											<s:textfield name='qtyBox' id="%{'Qty_' + #itemID}" size="7" maxlength="7"	
													value="%{#_action.getRequestedQty()}"
													theme="simple" onkeyup="isValidQuantityRemoveAlpha(this,event);"
													onchange="isValidQuantity(this); qtyInputCheck(this);"
													onmouseover="qtyInputCheck(this);"
													/>
											<s:set name="mulVal" value='itemOrderMultipleMap[#itemID]' />
											<s:hidden name="c" id="OrderMultiple" value="%{#mulVal}" />
											<s:set name="itemuomMap" value='#_action.getDisplayItemUOMsMap()' />
											<s:select name="itemUOMsSelect" id="%{'itemUomList_' + #itemID}" onchange='updateUOMFields()'
													cssClass="qty_selector" list="itemuomMap" listKey="key"
													listValue="value" value='%{#_action.getRequestedDefaultUOM()}' disabled="%{#isReadOnly}" theme="simple"
													/>
											<s:set name="requestedUOM"  value="%{#_action.getRequestedUOM()}" />
											<s:hidden name="selectedUOM" value="%{#_action.getRequestedDefaultUOM()}" id="selectedUOM" />	
											<s:set name="convFac" value='itemIdConVUOMMap[#requestedUOM]' />
											<s:hidden name="uomConvFactor" id="uomConvFactor" value="%{#convFac}" />
											<s:iterator value='itemIdConVUOMMap'>
												<s:set name='currentUomId' value='key' />
												<s:set name='currentUomConvFact' value='value' />
												<s:hidden name='convF_%{#currentUomId}' id="convF_%{#currentUomId}" value="%{#currentUomConvFact}" />
											</s:iterator>												
										</div> <%-- / order-input --%>
									</div> <%-- / order-row --%>
								</s:if>
								<s:if test=' (isCustomerPO == "Y") '>
									<div class="order-row">
										<div class="order-label"><s:property value='customerPOLabel' />:</div>
										<div class="order-input">
											<s:textfield name='customerPONo' theme="simple" cssClass="x-input bottom-mill-info-avail" id="customerPONo" value=""  maxlength="22" size="30" />
										</div>
									</div>
								</s:if>
								<s:if test=' (isCustomerLinAcc == "Y") '>
									<div class="order-row">
										<div class="order-label"><s:property value='custLineAccNoLabel' />:</div>
										<div class="order-input">
											<s:textfield name='custLineAccNo' theme="simple" cssClass="x-input bottom-mill-info-avail" id="custLineAccNo" value="" maxlength="24" size="30" />
										</div>
									</div>
								</s:if>		
							</div> <%-- / order-input-wrap --%>
							
							<div class="item-button-wrap">
								<s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
									<input name="button" type="button" onclick="addItemToCart();" class="btn-gradient floatright  addmarginright18" value="Add to Cart"/>
								</s:if>
								<s:else>
									<input name="button" type="button" onclick="addItemToCart();" class="btn-gradient floatright  addmarginright18" value="Add to Order"/>
								</s:else>						
								<input name="button" class="btn-neutral floatright  addmarginright10"  value="Add to List" onclick="addItemToWishList(); return false;" type="button" />
								<s:include value="../modals/XPEDXSelectWishListModal.jsp" />
								
								<div class="show-pa">
									<a href="javascript:getPriceAndAvailabilityForItems({modal:true, items:['<s:property value='%{#itemID}' />'], success:successCallback_PriceAndAvailability});">Update Price &amp; Availability</a>
								</div>
							</div> <%-- / item-button-wrap --%>
							
							<s:div id="%{'errorMsgForQty_' + #itemID}" cssClass="addmarginbottom20" cssStyle="display:inline-block;"></s:div>
						</s:else> <%-- / if-else guest user --%>
						
						<s:if test="(replacementAssociatedItems!=null && replacementAssociatedItems.size() > 0)">
							<div class="replacement-item">
								This item will be replaced once inventory is depleted.<br/>Select item:
								<s:iterator value='replacementAssociatedItems' id='replacementItem' status="count" >											
									<s:set name="promoItemPrimInfoElem" value='#xutil.getChildElement(#replacementItem,"PrimaryInformation")' />
									<s:set name="promoItemComputedPrice" value='#xutil.getChildElement(#replacementItem,"ComputedPrice")' />
									<s:set name="itemAssetList" value='#xutil.getElementsByAttribute(#replacementItem,"AssetList/Asset","Type","ITEM_IMAGE_1" )' />
									<s:if test='#itemAssetList != null && #itemAssetList.size() > 0'>
										<s:set name="itemAsset" value='#itemAssetList[0]' />
										<s:set name='imageLocation' value="#xutil.getAttribute(#itemAsset, 'ContentLocation')" />
										<s:set name='imageId' value="#xutil.getAttribute(#itemAsset, 'ContentID')" />
										<s:set name='imageLabel' value="#xutil.getAttribute(#itemAsset, 'Label')" />
										<s:set name='imageURL' value="#imageLocation + '/' + #imageId " />
										<s:if test='%{#imageURL == "/"}'>
											<s:set name='imageURL' value='%{"/xpedx/images/INF_150x150.jpg"}' />
										</s:if>
									</s:if>
									<s:url id='detailURLFromPromoProd' namespace='/catalog'	action='itemDetails.action'>
										<s:param name='itemID'><s:property value='#xutil.getAttribute(#replacementItem,"ItemID")' /></s:param>
										<s:param name='unitOfMeasure'><s:property	value='#xutil.getAttribute(#replacementItem,"UnitOfMeasure")' /></s:param>
									</s:url>
									<s:if test='#count.index != 0'><span>,</span>&nbsp;</s:if>
									<s:a href="%{detailURLFromPromoProd}"  ><s:property value='#xutil.getAttribute(#replacementItem,"ItemID")' /></s:a>									
								</s:iterator>
							</div> <%-- / replacement-item --%>
						</s:if>
						<div class="pa-wrap">
							<%-- This will be filled by ajax as the PnA call happens on page load as Ajax --%>
							<div style="display: none;" id="availabilty_<s:property value='%{#itemID}' />" class="price-and-availability"></div>
						</div>
					</div> <%-- / order-wrap --%>
				</div> <%-- / image-order-container --%>
				
				<s:include value="ItemDetailsPromotions.jsp" />
			</div> <%-- / content-container --%>
		</div> <%-- / main --%>
	</div> <%-- / main-container --%>
	
	<div class="loading-wrap"  style="display:none;">
         <div class="load-modal" ></div>
    </div>
	
	<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
	
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.liquidcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
</body> 
</swc:html>
