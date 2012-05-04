 <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="swc" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs’. 
    This is to avoid a defect in Struts that’s creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts’ OGNL statements. --%>
<s:set name='_action' value='[0]'/>
<s:set name='ctxt' value="#_action.getWCContext()" />

<swc:html isXhtml="true">
<head>
 <title>
    <s:property value="wCContext.storefrontId" /> - <s:text name="CustomerProfile"/>
 </title>
<swc:head/>
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/user/my-account.css" />
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/profile/profile.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/swc/js/profile/org/corporateInfo.js"></script>


</head>
<s:set name='xmlUtil' value="#_action.getXMLUtils()" />
<s:set name='sdoc' value="outputDoc" />
<s:set name='extnElem' value='#xmlUtil.getChildElement(#sdoc, "Extn")'/>


<body>

<div id="navigate">
</div>
<div id="main">


<!-- begin Common header -->
<div class="t2-header commonHeader" id="headerContainer">
      <!-- add content here for header information -->
      <s:action name="header" executeResult="true" namespace="/common" />
</div><!-- //Common header end -->

<!-- Begin Navigation include Tab -->
<s:action name="NavigationTab" executeResult="true" namespace="/profile/org" >
	<s:param name="navSelectedTab">UpdateOrgProfile</s:param>
</s:action>
<!-- End Navigation include Tab -->
 <div class="container"> <!-- // begin t2-product-list -->
 <div class="t2-mainContent" id="t2-mainContent"><!-- add content here for main content -->


<!-- Begin Customer include Tab -->
<s:action name="customerNavigationTab" executeResult="true"	namespace="/profile/org">
	<s:param name="selectedTab">CorporateInfo</s:param>
	<s:param name="customerId" value='#xmlUtils.getAttribute(#sdoc,"CustomerID")'/>
  	<s:param name="organizationCode" value='#xmlUtils.getAttribute(#sdoc,"OrganizationCode")'/>
</s:action> <!-- End include Tab -->

 <!-- Actual tab content starts from here -->
   <div class="tabContent">

        <div>
		    <table class="listTableHeader2" id="listTableHeader_x1">
		        <tr>
		            <td><span class="listTableHeaderText">
		            			<s:text name="BusinessRules"/>
		            	</span></td>
		        </tr>
		    </table>
    	</div>
    	<div>
    		<table class="listTableBody3 padding-left3" width="96%" >
				<tr>
					<td><s:checkbox name='AcceptPriceORFlag' fieldValue="true" value="%{#_action.IsAcceptPriceORFlag()}"/></td>
					<td><s:text name="Accept Price Override "	/></td>
				</tr>
				<tr>
					<td><s:checkbox name='PrvntAutoOrdersFlag' fieldValue="true" value="%{#_action.IsPrvntAutoOrdersFlag()}"/></td>
					<td><s:text name="Prevent orders from being placed automatically on ordering system"	/></td>
				</tr>
				<tr>
					<td><s:checkbox name='POAckFlag' fieldValue="true" value="%{#_action.IsPOAckFlag()}"/></td>
					<td><s:text name="Send PO Acknowledgment"	/></td>
				</tr>																									
			</table>
    	</div>
    	
        <div>
		    <table class="listTableHeader2" id="listTableHeader_x1">
		        <tr>
		            <td><span class="listTableHeaderText">
		            			<s:text name="Order Rules"/>
		            	</span></td>
		        </tr>
		    </table>
    	</div>
    	<div>
    		<table class="listTableBody3 padding-left3" width="96%" >
				<tr>
					<td><s:checkbox name='CustHeaderCommentsFlag' fieldValue="true" value="%{#_action.IsCustHeaderCommentsFlag()}"/></td>
					<td><s:text name="Header comments were sent by customer"	/></td>
				</tr>
				<tr>
					<td><s:checkbox name='DupPOFlag' fieldValue="true" value="%{#_action.IsDupPOFlag()}"/></td>
					<td><s:text name="Do Not Allow Duplicate POs "	/></td>
				</tr>
				<tr>
					<td><s:checkbox name='NonStdShipFlag' fieldValue="true" value="%{#_action.IsNonStdShipFlag()}"/></td>
					<td><s:text name="Non-Standard Ship Method"	/></td>
				</tr>		
				<tr>
					<td><s:checkbox name='UseCustAmtLimitFlag' fieldValue="true" value="%{#_action.IsUseCustAmtLimitFlag()}"/></td>
					<td><s:text name="Use Customer Min/Max order amount "	/></td>
				</tr>
				<tr>
					<td><s:checkbox name='CustShipCompleteFlag' fieldValue="true" value="%{#_action.IsCustShipCompleteFlag()}"/></td>
					<td><s:text name="Customer selected ship complete "	/></td>
				</tr>
				<tr>
					<td><s:checkbox name='ValSTZipFlag' fieldValue="true" value="%{#_action.IsValSTZipFlag()}"/></td>
					<td><s:text name="Validate ship-to zip code"	/></td>
				</tr>
				<tr>
					<td><s:checkbox name='ShipNotNextBusDayFlag' fieldValue="true" value="%{#_action.IsShipNotNextBusDayFlag()}"/></td>
					<td><s:text name="Ship date not next business day"	/></td>
				</tr>
				<tr>
					<td colspan="2">
						<table>
						<tr>
							<td colspan="2"><s:text name="Stock Check Option:"	/></td>
						</tr>
						<tr> 
							<td>
								<s:radio tabindex="" cssStyle="vertcal-align:center" list="#{'':#_action.getText('None')  }"
									   name="StockCheckOption" id="StockCheckOption" 
									   value='%{#extnElem.getAttribute("ExtnStockCheckOption")}'>
								</s:radio>
							</td> 
						</tr>
						<tr> 
							<td>
								<s:radio tabindex="" cssStyle="vertcal-align:center" list="#{  '03':#_action.getText('GroupIntoFewShipmentsAsPossible') }"
									   name="StockCheckOption" id="StockCheckOption" 
									   value='%{#extnElem.getAttribute("ExtnStockCheckOption")}'>
								</s:radio>
							</td> 
						</tr>
						<tr> 
							<td>
								<s:radio tabindex="" cssStyle="vertcal-align:center" list="#{ '01':#_action.getText('FasterShipAsTheyAreAvailableMayIncurAdditionalCost')+'<BR>'}"
									   name="StockCheckOption" id="StockCheckOption" 
									   value='%{#extnElem.getAttribute("ExtnStockCheckOption")}'>
								</s:radio>
							</td> 
						</tr>												
					
						</table>
					</td>
				</tr>																																
			</table>
    	</div>    	   
    	
        <div>
		    <table class="listTableHeader2" id="listTableHeader_x1">
		        <tr>
		            <td><span class="listTableHeaderText">
		            			<s:text name="Order Line Rules"/>
		            	</span></td>
		        </tr>
		    </table>
    	</div>   
    	<div>
    		<table class="listTableBody3 padding-left3" width="96%" >
				<tr>
					<td><s:checkbox name='CustLineCommentsFlag' fieldValue="true" value="%{#_action.IsCustLineCommentsFlag()}"/></td>
					<td><s:text name="Line comments were sent by customer"	/></td>
					<td></td>
				</tr>
				<tr>
					<td><s:checkbox name='CustLineAccNoFlag' disabled="true" fieldValue="true" value="%{#_action.IsCustLineAccNoFlag()}"/></td>
					<td><s:text name="Customer Line Account No: "	/></td>
					<td><s:textfield id='JobNoLabel' 
						readonly="%{!#_action.IsCustLineAccNoFlag()}"
						name='JobNoLabel' size="25" tabindex="" value='%{#extnElem.getAttribute("ExtnCustLineAccLbl")}'/>
					</td>
				</tr>
				<tr>
					<td><s:checkbox name='CustLineSeqNoFlag' disabled="true" fieldValue="true" value="%{#_action.IsCustLineSeqNoFlag()}"/></td>
					<td><s:text name="Customer Line Sequence No."	/></td>
					<td></td>
				</tr>
				<tr>
					<td><s:checkbox name='CustLinePONoFlag' disabled="true" fieldValue="true" value="%{#_action.IsCustLinePONoFlag()}"/></td>
					<td><s:text name="Customer Line PO No."	/></td>
					<td></td>
				</tr>
				<tr>
					<td><s:checkbox name='CustLineField1Flag' disabled="true" fieldValue="true" value="%{#_action.CustLineField1Flag()}"/></td>
					<td><s:text name="Customer field 1: "	/></td>
					<td><s:textfield id='CustLineField1Label' 
						readonly="%{!#_action.CustLineField1Flag()}"
						name='CustLineField1Label' size="25" tabindex="" value='%{#extnElem.getAttribute("ExtnCustLineField1Label")}'/>
					</td>
				</tr>
				<tr>
					<td><s:checkbox name='CustLineField2Flag' disabled="true"  fieldValue="true" value="%{#_action.CustLineField2Flag()}"/></td>
					<td><s:text name="Customer field 2: "	/></td>
					<td><s:textfield id='CustLineField2Label' 
						readonly="%{!#_action.CustLineField2Flag()}"
						name='CustLineField2Label' size="25" tabindex="" value='%{#extnElem.getAttribute("ExtnCustLineField2Label")}'/></td>
				</tr>
				<tr>
					<td><s:checkbox name='CustLineField3Flag' disabled="true"  fieldValue="true" value="%{#_action.CustLineField3Flag()}"/></td>
					<td><s:text name="Customer field 3: "	/></td>
					<td><s:textfield id='CustLineField3Label' 
						readonly="%{!#_action.CustLineField3Flag()}"
						name='CustLineField3Label' size="25" tabindex="" value='%{#extnElem.getAttribute("ExtnCustLineField3Label")}'/></td>
				</tr>
				<tr>
					<td><s:checkbox name='LineDelDateMatchFlag' fieldValue="true" value="%{#_action.IsLineDelDateMatchFlag()}"/></td>
					<td><s:text name="All Requested Line Delivery Dates do not match"	/></td>
					<td></td>
				</tr>
				<tr>
					<td><s:checkbox name='PrvntBOFlag' fieldValue="true" value="%{#_action.IsPrvntBOFlag()}"/></td>
					<td><s:text name="Prevent Backorders"	/></td>
					<td></td>
				</tr>
				<tr>
					<td><s:checkbox name='ItemNotAvlNextDayFlag' fieldValue="true" value="%{#_action.IsItemNotAvlNextDayFlag()}"/></td>
					<td><s:text name="Items not available for next day shipment "	/></td>
					<td></td>
				</tr>	
				<tr>
					<td><s:checkbox name='CustLineLvlCodeFlag' fieldValue="true" value="%{#_action.IsCustLineLvlCodeFlag()}"/></td>
					<td><s:text name="Line level code was sent by customer"	/></td>
					<td></td>
				</tr>
				<tr>
					<td><s:checkbox name='PrvntPriceBelowCostFlag' fieldValue="true" value="%{#_action.IsPrvntPriceBelowCostFlag()}"/></td>
					<td><s:text name="Prevent price below cost"	/></td>
					<td></td>
				</tr>
				<tr>
					<td><s:checkbox name='PriceDiscrpFlag' fieldValue="true" value="%{#_action.IsPriceDiscrpFlag()}"/></td>
					<td><s:text name="Price Discrepancy(%)"	/></td>
					<td><s:textfield id='PercentVariance' name='PercentVariance' size="25" tabindex="" value='%{#extnElem.getAttribute("ExtnPercentVariance")}'/></td>
				</tr>																																																																
			</table>
    	</div>    	  	 					    	


   	<p>&nbsp;</p>
  </div><!-- // tab content end -->
  
        <!-- // t2-main-content end --></div>
        </div><!-- //container end -->
<div class="t2-footer commonFooter" id="t2-footer">
	<!-- add content here for footer -->
	<s:action name="footer" executeResult="true" namespace="/common" />
</div><!-- // footer end -->

</div><!-- // main end -->

       </body>
</swc:html>

