<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:emailformatters="com.yantra.pca.email.formatters"
    xmlns:java="java"
    exclude-result-prefixes="java emailformatters">
	
	<xsl:template match="Order">
	
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
						<img src="xpedx_r_rgb_lo.jpg" width="216" height="69" alt="xpedx" longdesc="http://www.xpedx.com" />
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
												This is an automated notification to confirm that the following shipping address has been added to your <xsl:value-of select="./@SellerOrganizationCode"/>.com account.
												<span ></span>
											</td>
										</tr>
										<tr>
											<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">&#160;</td>
										</tr>

										<tr>
											<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
												<table width="100%" border="0" cellpadding="0" cellspacing="0">
													<tr>
														<td width="5" bgcolor="818181"><img align="left" src="table-left.jpg" alt="xpedx" title="xpedx" width="5" height="28" /></td>
														<td width="99" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181; padding:0px 2px; ">Customer Name:</td>
														<td width="76" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  padding:0px 2px;  background-color:#818181;">Account #:</td>
														<td width="53" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff; background-color:#818181; padding:0px 2px; ">Address </td>
														<td width="7" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;"><img src="table-right.jpg" width="5" height="28" alt="xpedx" align="right" title="xpedx" /></td>
													</tr>
						  
													<!-- add for each order line here -->						  
					  
														<tr>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px;border-bottom:solid 1px #818181; color:#000;border-left:solid 1px #818181; color:#000;">&#160;</td>
															
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;padding:10px 2px; ">
																<xsl:value-of select="PersonInfoShipTo/@FirstName"/>
																<xsl:text> </xsl:text>
																<xsl:value-of select="PersonInfoShipTo/@LastName"/>
															</td>
															
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; color:#000;border-bottom:solid 1px #818181; color:#000;padding:10px 2px; "><xsl:value-of select="PersonInfoShipTo/@LastName"/></td>

															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; ">
																<xsl:value-of select="PersonInfoShipTo/@AddressLine1"/>, <br></br>
																<xsl:value-of select="PersonInfoShipTo/@AddressLine2"/>, <br></br>
																<xsl:value-of select="PersonInfoShipTo/@City"/>, 
																<xsl:value-of select="PersonInfoShipTo/@State"/><br></br>					
																<xsl:value-of select="PersonInfoShipTo/Extn/@ExtnZip4"/>
															</td>
													
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;  border-right:solid 1px #818181; color:#000;">&#160;</td>
														</tr>

												</table>
												<div align="center"></div>
											</td>
										</tr>
										<tr>
											<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">&#160;</td>
										</tr>

									    <tr>
											<td style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000; text-align: left;">Thank you for your business!<br /><br /></td>
									    </tr>
									    <tr>
											<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
												<div align="center"> Please do not reply  to this email. This mailbox is not monitored and you will not receive a response </div>                
											</td>
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