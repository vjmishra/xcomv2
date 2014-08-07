<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<script type="text/javascript">
function viewAccountDetails()
{	//Added For Jira 2903
	//Commented for 3475
	//Ext.Msg.wait("Processing..."); 
	document.getElementById("selectAccountForm").submit();
}
</script>

<style type="text/css">
.noborder{ border:solid none;}
.noborder td {border:solid 0px; padding:0px; width:370px;}
.width-370px{ width:370px;}
.width-400{ width:400px;}
.margin-10{ margin:0px 10px;}
</style>

<div style="display: none;" >
	
	<div id="dlgSelectAccountBox" class="xpedx-light-box select-account" >
	<h1>Select Account</h1>
	
	<div class="form-service-light"> 
	<s:form id="selectAccountForm" name="selectAccountForm" namespace='/profile/org' action='MyGetCustomerInfo'>
	
		<s:hidden name="organizationCode" id="organizationCode" value='%{wCContext.storefrontId}' />
		
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="noborder">
        
        <tr>
            <td>
                <s:div id="dlgSelectCustomerAccounts">
							<!-- CONTENT FOR DISPLAYING RADIO BUTTONS WILL GO HERE -->
				</s:div>
            </td> 
         </tr>
        <br />
         <tr>
           		<td>                          
           		<ul id="tool-bar" class="tool-bar-bottom float-right">
             		<li style="float:left;" class="margin-10"><input class="btn-neutral" type="button" onclick="javascript:$.fancybox.close();" value="Cancel"/></li>
             		<li style="float:right;"><input class="btn-gradient" type="button" onclick="javascript:viewAccountDetails();" value="Select"/></li>
               	</ul>
             	</td>
         </tr> 
         
         
         </table>
       </s:form>
     </div>
     </div>
     
</div>
