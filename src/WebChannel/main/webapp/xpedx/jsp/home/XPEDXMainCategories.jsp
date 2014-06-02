 <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 



<s:bean name='com.sterlingcommerce.framework.utils.SCXmlUtils' id='util' />

<s:set name='_action' value='[0]'/>
<s:set name="xutil" value="XMLUtils"/>
<!--s:set name='sDoc' value='#attr["RootCatalogDoc"].documentElement' / -->
<s:set name='catDoc' value="#_action.getMainCatsDoc()" />

		<div class="anon-hp-prod-cat">
		   <!--  <h2>Browse Our B2B Catalogs</h2> -->
		    <h2><s:text name="MSG.SWC.B2B.COMMERCESITE.GENERIC.PGTITLE"/></h2>
		    <ul>
				
				<s:iterator value='XMLUtils.getElements(#catDoc, "//CategoryList/Category")' id='cat'>
       				  	<s:set name='contentlocation' value='#util.getXpathAttribute(#cat, "AssetList/Asset/@ContentLocation")' />
       				  	<s:set name='contentid' value='#util.getXpathAttribute(#cat, "AssetList/Asset/@ContentID")' />
       				  	
       				  	<s:set name='imageURL' value="#contentlocation + '/' + #contentid " />
       				  	
						<s:url id='catURL' namespace='/catalog' action='navigate.action'>
						<s:param name='path' value='#cat.getAttribute("CategoryPath")'/>
						<s:param name='cname' value='#cat.getAttribute("ShortDescription")'/>
						<s:param name='newOP' value='%{true}'/>
						</s:url>
						<s:set name='categoryName' value='#cat.getAttribute("ShortDescription")' />
						<s:if test='(#categoryName == "Packaging") || (#categoryName == "Facility Supplies")' >
					    	<li>
					        	<img class="anon-img-padding-hp" src="<s:url value='%{#imageURL}' includeParams='none' />" width="210" height="181" title="Product Image" alt="Product Image" />
					            <div style="margin-left: 20%; margin-top: 20px;"><s:a cssClass="blue-ui-btn" href="%{catURL}"><span><s:property value='#cat.getAttribute("ShortDescription")'/></span></s:a></div>
					        </li>
				        </s:if>
				        <s:if test='#categoryName == "Graphics"' >
					    	<li>
					        	<img class="anon-img-padding-hp" src="<s:url value='%{#imageURL}' includeParams='none' />" width="210" height="181" title="Product Image" alt="Product Image" />
					            <div style="margin-left: 20%; margin-top: 20px;"><s:a cssClass="blue-ui-btn" href="%{catURL}"><span>Print</span></s:a></div>
					        </li>
				        </s:if>
		       </s:iterator>
		         
		    </ul>
		</div>