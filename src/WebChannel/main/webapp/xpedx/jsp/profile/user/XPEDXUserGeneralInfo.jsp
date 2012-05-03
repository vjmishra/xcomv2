<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean'
	id='util' />
<s:set name='ctxt' value="getWCContext()" />
<s:set name='userListResId' value='"/swc/profile/ManageUserList"' />
<s:set name='manageOrgResId'
	value='"/swc/profile/ManageOrganizationProfile"' />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<swc:html>
<head>
<swc:head />
<title><s:text name="my.Account.page" /></title>

<link media="all" type="text/css" rel="stylesheet"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/user/my-account.css" />
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/profile/profile.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/user/user.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/common/address/editableAddress.js"></script>
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/common/verifyAddress.js"></script>
<s:set name='expandAddressPanel' value="getExpandAddressPanel()" />
<s:set name='expandPhonePanel' value="isExpandPhonePanel()" />
<script type="text/javascript">
	xyz = function(){
		expPhnPnl = '<s:property value="#expandPhonePanel" />';
		expAddPnl = '<s:property value="#expandAddressPanel" />';
	}

	/* STARTS - Customer-User Profile Changes - adsouza */

	function addEmailToList(){
		var newEmailAddr = document.getElementById("NewEmailAddr").value;
		var aEAL = document.getElementById("AddnlEmailAddrList");
		aEAL.options[aEAL.options.length] = new Option(newEmailAddr,newEmailAddr);
		
		document.getElementById("AddnlEmailAddrText").value = document.getElementById("AddnlEmailAddrText").value + newEmailAddr + ",";
		document.getElementById("NewEmailAddr").value = "";
				
	}

	function removeEmailFromList()
	{
		var i;
		var aEAL = document.getElementById("AddnlEmailAddrList");
		var listString = "";
		for(i=0;i<aEAL.options.length;i++)
		{	
			if(aEAL.options[i].selected)
				aEAL.remove(i);
			else{
				listString += aEAL.options[i].value + ","
			}	
		}
		document.getElementById("AddnlEmailAddrText").value = listString;
	}


	function addPOToList(){
		var newPO = document.getElementById("NewPO").value;
		var pol = document.getElementById("POList");
		pol.options[pol.options.length] = new Option(newPO,newPO);

		document.getElementById("POListText").value = document.getElementById("POListText").value + newPO + ",";
		document.getElementById("NewPO").value = "";


	}

	function removePOFromList()
	{
		var i;
		var newPO = document.getElementById("POList");
		var listString = "";
		for(i=newPO.options.length-1;i>=0;i--)
		{	
			if(newPO.options[i].selected)
				newPO.remove(i);
			else{
				listString += newPO.options[i].value + ","
			}
		}
		document.getElementById("POListText").value = listString;	
	}

	/* ENDS - Customer-User Profile Changes - adsouza */
</script>
</head>
<body>
<s:set name="AddressInformationTitle" scope="page"
	value="getText('Address_Information_Title')" />
<s:set name="confirmAddressDeleteMessage" scope="page"
	value="getText('confirmAddressDelete')" />
<s:set name="confirmAddressDuplicateMessage" scope="page"
	value="getText('confirmAddressDuplicate')" />
<s:set name='xmlUtil' value="XMLUtils" />
<s:set name='sdoc' value="getCustomer().getOwnerDocument()" />
<s:set name='customerelement'
	value='#util.getElement(#sdoc, "//Customer")' />

<!-- STARTS - Customer-User Profile Changes - adsouza -->
<s:set name='extnElem' value='#util.getElement(#sdoc, "//Extn")' />
<!-- ENDS - Customer-User Profile Changes - adsouza -->

<s:set name='viewInvoices' value='%{getViewInvoices()}'/>
<s:set name='punchoutUser' value='%{getPunchoutUsers()}'/>
<s:set name='stockCheckWebservice' value='%{getStockCheckWebservice()}'/>
<s:set name='estimator' value='%{getEstimator()}'/>

<s:set name='customer' value='customerelement' />
<!-- Customer Additional Address -->
<s:set name="additionalAddressList"
	value='#util.getElement(#customer,"CustomerAdditionalAddressList")' />
<s:set name='addladdress'
	value='#util.getElements(#additionalAddressList,"CustomerAdditionalAddress")' />


<!-- s:set name='customercontact' value='#util.getElement(#sdoc,"//Customer/CustomerContactList/CustomerContact")' /-->
<s:set name='customercontact' value='getContact()' />
<!-- Customer Contact Additional Address -->
<s:set name='customeraddladdresslist'
	value='#util.getElement(#customercontact,"//CustomerContact/CustomerAdditionalAddressList")' />
<s:set name='customeraddladdress'
	value='#util.getElements(#customeraddladdresslist,"CustomerAdditionalAddress")' />

<s:set name='userelement' value="getUser()" />
<s:set name='user' value='#userelement' />
<s:set name='userExists'
	value='%{#xmlUtil.hasAttribute(#user,"Loginid")}' />
<s:if test='%{#userExists}'>
	<s:property value="User Exists" />
	<s:set name='loginid' value='%{#user.getAttribute("Loginid")}' />
	<s:set name='displayUserID'
		value='%{#user.getAttribute("DisplayUserID")}' />
	<s:set name='orgQuestionList' value='%{getQuestionListForOrg()}' />
	<s:set name='userSecretQuestion' value='%{getSecretQuestionForUser()}' />
	<s:set name='maskedPasswordString' value='%{getMaskedPasswordString()}' />
	<s:set name='maskedAnswerString'
		value='%{getMaskedSecretAnswerString()}' />
</s:if>
<s:set name='buyerOrgCode' value='%{getBuyerOrgCode()}' />
<s:set name='buyerOrgName' value='%{getBuyerOrgName()}' />
<s:set name='selfAdmin' value="isSelfAdmin()" />
<s:set name='showAdminList' value="isAdminListEnabled()" />

<s:set name='customerId' value='#customer.getAttribute("CustomerID")' />
<s:set name='customerContactId'
	value='#customercontact.getAttribute("CustomerContactID")' />
<s:set name='defShipToAddId' value='%{getDefShipToAddId()}' />
<s:set name='defBillToAddId' value='%{getDefBillToAddId()}' />
<s:set name='defSoldToAddId' value='%{getDefSoldToAddId()}' />
<s:set name='roles' value="getUserGroups()" />
<s:set name='hasAccessToEdit' value="isChildCustomer" />
<s:if test='%{#hasAccessToEdit == "TRUE"}'>
	<s:set name='isDisabled' value="%{'false'}" />
</s:if>
<s:else>
	<s:set name='isDisabled' value="%{'true'}" />
</s:else>
<div id="main"><!-- begin header -->
<div class="t2-header commonHeader" id="t2-header"><s:action
	name="header" executeResult="true" namespace="/common" /> <!-- // header end -->
<!-- // t2-header end --> <!-- // t2-navigate end --></div>
<div class="t2-navigate commonNavigate"><s:action name="navTab"
	executeResult="true" namespace="/profile/user">
	<s:param name="navSelectedTab">UpdateUserProfile</s:param>
</s:action></div>
<!-- Begin Navigation include Tab --> <!-- End Navigation include Tab -->


<div class="container"><!-- // begin t2-product-list -->
<div class="t2-container">
<div class="t2-mainContent" id="t2-mainContent"><!-- add content here for main content -->
<!-- main content header --> <s:form id="myAccount" name="myAccount"
	method="post" validate="true" action="saveUserInfo">
	<s:hidden name="customerId" value="%{customerId}" />
	<s:hidden name="customerContactId"
		value="%{#customercontact.getAttribute('CustomerContactID')}" />
	<s:hidden name="buyerOrgCode" value="%{#buyerOrgCode}" />
	<s:hidden name="#action.namespace" value="/profile/user" />
	<s:hidden id="actionName" name="#action.name" value="saveUserInfo" />

	<div>

	<table width="98%">
		<tr>
			<td width="75%"><span class="headerText padding-left3"> <s:text
				name="update" />&nbsp;<s:text
				name="%{#customercontact.getAttribute('FirstName')}" />&nbsp;<s:text
				name="%{#customercontact.getAttribute('LastName')}" />&nbsp;<s:text
				name="GeneralInformation" /></span></td>
			<s:if
				test="@com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer@getInstance().isAuthorized(#userListResId,#ctxt)">
				<td align="right" class="padding-right3"><a tabindex="925"
					href="<s:url value='/profile/user/getUserList.action' includeParams='none'>
                <s:param name='customerID' value='%{customerId}'/></s:url>"><s:text
					name="ViewUsers" /></a></td>
			</s:if>
		</tr>
	</table>
	</div>

	<!-- Begin include Tab -->
	<s:action name="userTab" executeResult="true" namespace="/profile/user">
		<s:param name="selectedTab">GeneralInfo</s:param>
		<s:param name="customerId" value='%{customerId}' />
		<s:param name="customerContactId" value='%{customerContactId}' />
	</s:action>
	<!-- End include Tab -->

	<div class="tabContent"><!-- Product list -->


	<div>
	<table class="listTableBody3 padTop" width="96%">
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td class="textAlignLeft"><span id="expandTable1"><a
				tabindex="934" href="#" onClick="expandOrCollapse();"><s:text
				name="expand.Collapse" /></a></span></td>
			<td class="textAlignRight"><s:if
				test='%{#hasAccessToEdit == "TRUE"}'>
				<s:submit tabindex="935" action="saveUserInfo" name="Save"
					key="save.changes" cssClass="submitBtnBg2" />
			</s:if></td>
		</tr>
	</table>
	</div>


	<table class="listTableHeader2" id="listTableHeader1">
		<tr>
			<td width="11" class="altBorder textAlignLeft"><s:a
				tabindex="94" href="#" id="anchortoggleUserInfo">
				<div id="toggleUserInfo" class="">&nbsp;</div>
			</s:a></td>
			<td class="listTableHeaderText"><s:text name="user.Information" /></td>
		</tr>
	</table>

	<div id="generalInfoTable">

	<table width="96%" class="listTableBody3 padding-left3">

		<tr valign="top">
			<td width="35%">
			<table>
				<tr>
					<td class="boldText textAlignLeft" colspan="2"><s:text
						name="belongsTo" />: <s:if
						test="@com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer@getInstance().isAuthorized(#manageOrgResId,#ctxt)">
						<a tabindex="92" id="Belongsto"
							href="<s:url value='/profile/org/getCustomerGeneralInfo.action' includeParams='none'>
		                <s:param name='customerId' value='%{customerId}'/></s:url>">
						<s:property value="%{#buyerOrgName}" /></a>
					</s:if> <s:else>
						<s:property value="%{#buyerOrgName}" />
					</s:else></td>
				</tr>
				<tr>
					<td class="themeSubHeader" colspan="2"><s:text
						name="account.SignInAnd.Security" /></td>
				</tr>

				<tr>
					<td class="boldText textAlignLeft"><s:text name="RB_userName" />:</td>
					<td><s:if test='%{#userExists}'>
						<s:textfield value="%{#displayUserID}" disabled="true"
							cssClass="boldText textAlignLeft" name="userName" />
						<s:hidden name="userName" value="%{#displayUserID}" />
						<s:hidden name="validateUserId" value="%{false}" />
					</s:if> <s:else>
						<s:textfield tabindex="5" disabled="%{#isDisabled}"
							cssClass="boldText textAlignLeft" name="userName" />
						<s:hidden name="validateUserId" value="%{true}" />
					</s:else></td>
				</tr>
				<s:if test="%{selfAdmin}">
					<tr>
						<td class="boldText textAlignLeft"><s:text name="RB_password" />:</td>
						<td><s:password tabindex="10" name="userpassword"
							value="%{#maskedPasswordString}" showPassword="true" /></td>
					</tr>
					<tr>
						<td class="boldText textAlignLeft"><s:text
							name="RB_confirmPassword" />:</td>
						<td><s:password tabindex="15" name="confirmpassword"
							value="%{#maskedPasswordString}" showPassword="true" /></td>
					</tr>
					<s:if test="#orgQuestionList!=null">
						<tr>						
							<td class="boldText textAlignLeft"><s:text
								name="RB_secretQuestion" />:</td>
							<td><s:select tabindex="20" name="secretQuestion"
								emptyOption="true" value="%{#userSecretQuestion}"
								list="#orgQuestionList"
								onchange="javascript:document.myAccount.secretAnswer.value='';document.myAccount.confirmAnswer.value='';" /></td>
						</tr>
					</s:if>
					<tr>
						<td class="boldText textAlignLeft"><s:text
							name="RB_secretAnswer" />:</td>
						<td><s:password tabindex="25" name='secretAnswer'
							value="%{#maskedAnswerString}" showPassword="true" /></td>
					</tr>
					<tr>
						<td class="boldText textAlignLeft"><s:text
							name="RB_confirmAnswer" />:</td>
						<td><s:password tabindex="30" name='confirmAnswer'
							value="%{#maskedAnswerString}" showPassword="true" /></td>
					</tr>
				</s:if>

				<tr>
					<td class="boldText textAlignLeft"><s:text
						name="RB_preferredLocale" />:</td>
					<td><s:select tabindex="35" name="preferredLocale"
						emptyOption='%{!#userExists}' list="getLocaleList()"
						disabled="%{#isDisabled}"
						value='%{#user.getAttribute("Localecode")}' /></td>
				</tr>

				<s:if test='%{(selfAdmin==false) && (#userExists)}'>
					<s:url id="ResetPasswordURL" action="resetPassword"
						includeParams='none'>
						<s:param name="userId" value="%{loginid}" />
						<s:param name="customerId" value="%{#customerId}" />
						<s:param name="customerContactId"
							value="%{#customercontact.getAttribute('CustomerContactID')}" />
					</s:url>
					<tr>
						<td class="boldText textAlignLeft"><s:if
							test='%{#hasAccessToEdit == "TRUE"}'>
							<s:a tabindex="40" href="%{ResetPasswordURL}"
								cssClass="submitBtnBg1">
								<s:text name="reset.Password" />
							</s:a>
						</s:if></td>
					</tr>
				</s:if>
			</table>
			</td>
			<td width="30%">
			<table class="cartSubTable">
				<tr>
					<td colspan="2">&nbsp</td>
				</tr>
				<tr>
					<td class="themeSubHeader" colspan="2"><s:text
						name="userNameAndTitle" /></td>
				</tr>
				<tr>
					<td class="boldText textAlignLeft"><s:text name="RB_title" />:</td>
					<td><s:select tabindex="45" name="title"
						disabled="%{#isDisabled}" list="%{getTitle()}"
						value="%{#customercontact.getAttribute('Title')}" /></td>
				</tr>
				<tr>
					<td class="boldText textAlignLeft"><s:text name="RB_firstName" />:</td>
					<td><s:textfield tabindex="50" name="firstName"
						disabled="%{#isDisabled}"
						value='%{#customercontact.getAttribute("FirstName")}' /></td>
				</tr>
				<tr>
					<td class="boldText textAlignLeft"><s:text name="RB_lastName" />:</td>
					<td><s:textfield tabindex="55" name="lastName"
						disabled="%{#isDisabled}"
						value='%{#customercontact.getAttribute("LastName")}' /></td>
				</tr>
				<tr>
					<td class="boldText textAlignLeft"><s:text name="RB_jobTitle" />:</td>
					<td><s:textfield tabindex="60" name="jobTitle"
						disabled="%{#isDisabled}"
						value='%{#customercontact.getAttribute("JobTitle")}' /></td>
				</tr>
				<tr>
					<td class="boldText textAlignLeft"><s:text name="RB_EmailID" />:</td>
					<td><s:textfield tabindex="65" name="emailId"
						disabled="%{#isDisabled}"
						value='%{#customercontact.getAttribute("EmailID")}' /></td>
				</tr>
				<tr>
					<td class="boldText textAlignLeft"><s:text name="RB_deptName" />:</td>
					<td><s:textfield tabindex="70" name="deptName"
						disabled="%{#isDisabled}"
						value='%{#customercontact.getAttribute("Department")}' /></td>
				</tr>
			</table>
			</td>
			<td width="35%">
			<table class="cartSubTable">
				<tr>
					<td colspan="4">&nbsp</td>
				</tr>
				<tr>
					<td class="themeSubHeader" colspan="4"><s:text
						name="userRoles" /></td>
				</tr>
				<tr>
					<td><s:checkbox tabindex="75" name='test' fieldValue="test123"
						value="%{isInUserGroup('BUYER-USER')}" disabled="%{#userExists}" /></td>
					<td class="boldText textAlignLeft"><s:text name="buyerUser" /></td>
				</tr>
				<s:if test='%{isAdminListEnabled()}'>
					<s:set name="checkBoxDisable" value='%{true}' />
				</s:if>
				<s:else>
					<s:set name="checkBoxDisable" value='%{false}' />
				</s:else>
				<s:if
					test='%{!((isAdminListEnabled()) && (!isInUserGroup("BUYER-ADMIN")))}'>
					<tr>
						<td><s:checkbox tabindex="80" name='buyerAdmin'
							fieldValue="true" value='%{isInUserGroup("BUYER-ADMIN")}'
							disabled='%{#checkBoxDisable || #isDisabled}' /></td>
						<td class="boldText textAlignLeft"><s:text name="buyerAdmin" /></td>
					</tr>
				</s:if>
				<s:if
					test='%{!((isAdminListEnabled()) && (!isInUserGroup("BUYER-APPROVER")))}'>
					<tr>
						<td><s:checkbox tabindex="85" name='buyerApprover'
							value='%{isInUserGroup("BUYER-APPROVER")}' fieldValue="true"
							disabled='%{#checkBoxDisable || #isDisabled}' /></td>
						<td class="boldText textAlignLeft"><s:text
							name="buyerApprover" /></td>
					</tr>
				</s:if>
				
<!--			<s:if test='%{#viewInvoices=="T"}'>
					<tr>
						<td><s:checkbox tabindex="85" name='viewInvoices'
							value='true' fieldValue="true" disabled='false' /></td>
						<td class="boldText textAlignLeft"><s:text
							name="View Invoices" /></td>
					</tr>
				</s:if>
				<s:else>
        			<tr>
						<td><s:checkbox tabindex="85" name='viewInvoices'
							value='false' fieldValue="true" disabled='false' /></td>
						<td class="boldText textAlignLeft"><s:text
							name="View Invoices" /></td>
					</tr>
      			</s:else>
      			<s:if test='%{#estimator=="T"}'>
					<tr>
						<td><s:checkbox tabindex="86" name='estimator'
							value='true' fieldValue="true" disabled='false' /></td>
						<td class="boldText textAlignLeft"><s:text
							name="Estimator" /></td>
					</tr>
				</s:if>
				<s:else>
					<tr>
						<td><s:checkbox tabindex="86" name='estimator'
							value='false' fieldValue="true" disabled='false' /></td>
						<td class="boldText textAlignLeft"><s:text
							name="Estimator" /></td>
					</tr>
				</s:else>
				<s:if test='%{#stockCheckWebservice=="T"}'>
					<tr>
						<td><s:checkbox tabindex="87" name='stockCheckWebservice'
							value='true' fieldValue="true" disabled='false' /></td>
						<td class="boldText textAlignLeft"><s:text
							name="Stock Check Webservice" /></td>
					</tr>
				</s:if>
				<s:else>
					<tr>
						<td><s:checkbox tabindex="87" name='stockCheckWebservice'
							value='false' fieldValue="true" disabled='false' /></td>
						<td class="boldText textAlignLeft"><s:text
							name="Stock Check Webservice" /></td>
					</tr>
				</s:else> -->
				<tr>
					<td>
						<s:checkbox name="viewPrices" fieldValue="true" value="isViewPrices()" />
					</td>
					<td  class="boldText textAlignLeft">
						<s:text name="View Prices" />
					</td>
				</tr>
				<s:if test='%{#extnElem.getAttribute("ExtnViewPricesFlag") == "Y"}'>
					<tr>
						<td>
							<s:checkbox name="viewReports" fieldValue="true" value='isViewReports()' />
						</td>
						<td  class="boldText textAlignLeft">
							<s:text name="View Reprots" />
						</td>
					</tr>
				</s:if>
				<s:else>
					<tr style="display: none;">
						<td>
							<s:checkbox name="viewReports" fieldValue="true" value='isViewReports()' 
								disabled="true" />
						</td>
						<td  class="boldText textAlignLeft">
							<s:text name="View Reports" />
						</td>
					</tr>
				</s:else>
				<s:if test='%{#punchoutUser=="T"}'>
					<tr>
						<td><s:checkbox tabindex="88" name='punchoutUsers'
							value='true' fieldValue="true" disabled='false' /></td>
						<td class="boldText textAlignLeft"><s:text
							name="Punchout Users" /></td>
					</tr>
				</s:if>
				<s:else>
					<tr>
						<td><s:checkbox tabindex="88" name='punchoutUsers'
							value='false' fieldValue="true" disabled='false' /></td>
						<td class="boldText textAlignLeft"><s:text
							name="Punchout Users" /></td>
					</tr>
				</s:else>

				<tr>
					<td class="themeSubHeader" colspan="4"><s:text
						name="RB_statuses" /></td>
				</tr>
				<tr>
					<td class="boldText textAlignLeft"><s:text name="RB_Status" />:</td>
					<td><s:select tabindex="90" disabled="%{#isDisabled}"
						name="status" headerKey="1" value="%{getContactStatus()}"
						list="getStatusList()" /></td>
					<td class="boldText textAlignLeft"><s:text name="RB_EffStatus" />:</td>
					<td><s:property value="%{getEffectiveStatus()}" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<!-- STARTS - Customer-User Profile Changes - adsouza -->
		<tr>
			<td colspan="3">
				<table>

				<tr>
					<td class="boldText textAlignLeft"><s:text
						name="AdditionalEmailAddresses" />:</td>
					<td><s:select name="AddnlEmailAddrList" id="AddnlEmailAddrList" size="5" list="AddnlEmailAddrList"></s:select>
					<s:hidden name="AddnlEmailAddrText" id="AddnlEmailAddrText" value='%{#extnElem.getAttribute("ExtnAddnlEmailAddrs")}' />
					</td>	
					<td>
						<table>
						<tr>
							<td><s:textfield id='NewEmailAddr' name='NewEmailAddr'  tabindex="" value=''/>
							<input type="button" value="Add To List" onclick="javascript:addEmailToList()"/></td>
						</tr>
						<tr><td></td></tr>
						<tr>
							<td><input type="button" value="Remove From List" onclick="javascript:removeEmailFromList()"/></td>
						</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td class="boldText textAlignLeft"><s:text
						name="POList" />:</td>
					<td><s:select name="POList" id="POList" size="5" list="POList"></s:select>
					<s:hidden name="POListText"  id="POListText" value='%{#extnElem.getAttribute("ExtnPOList")}' />
					</td>	
					<td>
						<table>
						<tr>
							<td><s:textfield id='NewPO' name='NewPO'  tabindex="" value=''/>
							<input type="button" value="Add To List" onclick="javascript:addPOToList()"/></td>
						</tr>
						<tr><td></td></tr>
						<tr>
							<td><input type="button" value="Remove From List" onclick="javascript:removePOFromList()"/></td>
						</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td class="boldText textAlignLeft"><s:text
						name="Email Format" />:</td>
					<td>
						<!-- s:select tabindex="90" 
						name="OrderEmailOption" headerKey="1" 
						list="getEmailFormatOptions()" / -->
						<s:textfield tabindex="" name="PrefCat" id="PrefCat" readonly="true"
						value='%{#extnElem.getAttribute("ExtnOrderEmailFormat")}' />
						</td>
				</tr>
				<tr>
					<td class="boldText textAlignLeft"><s:text
						name="Emai Address For Invoice" />:</td>
					<td>
						<s:textfield tabindex="" name="invoiceEmailID" id="invoiceEmailID" 
						value='%{#extnElem.getAttribute("ExtnInvoiceEMailID")}' />
						</td>
				</tr>	
				<tr>
					<td class="boldText textAlignLeft"><s:text
						name="Preferred Catalog" />:</td>
					<td><s:textfield tabindex="" name="PrefCat" id="PrefCat" readonly="true"
						value='%{#extnElem.getAttribute("ExtnPrefCatalog")}' /></td>
					
					<td class="boldText textAlignLeft"><s:text
						name="Default ShipTo" />:</td>
					<td>
					<s:select theme="simple" name="defaultShipTo" tabindex="91"
						headerKey="" headerValue="-- Select Default ship to --"
						list="assignedCustomersMap" 
						listKey="key"
						listValue="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@formatBillToShipToCustomer(key)"
						cssClass='hidden' value='%{#extnElem.getAttribute("ExtnDefaultShipTo")}'>
					</s:select>						
				</tr>
				<tr>
					<td  class="boldText textAlignLeft">
						<s:text name="RecieveOrderConfirmationEmail" />
					</td>
					<td>
						<s:checkbox name="orderConfirmationEmailFlag" fieldValue="true" 
							value='isSendOrderConfEmail()' />
					</td>
					<td  class="boldText textAlignLeft">
						<s:text name="RecieveOrderCancellationEmail" />
					</td>
					<td>
						<s:checkbox name="orderCancellationEmailFlag" fieldValue="true" 
							value='isSendOrderCancelEmail()' />
					</td>
				</tr>
				<tr>
					
					<td  class="boldText textAlignLeft">
						<s:text name="RecieveOrderUpdateEmailByCSR" />
					</td>
					<td>
						<s:checkbox name="orderUpdateEmailFlag" fieldValue="true" 
						value="%{#extnElem.getAttribute('ExtnOrderConfEmailFlag')}" />
					</td>
					
					<td  class="boldText textAlignLeft">
						<s:text name="RecieveOrderShipmentEmail" />
					</td>
					<td>
						<s:checkbox name="orderShipmentEmailFlag" fieldValue="true" 
						value='isSendOrderShipEmail()'	/>
					</td>
				</tr>
				<tr>
					
					<td  class="boldText textAlignLeft">
						<s:text name="RecieveBackorderEmail" />
					</td>
					<td>
						<s:checkbox name="backorderEmailFlag" fieldValue="true" 
						value='isSendBackOrderEmail()' />
					</td>
					<td class="boldText textAlignLeft"><s:text name="B2B Catalog View" />:</td>
					<td><s:radio name="b2bCatalogView" list="b2bCatalogViewMap" value="defaultB2bCatalogView" /></td>
				</tr>					
				</table>
			</td>
		</tr>
		
		<!-- ENDS - Customer-User Profile Changes - adsouza -->
	</table>

	</div>

	<table class="listTableHeader2" id="listTableHeader2">
		<tr>
			<td width="11" class="altBorder textAlignLeft"><s:a
				tabindex="95" href="#" id="anchortoggleAddBook">
				<div id="toggleAddBook" class="">&nbsp;</div>
			</s:a></td>
			<td class="listTableHeaderText"><s:text name="address.Book" /></td>
		</tr>
	</table>

	<div id="addressBookTable"><!-- content for second section --> <!-- div id="addressBookTable" -->

	<s:set name="defaultAddressList" value="getDefaultContactAddressList()" />
	<s:set name="nonDefaultAddressList"
		value="getOtherContactAddressList()" /> <s:if
		test='%{(#defaultAddressList !=null && #defaultAddressList.size() >0) || (#nonDefaultAddressList != null && #nonDefaultAddressList.size() >0)}'>
		<s:set name='listEmty' value="%{'TRUE'}" />
	</s:if> <s:if test='%{#hasAccessToEdit != "TRUE" || #listEmty == null}'>
		<s:set name='isButtonDisabled' value="%{'true'}" />
	</s:if> <s:else>
		<s:set name='isButtonDisabled' value="%{'false'}" />
	</s:else>
	<table width="96%" class="listTableBody3 padding-left3">
		<tr>
			<td class="textAlignLeft" colspan="10">
			<div id="wwgrp_myAccount_addressbuttons" class="wwgrp">
			<div id="wwctrl_myAccount_addressbuttons" class="wwctrl"><s:if
				test='%{#hasAccessToEdit == "TRUE"}'>
				<s:if test='%{#listEmty != null && #listEmty == "TRUE"}'>
					<input tabindex="655" type="button"
						<s:if test='%{#isButtonDisabled}'>disabled</s:if> name="deleteAdd"
						value='<s:text name="delete"/>' class="submitBtnBg1"
						onclick="deleteAddress('<s:text name="confirmAddressDelete" />'); return false" />&nbsp;
	        	<input tabindex="660" type="button"
						<s:if test='%{#isButtonDisabled}'>disabled</s:if>
						name="duplicateAdd" value='<s:text name="duplicate"/>'
						class="submitBtnBg1"
						onclick="duplicateAddress('<s:text name="confirmAddressDuplicate" />'); return false" />&nbsp;
        	</s:if>
				<input tabindex="110" type="button"
					onClick="javascript:openAddressLightBox('ajax-body-1', 'modalDialogPanel2','<s:url value="/profile/user/createAddress.action"><s:param name="customerId" value='%{customerId}'/><s:param name="customerContactId" value='%{customerContactId}'/><s:param name="defShipToAddId" value='%{defShipToAddId}'/><s:param name="defBillToAddId" value='%{defBillToAddId}'/><s:param name="defSoldToAddId" value='%{defSoldToAddId}'/></s:url>')"
					name="addNew" value="<s:text name='add.New'/>" class="submitBtnBg1" />
			</s:if> &nbsp;</div>
			</div>

			</td>
		</tr>
		<tr>
			<td class="textAlignLeft subHeaderText" colspan="10"><s:text
				name="defaultUserAddressHeader" /></td>
		</tr>
		<tr valign="bottom">
			<td colspan="10"><s:set name='defAddressTabIndexStart'
				value='150' /> <s:action name="buildSimpleTable"
				executeResult="true" namespace="/common">
				<s:param name="id" value="defaultAddressTable" />
				<s:param name="cssClass" value="'listTableBody3 padding-left3'" />
				<s:param name="summary" value="'User Default address List Table'" />
				<s:param name="iterable" value="getDefaultAddressList()" />
				<s:param name="columnSpecs[0].label" value="'AddressNamelabel'" />
				<s:param name="columnSpecs[0].fieldCssClass"
					value="'draftOrderListTable'" />
				<s:param name="columnSpecs[0].labelCssClass" value="'tablecolumn'" />
				<s:param name="columnSpecs[0].dataCellBuilder"
					value="'userAddressListAnchor'" />
				<s:param
					name="columnSpecs[0].dataCellBuilderProperties['namespace']"
					value="'/profile/user'" />
				<s:param
					name="columnSpecs[0].dataCellBuilderProperties['customerId']"
					value="%{#customer.getAttribute('CustomerID')}" />
				<s:param
					name="columnSpecs[0].dataCellBuilderProperties['customerContactId']"
					value="%{#customercontact.getAttribute('CustomerContactID')}" />
				<s:param
					name="columnSpecs[0].dataCellBuilderProperties['contactListCount']"
					value="%{getDefaultContactAddressList().size()}" />
				<s:param
					name="columnSpecs[0].dataCellBuilderProperties['defShipToAddId']"
					value='%{getDefShipToAddId()}' />
				<s:param
					name="columnSpecs[0].dataCellBuilderProperties['defBillToAddId']"
					value='%{getDefBillToAddId()}' />
				<s:param
					name="columnSpecs[0].dataCellBuilderProperties['defSoldToAddId']"
					value='%{getDefSoldToAddId()}' />
				<s:param
					name="columnSpecs[0].dataCellBuilderProperties['defaultPanel']"
					value='%{"true"}' />
				<s:param
					name="columnSpecs[0].dataCellBuilderProperties['tabIndexCounter']"
					value='%{#defAddressTabIndexStart}' />

				<s:param name="columnSpecs[1].label" value="'Typelabel'" />
				<s:param name="columnSpecs[1].dataField" value="'AddressType'" />
				<s:param name="columnSpecs[1].fieldCssClass"
					value="'draftOrderListTable'" />
				<s:param name="columnSpecs[1].labelCssClass=tablecolumn" />
				<s:param name="columnSpecs[1].columnId" value="'AddressType'" />
				<s:param name="columnSpecs[1].dataCellBuilder"
					value="'userAddressIsCommertialAnchor'" />
				<s:param
					name="columnSpecs[1].dataCellBuilderProperties['namespace']"
					value="'/profile/user'" />
				<s:param
					name="columnSpecs[1].dataCellBuilderProperties['columnName']"
					value="'IsCommercialAddress'" />

				<s:param name="columnSpecs[2].label" value="'AddressLine1label'" />
				<s:param name="columnSpecs[2].dataField" value="'AddressLine1'" />
				<s:param name="columnSpecs[2].fieldCssClass"
					value="'draftOrderListTable'" />
				<s:param name="columnSpecs[2].labelCssClass" value="'tablecolumn'" />
				<s:param name="columnSpecs[2].columnId" value="'AddressLine1'" />
				<s:param name="columnSpecs[2].dataCellBuilder"
					value="'userAddressPersonInfoAnchor'" />
				<s:param
					name="columnSpecs[2].dataCellBuilderProperties['namespace']"
					value="'/profile/user'" />
				<s:param
					name="columnSpecs[2].dataCellBuilderProperties['columnName']"
					value="'AddressLine1'" />

				<s:param name="columnSpecs[3].label" value="'AddressLine2label'" />
				<s:param name="columnSpecs[3].dataField" value="'AddressLine2'" />
				<s:param name="columnSpecs[3].fieldCssClass"
					value="'draftOrderListTable'" />
				<s:param name="columnSpecs[3].labelCssClass" value="'tablecolumn'" />
				<s:param name="columnSpecs[3].columnId" value="'AddressLine2'" />
				<s:param name="columnSpecs[3].dataCellBuilder"
					value="'userAddressPersonInfoAnchor'" />
				<s:param
					name="columnSpecs[3].dataCellBuilderProperties['namespace']"
					value="'/profile/user'" />
				<s:param
					name="columnSpecs[3].dataCellBuilderProperties['columnName']"
					value="'AddressLine2'" />

				<s:param name="columnSpecs[4].label" value="'Citylabel'" />
				<s:param name="columnSpecs[4].dataField" value="'City'" />
				<s:param name="columnSpecs[4].fieldCssClass"
					value="'draftOrderListTable'" />
				<s:param name="columnSpecs[4].labelCssClass" value="'tablecolumn'" />
				<s:param name="columnSpecs[4].columnId" value="'City'" />
				<s:param name="columnSpecs[4].dataCellBuilder"
					value="'userAddressPersonInfoAnchor'" />
				<s:param
					name="columnSpecs[4].dataCellBuilderProperties['namespace']"
					value="'/profile/user'" />
				<s:param
					name="columnSpecs[4].dataCellBuilderProperties['columnName']"
					value="'City'" />

				<s:param name="columnSpecs[5].label" value="'StateProvincelabel'" />
				<s:param name="columnSpecs[5].dataField" value="'State'" />
				<s:param name="columnSpecs[5].fieldCssClass"
					value="'draftOrderListTable'" />
				<s:param name="columnSpecs[5].labelCssClass" value="'tablecolumn'" />
				<s:param name="columnSpecs[5].columnId" value="'State'" />
				<s:param name="columnSpecs[5].dataCellBuilder"
					value="'userAddressPersonInfoAnchor'" />
				<s:param
					name="columnSpecs[5].dataCellBuilderProperties['namespace']"
					value="'/profile/user'" />
				<s:param
					name="columnSpecs[5].dataCellBuilderProperties['columnName']"
					value="'State'" />

				<s:param name="columnSpecs[6].label" value="'PostalCodelabel'" />
				<s:param name="columnSpecs[6].dataField" value="'ZipCode'" />
				<s:param name="columnSpecs[6].fieldCssClass"
					value="'draftOrderListTable'" />
				<s:param name="columnSpecs[6].labelCssClass" value="'tablecolumn'" />
				<s:param name="columnSpecs[6].columnId" value="'ZipCode'" />
				<s:param name="columnSpecs[6].dataCellBuilder"
					value="'userAddressPersonInfoAnchor'" />
				<s:param
					name="columnSpecs[6].dataCellBuilderProperties['namespace']"
					value="'/profile/user'" />
				<s:param
					name="columnSpecs[6].dataCellBuilderProperties['columnName']"
					value="'ZipCode'" />

				<s:param name="columnSpecs[7].label" value="'Countrylabel'" />
				<s:param name="columnSpecs[7].dataField" value="'Country'" />
				<s:param name="columnSpecs[7].fieldCssClass"
					value="'draftOrderListTable'" />
				<s:param name="columnSpecs[7].labelCssClass" value="'tablecolumn'" />
				<s:param name="columnSpecs[7].columnId" value="'Country'" />
				<s:param name="columnSpecs[7].dataCellBuilder"
					value="'userAddressPersonInfoAnchor'" />
				<s:param
					name="columnSpecs[7].dataCellBuilderProperties['namespace']"
					value="'/profile/user'" />
				<s:param
					name="columnSpecs[7].dataCellBuilderProperties['columnName']"
					value="'Country'" />

			</s:action></td>
		</tr>
		<tr>
			<td class="textAlignLeft subHeaderText" colspan="10"><s:text
				name="otherUserAddressHeader" /></td>
		</tr>
		<tr valign="bottom">
			<td colspan="10"><s:set name='otherddressTabIndexStart'
				value='300' /> <s:action name="buildSimpleTable"
				executeResult="true" namespace="/common">
				<s:param name="id" value="otherAddressTable" />
				<s:param name="cssClass" value="'listTableBody3 padding-left3'" />
				<s:param name="summary" value="'User Other address List Table'" />
				<s:param name="iterable" value="getOtherAddressList()" />
				<s:param name="columnSpecs[0].label" value="'AddressNamelabel'" />
				<s:param name="columnSpecs[0].fieldCssClass"
					value="'draftOrderListTable'" />
				<s:param name="columnSpecs[0].labelCssClass" value="'tablecolumn'" />
				<s:param name="columnSpecs[0].dataCellBuilder"
					value="'userAddressListAnchor'" />
				<s:param
					name="columnSpecs[0].dataCellBuilderProperties['namespace']"
					value="'/profile/user'" />
				<s:param
					name="columnSpecs[0].dataCellBuilderProperties['customerId']"
					value="%{#customer.getAttribute('CustomerID')}" />
				<s:param
					name="columnSpecs[0].dataCellBuilderProperties['customerContactId']"
					value="%{#customercontact.getAttribute('CustomerContactID')}" />
				<s:param
					name="columnSpecs[0].dataCellBuilderProperties['contactListCount']"
					value="%{getOtherContactAddressList().size()}" />
				<s:param
					name="columnSpecs[0].dataCellBuilderProperties['defShipToAddId']"
					value='%{getDefShipToAddId()}' />
				<s:param
					name="columnSpecs[0].dataCellBuilderProperties['defBillToAddId']"
					value='%{getDefBillToAddId()}' />
				<s:param
					name="columnSpecs[0].dataCellBuilderProperties['defSoldToAddId']"
					value='%{getDefSoldToAddId()}' />
				<s:param
					name="columnSpecs[0].dataCellBuilderProperties['defaultPanel']"
					value='%{"false"}' />
				<s:param
					name="columnSpecs[0].dataCellBuilderProperties['tabIndexCounter']"
					value='%{#otherddressTabIndexStart}' />

				<s:param name="columnSpecs[1].label" value="'Typelabel'" />
				<s:param name="columnSpecs[1].dataField" value="'AddressType'" />
				<s:param name="columnSpecs[1].fieldCssClass"
					value="'draftOrderListTable'" />
				<s:param name="columnSpecs[1].labelCssClass=tablecolumn" />
				<s:param name="columnSpecs[1].columnId" value="'AddressType'" />
				<s:param name="columnSpecs[1].dataCellBuilder"
					value="'userAddressIsCommertialAnchor'" />
				<s:param
					name="columnSpecs[1].dataCellBuilderProperties['namespace']"
					value="'/profile/user'" />
				<s:param
					name="columnSpecs[1].dataCellBuilderProperties['columnName']"
					value="'IsCommercialAddress'" />

				<s:param name="columnSpecs[2].label" value="'AddressLine1label'" />
				<s:param name="columnSpecs[2].dataField" value="'AddressLine1'" />
				<s:param name="columnSpecs[2].fieldCssClass"
					value="'draftOrderListTable'" />
				<s:param name="columnSpecs[2].labelCssClass" value="'tablecolumn'" />
				<s:param name="columnSpecs[2].columnId" value="'AddressLine1'" />
				<s:param name="columnSpecs[2].dataCellBuilder"
					value="'userAddressPersonInfoAnchor'" />
				<s:param
					name="columnSpecs[2].dataCellBuilderProperties['namespace']"
					value="'/profile/user'" />
				<s:param
					name="columnSpecs[2].dataCellBuilderProperties['columnName']"
					value="'AddressLine1'" />

				<s:param name="columnSpecs[3].label" value="'AddressLine2label'" />
				<s:param name="columnSpecs[3].dataField" value="'AddressLine2'" />
				<s:param name="columnSpecs[3].fieldCssClass"
					value="'draftOrderListTable'" />
				<s:param name="columnSpecs[3].labelCssClass" value="'tablecolumn'" />
				<s:param name="columnSpecs[3].columnId" value="'AddressLine2'" />
				<s:param name="columnSpecs[3].dataCellBuilder"
					value="'userAddressPersonInfoAnchor'" />
				<s:param
					name="columnSpecs[3].dataCellBuilderProperties['namespace']"
					value="'/profile/user'" />
				<s:param
					name="columnSpecs[3].dataCellBuilderProperties['columnName']"
					value="'AddressLine2'" />

				<s:param name="columnSpecs[4].label" value="'Citylabel'" />
				<s:param name="columnSpecs[4].dataField" value="'City'" />
				<s:param name="columnSpecs[4].fieldCssClass"
					value="'draftOrderListTable'" />
				<s:param name="columnSpecs[4].labelCssClass" value="'tablecolumn'" />
				<s:param name="columnSpecs[4].columnId" value="'City'" />
				<s:param name="columnSpecs[4].dataCellBuilder"
					value="'userAddressPersonInfoAnchor'" />
				<s:param
					name="columnSpecs[4].dataCellBuilderProperties['namespace']"
					value="'/profile/user'" />
				<s:param
					name="columnSpecs[4].dataCellBuilderProperties['columnName']"
					value="'City'" />

				<s:param name="columnSpecs[5].label" value="'StateProvincelabel'" />
				<s:param name="columnSpecs[5].dataField" value="'State'" />
				<s:param name="columnSpecs[5].fieldCssClass"
					value="'draftOrderListTable'" />
				<s:param name="columnSpecs[5].labelCssClass" value="'tablecolumn'" />
				<s:param name="columnSpecs[5].columnId" value="'State'" />
				<s:param name="columnSpecs[5].dataCellBuilder"
					value="'userAddressPersonInfoAnchor'" />
				<s:param
					name="columnSpecs[5].dataCellBuilderProperties['namespace']"
					value="'/profile/user'" />
				<s:param
					name="columnSpecs[5].dataCellBuilderProperties['columnName']"
					value="'State'" />

				<s:param name="columnSpecs[6].label" value="'PostalCodelabel'" />
				<s:param name="columnSpecs[6].dataField" value="'ZipCode'" />
				<s:param name="columnSpecs[6].fieldCssClass"
					value="'draftOrderListTable'" />
				<s:param name="columnSpecs[6].labelCssClass" value="'tablecolumn'" />
				<s:param name="columnSpecs[6].columnId" value="'ZipCode'" />
				<s:param name="columnSpecs[6].dataCellBuilder"
					value="'userAddressPersonInfoAnchor'" />
				<s:param
					name="columnSpecs[6].dataCellBuilderProperties['namespace']"
					value="'/profile/user'" />
				<s:param
					name="columnSpecs[6].dataCellBuilderProperties['columnName']"
					value="'ZipCode'" />

				<s:param name="columnSpecs[7].label" value="'Countrylabel'" />
				<s:param name="columnSpecs[7].dataField" value="'Country'" />
				<s:param name="columnSpecs[7].fieldCssClass"
					value="'draftOrderListTable'" />
				<s:param name="columnSpecs[7].labelCssClass" value="'tablecolumn'" />
				<s:param name="columnSpecs[7].columnId" value="'Country'" />
				<s:param name="columnSpecs[7].dataCellBuilder"
					value="'userAddressPersonInfoAnchor'" />
				<s:param
					name="columnSpecs[7].dataCellBuilderProperties['namespace']"
					value="'/profile/user'" />
				<s:param
					name="columnSpecs[7].dataCellBuilderProperties['columnName']"
					value="'Country'" />

			</s:action></td>
		</tr>
	</table>

	</div>
	<!-- Phone Book -->

	<table class="listTableHeader2" id="listTableHeader1">
		<tr>
			<td width="11" class="altBorder textAlignLeft"><s:a
				tabindex="705" href="#" id="anchortogglePhoneBook">
				<div id="togglePhoneBook" class="">&nbsp;</div>
			</s:a></td>
			<td class="listTableHeaderText"><s:text name="my.Phone.Book" /></td>
		</tr>
	</table>

	<div id="phoneBookTable"><!-- content for thrid section -->

	<table width="96%" class="listTableBody3 padding-left3">

		<tr valign="top">
			<td width="35%">
			<table class="cartSubTable">
				<tr>
					<td class="boldText textAlignLeft"><s:text name="RB_dayPhone" />:</td>
					<td><s:textfield tabindex="710" name="dayPhone"
						disabled="%{#isDisabled}"
						value="%{#customercontact.getAttribute('DayPhone')}" /></td>
				</tr>
				<tr>
					<td class="boldText textAlignLeft"><s:text
						name="RB_eveningPhone" />:</td>
					<td><s:textfield tabindex="715" name="eveningPhone"
						disabled="%{#isDisabled}"
						value="%{#customercontact.getAttribute('EveningPhone')}" /></td>
				</tr>
			</table>
			</td>
			<td width="30%">
			<table class="cartSubTable">

				<tr>
					<td class="boldText textAlignLeft"><s:text name="RB_cell" />:</td>
					<td><s:textfield tabindex="720" name="mobilePhone"
						disabled="%{#isDisabled}"
						value="%{#customercontact.getAttribute('MobilePhone')}" /></td>

				</tr>
				<tr>
					<td class="boldText textAlignLeft"><s:text name="RB_dayFax" />:</td>
					<td><s:textfield tabindex="725" name="dayFaxNo"
						disabled="%{#isDisabled}"
						value="%{#customercontact.getAttribute('DayFaxNo')}" /></td>
				</tr>
			</table>
			</td>
			<td width="35%">
			<table class="cartSubTable">
				<tr>
					<td class="boldText textAlignLeft"><s:text
						name="RB_eveningFax" />:</td>
					<td><s:textfield tabindex="730" name="eveningFaxNo"
						disabled="%{#isDisabled}"
						value="%{#customercontact.getAttribute('EveningFaxNo')}" /></td>
				</tr>

			</table>
			</td>
		</tr>
	</table>

	</div>

	<!-- End Phone Book --> <!-- Buyer Admin List --> <s:if
		test="%{showAdminList}">
		<s:set name='adoc' value="getUserList().getOwnerDocument()" />
		<s:set name='buyerAdminList'
			value='#util.getElements(#adoc,"//CustomerContactList/CustomerContact")' />

		<table class="listTableHeader2" id="listTableHeader1">
			<tr>
				<td width="11" class="altBorder textAlignLeft"><s:a
					tabindex="735" href="#" id="anchortoggleAdminList">
					<div id="toggleAdminList" class="">&nbsp;</div>
				</s:a></td>
				<td class="listTableHeaderText"><s:text name="buyer.Admin.List" /></td>
			</tr>
		</table>

		<div id="adminListTable"
			style="width: 96%; margin-left: auto; margin-right: auto; display: none;">


		<s:action name="buildSimpleTable" executeResult="true"
			namespace="/common">
			<s:param name="id" value="adminListTableComponent" />
			<s:param name="cssClass" value="'listTableBody3 padding-left3'" />
			<s:param name="summary" value="'Admin List Table'" />
			<s:param name="iterable" value="#buyerAdminList" />

			<s:param name="columnSpecs[0].label" value="'LastNameLabel'" />
			<s:param name="columnSpecs[0].dataField" value="'LastName'" />
			<s:param name="columnSpecs[0].columnId" value="'LastName'" />

			<s:param name="columnSpecs[1].label" value="'FirstNameLabel'" />
			<s:param name="columnSpecs[1].dataField" value="'FirstName'" />
			<s:param name="columnSpecs[1].columnId" value="'FirstName'" />

			<s:param name="columnSpecs[2].label" value="'BusinessPhoneLabel'" />
			<s:param name="columnSpecs[2].dataField" value="'DayPhone'" />
			<s:param name="columnSpecs[2].columnId" value="'DayPhone'" />

			<s:param name="columnSpecs[3].label" value="'EmailAddressLabel'" />
			<s:param name="columnSpecs[3].dataField" value="'EmailID'" />
			<s:param name="columnSpecs[3].columnId" value="'EmailID'" />


		</s:action></div>
	</s:if> <!-- End Buyer Admin list -->
	<div>
	<table class="listTableBody3 padTop" width="96%">
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td class="textAlignLeft"><span id="expandTable2"><a
				tabindex="905" href="#" onClick="expandOrCollapse();" id=""><s:text
				name="expand.Collapse" /></a></span></td>
			<td class="textAlignRight"><s:if
				test='%{#hasAccessToEdit == "TRUE"}'>
				<s:submit tabindex="904" action="saveUserInfo" name="Save"
					key="save.changes" cssClass="submitBtnBg2" />
			</s:if></td>
		</tr>
	</table>
	</div>

	<p>&nbsp;</p>



	</div>
	<div><!-- // tabContent end -->
	<div>
	<table width="96%">
		<tr>
			<s:if
				test="@com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer@getInstance().isAuthorized(#userListResId,#ctxt)">
				<td class="textAlignRight"><a tabindex="915"
					href="<s:url value='/profile/user/getUserList.action' includeParams='none'>
                <s:param name='customerID' value='%{customerId}'/></s:url>"><s:text
					name="ViewUsers" /></a></td>
			</s:if>
		</tr>
		<tr>
			<td>
			<p>&nbsp;</p>
			</td>
		</tr>
	</table>
	</div>
</s:form>
<div><!-- // t2-main-content end --></div>
<!-- // container end --></div>
<div class="t2-footer commonFooter" id="t2-footer"><!-- add content here for footer -->

<s:action name="footer" executeResult="true" namespace="/common" /></div>
<!-- // footer end --></div>
<!-- // main end --> <!-- div for light box --> <swc:dialogPanel
	title="${AddressInformationTitle}" isModal="true"
	id="modalDialogPanel2">
	<s:form name="manageAddress" id="manageAddress" method="post">
		<div id="ajax-body-1"></div>
	</s:form>
</swc:dialogPanel> <swc:dialogPanel title="${AddressInformationTitle}" isModal="true"
	id="modalDialogMessagePanel">
	<div id="ajax-body-message"></div>
</swc:dialogPanel> <!-- end div for light box -->
</body>
</swc:html>
