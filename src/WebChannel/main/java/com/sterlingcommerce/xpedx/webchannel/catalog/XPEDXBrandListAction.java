
package com.sterlingcommerce.xpedx.webchannel.catalog;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import com.sterlingcommerce.webchannel.common.Breadcrumb;
import com.sterlingcommerce.webchannel.common.BreadcrumbHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;

import edu.emory.mathcs.backport.java.util.Collections;

public class XPEDXBrandListAction extends CatalogAction {

	// [yfs_item_attr.item_attr_name='FF_1182', item_attr_description='Brand']
	private static final String INDEX_FIELD_NAME_BRAND = "ItemAttribute.xpedx.FF_1182";

	private static final Logger log = Logger.getLogger(XPEDXBrandListAction.class);

	private Map<String,List<String>> brandMap;
	// these are set by input param
	private String path = "";
	private String cat1name = "";
	private String cat2name = "";
	private String bcs = "";


	public String getBrandsForCategory() {

		getBrandListFromApi();

		bcs = createBreadcrumbForCategories();

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

			Object outputObj = WCMashupHelper.invokeMashup("xpedxGetBrands", inputDoc, wcContext.getSCUIContext());
			Document outputDoc = ((Element) outputObj).getOwnerDocument();
			setOutDoc(outputDoc);

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

	public String createBreadcrumbForCategories() {
		List<Breadcrumb> bcl = new ArrayList<Breadcrumb>(3);

		Breadcrumb root = new Breadcrumb(null, null, null);
		root.setRoot(true);
		root.setUrl("/swc/catalog/navigate.action?sfId=xpedx&scFlag=Y");
		root.setGroup("catalog");
		root.setDisplayGroup("search");
		root.setDisplayName(null);
		bcl.add(root);

		Map<String, String> params1 = new LinkedHashMap<String, String>();
		String path1 = path.substring(0, path.lastIndexOf('/'));
		params1.put("path", path1);
		params1.put("cname", cat1name);
		params1.put("newOP", "true");
		params1.put("CategoryC3", "true");
		Breadcrumb bc1 = new Breadcrumb("/catalog", "navigate", params1);
		bc1.setUrl("");
		bc1.setGroup("catalog");
		bc1.setDisplayGroup("search");
		bc1.setDisplayName(cat1name);
		bcl.add(bc1);

		Map<String, String> params2 = new LinkedHashMap<String, String>();
		params2.put("path", path);
		params2.put("cname", cat2name);
		Breadcrumb bc2 = new Breadcrumb("/catalog", "navigate", params2);
		bc2.setUrl("");
		bc2.setGroup("catalog");
		bc2.setDisplayGroup("search");
		bc2.setDisplayName(cat2name);
		bcl.add(bc2);

		String bcs = BreadcrumbHelper.serializeBreadcrumb(bcl);

		try {
			// s:url will encode, so decode
			return URLDecoder.decode(bcs, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
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

	public String getBcs() {
		return bcs;
	}

}

