<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="xpedx" uri="xpedx" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>

<style>
.right {
	text-align:right;
}
</style>
<title><s:property value="wCContext.storefrontId" /> - Choose Ship-To</title>

</head>
<body>


 <!-- modal window container -->
    <div class="xpedx-light-box" id="change-bill-to">    
    
	<!-- START modal 'header' -->
	<div class="ship-to-header">
		<h2 class="no-border" >Choose Bill-To</h2>
		
</div>
<div class="clearall">&nbsp;</div>
<!-- END modal header -->

    <!-- START static top section with a top border -->
    <!-- hemantha -->
 
    
<!-- END top static section -->
        
        
        
    <!-- START main body (with scroll bar) -->
   
	<div class="ship-to-body">
		<div id="billtoaddress-list"  style="height: 250px;" class="x-corners ship-to-address-list">

			<form>
				<ul>
				
		<s:if test='billToMap!=null && billToMap.size()>0' >
				<s:iterator id="billToDetail" value="billToMap" status="accIndex" >
					<li class="ship-to-list">
					
						<s:set name="key" value='key' />
						<input type="radio" name="customerId" id="customerId_<s:property value="key"/>" value="<s:property value="value"/>;<s:property value="key"/>" />
						<s:property value="value"/>
					</li>
				<br/>
				</s:iterator>
			</s:if>
			<s:else>
				<li>
					<h5 align="center"><b><font color="red">You do not have sufficient security for this selection. Please contact your administrator to modify your security profile.</font></b></h5>	
				</li>
			</s:else>
</ul>
</form>


<div class="clearall"></div>
</div>
 <div class="clearview">&nbsp;</div>
<div class="float-right">
<ul id="tool-bar" class="tool-bar-bottom">

		<li>
			<a class="grey-ui-btn" href="#" style="" onclick="javascript:$.fancybox.close()"><span>Cancel</span></a>
		</li>
	<li>
		<a class="green-ui-btn" href="javascript:submitBillToForm();"><span>Apply</span></a>
	</li>
</ul>
	
</div>
</div>

</div>


</body>
</html>