<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:emailformatters="com.yantra.pca.email.formatters"
    xmlns:java="java"
    exclude-result-prefixes="java emailformatters">

	<xsl:variable name="Brand">
		<xsl:call-template name="BrandName">
			<xsl:with-param name="BrandCode" select="/User/@EnterpriseCode"/>
		</xsl:call-template>
	</xsl:variable>

	<xsl:variable name="BrandPhoneNumber">
		<xsl:call-template name="BrandPhoneNumber">
			<xsl:with-param name="BrandCode" select="/User/@EnterpriseCode"/>
		</xsl:call-template>
	</xsl:variable>

	<xsl:template name="BrandName">
	    <xsl:param name="BrandCode"></xsl:param>
	    <xsl:choose>
		<!-- This is used for website name.  For example, YCDYourWay becomes www.YCDYourWay.com -->
		<xsl:when test="$BrandCode = 'YCD_YWE'">YCDYourWay</xsl:when>
		<xsl:when test="$BrandCode = 'YCD_BC'">YCDBusinessChoice</xsl:when>
		<xsl:otherwise><xsl:value-of select="$BrandCode"/></xsl:otherwise>
	    </xsl:choose>
	</xsl:template>
	
	<xsl:template name="BrandPhoneNumber">
	    <xsl:param name="BrandCode"></xsl:param>
	    <xsl:choose>
		<xsl:when test="$BrandCode = 'YCD_YWE'">1.800.YOURWAY (1.800.xxx.xxxx)</xsl:when>
		<xsl:when test="$BrandCode = 'YCD_BC'">1.800.2CHOOSE (1.800.xxx.xxxx)</xsl:when>
		<xsl:otherwise>877 269-1784</xsl:otherwise>
	    </xsl:choose>
	</xsl:template>

<xsl:template name="applyStyle">
	<xsl:comment>CONTENT_TYPE=text/html; charset=UTF-8</xsl:comment>
	<HEAD>
	    <STYLE TYPE="text/css">

		.table  {
		    padding:0;
		    font-size: 12;
		    font-family: Tahoma;
		    font-weight: normal;
		    color: #000000;
		    width: 100%;
		    border: 1;
		}

		.tablecolumn{
		    padding-left:2px; 
		    padding-right:2px; 
		    padding-top: 0px;
		    padding-bottom: 0px;
		    font-size: 12;
		    vertical-align: top;
		    text-align: left;
		}
		.numerictablecolumn{
		    padding-left:2px; 
		    padding-right:2px; 
		    padding-top: 0px;
		    padding-bottom: 0px;
		    vertical-align: top;
		    text-align: right;
		    font-size: 12;
		}
		.tablecolumnheader {
		    border-left:1px solid buttonhighlight;
		    border-right:1px solid buttonshadow;
		    border-top:1px solid buttonhighlight;
		    border-bottom:1px solid buttonshadow;
		    PADDING-LEFT: 2px;
		    PADDING-RIGHT: 2px;
		    PADDING-top: 0px;
		    PADDING-bottom: 0px;
		    VERTICAL-ALIGN: middle;
		    HORIZONTAL-ALIGN: center;
		    BACKGROUND-COLOR: #e0e0e0;
		    TEXT-ALIGN: left
		}
	    </STYLE>

	</HEAD>
</xsl:template>

<xsl:variable name="subjectLINE">    
		<xsl:value-of select="/User/NotificationPwdEmailSubject/@Subject"/>
	</xsl:variable> 



<xsl:template name="StandardClosingMessage">
	<xsl:param name="Brand"></xsl:param>
	<xsl:param name="BrandPhoneNumber">877 269-1784</xsl:param>
	<xsl:param name="AdditionalClosing" />
		<xsl:if test="(contains($subjectLINE,'User Password Change Notification'))">
				<xsl:text>Your password has been changed.&#160;</xsl:text>
		</xsl:if>
	If we may be of further assistance, please contact us via e-mail at 
	<xsl:element name="a">
		<xsl:attribute name="href">mailto:ebusiness@<xsl:value-of select="$Brand"/>.com</xsl:attribute>
		ebusiness@<xsl:value-of select="$Brand"/>.com
	</xsl:element>
	or by calling <xsl:value-of select="$BrandPhoneNumber"/>. 
	<xsl:copy-of select="$AdditionalClosing" />
</xsl:template>

<xsl:variable name="envName">    
		<xsl:value-of select="/User/NotificationENV/@environment"/>
	</xsl:variable> 

	<xsl:variable name="imageURL">    
		<xsl:value-of select="/User/BrandImageURL/@URL"/>
	</xsl:variable> 

	<xsl:template match="/">
	<table width="600" border="0"  cellpadding="2" cellspacing="2" topmargin="0" leftmargin="0" STYLE="font:normal 10pt Tahoma">
	<tr>
	<td height="44">
	<img src="{$imageURL}"	width="216" height="69" alt="xpedx" longdesc="http://www.xpedx.com" />
	</td></tr></table>
		<HTML>
			<xsl:call-template name="applyStyle"/>
			<BODY topmargin="0" leftmargin="0" STYLE="font:normal 10pt Tahoma">
				<xsl:apply-templates select="User"/>
			</BODY>
		</HTML>
	</xsl:template>

	<xsl:template match="User">
	<table width="700" border="0" align="left" cellpadding="2" cellspacing="2" topmargin="0" leftmargin="0" style="border:solid 1px #999;  padding:20px 20px 0px 20px;">
		  <tr>
		    <td style="font-family: Arial, Geneva, sans-serif;font-size:12px; color:#000;">
		<p>Dear <xsl:value-of select="/User/ContactPersonInfo/@EMailID"/>,</p>
		<xsl:if test="/User/User/@RequestId">
		<p>
		You are receiving this notification because we recently received a password reset request for your account. <br/><br/>
		To reset your password, click on the following link: <br/>
			<a><xsl:attribute name="href">
			<xsl:value-of select="/User/URLInfo/@URL" />sfId=<xsl:value-of select="$Brand"/>&amp;requestId=<xsl:value-of select="/User/User/@RequestId" />&amp;userID=<xsl:value-of select="/User/User/@Loginid" /> 
			</xsl:attribute>
			<xsl:attribute name="target">_blank</xsl:attribute>
			<xsl:value-of select="/User/URLInfo/@URL" />sfId=<xsl:value-of select="$Brand"/>&amp;requestId=<xsl:value-of select="/User/User/@RequestId" />&amp;userID=<xsl:value-of select="/User/User/@Loginid" /></a><br/><br/>
			When you click on the link, you will be prompted to create a new password.
		</p>
		</xsl:if>
		<xsl:if test="/User/User/@GeneratedPassword">
		<p>
			Your username ID was recently created for <a><xsl:attribute name="href"><xsl:value-of select="$envName"/><xsl:value-of select="$Brand"/>.com/order</xsl:attribute>
			<xsl:attribute name="target">_blank</xsl:attribute>
			<xsl:value-of select="$envName"/><xsl:value-of select="$Brand"/>.com/order</a>.<xsl:text>&#160;&#160;Please update your password after you sign in by clicking on ‘My Profile’ under the Admin tab.</xsl:text>
		</p>
		<p>Your username is: <b><xsl:value-of select="@DisplayUserID" /></b></p>
		<p>Your initial password is: <b><xsl:value-of select="/User/User/@GeneratedPassword" /></b></p>
		</xsl:if>
		    	<p>
		<xsl:call-template name="StandardClosingMessage"><xsl:with-param name="Brand" select="$Brand"/><xsl:with-param name="BrandPhoneNumber" select="$BrandPhoneNumber"/></xsl:call-template>
		</p>
		
		<p>
		Thank you for your business!
		</p>
		<p>
		Please do not reply to this email.<xsl:text>&#160;&#160;This mailbox is not monitored and you will not receive a response.</xsl:text>
    	</p>
			<p/>
		</td></tr></table>
	</xsl:template>

</xsl:stylesheet>