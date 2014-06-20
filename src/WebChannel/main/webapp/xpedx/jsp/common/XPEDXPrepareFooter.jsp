<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />
<%-- Quick Scroll Up and Down --%>
<script>
	$(document).ready(function() {

		$(window).resize(function() {
			if ($("body").height() < $(window).height()) {
				$("#scroll-up-down").hide();

			} else 
				$("#scroll-up-down").show();
         });

		if ($("body").height() < $(window).height()) {
			$("#scroll-up-down").hide();
          }
   });
</script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/scroll-startstop.events.jquery<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/navArrows<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/processingIcon<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<div id="scroll-up-down">
		<div style="display:none;" class="nav_up" id="nav_up"></div>
		<div style="display:none;" class="nav_down" id="nav_down"></div>
</div>

<div class="t1-footer commonFooter" id="t1-footer">
		<table>
			<tr>
				<td class="footer-left" style="border-style: none">&nbsp;</td>
				<td class="footer-center" style="border-style: none">
					<table>
						<tr>						    
						    <!--  Server location -->
						    <s:set name='theStoreFront' value="wCContext.storefrontId" />
    						    <s:url id ='homeLink' action='home' namespace='/home' /> 
						    
    						    <td class="first"> 	<s:a href="%{homeLink}">Home</s:a></td>
    						    
				    
						    <s:if test="%{#theStoreFront=='xpedx'}">
								  <td ><a href="http://www.xpedx.com/" target="_blank">About Us</a></td>
						    </s:if>
						    <s:elseif test="%{#theStoreFront=='Saalfeld'}">
							      <td ><a href="http://saalfeldredistribution.com" target="_blank">About Us</a></td>
							</s:elseif>										    						    
						    <s:elseif test="%{#theStoreFront=='xpedxCanada'}">
								  <td><a href="http://www.xpedx.ca/" target="_blank">About Us</a></td>
						    </s:elseif>
						    <s:elseif test="%{#theStoreFront=='BulkleyDunton'}">
							      <td><a href="http://www.bulkleydunton.com/" target="_blank">About Us</a></td>
							</s:elseif>					
							<td>
								<s:url id='contactUsLink' namespace="/common" action='MyContact'>
								<!-- 	<s:param name="selectedHeaderTab">ToolsTab</s:param>  -->
								</s:url>
								<s:a href="%{contactUsLink}">Contact Us</s:a>
							</td>
							<td>
								<a href="http://www.msdsonfile.com/" target="_blank">MSDS</a>
							</td>
							<td>
								<s:url id='termsOfAccessLink' namespace="/home" action='MyTermsOfAccess'>
								</s:url>
								<s:a href="%{termsOfAccessLink}">Terms of Access</s:a>
							</td>
							<td class="last">
								<s:url id='privacyPolicyLink'  action='MyPrivacyPolicy'>
								</s:url>
								<s:a href="%{privacyPolicyLink}">Privacy Policy</s:a>
							</td>
						</tr>
					</table>
					<div id="footer-copyright">&copy; International Paper Company. All rights reserved.</div>
				</td>
					<!-- EB-1848 As a web channel user I would like to see updated and new social media icons on the footer page of xpedx.com so that the site reflects the new branding standards -->
					
					<td id="social-networking-footer" class="footer-right" style="border-style: none">
						 <s:if test="%{#theStoreFront=='xpedx'}">						 	
						 	<a target="_blank" href="https://www.linkedin.com/company/veritiv"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/linkedin_36x36.png" alt="" /></a>
						 	<a target="_blank" href="http://blog.xpedx.com/"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/blog_36x36.png" alt="" /></a>	
						 	<a target="_blank" href="https://www.youtube.com/user/VeritivCorp"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/you_tube_36x36.png" alt="" /></a>						
							<a target="_blank" href="https://twitter.com/veritiv"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/twitter_36x36.png" alt="" /></a>
							<a target="_blank" href="https://www.facebook.com/VeritivCorp"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/facebook_36x36.png" alt="" /></a>
						</s:if>
						<s:else>
						&nbsp;
						</s:else>
					</td>

			</tr>
		</table>
</div>
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/watermark.js"></script>

<s:include value="../../htmls/webtrends/webtrends.html"/><!--EB-519-->
