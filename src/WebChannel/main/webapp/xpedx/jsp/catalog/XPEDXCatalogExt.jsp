<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ taglib prefix="s" 		uri="/struts-tags"%>
<%@ taglib prefix="swc" 	uri="/WEB-INF/swc.tld"%>
<%@ taglib prefix="c" 		uri="/WEB-INF/c.tld"%>
<%@ taglib prefix="xpedx" 	uri="/WEB-INF/xpedx.tld"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--  my-price-and-availability is changed -->
<!--  catalog-views.css and theme-xpedx-v1.2 is replaced -->
<!--  dot-gray is added to images  -->
<!--  narrow by jsp is changed  -->
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta content='IE=8' http-equiv='X-UA-Compatible' />
	
	<s:set name="isUserAdmin" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@isCurrentUserAdmin(wCContext)" />
	<s:set name="CurrentCustomerId" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@getCurrentCustomerId(wCContext)" />
	<s:set name="storefrontId" value="wCContext.storefrontId" />
	
	<%-- <s:set name='errorQtyGreaterThanZero' value='<s:text name="MSG.SWC.CART.ADDTOCART.ERROR.QTYGTZERO" />' scope='session'/> --%>
	<s:bean name='com.sterlingcommerce.xpedx.webchannel.common.XPEDXSCXmlUtils' id='xpedxSCXmlUtil' />
	<s:set name="isEditOrderHeaderKey" value ="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute(@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@EDITED_ORDER_HEADER_KEY)}"/>

	<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/global/styles.css" />
	<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/global/ext-all.css" />
	<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/global/swc.css" />
	<!--  removed static include -->

	
	<style type="text/css">
		.table-header-bar A:link {text-decoration: none; color: white;}
		.table-header-bar A:visited {text-decoration: underline; color: white;}
		.table-header-bar A:active {text-decoration: underline; color: white;}
		.table-header-bar A:hover {text-decoration: underline; color: white;}
		div#fancybox-content { padding-left:12px !important; padding-top:12px !important; }
    </style>
	
<!-- arun included for new verion -->	
<link type="text/css" href="../xpedx/js/jquery-ui-1/development-bundle/themes/base/jquery.ui.all.css" rel="stylesheet" />
<!-- end of addition  -->
<s:include value="../common/XPEDXStaticInclude.jsp"/>

<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/theme/xpedx-forms.css"/>
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/theme/xpedx-quick-add.css"/>
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/theme/banner.css"/>
<!--  end of include  -->
	
	<link media="all" type="text/css" rel="stylesheet" href="../xpedx/js/jcarousel/skins/xpedx/theme.css" />
	<link media="all" type="text/css" rel="stylesheet" href="../xpedx/js/jcarousel/skins/xpedx/skin.css" /><!--  new include -->

	<!--  <link media="all" type="text/css" rel="stylesheet" href="../xpedx/js/jcarousel/skins/xpedx/catalog-carousel.css" />-->
	<link media="all"    type="text/css" rel="stylesheet" href="../xpedx/js/modals/checkboxtree/demo.css" />


	<link media="all"    type="text/css" rel="stylesheet" href="../xpedx/js/modals/checkboxtree/jquery.checkboxtree.css" />	
	<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/theme-xpedx_v1.2.css"/>
	<link media="all" type="text/css" rel="stylesheet" href="/swc/<s:property value="#storefrontId" />/css/sfskin-<s:property value="#storefrontId" />.css" />

	
	<script type="text/javascript" src="../xpedx/js/sorttable.js"></script>
<!-- END - Things needed for the new list popup -PN -->

<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/theme-xpedx_v1.2.css"/>
<link media="all" type="text/css" rel="stylesheet" href="/swc/<s:property value="#storefrontId" />/css/sfskin-<s:property value="#storefrontId" />.css" />
<!--script type="text/javascript" src="/swc/xpedx/js/jquery.blockUI.js"></script-->

<!-- arun included for new verion -->	
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/prod-details.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/my-items.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/catalog/catalog-views.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/catalog/my-price-and-availability.css" />

	<script type="text/javascript" src="../xpedx/js/global/ext-base.js"></script>
	<script type="text/javascript" src="../xpedx/js/global/ext-all.js"></script>
	<script type="text/javascript" src="../xpedx/js/catalog/catalogExt.js"></script>
	<script type="text/javascript" src="../xpedx/js/jquery-1.4.2.min.js"></script>

<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/js/jqdialog/jqdialog.css" />

	<script type="text/javascript" src="../xpedx/js/global/validation.js"></script>
	<script type="text/javascript" src="../xpedx/js/global/dojo.js"></script>
	<script type="text/javascript" src="../xpedx/js/global/dojoRequire.js"></script>
	<script type="text/javascript" src="../xpedx/js/theme/theme-1/theme.js"></script>
	<script type="text/javascript" src="<s:url value='/swc/js/catalog/XPEDXCatalogAddToCart.js'/>"></script>
	<script type="text/javascript" src="/swc/xpedx/js/swc.js"></script>
	
		<!-- START - Things needed for the new list popup -PN -->
	<script type="text/javascript" src="../xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
	<script type="text/javascript" src="../xpedx/js/fancybox/jquery.fancybox-1.3.4.js"></script>
	<link media="screen" type="text/css" rel="stylesheet" href="../xpedx/js/fancybox/jquery.fancybox-1.3.1.css" /><!-- link changed -->
	<script type="text/javascript" src="../xpedx/js/sorttable.js"></script>
	<!-- END - Things needed for the new list popup -PN -->
	
	<!-- javascript -->

	<script type="text/javascript" src="/swc/xpedx/js/jqdialog/jqdialog.js"></script>
	<script type="text/javascript" src="../xpedx/js/sorttable.js"></script>
	<script type="text/javascript" src="/swc/xpedx/js/jqdialog/jqdialog.js"></script>



	<!-- carousel scripts css  -->
	<!-- carousel scripts js   -->

	<!--  new include -->
	<script type="text/javascript" src="/swc/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.core.js"></script>
	<script type="text/javascript" src="/swc/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget.js"></script>
	<script type="text/javascript" src="/swc/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.tabs.js"></script>
	<script type="text/javascript" src="/swc/xpedx/js/jquery.shorten.js"></script>
	<!--  end of new include -->
	<script type="text/javascript" src="../xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
	<script type="text/javascript" src="../xpedx/js/jcarousel/lib/jquery.jcarousel.min.js"></script>


	<script type="text/javascript" src="../xpedx/js/jquery.dropdownPlain.js"></script>

	<script type="text/javascript" src="../xpedx/js/jcarousel/xpedx-custom-carousel.js"></script>
	<!-- arun included for new verion -->	
	<script type="text/javascript" src="/swc/xpedx/js/quick-add/jquery.form.js"></script>
	<script type="text/javascript" src="/swc/xpedx/js/quick-add/quick-add.js"></script>
	<!-- script type="text/javascript" src="/swc/xpedx/js/quick-add/jquery-ui.min.js"></script-->
	<script type="text/javascript" src="../xpedx/js/jquery.dropdownPlain.js"></script>
	<script type="text/javascript" src="../xpedx/js/modals/checkboxtree/jquery.checkboxtree.js"></script>

	<script type="text/javascript" src="/swc/xpedx/js/DD_roundies_0.0.2a-min.js"></script>
	<script type="text/javascript" src="/swc/xpedx/js/pseudofocus.js"></script>
	<script type="text/javascript" src="/swc/xpedx/js/global-xpedx-functions.js"></script>
	<script type="text/javascript" src="/swc/xpedx/js/jquery.cycle.min.js"></script>
	<!-- end of addition  -->



	<!-- script type="text/javascript" src="/swc/xpedx/js/jquery.blockUI.js"></script-->
	

<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="../xpedx/css/theme/ie.css"/>
<![endif]-->

<!-- end of addition  -->

	<script type="text/javascript">
	function loadAfterCompilation()
	{
	var arrowFieldID='<%=request.getParameter("theSpanNameValue")%>';
	var arrowdirection='<%=request.getParameter("sortDirection")%>';
	var theImageSpan = eval("document.getElementById('" + arrowFieldID + "')" );	

	if(theImageSpan!=null && theImageSpan!="")
		{
		if(arrowdirection=="sortUp")
			{	
			theImageSpan.innerHTML='&nbsp;<img alt="" src="/swc/xpedx/images/icons/12x12_white_down.png" class="sort-order sort-desc">';
			}
		  else
			{
			  theImageSpan.innerHTML='&nbsp;<img alt="" src="/swc/xpedx/images/icons/12x12_white_up.png" class="sort-order sort-desc">'
			}
		}

	}
		$(document).ready(function() {
			$(document).pngFix();
		});
		function highlightRows()
		{
				//$("tr:odd").css("background-color", "#fafafa");
				$("#x-tbl-cmmn tr:odd").css("background-color", "#fafafa");
				var table=document.getElementById("x-tbl-cmmn");
				if(table !=null && table != undefined)
				{
					var rowCount = table.rows.length;	
					if(rowCount >0)
					{
						var row = table.rows[0];
						if (row != undefined)
						{
							row.setAttribute('style', 'background-color:none');
						}					
					
						for(var i=(rowCount-1); i>=1; i--) 
						{
							var row = table.rows[i];
							if (row != undefined)
							 {
									if (i%2 != 0)
									{
										row.setAttribute('style', 'background-color:#fafafa');
									}
									else
									{
										row.setAttribute('style', 'background-color:none');
									}		
								
							}         			
						}
					}
				}	
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
      	function setFocus(component){
      		component.focus();
      	}
      	
      	//taken from global-xpedx-functions.js function not in the included js 
      	function expandPriceAndAvailability (el)
      	{
      		jQuery(el).parents("dd").find('.price-and-availability').toggle();
      		return false;
      	}
      	
      	function showCost (el)
      	{
      		if(el.innerHTML == '[Show]')
      		  el.innerHTML = '[Hide]';
      		else
      		  el.innerHTML = '[Show]';
      		  
      		jQuery('#cost').toggle();
      		
      		return false;
      	}

      	function toggleCost(id)
      	{
      	   var e = document.getElementById(id);
      	   if(e.innerHTML = 'N/A')
      		  e.style.display = 'none';
      	   else
      		  e.style.display = 'block';

      	}
      	//end of global-xped-functions.js 
      	
      	//Added for removing double quote from the search srting. Jira # 2415
      	function validateQuote(e){
    	  if (e.keyCode == 13) {  
    	 	var searchVal = document.getElementById("search_searchTerm").value;
    	 	while(searchVal.indexOf("\"")!= -1){
        	  	searchVal = searchVal.replace("\"", "");    	  
    	 	}
    	 	Ext.fly('search_searchTerm').dom.value=searchVal;
    	}
    }
        
	</script>

	
	<!-- end carousel scripts js   -->
	<title><s:property value="wCContext.storefrontId" /> - <s:text name='Catalog_Page_Title' /></title>

	
</head>
<body class="ext-gecko ext-gecko3"  onload="highlightRows()">



<s:set name='_action' value='[0]'/>

<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean'
	id='util' />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean' id='xpedxutil' />	
<s:bean
	name='com.sterlingcommerce.webchannel.catalog.utils.CatalogUtilBean'
	id='catUtil' />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils' id='utilMIL' />

<s:url action='navigate.action' namespace='/catalog' id='myUrl' />
<s:url action='setSelectedView' namespace='/catalog' id='selectedViewURL' />
<s:set name='myParam' value='{"searchTerm", "", "cname"}' />
<s:url id='addToCartURL' namespace='/order' action='addToCart' />
<s:url id='punchOutURL' namespace='/order' action='configPunchOut' />
<s:url id='selectedViewURL' action='setSelectedView' namespace='/catalog' />
<s:set name='appFlowContext' value='#session.FlowContext' />
<s:set name='isFlowInContext' value='#util.isFlowInContext(#appFlowContext)' />
<swc:breadcrumb id="searchBreadcrumb" rootURL='#myUrl' group='catalog' displayGroup='search' displayParam='#myParam' />
<s:url id='punchOutURLOrderChange' namespace='/order'
	action='configPunchOut'>
	<s:param name='orderHeaderKey' value='%{#appFlowContext.key}' />
	<s:param name='currency' value='%{#appFlowContext.currency}' />
	<s:param name='flowID' value='%{#appFlowContext.type}' />
</s:url>
<s:url id='addToMyItemListURLid' namespace='/xpedx/myItems' action='XPEDXMyItemsDetailsAddFromCatalog'/>
<!--  <s:a id='addToMyItemListURL' href='%{#addToMyItemListURLid}'/> -->
<s:set name='isProcurementInspectMode'
	value='#util.isProcurementInspectMode(wCContext)' />
<s:set name='isReadOnly' value='#isProcurementInspectMode' />
<s:set name='catDoc' value='%{outDoc.documentElement}' />
<s:set name='numResult' value='#catDoc.getAttribute("TotalHits")' />
<s:set name='pageNumber' value='#catDoc.getAttribute("PageNumber")' />
<s:set name='totalNumberOfPages'
	value='#catDoc.getAttribute("TotalPages")' />
<s:set name='catDoc' value='%{outDoc.documentElement}' />
<s:set name='numResult' value='#catDoc.getAttribute("TotalHits")' />
<s:set name='pageNumber' value='#catDoc.getAttribute("PageNumber")' />
<s:set name='totalNumberOfPages'
	value='#catDoc.getAttribute("TotalPages")' />
<s:url id='compareURL' action='prodCompare' namespace='/catalog' />
<s:set name='guestUser' value="#_action.getWCContext().isGuestUser()" />
<s:set name="xpedxCustomerContactInfoBean" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("XPEDX_Customer_Contact_Info_Bean")' />
<s:set name='b2bViewFromDB' value="#xpedxCustomerContactInfoBean.getExtnB2BCatalogView()" />
<s:set name="customerUseSKU" value ='%{#_action.getWCContext().getWCAttribute("customerUseSKU")}'/>
<s:set name="customerItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CUSTOMER_ITEM_LABEL"/>
<s:set name="manufacturerItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MANUFACTURER_ITEM_LABEL"/>
<s:set name="mpcItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MPC_ITEM_LABEL"/>

<s:set name="itemUomHashMap" value ='%{#_action.getItemUomHashMap()}'/>
<s:set name="defaultShowUOMMap" value ='%{#_action.getDefaultShowUOMMap()}'/> <!-- Code added to fix XNGTP 2964 -->
<s:set name="orderMultipleMap" value ='%{#_action.getOrderMultipleMap()}'/> <!-- Code added to fix XNGTP 2964 -->

<s:set name="plLineMap" value ='%{#_action.getPLLineMap()}'/>
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

<swc:breadcrumbScope>
	<div id="main-container">
	
	
	
	<s:if test='!#guestUser'>  
		<div id="main">
	</s:if>
	<s:else>
		<div id="main" class="anon-pages">
	</s:else>
	
	
	<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
	
	<s:if test='!#guestUser'>  
	<s:action name="xpedxShiptoHeaderCat3" executeResult="true"
		namespace="/common" /> 
	</s:if>
	<!-- <div class="container catalog" style="overflow: auto;" > -->

<%-- 	<s:if test='!#guestUser'>  
		<div id="main">
	</s:if>
	<s:else>
		<div id="main" class="anon-pages">
	</s:else>
	<s:action name="xpedxHeader" executeResult="true" namespace="/common" >
		<s:param name='shipToBanner' value="%{'true'}" />
	</s:action> --%>
	
	
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
			

			<script type="text/javascript">
			$(document).ready(function() {
				$('.slideshow').cycle({
						fx: 'fade', // choose your transition type, ex: fade, scrollUp, shuffle, etc...
						pager: '#catalog-image-rotation-nav-inner',
						timeout: 5000,
						prev:   '#catalog-image-rotation-nav .img-navi-left', 
						next:   '#catalog-image-rotation-nav .img-navi-right'
				});
			});
			</script>

			<!--Webtrands Start -->
			<meta name="WT.ti" content="<s:property value='%{#parameters.cname}' />"/>
			<!--Webtrands End -->
		 	
			<!-- Header promotion Start-->
			<div id="catalog-image-rotation"> 
					<%-- 			
					<div class="slideshow"> 
							<s:action name="xpedxDynamicPromotions" executeResult="true" namespace="/common" >
								<s:param name="callerPage">catalog</s:param>
								<s:param name="category" value='#parameters.cname'/>
							</s:action>
					</div>  
					--%>
					
					<div class="slideshow"> 
							<s:action name="xpedxDynamicPromotionsAction" executeResult="true" namespace="/common" >
								<s:param name="callerPage">CatalogPage</s:param>
								<s:param name="category" value='#parameters.cname'/>
								<s:param name="categoryPath" value='#parameters.path'/>
							</s:action>
					</div>
					
					<div id="catalog-image-rotation-nav"> 
							<div class="img-navi-left"></div> 
							<div id="catalog-image-rotation-nav-inner"></div> 
							<div class="img-navi-right"></div> 
					</div> 
			</div> 		
			<!-- Header promotion End-->


			<div class="catalog-ad"><div class="ad-float smallBody" style="float: none;">
			<img class="float-left" height="4" width="7" alt="" src="/swc/xpedx/images/mil/ad-arrow.gif" style="margin-top: 5px; padding-right: 5px;">advertisement</div>
				<!-- Ad Juggler Tag Starts -->
				<s:set name='ad_keyword' value='' />
							<s:iterator id='item' value='XMLUtils.getElements(#catDoc, "//ItemList/Item")' status='prodStatus'>
								<s:set name='info' value='XMLUtils.getChildElement(#item, "PrimaryInformation")'/>
								<s:if test='%{#prodStatus.index==0}'>
									<s:set name='itemID' value='#item.getAttribute("ItemID")'/>
									<s:if test="#itemID != null" >
										<s:set name="cat2Val" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getCatTwoDescFromItemId(#itemID,#storefrontId)" />
										<s:if test="#cat2Val != null" >
											<s:set name='ad_keyword' value='#cat2Val' />
										</s:if>
									</s:if>
								</s:if>
							</s:iterator>
				<s:set name='storefrontId' value="wCContext.storefrontId" />
				
		<!-- aj_server: https://rotator.hadj7.adjuggler.net:443/servlet/ajrotator/ -->
		
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
		<!-- END catalog promo ad horiz -->
	<!--  <div class="catalog-ad">-->
	<!--  <div id="catalog-promo-ad-horizontal">-->
 		<!-- script type="text/javascript" language="JavaScript">
				  aj_server = 'https://xpedx.rotator.hadj7.adjuggler.net:443/servlet/ajrotator/'; aj_tagver = '1.0';
				  aj_zone = 'xpedx'; aj_adspot = '52891'; aj_page = '0'; aj_dim ='52581'; aj_ch = ''; aj_ct = ''; aj_kw = '';
				  aj_pv = true; aj_click = '';
				</script><script type="text/javascript" language="JavaScript" src="https://img.hadj7.adjuggler.net/banners/ajtg.js"></script -->
	<!-- </div>-->
	<!--  new content -->
	<!-- BEGIN breadcrumb -->
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
					<xpedx:breadcrumbDisplay displayRootName='Catalog' breadcrumbSeparator=' / ' 
					removable='true' removeIcon='#rbtn' startTabIndex='2' />
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
               
                 <p class="pageresults"><s:property value='#numResult' /> Results<span>&nbsp;|&nbsp;Page&nbsp</span>
                 <!-- Webtrend Meta Tag start -->
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
		function clearCreateNewList()
		{			
			document.getElementById("listName").value="";
			document.getElementById("listDesc").value="";
			document.getElementById("shareAdminOnly").checked=false;
			$.fancybox.close();
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
			<li><a class="grey-ui-btn" href="javascript:clearCreateNewList();"><span>Cancel</span></a></li>
			<li style="float: right;"><a href="javascript:submitSL();"> <img
				src="../xpedx/images/theme/theme-1/ui-buttons/ui-btn-save.gif" width="49" height="23" alt="Save" title="Save" /> </a></li>
		</ul>
	
	</s:form> 
	<!-- CODE_END MIL - PN -->
	</div>
</div>
<!-- END of new MIL - PN -->

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
				<%--
				<div style="display: none;">
				<s:action name="XPEDXMyItemsList" executeResult="true" namespace="/xpedx/myItems">
					<s:param name="filterByAccChk" value="%{true}" />
					<s:param name="filterByShipToChk" value="%{true}" />
					<s:param name="filterByMyListChk" value="%{true}" />
					<s:param name="displayAsDropdownList" value="%{true}" />
				</s:action>
				</div> --%>
				
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
					
					function prepareDiv(data, itemId, name, desc, qty, uom){
						
						try{
							data = data.replace("[itemId]", itemId);
							data = data.replace("[name]", 	name); 
							data = data.replace("[desc]", 	desc);
							data = data.replace("[qty]",	qty);
							data = data.replace("[uom]", 	uom);
							try{ console.debug("data: " + data + ", itemId: " + itemId + ", name: " + name + ", desc: " + desc + ", qty: " + qty + ", UOM: " + uom) }catch(e){}
						}catch(e){
							data = "";
						}
						return data;
					}
					
					var currentAadd2ItemList = new Object();
					
				 </script>
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
	
function checkUpdateAvailability()
{
	var ItemId=document.getElementById("ITemId").value;
	var UOM=document.getElementById("UOM").value;	
	if(document.getElementById("Qty").value!=null)
	{
		var qtyEle = document.getElementById("Qty");
		ItemId=ItemId+'&Qty='+document.getElementById("Qty").value+'&UOM='+UOM;
	}
	openAddToCart(ItemId);
	
}
function openData(data)
{	var ITemId=document.getElementById("ITemId").value;
	var Qty=document.getElementById("Qty").value;
	var UOM=document.getElementById("UOM").value;
	var Job=document.getElementById("Job").value;
	var customer=document.getElementById("customer").value;
	
	listAddToCartItem('<s:property value="#addToCartURL" escape='false'/>',ITemId,UOM,Qty,Job,customer,'');
}
function openDialog()
{
 	openAddToCart1(document.getElementById("ITemId").value)
	javascript:DialogPanel.show('bracketPricingDialog');
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
function handleAjaxAvailabilitySuccess1(responseObject){
	myMask.hide();	
	var responseText = responseObject.responseText;
	Ext.fly('Price_Grid').dom.innerHTML='';
	Ext.fly('Price_Grid').dom.innerHTML=responseText;		
	svg_classhandlers_decoratePage();
}
function handleAjaxAvailabilityFailure1(){	
	myMask.hide();
	alert('fail');
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
	var obj = eval("(" + myData + ")");
	var storeFrontId = "<s:property value='wCContext.storefrontId' />";
	var url = "<s:property value='#ajaxAvailabilityJsonURL'/>";
	url = ReplaceAll(url,"&amp;",'&');
	var availStore = new Ext.data.JsonStore({	
		proxy: new Ext.data.HttpProxy({  
		    method:'POST',  
		    url: url,
		    headers: {'Content-type': 'application/json'}
		}),		
		data   : obj,					
		root: 'availList',
		fields:['availability','availValue']		
    });
	
	
	var grid = new Ext.grid.GridPanel({
        width:550,
        height:200,
        layout:'fit',
        title:'',
        store: availStore,
        trackMouseOver:false,
        disableSelection:true,
        loadMask: true,

        // grid columns
        columns:[{
            id: 'topic', 
            header: '',
            dataIndex: 'availability',
            width: 130,
            height:80,
            sortable: false
        },{
            header: '',
            dataIndex: 'availValue',
            width: 130,
            height:80,
            sortable: false
        }]
    });//grid			
    Ext.fly('availibility-grid').dom.innerHTML='';
	availStore.loadData(obj);	
	grid.render('availibility-grid'); 	    		
}

function handleAjaxAvailabilitySuccess(responseObject){	
	myMask.hide();	
	var responseText = responseObject.responseText;
	if(responseText.indexOf("Unable to get Price and Availability.Please contact system admin")>-1)
	{
		Ext.fly('availibility-grid_error').dom.innerHTML='';
		Ext.fly('availibility-grid_error').dom.innerHTML=responseText;
		DialogPanel.show("addToCart1");	
		svg_classhandlers_decoratePage();
	}
	else
	{
		var variable1="{\"availList\":";
		var startIndexof=responseText.indexOf(variable1);
		if(startIndexof > 0){
			var jsonString=responseText.substring(startIndexof,responseText.length);
			variable1="}]}";
			startIndexof=jsonString.indexOf(variable1)+variable1.length;
			jsonString=jsonString.substring(0,startIndexof);
			populateDivGrid1(jsonString);
		}		
		Ext.fly('add-to-cart-grid').dom.innerHTML='';
		Ext.fly('add-to-cart-grid').dom.innerHTML=responseText;
		DialogPanel.show("addToCart");
		svg_classhandlers_decoratePage();	
	}	
}

function handleAjaxAvailabilityFailure(){
	myMask.hide();	
	alert('Failure');
}

function listAddToCartItem(url, productID, UOM, quantity,Job,customer) {
      
	
   	Ext.Ajax.request({
    	// for testing only
        url: url,
        params: {
			productID: productID,
	    	productUOM: UOM,
	    	quantity: quantity,
	    	reqJobId: Job,
	    	reqCustomer:customer
	     },
        // end testing
        method: 'GET',
        success: function (response, request){
              
            //DialogPanel.toggleDialogVisibility('addToCart');	            
            //var myDiv = document.getElementById("ajax-body-1");	            
           	//myDiv.innerHTML = 'The product has been successfully added to the cart';	            
           	//DialogPanel.show('modalDialogPanel1');	            
           	//svg_classhandlers_decoratePage();
        },
        failure: function (response, request){
            var myDiv = document.getElementById("ajax-body-1");
            myDiv.innerHTML = response.responseText;
            DialogPanel.show('modalDialogPanel1');
            svg_classhandlers_decoratePage();
             }
    });		
}	

function addItemToMyItemList(pulldown,itemID,baseUom)
{
	if(pulldown.value == 'AddToListSample'){
		return;
	}
	 var submitForm = getNewSubmitForm();
	 createNewFormElement(submitForm, "qty", "1");
	 createNewFormElement(submitForm, "uomId", baseUom);
	 createNewFormElement(submitForm, "listKey", pulldown.value);
	 createNewFormElement(submitForm, "listName", pulldown.options[pulldown.selectedIndex].text);
	 createNewFormElement(submitForm, "itemId", itemID);
	 submitForm.action= document.getElementById('addToMyItemListURL');
	 submitForm.submit();
}

//helper function to create the form
function getNewSubmitForm(){
	 var submitForm = document.createElement("FORM");
	 document.body.appendChild(submitForm);
	 submitForm.method = "POST";
	 return submitForm;
}

//helper function to add elements to the form
function createNewFormElement(inputForm, elementName, elementValue){
	 var newElement = document.createElement("<input name='"+elementName+"' type='hidden'>");
	 inputForm.appendChild(newElement);
	 newElement.value = elementValue;
	 return newElement;
}


<s:set name="allowedColumns" value="columnList" />
/*
 * Sku
 Desc
 Size
 Environment
 StockStatus
 */
                    var itemWin;

  //function compileOnLoad (){
	<s:set name='itemList' value='XMLUtils.getChildElement(#catDoc, "ItemList")'/>
	<%--<s:url id='shipImg' value='/swc/images/common/shippable.jpg'/>--%>
    <s:url id='shipImg' value='/swc/images/theme/theme-1/Shippable.png'/>
    <%--<s:url id='availImg' value='/swc/images/common/available.jpg'/>--%>
	<s:set name='scuicontext' value="#_action.getWCContext().getSCUIContext()" />
	<s:set name='wcContext' value="#_action.getWCContext()" />
	<s:set name='currency' value='#itemList.getAttribute("Currency")' />
	<s:set name='showCurrencySymbol' value='true' />
	<s:set name="isEditOrderHeaderKey" value ="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute(@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@EDITED_ORDER_HEADER_KEY)}"/>
                    // Object include the following fields
                    /*
                    {
                    itemid,
                    uom,
                    itemkey,
                    name,
                    price,
                    icon,
                    desc,
                    extdescurl,
                    buttons,
                    supersedelink,
                    availablelink
                    }
                    */
					
//					var divAdd2List = Ext.get("divAdd2List").dom.innerHTML;
//					Ext.get("divAdd2List").dom.innerHTML = "";
					
                    var catalog = [{
                        title: 'Search Results',
                        items: [

					    <s:set name='tabidx' value='101'/>
						<s:iterator id='item' value='XMLUtils.getElements(#catDoc, "//ItemList/Item")' status='prodStatus'>
							<s:set name='info' value='XMLUtils.getChildElement(#item, "PrimaryInformation")'/>
							<s:set name='itemID' value='#item.getAttribute("ItemID")'/>
							<s:set name='itemUOMList' value='%{#itemUomHashMap.get(#itemID)}'/>
							<s:set name='defaultUOM' value='%{#defaultShowUOMMap.get(#itemID)}'/> /*Code added to fix XNGTP 2964*/							
							<s:set name='shortDesc' value='#info.getAttribute("ShortDescription")'/>
							<s:set name='desc' value='#info.getAttribute("Description")'/>
							<s:set name='itemKey' value='#item.getAttribute("ItemKey")'/>
							<s:set name='kitCode' value='#info.getAttribute("KitCode")' />
							<s:set name='unitOfMeasure' value='#item.getAttribute("UnitOfMeasure")'/>
							<s:set name='price' value='XMLUtils.getChildElement(#item, "ComputedPrice")'/>
							<s:if test='%{#kitCode == "BUNDLE" }'> 
							  <s:set name='myPrice' value='#price.getAttribute("BundleTotal")'/>
							</s:if><s:else>
							  <s:set name='myPrice' value='#price.getAttribute("UnitPrice")'/>
							</s:else>
							
							<s:set name='imageMainURL'	value='%{#xpedxSCXmlUtil.getAttribute(#info,"ImageLocation")+"/"+#info.getAttribute("ImageID")}'/>							
							<s:if test='%{#imageMainURL=="/"}'>
								<s:set name='imageMainURL' value='%{"/xpedx/images/INF_150x150.jpg"}' />
							</s:if>							
							<s:url id='pImg' value='%{#imageMainURL}'/>
							
							<s:set name='skuMap' value='itemMap.get(#itemID)'/>
							
							<s:set name='itemBranchBean' value='itemToItemBranchBeanMap.get(#itemID)'/>
							/*itemBranchBean is of Type com.sterlingcommerce.xpedx.webchannel.catalog.XPEDXItemBranchInfoBean*/
							/* 2964 <s:set name='orderMultiple' value='@com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils@getOrderMultipleForItem(#itemID)'/>
							 */
							 <s:set name='orderMultiple' value='%{#orderMultipleMap.get(#itemID)}'/> /*Code added to fix XNGTP 2964*/
							  <s:set name='b2cItemExtn' value='XMLUtils.getChildElement(#item, "Extn")'/>
							<s:set name='b2cSize' value='#b2cItemExtn.getAttribute("ExtnSize")'/>
							<s:set name='b2cColor' value='#b2cItemExtn.getAttribute("ExtnColor")'/>
							<s:set name='b2cMwt' value='#b2cItemExtn.getAttribute("ExtnMwt")'/>
							<s:set name='b2cBasis' value='#b2cItemExtn.getAttribute("ExtnBasis")'/>
							<s:set name='b2cCert' value='#b2cItemExtn.getAttribute("ExtnCert")'/>
							<s:set name='b2cVendorNumber' value='#b2cItemExtn.getAttribute("ExtnVendorNo")'/>
							<s:set name='b2cLegacyId' value='#b2cItemExtn.getAttribute("ExtnLegacyId")'/>
							<s:set name='b2cMaterial' value='#b2cItemExtn.getAttribute("ExtnMaterial")'/>
							<s:set name='b2cForm' value='#b2cItemExtn.getAttribute("ExtnForm")'/>
							<s:set name='b2cCapacity' value='#b2cItemExtn.getAttribute("ExtnCapacity")'/>
							<s:set name='b2cModel' value='#b2cItemExtn.getAttribute("ExtnModel")'/>
							<s:set name='b2cGuage' value='#b2cItemExtn.getAttribute("ExtnGauge")'/>
							<s:set name='b2cPly' value='#b2cItemExtn.getAttribute("ExtnPly")'/>
							<s:set name='b2cstockStatus' value='#itemBranchBean.inventoryIndicator'/>
							<s:set name='isSuperseded' value='#item.getAttribute("IsItemSuperseded")'/>
							<s:set name='isValid' value='#info.getAttribute("IsValid")'/>
							<s:set name='isModelItem' value='#info.getAttribute("IsModelItem")'/>
							<s:set name='isConfigurable' value='#info.getAttribute("IsConfigurable")'/>
							<s:set name='isPreConfigured' value='#info.getAttribute("IsPreConfigured")'/>
							<s:set name='isPickupable' value='#info.getAttribute("IsPickupAllowed")'/>
							<s:set name='isShippable' value='#info.getAttribute("IsShippingAllowed")'/>
							<s:set name='configurationKey' value='#info.getAttribute("ConfigurationKey")'/>
							<s:set name='isConfigurableBundle' value='%{"N"}'/>
							<s:set name='deschtml' value='#utilMIL.formatEscapeCharactersHtml(#desc)'/>
							<s:if test='%{#kitCode == "BUNDLE" && #isConfigurable=="Y" }'>
								<s:set name='isConfigurableBundle' value='%{"Y"}'/>
							</s:if>
							<s:set name='formattedUnitprice' value='#xpedxutil.formatPriceWithCurrencySymbol(#wcContext,#currency,#myPrice)'/>
                            {
							<s:set name='hasTier' value='false'/>
				            itemid: "<s:property value='#itemID'/>",
				            itemkey: "<s:property value='#itemKey'/>",
				            uom: "<s:property value='#unitOfMeasure'/>",
							name: "<s:property value='#utilMIL.formatEscapeCharactersHtml(#shortDesc)' escape='false'/>",
							itemUOMList: "<s:property value='#unitOfMeasure'/>",
							listprice: ""
								<s:iterator id='tierPrices'  value='%{#plLineMap.get(#itemID)}' status='tierStatus'>
								<s:set name='tierQty' value='#tierPrices.getAttribute("FromQuantity")'/>
								<s:if test = '#tierQty == null || #tierQty == ""'>
									<s:set name='tierQty' value='1'/>
								</s:if>
								<s:set name='hasTier' value='true'/>
								<s:set name='tierPrice' value='#tierPrices.getAttribute("ListPrice")'/>
								<s:set name='extnTierPrice' value='XMLUtils.getChildElement(#tierPrices, "Extn")'/>
								<s:set name='tierPriceUOM' value='#extnTierPrice.getAttribute("ExtnTierUom")'/>
								<s:set name='PriceUOM' value='#extnTierPrice.getAttribute("ExtnPricingUom")'/>
								<s:set name='formattedTierUnitprice' value='#xpedxutil.formatPriceWithCurrencySymbol(#wcContext,#currency,#tierPrice)'/>
								<s:if test='#formattedTierUnitprice == ""|| #formattedTierUnitprice == null'>
								  + "<br/>" 
								</s:if>
								<s:else>
									 + "<s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedQty(#tierQty)'/>&nbsp;<s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedUOMCode(#tierPriceUOM)'/>-<s:property value='#formattedTierUnitprice'/>/<s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedUOMCode(#PriceUOM)'/> <br/> "
								</s:else>
								</s:iterator>
								<s:if test='#hasTier'>
									,
								</s:if>
								<s:else>
									<s:if test='#formattedUnitprice == ""|| #formattedUnitprice==null'>
									  + "",
									</s:if>
									<s:else>
									   + "<s:property value='#formattedUnitprice'/> /<s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedUOMCode(#unitOfMeasure)'/>",
									</s:else>
								</s:else>
						    icon: "<s:property value='pImg'/>",
				            desc: "<s:property value='deschtml' escape='false'/>",
				            extdescurl:<s:url id='quickViewURL' namespace='catalog' action='getQuickView'>
		                    				<s:param name='itemID'><s:property value='#itemID'/></s:param>
		                    				<s:param name='unitOfMeasure'><s:property value='#unitOfMeasure'/></s:param>
		                				</s:url>
		                				"<s:property value='%{quickViewURL}' escape='false'/>",
		                  tabidx: "<s:property value='#tabidx'/>",
				            supersedelink: "",
				            <s:if test='!#guestUser'>
                           	availablelink:
                           		"<input type='hidden' id='baseUOMs_<s:property value='#itemID'/>' name='baseUOMs_<s:property value='#itemID'/>' value='<s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#unitOfMeasure)'/>'/>"+
                               	"<div class=\"itemOption\"><a href=\"javascript:void(0);\" class=\"submitBtnBg1 underlink\" style=\"padding-left:12px; font-weight: normal; \" onclick=\"displayAvailability('<s:property value='#itemID'/>');\">My Price &amp; Availability</a></div>" + "",
                           	uomLink:
                           		<s:if test='%{#orderMultiple > "1" && #orderMultiple !=null }'>	
                               	"<div class=\"notice\" style=\"margin-right:5px; font-weight: normal;float:right; display:inline;\"><s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> <s:property value='%{#orderMultiple}'/> <s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#unitOfMeasure)'/></div>"+
                               	
                               	
                               	</s:if>
                               	"",
                           </s:if>
                           
                           action:
                                "<p class='addtocart'><a href='' onclick=\"javascript:addItemToCart('<s:property value='#itemID'/>'); return false;\">" +
                                    "Add to Cart" +
                                "</a></p>" +
                                "<div class='availablelink'>" +
                                "<a class='itemOption submitBtnBg1' href='#' >Check Availability</a></div>" +
                                "<select class='xpedx_select_sm'>" +
								 "<option>Add to List</option>" +
                                "<option>Joe&#39;s List</option>" +
                                "<option>Andy&#39;s List</option>" +
                                "<option>Sue&#39;s List</option>" +
                                "</select>" + "",
                            price: "<s:property value='#tierPrice'/>",
				                        <s:set name='tabidx' value='%{#tabidx + 1}'/>
				            buttons:       "<ul id=\"prodlist\">" +
										"<s:property value='deschtml' escape='false'/>" +
										"</ul>" + ""
				                , // This is needed in case no struts condition is satisfied
				            buttonsprice:
				            	<s:if test='!#isReadOnly && !#guestUser'>
				            "<div class=\"AddToCartAndAvailabililty\"/>"+
							"<div class=\"qtyAndUom\"><label class=\"input-label\">Qty :</label><input type=\"text\" name='Qty_<s:property value='#itemID'/>' id='Qty_<s:property value='#itemID'/>' size=\"10\" tabindex=\"100\" onclick=\"javascript:setFocus(this);\" onkeyup=\"javascript:isValidQuantity(this);javascript:qtyInputCheck(this, <s:property value='#itemID'/>);\" onmouseover=\"javascript:qtyInputCheck(this, <s:property value='#itemID'/>);\"></input><input type=\"hidden\" id='Qty_Check_Flag_<s:property value='#itemID'/>' name='Qty_Check_Flag_<s:property value='#itemID'/>' value='false'/></div>"+
							/* "<div class='error' id='errorMsgForQty_<s:property value='#itemID'/>' style='display : none'/>Qty Should be greater than 01.</div>"+ */	
							"<div class='error' id='errorMsgForQty_<s:property value='#itemID'/>' style='display : none'/>{qtyGreaterThanZeroMsg}</div>"+	
				            "<div class=\"uoms\">"+
				           	<s:if test='#itemUOMList != null'>
					           	"<select name='itemUomList' id='itemUomList_<s:property value="#itemID"/>'>"+
					           	<s:iterator value='%{#itemUOMList}' id='itemUoms'>
					           		<s:set name='key' value='key'/>
					           		<s:set name='value' value='value' />
					           		"<option value='<s:property value="#key" />'><s:property value="#value" /></option>"+
					           	</s:iterator>"</select>"+
					        </s:if>
				           	"</div>"+
				           	<s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
		            		"<div class=\"addtocart\"><a tabindex=\"102\" href=\"#\" onclick=\"javascript:addItemToCart('<s:property value='#itemID'/>'); return false;\">Add To Cart</a></div>"+
		            		</s:if>
		            		<s:else>
		            		"<div class=\"addtocart\"><a tabindex=\"102\" href=\"#\" onclick=\"javascript:addItemToCart('<s:property value='#itemID'/>'); return false;\">Add To Order</a></div>"+
		            		</s:else>
		            		"<div class=\"availablelink\"><a tabindex=\"103\" href=\"javascript:displayAvailability('<s:property value='#itemID'/>')\">Check Availability</a></div>"+
		            		"</div>"+
		            		<s:set name='tabidx' value='%{#tabidx + 1}'/>
		                </s:if>
						"", // This is needed in case no struts condition is satisfied
							legacyId:
								"<s:property value='#b2cLegacyId'/>",
							material:
								"<s:property value='#b2cMaterial'/>",
							form: 
								"<s:property value='#b2cForm'/>",
							capacity:
								"<s:property value='#b2cCapacity'/>",
							model:
								"<s:property value='#b2cModel'/>",
							guage:
								"<s:property value='#b2cGuage'/>",
							ply:
								"<s:property value='#b2cPly'/>",
							size:
								"<s:property value='#b2cSize'/>",
							color:
								"<s:property value='#b2cColor'/>",
							custOptedSKU:
							<s:if test='%{#customerUseSKU == "2"}'>
							   "<s:property value='#manufacturerItemLabel'/>:" + "<s:property value='#skuMap.get("MPN")'/>",
							</s:if>
							<s:else>
								 <s:if test='%{#customerUseSKU == "3"}'>
								 "<s:property value='#mpcItemLabel'/>:" + "<s:property value='#skuMap.get("MPC")'/>",
								</s:if>
								<s:else>
									<s:if test='%{#customerUseSKU == "1"}'>
									"<s:property value='#customerItemLabel'/>:" + "<s:property value='#skuMap.get("CPN")'/>",
									</s:if>
									<s:else>
									 "",
									</s:else>
								</s:else>
							</s:else>								
							basis:
								"<s:property value='#b2cBasis'/>",
							mwt:
								"<s:property value='#b2cMwt'/>",
							vendorNumber:
								"<s:property value='#b2cVendorNumber'/>",
							stocked:
								<s:if test='%{#b2cstockStatus == "W"}'>
									"" +
								</s:if>
								<s:else>
									"M" +
								</s:else>
								"",
							cert:
								 <s:if test='%{#b2cCert == "Y"}'>
									"<span><img width=\"20\" height=\"20\" src='<s:url value='/xpedx/images/catalog/green-e-logo_small.png'/>'</span>" +
									 </s:if>
								"",
							qtyGreaterThanZeroMsg: ""+
							"<s:text name='MSG.SWC.CART.ADDTOCART.ERROR.QTYGTZERO' />"+
							"",
							partno:
								
									<s:if test='%{#customerUseSKU == "2"}'>
										"<s:property value='#manufacturerItemLabel'/>:"+" "+"<s:property value='#skuMap.get("MPN")'/>"+
									</s:if>
									<s:elseif test='%{#customerUseSKU == "3"}'>
										 "<s:property value='#mpcItemLabel'/>:"+" "+"<s:property value='#skuMap.get("MPC")'/>"+
									</s:elseif>
									<s:elseif test='%{#customerUseSKU == "1"}'>
											"<s:property value='#customerItemLabel'/>:"+" "+"<s:property value='#skuMap.get("CPN")'/>"+
									</s:elseif>
									"",
							<!-- --JIRA-2798-- uomdisplay: "<div class=\"uom-select\"><select name='itemUomList' id='itemUomList_<s:property value='#itemID'/>'>'"+ -->
							 uomdisplay: "<div class=\'uom-select\'><select name='itemUomList' id='itemUomList_<s:property value='#itemID'/>'>"+
								<s:iterator value='%{#itemUOMList}' id='itemUoms'>
									<s:set name='key' value='key'/>
									<s:set name='value' value='value' />/*Code added to fix XNGTP 2964*/
									<s:if test ='#defaultUOM != null && #value == #defaultUOM'>					           		
					           				"<option  selected='selected' value=\'<s:property value='#key' />\'> <s:property value='#value'/></option>"+					           				
					           		</s:if>
					           		<s:else>
					           				"<option value=\'<s:property value='#key' />\'><s:property value='#value' /></option>"+
					           				</s:else> 
									 </s:iterator>
							"</select><input type='hidden' id='orderMultiple_<s:property value='#itemID'/>' value='<s:property value='#orderMultiple' />'/></div>",
							itemtypedesc:<s:if test='%{#b2cstockStatus != "W" && !#guestUser}'>
							"<div class=\'mill-mfg\'>Mill / Mfg. Item<span class=\'addl-chg\'> - Additional charges may apply</span></div>"+
							</s:if>""
							 }
				
						<s:if test='!#prodStatus.last'>
				        ,
				        </s:if>
					</s:iterator>
                            ]
                    }];


<s:url id="goToPageURL" action="goToPage">
	<s:param name="pageNumber" value="'{0}'"/>
</s:url>
                    var extTemplates = new Array();
                    extTemplates["normal-view"] = new Ext.XTemplate(
                    		 '<div id="item-ct">',
								'<tpl for=".">',
 										'<dl>',
										'<tpl for="items">',
											'<dd id="{itemkey}" class="itemdiv">',
												'<div class="imgs">',
												    '<a href="javascript:processDetail(\'{itemid}\',\'{uom}\');">',
													'<img title="{name}" alt="{name}" src="{icon}" class="prodImg" id="pimg_{#}"/></a>',
													'<div class="hidden bubble extDescDiv" id="extDescDiv_{#}"></div>',
												'</div>',
												'<div class="contents">',
													'<p class="pprice">{price}</p>',
													'<div class="descriptions">',
													 '<a id="item-detail-lnk" href="javascript:processDetail(\'{itemid}\',\'{uom}\');" tabindex="{tabidx}">',
													'<p class="ddesc">{name}</p></a>',
													'<div class="buttons"><a href="javascript:processDetail(\'{itemid}\',\'{uom}\');" >{buttons}</a></div></div>',
													'<div class="clearBoth">&nbsp;</div>',
													'</div>',
													'<table class="bottable">',
														'<tr>',
															'<td class="compare_check">',
																// Do not delete this code. This will come as a CR.
																//'<input type="checkbox" name="compare_{itemkey}" id="compare_{itemkey}" />',
																//'<label for="compare_{itemkey}">Compare</label>',
															'</td>',
															'<td class="item_number">',
																'<s:property value="wCContext.storefrontId" /> Item #: {itemid} {cert}',
															'</td>',
															'<td class="quantity_box">',
																<s:if test='!#guestUser'>
																'Qty:&nbsp;<input type="textfield" id=\'Qty_{itemid}\'  name=\'Qty_{itemid}\' value="" size="10" maxlength="10" onclick="javascript:setFocus(this);" onkeyup="javascript:isValidQuantity(this);javascript:qtyInputCheck(this, \'{itemid}\');" onmouseover="javascript:qtyInputCheck(this,  \'{itemid}\');"/>',
														'<input type="hidden" id="Qty_Check_Flag_{itemid}" name="Qty_Check_Flag_{itemid}" value="false"/>',
																'{uomdisplay}',
																</s:if>
															'</td>',
														'</tr>',
														'<tr>',
															'<td></td>',
															'<td class="item_number">{partno}</td>',
															'<td class="add_to_cart">',
																<s:if test='!#guestUser'>
																	<s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
																		'<div class="addtocart"><a class="" id=\'addtocart_{itemid}\' href="#"  onclick=\"javascript:addItemToCart(\'{itemid}\'); return false;\">Add to Cart</a></div>',
																	</s:if>
																	<s:else>
																		'<div class="addtocart"><a class="" id=\'addtocart_{itemid}\' href="#"  onclick=\"javascript:addItemToCart(\'{itemid}\'); return false;\">Add to Order</a></div>',
																	</s:else>		
																'<div class="availablelink">{availablelink}</div>',
																</s:if>
															'</td>',
														'</tr>',
														'<tr>',
														'<td colspan="3">',
														<s:if test='!#guestUser'>
														'<div class="uomLink" id="errorMsgForQty_{itemid}">{uomLink}</div>',
														</s:if>
														'</td>',
														'</tr>',
											
														//<!-- Only if mill/mfg message to be displayed -->
														<s:if test='!#guestUser'>
														'<tr>',
															'<td style="height:auto;"></td>',
															'<td class="mill-mfg" colspan="2">{itemtypedesc}</td>',
														'</tr>',
														//<!-- End mill/mfg -->
														'<tr class="line_error">',
															'<td colspan="3">',
															/* '<div class=\'error\' id=\'errorMsgForQty_{itemid}\' style=\'display : none\'/>Qty Should be greater than 02.</div>', */
															'<div class=\'error\' id=\'errorMsgForQty_{itemid}\' style=\'display : none\'/>{qtyGreaterThanZeroMsg}</div>',
															
															'</td>',
														'</tr>',
														</s:if>
													'</table>',
													//'<div class="normal-x">',
													//	'<div class="supersedelink">{supersedelink}</div>',
													//'</div>',
												'<div class="clearBoth">&nbsp;</div>',
												//'<div id="availabiltyRow"></div>',
												'<div class="show-hide-wrap">',
													'<div style="display: none;" id="availabilty_{itemid}" class="price-and-availability">',
														'<!-- end prefs  -->',
														'<table class="catalog-my-price-availability">',
															'<tbody>',
																'<tr class="my-headings">',
																	'<td colspan="3" class="leftmost my-availability"><span>Availability</span></td>',
																	'<td colspan="3" class="my-bracket"><span>My Bracket Pricing (USD)</span></td>',
																	'<td colspan="3" class="my-pricing"><span>Price (USD)</span></td>',
																'</tr>',
																'<tr>',
																	'<td class="leftmost my-available">Total Available:</td>',
																	'<td class="my-number">225,000,000</td>',
																	'<td class="my-uom">Lids</td>',
																	'<td class="my-bracket">1 Lid -</td>',
																	'<td class="my-number">$35.48</td>',
																	'<td class="my-uom">&nbsp;/&nbsp;Lid</td>',
																	'<td class="my-price">My Price:</td>',
																	'<td class="my-number">$999,999,999.90000</td>',
																	'<td class="my-uom">&nbsp;/&nbsp;Carton</td>',
																'</tr>',
																'<tr>',
																	'<td class="leftmost my-timeframe">Next Day:</td>',
																	'<td class="my-number">5,000</td>',
																	'<td>&nbsp;</td>',
																	'<td class="my-bracket">10 Lid -</td>',
																	'<td class="my-number"> $325.48</td>',
																	'<td class="my-uom">&nbsp;/&nbsp;Lid</td>',
																	'<td>&nbsp;</td>',
																	'<td class="my-number">$999,999,999.90000</td>',
																	'<td class="my-uom">&nbsp;/&nbsp;Lid</td>',
																'</tr>',
																'<tr>',
																	'<td class="leftmost my-timeframe">2+ Days: </td>',
																	'<td class="my-number">20,000</td>',
																	'<td>&nbsp;</td>',
																	'<td class="my-bracket">20 Lid -</td>',
																	'<td class="my-number">$3,325.48</td>',
																	'<td class="my-uom">&nbsp;/&nbsp;Lid</td>',
																	'<td colspan="3">&nbsp;</td>',
																'</tr>',
																'<tr>',
																	'<td class="leftmost my-local-availability" colspan="3">2,500 available today at Cincinnati, OH</td>',
																	'<td class="my-bracket">30 Lid - </td>',
																	'<td style="text-align: right;"> $335,555.48</td>',
																	'<td>&nbsp;/&nbsp;Lid</td>',
																	'<td class="my-ext-price">Extended Price:</td>',
																	'<td class="my-number">$999,999,999.90 </td>',
																	'<td></td>',
																'</tr>',
															'</tbody>',
														'</table>',
													'</div>',
												'</div>',
											'</dd>',
										'</tpl>',
									'</dl>',
								'</tpl><div style="clear:left"></div>',
						'</div>'
                        
                    );
                    extTemplates["normal-view"].compile();

					extTemplates["condensed-view"] = new Ext.XTemplate(
                    		 '<div id="item-ct">',
								'<tpl for=".">',
 										'<dl>',
										'<tpl for="items">',
											'<dd id="{itemkey}" class="itemdiv" style="height:auto;">',
												'<div class="imgs">',
												    '<a href="javascript:processDetail(\'{itemid}\',\'{uom}\');">',
													'<img title="{name}" alt="{name}" src="{icon}" class="prodImg" id="pimg_{#}"/></a>',
													'<div class="hidden bubble extDescDiv" id="extDescDiv_{#}"></div>',
												'</div>',
												'<div class="contents">',
													'<p class="pprice">{price}</p>',
													'<div class="descriptions">',
													 '<a id="item-detail-lnk" href="javascript:processDetail(\'{itemid}\',\'{uom}\');" tabindex="{tabidx}">',
													'<p class="ddesc">{name}</p></a>',
													'<div class="buttons"><a href="javascript:processDetail(\'{itemid}\',\'{uom}\');" >{buttons}</a></div></div>',
													'<div class="clearBoth">&nbsp;</div>',
													'</div>',
													'<table class="bottable">',
														'<tr>',
															'<td class="compare_check">',
																// Do not delete this code. This will come as a CR.
																//'<input type="checkbox" name="compare_{itemkey}" id="compare_{itemkey}" />',
																//'<label for="compare_{itemkey}">Compare</label>',
															'</td>',
														'</tr>',
														'<tr>',
															'<td class="item_number">',
																'<s:property value="wCContext.storefrontId" /> Item #: {itemid} {cert}',
															'</td>',
															'<td class="quantity_box">',
																<s:if test='!#guestUser'>
																'Qty:&nbsp;<input type="textfield" id=\'Qty_{itemid}\'  name=\'Qty_{itemid}\' value="" size="10" maxlength="10" onclick="javascript:setFocus(this);" onkeyup="javascript:isValidQuantity(this);javascript:qtyInputCheck(this, \'{itemid}\');" onmouseover="javascript:qtyInputCheck(this,  \'{itemid}\');"/>',
																'<input type="hidden" id="Qty_Check_Flag_{itemid}" name="Qty_Check_Flag_{itemid}" value="false"/>',
																</s:if>
															'</td>',
														'</tr>',
														'<tr>',
															'<td class="item_number">{partno}</td>',
															'<td class="uom_cell">',
															<s:if test='!#guestUser'>
															'{uomdisplay}',
															</s:if>
															'</td>',
														'</tr>',
														'<tr>',
															'<td class="mill-mfg">{itemtypedesc}</td>',
															'<td class="add_to_cart">',
																<s:if test='!#guestUser'>
																	<s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
																		'<div class="addtocart"><a class="" id=\'addtocart_{itemid}\' href="#"  onclick=\"javascript:addItemToCart(\'{itemid}\'); return false;\">Add to Cart</a></div>',
																	</s:if>
																	<s:else>
																		'<div class="addtocart"><a class="" id=\'addtocart_{itemid}\' href="#"  onclick=\"javascript:addItemToCart(\'{itemid}\'); return false;\">Add to Order</a></div>',
																	</s:else>
																</s:if>
															'</td>',
														'</tr>',
														'<tr>',
															'<td colspan="2">',
															<s:if test='!#guestUser'>
															'<div class="uomLink" id="errorMsgForQty_{itemid}">{uomLink}</div>',
															</s:if>
															'</td>',
														'</tr>',
														'<tr>',
														'<tr>',
														'</tr>',
															'<td class="line_error" colspan="2" style="width:50px;">',
															<s:if test='!#guestUser'>
															/* '<div class=\'error\' id=\'errorMsgForQty_{itemid}\' style=\'display : none\'/>Qty Should be greater than 03.</div>', */
															'<div class=\'error\' id=\'errorMsgForQty_{itemid}\' style=\'display : none\'/> {qtyGreaterThanZeroMsg} </div>',
															</s:if>
															'</td>',
														'</tr>',
													'</table>',
												'<div class="clearBoth">&nbsp;</div>',												
											'</dd>',
										'</tpl>',
									'</dl>',
								'</tpl><div style="clear:left"></div>',
						'</div>'                        
                    );
                    extTemplates["condensed-view"].compile();

					extTemplates["mini-view"] = new Ext.XTemplate(
                    		 '<div id="item-ct">',
								'<tpl for=".">',
 										'<dl>',
										'<tpl for="items">',
											'<dd id="{itemkey}" class="itemdiv">',
												'<div class="imgs">',
												    '<a href="javascript:processDetail(\'{itemid}\',\'{uom}\');" >',
													'<img title="{name}" alt="{name}" src="{icon}" class="prodImg" id="pimg_{#}"/></a>',
													'<div class="hidden bubble extDescDiv" id="extDescDiv_{#}"></div>',
												'</div>',
												'<div class="contents">',
													'<p class="pprice">{price}</p>',
													'<div class="descriptions">',
													 '<a id="item-detail-lnk" href="javascript:processDetail(\'{itemid}\',\'{uom}\');" tabindex="{tabidx}">',
													'<p class="ddesc">{name}</p></a>',
													'<div class="buttons"><a href="javascript:processDetail(\'{itemid}\',\'{uom}\');" >{buttons}</a></div></div>',
													'<div class="clearBoth">&nbsp;</div>',

														'<div class="item_number"><s:property value="wCContext.storefrontId" /> Item #: {itemid} {cert}<br />{partno} {itemtypedesc}</div>',
														'<div class="quantity_box">',
															'<div class="qty">',
																<s:if test='!#guestUser'>
																'Qty:&nbsp;<input type="textfield" id=\'Qty_{itemid}\'  name=\'Qty_{itemid}\' value="" size="10" maxlength="10" onclick="javascript:setFocus(this);" onkeyup="javascript:isValidQuantity(this);javascript:qtyInputCheck(this, \'{itemid}\');" onmouseover="javascript:qtyInputCheck(this,  \'{itemid}\');"/>',
																'<input type="hidden" id="Qty_Check_Flag_{itemid}" name="Qty_Check_Flag_{itemid}" value="false"/>',																
																</s:if>
															'</div>',
															'<div class="uom-select">',
															<s:if test='!#guestUser'>
															'{uomdisplay}',
															</s:if>		
															'</div>',
															'<div class="clearall">&nbsp;</div>',
															<s:if test='!#guestUser'>
																<s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey==''" >
																	'<div class="addtocart"><a class="" id=\'addtocart_{itemid}\' href="#"  onclick=\"javascript:addItemToCart(\'{itemid}\'); return false;\">Add to Cart</a></div>',
																</s:if>
																<s:else>
																	'<div class="addtocart"><a class="" id=\'addtocart_{itemid}\' href="#"  onclick=\"javascript:addItemToCart(\'{itemid}\'); return false;\">Add to Order</a></div>',
																 </s:else>	
															</s:if>
															<s:if test='!#guestUser'>
															'<div class="uomLink" id="errorMsgForQty_{itemid}">{uomLink}</div>',
															'<br/>',
															</s:if>
														'</div>',
														'<div class="line_error" >',
															<s:if test='!#guestUser'>
															/* '<div class=\'error\' id=\'errorMsgForQty_{itemid}\' style=\'display : none\'/>Qty Should be greater than 04.</div>', */
															'<div class=\'error\' id=\'errorMsgForQty_{itemid}\' style=\'display : none\'/>{qtyGreaterThanZeroMsg}</div>',
															</s:if>
														'</div>',
													'</div>',
													'<div class="clearBoth">&nbsp;</div>',																								
											'</dd>',
										'</tpl>',
									'</dl>',
								'</tpl><div style="clear:left"></div>',
						'</div>'                        
                    );
					extTemplates["mini-view"].compile();			
				
				

                    extTemplates["papergrid-view"] = new Ext.XTemplate(
                            '<div class="clearview">&nbsp;</div>',
     				       '<div id="item-ct">',
     				      '<table id="x-tbl-cmmn" class="listTableHeader ${templateName}">',
			                '<thead class="table-header-bar">',
			                   // '<tr style="border-top:none; border-left-color:#fff;">',
			                   '<tr>',
			                   	 '<td class="no-border table-header-bar-left desc-hname"><a href="#" onclick="toggleDescSort();">Description<span id="directionDescArrow"></span></a></td>',
			                   	<s:if test='!#isReadOnly && !#guestUser'>
			                   	 '<td class="M-hname" style="width:20px;" ><a href="#" title="Mill / Mfg. Item" onclick="toggleStockSort();">M<span id="directionMArrow"></span></a></td>',
			                   	 </s:if>
			                	 '<td class="Item-hname"><a href="#" onclick="toggleItemSort();">Item #<span id="directionItemArrow"></span></a></td>',
				                   	 
			                    <s:if test='#allowedColumns.contains("Size")'>
			                     '<td class="Size-hname"><a href="#" onclick="toggleSizeSort();">Size<span id="directionSizeArrow"></span></a></td>',
			                    </s:if>
			                    
			                    <s:if test='#allowedColumns.contains("Color")'>
			                    '<td class="Color-hname"><a href="#" onclick="toggleColorSort();">Color<span id="directionColorArrow"></span></a></td>',
			                    </s:if>
			                    
			                    <s:if test='#allowedColumns.contains("Basis")'>
			                    '<td class="Basis-hname"><a href="#" onclick="toggleBasisSort();">Basis<span id="directionBasisArrow"></span></a></td>',
			                    </s:if>
			                    
			                    <s:if test='#allowedColumns.contains("Mwt")'>
			                    '<td class="Mwt-hname"><a href="#" onclick="toggleMwtSort();">Mwt<span id="directionMwtArrow"></span></a></td>',
			                    </s:if>
			                    
			                    <s:if test='#allowedColumns.contains("Capacity")'>
				                    	'<td class="Capacity-hname"><a href="#" onclick="toggleCapacitySort();">Capacity<span id="directionCapacityArrow"></span></a></td>',
				                </s:if>
				                
			                    <s:if test='#allowedColumns.contains("Model")'>
			                    '<td class="Model-hname"><a href="#" onclick="toggleModelSort();">Model<span id="directionModelArrow"></span></a></td>',
			                    </s:if>
			                    
			                    <s:if test='#allowedColumns.contains("Material")'>
			                    '<td class="Material-hname"><a href="#" onclick="toggleMaterialSort();">Material<span id="directionMaterialArrow"></span></a></td>',
			                    </s:if>				                
				                
			                    <s:if test='#allowedColumns.contains("Ply")'>
			                    '<td class="Ply-hname"><a href="#" onclick="togglePlySort();">Ply<span id="directionPlyArrow"></span></a></td>',
			                    </s:if>				                

			                    <s:if test='#allowedColumns.contains("Form")'>
			                    '<td class="Form-hname"><a href="#" onclick="toggleFormSort();">Form<span id="directionFormArrow"></span></a></td>',
			                    </s:if>			                    				                
				                		
			                    <s:if test='#allowedColumns.contains("Gauge")'>
			                    '<td class="Gauge-hname"><a href="#" onclick="toggleGaugeSort();">Gauge<span id="directionGaugeArrow"></span></a></td>',
			                    </s:if>			                    
			                    	                    
			                    <s:if test='#allowedColumns.contains("Vendor")'>
			                    '<td class="Vendor-hname"><a href="#" onclick="toggleVendorSort();">Mfg. Item #<span id="directionVendorArrow"></span></a></td>',
			                    </s:if>
			                    
			                    <s:if test='!#isReadOnly && !#guestUser'>
			                    <s:if test='#allowedColumns.contains("Environment")'>
                   			     '<td class="Environment-hname"><a class="underlink" onclick="toggleLeafSort();"><img style="margin-left:0px; display: inline; padding: 5px 0px 5px 10px;" src="/swc/xpedx/images/catalog/green-e-logo_small.png" ><span id="directionCertArrow"></span></a> </td>',
                   			    </s:if>  
                    			</s:if>

                    			<s:else>
                    			<s:if test='#allowedColumns.contains("Environment")'>
                  			     '<td class="Environment-hname table-header-bar-right"><a class="underlink" onclick="toggleLeafSort();"><img style="margin-left:0px; display: inline; padding: 5px 0px 5px 30px;" src="/swc/xpedx/images/catalog/green-e-logo_small.png" ><span id="directionCertArrow"></span></a> </td>',
                  			    </s:if> 
                   			    </s:else>
                    			                 			    			                    			                                         			
			                    <s:if test='!#isReadOnly && !#guestUser'>
			                    '<td class="no border table-header-bar-right lprice-hname"><span style="color:white;">List Price</span></td>',
			                    </s:if>
			                    
			                    <s:if test='!#isReadOnly && !#guestUser'>
           					//	'<td class="no border table-header-bar-right sorttable_nosort" align="center">Action</td>',
								</s:if>
								
			                    '</tr>',
			                '</thead>',
     			                '<tpl for=".">',
     			                '<tbody>',
     			                '<tpl for="items">',
     			                
     			                    '<tr id="{itemkey}" class="itemrow">',
     			                    
     			                    	'<td id="desctab">'+
     			                    		'<a id="item-detail-lnk" href="javascript:processDetail(\'{itemid}\',\'{uom}\');" tabindex="{tabidx}">',
											'<span class="ddesc desc-hname">{name}</span></a>',
     			                    	'</td>',
     			                    	<s:if test='!#isReadOnly && !#guestUser'>
     			                    	'<td class="stock-status M-hname">{stocked}</td>',
     			                    	</s:if>
     			                    	'<td>'+
     			                    	'<a  id="item-detail-lnk" href="javascript:processDetail(\'{itemid}\',\'{uom}\');" tabindex="{tabidx}">',
     			                    	'<span class="ddesc desc-hname">{itemid}</span></a>',
     			                    	'</td>',
     			                        <s:if test='#allowedColumns.contains("Size")'>
     			                        	'<td class="Size-hname">{size}</td>',
     			                        </s:if>
     			                        
     			                        <s:if test='#allowedColumns.contains("Color")'>
     			                        	'<td class="Color-hname">{color}</td>',
     			                        </s:if>
     			                        
     			                        <s:if test='#allowedColumns.contains("Basis")'>
     			                        '<td class="Basis-hname">{basis}</td>',
     			                        </s:if>
     			                        
     			                        <s:if test='#allowedColumns.contains("Mwt")'>
     			                        '<td class="Mwt-hname">{mwt}</td>',
     			                        </s:if>     			                        
 
     			                        <s:if test='#allowedColumns.contains("Capacity")'>
     				                    	'<td class="Capacity-hname">{capacity}</td>',
     				                    </s:if>     			                            			                        
     			                        
     				                    <s:if test='#allowedColumns.contains("Model")'>
     				                    '<td class="Model-hname">{model}</td>',
     				                    </s:if>     				                    
     				                    
     				                    <s:if test='#allowedColumns.contains("Material")'>
     				                    '<td class="Material-hname">{material}</td>',
     				                    </s:if>
     				                    
     				                    <s:if test='#allowedColumns.contains("Ply")'>
     				                    '<td class="Ply-hname">{ply}</td>',
     				                    </s:if>
     				                    
     				                    <s:if test='#allowedColumns.contains("Form")'>
     				                    '<td class="Form-hname">{form}</td>',
     				                    </s:if>
     				                    
     				                    <s:if test='#allowedColumns.contains("Gauge")'>
     				                    '<td class="Gauge-hname">{gauge}</td>',
     				                    </s:if>    				                       				                     				                         				                         				                         				                    
     				                         
     			                        <s:if test='#allowedColumns.contains("Vendor")'>
     				                    	'<td class="Vendor-hname">{vendorNumber}</td>',
     				                    </s:if>

     			                        <s:if test='#allowedColumns.contains("Environment")'>
                         				'<td class="Environment-hname" style="padding: 0px 25px 0px 0px;">{cert}</td>',
                         				</s:if>
                         				
                         				
     			                        <s:if test='!#isReadOnly && !#guestUser'>
     			                        '<td class="lprice-hname">{listprice}</td>',
     			                        </s:if>
     			                        <s:if test='!#isReadOnly && !#guestUser'>
                 						//	'<td id"action-cart">{buttonsprice}</td>',
     									</s:if>
     									
     			                    '</tr>',
     			                
     			                '</tpl>',
     			                '</tbody>',
     			                '</table>',
     			            '</tpl>',
     			           '<div id="table-bottom-bar" style="width:724px;">',
     						'<div id="table-bottom-bar-L"></div>',
     						'<div id="table-bottom-bar-R"></div>',
     						'</div>',
     					'</div>');
                    extTemplates["papergrid-view"].compile();

                    var selectedExtTemplate = null;
                    var ct = Ext.get('item-box-inner');

                  
                      // IW Update the extJS view
                    // NOTE: This process slows down after switching between views several times!
                     function initializeView()
                    {
                    	
                    	var ct = Ext.get('item-box-inner');
                    
											ct.on('mouseover', function (e, t) {
													if (t = e.getTarget('dd')) {
															Ext.fly(t).addClass('over');
													}
											});
											
											ct.on('mouseout', function (e, t) {
													if ((t = e.getTarget('dd')) && !e.within(t, true)) {
															Ext.fly(t).removeClass('over');
													}
											});
										
											Ext.get('items-cb').on('click', function (e) {
													// Change view
													var img = e.getTarget('img', 2);
													if (img) {
															Ext.getDom('items').className = img.className;
													}

													document.getElementById("selectedView").value = Ext.getDom('items').className;
													/******* IW Update the extJS View ******/
													updateView();

													shortenItemDescriptions();

													var url = "<s:property value='#selectedViewURL'/>";//'/swc/catalog/setSelectedView.action?sfId=xpedx&amp;scFlag=Y&amp;scGuestUser=Y';
													url = ReplaceAll(url,"&amp;",'&');
													Ext.Ajax.request({
															// for testing only
															url: url,
															params: {
																 selectedView: document.getElementById("selectedView").value
															},
															// end testing
															method: 'GET',
															callback: function () {
																	svg_classhandlers_decoratePage(); //TODO: not working for IE
															}
													});
											});
											
											setItemDragZone(Ext.get('item-box-inner'));
											
											setItemDropZone(Ext.get('items-combox'));
											
											
                    }


                    function loadView()
                    {
                    	//document.getElementById("selectedView").value = Ext.getDom('items').className; JIRA 1459
						//updateView();

						//shortenItemDescriptions();

						//var url = '/swc/catalog/setSelectedView.action?sfId=xpedx&amp;scFlag=Y';
						 initializeView(); updateView(); shortenItemDescriptions(); 
						 var url = "<s:property value='#selectedViewURL'/>";
					     url = ReplaceAll(url,"&amp;",'&');
						Ext.Ajax.request({
								// for testing only
								url: url,
								params: {
									 selectedView: document.getElementById("selectedView").value
								},
								// end testing
								method: 'POST',
								callback: function () {
										svg_classhandlers_decoratePage(); //TODO: not working for IE
								}
						});
                    }
                                        
                    // IW Update the extJS view
                    // NOTE: This process slows down after switching between views several times!
                    function updateView() {
                    	
                        var ct = Ext.get('item-box-inner');
                    	
                        var selectedClass = document.getElementById("selectedView").value;
                      
                        var tpl = extTemplates[selectedClass];
                      
                        if (tpl == selectedExtTemplate)
                           return; // only change html layout when necessary
                        
                       selectedExtTemplate = tpl;
                      
                        tpl.overwrite(ct, catalog);
                       
                        itemwin = new Ext.Window({
                            id: 'itemwin',
                            layout: 'fit',
                            width: 400,
                            height: 500,
                            shadow: 'drop',
                            shadowOffset: 10,
                            closeAction: 'hide',
                            modal: true,
                            resizable: false
                        });
                      
                        // select default view. can be based on user's last selection or preference
                        //Ext.getDom('items').className = 'condensed-view';
                        Ext.getDom('items').className = document.getElementById("selectedView").value;
                     
                        Ext.get('items-combox').addClassOnOver('com-over', true);
                        svg_classhandlers_decoratePage();
                        $("#x-tbl-cmmn tbody tr:odd").css("background-color", "#fafafa");
                        $("#x-tbl-cmmn tbody tr:even").css("background-color", "#fff");
                        
                        if("papergrid-view" == Ext.getDom('items').className){
                	        forEach(document.getElementsByTagName('table'), function(table) {
                	          if (table.className.search(/\bsortable\b/) != -1) {
                	            sorttable.makeSortable(table);
                	            headrow = table.tHead.rows[0].cells;
                	            if(defaultSortColumn != undefined)
                	            {
                	            	sorttable.auto_clicked(headrow[defaultSortColumn]);
                	            }
                	          }
                	        });
                		}
                        
                    }

                    Ext.onReady(function () {});

                    function shortenItemDescriptions()
                    {
                    	var selectedView = Ext.getDom('items').className;

                    	ddescWidth = 0;

                    	$('#item-box-inner dd .ddesc').each(function(){
                    			// Optimization: call width() only once, because each .ddesc is the same width
                    			if (ddescWidth == 0)
                    				ddescWidth = ($(this).width() * (selectedView == 'normal-view' ? 2.85 : 1.6));

                    			$(this).shorten({width: ddescWidth});
                    	});

                    	if (selectedView == 'normal-view' || selectedView == 'condensed-view')
                    	{
												$('#item-box-inner li').each(function(){
														$(this).shorten({noblock: true, width: ($(this).width() - 20)});
												});
											}
					}

                    function processDetail(itemid, uom) {
                    	var selView = document.getElementById("selectedView").value; 
                    	var storeFrontId = "<s:property value='wCContext.storefrontId' />";                   	
                        window.location.href = "/swc/catalog/itemDetails.action?sfId=" + storeFrontId + "&_bcs_=%11true%12%12%12%2Fswc%2Fcatalog%2Fnavigate.action%3FsfId%3Dxpedx%26scFlag%3DY%26%12%12catalog%12search%12%11&scFlag=Y" + "&itemID=" + itemid + "&unitOfMeasure=" + uom+"&selectedView="+selView;;
                    }

                    function setItemDragZone(v) {
                        v.dragZone = new Ext.dd.DragZone(v, {
                            getDragData: function (e) {
                            	//, #x-tbl-cmmn tbody tr
                                var sourceEl = e.getTarget('dd.itemdiv', 10) || e.getTarget('tr.itemrow', 10);
                                if (sourceEl) {
                                    d = Ext.query('.ddesc', sourceEl)[0].cloneNode(true);
                                    d.className = 'draggeditem';
                                    d.id = Ext.id();
                                    return v.dragData = {
                                        sourceEl: sourceEl,
                                        repairXY: Ext.fly(sourceEl).getXY(),
                                        ddel: d,
                                        itemid: sourceEl.id
                                    }
                                }
                            },
                            getRepairXY: function () {
                                return this.dragData.repairXY;
                            },
                            onMouseUp: function (evt) {
                            	var fld = evt.getTarget("input", 5);
                            	if (fld) fld.focus();
                            }
                        });
                    }

                    function setItemDropZone(g) {
                        g.dropZone = new Ext.dd.DropZone(g, {
                            getTargetFromEvent: function (e) {
                                var tEle = e.getTarget('#items-combox');
                                return tEle;
                            },
                            onNodeEnter: function (target, dd, e, data) {
                            },
                            onNodeOut: function (target, dd, e, data) {
                            },
                            onNodeOver: function (target, dd, e, data) {
                                return Ext.dd.DropZone.prototype.dropAllowed;
                            },
                            onNodeDrop: function (target, dd, e, data) {
                                if(data.itemid == '') {
                                    alert('This item cannot be compared at the moment. Please try again later');
                                    return true;
                                }
                                else {
	                                addCompare(data.itemid);
	                                return true;
                                }
                            }
                        });
                    }


                    <s:set name='wccontext' value="#_action.getWCContext()"/>
                    var compCount = <s:property value='#catUtil.getComparisonSetSize(#wccontext)'/>;
                    function updateCompareBucket(increment, effect) {
                        var c = Ext.get('comnum');
                        if (increment)
                            compCount += increment;
                        c.dom.innerHTML = (compCount == 1) ? (compCount + " Item") : (compCount + " Items");

                        if (effect) {
                            Ext.get('items-combox').highlight("ffdd00", { duration: 3 });
                            Ext.get('items-combox').frame("00ff00", 1, { duration: 1 });
                        }
                    }

                    function addCompare(itemKey) {
                    		if (compCount == 4)
                    		{
                    			//alert(' uld');
                    			alert ( "<s:text name='MSG.SWC.COMP.DRAGTOCOMPARE.ERROR.MORETHAN4ITEMS' /> " );
                    			return false;
                    		}

                       // alert("dropped! "+itemKey);
                        
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
                                //alert("addCompare successful...");
                                updateCompareBucket(1, true);
                            },
                            failure: function (response, request) {
                            	// TODO
                            	// TODO: CHANGE THIS
                            	// TODO
                            	updateCompareBucket(1, true);
                                // alert("addCompare failed...");
                            }
                        });
                    }

                    Ext.onReady(function ()
                    	{
                    		return; // disabled
                        var imgs = Ext.query('.prodImg');
                        var qvlTitle = document.getElementById("quickViewLaunchTitle").innerHTML;
                        for (var i = 0; i < imgs.length; i++) {
                        	var tmp = imgs[i].id.split("_");
                        	var idx = tmp[tmp.length - 1];
                        	bubbleURL = document.getElementById("quickViewURL_" + idx).href;
                        	qvTitle = document.getElementById("quickViewTitle_" + idx).innerHTML;
                        	new QuickView(imgs[i],
                        		{
                        			url: bubbleURL,
                        			callback: svg_classhandlers_decoratePage
                        		},
                        		{
                        			title: qvlTitle,
                        			quickViewWindowConf: {
                        				animateFromAnchor: false,
                        				windowConf: {
                        					title: qvTitle,
                        					width: 750,
                        					//height: 400
                        					autoHeight: true
                        				}
                        			}
                        		});
                        }
                      });


                    Ext.onReady(function () {
                        updateCompareBucket(0);
                    });
                    Ext.onReady(function () {
                    	loadAfterCompilation();
                    });
                    /* end of js copy*/
   
function toggleDescSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
 // alert(theQueryString);
 //var theImageSpan = eval("document.getElementById('" + "directionDescArrow" + "')" );
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
        //theImageSpan.innerHTML = '&nbsp;<img alt="" src="/swc/xpedx/images/12x12_white_down.png" class="sort-order sort-desc">';	  
		processSortByUpperTroy("SortableShortDescription--D","sortDown","directionDescArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
	  	// theImageSpan.innerHTML = '&nbsp;<img alt="" src="/swc/xpedx/images/icons/12x12_white_up.png" class="sort-order sort-desc">';
		processSortByUpperTroy("SortableShortDescription--A","sortUp","directionDescArrow");
	  }
	  else
	  {
	  //	 theImageSpan.innerHTML = '&nbsp;<img alt="" src="/swc/xpedx/images/icons/12x12_white_up.png" class="sort-order sort-desc">';
	   processSortByUpperTroy("SortableShortDescription--A","sortUp","directionDescArrow");
	  }
  }
  else
  {
//theImageSpan.innerHTML = '&nbsp;<img alt="" src="/swc/xpedx/images/icons/12x12_white_up.png" class="sort-order sort-desc">';
   processSortByUpperTroy("SortableShortDescription--A","sortUp","directionDescArrow");
  }
}        

function toggleSizeSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
 // alert(theQueryString);
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
	//    alert("toggleDescSort - sortDown");
		processSortByUpperTroy("ExtnSize--D","sortDown","directionSizeArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
	//    alert("toggleDescSort - sortUp");
		processSortByUpperTroy("ExtnSize--A","sortUp","directionSizeArrow");
	  }
	  else
	  {
	//   alert("toggleDescSort - default1");
	   processSortByUpperTroy("ExtnSize--A","sortUp","directionSizeArrow");
	  }
  }
  else
  {
 //  alert("toggleDescSort - default2");
   processSortByUpperTroy("ExtnSize--A","sortUp","directionSizeArrow");
  }
}

function toggleItemSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
 // alert(theQueryString);
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
	//    alert("toggleDescSort - sortDown");
		processSortByUpperTroy("ItemID--D","sortDown","directionItemArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
	//    alert("toggleDescSort - sortUp");
		processSortByUpperTroy("ItemID--A","sortUp","directionItemArrow");
	  }
	  else
	  {
	//   alert("toggleDescSort - default1");
	   processSortByUpperTroy("ItemID--A","sortUp","directionItemArrow");
	  }
  }
  else
  {
 //  alert("toggleDescSort - default2");
   processSortByUpperTroy("ItemID--A","sortUp","directionItemArrow");
  }
}

function toggleCertSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
 // alert(theQueryString);
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
	//    alert("toggleDescSort - sortDown");
		processSortByUpperTroy("ExtnCert--D","sortDown","directionCertArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
	//    alert("toggleDescSort - sortUp");
		processSortByUpperTroy("ExtnCert--A","sortUp","directionCertArrow");
	  }
	  else
	  {
	//   alert("toggleDescSort - default1");
	   processSortByUpperTroy("ExtnCert--A","sortUp","directionCertArrow");
	  }
  }
  else
  {
 //  alert("toggleDescSort - default2");
   processSortByUpperTroy("ExtnCert--A","sortUp","directionCertArrow");
  }
}   

function toggleRelevancySort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
 // alert(theQueryString);
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
	//    alert("toggleDescSort - sortDown");
		processSortByUpperTroy("relevancy","sortDown","directionMArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
	//    alert("toggleDescSort - sortUp");
		processSortByUpperTroy("relevancy","sortUp","directionMArrow");
	  }
	  else
	  {
	//   alert("toggleDescSort - default1");
	   processSortByUpperTroy("relevancy","sortUp","directionMArrow");
	  }
  }
  else
  {
 //  alert("toggleDescSort - default2");
   processSortByUpperTroy("relevancy","sortUp","directionMArrow");
  }
}

function toggleColorSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
 // alert(theQueryString);
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
	//    alert("toggleDescSort - sortDown");
		processSortByUpperTroy("ExtnColor--D","sortDown","directionColorArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
	//    alert("toggleDescSort - sortUp");
		processSortByUpperTroy("ExtnColor--A","sortUp","directionColorArrow");
	  }
	  else
	  {
	//   alert("toggleDescSort - default1");
	   processSortByUpperTroy("ExtnColor--A","sortUp","directionColorArrow");
	  }
  }
  else
  {
 //  alert("toggleDescSort - default2");
   processSortByUpperTroy("ExtnColor--A","sortUp","directionColorArrow");
  }
}

function toggleGaugeSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
		processSortByUpperTroy("ExtnGauge--D","sortDown","directionGaugeArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
		processSortByUpperTroy("ExtnGauge--A","sortUp","directionGaugeArrow");
	  }
	  else
	  {
	   processSortByUpperTroy("ExtnGauge--A","sortUp","directionGaugeArrow");
	  }
  }
  else
  {
   processSortByUpperTroy("ExtnGauge--A","sortUp","directionGaugeArrow");
  }
}

function toggleBasisSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
		processSortByUpperTroy("ExtnBasis--D","sortDown","directionBasisArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
		processSortByUpperTroy("ExtnBasis--A","sortUp","directionBasisArrow");
	  }
	  else
	  {
	   processSortByUpperTroy("ExtnBasis--A","sortUp","directionBasisArrow");
	  }
  }
  else
  {
   processSortByUpperTroy("ExtnBasis--A","sortUp","directionBasisArrow");
  }
}


function toggleEnvironmentSort()
{
	
}

function toggleMwtSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
		processSortByUpperTroy("ExtnMwt--D","sortDown","directionMwtArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
		processSortByUpperTroy("ExtnMwt--A","sortUp","directionMwtArrow");
	  }
	  else
	  {
	   processSortByUpperTroy("ExtnMwt--A","sortUp","directionMwtArrow");
	  }
  }
  else
  {
   processSortByUpperTroy("ExtnMwt--A","sortUp","directionMwtArrow");
  }
}

function toggleLeafSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
 // alert(theQueryString);
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
	//    alert("toggleDescSort - sortDown");
		processSortByUpperTroy("ExtnCert--D","sortDown","directionCertArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
	//    alert("toggleDescSort - sortUp");
		processSortByUpperTroy("ExtnCert--A","sortUp","directionCertArrow");
	  }
	  else
	  {
	//   alert("toggleDescSort - default1");
	   processSortByUpperTroy("ExtnCert--A","sortUp","directionCertArrow");
	  }
  }
  else
  {
 //  alert("toggleDescSort - default2");
   processSortByUpperTroy("ExtnCert--A","sortUp","directionCertArrow");
  }
}  

function togglePlySort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
		processSortByUpperTroy("ExtnPly--D","sortDown","directionPlyArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
		processSortByUpperTroy("ExtnPly--A","sortUp","directionPlyArrow");
	  }
	  else
	  {
	   processSortByUpperTroy("ExtnPly--A","sortUp","directionPlyArrow");
	  }
  }
  else
  {
   processSortByUpperTroy("ExtnPly--A","sortUp","directionPlyArrow");
  }
}

function toggleModelSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
		processSortByUpperTroy("ExtnModel--D","sortDown","directionModelArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
		processSortByUpperTroy("ExtnModel--A","sortUp","directionModelArrow");
	  }
	  else
	  {
	   processSortByUpperTroy("ExtnModel--A","sortUp","directionModelArrow");
	  }
  }
  else
  {
   processSortByUpperTroy("ExtnModel--A","sortUp","directionModelArrow");
  }
}

function toggleFormSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
		processSortByUpperTroy("ExtnForm--D","sortDown","directionFormArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
		processSortByUpperTroy("ExtnForm--A","sortUp","directionFormArrow");
	  }
	  else
	  {
	   processSortByUpperTroy("ExtnForm--A","sortUp","directionFormArrow");
	  }
  }
  else
  {
   processSortByUpperTroy("ExtnForm--A","sortUp","directionFormArrow");
  }
}

function toggleMaterialSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
		processSortByUpperTroy("ExtnMaterial--D","sortDown","directionMaterialArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
		processSortByUpperTroy("ExtnMaterial--A","sortUp","directionMaterialArrow");
	  }
	  else
	  {
	   processSortByUpperTroy("ExtnMaterial--A","sortUp","directionMaterialArrow");
	  }
  }
  else
  {
   processSortByUpperTroy("ExtnMaterial--A","sortUp","directionMaterialArrow");
  }
}

function toggleCapacitySort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
 // alert(theQueryString);
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
	//    alert("toggleDescSort - sortDown");
		processSortByUpperTroy("ExtnCapacity--D","sortDown","directionCapacityArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
	//    alert("toggleDescSort - sortUp");
		processSortByUpperTroy("ExtnCapacity--A","sortUp","directionCapacityArrow");
	  }
	  else
	  {
	//   alert("toggleDescSort - default1");
	   processSortByUpperTroy("ExtnCapacity--A","sortUp","directionCapacityArrow");
	  }
  }
  else
  {
 //  alert("toggleDescSort - default2");
   processSortByUpperTroy("ExtnCapacity--A","sortUp","directionCapacityArrow");
  }
}
function toggleVendorSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
		processSortByUpperTroy("ExtnVendorNo--D","sortDown","directionVendorArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
		processSortByUpperTroy("ExtnVendorNo--A","sortUp","directionVendorArrow");
	  }
	  else
	  {
	   processSortByUpperTroy("ExtnVendorNo--A","sortUp","directionVendorArrow");
	  }
  }
  else
  {
   processSortByUpperTroy("ExtnVendorNo--A","sortUp","directionVendorArrow");
  }
}

function toggleStockSort()
{
  theUrlValue=window.location.href;
  var theQueryString=window.location.search;
  if(theQueryString.indexOf("sortDirection") > -1)
  {
	  if(theQueryString.indexOf("sortDown") > -1)
	  {
		processSortByUpperTroy("showNormallyStockedItems--D","sortDown","directionMArrow");
	  }
	  else if(theQueryString.indexOf("sortUp" > -1))
	  {
		processSortByUpperTroy("showNormallyStockedItems--A","sortUp","directionMArrow");
	  }
	  else
	  {
	   processSortByUpperTroy("showNormallyStockedItems--A","sortUp","directionMArrow");
	  }
  }
  else
  {
   processSortByUpperTroy("showNormallyStockedItems--A","sortUp","directionMArrow");
  }
}

function processSortByUpperTroy(theValue,directionValue,theSpanNameValue)
{
<s:url id='sortFieldsURL' action='sortResultBy' namespace='/catalog'/>
theUrl = "<s:property value='%{sortFieldsURL}' escape='false'/>";
var sortFieldValue;

if(theValue.indexOf("showNormallyStockedItems") > -1)
{
sortFieldValue = theValue;
}
else{
sortFieldValue = "Item." + theValue;
}
var theImageSpan = eval("document.getElementById('" + theSpanNameValue + "')" );
//alert(theImageSpan);

if(directionValue == "sortUp")
{
// alert("up");
theUrl = theUrl + "&sortField=" + sortFieldValue + "&sortDirection=" + "sortDown"+"&theSpanNameValue="+theSpanNameValue;
theImageSpan.innerHTML = '&nbsp;<img alt="" src="/swc/xpedx/images/12x12_white_up.png" class="sort-order sort-desc">';
// alert(theImageSpan);
}
else if(directionValue == "sortDown")
{
//alert("down");
theUrl = theUrl + "&sortField=" + sortFieldValue + "&sortDirection=" + "sortUp"+"&theSpanNameValue="+theSpanNameValue;
theImageSpan.innerHTML = '&nbsp;<img alt="" src="/swc/xpedx/images/12x12_white_down.png" class="sort-order sort-desc">';
// alert(theImageSpan);
}
setTimeout("changeUrl()",1000);
}  
function changeUrl()
{
  window.location.href=theUrl
}

                
function processSortByUpper(){
	<s:url id='sortFieldsURL' action='sortResultBy' namespace='/catalog'/>
	var sortFieldValue = Ext.fly('sortFieldUpper').dom.value;
	window.location.href="<s:property value='%{sortFieldsURL}' escape='false'/>" + "&sortField=" + sortFieldValue;
}

function processSortByLower(){
	<s:url id='sortFieldsURL' action='sortResultBy' namespace='/catalog'/>
	var sortFieldValue = Ext.fly('sortFieldLower').dom.value;
	window.location.href="<s:property value='%{sortFieldsURL}' escape='false'/>" + "&sortField=" + sortFieldValue;
}

function processPageSizeUpper(){
	<s:url id='pageSizeURL' action='selectPageSize' namespace='/catalog'/>
	var pageSizeValue = Ext.fly('pageSizeUpper').dom.value;
	window.location.href="<s:property value='%{pageSizeURL}' escape='false'/>" + "&pageSize=" + pageSizeValue;
}

function processPageSizeLower(){
	<s:url id='pageSizeURL' action='selectPageSize' namespace='/catalog'/>
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
        window.location.href="<s:property value='%{compareURL}' escape='false'/>";
    }
    
    
}
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
	<div id="items-cb"><img src="../xpedx/images/global/s.gif"
		class="normal-view" title="Full View"><img
		src="../xpedx/images/global/s.gif" class="condensed-view"
		title="Condensed View"><img src="../xpedx/images/global/s.gif"
		class="mini-view" title="Mini View"><!-- IW 7/16/2010: new icon/button for papergrid-view --><img src="../xpedx/images/global/s.gif" class="papergrid-view"
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
	
	
	<p class="pageresults"><s:property value='#numResult' /> Results<span>&nbsp;|&nbsp;Page&nbsp</span>
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
</swc:breadcrumbScope>
<!-- // container end -->
<!-- begin swc:dialogPanel -->
<div class="x-hidden dialog-body " id="modalDialogPanel1Content">
<div class="dialog-body" id="ajax-body-1"></div>
</div>
<script type="text/javascript">

        Ext.onReady(function () {
            var w = new Ext.Window({
                autoScroll: true,
                closeAction: 'hide',
                cls: 'swc-ext',
                contentEl: 'modalDialogPanel1Content',
                hidden: true,
                id: 'modalDialogPanel1',
                modal: true,
                width: '600',
                height: 'auto',
                resizable: true,
                shadow: 'drop',
                shadowOffset: 10,
                title: 'Add To Cart Result'
            });
        });
    </script>
<!-- end swc:dialogPanel -->
<!-- begin swc:dialogPanel -->
<div class="x-hidden dialog-body " id="modalDialogPanel2Content">
<div class="supersedeBox" id="ajax-body-2"></div>
</div>
<script type="text/javascript">

        Ext.onReady(function () {
            var w = new Ext.Window({
                autoScroll: true,
                closeAction: 'hide',
                cls: 'swc-ext',
                contentEl: 'modalDialogPanel2Content',
                hidden: true,
                id: 'modalDialogPanel2',
                modal: true,
                width: '600',
                height: 'auto',
                resizable: true,
                shadow: 'drop',
                shadowOffset: 10,
                title: 'Supersession'
            });
        });
    </script>
<!-- end swc:dialogPanel -->
<!-- begin swc:dialogPanel -->
<div class="x-hidden dialog-body " id="product_availabilityContent">
<div id="ajax-prodAvailability">test</div>
</div>
<script type="text/javascript">

        Ext.onReady(function () {
            var w = new Ext.Window({
                autoScroll: true,
                closeAction: 'hide',
                cls: 'swc-ext',
                contentEl: 'product_availabilityContent',
                hidden: true,
                id: 'product_availability',
                modal: true,
                width: '600',
                height: 'auto',
                resizable: true,
                shadow: 'drop',
                shadowOffset: 10,
                title: 'Product Availability'
            });
        });

    </script>
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
<script>

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
    //valid size if 10,3(7 whole and 3 after digits)
    /*var quantity = component.value.trim();
    var qtyLen = quantity.length;
    var validVals = "0123456789.";
    var isValid=true;
    var char;
    for (i = 0; i < qtyLen && isValid == true; i++) {
       char = quantity.charAt(i); 
       if (validVals.indexOf(char) == -1) 
       {
          isValid = false;
       }
 	}
 	if (!isValid){
        component.value = "";
        return false;
 	}

    var qtyDecPointAt = quantity.indexOf(".");
    var wholenum = quantity.substr(0,qtyDecPointAt);
	if (qtyDecPointAt == -1){
		wholenum = quantity;
	}
    if (wholenum.length > 7){
        var val = quantity.substr(0,7);
		if (qtyDecPointAt != -1){
			val = val + quantity.substr(qtyDecPointAt,qtyLen);
		}
		quantity = val;
        component.value = quantity;
    }
    if (qtyDecPointAt != -1 && qtyDecPointAt < qtyLen - 4) {
        quantity = quantity.substr(0,qtyDecPointAt + 4);
        component.value = quantity;
        return false;
    }

    return true;
    */
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

LoadPage();
loadView();
//isClassBasedSort="N";
//compileOnLoad();
</script>		

	

</body>
</html>
