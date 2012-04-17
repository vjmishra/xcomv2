 <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />


<s:set name='_action' value='[0]'/>

<s:form name="catalogForm" id="catalogForm" namespace='/catalog' action='navigate' >
	<s:url id='catURLAll' >
		<s:param name='newOP' value='%{true}'/>
	</s:url>
	<div class="shop-consumer-products">
		<h2>Shop our Consumer Products</h2>
	
	    <p>Did you know we have a large variety of paper, office supplies and catering products for consumers?</p>
	    <img class="anon-img-padding-hp" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/anon-mid-img-4.jpg" width="270" height="151" title="Product Image" alt="Product Image" />
	    <div style="margin-left: 10%; margin-top: 3px;"><a class="blue-ui-btn" href="javascript:(function(){document.catalogForm.submit();})();"><span>Shop Consumer Products Now</span></a></div>
	</div>
</s:form>