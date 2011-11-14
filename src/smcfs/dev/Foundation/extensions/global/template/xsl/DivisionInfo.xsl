<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output indent="yes" />

   <xsl:template match="/">
      <xsl:element name="Divisions">
         <xsl:element name="Division">
            <xsl:attribute name="EnvironmentId">
               <xsl:value-of select="Divisions/Division/@EnvironmentId" />
            </xsl:attribute>

            <xsl:attribute name="CompanyCode">
               <xsl:value-of select="Divisions/Division/@CompanyCode" />
            </xsl:attribute>

            <xsl:attribute name="ProcessCode">
               <xsl:value-of select="Divisions/Division/@ProcessCode" />
            </xsl:attribute>

	    <xsl:attribute name="DivisionNumber">
               <xsl:value-of select="Divisions/Division/@AccessBranchNo" />
            </xsl:attribute>

            <xsl:attribute name="DivisionName">
               <xsl:value-of select="Divisions/Division/@BranchName" />
            </xsl:attribute>

            <xsl:attribute name="Address1">
               <xsl:value-of select="Divisions/Division/@AddressLine1" />
            </xsl:attribute>

            <xsl:attribute name="Address2">
               <xsl:value-of select="Divisions/Division/@AddressLine1" />
            </xsl:attribute>

            <xsl:attribute name="Address3">
               <xsl:value-of select="Divisions/Division/@AddressLine3" />
            </xsl:attribute>

          
            <xsl:attribute name="CountryCode">
               <xsl:value-of select="Divisions/Division/@CountryCode" />
            </xsl:attribute>

            <xsl:attribute name="DivisionPhone">
               <xsl:value-of select="Divisions/Division/@DivisionPhone" />
            </xsl:attribute>

            <xsl:attribute name="DivisionFax">
               <xsl:value-of select="Divisions/Division/@DivisionFax" />
            </xsl:attribute>

            <xsl:attribute name="PricingWarehouse">
               <xsl:value-of select="Divisions/Division/@PricingWarehouse" />
            </xsl:attribute>

            <xsl:attribute name="Geocode">
               <xsl:value-of select="Divisions/Division/@Geocode" />
            </xsl:attribute>

            <xsl:attribute name="JDECode">
               <xsl:value-of select="Divisions/Division/@JDECode" />
            </xsl:attribute>

            <xsl:attribute name="eTradingID">
               <xsl:value-of select="Divisions/Division/@eTradingID" />
            </xsl:attribute>

            <xsl:attribute name="BrandCode">
               <xsl:value-of select="Divisions/Division/@BrandCode" />
            </xsl:attribute>

            <xsl:element name="TransferCircles">
               <xsl:element name="TransferCircle">
                  <xsl:attribute name="DivisionNumber">
                     <xsl:value-of select="Divisions/Division/@DivisionNumber" />
                  </xsl:attribute>

                  <xsl:attribute name="DivisionName">
                     <xsl:value-of select="Divisions/Division/@DivisionName" />
                  </xsl:attribute>

                  <xsl:attribute name="NoOfDays">
                     <xsl:value-of select="Divisions/Division/@NoOfDays" />
                  </xsl:attribute>

                  <xsl:attribute name="OrderCutOffTime">
                     <xsl:value-of select="Divisions/Division/@OrderCutOffTime" />
                  </xsl:attribute>
               </xsl:element>

<!-- End Of TransferCircle -->
            </xsl:element>

<!-- End Of TransferCircles -->
         </xsl:element>

<!-- End OF Division -->
      </xsl:element>

<!-- End of Divisions-->
   </xsl:template>
</xsl:stylesheet>

