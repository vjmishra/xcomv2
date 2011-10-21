<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output indent="yes" />
   <xsl:template match="/Customer"> 				
			<xsl:element name="XPXCustomercontactExtn">
				<xsl:attribute  name="AcceptTAndCFlag"><xsl:value-of select="normalize-space(//Extn/@ExtnAcceptTAndCFlag)"/></xsl:attribute>
				<xsl:attribute  name="AddnlEmailAddrs"><xsl:value-of select="normalize-space(//Extn/@ExtnAddnlEmailAddrs)"/></xsl:attribute>
				<xsl:attribute  name="CustomerContactID"><xsl:value-of select="normalize-space(//CustomerContact/@CustomerContactID)"/></xsl:attribute>
				<xsl:attribute  name="POList"><xsl:value-of select="normalize-space(//Extn/@ExtnPOList)"/></xsl:attribute>
				<xsl:attribute  name="TAndCAcceptedOn"><xsl:value-of select="normalize-space(//Extn/@ExtnTAndCAcceptedOn)"/></xsl:attribute>		
			</xsl:element>
			</xsl:template>
			</xsl:stylesheet>
