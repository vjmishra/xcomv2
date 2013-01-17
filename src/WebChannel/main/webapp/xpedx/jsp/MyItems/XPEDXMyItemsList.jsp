<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<%@ taglib prefix="xpedx" uri="xpedx" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta content='IE=9' http-equiv='X-UA-Compatible' />
<%
  		request.setAttribute("isMergedCSSJS","true");
  	  %>
<s:set name="CurrentCustomerId" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@getCurrentCustomerId(wCContext)" />
<s:set name="SelectedCustomerId" value="wCContext.customerId" />
<s:set name="xpedxCustomerContactInfoBean" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("XPEDX_Customer_Contact_Info_Bean")' />

<!-- begin styles. These should be the only three styles. -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/MIL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<![endif]-->
<!-- end styles -->

<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel.js"></script>
--><!-- carousel scripts js   -->


<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />

 

<!-- Hemantha -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-jquery-headder<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- Arun uses value Current customer id for this javascript  -->
<script type="text/javascript">
			$(document).ready(function() {
				$("#various1").fancybox({
					'titleShow'			: false,
					'transitionIn'		: 'fade',
					'transitionOut'		: 'fade'
				});

				$("#various5").fancybox({
					'onStart' 	: function(){
						showSelectedList('<s:property value="#CurrentCustomerId"/>');
						Ext.get("smilTitle_2").dom.innerHTML = "Manage My Items Lists for Selected Locations";//Changes made for JIRA 2774
					
					},
					'onClosed' : function(){
						var div = document.getElementById("dynamiccontent");
						div.style.display = "block";
						shareSelectAll(false);
					},
					'autoDimensions'	: false,
					'width' 			: 820,
					'height' 			: 250  
				});

				$("#dlgImportItemsLink").fancybox({
					'onClosed' : function(){
					document.getElementById("errorMsgForBrowsePath").style.display = "none";
					document.getElementById("errorMsgForRequiredField").innerHTML = "";
					document.getElementById("errorMsgForRequiredField").style.display = "none";
					if(document.getElementById("File"))
						document.getElementById("File").value = "";
					},
					'autoDimensions'	:false,
	    			'width'				: 620,
	    			'height'			: 375
				});
				
				$("#various3,#various4").fancybox({
					'onStart' 	: function(){
						/*if (callShareList){
							showShareList('<s:property value="#CurrentCustomerId"/>');
						}
						if (isUserAdmin) {
							Ext.get("rbPermissionShared").dom.checked = true;
						} else {
							Ext.get("rbPermissionPrivate").dom.checked = true;
						}*/
						if (isUserAdmin){
							showShareList('<s:property value="#CurrentCustomerId"/>');
						}
						Ext.get("smilTitle").dom.innerHTML = "New My Items List";
					},
					'onClosed' : function(){
						document.getElementById("mandatoryFieldCheckFlag_dlgShareList").value = "false";
						document.XPEDXMyItemsDetailsChangeShareList.listName.style.borderColor="";
			            document.getElementById("errorMsgForMandatoryFields_dlgShareList").style.display = "none";
			            document.XPEDXMyItemsDetailsChangeShareList.listName.value = "";
						document.XPEDXMyItemsDetailsChangeShareList.listDesc.value = "";
						document.XPEDXMyItemsDetailsChangeShareList.shareAdminOnly.checked=false;
						var radioBtns = document.XPEDXMyItemsDetailsChangeShareList.sharePermissionLevel;
						var div = document.getElementById("dynamiccontent");
						if(!isUserAdmin)
						{
							//Check Private radio button
							radioBtns[0].checked = true;
							//Hide Ship To Locations
							div.style.display = "none";
						}
						else
						{		
							//Check Shared radio button
							radioBtns[1].checked = true;
							//Display Ship To Locations
							div.style.display = "block";
						}
						shareSelectAll(false);
						document.getElementById("errorMsgForAddressFieldsHL").style.display = "none";
						
					},
					'autoDimensions'	: false,
					'width' 			: 780,
					'height' 			: 450  
				});
				
			});
			
			function updateShareListChild(){
			}
		
			function hideSharedListForm(){
				document.getElementById("dynamiccontent").style.display = "none";
			}

			function hideSelectedListForm(){
				document.getElementById("dynamicContentInSelectedList").style.display = "none";
			}
			
			function showSharedListForm(){
			var dlgForm 		= document.getElementById("dynamiccontent");
			if (dlgForm){
				dlgForm.style.display = "block";
			}
			}

			function showSelectedListForm(){
				var dlgForm 		= document.getElementById("dynamicContentInSelectedList");
				if (dlgForm){
					dlgForm.style.display = "block";
				}
			}
			
			function deleteItemList(){
				doAction_delete_item_list.dom.submit();
			}
			
	</script>
<%-- <title><s:text name='myitemslists.title' /></title> --%>
<title><s:property value="wCContext.storefrontId" /> - <s:text name='MSG.SWC.MIL.MYITEMLISTS.GENERIC.TABTITLE' /></title>

</head>
<!-- END swc:head -->
 <s:url id="orderListPaginationURL" action="XPEDXMyItemsList">
    	 <s:param name="pageNumber" value="'{0}'"/>
    	 <s:param name="pageSetToken" value="%{pageSetToken}"/>
         <s:param name="orderByAttribute" value="orderByAttribute"/>
         <s:param name="orderDesc" value="orderDesc"/>
         <s:param name="customerIds" value="customerIds"/>
  		 <s:param name="customerPaths" value="customerPaths"/>
 </s:url>
 <s:url id="milListSortURL" action="XPEDXMyItemsList" >
  <s:param name="orderByAttribute" value="'{0}'"/>   
  <s:param name="pageNumber" value="%{pageNumber}"/>
  <s:param name="orderDesc" value="'{1}'"/>
  <s:param name="customerIds" value="customerIds"/>
  <s:param name="customerPaths" value="customerPaths"/>
   
</s:url>
   
<!-- CODE_START - Global Vars -PN -->
<s:set name='wcContext' value="wCContext" />
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />

<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs?. 
   	This is to avoid a defect in Struts that?s creating contention under load. 
   	The explicit call style will also help the performance in evaluating Struts? OGNL statements. 
   	--%>
<s:set name='_action' value='[0]' />

<s:set name="xutil" value="XMLUtils" />
<s:set name='categoryListElem' value="categoryListElement" />
<s:set name='childCategoryListElem' value="childCategoryListElement" />
<s:set name='fieldListElem' value="searchableIndexFieldListOutPutElement" />
<s:set name='outDoc2' value='%{outDoc.documentElement}' />

<!-- CODE_END - Global Vars -PN -->

<body class="  ext-gecko ext-gecko3">

<!-- CODE_START - Global HTML -PN -->
<style type="text/css">
.dlgForm {
	display: none;
	border-color: black;
	border-width: 10px;
	padding: 20px;
	background-color: white;
	position: absolute;
	left: 25%;
	top: 40%;
	z-index: 9999;
	width: auto;
	height: auto;
}
</style>
<SCRIPT type="text/javascript">
	
		function ReplaceAll(Source,stringToFind,stringToReplace){
		  var temp = Source;
		    var index = temp.indexOf(stringToFind);
		        while(index != -1){
		            temp = temp.replace(stringToFind,stringToReplace);
		            index = temp.indexOf(stringToFind);
				}
		        return temp;
		}
		
		function showForm(formId){
			var dlgForm 		= document.getElementById("dlg" + formId);
			
			if (dlgForm){
				dlgForm.style.display = "block";
			}
		}
		
		function hideForm(formId){
			var dlgForm 		= document.getElementById("dlg" + formId);
			
			if (dlgForm){
				dlgForm.style.display = "none";
			}
		}
		
		function editForm(formId, listKey, name, desc, customerId){
			//Populate fields
			var editForm 	= document.getElementById("formEdit");
			
			editForm.listKey.value 		= listKey;
			editForm.name.value 		= name;
			editForm.desc.value 		= desc;
			editForm.customerId.value 	= customerId;

			showForm(formId);
		}

		function showShareList(customerId, showRoot, clFromListId){
			//Populate fields
			var divMainId 	= "divMainShareList";
			var divMain 	= document.getElementById(divMainId);
			
			//Load the list if it has not been loaded before.
			//if (!isShareListLoaded){
				getShareList(customerId, divMainId, showRoot);
				isShareListLoaded = true;
			//}
			
			if (clFromListId != undefined || clFromListId != null){
				var x = Ext.get("clFromListId");
				x.dom.value = clFromListId;
				
				try{ console.log("From list id: " + clFromListId); }catch(ee){} ;
			}
			
			try{ console.log("showShareList: Customer ID: ", customerId); }catch(e){}
			try{ console.log("showShareList: ShowRoot: ", showRoot); }catch(e){}
			
			//showForm("ShareList");
			//showDialog('New item list', 'dlgShareList', 'Create new list', function(){ submitNewlist(); });
		}

		function showSelectedList(customerId, showRoot, clFromListId){
			//Populate fields
			var divMainId 	= "divMainSelectedList";
			divMainId.innerHTML = '';
			var divMain 	= document.getElementById(divMainId);
			
			//Load the list if it has not been loaded before.
			//if (!isShareListLoaded){
				getShareList(customerId,"", divMainId, showRoot);
				isShareListLoaded = true;
			//}
			
			if (clFromListId != undefined || clFromListId != null){
				var x = Ext.get("clFromListId");
				x.dom.value = clFromListId;
				
				try{ console.log("From list id: " + clFromListId); }catch(ee){} ;
			}
			
			try{ console.log("showShareList: Customer ID: ", customerId); }catch(e){}
			try{ console.log("showShareList: ShowRoot: ", showRoot); }catch(e){}
			
			//showForm("ShareList");
			//showDialog('New item list', 'dlgShareList', 'Create new list', function(){ submitNewlist(); });
		}
		
		String.prototype.trim = function() {
			return this.replace(/^\s+|\s+$/g,"");
		}

		function submitNewlistNew(docDivId, ignoreDivIds){
			try{
				if (mandatoryFieldValidation(docDivId, ignoreDivIds) != "")
					return;
			}catch(err){
			}
			
			submitNewlist();
		}
				
		function submitNewlist(){
			var form = Ext.get("XPEDXMyItemsDetailsChangeShareList");
			
			form.dom.submit();
			//Ext.Msg.wait('Saving changes...Please wait.');
		}

		function listNameCheck(component, docDivId){
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
		
		function resetclFromSelectedListId(){
			var form = Ext.get("XPEDXMyItemsSelectedList");
			form.dom.clFromListIdTemp.value="";
			 document.getElementById('rbFilterByPrivate').checked=true;
			
		}
			
		function submitNewSelectedlist(){
			var form = Ext.get("XPEDXMyItemsSelectedList");
			
			//var isLocSelected  = isLocationSelected( )
			
			//Validate form
			try{
				var clFromListIdVar = form.dom.clFromListId;
				clFromListIdVar.value=form.dom.clFromListIdTemp.value;
			}catch(err){
			}
			
		    form.dom.submit();
			
			//Ext.Msg.wait('Saving changes...Please wait.');
		}
		
		function resetclFromListId(){
			var form = Ext.get("XPEDXMyItemsDetailsChangeShareList");
			form.dom.clFromListIdTemp.value="";
		}
		function setAndExecute(divId, innerHTML, isCustomerSelected) {  
		   var div = document.getElementById(divId);  
		   div.innerHTML = innerHTML; 
		   saveShareListForChild(divId, isCustomerSelected); 
		   var x = div.getElementsByTagName("script");   
		   for( var i=0; i < x.length; i++) {  
		     eval(x[i].text);  
		   }  
		}

		function saveShareListForChild(divId, isCustomerSelected){
			   var allChildElements = $("div[id="+divId + "] :checkbox");
	           for(var j=0;j<allChildElements.length;j++)
				{
					var currentCB = allChildElements[j];
					currentCB.checked = isCustomerSelected;
				}
		}
		
		function getShareList(customerId, divId, showRoot){
            //Init vars
            <s:url id='getShareList' includeParams='none' action='XPEDXMyItemsDetailsGetShareList'/>
            if (showRoot == null){ showRoot = false; }

            var isCustomerSelected = false;
	           //Replace all '-' with '_'
	           var controlId = customerId.replace(/-/g, '_');
	           //Get the current checkbox selection of the customer
			   if(document.getElementById('customerPaths_'+controlId)!=null){
	               isCustomerSelected = document.getElementById('customerIds_'+controlId).checked;
			   }
            
            var url = "<s:property value='#getShareList'/>";
            url = ReplaceAll(url,"&amp;",'&');
            //Show the waiting box
            var x = document.getElementById(divId);
            x.innerHTML = "Loading data";
            
            
            //Execute the call
            document.body.style.cursor = 'wait';
            //alert("Customer id is: " + customerId + ", showRoot: " + showRoot);
            if(true){
                  Ext.Ajax.request({
                    url: url,
                    params: {
                              customerId: customerId,
                              showRoot: showRoot
                    },
                    method: 'POST',
                    success: function (response, request){
                        document.body.style.cursor = 'default';
                        setAndExecute(divId, response.responseText, isCustomerSelected);
                    },
                    failure: function (response, request){
                        var x = document.getElementById(divId);
                        x.innerHTML = "";
                        alert('Unable to load the share locations. Please try again.');//JIRA 3943
                        document.body.style.cursor = 'default';                                                  
                    }
                });     
            }
        	document.body.style.cursor = 'default';

		}
		
		function showDialog(title, contentId, okText, onOk){
		  Ext.onReady(function() {
		    var w = null;
		    
		    w = Ext.WindowMgr.get("win_" + contentId);
		    
		    if (w != undefined && w != null ){
		    	w.show(this);
		    	return;
		    }
		    
		    w = new Ext.Window({
		      autoScroll: true,
		      id: 'win_' + contentId,
		      closeAction: 'hide',
		      contentEl: contentId,
		      hidden: true,
		      modal: true,
		      width: 'auto',
		      height: 'auto',
		      resizable: true,
		      shadow: 'drop',
		      shadowOffset: 10,
		      title: title,
			  buttons: [{
				text: 'Close',
				handler: function(){
					w.hide();
				}
			  },{
			  	text: okText,
			  	handler: onOk
			  }]
		    });
		    w.show(this);
		  });
		}
		
		function submitCopyList(listId){
			var form = Ext.get('formCopyList');
			
			form.dom.clFromListId.value = listId;
			form.dom.submit();
		}
		
	</SCRIPT>

<!-- class: dlgFormShareList -->
<style>
.share-modal {
	width: 550px;
	height: 450px;;
}

.indent-tree {
	margin-left: 15px;
}

.indent-tree-act {
	margin-left: 25px;
}
</style>

	<s:set name="isUserAdmin" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@isCurrentUserAdmin(wCContext)" />
	<s:set name="isEstUser" value='%{#xpedxCustomerContactInfoBean.isEstimator()}' />
	<script type="text/javascript">
		var isUserAdmin = <s:property value="#isUserAdmin"/>;
		var isEstUser = <s:property value="#isEstUser"/>;
	
	</script>	
<div style="display: none;">
<div id="dlgShareList" class="share-modal xpedx-light-box">
<s:hidden id="mandatoryFieldCheckFlag_dlgShareList" name="mandatoryFieldCheckFlag_dlgShareList" value="%{false}"></s:hidden>
<h2 id="smilTitle">Share My Items List</h2>
<br/>

<!-- CODE_START MIL - PN --> <s:form id="XPEDXMyItemsDetailsChangeShareList" action="XPEDXMyItemsDetailsChangeShareList"
	method="post" enctype="multipart/form-data">
	<div class="error" id="errorMsgForMandatoryFields_dlgShareList" style="display : none"/></div>
	

	
	<p><strong>Name</strong>&nbsp;&nbsp;<input type="text" name="listName" id="listName" value="" maxlength="255" onkeyup="javascript:listNameCheck(this, 'dlgShareList');" onmouseover="javascript:listNameCheck(this, 'dlgShareList');"/></p>
	<p style="word-wrap:break-word; width: 50px;"><strong>Description</strong>&nbsp;&nbsp;<input type="text" name="listDesc" id="listDesc" value="" maxlength="30"/></p>
	
	<%--for jira 4134 - sales rep Last Modified By display  --%>
	 <s:set name="isSalesRep" value ="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute('IS_SALES_REP')}"/>
	<%--end of jira 4134 changes - sales rep Last Modified By display --%>
	
	<s:hidden name="listKey" value="new"></s:hidden>
	<s:hidden name="editMode" value="%{true}"></s:hidden>
	<s:hidden name="itemCount" value="%{0}"></s:hidden>
	<s:hidden id="clFromListId" name="clFromListId" value=""></s:hidden>
	<s:hidden id="clFromListIdTemp" name="clFromListIdTemp" value=""/>
	
	<s:set name="rbPermissionShared" value="%{''}" />
	
	<s:set name="rbPermissionPrivate" value="%{''}" />
	<s:if test="%{#isUserAdmin}">
		<s:set name="rbPermissionShared" value="%{' checked '}" />
	</s:if>
	<s:else>
		<s:set name="rbPermissionPrivate" value="%{' checked '}" />
	</s:else>
	
	<s:set name="saCV" value="%{''}" />
	<s:if test='%{#spShareAdminOnly == "Y"}'>
		<s:set name="saCV" value="%{' checked '}" />
	</s:if>

	<!-- START - Saved hidden data Fields -->
	<s:iterator id="item" value='savedSharedList'>
		<s:set name='customerID' value='#item.getAttribute("CustomerID")' />
		<s:set name='customerPath' value='#item.getAttribute("CustomerPath")' />

		<s:hidden name="sslNames" value="%{#customerID}"></s:hidden>
		<s:hidden name="sslValues" value="%{#customerPath}"></s:hidden>
	</s:iterator>
	<!-- END - Saved hidden data Fields -->

	<!-- Private and Shared are missing from the HTMLs -->
	<p><strong> This list is: 
	<input onclick="hideSharedListForm()" 
		id="rbPermissionPrivate" <s:property value="#rbPermissionPrivate"/>
		type="radio" name="sharePermissionLevel" 
		value="<s:property value="wCContext.loggedInUserId"/>" 
	/> 
	Private &nbsp;&nbsp; 
	
	<s:if test="%{!#isUserAdmin}">
		<div style="display: none;">
	</s:if>
		<input
			onclick="showSharedListForm()" 
			id="rbPermissionShared" 
			<s:property value="#rbPermissionShared"/>
			type="radio"
			name="sharePermissionLevel" 
			value=" "
		/> 
		Shared
	
	<s:if test="%{!#isUserAdmin}">
		</div>
	</s:if>

	</strong></p>
	<br />
	
	<s:set name="displayStyle" value="%{''}" />
	<s:if test="%{!#isUserAdmin}">
		<s:set name="displayStyle" value="%{'display: none;'}"/>
	</s:if>
	
	<div style="<s:property value="#displayStyle"/>" id="dynamiccontent" >
	<!-- Placeholder for the dynamic content -->
	<s:div id="dlgShareListShared" >
		<input type="checkbox" <s:property value="#saCV"/> name="shareAdminOnly" value="Y" /> Edit by Admin users only<br />
		<br />
	<script type="text/javascript">
		
		function shareSelectAll(checked_status){
			//var checked_status = this.checked;
			var checkboxes = Ext.query('input[name*=customerPaths]');
			Ext.each(checkboxes, function(obj_item){
				obj_item.checked = !checked_status;
				obj_item.click();
				//obj_item.fireEvent('click');
			});
		}
		
	</script>

		<a href="javascript:shareSelectAll(true)" >Select All</a>
		<a href="javascript:shareSelectAll(false)" >Deselect All</a>
		
		<!-- START - BODY OF SHARE FORM -->
		<s:div id="divMainShareList" cssClass="grey-msg x-corners">
			<!-- CONTENT WILL GO HERE -->
		</s:div>
		<!-- END - BODY OF SHARE FORM -->
	</s:div>
	
	</div>


	<SCRIPT type="text/javascript">
					/*if ("<s:property value="rbPermissionPrivate"/>" != ""){
						hideForm('ShareListShared');
					}*/
					
					function submitSL(docDivId, ignoreDivIds){
						submitNewlistNew(docDivId, ignoreDivIds);
					} 
				</SCRIPT>

	</br>
	</br>
	<ul id="tool-bar float-right" class="tool-bar-bottom" style="float:right; padding-top:5px; width:152px; margin-right:5px;">
		<li><a class="grey-ui-btn" href="javascript:$.fancybox.close(); resetclFromListId() ;"><span>Cancel</span></a></li>
		<li style="float: right;"><a href="javascript:submitSL('dlgShareList', []);" > <img
			src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/ui-buttons/ui-btn-save<s:property value='#wcUtil.xpedxBuildKey' />.gif" width="49" height="23" alt="Save" title="Save" /> </a></li>
	</ul>

</s:form> <!-- CODE_END MIL - PN --></div>

<!-- class: dlgFormShareList -->

<s:include value="../modals/XPEDXItemsForLocationModal.jsp"></s:include>

<div id="dlgNewForm" class="dlgForm">
<h3>New List</h3>
<s:form action="XPEDXMyItemsListCreate" method="post">
	<s:textfield label="Name" name="name"></s:textfield>
	<s:textfield label="Desc" name="desc"></s:textfield>
	<s:hidden name="customerId" value="1"></s:hidden>
	<s:hidden name="sharePrivate" value="#wcContext.loggedInUserId"></s:hidden>

	<br>
	<br>

	<s:submit value="Create New List" align="center"></s:submit> &nbsp;&nbsp;
	<input type="button" value="Cancel" onclick="javascript:resetclFromListId();hideForm('NewForm')" />
</s:form>
</div>

<a id="dlgImportItemsLink" href="#dlgImportForm"></a>

<div id="dlgEditForm" class="dlgForm">
<h3>Edit Record</h3>
<s:form id="formEdit" action="XPEDXMyItemsListChange" method="post">
	<s:textfield label="Name" name="name"></s:textfield>
	<s:textfield label="Desc" name="desc"></s:textfield>
	<s:hidden name="listKey" value=""></s:hidden>
	<s:hidden name="customerId" value="1"></s:hidden>

	<br>
	<br>

	<s:submit value="Save" align="center"></s:submit> &nbsp;&nbsp;
			<input type="button" value="Cancel" onclick="hideForm('EditForm')" />
</s:form></div>
</div>
<!-- CODE_END - Global HTML -PN -->

<div id="main-container">
<div id="main">

<!-- CODE_START Header - PN --> 
<s:action name="xpedxHeader" executeResult="true" namespace="/common" >
   <s:param name='shipToBanner' value="%{'true'}" />
</s:action>
<!-- CODE_END Header - PN -->

<%--JIRA 3711 START --%>

<%-- <s:if test='!#guestUser'>  	

		<s:action name="xpedxShiptoHeader" executeResult="true"
		namespace="/common" />
	</s:if>	
 --%>	
<%--JIRA 3711 End --%>

<div class="container mil-list"><!-- breadcrumb -->
<div id="breadcumbs-list-name" class="page-title"><s:text name='MSG.SWC.MIL.MYITEMLISTS.GENERIC.PGTITLE' /></div>

<div id="mid-col-mil">
<s:form id="filterByForm" action="XPEDXMyItemsList.action" method="post">
<div class="float-left" style="width:325px;"> 
<table>
<tr>
<td>
<img id="whitecart" style="display:block;float:left;" alt="" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/mil/20x20_personal_list.png"/>
<label>Personal</label>
</td>
<td>&nbsp;&nbsp;</td>
<td>
<img id="whitecart" style="display:block;float:left;" alt="" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/mil/20x20_shared_list.png"/>	
<label>Shared</label>						
</td>
</tr>
</table>
<br></br>
<table>
<tr>
<td>
<s:if test="#isUserAdmin">				
<tr>
<td height="5px" width="5px">
  <a class="underlink"   id="" name="" href="javascript:showListForSelectedOption();">  
  <div id="Layer2" style="FONT-WEIGHT: bold; WIDTH:300px; HEIGHT: 10px">Manage My Items Lists for Other Locations</div></a>
 
</td>
<td valign="right">
<div class="search-pagination-bottom" style="WIDTH:630px; HEIGHT: 10px">
                  <s:if test="%{totalNumberOfPages>1}">Page</s:if>&nbsp;&nbsp;<xpedx:pagectl currentPage="%{pageNumber}" lastPage="%{totalNumberOfPages}" showFirstAndLast="False"
                 	urlSpec="%{#orderListPaginationURL}" isAjax="false"/>
			</div>
</td>
</tr>
</s:if>
<s:elseif test="#isEstUser">
<tr>
<td height="5px" width="5px">
  <a class="underlink"   id="" name="" href="javascript:showListForSelectedOption();">  
  <div id="Layer2" style="FONT-WEIGHT: bold; WIDTH:300px; HEIGHT: 10px">Manage My Items Lists for Other Locations</div></a>
 
</td>
<td valign="right">
<div class="search-pagination-bottom" style="WIDTH:630px; HEIGHT: 10px">
                  <s:if test="%{totalNumberOfPages>1}">Page</s:if>&nbsp;&nbsp;<xpedx:pagectl currentPage="%{pageNumber}" lastPage="%{totalNumberOfPages}" showFirstAndLast="False"
                 	urlSpec="%{#orderListPaginationURL}" isAjax="false"/>
			</div>
</td>
</tr>
</s:elseif>
<s:else>
<tr>
<td height="5px" width="5px">
 <a class="underlink"   id="" name="" href="javascript:showListForSelectedOption();">  
  <div id="Layer2" style="FONT-WEIGHT: bold; WIDTH:300px; HEIGHT: 10px"></div></a>
 
</td>
<td valign="right">
<div class="search-pagination-bottom" style="WIDTH:630px; HEIGHT: 10px">
                  <s:if test="%{totalNumberOfPages>1}">Page</s:if>&nbsp;&nbsp;<xpedx:pagectl currentPage="%{pageNumber}" lastPage="%{totalNumberOfPages}" showFirstAndLast="False"
                 	urlSpec="%{#orderListPaginationURL}" isAjax="false"/>
			</div>
</td>
</tr>

 </s:else>

</td>
</tr>
</table>
<!-- <div id="tool-bar-bottom" class="float-left">
 </div>
 -->
<!-- <img id="whitecart" style="display:block;float:left;" alt="" src="/swc/xpedx/images/icons/20x20_personal_list.png">

           <input 
							onclick="javascript:showListForSelectedOption();" 
							id="rbFilterByPrivate"
							type="radio" 
							name="filterByOption" 
							<s:if test="%{#_action.getFilterByMyListChk()}"> 
								checked="checked"
							</s:if>
							value="1" 
			/> 
						
			 
			 <label>Personal</label>

            <img id="whitecart" style="display:block;float:left;" alt="" src="/swc/xpedx/images/icons/20x20_shared_list.png">		
					
           <input
							onclick="javascript:showListForSelectedOption();" 
							id="rbFilterBySelected"
							type="radio"
							name="filterByOption"
							<s:if test="%{#_action.getFilterBySelectedListChk()}"> 
								checked="checked "
							</s:if>
							value="2"
						/>
						<label>Share</label>
						
 -->						 
						
 <s:a href="#dlgSelectedList" id="various5" cssStyle="display:none;"></s:a> 
           						</div>

 <%-- <div class="float-left" style="width:325px;"> List Type: &nbsp;

           <input 
							onclick="javascript:showListForSelectedOption();" 
							id="rbFilterByPrivate"
							type="radio" 
							name="filterByOption" 
							<s:if test="%{#_action.getFilterByMyListChk()}"> 
								checked="checked"
							</s:if>
							value="1" /> 
							
			 <label>Personal</label>

           &nbsp; &nbsp; &nbsp;
           <input
							onclick="javascript:showListForSelectedOption();" 
							id="rbFilterBySelected"
							type="radio"
							name="filterByOption"
							<s:if test="%{#_action.getFilterBySelectedListChk()}"> 
								checked="checked "
							</s:if>
							value="2"
						/>
						
						 <label>Shared</label>
						<s:a href="#dlgSelectedList" id="various5" cssStyle="display:none;"></s:a> 
           						</div>
 --%>
 
<s:hidden name="filterBySelectedListChk" value="%{#_action.getFilterBySelectedListChk()}"/>
<s:hidden name="filterByMyListChk" value="%{#_action.getFilterByMyListChk()}"/>
<s:hidden name="filterByAllChk" value="%{#_action.getFilterByAllChk()}"/>
</s:form>


<script type="text/javascript">

	 /* function showListForSelectedOption(){
		alert("showListForSelectedOption()--++++++++++");
		var filterByOption = Ext.get("filterByForm").dom.filterByOption;

		var radioBtnVal;
		for (var i = 0; i < filterByOption.length; i++) {
	        if (filterByOption[i].checked) {
	        	radioBtnVal = filterByOption[i].value;
	        	if(radioBtnVal!=null){
		            if(radioBtnVal=="1"){
		            	Ext.get("filterByForm").dom.filterByMyListChk.value=true;
		            	Ext.get("XPEDXMyItemsSelectedList").dom.filterByMyListChk.value='true';
		            	Ext.get("XPEDXMyItemsSelectedList").dom.filterBySelectedListChk.value='false';
				    	Ext.get("XPEDXMyItemsSelectedList").dom.submit();
				    }else if(radioBtnVal=="2"){
			    		Ext.get("XPEDXMyItemsSelectedList").dom.filterByMyListChk.value='false';
				    	Ext.get("XPEDXMyItemsSelectedList").dom.filterBySelectedListChk.value='true';
			    		//Ext.get("XPEDXMyItemsSelectedList").dom.submit();
				    	if (isUserAdmin) {
					    	$('#various5').trigger('click');
						}else{
							Ext.get("XPEDXMyItemsSelectedList").dom.customerIds.value='<s:property value="#SelectedCustomerId"/>';
			            	Ext.get("XPEDXMyItemsSelectedList").dom.submit();
						}
					}
	        	}
				break;
	        }
	    }
	} */
 
  function showListForSelectedOption(){
			    		Ext.get("XPEDXMyItemsSelectedList").dom.filterByMyListChk.value='false';
				    	Ext.get("XPEDXMyItemsSelectedList").dom.filterBySelectedListChk.value='true';
			    		if (isUserAdmin || isEstUser) {
					    	$('#various5').trigger('click');
						}else{
							Ext.get("XPEDXMyItemsSelectedList").dom.customerIds.value='<s:property value="#SelectedCustomerId"/>';
			            	Ext.get("XPEDXMyItemsSelectedList").dom.submit();
						}
				
	}

	function orderByLastModified(){
		//try{
			var currentOrderBy 	= Ext.get("orderByLastModified").dom;
			var orderByForm 	= Ext.get("orderByForm").dom;
			
			//Flip the values
			if (currentOrderBy.value == "desc"){
				currentOrderBy.value = "asc";
			} else {
				currentOrderBy.value = "desc";
			}
			
			orderByForm.filterByAccSel.value 	= Ext.get("filterByAccSel").dom.value;
			orderByForm.filterByShipToSel.value = Ext.get("filterByShipToSel").dom.value;
			
			Ext.get('orderByForm').dom.submit();
			
		//}catch(e){
		//}
	}

</script>
<!-- <table align="right">
<tr>
<td>
<label><a href="javascript:showListForSelectedOption();">Manage My Items List for Other Locations</a></label>		 
</td>
</tr>
</table>
 --> <div id="tool-bar-bottom" class="float-right">
  <!--<a class="orange-ui-btn modal"   id="various3" href="#dlgShareList" onclick="javascript:resetclFromListId();"><span>Create New List</span></a>   -->
  <a class="orange-ui-btn modal"   id="dlgShareListLinkHL3" name="dlgShareListLinkHL" href="#dlgShareListHL"><span>Create New List</span></a><br>
 
		</div>
 <div id="tool-bar-bottom" class="float-bottom">
  
 		</div>
 
  <!-- 2774 CR Start -->
 <s:else>
   <div id="tool-bar-bottom" class="float-right">
  <!--<a class="orange-ui-btn modal"   id="various3" href="#dlgShareList" onclick="javascript:resetclFromListId();"><span>Create New List</span></a>   -->
  <a class=""   id="" name="" href="">  
  <div id="Layer1" style="FONT-WEIGHT: bold; WIDTH: 239px; COLOR: #ff0000; HEIGHT: 19px"></div></a></div>
 </s:else><!-- 2774 CR End --> 
	
<div class="clearview">&nbsp;</div>

<div id="divMyItemLists">

<table id="mil-list" class="standard-table">
<swc:sortctl sortField='%{orderByAttribute}'  sortDirection='%{orderDesc}' down="Y" up="N"   urlSpec='%{#milListSortURL}'>
	<tbody>
		<tr id="none" class="table-header-bar ">
			<td class=" table-header-bar-left" >
			
			<swc:sortable fieldname="%{'ListName'}">
										<span class="white"> Name</span>
			</swc:sortable>
			
			</td>
			<%-- <td class=" sortable" align="center" width="105"><span class="white">Last Modified By</span></td>--%>			
			<td class="" align="center" style="width:250px;">
			<swc:sortable fieldname="%{'ModifyUserName'}">
				<span class="white">Last Modified By</span>
				</swc:sortable>
				</td>
			<td class="" align="center" style="width:150px;">
			<swc:sortable fieldname="%{'Modifyts'}">			
			<span class="white">Last Modified</span>
			</swc:sortable>
			</td>
		    
			<td class=" table-header-bar-right sorttable_nosort"  align="center" colspan="2"><span class="white"></span></td>
		</tr>
		<!-- CODE_START  Table - PN-->
		<s:set name="listModifiedByMap" value="getListModifiedByMap()" />
		<s:set name="listSizeMap" value="getListSizeMap()" />		
		<s:iterator status="status" id="item" value="listOfItems">
			<s:set name='id' value='#item.getAttribute("MyItemsListKey")' />
			<s:set name='name' value='#item.getAttribute("ListName")' />
			<s:set name='name2' value='#item.getAttribute("ListName").replace("\'", "\\\\\'")' />
			<s:set name='desc' value='#item.getAttribute("ListDesc")' />
			<s:set name='customerId' value='#item.getAttribute("CustomerID")' />
			<s:set name='CustomerContactID' value='#item.getAttribute("Modifyuserid")' />
			<s:set name='modifiedBy' value='%{#listModifiedByMap.get(#id)}' />
			<s:set name='lastMod' value='#item.getAttribute("Modifyts")' />
			<s:set name="childrenEle" value='XMLUtils.getChildElement(#item,"XPEDXMyItemsItemsList")' />
			<s:set name="sharePrivateFlag" value='#item.getAttribute("SharePrivate")'/>
			
			<%-- <s:set name="numOfItems" value='#childrenEle.getAttribute("TotalNumberOfRecords")' />
			 --%>
			 <%-- <s:set name="numOfItems" value='#item.getAttribute("NumberOFItems")' />
			  --%> 
			  <s:set name="numOfItems" value='%{#listSizeMap.get(#id)}' />
			 
			  <s:set name="spLevels" value='#item.getAttribute("SharePrivate")' />
			<s:set name="spShareAdminOnly" value='#item.getAttribute("ShareAdminOnly")' />
			<s:set name="listOwner" value='#item.getAttribute("ShareAdminOnly")' />
			
			<!-- <set name="uId" value="uniqueId" /> -->
			<s:set name="uId" value="#id" />

			<s:url id='deleteListLink' action='XPEDXMyItemsListDelete.action' includeParams="get">
				<s:param name="listKey" value="%{#id}" />
			</s:url>

			<s:form id="doAction_export_%{#uId}" action="XPEDXMyItemsDetails" method="post">
				<s:hidden name="listKey" value="%{#id}" />
				<s:hidden name="listName" value="%{#name}" />
				<s:hidden name="command" value="export_list" />
				<s:hidden name="itemCount" value="%{#numOfItems}"></s:hidden>
				<s:hidden name="editMode" value="%{false}"></s:hidden>
			</s:form>
			
			<s:form id="doAction_view_%{#uId}" action="XPEDXMyItemsDetails" method="get">
			<s:hidden name="listKey" value="%{#id}" />
			<s:hidden name="listName" value="%{#name}" />
			<s:hidden name="listDesc" value="%{#desc}" />
			<s:hidden name="itemCount" value="%{#numOfItems}" />
			<s:hidden name="shareAdminOnly" value="%{#spShareAdminOnly}"></s:hidden>
			
			<s:hidden name="modifyts" value="%{#_action.getModifyts()}"></s:hidden>
			<s:hidden name="modifyUserid" value="%{#_action.getModifyUserid()}"></s:hidden>
			<s:hidden name="createUserId" value="%{#_action.getCreateUserId()}"></s:hidden>
			
			<s:hidden name="filterBySelectedListChk" value="%{#_action.getFilterBySelectedListChk()}"/>
			<s:hidden name="filterByMyListChk" value="%{#_action.getFilterByMyListChk()}"/>
			<s:hidden name="filterByAllChk" value="%{#_action.getFilterByAllChk()}"/>
			<s:hidden name="filterBySharedLocations" value="%{#_action.getFilterBySharedLocations()}"/>
			<s:hidden name='sharePrivateField' value='%{#spLevels}' />
		</s:form>
		

			<s:form id="doAction_general_%{#uId}">
				<s:hidden name="listKey" value="%{#id}" />
				<s:hidden name="customerId" value="%{#CurrentCustomerId}"></s:hidden>
			</s:form>
			<s:form id="doAction_delete_item_list" action="XPEDXMyItemsListDelete" method="get">
				<s:hidden name="listKey" value="%{#id}" />
				<s:hidden name="filterByMyListChk" value="%{#parameters.filterByMyListChk}" />
				
				<s:hidden name="filterBySelectedListChk" value="%{#parameters.filterBySelectedListChk}"/>
				<s:hidden name="filterByAllChk" value="%{#parameters.filterByAllChk}"/>
				<s:hidden name="customerIds" value="%{#_action.getCustomerIds()}"/>
				<s:hidden name="customerPaths" value="%{#_action.getCustomerPaths()}"/>
				<s:hidden name="deleteClicked" value="%{true}"/>
			</s:form>
			<s:set name='isPrivateList' value="%{#_action.getFilterByMyListChk()}"/>
			<s:set name='isSelectedList' value="%{#_action.getFilterBySelectedListChk()}"/>
			<s:if test="(#isPrivateList)||(#isUserAdmin && #isSelectedList)">
				<s:set name='showAllActions' value="%{true}" />
			</s:if>


			<%-- <s:property value="%{'class=odd'}"/> --%>
			<tr class="<s:if test="%{!#status.isOdd()}">odd</s:if>
				<s:if test="#status.last" > last</s:if>">
				<td class="left-cell"><s:a  href="javascript:doAction('view', '%{#uId}'); ">
					<s:property value="#name" /> (<s:property value="#numOfItems" />)</s:a>
					<s:if test='%{#sharePrivateFlag.trim() != ""}'>
					<img id="whitecart" style="display:block;" alt="" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/mil/20x20_personal_list.png"/>
					</s:if>
					<s:else>
					<img id="whitecart" style="display:block;" alt="" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/mil/20x20_shared_list.png"/>
					</s:else>
					<!--  removed bold text and the word items -->	
				<p class="grey-mil" style="width:440px; word-wrap:break-word;"><s:property value="#desc" /></p>
				</td> 
				<%--For jira 4134 - sales rep Last Modified By display --%>
				
					<td class="createdby-lastmod"><s:property value="#modifiedBy" /></td>
				
				<%--Fix End For jira 4134 - sales rep Last Modified By display--%>
				<td class="createdby-lastmod"><s:property value='%{#util.formatDate(#lastMod, #wcContext, null, "MM/dd/yyyy")}' /></td>
				<s:if test="%{#spShareAdminOnly != ''}">
				<s:if test='%{#isUserAdmin == false && #spShareAdminOnly == "Y"}'>
					<td class="actions right-cell">
						<select class="xpedx_select_sm" onchange="doAction(this.value, '<s:property value="#uId"/>', '<s:property value="#id"/>', '<s:property value="#name2"/>', '<s:property value="#numOfItems"/>'); this.selectedIndex = 0;">
							<option value="select" selected="selected">- Select Action -</option>
							<option value="view">Open List</option>
							<option value="export">Export List</option>
							<option value="copy">Copy List</option>
						</select>
					</td>
				</s:if>
				<s:else>
					<td class="actions right-cell">
						<select class="xpedx_select_sm" onchange="doAction(this.value, '<s:property value="#uId"/>', '<s:property value="#id"/>', '<s:property value="#name2"/>', '<s:property value="#numOfItems"/>'); this.selectedIndex = 0;">
							<option value="select" selected="selected">- Select Action -</option>
							<option value="view">Open List</option>
							<option value="export">Export List</option>
							<option value="import">Import New Items</option>
							<option value="copy">Copy List</option>
							<option value="delete">Delete List</option>
						</select>
					</td>
				</s:else>
				</s:if>
				<s:else>
					<td class="actions right-cell">
						<select class="xpedx_select_sm" onchange="doAction(this.value, '<s:property value="#uId"/>', '<s:property value="#id"/>', '<s:property value="#name2"/>', '<s:property value="#numOfItems"/>'); this.selectedIndex = 0;">
							<option value="select" selected="selected">- Select Action -</option>
							<option value="view">Open List</option>
							<option value="export">Export List</option>
							<option value="import">Import New Items</option>
							<option value="copy">Copy List</option>
							<option value="delete">Delete List</option>
						</select>
					</td>
				</s:else>
			</tr>
		</s:iterator>
		<!-- CODE_END  Table - PN-->

	</tbody>
	</swc:sortctl>	
</table>
</div>

<!-- <div id="table-bottom-bar">
<div id="table-bottom-bar-L"></div>
<div id="table-bottom-bar-R"></div>
</div> -->

<div style="display:none;">
<a class=" modal"   id="various3" name="dlgShareListLinkHL" href="#dlgShareListHL">&nbsp;</a>
 </div>
 <br/>
 <div class="search-pagination-bottom">
                   <s:if test="%{totalNumberOfPages>1}">Page</s:if>&nbsp;&nbsp;<xpedx:pagectl currentPage="%{pageNumber}" lastPage="%{totalNumberOfPages}" showFirstAndLast="False"
                 	urlSpec="%{#orderListPaginationURL}" isAjax="false"/>
			</div>
 
<div id="tool-bar-bottom" class="float-right"> 
 
<a class="orange-ui-btn modal"   id="dlgShareListLinkHL3" name="dlgShareListLinkHL" href="#dlgShareListHL"><span>Create New List</span></a>
<%-- <div class="search-pagination-bottom">
                   <s:if test="%{totalNumberOfPages>1}">Page</s:if>&nbsp;&nbsp;<xpedx:pagectl currentPage="%{pageNumber}" lastPage="%{totalNumberOfPages}" showFirstAndLast="False"
                 	urlSpec="%{#orderListPaginationURL}" isAjax="false"/>
			</div>
 --%>
 </div>
<%-- 
<!-- Light Box -->
<div style="display: none;">

<div id="inline1" class="xpedx-light-box">
<table id="lightbox-table">
	<tbody>
		<tr id="none" class="table-header-bar">
			<td class="no-border table-header-bar-left" width="350"><span class="white">Alternate Items</span></td>
			<td class="no-border" align="center"><span class="white">Quantity</span></td>
			<td class="no-border" align="center"><span class="white">UOM</span></td>

			<td class="no-border" align="center"><span class="white">Availability</span></td>
			<td class="no-border-right table-header-bar-right" align="center" colspan="2"><span class="white">My
			Price</span></td>
		</tr>
		<tr>
			<td valign="top">
			<div style="float: left; margin: 2px 20px 0 0; height: 40px;"><input type="radio" name="none" /></div>
			<div>
			<p class="grey-mil">Jiffy Padded Mailers, 4" W x 8" L, Size 000, Gold, Satin, Kraft Paper with Paper Fiber Inner
			Batting, Padded (500 Each/Case) <s:property value="wCContext.storefrontId" /> # 23453241</p>
			</div>

			</td>
			<td valign="top"><input title="QTY" tabindex="12" id="QTY" value="2" class="input-label" name="QTY" /></td>
			<td valign="top" class="createdby-lastmod"><select name="select" class="xpedx_select_sm">
				<option selected="selected" value="5">Carton (500)</option>
				<option value="8">Case (24)</option>

			</select></td>
			<td valign="top" align="center">
			<div id="table-avail-col">
			<ul>
				<li><b>Availability</b></li>
				<li><s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDivisionName()"/>:</li>
				<li>Next Day:</li>

				<li>2+ Days:</li>
				<li>Total</li>
			</ul>
			<ul>
				<li><b>Carton (500)</b></li>
				<li>0</li>
				<li>15</li>

				<li>12</li>
				<li>27</li>
			</ul>
			</div>
			</td>
			<td valign="top">$72.27</td>
		</tr>

		<tr>
			<td valign="top">
			<div style="float: left; margin: 2px 20px 0 0; height: 40px;"><input type="radio" name="none" /></div>
			<div>
			<p class="grey-mil">Jiffy Padded Mailers, 4" W x 8" L, Size 000, Gold, Satin, Kraft Paper with Paper Fiber Inner
			Batting, Padded (500 Each/Case) <s:property value="wCContext.storefrontId" /> # 23453241</p>
			</div>
			</td>
			<td valign="top"><input title="QTY" tabindex="12" id="QTY" value="2" class="input-label" name="QTY" /></td>

			<td valign="top" class="createdby-lastmod"><select name="select" class="xpedx_select_sm">
				<option selected="selected" value="5">Carton (500)</option>
				<option value="8">Case (24)</option>
			</select></td>
			<td valign="top" align="center">
			<div id="table-avail-col">

			<ul>
				<li><b>Availability</b></li>
				<li><s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getDivisionName()"/>:</li>
				<li>Next Day:</li>
				<li>2+ Days:</li>
				<li>Total</li>

			</ul>
			<ul>
				<li><b>Carton (500)</b></li>
				<li>0</li>
				<li>15</li>
				<li>12</li>
				<li>27</li>

			</ul>
			</div>
			</td>
			<td valign="top">$72.27</td>
		</tr>
	</tbody>
</table>
<div id="table-bottom-bar">

<div id="table-bottom-bar-L"></div>
<div id="table-bottom-bar-R"></div>
</div>
<ul id="tool-bar" class="tool-bar-bottom">
	<li><a class="grey-ui-btn" href="#"><span>Cancel</span></a></li>
	<li><select name="select" class="xpedx_select_sm">
		<option selected="selected" value="5">Select Action</option>

		<option value="8">Add Selected Item to Peet's Cart</option>
		<option value="8">Add Selected Item to Mike's List</option>
		<option value="8">Replace item in Peet's Cart with Selected Item</option>
		<option value="8">Replace item in Mike's Cart with Selected Item</option>
	</select></li>
	<li style="float: right;"><a href="#"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/ui-buttons/ui-btn-save.gif" width="49"
		height="23" alt="Save" title="Save" /></a></li>

</ul>s
</div>
</div> --%>
<!-- End LightBox -->

</div>
<!-- End mid-col-mil --> <!-- Min Height Hack --> 
<!-- Removed extra line breaks -->


<!-- Min Height Hack --></div>
</div>
</div>
</div>
</div>
<!-- end main  -->

<!-- CODE_START Footer -PN -->
<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
<!-- CODE_END Footer -PN -->

</div>
<!-- end container  -->
<script>
defaultSortColumn=2;
isFirstDesc=true;
</script>

<s:include value="../modals/XPEDXItemDeleteModal.jsp"></s:include>
<s:include value="../modals/XPEDXItemsListImportModal.jsp" />


<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/swc.js"></script>
<script type="text/javascript" src="../../xpedx/ster/js/common/ajaxValidation.js"></script>
--><!-- sterling 9.0 base  do not edit  javascript move all functions to js/global-xpedx-functions.js -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/modals/checkboxtree/jquery.checkboxtree.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add.js"></script>
-->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/DD_roundies_0.0.2a-min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pseudofocus.js"></script>

--><!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global-xpedx-functions.js"></script>
-->
 
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.1.js"></script>
-->
<!--<script type="text/javascript" src="../js/jquery-ui-1/development-bundle/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="../js/jquery-ui-1/development-bundle/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="../js/jquery-ui-1/development-bundle/ui/jquery.ui.accordion.js"></script>-->
<%--
<link type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/themes/base/jquery.ui.all.css" rel="stylesheet" />
 --%>
<!--<script type="text/javascript" src="../js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
<script type="text/javascript" src="../js/fancybox/jquery.fancybox-1.3.4.js"></script>-->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/sorttable<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jqdialog/jqdialog<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
</body>
</html>
