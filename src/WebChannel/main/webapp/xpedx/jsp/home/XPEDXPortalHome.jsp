<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="swc" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>  
<!-- styles -->



<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/global-1.<s:property value='#wcUtil.xpedxBuildKey' />css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/swc.min<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/home/home<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/common/header<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/home/portalhome<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/catalog/catalogExt<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/styles<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<s:include value="../common/XPEDXStaticInclude.jsp"/>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/global/swc_002<s:property value='#wcUtil.xpedxBuildKey' />.css"/>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx-mil<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx-dan<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx-forms<s:property value='#wcUtil.xpedxBuildKey' />.css" />
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/theme/xpedx-dan<s:property value='#wcUtil.xpedxBuildKey' />.css" />



<!-- javascript -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/swc<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/draftOrderList<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.core<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<link type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/themes/base/jquery.ui.all<s:property value='#wcUtil.xpedxBuildKey' />.css" rel="stylesheet" />
<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/bgiframe_2.1.1/jquery.bgiframe.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min<s:property value='#wcUtil.xpedxBuildKey' />.js" type="text/javascript" charset="utf-8"></script>
<!-- /STUFF -->
<!-- Lightbox/Modal Window -->
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />
<link rel="stylesheet" type="text/css" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/css/smoothness/jquery-ui-1.8.2.custom<s:property value='#wcUtil.xpedxBuildKey' />.css" media="screen" />

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui-1/development-bundle/ui/jquery.ui.widget<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.ui.datepicker<s:property value='#wcUtil.xpedxBuildKey' />.js"></script>

<script type="text/javascript">
          $(document).ready(function () {
           
              $("#various2").fancybox({
      			'titleShow'			: false,
      			'transitionIn'		: 'fade',
      			'transitionOut'		: 'fade',
      			'titlePosition' : 'inside',
      			'transitionIn' : 'none',
      			'transitionOut' : 'none',
      			//added for clearing the copycart name and copycartdescription fields
    			'onClosed'	: function(){$("#copyCartName").val('');$("#copyCartDescription").val('');}
      								
      		});

          }); 
 </script> 
			<script type="text/javascript">
			
			function linkedRowToggle(parentID){
				var ele = document.getElementById('ChildOf_'+parentID);
				switch(ele.style.display){
				case 'none':
					ele.style.display = 'table-row-group';
					break;
				case 'table-row-group':
					ele.style.display = 'none';
					break;
				}									
			}
</script>
<%-- <title><s:text name="orders.title" /></title> --%>
<title><s:text name="MSG.SWC.ORDR.ORDR.GENERIC.TABTITLE" /></title> 

</head>
<body class="ext-gecko ext-gecko3">

<s:set name='ctxt' value="wCContext" />
<s:set name='isGuestUser' value="wCContext.guestUser()" />
<s:set name='approvalWidgetPanel' value='"/swc/order/approvals"'/>
<s:set name='orderWidgetPanel' value='"/swc/order/authenticatedOrdering"'/>

	<s:url id="approvalListURL" action="approvalList" namespace="/order">
		<s:param name="pageNumber" value="1"/>
		<s:param name="searchFieldName" value=""/>
	    <s:param name="searchFieldValue" value=""/>
	</s:url>
	
	<s:url id="orderlistUrl" action="orderList" namespace="/order">
 	</s:url>
 	
 	<s:url id="orderListWidgetSortURL" action="portalHome" namespace="/home">
    	<s:param name="orderByAttribute" value="'{0}'"/>
    	<s:param name="orderDesc" value="'{1}'"/>
	</s:url>
	
<div id="main-container">
<!-- begin t1-header -->
	<div id="main">

		<s:action
			name="xpedxHeader" executeResult="true" namespace="/common" />
		

<!-- // t1-header end -->

<div class="container">

	<!-- breadcrumb -->
		<div id="searchBreadcrumb" class="page-title"> <a href="<s:url action="home" namespace="/home" includeParams='none'/>">Home</a> / 
		<span class="breadcrumb-inactive"><s:text name="orders.title" /></span> 
		<a href="javascript:window.print()"> <span class="print-ico-xpedx"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/common/print-icon<s:property value='#wcUtil.xpedxBuildKey' />.gif" width="16" height="15" alt="Print Page " />Print page </span></a> </div>

	<div id="mid-col-mil">

		
		<h2 id="orders">  Recent Approvals<span class="orange-order"> <s:a href="%{approvalListURL}">Show All of My Approvals</s:a></span> </h2>
		<s:if test='#isGuestUser != true'>
    		<s:if test="@com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer@getInstance().isAuthorized(#approvalWidgetPanel,#ctxt)">
    			<s:action name="approvalWidget" executeResult="true" namespace="/order" />

    		</s:if>
    	</s:if>
	
	<div class="mil-bot"></div>
    <div class="clearall"></div><br />
    	<h2 id="orders"> Recent Orders <span class="orange-order"> <s:a href="%{orderlistUrl}">Show All of My Recent Orders</s:a></span> </h2>
    	<s:if test='#isGuestUser != true'>
    		<s:if test="@com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer@getInstance().isAuthorized(#orderWidgetPanel,#ctxt)">
    			<s:action name="orderListWIdget" executeResult="true" namespace="/order" >
					<s:param name="orderByAttribute" value="OrderDate"/>
					<s:param name="orderDesc" value="Y"/>
				</s:action>
			</s:if>
    	</s:if>
    <div class="mil-bot"></div>
	<div class="clearall"></div><br />
	
		<h2 id="orders"> Recent Carts <span class="orange-order"> 
		<a href="<s:url action="draftOrderList" namespace="/order" />">Show All of My Carts</a></span> </h2>
		<s:if test='#isGuestUser != true'>
    		<s:if test="@com.sterlingcommerce.webchannel.core.wcaas.ResourceAccessAuthorizer@getInstance().isAuthorized(#approvalWidgetPanel,#ctxt)">
    			<s:action name="xpedxCartListWidget" executeResult="true" namespace="/order" />
    		</s:if>
    	</s:if>
    <div class="mil-bot"></div>
	<div class="clearall"></div><br />
    <!-- // begin t1-main-content -->
	
	</div>
    <!-- // end t1-main-content -->


<!-- //t1 container end -->
	

</div>
<!-- begin t1-footer -->
		<s:action name="xpedxFooter" executeResult="true" namespace="/common" />	
<!-- // t1-footer end -->
</div>
</div>
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

<a href="#copyCartNameDlg" id="various2" style="display:none"></a> 
<div style="display: none;">
	     	<div id='copyCartNameDlg' style="width:500px;height:350px;overflow:auto;">
	     		 <s:form name="copyOrder" id="copyOrder" method="post" action="draftOrderCopy" namespace="/order" validate="true">
			       <s:hidden name="OrderHeaderKey" id="OrderHeaderKey" value=""/>
			       <s:hidden name=''/>
				
					<div class="xpedx-light-box" style="width:340px; height:300px;">
				        <h2>Copy Cart</h2> 
				        <p class="mil-edit-forms-label">Cart Name:</p>
				        <input value="" name="copyCartName" id="copyCartName" size="35" class="x-input"/> 
				        <p class="mil-edit-forms-label">Description:</p>
				        <textarea name="copyCartDescription" id="copyCartDescription" cols="45" rows="5" class="x-input"></textarea>
				        <ul id="tool-bar" class="tool-bar-bottom">
				        	<li style="float: left;"><a class="green-ui-btn" href="javascript:(function(){document.copyOrder.submit();})();"><span>Save</span></a></li>
				        </ul>
			    	</div> 
					
			    </s:form>
	     	</div> 
	     </div>
<!-- // main end -->


</body>
</html>
