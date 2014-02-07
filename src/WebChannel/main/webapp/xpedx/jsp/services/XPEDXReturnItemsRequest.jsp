<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<% request.setAttribute("isMergedCSSJS","true"); %>
<swc:html> 
<head>
<meta http-equiv="X-UA-Compatible" content="IE=8" />

<!-- BEGIN head-calls -->
<!-- Version 1.1 Updated 8-18-10 -->

<!-- BEGIN styles -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<![endif]-->
<!-- END styles -->

<!-- sterling 9.0 base  do not edit  javascript move all functions to js/global-xpedx-functions.js -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-returns-ui<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<%-- <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> --%>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt<s:property value='#wcUtil.xpedxBuildKey' />.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/swc<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<!-- sterling 9.0 base  do not edit  javascript move all functions to js/global-xpedx-functions.js -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- carousel scripts js   -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.core<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.tabs<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.shorten<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<%-- <script type="text/javascript" src="../modals/checkboxtree/jquery.checkboxtree.js"></script> --%>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<%-- <script type="text/javascript" src="../js/quick-add/jquery-ui.min.js"></script> --%>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/DD_roundies_0.0.2a-min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pseudofocus<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global-xpedx-functions<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>


<!-- Facy Box (Lightbox/Modal Window -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.1<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- Page Calls -->
<!-- END head-calls -->
<!-- <title>Return Request Confirmation</title> -->
<title> <s:property value="wCContext.storefrontId" /> - <s:text name="MSG.SWC.ORDR.RETN.GENERIC.TABTITLE"/> </title>

<script type="text/javascript">
function reasonChange(me)
{
	var v = $(me).val();
	var el = $(me).parents('table').find('textarea');
	if (v == 'Other Reason') {
		el.show();
		el.focus();
	}
	else
		el.hide();
}
function toggleFields (str, el)
{
	var shide = $(str);
	var sel = $(el);
	if (sel.is(':checked'))
	{
		shide.show();
		reasonChange(sel.parents('.middle-section').find('select'));
	}
	else
	{
		shide.hide();
		sel.parents('.middle-section').find('textarea').hide();
	}
	
	return true;
}

	/* Begin long desc. shortener */
$(document).ready(function(){
	$(document).pngFix();
	$('.prodlist ul li, #prodlist ul li').each(function() {
		var html = $(this).html();
		var shortHTML = html.substring(0, 35);
		if( html.length > shortHTML.length )
		{
			$(this).html(shortHTML);
			$(this).append('...');	
			$(this).attr('title', html );
		}
	});
$('.short-description').each(function() {
	var html = $(this).html();
	var shortHTML = html.substring(0, 90);
	if( html.length > shortHTML.length )
	{
		$(this).html(shortHTML);
		$(this).append('...');	
		$(this).attr('title', html );
	}
});
});

</script>

</head>
<s:set name='_action' value='[0]' />
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean'
	id='util' />
	<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean'
	id='xpedxUtilBean' />
<s:set name="xutil" value="XmlUtils" />
<s:set name='wcContext' value="wCContext" />
<s:set name='orderDetail' value='elementOrder' />
<s:set name="uomMap" value='#util.getUOMDescriptions(#wcContext,true)' />
<s:set name='OrderExtn'
	value='#xutil.getChildElement(#orderDetail,"Extn")' />
<s:set name='paymentMethods'
	value='#xutil.getChildElement(#orderDetail,"PaymentMethods")' />
<s:set name='paymentMethod'
	value='#xutil.getChildElement(#paymentMethods,"PaymentMethod")' />
<s:set name='displayAccountNumber'
	value='#paymentMethod.getAttribute("DisplayCustomerAccountNo")' />
<s:set name='webConfirmationNumber'
	value='#OrderExtn.getAttribute("ExtnWebConfNum")' />
<s:set name='dorderNo'
	value='#xutil.getAttribute(#orderDetail,"OrderNo")' />
<s:set name='legacyOrderNumber' value='#OrderExtn.getAttribute("ExtnLegacyOrderNo")'/>
<s:set name='duserName' value='#_action.getUserName()' />
<s:set name='drequestedName' value='#_action.getRequestedBy()' />
<s:set name='orderHeaderKeyValue'
	value='#orderDetail.getAttribute("OrderHeaderKey")' />
<s:url id="orderlistUrl" action="orderList" namespace="/order">
</s:url>
<s:set name='returnUrl' value='%{#orderlistUrl}' />
<s:url id="orderDetailsURL" action="orderDetail" namespace="/order">
	<s:param name="orderHeaderKey"
		value='#orderDetail.getAttribute("OrderHeaderKey")' />
	<s:param name="orderListReturnUrl" value='#returnUrl' />
</s:url>
<body class="ext-gecko ext-gecko3">
    <div id="main-container">
        <div id="main">

<!-- begin header -->
   <s:action name="xpedxHeader" executeResult="true" namespace="/common" />
<!-- end header -->

<s:set name='customerId' value="wCContext.customerId" />
<!-- added for jira 3245 -->
<s:set name="currentShipTo" value="#wcUtil.getShipToAdress(getWCContext().getCustomerId(),getWCContext().getStorefrontId())" />
<s:set name="shipToCustomerDisplayStr" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@formatBillToShipToCustomer(#customerId)" />
<%-- <s:set name="shipToCustomerDisplayStr" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@formatBillToShipToCustomer(#customerId)" /> --%>

            <div class="container return-request"> 
               <!-- breadcrumb -->
               <div id="breadcumbs-list-name">
               	<%-- <p><span class="page-title"> Return Request </span></p> --%>
               	<p><span class="page-title"> <s:text name="MSG.SWC.ORDR.RETN.GENERIC.PGTITLE"/> </span></p>
               </div>
               <!-- end breadcrumb -->
			<s:form validate="true" id="returnItemForm"
				name="returnItemForm" namespace="/xpedx/services" method="post"
				action="returnItemsSendMailRequest">

				<s:hidden name="orderHeaderKey" value='%{#orderHeaderKeyValue}'></s:hidden>
                
               <!-- begin top section -->
               <div class="rounded-border ">
			<!-- begin content w/border -->
				<fieldset class="x-corners mil-col-mil-div">
				<!-- text on border -->
			    <legend>Order #: <s:if test='#legacyOrderNumber!=""'><s:property value='@com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils@getFormattedOrderNumber(#OrderExtn)'/></s:if></legend>
				    <!-- begin content-holding table -->
				    <table border="0px solid red" cellpadding="0" cellspacing="0" class="full-width no-border line-spacing-tr" id="top-section" style="width:100%;">
					    <tr>
							<td style="width: 10em;">Web Confirmation: </td>
							<td style="width: 335px;"><s:property value='%{webConfirmationNumber}' /></td>
							<td>&nbsp; <!-- this row intentionally left blank --></td>
					    </tr>

					    <tr class="shipto-row">
							<td>Ship-To: </td>
							<td><s:property value='#shipToCustomerDisplayStr'/></td>
						</tr>
						<tr class="shipto-row">
							<td></td>
							<td><s:if test="%{currentShipTo.firstName!='' || currentShipTo.middleName!='' || currentShipTo.lastName!=''}">
								<s:if test="%{#currentShipTo.firstName!='' && #currentShipTo.firstName!= null}">
									<s:property value='%{#currentShipTo.firstName}' /> <s:property value=',' />
								</s:if> 
								<s:if test="%{#currentShipTo.middleName!='' && #currentShipTo.middleName!= null}">
									<s:property value='%{#currentShipTo.middleName}' /> <s:property value=',' />
								</s:if> 
								<s:if test="%{#currentShipTo.lastName!='' && #currentShipTo.lastName!= null}">
									<s:property value='%{#currentShipTo.lastName}' />
								</s:if>
								</s:if>
							</td>
						</tr>
						<tr class="shipto-row">
							<td></td>
							<td><s:if test="%{#currentShipTo.getOrganizationName()!='' && #currentShipTo.getOrganizationName()!= null}">
									<s:property value='%{#currentShipTo.getOrganizationName()}' />
								</s:if>
								</td>
						</tr>
						<tr class="shipto-row">
							<td></td>
							<td><s:if test="%{#currentShipTo.LocationID!='' && #currentShipTo.LocationID!= null}">
									Local ID: <s:property value='%{#currentShipTo.locationID}' />
								</s:if>
							</td>
						</tr>
							<s:iterator value='%{#currentShipTo.addressList}' id='adressLine'>
								<tr class="shipto-row">
									<td></td>
									<td><s:property value='adressLine' />
									</td>
								</tr>
							</s:iterator>
						<tr class="shipto-row">
							<td></td>
							<td><s:if test="%{#currentShipTo.city!=''}">
									<s:property value='%{#currentShipTo.city}' />,<%-- &nbsp;--%>
								</s:if>
								<s:if test="%{#currentShipTo.state!=''}">
									<s:property value="%{#currentShipTo.state}" />&nbsp;
								</s:if>
								<s:if test="%{#currentShipTo.zipCode!=''}">
									<s:property value="%{@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedZipCode(#currentShipTo.zipCode)}" />&nbsp;
								</s:if>	
								<s:if test="%{#currentShipTo.country!=''}"> 
									<s:property value="%{#currentShipTo.country}" />
								</s:if>
							</td>
					  </tr>			
					    <tr>
							<td>Requested By: </td>
							<td colspan="2"><s:property value='%{drequestedName}' />, (<s:property value='%{duserName}' />)</td>
							<td>&nbsp; <!-- this row intentionally left blank --></td>
					    </tr>
					    <tr>
						    <td>Phone:</td>
						    <td><s:textfield id="returnsph" name="returnsph"
									onkeyup="formatPhone('Telephone No.',returnItemForm.returnsph);" maxlength="10" ></s:textfield></td>
							<td rowspan="2"> 					    <!-- begin right side comment box with label -->
								    <div class="float-right return-comment-div">
									    <div class="label"> <label for="">Comments: </label></div>
									   	<s:textarea name="notes" cols="56" rows="3" id="textfield3" cssStyle="margin-left:100px"></s:textarea>
								    </div>
					    	<!-- end right side comment box with label --> 
					    	</td>
					    </tr>
					    <tr>
							<td>Email Address:</td>
							<td><s:textfield name="returnsEmail" id="returnsEmail" maxlength="255" cssClass="email-input"></s:textfield ></td>
							<td>&nbsp; <!-- this row intentionally left blank --></td>
					    </tr>
					    </table>
					    <!-- end content-holding table -->
					    
					    <div class="clearall">&nbsp;</div>
					    
                </fieldset><!-- end border content -->
	    </div> <!-- end top section -->
	    
	    <!-- begin middle section -->
	    <div class="check-items-notice">Check Item(s) to Return</div>
		
		<s:iterator value="#_action.getMajorLineElements()" id='orderLine'
				status='orderLineStatIndex'>
				<s:set name='item'
					value='#xutil.getChildElement(#orderLine, "Item")' />
				<s:set name='itemdetails'
					value='#xutil.getChildElement(#orderLine, "ItemDetails")' />
				<s:set name='xpedxOrderLineExtn' 
					value='#xutil.getChildElement(#itemdetails,"Extn")'/>
				<s:set name='certFlag'
				value="#xutil.getAttribute(#xpedxOrderLineExtn, 'ExtnCert')" />
				<s:set name='orderLineKey'
					value='#xutil.getAttribute(#orderLine,"OrderLineKey")' />
				<s:set name='itemDetails'
					value='#util.getElement(#orderLine, "ItemDetails")' />
				<s:set name='primaryInfo'
					value='#util.getElement(#itemDetails, "PrimaryInformation")' />
				<s:set name='imageLocation'
					value='#primaryInfo.getAttribute("ImageLocation")' />				
				<s:set name='imageID' value='#primaryInfo.getAttribute("ImageID")' />
			  	<s:if test='#_action.getShortDescriptionForOrderLine(#orderLine) == #item.getAttribute("ItemID")'>
					<s:set name='showDesc' value='#_action.getDescriptionForOrderLine(#orderLine)'/>
			  	</s:if>
			  	<s:else>
					<s:set name='showDesc' value='#_action.getShortDescriptionForOrderLine(#orderLine)'/>
		 	 	</s:else>
			<s:if test="#orderLineStatIndex.index == 0">
			<div id="first-item" class="middle-section rounded-border rr-border" >
			</s:if>
			<s:else>
			<div id="" class="middle-section rounded-border rr-border" >
			</s:else>
		    <div style="">
		    	<table class="full-width">
		    		<tr> 
		    			<td rowspan="9" valign="top" class="ie-fix-td"> 
			    			<!--  begin short desc -->
							<s:checkbox id="selectedItems[%{#orderLineStatIndex.index}]"
						name="selectedItems[%{#orderLineStatIndex.index}]" fieldValue="%{#orderLineKey}"  onclick="return toggleFields('.disappear%{#orderLineStatIndex.index}', this);" />
							</td>
			    			<td rowspan="4" width="305" valign="top" class="ie-fix-td"> 
						<span class="short-description"><s:property	value='#showDesc' /></span>
						<!-- end short desc -->
						
						<!--  begin long desc -->
						<s:url id='detailURL' namespace='/catalog'
							action='itemDetails.action'>
							<s:param name='itemID'>
								<s:property value='#item.getAttribute("ItemID")' />
							</s:param>
							<s:param name='unitOfMeasure'>
								<s:property value='#item.getAttribute("UnitOfMeasure")' />
							</s:param>
							<s:param name='_r_url_' value='%{orderDetailsURL}' />
						</s:url> 
						<s:a href="%{#detailURL}">	
							<s:if test='#_action.getShortDescriptionForOrderLine(#orderLine) != #item.getAttribute("ItemID")'>
							<ul class="prodlist" style="margin-left:-30px;max-height:83px"><s:property value='#_action.getDescriptionForOrderLine(#orderLine)' escape="false"/></ul>
							</s:if>
						</s:a>
						<!--  end long desc -->
					
					</td>
		    			<td width="20" class="text-right"><div class="qty-height">Qty:</div></td>
		    			<td width="80" class="text-right table-qty-value-td">
					<s:set
					name='orderLineTranQuantity'
					value='#xutil.getChildElement(#orderLine, "OrderLineTranQuantity")' />
				<%-- <s:property value='#util.formatQuantity(wCContext, #orderLineTranQuantity.getAttribute("OrderedQty"))' /> --%>
				 <s:property value='#xpedxUtilBean.formatQuantityWithTrimmedDecimals(wCContext, #orderLineTranQuantity.getAttribute("OrderedQty"))' /> 
					
				<s:hidden id="orderLineQuantity[%{#orderLineStatIndex.index}]" name="orderLineQuantity[%{#orderLineStatIndex.index}]" 
				value='%{#util.formatQuantity(wCContext, #orderLineTranQuantity.getAttribute("OrderedQty"))}'/>
						</td>
		    			<td width="70" class="text-left table-uom-td "><s:set name="uom"
					value='#xutil.getAttribute(#item, "UnitOfMeasure")' /> <s:property
					value='#uomMap.get(#uom)' /></td>
		    			<td width="80" class="text-right return-qty-label-td "> <span class="disappear<s:property value='%{#orderLineStatIndex.index}' /> disappear"> Qty to Return:</span></td>
		    			<td width="155">
						<s:textfield cssClass="disappear%{#orderLineStatIndex.index} disappear" 
					name="returnsqty[%{#orderLineStatIndex.index}]"
					id="returnsqty[%{#orderLineStatIndex.index}]"
					maxlength="7" title="Qty to Return"
					tabindex="%{#itemPanelStartTabIndex+#tabIndexCount}" onkeyup="javascript:isValidQuantityRemoveAlpha(this,event);"></s:textfield>
						</td>
		    			<td  width="175" class="text-right table-return-input-td ">
		    			
						<s:autocompleter cssClass="disappear%{#orderLineStatIndex.index} disappear"
					name="reasonreturn[%{#orderLineStatIndex.index}]"
					id="reasonreturn[%{#orderLineStatIndex.index}]"
					onchange="reasonChange(this);"
					headerValue="- Select Reason to Return -" headerKey="1"
					list="{'Order Cancelled','Damaged Goods','Wrong Item - Filled','Wrong Item - Ordered','Invalid Quantity','Other Reason'}" theme="simple" /> 
					<s:div
					id="strdiv%{#orderLineStatIndex.index}"
					cssStyle="display:none;  width:170px; margin:8px; text-align: right;">
					<s:textarea name="othertext[%{#orderLineStatIndex.index}]"
						id="othertext[%{#orderLineStatIndex.index}]"
						cssClass="returns-details" cssStyle="width:143px;" cols="15" rows="5"></s:textarea></s:div>						
						</td>
		    		</tr>
		    		<tr>
		    			<!-- rowspan covers this td -->
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td class="text-right" colspan="2" rowspan="6"><textarea name="reason2" style="width:333px;" class="disappear" rows="4" cols="35"></textarea> </td>
		    		</tr>
				<tr>
		    			<!-- rowspan covers this td -->
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<!-- rowspan covers this td -->
				</tr>				
				<tr>
		    			<!-- rowspan covers this td -->
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<!-- rowspan covers this td -->
				</tr>
				<tr>
		    			<td> &nbsp; </td>
		    			<!--  <td rowspan="5" class="long-desc"> 
							bb1
						</td>-->
						<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<!-- rowspan covers this td -->
				</tr>
				<tr>
		    			<td> &nbsp; </td>
		    			<!-- rowspan covers this td -->
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<!-- rowspan covers this td -->
				</tr>
				<tr>
   			  			<td><s:property value="wCContext.storefrontId" />  Item #: <s:property value='#item.getAttribute("ItemID")' />
					<s:if test='%{#certFlag=="Y"}'>
						<!-- <img border="none"  src="/swc/xpedx/images/catalog/green-e-logo_small.png" alt="" /> --> 
						 <img border="none"  src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/catalog/green-e-logo_small.png" alt="" style="margin-left:0px; display: inline;"/>
					</s:if>
					  </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<!-- rowspan covers this td -->
				</tr>
				<tr>
		    			<td> Mfg. Item #: <s:property value='#xutil.getAttribute(#primaryInfo,"ManufacturerItem")' />  </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
				</tr>
				<tr>
		    			<td> &nbsp; </td>
		    			<!-- rowspan covers this td -->
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
				</tr>
				<tr>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
				</tr>
				<tr>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
		    			<td> &nbsp; </td>
				</tr>
		    	</table>     	    	
		    </div><!--end right-section -->
	</div><!-- end box section - "div class="middle-section rounded-border rr-border"" -->
				
		</s:iterator>	



		
		
	<!-- begin bottom section -->
	<div class="float-right bottom-section">
			<s:a cssClass="grey-ui-btn" cssStyle="margin-right: 10px;" href="%{orderDetailsURL}">
			<span>Cancel</span>
		</s:a>
		&nbsp;&nbsp;&nbsp;
			<s:a
			onclick="return check('%{#_action.getMajorLineElements().size()}');"
			href="#" cssClass="orange-ui-btn">
			<span>Submit Request</span>
		</s:a>
	</div>
	<!-- end bottom section -->
	   </s:form>
	</div><!-- end second container div-->
   </div><!-- end main -->


<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
</body>

</swc:html>
