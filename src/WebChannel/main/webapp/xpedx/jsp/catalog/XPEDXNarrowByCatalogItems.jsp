<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="scui" uri="scuiTag"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>

<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs�. 
    This is to avoid a defect in Struts that�s creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts� OGNL statements. --%>
<!-- Getting the cached xml doc for root catalog -->
<s:set name='catDoc' value='%{outDoc.documentElement}' />
<s:set name='tabStart' value='1001' />
<s:bean
	name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils'
	id='util' />
<s:set name='isGuestUser' value="wCContext.guestUser" />
<script type="text/javascript">	
	function context_newSearch_searchTerm_onclick(obj){
	  	obj.value='';
	  	return;
	  }
</script>

<!-- begin left column -->
<div id="left-col"><!-- breadcrumb -->





<div class="bgleftcol">
	<div id="normal-stock-select">
        		<span class="checkboxtxt"><input type="checkbox" id="search-stocked-check" />&nbsp;Search normally stocked items only</span>
     	</div>
	<div class="browseBox subPanelBox" id="narrow_spb2">
		<div class="header" id="narrow_header2"
			style="background-color:#003399">
			<span class="float-right"><a href="#" class="expand-narrow-by" title="Show/Hide">-</a></span>Product Categories
		</div>
		<div class="content narrowbyattributes" id="narrow_content2">
			<ul>
				<s:set name='count1' value='%{#tabStart + 10}' />
				<s:iterator id='cat'
					value='XMLUtils.getElements(#catDoc, "//CategoryList/Category")'>
					<s:set name='count1' value='%{#count1 + 1}' />
					<s:set name='subCatElem'
						value='XMLUtils.getElements(#cat, "ChildCategoryList/Category")' />
					<s:url id='catURL' namespace='/catalog' action='navigate.action'>
						<s:param name='path' value='#cat.getAttribute("CategoryPath")' />
						<s:param name='cname' value='#cat.getAttribute("ShortDescription")' />
					</s:url>
					<!-- s:if test='#subCatElem.size() > 0'-->
						<s:set name='catCount' value='#cat.getAttribute("Count")' />
						<s:set name='showCurrency' value="false" />
						<li class="roll close"><s:a href='%{catURL}' tabindex="1012">
							<s:property value='#cat.getAttribute("ShortDescription")' /> (<s:property value='#util.formatNumber(#catCount)' />)
							<!--  span><img src="../xpedx/images/XPEDX-linked-arrow-closed.gif"></span -->
						</s:a></li>
					<!-- /s:if-->
				</s:iterator>
			</ul>
		</div>
	</div>
<!-- end browse category --> <!-- begin narrow by  -->
<s:set name='FacetsList' value='XMLUtils.getElements(#catDoc, "//FacetList/ItemAttribute")' />

<s:if test="%{#FacetsList!=null && #FacetsList.size>0}"><%-- Show Narrow By section only if FacetsList size is greater than 0 --%>

<div class="browseBox subPanelBox" id="narrow_spb3">
	<div class="header" id="narrow_header3">
		<span
			style="background-color:#003399">Narrow By
		</span>
		<div class="clear"></div>
		<span style="font-size: 10px;" class="grey">Product Type</span>
		<div style="display: inline;">
		<div class="toggle"><img width="77" height="21" alt=""
			src="<s:property value='#util.staticFileLocation' />/xpedx/images/catalog/view-all-narrow-by-drop-hover.png"
			style="display: inline; position: absolute; margin-top: -42px; margin-left: 82px;"></div>

			<div id="viewAll" class=" favorita x-corners"
			style="background-color: rgb(250, 250, 250); border: 1px solid rgb(200, 200, 200); font-size: 11px; height: 305px; width: 840px; overflow: auto; z-index: 1000; padding: 12px 10px 20px; position: relative; display: none; top: -22px; margin-left: 82px; -moz-border-radius: 0pt 6px 6px 6px;">

<%-- View All Contents STARTS --%>
<s:set name='facetscount' value='#FacetsList.size' />
<s:set name="currentFacetCount" value='0' />

<s:if test="%{#facetscount>1}"><%-- Category contains multiple facets, show itemlist of each in separate columns --%>
<s:iterator id='facets'
		value='XMLUtils.getElements(#catDoc, "//FacetList/ItemAttribute")' >
<s:set name='count1' value='%{#count1 + 1}' />
<s:set name="currentFacetCount" value='%{#currentFacetCount + 1}' />

		<s:set name='facetList'
			value='XMLUtils.getElements(#facets, "AssignedValueList/AssignedValue")' />
		<s:set name='showFacet' value='"Y"' />
		<s:if test='%{#facets.getAttribute("IsProvidedFilter") == "Y"}'>
			<s:set name='showFacet' value='"N"' />
			<s:iterator id='facetVal' value='#facetList'>
				<s:if test='%{#facetVal.getAttribute("IsProvidedFilter") != "Y"}'>
					<s:set name='showFacet' value='"Y"' />
				</s:if>
			</s:iterator>
		</s:if>
		<s:if test='%{#showFacet == "Y"}'>
			
			<s:iterator id='facetVal' value='#facetList' status='facetValIndex'>
				<s:if test='%{#facetVal.getAttribute("IsProvidedFilter") != "Y"}'>
					<s:set name='count1' value='%{#count1 + 1}' />
					<s:url id='narrowURL' namespace='/catalog' action='filter.action'>
						<s:param name='indexField'
							value='#facets.getAttribute("IndexFieldName")' />
						<s:param name='facet' value='#facetVal.getAttribute("Value")' />
						<s:param name='cname' value='#facetVal.getAttribute("Value")' />
					</s:url>
					
					<s:if test="%{#facetValIndex.first}" >
						<s:if test="%{#currentFacetCount<#facetscount}">
						<div class="view-all-expand-hov-height-auto"><b><s:property
				value='#facets.getAttribute("ItemAttributeName")' id="facet" /></b>
						</s:if>
						<s:else>
						<div style="width: 114px; margin-left: 5px; float: left; height: 300px;"><b><s:property
				value='#facets.getAttribute("ItemAttributeName")' id="facet" /></b>
						</s:else>
					</s:if>
					
					<p><s:a href="%{narrowURL}"
						tabindex="%{#count1}"><s:property value='#facetVal.getAttribute("Value")' />
					</s:a></p>
					
					<s:if test="%{#facetValIndex.last}" >
						<s:if test="%{#currentFacetCount<#facetscount}">
						</div>
						</s:if>
						<s:else>
						<div class="toggle"><br />
						<br />
						<br />
						<a href="#" class="grey-ui-btn"><span style="text-decoration: none;">Close</span></a>
						</div>
						</div>
						</s:else>
					</s:if>
			</s:if>	<%-- IsProvidedFilter not eq Y --%>	
			</s:iterator>
		</s:if>
</s:iterator>
</s:if>

<s:else><%-- Category contains only 1 facet --%>
<s:set name="facets" value="%{#FacetsList[0]}" />
		<s:set name='count1' value='%{#count1 + 1}' />
		<s:set name='facetList'
			value='XMLUtils.getElements(#facets, "AssignedValueList/AssignedValue")' />
		<s:set name='showFacet' value='"Y"' />
		<s:if test='%{#facets.getAttribute("IsProvidedFilter") == "Y"}'>
			<s:set name='showFacet' value='"N"' />
			<s:iterator id='facetVal' value='#facetList'>
				<s:if test='%{#facetVal.getAttribute("IsProvidedFilter") != "Y"}'>
					<s:set name='showFacet' value='"Y"' />
				</s:if>
			</s:iterator>
		</s:if>
		<s:if test='%{#showFacet == "Y"}'>
			<div class="roll close"><b><s:property
				value='#facets.getAttribute("ItemAttributeName")' id="facet" /></b></div>
			<s:set name='rowcount' value='%{#facetList.size/6}' />
			<s:set name='rowcount' value='%{#rowcount + 1}' />
			<s:set name='divcount' value='0' />
			
			<s:iterator id='facetVal' value='#facetList' status='facetValIndex'>
				<s:if test='%{#facetVal.getAttribute("IsProvidedFilter") != "Y"}'>
					<s:set name='count1' value='%{#count1 + 1}' />
					<s:url id='narrowURL' namespace='/catalog' action='filter.action'>
						<s:param name='indexField'
							value='#facets.getAttribute("IndexFieldName")' />
						<s:param name='facet' value='#facetVal.getAttribute("Value")' />
						<s:param name='cname' value='#facetVal.getAttribute("Value")' />
					</s:url>
					
					<s:if test="%{(#facetValIndex.index)%(#rowcount)==0}">
						<s:if test="%{(#divcount)!=5}">
						<div class="view-all-expand-hov-height-auto">
						</s:if>
						<s:else>
						<div style="width: 114px; margin-left: 5px; float: left; height: 300px;">
						</s:else>
					</s:if>
					
					
					<p><s:a href="%{narrowURL}"
						tabindex="%{#count1}"><s:property value='#facetVal.getAttribute("Value")' />
					</s:a></p>
					
					<s:if test="%{#facetValIndex.last}">
					<div class="toggle"><br />
					<br />
					<br />
					<a href="#" class="grey-ui-btn"><span style="text-decoration: none;">Close</span></a>
					</div>
					</div>
					</s:if>
					
					<s:if test="%{((#facetValIndex.index)%(#rowcount)==(#rowcount - 1)) && !#facetValIndex.last}" >
					</div>
					<s:set name='divcount' value='%{#divcount  + 1}' />
					</s:if>
					
					</s:if>
			</s:iterator>
		</s:if>
</s:else>
<%-- View All Contents ENDS --%>

<br />
</div>
<script>
										$(".toggle").click(function () {
										$(".favorita").toggle();
										});
										</script></div>
</div>
<div class="content narrowbyattributes" id="narrow_content3"
	style="overflow: auto;">
<ul>
	<s:iterator id='facets'
		value='XMLUtils.getElements(#catDoc, "//FacetList/ItemAttribute")'>
		<s:set name='count1' value='%{#count1 + 1}' />
		<s:set name='facetList'
			value='XMLUtils.getElements(#facets, "AssignedValueList/AssignedValue")' />
		<s:set name='showFacet' value='"Y"' />
		<s:if test='%{#facets.getAttribute("IsProvidedFilter") == "Y"}'>
			<s:set name='showFacet' value='"N"' />
			<s:iterator id='facetVal' value='#facetList'>
				<s:if test='%{#facetVal.getAttribute("IsProvidedFilter") != "Y"}'>
					<s:set name='showFacet' value='"Y"' />
				</s:if>
			</s:iterator>
		</s:if>
		<s:if test='%{#showFacet == "Y"}'>
			<li class="roll close"><b><s:property
				value='#facets.getAttribute("ItemAttributeName")' id="facet" /></b></li>
			<s:iterator id='facetVal' value='#facetList'>
				<s:if test='%{#facetVal.getAttribute("IsProvidedFilter") != "Y"}'>
					<s:set name='count1' value='%{#count1 + 1}' />
					<s:url id='narrowURL' namespace='/catalog' action='filter.action'>
						<s:param name='indexField'
							value='#facets.getAttribute("IndexFieldName")' />
						<s:param name='facet' value='#facetVal.getAttribute("Value")' />
						<s:param name='cname' value='#facetVal.getAttribute("Value")' />
					</s:url>
					<li class="roll close"><s:a href="%{narrowURL}"
						tabindex="%{#count1}">
						<s:property value='#facetVal.getAttribute("Value")' />
					</s:a></li>
				</s:if>
			</s:iterator>
		</s:if>
	</s:iterator>
</ul>
</div>
</div>

</s:if><%-- if condition to check for FacetList size ENDS --%>
<!-- end narrow by  --></div>
</div>
<!-- end left column -->

<s:iterator id='facets'
	value='XMLUtils.getElements(#catDoc, "//FacetList/ItemAttribute")'>
	<s:set name='count1' value='%{#count1 + 1}' />
	<s:set name='facetList'
		value='XMLUtils.getElements(#facets, "AssignedValueList/AssignedValue")' />
	<s:set name='showFacet' value='"Y"' />
	<s:if test='%{#facets.getAttribute("IsProvidedFilter") == "Y"}'>
		<s:set name='showFacet' value='"N"' />
		<s:iterator id='facetVal' value='#facetList'>
			<s:if test='%{#facetVal.getAttribute("IsProvidedFilter") != "Y"}'>
				<s:set name='showFacet' value='"Y"' />
			</s:if>
		</s:iterator>
	</s:if>
	<s:set name='AttributeValue'
		value='#facets.getAttribute("ItemAttributeName")' />
	<s:set name='AttributeValue' value='%{"viewAll" + #AttributeValue}' />

	<s:if test='%{#showFacet == "Y"}'>
		<swc:dialogPanel title="View All" isModal="true"
			id='${AttributeValue}'>

			<table class="listTableBody padding-left" width="100%">

				<s:property value='#facets.getAttribute("ItemAttributeName")' />

				<s:set name='noOfItems' value='0' />
				<s:iterator id='facetVal' value='#facetList'>
					<s:if test='#noOfItems == "0" '>
						<tr>
					</s:if>
					<s:if test='%{#facetVal.getAttribute("IsProvidedFilter") != "Y"}'>
						<s:set name='noOfItems' value='%{#noOfItems + 1}' />
						<td><s:url id='narrowURL' namespace='/catalog'
							action='filter.action'>
							<s:param name='indexField'
								value='#facets.getAttribute("IndexFieldName")' />
							<s:param name='facet' value='#facetVal.getAttribute("Value")' />
							<s:param name='cname' value='#facetVal.getAttribute("Value")' />
						</s:url> <s:a href="%{narrowURL}" tabindex='%{#count1}'>
							<s:property value='#facetVal.getAttribute("Value")' />
		                  &nbsp;(<s:property
								value='#facetVal.getAttribute("Count")' />)
		              </s:a></td>
					</s:if>
					<s:if test='#noOfItems == "2"'>
						<s:set name='noOfItems' value='0' />
						</tr>
					</s:if>
				</s:iterator>

			</table>
		</swc:dialogPanel>
	</s:if>
</s:iterator>

<script>
function showViewAllDialog(attr)
{
	DialogPanel.show("viewAll" + attr.value );
    svg_classhandlers_decoratePage();
}
function setDefaultSearchText()
{
	if( document.getElementById("search_searchTerm").value=="Search Within Results..."){
		document.getElementById("search_searchTerm").value="";
	}
}
</script>