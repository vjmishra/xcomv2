 <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 

<s:bean name='com.sterlingcommerce.framework.utils.SCXmlUtils' id='util' />
<s:bean name='com.sterlingcommerce.webchannel.utilities.XMLUtilities' id='xmlUtilities' />
<s:set name='_action' value='[0]'/>
<s:set name="xutil" value="XMLUtils"/>
<!--s:set name='sDoc' value='#attr["RootCatalogDoc"].documentElement' / -->
<s:set name='catDoc' value="#_action.getMainCatsDoc()" />
<s:set name='userPrefcategoryElem' value="#_action.getPrefCategoryElem()" />
<s:set name='userPrefcategoryPath' value='#userPrefcategoryElem.getAttribute("CategoryPath")' />


<s:url id='userPrefCatURL' namespace='/catalog' action='navigate.action'>
<s:param name='path' value='#userPrefcategoryElem.getAttribute("CategoryPath")'/>
<s:param name='cname' value='#userPrefcategoryElem.getAttribute("ShortDescription")'/>
<s:param name='newOP' value='%{true}'/>
</s:url>
<s:set name='assetIdValuelr' value='#userPrefcategoryElem.getAttribute("ShortDescription")+" Large"' />
<s:set name='catAssetlr' value='#xmlUtilities.getElementByAttribute(#userPrefcategoryElem, "AssetList/Asset", "AssetID", #assetIdValuelr, true)' />
<s:set name='contentlocationlr' value='#catAssetlr.getAttribute("ContentLocation")' />
<s:set name='contentidlr' value='#catAssetlr.getAttribute("ContentID")' />

	
			<td colspan="2" class="colspan-two">
				<table id="primary-category">
					<tr>
						<td class="category-promo">
                			<img src="<s:property value="#contentlocationlr"/>/<s:property value="#contentidlr"/>" width="150" height="150" 
                				title="<s:property value='#userPrefcategoryElem.getAttribute("ShortDescription")'/> Image" 
                				alt="" /> 							
							<br />
							<a class="orange-ui-btn" href="<s:property value="#userPrefCatURL" escape="false"/>">
								<span>Shop <s:property value='#userPrefcategoryElem.getAttribute("ShortDescription")'/></span></a>
						</td>
						<td>
							<ul>
					            <s:iterator value='XMLUtils.getElements(#userPrefcategoryElem, "ChildCategoryList/Category")' id='subCats' status="rowStatus">
						            <s:url id='userPrefSubCatURL' namespace='/catalog' action='navigate.action'>
										<s:param name='path' value='#subCats.getAttribute("CategoryPath")'/>
										<s:param name='cname' value='#subCats.getAttribute("ShortDescription")'/>
										<s:param name='newOP' value='%{true}'/>
										<s:param name='CategoryC3' value='%{true}'/>
										
									</s:url>
									
									<s:if test='#rowStatus.modulus(8) == 0'>
										<ul>
									</s:if>	
										<li>
							           		<s:a href='%{userPrefSubCatURL}' cssClass="underlink" ><s:property value='#subCats.getAttribute("ShortDescription")'/></s:a>
							           	</li>
							        <s:if test='#rowStatus.modulus(7) == 0'>
										</ul>
									</s:if>	
									<s:if test='#rowStatus.last && #rowStatus.modulus(7) != 0'>
										</ul>
									</s:if>
							     </s:iterator>
						</td>
					</tr>	
			</table>
		</td>
		
		<td>
           	<s:set name='noOfCatsDisplayed' value="%{0}" />
           	<s:iterator value='XMLUtils.getElements(#catDoc, "//CategoryList/Category")' id='cat' status="rowStatus">
			  	<s:set name='assetIdValue' value='#cat.getAttribute("ShortDescription")+" Small"' />
				<s:set name='catAsset' value='#xmlUtilities.getElementByAttribute(#cat, "AssetList/Asset", "AssetID", #assetIdValue, true)' />
				<s:set name='contentlocation' value='#catAsset.getAttribute("ContentLocation")' />
			  	<s:set name='contentid' value='#catAsset.getAttribute("ContentID")' />
				<s:url id='catURL' namespace='/catalog' action='navigate.action'>
					<s:param name='path' value='#cat.getAttribute("CategoryPath")'/>
					<s:param name='cname' value='#cat.getAttribute("ShortDescription")'/>
					<s:param name='newOP' value='%{true}'/>
				</s:url>
				<s:set name='catPath' value="#cat.getAttribute('CategoryPath')"/>
				<s:if test="%{#userPrefcategoryPath != #catPath}">
					<s:if test="#noOfCatsDisplayed >= 3" >		
            		</s:if>
            		<s:else>
            			<div class="category">
	            			<img src="<s:property value="#contentlocation"/>/<s:property value="#contentid"/>" alt=""/>
							<s:a href="%{catURL}" cssClass="orange-ui-btn"><span>Shop <s:property value='#cat.getAttribute("ShortDescription")'/></span>
							</s:a>	
	            		</div>
	            		<s:set name="noOfCatsDisplayed" value="%{#noOfCatsDisplayed+1}"></s:set>
            		</s:else>
            	</s:if>
               </s:iterator>
		</td>
	