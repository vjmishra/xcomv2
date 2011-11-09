<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output indent="yes" />

   <xsl:template match="/">
   
		<xsl:element name="User">	 
			<xsl:attribute name="Activateflag">
				<xsl:value-of select="//@Activateflag" />
			</xsl:attribute>
			 
			<xsl:attribute name="EnterpriseCode">
				<xsl:value-of select="//@EnterpriseCode" />
			</xsl:attribute>

			<xsl:attribute name="DisplayUserID">
				<xsl:value-of select="//Extn/@ExtnUserNetworkID" />
			</xsl:attribute>       

			<xsl:attribute name="Password">
				<xsl:value-of select="//@Password" />
			</xsl:attribute>

			<xsl:attribute name="Loginid">
				<xsl:value-of select="//Extn/@ExtnUserNetworkID" />
			</xsl:attribute>

			<xsl:attribute name="OrganizationKey">
				<xsl:value-of select="//Customer/@OrganizationCode" />
			</xsl:attribute>

			<xsl:attribute name="Username">
				<xsl:value-of select="//@Username" />
			</xsl:attribute>


			<xsl:attribute name="DataSecurityGroupId">
				<xsl:value-of select="//@DataSecurityGroupId" />
			</xsl:attribute>


			<xsl:if test="(normalize-space(//@Localecode) !='')">
				<xsl:attribute name="Localecode">
					<xsl:value-of select="//@Localecode" />
				</xsl:attribute>
			</xsl:if>

			<xsl:element name="ContactPersonInfo">
			
				<xsl:if test="(normalize-space(//ContactPersonInfo/@Suffix) !='')">
					<xsl:attribute name="Suffix">
						<xsl:value-of select="//ContactPersonInfo/@Suffix" />
					</xsl:attribute>
				</xsl:if>
				
				<xsl:if test="(normalize-space(//ContactPersonInfo/@Title) !='')">
					<xsl:attribute name="Title">
						<xsl:value-of select="normalize-space(//ContactPersonInfo/@Title)" />
					</xsl:attribute>
				</xsl:if>
				
				<xsl:if test="(normalize-space(//ContactPersonInfo/@JobTitle) !='')">
					<xsl:attribute name="JobTitle">
						<xsl:value-of select="normalize-space(//ContactPersonInfo/@JobTitle)" />
					</xsl:attribute>
				</xsl:if>
				
				<xsl:if test="(normalize-space(//ContactPersonInfo/@FirstName) !='')">
					<xsl:attribute name="FirstName">
						<xsl:value-of select="normalize-space(//ContactPersonInfo/@FirstName)" />
					</xsl:attribute>
				</xsl:if>
				
				<xsl:if test="(normalize-space(//ContactPersonInfo/@MiddleName) !='')">
					<xsl:attribute name="MiddleName">
						<xsl:value-of select="normalize-space(//ContactPersonInfo/@MiddleName)" />
					</xsl:attribute>
				</xsl:if>
				
				<xsl:if test="(normalize-space(//ContactPersonInfo/@LastName) !='')">
					<xsl:attribute name="LastName">
						<xsl:value-of select="normalize-space(//ContactPersonInfo/@LastName)" />
					</xsl:attribute>
				</xsl:if>
				
				<xsl:if test="(normalize-space(//ContactPersonInfo/@AddressLine1) !='')">
					<xsl:attribute name="AddressLine1">
						<xsl:value-of select="normalize-space(//ContactPersonInfo/@AddressLine1)" />
					</xsl:attribute>
				</xsl:if>
				
				<xsl:if test="(normalize-space(//ContactPersonInfo/@AddressLine2) !='')">
					<xsl:attribute name="AddressLine2">
						<xsl:value-of select="normalize-space(//ContactPersonInfo/@AddressLine2)" />
					</xsl:attribute>
				</xsl:if>
				
				<xsl:if test="(normalize-space(//ContactPersonInfo/@AddressLine3) !='')">
					<xsl:attribute name="AddressLine3">
						<xsl:value-of select="normalize-space(//ContactPersonInfo/@AddressLine3)" />
					</xsl:attribute>
				</xsl:if>
				
				<xsl:if test="(normalize-space(//ContactPersonInfo/@AddressLine4) !='')">
					<xsl:attribute name="AddressLine4">
						<xsl:value-of select="normalize-space(//@AddressLine4)" />
					</xsl:attribute>
				</xsl:if>
				
				<xsl:if test="(normalize-space(//ContactPersonInfo/@AddressLine5) !='')">
					<xsl:attribute name="AddressLine5">
						<xsl:value-of select="normalize-space(//ContactPersonInfo/@AddressLine5)" />
					</xsl:attribute>
				</xsl:if>
				
				<xsl:if test="(normalize-space(//ContactPersonInfo/@City) !='')">
					<xsl:attribute name="City">
						<xsl:value-of select="normalize-space(//ContactPersonInfo/@City)" />
					</xsl:attribute>
				</xsl:if>
				
				<xsl:if test="(normalize-space(//ContactPersonInfo/@State) !='')">
					<xsl:attribute name="State">
						<xsl:value-of select="normalize-space(//ContactPersonInfo/@State)" />
					</xsl:attribute>
				</xsl:if>
				
				<xsl:if test="(normalize-space(//ContactPersonInfo/@Country) !='')">
					<xsl:attribute name="Country">
						<xsl:value-of select="normalize-space(//ContactPersonInfo/@Country)" />
					</xsl:attribute>
				</xsl:if>
				
				<xsl:if test="(normalize-space(//ContactPersonInfo/@ZipCode) !='')">
					<xsl:attribute name="ZipCode">
						<xsl:value-of select="normalize-space(//ContactPersonInfo/@ZipCode)" />
					</xsl:attribute>
				</xsl:if>
				
				<xsl:if test="(normalize-space(//ContactPersonInfo/@EMailID) !='')">
					<xsl:attribute name="EMailID">
						<xsl:value-of select="normalize-space(//ContactPersonInfo/@EMailID)" />
					</xsl:attribute>
				</xsl:if>
				
				<xsl:if test="(normalize-space(//ContactPersonInfo/@MobilePhone) !='')">
					<xsl:attribute name="MobilePhone">
						<xsl:value-of select="normalize-space(//ContactPersonInfo/@MobilePhone)" />
					</xsl:attribute>
				</xsl:if>
				
				<xsl:if test="(normalize-space(//ContactPersonInfo/@DayFaxNo) !='')">
					<xsl:attribute name="DayFaxNo">
						<xsl:value-of select="normalize-space(//ContactPersonInfo/@DayFaxNo)" />
					</xsl:attribute>
				</xsl:if>
				
				<xsl:if test="(normalize-space(//ContactPersonInfo/@DayPhone) !='')">
					<xsl:attribute name="DayPhone">
						<xsl:value-of select="normalize-space(//ContactPersonInfo/@DayPhone)" />
					</xsl:attribute>
				</xsl:if>
				
				<xsl:if test="(normalize-space(//ContactPersonInfo/@EveningFaxNo) !='')">
					<xsl:attribute name="EveningFaxNo">
						<xsl:value-of select="normalize-space(//ContactPersonInfo/@EveningFaxNo)" />
					</xsl:attribute>
				</xsl:if>
				
				<xsl:if test="(normalize-space(//ContactPersonInfo/@EveningPhone) !='')">
					<xsl:attribute name="EveningPhone">
						<xsl:value-of select="normalize-space(//ContactPersonInfo/@EveningPhone)" />
					</xsl:attribute>
				</xsl:if>
				
				<xsl:if test="(normalize-space(//ContactPersonInfo/@Company) !='')">
					<xsl:attribute name="Company">
						<xsl:value-of select="normalize-space(//ContactPersonInfo/@Company)" />
					</xsl:attribute>
				</xsl:if>
				
				<xsl:if test="(normalize-space(//ContactPersonInfo/@Department) !='')">
					<xsl:attribute name="Department">
						<xsl:value-of select="normalize-space(//ContactPersonInfo/@Department)" />
					</xsl:attribute>
				</xsl:if>				
			</xsl:element>
			
			<xsl:element name="UserGroupLists">
				<xsl:attribute name="Reset">
					<xsl:value-of select="//UserGroupLists/@Reset" />
				</xsl:attribute>
							
				<xsl:for-each select="//UserGroupLists/UserGroupList">
					<xsl:element name="UserGroupList">		
						<xsl:attribute name="UsergroupId">
							<xsl:value-of select="@UsergroupId" />
						</xsl:attribute>				
					</xsl:element>
				</xsl:for-each>							
			</xsl:element>
			<xsl:element name="Extn">
				<xsl:attribute  name="ExtnUserType">INTERNAL</xsl:attribute>
				<xsl:attribute  name="ExtnEmployeeId"><xsl:value-of select="normalize-space(//Extn/@ExtnEmployeeID)"/>
				</xsl:attribute>
			</xsl:element>  
      </xsl:element>
   </xsl:template>
</xsl:stylesheet>