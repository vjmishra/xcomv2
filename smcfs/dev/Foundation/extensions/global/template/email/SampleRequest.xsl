<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:emailformatters="com.yantra.pca.email.formatters"
    xmlns:java="java"
    exclude-result-prefixes="java emailformatters">
		<xsl:template match="/">
		<html>
		<xsl:comment>CONTENT_TYPE=text/html</xsl:comment>
			<HEAD>
				<title>Sample Service Request</title>
			</HEAD>

			<body>
				<table width="600" border="0" align="center" cellpadding="2" cellspacing="2">
					<tr>
						<td><img src="xpedx_r_rgb_lo.jpg" width="216" height="69" alt="xpedx" longdesc="http://www.xpedx.com" /></td>
					</tr>
					<tr>
					<xsl:if test = 'Emails/Email/@RequestType ="SampleServiceRequest"'>
					
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
													<span style="font-family:Arial, Geneva, sans-serif; font-size:20px; font-weight:normal;">		Action Required-Customer Sample Request 
													</span>
												</td>
											</tr>
											<tr>
												<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"> &#160; </td>
											</tr>                                                                               
                                            			    
										    <tr>
												<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
													<table width="100%" border="0">
													<tr>
														<td width="19%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Shipping Information</td>
													</tr><tr>
													<td width="19%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:for-each select="Emails/Email/ShippingAddress">										
														<xsl:value-of select="@address"/>										
										    			</xsl:for-each>
													</td>
													</tr>
									<tr>
												<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"> &#160; </td>
											</tr>                                                                               
                                            
									
														<tr>
															<td width="19%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
																Contact:
															</td>
															<td width="27%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																<xsl:value-of select="Emails/Email/@Contact"/>
															</td>
															<td width="19%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Notes</td>
															<td><xsl:value-of select="Emails/Email/@Notes"/></td>
															</tr>
															<tr>
															<td width="22%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																address1:
															</td>
															<td width="32%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																<xsl:value-of select="Emails/Email/@address1"/>															</td>
																<td width="32%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="Emails/Email/@ServiceProvider"/> #:</td>
														<td><xsl:value-of select="Emails/Email/@ServiceProviderNumber"/></td>
									
														   </tr>
															<tr>
															<td width="19%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
																address2:
															</td>
															<td width="27%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																<xsl:value-of select="Emails/Email/@address2"/>
															</td></tr>
															<tr>
															<td width="22%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																address3:
															</td>
															<td width="32%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																<xsl:value-of select="Emails/Email/@address2"/>
															</td></tr>
															<tr>
															<td width="22%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																city:
															</td>
															<td width="32%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																<xsl:value-of select="Emails/Email/@city"/>
															</td></tr>
															<tr>
															<td width="22%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																state:
															</td>
															<td width="32%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																<xsl:value-of select="Emails/Email/@state"/>
															</td></tr>
															<tr>
															<td width="22%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																country:
															</td>
															<td width="32%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																<xsl:value-of select="Emails/Email/@country"/>
															</td></tr>
															<tr>
															<td width="22%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																postal code:
															</td>
															<td width="32%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																<xsl:value-of select="Emails/Email/@zipCode"/>
															</td></tr>
															<tr>
															<td width="22%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																Phone:
															</td>
															<td width="32%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																<xsl:value-of select="Emails/Email/@Phone"/>
															</td></tr>
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
												<xsl:if test = 'Emails/Email/@RequestType ="SampleServiceRequest"'>
	    
												<tr>
												<td width="7" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;"></td>
													<td width="99" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181; padding:0px 2px; ">Item Number</td>
														<td width="76" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  padding:0px 2px;  background-color:#818181;">Manufacturer/Mill</td>
														<td width="53" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff; background-color:#818181; padding:0px 2px; ">Mfg Sku</td>
														<td width="57" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181; padding:0px 2px; ">Description </td>
														<td width="62" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;  padding:0px 2px; ">Qty</td>
														<td width="7" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;"></td>
													
												</tr>				
												<xsl:for-each select="Emails/Email/SampleRequest">
												<tr>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px;border-bottom:solid 1px #818181; color:#000;border-left:solid 1px #818181; color:#000;">&#160;</td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;padding:10px 2px; "><xsl:value-of select="@ItemNumber"/></td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; color:#000;border-bottom:solid 1px #818181; color:#000;padding:10px 2px; "><xsl:value-of select="@Mfg"/></td>
                                                            <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; "><xsl:value-of select="@MfgSku"/></td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; "><xsl:value-of select="@Description"/></td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; border-bottom:solid 1px #818181; color:#000;color:#000;padding:10px 2px; "><xsl:value-of select="@Qty"/></td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;  border-right:solid 1px #818181; color:#000;">&#160;</td>
														</tr>
														
												</xsl:for-each>		
												</xsl:if>		
												<xsl:if test = 'Email/@RequestType ="SampleRequest"'>
		
												<tr>
												<td width="7" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;"></td>
													<td width="99" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181; padding:0px 2px; ">Account Number</td>
														<td width="76" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  padding:0px 2px;  background-color:#818181;">City</td>
														<td width="53" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff; background-color:#818181; padding:0px 2px; ">Contact Phone Number</td>
														<td width="57" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181; padding:0px 2px; ">Customer Job Title </td>
														<td width="76" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  padding:0px 2px;  background-color:#818181;">Customer Name</td>
														<td width="53" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff; background-color:#818181; padding:0px 2px; ">Division Number</td>
														<td width="57" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181; padding:0px 2px; ">Manufacturer </td>
														<td width="62" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;  padding:0px 2px; ">Manufacturer Part Number</td>
														<td width="62" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;  padding:0px 2px; ">Notes</td>
														<td width="76" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  padding:0px 2px;  background-color:#818181;">Quantity</td>
														<td width="53" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff; background-color:#818181; padding:0px 2px; ">Requested Delivery Date</td>
														<td width="57" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181; padding:0px 2px; ">Sales Representative </td>
														<td width="76" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  padding:0px 2px;  background-color:#818181;">Shipping Address2</td>
														<td width="53" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff; background-color:#818181; padding:0px 2px; ">Shipping Address3</td>
														<td width="57" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181; padding:0px 2px; ">State </td>
														<td width="62" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;  padding:0px 2px; ">Unit of Measure</td>
														<td width="62" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;  padding:0px 2px; ">Zip</td>
														<td width="62" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;  padding:0px 2px; ">Email1</td>
														<td width="62" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;  padding:0px 2px; ">Email2</td>
														
														
														<td width="7" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;"></td>
														
														
																															
													
												</tr>		
												<tr>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px;border-bottom:solid 1px #818181; color:#000;border-left:solid 1px #818181; color:#000;">&#160;</td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;padding:10px 2px; "><xsl:value-of select="Email/@AccountNumber"/></td>
                                                            <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; "><xsl:value-of select="Email/@CityRequest"/></td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; "><xsl:value-of select="Email/@ContactPhoneNumber"/></td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; border-bottom:solid 1px #818181; color:#000;color:#000;padding:10px 2px; "><xsl:value-of select="Email/@CustomerJobTitle"/></td>
															
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; color:#000;border-bottom:solid 1px #818181; color:#000;padding:10px 2px; "><xsl:value-of select="Email/@CustomerName"/></td>
                                                            <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; "><xsl:value-of select="Email/@DivisionNumber"/></td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; "><xsl:value-of select="Email/@Manufacturer"/></td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; border-bottom:solid 1px #818181; color:#000;color:#000;padding:10px 2px; "><xsl:value-of select="Email/@ManufacturerPartNumber"/></td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; "><xsl:value-of select="Email/@Notes"/></td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; "><xsl:value-of select="Email/@Quantity"/></td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; border-bottom:solid 1px #818181; color:#000;color:#000;padding:10px 2px; "><xsl:value-of select="Email/@RequestedDeliveryDate"/></td>
															
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; color:#000;border-bottom:solid 1px #818181; color:#000;padding:10px 2px; "><xsl:value-of select="Email/@SalesRepresentative1"/></td>
                                                            <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; "><xsl:value-of select="Email/@ShippingAddress2"/></td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; "><xsl:value-of select="Email/@ShippingAddress3"/></td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; border-bottom:solid 1px #818181; color:#000;color:#000;padding:10px 2px; "><xsl:value-of select="Email/@StateRequest"/></td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; color:#000;border-bottom:solid 1px #818181; color:#000;padding:10px 2px; "><xsl:value-of select="Email/@UnitofMeasure1"/></td>
                                                            <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; "><xsl:value-of select="Email/@ZipRequest"/></td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; "><xsl:value-of select="Email/@Email1"/></td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; border-bottom:solid 1px #818181; color:#000;color:#000;padding:10px 2px; "><xsl:value-of select="Email/@Email2"/></td>
															
															
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;  border-right:solid 1px #818181; color:#000;">&#160;</td>
														</tr>
																																											
				
											</xsl:if>
											</table></td></tr>
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
						</xsl:if>
					</tr>
				</table>
				<xsl:apply-templates select="Emails"/>
			</body>
		</html>
	</xsl:template>
	</xsl:stylesheet>