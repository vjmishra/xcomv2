package com.sterlingcommerce.xpedx.webchannel.common;
import com.opensymphony.xwork2.util.ValueStack;
import com.sterlingcommerce.webchannel.common.SortControlComponent;

import java.io.IOException;
import java.io.Writer;
import org.apache.struts2.components.Component;
public class XPEDXSortingComponent extends Component
{
    public XPEDXSortingComponent(ValueStack valueStack)
    {

        super(valueStack);
        m_sortDirection = null;
        m_sortField = null;
        m_sortFieldUpClass = null;
        m_sortFieldDownClass = null;
        m_urlSpec = null;
        m_up = null;
        m_down = null;
        m_isAjax=false;
    }
    public boolean start(Writer writer)
    {
        String id = getId();
        String cssClass = getCssClass();
        String urlSpec = getUrlSpec();
        try
        {
        	
            writer.write("<div");
            if(id != null)
            {
                writer.write(" id=\"");
                writer.write(id);
                writer.write("\"");
            }
            if(cssClass != null)
            {
                writer.write(" class=\"");
                writer.write(cssClass);
                writer.write("\"");
            }
            /*if(getIsAjax() && urlSpec != null)
            {
            writer.write(" onclick=\"javascript: callAjaxForSorting('" + urlSpec + "','"+getDivId() + "')");
            writer.write("\"");
            }*/
            writer.write(">");
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return super.start(writer);
    }
    public boolean end(Writer writer, String string)
    {
        try
        {
            writer.write("</div>");
        }
       catch(IOException e)
        {
            e.printStackTrace();
        }
       return super.end(writer, string);
    }
    public String getCssClass()
    {
        return m_cssClass;
    }
    public void setCssClass(String cssClass)
    {
        m_cssClass = cssClass;
    }
    public String getSortDirection()
    {
        return m_sortDirection;
    }
    public void setSortDirection(String sortDirection)
    {
        m_sortDirection = sortDirection;
    }
    public String getSortField()
    {
        return m_sortField;
    }
    public void setSortField(String sortField)
    {
        m_sortField = sortField;
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
        m_urlSpec = urlSpec;
    }
    public String getUpToken()
    {
        return m_up;
    }
    public void setUp(String up)
    {
        m_up = up;
    }
    public String getDownToken()
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

     public void setIsAjax(Boolean mIsAjax) {
    	m_isAjax = mIsAjax;
     }

     public String getDivId() {
    		return m_divId;
    	}

    	public void setDivId(String divId) {
    		m_divId = divId;
    	}
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

