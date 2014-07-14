<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:emailformatters="com.yantra.pca.email.formatters"
    xmlns:java="java"
    exclude-result-prefixes="java emailformatters">

	<xsl:include href="/template/email/ycd/common/YCD_brand.xsl.sample"/>
	<xsl:include href="/template/email/ycd/common/YCD_personalInfo.xsl.sample"/>
	<xsl:include href="/template/email/ycd/common/YCD_miscUtils.xsl.sample"/>
	<xsl:include href="/template/email/ycd/common/YCD_itemData.xsl.sample"/>
	<xsl:include href="/template/email/ycd/common/YCD_summaryOfCharges.xsl.sample"/>
	<xsl:include href="/template/email/ycd/common/YCD_paymentInfo.xsl.sample"/>

	<xsl:variable name="Brand">
		<xsl:call-template name="BrandName">
			<xsl:with-param name="BrandCode" select="/Order/@EnterpriseCode"/>
		</xsl:call-template>
	</xsl:variable>

	<xsl:variable name="BrandPhoneNumber">
		<xsl:call-template name="BrandPhoneNumber">
			<xsl:with-param name="BrandCode" select="/Order/@EnterpriseCode"/>
		</xsl:call-template>
	</xsl:variable>

	<xsl:template match="/">
		<HTML>
			<xsl:call-template name="applyStyle"/>
			<BODY topmargin="0" leftmargin="0" STYLE="font:normal 10pt Tahoma">
				<xsl:apply-templates select="Order"/>
			</BODY>
		</HTML>
	</xsl:template>

	<xsl:template match="Order">
		<p>Greetings from <xsl:value-of select="$Brand"/>,</p>

		<p>
		The order <xsl:value-of select="@OrderNo"/> placed by <xsl:value-of select="@CustomerFirstName"/><xsl:text disable-output-escaping="yes">&amp;</xsl:text>nbsp;<xsl:value-of select="@CustomerLastName"/> is currently awaiting approval.  We will not start processing this order until it is approved.
		</p>
		<p>
		Order Date:  <xsl:value-of select="emailformatters:TextEmailFormat.formatDate(@OrderDate, 'MMM dd, yyyy')"/>
		</p>
		<p>
		<b>Shipping Address:</b>
		<xsl:apply-templates select="PersonInfoShipTo"/>
		</p>
		<p>
		<b>Items in this Order</b>
		<table class="table" >
			<xsl:call-template name="ItemDataHeader"/>
			<xsl:apply-templates select="OrderLines/OrderLine"/>
		</table>
		</p>
		<p>

		<b>Summary of Charges</b>
		<table class="table">
			<xsl:apply-templates select="OverallTotals[1]"/>
		</table>
		</p><br/>
		<!--<p/>
		<b>Payment Info</b>
		<br/>
		<xsl:apply-templates select="PaymentMethods/PaymentMethod"/>
		-->
		<p>
		Please following this link to approve or reject this order:<br/>
		<a>
			<xsl:attribute name="href">http://www.<xsl:value-of select="$Brand"/>.com/order/<xsl:value-of select="@OrderHeaderKey"/></xsl:attribute>
			<xsl:attribute name="target">_blank</xsl:attribute>
			http://www.<xsl:value-of select="$Brand"/>.com/order/<xsl:value-of select="@OrderHeaderKey"/>
		</a><br/><br/>
		<xsl:call-template name="StandardClosingMessage"><xsl:with-param name="Brand" select="$Brand"/><xsl:with-param name="BrandPhoneNumber" select="$BrandPhoneNumber"/></xsl:call-template>
		</p><br/>
	</xsl:template>

	<xsl:template match="OrderLine">
	    <xsl:variable name="LineQuantity"><xsl:value-of select="./@OrderedQty"/></xsl:variable>
	    <xsl:variable name="_UnitPrice"><xsl:value-of select="format-number(LinePriceInfo/@UnitPrice , '.00')"/></xsl:variable>
	    <xsl:variable name="LineTotal"><xsl:value-of select="format-number($_UnitPrice * $LineQuantity, '.00')"/></xsl:variable>

	    <xsl:call-template name="ItemData">
		<xsl:with-param name="ItemDesc" select="Item/@ItemDesc"/>
		<xsl:with-param name="UnitPrice" select="$_UnitPrice"/>
		<xsl:with-param name="Quantity" select="$LineQuantity"/>
		<xsl:with-param name="TotalPrice" select="$LineTotal"/>
	    </xsl:call-template>
	</xsl:template>

	<xsl:template match="PersonInfoShipTo">
	    <xsl:call-template name="FormatYantraAddress"/>
	</xsl:template>

	<xsl:template match="OverallTotals">
	    <xsl:call-template name="DisplaySummaryOfCharges">
		  <xsl:with-param name="LineSubTotal" select="@LineSubTotal"/>
		  <xsl:with-param name="TotalCharges" select="@GrandCharges"/>
		  <xsl:with-param name="TotalTax" select="@GrandTax"/>
		  <xsl:with-param name="TotalDiscount" select="@GrandDiscount"/>
		  <xsl:with-param name="GrandTotal" select="@GrandTotal"/>
	    </xsl:call-template>
	</xsl:template>

	<xsl:template match="PaymentMethod">
		<xsl:if test="./@PaymentType='CREDIT_CARD'">
			<xsl:call-template name="PaymentTypeCreditCard">
				<xsl:with-param name="CreditCardType" select="./@CreditCardType"/>
				<xsl:with-param name="DisplayCreditCardNo" select="./@DisplayCreditCardNo"/>
			</xsl:call-template>
		</xsl:if>

		<xsl:if test="./@PaymentType='CHECK'">
			<xsl:call-template name="PaymentTypeCheck">
				<xsl:with-param name="CheckNo" select="./@CheckNo"/>
			</xsl:call-template>
		</xsl:if>

		<xsl:if test="./@PaymentType='SVC'">
			<xsl:call-template name="PaymentTypeCheck">
				<xsl:with-param name="PaymentAmount" select="./@PaymentAmount"/>
				<xsl:with-param name="DisplaySvcNo" select="./@DisplaySvcNo"/>
			</xsl:call-template>
		</xsl:if>
		<br/>
	</xsl:template>

</xsl:stylesheet>

