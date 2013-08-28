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
				border-spacing: 0px;
				width: 80%;
				font-size: 12px;
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
				background-color: ##003399;
				color: white;
				padding: 10px;
				font-size: 11px;
			}
			table.saalfeldprice th{
				background-color: ##084823;
				color: white;
				padding: 10px;
				padding-right: 2px;
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
				border: 1px solid #ccc;;
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
				width:30%;
				position:relative;
				left:80%;
				border-radius: 5px;
				padding: 5px;
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
					
	    </STYLE>

	</HEAD>
</xsl:template>

	<xsl:template match="Order">
		</xsl:template>
	
		<xsl:variable name="shipToID" >
			<xsl:value-of select="Order/@ShipToID" />
		</xsl:variable>

		<xsl:variable name="maxDelim" select="'-M'"/>
		<xsl:variable name="accDelim" select="'-A'"/>	
			
		<xsl:variable name="shipToName" >
			<xsl:value-of select="Order/Extn/@ExtnShipToName" />
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
		
		<xsl:variable name="devSaalUrl" select="'dev.saalfeldredistribution.com/order'"/>
		<xsl:variable name="devxpedxUrl" select="'http://xpappd01.ipaper.com:8001/swc/home/home.action?sfId=xpedx'"/>
		<xsl:variable name="stgSaalUrl" select="'http://stg.saalfeldredistribution.com/'"/>
		<xsl:variable name="stgxpedxUrl" select="'http://stg.xpedx.com/'"/>
		

	<xsl:template match="/">
	<table width="600" border="0" align="center" cellpadding="2" cellspacing="2" topmargin="0" leftmargin="0" STYLE="font:normal 10pt Tahoma">
	<tr>
	<td height="44">
	</td></tr></table>
		<HTML>
			<xsl:call-template name="applyStyle"/>
			<BODY topmargin="0" leftmargin="0" STYLE="font:normal 10pt Tahoma">
			<table width="100%" border="0" align="center" cellpadding="2" cellspacing="2">
				<tr>
					<td>
						<img src="{$brandLogo}" width="216" height="69" alt="xpedx" longdesc="http://www.xpedx.com" />
					</td>
				</tr>
				
				<tr>
					<td style="font-family: Arial, Geneva, sans-serif;font-size:0px; color:#000;" >
					<table width="100%" border="0" cellpadding="0"  style="border:solid 1px #999;  padding:20px 0px 20px 20px;">
					<tr>
					
					
					<td>
					
					Your order has been rejected.If you have any question,please contact your order approver. Click here to review on 
					
					 <xsl:choose>
							<xsl:when test = 'Order/@EnvironmentID="STAGING"'>
								<xsl:if test = 'Order/@EnterpriseCode="xpedx"'>
									<a href="{$stgxpedxUrl}/order" >Click here</a>  to review this order on <xsl:value-of select="Order/@EnterpriseCode"/>.com/order.
								</xsl:if>
								<xsl:if test = 'Order/@EnterpriseCode="Saalfeld"'>
									<a href="{$stgSaalUrl}/order" color="084823">Click here</a>  to review this order on <xsl:value-of select="Order/@EnterpriseCode"/>redistribution.com/order.
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
					
												</td>
									</tr>
						</table>
						<table width="500%" border="0" cellpadding="0"  style="border:solid 1px #999;  padding:0px 0px 0px 0px;">
						<thead>
					<tr>
						
						<th>  </th>
						<th>  </th>
					</tr>
				</thead>
						
														
						<thead>
					<tr>
						<th> Order Information: </th>

						<th>  </th>
						<th> Shipping Information: </th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td> Web Confirmation: </td>

						<td> <xsl:value-of select="Order/Extn/@ExtnWebConfNum"/>
						 </td>
					</tr>
					<tr>
						<td> Order #: </td>

						<td> <xsl:value-of select="Order/@OrderNo"/> </td>
						<td>
						<xsl:if test="(contains($shipToID,$maxDelim))">							
						
								<xsl:value-of select="substring-before($shipToID, $maxDelim)" />
								,
								<span style="padding-left:10px">
									<xsl:value-of select="$shipToName" />
								</span>
								</xsl:if>
							<xsl:if test="(contains($shipToID,$accDelim))">
								<xsl:value-of select="substring-before($shipToID, $accDelim)" />
								,
								<span style="padding-left:10px">
									<xsl:value-of select="$shipToName" />
								</span>
							</xsl:if>
						
						</td>
											</tr>
					<tr>
						<td> Order Status: </td>

						<td> <xsl:value-of select="Order/@Status"/> </td>
						<!-- if (attention field is not null) -->
						<xsl:if test="Order/Extn/@ExtnAttentionName">
						<td> <xsl:value-of select="Order/Extn/@ExtnAttentionName"/> </td>
						</xsl:if>
					</tr>
					<tr>

						<td> PO #: </td>
						<td> <xsl:value-of select="Order/@CustomerPONo"/> </td>
						<!-- if (attention field is not null) -->
						<xsl:if test = 'Order/PersonInfoBillTo/@AddressLine1!=""' >
								<td> <xsl:value-of select="Order/PersonInfoShipTo/@AddressLine1"/> </td>
								<xsl:if test = 'Order/PersonInfoBillTo/@AddressLine2!=""' >
								,
								</xsl:if>
						</xsl:if>
						
					</tr>
					<tr>
						<td> Ordered By: </td>
						<td><xsl:value-of select="Order/@Createuserid"/></td><!-- if (attention field is not null) -->
								<xsl:if test = 'Order/PersonInfoBillTo/@AddressLine2!=""' >
								<td> <xsl:value-of select="Order/PersonInfoShipTo/@AddressLine2"/> </td>
								<xsl:if test = 'Order/PersonInfoBillTo/@AddressLine3!=""' >
								,
								</xsl:if>
						</xsl:if>
						
						<!-- /if                          
							 else if (address line 3 is not null)                          
								<td> &lt;address line 3&gt; </td>
						     /else                          -->
					</tr>					
					<tr>
						<td> </td>
						<td> </td>
						<!-- if (attention field is not null) -->
								<xsl:if test = 'Order/PersonInfoBillTo/@AddressLine3!=""' >
								<td> <xsl:value-of select="Order/PersonInfoShipTo/@AddressLine3"/> </td>
								</xsl:if>
						
						<!-- /if                          
							 else                            
								<td> &lt;city&gt;, &lt;state&gt; &lt;postal code&gt; &lt;country code&gt;  </td>
						     /else                          -->
					</tr>					
					<tr>
						<td> </td>
						<td> </td>
						<!-- if (attention field is not null) -->
								<td> <xsl:value-of select="Order/PersonInfoShipTo/@City"/>, <xsl:value-of select="Order/PersonInfoShipTo/@State"/>,<xsl:value-of select="Order/PersonInfoShipTo/Extn/@ExtnZip4"/>,<xsl:value-of select="Order/PersonInfoShipTo/@Country"/> </td>

						<!-- /if                          
							 else                             
								<td> &nbsp; </td>
						     /else                            -->
					</tr>
					<tr> <!-- This row is intentionally left blank for spacing. Do not alter this row. -->
						<td></td>
						<td></td>
						<td> </td>

					</tr><!-- End intentionally blank row. -->
					<tr>
						<td colspan="3"> <span class="bold"> Shipping Options: </span> </td>
					</tr>
					<tr> <!-- This row is intentionally left blank. Do not alter this row. -->

						<td> </td>
						<td> </td>
						<td> </td>
					</tr><!-- End intentionally blank row. -->
					<tr>
						<td colspan="3"> <span class="bold"> Order Comments: </span>
						<xsl:value-of select="Instructions/Instruction[@InstructionType='HEADER']/@InstructionText" />
						</td>

					</tr>
				</tbody>
	</table>
					</td>
				</tr>
				<tr>
				<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
				
							<table class="price" width="100%">
						
						<xsl:if test = 'Order/@EnterpriseCode="xpedx"'>
								<thead >
									<th> </th>
									<th> </th>
									<th> </th>
									<th valign="top" class="right"> My Price (USD) </th> <!-- The currency code on this line is dynamic.-->
									
									<th class="right" >Extended Price (USD)</th> <!-- The currency code on this line is dynamic.-->
					                </thead>
						</xsl:if>
						<xsl:if test = 'Order/@EnterpriseCode="Saalfeld"'>
								<thead class="table.saalfeldprice">
								<th> </th>
								<th> </th>
								<th> </th>
								<th valign="top" class="right"> My Price (USD) </th> <!-- The currency code on this line is dynamic.-->

								<th class="right" >Extended Price (USD)</th> <!-- The currency code on this line is dynamic.-->
				                </thead>
						</xsl:if>
			<xsl:for-each select="Order/OrderLines/OrderLine">						  
													
			<tbody>
				<tr>
					<td rowspan="7" valign="top">
					<xsl:value-of select="Item/@ItemDesc" disable-output-escaping="yes"/>
											</td>
					<td class="right"> Ordered Qty:</td>

					<td class="left"><xsl:value-of select="OrderLineTranQuantity/@OrderedQty"/><xsl:value-of select="OrderLineTranQuantity/@TransactionalUOM"/></td>
					<td class="right"> <xsl:value-of select="Extn/@ExtnUnitPrice"/>
					<xsl:if test = 'Extn/@ExtnPricingUOM!=""' >					
					/<xsl:value-of select="Extn/@ExtnPricingUOM"/> 
					</xsl:if>
					 </td>
					<td class="right"><xsl:value-of select="LineOverallTotals/@ExtendedPrice"/></td>
				</tr>
				<tr>

					<!-- <td> This cell is occupied via the rowspan property in the first row. Do not change. </td> --> 
					<td class="right"> Shippable Qty:</td>
					<td class="left"><xsl:value-of select="Extn/@ExtnReqShipOrdQty"/></td>
					<td class="right"><xsl:value-of select="Extn/@ExtnLineShippableTotal"/></td>
					<td class="right"></td>
				</tr>

				<tr>
					<!-- <td> This cell is occupied via the rowspan property in the first row. Do not change. </td> --> 
					<td class="right"> Backorder Qty:</td>
					<td class="left"><xsl:value-of select="Extn/@ExtnReqBackOrdQty"/></td>
					<td class="right"><xsl:value-of select="Extn/@ExtnBackQtyBase"/></td>
					<td class="right"></td>

				</tr>
				<tr>
					<!-- <td> This cell is occupied via the rowspan property in the first row. Do not change. </td> --> 
					<td class="right"> Line Status:</td>
					<td class="left"><xsl:value-of select="Extn/@ExtnLineStatusCode"/> </td>
					<td class="right"></td>

					<td class="right"></td>
				</tr>
				<tr>
					<!-- <td> This cell is occupied via the rowspan property in the first row. Do not change. </td> --> 
					<td class="right"><xsl:value-of select="Extn/@ExtnLineStatusCode"/>  </td>
					<td class="left"></td>
					<td class="right"></td>
					<td class="right"></td>
				</tr>
				<tr>
					<!-- <td> This cell is occupied via the rowspan property in the first row. Do not change. </td> --> 
					<td class="right"> Line PO#:</td>
					<td class="left"><xsl:value-of select="Extn/@CustomerLinePONo"/></td>
					<td class="right"></td>
					<td class="right"></td>

				</tr>
				<tr>
					<!-- <td> This cell is occupied via the rowspan property in the first row. Do not change. </td> --> 
					<td class="right" valign="top"> Cust Acct Line #:</td>
					<td class="left"><xsl:value-of select="Extn/@ExtnCustLineAccNo"/></td>
					<td class="right"></td>
					<td class="right"></td>
				</tr>

				<tr>
					<td><span class="itemno"> <xsl:value-of select='$storeFront' />  item #:<xsl:value-of select="Item/@ItemID"/></span> </td>
					<td class="right"> Mfg. Item #:</td>
					<td class="left"><xsl:value-of select="Item/@ManufacturerItem"/></td>
					<td class="right">My Item #:</td>
					<td class="left"><xsl:value-of select="Item/@CustomerItem"/></td>

				</tr>
				<!-- if (specialInstructions is not null) -->
				<tr class="special-instructions"> 
					<td colspan="5"> 
						<table class="inner">
							<tr>
								<td class="first" valign="top"> Special Instructions:</td>
								<td class="last"></td>

							</tr>
						</table>
					 </td>
				</tr>
				<!-- /if -->
				
				<!-- if (moreItems = true) -->
										<!-- if (specialInstructions is not null) -->
										<!-- <tr>

					<td> <br/> Fuel Surcharge </td>
					<td></td>
					<td></td>
					<td></td>
					<td class="right"><br/>$7.00</td>
				</tr>
					
 -->				<!-- /if -->
				
				<!-- if (specialCharges is not null) -->
				<!-- <tr>

					For all special charges, a <br/> on each line before the value.
					<td> <br/> Fuel Surcharge </td>
					<td></td>
					<td></td>
					<td></td>
					<td class="right"><br/>$7.00</td>
				</tr>

				if there are multiple special charges, the 'specialcharges' class needs to be added to include the gridlines.
				<tr class="specialcharges">
					For all special charges, a <br/> on each line before the value.
					<td> <br/> Fuel Surcharge 2</td>
					<td></td>
					<td></td>
					<td></td>

					<td class="right"><br/>$7.00</td>
				</tr>
 -->				<!-- /if -->
			</tbody>
			</xsl:for-each>
			</table>
					</td>
					</tr>
					<table class="order-total">
						<tr>

				<td>Subtotal:</td>
				<td> <xsl:value-of select="Order/Extn/@ExtnOrderSubTotal"/></td>
			</tr>
			<tr>
				<td> Order Total Adjustments:</td>
				<td><xsl:value-of select="Order/Extn/@ExtnTotOrderAdjustments"/></td>

			</tr>
			<tr>
				<td>Adjusted Subtotal:</td>
				<td><xsl:value-of select="Order/Extn/@ExtnTotOrdValWithoutTaxes"/></td>
			</tr>
			<tr>
				<td>Tax</td>

				<td><span class="tbd">To be determined</span></td>
			</tr>
			<tr>
				<td> Shipping and Handling</td>
				<td><span class="tbd">To be determined</span></td>
			</tr>

			<tr class="last">
				<td> Order Total (USD):</td>
				<td><xsl:value-of select="Order/Extn/@ExtnTotalOrderValue"/></td>
				</tr>

						</table>
						</table>
			</BODY>
		</HTML>
		
	</xsl:template>
</xsl:stylesheet>