<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:emailformatters="com.yantra.pca.email.formatters"
    xmlns:java="java"
    exclude-result-prefixes="java emailformatters">
	
	<xsl:template match="Order">
	
		<xsl:variable name="shipToID" >
			<xsl:value-of select="./@ShipToID" />
		</xsl:variable>

		<xsl:variable name="maxDelim" select="'-M'"/>
		<xsl:variable name="accDelim" select="'-A'"/>	
			
		<xsl:variable name="shipToName" >
			<xsl:value-of select="./Extn/@ExtnShipToName" />
		</xsl:variable>
	
		<xsl:variable name="urlPrefix" select="'https://www.'"/>	
		<xsl:variable name="urlSuffix" select="'.com'"/>
		
		<xsl:variable name="storeFront" >
			<xsl:value-of select="./@SellerOrganizationCode" />
		</xsl:variable>
		
		<xsl:variable name="storeFrontURL">    
			<xsl:value-of select="concat($urlPrefix, $storeFront, $urlSuffix)"/>   
		</xsl:variable> 
	
	<html>
		<body>
			<table width="600" border="0" align="center" cellpadding="2" cellspacing="2">
				<tr>
					<td>
						<img src="logo-email.jpg" width="210" height="79" alt="xpedx" longdesc="http://www.xpedx.com" />
					</td>
				</tr>
				<tr>
					<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
						<table width="100%" border="0" cellpadding="0"  style="border:solid 1px #999;  padding:20px 20px 0px 20px;">
							<tr>
								<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
									<table width="" border="0">
										<tr>
											<td width="600" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">&#160;</td>
										</tr>
										<tr>
										<h4>Action Required - Customer Return Request:</h4>
											<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
												The following customer has entered a Return Request via  <xsl:value-of select="./@SellerOrganizationCode"/>.com.  Please review and contact the customer with instructions to complete the return.                 
												<span ></span>
											</td>
										</tr>
										<tr>
											<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">&#160;</td>
										</tr>
										<tr>
											<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
												<table width="100%" border="0">

													<tr>
														<td width="19%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
															Customer Name:
														</td>
														<td width="32%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="./@OrderNo"/>
														</td>

													</tr>
													<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Account Number:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
														<!--<xsl:value-of select="./@OrderDate"/>
														 OrderDate="2010-11-24T14:10:07-05:00" to date format mm/dd/yyyy-->
															<xsl:value-of select="substring(./@OrderDate,6,2)"/>/<xsl:value-of select="substring(./@OrderDate,9,2)"/>/<xsl:value-of select="substring(./@OrderDate,1,4)"/>
														</td>
													</tr>
													<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >Users Name:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="./@Status"/>
														</td>
													</tr>
													<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Email Address:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="./@CustCustPONo"/>
														</td>
													</tr>
													<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Phone:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="./@Createuserid"/>
														</td>
													</tr>
													<tr>
														<td width="19%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
															Order Number:
														</td>
														<td width="32%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="./@OrderNo"/>
														</td>

													</tr>
													<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Order Date:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
															<xsl:value-of select="substring(./@OrderDate,6,2)"/>/
															<xsl:value-of select="substring(./@OrderDate,9,2)"/>/
															<xsl:value-of select="substring(./@OrderDate,1,4)"/>									   
														</td>
													</tr>
													<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">&#160;</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">&#160;</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">&#160; </td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">&#160;</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
												<table width="100%" border="0" cellpadding="0" cellspacing="0">
													<tr>
														<td width="5" bgcolor="818181"><img align="left" src="table-left.jpg" alt="xpedx" title="xpedx" width="5" height="28" /></td>
														<td align="center" width="99" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181; padding:0px 2px; ">Item Number:</td>
														<td align="center" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  padding:0px 2px;  background-color:#818181;">Item Description:</td>
														<td width="53" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff; background-color:#818181; padding:0px 2px; ">Quantity</td>
														<td align="center" width="66" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff; background-color:#818181;  padding:0px 2px; ">UOM</td>
														<td width="7" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;"><img src="table-right.jpg" width="5" height="28" alt="xpedx" align="right" title="xpedx" /></td>
													</tr>
						  
													<!-- add for each order line here -->						  
													<xsl:for-each select="OrderLines/OrderLine">						  
														<tr>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px;border-bottom:solid 1px #818181; color:#000;border-left:solid 1px #818181; color:#000;">&#160;</td>
															<td align="center" style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;padding:10px 2px; "><xsl:value-of select="Item/@ItemID"/></td>
															<td  align="center" style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; color:#000;border-bottom:solid 1px #818181; color:#000;padding:10px 2px; "><xsl:value-of select="Item/@ItemDesc"/></td>
															<td align="center" style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; "><xsl:value-of select="OrderLineTranQuantity/@OrderedQty"/></td>
															<td align="center" style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; border-bottom:solid 1px #818181; color:#000;color:#000;padding:10px 2px; "><xsl:value-of select="OrderLineTranQuantity/@TransactionalUOM"/></td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;  border-right:solid 1px #818181; color:#000;">&#160;</td>
														</tr>
													</xsl:for-each>
												</table>
												<tr></tr> <tr></tr> <tr></tr> <tr></tr>
												<table width="100%" border="0" cellpadding="0" cellspacing="0">
													<tr>
														<td width="5" bgcolor="818181"><img align="left" src="table-left.jpg" alt="xpedx" title="xpedx" width="5" height="28" /></td>
														<td align="center" width="99" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181; padding:0px 2px; ">Reason/s for Return</td>
														<td width="7" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;"><img src="table-right.jpg" width="5" height="28" alt="xpedx" align="right" title="xpedx" /></td>
													</tr>
						  
													<!-- add for each order line here -->						  
													<xsl:for-each select="OrderLines/OrderLine">						  
														<tr>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px;border-bottom:solid 1px #818181; color:#000;border-left:solid 1px #818181; color:#000;">&#160;</td>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;padding:10px 2px; "><xsl:value-of select="Item/@ItemID"/></td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;  border-right:solid 1px #818181; color:#000;">&#160;</td>
														</tr>
													</xsl:for-each>
												</table>
												
												<div align="center"></div>
											</td>
										</tr>

									    <tr>
											<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
												<div align="center">            Please do not reply  to this email. This mailbox is not monitored and you will not receive a response </div>                </td>
									    </tr>
									    <tr>
											<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">&#160;</td>
									    </tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<p>&#160;</p>
		</body>
	</html>
	</xsl:template>
</xsl:stylesheet>