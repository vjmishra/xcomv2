<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output indent="yes" />
	<xsl:template match="Order">

		<xsl:element name="Order">
			<xsl:copy-of select="@*" />
			<xsl:attribute name="DocumentType">
     			<xsl:text>0009.ex</xsl:text>
     		</xsl:attribute>
			<xsl:attribute name="PaymentStatus">
	     		<xsl:text></xsl:text>
	     	</xsl:attribute>
			<xsl:attribute name="OrderHeaderKey">
	     		<xsl:text></xsl:text>
	     	</xsl:attribute>
			<xsl:for-each select="Extn">
				<xsl:element name="Extn">
					<xsl:copy-of select="@*" />
				</xsl:element>
			</xsl:for-each>
			<xsl:element name="OrderLines">
				<xsl:for-each select="OrderLines/OrderLine">
					<xsl:element name="OrderLine">
						<xsl:copy-of select="@*" />
						<xsl:attribute name="OrderHeaderKey">
				     		<xsl:text></xsl:text>
				     	</xsl:attribute>
						<xsl:attribute name="OrderLineKey">
				     		<xsl:text></xsl:text>
				     	</xsl:attribute>
						<xsl:copy-of select="./*" />
					</xsl:element>
				</xsl:for-each>
			</xsl:element>
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>