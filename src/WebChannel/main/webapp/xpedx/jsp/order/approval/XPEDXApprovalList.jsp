<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% request.setAttribute("isMergedCSSJS","true"); %>
<swc:html>
<head>

<!-- styles -->
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" />
<!--[if IE]> 
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/<s:property value="wCContext.storefrontId" />/css/sfskin-ie-<s:property value="wCContext.storefrontId" /><s:property value='#wcUtil.xpedxBuildKey' />.css" /> 
<![endif]--> 
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/ORDERS<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link rel="stylesheet" type="text/css"
               href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />

<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/ie-hacks<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<![endif]-->

<!-- sterling 9.0 base  do not edit  javascript move all functions to js/global-xpedx-functions.js -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/swc.js"></script>

<!-- sterling 9.0 base  do not edit  javascript move all functions to js/global-xpedx-functions.js -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jQuery.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.core.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/ajaxValidation.js"></script>


<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel.js"></script>
<!-- carousel scripts js   -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.tabs.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.shorten.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/modals/checkboxtree/jquery.checkboxtree.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery-ui.min.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/DD_roundies_0.0.2a-min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pseudofocus.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global-xpedx-functions.js"></script>

<!-- Web Trends tag start -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/webtrends/displayWebTag<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- Web Trends tag end  -->



<title><s:property value="wCContext.storefrontId" /> - <s:text name="MSG.SWC.ORDR.APPROVALS.GENERIC.TABTITLE"/></title>

<!-- Facy Box (Lightbox/Modal Window -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
<!--<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.1.js"></script>
--><script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel.js"></script>

<script type="text/javascript">

	$(function() {
		$(".datepicker").datepicker({
			showOn: 'button',
						numberOfMonths: 1,
			buttonImage: '<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/theme/theme-1/calendar-icon.png',
			buttonImageOnly: true
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
		$("#shipToOrderSearch").fancybox({
    		'onStart' 	: function(){	    		
    			showShipToModalForOrderSearch();
    		},
    		'onClosed' : function() {    			
    			if(!$('#shipToSelectedOnShipToModal').val().trim()){
    				$("select#shipToSearchFieldName").attr('selectedIndex', 0);        				
    			}
      		},
			'autoDimensions'	: false,
			'width' 			: 800,
	 		'height' 			: 400  
		});
});
    function showShipToModalForOrderSearch() {
    	var customerContactId = $('#LoggedInUserIdForShipTo').val();
    	var getAssignedShipToURL = $('#getAssignedShipTosForSelectURL').val();
    	var includeShoppingForAndDefaultShipTo = "false";
    	$('#shipToSelectedOnShipToModal').val('');
    	/* Select Button click functionality */
    	selectShipToChanges = function selectShipToChanges(){
    		if (!$("input[name='selectedShipTo']:checked").val()) {
    			$('.shipToErrTxt').removeClass("notice").addClass("error");		
    			$('.shipToErrTxt').text("Please select a Ship-To Location.");		
    			return false;
    		}
    		var selectedShipCustomer = $("input[name='selectedShipTo']:checked").val();
    		$('#shipToSelectedOnShipToModal').val(selectedShipCustomer);
    		removeOptionAll();
        	var formattedShipTo = formatBillToShipToCustomer(selectedShipCustomer); 
    		appendOptionLast(selectedShipCustomer,formattedShipTo);
    		document.approvalList.shipToSearchFieldName1.value = selectedShipCustomer;
    		$.fancybox.close();    		
    	};
    	/* Cancel Button click functionality */
    	cancelShipToChanges = function cancelShipToChanges(){
    		$('#shipToSelectedOnShipToModal').val('');
    		$.fancybox.close();
    	};
    	showShiptos("Select Ship-To",	customerContactId,	getAssignedShipToURL,	includeShoppingForAndDefaultShipTo,	cancelShipToChanges, null, selectShipToChanges, null);
    }
</script>
<SCRIPT type="text/javascript">
	function clearFilters_onclick(){
		alwaysHideSearchField();
		Ext.fly('searchFieldName').dom.selectedIndex='0';
		//Ext.fly('search_searchTerm').dom.value='Enter Search Terms';
		//Ext.fly('accountSearchFieldName').dom.selectedIndex='0';
		Ext.fly('FromDate').dom.value='';
		Ext.fly('shipToSearchFieldName').dom.value='';
		Ext.fly('ToDate').dom.value='';
		//document.getElementById("shipToOrderSearch").innerHTML = '[Select]';
		//Ext.fly('order-ship-to').dom.selectedIndex='4';
	}

	function search_check(){
		if(Ext.fly('searchFieldName').dom.selectedIndex=='0' && 
			 Ext.fly('FromDate').dom.value=='' && Ext.fly('shipToSearchFieldName').dom.selectedIndex=='0' && 
			 	Ext.fly('ToDate').dom.value=='' )
			{
				//Ext.fly('search_searchTerm').dom.value='';
			}
			
	}
	
	
	function alwaysHideSearchField (){
		 var searchFieldValueDivId = document.getElementById("searchFieldValueDivId" ); 
		 searchFieldValueDivId.style.display = "none";
	}
	
	function hideSearchField(){
		//document.approvalList.searchFieldValue.value='';
		
		 var searchFieldInitinalValue = "- Select Search Criteria -";
		 var searchTermFeild = document.getElementById("searchFieldName" ); 
		 var searchFieldValueDivId = document.getElementById("searchFieldValueDivId" ); 
		 var searchFieldValueFld = document.getElementById("searchFieldValue" ); 
		 searchFieldValueFld.value='';
		
		 
		 var myindex  = searchTermFeild.selectedIndex
		 var SelValue = searchTermFeild.options[myindex].value
		
		 
		 //On selection of any item other than first item i.e. "- Select Search Criteria -" we need to display Search text box.
		 if ( myindex != 0 )
			 {	// alert ("Un Hide me ");
			 searchFieldValueDivId.style.display = "";
			 }
		 else
			 {	// alert (" --- Hiding ---- ");
			 searchFieldValueDivId.style.display = "none";
			 }
		
		 
		 return; 
	}

	
	
	function setDateFields(){
		if(Ext.fly('initialFromDateString').dom.value != '' && Ext.fly('initialFromDateString').dom.value != '')
		{
			Ext.fly('FromDate').dom.value = Ext.fly('initialFromDateString').dom.value;
			Ext.fly('ToDate').dom.value = Ext.fly('initialToDateString').dom.value ;
		}
		
		}

</SCRIPT>

<s:set name='_action' value='[0]'/>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="WT.ti" content='<s:text name="approvallist.title" />' />
	<title><s:text name="approvallist.title" /></title>

	<s:url id="approvalListPaginationURL" action="approvalList">
	    <s:param name="orderByAttribute" value="orderByAttribute"/>
	    <s:param name="orderDesc" value="orderDesc"/>
	    <s:param name="pageNumber" value="'{0}'"/>
	    <s:param name="searchFieldName" value="%{searchFieldName}"/>
	    <s:param name="searchFieldValue" value="%{searchFieldValue}"/>
	    <s:param name="pageSetToken" value="%{pageSetToken}"/>
	 </s:url>
  	<s:url id="returnUrl" action="approvalList">
	    <s:param name="orderByAttribute" value="orderByAttribute"/>
	    <s:param name="orderDesc" value="orderDesc"/>
	    <s:param name="pageNumber" value='%{pageNumber}'/>
	    <s:param name="searchFieldName" value="%{searchFieldName}"/>
	    <s:param name="searchFieldValue" value="%{searchFieldValue}"/>
	 </s:url>
	<script type="text/javascript">	 
	
	
	function openNotePanel(id, actionValue,orderHeaderKey){
		document.forms["approval"].elements["ReasonText"].value = "";
       
		DialogPanel.show(id);
		svg_classhandlers_decoratePage();
		/* if(actionValue == "Accept")
		     document.forms["approval"].elements["ApprovalAction"].value = "1300";
		 if(actionValue == "Reject")
		     document.forms["approval"].elements["ApprovalAction"].value = "1200";*/
		 document.forms["approval"].elements["OrderHeaderKey"].value = orderHeaderKey;
		}
	
	
	//added for XBT - 322
	
	function openNotePanelSetAction(actionValue){
		
		//end for XBT - 322
		
		 if(actionValue == "Accept"){
			 showProcessingIcon();
		     document.forms["approval"].elements["ApprovalAction"].value = "1300";
		     if(document.getElementById("ReasonText1")!=null && document.getElementById("ReasonText1").value==""){
			 		document.getElementById("ReasonText").value="Empty"; 
			 	}
			    else{
			    	 document.getElementById("ReasonText").value=document.getElementById("ReasonText1").value;
				}	
		 }    
		 if(actionValue == "Reject"){
		     document.forms["approval"].elements["ApprovalAction"].value = "1200";
		     if(document.getElementById("ReasonText1")!=null && document.getElementById("ReasonText1").value==""){
			 		document.getElementById("ReasonText").value="Empty";
			 	}
			    else{
			    	 document.getElementById("ReasonText").value=document.getElementById("ReasonText1").value;
				}
		   		//-- Web Trends tag start --
				writeMetaTag('DCSext.w_x_ord_reject','1');
				//-- Web Trends tag End --		
		 }    	
	     //submit it
		 document.forms["approval"].submit();
		}
	// Added for JIRA 2770
	function showShipTosOnSelect(){		
		if (document.getElementById("shipToSearchFieldName").selectedIndex=='1'){
			 $('#shipToOrderSearch').click();
			}
		else if(document.getElementById("shipToSearchFieldName").selectedIndex=='0'){
			document.getElementById("shipToSearchFieldName1").value="";
			}
		else{
			document.getElementById("shipToSearchFieldName1").value=document.getElementById("shipToSearchFieldName").value
			}
		return;
	}
	</script>
	
 <s:url id="approvalListSortURL" action="approvalList" >
    <s:param name="orderByAttribute" value="'{0}'"/>
    <s:param name="orderDesc" value="'{1}'"/>
    <s:param name="pageNumber" value="%{pageNumber}"/>
 </s:url>
 <s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
 <s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean' id='xpedxutil' />
 <s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='dateUtilBean'/>
 <s:set name="xpedxCustomerContactInfoBean" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getObjectFromCache("XPEDX_Customer_Contact_Info_Bean")' />
	<s:set name="xutil" value="#_action.getXMLUtils()"/>
	<!-- get the search result xml: getOutputDocument is an action method -->
	<s:set name='approvalListdoc' value="outputDocument" id='approvalListDoc'/>
	<s:set name='desc' value="OrderDesc" />
</head>

<body class="ext-gecko ext-gecko3">
		<div>
			<div class="loading-icon" style="display:none;"></div>
		</div>
<s:url id="getAssignedShipTosForSelectURLid" namespace="/common" action="getAssignedShipToCustomers" />
<s:hidden id="getAssignedShipTosForSelectURL" value="%{#getAssignedShipTosForSelectURLid}" />
<div class='x-hidden dialog-body ' id="shipToDivforordersearch">
	<div class="ship-container" id="ship-container">
    	<%-- dynamicaly populate data here  --%>
	</div>
</div>
    <div id="main-container">
        <div id="main">
        
<!-- begin header -->
   <s:action name="xpedxHeader" executeResult="true" namespace="/common" />
<!-- // header end -->
		<!-- begin tooltip boxes -->
		
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
				<p> <s:text name='MSG.SWC.ORDR.APPROVALS.INFO.INVSEARCH' /> </p>
			</div>
		</div>
		
		
		<!-- end tooltip boxes -->
		
		<div class="container content-container">
			<h1><s:text name="approvallist.title" /></h1>
                
                <!-- begin top section -->
                <div class="rounded-border top-section addmarginleft0">
                	<!-- begin content w/border -->
			<s:form name="approvalList" action="approvalList" id="approvalList" namespace="/order" method="POST">
			
			<fieldset class="x-corners mil-col-mil-div addmarginright5">
			<!-- text on border -->
			   <!--  <legend class="search-legend">Search Orders: Awaiting Approval</legend> -->
			    <legend class="search-legend">  <s:text name='MSG.SWC.ORDR.APPROVALS.GENERIC.LEGEND' /> </legend>
                        <!-- begin content-holding table -->
                        <table border="0px solid red " cellpadding="0" cellspacing="0" class="full-width line-spacing-tr" id="top-section">
								<tr>
													<td class="width-82">Search By:</td>
													<td>
														<s:select theme="simple"  name="searchFieldName" list="searchListNew" onchange="javascript:hideSearchField();"  id="searchFieldName"/>&nbsp; 
													<div id ="searchFieldValueDivId"  style="display:none;" > 
														<s:textfield name="searchFieldValue" id="searchFieldValue" cssClass="search-by-input-field" tabindex="1002" value="Enter Search Terms" size="29" theme="simple" />
													</div>	
													</td>
													<td colspan="2"></td>
								</tr>
								<tr>
													 <td>Ordered Date:</td>
													 <td> &nbsp;From&nbsp;&nbsp;
	                            		<s:hidden id="initialFromDateString" value="%{getInitialFromDateString()}" />
	                            		
	                                    <s:if test="#parameters.submittedTSFrom == null || #parameters.submittedTSFrom.length() == 0 " >
	                                    <s:textfield name='submittedTSFrom' theme="simple" size="15" cssClass='calendar-input-fields datepicker' id="FromDate"/>
	                                    </s:if>	
	                                     <s:else>
	                                    <s:textfield name='submittedTSFrom' theme="simple" size="15" cssClass='calendar-input-fields  datepicker' 
	                                    	value="%{#parameters.submittedTSFrom}" id="FromDate"/>
	                                    </s:else>	
	                                    	<span style="padding-left: 1.5em;"></span>
											&nbsp;To&nbsp;&nbsp;
												<s:hidden id="initialToDateString" value="%{getInitialToDateString()}" />
	                                   	<s:textfield name='submittedTSTo' theme="simple" size="15" cssClass='calendar-input-fields datepicker' value="%{#parameters.submittedTSTo}"  id="ToDate"/>
													 &nbsp; (mm/dd/yyyy)</td>
													 <td colspan="2"></td>
													 <!-- <td> Amount: </td>
													 <td> <input class=" x-input"/> to <input class=" x-input"/> </td> -->
								</tr>
								<tr><td style="width:85" colspan="2"><div id="errorDateDiv" style="color:red;"></div></td></tr>
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
						<s:select cssClass=" " name="shipToSearchFieldName" headerValue="All Ship-To(s)"
						 list="shipToSearchList" value="%{#parameters.shipToSearchFieldName}" id="shipToSearchFieldName" onchange="javascript:showShipTosOnSelect();"/>
						<s:hidden name="shipToSearchFieldName1" id="shipToSearchFieldName1" value="%{getShipToSearchFieldName()}" />
						<s:hidden name="shipToSelectedOnShipToModal" id="shipToSelectedOnShipToModal" value='' />
						<a href='#ship-container' id="shipToOrderSearch"></a></td>

						<td colspan="1">
							<input class="btn-gradient floatright addmarginright10" type="button" value="Search" onclick="search_check(); submit_approveOrderListForm();" />
							<input class="btn-neutral floatright addmarginright10" type="button" value="Clear" onclick="clearFilters_onclick();" />
						</td>
                        </tr>
                       
                        </table> <!-- end content-holding table -->
                        
                </fieldset><!-- end border content -->
                <!-- 	<div id="search-view-links">
									<a  href="#" id="view-all-invoices-btn"><span class="underlink">View Order History Reports </span><img src="../../images/icons/12x12_charcoal_help.png" alt="" title="Select from reports providing information on orders over the past 2 years." /></a>
									<a  href="#" id="view-order-history-btn"><span class="underlink">View All Invoices  </span><img src="../../images/icons/12x12_charcoal_help.png" alt="" title="Search all your invoices; you'll be taken to a new screen." /></a>
								</div> 	-->
		</s:form>
	    </div> <!-- end top section -->
	    <br/>
	    <!-- Begin mid-section -->
	    <div class="addmarginleft0"> <!-- Begin mid-section container -->
	    	
	       <div class="search-pagination-top">
	       		  <s:if test="%{totalNumberOfPages == 0 || totalNumberOfPages == 1}">Page&nbsp;&nbsp;<s:property value = "%{pageNumber}" /></s:if>
                  <s:if test="%{totalNumberOfPages>1}">
                  	Page
                  </s:if>
                  
                  	&nbsp;&nbsp;<swc:pagectl currentPage="%{pageNumber}" lastPage="%{totalNumberOfPages}" showFirstAndLast="False"
                 	urlSpec="%{#approvalListPaginationURL}"/>
			</div>
	    	
            <swc:sortctl sortField="%{orderByAttribute}"
		                  sortDirection="%{orderDesc}" down="Y" up="N"
		                  urlSpec="%{#approvalListSortURL}">			
	    	<table class="standard-table-width standard-table">
	    		
	    			<tr id="top-bar">
	    				<th style="min-width: 10em;">
						<swc:sortable  fieldname="%{'Extn_'+'ExtnWebConfNum'}">
						<span style="color:white" class="underlink">Web&nbsp;Confirmation </span></swc:sortable>
						</th>
	    				<th class="underlink" style="min-width: 9.5em;"><swc:sortable fieldname="%{'CustomerPONo'}"><span style="color:white" class="underlink"> PO # </span></swc:sortable></th>
	    				<th style="min-width: 6em;"><swc:sortable fieldname="%{'OrderDate'}"><span class="underlink" style="color:white">Ordered</span></swc:sortable> </th>
	    				<th style="min-width: 7em;"><swc:sortable fieldname="%{'Extn_'+'ExtnOrderedByName'}"><span class="underlink" style="color:white">Ordered&nbsp;By </span></swc:sortable></th>
	    				<th style="min-width: 10em;"><swc:sortable fieldname="%{'Extn_'+'ExtnShipToName'}"><span class="underlink" style="color:white">Ship-To</span></swc:sortable> </th>
	    				<th style="min-width: 8em;"><swc:sortable fieldname="%{'Extn_'+'ExtnTotalOrderValue'}"><span class="underlink" style="color:white">Amount </span></swc:sortable></th>
	    				<th style="min-width: 10em;" ><span class="underlink" style="color:white">Status </span></th>
	    			</tr>
	    		
                    <s:set name="parentOrderList" value="#util.getElements(#approvalListdoc, '//Page/Output/OrderList/Order')" />
	    		<tbody>
<!-- start -->
                        <s:iterator id='parentOrder' status='rowStatus' value='parentOrderList'>
							<s:set name="priceInfo"
								value='#parentOrder.getElementsByTagName("PriceInfo")' />
							<s:set name='shipToAddr' value='#xutil.getChildElement(#parentOrder,"PersonInfoShipTo")'/>
			            	<s:set name='addressLine1' value='%{#shipToAddr.getAttribute("AddressLine1")}'/>
			            	<s:set name='addressLine2' value='%{#shipToAddr.getAttribute("AddressLine2")}'/>
			            	<s:set name='addressLine3' value='%{#shipToAddr.getAttribute("AddressLine3")}'/>
			            	<s:set name='city' value='%{#shipToAddr.getAttribute("City")}'/>
			            	<s:set name='state' value='%{#shipToAddr.getAttribute("State")}'/>
			            	<s:set name='country' value='%{#shipToAddr.getAttribute("Country")}'/>
			            	<s:set name='zip' value='%{#shipToAddr.getAttribute("ZipCode")}'/>
	            			<s:set name='zip' value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedZipCode(#zip)"/>
			            	<s:set name="shipToId" value='#parentOrder.getAttribute("ShipToID")' />
							<s:set name='currencyCode'
								value='%{#priceInfo.item(0).getAttribute("Currency")}' />
							<s:set name='OrderExtn'
								value='#xutil.getChildElement(#parentOrder,"Extn")' />
							<s:set name='priceWithCurrency'
								value='#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, #OrderExtn.getAttribute("ExtnTotalOrderValue"))'/>
								
							<s:set name="orderDate"
								value='%{#dateUtilBean.formatDate(#parentOrder.getAttribute("OrderDate"),wCContext)}' />						
								
							<s:set name='shipToName'
								value='#OrderExtn.getAttribute("ExtnShipToName")' />
								
							<s:set name='webConfirmationNumber'
								value='#OrderExtn.getAttribute("ExtnWebConfNum")' />
							
							<s:set name='ohk'
								value='#parentOrder.getAttribute("OrderHeaderKey")' />
				
							<s:if test="#rowStatus.odd == true">
								<tr class="odd">
							</s:if>
							<s:else>
								<tr>
							</s:else>	
								<td>
								<s:url  id="orderDetailsURL" action="orderDetail"
									escapeAmp="false">
									<s:param name="orderHeaderKey"
										value='#ohk' />
									<s:param name="orderListReturnUrl" value='#returnUrl' />
								</s:url> 
								
								<span class="underlink">
								<s:a   href="%{orderDetailsURL}">
									<s:property value='#webConfirmationNumber' />
								</s:a>
								</span>
								</td>
								<td><s:property
									value='#parentOrder.getAttribute("CustomerPONo")' />
								</td>
					
								<td><s:property value='#orderDate' /></td>
					
								<td>
									<s:property value='#OrderExtn.getAttribute("ExtnOrderedByName")' />
								</td>
								
								<td>
										<s:if test='%{#shipToName!=null && #shipToName.length()>0}'>
										<s:property value='#shipToName' /> 
										<br />
										</s:if> 
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
										<s:property value='#state' />
									</s:if> 
									<s:if test='%{#zip!=null && #zip.length()>0}'>
										<s:property value='#zip' />						
									</s:if>
									<s:if test='%{#country!=null && #country.length()>0}'>
										<s:property value='#country' />
									</s:if>  
									<br /> 
									<s:if test='%{#shipToId!=null && #shipToId.length()>0}'>
										<s:property
											value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@formatBillToShipToCustomer(#shipToId)' />
									</s:if>
								
								<br />
								</td>
					
								<td>
									<s:if test='%{#xpedxCustomerContactInfoBean.getExtnViewPricesFlag() == "Y"}'>
										<s:set name="priceWithCurrencyTemp" value='%{#xpedxutil.formatPriceWithCurrencySymbol(wCContext, #currencyCode, "0")}' />
										<s:if test="%{#priceWithCurrency == #priceWithCurrencyTemp}">
											<span class="red bold"> <s:text name='MSG.SWC.ORDR.OM.INFO.TBD' /> </span>  
			                    		</s:if>
			                            <s:else>
											(<s:property value='#currencyCode' />)<s:property value='#priceWithCurrency' /> 
										</s:else>
									</s:if>
								</td>
					
								<td>
									<s:set name="isPendingApproval" value="%{#_action.isOrderOnHold(#parentOrder,'ORDER_LIMIT_APPROVAL')}" />
									<s:if test='#isPendingApproval'>
										<s:property value="#parentOrder.getAttribute('Status')" /> <s:text name='MSG.SWC.ORDR.NEEDSATTENTION.GENERIC.STATUSPENDING.PENDAPPROVAL' />
									</s:if>
									<s:else>
										<s:property value="#parentOrder.getAttribute('Status')" />
									</s:else>
									<br/>
									<input class="btn-neutral floatright addmarginright20" type="button" tabindex="91"
										value="Approve / Reject" onclick="openNotePanel('approvalNotesPanel', 'Accept','<s:property value="ohk"/>');" />
								</td>
					
							</tr>
						</s:iterator>							
<!-- end --> 
	    		</tbody>
	    	</table>
			</swc:sortctl>
	    	
			<div class="search-pagination-bottom">
			        <s:if test="%{totalNumberOfPages == 0 || totalNumberOfPages == 1}">Page&nbsp;&nbsp;<s:property value = "%{pageNumber}" /></s:if>
			        <s:if test="%{totalNumberOfPages>1}">Page</s:if>&nbsp;&nbsp;<swc:pagectl currentPage="%{pageNumber}" lastPage="%{totalNumberOfPages}" showFirstAndLast="False"
			       	urlSpec="%{#approvalListPaginationURL}"/>
			</div>
	    	
	    </div> <!-- end mid-section container -->
	    <!-- End mid section -->

	
 
        <!-- begin bottom section -->
        
        <!-- end bottom section -->
        	
	
	</div><!-- end second container div-->
        </div>
	<!-- begin footer -->
   <s:action name="xpedxFooter" executeResult="true" namespace="/common" />
<!-- // footer end -->
<!-- Added for EB-3642 Approval/Rejection Model Dailog pannel resize changes -->
		 <swc:dialogPanel title="" isModal="true" id="approvalNotesPanel" width="450"> 	
		<div>
			<div class="loading-icon" style="display:none;"></div>
 		</div>	
		<div  class="xpedx-light-box" id="" style="width:400px; height:150px;">	    			
			<h2> <s:text name='MSG.SWC.ORDR.PENDAPPROVALS.GENERIC.APPROVALREJECTCOMMENT' /> </h2>				    			
				<%--Start 3999 Changes Start --%><s:form id="approval" action="approvalAction" namespace="/order" validate="true" method="post">
					<s:textarea id="ReasonText1" name="ReasonText1" cols="69" rows="4" theme="simple" cssStyle="overflow:hidden;" onkeyup="restrictTextareaMaxLengthAlert(this,'255');"></s:textarea><%--Start 3999 Changes End --%>
					<s:hidden name="ReasonText" id="ReasonText" value="" />
					<s:hidden name="OrderHeaderKey" value="" />
					<s:hidden name="ApprovalAction" value=""/>
					<s:hidden name="#action.namespace" value="/order"/>
					<s:hidden id="actionName" name="#action.name" value="approval"/>
					<ul id="tool-bar" class="tool-bar-bottom" style="float:right">
						<li><a style="float:right;" class="grey-ui-btn" href="#" onclick="javascript:DialogPanel.hide('approvalNotesPanel'); return false;"><span>Cancel</span></a></li>
						<li><a style="float:right;" class="grey-ui-btn" href="#" onclick="javascript:openNotePanelSetAction('Reject');"><span>Reject</span></a></li>
						<li><a style="float:right;" class="green-ui-btn" href="#" onclick="javascript:openNotePanelSetAction('Accept');"><span>Approve</span></a></li>
						
					</ul>
				</s:form>
				</div>
				<div class="loading-wrap"  style="display:none;">
					<div class="load-modal" ></div>
				</div>
		</swc:dialogPanel> 
	
    </div><!-- end container  -->
    <div class="loading-wrap"  style="display:none;">
         <div class="load-modal" ></div>
    </div>
    
    <script type="text/javascript">
   
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
	function submit_approveOrderListForm()
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
		} 
		document.getElementById("errorDateDiv").innerHTML = '';
		//end of jira 3542 changes
		document.approvalList.submit();
	}
	
    </script>
     
</body>


</swc:html>
