<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:emailformatters="com.yantra.pca.email.formatters"
    xmlns:java="java"
    exclude-result-prefixes="java emailformatters">
		<xsl:template match="NewUser">
		<html>
		<xsl:template match="/">
			<xsl:comment>RECIPIENTS=</xsl:comment>
			<xsl:comment>FROM=tester@stercomm.com</xsl:comment>
			<xsl:comment>SUBJECT=xpedx.com-Registration Request</xsl:comment>
			<xsl:comment>CONTENT_TYPE=text/html</xsl:comment>
		</xsl:template>
		
			<body>
				<table width="600" border="0" align="center" cellpadding="2" cellspacing="2">
					<tr>
						<td><img src="xpedx_r_rgb_lo.jpg" width="216" height="69" alt="xpedx" longdesc="http://www.xpedx.com" /></td>
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
													<span style="font-family:Arial, Geneva, sans-serif; font-size:20px; font-weight:normal;">		Registration Request 
													</span>
												</td>
											</tr>
											<tr>
												<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"> &#160; </td>
											</tr>                                                                                   
                                            <tr>
											<!-- <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
												Thank you for registering online with <xsl:value-of select="./@OrganizationKey"/>.                
												The information below has been sent to your local.customer service department for processing .A representative will contact you with your login information.
												<span ></span>
											</td> -->
											<!-- <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
												Thank you for registering online with <xsl:value-of select="//@BrandName" />.com.                
												The information below has been sent to your local.customer service department for processing .A representative will contact you with your login information.
												<span ></span>
											</td>
											 -->
											 <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
												Thank you for registering online with xpedx.com.                
												The information below has been sent to your local customer service department for processing .A representative will contact you with your login information.
												<span ></span>
											</td>
										</tr>
										<tr>
												<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"> &#160; </td>
											</tr>                                                                                   
                                            
										
											<tr>
												<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
													<table width="100%" border="0">
														<tr>
															<td width="19%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
																User's Name:
															</td>
															<td width="27%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																<xsl:value-of select="@Name"/>
															</td>
															</tr>
															<tr>
															<td width="22%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																Company Name:
															</td>
															<td width="32%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																<xsl:value-of select="@CompanyName"/><BR/>
															</td>
														   </tr>
															<tr>
															<td width="19%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
																Email Address:
															</td>
															<td width="27%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																<xsl:value-of select="@EmailID"/>
															</td></tr>
															<tr>
															<td width="22%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																Phone:
															</td>
															<td width="32%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																<xsl:value-of select="@Phone"/><BR/>
															</td></tr>
															
														</table>
												</td>
											</tr>
											<tr>
												<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"> &#160; </td>
											</tr>                                                                                   
                                            
											<tr>
												<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
													<div align="left">                
														Thank You!
														</div>                
													
											</td>
											</tr>
											
											<tr>
												<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
													<div align="left">                
														Please do not reply  to this email. This mailbox is not monitored and you will not receive a response.
													</div>                
													<p></p>
												</td>
											</tr>
											<tr>
												<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">   </td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<p>-</p>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>