<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:emailformatters="com.yantra.pca.email.formatters"
    xmlns:java="java"
    exclude-result-prefixes="java emailformatters">
    
    <xsl:template match="NewUser">
    
    <xsl:variable name="imageURL">    
		<xsl:value-of select="@ImageUrl"/>
	</xsl:variable> 
    
     <html>		
			<body>
				<table width="600" border="0" align="left" cellpadding="2" cellspacing="2">
					<tr>
						<td><img src="{$imageURL}" width="216" height="69"  /></td>
					</tr>
					<tr>
						<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
							<table width="100%" border="0" cellpadding="0"  style="border:solid 1px #999;  padding:20px 20px 0px 20px;">
								<tr>
									<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
										<table width="100%" border="0">
											<tr>
												<td width="600" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"></td>
											</tr>
											
											<tr>
												<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
													<span style="font-family:Arial, Geneva, sans-serif; font-size:20px; font-weight:normal;">
													<xsl:if test = '@Brand="xpedx"'>
															<xsl:value-of select="@Brand"/>.com Registration Request 
													</xsl:if>
													<xsl:if test = '@Brand="Saalfeld"' >
															<xsl:value-of select="@Brand"/>redistribution.com Registration Request .
													</xsl:if>
													
													</span>
												</td>
											</tr>
											<tr>
												<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">  &#160; </td>
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
												Thank you for registering online with 
												<xsl:if test = '@Brand="xpedx"'>
															<xsl:value-of select="@Brand"/>.com. 
													</xsl:if>
													<xsl:if test = '@Brand="Saalfeld"' >
															<xsl:value-of select="@Brand"/>redistribution.com.
													</xsl:if>
												
												                
												The information below has been sent to your local support contact for processing. A representative will contact you.
												<span ></span>
											</td>
										</tr>
										<tr>
												<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"> &#160; </td>
											</tr>                                                                                   
                                            
										<!--Start for Jira 3269  -->
											<tr>
												<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
													<table width="65%" border="0">
														<tr>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
																Name:
															</td>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																<xsl:value-of select="@FirstName"/><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
																<xsl:value-of select="@LastName"/>
															</td>
															</tr>
															<tr>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																Company:
															</td>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																<xsl:value-of select="@CompanyName"/><BR/>
															</td>
														   </tr>
														   <tr>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
																Address:
															</td>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																<xsl:value-of select="@Address1"/>
															</td>
															</tr>
															<xsl:if test = '@Address2!=""'>
															<tr>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
																
															</td>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																<xsl:value-of select="@Address2"/>
															</td>
															</tr>
															</xsl:if>
															<tr>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
																City:
															</td>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																<xsl:value-of select="@City"/>
															</td>
															</tr>
															<tr>
															<td width="27%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
																State / Province:
															</td>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																<xsl:value-of select="@State"/>
															</td>
															</tr>
															<tr>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
																Postal Code:
															</td>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																<xsl:value-of select="@ZipCode"/>
															</td>
															</tr>
															<tr>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																Phone:	
															</td>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																<xsl:value-of select="@Phone"/><BR/>
															</td></tr>
															<tr>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
																Email Address:
															</td>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#084823;">
																<xsl:value-of select="@EmailID"/>
															</td></tr>
															</table>
															<table>
															<tr>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
																Questions / Comments:&#160;<xsl:value-of select="@Comments"/>
															</td>
															</tr>
															
															<!-- End for Jira 3269  -->
														</table>
												</td>
											</tr>
											<tr>
												<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"> &#160; </td>
											</tr>                                                                                   
                                            
											<tr>
												<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
													<div align="left">                
														Thank you!
														</div>                
													
											</td>
											</tr>
											<tr>
												<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"> &#160; </td>
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