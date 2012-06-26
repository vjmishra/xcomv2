<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<% request.setAttribute("isMergedCSSJS","true"); %>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%><s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean" id="xpedxUtilBean" />
<s:set name='_action' value='[0]'/>
<s:set name='xmlUtil' value="#_action.getXMLUtils()" />
<s:set name='sdoc' value="getOutputDoc().getDocumentElement()" />
<s:set name='extnElem' value='#xmlUtil.getChildElement(#sdoc, "Extn")'/>
<s:set name='currencyList' value='#xmlUtil.getElements(#sdoc, "CustomerCurrencyList/CustomerCurrency")'/>

<s:set name='custPersonInfoElem' value='billToAddress'/>
<s:set name='custPersonInfoExtnElem' value='#xmlUtil.getChildElement(#custPersonInfoElem,"Extn")'/>
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils' id='wcUtil' />
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

function Clear()
{	
	document.getElementById("txtCustEmailAddress").value="";
	document.getElementById("txtExtnPhone2").value="";
	document.getElementById("txtExtnFax2").value="";
	document.getElementById("invoiceEmailIdText").value="";	
}

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
	
	function addEmailToInvoiceList(){
		var newEmailAddr = document.getElementById("NewInvoiceEmailAddr").value;
		var aEAL = document.getElementById("invoiceEmailIdList");
		aEAL.options[aEAL.options.length] = new Option(newEmailAddr,newEmailAddr);
		
		document.getElementById("invoiceEmailIdText").value = document.getElementById("invoiceEmailIdText").value + newEmailAddr + ",";
		document.getElementById("NewInvoiceEmailAddr").value = "";
				
	}

	function removeEmailFromInvoiceList()
	{
		var i;
		var aEAL = document.getElementById("invoiceEmailIdList");
		var listString = "";
		for(i=0;i<aEAL.options.length;i++)
		{	
			if(aEAL.options[i].selected)
				aEAL.remove(i);
			else{
				listString += aEAL.options[i].value + ","
			}	
		}
		document.getElementById("invoiceEmailIdText").value = listString;
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

<title><s:property value="wCContext.storefrontId" /> - <s:text name="admin.billto.title" /></title> 

</head>
<!-- END swc:head -->
<body class="ext-gecko ext-gecko3">
	<div id="main-container">
		<div id="main">
			<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
			
			<div class="container bill-to">
		      <!-- breadcrumb -->
		      <div id="mid-col-mil">
		      <!-- 
		      <div id="searchBreadcrumb" class="page-title">
		      	<a href="#"><s:text name="admin.title" /></a> / <a href="#"><s:text name="admin.customerprofile.title" /></a> / <span class="breadcrumb-inactive"><s:text name="admin.billto.title" /></span> 
		      </div> -->
		      	<br/>
		      	<div class="underlines">
	 	 	<s:url id='showLocationUrlId' namespace="/profile/org" action='xpedxShowLocations' ></s:url>
	    	<s:hidden id="showLocationsUrl" name='showLocationsUrl' value='%{#showLocationUrlId}' />
         	<strong>Bill-To</strong>: <s:property value="displayCustomerFormat"/>
         	<br/>
         	<span class="txt-small underlines">
			<a href="#showLocationsDlg" class="underlines" name="changeLocationCL" id="changeLocationCL">[Change Location]</a></span></div>
			<div class="clearview">&nbsp;</div>
        		<s:form name="billToInfo" action="xpedxSaveBillToInfo" namespace="/profile/org">
        		<div id="requestform">
        			<s:hidden name="customerId" id="customerId" value="%{#sdoc.getAttribute('CustomerID')}" />
        			<s:hidden name="OrgCode" id="OrgCode" value="%{#sdoc.getAttribute('OrganizationCode')}" />
        			<s:hidden name="organizationCode" id="organizationCode" value="%{#sdoc.getAttribute('OrganizationCode')}" />
       	 		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="form">
        	 		<tr>
					    <td width="13%" class="no-border-right">Attention:
							<span class="float-right" style="margin-right:45px"><a href="#">
				   	 			<img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"  height="12" width="12" border="0" title=" Invoice billing data which cannot be edited online. If data is incorrect please call customer service. " alt=" Invoice billing data which cannot be edited online. If data is incorrect please call customer service." /></a>
				   	 		</span>
						</td>
					    <td colspan="3" class="no-border-right noBorder-left">
					    	<s:property value='%{#extnElem.getAttribute("ExtnAttnName")}'/>				   	 		
				   	 	</td>			   	 			
				    </tr>
				    <tr>
					    <td class="no-border-right">E-mail Address:</td>
					    <td colspan="3" class="no-border-right">
					    	<s:textfield id='txtCustEmailAddress' name='txtCustEmailAddress' size="25" maxlength="150" tabindex="" 
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
				    </tr> --%> 
			   		<tr>  
				    <td class="no-border-right">Phone 1:</td>
				
					    <td width="31%" class="no-border-right"> 	
					     <s:if test="%{#extnElem.getAttribute('ExtnPhone1')!='0000000000'} ">
					    <s:set id="Phone1FormatChange" name="Phone1FormatChange" value="%{#extnElem.getAttribute('ExtnPhone1')}" />								    				    
				        <s:property value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormatPhone(#Phone1FormatChange)'/>
					    	</s:if>
					    	
					    	<s:hidden name='txtExtnPhone1' value='%{#extnElem.getAttribute("ExtnPhone1")}'></s:hidden>
					    	 <%--<s:textfield id='txtExtnPhone1' name='txtExtnPhone1' size="25" tabindex="" disabled="true"
							value='%{#extnElem.getAttribute("ExtnPhone1")}' cssStyle="width:230px;" cssClass="x-input"/> --%>
					    </td>
					    <td width="9%" class="no-border-right">Phone 2:</td>
					    <td width="46%" class="no-border-right">
					    <s:textfield id='txtExtnPhone2' name='txtExtnPhone2' maxlength="10" size="25" tabindex="" 
							value='%{#extnElem.getAttribute("ExtnPhone2")}' cssStyle="width:230px;" cssClass="x-input phone-numeric" />
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
							value='%{#extnElem.getAttribute("ExtnFax1")}' cssStyle="width:230px;" cssClass="x-input"/> --%>
					    </td>
					    <td class="no-border-right">Fax 2:</td> 				    
					    <td class="no-border-right">
					    <s:textfield id='txtExtnFax2' name='txtExtnFax2' maxlength="10" size="25" tabindex="" 
							value='%{#extnElem.getAttribute("ExtnFax2")}' cssStyle="width:230px;" cssClass="x-input phone-numeric"/>
					    <%--<input type="text" class="x-input" style="width:250px;" onkeyup="javascript:backspacerUP(this,event);" onkeydown="javascript:backspacerDOWN(this,event);" name="txtInput2" /> 
					    	--%>
					    </td>
				    </tr>	 
        	 		<tr>
					    <td class="no-border-right">Invoice Preference: </td>
					    <td class="no-border-right" colspan="3">
			    			<s:property value='%{#extnElem.getAttribute("ExtnInvoiceDistMethod")}'/>,&nbsp;
					    	Currency Code: <s:property value="%{#extnElem.getAttribute('ExtnCurrencyCode')}"/>
					   </td>
				    </tr>
				    <s:set name="salesProfessionalSet" value="%{'false'}" />
				    <s:iterator value="getSalesRepList()" id="salesRep" status="rowStatus">
          				<s:set name='salesRepUser' value='%{#xmlUtil.getChildElement(#salesRep,"YFSUser")}' />
          				<s:set name='salesRepUserInfo' value='%{#xmlUtil.getChildElement(#salesRepUser,"ContactPersonInfo")}' />
          				<s:set name="salesRepPhone" value="%{#salesRepUserInfo.getAttribute('DayPhone')}"/>
          				<s:set name="fmtSalesRepPhone" value='#xpedxUtilBean.getFormattedPhone( #salesRepPhone )' />
          				<s:if test="#salesRepUserInfo!=null" >
	          				<s:if test="#salesProfessionalSet == 'false'">
		          				<tr class="padding-bottom1">
						            <td valign="top" class="no-border-right padding0">Sales Professional:</td>
						            <td colspan="3" valign="top" class="underlines no-border-right padding-bottom1">
						            <s:property value="%{#salesRepUser.getAttribute('Username')}"/><br/>
					              	<span class="grey-italic">
					              	<s:if test='%{#fmtSalesRepPhone != ""}'>
							              	<s:property value='%{#fmtSalesRepPhone}'/><br/>
							         </s:if>
							         <a href="mailto:<s:property value="%{#salesRepUserInfo.getAttribute('EMailID')}"/>"><s:property value="%{#salesRepUserInfo.getAttribute('EMailID')}"/></a>
							         </span></td>
					             <!-- <s:if test="%{#salesRepUserInfo.getAttribute('DayPhone') != ''}">
					              		<s:property value="%{#salesRepUserInfo.getAttribute('DayPhone')}"/>, 
					              	</s:if>	 -->	
							   </tr>		          				
						    	<s:set name="salesProfessionalSet" value="%{true}" />	          				
	          				</s:if>
	          				<s:else>
	          					<tr class="padding-bottom1">
						            <td valign="top" class="no-border-right padding0">&nbsp;</td>
						            <td colspan="3" valign="top" class="underlines no-border-right padding-bottom1">
						            <s:property value="%{#salesRepUser.getAttribute('Username')}"/><br />
					              	<span class="grey-italic">
					              	
					              	<s:if test='%{#fmtSalesRepPhone!= ""}'>
							              <s:property value='%{#fmtSalesRepPhone}'/><br/>
							         </s:if>
					             <!--  	<s:if test="%{#salesRepUserInfo.getAttribute('DayPhone') != ''}">
					              		<s:property value="%{#salesRepUserInfo.getAttribute('DayPhone')}"/>, 
					              	</s:if>		 -->		              	
							    	
								<a href="mailto:<s:property value="%{#salesRepUserInfo.getAttribute('EMailID')}"/>" ><s:property value="%{#salesRepUserInfo.getAttribute('EMailID')}"/></a>
							    	 </span></td>
					          	</tr>
	          				</s:else>
	          			</s:if>
          			</s:iterator>
          			
          			<s:set name="csr1ListEle" value="csr1UserEle" />
					<s:set name="csr2ListEle" value="csr2UserEle" />
					<s:if test='#csr1ListEle != null'>
						<s:set name="csr1UserName" value='#xmlUtil.getAttribute(#csr1ListEle,"Username")' />
						<s:set name="csr1Ele" value='#xmlUtil.getChildElement(#csr1ListEle,"ContactPersonInfo")' />
						<s:set name="csr1FirstName" value='#xmlUtil.getAttribute(#csr1Ele,"FirstName")' />
						<s:set name="csr1LastName" value='#xmlUtil.getAttribute(#csr1Ele,"LastName")' />
						<s:set name="csr1EMailID" value='#xmlUtil.getAttribute(#csr1Ele,"EMailID")' />
						<s:set name="csr1Phone" value='#xmlUtil.getAttribute(#csr1Ele,"DayPhone")' />
						<s:set name="fmtCsr1Phone" value='#xpedxUtilBean.getFormattedPhone( #csr1Phone )' />
					</s:if>
					<s:if test='#csr2ListEle != null'>
						<s:set name="csr2UserName" value='#xmlUtil.getAttribute(#csr2ListEle,"Username")' />
						<s:set name="csr2Ele" value='#xmlUtil.getChildElement(#csr2ListEle,"ContactPersonInfo")' />
						<s:set name="csr2FirstName" value='#xmlUtil.getAttribute(#csr2Ele,"FirstName")' />
						<s:set name="csr2LastName" value='#xmlUtil.getAttribute(#csr2Ele,"LastName")' />
						<s:set name="csr2EMailID" value='#xmlUtil.getAttribute(#csr2Ele,"EMailID")' />
						<s:set name="csr2Phone" value='#xmlUtil.getAttribute(#csr2Ele,"DayPhone")' />
						<s:set name="fmtCsr2Phone" value='#xpedxUtilBean.getFormattedPhone( #csr2Phone )' />
					</s:if>
					<s:set name="customerService" value="%{'false'}" />
					<s:if test='#csr1Ele != null'>
						<s:if test="%{#customerService == 'false'}">
							<tr class="padding-bottom1">
				            	<td valign="top" class="no-border-right padding0">Customer Service:</td>
				            	<td colspan="3" valign="top" class="underlines no-border-right padding-bottom1">
				            	<s:property value='%{#csr1UserName}'/><br/>
				            		<a href="mailto:<s:property value="#csr1EMailID"/>" ><s:property value="#csr1EMailID"/></a>
									<br/>
				            	<span class="grey-italic">
				            		<s:if test='%{#fmtCsr1Phone != ""}'>
							          	<s:property value='%{#fmtCsr1Phone}'/>
							          	 <br/>
							        </s:if>
								</span></td>
				          	</tr>
				          	<s:set name="customerService" value="%{true}" />
						</s:if>
						<s:else>
							<tr class="padding-bottom1">
				            	<td valign="top" class="no-border-right padding0">&nbsp; </td>
				            	<td colspan="3" valign="top" class="underlines no-border-right padding-bottom1">
				            	<s:property value='%{#csr1UserName}'/><br />
				            	 <a href="mailto:<s:property value="#csr1EMailID"/>" ><s:property value="#csr1EMailID"/></a>
				            		<br/>
						   
				            	<span class="grey-italic">
				            	
				            		<s:if test='%{#fmtCsr2Phone != ""}'>
						           		<s:property value='%{#fmtCsr2Phone}'/><br/>
						        	</s:if>
	                               
				            	</span></td>
				          	</tr>
						</s:else>
					</s:if>
					
					<s:if test='#csr2Ele != null'>
						<s:if test="#customerService == 'false'">
							<tr class="padding-bottom1">
				            	<td valign="top" class="no-border-right padding0">Customer Service:</td>
				            	<td colspan="3" valign="top" class="underlines no-border-right padding-bottom1">
				            	<s:property value='%{#csr2UserName}'/><br />
				            	<a href="mailto:<s:property value="#csr2EMailID"/>" ><s:property value="#csr2EMailID"/></a><br/>
				            	<span class="grey-italic">
  					          
				            	<s:if test="%{#csr2Phone != ''}">
				            		<s:property value="#csr2Phone"/>,
				            		<br/>
				            	</s:if>
				            	</span></td>
				          	</tr>
						</s:if>
						<s:else>
							<tr class="padding-bottom1">
				            	<td valign="top" class="no-border-right padding0">&nbsp; </td>
				            	<td colspan="3" valign="top" class="underlines no-border-right padding-bottom1">
				            	<s:property value='%{#csr2UserName}'/>
				            	<br />
								<a href="mailto:<s:property value="#csr2EMailID" />" ><s:property value="#csr2EMailID"/></a><br/>
				            	
								<span class="grey-italic">
				            	<s:if test="%{#fmtCsr2Phone != ''}">
				            		<s:property value="#fmtCsr2Phone"/>
				            		 <br/>
				            	</s:if>
								</span></td>				          	
				            </tr>
	
						</s:else>
					</s:if>
        	 		<%--<tr>
		            	<td> Customer Name: </td>
			            <td valign="top"><s:property value='%{#extnElem.getAttribute("ExtnCustomerName")}'/></td>
		          	</tr>
		          	<tr>
			            <td> Account #: </td>
		            	<td valign="top"><s:property value='%{#extnElem.getAttribute("ExtnLegacyCustNumber")}'/></td>
		          	</tr>
		          	<tr>
                    	<td valign="top"></td>
                    	<td valign="top"><s:property value='%{#extnElem.getAttribute("ExtnCustomerName")}'/></td>
                    </tr>
                    <tr>
		            	<td valign="top">Address:</td>
			            <td valign="top"><s:property value='%{#extnElem.getAttribute("ExtnCustomerName")}'/><br />
	             			<s:property value='%{#custPersonInfoElem.getAttribute("AddressLine1")}'/><br />
		             		<s:property value='%{#custPersonInfoElem.getAttribute("AddressLine2")}'/><br />
		             		<s:property value='%{#custPersonInfoElem.getAttribute("AddressLine3")}'/><br />
			             	<s:property value='%{#custPersonInfoElem.getAttribute("City")}'/>, 
			              	<s:property value='%{#custPersonInfoElem.getAttribute("State")}'/>&nbsp;
			              	<s:property value='%{#custPersonInfoElem.getAttribute("ZipCode")}'/>&nbsp;
			              	<s:property value='%{#custPersonInfoElem.getAttribute("Country")}'/></td>
	       			</tr>
	       			<tr>
			            <td>Phone:</td>
			            <td valign="top"><s:property value='%{#custPersonInfoElem.getAttribute("DayPhone")}'/></td>
	         		</tr>
		         		<tr>
			            <td>Fax:</td>
			            <td valign="top"><s:property value='%{#custPersonInfoElem.getAttribute("DayFaxNo")}'/></td>
	          		</tr>
	          		<tr>
                      	<td>Invoice Preference:</td>
                        <td valign="top"><s:property value='%{#extnElem.getAttribute("ExtnInvoiceDistMethod")}'/></td>
                    </tr>
                    <tr>
			            <td>Currency:</td>
			            <td valign="top">
			            	<s:iterator value='currencyList' id="currency" >
			            		<s:if test="%{#currency.getAttribute('IsDefaultCurrency')== true}">
			            			<s:property value="%{#currency.getAttribute('Currency')"/>
			            		</s:if>
			            	</s:iterator>
			            </td>
	          		</tr>
	          		<tr>
						<td>Primary Segment:</td>
                        <td valign="top"><s:property value='%{#extnElem.getAttribute("ExtnCustomerClass")}'/></td>
                    </tr>  
               	</table>
               	</div>
               	<div class="clearview">&nbsp;</div> 
               		<h2>Customer Support</h2>
               	<div class="clearview">&nbsp;</div>
               	<s:set name='salesRepList' value='salesRepList'/>
               	<s:if test='(#salesRepList != null && #salesRepList.size > 0)'>
               	<table id="mil-list-new" style="width:60%;">
               		<tr class="table-header-bar">
               			<td class="no-border table-header-bar-left"width="218"><span class="white txt-small"> Sales Professional Name</span></td>
                        <td width="110" align="left" class="no-border"><span class="white txt-small">Phone</span></td>
						<td width="226" align="left" class="no-border-right table-header-bar-right"><span class="white txt-small">Email Address</span></td>
          			</tr>
          			<s:iterator value="salesRepList" id="salesRep" status="rowStatus">
          				<s:set name='salesRepUser' value='%{#xmlUtil.getChildElement(#salesRep,"YFSUser")}' />
          				<s:set name='salesRepUserInfo' value='%{#xmlUtil.getChildElement(#salesRepUser,"ContactPersonInfo")}' />
          				<s:if test="#salesRepUserInfo!=null" >
	          				<tr <s:if test="#rowStatus.odd == true">class="odd"</s:if>>
	          					<td>
	          						<s:property value="%{#salesRepUser.getAttribute('Username')}"/>
	          					</td>
	          					<td>
	          						<s:property value="%{#salesRepUserInfo.getAttribute('DayPhone')}"/>
	          					</td>
	          					<td><a href="mailto:<s:property value="%{#salesRepUserInfo.getAttribute('EMailID')}"/>">
	          						<s:property value="%{#salesRepUserInfo.getAttribute('EMailID')}"/></a>
	          					</td>
	          				</tr>
	          			</s:if>
          			</s:iterator> 
          		</table>
                <div id="table-bottom-bar" style="width:60%;">
                    <div id="table-bottom-bar-L"></div>
                    <div id="table-bottom-bar-R"></div>
                </div>
                </s:if>
                <div class="clearview">&nbsp;</div> 
                
                <s:set name="csr1ListEle" value="csr1UserEle" />
				<s:set name="csr2ListEle" value="csr2UserEle" />
				<s:if test='#csr1ListEle != null'>
					<s:set name="csr1Ele" value='#xmlUtil.getChildElement(#csr1ListEle,"CustomerContact")' />
					
					<s:set name="csr1FirstName" value='#xmlUtil.getAttribute(#csr1Ele,"FirstName")' />
					<s:set name="csr1LastName" value='#xmlUtil.getAttribute(#csr1Ele,"LastName")' />
					<s:set name="csr1EMailID1" value='#xmlUtil.getAttribute(#csr1Ele,"EmailID")' />
					<s:set name="csr1Phone" value='#xmlUtil.getAttribute(#csr1Ele,"DayPhone")' />
				</s:if>
				<s:if test='#csr2ListEle != null'>
					<s:set name="csr2Ele" value='#xmlUtil.getChildElement(#csr2ListEle,"CustomerContact")' />
					<s:set name="csr2FirstName" value='#xmlUtil.getAttribute(#csr2Ele,"FirstName")' />
					<s:set name="csr2LastName" value='#xmlUtil.getAttribute(#csr2Ele,"LastName")' />
					<s:set name="csr2EMailID" value='#xmlUtil.getAttribute(#csr2Ele,"EMailID")' />
					<s:set name="csr2Phone" value='#xmlUtil.getAttribute(#csr2Ele,"DayPhone")' />
				</s:if>
				
				<s:if test='#csr1Ele != null || #csr2Ele != null'>
			      <table id="mil-list-new" style="width: 60%;">
			      			<tbody>
			                    <tr class="table-header-bar">
			                       
			                                <td class="no-border table-header-bar-left" width="218"><span class="white txt-small">CSR Name</span></td>
			                                <td class="no-border" width="110" align="left"><span class="white txt-small">Phone</span></td>
			
			                                <td class="no-border-right table-header-bar-right" width="226" align="left"><span class="white txt-small">E-mail</span></td>
			          			</tr>
			          			<s:hidden name="csr1EMailID" value='%{#csr1EMailID1}'/>
			          			<s:if test='#csr1Ele != null'>
			                    <tr>
			                      <td><s:property value='%{#csr1FirstName}'/> <s:property value='%{#csr1LastName}'/></td>
			                      <td valign="top"><s:property value='%{#csr1Phone}'/></td>
			                      <td valign="top"><a href="mailto:<s:property value='%{#csr1EMailID1}'/>"><s:property value='%{#csr1EMailID1}'/></a></td>
			                    </tr>
			                    </s:if>
			                    <s:if test='#csr2Ele != null'>
			                    <tr class="odd">
			                      <td><s:property value='%{#csr2FirstName}'/> <s:property value='%{#csr2LastName}'/></td>
			                      <td valign="top"><s:property value='%{#csr2Phone}'/></td>
			                      <td valign="top"><a href="mailto:<s:property value='%{#csr2EMailID}'/>"><s:property value='%{#csr2EMailID}'/></a></td>
			                    </tr>
			                   </s:if>
			               </tbody>
			     	</table>
			     	 <div id="table-bottom-bar" style="width: 60%;">
							<div id="table-bottom-bar-L"></div>
	                        <div id="table-bottom-bar-R"></div>
	                 </div>
			     </s:if> 
			     </table> 
                
                <div class="clearview">&nbsp;</div>
                
                <table  width="60%" class="form" >
		            <tr>
	              		<td width="209"  class="no-border-left" style="border: none;" >  Email Address: </td>
	              		<td colspan="2" valign="top" style="border: none;"><s:textfield id='txtCustEmailAddress' name='txtCustEmailAddress' size="25" tabindex="" 
							value='%{#extnElem.getAttribute("ExtnCustEmailAddress")}' cssStyle="width:230px;" cssClass="x-input"/></td>
		            </tr>
		            <tr>
	             		<td width="201"  class="no-border-left" style="border: none;">  Invoice Email Address: </td>
	              		<td width="357" valign="top" style="border: none;">
	              			<s:textfield name='EmailIDForInvoice' value="%{#extnElem.getAttribute('ExtnInvoiceEMailID')}" 
	              				cssStyle="width:230px;" cssClass="x-input"/>
	              		</td>
	            	</tr>
            	</table>  --%>
            	</table>
            <%--<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
			      	<td width="100%" valign="top" class="no-border-right padding0"> Email Address(es) to receive invoices:
						<div style="width: 60%; clear: both; margin-top: 10px;" class="table-top-bar">
				          <div class="table-top-bar-L"></div>
				          <div class="table-top-bar-R"></div>
			         	</div>  
				          <table cellspacing="0" cellpadding="0" border="0" align="left" style="background: none repeat scroll 0% 0% transparent; width: 60%;">
				            <tbody>
				              <tr>
				                <td width="55%" valign="top" class="noBorder noBorders-FAFAFA">
				                	<s:select list="invoiceEmailIdList" name="invoiceEmailIdList" id="invoiceEmailIdList" cssStyle="width: 250px;" size="7"></s:select>
				                	<s:hidden name="invoiceEmailIdText" id="invoiceEmailIdText" value='%{#extnElem.getAttribute("ExtnInvoiceEMailID")}' />
				                </td>
				                <td valign="top" class="noBorder">
				                <s:textfield id='NewInvoiceEmailAddr' name='NewInvoiceEmailAddr'  tabindex="" value='' cssClass="input-details-cart  x-input" cssStyle="width: 150px;"/>
				                  <div class="clearview">&nbsp;</div>
				                  <div class="clearview">
				                    <ul class="float-left">
				                      <li class="float-left margin-10">
				                      	<a class="grey-ui-btn" href="javascript:addEmailToInvoiceList()" "><span>Add to List</span></a></li>
				                      </ul>
				                    </div>
				                  <div class="clearview">&nbsp;</div>
				                  <div class="clearview">
				                    <ul class="float-left">
				                      <li class="float-left margin-10">
				                      	<a class="grey-ui-btn" href="javascript:removeEmailFromInvoiceList()"><span>Remove from List</span></a></li>
				                      </ul>
				                    </div>
				                </td>
				                </tr>
				              </tbody>
				            </table>  <div style="width: 60%; clear: both;" id="table-bottom-bar">
				              <div id="table-bottom-bar-L"></div>
				              <div id="table-bottom-bar-R"></div>
				              </div> </td>
				    </tr>
				    <tr>
				      <td valign="top" class="no-border-right padding0">&nbsp;</td>
				    </tr>
				</table> --%>
				<div class="clearview">&nbsp;</div>
            	
            	<div id="cart-actions" class="float-right">    
                    <ul id="cart-actions">
                       
                       	<li><a href="#" onclick="javascript:window.location.reload();" class="grey-ui-btn"><span>Cancel</span></a></li>
                        <li><a class="green-ui-btn" href="javascript:(function(){document.billToInfo.submit();})();"><span>Save</span></a></li>
                    </ul>
                </div>
                <div class="clearview">&nbsp;</div>
                <%--Code Added For XNGTP-3196 --%>
        		<s:if test="%{#_action.isSuccess()}">
					<div class="success" id="successMsgFor_save" style="display : inline; float: right"/>Bill-To Profile has been updated successfully.</div>
				</s:if>
         		<%-- End fix for XNGTP-3196 --%>
        
		        
		        <s:set name="modifiedUser" value="modifiedUser" />
                <s:if test='modifiedUser!=""'>
			     	<div class="clearview textAlignCenter">Last modified by <s:property value="modifiedUser"/> on <s:property value="#_action.getModifiedDate()"/></div>
		        </s:if>
		        <div class="clearview">&nbsp;</div>
                <div class="clearview">&nbsp;</div>
          		<div class="clearview">&nbsp;</div>
          	</div>
          </s:form>
          <div style="display: none;">
			<div title="Showing the Locations" id="showLocationsDlg">
				<div id="showLocationsDiv">
		
				</div>

			</div>
		</div>
   </div>
   <br />
    </div>
        		
		</div>
	</div>
	<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
</body>
</html>