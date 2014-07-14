<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/WEB-INF/yfc.tld" prefix="yfc" %>


<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean" id="xpedxUtilBean" />


<s:set name="dayPhone" value='#renderPersonInfo.getAttribute("DayPhone")' />
<s:set name="fmtDayPhone" value='#xpedxUtilBean.getFormattedPhone( #dayPhone )' />


<s:if test="#renderPersonInfo != null">
<s:if test='#util.getAttributeFromElementIfNonNull(#renderPersonInfo, "City") != ""'>
    <s:set name="displayCity" value='#renderPersonInfo.getAttribute("City") + ","'/>
</s:if>
<s:else>
    <s:set name="displayCity" value=""/>
</s:else>
	<s:if test='!(#contentOnly == "true")'>
		<s:if test='(#renderPersonInfo.getAttribute("AddressID") != null) && (#renderPersonInfo.getAttribute("AddressID") != "")'><s:property value="#renderPersonInfo.getAttribute('AddressID')"/><br/></s:if>
		<yfc:i18ndb><s:property value="#util.getCommonCodes('YCD_TITLE', @com.sterlingcommerce.webchannel.core.CommonCodeDescriptionType@SHORT, wCContext)[#renderPersonInfo.getAttribute('Title')]"/></yfc:i18ndb>
                        <s:property value="#renderPersonInfo.getAttribute('FirstName')"/>
                        <s:property value="#renderPersonInfo.getAttribute('MiddleName')"/>
                        <s:property value="#renderPersonInfo.getAttribute('LastName')"/><s:if test='(#renderPersonInfo.getAttribute("LastName") != null) && (#renderPersonInfo.getAttribute("LastName") != "")'><br/></s:if>
	</s:if>
	 
	 <s:if test='(#renderPersonInfo.getAttribute("Company") != null) && (#renderPersonInfo.getAttribute("Company") != "")'>
     	<s:property value="#renderPersonInfo.getAttribute('Company')"/><br/>
     </s:if>
      <s:if test='(#renderPersonInfo.getAttribute("AddressLine1") != null) && (#renderPersonInfo.getAttribute("AddressLine1") != "")'>
     	<s:property value="#renderPersonInfo.getAttribute('AddressLine1')"/><br/>
     </s:if>
     <s:if test='(#renderPersonInfo.getAttribute("AddressLine2") != null) && (#renderPersonInfo.getAttribute("AddressLine2") != "")'>
	     <s:property value="#renderPersonInfo.getAttribute('AddressLine2')"/><br/>
     </s:if>
    
     <s:if test='(#renderPersonInfo.getAttribute("AddressLine3") != null) && (#renderPersonInfo.getAttribute("AddressLine3") != "")'>
	     <s:property value="#renderPersonInfo.getAttribute('AddressLine3')"/><br/>
     </s:if>
     
     <s:property value="#displayCity"/>
     <s:property value="#renderPersonInfo.getAttribute('State')"/>
     <s:property value="#renderPersonInfo.getAttribute('ZipCode')"/>
     <yfc:i18ndb><s:property value="#util.getCommonCodes('COUNTRY', @com.sterlingcommerce.webchannel.core.CommonCodeDescriptionType@SHORT, wCContext)[#renderPersonInfo.getAttribute('Country')]"/></yfc:i18ndb><br/>
     
     <s:if test='(#renderPersonInfo.getAttribute("DayPhone") != null) && (#renderPersonInfo.getAttribute("DayPhone") != "")'>
     	<%-- <s:property value="%{#util.getFormattedPhone(#renderPersonInfo.getAttribute('DayPhone'), wCContext)}"/><br/> --%>
     	<s:property value='%{#fmtDayPhone}'/><br/><br/>
     </s:if>
      
    <%-- commented for jira 3780 <s:if test='(#renderPersonInfo.getAttribute("EMailID") != null) && (#renderPersonInfo.getAttribute("EMailID") != "")'>
     	<s:property value="#renderPersonInfo.getAttribute('EMailID')"/><br/>
     </s:if>   --%>

	<s:if test='!(#contentOnly == "true")'>
	     <s:set name='isCommercial' value='#renderPersonInfo.getAttribute("IsCommercialAddress")'/>
	     <s:if test='#isCommercial == "Y"'>
	         <s:text name="Commercial"/>
	     </s:if>
	     <s:elseif test='#isCommercial == "N"'>
	         <s:text name="Residential"/>
	     </s:elseif>
	</s:if>
</s:if>