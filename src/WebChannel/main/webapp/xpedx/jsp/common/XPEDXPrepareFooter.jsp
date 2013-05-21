<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" />
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
								<s:url id='contactUsLink' namespace="/common" action='xpedxContact'>
								<!-- 	<s:param name="xpedxSelectedHeaderTab">ToolsTab</s:param>  -->
								</s:url>
								<s:a href="%{contactUsLink}">Contact Us</s:a>
							</td>
							<td>
								<a href="http://www.msdsonfile.com/" target="_blank">MSDS</a>
							</td>
							<td>
								<s:url id='termsOfAccessLink' namespace="/home" action='xpedxTermsOfAccess'>
								</s:url>
								<s:a href="%{termsOfAccessLink}">Terms of Access</s:a>
							</td>
							<td class="last">
								<s:url id='privacyPolicyLink'  action='xpedxPrivacyPolicy'>
								</s:url>
								<s:a href="%{privacyPolicyLink}">Privacy Policy</s:a>
							</td>
						</tr>
					</table>
					<div id="footer-copyright">&copy; International Paper Company. All rights reserved.</div>
				</td>

					<td id="social-networking-footer" class="footer-right" style="border-style: none">
						 <s:if test="%{#theStoreFront=='xpedx'}">
							<a target="_blank" href="http://youtube.com/xpedxdistribution "><img id="youtube" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/you_tube_36x36.png" alt="" /></a>
							<a target="_blank" href="http://twitter.com/xpedx"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/twitter_36x36.png" alt="" /></a>
							<a target="_blank" href="http://facebook.com/xpedx"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/facebook_36x36.png" alt="" /></a>
						</s:if>
						<s:else>
						&nbsp;
						</s:else>
					</td>

			</tr>
		</table>
</div>
<s:include value="../../htmls/webtrends/webtrends.html"/>