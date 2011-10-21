/*
 * XML Type:  fGetAvailabilityResponse
 * Namespace: http://zwm1/com/ipaper/xpedx/wm/web/priceavailability/wsIpaperAvailability
 * Java type: zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityResponse
 *
 * Automatically generated - do not modify.
 */
package zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.impl;
/**
 * An XML fGetAvailabilityResponse(@http://zwm1/com/ipaper/xpedx/wm/web/priceavailability/wsIpaperAvailability).
 *
 * This is a complex type.
 */
public class FGetAvailabilityResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityResponse
{
    
    public FGetAvailabilityResponseImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName WSIPAPERAVAILABILITYOUTPUT$0 = 
        new javax.xml.namespace.QName("", "wsIpaperAvailabilityOutput");
    
    
    /**
     * Gets the "wsIpaperAvailabilityOutput" element
     */
    public java.lang.String getWsIpaperAvailabilityOutput()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(WSIPAPERAVAILABILITYOUTPUT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "wsIpaperAvailabilityOutput" element
     */
    public org.apache.xmlbeans.XmlString xgetWsIpaperAvailabilityOutput()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(WSIPAPERAVAILABILITYOUTPUT$0, 0);
            return target;
        }
    }
    
    /**
     * Tests for nil "wsIpaperAvailabilityOutput" element
     */
    public boolean isNilWsIpaperAvailabilityOutput()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(WSIPAPERAVAILABILITYOUTPUT$0, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * Sets the "wsIpaperAvailabilityOutput" element
     */
    public void setWsIpaperAvailabilityOutput(java.lang.String wsIpaperAvailabilityOutput)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(WSIPAPERAVAILABILITYOUTPUT$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(WSIPAPERAVAILABILITYOUTPUT$0);
            }
            target.setStringValue(wsIpaperAvailabilityOutput);
        }
    }
    
    /**
     * Sets (as xml) the "wsIpaperAvailabilityOutput" element
     */
    public void xsetWsIpaperAvailabilityOutput(org.apache.xmlbeans.XmlString wsIpaperAvailabilityOutput)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(WSIPAPERAVAILABILITYOUTPUT$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(WSIPAPERAVAILABILITYOUTPUT$0);
            }
            target.set(wsIpaperAvailabilityOutput);
        }
    }
    
    /**
     * Nils the "wsIpaperAvailabilityOutput" element
     */
    public void setNilWsIpaperAvailabilityOutput()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(WSIPAPERAVAILABILITYOUTPUT$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(WSIPAPERAVAILABILITYOUTPUT$0);
            }
            target.setNil();
        }
    }
}
