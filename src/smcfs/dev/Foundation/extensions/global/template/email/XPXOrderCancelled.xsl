<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:emailformatters="com.yantra.pca.email.formatters"
    xmlns:java="java"
    exclude-result-prefixes="java emailformatters">

<xsl:template match="/OrderList/Order">
<xsl:comment>CONTENT_TYPE=text/html; charset=UTF-8</xsl:comment>
<xsl:variable name="brandLogo" >
	<xsl:value-of select="@BrandLogo" />
</xsl:variable> 

<html>
<body>

<table width="600" border="0" align="center" cellpadding="2" cellspacing="2">
  <tr>
    <td><img src="{$brandLogo}" width="210" height="79" alt="xpedx" longdesc="http://www.xpedx.com" /></td>
  </tr>
  <tr>
      <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" ><table width="100%" border="0" cellpadding="0"  style="border:solid 1px #999;  padding:20px 20px 0px 20px;">
      <tr>
        <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><table width="" border="0">
          <tr>
            <td width="600" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td>
          </tr>
          <tr>
            <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Your <xsl:value-of select="@EnterpriseCode"/>.com order has been cancelled. Please review the details of your order below. <a href="#" style="color:#000;">Click Here</a> to review this order on <xsl:value-of select="@EnterpriseCode"/>.com </td>
          </tr>
          <tr>
            <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td>
          </tr>

          <tr>
            <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><table width="100%" border="0">
              <tr>
                <td colspan="2" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><strong>Order Information:</strong></td>
                <td colspan="2" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><strong>Shipping Information:</strong></td>
                </tr>
              <tr>
                <td width="19%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >Order Number:</td>
                <td width="32%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="@OrderNo"/></td>
                <td width="17%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Location:</td>
                <td width="32%" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="./Extn/@ExtnShipToName"/></td>
              </tr>
              <tr>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Order Date:</td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="substring(@OrderDate,6,2)"/>/<xsl:value-of select="substring(@OrderDate,9,2)"/>/<xsl:value-of select="substring(@OrderDate,1,4)"/></td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Address1:</td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="./PersonInfoShipTo/@AddressLine1"/></td>
              </tr>
              <tr>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >Order Status:</td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Cancelled</td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Address2:</td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="./PersonInfoShipTo/@AddressLine2"/></td>
              </tr>
              <tr>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">PO #:</td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="@CustomerPONo"/></td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">City, State:</td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="concat(./PersonInfoShipTo/@City,',',./PersonInfoShipTo/@State)"/></td>
              </tr>
              <tr>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Placed By:</td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="./Extn/@ExtnOrderedByName"/></td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Zip:</td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="./PersonInfoShipTo/@ZipCode"/></td>
              </tr>
              <tr>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"></td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" ></td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Country:</td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="./PersonInfoShipTo/@Country"/></td>
              </tr>
              <tr>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Order Comments:</td>
                 <xsl:for-each select="//Instruction">
                <xsl:if test="@InstructionType='HEADER'">
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="@InstructionText"/></td>
                 </xsl:if>
                </xsl:for-each>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Shipping Comments:</td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="./Extn/@ExtnOrdStatCom"/></td>
              </tr>             
              <tr>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td>
              </tr>
            </table>
 
              </td>
          </tr>
          <tr>
            <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td width="5" bgcolor="818181"><img align="left" src="table-left.jpg" alt="xpedx" title="xpedx" width="5" height="28" /></td>
                <td width="99" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181; padding:0px 2px; ">Item Number:</td>
                <td width="76" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  padding:0px 2px;  background-color:#818181;">Item Description:</td>
                <td width="53" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff; background-color:#818181; padding:0px 2px; ">QTY Ordered </td>
                <td width="66" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff; background-color:#818181;  padding:0px 2px; ">QTY Backordered</td>
                <td width="57" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181; padding:0px 2px; ">QTY UOM </td>
                <td width="62" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;  padding:0px 2px; ">Price/Price UOM</td>
                <td width="79" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;  padding:0px 2px; ">QTY <br />Shippable</td>
                <td width="76" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;  padding:0px 2px; ">Shippable Total:</td>
                <td width="7" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;"><img src="table-right.jpg" width="5" height="28" alt="xpedx" align="right" title="xpedx" /></td>
              </tr>
              <!-- Order Line Start  -->
              <xsl:for-each select="//OrderLine">
              <tr>
                <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;border-left:solid 1px #818181; color:#000;"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td>
                <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;padding:10px 2px; "><xsl:value-of select="./Item/@ItemID"/></td>
                <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; color:#000;border-bottom:solid 1px #818181; color:#000;padding:10px 2px; "><xsl:value-of select="./Item/@ItemDesc"/></td>
                <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; "><xsl:value-of select="./Extn/@ExtnBaseOrderedQty"/></td>
                <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; "><xsl:value-of select="./Extn/@ExtnReqBackOrdQty"/></td>
                <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; border-bottom:solid 1px #818181; color:#000;color:#000;padding:10px 2px; "><xsl:value-of select="@OrderingUOM"/></td>
                <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181;border-bottom:solid 1px #818181; color:#000; color:#000;padding:10px 2px; "><xsl:value-of select="concat(./Extn/@ExtnUnitPriceDiscount,'/',./Extn/@ExtnPricingUOM)"/></td>
                <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; border-bottom:solid 1px #818181; color:#000;color:#000;padding:10px 2px; "><xsl:value-of select="./Extn/@ExtnShipQtyBase"/></td>
                <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-left:solid 1px #818181; border-bottom:solid 1px #818181; color:#000;color:#000;padding:10px 2px; "><xsl:value-of select="./Extn/@ExtnLineShippableTotal"/></td>
                <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;  border-right:solid 1px #818181; color:#000;"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td>
              </tr>
              <tr>
                <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;border-left:solid 1px #818181; color:#000;"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td>
                <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;padding:10px 2px; ">Line Comments:</td>
                <xsl:for-each select="//Instruction">
                <xsl:if test="@InstructionType='LINE'">
                <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px;border-bottom:solid 1px #818181; color:#000; border-left:solid 1px #818181; color:#000;; color:#000;padding:10px 2px;" colspan="7" ><xsl:value-of select="@InstructionText"/></td>
                </xsl:if>
                </xsl:for-each>
                <td  style="font-family: Arial, Geneva, sans-serif;font-size:12px; border-bottom:solid 1px #818181; color:#000;  border-right:solid 1px #818181; color:#000;"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></td>
              </tr>              
              </xsl:for-each>
              <!-- Order Line End  -->
              
            </table>
              <div align="center"></div></td>
          </tr>
          <tr>
            <td style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000; text-align: center;">This document merely confirms your order; it is not an acceptance of your order. Additional fees may apply to accepted orders.<br /><br /></td> 
          </tr>
          <tr>
            <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
                <div align="center">Please do not reply  to this email. This mailbox is not monitored and you will not receive a response</div></td>
          </tr>
          <tr>
            <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"></td>
          </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
</table>

</body>
</html>

</xsl:template>
</xsl:stylesheet>
