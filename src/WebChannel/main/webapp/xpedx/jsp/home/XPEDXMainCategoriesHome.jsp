 <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 

<!-- link media="all" type="text/css" rel="stylesheet" href="<s:url value='/swc/xpedx/css/home/home.css'/>" /-->
<!-- link media="all" type="text/css" rel="stylesheet" href="<s:url value='/swc/xpedx/css/common/notes-list.css'/>" /-->

<s:bean name='com.sterlingcommerce.framework.utils.SCXmlUtils' id='util' />

<s:set name='_action' value='[0]'/>
<s:set name="xutil" value="XMLUtils"/>
<!--s:set name='sDoc' value='#attr["RootCatalogDoc"].documentElement' / -->
<s:set name='catDoc' value="#_action.getMainCatsDoc()" />
<s:set name='sfid' value='wCContext.storefrontId'/>
<s:set name='imageURL_facility' value="'https://xcontent.ipaper.com/storefront/' + #sfid + '_SignInPage_FacilitySupplies.png'" />
<s:set name='imageURL_graphics' value="'https://xcontent.ipaper.com/storefront/' + #sfid + '_SignInPage_Graphics.png'" />
<s:set name='imageURL_paper' value="'https://xcontent.ipaper.com/storefront/' + #sfid + '_SignInPage_Paper.png'" />
<s:set name='imageURL_packaging' value="'https://xcontent.ipaper.com/storefront/' + #sfid + '_SignInPage_Packaging.png'" />

		    <tr>
		    	
<%-- 		Commented out iterator in favor of on big container to 
			hold an image map.
			<s:iterator value='XMLUtils.getElements(#catDoc, "//CategoryList/Category")' id='cat'>
					<td>
       				  	<s:set name='contentlocation' value='#util.getXpathAttribute(#cat, "AssetList/Asset/@ContentLocation")' />
       				  	<s:set name='contentid' value='#util.getXpathAttribute(#cat, "AssetList/Asset/@ContentID")' />
       					<s:url id='catURL' namespace='/catalog' action='navigate.action'>
						<s:param name='path' value='#cat.getAttribute("CategoryPath")'/>
						<s:param name='cname' value='#cat.getAttribute("ShortDescription")'/>
						<s:param name='newOP' value='%{true}'/>
						</s:url>
						<s:set name='categoryName' value='#cat.getAttribute("ShortDescription")' />
						<s:if test='(#categoryName == "Packaging")' >
				    		<s:a href="%{catURL}" >
				    			<img src="<s:url value='%{#imageURL_packaging}'/>" />
				    		</s:a>
				        </s:if>
				        <s:elseif test='(#categoryName == "Facility Supplies")'>
				    		<s:a href="%{catURL}" >
				    			<img src="<s:url value='%{#imageURL_facility}'/>" />
				    		</s:a>
				        </s:elseif>
				        <s:elseif test='#categoryName == "Graphics"'>
				    		<s:a href="%{catURL}" >
				    			<img src="<s:url value='%{#imageURL_graphics}'/>" />
				    		</s:a>
				        </s:elseif>
				        <s:elseif test='#categoryName == "Paper"'>
				    		<s:a href="%{catURL}" >
				    			<img src="<s:url value='%{#imageURL_paper}'/>" />
				    		</s:a>
				        </s:elseif>
				 	</td>
		       </s:iterator> --%>
		       <td class="storefront-td" colspan="4">
		       <!--  Begin storefront_spb_promo.html content SignInPageBottom here -->
		       
		       	<%-- <div class="slideshow"> --%>
						<s:action name="xpedxDynamicPromotionsAction" executeResult="true" namespace="/common" >
							<s:param name="callerPage">SignInPageBottom</s:param>
						 </s:action>
				<%-- </div> --%>
		

		       <!-- End storefront_spb_promo.html Content here -->
		       </td>
		    </tr>
