package com.xpedx.sterling.rcp.pca.util;

import java.util.EventListener;

public interface IXPXCollapsibleCompositeListener extends EventListener
{

    public abstract void panelCollapsed(String s, boolean flag);

    public abstract void panelExpanded(String s, boolean flag);

}
