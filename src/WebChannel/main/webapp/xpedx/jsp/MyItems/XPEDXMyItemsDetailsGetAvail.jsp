<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

	<s:set name="json" value='pnaHoverMap.get(#itemId)'/>
	<s:set name="jsonUOM" value="#json.get('UOM')"/>
	<s:set name="jsonImmediate" value="#json.get('Immediate')"/>
	<s:set name="jsonNextDay" value="#json.get('NextDay')"/>
	<s:set name="jsonTwoPlus" value="#json.get('TwoPlusDays')"/>
	<s:set name="jsonAvailability" value="#json.get('Availability')"/>
	<s:set name="jsonTotalQty" value="#json.get('Total')"/>
  
	<table>
	    <tr valign="top">
	    	<td><s:text name='Availability' /></td>
	        <td>
	        <s:property value='%{#jsonUOM}' />
	        </td>
	    </tr>
	    <tr valign="top">
	    	<td><s:text name='Immediate' /></td>
	        <td> <s:if test='%{#jsonImmediate != null}'>
				<s:property value='%{#jsonImmediate}' />
			</s:if>
			<s:else>
				<s:set name="jsonImmediate" value="%{'0'}"></s:set>
				<s:property value='%{#jsonImmediate}' />
			</s:else> 
	        
	        </td>
	    </tr>
	    <tr valign="top">
	    	<td><s:text name='Next Day' /></td>
	        <td>
	        <s:if test='%{#jsonNextDay != null}'>
				<s:property value='%{#jsonNextDay}' />
			</s:if>
			<s:else>
				<s:set name="jsonNextDay" value="%{'0'}"></s:set>
				<s:property value='%{#jsonNextDay}' />
			</s:else> 
	        
	        </td>
	    </tr>
	    <tr valign="top">
	    	<td><s:text name='2+ Days' /></td>
	        <td>
	        <s:if test='%{#jsonTwoPlus != null}'>
				<s:property value='%{#jsonTwoPlus}' />
			</s:if>
			<s:else>
				<s:set name="jsonTwoPlus" value="%{'0'}"></s:set>
				<s:property value='%{#jsonTwoPlus}' />
			</s:else> 
	        
	        </td>
	    </tr>
	     <tr valign="top">
	    	<td><s:text name='Total Quantity' /></td>
	        <td>
	         <s:if test='%{#jsonTotalQty != null}'>
				 <s:property value='%{#jsonTotalQty}' />
			</s:if>
			<s:else>
				<s:set name="jsonTotalQty" value="%{'0'}"></s:set>
				 <s:property value='%{#jsonTotalQty}' />
			</s:else> 
	        
	       
	        </td>
	    </tr>
	</table>
