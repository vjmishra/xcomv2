
/**
 * WsIpaperAvailabilityMessageReceiverInOut.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.1  Built on : Oct 19, 2009 (10:59:00 EDT)
 */
        package zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability;

        /**
        *  WsIpaperAvailabilityMessageReceiverInOut message receiver
        */

        public class WsIpaperAvailabilityMessageReceiverInOut extends org.apache.axis2.receivers.AbstractInOutMessageReceiver{


        public void invokeBusinessLogic(org.apache.axis2.context.MessageContext msgContext, org.apache.axis2.context.MessageContext newMsgContext)
        throws org.apache.axis2.AxisFault{

        try {

        // get the implementation class for the Web Service
        Object obj = getTheImplementationObject(msgContext);

        WsIpaperAvailabilitySkeletonInterface skel = (WsIpaperAvailabilitySkeletonInterface)obj;
        //Out Envelop
        org.apache.axiom.soap.SOAPEnvelope envelope = null;
        //Find the axisOperation that has been set by the Dispatch phase.
        org.apache.axis2.description.AxisOperation op = msgContext.getOperationContext().getAxisOperation();
        if (op == null) {
        throw new org.apache.axis2.AxisFault("Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
        }

        java.lang.String methodName;
        if((op.getName() != null) && ((methodName = org.apache.axis2.util.JavaUtils.xmlNameToJavaIdentifier(op.getName().getLocalPart())) != null)){

        

            if("fGetAvailability".equals(methodName)){
                
                zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityResponseDocument fGetAvailabilityResponse3 = null;
	                        zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityDocument wrappedParam =
                                                             (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityDocument)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityDocument.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               fGetAvailabilityResponse3 =
                                                   
                                                   
                                                         skel.fGetAvailability(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), fGetAvailabilityResponse3, false);
                                    
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

            private  org.apache.axiom.om.OMElement  toOM(zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityDocument param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault{

            
                    return toOM(param);
                

            }

            private org.apache.axiom.om.OMElement toOM(final zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityDocument param)
                    throws org.apache.axis2.AxisFault {

                final javax.xml.stream.XMLStreamReader xmlReader = param.newXMLStreamReader();
                while (!xmlReader.isStartElement()) {
                    try {
                        xmlReader.next();
                    } catch (javax.xml.stream.XMLStreamException e) {
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                }

                org.apache.axiom.om.OMDataSource omDataSource = new org.apache.axiom.om.OMDataSource() {

                    public void serialize(java.io.OutputStream outputStream, org.apache.axiom.om.OMOutputFormat omOutputFormat)
                            throws javax.xml.stream.XMLStreamException {
                        try {
                            org.apache.xmlbeans.XmlOptions xmlOptions = new org.apache.xmlbeans.XmlOptions();
                            param.save(outputStream,xmlOptions.setSaveNoXmlDecl());
                        } catch (java.io.IOException e) {
                            throw new javax.xml.stream.XMLStreamException("Problem with saving document",e);
                        }
                    }

                    public void serialize(java.io.Writer writer, org.apache.axiom.om.OMOutputFormat omOutputFormat)
                            throws javax.xml.stream.XMLStreamException {
                        try {
                            org.apache.xmlbeans.XmlOptions xmlOptions = new org.apache.xmlbeans.XmlOptions();
                            param.save(writer,xmlOptions.setSaveNoXmlDecl());
                        } catch (java.io.IOException e) {
                            throw new javax.xml.stream.XMLStreamException("Problem with saving document",e);
                        }
                    }

                    public void serialize(javax.xml.stream.XMLStreamWriter xmlStreamWriter)
                            throws javax.xml.stream.XMLStreamException {
                        org.apache.axiom.om.impl.MTOMXMLStreamWriter mtomxmlStreamWriter =
                                                        (org.apache.axiom.om.impl.MTOMXMLStreamWriter) xmlStreamWriter;
                        try {
                            org.apache.xmlbeans.XmlOptions xmlOptions = new org.apache.xmlbeans.XmlOptions();
                            param.save(mtomxmlStreamWriter.getOutputStream(),xmlOptions.setSaveNoXmlDecl());
                            mtomxmlStreamWriter.getOutputStream().flush();
                        } catch (java.io.IOException e) {
                            throw new javax.xml.stream.XMLStreamException("Problem with saving document", e);
                        }
                    }

                    public javax.xml.stream.XMLStreamReader getReader()
                            throws javax.xml.stream.XMLStreamException {
                        return param.newXMLStreamReader();
                    }
                };
            
                return  new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(xmlReader.getName(),
                        org.apache.axiom.om.OMAbstractFactory.getOMFactory(),
                        omDataSource);
            }
        

            private  org.apache.axiom.om.OMElement  toOM(zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityResponseDocument param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault{

            
                    return toOM(param);
                

            }

            private org.apache.axiom.om.OMElement toOM(final zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityResponseDocument param)
                    throws org.apache.axis2.AxisFault {

                final javax.xml.stream.XMLStreamReader xmlReader = param.newXMLStreamReader();
                while (!xmlReader.isStartElement()) {
                    try {
                        xmlReader.next();
                    } catch (javax.xml.stream.XMLStreamException e) {
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                }

                org.apache.axiom.om.OMDataSource omDataSource = new org.apache.axiom.om.OMDataSource() {

                    public void serialize(java.io.OutputStream outputStream, org.apache.axiom.om.OMOutputFormat omOutputFormat)
                            throws javax.xml.stream.XMLStreamException {
                        try {
                            org.apache.xmlbeans.XmlOptions xmlOptions = new org.apache.xmlbeans.XmlOptions();
                            param.save(outputStream,xmlOptions.setSaveNoXmlDecl());
                        } catch (java.io.IOException e) {
                            throw new javax.xml.stream.XMLStreamException("Problem with saving document",e);
                        }
                    }

                    public void serialize(java.io.Writer writer, org.apache.axiom.om.OMOutputFormat omOutputFormat)
                            throws javax.xml.stream.XMLStreamException {
                        try {
                            org.apache.xmlbeans.XmlOptions xmlOptions = new org.apache.xmlbeans.XmlOptions();
                            param.save(writer,xmlOptions.setSaveNoXmlDecl());
                        } catch (java.io.IOException e) {
                            throw new javax.xml.stream.XMLStreamException("Problem with saving document",e);
                        }
                    }

                    public void serialize(javax.xml.stream.XMLStreamWriter xmlStreamWriter)
                            throws javax.xml.stream.XMLStreamException {
                        org.apache.axiom.om.impl.MTOMXMLStreamWriter mtomxmlStreamWriter =
                                                        (org.apache.axiom.om.impl.MTOMXMLStreamWriter) xmlStreamWriter;
                        try {
                            org.apache.xmlbeans.XmlOptions xmlOptions = new org.apache.xmlbeans.XmlOptions();
                            param.save(mtomxmlStreamWriter.getOutputStream(),xmlOptions.setSaveNoXmlDecl());
                            mtomxmlStreamWriter.getOutputStream().flush();
                        } catch (java.io.IOException e) {
                            throw new javax.xml.stream.XMLStreamException("Problem with saving document", e);
                        }
                    }

                    public javax.xml.stream.XMLStreamReader getReader()
                            throws javax.xml.stream.XMLStreamException {
                        return param.newXMLStreamReader();
                    }
                };
            
                return  new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(xmlReader.getName(),
                        org.apache.axiom.om.OMAbstractFactory.getOMFactory(),
                        omDataSource);
            }
        
                            private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityResponseDocument param, boolean optimizeContent)
                            throws org.apache.axis2.AxisFault {
                            org.apache.axiom.soap.SOAPEnvelope envelope = factory.getDefaultEnvelope();
                            if (param != null){
                            envelope.getBody().addChild(toOM(param, optimizeContent));
                            }
                            return envelope;
                            }
                        


        /**
        *  get the default envelope
        */
        private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory){
        return factory.getDefaultEnvelope();
        }

        public org.apache.xmlbeans.XmlObject fromOM(
        org.apache.axiom.om.OMElement param,
        java.lang.Class type,
        java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault{
        try{
        

            if (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityDocument.class.equals(type)){
            if (extraNamespaces!=null){
            return zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityDocument.Factory.parse(
            param.getXMLStreamReaderWithoutCaching(),
            new org.apache.xmlbeans.XmlOptions().setLoadAdditionalNamespaces(extraNamespaces));
            }else{
            return zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityDocument.Factory.parse(
            param.getXMLStreamReaderWithoutCaching());
            }
            }

        

            if (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityResponseDocument.class.equals(type)){
            if (extraNamespaces!=null){
            return zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityResponseDocument.Factory.parse(
            param.getXMLStreamReaderWithoutCaching(),
            new org.apache.xmlbeans.XmlOptions().setLoadAdditionalNamespaces(extraNamespaces));
            }else{
            return zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityResponseDocument.Factory.parse(
            param.getXMLStreamReaderWithoutCaching());
            }
            }

        
        }catch(java.lang.Exception e){
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
    