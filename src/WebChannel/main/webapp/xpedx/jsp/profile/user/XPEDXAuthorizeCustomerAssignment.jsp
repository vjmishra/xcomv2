<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<%@ taglib prefix="xpedx" uri="xpedx" %>

<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs?. 
    This is to avoid a defect in Struts that?s creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts? OGNL statements. --%>
    <script type="text/javascript">			
    function moveCustomers(fromLocation, toLocation )
	{
		var lboFrom = document.getElementById(fromLocation);
		var lboTo = document.getElementById(toLocation);
		
		for ( var i=0; i < lboFrom.options.length; i++ )
		{
			if ((lboFrom.options[i].selected == true ) )
			{
				strItemToAddText = lboFrom.options[i].text;
				strItemToAddVal = lboFrom.options[i].value;
				lboTo.options[lboTo.length] = new Option(strItemToAddText, strItemToAddVal);
				lboFrom.options[i] = null;
				i--;
			}
		}
	}
    </script>

	
    <s:set name='_action' value='[0]' />
	<s:set name='customers1' value='#_action.getCustomers1()' />
	<s:set name='customers2' value='#_action.getCustomers2()' />
	<s:set name='listSize' value='#_action.getListSize()' />
	<s:set name='userelement' value="getUser()" />
    <s:set name='user' value='#userelement' />
	<s:set name='displayUserID' value='%{#user.getAttribute("DisplayUserID")}' />
	<s:set name="isCustomerAdmin" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@isCurrentUserAdmin(wCContext)" />
    <s:hidden name="oldAssignedCustomers" value='%{#_action.getCustomers2()}'></s:hidden>
				<tr>

                    <td valign="top" class="no-border-right-user padding0"><div class="question">
                        <ul>
                          <li>Authorized Locations:</li>
                          <li><span class="float-right  padding-right4 margin-right"><a href="#" id="purposeofmails-al2"><img src="../../xpedx/images/icons/12x12_grey_help.png" style="margin-top:2px;"  width="12" height="12" border="0" alt="Chosen from Available Locations, these are all the Ship-To locations to which the user has access." title="Chosen from Available Locations, these are all the Ship-To locations to which the user has access."/></a></span></li>
                        </ul>
                      </div>
                      
                    </td>
                    <td class="no-border-right-user padding0"> 
						<div style="width:730px; overflow:auto;border: 1px solid #CCCCCC;"> 
                      <select name="customers2" id="customersTwo" multiple="multiple" size="%{listSize}" style=" min-width:735px; height:150px;">
							<s:iterator value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@custFullAddresses(#customers2,wCContext.storefrontId,"true","true")'>
								<s:set name='currentCustIdKey' value='key'/>
		    					<s:set name='currentCustIdValue' value='value'/>
	                			<option value='<s:property value="#currentCustIdKey"/>'>
                			    <script>
	              			      var theValue='<s:property value='#currentCustIdValue'/>';
	              			      if(theValue.indexOf("Billto") > -1)
	              			      {
		                			      theValueParts = theValue.split("-");
		                			      theNewValue = theValue.replace("Billto", "Bill-To");
		                			      document.write(theNewValue);
	              			      }
	              			      else
	              			      {
		                			    if(theValue.indexOf("Shipto") > -1)  
	              			        {
		                			      theValueParts = theValue.split("-");
		                			      theNewValue = theValue.replace("Shipto", "Ship-To");
		                			      document.write(theNewValue);
	              			        }		 
	              			        else
	              			        {
		                			        if(theValue.indexOf("-") > -1)
		                			        {
			                			        theNewValue = "Account: " + theValue;
			                			        document.write(theNewValue);
		                			        }
	              			        }               			      
	              			      }		                			    
	              			    </script>
								<%-- <s:property value='#currentCustIdValue'/> --%>									<s:property value='#currentCustIdValue'/>
								</option>
							</s:iterator>
						</select>
						</div>
					</td>
                </tr>
                	<s:hidden name="buyAdmin" value='%{isCustomerAdmin}'/>
                <s:if test='%{isCustomerAdmin}'>            
					<tr>
	                    <td valign="top" class="no-border-right-user padding0">&nbsp;</td>
	                    <%-- Fix for Jira 3048 issue item 2 --%>
						<s:if test="%{wCContext.loggedInUserId == #displayUserID}">
							<s:set name="disableSinceSelfApprover" value="%{false}" />
						</s:if>	    
						<s:else>						
						    <s:set name="disableSinceSelfApprover" value="%{true}" />
						</s:else>        
	                    <s:if test='%{disableSinceSelfApprover}'> 
		                    <td align="right" valign="top" class="no-border-right-user padding0">
		                    	<div class="float-left padding-left0" style="" align="center">
		                    		<a href='javascript:moveCustomers("customersTwo","customersOne")'>
		                    			<img src="../../xpedx/ster/images/bu-remove.jpg" alt="Remove" width="88" height="23" border="0" align="right" class=" padding-right2"  />	                    		
	                    			</a>
		                    		<a href='javascript:moveCustomers("customersOne","customersTwo")'>
										<img src="../../xpedx/ster/images/bu-add.jpg" alt="Add" width="80" height="23" border="0" align="right" class="padding-right2" />
		                    		</a>
		                    	</div>
		                    </td>
	                    </s:if>

	                   
	                   <%-- END Fix for Jira 3048 issue item 2 --%>
	                </tr>	
				</s:if>
				<tr>
                    <td valign="top" class="no-border-right-user padding0"><div class="question">
                        <ul>
                          <li>Available Locations:</li>
                          <li><span class="float-right   padding-right4 margin-right"><a href="#" id="purposeofmails-al3"><img src="../../xpedx/images/icons/12x12_grey_help.png" style="margin-top:2px;" width="12" height="12" border="0" alt="A list of all ship-to locations active for this account." title="A list of all ship-to locations active for this account."/></a></span></li>
                        </ul>
                    </div></td>
                    <td valign="top" class="no-border-right-user padding0">
<!--                     <div class=" clearview">  -->
					<div id="customers1_div">
						<div style="width:730px; overflow:auto;border: 1px solid #CCCCCC;"> 
	                      <select name="customers1" id="customersOne" multiple="multiple" size="%{listSize}" >
								<s:iterator value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@custFullAddresses(#customers1,wCContext.storefrontId,"true","true")'>
									<s:set name='currentCustIdKey' value='key'/>
			    					<s:set name='currentCustIdValue' value='value'/>
		                			<option value='<s:property value="#currentCustIdKey"/>'>
	                			    <script>
	                			      var theValue2='<s:property value='#currentCustIdValue'/>';
	                			      if(theValue2.indexOf("Billto") > -1)
	                			      {
		                			      theValueParts2 = theValue2.split("-");
		                			      theNewValue2 = theValue2.replace("Billto", "Bill-To");
		                			      document.write(theNewValue2);
	                			      }
	                			      else
	                			      {
		                			    if(theValue2.indexOf("Shipto") > -1)  
	                			        {
		                			      theValueParts2 = theValue2.split("-");
		                			      theNewValue2 = theValue2.replace("Shipto", "Ship-To");
		                			      document.write(theNewValue2);
	                			        }
	                			        else
	                			        {
		                			        if(theValue2.indexOf("-") > -1)
		                			        {
			                			        theNewValue2 = "Account: " + theValue2;
			                			        document.write(theNewValue2);
		                			        }
	                			        }		                			      
	                			      }		                			    
	                			    </script>										  
	                			    <%-- <s:property value='#currentCustIdValue'/> --%>										<s:property value='#currentCustIdValue'/>
									</option>								
								</s:iterator>
							</select>
	                      </div>
                      	<s:url id="paginateLocations" action="paginatedCustomerLocations" namespace="/profile/user">
							<s:param name="pageNumber" value="'{0}'"/>
						</s:url>
						<p align="center">
 						<xpedx:pagectl currentPage="%{pageNumber}"  divId="customers1_div" lastPage="%{totalNumberOfPages}" urlSpec="%{#paginateLocations}" isAjax="true" /></p>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <td valign="top" class="no-border-right-user ">&nbsp;</td>
                    <td valign="top" class="no-border-right-user ">&nbsp;</td>
                  </tr>