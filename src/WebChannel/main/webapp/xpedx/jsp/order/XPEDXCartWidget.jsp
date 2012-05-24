<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<link media="all" type="text/css" rel="stylesheet" href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/css/order/draft-order-list.css" />
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/order/draftOrderList.js"></script>

<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='dateUtilBean'/>
<s:set name="xutil" value="#_action.getXMLUtils()"/>
<s:set name='sdoc' value="outputDoc"/>
<s:set name='desc' value="#_action.getOrderDesc()"/>
<s:url id="draftOrderListSortURL" action="portalHome" namespace="/home">
    <s:param name="orderByAttribute" value="'{0}'"/>
    <s:param name="orderDesc" value="'{1}'"/>
</s:url>
<s:url id="orderListWidgetSortURL" action="portalHome" namespace="/home">
    <s:param name="orderByAttribute" value="'{0}'"/>
    <s:param name="orderDesc" value="'{1}'"/>
</s:url>


	<!-- // begin main-content -->
	    <div class="cartListWidgetTableHeader2" id="cartListWidgetTableHeader2">
		            <!-- table list -->
					<div id="cartListWidget">
					<swc:sortctl sortField="%{orderByAttribute}"
                  sortDirection="%{orderDesc}" down="Y" up="N"
                  urlSpec="%{#draftOrderListSortURL}">
                    <s:set name="iter" value="#util.getElements(#sdoc, '//Page/Output/OrderList/Order')"/>
                    <s:action name="xpedxBuildSimpleTable" executeResult="true" namespace="/common" >
                        <s:param name="id" value="'mil-list'"/>
                        <s:param name="summary" value="'Cart List Widget'"/>
                        <s:param name="cssClass" value="''"/>                       
                        <s:param name="iterable" value="#iter"/>
                      
                        <s:param name="columnSpecs[0].label" value="'Name And Description'"/>
                        <s:param name="columnSpecs[0].dataField" value="'OrderName'"/>
                        <s:param name="columnSpecs[0].sortable" value="'true'"/>
                        <s:param name="columnSpecs[0].labelCssClass" value="'no-border table-header-bar-left'"/>
                        <s:param name="columnSpecs[0].dataCellBuilder" value="'xpedxDraftOrderListOrderNameAnchor'"/>
		   				<s:param name="columnSpecs[0].dataCellBuilderProperties['namespace']" value="'/order'"/>
		   				
		   				<s:param name="columnSpecs[1].label" value="'Modified By'"/>
                        <s:param name="columnSpecs[1].dataField" value="'CustomerLastName'"/>
                        <s:param name="columnSpecs[1].sortable" value="'false'"/>
                        <s:param name="columnSpecs[1].labelCssClass" value="'no-border'"/>
                        <s:param name="columnSpecs[1].dataCellBuilder" value="'xpedxDraftOrderListOwnerNameAnchor'"/>
						<s:param name="columnSpecs[1].dataCellBuilderProperties['namespace']" value="'/order'"/>
                        
                        <s:param name="columnSpecs[2].label" value="'Last Modified'"/>
                        <s:param name="columnSpecs[2].dataField" value="'Modifyts'"/>
                        <s:param name="columnSpecs[2].sortable" value="'false'"/>
                        <s:param name="columnSpecs[2].labelCssClass" value="'no-border'"/>
                        <s:param name="columnSpecs[2].dataCellBuilder" value="'simpleTableDate'"/>

						<s:param name="columnSpecs[3].label" value="'Cart Actions'"/>
						<s:param name="columnSpecs[3].labelCssClass" value="'no-border-right table-header-bar-right'"/>
                        <s:param name="columnSpecs[3].sortable" value="'false'"/>
                        <s:param name="columnSpecs[3].dataCellBuilder" value="'xpedxDraftOrderListActionAnchor'"/>
						<s:param name="columnSpecs[3].dataCellBuilderProperties['namespace']" value="'/order'"/>

                  </s:action>
              </swc:sortctl>
                    </div> <!-- table end-->
                  	
	        </div>    <!-- // table list end -->


