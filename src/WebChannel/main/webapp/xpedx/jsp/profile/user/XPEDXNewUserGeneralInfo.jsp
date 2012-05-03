<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs�. 
    This is to avoid a defect in Struts that�s creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts� OGNL statements. --%>
<s:set name='_action' value='[0]' />
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean'
	id='util' />
<s:set name='ctxt' value="#_action.getWCContext()" />
<s:set name='userListResId' value='"/swc/profile/ManageUserList"' />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<swc:html>
<head>
<swc:head />
<title><s:text name="my.Account.page" /></title>

<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/user/my-account.css" />
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/profile/profile.js"></script>
</head>
<body>
<s:set name='xmlUtil' value="#_action.getXMLUtils()" />
<s:set name='customerId' value='%{#_action.getCustomerID()}' />
<s:set name='ctxt' value="#_action.getWCContext()" />
<s:set name='userListResId' value='"/swc/profile/ManageUserList"' />
<div id="main"><!-- begin header -->
<div class="t2-header commonHeader" id="headerContainer"><!-- add content here for header information -->
<s:action name="header" executeResult="true" namespace="/common" /></div>
<!-- end header --> <!-- Begin Navigation include Tab -->
<div class="t2-navigate commonNavigate"><s:action name="navTab"
	executeResult="true" namespace="/profile/user">
	<s:param name="navSelectedTab">WorkWithUsers</s:param>
</s:action></div>
<!-- End Navigation include Tab -->

<div class="container"><!-- // begin container-->
<div class="t2-container">
<div class="t2-mainContent" id="t2-mainContent"><!-- add content here for main content -->

<!-- main content header -->
<div>

<table width="98%">
	<tr>
		<td width="75%"><span class="headerText padding-left3"> <s:text
			name="new" />&nbsp;<s:text name="contact" /></span></td>
		<s:if
			test="@com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer@getInstance().isAuthorized(#userListResId,#ctxt)">
			<td align="right" class="padding-right3"><a tabindex="300"
				href="<s:url value='/profile/user/getUserList.action' includeParams='none'>
                <s:param name='customerID' value='%{customerId}'/></s:url>"><s:text
				name="ViewUsers" /></a></td>
		</s:if>
	</tr>
</table>
</div>

<div class="tab-1">
<ul>
	<li class="selected"><a href="#"><s:text
		name="GeneralInformation" /></a></li>
</ul>
</div>

<div class="tabContent"><s:form name="myAccount" method="post"
	validate="true" action="saveNewUserInfo">
	<s:hidden name='operation' value='%{#_action.getOperation()}' />
	<s:hidden name='customerId' value='%{#customerId}' />
	<s:hidden name="#action.namespace" value="/profile/user" />
	<s:hidden name="#action.name" value="saveNewUserInfo" />
	<s:hidden name='buyerOrgCode' value='%{#_action.getBuyerOrgCode()}' />

	<p>&nbsp;</p>

	<div class="mainContentHeadRow2" id="mainContentHeadRow2">
	<div class="mainContentHeadRow2Sub2" id="mainContentHeadRow2Sub2">
	<ul />
		<ul>
			<li><input tabindex="115" type="button" name="CancelButton"
				value="<s:text name='Cancel' />"
				onClick="javascript:document.cancelNewUserCreationForm.submit();"
				class="submitBtnBg2" /></li>
			<li><s:submit tabindex="110" action="saveNewUserInfo"
				name="Save" key="save" cssClass="submitBtnBg2" /></li>
		</ul>
		<ul />
	</div>
	</div>
	<table class="listTableHeader2" id="listTableHeader1">
		<tr>
		<tr>
			<td><span class="listTableHeaderText"><s:text
				name="user.Information" /></span></td>
		</tr>
	</table>
	<div id="generalInfoTable">

	<table width="96%" class="listTableBody3 padding-left3">
		<tr>
			<td class="boldText textAlignLeft"><s:text name="belongsTo" />:
			<a tabindex="200" id="Belongsto"
				href="<s:url value='/profile/org/getCustomerGeneralInfo.action' includeParams='none'>
                <s:param name='customerId' value='%{#customerId}'/></s:url>">
			<s:property value="#customerId" /> </a></td>
		</tr>
		<tr valign="top">
			<td width="35%">
			<table class="cartSubTable">
				<tr>
					<td class="themeSubHeader" colspan="3"><s:text
						name="account.SignInAnd.Security" /></td>
				</tr>

				<tr>
					<td class="boldText textAlignLeft"><s:text name="RB_userName" />:</td>
					<td class="mandatoryIndicator"><s:text
						name="mandatory.Indicator.Text" /></td>
					<td><s:textfield
						cssClass="generalInfoTableSmallTxt boldText textAlignLeft"
						tabindex="5" name="userName" /></td>

				</tr>

				<tr>
					<td class="boldText textAlignLeft"><s:text
						name="RB_preferredLocale" />:</td>
					<td class="mandatoryIndicator"><s:text
						name="mandatory.Indicator.Text" /></td>
					<td><s:select cssClass="generalInfoTableSmallSelect"
						tabindex="10" name="preferredLocale"
						list="#_action.getLocaleList()" emptyOption="true" /></td>

				</tr>

			</table>
			</td>
			<td width="30%">
			<table class="cartSubTable">
				<tr>
					<td class="themeSubHeader" colspan="2"><s:text
						name="userNameAndTitle" /></td>
				</tr>
				<tr>
					<td class="boldText textAlignLeft"><s:text name="RB_title" />:</td>
					<td class="mandatoryIndicator"></td>
					<td><s:select cssClass="generalInfoTableSmallSelect"
						tabindex="15" name="title" list="%{#_action.getTitle()}" /></td>
				</tr>
				<tr>
					<td class="boldText textAlignLeft"><s:text name="RB_firstName" />:</td>
					<td class="mandatoryIndicator"><s:text
						name="mandatory.Indicator.Text" /></td>
					<td><s:textfield cssClass="generalInfoTableSmallTxt"
						tabindex="20" name="firstName" /></td>
				</tr>
				<tr>
					<td class="boldText textAlignLeft"><s:text name="RB_lastName" />:</td>
					<td class="mandatoryIndicator"><s:text
						name="mandatory.Indicator.Text" /></td>
					<td><s:textfield cssClass="generalInfoTableSmallTxt"
						tabindex="25" name="lastName" /></td>
				</tr>
				<tr>
					<td class="boldText textAlignLeft"><s:text name="RB_jobTitle" />:</td>
					<td class="mandatoryIndicator"></td>
					<td><s:textfield cssClass="generalInfoTableSmallTxt"
						tabindex="30" name="jobTitle" /></td>
				</tr>
				<tr>
					<td class="boldText textAlignLeft"><s:text name="RB_EmailID" />:</td>
					<td class="mandatoryIndicator"><s:text
						name="mandatory.Indicator.Text" /></td>
					<td><s:textfield cssClass="generalInfoTableSmallTxt"
						tabindex="35" name="emailId" /></td>
				</tr>
				<tr>
					<td class="boldText textAlignLeft"><s:text name="RB_deptName" />:</td>
					<td class="mandatoryIndicator"></td>
					<td><s:textfield cssClass="generalInfoTableSmallTxt"
						tabindex="40" name="deptName" /></td>
				</tr>
			</table>
			</td>
			<td width="35%">
			<table class="cartSubTable">
				<tr>
					<td class="themeSubHeader" colspan="4"><s:text
						name="userRoles" /></td>
				</tr>
				<tr>
					<td><s:checkbox tabindex="45" name='test' fieldValue="test123"
						value="true" disabled="true" /></td>
					<td class="boldText textAlignLeft"><s:text name="buyerUser" /></td>
				</tr>
				<tr>
					<td><s:checkbox tabindex="50" name='buyerAdmin'
						fieldValue="true" /></td>
					<td class="boldText textAlignLeft"><s:text name="buyerAdmin" /></td>
				</tr>
				<tr>
					<td><s:checkbox tabindex="55" name='buyerApprover'
						fieldValue="true" /></td>
					<td class="boldText textAlignLeft"><s:text
						name="buyerApprover" /></td>
				</tr>				
				<tr>
					<td><s:checkbox name='viewInvoices' fieldValue="true" /></td>
					<td class="boldText textAlignLeft"><s:text
						name="View Invoices" /></td>
				</tr>
				<tr>
					<td><s:checkbox name='estimator' fieldValue="true" /></td>
					<td class="boldText textAlignLeft"><s:text name="Estimator" /></td>
				</tr>
				<tr>
					<td><s:checkbox name='stockCheckWebservice' fieldValue="true" /></td>
					<td class="boldText textAlignLeft"><s:text
						name="Stock Check Webservice" /></td>
				</tr>
				<tr>
					<td><s:checkbox name='punchoutUsers' fieldValue="true" /></td>
					<td class="boldText textAlignLeft"><s:text
						name="Punchout Users" /></td>
				</tr>
				<tr>
					<td>
						<s:checkbox name="viewPrices" fieldValue="true" />
					</td>
					<td  class="boldText textAlignLeft">
						<s:text name="View Prices" />
					</td>
					
				</tr>
				<tr>
					<td>
						<s:checkbox name="viewReports" fieldValue="true" />
					</td>
					<td  class="boldText textAlignLeft">
						<s:text name="View Reprots" />
					</td>
				</tr>
				<tr>					
					<td>
						<s:checkbox name="orderConfirmationEmailFlag" fieldValue="true" />
					</td>
					<td  class="boldText textAlignLeft">
						<s:text name="RecieveOrderConfirmationEmail" />
					</td>
				</tr>
				<tr>
					<td>
						<s:checkbox name="orderCancellationEmailFlag" fieldValue="true" />
					</td>
					<td  class="boldText textAlignLeft">
						<s:text name="RecieveOrderCancellationEmail" />
					</td>
					
				</tr>
				<tr>
					
					<td>
						<s:checkbox name="orderUpdateEmailFlag" fieldValue="true"  />
					</td>
					
					<td  class="boldText textAlignLeft">
						<s:text name="RecieveOrderUpdateEmailByCSR" />
					</td>
				</tr>
				<tr>
					
					<td>
						<s:checkbox name="orderShipmentEmailFlag" fieldValue="true" 	/>
					</td>
					<td  class="boldText textAlignLeft">
						<s:text name="RecieveOrderShipmentEmail" />
					</td>
				</tr>
				<tr>
					
					<td>
						<s:checkbox name="backorderEmailFlag" fieldValue="true" />
					</td>
					
					<td  class="boldText textAlignLeft">
						<s:text name="RecieveBackorderEmail" />
					</td>
				</tr>
				<tr>
					<td width="50%"><s:radio name="b2bCatalogView" list="b2bCatalogViewMap" value="defaultB2bCatalogView" cssStyle="display: table-column;" /></td>
					<td class="boldText textAlignLeft"><s:text name="B2B Catalog View" />:</td>
				</tr>				
				<tr>
					<td class="themeSubHeader" colspan="4"><s:text
						name="RB_statuses" /></td>
				</tr>
				<tr>
					<td class="boldText textAlignLeft"><s:text name="RB_Status" />:</td>
					<td><s:select cssClass="generalInfoTableSmallSelect"
						tabindex="60" name="status" headerKey="1"
						list="#_action.getStatusList()" /></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>

	</div>

	<div class="mainContentHeadRow2" id="mainContentHeadRow2">
	<div class="mainContentHeadRow2Sub2" id="mainContentHeadRow2Sub2">
	<ul>
		<li><input tabindex="120" type="button" name="CancelButton"
			value="<s:text name='Cancel' />"
			onClick="javascript:document.cancelNewUserCreationForm.submit();"
			class="submitBtnBg2" /></li>
		<li><s:submit tabindex="105" action="saveNewUserInfo" name="Save"
			key="save" cssClass="submitBtnBg2" /></li>
	</ul>
	</div>
	</div>
	<div>
	<table>
		<tr>
			<td>(</td>
			<td class="mandatoryIndicator"><s:text
				name="mandatory.Indicator.Text" />
			<td>): <s:text name="pageLevelTextForMandatoryIndicator" /></td>
		</tr>
	</table>
	</div>
</s:form> <s:form id="cancelNewUserCreationForm" name="cancelNewUserCreationForm"
	action="getUserList" namespace="/profile/user" method="POST">
	<s:hidden name="customerID" value='%{#customerId}' />
</s:form></div>
<!-- // tab content end --></div>
<!-- // t2-main-content end --> <!-- // t2-container end --></div>
</div>
<!-- // container end -->
<div class="t2-footer commonFooter" id="t2-footer"><!-- add content here for footer -->
<s:action name="footer" executeResult="true" namespace="/common" /></div>
<!-- // footer end --></div>
<!-- // main end -->
</body>
</swc:html>
