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

<!-- end browse category --> <!-- begin narrow by  -->
<s:set name='FacetsList' value='XMLUtils.getElements(#catDoc, "//FacetList/ItemAttribute")' />

<s:if test="%{#FacetsList!=null && #FacetsList.size>0}"><%-- Show Narrow By section only if FacetsList size is greater than 0 --%>

	
	<s:set name='headercount' value='%{2}' />
		<s:iterator id='facets'
			value='XMLUtils.getElements(#catDoc, "//FacetList/ItemAttribute")'>
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
				
				
			<ul>
				<s:set name='facetMap' value='facetListMap.get(#ShortDescription1)'/>
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
						</s:url>
						
						<li class="roll close"><s:a href="%{narrowURL}"
							tabindex="%{#count1}">
							<s:property value='#factVal.getAttribute("Value")' />
						</s:a> </li>
				</s:iterator>
			</ul>
	</s:iterator>

</s:if><%-- if condition to check for FacetList size ENDS --%>
