<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:lxslt="http://xml.apache.org/xslt" version="1.0">
	<xsl:template match="/">
		<html>
		<xsl:call-template name="applyStyle"/>
		<body>
		<table width="600" border="0" align="center" cellpadding="2" cellspacing="2" topmargin="0" leftmargin="0" STYLE="font:normal 10pt Tahoma">
		  <tr>
		    <td height="44"><img src="xpedx_r_rgb_lo.jpg" width="216" height="69" alt="xpedx"  /></td>
		  </tr>
		  <tr>
		    <td><table width="100%" border="0" cellpadding="0" cellspacing="0" style="border:solid 1px #999;  padding:20px 20px 0px 20px;">
		      <tr>
		        <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><table width="" border="0" cellpadding="0">
		          <tr>
		            <td width="600" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">This is a courtesy notification that your profile has been changed at <xsl:value-of select="//@BrandName" />.com. If you did not change your profile,please contact us at <a href="mailto:distribution.webmaster@ipaper.com" style="color:#000;">distribution.webmaster@ipaper.com</a> or  <strong> 877-269-1784</strong>.</td>
		          </tr>
		           <tr>
		            <td width="600" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Changes to your profile could be Password, User Name, or Security Question/Answer being changed.
					</td>
		          </tr>
		         
		          <tr>
		            <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><br />
		              Thank  you for your business!</td>
		          </tr>
		          <tr>
		            <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000; text-align:center"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td>
		          </tr>
		          <tr>
		            <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000; text-align:center"> Please do not reply  to this email. This mailbox is not monitored and you will not receive a response</td>
		          </tr>
		          <tr>
		            <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000; text-align:center"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td>
		          </tr>
		        </table>
		        </td>
		      </tr>
		    </table></td>
		  </tr>
		  </table>
		<p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></p>
		</body>
		</html>
	</xsl:template>
	<xsl:template name="applyStyle">
		<xsl:comment>CONTENT_TYPE=text/html; charset=UTF-8</xsl:comment>
		<HEAD>
		    <STYLE TYPE="text/css">
	
			.table  {
			    padding:0;
			    font-size: 12;
			    font-family: Tahoma;
			    font-weight: normal;
			    color: #000000;
			    width: 100%;
			    border: 1;
			}
	
			.tablecolumn{
			    padding-left:2px; 
			    padding-right:2px; 
			    padding-top: 0px;
			    padding-bottom: 0px;
			    font-size: 12;
			    vertical-align: top;
			    text-align: left;
			}
			.numerictablecolumn{
			    padding-left:2px; 
			    padding-right:2px; 
			    padding-top: 0px;
			    padding-bottom: 0px;
			    vertical-align: top;
			    text-align: right;
			    font-size: 12;
			}
			.tablecolumnheader {
			    border-left:1px solid buttonhighlight;
			    border-right:1px solid buttonshadow;
			    border-top:1px solid buttonhighlight;
			    border-bottom:1px solid buttonshadow;
			    PADDING-LEFT: 2px;
			    PADDING-RIGHT: 2px;
			    PADDING-top: 0px;
			    PADDING-bottom: 0px;
			    VERTICAL-ALIGN: middle;
			    HORIZONTAL-ALIGN: center;
			    BACKGROUND-COLOR: #e0e0e0;
			    TEXT-ALIGN: left
			}
		    </STYLE>
	
		</HEAD>
	</xsl:template>

</xsl:stylesheet>