<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output indent="yes" />
   <xsl:template match="/Organization"> 
			<xsl:element name="Organization">	 			 
				<xsl:attribute  name="OrganizationCode">
					<xsl:value-of select='concat(@DivisionNumber,"_",@EnvironmentID)' />
				</xsl:attribute>
				 
				<xsl:attribute  name="PrimaryEnterpriseKey">xpedx</xsl:attribute>
				<xsl:attribute  name="ParentOrganizationCode">xpedx</xsl:attribute>				

				<xsl:attribute  name="IsNode">
					<xsl:value-of select="@IsNode" />
				</xsl:attribute>
				
				<xsl:copy-of select="Extn/."/>
			</xsl:element>		
   </xsl:template>
</xsl:stylesheet>