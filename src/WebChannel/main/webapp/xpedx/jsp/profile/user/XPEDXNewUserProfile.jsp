<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:set name='_action' value='[0]' />
<s:set name='xmlUtil' value="#_action.getXMLUtils()" />
<s:set name='customerId' value='%{#_action.getCustomerID()}' />
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean'
	id='util' />
<s:set name='ctxt' value="getWCContext()" />
<s:set name='userListResId' value='"/swc/profile/ManageUserList"' />
<s:set name='manageOrgResId'
	value='"/swc/profile/ManageOrganizationProfile"' />
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />
<% request.setAttribute("isMergedCSSJS","true"); %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
	<%@ taglib prefix="s" uri="/struts-tags"%>
<head>
<meta content='IE=8' http-equiv='X-UA-Compatible' />
	<!-- begin styles. -->
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/ADMIN<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<!-- end styles -->
	
	
	<!-- javascript -->
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/ajaxValidation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/swc<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/jquery-1.4.2<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.core<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.tabs<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui<s:property value='#wcUtil.xpedxBuildKey' />.js" language="javascript"></script>
	
	
	<!-- carousel scripts js   -->
	
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	
	<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
	<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
	<!--[if IE]><script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/other_libs/excanvas_r3/excanvas.js" type="text/javascript" charset="utf-8"></script><![endif]-->
	<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
	<!-- /STUFF -->
	
	<!-- Facy Box (Lightbox/Modal Window -->
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/modals/checkboxtree/jquery.checkboxtree<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/sorttable<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
    <script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/sorting<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript"></script>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.0/jquery-ui.min.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery-ui.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	
	<script type="text/javascript">
	//Added for JIRA 3926
	function changeCase(){
	document.getElementById('userName').value=document.getElementById('userName').value.toLowerCase();
}
	
			function addNewUser(docDivId, ignoreDivIds)
			{
				try{
					if (mandatoryFieldValidation(docDivId, ignoreDivIds) != "")
						return false;
				}catch(err){
				}
				
				var email=document.getElementById("emailId").value;
			    len=email.length;
			    var no=0;
			    for(var i=0;i<len;i++)
			    {
			        if(email.charAt(i)=="@" || email.charAt(i)==",")
			        {
			            //alert(i);
			            no=no+1;
			            //alert(no);
			        }
			    }
			    var dat=Date();
			    //alert(dat);
			    if (email.indexOf("@") < 0)
			    {
			        alert("Please enter valid Email address!");
			        document.getElementById("emailId").focus();
			        return false;
			    }
			    // Email Checker
			    else if (email.indexOf(".") < 0)
			    {
			        alert("Incorrect email address. Please re-enter!");
			        document.getElementById("emailId").focus();
			        return false;
			    }
			    // Email Checker
			    else if (email.indexOf(" ") >= 0)
			    {
			        alert("Incorrect email address. Please re-enter!");
			        document.getElementById("emailId").focus();
			        return false;
			    }
			    else if (len > 50)
			    {
			        //length1=email.length;
			        //alert(length1);
			        alert("Email should not exceed more than 50 characters!");
			        document.getElementById("emailId").focus();
			        return false;
			    }
			    else if (no >= 2)
			    {
			        //length1=email.length;
			        //alert(length1);
			        alert("Please do not enter more than one email address!");
			        document.getElementById("emailId").focus();
			        return false;
			    }            	

			    var confirmemail=document.getElementById("confirmEmailId").value;
			    if(email!=confirmemail){
			    	document.getElementById("errorMsgFor_emailId").style.display = "inline";
			        return false;
			    }
			    document.addNewUserForm.userName.value=document.addNewUserForm.userName.value.toLowerCase();//Added for JIRA 3926
			    document.getElementById("addNewUserForm").submit();
			    return true;
			}

			function testFieldValueCheck(component, docDivId){
				var mandatoryFieldCheckFlag = document.getElementById("mandatoryFieldCheckFlag_"+docDivId).value;
				if(mandatoryFieldCheckFlag=="true"){
					if(component.value==""){
		            	component.style.borderColor="#FF0000";
		        	}
		        	else{
		            	component.style.borderColor="";
		    	    }
		    	}
		    }

			function confirmFieldValidation(component, compareWith){
				if(document.getElementById(compareWith).value!=""){
					if(component.value!=document.getElementById(compareWith).value){
		            	component.style.borderColor="#FF0000";
		            	document.getElementById("errorMsgFor_"+compareWith).style.display = "inline";
		        	}
		        	else{
		            	component.style.borderColor="";
		            	document.getElementById("errorMsgFor_"+compareWith).style.display = "none";
		    	    }
		    	}
		    }
		    
	</script>
			
<script type="text/javascript" 
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.numeric<s:property value='#wcUtil.xpedxBuildKey' />.js"> </script>
<script type="text/javascript" 
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.maskedinput-1.3<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript">
			$(document).ready(function() {
			$('.phone-numeric').numeric(false); 
			$("#dayPhone_new").mask("999 999-9999");
			$("#dayFaxNo_new").mask("999 999-9999");
	});		
</script>
	
	<!-- Facy Box (expand and collpse Modal Window -->
	<title><s:property value="wCContext.storefrontId" /> / Add New User Profile</title>
	</head>
	
	<body class="ext-gecko ext-gecko3">
		
			
			<div id="main-container">
				<div id="main">
			    	<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
			    	<div class="container">
			    	<s:form id="addNewUserForm" name="addNewUserForm" method="post" validate="true" action="saveNewUserInfo" namespace = "/profile/user">
						<s:hidden name='operation' value='%{#_action.getOperation()}' />
						<s:hidden name='customerId' value='%{#customerId}' />
						<s:hidden name="#action.namespace" value="/profile/user" />
						<s:hidden name="#action.name" value="saveNewUserInfo" />
						<s:hidden name='buyerOrgCode' value='%{#_action.getBuyerOrgCode()}' />
						<s:hidden name="preferredLocale" id="saveNewUserInfo_preferredLocale" value="%{'en_US_EST'}"/>
			
			      	<s:hidden id="mandatoryFieldCheckFlag_addNewUserForm" name="mandatoryFieldCheckFlag_addNewUserForm" value="%{false}"/>
			       <div id="mid-col-mil">
			      	<br/>
			      	<h2 class="table-hdr">Add User</h2>
			        
			        <div id="requestform">
			        	<table class="form" width="5%" border="0" cellspacing="0" cellpadding="0">
			  				<tr>
			  					<td class="no-border-right-user" colspan="2"><div class="mandatory float-left">*</div>Username:</td>
			                    <td width="31%" class="no-border-right-user">
			                    	<s:textfield tabindex="50" name="userName" id="userName" onkeyup="javascript:testFieldValueCheck(this, 'addNewUserForm');changeCase();" 
			                    	onmouseover="javascript:testFieldValueCheck(this, 'addNewUserForm');" maxlength="40" cssClass="x-input" cssStyle="width: 245px;"/> <!-- Jira 3971 -->                  
			                   </td>
			  				</tr>
						</table>
			        </div>
			
			        <div class="clearview">&nbsp;</div>
			   		
			

						<div class="TabbedPanelsContentGroup" id="UserInformationsData">
			            	<div class="TabbedPanelsContent" id="UserInformation">
			              		<div id="requestform">              
			              			
							<table id="userDataTable"  width="100%" border="0" cellspacing="0" cellpadding="0" class="tabs">
			              	  			<tr>
			                    			<td width="13%" valign="top" class="no-border-right-user">User Status:</td>
			                    			<td colspan="3" class="no-border-right-user"> 
			                    				<label><input type="radio" name="status" id="status" value="10" checked/>Active</label>
                                                <label><input type="radio" name="status" id="status" value="30" />Suspended</label></td>
			                  			</tr>                  
			                  			<tr>
			                    			<td valign="top" class="no-border-right-user">
			                    				<div  class="question">
				                      				<ul>
				                        				<li> User Type:</li>
				                        				<li> <a href="#" id="purposeofmails-u1"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png" width="12" height="12" alt="One or more roles may be assigned to each user. Hover over each role to see more details." title="One or more roles may be assigned to each user. Hover over each role to see more details." border="0" /></a></li>
				                      				</ul>
			                    				</div>
			                    			</td>
			                    			<td colspan="3" class="no-border-right-user">
							                	<label id="purposeofmails-u3" title="Responsible for overall administration of, and access to, accounts on the web site. Creates user profiles, assigns roles, assigns locations.">
							                    	<s:checkbox tabindex="80" name='buyerAdmin' id='buyerAdmin'/>
							                      	Admin
							                    </label> 
							                    <label id="purposeofmails-u4" title="Authorizes submission of orders.">
							                    	<s:checkbox tabindex="85" name='buyerApprover' id='buyerApprover'/>
							                    	Approver
							                    </label> 
							                    <label id="purposeofmails-u5" title="Buyer has the ability to submit orders.">
							                    	<s:checkbox tabindex="75" name='buyerUser' id='buyerUser' fieldValue="test123" value="true" disabled="true"/>
							                      	Buyer
							                    </label> 
							                    <label id="purposeofmails-u6" title="Estimator views available inventory and pricing.">
							                    	<s:checkbox tabindex="75" name='estimator' id='estimator'/>
							                      	Estimator
							                    </label>
							                    <label id="purposeofmails-u7" title="Permitted to view invoices online.">
							                    	<s:checkbox name="viewInvoices" id="viewInvoices"/>
							                      	<p class="float-right">View Invoices</p>
							                    </label> 
							                    <label id="purposeofmails-u8" title="Permitted to view prices.">
							                    	<s:checkbox name="viewPrices" id="viewPrices"/>
							                      	View Prices
							                    </label> 
							                    <label id="purposeofmails-u9" title="Permitted to view reports.">
							                    	<s:checkbox name="viewReports" id="viewReports"/>
							                      	<p class="float-right">View Reports</p>
							                    </label>
											</td>
			                  			</tr>
			                  			<tr>
			                    			<td class="no-border-right-user"><div class="mandatory float-left">*</div> First Name:</td>
			                    			<td width="31%" class="no-border-right-user">
			                    				<s:textfield tabindex="50" name="firstName" id="firstName" onkeyup="javascript:testFieldValueCheck(this, 'addNewUserForm');" 
			                    				onmouseover="javascript:testFieldValueCheck(this, 'addNewUserForm');" maxlength="20" cssClass="x-input" cssStyle="width: 185px;"/>                    
			                    			</td>
			                    			<td width="19%" class="no-border-right-user"><div class="mandatory float-left">*</div> Last Name:</td>
			                    			<td width="37%" class="no-border-right-user">
			                    				<s:textfield tabindex="55" name="lastName" id="lastName" onkeyup="javascript:testFieldValueCheck(this, 'addNewUserForm');" 
			                    				onmouseover="javascript:testFieldValueCheck(this, 'addNewUserForm');" cssClass="x-input" cssStyle="width: 185px;" maxlength="20"/>                   
			                    			</td>
			                  			</tr> 
			                  		    <tr>
			                    			<td  valign="top" class="no-border-right-user">
				                    			<div class="question">
				                      				<ul>
							                        	<li><div class="mandatory float-left">*</div> Email Address:</li>
					            			            <li> <a href="#" id="purposeofmails-u2"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png" width="12" height="12" border="0"  alt="Used for all user specific communications from this website, including new password and password resets." title="Used for all user specific communications from this website, including new password and password resets."/></a></li>
				    	                  			</ul>
				                    			</div>
				                    		</td>
			                    			<td class="no-border-right-user">
			                    				<s:textfield tabindex="65" name="emailId" cssClass="x-input" id="emailId" onkeyup="javascript:testFieldValueCheck(this, 'addNewUserForm');" 
			                    				onmouseover="javascript:testFieldValueCheck(this, 'addNewUserForm');" cssStyle="width: 185px;"/>
			                    			</td>
			                    			<td class="no-border-right-user"><div class="mandatory float-left">*</div> Confirm Email Address:</td>
			                    			<td class="no-border-right-user">
			                    			<!-- javascript:confirmFieldValidation(this, 'emailId'); -->
			                    				<s:textfield tabindex="65" id="confirmEmailId" name="confirmEmailId" onkeyup="" 
			                    				cssClass="x-input" cssStyle="width: 185px;"/>
			                    			</td>
			                  			</tr>
			                  			<tr>
			                    			<td class="no-border-right-user">Address Line 1:</td>
			                    			<td class="no-border-right-user" colspan="3">
			                    				<s:textfield name='addressLine1_new' id='addressLine1_new' cssClass="x-input" cssStyle="width:283px;" maxlength="35"/>
			                    			</td>
			                  			</tr>
						                <tr >
						                	<td class="no-border-right-user">Address Line 2:</td>
						                    <td class="no-border-right-user" colspan="3">
						                    	<s:textfield name='addressLine2_new' id='addressLine2_new' cssClass="x-input" cssStyle="width:283px;" maxlength="35"/>
						                    </td>
						                </tr>
						                <tr >
						                    <td class="no-border-right-user">Address Line 3:</td>
						                    <td colspan="3" class="no-border-right-user">
						                    	<s:textfield name='addressLine3_new' id='addressLine3_new' cssClass="x-input" cssStyle="width:283px;" maxlength="35"/>
						                    </td>
						                </tr>
						                <tr >
						                    <td class="no-border-right-user">City:</td>
						                    <td class="no-border-right-user"><s:textfield name='city_new' id='city_new'	cssClass="x-input" cssStyle="width:190px;" maxlength="20"/></td>
						                    <td class="no-border-right-user">State / Province:</td>
						                    <td class="no-border-right-user"><s:textfield name="state_new" id="state_new" cssClass="x-input" cssStyle="width: 190px;" maxlength="2"/></td>
						                </tr>
						                <tr >
						                    <td class="no-border-right-user">Postal Code:</td>
						                    <td class="no-border-right-user"><s:textfield name='zipCode_new' id='zipCode_new' cssClass="x-input" cssStyle="width: 190px;" maxlength="10"/></td>
						                    <td class="no-border-right-user">Country:</td>
						                    <td class="no-border-right-user">
						                    	<s:select
												tabindex="20" name="country_new" id="country_new"
												emptyOption="true"
												list='#wcUtil.getCountryCodeList()' cssClass="x-input" cssStyle="width: 198px;"/></td>
										</tr>
						                <tr >
						                    <td class="no-border-right-user">Phone:</td>
						                    <td class="no-border-right-user"><s:textfield name='dayPhone_new' id='dayPhone_new' cssClass="x-input phone-numeric" cssStyle="width: 190px;" maxlength="10"/></td>
						                    <td class="no-border-right-user">Fax:</td>
						                    <td class="no-border-right-user"><s:textfield name='dayFaxNo_new' id='dayFaxNo_new' cssClass="x-input phone-numeric" cssStyle="width: 190px;" maxlength="10"/></td>
						                </tr>
			                		</table>              
			             		</div>
			            	</div> 
						</s:form>
					</div>
			        
			        <div class="clearview">&nbsp;</div>
			          <div>
			            <ul  class="float-right">
			              <li class="float-left margin-10"><a href="javascript:window.history.go(-1)"  class="grey-ui-btn"><span>Cancel</span></a></li>
			              <li class="float-right"><a class="green-ui-btn" onclick="javascript: return addNewUser('addNewUserForm', []);" href="#" ><span>Save</span></a></li>
			            </ul>
			      </div>
			      <div class="clearview">&nbsp;</div>
			      <%--Code Added For XNGTP-3196 --%>
			         <div class="error" id="errorMsgForMandatoryFields_addNewUserForm" style="display : none; float: right"/></div>
			         <div class="error" id="errorMsgFor_emailId" style="display : none; float: right"/>Please enter the same email address in both Email Address and Confirm Email Address fields.</div>
			      <%-- End fix for XNGTP-3196 --%> 
			     
			          
			        </div>
			      </div>
			    </div>
			    <script type="text/javascript">
					<!--
					var TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1");
					//-->
				</script>
			</div>
			<!-- end main  -->
    		
    		<!-- Footer Start --> 
    			<s:action name="xpedxFooter" executeResult="true" namespace="/common" /> 
    		<!-- Footer End -->
			
			<div style="display: none;">
				<script type="text/javascript">
					defaultSortColumn=1;
				</script>
			</div>
		
	</body>
</html>