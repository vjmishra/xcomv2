<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output indent="yes" />
   <xsl:template match="/"> 
<xsl:element name="MultiApi">
   <xsl:for-each select="//CustomerAssignment">
<xsl:element name="API">
<xsl:attribute name="FlowName"/>
<xsl:attribute name="IsExtendedDbApi"/>
<xsl:attribute name="Name">manageCustomerAssignment</xsl:attribute>
<xsl:attribute name="Version"/>
<xsl:element name="Input">

		 <xsl:element name="CustomerAssignment"> 
			<xsl:attribute name="CustomerID">
				<xsl:value-of select="@CustomerID" />
			</xsl:attribute>
			 
			<xsl:attribute name="OrganizationCode">
				<xsl:value-of select="@OrganizationCode" />
			</xsl:attribute>

			<xsl:attribute name="UserId">
				<xsl:value-of select="@UserId" />
			</xsl:attribute>       

			<xsl:attribute name="Operation">
				<xsl:value-of select="@Operation" />
			</xsl:attribute>						
		</xsl:element>
			</xsl:element>
   </xsl:element>
		</xsl:for-each>
   </xsl:element>		
   </xsl:template>
</xsl:stylesheet>