package com.sterlingcommerce.simplifieddataaccess;

import java.util.Stack;

import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;

public class XMLTranslatorUtil {
	
	public static String getXmlFromString(String simpleString) {
		return getXmlDocFromString(simpleString).getString();
	}
	
	public static YFCDocument getXmlDocFromString(String simpleString) {
		//Ignore opening and closing parenthesis
		simpleString = simpleString.trim();
		if ("(".equals(simpleString.substring(0,1)) && ")".equals(simpleString.substring(simpleString.length()-1))) {
			simpleString = simpleString.substring(1, simpleString.length()-1).trim();
		}

		int nextSpace = simpleString.indexOf(" ");
		int nextParan = simpleString.indexOf("(");
		
		String rootElementName = simpleString;
		if (nextSpace==-1 && nextParan>-1) {
			rootElementName = simpleString.substring(0,nextParan);
		} else if (nextSpace>-1 && nextParan==-1) {
			rootElementName = simpleString.substring(0,nextSpace);
		} else if (nextSpace>-1 && nextParan>-1) {
			rootElementName = simpleString.substring(0,Math.min(nextSpace,nextParan));
		}
		
		YFCDocument xmlDoc = YFCDocument.createDocument(rootElementName);
		YFCElement xmlElem = xmlDoc.getDocumentElement();

		//Get all attributes
		int endOfAtt = simpleString.indexOf("(");
		String attributeString = simpleString;
		if (endOfAtt>-1)
			attributeString = simpleString.substring(0, endOfAtt);

		int eqIndex = attributeString.lastIndexOf("=");
		while (eqIndex > -1) {
			String value = attributeString.substring(eqIndex+1);
			attributeString = attributeString.substring(0,eqIndex);
			String[] words = attributeString.split(" ");
			String name = words[words.length-1];
			attributeString = attributeString.substring(0, attributeString.lastIndexOf(name)).trim();
			xmlElem.setAttribute(name, value);
			eqIndex = attributeString.lastIndexOf("=");
		}

		//Get all children elements
		String childString = simpleString;
		int begin = childString.indexOf("(");
		while (begin>-1) {
			childString = childString.substring(begin+1);
			Stack childStack = new Stack();
			childStack.push("*");
			int next = 0;
		
			while (!childStack.empty()) {
				int nextOpen = childString.indexOf("(", next+1);
				int nextClose = childString.indexOf(")", next+1);
				
				if (nextOpen==-1 && nextClose>-1) {
					next = nextClose;
					childStack.pop();
				} else if (nextOpen>-1 && nextClose==-1) {
					next = nextOpen;
					childStack.push("*");
				} else if (nextOpen>-1 && nextClose>-1) {
					next = Math.min(nextOpen, nextClose);
					if (nextOpen<nextClose) {
						childStack.push("*");
					} else {
						childStack.pop();
					}	
				}
			}
			
			String elemString = childString.substring(0, next);
			YFCElement childElem = getXmlDocFromString(elemString).getDocumentElement();
			xmlElem.importNode(childElem);
			
			childString = childString.substring(next+1);
			begin = childString.indexOf("(");
		}

		return xmlDoc;
	}

}

