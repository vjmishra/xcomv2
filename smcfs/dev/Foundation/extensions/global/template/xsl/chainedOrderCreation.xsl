<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

          <xsl:template match="/">

                   	<xsl:element name="Order">

                                
							 <xsl:attribute name="DocumentType">
                             
                                 <xsl:value-of select="Order/@DocumentType"/>

                             </xsl:attribute>							  

                             <xsl:attribute name="EnterpriseCode">
                             
                                 <xsl:value-of select="Order/@EnterpriseCode"/>

                             </xsl:attribute>
 
                             <xsl:attribute name="BuyerOrganizationCode">
                             
                                 <xsl:value-of select="Order/@BuyerOrganizationCode"/>

                             </xsl:attribute>

							 <xsl:attribute name="BillToID">
                             
                                 <xsl:value-of select="Order/@BillToID"/>

                             </xsl:attribute>

							 <xsl:attribute name="CustomerContactID">
                             
                                 <xsl:value-of select="Order/@CustomerContactID"/>

                             </xsl:attribute>


							 <xsl:attribute name="IgnoreOrdering">
                             
                                 <xsl:value-of select="Order/@IgnoreOrdering"/>

                             </xsl:attribute>

							 <xsl:attribute name="OrderName">
                             
                                 <xsl:value-of select="Order/@OrderName"/>

                             </xsl:attribute>

							 <xsl:attribute name="IgnoreOrdering">
                             
                                 <xsl:value-of select="Order/@IgnoreOrdering"/>

                             </xsl:attribute>

							 
							 <xsl:attribute name="OrderType">
                             
                             <xsl:text>LEGACY</xsl:text>

                             </xsl:attribute>	
                             
                          <xsl:copy-of select="Order/Extn"/>
                                                                             
                           	<xsl:element name="OrderLines">
                           	
                           	
                             	    <xsl:for-each select="Order/OrderLines/OrderLine">
                             	      
                             		<xsl:element name="OrderLine">
                             
                            		 	<xsl:attribute name="OrderedQty">
                            		 	
                            		 		 <xsl:value-of select="@OrderedQty"/>

                                        </xsl:attribute>

					                	<xsl:attribute name="ReservationMandatory">
                            		 	
                            		 		<xsl:value-of select="@ReservationMandatory"/>

                                        </xsl:attribute>
                             			
                             			<xsl:attribute name="ValidateItem">
                             				
                             				<xsl:value-of select="@ValidateItem"/>
                             				
                             			</xsl:attribute>
                                                                   			
                             			
										
                                         <xsl:copy-of select="./Extn"/>
                                         
                             				
                             			
                                        <xsl:element name="Item">

                                            <xsl:attribute name="ItemID">
                             				
                             				<xsl:value-of select="Item/@ItemID"/>
                             				
                             			    </xsl:attribute>

                                              <xsl:attribute name="UnitOfMeasure">
                             				
                             				<xsl:value-of select="Item/@UnitOfMeasure"/>
                             				
                             			    </xsl:attribute> 
                                     
					                     </xsl:element>
					                     
					                     
					                     <xsl:element name="OrderLineTranQuantity">

                                            <xsl:attribute name="OrderedQty">
                             				
                             				<xsl:value-of select="OrderLineTranQuantity/@OrderedQty"/>
                             				
                             			    </xsl:attribute>

                                              <xsl:attribute name="TransactionalUOM">
                             				
                             				<xsl:value-of select="OrderLineTranQuantity/@TransactionalUOM"/>
                             				
                             			    </xsl:attribute> 
                                     
					                     </xsl:element>
					                     
					                     
					                     <xsl:element name="ChainedFrom">
					                     
					                     
					                        <xsl:attribute name="DocumentType">
                             				
                             			     	<xsl:value-of select="/Order/@DocumentType"/>
                             				
                             			    </xsl:attribute>
                             			    
                             			    <xsl:attribute name="EnterpriseCode">
                             
                                                <xsl:value-of select="/Order/@EnterpriseCode"/>

                                            </xsl:attribute>
                                            
                                            <xsl:attribute name="OrderNo">
                             
                                                <xsl:value-of select="/Order/@OrderNo"/>

                                            </xsl:attribute>
					                     
					                     
					                         <xsl:attribute name="PrimeLineNo">
                             				
                             				<xsl:value-of select="@PrimeLineNo"/>
                             				
                             			    </xsl:attribute>

                                              <xsl:attribute name="SubLineNo">
                             				
                             				<xsl:value-of select="@SubLineNo"/>
                             				
                             			    </xsl:attribute> 
                                     
					                     </xsl:element>
					                             
                                    

                                  </xsl:element>
                                  </xsl:for-each>
                                  </xsl:element>
                                  
                                  <xsl:copy-of select="Order/PersonInfoBillTo"/>
                              <xsl:copy-of select="Order/PersonInfoShipTo"/>
                                  
                             
                              
                                                        
                              
                  </xsl:element>                    

          </xsl:template>

</xsl:stylesheet>