<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Comparator" %>
<%@page import="java.util.HashMap"%>
<%@ taglib prefix="swc" uri="swc" %>
<%@ taglib prefix="xpedx" uri="xpedx" %>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
 <title>Sales Representative Search</title> 
<%-- <title> <s:property value="wCContext.storefrontId" /> - <s:text name="MSG.SWC.SPRO.SEARCH.GENERIC.TABITLE"/></title> --%>

<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/salesrep/style<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<%--  <link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/salesrep/SpryTooltip<s:property value='#wcUtil.xpedxBuildKey' />.css" />--%>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.css" />

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/salesrep/lib<s:property value='#wcUtil.xpedxBuildKey' />.js">
</script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/salesrep/SpryTooltip<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

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

//Start fix for 3108
//commented for 3108
/* function logoutMessage(){
	alert("Sales Pro site must be relaunched for your next sign-in to be successful.");
	return ;
} */
//End fix for 3108
</script>
</head>
<body>
<div id="container">

  <div class="searchbg">
  <s:url id='logoutURL' namespace='/home' action='saleslogout' includeParams='none'/>							  
  <div id="logout-text">
  <s:a href="%{#logoutURL}" >Sign Out</s:a>
  </div>
       <div class="textbox"> 
      <s:form name="login2" id="login2" action="searchCustomer.action" namespace="/common" method="post">
      <input name="searchText" value="Search for Customer" size="15" 
      onfocus="javascript:clearTextField(this);" onblur="inputOnBlur(login, 'Search for Customer')" type="text"/>
      <s:hidden name="DisplayUserID" id="DisplayUserID" value="%{networkId}"/>
      <s:hidden name="command" id="command" value="%{'search'}"/>
          
        <input name="button" type="image" id="button" style="float:right;"  value="Submit" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/salesrep/button.png" onclick="javascript:document.login2.submit"/> </s:form>
    </div>
    </div>
    <%-- added for jira 3442 pagination--%>
  <s:set name='_action' value='[0]'/>
  <s:set name='wcContext' value="#_action.getWCContext()"/>

  <s:url id="salesRepCustomersPaginated" action="salesRepLogin" namespace="/common">
  <s:param name="pageNumber" value="'{0}'"/>
  <s:param name="DisplayUserID" value="%{#_action.getNetworkId()}"/>
  </s:url>
 <div id="search"> 
<div id="listOfCustomers">
<table width="100%"><tr><td style="width:70%;">
<div class="salesProNotice" style="width:auto;"> Please ensure correct Ship-To has been selected once you are logged in. You can view / change Ship-To by clicking on customer name on top right corner.</div></td>
 <td style="width:40%;"><div class="search-pagination-bottom">
  <span>
      <s:if test="%{totalNumberOfPages == 0 || totalNumberOfPages == 1}">Page&nbsp;&nbsp;<s:property value = "%{pageNumber}" /></s:if>
      <s:if test="%{totalNumberOfPages>1}">Page</s:if>&nbsp;&nbsp;<xpedx:pagectl currentPage="%{pageNumber}" lastPage="%{totalNumberOfPages}" urlSpec="%{#salesRepCustomersPaginated}" isAjax="False" divId="listOfCustomers" showFirstAndLast="False" showMyUserFormat="true" />
  </span>
</div></td></tr></table>
</div>
<div> </div>
 <%-- end of jira 3442 pagination--%>
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
          		final LinkedHashMap<String, String> searchedMap = (LinkedHashMap<String, String>)request.getAttribute("SEARCHED_CUSTOMERS");
				/*final TreeMap<String,String> treeSortedByValues = new TreeMap<String,String>(new Comparator<Object>()
				{
					public int compare(Object o1, Object o2)
					{
						return searchedMap.get(o1).compareTo(searchedMap.get(o2));
					}

				});
				treeSortedByValues.putAll(searchedMap);
				
				if(treeSortedByValues.size()!=0 && null!=treeSortedByValues){
					
					for(String customerId : treeSortedByValues.keySet()){
						String custID [] = customerId.split("_");
						String customerID="";
						if(custID != null){
						   customerID = custID[0];
						}
						String customerName = treeSortedByValues.get(customerId); */
					if(searchedMap.size()!=0 && null!=searchedMap){	
						for(String customerId : searchedMap.keySet()){	
							String customerName = searchedMap.get(customerId);
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
			final LinkedHashMap<String, String> assignedCustomerMap = (LinkedHashMap<String, String>)session.getAttribute("ASSIGNED_CUSTOMERS");
			if(null!=assignedCustomerMap){
				for(String customerId : assignedCustomerMap.keySet()){					
					String customerName = assignedCustomerMap.get(customerId);
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
 <s:hidden name="SRSalesRepEmailID" value='%{#_action.getWCContext().getSCUIContext().getSession().getAttribute("SRSalesRepEmailID")}' id="SRSalesRepEmailID"/>
 </s:form>                
 </table>
<div id="table-bottom-bar"  style="width:97%; margin:auto;">
<div id="table-bottom-bar-L"></div>
<div id="table-bottom-bar-R"></div>
<div>
	<!-- <input type="button" id="logoutButton" value="Logout" onclick="history.go(-1);">  -->
	<br/>
	<div id="errorMsgForCustomerData" align="center"><font color="red">No customer records found with the above criteria</font></div>
</div>
</div></div>
<%-- added for jira 3442 pagination--%>
  <s:set name='_action' value='[0]'/>
  <s:set name='wcContext' value="#_action.getWCContext()"/>

  <s:url id="salesRepCustomersPaginated" action="salesRepLogin" namespace="/common">
  <s:param name="pageNumber" value="'{0}'"/>
  <s:param name="DisplayUserID" value="%{#_action.getNetworkId()}"/>
  </s:url>
 <div id="search"> 
<div id="listOfCustomers" class="search-pagination-bottom">
  <span>
      <s:if test="%{totalNumberOfPages == 0 || totalNumberOfPages == 1}">Page&nbsp;&nbsp;<s:property value = "%{pageNumber}" /></s:if>
      <s:if test="%{totalNumberOfPages>1}">Page</s:if>&nbsp;&nbsp;<xpedx:pagectl currentPage="%{pageNumber}" lastPage="%{totalNumberOfPages}" urlSpec="%{#salesRepCustomersPaginated}" isAjax="False" divId="listOfCustomers" showFirstAndLast="False" showMyUserFormat="true" />
  </span>
</div>
<div> </div>
</div>
 <%-- end of jira 3442 pagination--%>
 <div id="strdiv2"></div>
  </div>   
  <div> </div>
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
