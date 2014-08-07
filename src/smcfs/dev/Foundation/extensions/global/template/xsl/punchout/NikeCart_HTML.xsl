<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:output method="html"/>
	<xsl:template match="/">
		<xsl:for-each select="//OrderLine">
			<input type="hidden" name="NEW_ITEM-DESCRIPTION[{@PrimeLineNo}]" value="{ItemDetails/PrimaryInformation/@ExtendedDescription}"/>
			<input type="hidden" name="NEW_ITEM-MATNR[{@PrimeLineNo}]" value=""/>
			
			<input type="hidden" name="NEW_ITEM-QUANTITY[{@PrimeLineNo}]">
				<xsl:attribute name="value">
					<xsl:value-of select='format-number(OrderLineTranQuantity/@OrderedQty, "#") ' disable-output-escaping="yes" />
				</xsl:attribute>
			</input>
			<input type="hidden" name="NEW_ITEM-UNIT[{@PrimeLineNo}]" value="{OrderLineTranQuantity/@TransactionalUOM}"/>
			<input type="hidden" name="NEW_ITEM-PRICE[{@PrimeLineNo}]">
				<xsl:attribute name="value">
					<xsl:value-of select='format-number(Extn/@UnitPriceRounded, "0.##")' disable-output-escaping="yes" />
				</xsl:attribute>
			</input>
			<input type="hidden" name="NEW_ITEM-CURRENCY[{@PrimeLineNo}]" value="USD"/>
			<input type="hidden" name="NEW_ITEM-PRICEUNIT[{@PrimeLineNo}]" value=""/>
			<input type="hidden" name="NEW_ITEM-LEADTIME[{@PrimeLineNo}]" value="3"/>
			<input type="hidden" name="NEW_ITEM-VENDOR[{@PrimeLineNo}]" value=""/>
			<input type="hidden" name="NEW_ITEM-VENDORMAT[{@PrimeLineNo}]" value="{Item/@ItemID}"/>
			
			<input type="hidden" name="NEW_ITEM-MATGROUP[{@PrimeLineNo}]" value="{ItemDetails/Extn/@ExtnUNSPSC}"/>
			<input type="hidden" name="NEW_ITEM-SERVICE[{@PrimeLineNo}]" value=""/>
			<input type="hidden" name="NEW_ITEM-CONTRACT[{@PrimeLineNo}]" value=""/>
			<input type="hidden" name="NEW_ITEM-CONTRACT_ITEM[{@PrimeLineNo}]" value=""/>
			
			<input type="hidden" name="NEW_ITEM-EXT_QUOTE_ID[{@PrimeLineNo}]" value=""/>
			<input type="hidden" name="NEW_ITEM-EXT_QUOTE_ITEM[{@PrimeLineNo}]" value=""/>
			<input type="hidden" name="NEW_ITEM-EXT_PRODUCT_ID[{@PrimeLineNo}]" value="{Item/@ItemID}"/>
			
			<input type="hidden" name="NEW_ITEM-EXT_SCHEMA_TYPE[{@PrimeLineNo}]" value="UNSPSC"/>
			<input type="hidden" name="NEW_ITEM-EXT_CATEGORY_ID[{@PrimeLineNo}]" value="{ItemDetails/Extn/@ExtnUNSPSC}"/>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>

