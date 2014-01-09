<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="swc" %>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>
<s:set name='isGuestUser' value="wCContext.guestUser" />
<s:set name='_action' value='[0]' />
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>

	<s:bean name='com.sterlingcommerce.framework.utils.SCXmlUtils' id='util' />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta content='IE=8' http-equiv='X-UA-Compatible' />       
	<%
  		request.setAttribute("isMergedCSSJS","true");
  	  %>
  	 <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
  	 <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" />
 
 	<!--[if IE]>
	<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/IE.css" />
	<![endif]-->
	<s:if test="#isGuestUser != true">
	    
	
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>		
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-jquery-headder<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
		
	    <!-- jQuery -->
	    <!-- Page Calls -->
		<!-- END head-calls.php -->
	</s:if>
	<s:else>
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-jquery-headder<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	</s:else>
	
	<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />


	
    <s:set name="storefront"  value='wCContext.storefrontId'/>
    <s:if test="#isGuestUser == true">
		<title><s:property value="%{#storefront}"></s:property></title>
		<!-- Webtrends Start -->
		<meta name="WT.ti" content="<s:property value='%{#storefront}'/>"  /> 
		 <!-- Webtrends End -->
	</s:if>
	<s:else>
		<title><s:property value="%{#storefront}"/></title>
		<!-- Webtrends Start -->
		<meta name="WT.ti" content="<s:property value='%{#storefront}'/>" /> 
		 <!-- Webtrends End -->
	</s:else>
	
</head>
<body class="ext-gecko ext-gecko3">
    <s:if test="#isGuestUser == true">
		<div id="main-container">
			<div id="main" class="anon-pages">
			
				<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
			 	<s:action name="login" executeResult="true" namespace="/common" />
		  </div>
		</div>
		
		<div id="browser-not-supported" style=" display:none">
		<br/>
				&nbsp;&nbsp;xpedx.com is designed to work in the latest versions of Internet Explorer,<br/>
				&nbsp;&nbsp;Firefox, Safari. For more information please review the help document. <br/>
				&nbsp;&nbsp;Upgrading your browser is quick and easy, click here to get the latest versions <br/>
				&nbsp;&nbsp;(note: Safari can be upgraded via the Apple App Store): <br/><br/><br/><br/>
				<table width="100%">
					<tr>
						<td valign="top" style="line-height: 20px;" width="45%">
							<a href='http://windows.microsoft.com/en-US/internet-explorer/download-ie'>
							<img src='<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/Internet_Explorer_7_Logo<s:property value='#wcUtil.xpedxBuildKey' />.png'  style="position: relative; text-align:right; line-height: 20px;line-height: 20px;padding-left:150px;"  />
							</a>
						</td>
						<td valign="top" style="line-height: 20px;" width="10%"></td>
						<td valign="top" style="line-height: 20px;" width="45%">
							<a href='http://www.mozilla.org/en-US/'>
							<img src='<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/mozilla_firefox<s:property value='#wcUtil.xpedxBuildKey' />.jpg' style="position: relative;text-align:left; line-height: 20px;padding-right:150px;"/>
							</a>
						</td>
							
					</tr>
					<tr>
						<td colspan="3">&nbsp;&nbsp;</td>
					</tr>
					<tr>
						<td valign="top" width="46%">&nbsp;&nbsp;</td>
						<td style="float:center" valign="top" width="8%"><a style="float:center;" href="#" onclick='javascript:browserNotSupportedWin.close();'
								class="green-ui-btn"><span>OK</span></a>
						</td>
						<td width="46%">&nbsp;&nbsp;</td>
					</tr>
				</table>
		</div>
		<script type="text/javascript">
		var browserNotSupportedWin = new Ext.Window({
			autoScroll: false,
		    closeAction: 'hide',
		    cloaseable: false,
		    contentEl: 'browser-not-supported',
		    hidden: true,
		    id: 'browser-not-supportedBox',
		    modal: true,
		    width: 450,
		    height: 250,
		    resizable   : false,
		    draggable   : false,
		    closable    : false,
		    shadow: 'drop',
		    baseCls: 'swc-ext',
		    scrolling : 'no',
		    shadowOffset: 10,
		    border: 10,
		    bodyStyle : 'background-color: white'
		  	}); 	

		function browserSupport(){

		// Supported browser versions
		var VER_IE = "<s:property value='%{#wcUtil.getBrowserVersion("version.ie")}'/>";
		var VER_FIREFOX = "<s:property value='%{#wcUtil.getBrowserVersion("version.firefox")}'/>";
		var VER_SAFARI = "<s:property value='%{#wcUtil.getBrowserVersion("version.safari")}'/>";   // may be x.y
		//var VER_CHROME = 10;  // may be x.y.z

		// IE: "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR...; MS-RTC LM 8)"
		// IE11 is different: "Mozilla/5.0 (Windows NT 6.3; Trident/7.0; rv:11.0) like Gecko"
		if (/MSIE (\d+\.\d+);/.test(navigator.userAgent)){ //test for MSIE x.x;
		   var ieversion=new Number(RegExp.$1); // capture x.x portion and store as a number
			 if (ieversion < VER_IE)
			 {
				 // need? checking compatibility mode?
				 if(document.documentMode < VER_IE)
				 {
					 warnBrowserVersion();
				 }
			 }
				 
		}

		// Firefox: "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0"
		else if (/Firefox[\/\s](\d+\.\d+)/.test(navigator.userAgent)){ //test for Firefox/x.x or Firefox x.x
			var ffversion=new Number(RegExp.$1);
			if(ffversion < VER_FIREFOX)
			{
				 warnBrowserVersion();
			}
		 }

		// Safari: "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2"
		// win:"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2"
		else if (/Version\/(\d+\.\d+).*Safari/.test(navigator.userAgent)){
			var safariversion = new Number(RegExp.$1);// new Number(useragent.substr(useragent.lastIndexOf('Safari/') + 7, 6));
			if(safariversion < VER_SAFARI)
			{
				 warnBrowserVersion();
			}
		 }
		
		// Chrome is kind of supported and autoupdates to latest version so don't check
		// ex: "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.41 Safari/537.36"
// 		else if (/Chrome[\/\s](\d+\.\d+)/.test(navigator.userAgent)){
// 			var chromeversion=new Number(RegExp.$1);
// 			if(chromeversion < VER_CHROME)
// 			{
// 				 warnBrowserVersion();
// 			}
// 		}

		// Unsupported browsers
		else if ((/Netscape[\/\s](\d+\.\d+)/.test(navigator.userAgent)) ||
			(/Opera[\/\s](\d+\.\d+)/.test(navigator.userAgent)))
		{
			 warnBrowserVersion();
		}

		}
		
		function warnBrowserVersion() {
			 document.getElementById("browser-not-supported").style.display = "block";
			 browserNotSupportedWin.show();
		}
		
		</script>
<script type="text/javascript">
Ext.onReady(function(){		
		browserSupport();
});
		</script>	
</s:if>
<!-- RUgrani BEGIN: Logged in User Home page -->
<s:else>
	
	<div id="main-container">
	<div id="main">

		<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
		<div class="container">
		<div id="homepage-image-rotation">
		
			<%-- 			
			<div class="slideshow">
				<s:action name="xpedxDynamicPromotions" executeResult="true" namespace="/common" >
					<s:param name="callerPage">home</s:param>
				</s:action>
			</div> 
			--%>
			
			<div class="slideshow">
						<s:action name="xpedxDynamicPromotionsAction" executeResult="true" namespace="/common" >
							<s:param name="callerPage">HomePage</s:param>
						</s:action>
			</div>
			<s:if test="#request['imageCounter'] > 1" >
				<div id="home-image-rotation-nav">
					<div class="img-navi-left"></div>
					<div id="home-image-rotation-nav-inner"></div>
					<div class="img-navi-right"></div>
				</div>
			</s:if>
			
		</div>
		
		<div id="homepage-ad-area">
			<div class="ad-label"><img height="4" width="7" style="margin-top: 5px; padding-right: 5px;" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/mil/ad-arrow<s:property value='#wcUtil.xpedxBuildKey' />.gif" alt="" class="float-left" /> advertisement</div>
			<!-- Added for EB-1549 Display a Saalfeld advertisement image on the Home page  Starts -->
				<s:set name='storefrontId' value="wCContext.storefrontId" />
				<%-- <s:if test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_STORE_FRONT.equals(#storefrontId)}'>
				<img width="300" height="250" border="0" alt="" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/ad_placeholders/xpedx_300x250r<s:property value='#wcUtil.xpedxBuildKey' />.jpg"/>
				</s:if> --%>
				<s:if test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT.equals(#storefrontId)}'>			
				<img width="300" height="250" border="0" alt="" src="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/images/SD3_300x250<s:property value='#wcUtil.xpedxBuildKey' />.jpg"/>
				</s:if> 
			<!-- EB-1549 END -->
				<s:set name='storefrontId' value="wCContext.storefrontId" />
			
			<!-- Ad Juggler Tag Starts -->
			
		

			<!-- aj_server : https://rotator.hadj7.adjuggler.net:443/servlet/ajrotator/ -->
			
			 <s:if test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT}' >
				<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118161'; aj_page = '0'; aj_dim ='114889'; aj_ch = ''; aj_ct = ''; aj_kw = '<%=session.getAttribute("CUST_PREF_CATEGORY_DESC")%>';
				aj_pv = true; aj_click = '';
				</script>
			</s:if> 
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CANADA_STORE_FRONT}' >
								<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118200'; aj_page = '0'; aj_dim ='114889'; aj_ch = ''; aj_ct = ''; aj_kw = '<%=session.getAttribute("CUST_PREF_CATEGORY_DESC")%>';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif>
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@BULKLEY_DUNTON_STORE_FRONT}' >
									<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '118188'; aj_page = '0'; aj_dim ='114889'; aj_ch = ''; aj_ct = ''; aj_kw = '<%=session.getAttribute("CUST_PREF_CATEGORY_DESC")%>';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif >
			<s:elseif test='%{#storefrontId == @com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_STORE_FRONT}' >
				<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '115702'; aj_page = '0'; aj_dim ='114889'; aj_ch = ''; aj_ct = ''; aj_kw = '<%=session.getAttribute("CUST_PREF_CATEGORY_DESC")%>';
				aj_pv = true; aj_click = '';
				</script>
			</s:elseif>
			<s:else >
				<script type="text/javascript" language="JavaScript">
				aj_server = '<%=session.getAttribute("AJ_SERVER_URL_KEY")%>'; aj_tagver = '1.0';
				aj_zone = 'ipaper'; aj_adspot = '115702'; aj_page = '0'; aj_dim ='114889'; aj_ch = ''; aj_ct = ''; aj_kw = '<%=session.getAttribute("CUST_PREF_CATEGORY_DESC")%>';
				aj_pv = true; aj_click = '';
				</script>
			</s:else>			
			<!-- Ad Juggler Tag Ends -->
			<script type="text/javascript" language="JavaScript" src="https://img.hadj7.adjuggler.net/banners/ajtg.js"></script>  
		</div>
		
		<div class="clearall">&nbsp;</div>
    
	    <table id="homepage-table">
			<tr>
			    <s:action name="xpedxCustomerPrefCategory" executeResult="true" namespace="/profile/user">
			        <s:param name="assetType">CATEGORY_IMAGE</s:param>
			    </s:action>
			</tr>
			<tr>
				<td> 
				<%--
					<s:action name="xpedxAnnouncements" executeResult="true" namespace="/home" />
				 --%>
					<div id="xpedxAnnouncementsDiv"></div>
					 <s:url id='xpedxAnnouncementsURL'  namespace='/home'  action='xpedxAnnouncements.action' ></s:url>
					<script>
				    	Ext.onReady(function(){
			    		   	var url = "<s:property value='#xpedxAnnouncementsURL'/>";
			    		   	lodDivAsAjax(url,"xpedxAnnouncementsDiv");
						});
					</script>   
						
				</td>
				<td>
				<%--
					<s:action name="xpedxCustomerAnnouncements" executeResult="true" namespace="/home" />
				--%>
					<div id="xpedxCustomerAnnouncementsDiv"></div>
					 <s:url id='xpedxCustomerAnnouncementsURL'  namespace='/home'  action='xpedxCustomerAnnouncements.action' ></s:url>
					<script>
				    	Ext.onReady(function(){
			    		   	var url = "<s:property value='#xpedxCustomerAnnouncementsURL'/>";
			    		   	lodDivAsAjax(url,"xpedxCustomerAnnouncementsDiv");
						});
					</script>  
				</td>
				<td>	
					<s:set name='loggedInUserCustomerID' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getLoggedInCustomerFromSession(wCContext)'/>
					<s:set name="loggedInUserOrgCode"  value='wCContext.storefrontId'/>
					<h2>Quick Links</h2>
					<div class="news-scrollable">
						<ul class = "news-links">
						   <%-- <s:action name="getCustomerQuickLinks" executeResult="true" namespace="/profile/org" /> --%>
						    <s:url id='getCustomerQuickLinksURL'  namespace='/profile/org'  action='getCustomerQuickLinks.action' ></s:url>
						    <div id="getCustomerQuickLinksDiv"></div>
							<script>
						    	Ext.onReady(function(){
					    		   	var url = "<s:property value='#getCustomerQuickLinksURL'/>";
					    		   	lodDivAsAjax(url,"getCustomerQuickLinksDiv");
								});
							</script>
							<%--
							<s:action name="xpedxCustomerQuickLink" executeResult="true" namespace="/profile/user" />
							 --%>	
							 <s:url id='xpedxCustomerQuickLinkURL'  namespace='/profile/user'  action='xpedxCustomerQuickLink.action' ></s:url>
								<div id="xpedxCustomerQuickLinkDiv"></div>
							<script>
						    	Ext.onReady(function(){
					    		   	var url = "<s:property value='#xpedxCustomerQuickLinkURL'/>";
					    		   	lodDivAsAjax(url,"xpedxCustomerQuickLinkDiv");
								});
							</script>									
						</ul>
					</div>
				</td>
			</tr>
		</table>
	</div>
   </div>
    </div>
	
</s:else>
<!--EB-519-->
<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
<script type="text/javascript">
	$(document).ready(function() {
        $(document).pngFix();
        $("#video-tour").fancybox({
            'titleShow'			: false,
            'transitionIn'		: 'fade',
            'transitionOut'		: 'fade'
        });
		$("#video-tour2").fancybox({
            'titleShow'			: false,
            'transitionIn'		: 'fade',
            'transitionOut'		: 'fade'
        });
    });
	
	$(document).ready(function() {
	    $('.slideshow').cycle({
	    		fx: 'fade', // choose your transition type, ex: fade, scrollUp, shuffle, etc...
	    		pager: '#home-image-rotation-nav-inner',
	    		timeout: 5000,
	    		prev:   '#home-image-rotation-nav .img-navi-left', 
	    		next:   '#home-image-rotation-nav .img-navi-right'
		});
	});
	$(document).ready(function() { 
		$("#primary-category td ul li a").shorten();});
		</script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-home-common<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>


<!-- End of files in xpedx-home-common.js -->

<!-- Added the below commented scripts in xpedx-header.js -->




<!-- End of files in xpedx-header.js -->

<!--  Added the below commented scripts in xpedx-ext-header.js -->

<!-- End of files in xpedx-ext-header.js -->



<!-- Added the below commented scripts in xpedx-jquery-header.js -->



<!-- End of files in xpedx-jquery-header.js -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

</body>
<!-- RUgrani END: Logged in User Home page -->
<script>
function lodDivAsAjax(url,div)
{
	url = ReplaceAll(url,"&amp;",'&');
		Ext.Ajax.request({
       	url:url,
	   	success: function (response, request)
			{
			var myDiv = document.getElementById(div);
			myDiv.innerHTML = response.responseText;
		}
	});
}
</script>
</html> 
