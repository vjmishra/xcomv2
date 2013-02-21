<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean'
	id='util' />
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>




<!-- styles -->
<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-1<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/swc.min<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/home/home<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/home/portalhome<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/catalog/narrowBy<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/catalog/catalogExt<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/styles<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.css" />

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx-dan<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/themes/base/jquery.ui.all<s:property value='#wcUtil.xpedxBuildKey' />.css" rel="stylesheet" />
<s:include value="../../common/XPEDXStaticInclude.jsp"/>

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/prod-details<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx-mil<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx-mil-new<s:property value='#wcUtil.xpedxBuildKey' />.css" />


<!-- javascript -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
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

<!-- Facy Box (Lightbox/Modal Window -->

<!-- Facy Box (Lightbox/Modal Window -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />

<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/css/smoothness/jquery-ui-1.8.2.custom<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />

<script  type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/SpryTabbedPanels<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' /><s:property value='#wcUtil.staticFileLocation' />/xpedx/css/user/my-account<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/profile/profile<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/address/editableAddress<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/verifyAddress<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/user/user<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/js/modals/checkboxtree/jquery.checkboxtree<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>


 

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- Facy Box (expand and collpse Modal Window -->

 
<link rel="stylesheet" type="text/css" href="checkboxtree/demo.css"/>
<link rel="stylesheet" type="text/css" href="checkboxtree/jquery.checkboxtree.css"/>
<script type="text/javascript" src="checkboxtree/jquery.checkboxtree.js"></script>

<!-- Facy Box (expand and collpse Modal Window -->

<script type="text/javascript">
        //<!--
        //    $(document).ready(function() {
        //        $('#tree').checkboxTree();
        //        $('#collapseAllButtonsTree').checkboxTree({
        //            collapseAllButton: 'Collapse all',
        //            expandAllButton: 'Expand all'
        //        });
        //    });
        //-->
        </script>      
<script type="text/javascript">

    function getNewContactInfo(url){
	    document.userList.action = url;
	    document.userList.submit();
	}

	xyz = function(){
		expPhnPnl = '<s:property value="#expandPhonePanel" />';
		expAddPnl = '<s:property value="#expandAddressPanel" />';
	}

	/* STARTS - Customer-User Profile Changes - adsouza */

	
	
	function addEmailToList(){
		var newEmailAddr = document.getElementById("NewEmailAddr").value;
		var aEAL = document.getElementById("AddnlEmailAddrList");
		aEAL.options[aEAL.options.length] = new Option(newEmailAddr,newEmailAddr);
		
		document.getElementById("AddnlEmailAddrText").value = document.getElementById("AddnlEmailAddrText").value + newEmailAddr + ",";
		document.getElementById("NewEmailAddr").value = "";
				
	}

	function removeEmailFromList()
	{
		var i;
		var aEAL = document.getElementById("AddnlEmailAddrList");
		var listString = "";
		for(i=0;i<aEAL.options.length;i++)
		{	
			if(aEAL.options[i].selected)
				aEAL.remove(i);
			else{
				listString += aEAL.options[i].value + ","
			}	
		}
		document.getElementById("AddnlEmailAddrText").value = listString;
	}

	function showPOListCreateDialog(attr)
	{		
		document.getElementById("poListOperation").value = "addoperation";
		DialogPanel.show(attr);
		svg_classhandlers_decoratePage();
	}


	function addPOToList(){
		var newPO = document.getElementById("NewPO").value;
		var pol = document.getElementById("POList");
		pol.options[pol.options.length] = new Option(newPO,newPO);

		document.getElementById("POListText").value = document.getElementById("POListText").value + newPO + ",";


	}

	function removePOFromList()
	{
		var i;
		var newPO = document.getElementById("POList");
		var listString = "";
		for(i=newPO.options.length-1;i>=0;i--)
		{	
			if(newPO.options[i].selected)
				newPO.remove(i);
			else{
				listString += newPO.options[i].value + ","
			}
		}
		document.getElementById("POListText").value = listString;	
	}

	function deleteSelectedPO(rowSelected) {
		var selRow = document.getElementById(rowSelected);
		var tbody = document.getElementById(rowSelected).parentNode;
		var rowToDelete = document.getElementById(rowSelected);
		var rowCount = tbody.rows.length;
		removePOFromList();
			for(var i=rowCount; i>=1; i--) {
				var row = tbody.rows[i];
				if (null != row) {			
	    			if(null != rowSelected && rowSelected == row.id) {			    									
	    				tbody.deleteRow(i);
	    			}
				}         			
			}			
		}

	function addPOToListAndTable() {
		if (document.getElementById("poListOperation").value == "addoperation") {			
			var newPOValue = document.getElementById("NewPO").value;			
			addPOToList();
			DialogPanel.hide("addToPOList");
			document.getElementById("NewPO").value = "";
			
			var tbody = document.getElementById("poListTable").getElementsByTagName("tbody")[0];
			var rowCount = tbody.rows.length;
			var row = null;
			
			if (rowCount%2 == 0) {
				row = document.createElement("tr")
				row.setAttribute('id',newPOValue)
				row.setAttribute('class', 'odd');
			} else {
				row = document.createElement("tr")
				row.setAttribute('id',newPOValue);
			}
			
			var data1 = document.createElement("td")		
				
			data1.appendChild (document.createTextNode(newPOValue))			
			var data2 = null;
			
			if(rowCount == 1) {
				data2 = document.createElement("td")
				data2.setAttribute('width', '12%');
				data3 = document.createElement("td")
				data3.setAttribute('width', '17%');
			} else {
				data2 = document.createElement("td")
				data3 = document.createElement("td")
			}
			
			var editLink = document.createElement('a');				
			editLink.appendChild (document.createTextNode ('Edit'));
			editLink.setAttribute('href', 'javascript:editSelectedPO('+'"'+newPOValue+'"'+')');

			data2.appendChild(editLink);
			var deleteLink = document.createElement('a');
			deleteLink.appendChild (document.createTextNode ('Delete'));
			deleteLink.setAttribute('href', 'javascript:deleteSelectedPO('+'"'+newPOValue+'"'+')');
						
			data3.appendChild(deleteLink);        		
			row.appendChild(data1);
			row.appendChild(data2);
			row.appendChild(data3);
			tbody.appendChild(row);						
		}
	}

	function changeUserStatus(changeToStatus) {
		if(changeToStatus == "Activate") {
			document.getElementById('status').value='10';
			callSave();			
		}
		if(changeToStatus == "Suspend") {
			document.getElementById('status').value='20';
			callSave();			
		}
	}

	/* ENDS - Customer-User Profile Changes - adsouza */
</script>


<title><s:text name="admin.myprofile.title" /></title>



<link href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/SpryTabbedPanels<s:property value='#wcUtil.xpedxBuildKey' />.css' />" rel="stylesheet" type="text/css" />

 
</head>

<s:set name='_action' value='[0]'/>
<s:set name="AddressInformationTitle" scope="page" value="getText('Address_Information_Title')" />
<s:set name="confirmAddressDeleteMessage" scope="page" value="getText('confirmAddressDelete')" />
<s:set name="confirmAddressDuplicateMessage" scope="page" value="getText('confirmAddressDuplicate')" />
<s:set name='xmlUtil' value="XMLUtils" />
<s:set name='sdoc' value="#_action.getCustomer().getOwnerDocument()" />
<s:set name='customerelement' value='#_action.getCustomer()' />

<!-- STARTS - Customer-User Profile Changes - adsouza -->
<s:set name='extnElem' value='#util.getElement(#sdoc, "//Extn")' />
<!-- ENDS - Customer-User Profile Changes - adsouza -->

<s:set name='viewInvoices' value='%{getViewInvoices()}'/>
<s:set name='punchoutUser' value='%{getPunchoutUsers()}'/>
<s:set name='stockCheckWebservice' value='%{getStockCheckWebservice()}'/>
<s:set name='estimator' value='%{getEstimator()}'/>
<s:set name='customer' value='customerelement' />

<!-- Customer Additional Address -->

<s:set name="additionalAddressList" value='#util.getElement(#customer,"CustomerAdditionalAddressList")' />
<s:set name='addladdress' value='#util.getElements(#additionalAddressList,"CustomerAdditionalAddress")'  />

<!-- s:set name='customercontact' value='#util.getElement(#sdoc,"//Customer/CustomerContactList/CustomerContact")' /-->
<s:set name='customercontact' value='getContact()' />
<!-- Customer Contact Additional Address -->
<s:set name="additionalAddressList"
	value='#util.getElement(#customer,"CustomerAdditionalAddressList")' />
<s:set name='addladdress'
	value='#util.getElements(#additionalAddressList,"CustomerAdditionalAddress")' />

<s:set name='userelement' value="getUser()" />
<s:set name='user' value='#userelement' />
<s:set name='userExists' value='%{#xmlUtil.hasAttribute(#user,"Loginid")}' />
<s:if test='%{#userExists}'>
	<s:property value="User Exists" />
	<s:set name='loginid' value='%{#user.getAttribute("Loginid")}' />
	<s:set name='displayUserID' value='%{#user.getAttribute("DisplayUserID")}' />
	<s:set name='orgQuestionList' value='%{getQuestionListForOrg()}' />
	<s:set name='userSecretQuestion' value='%{getSecretQuestionForUser()}' />
	<s:set name='maskedPasswordString' value='%{getMaskedPasswordString()}' />
	<s:set name='maskedAnswerString' value='%{getMaskedSecretAnswerString()}' />
</s:if>
<s:set name='buyerOrgCode' value='%{getBuyerOrgCode()}' />
<s:set name='buyerOrgName' value='%{getBuyerOrgName()}' />
<s:set name='selfAdmin' value="isSelfAdmin()" />
<s:set name='showAdminList' value="isAdminListEnabled()" />

<s:set name='customerId' value='#customer.getAttribute("CustomerID")' />
<s:set name='customerContactId'
	value='#customercontact.getAttribute("CustomerContactID")' />
<s:set name='defShipToAddId' value='%{getDefShipToAddId()}' />
<s:set name='defBillToAddId' value='%{getDefBillToAddId()}' />
<s:set name='defSoldToAddId' value='%{getDefSoldToAddId()}' />
<s:set name='roles' value="getUserGroups()" />
<s:set name='hasAccessToEdit' value="%{getIsChildCustomer()}" />
<s:if test='%{#hasAccessToEdit == "TRUE"}'>
	<s:set name='isDisabled' value="%{'false'}" />
</s:if>
<s:else>
	<s:set name='isDisabled' value="%{'true'}" />
</s:else>
<s:url id='assignedShipToURL' namespace='/common'
	action='xpedxGetAssignedCustomers' />
<s:url id="xpedxGetCustomerLocations" action="xpedxGetCustomerLocations" namespace="/profile/user" />
	
<script type="text/javascript">
		
		function loadLocations(userId) {
			if(userId != "")
			{
				param={loggedInUserId : userId };
			}
			else{
				param={customerID : '<s:property value="wCContext.loggedInUserId" escape='false'/>'}
			}
			var loadedFalg = document.getElementById('LocationsLoaded').value;
			if(loadedFalg == 'false') {
				Ext.Msg.wait("Loading Locations.... Please Wait!!!");
        		document.getElementById('Locations').innerHTML = "";
				Ext.Ajax.request({
					
			        url: '<s:property value="#xpedxGetCustomerLocations" escape='false'/>',
			        params: param,
			        method: 'POST',
			        success: function (response, request){
					        document.getElementById('Locations').innerHTML = response.responseText;
					        Ext.MessageBox.hide();
			            },
			        failure: function (response, request){
			            	document.getElementById('Locations').innerHTML =
				            			"Failed to Load Locations for this particular User Try again Later";
			            		 Ext.MessageBox.hide();
			             }
			    });
			}
			
		}
		function showAssignedShipTos(billToID){
			var shipToDiv = document.getElementById(billToID);
			switch(shipToDiv.style.display){
			case 'none':
				shipToDiv.style.display = 'block';
				break;
			case 'block':
				shipToDiv.style.display = 'none';
				break;
			}
		}		
		function showShipTo(url)
		{
			Ext.Ajax.request({
		        url :url,
		        method: 'POST',
		        success: function (response, request){
				document.getElementById('viewShipToDlg').innerHTML = response.responseText;
	       	 	},
		    	failure: function (response, request){
	       	 		document.getElementById('viewShipToDlg').innerHTML = response.responseText;
		        }
		    });     
		}
		function showOrHideAll(flag) {
			if(flag == 'false') {
				var billToText = document.getElementById('billToListText').value;
				var billTosArray = billToText.split(',');
				for(var i=0; i<billTosArray.length; i++) {
					var shipToDiv = document.getElementById(billTosArray[i]);
					if(shipToDiv!= null)
						shipToDiv.style.display = 'none';
					}			
			}
			if(flag == 'true') {
				var billToText = document.getElementById('billToListText').value;
				var billTosArray = billToText.split(',');
				for(var i=0; i<billTosArray.length; i++) {
					var shipToDiv = document.getElementById(billTosArray[i]);
					if(shipToDiv!= null)
						shipToDiv.style.display = 'block';
					}
				}
			}
		
</script>
<script type="text/javascript">
	$(document).ready(function() {
		$(document).pngFix();		
		$("#changeShipTo").fancybox({
			'onStart'	:	function(){
			showShipTo('<s:property value="#assignedShipToURL"/>');
			},
			'showCloseButton'	: true,
	 		'enableEscapeButton': true,
	 		'autoDimensions'	: true,
	 		'width' 			: 700,
	 		'height' 			: 650  
			});
		$("#addNewQL").fancybox({
			'onStart'		:	function(){
			document.getElementById("operation").value = "addoperation";
			document.getElementById("linkName").value = "";
			document.getElementById("url").value = "";	
			},
			'titleShow'			: false,
			'transitionIn'		: 'fade',
			'transitionOut'		: 'fade',
			'titlePosition' : 'inside',
			'transitionIn' : 'none',
			'transitionOut' : 'none'
								
		});

$("#various1").fancybox({
	'autoDimensions'	: false,
	'width' 			: 300,
	'height' 			: 200  
});
$("#various3").fancybox();
$("#various4").fancybox();
$("#various5").fancybox();
$("#various6").fancybox();
$("#various7").fancybox();
$("#various8").fancybox();
$("#various9").fancybox();
$("#various10").fancybox();
$("#various11").fancybox();
$("#various12").fancybox();
$("#various13").fancybox();
$("#various14").fancybox();
$("#various15").fancybox();
$("#various16").fancybox();
$("#various17").fancybox();
$("#various18").fancybox();
$("#various19").fancybox();
$("#various20").fancybox();
$("#various21").fancybox();
$("#various22").fancybox({
	'autoDimensions'	: false,
	'width' 			: 300,
	'height' 			: 200  
	});
});

	
	
	
</script>
<!-- END swc:head -->
<body class="ext-gecko ext-gecko3">
<div id="main-container">
    <div id="main">
    	<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
    
    
                <div class="container">
                <!-- breadcrumb -->
                
                <div id="searchBreadcrumb">
	    			<a href="<s:url action="XPEDXAdminProfile" namespace="/profile/user" includeParams='none'/>"><s:text name="admin.title" /></a> / <span class="page-title"><s:text name="admin.myprofile.title" /></span> 
	    			<a href="javascript:window.print()"><span class="print-ico-xpedx">
	    			<img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/print-icon<s:property value='#wcUtil.xpedxBuildKey' />.gif" width="16" height="15" alt="Print Page" />Print Page</span></a>
   				</div>

                <div id="mid-col-mil">
				  <h2>Currently Editing:  <s:property value="%{#customercontact.getAttribute('CustomerContactID')}"/> 
				  <span style="padding:0px 15px;"><a id='selectusertomodify' href='#viewUsersDlg' class=" margin-right txt-lnk-sml-1" style="font-weight:normal">Change User</a></span>
				  <span class="edited">  Last Modified  By: <span class="italic"><s:property value="wCContext.customerContactId"/></span></span> 
				  </h2>
  <div class="clearview">&nbsp;</div>
                <div id="cart-actions" class="float-left">
          <ul id="cart-actions">
            <li><a href="javascript:changeUserStatus('Suspend');"  class="orange-ui-btn"><span>Suspend User</span></a></li> 
             <li><a href="javascript:changeUserStatus('Activate');" class="green-ui-btn"><span>Activate User</span></a></li> 
          </ul>
        </div>
          <div class="clearview">&nbsp;</div>
          <s:form id="myAccount" name="myAccount"	method="post" validate="true" action="xpedxSaveAdminUserInfo">
								<s:hidden name="customerId" value="%{customerId}" />
								<s:hidden name="customerContactId"
									value="%{#customercontact.getAttribute('CustomerContactID')}" />
								<s:hidden name="buyerOrgCode" value="%{#buyerOrgCode}" />
								<s:hidden name="#action.namespace" value="/profile/user" />
								<s:hidden id="actionName" name="#action.name" value="saveUserInfo" />
								<s:hidden name='status' id="status" value="%{getContactStatus()}" />
								<s:hidden name="orderConfirmationEmailFlag" value='%{isSendOrderConfEmail()}' />
								<s:hidden name="orderCancellationEmailFlag" value='%{isSendOrderCancelEmail()}' /> 
								<s:hidden name="orderShipmentEmailFlag" value='%{isSendOrderShipEmail()}'	/>
								<s:hidden name="backorderEmailFlag" value='%{isSendBackOrderEmail()}' />
								<s:hidden name="orderApprovalFlag" value='%{isSendOrdersForApproval()}' />
								<s:hidden name="b2bCatalogView" id="b2bCatalogView" value='%{getDefaultB2bCatalogView()}' />
								<s:hidden name="POListText" id="POListText"	value='%{#extnElem.getAttribute("ExtnPOList")}' />
								<s:hidden name="defaultShipTo" id="defaultShipTo"	value='%{#extnElem.getAttribute("ExtnDefaultShipTo")}' />
								<s:hidden name="AddnlEmailAddrText" id="AddnlEmailAddrText" value='%{#extnElem.getAttribute("ExtnAddnlEmailAddrs")}' />
								<s:hidden name='userExistsId' value='%{#userExists}' id='userExistsId' />
								<s:hidden name='selfAdminId' id="selfAdminId" value="%{isSelfAdmin()}" />
								<s:hidden name="PrefCatalog" id="PrefCatalog" value='%{getPrefCategory()}' />
                <div id="TabbedPanels1" class="TabbedPanels">
                  <ul class="TabbedPanelsTabGroup" style="margin-left:5px;">
                    <li class="TabbedPanelsTab" tabindex="0">General Information</li>
                    <li class="TabbedPanelsTab" tabindex="0" onclick="javascript: loadLocations('<s:property value="%{#customercontact.getAttribute('CustomerContactID')}"/>');">Locations</li>
                    <li class="TabbedPanelsTab" tabindex="0">Manage Site Settings</li>
                  </ul>
                  <div class="TabbedPanelsContentGroup">
                    <div class="TabbedPanelsContent">
                    <div id="requestform">
                    	
                    	<table width="60%" class="form" border="0" cellspacing="0" cellpadding="0">
                        		<tr>
                                    <td>&nbsp;&nbsp;<s:text name="RB_userName" />:</td>
                                    <td><s:if test='%{#userExists}'>
											<s:property value="%{#displayUserID}" />
											<s:hidden name="userName" value="%{#displayUserID}" />
											<s:hidden name="validateUserId" value="%{false}" />
										</s:if> 
										<s:else>
											<s:textfield tabindex="5" disabled="%{#isDisabled}" name="userName" cssClass="x-input" cssStyle="width: 130px;" />
											<s:hidden name="validateUserId" value="%{true}" />
										</s:else>
									</td>
                                </tr>
                                <tr>
                                	<td>&nbsp;&nbsp;Status:</td>
									<s:set name='userStatus' value="%{getContactStatus()}" />
                                	<td>
                                		<s:if test="#userStatus == '10'" >
                                			Active
                                		</s:if>
                                		<s:if test="#userStatus == '20'" >
                                			On Hold
                                		</s:if>
                                		<s:if test="#userStatus == '30'">
                                			Inactive
                                		</s:if>
                                		
                                	</td>
                                </tr>
                                <tr >
                                    <td>&nbsp;&nbsp;Last Login:</td>
                                    <td>                                   	
                                    		<s:property value="%{getExtnLastLoginDate()}" />                                   									
											
				    			</td>
                                </tr>
                                <tr >

                                    <td valign="top">&nbsp;&nbsp;User Type:</td>
                                    <td valign="top">
                               	 		<s:if test='%{isAdminListEnabled()}'>
											<s:set name="checkBoxDisable" value='%{true}' />
										</s:if>
										<s:else>
											<s:set name="checkBoxDisable" value='%{false}' />
										</s:else>
                                    
	                                    <label class="clearview">
	                                    <s:checkbox tabindex="75" name='test' fieldValue="test123" cssClass="margin-15"
											value="%{isInUserGroup('BUYER-USER')}" disabled="%{#userExists}" />Buyer</label>   
	                                    <s:if test='%{!((isAdminListEnabled()) && (!isInUserGroup("BUYER-APPROVER")))}'>
											<label class="clearview">
											<s:checkbox tabindex="85" name='buyerApprover' cssClass="margin-15"
													value='%{isInUserGroup("BUYER-APPROVER")}' fieldValue="true"
													disabled='%{#checkBoxDisable || #isDisabled}' />
												Approver</label> 
										</s:if>	                                    
	                                    
	                                    <s:if
											test='%{!((isAdminListEnabled()) && (!isInUserGroup("BUYER-ADMIN")))}'>
											<label class="clearview">
												<s:checkbox tabindex="80" name='buyerAdmin' cssClass="margin-15"
													fieldValue="true" value='%{isInUserGroup("BUYER-ADMIN")}'
													disabled='%{#checkBoxDisable || #isDisabled}' />
											Admin</label> 
										</s:if>
	                                    
	                                    <s:if test='%{#estimator=="T"}'>
	                                    	<label class="clearview">
											<s:checkbox tabindex="86" name='estimator' cssClass="margin-15"
													value='true' fieldValue="true" disabled='false' />
												Estimator</label> 
										</s:if>
										<s:else>
											<label class="clearview">
											<s:checkbox tabindex="86" name='estimator' cssClass="margin-15"
													value='false' fieldValue="true" disabled='false' />
												Estimator</label> 
										</s:else>
										
										<s:if test='%{#viewInvoices=="T"}'>
											<label class="clearview">
											<s:checkbox tabindex="85" name='viewInvoices' cssClass="margin-15"
													value='true' fieldValue="true" disabled='false' />
													View Invoices</label>		
										</s:if>
										<s:else>
											<label class="clearview">
						        			<s:checkbox tabindex="85" name='viewInvoices' cssClass="margin-15"
													value='false' fieldValue="true" disabled='false' />
												View Invoices</label>											
						      			</s:else>
						      			
	                                   <label class="clearview">
												<s:checkbox name="viewPrices" fieldValue="true" value="isViewPrices()"  cssClass="margin-15"/>
											View Prices</label>
										
										<s:if test='isViewPrices() == true'>
											<label class="clearview">
													<s:checkbox name="viewReports" fieldValue="true" value='isViewReports()' cssClass="margin-15"/>
												View Reports</label>
										</s:if>
										<s:else>
											<label class="clearview">
													<s:checkbox name="viewReports" fieldValue="true" value='isViewReports()' cssClass="margin-15"
														disabled="true" />
												View Reports</label>
										</s:else> 
                                   	</td>
                                </tr>
                                <s:if test="%{selfAdmin}">
                                <tr >
                                    <td>&nbsp;&nbsp;<s:text name="RB_password" />:</td>
                                    <td>&nbsp;&nbsp;<s:password tabindex="10" name="userpassword" id="userpassword"  cssClass="x-input" cssStyle="width: 130px;"
											value="%{#maskedPasswordString}" showPassword="true" />
                                   	</td>
                                </tr>
                                <tr >
                                    <td>&nbsp;&nbsp;<s:text name="RB_confirmPassword" />:</td>
                                    <td>&nbsp;&nbsp;<s:password tabindex="15" name="confirmpassword" id="confirmpassword" cssClass="x-input" cssStyle="width: 130px;"
                                    	value="%{#maskedPasswordString}" showPassword="true" />
                                    </td>
                                </tr>
                          		<s:if test="#orgQuestionList!=null">                                
		                            <tr>
										<td>&nbsp;&nbsp;<s:text
											name="RB_secretQuestion" />:</td>
										<td><s:select tabindex="20" name="secretQuestion" id="secretQuestion"
											emptyOption="true" value="%{#userSecretQuestion}"
											list="#orgQuestionList" cssClass="x-input" cssStyle="width: 130px;"
											onchange="javascript:document.myAccount.secretAnswer.value='';document.myAccount.confirmAnswer.value='';" /></td>
									</tr>
								</s:if> 
								<tr>
									<td>&nbsp;&nbsp;<s:text
										name="RB_secretAnswer" />:</td>
									<td><s:password tabindex="25" name='secretAnswer' id='secretAnswer' cssClass="x-input" cssStyle="width: 130px;"
										value="%{#maskedAnswerString}" showPassword="true" /></td>
								</tr>
								<tr>
									<td>&nbsp;&nbsp;<s:text
										name="RB_confirmAnswer" />:</td>
									<td><s:password tabindex="30" name='confirmAnswer' id='confirmAnswer' cssClass="x-input" cssStyle="width: 130px;"
										value="%{#maskedAnswerString}" showPassword="true" /></td>
								</tr> 
								</s:if>
                                <s:if test='#selfAdmin == false'>
									<s:url id="ResetPasswordURL" action="resetPassword">										
									</s:url>
									<tr>
										<s:if test='%{#hasAccessToEdit == "TRUE"}'>
										&nbsp;&nbsp;<td>Password:</td>
										<td><s:a tabindex="40" href="javascript:resetPassword();"
												cssClass="txt-lnk-sml-1">
												<s:text name="reset.Password" />
											</s:a></td>
										</s:if>
									</tr>
								</s:if>
								<tr style="display: none">
									<td class="boldText textAlignLeft">&nbsp;&nbsp;<s:text
										name="RB_preferredLocale" />:</td>
									<td><s:select tabindex="35" name="preferredLocale"
										emptyOption='%{!#userExists}' list="getLocaleList()"
										disabled="%{#isDisabled}"
										value='%{#user.getAttribute("Localecode")}' /></td>
								</tr> 
								<tr  style="display: none;">
									<td class="boldText textAlignLeft">&nbsp;&nbsp;<s:text name="RB_title" />:</td>
									<td><s:select tabindex="45" name="title"
										disabled="%{#isDisabled}" list="%{getTitle()}"
										value="%{#customercontact.getAttribute('Title')}" /></td>
								</tr>                           
								<tr >
                                    <td><span class="red">* </span><s:text name="RB_firstName" />:</td>
                                    <td><s:textfield tabindex="50" name="firstName" id="firstName" cssClass="x-input" cssStyle="width: 130px;"
											disabled="%{#isDisabled}" value='%{#customercontact.getAttribute("FirstName")}' />
                                    </td>
                                </tr>
                                 <tr >
                                    <td><span class="red">* </span><s:text name="RB_lastName" />:</td>
                                    <td><s:textfield tabindex="55" name="lastName" id="lastName" cssClass="x-input" cssStyle="width: 130px;"
											disabled="%{#isDisabled}" value='%{#customercontact.getAttribute("LastName")}' />
                                    </td>
                                </tr>
                                <tr style="display: none;">
									<td class="boldText textAlignLeft"><s:text name="RB_jobTitle" />:</td>
									<td><s:textfield tabindex="60" name="jobTitle"
										disabled="%{#isDisabled}"
										value='%{#customercontact.getAttribute("JobTitle")}' /></td>
								</tr>
                            	<tr >
                                    <td><span class="red">* </span><s:text name="RB_EmailID" />:</td>
                                    <td><s:textfield tabindex="65" name="emailId" id="emailId"  cssClass="x-input" cssStyle="width: 180px;"
											disabled="%{#isDisabled}"
											value='%{#customercontact.getAttribute("EmailID")}' />
                                    </td>
								</tr>
                            	<tr style="display: none;">
									<td class="boldText textAlignLeft"><s:text name="RB_deptName" />:</td>
									<td><s:textfield tabindex="70" name="deptName"
										disabled="%{#isDisabled}"
										value='%{#customercontact.getAttribute("Department")}' /></td>
								</tr>
								<s:set name="defaultAddressList" value="getDefaultContactAddressList()" />
								<s:set name="nonDefaultAddressList" value="getOtherContactAddressList()" />
								<s:if
									test='%{(#defaultAddressList !=null && #defaultAddressList.size() >0) || (#nonDefaultAddressList != null && #nonDefaultAddressList.size() >0)}'>
									<s:set name='listEmty' value="%{'TRUE'}" />
								</s:if>
								<s:if test='%{#hasAccessToEdit != "TRUE" || #listEmty == null}'>
									<s:set name='isButtonDisabled' value="%{'true'}" />
								</s:if> 
								<s:else>
									<s:set name='isButtonDisabled' value="%{'false'}" />
								</s:else>
								<s:set name='custContactAddtnlAddress' value="#_action.getCustContactAddtnlAddress()"/>
								<s:set name='personInfo' value='#xmlUtil.getChildElement(#custContactAddtnlAddress,"PersonInfo")'/>
								<s:hidden name='addtnlAddressID' value='%{#custContactAddtnlAddress.getAttribute("CustomerAdditionalAddressID")}'/>
                            	<tr>
                                    <td>&nbsp;&nbsp;&nbsp;Phone:</td>
                                    <td><s:textfield name='dayPhone_new' value='%{#personInfo.getAttribute("DayPhone")}' id='dayPhone_new' 
                                    	cssClass="x-input" cssStyle="width: 130px;"/></td>
                                </tr>
	                            <tr >
	                              <td>&nbsp;&nbsp;&nbsp;Fax:</td>
	                              <td><s:textfield name='dayFaxNo_new' value='%{#personInfo.getAttribute("DayFaxNo")}' id='dayFaxNo_new' 
                                    	cssClass="x-input" cssStyle="width: 130px;"/></td>
	                            </tr>
	                            <s:hidden name='personfInfoAddressID' value='%{#personInfo.getAttribute("AddressID")}'/>
	                            <s:hidden name='personfInfoKey' value='%{#personInfo.getAttribute("PersonInfoKey")}'/>
	                            <s:hidden name='isCommercialAddress' value="%{#personInfo.getAttribute('IsCommercialAddress')}" />
	                            <tr >
	                              <td>&nbsp;&nbsp;&nbsp;Address Line 1:</td>
	                              <td><s:textfield name='addressLine1_new' value='%{#personInfo.getAttribute("AddressLine1")}' id='addressLine1_new' 
                                    	cssClass="x-input" cssStyle="width: 180px;"/></td>
	                            </tr>
	                            <tr >
	                              <td>&nbsp;&nbsp;&nbsp;Address Line 2:</td>
	                              <td><s:textfield name='addressLine2_new' value='%{#personInfo.getAttribute("AddressLine2")}' id='addressLine2_new' 
                                    	cssClass="x-input" cssStyle="width: 180px;"/></td>
	                            </tr>
	                            <tr >
	                              <td>&nbsp;&nbsp;&nbsp;Address Line 3:</td>
	                              <td><s:textfield name='addressLine3_new' value='%{#personInfo.getAttribute("AddressLine3")}' id='addressLine3_new' 
                                    	cssClass="x-input" cssStyle="width: 180px;"/></td>
	                            </tr>
	                            <tr >
	                              <td>&nbsp;&nbsp;&nbsp;City:</td>
	                              <td><s:textfield name='city_new' value='%{#personInfo.getAttribute("City")}' id='city_new' 
                                    	cssClass="x-input" cssStyle="width: 130px;"/></td>
	                            </tr>
	                            <tr >
	                              <td>&nbsp;&nbsp;&nbsp;State / Province:</td>
	                              <td><s:textfield name="state_new" value="%{#personInfo.getAttribute('State')}" id="state_new" 
                                    	cssClass="x-input" cssStyle="width: 130px;"/></td>
	                            </tr>
	                            <tr >
	                              <td>&nbsp;&nbsp;&nbsp;Postal Code:</td>
	                              <td><s:textfield name='zipCode_new' value='%{#personInfo.getAttribute("ZipCode")}' id='zipCode_new' 
                                    	cssClass="x-input" cssStyle="width: 130px;"/></td>
	                            </tr>
                                <tr >
                                    <td>&nbsp;&nbsp;&nbsp;Country:</td>

                                    <td><s:textfield name='country_new' value='%{#personInfo.getAttribute("Country")}' id='country_new' 
                                    	cssClass="x-input" cssStyle="width: 130px;"/></td>
                                </tr>
                    </table></div></div>
                    <s:set name='defaultShipToId' value='%{#extnElem.getAttribute("ExtnDefaultShipTo")}' />
					<s:set name='shipTo' value='#wcUtil.getShipToAdress(#defaultShipToId,wCContext.storefrontId)'/>
                    <div class="TabbedPanelsContent">	
						<div>
								<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tabs">
									<s:if test="#shipTo != null">									
										<tr>
											<td width="15%" class="no-borders paddingtop0" valign="top">
												<s:text name="Default Ship-To:"></s:text>
												<br/>
												<a id='changeShipTo' href='#viewShipToDlg' class="txt-lnk-sml-1">Change Default</a>
											</td>
											<td valign="top" class="no-borders paddingtop0"><div class="float-right"><img height="19" border="0" width="19" alt="help" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"/></div>
												<s:property value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@formatBillToShipToCustomer(#defaultShipToId)" /><br/>
												<s:if test="%{#shipTo.getOrganizationName()!=''}">
													<s:property	value='#shipTo.getOrganizationName()'/><br/>
												</s:if>
												<s:iterator value="#shipTo.getAddressList()" id="adressLine">
													<s:property value="adressLine"/> &nbsp;			
												</s:iterator><br/>
												<s:property value ="#shipTo.getCity()" />,
												<s:property value ="#shipTo.getState()" /><br/>
												<s:property value ="#shipTo.getCountry()" />
												<s:property value ="#shipTo.getZipCode()" /><br/>
												<s:url id='targetURL' namespace='/common' action='xpedxGetAssignedCustomers' />												
											</td>
										</tr>														
									</s:if>
									<s:else>
										<tr>
											<td width="15%" class="no-borders paddingtop0" valign="top">
												<s:text name="Default Ship-To:"></s:text>
											</td>
											<td valign="top" class="no-borders paddingtop0"><div class="float-right"><img height="12" border="0" width="12" alt="help" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png"/></div>
													<s:text name="No Default Ship To is selected"></s:text><br/>
													<s:url id='targetURL' namespace='/common' action='xpedxGetAssignedCustomers' />
													<a id='changeShipTo' href='#viewShipToDlg' class="txt-lnk-sml-1">Change Default</a>
												</td>
										</tr>
									</s:else>
																
									<s:action name="AuthorizeCustomerAssignment" namespace="/profile/user"
										executeResult="true"/>				
									<tr>
				                    	<td valign="top" class="no-borders ">&nbsp;</td>
				                    	<td valign="top" class="no-borders ">&nbsp; </td>
				                  	</tr>
				        	</table>
				    	</div>
				    </div>
                    <div class="TabbedPanelsContent">
                   		<table width="100%" border="0" cellspacing="0" cellpadding="0"
							class="tabs">
							<tr>
								<td valign="top" class="no-borders user-profile ">
								<table width="100%" border="0" cellpadding="0" cellspacing="0">
									<tr>
				                   		<s:action name="xpedxCustomerQuickLink" namespace="/profile/user" executeResult="true" >
				                   			<s:param name='customerContactId' value='%{#customercontact.getAttribute("CustomerContactID")}' />
				                   			<s:param name='createSelected' value='true'/>
				                   		</s:action>
				                   	</tr>
								</table>
								</td>
							</tr>														
                   		</table>
                   		<div class="clearview"> &nbsp;</div>
                      <div class="clearview"> &nbsp;</div>
                      <div class="clearview"> &nbsp;</div>
                      <div class="clearview"> &nbsp;</div>
                      <div class="clearview"> &nbsp;</div>
                      <div class="clearview"> &nbsp;</div>
                      <div class="clearview"> &nbsp;</div>
                      <div class="clearview"> &nbsp;</div>
                      <div class="clearview"> &nbsp;</div>
                      <div class="clearview"> &nbsp;</div>
                      <div class="clearview"> &nbsp;</div>
                      <div class="clearview"> &nbsp;</div>
                   	</div>
                  </div>
                  <div class="clearview">&nbsp;</div>
                  
                  <div id="cart-actions" class="float-right">
			          <ul id="cart-actions">
			            <li><a href="#" class="grey-ui-btn"><span>Cancel</span></a></li>
			            <li><a class="green-ui-btn" onclick="javascript: return callSave()" href="#"><span>Save</span></a></li>
			          </ul>
			        </div> 
			        <div class="clearview">&nbsp;</div>
                </div></s:form>
              </div> </div>
            <script type="text/javascript">
            
var TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1");

            </script>
 
        </div>
     
    <!-- end main  -->
   <s:action name="xpedxFooter" executeResult="true" namespace="/common" />
   
  </div>
<div style="display: none;">
<div title="Add New Quick Link" id="newQL"
	style="width: 400px; height: 220px; overflow: auto;"
	class="xpedx-light-box">
<h2>Add New</h2>
<p><strong></strong></p>
<s:hidden id="operation" value=""></s:hidden>

<table class="form" width="100%" style="clear: both">
	<tr>
		<td width="24%" valign="top" class="noBorders-fff padding-td"
			style="border-left: solid 1px #fff;">Name:</td>
		<td width="76%" valign="top" class="noBorders-fff padding-td"><s:textfield
			cssClass="x-input" id="linkName" name="linkName" value='' maxlength="35"/></td>
	</tr>
	<tr>
		<td width="24%" valign="top" class="noBorders-fff padding-td"
			style="border-left: solid 1px #fff;">URL:</td>
		<td width="76%" valign="top" class="noBorders-fff padding-td"><s:textfield
			cssClass="x-input" id="url" name="url" value='' maxlength="150" /></td>
	</tr>
</table>
<div></div>
<ul id="tool-bar" class="tool-bar-bottom float-right">
	<li><s:a cssClass="grey-ui-btn "
		href="javascript:$.fancybox.close();">
		<span>Cancel</span>
	</s:a></li>
	<li><s:a href="javascript:saveClick()" cssClass="green-ui-btn">
		<span>Save</span>
	</s:a></li>
</ul>

</div>
</div>
<div style="display: none; " class='x-hidden dialog-body '>
	<div id="viewShipToDlg" style="width: 745px; height: 397px; overflow: -moz-scrollbars-vertical;">
	</div>
</div>

</body>
<script>
function resetPassword()
{
	var url = '<s:property value="#ResetPasswordURL" />';
	Ext.Ajax.request({
        url :url,
        params:{userId :'<s:property value="#loginid" />',
		customerId : '<s:property value="#customerId"/>',
		customerContactId:'<s:property value="#customercontact.getAttribute(\'CustomerContactID\')" />'},
        method: 'POST',
        success: function (response, request){
        	alert("Reset Password link has been sent to your registered email.");
   		},
   		failure: function (response, request){
   			alert("Error sending reset password notification email!");
         }
    });     
}
</script>
</html>