<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match = "Order">
     
     <xsl:element name="Order">
     	<xsl:copy-of select="@*"/>
     
     	<xsl:attribute name="EntryType">
     		<xsl:text>EDI</xsl:text>
     	</xsl:attribute>
     	<xsl:copy-of select = "./*"/>
     </xsl:element>     
     
</xsl:template>
</xsl:stylesheet> 