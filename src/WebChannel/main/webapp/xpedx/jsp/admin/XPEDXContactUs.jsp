<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>
<%request.setAttribute("isMergedCSSJS","true");%>

<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils" id="wcUtil" />
<s:bean name="com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXUtilBean" id="xpedxUtilBean" />
<s:bean name="com.sterlingcommerce.webchannel.utilities.XMLUtilities" id="xmlUtilities" />
<s:set name='_action' value='[0]' />
<s:set name='isGuestUser' value="wCContext.guestUser" />
<s:set name="xutil" value="XMLUtils" />
<s:set name='xmlUtil' value="#_action.getXMLUtils()" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta content='IE=8' http-equiv='X-UA-Compatible' />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />


<!-- styles -->
<s:if test="#isGuestUser == false">
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/GLOBAL.css" />
</s:if>
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/theme/MISC.css" /> 

<!--[if IE]>
	<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/IE.css" />
	<![endif]-->

<s:if test="#isGuestUser == true">
<link media="all" type="text/css" rel="stylesheet" href="/swc/xpedx/css/global/GLOBAL.css" />
</s:if>

<!-- javascript -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-base.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/ext-all.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/validation.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojo.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/global/dojoRequire.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/theme/theme-1/theme.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/catalog/catalogExt.js"></script>

<!-- jQuery -->
	<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-1.4.2.min.js"></script>

    <script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/pngFix/jquery.pngFix.pack.js"></script>
    
    <link rel="stylesheet" type="text/css"
	href="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
     
      <script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.mousewheel-3.0.2.pack.js"></script>
 
<script type="text/javascript"
	src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/fancybox/jquery.fancybox-1.3.4.js"></script>


<!-- carousel scripts js   -->

<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/xpedx-custom-carousel.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery.dropdownPlain.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jcarousel/lib/jquery.jcarousel.min.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/jquery.form.js"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/quick-add/quick-add.js"></script>



<!-- STUFF YOU NEED FOR BEAUTYTIPS -->
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.hoverIntent.minified.js" type="text/javascript" charset="utf-8"></script>
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/bgiframe_2.1.1/jquery.bgiframe.min.js" type="text/javascript" charset="utf-8"></script>
<script src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-tool-tip/jquery.bt.min.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/xpedx-contactus-ui.js" language="javascript">
	
</script>


<script type="text/javascript" src="<s:property value='#wcUtil.staticFileLocation' />/xpedx/js/jquery-ui.min.js"></script>

<title><s:property value="wCContext.storefrontId" /> - Contact Us </title> 

<!--Webtrands Start -->
<s:if test="#isGuestUser == false">    	
	<meta name="WT.ti" content="<s:property value="wCContext.storefrontId" />  / Contact Us" /> 
</s:if>
<s:else>
	<meta name="WT.ti" content="<s:property value="wCContext.storefrontId" />  / Contact Us" /> 
</s:else>
<!--Webtrands End -->
</head>

<body class="ext-gecko ext-gecko3">
<div id="main-container">
	
	<s:if test="#isGuestUser == false">
    	<div id="main">
    </s:if>
    <s:if test="#isGuestUser == true">
    	<div id="main" class="anon-pages">
    </s:if>
        <s:action name="xpedxHeader" executeResult="true" namespace="/common" />
        
            <div class="container">
                <!-- breadcrumb -->
                <s:set name="eBusinessPhoneNo" value="eBusinessPhoneNo" />
                <s:set name="fmtEBusinessPhoneNo" value='#xpedxUtilBean.getFormattedPhone( #eBusinessPhoneNo )' />
                
              
	<s:form action="xpedxContactUsSendMail" namespace="/common" method="post" name="contractUsForm" id="contractUsForm">
                <div id="mid-col-mil"><br>
                
	                <div class="page-title"> Contact Us</div>
                
                <s:if test="#isGuestUser == false">
                <%-- Code Commented for JIra 3307 
                <s:set name="csr1ListEle" value="csr1UserEle" />
				<s:set name="csr2ListEle" value="csr2UserEle" />
				<s:if test='#csr1ListEle != null'>
					<s:set name="csr1Ele" value='#xutil.getChildElement(#csr1ListEle,"CustomerContact")' />
					
					<s:set name="csr1FirstName" value='#xutil.getAttribute(#csr1Ele,"FirstName")' />
					<s:set name="csr1LastName" value='#xutil.getAttribute(#csr1Ele,"LastName")' />
					<s:set name="csr1EMailID1" value='#xutil.getAttribute(#csr1Ele,"EmailID")' />
					<s:set name="csr1Phone" value='#xutil.getAttribute(#csr1Ele,"DayPhone")' />
					<s:set name="fmtCsr1Phone" value='#xpedxUtilBean.getFormattedPhone( #csr1Phone )' />
				</s:if>
				< s:if test='#csr2ListEle != null'>
					<s:set name="csr2Ele" value='#xutil.getChildElement(#csr2ListEle,"CustomerContact")' />
					<s:set name="csr2FirstName" value='#xutil.getAttribute(#csr2Ele,"FirstName")' />
					<s:set name="csr2LastName" value='#xutil.getAttribute(#csr2Ele,"LastName")' />
					
					
					<s:set name="csr2EMailID" value='#xutil.getAttribute(#csr2Ele,"EMailID")' />
					<s:set name="csr2Phone" value='#xutil.getAttribute(#csr2Ele,"DayPhone")' />
					<s:set name="fmtCsr2Phone" value='#xpedxUtilBean.getFormattedPhone( #csr2Phone )' />
				<  /s:if>
				-->
				
				<%--  Start of  Code for JIra 3307 --%>
				
					<s:set name="csr1ListEle" value="csr1CustServEle" />
					<s:set name="csr2ListEle" value="csr2CustServEle" />
					<s:if test='#csr1ListEle != null'>
						<s:set name="csr1UserName" value='#xmlUtil.getAttribute(#csr1ListEle,"Username")' />
						<s:set name="csr1Ele" value='#xmlUtil.getChildElement(#csr1ListEle,"ContactPersonInfo")' />
						<s:set name="csr1FirstName" value='#xmlUtil.getAttribute(#csr1Ele,"FirstName")' />
						<s:set name="csr1LastName" value='#xmlUtil.getAttribute(#csr1Ele,"LastName")' />
						<s:set name="csr1EMailID" value='#xmlUtil.getAttribute(#csr1Ele,"EMailID")' />
						<s:set name="csr1Phone" value='#xmlUtil.getAttribute(#csr1Ele,"DayPhone")' />
						<s:set name="fmtCsr1Phone" value='#xpedxUtilBean.getFormattedPhone( #csr1Phone )' />
					</s:if>
					<s:if test='#csr2ListEle != null'>
						<s:set name="csr2UserName" value='#xmlUtil.getAttribute(#csr2ListEle,"Username")' />
						<s:set name="csr2Ele" value='#xmlUtil.getChildElement(#csr2ListEle,"ContactPersonInfo")' />
						<s:set name="csr2FirstName" value='#xmlUtil.getAttribute(#csr2Ele,"FirstName")' />
						<s:set name="csr2LastName" value='#xmlUtil.getAttribute(#csr2Ele,"LastName")' />
						<s:set name="csr2EMailID" value='#xmlUtil.getAttribute(#csr2Ele,"EMailID")' />
						<s:set name="csr2Phone" value='#xmlUtil.getAttribute(#csr2Ele,"DayPhone")' />
						<s:set name="fmtCsr2Phone" value='#xpedxUtilBean.getFormattedPhone(#csr2Phone )' />
					</s:if>
				
				<%--  End of  Code for JIra 3307 --%>
				
				<s:set name="CustExtn" value="#xutil.getChildElement(customerElement,'Extn')" />
				<s:set name="orgListEle" value="organizationElement" />
				<s:set name="orgElem" value='#xutil.getChildElement(#orgListEle,"Organization")' />
				<s:set name="OrgName"  value='#xutil.getAttribute(#orgElem,"OrganizationName")' />
				<!-- Changed ExtnCustomerDivision to  ExtnCustOrderBranch for JIRA 3251 -->
				<s:set name="OrgCode" value='#xutil.getAttribute(#CustExtn,"ExtnCustOrderBranch")' />
				<s:set name="orgCorpInfoEle" value='#xutil.getChildElement(#orgElem,"CorporatePersonInfo")' />
				
				<s:set name='orgAddress1' value='#xutil.getAttribute(#orgCorpInfoEle,"AddressLine1")' />
				<s:set name="orgAddress2" value='#xutil.getAttribute(#orgCorpInfoEle,"AddressLine2")' />
				<s:set name='orgAddress3' value='#xutil.getAttribute(#orgCorpInfoEle,"AddressLine3")' />
				<s:set name="orgCity" value='#xutil.getAttribute(#orgCorpInfoEle,"City")' />
				<s:set name="orgState" value='#xutil.getAttribute(#orgCorpInfoEle,"State")' />
				<s:set name="orgZip" value='#xutil.getAttribute(#orgCorpInfoEle,"ZipCode")' />
				<s:set name="orgZip" value='@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedZipCode(orgZip)' />
				<s:set name="orgCountry" value='#xutil.getAttribute(#orgCorpInfoEle,"Country")' />
				
 				<s:set name="orgPhone" value='#xutil.getAttribute(#orgCorpInfoEle,"DayPhone")' /> 
				<s:set name="fmtOrgPhone" value='#xpedxUtilBean.getFormattedPhone( #orgPhone )' />
				
				<s:set name="orgEmail" value='#xutil.getAttribute(#orgCorpInfoEle,"EMailID")' />
				
				<s:set name="orgExtnEle" value='#xutil.getChildElement(#orgElem,"Extn")' />
				<s:set name="orgCutOffTime" value='#xutil.getAttribute(#orgExtnEle,"ExtnDeliveryCutOffTime")' />
			
				<s:set name="custEle" value="customerElement" />
				<s:set name="parentCustElem" value='#xutil.getChildElement(#custEle,"ParentCustomer")' />
				<s:set name="parentCustExtnElem" value='#xutil.getChildElement(#parentCustElem,"Extn")' />
				<s:set name="custExtnEle" value='#xutil.getChildElement(#custEle,"Extn")' />
				<s:set name='salesRepListEle' value='#xutil.getChildElement(#parentCustExtnElem, "XPEDXSalesRepList")' />
				<s:if test='#salesRepListEle != null'>
			        <s:set name='salesRepUserList' value='#xmlUtilities.getElements(#salesRepListEle, "//XPEDXSalesRepList/XPEDXSalesRep/YFSUser")'/>
			    </s:if>
			    <s:set name="shipToAddress" value="shipToAddress" />
			    
            
                   <table class="full-width">
          <tbody>
            <tr>
              <td colspan="2" class="underlines no-border-right-user">
			                   Technical Support: <s:property value='%{#fmtEBusinessPhoneNo}'/>, <a href="mailto:<s:property value="%{eBusinessEmailID}"/>"><s:property value="%{eBusinessEmailID}"/></a><br>
                            </td>
            </tr>
          
							 <s:if test="#shipToAddress != null">
								<tr>
	                                <td valign="top" class="underlines no-border-right-user" style="width: 190px;">
	                                <div>
	                                   Questions Related To Ship To: <br/>
	                                   <p style="color: #00399;"><a href="#ajax-assignedShipToCustomers" id="contactUsShipTo" class="eleven underlink">[Change]</a></p>
	                                </div>
	                                </td>
	                            	<td valign="top" class="underlines no-border-right-user">
	                            		<s:set name="formattedShipToID" value="@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@formatBillToShipToCustomer(#shipToAddress.customerID)" />
	                            		<s:property value="#shipToAddress.shipToName"/>&nbsp; (<s:property value="#formattedShipToID"/>) <br/>
	                            		<s:iterator value='%{#shipToAddress.addressList}' id='adressLine'>
	                            		 	<s:if test='%{#adressLine != "" || adressLine != null}'>
												<s:property value='adressLine' /><br/>
											</s:if>
										</s:iterator>
										<s:if test="%{#shipToAddress.city!=''}">
											<s:property value='%{#shipToAddress.city}' />,&nbsp;
										</s:if>
										<s:if test="%{#shipToAddress.state!=''}">
											<s:property value="%{#shipToAddress.state}" />&nbsp; 
										</s:if>
										<s:if test="%{#shipToAddress.zipCode!=''}">
											<s:property value="%{@com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils@getFormattedZipCode(#shipToAddress.zipCode)}" />
										</s:if>
										<s:if test="%{#shipToAddress.country!=''}">
											<s:property value="%{#shipToAddress.country}" />
										</s:if>
	                            	</td>
	                            </tr>
	                         </s:if>
							<s:if test='%{(#salesRepUserList != null && #salesRepUserList.size > 0) || #csr1ListEle != null || #csr2ListEle != null}'>
								<!--   <tr>							  
								 	<td colspan="2" class="underlines no-border-right-user"><strong>A few others who want to help...</strong></td>
								 </tr> -->
								<%--  
								  Code commented for JIra 3307
								<s:if test='#csr1Ele != null || #csr2Ele != null'> 
									 <tr>
										 <td valign="top" class="underlines no-border-right-user">Customer Service Representative:</td>
										 <td class="underlines no-border-right-user">
										 	<s:hidden name="csr1EMailID" value='%{#csr1EMailID1}'/>
						          			<s:if test='#csr1Ele != null'>
							                 <s:property value='%{#csr1FirstName}'/> <s:property value='%{#csr1LastName}'/><br/>
							                    <s:if test='%{#fmtCsr1Phone != ""}'>
							                    	<s:property value='%{#fmtCsr1Phone}'/><br/>
							                    </s:if>
							                   <s:if test='%{#csr1EMailID1 != ""}'>
							                    	<a href="mailto:<s:property value='%{#csr1EMailID1}'/>"><s:property value='%{#csr1EMailID1}'/></a><br/><br/>
							                   </s:if>
						                    </s:if>
						                    <s:if test='#csr2Ele != null'>
						                      <s:property value='%{#csr2FirstName}'/> <s:property value='%{#csr2LastName}'/><br/>
						                      <s:if test='%{#fmtCsr2Phone != ""}'>
						                      	<s:property value='%{#fmtCsr2Phone}'/><br/>
						                      </s:if>
						                      <s:if test='%{#csr2EMailID != ""}'>
						                      	<a href="mailto:<s:property value='%{#csr2EMailID}'/>"><s:property value='%{#csr2EMailID}'/></a><br/>
						                      </s:if>
						                   </s:if>
										 </td>
									 </tr>
								 </s:if>--%>
								 <s:if test='(#salesRepUserList != null && #salesRepUserList.size > 0)'>
								 	<tr>
								 		<td valign="top" class="underlines no-border-right-user">
								 			Sales Professional:
								 		</td>
								 		<td class="underlines no-border-right-user">
								 			<s:iterator value='salesRepUserList' id='salesRepUser' status="salesRepCount">
												<s:set name="userContInfoEle" value='#xutil.getChildElement(#salesRepUser,"ContactPersonInfo")' />
												<s:set name="FirstName" value='#xutil.getAttribute(#userContInfoEle,"FirstName")' />
												<s:set name="LastName" value='#xutil.getAttribute(#userContInfoEle,"LastName")' />
												<s:set name="EMailID" value='#xutil.getAttribute(#userContInfoEle,"EMailID")' />
												<s:set name="Phone" value='#xutil.getAttribute(#userContInfoEle,"DayPhone")' />
												 <s:set name="fmtPhone" value='#xpedxUtilBean.getFormattedPhone( #Phone )' />
				                               	<s:property value='%{#FirstName}'/> <s:property value='%{#LastName}'/><br/>
				                               	<s:if test='%{#fmtPhone != ""}'>
				                               	<s:property value='%{#fmtPhone}'/><br/>
				                               	</s:if>
				                               	<s:if test='%{#EMailID != ""}'>
				                               		<a href="mailto:<s:property value='%{#EMailID}'/>"><s:property value='%{#EMailID}'/></a><br/><br/>
				                               	</s:if>
				                               	<s:else><br/><br/></s:else>
			                           		</s:iterator>
								 		</td>
								 	</tr>
								 </s:if>
							</s:if>
	          		
					
					
					<s:set name="customerService" value="%{'false'}" />
					<s:if test='#csr1Ele != null'>
						<s:if test="%{#customerService == 'false'}">
							<tr class="padding-bottom1">
				            	<td valign="top" class="no-border-right padding0">Customer Service:</td>
				            	<td colspan="3" valign="top" class="underlines no-border-right-user padding-bottom1">
				            	<s:property value='%{#csr1UserName}'/><br/>
				            	<a href="mailto:<s:property value='%{#csr1EMailID}'/>"><s:property value='%{#csr1EMailID}'/></a><br/>
								<span class="grey-italic">
									<s:if test='%{#fmtCsr1Phone != ""}'>
							          	<s:property value='%{#fmtCsr1Phone}'/>
							          	 <br/>
							        </s:if>
							        
						   	    </span></td>
				          	</tr>
				          	<s:set name="customerService" value="%{true}" />
						</s:if>
						<s:else>
							<tr class="padding-bottom1">
				            	<td valign="top" class="underlines no-border-right padding0">&nbsp; </td>
				            	<td colspan="3" valign="top" class="no-border-right-user padding-bottom1">
				            	<s:property value='%{#csr2UserName}'/><br />
				            	<a href="mailto:<s:property value='%{#csr1EMailID}'/>"><s:property value='%{#csr1EMailID}'/></a>
						         <br/>
								<span class="grey-italic">
				            		
				            	<s:if test='%{#fmtCsr2Phone != ""}'>
						           	<s:property value='%{#fmtCsr2Phone}'/><br/>
						           	<br/>
						        </s:if>
						            	 
				          	</tr>
						</s:else>
					</s:if>
					
					<s:if test='#csr2Ele != null'>
						<s:if test="#customerService == 'false'">
							<tr class="padding-bottom1">
				            	<td valign="top" class="no-border-right padding0">Customer Service:</td>
				            	<td colspan="3" valign="top" class="underlines no-border-right-user padding-bottom1">
				            	<s:property value="#csr2UserName"/><br />
				            	<a href="mailto:<s:property value='%{#csr2EMailID}'/>"><s:property value='%{#csr2EMailID}'/></a>
				            	<br />
								<span class="grey-italic">
  					            
				            	<s:if test="%{#fmtCsr2Phone != ''}">
				            		<s:property value="#fmtCsr2Phone"/>
				            		<br/>
				            	</s:if>
				            	
				          	</tr>
						</s:if>
						<s:else>
							<tr class="padding-bottom1">
				            	<td valign="top" class="no-border-right padding0">&nbsp; </td>
				            	<td colspan="3" valign="top" class="underlines no-border-right-user padding-bottom1">
				            	<s:property value="#csr2UserName"/>
				            	<span class="grey-italic">
				            	<br/>
				            	 	<a href="mailto:<s:property value='%{#csr2EMailID}'/>"><s:property value='%{#csr2EMailID}'/></a>
								<br/>
								<s:if test="%{#fmtCsr2Phone != ''}">
				            		<s:property value="#fmtCsr2Phone"/>
				            		 <br/>
				            	</s:if>
				            	</span></td>
				          	</tr>
						</s:else>
					</s:if>
							
							    <tr>
                                <td valign="top" class="underlines no-border-right-user">
                                   Division Information:</td>
                                <td valign="top" class="underlines no-border-right-user">
                                <s:property value="%{#OrgName}" />&nbsp; (<s:property value="%{#OrgCode}"/>)<br/>
                                <s:property value='%{#orgAddress1}'/>&nbsp;<s:property value='%{#orgAddress2}'/><br/>
                                <s:if test='%{#orgAddress3 != ""}'>
                               		<s:property value='%{#orgAddress3}'/><br/>
                                </s:if>
                                <s:property value='%{#orgCity}'/>,&nbsp;<s:property value='%{#orgState}'/>
                                 <s:property value='%{#orgZip}'/>&nbsp;<s:property value='%{#orgCountry}'/><br/>
                                <s:property value='%{#fmtOrgPhone}'/> <br/>
                                <s:if test='%{#orgEmail != ""}'>
                                	<a href="mailto:<s:property value='%{#orgEmail}'/>"><s:property value='%{#orgEmail}'/></a><br/>
                                </s:if>
                              	</td>
							 </tr>
							
							 <tr>
							  	<s:if test='%{#orgCutOffTime != ""}'>
								    <td class="underlines no-border-right-user">Order Cutoff Time:</td>
	                              	<td valign="top" class="underlines no-border-right-user"> <s:property value='%{#orgCutOffTime}'/></td>
	                              </s:if>
                              </tr>
				
														
               	  </tbody>
               	  </table>

                

                 </s:if>
                 
                  <s:if test="#isGuestUser == true">
                     <table class="full-width">
			          <tbody>
			            <tr>
		              <td colspan="3" class="underlines no-border-right-user">
			                   Technical Support: <s:property value='%{#fmtEBusinessPhoneNo}'/>, <a href="mailto:<s:property value="%{eBusinessEmailID}"/>"><s:property value="%{eBusinessEmailID}"/></a><br>
			                   </td>
          		 		 </tr>
          		 		 </tbody>
          		 	  </table>
	  				
  				  </s:if>
  				
                 
   </div> 
   
   </s:form>
                    <!-- End Pricing -->
                    
                                
                    
                    <br>
                </div>
            </div>
        </div>
     
    <!-- end main  -->
    
    <s:action name="xpedxFooter" executeResult="true" namespace="/common" />    
 
<!-- end container  -->
</body>
</html> 
	