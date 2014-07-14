package zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;




/**
 * <p>Java class for fGetAvailability complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fGetAvailability">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="wsIpaperAvailabilityInput" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fGetAvailability", propOrder = {
    "wsIpaperAvailabilityInput"
})
public class FGetAvailability {


    @XmlElement(required = true, nillable = true)
    protected String wsIpaperAvailabilityInput;


    /**
     * Gets the value of the wsIpaperAvailabilityInput property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWsIpaperAvailabilityInput() {
        return wsIpaperAvailabilityInput;
    }


    /**
     * Sets the value of the wsIpaperAvailabilityInput property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWsIpaperAvailabilityInput(String value) {
        this.wsIpaperAvailabilityInput = value;
    }


}