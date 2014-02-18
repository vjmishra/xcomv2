<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:set name='_action' value='[0]'/>
<s:set name="ohk" value='%{row.getAttribute("OrderHeaderKey")}' />

<script type="text/javascript">
function actionOnList(oForm,oKey){

	if(document.getElementById("cartActions"+oKey).value=="Open"){
		 
		<s:url id="cartActionURL" value="/order/draftOrderMakeCartInContextDetails.action">
		</s:url>
			oForm.action = '<s:property value="cartActionURL"/>';
           oForm.submit();

	} else if(document.getElementById("cartActions"+oKey).value=="Copy"){
		document.copyOrder.copyCartName.value="";
		document.copyOrder.copyCartDescription.value="";
		<s:url id="cartActionURL" value="/order/draftOrderCopy.action">
		</s:url>
			document.copyOrder.OrderHeaderKey.value=oKey;
			$('#various2').trigger('click');
			$("#copyCartName").focus();
			document.getElementById("cartActions"+oKey).value="None";
			
	}else if(document.getElementById("cartActions"+oKey).value=="Delete"){
	
			document.delOrder.OrderHeaderKey.value=oKey;
			//DialogPanel.show('xpedxDeleteCartDialog');
			$('#xpedxDeleteCartDialog1').trigger('click');
			document.getElementById("cartActions"+oKey).value="None";
			
	}


}

function deleteCart(formObj)
{
		<s:url id="cartActionURL" value="/order/draftOrderDelete.action">
		</s:url>
			formObj.action = '<s:property value="cartActionURL"/>';
           formObj.submit();
}
</script>




<s:form id="cartActionsForm%{#ohk}" name="cartActionsForm%{#ohk}">
					<s:hidden name="OrderHeaderKey" id="OrderHeaderKey" value="%{#ohk}"/>
					<s:hidden name="draft" id="draft" value="Y"/>
<div class="actions" >
<select name="cartActions<s:property value='ohk' />" id="cartActions<s:property value='ohk' />" 
onchange="actionOnList(this.form, '<s:property value='ohk' />')">
<option value="None" selected >- Select Action -</option>
<option value="Open">Edit Cart</option>
<option value="Copy">Copy Cart</option>
<option value="Delete">Delete Cart</option>
</select>
</div>
</s:form>

