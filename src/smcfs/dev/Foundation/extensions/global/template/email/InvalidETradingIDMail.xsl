<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:lxslt="http://xml.apache.org/xslt" version="1.0">
   <xsl:template match="/">
      <HTML>
         <xsl:comment>CONTENT_TYPE=text/html</xsl:comment>
         <HEAD>
            <title>CSR RESOLUTION EMAIL</title>
         </HEAD>
         
         <BODY>
              <p>
              <font face="Verdana">Hi,
               
                <br></br>
               The B2B order could not be created due to an Invalid or Missing ETradingID.The details of the order are as follows:
                <br></br>
                <br></br>
               BuyerID: <xsl:value-of select="/Order/@BuyerID" />
                <br></br>
               LiaisonHeaderMessageID:  <xsl:value-of select="/Order/@LiaisonHeaderMessageID" />
                <br></br>
               ETradingID:  <xsl:value-of select="/Order/@ETradingID" />
                <br></br>
                <br></br>
                
               Please do get in touch with xpedx customer support for any queries in this regard.
               <br></br>
                              
               </font>
               </p>
         </BODY>      
               
       </HTML>
       
    </xsl:template>
 </xsl:stylesheet>     