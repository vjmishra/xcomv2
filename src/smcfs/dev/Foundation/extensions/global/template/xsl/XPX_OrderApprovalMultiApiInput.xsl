<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" encoding="UTF-8" indent="no"/>
	<xsl:template match="/">
		<MultiApi>
			<API>
				<xsl:attribute name="Name">getCompleteOrderDetails</xsl:attribute>
				<Input>
					<Order>
						<xsl:attribute name="OrderHeaderKey"><xsl:value-of select="OrderHoldType/Order/@OrderHeaderKey" /></xsl:attribute>
					</Order>
				</Input>
				<Template>
					<Order BrandLogo="" AllocationRuleID="" AuthorizationExpirationDate=""
					    AutoCancelDate="" BillToID="" BuyerOrganizationCode=""
					    CarrierAccountNo="" CarrierServiceCode="" ChainType=""
					    ChargeActualFreightFlag="" CustCustPONo="" CustomerContactID="" CustomerEMailID="" 
					    CustomerFirstName="" CustomerLastName=""
					    CustomerPONo="" DeliveryCode="" Division="" DocumentType=""
					    DraftOrderFlag="" EnteredBy="" EnterpriseCode="" EntryType=""
					    ExchangeType="" FreightTerms="" HasDeliveryLines=""
					    HasProductLines="" HasServiceLines="" HoldFlag="" HoldReasonCode=""
					    MaxOrderStatus="" MaxOrderStatusDesc="" MinOrderStatus=""
					    MinOrderStatusDesc="" NotificationReference="" NotificationType=""
					    NotifyAfterShipmentFlag="" OrderDate="" OrderHeaderKey=""
					    OrderName="" OrderNo="" OrderPurpose="" OrderType="" OriginalTax=""
					    OriginalTotalAmount="" OtherCharges="" PaymentStatus=""
					    PendingTransferIn="" PersonalizeCode="" PriceProgramKey=""
					    PriorityCode="" PriorityNumber="" Purpose="" ReceivingNode=""
					    ReqCancelDate="" ReqDeliveryDate="" ReqShipDate=""
					    ReturnByGiftRecipient="" ReturnOrderHeaderKeyForExchange="" SCAC=""
					    ScacAndService="" ScacAndServiceKey="" SearchCriteria1=""
					    SearchCriteria2="" SellerOrganizationCode="" ShipCompleteFlag=""
					    ShipToID="" SourcingClassification="" Status="" TaxExemptFlag=""
					    TaxExemptionCertificate="" TaxJurisdiction="" TaxPayerId="" TeamCode=""
					    TermsCode="" TotalAdjustmentAmount="" VendorID="" isHistory=""
					    Createuserid="" strCustomerAdminEmailList="" strToEmailid="" >
					    <Extn/>
					    <OrderLines>
						<OrderLine AllocationDate="" ApptStatus="" ApptType=""
						    AwaitingDeliveryRequest="" CanAddServiceLines=""
						    CarrierAccountNo="" CarrierServiceCode=""
						    CustomerLinePONo="" CustomerPONo="" DeliveryCode=""
						    DeliveryMethod="" DepartmentCode="" FreightTerms=""
						    FulfillmentType="" HasDeliveryLines="" HasServiceLines=""
						    HoldFlag="" HoldReasonCode="" ImportLicenseExpDate=""
						    ImportLicenseNo="" InvoicedQty="" ItemGroupCode=""
						    KitCode="" LineSeqNo="" LineType="" MaxLineStatus=""
						    MaxLineStatusDesc="" MinLineStatus="" MinLineStatusDesc=""
						    NumberOfWorkOrders="" OpenQty="" OrderHeaderKey=""
						    OrderLineKey="" OrderedQty="" OrigOrderLineKey=""
						    OriginalOrderedQty="" OtherCharges="" PackListType=""
						    PersonalizeCode="" PersonalizeFlag="" PickableFlag=""
						    PrimeLineNo="" PromisedApptEndDate=""
						    PromisedApptStartDate="" Purpose="" ReceivingNode=""
						    RemainingQty="" ReqCancelDate="" ReqDeliveryDate=""
						    ReqShipDate="" ReservationID="" ReservationPool="" SCAC=""
						    ScacAndService="" ScacAndServiceKey="" Segment=""
						    SegmentType="" SerialNo="" ShipNode="" ShipToID=""
						    ShipTogetherNo="" SplitQty="" Status="" StatusQuantity=""
						    SubLineNo="" isHistory="">
						    <Extn/>
						    <Item/>
							<Instructions NumberOfInstructions="" InstructionType="" InstructionText=""/>
							<OrderLineTranQuantity OrderedQty="" TransactionalUOM="" />
						    <LineOverallTotals Charges="" Discount="" ExtendedPrice=""
							LineTotal="" OptionPrice="" PricingQty="" Tax="" UnitPrice=""/>
						    <LinePriceInfo ActualPricingQty="" DiscountPercentage=""
							InvoicedPricingQty="" IsPriceLocked="" LineTotal=""
							ListPrice="" OrderedPricingQty=""
							PricingQtyConversionFactor="" PricingUOM=""
							RetailPrice="" SettledAmount="" SettledQuantity=""
							TaxableFlag="" UnitPrice=""/>
						</OrderLine>
					    </OrderLines>
					    <PersonInfoShipTo AddressLine1="" AddressLine2="" AddressLine3=""
						AddressLine4="" AddressLine5="" AddressLine6=""
						AlternateEmailID="" Beeper="" City="" Company="" Country=""
						DayFaxNo="" DayPhone="" Department="" EMailID="" EveningFaxNo=""
						EveningPhone="" FirstName="" JobTitle="" LastName=""
						MiddleName="" MobilePhone="" OtherPhone="" PersonID=""
						PersonInfoKey="" State="" Suffix="" Title="" ZipCode=""/>
						<Instructions NumberOfInstructions="">
							<Instruction InstructionText="" InstructionType=""/>
						</Instructions>
					    <PersonInfoBillTo AddressLine1="" AddressLine2="" AddressLine3=""
						AddressLine4="" AddressLine5="" AddressLine6=""
						AlternateEmailID="" Beeper="" City="" Company="" Country=""
						DayFaxNo="" DayPhone="" Department="" EMailID="" EveningFaxNo=""
						EveningPhone="" FirstName="" JobTitle="" LastName=""
						MiddleName="" MobilePhone="" OtherPhone="" PersonID=""
						PersonInfoKey="" State="" Suffix="" Title="" ZipCode=""/>
					    <OverallTotals GrandCharges="" GrandDiscount="" GrandTax=""
						GrandTotal="" HdrCharges="" HdrDiscount="" HdrTax="" HdrTotal="" LineSubTotal=""/>
					    <PaymentMethods>
						<PaymentMethod CheckNo="" CheckReference="" CreditCardExpDate=""
						    CreditCardName="" CreditCardNo="" CreditCardType=""
						    CustomerAccountNo="" CustomerPONo="" DisplayCreditCardNo=""
						    DisplayCustomerAccountNo="" DisplayPaymentReference1=""
						    DisplaySvcNo="" FundsAvailable=""
						    GetFundsAvailableUserExitInvoked="" PaymentReference1=""
						    PaymentReference2="" PaymentReference3="" PaymentType="" SvcNo=""/>
					    </PaymentMethods>
					</Order>
				</Template>
			</API>
			<API>
				<xsl:attribute name="Name">getCustomerContactList</xsl:attribute>
				<Input>
					<CustomerContact>
						<xsl:attribute name="CallingOrganizationCode"><xsl:value-of select="OrderHoldType/Order/@EnterpriseCode" /></xsl:attribute>
						<xsl:attribute name="CustomerID"><xsl:value-of select="OrderHoldType/Order/@BillToID" /></xsl:attribute>
						<xsl:attribute name="UserID"><xsl:value-of select="OrderHoldType/@ResolverUserId" /></xsl:attribute>
					</CustomerContact>
				</Input>
				<Template>
					<CustomerContactList>
						<CustomerContact FirstName="" LastName="" EmailID="" UserID="" DayPhone="" CustomerContactID="" ApproverProxyUserId="" />
					</CustomerContactList>
				</Template>
			</API>
			<Input>
				<OrderHoldType>
					<xsl:copy-of select="OrderHoldType/@*" />
				</OrderHoldType>
			</Input>
		</MultiApi>
	</xsl:template>
</xsl:stylesheet>