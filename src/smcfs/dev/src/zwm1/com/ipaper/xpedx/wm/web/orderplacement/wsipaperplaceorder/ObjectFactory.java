
package zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder package. 
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

    private final static QName _FPlaceOrder_QNAME = new QName("http://zwm1/com/ipaper/xpedx/wm/web/orderplacement/wsIpaperPlaceOrder", "fPlaceOrder");
    private final static QName _FPlaceOrderResponse_QNAME = new QName("http://zwm1/com/ipaper/xpedx/wm/web/orderplacement/wsIpaperPlaceOrder", "fPlaceOrderResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: zwm1.com.ipaper.xpedx.wm.web.orderplacement.wsipaperplaceorder
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FPlaceOrderResponse }
     * 
     */
    public FPlaceOrderResponse createFPlaceOrderResponse() {
        return new FPlaceOrderResponse();
    }

    /**
     * Create an instance of {@link FPlaceOrder }
     * 
     */
    public FPlaceOrder createFPlaceOrder() {
        return new FPlaceOrder();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FPlaceOrder }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://zwm1/com/ipaper/xpedx/wm/web/orderplacement/wsIpaperPlaceOrder", name = "fPlaceOrder")
    public JAXBElement<FPlaceOrder> createFPlaceOrder(FPlaceOrder value) {
        return new JAXBElement<FPlaceOrder>(_FPlaceOrder_QNAME, FPlaceOrder.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FPlaceOrderResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://zwm1/com/ipaper/xpedx/wm/web/orderplacement/wsIpaperPlaceOrder", name = "fPlaceOrderResponse")
    public JAXBElement<FPlaceOrderResponse> createFPlaceOrderResponse(FPlaceOrderResponse value) {
        return new JAXBElement<FPlaceOrderResponse>(_FPlaceOrderResponse_QNAME, FPlaceOrderResponse.class, null, value);
    }

}
