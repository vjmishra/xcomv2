<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<!-- Web Trends tag start -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/webtrends/displayWebTag<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- Web Trends tag end  -->

	<s:set name="isUserAdmin" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@isCurrentUserAdmin(wCContext)" />
	<s:set name="CurrentCustomerId" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@getCurrentCustomerId(wCContext)" />
	
	<script type="text/javascript">
		var isUserAdmin = <s:property value="#isUserAdmin"/>;
	</script>
	<s:set name='_action' value='[0]' />	


<script type="text/javascript">
		function updateShareListChild(){
		}
		
		var callShareList  = true;
		
		function doAction(action, idx, listKey, listName, numOfItems){
			
			//Declare some vars
			var showWait = true;
			
			//Init the form
			var form = Ext.get("doAction_" + action + "_" + idx);
			
			switch(action) {
				case 'open':
					form.dom.submit();
				  break;
				case 'view':
				  	form.dom.submit();
				  	break;
				case 'export':
					//Wait for 5 sec and them take the dialog off 
					var task = new Ext.util.DelayedTask(function(){
					    Ext.MessageBox.hide();
					});
					task.delay(5000);
					form.dom.submit();
				  break;
				case 'import':
					document.formImport.listKey.value  = listKey;
					document.formImport.listName.value = listName;
					document.formImport.itemCount.value = numOfItems;
					if(numOfItems<200){
						$("#dlgImportItemsLink").trigger('click');
						
					}
					else{
						  alert("Maximum number of element in a list can only be 200..\n Please try again with removing some items or create a new list.");
					}
					
				  break;
				  
				case 'copy':
					callShareList = false;
					
					var elemsToValidateHL = document.getElementsByTagName('input');
					var invalidMandatoryFieldCountHL = 0;
					for(var i=0; i< elemsToValidateHL.length; i++)
					{			
							elemsToValidateHL[i].style.borderColor="";
							
					} 
					
					
					document.getElementById("errorMsgForMandatoryFieldsHL").style.display = "none";
					Ext.get("XPEDXMyItemsDetailsChangeShareListHL").dom.clFromListId.value=listKey;
					$("#various3").trigger('click');
					form = Ext.get("doAction_general_" + idx);
					showShareListHL(form.dom.customerId.value, false, form.dom.listKey.value);
					var clFrom = Ext.get("XPEDXMyItemsDetailsChangeShareList");
					clFrom.dom.clFromListIdTemp.value=form.dom.listKey.value;
					Ext.get("smilTitleHL").dom.innerHTML = "Copy My Items List";
					Ext.get("XPEDXMyItemsDetailsChangeShareListHL").dom.listNameHL.value="";
					Ext.get("XPEDXMyItemsDetailsChangeShareListHL").dom.listDesc.value="";
					showWait = false;
				  break;
				  
				case 'delete':		
					$.fancybox(
						Ext.get("delete_my_item_list").dom.innerHTML,{
						'autoDimensions'	: true  
					});
					var form = Ext.get("doAction_delete_item_list");
					form.dom.listKey.value=listKey;
				  break;
				  
				case 'share':
				  break;
				  
				default:
					showWait = false; 
			}
			
			if (showWait){
				//Ext.Msg.wait('Executing your request...Please wait.');
			}		
		}
		
	$(document).ready(function() {
		
		$("#dlgShareListLinkHL,#dlgShareListLinkHL1,#dlgShareListLinkHL2,#dlgShareListLinkHL3").fancybox({
			'onStart' 	: function(){
				if (isUserAdmin){
				//Call AJAX function to fetch Ship-To locations only when user is an Admin
					showShareListHL('<s:property value="#CurrentCustomerId"/>');
				}
				Ext.get("smilTitleHL").dom.innerHTML = "New My Items List"; 
				document.XPEDXMyItemsDetailsChangeShareListHL.listName.value = "";
				document.XPEDXMyItemsDetailsChangeShareListHL.listDesc.value = "";
				document.XPEDXMyItemsDetailsChangeShareListHL.shareAdminOnly.checked=false;
			},
			'onClosed' : function(){
				mandatoryFieldCheckFlagHL = false;
				document.XPEDXMyItemsDetailsChangeShareListHL.listName.style.borderColor="";
	            document.getElementById("errorMsgForMandatoryFieldsHL").style.display = "none";
	            var radioBtns = document.XPEDXMyItemsDetailsChangeShareListHL.sharePermissionLevel;
				var div = document.getElementById("dynamiccontentHL");
				clearTheArrays();
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
			'height' 			: 475  
		});
		
	});	
	
	function showShareListHL(customerId, showRoot, clFromListId){
		//Populate fields
		var divMainId 	= "divMainShareListHL";
		var divMain 	= document.getElementById(divMainId);
		
		//Check for show root
		if (showRoot == undefined ){
			//showRoot = true;
		}
		
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
	}

	function collapseTheDiv(customerId, divId, showRoot, controlId, customerPath, name, div){

		var isInputChecked = "";
		var inputElem = $("div[id="+divId + "] input[id=customerPaths_"+controlId + "] ");
		if(inputElem!=null)
			isInputChecked = inputElem.attr("checked");
		if(isInputChecked == true)
			isInputChecked = "checked";
		
		document.getElementById(divId).innerHTML = "";	
		var newHTML = '<input type="button" class="icon-plus" style="vertical-align:middle;" onclick="getShareList(\'' + customerId + '\',\'' + divId + '\', false) " />';
	    newHTML += '<input type="checkbox" 	id="customerPaths_' + controlId + '"  onclick="selectNode(\'' + controlId + '\', this.checked);" name="customerPaths" value="' + customerPath + '" '+isInputChecked+' /> ';
		newHTML += name;
		newHTML += '<div style="display: none;" >';
		newHTML += '<input type="checkbox" id="customerIds_' + controlId + '" name="customerIds" 	value="' + customerId + '" '+isInputChecked+'>';
		newHTML += '<input type="checkbox" id="customerDivs_' + controlId + '" name="customerDivs" 	value="' + div + '" '+isInputChecked+'>';
		newHTML += '</div>';

		document.getElementById(divId).innerHTML = newHTML;
	}
	var selectSavedCustomers = false;
	function getShareList(customerId, divId, showRoot){
           //Init vars
		   //showRoot will be set to true only from MIL details page on fancybox onStart getShareList
		   //This is because we need to determine as we must show the saved customer selection only on first getShareList call
		   //and not on successive calls
           if(showRoot == true){
        	   selectSavedCustomers = true;
           }

    	   showRoot = null;
    	   
           <s:url id='getShareList' includeParams='none' action='XPEDXMyItemsDetailsGetShareList'/>
           if (showRoot == null){ showRoot = false; }
           
           var isCustomerSelected = false;
           //Replace all '-' with '_'
           var controlId = customerId.replace(/-/g, '_');
           //Get the current checkbox selection of the customer
		   if(document.getElementById('customerPaths_'+controlId)!=null){
               isCustomerSelected = document.getElementById('customerPaths_'+controlId).checked;
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
                       alert('Failed to fecth the share list');
                       document.body.style.cursor = 'default';                                                  
                   }
               });     
           }
       	document.body.style.cursor = 'default';
	}

	/**
	* The below function is called in the setAndExecute method of getShareList function which is called to fetch the child customers
	*
	*/
	function saveShareListForChild(divId, isCustomerSelected){
		   //Get all the child checkboxes within the div.
		   var allChildElements = $("div[id="+divId + "] input[name=customerPaths]");
           for(var j=0;j<allChildElements.length;j++)
			{
				var currentCB = allChildElements[j];
				var controlId = currentCB.id.replace("customerPaths_","");
				//Check if the current child customer was selected
				
				var isInSelectedCustomersMap = checkIfControlIdInMap(controlId);
				//alert('isCustomerSelected===>'+isCustomerSelected+", isInSelectedCustomersMap==>"+isInSelectedCustomersMap);
				//If the parent customer is checked, check the child customer too
				//if the parent customer is unchecked, uncheck the child, provided its not present in selectedCustomersMap
				if((isCustomerSelected==isInSelectedCustomersMap) || (isCustomerSelected==true && isInSelectedCustomersMap== false )){
					updateSelectedCustomersMap(controlId,isCustomerSelected);
					currentCB.checked = isCustomerSelected;
					checkAll("customerIds_" + controlId, isCustomerSelected);
					checkAll("customerDivs_" + controlId, isCustomerSelected);
				}
			}
	}
	
	function ReplaceAll(Source,stringToFind,stringToReplace){
	  var temp = Source;
	    var index = temp.indexOf(stringToFind);
	        while(index != -1){
	            temp = temp.replace(stringToFind,stringToReplace);
	            index = temp.indexOf(stringToFind);
			}
	        return temp;
	}

	//The below function is used to display the child customers fetched by getShareList call
	function setAndExecute(divId, innerHTML, isCustomerSelected) {  
	   var div = document.getElementById(divId);  
	   div.innerHTML = innerHTML; 
	//Execute the scripts to create the countAndPath Map and preselect customers
	   var x = div.getElementsByTagName("script");   
	   for( var i=0; i < x.length; i++) {  
	     eval(x[i].text);  
	   }
	   
	   saveShareListForChild(divId, isCustomerSelected);
	   var divMainShareList = $("div[id=divMainShareList]");
	   //divMainShareList exists only for the Shared List popup in MIL details page(in other places the div is divMainShareListHL)
	   if(divMainShareList!=null && selectSavedCustomers== true)
	   		showSavedSharedSelection();
  	   //Make selectSavedCustomers to false so that it does not load the saved customers selection on every page load/getshareList call.
	   selectSavedCustomers = false;
	   
	   /*var x = div.getElementsByTagName("script");   
	   for( var i=0; i < x.length; i++) {  
	     eval(x[i].text);  
	   }*/
	}

	//The below function is used to show the saved customer selection for a shared MIL in MIL Details page.
	function showSavedSharedSelection()
	{
		var customerPathsCB = $("div[id=divMainShareList] input[name=customerPaths]");
		if(customerPathsCB!=null){
			for(var i=0;i<customerPathsCB.length;i++)
			{
				var currentCustomerPathCB = customerPathsCB[i];
				var sslNames = $('input[name=sslNames]');
				if(sslNames!=null){
					for(var j=0;j<sslNames.length;j++)
					{
						var currentCustomerId = sslNames[j].value;
						var controlId = currentCustomerId.replace(/-/g, '_');
						if(currentCustomerPathCB.id == 'customerPaths_'+controlId)
						{
							currentCustomerPathCB.checked = true;
							updateSelectedCustomersMap(controlId, true);
							//To select a customer, we should select all 3 checkboxes: customerPaths_,customerIds_ and customerDivs_
							var customerIdsCB = $("div[id=divMainShareList] input[id=customerIds_"+controlId+"]");
							if(customerIdsCB!=null){
								customerIdsCB.checked = true;
							}
							var customerDivsCB = $("div[id=divMainShareList] input[id=customerDivs_"+controlId+"]");
							if(customerDivsCB!=null){
								customerDivsCB.checked = true;
							}
							break;
						}
					}
				}
			}
		}	
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

	var mandatoryFieldCheckFlagHL = false;
	function listNameCheckHL(component){
		if(mandatoryFieldCheckFlagHL){
			if(component.value==""){
            	component.style.borderColor="#FF0000";
        	}
        	else{
            	component.style.borderColor="";
    	    }
    	}
    }
	function checkAddressSelection()
	{
		var returnErrorMsgHL ="";
		var checkboxes = Ext.query('input[id*=customerPaths_]');
		var addressSelected = false;
		Ext.each(checkboxes, function(obj_item){
			debug("checkAll " + id + ", value: " + obj_item.checked);
			if(!addressSelected && obj_item.checked)
				addressSelected=true;
		});
		if(!addressSelected)
		{
			returnErrorMsgHL="Please select at least one location to share.";
			document.getElementById("errorMsgForAddressFieldsHL").innerHTML =returnErrorMsgHL;
			document.getElementById("errorMsgForAddressFieldsHL").style.display = "inline";
			//mandatory passed when checking for address selection
			document.getElementById("errorMsgForMandatoryFieldsHL").style.display = "none";
			
		}
		else
		{
			document.getElementById("errorMsgForAddressFieldsHL").style.display = "none";
		}
		return returnErrorMsgHL;
			
	}
	
	
		function checkifShared() {
			var radioObj =document.XPEDXMyItemsDetailsChangeShareListHL.sharePermissionLevel
			if(!radioObj)
				return "";
			var radioLength = radioObj.length;
			if(radioLength == undefined)
				if(radioObj.checked)
					return radioObj.value;
				else
					return "";
			for(var i = 0; i < radioLength; i++) {
				if(radioObj[i].checked) {
		                        if(radioObj[i].id =="rbPermissionShared")
		                            return "shared";
		                        else
		                            return radioObj[i].value;
				}
			}
			return "";
		}
		
		function submitNewlistHLNew(xForm){
		try{
			if (mandatoryFieldValidationHL() != "")
			{
				document.getElementById("errorMsgForAddressFieldsHL").style.display = "none";
				//mandatory fields error don't show address error message
				
				return;
			}
			
			if (checkifShared() == "shared" && checkAddressSelection() != "")
			{
				document.getElementById("errorMsgForMandatoryFieldsHL").style.display = "none";
				//address error don't show mandatory fields error message
				return;
			}
		}catch(err){
		}

		//Check for maxlength description
		if (document.getElementById("listDescHL").value.length > 255){
			var jasonErrorMess = "The description can contain a maximum of "+ 255 +" characters, please revise and try again.";
			document.getElementById("errorMsgForMandatoryFieldsHL").innerHTML = jasonErrorMess;
			document.getElementById("errorMsgForMandatoryFieldsHL").style.display = "inline";
			return;
		}
		else{
			document.getElementById("errorMsgForMandatoryFieldsHL").style.display = "none";
		}
		//The following check is to determine whether the new list modal is in Item details page.
		//If it is so, we should add the item to the newly created list.
		var div = $("div[id*=divAdd2ListRadio]");
		if(div!=null && div.length>0)
			submitNewlistAddItem(xForm);
		else
			submitNewlistHL(xForm);
	}

	function submitImportForm(xForm){
		var form = Ext.get(xForm);
		var fileComponent = form.dom.upload;
		if (fileComponent.value == null || fileComponent.value == "")
		{
			//alert(document.getElementById("errorMsgForBrowsePath").style.display);
			document.getElementById("errorMsgForBrowsePath").style.display = "block";
			//document.getElementById("errorMsgForRequiredField").innerHTML = "Mandatory Fields missing : Browse path";
			document.getElementById("errorMsgForRequiredField").style.display = "block";
			return;
		}
		
		form.dom.submit();
	}
	
	function submitNewlistHL(xForm){
		//Validate the form
		try{
			if (xForm == undefined || xForm == null || xForm == ""){
				xForm = "XPEDXMyItemsDetailsChangeShareList";
			}
		}catch(er){
			xForm = "XPEDXMyItemsDetailsChangeShareList";
		}
		
		var form = Ext.get(xForm);
		
		var dataParams = new Object();
		
		for (i=0; i < form.dom.elements.length; i++){
			var item = form.dom.elements[i];
			if (item.name != ""){
				dataParams[item.name] = item.value;
			}
			
			//alert("name 1: " + item.name + ", value: " + item.value);
			//alert("name 2: " + item.name + ", value: " + params[item.name]);
		}
		try{ console.debug("params" , dataParams); }catch(e){}

		//adding the already seleted as hidden checkboxes to the form 
		createHiddenInputsForSelectedCustomers(xForm);
		clearTheArrays();// clearing the arrays
		
		//form.dom.submit();
        //Submit the data via ajax
        
        //Init vars
        <s:url id='XPEDXMyItemsDetailsChangeShareListURL' includeParams='none' action='XPEDXMyItemsDetailsChangeShareList' namespace="/xpedx/myItems" escapeAmp="false" />
        
        var url = "<s:property value='#XPEDXMyItemsDetailsChangeShareListURL'/>";
        url = ReplaceAll(url,"&amp;",'&');
        
        //Execute the call
        document.body.style.cursor = 'wait';
        $.fancybox.close();
        Ext.Msg.wait('Creating list...');

        Ext.Ajax.request({
          url: url,
          form: xForm,
          method: 'POST',
          success: function (response, request){
              document.body.style.cursor = 'default';
              //try{ console.debug("response" , response.responseText); }catch(e){}
              //Get the data
              var data = response.responseText;
              var listKey 	= getValueFromHTML(data, 'listKey" value="');
              var listName 	= getValueFromHTML(data, 'listName" value="');;
              
              //Add the items to the list
              try{
	              var dlItems = currentAadd2ItemList;
	              var opt = document.createElement('option');
				  opt.text	= listName;
				  opt.value = listKey;
				  if(dlItems != null)
				  {				  
					  try { dlItems.add(opt,null); } 
					  catch(ex) { dlItems.add(opt); }
					  dlItems.selectedIndex = dlItems.length-1;
					  currentAadd2ItemList.onchange();
				  }
					  }catch(ex){
					  }

			  //addItemsToList(dlItems.selectedIndex);
			  //$("#itemListSelect").trigger('change');
			  
			  /*Web Trends tag start */
			  writeMetaTag('DCSext.w_x_list_addnew','1');
			  /*Web Trends tag End */
			  //reloadMenu();
			  // Removal of MIL dropdown list from header for performance improvement
			  Ext.Msg.hide();
          },
          failure: function (response, request){
              document.body.style.cursor = 'default';
              Ext.Msg.hide();
              //alert("Error creating new list. Please try again later.");
          }
      });
      document.body.style.cursor = 'default';
		
		
	}

	var currentAadd2ItemList = null;
	
	function getValueFromHTML(data, key, delimiter){
		var res = "";
		try{
			if (delimiter == undefined || delimiter == ""){
				delimiter = '"';
			}
			var tmp1 = data.split(key);
			var tmp2 = tmp1[1];
			tmp1 = tmp2.split(delimiter);
			res = tmp1[0];
		}catch(e){
			res = "";	
		}
		
		return res;
	}
	
	function deleteMyItemList(){
		form.dom.submit();
	}
	
</script>

<style type="text/css">
.icon-plus {
    margin-bottom: 8px;
    margin-right: 8px;
    width: 18px;
    height: 18px;
    background:url("<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/icon-plus.png") no-repeat;
    border:none;    
}
.icon-minus {
    margin-bottom: 8px;
    margin-right: 8px;
    width: 18px;
    height: 18px;
    background:url("<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/icon-minus.png") no-repeat;
    border:none;    
}
</style>



<ul class="sub_menu" style="visibility: hidden;">
		<li class="">
			<a class="link" id="dlgShareListLinkHL1" name="dlgShareListLinkHL" href="#dlgShareListHL"> Create New List</a>
		</li>

	<s:iterator status="status" id="item" value="listOfItems">
		<s:set name='id' value='#item.getAttribute("MyItemsListKey")'/>
		<s:set name='name' value='#item.getAttribute("Name")'/>
		<s:set name='desc' value='#item.getAttribute("Desc")'/>
		<s:set name='customerId' value='#item.getAttribute("CustomerID")'/>
		<s:set name='createdBy' value='#item.getAttribute("Createuserid")'/>
		<s:set name='lastMod' value='#item.getAttribute("Modifyts")'/>
		<s:set name="childrenEle" value='XMLUtils.getChildElement(#item,"XPEDXMyItemsItemsList")' />
		<s:set name="numOfItems" value='#childrenEle.getAttribute("TotalNumberOfRecords")' />
		<s:set name="spLevels" value='#item.getAttribute("SharePrivate")' />
		<s:set name="spShareAdminOnly" value='#item.getAttribute("ShareAdminOnly")' />
		<s:set name="listOwner" value='#item.getAttribute("ShareAdminOnly")' />
		
		<s:set name="uId" value="#id" />
		
		<s:form id="doAction_open_%{#uId}" action="XPEDXMyItemsDetails" method="get">
			<s:hidden name="listKey" value="%{#id}" />
			<s:hidden name="listName" value="%{#name}" />
			<s:hidden name="listDesc" value="%{#desc}" />
			<s:hidden name="itemCount" value="%{#numOfItems}" />
			<s:hidden name="sharePermissionLevel" value="%{#spLevels}" />
			<s:hidden name="shareAdminOnly" value="%{#spShareAdminOnly}"/>
			<s:hidden name="listOwner" value="%{#listOwner}"/>
			<s:hidden name="listCustomerId" value="%{#customerId}"/>
			<s:hidden name="filterBySelectedListChk" value="%{#_action.getFilterBySelectedListChk()}"/>
			<s:hidden name="filterByMyListChk" value="%{#_action.getFilterByMyListChk()}"/>
			<s:hidden name="filterByAllChk" value="%{#_action.getFilterByAllChk()}"/>
			<s:hidden name="filterBySharedLocations" value="%{#_action.getFilterBySharedLocations()}"/>
			<s:hidden name='sharePrivateField' value='%{#spLevels}' />
		</s:form>
		
		<s:set name='displayName' value='#item.getAttribute("Name")'/>
		
		<s:if test="%{(#displayName.length()) > 15}">
			<s:set name='displayName' value='%{#displayName.substring(0,15)}'/>
			<s:set name='displayName' value='%{#displayName + "..."}'/>
		</s:if>
		
		<li class="">
			<s:a cssClass="link" href="javascript:doAction('open', '%{#uId}');" title="%{#name}"> <s:property value="#displayName"/> (<s:property value="#numOfItems"/>)</s:a>
		</li>
	</s:iterator>
</ul>