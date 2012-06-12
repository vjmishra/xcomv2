package com.sterlingcommerce.xpedx.webchannel.common;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.TextUtils;
import com.sterlingcommerce.webchannel.common.Breadcrumb;
import com.sterlingcommerce.webchannel.common.BreadcrumbHelper;
import com.sterlingcommerce.webchannel.common.ParamAttachMapper;


/**
 * This component takes the breadcrumb information processed from the BreadcrumbComponent
 * and display them in the page. It will display the breadcrumbs with the separator. If the
 * removable setting is true, it will display a removable icon (if specified) for the breadcrumbs
 * except for the root and the last. The remove operation will invoke the action captured in the
 * last breadcrumb by passing removing the breadcrumb where the remove is clicked. It is for the 
 * application to determine if it make sense to enable remove capability or not. 
 * 
 * @author chsing
 *
 */
public class XPEDXBreadcrumbDisplayComponent
    extends Component
{
    public static final String DEFAULT_BREADCRUMB_SEPARATOR = " > ";
    public XPEDXBreadcrumbDisplayComponent(ValueStack stck,
            HttpServletRequest req,
            HttpServletResponse res,
            XPEDXBreadcrumbDisplayTag tag)
    {
        super(stck);
        this.req = req;
        this.res = res;
        this.tag = tag;
        if(tag.getStartTabIndex() != null)
        {
            this.tabindex = tag.getStartTabIndex().intValue();
        }
    }
    
    @Override
    public boolean end(Writer writer, String body)
    {
        StringBuffer sb = new StringBuffer();
        List<Breadcrumb> bcl = (List<Breadcrumb>)req.getAttribute(BreadcrumbHelper.BREADCRUMB_LIST);
        if(bcl != null)
        {
            //1: generate output for root breadcrumb
            int rootIndex = this.determineDisplayRootIndex(bcl);
            String rootDisp = this.getRootBreadcrumbDisplay(rootIndex, bcl);
            sb.append(rootDisp);
            if (bcl.size() >1)
            	sb.append(this.getSeparator());

            //2: generate output for regular breadcrumbs
            for(int i = rootIndex + 1; i < bcl.size() - 1; i++)
            {
                // The link for the breadcrum itself
               String disp = this.getBreadcrumbDisplay(i, bcl, null, false);
            	// String disp = this.getBreadcrumbDisplay(i, bcl, "Catalog", false);
            	 /* changed for using Catalog as the root element */
                sb.append(disp);

                // If the breadcrumb is configured to be removable
                if(tag.isRemovable())
                {
                    sb.append(this.getRemoveBreadcrumbDisplay(bcl, i));
                }

                // append the separator
                sb.append(this.getSeparator());
            }

            //3: generate output for last breadcrumb
            //      This won't be generated if there's only the root breadcrumb
            if(bcl.size() - rootIndex > 1)
            {
                String lastDisp = this.getLastBreadcrumbDisplay(bcl);
                sb.append(lastDisp);
              //  sb.append(this.getSeparator());
                /* 3/10/2011 Seperator not required for the last bread crumb */
            }

            try
            {
                writer.append((CharSequence)sb);
            }
            catch(IOException e)
            {
                //log error; throw runtime error
            }
        }

        return super.end(writer, body);
    }

    /**
     * determineDisplayRootIndex try to find the correct breadcrumb to use
     * as root breadcrumb in display.
     * The logic in this implementation is to look for the following
     *  1. Starting from the end of breadcrumb list and searching backwards
     *  2. Find the first breadcrumb whose displayGroup is different then the last one
     * @param bcl
     * @return index of the breadcrumb in the list display root
     */
    protected int determineDisplayRootIndex(List<Breadcrumb> bcl)
    {
        int rootIndex = bcl.size()-1;

        // Find the displayroot in the list
        Breadcrumb displayRoot = bcl.get(rootIndex);
        for(int idx = rootIndex-1; idx >= 0; idx--)
        {
            Breadcrumb bc = bcl.get(idx);
            rootIndex = idx;
            if(displayRoot.getDisplayGroup() != null && displayRoot.getDisplayGroup().equals(bc.getDisplayGroup()))
            {
                displayRoot = bc;
            }
            else
            {
                break;
            }
        }
        
        return rootIndex;
    }

    protected String getRootBreadcrumbDisplay(int rootIndex, List<Breadcrumb> bcl)
    {
        String toReturn = null;

        String displayName = tag.getDisplayRootName();
        displayName = findString(displayName);

        toReturn = this.getBreadcrumbDisplay(rootIndex, bcl, displayName, true);
        return toReturn;
    }

    protected String getBreadcrumbDisplay(int index, List<Breadcrumb> bcl, String displayName, boolean isDisplayRoot)
    {
        String toReturn = null;
        StringBuffer sb = new StringBuffer();
        Breadcrumb bc = bcl.get(index);
        String bcs = null;
        if(index != 0)
        {
            bcs = BreadcrumbHelper.serializeBreadcrumb(bcl, 0, index-1, -1);
            bc.addParam(BreadcrumbHelper.BREADCRUMB_STRING, bcs);  // add the breadcrumb string as param for this particular breadcrumb
        }
        
        String url = this.getBreadcrumbURL(bc);
        bc.removeParam(BreadcrumbHelper.BREADCRUMB_STRING);
        
        if(index == 0)
        {
            // This is the breadcrumb root. We prepare special breadcrumb string for it
            if(url.indexOf("?") < 0)
            {
                url += "?";
            }
            else
            {
                url += "&";
            }
            url +=  BreadcrumbHelper.BREADCRUMB_STRING + "=" + Breadcrumb.NULL_BREADCRUMB_FLAG;
            
            if((bc.getUrl() != null || bc.getUrl().length() > 0) && !bc.getDisplayGroup().equals(bcl.get(bcl.size()-1).getDisplayGroup()))
            {
                // Append parameters if there's any; those must be added by the actions
                // Note: If bc.displayGroup != bcl.get(bcl.size()-1).displayGroup, that means it's the original root defined
                // in the same page, then just use the url as specified. If it's in different page, then allow the padding
                // to go back to the original page with certain states
                Map<String, String> pMap = bc.getParams();
                if(pMap != null && pMap.size() > 0)
                {
                    Iterator<String> it = pMap.keySet().iterator();
                    while(it != null && it.hasNext())
                    {
                        String param = it.next();
                        param += "=" + TextUtils.htmlEncode(pMap.get(param));
                        url += "&" + param;
                    }
                }
            }
        }

        //TODO: resolve display name for root; can come from several places
        String name = displayName == null ? bcl.get(index).getDisplayName() : displayName;
        if(name == null)
        {
            name = tag.getDefaultDisplayName();
        }

        name = TextUtils.htmlEncode(name);
        sb.append("<a href='").append(url);
        sb.append("' tabindex='").append(this.tag.startTabIndex!=null?(""+this.tabindex++):"");
        sb.append("'>").append(name).append("</a>");
        toReturn = sb.toString();
        return toReturn;
    }

    // Assuming this function would be invoked only if it's not
    // the home nor the last breadcrumb.
    // The calculation for last breadcrumb would be done once and cached
    // Subsequent invokation are different only in the index
    protected String getRemoveBreadcrumbDisplay(List<Breadcrumb>bcl, int skippedIndex)
    {
        String toReturn = null;
        String lastBreadcrumbURL = null;
        StringBuffer sb = new StringBuffer();
        int lastIndex = bcl.size()-1;

        // The remove always direct to the last action
        Breadcrumb bc = bcl.get(lastIndex);
        // It's very important to use lastIndex-1 because the last action use only bsc with form 0 to lastIndex-1
        String bcs = BreadcrumbHelper.serializeBreadcrumb(bcl, 0, lastIndex-1, skippedIndex);
        bc.addParam(BreadcrumbHelper.BREADCRUMB_STRING, bcs);  // add the breadcrumb string as param for this particular breadcrumb
        lastBreadcrumbURL = this.getBreadcrumbURL(bc);
        bc.removeParam(BreadcrumbHelper.BREADCRUMB_STRING);

        sb.append("<a href='");
        sb.append(lastBreadcrumbURL);
        sb.append("' tabindex='").append(this.tag.startTabIndex!=null?(""+this.tabindex++):"");
        sb.append("'>");
        String img = this.getActualRemoveIconURL();
        if(img != null && img.length() > 0)
        {
        	sb.append("<img border=\"none\" style=\"display:inline;height: 11px; width: 11px; margin-left: 3px;\" src='").append(this.getActualRemoveIconURL()).append("'></img>");
        	
        }
        else
        {
            //sb.append("<span class='removeBreadcrumb'>&nbsp;</span>");
        	/* Arun 3/10/2011 Changed default value of the bread crumb remove to x */
        	sb.append("&nbsp;x&nbsp;");
        }
        sb.append("</a>");
        toReturn = sb.toString();
        return toReturn;
    }

    protected String getSeparator()
    {
        String breadcrumbSeparator = this.tag.getBreadcrumbSeparator();
        if(breadcrumbSeparator == null)
        {
            breadcrumbSeparator = DEFAULT_BREADCRUMB_SEPARATOR;
        }
        return breadcrumbSeparator;
    }

    protected String getLastBreadcrumbDisplay(List<Breadcrumb> bcl)
    {
        String toReturn = null;
        StringBuffer sb = new StringBuffer();
        Breadcrumb bc = bcl.get(bcl.size()-1);
        
        String name = bc.getDisplayName()==null?tag.getDefaultDisplayName():bc.getDisplayName();
        name = TextUtils.htmlEncode(name);
        sb.append(name);
        toReturn = sb.toString();
        if(!("").equals(toReturn) && (toReturn.indexOf("*") == 0 || toReturn.indexOf("?") == 0)) {
        	toReturn = toReturn.substring(1, toReturn.length());
        }

        return toReturn;
    }

    // Assuming the _bcs_ is not inlcuded
    protected String getBreadcrumbURL(Breadcrumb bc)
    {
        String url = bc.getUrl();
        if(url == null || url.length() == 0)
        {
            Map pparams = (Map)req.getAttribute(ParamAttachMapper.PERSISTENT_PARAMS);
            String bcs = null;
            if(pparams != null)
            {
                bcs = (String)pparams.get(BreadcrumbHelper.BREADCRUMB_STRING);
                pparams.remove(BreadcrumbHelper.BREADCRUMB_STRING);
            }
            url = this.determineActionURL(bc.getAction(), bc.getNameSpace(), null, this.req, this.res, bc.getParams(), null, true, true, false, false);
            if(bcs != null)
            {
                pparams.put(BreadcrumbHelper.BREADCRUMB_STRING, bcs);
            }
        }
        return url;
    }

    protected String getActualRemoveIconURL()
    {
        String actualValue = null;
        String value = this.tag.getRemoveIcon();

        if(value != null && value.length() > 0)
        {
            if (altSyntax()) {
                // the same logic as with findValue(String)
                // if value start with %{ and end with }, just cut it off!
                if (value.startsWith("%{") && value.endsWith("}")) {
                    value = value.substring(2, value.length() - 1);
                }
            }
    
            // exception: don't call findString(), since we don't want the
            //            expression parsed in this one case. it really
            //            doesn't make sense, in fact.
            actualValue = (String) getStack().findValue(value, String.class);
        }

        return actualValue;
    }
    private int tabindex = -1;
    protected HttpServletRequest req = null;
    protected HttpServletResponse res = null;
    protected XPEDXBreadcrumbDisplayTag tag = null;
}
