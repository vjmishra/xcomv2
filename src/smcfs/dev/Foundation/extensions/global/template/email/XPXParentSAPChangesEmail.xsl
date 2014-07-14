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
															<tr>
															<td width="30%" valign="top" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;padding-left:3px" >Username: </td>
 															<xsl:for-each select="./Users/User/@UserId" >
															<tr><td></td><td valign="top" width="100%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000; color:#000;padding:10px 2px;">
																<xsl:value-of select="."/>																															
																	<xsl:choose>
																	 <xsl:when test="count(./Users/User) &gt; 0"> 
																		<xsl:text></xsl:text>
																	</xsl:when>
																	<xsl:otherwise>continue</xsl:otherwise>
																</xsl:choose>
																</td></tr>
																</xsl:for-each>
																<td width="30%" valign="top" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;padding-left:3px">
																<xsl:choose>
																<xsl:when test="count(./Users/User) = 0">
																<xsl:text>No User Assigned</xsl:text>
																</xsl:when>
															</xsl:choose>
																</td>
														</tr>
														<tr>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" height="10"></td>
														</tr>
													   <tr>
															<td width="30%" valign="top" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;padding-left:3px" >Sales Rep: </td>
 															<xsl:for-each select="./SalesReps/SalesRep/@SalesId" >
 															<tr><td></td><td valign="top" width="100%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000; color:#000;padding:10px 2px;">
																<xsl:value-of select="."/>																															
																	<xsl:choose>
																	 <xsl:when test="count(./SalesReps/SalesRep) &gt; 0"> 
																		<xsl:text></xsl:text>
																	</xsl:when>
																	</xsl:choose>
																</td></tr>
																</xsl:for-each>
																<td width="30%" valign="top" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;padding-left:3px">
																<xsl:choose>
																<xsl:when test="count(./SalesReps/SalesRep) = 0">
																<xsl:text>No SalesRep Assigned</xsl:text>
																</xsl:when>
															</xsl:choose>
																</td>
														</tr>
														<br></br>	
														<tr>
															<td width="27%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;padding-left:3px" nowrap="nowrap">Old SAP #: </td>
															<td width="30%"  style="font-family: Arial, Geneva, sans-serif;font-size:12px; padding:10px 2px; "><xsl:value-of select="./@OldSAPAccountNumber"/></td>
														</tr>
														<tr>
															<td width="27%"  style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;padding-left:3px" nowrap="nowrap">New SAP #: </td>															
															<td width="30%"  style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000; color:#000;padding:10px 2px; "><xsl:value-of select="./@NewSAPAccountNumber"/></td>
														</tr>
														<tr>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" height="10"></td>
														</tr>
															<tr>
															<td width="30%" valign="top" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;padding-left:3px" >Username: </td>
 															<xsl:for-each select="./UsersSAP/UserSAP/@UserId" >
															<tr><td></td><td valign="top" width="100%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000; color:#000;padding:10px 2px;">
																<xsl:value-of select="."/>																															
																	<xsl:choose>
																	 <xsl:when test="count(./UsersSAP/UserSAP) &gt; 0"> 
																		<xsl:text></xsl:text>
																	</xsl:when>
																	<xsl:otherwise>continue</xsl:otherwise>
																</xsl:choose>
																</td></tr>
																</xsl:for-each>
																<td width="30%" valign="top" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;padding-left:3px">
																<xsl:choose>
																<xsl:when test="count(./UsersSAP/UserSAP) = 0">
																<xsl:text>No User Assigned</xsl:text>
																</xsl:when>
															</xsl:choose>
																</td>
														</tr>
														<tr>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" height="10"></td>
														</tr>
													   <tr>
															<td width="30%" valign="top" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;padding-left:3px" >Sales Rep: </td>
 															<xsl:for-each select="./SalesRepsSAP/SalesRepSAP/@SalesId" >
 															<tr><td></td><td valign="top" width="100%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000; color:#000;padding:10px 2px;">
																<xsl:value-of select="."/>																															
																	<xsl:choose>
																	 <xsl:when test="count(./SalesRepsSAP/SalesRepSAP) &gt; 0"> 
																		<xsl:text></xsl:text>
																	</xsl:when>
																	</xsl:choose>
																</td></tr>
																</xsl:for-each>
																<td width="30%" valign="top" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;padding-left:3px">
																<xsl:choose>
																<xsl:when test="count(./SalesRepsSAP/SalesRepSAP) = 0">
																<xsl:text>No SalesRep Assigned</xsl:text>
																</xsl:when>
															</xsl:choose>
																</td>
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