<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="xpedx" uri="xpedx" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />


<s:set name='_action' value='[0]'/>
<s:set name='xmlUtil' value="#_action.getXMLUtils()" />
<s:set name="displayChildCustomersMap" value="displayChildCustomersMap"></s:set>
<s:set name="msapAndSapCustomersMap" value="msapAndSapCustomersMap"/>
<s:set name="sapAndBillToCustomersMap" value="sapAndBillToCustomersMap"/>
<s:set name="billToAndShipToCustomersMap" value="billToAndShipToCustomersMap"/>
<s:set name="shownCustomerId" value="shownCustomerId"/>
<s:set name="sapParentName" value="%{#_action.getmSapName()}" />
<s:set name="msap" value="%{#_action.getBuyrOrgName()}"/>

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/locationModal-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/js/jquery-ui-1.8.2.custom.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.shorten<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/modals/checkboxtree/jquery.checkboxtree<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript">
//<!--
   /* $(document).ready(function() {
        $('#tree').checkboxTree();
	 $('#collapseAllButtonsTree').checkboxTree({
            collapseAllButton: '',
            expandAllButton: ''
        });
    });*/	
//-->
</script>

<style>


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
#collapseAllButtonsTree
{
	padding: 0px 20px;
	margin: 0px;
}

div.xpedx-light-box
{
	overflow-x: hidden; 	
}
div#fancybox-content 
{
	height: 420px !important;	
}


</style>
  
<div class="share-modal1 xpedx-light-box">
<s:form name="locatoinsForm" id="locatoinsForm" namespace="/profile/org" action="getShipToBillToInfo">
	<s:hidden name='orgCode' value="%{#_action.getWCContext().getStorefrontId()}" />
	<input type="hidden" name="sapCustomerID" value="<s:property value='shownCustomerId' />" />
	    <h1>Change Location</h1>
		
		<div class="radio-container">
		<s:property value="%{#_action.getBuyrOrgName()}"/><br />
			<ul id="">
				<li>
					<input type="button" class="icon-minus" style="vertical-align:middle;" onclick="getChildCustomerList(this,'<s:property value="shownCustomerId"/>','C', 'sapCustomerDiv') " />
					<input type="radio" id="Tree" name="customerId" value="<s:property value='shownCustomerId' />" checked="checked" /> 
					<label>Account: </label><s:property value="sapCustomerDisplay"/>
					<div  id="sapCustomerDiv">
					
						<s:include value="/xpedx/jsp/profile/org/XPEDXShowLocationsList.jsp"/>
					</div>
				
				</li>
			</ul>
		</div>
		<div style="" class="clearview"> 
		
	            <ul class="float-right">
	              <li class="float-left margin-10">
	              		<input type="button" class="btn-neutral" onclick="javascript:$.fancybox.close();" value="Cancel"/>
					</li>
	              <li class="float-right">
	              		<input  type="button" class="btn-gradient" onclick="javascript:submitForm()" value="Apply"/>
					</li>
	            </ul>
	         
         </div>
</s:form>
</div>		
	
