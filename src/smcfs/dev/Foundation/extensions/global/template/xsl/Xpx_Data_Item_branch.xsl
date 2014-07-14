
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   
   <xsl:output indent="yes" />
   
   <xsl:template match="/">
      
      <xsl:element name="ItemBranchs">
         
         <xsl:element name="ItemBranch">         
           
           <xsl:attribute name="EnvironmentId">
               <xsl:value-of select="ItemBranchs/ItemBranch/@EnvironmentId" />
            </xsl:attribute>

            <xsl:attribute name="CompanyCode">
               <xsl:value-of select="ItemBranchs/ItemBranch/@CompanyCode" />
            </xsl:attribute>

            <xsl:attribute name="ProcessCode">
               <xsl:value-of select="ItemBranchs/ItemBranch/@ProcessCode" />
            </xsl:attribute>

            <xsl:attribute name="MasterProductCode">
               <xsl:value-of select="ItemBranchs/ItemBranch/@MasterProductCode" />
            </xsl:attribute>

            <xsl:attribute name="LegacyProductNumber">
               <xsl:value-of select="ItemBranchs/ItemBranch/@LegacyProductNumber" />
            </xsl:attribute>

            <xsl:attribute name="DivisionNumber">
               <xsl:value-of select="ItemBranchs/ItemBranch/@DivisionNumber" />
            </xsl:attribute>

            <xsl:attribute name="CustomerNumber">
               <xsl:value-of select="ItemBranchs/ItemBranch/@CustomerNumber" />
            </xsl:attribute>

          
            <xsl:attribute name="ProductStatus">
               <xsl:value-of select="ItemBranchs/ItemBranch/@ProductStatus" />
            </xsl:attribute>

            <xsl:attribute name="ItemStockStatus">
               <xsl:value-of select="ItemBranchs/ItemBranch/@ItemStockStatus" />
            </xsl:attribute>

            <xsl:attribute name="InventoryIndicator">
               <xsl:value-of select="ItemBranchs/ItemBranch/@InventoryIndicator" />
            </xsl:attribute>
            
            <xsl:attribute name="OrderMultiple">
               <xsl:value-of select="ItemBranchs/ItemBranch/@OrderMultiple" />
            </xsl:attribute>

            <xsl:attribute name="InventoryIndicator">
               <xsl:value-of select="ItemBranchs/ItemBranch/@InventoryIndicator" />
            </xsl:attribute>
	            
	            <xsl:element name="AlternateItems">
		              <xsl:element name="AlternateItem">
		               	<xsl:attribute name="MasterProductCode">
		               		<xsl:value-of select="ItemBranchs/ItemBranch/@MasterProductCode" />
		            	</xsl:attribute>
	               </xsl:element>
               </xsl:element>
               
               <xsl:element name="ReplacementItems">
					<xsl:element name="ReplacementItem">
						<xsl:attribute name="MasterProductCode">
							<xsl:value-of select="ItemBranchs/ItemBranch/@MasterProductCode" />
						</xsl:attribute>
                    </xsl:element>
               </xsl:element>
               
               <xsl:element name="ComplimentaryItems">
					<xsl:element name="ComplimentaryItem">
						<xsl:attribute name="MasterProductCode">
							<xsl:value-of select="ItemBranchs/ItemBranch/@MasterProductCode" />
						</xsl:attribute>
                    </xsl:element>
               </xsl:element>
               
             </xsl:element>
             
             </xsl:element>


   </xsl:template>
   </xsl:stylesheet>