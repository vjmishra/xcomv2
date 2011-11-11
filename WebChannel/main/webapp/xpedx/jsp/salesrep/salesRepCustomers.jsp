<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="java.util.Map" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Comparator" %>
<%@page import="java.util.HashMap"%>
<%@ taglib prefix="swc" uri="swc" %>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- <title>Sales Representative Search</title> -->
<title> <s:property value="wCContext.storefrontId" /> - <s:text name="MSG.SWC.SPRO.SEARCH.GENERIC.TABITLE"/></title>

<link media="all" type="text/css" rel="stylesheet" href="<s:url value='/xpedx/css/salesrep/style.css'/>" />
<link media="all" type="text/css" rel="stylesheet" href="<s:url value='/xpedx/css/salesrep/SpryTooltip.css'/>" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/ext-all.css" />

<script type="text/javascript" src="<s:url value='/xpedx/js/lib.js'/>">
</script>
<script type="text/javascript" src="<s:url value='/xpedx/js/SpryTooltip.js'/>"></script>
<script type="text/javascript" src="/swc/xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/global/ext-all.js"></script>

<style>
div a.underlink { text-decoration: none; }
div a.underlink:hover { text-decoration: underline;}
#logout-text { float: right; margin-right: 25px; margin-top: 40px;}
</style>

<script type="text/javascript">

function clearTextField(field){
    if (field.defaultValue == field.value) 
    	field.value = '';
    else if (field.value == '') 
    	field.value = field.defaultValue;
}
var data="false";
</script>
</head>
<body>
<div id="container">
 <div id="search"> 
  <div class="searchbg">
  <s:url id='logoutURL' namespace='/home' action='salesReploginFullPage'/>					
					  
  <div id="logout-text">
  <s:a href="%{#logoutURL}" >Sign Out</s:a>
  </div>
       <div class="textbox"> 
      <s:form name="login2" id="login2" action="searchCustomer.action" namespace="/common" method="post">
      <input name="searchText" value="Search for Customer" size="15" 
      onfocus="javascript:clearTextField(this);" onblur="inputOnBlur(login, 'Search for Customer')" type="text"/>
      <s:hidden name="DisplayUserID" id="DisplayUserID" value="%{networkId}"/>
      <s:hidden name="command" id="command" value="%{'search'}"/>
          
        <input name="button" type="image" id="button" style="float:right;"  value="Submit" src="<s:url value='/xpedx/images/salesrep/button.png'/>" onclick="javascript:document.login2.submit"/> </s:form>
    </div>
    <div class="table">     
    
    <table width="100%" id="mil-list-new" style="width:97%; margin:auto;">
 		<s:form action="salesRepCustomerLogin" namespace="/common" method="post">
            <tr class="table-header-bar">
	            <td class="no-border table-header-bar-left" width="264"><span class="white txt-small">Customer Name</span></td>
	            <td width="253" align="left" class="no-border"><span class="white txt-small">Customer Number</span></td>
	
	            <td width="171" align="left" class="no-border-right table-header-bar-right"> </td>
          	</tr>
          	<% 
          	String commandResult=(String)request.getAttribute("RESULT_COMMAND");
          	
          	if("searched".equals(commandResult)){
          		final Map<String, String> searchedMap = (HashMap<String, String>)request.getAttribute("SEARCHED_CUSTOMERS");
				final TreeMap<String,String> treeSortedByValues = new TreeMap<String,String>(new Comparator<Object>()
				{
					public int compare(Object o1, Object o2)
					{
						return searchedMap.get(o1).compareTo(searchedMap.get(o2));
					}

				});
				treeSortedByValues.putAll(searchedMap);
				
				if(treeSortedByValues.size()!=0 && null!=treeSortedByValues){
					
					for(String customerId : treeSortedByValues.keySet()){
						String customerName = treeSortedByValues.get(customerId);
				%>
                    <tr>
                      <td><%=customerName%></td>
                      <td valign="top"><%=customerId%></td>
                      <td valign="top">
                      <s:url id='selectedCustURL' namespace='/common' action='salesRepCustomerLogin'>
						<s:param name='selectedCustomer'>
							<%=customerId%>
						</s:param>
					  </s:url> 
                      <s:a href="%{#selectedCustURL}" value="">Select</s:a></td>
                    </tr>
                <%}
				}
				else
				{
					%>
					<script type="text/javascript">
					data="true";
					</script>
					<%
				}			
          	}
          	else{
			final Map<String, String> assignedCustomerMap = (Map<String, String>)session.getAttribute("ASSIGNED_CUSTOMERS");
			final TreeMap<String,String> treeSorted = new TreeMap<String,String>(new Comparator<Object>()
					{
						public int compare(Object o1, Object o2)
						{
							return assignedCustomerMap.get(o1).compareTo(assignedCustomerMap.get(o2));
						}

					});
					treeSorted.putAll(assignedCustomerMap);
					
			if(null!=treeSorted){
				for(String customerId : treeSorted.keySet()){
					String customerName = treeSorted.get(customerId);
		%>
		
                    <tr>
                       <td><%=customerName%></td>
                      <td valign="top"><%=customerId%></td>
                      <td valign="top">
                      <s:url id='selectedCustURL' namespace='/common' action='salesRepCustomerLogin'>
						<s:param name='selectedCustomer'>
							<%=customerId%>
						</s:param>
					  </s:url> 
                      <s:a href="%{#selectedCustURL}" value="">Select</s:a></td>
                    </tr>
                <%}}			
	} %>
 <s:hidden name="EnterpriseCode" value="xpedx"/>
 <s:hidden name="organizationCode" value="xpedx"/>
 <s:hidden name="sfId" value="xpedx"/>
 </s:form>                
 </table>
<div id="table-bottom-bar"  style="width:97%; margin:auto;">
<div id="table-bottom-bar-L"></div>
<div id="table-bottom-bar-R"></div>
<div>
	<!-- <input type="button" id="logoutButton" value="Logout" onclick="history.go(-1);">  -->
	<div id="errorMsgForCustomerData" align="center"><font color="red">No customer records found with the above criteria</font></div>
</div>
</div></div> <div id="strdiv2"></div>
  </div>   
  <div> </div>
</div>
</div>
 <script type="text/javascript">
 if(data=="true")
	 {
	 document.getElementById("errorMsgForCustomerData").style.display ='block';
	 }
 else
	 {
      document.getElementById("errorMsgForCustomerData").style.display ="none";
	 }
</script>
</body>
</html>
