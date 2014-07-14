<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

          <xsl:template match="/">

                   	<xsl:element name="Order">

                                
			     <xsl:attribute name="OrderHeaderKey">
                             
                                 <xsl:value-of select="Order/@OrderHeaderKey"/>

                             </xsl:attribute>
                        </xsl:element>

          </xsl:template>
 </xsl:stylesheet>                       