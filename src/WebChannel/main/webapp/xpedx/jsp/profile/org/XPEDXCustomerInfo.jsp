<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%> 

<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%><s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
<% request.setAttribute("isMergedCSSJS","true"); %>
<s:set name='_action' value='[0]'/>
<s:set name='xmlUtil' value="#_action.getXMLUtils()" />
<s:set name='sdoc' value="outputDoc" />
<s:set name='extnElem' value='#xmlUtil.getChildElement(#sdoc, "Extn")'/>
<s:set name='salesRepList' value='#xmlUtil.getChildElement(#extnElem,"XPEDXSalesRepList")'/>
<s:set name='custAddressElem' value='custAddressElem' />
<s:set name='custPersonInfoElem' value='#xmlUtil.getChildElement(#custAddressElem,"PersonInfo")'/>
<s:set name='custPersonInfoExtnElem' value='#xmlUtil.getChildElement(#custPersonInfoElem,"Extn")'/>
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils' id='wcUtil' />
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta content='IE=8' http-equiv='X-UA-Compatible' />

		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>		
		
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-jquery-headder<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
		<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/ADMIN<s:property value='#wcUtil.xpedxBuildKey' />.css" />

<!-- begin styles. -->

<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<![endif]-->
<!-- end styles -->

<!-- javascript 

<script type="text/javascript" src="../../xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="../../xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="../../xpedx/js/global/validation.js"></script>
<script type="text/javascript" src="../../xpedx/js/global/dojo.js"></script>
<script type="text/javascript" src="../../xpedx/js/global/dojoRequire.js"></script>
<script type="text/javascript" src="../../xpedx/js/theme/theme-1/theme.js"></script>

<script type="text/javascript" src="../../xpedx/js/catalog/catalogExt.js"></script>
<script type="text/javascript" src="<s:url value='/xpedx/js/swc.js'/>"></script>
-->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quicklinks/xpedxCustomerQuickLinks<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/profile/org/xpedxCustomerLocations<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- carousel scripts js   

<script type="text/javascript" src="../../xpedx/js/jcarousel/xpedx-custom-carousel.js"></script>
<script type="text/javascript" src="../../xpedx/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="../../xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript" src="../../xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="../../xpedx/js/jcarousel/lib/jquery.jcarousel.min.js"></script>
<script type="text/javascript" src="../../xpedx/js/quick-add/jquery.form.js"></script>
<script type="text/javascript" src="../../xpedx/js/quick-add/quick-add.js"></script>
<script type="text/javascript" src="../../xpedx/js/modals/checkboxtree/jquery.checkboxtree.js"></script> 
-->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bgiframe.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script> <!-- modified the path for jira 1833 -->
<!--[if IE]><script src="<s:url value='/xpedx/other_libs/excanvas_r3/excanvas.js'/>" type="text/javascript" charset="utf-8"></script><![endif]-->
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF --><script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui<s:property value='#wcUtil.xpedxBuildKey' />.js" language="javascript">
</script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!--  


<!-- Facy Box (Lightbox/Modal Window 
<script type="text/javascript" src="<s:url value='/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js'/>"></script>
<script type="text/javascript" src="<s:url value='/xpedx/js/fancybox/jquery.fancybox-1.3.4.js'/>"></script>
-->
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
<!--  
<script type="text/javascript" src="../../xpedx/js/jquery.shorten.js"></script>
<script type="text/javascript" src="../../xpedx/js/global-xpedx-functions.js"></script>
-->
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/SpryTabbedPanels<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript"></script> <!-- modified the path for jira 1833 -->

<!--  to restrict numeric values in spending limit  -->


<script type="text/javascript">
	$(document).ready(function() {
		$(document).pngFix();
		$("#addNewQL").fancybox({
			'onStart'		:	function(){		
			document.getElementById("linkName").value = "";
			document.getElementById("url").value = "";	
			},
			'titleShow'			: false,
			'transitionIn'		: 'fade',
			'transitionOut'		: 'fade',
			'titlePosition' : 'inside',
			'transitionIn' : 'none',
			'transitionOut' : 'none'
								
		});
		
		//XNGTP - JIRA- 383 EDIT AND MANAGE LINK CODE END
		
		$("#changeLocationCL").fancybox({
			'onStart'		:	function(){
			//Show location fancybox
		if(document.getElementById("showLocationsUrl").value == '')	
 			{	
				alert("Can not Locations at the moment. Try Again Later");
				$.fancybox.close();
			}
		else
			{
				showLocations();
			}
			},
			'autoDimensions'	: false,
			'width' 			: 700,
			'height' 			: 410,
			//XNGTP - JIRA- 489 
			'onClosed' : function(){				
		    	document.getElementById("showLocationsDiv").innerHTML = '';
		    },		
								
		});	
$("#various4").fancybox();
$("#various5").fancybox(); 
});
	
	 function ismaxlength(obj,mlength){
	    	if (obj.getAttribute && obj.value.length>mlength)
	    	obj.value=obj.value.substring(0,mlength)
	    	}		
</script> 

<!-- Facy Box (expand and collpse Modal Window -->
 
<script type="text/javascript">
	function submitForm() {
		document.getElementById("locatoinsForm").submit();
	}
</script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.numeric<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.maskedinput-1.3<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript">
$(document).ready(function() {
$('.phone-numeric').numeric(false); 
$("#txtPhone1").mask("999 999-9999");
$("#txtPhone2").mask("999 999-9999");
$("#txtFax1").mask("999 999-9999");
$("#txtFax2").mask("999 999-9999");
});
</script> 




<style>
.share-modal h2{ margin-bottom:5px; color:#000;}
.share-modal {width:650px; height:auto;} 
.indent-tree { margin-left:15px; }    
.indent-tree-act { margin-left:25px; } 
.checkboxTree input[type=radio]{ margin-top:1px; }
ul.checkboxTree li
{
	margin-bottom:3px;
	color: #000;
	font-weight: normal;
	font-size: 11.7px;
}
.radio-container
{
	height: 300px;
	overflow: auto;
	border: 1px solid #ccc;
	margin: 0px 0px 10px 0px;
}
#collapseAllButtonsTree
{
	padding: 0px 20px;
	margin: 0px;
}

</style>  



<s:url id='adminProfile' namespace='/profile/user' action='XPEDXAdminProfile' />
<title><s:property value="wCContext.storefrontId" /> - <s:text name="admin.customer.title" /></title>


</head>
<!-- END swc:head -->
<body class="ext-gecko ext-gecko3">
<div id="main-container">
  <div id="main">
  	<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
  	<s:form name="custInfoForm" id="custInfoForm" action="xpedxSaveCustInfo" method="post">
  	<s:hidden name="customerId" id="customerId" value='%{#sdoc.getAttribute("CustomerID")}' />
  	<s:hidden name="sapCustomerID" id="sapCustomerID" value='%{#_action.getSapCustomerID()}' />
     <s:hidden name='organizationCode' id='organizationCode' value='%{#sdoc.getAttribute("OrganizationCode")}' />
  <div class="container">
      <div id="mid-col-mil">
        <div>
          <s:url id='showLocationUrlId' namespace="/profile/org" action='xpedxShowLocations' ></s:url>
		    <s:hidden id="showLocationsUrl" name='showLocationsUrl' value='%{#showLocationUrlId}' />
      			<!-- breadcrumb 
      		<div id="searchBreadcrumb" class="page-title"><a href="%{#adminProfile}"><s:text name="admin.title" /></a> / <a href="#"><s:text name="admin.customerprofile.title" /></a> / <span class="breadcrumb-inactive"><s:text name="admin.customer.title" /></span>
          	-->
          	<br/>
			<s:set name="displayCustomerMap" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@custFullAddresses(#sdoc.getAttribute('CustomerID'),wCContext.storefrontId)" />
          	<strong>Account</strong>: <s:property value='%{#displayCustomerMap.get(#sdoc.getAttribute("CustomerID"))}' />
          	<br/>
          	<span class="txt-small underlines"><s:a href="#showLocationsDlg"  name="changeLocationCL" id="changeLocationCL" cssClass="underlines"> [Change Location]</s:a></span></div>
	        <br />
	        <div class="question">
	          <ul class="padding-top2 padding-bottom2">
	            <li> <strong>Central Contact Information </strong></li>
	            <li><a href="#"><img height="12" width="12" border="0" title="Optional information for online viewing only." alt="Optional information for online viewing only." src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png" /></a></li>
	          </ul>
	        </div>
	        <table width="100%" border="0" cellspacing="0" cellpadding="0" >
	          <tr>
<!-- 	            <td class="no-border-right padding0">Attention:</td> -->
	            <td width="18%" class="no-border-right padding0">Attention: </td> 
	            
	            <td colspan="3" class="no-border-right noBorder-left">
	            	<s:textfield id='txtAttnName' name='txtAttnName' maxlength="35"
						value='%{#extnElem.getAttribute("ExtnAttnName")}' cssClass="x-input" cssStyle="width:250px;" />	          
	            </td>
	          </tr>
	          <tr>
	            <td class="no-border-right padding0">Email Address:</td>
	            <td colspan="3" class="no-border-right">
	            	<s:textfield id='txtCustEmailAddress' name='txtCustEmailAddress' maxLength="150"
						value='%{#extnElem.getAttribute("ExtnCustEmailAddress")}' cssStyle="width:250px;" cssClass="x-input" maxlength="150"/>	            
	            </td>
	          </tr>
	          <s:hidden name="addtnlAddressID" value="%{#custAddressElem.getAttribute('CustomerAdditionalAddressID')}"></s:hidden>
	            <s:hidden name="addtnlAddressKey" value="%{#custAddressElem.getAttribute('CustomerAdditionalAddressKey')}"></s:hidden>
          <tr>
            <td class="no-border-right padding0">Address Line 1:</td>
            <td colspan="3" class="no-border-right">
            	<s:textfield id='txtAddress1' name='txtAddress1' maxlength="35"
					value='%{#custPersonInfoElem.getAttribute("AddressLine1")}' cssClass="x-input" cssStyle="width:400px;"/>            
            </td>
          </tr>
          <tr>
            <td class="no-border-right padding0">Address Line 2:</td>
            <td class="no-border-right" colspan="3">
    	    	<s:textfield id='txtAddress2' name='txtAddress2' tabindex="" 
					value='%{#custPersonInfoElem.getAttribute("AddressLine2")}' maxlength="35" cssClass="x-input" cssStyle="width:400px;" />            
            </td>
          </tr>
          <tr>
            <td class="no-border-right padding0">Address Line 3:</td>
            <td class="no-border-right" colspan="3">
            	<s:textfield id='txtAddress3' name='txtAddress3' tabindex=""  maxlength="35"
					value='%{#custPersonInfoElem.getAttribute("AddressLine3")}' cssClass="x-input" cssStyle="width:400px;" />            
            </td>
          </tr>
          <tr>
            <td class="no-border-right padding0">City:</td>
            <td class="no-border-right">
            	<s:textfield id='txtCity' name='txtCity' maxlength="20"
					value='%{#custPersonInfoElem.getAttribute("City")}' cssClass="x-input" cssStyle="width:230px;" />
            </td>
            <td class="no-border-right">State / Province:</td>
            <td width="32%" class="no-border-right">
            	<s:textfield id='txtState' name='txtState' maxlength="2"
					value='%{#custPersonInfoElem.getAttribute("State")}' cssClass="x-input" cssStyle="width:230px;" />
            </td>
          </tr>
          <tr>
            <td width="18%" class="no-border-right padding0">Postal Code:</td>
            <td width="31%" class="no-border-right">
            	<s:textfield id='txtZipCode' name='txtZipCode' maxlength="10"
					value='%{#custPersonInfoElem.getAttribute("ZipCode")}' cssClass="x-input" cssStyle="width:230px;" />            
            </td>
            <td width="19%" class="no-border-right">Country:</td>
            <td class="no-border-right">
            	<s:select tabindex="20" name="selCountryCode" id="selCountryCode"
					emptyOption="true" value='%{#custPersonInfoElem.getAttribute("Country")}'
					list="%{getCountriesMap()}" headerKey="-1" headerValue="-Select Country-" cssClass="x-input" cssStyle="width:230px;"/>
            </td>
          </tr>
          <tr>
            <td class="no-border-right padding0">Phone 1:</td>
            <td class="no-border-right">
            	<s:textfield id='txtPhone1' name='txtPhone1' maxlength="10"
					value='%{#extnElem.getAttribute("ExtnPhone1")}' cssClass="x-input phone-numeric" cssStyle="width:230px;" maxlength="15" 
					/>
            </td>
            <td class="no-border-right">Phone 2:</td>
            <td class="no-border-right">
            	<s:textfield id='txtPhone2' name='txtPhone2' maxlength="10"
					tabindex="" value='%{#extnElem.getAttribute("ExtnPhone2")}' cssClass="x-input  phone-numeric" cssStyle="width:230px;" maxlength="15"  
					/>
            </td>
          </tr>
          <tr class="padding-bottom1">
            <td class="no-border-right padding0">Fax 1:</td>
            <td class="no-border-right padding-bottom1">
            	<s:textfield id='txtFax1' name='txtFax1' maxlength="10"
					value='%{#extnElem.getAttribute("ExtnFax1")}' cssClass="x-input phone-numeric" cssStyle="width:230px;" maxlength="15"
					/>
            </td>
            <td class="no-border-right">Fax 2:</td>
            <td class="no-border-right padding-bottom1">
            	<s:textfield id='txtFax2' name='txtFax2' maxlength="10"
					value='%{#extnElem.getAttribute("ExtnFax2")}' cssClass="x-input phone-numeric" cssStyle="width:230px;" maxlength="15" 
					 />
            </td>
          </tr>
</table> 
<table width="100%" border="0" cellspacing="0" cellpadding="0" >

    <tr>
					<s:action name="getCustomerQuickLinks" namespace="/profile/org" executeResult="true" >
			  			<s:param name='createSelected' value='true'/>
			  			<s:param name="customerId" value='%{#sdoc.getAttribute("CustomerID")}' />
			      		<s:param name='organizationCode' value='%{#sdoc.getAttribute("OrganizationCode")}' />
			  		</s:action>
	</tr>
	<tr>
    <td width="100%" class="no-border-right padding0" valign="top">
      <div class="question">
        <ul class="padding-top3 padding-bottom2">
          <li> <strong>Custom Fields</strong></li>
          <li><a href="#"><img height="12" width="12" border="0" title="Customized line level fields." alt="Customized line level fields." src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"/></a></li>
        </ul>
      </div>
      <table id="customerFieldsTable" style="width:100%" class="standard-table">
        <tbody>
          <tr class="table-header-bar no-border">
          	<td class="no-border table-header-bar-left"width="201"><span class="white txt-small white">Field Name</span></td>
          	<td width="357" align="left" class="no-border-right table-header-bar-right"><span class="white txt-small white">Site Label</span></td>
          </tr>
          <s:if test="(#_action.IsCustLineAccNoFlag())">  
          <tr>
            <td>Line Account #: </td>
            <td valign="top" >
            	<s:textfield id='CustLineAccNoLabel' readonly="%{!#_action.IsCustLineAccNoFlag()}" cssClass="input-details-cart  x-input" tabindex="12"
					maxlength="25" cssStyle="width: 180px;" name='CustLineAccNoLabel' size="25" value='%{#extnElem.getAttribute("ExtnCustLineAccLbl")}'/>
            </td>
          </tr>
          </s:if>
            <s:else>
            	<s:hidden name='CustLineAccNoLabel' value='%{#extnElem.getAttribute("ExtnCustLineAccLbl")}'/>
            </s:else>
           <s:if test="(#_action.IsCustLinePONoFlag())">
            <tr class="odd" >
              <td >Line PO #: </td>
              <td valign="top">
              	<s:text name="Line PO #"/>
              </td>
            </tr>
            </s:if>
            <s:if test="(#_action.IsCustLineField1Flag())">
          <tr class="odd">
            <td >Line Field #1: </td>
            <td valign="top">
            	<s:textfield id='CustLineField1Label' readonly="%{!#_action.IsCustLineField2Flag()}" cssClass="input-details-cart  x-input"
					maxlength="25" cssStyle="width: 180px;" name='CustLineField1Label' size="25" tabindex="" value='%{#extnElem.getAttribute("ExtnCustLineField1Label")}'/>
			</td>
          </tr>
          </s:if>
            <s:else>
            	<s:hidden name='CustLineField1Label' value='%{#extnElem.getAttribute("ExtnCustLineField1Label")}'/>
            </s:else>
            <s:if test="(#_action.IsCustLineField2Flag())">
            <tr>
              <td>Line Field #2: </td>
              <td valign="top">
              <s:textfield id='CustLineField2Label' readonly="%{!#_action.IsCustLineField2Flag()}" cssClass="input-details-cart  x-input"
						maxlength="25" cssStyle="width: 180px;" name='CustLineField2Label' size="25" tabindex="" value='%{#extnElem.getAttribute("ExtnCustLineField2Label")}'/>
              </td>
            </tr>
            </s:if>
            <s:else>
            	<s:hidden name='CustLineField2Label' value='%{#extnElem.getAttribute("ExtnCustLineField2Label")}'/>
            </s:else>
            <s:if test="(#_action.IsCustLineField3Flag())">
            <tr class="odd" >
              <td >Line Field #3: </td>
              <td valign="top">
              	<s:textfield id='CustLineField3Label' readonly="%{!#_action.IsCustLineField3Flag()}" cssClass="input-details-cart  x-input"
						maxlength="25" cssStyle="width: 180px;" name='CustLineField3Label' size="25" tabindex="" value='%{#extnElem.getAttribute("ExtnCustLineField3Label")}'/>
              </td>
            </tr>
            </s:if>
            <s:else>
            	<s:hidden name='CustLineField3Label' value='%{#extnElem.getAttribute("ExtnCustLineField3Label")}'/>
            </s:else>
          </tbody>
        </table>
      <%-- <div id="table-bottom-bar">
        <div id="table-bottom-bar-L"></div>
        <div id="table-bottom-bar-R"></div>
        </div> --%>
      </td>
	</tr>
	</table>

        
     <div class="clearview">&nbsp;</div>
         <div class="clearview"> <div>
            <ul class="float-right">
              <li class="float-left margin-10"><a href="#" onclick="javascript:window.location.reload();"  class="grey-ui-btn"><span>Cancel</span></a></li>
              <li class="float-right"><a href="javascript:callSave()" class="green-ui-btn"><span>Save</span></a></li>
            </ul>
          </div></div>
           <div class="clearview">&nbsp;</div>
	           <s:if test="%{#_action.isSuccess()}">
					<div class="success" id="successMsgFor_save" style="display : inline; float: right"/>Account Profile has been updated successfully.</div>
				</s:if>
              <div class="clearview textAlignCenter">Last modified by <s:property value="%{getContactFirstName()}"/> <s:property value="%{getContactLastName()}"/> on <s:property value="#_action.getLastModifiedDateToDisplay()"/></div>
        
        <!-- Pricing -->
         
        <div class="clearview">&nbsp;</div>
         
      </div>
    </s:form>
    </div>
    </div>
    <!-- End Pricing -->
   
  <!-- Footer Start --> <s:action name="xpedxFooter" executeResult="true"
	namespace="/common" /> <!-- Footer End -->


<div style="display: none;">
<div title="Add New Quick Link" id="newQL"
	style="width: 400px; height: 220px; overflow: auto;"
	class="xpedx-light-box">
<h2>Add New Quick Link</h2>
<p><strong></strong></p>

<table class="form" width="100%" style="clear: both">
	<tr>
		<td width="24%" valign="top" class="noBorders-fff padding-td"
			style="border-left: solid 1px #fff;">Name:</td>
		<td width="76%" valign="top" class="noBorders-fff padding-td"><s:textfield
			cssClass="x-input" id="linkName" size="36" name="linkName" value='' maxlength="35"/></td>
	</tr>
	<tr>
		<td width="24%" valign="top" class="noBorders-fff padding-td"
			style="border-left: solid 1px #fff;">URL:</td>
		<td width="76%" valign="top" class="noBorders-fff padding-td"><s:textfield
			cssClass="x-input" id="url" size="36" name="url" value='' maxlength="150" /></td>
	</tr>
</table>
<div></div>
<ul id="tool-bar" class="tool-bar-bottom float-right"  style="padding-top: 30px;">
	<li><s:a cssClass="grey-ui-btn "
		href="#" onclick="javascript:$.fancybox.close();">
		<span>Cancel</span>
	</s:a></li>
	<li><s:a href="javascript:addRow()" cssClass="green-ui-btn">
		<span>Add</span>
	</s:a></li>
</ul>

</div>
</div>


<div style="display: none;">
<div title="Showing the Locations" id="showLocationsDlg">
	<div id="showLocationsDiv">
		
	</div>

</div>
</div>
<script type="text/javascript">
changeTableCssClass();

function changeTableCssClass(){
	var table = document.getElementById('customerFieldsTable');
	var rowCount = table.rows.length;	
	for(var i=rowCount; i>=1; i--) {
		var row = table.rows[i];
		if (null != row) {
			if (i%2 != 0)
			{
				row.setAttribute('class', 'odd');
			}
			else
			{
				row.setAttribute('class', '');
			}			
		}         			
	}
}
</script>

    <!-- end main  -->
  
<!-- end container  -->
</body>
</html>