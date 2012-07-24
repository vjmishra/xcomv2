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
<!-- javascript -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

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
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bgiframe.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF --><script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-new-ui.js" language="javascript">
    
</script>


<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery-ui.min.<s:property value='#wcUtil.xpedxBuildKey' />js"></script>

<!-- Facy Box (Lightbox/Modal Window -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.1<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/SpryTabbedPanels<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript"></script>


<script type="text/javascript">
    
    function secretQuestionSubmit(){
        var answerFiled = document.forgotPwdQuestionForm.answer;
        var errorDiv = document.getElementById("errorMsgForAnswer");
        var returnval = false;
        
        errorDiv.innerHTML = "";
        answerFiled.style.borderColor="";
        errorDiv.style.display = "none";
        
        if(answerFiled.value.trim().length == 0)
        {
            //errorDiv.innerHTML = "Answer is required.";
            errorDiv.innerHTML = "<s:text name='MSG.SWC.MISC.FORGOTPASSWORD.ERROR.ANSWERREQ' />";
            answerFiled.style.borderColor="#FF0000";
            errorDiv.style.display = 'inline';
            return returnval;
        }
        else{
        document.forgotPwdQuestionForm.submit();
        returnval = true;
        }
        return returnval;
    }
</script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/home/forgotPassword<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- <title>Forgot Password</title> -->
<title><s:text name="MSG.SWC.MISC.FORGOTPASSWORD.GENERIC.TABTITLE"/></title>

</head>
<!-- END swc:head -->

<s:set name='_action' value='[0]' />

<body class="ext-gecko ext-gecko3" onload="javascript:document.getElementById('answer').focus();">
<div id="main-container">
	<div id="main" class="anon-pages">
    	<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
    	<script type="text/javascript">
			document.getElementById("newSearch").style.display = 'none';
		</script>
    	<div class="container">
      	<!-- breadcrumb -->
       		<s:set name='error' value="#_action.getErrorMessageType()"/>
       		

			
       		<s:set name='wcContext' value="#_action.getWCContext()"/>
      		<div id="mid-col-mil"> 
		    <div>
      		<%-- <div class="padding-top3 page-title black"><strong class="black"> Forgot Password</strong></div> --%>
      		<div class="padding-top3 page-title black"><strong class="black"> <s:text name="MSG.SWC.MISC.FORGOTPASSWORD.GENERIC.PGTITLE"/></strong></div>
			</div>
			<div class=" padding-bottom clearview"> </div>
			
		<!-- begin progress bar -->
			<div id="second-navigation">
	             <ul id="main-nav" class="dropdown">
					<li>
					     <span class="link" >Confirm your identity</span>
					</li>
					<li class="active">
					     <span class="link">Answer your security question</span>
					</li>
					<li>
					    <span class="link" >Password reset confirmation</span>
					 </li>
	             </ul>
			</div>
			<div class="clearall">&nbsp;</div>
		<!--  end progress bar -->
		
			
			<s:form id="forgotPwdQuestionForm" name="forgotPwdQuestionForm" action="answerSubmit" validate="true" namespace="/home" method="POST">
	        <s:hidden name="#action.namespace" value="/home"/>
			<s:hidden id="actionName" name="#action.name" value="answerSubmit"/>
			<s:hidden id="validationField" name="validationField" value="true"/>
			
			<s:url id='homePage' namespace='/home' action='home' />

			<table class="full-width">
				<tbody>
					<tr>
						<td colspan="3" class="underlines no-border-right-user">
							Enter the answer to your security question. <a href="#" class="txt-lnk-sml-1"></a></td>
					</tr>

					<tr>
						<td width="7%" class="no-border-right-user">Question:</td>
						<td colspan="3" class="underlines no-border-right-user"><s:label key="question" value='%{#session.question}'/></td>
					</tr>
					<tr>
						<td width="7%" class="underlines no-border-right-user">Answer:</td>
						<td colspan="2" class="underlines no-border-right-user"><span
							class=" noBorder-left"><s:password id="answer" name="answer" showPassword="true" cssClass="x-input width-250px" maxlength="35"/></span>
						</td>
					</tr>
					<tr>
						<td class="grey  no-border-right-user">&nbsp;</td>
						<td width="38%" class="grey  no-border-right-user">
							<div class="fp-btn-container">
								<ul class="float-left  padding-left1">
									<li class="float-left margin-10"><a href="javascript:cancelForgotPasswordFlow();"
										class="grey-ui-btn"><span>Cancel</span>
									</a>
									</li>
									<!-- <li class="float-right"><a href="javascript:secretQuestionSubmit();"
										class="orange-ui-btn"><span>Submit</span> -->
									 <li class="float-right"><a href="#" onclick="secretQuestionSubmit();" 
                                        class="orange-ui-btn"><span>Submit</span>										
									</a>
									</li>
								</ul>
							</div>
						</td>
						<td width="55%" class="grey  no-border-right-user">&nbsp;</td>
					</tr>
					<tr><td colspan="2">
            			<div class="error"  style="float:right; margin-right: 12px;display:none;margin-right: 110px;" id="errorMsgForAnswer" ></div>
            			 <s:if test='%{#error=="WrongAnswer"}'> 
            				<div class="error"  style="float:right; margin-right: 12px;display:block; margin-right: 110px;" id="errorInvalidAnswer" > 
            					<%-- <s:text name="wrong.answer"/> --%>
            					<s:text name='MSG.SWC.MISC.FORGOTPASSWORD.ERROR.ANSWERINVALID' />
            				</div>
            				 </s:if> 
            		</td></tr>
            		

            		
            		
					<tr>
              			<td colspan="3" class="grey  no-border-right-user"><s:text name="MSG.SWC.MISC.HELPDESK.GENERIC.CONTACT"/></td>
            		</tr>
				</tbody>
			</table>

			</s:form>
			
			<s:form id="cancelforgotPwdForm" name="cancelforgotPwdForm" action="cancelForgotPassword" namespace="/home" method="POST">
        	</s:form>
        	
        	<div class=" bot-margin"> &nbsp; </div>
        	<div class=" bot-margin"> &nbsp;</div>
  
			</div>
      		<br />
    	</div>
	</div>
</div>
<!-- end main  -->
    
<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
	
<!-- end container  -->
</body>
</html>