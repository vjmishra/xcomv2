<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/catalog/mini-cart<s:property value='#wcUtil.xpedxBuildKey' />.css" /> 
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/catalog/change-ship-to<s:property value='#wcUtil.xpedxBuildKey' />.css" />
 <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/themes/base/jquery.ui.all<s:property value='#wcUtil.xpedxBuildKey' />.css" />
  <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery-ui.min.<s:property value='#wcUtil.xpedxBuildKey' />js"></script>	
  <!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/request.js"></script>	
  <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/applicationinfo.js"></script>
  <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/userpreferences.js"></script>
  
  <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/scuiplat.js"></script>
  <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/debugger.js"></script> !-->
	<s:set name="isUserAdmin" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@isCurrentUserAdmin(wCContext)" />
	<s:set name="CurrentCustomerId" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@getCurrentCustomerId(wCContext)" />
	<s:set name="canRequestProductSample" value="#session.showSampleRequest" />
<s:url id='getCategoryMenu' action='gategorySubMenu' namespace='/common' >
</s:url>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global-xpedx-functions<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript">
	Ext.Ajax.timeout = 240000;
	var customerChildCountMap = new Array();
	var selectedCustomerMap = new Array();
	var customerPathMap = new Array();
	var customersArray = new Array();
	var customerDivMap = new Array();

	function clearTheArrays(){
		customerChildCountMap = [];
		selectedCustomerMap = [];
		customerPathMap = [];
		customersArray = [];
		customerDivMap = [];
	}
	
	var isUserAdmin = <s:property value="#isUserAdmin"/>;
	
	
</script>	
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/cluetip/jquery.cluetip<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- Web Trends tag start -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/webtrends/displayWebTag<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- Web Trends tag end  -->
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/cluetip/jquery.cluetip<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/mini-cart<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
	


<%--This is to setup reference to the action object so we can make calls to 
    action methods explicitly in JSP's.
    This is to avoid a defect in Struts that's creating contention under load.
    The explicit call style will also help the performance in evaluating Struts'
    OGNL statements. --%>
    

<style>
<!-- Style for ship to address hover-->
	#viewAll a:hover { text-decoration:underline; } 
	
	a:focus {
	       outline: none;
	}
	#panel {
	       height: 200px; width:200px; left:-23px; top:-47px; 
	       display: none; z-index:10000; position:relative; background-image:url(<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/customer-block-hover.png); background-repeat:no-repeat;
	}
	.slide {
	       margin: 0;
	       padding: 0;
	}
	.btn-slide {
	       width: 144px;
	       height: 31px;
	       display: block;
	       text-decoration: none; outline:none;
	}
	.active2 { top:-125px;
	}
	
.share-modal { /*width:399px!important; height:37	0px;*/}         
.indent-tree { margin-left:15px; }       
.indent-tree-act { margin-left:25px; } 
   

	
</style>



<noscript>
<div class="noScript"><s:text name='NoScriptWarning' /></div>

</noscript>

<!-- begin t1-header -->
<!-- <div id="noassignedShipto" style="display:none;color:red;">There are no shipTo locations assigned for your profile, Please contact administrator..</div> commented for jira2881-->

<div class="t1-header commonHeader signOnHeader" id="headerContainer" >



  <!-- add content here for header information -->
	<s:url id ='homeLink' action='home' namespace='/home' /> 
	<div class="logo">
		<s:a href="%{homeLink}">&nbsp;</s:a>
	</div>
  
  
 
  
  
		
		
  
  <ul class="header-subnav commonHeader-subnav">

    
  </ul>
  
  

</div>

<div style="display: none;">
	<div id="viewUsersDlg">
	</div>
</div>

 
<!-- end ship to banner -->