package com.xpedx.nextgen.item;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXItemDeleteService implements YIFCustomApi {

	private static YFCLogCategory log;
	private static YIFApi api = null;

	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try {
			api = YIFClientFactory.getInstance().getApi();

			// TODO:createXPXCustomerRulesProfile
		} catch (YIFClientCreationException e1) {

			e1.printStackTrace();
		}
	}

	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * This is the method which gets invoked on the hit of the service. This
	 * forms the complete input xml for manageCustomer api
	 * 
	 * @param env
	 * @param inXML
	 * @return
	 * @throws Exception
	 */
	public Document invokeItemDelete(YFSEnvironment env, Document inXML) throws Exception {
		String fileName = "ItemDelete.xls";
		Element customerElement = inXML.getDocumentElement();
		itemDeleteProcess(fileName, customerElement, env);
		return null;
	}

	/**
	 * This Operation will read data from excel , create customer id and inser
	 * record in custome rule profile table createCustomerRuleProfile
	 * 
	 * @param fileName
	 * @param customerElement
	 * @param env
	 */
	private void itemDeleteProcess(String fileName, Element customerElement, YFSEnvironment env) {
		String workSheetName = customerElement.getAttribute("WorkSheetName");
		String removalIndicator = customerElement.getAttribute("RemovalIndicator");
		Workbook workbook = null;
		HSSFSheet sheet = null;
		InputStream in;
		try {
			String name = YFSSystem.getProperty("itemDeleteProcess.file");
			System.out.println("itemDeleteProcess.file " + name);
			if (name == null || name.isEmpty()) {
				name = "/xpedx/sterling/Foundation/ItemDelete.xls";
			}
			in = new FileInputStream(new File(name));
			log.info("File Stream" + in.toString());

			workbook = WorkbookFactory.create(in);
			sheet = (HSSFSheet) workbook.getSheet(workSheetName);
			int noOfRows = ((org.apache.poi.ss.usermodel.Sheet) sheet).getPhysicalNumberOfRows();
			log.info("Number of excel rows are " + noOfRows);

			String customerId = null;

			String itemId = null;
			String catalogId = null;
			String categoryPath = null;
			String unitOfMeasure = null;
			String globalItemID = null;

			for (int rownum = 0; rownum < noOfRows; rownum++) {
				Row row = ((org.apache.poi.ss.usermodel.Sheet) sheet).getRow(rownum);
				int counter = 0;
				int noOfColumns = row.getLastCellNum();
				for (int colnum = 0; colnum < noOfColumns; colnum++) {

					if (noOfColumns == colnum) {
						break;
					}
					Cell cell = row.getCell(colnum);
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						customerId = cell.getStringCellValue();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						if (DateUtil.isCellDateFormatted(cell)) {
							cell.getDateCellValue();
						} else {
							customerId = String.valueOf(new java.text.DecimalFormat("0").format(cell.getNumericCellValue()));
						}
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						customerId = String.valueOf(cell.getBooleanCellValue());
						break;
					case Cell.CELL_TYPE_FORMULA:
						customerId = cell.getCellFormula();
						break;
					default:
						System.out.println("No Formated Data");
					}

					if (counter == 0) {
						itemId = customerId;
					} else if (counter == 1) {
						unitOfMeasure = customerId;
					} else if (counter == 2) {
						catalogId = customerId;
					} else if (counter == 3) {
						categoryPath = customerId;
					} else if (counter == 4) {
						globalItemID = customerId;
					}
					counter++;

				}
				//Conditon has been added as first need to remove Category Item record and then record of an Item
				if(removalIndicator.equalsIgnoreCase("Item")){
					
				YFCDocument getItemListTemplate = YFCDocument.createDocument("ItemList");
				YFCElement eItemListTemplate = getItemListTemplate.getDocumentElement();
				YFCElement yfcElement = getItemListTemplate.createElement("Item");
				yfcElement.setAttribute("Action", "Delete");
				yfcElement.setAttribute("ItemID", itemId);
				yfcElement.setAttribute("UnitOfMeasure", unitOfMeasure);
				yfcElement.setAttribute("GlobalItemID", globalItemID);
				yfcElement.setAttribute("OrganizationCode", "xpedx");
				eItemListTemplate.appendChild(yfcElement);
				
				log.debug("DeleteItemService: \n" + SCXmlUtil.getString(getItemListTemplate.getDocument()));
				Document itemListOutDoc = api.invoke(env, "manageItem", getItemListTemplate.getDocument());
				log.debug("DeleteItemService: \n" + SCXmlUtil.getString(itemListOutDoc));
				}else{
					YFCDocument getModiftyCategoryItem = YFCDocument.createDocument("ModifyCategoryItems");
					YFCElement eItemListTemplate = getModiftyCategoryItem.getDocumentElement();
					eItemListTemplate.setAttribute("CallingOrganizationCode", "xpedx");
					YFCElement yfcCategoryElement = getModiftyCategoryItem.createElement("Category");
					yfcCategoryElement.setAttribute("CategoryPath", categoryPath);
					yfcCategoryElement.setAttribute("OrganizationCode", "xpedx");
					eItemListTemplate.appendChild(yfcCategoryElement);
					YFCElement yfcCategorItemList = getModiftyCategoryItem.createElement("CategoryItemList");
					YFCElement eCategoryItemTemplate = getModiftyCategoryItem.createElement("CategoryItem");
					eCategoryItemTemplate.setAttribute("Action", "Delete");
					eCategoryItemTemplate.setAttribute("ItemID", itemId);
					eCategoryItemTemplate.setAttribute("UnitOfMeasure", unitOfMeasure);
					eCategoryItemTemplate.setAttribute("GlobalItemID", globalItemID);
					eCategoryItemTemplate.setAttribute("OrganizationCode", "xpedx");
					yfcCategoryElement.appendChild(yfcCategorItemList);
					yfcCategorItemList.appendChild(eCategoryItemTemplate);
					
					log.debug("DeleteItemService: \n" + SCXmlUtil.getString(getModiftyCategoryItem.getDocument()));
					Document itemListOutDoc = api.invoke(env, "modifyCategoryItem", getModiftyCategoryItem.getDocument());
					log.debug("DeleteItemService: \n" + SCXmlUtil.getString(itemListOutDoc));
					
					

				}

			}
			
			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
