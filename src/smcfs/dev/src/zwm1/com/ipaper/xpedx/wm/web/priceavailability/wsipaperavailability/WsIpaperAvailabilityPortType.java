
package zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "wsIpaperAvailability_PortType", targetNamespace = "http://zwm1/com/ipaper/xpedx/wm/web/priceavailability/wsIpaperAvailability")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface WsIpaperAvailabilityPortType {


    /**
     * 
     * @param wsIpaperAvailabilityInput
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "com_ipaper_xpedx_wm_web_priceavailability_wsIpaperAvailability_Binder_fGetAvailability")
    @WebResult(name = "wsIpaperAvailabilityOutput", targetNamespace = "")
    @RequestWrapper(localName = "fGetAvailability", targetNamespace = "http://zwm1/com/ipaper/xpedx/wm/web/priceavailability/wsIpaperAvailability", className = "zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailability")
    @ResponseWrapper(localName = "fGetAvailabilityResponse", targetNamespace = "http://zwm1/com/ipaper/xpedx/wm/web/priceavailability/wsIpaperAvailability", className = "zwm1.com.ipaper.xpedx.wm.web.priceavailability.wsipaperavailability.FGetAvailabilityResponse")
    public String fGetAvailability(
        @WebParam(name = "wsIpaperAvailabilityInput", targetNamespace = "")
        String wsIpaperAvailabilityInput);

}
