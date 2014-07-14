<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:set name='_action' value='[0]' />
<script lanaguage="javascript">
	 function getUsers(url)
  	 {
		var customerListDropDown = document.customerUserDraftOrderCreate.customerList;
		var selectedCustomer = customerListDropDown.options[customerListDropDown.selectedIndex].value;
		var userListDropDown = document.customerUserDraftOrderCreate.userList;
		document.body.style.cursor = 'wait';
		userListDropDown.disabled=true;
		if(selectedCustomer != null && customerListDropDown.selectedIndex >0){
			Ext.Ajax.request({
		        url: url,
		        params: {
		   			selectedCustomer: selectedCustomer
		        },
		        method: 'POST',
		        success: function (response, request){
		            document.body.style.cursor = 'default';
		            var userListDivId = document.getElementById("customerUsersDiv");
		            userListDivId.innerHTML = response.responseText;
		            userListDropDown.disabled=false;		            
		        },
		        failure: function (response, request){
		            alert('Failed to fecth users for this customer');
		            document.body.style.cursor = 'default';		            			           
		        }
		    });	
	  	}
        document.body.style.cursor = 'default';
  	}	
	function createCustomerUserCart(){
		var customerListDropDown = document.customerUserDraftOrderCreate.customerList;
		var selectedCustomer = customerListDropDown.options[customerListDropDown.selectedIndex].value;
		var userListDropDown = document.customerUserDraftOrderCreate.userList;
		var selectedUser = userListDropDown.options[userListDropDown.selectedIndex].value;
		var orderName=document.customerUserDraftOrderCreate.customerUserCartName.value;
		
		if(customerListDropDown.selectedIndex==0){
			alert('Plaese select customer');
			return false;
		}
		if(userListDropDown.selectedIndex==0){
			alert('Plaese select customer user');
			return false;
		}
		if(orderName.trim().length==0){
			alert('Plaese enter cart name');
			document.customerUserDraftOrderCreate.customerUserCartName.focus();
			return false;			
		}
		if(customerListDropDown.selectedIndex >0 && userListDropDown.selectedIndex >0){	
			document.customerUserDraftOrderCreate.selectedCustomer.value=selectedCustomer;
			document.customerUserDraftOrderCreate.selectedUser.value=selectedUser;
			document.customerUserDraftOrderCreate.orderName.value=orderName;
			document.customerUserDraftOrderCreate.submit();
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
</script>
<div id="customerUsersDiv">
<s:label name="SelectUser"
	value="Select User:"/>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<s:autocompleter theme="simple" name="userList" dropdownWidth="20"
	headerKey="-- Select User --" headerValue="-- Select User --"
	list="%{#_action.getCustomerUserList()}"		
	cssClass='hidden'>
</s:autocompleter></div>