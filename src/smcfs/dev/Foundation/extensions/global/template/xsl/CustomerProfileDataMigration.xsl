<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output indent="yes" />
   <xsl:template match="/Customer"> 
		<xsl:element name="Customer">
			<xsl:copy-of select="@*" />
			<xsl:attribute name="Operation">
				<xsl:value-of select="'Modify'" />
			</xsl:attribute>
		
			<xsl:copy-of select="./*" />
			
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>