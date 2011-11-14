/*
 * An XML document type.
 * Localname: fGetAvailability
 * Namespace: http://zwm1/com/ipaper/xpedx/wm/web/priceavailability/wsIpaperAvailability
 * Java type: zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityDocument
 *
 * Automatically generated - do not modify.
 */
package zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.impl;
/**
 * A document containing one fGetAvailability(@http://zwm1/com/ipaper/xpedx/wm/web/priceavailability/wsIpaperAvailability) element.
 *
 * This is a complex type.
 */
public class FGetAvailabilityDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityDocument
{
    
    public FGetAvailabilityDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName FGETAVAILABILITY$0 = 
        new javax.xml.namespace.QName("http://zwm1/com/ipaper/xpedx/wm/web/priceavailability/wsIpaperAvailability", "fGetAvailability");
    
    
    /**
     * Gets the "fGetAvailability" element
     */
    public zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability getFGetAvailability()
    {
        synchronized (monitor())
        {
            check_orphaned();
            zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability target = null;
            target = (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability)get_store().find_element_user(FGETAVAILABILITY$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "fGetAvailability" element
     */
    public void setFGetAvailability(zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability fGetAvailability)
    {
        synchronized (monitor())
        {
            check_orphaned();
            zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability target = null;
            target = (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability)get_store().find_element_user(FGETAVAILABILITY$0, 0);
            if (target == null)
            {
                target = (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability)get_store().add_element_user(FGETAVAILABILITY$0);
            }
            target.set(fGetAvailability);
        }
    }
    
    /**
     * Appends and returns a new empty "fGetAvailability" element
     */
    public zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability addNewFGetAvailability()
    {
        synchronized (monitor())
        {
            check_orphaned();
            zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability target = null;
            target = (zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability)get_store().add_element_user(FGETAVAILABILITY$0);
            return target;
        }
    }
}
