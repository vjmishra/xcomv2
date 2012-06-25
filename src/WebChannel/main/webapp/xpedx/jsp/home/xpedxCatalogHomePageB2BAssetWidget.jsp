<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="../WEB-INF/c.tld" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />


<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs�. 
    This is to avoid a defect in Struts that�s creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts� OGNL statements. --%>
	<s:set name='_action' value='[0]'/>
	<!-- <s:set name='tourURL' value='%{tourURL}'/> -->
	
	<div class="anon-hp-announcements-containers">
       <!--  <h2>Tour Our B2B Site</h2> -->
        <h2><s:text name="MSG.SWC.B2B.COMMERCESITE.GENERIC.PGTITLE"/></h2>
		
		<!-- 
        <a href="../modals/video-tour.html" id="video-tour"><img class="img-padding-hp" src="<s:property value='assetURL'/>" alt="Video Tour" width="90" height="81" align="left" border="none" /></a>
         -->
        <a href="../modals/video-tour.html" id="video-tour"><img class="img-padding-hp" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/anon-video-tour-launch<s:property value='#wcUtil.xpedxBuildKey' />.gif" alt="Video Tour" width="90" height="81" align="left" border="none" /></a>
        <p>Get a brief introduction to our B2B commerce site and services.</p>
        <br />
        <a href="javascript:takeTheTour('www.google.com');" id="video-tour2" class="grey-ui-btn"><span>Take the Tour »</span></a>
    </div>
	
	
	<script type="text/javascript">    

		function takeTheTour(tourURL)
		{
			alert("tour url" + tourURL);
			window.open(tourURL,'B2B Tour');
		}
	</script>