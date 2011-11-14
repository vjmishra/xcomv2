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
<s:bean
	name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils'
	id='util' />
<s:set name='isGuestUser' value="wCContext.guestUser" />
<script type="text/javascript">	
	function context_newSearch_searchTerm_onclick(obj){
		if(obj.value == 'Search Within Results...')
	  		{ obj.value=''; }
		else
			{ }
	  	return;
	  }
	  
</script>

<script type="text/javascript">	
function setStockItemFlag()
{
	if(document.getElementById('stockedItemChk').value == 'true')
	{
		document.getElementById('stockedItem').value = true;
	}
}
</script>

<!-- begin left column -->
<div id="left-col"><!-- breadcrumb -->
<div class="bgleftcol">
					<%--<div id="normal-stock-select">
					<s:if test="#isGuestUser == false">	
					<s:set name="checkedval" value="%{getWCContext().getWCAttribute('StockedCheckbox')}"/>
					<s:checkbox id="stockedItemChk" name="stockedItemChk" fieldValue="true" value="#checkedval" onchange="javascript:setNormallyStockedCheckbox();setStockItemFlag();"></s:checkbox>&nbsp;
				<span class="checkboxtxt">Search normally stocked items only</span>
					</div>
					</s:if> --%>
			<s:iterator id='subCatElem_dup' value='XMLUtils.getElements(#catDoc, "//CategoryList/Category")'>
				<s:set name='subCatElem_count' value='#subCatElem_dup.getAttribute("ShortDescription")' />
				 <s:if test='#subCatElem_count!=null'>
					<s:set name='subCatElem_flag' value='true' />
				 </s:if>
			  </s:iterator>
					 <s:if test='#subCatElem_count!=null'>
						<div id="narrow_spb2" class="browseBox subPanelBox"> 
						<div id="narrow_header2" class="header" style="background-color:#003399">
						  <span class="float-right">
						    <a href="#" class="expand-narrow-by" title="Show/Hide">
						      <img src="../xpedx/images/icons/12x12_white_collapse.png" style="margin-top:5px" alt="expand">
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
								</s:url>
								<!-- s:if test='#subCatElem.size() > 0'-->
									<s:set name='catCount' value='#cat.getAttribute("Count")' />
									<s:set name='showCurrency' value="false" />
									<li class="roll close"><s:a href='%{catURL}' tabindex="1012"><span>(<s:property value='#util.formatNumber(#catCount)' />)</span>
										<s:property value='#cat.getAttribute("ShortDescription")' />
									</s:a></li>
								<!-- /s:if-->
							</s:iterator>
						</ul>
						</div>
						</div>
			</s:if>
<!-- end browse category --> <!-- begin narrow by  -->
<s:set name='FacetsList' value='XMLUtils.getElements(#catDoc, "//FacetList/ItemAttribute")' />

<s:if test="%{#FacetsList!=null && #FacetsList.size>0}"><%-- Show Narrow By section only if FacetsList size is greater than 0 --%>

<!-- 
<div style="display: inline;">
<div class="toggle"><img width="77" height="21" alt=""
	src="../xpedx/images/catalog/view-all-narrow-by-drop-hover.png"
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
						<div class="view-all-expand-hov-height-auto"><b>
						<s:set name='AttributeElement4' value='XMLUtils.getChildElement(#facets, "Attribute")' />
						<s:set name='ShortDescription4' value='#AttributeElement4.getAttribute("ShortDescription")' />
						<s:property value='#ShortDescription4' id="facet" />
						</b>
						</s:if>
						<s:else>
						<div style="width: 114px; margin-left: 5px; float: left; height: 300px;"><b>
						
						<s:set name='AttributeElement3' value='XMLUtils.getChildElement(#facets, "Attribute")' />
						<s:set name='ShortDescription3' value='#AttributeElement3.getAttribute("ShortDescription")' />
						<s:property value='#ShortDescription3' id="facet" />
						</b>
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
			<div class="roll close"><b>
			<s:set name='AttributeElement2' value='XMLUtils.getChildElement(#facets, "Attribute")' />
			<s:set name='ShortDescription2' value='#AttributeElement2.getAttribute("ShortDescription")' />
			<s:property value='#ShortDescription2' id="facet" />
			</b></div>
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
<script>
										$(".toggle").click(function () {
										$(".favorita").toggle();
										});
										</script></div>
</div>

-->

	
	<s:set name='headercount' value='%{2}' />
		<s:iterator id='facets'
			value='XMLUtils.getElements(#catDoc, "//FacetList/ItemAttribute")'>
			<div id="narrow_spb<s:property value='#headercount'/>" class="browseBox subPanelBox">
			<s:set name='count1' value='%{#count1 + 1}' />
			<s:set name='facetList'
				value='XMLUtils.getElements(#facets, "AssignedValueList/AssignedValue")' />
			<s:set name='showFacet' value='"Y"' />
			
			
			<!-- Webtrends tag start -->
				<META Name="DCSext.w_x_narrowby" Content="1" />
			<!-- Webtrends tag end -->
			
				<s:set name='showFacet' value='"N"' />
				<s:iterator id='facetVal' value='#facetList'>
					<s:if test='%{#facetVal.getAttribute("IsProvidedFilter") != "Y"}'>
						<s:set name='showFacet' value='"Y"' /> 
					</s:if>
				</s:iterator>
			
			
				<s:set name='headercount' value='%{#headercount + 1}' />
				<div id="narrow_header<s:property value='#headercount'/>" class="header"  style="background-color:#003399"><span class="float-right"><a href="#" class="expand-narrow-by" title="Show/Hide"><img src="../xpedx/images/icons/12x12_white_expand.png" style="margin-top:5px" alt="expand"></a></span>
				<s:set name='AttributeElement1' value='XMLUtils.getChildElement(#facets, "Attribute")' />
				<s:set name='ShortDescription1' value='#AttributeElement1.getAttribute("ShortDescription")' />
				<s:property value='#ShortDescription1' id="facet" /></div>
				<div id="narrow_content<s:property value='#headercount'/>" class="content narrowbyattributes default-collapsed">
	
				
			<ul>
			
				<s:iterator id='facetVal' value='#facetList'>
					
						<s:set name='count1' value='%{#count1 + 1}' />
						<s:url id='narrowURL' namespace='/catalog' action='filter.action'>
							<s:param name='indexField'
								value='#facets.getAttribute("IndexFieldName")' />
							<s:param name='facet' value='#facetVal.getAttribute("Value")' />
							<s:param name='cname' value='#facetVal.getAttribute("Value")' />
							<s:param name='filterDesc' value='#ShortDescription1' />
						</s:url>
						<li class="roll close"><s:a href="%{narrowURL}"
							tabindex="%{#count1}">
							<s:property value='#facetVal.getAttribute("Value")' />
						</s:a> </li>
					
				</s:iterator>
			</ul>
			</div>	
			
			</div>
	</s:iterator>
<!--</ul>-->
<!---->
<!--</div>-->

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
	<s:set name='AttributeElement' value='XMLUtils.getChildElement(#facets, "Attribute")' />
	<s:set name='ShortDescription' value='#AttributeElement.getAttribute("ShortDescription")' />
	<s:set name='AttributeValue' value='%{"viewAll" + #ShortDescription}' />

	<s:if test='%{#showFacet == "Y"}'>
		<swc:dialogPanel title="View All" isModal="true"
			id='${AttributeValue}'>

			<table class="listTableBody padding-left" width="100%">

				<s:property value='#ShortDescription' />

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