<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:output method="html"/>
	<xsl:template match="/">
		<FORM name="Form1">
			<xsl:attribute name="action"><xsl:value-of select="Products/hook_url" disable-output-escaping="yes"/></xsl:attribute>
			<xsl:attribute name="method">post</xsl:attribute>
			
			<xsl:for-each select="//OrderLine">
				<input type="hidden" name="NEW_ITEM-DESCRIPTION[{@PrimeLineNo}]" >
				  <xsl:attribute name="value">
				  <xsl:value-of select='substring(Item/@ItemDesc,1,40)' disable-output-escaping="yes" />
				  </xsl:attribute>
				</input>
				<input type="hidden" name="NEW_ITEM-MATGROUP[{@PrimeLineNo}]" value="{ItemDetails/ClassificationCodes/@UNSPSC}"/>
				<input type="hidden" name="NEW_ITEM-QUANTITY[{@PrimeLineNo}]" value="{@OrderedQty}"/>
				<input type="hidden" name="NEW_ITEM-UNIT[{@PrimeLineNo}]" value="{Item/@UnitOfMeasure}"/>
				<input type="hidden" name="NEW_ITEM-PRICE[{@PrimeLineNo}]" value="{LineOverallTotals/@LineTotal}"/>
				<input type="hidden" name="NEW_ITEM-PRICEUNIT[{@PrimeLineNo}]" value="1"/>
				<input type="hidden" name="NEW_ITEM-CURRENCY[{@PrimeLineNo}]" value="USD"/>
				<input type="hidden" name="NEW_ITEM-LEADTIME[{@PrimeLineNo}]" value="3"/>
				<input type="hidden" name="NEW_ITEM-VENDOR[{@PrimeLineNo}]" value="001316561"/>
				<input type="hidden" name="NEW_ITEM-VENDORMAT[{@PrimeLineNo}]" value="{LegacySKU}"/>
  
				
				
				<input type="hidden" name="NEW_ITEM-CONTRACT[{@PrimeLineNo}]" value=""/>
				<input type="hidden" name="NEW_ITEM-CONTRACT_ITEM[{@PrimeLineNo}]" value=""/>
				<input type="hidden" name="NEW_ITEM-SERVICE[{@PrimeLineNo}]" value=""/>
				<input type="hidden" name="NEW_ITEM-EXT_QUOTE_ID[{@PrimeLineNo}]" value=""/>
				<input type="hidden" name="NEW_ITEM-EXT_QUOTE_ITEM[{@PrimeLineNo}]" value=""/>
				<input type="hidden" name="NEW_ITEM-EXT_PRODUCT_ID[{@PrimeLineNo}]" value="{LegacySKU}"/>
				<input type="hidden" name="NEW_ITEM-EXT_SCHEMA_TYPE[{@PrimeLineNo}]" value="UNSPSC"/>
				<input type="hidden" name="NEW_ITEM-EXT_CATEGORY_ID[{@PrimeLineNo}]" value="{ItemDetails/ClassificationCodes/@UNSPSC}"/>
			</xsl:for-each>
		</FORM>
	</xsl:template>
</xsl:stylesheet>
