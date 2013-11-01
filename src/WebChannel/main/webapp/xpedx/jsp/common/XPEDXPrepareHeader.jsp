<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<meta http-equiv="X-UA-Compatible" content="IE=8" /> 
	<s:set name="isMergedCSSJS" value="(#request.isMergedCSSJS )"/>
  
    <%
  		request.removeAttribute("isMergedCSSJS");
  	  %>
  	  
  	<s:if test="(#isMergedCSSJS == null && #isMergedCSSJS != 'true')"> 
		<!-- BEGIN Styles -->
		<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" />
		<!--[if IE]>
			<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-ie-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" />
		<![endif]-->
		
		<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/catalog/change-ship-to<s:property value='#wcUtil.xpedxBuildKey' />.css" />
 		<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/themes/base/jquery.ui.all<s:property value='#wcUtil.xpedxBuildKey' />.css" />
		<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/cluetip/jquery.cluetip<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
		<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/mini-cart<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
		<!-- END Styles -->
		
		<!-- BEGIN JS -->
		<!-- Web Trends tag start -->
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/webtrends/displayWebTag<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
		<!-- Web Trends tag end  -->
		
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
  		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global-xpedx-functions<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
  		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/cluetip/jquery.cluetip<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/ajaxValidation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/swc<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
		<!-- END JS -->
	</s:if>
	<s:else>
		<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" />
		<!--[if IE]>
			<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-ie-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" />
		<![endif]-->
		
		<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
	</s:else>
	<s:include value="../order/XPEDXRefreshMiniCart.jsp"/>	
	<s:set name="isUserAdmin" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@isCurrentUserAdmin(wCContext)" />
	<s:set name="isEstUser" value='%{#xpedxCustomerContactInfoBean.isEstimator()}' />
	<s:set name="CurrentCustomerId" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@getCurrentCustomerId(wCContext)" />
	<s:set name="canRequestProductSample" value="#session.showSampleRequest" />
	<s:set name="goBackFlag" value='%{"false"}' />
	<s:hidden id="goBackFlag" name="goBackFlag" value="%{goBackFlag}"></s:hidden>
<s:url id='getCategoryMenu' action='gategorySubMenu' namespace='/common' >
</s:url>
	<s:url id="ValidatePasswordURL" action="xpedxPasswordValidation"/>
	<s:url id="XPEDXPasswordSubmitURL" action="XPEDXPasswordSubmit"/>
	<s:url id="orderdetailsURL" namespace="/order" action="orderDetail"/>
	<s:hidden id='orderdetailsURLId' value='%{#orderdetailsURL}' />	
<script type="text/javascript">

function savePassword(){
	var errorDiv = document.getElementById("pwdValidationDiv");
	var errDiv = document.getElementById("errorNote");
	var error = document.getElementById("pwdErrorDiv");
	if(error !=null){
		error.innerHTML = '';
	}
	if(errDiv != null)
	{
		errDiv.style.display = 'none';
	}
	if(errorDiv != null){
	errorDiv.style.display = 'none';
	}
	 var answerFiled = document.passwordUpdateForm.userpassword;
	 var answerConfirmFiled = document.passwordUpdateForm.confirmpassword;
	 var errDiv = document.getElementById("errorMsgFor_userpassword");
	 var errDivBlank = document.getElementById("errorMsgFor_blank");
	 if(errDivBlank != null){
		 errDivBlank.style.display = 'none';
	 }
	 if(errDiv != null){
		 errDiv.style.display = 'none';
	 }
	 answerFiled.style.borderColor="";
	 answerConfirmFiled.style.borderColor="";
	 document.passwordSubmit.newPassword.value = answerFiled.value;
	 var returnVal = false;
	 if(answerFiled.value.trim().length == 0 && answerConfirmFiled.value.trim().length == 0){
	 		errDivBlank.innerHTML = "Required fields missing. Please review and try again."
	 			errDivBlank.style.display = "inline";
			 answerFiled.style.borderColor="#FF0000";
			 answerConfirmFiled.style.borderColor="#FF0000";
			 return;
			}
	 	if(answerFiled.value.trim().length == 0 || answerFiled.value == null){
	 		errDivBlank.innerHTML = "Required field missing. Please review and try again."
	 			errDivBlank.style.display = "inline";
			 answerFiled.style.borderColor="#FF0000";
			 return;
			}
	 	
	 	if(answerConfirmFiled.value.trim().length == 0 || answerConfirmFiled.value == null){
	 		errDivBlank.innerHTML = "Required field missing. Please review and try again."
	 			errDivBlank.style.display = "inline";
			answerConfirmFiled.style.borderColor="#FF0000";
			 return;
			}
	 	
	 	
		if(answerFiled.value != answerConfirmFiled.value){
				errDiv.innerHTML = "Please enter the same password in both Password and Confirm Password fields."
				errDiv.style.display = "inline";
				return;
			}

			validatePassword();
			
	}

function validatePassword(){
	var erDiv = document.getElementById("pwdErrorDiv");
	if(erDiv != null){
		erDiv.innerHTML = '';
	}
	 var answerFiled = document.passwordUpdateForm.userpassword;
	 document.getElementById("userPwdToValidate").value=answerFiled.value;
	var url = '<s:property value="#ValidatePasswordURL" />';
		Ext.Ajax.request({
	        url :url,
	        params:{
	        	userPwdToValidate : answerFiled.value
	        	},
	        method: 'POST',
	        success: function (response, request){
	           var responseText = response.responseText;
	           var errorDiv = document.getElementById("pwdValidationDiv");
              		if(responseText.indexOf("error")>-1){
	                    errorDiv.innerHTML = response.responseText;
	                    errorDiv.style.display = 'block';
	                }else{
	                	//errorDiv.removeChild(document.getElementById('pwdErrorDiv'));
	                	document.getElementById("submitButton").disabled=true;
	                	errorDiv.style.display = 'none';
	                	document.passwordSubmit.submit();
	                }
              
  	   		},
	   		failure: function (response, request){
	   		   errorDiv = document.getElementById("pwdValidationDiv");
              if(errorDiv){
               errorDiv.style.display = 'none';
              }
	    	   //alert("Error in service.");
	        }
	    });
}

function saveAnswer(){
    var answerFiled = document.secrectQuestionForm.secretAnswer ;
    var answerConfirmFiled = document.secrectQuestionForm.confirmAnswer ;
    var errorDiv = document.getElementById("errorMsgForAnswer");
    var returnval = false;
    
    var questionEle = document.getElementById("secretQuestion");
        
    errorDiv.innerHTML = "";
   answerFiled.style.borderColor="";
    errorDiv.style.display = "none";

    if(questionEle.selectedIndex==0){
    	errorDiv.innerHTML = "Required fields missing. Please review and try again.";
    	//questionEle.style.borderColor="#FF0000";
    	questionEle.focus();
        errorDiv.style.display = 'inline';
        return ;
        }
    
    else if(answerFiled.value.trim().length == 0)
    {
        errorDiv.innerHTML = "Required fields missing. Please review and try again.";
        answerFiled.style.borderColor="#FF0000";
        errorDiv.style.display = 'inline';
        return ;
    }
    else if(answerConfirmFiled.value.trim().length == 0){
    	 errorDiv.innerHTML = "Required fields missing. Please review and try again.";
    	 answerConfirmFiled.style.borderColor="#FF0000";
         errorDiv.style.display = 'inline';
         return ;
        }
    else if(answerFiled.value!=answerConfirmFiled.value){
    	errorDiv.innerHTML = "Please enter the same answers in both answer and confirm answer fields.";
       	answerFiled.style.borderColor="#FF0000";
       	answerConfirmFiled.style.borderColor="#FF0000";
        errorDiv.style.display = 'inline';
        return ;
        }
    else{
    document.secrectQuestionForm.submit();
    returnval = true;
    }
    return returnval;
    
}

var selectedShipCustomer = null;
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
						document.getElementById("importWarning").innerHTML = "";
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
					Ext.get("XPEDXMyItemsDetailsChangeShareListHL").dom.clAjax.value=true;
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
		Ext.onReady(function(){		
		

			
		$("#dlgShareListLinkHL,#dlgShareListLinkHL1,#dlgShareListLinkHL2,#dlgShareListLinkHL3").fancybox({
			'onStart' 	: function(){
				if (isUserAdmin || isEstUser){
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
				if(!isUserAdmin && !isEstUser)
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
			showRoot = true;
		}		
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
	}
	function collapseTheDiv(customerId, suffixtype,divId, showRoot, controlId, customerPath, name, div){
		var isInputChecked = "";
		var inputElem = $("div[id="+divId + "] input[id=customerPaths_"+controlId + "] ");
		if(inputElem!=null)
			isInputChecked = inputElem.attr("checked");
		if(isInputChecked == true)
			isInputChecked = "checked";
		
		document.getElementById(divId).innerHTML = "";	
		var newHTML = '<input type="button" class="icon-plus" style="vertical-align:middle;" onclick="getShareList(\'' + customerId + '\',\'' + suffixtype + '\',\'' + divId + '\', false) " />';
	    newHTML += '<input type="checkbox" 	id="customerPaths_' + controlId + '"  onclick="selectNode(\'' + controlId + '\', this.checked);" name="customerPaths" value="' + customerPath + '" '+isInputChecked+' /> ';
		newHTML += name;
		newHTML += '<div style="display: none;" >';
		newHTML += '<input type="checkbox" id="customerIds_' + controlId + '" name="customerIds" 	value="' + customerId + '" '+isInputChecked+'>';
		newHTML += '<input type="checkbox" id="customerDivs_' + controlId + '" name="customerDivs" 	value="' + div + '" '+isInputChecked+'>';
		newHTML += '</div>';

		document.getElementById(divId).innerHTML = newHTML;
	}
	var selectSavedCustomers = false;
	function getShareList(customerId,suffixtype, divId, showRoot){
           //Init vars
		   //showRoot will be set to true only from MIL details page on fancybox onStart getShareList
		   //This is because we need to determine as we must show the saved customer selection only on first getShareList call
		   //and not on successive calls
           if(showRoot == true){
        	   selectSavedCustomers = true;
           }
    	   showRoot = null;   	   
           <s:url id='getShareList' namespace='/myItems' action='XPEDXMyItemsDetailsGetShareList'></s:url> 
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
           url=url+"&suffixtype="+suffixtype;
           //alert(url);
           //Show the waiting box
           var x = document.getElementById(divId);
           x.innerHTML = "Loading data... please wait!";          
           //Execute the call
           document.body.style.cursor = 'wait';
           if(true){
                 Ext.Ajax.request({
                   url: url,
                   params: {
                             customerId: customerId,
                             suffixtype: suffixtype,
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
                       alert('Unable to load the share locations. Please try again.');
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
				//If the parent customer is checked, check the child customer too
				//if the parent customer is unchecked, uncheck the child, provided its not present in selectedCustomersMap				
				isCustomerSelected = false;
				if((isCustomerSelected==isInSelectedCustomersMap) || (isCustomerSelected==true && isInSelectedCustomersMap== false )){
					updateSelectedCustomersMap(controlId,isCustomerSelected);
					currentCB.checked = isCustomerSelected;
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
	    //commented for jira 2940 if((divMainShareList!=null )&& (selectSavedCustomers== true)) 
	   if(divMainShareList!=null ) 
	   		showSavedSharedSelection();
  	   //Make selectSavedCustomers to false so that it does not load the saved customers selection on every page load/getshareList call.
	   selectSavedCustomers = false;
	       isCustomerSelected = false;	   
	}
	//The below function is used to show the saved customer selection for a shared MIL in MIL Details page.
	function showSavedSharedSelection()
	{
	      isCustomerSelected = false;	
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
			document.getElementById("errorMsgForBrowsePath").style.display = "block";
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
		}
		try{ console.debug("params" , dataParams); }catch(e){}
		//adding the already seleted as hidden checkboxes to the form 
		createHiddenInputsForSelectedCustomers(xForm);
		clearTheArrays();// clearing the arrays
        //Removed Submit the data via ajax       
        //Init vars
        <s:url id='XPEDXMyItemsDetailsChangeShareListURL' includeParams='none' action='MyItemsDetailsChangeShareList' namespace="/myItems" escapeAmp="false" />       
        var url = "<s:property value='#XPEDXMyItemsDetailsChangeShareListURL'/>";
        url = ReplaceAll(url,"&amp;",'&');        
        //Execute the call
        document.body.style.cursor = 'wait';
        $.fancybox.close();
		//Added For Jira 2903
		//Coomented for 3475
        //Ext.Msg.wait("Processing..."); 
        form.dom.submit();	
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
<script type="text/javascript">
	Ext.Ajax.timeout = 240000;
	var customerChildCountMap = new Array();
	var selectedCustomerMap = new Array();
	var customerPathMap = new Array();
	var customersArray = new Array();
	var customerDivMap = new Array();
	function clearTheArrays(){
		customerChildCountMap = [];
		selectedCustomerMap = [];
		customerPathMap = [];
		customersArray = [];
		customerDivMap = [];
	}
	function addCSRFToken(o, type)
	{
		var csrfToken='<%=session.getAttribute("SCUI_CSRF_SECURITY_TOKEN") %>';
		if(csrfToken != 'null')
		{
			if (type == 'link')
			{
				var url= o + '&scCSRFToken=' +csrfToken;
				url = ReplaceAll(url,"&amp;",'&');
				return url;
			}			
			//otherwise the o is assumed to be of form type.
			var el = document.createElement("input");
				el.type = "hidden";
				el.name = "scCSRFToken";
				el.id = "scCSRFToken";
				el.value = csrfToken;
				o.appendChild(el);
		}
		return o;
	}		
	function addSelectedCustomer(firstname,lastname,age,eyecolor)
	{
	this.firstname=firstname;
	this.lastname=lastname;
	this.age=age;
	this.eyecolor=eyecolor;
	}

	if (!jQuery) { 
		var headID = document.getElementsByTagName("head")[0];         
		var newScript = document.createElement('script');
		newScript.type = 'text/javascript';
		newScript.src = '<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js';
		headID.appendChild(newScript);
	}
	var isUserAdmin = <s:property value="#isUserAdmin"/>;
</script>	
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/cluetip/jquery.cluetip<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- Web Trends tag start -->
<%--
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/webtrends/displayWebTag.js"></script>
 --%>
<!-- Web Trends tag end  -->
	<script type="text/javascript">
	$(document).ready(function() {
		$('a#inline').fancybox();
		$("#preventChangeShipTo").fancybox();		
	});
		var isUserAdmin = <s:property value="#isUserAdmin"/>;
		function callCartDetails(storefrontId)
		{
			var orderHeaderKey=document.getElementById('carddetailOrderHeaderKey').value;
			if(orderHeaderKey == null || orderHeaderKey == '' || orderHeaderKey == 'null' || orderHeaderKey == '_CREATE_NEW_')
			{
				orderHeaderKey = "_CREATE_NEW_";
				document.location.href= "/swc/order/quickAddAction.action?sfId="+ storefrontId +"&orderHeaderKey=" + orderHeaderKey + "&quickAdd=false&draft=Y&scFlag=Y";
			}
			else{
				document.location.href= "/swc/order/draftOrderDetails.action?sfId="+ storefrontId +"&orderHeaderKey=" + orderHeaderKey + "&draft=Y&scFlag=Y";
			}
		}
	</script>	
	<script>
	function getChildCustomerList(btn,customerId,suffixtype, divId){
		
           <s:url id='ChildCustomerList' namespace='/profile/org' action='xpedxShowLocations'></s:url> 
          
           var url = "<s:property value='#ChildCustomerList'/>";
           url = ReplaceAll(url,"&amp;",'&');
           url=url+"&shownCustomerSuffixType="+suffixtype+"&shownCustomerId="+customerId;
           var clsName=btn.className;
           if(clsName !=null && clsName !=undefined && clsName.indexOf('icon-plus') != -1)
           {
        	   btn.className='icon-minus';
         
           var x = document.getElementById(divId);
           x.innerHTML = "Loading data... please wait!";          
           //Execute the call
           document.body.style.cursor = 'wait';
                 Ext.Ajax.request({
                   url: url,
                   method: 'POST',
                   success: function (response, request){
                       document.body.style.cursor = 'default';
                       x.innerHTML = response.responseText;
                   },
                   failure: function (response, request){
                       var x = document.getElementById(divId);
                       x.innerHTML = "";
                       alert('Unable to load the share locations. Please try again.');
                       document.body.style.cursor = 'default';                                                  
                   }
               }); 
           }
           else
           {
        	   btn.className='icon-plus';
        	   document.getElementById(divId).innerHTML='';
           }
       	document.body.style.cursor = 'default';
	}
</script>
<%--This is to setup reference to the action object so we can make calls to 
    action methods explicitly in JSP's.
    This is to avoid a defect in Struts that's creating contention under load.
    The explicit call style will also help the performance in evaluating Struts'
    OGNL statements. --%>
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='xpedxutil' />    
<s:bean name='com.sterlingcommerce.framework.utils.SCXmlUtils' id='util' />
<s:set name='_action' value='[0]' />
<s:set name="editOrderHeaderKey" value ="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute(@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@EDITED_ORDER_HEADER_KEY)}"/>
<s:hidden id='editOrderHeaderKey' value='%{#editOrderHeaderKey}' />
<s:set name="isSalesRep" value ="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute('IS_SALES_REP')}"/>
<s:set name="isEditOrderHeaderKey" value ="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute(@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@EDITED_ORDER_HEADER_KEY)}"/>
<s:set name="xutil" value="#_action.getXMLUtils()"/>
<s:set name="xpedxCustomerContactInfoBean" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("XPEDX_Customer_Contact_Info_Bean")' />
<s:set name="loggedInUser" value="%{#_action.getWCContext().getLoggedInUserId()}"/>
<s:set name="isProcurementUser" value="%{#_action.getWCContext().isProcurementUser()}"/>
<s:set name="logUser" value ="%{#_action.getWCContext().getSCUIContext().getSecurityContext().getLoginId()}"/>
<s:set name="assgnCustomers" value="#_action.getAssignedShipTos()" />
<s:set name="fromWhichPage" value="#_action.getIsFromWhichPage()"/>
<s:set name="isCustomerSelectedIntoConext" value="#wcUtil.isCustomerSelectedIntoConext(wCContext)"/>
<s:set name='prodCategoryDocument' value="prodCategoryOutputDoc" />
<s:set name='prodCategoryElement' value="#prodCategoryDocument.getDocumentElement()" />
<s:set name="numOfCatToDisplay" value="numOfCatToDisplay"/>
<s:set name="xutil" value="XMLUtils"/>
<s:url id="userListURL" action="xpedxGetUserList" namespace="/profile/user" />
<s:url id='targetURL' namespace='/common'
	action='xpedxGetAssignedCustomers' />
<s:set name="isDefaultShipToSuspended" value="#_action.isDefaultShipToSuspended()"/>
<s:url id='targetURLForDefault' namespace='/common' action='xpedxGetAssignedCustomersForDefaultShipTo' />
<s:url id='xpedxHeaderUrl' action='xpedxHeader' namespace="/common" >
        <s:param name='shipToBanner' value="%{'true'}" />
</s:url>
<s:url id='refreshCustomerIntoContext' namespace='/common' action='refreshCustomerIntoContextForMIL.action'>	
<s:param name='shipToBanner' value="%{'true'}" />					
</s:url>
		 
<s:url id='shipToForOrderSearch' namespace='/common'
	action='xpedxGetAssignedCustomersForOrderList' />
<s:url id='shipToForUserProfileSearch' namespace='/common'
	action='xpedxGetAssignedCustomersForUserProfile' />
<s:url id='shipToSearchForOrderList' namespace='/common'
	action='xpedxSearchAssignedCustomersForOrderList' />
<s:url id='shipToSearchForReporting' namespace='/common'
	action='xpedxSearchAssignedCustomersForReporting' />	
<s:url id='shipToSearchForUserProfile' namespace='/common'
	action='xpedxSearchAssignedCustomersForUserProfile' />
<s:url id='showLocationsDivForReportingSearch' namespace='/services'
	action='xpedxGetAssignedCustomersForReporting' />
			
<s:url id='xpedxManageOtherProfilesURL' namespace='/profile/user'
	action='MyManageOtherProfiles' />
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean'
	id='hUtil' />

<!-- Terms of access modal doesnt work properly if the ext js is included after the css include -->
<!-- which is the case in home page . If required dynamically include ext-js only if not included already  -->
<div class='x-hidden dialog-body ' id="assignedShipToCustomersContent">
	<div id="ajax-assignedShipToCustomers" style="width: 750px; height:520px;"></div>
</div>
<s:url id='toaURL' namespace='/common' action='xpedxGetTermsOfAccess' />
<div class='x-hidden dialog-body ' id="termsOfAccessContent">
	<div id="ajax-termsOfAccessContent" class="xpedx-light-box"
		style="width: 1000px; height: 400px; overflow: auto;">
	</div>
</div> 
<%--JIRA 3487 start--%>
<s:set name="loggedInUserOrgCode"  value='wCContext.storefrontId'/>	
<s:url id='securityQueURL' namespace='/common' action='xpedxGetSecurityQuestion' >
<s:param name="organizationCode" value="#loggedInUserOrgCode" />
</s:url>
<s:url id='passwordUpdate' namespace='/common' action='xpedxPasswordUpdate' >
</s:url>

<div class='x-hidden dialog-body ' id="securityQueContent">
	<div id="ajax-securityQueContent" class="xpedx-light-box"
		style="width: auto; height: auto; ">
	</div>
</div>

<div class='x-hidden dialog-body ' id="passwordUpdateContent">
	<div id="ajax-passwordUpdateContent" class="xpedx-light-box"
		style="width: auto; height: auto; ">
	</div>
</div>
<%--JIRA 3487 end--%>
<s:url id='searchURL' namespace='/common' action='xpedxSearchAssignedCustomers' />
<s:url id='setStockedCheckboxURL' action="setStockedCheckbox" namespace="/catalog"/>
<input type="hidden" value="" id="orderDescending" name="orderDescending" />
<script type="text/javascript">
	function setNormallyStockedCheckbox() {
		var isSelected = document.getElementById('stockedItemChk').checked;
		Ext.Ajax.request({
      		url: '<s:property value="#setStockedCheckboxURL"/>',
      		params: {
          		stockedCheckeboxSelected: isSelected
          		},
      		method: 'POST',
      		success: function (response, request){
      		},
      		failure: function (response, request){
      		}
   		});
	}
	function setNormallyStockedSelectDropDown() {
		var isSelected = document.getElementById('stockedItemChk').value;	
		setNormallyStockedAjaxCall(isSelected);
	}	
	function setNormallyStockedAjaxCall(isSelected) {
	// Added for Jira 4195
		var searchTermString = document.getElementById("searchTermString").value;
		Ext.Ajax.request({
      		url: '<s:property value="#setStockedCheckboxURL"/>',
      		params: {
          		stockedCheckeboxSelected: isSelected
          		},
      		method: 'POST',
      		success: function (response, request){
if(searchTermString!=null && searchTermString.trim().length != 0){
                  	  var searchTermSuffix = '&searchTerm='+searchTermString;
            		  window.location.href=window.location.href+searchTermSuffix;  
                    }
                    else{
      			window.location.reload();      			
			}
      		},
      		failure: function (response, request){
      		}
   		});
	}
	function setNormallyStockedSelectDropDownBottom() {
		var isSelected = document.getElementById('stockedItemChkBtm').value;	
		setNormallyStockedAjaxCall(isSelected);
	}
	function shipToSearchSubmit(e,divId,url){
		var searchTxt = document.getElementById('Text1').value;
		var keycode;
		if (window.event) keycode = window.event.keyCode;
		else if (e) keycode = e.which;
		else return true;
		 <!-- XBT-343 Start-->
         if (keycode == 13)
         {     
                 searchShipToAddress(divId,url);
                 return true;

         } else {
                 return false;
         }
	}
	 <!-- XBT-343 Stop-->
		function errorValidate(){
			var searchTerm = document.getElementById("Text1").value;
				if(searchTerm == "" ||searchTerm==null || searchTerm == "Search Ship-To..."){
						document.getElementById("errorText").innerHTML  = "Please enter search criteria.";
						document.getElementById("errorText").setAttribute("class", "error");
				}
		}		
</script>
<script type="text/javascript">
  var valuesSet = null;
	var w = new Ext.Window({
    autoScroll: true,
    cls: 'swc-ext',
    closable: false,
    closeAction:'hide',
    contentEl: 'assignedShipToCustomersContent',
    hidden: true,
    id: 'assignedShipToCustomers',
    modal: true,
    width: '700',
    height: 'auto',
    resizable: true,
    shadow: 'drop',
    shadowOffset: 10,
    title: 'Select ship to:'
  	});      
    var ajaxValidationURL = "<s:url value='/common/validators/ajaxValidateJson.action'/>";
    loadDialog();
    function setCatShortDesc(path,displayName){
        path = document.getElementById(path);
        for(var i=0;i<path.options.length;i++){
            if((path.options[i].selected==true) ){
                var  catShortName= path.options[i].text;
                var  selectedCatPath= path.options[i].value;
                document.getElementById(displayName).value=catShortName;
            }
        }
    }
    function showAssignedShipTo(url)
    {	
    	//Added For Jira 2903
    	//Commented for 3475
    	//Ext.Msg.wait("Processing..."); 
    	var x = document.getElementById('ajax-assignedShipToCustomers');
        url = ReplaceAll(url,"&amp;",'&');
        "<s:url id='homeAction' action='home' namespace='/home' />";
        var logoutURL="<s:property value='%{#homeAction}' />";
        logoutURL = ReplaceAll(logoutURL,"&amp;",'&');
    	Ext.Ajax.request({
            url :url,
            params: {
            	status: "30"
            },
            method: 'POST',
            success: function (response, request){
			if(response.responseText.indexOf('Search Catalog...')!=-1 || response.responseText.indexOf('undefined')!=-1 || response.responseText == undefined){
    		window.location=logoutURL;
    		}
	        	document.getElementById('ajax-assignedShipToCustomers').innerHTML = response.responseText;
	        	Ext.Msg.hide();
       		},
       		failure: function (response, request){
       			if(response.responseText == undefined){
            		window.location=logoutURL;
            		}
           		else{
       				document.getElementById('ajax-assignedShipToCustomers').innerHTML = response.responseText;
           		}
       			Ext.Msg.hide();
             }
        });     
    }
    function showAssignedShipToForOrderSearch(url)
    {
    	var x = document.getElementById('shipToOrderSearchDiv');
    	valuesSet = null;
        x.innerHTML = "Loading data... please wait!";
    	Ext.Ajax.request({
            url :url,
            method: 'POST',
            success: function (response, request){
	        	document.getElementById('shipToOrderSearchDiv').innerHTML = response.responseText;
       		},
       		failure: function (response, request){
       			document.getElementById('shipToOrderSearchDiv').innerHTML = response.responseText;
             }
        });     
    }
    function updateCurrentCustomer(url,selectedRadioButton){
        var selectedCustomer = selectedRadioButton.value;
        document.FormToPost.selectedCustomerId.value=selectedCustomer;
    }
   
   // Added for JIRA 2770 
    function insertOptionBefore(num)
    {
      var elSel = document.getElementById('shipToSearchFieldName');
      
      if (elSel.selectedIndex >= 0) {
        var elOptNew = document.createElement('option');
        elOptNew.text =  num;
        elOptNew.value = num;
        var elOptOld = elSel.options[elSel.selectedIndex];  
        try {
          elSel.add(elOptNew, elOptOld); // standards compliant; doesn't work in IE
        }
        catch(ex) {
          elSel.add(elOptNew, elSel.selectedIndex); // IE only
        }
      }
    }
    function removeOptionSelected()
    {
      var elSel = document.getElementById('shipToSearchFieldName');
      var i;
      for (i = elSel.length - 1; i>1; i--) {         
        if (elSel.options[i].selected) {
          elSel.remove(i);
       }
      }
    }
    function removeOptionAll()
    {
      var elSel = document.getElementById('shipToSearchFieldName');
      var i;
      for (i = elSel.length - 1; i>1; i--) {                 
          elSel.remove(i);     
      }
    }        
    function appendOptionLast(num,formattedShipTo)
    {
      var elOptNew = document.createElement('option');
      elOptNew.text =  formattedShipTo;
      elOptNew.value =  num;
      var elSel = document.getElementById('shipToSearchFieldName');
      try {
        elSel.add(elOptNew, null); // standards compliant; doesn't work in IE
        elOptNew.selected=true;
        selectedShipCustomer = null;
      }
      catch(ex) {
        elSel.add(elOptNew); // IE only
        elOptNew.selected=true;
      }
    }
    function formatBillToShipToCustomer(customerId){
    	var custDetails = customerId.split("-");
		if(custDetails!=null && custDetails.length>3)
		{
			var division = custDetails[0];
			var customerNumber = custDetails[1];
			var suffix = custDetails[2];
			var sb = "";
			sb=sb + division + "-" + customerNumber + "-" + suffix;
			return sb;
		}
		//If the string cannot be properly split, return the customerId
		return customerId;
		}
    function setSelectedValue(selectedShipTo) {
        valuesSet = null;
	    if(selectedShipTo!= null) {
	        selectedShipCustomer = selectedShipTo.value;
	        /* if(selectedShipCustomer!=null) {	        	
	        	if(document.orderListForm) {
	        		removeOptionAll();
		        	var formattedShipTo = formatBillToShipToCustomer(selectedShipCustomer); 
	        		appendOptionLast(selectedShipCustomer,formattedShipTo);
	        		document.orderListForm.shipToSearchFieldName1.value = selectedShipCustomer;
	        	}
	        	if(document.approvalList) {
	        		removeOptionAll();
		        	var formattedShipTo = formatBillToShipToCustomer(selectedShipCustomer); 
	        		appendOptionLast(selectedShipCustomer,formattedShipTo);
	        		document.approvalList.shipToSearchFieldName1.value = selectedShipCustomer;
	        	}
	        } */
	    }
	}
    function clearShipToField() {
       	if(valuesSet !="selected"){      		
      		 valuesSet = null;
   	      	 var elSel = document.getElementById('shipToSearchFieldName');
     	     elSel.selectedIndex=0;
   		 if(document.orderListForm != null && document.orderListForm.shipToSearchFieldName1 != null) { 
 			document.orderListForm.shipToSearchFieldName1.value = elSel.selectedIndex ;
    	}
    	if(document.approvalList != null && document.approvalList.shipToSearchFieldName != null) {
    		document.approvalList.shipToSearchFieldName.value ='';
    	}
		var link = document.getElementById('shipToOrderSearch');
	}
    }
    
    function setVariable(defaultShipToValue, size){
    	valuesSet = "selected";
         if(selectedShipCustomer!=null) {
        	if(document.orderListForm) {
        		removeOptionAll();
	        	var formattedShipTo = formatBillToShipToCustomer(selectedShipCustomer); 
        		appendOptionLast(selectedShipCustomer,formattedShipTo);
        		document.orderListForm.shipToSearchFieldName1.value = selectedShipCustomer;
        	}
        	if(document.approvalList) { 
        		removeOptionAll();
	        	var formattedShipTo = formatBillToShipToCustomer(selectedShipCustomer); 
        		appendOptionLast(selectedShipCustomer,formattedShipTo);
        		document.approvalList.shipToSearchFieldName1.value = selectedShipCustomer;
        	}
        }
         else{
        	 if(document.orderListForm) {
 	        	var formattedShipTo = formatBillToShipToCustomer(defaultShipToValue); 
 	        	if(size > 20){
 	        	 valuesSet = null;
 	        	}else{
 	        	if(size < 20){
 	   		     removeOptionAll();
 	        	}
         		appendOptionLast(defaultShipToValue,formattedShipTo);
         		document.orderListForm.shipToSearchFieldName1.value = defaultShipToValue;
 	        	}
        	 }
         	if(document.approvalList) {
         		removeOptionAll();
         		removeOptionSelected();
 	        	var formattedShipTo = formatBillToShipToCustomer(defaultShipToValue); 
         		appendOptionLast(defaultShipToValue,formattedShipTo);
         		document.approvalList.shipToSearchFieldName1.value = defaultShipToValue;
         	}
         }
       
    }
    function isValidString(stringValue)
    {
        var numaric = stringValue;
        for(var j=0; j<numaric.length; j++)
        {
            var value1 = numaric.charAt(j);
            var hh = value1.charCodeAt(0);
            if((hh > 47 && hh<58) || (hh > 64 && hh<91) || (hh > 96 && hh<123))
            {
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }
    String.prototype.trim = function () {
        return this.replace(/^\s*/, "").replace(/\s*$/, "");
    }
        function isValidQuantity(component){
    	//valid size if 10,3(7 whole and 3 after digits)

    	//Fix for Qty not being decimal
    	var quantity = component.value.trim();
    	var qtyLen = quantity.length;
    	var validVals = "0123456789";
    	var char;
    		for (i = 0; i < qtyLen ; i++) {
    		char = quantity.charAt(i);
    			if (validVals.indexOf(char) == -1)
    			{
    			quantity = quantity.substr(0,i) ;
    			}
    		}
    	component.value = quantity;
    	/* if (!isValid){
    	component.value = quantity;
    	return false;
    	} */
    		if (quantity.length > 7){
    		var val = quantity.substr(0,7);
    		quantity = val;
    		component.value = quantity;
    		}
    	return true;
    	}
        var isApplyPreferredSelected = false;
	function applyPreferred(defaultShipTo,url) {
		if(defaultShipTo!='null' || defaultShipTo!= '') {
			document.FormToPost.selectedCustomerId.value = defaultShipTo;
			isApplyPreferredSelected = true;
			saveShipToChanges(url);
		}
	}
    function cancelShipToChanges(){
       	var isCustomerSelectedIntoConext="<s:property value='#isCustomerSelectedIntoConext'/>";
       	if(isCustomerSelectedIntoConext=="true"){
           	var customerId='<s:property value="%{#_action.getWCContext().getCustomerId()}"/>';
           	selectedCustomer=customerId;
           	$.fancybox.close();// JIRA - 694 
        }
       	else {
           	w.closable = false;
           	return; 
       	}
    }    
    function clearText() { 
    	Ext.fly('Text1').dom.value='';
   		 return; 
   		 } 
		
    function clearTxt() { 
    	 //Added for JIRA 3692 
    	if(Ext.fly('newSearch_searchTerm').dom.value =='Search Catalog...')
   		{
        	Ext.fly('newSearch_searchTerm').dom.value='';
   		}
        return;
   		 }     
    function saveShipToChanges(url)    
    {  
    	var radioCustomer=document.getElementsByName("selectedShipTo");
		var selected=false;
		if (isApplyPreferredSelected == false) {
			for(var i=0;i<radioCustomer.length;i++)
			{
				if(radioCustomer[i].checked == true)
					selected=true;				
			}
		} else {
			selected=true;
		}
		
    	if(selected == false){
            document.getElementById('errorText').innerHTML = "Please select a Ship-To.";
            document.getElementById('errorText').setAttribute("class", "error float-right");
        }        
        else{
    	//Added For Jira 2903
    	//Commented for 3475
    	//Ext.Msg.wait("Processing...");     	
        var selectedCustomer=document.FormToPost.selectedCustomerId.value;       
		// JIRA 1878 :  getting the userName of selected user, and sending as a parameter to Ajax request
        var selected_customerContactId=document.getElementById("customerContactId");
        if(selected_customerContactId)
        	selected_customerContactId = selected_customerContactId.value   ; 
        	 if(document.getElementById("setAsDefault")== null || document.getElementById("setAsDefault").checked == null || document.getElementById("setAsDefault") =='undefined' ){
        		 var setAsDefault= true;
        		 var checkboxChecked = setAsDefault;
             }
        	 else{
        		 var setAsDefault=document.getElementById("setAsDefault");
        		 var checkboxChecked = setAsDefault.checked;
        	 }
           
             
        if(selectedCustomer=='' || selectedCustomer==null){
        	var isCustomerSelectedIntoConext="<s:property value='#isCustomerSelectedIntoConext'/>";
        	if(isCustomerSelectedIntoConext=="true"){
            	var customerId='<s:property value="%{#_action.getWCContext().getCustomerId()}"/>';
            	selectedCustomer=customerId;
        	}else{
            	return;
        	}
        }                
        var xpedxSTName=document.FormToPost.xpedxSTName.value;
        var xpedxSTStreet=document.FormToPost.xpedxSTStreet.value;
        var xpedxSTAddressLine2=document.FormToPost.xpedxSTAddressLine2.value;
        var xpedxSTAddressLine3=document.FormToPost.xpedxSTAddressLine3.value;
        var xpedxSTCity=document.FormToPost.xpedxSTCity.value;
        var xpedxSTState=document.FormToPost.xpedxSTState.value;
        var xpedxSTZip=document.FormToPost.xpedxSTZip.value;

    	//var checkboxChecked = document.getElementById("setAsDefault").checked;
        if(selectedCustomer != null){
            document.body.style.cursor = 'wait';
            Ext.Ajax.request({
                url: url,
                params: {
                    selectedCurrentCustomer: selectedCustomer,
                    setSelectedAsDefault: checkboxChecked,
                    'xOverriddenShipToAddress.xpedxSTName':xpedxSTName,
                    'xOverriddenShipToAddress.xpedxSTStreet':xpedxSTStreet,
                    'xOverriddenShipToAddress.xpedxSTAddressLine2':xpedxSTAddressLine2,
                    'xOverriddenShipToAddress.xpedxSTAddressLine3':xpedxSTAddressLine3,
                    'xOverriddenShipToAddress.xpedxSTCity':xpedxSTCity,
                    'xOverriddenShipToAddress.xpedxSTState':xpedxSTState,
                    'xOverriddenShipToAddress.xpedxSTZip':xpedxSTZip,
                    selectedCustomerContactId:selected_customerContactId
                },
                method: 'POST',
                success: function (response, request){
                    document.body.style.cursor = 'default';
                  //added for jira 2440 - focus on AuthLoc tab
                    var pathname=window.location.pathname;
                    var selectedTab;
                    if(document.getElementById("selectedTab")!=null){
                    	selectedTab = document.getElementById("selectedTab").value;
                    }
                    if(pathname=="/swc/profile/user/MyUserProfile.action" && selectedTab=="1")
                    {
	                    var storeFrontId = '<s:property value="%{#_action.getWCContext().getStorefrontId()}"/>';
	                    var customerContactId = document.getElementById("customerContactId").value;
	                    var customerId = document.getElementById("customerId").value;
	                    var selectedTab=document.getElementById("selectedTab").value;
	                    var pathname=window.location.pathname;
	                    window.location.href="/swc/profile/user/MyUserProfile.action?sfId=" + storeFrontId + "&customerContactId="+ customerContactId +"&customerId=" + customerId +"&selectedTab="+ selectedTab +"&scFlag=Y";;
                    }
                    else if(pathname=="/swc/profile/user/MyUserProfile.action" && checkboxChecked == true)
                    {
                    	var storeFrontId = '<s:property value="%{#_action.getWCContext().getStorefrontId()}"/>';
 	                    var customerContactId = document.getElementById("customerContactId").value;
 	                    var customerId = document.getElementById("customerId").value;
 	                    var selectedTab=document.getElementById("selectedTab").value;
 	                    var pathname=window.location.pathname;
 	                    var success = true;
 	                    window.location.href="/swc/profile/user/MyUserProfile.action?sfId=" + storeFrontId + "&customerContactId="+ customerContactId +"&customerId=" + customerId +"&selectedTab="+ selectedTab +"&scFlag=Y"+ "&success="+ success;;	
                    }
                    else
                    {
	                    if(pathname=="/swc/myItems/MyItemsList.action"){                        
                          var headerUrl='<s:property value="#refreshCustomerIntoContext" />';
                          window.location.href = headerUrl ;
                          
                        }
                        else{
                        window.location.reload( true );
                        }
                    }
                    //end of jira 2440
                },
                failure: function (response, request){
                    document.body.style.cursor = 'default';
                }
            });
        }
        document.body.style.cursor = 'default';
      }
    }
    function editDetails()
    {   	
        document.getElementById('EditSelectedAddress').style.display = 'block'
    }
    function setSelectedAddressAsDefault(url)
    {
        var selectedCustomer=document.FormToPost.selectedCustomerId.value;		
    	if(selectedCustomer=='' || selectedCustomer==null){
        	return;
    	}
        document.body.style.cursor = 'wait';
        Ext.Ajax.request({
            url: url,
            method: 'POST',
            success: function (response, request){
                alert('Successfully set as default customer');
            },
            failure: function (response, request){
                document.body.style.cursor = 'default';
                return;
            }
        });
        document.body.style.cursor = 'default';
    }   
    function loadDialog(){
        var isguestuser = "<s:property value='%{wCContext.guestUser}'/>"; 
   	 	var assgnCustomerSize ='<s:property value="#assgnCustomers.size()"/>';           
        if(isguestuser!="true"){
//			Performance Fix - Removal of the mashup call of - XPEDXGetPaginatedCustomerAssignments
             var defaultShipTo = '<%=request.getAttribute("defaultShipTo")%>';
             var isCustomerSelectedIntoConext="<s:property value='#isCustomerSelectedIntoConext'/>";
            if(defaultShipTo=="" && assgnCustomerSize>0 && isCustomerSelectedIntoConext!="true"){
            	$('#shipToSelect,#shipToSelect1,#shipToSelect2').trigger('click');
//          Performance Fix - Removal of the mashup call of - XPEDXGetPaginatedCustomerAssignments
            }else if((defaultShipTo=="" || defaultShipTo=="null" || defaultShipTo==null)&& 
                    (assgnCustomerSize==0 || assgnCustomerSize==null) && isCustomerSelectedIntoConext!="true"){
             /*   $(function() {
                     $("#noassignedShipto").dialog({
                    		 disabled: true,
                 			resizable: false,
                 			height:140,
                 			modal: true,
                 			closeOnEscape: false,
                 			open: function(event, ui) { $(".ui-dialog-titlebar").hide(); }
                 			});
                }); commented for jira2881 */
            }
        }
    }
    function newSearch_searchTerm_onclick(){
    	if(Ext.fly('newSearch_searchTerm').dom.value =='Search Catalog...')
   		{
        	Ext.fly('newSearch_searchTerm').dom.value='';
   		}
    	else{}
        return;
    }
	// Added for removing the double quotes from an search term. Jira # 2415
	
	var myMask;
  function validateVal(e){
	//added for jira 3974
		var waitMsg = Ext.Msg.wait("Processing...");
		myMask = new Ext.LoadMask(Ext.getBody(), {msg:waitMsg});
		myMask.show();
	 	var searchText = document.getElementById("newSearch_searchTerm").value;
	 	
	 	/*while(searchText.indexOf("*")!= -1){
    	 	searchText = searchText.replace("*", " ");    	  
	 	}*/
		document.getElementById("goBackFlag").value = 'true';
	 	Ext.fly('newSearch_searchTerm').dom.value=searchText;	
}
  // End of Jira # 2415 
var toaWin = new Ext.Window({
        autoScroll: true,
        closeAction: 'hide',
        cloaseable: false,
        contentEl: 'termsOfAccessContent',
        hidden: true,
        id: 'termsOfAccess',
        modal: true,
        width: 1050,
        height: 'auto',
        resizable   : false,
        draggable   : false,
        closable    : false,
        shadow: 'drop',
        baseCls: 'swc-ext',
        shadowOffset: 10
      	}); 

var passwordUpdateWin = new Ext.Window({
    autoScroll: false,
    closeAction: 'hide',
    cloaseable: false,
    contentEl: 'passwordUpdateContent',
    hidden: true,
    id: 'passwordUpdateBox',
    modal: true,
    width: 750,
    height: 'auto',
    resizable   : false,
    draggable   : false,
    closable    : false,
    shadow: 'drop',
    baseCls: 'swc-ext',
    scrolling : 'no',
    shadowOffset: 10
  	}); 	
//JIRA 3487 start
var securityQuestionWin = new Ext.Window({
    autoScroll: false,
    closeAction: 'hide',
    cloaseable: false,
    contentEl: 'securityQueContent',
    hidden: true,
    id: 'securityQuestionBox',
    modal: true,
    width: 750,
    height: 'auto',
    resizable   : false,
    draggable   : false,
    closable    : false,
    shadow: 'drop',
    baseCls: 'swc-ext',
    scrolling : 'no',
    shadowOffset: 10
  	}); 	

var isGuestuser = "<s:property value='%{wCContext.guestUser}'/>";
var isTOAaccepted = '<s:property value="%{wCContext.getWCAttribute('isTOAaccepted')}"/>';
var secrectQuestionSet = '<s:property value="%{wCContext.getWCAttribute('setSecretQuestion')}"/>';
var passwordUpdateFlag = '<s:property value="%{wCContext.getWCAttribute('setPasswordUpdate')}"/>';
if((isGuestuser!="true")&& (isTOAaccepted == null || isTOAaccepted == "" || isTOAaccepted== "N")){
	loadTermsOfAccess();
	}


function passwordUpdateModal()
{
	
     		showPasswordUpdateDialog('<s:property value="#passwordUpdate"/>');
    
}
    function loadTermsOfAccess()
    {
		
				
		//	alert("isTOAaccepted="+isTOAaccepted);		
			
         		showTermsOfAccessDialog('<s:property value="#toaURL"/>');
	    
    }
    function toaSubmit(key){
    	 document.getElementById('toaChecked').value=key;
    	document.toaform.submit();
    
    }     

    function selectSecurityQuestionDialog(url){
		  Ext.get('ajax-securityQueContent').load({
	            url :url,
	            method: 'POST',
	            callback:function(el, success, response){
	                if(success)
	                {
	                	DialogPanel.show('securityQuestionBox');
	                	
	               }
	                else
	                {	
	                    alert("Error getting securityQuestionBox");
	                }
	            }
	        });     
    	//document.toaform.submit();
        }
    function showTermsOfAccessDialog(url)
    {
        Ext.get('ajax-termsOfAccessContent').load({
            url :url,
            method: 'POST',
            callback:function(el, success, response){
                if(success)
                {
                	DialogPanel.show('termsOfAccess');
               }
                else
                {	
                    alert("Error getting terms of access content");
                }
            }
        });     
    }

    function showPasswordUpdateDialog(url)
    {
        Ext.get('ajax-passwordUpdateContent').load({
            url :url,
            method: 'POST',
            callback:function(el, success, response){
                if(success)
                {
                	DialogPanel.show('passwordUpdateBox');
               }
                else
                {	
                    alert("Error getting password Update Box");
                }
            }
        });     
    }
	function showUsers(custID){
		document.getElementById('viewUsersDlg').innerHTML = '';
		var orderDesc=document.getElementById('orderDescending').value;
		var orderByAttribute=""
		var param;	
		if(orderDesc != "")
		{
			param={customerID : custID ,
					orderByAttribute:'FirstName',
					orderDesc:orderDesc};
		}
		else{
			param={customerID : custID}
		}
		Ext.Ajax.request({			
			url: '<s:property value="#userListURL" escape='false'/>',
			params: param,
			method: 'POST',
			success: function (response, request){
					document.getElementById('viewUsersDlg').innerHTML = response.responseText;
				},
			failure: function (response, request){
					document.getElementById('viewUsersDlg').innerHTML =
								"Failed to Load users for this particular Customer Try again Later"
				 }
		});
	}
	function setOrderBy(val){
		document.getElementById('orderDescending').value=val;
		showUsers('<s:property value="#CurrentCustomerId"/>');
		document.getElementById('orderDescending').value="";
	}
	function setSelectedUrl(contactId,customerId,storefrntId){
		
		var url='<s:property value="#xpedxManageOtherProfilesURL"/>';//"/swc/profile/user/MyManageOtherProfiles.action?sfId="+storefrntId+"&customerContactId="+contactId+"&customerId="+customerId;
		url = ReplaceAll(url,"&amp;",'&');
		url = url+"&customerContactId="+contactId+"&customerId="+customerId;
		document.getElementById("seletedUrl").value=url;
	}
	function selectedUser()
	{
		var url=document.getElementById("seletedUrl").value;
		if(url == "")
		{
			return false;
		}
		else{		
		document.selectUserForm.action = url;
    	document.selectUserForm.submit();
		}
	}
	function getCustomerAccounts(){
		//Init vars
	   var divId = "dlgSelectCustomerAccounts";	   
	   <s:url id='selectAccountURL' namespace='/profile/org' action='xpedxGetCustomerAccounts' />	
	    var url = '<s:property value="#selectAccountURL"/>';
	    url = ReplaceAll(url,"&amp;",'&');
	    //Show the waiting box
	    var x = document.getElementById(divId);
	    //Added For Jira 2903
	    //Commented for 3475
	    //Ext.Msg.wait("Processing..."); 
	    //Execute the call
	    document.body.style.cursor = 'wait';
	    if(true){
	          Ext.Ajax.request({
	            url: url,
	            method: 'POST',
	            success: function (response, request){
	                document.body.style.cursor = 'default';
	                setAndExecute(divId, response.responseText);
	                Ext.Msg.hide();
	            },
	            failure: function (response, request){
	                var x = document.getElementById(divId);
	                x.innerHTML = "";
	                alert('Failed to fecth the accounts');
	                document.body.style.cursor = 'default'; 
	                Ext.Msg.hide();
	            }
	        });     
	    }
		document.body.style.cursor = 'default';
	}
    $(document).ready(function(){
        
    	$("#cancelShoppingLink").fancybox({
			'autoDimensions'	: false,
			'width' 			: 300,
			'height' 			: 200  
		});
    	/* BB3 Select Ship-To */
    	$("#shipToOrderSearch").fancybox({
    		'onStart' 	: function(){
	    		if(isguestuser!="true") {
	    			 showAssignedShipToForOrderSearch('<s:property value="#shipToForOrderSearch"/>');
	    		}
	    		else {
		    		alert('Please Login to see the Assigned Customers');
		    		$fancybox.close();
	    		}
    			},
    			'onClosed' : function() {
   				 clearShipToField();      				
   			},
			'autoDimensions'	: false,
			'width' 			: 751,
	 		'height' 			: 350  
		});   	
		$('#cart-management-btn').click(function(){
			$('#cart-management-popup').toggle();
			return false;
		});
    	$("#welcome-address-info-icon").click(function(){
    		$('#welcome-address-popup').toggle();
    		return false;
    	});
		$("#welcome-address-popup-close").click(function(){
    		$('#welcome-address-popup').hide();
    		return false;
    	});     	
		try{
		$("#selectusertomodify").fancybox({
			'onStart' 	: function(){
			showUsers('<s:property value="#CurrentCustomerId"/>');
			},
			'autoDimensions'	: false,
			'width' 			: 780,
			'height' 			: 425  
		});
		}
		catch(e)
		{
			//Arun 3/3 Catalog and other pages are erroring when this box fails 
			//doing try catch on temporary basis
		}
        $(".btn-slide").click(function(){
                $("#panel").slideToggle("slow");
                $(this).toggleClass("slidetoggle"); return false;
        });
        var isguestuser = "<s:property value='%{wCContext.guestUser}'/>";
		var assgnCustomerSize ='<s:property value="#assgnCustomers.size()"/>';
		var isSalesRep = "<s:property value='%{wCContext.getSCUIContext().getSession().getAttribute("IS_SALES_REP")}'/>";
		if(isguestuser!="true"){
			var defaultShipTo = '<%=request.getParameter("defaultShipTo")%>';
			var isCustomerSelectedIntoConext="<s:property value='#isCustomerSelectedIntoConext'/>";
			var isDefaultShipToSuspended = "<s:property value='#isDefaultShipToSuspended'/>";
			if((!isSalesRep) && (passwordUpdateFlag == "true") && (isTOAaccepted== "Y")){
				var myMask
				var waitMsg = Ext.Msg.wait("");
				 myMask = new Ext.LoadMask(Ext.getBody(), {msg:waitMsg});
				 myMask.show();
				passwordUpdateModal();
				Ext.Msg.hide();
			} /* EB-76 Code Changes start  */
			else if((defaultShipTo == "" || defaultShipTo == "null") && isCustomerSelectedIntoConext =="true" && isDefaultShipToSuspended=="true" ){	
				//Please select an active ship-to as your default
				$("#shipToSelect,#shipToSelect1,#shipToSelect2").fancybox({
					'onStart' 	: function(){			    	  	        
			          	if(isguestuser!="true"){			               
			            	showAssignedShipTo('<s:property value="#targetURLForDefault"/>');
			 	        }
			 		},
			 		'onClosed'	: function(){			 			
			 			if(isguestuser!="true"){			 			
			 				 showAssignedShipTo('<s:property value="#targetURLForDefault"/>');
			 			}
			 		},
			 		'hideOnOverlayClick': false,
			 		'showCloseButton'	: false,
			 		'enableEscapeButton': false,
			 		'autoDimensions'	: false,
			 		'scrolling'   		: 'no',
			 		'width' 		: 750,
			 		'height' 		: 530  
				}).trigger('click');
			} /* EB-76 Code Changes End */
			else if((defaultShipTo == "" || defaultShipTo == "null") && isCustomerSelectedIntoConext!="true"){				
					$("#shipToSelect,#shipToSelect1,#shipToSelect2").fancybox({
					'onStart' 	: function(){
			    	  	var isguestuser = "<s:property value='%{wCContext.guestUser}'/>";            
			          	if(isguestuser!="true"){			               
			            	showAssignedShipTo('<s:property value="#targetURL"/>');
			 	        }
			 		},
			 		'onClosed'	: function(){
			 			var isguestuser = "<s:property value='%{wCContext.guestUser}'/>";
			 			if(isguestuser!="true"){
			 				 var defaultShipTo = '<%=request.getParameter("defaultShipTo")%>';
			 				 var isCustomerSelectedIntoConext="<s:property value='#isCustomerSelectedIntoConext'/>";
			 				if(defaultShipTo=="" && isCustomerSelectedIntoConext!="true"){
			 					 showAssignedShipTo('<s:property value="#targetURL"/>');
			 				}
			 			}
			 		},
			 		'hideOnOverlayClick': false,
			 		'showCloseButton'	: false,
			 		'enableEscapeButton': false,
			 		'autoDimensions'	: false,
			 		'scrolling'   		: 'no',
			 		'width' 		: 750,
			 		'height' 		: 530  
				}).trigger('click');
			} 
			else if((!isSalesRep) && (isTOAaccepted== "Y") && (secrectQuestionSet == null || secrectQuestionSet == "" || secrectQuestionSet== "N")){
		  		selectSecurityQuestionDialog('<s:property value="#securityQueURL"/>');
			}
		}		
        $("#shipToSelect,#contactUsShipTo,#shipToSelect1,#shipToSelect2").fancybox({
 			'onStart' 	: function(){
 	    	  	var isguestuser = "<s:property value='%{wCContext.guestUser}'/>";   
 	          	if(isguestuser!="true"){ 	                
 	                showAssignedShipTo('<s:property value="#targetURL"/>');
 	           }
 			},
 			'onClosed'	: function(){
 				var isguestuser = "<s:property value='%{wCContext.guestUser}'/>";
 				if(isguestuser!="true"){
 					 var defaultShipTo = '<%=request.getParameter("defaultShipTo")%>';
 					 var isCustomerSelectedIntoConext="<s:property value='#isCustomerSelectedIntoConext'/>";
 					if(defaultShipTo=="" && isCustomerSelectedIntoConext!="true"){
 						 showAssignedShipTo('<s:property value="#targetURL"/>');
 					}
 				}
 			},
 			'autoDimensions' : true,

 			'width' : '54%',

 			'scrolling' : 'no',

 			'autoscale' : false,

 			'height' : '95%'

 			});
        $("#shipToAnchor,#shipToAnchor1").fancybox({
 			'onStart' 	: function(){
 	    	  	var isguestuser = "<s:property value='%{wCContext.guestUser}'/>";            
 	          	if(isguestuser!="true"){
 	          		showAssignedShipTo('<s:property value="#targetURL"/>');
 	           }
 			},
 			'onClosed'	: function(){
 				var isguestuser = "<s:property value='%{wCContext.guestUser}'/>"; 				
 				if(isguestuser!="true"){
 					 var defaultShipTo = '<%=request.getParameter("defaultShipTo")%>';
 					 var isCustomerSelectedIntoConext="<s:property value='#isCustomerSelectedIntoConext'/>";
 					if(defaultShipTo=="" && isCustomerSelectedIntoConext!="true"){
 						 showAssignedShipTo('<s:property value="#targetURL"/>');
 					}
 				}
 			},
 			'autoDimensions'	: true,
 			'width' 			: 750,
 			'height' 			: 490 
 			});
        
        $("#dlgSelectAccount").fancybox({
			'onStart' 	: function(){
        		getCustomerAccounts();
			},
			'autoDimensions'	: false,
			'width' 			: 450,
			'height' 			: 350  
		});
 });
</script>
<script type="text/javascript">
function searchShipToAddress(divId,url) {
    // look for window.event in case event isn't passed in
    	var searchText = document.getElementById('Text1').value
    	var suspendedStatus ="";
    	if(divId == null)
			divId = 'ajax-assignedShipToCustomers';
    	if(divId == 'shipToOrderSearchDiv' && searchText == '')
    		url = '<s:property value="#shipToForOrderSearch"/>';
   		if(divId == 'shipToOrderSearchDiv' && searchText != '')                                  
   			url = '<s:property value="#shipToSearchForOrderList"/>';
		if(divId == 'shipToUserProfile' && searchText == ''){
		  	url = '<s:property value="#shipToForUserProfileSearch"/>';
		  	suspendedStatus= "30";
		}
		if(divId == 'shipToUserProfile' && searchText != ''){                                  
		 	url = '<s:property value="#shipToSearchForUserProfile"/>';
		 	suspendedStatus= "30";
		}
		if(divId == 'showShipToLocationsDiv' && searchText == '')
			url = '<s:property value="#showLocationsDivForReportingSearch"/>'
		if(divId == 'showShipToLocationsDiv' && searchText != '')                                  
   			url = '<s:property value="#shipToSearchForReporting"/>';
		if(url == null) {
			url = '<s:property value="%{searchURL}"/>';
			suspendedStatus= "30";
		}
/* 		Performance Fix - Removal of the mashup call of - XPEDXGetPaginatedCustomerAssignments
		if(searchText==''|| searchText==null)
*/
		/* JIRA 3331 if search text is blank then all ship tos should be shown
		if(searchText==''|| searchText==null || searchText=='Search Ship-Toï¿½')
		{
			document.getElementById('errorText').innerHTML= "Please enter search criteria.";
			document.getElementById('errorText').setAttribute("class", "error");
		}		
		else
		{*/
			
			var x = document.getElementById('address-list');
	         x.innerHTML = "Loading data... please wait!";
			 Ext.Ajax.request({
	                url: url,
	                params: {
						searchTerm : searchText,
						status: suspendedStatus
			 		},
		            method: 'POST',
		            success: function (response, request){
		                 document.getElementById(divId).innerHTML = '';
		                 document.getElementById(divId).innerHTML = response.responseText;
		             },
		             failure: function (response, request){
		            	 document.getElementById(divId).innerHTML = '';
		            	 document.getElementById(divId).innerHTML = response.responseText;
		             }
		        });
		//}
    }
function callAjaxForPagination(url,divId)
{
	var custIdString = 'customerId';
	var customerIdIndex = url.indexOf(custIdString);
	var customerId;
	var isCustomerSelected;
	if(customerIdIndex != -1) {
		var customerIdEndIndex = url.indexOf('&',customerIdIndex);
		customerId = url.substring(customerIdIndex+custIdString.length+1,customerIdEndIndex);
		var controlId = customerId.replace(/-/g, '_');
		if(document.getElementById('customerPaths_'+controlId)!=null){
           isCustomerSelected = document.getElementById('customerPaths_'+controlId).checked;
	  	}
	}
	if(document.getElementById(divId) !=null){
        document.getElementById(divId).innerHTML = "Loading.....Please Wait";
   	}
   	var selAssignments = new Array();
	if(document.getElementById('customersTwo')) {		
		if(document.getElementById('customersTwo')!=null) {
			var lboTo=document.myAccount.customers2;
			for ( var i=0; i < lboTo.options.length; i++ )
			{
				selAssignments.push(lboTo.options[i].value)
			}
		}
	}
	document.body.style.cursor = 'wait';
	Ext.Ajax.request({
        url: url,
        params : {
			alreadySelectedCustomers : selAssignments
		},
        method: 'POST',
        success: function (response, request){
	        if(document.getElementById(divId) !=null){
	             if(divId.indexOf("divShareList_")!= -1){
		             setAndExecute(divId, response.responseText, isCustomerSelected);
		             document.body.style.cursor = 'default';
	             }
	             else {
	            	 document.getElementById(divId).innerHTML = response.responseText;
	            	 document.body.style.cursor = 'default';
	             }
	        }
         },
         failure: function (response, request){
        	 if(document.getElementById(divId) !=null){
        	 	document.getElementById(divId).innerHTML ="Failed to load page!";
        	 	document.body.style.cursor = 'default';
        	 }
         }
    });
}

//JIRA 2736
function callAjaxForSorting(url,divId)
{	
	if(document.getElementById(divId) !=null){
        document.getElementById(divId).innerHTML = "Loading.....Please Wait";
   	}    		
   	var orderDesc="Y";
		var orderByAttribute=""
		var param;					
	url = ReplaceAll(url,"&amp;",'&');	
         				Ext.Ajax.request({
	         		        url:url,
	         		        params: param,
	         		        method: 'POST',
	         		        success: function (response, request){
	         		             document.getElementById('viewUsersDlg').innerHTML = response.responseText;	         		            
	         		         },
	         		         failure: function (response, request){
	         		         }
	         		    });
}
</script>
<script type="text/javascript">
	function debug(msg){
		try{
			console.debug(msg);
		}catch(ex){
		}
	}	
	function checkAll(id, value){
		try{
			var checkboxes = Ext.query('input[id*='+id+']');
			Ext.each(checkboxes, function(obj_item){
				obj_item.checked = value;
				debug("checkAll " + id + ", value: " + value);
			});
		}catch(ex){
		}
	}
	function checkIfControlIdInMap(id) {
		var custPath = customerPathMap[id];
		var parentCustomer;
		var isInMap = false;
		if(custPath != null){
			var customerHierarchy = custPath.split("|");
			if(customerHierarchy.length >= 2) {
				parentCustomer = customerHierarchy[customerHierarchy.length-2];
				parentCustomer = parentCustomer.replace(/-/g, '_');
			}
			else if(customerHierarchy.length == 1) {
				parentCustomer = 'MSAP';
			}
			var preSelectedCustomers = selectedCustomerMap[parentCustomer];
			if(preSelectedCustomers!=null){
				for(var i=0; i<preSelectedCustomers.length; i++) {
					var selCustomer = preSelectedCustomers[i];
					if(selCustomer == id) {
						isInMap = true;
					}
				}
			}
		}
		else {
			try{
				console.log("Error Error !! There is no Path in the customerPathMap for the id " + id);
				return isInMap; 
			}catch(ex){
				return isInMap;
				}
		}
		return isInMap;
	}
	function createCountAndPathMap(id,path,childCount,custDiv) {
		if(customerPathMap[id] == null) {
			customerPathMap[id] = path;
		}
		if($.inArray(id, customersArray) == -1) {
			customersArray[customersArray.length] = id;
		}		
		if(childCount!=null) {
			var custPath = customerPathMap[id];
			var parentCustomer;
			if(custPath != null){
				var customerHierarchy = custPath.split("|");
				if(customerHierarchy.length >= 2) {
					parentCustomer = customerHierarchy[customerHierarchy.length-2];
					parentCustomer = parentCustomer.replace(/-/g, '_');
				}
				else if(customerHierarchy.length == 1) {
					parentCustomer = 'MSAP';
				}
			}
			if(customerChildCountMap[parentCustomer] == null) {
				customerChildCountMap[parentCustomer] = childCount;
			}
		}
		if(custDiv!=null){
			if(customerDivMap[id]==null)
				customerDivMap[id] = custDiv;
		}
	}
	function preSelected(id) {
		var custPath = customerPathMap[id];
		var parentCustomer;
		if(custPath != null){
			var customerHierarchy = custPath.split("|");
			if(customerHierarchy.length >= 2) {
				parentCustomer = customerHierarchy[customerHierarchy.length-2];
				parentCustomer = parentCustomer.replace(/-/g, '_');
			}
			else if(customerHierarchy.length == 1) {
				parentCustomer = 'MSAP';
			}
			var preSelectedCustomers = selectedCustomerMap[parentCustomer];
			if(preSelectedCustomers!=null){
				for(var i=0; i<preSelectedCustomers.length; i++) {
					var selCustomer = preSelectedCustomers[i];
					if(selCustomer == id) {
						var fieldId = 'customerPaths_'+id;
						var custCheckbox = $("input[id="+fieldId+"] ");
						custCheckbox.attr('checked','checked');
						selectNode(id, true);
					}
				}
			}
		}
		else {
			try{
				console.log("Error pre selecting the selected node: in method preSelected with id " + id); 
			}catch(ex){}
		}
	}
	function updateSelectedCustomersMap(id,val) {
		var customerPath = customerPathMap[id];
		// adding or removing the id to or from the parents selected map
		if(customerPath!=null) {
			var customerHierarchy = customerPath.split("|");
			var parentCustomer;
			if(customerHierarchy.length >= 2) {
				parentCustomer = customerHierarchy[customerHierarchy.length-2];
				parentCustomer = parentCustomer.replace(/-/g, '_');
			}
			else if(customerHierarchy.length == 1) {
				parentCustomer = 'MSAP';
			}
			var selectedCustomers = selectedCustomerMap[parentCustomer];
			if(val == true) {
				if(selectedCustomers == null){
					var newSelectedCustomers = new Array();
					newSelectedCustomers[0]= id;
					selectedCustomerMap[parentCustomer] = newSelectedCustomers;
				}
				else {
					var isAlreadyInSelected = false;
					for(var k=0;k<selectedCustomers.length;k++) {
						if(selectedCustomers[k]==id) {
							isAlreadyInSelected = true;
						}
					}
					if(isAlreadyInSelected == false) {
						selectedCustomers[selectedCustomers.length] = id;
						selectedCustomerMap[parentCustomer] = selectedCustomers;
					}
				}
			}	
			if(val == false && selectedCustomers != null) {
				for(var j=0; j<selectedCustomers.length; j++) {
					if(selectedCustomers[j]==id) {
						selectedCustomers.splice(j,1);
					}
				}
				selectedCustomerMap[parentCustomer] = selectedCustomers;
			}
		}		
	}
	function selectNode(id, val){		
		try{
			debug("selectNode - BEGIN for " + id + ", value: " + val); 
			//change the hidden checkbox selections
			checkAll("customerIds_" + id, val);
			checkAll("customerDivs_" + id, val);
			// update the selected customers map
			updateSelectedCustomersMap(id, val);			
			//Krithika's Code Start
			//changing the selection of all the childs of this Checkbox.(suppose B1 is selected, select all the Shiptos under that)
			var divId = $("input[id=customerPaths_"+id + "] ").parent().attr("id");
			var allChildElements = $("div[id="+divId + "] :checkbox");			
			//Get the selected child customers from other pages
			var selectedCustomers = selectedCustomerMap[id];
	        var currentDiv = $("input[id=customerPaths_"+id + "] ");			
			var currentDivParents = currentDiv.parents("div[id^=divShareList]");
			debug("selectNode - END for " + id + ", value: " + val);
		}catch(e){
			try{ console.log("Error selectin the node: " + e, e); }catch(ex){}
		}
	}	
	function createHiddenInputsForSelectedCustomers(xForm) {
		if (xForm == undefined || xForm == null || xForm == ""){
			return;
		}else {
			var form = Ext.get(xForm);
			var hiddenDiv = document.createElement('div');
			hiddenDiv.style.display = "none";
			for(var i=0; i<customersArray.length; i++) {
				var controlId = customersArray[i];
				var custPath = customerPathMap[controlId];
				var isSelected = checkIfControlIdInMap(controlId);
				var custDiv = customerDivMap[controlId]; 
				var custId = ReplaceAll(controlId,"_",'-');;
				if(isSelected) {
					//getting the corresponding checkbox of the document
					var checkBoxofCtrlId = document.getElementById('customerPaths_'+controlId);
					//checking if the document already has the checkbox, if not there then creating a hidden checkbox
					if(checkBoxofCtrlId == null) {
						//creating customerPath checkbox
						var checkbox_path = document.createElement('input');
						checkbox_path.type = "checkbox";
						checkbox_path.name = "customerPaths";
						checkbox_path.value = custPath;
						checkbox_path.id = "customerPaths_"+controlId;
						checkbox_path.checked = true;
						//creating customerIds checkbox
						var checkbox_id = document.createElement('input');
						checkbox_id.type = "checkbox";
						checkbox_id.name = "customerIds";
						checkbox_id.value = custId;
						checkbox_id.id = "customerIds_"+controlId;
						checkbox_id.checked = true;
						//creating customerDivs checkbox
						var checkbox_div = document.createElement('input');
						checkbox_div.type = "checkbox";
						checkbox_div.name = "customerDivs";
						checkbox_div.value = custDiv;
						checkbox_div.id = "customerDivs_"+controlId;
						checkbox_div.checked = true;
						hiddenDiv.appendChild(checkbox_path);
						hiddenDiv.appendChild(checkbox_id);
						hiddenDiv.appendChild(checkbox_div);											
					}
				}
			}
			form.appendChild(hiddenDiv);
		}
	}
	function hideSharedListHLForm(){
		document.getElementById("dynamiccontentHL").style.display = "none";
		if(document.getElementById("shareAdminOnlyHL")!=null)
		document.getElementById("shareAdminOnlyHL").style.display = "none";
		document.getElementById("errorMsgForAddressFieldsHL").innerHTML ="";
		document.getElementById("errorMsgForAddressFieldsHL").style.display = "none";
	} 	
  	function showSharedListHLForm(){
  		var dlgForm 		= document.getElementById("dynamiccontentHL");
		if (dlgForm){
  			dlgForm.style.display = "block";
  			if(document.getElementById("shareAdminOnlyHL")!=null)
  			document.getElementById("shareAdminOnlyHL").style.display = "";
  		}
  	}  	
  	function openHelp() {
  		 //-- Web Trends tag start --
		writeMetaTag('WT.ti','Help');
		//-- Web Trends tag End --
  		var load = window.open('https://content.ipaper.com/storefront/<s:property value="wCContext.storefrontId" />_help.html','','menubar=no,height=600,width=800,resizable=yes,toolbar=no,location=no,status=no');
  		}
	function getCategorySubMenu()
	{		
		   	var url = "<s:property value='#getCategoryMenu'/>";
   			url = ReplaceAll(url,"&amp;",'&');
       		Ext.Ajax.request({
		       	url:url,
    		   	success: function (response, request)
       			{
	    			var myDiv = document.getElementById("categorySubMenu");
    				myDiv.innerHTML = response.responseText;
    			}
			});
	}	
	

	//check for timeout for JIRA 1650 
function checkSessionTimeout(){
	<s:url id='homeAction' action='home' namespace='/home' />;
    var logoutURL="<s:property value='#homeAction' />";
    logoutURL = ReplaceAll(logoutURL,"&amp;",'&');
	<s:url id='checkSesseionTimeoutURL'  namespace='/order'  action='checkSessionTimeoutForCart.action' ></s:url>
     var checkSesseionTimeoutURL="<s:property value='#checkSesseionTimeoutURL' />";
     checkSesseionTimeoutURL = ReplaceAll(checkSesseionTimeoutURL,"&amp;",'&');
 	Ext.Ajax.request({
         url :checkSesseionTimeoutURL,
         method: 'POST',
         success: function (response, request){
 		if(response.responseText == undefined || response.responseText.indexOf('Search Catalog...')!=-1 ){
		window.location=logoutURL;
		}
 		
 		
    		},
    		failure: function (response, request){
    			if(response.responseText == undefined || response.responseText.indexOf('Search Catalog...')!=-1 ){
    			window.location=logoutURL;
    			}
          }
     });  
}
//added for XBT 298
var myMask;
function msgWait(){
		var waitMsg = Ext.Msg.wait("Processing...");
		myMask = new Ext.LoadMask(Ext.getBody(), {msg:waitMsg});
		myMask.show();
	}
//end for XBT 298

</script>
<!-- WebTrends tag start -->
<s:if test='%{#xpedxCustomerContactInfoBean.getUsergroupKeyList() != null && #xpedxCustomerContactInfoBean.getUsergroupKeyListActive() == true}'>	
<s:set name="firstTimeFlag" value="%{wCContext.getWCAttribute('firstTimeFlag')}"/>
	<s:if test='%{#firstTimeFlag != null}'>
		<s:set name="userGroupKey" value ="%{#_action.getUserTypeForWebtrend()}"/>	
		
           		<meta name="DCSext.w_x_ut" content='<s:property value="#userGroupKey"/>' />
          
		<s:set name="firstTimeFlag" value="<s:property value=null />" scope="session"/> 
		
	</s:if>	
</s:if>
<!-- WebTrends tag end -->
<style>
<!-- Style for ship to address hover-->
	#viewAll a:hover { text-decoration:underline; } 	
	a:focus {
	       outline: none;
	}
	#panel {
	       height: 200px; width:200px; left:-23px; top:-47px; 
	       display: none; z-index:10000; position:relative; background-image:url(<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/customer-block-hover.png); background-repeat:no-repeat;
	}
	.slide {
	       margin: 0;
	       padding: 0;
	}
	.btn-slide {
	       width: 144px;
	       height: 31px;
	       display: block;
	       text-decoration: none; outline:none;
	}
	.active2 { top:-125px;
	}
.share-modal { /*width:399px!important; height:37	0px;*/}         
.indent-tree { margin-left:15px; }       
.indent-tree-act { margin-left:25px; } 	
</style>
<s:set name='isProcurementInspectMode'
	value='#hUtil.isProcurementInspectMode(wCContext)' />
<s:set name='isGuestUser' value="wCContext.guestUser" />
<s:set name="selectedHeaderTab" value="#_action.getSelectedHeaderTab()"/>
<s:set name='isThereAUser' value="wCContext.thereAUser" />
<noscript>
<div class="noScript"><s:text name='NoScriptWarning' /></div>
</noscript>
<!-- begin t1-header -->
<!-- <div id="noassignedShipto" style="display:none;color:red;">There are no shipTo locations assigned for your profile, Please contact administrator..</div> commented for jira2881-->
<!-- WebTrends tag start -->
<s:if test='(#isGuestUser != true)'>
	<s:if test="%{wCContext.getWCAttribute('lastLoginDate')==null || wCContext.getWCAttribute('lastLoginDate')==''}" >
	<meta name="DCSext.w_x_ul" content='1' />
	</s:if>
</s:if>
<!-- WebTrends tag End -->
<s:if test='(#isGuestUser == true)'>
<div class="t1-header commonHeader signOnHeader" id="headerContainer" >
</s:if>
<s:else>
<s:include value="../modals/XPEDXItemCopyModal.jsp"></s:include>
<div class="t1-header commonHeader" id="headerContainer"> 
</s:else>
  <!-- add content here for header information -->
	<s:url id ='homeLink' action='home' namespace='/home' /> 
	<div class="logo">
		<s:a href="%{homeLink}">&nbsp;</s:a>
	</div> 
  <!-- Edit: IW-DH -->
 <s:if test='(isThereAUser == true) || (#isGuestUser != true)'>   	
    <!-- Close Customer Block -->   
	  <s:url id='newCatSearch' action='newSearch' namespace='/catalog' >
	  </s:url>
	  <div  class="searchbox-1 auth">
	  <!-- XBT-391 Removed submit event from Submit button and added to form -->
	   <s:form name='newSearch' action='newSearch' namespace='/catalog' onSubmit="newSearch_searchTerm_onclick();validateVal(event);return;">
	   		<s:hidden name='path' id='path' value="/" />
	   		<!-- XBT-391 removed the onkeydown event -->
			<input name="searchTerm" tabindex="2012" id="newSearch_searchTerm" class="searchTermBox" 
	        	type="text" value="Search Catalog..." onclick="clearTxt();" >
			<button type="submit" id="newSearch_0" value="Submit" class="searchButton" title="Search" tabindex="2013" 
	                            ></button>
		    <div id="tips-container">
		    	 <a class="whitest underlink" id="inline" href="#searchTips"> Search Tips </a>
		    </div>
	    </s:form>
	    <!-- END wctheme.form-close.ftl --> 
	  </div>
 </s:if> 
 <s:if test='(#isGuestUser == true)'> 
	<div  class="searchbox-1">
	<!-- XBT-391 Removed submit event from Submit button and added to form -->
	  <s:form name='newSearch' action='newSearch' namespace='/catalog' onSubmit="newSearch_searchTerm_onclick();validateVal(event);return;">
	  		<s:hidden name='path' id='path' value="/" />
	  		<!-- XBT-391 removed the onkeydown event -->
		<input name="searchTerm" tabindex="2012" id="newSearch_searchTerm" class="searchTermBox" 
	         type="text" value="Search Catalog..." onclick="clearTxt();" >
		<button type="submit" id="newSearch_0" value="Submit" class="searchButton"  title="Search"  tabindex="2013" 
	            style="top:-4px;margin-left: 3px;height: 20px;"></button>
	     <div id="tips-container">
		    	 <a class="whitest underlink" id="inline" href="#searchTips"> Search Tips </a>
		</div> 
	   </s:form>
	 </div>
 </s:if>   
  <s:if test='(isThereAUser == true) || (#isGuestUser != true)'>
  	<%-- Minicart --%>
	<s:set name='shoppingCartResId' value='"/swc/order/guestShopping"' />
	<s:if
		test="@com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer@getInstance().isAuthorized(#shoppingCartResId,wCContext)">	
	<%--Hemantha --%>	   	
 <s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">	          	
    <div class="searchbox-2">
    <s:url id='XPEDXMiniCartLinkDisplayURL'  namespace='/order'  action='XPEDXMiniCartLinkDisplay.action' ></s:url>

			 
			<a  class="underlink"  id="miniCartMouseoverArea2" tabindex="2015" href="javascript:callCartDetails('<s:property value="%{#_action.getWCContext().getStorefrontId()}"/>');">
				<img id="whitecart" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/16x16_white_cart.png" alt="" style="display:block;float:left;" />
			</a> 			
		<s:url id='XPEDXMiniCartLinkDisplayURL'  namespace='/order'  action='XPEDXMiniCartLinkDisplay.action' ></s:url>
    	<s:set name='sessionOrderHeaderKey' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("OrderHeaderInContext")'/>
    	<span id="miniCartLinkDiv" class="float-right">
    		
    		<s:if test='#sessionOrderHeaderKey == null'>
    		<div id="progressDiv"> Processing ...</div>
    	   <a  class="underlink"  href="javaScript:callCartDetails('<s:property value="%{#_action.getWCContext().getStorefrontId()}"/>');"  id="miniCartMouseoverArea1" tabindex="2015">
   			
		    		<div id ="XPEDXMiniCartLinkDisplayDiv">	
					</div>
			    	<script>
				    		Ext.onReady(function(){				    				
					    			refreshMiniCartLink();				
					    		});
							</script>
					</a>
	    		</s:if>
	    		<s:else>
	    		
		    		 <a  class="underlink" href="javaScript:callCartDetails('<s:property value="%{#_action.getWCContext().getStorefrontId()}"/>');" id="miniCartMouseoverArea1" tabindex="2015">	    			
		    			<div id ="XPEDXMiniCartLinkDisplayDiv">
		    				<s:include value="/xpedx/jsp/order/XPEDXMiniCartLink.jsp"/>
						</div>
					</a>
	    		</s:else>
    		
			</span>			
	</div>
</s:if>
</s:if>
  </s:if>
  <ul class="header-subnav commonHeader-subnav">
	  	<s:if test="%{!#isProcurementUser}">
			 <s:if test='#isGuestUser != true' >
			 <s:set name='storefrontId' value="wCContext.storefrontId" />
				<s:if test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_STORE_FRONT.equals(#storefrontId)}'>			 	
				<li style="border-right: none;"><a href="javascript:void(0);" tabindex="2000" onClick="javascript:openHelp();">Help</a></li>
				</s:if>
				<s:elseif test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT.equals(#storefrontId)}'></s:elseif>
				<li> | </li>
				<li><a
					href="<s:url action="logout" namespace="/home" includeParams='none'><s:param name='sfId' value='wCContext.storefrontId'/></s:url>"
					tabindex="2006">Sign Out</a></li>									
				<!-- Open Welcome Message Block -->
				<li> | </li>
				<li class="text-right pointers" id="welcome-address-info-icon">
			    	<s:set name='customerId' value="wCContext.customerId" />
					<s:set name='storeFrontId' value="wCContext.storefrontId" />
					<s:set name="defualtShipTAddress" value="#_action.getShipToAddress()" />
					<s:set name="shipToCustomerDisplayStr" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@formatBillToShipToCustomer(#customerId)" />
					<s:if test="%{#isSalesRep}">
						<s:if test='%{#session.loggedInUserName != null}'>		
							Welcome <s:property value='%{#session.loggedInUserName}'/><s:if test='{#welcomeUserShipToName != null}'>,<s:property value='welcomeUserShipToName'/>
							</s:if>
							
				<s:if test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_STORE_FRONT.equals(#storeFrontId)}'>
							<img  src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_white_down.png" alt="" />	
							</s:if>
				<s:elseif test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT.equals(#storeFrontId)}'>
				<img  src="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/images/12x12_green_down.png" alt="" />	
				</s:elseif>					
						</s:if>
					</s:if>
					<s:else>
						Welcome <s:property value='welcomeUserFirstName'/> <s:property value='welcomeUserLastName'/><s:if test='{#welcomeUserShipToName != null}'>, <s:property value='welcomeUserShipToName'/>
							</s:if>
						<s:if test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_STORE_FRONT.equals(#storeFrontId)}'>
							<img  src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_white_down.png" alt="" />	
							</s:if>
				<s:elseif test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT.equals(#storeFrontId)}'>
				<img  src="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/images/12x12_green_down.png" alt="" />	
				</s:elseif>			
					</s:else>										
					<!--  Drop down fields  -->
					<div id="welcome-address-popup" style="display: none;">
						 <s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
	         				Shopping for : 
	       				</s:if>
	       				<s:else>
	         				Orders for :
	      				 </s:else>	        
						<a href="#" id="welcome-address-popup-close"><img title="Close" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_charcoal_x.png" alt="[close]" /></a>
						<s:if test="#isEditOrderHeaderKey == null ">
							 <s:if test="%{#fromWhichPage == 'checkout'}">
								<a href="#viewPrvenetChangeShipToDlg" id="preventChangeShipTo">[Change]</a>
							</s:if>
							<s:else>
								<a href="#ajax-assignedShipToCustomers" id="shipToSelect">[Change]</a>
							</s:else>
						</s:if>						
						<br/> 
					  	<s:property value='loggerInUserCustomerName'/> (<s:property value='#shipToCustomerDisplayStr'/>)<br/>  																	
						 <s:if test="{#defualtShipTAddress.getLocationID()!=null && #defualtShipTAddress.getLocationID().trim().length() > 0}">
							Local ID: <s:property value='#defualtShipTAddress.getLocationID()'/><br/>
						</s:if>						
						<s:iterator value="#defualtShipTAddress.getAddressList()" id='addressline' >
							<s:if test='#addressline.length() > 30'>
							<s:set name='addressline' value='%{#addressline.substring(0,30)}'/>
								<s:property value='addressline'/><br/>
							</s:if>
							<s:else>
								<s:if test="%{#addressline.trim() != ''}">
								<s:property value='addressline'/><br/>
								</s:if>
							</s:else>
					    </s:iterator>
					    <s:if test="{#defualtShipTAddress.getCityCode()!=''}">
							<s:property value="#defualtShipTAddress.getCityCode()"/>,<s:property value=" "/>
						</s:if>
						<s:if test="{#defualtShipTAddress.getState()!=''}">
							<s:property value="#defualtShipTAddress.getState()"/><s:property value=" "/>
						</s:if>
						<s:if test="{#defualtShipTAddress.getZipCode()!=''}">
							<s:property value="%{@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedZipCode(#defualtShipTAddress.getZipCode())}" /><s:property value=" "/>
						</s:if>
						<s:if test="{#defualtShipTAddress.getCountry()!=''}">
							<s:property value="#defualtShipTAddress.getCountry()"/>
						</s:if>
					    <br/>
					</div>
		        </li>
				<!-- Close Welcome Message Block -->
			</s:if>
			<s:else>
				<li style="border-right: none;">&nbsp;</li>
				<li> &nbsp;&nbsp;&nbsp;&nbsp; </li>
				<li><a
					href="<s:url action="loginFullPage" namespace="/home" includeParams='none'><s:param name='sfId' value='wCContext.storefrontId'/></s:url>"
					tabindex="2006" id="signIn"><span style="font-size: 12px">Sign In</span></a>
				</li>
				<s:set name='storeFront' value='wCContext.storefrontId'/>
				<s:if test="#storeFront == 'xpedx'">
					
					<div id="signId"></div>
				
				<%-- eb-3067: note the use of '*' on margin-right property. this is an IE7 hack: http://net.tutsplus.com/tutorials/html-css-techniques/quick-tip-how-to-target-ie6-ie7-and-ie8-uniquely-with-4-characters/ --%>
				<div class="float-right" style="margin-top:25px;margin-right:-60px; *margin-right:-20px;"><a href="https://www.xpedx.com/contact-us.aspx" target="_blank"><img border="0" alt="" width="120" height="40" top="15" position="absolute" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/ster/images/888xpedx76.png"></a></div>
				</s:if>
			</s:else>
	   	</s:if>
	   	<s:else>
	   		<li><a href="<s:url action="getUserInfo" namespace="/profile/user" includeParams='none'/>"
					tabindex="2004">My Account</a></li>
			<li><a id="cancelShoppingLink" href="#cancelShopping"
					tabindex="2006">Cancel Shopping</a></li>
	   	</s:else>   
  </ul>
  <s:set name='loggedInUserCustomerID' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getLoggedInCustomerFromSession(wCContext)'/>
  <s:set name="loggedInUserOrgCode"  value='wCContext.storefrontId'/>						
	<%-- Get the SAPs under the MSAP and show the Select Account pop-up if there is more than 1 SAP entitled with the MSAP --%>	       		
	<s:set name="custInfoMap" value="#session.loggedInFormattedCustomerIDMap" />
	<s:set name="custInfoMapKey" value="%{#loggedInUserCustomerID + '_' + #loggedInUserOrgCode}" />	
	<s:set name="sapCustSize" value='%{0}' />	
	<s:if test='#custInfoMap!=null'>
		<s:set name='map' value='%{#custInfoMap.get(#custInfoMapKey)}' />
		<s:if test='#map!=null && #map.size>0'>
			<s:iterator value="#map" id="currentCust">
				<s:if test="key!= 'SAPParentAccNo'">
					<s:set name="sapCustSize" value='%{#sapCustSize + 1}' />
				</s:if>
			</s:iterator>
		</s:if>
	</s:if>
  <s:set name="sapCustomerId" value='%{""}' />					
  <s:if test='#sapCustSize > 1' >					
  <!-- Modal to show multiple sap accounts -->
	<s:include value="../modals/XPEDXSelectAccountModal.jsp"></s:include>
  <!-- Modal to show multiple sap accounts -->
  </s:if>
  <s:elseif test='#sapCustSize == 1' >
  <!-- One sap account -->
  		<s:iterator value="#map" id="currentCustomer">
				<s:if test="key!= 'SAPParentAccNo'">
					<s:set name="sapCustomerId" value='%{key}' />
				</s:if>
			</s:iterator>
  </s:elseif>
<s:if test='(#isGuestUser != true)'>
	<div id="main-navigation">
	        <ul class="dropdown" id="main-nav">
	            <%-- Hemantha, removed since it is not in new UI screen --%>	            
	            	<s:url id='allCatURL' namespace='/catalog' action='navigate.action'>
		      			<s:param name="displayAllCategories" value="%{true}" />
						<s:param name='newOP' value='%{true}'/>
						<s:param name="selectedHeaderTab">CatalogTab</s:param>
					</s:url>	
					<s:set name="categoryPath" value='wCContext.getSCUIContext().getLocalSession().getAttribute("categoryCache")'/>
	            	<s:if test='#selectedHeaderTab=="CatalogTab"'>
	            		<li class="active">
	            		<s:if test="#categoryPath !=null">
	            			<s:a href="%{allCatURL}"  cssClass="active">Catalog</s:a>
	            		</s:if>
	            		<s:else>
	            			<s:a href="%{allCatURL}"  onmouseover="javascript:getCategorySubMenu();" cssClass="active">Catalog</s:a>
	            		</s:else>	            			            		
	            	</s:if>
	            	<s:else>
	            		<li>
	            		<s:if test="#categoryPath !=null">
	            			<s:a href="%{allCatURL}" >Catalog</s:a>
	            		</s:if>
	            		<s:else>
	            			<s:a href="%{allCatURL}" onmouseover="javascript:getCategorySubMenu();">Catalog</s:a>
	            		</s:else>	            		
	            	</s:else>
	            	<ul class="sub_menu" style="visibility: hidden;" id="categorySubMenu" >
	            		<s:if test="#categoryPath !=null">
	            			<s:include value="/xpedx/jsp/common/XPEDXCatalogSubMenu.jsp"></s:include>
	            		</s:if>
	            	</ul>
			    </li>			    	            
	            <s:if test="%{#isProcurementUser}">
	            	<s:if test="%{procurementMyItemsLinkFlag}">	            		
						<s:url id='myListsLink' namespace='/myItems' action='MyItemsList.action'>
							<s:param name="filterByAllChk" value="%{false}" />
							<s:param name="filterByMyListChk" value="%{true}" />
							<s:param name="selectedHeaderTab">MyItemTab</s:param>
						</s:url>		            	
		            	<s:if test='#selectedHeaderTab=="MyItemTab"'>
		            		<li class="active">
		            		<s:a href="%{myListsLink}"  cssClass="active ieNavhack">
		            			<span class="left">&nbsp;</span><span class="right">My Items Lists</span>
		            		</s:a>
		            	</s:if>
		            	<s:else>
		            		<li>
		            		<s:a href="%{myListsLink}" cssClass="ieNavhack">
		            			<span class="left">&nbsp;</span><span class="right">My Items Lists</span>
		            		</s:a>
		            	</s:else>						
						<div id="MILSubMenu" style="display: inline">
						</div>
		            </li>
	            	</s:if>
	            </s:if>
	            <s:else>
						<s:url id='myListsLink' namespace='/myItems' action='MyItemsList.action'>
							<s:param name="filterByAllChk" value="%{false}" />
							<s:param name="filterByMyListChk" value="%{false}" />
							<s:param name="selectedHeaderTab">MyItemTab</s:param>
						</s:url>		            	
		            	<s:if test='#selectedHeaderTab=="MyItemTab"'>
		            		<li class="active">
		            		<s:a href="%{myListsLink}"  cssClass="active ieNavhack">My Items Lists</s:a>
		            	</s:if>
		            	<s:else>
		            		<li>
		            		<s:a href="%{myListsLink}" cssClass="ieNavhack">My Items Lists</s:a>
		            	</s:else>
		            </li>
	            </s:else>	            
	            <s:url id ='quickAddLink' action='quickAddAction' namespace='/order'>
	            	<s:param name="selectedHeaderTab">QuickAdd</s:param>
	       			<s:param name="quickAdd" value="%{true}" />
	            </s:url>	            
	            	<s:if test='#selectedHeaderTab=="QuickAdd"'>
	            		<li class="active">
		            	<s:a href="%{quickAddLink}" cssClass="active">Quick Add</s:a>
		            	 </li>
	            	</s:if>
	            	<s:else>
	            		<li>
	            		<s:a href="%{quickAddLink}">Quick Add</s:a>
	            		 </li>
	            	</s:else>		      	
	            <s:if test="%{!#isProcurementUser}">
				  <s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
					<s:if test='#selectedHeaderTab=="OrderTab"'>            	
		            	<li class="active">
		            	 <!-- cssClass="active" -->
		            	<s:a  href='%{catURL11}' cssClass="link" >Order Management</s:a>
	            	</s:if>
	            	<s:else>
	            		<li>
	            		<s:a href='%{catURL11}'>Order Management</s:a>
	            	</s:else>
	            	<ul class="sub_menu" style="visibility: hidden;" >			        					
				        <s:url id='catURL10' namespace='/order' action='approvalList.action'>
									<s:param name="sfId"><s:property value="wCContext.storefrontId" /></s:param>
									<s:param name="pageNumber">1</s:param>
									<s:param name='scFlag'>Y</s:param>
						</s:url>
						<s:if test="%{isApprover()}">
							<li>
								<s:a href='%{catURL10}' cssClass="link">
									Pending Approval(s)
								</s:a>
							</li>
						</s:if>				        
				         <s:url id='catURL11' namespace='/order' action='orderList.action'>
									<s:param name="sfId"><s:property value="wCContext.storefrontId" /></s:param>
									<s:param name='scFlag'>Y</s:param>
						</s:url>
						<li>
							<s:a href='%{catURL11}' cssClass="link">
								Orders
							</s:a>
						</li>					
						<s:if test="%{isViewInvoices()}">
							<li>
								<a href="<s:property value='%{invoiceURL}'/>UserID=<s:property value='%{userKey}' />&shipTo=<s:property value='%{custSuffix}' />" target="_blank" class="link">
									Invoices
								</a>
							</li>
						</s:if>
					<!--  	<li>
							<s:a href='%{catURL11}' cssClass="link">
								Return Requests
							</s:a>
						</li> -->
						<li>
							<a cssClass="link" href="<s:url action="draftOrderList" namespace="/order" />"  tabindex="2003">My Carts</a>
						</li>
			        </ul>
	            </li>
	            </s:if> 
	          </s:if>   
	            <s:if test="%{!#isProcurementUser}">	            
	                <s:url id='emailSampleLink' namespace='/services' action='MyServicesHome'>
						<s:param name="selectedHeaderTab">ServicesTab</s:param>
					</s:url>
					<s:url id='RequestProdSampleLink' namespace='/services' action='MyServices'>
						<s:param name="selectedHeaderTab">ServicesTab</s:param>
					</s:url>
					<s:url id='servicesHomeLink' namespace='/services' action='MyServicesHome'>
						<s:param name="selectedHeaderTab">ServicesTab</s:param>
					</s:url>
					<s:url id='estimatingFilesLink' namespace='/services' action='MyEstimatingFiles'>
						<s:param name="selectedHeaderTab">ServicesTab</s:param>
					</s:url>
					<s:url id='reportsLink' namespace='/services' action='XPEDXReports'>
						<s:param name="selectedHeaderTab">ServicesTab</s:param>
					</s:url>
					<s:url id='newToolsLink' namespace='/services' action='MyTools'>
						<s:param name="selectedHeaderTab">ServicesTab</s:param>
					</s:url>												
					<s:if test='#selectedHeaderTab=="ServicesTab" || #selectedHeaderTab=="ToolsTab" '>
		            	<li class="active">	
		            	<s:a href="#" cssClass="active">Resources</s:a>
		            </s:if>
		            <s:else>
		            	<li>
		            	<s:a >Resources</s:a>
		            </s:else>
		            <ul class="sub_menu" style="visibility: hidden;" >
		             <s:if test="%{isViewReports()}">
		            	<li>
							<s:a href='%{reportsLink}' cssClass="link">
								<s:text name="Reports"></s:text>
							</s:a>
						</li>
						</s:if>					 
					    <s:if test='#canRequestProductSample == "Y"'>
							<li>
								<s:a href='%{RequestProdSampleLink}' cssClass="link">
									<s:text name="Request Sample"></s:text>
								</s:a>
							</li>
					    </s:if>
					     <!-- Added for EB-1641 Remove any associations to estimating files Starts -->
					    <s:set name='storefrontId' value="wCContext.storefrontId" />
					   <s:if test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_STORE_FRONT.equals(#storefrontId)}'>
						<li>
							<s:a href='%{estimatingFilesLink}' cssClass="link">
								<s:text name="Estimating Files"></s:text>
							</s:a>
						</li>
						 </s:if>
					<!--  EB-1641 End -->
						<li>
							<s:a href='%{estimatingFilesLink}' cssClass="link">
								<s:text name="Printable Catalogs"></s:text>
							</s:a>
						</li>
						<s:url id='toolsLink' namespace='/tools' action='MyTools'>
							<s:param name="selectedHeaderTab">ServicesTab</s:param>
						</s:url>
						<s:set name='storeFrontId' value='wCContext.storefrontId'/>
						<s:if test="#storeFrontId == 'xpedx'">
							<li>
								<s:a href='%{newToolsLink}' cssClass="link">
									<s:text name="Tools"></s:text>
								</s:a>
							</li>
						</s:if>
		            </ul>
	            </li>
	            </s:if>	          
	            <s:if test="%{!#isProcurementUser}"> 	            
					<s:url id='adminProfile' namespace='/profile/user' action='XPEDXAdminProfile'>						
						<s:param name="selectedHeaderTab">AdminTab</s:param>
					</s:url> 
					<s:if test='#selectedHeaderTab=="AdminTab"'>
						<li class="active admin_tab">
		            	<s:a href="#" cssClass="active">Admin</s:a>
	            	</s:if>
	            	<s:else>
	            		<li class="admin_tab">
	            		<s:a>Admin</s:a>
	            	</s:else>
	            	<ul class="sub_menu" style="visibility: hidden;" >
				       	<s:url id='myProfile' namespace='/profile/user' action='MyUserProfile' >
				       		<s:param name="isUserProfile" value="%{true}" />
				       	</s:url>
			       		<s:url id='shipTo' namespace='/profile/org' action='xpedxGetShipToInfo' />
						<s:url id='billTo' namespace='/profile/org' action='xpedxGetBillToInfo' />			       
						<li>
							<s:a href='%{myProfile}' cssClass="link" onclick="javascript:msgWait();">
								<s:text name="My Profile"></s:text>
							</s:a>
						</li>						
						<s:set name='loggedInUserCustomerID' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getLoggedInCustomerFromSession(wCContext)'/>
						<s:set name="loggedInUserOrgCode"  value='wCContext.storefrontId'/>								       	
						<s:url id='sapCustProfile' namespace='/profile/org' action='MyGetCustomerInfo'>
							<s:param name="customerId" value="#sapCustomerId" />
							<s:param name="organizationCode" value="#loggedInUserOrgCode" />
						</s:url>
					<s:if test="%{#isUserAdmin && !#isSalesRep}">
						<li>
							<s:if test='#sapCustSize > 1' >
								<s:a id="dlgSelectAccount" href="#dlgSelectAccountBox">
									<s:text name="My Accounts"></s:text>
								</s:a>
							</s:if>
							<s:elseif test='#sapCustomerId!= ""'>
							<s:a href='%{sapCustProfile}' cssClass="link">
								<s:text name="My Accounts"></s:text>
							</s:a>
							</s:elseif>
						</li>
					</s:if>
					<s:if test="%{#isUserAdmin && !#isSalesRep}">
						<li>
							<s:a href='%{myProfile}' cssClass="link" onclick="javascript:msgWait();">
								<s:text name="My Users"></s:text>
							</s:a>
						</li>
					</s:if>
						<s:if test="%{#isUserAdmin && !#isSalesRep}">
							<s:url id='newsMaintenanceLink' namespace="/profile/user" action='MyNewsMaintenance'>					
							</s:url>
							<li>
								<s:a href='%{newsMaintenanceLink}' cssClass="link">
									<s:text name="Manage News"></s:text>
								</s:a>
							</li>
						</s:if>
			        </ul>
	            </li>
	           </s:if>
	            <s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
		            <s:url id='homeLink' namespace='/order' action='orderList.action'>
						<s:param name="sfId"><s:property value="wCContext.storefrontId" /></s:param>
						<s:param name='scFlag'>Y</s:param>
						<s:param name="selectedHeaderTab">AddToExistingOrder</s:param>						
						<s:param name="sourceTab">Open</s:param>
					</s:url>
	            	<s:if test='#selectedHeaderTab=="AddToExistingOrder"'>
	            		<li class="active lighter" >
		            	<s:a href="%{homeLink}" cssClass="active">Add To Existing Order</s:a>
	            	</s:if>
	            	<s:else>
	            		<li class="lighter">
	            		<s:a href="%{homeLink}">Add To Existing Order</s:a>
	            	</s:else>
	            </li>
	            </s:if>
	            <s:else>
	            	<li class="lighter order-edit-bg">
	            		<s:a href="">Order edit in progress...</s:a>
	           		 </li>		
	           		 
					<s:url id="viewEditOrderChanges" includeParams="none"
							action='MyViewEditOrderChanges' namespace='/order' escapeAmp="false">
							<s:param name="orderHeaderKey" value='%{#isEditOrderHeaderKey}' />
							<s:param name="isEditOrder" value='true' />
							<s:param name="isEditOrder" value='true' />
					</s:url>
	            	<li class="lighter order-edit">
	            		<s:a href="%{viewEditOrderChanges}">View Changes</s:a>
	            	</li>
	            	<s:url id="cancelEditOrderChanges" includeParams="none"
							action='MyResetPendingOrder' namespace='/order' escapeAmp="false">
							<s:param name="orderHeaderKey" value='%{#isEditOrderHeaderKey}' />
					</s:url>
	            	<li class="lighter order-edit">
	            		<s:a href="%{cancelEditOrderChanges}">Cancel Changes</s:a>
	            	</li>		            
	            </s:else>
	        </ul>
		</div>
	</s:if>
</div>
<div style="display: none;">
	<div id="viewUsersDlg">
	</div>
</div>
<div style="display: none;">
	<div id="cancelShopping">
		<p> Are you sure you want to leave this page? </p><br/><br/>
		<p>If inactive, your current cart will automatically be purged in 7 days.</p>
		<div class="float-right">
			<ul id="tool-bar" class="tool-bar-bottom">			
			<s:url id='procurementPunchOutURL' action='procurementPunchOut' namespace="/order" escapeAmp="false">
	            <s:param name='mode' value='"cancel"'/>
	            <s:param name='draft' value='"Y"'/>
	        </s:url>
			<li>
				<a class="green-ui-btn" href="<s:property value='#procurementPunchOutURL'/>">
					<SPAN>Yes</SPAN>
				</a>
			</li>
			<li>
				<a class="grey-ui-btn" href="javascript:$.fancybox.close()">
					<SPAN>No</SPAN>
				</a>
			</li>
			</ul>
		</div>
	</div>
</div>
 <!-- ship to banner -->
<s:if test="%{getShipToBanner()}">
	<s:set name='guestUser' value="%{#_action.getWCContext().isGuestUser()}" />
	<s:set name='defualtShipTAddress'  value="%{#_action.getShipToAddress()}" />
	<s:set name="isEditOrderHeaderKey" value ="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute(@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@EDITED_ORDER_HEADER_KEY)}"/>
	<s:if test='!#guestUser'>
		<div class="ship-banner-container">
			<div class="ship_banner">
		     <p>		     
		       <span class="bold">
		         <s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
	         		Shopping for: 
	       		</s:if>
	      		 <s:else>
	        		 Orders for:
	       		</s:else>	     
		       </span>
		       <s:property   value='LoggerInUserCustomerName' />, 
		       <s:iterator value="#defualtShipTAddress.getAddressList()" id='addressline'>
		       	<s:if test="%{#addressline.trim() != ''}">		       	
		          <s:property value="addressline" />, 
		       	</s:if>		           
		       </s:iterator>
		       <s:property value="#defualtShipTAddress.getCity()" />,
		       <s:property value="#defualtShipTAddress.getState()" />
		       <s:property value="%{@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedZipCode(#defualtShipTAddress.getZipCode())}" /> 
		       <s:property value="#defualtShipTAddress.getCountry()" />
		       <s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
		       	<a href="#ajax-assignedShipToCustomers" id="shipToSelect">[Change]</a>
		       </s:if>
		    </p>
	    	</div>
	   	</div>
    </s:if>
</s:if>
<!-- end ship to banner -->

<!-- begin search tips -->
<div style="display: none;">
	<div id="searchTips">
		<h2>Search Tips</h2>
		<p>You can use special characters such as the asterisk (*) and ? when performing an advanced search.</p>
		<ul>
			<li>
				<p>The asterisk (*) can be used in search terms for any number of unknown alphanumeric characters. For example, "Comput*" can be used to search for products containing words such as "computing systems", "computing power", "computational device", and so on.</p>
				<p>Note: Using the asterisk (*) as the leading character when specifying a search term is not supported. For example, if you are looking for a product with ABC123 as the Product ID, you cannot provide *C123 as the search term. However, you can search for the product using AB* as the search term.</p>
			</li>
			<li>
				<p>The question mark (?) can be used in search terms as a substitute for exactly one unknown alphanumeric character. Note: Using the question mark (?) as the leading character when specifying a search term is not supported. For example, if you are looking for a product with ABCD as the Product ID, you cannot provide ?BCD as the search term. However, you can search for the product using AB?D as the search term.</p>
			</li>
		</ul>
	</div>
</div>
<!-- end search tips -->