<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<%@ taglib prefix="xpedx" uri="xpedx" %>
<s:set name='_action' value='[0]' />
<s:set name="listSize" value="#_action.getListSize()" />
<s:set name='customers2' value='#_action.getAuthorizedLocationMap()' />
					 <div id="customers2_div">
						<div> 
                      <select name="customers2" id="customersTwo" multiple="multiple" size="%{listSize}" style=" min-width:735px; height:150px;">
							<s:iterator value='#customers2'>
								<s:set name='currentCustIdKey' value='key'/>
		    					<s:set name='currentCustIdValue' value='value'/>
	                			<option value='<s:property value="#currentCustIdKey"/>'>
								<%-- <s:property value=theNewValue/> added by balkhi for remove ? 3244 duplicate values --%>	
									 <s:property escape='false' value='#currentCustIdValue'/> 									
								</option>
								
							</s:iterator>
						</select>
						
						</div>
						</div>