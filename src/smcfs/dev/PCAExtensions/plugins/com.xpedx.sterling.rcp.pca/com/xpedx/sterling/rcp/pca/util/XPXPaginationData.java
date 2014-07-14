/**
 * 
 */
package com.xpedx.sterling.rcp.pca.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.eclipse.swt.layout.GridData;
/**
 * @author Krithika S
 *
 */
public class XPXPaginationData {
	//The document to be set in the 'Input' element of getPage API I/P XML
	private Document inputXml;
	//The api name to be called
	private String apiName;
	//To specify whether its an API or Flow(Service) - Set Y to call service
	private String isFlow;
	
	//The following attributes are not used as of now, may be needed later
	private String totalRecordCount;
	private boolean isFirstPage;
	private boolean isLastPage;
	
	//The element to be set with the last record of the previous page when the PaginationStrategy is NEXTPAGE
	private Element previousPageElem;
	//The sortColumn to be specified in the OrderBy element
	private String sortColumn;
	
	//Default values 
	private String sortOrderDesc = "Y";
	private String pageNumber = "1";
	private String pageSize  = "25";
	private String totalNoOfPages = "0";
	private String paginationStrategy = "GENERIC";
	/**
	 * @return the inputXml
	 */
	public Document getInputXml() {
		return inputXml;
	}
	/**
	 * @param inputXml the inputXml to set
	 */
	public void setInputXml(Document inputXml) {
		this.inputXml = inputXml;
	}
	/**
	 * @return the apiName
	 */
	public String getApiName() {
		return apiName;
	}
	/**
	 * @param apiName the apiName to set
	 */
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
	/**
	 * @return the isFlow
	 */
	public String getIsFlow() {
		return isFlow;
	}
	/**
	 * @param isFlow the isFlow to set
	 */
	public void setIsFlow(String isFlow) {
		this.isFlow = isFlow;
	}
	/**
	 * @return the totalRecordCount
	 */
	public String getTotalRecordCount() {
		return totalRecordCount;
	}
	/**
	 * @param totalRecordCount the totalRecordCount to set
	 */
	public void setTotalRecordCount(String totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
	}
	/**
	 * @return the isFirstPage
	 */
	public boolean isFirstPage() {
		return isFirstPage;
	}
	/**
	 * @param isFirstPage the isFirstPage to set
	 */
	public void setFirstPage(boolean isFirstPage) {
		this.isFirstPage = isFirstPage;
	}
	/**
	 * @return the isLastPage
	 */
	public boolean isLastPage() {
		return isLastPage;
	}
	/**
	 * @param isLastPage the isLastPage to set
	 */
	public void setLastPage(boolean isLastPage) {
		this.isLastPage = isLastPage;
	}
	/**
	 * @return the previousPageElem
	 */
	public Element getPreviousPageElem() {
		return previousPageElem;
	}
	/**
	 * @param previousPageElem the previousPageElem to set
	 */
	public void setPreviousPageElem(Element previousPageElem) {
		this.previousPageElem = previousPageElem;
	}
	/**
	 * @return the sortColumn
	 */
	public String getSortColumn() {
		return sortColumn;
	}
	/**
	 * @param sortColumn the sortColumn to set
	 */
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}
	/**
	 * @return the sortOrderDesc
	 */
	public String getSortOrderDesc() {
		return sortOrderDesc;
	}
	/**
	 * @param sortOrderDesc the sortOrderDesc to set
	 */
	public void setSortOrderDesc(String sortOrderDesc) {
		this.sortOrderDesc = sortOrderDesc;
	}
	/**
	 * @return the pageNumber
	 */
	public String getPageNumber() {
		return pageNumber;
	}
	/**
	 * @param pageNumber the pageNumber to set
	 */
	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}
	/**
	 * @return the pageSize
	 */
	public String getPageSize() {
		return pageSize;
	}
	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * @return the totalNoOfPages
	 */
	public String getTotalNoOfPages() {
		return totalNoOfPages;
	}
	/**
	 * @param totalNoOfPages the totalNoOfPages to set
	 */
	public void setTotalNoOfPages(String totalNoOfPages) {
		this.totalNoOfPages = totalNoOfPages;
	}
	/**
	 * @return the paginationStrategy
	 */
	public String getPaginationStrategy() {
		return paginationStrategy;
	}
	/**
	 * @param paginationStrategy the paginationStrategy to set
	 */
	public void setPaginationStrategy(String paginationStrategy) {
		this.paginationStrategy = paginationStrategy;
	}
	
	
}
