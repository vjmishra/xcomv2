<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output indent="yes" />
   <xsl:preserve-space elements="OrderCode" />
   <xsl:preserve-space elements="PriceOverrideFlag"/>
   <xsl:preserve-space elements="WillCall"/>

   <xsl:template match="/">
      <Order>
	 <TransactionStatus>
      	 	<xsl:value-of select="Order/@TransactionStatus"/>
      	 </TransactionStatus>

      	 <TransactionMessage>
      	 	<xsl:value-of select="Order/@TransactionMessage"/>
      	 </TransactionMessage>

         <SourceIndicator>
         
                              
           <xsl:if test="Order/@EntryType ='B2B'">2</xsl:if>
          <xsl:if test="Order/@EntryType ='Web'">1</xsl:if>
          <xsl:if test="Order/@EntryType ='Call Center'">1</xsl:if>
          <xsl:if test="Order/@EntryType ='EDI'">2</xsl:if>
           
         <!-- <xsl:value-of select="Order/@EntryType" />-->
         </SourceIndicator>

         <EnvironmentId>
            <xsl:value-of select="Order/Extn/@ExtnEnvtId" />
         </EnvironmentId>
         
         <CustomerEnvironmentId>
          
            <xsl:value-of select="Order/Extn/@ExtnOrigEnvironmentCode" />            
                                                
         </CustomerEnvironmentId>

         <Company>
            <xsl:value-of select="Order/Extn/@ExtnCompanyId" />
         </Company>

         <WebConfirmationNumber>
            <xsl:value-of select="Order/Extn/@ExtnWebConfNum" />
         </WebConfirmationNumber>

	     <OrderingDivision>
                  
	       <xsl:value-of select="substring-before(Order/Extn/@ExtnOrderDivision,'_')"/>
	           
         </OrderingDivision>

         <LegacyOrderNumber>
            <xsl:value-of select="Order/Extn/@ExtnLegacyOrderNo" />
         </LegacyOrderNumber>

         <GenerationNumber>
            <xsl:value-of select="Order/Extn/@ExtnGenerationNo" />
         </GenerationNumber>

         <LegacyOrderType>
            <xsl:value-of select="Order/Extn/@ExtnLegacyOrderType" />
         </LegacyOrderType>

         <WebHoldFlag>
            <xsl:value-of select="Order/Extn/@ExtnWebHoldFlag" />
         </WebHoldFlag>

         <ShipFromDivision>
                     
            <xsl:value-of select="substring-before(Order/@ShipNode,'_')"/>
      
         </ShipFromDivision>

         <CustomerDivision>
            <xsl:value-of select="substring-before(Order/Extn/@ExtnCustomerDivision,'_')"/>          
         </CustomerDivision>

         <CustomerNumber>
            <xsl:value-of select="Order/Extn/@ExtnCustomerNo" />
         </CustomerNumber>

         <ShipToSuffix>
            <xsl:value-of select="Order/Extn/@ExtnShipToSuffix" />
         </ShipToSuffix>

         <ShipToName>
            <xsl:value-of select="Order/Extn/@ExtnShipToName" />
         </ShipToName>

         <AttentionName>
            <xsl:value-of select="Order/Extn/@ExtnAttentionName" />
         </AttentionName>

         <ShipToAddress1>
            <xsl:value-of select="Order/PersonInfoShipTo/@AddressLine1" />
         </ShipToAddress1>

         <ShipToAddress2>
            <xsl:value-of select="Order/PersonInfoShipTo/@AddressLine2" />
         </ShipToAddress2>

         <ShipToAddress3>
            <xsl:value-of select="Order/PersonInfoShipTo/@AddressLine3" />
         </ShipToAddress3>

         <ShipToCity>
            <xsl:value-of select="Order/PersonInfoShipTo/@City" />
         </ShipToCity>

         <ShipToState>
            <xsl:value-of select="Order/PersonInfoShipTo/@State" />
         </ShipToState>

         <ShipToZIP>
            <xsl:value-of select="Order/PersonInfoShipTo/@ZipCode" />
         </ShipToZIP>

         <ShipToCountryCode>
            <xsl:value-of select="Order/PersonInfoShipTo/@Country" />
         </ShipToCountryCode>

         <BillToSuffix>
            <xsl:value-of select="Order/Extn/@ExtnBillToSuffix" />
         </BillToSuffix>
         
         <BillToName>
            <xsl:value-of select="Order/Extn/@ExtnBillToName" />
         </BillToName>

         <BillToAddress1>
            <xsl:value-of select="Order/PersonInfoBillTo/@AddressLine1" />
         </BillToAddress1>

         <BillToAddress2>
            <xsl:value-of select="Order/PersonInfoBillTo/@AddressLine2" />
         </BillToAddress2>

         <BillToAddress3>
            <xsl:value-of select="Order/PersonInfoBillTo/@AddressLine3" />
         </BillToAddress3>

         <BillToCity>
            <xsl:value-of select="Order/PersonInfoBillTo/@City" />
         </BillToCity>

         <BillToState>
            <xsl:value-of select="Order/PersonInfoBillTo/@State" />
         </BillToState>

         <BillToZIP>
            <xsl:value-of select="Order/PersonInfoBillTo/@ZipCode" />
         </BillToZIP>

         <BillToCountryCode>
            <xsl:value-of select="Order/PersonInfoBillTo/@Country" />
         </BillToCountryCode>

         <CustomerHeaderPONumber>
            <xsl:value-of select="Order/@CustomerPONo" />
         </CustomerHeaderPONumber>

         <OrderCode>
         
             <xsl:if test = "Order/Extn/@ExtnDeliveryHoldFlag = 'Y'">H</xsl:if>
             <xsl:if test = "Order/Extn/@ExtnDeliveryHoldFlag = 'N'" >O</xsl:if>
             
            <!--<xsl:value-of select="Order/Extn/@ExtnDeliveryHoldFlag" />-->
         </OrderCode>

         <ShipComplete>
            <xsl:value-of select="Order/Extn/@ExtnShipComplete" />
         </ShipComplete>

         <WillCall>
         
            <xsl:if test = "Order/Extn/@ExtnWillCall = 'Y'">P</xsl:if>
            <xsl:if test = "Order/Extn/@ExtnWillCall = 'N'" > </xsl:if> 
            <!--<xsl:value-of select="Order/Extn/@ExtnWillCall" />-->
         </WillCall>

         <ShipDate>
            <xsl:value-of select="Order/@ReqDeliveryDate" />
         </ShipDate>

         <HeaderComments>
            <xsl:value-of select="Order/Instructions/Instruction[@InstructionType='HEADER']/@InstructionText" />
         </HeaderComments>

         <OrderedByName> 
            <xsl:value-of select="substring(Order/Extn/@ExtnOrderedByName,0,49)" /> 
         </OrderedByName> 

         <OrderCreateDate>
            <xsl:value-of select="Order/@OrderDate" />
         </OrderCreateDate>

         <OrderSource>
            <xsl:value-of select="Order/Extn/@ExtnSourceType" />
         </OrderSource>

         <HeaderProcessCode>
            <xsl:value-of select="Order/@HeaderProcessCode" />
         </HeaderProcessCode>

         <OrderStatus>
            <!--<xsl:value-of select="Order/@MinOrderStatus" />-->
            <xsl:if test="Order/@MinOrderStatus = '1100.0100'">
               <xsl:value-of select="'100'" />
            </xsl:if>
            <xsl:if test="Order/@MinOrderStatus = '1100.5100'">
               <xsl:value-of select="'150'" />
            </xsl:if> 
            <xsl:if test="Order/@MinOrderStatus = '1100.5150'">
               <xsl:value-of select="'200'" />
            </xsl:if> 
            <xsl:if test="Order/@MinOrderStatus = '1100.5200'">
               <xsl:value-of select="'300'" />
            </xsl:if> 
            <xsl:if test="Order/@MinOrderStatus = '1100.5250'">
               <xsl:value-of select="'400'" />
            </xsl:if> 
            <xsl:if test="Order/@MinOrderStatus = '1100.5300'">
               <xsl:value-of select="'450'" />
            </xsl:if> 
            <xsl:if test="Order/@MinOrderStatus = '1100.5350'">
               <xsl:value-of select="'500'" />
            </xsl:if> 
            <xsl:if test="Order/@MinOrderStatus = '1100.5400'">
               <xsl:value-of select="'600'" />
            </xsl:if> 
            <xsl:if test="Order/@MinOrderStatus = '1100.5450'">
               <xsl:value-of select="'650'" />
            </xsl:if>
            <xsl:if test="Order/@MinOrderStatus = '1100.5500'">
               <xsl:value-of select="'700'" />
            </xsl:if>
            <xsl:if test="Order/@MinOrderStatus = '1100.5550'">
               <xsl:value-of select="'800'" />
            </xsl:if>
            <xsl:if test="Order/@MinOrderStatus = '1100.5600'">
               <xsl:value-of select="'850'" />
            </xsl:if>
            <xsl:if test="Order/@MinOrderStatus = '1100.5700'">
               <xsl:value-of select="'900'" />
            </xsl:if> 
            <xsl:if test="Order/@MinOrderStatus = '1100.5750'">
               <xsl:value-of select="'910'" />
            </xsl:if> 
            <xsl:if test="Order/@MinOrderStatus = '1100.5800'">
               <xsl:value-of select="'920'" />
            </xsl:if> 
            <xsl:if test="Order/@MinOrderStatus = '1100.5850'">
               <xsl:value-of select="'930'" />
            </xsl:if> 
            <xsl:if test="Order/@MinOrderStatus = '1100.5900'">
               <xsl:value-of select="'940'" />
            </xsl:if> 
            <xsl:if test="Order/@MinOrderStatus = '1100.5950'">
               <xsl:value-of select="'950'" />
            </xsl:if> 
            <xsl:if test="Order/@MinOrderStatus = '1100.6000'">
               <xsl:value-of select="'960'" />
            </xsl:if>
            <xsl:if test="Order/@MinOrderStatus = '9000'">
               <xsl:value-of select="'250'" />
            </xsl:if> 
                
         </OrderStatus>

         <OrderStatusComment>
            <xsl:value-of select="Order/Extn/@ExtnOrdStatCom" />
         </OrderStatusComment>

         <CurrencyCode>
            <xsl:value-of select="Order/PriceInfo/@Currency" />
         </CurrencyCode>

         <TotalShippableValue>
            <xsl:value-of select="Order/Extn/@ExtnTotalShipValue" />
         </TotalShippableValue>

         <TotalOrderValue>
            <xsl:value-of select="Order/Extn/@ExtnTotalOrderValue" />
         </TotalOrderValue>

         <OrderSpecialCharges>
            <xsl:value-of select="Order/Extn/@ExtnOrderSpecialCharges" />
         </OrderSpecialCharges>

         <TotalOrderFreight>
            <xsl:value-of select="Order/Extn/@ExtnTotalOrderFreight" />
         </TotalOrderFreight>

         <OrderTax>
            <xsl:value-of select="Order/Extn/@ExtnOrderTax" />
         </OrderTax>
         
         <SystemIdentifier>
            <xsl:value-of select="Order/Extn/@ExtnSystemIdentifier" />
         </SystemIdentifier>
         
         <WebHoldReason>
            <xsl:value-of select="Order/Extn/@ExtnWebHoldReason" />
         </WebHoldReason>

	 <HeaderStatusCode>
            <xsl:value-of select="Order/Extn/@ExtnHeaderStatusCode" />
         </HeaderStatusCode>	 
         
         <AdjustmentAmount>
         <xsl:if test="Order/Extn/@ExtnLegTotOrderAdjustments != '0.00' " >
		<xsl:value-of select="Order/Extn/@ExtnLegTotOrderAdjustments" />
		</xsl:if>
		</AdjustmentAmount>
		
		<AdjustmentAmountProcessCode></AdjustmentAmountProcessCode>
		<AdjustmentAmountStatusCode></AdjustmentAmountStatusCode> 
		
		<OrderHeaderKey> 
		   <xsl:value-of select="Order/@OrderHeaderKey" />
         </OrderHeaderKey>
         
         <OrderLockFlag>
		    <xsl:value-of select="Order/Extn/@ExtnOrderLockFlag" />
        </OrderLockFlag>
        
        <NoBoSplit></NoBoSplit>

         <LineItems>
            <xsl:for-each select="Order/OrderLines/OrderLine">
            <xsl:sort select="Extn/@ExtnWebLineNumber" order="ascending" />
               <LineItem>
               
                  <WebLineNumber>
                     <xsl:value-of select="Extn/@ExtnWebLineNumber" />
                  </WebLineNumber>

                  <LegacyLineNumber>
                     <xsl:value-of select="Extn/@ExtnLegacyLineNumber" />
                  </LegacyLineNumber>

                  <LineType>
                     <xsl:value-of select="@LineType" />
                  </LineType>
                  
                  <LineProcessCode>
                     <xsl:value-of select="@LineProcessCode" />
                  </LineProcessCode>

                 
                  <xsl:choose>
                       	<xsl:when test="@LineType = 'M'">
                       	      <SpecialChargeCode>
                       	             <xsl:value-of select="Item/@ItemID" />
                       	      </SpecialChargeCode>  
                       	      <LegacyProductCode>
                       	             <xsl:value-of select="Item/@ItemID" />
                       	      </LegacyProductCode>
                       	</xsl:when>
                         
                       	<xsl:otherwise>
                       	      <LegacyProductCode>
                       	             <xsl:value-of select="Item/@ItemID" />
                       	      </LegacyProductCode> 
                       	</xsl:otherwise>
                  </xsl:choose>
                     
                 

                  <CustomerProductCode>
                     <xsl:value-of select="Item/@CustomerItem" />
                  </CustomerProductCode>
                  
                   
                 <!-- <xsl:choose> //Commented out as part of fix for JIRA # 1791
                  
                         <xsl:when test='contains(Item/@UnitOfMeasure,"_" )'>
                         
                             <BaseUnitOfMeasure>
                             
                                   <xsl:value-of select="substring-after(Item/@UnitOfMeasure,'_')"/>
                                   
                             </BaseUnitOfMeasure>    
                         
                         </xsl:when>
                         
                         <xsl:otherwise> -->
                         
                             <BaseUnitOfMeasure>
                             
                                   <xsl:value-of select="Item/@UnitOfMeasure" />
                                   
                             </BaseUnitOfMeasure>
                             
                     <!--    </xsl:otherwise>     
                                     
                  
                  </xsl:choose> -->

                 

                 <OrderedQtyInBase> 
                  	  <xsl:choose>
                       	<xsl:when test="Extn/@ExtnBaseOrderedQty = '0'">                        	                      	      
                       	    <xsl:value-of select="@OrderedQty"/>                       	  
                       	</xsl:when>
                       	<xsl:when test="Extn/@ExtnBaseOrderedQty = '0.0'">                        	                      	      
                       	    <xsl:value-of select="@OrderedQty"/>                       	  
                       	</xsl:when>
                       	<xsl:when test="Extn/@ExtnBaseOrderedQty = '0.00'">                        	                      	      
                       	    <xsl:value-of select="@OrderedQty"/>                       	  
                       	</xsl:when>
                       	<xsl:when test="Extn/@ExtnBaseOrderedQty = ''">                        	                      	      
                       	    <xsl:value-of select="@OrderedQty"/>                       	  
                       	</xsl:when>
                       	<xsl:when test="Extn/@ExtnBaseOrderedQty = 'null'">                        	                      	      
                       	    <xsl:value-of select="@OrderedQty"/>                       	  
                       	</xsl:when>                         	
                       	<xsl:otherwise>                       	    
                       	     <xsl:value-of select="Extn/@ExtnBaseOrderedQty"/>                       	  
                       	</xsl:otherwise>
                 	 </xsl:choose>                   
                  </OrderedQtyInBase>

                  <PriceUnitOfMeasure>
                     <xsl:value-of select="Extn/@ExtnPricingUOM" />
                  </PriceUnitOfMeasure>

                  <UnitPrice>
                     <xsl:value-of select="Extn/@ExtnAdjUnitPrice" />
                  </UnitPrice>

                  <LineDescription>
                     <xsl:value-of select="Item/@ItemShortDesc" />
                  </LineDescription>

                  <PriceOverrideFlag>
                  
                      <xsl:if test = "Extn/@ExtnPriceOverrideFlag = 'Y'">P</xsl:if>
                      <xsl:if test = "Extn/@ExtnPriceOverrideFlag = 'N'" > </xsl:if>
                     <!--<xsl:value-of select="Extn/@ExtnPriceOverrideFlag" />-->
                  </PriceOverrideFlag>
                  
                  
                  
                  <!-- <xsl:choose> //Commented out as part of fix for JIRA # 1791
                  
                         <xsl:when test='contains(OrderLineTranQuantity/@TransactionalUOM,"_" )'>
                         
                             <RequestedUnitOfMeasure>
                             
                                   <xsl:value-of select="substring-after(OrderLineTranQuantity/@TransactionalUOM,'_')"/>
                                   
                             </RequestedUnitOfMeasure>    
                         
                         </xsl:when>
                         
                         <xsl:otherwise> -->
                         
                             <RequestedUnitOfMeasure>
                             
                                   <xsl:value-of select="OrderLineTranQuantity/@TransactionalUOM"/>
                                   
                             </RequestedUnitOfMeasure>
                             
                    <!--     </xsl:otherwise>     
                                     
                  
                  </xsl:choose> -->

                 

                  <RequestedOrderQuantity>
                     <xsl:value-of select="OrderLineTranQuantity/@OrderedQty" />
                  </RequestedOrderQuantity>

                  <ShippedQty>
                     <xsl:value-of select="Extn/@ExtnReqShipOrdQty" />
                  </ShippedQty>

                  <BackOrderQty>
                     <xsl:value-of select="Extn/@ExtnReqBackOrdQty" />
                  </BackOrderQty>

                  <CustomerLineNumber>
                     <xsl:value-of select="@CustomerLinePONo" />
                  </CustomerLineNumber>

                  <CustomerLinePONumber>
                     <xsl:value-of select="@CustomerPONo" />
                  </CustomerLinePONumber>

                  <CustomerUserField1>
                     <xsl:value-of select="Extn/@ExtnCustLineField1" />
                  </CustomerUserField1>

                  <CustomerUserField2>
                     <xsl:value-of select="Extn/@ExtnCustLineField2" />
                  </CustomerUserField2>

                  <CustomerUserField3>
                     <xsl:value-of select="Extn/@ExtnCustLineField3" />
                  </CustomerUserField3>

                  <ShipFromBranch>
                     <xsl:value-of select="substring-before(@ShipNode,'_')"/>
                  </ShipFromBranch>

                  <LineNotes>
                     <xsl:value-of select="Instructions/Instruction[@InstructionType='LINE']/@InstructionText" />
                  </LineNotes>

                  <ShippableLineTotal>
                     <xsl:value-of select="Extn/@ExtnLineShippableTotal" />
                  </ShippableLineTotal>
                  
                  <OrderedLineTotal>
                     <xsl:value-of select="Extn/@ExtnLineOrderedTotal" />
                  </OrderedLineTotal>

                                 
                  <ShippableQtyInBase>
                     <xsl:value-of select="Extn/@ExtnShipQtyBase" />
                  </ShippableQtyInBase>
                  
                  <BackOrderQtyInBase>
                     <xsl:value-of select="Extn/@ExtnBackQtyBase" />
                  </BackOrderQtyInBase>
                  
                  <CustLineAccNumber>
                     <xsl:value-of select="Extn/@ExtnCustLineAccNo" />
                  </CustLineAccNumber>
                  
                  <AdjustDollarAmount>
                     <xsl:value-of select="Extn/@ExtnAdjDollarAmt" />
                  </AdjustDollarAmount>
                  
                  <CouponCode>
                     <xsl:value-of select="Awards/Award/@AwardId"/>
                  </CouponCode>
                  
                  <LineStatusCode>
                    <xsl:value-of select="Extn/@ExtnLineStatusCode" />
                  </LineStatusCode>
                  
                  
                  
               </LineItem>
            </xsl:for-each>
         </LineItems>
      </Order>
   </xsl:template>
</xsl:stylesheet>