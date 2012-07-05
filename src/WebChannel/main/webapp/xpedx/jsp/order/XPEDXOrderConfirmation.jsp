<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:set name='_action' value='[0]'/>
<s:set name="xutil" value="#_action.getXMLUtils()"/>
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean' id='xpedxutil' />
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<s:set name="emailDialogTitle" scope="page" value="#_action.getText('Email_Title')"/>
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="webapp-context" content="/swc" />
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<%
  		request.setAttribute("isMergedCSSJS","true");
  	  %>
<!-- Webtrends Tag starts -->
<s:if test="%{deliveryMetaTag}">
		<META Name="DCSext.w_x_hold" Content="1"> 		
</s:if>
<s:if test='%{willCallFlag == "true"}'>
 <META Name="DCSext.w_x_ord_willcall" Content="1">
 </s:if>
 
 <s:if test='%{#session.rushOrderFlag != null}'>		
	<meta name ="DCSext.w_x_rush" content="1" />
	<s:set name="rushOrderFlag" value="<s:property value=null />" scope="session"/> 
	<s:set name="metatagValue" value="<s:property value=null />" scope="session"/>
</s:if>
  
  <s:if test='%{poNumberSaveNeeded == "true"}'>
	<META Name="DCSext.w_x_po" Content="1">
	</s:if>
  
<!-- Webtrends Tag stops -->
<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />

<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<![endif]-->
<!--  End Styles -->
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/xpedx-header<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/common/xpedx-ext<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/common/xpedx-jquery-header<s:property value='#wcUtil.xpedxBuildKey' />.css" />
 
<!-- 
<link media="all" type="text/css" rel="stylesheet"	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/shipping-option.css" />
<link media="all" type="text/css" rel="stylesheet"	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/draft-order-list.css" />
<link media="all" type="text/css" rel="stylesheet"	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/shopping-cart-detail.css" />
<link media="all" type="text/css" rel="stylesheet"	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/order-adjustment.css" />
<link media="all" type="text/css" rel="stylesheet"	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/common/email/email.css" />
 -->
<!-- styles -->
<%-- <link media="all" type="text/css" rel="stylesheet"	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/ext-all.css" /> --%>
<!-- 
<link media="all" type="text/css" rel="stylesheet"	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/theme-xpedx-nav-test.css" />
 -->
<s:include value="../common/XPEDXStaticInclude.jsp"/>

<link media="all" type="text/css" rel="stylesheet"	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx-mil<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/xpedx-order<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<%-- <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/shopping-cart.css" />--%>


<!-- jQuery -->
<%--<link type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/themes/base/jquery.ui.all.css" rel="stylesheet" /> --%>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/jquery-1.4.2<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-jquery-headder<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.core.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.widget.js"></script>
-->

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/order-confirmation<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/common/email/email<s:property value='#wcUtil.xpedxBuildKey' />.css" />

<!-- carousel scripts css  -->
<%-- <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/skins/xpedx/theme.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/skins/xpedx/skin.css" /> --%>
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel.js"></script>
--><!-- carousel scripts js   -->
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.tabs.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.shorten.js"></script>
-->
<%-- <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/modals/checkboxtree/demo.css"/> --%>
<%-- <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/modals/checkboxtree/jquery.checkboxtree.css"/> --%>
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/modals/checkboxtree/jquery.checkboxtree.js"></script>
--><!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add.js"></script>
-->
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/DD_roundies_0.0.2a-min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pseudofocus.js"></script>
--><!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global-xpedx-functions.js"></script>
-->
<%-- <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/shopping-cart.css" /> 
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/om2.css" />--%>
<title><s:property value="wCContext.storefrontId" /> - <s:text name="orderlist.title"/></title>

<!-- Facy Box (Lightbox/Modal Window -->
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.1.js"></script> --%>
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.1.css" media="screen" />
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel.js"></script>--><!-- end of addition of js for jira 2128 -->
<script type="text/javascript">
	$(document).ready(function() {
		$(document).pngFix();
	});
</script>
<script type="text/javascript">
	function printEmails() {
			var emailAddress = document.getElementById('addtnlEmailAddresses').value;
	        var emailAddrs = emailAddress.split(";");
	        for(i = 0; i < emailAddrs.length; i++){
	            if(i==0)
	            {
	            	document.write("<ul>");
	            }
	        	document.write("<li>"+emailAddrs[i]+"</li>");
	        	if(i==(emailAddrs.length-1)) 
	            {
	        		document.write("</ul>");	
	            } 
	        }
		}	
</script>
<title><s:property value="wCContext.storefrontId" /> / Order Confirmation: Success</title>
<!-- Start of webtrends -->
 <Meta name="WT.ti" Content='<s:property value="wCContext.storefrontId" /> / Order Confirmation: Success'>
 <!-- End of webtrends -->
</head>
<body class="ext-gecko ext-gecko3">
	<s:set name='scuicontext' value="wCContext" />
	<s:set name='showCurrencySymbol' value='%{true}' />
	<s:set name='conOrder' value="confirmDraftOrderElem" />
	<s:set name='overallTotals' value='#xutil.getChildElement(#conOrder,"OverallTotals")'/>
	<s:set name='priceInfo' value='#xutil.getChildElement(#conOrder,"PriceInfo")'/>
	<s:set name='ExtnInfo' value='#xutil.getChildElement(#conOrder,"Extn")'/>
	<s:set name='ototal' value='#xutil.getAttribute(#ExtnInfo,"ExtnTotalOrderValue")'/>
	<s:set name='currencyCode' value='#xutil.getAttribute(#priceInfo,"Currency")'/>
	<s:set name='orderDate' value='#xutil.getAttribute(#conOrder,"OrderDate")'/>
	<s:set name='contactId' value='%{#_action.getWCContext().getCustomerContactId()}'/>
	<s:set name='storeFrontId' value='%{#_action.getWCContext().getStorefrontId()}'/>
	<s:set name="xpedxCustomerContactInfoBean" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("XPEDX_Customer_Contact_Info_Bean")' />	
	<%-- Commented for performance issue since already we have emailID cached.
	<s:set name='userInfo' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUserInfo(#contactId, #storeFrontId)'/>	
	<s:set name='CustomerContact' value='#xutil.getChildElement(#userInfo,"CustomerContact")'/>
	<s:set name='Customer' value='#xutil.getChildElement(#CustomerContact,"Customer")'/>
	<s:set name='customerContactExtn' value='#xutil.getChildElement(#CustomerContact,"Extn")'/>
	<s:set name='orderConfirmationFalg' value='#xutil.getAttribute(#customerContactExtn,"ExtnOrderConfEmailFlag")'/>
	<s:set name='CustomerAdditionalAddressList' value='#xutil.getChildElement(#CustomerContact,"CustomerAdditionalAddressList")'/>
	<s:set name='CustomerAdditionalAddress' value='#xutil.getChildElement(#CustomerAdditionalAddressList,"CustomerAdditionalAddress")'/>
	<s:set name='PersonInfo' value='#xutil.getChildElement(#CustomerAdditionalAddress,"PersonInfo")'/>
	 --%>
	 <%--for jira 3438 - sales rep emailID display --%>
	 <s:set name="isSalesRep" value ="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute('IS_SALES_REP')}"/>
	 <%--end of jira 3438 changes - sales rep emailID display --%>
	<s:set name='isOrderOnApprovalHoldStatus' value='%{#_action.isOrderOnApprovalHoldStatus()}'/>
	<s:set name='isOrderOnNeedsAttentionHold' value='%{#_action.isOrderOnNeedsAttentionHold()}'/>
	<s:set name="isCSRReview" value="%{#_action.isCSRReview()}"/>
	<s:set name='resolverUserID' value='%{#_action.getResolverUserID()}'/>
	<s:if test="#isOrderOnApprovalHoldStatus">
		 	<%-- <s:set name='usersInfoMap' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUsersInfoMap(#resolverUserID, #storeFrontId)'/>
		 	<s:set name='userInfo' value='#usersInfoMap.get(#_action.getPrimaryApproverID())'/>	
			<s:set name='CustomerContact' value='#xutil.getChildElement(#userInfo,"CustomerContact")'/>
			<s:set name='CustomerContact' value='#usersInfoMap.get(#_action.getPrimaryApproverID())'/>
			<s:set name='Customer' value='#xutil.getChildElement(#CustomerContact,"Customer")'/>
			<s:set name='CustomerAdditionalAddressList' value='#xutil.getChildElement(#CustomerContact,"CustomerAdditionalAddressList")'/>
			<s:set name='CustomerAdditionalAddress' value='#xutil.getChildElement(#CustomerAdditionalAddressList,"CustomerAdditionalAddress")'/>
			<s:set name='PersonInfo' value='#xutil.getChildElement(#CustomerAdditionalAddress,"PersonInfo")'/>
			
		 	<s:set name='CustomerContact1' value='#usersInfoMap.get(#_action.getProxyApproverID())'/>
			<s:set name='Customer1' value='#xutil.getChildElement(#CustomerContact1,"Customer")'/>
			<s:set name='CustomerAdditionalAddressList1' value='#xutil.getChildElement(#CustomerContact1,"CustomerAdditionalAddressList")'/>
			<s:set name='CustomerAdditionalAddress1' value='#xutil.getChildElement(#CustomerAdditionalAddressList1,"CustomerAdditionalAddress")'/>
			<s:set name='PersonInfo1' value='#xutil.getChildElement(#CustomerAdditionalAddress1,"PersonInfo")'/> --%>
			<%-- <s:set name='userInfo1' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUserInfo(#resolverUserID, #storeFrontId)'/> --%>			
    </s:if>
	<s:else>
			
	</s:else>
	<s:url id="returnUrl" action="orderList">
    <s:param name="orderByAttribute" value="orderByAttribute"/>
    <s:param name="orderDesc" value="orderDesc"/>
    <s:param name="pageNumber" value='%{pageNumber}'/>
    <s:param name="searchFieldName" value="getXpedxSearchFieldName()"/>
    <s:param name="searchFieldValue" value="searchFieldValue"/>
    <s:param name="statusSearchFieldName" value="getExactStatus()"/>
	<s:param name="submittedTSFrom" value="submittedTSFrom"/>
	<s:param name="submittedTSTo" value="submittedTSTo"/>
    <s:param name="shipToSearchFieldName" value="shipToSearchFieldName"/>   
	</s:url>
	<div id="main-container">
			        <s:set name='orderHeaderKey' value="orderHeaderKey"/>
			         <s:set name='draftOrderFlag' value='#parameters.draft'/>
					<s:url  id = "urlEmail" includeParams="none" escapeAmp="false" action='emailOrder' namespace = '/order'>
						<s:param name="messageType" value='%{"ComposeMail"}'/>
						<s:param name="orderHeaderKey" value='#orderHeaderKey'/>
				        <s:param name="draft" value="#draftOrderFlag"/>
					</s:url>
					<s:set name="isDraftOrder" value="#_action.isDraftOrder()"  />
					<s:if test='#isDraftOrder'>
							<s:url id="urlPrint"  includeParams="none" escapeAmp="false" action='PrintCartDetail.action' namespace = '/order' >
							</s:url>
					</s:if>
					<s:else>
							<s:url id="urlPrint"  includeParams="none" escapeAmp="false" action='PrintOrderDetail.action' namespace = '/order' >
							</s:url>
					</s:else>
				
					<s:url  id = "urlEmail" includeParams="none" escapeAmp="false" action='emailOrder' namespace = '/order'>
						<s:param name="messageType" value='%{"ComposeMail"}'/>
						<s:param name="orderHeaderKey" value='%{#orderHeaderKey}'/>
				        <s:param name="draft" value="#draftOrderFlag"/>
					</s:url>
        <div id="main">        	 
			<div class="t1-header commonHeader" id="headerContainer"><s:action name="xpedxHeader" executeResult="true" namespace="/common" /></div>
			<div class="container">
                <!-- breadcrumb -->
                <div id="breadcumbs-list-name">
                		
                		<s:if test='#isOrderOnApprovalHoldStatus'>
                		<span class="page-title">Confirmation</span>
<%-- 							<a href="<s:url action="portalHome" namespace="/home" includeParams='none'/>">&nbsp;Orders</a> / <span class="breadcrumb-inactive">Order Confirmation: <span class="breadcrumb-alert">Approval Required</span></span> --%>
						</s:if>
						<s:else>
						<span class="page-title">Confirmation</span>
<%-- 							<a href="<s:url action="portalHome" namespace="/home" includeParams='none'/>">Orders</a> / <span class="breadcrumb-inactive">Order Confirmation: <span class="breadcrumb-alert"></span>Success</span> --%>
						</s:else>
                	    <a href="javascript:window.print()"><span class="print-ico-xpedx order-confirm underlink" ><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/print-icon<s:property value='#wcUtil.xpedxBuildKey' />.gif" width="16" height="15" alt="Print This Page" />Print Page</span></a>
<%--                     <a href="javascript:emailLightBox('<s:property value="%{urlEmail}" />')" class="underlink"><span class="print-ico-xpedx order-confirm"><img width="16" height="15" alt="Print This Page" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/email-icon.gif">Email Page</span></a> --%>
					<br/>
					<br/>
                </div>
                <div id="mid-col-mil">
					<fieldset  class="x-corners mil-col-mil-div">						
                        <legend>Web Confirmation:&nbsp; 
                        <s:if test='#isDraftOrder'>
							<s:url id="orderDetailsURL" action="orderDetail" escapeAmp="false" >
							  <s:param name="orderHeaderKey" value='#conOrder.getAttribute("OrderHeaderKey")'/>  
							  <s:param name="orderListReturnUrl" value='#returnUrl'/>
							</s:url>
                        </s:if>
						<s:else>
							<s:url id="orderDetailsURL" action="orderDetail" escapeAmp="false" >
							  <s:param name="orderHeaderKey" value='parentOrderHeaderKeyForFO'/> 
							  <s:param name="orderListReturnUrl" value='#returnUrl'/> 
							</s:url>
						</s:else>
<%--                         <s:a cssClass="underlink" href="%{orderDetailsURL}"> --%>
                        	<s:property value='#xutil.getAttribute(#ExtnInfo,"ExtnWebConfNum")'/>
<%--                         </s:a> --%>
                        </legend>
                        <br>
                        <s:if test='#isOrderOnApprovalHoldStatus'>
							<p>Your order <span class="attention">requires approval</span> and has been sent to:&nbsp; 
							<s:if test='%{#_action.getPrimaryApprovalEmailId() != "" }'>
								<s:property value='%{#_action.getPrimaryApprovalEmailId()}'/>
							</s:if>
							<s:if test='%{#_action.getPrimaryApprovalEmailId() != null && #_action.getProxyApprovalEmailId() != null}'>, 
							</s:if>
							<s:if test='%{#_action.getProxyApprovalEmailId() != "" }'>
							<s:property value='%{#_action.getProxyApprovalEmailId()}'/> </s:if></p>
							
							<br>
							<s:if test='%{#orderConfirmationFalg =="Y"}'>
							    <p>Status updates have been sent to your email address:&nbsp; <s:property value='#xpedxCustomerContactInfoBean.getEmailID()'/></p>
								<br>	
						    </s:if>													
							<p>Additional status updates will be sent to the following addresses:</p>							
						</s:if>
						<s:else>
							<p>Thank you for your order. Below are your order details.</p>
							<br> 
							<s:if test='%{#orderConfirmationFalg =="Y"}'>
							    <p> Confirmation and status updates will be sent to your email address:&nbsp;<s:property value='#xpedxCustomerContactInfoBean.getEmailID()'/> </p>
								<br>
						    </s:if>
							<p> Additional confirmation and status updates will be sent to the following addresses:</p>							 
						</s:else>
                       	<ul>
                          <li><s:hidden value='%{#xutil.getAttribute(#ExtnInfo,"ExtnAddnlEmailAddr")}' id="addtnlEmailAddresses"/>                            
                          </li>
                         </ul> 
                      	<br>
                           <SCRIPT type="text/javascript">
                           		printEmails();
                           </SCRIPT>
                        <br />
                        <br />
                        <br />
                        <table width="600" class="base-tbl light-tbl">
                            <tbody>
                            
                                <tr>
                                    <td valign="top" align="left"><b>Web Confirmation:</b></td>
                                    <td valign="top" align="left">
                                    	<s:a href="%{orderDetailsURL}" cssClass="underlink">
                                    		<s:property value='#xutil.getAttribute(#ExtnInfo,"ExtnWebConfNum")'/>
                                    	</s:a>
                                    </td>
                                </tr>
                                
                                <tr>
                                	<s:set name="isPendingOrderCheckReq" value='%{true}' />  
                                    <td valign="top" align="left"><b>Order #:</b></td>
									<td valign="top" align="left">
	                                    <s:if test='#isDraftOrder'>
		                                    <s:set name="xpedxChainedOrderListMap" value='xpedxChainedOrderListMap'/>
		                                    <s:set name="chainedOrderList" value='#xpedxChainedOrderListMap.get(#orderHeaderKey)'/>
		                                    <s:iterator value='#chainedOrderList' id='chainedOrder' status='iStatus'>
			                						<s:set name='ChainedOrderExtn' value='#xutil.getChildElement(#chainedOrder,"Extn")'/>
			                							<s:url id="chainedOrderDetailsURL" action="orderDetail" escapeAmp="false" >
														  <s:param name="orderHeaderKey" value='#chainedOrder.getAttribute("OrderHeaderKey")'/>
														  <s:param name="orderListReturnUrl" value='#returnUrl'/>  
														</s:url>
														<%-- Added Condition - isPendingOrderCheckReq For Jira 4109 --%>
														
														<s:if test='%{#ChainedOrderExtn.getAttribute("ExtnLegacyOrderNo")==""}'>
<%-- 															<s:a href="%{chainedOrderDetailsURL}" cssClass="underlink"> --%>
															In progress
															<s:set name="isPendingOrderCheckReq" value='%{false}' />  
<%-- 															</s:a> --%>
														</s:if>
														<s:else>
															<s:a href="%{chainedOrderDetailsURL}" cssClass="underlink">
												  				<s:property value='@com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils@getFormattedOrderNumber(#ChainedOrderExtn)'/>
															</s:a>
														</s:else>
			                					</s:iterator>
										</s:if>
										<s:else>
	               							<s:url id="chainedOrderDetailsURL" action="orderDetail" escapeAmp="false" >
	     										<s:param name="orderHeaderKey" value='#conOrder.getAttribute("OrderHeaderKey")'/>  
	     										<s:param name="orderListReturnUrl" value='#returnUrl'/>
											</s:url>
											<s:a href="%{chainedOrderDetailsURL}" cssClass="underlink">
												<s:property value='@com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils@getFormattedOrderNumber(#ExtnInfo)'/>
											</s:a>
										</s:else>
                                    </td>
                                </tr>
                                
                                <tr>
                                    <td valign="top" align="left"><b>Order Date:</b></td>
                                    <td valign="top" align="left"><s:property value='#util.formatDate(#orderDate, #scuicontext)'/></td>
                                </tr>
                                <tr>
                                    <td valign="top" align="left"><b>Order Status:</b></td>
                                    <td valign="top" align="left"><p>
                                    		<s:if test="#isPendingOrderCheckReq && (#isOrderOnApprovalHoldStatus || #isOrderOnNeedsAttentionHold)">
                                    			<span class="attention"><s:property value="#conOrder.getAttribute('Status')"/> 
												<s:if test='#isOrderOnApprovalHoldStatus && #isPendingOrderCheckReq'>
													 <s:text name="MSG.SWC.ORDR.NEEDSATTENTION.GENERIC.STATUSPENDING.PENDAPPROVAL" />													
												</s:if>
												<s:elseif test='%{#isOrderOnNeedsAttentionHold || #isCSRReview}'>
													<s:text name="MSG.SWC.ORDR.NEEDSATTENTION.GENERIC.STATUSPENDING.CSRREVIEW" />													
												</s:elseif>
												</span>
											</s:if>
											<s:else>
													<span><s:property value="#conOrder.getAttribute('Status')"/></span>
											</s:else>
										</p></td>
                                </tr>
                                <tr>
                                    <td valign="top" align="left"><b>Supplier:</b></td>
                                    <td valign="top" align="left"><s:property value='#storeFrontId'/></td>
                                </tr>
                                <tr>
                                    <td valign="top" align="left"><b>PO #:</b></td>
                                    <td valign="top" align="left"><s:property value='#xutil.getAttribute(#conOrder,"CustomerPONo")'/></td>
                                </tr>
                                <tr>

                                    <td valign="top" align="left"><b>Ordered By:</b></td>
                                    <td valign="top" align="left">
                                    <s:property value='#xutil.getAttribute(#ExtnInfo,"ExtnOrderedByName")'/><s:if test='#xpedxCustomerContactInfoBean.getEmailID() != null && #xpedxCustomerContactInfoBean.getEmailID().trim() !="" ' >, <s:property value='#xpedxCustomerContactInfoBean.getEmailID()' /></s:if>
                                    	<%--for jira 3438 - sales rep emailID display --%>
                                    <s:if test="%{#isSalesRep && #session.SRSalesRepEmailID != ''}">, <s:property value='%{#session.SRSalesRepEmailID}'/></s:if>
                                    	<%--end of jira 3438 changes- sales rep emailID display --%>
                                    </td>
                                </tr>
                                <tr>
                                    <td valign="top" align="left"><h1> Order Total (USD):</h1></td>
                                    <td valign="top" align="left">
                                    <s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
                                    <s:set name="theMyPrice" value='#xpedxutil.formatPriceWithCurrencySymbol(#scuicontext,#currencyCode,#ototal)'/>
                                    <s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'> 
									<s:if test="%{#theMyPrice==#priceWithCurrencyTemp}">
						    			        <s:set name="isMyPriceZero" value="%{'true'}" />
											    <span class="gray"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>  
                                	</s:if>
                                	 <s:else> 
                                    <%-- <h1><s:property value='#xpedxutil.formatPriceWithCurrencySymbol(#scuicontext,#currencyCode,#ototal)'/></h1>Additional charges may apply</p> --%>
                                    <h1><s:property value='#xpedxutil.formatPriceWithCurrencySymbol(#scuicontext,#currencyCode,#ototal)'/></h1> <s:text name='MSG.SWC.ORDR.OM.INFO.ADDITIONALCHARGESTXT' /> </p>
                                    </s:else>
                                    </s:if>
                                    <s:else></s:else>
                                    </td>

                                </tr>
                            </tbody>    
                        </table>
                        <p class="legal-text"> <s:text name='MSG.SWC.ORDR.OM.INFO.LEAGALTXT' /></p>
                    </fieldset >
                   <ul class="tool-bar-bottom-right" id="tool-bar">
                        <li><a href="<s:url action="orderList"></s:url>" class="grey-ui-btn"><span>View Orders</span></a></li>
                    	<li><a href="<s:url namespace ="/catalog" action="navigate"></s:url>" class="orange-ui-btn"><span>Continue Shopping</span></a></li>
                   	</ul>

                    <div class="clearall"></div> 
                </div>
                </div>
                </div>
</div>                
                <s:form namespace="/order" method ="post"  name="downloadForm" id="downloadForm" target="">
	    			<s:hidden id="orderHeaderKey" name="orderHeaderKey" value='%{#orderHeaderKey}'/>
	    			<s:hidden name="draft" value="%{#draftOrderFlag}"/>
				</s:form>  
				<s:action name="xpedxFooter" executeResult="true" namespace="/common" /> 


<swc:dialogPanel title='${emailDialogTitle}' isModal="true" id="emailDialogPanel">
        <div class="dialog-body" id="ajax-body-1"></div>
</swc:dialogPanel>
	<!-- end container  -->
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/ajaxValidation.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/swc.js"></script>
-->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jQuery<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/XPEDXOrderConfirmation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/green/theme.js"></script>
<script type="text/javascript" src="<s:url value='/swc/js/order/email.js'/>"></script>
<script type="text/javascript" src="<s:url value='/swc/js/order/orderConfirmation.js'/>"></script>
--><!-- addition of js for jira 2128 -->
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt.js"></script> 
-->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery-ui.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
</body>

</html>