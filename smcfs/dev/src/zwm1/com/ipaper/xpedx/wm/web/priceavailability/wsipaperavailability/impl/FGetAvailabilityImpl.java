/*
 * XML Type:  fGetAvailability
 * Namespace: http://zwm1/com/ipaper/xpedx/wm/web/priceavailability/wsIpaperAvailability
 * Java type: zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability
 *
 * Automatically generated - do not modify.
 */
package zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.impl;
/**
 * An XML fGetAvailability(@http://zwm1/com/ipaper/xpedx/wm/web/priceavailability/wsIpaperAvailability).
 *
 * This is a complex type.
 */
public class FGetAvailabilityImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability
{
    
    public FGetAvailabilityImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName WSIPAPERAVAILABILITYINPUT$0 = 
        new javax.xml.namespace.QName("", "wsIpaperAvailabilityInput");
    
    
    /**
     * Gets the "wsIpaperAvailabilityInput" element
     */
    public java.lang.String getWsIpaperAvailabilityInput()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(WSIPAPERAVAILABILITYINPUT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "wsIpaperAvailabilityInput" element
     */
    public org.apache.xmlbeans.XmlString xgetWsIpaperAvailabilityInput()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(WSIPAPERAVAILABILITYINPUT$0, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "wsIpaperAvailabilityInput" element
     */
    public boolean isNilWsIpaperAvailabilityInput()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(WSIPAPERAVAILABILITYINPUT$0, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "wsIpaperAvailabilityInput" element
     */
    public void setWsIpaperAvailabilityInput(java.lang.String wsIpaperAvailabilityInput)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(WSIPAPERAVAILABILITYINPUT$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(WSIPAPERAVAILABILITYINPUT$0);
            }
            target.setStringValue(wsIpaperAvailabilityInput);
        }
    }
    
    /**
     * Sets (as xml) the "wsIpaperAvailabilityInput" element
     */
    public void xsetWsIpaperAvailabilityInput(org.apache.xmlbeans.XmlString wsIpaperAvailabilityInput)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(WSIPAPERAVAILABILITYINPUT$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(WSIPAPERAVAILABILITYINPUT$0);
            }
            target.set(wsIpaperAvailabilityInput);
        }
    }
    
    /**
     * Nils the "wsIpaperAvailabilityInput" element
     */
    public void setNilWsIpaperAvailabilityInput()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(WSIPAPERAVAILABILITYINPUT$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(WSIPAPERAVAILABILITYINPUT$0);
            }
            target.setNil();
        }
    }
}
