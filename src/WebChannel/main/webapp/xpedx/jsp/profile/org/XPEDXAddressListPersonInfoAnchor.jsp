<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs’. 
    This is to avoid a defect in Struts that’s creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts’ OGNL statements. --%>
<s:set name='_action' value='[0]'/>
<s:set name="xutil" value="#_action.getXMLUtils()"/>
<s:set name='personInfo' value='#xutil.getChildElement(row, "PersonInfo")' id='personInfo'/>
<s:set name='ctxt' value="#_action.getWCContext()" />
<s:set name="columnname" value='%{properties.get("columnName")}'/> 
<s:set name="columnvalue" value="#xutil.getAttribute(#personInfo, #columnname)" />
<s:if test='%{#columnname == "State" || #columnname == "Country"}'>
	<s:set name='columnvalue' value='#util.getDBString(#ctxt,#columnvalue)' />
</s:if>
<s:if test='%{#columnname == "ExtnZip4"}'>
	<s:set name='piExtn' value='#xutil.getChildElement(#personInfo,"Extn")' />
	<s:set name="columnvalue" value="#xutil.getAttribute(#piExtn, #columnname)" />
</s:if>
<span><s:property value='#columnvalue' /></span>