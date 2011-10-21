package com.sterlingcommerce.xpedx.webchannel.common;
import com.opensymphony.xwork2.util.ValueStack;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ComponentTagSupport;

public class XPEDXSortingTag extends ComponentTagSupport
{
    public XPEDXSortingTag()
    {
        m_cssClass = null;
        m_sortDirection = null;
        m_sortField = null;
        m_sortFieldUpClass = "sort-field-up";
        m_sortFieldDownClass = "sort-field-down";
        m_urlSpec = null;
        m_up = "ascending";
        m_down = "descending";
        m_isAjax=false;
    }
    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res)
    {

        XPEDXSortingComponent sortControlComponent = new XPEDXSortingComponent(stack);

        req.setAttribute("SortControlComponent", sortControlComponent);
        return sortControlComponent;
    }
    protected void populateParams()
    {

        XPEDXSortingComponent sortControlComponent = (XPEDXSortingComponent)component;

        LOGGER.debug((new StringBuilder()).append("SortControlTag.populateParams: cssClass:").append(m_cssClass).toString());

        LOGGER.debug((new StringBuilder()).append("SortControlTag.populateParams: sortDirection:").append(m_sortDirection).toString());

        LOGGER.debug((new StringBuilder()).append("SortControlTag.populateParams: sortField:").append(m_sortField).toString());

        LOGGER.debug((new StringBuilder()).append("SortControlTag.populateParams: sortFieldUpClass:").append(m_sortFieldUpClass).toString());

        LOGGER.debug((new StringBuilder()).append("SortControlTag.populateParams: sortFieldDownClass:").append(m_sortFieldDownClass).toString());

        LOGGER.debug((new StringBuilder()).append("SortControlTag.populateParams: urlSpec:").append(m_urlSpec).toString());

        LOGGER.debug((new StringBuilder()).append("SortControlTag.populateParams: up:").append(m_up).toString());

        LOGGER.debug((new StringBuilder()).append("SortControlTag.populateParams: down:").append(m_down).toString());
        
        LOGGER.debug((new StringBuilder()).append("SortControlTag.populateParams: isAjax:").append(m_isAjax).toString());
        LOGGER.debug((new StringBuilder()).append("SortControlTag.populateParams: idvId:").append(m_divId).toString());

        XPEDXSortingComponent sortingComponent =  (XPEDXSortingComponent)component;
        sortingComponent.setCssClass(m_cssClass);
        sortingComponent.setSortDirection(m_sortDirection);
        sortingComponent.setSortField(m_sortField);
        sortingComponent.setSortFieldUpClass(m_sortFieldUpClass);
        sortingComponent.setSortFieldDownClass(m_sortFieldDownClass);
        sortingComponent.setUrlSpec(m_urlSpec);
        sortingComponent.setUp(m_up);
        sortingComponent.setDown(m_down);
        sortingComponent.setIsAjax(m_isAjax);
        sortingComponent.setDivId(m_divId);
    }
    public String getCssClass()
    {
/*  75*/        return m_cssClass;
    }
    public void setCssClass(String cssClass)
    {
/*  79*/        m_cssClass = cssClass;
    }
    public String getSortDirection()
    {
        return m_sortDirection;
    }
    public void setSortDirection(String sortDirection)
    {
        m_sortDirection = (String)findValue(sortDirection);
    }
    public String getSortField()
    {
        return m_sortField;
    }
    public void setSortField(String sortField)
    {
        m_sortField = (String)findValue(sortField);
    }
    public String getSortFieldUpClass()
    {

        return m_sortFieldUpClass;
    }
    public void setSortFieldUpClass(String sortFieldUpClass)
    {
        m_sortFieldUpClass = sortFieldUpClass;
    }
    public String getSortFieldDownClass()
    {
        return m_sortFieldDownClass;
    }
    public void setSortFieldDownClass(String sortFieldDownClass)
    {
        m_sortFieldDownClass = sortFieldDownClass;
    }
    public String getUrlSpec()
    {
        return m_urlSpec;
    }
    public void setUrlSpec(String urlSpec)
    {
        m_urlSpec = URLDecoder.decode((String)findValue(urlSpec));
    }
    public String getUp()
    {
        return m_up;
    }
    public void setUp(String up)
    {
        m_up = up;
    }
    public String getDown()
    {
        return m_down;
    }
    public void setDown(String down)
    {
        m_down = down;
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

    private static final Logger LOGGER = Logger.getLogger(XPEDXSortingTag.class);
    private String m_cssClass;
    private String m_sortDirection;
    private String m_sortField;
    private String m_sortFieldUpClass;
    private String m_sortFieldDownClass;
    private String m_urlSpec;
    private String m_up;
    private String m_down;
    private Boolean m_isAjax;
    private String m_divId;
}

