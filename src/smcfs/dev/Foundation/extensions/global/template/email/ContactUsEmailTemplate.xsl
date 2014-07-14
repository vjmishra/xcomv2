<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:lxslt="http://xml.apache.org/xslt"
                version="1.0">
<xsl:template match="/">
<HTML>
<xsl:comment>RECIPIENTS=</xsl:comment>
<xsl:comment>FROM=tester@stercomm.com</xsl:comment>
<xsl:comment>SUBJECT=Contact Us Query</xsl:comment>
<xsl:comment>CONTENT_TYPE=text/html</xsl:comment>
<HEAD>
	<title>Contact Us Query</title>
</HEAD>

<BODY topmargin="0" leftmargin="0">
	<BR/><BR/><font></font><BR/><BR/>
	<font>Contact Us Query
	<BR/><BR/>
	</font>
	<xsl:apply-templates select="ContactUs"/>
</BODY>
</HTML>
</xsl:template>
	<xsl:template match="ContactUs">
	<BR/><BR/><BR/>
	Here are the Details of the User Query.
		<P><font><B>
		Company Name :<xsl:value-of select="@CompName"/><BR/>
		Name :<xsl:value-of select="@FirstName"/><BR/>
		City :<xsl:value-of select="@City"/><BR/>
		State :<xsl:value-of select="@State"/><BR/>
		Postal Code :<xsl:value-of select="@PostalCode"/><BR/>
		Email ID :<xsl:value-of select="@EmailID"/><BR/>
		Subject :<xsl:value-of select="@Subject"/><BR/>
		Questions/Comments :<xsl:value-of select="@Comments"/><BR/></B></font></P>
	</xsl:template>		
</xsl:stylesheet>