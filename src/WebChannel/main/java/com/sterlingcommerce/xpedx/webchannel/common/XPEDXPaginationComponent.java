package com.sterlingcommerce.xpedx.webchannel.common;

import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.struts2.components.Component;

import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.util.ValueStack;

public class XPEDXPaginationComponent extends Component {

	private static final Logger LOGGER = Logger.getLogger("com/sterlingcommerce/webchannel/common/PaginationComponent");

	private Integer m_currentPage;
	private Integer m_lastPage;
	private String m_urlSpec;
	private String m_cssClass;
	private Boolean m_isLastUnknown;
	private Integer m_numSpan;
	private Boolean m_showFirstAndLast;
	private Boolean m_showMyUserFormat;
	private Integer startTabIndex;
	private int tabindex;
	private Boolean m_isAjax;
	private String m_divId;

	public XPEDXPaginationComponent(ValueStack stack) {
		super(stack);
		m_currentPage = null;
		m_lastPage = null;
		m_urlSpec = null;
		m_cssClass = null;
		m_isLastUnknown = null;
		m_numSpan = Integer.valueOf(5);
		m_showFirstAndLast = null;
		m_showMyUserFormat = null;
		startTabIndex = null;
		tabindex = -1;
		m_isAjax = false;
	}

	@Override
	public boolean start(Writer writer) {
		int currentPage = getCurrentPage().intValue();
		Integer endPage = getLastPage();
		String urlSpec = getUrlSpec();
		Integer numSpan = getNumSpan();
		if (numSpan == null) {
			numSpan = Integer.valueOf(5);
		}
		Boolean showFirstAndLast = getShowFirstAndLast();
		if (showFirstAndLast == null) {
			showFirstAndLast = Boolean.valueOf(true);
		}
		Boolean showMyUserFormat = getShowMyUserFormat();
		if (showMyUserFormat == null) {
			showMyUserFormat = Boolean.valueOf(true);
		}

		if (startTabIndex != null) {
			tabindex = startTabIndex.intValue();
		}

		try {
			boolean hasNext = false;
			boolean hasPrev = currentPage > 1;
			Boolean maxUnknown = getLastUnknown();
			if (endPage == null || maxUnknown != null && maxUnknown.booleanValue()) {
				hasNext = true;
				printRootElementOpen(writer, getId(), getCssClass());
				printPageRef(writer, hasPrev, decodeBrackets(urlSpec), currentPage - 1, getText("prev"));
				printPageRef(writer, hasNext, decodeBrackets(urlSpec), currentPage + 1, getText("next"));
				printRootElementClose(writer);

			} else {
				hasNext = endPage.intValue() > currentPage;
				boolean hasRange = (endPage != null) & (endPage.intValue() > 1);
				if (hasRange) {
					printRootElementOpen(writer, getId(), getCssClass());
					if (showFirstAndLast.booleanValue()) {
						printPageRef(writer, hasPrev, decodeBrackets(urlSpec), 1, getText("first"));
					}
					if (showMyUserFormat.booleanValue()) {
						printPageRef(writer, hasPrev, decodeBrackets(urlSpec), 1, getText("<<"));
					}
					printPageRef(writer, hasPrev, decodeBrackets(urlSpec), currentPage - 1, getText("prev"));
					int startRange;
					int endRange;
					if (endPage.intValue() <= numSpan.intValue()) {
						startRange = 1;
						endRange = endPage.intValue();
					} else {
						int proposedStartRange = Math.max(1, currentPage - numSpan.intValue() / 2);
						endRange = Math.min((proposedStartRange + numSpan.intValue()) - 1, endPage.intValue());
						startRange = (endRange - numSpan.intValue()) + 1;
					}
					for (int i = startRange; i <= endRange; i++) {
						printPageRef(writer, i != currentPage, decodeBrackets(urlSpec), i);
					}

					printPageRef(writer, hasNext, decodeBrackets(urlSpec), currentPage + 1, getText("next"));
					if (showMyUserFormat.booleanValue()) {
						printPageRef(writer, endPage.intValue() != currentPage, decodeBrackets(urlSpec), endPage.intValue(), getText(">>"));
					}
					if (showFirstAndLast.booleanValue()) {
						printPageRef(writer, endPage.intValue() != currentPage, decodeBrackets(urlSpec), endPage.intValue(), getText("last"));
					}
					printRootElementClose(writer);
				}
			}

		} catch (IOException e) {
			LOGGER.error(e, e);
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
		out.append("<span");
		if (id != null) {
			out.append(" id=\"").append(id).append("\"");
		}
		if (cssClass != null) {
			out.append(" class=\"").append(cssClass).append("\"");
		}
		out.append(">");
	}

	private void printRootElementClose(Writer out) throws IOException {
		out.append("</span>");
	}

	private void printPageRef(Writer out, boolean makeLink, String urlSpec, int pageNumber) throws IOException {
		printPageRef(out, makeLink, urlSpec, pageNumber, (new StringBuilder()).append("").append(pageNumber).toString());
	}

	private void printPageRef(Writer out, boolean makeLink, String urlSpec, int pageNumber, String label) throws IOException {
		if (makeLink && urlSpec != null) {
			String url = MessageFormat.format(urlSpec, new Object[] { Integer.valueOf(pageNumber) });
			if (getIsAjax()) {
				out.append(" <a href=\"#\" tabindex=\"").append(startTabIndex == null ? "" : Integer.toString(tabindex++)).append("\" onclick=\"javascript: callAjaxForPagination('").append(url).append("','").append(getDivId()).append("'); return false;\" >")
						.append(label)
						.append("</a>");
			} else {
				out.append(" <a href=\"").append(url).append("\" tabindex=\"").append(startTabIndex == null ? "" : ((CharSequence) (Integer.toString(tabindex++)))).append("\">")
						.append(label)
						.append("</a>");
			}

		} else {
			out.append(" ").append(label);
		}
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

	public String getUrlSpec() {
		return m_urlSpec;
	}

	public void setUrlSpec(String urlSpec) {
		m_urlSpec = urlSpec;
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

	public Integer getNumSpan() {
		return m_numSpan;
	}

	public void setNumSpan(Integer numSpan) {
		m_numSpan = numSpan;
	}

	public Boolean getShowFirstAndLast() {
		return m_showFirstAndLast;
	}

	public void setShowFirstAndLast(Boolean showFirstAndLast) {
		m_showFirstAndLast = showFirstAndLast;
	}

	public Boolean getShowMyUserFormat() {
		return m_showMyUserFormat;
	}

	public void setShowMyUserFormat(Boolean showMyUserFormat) {
		m_showMyUserFormat = showMyUserFormat;
	}

	public Integer getStartTabIndex() {
		return startTabIndex;
	}

	public void setStartTabIndex(Integer startTabIndex) {
		this.startTabIndex = startTabIndex;
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

	private String getText(String key) {
		TextProvider tp = findProviderInStack();
		if (tp != null) {
			return tp.getText(key);
		} else {
			LOGGER.warn("No text provider in value stack. Returning key.");
			return key;
		}
	}

	private TextProvider findProviderInStack() {
		for (Iterator iterator = getStack().getRoot().iterator(); iterator.hasNext();) {
			Object o = iterator.next();
			if (o instanceof TextProvider) {
				return (TextProvider) o;
			}
		}

		return null;
	}

}
