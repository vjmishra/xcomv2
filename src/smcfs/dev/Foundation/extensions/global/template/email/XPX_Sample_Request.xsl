<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:emailformatters="com.yantra.pca.email.formatters"
    xmlns:java="java"
    exclude-result-prefixes="java emailformatters">
	
	<xsl:template match="Order">
		<html>
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
													<span style="font-family:Arial, Geneva, sans-serif; font-size:20px; font-weight:normal;">		Action Required - Customer Sample Request: 
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
															<td colspan="2" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																<strong>Sample Request Information:</strong>
															</td>
															<td colspan="2" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																<strong>Customer Information:</strong>
															</td>
														</tr>
														<tr>
															<td width="19%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
																Requested Delivery Date:
															</td>
															<td width="27%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																&lt;requestdeliverydate&gt;
															</td>
															<td width="22%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																Account Number:
															</td>
															<td width="32%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																&lt;accountnumber&gt;
															</td>
														</tr>
														<tr>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">			Customer Job Title:
															</td>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																&lt;OrderDate&gt;
															</td>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">			Customer Name:
															</td>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																&lt;custname&gt;
															</td>
														</tr>
														<tr>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
																Notes:
															</td>
															<td rowspan="3" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																&lt;Notes&gt;
															</td>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																Phone Number:
															</td>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																&lt;phone#&gt;
															</td>
														</tr>
														<tr>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																
															</td>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																Address:
															</td>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																&lt;Address1&gt;
															</td>
														</tr>
														<tr>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																
															</td>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																
															</td>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																&lt;Address2&gt;
															</td>
														</tr>
														<tr>
															<td colspan="2" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																<strong> Division Information:</strong>
															</td>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																City, State:
															</td>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																&lt;city&gt;,&lt;State&gt;
															</td>
														</tr>
														<tr>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																Division:
															</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
															&lt;division&gt;
														</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Zip:</td>
														<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">&lt;zip&gt;</td>
														</tr>
														<tr>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																Sales Rep:
															</td>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																&lt;salesrep&gt;
															</td>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																Country:
															</td>
															<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
																&lt;country
															</td>
														</tr>
													</table>
												</td>
											</tr>
											<tr>
												<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
													<br />
													<table width="100%" border="0" cellpadding="0" cellspacing="0">
														<tr>
															<td width="5" bgcolor="818181"><img align="left" src="table-left.jpg" alt="xpedx" title="xpedx" width="5" height="28" /></td>
															<td width="76" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181; padding:0px 2px; ">Manufacturer</td>
															<td width="94" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  padding:0px 2px;  background-color:#818181;">Manufacturer Item Number:</td>
															<td width="152" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff; background-color:#818181; padding:0px 2px; ">Item Description:</td>
															<td width="58" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181; padding:0px 2px; ">QTY  </td>
															<td width="81" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;  padding:0px 2px; ">UOM</td>
															<td width="97" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;  padding:0px 2px; ">QTY <br />Shippable</td>
															<td width="17" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;"><img src="table-right.jpg" width="5" height="28" alt="xpedx" align="right" title="xpedx" /></td>
														</tr>
														<tr>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px;border-bottom:solid 1px #818181; color:#000;border-left:solid 1px #818181; color:#000;">&#160;</td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;padding:10px 2px; ">&lt;mfr&gt;</td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; color:#000;border-bottom:solid 1px #818181; color:#000;padding:10px 2px; ">&lt;mfritem#&gt;</td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; ">&lt;itemdescription&gt;</td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; border-bottom:solid 1px #818181; color:#000;color:#000;padding:10px 2px; ">&lt;qty&gt;</td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; ">&lt;uom&gt;</td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; border-bottom:solid 1px #818181; color:#000;color:#000;padding:10px 2px; ">&lt;qtyshipp&gt;</td>
															<td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;  border-right:solid 1px #818181; color:#000;">&#160;</td>
														</tr>
													</table>
													<div align="center"></div>
												</td>
											</tr>
											<tr>
												<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
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