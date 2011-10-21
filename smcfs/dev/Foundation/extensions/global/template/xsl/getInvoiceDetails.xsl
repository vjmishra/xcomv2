<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output indent="yes" />

   <xsl:template match="/">
      <xsl:element name="XPXInvoiceHdr">
      
	<xsl:attribute name="IsB2BCustomer">
            <xsl:value-of select="normalize-space(//IsB2BCustomer)" />
        </xsl:attribute>

	    <xsl:attribute name="EnvironmentId">
            <xsl:value-of select="//EnvironmentId" />
        </xsl:attribute>
      
         <xsl:attribute name="Company">
            <xsl:value-of select="//Company" />
         </xsl:attribute>

         <xsl:attribute name="WebConfirmationNo">
            <xsl:value-of select="//WebConfirmationNumber" />
         </xsl:attribute>

                 
         <xsl:if test="(//OrderingDivision !='')">
         <xsl:attribute name="OrderingDivision">
         <xsl:value-of select='concat(//OrderingDivision,"_",//EnvironmentId)' />
         </xsl:attribute>
         </xsl:if>  

         <xsl:attribute name="GenerationNo">
            <xsl:value-of select="//GenerationNumber" />
         </xsl:attribute>

         <xsl:attribute name="LegacyOrderType">
            <xsl:value-of select="//LegacyOrderType" />
         </xsl:attribute>

         <xsl:attribute name="LegacyOrderNo">
            <xsl:value-of select="//LegacyOrderNumber" />
         </xsl:attribute>


	 <xsl:attribute name="InvoiceDistFlag">
            <xsl:value-of select="//InvoiceDistributionFlag" />
         </xsl:attribute>


                 
         <xsl:if test="(//ShipFromDivision !='')">

         <xsl:attribute name="ShipFromDivision">
         <xsl:value-of select='concat(//ShipFromDivision,"_",//EnvironmentId)' />
         </xsl:attribute>
         </xsl:if>  


	 <xsl:attribute name="CustomerEnvironmentId">
            <xsl:value-of select="//CustomerEnvironmentID" />
         </xsl:attribute>



                  
         <xsl:if test="(//CustomerDivision !='')">

         <xsl:attribute name="CustomerDivision">
         <xsl:value-of select='concat(//CustomerDivision,"_",//EnvironmentId)' />
         </xsl:attribute>
         </xsl:if>  

         <xsl:attribute name="CustomerNo">
            <xsl:value-of select="//CustomerNumber" />
         </xsl:attribute>

	 <xsl:attribute name="ShipToSuffix">
            <xsl:value-of select="//ShipToSuffix" />
         </xsl:attribute>

	 <xsl:attribute name="ShipToName">
            <xsl:value-of select="//ShipToName" />
         </xsl:attribute>

	  <xsl:attribute name="AttentionName">
            <xsl:value-of select="//AttentionName" />
         </xsl:attribute>

	 <xsl:attribute name="ShipToAddr1">
            <xsl:value-of select="//ShipToAddress1" />
         </xsl:attribute>

	 <xsl:attribute name="ShipToAddr2">
            <xsl:value-of select="//ShipToAddress2" />
         </xsl:attribute>

	 <xsl:attribute name="ShipToAddr3">
            <xsl:value-of select="//ShipToAddress3" />
         </xsl:attribute>

	  <xsl:attribute name="ShipToCity">
            <xsl:value-of select="//ShipToCity" />
         </xsl:attribute>

	 <xsl:attribute name="ShipToState">
            <xsl:value-of select="//ShipToState" />
         </xsl:attribute>

	<xsl:attribute name="ShipToZip">
            <xsl:value-of select="//ShipToZIP" />
         </xsl:attribute>

	 <xsl:attribute name="ShipToCountry">
            <xsl:value-of select="//ShipToCountryCode" />
         </xsl:attribute>

	 <xsl:attribute name="BillToSuffix">
            <xsl:value-of select="//BillToSuffix" />
         </xsl:attribute>

	 <xsl:attribute name="BillToName">
            <xsl:value-of select="//BillToName" />
         </xsl:attribute>

	 <xsl:attribute name="BillToAddr1">
            <xsl:value-of select="//BillToAddress1" />
         </xsl:attribute>

	 <xsl:attribute name="BillToAddr2">
            <xsl:value-of select="//BillToAddress2" />
         </xsl:attribute>

	 <xsl:attribute name="BillToAddr3">
            <xsl:value-of select="//BillToAddress3" />
         </xsl:attribute>

	 <xsl:attribute name="BillToCity">
            <xsl:value-of select="//BillToCity" />
         </xsl:attribute>

	 <xsl:attribute name="BillToCountry">
            <xsl:value-of select="//BillToCountryCode" />
         </xsl:attribute>

	 <xsl:attribute name="BillToState">
            <xsl:value-of select="//BillToState" />
         </xsl:attribute>

	 <xsl:attribute name="BillToZip">
            <xsl:value-of select="//BillToZIP" />
         </xsl:attribute>

	 <xsl:attribute name="CustomerPoNo">
            <xsl:value-of select="//CustomerHeaderPONumber" />
         </xsl:attribute>

	 <xsl:attribute name="ShipDate">
            <xsl:value-of select="//ShipDate" />
         </xsl:attribute>

	 <xsl:attribute name="Instruction">
            <xsl:value-of select="//HeaderComments" />
         </xsl:attribute>

	 <xsl:attribute name="OrderedBy">
            <xsl:value-of select="//OrderedByName" />
         </xsl:attribute>

	 <xsl:attribute name="OrderDate">
            <xsl:value-of select="//OrderCreateDate" />
         </xsl:attribute>

	 <xsl:attribute name="CurrencyCode">
            <xsl:value-of select="//CurrencyCode" />
         </xsl:attribute>

	 <xsl:attribute name="TotalShipableValue">
            <xsl:value-of select="//TotalShippableValue" />
         </xsl:attribute>

	 <xsl:attribute name="TotalOrderValue">
            <xsl:value-of select="//TotalOrderValue" />
         </xsl:attribute>

	 <xsl:attribute name="SpecialCharges">
            <xsl:value-of select="//OrderSpecialCharges" />
         </xsl:attribute>

	 <xsl:attribute name="TotalFreight">
            <xsl:value-of select="//TotalOrderFreight" />
         </xsl:attribute>

	 <xsl:attribute name="TotalTax">
            <xsl:value-of select="//TotalOrderTax" />
         </xsl:attribute>

	 <xsl:attribute name="InvoiceDate">
            <xsl:value-of select="//InvoicedDate" />
         </xsl:attribute>

	  <xsl:attribute name="LDInvoiceNo">
            <xsl:value-of select="//LDInvoiceNumber" />
         </xsl:attribute>

	  <xsl:attribute name="ShipVia">
            <xsl:value-of select="//ShipVia" />
         </xsl:attribute>

	  <xsl:attribute name="CreditTerms">
            <xsl:value-of select="//CreditTerms" />
         </xsl:attribute>

	  <xsl:attribute name="SalesTaxPct">
            <xsl:value-of select="//SalesTaxPercentage" />
         </xsl:attribute>

	  <xsl:attribute name="SalesTaxAmnt">
            <xsl:value-of select="//SalesTaxAmount" />
         </xsl:attribute>

	  <xsl:attribute name="InvoiceTotal">
            <xsl:value-of select="//InvoiceTotal" />
         </xsl:attribute>

	  <xsl:attribute name="TotalDiscountAllowed">
            <xsl:value-of select="//TotalDiscountAllowed" />
         </xsl:attribute>

	  <xsl:attribute name="SCACNumber">
            <xsl:value-of select="//SCACNumber" />
         </xsl:attribute>

	  <xsl:attribute name="CareerName">
            <xsl:value-of select="//CarrierNameDescription" />
         </xsl:attribute>

	  <xsl:attribute name="TermsNetDays">
            <xsl:value-of select="//TermsNetDays" />
         </xsl:attribute>

	  <xsl:attribute name="TermsPercent">
            <xsl:value-of select="//TermsPercent" />
         </xsl:attribute>

	  <xsl:attribute name="CashDiscountDays">
            <xsl:value-of select="//CashDiscountDays" />
         </xsl:attribute>

	  <xsl:attribute name="DueDate">
            <xsl:value-of select="//DueDate" />
         </xsl:attribute>

	 <xsl:attribute name="RemitToDunsNo">
            <xsl:value-of select="//RemitToDUNSNumber" />
         </xsl:attribute>

	  <xsl:attribute name="RemitToName">
            <xsl:value-of select="//RemitToName" />
         </xsl:attribute>

	  <xsl:attribute name="RemitAddr1">
            <xsl:value-of select="//RemitAddress1" />
         </xsl:attribute>

	  <xsl:attribute name="RemitAddr2">
            <xsl:value-of select="//RemitAddress2" />
         </xsl:attribute>

	  <xsl:attribute name="RemitCity">
            <xsl:value-of select="//RemitCity" />
         </xsl:attribute>

	 <xsl:attribute name="RemitState">
            <xsl:value-of select="//RemitState" />
         </xsl:attribute>

	 <xsl:attribute name="RemitZip">
            <xsl:value-of select="//RemitZip" />
         </xsl:attribute>

	
	 

		 <xsl:element name="XPXInvoiceLineList">
			<xsl:for-each select="//LineItem">
				<xsl:element name="XPXInvoiceLine">

					<xsl:attribute name="WebLineNo">
						<xsl:value-of select="WebLineNumber" />
					</xsl:attribute>

					<xsl:attribute name="LegacyLineNo">
						<xsl:value-of select="LegacyLineNumber" />
					 </xsl:attribute>

					
					 <xsl:attribute name="LegacyProductCode">
						<xsl:value-of select="LegacyProductCode" />
					</xsl:attribute>

					<xsl:attribute name="CustomerProductCode">
						<xsl:value-of select="CustomerProductCode" />
					</xsl:attribute>


					<xsl:attribute name="BaseUom">
						<xsl:value-of select="BaseUnitOfMeasure" />
					</xsl:attribute>

					<xsl:if test="not(contains(BaseUnitOfMeasure,'_')) and BaseUnitOfMeasure != '' ">
                    <xsl:attribute name="BaseUom">
                    <xsl:value-of select='concat(//EnvironmentId,"_",BaseUnitOfMeasure)' />
                    </xsl:attribute>
                    </xsl:if> 

					<xsl:attribute name="OrderedQtyInBaseUom">
						<xsl:value-of select="OrderedQtyInBase" />

                    

					</xsl:attribute>

					<xsl:attribute name="ShippedQtyInBaseUom">
						<xsl:value-of select="ShippableQtyInBase" />
					</xsl:attribute>

					<xsl:attribute name="BackorderedQtyInBaseUom">
						<xsl:value-of select="BackOrderQtyInBase" />
					</xsl:attribute>

					<xsl:attribute name="PriceUom">
						<xsl:value-of select="PriceUnitOfMeasure" />
					</xsl:attribute>
					
					<xsl:if test="not(contains(PriceUnitOfMeasure,'_')) and PriceUnitOfMeasure != '' ">
                    <xsl:attribute name="PriceUom">
                    <xsl:value-of select='concat(//EnvironmentId,"_",PriceUnitOfMeasure)' />
                    </xsl:attribute>
                    </xsl:if> 

					<xsl:attribute name="UnitPrice">
						<xsl:value-of select="UnitPrice" />
					</xsl:attribute>

					<xsl:attribute name="LineDescription">
						 <xsl:value-of select="LineDescription" />
					</xsl:attribute>

					<xsl:attribute name="PriceOverrideFlag">
						 <xsl:value-of select="PriceOverrideFlag" />
					</xsl:attribute>

					<xsl:attribute name="ReqUom">
						<xsl:value-of select="RequestedUnitOfMeasure" />
					</xsl:attribute>
					
					<xsl:if test="not(contains(RequestedUnitOfMeasure,'_')) and RequestedUnitOfMeasure != '' ">
                    <xsl:attribute name="ReqUom">
                    <xsl:value-of select='concat(//EnvironmentId,"_",RequestedUnitOfMeasure)' />
                    </xsl:attribute>
                    </xsl:if> 

					<xsl:attribute name="OrderedQtyInReqUom">
						<xsl:value-of select="RequestedOrderQuantity" />
					</xsl:attribute>

					<xsl:attribute name="ShippableQtyInReqUom">
						<xsl:value-of select="RequestedShippedQty" />
					</xsl:attribute>

					<xsl:attribute name="BackorderedQtyInReqUOM">
						<xsl:value-of select="RequestedBackOrderQty" />
					</xsl:attribute>

					<xsl:attribute name="CustomerLineSeqNo">
						<xsl:value-of select="CustomerLineNumber" />
					</xsl:attribute>

					<xsl:attribute name="CustomerLinePoNo">
						<xsl:value-of select="CustomerLinePONumber" />
					</xsl:attribute>

					<xsl:attribute name="CustomerLineAccntNo">
						<xsl:value-of select="CustomerLineAccountNumber" />
					</xsl:attribute>


					<xsl:attribute name="CustUserField1">
						<xsl:value-of select="CustomerLineField1" />
					</xsl:attribute>

					<xsl:attribute name="CustUserField2">
						<xsl:value-of select="CustomerLineField2" />
					</xsl:attribute>

					<xsl:attribute name="CustUserField3">
						<xsl:value-of select="CustomerLineField3" />
					</xsl:attribute>


					<xsl:attribute name="ShipFromBranch">
						<xsl:value-of select="ShipFromBranch" />
					</xsl:attribute>

								
					<xsl:if test="(ShipFromBranch !='')">
                    <xsl:attribute name="ShipFromBranch">
                    <xsl:value-of select='concat(ShipFromBranch,"_",//EnvironmentId)' />
                    </xsl:attribute>
                    </xsl:if>  


					<xsl:attribute name="LineNotes">
						<xsl:value-of select="LineNotes" />
					</xsl:attribute>

					<xsl:attribute name="LineTax">
						<xsl:value-of select="LineTax" />
					</xsl:attribute>

					<xsl:attribute name="LineTotal">
						<xsl:value-of select="LineTotal" />
					</xsl:attribute>

					<xsl:attribute name="LineFreight">
						<xsl:value-of select="LineFreight" />
					</xsl:attribute>

					<xsl:attribute name="ShipQtyLineTotal">
						<xsl:value-of select="ShippableQtyLineTotal" />
					</xsl:attribute>

					<xsl:attribute name="LDOrderNumber">
						<xsl:value-of select="LDOrderNumber" />
					</xsl:attribute>

					<xsl:attribute name="ManufacturerItemNo">
						<xsl:value-of select="ManufacturerItemNumber" />
					</xsl:attribute>

					<xsl:attribute name="UnitWeight">
						<xsl:value-of select="UnitWeight" />
					</xsl:attribute>

					<xsl:attribute name="WeightPerCode">
						<xsl:value-of select="WeightPerCode" />
					</xsl:attribute>

					<xsl:attribute name="ExtendedWeight">
						<xsl:value-of select="ExtendedWeight" />
					</xsl:attribute>

					<xsl:attribute name="LineShipDate">
						<xsl:value-of select="LineShipDate" />
					</xsl:attribute>

					<xsl:attribute name="LineSalesTaxPercent">
						<xsl:value-of select="LineSalesTaxPercentage" />
					</xsl:attribute>

					<xsl:attribute name="PromotionCode">
						<xsl:value-of select="PromotionCode" />
					</xsl:attribute>

					<xsl:attribute name="AdjustmentAmnt">
						<xsl:value-of select="AdjustedAmount" />
					</xsl:attribute>

					

	 </xsl:element>
         	</xsl:for-each>
         
<!-- Order Lines End  -->
      </xsl:element>
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>

