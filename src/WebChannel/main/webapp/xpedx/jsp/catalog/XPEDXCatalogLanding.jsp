<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<%@ taglib prefix="xpedx" uri="/WEB-INF/xpedx.tld"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<%
  		request.setAttribute("isMergedCSSJS","true");
  	  %>
<!-- INCLUDES GO HERE -->
<!-- Version 1.1 Updated 8-18-10 -->
<!-- begin styles. These should be the only three styles. -->


<s:set name='isGuestUser' value="wCContext.guestUser" />
<s:if test="#isGuestUser != true">
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
 <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/CATALOG<s:property value='#wcUtil.xpedxBuildKey' />.css" />

<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<![endif]-->	

</s:if>

<s:else>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/CATALOG<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<![endif]-->

</s:else>
<!-- end styles -->


<!-- sterling 9.0 base  do not edit  javascript move all functions to js/global-xpedx-functions.js -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-jquery-headder<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />

<title><s:property value="wCContext.storefrontId" /> - <s:text name='catalog.title' /></title>
</head>
<!-- END swc:head -->
<s:set name='_action' value='[0]'/>
<body class="  ext-gecko ext-gecko3">
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
<s:bean name='com.sterlingcommerce.webchannel.catalog.utils.CatalogUtilBean' id='catUtil' />
<s:url action='navigate.action' namespace='/catalog' id='myUrl'/>
<s:set name='myParam' value='{"searchTerm", "", "cname"}'/>
<s:url id='addToCartURL' namespace='/order' action='addToCart'/>
<s:url id='punchOutURL' namespace='/order' action='configPunchOut'/>
<s:url id='selectedViewURL' action='setSelectedView' namespace='/catalog'/>
<s:set name='appFlowContext' value='#session.FlowContext'/>
<s:set name='isFlowInContext' value='#util.isFlowInContext(#appFlowContext)'/>
<s:set name='guestUser' value="#_action.getWCContext().isGuestUser()" />
<swc:breadcrumb rootURL='#myUrl' group='catalog' displayGroup='search' displayParam='#myParam'/> 
<s:url id='punchOutURLOrderChange' namespace='/order' action='configPunchOut'>
   <s:param name='orderHeaderKey' value='%{#appFlowContext.key}'/>
   <s:param name='currency' value='%{#appFlowContext.currency}'/>
   <s:param name='flowID' value='%{#appFlowContext.type}'/>
</s:url>
<s:set name='isProcurementInspectMode' value='#util.isProcurementInspectMode(wCContext)'/>
<s:set name='isReadOnly' value='#isProcurementInspectMode'/>
<s:set name='catDoc' value='%{outDoc.documentElement}' />
<s:set name='numResult' value='#catDoc.getAttribute("TotalHits")'/>
<s:set name='pageNumber' value='#catDoc.getAttribute("PageNumber")'/>
<s:set name='totalNumberOfPages' value='#catDoc.getAttribute("TotalPages")'/>
<s:url id='compareURL'  action='prodCompare.action' namespace='/catalog'/>
<swc:breadcrumbScope>
<!--web trands start -->
<s:if test='!#guestUser'> 
</s:if>
<s:else>
<s:if test='%{(#parameters.cname!="")}'>
	<meta name="WT.ti" content="<s:property value='%{#parameters.cname}' />"/>
</s:if>
<meta name="WT.ti" content="<s:text name="catalog.title" />" /> 
</s:else>
<!--web trands end -->
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
<div class="container catalog" style="overflow: auto;" >
</s:if>
<s:else>
	<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
<div class="container" id="catalog-landing-page" style="overflow: auto;" >
</s:else>

<!--ends -->
<!-- Hemantha 
		<div class="container">
		 -->
			<!-- BEGIN breadcrumb --> 
			<div id="catalog-header-breadcrumbs">
				<s:form name='narrowSearch' action='search' namespace='/catalog'>
				<div class="searchbox-form1">
					<div class="catalog-search-container">
					<input id="search_searchTerm" value="Search Within Results..." name="searchTerm"
					tabindex="1002" type="text" onkeydown="javascript:validateDQuote(event)" onclick="javascript:context_newSearch_searchTerm_onclick(this)" /> 
					<button type="submit" class="searchButton" tabindex="1003" title="Search" onclick="javascript:setDefaultSearchText();"></button> 
						<s:set name="checkedval1" value="%{getWCContext().getWCAttribute('StockedCheckbox')}"/>
						<s:hidden id="stockedItem" name="stockedItem" value="%{#checkedval1}"/> 
						<!-- START wctheme.submit.ftl (use simple) --> 
						<!-- <button type="submit" id="search_0" value="Submit" class="searchButton"></button> --> 
						<!-- END submit.ftl --> 
					</div>
				</div>
				</s:form>
				<div id="breadcrumbs-list-name">
					<div id="breadcrumb-my-selection" style="margin-left:11px; padding-right: 4px; text-align: left;">My Selection</div>
					<s:url value='/xpedx/images/icons/12x12_charcoal_x.png' id='rbtn'   /> <!--  title="Search"  -->
					<span class="breadcrumbs-inner"><span class="breadcrumb-inactive">
					<xpedx:breadcrumbDisplay displayRootName='Catalog' breadcrumbSeparator=' / ' 
					removable='true' removeIcon='#rbtn' startTabIndex='2' />
					</span></span>
				</div>
				<div class="clearall">&nbsp;</div>
			</div>
			<!-- END breadcrumb --> 
			
			<s:include value='XPEDXNarrowBy.jsp'/>
			
			<!-- begin mid column -->
			<div id="catalog-landing">
			<s:set name='storefrontId' value="wCContext.storefrontId" />
			<s:set name='ad_keyword' value='' />
			 <s:set name="categoryDepth" value="categoryDepth"/>
			 <s:if test="#categoryDepth==1">
				<s:set name='currentCat' value='XMLUtils.getChildElement(#catDoc, "CurrentCategory")' />
			<div>
			<!-- 
			<br>
			<span id="catalog-expanded-label"><s:property value='#currentCat.getAttribute("ShortDescription")'/></span>
			 -->
			<!-- Hemantha -->
			<fieldset><legend><s:property value='#currentCat.getAttribute("ShortDescription")'/></legend> 
        	<table class="catalog-row-five" width="100%">
        	<!-- AssetBean is of Type com.sterlingcommerce.xpedx.webchannel.catalog.XPEDXCatalogCategoryImageBean -->
        	<tbody>
        	<s:set name='catRowCount' value="%{0}"/>
        	<s:iterator id='cat' value='XMLUtils.getElements(#catDoc, "//CategoryList/Category")' status='catIndex'>
        		<s:set name='categoryName' value='#cat.getAttribute("ShortDescription")'/>
        		<s:set name='catRowCount' value="#catRowCount+1" />
			    <s:set name='categoryPath' value='#cat.getAttribute("CategoryPath")'/>
						<s:url id='topCatURL' namespace='/catalog' action='navigate.action'>
								<s:param name='path' value='#categoryPath'/>
								<s:param name='cname' value='#categoryName'/>
							</s:url>
							<s:set name='assetBeanList' value='categoryAssetMap.get(#categoryName)'/>
							<s:set name='assetBean' value='#assetBeanList.get(0)'/>
						
							<s:if test="#catIndex.index % 5 == 0">
								<tr> 
							</s:if>	
								<td valign="top" > 
									<s:a href='%{#topCatURL}'> 
<!--										<img src="${assetBean.imageUrl}" width="91" height="95" alt="<s:property value='#assetBean.cname'/>" border="0" title="<s:property value='#assetBean.cname'/>" />  http://localhost:7002/swc/xpedx/static/xpedx/images/catalog/products/placeholder-91x95.png -->
										<img src="${assetBean.imageUrl}" width="91" height="95" alt="<s:property value='#assetBean.cname'/>" border="0" title="<s:property value='#assetBean.cname'/>" /> 
									</s:a> 
									<p> <s:a href='%{#topCatURL}'><s:property value='#assetBean.cname'/></s:a></p>
								</td> 
							<s:if test="#catIndex.index % 5 == 4 || #catIndex.last">
								<s:if test=" #catIndex.last">
									<s:if test="#catRowCount % 5 !=0">
										<td colspan="${5-(catRowCount % 5)}">&nbsp;</td>
									</s:if>
								</s:if>	
								</tr>	
							</s:if>						               
													
        	</s:iterator>
        	</tbody>
        	</table>
			<!-- Hemantha -->
		</fieldset>
			</div>
			<!-- Ad Juggler Tag Starts - Cat1 Landing Page -->		
			<%-- <s:set name='ad_keyword' value='#currentCat.getAttribute("ShortDescription")' />
			
			<s:if test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT}' >
					<s:set name="aj_adspot" value="115162" />
			</s:if>
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CANADA_STORE_FRONT}' >
					<s:set name="aj_adspot" value="118208" />
			</s:elseif>
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@BULKLEY_DUNTON_STORE_FRONT}' >
					<s:set name="aj_adspot" value="118193" />
			</s:elseif >
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_STORE_FRONT}' >
					<s:set name="aj_adspot" value="115712" />
			</s:elseif>
			<s:else>
					<s:set name="aj_adspot" value="115712" />
			</s:else> --%>
			<!-- Ad Juggler Tag Ends -->
			 </s:if>
			 <s:else>
			 <div>
		   	 <s:iterator id='cat' value='XMLUtils.getElements(#catDoc, "//CategoryList/Category")' status='catIndex'>
        	 <s:set name='categoryName' value='#cat.getAttribute("ShortDescription")'/>
			 <s:set name='categoryPath' value='#cat.getAttribute("CategoryPath")'/>
		     <s:url id='topCatURL' namespace='/catalog' action='navigate.action'>
				<s:param name='path' value='#categoryPath'/>
				<s:param name='cname' value='#categoryName'/>
			 </s:url>
         	<fieldset><legend><s:property value='#categoryName'/></legend> 
					<table class="catalog-row-five" width="100%">
						<tbody>
								<s:set name='assetBeanList' value='categoryAssetMap.get(#categoryName)'/>
								<!-- AssetBean is of Type com.sterlingcommerce.xpedx.webchannel.catalog.XPEDXCatalogCategoryImageBean -->
								 <s:set name='catRowCount' value="%{0}"/>
								<s:iterator value='#assetBeanList' id='assetBean' status='subCatIndex'>
								 <s:set name='catRowCount' value="#catRowCount+1" />
									<s:if test="#subCatIndex.index % 5 == 0">
										<tr>
									</s:if>
									<!-- -FXD-1 Number of Cat1 Items to display -->
										<s:if test="#subCatIndex.index <= 4">
											<s:url id='catURL' namespace='/catalog' action='navigate.action'>
												<s:param name='path' value='#assetBean.categoryPath'/>
												<s:param name='cname' value='#assetBean.cname'/>
											</s:url>
											<td valign="top">  
												<s:a href='%{#catURL}'> 
													<!--  <img src="${assetBean.imageUrl}" width="91" height="95" alt="<s:property value='#assetBean.cname'/>" border="0" title="<s:property value='#assetBean.cname'/>" />  http://localhost:7002/swc/xpedx/static/xpedx/images/catalog/products/placeholder-91x95.png -->
													<img src="${assetBean.imageUrl}" width="91" height="95" alt="<s:property value='#assetBean.cname'/>" border="0" title="<s:property value='#assetBean.cname'/>" />
													
												</s:a> 
												<p> <s:a href='%{#catURL}'><s:property value='#assetBean.cname'/></s:a></p>
											</td>												
										</s:if>
										<s:if test="#subCatIndex.index % 5 == 4 || #subCatIndex.last ||  #subCatIndex.index == 9">
											<s:if test=" #subCatIndex.last">
												<s:if test="#catRowCount % 5 !=0">
													<td colspan="${5-(catRowCount % 5)}">&nbsp;</td>
												</s:if>
											</s:if>	
										</tr>
										</s:if>
								</s:iterator>
								<div class="clearall"></div>
								<s:if test='#guestUser'>
									<br /><br/>
								</s:if>
								</tbody>
						</table>                     
						<div class="cat-view-all"><s:a href='%{#topCatURL}'>View All <s:property value='#categoryName'/></s:a></div>
					</fieldset>
					</s:iterator>
					</div>
			<!-- Ad Juggler Tag Start - Catalog Landing Page 160X600 -->		
			 <s:if test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT}' >
					<s:set name="aj_adspot" value="118185" />
			</s:if>
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CANADA_STORE_FRONT}' >
					<s:set name="aj_adspot" value="118207" />
			</s:elseif>
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@BULKLEY_DUNTON_STORE_FRONT}' >
					<s:set name="aj_adspot" value="118192" />
			</s:elseif >
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_STORE_FRONT}' >
					<s:set name="aj_adspot" value="115722" />
			</s:elseif>
			<s:else>
					<s:set name="aj_adspot" value="115722" />
			</s:else >			
			<!-- Ad Juggler Tag Ends -->

					
			</s:else>
			</div>

			<!-- end mid column -->
			<!-- aj_server : https://rotator.hadj7.adjuggler.net:443/servlet/ajrotator/ -->
			
				<div id="right-col-int" class="cat-landing" style="margin-top:0px;"> 
				 <div align="left" style="padding-right: 62px;padding-bottom:2px">
				<div class="ad-float smallBody" style="float: none;" > <img height="4" width="7" style="margin-top: 5px; padding-right: 5px;" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/mil/ad-arrow<s:property value='#wcUtil.xpedxBuildKey' />.gif" alt="" class="float-left" /> advertisement</div>
				
			</div>
				<!-- Added for EB-1712 Display a Saalfeld advertisement image on Catalog Home page  Starts -->
						<s:set name='storefrontId' value="wCContext.storefrontId" />
						 <%-- <div align="center" style="padding-right: 15px;padding-top: 5.3px;">
						 <s:if test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_STORE_FRONT.equals(#storefrontId)}'>
						 <img width="160" height="600" border="0" alt="" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/ad_placeholders/xpedx_160x600r<s:property value='#wcUtil.xpedxBuildKey' />.jpg"/></div>
						 </s:if>
						<s:elseif test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT.equals(#storefrontId)}'>
						<img width="160" height="600" border="0" alt="" src="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/images/SD_160x600<s:property value='#wcUtil.xpedxBuildKey' />.jpg"/></div>
						</s:elseif> --%>
				<!-- EB-1549 END -->
				
				<!-- Ad Juggler Tag Starts  -->
				<!-- jira 2890 - TEST was appended to url which is wrong, it should be prepended to aj_kw keyword for dev and staging -->
				<s:set name="prependTestString" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getAdJugglerKeywordPrefix()" />
				<s:set name="sanitizedCategoryName" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@sanitizeAJKeywords(#ad_keyword)"/>
				<s:if test="#categoryDepth==1">
					<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot='<s:property value="%{#aj_adspot}" />'; aj_page = '0'; aj_dim ='114897'; aj_ch = ''; aj_ct = ''; aj_kw='<s:property value="%{#prependTestString}" /><s:property value="%{#sanitizedCategoryName}" />';
				aj_pv = true; aj_click = ''; </script>
				<script type="text/javascript" language="JavaScript" src="https://img.hadj7.adjuggler.net/banners/ajtg.js"></script>
				</s:if>
				<s:else>
				<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot='<s:property value="%{#aj_adspot}" />';  aj_page = '0'; aj_dim ='114897'; aj_ch = ''; aj_ct = ''; aj_kw = '<%=session.getAttribute("CUST_PREF_CATEGORY_DESC")%>';
				aj_pv = true; aj_click = '';
				</script>
				<script type="text/javascript" language="JavaScript" src="https://img.hadj7.adjuggler.net/banners/ajtg.js"></script>
				</s:else>
				<!-- Ad Juggler Tag Ends -->  
			</div>
			
			
		<!-- Hemantha  
		</div>
		-->
		<!-- end main  -->
	
	</div>
	
	</swc:breadcrumbScope>
<!-- </div>-->

 
<!-- end container  -->
<!-- FOOTER GOES HERE -->


<!--<script type="text/javascript" src="../xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="../xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="../xpedx/js/global/validation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="../xpedx/js/global/dojo<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="../xpedx/js/global/dojoRequire<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="../xpedx/js/theme/theme-1/theme<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="../xpedx/js/catalog/catalogExt<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
<script type="text/javascript" src="../xpedx/js/swc<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
-->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- carousel scripts js   -->


<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!--<script type="text/javascript" src="../xpedx/js/jquery.dropdownPlain<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="../xpedx/css/modals/checkboxtree/jquery.checkboxtree.js"></script>
<script type="text/javascript" src="../xpedx/js/quick-add/quick-add<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

--><script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!--<script type="text/javascript" src="../xpedx/js/DD_roundies_0.0.2a-min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="../xpedx/js/pseudofocus<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>-->
<!--<script type="text/javascript" src="../xpedx/js/global-xpedx-functions<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
-->
<!--<script type="text/javascript" src="../xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
-->
<!--EB-519-->
<s:action name="xpedxFooter" executeResult="true" namespace="/common" />

<script type="text/javascript">
	$(document).ready(function() {
		$(document).pngFix();
	});


	//Added for removing double quote from the search srting. Jira # 2415
	var myMask;
	 function validateDQuote(e){
	   	  if (e.keyCode == 13) {  
	   	  //added for jira 3974
		var waitMsg = Ext.Msg.wait("Processing...");
		myMask = new Ext.LoadMask(Ext.getBody(), {msg:waitMsg});
		myMask.show();
	   	 	var searchValue = document.getElementById("search_searchTerm").value;
	   	 	while(searchValue.indexOf("\"")!= -1){
	       	 	 searchValue = searchValue.replace("\"", "");    	  
	   	 	}
	   	 	Ext.fly('search_searchTerm').dom.value=searchValue;
	   	}
	   }
		
</script>

</body>
</html>