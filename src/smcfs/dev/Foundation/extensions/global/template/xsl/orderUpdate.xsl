<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output indent="yes" />

   <xsl:template match="/">
      <xsl:element name="Order">
         <xsl:if test="((normalize-space(./Order/@IsOrderPlace))='Y')">
         	<xsl:attribute name="IsOrderPlace">
				<xsl:text>Y</xsl:text> 
			</xsl:attribute>
         </xsl:if>
		 
		 <xsl:if test="((normalize-space(./Order/@IsOrderEdit))='Y')">
         	<xsl:attribute name="IsOrderEdit">
				<xsl:text>Y</xsl:text> 
			</xsl:attribute>
         </xsl:if>

         <xsl:attribute name="SourceType">
         	<xsl:value-of select="normalize-space(//OrderSource)" />
         </xsl:attribute>

         <xsl:attribute name="SourceIndicator">
            <xsl:value-of select="normalize-space(//SourceIndicator)" />
         </xsl:attribute>

	 <xsl:attribute name="OrderHeaderKey">
            <xsl:value-of select="normalize-space(//OrderHeaderKey)" />
         </xsl:attribute>

	 <xsl:attribute name="NoBoSplit">
            <xsl:value-of select="normalize-space(//NoBoSplit)" />
         </xsl:attribute>

         <xsl:attribute name="EnterpriseCode">
            <xsl:value-of select="'xpedx'" />
         </xsl:attribute>

         <xsl:if test="(normalize-space(//ShipFromDivision) !='')">
            <xsl:attribute name="ShipNode">
               <xsl:value-of select='concat(normalize-space(//ShipFromDivision),"_",//EnvironmentId)' />
            </xsl:attribute>
         </xsl:if>

         <xsl:attribute name="CustomerPONo">
            <xsl:value-of select="normalize-space(//CustomerHeaderPONumber)" />
         </xsl:attribute>

         <xsl:attribute name="ReqDeliveryDate">
            <xsl:value-of select="normalize-space(//ShipDate)" />
         </xsl:attribute>
		
	 <xsl:if test="(normalize-space(//HeaderProcessCode)='A')" >
	    <xsl:attribute name="OrderDate">
                <xsl:value-of select="normalize-space(//OrderCreateDate)" />
            </xsl:attribute>
         </xsl:if>

         <xsl:attribute name="HeaderProcessCode">
            <xsl:value-of select="normalize-space(//HeaderProcessCode)" />
         </xsl:attribute>

         <xsl:attribute name="OrderStatus">
            <xsl:if test="((normalize-space(//OrderStatus))=100)">
               <xsl:value-of select="'1100.0100'" />
            </xsl:if>

            <xsl:if test="((normalize-space(//OrderStatus))=150)">
               <xsl:value-of select="'1100.5100'" />
            </xsl:if>

            <xsl:if test="((normalize-space(//OrderStatus))=200)">
               <xsl:value-of select="'1100.5150'" />
            </xsl:if>

            <xsl:if test="((normalize-space(//OrderStatus))=300)">
               <xsl:value-of select="'1100.5200'" />
            </xsl:if>

            <xsl:if test="((normalize-space(//OrderStatus))=400)">
               <xsl:value-of select="'1100.5250'" />
            </xsl:if>

            <xsl:if test="((normalize-space(//OrderStatus))=450)">
               <xsl:value-of select="'1100.5300'" />
            </xsl:if>

            <xsl:if test="((normalize-space(//OrderStatus))=500)">
               <xsl:value-of select="'1100.5350'" />
            </xsl:if>

            <xsl:if test="((normalize-space(//OrderStatus))=600)">
               <xsl:value-of select="'1100.5400'" />
            </xsl:if>

            <xsl:if test="((normalize-space(//OrderStatus))=650)">
               <xsl:value-of select="'1100.5450'" />
            </xsl:if>

            <xsl:if test="((normalize-space(//OrderStatus))=700)">
               <xsl:value-of select="'1100.5500'" />
            </xsl:if>

            <xsl:if test="((normalize-space(//OrderStatus))=800)">
               <xsl:value-of select="'1100.5550'" />
            </xsl:if>

            <xsl:if test="((normalize-space(//OrderStatus))=850)">
               <xsl:value-of select="'1100.5600'" />
            </xsl:if>

            <xsl:if test="((normalize-space(//OrderStatus))=900)">
               <xsl:value-of select="'1100.5700'" />
            </xsl:if>

            <xsl:if test="((normalize-space(//OrderStatus))=910)">
               <xsl:value-of select="'1100.5750'" />
            </xsl:if>

            <xsl:if test="((normalize-space(//OrderStatus))=920)">
               <xsl:value-of select="'1100.5800'" />
            </xsl:if>

            <xsl:if test="((normalize-space(//OrderStatus))=930)">
               <xsl:value-of select="'1100.5850'" />
            </xsl:if>

            <xsl:if test="((normalize-space(//OrderStatus))=940)">
               <xsl:value-of select="'1100.5900'" />
            </xsl:if>

            <xsl:if test="((normalize-space(//OrderStatus))=950)">
               <xsl:value-of select="'1100.5950'" />
            </xsl:if>

            <xsl:if test="((normalize-space(//OrderStatus))=960)">
               <xsl:value-of select="'1100.6000'" />
            </xsl:if>

            <xsl:if test="((normalize-space(//OrderStatus))=250)">
               <xsl:value-of select="'9000'" />
            </xsl:if>
         </xsl:attribute>
		
		 <!-- Begin - Changes made by Mitesh Parikh for JIRA 3248 -->         
         <xsl:if test="((string-length(normalize-space(//HeaderComments)) &gt; 0) or ((string-length(normalize-space(//HeaderComments)) = 0) and (normalize-space(//HeaderProcessCode) = 'C' or normalize-space(//HeaderProcessCode) = 'D')))"> 
            <xsl:element name="Instructions">
               <xsl:element name="Instruction">
                  <xsl:attribute name="InstructionType">HEADER</xsl:attribute>

                  <xsl:attribute name="InstructionText">
                     <xsl:value-of select="normalize-space(//HeaderComments)" />
                  </xsl:attribute>
               </xsl:element>
            </xsl:element>
         </xsl:if>
         <!--End - Changes made by Mitesh Parikh for JIRA 3248 -->

         <xsl:element name="Extn">
            <xsl:attribute name="ExtnEnvtId">
               <xsl:value-of select="normalize-space(//EnvironmentId)" />
            </xsl:attribute>

            <xsl:attribute name="ExtnHeaderStatusCode">
               <xsl:value-of select="normalize-space(//HeaderStatusCode)" />
            </xsl:attribute>

            <xsl:attribute name="ExtnBillToSuffix">
               <xsl:value-of select="normalize-space(//BillToSuffix)" />
            </xsl:attribute>

            <xsl:attribute name="ExtnCompanyId">
               <xsl:value-of select="normalize-space(//Company)" />
            </xsl:attribute>

            <xsl:attribute name="ExtnWebConfNum">
               <xsl:value-of select="normalize-space(//WebConfirmationNumber)" />
            </xsl:attribute>

            <xsl:if test="(normalize-space(//OrderingDivision) !='')">
               <xsl:attribute name="ExtnOrderDivision">
                  <xsl:value-of select='concat(normalize-space(//OrderingDivision),"_",//EnvironmentId)' />
               </xsl:attribute>
            </xsl:if>

            <xsl:attribute name="ExtnLegacyOrderNo">
               <xsl:value-of select="normalize-space(//LegacyOrderNumber)" />
            </xsl:attribute>

            <xsl:attribute name="ExtnGenerationNo">
               <xsl:value-of select="normalize-space(//GenerationNumber)" />
            </xsl:attribute>

            <xsl:attribute name="ExtnLegacyOrderType">
               <xsl:value-of select="normalize-space(//LegacyOrderType)" />
            </xsl:attribute>

            <xsl:attribute name="UpdateFlag">
               <xsl:value-of select="normalize-space(//UpdateFlag)" />
            </xsl:attribute>

            <xsl:if test="(normalize-space(//CustomerDivision) !='')">
               <xsl:attribute name="ExtnCustomerDivision">
                  <xsl:value-of select='concat(normalize-space(//CustomerDivision),"_",//EnvironmentId)' />
               </xsl:attribute>
            </xsl:if>

            <xsl:attribute name="ExtnCustomerNo">
               <xsl:value-of select="normalize-space(//CustomerNumber)" />
            </xsl:attribute>

            <xsl:attribute name="ExtnShipToSuffix">
               <xsl:value-of select="normalize-space(//ShipToSuffix)" />
            </xsl:attribute>

            <xsl:attribute name="ExtnShipToName">
               <xsl:value-of select="normalize-space(//ShipToName)" />
            </xsl:attribute>

            <xsl:attribute name="ExtnAttentionName">
               <xsl:value-of select="normalize-space(//AttentionName)" />
            </xsl:attribute>

            <xsl:attribute name="ExtnShipComplete">
               <xsl:value-of select="normalize-space(//ShipComplete)" />
            </xsl:attribute>

            <xsl:attribute name="ExtnWillCall">
            <xsl:if test="(normalize-space(//WillCall) = 'P')" >
               <xsl:value-of select="'Y'" />
             </xsl:if>
             <xsl:if test="(normalize-space(//WillCall) != 'P')" >
               <xsl:value-of select="'N'" />
             </xsl:if>
            </xsl:attribute>
			
			<xsl:attribute name="ExtnWebHoldFlag">
               <xsl:value-of select="normalize-space(//WebHoldFlag)" />
            </xsl:attribute>

            <xsl:attribute name="ExtnOrderedByName">
               <xsl:value-of select="normalize-space(//OrderedByName)" />
            </xsl:attribute>

            <xsl:attribute name="ExtnTotalShipValue">
               <xsl:value-of select="normalize-space(//TotalShippableValue)" />
            </xsl:attribute>

            <xsl:attribute name="ExtnTotalOrderValue">
               <xsl:value-of select="normalize-space(//TotalOrderValue)" />
            </xsl:attribute>

            <xsl:attribute name="ExtnOrderSpecialCharges">
               <xsl:value-of select="normalize-space(//OrderSpecialCharges)" />
            </xsl:attribute>

            <xsl:attribute name="ExtnTotalOrderFreight"> 
            <xsl:choose> 
                    <xsl:when test="((normalize-space(//OrderStatus) = 900) or (normalize-space(//OrderStatus) = 910) or normalize-space(//OrderStatus) = 950)"> 
                                           <xsl:value-of select="normalize-space(//TotalOrderFreight)" /> 
                                </xsl:when> 
                                <xsl:otherwise>0.0</xsl:otherwise> 
                                </xsl:choose> 
            </xsl:attribute> 

            <xsl:attribute name="ExtnOrdStatCom">
               <xsl:value-of select="normalize-space(//OrderStatusComment)" />
            </xsl:attribute>

           <xsl:attribute name="ExtnOrderTax"> 
               <xsl:choose> 
                                        <xsl:when test="((normalize-space(//OrderStatus) = 900) or (normalize-space(//OrderStatus) = 910) or normalize-space(//OrderStatus) = 950)"> 
                                           <xsl:value-of select="normalize-space(//OrderTax)" /> 
                                        </xsl:when> 
                                        <xsl:otherwise>0.0</xsl:otherwise> 
                                </xsl:choose>               
            </xsl:attribute> 

            <xsl:attribute name="ExtnSourceType">
               <xsl:value-of select="normalize-space(//OrderSource)" />
            </xsl:attribute>
            
            <xsl:attribute name="ExtnLegTotOrderAdjustments">
               <xsl:value-of select="normalize-space(//AdjustmentAmount)" />
            </xsl:attribute>

			<xsl:attribute name="ExtnOrderLockFlag">
				 <xsl:choose>
						<xsl:when test="( (normalize-space(//OrderLockFlag)='Y') or  (normalize-space(//OrderLockFlag)='y') )">
						   <xsl:value-of select="'Y'" />
						</xsl:when>

						<xsl:otherwise>
						   <xsl:value-of select="'N'" />
						</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
            
         </xsl:element>

<!-- order Extn end -->
         <xsl:if test="(((normalize-space(//ShipToAddress1))!='') or ((normalize-space(//ShipToAddress2))!='') or ((normalize-space(//ShipToAddress3))!='') or ((normalize-space(//ShipToCity))!='') or ((normalize-space(//ShipToState))!='') or ((normalize-space(//ShipTozip))!='') or ((normalize-space(//ShipToCountryCode))!=''))">
            <xsl:element name="PersonInfoShipTo">
               <xsl:attribute name="AddressLine1">
                  <xsl:value-of select="normalize-space(//ShipToAddress1)" />
               </xsl:attribute>

               <xsl:attribute name="AddressLine2">
                  <xsl:value-of select="normalize-space(//ShipToAddress2)" />
               </xsl:attribute>

               <xsl:attribute name="AddressLine3">
                  <xsl:value-of select="normalize-space(//ShipToAddress3)" />
               </xsl:attribute>

               <xsl:attribute name="City">
                  <xsl:value-of select="normalize-space(//ShipToCity)" />
               </xsl:attribute>

               <xsl:attribute name="State">
                  <xsl:value-of select="normalize-space(//ShipToState)" />
               </xsl:attribute>

               <xsl:attribute name="ZipCode">
                  <xsl:value-of select="normalize-space(//ShipToZIP)" />
               </xsl:attribute>

               <xsl:attribute name="Country">
                  <xsl:value-of select="normalize-space(//ShipToCountryCode)" />
               </xsl:attribute>
            </xsl:element>
         </xsl:if>

         <xsl:if test="(((normalize-space(//BillToAddress1))!='') or ((normalize-space(//BillToAddress2))!='') or ((normalize-space(//BillToAddress3))!='') or ((normalize-space(//BillToCity))!='') or ((normalize-space(//BillToState))!='') or ((normalize-space(//BillToZIP))!='') or ((normalize-space(//BillToCountryCode))!=''))">
            <xsl:element name="PersonInfoBillTo">
               <xsl:attribute name="AddressLine1">
                  <xsl:value-of select="normalize-space(//BillToAddress1)" />
               </xsl:attribute>

               <xsl:attribute name="AddressLine2">
                  <xsl:value-of select="normalize-space(//BillToAddress2)" />
               </xsl:attribute>

               <xsl:attribute name="AddressLine3">
                  <xsl:value-of select="normalize-space(//BillToAddress3)" />
               </xsl:attribute>

               <xsl:attribute name="City">
                  <xsl:value-of select="normalize-space(//BillToCity)" />
               </xsl:attribute>

               <xsl:attribute name="State">
                  <xsl:value-of select="normalize-space(//BillToState)" />
               </xsl:attribute>

               <xsl:attribute name="ZipCode">
                  <xsl:value-of select="normalize-space(//BillToZIP)" />
               </xsl:attribute>

               <xsl:attribute name="Country">
                  <xsl:value-of select="normalize-space(//BillToCountryCode)" />
               </xsl:attribute>
            </xsl:element>
         </xsl:if>

         <xsl:if test="((normalize-space(//CurrencyCode))!='')">
            <xsl:element name="PriceInfo">
               <xsl:attribute name="Currency">
                  <xsl:value-of select="normalize-space(//CurrencyCode)" />
               </xsl:attribute>
            </xsl:element>
         </xsl:if>

         <xsl:element name="OrderLines">
            <xsl:for-each select="//LineItem">
               <xsl:element name="OrderLine">

				<xsl:attribute name="OrderedQty">   
				 <xsl:choose>
                    <xsl:when test="( ( (normalize-space(LineType)='M') and (normalize-space(OrderedQtyInBase)='') ) and (normalize-space(LineProcessCode)='D') )">
                       <xsl:value-of select="'0'" />
                    </xsl:when>

                    <xsl:when test="( ( (normalize-space(LineType)='C') and (normalize-space(OrderedQtyInBase)='') ) and (normalize-space(LineProcessCode)='D') )">
                       <xsl:value-of select="'0'" />
                    </xsl:when>

                    <xsl:when test="( ( (normalize-space(LineType)='M') and ((normalize-space(OrderedQtyInBase)='0') or (normalize-space(OrderedQtyInBase)='')) ) and (normalize-space(LineProcessCode)!='D') )">
                       <xsl:value-of select="'1'" />
                    </xsl:when>

                    <xsl:when test="( ( (normalize-space(LineType)='C') and ((normalize-space(OrderedQtyInBase)='0') or (normalize-space(OrderedQtyInBase)='')) ) and (normalize-space(LineProcessCode)!='D') )">
                       <xsl:value-of select="'1'" />
                    </xsl:when>

                    <xsl:otherwise>
                       <xsl:value-of select="normalize-space((OrderedQtyInBase))" />
                    </xsl:otherwise>
                 </xsl:choose>                          
				</xsl:attribute>
		

                  <xsl:attribute name="CustomerLinePONo">
                     <xsl:value-of select="normalize-space(CustomerLineNumber)" />
                  </xsl:attribute>

                  <xsl:attribute name="CustomerPONo">
                     <xsl:value-of select="normalize-space(CustomerLinePONumber)" />
                  </xsl:attribute>

                  <xsl:if test="(normalize-space(ShipFromBranch)!='')">
                     <xsl:attribute name="ShipNode">
                        <xsl:value-of select='concat(normalize-space(ShipFromBranch),"_",//EnvironmentId)' />
                     </xsl:attribute>
                  </xsl:if>

                  <xsl:attribute name="LineProcessCode">
                     <xsl:value-of select="normalize-space(LineProcessCode)" />
                  </xsl:attribute>

                  <xsl:attribute name="LineType">
                     <xsl:value-of select="normalize-space(LineType)" />
                  </xsl:attribute>

                  <xsl:element name="Extn">

                     <xsl:attribute name="ExtnAdjUnitPrice">
                        <xsl:value-of select="normalize-space(UnitPrice)" />
                     </xsl:attribute>

                     <xsl:attribute name="ExtnWebLineNumber">
                        <xsl:value-of select="normalize-space(WebLineNumber)" />
                     </xsl:attribute>
                     
                     <xsl:attribute name="ExtnUnitPrice">
                        <xsl:value-of select="normalize-space(UnitPrice)" />
                     </xsl:attribute>

                     <xsl:attribute name="ExtnLegacyLineNumber">
                        <xsl:value-of select="normalize-space(LegacyLineNumber)" />
                     </xsl:attribute>

                     <xsl:attribute name="ExtnCustLineField1">
                        <xsl:value-of select="normalize-space(CustomerUserField1)" />
                     </xsl:attribute>

                     <xsl:attribute name="ExtnCustLineField2">
                        <xsl:value-of select="normalize-space(CustomerUserField2)" />
                     </xsl:attribute>

                     <xsl:attribute name="ExtnCustLineField3">
                        <xsl:value-of select="normalize-space(CustomerUserField3)" />
                     </xsl:attribute>

                     <xsl:attribute name="ExtnLineOrderedTotal">
                        <xsl:value-of select="normalize-space(OrderedLineTotal)" />
                     </xsl:attribute>

                     <xsl:attribute name="ExtnLineStatusCode">
                        <xsl:value-of select="normalize-space(LineStatusCode)" />
                     </xsl:attribute>

                     <xsl:attribute name="ExtnPriceOverrideFlag">
					<xsl:choose>
				<xsl:when test="((normalize-space(PriceOverrideFlag)='P') or (normalize-space(PriceOverrideFlag)='p')) or normalize-space(LineType) = 'M'">
					<xsl:value-of select="'Y'" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="'N'" />
				</xsl:otherwise>
			</xsl:choose> 
                     </xsl:attribute>

                     <xsl:attribute name="ExtnPricingUOM">
                        <xsl:value-of select="normalize-space(PriceUnitOfMeasure)" />
                     </xsl:attribute>

                     <xsl:attribute name="ExtnReqShipOrdQty">
                        <xsl:value-of select="normalize-space(ShippedQty)" />
                     </xsl:attribute>

                     <xsl:attribute name="ExtnReqBackOrdQty">
                        <xsl:value-of select="normalize-space(BackOrderQty)" />
                     </xsl:attribute>

                     <xsl:attribute name="ExtnBaseOrderedQty">
                        <xsl:value-of select="normalize-space(OrderedQtyInBase)" />
                     </xsl:attribute>

                     <xsl:attribute name="ExtnShipQtyBase">
                        <xsl:value-of select="normalize-space(ShippableQtyInBase)" />
                     </xsl:attribute>

                     <xsl:attribute name="ExtnBackQtyBase">
                        <xsl:value-of select="normalize-space(BackOrderQtyInBase)" />
                     </xsl:attribute>
                     
                     <xsl:attribute name="ExtnCustLineAccNo">
                        <xsl:value-of select="normalize-space(CustLineAccNumber)" />
                     </xsl:attribute>
                     
                     <xsl:attribute name="ExtnCustLineNo">
                        <xsl:value-of select="normalize-space(CustomerLineNumber)" />
                     </xsl:attribute>

                     <xsl:attribute name="ExtnLineShippableTotal">
                        <xsl:value-of select="normalize-space(ShippableLineTotal)" />
                     </xsl:attribute>
					 
					 <xsl:attribute name="ExtnLegOrderLineAdjustments">
                        <xsl:value-of select="normalize-space(AdjustDollarAmount)" />
                     </xsl:attribute>
                  </xsl:element>

<!-- Extn End -->
                  <xsl:if test="((normalize-space(LegacyProductCode)!='') or (normalize-space(BaseUnitOfMeasure)!='') or (normalize-space(CustomerProductCode)!=''))">
                     <xsl:element name="Item">
                        <xsl:attribute name="ItemID">
                           <xsl:value-of select="normalize-space(LegacyProductCode)" />
                        </xsl:attribute>

                        <xsl:attribute name="CustomerItem">
                           <xsl:value-of select="normalize-space(CustomerProductCode)" />
                        </xsl:attribute>

                        <xsl:attribute name="UnitOfMeasure">
                        <xsl:if test="((normalize-space(LineType))!='M')">
                           <xsl:value-of select="normalize-space(BaseUnitOfMeasure)" />
                        </xsl:if>
                        </xsl:attribute>
                        
			<!-- Begin - Add a condition to ignore description update for Delete line for M & C type lines - Ramesh Iyer for JIRA 4254 -->  
                        <xsl:if test="((normalize-space(LineType) = 'M' or normalize-space(LineType) = 'C' or normalize-space(LineType) = 'T') and (normalize-space(LineProcessCode) != 'D'))"  >
                        <xsl:attribute name="ItemDesc">
                           <xsl:value-of select="normalize-space(LineDescription)" />
                        </xsl:attribute>

                        <xsl:attribute name="ItemShortDesc">
                           <xsl:value-of select="normalize-space(LineDescription)" />
                        </xsl:attribute>
                        </xsl:if>
                     </xsl:element>
                  </xsl:if>
		  <!-- End - Ramesh Iyer for JIRA 4254-->  

<!-- Item End -->
                  <xsl:if test="(((normalize-space(BaseUnitOfMeasure))!='') or ((normalize-space(UnitPrice))!=''))">
                     <xsl:element name="LinePriceInfo">

                        <xsl:attribute name="UnitPrice">
                           <xsl:value-of select="normalize-space(UnitPrice)" />
                        </xsl:attribute>

                        <xsl:if test="normalize-space(UnitPrice) !='' ">
                           <xsl:attribute name="IsPriceLocked">
                              <xsl:value-of select="'Y'" />
                           </xsl:attribute>
                        </xsl:if>
                     </xsl:element>
                  </xsl:if>

<!-- LinePriceInfo End -->
                  <xsl:element name="OrderLineTranQuantity">
					<xsl:attribute name="OrderedQty">
                     <xsl:choose>
                        <xsl:when test="( ( (normalize-space(LineType)='M') and (normalize-space(RequestedOrderQuantity)='') ) and (normalize-space(LineProcessCode)='D') )">
                           <xsl:value-of select="'0'" />
                        </xsl:when>

                        <xsl:when test="( ( (normalize-space(LineType)='C') and (normalize-space(RequestedOrderQuantity)='') ) and (normalize-space(LineProcessCode)='D') )">
                           <xsl:value-of select="'0'" />
                        </xsl:when>

                        <xsl:when test="( ( (normalize-space(LineType)='M') and ((normalize-space(RequestedOrderQuantity)='0') or (normalize-space(RequestedOrderQuantity)='')) ) and (normalize-space(LineProcessCode)!='D') )">
                           <xsl:value-of select="'1'" />
                        </xsl:when>

                        <xsl:when test="( ( (normalize-space(LineType)='C') and ((normalize-space(RequestedOrderQuantity)='0') or (normalize-space(RequestedOrderQuantity)='')) ) and (normalize-space(LineProcessCode)!='D') )">
                           <xsl:value-of select="'1'" />
                        </xsl:when>

                        <xsl:otherwise>
                           <xsl:value-of select="normalize-space((RequestedOrderQuantity))" />
                        </xsl:otherwise>
                     </xsl:choose>
                  </xsl:attribute>
                     <xsl:attribute name="TransactionalUOM">
		        <xsl:if test="((normalize-space(LineType))!='M')">
                        <xsl:value-of select="normalize-space(RequestedUnitOfMeasure)" />
			</xsl:if>
                     </xsl:attribute>
                  </xsl:element>

<!-- OrderLineTranQuantity End  -->
                  <xsl:if test="(normalize-space(LineNotes)!='')">
                     <xsl:element name="Instructions">
                        <xsl:element name="Instruction">
                           <xsl:attribute name="InstructionType">LINE</xsl:attribute>

                           <xsl:attribute name="InstructionText">
                              <xsl:value-of select="normalize-space(LineNotes)" />
                           </xsl:attribute>
                        </xsl:element>
                     </xsl:element>
                  </xsl:if>
               </xsl:element>

<!-- Order Line End  -->
            </xsl:for-each>
         </xsl:element>

<!-- Order Lines End  -->
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>