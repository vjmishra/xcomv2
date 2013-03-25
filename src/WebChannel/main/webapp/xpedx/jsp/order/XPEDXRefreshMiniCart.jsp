<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script>
function    refreshMiniCartLink(){
	<s:url id='XPEDXMiniCartLinkDisplayURL'  namespace='/order'  action='XPEDXMiniCartLinkDisplay.action'>
		<s:param name="forceRefresh" value="%{true}" />
	</s:url>
   	var url = "<s:property value='#XPEDXMiniCartLinkDisplayURL'/>";
	url = ReplaceAll(url,"&amp;",'&');
		Ext.Ajax.request({
		   	url:url,
		   	success: function (response, request)
			{   
				var progressDiv = document.getElementById("progressDiv");
				 if(progressDiv != null)
				 	progressDiv.innerHTML = ""; 
				var myDiv = document.getElementById("XPEDXMiniCartLinkDisplayDiv");
				myDiv.innerHTML = response.responseText;
			},
			failure: function ( response, request ) {
						refreshMiniCartLinkOnFailure();
		    	}
		});
	}
	function refreshMiniCartLinkOnFailure(){
				<s:set name='orderHeaderKey' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("OrderHeaderInContext")'/>
				<s:set name="loggedincustomer" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getLoggedInCustomerFromSession(wCContext) ' />
				<s:if test='#orderHeaderKey == null && #loggedincustomer != null'>
					<s:set name='orderHeaderKey' value='@com.sterlingcommerce.xpedx.webchannel.order.utilities.XPEDXCommerceContextHelper@getCartInContextOrderHeaderKey(wCContext)'/>
				</s:if>
				var myDiv = document.getElementById("XPEDXMiniCartLinkDisplayDiv");
				<s:if test='#orderHeaderKey != null && #orderHeaderKey != ""'> 
					myDiv.innerHTML= '<input type="hidden" name="carddetailOrderHeaderKey" id="carddetailOrderHeaderKey" value="'+<s:property value='#orderHeaderKey'/>+'"/>View Cart';
				 </s:if>
				 <s:else>
				  	myDiv.innerHTML= '<input type="hidden" name="carddetailOrderHeaderKey" id="carddetailOrderHeaderKey" value=""/>View Cart';
				 </s:else>
	}
	
	function refreshWithNextOrNewCartInContext(){
	
	<s:url id='XPEDXRefreshCartURL'  namespace='/order'  action='XPEDXRefreshCart.action'>
		<s:param name="forceRefresh" value="%{false}" />
	</s:url>	
		var url1 = "<s:property value='#XPEDXRefreshCartURL'/>";
		url1 = ReplaceAll(url1,"&amp;",'&');	
			Ext.Ajax.request({
			   	url:url1,
			   	success: function (response, request)
				{
					 var progressDiv = document.getElementById("progressDiv");
					 if(progressDiv != null)
				 		progressDiv.innerHTML = ""; 
					var myDiv = document.getElementById("XPEDXMiniCartLinkDisplayDiv");
					myDiv.innerHTML = response.responseText;
				},
				failure: function ( response, request ) {
							refreshMiniCartLinkOnFailure();
			    	}
			});

	}
</script>