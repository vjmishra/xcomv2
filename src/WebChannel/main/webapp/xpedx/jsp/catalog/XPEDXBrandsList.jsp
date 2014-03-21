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
	
	<!-- TODO does this need to vary depending on whether guest? -->
	<s:set name='isGuestUser' value="wCContext.guestUser" />
	<s:if test="#isGuestUser != true">
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<%-- 	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" /> --%>
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

	<title><s:property value="wCContext.storefrontId" /> - <s:text name='catalog.title' /></title>

	<s:set name='_action' value='[0]'/>
</head>

<body class="  ext-gecko ext-gecko3">
	<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
	<s:bean name='com.sterlingcommerce.webchannel.catalog.utils.CatalogUtilBean' id='catUtil' />
	<s:url action='navigate.action' namespace='/catalog' id='myUrl'/>
	<s:set name='myParam' value='{"searchTerm", "", "cname"}'/>
<%-- 	<s:set name='appFlowContext' value='#session.FlowContext'/> --%>
<%-- 	<s:set name='isFlowInContext' value='#util.isFlowInContext(#appFlowContext)'/> --%>
<%-- 	<s:set name='isGuestUser' value="wCContext.guestUser" /> --%>
	<s:set name='guestUser' value="#_action.getWCContext().isGuestUser()" />
	<s:set name='brands' value="brands"/>

	<!--web trends start -->
	<!-- TODO -->

	<div id="main" class='%{ #guestUser ? "" : "anon-pages" }'>

		<!-- TODO should this vary depending on whether guest? -->
		<s:action name="xpedxHeader" executeResult="true" namespace="/common" >
			<s:param name='shipToBanner' value="%{'true'}" />
		</s:action> 


	<!-- *** NEW STUFF *** -->
	<!-- 	Different container div attrs for guest? -->
	<div class="container container-pad">

		<div class="page-title">TODO Breadcrumb here instead of this size: <s:property value='%{#brands.size()}' /></div>
		<div class="alphabet">
			<p>
				<!-- TODO loop through letters; set class="alpha-letter-inactive" if no entries  -->
				<a href="#numbers" class="alpha-letter">#</a> <a href="#A" class="alpha-letter">A</a> <a href="#B" class="alpha-letter">B</a>
				<a href="#C" class="alpha-letter">C</a> <a href="#D" class="alpha-letter">D</a> <a href="#E" class="alpha-letter">E</a>
				<a href="#F" class="alpha-letter">F</a> <a href="#G" class="alpha-letter">G</a> <a href="#H" class="alpha-letter">H</a>
				<a href="#I" class="alpha-letter">I</a> <a href="#J" class="alpha-letter">J</a> <a href="#K" class="alpha-letter">K</a>
				<a href="#L" class="alpha-letter">L</a> <a href="#M" class="alpha-letter">M</a> <a href="#N" class="alpha-letter">N</a>
				<a href="#O" class="alpha-letter">O</a> <a href="#P" class="alpha-letter">P</a>
				<span class="alpha-letter-inactive">Q</span>
				<a href="#R" class="alpha-letter">R</a> <a href="#S" class="alpha-letter">S</a> <a href="#T" class="alpha-letter">T</a>
				<a href="#U" class="alpha-letter">U</a> <a href="#V" class="alpha-letter">V</a> <a href="#W" class="alpha-letter">W</a>
				<span class="alpha-letter-inactive">X</span>
				<span class="alpha-letter-inactive">Y</span>
				<span class="alpha-letter-inactive">Z</span>
			</p>
		</div>
		<!-----close alphabets----->

		<!-- TODO loop over letters/#, putting letter in h2 then brands in div,ul li for each, wrapped into 4 cols
		          then make href go to items of that brand and CAT2 -->
		<div class="alpha-list">
			<h2>
				#<a name="numbers"></a>
			</h2>
			<div class="brand-wrap">
				<ul>
					<div class="col-1">
						<li><a href="#">3M</a></li>
					</div>
					<div class="col-2"></div>
					<div class="col-3"></div>
					<div class="col-4"></div>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>
		<div class="alpha-list">
			<h2>
				A<a name="A"></a>
			</h2>
			<div class="brand-wrap">
				<ul>
					<div class="col-1">
						<li><a href="#">Accumix</a></li>
						<li><a href="#">All</a></li>
					</div>
					<div class="col-2">
						<li><a href="#">Alpha-HP</a></li>
						<li><a href="#">Amplify</a></li>
					</div>
					<div class="col-3">
						<li><a href="#">Aquaria</a></li>
						<li><a href="#">AutoFresh </a></li>
					</div>
					<div class="col-4">
						<li><a href="#">AutoJanitor </a></li>
					</div>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>

		<div class="alpha-list">
			<h2>
				B<a name="B"></a>
			</h2>
			<div class="brand-wrap">
				<ul>
					<div class="col-1">
						<li><a href="#">Blue Skies II</a></li>
					</div>
					<div class="col-2">
						<li><a href="#">Bravo</a></li>
					</div>
					<div class="col-3"></div>
					<div class="col-4"></div>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>
		<div class="alpha-list">
			<h2>
				C<a name="C"></a>
			</h2>
			<div class="brand-wrap">
				<ul>
					<div class="col-1">
						<li><a href="#">CLAX Mild Forte</a></li>
						<li><a href="#">Carefree</a></li>
					</div>
					<div class="col-2">
						<li><a href="#">Complete</a></li>
						<li><a href="#">Conq-R-Dust</a></li>
					</div>
					<div class="col-3">
						<li><a href="#">Control Plus</a></li>
						<li><a href="#">Crew</a></li>
					</div>
					<div class="col-4">
						<li><a href="#">Crew Super Blue</a></li>
					</div>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>
		<div class="alpha-list">
			<h2>
				D<a name="D"></a>
			</h2>
			<div class="brand-wrap">
				<ul>
					<div class="col-1">
						<li><a href="#">Deep Gloss</a></li>
					</div>
					<div class="col-2">
						<li><a href="#">Diversey</a></li>
					</div>
					<div class="col-3">
						<li><a href="#">Divosan Forte</a></li>
					</div>
					<div class="col-4"></div>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>
		<div class="alpha-list">
			<h2>
				E<a name="E"></a>
			</h2>
			<div class="brand-wrap">
				<ul>
					<div class="col-1">
						<li><a href="#">Easy Paks</a></li>
						<li><a href="#">Easy Shine</a></li>
					</div>
					<div class="col-2">
						<li><a href="#">Emerel</a></li>
						<li><a href="#">End Bac II</a></li>
					</div>
					<div class="col-3">
						<li><a href="#">Envy</a></li>
					</div>
					<div class="col-4">
						<li><a href="#">Expose </a></li>
					</div>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>
		<div class="alpha-list">
			<h2>
				F<a name="F"></a>
			</h2>
			<div class="brand-wrap">
				<ul>
					<div class="col-1">
						<li><a href="#">Fantastik</a></li>
						<li><a href="#">Fantastik Scrubbing Bubbles</a></li>
					</div>
					<div class="col-2">
						<li><a href="#">Fastrip</a></li>
						<li><a href="#">Final Step</a></li>
					</div>
					<div class="col-3">
						<li><a href="#">Forward</a></li>
						<li><a href="#">Freedom</a></li>
					</div>
					<div class="col-4">
						<li><a href="#">FrescoMax</a></li>
					</div>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>
		<div class="alpha-list">
			<h2>
				G<a name="G"></a>
			</h2>
			<div class="brand-wrap">
				<ul>
					<div class="col-1">
						<li><a href="#">GP Forward</a></li>
					</div>
					<div class="col-2">
						<li><a href="#">Glance</a></li>
					</div>
					<div class="col-3">
						<li><a href="#">Good Sense</a></li>
					</div>
					<div class="col-4"></div>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>
		<div class="alpha-list">
			<h2>
				H<a name="H"></a>
			</h2>
			<div class="brand-wrap">
				<ul>
					<div class="col-1">
						<li><a href="#">High Mileage</a></li>
					</div>
					<div class="col-2">
						<li><a href="#">Hot Springs</a></li>
					</div>
					<div class="col-3">
						<li><a href="#">Hydrion </a></li>
					</div>
					<div class="col-4"></div>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>
		<div class="alpha-list">
			<h2>
				I<a name="I"></a>
			</h2>
			<div class="brand-wrap">
				<ul>
					<div class="col-1">
						<li><a href="#">Impermo</a></li>
					</div>
					<div class="col-2">
						<li><a href="#">Instapak</a></li>
					</div>
					<div class="col-3"></div>
					<div class="col-4"></div>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>
		<div class="alpha-list">
			<h2>
				J<a name="J"></a>
			</h2>
			<div class="brand-wrap">
				<ul>
					<div class="col-1">
						<li><a href="#">J-512</a></li>
						<li><a href="#">J-Fill</a></li>
						<li><a href="#">J-512</a></li>
					</div>
					<div class="col-2">
						<li><a href="#">J-Fill QuattroSelect</a></li>
						<li><a href="#">J-Works</a></li>
						<li><a href="#">J-Works Strata</a></li>
					</div>
					<div class="col-3">
						<li><a href="#">J-Works Tempest</a></li>
						<li><a href="#">Jiffy</a></li>
					</div>
					<div class="col-4">
						<li><a href="#">Jon-Stone</a></li>
						<li><a href="#">JonCrete</a></li>
					</div>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>
		<div class="alpha-list">
			<h2>
				K<a name="K"></a>
			</h2>
			<div class="brand-wrap">
				<ul>
					<div class="col-1">
						<li><a href="#">KimCare</a></li>
					</div>
					<div class="col-2">
						<li><a href="#">KimTech Science</a></li>
					</div>
					<div class="col-3"></div>
					<div class="col-4"></div>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>
		<div class="alpha-list">
			<h2>
				L<a name="L"></a>
			</h2>
			<div class="brand-wrap">
				<ul>
					<div class="col-1">
						<li><a href="#">LinoBASE</a></li>
					</div>
					<div class="col-2">
						<li><a href="#">LinoSAFE</a></li>
					</div>
					<div class="col-3">
						<li><a href="#">Liqu-A-Klor</a></li>
					</div>
					<div class="col-4"></div>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>
		<div class="alpha-list">
			<h2>
				M<a name="M"></a>
			</h2>
			<div class="brand-wrap">
				<ul>
					<div class="col-1">
						<li><a href="#">Microburst</a></li>
					</div>
					<div class="col-2">
						<li><a href="#">Morning Mist</a></li>
					</div>
					<div class="col-3">
						<li><a href="#">Multi</a></li>
					</div>
					<div class="col-4"></div>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>
		<div class="alpha-list">
			<h2>
				N<a name="N"></a>
			</h2>

			<div class="brand-wrap">
				<ul>
					<div class="col-1"></div>
					<div class="col-2"></div>
					<div class="col-3"></div>
					<div class="col-4"></div>
					<div class="clearfix"></div>
				</ul>
			</div>

		</div>

		<div class="alpha-list">
			<h2>
				O<a name="O"></a>
			</h2>
			<div class="brand-wrap">
				<ul>
					<div class="col-1">
						<li><a href="#">Oxivir</a></li>
					</div>
					<div class="col-2">
						<li><a href="#">Oxivir AHP</a></li>
					</div>
					<div class="col-3">
						<li><a href="#">Oxivir Five 16</a></li>
					</div>
					<div class="col-4"></div>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>
		<div class="alpha-list">
			<h2>
				P<a name="P"></a>
			</h2>
			<div class="brand-wrap">
				<ul>
					<div class="col-1">
						<li><a href="#">PERdiem</a></li>
						<li><a href="#">Plaza Plus</a></li>
					</div>
					<div class="col-2">
						<li><a href="#">Pledge</a></li>
						<li><a href="#">Premia</a></li>
					</div>
					<div class="col-3">
						<li><a href="#">Pro Strip</a></li>
						<li><a href="#">Profi</a></li>
					</div>
					<div class="col-4">
						<li><a href="#">Prominence</a></li>
					</div>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>
		<div class="alpha-list">
			<h2>
				Q<a name="Q"></a>
			</h2>
			<div class="brand-wrap">
				<ul>
					<div class="col-1"></div>
					<div class="col-2"></div>
					<div class="col-3"></div>
					<div class="col-4"></div>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>
		<div class="alpha-list">
			<h2>
				R<a name="R"></a>
			</h2>
			<div class="brand-wrap">
				<ul>
					<div class="col-1">
						<li><a href="#">RTD</a></li>
						<li><a href="#">Raindance</a></li>
					</div>
					<div class="col-2">
						<li><a href="#">Regency Professional</a></li>
						<li><a href="#">Revive </a></li>
					</div>
					<div class="col-3">
						<li><a href="#">Revive Plus</a></li>
					</div>
					<div class="col-4">
						<li><a href="#">Rubbermaid</a></li>
					</div>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>
		<div class="alpha-list">
			<h2>
				S<a name="S"></a>
			</h2>
			<div class="brand-wrap">
				<ul>
					<div class="col-1">
						<li><a href="#">SC Johnson</a></li>
						<li><a href="#">Sani-Sure</a></li>
						<li><a href="#">SaniSense</a></li>
						<li><a href="#">Scotchgard</a></li>
						<li><a href="#">Scott</a></li>
						<li><a href="#">Scrubbing Bubbles</a></li>
						<li><a href="#">SeBreeze</a></li>
						<li><a href="#">Sharpshooter</a></li>
					</div>
					<div class="col-2">
						<li><a href="#">Shine-Up </a></li>
						<li><a href="#">Shout</a></li>
						<li><a href="#">Signature</a></li>
						<li><a href="#">Snapback</a></li>
						<li><a href="#">Speedball</a></li>
						<li><a href="#">Speedball 2000</a></li>
						<li><a href="#">Spitfire</a></li>
						<li><a href="#">Stride</a></li>
					</div>
					<div class="col-3">
						<li><a href="#">Suma</a></li>
						<li><a href="#">Suma Alupak</a></li>
						<li><a href="#">Suma Armada</a></li>
						<li><a href="#">Suma Bio-Floor</a></li>
						<li><a href="#">Suma Break-up</a></li>
						<li><a href="#">Suma Eden</a></li>
						<li><a href="#">Suma Final Step</a></li>
						<li><a href="#">Suma Flow</a></li>
					</div>
					<div class="col-4">
						<li><a href="#">Suma Pan-Clean</a></li>
						<li><a href="#">Suma Pronounce</a></li>
						<li><a href="#">Suma Shine</a></li>
						<li><a href="#">Suma Silver-Safe</a></li>
						<li><a href="#">Suma Stop Slip</a></li>
						<li><a href="#">Suma Tray-Glide</a></li>
					</div>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>
		<div class="alpha-list">
			<h2>
				T<a name="T"></a>
			</h2>
			<div class="brand-wrap">
				<ul>
					<div class="col-1">
						<li><a href="#">T-Cell</a></li>
						<li><a href="#">TC</a></li>
						<li><a href="#">TC AutoClean</a></li>
					</div>
					<div class="col-2">
						<li><a href="#">Taski</a></li>
						<li><a href="#">Taski Ultra</a></li>
					</div>
					<div class="col-3">
						<li><a href="#">TimeSaver SR</a></li>
						<li><a href="#">Triad III</a></li>
					</div>
					<div class="col-4">
						<li><a href="#">TroubleShooter</a></li>
						<li><a href="#">Twist 'n Fill</a></li>
					</div>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>
		<div class="alpha-list">
			<h2>
				U<a name="U"></a>
			</h2>
			<div class="brand-wrap">
				<ul>
					<div class="col-1">
						<li><a href="#">UHS</a></li>
					</div>
					<div class="col-2"></div>
					<div class="col-3"></div>
					<div class="col-4"></div>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>
		<div class="alpha-list">
			<h2>
				V<a name="V"></a>
			</h2>
			<div class="brand-wrap">
				<ul>
					<div class="col-1">
						<li><a href="#">Vanish</a></li>
					</div>
					<div class="col-2">
						<li><a href="#">Vectra</a></li>
					</div>
					<div class="col-3">
						<li><a href="#">Virex</a></li>
					</div>
					<div class="col-4">
						<li><a href="#">Virex II</a></li>

					</div>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>
		<div class="alpha-list">
			<h2>
				W<a name="W"></a>
			</h2>
			<div class="brand-wrap">
				<ul>
					<div class="col-1">
						<li><a href="#">Wall Power</a></li>
					</div>
					<div class="col-2">
						<li><a href="#">Windex</a></li>
					</div>
					<div class="col-3">
						<li><a href="#">Wiwax</a></li>
					</div>
					<div class="col-4"></div>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>
		<div class="alpha-list">
			<h2>
				X<a name="X"></a>
			</h2>
			<div class="brand-wrap">
				<ul>
					<div class="col-1"></div>
					<div class="col-2"></div>
					<div class="col-3"></div>
					<div class="col-4"></div>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>
		<div class="alpha-list">
			<h2>
				Y<a name="Y"></a>
			</h2>
			<div class="brand-wrap">
				<ul>
					<div class="col-1"></div>
					<div class="col-2"></div>
					<div class="col-3"></div>
					<div class="col-4"></div>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>
		<div class="alpha-list">
			<h2>
				Z<a name="Z"></a>
			</h2>
			<div class="brand-wrap">
				<ul>
					<div class="col-1"></div>
					<div class="col-2"></div>
					<div class="col-3"></div>
					<div class="col-4"></div>
					<div class="clearfix"></div>
				</ul>
			</div>
		</div>
	</div>
	<!-----close container-----> 
	 
	</div>

	<!-- FOOTER GOES HERE -->
	<s:action name="xpedxFooter" executeResult="true" namespace="/common" />

	<!-- TODO add floating top/bottom buttons  -->

</body>
</html>