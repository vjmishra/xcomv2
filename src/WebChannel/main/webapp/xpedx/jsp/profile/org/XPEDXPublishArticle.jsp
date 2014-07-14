<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs’. 
    This is to avoid a defect in Struts that’s creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts’ OGNL statements. --%>
<s:set name='_action' value='[0]'/>
<s:set name='ctxt' value="#_action.getWCContext()" />
<s:set name='userListResId' value='"/swc/profile/ManageUserList"'/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<swc:html isXhtml="true">
<head>
  <swc:head/>
  <title><s:property value="wCContext.storefrontId" /> - <s:text name="my.Account.page"/></title>
  <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/user/my-account<s:property value='#wcUtil.xpedxBuildKey' />.css" />
  <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/common/notes-list<s:property value='#wcUtil.xpedxBuildKey' />.css" />
  <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/user/userPreferences<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
</head>
    <s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
    <s:set name='xmlUtils' value="#_action.getXMLUtils()" />
    <s:set name='formAction' value='"xpedxAddArticle.action"'/>
    <s:set name='formNamespace' value='"/profile/org"'/>
    <s:set name='orderNoteFlag' value='"N"'/>
    <s:set name='orgNoteFlag' value='"N"'/>
    <s:set name='userNoteFlag' value='"Y"'/>
    <s:set name='addNoteFlag' value='"Y"'/>
    <s:set name="customerId" value="customerId"/>
    <s:set name="customerContactID" value="customerContactID"/>
    <s:set name='pageLabel' value="Article List"/>

<body>

<div id="main">
<!-- begin header -->
    <div class="t2-header commonHeader" id="headerContainer"><!-- add content here for header information -->
      <s:action name="header" executeResult="true" namespace="/common" />
    </div>
    <!-- // header end -->

<!-- Begin Navigation include Tab -->
<s:action name="NavigationTab" executeResult="true" namespace="/profile/org" >
	<s:param name="navSelectedTab">UpdateOrgProfile</s:param>
</s:action>
<!-- End Navigation include Tab -->


<div class="container">
<div class="t2-mainContent" id="t2-mainContent"><!-- add content here for main content -->
    <div>
    <table width="98%">
        <tr>
            <td width="75%"><span class="headerText padding-left3">
            <s:property value="#pageLabel" /></span></td>
            <s:if test="@com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer@getInstance().isAuthorized(#userListResId,#ctxt)">
            <td align="right" class="padding-right3"><a tabindex="925"
                href="<s:url value='/profile/user/getUserList.action'><s:param name="customerID" value="%{#customerId}"/></s:url>"><s:text name="ViewUsers"/></a></td>
            </s:if>
        </tr>
    </table>
    </div>



<!-- Begin include Tab -->
<s:action name="customerNavigationTab" executeResult="true"	namespace="/profile/org">
	<s:param name="selectedTab">PublishArticle</s:param>
	<s:param name="customerId" value="%{#customerId}"/>
  	<s:param name="organizationCode" value="%{WCContext.customerMstrOrg}"/>
</s:action>
<!-- End include Tab -->

<div class="tabContent"><!-- Product list -->
        <!-- // begin common-content -->
        <s:set name='hasAccessToEdit' value="#_action.getIsChildCustomer()" />
        <s:if test='%{#hasAccessToEdit == "TRUE"}'>
        	<s:set name='addNoteFlag' value='"Y"'/>
        </s:if>
        <s:else>
        	<s:set name='addNoteFlag' value='"N"'/>
        </s:else>
        <s:include value="../../common/XPEDXArticleData.jsp"/>
        <!-- // end common-content -->
</div>
<div>
    <table width="98%">
        <tr>
            <s:if test="@com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer@getInstance().isAuthorized(#userListResId,#ctxt)">
            <td align="right" class="padding-right3"><a tabindex="915"
                href="<s:url value='/profile/user/getUserList.action'><s:param name="customerID" value="%{#customerId}"/></s:url>"><s:text name="ViewUsers"/></a></td>
            </s:if>
        </tr>
        <tr>
		<td>
		<p>&nbsp;</p>
		</td>
	</tr>
    </table>
    </div>
</div><!--  End main content -->
</div> <!-- // container end -->



<div class="t2-footer commonFooter" id="t2-footer">
      <!-- add content here for footer -->
      <s:action name="footer" executeResult="true" namespace="/common" />
</div>
        <!-- // footer end -->
</div><!-- // main end -->


</body>
</swc:html>
