<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output indent="yes" />

   <xsl:template match="/">
      <xsl:element name ="CustomerList">
         <xsl:element name="Customer">
            <xsl:attribute name="EnvironmentId">
               <xsl:value-of select="CustomerList/Customer/@EnvironmentId" />
            </xsl:attribute>

            <xsl:attribute name="CompanyCode">
               <xsl:value-of select="CustomerList/Customer/@CompanyCode" />
            </xsl:attribute>

            <xsl:attribute name="ProcessCode">
               <xsl:value-of select="CustomerList/Customer/@ProcessCode" />
            </xsl:attribute>

            <xsl:attribute name="CustomerDivision">
               <xsl:value-of select="CustomerList/Customer/@CustomerDivision" />
            </xsl:attribute>

            <xsl:attribute name="LegacyCustomerNumber">
               <xsl:value-of select="CustomerList/Customer/@LegacyCustomerNumber" />
            </xsl:attribute>

            <xsl:attribute name="SuffixType">
               <xsl:value-of select="CustomerList/Customer/@SuffixType" />
            </xsl:attribute>

            <xsl:attribute name="ShipToSuffix">
               <xsl:value-of select="CustomerList/Customer/@ShipToSuffix" />
            </xsl:attribute>

            <xsl:attribute name="BillToSuffix">
               <xsl:value-of select="CustomerList/Customer/@BillToSuffix" />
            </xsl:attribute>

            <xsl:attribute name="CustomerOrderBranch">
               <xsl:value-of select="CustomerList/Customer/@CustomerOrderBranch" />
            </xsl:attribute>

            <xsl:attribute name="ShipFromBranch">
               <xsl:value-of select="CustomerList/Customer/@ShipFromBranch" />
            </xsl:attribute>

            <xsl:attribute name="CustomerStatus">
               <xsl:value-of select="CustomerList/Customer/@CustomerStatus" />
            </xsl:attribute>

            <xsl:attribute name="CustomerName">
               <xsl:value-of select="CustomerList/Customer/@CustomerName" />
            </xsl:attribute>

            <xsl:attribute name="BrandCode">
               <xsl:value-of select="CustomerList/Customer/@BrandCode" />
            </xsl:attribute>

            <xsl:attribute name="CustomerClass">
               <xsl:value-of select="CustomerList/Customer/@CustomerClass" />
            </xsl:attribute>

            <xsl:attribute name="ServiceOptimizationCode">
               <xsl:value-of select="CustomerList/Customer/@ServiceOptimizationCode" />
            </xsl:attribute>

            <xsl:attribute name="CurrencyCode">
               <xsl:value-of select="CustomerList/Customer/@CurrencyCode" />
            </xsl:attribute>

            <xsl:attribute name="InvoiceDistributionMethod">
               <xsl:value-of select="CustomerList/Customer/@InvoiceDistributionMethod" />
            </xsl:attribute>

            <xsl:attribute name="NationalAccountNumber">
               <xsl:value-of select="CustomerList/Customer/@NationalAccountNumber" />
            </xsl:attribute>

            <xsl:attribute name="SAPNumber">
               <xsl:value-of select="CustomerList/Customer/@SAPNumber" />
            </xsl:attribute>

            <xsl:attribute name="SAPParentAccountNumber">
               <xsl:value-of select="CustomerList/Customer/@SAPParentAccountNumber" />
            </xsl:attribute>

            <xsl:attribute name="ShipComplete">
               <xsl:value-of select="CustomerList/Customer/@ShipComplete" />
            </xsl:attribute>

            <xsl:attribute name="OrderUpdateFlag">
               <xsl:value-of select="CustomerList/Customer/@OrderUpdateFlag" />
            </xsl:attribute>

            <xsl:attribute name="CapsId">
               <xsl:value-of select="CustomerList/Customer/@CapsId" />
            </xsl:attribute>

          
           <xsl:element name="AddressList">
           
               <xsl:element name="Address">
                  <xsl:attribute name="AddressLine1">
                     <xsl:value-of select="CustomerList/Customer/@AddressLine1" />
                  </xsl:attribute>

                  <xsl:attribute name="AddressLine2">
                     <xsl:value-of select="CustomerList/Customer/@AddressLine2" />
                  </xsl:attribute>

                  <xsl:attribute name="AddressLine3">
                     <xsl:value-of select="CustomerList/Customer/@AddressLine3" />
                  </xsl:attribute>

                  <xsl:attribute name="City">
                     <xsl:value-of select="CustomerList/Customer/@City" />
                  </xsl:attribute>

                  <xsl:attribute name="State">
                     <xsl:value-of select="CustomerList/Customer/@State" />
                  </xsl:attribute>

			
                  <xsl:attribute name="Country">
                     <xsl:value-of select="CustomerList/Customer/@Country" />
                  </xsl:attribute>

                  <xsl:attribute name="ZipCode">
                     <xsl:value-of select="CustomerList/Customer/@ZipCode" />
                  </xsl:attribute>
               </xsl:element>
           </xsl:element>
 			<SalesReps>
               <SalesRep>
                  <EmployeeId />
               </SalesRep>
            </SalesReps>
           
         </xsl:element>
     </xsl:element>
     
   </xsl:template>
</xsl:stylesheet>

