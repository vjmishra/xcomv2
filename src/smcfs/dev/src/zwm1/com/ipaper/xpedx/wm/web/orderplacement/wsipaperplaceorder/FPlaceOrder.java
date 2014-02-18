
package zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fPlaceOrder complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fPlaceOrder">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="wsIpaperPlaceOrderInput" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fPlaceOrder", propOrder = {
    "wsIpaperPlaceOrderInput"
})
public class FPlaceOrder {

    @XmlElement(required = true, nillable = true)
    protected String wsIpaperPlaceOrderInput;

    /**
     * Gets the value of the wsIpaperPlaceOrderInput property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWsIpaperPlaceOrderInput() {
        return wsIpaperPlaceOrderInput;
    }

    /**
     * Sets the value of the wsIpaperPlaceOrderInput property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWsIpaperPlaceOrderInput(String value) {
        this.wsIpaperPlaceOrderInput = value;
    }

}
