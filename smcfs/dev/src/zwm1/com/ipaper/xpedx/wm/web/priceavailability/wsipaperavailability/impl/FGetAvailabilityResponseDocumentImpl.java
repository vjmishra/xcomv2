/*
 * An XML document type.
 * Localname: fGetAvailabilityResponse
 * Namespace: http://zwm1/com/ipaper/xpedx/wm/web/priceavailability/wsIpaperAvailability
 * Java type: zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityResponseDocument
 *
 * Automatically generated - do not modify.
 */
package zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.impl;
/**
 * A document containing one fGetAvailabilityResponse(@http://zwm1/com/ipaper/xpedx/wm/web/priceavailability/wsIpaperAvailability) element.
 *
 * This is a complex type.
 */
public class FGetAvailabilityResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityResponseDocument
{
    
    public FGetAvailabilityResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName FGETAVAILABILITYRESPONSE$0 = 
        new javax.xml.namespace.QName("http://zwm1/com/ipaper/xpedx/wm/web/priceavailability/wsIpaperAvailability", "fGetAvailabilityResponse");
    
    
    /**
     * Gets the "fGetAvailabilityResponse" element
     */
    public zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityResponse getFGetAvailabilityResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityResponse target = null;
            target = (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityResponse)get_store().find_element_user(FGETAVAILABILITYRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "fGetAvailabilityResponse" element
     */
    public void setFGetAvailabilityResponse(zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityResponse fGetAvailabilityResponse)
    {
        synchronized (monitor())
        {
            check_orphaned();
            zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityResponse target = null;
            target = (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityResponse)get_store().find_element_user(FGETAVAILABILITYRESPONSE$0, 0);
            if (target == null)
            {
                target = (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityResponse)get_store().add_element_user(FGETAVAILABILITYRESPONSE$0);
            }
            target.set(fGetAvailabilityResponse);
        }
    }
    
    /**
     * Appends and returns a new empty "fGetAvailabilityResponse" element
     */
    public zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityResponse addNewFGetAvailabilityResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityResponse target = null;
            target = (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityResponse)get_store().add_element_user(FGETAVAILABILITYRESPONSE$0);
            return target;
        }
    }
}
