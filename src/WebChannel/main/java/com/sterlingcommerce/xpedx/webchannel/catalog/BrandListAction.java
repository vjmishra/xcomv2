
package com.sterlingcommerce.xpedx.webchannel.catalog;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.webchannel.catalog.CatalogAction;
import com.sterlingcommerce.webchannel.common.Breadcrumb;
import com.sterlingcommerce.webchannel.common.BreadcrumbHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;

import edu.emory.mathcs.backport.java.util.Collections;

/*
 * Action class for the brands page. This page lists brands for particular CAT1 / CAT2.
 * The brands on this page link to the catalog, specifying the brand selected along with
 * a bcs specifying the CAT1 and CAT2, so that the resulting list will be filtered and
 * the catalog page will be set up correctly including breadcrumb.
 */
public class BrandListAction extends CatalogAction {

	// [yfs_item_attr.item_attr_name='FF_1182', item_attr_description='Brand']
	private static final String INDEX_FIELD_NAME_BRAND = "ItemAttribute.xpedx.FF_1182";

	private static final Logger log = Logger.getLogger(BrandListAction.class);

	private Map<String,List<String>> brandMap;
	// these are set by input param
	private String path = "";
	private String cat1name = "";
	private String cat2name = "";
	private String bcs = "";


	@Override
	public String execute() {

		// Make brand names for this cat1/cat2 available for JSP access
		brandMap = getBrandListFromApi();

		// Define bcs to be used for all brand links
		bcs = createBreadcrumbForCategories();

		return SUCCESS;
	}

	private Map<String, List<String>> getBrandListFromApi() {
		try {
			Map<String,String> valueMapinput = new HashMap<String,String>();
			valueMapinput.put("/SearchCatalogIndex/@CallingOrganizationCode", wcContext.getStorefrontId());
			valueMapinput.put("/SearchCatalogIndex/@CategoryPath", path);
			valueMapinput.put("/SearchCatalogIndex/Item/CustomerInformation/@CustomerID", wcContext.getCustomerId());
			valueMapinput.put("/SearchCatalogIndex/ShowAllAssignedValues/ItemAttribute/@IndexFieldName", INDEX_FIELD_NAME_BRAND);
			Element inputDoc = WCMashupHelper.getMashupInput("xpedxGetBrands", valueMapinput, wcContext.getSCUIContext());

			Object outputObj = WCMashupHelper.invokeMashup("xpedxGetBrands", inputDoc, wcContext.getSCUIContext());

			setOutDoc(((Element) outputObj).getOwnerDocument()); // base class

			return getBrandListFromOutput();
		}
		catch (Exception e) {
			log.error("Problem getting brand list for this customer & cat1/cat2", e);
		}
		return new HashMap<String, List<String>>(0);
	}

	private Map<String, List<String>> getBrandListFromOutput() {

		ArrayList<String> brandList = new ArrayList<String>(100);
		NodeList FacetList = SCXmlUtil.getXpathNodes(getOutDoc().getDocumentElement(), "/CatalogSearch/FacetList/ItemAttribute");

		// (should be exactly one entry for brand facet)
		for (int i = 0; i < FacetList.getLength(); i++) {
			Element facetElem = (Element) FacetList.item(i);

			List<Element> values = SCXmlUtil.getElements(facetElem, "AssignedValueList/AssignedValue");

			List<String> brands = new ArrayList<String>(values.size());
			for (Element value : values) {
				brands.add(value.getAttribute("Value"));
			}

			brandList.addAll(brands);
		}

		return splitBrandsByLetter(brandList);
	}

	private Map<String, List<String>> splitBrandsByLetter(ArrayList<String> brandList) {
		Map<String, List<String>> map = new LinkedHashMap<String, List<String>>(27);

		Collections.sort(brandList, String.CASE_INSENSITIVE_ORDER);

		// create entry for all letters/# since want empty ones too
		map.put("#", new ArrayList<String>());
		for (char c='A'; c<= 'Z'; c++) {
			map.put(String.valueOf(c), new ArrayList<String>());
		}

		// add each brand to appropriate letter's list
		for (String brand: brandList) {
			char letter = brand.charAt(0);
			List<String> aList;

			if (letter >= 'A' && letter <= 'Z')
				aList = map.get(String.valueOf(letter));
			else
				aList = map.get("#");

			aList.add(brand);
		}

		return map;
	}

	private String createBreadcrumbForCategories() {

		// 3-part breadcrumb for this contains "Catalog", cat1, cat2
		List<Breadcrumb> bcList = new ArrayList<Breadcrumb>(3);

		Breadcrumb root = new Breadcrumb(null, null, null);
		root.setRoot(true);
		root.setUrl("/swc/catalog/navigate.action?sfId=" + wcContext.getStorefrontId() + "&scFlag=Y");
		root.setGroup("catalog");
		root.setDisplayGroup("search");
		root.setDisplayName(null);
		bcList.add(root);

		Map<String, String> params1 = new LinkedHashMap<String, String>();
		String path1 = path.substring(0, path.lastIndexOf('/'));
		params1.put("path", path1);
		params1.put("cname", cat1name);
		params1.put("newOP", "true");
		params1.put("CategoryC3", "true");
		Breadcrumb bc1 = createCatBreadCrumb(params1, cat1name);
		bcList.add(bc1);

		Map<String, String> params2 = new LinkedHashMap<String, String>();
		params2.put("path", path);
		params2.put("cname", cat2name);
		Breadcrumb bc2 = createCatBreadCrumb(params2, cat2name);
		bcList.add(bc2);

		String bcs = BreadcrumbHelper.serializeBreadcrumb(bcList);

		try {
			// s:url will encode, so decode
			return URLDecoder.decode(bcs, "utf-8");
		}
		catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	private Breadcrumb createCatBreadCrumb(Map<String, String> params, String catName) {
		Breadcrumb bc1 = new Breadcrumb("/catalog", "navigate", params);
		bc1.setUrl("");
		bc1.setGroup("catalog");
		bc1.setDisplayGroup("search");
		bc1.setDisplayName(catName);
		return bc1;
	}


	// Need to round up int to nearest int
	public int getNumRows(int size) {
		return (int)Math.ceil(size / 4.0);
	}


	/**** setters and getters ****/

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

	public Map<String, List<String>> getBrands() {
		return brandMap;
	}

	public String getBcs() {
		return bcs;
	}

}

