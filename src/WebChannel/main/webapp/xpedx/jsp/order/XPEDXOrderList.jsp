<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% request.setAttribute("isMergedCSSJS","true"); %>
<swc:html>
<head>
<!-- styles -->
<%--Added meta tag for Navigation Tab Issue- Jira 3886/3872 --%>
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/ORDERS.css" />

<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE.css" />
<![endif]-->
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
<!-- sterling 9.0 base  do not edit  javascript move all functions to js/global-xpedx-functions.js -->
<%--
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.1.css" media="screen" />
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all.js"></script>
 --%>
 <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header.js"></script>
 <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-jquery-headder.js"></script>
 <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-header.js"></script>
 <%--
 <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.1.js"></script>
 --%>
 
<title> <s:property value="wCContext.storefrontId" /> - <s:text name="MSG.SWC.ORDR.ORDRLIST.GENERIC.TABTITLE"/></title>
 
<script type="text/javascript">
$(function() {
		$(".datepicker").datepicker({
			showOn: 'button',
						numberOfMonths: 1,

			buttonImage: '<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/calendar-icon.png',
			buttonImageOnly: true
		});
	});
	$(document).ready(function() {
		$(document).pngFix();
	});
	/*
	function searchTerm_onclick(){
	  	Ext.fly('searchFieldValue').dom.value='';
	  	return;
	  }
	 */
	 $(function() {
			$(".datepicker").datepicker({
				showOn: 'button',
							numberOfMonths: 1,
				buttonImage: '<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/calendar-icon.png',
				buttonImageOnly: true,
				buttonText: "Select Date"
			}); 
		});
				$(document).ready(function(){
			$(document).pngFix();
			$('#split-btn').click(function(){       
					$('.split-rows').toggle();
					return false;
			});
			$('#popup-window-close').click(function(){
					$('#split-order-overlay').toggle();
					return false;
			});
	});
	 
</script>
<script type="text/javascript">
function printPOs(customerPos) {
    var customerPosArray = customerPos.split(";");
    for(i = 0; i <customerPosArray.length; i++){
        if(i==0)
        {
        	document.write("<ul>");
        }
    	document.write("<li>"+customerPosArray[i]+"</li>");
    	if(i==(customerPosArray.length-1)) 
        {
    		document.write("</ul>");	
        } 
    }
}
</script>
<s:set name='_action' value='[0]' />
<s:url id="orderListSortURL" action="orderList" >
    <s:param name="orderByAttribute" value="'{0}'"/>
    <s:param name="orderDesc" value="'{1}'"/>
    <s:param name="pageNumber" value="%{pageNumber}"/>
    <s:param name="searchFieldName" value="getXpedxSearchFieldName()"/>
    <s:param name="searchFieldValue" value="searchFieldValue"/>
    <s:param name="statusSearchFieldName" value="getExactStatus()"/>
	<s:param name="submittedTSFrom" value="submittedTSFrom"/>
	<s:param name="submittedTSTo" value="submittedTSTo"/>
    <s:param name="shipToSearchFieldName" value="shipToSearchFieldName"/>
    <!-- 
    <s:param name="OrderNameValue" value="OrderNameSearchValue"/>
    <s:param name="ProductIdValue" value="ProductIdSearchValue"/>
    <s:param name="OwnerFirstNameValue" value="OwnerFirstNameSearchValue"/>
    <s:param name="OwnerLastNameValue" value="OwnerLastNameSearchValue"/>    
    <s:param name="createTSFrom" value="CreateTSFrom"/>
    <s:param name="modifyTSFrom" value="ModifyTSFrom"/>
    <s:param name="createTSTo" value="CreateTSTo"/>
    <s:param name="modifyTSTo" value="ModifyTSTo"/>
    <s:param name="OrderNumberValue" value="OrderNumberSearchValue"/>
    <s:param name="PurchaseOrderNumberValue" value="PurchaseOrderNumberSearchValue"/>
    <s:param name="holdSearchFieldName" value="HoldStatusSearch"/>  -->
</s:url>
<s:url id="orderListPaginationURL" action="orderList">
    <s:param name="orderByAttribute" value="orderByAttribute"/>
    <s:param name="orderDesc" value="orderDesc"/>
    <s:param name="pageNumber" value="'{0}'"/>
    <s:param name="searchFieldName" value="getXpedxSearchFieldName()"/>
    <s:param name="searchFieldValue" value="searchFieldValue"/>
    <s:param name="statusSearchFieldName" value="getExactStatus()"/>
	<s:param name="submittedTSFrom" value="submittedTSFrom"/>
	<s:param name="submittedTSTo" value="submittedTSTo"/>
    <s:param name="shipToSearchFieldName" value="shipToSearchFieldName"/>
    <s:param name="pageSetToken" value="#_action.getPageSetToken()"/>
    
    <!-- 
    <s:param name="OrderNameValue" value="OrderNameSearchValue"/>
    <s:param name="ProductIdValue" value="ProductIdSearchValue"/>
    <s:param name="OwnerFirstNameValue" value="OwnerFirstNameSearchValue"/>
    <s:param name="OwnerLastNameValue" value="OwnerLastNameSearchValue"/>    
    <s:param name="createTSFrom" value="CreateTSFrom"/>
    <s:param name="modifyTSFrom" value="ModifyTSFrom"/>
    <s:param name="createTSTo" value="CreateTSTo"/>
    <s:param name="modifyTSTo" value="ModifyTSTo"/>
    <s:param name="OrderNumberValue" value="OrderNumberSearchValue"/>
    <s:param name="PurchaseOrderNumberValue" value="PurchaseOrderNumberSearchValue"/>
    <s:param name="holdSearchFieldName" value="HoldStatusSearch"/>  -->
</s:url>
<s:url id="orderListAdvancedSearchURL" escapeAmp="false" action="orderAdvancedSearch">
    <s:param name="searchFieldName" value="SearchFieldName"/>
    <s:param name="searchFieldValue" value="SearchFieldValue"/>
    <s:param name="OrderNameValue" value="OrderNameSearchValue"/>
    <s:param name="ProductIdValue" value="ProductIdSearchValue"/>
    <s:param name="OwnerFirstNameValue" value="OwnerFirstNameSearchValue"/>
    <s:param name="OwnerLastNameValue" value="OwnerLastNameSearchValue"/>    
    <s:param name="createTSFrom" value="CreateTSFrom"/>
    <s:param name="modifyTSFrom" value="ModifyTSFrom"/>
	<s:param name="submittedTSFrom" value="SubmittedTSFrom"/>
    <s:param name="createTSTo" value="CreateTSTo"/>
    <s:param name="modifyTSTo" value="ModifyTSTo"/>
	<s:param name="submittedTSTo" value="SubmittedTSTo"/>
    <s:param name="OrderNumberValue" value="OrderNumberSearchValue"/>
    <s:param name="PurchaseOrderNumberValue" value="PurchaseOrderNumberSearchValue"/>
    <s:param name="statusSearchFieldName" value="StatusSearchFieldNameValue"/>
    <s:param name="holdSearchFieldName" value="HoldStatusSearch"/>
</s:url>
<s:url id="returnUrl" action="orderList">
    <s:param name="orderByAttribute" value="orderByAttribute"/>
    <s:param name="orderDesc" value="orderDesc"/>
    <s:param name="pageNumber" value='%{pageNumber}'/>
    <s:param name="searchFieldName" value="getXpedxSearchFieldName()"/>
    <s:param name="searchFieldValue" value="searchFieldValue"/>
    <s:param name="statusSearchFieldName" value="getExactStatus()"/>
	<s:param name="submittedTSFrom" value="submittedTSFrom"/>
	<s:param name="submittedTSTo" value="submittedTSTo"/>
    <s:param name="shipToSearchFieldName" value="shipToSearchFieldName"/>
    <!-- 
    <s:param name="OrderNameValue" value="OrderNameSearchValue"/>
    <s:param name="ProductIdValue" value="ProductIdSearchValue"/>
    <s:param name="OwnerFirstNameValue" value="OwnerFirstNameSearchValue"/>
    <s:param name="OwnerLastNameValue" value="OwnerLastNameSearchValue"/>    
    <s:param name="createTSFrom" value="CreateTSFrom"/>
    <s:param name="modifyTSFrom" value="ModifyTSFrom"/>
    <s:param name="createTSTo" value="CreateTSTo"/>
    <s:param name="modifyTSTo" value="ModifyTSTo"/>
    <s:param name="OrderNumberValue" value="OrderNumberSearchValue"/>
    <s:param name="PurchaseOrderNumberValue" value="PurchaseOrderNumberSearchValue"/>
    <s:param name="holdSearchFieldName" value="HoldStatusSearch"/>  -->
</s:url>
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util'/>
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean' id='xpedxutil' />
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='dateUtilBean'/>
<s:set name="xutil" value="XmlUtils"/>
<s:set name="xpedxCustomerContactInfoBean" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("XPEDX_Customer_Contact_Info_Bean")' />
<!-- get the search result xml: getDocuments is an action method -->
<s:set name='sdoc' value="outputDoc"/>
<s:set name='desc' value="orderDesc"/>
<s:set name="openOrder" value="%{'true'}"/>
</head>

<body class="ext-gecko ext-gecko3" onLoad="setDateFields();hideSearchField(false);">
<div id="main-container">
<div id="main">

<!-- begin header -->
   <s:action name="xpedxHeader" executeResult="true" namespace="/common" />
<!-- // header end -->

<s:set name="ViewReportsFlag" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getReportsFlagForLoggedInUser(wCContext)" />

<div class='x-hidden dialog-body ' id="shipToDivforordersearch">
	<div id="shipToOrderSearchDiv"></div>
</div>
		<div id="view-order-popup" style="display: none;">
			<div class="float-right">
				<img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_charcoal_x.png" id="view-order-history-btn-close" width="12" height="12" alt="[Close Window]" />
			</div>
			<div> 
				<p>  </p>
			</div>
		</div>
		
		<div id="view-invoices-popup" style="display: none;">
			<div class="float-right">
				<img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_charcoal_x.png" id="view-all-invoices-btn-close" width="12" height="12" alt="[Close Window]" />
			</div>
			<div> 
				<p> Search all your invoices; you'll be taken to a new screen. </p>
			</div>
		</div>
		<!-- end tooltip boxes -->
            <div class="container orders-page" > 
                <!-- breadcrumb -->
                <div class="OM-breadcrumb">
                	<%-- <p><span class="page-title"> Order Management</span></p> --%>
                	 <p><span class="page-title"> <s:text name='MSG.SWC.ORDR.ORDRLIST.GENERIC.PGTITLE' /> </span></p>
                	<!-- <div id="divid" align="center" style="color:red;">&nbsp;</div>  --> 
                </div>
                <!-- end breadcrumb -->
                <!-- begin top section -->
                <s:set name="selectedHeaderTab" value="#_action.getXpedxSelectedHeaderTab()"> </s:set>
                <s:set name="blankValue" value="%{#selectedHeaderTab ==#blankValue}" />
	     		<s:form id="orderListForm" name="orderListForm"  action="orderList"
                    namespace="/order" cssClass="myClass" method="post" validate="true">
                    <s:hidden name='#action.name' value='orderList'/>
                	<s:hidden name='#action.namespace' value='/order'/>
                	<%-- start of Fix : JIRA - 3123 --%>
                	<s:if test='!#blankValue '> 
	                	<s:hidden name='xpedxSelectedHeaderTab' value="%{'AddToExistingOrder'}"/>
	                	<s:hidden name='sourceTab' value="%{'Open'}"/>
	                	<s:set name='openOrder' value="%{'true'}"/>
	                	<%-- End of Fix : JIRA - 3123 --%>
                	</s:if>
<br/>
                <div class="rounded-border top-section ">
                	<!-- begin content w/border -->
			<fieldset class="x-corners mil-col-mil-div">
			<!-- text on border -->
			<s:if test="%{#ViewReportsFlag}">
			    <legend class="search-legend"> <s:text name='MSG.SWC.ORDR.ORDRLIST.GENERIC.ORDRLAST6MONTHS' /> <a href="#" id="" ><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_charcoal_help.png" alt="" 
			    	 title="View orders which have been placed in the last 6 months. To view older orders, select 'View Order History Reports' below." /></a></legend>
			 </s:if>
			 <s:else>
			    <legend class="search-legend"> <s:text name='MSG.SWC.ORDR.ORDRLIST.GENERIC.ORDRLAST6MONTHS' /> <a href="#" id="" ><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_charcoal_help.png" alt="" 
			    	title="View orders which have been placed in the last 6 months. " /></a></legend>
			 </s:else>
                        <!-- begin content-holding table -->
                        <table border="0px solid red" cellpadding="0" cellspacing="0" class="full-width line-spacing-tr" id="top-section">
                        <tr>
													<td style="width: 85px;">Search By:</td>
													<td><s:select theme="simple"  name="searchFieldName" onchange="javascript:hideSearchField();" list="searchList" id="searchFieldName" />&nbsp; 
													
												 	<div id ="searchFieldValueDivId"  style="display:none;" > 
														<s:textfield theme="simple" cssStyle="width:209px;" cssClass="search-by-input-field"  name="searchFieldValue" value="%{#parameters.searchFieldValue}"  id="searchFieldValue" />				 
													</div>
													<!-- <input type="text" onclick="javascript:searchTerm_onclick()" class="search-by-input-field x-input" id="searchFieldValue" value="Search Orders " name="searchFieldValue"> -->
													</td>
													<td colspan="2"></td>
												</tr>
												<tr>
												<%-- start of Fix : JIRA - 3123 --%>
	            									<s:if test='#blankValue '> 
	            							<td>Order Status: </td>
													<td>
														<s:select cssClass=" " name="statusSearchFieldName" list="statusSearchList" value="%{#parameters.statusSearchFieldName}" id="statusSearchFieldName"/>
														<s:set name='openOrder' value="%{'false'}"/>
													</td>
													<td colspan="2"></td>
													
							</s:if>
							<%-- End of Fix : JIRA - 3123 --%>
                        </tr>
                        <tr>
													 <td>Ordered Date:</td>
													 <td>From&nbsp;&nbsp;<s:hidden id="initialFromDateString" value="%{getInitialFromDateString()}" />

														<s:textfield name='submittedTSFrom' theme="simple" size="15"  cssClass='calendar-input-fields datepicker' value="%{#parameters.submittedTSFrom}" id="FromDate"/>
													 
													 &nbsp;<span style="padding-left: 1.5em;"></span>&nbsp;To&nbsp;&nbsp;
													 <s:hidden id="initialToDateString" value="%{getInitialToDateString()}" />
													 <s:textfield name='submittedTSTo' cssClass='calendar-input-fields  datepicker' theme="simple" value="%{#parameters.submittedTSTo}" id="ToDate"/>
													 
													 &nbsp; (mm/dd/yyyy) 
													 <!-- This needs to be moved, or fixed to accomodate the cell width. (BB)-->
													  <div id="errorDateDiv" style="color:red;"></div>  
													 </td>

													 <td colspan="2"></td>
													 <!-- <td> Amount: </td>
													 <td> <input class=" x-input"/> to <input class=" x-input"/> </td> -->
                        </tr>
                        <!-- <tr>
                        	    <td >Account:</td>
                        	    <td> <select class="account-input-field x-input"> <option value="choose" selected="selected">-Select Account Criteria-</option> <option value="Criteria1">- Account Criteria 1-</option><option value="Criteria2">- Account  Criteria 2-</option> <option value="Criteria3">-  Account Criteria 3-</option> </select> </td>
                        	    <td> &nbsp; </td>
                        	    <td> &nbsp; </td>
                        </tr> -->
                        <!-- <tr>
														<td>Bill-To:</td>
														<td> <select class="bill-to-input-field x-input"> <option value="choose" selected="selected">-Select Search Criteria-</option> <option value="Criteria1">902101135 Metro Graphics DBA, Flash Printing, Charlotte, NC 28205</option><option value="Criteria2">- Criteria 2-</option> <option value="Criteria3">- Criteria 3-</option> </select> </td>
														<td>&nbsp; </td>
														<td> &nbsp; </td>
                        </tr> -->
                        <tr>
						<td> Ship-To: </td>
						<td colspan="2"> 
						<s:select cssClass=" " name="shipToSearchFieldName" headerValue="All Ship-Tos"
						 list="shipToSearchList" value="%{#parameters.shipToSearchFieldName}" id="shipToSearchFieldName" onchange="javascript:showShipTos();"/>
						<s:hidden name="shipToSearchFieldName1" id="shipToSearchFieldName1" value="%{getShipToSearchFieldName()}" />
						<%--<s:select cssClass="ship-to-input-field x-input" name="shipToSearchFieldName" list="shipToList" 
							listKey="key" listValue="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@formatBillToShipToCustomer(key)"
							value="%{#parameters.shipToSearchFieldName}" id="shipToSearchFieldName" /> --%>
						<a href='#shipToOrderSearchDiv' id="shipToOrderSearch"></a></td>
						<td colspan="1"><a class="orange-ui-btn float-right" href="javascript:submit_orderListForm();"><span>Search </span></a> 
						<a class="grey-ui-btn float-right" href="javascript:clearFilters_onclick()" ><span>Clear </span></a> </td>
                        </tr>
                        </table> <!-- end content-holding table -->
                </fieldset><!-- end border content -->
                <div id="search-view-links">
					<s:url id='reportsLink' namespace='/xpedx/services' action='XPEDXReports'>
						<s:param name="xpedxSelectedHeaderTab">ServicesTab</s:param>
					</s:url>
					<s:if test="%{#ViewReportsFlag}">
						<%-- <s:a href='%{reportsLink}' cssClass="link"><span class="underlink">View Order History Reports</span> --%>
 						<s:a href='%{reportsLink}' cssClass="link"><span class="underlink"> <s:text name='MSG.SWC.ORDR.ORDRREPORTS.GENERIC.VIEWORDRHISTREPORTS' /> </span> 
						<img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_charcoal_help.png" alt="" title="List of reports providing order history data for the previous 2 years." /></s:a>
					</s:if>	
					<s:set name="ViewInvoicesFlag" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getInvoiceFlagForLoggedInUser(wCContext)" />
					 <s:if test="%{#ViewInvoicesFlag}"> 
						<a  href="<s:property value='%{invoiceURL}'/>UserID=<s:property value='%{userKey}' />&shipTo=<s:property value='%{custSuffix}' />" target="_blank" id="view-order-history-btn"><span class="underlink">View All Invoices  </span><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_charcoal_help.png" alt="" title="Viewing invoices will open a separate pop-up window. If the window does not open, check your pop-up blocker settings." /></a>
					</s:if> 
				</div>
	    </div> <!-- end top section -->
		
			</s:form>
			
	    <!-- Begin mid-section -->
	    <div class="midsection"> <!-- Begin mid-section container -->
		
             <div id="open-orders-Msg-top"  style="display: none;position:relative;left:375px;color:red;" class="error">&nbsp;</div> 
            <div class="search-pagination-bottom">
            	  <s:if test="%{totalNumberOfPages == 0 || totalNumberOfPages == 1}">Page&nbsp;&nbsp;<s:property value = "%{pageNumber}" /></s:if>
                  <s:if test="%{totalNumberOfPages>1}">Page</s:if>&nbsp;&nbsp;<swc:pagectl currentPage="%{pageNumber}" lastPage="%{totalNumberOfPages}" showFirstAndLast="False"
                 	urlSpec="%{#orderListPaginationURL}"/>
			</div>
<!--	    	<div class="search-pagination-top">Page&nbsp;&nbsp;<a class="underlink" href="#"><</a> <a class="underlink" href="#">1</a> <a class="underlink" href="#">2</a> <a class="underlink" href="#">3</a> <a class="underlink" href="#">4</a> <a class="underlink" href="#">5</a> <a class="underlink" href="#"> ></a></div> -->
			<s:form id="dol" namespace="/order" method="post" onsubmit="return swc_validateForm('dol');" >
				<s:hidden id="action_namespace" name="#action.namespace" value="/order"/>
				<s:hidden id="actionName" name="#action.name" value="draftOrderCopy"/>
					<swc:sortctl sortField="%{orderByAttribute}"
						  sortDirection="%{orderDesc}" down="Y" up="N"
						  urlSpec="%{#orderListSortURL}">
	    	<table class="search-table standard-table">
	    			<tbody>
							<tr class="table-header-bar">
								<td class="table-header-bar-left " style="min-width: 10em;">
						<swc:sortable fieldname="%{'ExtnWebConfNum'}">
										<span style="color: white" class="underlink">Web&nbsp;Confirmation
										</span>
									</swc:sortable></td>
								<td style="min-width: 5.5em;">
									<span style="color: white">Order&nbsp;#</span>
								</td>
								<td style="min-width: 9.5em;"><swc:sortable
										fieldname="%{'CustomerPONo'}">
										<span style="color: white;" class="underlink"> PO # </span>
									</swc:sortable></td>
								<td style="min-width: 6em;"><swc:sortable
										fieldname="%{'OrderDate'}">
										<span class="underlink" style="color: white;">Ordered</span>
									</swc:sortable></td>
								<td style="min-width: 7em;"><swc:sortable
										fieldname="%{'ExtnOrderedByName'}">
										<span class="underlink" style="color: white;">Ordered&nbsp;By
										</span>
									</swc:sortable></td>
								<td style="min-width: 9.3em;"><swc:sortable
										fieldname="%{'ExtnShipToName'}">
										<span class="underlink" style="color: white;">Ship-To</span>
									</swc:sortable></td>
								<td style="min-width: 8em;"><swc:sortable
										fieldname="%{'ExtnTotalOrderValue'}">
										<span class="underlink" style="color: white;">Amount
										</span>
									</swc:sortable></td>
								<td style="min-width: 10.7em;" class="table-header-bar-right"><span
									style="color: white">Status</span>
								</td>
	    			</tr>
	    		<%-- 
	            <s:set name="parentOrderList" value="#util.getElements(#sdoc, '//Page/Output/OrderList/Order')"/>
	            --%>
	            <%-- Parent customer order info is being read from xpedxParentOrderListMap --%>
	            <s:set name="parentOrderList" value="xpedxParentOrderListMap"/>
	            
	            <s:iterator  status='rowStatus' value='xpedxParentOrderListMap' >
	            	<s:set name='parentOrder' value='value' />
	            	<s:set name="priceInfo" value='#parentOrder'/>
	            	<s:set name='currencyCode' value='%{#priceInfo.getAttribute("Currency")}'/>
	            	<s:set name="orderDate" value='%{#dateUtilBean.formatDate(#parentOrder.getAttribute("OrderDate"),wCContext)}'/>
	            	<s:set name='OrderExtn' value='#parentOrder'/>
	            	<s:set name='priceWithCurrency' value='#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, #OrderExtn.getAttribute("ExtnTotalOrderValue"))'/>
	            	<s:set name='shipToAddr' value='#parentOrder'/>
	            	<s:set name='addressLine1' value='%{#shipToAddr.getAttribute("AddressLine1")}'/>
	            	<s:set name='addressLine2' value='%{#shipToAddr.getAttribute("AddressLine2")}'/>
	            	<s:set name='addressLine3' value='%{#shipToAddr.getAttribute("AddressLine3")}'/>
	            	<s:set name='city' value='%{#shipToAddr.getAttribute("City")}'/>
	            	<s:set name='state' value='%{#shipToAddr.getAttribute("State")}'/>
	            	<s:set name='country' value='%{#shipToAddr.getAttribute("Country")}'/>
	            	<s:set name='zip' value='%{#shipToAddr.getAttribute("ZipCode")}'/>
	            	<s:set name='zip' value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedZipCode(#zip)"/>
	            	<s:set name="shipToId" value='#parentOrder.getAttribute("ShipToID")' />
	            	<s:set name="createuserid" value='#parentOrder.getAttribute("Createuserid")' />
					<s:set name='extnInvoiceNo' value='#OrderExtn.getAttribute("ExtnInvoiceNo")' />
					<s:set name='extnInvoicedDate' value='#OrderExtn.getAttribute("ExtnInvoicedDate")' />
					<s:set name='webConfirmationNumber' value='#OrderExtn.getAttribute("ExtnWebConfNum")' />
					<s:set name='legacyOrderNumber' value='#OrderExtn.getAttribute("ExtnLegacyOrderNo")' />
					<s:if test="legacyOrderNumber==''"><s:set name='legacyOrderNumber' value="BlankValue" /></s:if>
					<s:set name="chainedOrderList" value='xpedxChainedOrderListMap.get(#parentOrder.getAttribute("OrderHeaderKey"))'/>
					<s:set name="temChainedOrder" value='#chainedOrderList.get(0)'/>
					<s:set name="sourceTabVal" value="#_action.getSourceTab()"></s:set>
					
					<%-- Fix for Jira 3123, Added openOrder == true to if condition --%>
					<s:if test='#openOrder == "true" && #sourceTabVal != null && #temChainedOrder.getAttribute("Status") == #sourceTabVal '>
						<s:set name='openOrder' value="%{'false'}"/>
					</s:if>
					<%-- End Fix for Jira 3123 --%>
					
					<s:set name='customerOhk' value='#parentOrder.getAttribute("OrderHeaderKey")' />
								
					<%-- <tr <s:if test="#rowStatus.odd == true" >class="odd"</s:if> style="border-top: 1px solid #D7D7D7;">	 --%>		
					
					<s:if test="#rowStatus.last" >
						<tr <s:if test="#rowStatus.odd == true" >class="last odd"</s:if><s:else>class="last"</s:else> style="border-top: 1px solid #D7D7D7;">
              		</s:if>
              		<s:else>
              			<tr <s:if test="#rowStatus.odd == true" >class="odd"</s:if> style="border-top: 1px solid #D7D7D7;">
              		</s:else>	
	                	
	                <td class="left-cell">
	                	<s:set name="chainedOrderListSize" value='#chainedOrderList.size()'/>
	            		<s:if test='xpedxChainedOrderListMap.containsKey(#parentOrder.getAttribute("OrderHeaderKey")) && #chainedOrderListSize > 1'>
		               		 <s:url id="orderDetailsURL" action="orderDetail" escapeAmp="false" >
							   <s:param name="orderHeaderKey" value='#parentOrder.getAttribute("OrderHeaderKey")'/>
							   <s:param name="orderListReturnUrl" value='#returnUrl'/>
							   <s:param name="splitStatus" value="%{'Y'}" />  
							</s:url>
	                    </s:if>		                                        
						<s:else>	
							  <s:url id="orderDetailsURL" action="orderDetail" escapeAmp="false" >
							    <s:param name="orderHeaderKey" value='#parentOrder.getAttribute("OrderHeaderKey")'/>
							    <s:param name="orderListReturnUrl" value='#returnUrl'/>
							  </s:url>
						</s:else>  
						<span class="underlink">					
						<s:a href="%{orderDetailsURL}">
						  <s:property value='#webConfirmationNumber'/>
						</s:a>
						</span>
	                </td>
	            	<td rowspan="1">
	            		<s:set name="chainedOrderListSize" value='#chainedOrderList.size()'/>
	            		<s:if test='xpedxChainedOrderListMap.containsKey(#parentOrder.getAttribute("OrderHeaderKey")) && #chainedOrderListSize > 1'>
							<a class="underlink" id="split-btn" onclick="linkedRowToggle('<s:property value='#parentOrder.getAttribute("OrderHeaderKey")'/>');">Split</a>
	            			 </td>
						<td></td>
				    	<td></td>
				    	<td></td>
				    	<td></td>
				    	<td></td>
				    	<td>
				    		<s:set name="isPendingApproval" value="%{#_action.isOrderOnHold(#parentOrder,'ORDER_LIMIT_APPROVAL')}" />
							<s:set name="isOrderNeedsAttention" value="%{#_action.isOrderOnHold(#parentOrder,'NEEDS_ATTENTION')}" />
							<s:set name="isOrderLegacyCnclOrd" value="%{#_action.isOrderOnHold(#parentOrder,'LEGACY_CNCL_ORD_HOLD')}" />
							<s:set name="isOrderException" value="%{#_action.isOrderOnHold(#parentOrder,'ORDER_EXCEPTION_HOLD')}" />
							<s:set name="isOrderRejected" value="%{#_action.isOrderOnRejectHold(#parentOrder)}" />
							<s:set name="status" value="#parentOrder.getAttribute('Status')" />
							
							<s:if test='%{#status != "Cancelled"}'>
								<s:property value="#status" />
								
									<s:if test='#isPendingApproval && !#isOrderRejected'>
										 <s:text name='MSG.SWC.ORDR.NEEDSATTENTION.GENERIC.STATUSPENDING.PENDAPPROVAL' />
									</s:if>
									<s:elseif test='#isPendingApproval && #isOrderRejected'>
										 <s:text name='MSG.SWC.ORDR.NEEDSATTENTION.GENERIC.STATUSPENDING.REJECTED' />
									</s:elseif>
									<s:elseif test='#isOrderNeedsAttention || #isOrderLegacyCnclOrd || #isOrderException'>
										 <s:text name='MSG.SWC.ORDR.NEEDSATTENTION.GENERIC.STATUSPENDING.CSRREVIEW' />
									</s:elseif>
							</s:if>
							<s:else>
								<s:property value="#status" />
							</s:else>							
							
				    	</td>
            	
	                    </s:if>	                    
						<s:else>
						
							<s:set name="chainedOrderListSize" value='#chainedOrderList.size()' />
							<s:if test="#chainedOrderListSize > 0">
								<s:set name="chainedOrder" value='#chainedOrderList.get(0)' />
							</s:if>
							<s:else>
								<s:set name="chainedOrder" value='#parentOrder' />
							</s:else>
							<s:set name='ChainedOrderExtn'
								value='#chainedOrder' />
							<s:set name='ChainedLegacyOrderNumber'
								value='#ChainedOrderExtn.getAttribute("ExtnLegacyOrderNo")' />
								
							<s:url id="chainedOrderDetailsURL" action="orderDetail"
								escapeAmp="false">
								<s:param name="orderHeaderKey" value='#chainedOrder.getAttribute("OrderHeaderKey")' />
								<s:param name="orderListReturnUrl" value='#returnUrl' />										
								<s:param name="parentOrderKey" value='#parentOrder.getAttribute("OrderHeaderKey")'/>
								<s:param name="theWebConfNumber" value='#webConfirmationNumber'/>		
								<s:param name="dispOrderOrWebConf" value="%{'dispWebConf'}" />					
							</s:url>
							
							<s:if test='#ChainedLegacyOrderNumber!=""'>
							<span class="underlink">
								<s:a href="%{chainedOrderDetailsURL}">
									<s:property value='@com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils@getFormattedOrderNumber(#ChainedOrderExtn.getAttribute("ExtnOrderDivision"),#ChainedLegacyOrderNumber,#ChainedOrderExtn.getAttribute("ExtnGenerationNo"))' />
									<s:param name="parentOrderKey" value='#parentOrder.getAttribute("OrderHeaderKey")'/>
									<s:param name="theWebConfNumber" value='#webConfirmationNumber'/>
								</s:a>
								</span>
							</s:if>
							
							<%--<s:else>
										<s:a href="%{chainedOrderDetailsURL}">
											In progress
										</s:a>
									</s:else> --%>
		
							<%--<s:set name="priceInfo"
								value='#parentOrder.getElementsByTagName("PriceInfo")' />
							<s:set name='currencyCode'
								value='%{#priceInfo.item(0).getAttribute("Currency")}' /> --%>
								
							<s:set name='priceWithCurrency'
								value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, #OrderExtn.getAttribute("ExtnTotalOrderValue"))}' />
							<s:set name="orderDate"
								value='%{#dateUtilBean.formatDate(#parentOrder.getAttribute("OrderDate"),wCContext)}' />
		
							<td><s:set name='customerPO'
								value='%{#parentOrder.getAttribute("CustomerPONo")}' /> <SCRIPT type="text/javascript">
								printPOs('<s:property value="#customerPO" escape='false'/>');
								</SCRIPT></td>
		
							<td><s:property value='#orderDate' /> </td>
							<%-- Fix for JIRA 2243 : Showing OrderedByName from customer order --%>
							<td><s:property
								value='#OrderExtn.getAttribute("ExtnOrderedByName")' /></td>
		
							<td><s:property
								value='#OrderExtn.getAttribute("ExtnShipToName")' /> <br />
							<s:if test='%{#addressLine1!=null && #addressLine1.length()>0}'>
								<s:property value='#addressLine1' />
								<br />
							</s:if> 
								<s:if test='%{#addressLine2!=null && #addressLine2.length()>0}'>
								<s:property value='#addressLine2' />
								<br />
							</s:if>	<s:if test='%{#addressLine3!=null && #addressLine3.length()>0}'>
								<s:property value='#addressLine3' />
								<br />
							</s:if>							
							<s:if test='%{#city!=null && #city.length()>0}'>
								<s:property value='#city' />,
							</s:if> 
							<s:if test='%{#state!=null && #state.length()>0}'>
								<s:property value='#state' />&nbsp;
							</s:if> 
							<s:if test='%{#zip!=null && #zip.length()>0}'>
								<s:property value='#zip' />
							<!--  Address ZipExt , Country code, Local ID -->	
							</s:if> 
							<s:if test='%{#country!=null && #country.length()>0}'>
								&nbsp;<s:property value='#country' />
							</s:if> 
							
							<br /> 
							<s:if test='%{#shipToId!=null && #shipToId.length()>0}'>
								<s:property
									value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@formatBillToShipToCustomer(#shipToId)' />
								
							</s:if><br /></td>
		
							<td align="right">
								<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
									<s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
									<s:if test="%{#priceWithCurrency == #priceWithCurrencyTemp}">
										<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>  
		                    		</s:if>
		                            <s:else>
										(<s:property value='#currencyCode' />) <s:property value='#priceWithCurrency' /> 
									</s:else>
								</s:if>							
							</td>
							<td class="right-cell">
								<s:set name="isPendingApproval" value="%{#_action.isOrderOnHold(#parentOrder,'ORDER_LIMIT_APPROVAL')}" />
								<%--   <s:set name="isOrderNeedsAttention" value="%{#_action.isOrderOnHold(#parentOrder,'NEEDS_ATTENTION')}" />
								 <s:set name="isOrderLegacyCnclOrd" value="%{#_action.isOrderOnHold(#parentOrder,'LEGACY_CNCL_ORD_HOLD')}" /> 
								<s:set name="isOrderException" value="%{#_action.isOrderOnHold(#parentOrder,'ORDER_EXCEPTION_HOLD')}" />  --%>
								<s:set name="isOnCSRReviewHold" value="%{#_action.isOrderOnCSRReviewHold(#parentOrder)}" />
								<s:set name="isOrderRejected" value="%{#_action.isOrderOnRejectHold(#parentOrder)}" />
								<s:set name="Orderstatus" value="#parentOrder.getAttribute('Status')" />
								
								<s:if test='%{#chainedOrder.getAttribute("Status") == "Awaiting FO Creation" || #parentOrder.getAttribute("Status") == "Awaiting FO Creation"}'>
												Submitted <s:text name='MSG.SWC.ORDR.NEEDSATTENTION.GENERIC.STATUSPENDING.CSRREVIEW' />
								</s:if>
								<s:else>
									<s:if test='%{#status != "Cancelled"}'>
											<s:property value="#parentOrder.getAttribute('Status')" />
											<s:if test='#isPendingApproval && !#isOrderRejected'>
												 <s:text name='MSG.SWC.ORDR.NEEDSATTENTION.GENERIC.STATUSPENDING.PENDAPPROVAL' />
												<br/>
												<s:set name="loggedInUser" value="%{#_action.getWCContext().getLoggedInUserId()}"/>
											 	<s:set name='resolverId' value="%{#_action.getResolverUserId(#parentOrder,'ORDER_LIMIT_APPROVAL')}"/>
											 	<s:set name='primaryApprover' value="%{#_action.getPrimaryApproverID()}"/>
											 	<s:set name='proxyApprover' value="%{#_action.getProxyApproverID()}"/>
											 	<s:if test='%{#xpedxCustomerContactInfoBean.getIsApprover() == "Y" && (#primaryApprover == #loggedInUser || #proxyApprover == #loggedInUser)}'>
													<s:a key="accept" href="javascript:openNotePanel('approvalNotesPanel', 'Approve','%{customerOhk}'); " cssClass="grey-ui-btn" cssStyle="margin-right:5px;" tabindex="91" theme="simple"><span>Approve / Reject</span></s:a>
												</s:if><br/>
											</s:if>
											<s:elseif test='#isPendingApproval && #isOrderRejected'>
												 <s:text name='MSG.SWC.ORDR.NEEDSATTENTION.GENERIC.STATUSPENDING.REJECTED' />
											</s:elseif>
											<s:elseif test='#isOnCSRReviewHold'> 
												 <s:text name='MSG.SWC.ORDR.NEEDSATTENTION.GENERIC.STATUSPENDING.CSRREVIEW' />                    				 			
											</s:elseif>
										
									</s:if>
									<s:else>
										<s:property value='#chainedOrder.getAttribute("Status")' />
									</s:else>
								</s:else>								
								</td>
		
						</s:else>
	            		
	            	</tr>
	             	<tbody id="ChildOf_<s:property value='#parentOrder.getAttribute("OrderHeaderKey")'/>" style="display:none; ">
	            	<s:iterator value='xpedxChainedOrderListMap'>
	            		<s:set name='chainedOrderListKey' value='key'/>
	                	<s:set name='chainedOrderList' value='value'/>
	                	<s:iterator value='#chainedOrderList' id='chainedOrder' status='iStatus'>
	                		<s:if test='#chainedOrderListKey==#parentOrder.getAttribute("OrderHeaderKey")'>	                		
			            	<s:set name="priceInfo" value='#parentOrder'/>
	            			<s:set name='currencyCode' value='%{#priceInfo.getAttribute("Currency")}'/>			            	
			            	<s:set name="orderDate" value='%{#dateUtilBean.formatDate(#parentOrder.getAttribute("OrderDate"),wCContext)}'/>
			            	<s:set name='ChainedOrderExtn' value='#chainedOrder'/>
							<s:set name='ChainedLegacyOrderNumber' value='#ChainedOrderExtn.getAttribute("ExtnLegacyOrderNo")'/>
							<s:set name='priceWithCurrency' value='#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, #ChainedOrderExtn.getAttribute("ExtnTotalOrderValue"))'/>
							
							<s:if test='#ChainedLegacyOrderNumber==""'>
								<s:set name='ChainedLegacyOrderNumber' value="''"/>
							</s:if>
							<s:if test="#rowStatus.last" >
								<tr <s:if test="#rowStatus.odd == true" >class="last odd"</s:if><s:else>class="last"</s:else> style="border-top: 1px solid #D7D7D7;">
	                		</s:if>
	                		<s:else>
	                			<tr <s:if test="#rowStatus.odd == true" >class="odd"</s:if> style="border-top: 1px solid #D7D7D7;">
	                		</s:else>
	                		
	                			<td class="left-cell">  &nbsp;</td>
				            	<td>
				            		<s:url id="chainedOrderDetailsURL" action="orderDetail" escapeAmp="false" >
									  <s:param name="orderHeaderKey" value='#chainedOrder.getAttribute("OrderHeaderKey")'/>
									  <s:param name="orderListReturnUrl" value='#returnUrl'/>  
									</s:url>
									<s:if test='%{#ChainedOrderExtn.getAttribute("ExtnLegacyOrderNo")==""}'>
										<s:a href="%{chainedOrderDetailsURL}">
										In progress
										</s:a>
									</s:if>
									<s:else>
										<s:a href="%{chainedOrderDetailsURL}">
							  				<s:property value='@com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils@getFormattedOrderNumber(#ChainedOrderExtn.getAttribute("ExtnOrderDivision"),#ChainedOrderExtn.getAttribute("ExtnLegacyOrderNo"),#ChainedOrderExtn.getAttribute("ExtnGenerationNo"))' />
										</s:a>
									</s:else>
				            	</td>
			            		<td><s:set name='customerPO' value='%{#parentOrder.getAttribute("CustomerPONo")}'/>
									<SCRIPT type="text/javascript">
									printPOs('<s:property value="#customerPO" escape='false'/>');
									</SCRIPT>
	                          	</td>
	           	
				            	<td><s:property value='#orderDate'/></td>
				            	
				            	<td><s:property value='#OrderExtn.getAttribute("ExtnOrderedByName")'/></td>
				            	
				            	<td><s:property value='#OrderExtn.getAttribute("ExtnShipToName")'/></td>
				            	
				            	<td>
				            		<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
					            		<s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
											<s:if test="%{#priceWithCurrency == #priceWithCurrencyTemp}">
												<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>  
				                    		</s:if>
				                            <s:else>
												(<s:property value='#currencyCode' />) <s:property value='#priceWithCurrency' /> 
											</s:else>
									</s:if>
				            	</td>
				            	
				            	<td class="right-cell">
					            	<s:if test='%{#chainedOrder.getAttribute("Status") == "Awaiting FO Creation"'>
													Submitted <s:text name='MSG.SWC.ORDR.NEEDSATTENTION.GENERIC.STATUSPENDING.CSRREVIEW' />
									</s:if>
									<s:else>
					            		<s:property value='#chainedOrder.getAttribute("Status")'/>
					            	</s:else>
				            	</td>
				            </tr>
				            </s:if>				            
	            		</s:iterator>	
	            	</s:iterator>
	                </tbody> 	                 
	            </s:iterator>				
	    	</table>
			  </swc:sortctl>
    </s:form>
    		<!-- <div id="table-bottom-bar" class="search-bottom-table-bar"><div id="table-bottom-bar-L"></div><div id="table-bottom-bar-R"></div></div> -->
    		
            <div class="search-pagination-bottom">
				 <s:if test="%{totalNumberOfPages == 0 || totalNumberOfPages == 1}">Page&nbsp;&nbsp;<s:property value = "%{pageNumber}" /></s:if>
                 <s:if test="%{totalNumberOfPages>1}">Page</s:if>&nbsp;&nbsp;<swc:pagectl currentPage="%{pageNumber}" lastPage="%{totalNumberOfPages}" showFirstAndLast="False"
                 	urlSpec="%{#orderListPaginationURL}"/>
			</div>
		 <div id="open-orders-Msg-bottom" style="display: none;position:relative;left:375px;color:red;" align="center" class="error">&nbsp;</div> 
		 
	    </div> <!-- end mid-section container -->
	    <!-- End mid section -->

<!-- end second container div-->
        </div><!-- end main  -->
		<!-- begin footer -->
    <s:action name="xpedxFooter" executeResult="true" namespace="/common" />
	<!-- // footer end -->
	<%-- Add the Approval Panel --%>
	
		<swc:dialogPanel title="Approval/Rejection Notes" isModal="true" id="approvalNotesPanel"> 		
		<div  class="xpedx-light-box" id="" style="width:400px; height:300px;">
			<!-- <h2>Approval / Rejection Comments</h2>		 -->	
			<h2><s:text name="MSG.SWC.ORDR.PENDAPPROVALS.GENERIC.APPROVALREJECTCOMMENT" /></h2>			
				<s:form id="approval" action="approvalAction" namespace="/order" validate="true" method="post">					
					<s:textarea id="ReasonText1" name="ReasonText1" cols="69" rows="5" theme="simple"></s:textarea>
					<s:hidden name="ReasonText" id="ReasonText" value="" />
					<s:hidden name="OrderHeaderKey" value="" />
					<s:hidden name="ApprovalAction" value=""/>
					<s:hidden name="ApprovalActionRequestUrl" value="orderList"/>
					<s:hidden name="#action.namespace" value="/order"/>
					<s:hidden id="actionName" name="#action.name" value="approval"/>
					<ul id="tool-bar" class="tool-bar-bottom">
						<li><a style="float:right; class="grey-ui-btn" href="#" onclick="javascript:DialogPanel.hide('approvalNotesPanel');"><span>Cancel</span></a></li>
						<li><a style="float:right;" class="grey-ui-btn" href="#" onclick="javascript:openNotePanelSetAction('Reject');"><span>Reject</span></a></li>
						<li><a style="float:right;" class="green-ui-btn" href="#" onclick="javascript:openNotePanelSetAction('Accept');"><span>Approve</span></a></li>						 
					</ul>
				</s:form>
				</div>
		 </swc:dialogPanel> 
    </div><!-- end container  -->
	
</body>


<!-- Added the below commented scripts in xpedx-header.js -->


<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel.js"></script>-->

<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo.js"></script>
-->

<!-- End of files in xpedx-header.js -->

<!--  Added the below commented scripts in xpedx-ext-header.js -->


<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt.js"></script>
<!-- End of files in xpedx-ext-header.js -->

<!-- Added the below commented scripts in xpedx-jquery-header.js -->
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
-->

<!-- End of files in xpedx-ext-header.js --><!--

<!-Added the below commented scripts in xpedx-order-common.js -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add.js"></script>
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pseudofocus.js"></script>
--><!-- End of files in xpedx-order-common.js -->

<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery-ui.min.js"></script>
--><!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/DD_roundies_0.0.2a-min.js"></script>
--><script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
<%--
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/jQuery.js"></script> 
--%> 
<!-- Facy Box (Lightbox/Modal Window -->

<script type="text/javascript">
	
	 	
/*function setErrorMessage(flag,divId)
	{
		if(flag == "true")
		{
			var divid=document.getElementById(divId);
			divid.innerHTML="Currently No Open Orders are Available.";
			//divid.innerHTML="<s:text name='MSG.SWC.ORDR.OM.INFO.NOOPENORDERS' />";
			
			open-orders-Mag-bottom
		}
	} 
	setErrorMessage('<s:property value="#openOrder"/>',"divid");
	*/
	<%-- start of Fix : JIRA - 3123 --%>
	
	function setErrorMessage(flag)
	{
		if(flag == "true")
		{
		//"Currently No Open Orders are Available.";
		var dividtop=document.getElementById("open-orders-Msg-top");
		dividtop.innerHTML="<s:text name='MSG.SWC.ORDR.OM.INFO.NOOPENORDERS' />";
		dividtop.style.display = "inline"; 
		
		var dividbottom=document.getElementById("open-orders-Msg-bottom");
		dividbottom.innerHTML="<s:text name='MSG.SWC.ORDR.OM.INFO.NOOPENORDERS' />";
		dividbottom.style.display = "inline"; 
		}
		else
		{
			//"Currently No Open Orders are Available.";
			var dividtop=document.getElementById("open-orders-Msg-top");
			dividtop.innerHTML="";
			dividtop.style.display = "none"; 
			
			var dividbottom=document.getElementById("open-orders-Msg-bottom");
			dividbottom.innerHTML="";
			dividbottom.style.display = "none"; 
		}
	}
	
	setErrorMessage('<s:property value="#openOrder"/>');
	
	<%-- End of Fix : JIRA - 3123 --%>
	</script>

<script type="text/javascript">
function openNotePanel(id, actionValue,orderHeaderKey){
	document.forms["approval"].elements["ReasonText"].value = "";
	DialogPanel.show(id);
	svg_classhandlers_decoratePage();
	/* if(actionValue == "Approve")
	     document.forms["approval"].elements["ApprovalAction"].value = "1300";
	 if(actionValue == "Reject")
	     document.forms["approval"].elements["ApprovalAction"].value = "1200";*/
	 document.forms["approval"].elements["OrderHeaderKey"].value = orderHeaderKey;
	}
   function openNotePanelSetAction(actionValue){
	 if(actionValue == "Accept"){
	     document.forms["approval"].elements["ApprovalAction"].value = "1300";
	     if(document.getElementById("ReasonText1")!=null && document.getElementById("ReasonText1").value==""){
	 		document.getElementById("ReasonText").value="Accepted";
	 	}
	     else{
	    	 document.getElementById("ReasonText").value=document.getElementById("ReasonText1").value;

		 }	
	 }
	 if(actionValue == "Reject"){
	     document.forms["approval"].elements["ApprovalAction"].value = "1200";
	     if(document.getElementById("ReasonText1")!=null && document.getElementById("ReasonText1").value==""){
		 		document.getElementById("ReasonText").value="Rejected"
		 }	
	     else{
	    	 document.getElementById("ReasonText").value=document.getElementById("ReasonText1").value;

		 }	
	 }		
	//submit it	
	 document.forms["approval"].submit();	
	}
	</script>

<script type="text/javascript">
			
			function linkedRowToggle(parentID){
				var el = document.getElementById('ChildOf_'+parentID);
				switch(el.style.display){
				case 'none':
					el.style.display = 'table-row-group';
					break;
				case 'table-row-group':
					el.style.display = 'none';
					break;
				}									
			}
</script>
<script type="text/javascript">
	function alwaysHideSearchField(){
		 var searchFieldValueDivId = document.getElementById("searchFieldValueDivId" );
		 searchFieldValueDivId.style.display = "none";
	 }
	function hideSearchField(emptySearchFieldValue){
		 var searchFieldInitinalValue = "- Select Search Criteria -";
		 var searchTermFeild = document.getElementById("searchFieldName" ); 
		 var searchFieldValueDivId = document.getElementById("searchFieldValueDivId" ); 
		 var searchFieldValueFld = document.getElementById("searchFieldValue" );
		 if(emptySearchFieldValue == null || emptySearchFieldValue == undefined)
			 emptySearchFieldValue = true
		 if(emptySearchFieldValue)
		 	searchFieldValueFld.value='';
		 var myindex  = searchTermFeild.selectedIndex
		 var SelValue = searchTermFeild.options[myindex].value
		 if ( myindex != 0 ){
			 searchFieldValueDivId.style.display = "";}
		 else{
			 searchFieldValueDivId.style.display = "none";}
		 return; 
	}
	function clearFilters_onclick(){
		alwaysHideSearchField();
		Ext.fly('searchFieldName').dom.selectedIndex='0';
		Ext.fly('searchFieldValue').dom.value='';
		Ext.fly('FromDate').dom.value= Ext.fly('initialFromDateString').dom.value;
		//Ext.fly('shipToSearchFieldName').dom.value=''; Commented this, as it was clearing the shiptos, on click of clear button -Jira 3963
		//Added For Jira 3963
		document.getElementById("shipToSearchFieldName").selectedIndex='0';
		Ext.fly('statusSearchFieldName').dom.selectedIndex='0';
		Ext.fly('ToDate').dom.value= Ext.fly('initialToDateString').dom.value ;
		document.getElementById("errorDateDiv").innerHTML = '';
		//document.getElementById("shipToOrderSearch").innerHTML = '[Select]';
	}
	function showShipTos(){		
		if (document.getElementById("shipToSearchFieldName").selectedIndex=='1'){	
			$('a[href="#shipToOrderSearchDiv"]').click(); }
		else if(document.getElementById("shipToSearchFieldName").selectedIndex=='0'){
			document.getElementById("shipToSearchFieldName1").value="";}
		else{
			document.getElementById("shipToSearchFieldName1").value=document.getElementById("shipToSearchFieldName").value}
		return;
		}
	
//added for jira 3542 - Order Date validation	
	function isValidDate(dtStr)
	{
		var daysInMonth = DaysArray(12)
		var pos1=dtStr.indexOf(dtCh)
		var pos2=dtStr.indexOf(dtCh,pos1+1)
		var strMonth=dtStr.substring(0,pos1)
		var strDay=dtStr.substring(pos1+1,pos2)
		var strYear=dtStr.substring(pos2+1)
		strYr=strYear
		if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
		if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)
		for (var i = 1; i <= 3; i++) {
			if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
		}
		month=parseInt(strMonth)
		day=parseInt(strDay)
		year=parseInt(strYr)
		if (pos1==-1 || pos2==-1){
			return false
		}
		if (strMonth.length<1 || month<1 || month>12){
			return false
		}
		if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
			return false
		}
		if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
			return false
		}
		if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
			return false
		}
	return true
	}
	function submit_orderListForm()
	{
		var str1  = document.getElementById("FromDate").value; 
		var str2  = document.getElementById("ToDate").value;
		if(str1!=null && str1.length>0 && str2!=null && str2.length>0)
		{ 
			var mon1   = parseInt(str1.substring(0,2),10);  
			var dt1  = parseInt(str1.substring(3,5),10); 
			var yr1   = parseInt(str1.substring(6,10),10);  
			var mon2   = parseInt(str2.substring(0,2),10);  
			var dt2  = parseInt(str2.substring(3,5),10);  
			var yr2   = parseInt(str2.substring(6,10),10);  
			var date1 = new Date(yr1, mon1, dt1);  
			var date2 = new Date(yr2, mon2, dt2);    
			if (isValidDate(str1)==false){
				if (isValidDate(str2)==false){
					document.getElementById("errorDateDiv").innerHTML = "Please enter a valid From and To date";
					return;
			}
				document.getElementById("errorDateDiv").innerHTML = "Please enter a valid From date";
				return;
			}
			if (isValidDate(str2)==false){
				document.getElementById("errorDateDiv").innerHTML = "Please enter a valid To date";
				return;
			}
			if(date2 < date1) {    
				document.getElementById("errorDateDiv").innerHTML = "From date cannot be greater than To date";
				return; 
			}
			//end of jira 3542 changes
		} 
		document.getElementById("errorDateDiv").innerHTML = '';
		document.orderListForm.submit();
	}
	function setDateFields(){
		if(Ext.fly('initialFromDateString').dom.value != '' && Ext.fly('initialFromDateString').dom.value != ''){
			Ext.fly('FromDate').dom.value = Ext.fly('initialFromDateString').dom.value;
			Ext.fly('ToDate').dom.value = Ext.fly('initialToDateString').dom.value ;}
		}
</script>
</swc:html>
