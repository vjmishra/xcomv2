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
				margin-top:-3px;
				margin-bottom:-3px;
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
				padding-right: 2px;
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
			
			.addWidth
			{
				width: 120px;
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
		
		<xsl:variable name="custlinePOLbl" >
			<xsl:value-of select="Order/Extn/@ExtnCustLinePOLbl" />
		</xsl:variable>
		
		<xsl:variable name="viewPricesFlag" >
			<xsl:value-of select="Order/@viewPricesFlag" />
		</xsl:variable>
		
		<xsl:variable name="urlPrefix" select="'https://www.'"/>	
		<xsl:variable name="urlSuffix" select="'.com'"/>
		
		<xsl:variable name="storeFront" >
			<xsl:value-of select="Order/@EnterpriseCode" />
		</xsl:variable>
		
		<xsl:variable name="storeFrontURL">    
			<xsl:value-of select="concat($urlPrefix, $storeFront, $urlSuffix)"/>   
		</xsl:variable> 
		<xsl:variable name="urlSaalFeldSuffix" select="'redistribution.com'"/>
		<xsl:variable name="storeSaalFeldFrontURL">    
			<xsl:value-of select="concat($urlPrefix, $storeFront, $urlSaalFeldSuffix)"/>   
		</xsl:variable> 
		
		<xsl:variable name="devSaalUrl" select="'dev.saalfeldredistribution.com/order'"/>
		<xsl:variable name="devxpedxUrl" select="'http://xpappd01.ipaper.com:8001/swc/home/home.action?sfId=xpedx'"/>
		<xsl:variable name="stgSaalUrl" select="'http://stg.saalfeldredistribution.com/'"/>
		<xsl:variable name="stgxpedxUrl" select="'http://stg.xpedx.com/'"/>
		
		<xsl:variable name="brandLogo" >
			<xsl:value-of select="Order/@BrandLogo" />
		</xsl:variable>	
		
    <xsl:template match="/">
		<HTML>
			<xsl:call-template name="applyStyle"/>
			<BODY topmargin="0" leftmargin="0" STYLE="font:normal 10pt Tahoma" align="left" >
			
			<table  width="80%" border="0" halign="left" cellpadding="0" cellspacing="0" style="margin:5px;">
				<tr>	
					<td >
						<table width="100%" border="0" >
							<tr>
								<td >
								</td>
								<td width="100%">
								<img src="{$brandLogo}" width="210" height="47" alt="xpedx" longdesc="http://www.xpedx.com" />
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
					Your order has been rejected. If you have any questions, please contact your order approver.
					
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
					<tr>
					
					<td colspan="2">
					<xsl:if test = 'Order/Input/OrderHoldType/@ReasonText!="Empty"' >
					<span class="bold">Rejected Comments:&#160;</span><xsl:value-of select="Order/Input/OrderHoldType/@ReasonText"/>
					</xsl:if>
					</td>
					</tr>
					</table>
								
										
			<table width="100%"  cellpadding="4" cellspacing="4" style="border:solid 1px #ccc;border-top:none;">														
				<thead>
					<tr>
						<th colspan="2"> Order Information: </th>
						<th colspan="2"> Bill To Detail: </th>
						<th colspan="2"> Shipping Information: </th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td width="50%" colspan="2" style="vertical-align:top">	
							<table>
								<tr>
									<td class="widthLeft">Web Confirmation: </td>
									<td class="widthRight"><xsl:value-of select="Order/Extn/@ExtnWebConfNum"/> </td>
								</tr>
								<tr>
									<td class="widthLeft">Order #: </td>
									<td class="widthRight">In Progress</td>
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
						<td width="25%" colspan="2" style="vertical-align:top">
					
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
					<td colspan="3"> <span class="bold"> Estimated Delivery Date: </span>
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
				<table class='price' width="100%" >
			<xsl:if test = 'Order/@EnterpriseCode="xpedx"'>
								<thead>
									<th  bgcolor="#003399" > </th>
									<th  bgcolor="#003399"> </th>
									<th  bgcolor="#003399"> </th>
									<th valign="top" class="right"  bgcolor="#003399" color="#fff"><font color="#fff"> My Price (USD) </font></th> <!-- The currency code on this line is dynamic.-->
									<th class="right" bgcolor="#003399"></th> <!-- The currency code on this line is dynamic.-->
									<th    class="right"  bgcolor="#003399" color="#fff"><font color="#fff"> Extended Price (USD)</font></th> <!-- The currency code on this line is dynamic.-->
					                </thead>
						</xsl:if>
						<xsl:if test = 'Order/@EnterpriseCode="Saalfeld"'>
								<thead>
									<th  bgcolor="##084823" > </th>
									<th  bgcolor="##084823"> </th>
									<th  bgcolor="##084823"> </th>
									<th valign="top" class="right"  bgcolor="##084823" color="#fff"><font color="#fff"> My Price (USD) </font></th> <!-- The currency code on this line is dynamic.-->
									<th class="right" bgcolor="##084823"></th> <!-- The currency code on this line is dynamic.-->
									<th    class="right"  bgcolor="##084823" color="#fff"><font color="#fff"> Extended Price (USD)</font></th> <!-- The currency code on this line is dynamic.-->
					                </thead>
				</xsl:if>
			<xsl:for-each select="Order/OrderLines/OrderLine">						  
								<xsl:sort select="Extn/@ExtnLegacyLineNumber"/>
													
			<tbody>
				<tr>
					
					<td rowspan="7" valign="top">
					<xsl:value-of select="Item/@ItemShortDesc" disable-output-escaping="yes"/>
					<xsl:if test = '@LineType!="M"' >					
					<xsl:value-of select="Item/@ItemDesc" disable-output-escaping="yes"/>
					</xsl:if>
					</td>
					
					<xsl:choose>
					<xsl:when test='@LineType!="M"'> 															
					<td width="20%" class="right"> Ordered Qty:&#160;</td>
					<td class="left"><xsl:value-of select='format-number(OrderLineTranQuantity/@OrderedQty,"#")'/>&#160;
					<xsl:choose>
						<xsl:when test = 'OrderLineTranQuantity/@UOMDescription="M_PC"' >
						<xsl:value-of select="substring(OrderLineTranQuantity/@UOMDescription,3,4)"/>
						</xsl:when>
						<xsl:otherwise>
						<xsl:value-of select="OrderLineTranQuantity/@UOMDescription"/>
						</xsl:otherwise>
						</xsl:choose></td>
						<td class="align-right">
							<xsl:if test='$viewPricesFlag ="Y"'>						
								<xsl:choose>
									<xsl:when test='Extn/@ExtnUnitPrice ="0.00"'><span class="tbd">Call for price</span></xsl:when>
									<xsl:when test='Extn/@ExtnUnitPrice =""'><span class="tbd">Call for price</span></xsl:when>
									<xsl:otherwise><xsl:value-of select='format-number(Extn/@ExtnUnitPrice,"$#,###,###,##0.00000")'/></xsl:otherwise>
								</xsl:choose>
							</xsl:if>		
						</td>
						<td class="align-right">
						</td>
						<td class="align-right">
							<xsl:if test='$viewPricesFlag ="Y"'>		
								<xsl:choose>
									<xsl:when test='not(Extn/@ExtnExtendedPrice)'><span class="tbd">To be determined</span></xsl:when>
									<xsl:when test='Extn/@ExtnExtendedPrice =""'><span class="tbd">To be determined</span></xsl:when>
									<xsl:when test='Extn/@ExtnExtendedPrice ="0.00"'><span class="tbd">To be determined</span></xsl:when>
									<xsl:otherwise><xsl:value-of select='format-number(Extn/@ExtnExtendedPrice,"$#,###,###,##0.00#")'/></xsl:otherwise>
								</xsl:choose>
							</xsl:if>	
						</td>
					</xsl:when>
					<xsl:otherwise>
						<td class="right"> </td>
						<td class="left"></td>
						<td class="right"></td>
						<td class="right"></td>
						<td class="align-right">
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
				<tr>

					<!-- <td> This cell is occupied via the rowspan property in the first row. Do not change. </td> --> 
					<xsl:if test = '@LineType!="M"' >
								
						<xsl:if test = '(Extn/@ExtnReqShipOrdQty!="") and  ($IsOrderSplit ="N") ' >	
					
					<td class="right"> Shippable Qty:&#160;</td>
					<td class="addWidth"><xsl:value-of select='format-number(Extn/@ExtnReqShipOrdQty,"#")'/>&#160;
					<xsl:choose>
						<xsl:when test = 'OrderLineTranQuantity/@UOMDescription="M_PC"' >
						<xsl:value-of select="substring(OrderLineTranQuantity/@UOMDescription,3,4)"/>
						</xsl:when>
						<xsl:otherwise>
						<xsl:value-of select="OrderLineTranQuantity/@UOMDescription"/>
						</xsl:otherwise>
						</xsl:choose></td>
						<td class="align-right"></td>
					<td class="align-right">
					<xsl:if test='$viewPricesFlag ="Y"'>
						<xsl:choose>
						<xsl:when test='Extn/@ExtnUnitPrice =""'>
						</xsl:when>
						<xsl:when test='Extn/@ExtnUnitPrice ="0.00"'>
						</xsl:when>
						<xsl:otherwise>per <xsl:value-of select='Extn/@ExtnPricingUOMDescription'/></xsl:otherwise>
						</xsl:choose>
					</xsl:if>
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
					<td class="left"><xsl:value-of select='format-number(Extn/@ExtnReqBackOrdQty,"#")'/>&#160;
					<xsl:choose>
						<xsl:when test = 'OrderLineTranQuantity/@UOMDescription="M_PC"' >
						<xsl:value-of select="substring(OrderLineTranQuantity/@UOMDescription,3,4)"/>
						</xsl:when>
						<xsl:otherwise>
						<xsl:value-of select="OrderLineTranQuantity/@UOMDescription"/>
						</xsl:otherwise>
						</xsl:choose></td>
						<td class="align-right"></td>
					<td class="right"></td>
					<td class="right"></td>
					</xsl:if>
                     </xsl:if>
				</tr>
				<tr>
				<xsl:if test = '@LineType!="M"' >					
						
				<xsl:if test = '@lineOrderNO!=""' >					
					
					<!-- <td> This cell is occupied via the rowspan property in the first row. Do not change. </td> --> 
					<td class="right"><xsl:value-of select="OrderLineTranQuantity/@OrderedQty"/>&#160;
					<xsl:choose>
						<xsl:when test = 'OrderLineTranQuantity/@UOMDescription="M_PC"' >
						<xsl:value-of select="substring(OrderLineTranQuantity/@UOMDescription,3,4)"/>
						</xsl:when>
						<xsl:otherwise>
						<xsl:value-of select="OrderLineTranQuantity/@UOMDescription"/>
						</xsl:otherwise>
						</xsl:choose></td>
						<td class="align-right"></td>
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
					<td class="addWidth"></td>
					<td class="right"></td>
					<td class="right"></td>
					</xsl:if>
					</xsl:if>
				</tr>
				
				<tr>
					<!-- <td> This cell is occupied via the rowspan property in the first row. Do not change. </td> --> 
					<xsl:if test = '@LineType!="M"' >					
					<xsl:if test = '@CustomerPONo!=""' >
					<xsl:choose>
					<xsl:when test="$custlinePOLbl!=''">
					<td class="right"><xsl:value-of select='$custlinePOLbl'/>:&#160;</td>
					</xsl:when>
					<xsl:otherwise><td class="right">Line PO#:&#160;</td></xsl:otherwise>
					</xsl:choose>
					<td class="addWidth"><xsl:value-of select="@CustomerPONo"/></td>
					<td class="right"></td>
					<td class="right"></td>
					</xsl:if>
					</xsl:if>

				</tr>
				<tr>
				<xsl:if test = '@LineType!="M"' >					
						
				<xsl:if test = 'Extn/@ExtnCustLineAccNo!=""' >					
					
					<!-- <td> This cell is occupied via the rowspan property in the first row. Do not change. </td> --> 
					<xsl:choose>
					<xsl:when test="$custlineAcctLbl!=''">
					<td class="right" valign="top"><xsl:value-of select='$custlineAcctLbl'/>:&#160;</td>
					</xsl:when>
					<xsl:otherwise><td class="right" valign="top">Line Account#:&#160;</td></xsl:otherwise>
					</xsl:choose>
					<td class="addWidth" valign="top"><xsl:value-of select="Extn/@ExtnCustLineAccNo"/></td>
					<td class="right"></td>
					<td class="right"></td>
					</xsl:if>
					</xsl:if>
				</tr>

				<tr>
				<xsl:if test = 'Item/@ItemID!=""' >					
				
					<td><span class="itemno"><xsl:value-of select='$storeFront' />  item #: <xsl:value-of select="Item/@ItemID"/></span> </td>
					</xsl:if>
					<xsl:if test = '@LineType!="M"' >					
						
					<xsl:if test = 'Item/@ManufacturerItem!=""' >					
				
					<td class="right"> Mfg. Item #:&#160;</td>
					<td class="left"><xsl:value-of select="Item/@ManufacturerItem"/></td>
					</xsl:if>
					<xsl:if test = 'Item/@CustomerItem!=""' >					
				
					<td class="right">My Item #:&#160;</td>
					<td class="addWidth"><xsl:value-of select="Item/@CustomerItem"/></td>
                    </xsl:if>
                    </xsl:if>
				</tr>
				<tr>
				<td colspan="6" style="border-bottom:1px solid #ccc;" >
				</td>
				</tr>
				
				<!-- if (specialInstructions is not null) -->
				<xsl:if test = 'Instructions/Instruction/@InstructionText!=""'>
				<tr class="special-instructions"> 
					<td colspan="6"> 
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
					<xsl:if test='$viewPricesFlag ="Y"'>
					<table class="order-total" align="right">
						<tr>

				<td>Subtotal:</td>
				<td>
				<td class="align-right"><xsl:choose>
						<xsl:when test='not(Order/Extn/@ExtnOrderSubTotal)'>$0.00</xsl:when>
						<xsl:when test='Order/Extn/@ExtnOrderSubTotal =""'>$0.00</xsl:when>
						<xsl:when test='Order/Extn/@ExtnOrderSubTotal ="0.00"'>$0.00</xsl:when>
						<xsl:otherwise><xsl:value-of select='format-number(Order/Extn/@ExtnOrderSubTotal,"$#,###,###,##0.00")'></xsl:value-of>
						</xsl:otherwise>
					</xsl:choose></td>
				</td>
			</tr>
			<tr>
				<td> Order Total Adjustments:</td>
				<td>
				<td class="align-right"><xsl:choose>
						<xsl:when test='not(Order/Extn/@ExtnTotOrderAdjustments)'>$0.00</xsl:when>
						<xsl:when test='Order/Extn/@ExtnTotOrderAdjustments =""'>$0.00</xsl:when>
						<xsl:when test='Order/Extn/@ExtnTotOrderAdjustments ="0.00"'>$0.00</xsl:when>
						<xsl:otherwise><xsl:value-of select='format-number(Order/Extn/@ExtnTotOrderAdjustments,"$#,###,###,##0.00#")'></xsl:value-of>
						</xsl:otherwise>
					</xsl:choose></td>
				</td>
			</tr>
			<tr>
			<td>Adjusted Subtotal:</td>
				 <td>
				 <td class="align-right"><xsl:choose>
						<xsl:when test='not(Order/Extn/@ExtnTotOrdValWithoutTaxes)'>$0.00</xsl:when>
						<xsl:when test='Order/Extn/@ExtnTotOrdValWithoutTaxes =""'>$0.00</xsl:when>
						<xsl:when test='Order/Extn/@ExtnTotOrdValWithoutTaxes ="0.00"'>$0.00</xsl:when>
						<xsl:otherwise><xsl:value-of select='format-number(Order/Extn/@ExtnTotOrdValWithoutTaxes,"$#,###,###,##0.00#")'></xsl:value-of>
						</xsl:otherwise>
					</xsl:choose></td>
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
				 <td class="align-right"><xsl:choose>
						<xsl:when test='not(Order/Extn/@ExtnTotalOrderValue)'>$0.00</xsl:when>
						<xsl:when test='Order/Extn/@ExtnTotalOrderValue =""'>$0.00</xsl:when>
						<xsl:when test='Order/Extn/@ExtnTotalOrderValue ="0.00"'>$0.00</xsl:when>
						<xsl:otherwise><xsl:value-of select='format-number(Order/Extn/@ExtnTotalOrderValue,"$#,###,###,##0.00")'></xsl:value-of>
						</xsl:otherwise>
					</xsl:choose></td>
				 </td>
			
				 </tr>
                
						</table>
						<tr>
					<td>
					&#160;
					</td>
					</tr>
						</xsl:if>
						</td></tr>
					<tr align="right"><td>
					<table width="100%" border="0" align="" cellpadding="1" cellspacing="1" style="margin-left:5px;border-collapse:inherit;text-align:left;" >
					<tr>
					<td style="text-wrap:7.5in;width:720px;">
					This document merely confirms your order, it is not an acceptance of your order. Additional fees may apply to accepted orders. 
					</td>
				</tr>
			</table></td></tr>
			
	</table>
						
			</BODY>
		</HTML>
		
	</xsl:template>
</xsl:stylesheet>