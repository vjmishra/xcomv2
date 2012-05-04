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

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/js/jquery-ui-1.8.2.custom.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.shorten.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/modals/checkboxtree/jquery.checkboxtree.js"></script>

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
.share-modal1 h2{ margin-bottom:5px; color:#000;}
.share-modal1 {width:650px; height:auto;} 
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
	min-height: 200px;
	max-height: 300px;
	width:650px;
	overflow: auto;
	border: 1px solid #ccc;
	margin: 0px 0px 10px 0px;
	white-space: nowrap;
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
  
<div class="share-modal1 xpedx-light-box" style="border-left:solid 0px; border-right:solid 0px; height:auto;">
<s:form name="locatoinsForm" id="locatoinsForm" namespace="/profile/org" action="getShipToBillToInfo">
	<s:hidden name='orgCode' value="%{#_action.getWCContext().getStorefrontId()}" />
	    <h2 style="color:#000;">Change Location</h2>
		<br /> 
		<div class="radio-container">
		<s:iterator id="msapAndSapMap" value="msapAndSapCustomersMap">
			<s:set name="key" value='key' />
			<s:set name='sapCustomers' value='value' />
			<s:property value="%{#_action.getBuyrOrgName()}"/><br />
			
			<s:iterator id="sapCustomer" value="#sapCustomers" status="sapCustomerStatus">
				<s:set name="sapCustomerId" value="#sapCustomer" />
				<s:set name="sapCustomerDisplay" value="#displayChildCustomersMap.get(#sapCustomer)" />
				<ul id="collapseAllButtonsTree">
					<li>
					<s:if test="%{#shownCustomerId == #sapCustomerId}">
						<input type="radio" id="Tree" name="customerId" value="<s:property value='#sapCustomerId' />" checked="checked" />
					</s:if>
					<s:else>
						<input type="radio" id="Tree" name="customerId" value="<s:property value='#sapCustomerId' />" />
					</s:else>
					<s:property value="%{#displayChildCustomersMap.get(<s:property value='#sapCustomerId' />)}"/>
					<label>Account: </label><s:property value="#sapCustomerDisplay"/>
				<s:set name="billToCustomers" value="sapAndBillToCustomersMap.get(#sapCustomerId)" />
				<s:iterator id="billToCusotmer" value="#billToCustomers">
					<s:set name="billToCusotmerId" value="#billToCusotmer" />
					<s:set name="billToCusotmerDisplay" value="displayChildCustomersMap.get(#billToCusotmerId)" />
					<ul class="indent-tree">
						<li>
						<s:if test="%{#shownCustomerId == #billToCusotmerId}">
							<input type="radio" name="customerId" value="<s:property value='#billToCusotmerId' />" checked="checked" />
						</s:if>
						<s:else>
							<input type="radio" name="customerId" value="<s:property value='#billToCusotmerId' />" />
						</s:else>
						<s:property value="#billToCusotmerDisplay"/>
						<s:set name="shipToCustomers" value="%{#billToAndShipToCustomersMap.get(#billToCusotmerId)}" />
						<s:iterator value="#shipToCustomers" id="shipToCustomer" status="shipToIndex">
							<s:set name="shipToCustomerId" value="#shipToCustomer" />
							<s:set name="shipToCusotmerDisplay" value="displayChildCustomersMap.get(#shipToCustomerId)" />
 							<ul class="indent-tree-act">	
 								<s:if test="#shipToIndex.first">
									<li>
										<s:if test="%{#shownCustomerId == #shipToCustomerId}">
											<input type="radio" id="Tree" name="customerId" value="<s:property value='#shipToCustomerId' />" checked="checked" />
										</s:if>
										<s:else>
											<input type="radio" id="Tree" name="customerId" value="<s:property value='#shipToCustomerId' />" />
										</s:else>
										<s:property value="#shipToCusotmerDisplay"/>
									</li>
							</s:if>
							<s:elseif test="#shipToIndex.last">
									<li>
										<s:if test="%{#shownCustomerId == #shipToCustomerId}">
											<input type="radio" id="Tree" name="customerId" value="<s:property value='#shipToCustomerId' />" checked="checked" />
										</s:if>
										<s:else>
											<input type="radio" id="Tree" name="customerId" value="<s:property value='#shipToCustomerId' />" />
										</s:else>
										<s:property value="#shipToCusotmerDisplay"/>
									</li>
							</s:elseif>
							<s:else>
									<li>
										<s:if test="%{#shownCustomerId == #shipToCustomerId}">
											<input type="radio" name="customerId" value="<s:property value='#shipToCustomerId' />" checked="checked" />
										</s:if>
										<s:else>
											<input type="radio" name="customerId" value="<s:property value='#shipToCustomerId' />" />
										</s:else>
										<s:property value="#shipToCusotmerDisplay"/>
									</li>	
							</s:else>
							</ul>						
						</s:iterator>
						</li>
					</ul>
				</s:iterator>
				</li>
				</ul>
			</s:iterator>
		</s:iterator>
		</div>
		
		<div style="" class="clearview"> 
			<div>
	            <ul class="float-right">
	              <li class="float-left margin-10">
	              	<s:a cssClass="grey-ui-btn "	href="javascript:$.fancybox.close();">
						<span>Cancel</span>
						</s:a>
					</li>
	              <li class="float-right">
	              	<s:a href="javascript:submitForm()" cssClass="green-ui-btn">
							<span>Apply</span>
						</s:a>
					</li>
	            </ul>
	          </div>
         </div>
</s:form>
</div>		
	
