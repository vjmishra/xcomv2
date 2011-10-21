package com.sterlingcommerce.xpedx.webchannel.common;

	import com.opensymphony.xwork2.util.ValueStack;
	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;
	import org.apache.log4j.Logger;
	import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ComponentTagSupport;

	// Referenced classes of package com.sterlingcommerce.webchannel.common:
//	            PaginationComponent

	public class XPEDXPaginationTag extends ComponentTagSupport
	{

	    public XPEDXPaginationTag()
	    {
	        m_currentPage = null;
	        m_lastPage = null;
	        m_numSpan = null;
	        m_urlSpec = null;
	        m_cssClass = null;
	        m_isLastUnknown = null;
	        m_showFirstAndLast = null;
	        m_showMyUserFormat = null;
	        startTabIndex = null;
	        m_isAjax=false;
	    }

	    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res)
	    {
	        return new XPEDXPaginationComponent(stack);
	    }

	    protected void populateParams()
	    {
	        super.populateParams();
	        LOGGER.debug((new StringBuilder()).append("PaginationTag.populateParams: currentPage:").append(m_currentPage).toString());
	        LOGGER.debug((new StringBuilder()).append("PaginationTag.populateParams: lastPage:").append(m_lastPage).toString());
	        LOGGER.debug((new StringBuilder()).append("PaginationTag.populateParams: urlSpec:").append(m_urlSpec).toString());
	        LOGGER.debug((new StringBuilder()).append("PaginationTag.populateParams: cssClass:").append(m_cssClass).toString());
	        LOGGER.debug((new StringBuilder()).append("PaginationTag.populateParams: numSpan:").append(m_numSpan).toString());
	        LOGGER.debug((new StringBuilder()).append("PaginationTag.populateParams: isLastUnknown:").append(m_isLastUnknown).toString());
	        LOGGER.debug((new StringBuilder()).append("PaginationTag.populateParams: showFirstAndLast:").append(m_showFirstAndLast).toString());
	        LOGGER.debug((new StringBuilder()).append("PaginationTag.populateParams: showMyUserFormat:").append(m_showMyUserFormat).toString());
	        LOGGER.debug((new StringBuilder()).append("PaginationTag.populateParams: isAjax:").append(m_isAjax).toString());
	        LOGGER.debug((new StringBuilder()).append("PaginationTag.populateParams: idvId:").append(m_divId).toString());
	        XPEDXPaginationComponent paginationComponent = (XPEDXPaginationComponent)component;
	        paginationComponent.setCurrentPage(m_currentPage);
	        paginationComponent.setLastPage(m_lastPage);
	        paginationComponent.setUrlSpec(m_urlSpec);
	        paginationComponent.setCssClass(m_cssClass);
	        paginationComponent.setNumSpan(m_numSpan);
	        paginationComponent.isLastUnknown(m_isLastUnknown);
	        paginationComponent.setShowFirstAndLast(m_showFirstAndLast);
	        paginationComponent.setShowMyUserFormat(m_showMyUserFormat);
	        paginationComponent.setStartTabIndex(startTabIndex);
	        paginationComponent.setIsAjax(m_isAjax);
	        paginationComponent.setDivId(m_divId);
	    }

	    public void setCurrentPage(String currentPage)
	    {
	        m_currentPage = (Integer)findValue(currentPage, Integer.class);
	    }

	    public void setLastPage(String lastPage)
	    {
	        m_lastPage = (Integer)findValue(lastPage, Integer.class);
	    }

	    public void setNumSpan(Integer numSpan)
	    {
	        m_numSpan = numSpan;
	    }

	    public void setUrlSpec(String urlSpec)
	    {
	        m_urlSpec = (String)findValue(urlSpec);
	    }

	    public void setCssClass(String cssClass)
	    {
	        m_cssClass = cssClass;
	    }

	    public void setLastUnknown(Boolean lastUnknown)
	    {
	        m_isLastUnknown = lastUnknown;
	    }

	    public void setIsLastUnknown(Boolean lastUnknown)
	    {
	        m_isLastUnknown = lastUnknown;
	    }

	    public void setShowFirstAndLast(Boolean showFirstAndLast)
	    {
	        m_showFirstAndLast = showFirstAndLast;
	    }

	    public Integer getStartTabIndex()
	    {
	        return startTabIndex;
	    }

	    public void setStartTabIndex(Integer startTabIndex)
	    {
	        this.startTabIndex = startTabIndex;
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
			if(divId.contains("%{"))
				m_divId = (String)findValue(divId);
			else
				m_divId = divId;
		}

		public Boolean getShowMyUserFormat() {
			return m_showMyUserFormat;
		}

		public void setShowMyUserFormat(Boolean showMyUserFormat) {
			m_showMyUserFormat = showMyUserFormat;
		}

		private static final Logger LOGGER = Logger.getLogger(XPEDXPaginationTag.class);
	    private Integer m_currentPage;
	    private Integer m_lastPage;
	    private Integer m_numSpan;
	    private String m_urlSpec;
	    private String m_cssClass;
	    private Boolean m_isLastUnknown;
	    private Boolean m_showFirstAndLast;
	    private Boolean m_showMyUserFormat;
		private Integer startTabIndex;
	    private Boolean m_isAjax;
	    private String m_divId;

	}
