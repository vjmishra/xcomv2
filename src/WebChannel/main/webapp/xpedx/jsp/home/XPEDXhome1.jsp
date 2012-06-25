<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="swc" %>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />


       
    <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-1<s:property value='#wcUtil.xpedxBuildKey' />.css" />
    <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/swc.min<s:property value='#wcUtil.xpedxBuildKey' />.css" />
    <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/home/home<s:property value='#wcUtil.xpedxBuildKey' />.css" />
    <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/home/portalhome<s:property value='#wcUtil.xpedxBuildKey' />.css" />

    <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/catalog/narrowBy<s:property value='#wcUtil.xpedxBuildKey' />.css" />
    <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/catalog/catalogExt<s:property value='#wcUtil.xpedxBuildKey' />.css" />
    <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/styles<s:property value='#wcUtil.xpedxBuildKey' />.css" />
    <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.css" />

	<s:include value="../common/XPEDXStaticInclude.jsp"/>

    <!-- jQuery -->
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

    <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
    <!-- Lightbox/Modal Window -->
    <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
    <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
    <link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
    
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
    </script>
	<title><s:property value="wCContext.storefrontId" /> / Anonymous Homepage</title>

</head>
    <s:set name='isGuestUser' value="wCContext.guestUser" />
    <s:set name='_action' value='[0]' />

<body class="ext-gecko ext-gecko3">
	<div id="main-container">
		<div id="main" class="anon-pages">
			
			<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
			<!-- // header end -->
            
        	<s:if test="#isGuestUser == true">
	            <div class="container" id="anon-home">
					
					<s:action name="homePageSignUpNow" executeResult="true" namespace="/common" />
	                
	                <s:action name="login" executeResult="true" namespace="/common" />
	            </div>
          
	            <div class="anon-hp-mid-bg"></div>
	            
	            <div class="anon-hp-mid-rpt">
	                <s:action name="xpedxMainCategoriesAction" executeResult="true" namespace="/catalog" />
	                <s:action name="xpedxLink2CatalogAction" executeResult="true" namespace="/catalog" />
	            </div>
	            
	            <div class="hp-announcements anon-hp-announcements">
	               	<s:action name="xpedxCatalogHomePageB2BAsset" executeResult="true">
	                    <s:param name="assetId">TourB2BAsset</s:param>
	                </s:action>
	                <s:action name="xpedxCatalogPromotions" executeResult="true" namespace="/home" />	                
	                <s:action name="xpedxAnonymousQuickLink" executeResult="true" namespace="/profile/user" />
            	</div>
            </s:if>
           
    <!-- end main  --> 
  </div>

</div>
<!-- end container  -->  
            
<s:action name="xpedxFooter" executeResult="true" namespace="/common" />            

</div>
</body>
</html> 
