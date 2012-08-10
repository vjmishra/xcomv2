<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<%@ taglib prefix="xpedx" uri="xpedx" %>

					<div id="customers1_div">
					<s:set name='displayUserID' value='%{SelectedCurrentCustomer}' />
						<div style="width:605px; overflow:auto;border: 1px solid #CCCCCC;"> 
	                      <select name="customers1" id="customersOne" multiple="multiple" size="%{listSize}" style="min-width:605px;height:90px;">
								<s:iterator value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@custFullAddresses(customers1,wCContext.storefrontId,"true","true")'>
									<s:set name='currentCustIdKey' value='key'/>
			    					<s:set name='currentCustIdValue' value='value'/>
		                			<option value='<s:property value="#currentCustIdKey"/>'>
										<s:property escape='false' value='#currentCustIdValue'/>
									</option>								
								</s:iterator>
							</select>
	                      </div>
              			<s:url id="paginateLocations" action="paginatedCustomerLocations" namespace="/profile/user">
							<s:param name="userId" value="#displayUserID"/>
							<s:param name="pageNumber" value="'{0}'"/>
							<s:param name="pageSetToken" value="%{pageSetToken}"/>
						</s:url>
						<p align="center">
 						<xpedx:pagectl currentPage="%{pageNumber}"  divId="customers1_div" lastPage="%{totalNumberOfPages}" urlSpec="%{#paginateLocations}" isAjax="true" /></p>
                      </div>