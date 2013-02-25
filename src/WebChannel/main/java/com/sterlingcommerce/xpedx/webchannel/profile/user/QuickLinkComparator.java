package com.sterlingcommerce.xpedx.webchannel.profile.user;

import java.util.Comparator;

public class QuickLinkComparator implements Comparator {
    @Override
    public int compare(Object quickLinkBean1, Object quickLinkBean2) {
	int bean1urlOrder = ((QuickLinkBean) quickLinkBean1).getUrlOrder();

	int bean2urlOrder = ((QuickLinkBean) quickLinkBean2).getUrlOrder();

	if (bean1urlOrder > bean2urlOrder)
	    return 1;

	else if (bean1urlOrder < bean2urlOrder)
	    return -1;

	else
	    return 0;
    }
}
