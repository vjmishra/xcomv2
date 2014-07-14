<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output indent="yes" />
		 <xsl:template match="/">
		 <B2BOrderPlaceResponse>
			<CurrentCustomerOrder>

			<OrderResponse>
				<OrderResponseHeader>
					<OrderResponseNumber>
						<ListOfMessageID>
							<MessageID>
								<IDNumber>
									<xsl:value-of select="Order/Extn/@ExtnWebConfNum"/>
								</IDNumber>
							</MessageID>
						</ListOfMessageID>
						<BuyerOrderResponseNumber>
							<xsl:value-of select="Order/@CustomerPONo"/>
						</BuyerOrderResponseNumber>
						<SellerOrderResponseNumber>
							<xsl:value-of select="Order/@TransactionMessage"/>
						</SellerOrderResponseNumber>
					</OrderResponseNumber>
					<OrderResponseIssueDate>
						<xsl:value-of select="Order/@OrderDate"/>
					</OrderResponseIssueDate>
					<OrderResponseDocTypeCoded>
						
					</OrderResponseDocTypeCoded>
					<OrderReference>
						<Reference>
							<RefNum>
								
							</RefNum>
							<RefDate>
								
							</RefDate>
						</Reference>
					</OrderReference>
					<ListOfReferenceCoded>
						<ReferenceCoded>
							<ReferenceTypeCoded>
								
							</ReferenceTypeCoded>
							<ReferenceTypeCodedOther>
								
							</ReferenceTypeCodedOther>
							<PrimaryReference>
								<Reference>
									<RefNum>
										
									</RefNum>
								</Reference>
							</PrimaryReference>
						</ReferenceCoded>
					</ListOfReferenceCoded>
					<Purpose>
						<PurposeCoded>
							
						</PurposeCoded>
					</Purpose>
					<ResponseType>
						<ResponseTypeCoded>
							
						</ResponseTypeCoded>
					</ResponseType>
					<OrderHeaderChanges>
						<OrderHeader>
							<OrderNumber>
								<BuyerOrderNumber>
									<xsl:value-of select="Order/@CustomerPONo"/>
								</BuyerOrderNumber>
							</OrderNumber>
							<OrderIssueDate>
								<xsl:value-of select="Order/@OrderDate"/>
							</OrderIssueDate>
							<Purpose>
								<PurposeCoded>
									
								</PurposeCoded>
							</Purpose>
							<OrderCurrency>
								<Currency>
									<CurrencyCoded>
										<xsl:value-of select="Order/PriceInfo/@Currency"/>
									</CurrencyCoded>
								</Currency>
							</OrderCurrency>
							<OrderLanguage>
								<Language>
									<LanguageCoded>
										
									</LanguageCoded>
								</Language>
							</OrderLanguage>
							<OrderParty>
								<ShipToParty>
									<Party>
										<PartyID>
											<Identifier>
												<Agency>
													<AgencyCoded>
														
													</AgencyCoded>
												</Agency>
												<Ident>
													<xsl:value-of select="Order/Extn/@ExtnETradingID"/>
												</Ident>
											</Identifier>
										</PartyID>
										<NameAddress>
											<Name1>
												<xsl:value-of select="Order/Extn/@ExtnShipToName"/>
											</Name1>
											<Street>
												<xsl:value-of select="Order/PersonInfoShipTo/@AddressLine1"/>
											</Street>
											<StreetSupplement1>
												<xsl:value-of select="Order/PersonInfoShipTo/@AddressLine2"/>
											</StreetSupplement1>
											<StreetSupplement2>
												<xsl:value-of select="Order/PersonInfoShipTo/@AddressLine3"/>
											</StreetSupplement2>
											<PostalCode>
												<xsl:value-of select="Order/PersonInfoShipTo/@ZipCode"/>
											</PostalCode>
											<City>
												<xsl:value-of select="Order/PersonInfoShipTo/@City"/>
											</City>
											<Region>
												<RegionCoded>
													<xsl:value-of select="Order/PersonInfoShipTo/@Country"/>
												</RegionCoded>
											</Region>
										</NameAddress>
										<OrderContact>
											<Contact>
												<ContactName>
													<xsl:value-of select="Order/Extn/@ExtnAttentionName"/>
												</ContactName>
												<ListOfContactNumber>
													<xsl:value-of select="Order/Extn/@ExtnAddnlEmailAddr"/>
												</ListOfContactNumber>
											</Contact>
										</OrderContact>

									</Party>
								</ShipToParty>
							</OrderParty>
							<OrderTermsOfDelivery>
								<TermsOfDelivery>
									<ShipmentMethodOfPaymentCoded>
										
									</ShipmentMethodOfPaymentCoded>
									<Location>
										<LocationQualifierCoded>
											
										</LocationQualifierCoded>
									</Location>
								</TermsOfDelivery>
							</OrderTermsOfDelivery>
							<OrderDates>
								<RequestedDeliveryByDate>
									<xsl:value-of select="Order/@ReqDeliveryDate"/>
								</RequestedDeliveryByDate>
							</OrderDates>

						</OrderHeader>
					</OrderHeaderChanges>
				</OrderResponseHeader>
				<OrderResponseDetail>
					<ListOfOrderResponseItemDetail>
						<xsl:for-each select="Order/OrderLines/OrderLine">
						<OrderResponseItemDetail>
							<OriginalItemDetail>
								<ItemDetail>
									<BaseItemDetail>
										<LineItemNum>
											<SellerLineItemNum>
												<xsl:value-of select="Extn/@ExtnCustLineNo"/>
											</SellerLineItemNum>
										</LineItemNum>

										<LineItemType>
											<LineItemTypecoded>
												<xsl:value-of select="Extn/@ExtnPOLineID"/>
											</LineItemTypecoded>
										</LineItemType>
										<ItemIdentifiers>
											<PartNumbers>
												<SellerPartNumber>
													<PartNumber>
														<PartID>
															<xsl:value-of select="Item/@ItemID"/>
														</PartID>
													</PartNumber>
												</SellerPartNumber>
												<BuyerPartNumber>
													<PartNumber>
														<PartID>
															<xsl:value-of select="Item/@CustomerItem"/>
														</PartID>
													</PartNumber>
												</BuyerPartNumber>
												<ManufacturerPartNumber>
													<PartNumber>
														<PartID>
															<xsl:value-of select="Item/@ManufacturerItem"/>
														</PartID>
													</PartNumber>
												</ManufacturerPartNumber>
												<StandardPartNumber>
													<PartNumber>
														<PartID>
															<xsl:value-of select="Item/@ExtnMpc"/>
														</PartID>
													</PartNumber>
												</StandardPartNumber>
											</PartNumbers>
										</ItemIdentifiers>
										<TotalQuantity>
											<Quantity>
												<QuantityValue>
													<xsl:value-of select="OrderLineTranQuantity/@OrderedQty"/>
												</QuantityValue>
												<UnitOfMeasurement>
													<UOMCoded>
														<xsl:value-of select="Item/@UnitOfMeasure"/>
													</UOMCoded>
												</UnitOfMeasurement>
												
											</Quantity>
										</TotalQuantity>

									</BaseItemDetail>
									<PricingDetail>
										<ListOfPrice>
											<Price>
												<UnitPrice>
													<UnitPriceValue>
														<xsl:value-of select="/@ExtnReqUOMUnitPrice"/>
													</UnitPriceValue>
												</UnitPrice>
											</Price>
										</ListOfPrice>
									</PricingDetail>
								</ItemDetail>
							</OriginalItemDetail>
							<ItemDetailResponseCoded>
								<xsl:value-of select="Extn/@ExtnLineStatusCode"/>
							</ItemDetailResponseCoded>
								<ListOfReferenceCoded>
									<ReferenceCoded>
										<ReferenceTypeCoded>
											
										</ReferenceTypeCoded>
										<ReferenceTypeCodedOther>
											
										</ReferenceTypeCodedOther>
										<PrimaryReference>
											<Reference>
												<RefNum>
													
												</RefNum>
											</Reference>
										</PrimaryReference>
									</ReferenceCoded>
									<ReferenceCoded>
										<ReferenceTypeCoded>
											
										</ReferenceTypeCoded>
										<ReferenceTypeCodedOther>
											
										</ReferenceTypeCodedOther>
										<PrimaryReference>
											<Reference>
												<RefNum>
													
												</RefNum>
											</Reference>
										</PrimaryReference>
									</ReferenceCoded>
								</ListOfReferenceCoded>
								<ItemDetailChanges>
									<ItemDetail>
										<BaseItemDetail>
											<LineItemNum>
												<BuyerLineItemNum>
													
												</BuyerLineItemNum>
											</LineItemNum>
											<ItemIdentifiers>
												<PartNumbers>
													<SellerPartNumber>
														<PartNum>
															<PartID>
																<xsl:value-of select="Item/@ItemID"/>
															</PartID>
														</PartNum>
													</SellerPartNumber>
													<BuyerPartNumber>
														<PartNum>
															<PartID>
																<xsl:value-of select="Item/@CustomerItem"/>
															</PartID>
														</PartNum>
													</BuyerPartNumber>
													<ManufacturerPartNumber>
														<PartID>
															<xsl:value-of select="Item/@ManufacturerItem"/>
														</PartID>
													</ManufacturerPartNumber>
												</PartNumbers>
												<ItemDescription>
													<xsl:value-of select="Item/@ItemShortDesc"/>
												</ItemDescription>
											</ItemIdentifiers>
											<TotalQuantity>
												<Quantity>
													<QuantityValue>
														<xsl:value-of select="OrderLineTranQuantity/@OrderedQty"/>
													</QuantityValue>
													<UnitOfMeasurement>
														<UOMCoded>
															<xsl:value-of select="OrderLineTranQuantity/@TransactionalUOM"/>
														</UOMCoded>
													</UnitOfMeasurement>
												</Quantity>
											</TotalQuantity>
											<ListOfItemReferences>
												<ListOfReferenceCoded>
													<ReferenceCoded>
														<ReferenceTypeCoded>
															
														</ReferenceTypeCoded>
														<ReferenceTypeCodedOther>
															<xsl:value-of select="/@CustomerLinePONo"/>
														</ReferenceTypeCodedOther>
														<PrimaryReference>
															<Reference>
																<RefNum>
																	<xsl:value-of select="Extn/@ExtnCustLineField1"/>
																</RefNum>
															</Reference>
														</PrimaryReference>
														<SupportingReference>
															<Reference>
																<RefNum>
																	<xsl:value-of select="Extn/@ExtnCustLineField2"/>
																</RefNum>
															</Reference>
														</SupportingReference>
													</ReferenceCoded>
												</ListOfReferenceCoded>
											</ListOfItemReferences>
										</BaseItemDetail>
										<PricingDetail>
											<ListOfPrice>
												<Price>
													<UnitPrice>
														<UnitPriceValue>
															<xsl:value-of select="LinePriceInfo/@UnitPrice"/>
														</UnitPriceValue>
														<UnitOfMeasurement>
															<UOMCoded>
																<xsl:value-of select="OrderLineTranQuantity/@TransactionalUOM"/>
															</UOMCoded>
														</UnitOfMeasurement>
													</UnitPrice>
												</Price>
											</ListOfPrice>
										</PricingDetail>
										<DeliveryDetail>
											<ListOfScheduleLine>
												<ScheduleLine>
													<Quantity>
														<QuantityValue>
															
														</QuantityValue>
														<UnitOfMeasurement>
															<UOMCoded>
																
															</UOMCoded>
														</UnitOfMeasurement>
													</Quantity>
													<RequestedDeliveryDate>
														
													</RequestedDeliveryDate>
												</ScheduleLine>
										</ListOfScheduleLine>
									</DeliveryDetail>
								</ItemDetail>
							</ItemDetailChanges>
							<ListOfStructuredNote>
								<StructuredNote>
									<GeneralNote>
										<xsl:value-of select="Instructions/Instruction/@InstructionText"/>
									</GeneralNote>
								</StructuredNote>
							</ListOfStructuredNote>
						</OrderResponseItemDetail>
						</xsl:for-each>
					</ListOfOrderResponseItemDetail>
				</OrderResponseDetail>
				<OrderResponseSummary>
					<OriginalOrderSummary>
						<OrderSummary>
							<NumberOfLines>
								
							</NumberOfLines>
						</OrderSummary>
					</OriginalOrderSummary>
					<RevisedOrderSummary>
						<OrderSummary>
							<NumberOfLines>
								
							</NumberOfLines>
						</OrderSummary>
					</RevisedOrderSummary>
				</OrderResponseSummary>
			</OrderResponse>
			
			
			</CurrentCustomerOrder>
			<OriginalCustomerOrder>
			
					
			<OrderResponseHeader>
					<OrderResponseNumber>
						<BuyerOrderResponseNumber>
							<xsl:value-of select="Order/XPXRefOrderHdr/@CustomerPONO"/>
						</BuyerOrderResponseNumber>
						<SellerOrderResponseNumber>
							
						</SellerOrderResponseNumber>
					</OrderResponseNumber>
					<OrderResponseIssueDate>
						<xsl:value-of select="Order/XPXRefOrderHdr/@OrderDate"/>
					</OrderResponseIssueDate>
					<OrderResponseDocTypeCoded>
						
					</OrderResponseDocTypeCoded>
					<OrderReference>
						<Reference>
							<RefNum>
								
							</RefNum>
							<RefDate>
								
							</RefDate>
						</Reference>
					</OrderReference>
					<ListOfReferenceCoded>
						<ReferenceCoded>
							<ReferenceTypeCoded>
								
							</ReferenceTypeCoded>
							<ReferenceTypeCodedOther>
								
							</ReferenceTypeCodedOther>
							<PrimaryReference>
								<Reference>
									<RefNum>
										
									</RefNum>
								</Reference>
							</PrimaryReference>
						</ReferenceCoded>
					</ListOfReferenceCoded>
					<Purpose>
						<PurposeCoded>
							
						</PurposeCoded>
					</Purpose>
					<ResponseType>
						<ResponseTypeCoded>
							
						</ResponseTypeCoded>
					</ResponseType>
					<OrderHeaderChanges>
						<OrderHeader>
							<OrderNumber>
								<ListOfMessageID>
								<MessageID>
									<xsl:value-of select="Order/XPXRefOrderHdr/@MsgHeaderId"/>
								</MessageID>
								</ListOfMessageID>
								<BuyerOrderNumber>
									<xsl:value-of select="Order/XPXRefOrderHdr/@CustomerPONO"/>
								</BuyerOrderNumber>
							</OrderNumber>
							<OrderIssueDate>
								<xsl:value-of select="Order/XPXRefOrderHdr/@OrderDate"/>
							</OrderIssueDate>
							<Purpose>
								<PurposeCoded>
									
								</PurposeCoded>
							</Purpose>
							<OrderCurrency>
								<Currency>
									<CurrencyCoded>
										<xsl:value-of select="Order/XPXRefOrderHdr/@Currency"/>
									</CurrencyCoded>
								</Currency>
							</OrderCurrency>
							<OrderLanguage>
								<Language>
									<LanguageCoded>
										
									</LanguageCoded>
								</Language>
							</OrderLanguage>
							<OrderParty>
								<ShipToParty>
									<Party>
										<PartyID>
											<Identifier>
												<Agency>
													<AgencyCoded>
														
													</AgencyCoded>
												</Agency>
												<Ident>
													<xsl:value-of select="Order/XPXRefOrderHdr/@EtradingID"/>
												</Ident>
											</Identifier>
										</PartyID>
										<NameAddress>
											<Name1>
												<xsl:value-of select="Order/XPXRefOrderHdr/@ShipToName"/>
											</Name1>
											<Street>
												<xsl:value-of select="Order/XPXRefOrderHdr/@ShipToAddr1"/>
											</Street>
											<StreetSupplement1>
												<xsl:value-of select="Order/PersonInfoShipTo/@AddressLine2"/>
											</StreetSupplement1>
											<StreetSupplement2>
												<xsl:value-of select="Order/PersonInfoShipTo/@AddressLine3"/>
											</StreetSupplement2>
											<PostalCode>
												<xsl:value-of select="Order/XPXRefOrderHdr/@ShipToZip"/>
											</PostalCode>
											<City>
												<xsl:value-of select="Order/XPXRefOrderHdr/@ShipToCity"/>
											</City>
											<Region>
												<RegionCoded>
													<xsl:value-of select="Order/XPXRefOrderHdr/@ShipToState"/>
												</RegionCoded>
											</Region>
										</NameAddress>
									</Party>
								</ShipToParty>
							</OrderParty>
							<OrderTermsOfDelivery>
								<TermsOfDelivery>
									<ShipmentMethodOfPaymentCoded>
										
									</ShipmentMethodOfPaymentCoded>
									<Location>
										<LocationQualifierCoded>
											
										</LocationQualifierCoded>
									</Location>
								</TermsOfDelivery>
							</OrderTermsOfDelivery>
							<OrderHeaderNote>
								<xsl:value-of select="Order/XPXRefOrderHdr/@HeaderComments"/>
							</OrderHeaderNote>
						</OrderHeader>
					</OrderHeaderChanges>
				</OrderResponseHeader>
				<OrderResponseDetail>
					<ListOfOrderResponseItemDetail>
						<xsl:for-each select="Order/XPXRefOrderHdr/XPXRefOrderLineList/XPXRefOrderLine">
						<OrderResponseItemDetail>
							<ItemDetailResponseCoded>
								<xsl:value-of select="@LineStatusCode"/>
							</ItemDetailResponseCoded>
								<ListOfReferenceCoded>
									<ReferenceCoded>
										<ReferenceTypeCoded>
											
										</ReferenceTypeCoded>
										<ReferenceTypeCodedOther>
											
										</ReferenceTypeCodedOther>
										<PrimaryReference>
											<Reference>
												<RefNum>
													
												</RefNum>
											</Reference>
										</PrimaryReference>
									</ReferenceCoded>
									<ReferenceCoded>
										<ReferenceTypeCoded>
											
										</ReferenceTypeCoded>
										<ReferenceTypeCodedOther>
											
										</ReferenceTypeCodedOther>
										<PrimaryReference>
											<Reference>
												<RefNum>
													
												</RefNum>
											</Reference>
										</PrimaryReference>
									</ReferenceCoded>
								</ListOfReferenceCoded>
								<ItemDetailChanges>
									<ItemDetail>
										<BaseItemDetail>
											<LineItemNum>
												<BuyerLineItemNum>
													<xsl:value-of select="@PrimeLineNo"/>
												</BuyerLineItemNum>
												<SellerLineItemNum>
													<xsl:value-of select="@CustomerLinePONo"/>
												</SellerLineItemNum>
											</LineItemNum>
											<ItemIdentifiers>
												<PartNumbers>
													<SellerPartNumber>
														<PartNum>
															<PartID>
																<xsl:value-of select="@ItemID"/>
															</PartID>
														</PartNum>
													</SellerPartNumber>
													<BuyerPartNumber>
														<PartNum>
															<PartID>
																<xsl:value-of select="@CustomerProductCode"/>
															</PartID>
														</PartNum>
													</BuyerPartNumber>
													<ManufacturerPartNumber>
														<PartID>
															<xsl:value-of select="@ManufacturerProductCode"/>
														</PartID>
													</ManufacturerPartNumber>
												</PartNumbers>
												<ItemDescription>
													<xsl:value-of select="@ItemDesc"/>
												</ItemDescription>
											</ItemIdentifiers>
											<TotalQuantity>
												<Quantity>
													<QuantityValue>
														<xsl:value-of select="@OrderedQty"/>
													</QuantityValue>
													<UnitOfMeasurement>
														<UOMCoded>
															<xsl:value-of select="@TransactionalUOM"/>
														</UOMCoded>
													</UnitOfMeasurement>
												</Quantity>
											</TotalQuantity>
											<ListOfItemReferences>
												<ListOfReferenceCoded>
													<ReferenceCoded>
														<ReferenceTypeCoded>
															
														</ReferenceTypeCoded>
														<ReferenceTypeCodedOther>
															<xsl:value-of select="@CustomerLinePONo"/>
														</ReferenceTypeCodedOther>
														<PrimaryReference>
															<Reference>
																<RefNum>
																	<xsl:value-of select="@CustUserField1"/>
																</RefNum>
															</Reference>
														</PrimaryReference>
														<SupportingReference>
															<Reference>
																<RefNum>
																	<xsl:value-of select="@CustUserField2"/>
																</RefNum>
															</Reference>
														</SupportingReference>
														<OriginalPurchaseOrder>
															<PrimaryReference>
																<Reference>
																	<RefNum>
																		<xsl:value-of select="@CustomerPONo"/>
																	</RefNum>
																</Reference>
															</PrimaryReference>
														</OriginalPurchaseOrder>
														<ReleaseNumber>
															<PrimaryReference>
																<Reference>
																	<RefNum>
																		<xsl:value-of select="@CustUserField1"/>
																	</RefNum>
																</Reference>
															</PrimaryReference>
														</ReleaseNumber>
														<CustomerReferenceNumber>
															<PrimaryReference>
																<Reference>
																	<RefNum>
																		<xsl:value-of select="@CustUserField2"/>
																	</RefNum>
																</Reference>
															</PrimaryReference>
														</CustomerReferenceNumber>
													</ReferenceCoded>
												</ListOfReferenceCoded>
											</ListOfItemReferences>
											<LineItemType>
												<LineItemTypeCode>
													<LineItemTypeCoded>
														<xsl:value-of select="@MsgLineId"/>
													</LineItemTypeCoded>
												</LineItemTypeCode>
											</LineItemType>
										</BaseItemDetail>
										<PricingDetail>
											<ListOfPrice>
												<Price>
													<UnitPrice>
														<UnitPriceValue>
															<xsl:value-of select="@UnitPrice"/>
														</UnitPriceValue>
														<UnitOfMeasurement>
															<UOMCoded>
																<xsl:value-of select="@PricingUOM"/>
															</UOMCoded>
														</UnitOfMeasurement>
														<Currency>
															<CurrencyCoded>
																<xsl:value-of select="@Currency"/>
															</CurrencyCoded>
														</Currency>
													</UnitPrice>
												</Price>
											</ListOfPrice>
										</PricingDetail>
										<DeliveryDetail>
											<ListOfScheduleLine>
												<ScheduleLine>
													<Quantity>
														<QuantityValue>
															
														</QuantityValue>
														<UnitOfMeasurement>
															<UOMCoded>
																
															</UOMCoded>
														</UnitOfMeasurement>
													</Quantity>
													<RequestedDeliveryDate>
														
													</RequestedDeliveryDate>
												</ScheduleLine>
										</ListOfScheduleLine>
									</DeliveryDetail>
									<ListOfStructuredNote>
										<StructuredNote>
											<GeneralNote>
												<NoteID>
													<xsl:value-of select="@CustomerLineAccntNo"/>
												</NoteID>
											</GeneralNote>
										</StructuredNote>
									</ListOfStructuredNote>
								</ItemDetail>
							</ItemDetailChanges>
						</OrderResponseItemDetail>
						</xsl:for-each>
					</ListOfOrderResponseItemDetail>
				</OrderResponseDetail>
				<OrderResponseSummary>
					<OriginalOrderSummary>
						<OrderSummary>
							<NumberOfLines>
								
							</NumberOfLines>
						</OrderSummary>
					</OriginalOrderSummary>
					<RevisedOrderSummary>
						<OrderSummary>
							<NumberOfLines>
								
							</NumberOfLines>
						</OrderSummary>
					</RevisedOrderSummary>
				</OrderResponseSummary>
				
			</OriginalCustomerOrder>
	</B2BOrderPlaceResponse>
	</xsl:template>
</xsl:stylesheet>




























































































































































									
											






























































































