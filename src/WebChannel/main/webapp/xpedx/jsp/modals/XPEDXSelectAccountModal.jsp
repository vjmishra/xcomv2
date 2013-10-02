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
	<h2 style="margin-bottom:10px; color:#000;"><span>Select Account</span></h2>
	
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
             		<li style="float:left;" class="margin-10"><a class="grey-ui-btn" href="javascript:$.fancybox.close();" ><span>Cancel</span></a></li>
             		<li style="float:right;"><a class="green-ui-btn" href="javascript:viewAccountDetails();" ><span>Select</span></a></li>
               	</ul>
             	</td>
         </tr> 
         
         
         </table>
       </s:form>
     </div>
     </div>
</div>
