

/**
 * WsIpaperPlaceOrder.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.1  Built on : Oct 19, 2009 (10:59:00 EDT)
 */

    package zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder;

    /*
     *  WsIpaperPlaceOrder java interface
     */

    public interface WsIpaperPlaceOrder {
          

        /**
          * Auto generated method signature
          * 
                    * @param fPlaceOrder14
                
         */

         
                     public zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder.FPlaceOrderResponseE fPlaceOrder(

                        zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder.FPlaceOrderE fPlaceOrder14)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param fPlaceOrder14
            
          */
        public void startfPlaceOrder(

            zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder.FPlaceOrderE fPlaceOrder14,

            final zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder.WsIpaperPlaceOrderCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    