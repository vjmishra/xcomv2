
 <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="swc" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />



<swc:html isXhtml="true">
<head>
<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs�. 
    This is to avoid a defect in Struts that�s creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts� OGNL statements. --%>
<s:set name='_action' value='[0]'/>
 <title>
    <s:property value="wCContext.storefrontId" /> - <s:text name="CustomerProfile"/>
 </title>

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/user/my-account.css" />

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/profile/profile.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/profile/org/org.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/common/verifyAddress.js" ></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/common/address/editableAddress.js" ></script>

</head>
			 <s:set name='userListResId' value='"/swc/profile/ManageUserList"'/>
			<s:set name="AddressInformationTitle" scope="page" value="#_action.getText('Address_Information_Title')"/>
			 <s:set name='xmlUtil' value="#_action.getXMLUtils()" />
			 <s:set name='sdoc' value="#_action.getCustomerOrganizationEle()" />
			 <s:set name='sBuyerOrg' value='#xmlUtil.getChildElement(#sdoc, "BuyerOrganization")'/>
 			 <s:set name='CustomerCurrencyList' value='#xmlUtil.getChildElement(#sdoc, "CustomerCurrencyList")'/>
			 <s:set name='CustomerCurrency' value='#xmlUtil.getChildElement(#CustomerCurrencyList, "CustomerCurrency")'/>
			 <s:set name='parentCustomer' value='#xmlUtil.getChildElement(#sdoc, "ParentCustomer")'/>
             <s:set name='effectiveStatus' value='#xmlUtil.getAttribute(#sdoc, "AggregateStatus")' />

            <!-- Common codes -->
             <s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
             <s:set name='wccontext' value='#_action.getWCContext()'/>
             <s:set name='currency' value='#util.getCustomerCurrency(#wccontext)' />
             <s:set name='defaultCurrency' value="#_action.getCustomerDefaultCurrency()" />
             <s:set name='hasAccessToEdit' value="#_action.getIsChildCustomer()" />
             <s:set name='statusCodes' value="#util.getCommonCodes('CUSTOMER_STATUS', @com.sterlingcommerce.webchannel.core.CommonCodeDescriptionType@SHORT, #wccontext)" />

             <!--ENDS Common codes -->
             <s:set name='loginUserId' value="#wccontext.getCustomerId()" />
             <s:set name='cId' value='#xmlUtil.getAttribute(#sdoc, "CustomerID")'/>

             <s:if test='%{#cId != null && #loginUserId != null && (#cId == #loginUserId) }'>
               <s:set name='selfUser' value="%{'TRUE'}"/>
             </s:if>
             <s:if test='%{#hasAccessToEdit == "TRUE"}'>
	             <s:set name='isDisabled' value="%{'false'}"/>
             </s:if>
             <s:else>
             	 <s:set name='isDisabled' value="%{'true'}"/>
             </s:else>
             
           
<body>
<div id="navigate">
</div>
<div id="main">



<div class="container"> <!-- // begin t2-product-list -->
<div class="t2-mainContent" id="t2-mainContent"><!-- add content here for main content -->
<!-- main content header -->
	<strong class="logo"><img src="<s:property value='%{custLogoUrl}'/>" alt="customer logo"/></strong>
 	<input type="button" name="close" value="<s:text name="Close" />" class="submitBtnBg1" onclick = "javascript:window.close();" />

</div>
</div>


</div><!-- // main end -->



</body>
</swc:html>
