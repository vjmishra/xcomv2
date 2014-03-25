
package com.sterlingcommerce.xpedx.webchannel.catalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

	// [yfs_item_attr.item_attr_name='FF_1182', item_attr_description='Brand']
	private static final String INDEX_FIELD_NAME_BRAND = "ItemAttribute.xpedx.FF_1182";

	private static final Logger log = Logger.getLogger(XPEDXBrandListAction.class);

	private Map<String,List<String>> brandMap;
	// these are set by input param
	private String path = "";
	private String cat1name = ""; // TODO use these for bread crumb in jsp, and bcs in links
	private String cat2name = "";


	public String getBrandsForCategory() {

		getBrandListFromApi();

		return SUCCESS;
	}

	private void getBrandListFromApi() {
		try {
			Map<String,String> valueMapinput = new HashMap<String,String>();
			valueMapinput.put("/SearchCatalogIndex/@CallingOrganizationCode", wcContext.getStorefrontId());
			valueMapinput.put("/SearchCatalogIndex/@CategoryPath", path);
			valueMapinput.put("/SearchCatalogIndex/Item/CustomerInformation/@CustomerID", wcContext.getCustomerId());
			valueMapinput.put("/SearchCatalogIndex/ShowAllAssignedValues/ItemAttribute/@IndexFieldName", INDEX_FIELD_NAME_BRAND);
			Element inputDoc = WCMashupHelper.getMashupInput("xpedxGetBrands", valueMapinput, wcContext.getSCUIContext());

			System.out.println("input xml: " + SCXmlUtil.getString(inputDoc));

			Object outputObj = WCMashupHelper.invokeMashup("xpedxGetBrands", inputDoc, wcContext.getSCUIContext());
			Document outputDoc = ((Element) outputObj).getOwnerDocument();
			setOutDoc(outputDoc);

			System.out.println("output xml: " + SCXmlUtil.getString(outputDoc)); //TODO remove println

			// extract list of brand names from returned doc for JSP access
			setBrandListFromOutput();
		}
		catch (Exception e) {
			log.error("Problem getting brand list for this customer & cat1/cat2", e);
		}
	}

	private void setBrandListFromOutput() {
		brandMap = new HashMap<String, List<String>>();
		ArrayList<String> brandList = new ArrayList<String>();

		// Need this loop or just grab 1st/only entry (for brand facet)?
		NodeList FacetList = SCXmlUtil.getXpathNodes(getOutDoc().getDocumentElement(), "/CatalogSearch/FacetList/ItemAttribute");
		for (int i = 0; i < FacetList.getLength(); i++) {
			Element facetElem = (Element) FacetList.item(i);

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
		HashMap<String, List<String>> map = new LinkedHashMap<String, List<String>>();

		Collections.sort(brandList, String.CASE_INSENSITIVE_ORDER);

		// create entry for all letters/# since want empty ones too
		map.put("#", new ArrayList<String>());
		for (char c='A'; c<= 'Z'; c++) {
			map.put(String.valueOf(c), new ArrayList<String>());
		}

		for (String brand: brandList) {
			char letter = brand.charAt(0);
			if (letter >= 'A' && letter <= 'Z') {
				// should be unique so shouldn't already be in list
				map.get(String.valueOf(letter)).add(brand);
			}
			else {
				map.get("#").add(brand);
			}
		}

		return map;
	}

	// Need to round up int to nearest int
	public int getNumRows(int size) {
		return (int)Math.ceil(size / 4.0);
	}

	public Map<String, List<String>> getBrands() {
		return brandMap;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public void setPath(String path) {
		this.path = path;
	}

	public String getCat1name() {
		return cat1name;
	}

	public void setCat1name(String cat1name) {
		this.cat1name = cat1name;
	}

	public String getCat2name() {
		return cat2name;
	}

	public void setCat2name(String cat2name) {
		this.cat2name = cat2name;
	}

}

