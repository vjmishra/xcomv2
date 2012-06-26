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
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/user/my-account<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/profile/profile<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/profile/org/corporateInfo<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>


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
		<s:form action="updateCustMaintenanceInfo" namespace="/profile/org" name="customerCorporateInfo" id="customerCorporateInfo" method="post">
	 		
		<div>
            <table  class="listTableBody3 padTop" width="96%">
            	<tr><td>&nbsp;</td></tr>
            	<tr>
                    <td class="textAlignRight"><s:if test='%{#hasAccessToEdit == "TRUE"}'> <s:submit tabindex="35" name="CorpInfoTop" key="SaveChanges"  cssClass="submitBtnBg2"/> </s:if></td>
                </tr>
            </table>
        </div>
        <!--  XPEDX Customer Profile Maintenance Starts: adsouza -->
        <div>       
		<s:action name="xpedxCustomerMaintenance"  executeResult="true" namespace="/profile/org">
			<s:param name="customerId" value='#xmlUtils.getAttribute(#sdoc,"CustomerID")'/>
  			<s:param name="organizationCode" value='#xmlUtils.getAttribute(#sdoc,"OrganizationCode")'/>
		</s:action>
		</div>
        
        <!--  XPEDX Customer Profile Maintenance Ends: adsouza -->
        
        <div>
		    <table class="listTableHeader2" id="listTableHeader1">
		        <tr>
		            <td><span class="listTableHeaderText"><s:text name="ShippingPreferences"/> </label></span></td>
		        </tr>
		    </table>
    	</div>
		<div id="shipmentinfo" >
				<TABLE class="listTableBody3 padding-left3" width="96%" >
				    <s:hidden name="customerSchPreferenceKey" value='%{#xmlUtils.getAttribute(#customerSchPreferences,"CustomerSchPreferenceKey")}'  />
					<TR>
						<TD>
							<TABLE>
							   <TR>
							    <TD colspan="2" class="themeSubHeader"><s:text name="AvailabilityPreferences"/> </TH>
							   </TR>
							   <TR> <TD>
								   <s:radio tabindex="5" cssStyle="vertcal-align:center" list="#{'':#_action.getText('None')  }"
									   name="optimizationType" id="optimizationType" disabled="%{#isDisabled}" value='%{#xmlUtils.getAttribute(#customerSchPreferences,"OptimizationType")}' >

									   </s:radio></TD> </TR><TR><TD>
									   <s:radio tabindex="10" cssStyle="vertcal-align:center" list="#{  '03':#_action.getText('GroupIntoFewShipmentsAsPossible') }"
									   name="optimizationType" id="optimizationType" disabled="%{#isDisabled}" value='%{#xmlUtils.getAttribute(#customerSchPreferences,"OptimizationType")}' >

									   </s:radio></TD> </TR><TR><TD>
									   <s:radio tabindex="15" cssStyle="vertcal-align:center" list="#{ '01':#_action.getText('FasterShipAsTheyAreAvailableMayIncurAdditionalCost')+'<BR>'}"
									   name="optimizationType" id="optimizationType" disabled="%{#isDisabled}" value='%{#xmlUtils.getAttribute(#customerSchPreferences,"OptimizationType")}' >

									   </s:radio>
							  </TD> </TR>
							</TABLE>

						</TD>
					</TR>
			</TABLE>
		</div>

    	<s:hidden name="customerId" id="customerId" value='%{#xmlUtils.getAttribute(#sdoc,"CustomerID")}'  />
	    <s:hidden name="organizationCode" id="organizationCode" value='%{#xmlUtils.getAttribute(#sdoc,"OrganizationCode")}'  />

	 </s:form>
	</div>

	<div>
	<s:form action="getChildCustomerGeneralInfo" namespace="/profile/org" validate="true" name="customerCorporateInfohierarchy" id="customerCorporateInfohierarchy" method="post" >
     	<s:hidden name="organizationCode" id="organizationCode" value='%{#xmlUtils.getAttribute(#sdoc,"OrganizationCode")}'  />
		<s:hidden name="#action.namespace" value="/profile/org"/>
		<s:hidden name="#action.name" value="getChildCustomerGeneralInfo"/>
		 <div>
		    <table class="listTableHeader2" id="listTableHeader1">
		        <tr>
		            <td><span class="listTableHeaderText"><s:text name="Hierarchy"/> </label></span></td>
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
			    				 <s:select tabindex="50" 
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
  	<!--  Addressees START -->
  	<s:set name='extnElem' value='#xmlUtils.getChildElement(#sdoc, "Extn")'/>
  	<s:if test='%{#extnElem.getAttribute("ExtnSuffixType") == "B"}'>
  	<s:form action="deleteCustomerAddress" namespace="/profile/org" id="updateCustOrg" name="updateCustOrg" method="post"   >
			 <s:hidden name="customerId" id="customerId" value='%{#xmlUtil.getAttribute(#sdoc,"CustomerID")}'  />
		     <s:hidden name="organizationCode" id="organizationCode" value='%{#xmlUtil.getAttribute(#sdoc,"OrganizationCode")}'  />
			 <s:hidden name="#action.namespace" value="/profile/org"/>
			<s:hidden id="actionName" name="#action.name" value="deleteCustomerAddress"/>
			<s:hidden name="validationField" value=""/>
			<table class="listTableHeader2" id="listTableHeader1">
			  <tr>
			    <td width="11" class="altBorder textAlignLeft">
			    <s:a tabindex="65" href="#" id="anchortoggleAddressBook">
			      <div id="toggleAddressBook" class="">&nbsp;</div>
			      </s:a>
			    </td>
			    <td class="listTableHeaderText"><s:text name="AddressBook"/></td>
			  </tr>
			</table>

            <div id="addressBookTable">


		      <s:set name='sdoc' value="#_action.getCustomerOrganizationEle()" />
           	  <s:set name='custId' value='#xmlUtil.getAttribute(#sdoc, "CustomerID")'/>
		      <s:set name='orgCode' value='#xmlUtil.getAttribute(#sdoc, "OrganizationCode")'/>
		  <s:set name="defaultAddressList" value="#_action.getCustomerDefaultAddressListEle()" />
		  <s:set name="defaultNonInheritedAddressList" value="#_action.getCustomerNonInheritedDefaultAddressListEle()" />
		  <s:set name="nonDefaultAddressList" value="#_action.getCustomerAddressListEle()" />
		  <s:if test='%{(#defaultNonInheritedAddressList !=null && #defaultNonInheritedAddressList.size() >0) || (#nonDefaultAddressList != null && #nonDefaultAddressList.size() >0)}'>
		  	<s:set name='listEmty' value="%{'TRUE'}" />
		  </s:if>
		     <s:if test='%{#hasAccessToEdit != "TRUE" || #listEmty == null}'>
	             <s:set name='isButtonDisabled' value="%{'true'}"/>
             </s:if>
             <s:else>
             	 <s:set name='isButtonDisabled' value="%{'false'}"/>
             </s:else>
  <table width="96%" class="listTableBody3 padding-left3">
    <tr>
    	<td class="textAlignLeft" colspan="10">
    	<div id="wwgrp_myAccount_addressbuttons" class="wwgrp">
		<div id="wwctrl_myAccount_addressbuttons" class="wwctrl">
		<s:if test='%{#hasAccessToEdit == "TRUE"}'>
			<s:if test='%{#listEmty != null && #listEmty == "TRUE"}'>
	        	<input tabindex="655"  type="button" <s:if test='%{#isButtonDisabled}'>disabled</s:if> name="deleteAdd" value='<s:text name="Delete"/>' class="submitBtnBg1"   onclick="deleteAddress('<s:text name="confirmAddressDelete" />', '<s:url value="/profile/org/deleteCustomerAddress.action"/>', '<s:url value="/profile/org/confirmJsp.action"/>'); return false" />&nbsp;
	        	<input tabindex="660"  type="button"  <s:if test='%{#isButtonDisabled}'>disabled</s:if> name="duplicateAdd" value='<s:text name="Duplicate"/>' class="submitBtnBg1"   onclick="duplicateAddress('<s:text name="confirmAddressDuplicate" />','<s:url value="/profile/org/duplicateCustomerAddress.action"/>', '<s:url value="/profile/org/confirmJsp.action"/>'); return false" />&nbsp;
        	</s:if>
        	<input tabindex="110" type="button" name="addAddress" value="<s:text name="AddNew" />" class="submitBtnBg1" onclick="addAddressLightBox('<s:url value="/profile/org/newCustomerAddress.action"><s:param name="customerId" value='%{#xmlUtil.getAttribute(#sdoc,"CustomerID")}'/><s:param name="organizationCode" value='%{#xmlUtil.getAttribute(#sdoc,"OrganizationCode")}'/></s:url>')"/>
        	&nbsp;</s:if>
        </div></div>
    	</td>
    </tr>
    <tr>
    	<td class="textAlignLeft subHeaderText" colspan="10">*<s:text name="DefaultSoldToShipToAndBillToAddresses" /></td>
    </tr>

   <!-- table component for default addresses STARTS-->
    <tr>
  	<td colspan="10">

 	 <s:action name="buildSimpleTable" executeResult="true" namespace="/common">
	    <s:param name="id" value="defaultAddressTable"/>
	    <s:param name="cssClass" value="'listTableBody3 padding-left3'"/>
	    <s:param name="summary" value="'Customer Default address List Table'"/>
 	    <s:param name="iterable" value="#_action.getCustomerDefaultAddressListEle()"/>
          <s:param name="columnSpecs[0].label" value="'AddressNamelabel'"/>
          <s:param name="columnSpecs[0].dataField" value="'AddressID'"/>
          <s:param name="columnSpecs[0].fieldCssClass" value="'draftOrderListTable'"/>
          <s:param name="columnSpecs[0].labelCssClass" value="'tablecolumn'"/>
          <s:param name="columnSpecs[0].columnId" value="'AddressID'"/>
          <s:param name="columnSpecs[0].dataCellBuilder" value="'CustomerAddressListAnchor'"/>
		  <s:param name="columnSpecs[0].dataCellBuilderProperties['namespace']" value="'/profile/org'"/>
		  <s:param name="columnSpecs[0].dataCellBuilderProperties['customerId']" value="#custId"/>
		  <s:param name="columnSpecs[0].dataCellBuilderProperties['organizationCode']" value="#orgCode"/>
		  <s:param name="columnSpecs[0].dataCellBuilderProperties['defaultPanel']" value='%{"true"}'/>

		  <s:param name="columnSpecs[1].label" value="'Typelabel'"/>
          <s:param name="columnSpecs[1].dataField" value="'AddressType'"/>
          <s:param name="columnSpecs[1].fieldCssClass" value="'draftOrderListTable'"/>
          <s:param name="columnSpecs[1].labelCssClass=tablecolumn"/>
          <s:param name="columnSpecs[1].columnId" value="'AddressType'"/>
          <s:param name="columnSpecs[1].dataCellBuilder" value="'CustomerAddressIsCommertialAnchor'"/>
		  <s:param name="columnSpecs[1].dataCellBuilderProperties['namespace']" value="'/profile/org'"/>
          <s:param name="columnSpecs[1].dataCellBuilderProperties['columnName']" value="'IsCommercialAddress'"/>

          <s:param name="columnSpecs[2].label" value="'AddressLine1label'"/>
          <s:param name="columnSpecs[2].dataField" value="'AddressLine1'"/>
          <s:param name="columnSpecs[2].fieldCssClass" value="'draftOrderListTable'"/>
          <s:param name="columnSpecs[2].labelCssClass" value="'tablecolumn'"/>
          <s:param name="columnSpecs[2].columnId" value="'AddressLine1'"/>
          <s:param name="columnSpecs[2].dataCellBuilder" value="'CustomerAddressPersonInfoAnchor'"/>
		  <s:param name="columnSpecs[2].dataCellBuilderProperties['namespace']" value="'/profile/org'"/>
		  <s:param name="columnSpecs[2].dataCellBuilderProperties['columnName']" value="'AddressLine1'"/>

          <s:param name="columnSpecs[3].label" value="'AddressLine2label'"/>
          <s:param name="columnSpecs[3].dataField" value="'AddressLine2'"/>
          <s:param name="columnSpecs[3].fieldCssClass" value="'draftOrderListTable'"/>
          <s:param name="columnSpecs[3].labelCssClass" value="'tablecolumn'"/>
          <s:param name="columnSpecs[3].columnId" value="'AddressLine2'"/>
          <s:param name="columnSpecs[3].dataCellBuilder" value="'CustomerAddressPersonInfoAnchor'"/>
		  <s:param name="columnSpecs[3].dataCellBuilderProperties['namespace']" value="'/profile/org'"/>
          <s:param name="columnSpecs[3].dataCellBuilderProperties['columnName']" value="'AddressLine2'"/>

          <s:param name="columnSpecs[4].label" value="'Citylabel'"/>
          <s:param name="columnSpecs[4].dataField" value="'City'"/>
          <s:param name="columnSpecs[4].fieldCssClass" value="'draftOrderListTable'"/>
          <s:param name="columnSpecs[4].labelCssClass" value="'tablecolumn'"/>
          <s:param name="columnSpecs[4].columnId" value="'City'"/>
          <s:param name="columnSpecs[4].dataCellBuilder" value="'CustomerAddressPersonInfoAnchor'"/>
		  <s:param name="columnSpecs[4].dataCellBuilderProperties['namespace']" value="'/profile/org'"/>
          <s:param name="columnSpecs[4].dataCellBuilderProperties['columnName']" value="'City'"/>

          <s:param name="columnSpecs[5].label" value="'StateProvincelabel'"/>
          <s:param name="columnSpecs[5].dataField" value="'State'"/>
          <s:param name="columnSpecs[5].fieldCssClass" value="'draftOrderListTable'"/>
          <s:param name="columnSpecs[5].labelCssClass" value="'tablecolumn'"/>
          <s:param name="columnSpecs[5].columnId" value="'State'"/>
          <s:param name="columnSpecs[5].dataCellBuilder" value="'CustomerAddressPersonInfoAnchor'"/>
		  <s:param name="columnSpecs[5].dataCellBuilderProperties['namespace']" value="'/profile/org'"/>
          <s:param name="columnSpecs[5].dataCellBuilderProperties['columnName']" value="'State'"/>

          <s:param name="columnSpecs[6].label" value="'PostalCodelabel'"/>
          <s:param name="columnSpecs[6].dataField" value="'ZipCode'"/>
          <s:param name="columnSpecs[6].fieldCssClass" value="'draftOrderListTable'"/>
          <s:param name="columnSpecs[6].labelCssClass" value="'tablecolumn'"/>
          <s:param name="columnSpecs[6].columnId" value="'ZipCode'"/>
          <s:param name="columnSpecs[6].dataCellBuilder" value="'CustomerAddressPersonInfoAnchor'"/>
		  <s:param name="columnSpecs[6].dataCellBuilderProperties['namespace']" value="'/profile/org'"/>
          <s:param name="columnSpecs[6].dataCellBuilderProperties['columnName']" value="'ZipCode'"/>

          <s:param name="columnSpecs[7].label" value="'Countrylabel'"/>
          <s:param name="columnSpecs[7].dataField" value="'Country'"/>
          <s:param name="columnSpecs[7].fieldCssClass" value="'draftOrderListTable'"/>
          <s:param name="columnSpecs[7].labelCssClass" value="'tablecolumn'"/>
          <s:param name="columnSpecs[7].columnId" value="'Country'"/>
          <s:param name="columnSpecs[7].dataCellBuilder" value="'CustomerAddressPersonInfoAnchor'"/>
		  <s:param name="columnSpecs[7].dataCellBuilderProperties['namespace']" value="'/profile/org'"/>
          <s:param name="columnSpecs[7].dataCellBuilderProperties['columnName']" value="'Country'"/>

     </s:action>
   </td>
   </tr>
  <!-- table component for default addresses ENDS-->


     <tr>
    	<td class="textAlignLeft subHeaderText" colspan="10"><s:text name="OtherAddresses" /></td>
     </tr>

 <!-- table component for Other addresses STARTS-->
    <tr>
  	<td colspan="10">
 	 <s:action name="buildSimpleTable" executeResult="true" namespace="/common" >
	    <s:param name="id" value="defaultAddressTable"/>
	    <s:param name="cssClass" value="'listTableBody3 padding-left3'"/>
	    <s:param name="summary" value="'Customer Default address List Table'"/>
 	    <s:param name="iterable" value="#_action.getCustomerAddressListEle()"/>
          <s:param name="columnSpecs[0].label" value="'AddressNamelabel'"/>
          <s:param name="columnSpecs[0].dataField" value="'AddressID'"/>
          <s:param name="columnSpecs[0].fieldCssClass" value="'draftOrderListTable'"/>
          <s:param name="columnSpecs[0].labelCssClass" value="'tablecolumn'"/>
          <s:param name="columnSpecs[0].columnId" value="'AddressID'"/>
          <s:param name="columnSpecs[0].dataCellBuilder" value="'CustomerAddressListAnchor'"/>
		  <s:param name="columnSpecs[0].dataCellBuilderProperties['namespace']" value="'/profile/org'"/>
		  <s:param name="columnSpecs[0].dataCellBuilderProperties['customerId']" value="#custId"/>
		  <s:param name="columnSpecs[0].dataCellBuilderProperties['organizationCode']" value="#orgCode"/>
		  <s:param name="columnSpecs[0].dataCellBuilderProperties['defaultPanel']" value='%{"false"}'/>

		  <s:param name="columnSpecs[1].label" value="'Typelabel'"/>
          <s:param name="columnSpecs[1].dataField" value="'IsCommercialAddress'"/>
          <s:param name="columnSpecs[1].fieldCssClass" value="'draftOrderListTable'"/>
          <s:param name="columnSpecs[1].labelCssClass=tablecolumn"/>
          <s:param name="columnSpecs[1].columnId" value="'IsCommercialAddress'"/>
          <s:param name="columnSpecs[1].dataCellBuilder" value="'CustomerAddressIsCommertialAnchor'"/>
		  <s:param name="columnSpecs[1].dataCellBuilderProperties['namespace']" value="'/profile/org'"/>
          <s:param name="columnSpecs[1].dataCellBuilderProperties['columnName']" value="'IsCommercialAddress'"/>

          <s:param name="columnSpecs[2].label" value="'AddressLine1label'"/>
          <s:param name="columnSpecs[2].dataField" value="'AddressLine1'"/>
          <s:param name="columnSpecs[2].fieldCssClass" value="'draftOrderListTable'"/>
          <s:param name="columnSpecs[2].labelCssClass" value="'tablecolumn'"/>
          <s:param name="columnSpecs[2].columnId" value="'AddressLine1'"/>
          <s:param name="columnSpecs[2].dataCellBuilder" value="'CustomerAddressPersonInfoAnchor'"/>
		  <s:param name="columnSpecs[2].dataCellBuilderProperties['namespace']" value="'/profile/org'"/>
		  <s:param name="columnSpecs[2].dataCellBuilderProperties['columnName']" value="'AddressLine1'"/>

          <s:param name="columnSpecs[3].label" value="'AddressLine2label'"/>
          <s:param name="columnSpecs[3].dataField" value="'AddressLine2'"/>
          <s:param name="columnSpecs[3].fieldCssClass" value="'draftOrderListTable'"/>
          <s:param name="columnSpecs[3].labelCssClass" value="'tablecolumn'"/>
          <s:param name="columnSpecs[3].columnId" value="'AddressLine2'"/>
          <s:param name="columnSpecs[3].dataCellBuilder" value="'CustomerAddressPersonInfoAnchor'"/>
		  <s:param name="columnSpecs[3].dataCellBuilderProperties['namespace']" value="'/profile/org'"/>
          <s:param name="columnSpecs[3].dataCellBuilderProperties['columnName']" value="'AddressLine2'"/>

          <s:param name="columnSpecs[4].label" value="'Citylabel'"/>
          <s:param name="columnSpecs[4].dataField" value="'City'"/>
          <s:param name="columnSpecs[4].fieldCssClass" value="'draftOrderListTable'"/>
          <s:param name="columnSpecs[4].labelCssClass" value="'tablecolumn'"/>
          <s:param name="columnSpecs[4].columnId" value="'City'"/>
          <s:param name="columnSpecs[4].dataCellBuilder" value="'CustomerAddressPersonInfoAnchor'"/>
		  <s:param name="columnSpecs[4].dataCellBuilderProperties['namespace']" value="'/profile/org'"/>
          <s:param name="columnSpecs[4].dataCellBuilderProperties['columnName']" value="'City'"/>

          <s:param name="columnSpecs[5].label" value="'StateProvincelabel'"/>
          <s:param name="columnSpecs[5].dataField" value="'State'"/>
          <s:param name="columnSpecs[5].fieldCssClass" value="'draftOrderListTable'"/>
          <s:param name="columnSpecs[5].labelCssClass" value="'tablecolumn'"/>
          <s:param name="columnSpecs[5].columnId" value="'State'"/>
          <s:param name="columnSpecs[5].dataCellBuilder" value="'CustomerAddressPersonInfoAnchor'"/>
		  <s:param name="columnSpecs[5].dataCellBuilderProperties['namespace']" value="'/profile/org'"/>
          <s:param name="columnSpecs[5].dataCellBuilderProperties['columnName']" value="'State'"/>

          <s:param name="columnSpecs[6].label" value="'PostalCodelabel'"/>
          <s:param name="columnSpecs[6].dataField" value="'ZipCode'"/>
          <s:param name="columnSpecs[6].fieldCssClass" value="'draftOrderListTable'"/>
          <s:param name="columnSpecs[6].labelCssClass" value="'tablecolumn'"/>
          <s:param name="columnSpecs[6].columnId" value="'ZipCode'"/>
          <s:param name="columnSpecs[6].dataCellBuilder" value="'CustomerAddressPersonInfoAnchor'"/>
		  <s:param name="columnSpecs[6].dataCellBuilderProperties['namespace']" value="'/profile/org'"/>
          <s:param name="columnSpecs[6].dataCellBuilderProperties['columnName']" value="'ZipCode'"/>

          <s:param name="columnSpecs[7].label" value="'Countrylabel'"/>
          <s:param name="columnSpecs[7].dataField" value="'Country'"/>
          <s:param name="columnSpecs[7].fieldCssClass" value="'draftOrderListTable'"/>
          <s:param name="columnSpecs[7].labelCssClass" value="'tablecolumn'"/>
          <s:param name="columnSpecs[7].columnId" value="'Country'"/>
          <s:param name="columnSpecs[7].dataCellBuilder" value="'CustomerAddressPersonInfoAnchor'"/>
		  <s:param name="columnSpecs[7].dataCellBuilderProperties['namespace']" value="'/profile/org'"/>
          <s:param name="columnSpecs[7].dataCellBuilderProperties['columnName']" value="'Country'"/>

     </s:action>
   </td>
   </tr>
  <!-- table component for Other addresses ENDS-->

            </table>

            </div>
            </s:form>
  	</s:if>
  	<!--  Addressees END-->
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

