<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" encoding="UTF-8" />
	<xsl:template match="/">
		<xsl:apply-templates select="Order" />
	</xsl:template>
	<xsl:template match="Order">
		<cXML version="1.2.014" payloadID="none">
			<xsl:attribute name="timestamp"></xsl:attribute>
			<Header>
				<From>
					<Credential domain="NetworkId">
						<Identity>@@replaceNetworkId1@@</Identity>
					</Credential>
				</From>
				<To>
					<Credential domain="NetworkId">
						<Identity>@@replaceNetworkId2@@</Identity>
					</Credential>
				</To>
				<Sender>
					<Credential domain="DUNS">
						<Identity>@@replaceNetworkId2@@</Identity>
					</Credential>
					<UserAgent />
				</Sender>
			</Header>
			<Message>
				<PunchOutOrderMessage>
					<BuyerCookie>@@replaceBuyerCookie@@</BuyerCookie>
					<BrowserFormPost><URL>@@replaceURL@@</URL></BrowserFormPost>
					<PunchOutOrderMessageHeader operationAllowed="edit">
							<Total>
							<Money currency="USD"><xsl:choose>
									<xsl:when test="Extn/@ExtnTotalOrderValue &gt; 0">
										<xsl:value-of select="Extn/@ExtnTotalOrderValue" />
									</xsl:when>
									<xsl:otherwise>0.00</xsl:otherwise>
								</xsl:choose>
							</Money>
						</Total>
					</PunchOutOrderMessageHeader>
					<xsl:apply-templates select="OrderLines" />
				</PunchOutOrderMessage>
			</Message>
		</cXML>
	</xsl:template>

	<xsl:template match="OrderLines">
		<xsl:apply-templates select="OrderLine" />
	</xsl:template>

	<xsl:template match="OrderLine">
		<ItemIn>
			<xsl:attribute name="quantity"><xsl:value-of select="OrderLineTranQuantity/@OrderedQty" /></xsl:attribute>
			<xsl:attribute name="lineNumber"><xsl:value-of select="@PrimeLineNo" /></xsl:attribute>
			<ItemID>
				<SupplierPartID>
					<xsl:value-of select="Item/@ItemID" />
				</SupplierPartID>
				<SupplierPartAuxiliaryID>
					<xsl:value-of select="CPN" />
				</SupplierPartAuxiliaryID>
			</ItemID>
			<ItemDetail>
				<UnitPrice>
					<Money currency="USD">
						<xsl:choose>
							<xsl:when test="Extn/@ExtnExtendedPrice &gt; 0">
								<xsl:value-of select="Extn/@ExtnExtendedPrice" />
							</xsl:when>
							<xsl:otherwise>0.00</xsl:otherwise>
						</xsl:choose>
					</Money>
				</UnitPrice>
				<Description xml:lang="en-US"><xsl:value-of select="Item/@ItemShortDesc" /></Description>
				<UnitOfMeasure>
					<xsl:value-of select="OrderLineTranQuantity/@TransactionalUOM" />
				</UnitOfMeasure>
				<Classification domain="UNSPSC">
					<xsl:value-of select="ItemDetails/Extn/@ExtnUNSPSC" />
				</Classification>

			</ItemDetail>
		</ItemIn>
	</xsl:template>
</xsl:stylesheet>