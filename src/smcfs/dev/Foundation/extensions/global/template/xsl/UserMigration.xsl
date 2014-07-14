<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

          <xsl:template match="/">
				<MultiApi>
					<API Name="manageCustomer">
						<Input>
							<Customer>
		                   		<xsl:copy-of select="Customer/@*"/>
		                   		<xsl:copy-of select="Customer/BuyerOrganization"/>
		                   		<CustomerContactList>
		                   			<CustomerContact>
		                   				<xsl:copy-of select="Customer/CustomerContactList/CustomerContact/@*" />
		                   				<xsl:copy-of select="Customer/CustomerContactList/CustomerContact/User" />
		                   				<xsl:copy-of select="Customer/CustomerContactList/CustomerContact/Extn" />
		                   				<xsl:copy-of select="Customer/CustomerContactList/CustomerContact/CustomerAdditionalAddressList" />
		                   			</CustomerContact>
		                   		</CustomerContactList>
		                   	</Customer>
                   		</Input>
                   	</API>
                   	<xsl:for-each select="Customer/CustomerContactList/CustomerContact/CustomerAssignmentList/CustomerAssignment">
                   	<API Name="manageCustomerAssignment">
                   		<Input>
                   			<xsl:copy-of select="." />
                   		</Input>
                   	</API>
                   	</xsl:for-each>
                </MultiApi>
        </xsl:template>
</xsl:stylesheet>           	