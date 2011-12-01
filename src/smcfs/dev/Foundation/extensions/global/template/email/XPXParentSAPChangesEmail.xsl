<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:emailformatters="com.yantra.pca.email.formatters"
    xmlns:java="java"
    exclude-result-prefixes="java emailformatters">
	
	<xsl:template match="Customer">
	
		<xsl:variable name="brandLogo" select="./@BrandLogo"></xsl:variable>
		<html>
		<xsl:comment>CONTENT_TYPE=text/html</xsl:comment>
	
			<body>
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
												<td width="600" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"></td>
											</tr>
											<tr>
												<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
													This is an automated email to notify you that there has been a change in the Parent SAP # in your <xsl:value-of select="./@SellerOrganizationCode"/>.com account. Please review the details below.
													<span ></span>
												</td>
											</tr>
											<tr>
												<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">&#160;</td>
											</tr>
										</table>
										<table width="75%" border="0">	
											<tr>
												<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
													<table width="100%" border="0" cellpadding="0" cellspacing="0">
														<tr>															
															<td width="25%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="./@CustomerRecordType"/> Customer #: </td>
															<td width="30%"  style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;padding:10px 2px; ">																																	<xsl:value-of select="./@CustomerID"/>
															</td>
														</tr>																				  
														<tr>
															<td width="25%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Old Parent SAP #: </td>
															<td width="30%"  style="font-family: Arial, Geneva, sans-serif;font-size:12px; padding:10px 2px; "><xsl:value-of select="./@OldSAPParentAccountNumber"/></td>
														</tr>
														<tr>
															<td width="25%"  style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">New Parent SAP #: </td>															
															<td width="30%"  style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000; color:#000;padding:10px 2px; "><xsl:value-of select="./@NewSAPParentAccountNumber"/></td>
														</tr>
														<tr>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" height="10"></td>
														</tr>
														<tr>
															<td width="25%" valign="top" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >Username: </td>
															<td width="30%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">																																
																<select size="5" style="width:100%;" multiple="multiple">
																<xsl:for-each select="Users/User">
																	<option>
																		<xsl:value-of select="./@UserId"/>
																	</option>
																	</xsl:for-each>
																</select> 																																										
															</td>
														</tr>	
													</table>
												</td>
											  </tr>
											</table>
											<table width="" border="0">
												<tr>
													<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">&#160;</td>
												</tr>										    
											    <tr>
													<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
														<div align="left"> Please do not reply to this email. This mailbox is not monitored and you will not receive a response.</div>                
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
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>	