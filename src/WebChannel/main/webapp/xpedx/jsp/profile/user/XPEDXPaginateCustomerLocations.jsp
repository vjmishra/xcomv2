<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<%@ taglib prefix="xpedx" uri="xpedx" %>
<s:set name='_action' value='[0]' />
<s:set name='customers1' value='#_action.getAvailableLocationMap()' />
				<div id="customers1_div">
						<div style="width:730px; overflow:auto;border: 1px solid #CCCCCC;"> 
	                      <select name="customers1" id="customersOne" multiple="multiple" size="%{listSize}"> 
								<s:iterator value='#customers1'>
									<s:set name='currentCustIdKey' value='key'/>
			    					<s:set name='currentCustIdValue' value='value'/>
		                			<option value='<s:property value="#currentCustIdKey"/>'>
	                			    <!--removed Account: for zira 3244 by balkhi-->										  	
									 <s:property escape='false' value='#currentCustIdValue'/>
									</option>								
								</s:iterator>
							</select>
	                      </div>
                      	<s:url id="paginateLocations" action="paginatedCustomerLocations" namespace="/profile/user" >
							<s:param name="userId" value="#displayUserID"/>
							<s:param name="pageNumber" value="'{0}'"/>
						</s:url>
						<p align="center">
 						<xpedx:pagectl currentPage="%{pageNumber}"  divId="customers1_div" lastPage="%{totalNumberOfPages}" urlSpec="%{#paginateLocations}" isAjax="true" /></p>
                      </div>