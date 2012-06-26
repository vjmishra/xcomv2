<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 

<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<% request.setAttribute("isMergedCSSJS","true"); %>
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
<s:set name='_action' value='[0]'/>
<s:set name='xmlUtil' value="#_action.getXMLUtils()" />
<s:set name='sdoc' value="getOutputDoc().getDocumentElement()" />
<s:set name='billToCustElem' value="billToCustElem" />
<s:set name='extnElem' value='#xmlUtil.getChildElement(#sdoc, "Extn")'/>
<s:set name='billToExtnElem' value='#xmlUtil.getChildElement(#billToCustElem, "Extn")'/>
<s:set name='currencyList' value='#xmlUtil.getElements(#sdoc, "CustomerCurrencyList/CustomerCurrency")'/>
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils' id='wcUtil' />

<s:set name='custPersonInfoElem' value='shipToAddress'/>
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils' id='wcUtil' />
<s:set name='custPersonInfoExtnElem' value='#xmlUtil.getChildElement(#custPersonInfoElem,"Extn")'/>
<s:set name='assoBillToAddressElem' value='associatedBillToAddress' />
<s:set name='assoBillToPersonInfoElem' value='#xmlUtil.getChildElement(#assoBillToAddressElem,"PersonInfo")'/>
<s:set name='assoBillToPersonInfoExtnElem' value='#xmlUtil.getChildElement(#assoBillToPersonInfoElem,"Extn")'/>

<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<!-- begin styles. -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/ADMIN<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<![endif]-->
<!-- end styles -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>		
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-jquery-headder<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- javascript

<script type="text/javascript" src="<s:url value='/xpedx/js/global/ext-base.js'/>"></script>
<script type="text/javascript" src="<s:url value='/xpedx/js/global/ext-all.js'/>"></script>
<script type="text/javascript" src="<s:url value='/xpedx/js/global/validation.js'/>"></script>
<script type="text/javascript" src="<s:url value='/xpedx/js/global/dojo.js'/>"></script>
<script type="text/javascript" src="<s:url value='/xpedx/js/global/dojoRequire.js'/>"></script>
<script type="text/javascript" src="<s:url value='/xpedx/js/theme/theme-1/theme.js'/>"></script>
<script type="text/javascript" src="<s:url value='/xpedx/js/catalog/catalogExt.js'/>"></script>
 -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/profile/org/xpedxCustomerLocations<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- carousel scripts js   -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!--  to restrict numeric values in spending limit  -->

<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<!--[if IE]><script src="<s:url value='/xpedx/other_libs/excanvas_r3/excanvas.js'/>" type="text/javascript" charset="utf-8"></script><![endif]-->
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF --><script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui<s:property value='#wcUtil.xpedxBuildKey' />.js" language="javascript">
	
</script>


<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- Facy Box (Lightbox/Modal Window -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
 

<script type="text/javascript">
	$(document).ready(function() {
		$(document).pngFix();
		$("#varous1").fancybox();
		$("#various2").fancybox();
		$("#various3").fancybox();

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
				'width' 			: 670,
				'height' 			: 420,
				//XNGTP - JIRA- 489 
				'onClosed' : function(){				
			    	document.getElementById("showLocationsDiv").innerHTML = '';
			    },		
									
			});	
$("#various4").fancybox();
$("#various5").fancybox();
});
	
function maxLength(field,maxlimit) {
	    if (field.value.length > maxlimit) // if too long...trim it!
	        alert(field.title + ' should not exceed '+maxlimit+ ' characters');
	        field.value = field.value.substring(0, maxlimit);
	        return false;
	    }
	
</script> 

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.numeric<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.maskedinput-1.3<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript">
		$(document).ready(function() {
		$('.phone-numeric').numeric(false); 
		$("#txtExtnPhone2").mask("999 999-9999");
		$("#txtExtnFax2").mask("999 999-9999");
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
	max-height: 200px;
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

<title><s:property value="wCContext.storefrontId" /> - <s:text name="admin.shipto.title" /></title>
 
</head>
<body class="ext-gecko ext-gecko3">
<div id="main-container">
  <div id="main">
  	<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
  	<div class="container ship-to">
      <!-- breadcrumb -->
      <div id="mid-col-mil">
    <%--   <div id="searchBreadcrumb"><a href="%{#adminProfile}"><s:text name="admin.title" /></a> / <a href="#"><s:text name="admin.customerprofile.title" /></a> / <span class="breadcrumb-inactive"><s:text name="admin.shipto.title" /></span> 
      <a href="#"><span class="print-ico-xpedx">
      		<img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/print-icon.gif" width="16" height="15" alt="Print This Page" />Print Page</span></a>     </div> --%>
      		<br/>
   	 	<div class="underlines">
	 	 	<s:url id='showLocationUrlId' namespace="/profile/org" action='xpedxShowLocations' ></s:url>
	    	<s:hidden id="showLocationsUrl" name='showLocationsUrl' value='%{#showLocationUrlId}' />
         	<strong>Ship-To</strong>: <s:property value="displayCustomerFormat"/><br/>
         	<span class="txt-small underlines">
			<a href="#showLocationsDlg" class="underlines" name="changeLocationCL" id="changeLocationCL">[Change Location]</a></span></div>
   	         <div class="clearview">&nbsp;</div>
        <s:form name="shipToInfo" action="xpedxSaveShipToInfo" namespace="/profile/org">
        <div id="requestform">
       			<s:hidden name="customerId" id="customerId" value="%{#sdoc.getAttribute('CustomerID')}" />
       			<s:hidden name="OrgCode" id="OrgCode" value="%{#sdoc.getAttribute('OrganizationCode')}" />
       			<s:hidden name="organizationCode" id="organizationCode" value="%{#sdoc.getAttribute('OrganizationCode')}" />
        	 <table class="form" width="100%" border="0" cellspacing="0" cellpadding="0">
   	 			 <tr>
				    <td width="10%" class="no-border-right">Attention:&nbsp;
				    	<span class="float-right help"><a href="#">
			   	 			<img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png" border="0" title="Approved Ship-To locations which cannot be edited online. If data is incorrect or a new Ship-To needs to be setup, please call your CSR." style="display:inline;"></a>
			   	 		</span>
				    </td>
				    <td colspan="3" class="no-border-right noBorder-left">
				    	<s:property value='%{#extnElem.getAttribute("ExtnAttnName")}'/>
			   	 	</td>			   	 			
			    </tr>
		     	<tr>
				    <td class="no-border-right">E-mail Address:</td>
				    <td colspan="3" class="no-border-right">
				    	<s:textfield id='txtCustEmailAddress' maxlength="150" name='txtCustEmailAddress' size="25" tabindex="" 
						value='%{#extnElem.getAttribute("ExtnCustEmailAddress")}' cssStyle="width:230px;" cssClass="x-input"/>
				    </td>
		   		</tr>
		   		<tr>
					    <td class="no-border-right" valign="top">Address:</td>
					    <td colspan="3" class="no-border-right">
				    		<s:property value='%{#custPersonInfoElem.getAttribute("AddressLine1")}'/>
				    		<s:if test="%{#custPersonInfoElem.getAttribute('AddressLine2')!= ''}">
				    			<br/><s:property value='%{#custPersonInfoElem.getAttribute("AddressLine2")}'/>
				    		</s:if>
				    		<s:if test="%{#custPersonInfoElem.getAttribute('AddressLine3')!= ''}">
				    			<br/><s:property value='%{#custPersonInfoElem.getAttribute("AddressLine3")}'/>
				    		</s:if>
				    		<s:if test="%{#custPersonInfoElem.getAttribute('City')!= '' || #custPersonInfoElem.getAttribute('State')!= ''|| #custPersonInfoElem.getAttribute('ZipCode')!= '' || #custPersonInfoElem.getAttribute('ZipCode')!= '' }">
				    			<br/>
				    		</s:if>
				    		<s:if test="%{#custPersonInfoElem.getAttribute('City')!= ''}">
				    			<s:property value='%{#custPersonInfoElem.getAttribute("City")}'/>,
				    		</s:if>
				    		<s:if test="%{#custPersonInfoElem.getAttribute('State')!= ''}">
				    			<s:property value='%{#custPersonInfoElem.getAttribute("State")}'/>&nbsp;
				    		</s:if> 
				    		<s:if test="%{#custPersonInfoElem.getAttribute('ZipCode')!= ''}">
				    			 <s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedZipCode(#custPersonInfoElem.getAttribute('ZipCode'))"/>&nbsp;
				    		</s:if>
				    		<s:if test="%{#custPersonInfoElem.getAttribute('Country')!= ''}">
				    			<s:property value='%{#custPersonInfoElem.getAttribute("Country")}'/>
				    		</s:if>
					    </td>
				    </tr>
			   <%-- <s:if test="%{#custPersonInfoElem.getAttribute('AddressLine2')!= ''}">
				    <tr>
					    <td class="no-border-right">&nbsp;</td>
					    <td colspan="3" class="no-border-right">
			             		<s:property value='%{#custPersonInfoElem.getAttribute("AddressLine2")}'/>
					    </td>
				    </tr>
			    </s:if>
		     	<s:if test="%{#custPersonInfoElem.getAttribute('AddressLine3') != ''}">
				    <tr>
					    <td class="no-border-right">&nbsp;</td>
					    <td colspan="3" class="no-border-right">
			             		<s:property value='%{#custPersonInfoElem.getAttribute("AddressLine3")}'/>
					    </td>
				    </tr>
			    </s:if>
			    <tr>
				    <td class="no-border-right">&nbsp;</td>
				    <td colspan="3" class="no-border-right">
			    		<s:property value='%{#custPersonInfoElem.getAttribute("City")}'/>, 
			              	<s:property value='%{#custPersonInfoElem.getAttribute("State")}'/>&nbsp;
			              	<s:property value='%{#custPersonInfoElem.getAttribute("ZipCode")}'/>&nbsp;
			              	<s:property value='%{#custPersonInfoElem.getAttribute("Country")}'/>
				    </td>
			    </tr>  --%> 
			    <tr>
				    <td class="no-border-right">Phone 1:</td>
				    <td width="31%" class="no-border-right">
				    <s:if test="%{#extnElem.getAttribute('ExtnPhone1')!='0000000000'}  ">
				    <s:set id="Phone1FormatChange" name="Phone1FormatChange" value="%{#extnElem.getAttribute('ExtnPhone1')}" />
				   	<s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormatPhone(#Phone1FormatChange)'/>
				    </s:if>
					<s:hidden name='txtExtnPhone1' value='%{#extnElem.getAttribute("ExtnPhone1")}'></s:hidden>
				    	<%--<s:textfield id='txtExtnPhone1' name='txtExtnPhone1' size="25" tabindex="" disabled="true"
						value='%{#extnElem.getAttribute("ExtnPhone1")}' cssStyle="width:230px;" cssClass="x-input"/>  --%> 
				    </td>
				    <td width="9%" class="no-border-right">Phone 2:</td>
				    <td width="46%" class="no-border-right">
				    <s:textfield id='txtExtnPhone2' name='txtExtnPhone2' maxlength="10" size="25" tabindex="" value='%{#extnElem.getAttribute("ExtnPhone2")}' cssStyle="width:230px;" cssClass="x-input phone-numeric"/>
				     <%--<input type="text" class="x-input" style="width:250px;" onkeyup="javascript:backspacerUP(this,event);" onkeydown="javascript:backspacerDOWN(this,event);" name="txtInput" /> 
				    	--%>
			    	</td>
			    </tr>
			    <tr>
				    <td class="no-border-right">Fax 1:</td>
				    <td class="no-border-right">
				   
				    <s:if test="%{#extnElem.getAttribute('ExtnFax1')!='0000000000'} ">
				     <s:set id="Fax1FormatChange" name="Fax1FormatChange" value="%{#extnElem.getAttribute('ExtnFax1')}" />									    
					 <s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormatPhone(#Fax1FormatChange)'/>
					</s:if>
					    	<s:hidden name='txtExtnFax1' value='%{#extnElem.getAttribute("ExtnFax1")}'></s:hidden>
				    <%-- <s:textfield id='txtExtnFax1' name='txtExtnFax1' size="25" tabindex="" disabled="true"
						value='%{#extnElem.getAttribute("ExtnFax1")}' cssStyle="width:230px;" cssClass="x-input"/>  --%>
				    </td>
				    <td class="no-border-right">Fax 2:</td> 				    
				    <td class="no-border-right">
				    <s:textfield id='txtExtnFax2' name='txtExtnFax2' maxlength="10" size="25" tabindex="" 
						value='%{#extnElem.getAttribute("ExtnFax2")}' cssStyle="width:230px;" cssClass="x-input phone-numeric"/>
				    <%--<input type="text" class="x-input" style="width:250px;" onkeyup="javascript:backspacerUP(this,event);" onkeydown="javascript:backspacerDOWN(this,event);" name="txtInput2" /> 
				    	--%>
				    </td>
			    </tr>	 	
        	</table>
        </div>
        <div class="clearview">&nbsp;</div>
        <div id="clearview" class="float-right">    
            <ul id="cart-actions">
               
               	<li><a href="#" onclick="javascript:window.location.reload();" class="grey-ui-btn"><span>Cancel</span></a></li>
                <li><a class="green-ui-btn" href="javascript:(function(){document.shipToInfo.submit();})();"><span>Save</span></a></li>
            </ul>
        </div>
        <div class="clearview">&nbsp;</div>
        <%--Code Added For XNGTP-3196 --%>
        <s:if test="%{#_action.isSuccess()}">
	<div class="success" id="successMsgFor_save" style="display : inline; float: right"/>Ship-To Profile has been updated successfully.</div>
	</s:if>
         <%-- End fix for XNGTP-3196 --%>
         <s:if test='modifiedUser!=""'>
        <div class="clearview textAlignCenter">Last modified by <s:property value="modifiedUser"/> on <s:property value="modifiedDate"/></div>
        <div class="clearview">&nbsp;</div>
        </s:if>
       
        </s:form>
        <div style="display: none;">
			<div title="Showing the Locations" id="showLocationsDlg" >
				<div id="showLocationsDiv">
					
				</div>
			</div>
		</div>
        </div>
        </div>
     	</div>
    </div>
    <s:action name="xpedxFooter" executeResult="true" namespace="/common" />
	</body> 
</html> 	 
        	 
        	 
        	 
        	 