package com.sterlingcommerce.xpedx.webchannel.common;

import com.opensymphony.xwork2.util.ValueStack;
import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;
import org.apache.log4j.Logger;
import org.apache.struts2.components.Component;


public class XPEDXSortableComponent extends Component
	
	{

	    public XPEDXSortableComponent(ValueStack valueStack, XPEDXSortingComponent sortControl)
	    {
	        super(valueStack);
	        m_fieldname = null;
	        m_sortControlComponent = sortControl;
	    }

	    public boolean start(Writer writer)
	    {
	        if(m_sortControlComponent != null)
	        {
	            boolean isSortField = m_sortControlComponent.getSortField().equals(m_fieldname);
	            String direction = m_sortControlComponent.getSortDirection();
	            String urlspec = m_sortControlComponent.getUrlSpec();
	            String upToken = m_sortControlComponent.getUpToken();
	            String downToken = m_sortControlComponent.getDownToken();
	            String newDirection;
	            String classString;
	            if(direction.equals(upToken))
	            {
	                newDirection = downToken;
	                classString = (new StringBuilder()).append(" class=\"").append(m_sortControlComponent.getSortFieldUpClass()).append("\"").toString();
	            } else
	            {
	                newDirection = upToken;
	                classString = (new StringBuilder()).append(" class=\"").append(m_sortControlComponent.getSortFieldDownClass()).append("\"").toString();
	            }
	            String url;
	            if(isSortField)
	                url = MessageFormat.format(urlspec, new Object[] {
	                    m_fieldname, newDirection
	                });
	            else
	                url = MessageFormat.format(urlspec, new Object[] {
	                    m_fieldname, direction
	                });
	            try
	            {
	            	if(m_sortControlComponent.getIsAjax() && url != null)
	                {
	            		writer.write(" <a href=");
	                writer.write(" \"javascript: callAjaxForSorting('" + url + "','"+m_sortControlComponent.getDivId() + "')");
	                writer.write("\"");
	                } else {
	                writer.write(" <a href=\"");
	                writer.write(url);
	                writer.write("\"");
	                }
	                if(isSortField)
	                    writer.write(classString);
	                writer.write(">");
	            }
	            catch(IOException e)
	            {
	                e.printStackTrace();
	            }
	        } else
	        {
	            LOGGER.error("SortableTag: cannot locate parent SortControlTag.");
	        }
	        return true;
	    }

	    public boolean end(Writer writer, String string)
	    {
	        if(m_sortControlComponent != null)
	            try
	            {
	                writer.write("</a>");
	            }
	            catch(IOException e)
	            {
	                e.printStackTrace();
	            }
	        return super.end(writer, string);
	    }

	    public String getFieldname()
	    {
	        return m_fieldname;
	    }

	    public void setFieldname(String fieldname)
	    {
	        m_fieldname = fieldname;
	    }

	    public XPEDXSortingComponent getSortControlComponent()
	    {
	        return m_sortControlComponent;
	    }

	    public void setSortControlComponent(XPEDXSortingComponent sortControlComponent)
	    {
	        m_sortControlComponent = sortControlComponent;
	    }

	    private static final Logger LOGGER = Logger.getLogger(XPEDXSortableComponent.class);
	    private String m_fieldname;
	    private XPEDXSortingComponent m_sortControlComponent;

	
}
