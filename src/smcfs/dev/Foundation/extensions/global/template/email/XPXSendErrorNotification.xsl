<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:lxslt="http://xml.apache.org/xslt" version="1.0">
   <xsl:template match="/">
      <HTML>
         <xsl:comment>CONTENT_TYPE=text/html</xsl:comment>
         <head>
       

<title>Order Confirmation</title>
  <style type="text/css">
a:link {	text-decoration:underline;	color:#900;}
a:hover {	text-decoration:none;	color:#900;}
</style>
</head>


<body>

 
<table width="600" border="0" align="center" cellpadding="2" cellspacing="2">
  <tr>
  <xsl:variable name="BrandLogo">
   <xsl:value-of select="/RefOrder/@LogoPath"/>
  </xsl:variable>
    <td><img src="{$BrandLogo}" width="210" height="47" alt="xpedx" longdesc="http://www.xpedx.com" /></td>
  </tr>
  <tr>
      <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" ><table width="100%" border="0" cellpadding="0"  style="border:solid 1px #999;  padding:20px 20px 0px 20px;">
      <tr>
        <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><table width="" border="0">
          <tr>
            <td width="600" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><span style="font-family:Arial, Geneva, sans-serif; font-size:20px; font-weight:normal; margin:0px; margin-bottom:0px;">Action Required - B2B Order Received:</span></td>
          </tr>
          <tr>
            <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><br />
              The following order has been received in Call Center.  Please review the Order Status below to determine if this order requires further action. </td>
          </tr>
          <tr>
            <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"></td>
          </tr>

          <tr>
            <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><table width="100%" border="0">
              <tr>
                <td colspan="2" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><strong>Primary Details:</strong></td>
              </tr>
             
              	<tr>
             	 <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;" >BuyerID:</td>
                 <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="/RefOrder/@BuyerID" /></td>
                </tr>
                
                <tr>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">ETradingID:</td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="/RefOrder/@ETradingID" /></td>
                </tr>
                
                <tr>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">ShipToName:</td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="/RefOrder/@ShipToName" /></td>
                </tr>
                
                
                
                <tr>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">AttentionName:</td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="/RefOrder/@AttentionName" /></td>
                </tr>
                
                
                <tr>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Ship To Address1:</td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="/RefOrder/@ShipToAddress1"/></td>
                </tr>
                
                <tr>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Ship To Address2:</td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="/RefOrder/@ShipToAddress2"/></td>
                </tr>
                
                 <tr>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Ship To Address3:</td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="/RefOrder/@ShipToAddress3"/></td>
                </tr>
               
                
        		 <tr>
		        <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Ship To City:</td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="/RefOrder/@ShipToCity"/></td>
				</tr>              
                
        		 <tr>
		        <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Ship To State:</td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="/RefOrder/@ShipToState"/></td>
				</tr>
              
              
              
              <tr>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Ship To Zip:</td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="/RefOrder/@ShipToZIP" /></td>
              </tr>
                
              
              
              <tr>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Customer PO #:</td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="/RefOrder/@CustomerPO"/></td>
              </tr>
              
              
              
              <tr>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Ordered By Name:</td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="/RefOrder/@OrderedByName" /></td>
              </tr>
              
              
                <tr>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Order Date:</td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="/RefOrder/@OrderDate" /></td>
              </tr>
              
              
              
                <tr>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">Req Ship Date:</td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="/RefOrder/@OrderRequestedDeliveryDate" /></td>
              </tr>
              
              <tr>
              <td style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;">Comments:</td>
              <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><xsl:value-of select="/RefOrder/@HeaderComments" /></td>
             </tr>
          
              <tr>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"></td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"></td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"></td>
                <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"></td>
              </tr>
            </table>
 
              </td>
          </tr>
          <tr>
            <!--<td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><table width="100%" border="10" cellpadding="0" cellspacing="0">-->
            <!--<tr>
                <td width="5" bgcolor="818181"><img align="left" src="table-left.jpg" alt="xpedx" title="xpedx" width="5" height="28" /></td>
                <td width="99" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181; padding:0px 2px; ">LegacyProductCode:</td>
                <td width="76" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  padding:0px 2px;  background-color:#818181;">Item Description:</td>
                <td width="53" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff; background-color:#818181; padding:0px 2px; ">QTY Ordered </td>
                <td width="57" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181; padding:0px 2px; ">QTY UOM </td>
                <td width="62" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;  padding:0px 2px; ">Price/Price UOM</td>
                <td width="79" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;  padding:0px 2px; ">POLineID</td>
                <td width="76" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;  padding:0px 2px; ">CustomerProductCode:</td>
                <td width="7" style="font-family: Arial, Geneva, sans-serif;font-size:11px; color:#fff;  background-color:#818181;"><img src="table-right.jpg" width="5" height="28" alt="xpedx" align="right" title="xpedx" /></td>
              </tr>-->
              
              
              <table width="70%" border="0">
              <tr>
                <td colspan="2" style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"><strong>Order Details:</strong></td>
              </tr>
             
              <xsl:for-each select="/RefOrder/RefOrderLines/RefOrderLine">
               <tr></tr><tr></tr>
             
                <tr>
                <td  style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;">Customer Line #:</td>
                <td  style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;"><xsl:value-of select="@CustomerLineNo"/></td>
                </tr>
             
                 
                <tr>
                <td  style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;">Legacy Item #:</td>
                <td  style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;"><xsl:value-of select="@LegacyProductCode"/></td>
                </tr>
                
              
             	
                <tr>
                <td  style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;">Customer Item #:</td>
                <td  style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;"><xsl:value-of select="@CustomerProductCode"/></td>
                </tr>
                
             
             
                <tr>
                <td  style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;">Mfr Item #:</td>
                <td  style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;"><xsl:value-of select="@ManufacturerItem"/></td>
                </tr>
                
                
                <tr>
                <td  style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;">MPC #:</td>
                <td  style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;"><xsl:value-of select="@MasterProductCode"/></td>
                </tr>
                
                
                <tr>
                <td  style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;">Order Qty:</td>
                <td  style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;"><xsl:value-of select="@RequestedOrderQuantity" /></td>
                </tr>
                
                <tr>
                <td  style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000; ">Requested UOM:</td>
                <td  style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000; "><xsl:value-of select="@RequestedUnitOfMeasure" /></td>
                </tr>
                
                
                 <tr>
                 <td style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;">Price UOM:</td>
                 <td style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;"><xsl:value-of select="@PriceUnitOfMeasure" /></td>
                 </tr>
                
                <tr>
                <td style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;">Description:</td>
                <td  style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;"><xsl:value-of select="@LineDescription"/></td>
                </tr>
                
                
                <tr>
                <td  style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;">Unit Price:</td>
                <td style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;"><xsl:value-of select="@UnitPrice"/></td>
                </tr>
                 
                 
                 
                <tr>
                <td  style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;">Req Delivery Date:</td>
                <td style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;"><xsl:value-of select="@LineRequestedDeliveryDate"/></td>
                </tr>
                
      
                
                <tr>
                <td  style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;">Line Comments:</td>
                <td style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;"><xsl:value-of select="@LineNotes"/></td>
                </tr>
                
                
                 
                <tr> 
                <td  style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;">Line PO #:</td>
                <td style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;"> <xsl:value-of select="@CustomerLinePONumber"/></td>
                 </tr>
             
             
             
                <tr> 
                <td  style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;">Line Level Code:</td>
                <td style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;"> <xsl:value-of select="@POLineID"/></td>
                 </tr>
                 
                <tr> 
                <td  style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;">UDF 1:</td>
                <td style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;"> <xsl:value-of select="@CustLineField1"/></td>
                </tr>
                
                <tr> 
                <td  style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;">UDF 2:</td>
                <td style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;"> <xsl:value-of select="@CustLineField2"/></td>
                </tr>
                
                <tr> 
                <td  style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;">UDF 3:</td>
                <td style="font-family: Arial, Geneva, sans-serif; font-size:12px; color:#000;"> <xsl:value-of select="@CustLineField3"/></td>
                </tr> 
                 
                 
                 
              </xsl:for-each>
            </table>
              <div align="center"></div><!--</td>-->
          </tr>
          <tr>
            <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
                <div align="center">                Please do not reply  to this email. This mailbox is not monitored and you will not receive a response</div>                </td>
          </tr>
          <tr>
            <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;"></td>
          </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
</table>
<br/>
</body>
</HTML>
</xsl:template>
 </xsl:stylesheet>  
