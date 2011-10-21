<?xml version="1.0" encoding="UTF-8"?>							
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">							
	<xsl:output method="xml" encoding="ISO-8859-1" />	
	<xsl:variable name="date" >
	<xsl:value-of select="/PunchOutOrder/Order/@Createts"/>
</xsl:variable>					
	<xsl:template match="/">						
		<xsl:apply-templates select="PunchOutOrder"/>					
	</xsl:template>						
	<xsl:template match="PunchOutOrder">						
		<cXML version="1.2.014" payloadID="none">					
			<xsl:attribute name="timestamp">				
				<xsl:value-of select="$date"/>			
			</xsl:attribute>				
			<Header>				
				<From>			
					<Credential domain="NetworkId">		
						<Identity>	
							<xsl:value-of select="HeaderInfo/@Identity"/>
						</Identity>	
					</Credential>		
				</From>			
				<To>			
					<Credential domain="NetworkId">		
						<Identity>	
							<xsl:value-of select="HeaderInfo/@ToIdentity"/>
						</Identity>	
					</Credential>		
				</To>			
				<Sender>			
					<Credential domain="DUNS">		
						<Identity>	
							<xsl:value-of select="HeaderInfo/@Identity"/>
						</Identity>	
					</Credential>		
					<UserAgent/>					
				</Sender>						
			</Header>							
			<Message>							
				<PunchOutOrderMessage>						
					<BuyerCookie>					
						<xsl:value-of select="HeaderInfo/@BuyerCookie"/>				
					</BuyerCookie>					
					<PunchOutOrderMessageHeader operationAllowed="edit">					
						<Total>				
							<Money currency="USD">			
								<xsl:choose>		
									<xsl:when test="Order/OverallTotals/@GrandTotal &gt; 0">	
										<xsl:value-of select="Order/OverallTotals/@GrandTotal"/>
									</xsl:when>	
									<xsl:otherwise>0.00</xsl:otherwise>	
								</xsl:choose>
							</Money>	
						</Total>		
					</PunchOutOrderMessageHeader>			
					<xsl:apply-templates select="Order/OrderLines"/>			
				</PunchOutOrderMessage>				
			</Message>					
		</cXML>						
	</xsl:template>	

<xsl:template match="OrderLines">	
	<xsl:apply-templates select="OrderLine" />	
</xsl:template>
	
							
	<xsl:template match="OrderLine">							
		<ItemIn>						
			<xsl:attribute name="quantity"><xsl:value-of select="@OrderedQty"/></xsl:attribute>					
			<xsl:attribute name="lineNumber"><xsl:value-of select="@PrimeLineNo"/></xsl:attribute>					
			<ItemID>					
				<SupplierPartID>				
					<xsl:value-of select="Item/@ItemID"/>			
				</SupplierPartID>
        <SupplierPartAuxiliaryID>				
          <xsl:value-of select="CPN"/>				
        </SupplierPartAuxiliaryID>				
      </ItemID>				
      <ItemDetail>				
        <UnitPrice>				
          <Money currency="USD">				
            <xsl:choose>				
              <xsl:when test="LineOverallTotals/@UnitPrice &gt; 0">				
                <xsl:value-of select="LineOverallTotals/@UnitPrice"/>				
              </xsl:when>				
              <xsl:otherwise>0.00</xsl:otherwise>				
            </xsl:choose>				
          </Money>				
        </UnitPrice>				
        <Description xml:lang="en-US">
          <xsl:value-of select="Item/@ItemDesc"/>
        </Description>
        <UnitOfMeasure>
          <xsl:choose>
            <xsl:when test="UOM">
              <xsl:value-of select="Item/@UnitOfMeasure" />
            </xsl:when>
            <xsl:otherwise>EA</xsl:otherwise>
          </xsl:choose>
        </UnitOfMeasure>
        <Classification domain="UNSPSC">
          <xsl:value-of select="concat(ItemDetails/ClassificationCodes/@UNSPSC,'00')"/>
        </Classification>

      </ItemDetail>
		</ItemIn>
	</xsl:template>	
</xsl:stylesheet>		
