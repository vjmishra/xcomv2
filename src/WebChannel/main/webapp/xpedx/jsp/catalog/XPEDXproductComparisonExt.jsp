<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<%@ taglib prefix="xpedx" uri="/WEB-INF/xpedx.tld"%>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />
<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs�. 
    This is to avoid a defect in Struts that's creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts� OGNL statements. --%>
<s:set name='_action' value='[0]' />
<html>
<head>
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:property   value="wCContext.storefrontId" /> - Catalog - Compare Items </title>
<s:url action='navigate.action' namespace='/catalog' id='myUrl' />

<swc:breadcrumb rootURL='#myUrl' group='catalog' displayGroup='compare' />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/GLOBAL.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/catalog/product-comparisonExt.css" />
	
	<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/ext-all.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/swc.css" />


<link type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/themes/base/jquery.ui.all.css" rel="stylesheet" />
<s:include value="../common/XPEDXStaticInclude.jsp"/>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/banner.css"/>

<!-- sterling 9.0 base  do not edit  javascript move all functions to js/global-xpedx-functions.js -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt.js"></script> 
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/swc.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/productComparison.js"></script>

<!-- sterling 9.0 base  do not edit  javascript move all functions to js/global-xpedx-functions.js -->


<!-- carousel scripts css  -->
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/skins/xpedx/theme.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/skins/xpedx/skin.css" />
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel.js"></script>
<!-- carousel scripts js   -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4.js"></script>
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
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery-ui.min.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/DD_roundies_0.0.2a-min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pseudofocus.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global-xpedx-functions.js"></script>

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jqdialog/jqdialog.js"></script>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jqdialog/jqdialog.css" />

<!-- Facy Box (Lightbox/Modal Window -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
<!-- Page Calls -->
</head>
<body class="ext-gecko ext-gecko3">
<s:set name="xutil" value="XMLUtils" />
<s:set name="prodCompDocument" value="prodComparisonOutputDoc" />
<s:set name="prodCompElement"
	value="#prodCompDocument.getDocumentElement()" />
<s:set name="prodCompDocumentExcluded"
	value="prodComparisonExclOutputDoc" />
<s:set name='scuicontext' value="uiContext" />
<s:set name='showDiffFlag' value="showAttributesWithDifferentValuesOnly" />
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean'
	id='util' />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean' id='xpedxutil' />	
<s:bean
	name='com.sterlingcommerce.webchannel.catalog.utils.CatalogUtilBean'
	id='catalogUtil' />
<s:set name="excludedComparisonSet"
	value="#_action.getExcludedComparisonSet()" />
<s:set name="excludedComparisonSetFlag"
	value="#_action.getHasExcludedComparisonSetFlag()" />
<s:set name="removeProdCompareSetExcl" value="%{'RemoveExcl'}" />
<s:set name="removeProdCompareSetIncl" value="%{'RemoveIncl'}" />
<s:set name="htmlBreak" value="%{'<BR/>'}" />
<s:url id='addToCartURL' namespace='/order' action='addToCart' />
<s:set name='appFlowContext' value='#session.FlowContext' />
<s:set name='isFlowInContext'
	value='#util.isFlowInContext(#appFlowContext)' />
<s:url id='punchOutURL' namespace='/order' action='configPunchOut' />
<s:url id='punchOutURLOrderChange' namespace='/order'
	action='configPunchOut'>
	<s:param name='orderHeaderKey' value='%{#appFlowContext.key}' />
	<s:param name='currency' value='%{#appFlowContext.currency}' />
	<s:param name='flowID' value='%{#appFlowContext.type}' />
</s:url>
<s:set name='isProcurementInspectMode'
	value='#util.isProcurementInspectMode(wCContext)' />
<s:set name='wcContext' value="#_action.getWCContext()" />
<s:set name='itemBranchBeanMap' value='#wcContext.getWCAttribute("itemToItemBranchBeanMap")' />
<s:set name='isReadOnly' value='#isProcurementInspectMode' />
<s:set name='guestUser' value="#_action.getWCContext().isGuestUser()" />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.common.XPEDXSCXmlUtils' id='xpedxSCXmlUtil' />
<s:url id="ajaxAvailabilityJsonURL" action="ajaxAvailabilityJson" namespace="/catalog"/>
<s:url id="ajaxAvailabilityJson_PriceURL" action="ajaxAvailabilityJson_Price" namespace="/catalog"/>
<div id="main-container">
	<div id="main"><!-- begin header -->
		<div class="t2-header commonHeader" id="headerContainer"><!-- add content here for header information -->
			<s:action name="xpedxHeader" executeResult="true" namespace="/common" />
		</div>

		<s:if test='!#guestUser'>  	
			<s:action name="xpedxShiptoHeader" executeResult="true" namespace="/common" />
		</s:if>	
 
 		<div class="container" style="overflow: auto;" >
 
 		<!--- begin breadcrumbs header--->
		<div id="breadcrumbs-list-name" class="breadcrumbs-no-float">
			<!-- <a href="javascript:window.history.back();">Back</a> / Compare Items -->			
			<!-- Begin - Changes made by Mitesh Parikh for 2422 JIRA -->
			<script type="text/javascript">
			function getbackUrl()
			{
				var backUrl = document.getElementById("backUrl").value;
				window.location.href=backUrl;
			}
			</script>
			<a href="javascript:getbackUrl();">Back</a> / <span class="page-title"> Compare Items </span>
			<%--<s:property value='%{#session.lastPage}' />
			 String referer = request.getHeader("referer");
			
			if(session.getAttribute("lastPageUrl")==null){
				session.setAttribute("lastPageUrl",referer);
			}
			else{
				referer = (String)session.getAttribute("lastPageUrl");
			}
		--%>
			<s:hidden id='backUrl' name='backUrl' value="%{#session.productCompareBackPageURL.substring(#session.productCompareBackPageURL.indexOf('/swc'))}" />
			<!-- End - Changes made by Mitesh Parikh for 2422 JIRA -->
		</div>
		<!--- clearall puts stuff below 'floating' items. nonbreaking space is for IE--->				
		<div class="clearall">&nbsp;</div>
		<!--- end breadcrumbs header --->
 	 
	<!-- BEGIN compare -->
	<!-- table list -->
	<s:form action="prodCompare" namespace="/catalog" id="prodCompare"
		name="prodCompare" method="post" theme="css_xhtml">
				
		<div class="listTableContainer2" id="productComparison">
		
		<s:if test="%{null != #xutil.getChildElement(#prodCompElement, 'Item')}">
			<s:set name='currency'
				value="#xutil.getAttribute(#prodCompElement, 'Currency')" />
			<!-- table list -->
			<s:if test='%{#excludedComparisonSetFlag == "true" }'>
				<table class="listTableBody4CompExcl">
					<tr>
						<td><s:text name='Excluded_Item_List_Message' /></td>
					</tr>
				</table>
				<table class="listTableBody4CompExcl">

					<s:set name="prodCompElementExcluded"
						value="#prodCompDocumentExcluded.getDocumentElement()" />

					<s:iterator
						value='#xutil.getChildren(#prodCompElementExcluded, "Item")'
						id='itemExcl' status="ItrStatus">
						<s:set name='primeryInfo'
							value="#xutil.getChildElement(#itemExcl, 'PrimaryInformation')" />

						<tr>

							<s:url id='actionURL' namespace='/catalog'
								action='prodCompare.action' includeParams='none'>
								<s:param name='prodCompareAction'>
									<s:property value='#removeProdCompareSetExcl' />
								</s:param>
								<s:param name='itemKeys'>
									<s:property value='#xutil.getAttribute(#itemExcl,"ItemKey")' />
								</s:param>
							</s:url>
							<td class="listTableBody4CompExcltd"><s:a title="Remove"
								href="%{actionURL}" cssClass="deleteIcon"
								tabindex="%{101+#ItrStatus.index}">
			                    		&nbsp;
			                    	</s:a></td>
							<td><s:property
								value='#xutil.getAttribute(#itemExcl,"ItemID")' />&nbsp; <s:property
								value='#xutil.getAttribute(#primeryInfo,"ShortDescription")' />
							</td>

						</tr>
					</s:iterator>
				</table>
				<BR />
			</s:if>
			<s:hidden name="showAttributesWithDifferentValuesOnly"
				id="showAttributesWithDifferentValuesOnly" />
			<script type="text/javascript">
/*
 * Functions for add to cart operation
 */	
function addToCart(url, productID, UOM, quantity, headerKey,returnURL,flowID,currency)
{
	
	if(flowID == "2" && currency != "")
	{
			Ext.Ajax.request({
		    	// for testing only
		        url: url,
		        params: {
					productID: productID,
			    	productUOM: UOM,
			    	quantity: quantity,
			    	orderHeaderKey: headerKey,
			    	flowID: flowID,
			    	currency:currency
		        },
		        // end testing
		        method: 'GET',
		        success: function (response, request){
            	document.location.href = returnURL;
		            //var myDiv = document.getElementById("ajax-body-1");
		            //myDiv.innerHTML = response.responseText;
		            //DialogPanel.toggleDialogVisibility('modalDialogPanel1');
		        },
		        failure: function (response, request){
		            var myDiv = document.getElementById("ajax-body-1");
		            myDiv.innerHTML = response.responseText;
		            DialogPanel.show('addToCartFailure');
		            svg_classhandlers_decoratePage();
		        }
		    });
    }
    else
    {
    				Ext.Ajax.request({
		    	// for testing only
		        url: url,
		        params: {
					productID: productID,
			    	productUOM: UOM,
			    	quantity: quantity,
			    	orderHeaderKey: headerKey

		        },
		        // end testing
		        method: 'GET',
		        success: function (response, request){
			         refreshMiniCartLink();
			         showMiniCartWindow();
		            //var myDiv = document.getElementById("ajax-body-1");
		            //myDiv.innerHTML = response.responseText;
		            //DialogPanel.toggleDialogVisibility('modalDialogPanel1');
		        },
		        failure: function (response, request){
		            var myDiv = document.getElementById("ajax-body-1");
		            myDiv.innerHTML = response.responseText;
		            DialogPanel.show('addToCartFailure');
		            svg_classhandlers_decoratePage();
		        }
		    });
    }
}
var myMask;
function openAddToCart(val){
	myMask = new Ext.LoadMask(Ext.getBody(), {msg:"Processing..."});
	myMask.show();
	var url = "<s:property value='#ajaxAvailabilityJsonURL'/>";
    url = ReplaceAll(url,"&amp;",'&');
    url=url+'&pnaItemId='+val;
	Ext.onReady(function(){
		Ext.Ajax.request({	
				   url: url,
				   success: handleAjaxAvailabilitySuccess,
				   failure: handleAjaxAvailabilityFailure			
		});
					
	});
}

function handleAjaxAvailabilityFailure(){
	myMask.hide();	
	alert('Failure');
}

function handleAjaxAvailabilitySuccess(responseObject){	
	myMask.hide();	
	var responseText = responseObject.responseText;
	if(responseText.indexOf("Unable to get Price and Availability.Please contact system admin")>-1)
	{
		Ext.fly('availibility-grid_error').dom.innerHTML='';
		Ext.fly('availibility-grid_error').dom.innerHTML=responseText;
		DialogPanel.show("addToCart1");	
		svg_classhandlers_decoratePage();
	}
	else
	{
		var variable1="{\"availList\":";
		var startIndexof=responseText.indexOf(variable1);
		if(startIndexof > 0){
			var jsonString=responseText.substring(startIndexof,responseText.length);
			variable1="}]}";
			startIndexof=jsonString.indexOf(variable1)+variable1.length;
			jsonString=jsonString.substring(0,startIndexof);
			populateDivGrid1(jsonString);
		}		
		Ext.fly('add-to-cart-grid').dom.innerHTML='';
		Ext.fly('add-to-cart-grid').dom.innerHTML=responseText;
		DialogPanel.show("addToCart");
		svg_classhandlers_decoratePage();	
	}	
}

function populateDivGrid1(myData)
{	
	var obj = eval("(" + myData + ")");
	var url = "<s:property value='#ajaxAvailabilityJsonURL'/>";
	url = ReplaceAll(url,"&amp;",'&');
	var availStore = new Ext.data.JsonStore({
	   proxy: new Ext.data.HttpProxy({  
		    method:'POST',  
		    url: url,
		    headers: {'Content-type': 'application/json'}
		}),		
		data   : obj,					
		root: 'availList',
		fields:['availability','availValue']		
    });
	var grid = new Ext.grid.GridPanel({
        width:550,
        height:200,
        layout:'fit',
        title:'',
        store: availStore,
        trackMouseOver:false,
        disableSelection:true,
        loadMask: true,

        // grid columns
        columns:[{
            id: 'topic', 
            header: '',
            dataIndex: 'availability',
            width: 130,
            height:80,
            sortable: false
        },{
            header: '',
            dataIndex: 'availValue',
            width: 130,
            height:80,
            sortable: false
        }]
    });//grid			
    Ext.fly('availibility-grid').dom.innerHTML='';
	availStore.loadData(obj);	
	grid.render('availibility-grid'); 	    		
}
function openData(data)
{	var ITemId=document.getElementById("ITemId").value;
	var Qty=document.getElementById("Qty").value;
	var UOM=document.getElementById("UOM").value;
	var Job=document.getElementById("Job").value;
	var customer=document.getElementById("customer").value;
	
	listAddToCartItem('<s:property value="#addToCartURL" escape='false'/>',ITemId,UOM,Qty,Job,customer,'');
}

function listAddToCartItem(url, productID, UOM, quantity,Job,customer) {
      
   	Ext.Ajax.request({
    	// for testing only
        url: url,
        params: {
			productID: productID,
	    	productUOM: UOM,
	    	quantity: quantity,
	    	reqJobId: Job,
	    	reqCustomer:customer
	     },
        // end testing
        method: 'GET',
        success: function (response, request){
              
            //DialogPanel.toggleDialogVisibility('addToCart');	            
            //var myDiv = document.getElementById("ajax-body-1");	            
            //myDiv.innerHTML = 'The product has been successfully added to the cart';	            
            //DialogPanel.show('modalDialogPanel1');	            
            //svg_classhandlers_decoratePage();
        },
        failure: function (response, request){
            var myDiv = document.getElementById("ajax-body-1");
            myDiv.innerHTML = response.responseText;
            DialogPanel.show('modalDialogPanel1');
            svg_classhandlers_decoratePage();
             }
    });		
}	

function openDialog()
{
 	openAddToCart1(document.getElementById("ITemId").value)
	javascript:DialogPanel.show('bracketPricingDialog');
}

function openAddToCart1(val){
	var url = "<s:property value='#ajaxAvailabilityJson_PriceURL'/>";
	url = ReplaceAll(url,"&amp;",'&');
	Ext.onReady(function(){
		Ext.Ajax.request({
		   url: url,//'/swc/catalog/ajaxAvailabilityJson_Price.action?sfId=xpedx&pnaItemId='+val,
		   success: handleAjaxAvailabilitySuccess1,
		   failure: handleAjaxAvailabilityFailure1
		});
					
	});
}

function handleAjaxAvailabilitySuccess1(responseObject){
	myMask.hide();	
	var responseText = responseObject.responseText;
	Ext.fly('Price_Grid').dom.innerHTML='';
	Ext.fly('Price_Grid').dom.innerHTML=responseText;		
	svg_classhandlers_decoratePage();
}
function handleAjaxAvailabilityFailure1(){	
	myMask.hide();
	alert('fail');
}
 
function checkUpdateAvailability()
{
	var ItemId=document.getElementById("ITemId").value;
	var UOM=document.getElementById("UOM").value;	
	if(document.getElementById("Qty").value!=null)
	{
		var qtyEle = document.getElementById("Qty");
		ItemId=ItemId+'&Qty='+document.getElementById("Qty").value+'&UOM='+UOM;
	}
	openAddToCart(ItemId);
	
}

function processDetail(itemid, uom) {
	<s:url id='detailURL' namespace='/catalog' action='itemDetails.action'>
	</s:url>
	// Begin - Changes made by Mitesh Parikh for 2422 JIRA
	<s:set name="itemDtlBackPageURL" value="%{itemDtlBackPageURL}" scope="session"/>
	// End - Changes made by Mitesh Parikh for 2422 JIRA
	location.href="<s:property value='%{detailURL}' escape='false'/>" + "&itemID=" + itemid + "&unitOfMeasure=" + uom;
}
Ext.onReady(function(){

    var compSetDef = Ext.data.Record.create([
    {name: 'compattr'},
    {name: 'compaatrgrp'},
    {name: 'sortorder'},
    <s:iterator value='#xutil.getChildren(#prodCompElement, "Item")' id='item' status="iterStatus">
	    <s:set name='itemID' value='#xutil.getAttribute(#item,"ItemID")'/>
	    <s:set name='unitOfMeasure' value='#xutil.getAttribute(#item,"UnitOfMeasure")'/>
	    {name: '<s:property value="#itemID"/><s:property value="#unitOfMeasure"/>'}
	    <s:if test="#iterStatus.last == false ">,</s:if>
    </s:iterator>
    ]);
    var reader = new Ext.data.ArrayReader({},compSetDef);
	var tmpl = new Ext.XTemplate(
	'<table class="comparisonTable"><tbody>',
	/*'<tr><td>',
			'<table class="buttonComp"><tr><td>',
				'{[values.info[5]]}{[values.info[6]]}</td><td>',
				'</td>',			
			'</tr><tr><td>{[values.info[14]]}</td><td>&nbsp;</td></tr>',
			'</tr><tr><td>{[values.info[7]]}</td><td>&nbsp;'</td></tr></table>',
		'</td></tr>',
		 '<tr><td></td></tr>',			
		'<tr class="bottomDottedLineComp">',
			'<td><b>{[values.info[2]]}</b></td>',
		'</tr>', 
		*/
		'<tr><td  class="paddingTopComp">',
			'{[values.info[1]]}',
		'</td></tr>',
// 		'<tr><td><div class="itemDescComp">{[values.info[4]]}</div></td></tr>', 
	'</tbody></table>',
        {
           getDecodedString : function(s) {
             if(!s) s=""; 
  			 s= s.replace(/\'/g, "&#39;");
  			 s= s.replace(/\"/g, "&#34;");
             return s;
           }
		}
    );

	tmpl.compile();
	function renderProd(v, p, r, ri, ci, s){
        if (ri == 0) {
			return tmpl.apply(prodData[v]);
		}
		else if (v == 'yes' || v == 'no') {
			return '<img src="<s:property value='#wcUtil.staticFileLocation' />/swc/images/common/'+v+'.gif" alt="'+v+'"/>';
		}
		else {
			return "<div class='comparisonTable'>"+v+"</div>";
		}
    }
    function renderShowDiff(v, p, r, ri, ci, s){
     if (ri == 0)
     {
         <s:if test='%{#showDiffFlag == "Y"}'>
	       	return "<table width=\"100%\"><tr><td><a class=\"submitBtnBg1 underlink\" href=\"javascript:showAll()\";>Show All</td></tr></table>";
	     </s:if>
	   	 <s:else>
	       	return "<table><tr><td><a class=\"underlink\" href=\"javascript:showDifferences()\";>Show Differences</td></tr></table>";
	     </s:else>
	}
	else
		return v;
    }
	var grid = new Ext.grid.GridPanel({
        store: new Ext.data.GroupingStore({
            reader: reader,
            data: prodCompData,
           sortInfo:{field: 'sortorder', direction: "ASC"},
            groupField:'compaatrgrp'
        }),
		cls: 'swc-ext',
        columns: [
            {id:'compattr', header: ' ', width: 200,  fixed: true, dataIndex: 'compattr', renderer: renderShowDiff},
            {header: 'compaatrgrp', width: 0,  dataIndex: 'compaatrgrp'},
                <s:iterator value='#xutil.getChildren(#prodCompElement, "Item")' id='item' status="iterStatus">
	    			<s:set name='itemID' value='#xutil.getAttribute(#item,"ItemID")'/>
	    			<s:set name='unitOfMeasure' value='#xutil.getAttribute(#item,"UnitOfMeasure")'/>
	    			<s:set name='itemKey' value='#xutil.getAttribute(#item,"ItemKey")'/>
	    			<s:set name='removeIncl' value='#removeProdCompareSetIncl'/>

            			{header: '<span style="cursor:move;"><b><s:property value="#itemID"/></b></span><span class="padding-left2"><a class="underlink" href="javascript:removeItem(\'<s:property value="#itemKey"/>\',\'<s:property value="#removeIncl"/>\');" tabindex=\'<s:property value="100+#iterStatus.index"/>\'>[Remove]</a>', width: 200, fixed: false, dataIndex: '<s:property value="#itemID"/><s:property value="#unitOfMeasure"/>', renderer: renderProd}
            			<s:if test="#iterStatus.last == false ">,</s:if>
            	</s:iterator>
        	],
        view: new Ext.grid.GroupingView({
            forceFit:true,
			enableGroupingMenu: false,
			showGroupName: false,
			hideGroupedColumn: true,
			groupTextTpl: '{[values.group.substring(5)]}'
        }),
        frame: false,
        autoScroll : true,
		enableHdMenu: false,
		enableColumnMove: true,
		enableColumnResize: true,
		enableColumnHide: false,
        autoWidth: true,
		trackMouseOver: true,
        autoHeight: true,
        collapsible: true,
		enableDragDrop: true,
		disableSelection: true,
        animCollapse: false,
        iconCls: 'icon-grid',
		renderTo: 'productComparison',
		listeners: {  
			'afterrender': {    
				fn: function() {      
	           		svg_classhandlers_decoratePage();
		    	},    
		    	delay:500  
		    }
		}
		
    });
	grid.on('columnmove', colMove);
	grid.on('render', function(){

	}); 

	function colMove(oi, ni) {
		if (oi == 0){
			this.getColumnModel().moveColumn(ni, oi);
			}

		svg_classhandlers_decoratePage();
	}

	function setGridStyles(g) {
		alert("sds");
	}
});
function removeItem(itmKey, action) {
	<s:url id='removeCompSetURL' namespace='/catalog' action='prodCompare.action'>
	</s:url>
	// Begin - Changes made by Mitesh Parikh for 2422 JIRA
	<s:set name="itemDtlBackPageURL" value="%{itemDtlBackPageURL}" scope="session"/>
	// End - Changes made by Mitesh Parikh for 2422 JIRA
	location.href="<s:property value='%{removeCompSetURL}' escape='false'/>" + "&itemKeys=" + itmKey + "&prodCompareAction=" + action;
}

var prodCompData = [
	['Product','0000-  Item Information','',

	<s:iterator value='#xutil.getChildren(#prodCompElement, "Item")' id='item' status="iterStatus">
	    <s:set name='itemID' value='#xutil.getAttribute(#item,"ItemID")'/>
	    <s:set name='unitOfMeasure' value='#xutil.getAttribute(#item,"UnitOfMeasure")'/>
	    '<s:property value="#itemID"/><s:property value="#unitOfMeasure"/>'
		<s:if test="#iterStatus.last == false ">,</s:if>
	</s:iterator>
	]
	<s:set name='itemAttributeGroupTypeList' value="#xutil.getChildElement(#prodCompElement, 'ItemAttributeGroupTypeList')" />
	<s:set name='itemAttributeGroupType' value="#xutil.getChildElement(#itemAttributeGroupTypeList, 'ItemAttributeGroupType')" />
	<s:set name='itemAttributeGroupList' value="#xutil.getChildElement(#itemAttributeGroupType, 'ItemAttributeGroupList')" />
	<s:if test="%{null != #xutil.getChildElement(#itemAttributeGroupList, 'ItemAttributeGroup')}">
		<s:iterator value='#xutil.getChildren(#itemAttributeGroupList, "ItemAttributeGroup")' id='attributeGroup' status='attrGroupCount'>
			<s:if test="#attrGroupCount.count < 10 ">
				<s:set name='attrGrpPrefix' value='%{"000" + #attrGroupCount.count}'/>
			</s:if>
			<s:elseif test="#attrGroupCount.count < 100 ">
				<s:set name='attrGrpPrefix' value='%{"00" + #attrGroupCount.count}'/>
			</s:elseif>
			<s:elseif test="#attrGroupCount.count < 1000 ">
				<s:set name='attrGrpPrefix' value='%{"0" + #attrGroupCount.count}'/>
			</s:elseif>
			<s:elseif test="#attrGroupCount.count < 10000 ">
				<s:set name='attrGrpPrefix' value='#attrGroupCount.count'/>
			</s:elseif>
			<s:set name='grpDesc' value='%{#attrGrpPrefix + "-" + #xutil.getAttribute(#attributeGroup,"ItemAttributeGroupDescription") }'/>
			<s:set name='itemAttributeList' value="#xutil.getChildElement(#attributeGroup, 'ItemAttributeList')" />
			<s:if test="%{#itemAttributeList != null && #xutil.getChildren(#itemAttributeList, 'ItemAttribute') != null
				&& #xutil.getChildren(#itemAttributeList, 'ItemAttribute').size() > 0}">
				,
			</s:if>
			<s:iterator value='#xutil.getChildren(#itemAttributeList, "ItemAttribute")' id='itemAttribute' status='attrCount'>

				<s:set name="itemAttrFlag" value="%{false}" />
				<s:set name='itemList' value='#xutil.getChildElement(#itemAttribute,"ItemList")' />
				<s:iterator value='#xutil.getChildren(#itemList, "Item")' id='item' status='assignItemCount1'>
					<s:set name='assignedValueList' value='#xutil.getChildElement(#item,"AssignedValueList")' />
					<s:if test="%{null != #assignedValueList && null != #xutil.getChildElement(#assignedValueList, 'AssignedValue')}">
						<s:iterator value='#xutil.getChildren(#assignedValueList, "AssignedValue")' id='attributeValue'>
							<s:set name='value' value='#xutil.getAttribute(#attributeValue,"Value")' />
							<s:if test='%{#value != ""}'>
								<s:set name='itemAttrFlag' value="%{true}" />
							</s:if>
						</s:iterator>
					</s:if>
				</s:iterator>

			<s:if test='#itemAttrFlag'>		
				<s:set name='attribute' value="#xutil.getChildElement(#itemAttribute, 'Attribute')" />
				<s:if test="%{null != #xutil.getAttribute(#attribute,'ShortDescription')&& '' !=#xutil.getAttribute(#attribute,'ShortDescription') }">
					<s:set name='attrName' value='#xutil.getAttribute(#attribute,"ShortDescription")'/>
				</s:if>
				<s:else>
					<s:set name='attrName' value='#xutil.getAttribute(#itemAttribute,"ItemAttributeDescription")'/>
				</s:else>
				<s:if test="%{null != #xutil.getAttribute(#attribute,'AllowMultipleValues')}">
				   <s:set name='allowMultiVals' value='%{#xutil.getAttribute(#attribute,"AllowMultipleValues")}' />
				</s:if>
				<s:if test="%{null != #xutil.getAttribute(#attribute,'IsAllowedValueDefined')}">
				   <s:set name='valueDefined' value='%{#xutil.getAttribute(#attribute,"IsAllowedValueDefined")}' />
				</s:if>
                <s:set name="derivedFrom" value='' />  
				<s:if test="%{null != #xutil.getAttribute(#attribute,'DerivedFromAttributeKey')}">
				   <s:set name='derivedFrom' value='%{#xutil.getAttribute(#attribute,"DerivedFromAttributeKey")}' />
				</s:if>
				 
				['<s:property value="#attrName"/>','<s:property value="#grpDesc"/>','<s:property value="#attrCount.count"/>',
				<s:set name='dataType' value='#xutil.getAttribute(#attribute,"DataType")' />
				<s:set name='postFix' value='#xutil.getAttribute(#attribute,"AttributePostFix")' />
		  	    <s:if test="%{#dataType=='BOOLEAN'}">
					<s:set name='itemList' value='#xutil.getChildElement(#itemAttribute,"ItemList")' />
					<s:iterator value='#xutil.getChildren(#itemList, "Item")' id='item' status='assignItemCount'>
						<s:set name='assignedValueList' value='#xutil.getChildElement(#item,"AssignedValueList")' />
							<s:if test="%{null == #assignedValueList || null == #xutil.getChildElement(#assignedValueList, 'AssignedValue')}">
								''
							</s:if>
						<s:iterator value='#xutil.getChildren(#assignedValueList, "AssignedValue")' id='attributeValue'>
						<s:set name='value' value='#xutil.getAttribute(#attributeValue,"Value")' />
							<s:if test='%{#value == "Y"}'>
								'yes'
							</s:if>
							<s:else>
								'no'
							</s:else>
						</s:iterator>
						<s:if test="#assignItemCount.last == false ">,</s:if>
					</s:iterator>
				</s:if>
				<s:else>
					<s:set name='itemList' value='#xutil.getChildElement(#itemAttribute,"ItemList")' />
					<s:iterator value='#xutil.getChildren(#itemList, "Item")' id='item' status='assignItemCount1'>
						<s:set name='assignedValueList' value='#xutil.getChildElement(#item,"AssignedValueList")' />
						<s:if test="%{null == #assignedValueList || null == #xutil.getChildElement(#assignedValueList, 'AssignedValue')}">
						''
						</s:if>
						<s:else>
						<s:set name='attrValuesString' value='%{""}'/>
						<s:iterator value='#xutil.getChildren(#assignedValueList, "AssignedValue")' id='attributeValue' status='attrValueCount'>
// 							Adding for getting description for baseom
							<s:if test='%{"BaseUom" == #attrName}'>
								<s:set name='attrValue' value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getUOMDescription(#xutil.getAttribute(#attributeValue,"Value"))'/>
								<s:set name='attrValueWithPostFix' value='%{#attrValue + " " + #postFix}'/>
							</s:if>
							<s:elseif test='%{"" != #derivedFrom}'>
    							<s:set name='attrValue' value='#xutil.getAttribute(#attributeValue,"Value")'/>
    							<s:set name='attrValueWithPostFix' value='%{#attrValue + " " + #postFix}'/>
                            </s:elseif> 
							<s:elseif test="%{#dataType=='INTEGER'}">
							    <s:if test='%{(#valueDefined == "Y" && allowMultiVals == "Y") || (#derivedFrom != null && #derivedFrom.length() > 0)}'>
								   <s:set name='attrValue' value='#xutil.getAttribute(#attributeValue,"Value")'/>
								</s:if>
								<s:else>
									<s:set name='attrValue' value='#xutil.getAttribute(#attributeValue,"IntegerValue")'/>
								</s:else>	   
                                <s:set name='attrValueWithPostFix' value='%{#attrValue + " " + #postFix}'/>
							</s:elseif>
                            <s:elseif test="%{#dataType=='INTEGER'}">
                                <s:if test='%{(#valueDefined == "Y" && allowMultiVals == "Y") || (#derivedFrom != null && #derivedFrom.length() > 0)}'>
                                   <s:set name='attrValue' value='#xutil.getAttribute(#attributeValue,"Value")'/>
                                </s:if>
                                <s:else>
                                    <s:set name='attrValue' value='#xutil.getAttribute(#attributeValue,"IntegerValue")'/>
                                </s:else>      
                                <s:set name='attrValueWithPostFix' value='%{#attrValue + " " + #postFix}'/>
                            </s:elseif>
							<s:elseif test="%{#dataType=='DECIMAL'}">
							    <s:if test='%{(#valueDefined == "Y" && allowMultiVals == "Y") || (#derivedFrom != null && #derivedFrom.length() > 0)}'>
								   <s:set name='attrValue' value='#xutil.getAttribute(#attributeValue,"Value")'/>
								</s:if>
								<s:else>
									<s:set name='attrValue' value='#xutil.getAttribute(#attributeValue,"DoubleValue")'/>
								</s:else>	
                                <s:set name='attrValueWithPostFix' value='%{#attrValue + " " + #postFix}'/>
							</s:elseif>
							<s:else >
    							<s:set name='attrValue' value='#xutil.getAttribute(#attributeValue,"Value")'/>
    							<s:set name='attrValueWithPostFix' value='%{#attrValue + " " + #postFix}'/>
							</s:else>

							<s:set name='attrValuesString' value='%{#attrValuesString + #attrValueWithPostFix}'/>
							<s:if test="#attrValueCount.last == false ">
							<s:set name='attrValuesString' value='%{#attrValuesString + #htmlBreak}'/>

							</s:if>
							<s:if test="#attrValueCount.last == true "></s:if>
						</s:iterator>
						'<s:property escape='false' value="#attrValuesString"/>'
						</s:else>
						<s:if test="#assignItemCount1.last == false ">,</s:if>
					</s:iterator>
				</s:else>
				]<s:if test="#attrCount.last == false ">,</s:if></s:if>
          	</s:iterator>
		</s:iterator>
	</s:if>
];
var prodData = {

        <s:iterator value='#xutil.getChildren(#prodCompElement, "Item")' id='item' status="iterStatus">
        	<s:set name='itemID' value='#xutil.getAttribute(#item,"ItemID")'/>
        	<s:set name='unitOfMeasure' value='#xutil.getAttribute(#item,"UnitOfMeasure")'/>
	        <s:set name='primeInfo' value="#xutil.getChildElement(#item, 'PrimaryInformation')" />
	        <s:set name='shortDesc' value="#util.formatEscapeCharacters(#xutil.getAttribute(#primeInfo, 'ShortDescription'))" />
	        <s:set name='desc' value="#util.formatEscapeCharacters(#xutil.getAttribute(#primeInfo, 'Description'))" />
	        <s:set name='imageLocation' value="#xpedxSCXmlUtil.getAttribute(#primeInfo, 'ImageLocation')" />
			<s:set name='imageId' value="#xutil.getAttribute(#primeInfo, 'ImageID')" />
			<s:set name='imageLabel' value="#xutil.getAttribute(#primeInfo, 'ImageLabel')" />
			<s:set name='imageURL' value="#imageLocation + '/' + #imageId " />
			<s:if test='%{#imageURL=="/"}'>
				<s:set name='imageURL' value='%{"/xpedx/images/INF_150x150.jpg"}' />
			</s:if>	
			<s:set name='kitCode' value='#xutil.getAttribute(#primeInfo,"KitCode")' />
			<s:url id='imgURL' value='%{#imageURL}'/>
			<s:set name='computedPrice' value="#xutil.getChildElement(#item, 'ComputedPrice')" />
			<s:set name='itemBranchBean' value='#itemBranchBeanMap.get(#itemID)'/>
			<s:set name='b2cstockStatus' value='#itemBranchBean.inventoryIndicator'/>
	        <s:if test='%{#kitCode == "BUNDLE" }'> 
			  <s:set name='price' value='#xutil.getAttribute(#computedPrice,"BundleTotal")' />
			</s:if><s:else>
			  <s:set name='price' value='#xutil.getAttribute(#computedPrice,"UnitPrice")' />
			</s:else>
			<s:set name='showCurrencySymbol' value='true' />
	        <s:set name='displayPrice' value='#xpedxutil.formatPriceWithCurrencySymbol(#scuicontext,#currency,#price,#showCurrencySymbol)'/>
       		<s:set name='isSuperseded' value='#xutil.getAttribute(#item,"IsItemSuperseded")'/>
       		<s:set name='isValid' value='#xutil.getAttribute(#primeInfo"IsValid")'/>
			<s:set name='isModelItem' value='#xutil.getAttribute(#primeInfo,"IsModelItem")'/>
			<s:set name='isConfigurable' value='#xutil.getAttribute(#primeInfo,"IsConfigurable")'/>
			<s:set name='isPreConfigured' value='#xutil.getAttribute(#primeInfo,"IsPreConfigured")'/>
			'<s:property value="#itemID"/><s:property value="#unitOfMeasure"/>':{'info':['<s:property value="#itemID"/><s:property value="#unitOfMeasure"/>','<a href=\'javascript:processDetail("<s:property value='#itemID'/>\", \"<s:property value='#unitOfMeasure'/>\");\' tabindex=\'<s:property value="(#iterStatus.count)+240"/>\' ><img src="<s:property value="#imgURL"/>" alt="<s:property value="#shortDesc"/>" title="<s:property value="#shortDesc"/>" height="125px" width="125"/><s:property value="#shortDesc"/></a>','', '<s:property value="#displayPrice"/>', '<s:property value="#desc" escape="false"/>',
    <s:if test='!#isReadOnly'>            
			 
			<s:if test='{!#guestUser}'>
				'<tr><td>',
					'<s:property value="%{#_action.getItemListMapHTMLString()}" escape="false" />',
				'</td></tr>',
			</s:if>
		
			<s:elseif test='%{#isModelItem=="Y" && #isValid != "N"}'>
				<s:url id='variationPageURL' namespace='/catalog' action='itemVariation.action'>
					<s:param name='itemID'><s:property value='#itemID'/></s:param>
					<s:param name='unitOfMeasure'><s:property value='#unitOfMeasure'/></s:param>
				</s:url>
				'<a href=\'<s:property value="#variationPageURL" escape="false"/>\' class=\'submitBtnBg2\' id=\'select_<s:property value="#prodStatus.index"/>\' tabindex=\'<s:property value="(#iterStatus.count)+200"/>\'><s:text name="Select"/></a>',
 				
		     </s:elseif>
			<s:else>
			'',
			</s:else>
			<s:if test='%{#isConfigurable=="Y" && (#isValid != "N")}'>
					<s:url id='catalogURL' namespace='/catalog' action='prodCompare'/>
					<s:set name='configurationKey' value='#primeInfo.getAttribute("ConfigurationKey")'/>
					<s:if test='#isPreConfigured!="Y" || #configurationKey==null || #configurationKey=="" '>
						<s:set name='model' value='#primeInfo.getAttribute("ConfigurationModelName")'/>
						<s:if test='%{#isFlowInContext == true}'>
							<s:url id='configURL' namespace='/configurator' action='configure'>
								<s:param name='Model' value='#model'/>
								<s:param name='ReturnURL' value='%{punchOutURLOrderChange}'/>
								<s:param name='CancelURL' value='%{catalogURL}'/>
								<s:param name='doneButton' value='%{#_action.getText("Add_to_order")}'/>
								<s:param name='orderHeaderKey' value='%{#appFlowContext.key}'/>
								<s:param name='currency' value='%{#appFlowContext.currency}'/>
								<s:param name='flowID' value='%{#appFlowContext.type}'/>
								<s:param name="displayHeader">/common/header</s:param>
		                        <s:param name="displayFooter">/common/footer</s:param>
                                <s:param name="displayNavBar">/catalog/catalogNavBar</s:param>
								<%--
								<s:param name='Model' value='#model'/>
								 --%>
							</s:url>
						</s:if>
						<s:else>
								<s:url id='configURL' namespace='/configurator' action='configure'>
								<s:param name='Model' value='#model'/>
								<s:param name='ReturnURL' value='%{punchOutURL}'/>
								<s:param name='CancelURL' value='%{catalogURL}'/>
								<s:param name='doneButton' value='%{#_action.getText("Add_to_cart")}'/>
								<s:param name="displayHeader">/common/header</s:param>
		                        <s:param name="displayFooter">/common/footer</s:param>
                                <s:param name="displayNavBar">/catalog/catalogNavBar</s:param>
								<%--
								<s:param name='Model' value='#model'/>
								 --%>
							</s:url>
						</s:else>
					</s:if>
					<s:else>
						<s:if test='%{#isFlowInContext == true}'>
						<s:url id='configURL' namespace='/configurator' action='configure'>
							<%--
							<s:param name='configurationKey' value='#configurationKey'/>
							 --%>
							<s:param name='configurationKey' value='#configurationKey'/>
							<s:param name='ReturnURL' value='%{punchOutURLOrderChange}'/>
							<s:param name='CancelURL' value='%{catalogURL}'/>
							<s:param name='doneButton' value='%{#_action.getText("Add_to_order")}'/>
							<s:param name='orderHeaderKey' value='%{#appFlowContext.key}'/>
							<s:param name='currency' value='%{#appFlowContext.currency}'/>
							<s:param name='flowID' value='%{#appFlowContext.type}'/>
							<s:param name="displayHeader">/common/header</s:param>
	                        <s:param name="displayFooter">/common/footer</s:param>
                            <s:param name="displayNavBar">/catalog/catalogNavBar</s:param>
						</s:url>
						</s:if>
						<s:else>
							<s:url id='configURL' namespace='/configurator' action='configure'>
							<%--
							<s:param name='configurationKey' value='#configurationKey'/>
							 --%>
							<s:param name='configurationKey' value='#configurationKey'/>
							<s:param name='ReturnURL' value='%{punchOutURL}'/>
							<s:param name='CancelURL' value='%{catalogURL}'/>
							<s:param name='doneButton' value='%{#_action.getText("Add_to_cart")}'/>
							<s:param name="displayHeader">/common/header</s:param>
	                        <s:param name="displayFooter">/common/footer</s:param>
                            <s:param name="displayNavBar">/catalog/catalogNavBar</s:param>
						</s:url>
						</s:else>
					</s:else>
				'<a href=\'<s:property value="#configURL" escape="false"/>\' class=\'submitBtnBg2\' id=\'config_<s:property value="#iterStatus.index"/>\' tabindex=\'<s:property value="(#iterStatus.count)+220"/>\'><s:text name="Build"/></a>',
				</s:if>
				<s:else>
				'',
				</s:else>
				<s:if test='#isFlowInContext == true'>
						'<a href=\'<s:property value="#appFlowContext.returnURL" escape="false"/>\'  id=\'return_<s:property value="#iterStatus.index"/>\' tabindex=\'<s:property value="(#iterStatus.count)+220"/>\' ><s:text name="Return_to_Order"/></a>',
				</s:if>
				<s:else>
				'',
				</s:else>
    </s:if>
    <s:else>
                '', '', '',
    </s:else>  
          
			'<s:property value="#unitOfMeasure"/>','<s:property value="#shortDesc"/>',
			 '<tr><td>',  
				'','','','',
				'</td></tr>',       ]}
			<s:if test="#iterStatus.last == false ">,</s:if>
	</s:iterator>
};

--></script>	
		</s:if> <s:else>
			<table>
				<tr>
					<td align="center" class="itemContentLeft"><span
						class="headerText"><s:text
						name='No_comparisonSet_available' /></span></td>
				</tr>
			</table>
		</s:else></div>
	</s:form>
	<p>&nbsp;</p>
	</div>
	
	<!-- // table list end -->
	<!-- // container end -->
</div>
</div>
<!-- end container  -->
<!-- BEGIN footer -->
<!-- <div class="t1-footer commonFooter" id="t1-footer">
	<div id="footerContent"><s:action name="footer"
		executeResult="true" namespace="/common" /></div>
</div>   -->
<!-- added for jira  3222 -->
<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
<!-- // footer end --> <swc:dialogPanel title='Add To Cart Result'
	isModal="true" id="addToCartFailure">
	<div class="x-hidden dialog-body " id="modalDialogPanel1Content">
	<div class="dialog-body" id="ajax-body-1"></div>
	<script type="text/javascript">

        Ext.onReady(function () {
            var w = new Ext.Window({
                autoScroll: true,
                closeAction: 'hide',
                cls: 'swc-ext',
                contentEl: 'modalDialogPanel1Content',
                hidden: true,
                id: 'modalDialogPanel1',
                modal: true,
                width: '600',
                height: 'auto',
                resizable: true,
                shadow: 'drop',
                shadowOffset: 10,
                title: 'Add To Cart Result'
            });
        });
    </script>
</swc:dialogPanel>


<swc:dialogPanel title="Check Item Availability or Add to Cart"
	isModal="true" id="addToCart">
	<s:form action="xxx" name="addToCart" id="addToCartForm"
		namespace="/xxx" method="POST">
		<table>
			<tr>
				<td>
				<div id="add-to-cart-grid"></div>
				</td>
			</tr>
			<tr>
				<td >
				</td>
			</tr>
		</table>
		<div id="availibility-grid"></div>
		<table>
			<tr>
				<td><input type="button" id="UpdateAvailability"
					value="Update Availability" class="submitBtnBg1"
					onClick="javascript:checkUpdateAvailability();" /></td>
			</tr>
		</table>
		<s:hidden name='#action.name' id='xx' value='xx' />
		<s:hidden name='#action.namespace' value='/xx' />
	</s:form>
</swc:dialogPanel>

<swc:dialogPanel title="Check Item Availability or Add to Cart"
	isModal="true" id="addToCart1">
	<div id="availibility-grid_error"></div>
</swc:dialogPanel>
<swc:dialogPanel title="Bracket Pricing" isModal="true"
	id="bracketPricingDialog" cssClass="my-class">

	<div id="Price_Grid"></div>

</swc:dialogPanel>
<!-- for jira 2422 killing the session in item details --> <%--s:set name='ItemDetailLastPageUrl' value='<s:property value=null />' scope='session'/> --%>

<!-- // main end -->
</body>
</html>
