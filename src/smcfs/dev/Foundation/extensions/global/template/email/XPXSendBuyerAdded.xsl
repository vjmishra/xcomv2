<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:lxslt="http://xml.apache.org/xslt" version="1.0">
   <xsl:template match="/">
   
		<xsl:variable name="brandLogo" >
			<xsl:value-of select="//@BrandLogo" />
		</xsl:variable>	
		
<html > 
<body>
<table width="600" border="0" align="center" cellpadding="2" cellspacing="2">
  <tr>
    <td height="44"><img src="{$brandLogo}" width="210" height="79" alt="xpedx"  /></td>
  </tr>
  <tr>
    <td ><table width="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td ><table width="" border="0" cellpadding="0" style="border:solid 1px #999;  padding:20px 20px 0px 20px;">
          <tr>
            <td width="600"  style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000; padding:0px 8px"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td>
          </tr>
          <tr>
            <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000; padding:0px 8px"><span style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">This is an automated notification</span> is  to confirm that the following buyer been added to your  <xsl:value-of select="//@SellerOrganizationCode" />.com account.</td>
          </tr>
          <tr>
            <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000; padding:0px 8px"><xsl:text  disable-output-escaping="yes">&amp;nbsp;</xsl:text></td>
          </tr> 
          <tr>
           <td style="border:solid 0px #999;  margin:0px 0px;  padding:0px 5px;"><table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td width="5" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#fff;  background-color:#818181; background-position:right; )"><img align="left" src="table-left.jpg" alt="xpedx" title="xpedx" width="5" height="28" /></td>
                <td width="33%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#fff; background-image: url(table-left.jpg) background-repeat:no-repeat; background-color:#818181; background-position:right; background-position:left; )">Account Number:</td>
                <td width="33%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#fff; background-image: url(table-left.jpg) background-repeat:no-repeat; background-color:#818181; background-position:right; background-position:left; )">Buyer Name</td>
                <td width="33%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#fff; background-image: url(table-left.jpg) background-repeat:no-repeat; background-color:#818181; background-position:right; background-position:left; )">Email Address:</td>
                <td width="5" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#fff;  background-color:#818181;)"><img src="table-right.jpg" width="5" height="28" alt="xpedx" align="right" title="xpedx" /></td>
              </tr>
              <tr>
                <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181;border-left:solid 1px #818181; color:#000;"><xsl:text  disable-output-escaping="yes">&amp;nbsp;</xsl:text></td>
                <td width="33%"  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;padding:10px 2px; "><xsl:value-of select="//@AccountNumber" /></td>
                <td width="33%"  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;padding:10px 2px; "><strong><xsl:value-of select="//@FirstName" /></strong>
				<xsl:text> </xsl:text>
				<strong><xsl:value-of select="//@LastName" /></strong></td>
                <td width="33%"  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;padding:10px 2px;  " ><a href="#" style="color:#000;"><xsl:value-of select="//@cEmailAddress" /></a></td>
                <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181;  border-right:solid 1px #818181; color:#000;"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td>
                </tr>
            </table></td>
          </tr>
          
          <tr>
            <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000; padding:0px 8px"><xsl:text  disable-output-escaping="yes">&amp;nbsp;</xsl:text></td>
          </tr>
          <tr>
             <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000; padding:0px 8px"><span >              Thank  you for your business!</span></td>
          </tr>
          <tr>
            <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000; padding:0px 8px; text-align:center;"><xsl:text  disable-output-escaping="yes">&amp;nbsp;</xsl:text></td>
          </tr>
          <tr>
             <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000; padding:0px 8px; text-align:center;"> Please do not reply  to this email. This mailbox is not monitored and you will not receive a response</td>
          </tr>
          <tr>
            <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000; padding:0px 8px; text-align:center;"><xsl:text  disable-output-escaping="yes">&amp;nbsp;</xsl:text></td>
          </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
</table>
<p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></p>
</body>
</html>
</xsl:template>
</xsl:stylesheet>
