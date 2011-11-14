package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.sterlingcommerce.webchannel.order.AddCouponAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfc.ui.backend.util.APIManager;

public class XPEDXAddCouponAction extends AddCouponAction{
	public String execute() {
		
		//change for jira 2410 start
		HttpServletRequest httpRequest = wcContext.getSCUIContext().getRequest();
	    HttpSession localSession = httpRequest.getSession();
	    if(httpRequest.getParameter("PoNumberSaveNeeded") != null){
	    	localSession.setAttribute("PoNumberSaveNeeded","checked" );        	
	    }
		return super.execute();
	}
	
	protected Element prepareAndInvokeMashup(String mashupId) throws WCMashupHelper.CannotBuildInputException, APIManager.XMLExceptionWrapper {
		
		//Change method to set only discount calculate true since pna is not required while applying on coupons.
	 	XPEDXWCUtils.setYFSEnvironmentVariablesForDiscounts(wcContext);
        //construct a Set with the passed mashup id
        Set<String> mashupList = new HashSet<String> ();
        mashupList.add(mashupId);
        Map<String, Element> retMap = prepareAndInvokeMashups(mashupList);
        XPEDXWCUtils.releaseEnv(wcContext);
        return retMap.get(mashupId);        
	}
	 
	private String newPoNumber;
	private String SpecialInstructions;
	
	
	public String getSpecialInstructions() {
		return SpecialInstructions;
	}

	public void setSpecialInstructions(String specialInstructions) {
		SpecialInstructions = specialInstructions;
	}

	public String getNewPoNumber() {
		return newPoNumber;
	}

	public void setNewPoNumber(String newPoNumber) {
		this.newPoNumber = newPoNumber;
	}
	 
}
