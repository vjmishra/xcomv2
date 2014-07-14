<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>

<xsl:template name="ItemDataHeader">
	<xsl:param name="additionalRows" />
	<xsl:param name="param1" />
	<thead>
		<tr>
			<td class="tablecolumnheader">
				Item Description
			</td>

			<td class="tablecolumnheader" style="text-align:right;">
				Unit Price
			</td>

			<td class="tablecolumnheader" style="text-align:right;">
				Quantity
			</td>

			<td class="tablecolumnheader" style="text-align:right;">
				Total Price
			</td>

			<td class="tablecolumnheader" style="text-align:right;">
				Legacy Order No
			</td>
			<xsl:if test="$additionalRows > '0'" >
				<td class="tablecolumnheader" style="text-align:right;">
					<xsl:value-of select="$param1"/>
				</td>
			</xsl:if>
			<td  WIDTH="30%">
			</td>
		</tr>
	</thead>

</xsl:template>


<xsl:template name="ItemData">
	<xsl:param name="ItemDesc"></xsl:param>
	<xsl:param name="UnitPrice">0</xsl:param>
	<xsl:param name="Quantity">0</xsl:param>
	<xsl:param name="TotalPrice">0</xsl:param>
	<xsl:param name="TrackingNo">YantraDefault</xsl:param>
	<xsl:param name="UnitOfMeasure"></xsl:param>
	<xsl:param name="Currency"></xsl:param>
	<xsl:param name="LegacyOrderNo"></xsl:param>
	<tbody>
		<tr>
			<td class="tablecolumn">
				<xsl:value-of select="$ItemDesc"/>
			</td>
			<td class="numerictablecolumn">
				<xsl:value-of select="format-number($UnitPrice, '.00')"/><xsl:text> </xsl:text> <xsl:value-of select="$Currency"/>
			</td>
			<td class="numerictablecolumn">
				<xsl:value-of select="format-number($Quantity, '.00')"/><xsl:text> </xsl:text> <xsl:value-of select="$UnitOfMeasure"/>
			</td>
			<td class="numerictablecolumn">
				<xsl:value-of select="format-number($TotalPrice, '.00')"/><xsl:text> </xsl:text> <xsl:value-of select="$Currency"/>
			</td>
			<td class="tablecolumn">
				<xsl:value-of select="$LegacyOrderNo"/>
			</td>
			<xsl:if test="$TrackingNo != 'YantraDefault'" >
				<td class="numerictablecolumn">
					<xsl:value-of select="$TrackingNo"/>
				</td>
			</xsl:if>
		</tr>
	</tbody>

</xsl:template>

</xsl:stylesheet>