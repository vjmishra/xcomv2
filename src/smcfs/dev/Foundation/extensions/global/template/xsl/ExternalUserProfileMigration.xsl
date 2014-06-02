<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output indent="yes" />
   <xsl:template match="/Customer"> 
		<xsl:element name="Customer">	 			 
			<xsl:attribute name="CustomerID">
				<xsl:value-of select="//@CustomerID" />
			</xsl:attribute>
			
			<xsl:attribute name="OrganizationCode">
				<xsl:value-of select="//@OrganizationCode" />
			</xsl:attribute>
			
			<xsl:attribute name="Operation">
				<xsl:value-of select="'Modify'" />
			</xsl:attribute>
		 
			<xsl:element name="CustomerContactList">	 
				<xsl:element name="CustomerContact">
					<xsl:attribute name="Company">
						<xsl:value-of select="//CustomerContact/@Company" />
					</xsl:attribute>
					<xsl:attribute name="CustomerContactID">
						<xsl:value-of select="//CustomerContact/@CustomerContactID" />
					</xsl:attribute>
					<xsl:attribute name="SpendingLimitCurrency">
						<xsl:value-of select="//CustomerContact/@SpendingLimitCurrency" />
					</xsl:attribute>
					<xsl:attribute name="SpendingLimit">
						<xsl:value-of select="//CustomerContact/@SpendingLimit" />
					</xsl:attribute>
					<xsl:attribute name="ApproverProxyUserId">
						<xsl:value-of select="//CustomerContact/@ApproverProxyUserId" />
					</xsl:attribute>					
					<xsl:attribute name="ApproverUserId">
						<xsl:value-of select="//CustomerContact/@ApproverUserId" />
					</xsl:attribute>
					<xsl:attribute name="DayFaxNo">
						<xsl:value-of select="//CustomerContact/@DayFaxNo" />
					</xsl:attribute>
					<xsl:attribute name="DayPhone">
						<xsl:value-of select="//CustomerContact/@DayPhone" />
					</xsl:attribute>
					<xsl:attribute name="Department">
						<xsl:value-of select="//CustomerContact/@Department" />
					</xsl:attribute>
					<xsl:attribute name="EmailID">
						<xsl:value-of select="//CustomerContact/@EmailID" />
					</xsl:attribute>
					<xsl:attribute name="EveningFaxNo">
						<xsl:value-of select="//CustomerContact/@EveningFaxNo" />
					</xsl:attribute>
					<xsl:attribute name="EveningPhone">
						<xsl:value-of select="//CustomerContact/@EveningPhone" />
					</xsl:attribute>
					<xsl:attribute name="FirstName">
						<xsl:value-of select="//CustomerContact/@FirstName" />
					</xsl:attribute>
					<xsl:attribute name="JobTitle">
						<xsl:value-of select="//CustomerContact/@JobTitle" />
					</xsl:attribute>
					<xsl:attribute name="LastName">
						<xsl:value-of select="//CustomerContact/@LastName" />
					</xsl:attribute>
					<xsl:attribute name="MobilePhone">
						<xsl:value-of select="//CustomerContact/@MobilePhone" />
					</xsl:attribute>
				
					<xsl:element name="User">
						<xsl:attribute name="Activateflag">
							<xsl:value-of select="//User/@Activateflag" />
						</xsl:attribute>
						<xsl:attribute name="EnterpriseCode">
							<xsl:value-of select="//User/@EnterpriseCode" />
						</xsl:attribute>
						<xsl:attribute name="DisplayUserID">
							<xsl:value-of select="//User/@DisplayUserID" />
						</xsl:attribute>
						<xsl:attribute name="Password">
							<xsl:value-of select="//User/@Password" />
						</xsl:attribute>
						<xsl:attribute name="Loginid">
							<xsl:value-of select="//User/@Loginid" />
						</xsl:attribute>  
						<xsl:attribute name="GeneratePassword">
							<xsl:value-of select="//User/@GeneratePassword" />
						</xsl:attribute>  
						<xsl:attribute name="Username">
							<xsl:value-of select="//User/@Username" />
						</xsl:attribute>  
						<xsl:attribute name="Localecode">
							<xsl:value-of select="//User/@Localecode" />
						</xsl:attribute>  
							
						<xsl:attribute  name="SessionTimeout">0</xsl:attribute>
						
						<xsl:copy-of select="//User/ContactPersonInfo/."/>
						<xsl:copy-of select="//User/UserGroupLists/."/>
						
						<xsl:element name="Extn">
							<xsl:attribute  name="ExtnUserType">EXTERNAL</xsl:attribute>
						</xsl:element>  
					</xsl:element>  
						
					<xsl:copy-of select="//CustomerContact/Extn"/>
					
					<xsl:copy-of select="//CustomerAdditionalAddressList"/>
					
					<xsl:copy-of select="//CustomerAssignmentList"/>	
				</xsl:element>	
			</xsl:element>	
		</xsl:element>	
	</xsl:template>
</xsl:stylesheet>