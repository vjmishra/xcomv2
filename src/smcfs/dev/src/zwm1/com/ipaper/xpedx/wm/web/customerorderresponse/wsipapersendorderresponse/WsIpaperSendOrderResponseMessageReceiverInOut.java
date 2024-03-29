
/**
 * WsIpaperSendOrderResponseMessageReceiverInOut.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.1  Built on : Oct 19, 2009 (10:59:00 EDT)
 */
        package zwm1.com.ipaper.xpedx.wm.web.customerorderresponse.wsipapersendorderresponse;

        /**
        *  WsIpaperSendOrderResponseMessageReceiverInOut message receiver
        */

        public class WsIpaperSendOrderResponseMessageReceiverInOut extends org.apache.axis2.receivers.AbstractInOutMessageReceiver{


        public void invokeBusinessLogic(org.apache.axis2.context.MessageContext msgContext, org.apache.axis2.context.MessageContext newMsgContext)
        throws org.apache.axis2.AxisFault{

        try {

        // get the implementation class for the Web Service
        Object obj = getTheImplementationObject(msgContext);

        WsIpaperSendOrderResponseSkeletonInterface skel = (WsIpaperSendOrderResponseSkeletonInterface)obj;
        //Out Envelop
        org.apache.axiom.soap.SOAPEnvelope envelope = null;
        //Find the axisOperation that has been set by the Dispatch phase.
        org.apache.axis2.description.AxisOperation op = msgContext.getOperationContext().getAxisOperation();
        if (op == null) {
        throw new org.apache.axis2.AxisFault("Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
        }

        java.lang.String methodName;
        if((op.getName() != null) && ((methodName = org.apache.axis2.util.JavaUtils.xmlNameToJavaIdentifier(op.getName().getLocalPart())) != null)){

        

            if("fSendOrderResponse".equals(methodName)){
                
                zwm1.com.ipaper.xpedx.wm.web.customerorderresponse.wsipapersendorderresponse.FPlaceOrderResponseE fPlaceOrderResponse3 = null;
	                        zwm1.com.ipaper.xpedx.wm.web.customerorderresponse.wsipapersendorderresponse.FPlaceOrderE wrappedParam =
                                                             (zwm1.com.ipaper.xpedx.wm.web.customerorderresponse.wsipapersendorderresponse.FPlaceOrderE)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    zwm1.com.ipaper.xpedx.wm.web.customerorderresponse.wsipapersendorderresponse.FPlaceOrderE.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               fPlaceOrderResponse3 =
                                                   
                                                   
                                                         skel.fSendOrderResponse(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), fPlaceOrderResponse3, false);
                                    
            } else {
              throw new java.lang.RuntimeException("method not found");
            }
        

        newMsgContext.setEnvelope(envelope);
        }
        }
        catch (java.lang.Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
        }
        }
        
        //
            private  org.apache.axiom.om.OMElement  toOM(zwm1.com.ipaper.xpedx.wm.web.customerorderresponse.wsipapersendorderresponse.FPlaceOrderE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(zwm1.com.ipaper.xpedx.wm.web.customerorderresponse.wsipapersendorderresponse.FPlaceOrderE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(zwm1.com.ipaper.xpedx.wm.web.customerorderresponse.wsipapersendorderresponse.FPlaceOrderResponseE param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(zwm1.com.ipaper.xpedx.wm.web.customerorderresponse.wsipapersendorderresponse.FPlaceOrderResponseE.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, zwm1.com.ipaper.xpedx.wm.web.customerorderresponse.wsipapersendorderresponse.FPlaceOrderResponseE param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(zwm1.com.ipaper.xpedx.wm.web.customerorderresponse.wsipapersendorderresponse.FPlaceOrderResponseE.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private zwm1.com.ipaper.xpedx.wm.web.customerorderresponse.wsipapersendorderresponse.FPlaceOrderResponseE wrapfSendOrderResponse(){
                                zwm1.com.ipaper.xpedx.wm.web.customerorderresponse.wsipapersendorderresponse.FPlaceOrderResponseE wrappedElement = new zwm1.com.ipaper.xpedx.wm.web.customerorderresponse.wsipapersendorderresponse.FPlaceOrderResponseE();
                                return wrappedElement;
                         }
                    


        /**
        *  get the default envelope
        */
        private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory){
        return factory.getDefaultEnvelope();
        }


        private  java.lang.Object fromOM(
        org.apache.axiom.om.OMElement param,
        java.lang.Class type,
        java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault{

        try {
        
                if (zwm1.com.ipaper.xpedx.wm.web.customerorderresponse.wsipapersendorderresponse.FPlaceOrderE.class.equals(type)){
                
                           return zwm1.com.ipaper.xpedx.wm.web.customerorderresponse.wsipapersendorderresponse.FPlaceOrderE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (zwm1.com.ipaper.xpedx.wm.web.customerorderresponse.wsipapersendorderresponse.FPlaceOrderResponseE.class.equals(type)){
                
                           return zwm1.com.ipaper.xpedx.wm.web.customerorderresponse.wsipapersendorderresponse.FPlaceOrderResponseE.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
        } catch (java.lang.Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
        }
           return null;
        }



    

        /**
        *  A utility method that copies the namepaces from the SOAPEnvelope
        */
        private java.util.Map getEnvelopeNamespaces(org.apache.axiom.soap.SOAPEnvelope env){
        java.util.Map returnMap = new java.util.HashMap();
        java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
        while (namespaceIterator.hasNext()) {
        org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator.next();
        returnMap.put(ns.getPrefix(),ns.getNamespaceURI());
        }
        return returnMap;
        }

        private org.apache.axis2.AxisFault createAxisFault(java.lang.Exception e) {
        org.apache.axis2.AxisFault f;
        Throwable cause = e.getCause();
        if (cause != null) {
            f = new org.apache.axis2.AxisFault(e.getMessage(), cause);
        } else {
            f = new org.apache.axis2.AxisFault(e.getMessage());
        }

        return f;
    }

        }//end of class
    