<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld" %>
<%@page import="java.util.Date"%>
<% request.setAttribute("isMergedCSSJS","true"); %>


<s:set name='_action' value='[0]' />
<s:set name="xutil" value="XMLUtils" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<!--Web trends tag start  -->
<meta name="DCSext.w_x_news_add" content="1" />
<meta name="WT.ti" content="xpedx/News Maintenance">
<!--Web trends tag end  -->

<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='dateUtilBean'/>
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils' id='util' />

<%-- <title><s:property value="wCContext.storefrontId" /> - News Maintenance</title> --%>
<title><s:property value="wCContext.storefrontId" /> - <s:text name='MSG.SWC.NEWSARTL.CREATE.GENERIC.TABTITLE' /> </title>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="webapp-context" content="/swc" />

<!-- begin styles. -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#util.staticFileLocation' />/xpedx/css/global/GLOBAL.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#util.staticFileLocation' />/xpedx/css/theme/ADMIN.css" />
<link rel="stylesheet" type="text/css"	href="<s:property value='#util.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" /> 
<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#util.staticFileLocation' />/xpedx/css/global/IE.css" />
<![endif]-->
<!-- end styles -->

<!-- jQuery Base & jQuery UI -->
<script type="text/javascript" src="<s:property value='#util.staticFileLocation' />/xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="<s:property value='#util.staticFileLocation' />/xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="<s:property value='#util.staticFileLocation' />/xpedx/js/swc.js"></script>
<script type="text/javascript" src="<s:property value='#util.staticFileLocation' />/xpedx/js/order/draftOrderList.js"></script>
<script type="text/javascript" src="<s:property value='#util.staticFileLocation' />/xpedx/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<s:property value='#util.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript" src="<s:property value='#util.staticFileLocation' />/xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="<s:property value='#util.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="<s:property value='#util.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget.js"></script>

<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="<s:property value='#util.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified.js" type="text/javascript" charset="utf-8"></script>
<!--[if IE]><script src="../other_libs/excanvas_r3/excanvas.js" type="text/javascript" charset="utf-8"></script><![endif]-->
<script src="<s:property value='#util.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF -->
<!-- Lightbox/Modal Window -->
<script type="text/javascript" src="<s:property value='#util.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>


<script type="text/javascript" src="<s:property value='#util.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<s:property value='#util.staticFileLocation' />/xpedx/js/jquery.ui.datepicker.js"></script>
<script type="text/javascript" src="<s:property value='#util.staticFileLocation' />/xpedx/js/ckeditor/ckeditor.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$(document).pngFix();
	});
	$(function() {
		$(".datepicker").keydown(function(){ return false; }); 
		$(".datepicker").datepicker({
			showOn: 'focus',
			numberOfMonths: 1,
			buttonImage: '<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/calendar-icon.png',
			buttonImageOnly: true
		});
	});
</script>

</head>
<%-- <swc:extDateFieldComponentSetup/> --%>
<script type="text/javascript">
    function swc_validateForm_addArticle() {
    var errors = false;
        
        form = document.getElementById("addArticle");

        
        // validator name: swcajax
        var isAjaxValidated = swc_validateForm("addArticle", false); //calling global js-function for ajax-validation defined in ajaxValidation.js
        if(!isAjaxValidated)
           errors = true;
    if(errors){
    	try{
            svg_classhandlers_decoratePage();
        }catch (err){
            alert("There is an error in Svg-apply: "+err.message);
        }
    }
	    return !errors;
    }
    
    function ismaxlength(obj){
    	var mlength=obj.getAttribute? parseInt(obj.getAttribute("maxlength")) : ""
    	if (obj.getAttribute && obj.value.length>mlength)
    	obj.value=obj.value.substring(0,mlength)
    	}

	//added for jira 2647 and 2690 
	
	function isArticleFormValidation(){
		var effectiveDate=document.getElementById("submittedTSFrom").value;
		var expirationDate=document.getElementById("submittedTSTo").value;
		var currentArtList=document.getElementById("currentArtList").value.split(",");
		var articleName=document.getElementById("articleName").value;
		// Added For Jira 3315
			var currentDate = new Date()
	 		var day = currentDate.getDate()
	  		var month = currentDate.getMonth() + 1
	  		var year = currentDate.getFullYear()
	  		var currentday = month+"/" + day + "/"+year;
	  	//End Fix For 3315
		if(currentArtList.length>0) {
			for(var i=0; i<currentArtList.length; i++) {
				if(currentArtList[i].trim() == articleName) {
					document.getElementById("articleName").style.borderColor="#FF0000";
					document.getElementById("errorMsgFor_articleTitle").style.display = "inline";
					return false;
				}
			}
		}

		if(articleName=="" || articleName==null){

			document.getElementById("articleName").style.borderColor="#FF0000";
			alert("Article Title cannot be blank. Please enter the article name.");
			document.getElementById("articleName").focus();
			return false; 
			}
		
		if((effectiveDate=="" || effectiveDate==null) && (expirationDate=="" || expirationDate==null))
		{
			 document.getElementById("submittedTSFrom").style.borderColor="#FF0000";
			 document.getElementById("submittedTSTo").style.borderColor="#FF0000";
			 alert("Please enter effective date and expiration date for the article");
			 document.getElementById("submittedTSFrom").focus();				 
			 return false;
				 
			}
		else if(effectiveDate=="" || effectiveDate==null){
			document.getElementById("submittedTSFrom").style.borderColor="#FF0000";
			alert("Please enter effective date for the article");
			document.getElementById("submittedTSFrom").focus();
		        return false;    
			}
		else if(expirationDate=="" || expirationDate==null){
			document.getElementById("submittedTSTo").style.borderColor="#FF0000";
			alert("Please enter expiration date for the article");
		 	document.getElementById("submittedTSTo").focus();
	        	return false;    
			}
		// Start Fix For Jira 3315
		var effectiveDateDay = new Date(document.getElementById("submittedTSFrom").value); 
		var expirationDateDay = new Date(document.getElementById("submittedTSTo").value);
		var currentDateDay=new Date(currentday);
		//Expiration date > or = Current Date Jira 3315
		if(expirationDateDay < currentDateDay){
		document.getElementById("submittedTSTo").style.borderColor="#FF0000";
		document.getElementById("submittedTSFrom").style.borderColor="";        
		alert("Article expiration date should be greater than or equal to current date. Please re-enter the expiration date");
	 	document.getElementById("submittedTSTo").focus();
		return false;
		}
		//Effective Date < or = Expiration Date Jira 3315
		if(effectiveDateDay > expirationDateDay){
			document.getElementById("submittedTSFrom").style.borderColor="#FF0000";
			document.getElementById("submittedTSTo").style.borderColor="";
			alert("Article effective date should be less than or equal to expiration date. Please re-enter the effective date");
		 	document.getElementById("submittedTSFrom").focus();
			return false;
		}
		
		// End Fix For Jira 3315
		else{
			document.newArticleForm.submit();
			}
	}
//end of jira 2647	

</script>

<style>
.ui-datepicker-trigger {
margin-left:116px;
margin-top:2px; } 

#order-filter table {
    width: 100%;
}
/* required for text area to show properly */
</style>

<body class="ext-gecko ext-gecko3">
		<div id="main-container">
			<div id="main">
				
				<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
				<!-- // header end -->			
			<div class="container">
			      <!-- breadcrumb -->
			     <div id="mid-col-mil"><br />
			      
			      <s:url id='toolsLink' namespace='/xpedx/tools' action='XPEDXTools'>
						<s:param name="xpedxSelectedHeaderTab">ToolsTab</s:param>
				  </s:url>
				  <s:url id='toolsNewsMaintLink' namespace="/profile/user" action='xpedxNewsMaintenance'>					
				  </s:url>
<%-- 				  <div id="breadcumbs-list-name" class="page-title"><s:a href="%{toolsLink}">Tools</s:a> /  <s:a href="%{toolsNewsMaintLink}"> News Maintenance</s:a> /  <span class="breadcrumb-inactive">  New Article</span> <a href="#"><span class="print-ico-xpedx"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/print-icon.gif" width="16" height="15" alt="Print This Page" />Print Page</span></a>     </div> --%>
			     
		          <div id="requestform">
<%-- 		          <div>
          				<a class="underlines" href="javascript:history.go(-1)">Back</a>&nbsp;/&nbsp;<strong>News Article</strong> 
 				  </div>  --%>
 				  <div>
 				 	 <%-- <span class="page-title">News Maintenance</span> --%>
 				 	 <span class="page-title"><s:text name='MSG.SWC.NEWSARTL.CREATE.GENERIC.PGTITLE' /></span>
 				  </div>
 				  <br/>
		          <s:form name="newArticleForm" action="xpedxViewArticle" namespace="/profile/user" method="POST">
					<input type="hidden" name="nextActionName" value="xpedxAddArticle"/>
				    <input type="hidden" name="backActionName" value="XPEDXNewArticle"/>
				    <input type="hidden" name="articleKey" value=""/>
				    <input type="hidden" name="currentArtList" id="currentArtList" value='<s:property value="#parameters['artNamesList']"/>'/>
		        <!--     <div class="error" id="errorMsgFor_articleTitle" style="display : none">You can't have two article with same "Article Title". Please give a different name.</div> -->				    
		            <div class="error" id="errorMsgFor_articleTitle" style="display : none"> <s:text name='MSG.SWC.NEWSARTL.CREATE.ERROR.DUPLICATE' /> </div>				    
		         	
		         	 <table   width="100%" class="form"  id="order-filter">
		            <tr>
		               <td width="80" class="no-border-left" > Article Title:</td>
		               <td colspan="3" valign="top" ><input name="articleName" id="articleName" type="text"  style="width:705px;" class="x-input" maxlength="50" /></td>
		            </tr>
	
		            
		            <tr>
		              <td> Effective Date:</td>
		              <td width="120" valign="top"><div class="demo"><input type="text" name="submittedTSFrom" id="submittedTSFrom" class="x-input datepicker" value="" size="14" maxlength="10"/></div></td>
		              <td width="87">Expiration Date:</td>
		              <td ><div class="demo"><input type="text" name="submittedTSTo" id="submittedTSTo" class="x-input  datepicker" value="" size="14" maxlength="10"/></div></td>
		            </tr> 
		          <tr>
		              <td  colspan="4"  style="padding-bottom: 0;">
 				  		<span class="dkcharcole"> 
 				  		<!-- Note: It is not possible to attach or paste a local file (like an image) directly to a website. Please ensure files can be accessed from the Internet. -->
 				  		<s:text name='MSG.SWC.NEWSARTL.NEWSMAINTENANCE.INFO.ATTACHEDFILEACESS' /> 
 				  		</span>
		           	</td>
		           </tr>
		           
		            <tr>
		              <td  colspan="4"><textarea  name="articleBody" id="articleBody" rows="20" class="ckeditor x-input article-textarea" style="width:924px;" maxlength="2000" onkeyup="return ismaxlength(this)"></textarea></td>
<!-- 		              cols="9"  -->
		            </tr>		            
		            <tr>
		              <td colspan="4">
		              <ul id="cart-actions" class="float-right news-page">
		            <li><s:a cssClass="grey-ui-btn" href="%{toolsNewsMaintLink}"><span>Cancel</span></s:a></li>
		           <!--<li><s:a href="javascript:document.newArticleForm.submit();" onclick="javascript:isDateSelected();" cssClass="orange-ui-btn"><span>Preview</span></s:a></li>  commented for 2647jira-->
		            <li><s:a href="#" onclick="javascript:isArticleFormValidation();" cssClass="orange-ui-btn"><span>Preview</span></s:a></li> <!-- modified for jira  2647 -->
		            
		            
		          	</ul></td>
		            </tr>
		          </table>
		          </s:form>
		        </div> 
		<div class="clearview">&nbsp;</div>  
		      
		</div>
			</div>
			      <!-- End Pricing -->
			   <br />
			</div>
			
		</div>
	<!-- end container  -->  
          
		<s:action name="xpedxFooter" executeResult="true" namespace="/common" />            
</body>
</html>