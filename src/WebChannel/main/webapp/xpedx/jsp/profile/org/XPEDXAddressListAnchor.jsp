<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs’. 
    This is to avoid a defect in Struts that’s creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts’ OGNL statements. --%>
<s:set name='_action' value='[0]'/>
<s:set name="xutil" value="#_action.getXMLUtils()"/>
<s:set name='personInfo' value='#xutil.getChildElement(row, "PersonInfo")' id='personInfo'/>
<s:set name='isInherited' value='%{row.getAttribute("IsInherited")}' />
<s:set name="rowCount" value="%{rowNumber}"/>
<s:set name='addrId' value='#xutil.getAttribute(#personInfo, "AddressID")' id='addrId'/>
<s:if test='%{properties.get("defaultPanel")=="true"}'>
	<s:set name="tabIndexCounter" value='150' />
</s:if>
<s:else>
	<s:set name="tabIndexCounter" value='200' />
</s:else>
<s:set name="checkBoxTabIndexCounter" value='%{#tabIndexCounter + (#rowCount*2)}' />
<s:set name="addLinkTabIndexCounter" value='%{#checkBoxTabIndexCounter + 1}' />
<s:if test='%{#addrId == ""}'>
	<s:set name='city' value='#xutil.getAttribute(#personInfo, "City")' id='city'/>
	<s:set name='state' value='#xutil.getAttribute(#personInfo, "State")' id='state'/>
	<s:if test='%{#city != "" && #state != ""}'>
		<s:set name='addrId' value='%{#city+"-"+#state}' />
	</s:if>
	<s:else>
		<s:if test='%{#city != ""}'>
			<s:set name='addrId' value='%{#city}' />
		</s:if>
		<s:if test='%{#state != ""}'>
			<s:set name='addrId' value='%{#state}' />
		</s:if>
	</s:else>
	<s:if test='%{#addrId == ""}'>
	    <s:set name='addrId' value='%{row.getAttribute("CustomerAdditionalAddressID")}' />
	</s:if>
</s:if>

<s:property value='#addrId'/>
  