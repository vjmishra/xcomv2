<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="xpedx" uri="xpedx"%>

<s:set name='_action' value='[0]' />
<s:bean
	name='com.sterlingcommerce.xpedx.webchannel.common.XPEDXSCXmlUtils'
	id='xutil' />
<s:bean
	name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils"
	id="wcUtil" />
<s:set name='scuicontext' value="uiContext" />
<s:set name='PrntChildComb' value="#_action.getPrntChildComb()" />
<s:set name='assignedCustomerList' value="getAssignedCustomerList" />

<html>
<head>
<s:url id='AjaxDynamic' action='AjaxDynamic' namespace="/profile/user"
	escapeAmp="false" />
	<style>
	.ajax_loader {background: url("../../xpedx/css/images/loading.gif") no-repeat center center transparent;width:100%;height:100%;}
	
	</style>
	
<script type="text/javascript">
	var url = "<s:property value='#AjaxDynamic'/>";
		$(function() {
				$("#tree")
				.dynatree(
						{
							checkbox : "true",
							selectMode : 3,
							onSelect : function(select, node) {
								// Get a list of all selected nodes, and convert to a key array:
								var selKeys = $.map(node.tree
										.getSelectedNodes(), function(node) {
									return node.data.key;
								});
								$("#echoSelection3").text(selKeys.join(", "));

								// Get a list of all selected TOP nodes
								var selRootNodes = node.tree
										.getSelectedNodes(true);
								// ... and convert to a key array:
								var selRootKeys = $.map(selRootNodes, function(
										node) {
									return node.data.key;
									//	return node.data.title;
								});
								$("#echoSelectionRootKeys3").text(
										selRootKeys.join(", "));
								$("#echoSelectionRoots3").text(
										selRootNodes.join(", "));
							},
							onDblClick : function(node, event) {
								node.toggleSelect();
							},
							onKeydown : function(node, event) {
								if (event.which == 32) {
									node.toggleSelect();
									return false;
								}
							},
							
							onLazyRead: function(currentNode,event) {
														
								var CurrCust = currentNode.data.key;

								if ($('#prevnode').val() == CurrCust) {
									//alert("prevnode val == currcust");
									return;
									} else {
									$('#prevnode').val(CurrCust);
								}
								$.ajax({
									type : "GET",
									url : url,
									data : "ParentCustomerId="
											+ CurrCust,
											
									success : function(response,
											textStatus, jqXHR) {
										var bSelected = currentNode.data.select;
										var blazy =true;
										var childValues = jqXHR.responseText;

										var childValuesArray = childValues
												.split("|");
										for ( var index = 1; index < childValuesArray.length; index++) {
											var childKeyValue = childValuesArray[index]
													.split("##");
											var childKey = childKeyValue[0];
											var childcombinedValue = childKeyValue[1];
											childValue = childcombinedValue
													.split("^^^");
											if(currentNode.isSelected()){	
												bSelected =true;
											}
											else if (childValue[1] == "checked") {
												bSelected = true;
											} else {
												bSelected = false;
											}
											if(currentNode.getLevel()>=3){
												blazy  = false;	
											}else{
												blazy =true;
											}
											
											
											
											var childNode = currentNode
													.addChild({
														title : childValue[0],
														key : childKey,
														select : bSelected,
														isLazy:blazy
													});

										/*	if (childNode.getLevel() < 4) {
												var invisibleNode = childNode
														.addChild({
															title : "empty",
															isFolder : "false"
															
														});
											}*/

										}
										currentNode.expand(true);
									},
									error : function(response,
											textStatus, jqXHR) {
									}
								
								});
					},

								onClick : function(currentNode, event) {
								
									var isNotAdmin = $("#userNotAdmin").val();
									if (currentNode.getEventTargetType(event) == "checkbox") {
										if (isNotAdmin == "true") {
											currentNode.data.unselectable = true;
											return false;
										}
										if(currentNode.isSelected()){
											bSelected =true;
										}else{
											bSelected= false;
										}
										
									}
								}
						});

		// Now get the root node object
		var rootNode = $("#tree").dynatree("getRoot");
		// Call the DynaTreeNode.addChild() member function and pass options for the new node
		var master = '<s:property value="%{#PrntChildComb}" />'; 
		var masterarray = master.split("|");
		var msapKeyValue = masterarray[0].split("##");
		var sapKeyValue = masterarray[1].split("##");
		var msapKey = "\"" + msapKeyValue[0] + "\"";
		var sapKey = "\"" + sapKeyValue[0] + "\"";
		var msapValuesplit = msapKeyValue[1].split("^^^");
		var sapValuesplit = sapKeyValue[1].split("^^^");
		var bSelected = false;
		if (msapValuesplit[1] == "checked") {
			bSelected = true;
		}

		var childNode = rootNode.addChild({
			key : eval(msapKey),
			title : eval("\"" + msapValuesplit[0] + "\""),
			select : bSelected
		});

		var childNodeSap = childNode.addChild({
			key : eval(sapKey),
			title : eval("\"" + sapValuesplit[0] + "\""),
			select : bSelected,
			isLazy : true
		});
		childNode.expand(true);
	});

	
</script>
</head>
<body>
	
	<table id="tbl" cellpadding="0" cellspacing="0" border="0">
		<tr>
			
				<div id="tree" 	style="width: 900px; height: 600px; overflowY: scroll"></div>
		</tr>
		<tr>
			<div>
				<span id="echoActive"></span>
			</div>
			<div>
				<span id="echoFocused"></span>
			</div>
			<p>
				<input id="msap" type="hidden" name="msap" value="sap1" /> <input
					id="prevnode" type="hidden" name="prevnode" />
			</p>

			<input type=hidden id="echoSelectionRoots3" />
			<input type=hidden id="echoSelection3" name="echoSelection3" />
			<input type=hidden id="echoSelectionRootKeys3" name="echoSelection3" />
		</tr>
	</table>

</body>
</html>


