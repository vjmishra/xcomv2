/*
 * XML Type:  fGetAvailability
 * Namespace: http://zwm1/com/ipaper/xpedx/wm/web/priceavailability/wsIpaperAvailability
 * Java type: zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability
 *
 * Automatically generated - do not modify.
 */
package zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability;


/**
 * An XML fGetAvailability(@http://zwm1/com/ipaper/xpedx/wm/web/priceavailability/wsIpaperAvailability).
 *
 * This is a complex type.
 */
public interface FGetAvailability extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(FGetAvailability.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sCCA32640655C75AE68B9D6D71DB60485").resolveHandle("fgetavailabilitybbd5type");
    
    /**
     * Gets the "wsIpaperAvailabilityInput" element
     */
    java.lang.String getWsIpaperAvailabilityInput();
    
    /**
     * Gets (as xml) the "wsIpaperAvailabilityInput" element
     */
    org.apache.xmlbeans.XmlString xgetWsIpaperAvailabilityInput();
    
    /**
     * Tests for nil "wsIpaperAvailabilityInput" element
     */
    boolean isNilWsIpaperAvailabilityInput();
    
    /**
     * Sets the "wsIpaperAvailabilityInput" element
     */
    void setWsIpaperAvailabilityInput(java.lang.String wsIpaperAvailabilityInput);
    
    /**
     * Sets (as xml) the "wsIpaperAvailabilityInput" element
     */
    void xsetWsIpaperAvailabilityInput(org.apache.xmlbeans.XmlString wsIpaperAvailabilityInput);
    
    /**
     * Nils the "wsIpaperAvailabilityInput" element
     */
    void setNilWsIpaperAvailabilityInput();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability newInstance() {
          return (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
