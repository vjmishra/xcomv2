 <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="swc" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs’. 
    This is to avoid a defect in Struts that’s creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts’ OGNL statements. --%>
<s:set name='_action' value='[0]'/>
<s:set name='ctxt' value="#_action.getWCContext()" />
<s:set name='userListResId' value='"/swc/profile/ManageUserList"'/>
<swc:html isXhtml="true">
<head>
 <title>
    <s:property value="wCContext.storefrontId" /> - <s:text name="CustomerProfile"/>
 </title>
<swc:head/>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/user/my-account.css" />
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/profile/profile.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/profile/org/corporateInfo.js"></script>


</head>
 <s:set name='xmlUtils' value="#_action.getXMLUtils()" />
 <s:set name='sdoc' value="#_action.getCustomerOrganizationEle()" />
 <s:set name='customerSchPreferences' value='#xmlUtils.getChildElement(#sdoc, "CustomerSchedulingPreferences")'/>
 <s:set name='ParentCustomer' value='#xmlUtils.getChildElement(#sdoc, "ParentCustomer")'/>
 <s:set name='sBuyerOrg' value='#xmlUtils.getChildElement(#sdoc, "BuyerOrganization")'/>
 <s:set name='hasAccessToEdit' value="#_action.getIsChildCustomer()" />
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


<!-- begin Common header -->
<div class="t2-header commonHeader" id="headerContainer">
      <!-- add content here for header information -->
      <s:action name="header" executeResult="true" namespace="/common" />
</div><!-- //Common header end -->

<!-- Begin Navigation include Tab -->
<s:action name="NavigationTab" executeResult="true" namespace="/profile/org" >
	<s:param name="navSelectedTab">UpdateOrgProfile</s:param>
</s:action>
<!-- End Navigation include Tab -->
 <div class="container"> <!-- // begin t2-product-list -->
 <div class="t2-mainContent" id="t2-mainContent"><!-- add content here for main content -->
<!-- main content header -->
	<div>
	<table width="98%">
		<tr>
			<td width="75%"><span class="headerText padding-left3">
			<s:text name="Update"/>&nbsp;<s:text name='%{#xmlUtils.getAttribute(#sBuyerOrg,"OrganizationName")}'/>&nbsp;<s:text name="corporateinformation"/></span></td>
			<s:if test="@com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer@getInstance().isAuthorized(#userListResId,#ctxt)">
			<td align="right" class="padding-right3"><a tabindex="925"
				href="<s:url value='/profile/user/getUserList.action'><s:param name="customerID" value='%{#xmlUtils.getAttribute(#sdoc,"CustomerID")}'/></s:url>"><s:text name="ViewUsers"/></a></td>
			</s:if>
		</tr>
	</table>
	</div>

<!-- Begin Customer include Tab -->
<s:action name="customerNavigationTab" executeResult="true"	namespace="/profile/org">
	<s:param name="selectedTab">CorporateInfo</s:param>
	<s:param name="customerId" value='#xmlUtils.getAttribute(#sdoc,"CustomerID")'/>
  	<s:param name="organizationCode" value='#xmlUtils.getAttribute(#sdoc,"OrganizationCode")'/>
</s:action> <!-- End include Tab -->

 <!-- Actual tab content starts from here -->
   <div class="tabContent">

   	<div>
		
	</div>

	<div>
	<s:form action="getCustomerCorporateInfo" namespace="/profile/org" validate="true" name="customerCorporateInfohierarchy" id="customerCorporateInfohierarchy" method="post" >
     	<s:hidden name="organizationCode" id="organizationCode" value='%{#xmlUtils.getAttribute(#sdoc,"OrganizationCode")}'  />
		<s:hidden name="#action.namespace" value="/profile/org"/>
		<s:hidden name="#action.name" value="getCustomerCorporateInfo"/>
		 <div>
		    <table class="listTableHeader2" id="listTableHeader1">
		        <tr>
		            <td><span class="listTableHeaderText"><s:text name="Location Administration"/></span></td>
		        </tr>
		    </table>
       	</div>

	   <div id="hierarchy">
			<TABLE class="listTableBody3 padding-left3" width="96%" >
				<TR>
					<TD>
					   <TABLE>
					     <TR>
					        <s:url id="parent" action="getCustomerGeneralInfo.action" >
			  				      <s:param name="customerId" value='#xmlUtils.getAttribute(#ParentCustomer,"CustomerID")'/>
						    </s:url>
					     <TD><s:text name="ParentOrganization"/>  : <s:a href="%{parent}" > <s:property value='#xmlUtils.getAttribute(#ParentCustomer,"CustomerID")'  /> </s:a></TD> </TR>
					     <TR><TD>
					     <s:submit tabindex="60" name="goto" cssClass="submitBtnBg1" key="GotoChild" />
					     </TD><TD>
					     <s:if test='%{#hasAccessToEdit == "TRUE"}'>
				  		 <input type="button"  tabindex="40" name="add" onClick="getNewCorporateInfo('<s:url value="/profile/org/newCustomerProfile.action"><s:param name="parentCustomerId" value='%{#xmlUtils.getAttribute(#sdoc,"CustomerID")}'/></s:url>')" class="submitBtnBg1" value="<s:text name="AddChild"/>" />
				  		 </s:if>
					     </TD></TR>
					         <TR><TD>

					      	<s:set name='Customers' value='childCustomersMap'/>
					      	<s:if test='%{ null !=#Customers }'>
			    				 <s:select tabindex="50" size="10" 
							        name="customerId"
							        list='#Customers'
							        multiple="true" />
							 </s:if>

					     </TD></TR>

					   </TABLE>
					</TD>
				</TR>
			</TABLE>
		</div>

<%--
	    <div>
		    <table  class="listTableBody3 padTop" width="96%">
		    	<tr><td>&nbsp;</td></tr>
		    	<tr>
		            <td class="textAlignRight"><s:if test='%{#hasAccessToEdit == "TRUE"}'><s:submit tabindex="905" name="CorpInfoBottom" key="SaveChanges"  cssClass="submitBtnBg2"/></s:if></td>
		        </tr>
		    </table>
	    </div>
--%>
   </s:form>
   </div>
   	<p>&nbsp;</p>
  </div><!-- // tab content end -->
  <div>
	<table width="98%">
		<tr>
			<s:if test="@com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer@getInstance().isAuthorized(#userListResId,#ctxt)">
			<td align="right" class="padding-right3"><a tabindex="915"
				href="<s:url value='/profile/user/getUserList.action'><s:param name="customerID" value='%{#xmlUtils.getAttribute(#sdoc,"CustomerID")}'/></s:url>"><s:text name="ViewUsers"/></a></td>
			</s:if>
		</tr>
		<tr>
		<td>
		<p>&nbsp;</p>
		</td>
	</tr>
	</table>
	</div>
        <!-- // t2-main-content end --></div>
        </div><!-- //container end -->
<div class="t2-footer commonFooter" id="t2-footer">
	<!-- add content here for footer -->
	<s:action name="footer" executeResult="true" namespace="/common" />
</div><!-- // footer end -->

</div><!-- // main end -->

       </body>
</swc:html>

