<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<%
  		request.setAttribute("isMergedCSSJS","true");
  	  %>
<s:set name='_action' value='[0]' />
<s:bean	name='com.sterlingcommerce.xpedx.webchannel.common.XPEDXSCXmlUtils'	id='xutil' />
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />
<s:bean	name='com.sterlingcommerce.webchannel.catalog.utils.CatalogUtilBean' id='catalogUtil' />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean' id='xpedxutil' />
<s:set name='scuicontext' value="uiContext" />
<s:set name="isEditOrderHeaderKey" value ="%{#_action.getWCContext().getSCUIContext().getSession().getAttribute(@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@EDITED_ORDER_HEADER_KEY)}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<swc:html isXhtml="true">
<head>
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<meta name="DCSext.w_x_sc_count" content="1"/>
<meta name="DCSext.w_x_itemtype" content="<s:property value='%{#session.itemType}' />" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-2014<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/ORDERS<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
<!--[if IE]>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/IE<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/ADMIN<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<![endif]-->

<title><s:property value="wCContext.storefrontId" /> - <s:property value="wCContext.storefrontId" /> Product Details</title>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-ext-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/common/XPEDXUtils<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-jquery-headder<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/xpedx-header<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<s:include value="../order/XPEDXRefreshMiniCart.jsp"/>
<!-- Web Trends tag start -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/webtrends/displayWebTag<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/PriceAndAvailability<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<!-- Web Trends tag end  -->
<script type="text/javascript">
var defaultUOM;
	$(document).ready(function() {
		updateUOMFields();
		var requestedUom = $('#selectedUOM').val();
		var baseUom = $('#unitOfMeasure').val();
		defaultUOM = $('#selectedUOM').val();

		callPnA(baseUom);
		//getPriceAndAvailabilityForItems([$('#itemID').val()]);
	});

function callPnA(requestedUom)
{
	var itemId = $('#itemID').val();
	var Quantity = $('#qtyBox').val();
	var baseUom = $('#unitOfMeasure').val();
	var prodMweight = $('#prodMweight').val();
	var pricingUOMConvFactor = $('#pricingUOMConvFactor').val();
	pandaByAjax(itemId,requestedUom,Quantity, baseUom, prodMweight, pricingUOMConvFactor);
}
function callPnAfromLink(requestedUom){
	var itemId = $('#itemID').val();
	var Quantity = $('#qtyBox').val();
	var baseUom = $('#unitOfMeasure').val();
	var prodMweight = $('#prodMweight').val();
	var pricingUOMConvFactor = $('#pricingUOMConvFactor').val();
	pandaByAjaxFromLink(itemId,requestedUom,Quantity, baseUom, prodMweight, pricingUOMConvFactor);
}

function updateUOMFields() {
   var uomvalue = $('#itemUOMsSelect').val();
   $('#selectedUOM').val(uomvalue);
   $('#uomConvFactor').val($('#convF_'+uomvalue).val());
}
var myMask;
function updatePandAfromLink(){
	/*Web Trends tag start*/ 
	if(document.getElementById("webtrendItemType")!=null){
		var itemType=document.getElementById("webtrendItemType").value;	
		writeMetaTag("DCSext.w_x_itemtype",itemType);
	}	
		/*Web Trends tag end*/
	//added for jira 3974
	var errorMessageDiv = document.getElementById("errorMessageDiv");
	if(errorMessageDiv != null && errorMessageDiv != undefined)
		errorMessageDiv.innerHTML='';
	
	var waitMsg = Ext.Msg.wait("Processing...");
	myMask = new Ext.LoadMask(Ext.getBody(), {msg:waitMsg});
	myMask.show();
	try
	{
		
	var UOMelement = document.getElementById("itemUOMsSelect");
	var uomvalue = UOMelement.options[UOMelement.selectedIndex].value;	
	callPnAfromLink(uomvalue);
	}
	catch (err) {
		var errorMessageDiv = document.getElementById("errorMessageDiv");
		if(errorMessageDiv != null && errorMessageDiv != undefined)
			errorMessageDiv.innerHTML='<h5 align="center"><b><font color="red">Could not get the pricing details for this Particular Item at the moment. Please try again Later</font></b></h5>';
		Ext.Msg.hide();
		myMask.hide();
	}
	
}
function pandaByAjaxFromLink(itemId,reqUom,Qty,baseUom,prodMweight,pricingUOMConvFactor,isOrderData){
	if(isOrderData == undefined || isOrderData == null)
	{
		isOrderData='false';
	}
	
	if(itemId == null || itemId == "null" || itemId == "") {
		Ext.Msg.hide();
	        myMask.hide();
		return;
	}
	if(reqUom == null || reqUom == "null" || reqUom == "") {
		reqUom = $('#selectedUOM').val();
	}
	var Qty=$('#qtyBox').val();
	var qtyTextBox=Qty;
	
	if(Qty =='')
	{	
		var uomConvFactor;
		var orderMul = $('#OrderMultiple').val();
		var conversionFactor =  $('#uomConvFactor').val();
		if(Qty == null || Qty == "null" || Qty == "") {
			reqUom =   $('#selectedUOM').val();
			if(orderMul != null && orderMul.value != 0 && uomConvFactor != 0 && conversionFactor != null ){
				if(uomConvFactor == 1){
					Qty = orderMul;
				}
				else if(uomConvFactor <= orderMul.value){
					if((orderMul.value % uomConvFactor)==0){
						Qty = orderMul.value / uomConvFactor;
					}
					else{
						Qty = 1;
					}
				}
				else{//if conversionFactor is greater than OrderMul irrespective of the moduloOf(conversionFactor,OrderMul) is a whole number / decimal result we set the Qty to 1
					Qty=1;
				}
			}
		}
	}	
	var itemAvailDiv = document.getElementById("tabs-1");
	if(Qty=='0')
	{	
		
		document.getElementById("qtyBox").style.borderColor="#FF0000";
		document.getElementById("qtyBox").focus();
		document.getElementById("errorMsgForQty").innerHTML  = "Please enter a valid quantity and try again.";
  		document.getElementById("errorMsgForQty").style.display = "inline-block"; 
  		document.getElementById("errorMsgForQty").setAttribute("class", "error");
		document.getElementById("Qty_Check_Flag").value = true;
		document.getElementById("qtyBox").value = "";
		itemAvailDiv.style.display = "none"; 
		Ext.Msg.hide();
	    	myMask.hide();
	    return;
	}	
	priceCheck = true;
	var Category = $('#catagory').val();
	var customerUom = $('#custUOM').val()
	var url = $('#xpedxItemDetailsPandAURLid').val();
	var validationSuccess = validateOrderMultiple();
 	if(validationSuccess==false){
		Ext.Msg.hide();
	    	myMask.hide();
		return;
	}		
	else{
	Ext.Ajax.request({
       	url:url,
        params: {
			itemID 	: itemId,
	       	requestedUOM : reqUom,
	       	pnaRequestedQty	: Qty,
	       	qtyTextBox : qtyTextBox,
	       	baseUOM	: baseUom,
	       	prodMweight : prodMweight,
	       	pricingUOMConvFactor : pricingUOMConvFactor,
	       	validateOrderMul : validationSuccess,
	       	Category : Category,
	       	isOrderData :isOrderData,
	       	customerUOM : customerUom
		},      	
	   	success: function (response, request){
	   		if(response.responseText.indexOf('Sign In</span></a>') != -1 && response.responseText.indexOf('signId') != -1){
	   			window.location.reload(true);
	   			Ext.Msg.hide();
				myMask.hide();
				return;
	   		}
			document.getElementById("priceAndAvailabilityAjax").innerHTML = response.responseText;
			Ext.Msg.hide();
			myMask.hide();
			//-- Web Trends tag start --
			var responseText = response.responseText;
			writeWebtrendTag(responseText);
			//-- Web Trends tag end --
		},
		failure: function (response, request){
			Ext.Msg.hide();
			myMask.hide(); 
			var errorMessageDiv = document.getElementById("errorMessageDiv");
			if(errorMessageDiv != null && errorMessageDiv != undefined)
				errorMessageDiv.innerHTML='<h5 align="center"><b><font color="red">Could not get the pricing details for this Particular Item at the moment. Please try again Later</font></b></h5>';
        }
		
	});
  } 
 }
function pandaByAjax(itemId,reqUom,Qty,baseUom,prodMweight,pricingUOMConvFactor) {
	if(itemId == null || itemId == "null" || itemId == "") {
		return;
	}
	if(reqUom == null || reqUom == "null" || reqUom == "") {
		reqUom = $('#selectedUOM').val();
	}
	// added for  Jira 2101 to get the PnA results based on the selected UOM on page load
	var uomConvFactor;
	var orderMul = $('#OrderMultiple').val();
	var conversionFactor = document.getElementById("uomConvFactor");
	if(conversionFactor!=null && conversionFactor!=undefined){
		 uomConvFactor = document.getElementById("uomConvFactor").value;
	}
	var qtyTextBox=Qty;
	if(Qty == null || Qty == "null" || Qty == "") {
		reqUom = $('#selectedUOM').val();
		if(orderMul != null && orderMul.value != 0 && uomConvFactor != 0 && conversionFactor != null ){
			if(uomConvFactor == 1){
				Qty = orderMul;
			}
			else if(uomConvFactor <= orderMul.value){
					if((orderMul.value % uomConvFactor)==0){
						Qty = orderMul.value / uomConvFactor;
					}
					else{
						Qty = 1;
					}
			}
			else{//if conversionFactor is greater than OrderMul irrespective of the moduloOf(conversionFactor,OrderMul) is a whole number / decimal result we set the Qty to 1
				Qty=1;
			}
		}
	}
	priceCheck = true;
	var Category = $('#catagory').val();
	var customerUom = $('#custUOM').val()
	var url = $('#xpedxItemDetailsPandAURLid').val();
	var validationSuccess = validateOrderMultiple();
	Ext.Ajax.request({
       	url:url,
        params: {
			itemID 	: itemId,
	       	requestedUOM : reqUom,
	       	pnaRequestedQty	: Qty,
	       	qtyTextBox : qtyTextBox,
	       	baseUOM	: baseUom,
	       	prodMweight : prodMweight,
	       	pricingUOMConvFactor : pricingUOMConvFactor,
	       	validateOrderMul : validationSuccess,
	       	Category : Category,
	       	customerUOM : customerUom
		},      	
	   	success: function (response, request){
			document.getElementById("priceAndAvailabilityAjax").innerHTML = response.responseText;
			Ext.Msg.hide();
			//-- Web Trends tag start --
			var responseText = response.responseText;
			writeWebtrendTag(responseText);
			//-- Web Trends tag end --
		}
	});
}
function resetQuantityErrorMessage()
{
		var divId='errorMsgForQty';
		var divVal=document.getElementById(divId);
		divVal.innerHTML='';
		document.getElementById("qtyBox").style.bordercolor="";

}
function validateOrderMultiple() {
	resetQuantityErrorMessage();
	var Qty = document.getElementById("qtyBox");
	if(Qty.value == "" || Qty.value == null || Qty.value == '0'){
	}
	var UOM = document.getElementById("itemUOMsSelect");
	var OrdMultiple = document.getElementById("OrderMultiple");
	var uomCF = 1;
	if(OrdMultiple != null && OrdMultiple!=undefined && OrdMultiple.value != 1){
		var selectedUOM = UOM.options[UOM.selectedIndex].text;
		var selectedUOMQty = selectedUOM.split(" ");
			if(selectedUOMQty.length == 2){
				var splitUOMQty = selectedUOMQty[1];
				var splitUOMQty1 = splitUOMQty.split("(");
				var splitUOMQty2 = splitUOMQty1[1];
				splitUOMQty2 = splitUOMQty2.split(")");
				var uomConvFactor = splitUOMQty2[0];
					if(uomConvFactor!=null && uomConvFactor!=undefined){
						uomCF = uomConvFactor;
				}
		}
		else{
			var selectedUOM = document.getElementById("selectedUOM");
			var uomConvFactor = document.getElementById("uomConvFactor");
				if(uomConvFactor!=null && uomConvFactor!=undefined){
					uomCF = uomConvFactor.value;
			}
		}
	}
	else{
			var selectedUOM = document.getElementById("selectedUOM");
			var uomConvFactor = document.getElementById("uomConvFactor");
				if(uomConvFactor!=null && uomConvFactor!=undefined){
					uomCF = uomConvFactor.value;
			}
		}
	
	var totalQty =  uomCF * Qty.value;	
	if(OrdMultiple!=null && OrdMultiple!=undefined && OrdMultiple.value!=0){
		var ordMul = totalQty % OrdMultiple.value;
		var myMessageDiv = document.getElementById("errorMsgForQty");	
		if (OrdMultiple.value > 1){
			if (priceCheck == true){
			      myMessageDiv.innerHTML = "<s:text name='MSG.SWC.CART.ADDTOCART.ERROR.ORDRMULTIPLES' /> " + addComma(OrdMultiple.value) + " <s:property value='#baseUOMDesc'></s:property>";	            
         		      myMessageDiv.style.display ="inline-block";
          		      myMessageDiv.setAttribute("class", "notice");
			}
			else{
				myMessageDiv.innerHTML = "";
			}
		}
	}
	return true;
}

function qtyInputCheck(component){
	var qtyCheckFlag = $('#Qty_Check_Flag').val();
	if(qtyCheckFlag=="true"){
		if(component.value==""){
    		component.style.borderColor="#FF0000";
        	document.getElementById('errorMsgForQty').style.display = "inline-block";
    	}
		else{
    		component.style.borderColor="";
	    }
	}
}
</script>
</head>
<body onload="document.productDetailForm.qtyBox.focus();" class="ext-gecko ext-gecko3">

<s:hidden name="webtrendItemType" id="webtrendItemType" value="%{#session.itemType}"/>
<s:url id="xpedxItemDetailsPandAURL" namespace="/catalog" action="xpedxItemDetailsPandA"/>
<s:hidden name="xpedxItemDetailsPandAURLid" id="xpedxItemDetailsPandAURLid" value="%{#xpedxItemDetailsPandAURL}"/>
<s:url id="getPriceAndAvailabilityForItemsURLid" action="getPriceAndAvailabilityForItems" namespace="/catalog" />
<s:hidden id="getPriceAndAvailabilityForItemsURL" value="%{#getPriceAndAvailabilityForItemsURLid}" />
<s:hidden name="catagory" id="catagory" value="%{#_action.getCatagory()}" />
<s:hidden id="custUOM" name="custUOM" value="%{#_action.getCustomerUOM()}" />
	<s:if test='%{#_action.getWCContext().isGuestUser() == true}'>
		<s:include value='XPEDXAnonItemDetails.jsp' />
	</s:if>
	<s:else>
	<s:set name="itemListElem" value="itemListElem" />
	<s:if test="%{null != #xutil.getChildElement(#itemListElem, 'Item')}">
		<s:set name="itemElem" value='#xutil.getChildElement(#itemListElem,"Item")' />
		
		<s:set name='itemID' value='#xutil.getAttribute(#itemElem,"ItemID")' />
		<s:set name='unitOfMeasure'	value='#xutil.getAttribute(#itemElem,"UnitOfMeasure")' />
		<s:set name="prodMweight" value="%{#_action.getProdMweight()}"/>
		<s:set name="pricingUOMConvFactor" value="%{#_action.getPricingUOMConvFactor()}"/>
		<s:set name="pricingUOMConvFactor" value="%{#_action.getPricingUOMConvFactor()}"/>
		
		<s:hidden name="itemID" id="itemID" value="%{#itemID}" />
		<s:hidden id="unitOfMeasure" name="unitOfMeasure" value="%{#unitOfMeasure}" />	
		<s:hidden id="prodMweight" name="prodMweight" value="%{#prodMweight}" />
		<s:hidden id="pricingUOMConvFactor" name="pricingUOMConvFactor" value="%{#pricingUOMConvFactor}" />
			
		<s:set name="itemElemExtn"	value='#xutil.getChildElement(#itemElem,"Extn")' />
		<s:set name='certFlag'	value="#xutil.getAttribute(#itemElemExtn, 'ExtnCert')" />
		<s:set name="primaryInfoElem" value='#xutil.getChildElement(#itemElem,"PrimaryInformation")' />
		<s:set name="itemAssets" value='#xutil.getChildElement(#itemElem,"AssetList")' />
		<s:set name="itemMainImages" value='#catalogUtil.getAssetList(#itemAssets,"ITEM_IMAGE_1")' /> 
		<s:set name='pImg' value='%{#imageLocation+"/"+#primaryInfoElem.getAttribute("ImageID")}' />
		<s:if test='%{#pImg=="/"}'>
			<s:set name='pImg' value='%{"/xpedx/images/INF_150x150.jpg"}' />
		</s:if>
		<s:set name="isStocked" value="isStocked" />
		<s:set name="orderMultiple" value="orderMultiple" />
		<s:set name='showCurrencySymbol' value='true' />
		<s:set name='currency' value='#xutil.getAttribute(#itemListElem,"Currency")' />
		<s:if test='%{#kitCode == "BUNDLE" }'>
			<s:set name='price'	value='#xutil.getAttribute(#computedPrice,"BundleTotal")' />
		</s:if>
		<s:else>
			<s:set name='price'	value='#xutil.getAttribute(#computedPrice,"UnitPrice")' />
		</s:else>
		 <s:if test="displayPriceForUoms.size() > 0">
			<s:set name='price' value='%{displayPriceForUoms.get(2)}' />
			<s:set name='formattedUnitprice' value='#xpedxutil.formatPriceWithCurrencySymbol(#scuicontext,#currency,#price,#showCurrencySymbol)' />
		</s:if> 
			
	</s:if>
		<div id="main-container">
			<div id="main">
				<s:action name="xpedxHeader" executeResult="true" namespace="/common" >
					<s:param name='shipToBanner' value="%{'true'}" />
				</s:action> 
				<div class="container content-container detail-view" id="containerId">
					<h1 style="font-weight:bold; "><s:property	value='#xutil.getAttribute(#primaryInfoElem,"ShortDescription")' /></h1>
					<div id="printButton" class="print-ico-xpedx underlink"> <img src='%{"/xpedx/images/common/print-icon.gif"}'alt="Print Page" height="15" width="16"/>Print Page</div>
					<div class="clearfix"></div>
				    <div class="specs-wrap">
				    <!--  Item specifications -->
				 	</div>
				 	<div class="image-order-container">
					      <div class="detail-image-wrap">
							      <ul id="prodlist">
							      <s:property value='#xutil.getAttribute(#primaryInfoElem,"Description")' escape="false"/>
							      </ul>
							      <s:if	test="#itemMainImages != null && #itemMainImages.size() > 0">
										<s:set name='imageMainLocation'	value="#xutil.getAttribute(#itemMainImages[0], 'ContentLocation')" />
										<s:set name='imageMainId' value="#xutil.getAttribute(#itemMainImages[0], 'ContentID')" />
										<s:hidden name="hdn_imageMainId" value="%{#imageMainId}" />
										<s:set name='imageMainLabel' value="#xutil.getAttribute(#itemMainImages[0], 'Label')" />
										<s:set name='imageMainURL'	value="#imageMainLocation + #imageMainId " />
										<s:if test='%{#imageMainURL=="/"}'>
											<s:set name='imageMainURL' value='%{"/xpedx/images/INF_150x150.jpg"}' />
										</s:if>
										<img src="<s:url value='%{#imageMainURL}' includeParams='none'/>" class="prodImg" id="productImg1" alt="<s:text name='%{#imageMainLabel}'/>" />
								 </s:if>
								 <s:else>
										<img src="<s:url value='%{#pImg}'/>"  class="prodImg" id="productImg1" alt="<s:text name='%{#pImg}'/>"/>
								</s:else>
									<s:set name="xpedxItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@XPEDX_ITEM_LABEL"/>
									<s:set name="customerItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@CUSTOMER_ITEM_LABEL"/>
									<s:set name="manufacturerItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MANUFACTURER_ITEM_LABEL"/>
									<s:set name="mpcItemLabel" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MPC_ITEM_LABEL"/>
									<div class="item-numbers"><b><s:property value="wCContext.storefrontId" /> <s:property value="#xpedxItemLabel" />: <s:property value='%{#itemID}' /></b>											
										<s:if test='certFlag=="Y"'>
										<img border="none"  src="/swc/xpedx/images/catalog/green-e-logo_small.png" alt="" />
										</s:if>
								  </div>
								  <s:if test= '%{#_action.getExtnMfgItemFlag()== "Y"}'>
								  	<div class="mfg-numbers"><s:property value="#manufacturerItemLabel" />: <s:property value='ManufacturerPartNumber' /></div>
								  </s:if>
								  <s:if test= '%{#_action.getExtnCustomerItemFlag()== "Y"}'>
								  	<div class="cust-numbers"><s:property value="#customerItemLabel" />: <s:property value='custPartNumber' /></div>
								  </s:if>
								  <s:if test='%{#isStocked !="Y"}'>
									<div class="mill-mfg-message">Mill / Mfg. Item - Additional charges may apply</div>
								</s:if>
								<s:if test="msdsLinkMap != null && msdsLinkMap.size() > 0">
									<div class="detail-msds-button">
										<s:iterator value="msdsLinkMap" id="msdsMap" status="status" >
												<s:set name="link" value="value" />
												<s:set name="desc" value="key" />
												<input name="" type="button"  class="btn-neutral" value="MSDS" onclick="javascript:window.open('<s:property value='#link'/>');"/>
										</s:iterator>	          						
		        					</div>
	        					</s:if>
					      </div>
					      <div class="order-wrap">
					      	<p><s:property value='#xutil.getAttribute(#itemElemExtn,"ExtnSellText")' escape="false"/></p>
					      	<div class="order-input-wrap">					      						      
						      	<s:set name="addToCartDisabled"	value="%{''}" />
						      	 <s:if test='%{(#catalogUtil.hasAccessToAddtoCart(#primaryInfoElem,#formattedUnitprice))=="Y"}'>
										<s:set name="addToCartDisabled" value="%{''}" />
								</s:if> 
								<s:if test='!#isReadOnly && !(#_action.getWCContext().isGuestUser() == true)'>
									<s:if test="%{#addToCartDisabled ==''}">
										<div class="order-row">
											<s:hidden id="Qty_Check_Flag" name="Qty_Check_Flag" value="false"/>
			            					<div class="order-label">Qty:</div>
			            					<div class="order-input">
			            						<s:textfield name='qtyBox' id="qtyBox" size="7" maxlength="7"	
			            									 value="%{#_action.getRequestedQty()}"
								 							 theme="simple" onkeyup="javascript:isValidQuantityRemoveAlpha(this,event);"
															 onchange="javascript:isValidQuantity(this); javascript:qtyInputCheck(this);"
															 onmouseover="javascript:qtyInputCheck(this);">
												</s:textfield>
												<s:set name="mulVal" value='itemOrderMultipleMap[#itemID]' />
												<s:hidden name="c" id="OrderMultiple" value="%{#mulVal}" />
												<s:set name="itemuomMap" value='#_action.getDisplayItemUOMsMap()' />
												<s:select name="itemUOMsSelect" id="itemUOMsSelect" onchange='javascript:updateUOMFields()'
															cssClass="qty_selector" list="itemuomMap" listKey="key"
															listValue="value" value='%{#_action.getRequestedDefaultUOM()}' disabled="%{#isReadOnly}" theme="simple"
												/>
												<s:set name="requestedUOM"  value="%{#_action.getRequestedUOM()}" />
												<s:hidden name="selectedUOM" value="%{#_action.getRequestedDefaultUOM()}" id="selectedUOM" />	
						 						<s:set name="convFac" value='itemIdConVUOMMap[#requestedUOM]' />
												<s:hidden name="uomConvFactor" id="uomConvFactor" value="%{#convFac}" />
												<s:iterator value='itemIdConVUOMMap'>
													<s:set name='currentUomId' value='key' />
													<s:set name='currentUomConvFact' value='value' />
													<s:hidden name='convF_%{#currentUomId}' id="convF_%{#currentUomId}" value="%{#currentUomConvFact}" />
												</s:iterator>												
			            					</div>
							      		</div>
 								      </s:if>
							 	 </s:if>
							      	 <s:if test=' (isCustomerPO == "Y") '>
										<div class="order-row">
											<div class="order-label"><s:property value='customerPOLabel' />:</div>
											<div class="order-input">
												<s:textfield name='customerPONo' theme="simple" cssClass="x-input bottom-mill-info-avail" id="customerPONo" value=""  maxlength="22" size="30" />
											</div>
										</div>
									</s:if>
									<s:if test=' (isCustomerLinAcc == "Y") '>
										<div class="order-row">
											<div class="order-label"><s:property value='custLineAccNoLabel' />:</div>
											<div class="order-input">
												<s:textfield name='custLineAccNo' theme="simple" cssClass="x-input bottom-mill-info-avail" id="custLineAccNo" value="" maxlength="24" size="30" />
											</div>
										</div>
									</s:if>		
					       </div>
					       <div class="item-button-wrap">
					       		<s:if test="#isEditOrderHeaderKey == null || #isEditOrderHeaderKey=='' ">
									<input name="button" type="button" onclick="javascript:addItemToCart();" class="btn-gradient floatright  addmarginright18" value="Add to Cart"/>
								</s:if>
								<s:else>
									<input name="button" type="button" onclick="javascript:addItemToCart();" class="btn-gradient floatright  addmarginright18" value="Add to Order"/>
								</s:else>					      
	          					<input name="button" class="btn-neutral floatright  addmarginright10"  value="Add to List" onclick="javascript:addItemToWishList();" type="button" />
	         					<div class="show-pa"><a href="javascript:getPriceAndAvailabilityForItems(['<s:property value='%{#itemID}' />']);">Update Price & Availability</a></div>
					       </div>
					       <div class="notice float-right addmarginright18" id="errorMsgForQty" style="display:inline-block;"></div>
					       <s:if test="(replacementAssociatedItems!=null && replacementAssociatedItems.size() > 0)">
						       <div class="replacement-item">
							     This item will be replaced once inventory is depleted.<br/>Select item:
							     <s:iterator value='replacementAssociatedItems' id='replacementItem' status="count" >											
										<s:set name="promoItemPrimInfoElem" value='#xutil.getChildElement(#replacementItem,"PrimaryInformation")' />
										<s:set name="promoItemComputedPrice" value='#xutil.getChildElement(#replacementItem,"ComputedPrice")' />
										<s:set name="itemAssetList" value='#xutil.getElementsByAttribute(#replacementItem,"AssetList/Asset","Type","ITEM_IMAGE_1" )' />
										<s:if test='#itemAssetList != null && #itemAssetList.size() > 0 '>
					        					<s:set name="itemAsset" value='#itemAssetList[0]' />
												<s:set name='imageLocation' value="#xutil.getAttribute(#itemAsset, 'ContentLocation')" />
												<s:set name='imageId' value="#xutil.getAttribute(#itemAsset, 'ContentID')" />
												<s:set name='imageLabel' value="#xutil.getAttribute(#itemAsset, 'Label')" />
												<s:set name='imageURL' value="#imageLocation + '/' + #imageId " />
												<s:if test='%{#imageURL=="/"}'>
													<s:set name='imageURL' value='%{"/xpedx/images/INF_150x150.jpg"}' />
												</s:if>
									  </s:if>
									  <s:url id='detailURLFromPromoProd' namespace='/catalog'	action='itemDetails.action'>
										 <s:param name='itemID'><s:property value='#xutil.getAttribute(#replacementItem,"ItemID")' /></s:param>
										<s:param name='unitOfMeasure'><s:property	value='#xutil.getAttribute(#replacementItem,"UnitOfMeasure")' /></s:param>
									  </s:url>
									  <s:if test='#count.index != 0'><span>,</span>&nbsp;</s:if>
									  <s:a href="%{detailURLFromPromoProd}"  ><s:property value='#xutil.getAttribute(#replacementItem,"ItemID")' /></s:a>									 
				    			</s:iterator>
							 </div>  
					     </s:if>
						     <div class="pa-wrap">
						     		<%-- This will be filled by ajax as the P and A call happens on page load as Ajax --%>
	       						 <div id="priceAndAvailabilityAjax"class="pa-avail">
	       						<%--  <div style="display: none;" id="availabilty_<s:property value='%{#itemID}' />" class="price-and-availability">',
	       						 </div> --%>
	       					 </div>
				     </div>
			 	</div>
			 </div>
		</div>
	</div>	
	</s:else>
</body> 
</swc:html>
