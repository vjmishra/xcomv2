<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<div style="display: none;" id='replacementModal'>
		<div class="xpedx-light-box">
			<s:set name='tabIndex' value='3001' />
			<s:iterator value='xpedxItemIDUOMToReplacementListMap'>
				<s:set name='altItemList' value='value' />
				<div id="replacement_<s:property value='key'/>">
					<h1>
						Replacement Item(s) for
						<s:property value="wCContext.storefrontId" />
						Item #:
						<s:property value='key' />
					</h1>
					<s:if test="#altItemList.size() > 0">
						<input type="hidden" id="rListSize_<s:property value='key'/>"
							value="<s:property value='#altItemList.size()'/>" />
					</s:if>
					<s:iterator value='#altItemList' id='altItem' status='iStatus'>
						<s:div cssClass="replace-item-wrap %{#iStatus.last ? 'last' : ''}">
							<s:set name='uId'
								value='%{key + "_" +#altItem.getAttribute("ItemID")}' />
							<div class="replace-radio">
								<s:if test="#altItemList.size() == 1">
									<input type="hidden" id="hUId_<s:property value='key'/>"
										value='<s:property value="#uId" />' />
								</s:if>
								<s:else>
									<input name="relatedItems"
										onclick="setUId('<s:property value="#uId" />');" type="radio" />
								</s:else>
							</div>
							<a class="item-lnk"
								href='<s:property value="%{itemDetailsLink}" />'> <img
								class="prodImg" src="<s:property value='%{#pImg}' />"
								width="150" height="150" alt="" />
							</a>
							<s:set name='altItemIDUOM'
								value='#_action.getIDUOM(#altItem.getAttribute("ItemID"), #altItem.getAttribute("UnitOfMeasure"))' />
							<s:set name='altItemPrimaryInfo'
								value='#util.getElement(#altItem, "PrimaryInformation")' />
							<s:set name='name'
								value='%{#altItemPrimaryInfo.getAttribute("ShortDescription")}' />
							<s:set name='rItemID' value='%{#altItem.getAttribute("ItemID")}' />
							<s:set name='rdesc1' value="%{#itemDescMap.get(rItemID)}" />
							<s:set name='rdesc'
								value='%{#altItemPrimaryInfo.getAttribute("Description")}' />
							<s:url id='pImg'
								value='%{#_action.getImagePath(#altItemPrimaryInfo)}' />

							<s:set name='ritemUomId'
								value='#altItem.getAttribute("UnitOfMeasure")' />
							<s:set name='ritemType' value='#altItem.getAttribute("ItemType")' />

							<s:url id='ritemDetailsLink' namespace="/catalog"
								action='itemDetails.action' includeParams='none'
								escapeAmp="false">
								<s:param name="itemID" value="#rItemID" />
								<s:param name="sfId" value="#parameters.sfId" />
								<s:param name="unitOfMeasure" value="#ritemUomId" />
							</s:url>
							<s:hidden name="replacement_%{uId}_itemid" value='%{#rItemID}' />
							<s:hidden name="replacement_%{uId}_name" value='%{#name}' />
							<s:hidden name="replacement_%{uId}_desc" value='%{#rdesc}' />

							<s:set name='altItemUomList'
								value='itemIdsUOMsDescMap.get(#rItemID)' />
							<s:set name="repItemUOM" value="" />
							<s:iterator value="altItemUomList" id="itemUOM"
								status="repItemUOMStatus">
								<s:if test="%{#repItemUOMStatus.first}">
									<s:set name="repItemUOM" value="key" />
								</s:if>
							</s:iterator>
							<s:hidden id="replacement_%{uId}_uom"
								name="replacement_%{uId}_uom" value="%{#repItemUOM}" />
							<div class="contents width-400 addmarginleft5">
								<div class="descriptions">
									<s:if test="%{#ritemType != 99}">
										<a href='<s:property value="%{ritemDetailsLink}" />'> <span
											class="full-description-replacement-model"> <s:property
													value="#name" />
										</span>
										</a>
									</s:if>
									<div class="clearfix"></div>
									<div class="buttons">
										<ul class="prodlist">
											<a href='<s:property value="%{ritemDetailsLink}" />'> <s:property
													value='#rdesc' escape='false' />
											</a>
										</ul>
									</div>
								</div>
								<div class="item_number-wrap">
									<b> <s:property value="wCContext.storefrontId" /> Item #:
										<s:property value="#rItemID" />
									</b>
								</div>
								<s:if test='skuMap!=null && skuMap.size()>0'>
									<s:set name='rItemSkuMap' value='%{skuMap.get(#rItemID)}' />
									<s:set name='rMfgItemVal'
										value='%{#rItemSkuMap.get(@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MFG_ITEM_NUMBER)}' />
									<s:set name='rPartItemVal'
										value='%{#rItemSkuMap.get(@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CUST_PART_NUMBER)}' />
								</s:if>
								<s:if test='mfgItemFlag != null && mfgItemFlag=="Y"'>
									<div class="mfg-numbers">
										<s:property value="#manufacturerItemLabel" />
										:
										<s:property value='#rMfgItemVal' />
									</div>
								</s:if>
								<s:if test='customerItemFlag != null && customerItemFlag=="Y"'>
									<div>
										<s:property value="#customerItemLabel" />
										:
										<s:property value='#rPartItemVal' />
									</div>
								</s:if>
							</div>
						</s:div>
					</s:iterator>
				</div>
			</s:iterator>
		</div>
	</div>
</body>
</html>