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
						<img src="logo-email.jpg" width="210" height="47" alt="xpedx" longdesc="http://www.xpedx.com" />
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
											<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
												The following  <xsl:value-of select="./@SellerOrganizationCode"/>.com order has been submitted for your approval. Please review the details of your order below.                 
												<span ></span>
												<a href="{$storeFrontURL}" style="color:#000;">Click Here</a> to review this order on 
													<xsl:value-of select="./@SellerOrganizationCode"/>.com.		
											</td>
										</tr>
										<tr>
											<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">&#160;</td>
										</tr>
										<tr>
											<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
												<table width="100%" border="0">
													<tr>
														<td colspan="2" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<strong>Order Information:</strong>
														</td>
														<td colspan="2" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<strong>Shipping Information:</strong>
														</td>
													</tr>
													<tr>
														<td width="19%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
															Order Number:
														</td>
														<td width="32%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="./@OrderNo"/>
														</td>
														<td width="17%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															Location:
														</td>
														<td width="32%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:if test="(contains($shipToID,$maxDelim))">
																<xsl:value-of select="substring-before($shipToID, $maxDelim)"/>
																,<span style="padding-left:10px">
																	<xsl:value-of select="$shipToName"/>
																</span>
															</xsl:if>	
															<xsl:if test="(contains($shipToID,$accDelim))">
																<xsl:value-of select="substring-before($shipToID, $accDelim)"/>
																,<span style="padding-left:10px">
																	<xsl:value-of select="$shipToName"/>
																</span>
															</xsl:if>	
														</td>
													</tr>
													<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Order Date:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
														<!--<xsl:value-of select="./@OrderDate"/>
														 OrderDate="2010-11-24T14:10:07-05:00" to date format mm/dd/yyyy-->
															<xsl:value-of select="substring(./@OrderDate,6,2)"/>/<xsl:value-of select="substring(./@OrderDate,9,2)"/>/<xsl:value-of select="substring(./@OrderDate,1,4)"/>
														</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Address1:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="PersonInfoShipTo/@AddressLine1"/>
														</td>
													</tr>
													<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >Order Status:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="./@Status"/>
														</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Address2:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="PersonInfoShipTo/@AddressLine2"/>
														</td>
													</tr>
													<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">PO #:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="./@CustCustPONo"/>
														</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">City, State:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="PersonInfoShipTo/@City"/>,
															<xsl:value-of select="PersonInfoShipTo/@State"/>
														</td>
													</tr>
													<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Placed By:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="./@Createuserid"/>
														</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Zip:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="PersonInfoShipTo/Extn/@ExtnZip4"/>
														</td>
													</tr>
													<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Order Date:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
															<xsl:value-of select="substring(./@OrderDate,6,2)"/>/
															<xsl:value-of select="substring(./@OrderDate,9,2)"/>/
															<xsl:value-of select="substring(./@OrderDate,1,4)"/>									   
														</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Country:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="PersonInfoShipTo/@Country"/>
														</td>
													</tr>
													<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															Order Comments:
														</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
														<xsl:value-of select="Instructions/Instruction[@InstructionType='HEADER']/@InstructionText" />
														</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															Shipping Comments:
														</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															NA
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
														<td width="99" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181; padding:0px 2px; ">Item Number:</td>
														<td width="76" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  padding:0px 2px;  background-color:#818181;">Item Description:</td>
														<td width="53" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff; background-color:#818181; padding:0px 2px; ">QTY Ordered </td>
														<td width="66" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff; background-color:#818181;  padding:0px 2px; ">QTY Backordered</td>
														<td width="57" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181; padding:0px 2px; ">QTY UOM </td>
														<td width="62" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;  padding:0px 2px; ">Price/Price UOM</td>
														<td width="79" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;  padding:0px 2px; ">QTY <br />Shippable</td>
														<td width="76" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;  padding:0px 2px; ">Shippable Total:</td>
														<td width="7" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;"><img src="table-right.jpg" width="5" height="28" alt="xpedx" align="right" title="xpedx" /></td>
													</tr>
						  
													<!-- add for each order line here -->						  
													<xsl:for-each select="OrderLines/OrderLine">						  
														<tr>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px;border-bottom:solid 1px #818181; color:#000;border-left:solid 1px #818181; color:#000;">&#160;</td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;padding:10px 2px; "><xsl:value-of select="Item/@ItemID"/></td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; color:#000;border-bottom:solid 1px #818181; color:#000;padding:10px 2px; "><xsl:value-of select="Item/@ItemDesc"/></td>

															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; "><xsl:value-of select="OrderLineTranQuantity/@OrderedQty"/></td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; "><xsl:value-of select="Extn/@ExtnReqBackOrdQty"/></td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; border-bottom:solid 1px #818181; color:#000;color:#000;padding:10px 2px; "><xsl:value-of select="OrderLineTranQuantity/@TransactionalUOM"/></td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; "><xsl:value-of select="Extn/@ExtnUnitPrice"/>/<xsl:value-of select="Extn/@ExtnPricingUOM"/></td>
														   
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; border-bottom:solid 1px #818181; color:#000;color:#000;padding:10px 2px; "><xsl:value-of select="Extn/@ExtnReqShipOrdQty"/></td>

															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; border-bottom:solid 1px #818181; color:#000;color:#000;padding:10px 2px; "><xsl:value-of select="Extn/@ExtnLineShippableTotal"/></td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;  border-right:solid 1px #818181; color:#000;">&#160;</td>
														</tr>
														<tr>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;border-left:solid 1px #818181; color:#000;">&#160;</td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;padding:10px 2px; ">Line Comments:</td>
															<td colspan="7"  style="font-family: Arial, Geneva, sans-serif;font-size:12px;border-bottom:solid 1px #818181; color:#000; border-left:solid 1px #818181; color:#000;; color:#000;padding:10px 2px; "><xsl:value-of select="Instructions/Instruction[@InstructionType='LINE']/@InstructionText" /></td>

															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;  border-right:solid 1px #818181; color:#000;">&#160;</td>
														</tr>  
													</xsl:for-each>
												</table>
												<div align="center"></div>
											</td>
										</tr>
									    <tr>
											<td style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000; text-align: center;">This document merely confirms your order; it is not an acceptance of your order. Additional fees may apply to accepted orders.<br /><br /></td>
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