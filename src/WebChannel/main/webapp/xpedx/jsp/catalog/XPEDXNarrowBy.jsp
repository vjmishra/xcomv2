<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="scui" uri="scuiTag"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>

<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs?. 
    This is to avoid a defect in Struts that?s creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts? OGNL statements. --%>
<!-- Getting the cached xml doc for root catalog -->
<s:set name='catDoc' value='%{outDoc.documentElement}' />
<s:set name='tabStart' value='1001' />
<s:url id="getFacetListURL" action="getFacetList" escapeAmp="false">
	<s:param name="marketingGroupId" value="#parameters.marketingGroupId" />
</s:url>
<s:url id="getNarrowByListURL" action="getNarrowByList" escapeAmp="false">
	<s:param name="marketingGroupId" value="#parameters.marketingGroupId" />
</s:url>
<s:bean
	name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils'
	id='util' />
<s:set name='isGuestUser' value="wCContext.guestUser" />

<script type="text/javascript">	
function setStockItemFlag()
{
	if(document.getElementById('stockedItemChk').value == 'true')
	{
		document.getElementById('stockedItem').value = true;
	}
	if(document.getElementById('stockedItemChkBtm').value == 'true')
	{
		document.getElementById('stockedItem').value = true;
	}
}
</script>
<!-- Added script to disable the user to enter text in searchbox before the page loads completely -->
<script type="text/javascript">
window.addEventListener("load", function() { document.getElementById('search_searchTerm').disabled = false; }, false);
</script>

<!-- begin left column -->
<s:set name='FacetsList' value='XMLUtils.getElements(#catDoc, "//FacetList/ItemAttribute")' />
<s:set name='narrowByCatalogItemsCount' value='%{0}' />
<s:set name='expandNarrowByCatalogItems' value='"Y"' />

<%-- NarrowBy JSP is run twice - first when page loads to display Prod Cat, then after Ajax for rest of narrow bys --%>
<%-- Need to run this Prod Cat block only on first time called (search with no results made us add param for this) --%>
<s:if test='%{(#parameters.secondCallForFacets == null)}'>

	<div id="left-col">
	<div class="bgleftcol">

	<s:form name='narrowSearch' action="search">
		<div class="searchbox-form1">
			<div class="catalog-search-container">
				<input id="search_searchTerm" class="x-input input-watermark" data-watermark="Search Within Results..." name="searchTerm" tabindex="1002" type="text" disabled="disabled">
				<input name="stockedItem" value="false" id="stockedItem" type="hidden">
			</div>
		</div>
	</s:form>

			<s:iterator id='subCatElem_dup' value='XMLUtils.getElements(#catDoc, "//CategoryList/Category")'>
				<s:set name='subCatElem_count' value='#subCatElem_dup.getAttribute("ShortDescription")' />
				<s:if test='#subCatElem_count!=null'>
					<s:set name='subCatElem_flag' value='true' />
				</s:if>
			</s:iterator>
			<s:if test='#subCatElem_count!=null'>
				<s:set name='narrowByCatalogItemsCount' value='%{#narrowByCatalogItemsCount + 1}' />
				<div id="narrow_spb2" class="browseBox subPanelBox">
					<div id="narrow_header2" class="header">
						<span class="float-right">
						<a href="#" class="expand-narrow-by" title="Show/Hide">
							<img src="<s:property value='#util.staticFileLocation' />/xpedx/images/icons/12x12_white_collapse.png" style="margin-top:5px" alt="expand">
						</a>
						</span>
						<s:if test='#subCatElem_flag'>
								 Product Categories
						</s:if>
					</div>

					<div id="narrow_content2" class="content narrowbyattributes catalog-landing">
						<ul>
							<s:set name='count1' value='%{#tabStart + 10}' />
							<s:iterator id='cat' value='XMLUtils.getElements(#catDoc, "//CategoryList/Category")'>
								<s:set name='count1' value='%{#count1 + 1}' />
								<s:set name='subCatElem' value='XMLUtils.getElements(#cat, "ChildCategoryList/Category")' />
								<s:url id='catURL' namespace='/catalog' action='navigate.action'>
									<s:param name='path' value='#cat.getAttribute("CategoryPath")' />
									<s:param name='cname' value='#cat.getAttribute("ShortDescription")' />
									<s:param name='marketingGroupId' value='#parameters.marketingGroupId' />
								</s:url>
								<s:url id='cat3URL' namespace='/catalog' action='navigate.action'>
									<s:param name='path' value='#cat.getAttribute("CategoryPath")' />
									<s:param name='cname' value='#cat.getAttribute("ShortDescription")' />
									<s:param name='categoryDepthNarrowBy' value='#categoryDepthNarrowBy' />
									<s:param name='marketingGroupId' value='#parameters.marketingGroupId' />
								</s:url>

								<!-- s:if test='#subCatElem.size() > 0'-->
								<s:set name='catCount' value='#cat.getAttribute("Count")' />
								<s:set name='showCurrency' value="false" />
								<li class="roll close">
									<s:if test="%{#categoryDepthNarrowBy =='2' }">
										<s:a href='%{#cat3URL}' tabindex="1012">
											<span>(<s:property value='#util.formatNumber(#catCount)' />)</span>
											<s:property value='#cat.getAttribute("ShortDescription")' />
										</s:a>
									</s:if>
									<s:else>
										<s:a href='%{#catURL}' tabindex="1012">
											<span>(<s:property value='#util.formatNumber(#catCount)' />)</span>
											<s:property value='#cat.getAttribute("ShortDescription")' />
										</s:a>
									</s:else>
								</li>
								<!-- /s:if-->
							</s:iterator>

						</ul>
					</div>
				</div>
			</s:if>
	<!-- end browse category -->

	<%-- this div will be replaced with results of ajax call --%>
	<div id="narrowByDivList">

</s:if>

<%-- Ajax results of getting facets are used to populate this on second pass through this JSP --%>
<%-- Show Narrow By section only if FacetsList size is greater than 0 --%>
<s:if test="%{#FacetsList!=null && #FacetsList.size>0}">

	<s:set name='headercount' value='%{3}' />

		<s:iterator id='facets'
			value='XMLUtils.getElements(#catDoc, "//FacetList/ItemAttribute")'>
			<div id="narrow_spb<s:property value='#headercount'/>" class="browseBox subPanelBox">
			<s:set name='count1' value='%{#count1 + 1}' />
			<s:set name='facetList'
				value='XMLUtils.getElements(#facets, "AssignedValueList/AssignedValue")' />
			<s:set name='hasMoreFacetList'
				value='XMLUtils.getElements(#facets, "AssignedValueList").get(0).getAttribute("HasMoreAssignedValues")' />

			<%-- <s:set name='showFacet' value='"Y"' /> --%>


			<!-- Webtrends tag start -->
				<META Name="DCSext.w_x_narrowby" Content="1" />
			<!-- Webtrends tag end -->


				<s:set name='narrowByCatalogItemsCount' value='%{#narrowByCatalogItemsCount + 1}' />
				<s:set name='headercount' value='%{#headercount + 1}' />
				<s:set name='AttributeElement1' value='XMLUtils.getChildElement(#facets, "Attribute")' />
				<s:set name='ShortDescription1' value='#AttributeElement1.getAttribute("ShortDescription")' />										

				<div id="narrow_header<s:property value='#headercount'/>" class="header">
					<span class="float-right"><a href="#" class="expand-narrow-by" title="Show/Hide">
							<img src="<s:property value='#util.staticFileLocation' />/xpedx/images/icons/12x12_white_collapse.png" style="margin-top:5px" alt="expand"></a>
					</span>
					<s:property value='#ShortDescription1' id="facet" />
				</div>
				<s:if test='%{#expandNarrowByCatalogItems == "Y"}'>
					<div id="narrow_content<s:property value='#headercount'/>" class="content narrowbyattributes catalog-landing">
				</s:if>
				<s:else>
					<div id="narrow_content<s:property value='#headercount'/>" class="content narrowbyattributes default-collapsed">
				</s:else>

			<ul>
				<s:set name='facetMap' value='facetListMap.get(#ShortDescription1)'/>
				<div id='narrowByDiv_<s:property value="#ShortDescription1" />' >
					<s:iterator value="facetMap" id="factVal">
							<s:set name='count1' value='%{#count1 + 1}' />
							<s:url id='narrowURL' namespace='/catalog' action='filter.action'>						

								<s:param name='_bcs_' value='_bcs_' />
								<s:param name='indexField'
									value='#facets.getAttribute("IndexFieldName")' />
								<s:param name='facet' value='#factVal.getAttribute("Value")' />
								<s:param name='cname' value='#factVal.getAttribute("Value")' />
								<s:param name='filterDesc' value='#ShortDescription1' />
								<s:param name="categoryPath" value='#parameters.path'/>
								<s:param name="path" value='#parameters.path'/>
								<s:param name='marketingGroupId' value='#parameters.marketingGroupId' />
							</s:url>

						<s:set name='facetCount' value='#factVal.getAttribute("Count")' />
						<li class="roll close">
							<s:a href="%{narrowURL}" tabindex="%{#count1}">
								<s:property value='#factVal.getAttribute("Value")' />
							</s:a>
							<span>(<s:property value='#util.formatNumber(#facetCount)' />)</span>
						</li>

					</s:iterator>
					<s:if test='%{#hasMoreFacetList == "Y"}'>
						<a class="narrowByViewAllLink" style=""
							href="javascript:getFacetList('<s:property value='#ShortDescription1' />','<s:property value='#facets.getAttribute("ItemAttributeKey")' />');">View All</a> 
					</s:if>
				</div>

			</ul>
			</div>	

			</div>
	</s:iterator>

</s:if><%-- if condition to check for FacetList size ENDS --%>
</div>

<s:if test="%{ !(#FacetsList!=null && #FacetsList.size>0)}">
<!-- end narrow by  --></div>
</div>
</s:if>
<!-- end left column -->

<script>
function replaceAll(Source,stringToFind,stringToReplace){
	var temp = Source;
	var index = temp.indexOf(stringToFind);
	while(index != -1){
		temp = temp.replace(stringToFind,stringToReplace);
		index = temp.indexOf(stringToFind);
	}
	return temp;
}

Ext.onReady(function(){		
	var inutXML='<s:property value ="searchIndexInputXML" />'.replace(/&(lt|gt|quot);/g, function (m, p) { 
	    return (p == "lt")? "<" : (p == "gt") ? ">" : "'";
	  });
	var divID="narrowByDivList";
	var div=document.getElementById(divID);
	var url = '<s:property value ="#getNarrowByListURL" escape="false" />';
		Ext.Ajax.request({
			url: url,
			params: {
				searchIndexInputXML:inutXML,
				secondCallForFacets:true
			},
			method: 'POST',
			success: function (response, request){
				div.innerHTML=response.responseText;

				// eb-2296: we must bind click events for these new dom elements
				//    note that this function is also called on page load (see xpedx-header.js)
				//     since the 'Product Categories' narrow-by is loaded separately (at page load)
				rebindClickForNarrowByExpandCollapse();
			},
			failure:function (response, request){

			}
		});
});
function showViewAllDialog(attr)
{
	DialogPanel.show("viewAll" + attr.value );
    svg_classhandlers_decoratePage();
}
var myMask;
function showProcessingMessage()
{
	//added for jira 3974
	var waitMsg = Ext.Msg.wait("Processing...");
	myMask = new Ext.LoadMask(Ext.getBody(), {msg:waitMsg});
	myMask.show();
}
function getFacetList(shortDescription,itemAttributeKey)
{
	var inutXML='<s:property value ="searchIndexInputXML" />'.replace(/&(lt|gt|quot);/g, function (m, p) { 
	    return (p == "lt")? "<" : (p == "gt") ? ">" : "'";
	  });
	var divID="narrowByDiv_"+shortDescription;
	var div=document.getElementById(divID);
	var url = '<s:property value ="#getFacetListURL" escape="false" />';
	// if(currentDivIndex == null || currentDivIndex=="")
	//	currentDivIndex="0"; 
	//if(div != null && dev != undefined)
	//{
		Ext.Ajax.request({
			url: url,
			params: {
				facetDivShortDescription: shortDescription,
				//facetCurrentDivIndex: currentDivIndex,
				searchIndexInputXML:inutXML,
				facetListItemAttributeKey:itemAttributeKey
			},
			method: 'POST',
			success: function (response, request){
				div.innerHTML=response.responseText;
			},
			failure:function (response, request){

			}
		});
	//}
}
function replaceAll(Source,stringToFind,stringToReplace){
	  var temp = Source;
	    var index = temp.indexOf(stringToFind);
	        while(index != -1){
	            temp = temp.replace(stringToFind,stringToReplace);
	            index = temp.indexOf(stringToFind);
			}
	        return temp;
	}

</script>