<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output indent="yes" />
<xsl:template match="/">
<B2BInvoice>
	<EnvironmentId>
		<xsl:value-of select="XPXInvoiceHdr/@EnvironmentId"/>
	</EnvironmentId>
	<Company>
		<xsl:value-of select="XPXInvoiceHdr/@Company"/>
	</Company>
	<BuyerID>
		<xsl:value-of select="XPXInvoiceHdr/@CustomerBuyerID"/>
	</BuyerID>
	<WebConfirmationNumber>
		<xsl:value-of select="XPXInvoiceHdr/@WebConfirmationNo"/>
	</WebConfirmationNumber>
	
	<OrderingDivision>
	    <xsl:value-of select="substring-before(XPXInvoiceHdr/@OrderingDivision,'_')"/>
		<!--<xsl:value-of select="XPXInvoiceHdr/@OrderingDivision"/>-->
	</OrderingDivision>
	
	<LegacyOrderNumber>
		<xsl:value-of select="XPXInvoiceHdr/@LegacyOrderNo"/>
	</LegacyOrderNumber>
	<GenerationNumber>
		<xsl:value-of select="XPXInvoiceHdr/@GenerationNo"/>
	</GenerationNumber>
	<LegacyOrderType>
		<xsl:value-of select="XPXInvoiceHdr/@LegacyOrderType"/>
	</LegacyOrderType>
	<InvoiceDistributionFlag>
		<xsl:value-of select="XPXInvoiceHdr/@InvoiceDistFlag"/>
	</InvoiceDistributionFlag>
	
	<ShipFromDivision>
	    <xsl:value-of select="substring-before(XPXInvoiceHdr/@ShipFromDivision,'_')"/>
		<!--<xsl:value-of select="XPXInvoiceHdr/@ShipFromDivision"/>-->
	</ShipFromDivision>
	
	
	<CustomerEnvironmentID>
		<xsl:value-of select="XPXInvoiceHdr/@CustomerEnvironmentId"/>
	</CustomerEnvironmentID>
	
	<CustomerDivision>
	    <xsl:value-of select="substring-before(XPXInvoiceHdr/@CustomerDivision,'_')"/>
		<!--<xsl:value-of select="XPXInvoiceHdr/@CustomerDivision"/>-->
	</CustomerDivision>
	
	<CustomerNumber>
		<xsl:value-of select="XPXInvoiceHdr/@CustomerNo"/>
	</CustomerNumber>
	<ShipToSuffix>
		<xsl:value-of select="XPXInvoiceHdr/@ShipToSuffix"/>
	</ShipToSuffix>
	<ShipToName>
		<xsl:value-of select="XPXInvoiceHdr/@ShipToName"/>
	</ShipToName>
	<AttentionName>
		<xsl:value-of select="XPXInvoiceHdr/@AttentionName"/>
	</AttentionName>
	<ShipToAddress1>
		<xsl:value-of select="XPXInvoiceHdr/@ShipToAddr1"/>
	</ShipToAddress1>
	<ShipToAddress2>
		<xsl:value-of select="XPXInvoiceHdr/@ShipToAddr2"/>
	</ShipToAddress2>
	<ShipToAddress3>
		<xsl:value-of select="XPXInvoiceHdr/@ShipToAddr3"/>
	</ShipToAddress3>
	<ShipToCity>
		<xsl:value-of select="XPXInvoiceHdr/@ShipToCity"/>
	</ShipToCity>
	<ShipToState>
		<xsl:value-of select="XPXInvoiceHdr/@ShipToState"/>
	</ShipToState>
	<ShipToZIP>
		<xsl:value-of select="XPXInvoiceHdr/@ShipToZip"/>
	</ShipToZIP>
	<ShipToCountryCode>
		<xsl:value-of select="XPXInvoiceHdr/@ShipToCountry"/>
	</ShipToCountryCode>
	<BillToSuffix>
		<xsl:value-of select="XPXInvoiceHdr/@BillToSuffix"/>
	</BillToSuffix>
	<BillToName>
		<xsl:value-of select="XPXInvoiceHdr/@BillToName"/>
	</BillToName>
	<BillToAddress1>
		<xsl:value-of select="XPXInvoiceHdr/@BillToAddr1"/>
	</BillToAddress1>
	<BillToAddress2>
		<xsl:value-of select="XPXInvoiceHdr/@BillToAddr2"/>
	</BillToAddress2>
	<BillToAddress3>
		<xsl:value-of select="XPXInvoiceHdr/@BillToAddr3"/>
	</BillToAddress3>
	<BillToCity>
		<xsl:value-of select="XPXInvoiceHdr/@BillToCity"/>
	</BillToCity>
	<BillToState>
		<xsl:value-of select="XPXInvoiceHdr/@BillToState"/>
	</BillToState>
	<BillToZIP>
		<xsl:value-of select="XPXInvoiceHdr/@BillToZip"/>
	</BillToZIP>
	<BillToCountryCode>
		<xsl:value-of select="XPXInvoiceHdr/@BillToCountry"/>
	</BillToCountryCode>
	<CustomerHeaderPONumber>
		<xsl:value-of select="XPXInvoiceHdr/@CustomerPoNo"/>
	</CustomerHeaderPONumber>
	<ShipDate>
		<xsl:value-of select="XPXInvoiceHdr/@ShipDate"/>
	</ShipDate>
	<HeaderComments>
		<xsl:value-of select="XPXInvoiceHdr/@Instruction"/>
	</HeaderComments>
	<OrderedByName>
		<xsl:value-of select="XPXInvoiceHdr/@OrderedBy"/>
	</OrderedByName>
	<OrderCreateDate>
		<xsl:value-of select="XPXInvoiceHdr/@OrderDate"/>
	</OrderCreateDate>
	<CurrencyCode>
		<xsl:value-of select="XPXInvoiceHdr/@CurrencyCode"/>
	</CurrencyCode>
	<TotalShippableValue>
		<xsl:value-of select="XPXInvoiceHdr/@TotalShipableValue"/>
	</TotalShippableValue>
	<TotalOrderValue>
		<xsl:value-of select="XPXInvoiceHdr/@TotalOrderValue"/>
	</TotalOrderValue>
	<OrderSpecialCharges>
		<xsl:value-of select="XPXInvoiceHdr/@SpecialCharges"/>
	</OrderSpecialCharges>
	<TotalOrderFreight>
		<xsl:value-of select="XPXInvoiceHdr/@TotalFreight"/>
	</TotalOrderFreight>
	<TotalOrderTax>
		<xsl:value-of select="XPXInvoiceHdr/@TotalTax"/>
	</TotalOrderTax>
	<InvoicedDate>
		<xsl:value-of select="XPXInvoiceHdr/@InvoiceDate"/>
	</InvoicedDate>
	<LDInvoiceNumber>
		<xsl:value-of select="XPXInvoiceHdr/@LDInvoiceNo"/>
	</LDInvoiceNumber>
	<ShipVia>
		<xsl:value-of select="XPXInvoiceHdr/@ShipVia"/>
	</ShipVia>
	<CreditTerms>
		<xsl:value-of select="XPXInvoiceHdr/@CreditTerms"/>
	</CreditTerms>
	<SalesTaxPercentage>
		<xsl:value-of select="XPXInvoiceHdr/@SalesTaxPct"/>
	</SalesTaxPercentage>
	<SalesTaxAmount>
		<xsl:value-of select="XPXInvoiceHdr/@SalesTaxAmnt"/>
	</SalesTaxAmount>
	<InvoiceTotal>
		<xsl:value-of select="XPXInvoiceHdr/@InvoiceTotal"/>
	</InvoiceTotal>
	<TotalDiscountAllowed>
		<xsl:value-of select="XPXInvoiceHdr/@TotalDiscountAllowed"/>
	</TotalDiscountAllowed>
	<SCACNumber>
		<xsl:value-of select="XPXInvoiceHdr/@SCACNumber"/>
	</SCACNumber>
	<CarrierNameDescription>
		<xsl:value-of select="XPXInvoiceHdr/@CareerName"/>
	</CarrierNameDescription>
	<TermsNetDays>
		<xsl:value-of select="XPXInvoiceHdr/@TermsNetDays"/>
	</TermsNetDays>
	<TermsPercent>
		<xsl:value-of select="XPXInvoiceHdr/@TermsPercent"/>
	</TermsPercent>
	<CashDiscountDays>
		<xsl:value-of select="XPXInvoiceHdr/@CashDiscountDays"/>
	</CashDiscountDays>
	<DueDate>
		<xsl:value-of select="XPXInvoiceHdr/@DueDate"/>
	</DueDate>
	<RemitToDUNSNumber>
		<xsl:value-of select="XPXInvoiceHdr/@RemitToDunsNo"/>
	</RemitToDUNSNumber>
	<RemitToName>
		<xsl:value-of select="XPXInvoiceHdr/@RemitToName"/>
	</RemitToName>
	<RemitAddress1>
		<xsl:value-of select="XPXInvoiceHdr/@RemitAddr1"/>
	</RemitAddress1>
	<RemitAddress2>
		<xsl:value-of select="XPXInvoiceHdr/@RemitAddr2"/>
	</RemitAddress2>
	<RemitCity>
		<xsl:value-of select="XPXInvoiceHdr/@RemitCity"/>
	</RemitCity>
	<RemitState>
		<xsl:value-of select="XPXInvoiceHdr/@RemitState"/>
	</RemitState>
	<RemitZip>
		<xsl:value-of select="XPXInvoiceHdr/@RemitZip"/>
	</RemitZip>
	<BillToDUNSNumber>
		<xsl:value-of select="XPXInvoiceHdr/@BillToDunsNo"/>
	</BillToDUNSNumber>
	<SellerDUNSNumber>
		<xsl:value-of select="XPXInvoiceHdr/@SellerDunsNo"/>
	</SellerDUNSNumber>
	<ShiptoStoreNumber>
		<xsl:value-of select="XPXInvoiceHdr/@ShipToStoreNo"/>
	</ShiptoStoreNumber>
	<EmailAddress>
		<xsl:value-of select="XPXInvoiceHdr/@EmailAddr"/>
	</EmailAddress>
	<eTradingID>
		<xsl:value-of select="XPXInvoiceHdr/@ETradingID"/>
	</eTradingID>
	<LineItems>
		<xsl:for-each select="XPXInvoiceHdr/XPXInvoiceLineList/XPXInvoiceLine">
		<LineItem>
			<WebLineNumber>
				<xsl:value-of select="@WebLineNo"/>
			</WebLineNumber>
			<LegacyLineNumber>
				<xsl:value-of select="@LegacyLineNo"/>
			</LegacyLineNumber>
			<LineDistributionNumber>
				<xsl:value-of select="@LineDistNo"/>
			</LineDistributionNumber>
			<LegacyProductCode>
				<xsl:value-of select="@LegacyProductCode"/>
			</LegacyProductCode>
			<CustomerProductCode>
				<xsl:value-of select="@CustomerProductCode"/>
			</CustomerProductCode>
			
			<BaseUnitOfMeasure>
			    <!--<xsl:value-of select="substring-after(@BaseUom,'_')"/>-->
				<xsl:value-of select="@BaseUom"/>
			</BaseUnitOfMeasure>
			
			<OrderedQtyInBase>
				<xsl:value-of select="@OrderedQtyInBaseUom"/>
			</OrderedQtyInBase>
			<ShippableQtyInBase>
				<xsl:value-of select="@ShippedQtyInBaseUom"/>
			</ShippableQtyInBase>
			<BackOrderQtyInBase>
				<xsl:value-of select="@BackorderedQtyInBaseUom"/>
			</BackOrderQtyInBase>
			<PriceUnitOfMeasure>
				<xsl:value-of select="@PriceUom"/>
			</PriceUnitOfMeasure>
			<UnitPrice>
				<xsl:value-of select="@UnitPrice"/>
			</UnitPrice>
			<LineDescription>
				<xsl:value-of select="@LineDescription"/>
			</LineDescription>
			<RequestedUnitOfMeasure>
			   <!-- <xsl:value-of select="substring-after(@ReqUom,'_')"/>-->
				<xsl:value-of select="@ReqUom"/>
			</RequestedUnitOfMeasure>
			
			<OrderedQtyInRequestedUoM>
				<xsl:value-of select="@OrderedQtyInReqUom"/>
			</OrderedQtyInRequestedUoM>
			<ShippableQtyInRequestedUoM>
				<xsl:value-of select="@ShippableQtyInReqUom"/>
			</ShippableQtyInRequestedUoM>
			<BackOrderQtyInRequestedUoM>
				<xsl:value-of select="@BackorderedQtyInReqUOM"/>
			</BackOrderQtyInRequestedUoM>
			<CustomerLineSequenceNumber>
				<xsl:value-of select="@CustomerLineSeqNo"/>
			</CustomerLineSequenceNumber>
			<CustomerLinePONumber>
				<xsl:value-of select="@CustomerLinePoNo"/>
			</CustomerLinePONumber>
			<CustomerLineAccountNumber>
				<xsl:value-of select="@CustomerLineAccntNo"/>
			</CustomerLineAccountNumber>
			<CustomerLineField1>
				<xsl:value-of select="@CustUserField1"/>
			</CustomerLineField1>
			<CustomerLineField2>
				<xsl:value-of select="@CustUserField2"/>
			</CustomerLineField2>
			<CustomerLineField3>
				<xsl:value-of select="@CustUserField3"/>
			</CustomerLineField3>
			
			<ShipFromBranch>
			    <xsl:value-of select="substring-before(@ShipFromBranch,'_')"/>
				<!--<xsl:value-of select="@ShipFromBranch"/>-->
			</ShipFromBranch>
			
			<LineNotes>
				<xsl:value-of select="@LineNotes"/>
			</LineNotes>
			<LineTax>
				<xsl:value-of select="@LineTax"/>
			</LineTax>
			<LineTotal>
				<xsl:value-of select="@LineTotal"/>
			</LineTotal>
			<ShippableQtyLineTotal>
				<xsl:value-of select="@ShipQtyLineTotal"/>
			</ShippableQtyLineTotal>
			<LDOrderNumber>
				<xsl:value-of select="@LDOrderNumber"/>
			</LDOrderNumber>
			<ManufacturerItemNumber>
				<xsl:value-of select="@ManufacturerItemNo"/>
			</ManufacturerItemNumber>
			<UnitWeight>
				<xsl:value-of select="@UnitWeight"/>
			</UnitWeight>
			<WeightPerCode>
				<xsl:value-of select="@WeightPerCode"/>
			</WeightPerCode>
			<ExtendedWeight>
				<xsl:value-of select="@ExtendedWeight"/>
			</ExtendedWeight>
			<FSCCertificationCode>
				<xsl:value-of select="@FSCCertCode"/>
			</FSCCertificationCode>
			<SFICertificationCode>
				<xsl:value-of select="@SFICertCode"/>
			</SFICertificationCode>
			<PEFCCertificationCode>
				<xsl:value-of select="@PEFCCertCode"/>
			</PEFCCertificationCode>
			<LineShipDate>
				<xsl:value-of select="@LineShipDate"/>
			</LineShipDate>
			<CustomerPODate>
				<xsl:value-of select="@OrderDate"/>
			</CustomerPODate>
			
			<LineSalesTaxPercentage>
				<xsl:value-of select="@LineSalesTaxPercent"/>
			</LineSalesTaxPercentage>
			<BasisWeight>
				<xsl:value-of select="@BasisWeight"/>
			</BasisWeight>
			<RollWidth>
				<xsl:value-of select="@RollWidth"/>
			</RollWidth>
			<RollDiameter>
				<xsl:value-of select="@RollDiameter"/>
			</RollDiameter>
			<Brand>
				<xsl:value-of select="@Brand"/>
			</Brand>	
			<Unspsc>
			<xsl:value-of select="@Unspsc"/>
			</Unspsc>
			<PklvOneUPC>
			<xsl:value-of select="@PklvOneUPC"/>
			</PklvOneUPC>
			<PklvTwoUPC>
			<xsl:value-of select="@PklvTwoUPC"/>
			</PklvTwoUPC>
			<PklvThreeUPC>
			<xsl:value-of select="@PklvThreeUPC"/>
			</PklvThreeUPC>
			<PklvFourUPC>
			<xsl:value-of select="@PklvFourUPC"/>
			</PklvFourUPC>
			<PklvFiveUPC>
			<xsl:value-of select="@PklvFiveUPC"/>
			</PklvFiveUPC>
			<PromotionCode>
				<xsl:value-of select="@PromotionCode"/>
			</PromotionCode>
			<AdjustedAmount>
				<xsl:value-of select="@AdjustmentAmnt"/>
			</AdjustedAmount>
		</LineItem>
		</xsl:for-each>
	</LineItems>
</B2BInvoice>
 </xsl:template>
</xsl:stylesheet>
