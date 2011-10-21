<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">	     
	     <xsl:output method="xml" version="1.0"
encoding="iso-8859-1" indent="yes"/>
   <xsl:template match="/">	                  
      <xsl:element name="MultiApi">
         <xsl:for-each select="XPXB2bCustomerSpecificData/XPXB2bLegacyUnspscXrefList/XPXB2bLegacyUnspscXref">
            <xsl:element name="API">
               <xsl:attribute name="FlowName">XPXInsIntoLegacyUNSPSCXRef</xsl:attribute>
               <xsl:element name="Input">
                 <xsl:element name="XPXB2bLegacyUnspscXref">
                 <xsl:copy-of select="@*" /> 
                  <xsl:attribute name ="LegacyItemID">
                    	<xsl:if test="@LegacyItemID != ''" >
                    		<xsl:value-of select="@LegacyItemID" />
                    	</xsl:if>  
                    	<xsl:if test="@LegacyItemID =''" >
                    		<xsl:value-of select="'DEFAULT'" />
                    	</xsl:if>                      	                    	                 	                    	
                    </xsl:attribute> 
				</xsl:element>
               </xsl:element>
            </xsl:element>
         </xsl:for-each>

         <xsl:for-each select="XPXB2bCustomerSpecificData/XPXB2bLegacyUomXrefList/XPXB2bLegacyUomXref">
            <xsl:element name="API">
               <xsl:attribute name="FlowName">XPXInsIntoLegacyUOMXRef</xsl:attribute>
               <xsl:element name="Input">
                  <xsl:copy-of select="." />
               </xsl:element>
            </xsl:element>
         </xsl:for-each>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>