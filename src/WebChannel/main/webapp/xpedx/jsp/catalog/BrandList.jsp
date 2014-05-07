<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<%@ taglib prefix="xpedx" uri="/WEB-INF/xpedx.tld"%>

<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta content='IE=8' http-equiv='X-UA-Compatible' />
	
	<%
		request.setAttribute("isMergedCSSJS","true");
	%>
	
	<s:set name='isGuestUser' value="wCContext.guestUser" />
	<s:if test="#isGuestUser != true">
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<!--[if IE]> 
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-ie-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" /> 
	<![endif]--> 
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/CATALOG<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<!--[if IE]>
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<![endif]-->	
	</s:if>

	<s:else>
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/CATALOG<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<!--[if IE]>
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<![endif]-->
	</s:else>
	<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
	
	<!-- sterling 9.0 base  do not edit  javascript move all functions to js/global-xpedx-functions.js -->
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-jquery-headder<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

	<title><s:property value="wCContext.storefrontId" /> - Catalog - Brands</title>

	<!-- Web Trends tag start -->
	<meta name="WT.ti" content="<s:property value="wCContext.storefrontId" /> - Catalog - Brands"/>
	<!-- Web Trends tag end -->

</head>

<body class="  ext-gecko ext-gecko3">
	<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
	<s:bean name='com.sterlingcommerce.webchannel.catalog.utils.CatalogUtilBean' id='catUtil' />
	<s:url action='navigate.action' namespace='/catalog' id='myUrl'/>
	<s:set name='_action' value='[0]' />
	<s:set name='myParam' value='{"searchTerm", "", "cname"}'/>
<%-- 	<s:set name='appFlowContext' value='#session.FlowContext'/> --%>
<%-- 	<s:set name='isFlowInContext' value='#util.isFlowInContext(#appFlowContext)'/> --%>
<%-- 	<s:set name='isGuestUser' value="wCContext.guestUser" /> --%>
	<s:set name='guestUser' value="#_action.getWCContext().isGuestUser()" />
	<s:set name='cat1name' value="cat1name"/>
	<s:set name='cat2name' value="cat2name"/>
	<s:set name='bcs' value="bcs"/>
	<s:set name='brandMap' value="brands"/>
	<s:set name="keys" value="#brandMap.keySet()"/>

	<div id="main" class='%{ #guestUser ? "" : "anon-pages" }'>

	<s:if test='!#guestUser'>  
		<s:action name="xpedxHeader" executeResult="true" namespace="/common" >
			<s:param name='shipToBanner' value="%{'true'}" />
		</s:action>
	</s:if>
	<s:else>
		<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
	</s:else>

	<div class="container container-pad">

		<div class="page-title">Brands for <s:property value='%{#cat1name + " / " + #cat2name}' /></div>
		<div class="alphabet">
			<p>
				<%-- row of letters across the top --%>
				<%-- (first entry key works as "#" but should this be "numbers" instead though displayed as "#" ?) --%>
				<s:iterator id="key" value="keys">
					<s:if test="#brandMap.get(#key).size()>0">
						<s:a href='%{ "#" + #key }' cssClass="alpha-letter">
							<s:property value="#key" />
						</s:a>
					</s:if>
					<s:else>
						<span class="alpha-letter-inactive"><s:property value="#key" /></span>
					</s:else>
				</s:iterator>
			</p>
		</div>  <%-----close alphabets-----%>

		<s:iterator id="key" value="keys">
			<div class="alpha-list">
				<h2>
					<s:property value="#key" /><a name='<s:property value="#key" />'></a>
				</h2>
				<div class="brand-wrap">
					<ul>

						<%-- Put brands that start with this letter in 4 columns, each a link --%>
						<s:set name="letterBrands" value="%{#brandMap.get(#key)}"></s:set>
						<s:set name="numRows" value="%{ #_action.getNumRows(#letterBrands.size()) }"></s:set>
						<s:set name="column" value="0"></s:set>
						<s:iterator id="brand" value="letterBrands" status="stat">

							<s:if test="#stat.index % #numRows == 0">
								<s:set name="column" value="%{#column+1}"></s:set>
								<div class='col-<s:property value="#column"/>'>
							</s:if>

							<li>
							<s:url id='brandURL' namespace='/catalog' action='filter.action'>
								<s:param name='indexField' value='"ItemAttribute.xpedx.FF_1182"' />
								<s:param name='facet' value='#brand' />
								<s:param name='cname' value='#brand' />
								<s:param name='filterDesc' value='"Brand"' />
								<s:param name='_bcs_' value='#bcs' />
							</s:url>
							<a href='<s:property value="#brandURL" escape="false"/>'><s:property value='%{#brand}' /></a>
							</li>

							<s:if test="((#stat.index+1) % #numRows == 0) || (#stat.index+1 == #letterBrands.size())">
								</div>
							</s:if>
						</s:iterator>
						<%-- Need to still put column divs even if no entries for this letter --%>
						<s:if test="#column<2"> <div class="col-2"></div> </s:if>
						<s:if test="#column<3"> <div class="col-3"></div> </s:if>
						<s:if test="#column<4"> <div class="col-4"></div> </s:if>
		                <div class="clearfix"></div>
					</ul>

				</div>
			</div>
		</s:iterator>


	</div> <%-----close container-----%> 
	 
	</div>

	<s:action name="xpedxFooter" executeResult="true" namespace="/common" />

</body>
</html>