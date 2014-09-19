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
	
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/MIL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<!--[if IE]> 
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-ie-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" /> 
	<![endif]--> 
	<!--[if IE]>
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
	<![endif]-->
	
	<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
	
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-jquery-headder<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	
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
					Ext.get("smilTitle_2").dom.innerHTML = "Manage My Items Lists for Selected Locations";
				},
				'onClosed' : function(){
					var div = document.getElementById("dynamiccontent");
					div.style.display = "block";
					shareSelectAll(false);
				},
				'autoDimensions'	: false,
				'width' 			: 748,
				'height' 			: 272 
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
			var dlgForm = document.getElementById("dynamiccontent");
			if (dlgForm) {
				dlgForm.style.display = "block";
			}
		}
	
		function showSelectedListForm(){
			var dlgForm = document.getElementById("dynamicContentInSelectedList");
			if (dlgForm) {
				dlgForm.style.display = "block";
			}
		}
		
		function deleteItemList(){
			doAction_delete_item_list.dom.submit();
		}
			
	</script>
	
	<script type="text/javascript">
		
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
			var dlgForm = document.getElementById("dlg" + formId);
			
			if (dlgForm){
				dlgForm.style.display = "block";
			}
		}
		
		function hideForm(formId){
			var dlgForm = document.getElementById("dlg" + formId);
			
			if (dlgForm){
				dlgForm.style.display = "none";
			}
		}
		
		function editForm(formId, listKey, name, desc, customerId){
			//Populate fields
			var editForm = document.getElementById("formEdit");
			
			editForm.listKey.value 		= listKey;
			editForm.name.value 		= name;
			editForm.desc.value 		= desc;
			editForm.customerId.value 	= customerId;

			showForm(formId);
		}

		function showShareList(customerId, showRoot, clFromListId){
			//Populate fields
			var divMainId = "divMainShareList";
			var divMain = document.getElementById(divMainId);
			
			//Load the list if it has not been loaded before.
			getShareList(customerId, divMainId, showRoot);
			isShareListLoaded = true;
			
			if (clFromListId != undefined || clFromListId != null){
				var x = Ext.get("clFromListId");
				x.dom.value = clFromListId;
			}
		}

		function showSelectedList(customerId, showRoot, clFromListId){
			//Populate fields
			var divMainId 	= "divMainSelectedList";
			divMainId.innerHTML = '';
			var divMain 	= document.getElementById(divMainId);
			
			//Load the list if it has not been loaded before.
			getShareList(customerId,"", divMainId, showRoot);
			isShareListLoaded = true;
			
			if (clFromListId != undefined || clFromListId != null){
				var x = Ext.get("clFromListId");
				x.dom.value = clFromListId;
			}
		}
		
		String.prototype.trim = function() {
			return this.replace(/^\s+|\s+$/g,"");
		}

		function submitNewlistNew(docDivId, ignoreDivIds){
			try{
				if (mandatoryFieldValidation(docDivId, ignoreDivIds) != "") {
					return;
				}
			}catch(err){
			}
			
			submitNewlist();
		}
				
		function submitNewlist(){
			var form = Ext.get("XPEDXMyItemsDetailsChangeShareList");
			
			form.dom.submit();
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
			
			//Validate form
			try{
				var clFromListIdVar = form.dom.clFromListId;
				clFromListIdVar.value=form.dom.clFromListIdTemp.value;
			}catch(err){
			}
			
		    form.dom.submit();
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
		}
		
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
	
	<style type="text/css">
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
	
	<title>
		<s:property value="wCContext.storefrontId" /> - <s:text name='MSG.SWC.MIL.MYITEMLISTS.GENERIC.TABTITLE' />
	</title>
</head>

<s:url id="orderListPaginationURL" action="MyItemsList">
	<s:param name="pageNumber" value="'{0}'"/>
	<s:param name="pageSetToken" value="%{pageSetToken}"/>
	<s:param name="orderByAttribute" value="orderByAttribute"/>
	<s:param name="orderDesc" value="orderDesc"/>
	<s:param name="customerIds" value="customerIds"/>
	<s:param name="customerPaths" value="customerPaths"/>
</s:url>
<s:url id="milListSortURL" action="MyItemsList">
	<s:param name="orderByAttribute" value="'{0}'"/>
	<s:param name="pageNumber" value="%{pageNumber}"/>
	<s:param name="orderDesc" value="'{1}'"/>
	<s:param name="customerIds" value="customerIds"/>
	<s:param name="customerPaths" value="customerPaths"/>
</s:url>

<s:set name='wcContext' value="wCContext" />
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />

<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs?
   	This is to avoid a defect in Struts that's creating contention under load.
   	The explicit call style will also help the performance in evaluating Struts? OGNL statements.
--%>
<s:set name='_action' value='[0]' />

<s:set name="xutil" value="XMLUtils" />
<s:set name='categoryListElem' value="categoryListElement" />
<s:set name='childCategoryListElem' value="childCategoryListElement" />
<s:set name='fieldListElem' value="searchableIndexFieldListOutPutElement" />
<s:set name='outDoc2' value='%{outDoc.documentElement}' />

<body class="ext-gecko ext-gecko3">

	<%-- fancybox workaround: fancybox only works with 'a' tag so programatically click it when the button is clicked --%>
	<a style="display: none;" id="dlgShareListLinkHL" href="#dlgShareListHL" />
	<a style="display: none;" id="various3" href="#dlgShareListHL" />
	<a style="display: none;" id="various5" href="#dlgSelectedList" />
	
	<s:set name="isUserAdmin" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@isCurrentUserAdmin(wCContext)" />
	<s:set name="isEstUser" value='%{#xpedxCustomerContactInfoBean.isEstimator()}' />
	<script type="text/javascript">
		var isUserAdmin = <s:property value="#isUserAdmin"/>;
		var isEstUser = <s:property value="#isEstUser"/>;
	</script>
	
	<div style="display: none;">
		<div id="dlgShareList" class="share-modal xpedx-light-box">
			<s:hidden id="mandatoryFieldCheckFlag_dlgShareList" name="mandatoryFieldCheckFlag_dlgShareList" value="%{false}" />
			<h2 id="smilTitle">Share My Items List</h2>
			<br/>
			
			<s:form id="XPEDXMyItemsDetailsChangeShareList" action="MyItemsDetailsChangeShareList" method="post" enctype="multipart/form-data">
			
				<div class="error" id="errorMsgForMandatoryFields_dlgShareList" style="display : none"></div>
				
				<p>
					<strong>Name</strong>&nbsp;&nbsp;<input type="text" name="listName" id="listName" value="" maxlength="255" onkeyup="javascript:listNameCheck(this, 'dlgShareList');" onmouseover="javascript:listNameCheck(this, 'dlgShareList');"/>
				</p>
				<p style="word-wrap:break-word; width: 50px;">
					<strong>Description</strong>&nbsp;&nbsp;<input type="text" name="listDesc" id="listDesc" value="" maxlength="30"/>
				</p>
				
				<s:set name="isSalesRep" value ="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute('IS_SALES_REP')}"/>
				
				<s:hidden name="listKey" value="new" />
				<s:hidden name="editMode" value="%{true}" />
				<s:hidden name="itemCount" value="%{0}" />
				<s:hidden id="clFromListId" name="clFromListId" value="" />
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
				
				<s:iterator id="item" value='savedSharedList'>
					<s:set name='customerID' value='#item.getAttribute("CustomerID")' />
					<s:set name='customerPath' value='#item.getAttribute("CustomerPath")' />
					
					<s:hidden name="sslNames" value="%{#customerID}" />
					<s:hidden name="sslValues" value="%{#customerPath}" />
				</s:iterator>
			
				<p>
					<strong> This list is: 
						<input onclick="hideSharedListForm()" 
							id="rbPermissionPrivate" <s:property value="#rbPermissionPrivate"/>
							type="radio" name="sharePermissionLevel" 
							value="<s:property value="wCContext.loggedInUserId"/>" 
						/> 
						Private &nbsp;&nbsp; 
						
						<s:if test="%{#isUserAdmin}">
							<input
								onclick="showSharedListForm()" 
								id="rbPermissionShared" 
								<s:property value="#rbPermissionShared"/>
								type="radio"
								name="sharePermissionLevel" 
								value=" "
							/> 
							Shared
						</s:if>
					</strong>
				</p>
				
				<div style="<s:property value="%{#isUserAdmin ? '' : 'display:none;'}"/>" id="dynamiccontent" >
					<s:div id="dlgShareListShared" >
						<input type="checkbox" <s:property value="#saCV"/> name="shareAdminOnly" value="Y" />
						Edit by Admin users only
						<br />
						<br />
						
						<a href="javascript:shareSelectAll(true)">Select All</a>
						<a href="javascript:shareSelectAll(false)">Deselect All</a>
						
						<s:div id="divMainShareList" cssClass="grey-msg x-corners">
						</s:div>
					</s:div>
				</div> <%-- / dynamiccontent --%>
			
				<script type="text/javascript">
					function submitSL(docDivId, ignoreDivIds){
						submitNewlistNew(docDivId, ignoreDivIds);
					} 
				</script>
			
				<br/>
				<br/>
				<ul id="tool-bar float-right" class="tool-bar-bottom" style="float:right; padding-top:5px; width:152px; margin-right:5px;">
					<li>
						<a class="grey-ui-btn" href="javascript:$.fancybox.close(); resetclFromListId() ;">
							<span>Cancel</span>
						</a>
					</li>
					<li style="float: right;">
						<a href="javascript:submitSL('dlgShareList', []);" >
							<img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/ui-buttons/ui-btn-save<s:property value='#wcUtil.xpedxBuildKey' />.gif" width="49" height="23" alt="Save" title="Save" />
						</a>
					</li>
				</ul>
			
			</s:form>
		</div>
	
		<s:include value="../modals/XPEDXItemsForLocationModal.jsp" />
		
		<div id="dlgNewForm" class="dlgForm">
			<h3>New List</h3>
			<s:form action="XPEDXMyItemsListCreate" method="post">
				<s:textfield label="Name" name="name"></s:textfield>
				<s:textfield label="Desc" name="desc"></s:textfield>
				<s:hidden name="customerId" value="1" />
				<s:hidden name="sharePrivate" value="#wcContext.loggedInUserId" />
			
				<br/>
				<br/>
			
				<s:submit value="Create New List" align="center"></s:submit>
				&nbsp;&nbsp;
				<input type="button" value="Cancel" onclick="javascript:resetclFromListId();hideForm('NewForm')" />
			</s:form>
		</div> <%-- / dlgNewForm --%>
		
		<a id="dlgImportItemsLink" href="#dlgImportForm"></a>
		
		<div id="dlgEditForm" class="dlgForm">
			<h3>Edit Record</h3>
			<s:form id="formEdit" action="XPEDXMyItemsListChange" method="post">
				<s:textfield label="Name" name="name"></s:textfield>
				<s:textfield label="Desc" name="desc"></s:textfield>
				<s:hidden name="listKey" value="" />
				<s:hidden name="customerId" value="1" />
			
				<br/>
				<br/>
			
				<s:submit value="Save" align="center"></s:submit>
				&nbsp;&nbsp;
				<input type="button" value="Cancel" onclick="hideForm('EditForm')" />
			</s:form>
		</div> <%-- / dlgEditForm --%>
	</div> <%-- / empty hidden div for share list modal --%>
	
	<div id="main-container">
		<div id="main">
		
			<s:action name="xpedxHeader" executeResult="true" namespace="/common" >
			   <s:param name='shipToBanner' value="%{'true'}" />
			</s:action>
			
			<div class="container content-container">
				<h1><s:text name='MSG.SWC.MIL.MYITEMLISTS.GENERIC.PGTITLE' /></h1>
				
				<div class="mil-lists-legend">
					<div class="mil-lists-personal">Personal</div>
					<div class="mil-lists-shared">Shared</div>
					
					<s:if test="#isUserAdmin || #isEstUser">
						<div class="mil-manage-lists">
							<a class="underlink" id="a2" name="" href="javascript:showListForSelectedOption();">Manage My Items Lists for Other Locations</a>
						</div>
					</s:if>
				</div>
				
				<div class="mil-lists-toolbar" style="margin-right:0">
					<input name="button" type="button" class="btn-gradient floatright" value="Create New List" onclick="$('#dlgShareListLinkHL').click(); return false;" />
					
					<xpedx:flexpagectl currentPage="%{pageNumber}" lastPage="%{totalNumberOfPages}" urlSpec="%{#orderListPaginationURL}" isAjax="false" />
				</div>
				<div class="clearfix"></div>
				<table id="mil-list" class="standard-table addmargintop5" >
					<swc:sortctl sortField='%{orderByAttribute}' sortDirection='%{orderDesc}' down="Y" up="N" urlSpec='%{#milListSortURL}'>
						
							<tr id="none">
								<th class="width-460">
									<swc:sortable fieldname="%{'ListName'}">
										<span class="white"> Name</span>
									</swc:sortable>
								</th>
								<th class="width-140">
									<swc:sortable fieldname="%{'ModifyUserName'}">
										<span class="white">Last Modified By</span>
									</swc:sortable>
								</th>
								<th class="width-140">
									<swc:sortable fieldname="%{'Modifyts'}">			
										<span class="white">Last Modified</span>
									</swc:sortable>
								</th>
								<th>
									&nbsp;
								</th>
							</tr>
							<tbody>
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
								
								<s:set name="numOfItems" value='%{#listSizeMap.get(#id)}' />
								 
								<s:set name="spLevels" value='#item.getAttribute("SharePrivate")' />
								<s:set name="spShareAdminOnly" value='#item.getAttribute("ShareAdminOnly")' />
								<s:set name="listOwner" value='#item.getAttribute("ShareAdminOnly")' />
								
								<s:set name="uId" value="#id" />
					
								<s:url id='deleteListLink' action='XPEDXMyItemsListDelete.action' includeParams="get">
									<s:param name="listKey" value="%{#id}" />
								</s:url>
					
								<s:form id="doAction_edit_%{#uId}" action="MyItemsDetails" method="post">
									<s:hidden name="listKey" value="%{#id}" />
									<s:hidden name="listName" value="%{#name}" />
									<s:hidden name="listDesc" value="%{#desc}" />
									<s:hidden name="command" value="edit_list" />
									<s:hidden name="itemCount" value="%{#numOfItems}" />
									<s:hidden name="editMode" value="%{true}" />
									<s:hidden name="shareAdminOnly" value="%{#spShareAdminOnly}" />
									<s:hidden name='sharePrivateField' value='%{#spLevels}' />
								</s:form>
								
								<s:form id="doAction_export_%{#uId}" action="MyItemsDetails" method="post">
									<s:hidden name="listKey" value="%{#id}" />
									<s:hidden name="listName" value="%{#name}" />
									<s:hidden name="command" value="export_list" />
									<s:hidden name="itemCount" value="%{#numOfItems}" />
									<s:hidden name="editMode" value="%{false}" />
								</s:form>
								
								<s:form id="doAction_view_%{#uId}" action="MyItemsDetails" method="get">
									<s:hidden name="listKey" value="%{#id}" />
									<s:hidden name="listName" value="%{#name}" />
									<s:hidden name="listDesc" value="%{#desc}" />
									<s:hidden name="itemCount" value="%{#numOfItems}" />
									<s:hidden name="shareAdminOnly" value="%{#spShareAdminOnly}" />
									
									<s:hidden name="modifyts" value="%{#_action.getModifyts()}" />
									<s:hidden name="modifyUserid" value="%{#_action.getModifyUserid()}" />
									<s:hidden name="createUserId" value="%{#_action.getCreateUserId()}" />
									
									<s:hidden name="filterBySelectedListChk" value="%{#_action.getFilterBySelectedListChk()}"/>
									<s:hidden name="filterByMyListChk" value="%{#_action.getFilterByMyListChk()}"/>
									<s:hidden name="filterByAllChk" value="%{#_action.getFilterByAllChk()}"/>
									<s:hidden name="filterBySharedLocations" value="%{#_action.getFilterBySharedLocations()}"/>
									<s:hidden name='sharePrivateField' value='%{#spLevels}' />
								</s:form>
							
					
								<s:form id="doAction_general_%{#uId}">
									<s:hidden name="listKey" value="%{#id}" />
									<s:hidden name="customerId" value="%{#CurrentCustomerId}" />
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
								<s:if test="(#isPrivateList) || (#isUserAdmin && #isSelectedList)">
									<s:set name='showAllActions' value="%{true}" />
								</s:if>
					
								<tr class="<s:if test="%{!#status.isOdd()}">odd</s:if>
									<s:if test="#status.last" > last</s:if>">
									<td class="left-cell">
										<s:if test='%{#sharePrivateFlag.trim() != ""}'>
											<img id="whitecart" class="mil-list-row-icon float-left" alt="" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/mil/20x20_personal_list.png"/>
										</s:if>
										<s:else>
											<img id="whitecart" class="mil-list-row-icon float-left" alt="" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/mil/20x20_shared_list.png"/>
										</s:else>
										<s:a  href="javascript:doAction('view', '%{#uId}'); ">
											<s:property value="#name" /> (<s:property value="#numOfItems" />)
										</s:a>
										<p class="grey-mil"><s:property value="#desc" /></p>
									</td> 
									<td class="createdby-lastmod">
										<s:property value="#modifiedBy" />
									</td>
									<td class="createdby-lastmod">
										<s:property value='%{#util.formatDate(#lastMod, #wcContext, null, "MM/dd/yyyy")}' />
									</td>
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
												<option value="edit">Edit List</option>
												<option value="export">Export List</option>
												<option value="import">Import New Items</option>
												<option value="copy">Copy List</option>
												<option value="delete">Delete List</option>
											</select>
										</td>
									</s:else>
								</tr>
							</s:iterator>
						</tbody>
					</swc:sortctl>	
				</table>
				
				<div class="mil-lists-toolbar" style="margin-right:0">
					<xpedx:flexpagectl currentPage="%{pageNumber}" lastPage="%{totalNumberOfPages}" urlSpec="%{#orderListPaginationURL}" isAjax="false" />
					<input name="button" type="button" class="btn-gradient floatright addmargintop10" value="Create New List" onclick="$('#dlgShareListLinkHL').click(); return false;" />
				</div>
				
			</div> <%-- / container mil-list --%>
		</div> <%-- / main --%>
	</div> <%-- / main-container --%>
	
	<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
	
	<script>
		defaultSortColumn = 2;
		isFirstDesc = true;
	</script>
	
	<s:include value="../modals/XPEDXItemDeleteModal.jsp" />
	<s:include value="../modals/XPEDXItemsListImportModal.jsp" />
	
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	
	<script type="text/javascript" src="../js/fancybox/jquery.fancybox-1.3.4.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/sorttable<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jqdialog/jqdialog<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
</body>
</html>
