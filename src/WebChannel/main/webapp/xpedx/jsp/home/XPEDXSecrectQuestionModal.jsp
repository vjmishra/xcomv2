<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="swc" %>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>
<% request.setAttribute("isMergedCSSJS","true"); %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />


<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>

<!-- begin styles. -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/MISC<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.1<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
</head>
<!-- END swc:head -->

<s:set name='orgQuestionList' value='%{getQuestionListForOrg()}' />

<body class="ext-gecko ext-gecko3" onload="javascript:document.getElementById('answer').focus();">


    	
    	<div >
      
			
			<s:form id="secrectQuestionForm" name="secrectQuestionForm"  namespace='/common' action='xpedxSubmitSecurityQuestion'  validate="true" method="POST">
			
			<table class="full-width">
			<tbody style="width: 721px">
			<tr><td align ="left"><div class="xpedx-light-box" style="width: 718px"><h2>Security Question and Answer</h2></div></td></tr>
			<tr><td class="no-border-right-user" ></td></tr>
			</tbody>
			</table>
			<table width="75%">
				<tbody>
					<tr>
						<td colspan="4" class="underlines no-border-right-user" align="center">
						 <b>Please set up your security question and answer. We will ask you this question if you should </br>
						forget your password in future, in order to verify that you are the correct user. Keep in mind
							</br>
						 that the answers to the security questions are case-sensitive.</b></br></br>
							</td>
					</tr>
					<tr>
					<td class="no-border-right-user">&nbsp;
					</td>
					<td  class="no-border-right-user"></td>
			<td colspan="1" class="text-right">
					</td>
					<td class="no-border-right-user"></td>
				</tr>
					<s:if test="#orgQuestionList!=null">
				<tr> 
			<td class="no-border-right-user">
			Security Question:</td>
			<td colspan="3" class="no-border-right-user"><s:select headerKey="" headerValue="- Security Question -"
				tabindex="20" name="secretQuestion" id="secretQuestion"
				list="#orgQuestionList" cssClass="x-input" cssStyle="width: 99.8%;"
				onchange="javascript:document.secrectQuestionForm.secretAnswer.value='';javascript:document.secrectQuestionForm.confirmAnswer.value='';" />
			</td>
		</tr>
		</s:if>
		<tr>
			<td class="no-border-right-user">&nbsp;
			</td>
			<td  class="no-border-right-user"></td>
			<td colspan="1" align="right" class="text-right">
			</td>
			<td class="no-border-right-user"></td>
		</tr>
		<tr>
			<td class="no-border-right-user">
			Security Answer:</td>
			<td  class="no-border-right-user"><s:password tabindex="25"
				name='secretAnswer' id="secretAnswer" 
			    cssClass="x-input"
				cssStyle="width: 99%;" 
				showPassword="true" /></td>
		</tr>
		<tr>
			<td class="no-border-right-user">&nbsp;
			</td>
			<td  class="no-border-right-user"></td>
			<td colspan="1" align="right" class="text-right">
			</td>
			<td class="no-border-right-user"></td>
		</tr>
		<tr>
			<td class="no-border-right-user">
			Confirm Answer:</td>
			<td class="no-border-right-user"><s:password tabindex="30"
				name='confirmAnswer' id="confirmAnswer" 
			    cssClass="x-input"
				cssStyle="width: 99%;" 
				showPassword="true" /></td>
		</tr>
		<tr>
			<td class="no-border-right-user">&nbsp;
			</td>
			<td  class="no-border-right-user"></td>
			<td colspan="1" class="text-right">
			</td>
			<td class="no-border-right-user"></td>
		</tr>		
		<tr>
			<td></td>
			 <td  class=" no-border-right-user" align="right" >
	
			</td>
			<td></td>
			<td></td>
			</tr>
				</tbody>
			</table>
			
			<table width="82%">
			<tbody>
			<tr>
			<td class ="no-border-right-user" colspan="1" width="79%"></td>
			<td class ="no-border-right-user"  colspan="2" width="5%">
			<div>
			<ul class="float-right">
			<li >
			<a href="javascript:saveAnswer();"  class="orange-ui-btn"><span>Submit</span>					
			</a>
			</li>
			</ul>
			</div></td>
			<td></td>
			</tr>
			
			<tr>
				<td class="grey  no-border-right-user" colspan="3" >
				</br>
					<div class="error"  style="float:right; margin-right: 1px;display:none;" id="errorMsgForAnswer" ></div>
						</td>
						<td class="grey  no-border-right-user" colspan="1">
						</td>
					</tr>
			</tr>
			</tbody>
			</table>

			</s:form>
			
		
			</div> 
      		<br />
    	

<!-- end main  -->
    
<%--s:action name="xpedxFooter" executeResult="true" namespace="/common" /--%>
	
<!-- end container  -->
</body>
</html>