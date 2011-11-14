<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:lxslt="http://xml.apache.org/xslt" version="1.0">
   <xsl:template match="/">
      <HTML>
         <xsl:comment>CONTENT_TYPE=text/html</xsl:comment>
         <HEAD>
            <title>CSR HOLD RESOLUTION EMAIL</title>
         </HEAD>
         
         <BODY>
              <p>
              <font face="Verdana">
               <xsl:value-of select="/Order/@BillToID" />,
                <xsl:text> </xsl:text>
                <br></br>
               The order with Web Confirmation Number 
               #<xsl:value-of select="/Order/Extn/@ExtnWebConfNum" />
               needs your attention as it is placed on hold!
               </font>
               </p>
         </BODY>      
               
       </HTML>
       
    </xsl:template>
 </xsl:stylesheet>      