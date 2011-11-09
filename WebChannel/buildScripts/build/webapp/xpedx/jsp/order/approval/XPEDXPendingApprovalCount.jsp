<!-- This JSP file is use for returning count of Pending Orders -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<s:set name='_action' value='[0]'/>



	<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util'/>
	<s:set name='outDoc' value="outputDocument" id='outDoc'/>
	<s:set name="orderEle" value="#util.getElements(#outDoc, '//OrderList/Order')"/>
	<s:set name="ApCount" value="#orderEle.size()"/>
	<s:set name="labelAlert" value="'Pending Approval Alert'"/>




					
					
					<div>
						<s:if test='#ApCount > 0 '>
						<font color="red"><s:property value="#labelAlert"/> :<s:property value="#ApCount"/></font>
						</s:if>
					</div>



