package com.sterlingcommerce.simplifieddataaccess;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

//import com.xpedx.common.XMLUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
//import com.xpedx.common.XMLUtil;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.util.YFCDate;

public class SimpleXML {
	private YFCElement xmlElem;

	public SimpleXML(String input) {
		if (input.trim().startsWith("<")) {
			xmlElem = YFCDocument.getDocumentFor(input).getDocumentElement();
		} else {
			xmlElem = YFCDocument.getDocumentFor(XMLTranslatorUtil.getXmlFromString(input)).getDocumentElement();
		}
	}
	
	public SimpleXML(YFCDocument xmlDoc) {
		this.xmlElem = xmlDoc.getDocumentElement();
	}
	
	public SimpleXML(YFCElement xmlElem) {
		this.xmlElem = xmlElem;
	}
	
	public SimpleXML(Document xmlDoc) {
		this.xmlElem = YFCDocument.getDocumentFor(xmlDoc).getDocumentElement();
	}
	
	public SimpleXML(InputStream inputStream) throws IOException, SAXException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document xmlDoc = factory.newDocumentBuilder().parse(inputStream);
		this.xmlElem = YFCDocument.getDocumentFor(xmlDoc).getDocumentElement();
	}
	
	public SimpleXML(File inputFile) throws IOException, SAXException, ParserConfigurationException {
		this(new FileInputStream(inputFile));
	}
	
	public String getAttribute(String name) {
		return xmlElem.getAttribute(name);
	}
	
	public Date getDateAttribute(String name) {
		return xmlElem.getDateAttribute(name);
	}
	
	public int getIntAttribute(String name) {
		return xmlElem.getIntAttribute(name);
	}
	
	public long getLongAttribute(String name) {
		return xmlElem.getLongAttribute(name);
	}
	
	public double getDoubleAttribute(String name) {
		return xmlElem.getDoubleAttribute(name);
	}
	
	public Map getAttributes() {
		return xmlElem.getAttributes();
	}
	
	public String getXPathAttribute(String xpath) {
		return SCXmlUtil.getXpathAttribute((Element)xmlElem.getDOMNode(), xpath);
	}
	
	public void setAttribute(String name, String value) {
		xmlElem.setAttribute(name, value);
	}
	
	public void setAttribute(String name, Date value) {
		xmlElem.setAttribute(name, new YFCDate(value));
	}
	
	public void setAttribute(String name, int value) {
		xmlElem.setAttribute(name, value);
	}
	
	public void setAttribute(String name, long value) {
		xmlElem.setAttribute(name, value);
	}
	
	public void setAttribute(String name, double value) {
		xmlElem.setAttribute(name, value);
	}
	
	public SimpleXML getChild(String name) {
		return new SimpleXML(xmlElem.getChildElement(name));
	}
	
	public ArrayList<SimpleXML> getChildren(String tagName) {
		ArrayList<SimpleXML> children = new ArrayList();
		YFCIterable<YFCElement> iter = xmlElem.getChildren(tagName);
		while (iter.hasNext()) {
			YFCElement elem = (YFCElement)iter.next();
			children.add(new SimpleXML(elem));
		}
		return children;
	}
	
	public void addChild(SimpleXML child) {
		xmlElem.importNode(child.getElement());
	}
	
	public String getName() {
		return xmlElem.getNodeName();
	}
	
	public YFCElement getElement() {
		return xmlElem;
	}
	
	public YFCDocument getDocument() {
		return YFCDocument.getDocumentFor(toString());
	}
	
	public Document getDOMDocument() {
		Document result = SCXmlUtil.createDocument(xmlElem.getNodeName());
		//Document result = XMLUtil.createDocument(xmlElem.getNodeName());
		Element resultElement = result.getDocumentElement();
		deepCopyElement(xmlElem, resultElement, result);
		return result;
	}
	
	public static void deepCopyElement(YFCElement source, Element target, Document targetDoc) {
		Iterator attrIter = source.getAttributes().keySet().iterator();
		while (attrIter.hasNext()) {
			String attributeName = (String)attrIter.next();
			String attributeValue = source.getAttribute(attributeName);
			target.setAttribute(attributeName, attributeValue);
		}
		
		YFCIterable childElementIter = source.getChildren();
		while (childElementIter.hasNext()) {
			YFCElement sourceChild = (YFCElement)childElementIter.next();
			Element newChild = targetDoc.createElement(sourceChild.getNodeName());
			target.appendChild(newChild);
			deepCopyElement(sourceChild, newChild, targetDoc);
		}
	}
	
	public static SimpleXML getInstance(String input) {
		return new SimpleXML(input);
	}
	
	public static SimpleXML getInstanceByFile(String fileName) throws FileNotFoundException, IOException, SAXException, ParserConfigurationException {
		return new SimpleXML(new FileInputStream(fileName));
	}
	
	public static SimpleXML getInstanceByResource(String fileName) throws IOException, SAXException, ParserConfigurationException {
		return new SimpleXML((new QuickQuery()).getClass().getClassLoader().getResourceAsStream(fileName));
	}
	
	public String toString() {
		return xmlElem.getString();
	}
}
