<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

	<!-- TODO Remove the hard coded name and get from the constants file -->
	<s:form name='homePageNewUserRegistration' namespace='/profile/user' action='MyRegisterUser'>
	     <div class="anon-home-top-left float-left">
	         <h2>New to <s:property value="wCContext.storefrontId" />?</h2>
	         <h3>Sign up to have an <s:property value="wCContext.storefrontId" /> sales representative work with you to create <br />your personalized B2B account and services.</h3>
	         <a class="orange-ui-btn" href="javascript:(function(){document.homePageNewUserRegistration.submit();})();"><span>Sign Up Now</span></a>
	         <br />
	         <br />
	         <div style="width: auto; height: 10px; border-bottom: 1px solid #ebebeb;"></div>
	
	         <h4>About Our Business-to-Business Services</h4>
	         <p>Our Business-to-Business (B2B) site delivers the best in electronic <br />commerce and information to our customers.</p>
	         <ul class="anon-srv-list float-left">
	             <li>Real time inventory</li>
	             <li>Custom pricing</li>
	             <li>Personalized My Items lists to accelerate ordering</li>
	
	             <li>Sustainability purchase reports</li>
	         </ul>
	         <ul class="anon-srv-list float-left">
	             <li>Budget controls</li>
	             <li>Custom reporting</li>
	             <li>Best class customer service</li>
	         </ul>
	
	     </div>
     </s:form>