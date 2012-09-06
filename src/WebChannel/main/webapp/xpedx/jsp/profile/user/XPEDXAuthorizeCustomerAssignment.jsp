<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld"%>
<%@ taglib prefix="xpedx" uri="xpedx" %>
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />


<%--This is to setup reference to the action object so we can make calls to action methods explicitly in JSPs?. 
    This is to avoid a defect in Struts that?s creating contention under load. 
    The explicit call style will also help the performance in evaluating Struts? OGNL statements. --%>
    <script type="text/javascript">			
    //function for Jira 4146- On ADD
    var addToavailable="";
    var removeFromavailable="";
    function ReplaceAll(Source,stringToFind,stringToReplace){
  		var temp = Source;
  	    var index = temp.indexOf(stringToFind);
  	        while(index != -1){
  	            temp = temp.replace(stringToFind,stringToReplace);
  	            index = temp.indexOf(stringToFind);
  	        }
  	        return temp;
  	}
    
  //Start - Modified the two methods for Jira 4146 
  var myMask;
    function moveCustomersonAdd(fromLocation, toLocation )
	{
    	//added for jira 3974
		var waitMsg = Ext.Msg.wait("Processing...");
		myMask = new Ext.LoadMask(Ext.getBody(), {msg:waitMsg});
		myMask.show();
		var lboFrom = document.getElementById(fromLocation);
		var lboTo = document.getElementById(toLocation);
		var len = lboFrom.options.length;
		var custID;
		var existingCustId;
		for ( var i=0; i < lboTo.options.length; i++ )
		{
			var temp =lboTo.options[i].value;
			if(existingCustId != undefined){
				existingCustId = existingCustId + ','+ temp;
				}else
					existingCustId = temp;
		}
		for ( var i=0; i < lboFrom.options.length; i++ )
		{
			
			if ((lboFrom.options[i].selected == true ) )
			{
				strItemToAddText = lboFrom.options[i].text;
				strItemToAddVal = lboFrom.options[i].value;
				var temp = strItemToAddVal;
				if(custID != undefined){
				custID = custID + ','+ temp;
				}else
					custID = temp;
				if(removeFromavailable != ""){
					removeFromavailable = removeFromavailable + ','+ temp;
				}else
					removeFromavailable = temp;
				
				addToavailable = ReplaceAll(addToavailable,removeFromavailable,"");	
				lboFrom.options[i] = null;
				i--;
			}
		}
		var url = document.getElementById("AddCustomerURL").value; 
		Ext.Ajax.request({
			url: url,
			params: {
				custID: custID,
				existingCustId: existingCustId
			},
			success: function (response, request){
			document.getElementById("customers2_div").innerHTML = response.responseText;
			Ext.Msg.hide();
			myMask.hide();
			},
			failure: function (response, request){
				Ext.Msg.hide();
				myMask.hide();
			}
	});
	}
		
    
    
    var myMask;
    function moveCustomersOnRemove(fromLocation, toLocation )
	{
		var lboFrom = document.getElementById(fromLocation);
		var lboTo = document.getElementById(toLocation);
		var len = lboFrom.options.length;
		var custID;
		//added for jira 3974
		var waitMsg = Ext.Msg.wait("Processing...");
		myMask = new Ext.LoadMask(Ext.getBody(), {msg:waitMsg});
		myMask.show();
		for ( var i=0; i < lboFrom.options.length; i++ )
		{
			if ((lboFrom.options[i].selected == true ) )
			{
				strItemToAddText = lboFrom.options[i].text;
				strItemToAddVal = lboFrom.options[i].value;
				var temp = strItemToAddVal;
				if(custID != undefined){
				custID = custID + ','+ temp;
				}else
					custID = temp;
				if(addToavailable !=""){
					addToavailable = addToavailable + ','+ temp;
				}else
					addToavailable = temp;
				
				removeFromavailable = ReplaceAll(removeFromavailable,addToavailable,"");
				lboFrom.options[i] = null;
				i--;
			}
		}
		
		 var url = document.getElementById("saveURL").value; 
		 var pageNum = document.getElementById("pageNum");
		 var pageLength = document.getElementById("pageLength");
		 var rootCustomerKey = document.getElementById("rootCustomerKey");
		 var selectedCurntCust = document.getElementById("selectedCurrentCustomer");
		 url = url +"&selectedCurrentCustomer="+selectedCurntCust.value +"&custID="+custID +"&pageLength="+ pageLength.value +"&pageNum="+pageNum.value  +"&rootCustomerKey="+rootCustomerKey.value+"&addToavailable="+addToavailable+"&removeFromavailable="+removeFromavailable;
			Ext.Ajax.request({
			url: url,
			success: function (response, request){
			document.getElementById("customers1_div").innerHTML = response.responseText;
			Ext.Msg.hide();
			myMask.hide();
			},
			failure: function (response, request){
				Ext.Msg.hide();
				myMask.hide();
			}
	}); 
	}
    </script>

	
    <s:set name='_action' value='[0]' />
	<s:set name='customers1' value='#_action.getAvailableLocationMap()' />
	<s:set name='customers2' value='#_action.getAuthorizedLocationMap()' />
	<s:set name='shipToStr' value='#_action.shipToStr()' />
	<s:set name='listSize' value='#_action.getListSize()' />
	<s:set name='userelement' value="getUser()" />
    <s:set name='user' value='#userelement' />
	<s:set name='displayUserID' value='%{#user.getAttribute("DisplayUserID")}' />
	<s:set name="isCustomerAdmin" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@isCurrentUserAdmin(wCContext)" />
    <s:hidden name="oldAssignedCustomers" value='%{#_action.getCustomers2()}'></s:hidden>
    <s:url id="AddCustomerURLId"  namespace="/profile/user" action="addCustomersToAuthorize" > </s:url>	
    <s:hidden id="AddCustomerURL" name='AddCustomerURL' value='%{#AddCustomerURLId}' />
    <s:url id="saveURLId"  namespace="/profile/org" action="removeCustomersFromAuthorize" > </s:url>	
    <s:hidden id="saveURL" name='saveURL' value='%{#saveURLId}' />
    <s:hidden id="selectedCurrentCustomer" name="selectedCurrentCustomer"  value='%{#_action.getSelectedCurrentCustomer()}' />
    <s:hidden id="rootCustomerKey" name="rootCustomerKey"  value='%{#_action.getRootCustomerKey()}' />
	<s:hidden id="pageNum" name="pageNum"  value='%{#_action.getPageNumber()}' />	
	<s:hidden id="pageLength" name="pageLength"  value='%{#_action.getPageSize()}' />
	<s:hidden id="addToavailable" name="addToavailable"  value='%{#_action.getAddToavailable()}' />
	<s:hidden id="removeFromavailable" name="removeFromavailable"  value='%{#_action.getRemoveFromavailable()}' />	
				<tr>

                    <td valign="top" class="no-border-right-user padding0"><div class="question">
                        <ul>
                          <li>Authorized Locations:</li>
                          <li><span class="float-right  padding-right4 margin-right"><a href="#" id="purposeofmails-al2"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png" style="margin-top:2px;"  width="12" height="12" border="0" alt="Chosen from Available Locations, these are all the Ship-To locations to which the user has access." title="Chosen from Available Locations, these are all the Ship-To locations to which the user has access."/></a></span></li>
                        </ul>
                      </div>
                      
                    </td>
                    <td class="no-border-right-user padding0"> 
                    <div id="customers2_div">
						<div style="width:730px; overflow:auto;border: 1px solid #CCCCCC;"> 
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
	                        <td align="right" valign="top" class="no-border-right-user padding0">
		                    	<div class="float-left padding-left0" style="" align="center">
		                    		<a href='javascript:moveCustomersOnRemove("customersTwo","customersOne")'>
		                    			<img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/ster/images/bu-remove<s:property value='#wcUtil.xpedxBuildKey' />.jpg" alt="Remove" width="88" height="23" border="0" align="right" class=" padding-right2"  />	                    		
	                    			</a>
	                    			<a href='javascript:moveCustomersonAdd("customersOne","customersTwo")'>
										<img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/ster/images/bu-add<s:property value='#wcUtil.xpedxBuildKey' />.jpg" alt="Add" width="80" height="23" border="0" align="right" class="padding-right2" />
		                    		</a>
		                    	</div>
		                    </td>
	                   
	                   <%-- END Fix for Jira 3048 issue item 2 --%>
	                </tr>	
				</s:if>
				<tr>
                    <td valign="top" class="no-border-right-user padding0"><div class="question">
                        <ul>
                          <li>Available Locations:</li>
                          <li><span class="float-right   padding-right4 margin-right"><a href="#" id="purposeofmails-al3"><img src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/images/icons/12x12_grey_help.png" style="margin-top:2px;" width="12" height="12" border="0" alt="A list of available locations for this account." title="A list of available locations for this account."/></a></span></li>
                        </ul>
                    </div></td>
                    <td valign="top" class="no-border-right-user padding0">
<!--                     <div class=" clearview">  -->
					<div id="customers1_div">
						<div style="width:730px; overflow:auto;border: 1px solid #CCCCCC;"> 
	                      <select name="customers1" id="customersOne" multiple="multiple" size="%{listSize}" style=" min-width:735px; height:150px;"> 
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
                    </td>
                  </tr>
                  <tr>
                    <td valign="top" class="no-border-right-user ">&nbsp;</td>
                    <td valign="top" class="no-border-right-user ">&nbsp;</td>
                  </tr>