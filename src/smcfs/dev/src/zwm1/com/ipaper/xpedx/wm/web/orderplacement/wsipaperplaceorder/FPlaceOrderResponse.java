
package zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fPlaceOrderResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fPlaceOrderResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="wsIpaperPlaceOrderOutput" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fPlaceOrderResponse", propOrder = {
    "wsIpaperPlaceOrderOutput"
})
public class FPlaceOrderResponse {

    @XmlElement(required = true, nillable = true)
    protected String wsIpaperPlaceOrderOutput;

    /**
     * Gets the value of the wsIpaperPlaceOrderOutput property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWsIpaperPlaceOrderOutput() {
        return wsIpaperPlaceOrderOutput;
    }

    /**
     * Sets the value of the wsIpaperPlaceOrderOutput property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWsIpaperPlaceOrderOutput(String value) {
        this.wsIpaperPlaceOrderOutput = value;
    }

}
