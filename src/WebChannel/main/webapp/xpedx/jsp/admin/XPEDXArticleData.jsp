<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld" %>
<% request.setAttribute("isMergedCSSJS","true"); %>
<s:set name='_action' value='[0]' />
<s:set name="xutil" value="XMLUtils" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='dateUtilBean'/>
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils' id='util' />
<!-- Webtrends Tag starts -->
<Meta name="WT.ti" Content='<s:text name="tools.newsmaintenance.title" />'>
<!-- Webtrends Tag stops --> 

<%-- <title><s:text name="tools.newsmaintenance.title" /></title> --%>
<title><s:property value="wCContext.storefrontId" /> -  <s:text name='MSG.SWC.NEWSARTL.LISTALL.GENERIC.TABTITLE' /> </title>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="webapp-context" content="/swc" />

<!-- begin styles. -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#util.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#util.staticFileLocation' />/xpedx/css/theme/ADMIN<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#util.staticFileLocation' />/xpedx/css/global/IE.css" />
<![endif]-->
<!-- end styles -->

<!-- jQuery Base & jQuery UI -->
<script type="text/javascript" src="<s:property value='#util.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#util.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#util.staticFileLocation' />/xpedx/js/swc<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#util.staticFileLocation' />/xpedx/js/order/draftOrderList<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#util.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#util.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#util.staticFileLocation' />/xpedx/js/jquery.dropdownPlain<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#util.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.core<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#util.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="<s:property value='#util.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<!--[if IE]><script src="../other_libs/excanvas_r3/excanvas.js" type="text/javascript" charset="utf-8"></script><![endif]-->
<script src="<s:property value='#util.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF -->
<!-- Lightbox/Modal Window -->
<script type="text/javascript" src="<s:property value='#util.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#util.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<link rel="stylesheet" type="text/css"
href="<s:property value='#util.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
<!-- Added for sorting Arun 3-31 -->

<script type="text/javascript" src="<s:property value='#util.staticFileLocation' />/xpedx/js/sorttable<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<s:url id="delArticleURL" namespace="/profile/user" action="xpedxDeleteArticle"  />
</head>
<%-- <swc:extDateFieldComponentSetup/>
 --%>
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

    function selectAll(tableRef) {
		var tbody1 = document.getElementById(tableRef);
		var rowCount = tbody1.rows.length;        		
		for(var i=1; i<rowCount; i++) {
			var row = tbody1.rows[i]; 
			if (null != row) {
				 cell = row.cells[0];
				 childNodelenght = cell.childNodes.length ;
				for (j=0; j<childNodelenght; j++)
                {           
                    //if childNode type is CheckBox                 
                    if (cell.childNodes[j].type =="checkbox")
                    {
                    //assign the status of the Select All checkbox to the cell checkbox within the grid
                        cell.childNodes[j].checked = true;
                    }
                }
			}
		}		
	}

	//modified for jira 2484 - deletion of article
    function deleteSelected(tableRef) {
		var tbody = document.getElementById(tableRef);
		var rowCount = tbody.rows.length;        
		var countSelected = 0;
		var articleData = "";
		for(var i=rowCount; i>=1; i--) {
			var row = tbody.rows[i];
			if (null != row) {
				cell = row.cells[0];
				childNodelenght = cell.childNodes.length ;
				for (j=0; j<childNodelenght; j++)
                {           
                    //if childNode type is CheckBox                 
                    if (cell.childNodes[j] && cell.childNodes[j].type =="checkbox")
                    {
                    	var chkbox = cell.childNodes[j]
                    //Check whether Checkbox is checked or not
                        if(null != chkbox && true == chkbox.checked) {		
    				countSelected++;					
    					}
                    }
                }
			}         			
		} 
		
		if (countSelected == 0) {
			alert('Please select one article to delete.');
		} 
		else if(countSelected > 1){
			alert('Please select only one article to delete.');
		}
		else {
			for(var i=rowCount; i>=1; i--) {
				var row = tbody.rows[i];
				if (null != row) {
					var deleteRow = false;
					cell = row.cells[0];
					childNodelenght = cell.childNodes.length ;
					for (j=0; j<childNodelenght; j++)
	                {           
	                    //if childNode type is CheckBox                 
	                    if (cell.childNodes[j] && cell.childNodes[j].type =="checkbox")
	                    {
	                    	var chkbox = cell.childNodes[j]
	                    //Check whether Checkbox is checked or not
	                        if(null != chkbox && true == chkbox.checked) {
	                        		deleteRow = true;	
		                			var articleNo = chkbox.id;
		                			var articleNametoDel = document.getElementById("hdn_"+ articleNo).value;
		                			if(articleData == "" || articleData == null)
		                			{
		                				articleData = articleNametoDel;
		                			}else{    				            			
	                    				articleData = articleData+"|"+articleNametoDel; 
		                			}
	                			}                        				
	    					}
	                    }
                    	if(deleteRow) {
							document.getElementById('articleDataToDelete').value=articleData;
							document.getElementById('rowToDelete').value=i;
							document.getElementById('deleteFromTable').value=tableRef;
							
							$(document).ready(function(){
		                		$("#confirmDeleteArticle").fancybox({
		                			'autoDimensions'	: false,
		                			'width' 			: 300,
		                			'height' 			: 150  
		                		}).trigger('click');
		                	    
		                	});							
                    	}
	                }					
				}
				//var customerId =  document.getElementById("customerId").value;
			}
			
		}
	
	function deleteRowSelected(articleNo, tableRef) {	
		var tbody = document.getElementById(tableRef);
		var rowCount = tbody.rows.length;
		var articleNametoDel = document.getElementById("hdn_"+ articleNo).value;
		for(var i=rowCount; i>=1; i--) {
			var row = tbody.rows[i];
			if (null != row) {
				var deleteRow = false;
				cell = row.cells[3];
				childNodelenght = cell.childNodes.length ;
				for (j=0; j<childNodelenght; j++)
                {
					var articleName = cell.childNodes[j].textContent;	
					if (articleName != null)
						articleName = articleName.trim();
					if (typeof(articleName) == 'undefined')
						articleName = cell.childNodes[j].childNodes[0].nodeValue;			
					
					if(articleNametoDel == articleName) {					
						document.getElementById('articleDataToDelete').value=articleNametoDel;
						document.getElementById('rowToDelete').value=i;
						document.getElementById('deleteFromTable').value=tableRef;
						$(document).ready(function(){
				    		$("#confirmDeleteArticle").fancybox({
				    			'autoDimensions'	: false,
				    			'width' 			: 300,
				    			'height' 			: 150  
				    		}).trigger('click');
				    	    
				    	});
					}                                        	                    	                   
                }
			}
		}		
	}


	function fancyBoxCloseAndDelArticle(){
		$.fancybox.close();
		setTimeout("deleteArticle()", 2000);
	}
	function deleteArticle() {
	
		var articleData = document.getElementById("articleDataToDelete").value;
		var customerID =  document.getElementById("articleCustomerId").value;
		var rowNo = document.getElementById("rowToDelete").value;
		var tableId = document.getElementById('deleteFromTable').value;
		var tbody = document.getElementById(tableId);
		if(articleData!= null && customerID!= null) {
			Ext.Ajax.request({
		    	// for testing only
		        url: '<s:property value="#delArticleURL" escape='false'/>',
		        params: {
			        articleData : articleData,
			       	customerId : customerID
			     },
		        // end testing
		        method: 'POST',
		        success: function (response, request){  
			    	tbody.deleteRow(rowNo);    
		            alert("Successfully Deleted "+ articleData+" Article");
		            },
		        failure: function (response, request){
				        alert("Failed to Delete " + articleData +" Article");
				        window.location.reload()
		             }
		    });
		}	
	}
    
</script>


<body class="ext-gecko ext-gecko3">
		<div id="main-container">
			<div id="main">
				<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
				<!-- // header end -->
				
				<!-- added for jira 2484  -->
				<s:hidden name='articleDataToDelete' id='articleDataToDelete' value=""/>
				<s:hidden name='rowToDelete' id='rowToDelete' value=""/>
				<s:hidden name='deleteFromTable' id='deleteFromTable' value=""/>
				<!-- end for jira 2484  -->
			<div class="container">
			      <!-- breadcrumb -->
			     <div id="mid-col-mil"><br />
			      
			      <s:url id='toolsLink' namespace='/xpedx/tools' action='XPEDXTools'>
						<s:param name="xpedxSelectedHeaderTab">ToolsTab</s:param>
				  </s:url>
				  <s:url id='newArticleLink' namespace='/profile/user' action='XPEDXNewArticle'>
				  </s:url>
				
				  <s:form id='newArticleForm' namespace='/profile/user' action='XPEDXNewArticle' name='newArticleForm'>
					  <s:iterator value='articleLines' id='articleLine' status="articleLineCount">
			     	  	<s:set name="artName" value='#xutil.getAttribute(#articleLine,"ArticleName")' />
						<s:hidden name="artNamesList" value="%{#artName}"/>
					  </s:iterator>
				  </s:form>
				  
<%-- 			      <div id="breadcumbs-list-name" class="page-title"><s:a href="%{toolsLink}"><s:text name="tools.title" /></s:a> /  <span class="breadcrumb-inactive"><s:text name="tools.newsmaintenance.title" /></span> <a href="javascript:window.print()"><span class="print-ico-xpedx"><img src="<s:property value='#util.staticFileLocation' />/xpedx/images/common/print-icon.gif" width="16" height="15" alt="Print This Page" />Print Page</span></a>     </div> --%>
					<div>
						<%-- <span class="page-title">News Articles</span> --%>
						<span class="page-title"> <s:text name="MSG.SWC.NEWSARTL.LISTALL.GENERIC.PGTITLE" /> </span>
					</div>
			      
<!-- 			      <ul id="tool-bar" class="tool-bar-top"> -->
<%-- 			         <li><s:a cssClass="orange-ui-btn" href="%{newArticleLink}" id="various3" ><span>New Article</span></s:a></li> --%>
<!-- 				  </ul> -->
					<s:hidden  value="%{newArticleLink}" id="various3" /> 
 					 <div class="clearview black padding-bottom4"> 
 					    <ul class="float-right">
      						<li><s:a cssClass="orange-ui-btn" href="javascript:document.newArticleForm.submit();" id="various3"><span>Create New Article</span></s:a></li> 
    					</ul>  
    					
    				
    				    </div>
				<!--  <s:hidden id="customerId" value='%{wCContext.customerId}' name='customerId'/>  commented for jira 2484 -->
				  	<a id="confirmDeleteArticle" href="#deleteArticleDiv"></a>
				   	<table style="width: 100%;" id="articletable" class="sortable">
	                    <tr class="table-header-bar">
	                      		<td width="6%" class="no-border table-header-bar-left padding8 c1 sorttable_nosort "><!--<span class="white txt-small"><a href="javascript:selectAll('articletable');" class="white">&nbsp;</a></span>--></td>
	                            <td width="13%" align="left" class="no-border sortable c2 text-center "><span class="white txt-small">Effective</span></td>
	                            <td width="13%" align="left" class="no-border sortable c2 text-center "><span class="white txt-small">Expiration</span></td>
	                        	<td width="48%" align="left" class="no-border sortable c2 "><span class="white txt-small">Article Title</span></td>
	        					<td width="20%" align="left" class="table-header-bar-right no-border sortable c2 text-center "><span class="white txt-small">Last Modified By</span></td>
	         			</tr>
	                    <s:set name="modifiedUserFormattedNames" value='#util.createModifyUserNameMap(articleLines)'/>
						<s:iterator value='articleLines' id='articleLine' status="articleLineCount">
							<s:set name="StartDate" value='%{#dateUtilBean.formatDate(#xutil.getAttribute(#articleLine,"StartDate"),wCContext)}' />
							<s:set name="EndDate" value='%{#dateUtilBean.formatDate(#xutil.getAttribute(#articleLine,"EndDate"),wCContext)}' />
							<s:set name="ArticleName" value='#xutil.getAttribute(#articleLine,"ArticleName")' />
							<s:set name="Article" value='#xutil.getAttribute(#articleLine,"Article")' />
							<s:set name="Forced" value='#xutil.getAttribute(#articleLine,"ForcedMessage")' />
							<s:set name="ModifiedUserId" value='#xutil.getAttribute(#articleLine,"Modifyuserid")' />
							<!-- added for 2484 jira -->
							<s:set name="ArticleCustomerID" value='#xutil.getAttribute(#articleLine,"CustomerID")' />
							<s:hidden name="articleCustomerId" id="articleCustomerId" value="%{#ArticleCustomerID}" /> 
							<!-- end of 2484 -->                 
							<s:if test="#articleLineCount.even">
								<tr class="odd">
							</s:if>
	                        <s:else>
								<tr>
							</s:else>
	                              <td class="c1 text-center ">
	                              <!-- -FXD3- Remove Item redX Functionality -->
	                               <!-- <input id="<s:property value='#articleLineCount.count'/>" type="checkbox" name="articleChkbox_<s:property value='#articleLineCount.count' />" class=" margin-15" />-->
	                               <a href='javascript:deleteRowSelected("<s:property value='#articleLineCount.count'/>", "articletable");'><img id="<s:property value='#articleLineCount.count'/>" name="articleChkbox_<s:property value='#articleLineCount.count' />" src="<s:property value='#util.staticFileLocation' />/xpedx/images/icons/12x12_red_x.png" title="Remove"/></a>
	                              </td>
	                              <td class="c2 text-center " ><s:property value='%{#StartDate}'/></td>
	                              <td class="c3 text-center " ><s:property value='%{#EndDate}'/></td>
	                              <td class="c4" >
	                              <a href="<s:url namespace='/profile/user' action='xpedxEditArticle'><s:param name="articleKey" value='#xutil.getAttribute(#articleLine,"ArticleKey")' /></s:url>">
	                              <s:property value='%{#ArticleName}'/></a></td>
	                              <td class="c5 text-center" ><s:property value='%{#modifiedUserFormattedNames.get(#ModifiedUserId)}'/></td>
	                        </tr>
								<s:hidden id="hdn_%{#articleLineCount.count}" value="%{#ArticleName}"/>
	                              
						</s:iterator>
						</tbody>
					</table> 
					
					<div id="table-bottom-bar" style="width:100%; clear:both;">
			          <div id="table-bottom-bar-L"></div>
			          <div id="table-bottom-bar-R"></div>
			        </div>
					
					
					
		        	<div class="clearview">&nbsp;</div>
		        	<div class="clearview">
		        	<ul class="float-right"><li><s:a cssClass="orange-ui-btn" href="javascript:document.newArticleForm.submit();" id="various3"><span>Create New Article</span></s:a></li></ul>  
		         	
		         	  <!-- <fieldset class="feildsets" style="width:120px;"><legend>For Selected Items:</legend> 
		        	  <ul id="tool-bar" class="tool-bar-top padding-left2 padding-top2" >
		                <li><a class="grey-ui-btn" href="javascript:deleteSelected('articletable')"><span>Remove</span></a></li>
		                <li><a id="confirmDeleteArticle" href="#deleteArticleDiv" style="display: none;"><span>Confirm Delete</span></a></li>
		              </ul>
		              </fieldset>-->
					</div>
		       		<div class=" bot-margin">&nbsp;</div>
		       		<div class=" bot-margin">&nbsp;</div>
				</div>
			      <!-- End Pricing -->
			   <br />
			</div>
		</div>
		</div>
	<!-- end container -->
          
		<s:action name="xpedxFooter" executeResult="true" namespace="/common" />    
		
		<!-- added for jira 2484 confirmation box -->  
		<div style="display: none;">
	<div id="deleteArticleDiv">
		<!-- <h2>Delete Article</h2> -->
		<h2> <s:text name='MSG.SWC.NEWSARTL.DELETE.GENERIC.PGTITLE' /> </h2>
		
		<br /><hr /><br></br>
		
		<!-- <p>Are you sure you would like to delete this article?</p><br></br> <br></br> -->
		<p> <s:text name='MSG.SWC.NEWSARTL.DELETE.GENERIC.DELETECONFIRM' /> </p><br></br> <br></br>
			<ul id="tool-bar" class="tool-bar-bottom">
			<li>
				<a class="grey-ui-btn" href="javascript:$.fancybox.close();">
					<span>No</span>
				</a>
			</li>
			<li class="float-right">
				<a class="green-ui-btn" href="javascript:fancyBoxCloseAndDelArticle();">
					<span>Yes</span>
				</a>
			</li>
			</ul>
	</div>
	</div>        
</body>
</html>