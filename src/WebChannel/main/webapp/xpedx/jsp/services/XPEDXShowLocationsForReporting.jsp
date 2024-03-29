<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="xpedx" uri="xpedx" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/locationModal-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<style>
.right {
	text-align:right;
}
</style>
<title><s:property value="wCContext.storefrontId" /> - Choose Account</title>

</head>
<body>


 <!-- modal window container -->
    <div class="share-modal1 xpedx-light-box" id="change-account">    
    
	<!-- START modal 'header' -->
	
		<h1>Select Account</h1>
		<div class="clearall">&nbsp;</div>
	<!-- END modal header -->

    <!-- START main body (with scroll bar) -->

	<div id="account-list" class="radio-container">

			<form>
				<ul>
						<s:if test='accountsMap!=null && accountsMap.size()>0' >
						
						<s:iterator id="accountDetail" value="accountsMap" status="accIndex" >
							<li>
								<s:set name='customerID' value='key' />
						
								<input type="radio" name="customerId" id="customerId_<s:property value="key"/>" value="<s:property value="value"/>;<s:property value="key"/>" />
								<s:property value="value"/>
							</li>
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
 

		<div class="float-right">
			<div class="button-container addpadtop10">
				<input class="btn-gradient floatright addmarginright10" type="submit" value="Select"  onclick="submitSapForm(); return false;" />
				<input class="btn-neutral  floatright addmarginright10" type="submit" value="Cancel" onclick="$.fancybox.close(); return false;" />
			</div>
		</div>

</div>
</body>
</html>