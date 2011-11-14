<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output indent="yes" />

   <xsl:template match="/XPXRefOrderHdrList/XPXRefOrderHdr">
      <B2BOrderPlaceRequest>
	  <RefOrderHdrKey>
            <xsl:value-of select='@RefOrderHdrKey' />
         </RefOrderHdrKey>

	 <IsInvalidEtradingID>
            <xsl:value-of select='@IsInvalidETradingID' />
         </IsInvalidEtradingID>

         <LiaisonMessageId>
            <xsl:value-of select='@MsgHeaderId' />
         </LiaisonMessageId>

         <BuyerId>
            <xsl:value-of select='@BuyerID' />
         </BuyerId>

         <EtradingId>
            <xsl:value-of select='@EtradingID' />
         </EtradingId>

         <ShipToName>
            <xsl:value-of select='@ShipToName' />
         </ShipToName>

         <AttentionName>
            <xsl:value-of select='@AttentionName' />
         </AttentionName>

         <ShipDate>
            <xsl:value-of select='@ShipDate' />
         </ShipDate>

         <ShipToAddress1>
            <xsl:value-of select='@ShipToAddr1' />
         </ShipToAddress1>

         <ShipToAddress2>
            <xsl:value-of select='@ShipToAddr2' />
         </ShipToAddress2>

         <ShipToAddress3>
            <xsl:value-of select='@ShipToAddr3' />
         </ShipToAddress3>

         <ShipToCity>
            <xsl:value-of select='@ShipToCity' />
         </ShipToCity>

         <ShipToState>
            <xsl:value-of select='@ShipToState' />
         </ShipToState>

         <ShipToZIP>
            <xsl:value-of select='@ShipToZip' />
         </ShipToZIP>

         <ShipToCountryCode>
            <xsl:value-of select='@ShipToCountry' />
         </ShipToCountryCode>

         <CustomerPO>
            <xsl:value-of select='@CustomerPONO' />
         </CustomerPO>

         <HeaderComments>
            <xsl:value-of select='@HeaderComments' />
         </HeaderComments>

         <OrderedByName>
            <xsl:value-of select='@OrderedByName' />
         </OrderedByName>

         <OrderCreateDate>
            <xsl:value-of select='@OrderDate' />
         </OrderCreateDate>

         <OrderSource>1</OrderSource>

         <HeaderProcessCode>
            <xsl:value-of select='@HeaderProcessCode' />
         </HeaderProcessCode>

         <CurrencyCode>
            <xsl:value-of select='@Currency' />
         </CurrencyCode>

         <ShipMethod>
            <xsl:value-of select='@ShipMethod' />
         </ShipMethod>

         <LineItems>
            <xsl:for-each select="/XPXRefOrderHdrList/XPXRefOrderHdr/XPXRefOrderLineList/XPXRefOrderLine">
               <LineItem>
                  <POLineID>
                     <xsl:value-of select='@MsgLineId' />
                  </POLineID>

                  <LineProcessCode>
                     <xsl:value-of select='@LineProcessCode' />
                  </LineProcessCode>

                  <LegacyProductCode>
                     <xsl:value-of select='@ItemID' />
                  </LegacyProductCode>

                  <CustomerProductCode>
                     <xsl:value-of select='@CustomerProductCode' />
                  </CustomerProductCode>

                  <ManufacturerProductCode>
                     <xsl:value-of select='@ManufacturerProductCode' />
                  </ManufacturerProductCode>

                  <MasterProductCode>
                     <xsl:value-of select='@Mpc' />
                  </MasterProductCode>

                  <PriceUnitOfMeasure>
                     <xsl:value-of select='@PricingUOM' />
                  </PriceUnitOfMeasure>

                  <UnitPrice>
                     <xsl:value-of select='@UnitPrice' />
                  </UnitPrice>

                  <LineDescription>
                     <xsl:value-of select='@ItemDesc' />
                  </LineDescription>

                  <RequestedUnitOfMeasure>
                     <xsl:value-of select='@TransactionalUOM' />
                  </RequestedUnitOfMeasure>

                  <RequestedOrderQuantity>
                     <xsl:value-of select='@OrderedQty' />
                  </RequestedOrderQuantity>

                  <LineRequestedDeliveryDate>
                     <xsl:value-of select='@ReqLineDelvryDate' />
                  </LineRequestedDeliveryDate>

                  <CustomerLineNumber>
                     <xsl:value-of select='@CustomerLineNO' />
                  </CustomerLineNumber>

                  <CustomerLinePONumber>
                     <xsl:value-of select='@CustomerLinePoNo' />
                  </CustomerLinePONumber>

                  <CustomerLineAccountNumber>
                     <xsl:value-of select='@CustomerLineAccntNo' />
                  </CustomerLineAccountNumber>

                  <CustomerUserField1>
                     <xsl:value-of select='@CustUserField1' />
                  </CustomerUserField1>

                  <CustomerUserField2>
                     <xsl:value-of select='@CustUserField2' />
                  </CustomerUserField2>

                  <CustomerUserField3>
                     <xsl:value-of select='@CustUserField3' />
                  </CustomerUserField3>

                  <LineNotes>
                     <xsl:value-of select='@LineNotes' />
                  </LineNotes>
               </LineItem>
            </xsl:for-each>
         </LineItems>
      </B2BOrderPlaceRequest>
   </xsl:template>
</xsl:stylesheet>