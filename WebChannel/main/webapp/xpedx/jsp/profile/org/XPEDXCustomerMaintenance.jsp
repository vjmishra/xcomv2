 <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 

<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%><s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='util' />
<s:set name='_action' value='[0]'/>
<s:set name='xmlUtil' value="#_action.getXMLUtils()" />
<s:set name='sdoc' value="outputDoc" />
<s:set name='extnElem' value='#xmlUtil.getChildElement(#sdoc, "Extn")'/>
<s:set name='salesRepList' value='#xmlUtil.getChildElement(#extnElem,"XPEDXSalesRepList")'/>
<s:set name='custAddressElem' value='custAddressElem' />
<s:set name='custPersonInfoElem' value='#xmlUtil.getChildElement(#custAddressElem,"PersonInfo")'/>
<s:set name='custPersonInfoExtnElem' value='#xmlUtil.getChildElement(#custPersonInfoElem,"Extn")'/>

 
        
        <div>
		    <table class="listTableHeader2" id="listTableHeader_x1">
		        <tr>
		            <td><span class="listTableHeaderText">
		            		<s:if test='%{#extnElem.getAttribute("ExtnSuffixType") == "S"}'>
		            			<s:text name="CustomerMaintenance"/>
		            		</s:if>
		            		<s:else>
		            			<s:text name="LocationAdministration"/>
		            		</s:else> 	
		            	</span></td>
		        </tr>
		    </table>
    	</div>
    	<div>
    		<table class="listTableBody3 padding-left3" width="96%" >

				
				<tr>
					<td><s:text name="CustomerEmailAddress"/></td>
					<td><s:textfield id='txtCustEmailAddress' name='txtCustEmailAddress' size="25" tabindex="" maxlength="500"
					value='%{#extnElem.getAttribute("ExtnCustEmailAddress")}'/>
					</td>
				</tr>				
				
				<tr>
					<td><s:text name="CSR1"/></td>
					<td><s:textfield id='txtxpedxeCSR' name='txtxpedxeCSR' size="25" tabindex="" 
					value='%{#extnElem.getAttribute("ExtnECSR")}' readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/></td>
				</tr>
				
				<tr>
					<td><s:text name="eCSR2"/></td>
					<td><s:textfield id='txtxpedxeCSR' name='txtxpedxeCSR' size="25" tabindex="" 
					value='%{#extnElem.getAttribute("ExtnECSR2")}' readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/></td>
				</tr>
				
		
<!--			<tr>
					<td><s:text name="MaximumOrderAmount"/></td>
					<td><s:textfield id='txtMaxOrderAmt' name='txtMaxOrderAmt' size="25" tabindex="" 
					value='%{#util.formatDouble(wCContext, #extnElem.getAttribute("ExtnMaxOrderAmount"))}'
					readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/>
					</td>
				</tr>
				<tr>
					<td><s:text name="MinimumOrderAmount"/></td>
					<td><s:textfield id='txtMinOrderAmt' name='txtMinOrderAmt' size="25" tabindex="" 
					value='%{#util.formatDouble(wCContext, #extnElem.getAttribute("ExtnMinOrderAmount"))}'
					readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/>
					</td>
				</tr>
				<tr>
					<td><s:text name="SmallOrderFee"/></td>
					<td><s:textfield id='txtMinChargeAmt' name='txtMinChargeAmt' size="25" tabindex="" 
					value='%{#util.formatDouble(wCContext, #extnElem.getAttribute("ExtnMinChargeAmount"))}'
					readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/>
					</td>
				</tr>  -->
				<tr>
					<td><s:text name="View Invoices "	/></td>
					<td><s:checkbox name='CanViewInvoice' fieldValue="true" value="%{#_action.isPreviewInvoices()}"/></td>
				</tr>
				<tr>
					<td><s:text name="EmailAddressForInvoice "	/></td>
					<td><s:textfield name='EmailIDForInvoice' value="%{#extnElem.getAttribute('ExtnInvoiceEMailID')}"/></td>
				</tr>
				<tr>
					<td><s:text name='InvoiceDistributionMethod'/></td>
					<td><s:textfield name="InvoiceDistMethod" readonly="true" value="%{#extnElem.getAttribute('ExtnInvoiceDistMethod')}" 
							cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/>
					</td>
				</tr>
				<tr>
					<td>
						<s:text name="PrimarySalesRep" />
					</td>
					<td>
						<s:textfield value='%{#_action.getPrimarySalesRepID()}' readonly="true" 
									cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/>
					</td>
				</tr>
				<s:iterator value="otherSalesRepList" id="otherSalesRep" status="status">
					<tr>
						<td>
							<s:text name="SalesRep" /><s:property value="%{#status.index+1}"/>
						</td>
						<td>
							<s:textfield value="%{#otherSalesRep}" readonly="true" />
						</td>
					</tr>
				</s:iterator>

				
				<tr><td colspan="2">
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
						<td><s:checkbox name='CustLineAccNoFlag' disabled="true" fieldValue="true" value="%{#_action.IsCustLineAccNoFlag()}"/></td>
						<td><s:text name="Customer Line Account No: "	/></td>
						<td><s:textfield id='CustLineAccNoLabel' 
							readonly="%{!#_action.IsCustLineAccNoFlag()}"
							name='CustLineAccNoLabel' size="25" tabindex="" value='%{#extnElem.getAttribute("ExtnCustLineAccLbl")}'/>
						</td>
					</tr>
<!--				<tr>
						<td><s:checkbox name='CustLineSeqNoFlag' disabled="true" fieldValue="true" value="%{#_action.IsCustLineSeqNoFlag()}"/></td>
						<td><s:text name="Customer Line Sequence No."	/></td>
						<td></td>
					</tr>
					<tr>
						<td><s:checkbox name='CustLinePONoFlag' disabled="true" fieldValue="true" value="%{#_action.IsCustLinePONoFlag()}"/></td>
						<td><s:text name="Customer Line PO No."	/></td>
						<td></td>
					</tr>  -->
					<tr>
						<td><s:checkbox name='CustLineField1Flag' disabled="true" fieldValue="true" value="%{#_action.IsCustLineField1Flag()}"/></td>
						<td><s:text name="Customer field 1: "	/></td>
						<td><s:textfield id='CustLineField1Label' 
							readonly="%{!#_action.IsCustLineField1Flag()}"
							name='CustLineField1Label' size="25" tabindex="" value='%{#extnElem.getAttribute("ExtnCustLineField1Label")}'/>
						</td>
					</tr>
					<tr>
						<td><s:checkbox name='CustLineField2Flag' disabled="true"  fieldValue="true" value="%{#_action.IsCustLineField2Flag()}"/></td>
						<td><s:text name="Customer field 2: "	/></td>
						<td><s:textfield id='CustLineField2Label' 
							readonly="%{!#_action.IsCustLineField2Flag()}"
							name='CustLineField2Label' size="25" tabindex="" value='%{#extnElem.getAttribute("ExtnCustLineField2Label")}'/></td>
					</tr>
					<tr>
						<td><s:checkbox name='CustLineField3Flag' disabled="true"  fieldValue="true" value="%{#_action.IsCustLineField3Flag()}"/></td>
						<td><s:text name="Customer field 3: "	/></td>
						<td><s:textfield id='CustLineField3Label' 
							readonly="%{!#_action.IsCustLineField3Flag()}"
							name='CustLineField3Label' size="25" tabindex="" value='%{#extnElem.getAttribute("ExtnCustLineField3Label")}'/></td>
					</tr>
				</table>
				</div>
				</td></tr>
				
				<tr>
					<td><s:text name="AttentionName"/></td>
					<td><s:textfield id='txtAttnName' name='txtAttnName' size="25" tabindex="" readonly="true"
							value='%{#extnElem.getAttribute("ExtnAttnName")}' cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/>
					</td>
				</tr>
				<tr>
					<td><s:text name="Address 1"/></td>
					<td><s:textfield id='txtAddress1' name='txtAddress1' tabindex="" readonly="true"
							value='%{#custPersonInfoElem.getAttribute("AddressLine1")}' cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/>
					</td>
				</tr>
				<tr>
					<td><s:text name="Address 2"/></td>
					<td><s:textfield id='txtAddress2' name='txtAddress2' tabindex="" readonly="true"
							value='%{#custPersonInfoElem.getAttribute("AddressLine2")}' cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/>
					</td>
				</tr>
				<tr>
					<td><s:text name="Address 3"/></td>
					<td><s:textfield id='txtAddress3' name='txtAddress3' tabindex="" readonly="true"
							value='%{#custPersonInfoElem.getAttribute("AddressLine3")}' cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/>
					</td>
				</tr>
				<tr>
					<td><s:text name="City"/></td>
					<td><s:textfield id='txtCity' name='txtCity' tabindex="" readonly="true"
							value='%{#custPersonInfoElem.getAttribute("City")}' cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/>
					</td>
				</tr>
				<tr>
					<td><s:text name="State"/></td>
					<td><s:textfield id='txtState' name='txtState' tabindex="" readonly="true"
							value='%{#custPersonInfoElem.getAttribute("State")}' cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/>
					</td>
				</tr>
				<tr>
					<td><s:text name="Country Code"/></td>
					<td><s:textfield id='txtCountryCode' name='txtCountryCode' tabindex="" readonly="true"
							value='%{#custPersonInfoElem.getAttribute("Country")}' cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/>
					</td>
				</tr>
				<tr>
					<td><s:text name="ZipCode"/></td>
					<td><s:textfield id='txtZipCode' name='txtZipCode' tabindex="" readonly="true"
							value='%{#custPersonInfoElem.getAttribute("ZipCode")}' cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/>
					</td>
				</tr>
				<tr>
					<td><s:text name="Zip4"/></td>
					<td><s:textfield id='txtZip4' name='txtZip4' tabindex="" readonly="true"
							value='%{#custPersonInfoExtnElem.getAttribute("ExtnZip4")}' cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/>
					</td>
				</tr>
				<tr>
					<td><s:text name="Phone1"/></td>
					<td><s:textfield id='txtPhone1' name='txtPhone1' size="25" tabindex="" readonly="true"
							value='%{#extnElem.getAttribute("ExtnPhone1")}' cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/>
					</td>
				</tr>
				<tr>
					<td><s:text name="Phone2"/></td>
					<td><s:textfield id='txtPhone2' name='txtPhone2' size="25" tabindex="" 
							value='%{#extnElem.getAttribute("ExtnPhone2")}'/>
					</td>
				</tr>	
				<tr>
					<td><s:text name="Fax1"/></td>
					<td><s:textfield id='txtFax1' name='txtFax1' size="25" tabindex="" readonly="true"
							value='%{#extnElem.getAttribute("ExtnFax1")}' cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/>
					</td>
				</tr>	
				<tr>
					<td><s:text name="Fax2"/></td>
					<td><s:textfield id='txtFax2' name='txtFax2' size="25" tabindex="" 
							value='%{#extnElem.getAttribute("ExtnFax2")}'/>
					</td>
				</tr>										
				
				<s:if test='%{#extnElem.getAttribute("ExtnSuffixType") == "S" || #extnElem.getAttribute("ExtnSuffixType") == "B"}'>
				<s:if test='%{#extnElem.getAttribute("ExtnSuffixType") == "S"}'>
				<tr>
					<td><s:text name="CustomerDivision"/></td>
					<td><s:textfield id='txtCustomerDivision' name='txtCustomerDivision' size="25" tabindex="" 
					value='%{#extnElem.getAttribute("ExtnCustomerDivision")}' readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/></td>
				</tr>	
				</s:if>	
				<tr>
					<td><s:text name="LegacyCustomerNumber"/></td>
					<td><s:textfield id='txtLegacyCustomerNumber' name='txtLegacyCustomerNumber' size="25" tabindex="" 
					value='%{#extnElem.getAttribute("ExtnLegacyCustNumber")}' readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/></td>
				</tr>				
				
				<tr>
					<td><s:text name="SuffixType"/></td>
					<td><s:textfield id='txtSuffixType' name='txtSuffixType' size="25" tabindex="" 
					value='%{#extnElem.getAttribute("ExtnSuffixType")}' readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;" /></td>
				</tr>

				<tr>
					<td><s:text name="Suffix"/></td>
					<td><s:textfield id='txtSuffix' name='txtSuffix' size="25" tabindex="" 
					value='%{#extnElem.getAttribute("ExtnSuffix")}' readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;" /></td>
				</tr>
								
				
<!--			<tr>
					<td><s:text name="CustomerOrderBranch"/></td>
					<td><s:textfield id='txtCustOrderBranch' name='txtCustOrderBranch' size="25" tabindex="" 
					value='%{#extnElem.getAttribute("ExtnCustOrderBranch")}' readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/></td>
				</tr>
				<tr>
					<td><s:text name="ShipFromBranch"/></td>
					<td><s:textfield id='txtShipFromBranch' name='txtShipFromBranch' size="25" tabindex="" 
					value='%{#extnElem.getAttribute("ExtnShipFromBranch")}' readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/></td>
				</tr>
				<tr>
					<td><s:text name="CustomerStatus"/></td>
					<td><s:textfield id='txtCustomerStatus' name='txtCustomerStatus' size="25" tabindex="" 
					value='%{#extnElem.getAttribute("ExtnCustomerStatus")}' readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/></td>
				</tr> -->
																				
				
				
				
<!--			<tr>
					<td><s:text name="BrandCode"/></td>
					<td><s:textfield id='txtBrandCode' name='txtBrandCode' size="25" tabindex="" 
					value='%{#extnElem.getAttribute("ExtnBrandCode")}' readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/></td>
				</tr>  -->
				<tr>
					<td><s:text name="CustomerClass"/></td>
					<td><s:textfield id='txtCustomerClass' name='txtCustomerClass' size="25" tabindex="" 
					value='%{#extnElem.getAttribute("ExtnCustomerClass")}' readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/></td>
				</tr>
<!--			<tr>
					<td><s:text name="ServiceOptimizationCode"/></td>
					<td><s:textfield id='txtServiceOptCode' name='txtServiceOptCode' size="25" tabindex="" 
					value='%{#extnElem.getAttribute("ExtnServiceOptCode")}' readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/></td>
				</tr>  -->
				<tr>
					<td><s:text name="CurrencyCode"/></td>
					<td><s:textfield id='txtCurrencyCode' name='txtCurrencyCode' size="25" tabindex="" 
					value='%{#extnElem.getAttribute("ExtnCurrencyCode")}' readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/></td>
				</tr>												
				
				</s:if>
				
				<tr>
					<td><s:text name="CustomerName"/></td>
					<td><s:textfield id='txtCustomerName' name='txtCustomerName' size="25" tabindex="" 
					value='%{#extnElem.getAttribute("ExtnCustomerName")}' readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/></td>
				</tr>
				
				<tr>
					<td><s:text name="SAPNumber"/></td>
					<td><s:textfield id='txtSAPNumber' name='txtSAPNumber' size="25" tabindex="" 
					value='%{#extnElem.getAttribute("ExtnSAPNumber")}' readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/></td>
				</tr>

				<tr>
					<td><s:text name="SAPName"/></td>
					<td><s:textfield id='txtSAPName' name='txtSAPName' size="25" tabindex="" 
					value='%{#extnElem.getAttribute("ExtnSAPName")}' readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/></td>
				</tr>

				<tr>
					<td><s:text name="SAPParentAccNo"/></td>
					<td><s:textfield id='txtSAPParentAccNo' name='txtSAPParentAccNo' size="25" tabindex="" 
					value='%{#extnElem.getAttribute("ExtnSAPParentAccNo")}' readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/></td>
				</tr>
				
				<tr>
					<td><s:text name="SAPParentName"/></td>
					<td><s:textfield id='txtSAPParentName' name='txtSAPParentName' size="25" tabindex="" 
					value='%{#extnElem.getAttribute("ExtnSAPParentName")}' readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/></td>
				</tr>												
				
<!--			<s:if test='%{#extnElem.getAttribute("ExtnSuffixType") == "S" || #extnElem.getAttribute("ExtnSuffixType") == "B"}'>
				<tr>
					<td><s:text name="ShipComplete"	/></td>
					<td><s:checkbox name='ShipComplete'  disabled="true" fieldValue="true" value="%{#_action.isShipComplete()}"/></td>
				</tr>
				
				<tr>
					<td><s:text name="OrderUpdateFlag"	/></td>
					<td><s:checkbox name='OrderUpdateFlag'  disabled="true" fieldValue="true" value="%{#_action.isOrderUpdateFlag()}"/></td>
				</tr>
				</s:if>
				 -->
				
				
				<tr>
					<td><s:text name="NAICSCode"/></td>
					<td><s:textfield id='txtNAICSCode' name='txtNAICSCode' size="25" tabindex="" 
							value='%{#extnElem.getAttribute("ExtnNAICSCode")}'
							readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/>
					</td>
				</tr>
				
				<tr>
					<td><s:text name="NAICSName"/></td>
					<td><s:textfield id='txtNAICSName' name='txtNAICSName' size="25" tabindex="" 
							value='%{#extnElem.getAttribute("ExtnNAICSName")}'
							readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/>
					</td>
				</tr>
				
				
				<s:if test='%{#extnElem.getAttribute("ExtnSuffixType") == "S"}'>
				<tr>
					<td><s:text name="ShipToOverrideFlag"	/></td>
					<td><s:checkbox name='txtShipToOverrideFlag' disabled="true" fieldValue="true" value="%{#_action.isShipToOverrideFlag()}"/></td>
				</tr>
				</s:if>
				
<!--			<tr>
					<td><s:text name="WMLocationID"/></td>
					<td><s:textfield id='txtWMLocationID' name='txtWMLocationID' size="25" tabindex="" 
							value='%{#extnElem.getAttribute("ExtnWMLocID")}'
							readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/>
					</td>
				</tr>
				
				<tr>
					<td><s:text name="WMOrgID"/></td>
					<td><s:textfield id='txtWMOrgID' name='txtWMOrgID' size="25" tabindex="" 
							value='%{#extnElem.getAttribute("ExtnWMOrgID")}'
							readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/>
					</td>
				</tr>
				 -->
				
				<tr>
					<td><s:text name="PunchoutProtocol"/></td>
					<td><s:textfield id='txtPunchoutProtocol' name='txtPunchoutProtocol' size="25" tabindex="" 
							value='%{#extnElem.getAttribute("ExtnPunchoutProtocol")}'
							readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/>
					</td>
				</tr>
				
				<tr>
					<td><s:text name="ViewMyItemsLink"	/></td>
					<td><s:checkbox name='txtViewMyItemsLink' disabled="true" fieldValue="true" value="%{#_action.IsMyItemsLink()}"/></td>
				</tr>
				
				<tr>
					<td><s:text name="XSLTVersion"/></td>
					<td><s:textfield id='txtXSLTVersion' name='txtXSLTVersion' size="25" tabindex="" 
							value='%{#extnElem.getAttribute("ExtnXSLTVer")}'
							readonly="true" cssStyle="background:#87CEFA;border-style:solid;border-color:#87CEFA;"/>
					</td>
				</tr>
				
				<tr>
					<td align="left" class="padding-right3"><B>
						<a tabindex="" href="<s:url value='/profile/org/xpedxGetOrderBusinessRules.action'><s:param name="customerId" value='%{#sdoc.getAttribute("CustomerID")}'/><s:param name="organizationCode" value='%{#sdoc.getAttribute("OrganizationCode")}'/></s:url>">
						<s:text name=">>    Order Dashboard Business Rules Maintenance"/></a>
					</B></td>
				</tr>
				
				<s:if test='%{#extnElem.getAttribute("ExtnSuffixType") == "S"}'>
				<tr>
					<td align="left" class="padding-right3"><B>
						<a tabindex="" href="<s:url value='/profile/org/xpedxGetCustLocationList.action'><s:param name="parentCustomerKey" value='%{#sdoc.getAttribute("CustomerKey")}'/><s:param name="organizationCode" value='%{#sdoc.getAttribute("OrganizationCode")}'/></s:url>">
						<s:text name=">>    Location Administration"/></a>
					</B></td>
				</tr>


				<tr>
					<td align="left" class="padding-right3"><B>
						<a tabindex="" href="<s:url value='/profile/user/getUserList.action'><s:param name="customerID" value='%{#sdoc.getAttribute("CustomerID")}'/></s:url>">
						<s:text name=">>    User Administration"/></a>
					</B></td>
				</tr>
				</s:if>																										
			</table>
    	</div>