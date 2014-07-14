<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:output indent="yes" />
   
<xsl:template match="/">

   <PriceAndAvailabilityRequest>
     
       <sSourceIndicator>
       
           <xsl:value-of select="PriceAndAvailability/SourceIndicator"/>
       
       </sSourceIndicator>    
       
       <sEnvironmentId>
       
           <xsl:value-of select="PriceAndAvailability/EnvironmentId"/>
        
       </sEnvironmentId> 
       
       <sCustomerEnvironmentId>
       
           <xsl:value-of select="PriceAndAvailability/CustomerEnvironmentId"/>   
       
       </sCustomerEnvironmentId>
       
       <sCompany>
        
           <xsl:value-of select="PriceAndAvailability/Company"/> 
       
       </sCompany>
       
       <sCustomerBranch>
       
           <xsl:value-of select="PriceAndAvailability/CustomerBranch"/>   
       
       </sCustomerBranch>
       
       <sCustomerNumber>
       
           <xsl:value-of select="PriceAndAvailability/CustomerNumber"/> 
       
       </sCustomerNumber>
       
       <sShipToSuffix>
       
           <xsl:value-of select="PriceAndAvailability/ShipToSuffix"/> 
       
       </sShipToSuffix>
       
       <sOrderBranch>
       
           <xsl:value-of select="PriceAndAvailability/OrderBranch"/>
       
       </sOrderBranch>
       
       
       <aItems>
       
           <xsl:for-each select="PriceAndAvailability/Items/Item">
           
           
               <aItem>
               
                     <sLineNumber>
                     
                           <xsl:value-of select="LineNumber"/>
                          
                     </sLineNumber>
                     
                     <sLegacyProductCode>
                     
                           <xsl:value-of select="LegacyProductCode"/>  
                     
                     </sLegacyProductCode> 
                     
                     <sRequestedQtyUOM>
                     
                           <xsl:value-of select="RequestedQtyUOM"/>
                     
                     </sRequestedQtyUOM>   
                     
                     <sRequestedQty>
                     
                     
                           <xsl:value-of select="RequestedQty"/>  
                                              
                     </sRequestedQty>
                     
                </aItem>
                
                
            </xsl:for-each> 
            
        </aItems>
        
        
    </PriceAndAvailabilityRequest>    
        
    </xsl:template>
 
</xsl:stylesheet>       
        
                    
                     
                     