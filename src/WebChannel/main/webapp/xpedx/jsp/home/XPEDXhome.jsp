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
	<meta content='IE=9' http-equiv='X-UA-Compatible' />       
	<%
  		request.setAttribute("isMergedCSSJS","true");
  	  %>
 	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
 	<!--[if IE]>
	<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
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
		<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
		
		<script type="text/javascript">
		function browserSupport(){

		if (/MSIE (\d+\.\d+);/.test(navigator.userAgent)){ //test for MSIE x.x;
		   var ieversion=new Number(RegExp.$1) // capture x.x portion and store as a number
			 if (ieversion < 7)
			  alert("The application does not support this browser or this browser version. For a list of supported browsers please visit: https://content.ipaper.com/storefront/xpedx_help.html"); 	
			}

		if (/Netscape[\/\s](\d+\.\d+)/.test(navigator.userAgent)){ //test for Netscape navigator x.x (ignoring remaining digits);
		    var NavigatorVersion=new Number(RegExp.$1)
		         alert("The application does not support this browser or this browser version. For a list of supported browsers please visit: https://content.ipaper.com/storefront/xpedx_help.html"); 
		}

		else if (/Firefox[\/\s](\d+\.\d+)/.test(navigator.userAgent)){ //test for Firefox/x.x or Firefox x.x (ignoring remaining digits);
		    var ffversion=new Number(RegExp.$1)
			 if(ffversion < 3.4)
		 	    alert("The application does not support this browser or this browser version. For a list of supported browsers please visit: https://content.ipaper.com/storefront/xpedx_help.html"); 
		 }

		if (/Safari[\/\s](\d+\.\d+)/.test(navigator.userAgent)){ //test for Safari x.x (ignoring remaining digits);
		    var str= navigator.appVersion;
			var str1= str.substring(89,90);
			if(str1 < 4)
		  	  alert("The application does not support this browser or this browser version. For a list of supported browsers please visit: https://content.ipaper.com/storefront/xpedx_help.html"); 
		 }
		
		/* Commented For Jira 3646 
		if (/Chrome[\/\s](\d+\.\d+)/.test(navigator.userAgent)){ //test for Netscape navigator x.x (ignoring remaining digits);
		      alert("The application does not support this browser or this browser version. For a list of supported browsers please visit: https://content.ipaper.com/storefront/xpedx_help.html"); 
		} */
		
		if (/Opera[\/\s](\d+\.\d+)/.test(navigator.userAgent)){ //test for Netscape navigator x.x (ignoring remaining digits);
		     alert("The application does not support this browser or this browser version. For a list of supported browsers please visit: https://content.ipaper.com/storefront/xpedx_help.html"); 
		}

		}
		
		
		</script>
<script type="text/javascript">
		browserSupport();
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
			<!-- Ad Juggler Tag Starts -->
			
			<s:set name='storefrontId' value="wCContext.storefrontId" />

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
		<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
		
 		
</s:else>

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

<script type="text/javascript">

	function loginSubmit(myfield,e)
	{
		var keycode;
		if (window.event) keycode = window.event.keyCode;
		else if (e) keycode = e.which;
		else return true;

		if (keycode == 13)
	   	{
			signIn();	//added for JIRA 3936
	   	}
		else{
	   		return true;
		}
	}
	function signIn()//added for JIRA 3936
	{
		document.singForm.DisplayUserID.value=document.singForm.DisplayUserID.value.toLowerCase();
		document.singForm.submit();
   		return false;
	}
    </script>

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
