<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="swc" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />


<div id="main-container">
<meta name="WT.ti" content="Anon xpedx Product Details" /> 
	<div id="main" class="anon-pages">
		<!-- Header Start  -->
		
		<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
		
		<!-- Header End -->
		
		<s:if test="%{null != #xutil.getChildElement(#itemListElem, 'Item')}">
			<s:set name="itemElem" value='#xutil.getChildElement(#itemListElem,"Item")' />
			<s:set name='isSuperseded' value='#xutil.getAttribute(#itemElem,"IsItemSuperseded")' />
			<s:set name="primaryInfoElem" value='#xutil.getChildElement(#itemElem,"PrimaryInformation")' />
			<s:set name='isValid' value='#xutil.getAttribute(#primaryInfoElem,"IsValid")' />
			<s:set name="computedPrice" value='#xutil.getChildElement(#itemElem,"ComputedPrice")' />
			<s:set name="availabilityEle" value='#xutil.getChildElement(#itemElem,"Availability")' />
			<s:set name='imageLocation' value="#xutil.getAttribute(#primaryInfoElem, 'ImageLocation')" />
			<s:set name='imageId' value="#xutil.getAttribute(#primaryInfoElem, 'ImageID')" />
			<s:set name='imageLabel' value="#xutil.getAttribute(#primaryInfoElem, 'ImageLabel')" />
			<s:set name='imageURL' value="#imageLocation + '/' + #imageId " />
			<s:set name="itemAssets" value='#xutil.getChildElement(#itemElem,"AssetList")' />
			<!--<s:set name="itemLargeImages" value='#catalogUtil.getAssetList(#itemAssets,"ITEM_IMAGE_LRG_1")' />-->
			<s:set name="hasItemLargeImages" value='#catalogUtil.hasAssetsDefined(#itemAssets,"ITEM_IMAGE_LRG_1")' />
			<s:set name="itemThumnailImages" value='#catalogUtil.getAssetList(#itemAssets,"ITEM_THUMBNAIL_IMAGE1")' />
			<s:set name='pImg'
				value='%{#imageLocation+"/"+#primaryInfoElem.getAttribute("ImageID")}' />
			<s:if test='%{#pImg=="/"}'>
				<s:set name='pImg' value='%{"/xpedx/images/INF_150x150<s:property value='#wcUtil.xpedxBuildKey' />.jpg"}' />
			</s:if>			
			<s:set name="itemDataSheets" value='#catalogUtil.getAssetList(#itemAssets,"ITEM_DATA_SHEET")' />
			<s:set name="itemMainImages" value='#catalogUtil.getAssetList(#itemAssets,"ITEM_IMAGE_1")' />
			<s:set name='minQty' value="#xutil.getAttribute(#primaryInfoElem, 'MinOrderQuantity')" />
			<s:set name='maxQty' value="#xutil.getAttribute(#primaryInfoElem, 'MaxOrderQuantity')" />
			<s:set name='kitCode' value='#xutil.getAttribute(#primaryInfoElem,"KitCode")' />
			<s:set name='isModelItem' value='#primaryInfoElem.getAttribute("IsModelItem")' />
			<s:set name='isPreConfigured' value='#primaryInfoElem.getAttribute("IsPreConfigured")' />
			<s:set name='isModelItem' value='#primaryInfoElem.getAttribute("IsModelItem")' />
			<s:set name='isConfigurable' value='#primaryInfoElem.getAttribute("IsConfigurable")' />
			<s:set name='itemID' value='#xutil.getAttribute(#itemElem,"ItemID")' />
			<s:set name='unitOfMeasure' value='#xutil.getAttribute(#itemElem,"UnitOfMeasure")' />
			<s:set name="itemElem1" value='#xutil.getChildElement(#itemListElem,"Item")' />
			<s:set name='itemID' value='#xutil.getAttribute(#itemElem1,"ItemID")' />
			<s:set name='unitOfMeasure' value='#xutil.getAttribute(#itemElem1,"UnitOfMeasure")' />
			<s:set name='configurationKey' value='#xutil.getAttribute(#primaryInfoElem,"ConfigurationKey")'/>
			<s:set name='isConfigurableBundle' value='%{"N"}'/>
			<s:if test='%{#kitCode == "BUNDLE" && #isConfigurable=="Y" }'>
				<s:set name='isConfigurableBundle' value='%{"Y"}'/>
			</s:if>
			<s:set name='ManufactureSKU' value='#xutil.getAttribute(#primaryInfoElem,"ManufacturerItem")'/>
		</s:if>
		
		<s:url id='punchOutURL' namespace='/order' action='configPunchOut' />
 					<s:set name="itemElem" value='#xutil.getChildElement(#itemListElem,"Item")' />
					<s:set name='isSuperseded' value='#xutil.getAttribute(#itemElem,"IsItemSuperseded")' />
					<s:set name="primaryInfoElem" value='#xutil.getChildElement(#itemElem,"PrimaryInformation")' />
					<s:set name="isValid" value='#xutil.getAttribute(#primaryInfoElem,"IsValid")' />
					<s:set name="computedPrice" value='#xutil.getChildElement(#itemElem,"ComputedPrice")' />
					<s:set name="availabilityEle" value='#xutil.getChildElement(#itemElem,"Availability")' />
					<s:set name='imageLocation' value="#xutil.getAttribute(#primaryInfoElem, 'ImageLocation')" />
					<s:set name='imageId' value="#xutil.getAttribute(#primaryInfoElem, 'ImageID')" />
					<s:set name='imageLabel' value="#xutil.getAttribute(#primaryInfoElem, 'ImageLabel')" />
					<s:set name='imageURL' value="#imageLocation + '/' + #imageId " />
					<s:set name="itemAssets" value='#xutil.getChildElement(#itemElem,"AssetList")' />
					<!--<s:set name="itemLargeImages" value='#catalogUtil.getAssetList(#itemAssets,"ITEM_IMAGE_LRG_1")' />-->
					<s:set name="hasItemLargeImages" value='#catalogUtil.hasAssetsDefined(#itemAssets,"ITEM_IMAGE_LRG_1")' />
					<s:set name="itemThumnailImages" value='#catalogUtil.getAssetList(#itemAssets,"ITEM_THUMBNAIL_IMAGE1")' />
					<s:set name="itemDataSheets" value='#catalogUtil.getAssetList(#itemAssets,"ITEM_DATA_SHEET")' />
					<s:set name="itemMainImages" value='#catalogUtil.getAssetList(#itemAssets,"ITEM_IMAGE_1")' />
					<s:set name='minQty' value="#xutil.getAttribute(#primaryInfoElem, 'MinOrderQuantity')" />
					<s:set name='maxQty' value="#xutil.getAttribute(#primaryInfoElem, 'MaxOrderQuantity')" />
					<s:set name='kitCode' value='#xutil.getAttribute(#primaryInfoElem,"KitCode")' />
					<s:set name='isModelItem' value='#primaryInfoElem.getAttribute("IsModelItem")' />
					<s:set name='isPreConfigured' value='#primaryInfoElem.getAttribute("IsPreConfigured")' />
					<s:set name='isModelItem' value='#primaryInfoElem.getAttribute("IsModelItem")' />
					<s:set name='isConfigurable' value='#primaryInfoElem.getAttribute("IsConfigurable")' />
					<s:set name='itemID' value='#xutil.getAttribute(#itemElem,"ItemID")' />
					<s:set name='unitOfMeasure' value='#xutil.getAttribute(#itemElem,"UnitOfMeasure")' />
					<s:set name='currency' value='#xutil.getAttribute(#itemListElem,"Currency")' />
		<div class="container">
		<s:set name="emailDialogTitle" scope="page"
			value="#_action.getText('Email_Title')" />

				<s:if test="%{null != #xutil.getChildElement(#itemListElem, 'Item')}">
			<s:set name="itemElem"
				value='#xutil.getChildElement(#itemListElem,"Item")' />
				<s:set name="itemElemExtn"
				value='#xutil.getChildElement(#itemElem,"Extn")' />					
			<s:set name='certFlag'
				value="#xutil.getAttribute(#itemElemExtn, 'ExtnCert')" />
				
			<s:set name='isSuperseded'
				value='#xutil.getAttribute(#itemElem,"IsItemSuperseded")' />
			<s:set name="primaryInfoElem"
				value='#xutil.getChildElement(#itemElem,"PrimaryInformation")' />
			<s:set name='isValid'
				value='#xutil.getAttribute(#primaryInfoElem,"IsValid")' />
			<s:set name="computedPrice"
				value='#xutil.getChildElement(#itemElem,"ComputedPrice")' />
			<s:set name="availabilityEle"
				value='#xutil.getChildElement(#itemElem,"Availability")' />
			<s:set name='imageLocation'
				value="#xutil.getAttribute(#primaryInfoElem, 'ImageLocation')" />
			<s:set name='imageId'
				value="#xutil.getAttribute(#primaryInfoElem, 'ImageID')" />
			<s:set name='imageLabel'
				value="#xutil.getAttribute(#primaryInfoElem, 'ImageLabel')" />
			<s:set name='imageURL' value="#imageLocation + '/' + #imageId " />
			<s:set name="itemAssets"
				value='#xutil.getChildElement(#itemElem,"AssetList")' />
			<!--<s:set name="itemLargeImages" value='#catalogUtil.getAssetList(#itemAssets,"ITEM_IMAGE_LRG_1")' />-->
			<s:set name="hasItemLargeImages"
				value='#catalogUtil.hasAssetsDefined(#itemAssets,"ITEM_IMAGE_LRG_1")' />
			<s:set name="itemThumnailImages"
				value='#catalogUtil.getAssetList(#itemAssets,"ITEM_THUMBNAIL_IMAGE1")' />
			<s:set name='pImg'
				value='%{#imageLocation+"/"+#primaryInfoElem.getAttribute("ImageID")}' />
			<s:if test='%{#pImg=="/"}'>
				<s:set name='pImg' value='%{"/xpedx/images/INF_150x150<s:property value='#wcUtil.xpedxBuildKey' />.jpg"}' />
			</s:if>
			<s:set name="itemDataSheets"
				value='#catalogUtil.getAssetList(#itemAssets,"ITEM_DATA_SHEET")' />
			<s:set name="itemMainImages"
				value='#catalogUtil.getAssetList(#itemAssets,"ITEM_IMAGE_1")' />
			<s:set name='minQty'
				value="#xutil.getAttribute(#primaryInfoElem, 'MinOrderQuantity')" />
			<s:set name='maxQty'
				value="#xutil.getAttribute(#primaryInfoElem, 'MaxOrderQuantity')" />
			<s:set name='kitCode'
				value='#xutil.getAttribute(#primaryInfoElem,"KitCode")' />
			<s:set name='isModelItem'
				value='#primaryInfoElem.getAttribute("IsModelItem")' />
			<s:set name='isPreConfigured'
				value='#primaryInfoElem.getAttribute("IsPreConfigured")' />
			<s:set name='isModelItem'
				value='#primaryInfoElem.getAttribute("IsModelItem")' />
			<s:set name='isConfigurable'
				value='#primaryInfoElem.getAttribute("IsConfigurable")' />
			<s:set name='itemID' value='#xutil.getAttribute(#itemElem,"ItemID")' />
			<s:set name='unitOfMeasure'
				value='#xutil.getAttribute(#itemElem,"UnitOfMeasure")' />
			<s:set name="itemElem1"
				value='#xutil.getChildElement(#itemListElem,"Item")' />
			<s:set name='itemID' value='#xutil.getAttribute(#itemElem1,"ItemID")' />
			<s:set name='unitOfMeasure'
				value='#xutil.getAttribute(#itemElem1,"UnitOfMeasure")' />
			<s:set name='configurationKey'
				value='#xutil.getAttribute(#primaryInfoElem,"ConfigurationKey")' />
			<s:set name='isConfigurableBundle' value='%{"N"}' />
			<s:if test='%{#kitCode == "BUNDLE" && #isConfigurable=="Y" }'>
				<s:set name='isConfigurableBundle' value='%{"Y"}' />
			</s:if>
			<s:set name='ManufactureSKU'
				value='#xutil.getAttribute(#primaryInfoElem,"ManufacturerItem")' />
		</s:if>
		
		<META name="DCSext.wtP_A_Item" content="<s:property value='#itemID'/>"/>
	<!-- If condition... and form things starts..-->
			<s:if test="%{null != #xutil.getChildElement(#itemListElem, 'Item')}">

			<!-- icons for print & email 
		    <div class="iconBox" id="icons">
		      <ul class="icons">
						<s:if test="%{null != #xutil.getChildElement(#itemListElem, 'Item')}">
							<s:set name="itemElem1" value='#xutil.getChildElement(#itemListElem,"Item")' />
							<s:set name='itemID' value='#xutil.getAttribute(#itemElem1,"ItemID")' />
							<s:set name='unitOfMeasure' value='#xutil.getAttribute(#itemElem1,"UnitOfMeasure")' />
							<s:set name='isSuperseded' value='#xutil.getAttribute(#itemElem,"IsItemSuperseded")' />
							<s:set name='isModelItem' value='#primaryInfoElem.getAttribute("IsModelItem")' />
							<s:set name='isValid' value='#primaryInfoElem.getAttribute("IsValid")' />
						</s:if>
		        <li>
		          <s:a cssClass="printIcon" href="javascript:openPrintItemPage()" tabindex="101">
							  <s:text name="Print.page" />
						  </s:a>
		        </li>
		        <s:url id='itemEmailURL' includeParams="none" escapeAmp="false" namespace='/catalog' value='emailItemDetails.action?itemID=%{#itemID}&unitOfMeasure=%{#unitOfMeasure}&messageType=ComposeMail'/>
		        <li>
						  <s:a cssClass="emailIcon" href="javascript:openItemEmailLightBox('%{#itemEmailURL}');" tabindex="103">
							  <s:text name="Email.page" />
						  </s:a>
		        </li>
		      </ul>
		      <div class="itemVariationInstr" >
						<s:if test='%{#isModelItem=="Y" && #isValid == "Y"}'>
							<s:text name="Choose.an.item.from.the.variation.tab.below" />
						</s:if>
			  </div>
		      <div class="clearBoth">&nbsp;</div>
		    </div> -->

			<!-- main content -->

				<s:set name="shortDesc"
					value='%{#utilMIL.formatEscapeCharacters(#xutil.getAttribute(#primaryInfoElem, "ShortDescription"))}' />
				<s:set name="longDesc"
					value='%{#utilMIL.formatEscapeCharacters(#xutil.getAttribute(#primaryInfoElem, "Description"))}' />
			<!-- START of new MIL - PN -->
			<div style="display: none;">
			<a id="dlgShareListLink" href="#dlgShareList"> show new list</a>
			<div id="dlgShareList" class="share-modal xpedx-light-box">
			<h2 id="smilTitle">Share My Items List</h2>
			<br> <!-- CODE_START MIL - PN --> <s:form
				id="XPEDXMyItemsDetailsChangeShareList"
				name="XPEDXMyItemsDetailsChangeShareList"
				action="XPEDXMyItemsDetailsChangeShareList"
				namespace="/xpedx/myItems" method="post">

				<p><strong>List Name:</strong>&nbsp;&nbsp;
				<input type="text" maxlength="255"
					name="listName" value="" /></p>
				<p><strong>Description:</strong>&nbsp;&nbsp;<input type="text"
					name="listDesc" value="" maxlength="255" /></p>
				<p><strong>Share With:</strong></p>

				<s:hidden name="listKey" value="new"></s:hidden>
				<s:hidden name="editMode" value="%{true}"></s:hidden>
				<s:hidden name="itemCount" value="%{0}"></s:hidden>
				<s:hidden id="clFromListId" name="clFromListId" value=""></s:hidden>

				<s:set name="rbPermissionShared" value="%{''}" />
				<s:set name="rbPermissionPrivate" value="%{''}" />
				<s:if test="%{#isUserAdmin}">
					<s:set name="rbPermissionShared" value="%{' checked '}" />
				</s:if>
				<s:else>
					<s:set name="rbPermissionPrivate" value="%{' checked '}" />
				</s:else>
			
				<s:set name="saCV" value="%{''}" />
				<s:if test='%{shareAdminOnly == "Y"}'>
					<s:set name="saCV" value="%{' checked '}" />
				</s:if>

				<!-- START - Saved hidden data Fields -->
				<s:iterator id="item" value='savedSharedList'>
					<s:set name='customerID' value='#item.getAttribute("CustomerID")' />
					<s:set name='customerPath'
						value='#item.getAttribute("CustomerPath")' />

					<s:hidden name="sslNames" value="%{#customerID}"></s:hidden>
					<s:hidden name="sslValues" value="%{#customerPath}"></s:hidden>
				</s:iterator>
				<!-- END - Saved hidden data Fields -->

				<!-- Private and Shared are missing from the HTMLs -->
				<p><strong> This list is: <input
					onclick="hideSharedListForm()" id="rbPermissionPrivate"
					<s:property value="#rbPermissionPrivate"/> type="radio"
					name="sharePermissionLevel"
					value="<s:property value="wCContext.loggedInUserId"/>" /> Private
				&nbsp;&nbsp; <s:if test="%{!#isUserAdmin}">
					<div style="display: none;">
				</s:if> <input onclick="showSharedListForm()"
					id="rbPermissionShared" <s:property value="#rbPermissionShared"/>
					type="radio" name="sharePermissionLevel" value=" " /> Shared <s:if
					test="%{!#isUserAdmin}">
			</div>
		</s:if>

		</strong>
		</p>

		<br />
	
	<s:set name="displayStyle" value="%{''}" />
		<s:if test="%{!#isUserAdmin}">
			<s:set name="displayStyle" value="%{'display: none;'}"/>
		</s:if>
		

		
	</br>
	</br>
	<ul id="tool-bar" class="tool-bar-bottom"
		style="border-top: 1px solid #CCCCCC; padding-top: 20px;">
		<li><a class="grey-ui-btn" href="javascript:$.fancybox.close()"><span>Cancel</span></a></li>
		<li style="float: right;"><a href="javascript:submitSL();"> <img
			src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/ui-buttons/ui-btn-save<s:property value='#wcUtil.xpedxBuildKey' />.gif"
			width="49" height="23" alt="Save" title="Save" /> </a></li>
	</ul>

	</s:form> <!-- CODE_END MIL - PN --></div>
	</div>
	<!-- END of new MIL - PN -->
	</s:if>

	<!-- if condition and form things ends -->

		<!-- ship to banner -->
	<s:action name="xpedxShiptoHeader"
		executeResult="true" namespace="/common" />		<!-- end ship to banner -->
		
	<!--- begin breadcrumbs header--->
		<div id="breadcrumbs-list-name" class="breadcrumbs-no-float">
		<a href="javascript:window.history.back();" >Back</a> / <s:property value='%{#itemID}' />
		</div>
		<!--- clearall puts stuff below 'floating' items. nonbreaking space is for IE--->				
		<div class="clearall">&nbsp;</div>
	<!--- end breadcrumbs header --->
	<s:form name="productDetailForm" id="productDetailForm"
		onsubmit="return false;	">
		<s:hidden name="#action.namespace" value="/catalog" />
		<s:hidden id="actionName" name="#action.name" value="itemDetails" />
		<s:hidden id="itemID" name="itemID" value="%{#itemID}" />
		<s:hidden id="unitOfMeasure" name="unitOfMeasure"
			value="%{#unitOfMeasure}" />
		<!-- addToCartURL to be retrieved from javascript -->

		<input type="hidden" id="addToCartURL"
			value="<s:property value='#addToCartURL'/>" />
		<s:hidden id="maxQty" name="maxQty" value="%{#maxQty}" />
                
	<div class="prod_detail">
			<h5><b><font color="red"><s:property
			value="ajaxLineStatusCodeMsg" /></font></b></h5>
		<div class="prod_detail_top">&nbsp;</div>
		<div class="prod_detail_rpt">
			<!-- right -->
			<div class="right-prod-detail">
				<div class="prod_desc">
			<s:url id='punchOutURL'
			namespace='/order' action='configPunchOut' /> <s:set name="itemElem"
			value='#xutil.getChildElement(#itemListElem,"Item")' /> <s:set name="itemElemExtn"
				value='#xutil.getChildElement(#itemElem,"Extn")' /><s:set
			name='isSuperseded'
			value='#xutil.getAttribute(#itemElem,"IsItemSuperseded")' /> <s:set
			name="primaryInfoElem"
			value='#xutil.getChildElement(#itemElem,"PrimaryInformation")' /> <s:set
			name="isValid"
			value='#xutil.getAttribute(#primaryInfoElem,"IsValid")' /> <s:set
			name="computedPrice"
			value='#xutil.getChildElement(#itemElem,"ComputedPrice")' /> <s:set
			name="availabilityEle"
			value='#xutil.getChildElement(#itemElem,"Availability")' /> <s:set
			name='imageLocation'
			value="#xutil.getAttribute(#primaryInfoElem, 'ImageLocation')" /> <s:set
			name='imageId'
			value="#xutil.getAttribute(#primaryInfoElem, 'ImageID')" /> <s:set
			name='imageLabel'
			value="#xutil.getAttribute(#primaryInfoElem, 'ImageLabel')" /> <s:set
			name='imageURL' value="#imageLocation + '/' + #imageId " /> <s:set
			name="itemAssets"
			value='#xutil.getChildElement(#itemElem,"AssetList")' /> <!--<s:set name="itemLargeImages" value='#catalogUtil.getAssetList(#itemAssets,"ITEM_IMAGE_LRG_1")' />-->
		<s:set name="hasItemLargeImages"
			value='#catalogUtil.hasAssetsDefined(#itemAssets,"ITEM_IMAGE_LRG_1")' />
		<s:set name="itemThumnailImages"
			value='#catalogUtil.getAssetList(#itemAssets,"ITEM_THUMBNAIL_IMAGE1")' />
		<s:set name="itemDataSheets"
			value='#catalogUtil.getAssetList(#itemAssets,"ITEM_DATA_SHEET")' />
		<s:set name="itemMainImages"
			value='#catalogUtil.getAssetList(#itemAssets,"ITEM_IMAGE_1")' /> <s:set
			name='minQty'
			value="#xutil.getAttribute(#primaryInfoElem, 'MinOrderQuantity')" />
		<s:set name='maxQty'
			value="#xutil.getAttribute(#primaryInfoElem, 'MaxOrderQuantity')" />
		<s:set name='kitCode'
			value='#xutil.getAttribute(#primaryInfoElem,"KitCode")' /> <s:set
			name='isModelItem'
			value='#primaryInfoElem.getAttribute("IsModelItem")' /> <s:set
			name='isPreConfigured'
			value='#primaryInfoElem.getAttribute("IsPreConfigured")' /> <s:set
			name='isModelItem'
			value='#primaryInfoElem.getAttribute("IsModelItem")' /> <s:set
			name='isConfigurable'
			value='#primaryInfoElem.getAttribute("IsConfigurable")' /> <s:set
			name='itemID' value='#xutil.getAttribute(#itemElem,"ItemID")' /> <s:set
			name='unitOfMeasure'
			value='#xutil.getAttribute(#itemElem,"UnitOfMeasure")' /> <s:set
			name='currency' value='#xutil.getAttribute(#itemListElem,"Currency")' />

					<p class="short-description"><s:property
			value='#xutil.getAttribute(#primaryInfoElem,"ShortDescription")' /> </p>
					<p>&nbsp;</p>                  
					<ul class="bullet_pts">
						<s:property value='#xutil.getAttribute(#primaryInfoElem,"Description")' escape="false" />
					</ul>
					<br/>
					<p><span id="sell-truncated">Loading</span><!--<span id="sell-full" style="display: none;"> commented for jira 2761-->
					<ul class="bullet_pts" stlye="padding-left:0px;">
						<s:property value='#xutil.getAttribute(#itemElemExtn,"ExtnSellText")' escape="false"/>						
					</ul>
					<!-- </span>--></p>
					<br>
					<div>
						<s:property value='wCContext.storefrontId' />  Item #: <s:property value='%{#itemID}' />
						<s:set name="certImage" value="#_action.getCertImagePath()" /> 
								<s:if test='%{#certFlag=="Y"}'>
									<img border="none"  src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/catalog/green-e-logo_small.png" alt="" />
								</s:if>
					</div>
		<s:if test='%{#isModelItem != "Y"}'>
			<s:if test='custSKU!= ""'>
				<p class="boldText">
					<s:if test='%{custSKU == "1" && custPartNumber!=null}' >
						<s:property value="#customerItemLabel" /> <s:property value='custPartNumber' />
					</s:if>
					<s:elseif test='%{custSKU == "2" && ManufacturerPartNumber!=null}'>
						<s:property value="#manufacturerItemLabel" /> <s:property value='ManufacturerPartNumber' />
					</s:elseif>
					<s:elseif test='%{custSKU == "3" && MPC!=null}'>
						<s:property value="#mpcItemLabel" /> <s:property value='MPC' />
					</s:elseif>
				</p>				
			</s:if>
			
		</s:if>
					<br/>
					<div class="red bold"> 
    <s:if test="(replacementAssociatedItems!=null && replacementAssociatedItems.size() > 0)">
				<s:iterator value='replacementAssociatedItems' id='replacementItem'
						status="count" >
				<s:set name="promoItemPrimInfoElem"
					value='#xutil.getChildElement(#replacementItem,"PrimaryInformation")' />
				<s:set name="promoItemComputedPrice"
					value='#xutil.getChildElement(#replacementItem,"ComputedPrice")' />
				<s:set name="itemAssetList"
					value='#xutil.getElementsByAttribute(#replacementItem,"AssetList/Asset","Type","ITEM_IMAGE_1" )' />

		        	<s:if test='#itemAssetList != null && #itemAssetList.size() > 0 '>
		        		<s:set name="itemAsset" value='#itemAssetList[0]' />
						<s:set name='imageLocation'
							value="#xutil.getAttribute(#itemAsset, 'ContentLocation')" />
						<s:set name='imageId'
							value="#xutil.getAttribute(#itemAsset, 'ContentID')" />
						<s:set name='imageLabel'
							value="#xutil.getAttribute(#itemAsset, 'Label')" />
						<s:set name='imageURL' value="#imageLocation + '/' + #imageId " />
						<!-- <img src="<s:url value='%{#imageURL}' includeParams='none' />"
										alt="<s:text name='%{#imageLabel}'/>" width="52" height="50" align="left" /> -->
		        	</s:if>
		        	<s:else>
<!--		        		<s:set name='imageIdBlank' value="%{'/images/blank.jpg'}" />
		        		<img src="<s:url value='%{#imageIdBlank}'/>" width="52" height="50" align="left" /> -->
		        	</s:else>
					<ul class="bullet_pts">
						<s:property	value='#xutil.getAttribute(#promoItemPrimInfoElem,"LongDescription")' escape="false" />
					</ul>
					</p>
		        		<s:url id='detailURLFromPromoProd' namespace='/catalog'
							action='itemDetails.action'>
							<s:param name='itemID'>
								<s:property value='#xutil.getAttribute(#replacementItem,"ItemID")' />
							</s:param>
							<s:param name='unitOfMeasure'>
								<s:property
									value='#xutil.getAttribute(#replacementItem,"UnitOfMeasure")' />
							</s:param>
					</s:url>
						<s:if test="#count.first"  >
						This item has been replaced. Select item:
						</s:if>
						<s:a href="%{detailURLFromPromoProd}" tabindex="%{#ipTabIndex}" >
						<s:if test="#count.first" && test="!#count.last" >
							#<s:property value='#xutil.getAttribute(#replacementItem,"ItemID")' />,
						</s:if>
						<s:if test="#count.last" && test="#count.first">
							#<s:property value='#xutil.getAttribute(#replacementItem,"ItemID")' />
						</s:if>
						</s:a>
	    	</s:iterator>
		</s:if>
		<s:if test='#itemID==null || #itemID==""' >
			You are not entitled to view this item.
		</s:if>
					
					</b></p></div>				
					<br/>

	<%-- Adding link for Replacement Items Ends --%>

					
				</div>
			</div>
			<!--end right prod detail -->
			<!-- right side image -->
			<div class="prod-detail-img">
			
			<s:if
			test="#itemMainImages != null && #itemMainImages.size() > 0">
			<s:set name='imageMainLocation'
				value="#xutil.getAttribute(#itemMainImages[0], 'ContentLocation')" />
			<s:set name='imageMainId'
				value="#xutil.getAttribute(#itemMainImages[0], 'ContentID')" />
			<s:set name='imageMainLabel'
				value="#xutil.getAttribute(#itemMainImages[0], 'Label')" />
			<s:set name='imageMainURL'
				value="#imageMainLocation + '/' + #imageMainId " />
			<s:if test='%{#imageMainURL=="/"}'>
				<s:set name='imageMainURL' value='%{"/xpedx/images/INF_150x150<s:property value='#wcUtil.xpedxBuildKey' />.jpg"}' />
			</s:if>
			<img src="<s:url value='%{#imageMainURL}' includeParams='none'/>"
				width="150" height="150" id="productImg1"
				alt="<s:text name='%{#imageMainLabel}'/>" />
		</s:if> <s:else>
		<img src="<s:url value='%{#pImg}'/>" width="150" height="150" 
				id="productImg1" alt="<s:text name='%{#pImg}'/>"/>			
		</s:else> <s:if
			test='%{#hasItemLargeImages != null && #hasItemLargeImages == "true"}'>
			<script type="text/javascript"
				src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/component/lightbox<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
			<script type="text/javascript"
				src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/commonutils<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
			<script>
        	Ext.ux.Lightbox.register('a.imageViewer', true); // true to show them as a set
        	</script>
			<div id="itemImagesViwer"></div>
		</s:if>
			
		</div>
			<!--<div class="clearall">&nbsp; </div>-->
			<!-- end right side image -->
			<!-- begin lower half -->
			
			<!-- BEGIN prod-config -->
			<div class="prod-config">

				<br />				
		<script type="text/javascript">

		 </script> <!-- END - Adding the MIL dropdown - PN -->

		<s:set name="xpedxItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_ITEM_LABEL"/>
		<s:set name="customerItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CUSTOMER_ITEM_LABEL"/>
		<s:set name="manufacturerItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MANUFACTURER_ITEM_LABEL"/>
		<s:set name="mpcItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MPC_ITEM_LABEL"/>
			 
				<!-- table to properly format the pricing per the mockup -->
				<div class="bottom_right">				
				<br>	
					<br/>
					<br/>
					<br/>
					<br/>
				</div>
				<!-- end table and pricing info -->
			</div>
			<!-- END prod-config -->
		<!-- TABS -->
		<div class="avail-grid">
			<div id="tabs" >
				<ul>
					<li><a href="#tabs-2">Specifications</a></li>
				</ul>
				
				<p class="tablinks">&nbsp;
				<s:iterator value="msdsLinkMap" id="msdsMap" status="status" >
					<s:set name="link" value="value" />
					<s:set name="desc" value="key" />	
					<a class="slightly_left" href="<s:property value='#link'/>" target="_blank"><s:property value="#desc"/></a>
				</s:iterator>
				</p>
				<!-- tab1 -->
				<!-- end tab1 -->
                                    
				<!-- tab2 -->
		<div id="tabs-2" ><s:set name="certImage"
			value="#_action.getCertImagePath()" /> <s:if
			test="%{null != #certImage}">
			<img src="#certImage" class="green-e-prod" align="right" />
		</s:if> <s:set name="itemAttributeGroupTypeList"
			value='#xutil.getChildElement(#itemElem,"ItemAttributeGroupTypeList")' />		
		<div id="tabs-2" class="ie_tabsfix" >	
		<br/>
		<table id="prod-details-tbl" border="0" cellspacing="0"
			cellpadding="0" style="top=24px;" >
			<tr class="detail-head-prod-bg">
				<td class="tblhead-white int-deets2">Specification</td>
				<td class="tblhead-white">Details</td>
			</tr>
			<s:iterator
				value='#xutil.getChildren(#itemAttributeGroupTypeList, "ItemAttributeGroupType")'
				id='itemAttributeGroupType'>
				<s:set name='classificationPurposeCode'
					value='#xutil.getAttribute(#itemAttributeGroupType,"ClassificationPurposeCode")' />
				<s:if test="%{#classificationPurposeCode=='SPECIFICATION'}">
					<s:set name="itemAttributeGroupList"
						value='#xutil.getChildElement(#itemAttributeGroupType,"ItemAttributeGroupList")' />
					<s:set name="itemAttrGrp"
						value='#xutil.getChildElement(#itemAttributeGroupList,"ItemAttributeGroup")' />
					<s:if test="#itemAttrGrp!= null">
						<s:set name="counter" value="0" />
						<s:iterator
							value='#xutil.getChildren(#itemAttributeGroupList, "ItemAttributeGroup")'
							id='itemAttributeGroup'>
							<s:set name="itemAttributeList"
								value='#xutil.getChildElement(#itemAttributeGroup,"ItemAttributeList")' />
								<s:set name='tableRowCount' value="%{0}" />
							<s:iterator status="idx"
								value='#xutil.getChildren(#itemAttributeList, "ItemAttribute")'
								id='itemAttribute'>
								<s:set name="assignedValueList_new"
									value='#xutil.getChildElement(#itemAttribute,"AssignedValueList")' />
								<s:set name="assignedValue_new"
									value='#xutil.getChildElement(#assignedValueList_new,"AssignedValue")' />
								<s:if test='%{(#xutil.getAttribute(#assignedValue_new,"Value") != "") && (#xutil.getAttribute(#assignedValue_new,"Value") != null)}' >
									<s:if test="#tableRowCount%2  == 0">
										<tr class="int-deets">
									</s:if>
									<s:else>
										<tr class="odd int-deets">
									</s:else>
									<td><s:property
										value='#xutil.getAttribute(#itemAttribute,"ItemAttributeDescription")' /></td>
									<td class="divider"><s:set name="assignedValueList"
										value='#xutil.getChildElement(#itemAttribute,"AssignedValueList")' />
									<s:set name="attribute"
										value='#xutil.getChildElement(#itemAttribute,"Attribute")' />
									<s:set name="dataType"
										value='#xutil.getAttribute(#attribute,"DataType")' /> <s:set
										name="derivedFrom" value='' /> <s:if
										test="%{null != #xutil.getAttribute(#attribute,'AllowMultipleValues')}">
										<s:set name='allowMultiVals'
											value='%{#xutil.getAttribute(#attribute,"AllowMultipleValues")}' />
									</s:if> <s:if
										test="%{null != #xutil.getAttribute(#attribute,'IsAllowedValueDefined')}">
										<s:set name='valueDefined'
											value='%{#xutil.getAttribute(#attribute,"IsAllowedValueDefined")}' />
									</s:if> <s:if
										test="%{null != #xutil.getAttribute(#attribute,'DerivedFromAttributeKey')}">
										<s:set name='derivedFrom'
											value='%{#xutil.getAttribute(#attribute,"DerivedFromAttributeKey")}' />
									</s:if> <s:iterator
										value='#xutil.getChildren(#assignedValueList, "AssignedValue")'
										id='assignedValue'>
										<%-- commented for jira 3824 <s:if test='%{"" != #derivedFrom}'>
											<s:property
												value='#xutil.getAttribute(#assignedValue,"Value")' />
											<s:property
												value='#xutil.getAttribute(#attribute,"AttributePostFix")' />
										</s:if> --%>
									<%-- commented for 3824jira 
										<s:elseif test="%{#dataType=='TEXT'}">
											<s:property
												value='#xutil.getAttribute(#assignedValue,"Value")' />
											<s:property
												value='#xutil.getAttribute(#attribute,"AttributePostFix")' />
										</s:elseif>
										--%>
										<!-- Jira 3824, Adding To Fetch Asset of Attr -->
										<s:set name="Value" value='#xutil.getAttribute(#assignedValue,"Value")' />
										<s:hidden name="hdn_Value" value="%{#Value}" />
										<s:if test='%{"" != #derivedFrom}'>
											<s:property
												value='#xutil.getAttribute(#assignedValue,"Value")' />
											<s:property
												value='#xutil.getAttribute(#attribute,"AttributePostFix")' />
										</s:if>
										<!-- Jira 3824 - Check if Attribute has Asset -->
										<s:elseif test="%{#dataType=='TEXT'}">
										<s:set name="found" value="false" />
										<!-- Adding Iterator assetLinkMap for Jira 2634 -->
										<s:iterator value="assetLinkMap" id="assetMap" status="status" >
											<s:set name="link" value="value" />
											<s:set name="assetId" value="key" />										
											<s:hidden name="hdn_test" value="%{#assetId}"  />
											<s:if test='%{#assetId == #Value}'>
												<a href="<s:property value='#link'/>" target="_blank"><s:property value='#xutil.getAttribute(#assignedValue,"Value")'/></a>
												<s:property	value='#xutil.getAttribute(#attribute,"AttributePostFix")' />
												<s:set name="found" value="true" />
											</s:if>
										</s:iterator>
										<s:if test="%{#found == false}"> 
											<s:property value='#xutil.getAttribute(#assignedValue,"Value")'/>
											<s:property	value='#xutil.getAttribute(#attribute,"AttributePostFix")' />
										</s:if>
										</s:elseif> <%-- end of jira 3824 --%>
										<s:elseif test="%{#dataType=='BOOLEAN'}">
											<s:property
												value='#xutil.getAttribute(#assignedValue,"Value")' />
											<s:property
												value='#xutil.getAttribute(#attribute,"AttributePostFix")' />
										</s:elseif>
										<s:elseif test="%{#dataType=='INTEGER'}">
											<s:if
												test='%{(#valueDefined == "Y" && allowMultiVals == "Y") || (#derivedFrom != null && #derivedFrom.length() > 0)}'>
												<s:property
													value='#xutil.getAttribute(#assignedValue,"Value")' />
											</s:if>
											<s:else>
												<s:property
													value='#xutil.getAttribute(#assignedValue,"IntegerValue")' />
											</s:else>
											<s:property
												value='#xutil.getAttribute(#attribute,"AttributePostFix")' />
										</s:elseif>
										<s:elseif test="%{#dataType=='DECIMAL'}">
											<s:if
												test='%{(#valueDefined == "Y" && allowMultiVals == "Y") || (#derivedFrom != null && #derivedFrom.length() > 0)}'>
												<s:property
													value='#xutil.getAttribute(#assignedValue,"Value")' />
											</s:if>
											<s:else>
												<s:property
													value='#xutil.getAttribute(#assignedValue,"DoubleValue")' />
											</s:else>
											<s:property
												value='#xutil.getAttribute(#attribute,"AttributePostFix")' />
										</s:elseif>
									</s:iterator></td></tr>
									<s:set name='tableRowCount' value="#tableRowCount+1" />
								</s:if>
							</s:iterator>
						</s:iterator>
					</s:if>
				</s:if>
			</s:iterator>
		</table>
		</div>

				<!-- end tab2 -->
			</div>
			
			<div class="clearall">&nbsp;</div>
		</div>
	</div>
	
        </div>
	
		
		
				<!-- end Tabs -->
                <!-- end main left column -->
		</s:form>
		<div class="prod_detail_bot">&nbsp;</div>
                <!-- begin right column -->
				
			<s:include value="XPEDXItemPromotions.jsp"></s:include>
                <!-- end right column -->
            </div>
        </div>
    </div>
    <!-- // container end -->
		
		</div>
	</div>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- BEGIN footer -->
	<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
<!-- END footer -->

