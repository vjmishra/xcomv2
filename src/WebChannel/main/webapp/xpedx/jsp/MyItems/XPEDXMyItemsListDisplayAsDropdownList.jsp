<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

	<s:set name="isUserAdmin" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@isCurrentUserAdmin(wCContext)" />
	<s:set name="CurrentCustomerId" value="@com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils@getCurrentCustomerId(wCContext)" />
	
	<script type="text/javascript">
		var isUserAdmin = <s:property value="#isUserAdmin"/>;
	</script>	

<script type="text/javascript">
	
	function updateShareListChild(){
	}
	
	function submitNewlist(xForm){
		//Validate the form
		try{
			if (xForm == undefined || xForm == null || xForm == ""){
				xForm = "XPEDXMyItemsDetailsChangeShareList";
			}
		}catch(er){
			xForm = "XPEDXMyItemsDetailsChangeShareList";
		}
		
		var form = Ext.get(xForm);
		
		//Validate form
		try{
			var val = form.dom.listName.value;
			if (val.trim() == ""){
				alert("Name is required. Please enter a name for this list.");
				return;
			}
		}catch(err){
		}
		
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
		
		//form.dom.submit();
        //Submit the data via ajax
        
        //Init vars
        <s:url id='XPEDXMyItemsDetailsChangeShareListURL' includeParams='none' action='MyItemsDetailsChangeShareList' namespace="/myItems" escapeAmp="false" />
        
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
              var dlItems = currentAadd2ItemList;
              var opt = document.createElement('option');
			  opt.text	= listName;
			  opt.value = listKey;
			  try { dlItems.add(opt,null); } catch(ex) { dlItems.add(opt); }
			  dlItems.selectedIndex = dlItems.length-1;
			  currentAadd2ItemList.onchange();
			  //addItemsToList(dlItems.selectedIndex);
			  //$("#itemListSelect").trigger('change');
			  
			  //reloadMenu();
			  // Removal of MIL dropdown list from header for performance improvement
			  Ext.Msg.hide();
          },
          failure: function (response, request){
              document.body.style.cursor = 'default';
              Ext.Msg.hide();
              
             // alert("Error creating new list. Please try again later.");                                      
          }
      });
      document.body.style.cursor = 'default';
		
		
	}
	
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
	
	
	function ReplaceAll(Source,stringToFind,stringToReplace){
	  var temp = Source;
	    var index = temp.indexOf(stringToFind);
	        while(index != -1){
	            temp = temp.replace(stringToFind,stringToReplace);
	            index = temp.indexOf(stringToFind);
			}
	        return temp;
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
                       alert('Failed to fecth the share list');
                       document.body.style.cursor = 'default';                                                  
                   }
               });     
           }
       	document.body.style.cursor = 'default';

	}
	
	function showShareList(customerId, showRoot, clFromListId){
		//Populate fields
		var divMainId 	= "divMainShareList";
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
	
	function addItemsToList(idx) {
		if (idx == 1){
			$.fancybox(
				Ext.get("dlgShareList").dom.innerHTML,
				{
					'onStart' 	: function(){
						showShareList('<s:property value="#CurrentCustomerId"/>');
						if (isUserAdmin){
							Ext.get("rbPermissionShared").dom.checked = true;
						} else {
							Ext.get("rbPermissionPrivate").dom.checked = true;
						}
						Ext.get("smilTitle").dom.innerHTML = "New My Items List"; 
					},
		        	'autoDimensions'	: false,
					'width'         	: 600,
					'height'        	: 500
				}
			);			
	    } else if (idx > 1){
			document.OrderDetailsForm.action 			= document.getElementById('addItemsToListURL');
			document.getElementById("listKey").value 	= document.getElementById("itemListSelect").value;
		    //document.OrderDetailsForm.submit();
		    
	        <s:url id='AddItemURL' includeParams='none' escapeAmp="false" namespace="/order" action="xpedxAddItemsToList" />
	        
	        var url = "<s:property value='#AddItemURL'/>";
	        //url = ReplaceAll(url,"&amp;",'&');
	        
	        //Execute the call
	        document.body.style.cursor = 'wait';
	        
	        Ext.Ajax.request({
	          url: url,
	          form: 'OrderDetailsForm',
	          method: 'POST',
	          success: function (response, request){
	              document.body.style.cursor = 'default';
	              
	              location.reload(true); //Refresh the apge
	          },
	          failure: function (response, request){
	              document.body.style.cursor = 'default';
	              alert("Error adding item to the list. Please try again later.");                                      
	          }
	       });
	      
	      document.body.style.cursor = 'default';
		  currentAadd2ItemList.selectedIndex = 0;
		  document.getElementById("itemListSelect").selectedIndex = 0; 
		  
	    }
	}
	
	$(document).ready(function() {
		
		$("#dlgShareListLink").fancybox({
			'onStart' 	: function(){
				if (isUserAdmin){
					//Calling AJAX function to fetch 'Ship-To' locations only when user is an Admin
					showShareList('<s:property value="#CurrentCustomerId"/>');
				}
				Ext.get("smilTitle").dom.innerHTML = "New My Items List";  
			},
			'onClosed':function() {
				document.XPEDXMyItemsDetailsChangeShareList.listName.value = "";
				document.XPEDXMyItemsDetailsChangeShareList.listDesc.value = "";
				document.getElementById("itemListSelect").value="-1";
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
				
			},
			'autoDimensions'	: false,
			'width' 			: 600,
			'height' 			: 500  
		});
		
	});	
		var itemCountList=new Array();
		var num=0;

		function setArrayValue(key,val){
			itemCountList[num]={
				listKeyId :key,
			 	itemCount :val
			}
			num=num+1;
		}
    
</script>
<div id="divAdd2List">
	<s:select 
		cssClass="xpedx_select_add_list float-left" 
		name="itemListSelect"
		id="itemListSelect" 
		onchange='javascript:currentAadd2ItemList = this; addItemsToList(this.selectedIndex, "[itemId]", "[name]", "[desc]", "[qty]", "[uom]");'
		headerKey="-1" 
		headerValue="Add to List" 
		list="listOfItems"
		listKey='getAttribute("MyItemsListKey")' 
		listValue='getAttribute("Name")' 
		disabled=""
		tabindex="948" 
		theme ="simple"
	/>
	
	<s:iterator id="listSizeId" value="%{listSizeMap.keySet()}">
		<s:set name="itemCount" value="%{listSizeMap.get(#listSizeId)}" />
		<script>
			setArrayValue('<s:property value="#listSizeId"/>','<s:property value="#itemCount"/>');
		</script>	
	</s:iterator>

</div>