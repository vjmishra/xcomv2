<?xml version="1.0"?>

<xsl:stylesheet version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:emailformatters="com.yantra.pca.email.formatters"
    xmlns:java="java"
    exclude-result-prefixes="java emailformatters">
    

<xsl:template name="applyStyle">
<xsl:comment>CONTENT_TYPE=text/html; charset=UTF-8</xsl:comment>
	<HEAD>
	    <STYLE TYPE="text/css">

		table {
		font-size: 12px;
		font-family: Tahoma, Arial, Geneva, sans-serif;
		font-size:12px; 
		color:#000000;
		border-collapse: collapse;
	}
	table.mainPanel{
		border:solid 1px #ccc;border-bottom:none;
	}
	table.price {
		border-radius: 5px;
		border-collapse:0px;
	}
	table.price td {
		padding: 2px;
	}
	table.price th{		
		color: white;
		padding: 10px;
		font-size: 11px; 
		margin:2px;
	}
	table.price th:first-of-type{
		border-radius: 5px 0 0 0;
	}
	table.price th:last-of-type{
		border-radius: 0 5px 0 0;
	}
	th {
		text-align: left;
	}
	a {
		color: #003399;
		text-decoration: none;
	}
	li {
		margin-bottom: 0px;
	}
	ul {
		margin-bottom: 0px;
	}
	a:hover {
		color: #003399;
		text-decoration: underline;
	}
	.align-right {
		text-align: right;
	}
	.align-center {
		text-align: center;
	}
	.align-left {
		text-align: left;
	}
	table.order-total {
		border: 1px solid #cccccc;
		border-radius: 8px;
	}
	.tbd{
		color: red;
		font-weight: bold;
	}
	table.order-total td {
		padding: 2px;
		text-align:right;
	}
	table.order-total tr.last td {
		font-weight: bold;
		background-color: #cccccc;   		
	}
	tr.specialcharges td {
		border-top: 1px solid #cccccc;
	}
	.footer {
		font-size:11px; 
	}
	.labelText {
		font-weight: bold;
	}
	table.order-total td.totalLabelText {
		padding-right: 5px;	
	}
	
			

	    </STYLE>
			
	</HEAD>
</xsl:template>

	<xsl:template match="Order">
		</xsl:template>
	
		<xsl:variable name="shipToID" >
			<xsl:value-of select="Order/@ShipToID" />
		</xsl:variable>
		
		<xsl:variable name="billToID" >
			<xsl:value-of select="Order/Extn/@ExtnBillToCustomerID" />
		</xsl:variable>

		<xsl:variable name="IsOrderSplit" >
			<xsl:value-of select="Order/@IsOrderSplit" />
		</xsl:variable>
		
		<xsl:variable name="maxDelim" select="'-M'"/>
		<xsl:variable name="accDelim" select="'-A'"/>	
		
		<xsl:variable name="shipToName" >
			<xsl:value-of select="Order/Extn/@ExtnShipToName" />
		</xsl:variable>
		
		<xsl:variable name="billToName" >
			<xsl:value-of select="Order/Extn/@ExtnBillToName" />
		</xsl:variable>
		
		<xsl:variable name="custlineAcctLbl" >
			<xsl:value-of select="Order/Extn/@ExtnCustLineAccLbl" />
		</xsl:variable>
		
		<xsl:variable name="Approver" select="'Approved'"/>
		<xsl:variable name="Pending" select="'Pending'"/>
		<xsl:variable name="Reject" select="'Rejected'"/>
		<xsl:variable name="Submit" select="'Submitted'"/>
		
		
		 
		<xsl:variable name="Subject" >
			<xsl:value-of select="Order/@Subject" />
		</xsl:variable>	
		<xsl:variable name="OrderStatus" >
			<xsl:value-of select="Order/Extn/@ExtnOrderStatus" />
		</xsl:variable>	
		<xsl:variable name="custHold" select="'5350'"/>	
		
		<xsl:variable name="custlinePOLbl" >
			<xsl:value-of select="Order/Extn/@ExtnCustLinePOLbl" />
		</xsl:variable>
		
		<xsl:variable name="viewPricesFlag" >
			<xsl:value-of select="Order/@viewPricesFlag" />
		</xsl:variable>
		
		<xsl:variable name="urlPrefix" select="'https://www.'"/>	
		<xsl:variable name="urlSuffix" select="'.com'"/>
		
		<xsl:variable name="urlSaalFeldSuffix" select="'redistribution.com'"/>
		
		<xsl:variable name="storeFront" >
			<xsl:value-of select="Order/@EnterpriseCode" />
		</xsl:variable>
		
		<xsl:variable name="storeFrontURL">    
			<xsl:value-of select="concat($urlPrefix, $storeFront, $urlSuffix)"/>   
		</xsl:variable> 
		
		<xsl:variable name="storeSaalFeldFrontURL">    
			<xsl:value-of select="concat($urlPrefix, $storeFront, $urlSaalFeldSuffix)"/>   
		</xsl:variable> 
		
		<xsl:variable name="brandLogo" >
			<xsl:value-of select="Order/@BrandLogo" />
		</xsl:variable>	
		<xsl:variable name="ordercomments" >
		<xsl:value-of select="Order/Instructions/Instruction[@InstructionType='HEADER']/@InstructionText" />
		</xsl:variable>	
		
		<xsl:variable name="devSaalUrl" select="'dev.saalfeldredistribution.com/order'"/>
		<xsl:variable name="devxpedxUrl" select="'http://xpappd01.ipaper.com:8001/swc/home/home.action?sfId=xpedx'"/>
		<xsl:variable name="stgSaalUrl" select="'http://stg.saalfeldredistribution.com/'"/>
		<xsl:variable name="stgxpedxUrl" select="'http://stg.xpedx.com/'"/>
		
    <xsl:template match="/">
		<HTML>
			<xsl:call-template name="applyStyle"/>
			<body leftmargin="0" topmargin="0">
			
			<table style="margin-left:5px" width="815">
				<tr>	
					<td style="padding-bottom:20px;">
						<table width="720">
							<xsl:if test = 'Order/@EnterpriseCode="xpedx"'>
							<tr>								
								<td align="left" style="padding-bottom:4px;">
								<img src="{$brandLogo}" width="108" height="32" />
								</td>
							</tr>
							</xsl:if>
							 <xsl:if test = 'Order/@EnterpriseCode="Saalfeld"'>
							 <tr>								
								<td align="left" style="padding-bottom:4px;">
								<img src="{$brandLogo}" width="216" height="64" />
								</td>
							</tr>
							 </xsl:if>
						</table> 
					    
				<table class="mainPanel" cellpadding="0"> 
			  <tr>
			  <td style="padding-left:8px;padding-top:8px;">
											
			  		<xsl:if test="(contains($Subject,$Approver))">
					Your order has been approved.
					</xsl:if>
					<xsl:if test="(contains($Subject,$Reject))">
					Your order has been rejected. If you have any questions, please contact your order approver.					
					</xsl:if>
					<xsl:if test="(contains($Subject,$Pending))">
					The following order requires your attention.					
					</xsl:if>	
					<xsl:if test="(contains($Subject,$Submit))">This is a courtesy notification that an order has been placed or changed at</xsl:if>											
					<xsl:choose>
					<xsl:when test = 'Order/@EnvironmentID="STAGING"'>
						<xsl:if test = 'Order/@EnterpriseCode="xpedx"'>
							<a href="{$stgxpedxUrl}/order" >Click here</a> to review this order on <xsl:value-of select="Order/@EnterpriseCode"/>&#160;.com/order.
						</xsl:if>
						<xsl:if test = 'Order/@EnterpriseCode="Saalfeld"'>
							<a href="{$stgSaalUrl}/order" color="084823">Click here</a>  to review this order on  <xsl:value-of select="Order/@EnterpriseCode"/>redistribution.com/order.
						</xsl:if>
					 
					</xsl:when>
					<xsl:when test = 'Order/@EnvironmentID="DEVELOPMENT"'>
						<xsl:if test = 'Order/@EnterpriseCode="xpedx"'>
							<a href="{$devxpedxUrl}">Click here</a>  to review this order on <xsl:value-of select="Order/@EnterpriseCode"/>.com/order.
						</xsl:if>
						<xsl:if test = 'Order/@EnterpriseCode="Saalfeld"' >
							<a href="{$devSaalUrl}" color="084823">Click here</a>  to review this order on <xsl:value-of select="Order/@EnterpriseCode"/>redistribution.com/order.
						</xsl:if>
				
				 
					</xsl:when>
					<xsl:otherwise>
						<xsl:if test = 'Order/@EnterpriseCode="xpedx"' >
							<a href="{$storeFrontURL}/order">Click here</a>  to review this order on <xsl:value-of select="Order/@EnterpriseCode"/>.com/order.
						</xsl:if>
						<xsl:if test = 'Order/@EnterpriseCode="Saalfeld"' >
							<a href="{$storeSaalFeldFrontURL}/order">Click here</a>  to review this order on <xsl:value-of select="Order/@EnterpriseCode"/>redistribution.com/order.
						</xsl:if>
			
					</xsl:otherwise>
					</xsl:choose>
					<xsl:if test="(contains($Subject,$Submit))">Thank you for your business!</xsl:if>
					</td>
					</tr>					
					<tr>					
					<td style="padding-left:8px;padding-top:8px;">
					<xsl:if test="(contains($Subject,$Approver))">
					<xsl:if test = 'Order/Input/OrderHoldType/@ReasonText!="Empty"' >
					<span class="bold">Approved Comments:&#160;</span><xsl:value-of select="Order/Input/OrderHoldType/@ReasonText"/>
					</xsl:if>
					</xsl:if>
					<xsl:if test="(contains($Subject,$Reject))">
					<xsl:if test = 'Order/Input/OrderHoldType/@ReasonText!="Empty"' >
					<span class="bold">Rejected Comments:&#160;</span><xsl:value-of select="Order/Input/OrderHoldType/@ReasonText"/>
					</xsl:if>
					</xsl:if>
					
					</td>
					</tr>
					<tr>
				<td>
				  <table width="100%" cellpadding="4">
					<tr>
						<td>
							<table width="100%" cellpadding="4">
								<tr>
									<td align="left" valign="top">
										<table>
											<thead>
											  <tr>
												<th>Order Information:</th>
											  </tr>
											</thead>
												<tbody>
													<tr>
														<td valign="top">	
															<table>
																<tr>
																	<td>Web&#160;Confirmation:</td>
																	<td><xsl:value-of select="Order/Extn/@ExtnWebConfNum"/></td>
																</tr>
																<tr>
																	<td>Order #:</td>
																	<xsl:choose>
																	<xsl:when test="(contains($Subject,$Approver))">
																	<td><xsl:value-of select="Order/@FormattedOrderNo"/></td>
																	</xsl:when>
																	<xsl:otherwise>
																	<td><xsl:value-of select="Order/@OrderNo"/></td>
																	</xsl:otherwise>
																	</xsl:choose>
																</tr>
																<tr>
																	<td>Order Status:</td>
																	<td><xsl:value-of select="Order/@Status"/></td>
																</tr>
																<tr>
																	<td>PO #:</td>
																	<td><xsl:value-of select="Order/@CustomerPONo"/></td>
																</tr>
																<tr>
																	<td>Ordered By:</td>
																	<td><xsl:value-of select="Order/Extn/@ExtnOrderedByName"/></td>
																</tr>	
																<tr>
																	<td>Ordered Date:</td>
																	<td><xsl:value-of select="substring(Order/@OrderDate,6,2)"/>/<xsl:value-of select="substring(Order/@OrderDate,9,2)"/>/<xsl:value-of select="substring(Order/@OrderDate,1,4)"/></td>
																</tr>
						 									</table>						 	
														</td>
													</tr>		
											</tbody>
										</table>
									</td>
									<td align="right" valign="top">
									<table>
										<thead>
											  <tr>
												<th>Bill To Detail:</th>
												<th>Shipping Information:</th>
											  </tr>
										</thead>
										<tbody>
											<tr>
												<td valign="top" style="padding-right:30px;">
													<table>
														<tr>
															<td>
																<xsl:choose>
																<xsl:when test="(contains($billToID,$maxDelim))">
																<xsl:value-of select="substring-before($billToID, $maxDelim)" />
																<BR/>
																</xsl:when>
																<xsl:otherwise></xsl:otherwise>
																</xsl:choose>
																<xsl:choose>
																<xsl:when test="$billToName!=''">
																<xsl:value-of select="$billToName" />
																<BR/>
																</xsl:when>
																<xsl:otherwise></xsl:otherwise>
																</xsl:choose>
																<xsl:choose>
																<xsl:when test='Order/Extn/@ExtnAttnName!=""'>
																<xsl:value-of select="Order/Extn/@ExtnAttnName"/>
																<BR/>
																</xsl:when>
																<xsl:otherwise></xsl:otherwise>
																</xsl:choose>
																<xsl:choose>
																<xsl:when test = 'Order/PersonInfoBillTo/@AddressLine1!=""' >
																<xsl:value-of select="Order/PersonInfoBillTo/@AddressLine1"/>
																<BR/>
																</xsl:when>
																<xsl:otherwise></xsl:otherwise>
																</xsl:choose>
																<xsl:choose>
																<xsl:when test = 'Order/PersonInfoBillTo/@AddressLine2!=""' >
																<xsl:value-of select="Order/PersonInfoBillTo/@AddressLine2"/>
																<BR/>
																</xsl:when>
																<xsl:otherwise></xsl:otherwise>
																</xsl:choose>
																<xsl:choose>
																<xsl:when test = 'Order/PersonInfoBillTo/@AddressLine3!=""' >
																<xsl:value-of select="Order/PersonInfoBillTo/@AddressLine3"/>
																<BR/>
																</xsl:when>
																<xsl:otherwise></xsl:otherwise>
																</xsl:choose>
																<xsl:if test = 'Order/PersonInfoBillTo/@City!=""' >
																<xsl:value-of select="Order/PersonInfoBillTo/@City"/>,&#160;
																</xsl:if>
																<xsl:if test = 'Order/PersonInfoBillTo/@State!=""' >
																<xsl:value-of select="Order/PersonInfoBillTo/@State"/>&#160;
																</xsl:if>
																<xsl:if test = 'Order/PersonInfoBillTo/@ZipCode!=""' >
																<xsl:value-of select="Order/PersonInfoBillTo/@ZipCode"/>&#160;
																</xsl:if>
																<xsl:if test = 'Order/PersonInfoBillTo/@Country!=""' >
																<xsl:value-of select="Order/PersonInfoBillTo/@Country"/>
																</xsl:if>
															</td>	
														</tr>
												</table>
											</td>
											<td valign="top" style="padding-right:60px;">
												<table>
													<tr>
														<td>					
															<xsl:choose>
															<xsl:when test="(contains($shipToID,$maxDelim))">
															<xsl:value-of select="substring-before($shipToID, $maxDelim)" />
															<BR/>
															</xsl:when>
															<xsl:otherwise></xsl:otherwise>
															</xsl:choose>
															<xsl:choose>
															<xsl:when test="$shipToName!=''">
															<xsl:value-of select="$shipToName" />
															<BR/>
															</xsl:when>
															<xsl:otherwise></xsl:otherwise>
															</xsl:choose>
															<xsl:choose>
															<xsl:when test='Order/Extn/@ExtnAttentionName!=""'>
															<xsl:value-of select="Order/Extn/@ExtnAttentionName"/>
															<BR/>
															</xsl:when>
															<xsl:otherwise></xsl:otherwise>
															</xsl:choose>
															<xsl:choose>
															<xsl:when test = 'Order/PersonInfoShipTo/@AddressLine1!=""' >
															<xsl:value-of select="Order/PersonInfoShipTo/@AddressLine1"/>
															<BR/>
															</xsl:when>
															<xsl:otherwise></xsl:otherwise>
															</xsl:choose>
															<xsl:choose>
															<xsl:when test = 'Order/PersonInfoShipTo/@AddressLine2!=""' >
															<xsl:value-of select="Order/PersonInfoShipTo/@AddressLine2"/>
															<BR/>
															</xsl:when>
															<xsl:otherwise></xsl:otherwise>
															</xsl:choose>
															<xsl:choose>
															<xsl:when test = 'Order/PersonInfoShipTo/@AddressLine3!=""' >
															<xsl:value-of select="Order/PersonInfoShipTo/@AddressLine3"/>
															<BR/>
															</xsl:when>
															<xsl:otherwise></xsl:otherwise>
															</xsl:choose>
															<xsl:if test = 'Order/PersonInfoShipTo/@City!=""' >
															<xsl:value-of select="Order/PersonInfoShipTo/@City"/>,&#160;
															</xsl:if>
															<xsl:if test = 'Order/PersonInfoShipTo/@State!=""' >
															<xsl:value-of select="Order/PersonInfoShipTo/@State"/>&#160;
															</xsl:if>
															<xsl:if test = 'Order/PersonInfoShipTo/@ZipCode!=""' >
															<xsl:value-of select="Order/PersonInfoShipTo/@ZipCode"/>&#160;
															</xsl:if>
															<xsl:if test = 'Order/PersonInfoShipTo/@Country!=""' >
															<xsl:value-of select="Order/PersonInfoShipTo/@Country"/>
															</xsl:if>														
															</td>
															</tr>
														</table>
													</td>
												</tr>		
											</tbody>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<table cellpadding="4">
								<tr>
									<td valign="top"><span class="labelText"> Expected Delivery Date: </span></td>
									<td valign="top" style="padding-right:30px;">
										<xsl:choose>
										<xsl:when test = 'Order/@ReqDeliveryDate!=""' >
										<xsl:value-of select="substring(Order/@ReqDeliveryDate,6,2)"/>/<xsl:value-of select="substring(Order/@ReqDeliveryDate,9,2)"/>/<xsl:value-of select="substring(Order/@ReqDeliveryDate,1,4)"/>
										</xsl:when>
										<xsl:when test = 'Order/@ReqDeliveryDate = ""' >
										To Be Determined
										</xsl:when>
										<xsl:otherwise>
										To Be Determined
										</xsl:otherwise>
										</xsl:choose>						
									</td>
									<xsl:if test = 'Order/Extn/@ShippingOptions!=""'>								
									<td valign="top"><span class="labelText">Shipping Options: </span></td>
									<td valign="top">
										<xsl:value-of select="Order/Extn/@ShippingOptions"/>
									</td>
									</xsl:if>									
									<xsl:if test="(contains($Subject,$Approver))">
																
									
									<xsl:if test = 'Order/Extn/@ExtnShipComplete="C" or Order/Extn/@ExtnWillCall!="N" or Order/Extn/@ExtnRushOrderFlag!="N" or Order/Extn/@ExtnWebHoldFlag!="N" or Order/Extn/@ExtnOrderStatus!=""' >	
									<td colspan="3"> <span class="labelText"> Shipping Options: </span></td>
									
									</xsl:if>
									<td>
									<xsl:if test = 'Order/Extn/@ExtnShipComplete="C"' >	
									Ship Order Complete
									<xsl:if test = 'Order/Extn/@ExtnWillCall!="N" or Order/Extn/@ExtnRushOrderFlag!="N" or Order/Extn/@ExtnWebHoldFlag!="N"' >	
								    ,
								    </xsl:if>
									</xsl:if>						
									<xsl:if test = 'Order/Extn/@ExtnWillCall!="N"' >	
									Will Call
								    <xsl:if test = 'Order/Extn/@ExtnRushOrderFlag!="N" or Order/Extn/@ExtnWebHoldFlag!="N"' >					
									,
									</xsl:if>					    					
									</xsl:if>
									<xsl:if test = 'Order/Extn/@ExtnRushOrderFlag!="N"' >					
									Rush Order
									<xsl:if test = 'Order/Extn/@ExtnWebHoldFlag!="N"' >					
									,
									</xsl:if>	
									</xsl:if>
						 				
									<xsl:choose>
									<xsl:when test='Order/Extn/@ExtnWebHoldFlag!="N"'>
									Order Placed on Hold
									</xsl:when>
									<xsl:otherwise>
									 <xsl:if test = 'Order/Extn/@ExtnRushOrderFlag!="N" or Order/Extn/@ExtnWebHoldFlag!="N" or Order/Extn/@ExtnWillCall!="N" or Order/Extn/@ExtnShipComplete="C"' >
									,
									</xsl:if>
																		
									<xsl:if test="(contains($OrderStatus,$custHold))">
									Customer Hold
									</xsl:if>
									
									
									</xsl:otherwise>
									</xsl:choose>
												
									</td>
									</xsl:if>
									
									<xsl:if test="(contains($Subject,$Pending))"> 
									<xsl:if test = 'Order/Extn/@ExtnShipComplete="C"' >	
									Ship Order Complete
									<xsl:if test = 'Order/Extn/@ExtnWillCall!="N" or Order/Extn/@ExtnRushOrderFlag!="N" or Order/Extn/@ExtnWebHoldFlag!="N"' >	
								    ,
								    </xsl:if>
									</xsl:if>						
									<xsl:if test = 'Order/Extn/@ExtnWillCall!="N"' >	
									Will Call
								    <xsl:if test = 'Order/Extn/@ExtnRushOrderFlag!="N" or Order/Extn/@ExtnWebHoldFlag!="N"' >					
									,
									</xsl:if>					    					
									</xsl:if>
									<xsl:if test = 'Order/Extn/@ExtnRushOrderFlag!="N"' >					
									Rush Order
									<xsl:if test = 'Order/Extn/@ExtnWebHoldFlag!="N"' >					
									,
									</xsl:if>
									</xsl:if>
									<xsl:if test = 'Order/Extn/@ExtnWebHoldFlag!="N"' >	
									Order Placed on Hold
									</xsl:if>
									</xsl:if>
									
								</tr>
							</table>
						</td>
					</tr>					
					<tr>
					<td style="padding-bottom:15px;">
							<table cellpadding="4">
								<tr>
								<xsl:if test = '$ordercomments!=""'>
									<td valign="top"><span class="labelText">Order Comments:</span></td>
									<td valign="top">
										<xsl:value-of select="Order/Instructions/Instruction[@InstructionType='HEADER']/@InstructionText" />
									</td>
								</xsl:if>
								</tr>
							</table>
						</td>
					</tr>
				  </table>
				</td>
			  </tr>	
			  		  
			  <tr>
				<td>
				  <table width="100%" class="price">
				  <xsl:if test = 'Order/@EnterpriseCode="xpedx"'>
					<thead>
					  <tr>
						<th bgcolor="#003399" style="padding-right:0px;"></th>
						<th bgcolor="#003399" class="align-left" valign="top" style="padding-left:4px;padding-right:10px;">Ordered<br/>Quantity</th>
						 <xsl:choose>
						 <xsl:when test = '(Order/OrderLines/OrderLine/Extn/@ExtnReqShipOrdQty!="") and  ($IsOrderSplit ="N") ' >
						 <th bgcolor="#003399" class="align-left" valign="top" style="padding-left:0px;padding-right:10px;">Shippable<br/>Quantity</th>
						<th bgcolor="#003399" class="align-left" valign="top" style="padding-left:0px;padding-right:0px;">Backorder<br/>Quantity</th>
						 </xsl:when>
						 <xsl:otherwise>
						 <th bgcolor="#003399" class="align-left" valign="top" style="padding-left:0px;padding-right:10px;"></th>
						<th bgcolor="#003399" class="align-left" valign="top" style="padding-left:0px;padding-right:0px;"></th>
						 </xsl:otherwise>
						 </xsl:choose>
						
						<th bgcolor="#003399" class="align-center" valign="top">My<br/>Price&#160;(USD)</th>
						 <xsl:choose>
						 <xsl:when test = '(Order/OrderLines/OrderLine/Extn/@ExtnTotalOfShippableTotals!="") ' > 
						 
						 <th bgcolor="#003399" class="align-center" valign="top">Shippable<br/>Price&#160;(USD)</th></xsl:when>
						 <xsl:otherwise>
						 <th bgcolor="#003399" class="align-center" valign="top"></th>
						 </xsl:otherwise>
						 
						 </xsl:choose>
						
						
						<th bgcolor="#003399" class="align-center" valign="top">Extended<br/>Price&#160;(USD)</th>
					  </tr>
					</thead>
					</xsl:if>
					<xsl:if test = 'Order/@EnterpriseCode="Saalfeld"'>
						<thead>
						  <tr>
							<th bgcolor="##084823" style="padding-right:0px;"></th>
							<th class="align-left" bgcolor="##084823" valign="top" style="padding-left:4px;padding-right:10px;">Ordered<br/>Quantity</th>
							<xsl:choose>
						 <xsl:when test = '(Order/OrderLines/OrderLine/Extn/@ExtnReqShipOrdQty!="") and  (Order/@IsOrderSplit ="N") ' >
						 <th bgcolor="##084823" class="align-left" valign="top" style="padding-left:0px;padding-right:10px;">Shippable<br/>Quantity</th>
						<th bgcolor="##084823" class="align-left" valign="top" style="padding-left:0px;padding-right:0px;">Backorder<br/>Quantity</th>
						 </xsl:when>
						 <xsl:otherwise>
						 <th bgcolor="##084823" class="align-left" valign="top" style="padding-left:0px;padding-right:10px;"></th>
						<th bgcolor="##084823" class="align-left" valign="top" style="padding-left:0px;padding-right:0px;"></th>
						 </xsl:otherwise>
						 </xsl:choose>
							
							<th class="align-center" bgcolor="##084823" valign="top">My<br/>Price&#160;(USD)</th>
							<xsl:choose>
						 <xsl:when test = '(Order/OrderLines/OrderLine/Extn/@ExtnTotalOfShippableTotals!="") ' > 
						 
						 <th bgcolor="##084823" class="align-center" valign="top">Shippable<br/>Price&#160;(USD)</th></xsl:when>
						 <xsl:otherwise>
						 <th bgcolor="##084823" class="align-center" valign="top"></th>
						 </xsl:otherwise>
						 
						 </xsl:choose>
							<th class="align-center" bgcolor="##084823" valign="top">Extended<br/>Price&#160;(USD)</th>
						  </tr>
						</thead>						
					</xsl:if>	
					 <tbody>					
					   <xsl:for-each select="Order/OrderLines/OrderLine">
					  				  
					      <tr>
					      <!-- start -->
					     <td valign="top" align="left" width="280" rowspan="2">
						  <table>
							<tr>
							  <td colspan="2" class="labelText"><xsl:value-of select="Item/@ItemShortDesc" disable-output-escaping="yes"/></td>
							</tr>
							<tr>
							  <td valign="top">
								<xsl:if test = '@LineType!="M"' >					
									<xsl:value-of select="Item/@ItemDesc" disable-output-escaping="yes"/>
								</xsl:if>
							  </td>							
							</tr>
						  </table>
						</td>
						<td valign="top" align="left">
						  <table>
							<tr>
							  <xsl:choose>
					<xsl:when test='@LineType!="M"'> 
						<td><xsl:value-of select='format-number(OrderLineTranQuantity/@OrderedQty,"#")'/></td>
						<td>
						<xsl:choose>
						<xsl:when test = 'OrderLineTranQuantity/@UOMDescription="M_PC"' >
						<xsl:value-of select="substring(OrderLineTranQuantity/@UOMDescription,3,4)"/>
						</xsl:when>
						<xsl:otherwise>
						<xsl:value-of select="OrderLineTranQuantity/@UOMDescription"/>
						</xsl:otherwise>
						</xsl:choose></td>
						
					</xsl:when>
					<xsl:otherwise>						
						<td>
							<xsl:if test='$viewPricesFlag ="Y"'>
								<xsl:choose>
									<xsl:when test='not(Extn/@ExtnExtendedPrice)'><span class="tbd">To be determined</span></xsl:when>
									<xsl:when test='Extn/@ExtnExtendedPrice =""'><span class="tbd">To be determined</span></xsl:when>
									<xsl:when test='Extn/@ExtnExtendedPrice ="0.00"'><span class="tbd">To be determined</span></xsl:when>
									<xsl:otherwise><xsl:value-of select='format-number(Extn/@ExtnExtendedPrice,"$#,###,###,##0.00#")'/></xsl:otherwise>
								</xsl:choose>
							</xsl:if>
						</td>						
					</xsl:otherwise>
					</xsl:choose>
							</tr>
						  </table>
						</td>
						<xsl:if test = '@LineType!="M"' >							
							<td valign="top" align="left">
							  <table>
								<tr>
								 <xsl:if test = '(Extn/@ExtnReqShipOrdQty!="") and  ($IsOrderSplit ="N") ' >
									<td><xsl:value-of select='format-number(Extn/@ExtnReqShipOrdQty,"#")'/></td>
										<td>
										<xsl:choose>
										<xsl:when test = 'OrderLineTranQuantity/@UOMDescription="M_PC"' >
										<xsl:value-of select="substring(OrderLineTranQuantity/@UOMDescription,3,4)"/>
										</xsl:when>
										<xsl:otherwise>
										<xsl:value-of select="OrderLineTranQuantity/@UOMDescription"/>
										</xsl:otherwise>
										</xsl:choose>
										</td>
								 </xsl:if>
								</tr>
							  </table>
							</td>
							
							<td valign="top" align="left">
							  <table>
								<tr>
								<xsl:if test = 'Extn/@ExtnReqBackOrdQty!="" and  ($IsOrderSplit ="N")' >
								 <td><xsl:value-of select='format-number(Extn/@ExtnReqBackOrdQty,"#")'/></td>
										<td>
										<xsl:choose>
										<xsl:when test = 'OrderLineTranQuantity/@UOMDescription="M_PC"' >
										<xsl:value-of select="substring(OrderLineTranQuantity/@UOMDescription,3,4)"/>
										</xsl:when>
										<xsl:otherwise>
										<xsl:value-of select="OrderLineTranQuantity/@UOMDescription"/>
										</xsl:otherwise>
										</xsl:choose>
										</td>
								</xsl:if>
								</tr>
							  </table>
							</td>							 
						</xsl:if>
						<xsl:choose>
						<xsl:when test='@LineType!="M"'>
						<td valign="top" align="right">
						  <table>
							<tr>
							  <td>
								<xsl:if test='$viewPricesFlag ="Y"'>						
								<xsl:choose>
									<xsl:when test='Extn/@ExtnUnitPrice ="0.00"'><span class="tbd">Call for price</span></xsl:when>
									<xsl:when test='Extn/@ExtnUnitPrice =""'><span class="tbd">Call for price</span></xsl:when>
									<xsl:otherwise><xsl:value-of select='format-number(Extn/@ExtnUnitPrice,"$#,###,###,##0.00000")'/></xsl:otherwise>
								</xsl:choose>
							</xsl:if>		
							 </td>
							</tr>
							<tr>
							<xsl:if test='$viewPricesFlag ="Y"'>							  
							  <xsl:choose>
								<xsl:when test='Extn/@ExtnUnitPrice =""'>
								</xsl:when>
								<xsl:when test='Extn/@ExtnUnitPrice ="0.00"'>
								</xsl:when>
								<xsl:otherwise>										
								<td align="center">per <xsl:value-of select='Extn/@ExtnPricingUOMDescription'/></td>
								</xsl:otherwise>
								</xsl:choose>
							  </xsl:if>
							</tr>
						  </table>
						</td>
						<td valign="top" align="right">
						  <table>
							<tr>
							  <td>
							  <xsl:if test='$viewPricesFlag ="Y"'>						
								<xsl:choose>
									<xsl:when test='Extn/@ExtnTotalOfShippableTotals ="0.0"'><span>$0.00</span></xsl:when>
									<xsl:when test='Extn/@ExtnTotalOfShippableTotals ="0.00"'><span>$0.00</span></xsl:when>
									<xsl:when test='Extn/@ExtnTotalOfShippableTotals ="0.00000"'><span>$0.00</span></xsl:when>
									<xsl:when test='Extn/@ExtnTotalOfShippableTotals =""'><span>$0.00</span></xsl:when>
									<xsl:otherwise>
									<xsl:if test = '(Extn/@ExtnTotalOfShippableTotals!="") ' >
									<xsl:value-of select='format-number(Extn/@ExtnTotalOfShippableTotals,"$#,###,###,##0.00#")'/>
									</xsl:if>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:if>
							
							 </td>
							</tr>
						  </table>
						</td>
						<td valign="top" align="right">
						  <table>
							<tr>
							  <td>							  
							  <xsl:if test='$viewPricesFlag ="Y"'>		
								<xsl:choose>
									<xsl:when test='not(Extn/@ExtnExtendedPrice)'><span class="tbd">To be determined</span></xsl:when>
									<xsl:when test='Extn/@ExtnExtendedPrice =""'><span class="tbd">To be determined</span></xsl:when>
									<xsl:when test='Extn/@ExtnExtendedPrice ="0.00"'><span class="tbd">To be determined</span></xsl:when>
									<xsl:otherwise><xsl:value-of select='format-number(Extn/@ExtnExtendedPrice,"$#,###,###,##0.00#")'/></xsl:otherwise>
								</xsl:choose>
							</xsl:if>

							 </td>
							</tr>
						  </table>
						</td>
						</xsl:when>
						</xsl:choose>
						<!-- end -->
					      
					      </tr>
					       <tr>
					       <xsl:if test = '@LineType!="M"' >	
						       <td colspan="6" align="left" valign="top" style="padding-top:5px;padding-bottom:10px;">
							  <table>
								<tr>
								<xsl:if test = '@CustomerPONo!=""' >
									 <xsl:choose>
										  <xsl:when test="$custlinePOLbl!=''">
										  <td><xsl:value-of select='$custlinePOLbl'/>:&#160;</td>
										  </xsl:when>
										  <xsl:otherwise><td>Line PO#:&#160;</td></xsl:otherwise>
									 </xsl:choose>
								  <td><xsl:value-of select="@CustomerPONo"/></td>
								</xsl:if>  
								</tr>
								<tr>
								<xsl:if test = 'Extn/@ExtnCustLineAccNo!=""' >
								<xsl:choose>
									<xsl:when test="$custlineAcctLbl!=''">
										<td> <xsl:value-of select='$custlineAcctLbl'/>:&#160;</td>
									</xsl:when>
									<xsl:otherwise><td>Cust Acct Line #:&#160;</td></xsl:otherwise>
								</xsl:choose>								 
								  <td><xsl:value-of select="Extn/@ExtnCustLineAccNo"/></td>
								</xsl:if>
								</tr>
							  </table>
							</td>	
							</xsl:if>				       
					       </tr>
					        	<tr>
						        	<td colspan="4" style="padding-bottom:10px;border-bottom:1px solid #cccccc;">
										<table>
											<tr>
											<xsl:if test = 'Item/@ItemID!=""' >
												<td style="padding-right:30px;"><xsl:value-of select='$storeFront' />  item #: <xsl:value-of select="Item/@ItemID"/></td>
											</xsl:if>
											<xsl:if test = '@LineType!="M"' >
												<xsl:if test = 'Item/@ManufacturerItem!=""' >
												<td style="padding-right:30px;">Mfg. Item #: <xsl:value-of select="Item/@ManufacturerItem"/></td>												
											</xsl:if>
											<xsl:if test = 'Item/@CustomerItem!=""' >
											<td>My Item #: <xsl:value-of select="Item/@CustomerItem"/></td>
											</xsl:if>	
											</xsl:if>
											</tr>
										</table>
									</td>
							 		<td colspan="3" style="padding-bottom:10px;border-bottom:1px solid #cccccc;"></td>
						 		
					        	</tr>
					        <!-- if (specialInstructions is not null) -->
							<xsl:if test = 'Instructions/Instruction/@InstructionText!=""'>
					         <tr>				         
						         <td colspan="7" style="padding:0px; border-bottom:1px solid #cccccc;" >
									<table>
										<tr >
											<td valign="top" style="border-right:1px solid #cccccc; padding:5px;">Special Instructions:</td>
											<td valign="top" style="padding:5px;"><xsl:value-of select="Instructions/Instruction/@InstructionText"/></td>
										</tr>
									</table>
								</td>					         
					        </tr>
					   	</xsl:if>
					   
						
					  </xsl:for-each>
					</tbody>
					  </table>
					 
			</td>
			</tr>
				</table>
					</td>
					</tr>
					<xsl:if test='$viewPricesFlag ="Y"'>
					<tr>					
					<td align="right" style="padding-bottom:25px;">
										
          				<table class="order-total">
						<tr>
							<td class="totalLabelText">Subtotal:</td>
							<td>
								<xsl:choose>
									<xsl:when test='not(Order/Extn/@ExtnOrderSubTotal)'>$0.00</xsl:when>
									<xsl:when test='Order/Extn/@ExtnOrderSubTotal =""'>$0.00</xsl:when>
									<xsl:when test='Order/Extn/@ExtnOrderSubTotal ="0.00"'>$0.00</xsl:when>
									<xsl:otherwise><xsl:value-of select='format-number(Order/Extn/@ExtnOrderSubTotal,"$#,###,###,##0.00#")'></xsl:value-of>
									</xsl:otherwise>
								</xsl:choose>
							</td>				
						</tr>
					<tr>
						<td class="totalLabelText">Order Total Adjustments:</td>
						<td>
							<xsl:choose>
								<xsl:when test='not(Order/Extn/@ExtnTotOrderAdjustments)'>$0.00</xsl:when>
								<xsl:when test='Order/Extn/@ExtnTotOrderAdjustments =""'>$0.00</xsl:when>
								<xsl:when test='Order/Extn/@ExtnTotOrderAdjustments ="0.00"'>$0.00</xsl:when>
								<xsl:otherwise><xsl:value-of select='format-number(Order/Extn/@ExtnTotOrderAdjustments,"$#,###,###,##0.00#")'></xsl:value-of>
								</xsl:otherwise>
							</xsl:choose>
						</td>	
					</tr>
					<tr>
						<td class="totalLabelText">Adjusted Subtotal:</td>
						 <td>
							 <xsl:choose>
									<xsl:when test='not(Order/Extn/@ExtnTotOrdValWithoutTaxes)'>$0.00</xsl:when>
									<xsl:when test='Order/Extn/@ExtnTotOrdValWithoutTaxes =""'>$0.00</xsl:when>
									<xsl:when test='Order/Extn/@ExtnTotOrdValWithoutTaxes ="0.00"'>$0.00</xsl:when>
									<xsl:otherwise><xsl:value-of select='format-number(Order/Extn/@ExtnTotOrdValWithoutTaxes,"$#,###,###,##0.00#")'></xsl:value-of>
									</xsl:otherwise>
							</xsl:choose>
						</td>
					</tr>
					<tr>
						<td class="totalLabelText">Tax:</td>						
							<xsl:choose>
				              	<xsl:when test='Order/@MaxOrderStatus="1100.5700" or Order/@MaxOrderStatus="1100.5950" or Order/@MaxOrderStatus="1100.5750"' >				              		
					              	<td>$<xsl:value-of select="Order/Extn/@ExtnOrderTax"/></td>						              							
								</xsl:when>
								<xsl:otherwise>									
									<td><span class="tbd">To be determined</span></td>
								</xsl:otherwise>
							</xsl:choose>						
					</tr>
					<tr>
						<td class="totalLabelText">Shipping and Handling:</td>						
						<xsl:choose>
							<xsl:when test='Order/@MaxOrderStatus="1100.5700" or Order/@MaxOrderStatus="1100.5950" or Order/@MaxOrderStatus="1100.5750"' >
								<td>$<xsl:value-of select="Order/Extn/@ExtnTotalOrderFreight"/></td>								
							</xsl:when>
						<xsl:otherwise>
							<td><span class="tbd">To be determined</span></td>
						</xsl:otherwise>
						</xsl:choose>
					</tr>

					<tr class="last">
						<td class="totalLabelText">Order Total (USD):</td>
						<td>
							<xsl:choose>
								<xsl:when test='not(Order/Extn/@ExtnTotalOrderValue)'>$0.00</xsl:when>
								<xsl:when test='Order/Extn/@ExtnTotalOrderValue =""'>$0.00</xsl:when>
								<xsl:when test='Order/Extn/@ExtnTotalOrderValue ="0.00"'>$0.00</xsl:when>
								<xsl:when test='Order/Extn/@ExtnTotalOrderValue ="0.00"'>
								</xsl:when>
								<xsl:otherwise><xsl:value-of select='format-number(Order/Extn/@ExtnTotalOrderValue,"$#,###,###,##0.00#")'></xsl:value-of>
								</xsl:otherwise>
							</xsl:choose>
						</td>
				 </tr>
                
						</table>
        </td>
      </tr>
      
						</xsl:if>
						<tr>
						<td align="left"><span class="footer">This document merely confirms your order, it is not an acceptance of your order. Additional fees may apply to accepted orders.</span></td>
						</tr>
    			</table>
			</body>
		</HTML>
		
	</xsl:template>
</xsl:stylesheet>