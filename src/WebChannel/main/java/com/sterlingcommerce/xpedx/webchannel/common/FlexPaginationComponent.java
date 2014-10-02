package com.sterlingcommerce.xpedx.webchannel.common;

import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.components.Component;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * Customization of Sterling's out-of-the-box pagination component (pagectl).
 * This component removed unwanted parameters (eg, tabindex) and added new parameters:
 * <ul>
 *  <li>isAjax: When true, calls a javascript function to get next page's data (instead of simply navigating to it)</li>
 *  <li>Always show at least first and last pages, and current page +/- 1. Then, if necessary, hide other page numbers with an ellipsis. (eg: 1, ..., 6, 7, 8, ..., 12)</li>
 * </ul>
 */
public class FlexPaginationComponent extends Component {

	private static final Logger LOGGER = Logger.getLogger(FlexPaginationComponent.class);

	private static enum ElementType {
		RESULTS ("", "results"),
		ARROW_LEFT("", "arrow-left"),
		ARROW_RIGHT("", "arrow-right"),
		MORE_PAGES("", "more-pages"),
		OTHER_PAGE("", ""),
		CURRENT_PAGE("active", "");

		private String liCssClass;
		private String spanCssClass;
		private ElementType(String liCssClass, String spanCssClass) {
			this.liCssClass = liCssClass;
			this.spanCssClass = spanCssClass;
		}
	}

	private Integer m_currentPage;
	private Integer m_lastPage;
	private Integer m_totalResults;
	private String m_urlSpec; // note: custom mutator to decode brackets
	private String m_cssClass;
	private Boolean m_isLastUnknown;
	private Boolean m_isAjax;
	private String m_divId;

	public FlexPaginationComponent(ValueStack stack) {
		super(stack);
	}

	@Override
	public boolean start(Writer writer) {
		int currentPage = getCurrentPage();
		Integer endPage = getLastPage();

		try {
			boolean hasNext = false;
			boolean hasPrev = currentPage > 1;
			Boolean maxUnknown = getLastUnknown();
			if (endPage == null || maxUnknown != null && maxUnknown.booleanValue()) {
				hasNext = true;
				printRootElementOpen(writer, getId(), getCssClass());

				printPageElement(writer, false, ElementType.RESULTS, null);

				printPageElement(writer, hasPrev, ElementType.ARROW_LEFT, currentPage - 1);
				printPageElement(writer, hasNext, ElementType.ARROW_RIGHT, currentPage + 1);

				printRootElementClose(writer);

			} else {
				hasNext = endPage > currentPage;
				boolean hasRange = (endPage != null) & (endPage > 1);
				if (hasRange) {
					printRootElementOpen(writer, getId(), getCssClass());

					printPageElement(writer, false, ElementType.RESULTS, null);

					printPageElement(writer, hasPrev, ElementType.ARROW_LEFT, currentPage - 1);

					List<Integer> pageNumbers = getPageNumbers(currentPage, endPage);
					for (Integer pageNumber : pageNumbers) {
						if (pageNumber == null) {
							printPageElement(writer, false, ElementType.MORE_PAGES, null);

						} else {
							boolean makeLink = pageNumber != currentPage;

							printPageElement(writer, makeLink, makeLink ? ElementType.OTHER_PAGE : ElementType.CURRENT_PAGE, pageNumber);
						}
					}

					printPageElement(writer, hasNext, ElementType.ARROW_RIGHT, currentPage + 1);

					printRootElementClose(writer);

				} else if (getTotalResults() != null) {
					// no pagination necessary, just display results
					printRootElementOpen(writer, getId(), getCssClass());

					printPageElement(writer, false, ElementType.RESULTS, null);

					printRootElementClose(writer);
				}
			}

		} catch (IOException e) {
			LOGGER.error("Error occurred writing to JSP", e);
		}

		return true;
	}

	public boolean end(Writer writer) {
		return true;
	}

	@Override
	public boolean usesBody() {
		return false;
	}

	private String decodeBrackets(String s) {
		return s.replaceAll("%7[bB]", "{").replaceAll("%7[dD]", "}");
	}

	private void printRootElementOpen(Writer out, String id, String cssClass) throws IOException {
		out.append("<div");
		if (id != null) {
			out.append(" id='").append(id).append("'");
		}
		out.append(" class='").append("pagination-wrap addmargintop10 addmarginbottom5").append(cssClass == null ? "" : " " + cssClass).append("'");
		out.append(">");
		out.append("	<ul class='pagination pagination-sm floatright'>");
	}

	private void printRootElementClose(Writer out) throws IOException {
		out.append("	</ul>");
		out.append("</div>");
	}

	/**
	 * @param out
	 * @param makeLink If true (and getUrlSpec() is not null) then an anchor tag will be created.
	 * @param type Indicates the element's type (arrow, page, etc)
	 * @param pageNumber The page number to include in the link. Pass null if page number not relevant (type=RESULTS, MORE_PAGE, etc)
	 * @throws IOException
	 */
	private void printPageElement(Writer out, boolean makeLink, ElementType type, Integer pageNumber) throws IOException {
		String spanCssClass = type.spanCssClass;
		if (type == ElementType.ARROW_LEFT && !makeLink) {
			spanCssClass += " inactive";
		}
		if (type == ElementType.ARROW_RIGHT && !makeLink) {
			spanCssClass += " inactive";
		}

		out.append("		<li class='").append(type.liCssClass).append("'>");
		out.append("			<span class='").append(spanCssClass).append("'").append(">");

		final String label;
		switch (type) {
		case RESULTS:
			if (getTotalResults() == null) {
				label = "Page";
			} else {
				label = getTotalResults() + " Results";
			}
			break;
		case ARROW_LEFT:
			label = "&lsaquo;";
			break;
		case ARROW_RIGHT:
			label = "&rsaquo;";
			break;
		case MORE_PAGES:
			label = "...";
			break;
		case OTHER_PAGE:
			label = String.valueOf(pageNumber);
			break;
		case CURRENT_PAGE:
			label = String.valueOf(pageNumber);
			break;
		default:
			throw new IllegalArgumentException("Unexpected type = " + type);
		}

		if (makeLink && getUrlSpec() != null) {
			if (getIsAjax()) {
				String url = MessageFormat.format(getUrlSpec(), pageNumber);
				out.append("		<a href='#' onclick='callAjaxForPagination(\"").append(url).append("\", \"").append(getDivId()).append("\"); return false;' >");
				out.append(				label);
				out.append("		</a>");

			} else {
				String url = MessageFormat.format(getUrlSpec(), pageNumber);
				out.append("		<a href='").append(url).append("'>");
				out.append(				label);
				out.append("		</a>");
			}

		} else {
			out.append(				label);
		}

		out.append("			</span>");
		out.append("		</li>");
	}

	/**
	 * @param currentPage
	 * @param endPage
	 * @return Returns the pages that will be clickable from the page. A null element indicates skipped numbers (shows as ellipsis on the page)
	 */
	public static List<Integer> getPageNumbers(int currentPage, int endPage) {
		boolean needSkip = endPage > 7;
		boolean skipLow = needSkip && (currentPage > 4);
		boolean skipHigh = needSkip && (currentPage < endPage - 3);

		if (endPage == 1) {
			return Collections.singletonList(1);
		}

		List<Integer> displayPages = new ArrayList<Integer>(7);

		displayPages.add(1);

		if (skipLow) {
			displayPages.add(null);
		}

		int startIndex;
		int endIndex;
		if (skipLow && skipHigh) {
			startIndex = Math.max(currentPage - 1, 2);
			endIndex = Math.min(startIndex + 2, endPage - 1);
		} else if (skipLow && !skipHigh) {
			startIndex = Math.min(currentPage - 1, endPage - 4);
			endIndex = endPage - 1;
		} else if (!skipLow && skipHigh) {
			startIndex = 2;
			endIndex = Math.min(startIndex + 3, endPage - 1);
		} else {
			startIndex = 2;
			endIndex = endPage - 1;
		}

		for (int i = startIndex; i <= endIndex; i++) {
			displayPages.add(i);
		}

		if (skipHigh) {
			displayPages.add(null);
		}

		displayPages.add(endPage);

		return displayPages;
	}

	public Integer getCurrentPage() {
		return m_currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		m_currentPage = currentPage;
	}

	public Integer getLastPage() {
		return m_lastPage;
	}

	public void setLastPage(Integer lastPage) {
		m_lastPage = lastPage;
	}

	public Integer getTotalResults() {
		return m_totalResults;
	}

	public void setTotalResults(Integer totalResults) {
		m_totalResults = totalResults;
	}

	public String getUrlSpec() {
		return m_urlSpec;
	}

	public void setUrlSpec(String urlSpec) {
		if (urlSpec != null) {
			m_urlSpec = decodeBrackets(urlSpec);
		}
	}

	public String getCssClass() {
		return m_cssClass;
	}

	public void setCssClass(String cssClass) {
		m_cssClass = cssClass;
	}

	public Boolean getLastUnknown() {
		return m_isLastUnknown;
	}

	public void isLastUnknown(Boolean maxUnknown) {
		m_isLastUnknown = maxUnknown;
	}

	public Boolean getIsAjax() {
		return m_isAjax;
	}

	public void setIsAjax(Boolean mIsAjax) {
		m_isAjax = mIsAjax;
	}

	public String getDivId() {
		return m_divId;
	}

	public void setDivId(String divId) {
		m_divId = divId;
	}

}
