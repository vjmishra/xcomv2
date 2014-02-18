
package zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fGetAvailabilityResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fGetAvailabilityResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="wsIpaperAvailabilityOutput" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fGetAvailabilityResponse", propOrder = {
    "wsIpaperAvailabilityOutput"
})
public class FGetAvailabilityResponse {

    @XmlElement(required = true, nillable = true)
    protected String wsIpaperAvailabilityOutput;

    /**
     * Gets the value of the wsIpaperAvailabilityOutput property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWsIpaperAvailabilityOutput() {
        return wsIpaperAvailabilityOutput;
    }

    /**
     * Sets the value of the wsIpaperAvailabilityOutput property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWsIpaperAvailabilityOutput(String value) {
        this.wsIpaperAvailabilityOutput = value;
    }

}
