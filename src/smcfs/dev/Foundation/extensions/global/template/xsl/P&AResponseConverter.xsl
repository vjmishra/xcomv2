<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" version="1.0" indent="yes" encoding="UTF-8" />
	<xsl:strip-space elements="*" />
	<xsl:template match="PriceAndAvailabilityResponse">
		<PriceAndAvailability>
            <TransactionStatus><xsl:value-of select="./sTransactionStatus" /></TransactionStatus>
 

            <EnvironmentId><xsl:value-of select="./sEnvironmentId" /></EnvironmentId>

            <Company><xsl:value-of select="./sCompany" /></Company>

            <CustomerBranch><xsl:value-of select="./sCustomerBranch" /></CustomerBranch>


            <CustomerNumber><xsl:value-of select="./sCustomerNumber" /></CustomerNumber>


            <ShipToSuffix><xsl:value-of select="./sShipToSuffix" /></ShipToSuffix>

            <OrderBranch><xsl:value-of select="./sOrderBranch" /></OrderBranch>

            <HeaderStatusCode><xsl:value-of select="./sHeaderStatusCode" /></HeaderStatusCode>

  			      <xsl:apply-templates select="aItems" />  
        			 	    
            
			
		</PriceAndAvailability>
	</xsl:template>

	<xsl:template match="aItems">
	<Items>
	<xsl:for-each select="aItem">
	<Item>
	
	                <LineNumber><xsl:value-of select="./sLineNumber" /></LineNumber>
	
	                <LegacyProductCode><xsl:value-of select="./sLegacyProductCode" /></LegacyProductCode>
	
	                <RequestedQtyUOM><xsl:value-of select="./sRequestedQtyUOM" /></RequestedQtyUOM>
	
	                <RequestedQty><xsl:value-of select="./sRequestedQty" /></RequestedQty>
	                
	                <OrderMultipleQty><xsl:value-of select="./sOrderMultipleQty" /></OrderMultipleQty>
	                
	                <OrderMultipleUOM><xsl:value-of select="./sOrderMultipleUOM" /></OrderMultipleUOM>	                
	
	                <PurchaseOrderQty><xsl:value-of select="./sPurchaseOrderQty" /></PurchaseOrderQty>
	
	                <PricingUOM><xsl:value-of select="./sPricingUOM" /></PricingUOM>
	
	                <PriceCurrencyCode><xsl:value-of select="./sPriceCurrencyCode" /></PriceCurrencyCode>
	
	                <UnitPricePerPricingUOM><xsl:value-of select="./sUnitPricePerPricingUOM" /></UnitPricePerPricingUOM>
	
	                <UnitPricePerRequestedUOM><xsl:value-of select="./sUnitPricePerRequestedUOM" /></UnitPricePerRequestedUOM>
	
	                <ExtendedPrice><xsl:value-of select="./sExtendedPrice" /></ExtendedPrice>
	
	                <ItemCost><xsl:value-of select="./sItemCost" /></ItemCost>
	
	                <CostCurrencyCode><xsl:value-of select="./sCostCurrencyCode" /></CostCurrencyCode>
	                
	                <LineStatusCode><xsl:value-of select="./sLineStatusCode" /></LineStatusCode>
	                
	                    <xsl:apply-templates select="aBrackets" />  
	                    <xsl:apply-templates select="aWarehouseLocations" />  
</Item>	                    
 		                
	  
	</xsl:for-each>
	</Items>
	</xsl:template>
	
	
	<xsl:template match="aBrackets">
	<Brackets>
	<xsl:for-each select="aBracket">
	
	<Bracket>
	
                    <BracketQTY><xsl:value-of select="./sBracketQTY" /></BracketQTY>

                    <BracketUOM><xsl:value-of select="./sBracketUOM" /></BracketUOM>

                    <BracketPrice><xsl:value-of select="./sBracketPrice" /></BracketPrice>
	
	    </Bracket>            
	</xsl:for-each>
	</Brackets>
	</xsl:template>	
	
	<xsl:template match="aWarehouseLocations">
	<WarehouseLocationList>
	<xsl:for-each select="aWarehouseLocation">
	<WarehouseLocation>
					<Warehouse><xsl:value-of select="./sWarehouse" /></Warehouse>

					<AvailableQty><xsl:value-of select="./sAvailableQty" /></AvailableQty>

					<NumberOfDays><xsl:value-of select="./sNumberOfDays" /></NumberOfDays>
	</WarehouseLocation>
	                
	</xsl:for-each>
	</WarehouseLocationList>
	</xsl:template>		

</xsl:stylesheet>

