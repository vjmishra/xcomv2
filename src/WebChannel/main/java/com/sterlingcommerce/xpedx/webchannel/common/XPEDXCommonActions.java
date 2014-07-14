package com.sterlingcommerce.xpedx.webchannel.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.order.XPEDXOrderUtils;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;
import com.yantra.yfs.japi.YFSException;

public class XPEDXCommonActions extends WCMashupAction {
	private String uomCode;
	protected String itemId;

	public String getUomCode() {
		return uomCode;
	}

	public void setUomCode(String uomCode) {
		this.uomCode = uomCode;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public void getUOMDescription() throws XPathExpressionException,
			XMLExceptionWrapper, CannotBuildInputException, IOException {
		String uomDescription = "";
		// XB-687 - start
	    LinkedHashMap<String, String> IsCustomerUomHashMap = new LinkedHashMap<String,String>();
	    if(XPEDXWCUtils.getObjectFromCache("UOMsMap") != null){
	    	IsCustomerUomHashMap = (LinkedHashMap<String, String>) XPEDXWCUtils.getObjectFromCache("UOMsMap");
	    }
		// XB-687 - End
		if (uomCode != null && uomCode.trim().length() > 0 ) {
			if(IsCustomerUomHashMap.get(uomCode)!=null && IsCustomerUomHashMap.get(uomCode).equalsIgnoreCase("Y")){
				uomDescription = uomCode.substring(2, uomCode.length());
			}
			else
				uomDescription = XPEDXWCUtils.getUOMDescription(uomCode);
		}
		getWCContext().getSCUIContext().getResponse().setContentType(
				"text/html");
		PrintWriter out = getWCContext().getSCUIContext().getResponse()
				.getWriter();
		out.print(uomDescription);
		out.flush();
		out.close();
	}
	
	public void getOrderMultiple() throws XPathExpressionException,
			XMLExceptionWrapper, CannotBuildInputException, IOException {
		String orderMultiple = "";
		if (itemId != null && itemId.trim().length() > 0) {
			try {
				orderMultiple = XPEDXOrderUtils.getOrderMultipleForItem(itemId);
			} catch (YFSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (YIFClientCreationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		getWCContext().getSCUIContext().getResponse().setContentType(
				"text/html");
		PrintWriter out = getWCContext().getSCUIContext().getResponse()
				.getWriter();
		out.print(orderMultiple);
		out.flush();
		out.close();
	}
	
	public void getItemUomsAndConversionsAsString() throws XPathExpressionException,
			XMLExceptionWrapper, CannotBuildInputException, IOException {
		Map<String,String> uomMap = new HashMap<String, String>();
		String uomAndConvAsString = "";
		if (itemId != null && itemId.trim().length() > 0) {
			try {
				uomMap = XPEDXOrderUtils.getXpedxUOMList(wcContext.getCustomerId(), itemId, wcContext.getStorefrontId());
				if(uomMap.size()>0) {
					Iterator<String> Iterator = uomMap.keySet().iterator();
					while(Iterator.hasNext()) {
						String UOM = Iterator.next();
						String conv = uomMap.get(UOM);
						uomAndConvAsString = uomAndConvAsString+"|"+UOM+":"+conv;
					}
				}
				
			} catch (YFSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		getWCContext().getSCUIContext().getResponse().setContentType(
				"text/html");
		PrintWriter out = getWCContext().getSCUIContext().getResponse()
				.getWriter();
		out.print(uomAndConvAsString);
		out.flush();
		out.close();
	}

}
