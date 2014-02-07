<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<s:set name='_action' value='[0]'/>
<s:set name="xutil" value="#_action.getXMLUtils()"/>
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<s:set name="emailDialogTitle" scope="page" value="#_action.getText('Email_Title')"/>
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="webapp-context" content="/swc" />
<meta content='IE=8' http-equiv='X-UA-Compatible' />


<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />

<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<![endif]-->
<link media="all" type="text/css" rel="stylesheet"	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/shipping-option<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet"	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/draft-order-list<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet"	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/shopping-cart-detail<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet"	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/order-adjustment<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet"	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/common/email/email<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<!-- styles -->
<link media="all" type="text/css" rel="stylesheet"	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet"  href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/swc<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<!--  
<link media="all" type="text/css" rel="stylesheet"	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/theme-xpedx-nav-test.css" />
-->
<link media="all" type="text/css" rel="stylesheet"  href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/theme-xpedx_v1.2<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet"	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx-mil<s:property value='#wcUtil.xpedxBuildKey' />.css" />


<!-- jQuery -->
<link type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/themes/base/jquery.ui.all<s:property value='#wcUtil.xpedxBuildKey' />.css" rel="stylesheet" />
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.core<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.widget<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/ajaxValidation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/swc<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jQuery<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jQuery<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/order-confirmation<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/common/email/email<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/green/theme<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/email<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/orderConfirmation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$(document).pngFix();
	});
</script>
<title><s:property value="wCContext.storefrontId" /> - <s:text name='MSG.SWC.ORDR.WEBCONFERROR.GENERIC.TABTITLE' /> </title>
<!-- Start of Webtrends -->
 <Meta name="WT.ti" Content='<s:property value="wCContext.storefrontId" /> - <s:text name="MSG.SWC.ORDR.WEBCONFERROR.GENERIC.TABTITLE" /> '>
<!-- End of webtrends -->
</head>
<body class="ext-gecko ext-gecko3">

    <div id="main-container">
        <div id="main">
             <s:action name="xpedxHeader" executeResult="true" namespace="/common" />
        	
			

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

            <div class="container"> 
                <!-- breadcrumb -->
                <div id="searchBreadcrumb" >
                <br/>
                	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="<s:url action="orderList"></s:url>">
                	<b>Orders</b></a> - <span class="page-title"> <s:text name='MSG.SWC.ORDR.WEBCONFERROR.GENERIC.PGTITLE' /></span>
                	<br/>
                	<br/>
                </div>
                <div id="mid-col-mil">
					<div style="border: 1px solid #CCC; width: auto; padding: 20px;" class="x-corners">
                        <h2>Error Details</h2>
                        <br />
                        
                        <br />
                        <br />
                        <s:set name="counter" value="1"/>
						<s:set name='generatedErrorMessage' value='generatedErrorMessage'/>
							

                        <ul>
                            <li><s:if test='generatedErrorMessage != null'> <s:property value='#counter'/>. <s:property value='#generatedErrorMessage'/><s:set name="counter" value="#counter + 1"/></s:if></li>
                            <s:set name='itemValidationException' value="hasItemValidationException"/>
                            <s:if test='%{#itemValidationException=="Y"}'>
								 <s:set name='itemValidationExceptionList' value="itemValidationExceptionList"/>
								 <s:iterator value='#itemValidationExceptionList' id='itemValidationException'>
									<ul>
										<li>
											<s:property value='#counter'/>. <s:property value='#itemValidationException'/>
										</li>
									</ul>
									<s:set name="counter" value="#counter + 1"/>
					            </s:iterator>
							</s:if>
                            <s:set name='paymentException' value="hasProcessOrderPaymentException"/>
							<s:if test='%{#paymentException=="Y"}'>
								 <s:set name='hasPaymentErrorMsg' value="hasPaymentErrorMessage"/>
								 <s:if test='%{#hasPaymentErrorMsg=="Y"}'>
									 <s:set name='processOrderPayment' value="processOrderPaymentElem"/>
									 <s:set name='chargeTRNDetails' value='#xutil.getChildElement(#processOrderPayment,"ChargeTransactionDetails")'/>
									 <s:iterator value='#xutil.getChildren(#chargeTRNDetails, "ChargeTransactionDetail")' id='chargeTRNDetail'>
											<s:set name='paymentTRNErrList' value='#xutil.getChildElement(#chargeTRNDetail,"PaymentTransactionErrorList")'/>
											<s:iterator value='#xutil.getChildren(#paymentTRNErrList, "PaymentTransactionError")' id='paymentTRNErr'>
											<s:set name='errorMessage' value='#xutil.getAttribute(#paymentTRNErr,"Message")'/>
											<s:if test='%{#errorMessage != null && #errorMessage != ""}'>
												<ul>
													<li>
														<s:property value='#counter'/>. <s:property value='#xutil.getAttribute(#paymentTRNErr,"Message")'/>
														<s:set name="counter" value="#counter + 1"/>
													</li>
												</ul>
											</s:if>
											</s:iterator>

									 </s:iterator>
								 </s:if>
								 <s:else>
									<ul>
										<li>
										<s:property value='#counter'/>. <s:text name='Process_Order_Failure_Message'/>
										<s:set name="counter" value="#counter + 1"/>
									   </li>
									</ul>
								 </s:else>
							</s:if>
                        </ul>	
                        <br />
                        <br />
                        <br />
                    </div>

                     <ul class="tool-bar-bottom float-right" id="tool-bar">
	                    	<li><a href="<s:url action="OrderSummary"><s:param name="orderHeaderKey" value="%{#orderHeaderKey}" /><s:param name="draft" value="#draftOrderFlag"/></s:url>" class="grey-ui-btn"><span>Return to Checkout</span></a></li>
	                  </ul>
                    <div class="clearall"></div>
                    <br />
                    <br />
                    <br />
                    <br />

                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />

                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />

                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                </div>

			</div>
        </div>
    </div>
	<!-- end main  -->
	<s:action name="xpedxFooter" executeResult="true" namespace="/common" />   
	</div>
	<swc:dialogPanel title='${emailDialogTitle}' isModal="true"	id="emailDialogPanel">
	<div class="dialog-body" id="ajax-body-1"></div>
    </swc:dialogPanel>

	<!-- end container  -->
</body>
</html>