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
			<table width="600" border="0" align="center" cellpadding="0" cellspacing="0">
			  <tr>
				<td height="44"><img src="logo-email.jpg" width="210" height="47" alt="xpedx" longdesc="http://www.xpedx.com" /></td>
			  </tr>
			  <tr>
				<td ><table width="100%" border="0" cellpadding="0" cellspacing="0" style="border:solid 1px #999;  padding:20px 20px 0px 20px;">
				  <tr>
					<td><table width="" border="0" cellpadding="0" cellspacing="0">
					  <tr>
						<td width="600" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
							<b>
								<xsl:value-of select="./ContactPersonInfo/@FirstName"/>
							</b>, 
							<br /><br />
							<b>
								<xsl:value-of select="./ContactPersonInfo/@FirstName"/>
								<xsl:text> </xsl:text>
								<xsl:value-of select="./ContactPersonInfo/@LastName"/>
							</b>  requested that we send this e-mail. If you have questions about <xsl:value-of select="./@OrganizationKey"/>.com.
						</td>
					  </tr>
					  <tr>
						<td ><br></br></td>
					  </tr>
					  <tr>
						<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; border:solid 1px #818181;color:#000;padding:0px 2px; "> 
						  <table width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr>
							  <td width="45%" rowspan="8" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><div align="center"><img name="" src="logo-email.jpg" width="210" height="47" alt="" /></div></td>
							  <td colspan="2" style="font-family:padding:10px 4px; Arial, Geneva, sans-serif;font-size:12px; color:#000;border-left:solid 1px #818181;"><h1 style="font-family:Arial, Geneva, sans-serif; font-size:20px; font-weight:normal; margin:10px 0px; margin-bottom:10px;">&lt;Product Heading&gt;</h1></td>
							  </tr>
							<tr>
							  <td width="22%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; color:#000;padding:10px 4px; border-top:solid 1px #818181;">Item Number:</td>
							  <td width="33%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; border-top:solid 1px #818181; color:#000;padding:10px 2px; ">&lt;itemnumber&gt;</td>
							</tr>
							<tr>
							  <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; color:#000;padding:10px 4px;border-top:solid 1px #818181; ">Description:</td>
							  <td style="border-top:solid 1px #818181; font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; color:#000;padding:10px 2px; ">&lt;description&gt;</td>
							</tr>
							<tr>
							  <td style="border-top:solid 1px #818181;font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; color:#000;padding:10px 4px; ">-</td>
							  <td style="border-top:solid 1px #818181;font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; color:#000;padding:10px 2px; ">-</td>
							</tr>
						   
							<tr>
							  <td style="font-family: Arial, Geneva, sans-serif; border-top:solid 1px #818181;font-size:12px; border-left:solid 1px #818181; color:#000;padding:10px 4px; ">-</td>
							  <td style="font-family: Arial, Geneva, sans-serif; border-top:solid 1px #818181;font-size:12px; border-left:solid 1px #818181; color:#000;padding:10px 2px; ">-</td>
							</tr>
							<tr>
							  <td style="border-top:solid 1px #818181; font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; color:#000;padding:10px 4px; ">-</td>
							  <td style="border-top:solid 1px #818181; font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; color:#000;padding:10px 2px; ">-</td>
							</tr>
							<tr>
							  <td colspan="2" style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; color:#000;padding:10px 4px;border-top:solid 1px #818181; ">-</td>
							  </tr>
						  </table>
						  </td>
					  </tr>
					  <tr>
						<td><br></br></td>
					  </tr>
					  <tr>
						<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">              Thank  you for your business!</td>
					  </tr>
					  <tr>
						<td><br></br></td>
					  </tr>
					  <tr>
						 <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000; text-align:center"> Please do not reply  to this email. This mailbox is not monitored and you will not receive a response</td>
					  </tr>
					  <tr>
						<td><br></br></td>
					  </tr>
					</table></td>
				  </tr>
				</table></td>
			  </tr>
			</table>
			<p>-</p>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>