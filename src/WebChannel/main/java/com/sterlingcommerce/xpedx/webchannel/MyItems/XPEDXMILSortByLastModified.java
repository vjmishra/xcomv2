package com.sterlingcommerce.xpedx.webchannel.MyItems;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.w3c.dom.Element;

import com.sterlingcommerce.webchannel.utilities.WCDataDeFormatHelper;
import com.sterlingcommerce.webchannel.utilities.WCDataFormatHelper;
import com.yantra.yfc.date.YDate;

public class XPEDXMILSortByLastModified implements Comparator<Element> {

	private boolean Asc = true;
	
	public XPEDXMILSortByLastModified(boolean asc) {
		super();
		Asc = asc;
	}

	@Override
	public int compare(Element o1, Element o2) {
		int res = 0;
		try {
			
			YDate ydate1 = YDate.newDate();
			YDate ydate2 = YDate.newDate();
			ydate1.setDate(o1.getAttribute("Modifyts"), YDate.ISO_DATETIME_FORMAT, false);
			ydate2.setDate(o2.getAttribute("Modifyts"), YDate.ISO_DATETIME_FORMAT, false);
			
			if (isAsc()){
				res = ydate1.compareTo(ydate2);
			}  else {
				res = ydate2.compareTo(ydate1);
			}
			
		} catch (Exception e) {
			res = 0;
		}
		return res;
	}

	public boolean isAsc() {
		return Asc;
	}

	public void setAsc(boolean asc) {
		Asc = asc;
	}

}
