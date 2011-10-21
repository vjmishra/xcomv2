<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output indent="yes" />
		 <xsl:template match="/">
		 <B2BOrderPlaceResponse>
	<CurrentCustomerOrder>
	<LocationId></LocationId>
	<BuyerId></BuyerId>
	<WebConfirmationNumber><xsl:value-of select="Order/Extn/@ExtnWebConfNum"/></WebConfirmationNumber>
	<LiaisonMessageId><xsl:value-of select="Order/Extn/@ExtnMsgHeaderId"/></LiaisonMessageId>
	<OrderingDivision><xsl:value-of select="Order/Extn/@ExtnOrderDivision"/></OrderingDivision>
	<LegacyOrderNumber><xsl:value-of select="Order/Extn/@ExtnLegacyOrderNo"/></LegacyOrderNumber>
	<GenerationNumber><xsl:value-of select="Order/Extn/@ExtnGenerationNo"/></GenerationNumber>
	<LegacyOrderType><xsl:value-of select="Order/Extn/@ExtnLegacyOrderType"/></LegacyOrderType>
	<UpdateFlag></UpdateFlag>
	<ShipFromDivision><xsl:value-of select="Order/Extn/@ExtnLegacyOrderType"/></ShipFromDivision>
	<CustomerDivision><xsl:value-of select="Order/Extn/@ExtnCustomerDivision"/></CustomerDivision>
	<CustomerNumber><xsl:value-of select="Order/Extn/@ExtnCustomerNo"/></CustomerNumber>
	<ShipToSuffix><xsl:value-of select="Order/Extn/@ExtnShipToSuffix"/></ShipToSuffix>
	<EtradingId><xsl:value-of select="Order/Extn/@ExtnETradingID"/></EtradingId>
	<ShipToName><xsl:value-of select="Order/Extn/@ExtnShipToName"/></ShipToName>
	<AttentionName><xsl:value-of select="Order/Extn/@ExtnAttentionName"/></AttentionName>
	<ShipToEmail><xsl:value-of select="Order/PersonInfoShipTo/@EMailID"/></ShipToEmail>
	<ShipToAddress1><xsl:value-of select="Order/PersonInfoShipTo/@AddressLine1"/></ShipToAddress1>
	<ShipToAddress2><xsl:value-of select="Order/PersonInfoShipTo/@AddressLine2"/></ShipToAddress2>
	<ShipToAddress3><xsl:value-of select="Order/PersonInfoShipTo/@AddressLine3"/></ShipToAddress3>
	<ShipToCity><xsl:value-of select="Order/PersonInfoShipTo/@City"/></ShipToCity>
	<ShipToState><xsl:value-of select="Order/PersonInfoShipTo/@State"/></ShipToState>
	<ShipToZIP><xsl:value-of select="Order/PersonInfoShipTo/@ZipCode"/></ShipToZIP>
	<ShipToCountryCode><xsl:value-of select="Order/PersonInfoShipTo/@Country"/></ShipToCountryCode>
	<BillToSuffix><xsl:value-of select="Order/Extn/@ExtnBillToSuffix"/></BillToSuffix>
	<BillToAddress1><xsl:value-of select="Order/PersonInfoBillTo/@AddressLine1"/></BillToAddress1>
	<BillToAddress2><xsl:value-of select="Order/PersonInfoBillTo/@AddressLine2"/></BillToAddress2>
	<BillToAddress3><xsl:value-of select="Order/PersonInfoBillTo/@AddressLine3"/></BillToAddress3>
	<BillToCity><xsl:value-of select="Order/PersonInfoBillTo/@City"/></BillToCity>
	<BillToState><xsl:value-of select="Order/PersonInfoBillTo/@State"/></BillToState>
	<BillToZIP><xsl:value-of select="Order/PersonInfoBillTo/@ZipCode"/></BillToZIP>
	<BillToCountryCode><xsl:value-of select="Order/PersonInfoBillTo/@Country"/></BillToCountryCode>
	<CustomerPO><xsl:value-of select="Order/@CustomerPONo"/></CustomerPO>
	<OrderCode></OrderCode>
	<ShipComplete><xsl:value-of select="Order/Extn/@ExtnShipComplete"/></ShipComplete>
	<WillCall><xsl:value-of select="Order/Extn/@ExtnWillCall"/></WillCall>
	<ShipDate><xsl:value-of select="Order/@ReqDeliveryDate"/></ShipDate>
	<HeaderComments></HeaderComments>
	<OrderedByName><xsl:value-of select="Order/Extn/@ExtnOrderedByName"/></OrderedByName>
	<OrderCreateDate><xsl:value-of select="Order/@OrderDate"/></OrderCreateDate>
	<OrderSource><xsl:value-of select="Order/Extn/@ExtnSourceType"/></OrderSource>
	<HeaderProcessCode></HeaderProcessCode>
	<OrderStatus/>
	<OrderStatusComment><xsl:value-of select="Order/Extn/@ExtnOrdStatCom"/></OrderStatusComment>
	<CurrencyCode><xsl:value-of select="Order/PriceInfo/@Currency"/></CurrencyCode>
	<TotalShippableValue><xsl:value-of select="Order/Extn/@ExtnTotalShipValue"/></TotalShippableValue>
	<TotalOrderValue><xsl:value-of select="Order/Extn/@ExtnTotalOrderValue"/></TotalOrderValue>
	<OrderSpecialCharges><xsl:value-of select="Order/Extn/@ExtnOrderSpecialCharges"/></OrderSpecialCharges>
	<OrderFreight><xsl:value-of select="Order/Extn/@ExtnTotalOrderFreight"/></OrderFreight>
	<OrderTax><xsl:value-of select="Order/Extn/@ExtnOrderTax"/></OrderTax>
	<HeaderStatusCode><xsl:value-of select="Order/Extn/@ExtnHeaderStatusCode"/></HeaderStatusCode>
	<ShipMethod></ShipMethod>
	<LineItems>
	<xsl:for-each select="Order/OrderLines/OrderLine">
		<LineItem>
			<WebLineNumber><xsl:value-of select="Extn/@ExtnWebLineNumber"/></WebLineNumber>
			<LegacyLineNumber><xsl:value-of select="Extn/@ExtnLegacyLineNumber"/></LegacyLineNumber>
			<POLineID></POLineID>
			<LineDistributionNumber></LineDistributionNumber>
			<LineProcessCode></LineProcessCode>
			<LineType><xsl:value-of select="Extn/@ExtnLineType"/></LineType>
			<LegacyProductCode><xsl:value-of select="Item/@ItemID"/></LegacyProductCode>
			<CustomerProductCode><xsl:value-of select="Item/@CustomerItem"/></CustomerProductCode>
			<ManufacturerProductCode><xsl:value-of select="Item/@ManufacturerItem"/></ManufacturerProductCode>
			<MasterProductCode><xsl:value-of select="Item/Extn/@ExtnMpc"/></MasterProductCode>
			<SpecialChargeCode><xsl:value-of select="Item/@ItemID"/></SpecialChargeCode>
			<BaseUnitOfMeasure><xsl:value-of select="Item/@UnitOfMeasure"/></BaseUnitOfMeasure>
			<OrderedQtyInBase><xsl:value-of select="@OrderedQty"/></OrderedQtyInBase>
			<ShippedQtyInBase><xsl:value-of select="Extn/@ExtnShipQtyBase"/></ShippedQtyInBase>
			<BackorderedQtyInBase><xsl:value-of select="Extn/@ExtnBackQtyBase"/></BackorderedQtyInBase>
			<PriceUnitOfMeasure><xsl:value-of select="Extn/@ExtnPricingUOM"/></PriceUnitOfMeasure>
			<UnitPrice><xsl:value-of select="LinePriceInfo/@UnitPrice"/></UnitPrice>
			<LineDescription><xsl:value-of select="Item/@ItemShortDesc"/></LineDescription>
			<PriceOverrideFlag><xsl:value-of select="Extn/@ExtnPriceOverrideFlag"/></PriceOverrideFlag>
			<RequestedUnitOfMeasure><xsl:value-of select="OrderLineTranQuantity/@TransactionalUOM"/></RequestedUnitOfMeasure>
			<RequestedOrderQuantity><xsl:value-of select="OrderLineTranQuantity/@OrderedQty"/></RequestedOrderQuantity>
			<ShippedQty><xsl:value-of select="Extn/@ExtnReqShipOrdQty"/></ShippedQty>
			<BackOrderQty><xsl:value-of select="Extn/@ExtnReqBackOrdQty"/></BackOrderQty>
			<CustomerLineNumber><xsl:value-of select="@CustomerLinePONo"/></CustomerLineNumber>
			<CustomerLinePONumber><xsl:value-of select="@CustomerPONo"/></CustomerLinePONumber>
			<CustomerLineAccountNumber><xsl:value-of select="Extn/@ExtnCustLineAccNo"/></CustomerLineAccountNumber>
			<CustomerUserField1><xsl:value-of select="Extn/@ExtnCustLineField1"/></CustomerUserField1>
			<CustomerUserField2><xsl:value-of select="Extn/@ExtnCustLineField2"/></CustomerUserField2>
			<CustomerUserField3><xsl:value-of select="Extn/@ExtnCustLineField3"/></CustomerUserField3>
			<ShipFromBranch><xsl:value-of select="@ShipNode"/></ShipFromBranch>
			<CouponCode><xsl:value-of select="Awards/Award/@AwardId"/></CouponCode>
			<AdjustmentAmount><xsl:value-of select="Extn/@ExtnAdjDollarAmt"/></AdjustmentAmount>
			<LineNotes></LineNotes>
			<LineSpecialCharges></LineSpecialCharges>
			<LineFreight></LineFreight>
			<LineTax></LineTax>
			<LineTotal></LineTotal>
			<OrderedQtyLineTotal></OrderedQtyLineTotal>
			<LineStatusCode></LineStatusCode>
		</LineItem>
		</xsl:for-each>
	</LineItems>
	</CurrentCustomerOrder>
	<OriginalCustomerOrder>
	<SourceIndicator></SourceIndicator>
	<LiaisonMessageId><xsl:value-of select="Order/XPXRefOrderHdr/@MsgHeaderId"/></LiaisonMessageId>
	<BuyerId><xsl:value-of select="Order/XPXRefOrderHdr/@BuyerID"/></BuyerId>
	<EtradingId><xsl:value-of select="Order/XPXRefOrderHdr/@EtradingID"/></EtradingId>
	<ShipToName><xsl:value-of select="Order/XPXRefOrderHdr/@ShipToName"/></ShipToName>
	<AttentionName><xsl:value-of select="Order/XPXRefOrderHdr/@AttentionName"/></AttentionName>
	<ShipToAddress1><xsl:value-of select="Order/XPXRefOrderHdr/@ShipToAddr1"/></ShipToAddress1>
	<ShipToAddress2></ShipToAddress2>
	<ShipToAddress3></ShipToAddress3>
	<ShipToCity><xsl:value-of select="Order/XPXRefOrderHdr/@ShipToCity"/></ShipToCity>
	<ShipToState><xsl:value-of select="Order/XPXRefOrderHdr/@ShipToState"/></ShipToState>
	<ShipToZIP><xsl:value-of select="Order/XPXRefOrderHdr/@ShipToZip"/></ShipToZIP>
	<ShipToCountryCode></ShipToCountryCode>
	<CustomerPO><xsl:value-of select="Order/@CustomerPONo"/></CustomerPO>
	<ShipDate></ShipDate>
	<HeaderComments><xsl:value-of select="Order/XPXRefOrderHdr/@HeaderComments"/></HeaderComments>
	<OrderedByName/>
	<OrderCreateDate><xsl:value-of select="Order/XPXRefOrderHdr/@OrderDate"/></OrderCreateDate>
	<OrderSource></OrderSource>
	<HeaderProcessCode><xsl:value-of select="Order/XPXRefOrderHdr/@HeaderProcessCode"/></HeaderProcessCode>
	<CurrencyCode><xsl:value-of select="Order/XPXRefOrderHdr/@Currency"/></CurrencyCode>
	<LineItems>
	<xsl:for-each select="Order/XPXRefOrderHdr/XPXRefOrderLineList/XPXRefOrderLine">
		<LineItem>
			<POLineID><xsl:value-of select="@MsgLineId"/></POLineID>
			<LineProcessCode><xsl:value-of select="@LineProcessCode"/></LineProcessCode>
			<LegacyProductCode><xsl:value-of select="@ItemID"/></LegacyProductCode>
			<CustomerProductCode><xsl:value-of select="@CustomerProductCode"/></CustomerProductCode>
			<ManufacturerProductCode><xsl:value-of select="@ManufacturerProductCode"/></ManufacturerProductCode>
			<MasterProductCode><xsl:value-of select="@Mpc"/></MasterProductCode>
			<PriceUnitOfMeasure><xsl:value-of select="@PricingUOM"/></PriceUnitOfMeasure>
			<UnitPrice><xsl:value-of select="@UnitPrice"/></UnitPrice>
			<LineDescription><xsl:value-of select="@ItemDesc"/></LineDescription>
			<RequestedUnitOfMeasure><xsl:value-of select="@TransactionalUOM"/></RequestedUnitOfMeasure>
			<RequestedOrderQuantity><xsl:value-of select="@OrderedQty"/></RequestedOrderQuantity>
			<LineRequestedDeliveryDate><xsl:value-of select="@ReqLineDelvryDate"/></LineRequestedDeliveryDate>
			<CustomerLineNumber><xsl:value-of select="@CustomerLineNO"/></CustomerLineNumber>
			<CustomerLinePONumber><xsl:value-of select="@CustomerLinePoNo"/></CustomerLinePONumber>
			<CustomerLineAccountNumber><xsl:value-of select="@CustomerLineAccntNo"/></CustomerLineAccountNumber>
			<CustomerUserField1><xsl:value-of select="@CustUserField1"/></CustomerUserField1>
			<CustomerUserField2><xsl:value-of select="@CustUserField2"/></CustomerUserField2>
			<CustomerUserField3><xsl:value-of select="@CustUserField3"/></CustomerUserField3>
			<LineNotes><xsl:value-of select="@LineNotes"/></LineNotes>
		</LineItem>
	</xsl:for-each>
	</LineItems>
</OriginalCustomerOrder>
</B2BOrderPlaceResponse>
</xsl:template>
</xsl:stylesheet>