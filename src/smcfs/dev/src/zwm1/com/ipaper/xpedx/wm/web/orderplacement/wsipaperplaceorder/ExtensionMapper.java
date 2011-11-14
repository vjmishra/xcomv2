
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.1  Built on : Oct 19, 2009 (10:59:34 EDT)
 */

            package zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder;
            /**
            *  ExtensionMapper class
            */
        
        public  class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "http://zwm1/com/ipaper/xpedx/wm/web/orderplacement/wsIpaperPlaceOrder".equals(namespaceURI) &&
                  "fPlaceOrder".equals(typeName)){
                   
                            return  zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder.FPlaceOrder.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://zwm1/com/ipaper/xpedx/wm/web/orderplacement/wsIpaperPlaceOrder".equals(namespaceURI) &&
                  "fPlaceOrderResponse".equals(typeName)){
                   
                            return  zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder.FPlaceOrderResponse.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    