
package com.sterlingcommerce.xpedx.webchannel.catalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.catalog.CatalogAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;

import edu.emory.mathcs.backport.java.util.Collections;

public class XPEDXBrandListAction extends CatalogAction {

	// TODO Is this OK hard-coded or need to look up?
	//  [yfs_item_attr.item_attr_name='FF_1182', item_attr_description='Brand']
	private static final String ITEM_ATTRIBUTE_KEY_BRAND = "20121030115421135299675";

	private static final Logger log = Logger.getLogger(XPEDXBrandListAction.class);

	private Map<String,List<String>> brandMap;


	public String getBrandsForCategory() {

		String retVal = SUCCESS;

		try {
			Element inputDoc = WCMashupHelper.getMashupInput("xpedxGetBrands", wcContext.getSCUIContext());
			System.out.println("initial xml: " + SCXmlUtil.getString(inputDoc));

			Element allAssignedListElem = SCXmlUtil.createChild(inputDoc, "ShowAllAssignedValues");

			//TODO move to Binding?
			allAssignedListElem.setAttribute("ConsiderOnlyAllAssignedValueAttributes", "Y");

			//TODO need to set this here or in Binding? sortField="Item.ExtnBestMatch--A"

			//TODO move to Binding since hard-coded?
			Element itemAttributeElem = SCXmlUtil.createChild(allAssignedListElem, "ItemAttribute");
			itemAttributeElem.setAttribute("ItemAttributeKey", ITEM_ATTRIBUTE_KEY_BRAND);

			System.out.println("input xml: " + SCXmlUtil.getString(inputDoc));

			Object outputObj = WCMashupHelper.invokeMashup("xpedxGetBrands", inputDoc, wcContext.getSCUIContext());
			Document outputDoc = ((Element) outputObj).getOwnerDocument();
			setOutDoc(outputDoc);

			System.out.println("output xml: " + SCXmlUtil.getString(outputDoc)); //TODO remove println

			// extract list of brand names from returned doc for jsp access
			setBrandListFromOutput();

		}
		catch (Exception e) {
			log.error("Exception in XPEDXBrandListAction - getBrandsForCategory method", e);
		}
		 System.out.println("# brands: " + brandMap.size());
		return retVal;
	}

	private void setBrandListFromOutput() {
		brandMap = new HashMap<String, List<String>>();
		ArrayList<String> brandList = new ArrayList<String>();

		// Need this loop or just grab 1st/only entry (for brand facet)?
		NodeList FacetList = SCXmlUtil.getXpathNodes(getOutDoc().getDocumentElement(), "/CatalogSearch/FacetList/ItemAttribute");
		for (int i = 0; i < FacetList.getLength(); i++) {
			Element facetElem = (Element) FacetList.item(i);
			//TODO any reason to make sure this is the brand facet?
//			Element itemattr = SCXmlUtil.getChildElement(facetElem, "Attribute");
//			String shortDesc = itemattr.getAttribute("ShortDescription");
//			if (facetDivShortDescription != null && !facetDivShortDescription.equalsIgnoreCase(shortDesc)) {
//				continue;
//			}

			List<Element> values = SCXmlUtil.getElements(facetElem, "AssignedValueList/AssignedValue");

			List<String> brands = new ArrayList<String>();
			for (Element value : values) {
				brands.add(value.getAttribute("Value"));
			}
			brandList.addAll(brands);

		}

		brandMap = splitBrandsByLetter(brandList);

	}

	private Map<String, List<String>> splitBrandsByLetter(ArrayList<String> brandList) {
		HashMap<String, List<String>> map = new HashMap<String, List<String>>();

		// returned list appears sorted but in case it's not
		Collections.sort(brandList);

		//TODO loop over brands and put into list keyed by first letter

		return map;
	}

	public Map<String, List<String>> getBrands() {
		return brandMap;
	}

	public void setBrands(Map<String, List<String>> brandMap) {
		this.brandMap = brandMap;
	}
}

