<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<s:if test='combinedAlternateItems.size() > 0'>
	<div class="associated-items-wrap" style="display:none;">
		<h3>You might also want to consider...</h3>
		
		<div class="liquid-carousel">
			<span class="previous"></span>
			
			<div class="wrapper">
				<ul>
					<s:iterator value='combinedAlternateItems' id='itemElem'>
						<s:set name="upItemPrimInfoElem" value='#xutil.getChildElement(#itemElem, "PrimaryInformation")' />
						<s:set name="upItemComputedPrice" value='#xutil.getChildElement(#itemElem, "ComputedPrice")' />
						<s:set name="itemAssetList" value='#xutil.getElementsByAttribute(#itemElem, "AssetList/Asset", "Type", "ITEM_IMAGE_1" )' />
						
						<s:url id='detailURLFromPromoProd' namespace='/catalog' action='itemDetails.action'>
							<s:param name='itemID'>
								<s:property value='#xutil.getAttribute(#itemElem, "ItemID")' />
							</s:param>
							<s:param name='unitOfMeasure'>
								<s:property value='#xutil.getAttribute(#itemElem, "UnitOfMeasure")' />
							</s:param>
							<%--  WebTrends tag start --%>
							<s:param name='prItemtype'>
								<s:property value="%{'U'}" />
							</s:param>
							<%--  WebTrends tag end --%>
						</s:url>
						<li>
							<s:set name="description" value='#xutil.getAttribute(#upItemPrimInfoElem, "ShortDescription")' />
							<s:a href="%{detailURLFromPromoProd}">
								<s:if test='#itemAssetList != null && #itemAssetList.size() > 0'>
									<s:set name="itemAsset" value='#itemAssetList[0]' />
									<s:set name='imageLocation' value="#xutil.getAttribute(#itemAsset, 'ContentLocation')" />
									<s:set name='imageId' value="#xutil.getAttribute(#itemAsset, 'ContentID')" />
									<s:set name='imageLabel' value="#xutil.getAttribute(#itemAsset, 'Label')" />
									<s:set name='imageURL' value="#imageLocation + '/' + #imageId" />
									<img src="<s:url value='%{#imageURL}' includeParams='none' />" align="left" />
								</s:if>
								<s:else>
									<s:set name='imageBlank' value='%{#wcUtil.staticFileLocation + "/xpedx/images/INF_150x150.jpg"}' />
									<img src="<s:url value='%{#imageBlank}'/>" align="left" />
								</s:else>
								
								<span class="short-description">
									<s:set name='displayDescription' value='%{#description}'/>
									<s:if test='#displayDescription.trim().equals("")'>
										<s:set name='displayDescription' value='%{#xutil.getAttribute(#upItemPrimInfoElem, "ExtendedDescription")}' />
									</s:if>
									<s:if test="%{(#description.length()) > 50}">
										<s:set name='displayDescription' value='%{#description.substring(0, 50) + "..."}'/>
									</s:if>
									<s:property	value='%{#displayDescription}' />
								</span>
							</s:a>
						</li>
					</s:iterator>
				</ul>
			</div>
			
			<span class="next"></span>
		</div>
	</div> <%-- / associated-items-wrap (alternates) --%>
</s:if>

<s:if test='combinedCrosssellItems.size() > 0'>
	<div class="associated-items-wrap" style="display:none;">
		<h3>Popular Accessories</h3>
		
		<div class="liquid-carousel">
			<span class="previous"></span>
			
			<div class="wrapper">
				<ul>
					<s:iterator value='combinedCrosssellItems' id='itemElem'>
						<s:set name="upItemPrimInfoElem" value='#xutil.getChildElement(#itemElem, "PrimaryInformation")' />
						<s:set name="upItemComputedPrice" value='#xutil.getChildElement(#itemElem, "ComputedPrice")' />
						<s:set name="itemAssetList" value='#xutil.getElementsByAttribute(#itemElem, "AssetList/Asset", "Type", "ITEM_IMAGE_1" )' />
						
						<s:url id='detailURLFromPromoProd' namespace='/catalog' action='itemDetails.action'>
							<s:param name='itemID'>
								<s:property value='#xutil.getAttribute(#itemElem, "ItemID")' />
							</s:param>
							<s:param name='unitOfMeasure'>
								<s:property value='#xutil.getAttribute(#itemElem, "UnitOfMeasure")' />
							</s:param>
							<%--  WebTrends tag start --%>
							<s:param name='prItemtype'>
								<s:property value="%{'U'}" />
							</s:param>
							<%--  WebTrends tag end --%>
						</s:url>
						<li>
							<s:set name="description" value='#xutil.getAttribute(#upItemPrimInfoElem, "ShortDescription")' />
							<s:a href="%{detailURLFromPromoProd}">
								<s:if test='#itemAssetList != null && #itemAssetList.size() > 0'>
									<s:set name="itemAsset" value='#itemAssetList[0]' />
									<s:set name='imageLocation' value="#xutil.getAttribute(#itemAsset, 'ContentLocation')" />
									<s:set name='imageId' value="#xutil.getAttribute(#itemAsset, 'ContentID')" />
									<s:set name='imageLabel' value="#xutil.getAttribute(#itemAsset, 'Label')" />
									<s:set name='imageURL' value="#imageLocation + '/' + #imageId" />
									<img src="<s:url value='%{#imageURL}' includeParams='none' />" align="left" />
								</s:if>
								<s:else>
									<s:set name='imageBlank' value='%{#wcUtil.staticFileLocation + "/xpedx/images/INF_150x150.jpg"}' />
									<img src="<s:url value='%{#imageBlank}'/>" align="left" />
								</s:else>
								
								<span class="short-description">
									<s:set name='displayDescription' value='%{#description}'/>
									<s:if test='#displayDescription.trim().equals("")'>
										<s:set name='displayDescription' value='%{#xutil.getAttribute(#upItemPrimInfoElem, "ExtendedDescription")}' />
									</s:if>
									<s:if test="%{(#description.length()) > 50}">
										<s:set name='displayDescription' value='%{#description.substring(0, 50) + "..."}'/>
									</s:if>
									<s:property	value='%{#displayDescription}' />
								</span>
							</s:a>
						</li>
					</s:iterator>
				</ul>
			</div>
		</div> <%-- / liquid-carousel --%>
	</div> <%-- / associated-items-wrap (cross sell) --%>
</s:if>
