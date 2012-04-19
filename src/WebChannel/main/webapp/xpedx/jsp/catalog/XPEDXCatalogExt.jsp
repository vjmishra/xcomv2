<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ taglib prefix="s" 		uri="/struts-tags"%>
<%@ taglib prefix="swc" 	uri="/WEB-INF/swc.tld"%>
<%@ taglib prefix="c" 		uri="/WEB-INF/c.tld"%>
<%@ taglib prefix="xpedx" 	uri="/WEB-INF/xpedx.tld"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta content='IE=8' http-equiv='X-UA-Compatible' />
<%
  		request.setAttribute("isMergedCSSJS","true");
  	  %>

<s:set name="isUserAdmin" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@isCurrentUserAdmin(wCContext)" />
<%-- <s:set name="CurrentCustomerId" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@getCurrentCustomerId(wCContext)" /> --%>
<s:set name="storefrontId" value="wCContext.storefrontId" />
<%-- <s:set name='errorQtyGreaterThanZero' value='<s:text name="MSG.SWC.CART.ADDTOCART.ERROR.QTYGTZERO" />' scope='session'/> --%>
<%--<s:bean name='com.sterlingcommerce.xpedx.webchannel.common.XPEDXSCXmlUtils' id='xpedxSCXmlUtil' /> --%>

<!-- begin styles. These should be the only three styles. -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/CATALOG.css" />
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />

<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE.css" />
<![endif]-->
<!-- end styles -->


<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header.js"></script>

<!--<script type="text/javascript" src="../xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="../xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="../xpedx/js/catalog/catalogExt.js"></script>
--><script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-jquery-headder.js"></script>

<!-- end carousel scripts js   -->
<title><s:property value="wCContext.storefrontId" /> - <s:text name='Catalog_Page_Title' /></title>

	
</head>
<body class="ext-gecko ext-gecko3"  onload="highlightRows()">



<s:set name='_action' value='[0]'/>
<s:set name="isEditOrderHeaderKey" value ="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute(@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@EDITED_ORDER_HEADER_KEY)}"/>
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
<%-- <s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean' id='xpedxutil' />	--%>
<s:bean	name='com.sterlingcommerce.webchannel.catalog.utils.CatalogUtilBean' id='catUtil' />
<%-- <s:bean name='com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils' id='utilMIL' /> --%>

<s:url action='navigate.action' namespace='/catalog' id='myUrl' />
<s:url action='setSelectedView' namespace='/catalog' id='selectedViewURL' />
<s:set name='myParam' value='{"searchTerm", "", "cname"}' />
<s:url id='addToCartURL' namespace='/order' action='addToCart' />
<%-- <s:url id='punchOutURL' namespace='/order' action='configPunchOut' /> --%>
<%-- <s:url id='selectedViewURL' action='setSelectedView' namespace='/catalog' /> --%>
<s:set name='appFlowContext' value='#session.FlowContext' />
<%-- <s:set name='isFlowInContext' value='#util.isFlowInContext(#appFlowContext)' /> --%>

<%-- Component Included --%>
<swc:breadcrumb id="searchBreadcrumb" rootURL='#myUrl' group='catalog' displayGroup='search' displayParam='#myParam' />

<%-- <s:url id='punchOutURLOrderChange' namespace='/order'
	action='configPunchOut'>
	<s:param name='orderHeaderKey' value='%{#appFlowContext.key}' />
	<s:param name='currency' value='%{#appFlowContext.currency}' />
	<s:param name='flowID' value='%{#appFlowContext.type}' />
</s:url> --%>

<%-- <s:url id='addToMyItemListURLid' namespace='/xpedx/myItems' action='XPEDXMyItemsDetailsAddFromCatalog'/>
<!--  <s:a id='addToMyItemListURL' href='%{#addToMyItemListURLid}'/> --> --%>
<s:set name='isProcurementInspectMode'	value='#util.isProcurementInspectMode(wCContext)' />
<s:set name='isReadOnly' value='#isProcurementInspectMode' />
<s:set name='catDoc' value='%{outDoc.documentElement}' />
<s:set name='numResult' value='#catDoc.getAttribute("TotalHits")' />
<s:set name='pageNumber' value='#catDoc.getAttribute("PageNumber")' />
<s:set name='totalNumberOfPages'	value='#catDoc.getAttribute("TotalPages")' />
<%-- <s:set name='catDoc' value='%{outDoc.documentElement}' /> --%>
<%-- <s:set name='numResult' value='#catDoc.getAttribute("TotalHits")' /> --%>
<%-- <s:set name='pageNumber' value='#catDoc.getAttribute("PageNumber")' /> --%>
<%-- <s:set name='totalNumberOfPages'value='#catDoc.getAttribute("TotalPages")' /> --%>
<s:url id='compareURL' action='prodCompare' namespace='/catalog' />
<s:set name='guestUser' value="#_action.getWCContext().isGuestUser()" />
<s:set name="xpedxCustomerContactInfoBean" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("XPEDX_Customer_Contact_Info_Bean")' />
<s:set name='b2bViewFromDB' value="#xpedxCustomerContactInfoBean.getExtnB2BCatalogView()" />
<%-- <s:set name="customerUseSKU" value ='%{#_action.getWCContext().getWCAttribute("customerUseSKU")}'/> --%>
<%-- <s:set name="customerItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CUSTOMER_ITEM_LABEL"/> --%>
<%-- <s:set name="manufacturerItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MANUFACTURER_ITEM_LABEL"/> --%>
<%-- <s:set name="mpcItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MPC_ITEM_LABEL"/> --%>

<%-- <s:set name="itemUomHashMap" value ='%{#_action.getItemUomHashMap()}'/> --%>
<%-- <s:set name="defaultShowUOMMap" value ='%{#_action.getDefaultShowUOMMap()}'/> <!-- Code added to fix XNGTP 2964 --> --%>
<%-- <s:set name="orderMultipleMap" value ='%{#_action.getOrderMultipleMap()}'/> <!-- Code added to fix XNGTP 2964 --> --%>

<%-- <s:set name="plLineMap" value ='%{#_action.getPLLineMap()}'/> --%>
<s:url id='addToCartURLId' namespace="/xpedx/myItems" action='addMyItemToCart' />
<s:hidden id='addItemToCartURL' value='%{#addToCartURLId}' />
<s:set name='currentCartInContextOHK' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("OrderHeaderInContext")'/>
<%--
<s:if test="#currentCartInContextOHK == null ">
<s:set name='currentCartInContextOHK' value='@com.sterlingcommerce.xpedx.webchannel.order.utilities.XPEDXCommerceContextHelper@getCartInContextOrderHeaderKey(#_action.getWCContext())'/>
</s:if>
 --%>
<s:hidden id='currentCartInContextOHKVal' value='%{#currentCartInContextOHK}' />
<s:url id='checkAvailabilityURL' action='getCatalogItemAvailabilty' namespace="/catalog" />
<s:hidden id='checkAvailabilityURLId' value='%{#checkAvailabilityURL}' />
<%-- <s:url id='orderMultipleURL' namespace="/common" action='getOrderMultiple' /> Code added to fix XNGTP 2964
<s:hidden name="orderMultipleURL" value='%{#orderMultipleURL}'/>
 --%>
<s:url id='getItemUomsURL' namespace="/common" action='getItemUomsURL' />
<s:hidden name="getItemUomsURL" value='%{#getItemUomsURL}'/>

<s:set name="catalogView" value='#_action.getSelectedView()' /><!-- added for fix     XNGTP-216 used to get selected view -->

<!-- start of code for Promotions Jira 2599 -->
<s:set name="patheTemp" value='#_action.getPath()'/>
<!-- End of code for Promotions Jira 2599 -->

<swc:breadcrumbScope>
	<div id="main-container">
	<s:if test='!#guestUser'>  
		<div id="main">
	</s:if>
	<s:else>
		<div id="main" class="anon-pages">
	</s:else>
	<s:if test='!#guestUser'>  
		<s:action name="xpedxHeader" executeResult="true" namespace="/common" >
			<s:param name='shipToBanner' value="%{'true'}" />
		</s:action>
	</s:if>
	<s:else>
		<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
	</s:else>

		<%--
		<!-- ship to banner todo -->
		<s:set name='customerId' value="wCContext.customerId" />
		<s:set name="shipToCustomerDisplayStr" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@formatBillToShipToCustomer(#customerId)" />
		<!--  <div class="ship_banner"> -->
		<!--	<p><a href="#">[Change]</a>Shopping for <span class="italic"><s:property value="#shipToCustomerDisplayStr"/>,<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getLoggedInCustomerFromSession(wCContext)" />, <s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDefaultShipTo()" />,<s:property value="wCContext.storefrontId" /></span></p>-->
		<!--</div>--> --%>
	<div class="container catalog"><!-- // begin t1-main-content -->
	
	<!--  removed old bread crumb -->
	
	<!--  <div class="t1-main-content" id="navigateContainer"> -->
	<!-- add content here for main content -->
	<!-- Breadcrumb -->
	<div id="catalog-promo-ad-horizontal">
			

			

			<!--Webtrands Start -->
			<meta name="WT.ti" content="<s:property value='%{#parameters.cname}' />"/>
			<!--Webtrands End -->
		 	
			<!-- Header promotion Start-->
			<div id="catalog-image-rotation"> 
					<%-- 			
					<div class="slideshow"> 
							<s:action name="xpedxDynamicPromotions" executeResult="true" namespace="/common" >
								<s:param name="callerPage">catalog</s:param>
								<s:param name="categoryPath" value='#parameters.path'/>
							</s:action>
					</div>  
					--%>
					
					<div class="slideshow"> 
							<s:action name="xpedxDynamicPromotionsAction" executeResult="true" namespace="/common" >
							  <s:param name="callerPage">CatalogPage</s:param>
							  <!-- Start of code for Promotions Jira 2599 -->
							  <s:param name="categoryPath" value="#patheTemp"/>
							  <!-- End of code for Promotions Jira 2599 -->
							</s:action>
					</div>
				
				<s:if test="#request['imageCounter'] > 1" >
	
					<div id="catalog-image-rotation-nav"> 
							<div class="img-navi-left"></div> 
							<div id="catalog-image-rotation-nav-inner"></div> 
							<div class="img-navi-right"></div> 
					</div> 
					</s:if>
			</div> 		
			<!-- Header promotion End-->


			<div class="catalog-ad"><div class="ad-float smallBody" style="float: none;">
			<img class="float-left" height="4" width="7" alt="" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/mil/ad-arrow.gif" style="margin-top: 5px; padding-right: 5px;">advertisement</div>
				<!-- Ad Juggler Tag Starts -->
				<s:set name='ad_keyword' value='' />
				<s:set name='firstItem1' value='%{firstItem}' />
				<s:set name='catPath' value='%{categoryPath}' />
				
				<s:set name="cat2Val" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getCatTwoDescFromItemIdForpath(#firstItem1,#storefrontId,#catPath)" />
			
				<s:if test="#cat2Val != null" >
					<s:set name='ad_keyword' value='#cat2Val' />
				</s:if>

		<%-- aj_server: https://rotator.hadj7.adjuggler.net:443/servlet/ajrotator/ --%>		
		<s:if test="#ad_keyword != null" >
			<s:if test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT}' >
				<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118165'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<s:property value="%{#ad_keyword}" />';
				aj_pv = true; aj_click = '';
				</script>
			</s:if>
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CANADA_STORE_FRONT}' >
								<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118201'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<s:property value="%{#ad_keyword}" />';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif>
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@BULKLEY_DUNTON_STORE_FRONT}' >
									<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118194'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<s:property value="%{#ad_keyword}" />';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif >
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_STORE_FRONT}' >
				<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '115714'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<s:property value="%{#ad_keyword}" />';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif>
			<s:else>
				<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '115714'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<s:property value="%{#ad_keyword}" />';
				aj_pv = true; aj_click = '';
				</script>
			</s:else>			
		</s:if>	
		<s:else>
					<s:if test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT}' >
				<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118165'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<%=session.getAttribute("CUST_PREF_CATEGORY_DESC")%>';
				aj_pv = true; aj_click = '';
				</script>
			</s:if>
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CANADA_STORE_FRONT}' >
								<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118201'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<%=session.getAttribute("CUST_PREF_CATEGORY_DESC")%>';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif>
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@BULKLEY_DUNTON_STORE_FRONT}' >
									<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118194'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<%=session.getAttribute("CUST_PREF_CATEGORY_DESC")%>';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif >
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_STORE_FRONT}' >
				<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '115714'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<%=session.getAttribute("CUST_PREF_CATEGORY_DESC")%>';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif>
			<s:else>
				<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '115714'; aj_page = '0'; aj_dim ='114881'; aj_ch = ''; aj_ct = ''; aj_kw = '<%=session.getAttribute("CUST_PREF_CATEGORY_DESC")%>';
				aj_pv = true; aj_click = '';
				</script>
			</s:else>
		</s:else>
		<script type="text/javascript" language="JavaScript" src="https://img.hadj7.adjuggler.net/banners/ajtg.js"></script>
		
		<!-- Ad Juggler Tag Ends -->
			
			
			</div>
			<div class="clearall">&nbsp;</div>

		
	</div>		
		<div id="catalog-header-breadcrumbs" >

				<s:form name='narrowSearch' action='search' namespace='/catalog'>
				<div class="searchbox-form1">
					<div  class="catalog-search-container"> <!-- -FX1- tile="search tooptip"  -->
						<input class="x-input" id="search_searchTerm" value="Search Within Results..." name="searchTerm"
						tabindex="1002" type="text" onkeydown="javascript:validateQuote(event)" onclick="javascript:context_newSearch_searchTerm_onclick(this)" /> 
						<button type="submit" class="searchButton"  tabindex="1003" title="Search" onclick="javascript:setDefaultSearchText();"></button>
						<s:set name="checkedval1" value="%{getWCContext().getWCAttribute('StockedCheckbox')}"/>
						<s:hidden id="stockedItem" name="stockedItem" value="%{#checkedval1}"/> 
					</div>	
				</div>		
				</s:form>
						
				<div id="breadcrumbs-list-name">
				<div id="breadcrumb-my-selection" style="margin-left:11px; padding-right: 4px; text-align: left;">My Selection</div>
				 <s:url value='/xpedx/images/icons/12x12_charcoal_x.png' id='rbtn' />  <!-- -FX2- tile="Remove"  -->
				
				<span class="breadcrumbs-inner" id="searchBreadcrumb" >
				<!--  <img  class="breadcrumb-x" src="/swc/xpedx/images/icons/12x12_charcoal_x.png" alt="Remove" title="Remove" id='rbtn' />  -->
					<xpedx:breadcrumbDisplay displayRootName='Catalog' breadcrumbSeparator=' / ' removable='true' removeIcon='#rbtn' startTabIndex='2' />
				</span>
				</div>
			<div class="clearall">&nbsp;</div>
		</div>
		<!-- END breadcrumb -->
	
	<s:include value='XPEDXNarrowBy.jsp' />
	<!--  end of new content -->
	
	<div class="t1-main-content" id="navigateContainer"> 
	
	 <div class="pagination">
                 <div class="sortbycontrols"> <span class="checkboxtxt">Sort By:&nbsp;</span>
				 <s:set name='sortList'	value='#{"relevancy":"Relevancy", "Item.ItemID--A":"Item # (Low to High)", "Item.ItemID--D":"Item # (High to Low)", "Item.SortableShortDescription--A":"Description (A to Z)", "Item.SortableShortDescription--D":"Description (Z to A)"}' />
                     <select name="pageSize" class="xpedx_select_sm" tabindex="81" id="sortFieldUpper"
						onchange="javascript:processSortByUpper()">
                		<%-- <s:iterator id='sortField' value='%{sortFieldList}'> --%>
                		<%-- <s:if test='%{#sortField.key.equals(sortField)}'> --%>
							<s:iterator id='sortListFeild' value='#sortList'>
                         	<s:if test='%{#sortListFeild.key.equals(sortField)}'>
								<option selected value="<s:property value='#sortListFeild.key'/>"><s:property
									value='#sortListFeild.value' /></option>
							</s:if>
							<s:else>
								<option value="<s:property value='#sortListFeild.key'/>"><s:property
									value='#sortListFeild.value' /></option>
							</s:else>		
							</s:iterator>							
						<%-- </s:if> --%>
						<%-- <s:else> --%>
							<!-- <option  value="<s:property value='#sortField.key'/>"><s:property
							value='#sortField.value' /></option> -->
						<%-- </s:else>	--%>
						<%-- </s:iterator> --%>
                     </select>
                     <span class="checkboxtxt">Show:&nbsp;</span>
                      <s:set name='pageNumList'	value='#{"20":"20 per page", "40":"40 per page", "60":"60 per page"}' />
                     	<select name="pageSize" class="xpedx_select_sm" tabindex="81"
								id="pageSizeUpper" onchange="javascript:processPageSizeUpper();">
								
                         	<s:iterator id='pageSizeField' value='#pageNumList'>
                         	<s:if test='%{#pageSizeField.key.equals(pageSize)}'>
								<option selected value="<s:property value='#pageSizeField.key'/>"><s:property
									value='#pageSizeField.value' /></option>
							</s:if>
							<s:else>
								<option value="<s:property value='#pageSizeField.key'/>"><s:property
									value='#pageSizeField.value' /></option>
							</s:else>		
							</s:iterator>
                    	 </select>
                     
                    <s:if test="#isGuestUser == false">	
                     <s:set name="checkedval" value="%{getWCContext().getWCAttribute('StockedCheckbox')}"/>	
                     <select id="stockedItemChkBtm" name="stockedItemChkBtm" value="#checkedval" onchange="javascript:setNormallyStockedSelectDropDownBottom();setStockItemFlag();">
							<option value="false">All Items</option>
							<s:if test='#checkedval'>
								<option value="true" selected="selected">Normally Stocked</option>
							</s:if>							
							<s:else>
								<option value="true">Normally Stocked</option>
							</s:else>
                     </select> 
                    </s:if>
                 </div>   
               
                 <p class="pageresults"><s:property value='#numResult' /> Results&nbsp;|<span>&nbsp;Page&nbsp
                 <!-- Webtrend Meta Tag start -->
                 <%--added for jira 3317 --%>
                 <s:if test="%{#totalNumberOfPages == 0 || #totalNumberOfPages == 1}">
				 <s:property value="%{#pageNumber}"/>
		 </s:if></span>
                 <s:if test='searchMetaTag == true'>
					<META Name="DCSext.w_x_ss" content="1">
					<META Name="WT.ossr" content="<s:property value='%{#numResult}' />">
					<META Name="WT.oss" content="2">
				</s:if>
 				<!-- Webtrend Meta Tag endt -->
 
                 	<s:url id="goToPageURL" action="goToPage">
					<s:param name="pageNumber" value="'{0}'" />
					</s:url> <swc:pagectl currentPage='%{#pageNumber}'
						lastPage='%{#totalNumberOfPages}' showFirstAndLast='False'
						urlSpec='%{#goToPageURL}' cssClass='pageresults' startTabIndex='51' />
				  </p>
          </div>
	
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
		namespace="/xpedx/myItems" method="post"
	>
	

	
		<p><strong>List Name:</strong>&nbsp;&nbsp;<input type="text" name="listName" id="listName" value="" maxlength="255" /></p>
		<p><strong>Description:</strong>&nbsp;&nbsp;<input type="text" name="listDesc" id="listDesc" value="" onkeyup="javascript:maxLength(this,'255');" maxlength="255" /></p>
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
			value="<s:property value="wCContext.loggedInUserId"/>" 
		/> 
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
		<!-- Placeholder for the dynamic content -->

		<s:set name="displayStyle" value="%{''}" />
		<s:if test="%{!#isUserAdmin}">
			<s:set name="displayStyle" value="%{'display: none;'}"/>
		</s:if>
		
		<div style="<s:property value="#displayStyle"/>" id="dynamiccontent">
		<s:div id="dlgShareListShared">
			<input type="checkbox" <s:property value="#saCV"/> name="shareAdminOnly" id="shareAdminOnly" value="Y" /> Edit by Admin users only<br />
			<br />
	
		<a href="javascript:shareSelectAll(true)" >Select All</a>
		<a href="javascript:shareSelectAll(false)" >Deselect All</a>
			
			<!-- START - BODY OF SHARE FORM -->
			<s:div id="divMainShareList" cssClass="grey-msg x-corners">
				<!-- CONTENT WILL GO HERE -->
			</s:div>
			<!-- END - BODY OF SHARE FORM -->
		</s:div>
	
	</div>

	
	
		</br>
		</br>
		<ul id="tool-bar" class="tool-bar-bottom" style="border-top: 1px solid #CCCCCC; padding-top: 20px;">
			<li><a class="grey-ui-btn" href="javascript:clearCreateNewList();"><span>Cancel</span></a></li>
			<li style="float: right;"><a href="javascript:submitSL();"> <img
				src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/ui-buttons/ui-btn-save.gif" width="49" height="23" alt="Save" title="Save" /> </a></li>
		</ul>
	
	</s:form> 
	<!-- CODE_END MIL - PN -->
	</div>
</div>
<!-- END of new MIL - PN -->

<%--  I don't think this piece of code is used in this page. Commenting:12/29/2011 - Vignesh
<!-- START - Adding the MIL dropdown - PN -->				
			<s:form name="OrderDetailsForm" id="OrderDetailsForm" namespace="/order" action="xpedxAddItemsToList">
				<s:hidden name="orderHeaderKey" value='%{#appFlowContext.key}' />
				<s:hidden name="draft" value="Y" />
				<s:hidden name='Currency' value="USD" />
				<s:hidden name='mode' value=' ' />
				<s:hidden name='fullBackURL' value=' ' />
				<s:hidden name="orderLineKeyForNote" id="orderLineKeyForNote" value="" />

				<s:hidden name="listKey" id="listKey" value="" />
				<s:hidden name="selectedLineItem" value="1" />
				<s:hidden name="orderLineKeys" value="1" />

				<s:hidden name="orderLineItemIDs" value="%{#itemID}" />
				<s:hidden name="orderLineItemNames" value='%{#xutil.getAttribute(#primaryInfoElem, "ShortDescription")}' />
				<s:hidden name="orderLineItemDesc" value='%{#xutil.getAttribute(#primaryInfoElem, "Description")}' />
				<s:hidden name="orderLineQuantities" value="%{#xutil.getAttribute(#primaryInfoElem, 'MinOrderQuantity')}" />
				<s:hidden name="orderLineItemOrders" value="" />
				<s:hidden name="orderLineCustLineAccNo" value=" " />
				<s:hidden name="itemUOMs" value=" " />
				<s:hidden name="sendToItemDetails" value="true" />

				<s:hidden name="itemID" value="%{#itemID}" />
				<s:hidden name="unitOfMeasure" value="%{#parameters.unitOfMeasure}" />

			</s:form>
				
				<s:url id='addItemsToListURLid' namespace='/order' action='xpedxAddItemsToList' includeParams="none" /> 
				<s:a id='addItemsToListURL' href='%{#addItemsToListURLid}' /> 
--%>
				<%--
				<div style="display: none;">
				<s:action name="XPEDXMyItemsList" executeResult="true" namespace="/xpedx/myItems">
					<s:param name="filterByAccChk" value="%{true}" />
					<s:param name="filterByShipToChk" value="%{true}" />
					<s:param name="filterByMyListChk" value="%{true}" />
					<s:param name="displayAsDropdownList" value="%{true}" />
				</s:action>
				</div> --%>


				<s:set name="allowedColumns" value="columnList" />
				<s:set name='itemList' value='XMLUtils.getChildElement(#catDoc, "ItemList")'/>
				<%--<s:url id='shipImg' value='/swc/images/common/shippable.jpg'/>--%>
				<%-- <s:url id='shipImg' value='/swc/images/theme/theme-1/Shippable.png'/> --%>
				<%--<s:url id='availImg' value='/swc/images/common/available.jpg'/>--%>
				<%-- <s:set name='scuicontext' value="#_action.getWCContext().getSCUIContext()" /> --%>
				<%-- <s:set name='wcContext' value="#_action.getWCContext()" /> --%>
				<%-- <s:set name='currency' value='#itemList.getAttribute("Currency")' /> --%>
				<%-- <s:set name='showCurrencySymbol' value='true' /> --%>
				<%-- <s:set name="isEditOrderHeaderKey" value ="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute(@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@EDITED_ORDER_HEADER_KEY)}"/> --%>
				<%-- Do not remove pImg. This is used in XPEDXItemsDataTemplateComponent--%>
				<s:url id='pImg' value='/xpedx/images/INF_150x150.jpg'/>
				
<%--  I don't think this piece of code is used in this page. Commenting:12/29/2011 - Vignesh

<script type="text/javascript">

function addItemsToList(idx, itemId, name, desc, qty, uom){
	name = unescape(name);
	desc = unescape(desc);
	try{ console.debug("List Key: " + currentAadd2ItemList.value); }catch(e){}
	try{ console.debug("idx: " + idx + ", name: " + name  + ", desc: " + desc  + ", qty: " + qty + ", uom: " + uom + ", ItemId: " + itemId); }catch(e){}
	//return;
	
	if (idx == 1){
		$("#dlgShareListLink").trigger('click');
	} else if (idx > 1){
		//Ext.Msg.wait("Adding item to list... Please wait.");
		xpedx_working_start();
		setTimeout(xpedx_working_stop, 3000);

		var reqIndex;
		for(i=0 ; i < itemCountList.length ; i++)
		{
			if(itemCountList[i].listKeyId==currentAadd2ItemList.value){
				reqIndex = i;
				break;
			}
		}
		
		document.OrderDetailsForm.orderLineItemIDs.value 	= itemId;
		document.OrderDetailsForm.itemID.value 				= itemId;
		document.OrderDetailsForm.orderLineItemNames.value 	= name;
		document.OrderDetailsForm.orderLineItemDesc.value 	= desc;
		document.OrderDetailsForm.orderLineQuantities.value = qty;
		document.OrderDetailsForm.orderLineItemOrders.value = Number(itemCountList[reqIndex].itemCount)+1;
		document.OrderDetailsForm.itemUOMs.value 			= uom;
		document.OrderDetailsForm.unitOfMeasure.value 		= uom;
		document.OrderDetailsForm.listKey.value 			= currentAadd2ItemList.value;
		
		<s:url id='AddItemURL' includeParams='none' escapeAmp="false" namespace="/order" action="xpedxAddItemsToList" />
		
		var url = "<s:property value='#AddItemURL'/>";
		url = ReplaceAll(url,"&amp;",'&');

		if(itemCountList[reqIndex].itemCount<200){
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
					itemCountList[reqIndex].itemCount++;
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
	}
	
	currentAadd2ItemList.selectedIndex = 0;
}					
</script>
--%>

<!-- END - Adding the MIL dropdown - PN -->

	
	<div id="quickViewLaunchTitle" class="hidden">Quick View</div>
	<!-- START wctheme.form.ftl --> <!-- START wctheme.form-validate.ftl -->
	<!-- END wctheme.form-validate.ftl -->
	<form id="productListForm" name="productListForm"
		onsubmit="return 
true;"
		action="/swc/catalog/prodCompare.action?sfId=xpedx&amp;_bcs_=%11true%12%12%12%2Fswc%2Fcatalog%2Fnavigate.action%3FsfId%3Dxpedx%26scFlag%3DY%26%12%12catalog%12search%12%11&amp;scFlag=Y"
		method="post"><!-- END wctheme.form.ftl --> <!-- start Ext list component -->		

<script>	
<%-- //IMPORTANT: The templates are optimized to reduce the space and # of lines in JS for performance reasons. Please maintain this in future. --%>
var itemWin;			
var catalog = [{title: 'Search Results',items: [<s:iterator id='item' value='XMLUtils.getElements(#catDoc, "//ItemList/Item")' status='prodStatus'>{<xpedx:catalogResultInit ItemElement='#item' currency='#itemList.getAttribute("Currency")'/>}<s:if test='!#prodStatus.last'>,</s:if></s:iterator>]}];

function getNormalView() {
return new Ext.XTemplate(
'<div id="item-ct">',
 '<tpl for=".">','<dl>','<tpl for="items">','<dd id="{itemkey}" class="itemdiv">',
   '<div class="imgs">','<a href="javascript:processDetail(\'{itemid}\',\'{uom}\');">','<img title="{name}" alt="{name}" src="{icon}" class="prodImg" id="pimg_{#}"/></a>',
    '<div class="hidden bubble extDescDiv" id="extDescDiv_{#}"></div>',
   '</div>',
  '<div class="contents">','<p class="pprice">{price}</p>','<div class="descriptions">','<a id="item-detail-lnk" href="javascript:processDetail(\'{itemid}\',\'{uom}\');" tabindex="{tabidx}">',
   '<p class="ddesc">{name}</p></a>',
   '<div class="buttons"><a href="javascript:processDetail(\'{itemid}\',\'{uom}\');" >{buttons}</a></div></div>',
   '<div class="clearBoth">&nbsp;</div>',
  '</div>',
  '<table class="bottable">','<tr>','<td class="compare_check">',
	// Do not delete this code. This will come as a CR. '<input type="checkbox" name="compare_{itemkey}" id="compare_{itemkey}" />','<label for="compare_{itemkey}">Compare</label>',
  '</td>','<td class="item_number">','<s:property value="wCContext.storefrontId" /> Item #: {itemid} {cert}','</td>',
  '<td class="quantity_box">',<s:if test='!#guestUser'>'Qty:&nbsp;<input type="textfield" id=\'Qty_{itemid}\'  name=\'Qty_{itemid}\' value="" size="7" maxlength="7" onkeyup="javascript:isValidQuantityRemoveAlpha(this,event);" onclick="javascript:setFocus(this);" onchange="javascript:isValidQuantity(this);javascript:qtyInputCheck(this, \'{itemid}\');" onmouseover="javascript:qtyInputCheck(this,  \'{itemid}\');"/>','<input type="hidden" id="Qty_Check_Flag_{itemid}" name="Qty_Check_Flag_{itemid}" value="false"/>','{uomdisplay}',</s:if>
  '</td>','</tr>',
  '<tr>','<td></td>','<td class="item_number">{partno}</td>',
  '<td class="add_to_cart"><input type="hidden" name="isEditOrder" id="isEditOrder" value="<s:property value='#isEditOrderHeaderKey'/>"/>',<s:if test='!#guestUser'><s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
   '<div class="addtocart"><a class="" id=\'addtocart_{itemid}\' href="#"  onclick=\"javascript:addItemToCart(\'{itemid}\'); return false;\">Add to Cart</a></div>',
  </s:if><s:else>
   '<div class="addtocart"><a class="" id=\'addtocart_{itemid}\' href="#"  onclick=\"javascript:addItemToCart(\'{itemid}\'); return false;\">Add to Order</a></div>',
  </s:else>		
   '<div class="availablelink">',<s:if test='!#guestUser'>
	'<input type=\'hidden\' id=\'baseUOMs_{itemid}\' name=\'baseUOMs_{itemid}\' value=\'{uomDesc}\'/>',  
	'<div class=\"itemOption\"><a href=\"javascript:void(0);\" class=\"submitBtnBg1 underlink\" style=\"padding-left:12px; font-weight: normal; \" onclick=\"displayAvailability(\'{itemid}\');\">My Price &amp; Availability</a></div>',</s:if>
   '</div>',
  </s:if>
  '</td>','</tr>',
  '<tr>','<td colspan="3">',<s:if test='!#guestUser'>'<div class="uomLink" id="errorMsgForQty_{itemid}">{uomLink}</div>',</s:if>'</td>','</tr>',
  <s:if test='!#guestUser'>'<tr>','<td style="height:auto;"></td>','<td class="mill-mfg" colspan="2">{itemtypedesc}</td>','</tr>',//<!-- End mill/mfg -->
  '<tr class="line_error">','<td colspan="3">','<div class=\'error\' id=\'errorMsgForQty_{itemid}\' style=\'display : none\'/>{qtyGreaterThanZeroMsg}</div>','</td>','</tr>',</s:if>
  '</table>',
  '<div class="clearBoth">&nbsp;</div>','<div class="show-hide-wrap">','<div style="display: none;" id="availabilty_{itemid}" class="price-and-availability">','</div>','</div>',
 '</dd>','</tpl>',  '</dl>','</tpl><div style="clear:left"></div>',
'</div>'	
);
}

function getCondensedView() {
return new Ext.XTemplate(
'<div id="item-ct">',
 '<tpl for=".">','<dl>','<tpl for="items">','<dd id="{itemkey}" class="itemdiv" style="height:auto;">',
  '<div class="imgs">',
   '<a href="javascript:processDetail(\'{itemid}\',\'{uom}\');">','<img title="{name}" alt="{name}" src="{icon}" class="prodImg" id="pimg_{#}"/></a>',
   '<div class="hidden bubble extDescDiv" id="extDescDiv_{#}"></div>',
  '</div>','<div class="contents">',
   '<p class="pprice">{price}</p>','<div class="descriptions">',
   '<a id="item-detail-lnk" href="javascript:processDetail(\'{itemid}\',\'{uom}\');" tabindex="{tabidx}">','<p class="ddesc">{name}</p></a>',
   '<div class="buttons"><a href="javascript:processDetail(\'{itemid}\',\'{uom}\');" >{buttons}</a></div></div>',
   '<div class="clearBoth">&nbsp;</div>',
  '</div>',
  '<table class="bottable">','<tr>','<td class="compare_check">',
   // Do not delete this code. This will come as a CR.'<input type="checkbox" name="compare_{itemkey}" id="compare_{itemkey}" />','<label for="compare_{itemkey}">Compare</label>',
   '</td>','</tr>',
   '<tr>','<td class="item_number">','<s:property value="wCContext.storefrontId" /> Item #: {itemid} {cert}','</td>',
    '<td class="quantity_box">',<s:if test='!#guestUser'>'Qty:&nbsp;<input type="textfield" id=\'Qty_{itemid}\'  name=\'Qty_{itemid}\' value="" size="7" maxlength="7" onkeyup="javascript:isValidQuantityRemoveAlpha(this,event);" onclick="javascript:setFocus(this);"  onchange="javascript:isValidQuantity(this);javascript:qtyInputCheck(this, \'{itemid}\');" onmouseover="javascript:qtyInputCheck(this,  \'{itemid}\');"/>','<input type="hidden" id="Qty_Check_Flag_{itemid}" name="Qty_Check_Flag_{itemid}" value="false"/>',</s:if>'</td>','</tr>',
   '<tr>','<td class="item_number">{partno}</td>',
    '<td class="uom_cell">',<s:if test='!#guestUser'>'{uomdisplay}',</s:if>'</td>','</tr>',
   '<tr>','<td class="mill-mfg">{itemtypedesc}</td>',
    '<td class="add_to_cart">',<s:if test='!#guestUser'>'<input type="hidden" name="isEditOrder" id="isEditOrder" value="<s:property value='#isEditOrderHeaderKey'/>"/>',<s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">'<div class="addtocart"><a class="" id=\'addtocart_{itemid}\' href="#"  onclick=\"javascript:addItemToCart(\'{itemid}\'); return false;\">Add to Cart</a></div>',</s:if><s:else>'<div class="addtocart"><a class="" id=\'addtocart_{itemid}\' href="#"  onclick=\"javascript:addItemToCart(\'{itemid}\'); return false;\">Add to Order</a></div>',</s:else>
    '<input type=\'hidden\' id=\'baseUOMs_{itemid}\' name=\'baseUOMs_{itemid}\' value=\'{uomDesc}\'/>',</s:if>'</td>',
   '</tr>','<tr>',
    '<td colspan="2">',<s:if test='!#guestUser'>'<div class="uomLink" id="errorMsgForQty_{itemid}">{uomLink}</div>',</s:if>'</td>',
   '</tr>','<tr>','<tr>','</tr>','<td class="line_error" colspan="2" style="width:50px;">',
    <s:if test='!#guestUser'>'<div class=\'error\' id=\'errorMsgForQty_{itemid}\' style=\'display : none\'/> {qtyGreaterThanZeroMsg} </div>',</s:if>
   '</td>','</tr>',
   '</table>',
   '<div class="clearBoth">&nbsp;</div>',				
 '</dd>','</tpl>','</dl>','</tpl><div style="clear:left"></div>',
'</div>'                        
);
}

function getMiniView() {
return new Ext.XTemplate(
'<div id="item-ct">',
 '<tpl for=".">','<dl>','<tpl for="items">','<dd id="{itemkey}" class="itemdiv">',
  '<div class="imgs">','<a href="javascript:processDetail(\'{itemid}\',\'{uom}\');" >','<img title="{name}" alt="{name}" src="{icon}" class="prodImg" id="pimg_{#}"/></a>',
   '<div class="hidden bubble extDescDiv" id="extDescDiv_{#}"></div>',
  '</div>','<div class="contents">',
   '<p class="pprice">{price}</p>','<div class="descriptions">',
   '<a id="item-detail-lnk" href="javascript:processDetail(\'{itemid}\',\'{uom}\');" tabindex="{tabidx}">','<p class="ddesc">{name}</p></a>',
   '<div class="buttons"><a href="javascript:processDetail(\'{itemid}\',\'{uom}\');" >{buttons}</a></div></div>','<div class="clearBoth">&nbsp;</div>',
   '<div class="item_number"><s:property value="wCContext.storefrontId" /> Item #: {itemid} {cert}<br />{partno} {itemtypedesc}</div>',
   '<div class="quantity_box">',
	'<div class="qty">',<s:if test='!#guestUser'>'Qty:&nbsp;<input type="textfield" id=\'Qty_{itemid}\'  name=\'Qty_{itemid}\' value="" size="7" maxlength="7" onkeyup="javascript:isValidQuantityRemoveAlpha(this,event);" onclick="javascript:setFocus(this);" onchange="javascript:isValidQuantity(this);javascript:qtyInputCheck(this, \'{itemid}\');" onmouseover="javascript:qtyInputCheck(this,  \'{itemid}\');"/>','<input type="hidden" id="Qty_Check_Flag_{itemid}" name="Qty_Check_Flag_{itemid}" value="false"/>',</s:if>
   '</div>','<div class="uom-select">',<s:if test='!#guestUser'>'{uomdisplay}',</s:if>'</div>',
   '<div class="clearall">&nbsp;</div>',
   <s:if test='!#guestUser'>'<input type="hidden" name="isEditOrder" id="isEditOrder" value="<s:property value='#isEditOrderHeaderKey'/>"/>',<s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey==''" >'<div class="addtocart"><a class="" id=\'addtocart_{itemid}\' href="#" onclick=\"javascript:addItemToCart(\'{itemid}\'); return false;\">Add to Cart</a></div>',</s:if><s:else>'<div class="addtocart"><a class="" id=\'addtocart_{itemid}\' href="#"  onclick=\"javascript:addItemToCart(\'{itemid}\'); return false;\">Add to Order</a></div>',</s:else>
   '<input type=\'hidden\' id=\'baseUOMs_{itemid}\' name=\'baseUOMs_{itemid}\' value=\'{uomDesc}\'/>',</s:if>
   <s:if test='!#guestUser'>'<div class="uomLink" id="errorMsgForQty_{itemid}">{uomLink}</div>','<br/>',</s:if>
  '</div>','<div class="line_error" >',
   <s:if test='!#guestUser'>'<div class=\'error\' id=\'errorMsgForQty_{itemid}\' style=\'display : none\'/>{qtyGreaterThanZeroMsg}</div>',</s:if>
  '</div>','</div>',
  '<div class="clearBoth">&nbsp;</div>',																								
 '</dd>','</tpl>','</dl>','</tpl><div style="clear:left"></div>',
'</div>'                        
);
}
				
function getGridView() {
return new Ext.XTemplate(
'<div class="clearview">&nbsp;</div>',
 '<div id="item-ct">',
  '<table id="x-tbl-cmmn" class="standard-table listTableHeader ${templateName}">','<thead class="table-header-bar">',
  '<tr>','<td class="no-border table-header-bar-left desc-hname"><a href="#" onclick="toggleDescSort();">Description<span id="directionDescArrow"></span></a></td>',
   <s:if test='!#isReadOnly && !#guestUser'>'<td class="M-hname" style="width:26px;" ><span title="Mill / Mfg. Item" id="directionMArrow">M</span></td>',</s:if>
  '<td class="Item-hname"><a href="#" onclick="toggleItemSort();">Item #<span id="directionItemArrow"></span></a></td>',
  <s:if test='#allowedColumns.contains("Size")'>'<td class="Size-hname"><a href="#" onclick="toggleSizeSort();">Size<span id="directionSizeArrow"></span></a></td>',</s:if>
  <s:if test='#allowedColumns.contains("Color")'>'<td class="Color-hname"><a href="#" onclick="toggleColorSort();">Color<span id="directionColorArrow"></span></a></td>',</s:if>
  <s:if test='#allowedColumns.contains("Basis")'>'<td class="Basis-hname"><a href="#" onclick="toggleBasisSort();">Basis<span id="directionBasisArrow"></span></a></td>',</s:if>
  <s:if test='#allowedColumns.contains("Mwt")'>'<td class="Mwt-hname"><a href="#" onclick="toggleMwtSort();">Mwt<span id="directionMwtArrow"></span></a></td>',</s:if>
  <s:if test='#allowedColumns.contains("Capacity")'>'<td class="Capacity-hname"><a href="#" onclick="toggleCapacitySort();">Capacity<span id="directionCapacityArrow"></span></a></td>',</s:if>
  <s:if test='#allowedColumns.contains("Model")'>'<td class="Model-hname"><a href="#" onclick="toggleModelSort();">Model<span id="directionModelArrow"></span></a></td>',</s:if>
  <s:if test='#allowedColumns.contains("Material")'>'<td class="Material-hname"><a href="#" onclick="toggleMaterialSort();">Material<span id="directionMaterialArrow"></span></a></td>',</s:if>
  <s:if test='#allowedColumns.contains("Ply")'>'<td class="Ply-hname"><a href="#" onclick="togglePlySort();">Ply<span id="directionPlyArrow"></span></a></td>',</s:if>				                
  <s:if test='#allowedColumns.contains("Form")'>'<td class="Form-hname"><a href="#" onclick="toggleFormSort();">Form<span id="directionFormArrow"></span></a></td>',</s:if>			                    				  <s:if test='#allowedColumns.contains("Gauge")'>'<td class="Gauge-hname"><a href="#" onclick="toggleGaugeSort();">Gauge<span id="directionGaugeArrow"></span></a></td>',</s:if>			                    
  <s:if test='#allowedColumns.contains("Vendor")'>'<td class="Vendor-hname"><a href="#" onclick="toggleVendorSort();">Mfg. Item #<span id="directionVendorArrow"></span></a></td>',</s:if>
  <s:if test='!#isReadOnly && !#guestUser'><s:if test='#allowedColumns.contains("Environment")'>'<td class="Environment-hname"><a class="underlink" onclick="toggleLeafSort();"><img style="margin-left:0px; display: inline; padding: 5px 0px 5px 5px;" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/catalog/green-e-logo_small.png" ><span id="directionCertArrow"></span></a> </td>',</s:if></s:if>
  <s:else><s:if test='#allowedColumns.contains("Environment")'>'<td class="Environment-hname table-header-bar-right"><a class="underlink" onclick="toggleLeafSort();"><img style="margin-left:0px; display: inline; padding: 5px 0px 5px 5px;" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/catalog/green-e-logo_small.png" ><span id="directionCertArrow"></span></a> </td>',</s:if> </s:else>
  <s:if test='!#isReadOnly && !#guestUser'>'<td class="no border table-header-bar-right lprice-hname"><span style="color:white;">List Price</span></td>',</s:if>
  <s:if test='!#isReadOnly && !#guestUser'>//	'<td class="no border table-header-bar-right sorttable_nosort" align="center">Action</td>',</s:if>
  '</tr>','</thead>',
  '<tpl for=".">','<tbody>',
  '<tpl for="items">',
   '<tr id="{itemkey}" class="itemrow">','<td id="desctab">'+'<a id="item-detail-lnk" href="javascript:processDetail(\'{itemid}\',\'{uom}\');" tabindex="{tabidx}">',
	  '<span class="ddesc desc-hname">{name}</span></a>','</td>',
      <s:if test='!#isReadOnly && !#guestUser'>'<td class="stock-status M-hname">{stocked}</td>',</s:if>'<td>'+
      '<a  id="item-detail-lnk" href="javascript:processDetail(\'{itemid}\',\'{uom}\');" tabindex="{tabidx}">','<span class="ddesc desc-hname">{itemid}</span></a>','</td>',
      <s:if test='#allowedColumns.contains("Size")'>'<td class="Size-hname">{size}</td>',</s:if>
      <s:if test='#allowedColumns.contains("Color")'>'<td class="Color-hname">{color}</td>',</s:if>
      <s:if test='#allowedColumns.contains("Basis")'>'<td class="Basis-hname">{basis}</td>',</s:if>
      <s:if test='#allowedColumns.contains("Mwt")'>'<td class="Mwt-hname">{mwt}</td>',</s:if>
      <s:if test='#allowedColumns.contains("Capacity")'>'<td class="Capacity-hname">{capacity}</td>',</s:if>     			                            			                        
      <s:if test='#allowedColumns.contains("Model")'>'<td class="Model-hname">{model}</td>',</s:if>     				                    
      <s:if test='#allowedColumns.contains("Material")'>'<td class="Material-hname">{material}</td>',</s:if>
      <s:if test='#allowedColumns.contains("Ply")'>'<td class="Ply-hname">{ply}</td>',</s:if>
      <s:if test='#allowedColumns.contains("Form")'>'<td class="Form-hname">{form}</td>',</s:if>
      <s:if test='#allowedColumns.contains("Gauge")'>'<td class="Gauge-hname">{gauge}</td>',</s:if>   
	  <s:if test='#allowedColumns.contains("Vendor")'>'<td class="Vendor-hname">{vendorNumber}</td>',</s:if>
      <s:if test='#allowedColumns.contains("Environment")'>'<td class="Environment-hname" style="padding: 0px 25px 0px 0px;">{cert}</td>',</s:if>
	  <s:if test='!#isReadOnly && !#guestUser'>'<td class="lprice-hname">{listprice}</td>',</s:if>
    '</tr>',
  '</tpl>','</tbody>','</table>','</tpl>',
'</div>');
}

var selectedExtTemplate = null;
var ct = Ext.get('item-box-inner');
</script>


	<div class="normal-view" id="items">
	<div id="items-control">
	<div class="drag-to-compare" id="items-combox">
	<h4><a href="javascript:validationforDragToCompare();" tabindex="41">
	<s:if test="%{#totalNumberOfPages} == 0"> 
	 	<div class="success"> Your search did not yield any results. Please try again. </div>
	 </s:if> 
	 <s:else>
			<s:text name="MSG.SWC.COMP.DRAGTOCOMPARE.GENERIC.PGTITLE" />	
			:<span id="comnum"> <s:text name='No_Items' /></span>
	 </s:else>
	 </a></h4>
	</div>
	<div id="items-cb"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/global/s.gif"
		class="normal-view" title="Full View"><img
		src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/global/s.gif" class="condensed-view"
		title="Condensed View"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/global/s.gif"
		class="mini-view" title="Mini View"><!-- IW 7/16/2010: new icon/button for papergrid-view --><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/global/s.gif" class="papergrid-view"
		title="Grid View">
		
		
	
		
		<s:if test='%{#session.selView != null}'>	
			<input name="selectedView" value="<s:property value='%{#session.selView}' />" id="selectedView" type="hidden" />	
		     <s:set name="selView" value="<s:property value=null />" scope="session"/> 
	   </s:if>
	   <s:else>
	   		<input name="selectedView" value="normal-view" id="selectedView" type="hidden" />
	   </s:else>
		</div>
	</div>
	<%--Added For Jira 3195 --%>
	<s:if test="#numResult == 0"> 
	 <center><div class="error">Your search did not yield any results. Please try again. </div> </center>
	 </s:if> 
	 <%--End For Jira 3195 --%>
	<div id="item-box"><!-- Begin: dynamic filled in by javascript -->
	<div id="item-box-inner"></div>
	<!-- End: dynamic filled in by javascript --></div>
	</div>
	<!-- end Ext list component --> <!-- START wctheme.form-close.ftl --></form>
	<!-- END wctheme.form-close.ftl -->
	
	<div class="clearall">&nbsp;</div>
	<div class="pagination line-spacing">
	<div class="sortbycontrols">
					<span class="checkboxtxt">Sort By:&nbsp;</span> 
					<s:set name='sortList'	value='#{"relevancy":"Relevancy", "Item.ItemID--A":"Item # (Low to High)", "Item.ItemID--D":"Item # (High to Low)", "Item.SortableShortDescription--A":"Description (A to Z)", "Item.SortableShortDescription--D":"Description (Z to A)"}' />
					<select name="pageSize" class="xpedx_select_sm" tabindex="81" id="sortFieldLower" name="sortFieldLower"
						onchange="javascript:processSortByLower()">
                		<%-- <s:iterator id='sortField' value='%{sortFieldList}'> --%>
                		<%-- <s:if test='%{#sortField.key.equals(sortField)}'> --%>
							<s:iterator id='sortListFeild' value='#sortList'>
                         	<s:if test='%{#sortListFeild.key.equals(sortField)}'>
								<option selected value="<s:property value='#sortListFeild.key'/>"><s:property
									value='#sortListFeild.value' /></option>
							</s:if>
							<s:else>
								<option value="<s:property value='#sortListFeild.key'/>"><s:property
									value='#sortListFeild.value' /></option>
							</s:else>		
							</s:iterator>							
						<%-- </s:if> --%>
						<%-- <s:else> --%>
							<!-- <option  value="<s:property value='#sortField.key'/>"><s:property
							value='#sortField.value' /></option> -->
						<%-- </s:else>	--%>
						<%-- </s:iterator> --%>
                     </select>
                     
                     <span class="checkboxtxt">Show:&nbsp;</span>
                      <s:set name='pageNumList'	value='#{"20":"20 per page", "40":"40 per page", "60":"60 per page"}' />
                     	<select name="pageSizeLower" class="xpedx_select_sm" tabindex="81"
								id="pageSizeLower" onchange="javascript:processPageSizeLower();">
								
                         	<s:iterator id='pageSizeField' value='#pageNumList'>
                         	<s:if test='%{#pageSizeField.key.equals(pageSize)}'>
								<option selected value="<s:property value='#pageSizeField.key'/>"><s:property
									value='#pageSizeField.value' /></option>
							</s:if>
							<s:else>
								<option value="<s:property value='#pageSizeField.key'/>"><s:property
									value='#pageSizeField.value' /></option>
							</s:else>		
							</s:iterator>
                    	 </select>
                     <s:if test="#isGuestUser == false">	
                     <s:set name="checkedval" value="%{getWCContext().getWCAttribute('StockedCheckbox')}"/>	
                     <select id="stockedItemChk" name="stockedItemChk" value="#checkedval" onchange="javascript:setNormallyStockedSelectDropDown();setStockItemFlag();">
							<option value="false" selected>All Items</option>
							<s:if test='#checkedval'>
								<option value="true" selected="selected">Normally Stocked</option>
							</s:if>							
							<s:else>
								<option value="true">Normally Stocked</option>
							</s:else>
                     </select> 
                    </s:if>                   	 
	</div>
	
	
	<p class="pageresults"><s:property value='#numResult' /> Results&nbsp;|<span>&nbsp;Page&nbsp
	<s:if test="%{#totalNumberOfPages == 0 || #totalNumberOfPages == 1}">
		<s:property value="%{#pageNumber}"/>
	</s:if></span>
	<s:url id="goToPageURL" action="goToPage">
		<s:param name="pageNumber" value="'{0}'" />
	</s:url> 
	<swc:pagectl currentPage='%{#pageNumber}'
		lastPage='%{#totalNumberOfPages}' showFirstAndLast='False'
		urlSpec='%{#goToPageURL}' cssClass='pageresults' startTabIndex='51' />
	</p>
	</div>
	</div>
	<!-- old narrow by include -->
	</div>
	<div class="clear"></div>
	</div>
	<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
	</div>
	<s:url id='sortFieldsURL' action='sortResultBy' namespace='/catalog'/>
	<s:url id='pageSizeURL' action='selectPageSize' namespace='/catalog'/>	
</swc:breadcrumbScope>
<!-- // container end -->
<!-- begin swc:dialogPanel -->
<div class="x-hidden dialog-body " id="modalDialogPanel1Content">
<div class="dialog-body" id="ajax-body-1"></div>
</div>
<!-- end swc:dialogPanel -->
<!-- begin swc:dialogPanel -->
<div class="x-hidden dialog-body " id="modalDialogPanel2Content">
<div class="supersedeBox" id="ajax-body-2"></div>
</div>
<!-- end swc:dialogPanel -->
<!-- begin swc:dialogPanel -->
<div class="x-hidden dialog-body " id="product_availabilityContent">
<div id="ajax-prodAvailability">test</div>
</div>
<!-- end swc:dialogPanel -->
<div class="hidden bubble" id="extDescBubble"></div>
<!-- // main end -->
<div style="position: absolute; z-index: 15000; visibility: hidden; left: -10000px; top: -10000px;" id="ext-gen3" class="x-dd-drag-proxy 
x-dd-drop-nodrop">
<div class="x-dd-drop-icon"></div>
<div id="ext-gen4" class="x-dd-drag-ghost"></div>
</div>
</div>
<s:url id="ajaxAvailabilityJsonURL" action="ajaxAvailabilityJson" namespace="/catalog"/>
<s:url id="ajaxAvailabilityJson_PriceURL" action="ajaxAvailabilityJson_Price" namespace="/catalog"/>
<swc:dialogPanel title="Check Item Availability or Add to Cart"
	isModal="true" id="addToCart">
	<s:form action="xxx" name="addToCart" id="addToCartForm"
		namespace="/xxx" method="POST">
		<table>
			<tr>
				<td>
				<div id="add-to-cart-grid"></div>
				</td>
			</tr>
			<tr>
				<td class="submitBtnBg2"><s:text name="Availability"></s:text>
				</td>
			</tr>
		</table>
		<div id="availibility-grid"></div>
		<table>
			<tr>
				<td><input type="button" id="UpdateAvailability" class="underlink"
					value="Update Availability" class="submitBtnBg1"
					onClick="javascript:checkUpdateAvailability();" /></td>
			</tr>
		</table>
		<s:hidden name='#action.name' id='xx' value='xx' />
		<s:hidden name='#action.namespace' value='/xx' />
	</s:form>
</swc:dialogPanel>
<swc:dialogPanel title="Check Item Availability or Add to Cart"
	isModal="true" id="addToCart1">
	<div id="availibility-grid_error"></div>
</swc:dialogPanel>

<swc:dialogPanel title="Bracket Pricing" isModal="true"
	id="bracketPricingDialog" cssClass="my-class">

	<div id="Price_Grid"></div>

</swc:dialogPanel>
<!-- for jira 2422 killing the session --> 
<s:set name='lastPageUrl' value='<s:property value=null />' scope='session'/>
<s:set name='ItemDetailLastPageUrl' value='<s:property value=null />' scope='session'/>
<!-- end of jira 2422 -->

<script type="text/javascript">

function LoadPage()
{
	var pageSize='<s:property value="#session.selectedPageSize" escape='false'/>';	
	var sortField='<%=request.getParameter("sortField")%>';	
	if(sortField!="null")
	{
		document.getElementById('sortFieldUpper').value=sortField;
		document.getElementById('sortFieldLower').value=sortField;
		document.getElementById('selectedView').value = '<s:property value="#catalogView"/>';// added for fix     XNGTP-216 used to get selected view 
			
	}else{
		/*document.getElementById('selectedView').value= '<s:property value="#b2bViewFromDB" escape='false'/>';*/
		/*if(document.getElementById('selectedView').value !=null){
			}
		modified for 2421 */
		 if('<s:property value="#catalogView"/>'!="")
		{
			document.getElementById('selectedView').value = '<s:property value="#catalogView"/>';
		}
		else{
			document.getElementById('selectedView').value= '<s:property value="#b2bViewFromDB" escape='false'/>';
		}
	}
	if(pageSize!="null" && pageSize!= "")
	{
		document.getElementById('pageSizeUpper').value=pageSize;
		document.getElementById('pageSizeLower').value=pageSize;
	}
	else
		{
		 document.getElementById('pageSizeLower').value=20;
		 document.getElementById('pageSizeUpper').value=20;
		
		}
	if(document.getElementById('selectedView').value==""){	
		document.getElementById('selectedView').value='normal-view';
	}	
}
String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
}
function isValidQuantity(component){
    
	//Fix for Qty not being decimal
	var quantity = component.value.trim();
    var qtyLen = quantity.length;
    var validVals = "0123456789";
    //var isValid=true;
    var char;
    for (i = 0; i < qtyLen ; i++) {
       char = quantity.charAt(i); 
       if (validVals.indexOf(char) == -1) 
       {
    		quantity = quantity.substr(0,i) ;
    	   //alert (" quantity After" + quantity)
         // isValid = false;
       }
       
 	}
    component.value = quantity;
/*  	if (!isValid){
        component.value = quantity;
        return false;
 	} */

    if (quantity.length > 7){
        var val = quantity.substr(0,7);
		quantity = val;
        component.value = quantity;
    }
    return true;
}
$(document).ready(function() {
	LoadPage();
	loadView();
	//isClassBasedSort="N";
	//compileOnLoad();
});
function loadAfterCompilation() {
	var arrowFieldID='<%=request.getParameter("theSpanNameValue")%>';
	var arrowdirection='<%=request.getParameter("sortDirection")%>';
	var theImageSpan = eval("document.getElementById('" + arrowFieldID + "')" );	

	if(theImageSpan!=null && theImageSpan!="")
		{
		if(arrowdirection=="sortUp")
			{	
			theImageSpan.innerHTML='&nbsp;<img alt="" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_white_down.png" class="sort-order sort-desc">';
			}
		  else
			{
			  theImageSpan.innerHTML='&nbsp;<img alt="" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_white_up.png" class="sort-order sort-desc">'
			}
		}

}	
function openData(data)
{	
	openDataJS(data, '<s:property value="#addToCartURL" escape='false'/>');
}

function openAddToCart1(val){
	Ext.onReady(function(){
		var url = "<s:property value='#ajaxAvailabilityJson_PriceURL'/>";
		url = ReplaceAll(url,"&amp;",'&');
		Ext.Ajax.request({
		   url:url,// '/swc/catalog/ajaxAvailabilityJson_Price.action?sfId='+storeFrontId+'&pnaItemId='+val,
		   success: handleAjaxAvailabilitySuccess1,
		   failure: handleAjaxAvailabilityFailure1
		});
					
	});
}
var myMask;
function openAddToCart(val){
	myMask = new Ext.LoadMask(Ext.getBody(), {msg:"Loading Data..."});
	myMask.show();
	Ext.onReady(function(){
		var storeFrontId = "<s:property value='wCContext.storefrontId' />";
		var url = "<s:property value='#ajaxAvailabilityJsonURL'/>";
	     url = ReplaceAll(url,"&amp;",'&');
	     url=url+'&pnaItemId='+val;
		Ext.Ajax.request({	
				   url: url,
				   success: handleAjaxAvailabilitySuccess,
				   failure: handleAjaxAvailabilityFailure			
		});
					
	});
}
function populateDivGrid1(myData)
{	
	populateDivGridJS(myData, "<s:property value='wCContext.storefrontId' />", "<s:property value='#ajaxAvailabilityJsonURL'/>");	   		
}               
// IW Update the extJS view
// NOTE: This process slows down after switching between views several times!
function initializeView()
{
	initializeViewJS("<s:property value='#selectedViewURL'/>");						
						
}
function loadView()
{
	loadViewJS("<s:property value='#selectedViewURL'/>");
}
function processDetail(itemid, uom) {
	var selView = document.getElementById("selectedView").value; 
	var storeFrontId = "<s:property value='wCContext.storefrontId' />";
	// Begin - Changes made by Mitesh Parikh for 2422 JIRA
	<s:set name="itemDtlBackPageURL" value="%{itemDtlBackPageURL}" scope="session"/>
	<s:set name="productCompareBackPageURL" value="%{productCompareBackPageURL}" scope="session"/>
	<s:set name='xpedxSwcContext' value="%{'/swc'}"/>
	<s:if test='%{#itemDtlBackPageURL == null || #itemDtlBackPageURL.trim().length() <= 0}'>
		window.location.href = "/swc/catalog/itemDetails.action?sfId=" + storeFrontId + "&_bcs_=%11true%12%12%12%2Fswc%2Fcatalog%2Fnavigate.action%3FsfId%3Dxpedx%26scFlag%3DY%26%12%12catalog%12search%12%11&scFlag=Y" + "&itemID=" + itemid + "&unitOfMeasure=" + uom+"&selectedView="+selView;
	</s:if>
	<s:else>
		window.location.href = "<s:property value='%{itemDtlBackPageURL.substring(itemDtlBackPageURL.indexOf(xpedxSwcContext))}' escape='false'/>";
	</s:else>
	// End - Changes made by Mitesh Parikh for 2422 JIRA
}
<s:set name='wccontext' value="#_action.getWCContext()"/>
var compCount = <s:property value='#catUtil.getComparisonSetSize(#wccontext)'/>;
                    
function addCompare(itemKey) {
	if (compCount == 4)
	{
		alert ( "<s:text name='MSG.SWC.COMP.DRAGTOCOMPARE.ERROR.MORETHAN4ITEMS' /> " );
		return false;
	}
	Ext.Ajax.request({
	   // for testing only
	   <s:url id="addCompareURL" action="addCompare"/>
	   url: "<s:property escape='false' value='%{addCompareURL}'/>",
	 //   url: "/swc/catalog/addCompare.action?sfId=xpedx1&_bcs_=%11true%12%12%12%2Fswc%2Fcatalog%2Fnavigate.action%3FsfId%3Dxpedx1%26scFlag%3DY%26scGuestUser%3DY%12%12catalog%12search%12%11&scFlag=Y",
		params: {
			itemKeys: itemKey
		},
		// end testing
		method: 'GET',
		success: function (response, request) {
			updateCompareBucket(1, true);
		},
		failure: function (response, request) {
			// TODO: CHANGE THIS
			updateCompareBucket(1, true);
		}
	});
}   
function processSortByUpperTroy(theValue,directionValue,theSpanNameValue)
{	
	theUrl = "<s:property value='%{sortFieldsURL}' escape='false'/>";
	processSortByUpperTroyJS(theValue,directionValue,theSpanNameValue, theUrl);
}               
function processSortByUpper(){
	var sortFieldValue = Ext.fly('sortFieldUpper').dom.value;
	window.location.href="<s:property value='%{sortFieldsURL}' escape='false'/>" + "&sortField=" + sortFieldValue;
}

function processSortByLower(){
	var sortFieldValue = Ext.fly('sortFieldLower').dom.value;
	window.location.href="<s:property value='%{sortFieldsURL}' escape='false'/>" + "&sortField=" + sortFieldValue;
}

function processPageSizeUpper(){
	var pageSizeValue = Ext.fly('pageSizeUpper').dom.value;
	window.location.href="<s:property value='%{pageSizeURL}' escape='false'/>" + "&pageSize=" + pageSizeValue;
}

function processPageSizeLower(){
	var pageSizeValue = Ext.fly('pageSizeLower').dom.value;
	window.location.href="<s:property value='%{pageSizeURL}' escape='false'/>" + "&pageSize=" + pageSizeValue;
}
function validationforDragToCompare()
{
	var c = Ext.get('comnum'); 
    if(c.dom.innerHTML=="0 Items" || c.dom.innerHTML=="1 Item")
    {
    	//alert("Atleast two items required for product comparison");
    	alert("<s:text name='MSG.SWC.COMP.DRAGTOCOMPARE.ERROR.ATLEAST2ITEMS' />");
    }
    else
    {    	
    	<s:set name="itemDtlBackPageURL" value="%{itemDtlBackPageURL}" scope="session"/>
        window.location.href="<s:property value='%{compareURL}' escape='false'/>";
    }
    
    
}
</script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-header.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/XPEDXCatalogExt.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jqdialog/jqdialog.js"></script>

<!--<script type="text/javascript" src="../xpedx/js/global/validation.js"></script>
<script type="text/javascript" src="../xpedx/js/global/dojo.js"></script>
<script type="text/javascript" src="../xpedx/js/global/dojoRequire.js"></script>
<script type="text/javascript" src="../xpedx/js/theme/theme-1/theme.js"></script>
-->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/catalog/XPEDXCatalogAddToCart.js"></script>

<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/swc.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.tabs.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add.js"></script>
<script type="text/javascript" src="../xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="../xpedx/js/modals/checkboxtree/jquery.checkboxtree.js"></script>
-->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/common/XPEDXUtils.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/DD_roundies_0.0.2a-min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pseudofocus.js"></script>
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global-xpedx-functions.js"></script>
<script type="text/javascript" src="../xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
<script type="text/javascript" src="../xpedx/js/fancybox/jquery.fancybox-1.3.4.js"></script>
--><script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.cycle.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.shorten.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/sorttable.js"></script>


</body>
</html>
