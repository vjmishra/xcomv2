

/**
 * WsIpaperSendInvoice.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.1  Built on : Oct 19, 2009 (10:59:00 EDT)
 */

    package zwm1.com.ipaper.xpedx.wm.web.customerinvoice.wsipapersendinvoice;

    /*
     *  WsIpaperSendInvoice java interface
     */

    public interface WsIpaperSendInvoice {
          

        /**
          * Auto generated method signature
          * 
                    * @param fPlaceOrder14
                
         */

         
                     public zwm1.com.ipaper.xpedx.wm.web.customerinvoice.wsipapersendinvoice.FPlaceOrderResponseE fSendInvoice(

                        zwm1.com.ipaper.xpedx.wm.web.customerinvoice.wsipapersendinvoice.FPlaceOrderE fPlaceOrder14)
                        throws java.rmi.RemoteException
             ;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param fPlaceOrder14
            
          */
        public void startfSendInvoice(

            zwm1.com.ipaper.xpedx.wm.web.customerinvoice.wsipapersendinvoice.FPlaceOrderE fPlaceOrder14,

            final zwm1.com.ipaper.xpedx.wm.web.customerinvoice.wsipapersendinvoice.WsIpaperSendInvoiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    