<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="xpedx" uri="xpedx" %>
<s:set name='_action' value='[0]' />
<script type="text/javascript" >
/*
//this page sets the saved selection from display as submenu jsp
function showSavedSharedSelection()
{
	var customerPathsCB = $("div[id=divMainShareList] input[name=customerPaths]");
	for(var i=0;i<customerPathsCB.length;i++)
	{
		var currentCustomerPathCB = customerPathsCB[i];
		var sslNames = $('input[name=sslNames]');
		for(var j=0;j<sslNames.length;j++)
		{
			var currentCustomerId = sslNames[j].value;
			var controlId = currentCustomerId.replace(/-/g, '_');
			if(currentCustomerPathCB.id == 'customerPaths_'+controlId)
			{
				currentCustomerPathCB.checked = true;
				break;
			}
		}
	}	
}

$(document).ready(function() {
	showSavedSharedSelection();
});
*/
</script>

<s:set name="masterCustomer" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@MASTER_CUSTOMER_SUFFIX_TYPE"/>
<s:set name="sapCustomer" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SAP_CUSTOMER_SUFFIX_TYPE"/>
<s:set name="shipToCustomer" value="@com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants@SHIP_TO_CUSTOMER_SUFFIX_TYPE"/>
<s:set name='displayCustomerMap' value="displayCustomerMap" />
<s:set name='customerSuffixTypeMap' value="suffixTypeMap" />
<s:set name='customerShipDivisionMap' value="shipFromDivisionMap" />
<s:set name='customerPathMap' value='customerPathMap' />
<s:bean name='com.sterlingcommerce.xpedx.webchannel.MyItems.utils.XPEDXMyItemsUtils' id='XPEDXMyItemsUtils' />
<s:set name='outDoc2' 		value='%{outDoc.documentElement}' />

<s:set name='randomId' 		value="%{generateRamdomId()}"/>
<s:set name='customerId' 	value="customerId"/>
<s:set name='controlId' 	value="#customerId.replace('-', '_')"/>
<s:set name="name" 			value="%{#customerId}" />
<s:set name='customerPath' value='%{#customerPathMap.get(#customerId)}'></s:set>
<s:set name="div" value="%{#customerShipDivisionMap.get(#customerId)}"/>

<s:if test='%{#div == ""}'>
	<s:set name="div" value="%{'PEDRO'}"/>
</s:if>

<s:set name="cust2" value="#session.loggedInFormattedCustomerIDMap" />
<s:set name="custInfoMapKey" value="%{#session.loggedInCustomerID + '_' + wCContext.storefrontId}" />

<s:if test="%{#cust2!=null}" >
	<s:set name="suffixType" value="%{#customerSuffixTypeMap.get(#customerId)}"/>
	<s:set name='name' value="%{#displayCustomerMap.get(#customerId)}" />
</s:if>	

<s:set name="isBillTo" value='%{#customerId.toLowerCase().contains("b")}' />
<s:set name="chkCss" value='' />
<s:if test="%{#isBillTo}">
	<s:set name="chkCss" value="%{'visibility:hidden;'}" />	
</s:if>

<div style="padding-left: 15; position: relative; display: block;"  id='divShareList_<s:property value="#randomId"/>_base' >
	<s:set id="paginateDivID" name="paginateDivID" value="%{'divShareList_'+#randomId+'_base'}"/>
	<input type="button" class="icon-minus"	style="vertical-align:middle;" value="" onclick="collapseTheDiv('<s:property value="#customerId"/>','<s:property value="#suffixType"/>','divShareList_<s:property value="#randomId"/>_base', false, '<s:property value="#controlId"/>', '<s:property value="#customerPath"/>', '<s:property value="#name"/>', '<s:property value="#div"/>') " />
	<input type="checkbox" 	id="customerPaths_<s:property value="#controlId"/>" onclick="selectNode('<s:property value="#controlId"/>', this.checked);" name="customerPaths" value="<s:property value="#customerPath"/>" /> <s:property value="#name"/>
	<div style="display: none;" >
		<input type="checkbox" id="customerIds_<s:property value="#controlId"/>" name="customerIds" 	value="<s:property value="#customerId"/>">
		<input type="checkbox" id="customerDivs_<s:property value="#controlId"/>" name="customerDivs" 	value="<s:property value="#div"/>">
	</div>
	<SCRIPT type="text/javascript">
		createCountAndPathMap('<s:property value="#controlId"/>','<s:property value="#customerPath"/>',null,'<s:property value="#div"/>');
		updateShareListChild('<s:property value="#customerId"/>', '<s:property value="#controlId"/>');
		preSelected('<s:property value="#controlId"/>');
	</SCRIPT>

	<s:iterator id="item" value='XMLUtils.getElements(#outDoc2, "Customer")'>
		 <s:set name='id' 		value='#item.getAttribute("CustomerID")'/>
		 <s:set name="extn" value='XMLUtils.getChildElement(#item, "Extn")'/>
		 <s:set name='suffixType' value='#extn.getAttribute("ExtnSuffixType")'/>
		 <!-- <s:set name='name' 	value='#item.getAttribute("name")'/> -->
		 <s:set name='name' 	value='#item.getAttribute("CustomerID")'/>
			<s:set name="suffixType" value="%{#customerSuffixTypeMap.get(#item.getAttribute('CustomerID'))}"/>
			<s:set name='name' value="%{#displayCustomerMap.get(#id)}" />
		
		 <s:set name='type' 	value='#item.getAttribute("type")'/>
		 <s:set name='orgcode' 	value='#item.getAttribute("OrganizationCode")'/>
		 
		 <s:set name='customerId' 	value="#id"/>
		 <s:set name='controlId' 	value="#id.replace('-', '_')"/>
		 <s:set name='randomId' 	value="%{generateRamdomId()}"/>
		 <s:set name='customerPath' value='%{#customerPathMap.get(#id)}'></s:set>
		 <s:set name="div2" value="%{#customerShipDivisionMap.get(#id)}"/>
		 
		<s:if test='%{#div2 == ""}'>
			<s:set name="div2" value="%{'PEDRO'}"/>
		</s:if>

<s:if test="showRoot == 'true' ">
		<div style="padding-left: 15px; position: relative; display: block;"  id='divShareList_<s:property value="#randomId"/>_base' >
			<input type="button" class="icon-minus"  style="vertical-align:middle;" value="" onclick="collapseTheDiv('<s:property value="#customerId"/>','<s:property value="#suffixType"/>', 'divShareList_<s:property value="#randomId"/>_base', false, '<s:property value="#controlId"/>', '<s:property value="#customerPath"/>', '<s:property value="#name"/>', '<s:property value="#div"/>') "/>
			<input type="checkbox" 	id="customerPaths_<s:property value="#controlId"/>" onclick="selectNode('<s:property value="#controlId"/>', this.checked);" name="customerPaths" value="<s:property value="#customerPath"/>" /> 
			<s:property value="#name"/>
			<div style="display: none;" >
				<input type="checkbox" id="customerIds_<s:property value="#controlId"/>" name="customerIds" 	value="<s:property value="#customerId"/>">
				<input type="checkbox" id="customerDivs_<s:property value="#controlId"/>" name="customerDivs" 	value="<s:property value="#div2"/>">
			</div>
			<SCRIPT type="text/javascript">
				createCountAndPathMap('<s:property value="#controlId"/>','<s:property value="customerPath"/>',null,'<s:property value="#div2"/>');
				updateShareListChild('<s:property value="#customerId"/>', '<s:property value="#controlId"/>');
				preSelected('<s:property value="#controlId"/>');
			</SCRIPT>
</s:if>

			<div id='divShareList_<s:property value="#randomId"/>' >
				<s:set name='itemChildListElement' value="getXMLUtils().getChildElement(#item,'CustomerList')" />
				<s:set name='itemChildCount' value="#itemChildListElement.getAttribute('TotalNumberOfRecords')" />
				<s:hidden name="countCustomer" value="%{#customerId + '|' +#_action.getCountCustomer()}"/>
				<s:iterator id="item2" value='XMLUtils.getElements(#item, "CustomerList/Customer")' status="iterStatus">
					<s:set name='id2' 		value='#item2.getAttribute("CustomerID")'/>
					<s:set name='name2' 	value='#item2.getAttribute("CustomerID")'/>
					<s:set name="extn2" value='XMLUtils.getChildElement(#item2, "Extn")'/>
					<s:set name='name2' value="%{#displayCustomerMap.get(#id2)}" />	
					<s:set name="suffixType" value="%{#customerSuffixTypeMap.get(#item2.getAttribute('CustomerID'))}"/>
					<s:set name='type2' 	value='#item2.getAttribute("type")'/>
					<s:set name='orgcode2' 	value='#item2.getAttribute("OrganizationCode")'/>
					
					<s:set name='customerId2' 	value="#id2"/>
					<s:set name='controlId2' 	value="#id2.replace('-', '_')"/>
					<s:set name='randomId2' 	value="%{generateRamdomId()}"/>
					<s:set name='customerPath2' value='%{#customerPathMap.get(#id2)}'></s:set>
					<s:set name="div3" value="%{#customerShipDivisionMap.get(#id2)}"/>
					<s:if test='%{#div3 == ""}'>
						<s:set name="div3" value="%{'PEDRO'}"/>
					</s:if>

					<s:set name="shipToDivPadding" value="15" />
					<s:if test='%{#suffixType == #shipToCustomer}' >
					<s:set name="shipToDivPadding" value="45" />
					</s:if>
					
					
					<div style="padding-left: <s:property value="#shipToDivPadding" />px; margin-bottom:5px; position: relative; display: block;"  id='divShareList_<s:property value="#randomId2"/>_base'>
						<s:if test='%{#suffixType != #shipToCustomer}' >
							<input type="button" class="icon-plus" style="vertical-align:middle;" onclick="getShareList('<s:property value="#customerId2"/>','<s:property value="#suffixType"/>', 'divShareList_<s:property value="#randomId2"/>_base', false) " />
						</s:if>
						<input type="checkbox" 	id="customerPaths_<s:property value="#controlId2"/>"  onclick="selectNode('<s:property value="#controlId2"/>', this.checked);" name="customerPaths" value="<s:property value="customerPath2"/>" /> <s:property value="#name2"/>
						
						
						<div style="display: none;" >
							<input type="checkbox" id="customerIds_<s:property value="#controlId2"/>" name="customerIds" 	value="<s:property value="#customerId2"/>">
							<input type="checkbox" id="customerDivs_<s:property value="#controlId2"/>" name="customerDivs" 	value="<s:property value="#div3"/>">
						</div>
						<div id='divShareList_<s:property value="#randomId2"/>' ></div>
					</div>				 
					<SCRIPT type="text/javascript">
						createCountAndPathMap('<s:property value="#controlId2"/>','<s:property value="customerPath2"/>','<s:property value="#itemChildCount"/>','<s:property value="#div3"/>');
						updateShareListChild('<s:property value="#customerId2"/>', '<s:property value="#controlId2"/>');
						preSelected('<s:property value="#controlId2"/>');
					</SCRIPT>
				
				</s:iterator>
				
				<s:set name="billtosuffixtype" value='B' />
				<s:url id="paginatedShipTo" action="XPEDXMyItemsDetailsGetShareList" namespace="/myItems">
					<s:param name="pageNumber" value="'{0}'"/>
					<s:param name="pageSetToken" value="%{pageSetToken}"/>
					<s:param name="suffixtype" value="%{billtosuffixtype}"/>
					<s:param name="customerId" value="customerId"/>
					
				</s:url>
 				<xpedx:pagectl currentPage="%{pageNumber}"  divId="%{#paginateDivID}" lastPage="%{totalNumberOfPages}" urlSpec="%{#paginatedShipTo}" isAjax="true" />
			</div>
<s:if test="showRoot == 'true'">
		</div>
</s:if>
	</s:iterator>

</div>