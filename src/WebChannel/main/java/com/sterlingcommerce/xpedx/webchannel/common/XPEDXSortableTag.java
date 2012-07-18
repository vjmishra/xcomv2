package com.sterlingcommerce.xpedx.webchannel.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ComponentTagSupport;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.TagSupport;

import com.opensymphony.xwork2.util.ValueStack;

public class XPEDXSortableTag  extends ComponentTagSupport
{
	private static final Logger LOG = Logger.getLogger(XPEDXSortableTag.class);
    public XPEDXSortableTag()
    {
        m_fieldname = null;
    }

    public Component getBean(ValueStack valueStack, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
    {
    	XPEDXSortingComponent sortControl = null;
        try
        {
            sortControl = (XPEDXSortingComponent)httpServletRequest.getAttribute("SortControlComponent");
        }
        catch(ClassCastException e)
        {
            LOG.error("Unexpected SortControlComponent type found in request."+ e);
        }
        return new XPEDXSortableComponent(valueStack, sortControl);
    }

    protected void populateParams()
    {
        super.populateParams();
        XPEDXSortableComponent sortableComponent = (XPEDXSortableComponent)component;
        sortableComponent.setFieldname(m_fieldname);
        XPEDXSortingTag parent = findSortCtlParent(this);
        if(parent != null)
            sortableComponent.setSortControlComponent((XPEDXSortingComponent)parent.getComponent());
    }

    XPEDXSortingTag findSortCtlParent(JspTag tag)
    {
        if(tag == null)
            return null;
        if(tag instanceof XPEDXSortingTag)
        {
        	XPEDXSortingTag sortControlTag = (XPEDXSortingTag)tag;
            return sortControlTag;
        }
        if(tag instanceof TagSupport)
        {
            TagSupport simpleTag = (TagSupport)tag;
            return findSortCtlParent(((JspTag) (simpleTag.getParent())));
        } else
        {
            return null;
        }
    }

    public String getFieldname()
    {
        return m_fieldname;
    }

    public void setFieldname(String fieldname)
    {
        m_fieldname = (String)findValue(fieldname);
    }

    private static final Logger LOGGER = Logger.getLogger(XPEDXSortableComponent.class);
    private String m_fieldname;
	
	

}


