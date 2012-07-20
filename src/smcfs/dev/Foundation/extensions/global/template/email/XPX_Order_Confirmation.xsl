<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:emailformatters="com.yantra.pca.email.formatters"
    xmlns:java="java"
    exclude-result-prefixes="java emailformatters">

<xsl:template name="applyStyle">
	<xsl:comment>CONTENT_TYPE=text/html; charset=UTF-8</xsl:comment>
	<HEAD>
	    <STYLE TYPE="text/css">

		.table  {
		    padding:0;
		    font-size: 12;
		    font-family: Tahoma;
		    font-weight: normal;
		    color: #000000;
		    width: 100%;
		    border: 1;
		}
        
        
		.tablecolumn{
		    padding-left:2px; 
		    padding-right:2px; 
		    padding-top: 0px;
		    padding-bottom: 0px;
		    font-size: 12;
		    vertical-align: top;
		    text-align: left;
		}
		.numerictablecolumn{
		    padding-left:2px; 
		    padding-right:2px; 
		    padding-top: 0px;
		    padding-bottom: 0px;
		    vertical-align: top;
		    text-align: right;
		    font-size: 12;
		}
		.tablecolumnheader {
		    border-left:1px solid buttonhighlight;
		    border-right:1px solid buttonshadow;
		    border-top:1px solid buttonhighlight;
		    border-bottom:1px solid buttonshadow;
		    PADDING-LEFT: 2px;
		    PADDING-RIGHT: 2px;
		    PADDING-top: 0px;
		    PADDING-bottom: 0px;
		    VERTICAL-ALIGN: middle;
		    HORIZONTAL-ALIGN: center;
		    BACKGROUND-COLOR: #e0e0e0;
		    TEXT-ALIGN: left
		}
			html {
				width: 7.5in;
				font-family: Arial, Helvetica, Sans-serif;
				font-size: 12px;
			}
			table {
				
				
				font-size: 12px;
				border-collapse: collapse;
			}
			table.price {
				border: 1px solid #ccc;
				border-radius: 5px;
				border-collapse:0px;
				
			}
			table.price td {
				padding: 2px;
				}
			table.price th{
				background-color: #003399;
				color: white;
				padding: 10px;
				font-size: 11px; 
				margin:2px;

			}
			table.price tr.special-instructions td{
				border-top: 1px solid #ccc;
				border-bottom: 1px solid #ccc;
				
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
			a:hover {
				color: #003399;
				text-decoration: underline;
			}
			ul {
				padding-left: 20px;
			}
			div {
				border: 1px solid #ccc;
				padding: 5px;
				border-radius: 5px;
			}
			.bold {
				font-weight: 700;
			}
			.logo {
				margin-bottom: 10px;
			}
			.align-right {
				text-align: right;
			}
			table.price td.right, table.price th.right{
				padding-right: 0px;
				text-align:right;
			}
			table.price td.right:last-of-type, table.price th.right:last-of-type{
				padding-right: 5px;
			}
			table.price td.left{
				padding-left: 0px;
			}
			table.order-total {
				border: 1px solid #ccc;
				width:25%;
				border-radius: 8px;
				
			}
			.tbd{
				color: red;
				font-weight: bold;
			}
			table.order-total td {
				padding: 2px;
			}
			table.order-total tr.last td {
				font-weight: bold;
				background-color: #ccc;				
			}
			
			table.order-total td.last{
				text-align:right;			
			}
			
			table.order-total tr td:last-of-type {
				text-align:right;
			}
			p.bottom {
				
			}
			p.short-desc {
				width: 250px;
				word-wrap: break-word;
			}
			ul.long-desc li {
				width: 250px;
				word-wrap: break-word;				
			}
			div.si-label {
				border: 0px;
				float: left;
				width: 115px;
				padding-right: 0px;
				padding-left: 0px;
			}
			div.si-text {
				border: 0px;
				float: right;
				word-wrap: break-word;
				width: 573px; 
				padding-left: 0px;
			}
			div.clearall {
				clear: both;
				height: 0px;
				border: 0px; 
			}
			table.inner td{
				border-top: 0px !important;
				border-bottom: 0px !important;
			}
			table.inner td.first{
				border-right: 1px solid #cccccc;
				width: 115px;
			}
			table.inner td.last{
				padding-left: 4px;
			}
			tr.specialcharges td {
				border-top: 1px solid #cccccc;
			}
			.widthLeft {
				width: 22%;
			}
			.widthRight {
				width: 82%;
			}
			
			
			

	    </STYLE>

	</HEAD>
</xsl:template>

	<xsl:template match="Order">
		</xsl:template>
	
		<xsl:variable name="shipToID" >
			<xsl:value-of select="Order/@ShipToID" />
		</xsl:variable>

		<xsl:variable name="IsOrderSplit" >
			<xsl:value-of select="Order/@IsOrderSplit" />
		</xsl:variable>
		
		<xsl:variable name="maxDelim" select="'-M'"/>
		<xsl:variable name="accDelim" select="'-A'"/>	
		
		<xsl:variable name="shipToName" >
			<xsl:value-of select="Order/Extn/@ExtnShipToName" />
		</xsl:variable>
		
		<xsl:variable name="urlPrefix" select="'https://www.'"/>	
		<xsl:variable name="urlSuffix" select="'.com'"/>
		
		<xsl:variable name="storeFront" >
			<xsl:value-of select="Order/@SellerOrganizationCode" />
		</xsl:variable>
		
		<xsl:variable name="storeFrontURL">    
			<xsl:value-of select="concat($urlPrefix, $storeFront, $urlSuffix)"/>   
		</xsl:variable> 
		
		<xsl:variable name="brandLogo" >
			<xsl:value-of select="Order/@BrandLogo" />
		</xsl:variable>	
		
    <xsl:template match="/">
		<HTML>
			<xsl:call-template name="applyStyle"/>
			<BODY topmargin="0" leftmargin="0" STYLE="font:normal 10pt Tahoma" align="left" >
			
			<table  width="60%" border="0" halign="left" cellpadding="0" cellspacing="0" style="margin-left:5px">
				<tr>	
					<td >
						<table width="100%" border="0" >
							<tr>
								<td >
								</td>
								<td width="100%">
								<img src="{$brandLogo}" width="216" height="69" alt="xpedx" longdesc="http://www.xpedx.com" />
								</td>
							</tr>
						</table> 
					    
					</td>
				</tr>
				<tr>
					<td>
					&#160;
					</td>
					</tr>
					
					
				
			<tr align="right">
					<td colspan="1" rowspan="1" style="font-family: Arial, Geneva, sans-serif;font-size:0px; color:#000;" >
					<table width="100%" border="0" cellpadding="4" cellspacing="4" style="border:solid 1px #ccc;border-bottom:none;">
					<tr>
					<td width="100%">
					<xsl:choose>
					<xsl:when test = 'Order/@EnvironmentID="STAGING"' >
					<a href="https://stg.xpedx.com/order">Click here</a>  to review this order on <xsl:value-of select="Order/@SellerOrganizationCode"/>.com/order. 
					</xsl:when>
					<xsl:when test = 'Order/@EnvironmentID="DEVELOPMENT"' >
					<a href="http://172.20.137.37:8001/swc/home/home.action?scFlag=Y">Click here</a>  to review this order on <xsl:value-of select="Order/@SellerOrganizationCode"/>.com/order. 
					</xsl:when>
					<xsl:otherwise>
					<a href="{$storeFrontURL}/order">Click here</a>  to review this order on <xsl:value-of select="Order/@SellerOrganizationCode"/>.com/order. 
					</xsl:otherwise>
					</xsl:choose>
					</td>
					</tr>
					<tr>
					<td width="100%">
					This is a courtesy notification that an order has been placed or changed at <xsl:value-of select="Order/@SellerOrganizationCode"/>.com/order. Thank you for your business!
					</td>
					</tr>
					</table>
										
			<table width="100%"  cellpadding="4" cellspacing="4" style="border:solid 1px #ccc;border-top:none;">														
				<thead>
					<tr>
						<th colspan="2"> Order Information: </th>
						<th colspan="2"> Shipping Information: </th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td width="60%" colspan="2" style="vertical-align:top">	
							<table>
								<tr>
									<td class="widthLeft">Web Confirmation: </td>
									<td class="widthRight"><xsl:value-of select="Order/Extn/@ExtnWebConfNum"/> </td>
								</tr>
								<tr>
									<td class="widthLeft">Order #: </td>
									<td class="widthRight"><xsl:value-of select="Order/@OrderNo"/> </td>
								</tr>
								<tr>
									<td class="widthLeft">Order Status: </td>
									<td class="widthRight"><xsl:value-of select="Order/@Status"/> </td>
								</tr>
								<tr>
									<td class="widthLeft">PO #: </td>
									<td class="widthRight"><xsl:value-of select="Order/@CustomerPONo"/> </td>
								</tr>
								<tr>
									<td class="widthLeft">Ordered By: </td>
									<td class="widthRight"><xsl:value-of select="Order/Extn/@ExtnOrderedByName"/> </td>
								</tr>	
						 	</table>
						 	
						</td>	
						
						<td width="40%" colspan="2" style="vertical-align:top">
					
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
				
				<tr>
					<td colspan="3"> <span class="bold"> Expected Delivery Date: </span>
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
						
					</td></tr>	
					<tr> <!-- This row is intentionally left blank for spacing. Do not alter this row. -->
						<td></td>
						<td></td>
						<td> </td>

					</tr><!-- End intentionally blank row. -->
					<tr>
						<td colspan="3"> <span class="bold"> Shipping Options: </span>
						<xsl:if test = 'Order/Extn/@ExtnShipComplete!="N"' >	
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
						
						 </td>
					</tr>
					<tr> 

						<td> </td>
						<td> </td>
						<td> </td>
					</tr>
					<tr> 

						<td> </td>
						<td> </td>
						<td> </td>
					</tr>
					
					<tr>
					<td colspan="3"> <span class="bold"> Order Comments: </span>
						<xsl:value-of select="Order/Instructions/Instruction[@InstructionType='HEADER']/@InstructionText" />
						</td>

					</tr>
				</tbody>
		</table>
					</td>
				</tr>
				<tr align="right">
				<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
				<table class="price" width="100%">
			<thead >
				<th> </th>
				<th> </th>
				<th> </th>
				<th valign="top" class="right"> My Price (USD) </th> <!-- The currency code on this line is dynamic.-->
				<th class="right" >Extended Price (USD)</th> <!-- The currency code on this line is dynamic.-->
                </thead>
			<xsl:for-each select="Order/OrderLines/OrderLine">						  
								<xsl:sort select="Extn/@ExtnLegacyLineNumber"/>
													
			<tbody>
				<tr>
					
					<td rowspan="7" valign="top">
					<xsl:value-of select="Item/@ItemShortDesc" disable-output-escaping="yes"/>
					<BR/>
					<xsl:if test = '@LineType!="M"' >					
					<xsl:value-of select="Item/@ItemDesc" disable-output-escaping="yes"/>
					</xsl:if>
					</td>
					
					<xsl:choose>
					<xsl:when test='@LineType!="M"'> 															
					<td width="20%" class="right"> Ordered Qty:&#160;</td>
					<td class="left"><xsl:value-of select='format-number(OrderLineTranQuantity/@OrderedQty,"#")'/>&#160;<xsl:value-of select="OrderLineTranQuantity/@UOMDescription"/></td>
					<td class="align-right">
					<xsl:choose>
					<xsl:when test='Extn/@ExtnUnitPrice ="0.00"'><span class="tbd">Call for price</span></xsl:when>
					<xsl:when test='Extn/@ExtnUnitPrice =""'><span class="tbd">Call for price</span></xsl:when>
					<xsl:otherwise><xsl:value-of select='format-number(Extn/@ExtnUnitPrice,"$#,###,###,###.00000")'/></xsl:otherwise>
					</xsl:choose>
					 </td>
					<td class="align-right">
					<xsl:choose>
					<xsl:when test='Extn/@ExtnExtendedPrice =""'><span class="tbd">To be determined</span></xsl:when>
					<xsl:when test='Extn/@ExtnExtendedPrice ="0.00"'><span class="tbd">To be determined</span></xsl:when>
					<xsl:otherwise><xsl:value-of select='format-number(Extn/@ExtnExtendedPrice,"$#,###,###,###.00")'/></xsl:otherwise>
					</xsl:choose>
					</td>
					</xsl:when>
					<xsl:otherwise>
					<td class="right"> </td>
					<td class="left"></td>
					<td class="right">
					 </td>
					 <td class="align-right">
					 <xsl:choose>
					<xsl:when test='Extn/@ExtnExtendedPrice =""'><span class="tbd">To be determined</span></xsl:when>
					<xsl:when test='Extn/@ExtnExtendedPrice ="0.00"'><span class="tbd">To be determined</span></xsl:when>
					<xsl:otherwise><xsl:value-of select='format-number(Extn/@ExtnExtendedPrice,"$#,###,###,###.00")'/></xsl:otherwise>
					</xsl:choose>
			
					</td>
					
					</xsl:otherwise>
					</xsl:choose>
					
					
					
					
					
				</tr>
				<tr>

					<!-- <td> This cell is occupied via the rowspan property in the first row. Do not change. </td> --> 
					<xsl:if test = '@LineType!="M"' >
								
						<xsl:if test = '(Extn/@ExtnReqShipOrdQty!="") and  ($IsOrderSplit ="N") ' >	
					
					<td class="right"> Shippable Qty:&#160;</td>
					<td class="left"><xsl:value-of select='format-number(Extn/@ExtnReqShipOrdQty,"#")'/>&#160;<xsl:value-of select="OrderLineTranQuantity/@UOMDescription"/></td> 
					<td class="align-right">
					<xsl:choose>
					<xsl:when test='Extn/@ExtnUnitPrice =""'>
					</xsl:when>
					<xsl:when test='Extn/@ExtnUnitPrice ="0.00"'>
					</xsl:when>
					<xsl:otherwise>per <xsl:value-of select='Extn/@ExtnPricingUOMDescription'/></xsl:otherwise>
					</xsl:choose>
					</td>
					<td class="right"></td>
					    </xsl:if>
					</xsl:if>
				</tr>

				<tr>
					<!-- <td> This cell is occupied via the rowspan property in the first row. Do not change. </td> --> 
					<xsl:if test = '@LineType!="M"' >					
						<xsl:if test = 'Extn/@ExtnReqBackOrdQty!="" and  ($IsOrderSplit ="N")' >	
					
					<td class="right"> Backorder Qty:&#160; </td>
					<td class="left"><xsl:value-of select='format-number(Extn/@ExtnReqBackOrdQty,"#")'/>&#160;<xsl:value-of select="OrderLineTranQuantity/@UOMDescription"/></td>
					<td class="right"></td>
					<td class="right"></td>
					</xsl:if>
                     </xsl:if>
				</tr>
				<tr>
				<xsl:if test = '@LineType!="M"' >					
						
				<xsl:if test = '@lineOrderNO!=""' >					
					
					<!-- <td> This cell is occupied via the rowspan property in the first row. Do not change. </td> --> 
					<td class="right"><xsl:value-of select="OrderLineTranQuantity/@OrderedQty"/><xsl:value-of select="OrderLineTranQuantity/@UOMDescription"/>  </td>
					<td class="left"></td>
					<td class="right"></td>
					<td class="right"></td>
					</xsl:if>
					</xsl:if>
				</tr>
				<tr>
				<xsl:if test = '@LineType!="M"' >					
						
				<xsl:if test = '@lineOrderNO!=""' >					
					
					<!-- <td> This cell is occupied via the rowspan property in the first row. Do not change. </td> --> 
					<td class="right"><xsl:value-of select="@Status"/>  </td>
					<td class="left"></td>
					<td class="right"></td>
					<td class="right"></td>
					</xsl:if>
					</xsl:if>
				</tr>
				
				<tr>
					<!-- <td> This cell is occupied via the rowspan property in the first row. Do not change. </td> --> 
					<xsl:if test = '@LineType!="M"' >					
						
					<xsl:if test = '@CustomerLinePONo!=""' >					
					
					<td class="right"> Line PO#:&#160;</td>
					<td class="left"><xsl:value-of select="@CustomerLinePONo"/></td>
					<td class="right"></td>
					<td class="right"></td>
					</xsl:if>
					</xsl:if>

				</tr>
				<tr>
				<xsl:if test = '@LineType!="M"' >					
						
				<xsl:if test = 'Extn/@ExtnCustLineAccNo!=""' >					
					
					<!-- <td> This cell is occupied via the rowspan property in the first row. Do not change. </td> --> 
					<td class="right" valign="top"> Cust Acct Line #:&#160;</td>
					<td class="left" valign="top"><xsl:value-of select="Extn/@ExtnCustLineAccNo"/></td>
					<td class="right"></td>
					<td class="right"></td>
					</xsl:if>
					</xsl:if>
				</tr>

				<tr>
				<xsl:if test = 'Item/@ItemID!=""' >					
				
					<td><span class="itemno">xpedx item #: <xsl:value-of select="Item/@ItemID"/></span> </td>
					</xsl:if>
					<xsl:if test = '@LineType!="M"' >					
						
					<xsl:if test = 'Item/@ManufacturerItem!=""' >					
				
					<td class="right"> Mfg. Item #:</td>
					<td class="left"><xsl:value-of select="Item/@ManufacturerItem"/></td>
					</xsl:if>
					<xsl:if test = 'Item/@CustomerItem!=""' >					
				
					<td class="right">My Item #:</td>
					<td class="left"><xsl:value-of select="Item/@CustomerItem"/></td>
                    </xsl:if>
                    </xsl:if>
				</tr>
				<tr>
				<td colspan="5" style="border-bottom:1px solid #ccc;" >
				</td>
				</tr>
				
				<!-- if (specialInstructions is not null) -->
				<xsl:if test = 'Instructions/Instruction/@InstructionText!=""'>
				<tr class="special-instructions"> 
					<td colspan="5"> 
						<table class="inner">
							<tr>
								
								<td class="first" valign="top"> Special Instructions:</td>
								<td class="last"><xsl:value-of select="Instructions/Instruction/@InstructionText"/></td>
							
							</tr>
						</table>
					 </td>
				</tr>
				</xsl:if>
							</tbody>
			</xsl:for-each>
			</table>
					</td>
					</tr>
					<tr>
					<td>
					&#160;
					</td>
					</tr>
					<tr>
					<td >
					<table class="order-total" align="right">
						<tr>

				<td>Subtotal:</td>
				<td>
				<td class="align-right"><xsl:value-of select='format-number(Order/Extn/@ExtnOrderSubTotal,"$#,###,###,###.00")'></xsl:value-of></td>
				</td>
			</tr>
			<tr>
				<td> Order Total Adjustments:</td>
				<td>
				<td class="align-right"><xsl:value-of select='format-number(Order/Extn/@ExtnTotOrderAdjustments,"$#,###,###,###.00")'/></td>
				</td>
			</tr>
			<tr>
			<td>Adjusted Subtotal:</td>
				 <td>
				 <td class="align-right"><xsl:value-of select='format-number(Order/Extn/@ExtnTotOrdValWithoutTaxes,"$#,###,###,###.00")'/></td>
				 </td>
			 </tr>
			<tr>
				<td>Tax:</td>
				<xsl:choose>
	              	<xsl:when test='Order/@MaxOrderStatus="1100.5700" or Order/@MaxOrderStatus="1100.5950" or Order/@MaxOrderStatus="1100.5750"' >
	              		<td>
		              	<td class="align-right">$<xsl:value-of select="Order/Extn/@ExtnOrderTax"/></td>	
		              	</td>						
					</xsl:when>
					<xsl:otherwise>
						<td>
						<td class="align-right"><span class="tbd">To be determined</span></td>
						</td>
					</xsl:otherwise>
				</xsl:choose>
			</tr>
			<tr>
				<td> Shipping and Handling:</td>
				<xsl:choose>
					<xsl:when test='Order/@MaxOrderStatus="1100.5700" or Order/@MaxOrderStatus="1100.5950" or Order/@MaxOrderStatus="1100.5750"' >
						<td>												
						<td class="align-right">$<xsl:value-of select="Order/Extn/@ExtnTotalOrderFreight"/></td>
						</td>
					</xsl:when>
				<xsl:otherwise>
					<td>
					<td class="align-right"><span class="tbd">To be determined</span></td>
					</td>
				</xsl:otherwise></xsl:choose>
			</tr>

			<tr class="last">
				<td> Order Total (USD):</td>
				<td>
				 <td class="align-right"><xsl:value-of select='format-number(Order/Extn/@ExtnTotalOrderValue,"$#,###,###,###.00")'/></td>
				 </td>
			
				 </tr>

						</table>
						<tr>
					<td>
					&#160;
					</td>
					</tr>
						
						</td></tr>
					<tr align="right"><td>
					<table width="100%" border="0" align="" cellpadding="1" cellspacing="1" style="margin-left:5px;border-collapse:inherit;text-align:left;" >
					<tr>
					<td style="text-wrap:7.5in;width:720px;">
					This document merely confirms your order, it is not an acceptance of your order. Additional fees may apply to accepted orders. 
					Please do not reply to this email. This mailbox is not monitored and you will not receive a response.
						</td>
				</tr>
			</table></td></tr>
			
	</table>
						
			</BODY>
		</HTML>
		
	</xsl:template>
</xsl:stylesheet>