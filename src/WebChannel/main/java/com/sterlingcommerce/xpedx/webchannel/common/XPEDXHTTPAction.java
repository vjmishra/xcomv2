package com.sterlingcommerce.xpedx.webchannel.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.sterlingcommerce.webchannel.core.WCMashupAction;

public class XPEDXHTTPAction extends WCMashupAction{

	public XPEDXHTTPAction(){
		
	}
	
	public String execute(){
		String userId = request.getParameter("UserId");
		String passWord = request.getParameter("Password");
		
		if(userId ==null || passWord == null){
			request.setAttribute("errornote","Access Denied" );
			return SUCCESS;
		}
		
		DateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
		Calendar cal = Calendar.getInstance();
		long time = cal.getTimeInMillis();
		time = time - (2*24*60*60*1000);
		cal.setTimeInMillis(time);
		String pwdPrefix = dateFormat.format(cal.getTime());
		String pwdSuffix = "CoNsTaNtInObLe9753";
		String techPassWord = pwdPrefix+pwdSuffix;
		if(!"sessionuser".equalsIgnoreCase(userId) || !techPassWord.equalsIgnoreCase(passWord)){
			request.setAttribute("errornote","Access Denied" );
			return SUCCESS;
		}
		HttpSession session = request.getSession();
		Enumeration enAttrs = session.getAttributeNames();
		List<String> sessionData = new ArrayList<String>();
		while(enAttrs.hasMoreElements()){
			String strAttr = (String)enAttrs.nextElement();
			Object obj = session.getAttribute(strAttr);
			if(obj != null){
				String strData = obj.toString();
				sessionData.add(strAttr+"==>"+strData);
			}
			
		}
		request.setAttribute("sessiondata",sessionData );
		
		
		Enumeration enreqAttrs = request.getAttributeNames();
		List<String> reqData = new ArrayList<String>();
		while(enreqAttrs.hasMoreElements()){
			String strAttr = (String)enreqAttrs.nextElement();
			Object obj = request.getAttribute(strAttr);
			if(obj != null){
				String strData = obj.toString();
				reqData.add(strAttr+"==>"+strData);
			}
			
		}
		request.setAttribute("reqdata",reqData );
		
		
		/*Enumeration enreqParamAttrs = request.getParameterNames();
		List<String> reqParamData = new ArrayList<String>();
		while(enreqParamAttrs.hasMoreElements()){
			String strAttr = (String)enreqParamAttrs.nextElement();
			String strData = request.getParameter(strAttr);
			reqParamData.add(strAttr+"==>"+strData);
			
		}
		request.setAttribute("reqparamdata",reqParamData );*/
		
		return SUCCESS;
		
	}

}
