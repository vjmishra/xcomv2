
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
 <script language="javascript" type="text/javascript">
	var hasAccessTo =  '<s:property value="#_action.getIsChildCustomer()" />';

	function openPreview()
	{
		window.open('', 'newwin', 'toolbar=no, location=yes, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, copyhistory=no, width=800, height=600');
		document.uploadCustomerLogo.action = "previewCustomerLogo.action";
		document.uploadCustomerLogo.target = 'newwin';
		document.uploadCustomerLogo.submit();
	}

	function uploadLogo()
	{
		document.uploadCustomerLogo.action = "uploadCustomerLogo.action";
		document.uploadCustomerLogo.target = "";
		document.uploadCustomerLogo.submit();
	}
	
	function showDeleteDialog()
	{
		 DialogPanel.show('deleteLogoDialog');
	}

	function deleteCustLogo()
	{
		document.uploadCustomerLogo.action = "deleteCustomerLogo.action";
		document.uploadCustomerLogo.target = "";
		document.uploadCustomerLogo.submit();
	}

	function showLogo()
	{
		window.open('', 'newwin1', 'toolbar=no, location=yes, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, copyhistory=no, width=400, height=400');
		document.uploadCustomerLogo.action = "viewCustomerLogo.action";
		document.uploadCustomerLogo.target = 'newwin1';
		document.uploadCustomerLogo.submit();
	}
	
		
 </script>
<swc:head/>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/user/my-account.css" />

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/profile/profile.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/profile/org/org.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/common/verifyAddress.js" ></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/common/address/editableAddress.js" ></script>
<s:set name='expandAddressPanel' value="#_action.isExpandAddressPanel()" />
<script type="text/javascript" >
	expPanel = function(){
		expAddPnl = '<s:property value="#expandAddressPanel" />';
	}
</script>

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
			 <s:set name='sCustExtn' value='#xmlUtil.getChildElement(#sdoc, "Extn")'/>
			 
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
             <s:set name='custLogoURL' value='#xmlUtil.getAttribute(#sCustExtn, "ExtnCustLogo")'/>
             <s:set name='custSuffixType' value='#xmlUtil.getAttribute(#sCustExtn, "ExtnSuffixType")'/>
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
			<s:text name="Update"/>&nbsp;<s:text name='%{#xmlUtil.getAttribute(#sBuyerOrg,"OrganizationName")}'/>&nbsp;<s:text name="generalinformation"/></span></td>
			<s:if test="@com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer@getInstance().isAuthorized(#userListResId,#wccontext)">
			<td align="right" class="padding-right3"><a tabindex="925"
				href="<s:url value='/profile/user/getUserList.action'><s:param name="customerID" value='%{#xmlUtil.getAttribute(#sdoc,"CustomerID")}'/></s:url>"><s:text name="ViewUsers"/></a></td>
			</s:if>
		</tr>
	</table>
	</div>

<!-- Begin Customer include Tab -->
<s:action name="customerNavigationTab" executeResult="true"	namespace="/profile/org">
	<s:param name="selectedTab">GeneralInfo</s:param>
	<s:param name="customerId" value='#xmlUtil.getAttribute(#sdoc,"CustomerID")'/>
  	<s:param name="organizationCode" value='#xmlUtil.getAttribute(#sdoc,"OrganizationCode")'/>
</s:action> <!-- End include Tab -->

  <div class="tabContent">

  			<s:form action="updateCustomerGeneralInfo" validate="true" id="getCustOrg"  name="getCustOrg" method="post">
  			<s:hidden name="#action.namespace" value="/profile/org"/>
			<s:hidden id="genInfoActionName" name="#action.name" value="updateCustomerGeneralInfo"/>
			 <s:hidden name="customerId" id="customerId" value='%{#xmlUtil.getAttribute(#sdoc,"CustomerID")}'  />
		     <s:hidden name="organizationCode" id="organizationCode" value='%{#xmlUtil.getAttribute(#sdoc,"OrganizationCode")}'  />

			<div>
            <table  class="listTableBody3 padTop" width="96%">
            	<tr><td>&nbsp;</td></tr>
            	<tr>
                	<td class="textAlignLeft"><span id="expandTable1"><a tabindex="934" href="#" onclick="expandOrCollapse();" ><s:text name="ExpandCollapse"/></a></span></td>
                    <td class="textAlignRight"><s:if test='%{#hasAccessToEdit == "TRUE"}'>  <s:submit tabindex="935" name="GeneralInfoTop" key="SaveChanges"  cssClass="submitBtnBg2"/></s:if></td>
                </tr>
            </table>
            </div>
			<table class="listTableHeader2" id="listTableHeader1">
			  <tr>
			    <td width="11" class="altBorder textAlignLeft">
			    <s:a tabindex="55" href="#" id="anchortoggleGeneralInfo">
			      <div id="toggleGeneralInfo" class="">&nbsp;</div>
			      </s:a>
			    </td>
			    <td class="listTableHeaderText"><s:text name="GeneralInformation"/></td>
			  </tr>
			</table>

            <div id="generalInfoTable" style="display:inline;">
            <table width="96%" class="listTableBody3 padding-left3">

                <tr valign="top">
                    <td width="45%">
                    <table class="cartSubTable">
                    	<tr>
                        	<td class="themeSubHeader" colspan="3"><s:text name="ContactInformation"/></td>
                        </tr>
                    	<tr>
                    		<td class="boldText textAlignLeft"><s:text name="RB_OrganizationName" />:</td>
                    		<td class="mandatoryIndicator"><s:text name="mandatory.Indicator.Text"/></td>
						    <td> <s:textfield  tabindex="5" name="organizationName" value='%{#xmlUtil.getAttribute(#sBuyerOrg,"OrganizationName")}' disabled="%{#isDisabled}" /></td>
                        </tr>
                        <tr>
                            <td class="boldText textAlignLeft"><s:text name="WebsiteURL" />:</td>
                            <td class="mandatoryIndicator"></td>
						    <td> <s:textfield  tabindex="10"  name="primaryUrl" value='%{#xmlUtil.getAttribute(#sBuyerOrg,"PrimaryUrl")}' disabled="%{#isDisabled}"   /></td>
                        </tr>
                        <tr>
	                        <td class="boldText textAlignLeft"><s:text name="DunBradStreetID" />:</td>
	                        <td class="mandatoryIndicator"></td>
	 				        <td> <s:textfield tabindex="15" labelposition="left" name="dunsNumber" value='%{#xmlUtil.getAttribute(#sBuyerOrg,"DunsNumber")}' disabled="%{#isDisabled}"    /></td>
						     <s:hidden name="buyerOrganizationCode" value='%{#xmlUtil.getAttribute(#sBuyerOrg,"OrganizationCode")}'  />
						</tr>

                    </table>
                    </td>
                    <td width="30%">
                    <table class="cartSubTable">
                    	<tr>
                        	<td class="themeSubHeader" colspan="2"><s:text name="ProfileInformation" /></td>
                        </tr>

                    		<tr>
	                        	<td class="boldText textAlignLeft"><s:text name="ProfileStatus" />:</td>
	                            <td><s:if test='%{#hasAccessToEdit == "TRUE" && #selfUser != "TRUE"}'>
	                            <s:select tabindex="20"  name="status" value='%{#xmlUtil.getAttribute(#sdoc,"Status")}'
								    list="#statusCodes"   />
	                            </s:if>
	                            <s:else>
		                            <s:property value='%{#statusCodes[#xmlUtil.getAttribute(#sdoc,"Status")]}'  /> </td>
		                            <s:hidden name="status" value='%{#xmlUtil.getAttribute(#sdoc,"Status")}'  />
	                            </s:else>


                         	</tr>
	                        <tr>
	                        	<td class="boldText textAlignLeft"><s:text name="EffectiveStatus" />:</td>
	                            <td> <s:property value='%{#statusCodes[#effectiveStatus]}'  /> </td>
 	                        </tr>
	                        <tr>
	                        	<td class="boldText textAlignLeft"><s:text name="CustomerID" />:</td>
	                            <td> <s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@formatBillToShipToCustomer(#xmlUtil.getAttribute(#sdoc,"CustomerID"))' /> </td>
 	                        </tr>
	                        <tr>
	                        	<td class="boldText textAlignLeft"><s:text name="CurrencyType" />:</td>
 							    <td> <s:select tabindex="25"  name="currency" value='%{#xmlUtil.getAttribute(#defaultCurrency,"Currency")}'
								    list="#currency" emptyOption="true" disabled="%{#isDisabled}"  /></td>
								<s:hidden name="customerCurrencyKey" value='%{#xmlUtil.getAttribute(#defaultCurrency,"CustomerCurrencyKey")}'  />
	                        </tr>
	                      </table>
                         </td>
                          <td>
                    	  <table>
                    	   <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
                    		<tr>
	                            <td class="boldText textAlignLeft"><s:text name="ProfileType" />:</td>
	                            <td> <s:property value='#xmlUtil.getAttribute(#sdoc,"RelationshipType")' /></td>
                        	</tr>
	                        <tr>
	                            <td class="boldText textAlignLeft"><s:text name="ProfileLevel" />:</td>
	                            <td><s:property value='#xmlUtil.getAttribute(#sdoc,"CustomerLevel")' />  </td>
	                        </tr>
	                        <tr>
	                            <td class="boldText textAlignLeft"><s:text name="Vertical" />:</td>
	                            <td><s:property value='#xmlUtil.getAttribute(#sdoc,"Vertical")' /> </td>
	                        </tr>
	                       
	                      </table>

                    	  </td>
                 </tr>
            </table>
			</s:form>
			<s:if test='%{#custSuffixType == "MC" || #custSuffixType =="C"}'>
			<table class="listTableBody3 padTop" width="96%">
				<tr>
				<td>
				<s:form action="uploadCustomerLogo" method="POST" enctype="multipart/form-data">
					<s:hidden name="customerId" id="customerId" value='%{#xmlUtil.getAttribute(#sdoc,"CustomerID")}'  />
		     		<s:hidden name="organizationCode" id="organizationCode" value='%{#xmlUtil.getAttribute(#sdoc,"OrganizationCode")}'  />
		     		<s:hidden name="custLo" id="custLo" value='%{#xmlUtil.getAttribute(#sCustExtn, "ExtnCustLogo")}'  />
		     		
					<tr>
						<td class="boldText textAlignLeft"><s:text name="Customer Logo" />:</td>
						<td>
						<s:if test='%{#custLogoURL != null && #custLogoURL != "" }'>
						 		<s:hidden name="logoUrl" id="logoUrl" value='%{#custLogoURL}'  />
							
								<input type="button" name="viewLogo" value="<s:text name="View" />" class="submitBtnBg1" onclick = "javascript:showLogo()" />
								<input type="button" name="deleteLogo" value="<s:text name="Delete" />" class="submitBtnBg1" onclick = "javascript:showDeleteDialog()" />
							
						</s:if>
						</td>
						<td>
							<s:file name="upload" label="File"/>
							<s:submit name="UploadLogo" value="Upload" onclick = "javascript:uploadLogo()"/>
							<input type="button" name="previewLogo" value="<s:text name="Preview" />" class="submitBtnBg1" onclick = "javascript:openPreview()" />
						</td>
					</tr>
				</s:form>
				</td>
				</tr>
			</table>
			</s:if>
            </div>
            
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

    </tr>
    <tr>

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

          <s:param name="columnSpecs[1].label" value="'AddressLine1label'"/>
          <s:param name="columnSpecs[1].dataField" value="'AddressLine1'"/>
          <s:param name="columnSpecs[1].fieldCssClass" value="'draftOrderListTable'"/>
          <s:param name="columnSpecs[1].labelCssClass" value="'tablecolumn'"/>
          <s:param name="columnSpecs[1].columnId" value="'AddressLine1'"/>
          <s:param name="columnSpecs[1].dataCellBuilder" value="'CustomerAddressPersonInfoAnchor'"/>
		  <s:param name="columnSpecs[1].dataCellBuilderProperties['namespace']" value="'/profile/org'"/>
		  <s:param name="columnSpecs[1].dataCellBuilderProperties['columnName']" value="'AddressLine1'"/>

          <s:param name="columnSpecs[2].label" value="'AddressLine2label'"/>
          <s:param name="columnSpecs[2].dataField" value="'AddressLine2'"/>
          <s:param name="columnSpecs[2].fieldCssClass" value="'draftOrderListTable'"/>
          <s:param name="columnSpecs[2].labelCssClass" value="'tablecolumn'"/>
          <s:param name="columnSpecs[2].columnId" value="'AddressLine2'"/>
          <s:param name="columnSpecs[2].dataCellBuilder" value="'CustomerAddressPersonInfoAnchor'"/>
		  <s:param name="columnSpecs[2].dataCellBuilderProperties['namespace']" value="'/profile/org'"/>
          <s:param name="columnSpecs[2].dataCellBuilderProperties['columnName']" value="'AddressLine2'"/>
          
          <s:param name="columnSpecs[3].label" value="'AddressLine3label'"/>
          <s:param name="columnSpecs[3].dataField" value="'AddressLine3'"/>
          <s:param name="columnSpecs[3].fieldCssClass" value="'draftOrderListTable'"/>
          <s:param name="columnSpecs[3].labelCssClass" value="'tablecolumn'"/>
          <s:param name="columnSpecs[3].columnId" value="'AddressLine3'"/>
          <s:param name="columnSpecs[3].dataCellBuilder" value="'CustomerAddressPersonInfoAnchor'"/>
		  <s:param name="columnSpecs[3].dataCellBuilderProperties['namespace']" value="'/profile/org'"/>
          <s:param name="columnSpecs[3].dataCellBuilderProperties['columnName']" value="'AddressLine3'"/>          

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

          <s:param name="columnSpecs[7].label" value="'Zip4'"/>
          <s:param name="columnSpecs[7].dataField" value="'ExtnZip4'"/>
          <s:param name="columnSpecs[7].fieldCssClass" value="'draftOrderListTable'"/>
          <s:param name="columnSpecs[7].labelCssClass" value="'tablecolumn'"/>
          <s:param name="columnSpecs[7].columnId" value="'ExtnZip4'"/>
          <s:param name="columnSpecs[7].dataCellBuilder" value="'CustomerAddressPersonInfoAnchor'"/>
		  <s:param name="columnSpecs[7].dataCellBuilderProperties['namespace']" value="'/profile/org'"/>
          <s:param name="columnSpecs[7].dataCellBuilderProperties['columnName']" value="'ExtnZip4'"/>          

          <s:param name="columnSpecs[8].label" value="'Countrylabel'"/>
          <s:param name="columnSpecs[8].dataField" value="'Country'"/>
          <s:param name="columnSpecs[8].fieldCssClass" value="'draftOrderListTable'"/>
          <s:param name="columnSpecs[8].labelCssClass" value="'tablecolumn'"/>
          <s:param name="columnSpecs[8].columnId" value="'Country'"/>
          <s:param name="columnSpecs[8].dataCellBuilder" value="'CustomerAddressPersonInfoAnchor'"/>
		  <s:param name="columnSpecs[8].dataCellBuilderProperties['namespace']" value="'/profile/org'"/>
          <s:param name="columnSpecs[8].dataCellBuilderProperties['columnName']" value="'Country'"/>

     </s:action>
   </td>
   </tr>
  <!-- table component for default addresses ENDS-->






            </table>

            </div>
            <%-- // Save Changes button problem for validations  so commenting this
            <div>
            <table  class="listTableBody3 padTop" width="96%">
            	<tr><td>&nbsp;</td></tr>
            	<tr>
                	<td class="textAlignLeft"><span id="expandTable1"><a tabindex="905" href="#" onClick="expandOrCollapse();" id=""><s:text name="ExpandCollapse" /></a></span></td>
                    <td class="textAlignRight"><s:if test='%{#hasAccessToEdit == "TRUE"}'><s:submit tabindex="904" type="button" action="updateCustomerGeneralInfo" formId="getCustOrg" onclick="submitOtherForm();" name="GeneralInfoBottom" key="SaveChanges" cssClass="submitBtnBg2"  /></s:if></td>
                </tr>
            </table>
            </div> --%>
  		</s:form>
			<p>&nbsp;</p>
	<div>
	<table>
	<tr>
	<td>(</td><td class="mandatoryIndicator"><s:text name="mandatory.Indicator.Text"/><td>): <s:text name="pageLevelTextForMandatoryIndicator"/></td>
	</tr>
	</table>
	</div>
	        </div> <!-- // tab content end -->
    <div>
	<table width="98%">
		<tr>
			<s:if test="@com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer@getInstance().isAuthorized(#userListResId,#wccontext)">
			<td align="right" class="padding-right3"><a tabindex="915"
				href="<s:url value='/profile/user/getUserList.action'><s:param name="customerID" value='%{#xmlUtil.getAttribute(#sdoc,"CustomerID")}'/></s:url>"><s:text name="ViewUsers"/></a></td>
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
</div>
<!-- // container end -->
<div class="t2-footer commonFooter" id="t2-footer">
	<!-- add content here for footer -->
	<s:action name="footer" executeResult="true" namespace="/common" />
</div><!-- // footer end -->

</div><!-- // main end -->

 <!-- div tag for light box -->
		    <swc:dialogPanel title="${AddressInformationTitle}" isModal="true" id="modalDialogPanel2">
		    <div id="ajax-body-1"> </div>
		    </swc:dialogPanel>
			<!-- end div for light box -->

			<swc:dialogPanel title="${AddressInformationTitle}" isModal="true" id="modalDialogMessagePanel">
		    <div id="ajax-body-message"> </div>
		    </swc:dialogPanel>
			
			<swc:dialogPanel title="" isModal="true" id="deleteLogoDialog">
		    	<span class="padding-left4 textAlignLeft">
                  Do you want to delete the logo
         	   	</span>
         	   	<div class="clearBoth"></div>
         		<div id="deleteButtonPanelId" class="padding-all1 textAlignCenter">         	
             		<s:submit  type="button" name="yes" id ='Confirm_Yes' key='Confirm_Yes'cssClass="submitBtnBg1" tabindex="3100" onclick="javascript:deleteCustLogo();DialogPanel.hide('deleteLogoDialog');return false;"/>
	    			<s:submit  type="button" name="no" id='Confirm_No' key='Confirm_No'  cssClass="submitBtnBg1" tabindex="3101" onclick="javascript:DialogPanel.hide('deleteLogoDialog'); svg_classhandlers_decoratePage();"/>
         		</div>
     		</swc:dialogPanel>
       </body>
</swc:html>
