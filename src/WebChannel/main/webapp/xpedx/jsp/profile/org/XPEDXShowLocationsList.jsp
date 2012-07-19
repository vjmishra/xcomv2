<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="s" uri="/struts-tags"%>
    <%@ taglib prefix="xpedx" uri="xpedx" %>
<s:set name='_action' value='[0]'/>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<s:set name="displayChildCustomersMap" value="displayChildCustomersMap"></s:set>
<s:set name="shownCustomerId" value="shownCustomerId"/>
<s:set name="suffixTypeVar" value="shownCustomerSuffixType" />
		<s:iterator id="msapAndSapMap" value="msapAndSapCustomersMap">
			<s:set name='sapCustomers' value='value' />
			<s:iterator id="sapCustomer" value="#sapCustomers" status="sapCustomerStatus">
				<s:set name="sapCustomerId" value="#sapCustomer" />
				<s:set name="sapCustomerDisplay" value="#displayChildCustomersMap.get(#sapCustomer)" />
				<s:set name="suffixType" value="suffixTypeMap.get(#sapCustomerId)" />
					<s:if test='%{#suffixType =="B" }'>
						<ul class="indent-tree">
					</s:if>
					<s:else>
						<ul class="indent-tree-act">
					</s:else>
						<li>	
							<s:if test='%{#suffixType =="B" }'>
								<input type="button" class="icon-plus" style="vertical-align:middle;" onclick="getChildCustomerList(this,'<s:property value="#sapCustomerId"/>','<s:property value="#suffixType"/>', 'billTo_<s:property value="%{#sapCustomerId}"/>') " />
								<input type="radio" name="customerId" value="<s:property value='#sapCustomerId' />" />
							</s:if>
							<s:else>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="customerId" value="<s:property value='#sapCustomerId' />" />
							</s:else>
							<s:property value="#displayChildCustomersMap.get(#sapCustomerId)"/>
							<s:if test='%{#suffixType =="B" }'>
								<div id="billTo_<s:property value='%{#sapCustomerId}'/>">
								</div>
							</s:if>
						</li>
					</ul>
					
			</s:iterator>
			<s:if test='%{#suffixType =="S" }'>
						<s:url id="paginatedShipTo" namespace="/profile/org" action="xpedxShowLocations">
								<s:param name="pageNumber" value="'{0}'"/>
								<s:param name="pageSetToken" value="%{pageSetToken}"/>
								<s:param name="shownCustomerSuffixType" value="%{'B'}"/>
								<s:param name="shownCustomerId" value="%{shownCustomerId}"/>
								
							</s:url>
							<s:set name="ajaxDivID" value="%{'billTo_'+shownCustomerId}"/>
			 				<xpedx:pagectl currentPage="%{pageNumber}"  divId="%{#ajaxDivID}" lastPage="%{totalNumberOfPages}" urlSpec="%{#paginatedShipTo}" isAjax="true" />
 				</s:if>
		</s:iterator>
</body>
</html>