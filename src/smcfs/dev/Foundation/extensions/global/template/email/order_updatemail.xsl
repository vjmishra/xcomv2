<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:lxslt="http://xml.apache.org/xslt"
                version="1.0">
<xsl:template match="/">
<HTML>
<xsl:comment>RECIPIENTS=</xsl:comment>
<xsl:comment>FROM=Hemachandra_Gumdlabhotla@stercomm.com</xsl:comment>
<xsl:comment>SUBJECT=Order update details</xsl:comment>
<xsl:comment>CONTENT_TYPE=text/html</xsl:comment>
<HEAD>
	<title>Order Updation</title>
</HEAD>

<BODY topmargin="0" leftmargin="0">
	<BR/><BR/><font></font><BR/><BR/>
	<font>Order Change Details
	<BR/><BR/>
	</font>
	<xsl:apply-templates select="Order"/>
</BODY>
</HTML>
</xsl:template>

<xsl:template match="Order">
<BR/><BR/><BR/><P><font><B>Order Number :<xsl:value-of select="@LegacyOrderNo"/><BR/>Web Confirmation Number :<xsl:value-of select="Extn/@ExtnWebConfNumber"/><BR/>Header Status Code :<xsl:value-of select="@HeaderStatusCode"/></B></font></P>


		<TABLE BORDER="2" WIDTH="50%" CELLSPACING="0"  CELLPADDING="3" borderColor="#e0e0e0">
			<TR>
			<TH BGCOLOR="#cccccc"><b><font size="2">Item ID</font></b></TH>
			<TH BGCOLOR="#cccccc"><b><font size="2">WebLineNo</font></b></TH>
			<TH BGCOLOR="#cccccc"><b><font size="2">Ordered Quantity</font></b></TH>
			<TH BGCOLOR="#cccccc"><b><font size="2">Shipped Quantity</font></b></TH>
			<TH BGCOLOR="#cccccc"><b><font size="2">BackOrdered Quantity</font></b></TH>
			<TH BGCOLOR="#cccccc"><b><font size="2">Line Status</font></b></TH>
			<TH BGCOLOR="#cccccc"><b><font size="2">Ship Node</font></b></TH>
			</TR>
			<xsl:for-each select="OrderLines/OrderLine">
				<TR class="Items">
				<TD align="center"><b><font size="-2"><xsl:value-of select="@ItemId"/></font></b></TD>		
				<TD align="center"><b><font size="-2"><xsl:value-of select="@WebLineNumber"/></font></b></TD>		
				<TD align="center"><b><font size="-2"><xsl:value-of select="@OrderedQty"/></font></b></TD>		
				<TD align="center"><b><font size="-2"><xsl:value-of select="@ShippedQty"/></font></b></TD>		
				<TD align="center"><b><font size="-2"><xsl:value-of select="@BackOrderQty"/></font></b></TD>		
				<TD align="center"><b><font size="-2"><xsl:value-of select="@LineStatus"/></font></b></TD>		
				<TD align="center"><b><font size="-2"><xsl:value-of select="@ShipNode"/></font></b></TD>		
				</TR>
			</xsl:for-each>
		</TABLE>


</xsl:template>		
</xsl:stylesheet>
