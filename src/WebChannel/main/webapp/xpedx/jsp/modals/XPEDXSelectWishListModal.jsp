<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>

	<div style="display: none;">
	<a id="dlgAddToListLink" href="#dlgAddToList">Select a WishList</a>
		
		<div id="dlgAddToList" class="xpedx-light-box" style="width:360px; height:242px;">
				<h2>Select My Items List</h2>				
				<s:form id="addItemToList" name="addItemToList">
					<div style="height:157px; border:solid 1px #ccc; width:350px; padding:4px; overflow:auto;">
						<s:div id="divMainPrivateList">
							<!-- CONTENT FOR DISPLAYING RADIO BUTTONS WILL GO HERE -->
						</s:div>
					</div>
				</s:form>
			<ul id="tool-bar" class="line-spacing">
			<s:if test='listSize != null && listSize !="0"'> 
				<li style="float: right;"><a class="green-ui-btn" href='javascript:addItemsToList();'><span>Apply</span></a></li>
			</s:if>	
				
				<li style="float:right; margin-right:5px;" ><a class="grey-ui-btn" href="javascript:$.fancybox.close()" 
					onclick="$('#fancybox-close').click();return false;"><span>Cancel</span></a></li>
				<li style="margin-left:1px;" ><a class="modal" id="dlgShareListLinkHL1" href="#dlgShareListHL" >Create New List </a></li>					
			</ul>
		</div>
		
	</div>
	

