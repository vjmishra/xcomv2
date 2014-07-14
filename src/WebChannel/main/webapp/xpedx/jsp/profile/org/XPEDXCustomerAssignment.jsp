<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<swc:html isXhtml="true">
<head>
  <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/user/my-account<s:property value='#wcUtil.xpedxBuildKey' />.css" />
  <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/common/notes-list<s:property value='#wcUtil.xpedxBuildKey' />.css" />
  <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/user/userPreferences<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<title><s:property value="wCContext.storefrontId" /> - <s:text name="Customer Assignments" /></title>
<script type="text/javascript">			
	function moveCustomers(lboFrom, lboTo )
	{
		for ( var i=0; i < lboFrom.options.length; i++ )
		{
			if ((lboFrom.options[i].selected == true ) )
			{
				strItemToAddText = lboFrom.options[i].text;
				strItemToAddVal = lboFrom.options[i].value;
				lboTo.options[lboTo.length] = new Option(strItemToAddText, strItemToAddVal);
				lboFrom.options[i] = null;
				i--;
			}
		}
	}
	function saveChanges(saveURL){
		var lboTo=document.customerAssinment.customers2;
		for ( var i=0; i < lboTo.options.length; i++ )
		{
			lboTo.options[i].selected = true;
		}
		lboTo=document.customerAssinment.customers1;
		for ( var i=0; i < lboTo.options.length; i++ )
		{
			lboTo.options[i].selected = true;
		}
		document.customerAssinment.action=saveURL;
		document.customerAssinment.submit();
	}

</script>

</script>
<swc:head />
</head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<body>
<div id="main"><!-- begin Common header -->
	<div class="t2-header commonHeader" id="headerContainer"><!-- add content here for header information -->
	<s:action name="header" executeResult="true" namespace="/common" />
	</div>
<!-- Begin Navigation include Tab -->
<!-- Begin Navigation include Tab -->
	<s:action name="navTab" executeResult="true" namespace="/profile/user" >
	    <s:param name="navSelectedTab">UpdateUserProfile</s:param>
	</s:action>
<!-- End Navigation include Tab -->
<div class="container"><!-- // begin t2-product-list -->
<div class="t2-mainContent" id="t2-mainContent">
<!-- main content header -->
<div>
<table width="98%">
	<tr>
		<td width="75%"><span class="headerText padding-left3"> <s:text
			name="Update" />&nbsp;<s:text
			name='%{#xmlUtil.getAttribute(#sBuyerOrg,"OrganizationName")}' />&nbsp;<s:text
			name="Customer assignments" /></span></td>
		<s:if
			test="@com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer@getInstance().isAuthorized(#userListResId,#wccontext)">
			<td align="right" class="padding-right3"><a tabindex="925"
				href="<s:url value='/profile/user/getUserList.action'><s:param name="customerID" value='%{#xmlUtil.getAttribute(#sdoc,"CustomerID")}'/></s:url>"><s:text
				name="ViewUsers" /></a></td>
		</s:if>
	</tr>
</table>
</div>

<!-- Begin Customer include Tab --> 
<s:action name="userTab" executeResult="true" namespace="/profile/user">
		<s:param name="selectedTab">CustomerAssignment</s:param>
		<s:param name="customerId" value='#xmlUtil.getAttribute(#sdoc,"CustomerID")' />
	<s:param name="organizationCode"
		value='#xmlUtil.getAttribute(#sdoc,"OrganizationCode")' />
</s:action>
	</br>
	</br> <!-- End include Tab --> <!-- End Navigation include Tab --> <s:form
	action="CustomerAssignmentAction" namespace="/profile/org"
	id="customerAssinment" name="customerAssinment" method="post">
	<s:set name='_action' value='[0]' />
	<s:set name='customers1' value='#_action.getCustomers1()' />
	<s:set name='customers2' value='#_action.getCustomers2()' />
	<s:set name='listSize' value='#_action.getListSize()' />
	<s:url id='saveURL' namespace='/profile/org' action='saveCustomerAssignments'/>
	<s:hidden name="customerContactId" value="%{#_action.getCustomerContactId()}"/>
	<s:hidden name="customerId" value="%{#_action.getCustomerId()}"/>	
	<br></br>
	<div align="center">
	<table class="" align="center">
		<tr>
			<td><s:text name="Avaialble Customers"></s:text>
			
			<div id="wwgrp_customer1" class="wwgrp">
			<div id="wwctrl_customer1" class="wwctrl">
				<select name="customers1" multiple="true" size="%{listSize}" tabindex="1" >
				<s:iterator value="#customers1" id="currentCustId">
				<option value="<s:property/>"><s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@formatCustomer(#currentCustId,wCContext)"/> </option>
				</s:iterator>
				</select>
			</div>
			</div>
			
			</td>
			
			<td class=""><input type='button' value='<s:text name="Add" />'
				onclick='javascript:moveCustomers(customers1,customers2)'
				class='submitBtnBg1' id="AddBtn" tabindex="2" /> <br>
			<input type='button' value='<s:text name="Remove"/>'
				onclick='javascript:moveCustomers(customers2,customers1)'
				class='submitBtnBg1' id="RemoveBtn" tabindex="3" /></td>

			<td><s:text name="Selected Customers"></s:text> 
			
			<div id="wwgrp_customer2" class="wwgrp">
			<div id="wwctrl_customer2" class="wwctrl">
				<select name="customers2" multiple="true" size="%{listSize}" tabindex="4" >
				<s:iterator value="#customers2" id="currentCustId">
				<option selected="true" value="<s:property/>"><s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@formatCustomer(#currentCustId,wCContext)"/> </option>
				</s:iterator>
				</select>
			</div>
			</div>
					
			</td>
		</tr>
	</table>
	<table align="center">
		<tr>
			<td><input type='button' value='<s:text name="SaveChanges"/>'
				onclick='return saveChanges("<s:property value="#saveURL"/>")' class='submitBtnBg1' id="SaveBtn"
				tabindex="4" /></td>
		</tr>
	</table>
	</div>
</s:form>
<table width="98%">
	<tr>
		<s:if
			test="@com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer@getInstance().isAuthorized(#userListResId,#wccontext)">
			<td align="right" class="padding-right3"><a tabindex="925"
				href="<s:url value='/profile/user/getUserList.action'><s:param name="customerID" value='%{#xmlUtil.getAttribute(#sdoc,"CustomerID")}'/></s:url>"><s:text
				name="ViewUsers" /></a></td>
		</s:if>
	</tr>
</table>
<div class="t2-footer commonFooter" id="t2-footer">
      <!-- add content here for footer -->
      <s:action name="footer" executeResult="true" namespace="/common" />
</div>
        <!-- // footer end -->
</body>
</swc:html>
