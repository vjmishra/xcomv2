<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output indent="yes" />
		 <xsl:template match="/">
		 	 <xsl:element name="XPXRefOrderHdr">
		 	 	 <xsl:attribute name="SourceIndicator">
            		<xsl:value-of select="//SourceIndicator" />
         		 </xsl:attribute>
		 	 	 <xsl:attribute name="Currency">
            		<xsl:value-of select="//CurrencyCode" />
         		 </xsl:attribute>
         		 <xsl:attribute name="CustomerPONO">
            		<xsl:value-of select="//CustomerPO" />
         		 </xsl:attribute>
         		 <xsl:attribute name="EnterpriseKey">
            		<xsl:value-of select="'xpedx'" />
         		 </xsl:attribute>
         		 <xsl:attribute name="EntryType">
            		<xsl:value-of select="'B2B'" />
         		 </xsl:attribute>
         		 <xsl:attribute name="EtradingID">
            		<xsl:value-of select="//EtradingId" />
         		 </xsl:attribute>
			  	<xsl:attribute name="BuyerID">
            		<xsl:value-of select="//BuyerId" />
         		 </xsl:attribute>
         		 <xsl:attribute name="OrderDate">
            		<xsl:value-of select="//OrderCreateDate" />
         		 </xsl:attribute>
         		 <xsl:attribute name="ShipToAddr1">
            		<xsl:value-of select="//ShipToAddress1" />
         		 </xsl:attribute>
         		 <xsl:attribute name="ShipToAddr2">
            		<xsl:value-of select="//ShipToAddress1" />
         		 </xsl:attribute>
         		 <xsl:attribute name="ShipToAddr3">
            		<xsl:value-of select="//ShipToAddress1" />
         		 </xsl:attribute>
         		 <xsl:attribute name="ShipToCity">
            		<xsl:value-of select="//ShipToCity" />
         		 </xsl:attribute>
         		 <xsl:attribute name="ShipToName">
            		<xsl:value-of select="//ShipToName" />
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
			 	 <xsl:attribute name="MsgHeaderId">
            		<xsl:value-of select="//LiaisonMessageId" />
         		 </xsl:attribute>
         		 <xsl:attribute name="HeaderComments">
            		<xsl:value-of select="//HeaderComments" />
         		 </xsl:attribute>
         		 <xsl:attribute name="AttentionName">
            		<xsl:value-of select="//AttentionName" />
         		 </xsl:attribute>
         		 <xsl:attribute name="ShipDate">
            		<xsl:value-of select="//ShipDate" />
         		 </xsl:attribute>
         		 <xsl:attribute name="OrderedByName">
            		<xsl:value-of select="//OrderedByName" />
         		 </xsl:attribute>
         		 <xsl:attribute name="OrderSource">
            		<xsl:value-of select="//OrderSource" />
         		 </xsl:attribute>
         		 <xsl:attribute name="ShipMethod">
            		<xsl:value-of select="//ShipMethod" />
         		 </xsl:attribute>
         		 <xsl:element name="XPXRefOrderLineList">
         		 	 <xsl:for-each select="//LineItems//LineItem">
         		 	 	<xsl:element name="XPXRefOrderLine">
         		 	 		 <xsl:attribute name="ItemID">
                     			<xsl:value-of select="./LegacyProductCode" />
                  			 </xsl:attribute>
                  			 <xsl:attribute name="LineNotes">
                     			<xsl:value-of select="./LineNotes" />
                  			 </xsl:attribute>                  			 
                  			 <xsl:attribute name="ItemDesc">
                     			<xsl:value-of select="./LineDescription" />
                  			 </xsl:attribute>
                  			 <xsl:attribute name="LineType">
                     			<xsl:value-of select="'L'" />
                  			 </xsl:attribute>
                  			 <xsl:attribute name="OrderedQty">
                     			<xsl:value-of select="./RequestedOrderQuantity" />
                  			 </xsl:attribute>
                  			 <xsl:attribute name="PricingUOM">
                     			<xsl:value-of select="./PriceUnitOfMeasure" />
                  			 </xsl:attribute>
                  			 <xsl:attribute name="TransactionalUOM">
                     			<xsl:value-of select="./RequestedUnitOfMeasure" />
                  			 </xsl:attribute>
                  			 <xsl:attribute name="UnitPrice">
                     			<xsl:value-of select="./UnitPrice" />
                  			 </xsl:attribute>
					 		 <xsl:attribute name="MsgLineId">
            		                 <xsl:value-of select="./POLineID" />
         		             </xsl:attribute>
         		             <xsl:attribute name="CustomerProductCode">
            		                 <xsl:value-of select="./CustomerProductCode" />
         		             </xsl:attribute>
         		             <xsl:attribute name="ManufacturerProductCode">
            		                 <xsl:value-of select="./ManufacturerProductCode" />
         		             </xsl:attribute>
         		             <xsl:attribute name="Mpc">
            		                 <xsl:value-of select="./MasterProductCode" />
         		             </xsl:attribute>
         		             <xsl:attribute name="ReqLineDelvryDate">
            		                 <xsl:value-of select="./LineRequestedDeliveryDate" />
         		             </xsl:attribute>
         		             <xsl:attribute name="CustomerLineNO">
            		                 <xsl:value-of select="./CustomerLineNumber" />
         		             </xsl:attribute>
         		             <xsl:attribute name="CustomerLinePoNo">
            		                 <xsl:value-of select="./CustomerLinePONumber" />
         		             </xsl:attribute>
         		             <xsl:attribute name="CustomerLineAccntNo">
            		                 <xsl:value-of select="./CustomerLineAccountNumber" />
         		             </xsl:attribute>
         		             <xsl:attribute name="CustUserField1">
            		                 <xsl:value-of select="./CustomerUserField1" />
         		             </xsl:attribute>
         		             <xsl:attribute name="CustUserField2">
            		                 <xsl:value-of select="./CustomerUserField2" />
         		             </xsl:attribute>
         		             <xsl:attribute name="CustUserField3">
            		                 <xsl:value-of select="./CustomerUserField3" />
         		             </xsl:attribute>
         		             
                  		</xsl:element>
                  	</xsl:for-each>
                 </xsl:element>
         		          		 
         	 </xsl:element>
		 </xsl:template>
</xsl:stylesheet>