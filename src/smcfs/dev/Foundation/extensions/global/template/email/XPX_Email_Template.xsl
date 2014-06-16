<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:emailformatters="com.yantra.pca.email.formatters"
    xmlns:java="java"
    exclude-result-prefixes="java emailformatters">
	
	<xsl:template match="User">
		
		<xsl:variable name="mailToPrefix" select="'mailto:'"/>	
		<xsl:variable name="mailID" select="'distribution.webmaster@ipaper.com'"/>
	
		<xsl:variable name="mailTo">    
			<xsl:value-of select="concat($mailToPrefix, $mailID)"/>   
		</xsl:variable> 
	
	<html>
		<body>
			<table width="600" border="0" align="center" cellpadding="2" cellspacing="2">
				<tr>
					<td>
						<img src="logo-email.jpg" width="210" height="79" alt="xpedx" longdesc="http://www.xpedx.com" />
					</td>
				</tr>
				<tr>
					<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >
						<table width="100%" border="0" cellpadding="0"  style="border:solid 1px #999;  padding:20px 20px 0px 20px;">
							<tr>
								<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
									<table width="" border="0">
										<tr>
											<td style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000; text-align: left;">Dear <b>
											<xsl:value-of select="./ContactPersonInfo/@FirstName"/>
											<xsl:text> </xsl:text>
											<xsl:value-of select="./ContactPersonInfo/@LastName"/>
											</b>, <br /><br /></td>
										</tr>
										<tr>
											<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
												Thank you for registering online with <xsl:value-of select="./@OrganizationKey"/>.                
												Your registration information has been sent to your local customer service department for processing.You will be contacted soon with logon information specific to your account so you can begin using our online functionality.
												<span ></span>
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
												<div align="center">
													Questions? Please contact our Support team at 
													<a href="{$mailTo}" style="color:#000;">distribution.webmaster@ipaper.com </a> or
													<b>877-269-1784</b>.
													   Please do not reply to this email. This mailbox is not monitored and you will not receive a response.													
													<span ></span>
												</div>                
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