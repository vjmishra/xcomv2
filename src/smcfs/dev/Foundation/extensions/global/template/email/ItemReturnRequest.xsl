<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:lxslt="http://xml.apache.org/xslt" version="1.0">
	
	<xsl:template match="/">
		
	<html>
	<xsl:comment>CONTENT_TYPE=text/html</xsl:comment>
		<body>
			<table width="600" border="0" align="center" cellpadding="2" cellspacing="2">
				<tr>
					<td>
						<img src="logo-email.jpg" width="216" height="69" alt="xpedx" longdesc="http://www.xpedx.com" />
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
										<h4>Action Required - Customer Return Request</h4>
											<!-- <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
												The following customer has entered a Return Request via  <xsl:value-of select="./@SellerOrganizationCode"/>.com.  Please review and contact the customer with instructions to complete the return.                 
												<span ></span>
											</td>
 -->										</tr>
										<tr>
											<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">The following customer has entered a Return Request via  <xsl:value-of select="./@SellerOrganizationCode"/>.com.  Please review and contact the customer with instructions to complete the return.                 
												<span ></span>
												</td>
										</tr>
										<tr>
										<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"></td>
										</tr>
										<br></br>
										<tr>
											<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
												<xsl:if test = 'Emails/Email/@ServiceRequestType ="ItemReturnRequest"'>	
			
												<table width="100%" border="0">

													<tr>
														<td width="19%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
															Ship To:
														</td>
														<td width="32%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:for-each select="Emails/Email/ShippingAddress">
															<xsl:value-of select="@address"/>										
										    				</xsl:for-each>
														</td>

													</tr>
													
													<!-- <tr>
														<td width="19%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
															Account Number:
														</td>
														<td width="32%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="Emails/Email/@AccountNumber"/>
														</td>

													</tr>
 -->													<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Web Confirmation#:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
														<!--<xsl:value-of select="./@OrderDate"/>
														 OrderDate="2010-11-24T14:10:07-05:00" to date format mm/dd/yyyy-->
															<xsl:value-of select="Emails/Email/@WCNumber"/></td>
													</tr>
													<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >Order Number#:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="Emails/Email/@OrderNumber"/></td>
													</tr>
													<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Username:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="Emails/Email/@RequestedBy"/>
														</td>
													</tr>
													<!-- <tr>
														<td width="19%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
															Username:
														</td>
														<td width="32%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="Emails/Email/@Username"/>
								
														</td>

													</tr>
													
 -->													
 														<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Email Address:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="Emails/Email/@InputEmail"/>
														</td>
													</tr>
													
 														<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Phone:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="./@Createuserid"/>
														</td>
													</tr>
													<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Comments:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="Emails/Email/@Note"/>
														</td>
													</tr>
													
													<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">&#160;</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">&#160;</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">&#160; </td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">&#160;</td>
													</tr>
												</table>
												</xsl:if>
												<xsl:if test = 'Emails/Email/@ServiceRequestType ="ItemReturnComplaintRequest"'>
			
												<table width="100%" border="0">

													<tr>
														<td width="19%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
															Request Type:
														</td>
														<td width="32%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="Emails/Email/@RequestType"/></td>

													</tr>
													<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Item Number:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
														<!--<xsl:value-of select="./@OrderDate"/>
														 OrderDate="2010-11-24T14:10:07-05:00" to date format mm/dd/yyyy-->
															<xsl:value-of select="Emails/Email/ProductDetail/@ItemNumber"/></td>
													</tr>
													<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >Reason for Return:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="Emails/Email/ProductDetail/@Quantity"/>
															</td>
															</tr>
													<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Reason for Return or Complaint:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="Emails/Email/ProductDetail/@ReasonForReturn"/>
														</td>
													</tr>
													<tr>
														<td width="19%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
															order Number:
														</td>
														<td width="32%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<td><xsl:value-of select="Emails/Email/@OrderNumber"/></td>
								
														</td>

													</tr>
													<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Order Date:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
															<xsl:value-of select="Emails/Email/@OrderDate"/>
														</td>
													</tr>
													
													<tr>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">&#160;</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">&#160;</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">&#160; </td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">&#160;</td>
													</tr>
												</table>
												</xsl:if>
												<xsl:apply-templates select="Emails"/>						
		
											</td>
										</tr>
										<tr>
										
										<!-- <xsl:template match="Emails">		
	 -->
											<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
											
											<xsl:if test = 'Emails/Email/@ServiceRequestType ="ItemReturnRequest"'>
	    
												<table width="100%" border="0" cellpadding="0" cellspacing="0">
													<tr>
														<td width="5" bgcolor="818181"><img align="left" src="table-left.jpg" alt="xpedx" title="xpedx" width="5" height="28" /></td>
														<td align="center" width="99" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181; padding:0px 2px; ">Description</td>
														<td align="center" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  padding:0px 2px;  background-color:#818181;">Quantity Shipped</td>
														<td width="53" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff; background-color:#818181; padding:0px 2px; ">Qty to Return</td>
														<td align="center" width="66" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff; background-color:#818181;  padding:0px 2px; ">UOM</td>
														<td align="center" width="66" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff; background-color:#818181;  padding:0px 2px; ">Reason for return</td>
														
														<td width="7" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;"><img src="table-right.jpg" width="5" height="28" alt="xpedx" align="right" title="xpedx" /></td>
													</tr>
						  
													<!-- add for each order line here -->						  
													<xsl:for-each select="Emails/Email/ProductDetail">
													<tr>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px;border-bottom:solid 1px #818181; color:#000;border-left:solid 1px #818181; color:#000;">&#160;</td>
															<td align="center" style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;padding:10px 2px; "><xsl:value-of select="@ItemShortDescription"/><BR/><BR/>xpedx # <xsl:value-of select="@ItemId"/></td>
															<td  align="center" style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; color:#000;border-bottom:solid 1px #818181; color:#000;padding:10px 2px; "><xsl:value-of select="@QTYShipped"/></td>
															<td align="center" style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; "><xsl:value-of select="@QTYReturn"/></td>
															<td align="center" style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; border-bottom:solid 1px #818181; color:#000;color:#000;padding:10px 2px; "><xsl:value-of select="@UOM"/></td>
															<td align="center" style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; border-bottom:solid 1px #818181; color:#000;color:#000;padding:10px 2px; "><xsl:value-of select="@ReasontoReturn"/></td>
															
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;  border-right:solid 1px #818181; color:#000;">&#160;</td>
														</tr>
													</xsl:for-each>
												</table>
																	
												<div align="center"></div>
												
												</xsl:if>
											</td>
											<xsl:apply-templates select="Emails"/>						
		
											<!-- </xsl:template>
										 --></tr>

									    <tr>
											<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
												<div align="left">Please do not reply  to this email. This mailbox is not monitored and you will not receive a response. </div>                </td>
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