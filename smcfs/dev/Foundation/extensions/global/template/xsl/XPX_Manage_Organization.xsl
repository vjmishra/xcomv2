<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output indent="yes" /> 
<xsl:template match="/">
<Organization>
<xsl:variable name="EnvironmentID" select="Divisions/Division/@EnvironmentId"/>
<xsl:attribute name="Operation">
<xsl:if test="Divisions/Division/@ProcessCode ='A'">Manage</xsl:if>
<xsl:if test="Divisions/Division/@ProcessCode ='C'">Manage</xsl:if>
<xsl:if test="Divisions/Division/@ProcessCode ='D'">Delete</xsl:if>
</xsl:attribute>
<xsl:attribute  name="OrganizationCode">
<xsl:value-of select='concat(Divisions/Division/@DivisionNumber,"_",$EnvironmentID)' />
</xsl:attribute>
<xsl:attribute  name="OrganizationName">
<xsl:value-of select="Divisions/Division/@DivisionName" />
</xsl:attribute>
<xsl:attribute  name="PrimaryEnterpriseKey">xpedx</xsl:attribute>
<xsl:attribute  name="IsNode">Y</xsl:attribute>
<xsl:attribute  name="ParentOrganizationCode">xpedx</xsl:attribute>
<ContactPersonInfo>
<xsl:attribute  name="AddressLine1">
<xsl:value-of select="Divisions/Division/@Address1" />
</xsl:attribute>
<xsl:attribute  name="AddressLine2">
<xsl:value-of select="Divisions/Division/@Address2" />
</xsl:attribute>
<xsl:attribute  name="AddressLine3">
<xsl:value-of select="Divisions/Division/@Address3" />
</xsl:attribute>
<xsl:attribute  name="City">
<xsl:value-of select="Divisions/Division/@City" />
</xsl:attribute>
<xsl:attribute  name="Country">
<xsl:value-of select="Divisions/Division/@CountryCode" />
</xsl:attribute>
<xsl:attribute  name="ZipCode">
<xsl:value-of select="Divisions/Division/@ZipCode" />
</xsl:attribute>
<xsl:attribute  name="State">
<xsl:value-of select="Divisions/Division/@State" />
</xsl:attribute>
<xsl:attribute  name="DayPhone">
<xsl:value-of select="Divisions/Division/@DivisionPhone" />
</xsl:attribute>
<xsl:attribute  name="DayFaxNo">
<xsl:value-of select="Divisions/Division/@DivisionFax" />
</xsl:attribute>
</ContactPersonInfo>
<CorporatePersonInfo>
<xsl:attribute  name="AddressLine1">
<xsl:value-of select="Divisions/Division/@Address1" />
</xsl:attribute>
<xsl:attribute  name="AddressLine2">
<xsl:value-of select="Divisions/Division/@Address2" />
</xsl:attribute>
<xsl:attribute  name="AddressLine3">
<xsl:value-of select="Divisions/Division/@Address3" />
</xsl:attribute>
<xsl:attribute  name="City">
<xsl:value-of select="Divisions/Division/@City" />
</xsl:attribute>
<xsl:attribute  name="Country">
<xsl:value-of select="Divisions/Division/@CountryCode" />
</xsl:attribute>
<xsl:attribute  name="ZipCode">
<xsl:value-of select="Divisions/Division/@ZipCode" />
</xsl:attribute>
<xsl:attribute  name="State">
<xsl:value-of select="Divisions/Division/@State" />
</xsl:attribute>
<xsl:attribute  name="DayPhone">
<xsl:value-of select="Divisions/Division/@DivisionPhone" />
</xsl:attribute>
<xsl:attribute  name="DayFaxNo">
<xsl:value-of select="Divisions/Division/@DivisionFax" />
</xsl:attribute>
</CorporatePersonInfo>
<BillingPersonInfo>
<xsl:attribute  name="AddressLine1">
<xsl:value-of select="Divisions/Division/@Address1" />
</xsl:attribute>
<xsl:attribute  name="AddressLine2">
<xsl:value-of select="Divisions/Division/@Address2" />
</xsl:attribute>
<xsl:attribute  name="AddressLine3">
<xsl:value-of select="Divisions/Division/@Address3" />
</xsl:attribute>
<xsl:attribute  name="City">
<xsl:value-of select="Divisions/Division/@City" />
</xsl:attribute>
<xsl:attribute  name="Country">
<xsl:value-of select="Divisions/Division/@CountryCode" />
</xsl:attribute>
<xsl:attribute  name="ZipCode">
<xsl:value-of select="Divisions/Division/@ZipCode" />
</xsl:attribute>
<xsl:attribute  name="State">
<xsl:value-of select="Divisions/Division/@State" />
</xsl:attribute>
<xsl:attribute  name="DayPhone">
<xsl:value-of select="Divisions/Division/@DivisionPhone" />
</xsl:attribute>
<xsl:attribute  name="DayFaxNo">
<xsl:value-of select="Divisions/Division/@DivisionFax" />
</xsl:attribute>
</BillingPersonInfo>
<Node>
<xsl:attribute  name="NodeType">DC</xsl:attribute>
</Node>
<Extn>
<xsl:attribute  name="ExtnPriceWarehouse">
<xsl:value-of select="Divisions/Division/@PricingWarehouse" />
</xsl:attribute>
<xsl:attribute  name="ExtnGeoCode">
<xsl:value-of select="Divisions/Division/@Geocode" />
</xsl:attribute>
<xsl:attribute  name="ExtnJdeCode">
<xsl:value-of select="Divisions/Division/@JDECode" />
</xsl:attribute>
<xsl:attribute  name="ExtnEtradingID">
<xsl:value-of select="Divisions/Division/@eTradingID" />
</xsl:attribute>
<xsl:attribute  name="ExtnBrandCode">
<xsl:value-of select="Divisions/Division/@BrandCode" />
</xsl:attribute>
<xsl:attribute  name="ExtnCurrencyCode">
<xsl:value-of select="Divisions/Division/@CurrencyCode" />
</xsl:attribute>
<xsl:attribute  name="ExtnEnvtId">
<xsl:value-of select="Divisions/Division/@EnvironmentId" />
</xsl:attribute>
<xsl:attribute  name="ExtnCompanyCode">
<xsl:value-of select="Divisions/Division/@CompanyCode" />
</xsl:attribute>

<XPXDivBrandList>
		<xsl:attribute  name="Reset">true</xsl:attribute>
<xsl:for-each select="Divisions/Division">
<XPXDivBrand>
<xsl:attribute  name="BrandCode">
<xsl:value-of select="@BrandCode" />
</xsl:attribute>
<xsl:attribute  name="DivisionNo">
<xsl:value-of select='concat(@DivisionNumber,"_",$EnvironmentID)' />
</xsl:attribute>
</XPXDivBrand>
</xsl:for-each>
</XPXDivBrandList>
<XPXXferCircleList>

		<xsl:attribute  name="Reset">true</xsl:attribute>


<xsl:for-each select="Divisions/Division/TransferCircles/TransferCircle">
<XPXXferCircle>
<xsl:attribute  name="SequenceNo">
<xsl:value-of select="position()" />
</xsl:attribute>
<xsl:attribute  name="DivisionName">
<xsl:value-of select="@DivisionName" />
</xsl:attribute>
<xsl:attribute  name="DivisionNo">
<xsl:value-of select='concat(@DivisionNumber,"_",$EnvironmentID)' />
</xsl:attribute>
<xsl:attribute  name="NoOfDays">
<xsl:value-of select="@NoOfDays" />
</xsl:attribute>
<xsl:attribute  name="OrderCutOffTime">
<xsl:value-of select="@OrderCutOffTime" />
</xsl:attribute>
</XPXXferCircle>
</xsl:for-each >
</XPXXferCircleList>

 </Extn>   
  </Organization>
  </xsl:template>
  </xsl:stylesheet>



