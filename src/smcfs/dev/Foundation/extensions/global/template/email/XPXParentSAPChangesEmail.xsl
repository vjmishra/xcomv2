<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:emailformatters="com.yantra.pca.email.formatters"
    xmlns:java="java"
    exclude-result-prefixes="java emailformatters">
	
	<xsl:template match="Customer">
	
		<html>
		<xsl:comment>CONTENT_TYPE=text/html</xsl:comment>
	
			<body>
				<table width="600" border="0" align="left" cellpadding="2" cellspacing="2">
					<tr>
						<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
							<table width="100%" border="0" cellpadding="0"  style="border: solid 1px #999;  padding:20px 20px 0px 20px;">
								<tr>
									<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
										<table width="" border="0">
											<tr>
												<td width="600" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"></td>
											</tr>
											<tr>
												<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;padding-left:3px;">
													This is an automated email notification. Please note that the parent SAP # has been changed in following  <xsl:value-of select="./@SellerOrganizationCode"/>.com account. Please review the details below.
												</td>
											</tr>
											<br></br>									
										  </table>
										  <table width="80%" border="0">	
											<tr>
												<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
													<table width="65%" border="0" cellpadding="0" cellspacing="0">
														<tr>															
															<td width="29%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;padding-left:3px" nowrap="nowrap"><xsl:value-of select="./@CustomerRecordType"/> Customer #: </td>
															<td width="30%"  style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;padding:10px 2px; padding-left:2px">																																	<xsl:value-of select="./@CustomerID"/>
															</td>
														</tr>																				  
														<tr>
															<td width="27%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;padding-left:3px" nowrap="nowrap">Old Parent SAP #: </td>
															<td width="30%"  style="font-family: Arial, Geneva, sans-serif;font-size:12px; padding:10px 2px; "><xsl:value-of select="./@OldSAPParentAccountNumber"/></td>
														</tr>
														<tr>
															<td width="27%"  style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;padding-left:3px" nowrap="nowrap">New Parent SAP #: </td>															
															<td width="30%"  style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000; color:#000;padding:10px 2px; "><xsl:value-of select="./@NewSAPParentAccountNumber"/></td>
														</tr>
														<tr>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" height="10"></td>
														</tr>
														<!-- <tr>
															<td width="27%" valign="top" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;padding-left:3px" >Username: </td>
															<td>																																
																<xsl:choose>
																	<xsl:when test="count(./Users/User) &gt; 0">
																		<select style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000; color:#000;padding:10px 2px;" >
																			<xsl:for-each select="Users/User">
																			<option>
																				<xsl:value-of select="@UserId"/>
																			</option>
																			</xsl:for-each>
																		</select>
																	</xsl:when>
																	<xsl:otherwise>
																		<select style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000; color:#000;padding:10px 2px;" >
																			<option><b>No UserName Available</b></option>																				
																		</select>																		
																	</xsl:otherwise>
																</xsl:choose>																																																									
															</td>
														</tr> -->
														<tr>
															<td width="30%" valign="top" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;padding-left:3px" >Username: </td>
 															<xsl:for-each select="./Users/User/@UserId" >
															<tr><td></td><td width="100%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000; color:#000;padding:10px 2px;">
																<xsl:value-of select="."/>																															
																	<xsl:choose>
																	 <xsl:when test="count(./Users/User) &gt; 0"> 
																		<xsl:text></xsl:text>
																	</xsl:when>
																	</xsl:choose>
																</td></tr>
																</xsl:for-each>
																<tr><td>
																	<xsl:when test="count(./Users/User) = 0"> 
																		<xsl:text>No user</xsl:text>
																	</xsl:when>
														    </td></tr>
														</tr>
														<tr></tr>
														 <tr>
															<td width="30%" valign="top" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;padding-left:3px" >SalesRep: </td>
 															<xsl:for-each select="./SalesRep/Sales/@SalesId" >
															<tr><td></td><td width="100%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000; color:#000;padding:10px 2px;">
																<xsl:value-of select="."/>																															
																	<xsl:choose>
																	 <xsl:when test="count(./SalesRep/Sales) &gt; 0"> 
																		<xsl:text></xsl:text>
																	</xsl:when>
															
																	</xsl:choose>
																</td></tr>
															</xsl:for-each>
															<tr><td>
																  	<xsl:when test="count(./SalesRep/Sales) = 0">
																		<xsl:text>No Sales Rep</xsl:text>
																	</xsl:when>
														    </td></tr>
														</tr>
														<br></br>	
													</table>
												</td>
											  </tr>
											</table>
											<table width="" border="0">
												<tr>
													<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">&#160;</td>
												</tr>										    
											    <tr>
													<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;padding-left:6px;">
														<div>Please do not reply to this email. This mailbox is not monitored and you will not receive a response.</div>                
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