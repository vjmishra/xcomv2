<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<%request.setAttribute("isMergedCSSJS","true");%>
<html class="ext-strict" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml" lang="en">

<s:set name='_action' value='[0]'/>
<s:url id='getUsersURL' namespace='/order' action='getCustomerUsers'/>

<head>
<!-- START swc:head -->

<!--cssMode=minified-->
<!--jsMode=minified-->
<!-- START wctheme.head.ftl -->
<!-- END head.ftl -->

<meta name="webapp-context" content="/swc" />
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<!-- Webtrends tag starts -->
<meta name="WT.ti" content='<s:text name="draftorderlist.title"/>'/>
<!-- Webtrends tag stops -->
<!-- styles -->

<!-- styles -->
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/GLOBAL.css" />
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/order/ORDERS.css" />

<!-- jQuery Base & jQuery UI -->
<script type="text/javascript" src="../xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="../xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="../xpedx/js/swc.js"></script>
<script type="text/javascript" src="../xpedx/js/xpedx.swc.min.js"></script>
<script type="text/javascript" src="../xpedx/js/order/draftOrderList.js"></script>
<script type="text/javascript" src="/swc/xpedx/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="../xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
<script type="text/javascript" src="../xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="../xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="../xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget.js"></script>
<!-- Web Trends tag start -->
<script type="text/javascript" src="/swc/xpedx/js/webtrends/displayWebTag.js"></script>
<!-- Web Trends tag end  -->

<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="../xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified.js" type="text/javascript" charset="utf-8"></script>
<script src="../xpedx/js/jquery-tool-tip/jquery.bgiframe.min.js" type="text/javascript" charset="utf-8"></script><!-- jira 2128 path modified -->
<!--[if IE]><script src="../other_libs/excanvas_r3/excanvas.js" type="text/javascript" charset="utf-8"></script><![endif]-->
<script src="../xpedx/js/jquery-tool-tip/jquery.bt.min.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF -->
<!-- Lightbox/Modal Window -->
<script type="text/javascript" src="../xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
<script type="text/javascript" src="../xpedx/js/fancybox/jquery.fancybox-1.3.4.js"></script>

<script type="text/javascript">

	$(document).ready(function() {
		$(document).pngFix();		
		$("#various1,#createCart2").fancybox({
			
			'titleShow'			: false,
			'transitionIn'		: 'fade',
			'transitionOut'		: 'fade',
			'titlePosition' : 'inside',
			'transitionIn' : 'none',
			'transitionOut' : 'none',
			//added for clearing the cart name and description fields			
			'onClosed'	: function(){$("#newcartname").val('');$("#orderDescription").val('');}
								
		});
		$("#various2").fancybox({
			'titleShow'			: false,
			'transitionIn'		: 'fade',
			'transitionOut'		: 'fade',
			'titlePosition' : 'inside',
			'transitionIn' : 'none',
			'transitionOut' : 'none',
			//added for clearing the copycart name and copycartdescription fields
			'onClosed'	: function(){$("#copyCartName").val('');$("#copyCartDescription").val('');$("#cp-cart-err-msg").hide();}
								
		});
		$('#Availability_Hover').bt({
			ajaxPath: '../tool-tips/cart-availability-hover.html div#tool-tip-content',
			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
			fill: '#ebebeb',
			cssStyles: {color: 'black'},
			padding: 20,
			spikeLength: 10,
  			spikeGirth: 15,
			cornerRadius: 6,
			shadow: true,
			shadowOffsetX: 0,
			shadowOffsetY: 3,
			shadowBlur: 3,
			shadowColor: 'rgba(0,0,0,.4)',
			shadowOverlap: false,
			strokeWidth: 1,
  			strokeStyle: '#FFFFFF',
			noShadowOpts:     {strokeStyle: '#969696'},
			positions: ['top']
		});
        $("#xpedxDeleteCartDialog1").fancybox({
			'titleShow'			: false,
			'transitionIn'		: 'fade',
			'transitionOut'		: 'fade',
			'titlePosition' : 'inside',
			'transitionIn' : 'none',
			'transitionOut' : 'none',
			//added for clearing the copycart name and copycartdescription fields
			'onClosed'	: function(){$("#copyCartName").val('');$("#copyCartDescription").val(''); $("#otherCartActions").val('None'); }
								
		});
	});

</script>
<script type="text/javascript">
function maxLength(field,maxlimit) {
    	if (field.value.length > maxlimit) // if too long...trim it!
    		alert(field.title + ' should not exceed '+maxlimit+ ' characters');
    		field.value = field.value.substring(0, maxlimit);
    		return false;
    	}
function maxNewLength(field,maxlimit)
{
	if (field.value.length > maxlimit) // if too long...trim it!
	//commeneted for 3098	
	//alert(field.title + ' should not exceed '+maxlimit+ ' characters');
		field.value = field.value.substring(0, maxlimit);
		return false;
}

	function saveNewCart() {
		if( $.trim($("#newcartname").val()).length > 0) {
		//-- Web Trends tag start --
			writeMetaTag("DCSext.w_x_cart_new", "1");	
		//-- Web Trends tag end --
			$("#cr-cart-err-msg").hide();
			document.createOrder.submit();
		/*	if( $.trim($("#orderDescription").val()).length > 0) 
			{		
				$("#cr-cart-err-msg1").hide();
				document.createOrder.submit();
			}
		else
			{
			
			$("#cr-cart-err-msg1").show();
			}	*/
			
		} else {
			$("#cr-cart-err-msg").show();
		}
	}
	function copyNewCart() {
		if( $.trim($("#copyCartName").val()).length > 0) {
			$("#cp-cart-err-msg").hide();
			document.copyOrder.submit();	
			/*if( $.trim($("#copyCartDescription").val()).length > 0) 
				{
				$("#cp-cart-err-msg1").hide();
				 document.copyOrder.submit();
				}
			  else
				{
				$("#cp-cart-err-msg1").show();
				} commented for 2919 - cart description not a required field	*/	
		} else {

			$("#cp-cart-err-msg").show();
		}
	}
	</script>
<title><s:property value="wCContext.storefrontId" /> - <s:text name="draftorderlist.title"/></title>

<s:url id="draftOrderListSortURL" action="draftOrderList" >
    <s:param name="orderByAttribute" value="'{0}'"/>
    <s:param name="orderDesc" value="'{1}'"/>
    <s:param name="pageNumber" value="%{pageNumber}"/>
    <s:param name="searchFieldName" value="SearchFieldName"/>
    <s:param name="searchFieldValue" value="SearchFieldValue"/>
    <s:param name="OrderNameValue" value="OrderNameSearchValue"/>
    <s:param name="ProductIdValue" value="ProductIdSearchValue"/>
    <s:param name="OwnerFirstNameValue" value="OwnerFirstNameSearchValue"/>
    <s:param name="OwnerLastNameValue" value="OwnerLastNameSearchValue"/>
    <s:param name="createTSFrom" value="CreateTSFrom"/>
    <s:param name="modifyTSFrom" value="ModifyTSFrom"/>
    <s:param name="createTSTo" value="CreateTSTo"/>
    <s:param name="modifyTSTo" value="ModifyTSTo"/>
</s:url>
<s:url id="draftOrderListPaginationURL" action="draftOrderList">
    <s:param name="orderByAttribute" value="orderByAttribute"/>
    <s:param name="orderDesc" value="orderDesc"/>
    <s:param name="pageNumber" value="'{0}'"/>
    <s:param name="searchFieldName" value="SearchFieldName"/>
    <s:param name="searchFieldValue" value="SearchFieldValue"/>
    <s:param name="OrderNameValue" value="OrderNameSearchValue"/>
    <s:param name="ProductIdValue" value="ProductIdSearchValue"/>
    <s:param name="OwnerFirstNameValue" value="OwnerFirstNameSearchValue"/>
    <s:param name="OwnerLastNameValue" value="OwnerLastNameSearchValue"/>    
    <s:param name="createTSFrom" value="CreateTSFrom"/>
    <s:param name="modifyTSFrom" value="ModifyTSFrom"/>
    <s:param name="createTSTo" value="CreateTSTo"/>
    <s:param name="modifyTSTo" value="ModifyTSTo"/>
</s:url>
<s:url id="draftOrderListAdvancedSearchURL" escapeAmp="false" action="draftOrderAdvancedSearch">
    <s:param name="searchFieldName" value="SearchFieldName"/>
    <s:param name="searchFieldValue" value="SearchFieldValue"/>
    <s:param name="OrderNameValue" value="OrderNameSearchValue"/>
    <s:param name="ProductIdValue" value="ProductIdSearchValue"/>
    <s:param name="OwnerFirstNameValue" value="OwnerFirstNameSearchValue"/>
    <s:param name="OwnerLastNameValue" value="OwnerLastNameSearchValue"/>    
    <s:param name="createTSFrom" value="CreateTSFrom"/>
    <s:param name="modifyTSFrom" value="ModifyTSFrom"/>
    <s:param name="createTSTo" value="CreateTSTo"/>
    <s:param name="modifyTSTo" value="ModifyTSTo"/>
</s:url>

<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util'/>
<!-- get the search result xml: getDocuments is an action method -->
<s:set name='sdoc' value="outputDoc"/>
<s:set name='desc' value="#_action.getOrderDesc()"/>
<style type="text/css">
.wid210{
width:210px;
}
.wid102{
width:102px;
}
/* This is required as Simple Table builder doesnt allow width as the parameter */
/* but class can be introduced with style property */
#mil-carts-list
{
width: 950px;
}
span.underlink:hover {
	text-decoration: underline !important;
}

</style>

</head>
<!-- END swc:head -->
<body class="  ext-gecko ext-gecko3">

<div id="main-container">
    <div id="main">
       
        <s:action name="xpedxHeader" executeResult="true"
		namespace="/common" />
            <div class="container">
            	<!--  <div id="breadcumbs-list-name" class="page-title">My Carts </div> --> 
            	 <div id="breadcumbs-list-name" class="page-title"> <s:text name='MSG.SWC.CART.CARTLIST.GENERIC.PGTITLE' /> </div> 
                <div id="mid-col-mil">
                 <div id="tool-bar-bottom" style="margin-top: 10px; padding-bottom: 5px;">
                 <!-- Webtrends Tag starts -->
     				<script type="text/javascript">
     				var tag = "WT.ti,DCSext.w_x_cart_em";
					var content = "Create New Cart,1";
     			</script>
     			
     			     <span class="float-left">
     			     	<img height="20" class="cart-image" width="20" title="Active Cart" alt="active cart" 
     			     		src="/swc/xpedx/images/theme/theme-1/20x20_blue_cart_on.png"  > 
     			     		<!-- is your active cart. --> 
     			     		<s:text name='MSG.SWC.CART.CARTLIST.INFO.ACTIVECART' />
     			     </span> 
		          	<s:a cssClass="orange-ui-btn modal float-right-imp" id="various1"   href="#createNewCartDlg" 
                 	onclick="javascript: writeMetaTag(tag,content,2);" title="Create New Cart"><span>Create New Cart</span></s:a>
                 </div> 
                <br />
                <div class="clear">&nbsp;</div>
   				 <p class="search-pagination-top" align="right" style="float: right;">
                 	<s:if test="%{totalNumberOfPages>1}">Page</s:if>&nbsp;&nbsp;<swc:pagectl currentPage="%{pageNumber}" lastPage="%{totalNumberOfPages}" showFirstAndLast="False" urlSpec="%{#draftOrderListPaginationURL}"/>
                 </p>
                <div class="clear">&nbsp;</div>
               
                <swc:sortctl sortField="%{orderByAttribute}" sortDirection="%{orderDesc}" down="Y" up="N" urlSpec="%{#draftOrderListSortURL}">
                    <s:set name="iter" value="#util.getElements(#sdoc, '//Page/Output/XPXOrderListViewList/XPXOrderListView')"/>
                    <s:action name="xpedxBuildSimpleTable" executeResult="true" namespace="/common" >
                        <s:param name="id" value="'mil-carts-list'"/>
                        <s:param name="summary" value="'Draft Order List Table'"/>
                        <s:param name="cssClass" value="'standard-table'" />
                        <s:param name="iterable" value="#iter"/>
                       
                        <s:param name="columnSpecs[0].label" value="'Name'"/>
                         <s:param name="columnSpecs[0].align" value="''"/>
                        <s:param name="columnSpecs[0].dataField" value="'OrderName'"/>
                        <s:param name="columnSpecs[0].labelCssClass" value="'table-header-bar-left width-460'"/>
                        <s:param name="columnSpecs[0].fieldCssClass" value="''"/>
                        <s:param name="columnSpecs[0].sortable" value="'true'"/>
                        <s:param name="columnSpecs[0].dataCellBuilder" value="'xpedxDraftOrderListOrderNameAnchor'"/>
		    			<s:param name="columnSpecs[0].dataCellBuilderProperties['namespace']" value="'/order'"/>
		    			
                        <s:param name="columnSpecs[1].label" value="' Last Modified By'"/>
                        <s:param name="columnSpecs[1].align" value="'center'"/>
                        <s:param name="columnSpecs[1].dataField" value="'OrderedByName'"/>
                        <s:param name="columnSpecs[1].labelCssClass" value="'width-140'"/>
                        <s:param name="columnSpecs[1].sortable" value="'true'"/>
                        <s:param name="columnSpecs[1].fieldCssClass" value="'createdby-lastmod'"/>
                        <%--
                        <s:param name="columnSpecs[1].dataCellBuilder" value="'xpedxDraftOrderListOwnerNameAnchor'"/>
						<s:param name="columnSpecs[1].dataCellBuilderProperties['namespace']" value="'/order'"/>
						 --%>
                        <s:param name="columnSpecs[2].label" value="'Last Modified'"/>
                        <s:param name="columnSpecs[2].align" value="'center'"/>
                        <s:param name="columnSpecs[2].dataField" value="'Modifyts'"/>
                        <s:param name="columnSpecs[2].labelCssClass" value="' text-center'"/>
                        <s:param name="columnSpecs[2].sortable" value="'true'"/>
                        <!-- <s:param name="columnSpecs[2].fieldCssClass" value="'createdby-lastmod'"/> -->
                        <s:param name="columnSpecs[2].fieldCssClass" value="'width-100'"/> 
                       	<s:param name="columnSpecs[2].dataCellBuilderProperties['namespace']" value="'/order'"/>

						<s:param name="columnSpecs[3].label" value="''"/>
						 <s:param name="columnSpecs[3].align" value="''"/>
						<s:param name="columnSpecs[3].labelCssClass" value="'no-border-right table-header-bar-right'"/>
						<%-- <s:param name="columnSpecs[3].fieldCssClass" value="'actions'"/> --%>
                        <s:param name="columnSpecs[3].sortable" value="'false'"/>
                        <s:param name="columnSpecs[3].dataCellBuilder" value="'xpedxDraftOrderListActionAnchor'"/>
						<s:param name="columnSpecs[3].dataCellBuilderProperties['namespace']" value="'/order'"/>

                  <%--<s:param name="columnConfigResource"--%>
                      <%--value="'/com/sterlingcommerce/webchannel/order/draftOrderListColumnSpec.properties'"/>--%>
                  </s:action>
              </swc:sortctl>
                    <div id="tool-bar-bottom" class="float-right">
					<p class="search-pagination-bottom" align="right" style="float: right;">
						<s:if test="%{totalNumberOfPages>1}">Page</s:if>&nbsp;&nbsp;<swc:pagectl currentPage="%{pageNumber}" lastPage="%{totalNumberOfPages}" showFirstAndLast="False" urlSpec="%{#draftOrderListPaginationURL}"/>
					</p><br/><br/>
                    <s:a cssClass="orange-ui-btn modal float-right" id="createCart2" href="#createNewCartDlg" title="Create New Cart"><span>Create New Cart</span></s:a> 
                    </div>
                    <br />

                    <br />
                    <br />
                    <!-- <div type="text" id="example10" class="target">hover</div> -->
                    <!-- <a href="tool-tip-content.html">Tool Tip</a> -->
                </div>
                <!-- End mid-col-mil -->
                
                <div style="height:200px;"></div>
                
            </div>
        </div>
</div>
<s:set name="CreateNewCart" scope="page" value="#_action.getText('CreateNewCart')"/>
      <swc:dialogPanel title="" isModal="true" id="deleteCartDialog">
		       <span class="padding-left4 textAlignLeft">
                  <s:text name='Cart_Delete_Message'/>
         	</span>
         	<div class="clearBoth"></div>
         	<div id="deleteButtonPanelId" class="padding-all1 textAlignCenter">         	
             	<s:submit  type="button" name="yes" id ='Confirm_Yes'  key='Confirm_Yes'cssClass="submitBtnBg1" tabindex="3100" onclick="javascript:changeParameterAndValidateForm(document.getElementById('dol'),'draftOrderDelete');DialogPanel.hide('deleteCartDialog');return false;"/>
	    		<s:submit  type="button" name="no"  id ='Confirm_No'    key='Confirm_No'  cssClass="submitBtnBg1" tabindex="3101" onclick="javascript:DialogPanel.hide('deleteCartDialog'); svg_classhandlers_decoratePage();"/>
         	</div>
     </swc:dialogPanel>
     <s:hidden name="copyFlag" id="copyFlag" value=""/>
     <%--Hemantha --%>
     <s:include value="modals/XPEDXDeleteCartModal.jsp"></s:include>
     <%-- Hemantha
	 <swc:dialogPanel title="" isModal="true" id="xpedxDeleteCartDialog">
	     <s:form name="delOrder" id="delOrder" method="post" validate="true">
					<s:hidden name="OrderHeaderKey" id="OrderHeaderKey" value=""/>
					<s:hidden name="draft" id="draft" value="Y"/>
		
		       <span class="padding-left4 textAlignLeft">
                  <s:text name='Cart_Delete_Message'/>
         	</span>
         	<div class="clearBoth"></div>
         	<div id="deleteButtonPanelId" class="padding-all1 textAlignCenter">         	
             	<s:submit  type="button" name="yes" id ='Confirm_Yes' key='Confirm_Yes'cssClass="submitBtnBg1" tabindex="3100" onclick="javascript:deleteCart(this.form);DialogPanel.hide('xpedxDeleteCartDialog');return false;"/>
	    		<s:submit  type="button" name="no" id='Confirm_No' key='Confirm_No'  cssClass="submitBtnBg1" tabindex="3101" onclick="javascript:DialogPanel.hide('xpedxDeleteCartDialog'); svg_classhandlers_decoratePage(); return false;"/>
         	</div>
		</s:form>
	     </swc:dialogPanel>
	      
	     
	     <div style="display: none;">
	     	<div id='createNewCartDlg' style="width:500px;height:350px;overflow:auto;">
	     		 <s:form name="createOrder" id="createOrder" method="post" action="draftOrderCreate" validate="true">
			        <s:hidden id="action_namespace" name="#action.namespace" value="/order"/>
			        <s:hidden id="actionName" name="#action.name" value="draftOrderCreate"/>
				
					<div class="xpedx-light-box" style="width:340px; height:300px;">
				        <h2>New Cart</h2> 
						<div id="cr-cart-err-msg" style="display: none;"><h5 align="left"><b><font color="red">Error: Please enter a Cart Name.</font></b></h5></div>
				        <p class="mil-edit-forms-label">Cart Name:</p>
				        <input value="" name="OrderName" id="newcartname" size="35" class="x-input" maxlength="100"/> 
				        <p class="mil-edit-forms-label">Name:</p>
				        <textarea name="orderDescription" id="orderDescription" cols="45" rows="5" class="x-input" onkeyup="javascript:maxLength(this,'240');"></textarea>
				      	<ul id="tool-bar" class="tool-bar-bottom">
				        	<li style="float: left;"><a class="green-ui-btn" href="javascript:saveNewCart();"><span>Save</span></a></li>
				        </ul>
			    	</div> 
					
			    </s:form>
	     	</div> 
	     </div>
	     --%>
<s:include value="modals/XPEDXNewCartModal.jsp"></s:include>	     
<%--Hemantha --%>	     
<s:include value="modals/XPEDXCopyCartModal.jsp"></s:include>
<%--	     
	      <div style="display: none;">
	     	<div id='copyCartNameDlg' style="width:500px;height:350px;overflow:auto;">
	     		 <s:form name="copyOrder" id="copyOrder" method="post" action="draftOrderCopy" validate="true">
			       <s:hidden name="OrderHeaderKey" id="OrderHeaderKey" value=""/>
				
					<div class="xpedx-light-box" style="width:340px; height:300px;">
				        <h2>Copy Cart</h2> 
						<div id="cp-cart-err-msg" style="display: none;"><h5 align="left"><b><font color="red">Error: Please enter a Cart Name.</font></b></h5></div>
				        <p class="mil-edit-forms-label">Cart Name:</p>
				        <input value="" name="copyCartName" id="copyCartName" size="35" class="x-input"/> 
				        <p class="mil-edit-forms-label">Name:</p>
				        <textarea name="copyCartDescription" id="copyCartDescription" cols="45" rows="5" class="x-input"></textarea>
				        <ul id="tool-bar" class="tool-bar-bottom">
				        	<li style="float: left;"><a class="green-ui-btn" href="javascript:copyNewCart();"><span>Save</span></a></li>
				        </ul>
			    	</div> 
					
			    </s:form> 
	     	</div> 
	     </div>
	     <a href="#copyCartNameDlg" id="various2" style="display:none"></a>
 --%>	      
<!-- begin t1-footer -->

		<s:action name="xpedxFooter" executeResult="true" namespace="/common" />
		
<!-- // t1-footer end -->
<!-- end container  -->
</body>
</html>