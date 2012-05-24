<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs’.
    This is to avoid a defect in Struts that’s creating contention under load.
    The explicit call style will also help the performance in evaluating Struts’ OGNL statements. --%>



<s:set name='_action' value='[0]'/>
<s:set name="xutil" value="xMLUtils"/>
<s:url id='targetURL' namespace='/common' action='xpedxGetAssignedCustomers' />
<%@ taglib prefix="swc" uri="swc" %>
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<swc:html isXhtml="true">
<head>
  <swc:head/>
  <!-- insert css and javascript links here -->
	<s:set name='sdoc' value="outputDoc"/>	
	<s:set name='orderNode' value='#util.getElement(#sdoc, "/Order")' />
  <s:set name='draftOrderFlag' value='#orderNode.getAttribute("DraftOrderFlag")'/>
	<s:set name='canChangeShipToAddress' value='#_action.isOrderModificationAllowed("SHIPTO")'/>
	<s:set name='addrtype' value='ShipTo' />
	<title>
	<s:if test='(#canChangeShipToAddress == true)'>
		<s:property value="wCContext.storefrontId" /> - <s:text name="ChooseOrderShipToAddress" />
	</s:if>
	<s:else>
		<s:property value="wCContext.storefrontId" /> - <s:text name="ShipToAddress" />
	</s:else>
	</title>
	<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/order-address.css" />
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/order.js" ></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/common/verifyAddress.js" ></script>
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/orderAddress.js" ></script>
</head>
<body>

<div id="main">
	<!-- begin header -->
	<div class="t2-header commonHeader" id="headerContainer">
		<!-- add content here for header information -->
        <s:action name="header" executeResult="true" namespace="/common">
            <s:param name='minimalMiniCart' value='%{#_action.isCartInContext()}'/>
            <s:param name='minimalMiniCartMessage' value='%{#_action.getText("TheCartContentsAreDisplayedBelow")}'/>
        </s:action>
	</div>
	<!-- // header end -->

	<div class="t2-navigate" id="navigateContainer">
    <s:action name="progressBar" executeResult="true" namespace="/order" >
      <s:param name="currentStep" value="'Ship_to'"/>
    </s:action>
	</div>
	<div class="container">
		<!-- // begin main-content -->
		<div class="t2-mainContent" id="mainContentContainer">
			<!-- add content here for main content -->
			<!-- main content header -->
			<div class="mainContentHeadContainer" id="mainContentHeadContainer1">
				<div class="mainContentHeadRow1" id="mainContentHeadRow1">
					<div class="mainContentHeadTitle padding-top3">
						<span class="headerText"><s:text name="Checkout_step_2_of_4_Enter_shipping_information" /></span>
					</div>
				</div>
			</div>
			<div class="mainContainer">
				<s:form action="saveOrderShipToAddress" namespace="/order" method="post" validate="true">
                    <s:hidden name='#action.name' value='saveOrderShipToAddress'/>
                    <s:hidden name='#action.namespace' value='/order'/>
				<div class="listTableContainer" id="productList">
					<!-- table list -->
					<div>
						<table class="listTableHeader" id="listTableHeader">
							<tr>
								<td class="padding-left3">
								<span class="listTableHeaderText">
								<s:if test='(#canChangeShipToAddress == true)'>
									<s:text name="ChooseOrderShipToAddress" />
								</s:if>
								<s:else>
									<s:text name="ShipToAddress" />
								</s:else>
								</span>
								</td>
							</tr>
						</table>
					</div>

					<div>

						<!-- get the search result xml: getDocuments is an action method -->

						<s:hidden name="OrderHeaderKey" value="%{#orderNode.getAttribute('OrderHeaderKey')}"  />
						<s:hidden name="returnURL" value="%{#_action.getReturnURL()}"  />
						<s:hidden name='draft' value='%{#draftOrderFlag}' />
						<br />
						<div>
							<table width="100%" class="listTableBody padding-left3">
							<tr>
							<td width="50%" class="top-td-align">
							<div class="padding-left3">
								<h2 class="titleText"><s:text name="Main_shipping_address_to_this_order" /></h2>
								<br />
								<div id='editableAddressDiv' class='hidden-data'>
									<s:include value="editableOrderAddress.jsp"/>
								</div>
								<div id='readOnlyAddressDiv'>
									<div id='readOnlyShipToAddressContentDiv'>
										<s:include value="../common/address/XPEDXReadOnlyAddress.jsp"/>
									</div>
									<s:if test='(#canChangeShipToAddress == true)'>
								<!-- 	<s:a href="javascript:toggleAddressBlock('saveOrderShipToAddress');"><s:text name="EditAddress"/></s:a> -->
									</s:if>
									<s:else>
										<br>
										<s:set name='hasMultipleAddress' value="hasChildLevelAddress"/>
									<!--  	<s:checkbox name="shipMultipleAddress" fieldValue="true" value='%{#hasMultipleAddress == "Y"}' />-->
										<s:text name="Ship_to_multiple_addresses"/>
									</s:else>
									<s:a href="javascript:showAssignedShipTo('%{#targetURL}')">Select ShipTo</s:a>
								</div>
								<div id="verifyaddrDiv" class="pageErrorMessage"></div>
							</div>
							</td>
							<td width="50%" class="top-td-align">
							<div>							
							</div>
							</td>
							</tr>
							</table>
						</div>
						<br />
					</div>
					<!-- end of listTableBody-->
					<br />
				</div>
				<!-- end of listTableContainer-->
				<br />
        <p class="order-place-nav">
          <s:set name="returnurl" value="%{#_action.getReturnURL()}"  />
          <s:set name="backurl" value="%{#_action.getBackURL()}"  />
            <s:if test='%{#returnurl == null || #returnurl == ""}'>
              <a href='<s:url action="%{#backurl}"><s:param name="orderHeaderKey" value="%{#orderNode.getAttribute('OrderHeaderKey')}" />
			  <s:param name="draft" value='%{#draftOrderFlag}' />
			  </s:url>' class="submitBtnBg1"><s:text name="Back_btn" /></a>
            </s:if>
            <s:else>
              <a href="<s:property value='#returnurl' />" class="submitBtnBg1"><s:text name="Back_btn" /></a>
            </s:else>
            <input type="submit" value="<s:text name='Continue_btn' />" class="submitBtnCheckout"
                onClick="saveAddressInfo(this.form,'<s:url action="saveOrderShipToAddress"/>','<s:url value="/common/verifyAddress.action"/>','verifyaddrDiv'); return false" />
        </p>
				</s:form>

		</div>
		<br />
		<!-- //end of main content -->
	</div>
	</div>
	<!-- end of container -->

	<!-- begin footer -->
	<div class="t2-footer commonFooter" id="footerContainer">

		<div id="footerContent">
		<!-- add content here for footer -->
		<s:action name="footer" executeResult="true" namespace="/common" />
		</div>
	</div>
	<!-- // footer end -->
</div>
<!-- end of main -->

</body>
</swc:html>
