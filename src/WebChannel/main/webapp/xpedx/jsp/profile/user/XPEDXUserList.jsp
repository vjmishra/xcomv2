<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="xpedx" uri="xpedx" %>
<%@ taglib prefix="swc" uri="swc"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs�. 
    This is to avoid a defect in Struts that�s creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts� OGNL statements. --%>
	<s:set name='_action' value='[0]'/>
	<s:set name='wcContext' value="wCContext" />
	<s:set name='storeFrontID' value='%{#wcContext.getStorefrontId()}' />
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
<s:bean name='com.sterlingcommerce.framework.utils.SCXmlUtils' id='SCXmlUtils' />
<s:set name="xutil" value="#_action.getXMLUtils()"/>
<s:set name='sdoc' value="#_action.getUserList().getOwnerDocument()" />
<s:set name='customerContactList'
	value='#util.getElements(#sdoc,"//Page/Output/CustomerContactList/CustomerContact")' />

	<s:set name="fchild" value="%{#customerContactList.getFirstChild()}"/>
<s:set name ="buyerorg" value="#util.getElement(#sdoc,'//Page/Output/CustomerContactList/CustomerContact/Customer/BuyerOrganization')"/>
<s:set name="buyerOrgCode" value="%{#_action.getBuyerOrgCode()}"/>
<s:set name="buyerOrgName" value="%{#_action.getBuyerOrgName()}"/>
<s:set name='manageUserProfileResId' value='"/swc/profile/ManageUserProfile"'/>
<s:url id="userListURL" action="xpedxGetUserList" namespace="/profile/user" />
<s:if test="@com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer@getInstance().isAuthorized(#manageUserProfileResId, getWCContext())" >
	<s:set name="manageProfileAllwd" value='"true"'/>
</s:if>
<s:else>
	<s:set name="manageProfileAllwd" value='"false"'/>
</s:else>
<input type="hidden" value="" id="orderDescending" name="orderDescending" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<style type="text/css">
	.padding-td{ padding:5px 5px 5px 10px; font-size:13px }
	.padding-td label{ font-size:13px }
	</style>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><s:text name="my.Account.page"/></title>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/sorttable.js"></script>
    <link media="all" type="text/css" rel="stylesheet"
    	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/user/my-account<s:property value='#wcUtil.xpedxBuildKey' />.css" />
    <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/user/userList<s:property value='#wcUtil.xpedxBuildKey' />.js'/>"></script>
    <SCRIPT type="text/javascript">
    function callAjaxForSorting(url,divId)
{
	
	if(document.getElementById(divId) !=null){
        document.getElementById(divId).innerHTML = "Loading.....Please Wait";
   	}    	
	
   	var orderDesc="Y";
		var orderByAttribute=""
		var param;			
		
	url = ReplaceAll(url,"&amp;",'&');
	
         				Ext.Ajax.request({
	         		        url:url,
	         		        params: param,
	         		        method: 'POST',
	         		        success: function (response, request){
	         		             document.getElementById('viewUsersDlg').innerHTML = response.responseText;	         		            
	         		         },
	         		         failure: function (response, request){
	         		         }
	         		    });
	         		}
         			
    function getNewContactInfo(url){
    	document.userList.action = url;
    	document.userList.submit();
    	} 

	function selectedUser()
	{
		var url=document.getElementById("seletedUrl").value;
		if(url == "")
		{
			return false;
		}
		else{
		
		document.selectUserForm.action = url;
    	document.selectUserForm.submit();
		}
	}
	function ReplaceAll(Source,stringToFind,stringToReplace){
	  var temp = Source;
	    var index = temp.indexOf(stringToFind);
	        while(index != -1){
	            temp = temp.replace(stringToFind,stringToReplace);
	            index = temp.indexOf(stringToFind);
	        }
	        return temp;
	}
	function setSelectedUrl(contactId,customerId,storefrntId){
		var url='<s:property value="#xpedxManageOtherProfilesURL"/>';//"/swc/profile/user/MyManageOtherProfiles.action?sfId="+storefrntId+"&customerContactId="+contactId+"&customerId="+customerId;
		url = ReplaceAll(url,"&amp;",'&');
		url = url+"&customerContactId="+contactId+"&customerId="+customerId;
		//var url="/swc/profile/user/MyManageOtherProfiles.action?sfId="+storefrntId+"&customerContactId="+contactId+"&customerId="+customerId;
		document.getElementById("seletedUrl").value=url;
	}
	
	
         		

	 </SCRIPT>
	 <style> 
     table thead {cursor: pointer;} 
     . {} 
     .sc{ height:100px; overflow:auto; overflow-x: hidden; overflow-y: scroll; margin-right:15px;} 
     . input[type=radio]{ margin-right:15px; margin-left:5px;} 
     .no-padding { padding:0px; } 
     div#border-div { border-left: 1px solid #CCCCCC; border-right: 1px solid #CCCCCC; } 
     a.underlink:hover { text-decoration: underline !important; } 
     div#fancybox-content { height: 540px !important; }
     </style> 
     <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>      	 
</head>
<body>
    <s:set name='hasAccessToEdit' value="#_action.getIsChildCustomer()" />
    <!-- Display Action Messages/Errors -->

    <s:set name='desc' value="#_action.getOrderDesc()"/>
    <s:set name='orderby' value="#_action.getOrderByAttribute()"/>
	<s:url id='xpedxManageOtherProfilesURL' namespace='/profile/user'
		action='MyManageOtherProfiles' />
    <s:url id="userListPaginationURL" action="xpedxGetUserList" namespace="/profile/user">
    	<s:param name="orderByAttribute" value="%{#orderby}"/>
    	<s:param name="customerID" value="%{#_action.getCustomerID()}"/>
    	<s:param name="orderDesc" value="orderDesc"/>
    	<s:param name="pageNumber" value="'{0}'"/>
    	<s:param name="searchCriteria" value="%{searchCriteria}"/>
    	<s:param name="searchValue" value="%{searchValue}"/>
    	<s:param name="pageSetToken" value="#_action.getPageSetToken()"/>
    </s:url>
    <s:url id="userListSortURL" action="xpedxGetUserList" >
    	<s:param name="orderByAttribute" value="'{0}'"/>
    	<s:param name="customerID" value="%{#_action.getCustomerID()}"/>
    	<s:param name="orderDesc" value="'{1}'"/>
    	<s:param name="pageNumber" value="%{pageNumber}"/>
    	<s:param name="searchCriteria" value="%{searchCriteria}"/>
    	<s:param name="searchValue" value="%{searchValue}"/>
    </s:url>
 	<div id="inline1" class="xpedx-light-box" style="overflow: auto;">
		<s:set name="iter" value="#util.getElements(#sdoc, '//Page/Output/CustomerContactList/CustomerContact')"/>
        <div>
            <div id="inline1" class="xpedx-light-box">
				<h1><span style="margin:0px;">My Users</span></h1> 
				<s:set name='storefrontId' value="wCContext.storefrontId" />
				<s:if test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_STORE_FRONT.equals(#storefrontId)}'>
					<s:set name="adminIcon" value="%{#wcUtil.staticFileLocation + '/xpedx/images/theme/theme-1/20x20_admin.png'}" />
				</s:if>
				<s:elseif test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT.equals(#storefrontId)}'>
					<s:set name="adminIcon" value="%{#wcUtil.staticFileLocation + '/' + wCContext.storefrontId + '/images/20x20_green_admin.png'}" />
				</s:elseif>
				<p class="less-margin floatleft">
					<img class="inline-image" src="<s:property value='#adminIcon' />" />
					Denotes an Admin User
   				</p>
				<xpedx:flexpagectl currentPage="%{pageNumber}" lastPage="%{totalNumberOfPages}" urlSpec="%{#userListPaginationURL}" isAjax="true" divId="viewUsersDlg" />

				<div class="clearfix"></div>  

					<div style="width:757px;"> 
 
						<xpedx:sortctl sortField="%{orderByAttribute}" sortDirection="%{orderDesc}" down="Y" up="N"
								urlSpec="%{#userListSortURL}" isAjax="true" divId="viewUsersDlg">
						  
						<table cellspacing="0" cellpadding="0" class="standard-table sortable addmargintop10" id="user-header" style="background: none repeat scroll 0% 0% transparent; margin-left: 1px;">  
                         
                              <tr id="none">
                             <!-- <th width="1%" class="no-border table-header-bar-left sorttable_nosort"></th>  --> 
                                <th width="25%"  class="sortable"><xpedx:sortable fieldname="%{'CustomerContactID'}">Username</xpedx:sortable></th>
                                <th width="21%" align="center" class="sortable"><xpedx:sortable fieldname="%{'FirstName'}">First Name</xpedx:sortable></th>
                                <th width="21%" align="center" class="sortable"><xpedx:sortable fieldname="%{'LastName'}">Last Name</xpedx:sortable></th>
                                <th width="33%" class="sortable"><xpedx:sortable fieldname="%{'EmailID'}"><span class="padding8">Email Address</span></xpedx:sortable></th>

                               
                                
                                
                              </tr>
                              	
	    	       <input type="hidden" name="seletedUrl" id="seletedUrl" value="" />
	    	<s:form name="selectUserForm" id="selectUserForm">
	    	</s:form>
	    	
	    
    			 <!-- <div id="border-div"> 
    			 <div style="height:185px; overflow:auto"> -->	    	
	    	    <tbody>
				<s:iterator id="userList" value="#iter" status="rowStatus">
				
					<s:set name='customerElem' value='#xutil.getChildElement(#userList, "Customer")'/>
					<s:set name="rowCount" value="%{rowStatus}"/>
					<s:set name="tabIndexCounter" value='150' />
					<s:url id="getUserInfo" action="MyManageOtherProfiles">  
					  <s:param name="customerContactId" value="%{#userList.getAttribute('CustomerContactID')}"/>
					  <s:param name="customerId" value="%{#customerElem.getAttribute('CustomerID')}"/>
					</s:url>
					<s:set name="CustomerContactID" value="%{#userList.getAttribute('CustomerContactID')}"/>
					<s:set name='extnElement' value='#xutil.getChildElement(#userList, "Extn")'/>
					<s:set name="isSalesRep" value="%{#extnElement.getAttribute('ExtnIsSalesRep')}"/>
					
					<s:if test='%{#isSalesRep != "Y"}'>

						<tr>
						<td class="left-cell">
							<div style="width:230px; word-wrap:break-word;">
								<input type="radio" onchange="javascript:setSelectedUrl('<s:property value="%{CustomerContactID}" />','<s:property value="%{CustomerID}" />','<s:property value="%{storeFrontID}" />');" name="selectRadio" id="selectRadio"/>
															<s:if test="#_action.isAdminMap.get(#userList.getAttribute('CustomerContactID'))">
									<s:set name='storefrontId' value="wCContext.storefrontId" />
				<s:if test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_STORE_FRONT.equals(#storefrontId)}'>						
									<img class="inline-image" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/20x20_admin.png" />
									</s:if>
				<s:elseif test='%{@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAALFELD_STORE_FRONT.equals(#storefrontId)}'>
				<img class="inline-image" src="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/images/20x20_green_admin.png" />
				</s:elseif>
								</s:if>
							&nbsp;<s:property value='%{#userList.getAttribute("CustomerContactID")}'/>

						  </div> </td>
							<td ><div>	
							    <%--Removing test per UI Blitz requirement --%>							
								<%--<s:if test='%{#manageProfileAllwd == "true"}'> 
									<s:a href="%{getUserInfo}">
									  <s:property value='%{#userList.getAttribute("FirstName")}'/>
									</s:a>
								</s:if>
								<s:else> --%>
									<s:property value='%{#userList.getAttribute("FirstName")}'/>
								<%--</s:else> --%>
							</div></td>
							<td><div>
								<%--<s:if test='%{#manageProfileAllwd == "true"}'>
									<s:a href="%{getUserInfo}">
									  <s:property value='%{#userList.getAttribute("LastName")}'/>
									</s:a>
								</s:if>
								<s:else> --%>
									<s:property value='%{#userList.getAttribute("LastName")}'/>
								<%--</s:else>--%>	
							</div></td>
								
							<td class="right-cell"><div style="width:175px; word-wrap:break-word;">
								<s:property value='%{#userList.getAttribute("EmailID")}'/>
							</div></td>
						</tr>
						
					</s:if>
					<s:if test="#rowStatus.last">				
						<s:set name='custID' value="%{#customerElem.getAttribute('CustomerID')}"/>
					</s:if>
					
				</s:iterator>
				</tbody>
                            </div>
                          </table> 
        </xpedx:sortctl>         
                           <!-- </div> 
    						 </div> -->
                            
                             
             
            </div>
</div>	
 	<!--
 	<div id="table-bottom-bar">
  	<div id="table-bottom-bar-L"></div>
    	<div id="table-bottom-bar-R"></div>
    </div>-->
    
        <br/>
		<ul id="tool-bar" class="tool-bar-bottom"  style="float:right; margin-right:13px;"  >
	        <li><a class="btn-neutral" href="javascript:$.fancybox.close()"><span>Cancel</span></a></li>
            <li ><a class="btn-gradient" href="#" onclick="javascript: selectedUser('<s:property value="%{CustomerContactID}" />','<s:property value="%{CustomerID}" />','<s:property value="%{storeFrontID}" />'); return false;"><span>Select</span></a></li>
	    </ul>
	    <s:form name="userList" id="userList">
		    <s:hidden name="customerID" value="%{#_action.getCustomerID()}"/>
		    <s:hidden name="buyerOrgCode" value="%{#buyerOrgCode}"/>
		    <s:url id="showAllUsersURL" action='getUserList' namespace='/profile/user'>
			    <s:param name="customerID" value="%{#_action.getCustomerID()}"/>
			    <s:param name="buyerOrgCode" value="%{#buyerOrgCode}"/>
		    </s:url>
		    <s:set name='custDoc' value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getCustomerDetails(#custID,#storeFrontID,'xpedx-customer-Minimum-Info')"/>
		    <s:set name='userCustElement' value='#custDoc.getDocumentElement()'/>
		    <s:set name='extnSuffixType' value='%{#SCXmlUtils.getXpathAttribute(#userCustElement, "//Extn/@ExtnSuffixType")}' />
		    <s:if test='%{#extnSuffixType =="MC"}'>
		    <div id="searchTable" >
		    	<ul id="tool-bar" class="tool-bar-bottom" style="float:left">
		            <li>
		          	<a href="javascript:getNewContactInfo('<s:url value="/profile/user/MyNewUserCreate.action"/>');" class="grey-ui-btn">
		          		<span><s:text name='Add_User'/></span>
		          	</a>
		        </ul>
		    </div>
		    </s:if>
	    </s:form>    
	

    </div>
    <!-- // main end -->
</body>
<script>
defaultSortColumn=1;
</script>
</html>
