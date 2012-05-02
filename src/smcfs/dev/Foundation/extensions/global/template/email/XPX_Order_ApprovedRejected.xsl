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
				background-color: #003399;
				color: white;
				padding: 10px;
				font-size: 11px;
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
				width:25%;
				border-radius: 8px;
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
			<BODY topmargin="0" leftmargin="0" STYLE="font:normal 10pt Tahoma">
			
			<table width="90%" border="0" align="center" cellpadding="1" cellspacing="1">
				<tr>	
					<td >
						<table width="100%" border="0" >
							<tr>
								<td >
								</td>
								<td width="75%">
								<img src="{$brandLogo}" width="216" height="69" alt="xpedx" longdesc="http://www.xpedx.com" />
								</td>
							</tr>
						</table> 
					    
					</td>
				</tr>
				
				<tr align="right">
					<td style="font-family: Arial, Geneva, sans-serif;font-size:0px; color:#000;" >
					<table width="100%" border="0" cellpadding="0"  style="border:solid 1px #999;  padding:20px 0px 20px 20px;">
					<tr>
					
					
					<td>
					Your order has been rejected.If you have any questions, please contact your order approver.Click here to review on <xsl:value-of select="Order/@SellerOrganizationCode"/>.com. 
					
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
						 <td rowspan="5">
						 <xsl:if test="(contains($shipToID,$maxDelim))">					
						
								<xsl:value-of select="substring-before($shipToID, $maxDelim)" />
								</xsl:if>
								<xsl:if test="$shipToName!=''">
								<BR/>
								<xsl:value-of select="$shipToName" />
								</xsl:if>
								<xsl:if test='Order/Extn/@ExtnAttentionName!=""'>
								<BR/>
								<xsl:value-of select="Order/Extn/@ExtnAttentionName"/>
								</xsl:if>
								<xsl:if test = 'Order/PersonInfoShipTo/@AddressLine1!=""' >
								<BR/>
								<xsl:value-of select="Order/PersonInfoShipTo/@AddressLine1"/>
								<xsl:if test = 'Order/PersonInfoShipTo/@AddressLine2!=""' >
								,
								</xsl:if>
						       </xsl:if>
						       <xsl:if test = 'Order/PersonInfoShipTo/@AddressLine2!=""' >
						       <BR/>
								<xsl:value-of select="Order/PersonInfoShipTo/@AddressLine2"/>
								<xsl:if test = 'Order/PersonInfoShipTo/@AddressLine3!=""' >
								,
								</xsl:if>
						       </xsl:if>
						      <xsl:if test = 'Order/PersonInfoShipTo/@AddressLine3!=""' >
						      <BR/>
								<xsl:value-of select="Order/PersonInfoShipTo/@AddressLine3"/>
								</xsl:if>
								<xsl:if test = 'Order/PersonInfoShipTo/@City!="" or Order/PersonInfoShipTo/@State or Order/PersonInfoShipTo/@ZipCode or Order/PersonInfoShipTo/@Country' >
					       <BR/>
						    <xsl:value-of select="Order/PersonInfoShipTo/@City"/>, <xsl:value-of select="Order/PersonInfoShipTo/@State"/>,<xsl:value-of select="translate(Order/PersonInfoShipTo/@ZipCode, '&#x20;&#x9;&#xD;&#xA;','')"/>,<xsl:value-of select="Order/PersonInfoShipTo/@Country"/> 
						    
						</xsl:if>
								
											 </td>
					</tr>
					<tr>
						<td> Order #: </td>

						<td> In Progress </td>
						
						<!-- /if                          
							 else if (address line 3 is not null)                          
								<td> &lt;address line 3&gt; </td>
						     /else                          -->
					</tr>					
					<tr>
						<td> Order Status: </td>

						<td> <xsl:value-of select="Order/@Status"/> </td>
						
					</tr>
					<tr>

						<td> PO #: </td>
						<td> <xsl:value-of select="Order/@CustomerPONo"/> </td>
						
					</tr>
					<tr>
						<td> Ordered By: </td>
						 <td><xsl:value-of select="Order/@Createuserid"/></td>
						 
					</tr>	
					<tr> <!-- This row is intentionally left blank for spacing. Do not alter this row. -->
						<td></td>
						<td></td>
						<td> </td>

					</tr><!-- End intentionally blank row. -->
					<tr>
						<td colspan="3"> <span class="bold"> Shipping Options: </span>
						<xsl:if test = 'Order/Extn/@ExtnShipComplete!="N"' >	
						Ship Order Complete
						<xsl:if test = 'Order/Extn/@ExtnWillCall!="N" or Order/Extn/@ExtnRushOrderFlag!="N"' >	
					    ,
					    </xsl:if>
						</xsl:if>						
						<xsl:if test = 'Order/Extn/@ExtnWillCall!="N"' >	
						
					    Will Call
					    <xsl:if test = 'Order/Extn/@ExtnRushOrderFlag!="N" or Order/Extn/@ExtnWillCall!="N"' >					
						,
						</xsl:if>					    					
						</xsl:if>
						<xsl:if test = 'Order/Extn/@ExtnRushOrderFlag!="N"' >					
						Rush Order
						</xsl:if>
						<xsl:if test = 'Order/Extn/@ExtnWebHoldFlag!="N"' >		
								<xsl:if test = 'Order/Extn/@ExtnShipComplete!="N" or Order/Extn/@ExtnWillCall!="N" or Order/Extn/@ExtnRushOrderFlag!="N"' >	
								,							
						  </xsl:if>
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
				<table class="price" >
			<thead>
				<th> </th>

				<th> </th>
				<th> </th>
				<th class="right"> My Price (USD) </th> <!-- The currency code on this line is dynamic.-->
				<th class="right"> Extended Price (USD) </th> <!-- The currency code on this line is dynamic.-->

			</thead>
			<xsl:for-each select="Order/OrderLines/OrderLine">						  
													
			<tbody>
				<tr>
					<td rowspan="7" valign="top">
						
					<xsl:value-of select="Item/@ItemShortDesc" disable-output-escaping="yes"/>
						<br></br>
						<xsl:if test = '@LineType!="M"' >					
						
					<xsl:value-of select="Item/@ItemDesc" disable-output-escaping="yes"/>
					</xsl:if>
											</td>
						<xsl:choose><xsl:when test='@LineType!="M"'> 															
							
					<td class="right"> Ordered Qty:</td>
					<td class="left"><xsl:value-of select="OrderLineTranQuantity/@OrderedQty"/>&#160;<xsl:value-of select="OrderLineTranQuantity/@UOMDescription"/></td>
					<td class="right">$<xsl:value-of select="Extn/@ExtnUnitPrice"/>/<xsl:value-of select="Extn/@ExtnPricingUOMDescription"/> 
					 </td>
					<td class="right">$<xsl:value-of select="Extn/@ExtnExtendedPrice"/></td>
					</xsl:when>
					<xsl:otherwise>
					<td class="right"> </td>
					<td class="left"></td>
					<td class="right">
					 </td>
					<td class="right">$<xsl:value-of select="Extn/@ExtnExtendedPrice"/></td>
					
					</xsl:otherwise>
					</xsl:choose>
					
					
					
					
				</tr>
				<tr>

					<!-- <td> This cell is occupied via the rowspan property in the first row. Do not change. </td> --> 
					<xsl:if test = '@LineType!="M"' >
								
						<xsl:if test = 'Extn/@ExtnReqShipOrdQty!=""' >	
					
					<td class="right"> Shippable Qty:</td>
					<td class="left"><xsl:value-of select="Extn/@ExtnReqShipOrdQty"/>&#160;<xsl:value-of select="OrderLineTranQuantity/@UOMDescription"/></td> 
					<td class="right"></td>
					<td class="right"></td>
					    </xsl:if>
					</xsl:if>
				</tr>

				<tr>
					<!-- <td> This cell is occupied via the rowspan property in the first row. Do not change. </td> --> 
					<xsl:if test = '@LineType!="M"' >					
						<xsl:if test = 'Extn/@ExtnReqBackOrdQty!=""' >	
					
					<td class="right"> Backorder Qty:</td>
					<td class="left"><xsl:value-of select="Extn/@ExtnReqBackOrdQty"/>&#160;<xsl:value-of select="OrderLineTranQuantity/@UOMDescription"/></td>
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
						
					<xsl:if test = 'Extn/@CustomerLinePONo!=""' >					
					
					<td class="right"> Line PO#:</td>
					<td class="left"><xsl:value-of select="Extn/@CustomerLinePONo"/></td>
					<td class="right"></td>
					<td class="right"></td>
					</xsl:if>
					</xsl:if>

				</tr>
				<tr>
				<xsl:if test = '@LineType!="M"' >					
						
				<xsl:if test = 'Extn/@ExtnCustLineAccNo!=""' >					
					
					<!-- <td> This cell is occupied via the rowspan property in the first row. Do not change. </td> --> 
					<td class="right" valign="top"> Cust Acct Line #:</td>
					<td class="left"><xsl:value-of select="Extn/@ExtnCustLineAccNo"/></td>
					<td class="right"></td>
					<td class="right"></td>
					</xsl:if>
					</xsl:if>
				</tr>

				<tr>
				<xsl:if test = 'Item/@ItemID!=""' >					
				
					<td><span class="itemno">xpedx item #:<xsl:value-of select="Item/@ItemID"/></span> </td>
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
				<!-- if (specialInstructions is not null) -->
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
							</tbody>
			</xsl:for-each>
			</table>
					</td>
					</tr>
					<tr>
					<td >
					<table class="order-total" align="right">
						<tr>

				<td>Subtotal:</td>
				<td>$<xsl:value-of select="Order/Extn/@ExtnOrderSubTotal"/></td>
			</tr>
			<tr>
				<td> Order Total Adjustments:</td>
				<td>$<xsl:value-of select="Order/Extn/@ExtnTotOrderAdjustments"/></td>

			</tr>
			<tr>
				<td>Adjusted Subtotal:</td>
				 <td>$<xsl:value-of select='format-number(Order/Extn/@ExtnTotOrdValWithoutTaxes,"#.00")'/></td>
			 </tr>
			<tr>
				<td>Tax</td>
              <xsl:choose><xsl:when test="Order/Extn/@ExtnOrderTax !='0.00'"> 															
				
				<td><span class="tbd">$<xsl:value-of select="Order/Extn/@ExtnOrderTax"/></span></td>
				</xsl:when><xsl:otherwise>
				<td><span class="tbd">To be determined</span></td>
				</xsl:otherwise></xsl:choose>
				</tr>
			<tr>
				<td> Shipping and Handling</td>
				<xsl:choose>$<xsl:when test="Order/Extn/@ExtnTotalOrderFreight !='0.00'"> 															
															
				<td><span class="tbd"><xsl:value-of select="Order/Extn/@ExtnTotalOrderFreight"/></span></td>
				</xsl:when><xsl:otherwise>
				<td><span class="tbd">To be determined</span></td>
				</xsl:otherwise></xsl:choose>
				</tr>

			<tr class="last">
				<td> Order Total (USD):</td>
				 <td>$<xsl:value-of select='format-number(Order/Extn/@ExtnTotalOrderValue,"#.00")'/></td>
				 </tr>

						</table>
						
						</td></tr>
						<tr align="right"><td>
						<table width="70%" border="0" align="" cellpadding="1" cellspacing="1">
				<tr>
					<td>
					This document merely confirms your order, it is not an acceptance of your order.Additional Fees may apply to accepted orders. 
					Please do not reply to this email.<br>This mailbox is not monitored and you will not receive a response</br>
						</td>
				</tr>
			</table></td></tr>
			
						</table>
						
			</BODY>
		</HTML>
		
	</xsl:template>
</xsl:stylesheet>