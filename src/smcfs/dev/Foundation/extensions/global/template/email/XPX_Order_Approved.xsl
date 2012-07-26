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
			<xsl:value-of select="Order/@approvedOrderURL"/>   
		</xsl:variable> 
		
		<xsl:variable name="brandLogo" >
			<xsl:value-of select="Order/@BrandLogo" />
		</xsl:variable>	

	<xsl:template match="/">
	<table width="600" border="0" align="center" cellpadding="2" cellspacing="2" topmargin="0" leftmargin="0" STYLE="font:normal 10pt Tahoma">
	<tr>
	<td height="44">
	</td></tr></table>
		<HTML>
			<xsl:call-template name="applyStyle"/>
			<BODY topmargin="0" leftmargin="0" STYLE="font:normal 10pt Tahoma">
			<table width="600" border="0" align="center" cellpadding="2" cellspacing="2">
				<tr>
					<td>
						<img src="{$brandLogo}" width="216" height="69" alt="xpedx" longdesc="http://www.xpedx.com" />
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
													  This email is to inform you that this order has been approved.  Please review all of the information to make sure the 
													  order is correct.  <a href="{$storeFrontURL}" style="color:#000;">Click Here</a>
													  to review this order on <xsl:value-of select="Order/@SellerOrganizationCode"/>.com.		
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
															Order #:
														</td>
														<td width="32%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="Order/@FormattedOrderNo"/>
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
														<!--<xsl:value-of select="Order/@OrderDate"/>
														 OrderDate="2010-11-24T14:10:07-05:00" to date format mm/dd/yyyy-->
															<xsl:value-of select="substring(Order/@OrderDate,6,2)"/>/<xsl:value-of select="substring(Order/@OrderDate,9,2)"/>/<xsl:value-of select="substring(Order/@OrderDate,1,4)"/>
														</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Address Line 1:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="Order/PersonInfoShipTo/@AddressLine1"/>
														</td>
													</tr>
													<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >Order Status:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="Order/@Status"/>
														</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Address Line 2:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="Order/PersonInfoShipTo/@AddressLine2"/>
														</td>
													</tr>
													<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">PO#:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="Order/@CustomerPONo"/>
														</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">City, State:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="Order/PersonInfoShipTo/@City"/>,
															<xsl:value-of select="Order/PersonInfoShipTo/@State"/>
														</td>
													</tr>
													<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Ordered By:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="Order/Extn/@ExtnOrderedByName"/>
														</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Postal Code:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="Order/PersonInfoShipTo/Extn/@ExtnZip4"/>
														</td>
													</tr>
													<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"></td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
														</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Country:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="Order/PersonInfoShipTo/@Country"/>
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
														<td width="7" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;"></td>
														<td width="99" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181; padding:0px 2px; ">Item Number</td>
														<td width="76" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  padding:0px 2px;  background-color:#818181;">Item Description</td>
														<td width="53" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff; background-color:#818181; padding:0px 2px; ">Ordered Qty</td>
														<td width="66" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff; background-color:#818181;  padding:0px 2px; ">Backordered Qty</td>
														<td width="57" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181; padding:0px 2px; ">Qty UOM </td>
														<td width="62" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;  padding:0px 2px; ">Price / Price UOM</td>
														<td width="79" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;  padding:0px 2px; ">Shippable <br />Qty</td>
														<td width="76" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;  padding:0px 2px; ">Shippable Total</td>
														<td width="7" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;"></td>
													</tr>
												
												<!-- add for each order line here -->						  
													<xsl:for-each select="Order/OrderLines/OrderLine">						  
														<tr>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px;border-bottom:solid 1px #818181; color:#000;border-left:solid 1px #818181; color:#000;">&#160;</td>
															<xsl:choose><xsl:when test="Item/@ItemID !=''"> 															
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;padding:10px 2px; "><xsl:value-of select="Item/@ItemID"/></td>
															</xsl:when><xsl:otherwise>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;padding:10px 2px; ">&#160;</td>
															</xsl:otherwise></xsl:choose>
															<xsl:choose><xsl:when test="Item/@ItemDesc !=''">															
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; color:#000;border-bottom:solid 1px #818181; color:#000;padding:10px 2px; "><xsl:value-of select="Item/@ItemDesc" disable-output-escaping="yes"/></td>
                                                              </xsl:when>
                                                              <xsl:otherwise>
                                                              <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; color:#000;border-bottom:solid 1px #818181; color:#000;padding:10px 2px; ">&#160;</td>
                                                              </xsl:otherwise>
                                                              </xsl:choose>
                                                              <xsl:choose><xsl:when test="OrderLineTranQuantity/@OrderedQty !=''">                                                             
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; "><xsl:value-of select="OrderLineTranQuantity/@OrderedQty"/></td>
															</xsl:when><xsl:otherwise>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; ">&#160;</td>
      														</xsl:otherwise></xsl:choose>
															<xsl:choose><xsl:when test="Extn/@ExtnReqBackOrdQty !=''">  
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; "><xsl:value-of select="Extn/@ExtnReqBackOrdQty"/></td>
															</xsl:when><xsl:otherwise>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; ">&#160;</td>
															</xsl:otherwise></xsl:choose>
															<xsl:choose><xsl:when test="OrderLineTranQuantity/@TransactionalUOM !=''">  
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; border-bottom:solid 1px #818181; color:#000;color:#000;padding:10px 2px; "><xsl:value-of select="OrderLineTranQuantity/@TransactionalUOM"/></td>
															</xsl:when><xsl:otherwise>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; border-bottom:solid 1px #818181; color:#000;color:#000;padding:10px 2px; ">&#160;</td>
															</xsl:otherwise></xsl:choose>
															<xsl:choose><xsl:when test="Extn/@ExtnPricingUOM !=''"> 
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; "><xsl:value-of select="Extn/@ExtnUnitPrice"/>/<xsl:value-of select="Extn/@ExtnPricingUOM"/></td>
														    </xsl:when><xsl:otherwise>
														    <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; ">&#160;</td>
														    </xsl:otherwise></xsl:choose>
														    <xsl:choose><xsl:when test="Extn/@ExtnReqShipOrdQty !=''"> 
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; border-bottom:solid 1px #818181; color:#000;color:#000;padding:10px 2px; "><xsl:value-of select="Extn/@ExtnReqShipOrdQty"/></td>
                                                             </xsl:when><xsl:otherwise>
                                                             <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; border-bottom:solid 1px #818181; color:#000;color:#000;padding:10px 2px; ">&#160;</td>
                                                             </xsl:otherwise></xsl:choose>
                                                             <xsl:choose><xsl:when test="Extn/@ExtnLineShippableTotal !=''"> 
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; border-bottom:solid 1px #818181; color:#000;color:#000;padding:10px 2px; "><xsl:value-of select="Extn/@ExtnLineShippableTotal"/></td>
															</xsl:when><xsl:otherwise>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; border-bottom:solid 1px #818181; color:#000;color:#000;padding:10px 2px; ">&#160;</td>
															</xsl:otherwise></xsl:choose>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;  border-right:solid 1px #818181; color:#000;">&#160;</td>
														</tr>
														<tr>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;border-left:solid 1px #818181; color:#000;">&#160;</td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;padding:10px 2px; ">Line Comments:</td>
															<xsl:choose>
    															<xsl:when test="Instructions/Instruction[@InstructionType='LINE']/@InstructionText !=''">
															<td colspan="7"  style="font-family: Arial, Geneva, sans-serif;font-size:12px;border-bottom:solid 1px #818181; color:#000; border-left:solid 1px #818181; color:#000;; color:#000;padding:10px 2px; "><xsl:value-of select="Instructions/Instruction[@InstructionType='LINE']/@InstructionText" /></td>                  
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;  border-right:solid 1px #818181; color:#000;">&#160;</td>
															</xsl:when>
															<xsl:otherwise>
															<td colspan="7"  style="font-family: Arial, Geneva, sans-serif;font-size:12px;border-bottom:solid 1px #818181; color:#000; border-left:solid 1px #818181; color:#000;; color:#000;padding:10px 2px; ">&#160;</td>                  
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;  border-right:solid 1px #818181; color:#000;">&#160;</td>
															</xsl:otherwise>
															</xsl:choose>
														</tr>  
												</xsl:for-each>
												
												</table>
												<div align="center"></div>
											</td>
										</tr>
										<tr>
											<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
												<div align="left">            This document merely confirms your order; it is not an acceptance of your order. Additional fees may apply to accepted orders.  </div>                </td>
									    </tr>
									    <tr>
											<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
												<div align="left">            Please do not reply  to this email. This mailbox is not monitored and you will not receive a response. </div>                </td>
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
			</BODY>
		</HTML>
		
	</xsl:template>
</xsl:stylesheet>