
package zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _FGetAvailabilityResponse_QNAME = new QName("http://zwm1/com/ipaper/xpedx/wm/web/priceavailability/wsIpaperAvailability", "fGetAvailabilityResponse");
    private final static QName _FGetAvailability_QNAME = new QName("http://zwm1/com/ipaper/xpedx/wm/web/priceavailability/wsIpaperAvailability", "fGetAvailability");
    

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FGetAvailability }
     * 
     */
    public FGetAvailability createFGetAvailability() {
        return new FGetAvailability();
    }

    /**
     * Create an instance of {@link FGetAvailabilityResponse }
     * 
     */
    public FGetAvailabilityResponse createFGetAvailabilityResponse() {
        return new FGetAvailabilityResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FGetAvailabilityResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://zwm1/com/ipaper/xpedx/wm/web/priceavailability/wsIpaperAvailability", name = "fGetAvailabilityResponse")
    public JAXBElement<FGetAvailabilityResponse> createFGetAvailabilityResponse(FGetAvailabilityResponse value) {
        return new JAXBElement<FGetAvailabilityResponse>(_FGetAvailabilityResponse_QNAME, FGetAvailabilityResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FGetAvailability }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://zwm1/com/ipaper/xpedx/wm/web/priceavailability/wsIpaperAvailability", name = "fGetAvailability")
    public JAXBElement<FGetAvailability> createFGetAvailability(FGetAvailability value) {
        return new JAXBElement<FGetAvailability>(_FGetAvailability_QNAME, FGetAvailability.class, null, value);
    }

}
