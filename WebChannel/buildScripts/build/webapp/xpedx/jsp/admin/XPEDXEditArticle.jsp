<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld" %>

<s:set name='_action' value='[0]' />
<s:set name="xutil" value="XMLUtils" />
<s:set name="articleElement" value="articleElement" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<!--Web trends tag start  -->
<meta name="DCSext.w_x_news_edit" content="1" />
<Meta name="WT.ti" Content="News Maintenance">
<!--Web trends tag end  -->
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='dateUtilBean'/>
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils' id='util' />

<style type="text/css">
.feildsets {
	-moz-border-radius:4px 4px 4px 4px;
	border:1px solid #D3D3D3;
	padding-left:5px;
}
.feildsets legend {
	color:#999999;
	font-size:11px;
	font-style:italic;
	margin-left:7px;
	padding:0 3px;
}
.feild-service {
	-moz-border-radius:4px 4px 4px 4px;
	border:1px solid #D3D3D3;
	margin-right:15px;
	padding-left:5px;
}
.feild-service legend {
	color:#003366;
	font-size:14px;
	font-weight:bold;
	margin-left:7px;
	padding:0 3px;
}
</style>

<title> News Maintenance</title>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="webapp-context" content="/swc" />




<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/global-1.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/home/home.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/home/portalhome.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/catalog/narrowBy.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/catalog/catalogExt.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/styles.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/ext-all.css" />

<s:include value="../common/XPEDXStaticInclude.jsp"/>
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/xpedx-mil.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/xpedx-forms.css"/>
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/xpedx-quick-add.css"/>
<!-- <link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/prod-details.css"/> -->
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/xpedx-mil-new.css" />

<!-- jQuery Base & jQuery UI -->
<script type="text/javascript" src="/swc/xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/swc.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/order/draftOrderList.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget.js"></script>

<link type="text/css" href="/swc/xpedx/js/jquery-ui-1/development-bundle/themes/base/jquery.ui.all.css" rel="stylesheet" />
<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="/swc/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified.js" type="text/javascript" charset="utf-8"></script>
<script src="/swc/xpedx/js/jquery-tool-tip/bgiframe_2.1.1/jquery.bgiframe.min.js" type="text/javascript" charset="utf-8"></script>
<!--[if IE]><script src="../other_libs/excanvas_r3/excanvas.js" type="text/javascript" charset="utf-8"></script><![endif]-->
<script src="/swc/xpedx/js/jquery-tool-tip/jquery.bt.min.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF -->
<!-- Lightbox/Modal Window -->
<script type="text/javascript" src="/swc/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/fancybox/jquery.fancybox-1.3.4.js"></script>
<link rel="stylesheet" type="text/css" href="/swc/xpedx/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
<link rel="stylesheet" type="text/css" href="/swc/xpedx/js/jquery-ui-1/css/smoothness/jquery-ui-1.8.2.custom.css" media="screen" />

<script type="text/javascript" src="/swc/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/jquery.ui.datepicker.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/ckeditor/ckeditor.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$(document).pngFix();
	});
	$(function() {
		$(".datepicker").keydown(function(){ return false; }); 
		$(".datepicker").datepicker({
			showOn: 'focus',
			numberOfMonths: 1,
			buttonImage: '/swc/xpedx/images/theme/theme-1/calendar-icon.png',
			buttonImageOnly: true
		});
	});
</script>

<style>
.ui-datepicker-trigger {
margin-left:116px;
margin-top:2px; 
} 

.underlines a:link {
    color: #0A2B70;
    text-decoration: none;
}
.form ul li {
    margin-right: 1px;
}
#order-filter table {
    width: 100%;
}
/* required for text area to show properly */
</style>
</head>
<swc:extDateFieldComponentSetup/>
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

	function articleFormValidation(){
		var articleName=document.getElementById("articleName").value;

		if(articleName=="" || articleName==null){

			document.getElementById("articleName").style.borderColor="#FF0000";
			alert("Article Title cannot be blank. Please enter the article name.");
			document.getElementById("articleName").focus();
			return false; 
			}

		if(document.getElementById("submittedTSTo").value < document.getElementById("submittedTSFrom").value){
			document.getElementById("submittedTSTo").style.borderColor="#FF0000";
			alert("Article expiration date cannot be less than effective date. Please re-enter the expiration date");
		 	document.getElementById("submittedTSTo").focus();
			return false;
			}
	else{
		document.newArticleForm.submit();
		}
	}
</script>


<body class="ext-gecko ext-gecko3">
		<div id="main-container">
			<div id="main">
				
				<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
				<!-- // header end -->			
			<div class="container no-padding">
			      <!-- breadcrumb -->
			     <div id="mid-col-mil">
			      <br/>
			      <s:url id='toolsLink' namespace='/xpedx/tools' action='XPEDXTools'>
						<s:param name="xpedxSelectedHeaderTab">ToolsTab</s:param>
				  </s:url>
				  <s:url id='toolsNewsMaintLink' namespace="/profile/user" action='xpedxNewsMaintenance'>					
				  </s:url>
<%-- 				  <div id="breadcumbs-list-name" class="page-title"><s:a href="%{toolsLink}">Tools</s:a> /  <s:a href="%{toolsNewsMaintLink}"> News Maintenance</s:a> /  <span class="breadcrumb-inactive">  New Article</span> <a href="javascript:window.print()"><span class="print-ico-xpedx"><img src="/swc/xpedx/images/common/print-icon.gif" width="16" height="15" alt="Print This Page" />Print Page</span></a>     </div> --%>
			     
			       <div id="requestform">
			        <div>
          				<%-- <a class="underlines" href="javascript:history.go(-1)">Back</a>&nbsp;/&nbsp;<strong><s:property value='#xutil.getAttribute(#articleElement,"ArticleName")'/></strong> --%>
      					<span class="page-title">News Maintenance</span> 
 				  </div> 
 				  <br/>
		          <s:form name="newArticleForm" action="xpedxViewArticle" namespace="/profile/user" method="POST">
				  <input type="hidden" name="nextActionName" value="xpedxUpdateArticle"/>
				  <input type="hidden" name="backActionName" value="xpedxEditArticle"/>
				  <input type="hidden" name="articleKey" value="<s:property value="#parameters['articleKey']"/>"/>
				  
		          <table  width="100%" class="form"  id="order-filter" >
		            <tr>
		              <td class="no-border-left" > Article Title:</td>
		              <td colspan="3" valign="top" ><input name="articleName" id="articleName" type="text" style="width:705px;" class="x-input" maxlength=50 value="<s:property value='#xutil.getAttribute(#articleElement,"ArticleName")'/>"/></td>
		            </tr>
		            <tr>
		              <td  width="79"> Effective Date:</td>
		              <td width="120" valign="top"><div class="demo"><input type="text" name="submittedTSFrom" id="submittedTSFrom" class="x-input datepicker" value="<s:property value='%{#dateUtilBean.formatDate(#xutil.getAttribute(#articleElement,"StartDate"),wCContext)}'/>" size="14" maxlength="10"/></div></td>
		              <td width="87">Expiration Date:</td>
		              <td valign="top"><div class="demo"><input type="text" name="submittedTSTo" id="submittedTSTo" class="x-input  datepicker" value="<s:property value='%{#dateUtilBean.formatDate(#xutil.getAttribute(#articleElement,"EndDate"),wCContext)}'/>" size="14" maxlength="10"/></div></td>
		            </tr> 
		            <tr>
		              <td  colspan="4"  style="padding-bottom: 0;">
 				  		<span class="dkcharcole"> Note: It is not possible to attach or paste a local file (like an image) directly to a website. Please ensure files can be accessed from the Internet.</span>
		           	</td>
		           </tr>
		             <tr>
		              <td  colspan="4"><textarea cols="9" name="articleBody" id="articleBody" rows="20" class="ckeditor x-input article-textarea" style="width:815px;" maxlength="2000" onkeyup="return ismaxlength(this)"><s:property value='#xutil.getAttribute(#articleElement,"Article")'/></textarea></td>
		            </tr>
		           <tr>
		              <td colspan="4"><ul id="cart-actions" class="float-right">
		            <li><s:a cssClass="grey-ui-btn" href="%{toolsNewsMaintLink}"><span>Cancel</span></s:a></li>
		         <!-- <li><s:a href="javascript:document.newArticleForm.submit();" cssClass="orange-ui-btn"><span>Preview</span></s:a></li>   -->
		            <li><s:a href="#" onclick="javascript:articleFormValidation();" cssClass="orange-ui-btn"><span>Preview</span></s:a></li>
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