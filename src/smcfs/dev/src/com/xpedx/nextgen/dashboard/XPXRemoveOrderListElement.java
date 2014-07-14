package com.xpedx.nextgen.dashboard;

import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXRemoveOrderListElement implements YIFCustomApi
{

	private static YFCLogCategory log;
	private static YIFApi api = null;
	
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try 
		{
			api = YIFClientFactory.getInstance().getApi();
		}
		catch (YIFClientCreationException e1) {

			e1.printStackTrace();
		}
	}
	
	public Document removeElement(YFSEnvironment env, Document getOrderListOutput) throws Exception
	{
		
		
		Document orderDetailsOutputXML = getDocument(
			    (Element) getOrderListOutput.getElementsByTagName(
				    "Order").item(0), true);
		
		log.debug("The getOrderDetails output xml is: "+SCXmlUtil.getString(orderDetailsOutputXML));
		
		return orderDetailsOutputXML;
	}
	private Document getDocument(Element inputElement, boolean deep)
            throws IllegalArgumentException, Exception {
        // Validate input element
        if (inputElement == null) {
            throw new IllegalArgumentException(
                    "Input element cannot be null in ");
        }

        // Create a new document
        Document outputDocument = getDocument();

        // Import data from input element and
        // set as root element for output document
        outputDocument.appendChild(outputDocument
                .importNode(inputElement, deep));

        // return output document
        return outputDocument;
    }
	
	
	/**
     * Creates a Document object
     *
     * @return empty Document object
     * @throws ParserConfigurationException
     */
    private Document getDocument() throws ParserConfigurationException {
        // Create a new Document Bilder Factory instance
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();

        // Create new document builder
        DocumentBuilder documentBuilder = documentBuilderFactory
                .newDocumentBuilder();

        // Create and return document object
        return documentBuilder.newDocument();
    }
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
