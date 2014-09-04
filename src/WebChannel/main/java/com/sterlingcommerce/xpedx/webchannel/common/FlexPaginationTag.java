package com.sterlingcommerce.xpedx.webchannel.common;

import com.opensymphony.xwork2.util.ValueStack;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ComponentTagSupport;

// Referenced classes of package com.sterlingcommerce.webchannel.common:
//	            PaginationComponent

public class FlexPaginationTag extends ComponentTagSupport {

	private static final Logger LOGGER = Logger.getLogger(FlexPaginationTag.class);

	private Integer m_currentPage;
	private Integer m_lastPage;
	private String m_urlSpec;
	private String m_cssClass;
	private Boolean m_isLastUnknown;
	private Boolean m_isAjax;
	private String m_divId;
	private Integer m_totalResults;

	public FlexPaginationTag() {
		m_currentPage = null;
		m_lastPage = null;
		m_urlSpec = null;
		m_cssClass = null;
		m_isLastUnknown = null;
		m_isAjax = false;
		m_totalResults = null;
	}

	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		return new FlexPaginationComponent(stack);
	}

	@Override
	protected void populateParams() {
		super.populateParams();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug((new StringBuilder()).append("PaginationTag.populateParams: currentPage:").append(m_currentPage).toString());
			LOGGER.debug((new StringBuilder()).append("PaginationTag.populateParams: lastPage:").append(m_lastPage).toString());
			LOGGER.debug((new StringBuilder()).append("PaginationTag.populateParams: urlSpec:").append(m_urlSpec).toString());
			LOGGER.debug((new StringBuilder()).append("PaginationTag.populateParams: cssClass:").append(m_cssClass).toString());
			LOGGER.debug((new StringBuilder()).append("PaginationTag.populateParams: isLastUnknown:").append(m_isLastUnknown).toString());
			LOGGER.debug((new StringBuilder()).append("PaginationTag.populateParams: isAjax:").append(m_isAjax).toString());
			LOGGER.debug((new StringBuilder()).append("PaginationTag.populateParams: divId:").append(m_divId).toString());
			LOGGER.debug((new StringBuilder()).append("PaginationTag.populateParams: m_totalResults:").append(m_totalResults).toString());
		}
		FlexPaginationComponent paginationComponent = (FlexPaginationComponent) component;
		paginationComponent.setCurrentPage(m_currentPage);
		paginationComponent.setLastPage(m_lastPage);
		paginationComponent.setUrlSpec(m_urlSpec);
		paginationComponent.setCssClass(m_cssClass);
		paginationComponent.isLastUnknown(m_isLastUnknown);
		paginationComponent.setIsAjax(m_isAjax);
		paginationComponent.setDivId(m_divId);
		paginationComponent.setTotalResults(m_totalResults);
	}

	public void setCurrentPage(String currentPage) {
		m_currentPage = (Integer) findValue(currentPage, Integer.class);
	}

	public void setLastPage(String lastPage) {
		m_lastPage = (Integer) findValue(lastPage, Integer.class);
	}

	public void setUrlSpec(String urlSpec) {
		m_urlSpec = (String) findValue(urlSpec);
	}

	public void setCssClass(String cssClass) {
		m_cssClass = cssClass;
	}

	public void setLastUnknown(Boolean lastUnknown) {
		m_isLastUnknown = lastUnknown;
	}

	public void setIsLastUnknown(Boolean lastUnknown) {
		m_isLastUnknown = lastUnknown;
	}

	public Boolean getIsAjax() {
		return m_isAjax;
	}

	public void setIsAjax(Boolean isAjax) {
		m_isAjax = isAjax;
	}

	public String getDivId() {
		return m_divId;
	}

	public void setDivId(String divId) {
		if (divId.contains("%{")) {
			m_divId = (String) findValue(divId);
		} else {
			m_divId = divId;
		}
	}

}
