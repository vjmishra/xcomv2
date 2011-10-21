<xsl:stylesheet version = "1.0" xmlns:xsl = "http://www.w3.org/1999/XSL/Transform">
    <xsl:output indent = "yes"/>
    <xsl:template match = "/">
        <XPEDXLegacyUomXref>
            <xsl:attribute name = "Operation">
                <xsl:if test = "LegacyUOMs/LegacyUOM/@ProcessCode ='A'">Create</xsl:if>
                <xsl:if test = "LegacyUOMs/LegacyUOM/@ProcessCode ='C'">Modify</xsl:if>
                <xsl:if test = "LegacyUOMs/LegacyUOM/@ProcessCode ='D'">Delete</xsl:if>
            </xsl:attribute>
            <xsl:attribute name = "UOM">
                <xsl:value-of select = "LegacyUOMs/LegacyUOM/@EDIUom"/>
            </xsl:attribute>
            <xsl:attribute name = "LegacyType">
                <xsl:value-of select = "LegacyUOMs/LegacyUOM/@EnvironmentId"/>
            </xsl:attribute>
            <xsl:attribute name = "LegacyUOM">
                <xsl:value-of select = "LegacyUOMs/LegacyUOM/@LegacyUom"/>
            </xsl:attribute>
            <xsl:attribute name = "LegacyDesc">
                <xsl:value-of select = "LegacyUOMs/LegacyUOM/@UOMDescription"/>
            </xsl:attribute>
            <xsl:attribute name = "CompanyCode">
                <xsl:value-of select = "LegacyUOMs/LegacyUOM/@CompanyCode"/>
            </xsl:attribute>
        </XPEDXLegacyUomXref>
    </xsl:template>
</xsl:stylesheet>